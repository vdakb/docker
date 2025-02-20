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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseModify.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseModify.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import java.sql.Time;
import java.sql.Types;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import oracle.sql.RAW;

import oracle.hst.foundation.object.Pair;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseModify
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseModify</code> provides the description of insert and
 ** update operations that can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class DatabaseModify extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final List<Pair<String, DatabaseParameter>> binding;
  protected final Collection<Pair<String, Integer>>     returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseModify</code> that can be applied on Database
   ** Servers and create records that match the filter condition by consecutive
   ** invocation of {@link #execute{Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   */
  protected DatabaseModify(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding) {
    // ensure inheritance
    this(entity, filter, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseModify</code> that can be applied on Database
   ** Servers and modifies records that match the filter condition by consecutive
   ** invocation of {@link #execute{Map)}.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  binding            the {@link List} of named-value pair attributes
   **                            that are part of this entity operation.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   */
  protected DatabaseModify(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    // ensure inheritance
    super(entity, filter);

    // initailize instance attributes
    this.binding   = binding;
    this.returning = returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes an INSERT/UPDATE-Statement for the
   ** {@link DatabaseEntity} this context belongs to.
   **
   ** @param  connection         the JDBC connection to prepare and execute the
   **                            statement for.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Pair&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public final Map<String, Object> execute(final Connection connection)
    throws DatabaseException {

    prepare(connection);
    return execute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes an INSERT/UPDATE-Statement for the
   ** {@link DatabaseEntity} this context belongs to.
   **
   ** @return                    a {@link Map} where each element contains the
   **                            value for each element defined by the alias
   **                            (the string with index <code>1</code>) in
   **                            <code>returning</code>; never <code>null</code>.
   **
   ** @throws DatabaseException  if the operation fails
   */
  protected abstract Map<String, Object> execute()
    throws DatabaseException;

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (DatabaseStatement)
  /**
   ** Builds an INSERT/UPDATE-Statement for the {@link DatabaseEntity} this
   ** context belongs.
   **
   ** @param  connection         the JDBC connection to prepare and execute the
   **                            statement for.
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

    // prevent bogus state
    if (this.statement != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "statement");

    this.parameter = new ArrayList<DatabaseParameter>();
    this.query     = prepareModify();
    this.statement = createPreparedStatement(connection, this.query);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareModify
  /**
   ** Prepares an INSERT/UPDATE-Statement for the {@link DatabaseEntity} this
   ** context belongs.
   **
   ** @return                    the statement string ready for bind and
   **                            execution operations.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  protected abstract String prepareModify()
    throws DatabaseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populate
  /**
   ** Builds a SQL-Statement for the {@link DatabaseEntity} this context belongs
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  collector          the {@link Map} receiving the values populated
   **                            from the returned {@link ResultSet}.
   **
   ** @throws DatabaseException  if the collector could not be filled with the
   **                            values populated from the returning result set.
   */
  protected void populate(final Map<String, Object> collector)
    throws DatabaseException {

    try {
      final ResultSet resultSet = this.statement.getReturnResultSet();
      // there couldn't not more than one record in the returning clause
      resultSet.next();
      int j = 1;
      for (Pair<String, Integer> cursor : this.returning) {
        final Object value = resultSet.getObject(j++);
        switch (cursor.value.intValue()) {
          case Types.BINARY    : collector.put(cursor.tag, resultSet.wasNull() ? null : new RAW((byte[])value).stringValue());
                                 break;
          case Types.DATE      : collector.put(cursor.tag, resultSet.wasNull() ? null : (Date)value);
                                 break;
          case Types.TIME      : collector.put(cursor.tag, resultSet.wasNull() ? null : fromTime((Time)value));
                                 break;
          case Types.TIMESTAMP : collector.put(cursor.tag, resultSet.wasNull() ? null : fromTimestamp((Timestamp)value));
                                 break;
          case Types.FLOAT     : collector.put(cursor.tag, resultSet.wasNull() ? null : Float.valueOf(value.toString()));
                                 break;
          case Types.DECIMAL   : collector.put(cursor.tag, resultSet.wasNull() ? null : Double.valueOf(value.toString()));
                                 break;
          case Types.INTEGER   : collector.put(cursor.tag, resultSet.wasNull() ? null : Integer.valueOf(value.toString()));
                                 break;
          case Types.VARCHAR   : collector.put(cursor.tag, resultSet.wasNull() ? null : String.valueOf(value));
                                 break;
          default              : collector.put(cursor.tag, resultSet.wasNull() ? null : value);
        }
      }
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw new DatabaseException(e);
    }
  }

  private static Date fromTime(final Time value) {
    final Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(value);
    return calendar.getTime();
  }

  private static Date fromTimestamp(final Timestamp value) {
    return new Date(value.getTime() + (value.getNanos() / 1000000));
  }
}