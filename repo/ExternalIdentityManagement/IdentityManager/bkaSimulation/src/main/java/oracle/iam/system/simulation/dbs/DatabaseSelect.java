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

    File        :   DatabaseSelect.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSelect.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseSelect
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseSelect</code> provides the description of a query that
 ** can be applied on Database Servers and returns all records that match the
 ** filter condition at once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseSelect extends DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final List<Pair<String, String>> returning;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseSelect</code> that can be applied on Database
   ** Servers and returns all records that match the filter condition at once.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   */

  public DatabaseSelect(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    // ensure inheritance
    super(entity, filter);

    // initailize instance attributes
    this.returning = returning;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseSelect</code> that can be
   ** applied on Database Servers and returns all records that match the filter
   ** condition at once.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **
   ** @return                    an instance of <code>DatabaseSelect</code>
   **                            with the properties provided.
   */
  public static DatabaseSelect build(final DatabaseEntity entity, final DatabaseFilter filter, final Pair<String, String>... returning) {
    return new DatabaseSelect(entity, filter, CollectionUtility.list(returning));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseSelect</code> that can be
   ** applied on Database Servers and returns all records that match the filter
   ** condition at once.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   ** @param  returning          the attributes of the entity to be returned by
   **                            the search. The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **
   ** @return                    an instance of <code>DatabaseSelect</code>
   **                            with the properties provided.
   */
  public static DatabaseSelect build(final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    return new DatabaseSelect(entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Builds and executes a SELECT-Statement for the {@link DatabaseEntity} this
   ** context belongs to that will return the attributes specified by
   ** <code>attribute</code>.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Pair&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public List<List<Pair<String, Object>>> execute(final Connection connection)
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
  // Method:   prepare
  /**
   ** Builds a SELECT-Statement for the {@link DatabaseEntity} this context
   ** belongs to, that will return the attributes specified by
   ** <code>returning</code> passed to the constructor.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @return                    the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException {

    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    if (this.returning == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "returning");

    this.parameter = new ArrayList<DatabaseParameter>();
    this.query     = prepareSelect(this.entity, this.filter, this.parameter, this.returning);
    this.statement = createPreparedStatement(connection, this.query);
    return this.parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Pair&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>; never
   **                            <code>null</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public List<List<Pair<String, Object>>> execute()
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
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Pair&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public List<List<Pair<String, Object>>> execute(final List<DatabaseParameter> parameter)
    throws DatabaseException {

    if (this.statement == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "statement");

    return execute(this.statement, parameter, this.returning);
  }
}