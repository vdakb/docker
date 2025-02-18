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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   Credential.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Credential.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.jacc;

import java.util.Base64;
import java.util.Objects;

import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.container.ContainerRequestContext;

////////////////////////////////////////////////////////////////////////////////
// interface Credential
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** An interface for classes which credentials.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Credential {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Basic
  // ~~~~~ ~~~~~
  /**
   ** A set of user-provided Basic Authentication credentials, consisting of a
   ** username and a password.
   */
  static class Basic implements Credential {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The user name the credential belongs to. */
    public final String username;

    /** The password credential used for authentication purpose. */
    public final String password;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Basic</code> Authentication credentials,
     ** consisting of a <code>username</code> and a <code>password</code>.
     **
     ** @param  username         the user name the credential belongs to.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the password credential used for authentication
     **                          purpose.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Basic(final String username, final String password) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.username = username;
      this.password = password;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.username, this.password);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Basic</code> credentials are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>Basic</code> credentials may be different even though they contain
     ** the same set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Basic that = (Basic)other;
      return Objects.equals(this.username, that.username)
          && Objects.equals(this.password, that.password)
      ;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("BasicCredentials{");
      builder.append("\"username\"=\"").append(this.username).append("\"");
      builder.append("\"password\"=\"").append("********").append("\"");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Token
  // ~~~~~ ~~~~~
  /**
   ** A set of user-provided token credentials, consisting of a string.
   */
  static class Token implements Credential {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** Query parameter used to pass Bearer token
     **
     ** @see <a href="https://tools.ietf.org/html/rfc6750#section-2.3">The OAuth 2.0 Authorization Framework: Bearer Token Usage</a>
     */
    public static final String PARAMETER = "access_token";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The token credential belongs to. */
    public final String token;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Token</code> Authentication {@link Credential},
     ** consisting of a <code>token</code>.
     **
     ** @param  token            the token the <code>Token</code>
     **                          {@link Credential} belongs to.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Token(final String token) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.token = token;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.token);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Token</code> credentials are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>Token</code> credentials may be different even though they contain
     ** the same set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Token that = (Token)other;
      return Objects.equals(this.token, that.token);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder("Token{");
      builder.append("\"token\"=\"").append(this.token).append("\"");
      return builder.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   basic
  /**
   ** Factory nethod to create a {@link Basic} Authentication
   ** <code>Credential</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   **
   ** @return                    the {@link Basic} Authentication
   **                            <code>Credential</code> populated with the
   **                            given <code>username</code> and
   **                            <code>password</code>.
   **                            <br>
   **                            Possible objject is {@link Basic}.
   */
  static Basic basic(final ContainerRequestContext request) {
    // get the Authorization header from the request
    final String credential = extract(request, "Basic");
    if (credential == null)
      return null;

    final String decoded;
    try {
      decoded = new String(Base64.getDecoder().decode(credential.trim()), StandardCharsets.UTF_8);
    }
    catch (IllegalArgumentException e) {
      return null;
    }

    // decoded credentials is 'username:password'
    final int i = decoded.indexOf(':');
    if (i <= 0) {
      return null;
    }

    return new Basic(decoded.substring(0, i), decoded.substring(i + 1));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Factory nethod to create a {@link Token} Authentication
   ** <code>Credential</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   **
   ** @return                    the {@link Token} Authentication
   **                            <code>Credential</code> populated with the
   **                            given <code>token</code>.
   **                            <br>
   **                            Possible objject is {@link Token}.
   */
  static Token token(final ContainerRequestContext request) {
    String credential = extract(request, "Bearer");
    // if authorization header is not used, check query parameter where token
    // can be passed as well
    if (credential == null) {
      credential = request.getUriInfo().getQueryParameters().getFirst(Token.PARAMETER);
    }
    // check if the Authorization header is valid
    // it must not be null and must be prefixed with "Bearer" plus a whitespace
    // the authentication scheme comparison must be case-insensitive
    return (credential == null) ? null : new Token(credential.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  extract
  /**
   ** Parses a value of the <code>Authorization</code> header in the form of
   ** <code>Basic a892bf3e284da9bb40648ab10</code> or
   ** <code>Bearer a892bf3e284da9bb40648ab10</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   ** @param  scheme             the authorization scheme.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a token string representing an identity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static String extract(final ContainerRequestContext request, final String scheme) {
    // get the Authorization header from the request
    String authorization = header(request, HttpHeaders.AUTHORIZATION);
    // prevent bogus state
    if (authorization == null)
      return null;

    // check if the Authorization header is valid
    // it must not be null and must be prefixed with scheme plus a whitespace
    // the authentication scheme comparison must be case-insensitive
    final int space = authorization.indexOf(' ');
    if (space < 1)
      return null;

    return scheme.equalsIgnoreCase(authorization.substring(0, space)) ? authorization.substring(space + 1) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  header
  /**
   ** Parses a value of the <code>Authorization</code> header in the form of
   ** <code>Basic a892bf3e284da9bb40648ab10</code> or
   ** <code>Bearer a892bf3e284da9bb40648ab10</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link ContainerRequestContext}.
   ** @param  header             the value of the <code>Authorization</code>
   **                            header.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a token string representing an identity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static String header(final ContainerRequestContext request, final String header) {
    return request.getHeaders().getFirst(header);
  }
}