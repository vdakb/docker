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
    Subsystem   :   Generic WebService Connector

    File        :   ServiceConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceConfiguration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest;

import java.net.URI;

import org.identityconnectors.common.security.GuardedString;

import org.identityconnectors.framework.spi.ConfigurationProperty;

import oracle.iam.identity.icf.connector.AbstractConfiguration;

import oracle.iam.identity.icf.rest.resource.Connector;
import oracle.iam.identity.icf.rest.resource.ConnectorBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServiceConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
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
public class ServiceConfiguration extends AbstractConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String     CATEGORY = "JCS.CONNECTOR.GWS";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on an <code>IT Resource</code> to
   ** specify the host name for the target <code>Service Endpoint</code>.
   */
  protected final ServiceEndpoint endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>ServiceConfiguration</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServiceConfiguration() {
    // ensure inheritance
    super(CATEGORY);

    // initialize instance
    setConnectorMessages(ConnectorBundle.RESOURCE);

    // initialize instance attributes
    this.endpoint = ServiceEndpoint.build(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link ServiceEndpoint} this configuration intialize.
   **
   ** @return                    the {@link ServiceEndpoint} this
   **                            configuration initialize.
   **                            <br>
   **                            Possible object {@link ServiceEndpoint}.
   */
  public final ServiceEndpoint endpoint() {
    return this.endpoint;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setServerHost
  /**
   ** Set the <code>primaryHost</code> attribute for the content provider.
   **
   ** @param  value              the value for the attribute
   **                            <code>primaryHost</code> used as the content
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=1, displayMessageKey=Connector.Endpoint.SERVER_HOST_LABEL, helpMessageKey=Connector.Endpoint.SERVER_HOST_HINT, required=true)
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
   **                            <br>
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
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @ConfigurationProperty(order=2, displayMessageKey=Connector.Endpoint.SERVER_PORT_LABEL, helpMessageKey=Connector.Endpoint.SERVER_PORT_HINT, required=true)
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int getServerPort() {
    return this.endpoint.primaryPort();
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
  @ConfigurationProperty(order=3, displayMessageKey=Connector.Endpoint.CONTEXT_LABEL, helpMessageKey=Connector.Endpoint.CONTEXT_HINT)
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
  // Method:   setSecureSocket
  /**
   ** Set the credential of the security principal for the directory used to
   ** connect to.
   **
   ** @param  value              specifies whether or not to use TLS to secure
   **                            communication between Identity Manager and the
   **                            target system directory.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=4, displayMessageKey=Connector.Endpoint.SECURE_LABEL, helpMessageKey=Connector.Endpoint.SECURE_HINT)
  public final void setSecureSocket(final boolean value) {
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getSecureSocket() {
    return this.endpoint.secureSocket();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setContentType
  /**
   ** Set the content media type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @param  value              the content media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=5, displayMessageKey=Connector.Endpoint.TYPE_CONTENT_LABEL, helpMessageKey=Connector.Endpoint.TYPE_CONTENT_HINT)
  public final void setContentType(final String value) {
    this.endpoint.typeContent(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContentType
  /**
   ** Returns the content media type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @return                    the content media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getContentType() {
    return this.endpoint.typeContent();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAcceptType
  /**
   ** Set the accepted type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @param  value              the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=6, displayMessageKey=Connector.Endpoint.TYPE_ACCEPT_LABEL, helpMessageKey=Connector.Endpoint.TYPE_ACCEPT_HINT)
  public final void setAcceptType(final String value) {
    this.endpoint.typeAccept(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAcceptType
  /**
   ** Returns the accepted type send to the server at any request except
   ** authentication requests to the Service Provider.
   **
   ** @return                    the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAcceptType() {
    return this.endpoint.typeAccept();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthenticationScheme
  /**
   ** Set authentication schema to authenticate security principal for the
   ** service provider.
   **
   ** @param  value              the schema used to authenticate security
   **                            principal for the service provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=7, displayMessageKey=Connector.Endpoint.AUTHENTICATION_SCHEME_LABEL, helpMessageKey=Connector.Endpoint.AUTHENTICATION_SCHEME_HINT)
  public final void setAuthenticationScheme(final String value) {
    this.endpoint.authenticationScheme(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthenticationScheme
  /**
   ** Returns authentication schema to authenticate security principal for the
   ** service provider.
   **
   ** @return                    the schema used to authenticate security
   **                            principal for the service provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getAuthenticationScheme() {
    return this.endpoint.authenticationScheme();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthorizationServer
  /**
   ** Sets the <code>Authorization Server</code> attribute exposed by the
   ** Service Provider endpoint for authentication purpose.
   **
   ** @param  value              the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=8, displayMessageKey=Connector.Endpoint.AUTHORIZATION_SERVER_LABEL, helpMessageKey=Connector.Endpoint.AUTHORIZATION_SERVER_HINT)
  public final void setAuthorizationServer(final URI value) {
    this.endpoint.authorizationURI(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAuthorizationServer
  /**
   ** Returns the <code>Authorization Server</code> attribute exposed by the
   ** Service Provider endpoint for authentication purpose.
   **
   ** @return                    the <code>Authorization Server</code> attribute
   **                            exposed by the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI getAuthorizationServer() {
    return this.endpoint.authorizationURI();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setClientIdentifier
  /**
   ** Set the identifier of the client to authenticate the connector at the
   ** Service Provider.
   **
   ** @param  value              the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=9, displayMessageKey=Connector.Endpoint.CLIENT_IDENTIFIER_LABEL, helpMessageKey=Connector.Endpoint.CLIENT_IDENTIFIER_HINT)
  public final void setClientIdentifier(final String value) {
    this.endpoint.clientIdentifier(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientIdentifier
  /**
   ** Returns client identifier to authenticate the service client at the
   ** Service Provider.
   **
   ** @return                    token to authenticate the service client at the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getClientIdentifier() {
    return this.endpoint.clientIdentifier();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setClientSecret
  /**
   ** Set the credential of the client to authenticate the connector at the
   ** Service Provider.
   **
   ** @param  value              the credential of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=10, displayMessageKey=Connector.Endpoint.CLIENT_SECRET_LABEL, helpMessageKey=Connector.Endpoint.CLIENT_SECRET_HINT)
  public final void setClientSecret(final GuardedString value) {
    this.endpoint.clientSecret(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getClientSecret
  /**
   ** Returns credential to authenticate the service client at the Service
   ** Provider.
   **
   ** @return                    token to authenticate the service client at the
   **                            Service Provider.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString getClientSecret() {
    return this.endpoint.clientSecret();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setPrincipalUsername
  /**
   ** Set the username of the security principal for the directory used to
   ** connect to.
   **
   ** @param  value              the username of the security principal for the
   **                            directory used to connect to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=11, displayMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_USERNAME_HINT)
  public final void setPrincipalUsername(final String value) {
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
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getPrincipalUsername() {
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
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=12, displayMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL, helpMessageKey=Connector.Endpoint.PRINCIPAL_PASSWORD_HINT)
  public final void setPrincipalPassword(final GuardedString value) {
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
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString getPrincipalPassword() {
    return this.endpoint.principalPassword();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResourceOwner
  /**
   ** Sets the <code>name</code> of the <code>Resource Owner</code> attribute
   ** for the Service Provider endpoint.
   ** <p>
   ** A <code>Resource Owner</code> is an entity capable of authorizing access
   ** to a protected resource. When the resource owner is a person, it is called
   ** an user.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link ServiceEndpoint.Principal#username()}.
   **
   ** @param  value              the <code>username</code> of the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @ConfigurationProperty(order=13, displayMessageKey=Connector.Endpoint.RESOURCE_OWNERNAME_LABEL, helpMessageKey=Connector.Endpoint.RESOURCE_OWNERNAME_HINT)
  public final void setResourceOwner(final String value) {
    this.endpoint.resourceOwner(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceOwner
  /**
   ** Returns the <code>name</code> of the <code>Resource Owner</code>
   ** attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link ServiceEndpoint.Principal#username()}.
   **
   ** @return                    the <code>username</code> of the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getResourceOwner() {
    return this.endpoint.resourceOwner();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResourceCredential
  /**
   ** Sets the <code>credential</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link ServiceEndpoint.Principal#password()}.
   **
   ** @param  value              the <code>password</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   */
  @ConfigurationProperty(order=14, displayMessageKey=Connector.Endpoint.RESOURCE_CREDENTIAL_LABEL, helpMessageKey=Connector.Endpoint.RESOURCE_CREDENTIAL_HINT)
  public final void setResourceCredential(final GuardedString value) {
    this.endpoint.resourceCredential(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceCredential
  /**
   ** Returns the <code>password</code> attribute for the
   ** <code>Resource Owner</code> attribute for the Service Provider endpoint.
   ** <p>
   ** Convenience method to shortens the access to the configuration property
   ** {@link ServiceEndpoint.Principal#password()}.
   **
   ** @return                    the <code>password</code> attribute for the
   **                            <code>Resource Owner</code> attribute for the
   **                            Service Provider endpoint.
   **                            <br>
   **                            Possible object is {@link GuardedString}.
   */
  public final GuardedString getResourceCredential() {
    return this.endpoint.resourceCredential();
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
  @ConfigurationProperty(order=15, displayMessageKey=Connector.Endpoint.LANGUAGE_LABEL, helpMessageKey=Connector.Endpoint.LANGUAGE_HINT)
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
  @ConfigurationProperty(order=16, displayMessageKey=Connector.Endpoint.COUNTRY_LABEL, helpMessageKey=Connector.Endpoint.COUNTRY_HINT)
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
  @ConfigurationProperty(order=17, displayMessageKey=Connector.Endpoint.TIMEZONE_LABEL, helpMessageKey=Connector.Endpoint.TIMEZONE_HINT)
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
  @ConfigurationProperty(order=18, displayMessageKey=Connector.Connection.CONNECT_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.CONNECT_TIMEOUT_HINT)
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
  @ConfigurationProperty(order=19, displayMessageKey=Connector.Connection.RESPONSE_TIMEOUT_LABEL, helpMessageKey=Connector.Connection.RESPONSE_TIMEOUT_HINT)
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
  @ConfigurationProperty(order=20, displayMessageKey=Connector.Feature.FETCH_SCHEMA_LABEL, helpMessageKey=Connector.Feature.FETCH_SCHEMA_HINT)
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
  // Method:   setEnforceRFC9100
  /**
   ** Set the value of the extended configuration attribute that specifies
   ** either the validation of the HTTP methods is performed according to the
   ** rules of RFC-9110 or it is skipped.
   **
   ** @param  value              the value of the extended configuration
   **                            attribute that specifies either the validation
   **                            of the HTTP methods is performed according to
   **                            the rules of RFC-9110 or it is skipped.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=21, displayMessageKey=Connector.Feature.RFC_9110_LABEL, helpMessageKey=Connector.Feature.RFC_9110_HINT)
  public final void setEnforceRFC9100(final boolean value) {
    this.endpoint.rfc9110(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnforceRFC9100
  /**
   ** Returns the value of the extended configuration attribute that specifies
   ** either the validation of the HTTP methods is performed according to the
   ** rules of RFC-9110 or it is skipped.
   **
   ** @return                    the value of the extended configuration
   **                            attribute that specifies either the validation
   **                            of the HTTP methods is performed according to
   **                            the rules of RFC-9110 or it is skipped.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getEnforceRFC9100() {
    return this.endpoint.rfc9110();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setEnterpriseFeature
  /**
   ** Set the value of the extended configuration attribute that specifies
   ** whether the Service Provider is licensed to use these enhanced features.
   **
   ** @param  value              the value of the extended configuration
   **                            attribute that the Service Provider is licensed
   **                            to use these enhanced features.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  @ConfigurationProperty(order=22, displayMessageKey=Connector.Feature.ENTERPRICE_FEATURE_LABEL, helpMessageKey=Connector.Feature.ENTERPRICE_FEATURE_HINT)
  public final void enterpriseFeature (final boolean value) {
    this.endpoint.enterpriseFeature(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEnterpriseFeature
  /**
   ** Returns the value of the extended configuration attribute that specifies
   ** whether the Service Provider is licensed to use these enhanced features.
   **
   ** @return                    the value of the extended configuration
   **                            attribute that the Service Provider is licensed
   **                            to use these enhanced features.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean getEnterpriseFeature() {
    return this.endpoint.enterpriseFeature();
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