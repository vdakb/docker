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

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Authorization.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Authorization.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.v1;

import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.InputStream;

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

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.logging.Loggable;
import oracle.hst.platform.core.logging.AbstractLogger;

import oracle.hst.platform.core.network.Principal;
import oracle.hst.platform.core.network.SecurityContext;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.iam.platform.oauth.Bearer;
import oracle.iam.platform.oauth.AuthorizationException;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

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

  final Map<String, String> accessRequest;
  final Map<String, String> refreshRequest;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~
  /**
   ** Builder implementation.
   */
  static abstract class Builder<T extends Builder> implements AuthorizationFlow.Builder<Builder> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    Client                 client;
    URI                    accessTokenURI;
    URI                    refreshTokenURI;
    Principal              identifier;
    Map<String, String>    accessToken   = new HashMap<>();
    Map<String, String>    refreshToken  = new HashMap<>();

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
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
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
      // Methods of abstract base classes
      //////////////////////////////////////////////////////////////////////////

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

        return new Credential(loggable, this.accessTokenURI, this.refreshTokenURI, this.identifier, this.client, this.accessToken, this.refreshToken);
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
     ** @param  client           a {@link Client} configured to make requests
     **                          to <code>Authorization Server</code>.
     **                          <br>
     **                          If <code>null</code> is passed in a default
     **                          client will be created.
     **                          <br>
     **                          Allowed object is {@link Client}.
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
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    private Credential(final Loggable loggable, final URI accessTokenURI, final URI refreshTokenURI, final Principal identifier, final Client client, final Map<String, String> accessRequest, final Map<String, String> refreshRequest)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.CLIENT_CREDENTIALS, accessTokenURI, refreshTokenURI, identifier, client, accessRequest, refreshRequest);
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
      // Methods of abstract base classes
      //////////////////////////////////////////////////////////////////////////

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

        return new Password(loggable, this.accessTokenURI, this.refreshTokenURI, this.identifier, this.principal, this.client, this.accessToken, this.refreshToken);
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
     ** @param  client           a {@link Client} configured to make requests
     **                          to <code>Authorization Server</code>.
     **                          <br>
     **                          If <code>null</code> is passed in a default
     **                          client will be created.
     **                          <br>
     **                          Allowed object is {@link Client}.
     **
     ** @throws SystemException  if the security context could not be
     **                          configured.
     */
    private Password(final Loggable loggable, final URI accessTokenURI, final URI refreshTokenURI, final Principal identifier, final Principal principal, final Client client, final Map<String, String> accessToken, final Map<String, String> refreshToken)
      throws SystemException {

      // ensure inheritance
      super(loggable, Grant.PASSWORD, accessTokenURI, refreshTokenURI, identifier, client, accessToken, refreshToken);

      // initialize instance attributes
      defaultProperty(USERNAME, principal.username,                   this.accessRequest);
      defaultProperty(PASSWORD, String.valueOf(principal.password()), this.accessRequest);
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
   ** @param  refreshTokenURI    the {@link URI} to obtain the
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
   ** @param  client             a {@link Client} configured to make requests
   **                            to <code>Authorization Server</code>.
   **                            <br>
   **                            Allowed object is {@link Client}.
   ** @param  accessRequest      the parameters to form the authorization
   **                            request to obtain an initial access token.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} for the value.
   ** @param  refreshRequest     the parameters to form the authorization
   **                            request to obtain an refreshed access token.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} for the value.
   **
   ** @throws SystemException    if the security context could not be
   **                            configured.
   */
  protected Authorization(final Loggable loggable, final Grant grantType, final URI accessTokenURI, final URI refreshTokenURI, final Principal identifier, final Client client, final Map<String, String> accessRequest, final Map<String, String> refreshRequest)
    throws SystemException {

    // ensure inheritance
    super((loggable == null) ? null : loggable.logger());

    // initialize instance attributes
    this.client          = configureClient(client, identifier);
    this.accessTokenURI  = accessTokenURI;
    this.refreshTokenURI = (refreshTokenURI != null) ? refreshTokenURI : accessTokenURI;
    this.accessRequest   = accessRequest;
    this.refreshRequest  = refreshRequest;

    // initialize instance attributes
    defaultProperty(GRANT_TYPE, grantType.name().toLowerCase(),           this.accessRequest);
    defaultProperty(GRANT_TYPE, Grant.REFRESH_TOKEN.name().toLowerCase(), this.refreshRequest);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessToken (AuthorizationFlow)
  /**
   ** Finish the authorization process and return the {@link Bearer} token.
   ** <br>
   ** The method must be called on the same instance after the {@link #start()}
   ** method was called and user granted access to this application.
   ** <p>
   ** The method makes a request to the <code>Authorization Server</code> in
   ** order to exchange <code>code</code> for access token.
   **
   ** @return                    the {@link Bearer} token result.
   **                            <br>
   **                            Possible object is {@link Bearer}.
   **
   ** @throws SystemException    if the access token cannot be obtained from
   **                            the <code>Authorization Server</code>.
   */
  @Override
  public final Bearer accessToken()
    throws SystemException {

    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.accessRequest.entrySet())
      form.param(entry.getKey(), entry.getValue());

    final WebTarget target   = this.client.target(this.accessTokenURI);
    Response        response = null;
    InputStream     stream   = null;
    try {
      response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
      final int status = response.getStatus();
      if (status != Response.Status.OK.getStatusCode()) {
        throw AuthorizationException.from(response);
      }
      stream = response.readEntity(InputStream.class);
      return Bearer.build(JsonMarshaller.read(JsonMarshaller.readObject(stream)));
    }
    catch (ProcessingException e) {
      throw AuthorizationException.from(e, target.getUri());
    }
    finally {
      if (stream != null)
        try {
          stream.close();
        }
        catch (IOException e) {
          e.printStackTrace(System.err);
        }
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
   ** @return                    the {@link Bearer} token result.
   **                            <br>
   **                            Possible object is {@link Bearer}.
   **
   ** @throws SystemException    if the refresh token cannot be obtained
   **                            from the <code>Authorization Server</code>.
   */
  @Override
  public final Bearer refreshToken(final String refreshToken)
    throws SystemException {

    this.refreshRequest.put(REFRESH_TOKEN, refreshToken);
    final Form form = new Form();
    for (final Map.Entry<String, String> entry : this.refreshRequest.entrySet()) {
      form.param(entry.getKey(), entry.getValue());
    }

    final WebTarget target   = this.client.target(this.refreshTokenURI);
    Response        response = null;
    InputStream     stream   = null;
    try {
      response = target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
      final int status   = response.getStatus();
      if (status != Response.Status.OK.getStatusCode()) {
        throw AuthorizationException.from(response);
      }
      stream = response.readEntity(InputStream.class);
      return Bearer.build(JsonMarshaller.read(JsonMarshaller.readObject(stream)));
    }
    catch (ProcessingException e) {
       throw AuthorizationException.from(e, target.getUri());
    }
    finally {
      if (stream != null)
        try {
          stream.close();
        }
        catch (IOException e) {
          e.printStackTrace(System.err);
        }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClient
  /**
   ** Configures the client that should be used internally by the
   ** {@link AuthorizationFlow} to make requests to
   ** <code>Authorization Server</code>.
   ** <p>
   ** The assumption made is that the communication with the
   ** <code>Authorization Server</code> is always secured.
   **
   ** @param  client             a {@link Client} configured to make requests
   **                            to <code>Authorization Server</code>.
   **                            <br>
   **                            Allowed object is {@link Client}.
   ** @param  identifier         the user password credentials. Issued by the
   **                            Service Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   **
   ** @return                    the {@link Client} configured to make requests
   **                            to <code>Authorization Server</code>.
   **                            <br>
   **                            Possible object is {@link Client}.
   **
   ** @throws SystemException    if the security context could not be
   **                            configured.
   */
  private synchronized Client configureClient(Client client, final Principal identifier)
    throws SystemException {

    if (client == null)
    // Use TLSv1.2
      client = ClientBuilder.newBuilder().sslContext(SecurityContext.build().create()).hostnameVerifier(SecurityContext.INSECURE_HOSTNAME_VERIFIER).build();

    return client.register(preemptiveBasic(identifier));
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultProperty
  private static void defaultProperty(final String key, final String value, final Map<String, String> properties) {
    if (key != null && value != null) {
      if (!properties.containsKey(key)) {
        properties.put(key, value);
      }
    }
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
  private static Feature preemptiveBasic(final Principal principal) {
    return HttpAuthenticationFeature.basic(principal.username, principal.password());
  }
}