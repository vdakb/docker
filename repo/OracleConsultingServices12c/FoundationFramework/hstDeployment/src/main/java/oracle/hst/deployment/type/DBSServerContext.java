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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities

    File        :   DBSServerContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DBSServerContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.PatternSyntaxException;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class DBSServerContext
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The client environment wrapper of a database connection to an Oracle
 ** WebLogic Domain Server.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DBSServerContext extends AbstractContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String                CONTEXT_TYPE      = "dbs";

  static final Map<String, String[]> DRIVER            = new HashMap<String, String[]>();
  static final String                ACCOUNT           = "user";
  static final String                PASSWORD          = "password";
  static final short                 QUERY_TIMEOUT     = 20;

  /**
   ** This regular expression uses one group to substitute expressions with a
   ** String.
   ** <p>
   ** Groups are defined by parentheses. Note that ?: will define a
   ** group as "non-contributing"; that is, it will not contribute to the return
   ** values of the <code>group</code> method.
   */
  static final String  URL_PATTERN       = "\\#\\{((?:\\w|\\s)+)}";

  /** Default value of the Oracle JDBC Driver class. */
  static final String  ORACLE_DRIVER     = "oracle.jdbc.OracleDriver";

  /** Default value of the Oracle JDBC URL. */
  static final String  ORACLE_PATTERN    = "jdbc:#{type}:#{protocol}:@#{host}:#{port}/#{database}";

  /** Default value of the MySQL Server JDBC Driver class. */
  static final String  MYSQL_DRIVER      = "com.mysql.jdbc.Driver";

  /** Default value of the MySQL Server JDBC URL. */
  static final String  MYSQL_PATTERN     = "jdbc:#{type}://#{host}:#{port}/#{database}";

  /** Default value of the Microsoft SQL Server JDBC Driver class. */
  static final String  SQLSERVER_DRIVER  = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

  /** Default value of the Microsoft SQL Server URL. */
  static final String  SQLSERVER_PATTERN = "jdbc:#{type}:sqlserver://#{host}:#{port}";

  /** Default value of the Sybase Server JDBC Driver class. */
  static final String  SYBASE_DRIVER     = "com.sybase.jdbc2.jdbc.SybDriver";

  /** Default value of the Sybase Server URL. */
  static final String  SYBASE_PATTERN    = "jdbc:#{type}:Tds:#{host}:#{port}";

  /** Default value of the IBM Universal Database Server JDBC Driver class. */
  static final String  UDB_DRIVER        = "com.ibm.db2.jcc.DB2Driver";

  /** Default value of the IBM Universal Database Server URL. */
  static final String  UDB_PATTERN       = "jdbc:#{type}://#{host}:#{port}/#{database}";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private DatabaseType type              = null;

  private int          retryCount        = 3;
  private int          retryInterval     = 3000;
  private String       database          = null;
  private String       contextURL        = null;

  private Connection   connection        = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    DRIVER.put(DatabaseType.ORACLE,    new String[] {ORACLE_DRIVER,    ORACLE_PATTERN});
    DRIVER.put(DatabaseType.MYSQL,     new String[] {MYSQL_DRIVER,     MYSQL_PATTERN });
    DRIVER.put(DatabaseType.SQLSERVER, new String[] {SQLSERVER_DRIVER, SQLSERVER_PATTERN});
    DRIVER.put(DatabaseType.SYBASE,    new String[] {SYBASE_DRIVER,    SYBASE_PATTERN });
    DRIVER.put(DatabaseType.UDB,       new String[] {UDB_DRIVER,       UDB_PATTERN });
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DBSServerContext</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DBSServerContext() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DBSServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            Allowed object is {@link DatabaseType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JDBC
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  database           the value for the attribute
   **                            <code>database</code>.
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public DBSServerContext(final DatabaseType type, final String protocol, final String host, final String port, final String database, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, database, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DBSServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            Allowed object is {@link DatabaseType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JDBC
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            JDBC provider connection.
   **                            Allowed object is <code>int</code>.
   ** @param  database           the value for the attribute
   **                            <code>database</code>.
   **                            Allowed object is {@link String}.
   ** @param  username           the name of the administrative user.
   **                            Allowed object is {@link String}.
   ** @param  password           the password of the administrative user.
   **                            Allowed object is {@link String}.
   */
  public DBSServerContext(final DatabaseType type, final String protocol, final String host, final String port, final int timeoutConnection, final String database, final String username, final String password) {
    // ensure inheritance
    this(type, protocol, host, port, timeoutConnection, database, new SecurityPrincipal(username, password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DBSServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            Allowed object is {@link DatabaseType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JDBC
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  database           the value for the attribute
   **                            <code>database</code>.
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public DBSServerContext(final DatabaseType type, final String protocol, final String host, final String port, final String database, final SecurityPrincipal principal) {
    // ensure inheritance
    this(type, protocol, host, port, TIMEOUT_DEFAULT_CONNECTION, database, principal);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DBSServerContext</code> for the specified
   ** <code>protocol</code>, <code>host</code> and <code>port</code>.
   ** <br>
   ** The required security context is provided by {@link SecurityPrincipal}
   ** <code>principal</code>.
   ** <p>
   ** <b>Note</b>:
   ** This constructor is mainly used for testing prupose only.
   ** The ANT task and type that leverage this type will be use the non-arg
   ** default constructor and inject their configuration values by the
   ** appropriate setters.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            Allowed object is {@link DatabaseType}.
   ** @param  protocol           the value for the attribute
   **                            <code>protocol</code> used as the JDBC
   **                            provider.
   **                            Allowed object is {@link String}.
   ** @param  host               the value for the attribute <code>host</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  port               the value for the attribute <code>port</code>
   **                            used as the JDBC provider.
   **                            Allowed object is {@link String}.
   ** @param  timeoutConnection  the timeout period for establishment of the
   **                            JDBC provider connection.
   **                            Allowed object is <code>int</code>.
   ** @param  database           the value for the attribute
   **                            <code>database</code>.
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal used to establish a
   **                            connection to the server.
   **                            Allowed object is {@link SecurityPrincipal}.
   */
  public DBSServerContext(final DatabaseType type, final String protocol, final String host, final String port, final int timeoutConnection, final String database, final SecurityPrincipal principal) {
    // ensure inheritance
    super(protocol, host, port, timeoutConnection, principal);

    // initialize instance attributes
    this.type     = type;
    this.database = database;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>type</code>.
   **
   ** @param  type               the value for the attribute <code>type</code>
   **                            used as the database provider.
   **                            Allowed object is {@link DatabaseType}.
   **
   ** @throws BuildException     if the <code>type</code> is already specified
   **                            by a reference.
   */
  public final void setType(final DatabaseType type)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("type");

    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the database used to connect to.
   **
   ** @return                    the type of the database used to connect to.
   **                            Possible object is {@link DatabaseType}.
   */
  public final DatabaseType type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRetryCount
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>retryCount</code>.
   ** <p>
   ** The number of consecutive attempts to be made at establishing a
   ** connection with the Database Server.
   **
   ** @param  retryCount         the number of consecutive attempts to be made
   **                            at establishing a connection with the Database
   **                            Server.
   **                            Allowed object is <code>int</code>.
   **
   ** @throws BuildException     if the <code>retryCount</code> is already
   **                            specified by a reference.
   */
  public final void setRetryCount(final int retryCount)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("retryCount");

    this.retryCount = retryCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the Database Server.
   **
   ** @return                    the number of consecutive attempts to be made
   **                            at establishing a connection with the Database
   **                            Server.
   **                            Possible object is <code>int</code>.
   */
  public final int retryCount() {
    return this.retryCount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRetryInterval
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>retryInterval</code>.
   ** <p>
   ** The the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Database Server.
   **
   ** @param  retryInterval      the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the Database Server.
   **                            Allowed object is <code>int</code>.
   **
   ** @throws BuildException     if the <code>retryInterval</code> is already
   **                            specified by a reference.
   */
  public final void setRetryInterval(final int retryInterval)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("retryInterval");

    this.retryInterval = retryInterval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Database Server.
   **
   ** @return                    the timeout period for establishment of the
   **                            Database Server connection.
   **                            Possible object is <code>int</code>.
   */
  public final int retryInterval() {
    return this.retryInterval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDatabase
  /**
   ** Call by the ANT kernel to inject the <code>database</code> attribute.
   **
   ** @param  database           the value for the attribute
   **                            <code>database</code>.
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if the <code>database</code> attribute is
   **                            already specified by a reference.
   */
  public final void setDatabase(final String database)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("database");

    this.database = database;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   database
  /**
   ** Returns the <code>database</code> attribute.
   **
   ** @return                    the <code>database</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String database() {
    return this.database;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContextURL
  /**
   ** Call by the ANT kernel to inject the argument for parameter contextURL.
   **
   ** @param  contextURL         the fullqualified context URL to the server.
   **                            Allowed object is {@link String}.
   **
   ** @throws BuildException     if any instance attribute <code>refid</code>
   **                            is set already.
   */
  public void setContextURL(final String contextURL)
    throws BuildException {

    // prevent bogus input
    if (this.getRefid() != null)
      handleAttributeError("contextURL");

    this.contextURL = contextURL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Database Server.
   **
   ** @return                    the fullqualified URL to the Database Server.
   **                            Possible object is {@link String}.
   */
  public final String contextURL() {
    return this.serviceURL();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Return the {@link Connection} instance to the Database Server.
   **
   ** @return                    the {@link Connection} instance to the Database
   **                            Server.
   */
  public final Connection connection() {
    return this.connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextType (AbstractContext)
  /**
   ** Returns the specific type of the implemented context.
   **
   ** @return                    the specific type of the implemented context.
   **                            Possible object is {@link String}.
   */
  @Override
  public String contextType() {
    return CONTEXT_TYPE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect (AbstractContext)
  /**
   ** Establishes the connection to the server.
   ** <p>
   ** This method binds to the context of the server instance.
   **
   ** @throws ServiceException   if the connection could not be established.
   */
  @Override
  public void connect()
    throws ServiceException {

    if (this.connection == null) {
      // Constructs an initial context object using environment properties and
      // connection request controls.
      this.connection = connect(serviceURL(), environment());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect (AbstractContext)
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  @Override
  public void disconnect()
    throws ServiceException {

    if (this.connection != null)
      try {
        this.connection.close();
      }
      catch (SQLException e) {
        throw new ServiceException(ServiceError.UNHANDLED, e);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitConnection
  /**
   ** Commit a transaction in supplied connection
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            commit.
   **
   ** @throws ServiceException   if connection cannot be commited.
   */
  public static final void commitConnection(final Connection connection)
    throws ServiceException {

    if (null != connection) {
      try {
        connection.commit();
      }
      catch (SQLException e) {
        throw new ServiceException(e);
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
   **
   ** @throws ServiceException   if connection cannot be rollback.
   */
  public static final void rollbackConnection(final Connection connection)
    throws ServiceException {

    if (null != connection) {
      try {
        connection.rollback();
      }
      catch (SQLException e) {
         throw new ServiceException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createStatement
  /**
   **  Creates a new callable statement
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            Allowed object is {@link Connection}.
   **
   ** @return                    the statement object
   **                            Possible object is {@link Statement}.
   **
   ** @throws ServiceException   if SQL statement can't created.
   */
  public static final Statement createStatement(final Connection connection)
    throws ServiceException {

    if (connection == null)
      throw new ServiceException(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection");

    Statement statement = null;
    try {
      statement = connection.createStatement();
    }
    catch (SQLException e) {
      throw new ServiceException(e);
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
   ** @param  statement          the statement to close
   **                            Allowed object is {@link Statement}.
   */
  public static final void closeStatement(final Statement statement) {

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
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new default {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            Possible object is {@link PreparedStatement}.
   **
   ** @throws ServiceException   if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final PreparedStatement createPreparedStatement(final Connection connection, final String string)
    throws ServiceException {

    if (connection == null)
      throw new ServiceException(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection");

    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(string);
      if (QUERY_TIMEOUT > 0)
        statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new ServiceException(e);
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
   **                            Allowed object is {@link String}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            Allowed object is {@link Connection}.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#TYPE_FORWARD_ONLY}
   **                              <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
   **                              <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
   **                            </ul>
   **                            Allowed object is {@link String}.
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#CONCUR_READ_ONLY}
   **                              <li>{@link ResultSet#CONCUR_UPDATABLE}
   **                            </ul>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new {@link PreparedStatement} object
   **                            containing the pre-compiled SQL statement that
   **                            will produce {@link ResultSet} objects with the
   **                            given <code>type</code> and
   **                            <code>concurrency</code>.
   **                            Possible object is {@link PreparedStatement}.
   **
   ** @throws ServiceException   if no connection was given or if a database
   **                            access error occurs or this method is called on
   **                            a closed connection.
   */
  public static final PreparedStatement createPreparedStatement(final Connection connection, final String string, int type, int concurrency)
    throws ServiceException {

    if (connection == null)
      throw new ServiceException(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection");

    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(string, type, concurrency);
      if (QUERY_TIMEOUT > 0)
        statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new ServiceException(e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeStatement
  /**
   ** Close a previous prepared statement.
   ** <br>
   ** If the statement has a dependence result set, the result set will also be
   ** closed.
   **
   ** @param  statement          the {@link PreparedStatement} to close.
   **                            Allowed object is {@link PreparedStatement}.
   */
  public static final void closeStatement(final PreparedStatement statement) {
    if (statement == null)
      return;

    try {
      // close the attached result set if any
      closeResultSet(statement.getResultSet());
    }
    catch (SQLException e) {
      // handle silenlty
      e.printStackTrace(System.err);
    }
    finally {
      try {
        // close the statement itself
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
   **                            Allowed object is {@link ResultSet}.
   */
  public static final void closeResultSet(final ResultSet resultSet) {
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
  // Method:   createCallableStatement
  /**
   ** Creates a {@link CallableStatement} object for calling database stored
   ** procedures. The {@link CallableStatement} object provides methods for
   ** setting up its IN and OUT parameters, and methods for executing the call
   ** to a stored procedure.
   ** <p>
   ** Result sets created using the returned {@link CallableStatement} object
   ** will by default be type <code>TYPE_FORWARD_ONLY</code> and have a
   ** concurrency level of <code>CONCUR_READ_ONLY</code>. The holdability of the
   ** created result sets can be determined by calling
   ** <code>getHoldability()</code>.
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            Allowed object is {@link Connection}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new default {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            Possible object is {@link CallableStatement}.
   **
   ** @throws ServiceException   if SQL statement can't created.
   */
  public static final CallableStatement createCallableStatement(final Connection connection, final String string)
    throws ServiceException {

    if (connection == null)
      throw new ServiceException(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection");

    CallableStatement statement = null;
    try {
      statement = connection.prepareCall(string);
      if (QUERY_TIMEOUT > 0)
        statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new ServiceException(e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCallableStatement
  /**
   ** Creates a {@link CallableStatement} object that will generate
   ** {@link ResultSet} objects with the given type and concurrency. This method
   ** is the same as the prepareCall method above, but it allows the default
   ** result set type and concurrency to be overridden. The holdability of the
   ** created result sets can be determined by calling getHoldability().
   **
   ** @param  connection         the JDBC connection to create a statement for.
   **                            Allowed object is {@link String}.
   ** @param  string             an SQL statement that may contain one or more
   **                            <code>'?'</code> IN parameter placeholders.
   **                            Allowed object is {@link String}.
   ** @param  type               a result set type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#TYPE_FORWARD_ONLY}
   **                              <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
   **                              <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
   **                            </ul>
   **                            Allowed object is <code>int</code>.
   ** @param  concurrency        a concurrency type; one of:
   **                            <ul>
   **                              <li>{@link ResultSet#CONCUR_READ_ONLY}
   **                              <li>{@link ResultSet#CONCUR_UPDATABLE}
   **                            </ul>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a new default {@link CallableStatement} object
   **                            containing the pre-compiled SQL statement.
   **                            Possible object is {@link CallableStatement}.
   **
   ** @throws ServiceException   if SQL statement can't created.
   */
  public static final CallableStatement createCallableStatement(final Connection connection, final String string, int type, int concurrency)
    throws ServiceException {

    if (connection == null)
      throw new ServiceException(ServiceError.TYPE_ATTRIBUTE_MANDATORY, "connection");

    CallableStatement statement = null;
    try {
      statement = connection.prepareCall(string, type, concurrency);
      if (QUERY_TIMEOUT > 0)
        statement.setQueryTimeout(QUERY_TIMEOUT);
    }
    catch (SQLException e) {
      throw new ServiceException(e);
    }
    return statement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  @Override
  public void validate()
    throws BuildException {

    // a principal is required for this context type ...
    if (principal() == null)
      handleAttributeMissing("principal");

    // ... and therefore needs also be validated
    principal().validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL (overridden)
  /**
   ** Constructs an service URL to bind to.
   ** <p>
   ** At first it checks if the context URL is set. If so it will return it as
   ** it is.
   **
   ** @return                    the context to bind to.
   **                            Usually for user operations, user container
   **                            context is passed and for group operations,
   **                            group container context is passed.
   **                            Possible object is {@link String}.
   */
  @Override
  public final String serviceURL() {
    if (!StringUtility.isEmpty(this.contextURL))
      return this.contextURL;

    // create a URL from the parts provided by protocol, host and port
    final String[] driver = DRIVER.get(this.type.getValue());
    return parseTemplate(driver[1]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   environment
  /**
   ** Creates the {@link Properties} from the attributes of this task that
   ** afterwards can be passed to establish a connection to the target system.
   **
   ** @return                    the context this connector use to communicate
   **                            with the database server.
   **                            Possible object is {@link Properties}.
   **
   ** @throws ServiceException   if the method is not able to retun a valid
   **                            URL.
   */
  private final Properties environment() {
    final Properties environment = new Properties();
    environment.put(ACCOUNT,  this.username());
    environment.put(PASSWORD, this.password());
    return environment;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Constructs an initial {@link Connection} by creating the appropriate
   ** environment from the instance attributes.
   **
   ** @param  serviceURL         the context URL to estanlich the connection.
   **                            Allowed object is {@link String}.
   ** @param  environment        environment used to create the initial
   **                            {@link Connection}.
   **                            <code>null</code> indicates an empty
   **                            environment.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context this task use to communicate with
   **                            the application server.
   **                            Possible object is {@link Connection}.
   **
   ** @throws ServiceException   if the {@link Connection} could not be
   **                            created at the first time this method is
   **                            invoked.
   */
  private final Connection connect(final String serviceURL, final Properties environment)
    throws ServiceException {

    final String[] properties = DRIVER.get(this.type().getValue());

    // instantiate the JDBC driver class accordingly to the configured type
    Class<?> driverClass = null;
    try {
      driverClass = Class.forName(properties[0]);
    }
    catch (ClassNotFoundException e) {
      throw new ServiceException(ServiceError.CLASSNOTFOUND, e);
    }

    int        attempts   = 1;
    Connection connection = null;
    do {
      DriverManager.setLoginTimeout(this.timeoutConnection());
      try {
        // Constructs a JDBC connection using environment properties
        // See java.sql.DriverManager for a discussion of environment
        // properties.
        connection = DriverManager.getConnection(serviceURL, environment);
        this.established(true);
        break;
      }
      catch (SQLException e) {
        // Check if we have a recoverable error like the target system is
        // temporarly unavailable onky in this case increase the attempts
        // in all other cases we have a misconfugiration the cannot be solved
        // without restart
        if (e.getErrorCode() == 20)
          attempts++;
        else
          attempts += this.retryCount;

        if (attempts > this.retryCount) {
          this.established(false);
          throw new ServiceException(ServiceError.UNHANDLED, e);
        }

        try {
          Thread.sleep(this.retryInterval);
        }
        catch (InterruptedException ex) {
          throw new ServiceException(ServiceError.UNHANDLED, ex);
        }
      }
    } while (attempts <= this.retryCount);
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseTemplate
  /**
   ** Returns a string with all placeholders substituted by the corresponding
   ** value of the provided map.
   ** <p>
   ** A placeholder in generell is specified by
   ** <code>${<i>placeholder name</i>}</code> or
   ** <code>#{<i>placeholder name</i>]</code>.
   ** <p>
   ** The pattern <code>#{<i>placeholder name</i>]</code> request to subtitute
   ** the expression with directly with the correspondedning value taken from
   ** <code>mapping</code>.
   **
   ** @param  urlPattern         the string containing placeholders.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string with all placeholders substituted by
   **                            the corresponding value of the provided
   **                            mapping.
   **                            Possible object is {@link String}.
   */
  private String parseTemplate(final String urlPattern) {
    final Map<String, String> mapping = new HashMap<String, String>();
    mapping.put("type",     type().getValue());
    mapping.put("protocol", protocol());
    mapping.put("host",     host());
    mapping.put("port",     port());
    mapping.put("database", database());

    StringBuffer result = new StringBuffer(urlPattern.length());
    try {
      // If you will be using a particular regular expression often, should
      // create a Pattern object to store the regular expression.
      // You can then reuse the regex as often as you want by reusing the
      // Pattern object.
      Pattern pattern = Pattern.compile(URL_PATTERN, Pattern.MULTILINE);

      // To use the regular expression on a string, create a Matcher object by
      // calling pattern.matcher() passing the subject string to it. The Matcher
      // will do the actual searching, replacing or splitting.
      Matcher match = pattern.matcher(urlPattern);
      while (match.find()) {
        if (match.groupCount() > 0) {
          // Capturing parentheses are numbered 1..groupCount() group number
          // zero is the entire regex match 1 is the value that we would like to
          // substitute
          final String name = match.group(1);
          if (!StringUtility.isEmpty(name)) {
            // check if placeholder is well known and and a key entry with a
            // legal value is existing
            if (mapping.containsKey(name)) {
              final String value = mapping.get(name);
              if (!StringUtility.isEmpty(value))
                match.appendReplacement(result, value);
            }
          }
        }
      }
      match.appendTail(result);
    }
    catch (PatternSyntaxException e) {
      // parameter expression does not contain a valid regular expression
      String[] parameter = {urlPattern, e.getDescription()};
      error(ServiceResourceBundle.string(ServiceError.EXPRESSION_INVALID), parameter);
    }
    catch (IllegalArgumentException ex) {
      // this exception indicates a bug in parameter options
      error(ServiceResourceBundle.string(ServiceError.EXPRESSION_UNDEFINED_BITVALUES));
    }
    return result.toString();
  }
}