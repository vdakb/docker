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

package oracle.iam.identity.icf.authn.oauth.v1;

import java.util.Map;
import java.util.HashMap;

import java.net.URI;

import javax.net.ssl.SSLContext;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;

import oracle.iam.identity.icf.foundation.SecurityContext;
import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.AbstractEndpoint.Principal;

import oracle.iam.identity.icf.foundation.logging.Loggable;
import oracle.iam.identity.icf.foundation.logging.AbstractLogger;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.rest.feature.LoggerFeature;
import oracle.iam.identity.icf.rest.feature.MarshallFeature;

import oracle.iam.identity.icf.authn.oauth.Token;
import oracle.iam.identity.icf.authn.oauth.AuthorizationFeature;

///////////////////////////////////////////////////////////////////////////////
// abstract class Authorization
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Default implementation of {@link AuthorizationFlow}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class Authorization extends    AbstractLogger
                             implements AuthorizationFlow {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final URI                 accessTokenURI;
  final URI                 refreshTokenURI;

  final Client              client;

  volatile Token            token;

  final Map<String, String> accessToken;
  final Map<String, String> refreshToken;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~
  /**
   ** Builder implementation.
   */
  abstract static class Builder<T extends Builder> implements AuthorizationFlow.Builder<Builder> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    URI                 accessTokenURI;
    URI                 refreshTokenURI;
    Principal           identifier;
    Map<String, String> accessToken   = new HashMap<>();
    Map<String, String> refreshToken  = new HashMap<>();

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
     ** @param  accessTokenURI   the access token URI on which the access token
     **                          can be requested.
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
     ** @return                  the access token URI.
     **                          <br>
     **                          Possible object is {@link URI}.
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
     ** @param  uri              the access token URI.
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
     ** defined in {@link #accessTokenURI(URI)} (which is the default value
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
    // Method: principal (AuthorizationFlow.Builder)
    /**
     ** Set client identifier of the application that should be authorized.
     **
     ** @param  identifier       the client identifier {@link Principal}
     **                          instance.
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
    public Builder property(final AuthorizationFlow.Phase phase, final String key, final String value) {
      phase.property(key, value, this.accessToken, this.refreshToken);
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Credential
  // ~~~~~ ~~~~~~~~~~
  /**
   ** Authorization implementation.
   */
  static class Credential extends Authorization {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Builder
    // ~~~~~ ~~~~~~
    /**
     ** Builder implementation.
     */
    static class Builder<T extends Builder> extends Authorization.Builder<Builder> {

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a default <code>Builder</code> for OAuth2 with defined
       ** URI's and client id.
       **
       ** @param  identifier     the client identifier (id of application that
       **                        wants to be approved). Issued by the Service
       **                        Provider.
       **                        <br>
       **                        Allowed object is {@link Principal}.
       ** @param  accessTokenURI the access token URI on which the access token
       **                        can be requested.
       **                        <br>
       **                        The URI points to the authorization server and
       **                        is defined by the Service Provider.
       **                        <br>
       **                        Allowed object is {@link URI}.
       */
      public Builder(final Principal identifier, final URI accessTokenURI) {
        // ensure inheritance
        super(identifier, accessTokenURI);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: build (AuthorizationFlow.Builder)
      /**
       ** Factory method to build a <code>AuthorizationFlow</code> which is
       ** associated with the specified {@link Loggable}.
       ** <br>
       ** The <code>AuthorizationFlow</code> will provide only default
       ** parameters therefor custom or additonal parameter need to be populated
       ** manually.
       **
       ** @param  loggable       the {@link Loggable} that instantiate this
       **                        <code>AuthorizationFlow</code> configuration
       **                        wrapper.
       **                        <br>
       **                        Allowed object is {@link Loggable}.
       ** @param  context        the {@link SSLContext} to proteced the
       **                        communication for this
       **                        <code>AuthorizationFlow</code>.
       **                        <br>
       **                        Allowed object is {@link SSLContext}.
       **
       ** @return                the new instance of {@code AuthorizationFlow}.
       **                        <br>
       **                        Possible object {@link AuthorizationFlow}.
       **
       ** @throws SystemException if the security context could not be
       **                         configured.
       */
      @Override
      public Authorization build(final Loggable loggable)
        throws SystemException {

        return new Credential(loggable, this.accessTokenURI, this.refreshTokenURI, this.identifier, this.accessToken, this.refreshToken);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link Authorization} <code>Client Credential Grant</code>
     ** which is associated with the specified {@link Loggable}.
     ** <br>
     ** The <code>IT Resource</code> will provide only default parameters
     ** therefor custom or additonal parameter need to be populated manually.
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Authorization</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  accessTokenURI   the {@link URI} to obtain the
     **                          <code>Access Token</code> from the
     **                          <code>Authorization Server</code>
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  refreshToeknURI  the {@link URI} to obtain the
     **                          <code>Refresh Token</code> from the
     **                          <code>Authorization Server</code>
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    private Credential(final Loggable loggable, final URI accessTokenURI, final URI refreshToeknURI, final Principal identifier, final Map<String, String> accessRequest, final Map<String, String> refreshRequest)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.CLIENT_CREDENTIALS, accessTokenURI, refreshToeknURI, identifier, accessRequest, refreshRequest);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Password
  // ~~~~~ ~~~~~~~~
  /**
   ** Authorization implementation.
   */
  static class Password extends Authorization {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Builder
    // ~~~~~ ~~~~~~
    /**
     ** Builder implementation.
     */
    static class Builder<T extends Builder> extends Authorization.Builder<Builder> {

      ////////////////////////////////////////////////////////////////////////////
      // instance attributes
      ////////////////////////////////////////////////////////////////////////////

      private Principal principal;

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a default <code>Builder</code> for OAuth2 with defined
       ** URI's and client id.
       **
       ** @param  identifier     the client identifier (id of application that
       **                        wants to be approved). Issued by the Service
       **                        Provider.
       **                        <br>
       **                        Allowed object is {@link Principal}.
       ** @param  principal      the user password credentials. Issued by the
       **                        Service Provider.
       **                        <br>
       **                        Allowed object is {@link Principal}.
       ** @param  tokenURI       the access token URI on which the access token
       **                        can be requested.
       **                        <br>
       **                        The URI points to the authorization server and
       **                        is defined by the Service Provider.
       **                        <br>
       **                        Allowed object is {@link URI}.
       */
      public Builder(final Principal identifier, final Principal principal, final URI tokenURI) {
        // ensure inheritance
        super(identifier, tokenURI);

        // initialize instance attributes
        this.principal = principal;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: build (AuthorizationFlow.Builder)
      /**
       ** Factory method to build a <code>AuthorizationFlow</code> which is
       ** associated with the specified {@link Loggable}.
       ** <br>
       ** The <code>AuthorizationFlow</code> will provide only default
       ** parameters therefor custom or additonal parameter need to be populated
       ** manually.
       **
       ** @param  loggable       the {@link Loggable} that instantiate this
       **                        <code>AuthorizationFlow</code> configuration
       **                        wrapper.
       **                        <br>
       **                        Allowed object is {@link Loggable}.
       **
       ** @return                the new instance of {@code AuthorizationFlow}.
       **                        <br>
       **                        Possible object {@link AuthorizationFlow}.
       **
       ** @throws SystemException if the security context could not be
       **                         configured.
       */
      @Override
      public Authorization build(final Loggable loggable)
        throws SystemException {

        return new Password(loggable, this.accessTokenURI, this.refreshTokenURI, this.identifier, this.principal, this.accessToken, this.refreshToken);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link Authorization} <code>Password Grant</code> which is
     ** associated with the specified {@link Loggable}.
     ** <br>
     ** The <code>IT Resource</code> will provide only default parameters
     ** therefor custom or additonal parameter need to be populated manually.
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Authorization</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  accessTokenURI   the {@link URI} to obtain the
     **                          <code>Access Token</code> from the
     **                          <code>Authorization Server</code>
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  refreshTokenURI  the {@link URI} to obtain the
     **                          <code>Refresh Token</code> from the
     **                          <code>Authorization Server</code>
     **                          <br>
     **                          Allowed object is {@link URI}.
     ** @param  identifier       the client credential to pass as
     **                          <code>client_id</code> and
     **                          <code>client_secret</code> to the
     **                          <code>Authorization Server</code>.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     ** @param  principal        the principal credential to pass as
     **                          <code>username</code> and
     **                          <code>password</code> to the
     **                          <code>Authorization Server</code>.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    private Password(final Loggable loggable, final URI accessTokenURI, final URI refreshTokenURI, final Principal identifier, final Principal principal, final Map<String, String> accessToken, final Map<String, String> refreshToken)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.PASSWORD, accessTokenURI, refreshTokenURI, identifier, accessToken, refreshToken);

      // initialize instance attributes
      defaultProperty(USERNAME, principal.username(),                            this.accessToken);
      defaultProperty(PASSWORD, CredentialAccessor.string(principal.password()), this.accessToken);
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
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Authorization</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  accessTokenURI     the {@link URI} to obtain the
   **                            <code>Access Token</code> from the
   **                            <code>Authorization Server</code>
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  refreshTokenhURI   the {@link URI} to obtain the
   **                            <code>Refresh Token</code> from the
   **                            <code>Authorization Server</code>
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  identifier         the client identifier credential to pass as
   **                            <code>client_id</code> and
   **                            <code>client_secret</code> to the
   **                            <code>Authorization Server</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @throws SystemException    if the security context could not be
   **                            configured.
   */
  protected Authorization(final Loggable loggable, final Grant grantType, final URI accessTokenURI, final URI refreshTokenURI, final Principal identifier, final Map<String, String> accessToken, final Map<String, String> refreshToken)
    throws SystemException {

    // ensure inheritance
    super((loggable == null) ? null : loggable.logger());

    // initialize instance attributes
    this.accessTokenURI  = accessTokenURI;
    this.refreshTokenURI = (refreshTokenURI != null) ? refreshTokenURI : accessTokenURI;

    this.accessToken     = accessToken;
    this.refreshToken    = refreshToken;

    this.client         = configureClient();

    // initialize instance attributes
    defaultProperty(GRANT_TYPE,    grantType.name().toLowerCase(),                    this.accessToken);
    defaultProperty(GRANT_TYPE,    Grant.REFRESH_TOKEN.name().toLowerCase(),          this.refreshToken);
    defaultProperty(CLIENT_PUBLIC, identifier.username(),                             this.accessToken, this.refreshToken);
    defaultProperty(CLIENT_SECRET, identifier.password() != null ? CredentialAccessor.string(identifier.password()) : "", this.accessToken, this.refreshToken);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessToken (AuthorizationFlow)
  /**
   ** Finish the authorization process and return the {@link Token}.
   ** <br>
   ** The method must be called on the same instance after the {@link #start()}
   ** method was called and user granted access to this application.
   ** <p>
   ** The method makes a request to the <code>Authorization Server</code> in
   ** order to exchange <code>code</code> for access token.
   **
   **
   ** @return                    the {@link Token} result.
   **                            <br>
   **                            Possible object is {@link Token}.
   **
   ** @throws SystemException    if the access token cannot be obtained from
   **                            the <code>Authorization Server</code>.
   */
  @Override
  public final void accessToken()
    throws SystemException {

    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.accessToken.entrySet())
      form.param(entry.getKey(), entry.getValue());

    final WebTarget target   = this.client.target(this.accessTokenURI);
    Response        response = null;
    try {
      response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
      final int status = response.getStatus();
      if (status != 200)
        throw toException(response);

      this.token = Token.build(response.readEntity((new GenericType<HashMap<String, Object>>(){})));
    }
    catch (ProcessingException e) {
      throw ServiceException.from(e, target.getUri());
    }
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
   **
   ** @throws SystemException    if the refresh token cannot be obtained from
   **                            the <code>Authorization Server</code>.
   */
  @Override
  public final void refreshToken(final String refreshToken)
    throws SystemException {

    this.refreshToken.put(REFRESH_TOKEN, refreshToken);
    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.refreshToken.entrySet()) {
      form.param(entry.getKey(), entry.getValue());
    }

    Response response = null;
    try {
      response = this.client.target(this.refreshTokenURI).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
      final int status   = response.getStatus();
      if (status != 200) {
        throw toException(response);
      }
      this.token = Token.build(response.readEntity((new GenericType<HashMap<String, Object>>(){})));
    }
    catch (ProcessingException e) {
      throw toException(response);
    }
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
  // Method:   toException
  /**
   ** Convert a JAX-RS response to a {@link ServiceException}.
   **
   ** @param  response           the JAX-RS response.
   **
   ** @return                    the converted {@link ServiceException}.
   */
  static SystemException toException(final Response response) {
    return ServiceException.from(response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClient
  /**
   ** Configures the client that should be used internally by the
   ** {@link AuthorizationFlow} to make requests to
   ** <code>Authorization Server</code>.
   ** <p>
   ** The assumption made is that the communication with the
   ** <code>Authorization Server</code> is always secured
   **
   ** @return                    the {@link Client} configured to make requests
   **                            to <code>Authorization Server</code>.
   **                            <br>
   **                            Possible object is {@link Client}.
   **
   ** @throws SystemException    if the security context could not be
   **                            configured.
   */
  private synchronized Client configureClient()
    throws SystemException {

    // Use TLSv1.2
    return ClientBuilder.newBuilder().sslContext(SecurityContext.build().create()).hostnameVerifier(SecurityContext.INSECURE_HOSTNAME_VERIFIER).build().register(MarshallFeature.class).register(new LoggerFeature(this, LoggerFeature.Verbosity.PAYLOAD_ANY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProperty
  private static void defaultProperty(final String key, final String value, final Map<String, String>... properties) {
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