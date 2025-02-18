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

    File        :   TokenInfo.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    TokenInfo.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-07  TSebo     First release version
*/

package bka.iam.access.oidc.extension.model;

import java.util.List;
import java.util.Objects;

///////////////////////////////////////////////////////////////////////////////
// class TokenInfo
// ~~~~~ ~~~~~~~~~
/**
 ** Class that contains a result of the Authorization Flow including a access
 ** token.
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TokenInfo {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Registered Claim <code>iss</code> (issuer) as defined by RFC 7519 Section
   ** 4.1.1.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The issuer claim identifies the principal that issued the JSON Web Token.
   ** If the value contains a <code>:</code> it must be a URI.
   ** <p>
   ** The processing of this claim is generally application specific.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The <code>iss</code> value is a case-sensitive string containing a string
   ** or URI value.
   */
  private String       iss;

  /**
   ** Registered Claim <code>sub</code> (subject) as defined by RFC 7519 Section
   ** 4.1.2.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The subject claim identifies the principal that is the subject of the JSON
   ** Web Token. If the value contains a <code>:</code> it must be a URI.
   ** <p>
   ** The claims in a JWT are normally statements about the subject. The subject
   ** value <b>must</b> either be scoped to be locally unique in the context of
   ** the issuer or be globally unique.
   ** <br>
   ** The processing of this claim is generally application specific.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The <code>sub</code> value is a case-sensitive string containing a string
   ** or URI value.
   */
  private String       sub;

  /**
   ** Registered Claim <code>aud</code> (audience) as defined by RFC 7519
   ** Section 4.1.3.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The audience claim identifies the recipients that the JSON Web Token is
   ** intended for. This may be an array of strings or a single string, in
   ** either case if the string value contains a <code>:</code> it must be a
   ** URI.
   ** <b>Note</b>:
   ** <br>
   ** <b>Each</b> principal intended to process the JSON Web Token <b>must</b>
   ** identify itself with a value in the audience claim.
   ** <p>
   ** The interpretation of audience values is generally application specific.
   ** <p>
   ** If the principal processing the claim does not identify itself with a
   ** value in the <code>aud</code> claim when this claim is present, then the
   ** JWT <b>must</b> be rejected. In the general case, the <code>aud</code>
   ** value is an array of case- sensitive strings, each containing a string or
   ** URI value. In the special case when the JWT has one audience, the
   ** <code>aud</code> value <b>may</b> be a single case-sensitive string
   ** containing a string or URI value.
   */
  private List<String> aud;

  /**
   ** Registered Claim <code>exp</code> (expiration time) as defined by RFC 7519
   ** Section 4.1.4.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The expiration time claim identifies the expiration time on or after which
   ** the JSON Web Token <b>must not</b> be accepted for processing. The
   ** expiration time is expected to provided in UNIX time, or the number of
   ** seconds since Epoch.
   ** <p>
   ** Implementers <b>may</b> provide for some small leeway, usually no more
   ** than a few minutes, to account for clock skew. Its value <b>must</b> be a
   ** number containing a numeric date value.
   ** <p>
   ** The timestamp when the token stops being valid, specified as seconds since
   ** 00:00:00 UTC, January 1, 1970. The maximum lifetime of a token is 24 hours
   ** + skew.
   */
  private long         exp;

  /**
   ** Registered Claim <code>nbf</code> (not before) as defined by RFC 7519
   ** Section 4.1.5.
   ** <p>
   ** The not before claim identifies the time before which the JSON Web Token
   ** <b>must not</b> be accepted for processing. The not before time is
   ** expected to provided in UNIX time, or the number of seconds since Epoch.
   ** <p>
   ** Implementers <b>may</b> provide for some small leeway, usually no more
   ** than a few minutes, to account for clock skew. Its value <b>must</b> be a
   ** number containing a numeric date value.
   ** <p>
   ** The timestamp when the token stops being valid, specified as seconds since
   ** 00:00:00 UTC, January 1, 1970. The maximum lifetime of a token is 24 hours
   ** + skew.
   */
  private long         nbf;

  /**
   ** Registered Claim <code>iat</code> (issued at) as defined by RFC 7519
   ** Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The issued at claim identifies the time at which the JSON Web Token was
   ** issued. The issued at time is expected to provided in UNIX time, or the
   ** number of seconds since Epoch.
   ** <p>
   ** The timestamp when the token was created, specified as seconds since
   ** 00:00:00 UTC, January 1, 1970. The server may report an error if this
   ** timestamp is too far in the past or the future (allowing 10 minutes for
   ** skew).
   ** This claim can be used to determine the age of the JSON Web Token.
   ** Its value <b>must</b> be a number containing a numeric date value.
   */
  private long         iat;

  /**
   ** Registered Claim <code>jti</code> (JWT ID) as defined by RFC 7519 Section
   ** 4.1.7.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The JSON Web Token unique ID claim provides a unique identifier for the
   ** JSON Web Token.
   ** <p>
   ** The identifier value <b>must</b> be assigned in a manner that ensures that
   ** there is a negligible probability that the same value will be accidentally
   ** assigned to a different data object; if the application uses multiple
   ** issuers, collisions <b>must</b> be prevented among values produced by
   ** different issuers as well.
   ** <p>
   ** The JSON Web Token unique ID claim can be used to prevent the JSON Web
   ** Token from being replayed.
   ** <br>
   ** The <code>jti</code> value is a case-sensitive string.
   */
  private String       jti;

  private String       client;

  private List<String> scope;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TokenInfo</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TokenInfo() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setIss
  /**
   ** Sets the value for <code>iss</code> claim.
   **
   ** @param  value              the value to set for  <code>iss</code> claim.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setIss(final String value) {
    this.iss = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIss
  /**
   ** Returns the value of <code>iss</code> claim.
   **
   ** @return                    the value of  <code>iss</code> claim.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getIss() {
    return this.iss;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setSub
  /**
   ** Sets the value for <code>sub</code> claim.
   **
   ** @param  value              the value to set for <code>sub</code> claim.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setSub(final String value) {
    this.sub = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getSub
  /**
   ** Returns the value of <code>sub</code> claim.
   **
   ** @return                    the value to set for <code>subject</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getSub() {
    return this.sub;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setAud
  /**
   ** Sets the for <code>aud</code> claim.
   **
   ** @param  value              the value to set for <code>aud</code> claim.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public void setAud(final List<String> value) {
    this.aud = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getAud
  /**
   ** Returns the for claim <code>aud</code>.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the object.
   **
   ** @return                    the value set for <code>aud</code> claim.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> getAud() {
    return this.aud;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setExp
  /**
   ** Sets the value for <code>exp</code> claim.
   **
   ** @param  value              the value to set for <code>exp</code> claim.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  public void setExp(final long value) {
    this.exp = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getExp
  /**
   ** Sets the value value of <code>exp</code> claim.
   **
   ** @return                    the value set for <code>exp</code> claim.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long getExp() {
    return this.exp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setNbf
  /**
   ** Sets the value for <code>nbf</code> claim.
   **
   ** @param  value              the value to set for <code>nbf</code> claim.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  public void setNbf(final long value) {
    this.nbf = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getNbf
  /**
   ** Sets the value value of <code>nbf</code> claim.
   **
   ** @return                    the value set for <code>nbf</code> claim.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long getNbf() {
    return this.nbf;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setIat
  /**
   ** Sets the value for <code>iat</code> claim.
   **
   ** @param  value              the value to set for <code>iat</code> claim.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  public void setIat(long value) {
    this.iat = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getIat
  /**
   ** Returns the value of <code>iat</code> claim.
   **
   ** @return                    the value set for <code>iat</code> claim.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public long getIat() {
    return this.iat;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setJti
  /**
   ** Sets the value value of <code>jti</code> claim.
   **
   ** @param  value              the value set for <code>jti</code> claim.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void setJti(final String value) {
    this.jti = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getJti
  /**
   ** Sets the value value of <code>jti</code> claim.
   **
   ** @return                    the value set for <code>jti</code> claim.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getJti() {
    return this.jti;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   setScope
  /**
   ** Sets the the collection of scopes the access token is requested for.
   **
   ** @param  value              the collection of scopes the access token is
   **                            requested for.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public void setScope(final List<String> value) {
    this.scope = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getScope
  /**
   ** Returns the collection of scopes the access token is requested for.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the object.
   **
   ** @return                    the collection of scopes the access token is
   **                            requested for.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> getScope() {
    return this.scope;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   has
  /**
   ** Return <code>true</code> in case access token has scope provided in
   ** parameter scope.
   **
   ** @param  scope              the name of the scope.
   **
   ** @return                    <code>true</code> in case access token has
   **                            <code>scope</code> provided in input parameter;
   **                            <code>false</code> otherwise.
   */
  public boolean has(final String scope) {
    boolean status = false;
    if (this.scope != null) {
      for (String s : this.scope) {
        if (s.equals(scope)) {
          status = true;
          break;
        }
      }
    }
    return status;
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
    return Objects.hash(this.iss, this.sub, this.aud, this.exp, this.nbf, this.iat, this.jti);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>TokenInfo</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>TokenInfo</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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

    final TokenInfo that = (TokenInfo)other;
    return (this.iss != null ? this.iss.equals(that.iss) : that.iss != null)
        && (this.sub != null ? this.sub.equals(that.sub) : that.sub != null)
        && (this.aud != null ? this.aud.equals(that.aud) : that.aud != null)
        && (this.jti != null ? this.jti.equals(that.jti) : that.jti != null)
        && this.exp  == that.exp && this.nbf == that.nbf && this.iat == that.iat
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
    sb.append("iss:").append(this.iss).append("\n");
    sb.append("sub:").append(this.sub).append("\n");
    sb.append("aud:").append(this.aud).append("\n");
    sb.append("exp:").append(this.exp).append("\n");
    sb.append("nbf:").append(this.nbf).append("\n");
    sb.append("iat:").append(this.iat).append("\n");
    sb.append("jti:").append(this.jti).append("\n");
    sb.append("client:").append(this.client).append("\n");
    sb.append("scope:").append(this.scope).append("\n");
    return sb.toString();
  }
}