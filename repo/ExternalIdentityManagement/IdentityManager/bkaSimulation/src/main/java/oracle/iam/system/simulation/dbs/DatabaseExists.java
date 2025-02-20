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

    File        :   DatabaseExists.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseExists.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.OracleResultSet;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseExists
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseExists</code> provides the description of a query that
 ** can be applied on Database Servers and returns one records that match the
 ** filter condition at once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseExists extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseExists</code> that can be applied on Database
   ** Servers and returns all records that match the filter condition at once.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   */
  private DatabaseExists(final DatabaseEntity entity, final DatabaseFilter filter) {
    // ensure inheritance
    super(entity, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseExists</code> that can be
   ** applied on Database Servers and returns all records that match the filter
   ** condition at once.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   **
   ** @return                    an instance of <code>DatabaseExists</code>
   **                            with the properties provided.
   */
  public static DatabaseExists build(final DatabaseEntity entity, final DatabaseFilter filter) {
    return new DatabaseExists(entity, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes an SELECT-EXISTS-Statement for the
   ** {@link DatabaseEntity} this context belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    <code>true</code> if an entry in the database
   **                            match the filter expression of this statement.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public boolean execute(final Connection connection)
    throws DatabaseException {

    try {
      prepare(connection);
      return execute();
    }
    finally {
      // we have to close the statement after execution is done and free all
      // allocated resources
      close();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare (overridden)
  /**
   ** Builds a SELECT-EXISTS-Statement for the {@link DatabaseEntity} this
   ** context belongs to, that will return the existance of a record which match
   ** the <code>filter</code> passed to the constructor.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @return                    the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException {

    // prevent bogus input
    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    // prevent bogus state
    if (this.statement != null)
      throw new DatabaseException(DatabaseError.INSTANCE_ILLEGAL_STATE, "statement");

    this.parameter = new ArrayList<DatabaseParameter>();
    this.query     = prepareExists(this.entity, this.filter, this.parameter);
    this.statement = createPreparedStatement(connection, this.query);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @return                    <code>true</code> if an entry in the database
   **                            match the filter expression of this statement.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public boolean execute()
    throws DatabaseException {

    return execute(this.parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   **
   ** @return                    <code>true</code> if an entry in the database
   **                            match the filter expression of this statement.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public boolean execute(final List<DatabaseParameter> parameter)
    throws DatabaseException {

    if (this.statement == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "statement");

    OracleResultSet resultSet = null;
    try {
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of select
      for (int i = 0; i < parameter.size(); i++) {
        final DatabaseParameter value = parameter.get(i);
        switch (value.type()) {
          case OracleTypes.DATE    : statement.setDate(i + 1, new java.sql.Date(value.dateValue().getTime()));
                                     break;
          case OracleTypes.TIME    : statement.setTime(i + 1, new java.sql.Time(value.dateValue().getTime()));
                                     break;
          case OracleTypes.FLOAT   : statement.setFloat(i + 1, value.floatValue());
                                     break;
          case OracleTypes.DECIMAL : statement.setBigDecimal(i + 1, value.bigDecimalValue());
                                     break;
          case OracleTypes.INTEGER : statement.setInt(i + 1, value.integerValue());
                                     break;
          case OracleTypes.VARCHAR : statement.setString(i + 1, value.stringValue());
                                     break;
          default                  : statement.setObject(i + 1, value.value());
        }
      }

      resultSet = (OracleResultSet)statement.executeQuery();
      // there couldn't not more than one record in the returning clause
      resultSet.next();
      return !resultSet.isAfterLast();
    }
    catch (SQLSyntaxErrorException e) {
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
      closeResultSet(resultSet);
    }
  }
}