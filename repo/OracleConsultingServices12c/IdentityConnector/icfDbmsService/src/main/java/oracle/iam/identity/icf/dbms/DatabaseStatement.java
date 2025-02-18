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

    File        :   DatabaseStatement.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseStatement.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.SQLTimeoutException;
import java.sql.SQLSyntaxErrorException;

import oracle.iam.identity.icf.foundation.SystemConstant;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

////////////////////////////////////////////////////////////////////////////////
// abstract class DatabaseStatement
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseStatement</code> provides the description of a query that
 ** can be applied on Database Servers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DatabaseStatement extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final DatabaseEntity    entity;
  protected final DatabaseFilter    filter;

  protected String                  query;
  protected List<DatabaseParameter> parameter;
  protected PreparedStatement       statement;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseStatement</code>.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
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
  public DatabaseStatement(final Loggable loggable, final DatabaseEntity entity, final DatabaseFilter filter) {
    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.entity    = entity;
    this.filter    = filter;
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
   **                            <br>
   **                            Possible object is {@link DatabaseFilter}.
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
   **                            <br>
   **                            Possible object is {@link DatabaseEntity}.
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String statement() {
    return this.query;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement
  /**
   **  Creates a new callable statement
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    the statement object
   **                            <br>
   **                            Possible object is {@link Statement}.
   **
   ** @throws SystemException  if SQL statement can't created.
   */
  public static final Statement createStatement(final Connection connection)
    throws SystemException {

    // prevent bogus input
    if (connection == null)
      throw SystemException.argumentNull("connection");

    Statement statement = null;
    try {
      statement = connection.createStatement();
    }
    catch (SQLException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.normalized(connection, e);
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
   ** @param  statement          the statement to close.
   **                            <br>
   **                            Allowed object is {@link Statement}.
   */
  public static final void closeStatement(final Statement statement) {
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
  // Method:   createPreparedStatement
  /**
   ** Creates a new {@link PreparedStatement} for sending parameterized SQL
   ** statements to the database.
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   ** <p>
   ** Result sets created using the returned {@link PreparedStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling
   ** <code>getHoldability()</code>.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new default {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   **
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final PreparedStatement createPreparedStatement(final Connection connection, final String string)
    throws SystemException {

    // prevent bogus input
    if (connection == null)
      throw SystemException.argumentNull("connection");

    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(string);
    }
    catch (SQLException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.normalized(connection, e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPreparedStatement
  /**
   ** Creates a {@link PreparedStatement} object that will generate
   ** {@link ResultSet} objects with the given <code>type</code> and
   ** <code>concurrency</code>. This method is the same as the prepareStatement
   ** method above, but it allows the default result set type and concurrency to
   ** be overridden. The holdability of the created result sets can be
   ** determined by calling getHoldability().
   ** <p>
   ** A SQL statement with or without IN parameters can be pre-compiled and
   ** stored in a {@link PreparedStatement} object. This object can then be used
   ** to efficiently execute this statement multiple times.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#TYPE_FORWARD_ONLY}
   **                              <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
   **                              <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#CONCUR_READ_ONLY}
   **                              <li>{@link ResultSet#CONCUR_UPDATABLE}
   **                            </ul>
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce {@link ResultSet} objects with the
   **                            given <code>type</code> and
   **                            <code>concurrency</code>.
   **                            <br>
   **                            Possible object is {@link PreparedStatement}.
   **
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final PreparedStatement createPreparedStatement(final Connection connection, final String string, int type, int concurrency)
    throws SystemException {

    // prevent bogus input
    if (connection == null)
      throw SystemException.argumentNull("connection");

    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(string, type, concurrency);
    }
    catch (SQLException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.normalized(connection, e);
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
   ** @param  statement          the {@link PreparedStatement} to close.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   */
  public static final void closeStatement(final PreparedStatement statement) {
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
   **                            <br>
   **                            Allowed object is {@link ResultSet}.
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
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareExists(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT 'x' AS x FROM dual WHERE EXISTS (");
    builder.append(prepareSelect(entity, filter, argument, null)).append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSearch
  /**
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSearch(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument, final List<DatabaseAttribute> returning)
    throws DatabaseException {

    return "SELECT * FROM (" + prepareSelect(entity, filter, argument, returning) +") WHERE rownumber between ? and ?";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareSelect
  /**
   ** Builds and returns a SELECT-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   ** @param  entity             the database entity the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            The convention is that any row in
   **                            <code>returning</code> has two strings. The
   **                            string of index <code>0</code> is the name of
   **                            the column in a table. The string of index
   **                            <code>1</code> is the alias of the colum in the
   **                            returned result set.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSelect(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> argument, final List<DatabaseAttribute> returning)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT ");
    if (returning != null) {
      for (int i = 0; i < returning.size(); i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(String.format("%s AS %s", returning.get(i).id, returning.get(i).alias));
      }
      // regardless if a paginated result set will be used or not at this time we
      // don't know how the select statement will be used hence we are always
      // selecting the row number pseudo column to ensure that we can use the
      // statement in a paginated query
      builder.append(String.format(",%s AS %s", "rownum", "rownumber"));
    }
    else {
      builder.append(String.format("%s AS %s", "rownum", "rownumber"));
    }

    builder.append(" FROM ").append(entity.id());
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
   ** @param  entity             the database entities the information has to be
   **                            fetched from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  join               the filter criteria to join the entities.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  filter             the filter criteria to build for the select
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  argument           the subtitition of bind values created during
   **                            the evaluation of the {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareSelect(final DatabaseEntity[] entity, final DatabaseFilter join, final DatabaseFilter filter, final List<DatabaseParameter> argument)
    throws DatabaseException {

    final StringBuilder builder = new StringBuilder("SELECT ");
    for (int i = 0; i < entity.length; i++) {
      for (int j = 0; j < entity[i].attribute.size(); j++) {
        if (i > 0 || j > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(String.format("%s AS %s", entity[i].attribute.get(j).id, entity[i].attribute.get(j).alias));
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
      builder.append(entity[i].id());
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
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  binding            the names of the attributes to be inserted by
   **                            the statement.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String prepareInsert(final DatabaseEntity entity, final Set<String> binding) {
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
   **                            modified from.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  binding            the names of the attributes to be inserted by
   **                            the statement.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String prepareInsert(final DatabaseEntity entity, final Set<String> binding, final String[] returning) {
    StringBuilder builder = new StringBuilder();
    builder.append("INSERT INTO ").append(entity.id()).append(" (");
    int i = 0;
    for (String name : binding) {
      if (i++ > 0)
        builder.append(SystemConstant.COMMA);
      // lookup the native name by the alias name from the binding
      builder.append(entity.lookup(name).id());
    }
    builder.append(") VALUES (");
    for (int j = 0; j < i; j++) {
      if (j > 0)
        builder.append(SystemConstant.COMMA);
      builder.append(SystemConstant.QUESTION);
    }
    builder.append(")");
    if (returning != null && returning.length > 0) {
      builder.append(" RETURNING ");
      for (i = 0; i < returning.length; i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(returning[i]);
      }
      builder.append(" INTO ");
      for (i = 0; i < returning.length; i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(SystemConstant.QUESTION);
      }
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
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the update
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  binding            the names of the attributes to be modified by
   **                            the statement.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter, final Set<String> binding)
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
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the update
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  binding            the names of the attributes to be modified by
   **                            the statement.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   ** @param  returning          the attributes include in the result set
   **                            fetched from the database and returned to the
   **                            invoker.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareUpdate(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter, final Set<String> binding, final String[] returning)
    throws DatabaseException {

    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ").append(entity.id());
    if (binding.size() > 0) {
      int i = 0;
      builder.append(" SET ");
      for (String name : binding) {
        if (i++ > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(entity.lookup(name).id()).append("=?");
      }
    }
    if (filter != null) {
      builder.append(" WHERE ");
      builder.append(prepareFilter(filter, parameter));
    }
    if (returning != null && returning.length > 0) {
      builder.append(" RETURNING ");
      for (int i = 0; i < returning.length; i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(returning[i]);
      }
      builder.append(" INTO ");
      for (int i = 0; i < returning.length; i++) {
        if (i > 0)
          builder.append(SystemConstant.COMMA);
        builder.append(SystemConstant.QUESTION);
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareDelete
  /**
   ** Builds and returns a DELETE-Statement for the specified <code>entity</code>
   ** that will return the attributes specified by <code>returning</code>.
   **
   **
   ** @param  entity             the database entity the information has to be
   **                            updated within.
   **                            <br>
   **                            Allowed object is {@link DatabaseEntity}.
   ** @param  filter             the filter criteria to build for the update
   **                            statement.
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws DatabaseException  if the build of the filter condition fails.
   */
  public static String prepareDelete(final DatabaseEntity entity, final DatabaseFilter filter, final List<DatabaseParameter> parameter)
    throws DatabaseException {

    StringBuilder builder = new StringBuilder();
    builder.append("DELETE FROM ").append(entity.id());
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
   **                            <br>
   **                            Allowed object is {@link DatabaseFilter}.
   ** @param  parameter          the subtitition of bind parameter created
   **                            during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    the statement string build from the specified
   **                            properties.
   **                            <br>
   **                            Possible object is {@link String}.
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
      case AND             : predicat = " AND ";
                             break;
      case OR              : predicat = " OR ";
                             break;
      case EQUAL           : predicat = " = ";
                             break;
      case GREATER_EQUAL   : predicat = " >= ";
                             break;
      case GREATER_THAN    : predicat = " > ";
                             break;
      case LESS_EQUAL      : predicat = " <= ";
                             break;
      case LESS_THAN       : predicat = " < ";
                             break;
      case NOT_EQUAL       : predicat = " != ";
                             break;
      case CONTAINS        :
      case STARTS_WITH     :
      case ENDS_WITH       : predicat = " LIKE ";
                             break;
      case NOT_CONTAINS    :
      case NOT_STARTS_WITH :
      case NOT_ENDS_WITH   : predicat = " NOT LIKE ";
                             break;
      case IN              :
      case HIERARCHY       : predicat = " IN ";
                             break;
      case NOT_IN          : predicat = " NOT IN ";
                             break;
      default              : throw DatabaseException.searchCondition(criteria.toString());
    }

    // validate the fiter criteria that they are syntactical correct
    if (((op == DatabaseFilter.Operator.AND) || (op == DatabaseFilter.Operator.OR)) && ((!(argument1 instanceof DatabaseFilter)) || (!(argument2 instanceof DatabaseFilter))))
      throw DatabaseException.searchCondition(criteria.toString());

    if ((op == DatabaseFilter.Operator.IN || op == DatabaseFilter.Operator.NOT_IN || op == DatabaseFilter.Operator.HIERARCHY) && (!((argument1 instanceof String) || (argument1 instanceof DatabaseEntity) || (argument1 instanceof DatabaseSelect) || ((argument1 instanceof Collection) && ((Collection)argument1).size() > 0))))
      throw DatabaseException.searchCondition(criteria.toString());

    String first = null;
    if ((argument1 instanceof DatabaseFilter)) {
      first = prepareFilter((DatabaseFilter)argument1, parameter);
    }
    else if ((argument1 instanceof DatabaseEntity)) {
      first = ((DatabaseEntity)argument1).primary.get(0).id;
    }
    else {
      first = argument1.toString();
    }

    final StringBuilder builder = new StringBuilder();

    String second = null;
    if ((argument2 == null)) {
      if (op == DatabaseFilter.Operator.NOT_EQUAL)
        builder.append("(").append(first).append(" IS NOT NULL").append(")");
      else if (op == DatabaseFilter.Operator.EQUAL)
        builder.append("(").append(first).append(" IS NULL").append(")");
      else
        throw DatabaseException.searchCondition(criteria.toString());
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
        subquery.append(entity.primary.get(0).id);
        subquery.append(" FROM ");
        subquery.append(entity.id());
        subquery.append(")");
        second = subquery.toString();
      }
      else
        second = entity.primary.get(0).id;
    }
    else if ((argument2 instanceof DatabaseSelect)) {
      final DatabaseSelect query = ((DatabaseSelect)argument2);
      // if we have a subquery
      if (op == DatabaseFilter.Operator.IN || op == DatabaseFilter.Operator.NOT_IN) {
        StringBuilder subquery = new StringBuilder();
        subquery.append("(SELECT ");
        subquery.append(query.entity.primary.get(0).id);
        subquery.append(" FROM ");
        subquery.append(query.entity.id());
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
      if ((argument2.toString().indexOf("*") >= 0) || ((op == DatabaseFilter.Operator.CONTAINS) || (op == DatabaseFilter.Operator.NOT_CONTAINS) || (op == DatabaseFilter.Operator.STARTS_WITH) || (op == DatabaseFilter.Operator.NOT_STARTS_WITH) || (op == DatabaseFilter.Operator.ENDS_WITH) || (op == DatabaseFilter.Operator.NOT_ENDS_WITH))) {
        switch (op) {
          case CONTAINS        :
          case NOT_CONTAINS    : argument2 = String.format("%%%s%%", argument2.toString().replace("*", ""));
                                 break;
          case STARTS_WITH     :
          case NOT_STARTS_WITH : argument2 = String.format("%s%%", argument2.toString().replace("*", ""));
                                 break;
          case ENDS_WITH       :
          case NOT_ENDS_WITH   : argument2 = String.format("%%%s", argument2.toString().replace("*", ""));
                                 break;
        }
      }
      parameter.add(DatabaseParameter.build(argument2));
    }
    builder.append("(").append(first).append(predicat).append(second).append(")");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyParameter
  /**
   ** Set the bindings for the binding values evaluated in the
   ** preparation of an statement so far.
   **
   ** @param  statement          the {@link PreparedStatement} to bind the
   **                            parameters to.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   ** @param  startIndex         the index at which binding of the parameters
   **                            have to start.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  parameter          the collection of {@link DatabaseParameter}s to
   **                            bind to the statement.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @return                    the index of the next binding value.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws SystemException    if the parameters could not been set for the
   **                            statement.
   */
  public static int applyParameter(final PreparedStatement statement, int startIndex, final List<DatabaseParameter> parameter)
    throws SystemException {

    if (statement == null)
      throw SystemException.argumentNull("statement");

    try {
      // set the bindings for the parameters evaluated so far in the preparation
      // phase of the statement given
      for (int i = 0; i < parameter.size(); i++) {
        final DatabaseParameter cursor = parameter.get(i);
        if (cursor.value() == null)
          statement.setNull(startIndex++, cursor.type());
        else {
          switch (cursor.type()) {
            case Types.BOOLEAN   : statement.setBoolean(startIndex++, cursor.booleanValue());
                                   break;
            case Types.INTEGER   : statement.setInt(startIndex++, cursor.integerValue());
                                   break;
            case Types.NUMERIC   : statement.setLong(startIndex++, cursor.longValue());
                                   break;
            case Types.DOUBLE    : statement.setDouble(startIndex++, cursor.doubleValue());
                                   break;
            case Types.FLOAT     : statement.setFloat(startIndex++, cursor.floatValue());
                                   break;
            case Types.DECIMAL   : statement.setBigDecimal(startIndex++, cursor.bigDecimalValue());
                                   break;
            case Types.DATE      : statement.setDate(startIndex++, new java.sql.Date(cursor.dateValue().getTime()));
                                   break;
            case Types.TIME      : statement.setTime(startIndex++, new java.sql.Time(cursor.dateValue().getTime()));
                                   break;
            case Types.TIMESTAMP : statement.setTimestamp(startIndex++, new java.sql.Timestamp(cursor.longValue()));
                                   break;
            case Types.VARCHAR   : statement.setString(startIndex++, cursor.stringValue());
                                   break;
            default              : statement.setObject(startIndex++, cursor.value());
          }
        }
      }
    }
    catch (SQLException e) {
      // wrap the exception occured in throw it to the invoker for further
      // analysis
      throw DatabaseException.normalized(statement, e);
    }
    return startIndex;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute
  /**
   ** Selects in the specified context or object for entries that satisfy the
   ** given search filter.
   **
   ** @param  statement          the {@link PreparedStatement} to exceute.
   **                            <br>
   **                            Allowed object is {@link PreparedStatement}.
   ** @param  parameter          the subtitition values for bind parameters
   **                            created during the evaluation of the
   **                            {@link DatabaseFilter}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   ** @param  returning          the name/alias mapping to be returned by the
   **                            database statement.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link DatabaseAttribute}.
   ** @param  timeOut            the timeout period the client will wait for a
   **                            response.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a {@link List} where each element in the list
   **                            is of type
   **                            <code>Map&lt;String, Object&gt;</code>s.
   **                            The mapping contains the value for each element
   **                            defined by the alias (the string with index
   **                            <code>1</code>) in <code>returning</code>;
   **                            never <code>null</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Map} with type
   **                            {@link String} as the key and type
   **                            {@link Object} as the value.
   **
   ** @throws SystemException    in case the search operation cannot be
   **                            performed.
   */
  public static List<Map<String, Object>> execute(final PreparedStatement statement, final List<DatabaseParameter> parameter, final List<DatabaseAttribute> returning, final int timeOut)
    throws SystemException {

    if (statement == null)
      throw SystemException.argumentNull("statement");

    final List<Map<String, Object>> batch = new ArrayList<Map<String, Object>>();
    ResultSet resultSet = null;
    try {
      // set timeout period we will wait for a response
      statement.setQueryTimeout(timeOut == -1 ? 0 : timeOut);
      // set the bindings for the filter criteria evaluated so far in the
      // preparation of select
      applyParameter(statement, 1, parameter);
      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        // wrap the current record  in a map with the alias name as the key and
        // the fetched value as the value
        final Map<String, Object> record = new LinkedHashMap<String, Object>(returning.size());
        for (int i = 0; i < returning.size(); i++) {
          final Object value = resultSet.getObject(returning.get(i).alias);
          record.put(returning.get(i).alias, resultSet.wasNull() ? null : value);
        }
        // enlist the name/value pairs in the returning batch
        batch.add(record);
      }
    }
    catch (SQLSyntaxErrorException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.syntax(statement.toString());
    }
    catch (SQLTimeoutException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.timeout(statement.toString());
    }
    catch (SQLException e) {
      // wrap the exception occured in a checked exception and throw that to the
      // invoker for further analysis
      throw DatabaseException.normalized(statement, e);
    }
    finally {
      closeResultSet(resultSet);
    }
    return batch;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Return a string representation of the statement for debugging.
   **
   ** @return                    a string representation of the statement.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return this.query;
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
   ** belongs to.
   **
   ** @param  connection         the JDBC connection to prepare a statement for.
   **                            <br>
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    the bindings for the filter criteria evaluated
   **                            so far in the preparation phase of the
   **                            statement.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link DatabaseParameter}.
   **
   ** @throws SystemException    if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public abstract List<DatabaseParameter> prepare(final Connection connection)
    throws SystemException;
}