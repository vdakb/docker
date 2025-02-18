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

    File        :   DatabaseEndpoint.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import java.util.Map;
import java.util.EnumMap;

import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import org.identityconnectors.common.security.GuardedString;

///////////////////////////////////////////////////////////////////////////////
// class DatabaseEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseEndpoint</code> wraps the Database Service endpoint
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseEndpoint<T extends DatabaseEndpoint> extends AbstractEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Map<Driver, String> DRIVER = new EnumMap<Driver, String>(Driver.class);

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    /**
     ** Default value of the Oracle Type 4 JDBC Driver class and Oracle JDBC URL.
     */
    DRIVER.put(Driver.ORACLE4,   "jdbc:oracle:thin:@%h:%p/%d");
    /**
     ** Default value of the Oracle Type 2 JDBC Driver class and Oracle JDBC URL.
     */
    DRIVER.put(Driver.ORACLE2,   "jdbc:oracle:thin:@%h:%p/%d");
    /**
     ** Default value of the MySQL Server JDBC Driver class and MySQL Server
     ** JDBC URL.
     */
    DRIVER.put(Driver.MYSQL,     "jdbc:mysql://%h:%p/%d");
    /**
     ** Default value of the PostgreSQL Server JDBC Driver class and PostgreSQL
     ** Server JDBC URL.
     */
    DRIVER.put(Driver.PSQL,      "jdbc:postgresql://%h:%p/%d");
    /**
     ** Default value of the Microsoft SQL Server JDBC Driver class and
     ** Microsoft SQL Server JDBC URL.
     */
    DRIVER.put(Driver.SQLSERVER, "jdbc:microsoft:sqlserver://%h:%d");
    /**
     ** Default value of the Sybase Server Type 3 JDBC Driver class and Sybase
     ** Server JDBC URL.
     */
    DRIVER.put(Driver.SYBASE3,   "jdbc:sybase:Tds:%h:%d");
    /**
     ** Default value of the Sybase Server Type 2 JDBC Driver class and Sybase
     ** Server JDBC URL.
     */
    DRIVER.put(Driver.SYBASE2,   "jdbc:sybase:Tds:%h:%d");
    /**
     ** Default value of the IBM Universal Database Server JDBC Driver class and
     ** IBM Universal Database Server JDBC URL.
     */
    DRIVER.put(Driver.UDB,       "jdbc:db2://%h:%p/%d");
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ***
   */
  private boolean fetchSchema        = false;
  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify depending on the Database Server that you are using the JDBC
   ** driver class.
   ** <p>
   ** Enter one of the following values as the JDBC driver class name:
   ** <pre>
   **  +----------------------+----------------------------------------------+
   **  | Database Type        | Driver Class Name                            |
   **  +----------------------+----------------------------------------------+
   **  | Oracle Database      | oracle.jdbc.OracleDriver                     |
   **  |                      | oracle.jdbc.driver.OracleDriver              |
   **  | Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
   **  | MySQL                | com.mysql.jdbc.Driver                        |
   **  | PostgreSQL           | org.postgresql.Driver                        |
   **  | Sybase               | com.sybase.jdbc2.jdbc.SybDriver              |
   **  |                      | com.sybase.jdbc3.jdbc.SybDriver              |
   **  | IBM DB2 UDB          | com.ibm.db2.jcc.DB2Driver                    |
   **  +----------------------+----------------------------------------------+
   ** </pre>
   */
  private Driver  databaseDriver     = Driver.ORACLE4;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the name of the catalog used.
   ** <br>
   ** Some databases like MySQL needs to set the catalog if the database is not
   ** specified, the connection is made with no default database. In this case,
   ** either call the setCatalog() method on the Connection instance, or fully
   ** specify table names using the database name in your SQL.
   */
  private String  databaseCatalog    = null;
  /**
   ** Attribute tag which may be defined on an <code>IT Resource</code> to
   ** specify the cataloge schema name to be used.
   */
  private String  databaseSchema     = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the database statement to obtain the current timestamp from the
   ** database.
   */
  private String  systemTimestamp    = null;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify that the JDBC Auto Commit feature needs stay on enabled if an
   ** connection is auqired from the pool.
   ** <p>
   ** This feature is only necessary to support DB2. This stupid database
   ** still needs a commit after each select to remove the read locks from
   ** the page.
   */
  private boolean enforceAutoCommit  = false;
  /**
   ** Attribute tag which must be defined on a <code>Metadata Descriptor</code>
   ** to specify the pseudo column used by paged resultsets.
   */
  private String  rowNumberAttribute = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ///////////////////////////////////////////////////////////////////////////////
  // enum Driver
  // ~~~~ ~~~~~~
  public static enum Driver {
      ORACLE2("oracle.jdbc.driver.OracleDriver")
    , ORACLE4("oracle.jdbc.OracleDriver")
    , MYSQL("com.mysql.jdbc.Driver")
    , PSQL("org.postgresql.Driver")
    , SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    , SYBASE2("com.sybase.jdbc2.jdbc.SybDriver")
    , SYBASE3("com.sybase.jdbc3.jdbc.SybDriver")
    , UDB("com.ibm.db2.jcc.DB2Driver")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Driver</code> with a constraint value.
     **
     ** @param  value            the class name of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Driver(final String id) {
      this.id = id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the id property of the <code>Driver</code> constraint.
     **
     ** @return                  the id property of the <code>Driver</code>
     **                          constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper driver from the given string value.
     **
     ** @param  value            the string value the driver should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the driver property.
     **                          <br>
     **                          Possible object is <code>Driver</code>.
     */
    public static Driver from(final String value) {
      for (Driver cursor : Driver.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractEndpoint}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private DatabaseEndpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseEndpoint</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DatabaseEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  driver             the value for the attribute <code>driver</code>
   **                            used as the database provider.
   **                            <br>
   **                            Allowed object is {@link Driver}.
   ** @param  server             the server properties on which the Service
   **                            Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  schema             the cataloge schema name to be used with the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the Service Provider
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private DatabaseEndpoint(final Loggable loggable, final Driver driver, final Server server, final String rootContext, final String schema, final Principal principal, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable, server, rootContext, principal, secureSocket, language, country, timeZone);

    // initialize instance attributes
    this.databaseDriver = driver;
    this.databaseSchema = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Set <code>true</code> if the schema description should be obtain natively
   ** from the Database Service.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Database Service.
   ** <p>
   ** Per default no roundtrib to the Database Service happens.
   **
   ** @param  value              <code>true</code> if the schema description
   **                            should be obtain natively from the Database
   **                            Service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public final void fetchSchema(final boolean value) {
    this.fetchSchema = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchSchema
  /**
   ** Returns <code>true</code> if the schema description should be obtain
   ** natively from the Database Service.
   ** <br>
   ** If <code>false</code> is specified a well know static schmea description
   ** is populated without a roundtrip to the Database Service.
   ** <p>
   ** Per default no roundtrib to the Database Service happens.
   **
   ** @return                    <code>true</code> if the schema description
   **                            should be obtain natively from the Database
   **                            Service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean fetchSchema() {
    return this.fetchSchema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseDriver
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>driver</code>.
   **
   ** @param  driver             the value for the attribute <code>driver</code>
   **                            used as the database provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void databaseDriver(final String driver) {
    this.databaseDriver = Driver.from(driver);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseDriver
  /**
   ** Returns the driver type of the database used to connect to.
   **
   ** @return                    the type of the database used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String databaseDriver() {
    return this.databaseDriver.id();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseCatalog
  /**
   ** Set the <code>databaseCatalog</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>databaseCatalog</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void databaseCatalog(final String value) {
    this.databaseCatalog = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseCatalog
  /**
   ** Returns the <code>databaseCatalog</code> attribute.
   **
   ** @return                    the <code>databaseCatalog</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String databaseCatalog() {
    return this.databaseCatalog;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseSchema
  /**
   ** Set the <code>databaseSchema</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>databaseSchema</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void databaseSchema(final String value) {
    this.databaseSchema = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   databaseSchema
  /**
   ** Returns the <code>databaseSchema</code> attribute.
   **
   ** @return                    the <code>databaseSchema</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String databaseSchema() {
    return this.databaseSchema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSystemTimestamp
  /**
   ** Set the <code>systemTimestamp</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>systemTimestamp</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void systemTimestamp(final String value) {
    this.systemTimestamp = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTimestamp
  /**
   ** Returns the <code>systemTimestamp</code> attribute.
   **
   ** @return                    the <code>systemTimestamp</code> attribute.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String systemTimestamp() {
    return this.systemTimestamp;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setEnforceAutoCommit
  /**
   ** Sets the <code>enforceAutoCommit</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>enforceAutoCommit</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public void enforceAutoCommit(final boolean value) {
    this.enforceAutoCommit = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceAutoCommit
  /**
   ** Returns the <code>enforceAutoCommit</code> attribute.
   **
   ** @return                    the value for the attribute
   **                            <code>enforceAutoCommit</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean enforceAutoCommit() {
    return this.enforceAutoCommit;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowNumberAttribute
  /**
   ** Set the name of the pseudo attribute used in paged result sets.
   **
   ** @param  value              the name of the pseudo attribute used in paged
   **                            result sets.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void rowNumberAttribute(final String value) {
    this.rowNumberAttribute = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rowNumberAttribute
  /**
   ** Returns the name of the pseudo attribute used in paged result sets.
   **
   ** @return                    the name of the pseudo attribute used in paged
   **                            result sets.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String rowNumberAttribute() {
    return this.rowNumberAttribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   driver
  /**
   ** Returns the {@link Driver} this configuration intialize.
   **
   ** @return                    the {@link Driver} this configuration
   **                            initialize.
   **                            <br>
   **                            Possible object {@link Driver}.
   */
  public final Driver driver() {
    return this.databaseDriver;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseEndpoint</code> without a
   ** {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @return                    an newly created instance of
   **                            <code>DatabaseEndpoint</code> providing default
   **                            values only.
   **                            <br>
   **                            Possible object <code>DatabaseEndpoint</code>.
   */
  public static DatabaseEndpoint build() {
    return build(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiates the {@link DatabaseEndpoint}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an newly created instance of
   **                            <code>DatabaseEndpoint</code> providing default
   **                            values only.
   **                            <br>
   **                            Possible object <code>DatabaseEndpoint</code>.
   */
  public static DatabaseEndpoint build(final Loggable loggable) {
    return new DatabaseEndpoint(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DatabaseEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  driver             the value for the attribute <code>driver</code>
   **                            used as the database provider.
   **                            Allowed object is {@link Driver}.
   ** @param  serverHost         the host name or IP address of the target
   **                            system on which the Database Service is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  serverPort         the port the Database Service is listening on
   **                            <br>
   **                            Default value for non-SSL: 1521
   **                            Default value for SSL: 4521
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  databaseSchema     the cataloge schema name to be used with the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the acccount of the Database Service with
   **                            administrator privikeges.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **                            target system.
   ** @param  language           the language code of the Service Provider
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>DatabaseEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>DatabaseEndpoint</code>.
   */
  public static DatabaseEndpoint build(final Loggable loggable, final Driver driver, final String serverHost, final int serverPort, final String rootContext, final String databaseSchema, final String principalName, final GuardedString principalPassword, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return build(loggable, driver, new Server(serverHost, serverPort), rootContext, databaseSchema == null ? principalName : databaseSchema, new Principal(principalName, principalPassword), secureSocket, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>DatabaseEndpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>DatabaseEndpoint</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  driver             the value for the attribute <code>driver</code>
   **                            used as the database provider.
   **                            <br>
   **                            Allowed object is {@link Driver}.
   ** @param  primary            the primary server properties on which the
   **                            Service Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  rootContext        the fully qualified domain name of the parent
   **                            or root organization.
   **                            <br>
   **                            For example, the root suffix.
   **                            <br>
   **                            Format: <code>ou=<i>ORGANIZATION_NAME</i>,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Sample value: <code>ou=Adapters, dc=adomain</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  schema             the cataloge schema name to be used with the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  language           the language code of the Service Provider
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            Country code of the ervice Provider
   **                            <br>
   **                            Default value: US
   **                            <br>
   **                            Note: You must specify the value in uppercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  timeZone           use this parameter to specify the time zone of
   **                            the target system. For example: GMT-08:00 and
   **                            GMT+05:30.
   **                            <br>
   **                            During a provisioning operation, the connector
   **                            uses this time zone information to convert
   **                            date-time values entered on the process form to
   **                            date-time values relative to the time zone of
   **                            the target system.
   **                            <br>
   **                            Default value: GMT
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an newly created instance of
   **                            <code>DatabaseEndpoint</code> populated with
   **                            the provided value.
   **                            <br>
   **                            Possible object <code>DatabaseEndpoint</code>.
   */
  public static final DatabaseEndpoint build(final Loggable loggable, final Driver driver, final Server primary, final String rootContext, final String schema, final Principal principal, final boolean secureSocket, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return new DatabaseEndpoint(loggable, driver, primary, rootContext, schema, principal, secureSocket, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Format a URL given a pattern.
   ** <p>
   ** Recognized template characters are:
   **
   **  % literal % h host p port d database
   **
   ** @return                    a database url of the form
   **                            <code>jdbc:<em>subprotocol</em>:<em>subname</em></code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String serviceURL() {
    final StringBuffer b = new StringBuffer();
    final String pattern = DRIVER.get(this.databaseDriver);
    final int len = pattern.length();
    for (int i = 0; i < len; i++) {
      char ch = pattern.charAt(i);
      if (ch != '%')
        b.append(ch);
      else if (i + 1 < len) {
        i++;
        ch = pattern.charAt(i);
        if (ch == '%')
          b.append(ch);
        else if (ch == 'h')
          b.append(this.primaryHost());
        else if (ch == 'p')
          b.append(this.primaryPort());
        else if (ch == 'd')
          b.append(this.rootContext);
      }
    }
    return b.toString();
  }
}