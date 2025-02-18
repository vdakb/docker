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

    File        :   EncryptionHeader.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EncryptionHeader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.HashSet;

import java.net.URI;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// final class EncryptionHeader
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** JSON Web Encryption (JWE) header.
 ** <p>
 ** Supports all {@link #RESERVED reserved header parameters} of the JWE
 ** specification:
 ** <ul>
 **   <li>alg
 **   <li>enc
 **   <li>epk
 **   <li>zip
 **   <li>jku
 **   <li>jwk
 **   <li>x5u
 **   <li>x5t
 **   <li>x5t#S256
 **   <li>x5c
 **   <li>kid
 **   <li>typ
 **   <li>cty
 **   <li>apu
 **   <li>apv
 **   <li>p2s
 **   <li>p2c
 **   <li>iv
 **   <li>skid
 ** </ul>
 ** The header may also carry {@link #parameter() custom parameters}; these will
 ** be serialized and parsed along the reserved ones.
 ** <p>
 ** Example:
 ** <pre>
 **   { "alg" : "RSA1_5"
 **   , "enc" : "A128CBC+HS256"
 **   }
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class EncryptionHeader extends SecureHeader<EncryptionHeader> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The ephemeral public key (<code>epk</code>) parameter used in conjunction
   ** with ECDH key agreement.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.6.1.1">RFC 7518 "epk" (Ephemeral Public Key) Header Parameter</a>
   */
  public static final String         EPK                = "epk";

  /**
   ** The Agreement PartyUInfo (<code>apu</code>) parameter used in conjunction
   ** with ECDH key agreement.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.6.1.2">RFC 7518 "apu" (Agreement PartyUInfo) Header Parameter</a>
   */
  public static final String         APU               = "apu";

  /**
   ** The Agreement PartyVInfo (<code>apv</code>) used in conjunction
   ** with ECDH key agreement.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.6.1.3">RFC 7518 "apv" (Agreement PartyVInfo) Header Parameter</a>
   */
  public static final String         APV              = "apv";

  /**
   ** The PBES2 salt (<code>p2s</code>) parameter used in conjunction
   ** with PBES2 key encryption.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.8.1.1">RFC 7518 "p2s" (PBES2 Salt Input) Header Parameter</a>
   */
  public static final String         P2S              = "p2s";

  /**
   ** The PBES2 count (<code>p2c</code>) parameter used in conjunction
   ** with PBES2 key encryption.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.8.1.2">RFC 7518 "p2c" (PBES2 Count) Header Parameter</a>
   */
  public static final String         P2C             = "p2c";

  /**
   ** The initialization vector (<code>iv</code>) parameter, used in conjunction
   ** with AES GCN key encryption.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-4.7.1.1">RFC 7518 "iv" (Initialization Vector) Header Parameter</a>
   */
  public static final String        I4V             = "iv";

  /**
   ** The sender identifier (<code>iv</code>) parameter, used in conjunction
   ** with ECDH-1PU key agreement.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/draft-madden-jose-ecdh-1pu-04#section-2.2.1">"skid" Header Parameter</a>
   */
  public static final String        SID             = "skid";

  /** The reserved parameter names. */
  private static final Set<String> RESERVED         = CollectionUtility.unmodifiableSet(ENC, EPK, ZIP, APU, APV, P2S, P2C, I4V, SID);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7654054596745863571")
  private static final long        serialVersionUID = -7720650986396509571L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The encryption method (<code>enc</code>). */
  public final Algorithm   enc;

  /** The ephemeral public key (<code>epk</code>). */
  public final JsonWebKey  epk;

  /** The compression algorithm (<code>zip</code>). */
  public final Compression zip;

  /** The agreement PartyUInfo (<code>apu</code>). */
  public final EncodedURL  apu;

  /** The agreement PartyVInfo (<code>apv</code>). */
  public final EncodedURL  apv;

  /** The the PBES2 salt (<code>p2s</code>). */
  public final EncodedURL  p2s;

  /** The the PBES2 count (<code>p2c</code>). */
  public final Integer     p2c;

  /** The initialization vector (<code>iv</code>). */
  public final EncodedURL  i4v;

  /** The Sender key ID (<code>skid</code>). */
  public final String      sid;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing JSON Web Encryption (JWE) headers.
   ** <br>
   ** This implementation provides the mutator methods to the JSON Web Key
   ** encryption header properties:
   ** <ul>
   **   <li>{@link #alg} (required)
   **   <li>{@link #typ} (required)
   **   <li>{@link #cty} (optional)
   **   <li>{@link #enc} (required)
   **   <li>{@link #epk} (optional)
   **   <li>{@link #zip} (optional)
   **   <li>{@link #apu} (optional)
   **   <li>{@link #apv} (optional)
   **   <li>{@link #p2s} (optional)
   **   <li>{@link #p2c} (optional)
   **   <li>{@link #iv} (optional)
   **   <li>{@link #skid} (optional)
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
   **   final EncyptionHeader header = EncyptionHeader.Builder.of(Algorithm.RSA1_5, EncryptionMethod.A128GCM)
   **     .contentType("text/plain")
   **     .parameter("exp", new Date().getTime())
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends SecureHeader.Builder<Builder, EncryptionHeader> {

    /** The encryption method (<code>enc<&code>). */
    private Algorithm   enc;

    /** The ephemeral public key (<code>epk</code>). */
    private JsonWebKey  epk;

    /** The compression algorithm (<code>zip</code>). */
    private Compression zip;

    /** The agreement PartyUInfo (<code>apu</code>). */
    private EncodedURL  apu;

    /** The agreement PartyVInfo (<code>apv</code>). */
    private EncodedURL  apv;

    /** The PBES2 salt (<code>p2s</code>). */
    private EncodedURL  p2s;

    /** The PBES2 count (<code>p2c</code>). */
    private Integer     p2c;

    /** The initialization vector (<code>iv</code>) parameter. */
    private EncodedURL  iv;

    /** The the sender key ID (<code>skid</code>). */
    private String      skid;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new {@link EncryptionHeader} <code>Builder</code> for the
     ** {@link Algorithm} and {@link EncpryptionMethod} specified.
     **
     ** @param  algorithm        the algorithm type.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     ** @param  method           the encryption method {@link Algorithm}.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @throws NullPointerException if either <code>algorithm</code> or
     **                              <code>method</code> is <code>null</code>.
     */
    private Builder(final Algorithm algorithm, final Algorithm method) {
      // ensure inheritance
      super(algorithm);

      // initialize instance attributes
      this.enc = Objects.requireNonNull(method,"The encryption method \"enc\" parameter must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: ephemeralPublicKey
    /**
     ** Sets the Ephemeral Public Key (<code>epk</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the Ephemeral Public Key (<code>epk</code>)
     **                          property of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link JsonWebKey}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     **
     ** @throws IllegalArgumentException if <code>epk</code> is a private key
     **                                  spec.
     */
    public Builder ephemeralPublicKey(final JsonWebKey value) {
      // prevent bogus input
      if (value != null && value.sensitive())
        throw new IllegalArgumentException("Ephemeral public key should not be a private key");

      this.epk =value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: agreementPartyVInfo
    /**
     ** Sets the agreement PartyVInfo (<code>apv</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the agreement PartyVInfo (<code>apv</code>)
     **                          property of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder agreementPartyVInfo(final EncodedURL value) {
      this.apv = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: compression
    /**
     ** Sets the compression algorithm (<code>zip</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the compression algorithm (<code>zip</code>)
     **                          property of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link Compression}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder compression(final Compression value) {
      this.zip = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: agreementPartyUInfo
    /**
     ** Sets the agreement PartyUInfo (<code>apu</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the agreement PartyUInfo (<code>apu</code>)
     **                          property of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder agreementPartyUInfo(final EncodedURL value) {
      this.apu = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pbes2Salt
    /**
     ** Sets the PBES2 salt (<code>p2s</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the PBES2 salt (<code>p2s</code>) property of
     **                          the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder pbes2Salt(final EncodedURL value) {
      this.p2s = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: pbes2Count
    /**
     ** Sets the PBES2 count (<code>p2c</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the PBES2 count (<code>p2c</code>) property of
     **                          the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link Integer}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     **
     ** @throws IllegalArgumentException if the specfied <code>value</code> is
     **                                  less than zero.
     */
    public Builder pbes2Count(final Integer value)
      throws IllegalArgumentException {

      // prevent bogus input
      if (value < 0)
        throw new IllegalArgumentException("The PBES2 count parameter must not be negative");

      this.p2c = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: iv
    /**
     ** Sets the initialization vector (<code>iv</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the initialization vector (<code>iv</code>)
     **                          property of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder iv(final EncodedURL value) {
      this.iv = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: senderId
    /**
     ** Sets the sender key ID (<code>skid</code>) property of the
     ** <code>EncryptionHeader</code> <code>Builder</code>.
     **
     ** @param  value            the sender key ID (<code>skid</code>) property
     **                          of the {@link EncryptionHeader}
     **                          <code>Builder</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    public Builder senderId(final String value) {
      this.skid = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: parameter (overridden)
    /**
     ** Adds a additional custom (non-reserved) parameter of this
     ** <code>EncryptionHeader</code>.
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
     ** @return                  the {@link EncryptionHeader}
     **                          <code>Builder</code> to allow method chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if the specified parameter name matches
     **                                  a reserved parameter name.
     */
    @Override
    public Builder parameter(final String name, final String value) {
      // prevent bogus input
      if (RESERVED.contains(name))
        throw new IllegalArgumentException("The parameter name \"" + name + "\" matches a reserved name");

      return super.parameter(name, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (Header.Builder)
    /**
     ** Factory method to create a {@link EncryptionHeader} with the properties
     ** configured.
     **
     ** @return                  the created {@link EncryptionHeader} populated
     **                          with the properties configured.
     **                          <br>
     **                          Possible object is {@link EncryptionHeader}.
     **
     ** @throws IllegalStateException if the JSON Web Key Header properties were
     **                               inconsistently specified.
     */
    @Override
    public final EncryptionHeader build() {
      return new EncryptionHeader(this.enc, this.epk, this.zip, this.apu, this.apv, this.p2s, this.p2c, this.iv, this.skid, this.jku, this.x5u, this.x5t, this.x5t256, this.x5c, this.jwk, this.kid, this.alg, this.typ, this.cty, this.prm, this.b64);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link EncryptionHeader} <code>Builder</code>
     ** for the {@link Algorithm algorithm} and {@link Algorithm method}
     ** specified.
     **
     ** @param  algorithm        the key encryption {@link Algorithm}
     **                          parameter.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     ** @param  method           the encryption method {@link Algorithm}
     **                          parameter.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @return                  the created @link EncryptionHeader}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>algorithm</code> or
     **                              <code>method</code> is <code>null</code>.
     */
    public static Builder of(final Algorithm algorithm, final Algorithm method)
      throws IllegalArgumentException {

      return new Builder(algorithm, method);
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum  Compression
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Represents the <code>zip</code> header parameter in JSON Web Encryption
   ** (JWE) objects.
   ** <p>
   ** Includes a constant for the standard DEFLATE compression algorithm:
   ** <ul>
   **   <li>{@link #DEF}
   ** </ul>
   */
  public static enum Compression {

    /**
     ** DEFLATE Compressed Data Format Specification version 1.3, as described
     ** in RFC 1951.
     */
    DEF("DEF");

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The compression algorithm. */
    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new compression algorithm with the specified name.
     **
     ** @param  value            the type value of the compression algorithm.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Compression(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value property of the <code>Compression</code>.
     **
     ** @return                  the value property of the
     **                          <code>Compression</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Compression</code> constraint
     ** from the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Compression</code> constraint.
     **                          <br>
     **                          Possible object is <code>Compression</code>.
     */
    public static Compression from(final String value) {
      for (Compression cursor : Compression.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns the string representation for the <code>Compression</code> in
     ** its minimal form, without any additional whitespace.
     **
     ** @return                  a string representation that represents this
     **                          <code>Compression</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String toString() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>EncryptionHeader</code> from the specified
   ** <code>EncryptionHeader</code>.
   **
   ** @param  other              the <code>EncryptionHeader</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            <code>EncryptionHeader</code>.
   */
  private EncryptionHeader(final EncryptionHeader other) {
    this(other.enc, other.epk, other.zip, other.apu, other.apv, other.p2s, other.p2c, other.i4v, other.sid, other.jku, other.x5u, other.x5t, other.x5t256, other.x5c, other.jwk, other.kid, other.alg, other.typ, other.cty, other.prm, other.b64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>EncryptionHeader</code> with the specified key
   ** {@link Encryption} and {@link EncryptionMethod}.
   **
   ** @param  enc                the encryption method parameter.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Algorithm}.
   ** @param  epk                the Ephemeral Public Key (<code>epk</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link JsonWebKey}.
   ** @param  zip                the compression algorithm (<code>zip</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link Compression}.
   ** @param  apu                the agreement PartyUInfo (<code>apu</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  apv                the agreement PartyVInfo (<code>apv</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  p2s                the PBES2 salt (<code>p2s</code>) property of
   **                            the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  p2c                the PBES2 count (<code>p2c</code>) property of
   **                            the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  i4v                the initialization vector (<code>iv</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  sid                the sender key ID (<code>skid</code>)
   **                            property of the <code>EncryptionHeader</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
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
  private EncryptionHeader(final Algorithm enc, final JsonWebKey epk, final Compression zip, final EncodedURL apu, final EncodedURL apv, final EncodedURL p2s, final Integer p2c, final EncodedURL i4v, final String sid, final URI jku, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final JsonWebKey jwk, final String kid, final Algorithm algorithm, final Type type, final String contentType, final Map<String, String> parameter, final EncodedURL parsed) {
    // ensure inheritance
    super(jku, x5u, x5t, x5t256, x5c, jwk, kid, algorithm, type, contentType, parameter, parsed);

    // initialize instance attributes
    this.enc = enc;
    this.epk = epk;
    this.zip = zip;
    this.apu = apu;
    this.apv = apv;
    this.p2s = p2s;
    this.p2c = p2c;
    this.i4v = i4v;
    this.sid = sid;
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
    parameter.add(ENC);
    if (this.epk != null)
      parameter.add(EPK);

    if (this.zip != null)
      parameter.add(ZIP);

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

    if (this.apu != null)
      parameter.add(APU);

    if (this.apv != null)
      parameter.add(APV);

    if (this.p2s != null)
      parameter.add(P2S);

    if (this.p2c != null)
      parameter.add(P2C);

    if (this.i4v != null)
      parameter.add(I4V);

    if (this.sid != null)
      parameter.add(SID);

    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Encryption (JWE) header from the specified
   ** {@link EncodedURL} value.
   **
   ** @param  value              the {@link EncodedURL} value to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>EncryptionHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWE header.
   */
  public static EncryptionHeader from(final EncodedURL value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(Objects.requireNonNull(value, "The encoded value must not be null").decodeString(), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Encryption (JWE) header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>EncryptionHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWE header.
   */
  public static EncryptionHeader from(final String value)
    throws IllegalArgumentException {

    return from(value, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Encryption (JWE) header from the specified string.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>EncryptionHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>EncryptionHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if <code>value</code> doesn't represent
   **                                  a valid JWE header.
   */
  public static EncryptionHeader from(final String value, final EncodedURL origin)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(JsonMarshaller.readObject(Objects.requireNonNull(value, "The encryption header string must not be null")), origin);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a JSON Web Encryption (JWE) header from the specified JSON
   ** object.
   **
   ** @param  object             the JSON object representation of the JSON Web
   **                            Encryption (JWE) header to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  origin             the original parsed {@link EncodedURL} of the
   **                            <code>EncryptionHeader</code> to set.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>EncryptionHeader</code> matching
   **                            standard algorithm constant.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the specified JSON object doesn't
   **                                  represent a valid JWE header.
   */
  public static EncryptionHeader from(final JsonObject object, final EncodedURL origin)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(object, "The Json Object must not be null");

    // create the header in its minmal form
    final Builder b = Builder.of(Header.algorithm(object), method(object)).origin(origin);
    // parse optional + custom parameters
    for (String name : object.keySet()) {
      // skip
      if (name.equals(ALG))
        continue;
      // skip
      else if (name.equals(ENC))
        continue;
      else if (name.equals(EPK))
        b.ephemeralPublicKey(JsonWebKey.from(object.getJsonObject(name)));
      else if (name.equals(ZIP))
        b.compression(Compression.from(object.getString(name)));
      else if (name.equals(TYP))
        b.type(Type.from(object));
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
      else if (name.equals(APU))
        // assume the object containes the encoded from of the data
        b.agreementPartyUInfo(EncodedURL.raw(object.getString(name)));
      else if (name.equals(APV))
        // assume the object containes the encoded from of the data
        b.agreementPartyVInfo(EncodedURL.raw(object.getString(name)));
      else if (name.equals(P2S))
        // assume the object containes the encoded from of the data
        b.pbes2Salt(EncodedURL.raw(object.getString(name)));
      else if (name.equals(P2C))
        // assume the object containes the encoded from of the data
        b.pbes2Count(object.getInt(name));
      else if (name.equals(I4V))
        // assume the object containes the encoded from of the data
        b.iv(EncodedURL.raw(object.getString(name)));
      else if (name.equals(SID))
        // assume the object containes the encoded from of the data
        b.senderId(object.getString(name));
      else
        b.parameter(name, object.getString(name));
    }
    return b.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create and return a copy of the
   ** <code>EncryptionHeader</code> <code>other</code>.
   **
   ** @param  other              the <code>EncryptionHeader</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @return                    the copy of the  <code>EncryptionHeader</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionHeader</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static EncryptionHeader of(final EncryptionHeader other) {
    return new EncryptionHeader(Objects.requireNonNull(other));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify (overridden)
  /**
   ** Returns a JSON object representation of this
   ** <code>EncryptionHeader</code>.
   ** <p>
   ** This method is intended to be called from extending classes.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@code JsonReader#readObject(Reader)} and to create a value that is
   ** <em>equal</em> to this object.
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  @Override
  public JsonObjectBuilder jsonify() {
    final JsonObjectBuilder builder = super.jsonify();
    if (this.enc != null)
      builder.add(ENC, this.enc.toString());
    if (this.epk != null)
      builder.add(EPK, this.epk.jsonify());
    if (this.zip != null)
      builder.add(ZIP, this.zip.toString());
    if (this.apu != null)
      builder.add(APU, this.apu.toString());
    if (this.apv != null)
      builder.add(APV, this.apv.toString());
    if (this.p2s != null)
      builder.add(P2S, this.p2s.toString());
    if (this.p2c != null)
      builder.add(P2C, this.p2c);
    if (this.i4v != null)
      builder.add(I4V, this.i4v.toString());
    if (this.sid != null)
      builder.add(SID, this.sid);
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   method
  /**
   ** Parses an encryption method (<code>enc</code>) parameter from the
   ** specified JWE header JSON object.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the encryption method.
   **                            <br>
   **                            Possible object is {@link Algorithm}.
   **
   ** @throws IllegalArgumentException if <code>enc</code> parameter couldn't be
   *                                   parsed.
   */
  private static Algorithm method(final JsonObject object)
    throws IllegalArgumentException {

    return Algorithm.from(object.getString(ENC));
  }
}