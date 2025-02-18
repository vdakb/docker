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

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   SignatureHeader.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SignatureHeader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;

import java.net.URI;

import javax.json.JsonObject;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// final class SignatureHeader
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** JSON Web Signature (JWS) header.
 ** <p>
 ** Supports all {@link #RESERVED reserved header parameters} of the JWS
 ** specification:
 ** <ul>
 **   <li>alg
 **   <li>jku
 **   <li>jwk
 **   <li>x5u
 **   <li>x5t
 **   <li>x5c
 **   <li>kid
 **   <li>typ
 **   <li>cty
 ** </ul>
 ** The header may also carry {@link #parameter() custom parameters}; these will
 ** be serialized and parsed along the reserved ones.
 ** <p>
 ** Example:
 ** <pre>
 **   { "alg" : "HS256" }
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class SignatureHeader extends SecureHeader<SignatureHeader> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3521685308450983638")
  private static final long serialVersionUID = -519717502991819468L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing JSON Web Signature (JWS) headers.
   ** <br>
   ** This implementation provides the mutator methods to the JSON Web Key
   ** encryption header properties:
   ** <ul>
   **   <li>{@link #alg} (required)
   **   <li>{@link #typ} (required)
   **   <li>{@link #cty} (optional)
   **   <li>{@link #jku} (optional)
   **   <li>{@link #x5u} (optional)
   **   <li>{@link #x5t} (optional, deprecated)
   **   <li>{@link #x5t256} (optional)
   **   <li>{@link #x5c} (optional)
   **   <li>{@link #jwk} (optional)
   **   <li>{@link #kid} (optional)
   ** </ul>
   ** <p>
   ** Example usage:
   ** <pre>
   **   final SignatureHeader header = SignatureHeader.Builder.of(Algorithm.HS256)
   **     .contentType("text/plain")
   **     .parameter("exp", new Date().getTime())
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends SecureHeader.Builder<Builder, SignatureHeader> {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new {@link SignatureHeader} <code>Builder</code> for the
     ** {@link Algorithm} specified.
     **
     ** @param  algorithm        the algorithm type.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @throws NullPointerException if <code>algorithm</code> is
     **                              <code>null</code>.
     */
    private Builder(final Algorithm algorithm) {
      // ensure inheritance
      super(algorithm);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: parameter (overridden)
    /**
     ** Adds a additional custom (non-reserved) parameter of this
     ** <code>SignatureHeader</code>.
     **
     ** @param  name             the name of the additional custom parameter to
     **                          add.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the additional custom parameter to
     **                          add.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link SignatureHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if the specified parameter name matches
     **                                  a reserved parameter name.
     */
    @Override
    public Builder parameter(final String name, final String value) {
      return super.parameter(name, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (Header.Builder)
    /**
     ** Factory method to create a {@link SignatureHeader} with the properties
     ** configured.
     **
     ** @return                  the created {@link SignatureHeader} populated
     **                          with the properties configured.
     **                          <br>
     **                          Possible object is {@link SignatureHeader}.
     **
     ** @throws IllegalStateException if the JSON Web Key Header properties were
     **                               inconsistently specified.
     */
    @Override
    public final SignatureHeader build() {
      return new SignatureHeader(this.jku, this.x5u, this.x5t, this.x5t256, this.x5c, this.jwk, this.kid, this.alg, this.typ, this.cty, this.prm, this.b64);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link SignatureHeader} <code>Builder</code>
     ** for the {@link Algorithm algorithm}.
     **
     ** @param  algorithm        the signature {@link Algorithm}.
     **                          <br>
     **                          Must not be {@link Algorithm#NONE} or
     **                          <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @return                  the created @link EncryptionHeader}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException     if <code>algorithm</code> is
     **                                  <code>null</code>.
     ** @throws IllegalArgumentException if <code>algorithm</code> is
     **                                  {@link Algorithm#NONE}.
     */
    public static Builder of(final Algorithm algorithm)
      throws IllegalArgumentException {

      return new Builder(algorithm);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>SignatureHeader</code> from the specified
   ** <code>SignatureHeader</code>.
   **
   ** @param  other              the <code>SignatureHeader</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>SignatureHeader</code>.
   */
  private SignatureHeader(final SignatureHeader other) {
    this(other.jku, other.x5u, other.x5t, other.x5t256, other.x5c, other.jwk, other.kid, other.alg, other.typ, other.cty, other.prm, other.b64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>SignatureHeader</code> with the specified
   ** {@link Algorithm algorithm}.
   **
   ** @param  jku                the JSON Web Key (JWK) Set URI parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  x5u                the X.509 certificate URI parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  x5t                the X.509 SHA-1 certificate thumbprint
   **                            parameter, <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5t256             the X.509 SHA-256 certificate thumbprint
   **                            parameter, <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5c                the X.509 certificate chain parameter,
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type of {@link Encoded}.
   ** @param  jwk                the JSON Web Key (JWK) (<code>jwk</code>),
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   ** @param  kid                the JSON Web Key (JWK) (<code>kid</code>),
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  algorithm          the key {@link Algorithm}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Algorithm}.
   ** @param  type               the optional {@link Type type}
   **                            (<code>typ</code>) parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  contentType        the optional content type (<code>cty</code>)
   **                            parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the optional custom parameter parameter.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} for the value.
   ** @param  parsed             the optional parsed {@link EncodedURL}.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  private SignatureHeader(final URI jku, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final JsonWebKey jwk, final String kid, final Algorithm algorithm, final Type type, final String contentType, final Map<String, String> parameter, final EncodedURL parsed) {
    // ensure inheritance
    super(jku, x5u, x5t, x5t256, x5c, jwk, kid, algorithm, type, contentType, parameter, parsed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   included (Header)
  /**
   ** Returns the names of all included parameters (reserved and custom) in the
   ** header.
   **
   ** @return                    the included parameters.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  public Set<String> included() {
    final Set<String> parameter = new HashSet<String>(this.prm.keySet());
    parameter.add(ALG);

    if (this.typ != null)
      parameter.add(TYP);

    if (this.cty != null)
      parameter.add(CTY);

    if (this.jku != null)
      parameter.add(JKU);

    if (this.jwk != null)
      parameter.add(JWK);

    if (this.x5u != null)
      parameter.add(X5U);

    if (this.x5t != null)
      parameter.add(X5T);

    if (this.x5t256 != null)
      parameter.add(X5TS256);

    if (this.x5c != null)
      parameter.add(X5C);

    if (this.kid != null)
      parameter.add(KID);

    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Signature (JWS) header from the specified
   ** {@link EncodedURL} value.
   **
   ** @param  value              the {@link EncodedURL} value to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>SignatureHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWS header.
   */
  public static SignatureHeader from(final EncodedURL value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(Objects.requireNonNull(value, "The signature value must not be null").decodeString(), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Parses a JSON Web Signature (JWS) header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>SignatureHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWS header.
   */
  public static SignatureHeader from(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(value, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Parses a JSON Web Signature (JWS) header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>SignatureHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>SignatureHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWS header.
   */
  public static SignatureHeader from(final String value, final EncodedURL origin)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(JsonMarshaller.readObject(Objects.requireNonNull(value, "The signature header string must not be null")), origin);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Signature (JWS) header from the specified JSON object.
   **
   ** @param  object             the JSON object representation of the JSON Web
   **                            Signature (JWS) header to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>SignatureHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>SignatureHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureHeader</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object doesn't
   **                                  represent a valid JWS header.
   */
  public static SignatureHeader from(final JsonObject object, final EncodedURL origin)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(object, "The Json Object must not be null");

    // create the header in its minmal form
    final Builder b = Builder.of(Header.algorithm(object)).origin(origin);
    // parse optional + custom parameters
    for (String name : object.keySet()) {
      // skip
      if (name.equals(ALG))
        continue;
      else if (name.equals(TYP))
        b.type(Type.from(object.getString(name)));
      else if (name.equals(CTY))
        b.contentType(object.getString(name));
      else if (name.equals(JKU))
        b.jku(certificateURI(object, name));
      else if (name.equals(JWK))
        b.jwk(JsonWebKey.from(object.getJsonObject(name)));
      else if (name.equals(X5U))
        b.x5u(certificateURI(object, name));
      else if (name.equals(X5T))
        // assume the object containes the encoded from of the data
        b.x5t(EncodedURL.raw(object.getString(name)));
      else if (name.equals(X5C))
        b.x5c(SecureHeader.certificateChain(object, name));
      else if (name.equals(KID))
        b.kid(object.getString(name));
      else
        b.parameter(name, object.getString(name));
    }
    return b.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create and return a copy of the
   ** <code>SignatureHeader</code> <code>other</code>.
   **
   ** @param  other              the <code>SignatureHeader</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            <code>SignatureHeader</code>.
   **
   ** @return                    the copy of the  <code>SignatureHeader</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureHeader</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static SignatureHeader of(final SignatureHeader other) {
    return new SignatureHeader(Objects.requireNonNull(other));
  }
}