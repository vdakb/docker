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

    File        :   DatabaseEntity.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.object.Pair;

import oracle.jdbc.OraclePreparedStatement;

////////////////////////////////////////////////////////////////////////////////
// abstract class DatabaseStatement
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseStatement</code> provides the description of a query that
 ** can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public abstract class DatabaseStatement {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final short         QUERY_TIMEOUT = 20;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DatabaseEntity    entity;
  protected final DatabaseFilter    filter;

  protected String                  query;
  protected List<DatabaseParameter> parameter;
  protected OraclePreparedStatement statement;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseStatement</code> .
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement.
   */
  public DatabaseStatement(final DatabaseEntity entity, final DatabaseFilter filter) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.entity = entity;
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the {@link DatabaseFilter} this context belongs to.
   **
   ** @return                    the {@link DatabaseFilter} this context
   **                            belongs to.
   */
  public final DatabaseFilter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the {@link DatabaseEntity} this context belongs to.
   **
   ** @return                    the {@link DatabaseEntity} this context
   **                            belongs to.
   */
  public final DatabaseEntity entity() {
    return this.entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   statement
  /**
   ** Returns the statement string this context belongs to.
   **
   ** @return                    the statement string this context belongs to.
   */
  public final String statement() {
    return this.query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreparedStatement
  /**
   ** Creates a new {@link OraclePreparedStatement} for sending parameterized
   ** SQL statements to the database.
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link OraclePreparedStatement} object. This object can then
   ** be used to efficiently execute this statement multiple times.
   ** <p>
   ** Result sets created using the returned {@link OraclePreparedStatement}
   ** object will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling
   ** <code>getHoldability()</code>.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **
   ** @return                    a new default {@link OraclePreparedStatement}
   **                            object containing the pre-compiled SQL
   **                            statement.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final OraclePreparedStatement createPreparedStatement(final Connection connection, final String string)
    throws DatabaseException {

    // prevent bogus input
    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    OraclePreparedStatement statement = null;
    try {
      statement = (OraclePreparedStatement)connection.prepareStatement(string);
      statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreparedStatement
  /**
   ** Creates a {@link OraclePreparedStatement} object that will generate
   ** <code>ResultSet</code> objects with the given <code>type</code> and
   ** <code>concurrency</code>. This method is the same as the prepareStatement
   ** method above, but it allows the default result set type and concurrency to
   ** be overridden. The holdability of the created result sets can be
   ** determined by calling getHoldability().
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link OraclePreparedStatement} object. This object can then
   ** be used to efficiently execute this statement multiple times.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>TYPE_FORWARD_ONLY
   **                              <li>TYPE_SCROLL_INSENSITIVE
   **                              <li>TYPE_SCROLL_SENSITIVE
   **                            </ul>
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>CONCUR_READ_ONLY
   **                              <li>CONCUR_UPDATABLE
   **                            </ul>
   **
   ** @return                    a new {@link OraclePreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce <code>ResultSet</code> objects
   **                            with the given <code>type</code> and
   **                            <code>concurrency</code>.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final OraclePreparedStatement createPreparedStatement(final Connection connection, final String string, int type, int concurrency)
    throws DatabaseException {

    // prevent bogus input
    if (connection == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "connection");

    OraclePreparedStatement statement = null;
    try {
      statement = (OraclePreparedStatement)connection.prepareStatement(string, type, concurrency);
      statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeStatement
  /**
   ** Close a previous prepared statement
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   **
   ** @param  statement          the {@link OraclePreparedStatement} to close.
   */
  public static final void closeStatement(final OraclePreparedStatement statement) {
    // prevent bogus input
    if (statement == null)
      return;

    try {
      closeResultSet(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
    }
    finally {
      try {
        statement.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeResultSet
  /**
   ** Close a JDBC resultset.
   **
   ** @param  resultSet          the {@link ResultSet} to close.
   */
  public static final void closeResultSet(final ResultSet resultSet) {
    // prevent bogus input
    if (resultSet != null)
      try {
        resultSet.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareExists
  /**
   ** Builds and returns a SELECT-EXISTS-Statement for the specified
   ** <code>entity</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareExists(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT 'x' FROM dual WHERE EXISTS (");
    builder.append(prepareSelect(entity, filter, argument, null)).append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareCount
  /**
   ** Builds and returns a SELECT-COUNT-Statement for the specified
   ** <code>entity</code> that will return the amount of records matching the
   ** spefified filter.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareCount(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT COUNT(");
    builder.append(entity.primary());
    builder.append(") as RS FROM ").append(entity.type());
    if (filter != null) {
      builder.append(" WHERE ");
      builder.append(prepareFilter(filter, argument));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSelect
  /**
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSelect(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument, final List<Pair<String, String>> returning)
    throws DatabaseException {
    
    return prepareSelect(entity, filter, null, argument, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSelect
  /**
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   ** @param  sort               the sort order criteria to build for the select
   **                            statement.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSelect(final DatabaseEntity entity, final DatabaseFilter filter, final DatabaseSort sort, final List<DatabaseParameter> argument, final List<Pair<String, String>> returning)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT ");
    if (returning != null) {
      for (int i = 0; i < returning.size(); i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(String.format("%s AS %s", returning.get(i).tag, returning.get(i).value));
      }
    }
    // regardless if a paginated result set will be used or not at this time we
    // don't know how the select statement will be used hence we are always
    // selecting the row number pseudo column to ensure that we can use the
    // statement in a paginated query
    if (returning != null)
      builder.append(',');
    builder.append(String.format("%s AS %s", "rownum", "rownumber"));

    builder.append(" FROM ").append(entity.type());
    if (filter != null) {
      builder.append(" WHERE ");
      builder.append(prepareFilter(filter, argument));
    }
    if (sort != null) {
      builder.append(" ORDER BY ");
      builder.append(prepareOrder(sort));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSelect
  /**
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entities the information has to be
   **                            fetched from.
   ** @param  join               the filter criteria to join the entities.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSelect(final DatabaseEntity[] entity, final DatabaseFilter join, final DatabaseFilter filter, final List<DatabaseParameter> argument)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT ");
    for (int i = 0; i < entity.length; i++) {
      final List<Pair<String, String>> returning = entity[i].returning();
      for (int j = 0; j < returning.size(); j++) {
        if (i > 0 || j > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(String.format("%s AS %s", returning.get(j).tag, returning.get(j).value));
      }
    }
    // regardless if a paginated result set will be used or not at this time we
    // don't know how the select statement will be used hence we are always
    // selecting the row number pseudo column to ensure that we can use the
    // statement in a paginated query
    builder.append(String.format(",%s AS %s", "rownum", "rownumber"));
    builder.append(" FROM ");
    for (int i = 0; i < entity.length; i++) {
      if (i > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(entity[i].type());
    }
    if (join != null || filter != null) {
      DatabaseFilter criteria = null;
      if (join != null && filter == null)
        criteria = join;
      else if (join == null && filter != null)
        criteria = filter;
      else
        criteria = DatabaseFilter.build(join, filter, DatabaseFilter.Operator.AND);
      builder.append(" WHERE ");
      builder.append(prepareFilter(criteria, argument));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareInsert
  /**
   ** Builds and returns an INSERT-Statement for the specified
   ** <code>entity</code> that will write the attributes specified by
   ** <code>data</code> to the database.
   **
   ** @param  entity             the database entity the information has to be
   **                            modified from.
   ** @param  binding            the names of the attributes to be inserted by
   **                            the statement.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   */
  public static String prepareInsert(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding) {
    return prepareInsert(entity, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareInsert
  /**
   ** Builds and returns an INSERT-Statement for the specified
   ** <code>entity</code> that will return the attributes specified by
   ** <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            written to.
   ** @param  binding            the names of the attributes to be inserted by
   **                            the statement.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   */
  public static String prepareInsert(final DatabaseEntity entity, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning) {
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ").append(entity.type()).append(" (");
    int i = 0;
    for (Pair<String, DatabaseParameter> name : binding) {
      if (i++ > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(name.tag);
    }
    builder.append(") VALUES (");
    for (int j = 0; j < binding.size(); j++) {
      if (j > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(SystemConstant.QUESTION);
    }
    builder.append(")");
    if (returning != null && returning.size() > 0) {
      builder.append(prepareReturning(returning));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareUpdate
  /**
   ** Builds and returns an UPDATE-Statement for the specified
   ** <code>entity</code> that will return the attributes specified by
   ** <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            updated within.
   ** @param  filter             the filter criteria to build for the update
   **                            statement.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   ** @param  binding            the names of the attributes to be modified by
   **                            the statement.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter, final List<Pair<String, DatabaseParameter>> binding)
    throws DatabaseException {

    return prepareUpdate(entity, filter, parameter, binding, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareUpdate
  /**
   ** Builds and returns an UPDATE-Statement for the specified
   ** <code>entity</code> that will return the attributes specified by
   ** <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            updated within.
   ** @param  filter             the filter criteria to build for the update
   **                            statement.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   ** @param  binding            the names of the attributes to be modified by
   **                            the statement.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter, final List<Pair<String, DatabaseParameter>> binding, final Collection<Pair<String, Integer>> returning)
    throws DatabaseException {

    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ").append(entity.type());
    if (binding.size() > 0) {
      int i = 0;
      builder.append(" SET ");
      for (Pair<String, DatabaseParameter> name : binding) {
        if (i++ > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(name.tag).append("=?");
      }
    }
    if (filter != null) {
      builder.append(" WHERE ");
      builder.append(prepareFilter(filter, parameter));
    }
    if (returning != null && returning.size() > 0) {
      builder.append(prepareReturning(returning));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareDelete
  /**
   ** Builds and returns a DELETE-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   ** @param  filter             the filter criteria to build for the delete
   **                            statement.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareDelete(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter)
    throws DatabaseException {

    StringBuilder builder = new StringBuilder();
    builder.append("DELETE FROM ").append(entity.type());
    if (filter != null) {
      builder.append(" WHERE ");
      builder.append(prepareFilter(filter, parameter));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareFilter
  /**
   ** Builds and returns a WHERE-Statement for the specified
   ** <code>criteria</code>.
   **
   ** @param  criteria           the filter criteria the WHERE-Statement has to
   **                            build for.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **
   ** @return                    the statement clause build from the specified
   **                            criteria.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareFilter(final DatabaseFilter criteria, final List<DatabaseParameter> parameter)
    throws DatabaseException {

    Object                  argument1 = criteria.first();
    Object                  argument2 = criteria.second();
    DatabaseFilter.Operator op        = criteria.operator();
    String                  predicat  = null;
    switch (op) {
      case AND    : predicat = " AND ";
                    break;
      case OR     : predicat = " OR ";
                    break;
      case EQ     : predicat = " = ";
                    break;
      case GE     : predicat = " >= ";
                    break;
      case GT     : predicat = " > ";
                    break;
      case LE     : predicat = " <= ";
                    break;
      case LT     : predicat = " < ";
                    break;
      case NOT_EQ : predicat = " != ";
                    break;
      case CO     :
      case SW     :
      case EW     : predicat = " LIKE ";
                    break;
      case NOT_CO :
      case NOT_SW :
      case NOT_EW : predicat = " NOT LIKE ";
                    break;
      case IN     :
      case NOT_IN : predicat = " NOT IN ";
                    break;
      default     : throw new DatabaseException(DatabaseError.SEARCH_CONDITION_FAILED, criteria.toString());
    }

    // validate the fiter criteria that they are syntactical correct
    if (((op == DatabaseFilter.Operator.AND) || (op == DatabaseFilter.Operator.OR)) && ((!(argument1 instanceof DatabaseFilter)) || (!(argument2 instanceof DatabaseFilter))))
      throw new DatabaseException(DatabaseError.SEARCH_CONDITION_FAILED, criteria.toString());

    if ((op == DatabaseFilter.Operator.IN || op == DatabaseFilter.Operator.NOT_IN || op == DatabaseFilter.Operator.HIERARCHY) && (!((argument1 instanceof String) || (argument1 instanceof DatabaseEntity) || (argument1 instanceof DatabaseSelect) || ((argument1 instanceof Collection) && ((Collection)argument1).size() > 0))))
      throw new DatabaseException(DatabaseError.SEARCH_CONDITION_FAILED, criteria.toString());

    String first = null;
    if ((argument1 instanceof DatabaseFilter)) {
      first = prepareFilter((DatabaseFilter)argument1, parameter);
    }
    else if ((argument1 instanceof DatabaseEntity)) {
      first = ((DatabaseEntity)argument1).primary();
    }
    else {
      first = argument1.toString();
    }

    final StringBuilder builder = new StringBuilder();

    String second = null;
    if ((argument2 == null)) {
      if (op == DatabaseFilter.Operator.NOT_EQ)
        builder.append("(").append(first).append(" IS NOT NULL").append(")");
      else if (op == DatabaseFilter.Operator.EQ)
        builder.append("(").append(first).append(" IS NULL").append(")");
      else
        throw new DatabaseException(DatabaseError.SEARCH_CONDITION_FAILED, criteria.toString());
      return builder.toString();
    }
    else if ((argument2 instanceof DatabaseFilter)) {
      second = prepareFilter((DatabaseFilter)argument2, parameter);
    }
    else if ((argument2 instanceof Date)) {
      second = "?";
      parameter.add(DatabaseParameter.build(new Timestamp(((Date)argument2).getTime()), Types.TIMESTAMP));
    }
    else if ((argument2 instanceof Boolean)) {
      second = "?";
      parameter.add(DatabaseParameter.build(((Boolean)argument2).booleanValue() ? "1" : "0", Types.VARCHAR));
    }
    else if ((argument2 instanceof DatabaseEntity)) {
      final DatabaseEntity entity = ((DatabaseEntity)argument2);
      // if we have a subquery
      if (op == DatabaseFilter.Operator.IN || op == DatabaseFilter.Operator.NOT_IN) {
        StringBuilder subquery = new StringBuilder();
        subquery.append("(SELECT ");
        subquery.append(entity.primary());
        subquery.append(" FROM ");
        subquery.append(entity.type());
        subquery.append(")");
        second = subquery.toString();
      }
      else
        second = ((DatabaseEntity)argument2).primary();
    }
    else if ((argument2 instanceof DatabaseSelect)) {
      final DatabaseSelect query = ((DatabaseSelect)argument2);
      // if we have a subquery
      if (op == DatabaseFilter.Operator.IN || op == DatabaseFilter.Operator.NOT_IN) {
        StringBuilder subquery = new StringBuilder();
        subquery.append("(SELECT ");
        subquery.append(query.entity.primary());
        subquery.append(" FROM ");
        subquery.append(query.entity.type());
        subquery.append(" WHERE ");
        subquery.append(DatabaseStatement.prepareFilter(query.filter, parameter));
        subquery.append(")");
        second = subquery.toString();
      }
    }
    else if ((argument2 instanceof Collection)) {
      final StringBuilder arguments = new StringBuilder("(");
      // if we have a simple collection it's easy
      if (op == DatabaseFilter.Operator.IN) {
        for (Object value : (Collection)argument2) {
          if (arguments.length() > 1)
            arguments.append(',');

          arguments.append('?');
          parameter.add(DatabaseParameter.build(value.toString(), Types.VARCHAR));
        }
        arguments.append(")");
      }
      // TODO: How to handle a in hierarchy query
      second = arguments.toString();
    }
    else {
      second = "?";
      if ((argument2.toString().indexOf("*") >= 0) || ((op == DatabaseFilter.Operator.CO) || (op == DatabaseFilter.Operator.NOT_CO) || (op == DatabaseFilter.Operator.SW) || (op == DatabaseFilter.Operator.NOT_SW) || (op == DatabaseFilter.Operator.EW) || (op == DatabaseFilter.Operator.NOT_EW))) {
        switch (op) {
          case CO      :
          case NOT_CO  : argument2 = String.format("%%%s%%", argument2.toString().replace("*", ""));
                         break;
          case SW     :
          case NOT_SW : argument2 = String.format("%s%%", argument2.toString().replace("*", ""));
                         break;
          case EW     :
          case NOT_EW : argument2 = String.format("%%%s", argument2.toString().replace("*", ""));
                         break;
        }
      }
      parameter.add(DatabaseParameter.build(argument2));
    }
    builder.append("(").append(first).append(predicat).append(second).append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareOrder
  /**
   ** Builds and returns a ORDER-BY-Statement for the specified
   ** <code>criteria</code>.
   **
   ** @param  criteria           the filter criteria the WHERE-Statement has to
   **                            build for.
   **
   ** @return                    the statement clause build from the specified
   **                            criteria.
   */
  public static String prepareOrder(final DatabaseSort criteria) {

    Object                argument1 = criteria.first();
    Object                argument2 = criteria.second();
    DatabaseSort.Operator op        = criteria.operator();

    String first = null;
    if ((argument1 instanceof DatabaseSort)) {
      first = prepareOrder((DatabaseSort)argument1);
    }
    else {
      first = argument1.toString();
    }

    final StringBuilder builder = new StringBuilder();
    if ((argument2 == null)) {
      if (op == null)
        builder.append(first);
      else
        builder.append(first).append(SystemConstant.BLANK).append(op.value());
    }
    else if ((argument2 instanceof DatabaseSort)) {
      builder.append(SystemConstant.COMMA).append(SystemConstant.BLANK).append(prepareOrder((DatabaseSort)argument2));
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareReturning
  /**
   ** Builds and returns a RETURNING-Statement clause for the specified
   ** <code>criteria</code>.
   **
   ** @param  criteria           the criteria the RETURNING-Statement clause has
   **                            to build for.
   **
   ** @return                    the statement clause build from the specified
   **                            criteria.
   */
  public static String prepareReturning(final Collection<Pair<String, Integer>> criteria) {
    final StringBuilder builder = new StringBuilder(" RETURNING ");
    int i = 0;
    for (Pair<String, Integer> r : criteria) {
      if (i++ > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(r.tag);
    }
    builder.append(" INTO ");
    for (int j = 0; j < criteria.size(); j++) {
      if (j > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(SystemConstant.QUESTION);
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  statement          the {@link OraclePreparedStatement} to exceute.
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   ** @param  returning          the name/alias mapping to be returned by the
   **                            database statement.
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
  public static List<List<Pair<String, Object>>> execute(final OraclePreparedStatement statement, final List<DatabaseParameter> parameter, final List<Pair<String, String>> returning)
    throws DatabaseException {

    if (statement == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "statement");

    final List<List<Pair<String, Object>>> batch = new ArrayList<List<Pair<String, Object>>>();
    ResultSet resultSet = null;
    try {
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of select
      for (int i = 0; i < parameter.size(); i++) {
        final DatabaseParameter value = parameter.get(i);
        switch (value.type()) {
          case Types.DATE    : statement.setDate(i + 1, new java.sql.Date(value.dateValue().getTime()));
                               break;
          case Types.TIME    : statement.setTime(i + 1, new java.sql.Time(value.dateValue().getTime()));
                               break;
          case Types.FLOAT   : statement.setFloat(i + 1, value.floatValue());
                               break;
          case Types.DECIMAL : statement.setBigDecimal(i + 1, value.bigDecimalValue());
                               break;
          case Types.INTEGER : statement.setInt(i + 1, value.integerValue());
                               break;
          case Types.VARCHAR : statement.setString(i + 1, value.stringValue());
                               break;
          default            : statement.setObject(i + 1, value.value());
        }
      }
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        final List<Pair<String, Object>> record = new ArrayList<Pair<String, Object>>(returning.size());
        for (Pair<String, String> cursor : returning) {
          final Object value = resultSet.getObject(cursor.value);
          record.add(Pair.of(cursor.value, resultSet.wasNull() ? null : value));
        }
        batch.add(record);
      }
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
    return batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Close a previous prepared statement
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   */
  public final void close() {
    closeStatement(this.statement);
    this.statement = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Builds an SELECT-Statement for the {@link DatabaseEntity} this context
   ** belongs.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **
   ** @return                    the bindings for the filter criteria evaluated
   **                            so far in the preparation phase of the
   **                            statement.
   **
   ** @throws DatabaseException  if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public abstract List<DatabaseParameter> prepare(final Connection connection)
    throws DatabaseException;
}