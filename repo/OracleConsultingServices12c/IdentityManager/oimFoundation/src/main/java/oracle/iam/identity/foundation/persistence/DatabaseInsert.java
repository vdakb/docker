/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DatabaseInsert.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseInsert.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseInsert
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseInsert</code> provides the description of an insert
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseInsert extends DatabaseModify {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseInsert</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   */
  private DatabaseInsert(final Loggable loggable, final DatabaseEntity entity, final Set<String> binding) {
    // ensure inheritance
    this(loggable, entity, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseInsert</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   */
  private DatabaseInsert(final Loggable loggable, final DatabaseEntity entity, final Set<String> binding, final String[] returning) {
    // ensure inheritance
    super(loggable, entity, null, binding, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (DatabaseStatement)
  /**
   ** Builds an INSERT-Statement for the {@link DatabaseEntity} this context
   ** belongs.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException {

    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    if (this.statement != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "statement");

    final String method ="prepare";
    trace(method, SystemMessage.METHOD_ENTRY);

    this.parameter = new ArrayList<DatabaseParameter>();
    super.prepare(connection, prepareInsert(this.entity, this.binding, this.returning));
    trace(method, SystemMessage.METHOD_EXIT);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Inserts in the {@link DatabaseEntity} this context belongs to and binds
   ** the passed attribute <code>data</code> to it.
   **
   ** @param  data               the attributes of the entry to create.
   **
   ** @throws DatabaseException  if the operation fails
   */
  @Override
  public void execute(final Map<String, Object> data)
    throws DatabaseException {

    if (this.statement == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "statement");

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      int j = 1;
      for (Map.Entry<String, Object> value : data.entrySet())
        this.statement.setObject(j++, value.getValue());

      debug(method, DatabaseBundle.format(DatabaseMessage.EXECUTE_STATEMENT, query));
      int affected = this.statement.executeUpdate();
      // on create more than one entries can be affected thus we have to test
      // for at least one
      if (affected == 0)
        throw new DatabaseException(DatabaseError.OBJECT_NOT_CREATED, entity.toString());
    }
    catch (SQLSyntaxErrorException e) {
      error(method, DatabaseBundle.format(DatabaseError.SYNTAX_INVALID, query));
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw new DatabaseException(e);
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw new DatabaseException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Builds and executes an INSERT-Statement for the {@link DatabaseEntity}
   ** this context belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   ** @param  data               the attributes of the entry to modify.
   **
   ** @throws DatabaseException  if the operation fails
   */
  @Override
  public void execute(final Connection connection, final Map<String, Object> data)
    throws DatabaseException {

    try {
      prepare(connection);
      execute(data);
    }
    finally {
      // we have to close the statement after execution is done and free all
      // allocated resources
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseInsert</code> that can be
   ** applied on Database Servers and create records that match the filter
   ** condition by consecutive invocation of {@link #execute(Map)}.
    **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **
   ** @return                    an instance of <code>DatabaseInsert</code>
   **                            with the properties provided.
   */
  public static DatabaseInsert build(final Loggable loggable, final DatabaseEntity entity, final Set<String> binding) {
    return new DatabaseInsert(loggable, entity, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseInsert</code> that can be
   ** applied on Database Servers and create records that match the filter
   ** condition by consecutive invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    an instance of <code>DatabaseInsert</code>
   **                            with the properties provided.
   */
  public static DatabaseInsert build(final Loggable loggable, final DatabaseEntity entity, final Set<String> binding, final String[] returning) {
    return new DatabaseInsert(loggable, entity, binding, returning);
  }
}