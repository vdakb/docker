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

    File        :   Token.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Token.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.authn.oauth;

import java.util.Map;
import java.util.Collection;

///////////////////////////////////////////////////////////////////////////////
// class Token
// ~~~~~ ~~~~~
/**
 ** Class that contains a result of the Authorization Flow including a access
 ** token.
 ** <p>
 ** All result properties can be get by the method {@link #properties()}. Some
 ** of the properties are standardized by the OAuth 2 specification and
 ** therefore the class contains getters that extract these properties from the
 ** property map.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Token {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The property name of the access token type issued by the authorization
   ** server as described in
   ** <a href="http://tools.ietf.org/html/rfc6749#section-7.1">Access Token Types</a>.
   */
  public static final String TYPE    = "token_type";
  /**
   ** The property name of the access token issued by the authorization server.
   */
  public static final String ACCESS  = "access_token";
  /**
   ** The property name of the refresh token issued by the authorization server.
   */
  public static final String REFRESH = "refresh_token";
  /**
   ** The property name of the lifetime in seconds of the access token (for
   ** example 3600 for an hour).
   */
  public static final String EXPIRY  = "expires_in";
  /**
   ** The property name of the token scope issued by the authorization server
   ** as described in
   ** <a href="http://tools.ietf.org/html/rfc6749#section-3.3">Access Token Scope</a>.
   */
  public static final String SCOPE   = "scope";
  /**
   ** The property name of the Just-In-Time provisionig capabilities of the
   ** token.
   */
  public static final String JTI    = "jti";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the access properties */
  private final Map<String, Object> properties;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Token</code> initiated from the property map.
   **
   ** @param  properties         the access token properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   */
  private Token(final Map<String, Object> properties) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.properties = properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tokenType
  /**
   ** Returns the type of the returned access token.
   ** <br>
   ** Type is in most cases <code>bearer</code> (no cryptography is used) but
   ** provider might support also other kinds of token like <code>mac</code>.
   **
   ** @return                    the token type.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String tokenType() {
    return property(TYPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessToken
  /**
   ** Returns the access token.
   **
   ** @return                    the access token.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String accessToken() {
    return property(ACCESS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshToken
  /**
   ** Returns the refresh token.
   ** <br>
   ** <b>Note</b>:
   ** The refresh token must not be issued during the authorization flow. Some
   ** Service Providers issue refresh token only on first user authorization and
   ** some providers does not support refresh token at all and authorization
   ** flow must be always performed when token expires.
   **
   ** @return                    the refresh token or <code>null</code> if the
   **                            value is not provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String refreshToken() {
    return property(REFRESH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expiry
  /**
   ** Returns expiration time of the {@link #accessToken() access token} in
   ** seconds.
   **
   ** @return                    the expiration time in seconds or
   **                            <code>null</code> if the value is not provided.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer expiry() {
    final String expiration = property(EXPIRY);
    return (expiration == null)  ? null : Integer.valueOf(expiration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   properties
  /**
   ** Returns the map of all properties returned in the Access Token Response.
   **
   ** @return                    the {@link Map} with all token properties.
   */
  public Map<String, Object> properties() {
    return this.properties;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  private String property(final String name) {
    final Object property = properties.get(name);

    if (property != null) {
      if (property instanceof Collection) {
        for (final Object value : (Collection)property) {
          if (value != null) {
            return value.toString();
          }
        }
      }
      else {
        return property.toString();
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a OAuth <code>Token</code>.
   **
   ** @param  properties         the access token properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   **
   ** @return                    the created create a OAuth <code>Token</code>.
   **                            <br>
   **                            Possible object is <code>Token</code>.
   */
  public static Token build(final Map<String, Object> properties) {
    return new Token(properties);
  }
}