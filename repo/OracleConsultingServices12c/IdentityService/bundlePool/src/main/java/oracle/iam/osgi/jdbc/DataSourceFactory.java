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
    Subsystem   :   Generic Persistence Interface

    File        :   DataSourceFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    DataSourceFactory

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.osgi.jdbc;

import java.util.Properties;

import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.sql.ConnectionPoolDataSource;

////////////////////////////////////////////////////////////////////////////////
// interface DataSourceFactory
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A factory for JDBC connection factories.
 ** There are 3 preferred connection factories for getting JDBC connections:
 ** <ol>
 **   <li><code>javax.sql.DataSource</code>
 **   <li><code>javax.sql.ConnectionPoolDataSource}</code>
 **   <li><code>javax.sql.XADataSource</code>
 ** </ol>
 ** DataSource providers should implement this interface and register it as an
 ** OSGi service with the JDBC driver class name in the
 ** {@link #OSGI_JDBC_DRIVER_CLASS} property.
 */
public interface DataSourceFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

	/**
	 ** Service property used by a JDBC driver to declare the driver class when
	 ** registering a JDBC DataSourceFactory service. Clients may filter or test
	 ** this property to determine if the driver is suitable, or the desired one.
	 */
	static final String	OSGI_JDBC_DRIVER_CLASS   = "osgi.jdbc.driver.class";

  /**
   ** Service property used by a JDBC driver to declare the driver name when
   ** registering a JDBC DataSourceFactory service. Clients may filter or test
   ** this property to determine if the driver is suitable, or the desired one.
   */
  static final String OSGI_JDBC_DRIVER_NAME    = "osgi.jdbc.driver.name";

  /**
   ** Service property used by a JDBC driver to declare the driver version when
   ** registering a JDBC DataSourceFactory service. Clients may filter or test
   ** this property to determine if the driver is suitable, or the desired one.
   */
  static final String OSGI_JDBC_DRIVER_VERSION = "osgi.jdbc.driver.version";

  /**
   ** The "databaseName" property that DataSource clients should supply a value
   ** for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_DATABASE_NAME       = "databaseName";

  /**
   ** The "dataSourceName" property that DataSource clients should supply a
   ** value for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_DATASOURCE_NAME     = "dataSourceName";

  /**
   ** The "description" property that DataSource clients should supply a value
   ** for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_DESCRIPTION         = "description";

  /**
   ** The "networkProtocol" property that DataSource clients should supply a
   ** value for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_NETWORK_PROTOCOL    = "networkProtocol";

  /**
   ** The "password" property that DataSource clients should supply a value for
   ** when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_PASSWORD            = "password";

  /**
   ** The "portNumber" property that DataSource clients should supply a value
   ** for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_PORT_NUMBER         = "portNumber";

  /**
   ** The "roleName" property that DataSource clients should supply a value for
   ** when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_ROLE_NAME          = "roleName";

  /**
   ** The "serverName" property that DataSource clients should supply a value
   ** for when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_SERVER_NAME        = "serverName";

  /**
   ** The "user" property that DataSource clients should supply a value for
   ** when calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_USER               = "user";

  /**
   ** The "url" property that DataSource clients should supply a value for when
   ** calling {@link #createDataSource(Properties)}.
   */
  static final String JDBC_URL                = "url";

  /**
   ** The "initialPoolSize" property that ConnectionPoolDataSource and
   ** XADataSource clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_INITIAL_POOL_SIZE  = "initialPoolSize";

  /**
   ** The "maxIdleTime" property that ConnectionPoolDataSource and XADataSource
   ** clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_MAX_IDLE_TIME      = "maxIdleTime";

  /**
   ** The "maxPoolSize" property that ConnectionPoolDataSource and XADataSource
   ** clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_MAX_POOL_SIZE      = "maxPoolSize";

  /**
   ** The "maxStatements" property that ConnectionPoolDataSource and
   ** XADataSource clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_MAX_STATEMENTS     = "maxStatements";

  /**
   ** The "minPoolSize" property that ConnectionPoolDataSource and XADataSource
   ** clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_MIN_POOL_SIZE      = "minPoolSize";

  /**
   ** The "propertyCycle" property that ConnectionPoolDataSource and
   ** XADataSource clients may supply a value for when calling
   ** {@link #createConnectionPoolDataSource(Properties)} or
   ** {@link #createXADataSource(Properties)} on drivers that support this
   ** property.
   */
  static final String JDBC_PROPERTY_CYCLE     = "propertyCycle";

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDataSource
  /**
   ** Create a new {@code DataSource} using the given properties.
   **
   ** @param  config              the properties used to configure the
   **                            {@link DataSource}.
   **                            <code>null</code> indicates no properties. If
   **                            the property cannot be set on the
   **                            {@link DataSource} being created then a
   **                            {@link SQLException} must be thrown.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    a configured {@code DataSource}.
   **                            <br>
   **                            Possible object is {@link DataSource}.
   **
   ** @throws SQLException       if the {@link DataSource} cannot be created.
   */
  DataSource createDataSource(final Properties config)
    throws SQLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConnectionPoolDataSource
  /**
   ** Create a new {@link ConnectionPoolDataSource} using the given properties.
   **
   ** @param  config             the properties used to configure the
   **                            {@link ConnectionPoolDataSource}.
   **                            <code>null</code> indicates no properties. If
   **                            the property cannot be set on the
   **                            {@link ConnectionPoolDataSource} being created
   **                            then a {@link SQLException} must be thrown.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    a configured {@code ConnectionPoolDataSource}.
   **                            <br>
   **                            Possible object is
   **                            {@link ConnectionPoolDataSource}.
   **
   ** @throws SQLException       if the {@link ConnectionPoolDataSource} cannot
   **                            be created.
   */
  ConnectionPoolDataSource createConnectionPoolDataSource(final Properties config)
    throws SQLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createXADataSource
  /**
   ** Create a new {@link XADataSource} using the given properties.
   **
   ** @param  config             the properties used to configure the
   **                            {@link XADataSource}.
   **                            <code>null</code> indicates no properties. If
   **                            the property cannot be set on the
   **                            {@link XADataSource} being created then a
   **                            {@link SQLException} must be thrown.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    a configured {@code XADataSource}.
   **                            <br>
   **                            Possible object is {@link XADataSource}.
   **
   ** @throws SQLException       if the {@link XADataSource} cannot be created.
   */
  XADataSource createXADataSource(final Properties config)
    throws SQLException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDriver
  /**
   ** Create a new {@link Driver} using the given properties.
   **
   ** @param  config             the properties used to configure the
   **                            {@link Driver}.
   **                            <code>null</code> indicates no properties. If
   **                            the property cannot be set on the
   **                            {@link Driver} being created then a
   **                            {@link SQLException} must be thrown.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    a configured {@code Driver}.
   **                            <br>
   **                            Possible object is {@link Driver}.
   **
   ** @throws SQLException       if the {@link Driver} cannot be created.
   */
  Driver createDriver(final Properties config)
    throws SQLException;
}
