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

    File        :   AuthorizationFlow.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AuthorizationFlow.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.authn.oauth.v2;

import java.util.Map;

import java.net.URI;

import javax.ws.rs.core.Feature;

import javax.ws.rs.client.Client;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.logging.Loggable;

import oracle.hst.platform.core.network.Principal;

import oracle.hst.platform.rest.authn.oauth.Token;

///////////////////////////////////////////////////////////////////////////////
// interface AuthorizationFlow
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The interface that defines OAuth 2 Client Credential Grant Flow.
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
   ** {@link Principal#username()}.
   */
  static final String CLIENT_PUBLIC = "client_id";
  /**
   ** Parameter <code>client_secret</code> that corresponds to
   ** {@link Principal#password()}.
   */
  static final String CLIENT_SECRET = "client_secret";
  /**
   ** Parameter <code>username</code> for the user trying to get a token
   ** corresponds to {@link Principal#username()}.
   */
  static final String USERNAME      = "username";
  /**
   ** Parameter <code>password</code> the password for the user trying to get a
   ** token corresponds to {@link Principal#password()}.
   */
  static final String PASSWORD      = "password";
  /**
   ** Parameter <code>refresh_token</code> contains Refresh Token (corresponds
   ** to {@link Token#refreshToken()}).
   */
  static final String REFRESH_TOKEN = "refresh_token";
  /**
   ** Parameter <code>grant_type</code>used in the access token request.
   */
  static final String GRANT_TYPE    = "grant_type";

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
      AUTHORIZATION_CODE
      /**
       ** Used to refresh an access token in the Authorization Code Grant Flow.
       ** <br>
       ** The parameter key defined by the OAuth2 protocol is equal to the name
       ** of this enum value converted to lowercase.
       */
    , REFRESH_TOKEN
      /**
       ** Used in Resource Owner Password Credential Grant.
       ** <br>
       ** The parameter key defined by the OAuth2 protocol is equal to the name
       ** of this enum value converted to lowercase.
       */
    , PASSWORD
      /**
       ** Used in Client Credentials Flow.
       ** <br>
       ** The parameter key defined by the OAuth2 protocol is equal to the name
       ** of this enum value converted to lowercase.
       */
    , CLIENT_CREDENTIALS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Phase
  // ~~~~ ~~~~~
  /**
   ** Phase of the Authorization Code Grant Flow.
   */
  enum Phase {
      /** Requesting the access token phase */
      ACCESS {
        @Override
        public void property(final String key, final String value, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, accessToken);
        }
      }
      /** Refreshing the access token phase */
    , REFRESH {
        @Override
        public void property(final String key, final String value, final Map<String, String> accessToken, final Map<String, String> refreshToken) {
          notNull(key, value, refreshToken);
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
     ** @param  accessToken      the properties (parameters) used in access
     **                          token request.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     ** @param  refreshToken     the roperties (parameters) used in request for
     **                          refreshing the access token.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is a {@link String} mapped to a
     **                          {@link String}.
     */
    public abstract void property(final String key, final String value, final Map<String, String> accessToken, final Map<String, String> refreshToken);

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
     ** @param  uri              the access token URI.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code> for
     **                          type <code>T</code>.
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
     ** @param  uri              the refresh token URI.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code> for
     **                          type <code>T</code>.
     */
    T refreshTokenURI(final URI uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: client
    /**
     ** Set client identifier of the application that should be authorized.
     **
     ** @param  client           the client identifier {@link Principal}
     **                          instance.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code> for
     **                          type <code>T</code>.
     */
    T client(final Principal client);

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
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code> for
     **                          type <code>T</code>.
     */
    T property(final Phase phase, final String key, final String value);

    ////////////////////////////////////////////////////////////////////////////
    // Method: register
    /**
     ** Register optional {@link Feature}s.
     ** <br>
     ** A JAX-RS 2.0 features are a type of plug-in that gives a developer
     ** access to all of the JAX-RS messages passing through a JAX-RS client or
     ** server.
     ** <br>
     ** A feature is suitable for processing the metadata associated with a
     ** message:
     ** <ul>
     **   <li>HTTP headers
     **   <li>query parameters
     **   <li>media type
     **   <li>... and other metadata
     ** </ul>
     ** {@link Feature}s have the capability to abort a message invocation
     ** (useful for security plug-ins, for example).
     ** <p>
     ** If you like, you can install multiple {@link Feature}s at each extension
     ** point, in which case the filters are executed in a chain (the order of
     ** execution is undefined, however, unless you specify a priority value for
     ** each installed {@link Feature}). 
     **
     ** @param  value            the request feature(s) to add.
     **                          <br>
     **                          Allowed object is array of {@link Feature}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    T register(final Feature... value);

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
     **                          Possible object {@code AuthorizationFlow}.
     **
     ** @throws SystemException  if the authentication flow is secured by SSL
     **                          and the required {@code SSLContext}
     **                          could not be configured.
     */
    AuthorizationFlow build(final Loggable loggable)
      throws SystemException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessToken
  /**
   ** Finish the authorization process and return the {@link Token}.
   ** <p>
   ** The method makes a request to the <code>Authorization Server</code> in
   ** order to obtain an access token.
   **
   ** @throws SystemException    if the access token cannot be obtained from the
   **                            <code>Authorization Server</code>.
   */
  void accessToken()
    throws SystemException;

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
   ** @throws SystemException    if the refresh token cannot be obtained from
   **                            the <code>Authorization Server</code>.
   */
  void refreshToken(final String refreshToken)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authorizedClient
  /**
   ** Return the client configured for performing authorized requests to the
   ** Service Provider.
   ** <br>
   ** The authorization process must be successfully finished by instance by
   ** calling method {@link #accessToken()}.
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
   ** calling method {@link #accessToken()}.
   **
   ** @return                    the OAuth2 filter feature configured with
   **                            received <code>AccessToken</code>.
   */
  Feature feature();
}