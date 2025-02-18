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
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

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
// class DatabaseDelete
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseDelete</code> provides the description of an delete
 ** operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
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
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
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
   ** condition by consecutive invocation of {@link #execute()}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated the {@link DatabaseStatement}.
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **
   ** @return                    an instance of <code>DatabaseDelete</code>
   **                            with the properties provided.
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
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public void execute(final Connection connection)
    throws DatabaseException {

    try {
      prepare(connection);
      execute();
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
   **
   ** @return                    the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
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
    try {
      this.query     = prepareDelete(this.entity, this.filter, this.parameter);
      this.statement = createPreparedStatement(connection, this.query);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes entries from the {@link DatabaseEntity} this context belongs to.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public void execute()
    throws DatabaseException {

    execute(this.parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Deletes entries from the {@link DatabaseEntity} this context belongs to.
   **
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public void execute(final List<DatabaseParameter> parameter)
    throws DatabaseException {

    if (this.statement == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "statement");

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    debug(method, DatabaseBundle.format(DatabaseMessage.EXECUTE_STATEMENT, this.query));
    try {
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of delete
      for (int i = 0; i < parameter.size(); i++) {
        final DatabaseParameter value = parameter.get(i);
        switch (value.type()) {
          case Types.BOOLEAN   : this.statement.setBoolean(i + 1, value.booleanValue());
                                 break;
          case Types.INTEGER   : this.statement.setInt(i + 1, value.integerValue());
                                 break;
          case Types.NUMERIC   : this.statement.setLong(i + 1, value.longValue());
                                 break;
          case Types.DOUBLE    : this.statement.setDouble(i + 1, value.doubleValue());
                                 break;
          case Types.FLOAT     : this.statement.setFloat(i + 1, value.floatValue());
                                 break;
          case Types.DECIMAL   : this.statement.setBigDecimal(i + 1, value.bigDecimalValue());
                                 break;
          case Types.DATE      : this.statement.setDate(i + 1, new java.sql.Date(value.dateValue().getTime()));
                                 break;
          case Types.TIME      : this.statement.setTime(i + 1, new java.sql.Time(value.dateValue().getTime()));
                                 break;
          case Types.TIMESTAMP : this.statement.setTimestamp(i + 1, new java.sql.Timestamp(value.longValue()));
                                 break;
          case Types.VARCHAR   : this.statement.setString(i + 1, value.stringValue());
                                 break;
          default              : this.statement.setObject(i + 1, value.value());
        }
      }

      int affected = this.statement.executeUpdate();
      // on delete more than one entries can be affected thus we have to test
      // for at least one
      if (affected == 0) {
        final String[] arguments = {this.filter.toString(), this.entity.toString()};
        throw new DatabaseException(DatabaseError.OBJECT_NOT_DELETED, arguments);
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
}