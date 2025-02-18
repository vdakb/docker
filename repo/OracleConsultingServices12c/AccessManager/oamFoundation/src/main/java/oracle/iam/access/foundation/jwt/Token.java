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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Token.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Token.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

////////////////////////////////////////////////////////////////////////////////
// class Token
// ~~~~~ ~~~~~
/**
 ** JSON Web Token (JWT) as defined by RFC 7519.
 ** <pre>
 **   From RFC 7519 Section 1. Introduction:
 **     The suggested pronunciation of JSON Web Token is the same as the English
 **     word "jot".
 ** </pre>
 ** The <code>Token</code> is not Thread-Safe and should not be re-used.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Token {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Registered Claim <code>aud</code> as defined by RFC 7519 Section 4.1.3.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The audience claim identifies the recipients that the JSON Web Token is
   ** intended for. This may be an array of strings or a single string, in
   ** either case if the string value contains a <code>:</code> it must be a
   ** URI.
   */
  @JsonProperty("aud")
  protected Object audience;

  /**
   ** Registered Claim <code>iss</code> as defined by RFC 7519 Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The issuer claim identifies the principal that issued the JSON Web Token.
   ** If the value contains a <code>:</code> it must be a URI.
   */
  @JsonProperty("iss")
  protected String issuer;

  /**
   ** Registered Claim <code>iat</code> as defined by RFC 7519 Section 4.1.6.
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
   */
  @JsonProperty("iat")
  protected ZonedDateTime issuedAt;

  /**
   ** Registered Claim <code>exp</code>  as defined by RFC 7519 Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The expiration time claim identifies the expiration time on or after which
   ** the JSON Web Token <b>must</b> NOT be accepted for processing. The expiration
   ** time is expected to provided in UNIX time, or the number of seconds since
   ** Epoch.
   ** <p>
   ** The timestamp when the token stops being valid, specified as seconds since
   ** 00:00:00 UTC, January 1, 1970. The maximum lifetime of a token is 24 hours
   ** + skew.
   */
  @JsonProperty("exp")
  protected ZonedDateTime expiration;

  /**
   ** Registered Claim <code>nbf</code> as defined by RFC 7519 Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** This claim identifies the time before which the JSON Web Token <b>must</b> NOT be
   ** accepted for processing. The not before value is expected to provided in
   ** UNIX time, or the number of seconds since Epoch.
   */
  @JsonProperty("nbf")
  protected ZonedDateTime notBefore;

  /**
   ** Registered Claim <code>sub</code> as defined by RFC 7519 Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The subject claim identifies the principal that is the subject of the JSON
   ** Web Token. If the value contains a <code>:</code> it must be a URI.
   */
  @JsonProperty("sub")
  protected String subject;

  /**
   ** Registered Claim <code>jti</code> as defined by RFC 7519 Section 4.1.6.
   ** <br>
   ** Use of this claim is OPTIONAL.
   ** <p>
   ** The JSON Web Token unique ID claim provides a unique identifier for the
   ** JSON Web Token.
   */
  @JsonProperty("jti")
  protected String uniqueId;

  /**
   ** This Map will contain all the claims that aren't specifically defined in
   ** the specification. These still might be IANA registered claims, but are
   ** not known JSON Web Token specification claims.
   */
  protected Map<String, Object> other = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Token</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Token() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   audience
  /**
   ** Sets the for claim <code>aud</code>.
   **
   ** @param  value              the value to set for claim <code>aud</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token audience(final Object value) {
    this.audience = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   issuer
  /**
   ** Sets the for claim <code>iss</code>.
   **
   ** @param  value              the value to set for claim <code>iss</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token issuer(final String value) {
    this.issuer = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   issuedAt
  /**
   ** Sets the for claim <code>iat</code>.
   **
   ** @param  value              the value to set for claim <code>iat</code>.
   **                            Allowed object is {@link Object}.
    **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
 public Token issuedAt(final ZonedDateTime value) {
    this.issuedAt = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   expiration
  /**
   ** Sets the for claim <code>exp</code>.
   **
   ** @param  value              the value to set for claim <code>exp</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token expiration(final ZonedDateTime value) {
    this.expiration = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   expired
  /**
   ** Return <code>true</code> if this JSON Web <code>Token</code> is expired.
   **
   ** @return                    <code>true</code> if expired; otherwise
   **                            <code>false</code>.
   */
  @JsonIgnore
  public boolean expired() {
    return expiration != null && expiration.isBefore(ZonedDateTime.now(ZoneOffset.UTC));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   isUnavailableForProcessing
  /**
   ** Return <code>true</code> if this JSON Web <code>Token</code> is
   ** unavailable for processing.
   **
   ** @return                    <code>true</code> if unavailable; otherwise
   **                            <code>false</code>.
   */
  public boolean isUnavailableForProcessing() {
    return notBefore != null && notBefore.isAfter(ZonedDateTime.now(ZoneOffset.UTC));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   notBefore
  /**
   ** Sets the for claim <code>nbf</code>.
   **
   ** @param  value              the value to set for claim <code>nbf</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token notBefore(final ZonedDateTime value) {
    this.notBefore = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   subject
  /**
   ** Sets the for claim <code>sub</code>.
   **
   ** @param  value              the value to set for claim <code>sub</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token subject(final String value) {
    this.subject = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   uniqueId
  /**
   ** Sets the for claim <code>jti</code>.
   **
   ** @param  value              the value to set for claim <code>jti</code>.
   **                            Allowed object is {@link Object}.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token uniqueId(final String value) {
    this.uniqueId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   raw
  /**
   ** Returns the original claims from the JSON Web <code>Token</code> without
   ** any Java data types like {@link ZonedDateTime}.
   ** <br>
   ** This will contain the otherClaims and the known JSON Web <code>Token</code>
   ** claims.
   **
   ** @return                    all the claims unconverted.
   */
  @JsonIgnore
  public Map<String, Object> raw() {
    final Map<String, Object> raw = new HashMap<>(this.other);
    if (audience != null) {
      raw.put("aud", audience);
    }
    if (expiration != null) {
      raw.put("exp", expiration.toEpochSecond());
    }
    if (issuedAt != null) {
      raw.put("iat", issuedAt.toEpochSecond());
    }
    if (issuer != null) {
      raw.put("iss", issuer);
    }
    if (notBefore != null) {
      raw.put("nbf", notBefore.toEpochSecond());
    }
    if (subject != null) {
      raw.put("sub", subject);
    }
    if (uniqueId != null) {
      raw.put("jti", uniqueId);
    }
    return raw;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   all
  /**
   ** Returns all the claims as Java types like {@link ZonedDateTime} (where
   ** appropriate of course).
   ** <br>
   ** This will contain the otherClaims and the known JSON Web
   ** <code>Token</code> claims.
   **
   ** @return                    all the claims as Java types.
   */
  @JsonIgnore
  public Map<String, Object> all() {
    final Map<String, Object> raw = new HashMap<>(this.other);
    if (this.audience != null) {
      raw.put("aud", this.audience);
    }
    if (this.expiration != null) {
      raw.put("exp", this.expiration);
    }
    if (this.issuedAt != null) {
      raw.put("iat", this.issuedAt);
    }

    if (this.issuer != null) {
      raw.put("iss", this.issuer);
    }

    if (this.notBefore != null) {
      raw.put("nbf", this.notBefore);
    }

    if (this.subject != null) {
      raw.put("sub", this.subject);
    }

    if (this.uniqueId != null) {
      raw.put("jti", this.uniqueId);
    }
    return raw;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>Boolean</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>Boolean</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>Boolean</code>.
   */
  public Boolean booleanValue(final String claim) {
    return (Boolean)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a <code>String</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>String</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringValue(final String claim) {
    return (String)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>Integer</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>Integer</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigInteger</code>.
   */
  public Integer integerValue(final String claim) {
    final BigInteger value = bigintegerValue(claim);
    return (value == null) ? null : value.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>Long</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>Long</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigInteger</code>.
   */
  public Long longValue(final String claim) {
    final BigInteger value = bigintegerValue(claim);
    return (value == null) ? null : value.longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   floatValue
  /**
   ** Returns a <code>Float</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>Float</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigDecimal</code>.
   */
  public Float floatValue(final String claim) {
    final BigDecimal value = bigdecimalValue(claim);
    return (value == null) ? null : value.floatValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Returns a <code>Double</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>Double</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigDecimal</code>.
   */
  public Double doubleValue(final String claim) {
    final BigDecimal value = bigdecimalValue(claim);
    return (value == null) ? null : value.doubleValue();
  }

  public Number getNumberValue(final String claim) {
    return (Number)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigintegerValue
  /**
   ** Returns a <code>BigInteger</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>BigInteger</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigInteger</code>.
   */
  public BigInteger bigintegerValue(final String claim) {
    return (BigInteger)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bigdecimalValue
  /**
   ** Returns a <code>BigDecimal</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired value.
   **
   ** @return                    the <code>BigDecimal</code> for the given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>BigDecimal</code>.
   */
  public BigDecimal bigdecimalValue(final String claim) {
    return (BigDecimal)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listValue
  /**
   ** Returns a {@link List} <code>Object</code>s from the claim mapping of this
   ** JSON Web <code>Token</code>
   **
   ** @param  claim              the name of the {@link List} <code>Object</code>s.
   **
   ** @return                    the {@link List} <code>Object</code>s for the
   **                            given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a {@link List}.
   */
  public List<Object> listValue(final String claim) {
    return (List<Object>)this.other.get(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mapValue
  /**
   ** Returns a {@link Map} <code>Object</code>s from the claim mapping of this
   ** JSON Web <code>Token</code>
   **
   ** @param  claim              the name of the {@link Map} <code>Object</code>s.
   **
   ** @return                    the {@link Map} <code>Object</code>s for the
   **                            given claim.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a {@link Map}.
   */
  public Map<String, Object> mapValue(final String claim) {
    return (Map<String, Object>)lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectValue
  /**
   ** Returns a <code>Object</code> from the claim mapping of this JSON Web
   ** <code>Token</code>
   **
   ** @param  claim              the name of the desired <code>Object</code>s.
   **
   ** @return                    the <code>Object</code> for the given claim.
   */
  public Object objectValue(final String claim) {
    return lookup(claim);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   other
  /**
   ** Sets the claims that aren't specifically defined in the specification.
   ** These still might be IANA registered claims, but are not known JSON Web
   ** Token specification claims.
   **
   ** @param  name               the name of the JSON Web <code>Token</code>
   **                            claim.
   ** @param  value              the value of the JSON Web <code>Token</code>
   **                            claim. This value is an object and is
   **                            expected to properly serialize.
   **
   ** @return                    the previous claim associated with
   **                            <code>name</code>, or <code>null</code> if
   **                            there was no mapping for <code>name</code>.
   */
   @JsonAnySetter
  public Object other(final String name, final Object value) {
    return this.other.put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   other
  /**
   ** Returns the claims that aren't specifically defined in the specification.
   ** These still might be IANA registered claims, but are not known JSON Web
   ** Token specification claims.
   **
   ** @return                    the claims that aren't specifically defined in
   **                            the specification.
   */
  @JsonAnyGetter
  public Map<String, Object> other() {
    return this.other;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decoder
  /**
   ** Factory method to return a singleton instance of the JSON Web
   ** <code>Token</code> {@link Decoder}.
   **
   ** @return                   a JSON Web <code>Token</code> {@link Decoder}.
   */
  public static Decoder decoder() {
    return Decoder.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encoder
  /**
   ** Factory method to return a singleton instance of the JSON Web
   ** <code>Token</code> {@link Encoder}.
   **
   ** @return                   a JSON Web <code>Token</code> {@link Encoder}.
   */
  public static Encoder encoder() {
    return Encoder.instance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.audience, this.issuer, this.issuedAt, this.expiration, this.notBefore, this.subject, this.uniqueId, this.other);
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>User</code> object that represents
   ** the same <code>name</code>.
   **
   ** @param other             the object to compare this <code>User</code>
   **                          with.
   **
   ** @return                  <code>true</code> if the <code>User</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;

    final Token token = (Token)other;
    return Objects.equals(audience, token.audience) && Objects.equals(this.other, token.other) && Objects.equals(this.expiration, token.expiration) && Objects.equals(this.issuedAt, token.issuedAt) && Objects.equals(this.issuer, token.issuer) && Objects.equals(this.notBefore, token.notBefore) && Objects.equals(this.subject, token.subject) && Objects.equals(this.uniqueId, token.uniqueId);
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method:   addClaim
  /**
   ** Add a claim to this JSON Web <code>Token</code>.
   ** <br>
   ** This claim can be public or private, it is up to the caller to properly
   ** name the claim as to avoid collision.
   **
   ** @param  name               the name of the JSON Web <code>Token</code>
   **                            claim.
   ** @param  value              the value of the JSON Web <code>Token</code>
   **                            claim. This value is an object and is
   **                            expected to properly serialize.
   **
   ** @return                    this <code>Token</code> for chaining
   **                            invocations.
   */
  public Token addClaim(final String name, Object value) {
    switch (name) {
      case "aud" : this.audience = value;
                   break;
      case "exp" : this.expiration = toZonedDateTime("exp", value);
                   break;
      case "iat" : this.issuedAt = toZonedDateTime("iat", value);
                   break;
      case "iss" : this.issuer = (String)value;
                   break;
      case "jti" : this.uniqueId = (String)value;
                   break;
      case "nbf" : this.notBefore = toZonedDateTime("nbf", value);
                   break;
      case "sub" : this.subject = (String)value;
                   break;
      default    : if (value instanceof Double || value instanceof Float) {
                     value = BigDecimal.valueOf(((Number)value).doubleValue());
                   }
                   else if (value instanceof Integer || value instanceof Long) {
                     value = BigInteger.valueOf(((Number)value).longValue());
                   }
                   this.other.put(name, value);
                   break;
    }
    return this;
  }

  private Object lookup(final String claim) {
    switch (claim) {
      case "aud" : return this.audience;
      case "iss" : return this.issuer;
      case "iat" : return this.issuedAt;
      case "exp" : return this.expiration;
      case "nbf" : return this.notBefore;
      case "sub" : return this.subject;
      case "jti" : return this.uniqueId;
      default    : return this.other.get(claim);
    }
  }

  private ZonedDateTime toZonedDateTime(final String claim, final Object value) {
    if (value instanceof ZonedDateTime) {
      return (ZonedDateTime) value;
    }
    else if (value instanceof Number) {
      return Instant.ofEpochSecond(((Number) value).longValue()).atZone(ZoneOffset.UTC);
    }
    else {
      throw new IllegalArgumentException("Invalid numeric value for [" + claim + "] claim");
    }
  }
}