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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Identity Services Integration

    File        :   ServiceClient.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceClient.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.service.rest;

import bka.iam.identity.service.ServiceException;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.UriBuilder;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.platform.core.SystemException;
import oracle.hst.platform.core.logging.SystemConsole;
import oracle.hst.platform.core.network.Principal;
import oracle.hst.platform.core.network.SecurityContext;
import oracle.hst.platform.rest.authn.oauth.v2.AuthorizationClient;
import oracle.hst.platform.rest.authn.oauth.v2.AuthorizationFlow;
import oracle.hst.platform.rest.plugin.DomainFeature;

import oracle.iam.identity.foundation.AbstractLoggable;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

///////////////////////////////////////////////////////////////////////////////
// class ServiceClient
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This is connector server specific client class that extends Jersey's
 ** client interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceClient extends AbstractLoggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Client          client;
  protected final ServiceResource endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceClient</code> which is associated with the
   ** specified {@link ServiceResource} for configuration purpose.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  endpoint           the {@link ServiceResource}
   **                            <code>IT Resource</code> instance where this
   **                            connector client is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceResource}.
   **
   ** @throws ServiceException   if the authentication/authorization process
   **                            fails.
   */
  protected ServiceClient(final Loggable loggable, final ServiceResource endpoint)
    throws ServiceException {

    // ensure inheritance
    super(loggable);

    // initialize instance attributes
    this.endpoint = endpoint;
    this.client   = createClient();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the {@link Client} this context is using to connect and perform
   ** operations on the Service Provider.
   **
   ** @return                    the {@link Client} this context is using to
   **                            connect and perform operations on the Service
   **                            Provider.
   **                            <br>
   **                            Possible object {@link Client}.
   */
  public final Client client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the {@link ServiceResource} this context is using to connect and
   ** perform operations on the Service Provider.
   **
   ** @return                    the {@link ServiceResource} this context is
   **                            using to connect and perform operations on the
   **                            Service Provider.
   **                            <br>
   **                            Possible object {@link ServiceResource}.
   */
  public final ServiceResource endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextURL
  /**
   ** Return the fullqualified URL to the Web Service.
   ** <p>
   ** The URL consists of the server part of the HTTP url, http(s)://host:port
   ** and the absolute path to the resource. The entry is post fixed with the
   ** context root of the connection.
   ** <p>
   ** This version of retrieving the context URl returns always the context root
   ** configured in the <code>IT Resource</code> definition associated with this
   ** instance.
   **
   ** @return                    the context URl the context root configured in
   **                            the <code>IT Resource</code> definition
   **                            associated with this instance.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String contextURL() {
    return this.endpoint.contextURL();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeContent
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
  public final String typeContent() {
    return this.endpoint.typeContent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   typeAccept
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
  public final String typeAccept() {
    return this.endpoint.typeAccept();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a default client.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this {@link AbstractLoggable}.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  endpoint           the {@link ServiceResource}
   **                            <code>IT Resource</code> definition where this
   **                            connector context is associated with.
   **                            <br>
   **                            Allowed object is {@link ServiceResource}.
   **
   ** @return                    a default Jersey client.
   **                            <br>
   **                            Possible object is
   **                            <code>ServiceClient</code>.
   **
   ** @throws ServiceException   if the authentication/authorization process
   **                            fails.
   */
  public static ServiceClient build(final Loggable loggable, final ServiceResource endpoint)
    throws ServiceException {

    return new ServiceClient(loggable, endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildTarget
  /**
   ** Factory method to create a {@link WebTarget} for the request.
   ** <p>
   ** Create a JAX-RS web target whose URI refers to the
   ** <code>ServiceEndpoint.contextURL()</code> the JAX-RS / Jersey application
   ** is deployed at.
   ** <p>
   ** This method is an equivalent of calling
   ** <code>client().target(endpoint.contextURL())</code>.
   **
   ** @return                    the created JAX-RS web target.
   **                            <br>
   **                            Possible object {@link WebTarget}.
   */
  protected WebTarget buildTarget() {
    return this.client.target(contextURL());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Factory method to build a new web resource target.
   **
   ** @param  uri                the web resource {@link URI}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the web resource target bound to the provided
   **                            {@link URI}.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public WebTarget target(final String uri) {
    return this.client.target(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   target
  /**
   ** Factory method to build a new web resource target.
   **
   ** @param  uri                the web resource {@link URI}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the web resource target bound to the provided
   **                            {@link URI}.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public WebTarget target(final URI uri) {
    return this.client.target(uri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createClient
  /**
   ** Factory method to create the Jersey {@link Client} instance.
   **
   ** @param  name               the name of the thread for debugging purpose.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a fully-configured Jersey {@link Client}.
   **                            <br>
   **                            Possible object is {@link Client}.
   **
   ** @throws ServiceException   if the authentication/authorization process
   **                            fails.
   */
  private Client createClient()
    throws ServiceException {

    final ClientBuilder builder  = ClientBuilder.newBuilder()
      .property(ClientProperties.CONNECT_TIMEOUT, this.endpoint.connectionTimeout())
      .property(ClientProperties.READ_TIMEOUT,    this.endpoint.responseTimeout())
      ;

    try {
      if (this.endpoint.secureSocket()) {
        builder.sslContext(SecurityContext.defaultContext(true)).hostnameVerifier(SecurityContext.INSECURE_HOSTNAME_VERIFIER);
      }
      // register the authentication feature
      switch (ServiceResource.Authentication.from(this.endpoint.authenticationScheme())) {
        case PREEMTIVE    : builder.register(preemptiveBasic());
                            break;
        case NONPREEMTIVE : builder.register(nonPreemptiveBasic());
                            break;
        case CREDENTIAL   : builder.register(credentialFlow());
                            break;
        case PASSWORD     : builder.register(passwordFlow(DomainFeature.build("SecureDomain2")));
                            break;
      }
    }
    catch (SystemException e) {
      throw ServiceException.abort(e);
    }
    // register the logging feature as the last one
    builder.register(new LoggerFeature(this, LoggerFeature.Verbosity.PAYLOAD_ANY));
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preemptiveBasic
  /**
   ** Configures the JAX-RS Client to perform basic preemptive authentication
   ** initialized with credentials.
   **
   ** @return                    the http authentication feature configured in
   **                            preemptive basic mode.
   **                            <br>
   **                            Possible object is {@link Feature}.
   */
  protected Feature preemptiveBasic() {
    return HttpAuthenticationFeature.basic(this.endpoint.principalName(), this.endpoint.principalPassword());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nonPreemptiveBasic
  /**
   ** Configures the JAX-RS Client to perform non-prempitive Basic
   ** Authentication Challenge.
   **
   ** @return                    the http authentication feature configured in
   **                            non-preemptive basic mode.
   **                            <br>
   **                            Possible object is {@link Feature}.
   */
  protected Feature nonPreemptiveBasic() {
    return HttpAuthenticationFeature.basicBuilder()
      .nonPreemptive()
      .credentials(this.endpoint.principalName(), this.endpoint.principalPassword())
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   universalBasic
  /**
   ** Configures the JAX-RS Client to perform Basic Authentication Challenge
   **
   ** @return                    the http authentication feature configured in
   **                            universal basic mode.
   **                            <br>
   **                            Possible object is {@link Feature}.
   */
  protected Feature universalBasic() {
    return HttpAuthenticationFeature.universalBuilder()
      .credentialsForBasic(this.endpoint.principalName(), this.endpoint.principalPassword())
      .credentials(this.endpoint.principalName(), this.endpoint.principalPassword())
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordFlow
  /**
   ** Authorize the specified {@link WebTarget} at the Service Provider.
   ** <p>
   ** The authorization authorize the {@link WebTarget} by injecting the
   ** appropriate <code>Bearer</code> authorization header.
   ** <br>
   ** This will be done only once if the authentication itself succeeds. Any
   ** subsequent call to this method will inject only the authorization header.
   **
   ** @param  feature            the client feature.
   **                            <br>
   **                            Possible object is {@link Feature}.
   ** 
   ** @return                    the OAuth2 filter feature configured with
   **                            received <code>AccessToken</code>.
   **
   ** @throws SystemException    if the authentication flow is secured by SSL
   **                            and the required 
   **                            {@link javax.net.ssl.SSLContext} could not
   **                            be configured.
   */
  protected Feature passwordFlow(final Feature feature)
    throws SystemException {

    final AuthorizationFlow flow = AuthorizationClient.passwordFlow(
      // client credentials
      Principal.of(this.endpoint.clientIdentifier(),  this.endpoint.clientSecret())
      // resource owner credentials
    , Principal.of(this.endpoint.resourceOwner(),     this.endpoint.resourceCredential())
    , UriBuilder.fromPath(this.endpoint.authorizationServer()).build()
    ).register(feature, HttpAuthenticationFeature.basic(this.endpoint.clientIdentifier(), this.endpoint.clientSecret())).build(new SystemConsole("gws"));
    flow.accessToken();
    return flow.feature();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordFlow
  /**
   ** Authorize the specified {@link WebTarget} at the Service Provider.
   ** <p>
   ** The authorization authorize the {@link WebTarget} by injecting the
   ** appropriate <code>Bearer</code> authorization header.
   ** <br>
   ** This will be done only once if the authentication itself succeeds. Any
   ** subsequent call to this method will inject only the authorization header.
   ** 
   ** @return                    the client feature.
   **                            <br>
   **                            Possible object is {@link Feature}.
   **
   ** @throws SystemException    if the authentication flow is secured by SSL
   **                            and the required 
   **                            {@link javax.net.ssl.SSLContext} could not
   **                            be configured.
   */
  protected Feature credentialFlow()
    throws SystemException {

    final AuthorizationFlow flow   = AuthorizationClient.credentialFlow(
      // client credentials
      Principal.of(this.endpoint.clientIdentifier(),  this.endpoint.clientSecret())
    , UriBuilder.fromPath(this.endpoint.authorizationServer()).build()
    ).build(new SystemConsole("gws"));
    flow.accessToken();
    return flow.feature();
  }
}