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

    File        :   Service.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceClient.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import java.net.URI;

import javax.ws.rs.core.Feature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientProperties;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import oracle.iam.identity.icf.foundation.SecurityContext;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.logging.AbstractLoggable;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.rest.feature.LoggerFeature;
import oracle.iam.identity.icf.rest.feature.MarshallFeature;

import oracle.iam.identity.icf.authn.oauth.v1.AuthorizationFlow;
import oracle.iam.identity.icf.authn.oauth.v1.AuthorizationClient;

///////////////////////////////////////////////////////////////////////////////
// class Service
// ~~~~~ ~~~~~~~
/**
 ** This is connector server specific client class that extends Jersey's
 ** client interface.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class is decleared as public only to access it in JUNit tests.
 ** <br>
 ** This may be changed in the future.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Service extends AbstractLoggable<Service> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Client   client;
  protected final Endpoint endpoint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Service</code> which is associated with the specified
   ** {@link Endpoint} for configuration purpose.
   **
   ** @param  endpoint           the {@link Endpoint} <code>IT Resource</code>
   **                            instance where this connector client is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link Endpoint}.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  private Service(final Endpoint endpoint)
    throws SystemException {

    // ensure inheritance
    super(endpoint);

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
   ** Returns the {@link Endpoint} this context is using to connect and perform
   ** operations on the Service Provider.
   **
   ** @return                    the {@link Endpoint} this context is using to
   **                            connect and perform operations on the Service
   **                            Provider.
   **                            <br>
   **                            Possible object {@link Endpoint}.
   */
  public final Endpoint endpoint() {
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
   ** @param  endpoint           the {@link Endpoint} <code>IT Resource</code>
   **                            instance where this connector client is
   **                            associated with.
   **                            <br>
   **                            Allowed object is {@link Endpoint}.
   **
   ** @return                    a default Jersey client.
   **                            <br>
   **                            Possible object is <code>Service</code>.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  public static Service build(final Endpoint endpoint)
    throws SystemException {

    return new Service(endpoint);
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
  // Method:   create
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
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  private Client createClient()
    throws SystemException {

    final ClientBuilder builder  = ClientBuilder.newBuilder()
      .property(ClientProperties.CONNECT_TIMEOUT, this.endpoint.timeOutConnect())
      .property(ClientProperties.READ_TIMEOUT,    this.endpoint.timeOutResponse())
      // register the binding layer first
      .register(MarshallFeature.class)
      ;
    if (this.endpoint.secureSocket()) {
      builder.sslContext(SecurityContext.defaultContext(true)).hostnameVerifier(SecurityContext.INSECURE_HOSTNAME_VERIFIER);
    }
    if (this.endpoint.proxy() != null) {
      builder.property(ClientProperties.PROXY_URI, this.endpoint.proxyURL());
      if (this.endpoint.proxyPrincipal() != null) {
        builder.property(ClientProperties.PROXY_USERNAME, this.endpoint.proxyUsername()).property(ClientProperties.PROXY_PASSWORD, CredentialAccessor.string(this.endpoint.proxyPassword()));
      }
    }
    // register the authentication feature
    switch (Endpoint.Authentication.from(this.endpoint.authenticationScheme())) {
      case PREEMTIVE    : builder.register(preemptiveBasic());
                          break;
      case NONPREEMTIVE : builder.register(nonPreemptiveBasic());
                          break;
      case PASSWORD     :
      case CREDENTIAL   : builder.register(authenticateOAuth());
                          break;
    }
    // register the logging feature as the last one
    builder.register(new LoggerFeature(this.endpoint, LoggerFeature.Verbosity.PAYLOAD_ANY));
    return builder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateOAuth
  /**
   ** Authorize the specified {@link WebTarget} at the Service Provider.
   ** <p>
   ** The authorization evalutes the authentication scheme to use and authorize
   ** the {@link WebTarget} by injecting the appropriate header.
   ** <br>
   ** This will be done only once if the authentication itself succeeds. Any
   ** subsequent call to this method will inject only the authorization header.
   **
   ** @throws ServiceException   if the status of the HTTP response obtaining
   **                            the access token is greater than or equal to
   **                            300 and <code>type</code> does not represent
   **                            the type {@link Response}.
   **                            Also thrown if the client handler fails to
   **                            process the request or response.
   ** @throws SystemException    if the authentication flow is secured by SSL
   **                            and the required {@link SSLContext} could not
   **                            be configured.
   */
  private Feature authenticateOAuth()
    throws SystemException {

    // we do not have Access Token yet. Let's perfom the Authorization Flow
    // first, let the user approve our app and get Access Token.
    AuthorizationFlow flow   = null;
    switch(Endpoint.Authentication.from(this.endpoint.authenticationScheme())) {
      case CREDENTIAL : flow = AuthorizationClient.credentialFlow(this.endpoint.client(), this.endpoint.authorizationURI()).build(this.endpoint);
                        break;
      case PASSWORD   : flow = AuthorizationClient.passwordFlow(this.endpoint.client(), this.endpoint.principal(), this.endpoint.authorizationURI()).build(this.endpoint);
                        break;
      default         : throw new IllegalArgumentException(this.endpoint.authenticationScheme());
    }
    flow.accessToken();
    return flow.feature();
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
  private Feature preemptiveBasic() {
    return HttpAuthenticationFeature.basic(this.endpoint.principalUsername(), CredentialAccessor.string(this.endpoint.principalPassword()));
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
  private Feature nonPreemptiveBasic() {
    return HttpAuthenticationFeature.basicBuilder().nonPreemptive().credentials(this.endpoint.principalUsername(), CredentialAccessor.string(this.endpoint.principalPassword())).build();
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
  private Feature universalBasic() {
    return HttpAuthenticationFeature.universalBuilder()
      .credentialsForBasic(this.endpoint.principalUsername(), CredentialAccessor.string(this.endpoint.principalPassword()))
      .credentials(this.endpoint.principalUsername(), CredentialAccessor.string(this.endpoint.principalPassword()))
      .build();
  }
}
