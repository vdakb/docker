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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Endpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Endpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.SystemConstant;

import oracle.iam.identity.icf.foundation.logging.Loggable;

import oracle.iam.identity.icf.rest.ServiceEndpoint;

///////////////////////////////////////////////////////////////////////////////
// class Endpoint
// ~~~~~ ~~~~~~~~
/**
 ** The <code>Endpoint</code> wraps the SCIM Service endpoint configuration.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class is decleared as public only to access it in JUNit tests.
 ** <br>
 ** This may be changed in the future.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the endpoints
 **                              extending this class (endpoints can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Endpoint<T extends Endpoint> extends ServiceEndpoint<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the context prefix for the User Account and Authentication (UAA) service
   */
  static final String CONTEXT_LOGIN = "login";

  /** the context prefix for the Cloud Controller (API) service */
  static final String CONTEXT_CLOUD = "api";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Endpoint</code> which is associated with the specified
   ** {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Endpoint</code> configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private Endpoint(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Endpoint</code> which is associated with the specified
   ** {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Endpoint</code> configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  service            the service properties on which the Service
   **                            Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  client             the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the security principal properties used to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  resource           the resource principal properties used to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  language           the language code of the Service Provider.
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the Service Provider.
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
  private Endpoint(final Loggable loggable, final Server service, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final Principal client, final Principal principal, final Principal resource, final String language, final String country, final String timeZone) {
    // ensure inheritance
    super(loggable, service, null /* rootContext */, secureSocket, typeContent, typeAccept, authentication, client, principal, resource, null /* authorizationURI */, language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizationURI (overridden)
  /**
   ** Returns the {@link URI} to the <code>Authorization Server</code> exposed
   ** by the Service Provider endpoint for authentication purpose.
   **
   ** @return                    the {@link URI} to the
   **                            <code>Authorization Server</code> exposed by
   **                            the Service Provider endpoint for
   **                            authentication purpose.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  @Override
  public final URI authorizationURI() {
    // lazy initialization of the configured service URL's
    return UriBuilder.fromPath(accountURL("oauth/token")).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection and the specified <code>contextPath</code>.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @param  contextPath        a component to a Web Resource path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String accountURL(final String contextPath) {
    // lazy initialization of the configured service URL's
    final StringBuilder context = new StringBuilder(accountURL());
    if (context.charAt(context.length() - 1) != SystemConstant.SLASH && contextPath.charAt(0) != SystemConstant.SLASH)
      context.append(SystemConstant.SLASH);
    return context.append(contextPath).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountURL
  /**
   ** Return the context URI to the
   ** <em>User Account and Authentication (UAA)</em> REST API.
   ** <p>
   ** The URL consists of the server part of the HTTP url,
   ** https://<em>uaa</em>.host:port and the absolute path to the resource. The
   ** entry is post fixed with the context domain of the connection.
   **
   ** @return                    the context URI the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String accountURL() {
    // lazy initialization of the configured service URL's
    return serviceURL(SCHEMA_DEFAULT_SECURE, CONTEXT_LOGIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controllerURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection and the specified <code>contextPath</code>.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @param  contextPath        a component to a Web Resource path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String controllerURL(final String contextPath) {
    // lazy initialization of the configured service URL's
    final StringBuilder context = new StringBuilder(controllerURL());
    if (context.charAt(context.length() - 1) != SystemConstant.SLASH && contextPath.charAt(0) != SystemConstant.SLASH)
      context.append(SystemConstant.SLASH);
    return context.append(contextPath).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   controllerURL
  /**
   ** Return the context URI to the <em>Administration</em> REST API.
   ** <p>
   ** The URL consists of the server part of the HTTP url,
   ** https://<em>api</em>.host:port and the absolute path to the resource. The
   ** entry is post fixed with the context domain of the connection.
   **
   ** @return                    the context URI the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String controllerURL() {
    // lazy initialization of the configured service URL's
    return serviceURL(this.secureSocket ? SCHEMA_DEFAULT_SECURE : SCHEMA_DEFAULT, CONTEXT_CLOUD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   proxyURL
  /**
   ** Return the server parts of the HTTP url, http(s)://host:port blonging to
   ** the <code>Proxy Server</code> deployed in front of a Service Provider.
   **
   ** @return                    the server part of the HTTP url,
   **                            http(s)://host:port.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String proxyURL() {
    // lazy initialization of the configured service URL's
    return this.proxy.serviceURL(this.secureSocket ? SCHEMA_DEFAULT_SECURE : SCHEMA_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serviceURL
  /**
   ** Return the server part of an url, <em>protocol</em>://<em>prefix</em>.domain:port.
   **
   ** @param  protocol         the protocol of the url to build.
   **                          <br>
   **                          Allowed object is {@link String}.
   ** @param  service          the service the service domain is prefixed with.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the server part of the an url,
   **                          <em>protocol</em>://host:port
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public String serviceURL(final String protocol, final String service) {
    final StringBuilder context = new StringBuilder(protocol);
    context.append("://").append(service);
    if (primaryHost() != null)
      context.append('.').append(primaryHost());
    if (primaryPort() != -1)
      context.append(SystemConstant.COLON).append(primaryPort());

    return context.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Endpoint</code> which is associated with
   ** the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Endpoint</code> configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an newly created instance of
   **                            <code>Endpoint</code> providing default values
   **                            only.
   **                            <br>
   **                            Possible object is <code>Endpoint</code>.
   */
  public static Endpoint build(final Loggable loggable) {
    return new Endpoint(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Endpoint</code> which is associated with
   ** the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Endpoint</code> configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  serviceDomain      the host name or IP address of the target
   **                            domain on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  servicePort        the port the Service Provider is listening on
   **                            <br>
   **                            Default value for non-SSL: 80
   **                            Default value for SSL: 443
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            target system.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientIdentifier   the identifier of the client that is used for
   **                            authorization purpose at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  clientSecret       the password of the client that is used for
   **                            authorization purpose at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  principalName      the fully qualified domain name corresponding
   **                            to the acccount of the Service Provider with
   **                            administrator privikeges.
   **                            <br>
   **                            Format: <code>cn=<i>ADMIN_LOGIN</i>,cn=Users,dc=<i>DOMAIN</i></code>
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  principalPassword  the password of the administrator account that
   **                            is used
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  resourceUsername   the resource owner account to authenticate at
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourcePassword   the password of the resource owner account
   **                            specified by the <code>resourceUsername</code>
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  language           the language code of the Service Provider.
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the Service Provider.
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
   **                            <code>Endpoint</code> populated with the values
   **                            provided.
   **                            <br>
   **                            Possible object is <code>Endpoint</code>.
   */
  public static Endpoint build(final Loggable loggable, final String serviceDomain, final int servicePort, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final String clientIdentifier, final GuardedString clientSecret, final String principalName, final GuardedString principalPassword, final String resourceUsername, final GuardedString resourcePassword, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return build(loggable, new Server(serviceDomain, servicePort), secureSocket, typeContent, typeAccept, authentication, new Principal(clientIdentifier, clientSecret), new Principal(principalName, principalPassword), new Principal(resourceUsername, resourcePassword), language, country, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Endpoint</code> which is
   ** associated with the specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will be populated from the specified
   ** parameters.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Endpoint</code> configuration wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  service            the service properties on which the Service
   **                            Provider is deployed and listening.
   **                            <br>
   **                            Allowed object is {@link Server}.
   ** @param  secureSocket       specifies whether or not to use SSL to secure
   **                            communication between Identity Manager and the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  typeContent        the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            Allowed object is {@link String}.
   ** @param  typeAccept         the accepted media type send to the server at
   **                            any request except authentication requests to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  authentication     the authentication scheme to authenticate the
   **                            security principal at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  client             the identifier of the client to authenticate
   **                            the connector at the Service Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the security principal properties user to
   **                            authenticate a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  resource           the security principal properties user to
   **                            authorize a connection with the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  language           the language code of the Service Provider.
   **                            <br>
   **                            Default value: en
   **                            <br>
   **                            Note: You must specify the value in lowercase.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  country            the country code of the Service Provider.
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
   **                            <code>Endpoint</code> populated with the values
   **                            provided.
   **                            <br>
   **                            Possible object is <code>Endpoint</code>.
   */
  public static final Endpoint build(final Loggable loggable, final Server service, final boolean secureSocket, final String typeContent, final String typeAccept, final String authentication, final Principal client, final Principal principal, final Principal resource, final String language, final String country, final String timeZone) {
    // ensure inheritance
    return new Endpoint(loggable, service, secureSocket, typeContent, typeAccept, authentication, client, principal, resource, language, country, timeZone);
  }
}