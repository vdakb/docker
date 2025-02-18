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

    File        :   Claim.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Claim.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwt;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Objects;
import java.util.Collections;
import java.util.LinkedHashMap;

import java.util.stream.Collectors;

import java.time.Instant;
import java.time.DateTimeException;

import javax.json.JsonValue;
import javax.json.JsonString;
import javax.json.JsonNumber;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.iam.platform.oauth.jwk.Payload;

////////////////////////////////////////////////////////////////////////////////
// class Claim
// ~~~~~ ~~~~~
/**
 ** JSON Web Token (JWT) claim set.
 ** <p>
 ** Supports all reserved claims of the JSON Web Token (JWT) specification:
 ** <ul>
 **   <li>exp - Expiration Time
 **   <li>nbf - Not Before
 **   <li>iat - Issued At
 **   <li>iss - Issuer
 **   <li>aud - Audience
 **   <li>prn - Principal
 **   <li>jti - JWT ID
 **   <li>typ - Type
 ** </ul>
 ** The set may also carry {@link #claim custom claims}; these will be
 ** serialized and parsed along the reserved ones.
 ** <p>
 ** Example JWT claims set:
 ** <pre>
 **  { "sub"                        : "joe"
 **  , "exp"                        : 1300819380
 **  , "http://example.com/is_root" : true
 **  }
 ** </pre>
 **<p>
 ** Example usage:
 ** <pre>
 **   final Claim set = Claim.builder()
 **     .subject("joe")
 **     .expirationTime(new Date(1300819380 * 1000l)
 **     .claim("http://example.com/is_root", true)
 **     .build();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Claim extends LinkedHashMap<String, Object> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4021112517018199223")
  private static final long        serialVersionUID = 792097637000082490L;

  private static final Set<String> RESERVED         = CollectionUtility.set(
    Tag.ISSUER.id
  , Tag.SUBJECT.id
  , Tag.AUDIENCE.id
  , Tag.EXPIRY.id
  , Tag.NOTBEFORE.id
  , Tag.ISSUED.id
  , Tag.ID.id
  , Tag.PRINCIPAL.id
  , Tag.ROLE.id
  );     

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The "iss" (Issuer) claim identifies the principal that issued the JWT.
   ** <br>
   ** The processing of this claim is generally application specific.
   ** <br>
   ** The "iss" value is a case-sensitive string containing a StringOrURI value.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1">RFC 7519 "iss" (Issuer) Claim</a>
   */
  public final String       issuer;
  /**
   ** The "sub" (Subject) claim identifies the principal that is the token
   ** issuing IdP subject. of the JWT.
   ** <br>
   ** The claims in a JWT are normally statements about the subject. The subject
   ** value <b>must</b> either be scoped to be locally unique in the context of
   ** the issuer or be globally unique.
   ** <P>
   ** The processing of this claim is generally application specific. The "sub"
   ** value is a case-sensitive string containing a StringOrURI value.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.2">RFC 7519 "sub" (Subject) Claim</a>
   */
  public final String       subject;
  /**
   ** The "aud" (Audience) claim identifies the recipients that the JWT is
   ** intended for.
   ** <br>
   ** Each principal intended to process the JWT <b>must</b> identify itself
   ** with a value in the audience claim.
   ** <br>
   ** If the principal processing the claim does not identify itself with a
   ** value in the "aud" claim when this claim is present, then the JWT
   ** <b>must</b> be rejected.
   ** <br>
   ** In the general case, the "aud" value is an array of case-sensitive
   ** strings, each containing a StringOrURI value.
   ** <br>
   ** In the special case when the JWT has one audience, the "aud" value
   ** <b>may</b> be a single case-sensitive string containing a StringOrURI value.
   ** <p>
   ** The interpretation of audience values is generally application specific.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.3">RFC 7519 "aud" (Audience) Claim</a>
   */
  public final List<String> audience;
  /**
   ** The "exp" (Expiration Time) claim identifies the expiration time on or
   ** after which the JWT <b>must not</b> be accepted for processing in seconds
   ** since 1970-01-01T00:00:00Z UTC.
   ** <br>
   ** The processing of the "exp" claim requires that the current date/time
   ** <b>must</b> be before the expiration date/time listed in the "exp" claim.
   ** <p>
   ** Implementers <b>may</b> provide for some small leeway, usually no more
   ** than a few minutes, to account for clock skew. Its value <b>must</b> be a
   ** number containing a NumericDate value.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4">RFC 7519 "exp" (Expiration Time) Claim</a>
   */
  public final Instant      expirationTime;
  /**
   ** The "nbf" (Not Before) claim identifies the time before which the JWT
   ** <b>must not</b> be accepted for processing in seconds since
   ** 1970-01-01T00:00:00Z UTC.
   ** <br>
   ** The processing of the "nbf" claim requires that the current date/time
   ** <b>must</b> be after or equal to the not-before date/time listed in the
   ** "nbf" claim.
   ** <p>
   ** Implementers <b>may</b> provide for some small leeway, usually no more
   ** than a few minutes, to account for clock skew. Its value <b>must</b> be a
   ** number containing a NumericDate value.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.5">RFC 7519 "nbf" (Not Before) Claim</a>
   */
  public final Instant      notBeforeTime;
  /**
   ** The "iat" (Issued At) claim identifies the time at which the JWT was
   ** issued in seconds since 1970-01-01T00:00:00Z UTC.
   ** <br>
   ** This claim can be used to determine the age of the JWT.
   ** <br>
   ** Its value <b>must</b> be a number containing a NumericDate value.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.6">RFC 7519 "iat" (Issued At) Claim</a>
   */
  public final Instant      issuedTime;
  /**
   ** The "jti" (JWT ID) claim provides a unique identifier for the JWT.
   ** <br>
   ** The identifier value <b>must</b> be assigned in a manner that ensures that
   ** there is a negligible probability that the same value will be accidentally
   ** assigned to a different data object; if the application uses multiple
   ** issuers, collisions <b>must</b> be prevented among values produced by
   ** different issuers as well.
   ** <p>
   ** The "jti" claim can be used to prevent the JWT from being replayed.
   ** <p>
   ** The "jti" value is a case-sensitive string.
   ** <p>
   ** Use of this claim is OPTIONAL.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.7">RFC 7519 "jti" (JWT ID) Claim</a>
   */
  public final String       tokenId;
  /**
   ** Microprofile specification JWT Auth:
   ** <br>
   ** A human readable claim that uniquely identifies the subject or user
   ** principal of the token, across the MicroProfile services the token will be
   ** accessed with.
   */
  public final String       principalName;
  /**
   ** Microprofile specification JWT Auth:
   ** <br>
   ** The token subject’s group memberships that will be mapped to Java EE
   ** style application level roles in the MicroProfile service container.
   */
  public final List<String> principalRole;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Tag
  // ~~~~ ~~~
  /**
   ** The OAuth reserved attribute constraints of a claim.
   */
  public enum Tag {
      /** The issuer claim. */
      ISSUER("iss", String.class)
      /** The principal claim. */
    , SUBJECT("sub", String.class)
      /** The audience claim. */
    , AUDIENCE("aud", String.class)
      /** The expiration time claim. */
    , EXPIRY("exp", Long.class)
      /** The not-before claim. */
    , NOTBEFORE("nbf", Long.class)
      /** The issued-at claim. */
    , ISSUED("iat", Long.class)
      /** The token ID claim. */
    , ID("jti", String.class)
      /** The principal claim. */
    , PRINCIPAL("upn", String.class)
      /** The group claim. */
    , ROLE("ugp", Set.class)
      /** The unknown claim. */
    , UNKNOWN("A catch all for any unknown claim", Void.class)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String   id;
    public final Class<?> type;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Tag</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in OAuth schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  type             the value type of the constraint name.
     **                          <br>
     **                          Allowed object is {@link Class} of type any.
     */
    Tag(final String value, final Class<?> type) {
      this.id   = value;
      this.type = type;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing claims.
   ** <p>
   ** Example usage:
   ** <pre>
   **   final Claim set = Claim.builder()
   **     .subject("joe")
   **     .expirationTime(new Date(1300819380 * 1000l)
   **     .claim("http://example.com/is_root", true)
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The "iss" (issuer) claim identifies the principal that issued the JWT.
     ** <br>
     ** The processing of this claim is generally application specific.
     ** <br>
     ** The "iss" value is a case-sensitive string containing a StringOrURI value.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1">RFC 7519 "iss" (Issuer) Claim</a>
     */
    private String                     issuer;
    /**
     ** The "sub" (subject) claim identifies the principal that is the subject of
     ** the JWT.
     ** <br>
     ** The claims in a JWT are normally statements about the subject. The subject
     ** value MUST either be scoped to be locally unique in the context of the
     ** issuer or be globally unique.
     ** <P>
     ** The processing of this claim is generally application specific. The "sub"
     ** value is a case-sensitive string containing a StringOrURI value.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.2">RFC 7519 "sub" (Subject) Claim</a>
     */
    private String                     subject;
    /**
     ** The "aud" (audience) claim identifies the recipients that the JWT is
     ** intended for.
     ** <br>
     ** Each principal intended to process the JWT MUST identify itself with a
     ** value in the audience claim.
     ** <br>
     ** If the principal processing the claim does not identify itself with a
     ** value in the "aud" claim when this claim is present, then the JWT MUST be
     ** rejected.
     ** <br>
     ** In the general case, the "aud" value is an array of case-sensitive
     ** strings, each containing a StringOrURI value.
     ** <br>
     ** In the special case when the JWT has one audience, the "aud" value MAY be
     ** a single case-sensitive string containing a StringOrURI value.
     ** <p>
     ** The interpretation of audience values is generally application specific.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.3">RFC 7519 "aud" (Audience) Claim</a>
     */
    private List<String>               audience;
    /**
     ** The "exp" (expiration time) claim identifies the expiration time on or
     ** after which the JWT MUST NOT be accepted for processing. The processing of
     ** the "exp" claim requires that the current date/time MUST be before the
     ** expiration date/time listed in the "exp" claim.
     ** <p>
     ** Implementers MAY provide for some small leeway, usually no more than a few
     ** minutes, to account for clock skew. Its value MUST be a number containing
     ** a NumericDate value.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4">RFC 7519 "exp" (Expiration Time) Claim</a>
     */
    private Instant                    expirationTime;
    /**
     ** The "nbf" (not before) claim identifies the time before which the JWT MUST
     ** NOT be accepted for processing.
     ** <br>
     ** The processing of the "nbf" claim requires that the current date/time MUST
     ** be after or equal to the not-before date/time listed in the "nbf" claim.
     ** <p>
     ** Implementers MAY provide for some small leeway, usually no more than a few
     ** minutes, to account for clock skew. Its value MUST be a number containing
     ** a NumericDate value.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.5">RFC 7519 "nbf" (Not Before) Claim</a>
     */
    private Instant                    notBeforeTime;
    /**
     ** The "iat" (issued at) claim identifies the time at which the JWT was
     ** issued.
     ** <br>
     ** This claim can be used to determine the age of the JWT.
     ** <br>
     ** Its value MUST be a number containing a NumericDate value.
     ** <p>
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.6">RFC 7519 "iat" (Issued At) Claim</a>
     */
    private Instant                    issuedTime;
    /**
     ** The "jti" (JWT ID) claim provides a unique identifier for the JWT.
     ** <br>
     ** The identifier value MUST be assigned in a manner that ensures that there
     ** is a negligible probability that the same value will be accidentally
     ** assigned to a different data object; if the application uses multiple
     ** issuers, collisions MUST be prevented among values produced by different
     ** issuers as well.
     ** <p>
     ** The "jti" claim can be used to prevent the JWT from being replayed.
     ** <p>
     ** The "jti" value is a case- sensitive string.
     ** Use of this claim is OPTIONAL.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.7">RFC 7519 "jti" (JWT ID) Claim</a>
     */
    private String                     tokenId;

    /**
     ** Microprofile specification JWT Auth:
     ** <br>
     ** A human readable claim that uniquely identifies the subject or user
     ** principal of the token, across the MicroProfile services the token will be
     ** accessed with.
     */
    private String                     principalName;

    /**
     ** Microprofile specification JWT Auth:
     ** <br>
     ** The token subject’s group memberships that will be mapped to Java EE
     ** style application level roles in the MicroProfile service container.
     */
    private List<String>              principalRole;

    private final Map<String, Object> claim = new LinkedHashMap<>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new {@link Cliam} <code>Builder</code>.
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: issuer
    /**
     ** Sets the issuer (<code>iss</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the issuer (<code>iss</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issuer(final String value) {
      this.issuer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: subject
    /**
     ** Sets the subject (<code>sub</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the principal (<code>prn</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder subject(final String value) {
      this.subject = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: audience
    /**
     ** Sets the audience (<code>aud</code>) property of the {@link Claim}
     ** <code>Builder</code>.
     **
     ** @param  value            the audience (<code>aud</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          each element is of type {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder audience(final List<String> value) {
      this.audience = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expirationTime
    /**
     ** Sets the expiration time (<code>exp</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the expiration time (<code>exp</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder expirationTime(final Long value) {
      return expirationTime(instant(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expirationTime
    /**
     ** Sets the expiration time (<code>exp</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the expiration time (<code>exp</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Date}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder expirationTime(final Date value) {
      return expirationTime(value.toInstant());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expirationTime
    /**
     ** Sets the expiration time (<code>exp</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the expiration time (<code>exp</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Instant}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder expirationTime(final Instant value) {
      this.expirationTime = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notBeforeTime
    /**
     ** Sets the not-before time (<code>nbf</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the not-before time (<code>nbf</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder notBeforeTime(final Long value) {
      return notBeforeTime(instant(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notBeforeTime
    /**
     ** Sets the not-before time (<code>nbf</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the not-before time (<code>nbf</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Date}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder notBeforeTime(final Date value) {
      return notBeforeTime(value.toInstant());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notBeforeTime
    /**
     ** Sets the not-before time (<code>nbf</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the not-before time (<code>nbf</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Instant}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder notBeforeTime(final Instant value) {
      this.notBeforeTime = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issuedTime
    /**
     ** Sets the issued-at time (<code>iat</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the issued-at time (<code>iat</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issuedTime(final Long value) {
      return issuedTime(instant(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issuedTime
    /**
     ** Sets the issued-at time (<code>iat</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the issued-at time (<code>iat</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Date}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issuedTime(final Date value) {
      return issuedTime(value.toInstant());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: issuedTime
    /**
     ** Sets the issued-at time (<code>iat</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the issued-at time (<code>iat</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link Instant}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder issuedTime(final Instant value) {
      this.issuedTime = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tokenId
    /**
     ** Sets the token identifier (<code>jti</code>) property of the
     ** {@link Claim} <code>Builder</code>.
     **
     ** @param  value            the type (<code>jti</code>) value or
     **                          <code>null</code> if not specified to set at
     **                          the {@link Claim} <code>Builder</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder tokenId(final String value) {
      this.tokenId = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  claim
    /**
     ** Adds a additional custom (non-reserved) claim of this {@link Claim}
     ** <code>Builder</code>.
     **
     ** @param  name             the name of the additional custom claim to add.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the additional custom claim to
     **                          add.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if the specified custom claim name
     **                                  matches a reserved claim name.
     */
    public final Builder claim(final String name, final Object value) {
      // prevent bogus input
      if (RESERVED.contains(name))
        throw new IllegalArgumentException("The claim name \"" + name + "\" matches a reserved name");

      if (value instanceof JsonValue) {
        switch (((JsonValue)value).getValueType()) {
          case NULL   : this.claim.put(name, null);
                        break;
          case TRUE   : this.claim.put(name, Boolean.TRUE);
                        break;
          case FALSE  : this.claim.put(name, Boolean.FALSE);
                        break;
          case STRING : this.claim.put(name, ((JsonString)value).getString());
                        break;
          case NUMBER : this.claim.put(name, ((JsonNumber)value).bigDecimalValue());
                        break;
          default     : this.claim.put(name, value.toString());
        }
      }
      else {
        this.claim.put(name, value);
      }
      return this;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a {@link Claim} setwith the properties
     ** configured.
     **
     ** @return                  the created {@link Claim} set populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is {@link Claim}.
		 */
    public final Claim build() {
      final Claim claim = new Claim(this.issuer, this.subject, this.audience, this.expirationTime, this.notBeforeTime, this.issuedTime, this.tokenId, this.principalName, this.principalRole);
      claim.putAll(this.claim);
      return claim;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web Token (JWT) <code>Claim</code> from the
   ** specified <code>Claim</code>.
   **
   ** @param  other              the <code>Claim</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>Claim</code>.
   */
  @SuppressWarnings("unchecked")
  private Claim(final Claim other) {
    // ensure inheritance
    this(other.issuer, other.subject, other.audience, other.expirationTime, other.notBeforeTime, other.issuedTime, other.tokenId, other.principalName, other.principalRole);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Claim</code> initiated from the property map.
   **
   ** @param  issuer             the issuer (<code>iss</code>) value or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  subject            the principal subject (<code>sub</code>) value
   **                            or <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  audience           the audience (<code>aud</code>) value
   **                            or <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  expirationTime     the expiration time (<code>exp</code>) value or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link Instant}.
   ** @param  notBeforeTime      the not-before time (<code>nbf</code>) value or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link Instant}.
   ** @param  issuedTime         the issued at (<code>iat</code>) value or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link Instant}.
   ** @param  tokenId            the identifier (<code>jti</code>) value or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Claim(final String issuer, final String subject, final List<String> audience, final Instant expirationTime, final Instant notBeforeTime, final Instant issuedTime, final String tokenId, final String principalName, final List<String> principalRole) {
    // ensure inheritance
    super();

    // initialize intance attributes
    this.issuer         = issuer;
    this.subject        = subject;
    this.audience       = audience;
    this.expirationTime = expirationTime;
    this.notBeforeTime  = notBeforeTime;
    this.issuedTime     = issuedTime;
    this.tokenId        = tokenId;
    this.principalName  = principalName;
    this.principalRole  = principalRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   claim
  /**
   ** Returns a additional custom (non-reserved) claim of this
   ** <code>Claim</code>.
   **
   ** @param  name               the name of the additional custom
   **                            (non-reserved) claim to return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the additional custom
   **                            (non-reserved) parameter mapped at
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public final Object claim(final String name) {
    final Object value = get(name);
    if (value instanceof JsonValue) {
      switch (((JsonValue)value).getValueType()) {
        case STRING  : return ((JsonString)value).getString();
        case NUMBER  : return ((JsonNumber)value).bigDecimalValue();
        case TRUE    : return Boolean.TRUE;
        case FALSE   : return Boolean.FALSE;
        default      : return value.toString();
      }
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Returns a JOSE object {@link Payload} representation of this
   ** <code>Claim</code> set.
	 **
   ** @return                    this <code>Claim</code> set converted to a
   **                            JOSE object {@link Payload} representation.
   **                            <br>
   **                            Possible object is {@link Payload}.
	 */
	public Payload payload() {
		return Payload.of(claim());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   claim
  /**
   ** Returns the <code>Claim</code> set mapping (reserverd/non-reserved) of
   ** this <code>Claim</code> set.
   ** <br>
   ** The returned mapping is non-invasive for this instance.
   **
   ** @return                    the {@link Map} representing the access token
   **                            claims.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   */
  final Map<String, ?> claim() {
    final Map<String, Object> tmp = new LinkedHashMap<>();
    tmp.put(Tag.ISSUER.id,    this.issuer);
    tmp.put(Tag.SUBJECT.id,   this.subject);
    tmp.put(Tag.AUDIENCE.id,  this.audience);
    tmp.put(Tag.EXPIRY.id,    this.expirationTime);
    tmp.put(Tag.NOTBEFORE.id, this.notBeforeTime);
    tmp.put(Tag.ISSUED.id,    this.issuedTime);
    tmp.put(Tag.ID.id,        this.tokenId);
    tmp.putAll(this);
    return tmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create and return a JSON Web Token (JWT)
   ** <code>Claim</code> set from the specified JSON object string
   ** representation.
	 **
   ** @param  value              the JSON object string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>Claim</code>.
	 *
	 * @return The JWT claims set.
	 *
   **
   ** @throws NullPointerException     if either <code>other</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object string
   **                                  doesn't represent a valid JWT claims set.
	 */
	public static Claim from(final String value) {
		return builder(JsonMarshaller.readObject(value)).build();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to create a <code>Claim</code> {@link Builder}.
   **
   ** @return                    <code>Claim</code> {@link Builder}
   **                            <br>
   **                            Possible object is {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to create and return a copy of a JSON Web Token (JWT)
   ** <code>Claim</code> of the specified <code>Claim</code>.
   **
   ** @param  other              the <code>Claim</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>Claim</code>.
   **
   ** @return                    the copy of the  <code>Claim</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>Claim</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static Claim builder(final Claim other) {
    return new Claim(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to create a OAuth <code>Claim</code>.
   **
   ** @param  origin             the {@link Map} representing the access token
   **                            properties.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   **
   ** @return                    the created create a OAuth <code>Claim</code>.
   **                            <br>
   **                            Possible object is <code>Claim</code>.
   */
  public static Builder builder(final Map<String, ?> origin) {
    final Builder builder = new Builder();
    builder.claim.putAll(origin.entrySet().stream().filter(e -> !RESERVED.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue())));
    // initialize instance attributes
    for (Tag cursor : Tag.values()) {
      final Object value = origin.get(cursor.id);
      if (value != null) {
        switch(cursor) {
          case ISSUER    : builder.issuer = string(value);
                           break;
          case SUBJECT   : builder.subject = string(value);
                           break;
          case AUDIENCE  : builder.audience = audience(value);
                           break;
          case EXPIRY    : builder.expirationTime = instant(value);
                           break;
          case NOTBEFORE : builder.notBeforeTime = instant(value);
                           break;
          case ISSUED    : builder.issuedTime = instant(value);
                           break;
          case ID    :     builder.tokenId = string(value);
                           break;
          case PRINCIPAL : builder.principalName = string(value);
                           break;
        }
      }
    }
    if (builder.principalName == null)
      builder.principalName = builder.subject;

    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instant
  /**
   ** Factory method to create an instance of {@link String} using seconds
   ** from the epoch of <code>1970-01-01T00:00:00Z</code>.
   ** <p>
   ** The nanosecond field is set to zero.
   **
   ** @param  value              the number of seconds from
   **                            <code>1970-01-01T00:00:00Z</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNumber}.
   **
   ** @return                    an {@link Instant} value, not
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Instant}.
   **
   ** @throws NullPointerException if either <code>value</code> is
   **                              <code>null</code>.
   ** @throws DateTimeException    if the instant exceeds the maximum or minimum
   **                              instant.
   */
  public static String string(final Object value) {
    if (value instanceof String)
      return (String)value;
    else if (value instanceof JsonString)
      return ((JsonString)value).getString();
    else
      throw new IllegalArgumentException("Unexpected type: " + value.getClass().getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instant
  /**
   ** Factory method to create an instance of {@link Instant} using seconds
   ** from the epoch of <code>1970-01-01T00:00:00Z</code>.
   ** <p>
   ** The nanosecond field is set to zero.
   **
   ** @param  value              the number of seconds from
   **                            <code>1970-01-01T00:00:00Z</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNumber}.
   **
   ** @return                    an {@link Instant} value, not
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Instant}.
   **
   ** @throws NullPointerException if either <code>value</code> is
   **                              <code>null</code>.
   ** @throws DateTimeException    if the instant exceeds the maximum or minimum
   **                              instant.
   */
  public static Instant instant(final Object value)
    throws DateTimeException {

    if (value instanceof Instant)
      return (Instant)value;
    else if (value instanceof JsonNumber)
      return instant((JsonNumber)value);
    else if (value instanceof Long)
      return instant((Long)value);
    else
      throw new IllegalArgumentException("Unexpected type: " + value.getClass().getName());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instant
  /**
   ** Factory method to create an instance of {@link Instant} using seconds
   ** from the epoch of <code>1970-01-01T00:00:00Z</code>.
   ** <p>
   ** The nanosecond field is set to zero.
   **
   ** @param  value              the number of seconds from
   **                            <code>1970-01-01T00:00:00Z</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNumber}.
   **
   ** @return                    an {@link Instant} value, not
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Instant}.
   **
   ** @throws NullPointerException if either <code>value</code> is
   **                              <code>null</code>.
   ** @throws DateTimeException    if the instant exceeds the maximum or minimum
   **                              instant.
   */
  public static Instant instant(final JsonNumber value)
    throws DateTimeException {

    return instant(Objects.requireNonNull(value, "The value must not be null").longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instant
  /**
   ** Factory method to create an instance of {@link Instant} using seconds
   ** from the epoch of <code>1970-01-01T00:00:00Z</code>.
   ** <p>
   ** The nanosecond field is set to zero.
   **
   ** @param  value              the number of seconds from
   **                            <code>1970-01-01T00:00:00Z</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an {@link Instant} value, not
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Instant}.
   **
   ** @throws NullPointerException if either <code>value</code> is
   **                              <code>null</code>.
   ** @throws DateTimeException    if the instant exceeds the maximum or minimum
   **                              instant.
   */
  public static Instant instant(final Date value)
    throws DateTimeException {

    return Objects.requireNonNull(value, "The value must not be null").toInstant();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instant
  /**
   ** Factory method to create an instance of {@link Instant} using seconds
   ** from the epoch of <code>1970-01-01T00:00:00Z</code>.
   ** <p>
   ** The nanosecond field is set to zero.
   **
   ** @param  value              the number of seconds from
   **                            <code>1970-01-01T00:00:00Z</code>.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an {@link Instant} value, not
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Instant}.
   **
   ** @throws NullPointerException if either <code>value</code> is
   **                              <code>null</code>.
   ** @throws DateTimeException    if the instant exceeds the maximum or minimum
   **                              instant.
   */
  public static Instant instant(final Long value)
    throws DateTimeException {

    return Instant.ofEpochSecond(Objects.requireNonNull(value, "The value must not be null").longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expired
  /**
   ** Determines the token has expired if the current time and issue time
   ** were both after the expiry time.
   **
   ** @return                    <code>true</code> if the Json Wed Token is
   **                            expired.
   **                            <br>
   **                            Possible object is <code>true</code>.
   */
  public final boolean expired() {
    return Instant.now().compareTo(this.expirationTime) > 0  && this.issuedTime.compareTo(this.expirationTime) < 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usedBefore
  /**
   ** Determines the token is used before the current time.
   **
   ** @return                    <code>true</code> if the Json Wed Token
   **                            is used before the current time.
   **                            <br>
   **                            Possible object is <code>true</code>.
   */
  public final boolean usedBefore() {
    return this.notBeforeTime != null && Instant.now().compareTo(this.notBeforeTime) < 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   audience
  /**
   ** Factory method to create a collection of audiences from the specified
   ** <code>value</code>.
   **
   ** @param  value              the value to transform.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    collection of audiences or <code>null</code> if
   **                            the given value couldn't be converted.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of {@link String}.
   */
  private static List<String> audience(final Object value) {
    if (value == null) {
      return Collections.emptyList();
    }
    else if (value instanceof String) {
      return Collections.singletonList((String)value);
    }
    else if (value instanceof List) {
      // determine the type of the elements
      final Object type = ((List)value).get(0);
      if (type == null) {
        return Collections.emptyList();
      }
      else if (type instanceof String) {
        return (List<String>)value;
      }
      else if (type instanceof JsonString) {
        return ((List<JsonString>)value).stream().map(e -> e.getString()).collect(Collectors.toList());
      }
      else
        return null;
    }
    else
      return null;
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
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.issuer, this.subject, this.audience, this.expirationTime, this.notBeforeTime, this.issuedTime, this.tokenId, this.principalName, this.principalRole, this.entrySet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Claim</code>s values are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Claim</code>s values may be different even though they contain the
   ** same set of names with the same values, but in a different order.
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

    if (!(other instanceof Claim))
      return false;

    if (!super.equals(other))
      return false;

    final Claim that = (Claim)other;
    return Objects.equals(this.issuer,         that.issuer)
        && Objects.equals(this.subject,        that.subject)
        && Objects.equals(this.audience,       that.audience)
        && Objects.equals(this.expirationTime, that.expirationTime)
        && Objects.equals(this.notBeforeTime,  that.notBeforeTime)
        && Objects.equals(this.issuedTime,     that.issuedTime)
        && Objects.equals(this.tokenId,        that.tokenId)
        && Objects.equals(this.principalName,  that.principalName)
        && Objects.equals(this.principalRole,  that.principalRole);
  }
}