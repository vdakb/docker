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

    File        :   DatabaseSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.resource.DatabaseBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseSearch
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseSearch</code> provides the description of a query that
 ** can be applied on Database Servers an populates records by pagination.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseSearch extends DatabaseSelect {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseSearch</code> that can be applied on Database
   ** Servers and returns records that match the filter condition and iterates
   ** over the resultset by consecutive invocation of
   ** {@link #execute(long, long)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
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
  public DatabaseSearch(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    // ensure inheritance
    super(loggable, entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DatabaseSearch</code> that can be
   ** applied on Database Servers and returns records that match the filter
   ** condition and iterates over the resultset by consecutive invocation of
   ** {@link #execute(long, long)}.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link DatabaseStatement}.
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
  public static DatabaseSearch build(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning) {
    return new DatabaseSearch(loggable, entity, filter, returning);
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
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  @Override
  public List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException {

    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    final String method ="prepare";
    trace(method, SystemMessage.METHOD_ENTRY);

    this.parameter = new ArrayList<DatabaseParameter>();
    try {
      this.query     = "SELECT * FROM (" + prepareSelect(this.entity, this.filter, this.parameter, this.returning) +") WHERE rownumber between ? and ?";
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
   ** Searches in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public List<Map<String, Object>> execute(final long startRow, final long lastRow)
    throws DatabaseException {

    return execute(this.parameter, startRow, lastRow);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Searches in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   ** @param  startRow           the start row of a page to fetch from the
   **                            Database Server.
   ** @param  lastRow            the last row of a page to fetch from the
   **                            Database Server.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public List<Map<String, Object>> execute(final List<DatabaseParameter> parameter, final long startRow, final long lastRow)
    throws DatabaseException {

    if (this.statement == null)
      throw new DatabaseException(DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL, "statement");

    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      // set the bindings for the pagination
      this.statement.setLong(parameter.size() + 1, startRow);
      this.statement.setLong(parameter.size() + 2, lastRow);
      return super.execute(parameter);
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