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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Authorization Management

    File        :   QueryProviderFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    QueryProviderFactory.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysconfig.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import oracle.iam.ui.platform.exception.OIMRuntimeException;

import oracle.iam.identity.foundation.persistence.DatabaseFilter;
import oracle.iam.identity.foundation.persistence.DatabaseParameter;
import oracle.iam.identity.foundation.persistence.DatabaseStatement;

////////////////////////////////////////////////////////////////////////////////
// class QueryProviderFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** This object contains factory methods to fetch predefined sql statements from
 ** <code>META-INF/sql/repository</code>.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class QueryProviderFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>QueryProviderFactory</code>
   ** <p>
   ** Singleton Pattern
   */
  public static final QueryProviderFactory instance        = new QueryProviderFactory();

  /** the name of the JDBC Datasourec used to execute SQL statments */
  public static final String               DATASOURCE      = "operationsDB";

  /** the location of the repository to load the sal statements from */
  private static final String              REPOSITORY      = "META-INF/sql/sysconfig.sql";

  private static final String              CONJUNCTION_NOT = "%s WHERE %s";
  private static final String              CONJUNCTION_AND = "%s AND %s";
  private static final String              PAGINATED_NOT   = "SELECT * FROM (%s WHERE %s) WHERE rownumber BETWEEN ? AND ?";
  private static final String              PAGINATED_AND   = "SELECT * FROM (%s AND %s) WHERE rownumber BETWEEN ? AND ?";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Properties                 repository = new Properties();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>QueryProviderFactory</code> object.
   ** <p>
   ** This creates the sql statement repository.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new QueryProviderFactory()" and enforces use of the public factory
   ** method below.
   */
  private QueryProviderFactory() {
    // ensure inheritance
    super();

    // load all the queries in singleton constructor
    final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(REPOSITORY);
    try {
      this.repository.load(stream);
      // normalize all whitespaces to a single blank
      for (Object property : this.repository.keySet()) {
        final String statement = (String)this.repository.get(property);
        this.repository.put(property, statement.replaceAll("\\s+", " "));
      }
    }
    catch (IOException e) {
      // intetionally left blank
      e.printStackTrace(System.err);
    }
    finally {
      try {
        stream.close();
      }
      catch (IOException e) {
        // intetionally left blank
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   query
  /**
   ** Searches for the sql statement with the specified key
   ** <code>property</code> in the repository.
   ** <br>
   ** If the <code>property</code> is not found in the repository, the method
   ** returns <code>null</code>.
   **
   ** @param  property           the property key.
   **
   ** @return                    the value in the repository with the specified
   **                            property value.
   */
  public String query(final String property) {
    return this.repository.getProperty(property);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedInsert
  /**
   ** Factore method to create and prepare a SQL INSERT statement specified by
   ** key <code>property</code> from the repository.
   ** <br>
   ** If the <code>property</code> is not found in the repository, the method
   ** returns <code>null</code>.
   **
   ** @param  property           the property key.
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  arguments          the values has to bind.
   **
   ** @return                    a {@link PreparedStatement} ready for
   **                            execution.
   */
  public PreparedStatement preparedInsert(final String property, final Connection connection, final List<DatabaseParameter> arguments) {
    try {
      // prepare the filter criteria
      final String query = QueryProviderFactory.instance.query(property);
      // prepare the SQL statement
      return preparedStatement(connection, query, arguments);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedUpdate
  /**
   ** Factore method to create and prepare a SQL UPDATE statement specified by
   ** key <code>property</code> from the repository.
   ** <br>
   ** If the <code>property</code> is not found in the repository, the method
   ** returns <code>null</code>.
   **
   ** @param  property           the property key.
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  criteria           the filter criteria the WHERE-Statement has to
   **                            build for.
   ** @param  arguments          the values has to bind.
   **
   ** @return                    a {@link PreparedStatement} ready for
   **                            execution.
   */
  public PreparedStatement preparedUpdate(final String property, final Connection connection, final DatabaseFilter criteria, final List<DatabaseParameter> arguments) {
    try {
      // prepare the filter criteria
      final String filter = DatabaseStatement.prepareFilter(criteria, arguments);
      final String query  = String.format(CONJUNCTION_NOT, QueryProviderFactory.instance.query(property), filter);
      // prepare the SQL statement
      return preparedStatement(connection, query, arguments);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedStatement
  /**
   ** Factore method to create and prepare a SQL SELECT statement specified by
   ** key <code>property</code> from the repository.
   ** <br>
   ** If the <code>property</code> is not found in the repository, the method
   ** returns <code>null</code>.
   **
   ** @param  property           the property key.
   ** @param  conjunction        ...
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  criteria           the filter criteria the WHERE-Statement has to
   **                            build for.
   **
   ** @return                    a {@link PreparedStatement} ready for
   **                            execution.
   */
  public PreparedStatement preparedStatement(final String property, final DatabaseFilter.Operator conjunction, final Connection connection, final DatabaseFilter criteria) {
    try {
      // prepare the filter criteria
      final List<DatabaseParameter> arguments = new ArrayList<DatabaseParameter>();
      final String                  filter    = DatabaseStatement.prepareFilter(criteria, arguments);
      final String                  query     = String.format(conjunction != null && conjunction == DatabaseFilter.Operator.AND ? CONJUNCTION_AND : CONJUNCTION_NOT, QueryProviderFactory.instance.query(property), filter);
      // prepare the SQL statement
      return preparedStatement(connection, query, arguments);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedStatement
  /**
   ** Factore method to create and prepare a SQL SELECT statement specified by
   ** key <code>property</code> from the repository.
   ** <br>
   ** If the <code>property</code> is not found in the repository, the method
   ** returns <code>null</code>.
   **
   ** @param  property           the property key.
   ** @param  conjunction        ...
   ** @param  connection         the JDBC connection to create a statement for.
   ** @param  criteria           the filter criteria the WHERE-Statement has to
   **                            build for.
   ** @param  start              the 1-based index of the first query result.
   ** @param  count              the desired maximum number of query results per
   **                            page.
   **
   ** @return                    a {@link PreparedStatement} ready for
   **                            execution.
   */
  public PreparedStatement preparedStatement(final String property, final DatabaseFilter.Operator conjunction, final Connection connection, final DatabaseFilter criteria, final int start, final int count) {
    try {
      // prepare the filter criteria
      final List<DatabaseParameter> arguments = new ArrayList<DatabaseParameter>();
      final String                  filter = DatabaseStatement.prepareFilter(criteria, arguments);
      final String                  query  = String.format(conjunction != null && conjunction == DatabaseFilter.Operator.AND ? PAGINATED_AND : PAGINATED_NOT, QueryProviderFactory.instance.query(property), filter);
      arguments.add(DatabaseParameter.build(start, Types.INTEGER));
      arguments.add(DatabaseParameter.build(count, Types.INTEGER));
      return preparedStatement(connection, query, arguments);
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparedStatement
 public PreparedStatement preparedStatement(final Connection connection, final String query, final List<DatabaseParameter> arguments) {
    try {
      final PreparedStatement statement = DatabaseStatement.createPreparedStatement(connection, query);
      for (int i = 0; i < arguments.size(); i++) {
        final DatabaseParameter arg = arguments.get(i);
        switch (arg.type()) {
          case Types.VARCHAR  : statement.setString(i + 1, arg.stringValue());
                                break;
          case Types.NUMERIC  : statement.setLong(i + 1, arg.longValue());
                                break;
          case Types.SMALLINT :
          case Types.INTEGER  : statement.setInt(i + 1, arg.integerValue());
                                break;
        }
      }
      return statement;
    }
    catch (Exception e) {
      throw new OIMRuntimeException(e);
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   executeConnection
  /**
   ** Execute a transaction in supplied connection.
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            excecution.
   ** @param  statement          the JDBC statement which will excecute.
   */
  public static final void executeConnection(final Connection connection, final PreparedStatement statement) {
    if (connection != null && statement != null) {
      try {
        if (statement.execute()) {
          connection.commit();
        }
        else {
          connection.rollback();
        }
      }
      catch (SQLException e) {
        throw new OIMRuntimeException(e);
      }
      finally {
        DatabaseStatement.closeStatement(statement);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitConnection
  /**
   ** Commit a transaction in supplied connection
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            commit.
   */
  public static final void commitConnection(final Connection connection) {
    if (null != connection) {
      try {
        connection.commit();
      }
      catch (SQLException e) {
        throw new OIMRuntimeException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rollbackConnection
  /**
   ** Rollback a transaction in supplied connection
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            rollback.
   */
  public static final void rollbackConnection(final Connection connection) {
    if (null != connection) {
      try {
        connection.rollback();
      }
      catch (SQLException e) {
        throw new OIMRuntimeException(e);
      }
    }
  }
}