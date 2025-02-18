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

    File        :   DatabaseUpdate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseUpdate.


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

import java.sql.Types;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseUpdate
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseUpdate</code> provides the description of an update
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseUpdate extends DatabaseModify {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseUpdate</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   */
  private DatabaseUpdate(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding) {
    // ensure inheritance
    this(loggable, entity, filter, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseUpdate</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the names of the attributes that are part of
   **                            this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   */
  private DatabaseUpdate(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding, final String[] returning) {
    // ensure inheritance
    super(loggable, entity, filter, binding, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (DatabaseStatement)
  /**
   ** Builds a UPDATE-Statement for the {@link DatabaseEntity} this context
   ** belongs that will return the attributes specified by
   ** <code>returning</code>.
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
    super.prepare(connection, prepareUpdate(this.entity, this.filter, this.parameter, this.binding, this.returning));
    trace(method, SystemMessage.METHOD_EXIT);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Builds and executes an UPDATE-Statement for the {@link DatabaseEntity}
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
  // Method:   execute (DatabaseModify)
  /**
   ** Updates in the {@link DatabaseEntity} this context belongs to and binds
   ** the passed attribute <code>data</code> to it.
   **
   ** @param  data               the attributes of the entry to modify.
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
      int k = 1;
      for (String name : this.binding) {
        this.statement.setObject(k++, data.get(name));
      }
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of update
      for (int j = 0; j < this.parameter.size(); j++ ) {
        final DatabaseParameter parameter = this.parameter.get(j);
        switch (parameter.type()) {
          case Types.BOOLEAN   : this.statement.setBoolean(k++, parameter.booleanValue());
                                 break;
          case Types.INTEGER   : this.statement.setInt(k++, parameter.integerValue());
                                  break;
          case Types.NUMERIC   : this.statement.setLong(k++, parameter.longValue());
                                 break;
          case Types.DOUBLE    : this.statement.setDouble(k++, parameter.doubleValue());
                                 break;
          case Types.FLOAT     : this.statement.setFloat(k++, parameter.floatValue());
                                 break;
          case Types.DECIMAL   : this.statement.setBigDecimal(k++, parameter.bigDecimalValue());
                                 break;
          case Types.DATE      : this.statement.setDate(k++, new java.sql.Date(parameter.dateValue().getTime()));
                                 break;
          case Types.TIME      : this.statement.setTime(k++, new java.sql.Time(parameter.dateValue().getTime()));
                                 break;
          case Types.TIMESTAMP : this.statement.setTimestamp(k++, new java.sql.Timestamp(parameter.longValue()));
                                 break;
          case Types.VARCHAR   : this.statement.setString(k++, parameter.stringValue());
                                 break;
          default              : this.statement.setObject(k++, parameter.value());
        }
      }

      debug(method, DatabaseBundle.format(DatabaseMessage.EXECUTE_STATEMENT, query));
      int affected = this.statement.executeUpdate();
      // on update more than one entries can be affected thus we have to test
      // for at least one
      if (affected == 0) {
        final String[] arguments = {this.filter.toString(), this.entity.toString()};
        throw new DatabaseException(DatabaseError.OBJECT_NOT_MODIFIED, arguments);
      }
    }
    catch (SQLSyntaxErrorException e) {
      error(method, DatabaseBundle.format(DatabaseError.SYNTAX_INVALID, this.query));
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseUpdate</code> that can be
   ** applied on Database Servers and modifies records that match the filter
   ** condition by consecutive invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **
   ** @return                    an instance of <code>DatabaseUpdate</code>
   **                            with the properties provided.
   */
  public static DatabaseUpdate build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding) {
    return new DatabaseUpdate(loggable, entity, filter, binding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseUpdate</code> that can be
   ** applied on Database Servers and modifies records that match the filter
   ** condition by consecutive invocation of {@link #execute(Map)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the names of the attributes that are part of
   **                            this entity operation.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    an instance of <code>DatabaseUpdate</code>
   **                            with the properties provided.
   */
  public static DatabaseUpdate build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final Set<String> binding, final String[] returning) {
    return new DatabaseUpdate(loggable, entity, filter, binding, returning);
  }
}