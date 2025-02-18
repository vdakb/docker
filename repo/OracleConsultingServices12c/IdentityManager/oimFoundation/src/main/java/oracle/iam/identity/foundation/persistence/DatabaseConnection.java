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

    File        :   DatabaseConnection.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseConnection.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.net.ConnectException;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import oracle.iam.platform.Platform;

import com.oracle.oim.gcp.pool.ConnectionService;

import com.oracle.oim.gcp.resourceconnection.ResourceConnection;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.naming.ServiceLocator;
import oracle.hst.foundation.naming.LocatorException;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLoggable;
import oracle.iam.identity.foundation.AbstractMetadataTask;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseConnection
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseConnection</code> implements the base functionality
 ** of an Oracle Identity Manager Connector for a Database Service.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DatabaseConnection extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String    ENVIRONMENT_ACCOUNT  = "user";
  protected static final String    ENVIRONMENT_PASSWORD = "password";
  protected static final String    ENVIRONMENT_INTERNAL = "internal_logon";
  protected static final String    ENVIRONMENT_SYSDBA   = "SYSDBA";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the wrapper of target specific parameters where this connector is attached
   ** to
   */
  protected final DatabaseResource resource;

  /**
   ** the wrapper of target specific features where this connector is attached
   ** to
   */
  protected final DatabaseFeature  feature;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseConnection</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  serverInstance     the system identifier of the
   **                            <code>IT Resource</code> instance where this
   **                            connector is associated with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public DatabaseConnection(final AbstractMetadataTask task, final Long serverInstance)
    throws TaskException {

    this(task, new DatabaseResource(task, serverInstance));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseConnection</code> which is associated with the
   ** specified task.
   **
   ** @param  task               the {@link AbstractMetadataTask} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link DatabaseResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   **
   ** @throws TaskException      if the feature configuration cannot be created.
   */
  public DatabaseConnection(final AbstractMetadataTask task, final DatabaseResource resource)
    throws TaskException {

    // ensure inheritance
    this(task, resource, unmarshal(task, resource.feature()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseConnection</code> which is associated with the
   ** specified task.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   ** @param  resource           the {@link DatabaseResource} IT Resource
   **                            definition where this connector is associated
   **                            with.
   ** @param  feature            the Metadata Descriptor providing the target
   **                            system specific features like objectClasses,
   **                            attribute id's etc.
   */
  public DatabaseConnection(final Loggable loggable, final DatabaseResource resource, final DatabaseFeature feature) {
    // ensure inheritance
    super(loggable);

    // create the property mapping for the Database control
    this.resource = resource;
    this.feature  = feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceInstance
  /**
   ** Returns the instance key of the IT Resource where this wrapper belongs to.
   **
   ** @return                    the instance key of the IT Resource where this
   **                            wrapper belongs to.
   */
  public final long resourceInstance() {
    return this.resource.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Returns the instance name of the IT Resource where this wrapper belongs
   ** to.
   **
   ** @return                    the instance name of the IT Resource where this
   **                            wrapper belongs to.
   */
  public final String resourceName() {
    return this.resource.name();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureSocket
  /**
   ** Returns whether the connection to the Database Server is secured by SSL.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#SECURE_SOCKET}.
   **
   ** @return                    <code>true</code> if the connection to the
   **                            Database Server is secured by SSL,
   **                            <code>false</code> otherwise.
   */
  public final boolean secureSocket() {
    return this.resource.secureSocket();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalName
  /**
   ** Returns the name of the security principal of the Database Server used to
   ** connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#PRINCIPAL_NAME}.
   **
   ** @return                    the name of the security principal Database
   **                            Server used to connect to.
   */
  public final String principalName() {
    return this.resource.principalName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   principalPassword
  /**
   ** Returns the password of the security principal of the Database Server used
   ** to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#PRINCIPAL_PASSWORD}.
   **
   ** @return                    the password of the security principal Database
   **                            Server used to connect to.
   */
  public final String principalPassword() {
    return this.resource.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeCountry
  /**
   ** Returns the country code of the Database Server used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#LOCALE_COUNTRY}.
   **
   ** @return                    the country code of the Database Server used to
   **                            connect to.
   */
  public final String localeCountry() {
    return this.resource.localeCountry();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeLanguage
  /**
   ** Returns the language code of the Database Server used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#LOCALE_LANGUAGE}.
   **
   ** @return                    the language code of the Database Server used
   **                            to connect to.
   */
  public final String localeLanguage() {
    return this.resource.localeLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localeTimeZone
  /**
   ** Returns the time zone of the Database Server used to connect to.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#LOCALE_TIMEZONE}.
   **
   ** @return                    the time tone of the Database Server used to
   **                            connect to.
   */
  public final String localeTimeZone() {
    return this.resource.localeTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseSchema
  /**
   ** Returns the database statement to obtain the name of the cataloge schema
   ** of the database database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseResource#DATABASE_SCHEMA}.
   **
   ** @return                    the database statement to obtain the name of
   **                            the cataloge schema of the database database.
   */
  public final String databaseSchema() {
    return this.resource.databaseSchema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   multiValueSeparator
  /**
   ** Returns separator character for Strings that provides more than one value.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#MULTIVALUE_SEPARATOR}.
   ** <p>
   ** If {@link DatabaseConstant#MULTIVALUE_SEPARATOR} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DatabaseConstant#MULTIVALUE_SEPARATOR_DEFAULT}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String multiValueSeparator() {
    return this.feature.multiValueSeparator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseDriver
  /**
   ** Returns depending on the Database Server that you are using the JDBC
   ** driver class.
   ** <p>
   ** Enter one of the following values as the JDBC driver class name:
   ** <pre>
   **  +----------------------+----------------------------------------------+
   **  | Database Type        | Driver Class Name                            |
   **  +----------------------+----------------------------------------------+
   **  | Oracle Database      | oracle.jdbc.driver.OracleDriver              |
   **  | Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
   **  | MySQL                | com.mysql.jdbc.Driver                        |
   **  | IBM DB2 UDB          | com.ibm.db2.jcc.DB2Driver                    |
   **  | Sybase               | com.sybase.jdbc2.jdbc.SybDriver              |
   **  | PostgreSQL           | org.postgresql.Driver                        |
   **  +----------------------+----------------------------------------------+
   ** </pre>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#DATABASE_DRIVER_CLASS}.
   ** <p>
   ** If {@link DatabaseConstant#DATABASE_DRIVER_CLASS} is not mapped in the
   ** underlying <code>Metadata Descriptor</code> this method returns
   ** {@link DatabaseConstant#DATABASE_DRIVER_ORACLE}
   **
   ** @return                    separator sign for Strings that provides more
   **                            than one value.
   */
  public final String databaseDriverClass() {
    return this.feature.databaseDriverClass();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimeStatement
  /**
   ** Returns the database statement to obtain the current timestamp from the
   ** database.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#DATABASE_SYSTEM_TIMESTAMP}.
   **
   ** @return                    the database statement to obtain the current
   **                            timestamp from the database.
   */
  public final String systemTimeStatement() {
    return this.feature.systemTimeStatement();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceAutoCommit
  /**
   ** Returns <code>true</code> the JDBC Auto Commit feature needs stay on
   ** enabled after a connection was auqired from the pool.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENFORCE_AUTO_COMMIT}.
   **
   ** @return                    <code>true</code> the JDBC Auto Commit feature
   **                            needs stay on enabled after a connection was
   **                            aquired from the pool.
   */
  public final boolean enforceAutoCommit() {
    return this.feature.enforceAutoCommit();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowNumberAttribute
  /**
   ** Returns the name of the pseudo attribute used in paged result sets.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ROW_NUMBER_ATTRIBUTE}.
   **
   ** @return                    the name of the pseudo attribute used in paged
   **                            result sets.
   */
  public final String rowNumberAttribute() {
    return this.feature.rowNumberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryCreatedAttribute
  /**
   ** Returns the name of the attribute to detect the created timestamp of a
   ** database entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTRY_CREATED_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute to detect the
   **                            created timestamp.
   */
  public final String entryCreatedAttribute() {
    return this.feature.entryCreatedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entryModifiedAttribute
  /**
   ** Returns the name of the attribute to detect the modified timestamp of a
   ** database entry.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTRY_MODIFIED_ATTRIBUTE}.
   **
   ** @return                    the name of the attribute to detect the
   **                            modified timestamp.
   */
  public final String entryModifiedAttribute() {
    return this.feature.entryModifiedAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementPrefixRequired
  /**
   ** Returns the <code>true</code> if the entitlements has to be prefixed with
   ** the internal system identifier and the name of the
   ** <code>IT Resource</code>.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link DatabaseConstant#ENTITLEMENT_PREFIX_REQUIRED}.
   **
   ** @return                    <code>true</code> the entitlements has to be
   **                            prefixed with the internal system identifier
   **                            and the name of the <code>IT Resource</code>;
   **                            otherwise <code>false</code>.
   */
  public final boolean entitlementPrefixRequired() {
    return this.feature.entitlementPrefixRequired();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
  /**
   ** Returns feature mapping of the connector.
   **
   ** @return                    feature mapping of the connector.
   */
  public final DatabaseFeature feature() {
    return this.feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSource
  /**
   ** Retrieves a datasource for a passed name from Java ENC.
   **
   ** @param  name               the Java ENC name of a JEE datasource.
   **
   ** @return                    a JEE datasource.
   **
   ** @throws DatabaseException  if the datasource cannot be found in the JNDI
   **                            namespace for the specified name
   */
  public static DataSource dataSource(final String name)
    throws DatabaseException {

    DataSource datasource = null;
    try {
      if (ServiceLocator.isJBoss())
        // JBoss is not able to lookup a DataSource in the standardised fashion
        datasource = (DataSource)ServiceLocator.lookup("java:" + name);
      else
        // all others should also be able to lookup a DataSource in the
        // standardizised fashion
        datasource = (DataSource)ServiceLocator.lookup(name);
    }
    catch (LocatorException e) {
      throw new DatabaseException(e);
    }

    return datasource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime
  /**
   ** The current time may be different from this system time.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the server time from the target system.
   ** <p>
   ** This current implementation check if the system is connected and ask the
   ** connected server for the system timestamp. If the connection is not valid
   ** at the time this method is invoked it returns simple the current data of
   ** the machine where this library is used.
   **
   ** @param  query              the statement to lookup the current system time
   **                            from the default {@link Connection}.
   **
   ** @return                    the timestamp of the remote system if
   **                            applicable; the local time otherwise.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public static final Date systemTime(final String query)
    throws DatabaseException {

    final Connection connection = aquire();
    try {
      return systemTime(connection, query);
    }
    finally {
      release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime (overridden)
  /**
   ** The Database Server current time will be different from the local time.
   ** <p>
   ** This current implementation ask the connected server for the current
   ** time using the configured property
   ** {@link DatabaseConstant#DATABASE_SYSTEM_TIMESTAMP}.
   ** <p>
   ** The assumption is that the configuration provides a single query statement
   ** that is able to fectch the system timestamp from the database server. For
   ** example a statement like:
   ** <pre>
   **   SELECT systimestamp FROM dual
   ** </pre>
   ** will work for an Oracle Database.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   ** @param  query              the statement to lookup the current system time
   **                            of the specified {@link Connection}.
   **
   ** @return                    the timestamp of the remote system.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public static Date systemTime(final Connection connection, final String query)
    throws DatabaseException {

    if (StringUtility.isEmpty(query))
      throw new DatabaseException(DatabaseError.ABORT, DatabaseConstant.DATABASE_SYSTEM_TIMESTAMP);

    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    Timestamp         timestamp =null;
    try {
      statement = DatabaseStatement.createPreparedStatement(connection, query);
      resultSet = statement.executeQuery();
      timestamp = resultSet.next() ? resultSet.getTimestamp(1) : new Timestamp(0L);
      return new Date(timestamp.getTime());
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Retrieves a JDBC connection for a given name from Java ENC.
   **
   ** @param  name               the Java ENC name of a JEE datasource.
   **
   ** @return                    a JDBC connection.
   **
   ** @throws DatabaseException  if no datasource was given, a JDBC
   **                            connection cannot be obtained or if the
   **                            datasource cannot be found in the JNDI
   **                            namespace for the specified name
   */
  public static Connection connection(final String name)
    throws DatabaseException {

    return aquire(dataSource(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Aquire the Oracle Identity Manager JDBC resources. All SQLExceptions are
   ** silently handled.
   **
   ** @return                    the JDBC connection.
   **
   ** @throws DatabaseException  if no datasource was given or a JDBC
   **                            connection cannot be obtained.
   */
  public static final Connection aquire()
    throws DatabaseException {

    Connection connection = null;
    try {
      connection = Platform.getOperationalDS().getConnection();
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** Aquire the given JDBC resources. All SQLExceptions are silently handled.
   **
   ** @param  datasource         the datasource.
   **
   ** @return                    the JDBC connection.
   **
   ** @throws DatabaseException  if no datasource was given or a JDBC
   **                            connection cannot be obtained.
   */
  public static final Connection aquire(final DataSource datasource)
    throws DatabaseException {

    if (datasource == null)
      throw new DatabaseException(DatabaseError.ARGUMENT_IS_NULL, "datasource");

    Connection connection = null;
    try {
      connection = datasource.getConnection();
      connection.setAutoCommit(false);
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <br>
   ** The name of the JDBC DataSource passed in must be the JNDI name of the
   ** configuration. Use AS Control to check the JNDI name before you configure
   ** the name in the IT Resource definition.
   ** <br>
   ** Make sure that the user configured in the JDBC DataSource to connect to
   ** the database has the privileges to read DBA objects. This is normally
   ** achived by setting the property
   ** <pre>
   **  property name="internal_logon" value"sysdba"
   ** </pre>
   ** at the <code>connection-factory</code> of the <code>connetion-pool</code>
   ** associated with the JDBC DataSource.
   ** <br>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @param  resource           the {@link DatabaseResource} providing the
   **                            service endpoint configurations details.
   ** @param  feature            the {@link DatabaseFeature} providing the
   **                            additional configurations details.
   **
   ** @return                    the {@link Connection} created by the database
   **                            driver interface.
   **
   ** @throws TaskException      if the JDBC driver class could not be found on
   **                            the classpath.
   ** @throws DatabaseException  if connection could not be established for any
   **                            reason.
   */
  public static Connection aquire(final DatabaseResource resource, final DatabaseFeature feature)
    throws TaskException {

    try {
      Class.forName(feature.databaseDriverClass());
    }
    catch (ClassNotFoundException e) {
      throw TaskException.classNotFound(feature.databaseDriverClass());
    }

    Properties environment = new Properties();
    environment.put(ENVIRONMENT_ACCOUNT,  resource.principalName());
    environment.put(ENVIRONMENT_PASSWORD, resource.principalPassword());

    Connection connection = null;
    int attempts = 1;
    do {
      DriverManager.setLoginTimeout(resource.connectionTimeout());
      try {
        connection = DriverManager.getConnection(resource.databaseURL(), environment);
        // always set auto commit to false to delegate the transaction control
        // to  the application
        connection.setAutoCommit(feature.enforceAutoCommit());
        // some databases like MySQL needs to set the catalog
        // if the database is not specified, the connection is made with no
        // default database. In this case, either call the setCatalog() method
        // on the Connection instance, or fully specify table names using the
        // database name in your SQL
        if (!StringUtility.isEmpty(resource.databaseName()))
          connection.setCatalog(resource.databaseName());
        break;
      }
      catch (SQLRecoverableException e) {
        attempts++;
        if (attempts > resource.connectionRetryCount()) {
          if (e.getCause() != null) {
            if (e.getCause() instanceof ConnectException)
              throw new DatabaseException(DatabaseError.CONNECTION_ERROR, e);
          }
          throw new DatabaseException(e);
        }
        try {
          Thread.sleep(resource.connectionRetryInterval());
        }
        catch (InterruptedException ex) {
          throw new TaskException(ex);
        }
      }
      catch (SQLException e) {
        throw new DatabaseException(e);
      }
    } while (attempts <= resource.connectionRetryCount());
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   release
  /**
   ** Release the given JDBC resources.
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to release.
   */
  public static final void release(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        connection.close();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   opened
  /**
   ** Ensures that given JDBC connection is valid in terms of:
   ** <ol>
   **   <li><code>connection</code> is not <code>null</code>.
   **   <li><code>connection</code> is not closed.
   ** </ol>
   ** <p>
   ** All SQLExceptions are silently handled.
   **
   ** @param  connection         the JDBC connection to validate.
   **
   ** @return                    <code>true</code> if the given
   **                            {@link Connection} is non-<code>null</code> and
   **                            not closed; otherwise it returns
   **                            <code>false</code>.
   */
  public static final boolean opened(final Connection connection) {
    if (connection != null) {
      try {
        // make sure that we will commit our unit of work if neccessary
        return !connection.isClosed();
      }
      catch (SQLException e) {
        // handle silenlty
        e.printStackTrace(System.err);
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   commitConnection
  /**
   ** Commit a transaction in supplied connection
   **
   ** @param  connection         the JDBC connection which will handle the
   **                            commit.
   **
   ** @throws TaskException      if connection cannot be commited.
   */
  public static final void commitConnection(final Connection connection)
    throws TaskException {

    if (null != connection) {
      try {
        connection.commit();
      }
      catch (SQLException e) {
        throw new DatabaseException(e);
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
   ** @throws TaskException      if connection cannot be rollback.
   */
  public static final void rollbackConnection(final Connection connection)
    throws TaskException {

    if (null != connection) {
      try {
        connection.rollback();
      }
      catch (SQLException e) {
         throw new DatabaseException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <br>
   ** The name of the JDBC DataSource passed in must be the JNDI name of the
   ** configuration. Use AS Control to check the JNDI name before you configure
   ** the name in the IT Resource definition.
   ** <br>
   ** Make sure that the user configured in the JDBC DataSource to connect to
   ** the database has the privileges to read DBA objects. This is normally
   ** achived by setting the property
   ** <pre>
   **  property name="internal_logon" value"sysdba"
   ** </pre>
   ** at the <code>connection-factory</code> of the <code>connetion-pool</code>
   ** associated with the JDBC DataSource.
   ** <br>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @param  datasource         the name of the IT Resource providing the
   **                            connection details,
   **
   ** @return                    the {@link Connection} aquired from the pool of
   **                            connections.
   **
   ** @throws DatabaseException  if the operation fails
   */
  protected static Connection aquire(final long datasource)
    throws DatabaseException {

    final ConnectionService service = new ConnectionService();
    try {
      return (Connection)service.getConnection(datasource);
    }
    catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aquire
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <br>
   ** The name of the JDBC DataSource passed in must be the JNDI name of the
   ** configuration. Use AS Control to check the JNDI name before you configure
   ** the name in the IT Resource definition.
   ** <br>
   ** Make sure that the user configured in the JDBC DataSource to connect to
   ** the database has the privileges to read DBA objects. This is normally
   ** achived by setting the property
   ** <pre>
   **  property name="internal_logon" value"sysdba"
   ** </pre>
   ** at the <code>connection-factory</code> of the <code>connetion-pool</code>
   ** associated with the JDBC DataSource.
   ** <br>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @param  datasource         the name of the IT Resource providing the
   **                            connection details.
   **
   ** @return                    the {@link Connection} aquired from the pool of
   **                            connections.
   **
   ** @throws DatabaseException  if the operation fails
   */
  protected static Connection aquire(final String datasource)
    throws DatabaseException {

    final ConnectionService service = new ConnectionService();
    try {
      return (Connection)service.getConnection(datasource);
    }
    catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link DatabaseFeature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **
   ** @return                    the {@link DatabaseFeature} created from the
   **                            specified propertyFile.
   **
   ** @throws DatabaseException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected static DatabaseFeature unmarshal(final AbstractMetadataTask task, final String path)
    throws DatabaseException {

    DatabaseFeature feature = new DatabaseFeature(task);
    try {
      final MDSSession session  = task.createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      DatabaseFeatureFactory.configure(feature, document);
    }
    catch (ReferenceException e) {
      throw new DatabaseException(e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(this.resource.toString());
    buffer.append(this.feature.toString());
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** The specified name of a JDBC DataSource is used to aquire a database
   ** connection used for provisioning and reconciliation.
   ** <br>
   ** The name of the JDBC DataSource passed in must be the JNDI name of the
   ** configuration. Use AS Control to check the JNDI name before you configure
   ** the name in the IT Resource definition.
   ** <br>
   ** Make sure that the user configured in the JDBC DataSource to connect to
   ** the database has the privileges to read DBA objects. This is normally
   ** achived by setting the property
   ** <pre>
   **  property name="internal_logon" value"sysdba"
   ** </pre>
   ** at the <code>connection-factory</code> of the <code>connetion-pool</code>
   ** associated with the JDBC DataSource.
   ** <br>
   ** The JDBC {@link Connection} should be released by invoking the
   ** <code>close()</code> method on this instance. If this is omitted may be
   ** the connection pool provided by the specified JDBC DataSource has no
   ** longer an available connection.
   **
   ** @return                    the {@link Connection} aquired either from the
   **                            pool of connections or directly created by the
   **                            database driver interface.
   **
   ** @throws TaskException      if the JDBC driver class could not be found on
   **                            the classpath.
   ** @throws DatabaseException  if connection could not be established for any
   **                            reason.
   */
  public Connection connect()
    throws TaskException {

    final String method = "connect";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      if (this.resource != null)
        if (this.resource.poolSupported())
          return aquire(this.resource.instance());
        else
          return aquire(this.resource, this.feature);
      else
        return aquire();
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close the connection aquired by {@link #connect()} and frees all
   ** resources.
   **
   ** @param  connection         the {@link Connection} to close.
   */
  public void disconnect(final Connection connection) {
    final String method = "disconnect";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      if (this.resource != null && this.resource.poolSupported()) {
        final ConnectionService service = new ConnectionService();
        service.releaseConnection((ResourceConnection)connection);
      }
      else
        release(connection);
    }
    catch (Exception e) {
      throw new RuntimeException("Failed to return the database connection to the connection pool.", e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime (overridden)
  /**
   ** The Database Server current time will be different from the local time.
   ** <p>
   ** This current implementation ask the connected server for the current
   ** time using the configured property
   ** {@link DatabaseConstant#DATABASE_SYSTEM_TIMESTAMP}.
   ** <p>
   ** The assumption is that the configuration provides a single query statement
   ** that is able to fectch the system timestamp from the database server. For
   ** example a statement like:
   ** <pre>
   **   SELECT systimestamp FROM dual
   ** </pre>
   ** will work for an Oracle Database.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   **
   ** @return                    the timestamp of the remote system.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public Date systemTime(final Connection connection)
    throws DatabaseException {

    final String query = systemTimeStatement();
    if (StringUtility.isEmpty(query))
      throw new DatabaseException(DatabaseError.ABORT, DatabaseConstant.DATABASE_SYSTEM_TIMESTAMP);

    final String method = "systemTime";
    trace(method, SystemMessage.METHOD_ENTRY);

    PreparedStatement statement = null;
    ResultSet         resultSet = null;
    Timestamp         timestamp =null;
    try {
      statement = DatabaseStatement.createPreparedStatement(connection, query);
      resultSet = statement.executeQuery();
      timestamp = resultSet.next() ? resultSet.getTimestamp(1) : new Timestamp(0L);
      return new Date(timestamp.getTime());
    }
    catch (SQLException e) {
      throw new DatabaseException(e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   existsEntry
  /**
   ** Returns <code>true</code> if a row exists in the specified
   ** {@link DatabaseEntity} thats primary attribute has the specified value
   ** <code>entryValue</code>.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   ** @param  entity             the entity to search within
   ** @param  entryValue         the value for {@link DatabaseEntity#primary()}
   **                            that the row in the returned result must match.
   **
   ** @return                    the {@link Map} of the attributes and their
   **                            values that matches the specified entryValue.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public boolean existsEntry(final Connection connection, final DatabaseEntity entity, final Object entryValue)
    throws DatabaseException {

    final DatabaseFilter            filter    = DatabaseFilter.build(entity.primary(), entryValue, DatabaseFilter.Operator.EQUAL);
    final DatabaseSelect            statement = DatabaseSelect.build(this, entity, filter, entity.returning());
    final List<Map<String, Object>> result    = statement.execute(connection);
    return (result.size() > 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEntry
  /**
   ** Retrieves the attributes specified by <code>returning</code> of an entity
   ** that matches the specified <code>entryValue</code>.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   ** @param  entity             the entity to search within
   ** @param  entryValue         the value for {@link DatabaseEntity#primary()}
   **                            that the row in the returned result must match.
   ** @param  returning          the names of the attribute that has to be
   **                            returned in the result set.
   **
   ** @return                    the {@link Map} of the attributes and their
   **                            values that matches the specified entryValue.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public Map<String, Object> findEntry(final Connection connection, final DatabaseEntity entity, final Object entryValue, final List<Pair<String, String>> returning)
    throws DatabaseException {

    final DatabaseFilter filter = DatabaseFilter.build(entity.primary(), entryValue, DatabaseFilter.Operator.EQUAL);
    return findEntry(connection, entity, filter, returning);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findEntry
  /**
   ** Retrieves the attributes specified by <code>returning</code> of an entity
   ** that matches the specified <code>entryValue</code>.
   **
   ** @param  connection         the {@link Connection} to used for the search.
   ** @param  entity             the entity to search within
   ** @param  filter             the filter criteria to build for the SQL
   **                            statement the row in the returned result must
   **                            match.
   ** @param  returning          the names of the attribute their has to be
   **                            returned in the result set.
   **
   ** @return                    the {@link Map} of the attributes and their
   **                            values that matches the specified entryValue.
   **
   ** @throws DatabaseException  in case the search operation cannot be
   **                            performed.
   */
  public Map<String, Object> findEntry(final Connection connection, final DatabaseEntity entity, final DatabaseFilter filter, final List<Pair<String, String>> returning)
    throws DatabaseException {

    final DatabaseSelect            statement = DatabaseSelect.build(this, entity, filter, returning);
    final List<Map<String, Object>> result    = statement.execute(connection);
    if (result.size() != 1) {
      final String[] arguments = {filter.toString(), entity.toString()};
      throw new DatabaseException(result.size() == 0 ? DatabaseError.OBJECT_NOT_EXISTS : DatabaseError.OBJECT_AMBIGUOUS, arguments);
    }
    return result.get(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertEntry
  /**
   ** Inserts entry in the specified <code>entity</code> and binds the passed
   ** attribute <code>data</code> to it.
   **
   ** @param  connection         the {@link Connection} to used for the
   **                            operation.
   ** @param  entity             the database entity the entry will be created
   **                            within.
   ** @param  data               the attributes of the entry to create.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public void insertEntry(final Connection connection, final DatabaseEntity entity, final Map<String, Object> data)
    throws DatabaseException {

    final DatabaseInsert statement = DatabaseInsert.build(this, entity, data.keySet());
    statement.execute(connection, data);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEntry
  /**
   ** Deletes all entries from the specified <code>entity</code> that match the
   ** specified <code>filter</code>.
   **
   ** @param  connection         the {@link Connection} to used for the
   **                            operation.
   ** @param  entity             the database entity that is target of the
   **                            delete operation.
   ** @param  filter             the filter expression to use for the delete.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public void deleteEntry(final Connection connection, final DatabaseEntity entity, final DatabaseFilter filter)
    throws DatabaseException {

    final DatabaseDelete statement = DatabaseDelete.build(this, entity, filter);
    statement.execute(connection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateEntry
  /**
   ** Updates all entres in the specified <code>entity</code> that match  the
   ** and specified <code>filter</code> binds the passed attributes
   ** <code>data</code> to it.
   **
   ** @param  connection         the {@link Connection} to used for the
   **                            operation.
   ** @param  entity             the database entity the entry will be updated
   **                            within.
   ** @param  filter             the filter expression to use for the update.
   ** @param  data               the attributes of the entry to modify.
   **
   ** @throws DatabaseException  if the operation fails
   */
  public void updateEntry(final Connection connection, final DatabaseEntity entity, final DatabaseFilter filter, final Map<String, Object> data)
    throws DatabaseException {

    final DatabaseUpdate statement = DatabaseUpdate.build(this, entity, filter, data.keySet());
    statement.execute(connection, data);
  }
}