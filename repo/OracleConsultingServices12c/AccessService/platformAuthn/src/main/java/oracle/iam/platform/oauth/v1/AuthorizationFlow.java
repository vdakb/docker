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

package oracle.iam.platform.oauth.v1;

import java.util.Map;

import java.net.URI;

import javax.net.ssl.SSLContext;

import javax.ws.rs.client.Client;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.logging.Loggable;

import oracle.hst.platform.core.network.Principal;

import oracle.iam.platform.oauth.Bearer;

///////////////////////////////////////////////////////////////////////////////
// interface AuthorizationFlow
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The interface that defines OAuth 1 Client Credential Grant Flow.
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
   ** to {@link oracle.iam.platform.oauth.Bearer#refreshToken()}).
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
       ** The parameter key defined by the OAuth1 protocol is equal to the name
       ** of this enum value converted to lowercase.
       */
      AUTHORIZATION_CODE
      /**
       ** Used to refresh an access token in the Authorization Code Grant Flow.
       ** <br>
       ** The parameter key defined by the OAuth1 protocol is equal to the name
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
       ** The parameter key defined by the OAuth1 protocol is equal to the name
       ** of this enum value converted to lowercase.
       */
    , CLIENT_CREDENTIALS
    ;
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
      },

    /** Refreshing the access token phase */
    REFRESH {
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
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
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
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    T refreshTokenURI(final URI uri);

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Set client identifier of the application that should be authorized.
     **
     ** @param  identifier       the {@link Principal} instance.
     **                          <br>
     **                          Allowed object is {@link Principal}.
     **
     ** @return                  the {@link Builder} to allow method chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    T identifier(final Principal identifier);

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
     **                          Possible object {@link SSLContext}.
     **
     ** @throws SystemException  if the authentication flow is secured by
     **                          SSl and the required {@link SSLContext}
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
   ** Finish the authorization process and obtains the
   ** {@link oracle.iam.platform.oauth.Bearer}.
   ** <p>
   ** The method makes a request to the <code>Authorization Server</code> in
   ** order to obtain an access token.
   **
   ** @return                    the {@link Bearer} token issued by the
   **                            authorization server.
   **                            <br>
   **                            Possible object is {@link Bearer}.
   **
   ** @throws SystemException    if the access token cannot be obtained from
   **                            <code>Authorization Server</code>.
   */
  Bearer accessToken()
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
   ** @return                    the {@link Bearer} token issued by the
   **                            authorization server.
   **                            <br>
   **                            Possible object is {@link Bearer}.
   **
   ** @throws SystemException    if the refresh token cannot be obtained
   **                            from the <code>Authorization Server</code>.
   */
  Bearer refreshToken(final String refreshToken)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Client Credentials Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  accessTokenURI     the access token URI on which the access token
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
  static AuthorizationFlow.Builder credential(final Principal identifier, final URI accessTokenURI) {
    return new Authorization.Credential.Builder(identifier, accessTokenURI);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Factory method to create the builder of the
   ** {@link AuthorizationFlow Client Credentials Grant Flow}.
   **
   ** @param  identifier         the client identifier (id of application that
   **                            wants to be approved). Issued by the Service
   **                            Provider.
   **                            <br>
   **                            Allowed object is {@link Principal}.
   ** @param  principal          the user password credentials. Issued by the
   **                            Service Provider.
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
  static AuthorizationFlow.Builder password(final Principal identifier, final Principal principal, final URI tokenURI) {
    return new Authorization.Password.Builder(identifier, principal, tokenURI);
  }
}