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

    File        :   Authorization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Authorization.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth.v2;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

import java.net.URI;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ClientBuilder;

import oracle.iam.identity.icf.foundation.AbstractEndpoint.Principal;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.rest.ServiceError;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.authn.oauth.Token;
import oracle.iam.identity.icf.authn.oauth.AuthorizationFeature;

///////////////////////////////////////////////////////////////////////////////
// class Authorization
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Default implementation of {@link AuthorizationFlow}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class Authorization implements AuthorizationFlow {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final URI                 accessTokenURI;
  private final URI                 authorizationURI;
  private final URI                 refreshTokenURI;
  private final Principal           identifier;

  private final Client              client;

  private volatile Token            token;

  private final Map<String, String> accessToken;
  private final Map<String, String> refreshToken;
  private final Map<String, String> authorization;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~
  /**
   ** Builder implementation.
   */
  static class Builder<T extends Builder> implements AuthorizationFlow.Builder<Builder> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private URI                 accessTokenURI;
    private URI                 refreshTokenURI;
    private URI                 authorizationURI;
    private String              callbackURI;
    private String              scope;
    private Client              client;
    private Principal           identifier;
    private Map<String, String> authorization     = new HashMap<>();
    private Map<String, String> accessToken       = new HashMap<>();
    private Map<String, String> refreshToken      = new HashMap<>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for OAuth2 that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for OAuth2 with defined URI's
     ** and client id.
     **
     ** @param  identifier       the client identifier (id of application that
     **                          wants to be approved). Issued by the Service
     **                          Provider.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  accessTokenURI   the access token {@link URI} on which the
     **                          access token can be requested.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     */
    public Builder(final Principal identifier, final URI accessTokenURI) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.identifier     = identifier;
      this.accessTokenURI = accessTokenURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for OAuth2 with defined URI's
     ** and client id.
     **
     ** @param  identifier       the client identifier (id of application that
     **                          wants to be approved). Issued by the Service
     **                          Provider.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  authorizationURI the {@link URI} to which the user should be
     **                          redirected to authorize our application.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  accessTokenURI   the access token {@link URI} on which the
     **                          access token can be requested.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     */
    public Builder(final Principal identifier, final URI authorizationURI, final URI accessTokenURI) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.identifier       = identifier;
      this.accessTokenURI   = accessTokenURI;
      this.authorizationURI = authorizationURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for OAuth2 with defined URI's
     ** and client id and callback uri.
     **
     ** @param  identifier       the client identifier (id of application that
     **                          wants to be approved). Issued by the Service
     **                          Provider.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  authorizationURI the {@link URI} to which the user should be
     **                          redirected to authorize our application.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  accessTokenURI   the access token {@link URI} on which the
     **                          access token can be requested.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  callbackURI      the redirect URI that should receive
     **                          authorization response from the Service
     **                          Provider.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public Builder(final Principal identifier, final URI authorizationURI, final URI accessTokenURI, final String callbackURI) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.identifier       = identifier;
      this.callbackURI      = callbackURI;
      this.accessTokenURI   = accessTokenURI;
      this.authorizationURI = authorizationURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: accessTokenURI
    /**
     ** Returns the access token URI on which the access token can be requested.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @return                  the access token {@link URI} on which the
     **                          access token can be requested.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     */
    URI accessTokenURI() {
      return this.accessTokenURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshTokenURI
    /**
     ** Returns the refresh token URI on which the access token can be refreshed
     ** using a refresh token.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @return                  the refresh token URI.
     **                          <br>
     **                          Possible object is {@link URI}.
     */
    URI refreshTokenURI() {
      return this.refreshTokenURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorizationURI
    /**
     ** Returns the URI to which the user should be redirected to authorize the
     ** application.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @return                  the {@link URI} to which the user should be
     **                          redirected to authorize our application.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     */
    URI authorizationURI() {
      return this.authorizationURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: redirectURI
    /**
     ** Returns the redirect URI to which the user (resource owner) should be
     ** redirected after he/she grants access to the application.
     **
     ** @return                  the redirect URI that should receive
     **                          authorization response from the Service
     **                          Provider.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    String redirectURI() {
      return this.callbackURI;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Returns client identifier {@link Principal} of the application that
     ** should be authorized.
     **
     ** @return                  the client identifier {@link Principal}
     **                          instance.
     **                          <br>
     **                          Possible object is {@link Principal}.
     */
    Principal identifier() {
      return this.identifier;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
    /**
     ** Returns the scope to which the application will get authorization grant.
     ** <br>
     ** Values of this parameter are defined by the Service Provider and defines
     ** usually subset of resource and operations available in the Service
     ** Provider.
     **
     ** @return                  the scope.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    String scope() {
      return this.scope;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: client
    /**
     ** Returns the client that should be used internally by the
     ** {@link AuthorizationFlow} to make requests to
     ** <code>Authorization Server</code>.
     **
     ** @return                  the client identifier instance.
     **                          <br>
     **                          Possible object is {@link Client}.
     */
    Client client() {
      return this.client;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorization
    /**
     ** Returns the authorization properties (parameter) that will be added to
     ** requests or URIs as a query parameters during the
     ** {@link AuthorizationFlow}.
     **
     ** @return                  the authorization properties (parameter) that
     **                          will be added to requests or URIs as a query
     **                          parameters during the
     **                          {@link AuthorizationFlow}.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     */
    Map<String, String> authorization() {
      return this.authorization;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: accessToken
    /**
     ** Returns the access token properties (parameter) that will be added to
     ** requests or URIs as a query parameters during the
     ** {@link AuthorizationFlow}.
     **
     ** @return                  the access token properties (parameter) that
     **                          will be added to requests or URIs as a query
     **                          parameters during the
     **                          {@link AuthorizationFlow}.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     */
    Map<String, String> accessToken() {
      return this.accessToken;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshToken
    /**
     ** Returns the refresh token properties (parameter) that will be added to
     ** requests or URIs as a query parameters during the
     ** {@link AuthorizationFlow}.
     **
     ** @return                  the refresh token properties (parameter) that
     **                          will be added to requests or URIs as a query
     **                          parameters during the
     **                          {@link AuthorizationFlow}.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     */
    Map<String, String> refreshToken() {
      return this.refreshToken;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: accessTokenURI (AuthorizationFlow.Builder)
    /**
     ** Set the access token URI on which the access token can be requested.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @param  uri              the access token {@link URI} on which the
     **                          access token can be requested.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder accessTokenURI(final URI uri) {
      this.accessTokenURI = uri;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshTokenURI (AuthorizationFlow.Builder)
    /**
     ** Set the refresh token URI on which the access token can be refreshed
     ** using a refresh token.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     ** <br>
     ** If the URI is not defined by this method it will be the same as URI
     ** defined in {@link #accessTokenURI(String)} (which is the default value
     ** defined by the OAuth2 spec).
     ** <p>
     ** Some providers do not support refreshing access tokens at all.
     **
     ** @param  uri              the refresh token URI.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder refreshTokenURI(final URI uri) {
      this.refreshTokenURI = uri;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorizationURI (AuthorizationFlow.Builder)
    /**
     ** Set the URI to which the user should be redirected to authorize the
     ** application.
     ** <br>
     ** The URI points to the authorization server and is defined by the
     ** Service Provider.
     **
     ** @param  uri              the {@link URI} to which the user should be
     **                          redirected to authorize our application.
     **                          <br>
     **                          The URI points to the authorization server and
     **                          is defined by the Service Provider.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder authorizationURI(final URI uri) {
      this.authorizationURI = uri;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: redirectURI (AuthorizationFlow.Builder)
    /**
     ** Set the redirect URI to which the user (resource owner) should be
     ** redirected after he/she grants access to the application.
     ** <br>
     ** In most cases, the URI is under control of the application and request
     ** done on this URI will be used to extract query parameter
     ** <code>code</code> and <code>state</code> that will be used in
     ** {@link AuthorizationFlow#finish(String, String)} method.
     ** <p>
     ** If URI is not defined by this method, the default value
     ** <code>urn:ietf:wg:oauth:2.0:oob</code> will be used in the Authorization
     ** Flow which should cause that <code>code</code> will be passed to
     ** application in other way than request redirection (for example shown to
     ** the user using html page).
     **
     ** @param  uri              the redirect URI that should receive
     **                          authorization response from the Service
     **                          Provider.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder redirectURI(final String uri) {
      this.callbackURI = uri;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier (AuthorizationFlow.Builder)
    /**
     ** Set client identifier of the application that should be authorized.
     **
     ** @param  identifier       the client identifier instance.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder identifier(final Principal identifier) {
      this.identifier = identifier;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope (AuthorizationFlow.Builder)
    /**
     ** Set the scope to which the application will get authorization grant.
     ** <br>
     ** Values of this parameter are defined by the Service Provider and defines
     ** usually subset of resource and operations available in the Service
     ** Provider.
     ** <p>
     ** The parameter is optional but Service Provider might require it.
     **
     ** @param  scope            the scope.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder scope(final String scope) {
      this.scope = scope;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: client (AuthorizationFlow.Builder)
    /**
     ** Set the client that should be used internally by the
     ** {@link AuthorizationFlow} to make requests to
     ** <code>Authorization Server</code>.
     ** <br>
     ** If this method is not called, it is up to the implementation to create
     ** or get any private client instance to perform these requests.
     ** <br>
     ** This method could be used mainly for performance reasons to avoid
     ** creation of new client instances and have control about created client
     ** instances used in the application.
     **
     ** @param  client           the client identifier instance.
     **                          <br>
     **                          Allowed object is {@link Client}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder client(final Client client) {
      this.client = client;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: property (AuthorizationFlow.Builder)
    /**
     ** Set property (parameter) that will be added to requests or URIs as a
     ** query parameters during the {@link AuthorizationFlow}.
     ** <br>
     ** Default parameters used during the {@link AuthorizationFlow} can be also
     ** overridden by this method.
     **
     ** @param  phase            the phase of the flow in which the properties
     **                          (parameters) should be used.
     **                          <br>
     **                          For example by using a {@link Phase#ACCESS},
     **                          the parameter will be added only to the http
     **                          request for access token.
     **                          <br>
     **                          Allowed object is {@link Phase}.
     ** @param  key              the property key.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the property value.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder property(final Phase phase, final String key, final String value) {
      phase.property(key, value, this.authorization, this.accessToken, this.refreshToken);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to build a {@code AuthorizationFlow} instance.
     **
     ** @return                  the new instance of {@code AuthorizationFlow}.
     */
    @Override
    public AuthorizationFlow build() {
      return new Authorization(this.authorizationURI, this.accessTokenURI, this.callbackURI, this.refreshTokenURI, this.identifier, this.scope, this.client, this.authorization, this.accessToken, this.refreshToken);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Authorization</code> which is associated with the
   ** specified {@link Loggable}.
   ** <br>
   ** The <code>IT Resource</code> will provide only default parameters therefor
   ** custom or additonal parameter need to be populated manually.
   **
   ** @param  authorizationURI   the {@link URI} to which the user should be
   **                            redirected to authorize our application.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  accessTokenURI     the access token {@link URI} on which the
   **                            access token can be requested.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  callbackURI        the redirect URI that should receive
   **                            authorization response from the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private Authorization(final URI authorizationURI, final URI accessTokenURI, final String callbackURI, final URI refreshTokenURI, final Principal identifier, final String scope, final Client client, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.accessTokenURI   = accessTokenURI;
    this.authorizationURI = authorizationURI;
    this.refreshTokenURI  = (refreshTokenURI != null) ? refreshTokenURI : accessTokenURI;

    this.authorization    = authorization;
    this.accessToken      = accessToken;
    this.refreshToken     = refreshToken;

    this.identifier       = identifier;
    this.client           = configureClient(client);

    // initialize instance attributes
    initProperty(callbackURI, scope);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start (AuthorizationFlow)
  /**
   ** Start the authorization flow and return redirection URI on which the user
   ** should give a consent for our application to access resources.
   **
   ** @return                    the URI to which user should be redirected.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  @Override
  public final URI start() {
    final UriBuilder uriBuilder = UriBuilder.fromUri(this.authorizationURI);
    for (final Map.Entry<String, String> entry : this.authorization.entrySet()) {
      uriBuilder.queryParam(entry.getKey(), entry.getValue());
    }
    return uriBuilder.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   finish (AuthorizationFlow)
  /**
   ** Finish the authorization process and return the {@link Token}.
   ** <br>
   ** The method must be called on the same instance after the {@link #start()}
   ** method was called and user granted access to this application.
   ** <p>
   ** The method makes a request to the <code>Authorization Server</code> in
   ** order to exchange <code>code</code> for access token.
   **
   ** @param  code               the code received from the user authorization
   **                            process.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  state              the state received from the user authorization
   **                            response.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Token} result.
   **                            <br>
   **                            Possible object is {@link Token}.
   */
  @Override
  public final Token finish(final String code, final String state) {
    if (!this.authorization.get(STATE).equals(state))
      throw new IllegalArgumentException(ServiceBundle.string(ServiceError.OAUTH_FLOW_WRONG_STATE));

    this.accessToken.put(CODE, code);
    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.accessToken.entrySet()) {
      form.param(entry.getKey(), entry.getValue());
    }

    final Response response = this.client.target(this.accessTokenURI).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    if (response.getStatus() != 200)
      throw new ProcessingException(ServiceBundle.string(ServiceError.OAUTH_FLOW_ACCESS_TOKEN, response.getStatus()));

    this.token = response.readEntity(Token.class);
    return token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshToken (AuthorizationFlow)
  /**
   ** Refresh the access token using a refresh token.
   ** <br>
   ** This method can be called on newly created instance or on instance on
   ** which the authorization flow was already performed.
   **
   ** @param  refreshToken       the refresh token.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Token} result.
   **                            <br>
   **                            Possible object is {@link Token}.
   */
  @Override
  public final Token refreshToken(final String refreshToken) {
    this.refreshToken.put(REFRESH_TOKEN, refreshToken);
    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.refreshToken.entrySet()) {
      form.param(entry.getKey(), entry.getValue());
    }

    final Response response = this.client.target(this.refreshTokenURI).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    if (response.getStatus() != 200)
      throw new ProcessingException(ServiceBundle.string(ServiceError.OAUTH_FLOW_REFRESH_TOKEN, response.getStatus()));

    this.token = response.readEntity(Token.class);
    return token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizedClient (AuthorizationFlow)
  /**
   ** Return the client configured for performing authorized requests to the
   ** Service Provider.
   ** <br>
   ** The authorization process must be successfully finished by instance by
   ** calling methods {@link #start()} and {@link #finish(String, String)}.
   **
   ** @return                    the {@link Client} configured to add correct
   **                            <code>Authorization</code> header to requests.
   **                            <br>
   **                            Possible object is {@link Client}.
   */
  @Override
  public final Client authorizedClient() {
    return ClientBuilder.newClient().register(feature());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature (AuthorizationFlow)
  /**
   ** Returns the {@link Feature oauth filter feature} that can be used to
   ** configure {@link Client client} instances to perform authenticated
   ** requests to the Service Provider.
   ** <p>
   ** The authorization process must be successfully finished by instance by
   ** calling methods {@link #start()} and {@link #finish(String, String)}.
   **
   ** @return                    the OAuth2 filter feature configured with
   **                            received <code>AccessToken</code>.
   */
  @Override
  public final Feature feature() {
    // prevent bogus state
    if (this.token == null)
      throw new ProcessingException(ServiceBundle.string(ServiceError.OAUTH_FLOW_NOT_FINISH));

    return AuthorizationFeature.build(this.token);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClient
  private Client configureClient(Client client) {
    if (client == null) {
      client = ClientBuilder.newClient();
    }

    return client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initProperty
  /**
   **
   ** @param  redirectURI        the redirect URI that should receive
   **                            authorization response from the Service
   **                            Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   * @param scope
   */
  private void initProperty(final String redirectURI, final String scope) {
    defaultProperty(RESPONSE_TYPE, CODE,                                                  this.authorization);
    defaultProperty(CLIENT_PUBLIC, this.identifier.username(),                            this.authorization, this.accessToken, this.refreshToken);
    defaultProperty(REDIRECT_URI,  redirectURI == null ? REDIRECT_OOB : redirectURI,      this.authorization, this.accessToken);
    defaultProperty(STATE,         UUID.randomUUID().toString(),                          this.authorization);
    defaultProperty(SCOPE,         scope,                                                 this.authorization);

    defaultProperty(CLIENT_SECRET, CredentialAccessor.string(this.identifier.password()), this.accessToken, this.refreshToken);
    defaultProperty(GRANT_TYPE,    Grant.AUTHORIZATION_CODE.name().toLowerCase(),         this.accessToken);
    defaultProperty(GRANT_TYPE,    Grant.REFRESH_TOKEN.name().toLowerCase(),              this.refreshToken);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProperty
  private void defaultProperty(final String key, final String value, final Map<String, String>... properties) {
    if (value == null) {
      return;
    }
    for (final Map<String, String> props : properties) {
      if (props.get(key) == null) {
        props.put(key, value);
      }
    }
  }
}