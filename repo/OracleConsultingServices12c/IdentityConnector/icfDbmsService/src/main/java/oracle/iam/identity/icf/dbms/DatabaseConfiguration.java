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

    File        :   DatabaseConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.dbms;

import oracle.iam.identity.icf.resource.Connector;
import oracle.iam.identity.icf.resource.ConnectorBundle;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Encapsulates the configuration of the connector.
 ** <p>
 ** The configuration includes every property that a caller MUST specify in
 ** order to use the connector, as well as every property that a caller MAY
 ** specify in order to affect the behavior of the connector overall (as opposed
 ** to operation options, which affect only a specific invocation of a specific
 ** operation).
 ** <br>
 ** Required configuration parameters ConfigurationProperty.required() generally
 ** include the information required to connect to a target instance--such as a
 ** URL, username and password.
 ** <br>
 ** Optional configuration parameters often specify preferences as to how the
 ** connector-bundle should behave.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseConfiguration extends AbstractConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name for the target system directory.
   */
  private DatabaseEndpoint endpoint = DatabaseEndpoint.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default database connector <code>DatabaseConfiguration</code>
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DatabaseConfiguration() {
    // ensure inheritance
    super();

    // ensure inheritance
    setConnectorMessages(ConnectorBundle.RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDatabaseDriver
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>driver</code>.
   **
   ** @param  driver             the value for the attribute <code>driver</code>
   **                            used as the database provider.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=1, displayMessageKey=Connector.Endpoint.DRIVER_LABEL, helpMessageKey=Connector.Endpoint.DRIVER_HINT)
  public final void setDatabaseDriver(final String driver) {
    this.endpoint.databaseDriver(driver);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDatabaseDriver
  /**
   ** Returns the driver type of the database used to connect to.
   **
   ** @return                    the type of the database used to connect to.
   **                            Possible object is {@link String}.
   */
  public final String getDatabaseDriver() {
    return this.endpoint.databaseDriver();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerHost
  /**
   ** Set the <code>primaryHost</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryHost</code> used as the content
   **                            provider.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=2, displayMessageKey=Connector.Endpoint.HOST_LABEL, helpMessageKey=Connector.Endpoint.HOST_HINT, required=true)
  public final void setServerHost(final String value) {
    this.endpoint.primaryHost(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerHost
  /**
   ** Returns the <code>primaryHost</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryHost</code> attribute for the
   **                            content provider.
   **                            Possible object is {@link String}.
   */
  public final String getServerHost() {
    return this.endpoint.primaryHost();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerPort
  /**
   ** Set the <code>primaryPort</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryPort</code> used as the content
   **                            provider.
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=3, displayMessageKey=Connector.Endpoint.PORT_LABEL, helpMessageKey=Connector.Endpoint.PORT_HINT, required=true)
  public final void setServerPort(final int value) {
    this.endpoint.primaryPort(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerPort
  /**
   ** Returns the <code>primaryPort</code> attribute for the content provider.
   **
   ** @return                    the <code>primaryPort</code> attribute for the
   **                            content provider.
   **                            Possible object is <code>int</code>.
   */
  public final int getServerPort() {
    return this.endpoint.primaryPort();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setSecureSocket
  /**
   ** Set the credential of the security principal for the directory used to
   ** connect to.
   **
   ** @param  value              specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system directory.
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=4, displayMessageKey=Connector.Endpoint.SECURE_LABEL, helpMessageKey=Connector.Endpoint.SECURE_HINT)
  public void setSecureSocket(final boolean value) {
    this.endpoint.secureSocket(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecureSocket
  /**
   ** Returns the credential of the security principal for the directory used to
   ** connect to.
   **
   ** @return                    specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system directory.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getSecureSocket() {
    return this.endpoint.secureSocket();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalUsername
  /**
   ** Set the username of the security principal for the directory used to
   ** connect to.
   **
   ** @param  value              the username of the security principal for the
   **                            directory used to connect to.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=5, displayMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_HINT)
  public void setPrincipalUsername(final String value) {
    this.endpoint.principalUsername(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalUsername
  /**
   ** Returns the username of the security principal for the directory used to
   ** connect to.
   **
   ** @return                    the username of the security principal for the
   **                            directory used to connect to.
   **                            Possible object is {@link String}.
   */
  public String getPrincipalUsername() {
    return this.endpoint.principalUsername();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalPassword
  /**
   ** Set the credential of the security principal for the directory used to
   ** connect to.
   **
   ** @param  value              the credential of the security principal for
   **                            the directory used to connect to.
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=6, displayMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_HINT)
  public void setPrincipalPassword(final GuardedString value) {
    this.endpoint.principalPassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalPassword
  /**
   ** Returns the credential of the security principal for the directory used to
   ** connect to.
   **
   ** @return                    the credential of the security principal for
   **                            the directory used to connect to.
   **                            Possible object is {@link GuardedString}.
   */
  public GuardedString getPrincipalPassword() {
    return this.endpoint.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDatabaseName
  /**
   ** Set the <code>name</code> attribute.
   **
   ** @param  value              the value for the attribute <code>name</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=7, displayMessageKey=Connector.Endpoint.NAME_LABEL, helpMessageKey=Connector.Endpoint.NAME_HINT)
  public final void setDatabaseName(final String value) {
    this.endpoint.rootContext(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDatabaseName
  /**
   ** Returns the <code>name</code> attribute.
   **
   ** @return                    the <code>name</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getDatabaseName() {
    return this.endpoint.rootContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDatabaseSchema
  /**
   ** Set the <code>schema</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>schema</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=8, displayMessageKey=Connector.Endpoint.SCHEMA_LABEL, helpMessageKey=Connector.Endpoint.SCHEMA_HINT)
  public final void setDatabaseSchema(final String value) {
    this.endpoint.databaseSchema(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDatabaseSchema
  /**
   ** Returns the <code>schema</code> attribute.
   **
   ** @return                    the <code>schema</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getDatabaseSchema() {
    return this.endpoint.databaseSchema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLanguage
  /**
   ** Set the <code>language</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>language</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=9, displayMessageKey=Connector.Endpoint.LANGUAGE_LABEL, helpMessageKey=Connector.Endpoint.LANGUAGE_HINT)
  public final void setLanguage(final String value) {
    this.endpoint.localeLanguage(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLanguage
  /**
   ** Returns the <code>language</code> attribute.
   **
   ** @return                    the <code>language</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getLanguage() {
    return this.endpoint.localeLanguage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCountry
  /**
   ** Set the <code>country</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>country</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=10, displayMessageKey=Connector.Endpoint.COUNTRY_LABEL, helpMessageKey=Connector.Endpoint.COUNTRY_HINT)
  public final void setCountry(final String value) {
    this.endpoint.localeCountry(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCountry
  /**
   ** Returns the <code>country</code> attribute.
   **
   ** @return                    the <code>country</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getCountry() {
    return this.endpoint.localeCountry();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone
  /**
   ** Set the <code>timeZone</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>timeZone</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=11, displayMessageKey=Connector.Endpoint.TIMEZONE_LABEL, helpMessageKey=Connector.Endpoint.TIMEZONE_HINT)
  public final void setTimeZone(final String value) {
    this.endpoint.localeTimeZone(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTimeZone
  /**
   ** Returns the <code>timeZone</code> attribute.
   **
   ** @return                    the <code>timeZone</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getTimeZone() {
    return this.endpoint.localeTimeZone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionTimeOut
  /**
   ** Sets the timeout period to establish a connection to the content provider.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @param  value              the timeout to establish a JMX connection.
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=12, displayMessageKey=Connector.Connection.CONNECT_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.CONNECT_TIMEOUT_HINT)
  public final void setConnectionTimeOut(final int value) {
    this.endpoint.timeOutConnect(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionTimeOut
  /**
   ** Returns the timeout period for establishment of the connection.
   ** <p>
   ** This property affects the TCP timeout when opening a connection to a
   ** content provider. When connection pooling has been requested, this
   ** property also specifies the maximum wait time or a connection when all
   ** connections in pool are in use and the maximum pool size has been reached.
   ** <p>
   ** If this property has not been specified, the content provider will wait
   ** indefinitely for a pooled connection to become available, and to wait for
   ** the default TCP timeout to take effect when creating a new connection.
   **
   ** @return                    the timeout to establish a connection.
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionTimeOut() {
    return this.endpoint.timeOutConnect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResponseTimeOut
  /**
   ** Sets the timeout period the service consumer doesn't get a response.
   ** <p>
   ** When an servive request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @param  value              timeout period the service consumer will wait
   **                            for a response.
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=13, displayMessageKey=Connector.Connection.RESPONSE_TIMEOUT_HINT, helpMessageKey=Connector.Connection.RESPONSE_TIMEOUT_HINT)
  public final void setResponseTimeOut(final int value) {
    this.endpoint.timeOutResponse(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResponseTimeOut
  /**
   ** Returns the timeout period the service consumer doesn't get a response.
   ** <p>
   ** When an servive request is made by a client to a server and the server
   ** does not respond for some reason, the client waits forever for the
   ** server to respond until the TCP timeouts. On the client-side what the
   ** user experiences is esentially a process hang. In order to control the
   ** service request in a timely manner, a read timeout can be configured for
   ** the service provider.
   ** <p>
   ** If this property is not specified, the default is to wait for the
   ** response until it is received.
   **
   ** @return                    timeout period the service consumer will wait
   **                            for a response.
   **                            Possible object is <code>int</code>.
   */
  public final int getResponseTimeOut() {
    return this.endpoint.timeOutResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionRetryCount
  /**
   ** Set the number of consecutive attempts to be made at establishing a
   ** connection with the Database Service.
   **
   ** @param  value              the number of consecutive attempts to be made
   **                            at establishing a connection with the Database
   **                            Service.
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=14, displayMessageKey=Connector.Connection.CONNECT_RETRYCOUNT_LABEL, helpMessageKey=Connector.Connection.CONNECT_RETRYCOUNT_HINT)
  public final void setConnectionRetryCount(final int value) {
    this.endpoint.retryCount(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionRetryCount
  /**
   ** Returns the number of consecutive attempts to be made at establishing a
   ** connection with the Database Service.
   **
   ** @return                    the timeout period for establishment of the
   **                            Database Service connection.
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionRetryCount() {
    return this.endpoint.retryCount();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setConnectionRetryInterval
  /**
   ** Set the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Database Service.
   **
   ** @param  value              the interval (in milliseconds) between
   **                            consecutive attempts at establishing a
   **                            connection with the Database Service.
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=15, displayMessageKey=Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, helpMessageKey=Connector.Connection.CONNECT_RETRYINTERVAL_HINT)
  public final void setConnectionRetryInterval(final int value) {
    this.endpoint.retryInterval(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getConnectionRetryInterval
  /**
   ** Returns the interval (in milliseconds) between consecutive attempts at
   ** establishing a connection with the Database Service.
   **
   ** @return                    the timeout period for establishment of the
   **                            Database Service connection.
   **                            Possible object is <code>int</code>.
   */
  public final int getConnectionRetryInterval() {
    return this.endpoint.retryInterval();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSystemTimestamp
  /**
   ** Set the <code>systemTimestamp</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>systemTimestamp</code>.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=16, displayMessageKey=Connector.Feature.SYSTEM_TIMESTAMP_LABEL, helpMessageKey=Connector.Feature.SYSTEM_TIMESTAMP_HINT)
  public final void setSystemTimestamp(final String value) {
    this.endpoint.systemTimestamp(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSystemTimestamp
  /**
   ** Returns the <code>systemTimestamp</code> attribute.
   **
   ** @return                    the <code>systemTimestamp</code> attribute.
   **                            Possible object is {@link String}.
   */
  public final String getSystemTimestamp() {
    return this.endpoint.systemTimestamp();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setEnforceAutoCommit
  /**
   ** Sets the <code>enforceAutoCommit</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>enforceAutoCommit</code>.
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=17, displayMessageKey=Connector.Feature.ENFORCE_AUTOCOMMIT_LABEL, helpMessageKey=Connector.Feature.ENFORCE_AUTOCOMMIT_HINT)
  public void setEnforceAutoCommit(final boolean value) {
    this.endpoint.enforceAutoCommit(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnforceAutoCommit
  /**
   ** Returns the <code>enforceAutoCommit</code> attribute.
   **
   ** @return                    the value for the attribute
   **                            <code>enforceAutoCommit</code>.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean getEnforceAutoCommit() {
    return this.endpoint.enforceAutoCommit();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPseudoRownumAttribute
  /**
   ** Set the name of the pseudo attribute used in paged result sets.
   **
   ** @param  value              the name of the pseudo attribute used in paged
   **                            result sets.
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=18, displayMessageKey=Connector.Feature.ROWNUM_ATTRIBUTE_LABEL, helpMessageKey=Connector.Feature.ROWNUM_ATTRIBUTE_HINT)
  public final void setPseudoRownumAttribute(final String value) {
    this.endpoint.rowNumberAttribute(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPseudoRownumAttribute
  /**
   ** Returns the name of the pseudo attribute used in paged result sets.
   **
   ** @return                    the name of the pseudo attribute used in paged
   **                            result sets.
   **                            Possible object is {@link String}.
   */
  public final String getPseudoRownumAttribute() {
    return this.endpoint.rowNumberAttribute();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFetchSchema
  /**
   ** Set the value of the extended configuration attribute that specifies
   ** the schema definition needs always to be fetched from the Service
   ** Provider.
   **
   ** @param  value              the value of the extended configuration
   **                            attribute that specifies schema definition
   **                            needs always to be fetched from the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=19, displayMessageKey=Connector.Feature.FETCH_SCHEMA_LABEL, helpMessageKey=Connector.Feature.FETCH_SCHEMA_HINT)
  public final void setFetchSchema(final boolean value) {
    this.endpoint.fetchSchema(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getFetchSchema
  /**
   ** Returns the value of the extended configuration attribute that specifies
   ** the schema definition needs always to be fetched from the Service
   ** Provider.
   **
   ** @return                    the value of the extended configuration
   **                            attribute that specifies schema definition
   **                            needs always to be fetched from the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getFetchSchema() {
    return this.endpoint.fetchSchema();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link DatabaseEndpoint} this configuration intialize.
   **
   ** @return                    the {@link DatabaseEndpoint} this
   **                            configuration initialize.
   **                            Possible object {@link DatabaseEndpoint}.
   */
  public final DatabaseEndpoint endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (AbstractConfiguration)
  /**
   ** Determines if the configuration is valid.
   ** <p>
   ** A valid configuration is one that is ready to be used by the connector: it
   ** is complete (all the required properties have been given values) and the
   ** property values are well-formed (are in the expected range, have the
   ** expected format, etc.)
   ** <p>
   ** Implementations of this method <b>should not</b> connect to the resource
   ** in an attempt to validate the configuration. For example, implementations
   ** should not attempt to check that a host of the specified name exists by
   ** making a connection to it. Such checks can be performed in the
   ** implementation of the TestOp.test() method.
   **
   ** @throws RuntimeException   if the configuration is not valid.
   **                            Implementations are encouraged to throw the
   **                            most specific exception available. When no
   **                            specific exception is available,
   **                            implementations can throw
   **                            ConfigurationException.
   */
  @Override
  public void validate() {
    // intentionally left blank
  }
}