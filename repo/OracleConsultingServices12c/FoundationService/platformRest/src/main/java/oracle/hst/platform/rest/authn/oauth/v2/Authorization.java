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
    Subsystem   :   Generic REST Library

    File        :   Authorization.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Authorization.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.authn.oauth.v2;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.net.URI;

import javax.ws.rs.ProcessingException;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.logging.Loggable;
import oracle.hst.platform.core.logging.AbstractLogger;

import oracle.hst.platform.core.network.Principal;
import oracle.hst.platform.core.network.SecurityContext;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.ServiceException;

import oracle.hst.platform.rest.plugin.LoggerFeature;

import oracle.hst.platform.rest.authn.oauth.Token;
import oracle.hst.platform.rest.authn.oauth.AuthorizationFeature;

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
  final List<Feature>       feature;

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

    Principal           client;
    List<Feature>       feature         = null;
    URI                 accessTokenURI;
    URI                 refreshTokenURI;
    Map<String, String> accessToken     = new HashMap<>();
    Map<String, String> refreshToken    = new HashMap<>();

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
    protected Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for OAuth2 with defined URI's
     ** and client id.
     **
     ** @param  client           the client identifier (id of application that
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
    public Builder(final Principal client, final URI accessTokenURI) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.client         = client;
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
    // Method: client
    /**
     ** Returns client identifier {@link Principal} of the application that
     ** should be authorized.
     **
     ** @return                  the client identifier {@link Principal}
     **                          instance.
     **                          <br>
     **                          Possible object is {@link Principal}.
     */
    Principal client() {
      return this.client;
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
     ** @param  client           the client identifier {@link Principal}
     **                          instance.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder client(final Principal client) {
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
    public Builder property(final AuthorizationFlow.Phase phase, final String key, final String value) {
      phase.property(key, value, this.accessToken, this.refreshToken);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: register (AuthorizationFlow.Builder)
    /**
     ** Register an optional {@link ClientRequestFilter}.
     ** <br>
     ** A JAX-RS 2.0 filter is a type of plug-in that gives a developer access
     ** to all of the JAX-RS messages passing through a JAX-RS client or server.
     ** <br>
     ** A filter is suitable for processing the metadata associated with a
     ** message:
     ** <ul>
     **   <li>HTTP headers
     **   <li>query parameters
     **   <li>media type
     **   <li>... and other metadata
     ** </ul>
     ** Filters have the capability to abort a message invocation (useful for
     ** security plug-ins, for example).
     ** <p>
     ** If you like, you can install multiple filters at each extension point,
     ** in which case the filters are executed in a chain (the order of
     ** execution is undefined, however, unless you specify a priority value for
     ** each installed filter). 
     **
     ** @param  value            the request feature(s) to add.
     **                          <br>
     **                          Allowed object is array of {@link Feature}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    @Override
    public Builder register(final Feature... value) {
      if (this.feature == null)
        this.feature = new ArrayList<Feature>();

      for (Feature cursor : value)
        this.feature.add(cursor);
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
       ** @param  client         the client identifier (id of application that
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
      public Builder(final Principal client, final URI accessTokenURI) {
        // ensure inheritance
        super(client, accessTokenURI);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: build (AuthorizationFlow.Builder)
      /**
       ** Factory method to build a <code>AuthorizationFlow</code>.
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

        return new Credential(loggable, this);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link Authorization} <code>Client Credential Grant</code>.
     ** <br>
     ** The <code>IT Resource</code> will provide only default parameters
     ** therefor custom or additonal parameter need to be populated manually.
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Authorization</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  builder          the configured {@link Credential.Builder}.
     **                          <br>
     **                          Allowed object is {@link Password.Builder}.
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    private Credential(final Loggable loggable, final Builder builder)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.CLIENT_CREDENTIALS, builder);

      // prevent bogus state
      if (StringUtility.empty(builder.client.username()))
        throw ServiceException.argumentIsNull(CLIENT_PUBLIC);

      // prevent bogus state
      if (builder.client.password() == null)
        throw ServiceException.argumentIsNull(CLIENT_SECRET);

      property(CLIENT_PUBLIC, builder.client.username(),                this.accessToken, this.refreshToken);
      property(CLIENT_SECRET, new String(builder.client.password()),    this.accessToken, this.refreshToken);
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
       ** @param  client         the client identifier (id of application that
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
      public Builder(final Principal client, final Principal principal, final URI tokenURI) {
        // ensure inheritance
        super(client, tokenURI);

        // initialize instance attributes
        this.principal = principal;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: build (AuthorizationFlow.Builder)
      /**
       ** Factory method to build a <code>AuthorizationFlow</code>.
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

        return new Password(loggable, this);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a {@link Authorization} <code>Password Grant</code>.
     ** <br>
     ** The <code>IT Resource</code> will provide only default parameters
     ** therefor custom or additonal parameter need to be populated manually.
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>Authorization</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     ** @param  builder          the configured {@link Password.Builder}.
     **                          <br>
     **                          Allowed object is {@link Password.Builder}.
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    @SuppressWarnings("unchecked")
    private Password(final Loggable loggable, final Builder builder)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.PASSWORD, builder);

      // prevent bogus state
      if (StringUtility.empty(builder.principal.username()))
        throw ServiceException.argumentIsNull(USERNAME);

      // prevent bogus state
      if (builder.principal.password() == null)
        throw ServiceException.argumentIsNull(PASSWORD);

      // initialize instance attributes
      property(USERNAME, builder.principal.username(),             this.accessToken);
      property(PASSWORD, new String(builder.principal.password()), this.accessToken);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Authorization</code>.
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
   ** @param  client             the client identifier credential to pass as
   **                            <code>client_id</code> and
   **                            <code>client_secret</code> to the
   **                            <code>Authorization Server</code>.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @throws SystemException    if the security context could not be
   **                            configured.
   */
  @SuppressWarnings("unchecked")
  protected Authorization(final Loggable loggable, final Grant grantType, final Builder builder)
    throws SystemException {

    // ensure inheritance
    super((loggable == null) ? null : loggable.logger());

    // initialize instance attributes
    this.accessTokenURI  = builder.accessTokenURI;
    this.refreshTokenURI = (builder.refreshTokenURI != null) ? builder.refreshTokenURI : builder.accessTokenURI;
    this.accessToken     = builder.accessToken;
    this.refreshToken    = builder.refreshToken;
    this.feature         = new ArrayList<>(builder.feature);
    this.client          = configureClient((loggable == null) ? this : loggable);

    // initialize instance attributes
    property(GRANT_TYPE, grantType.name().toLowerCase(),           this.accessToken);
    property(GRANT_TYPE, Grant.REFRESH_TOKEN.name().toLowerCase(), this.refreshToken);
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
    try {
      final Response response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
      if (response.getStatus() != Response.Status.OK.getStatusCode())
        throw toException(response);

      this.token = Token.build(response);
    }
    catch (ProcessingException e) {
      throw ServiceException.build(e, target.getUri());
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
      this.token = Token.build(response);
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
   ** Convert a JAX-RS response to a {@link SystemException}.
   **
   ** @param  response           the JAX-RS response.
   **
   ** @return                    the converted {@link SystemException}.
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
  private synchronized Client configureClient(final Loggable loggable)
    throws SystemException {

    // Use TLSv1.2
    final Client client = ClientBuilder.newBuilder()
      .sslContext(SecurityContext.build().create())
      .hostnameVerifier(SecurityContext.INSECURE_HOSTNAME_VERIFIER).build()
//      .register(MarshallFeature.class)
    ;
    if (this.feature != null) {
      for (Feature cursor : this.feature)
        client.register(cursor);
    }
    // register the logging feature as the last one
    return client.register(new LoggerFeature(loggable.unwrap(), LoggerFeature.Verbosity.PAYLOAD_ANY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  private static void property(final String key, final String value, final Map<String, String>... properties) {
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