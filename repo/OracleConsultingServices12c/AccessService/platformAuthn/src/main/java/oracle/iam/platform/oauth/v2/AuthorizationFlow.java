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

    File        :   AuthorizationFlow.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    AuthorizationFlow.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.v2;

import java.util.Map;

import java.net.URI;

import javax.ws.rs.core.Feature;

import javax.ws.rs.client.Client;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.logging.Loggable;
import oracle.hst.platform.core.network.Principal;

import oracle.iam.platform.oauth.Bearer;

///////////////////////////////////////////////////////////////////////////////
// interface AuthorizationFlow
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The interface that defines OAuth 2 Authorization Code Grant Flow.
 ** <p>
 ** The implementation of this interface is capable of performing of the user
 ** authorization defined in the OAuth2 specification as
 ** "Authorization Code Grant Flow" (OAuth 2 spec defines more Authorization
 ** Flows). The result of the authorization is the {@link Bearer access token}.
 ** <br>
 ** The implementation starts the authorization process by construction of a
 ** redirect URI to which the user should be redirected (the URI points to
 ** authorization consent page hosted by Service Provider). The user grants an
 ** access using this page. Service Provider redirects the user back to the our
 ** server and the authorization process is finished using the same instance of
 ** the interface implementation.
 ** <p>
 ** To perform the authorization follow these steps:
 ** <ol>
 **    <li>Get the instance of this interface using {@link Principal}.
 **    <li>Call {@link #start()} method. The method returns redirection uri as a
 **        String.
 **    <li>Redirect the user to the redirect URI returned from the
 **        <code>start</code> method. If your application deployment does not
 **        allow redirection (for example the app is a console application), then
 **        provide the redirection URI to the user in other ways.
 **    <li>User should authorize your application on the redirect URI.
 **    <li>After authorization the Authorization Server redirects the user back
 **        to the URI specified by
 **        {@link AuthorizationFlow.Builder#redirectURI(String)} and provide the
 **        <code>code</code> and <code>state</code> as a request query parameter.
 **        Extract these parameter from the request. If your deployment does not
 **        support redirection (your app is not a web server) then Authorization
 **        Server will provide the user with <code>code</code> in other ways (for
 **        example display on the html page). You need to get this code from the
 **        user. The <code>state</code> parameter is added to the redirect URI in
 **        the <code>start</code> method and the same parameter should be
 **        returned from the authorization response as a protection against CSRF
 **        attacks.
 **    <li>Use the <code>code</code> and <code>state</code> to finish the
 **        authorization process by calling the method
 **        {@link #finish(String,String)} supplying the <code>code</code> and
 **        the <code>state</code> parameter. The method will internally request
 **        the access token from the Authorization Server and return it.
 **    <li>You can use access token from {@link Bearer} together with
 **        {@link Principal} to perform the authenticated requests to the
 **        Service Provider. You can also call methods
 **        {@link #authorizedClient()} to get {@link Client client} already
 **        configured with support for authentication from consumer credentials
 **        and access token received during authorization process.
 ** </ol>
 ** <b>Important</b>:
 ** <br>
 ** One instance of the interface can be used only for one authorization
 ** process. The methods must be called exactly in the order specified by the
 ** list above. Therefore the instance is also not thread safe and no concurrent
 ** access is expected.
 ** <p>
 ** Instance must be stored between method calls (between <code>start</code> and
 ** <code>finish</code>) for one user authorization process as the instance
 ** keeps internal state of the authorization process.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface AuthorizationFlow {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Parameter <code>client_id</code> that corresponds to
   ** {@link Principal#identifier()}.
   */
//  static final String CLIENT_PUBLIC = "client_id";
  /**
   ** Parameter <code>client_secret</code> that corresponds to
   ** {@link Principal#credential()}.
   */
//  static final String CLIENT_SECRET = "client_secret";
  /**
   ** Parameter <code>response_type</code> used in the authorization request.
   ** For Authorization Code Grant Flow the value is <code>code</code>.
   */
  static final String RESPONSE_TYPE = "response_type";
  /**
   ** Parameter <code>refresh_token</code> contains Refresh Token (corresponds
   ** to {@link Bearer#refreshToken()}).
   */
  static final String REFRESH_TOKEN = "refresh_token";
  /**
   ** Parameter <code>grant_type</code>used in the access token request.
   */
  static final String GRANT_TYPE    = "grant_type";
  /**
   ** Parameter <code>redirect_uri</code> used in the authorization request.
   */
  static final String REDIRECT_URI  = "redirect_uri";
  static final String REDIRECT_OOB  = "urn:ietf:wg:oauth:2.0:oob";

  /** Authorization code */
  static final String CODE          = "code";
  /**
   ** Parameter <code>scope</code> that defines the scope to which an
   ** authorization is requested.
   ** <p>
   ** Space delimited format.
   ** <br>
   ** Scope values are defined by the Service Provider.
   */
  static final String SCOPE         = "scope";
  /**
   ** State parameter used in the authorization request and authorization
   ** response to protect against CSRF attacks.
   */
  static final String STATE         = "state";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Grant
  // ~~~~ ~~~~~
  /**
   ** Parameter <code>grant_type</code> used in the access token request.
   */
  enum Grant {
    /**
     ** Used to request an access token in the Authorization Code Grant Flow.
     ** <br>
     ** The parameter key defined by the OAuth2 protocol is equal to the name
     ** of this enum value converted to lowercase.
     */
    AUTHORIZATION_CODE,
    /**
     ** Used to refresh an access token in the Authorization Code Grant Flow.
     ** <br>
     ** The parameter key defined by the OAuth2 protocol is equal to the name
     ** of this enum value converted to lowercase.
     */
    REFRESH_TOKEN,
    /**
     ** Used in Resource Owner Password Credential Grant.
     ** <br>
     ** The parameter key defined by the OAuth2 protocol is equal to the name
     ** of this enum value converted to lowercase.
     */
    PASSWORD,
    /**
     ** Used in Client Credentials Flow.
     ** <br>
     ** The parameter key defined by the OAuth2 protocol is equal to the name
     ** of this enum value converted to lowercase.
     */
    CLIENT_CREDENTIALS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Phase
  // ~~~~ ~~~~~
  /**
   ** Phase of the Authorization Code Grant Flow.
   */
  enum Phase {
      /** All phases */
      ALL {
        @Override
        public void property(final String key, final String value, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, authorization);
          notNull(key, value, accessToken);
          notNull(key, value, refreshToken);
        }
      },

    /** Requesting the access token phase */
    ACCESS {
        @Override
        public void property(final String key, final String value, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, accessToken);
        }
      },

    /** Refreshing the access token phase */
    REFRESH {
        @Override
        public void property(final String key, final String value, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, refreshToken);
        }
      },

    /**
     ** Authorization phase.
     ** <br>
     ** The phase when user is redirected to the authorization server to
     ** authorize the application.
     */
    AUTHORIZATION {
        @Override
        public void property(final String key, final String value, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, authorization);
        }
      }
    ;

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: property
    /**
     ** Set property defined by <code>key</code> and <code>value</code> to the
     ** appropriate property map based on this phase.
     **
     ** @param  key              the property key.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the property value.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  authorization    the properties used in construction of redirect
     **                          URI.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     ** @param accessToken       the properties (parameters) used in access
     **                          token request.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     ** @param refreshToken      the roperties (parameters) used in request for
     **                          refreshing the access token.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     */
    public abstract void property(final String key, final String value, final Map<String, String> authorization, final Map<String, String> accessToken, final Map<String, String> refreshToken);

    ////////////////////////////////////////////////////////////////////////////
    // Method: notNull
    private static void notNull(final String key, final String value, final Map<String, String> properties) {
      if (value == null) {
        properties.remove(key);
      }
      else {
        properties.put(key, value);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Builder
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** The builder of {@link AuthorizationFlow}.
   **
   ** @param  <T>                the type of the builder implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the builders
   **                            implementing this interface (builder can return
   **                            their own specific type instead of type defined
   **                            by this interface only).
   */
  public interface Builder<T extends Builder> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: accessTokenURI
    /**
     ** Set the access token URI on which the access token can be requested.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @param  uri              the access token {@link URI}.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    T accessTokenURI(final URI uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshTokenURI
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
     ** @param  uri              the refresh token {@link URI}.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    T refreshTokenURI(final URI uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: authorizationURI
    /**
     ** Set the URI to which the user should be redirected to authorize the
     ** application.
     ** <br>
     ** The URI points to the authorization server and is defined by the Service
     ** Provider.
     **
     ** @param  uri              the authorization {@link URI}.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
     T authorizationURI(final URI uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: redirectURI
    /**
     ** Set the redirect URI to which the user (resource owner) should be
     ** redirected after he/she grants access to the application.
     ** <br>
     ** In most cases, the URI is under control of the application and request
     ** done on this URI will be used to extract query parameter
     ** <code>code</code> and <code>state</code> that will be used in
     ** {@link AuthorizationFlow#finish(String,String)} method.
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
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
     T redirectURI(final String uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Set client identifier of the application that should be authorized.
     **
     ** @param  identifier       the {@link Principal} instance.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    T identifier(final Principal identifier);

    ////////////////////////////////////////////////////////////////////////////
    // Method: scope
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
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    T scope(final String scope);

    ////////////////////////////////////////////////////////////////////////////
    // Method: client
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
    T client(final Client client);

    ////////////////////////////////////////////////////////////////////////////
    // Method: property
    /**
     ** Set roperty (parameter) that will be added to requests or URIs as a
     ** query parameters during the {@link AuthorizationFlow}.
     ** <br>
     ** Default parameters used during the {@link AuthorizationFlow} can be also
     ** overridden by this method.
     **
     ** @param  phase            the phase of the flow in which the properties
     **                          (parameters) should be used.
     **                          <br>
     **                          For example by using a
     **                          {@link AuthorizationFlow.Phase#ACCESS}, the
     **                          parameter will be added only to the http
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
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    T property(final Phase phase, final String key, final String value);

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to build a <code>AuthorizationFlow</code> which is
     ** associated with the specified {@link Loggable}.
     ** <br>
     ** The <code>AuthorizationFlow</code> will provide only default parameters
     ** therefor custom or additonal parameter need to be populated manually.
     **
     ** @param  loggable         the {@link Loggable} that instantiate this
     **                          <code>AuthorizationFlow</code> configuration
     **                          wrapper.
     **                          <br>
     **                          Allowed object is {@link Loggable}.
     **
     ** @return                  the new instance of {@code AuthorizationFlow}.
     **                          <br>
     **                          Possible object {@link AuthorizationFlow}.
     **
     ** @throws SystemException  if the authentication flow is secured by
     **                          SSl and the required {@code SSLContext}
     **                          could not be configured.
     */
    AuthorizationFlow build(final Loggable loggable)
      throws SystemException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Start the authorization flow and return redirection URI on which the user
   ** should give a consent for the application to access resources.
   **
   ** @return                    the {@link URI} to which user should be
   **                            redirected.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  URI start();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   finish
  /**
   ** Finish the authorization process and return the {@link Bearer} token.
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
   ** @return                    the {@link Bearer} token result.
   **                            <br>
   **                            Possible object is {@link Bearer}.
   */
  Bearer finish(final String code, final String state);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshToken
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
  Bearer refreshToken(final String refreshToken)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizedClient
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
  Client authorizedClient();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   feature
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
   **                            <br>
   **                            Possible object is {@link Feature}.
   */
  Feature feature();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Client Credential Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  tokenURI           the access token URI on which the access token
   **                            can be requested.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the builder of the
   **                            {@link AuthorizationFlow Client Credentials Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  static AuthorizationFlow.Builder credential(final Principal identifier, final URI tokenURI) {
    return new Authorization.Credential.Builder(identifier, tokenURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   codeGrant
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Authorization Code Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  tokenURI           the access token URI on which the access token
   **                            can be requested.
   **                            <br>
   **                            The URI points to the authorization server and
   **                            is defined by the Service Provider.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the builder of the
   **                            {@link AuthorizationFlow Client Credentials Grant Flow}.
   **                            Possible object is
   **                            {@link Authorization.Builder}.
   */
  static AuthorizationFlow.Builder codeGrant(final Principal identifier, final URI tokenURI) {
    return new Authorization.CodeGrant.Builder(identifier, tokenURI);
  }
}