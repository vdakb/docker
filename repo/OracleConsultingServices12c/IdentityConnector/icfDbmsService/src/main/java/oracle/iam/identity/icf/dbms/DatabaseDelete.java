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

    File        :   DatabaseDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseDelete
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseDelete</code> provides the description of an delete
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseDelete extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseDelete</code> that can be applied on Database
   ** Servers and deletes records that match the filter condition by
   ** consecutive invocation of {@link #execute()}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   */
  private DatabaseDelete(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter) {
    // ensure inheritance
    super(loggable, entity, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseDelete</code> that can be
   ** applied on Database Servers and deletes records that match the filter
   ** condition by consecutive invocation of {@link #execute(boolean, int)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   **
   ** @return                    an instance of <code>DatabaseDelete</code>
   **                            with the properties provided.
   **                            <br>
   **                            Possible object is <code>DatabaseDelete</code>.
   */
  public static DatabaseDelete build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter) {
    return new DatabaseDelete(loggable, entity, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds a DELETE-Statement for the {@link DatabaseEntity} this context
   ** belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
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
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public void execute(final Connection connection, final boolean strict, final int timeOut)
    throws SystemException {

    try {
      prepare(connection);
      execute(strict, timeOut);
    }
    finally {
      // we have to close the statement after execution is done and free all
      // allocated resources
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Builds a DELETE-Statement for the {@link DatabaseEntity} this context
   ** belongs.
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

    final String method ="prepare";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      this.parameter = new ArrayList<DatabaseParameter>();
      this.query     = prepareDelete(this.entity, this.filter, this.parameter);
      this.statement = createPreparedStatement(connection, this.query);
      return this.parameter;
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes entries from the {@link DatabaseEntity} this context belongs to.
   **
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
   ** @throws SystemException    if the operation fails:
   */
  public void execute(final boolean strict, final int timeOut)
    throws SystemException {

    if (this.statement == null)
      throw SystemException.attributeNull("statement");

    final String method = "execute";
    trace(method, Loggable.METHOD_ENTRY);
    try {
      applyParameter(this.statement, 1, this.parameter);
      // set timeout period we will wait for a response
      this.statement.setQueryTimeout(timeOut == -1 ? 0 : timeOut);
      debug(method, DatabaseBundle.string(DatabaseMessage.EXECUTE_STATEMENT, this.query));
      final int affected = this.statement.executeUpdate();
      // on delete more than one entries can be affected thus we have to test
      // for at least one and if strict mode is requested
      if (strict && affected == 0)
        throw DatabaseException.notDeleted(this.parameter.toString(), this.entity.id);
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
      throw DatabaseException.normalized(this.statement, e);
    }
    finally {
      trace(method, Loggable.METHOD_EXIT);
    }
  }
}