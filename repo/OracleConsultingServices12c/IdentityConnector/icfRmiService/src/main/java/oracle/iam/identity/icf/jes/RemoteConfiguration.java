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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   RemoteConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RemoteConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.jes;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.spi.ConfigurationProperty;

import oracle.iam.identity.icf.resource.Connector;

////////////////////////////////////////////////////////////////////////////////
// class RemoteConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Encapsulates the configuration of the connector.
 ** <p>
 ** The configuration includes every property that a caller <b>must</b> specify
 ** in order to use the connector, as well as every property that a caller
 ** <b>may</b> specify in order to affect the behavior of the connector overall
 ** (as opposed to operation options, which affect only a specific invocation of
 ** a specific operation).
 ** <br>
 ** Required configuration parameters ConfigurationProperty.required() generally
 ** include the information required to connect to a target instance -- such as
 ** a URL, username and password.
 ** <br>
 ** Optional configuration parameters often specify preferences as to how the
 ** connector-bundle should behave.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RemoteConfiguration extends ServerConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>RemoteConfiguration</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RemoteConfiguration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerType
  /**
   ** Set the <code>serverType</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>serverType</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=5, displayMessageKey=Connector.Endpoint.SERVER_TYPE_LABEL, helpMessageKey=Connector.Endpoint.SERVER_TYPE_HINT, required=true)
  public final void setServerType(final String value) {
    this.endpoint.serverType(ServerEndpoint.Type.from(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerType
  /**
   ** Returns the <code>serverType</code> attribute for the content provider.
   **
   ** @return                    the <code>serverType</code> attribute for the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getServerType() {
    return this.endpoint.serverType() == null ? null : this.endpoint.serverType().value;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setSecureSocket
  /**
   ** Set the credential of the security principal for the target system used to
   ** connect to.
   **
   ** @param  value              specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system service.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=6, displayMessageKey=Connector.Endpoint.SECURE_SOCKET_LABEL, helpMessageKey=Connector.Endpoint.SECURE_SOCKET_HINT)
  public final void setSecureSocket(final boolean value) {
    this.endpoint.secureSocket(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecureSocket
  /**
   ** Returns the credential of the security principal for the target system
   ** used to connect to.
   **
   ** @return                    specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system service.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getSecureSocket() {
    return this.endpoint.secureSocket();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRootContext
  /**
   ** Call by the ICF framework to inject the argument for parameter
   ** <code>rootContext</code>.
   **
   ** @param  value              the value for the attribute
   **                            <code>rootContext</code> used as the
   **                            content provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=7, displayMessageKey=Connector.Endpoint.ROOT_CONTEXT_LABEL, helpMessageKey=Connector.Endpoint.ROOT_CONTEXT_HINT)
  public final void setRootContext(final String value) {
    this.endpoint.rootContext(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRootContext
  /**
   ** Returns the rootContext type of the content used to connect to.
   **
   ** @return                    the value for the attribute
   **                            <code>rootContext</code> used as the
   **                            content provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getRootContext() {
    return this.endpoint.rootContext();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalPassword
  /**
   ** Set the credential of the security principal for the target system used to
   ** connect to.
   **
   ** @param  value              the credential of the security principal for
   **                            the target system used to connect to.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=8, displayMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_HINT, required=true)
  public final void setPrincipalPassword(final GuardedString value) {
    this.endpoint.principalPassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPrincipalPassword
  /**
   ** Returns the credential of the security principal for the target system
   ** used to connect to.
   **
   ** @return                    the credential of the security principal for
   **                            the target system used to connect to.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString getPrincipalPassword() {
    return this.endpoint.principalPassword();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setDomainPrincipal
  /**
   ** Set the username of the domain administrator principal for the target
   ** system used to connect to.
   **
   ** @param  value              the username of the domain administrator
   **                            principal for the target system used to connect
   **                            to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=9, displayMessageKey=Connector.Endpoint.DOMAIN_USERNAME_LABEL, helpMessageKey=Connector.Endpoint.DOMAIN_USERNAME_HINT)
  public final void setDomainPrincipal(final String value) {
    this.endpoint.domainPrincipal(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDomainPrincipal
  /**
   ** Returns the username of the domain administrator principal for the target
   ** system used to connect to.
   **
   ** @return                    the username of the domain administrator
   **                            principal for the target system used to connect
   **                            to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getDomainPrincipal() {
    return this.endpoint.domainPrincipal();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setDomainPassword
  /**
   ** Set the credential of the domain administrator principal for the target 
   ** system used to connect to.
   **
   ** @param  value              the credential of the domain administrator
   **                            principal for the target system used to connect
   **                            to.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=10, displayMessageKey=Connector.Endpoint.DOMAIN_PASSWORD_LABEL, helpMessageKey=Connector.Endpoint.DOMAIN_PASSWORD_HINT)
  public final void setDomainPassword(final GuardedString value) {
    this.endpoint.domainPassword(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDomainPassword
  /**
   ** Returns the credential of the domain administrator principal for the target 
   ** system used to connect to.
   **
   ** @return                    the credential of the domain administrator
   **                            principal for the target system used to connect
   **                            to.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString getDomainPassword() {
    return this.endpoint.domainPassword();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setLoginConfig
  /**
   ** Set the JASS login configuration of the Managed Server used to connect to.
   **
   ** @param  value              the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=11, displayMessageKey=Connector.Endpoint.LOGIN_CONFIG_LABEL, helpMessageKey=Connector.Endpoint.LOGIN_CONFIG_HINT, required=true)
  public final void setLoginConfig(final String value) {
    this.endpoint.loginConfig(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLoginConfig
  /**
   ** Returns the credential of the domain administrator principal for the target 
   ** system used to connect to.
   **
   ** @return                    the security configuration of the Managed
   **                            Server used to connect to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getLoginConfig() {
    return this.endpoint.loginConfig();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setLanguage
  /**
   ** Set the <code>language</code> attribute.
   **
   ** @param  value              the value for the attribute
   **                            <code>language</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=12, displayMessageKey=Connector.Endpoint.LANGUAGE_LABEL, helpMessageKey=Connector.Endpoint.LANGUAGE_HINT)
  public final void setLanguage(final String value) {
    this.endpoint.localeLanguage(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLanguage
  /**
   ** Returns the <code>language</code> attribute.
   **
   ** @return                    the <code>language</code> attribute.
   **                            <br>
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=13, displayMessageKey=Connector.Endpoint.COUNTRY_LABEL, helpMessageKey=Connector.Endpoint.COUNTRY_HINT)
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=14, displayMessageKey=Connector.Endpoint.TIMEZONE_LABEL, helpMessageKey=Connector.Endpoint.TIMEZONE_HINT)
  public final void setTimeZone(final String value) {
    this.endpoint.localeTimeZone(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTimeZone
  /**
   ** Returns the <code>timeZone</code> attribute.
   **
   ** @return                    the <code>timeZone</code> attribute.
   **                            <br>
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
   ** @param  value              the timeout to establish a connection socket.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=15, displayMessageKey=Connector.Connection.CONNECT_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.CONNECT_TIMEOUT_HINT)
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
   ** @return                    the timeout to establish a connection socket.
   **                            <br>
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
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=16, displayMessageKey=Connector.Connection.RESPONSE_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.RESPONSE_TIMEOUT_HINT)
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getResponseTimeOut() {
    return this.endpoint.timeOutResponse();
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
