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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

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
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseUpdate
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseUpdate</code> provides the description of an update
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
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
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            updated into.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            updated into.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  binding            the {@link Set} of names of the attributes that
   **                            are part of this entity operation.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the update. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **                            <br>
   **                            Allowed object is array of {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    the {@link DatabaseParameter} need to bind at
   **                            the execution time of this
   **                            {@link DatabaseStatement}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws SystemException {

    if (connection == null)
      throw SystemException.argumentNull("connection");

    if (this.statement != null)
      throw SystemException.attributeNull("statement");

    final String method ="prepare";
    trace(method, Loggable.METHOD_ENTRY);

    this.parameter = new ArrayList<DatabaseParameter>();
    super.prepare(connection, prepareUpdate(this.entity, this.filter, this.parameter, this.binding, this.returning));
    trace(method, Loggable.METHOD_EXIT);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (DatabaseModify)
  /**
   ** Builds and executes an UPDATE-Statement for the {@link DatabaseEntity}
   ** this context belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  data               the attributes of the entry to update.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} for the value.
   ** @param  strict             <code>true</code> if the result of the
   **                            operations needs to be verified that at least
   **                            one record is affected in the database.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws SystemException  if the operation fails
   */
  @Override
  public void execute(final Connection connection, final Map<String, Object> data, final boolean strict, final int timeOut)
    throws SystemException {

    try {
      prepare(connection);
      execute(data, strict, timeOut);
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
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} for the value.
   ** @param  strict             <code>true</code> if the result of the
   **                            operations needs to be verified that at least
   **                            one record is affected in the database.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws SystemException    if the operation fails.
   */
  @Override
  public void execute(final Map<String, Object> data, final boolean strict, final int timeOut)
    throws SystemException {

    if (this.statement == null)
      throw SystemException.attributeNull("statement");

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      int k = 1;
      for (String name : this.binding) {
        this.statement.setObject(k++, data.get(name));
      }
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of update
      applyParameter(this.statement, k, this.parameter);
      // set timeout period we will wait for a response
      this.statement.setQueryTimeout(timeOut == -1 ? 0 : timeOut);
      debug(method, DatabaseBundle.string(DatabaseMessage.EXECUTE_STATEMENT, this.query));
      int affected = this.statement.executeUpdate();
      // on update more than one entries can be affected thus we have to test
      // for at least one and if strict mode is requested
      if (strict && affected == 0) {
        throw DatabaseException.notModified(this.parameter.toString(), this.entity.id);
      }
    }
    catch (SQLSyntaxErrorException e) {
      final DatabaseException t = DatabaseException.syntax(this.query);
      error(method, t.getMessage());
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw t;
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw DatabaseException.normalized(statement, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
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
   ** condition by consecutive invocation of
   ** {@link #execute(Map, boolean, int)}.
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
   ** condition by consecutive invocation of
   ** {@link #execute(Map, boolean, int)}.
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