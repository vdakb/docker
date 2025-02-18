/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   TokenResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    TokenResponse.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.model;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// class TokenResponse
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Data Value Object for Token service
 **
 ** @author  jovan.j.lakic@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TokenResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The segment name for the type of a token.
   */
  public static final String TYPE      = "token_type";

  /**
   ** The segment name for the id token string as issued by the authorization
   ** server.
   */
  public static final String OPENID    = "id_token";

  /**
   ** The segment name for the access token string as issued by the
   ** authorization server.
   */
  public static final String ACCESS    = "access_token";

  /**
   ** The segment name for the refresh token string as issued by the
   ** authorization server.
   */
  public static final String REFRESH   = "refresh_token";

  /**
   ** The segment name for the expiry timestamp as issued by the authorization
   ** server.
   */
  public static final String EXPIRESIN = "expires_in";

  /**
   ** The segment name for the granted scope requested by a userr.
   */
  public static final String SCOPE     = "scope";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The type of token this is, typically just the string “Bearer”.
   */
  private String  token_type;
  /**
   ** The id token string as issued by the authorization server.
   */
  private String  id_token;
  /**
   ** The access token string as issued by the authorization server.
   */
  private String  access_token;
  /**
   ** If the access token will expire, then it is useful to return a refresh
   ** token which applications can use to obtain another access token. However,
   ** tokens issued with the implicit grant cannot be issued a refresh token.
   */
  private String  refresh_token;
  /**
   ** If the access token expires, the server replies with the duration of time
   ** the access token is granted for.
   */
  private Integer expires_in;
  /**
   ** If the scope the user granted is identical to the scope the app requested,
   ** this parameter is optional. If the granted scope is different from the
   ** requested scope, such as if the user modified the scope, then this
   ** parameter is required.
   */
  private String  scope;

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToken_type
  /**
   ** Sets the type of the returned access token.
   ** <br>
   ** Type is in most cases <code>bearer</code> (no cryptography is used) but
   ** provider might support also other kinds of token like <code>mac</code>.
   **
   ** @param  value              the token type.
   */
  public final void setToken_type(final String value) {
    this.token_type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getToken_type
  /**
   ** Returns the type of the returned access token.
   ** <br>
   ** Type is in most cases <code>bearer</code> (no cryptography is used) but
   ** provider might support also other kinds of token like <code>mac</code>.
   **
   ** @return                    the token type.
   */
  public final String getToken_type() {
    return this.token_type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId_token
  /**
   ** Sets the id token.
   **
   ** @param  value              the id token.
   */
  public final void setId_token(final String value) {
    this.id_token = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIdToken
  /**
   ** Returns the id token.
   **
   ** @return                    the id token.
   */
  public String getId_token() {
    return this.id_token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setAccess_token
  /**
   ** Sets the access token.
   **
   ** @param  value              the access token.
   */
  public final void setAccess_token(final String value) {
    this.access_token = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccess_token
  /**
   ** Returns the access token.
   **
   ** @return                    the access token.
   */
  public final String getAccess_token() {
    return this.access_token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefresh_token
  /**
   ** Sets the refresh token.
   ** <br>
   ** <b>Note</b>:
   ** The refresh token must not be issued during the authorization flow. Some
   ** Service Providers issue refresh token only on first user authorization and
   ** some providers does not support refresh token at all and authorization
   ** flow must be always performed when token expires.
   **
   ** @param  value              the refresh token or <code>null</code> if the
   **                            value is not provided.
   */
 public final void setRefresh_token(final String value) {
    this.refresh_token = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRefresh_token
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
   */
  public final String getRefresh_token() {
    return this.refresh_token;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExpires_in
  /**
   ** Returns expiration time of the {@link #getAccess_token() access token} in
   ** seconds.
   **
   ** @param  value              the expiration time in seconds or
   **                            <code>null</code> if the value is not provided.
   */
  public final void setExpires_in(final Integer value) {
    this.expires_in = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExpires_in
  /**
   ** Returns expiration time of the {@link #getAccess_token() access token} in
   ** seconds.
   **
   ** @return                    the expiration time in seconds or
   **                            <code>null</code> if the value is not provided.
   */
  public final Integer getExpires_in() {
    return this.expires_in;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setScope
  /**
   ** Sets the scopes of the {@link #getAccess_token() access token}.
   **
   ** @param  value              the scopes or <code>null</code> if the value is
   **                            not provided.
   */
  public final void setScope(final String value) {
    this.scope = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getScope
  /**
   ** Returns scopes of the {@link #getAccess_token() access token}.
   **
   ** @return                    the scopes or <code>null</code> if the value is
   **                            not provided.
   */
  public final String getScope() {
    return this.scope;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overidden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int hashCode() {
    return Objects.hash(this.token_type, this.id_token, this.access_token, this.refresh_token, this.expires_in, this.scope);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>TokenResponse</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>TokenResponse</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final TokenResponse that = (TokenResponse)other;
    return (this.token_type    != null ? this.token_type.equals(that.token_type)       : that.token_type    != null)
        && (this.id_token      != null ? this.id_token.equals(that.id_token)           : that.id_token      != null)
        && (this.access_token  != null ? this.access_token.equals(that.access_token)   : that.access_token  != null)
        && (this.refresh_token != null ? this.refresh_token.equals(that.refresh_token) : that.refresh_token != null)
        && (this.expires_in    != null ? this.expires_in.equals(that.expires_in)       : that.expires_in    != null)
        && (this.scope         != null ? this.scope.equals(that.scope)                 : that.scope         != null)
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overidden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                  the string representation for this instance in
   **                          its minimal form.
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append(TYPE).append(":").append(this.token_type).append("\n");
    sb.append(OPENID).append(":").append(this.id_token).append("\n");
    sb.append(ACCESS).append(":").append(this.access_token).append("\n");
    sb.append(REFRESH).append(":").append(this.refresh_token).append("\n");
    sb.append(EXPIRESIN).append(":").append(this.expires_in).append("\n");
    sb.append(SCOPE).append(":").append(this.scope).append("\n");
    return sb.toString();
  }
}