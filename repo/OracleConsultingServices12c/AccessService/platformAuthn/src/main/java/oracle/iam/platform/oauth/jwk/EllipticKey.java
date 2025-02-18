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

    File        :   EllipticKey.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    EllipticKey.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.LinkedHashMap;

import java.net.URI;

import java.security.Key;
import java.security.KeyPair;
import java.security.Provider;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.security.spec.ECPoint;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.ECPrivateKey;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// final class EllipticKey
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Public and private {@link Type#EC Elliptic Curve}
 ** {@link JsonWebKey JSON Web Key (JWK)}.
 ** <p>
 ** Provides EC JWK import from / export to the following standard Java
 ** interfaces and classes:
 ** <ul>
 **   <li>{@link ECPublicKey}
 **   <li>{@link ECPrivateKey}
 **   <li>{@link PrivateKey} for an EC key in a PKCS#11 store
 **   <li>{@link java.security.KeyPair}
 ** </ul>
 ** <p>
 ** Supported cureves:
 ** <ul>
 **   <li>{@link Curve#P256 P-256}
 **   <li>{@link Curve#P384 P-384}
 **   <li>{@link Curve#P521 P-521}
 **   <li>{@link Curve#SECP256K1 secp256k1}
 ** </ul>
 ** <p>
 ** Example JSON object representation of a public EC JWK:
 ** <pre>
 **   { "kty" : "EC"
 **   , "crv" : "P-256"
 **   , "x"   : "MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
 **   , "y"   : "4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM"
 **   , "use" : "enc"
 **   , "kid" : "1"
 **   }
 ** </pre>
 ** <p>
 ** Example JSON object representation of a private EC JWK:
 ** <pre>
 **   { "kty" : "EC"
 **   , "crv" : "P-256"
 **   , "x"   : "MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
 **   , "y"   : "4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM"
 **   , "d"   : "870MB6gfuTJ4HtUnUvYMyJpr5eUZNP4Bk43bVdj3eAE"
 **   , "use" : "enc"
 **   , "kid" : "1"
 **   }
 ** </pre>
 ** <p>
 ** Use the builder factory method to create a new EC JWK:
 ** <pre>
 **   final EllipticKey key = EllipticKey.Builder.of(Curve.P_256, x, y)
 **     .identifier("1")
 **     .usage(EllipticKey.Usage.SIGNATURE)
 **     .build();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class EllipticKey extends    JsonWebKey
                               implements Asymmetric {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** JWK parameter for X coordinate.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.1.2">RFC 7518, section 6.2.1.2 "x" (X Coordinate) Parameter</a>
   */
  public static final String CX               = "x";

  /**
   ** JWK parameter for X coordinate.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.1.3">RFC 7518, section 6.2.1.3 "y" (Y Coordinate) Parameter</a>
   */
  public static final String CY               = "y";

  /**
   ** JWK parameter for private key.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.2.1">RFC 7518, section 6.2.2.1 "d" (ECC Private Key) Parameter</a>
   */
  public static final String CD               = "d";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7998159271473276198")
  private static final long  serialVersionUID = 5656324857559210128L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The curve name. */
  public final Curve         c;

  /** The 'x' coordinate. */
  public final EncodedURL    x;

  /** The 'y' coordinate. */
  public final EncodedURL    y;

  /** The 'd' coordinate. */
  public final EncodedURL    d;

  /** The private EC key, as PKCS#11 handle, optional. */
  private final PrivateKey   pkcs;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing {@link EllipticKey Elliptic Curve}
   ** {@link JsonWebKey JSON Web Key (JWK)}.
   ** <p>
   ** Example usage:
   ** <pre>
   **   final EllipticKey key = EllipticKey.Builder.of(n, e)
   **     .identifier("1")
   **      .d(d)
   **     .algorithm(Algorithm.ES512)
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends JsonWebKey.Builder<Builder, EllipticKey> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The curve name. */
    private final Curve      c;

    /** The 'x' coordinate. */
    private final EncodedURL x;

    /** The 'y' coordinate. */
    private final EncodedURL y;

    /** The 'd' coordinate, optional. */
    private EncodedURL       d;

    /** The private EC key, as PKCS#11 handle, optional. */
    private PrivateKey       pkcs;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new Elliptic Curve JWK builder.
     **
     ** @param  c                the cryptographic curve.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Curve}.
     ** @param  x                the <code>x</code> coordinate for the elliptic
     **                          curve point.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  y                the <code>y</code> coordinate for the elliptic
     **                          curve point.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws NullPointerException if either <code>curve</code>,
     **                              <code>x</code> or <code>y</code> is
     **                              <code>null</code>.
     */
    private Builder(final Curve c, final EncodedURL x, final EncodedURL y) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.c = Objects.requireNonNull(c, "The curve must not be null");
      this.x = Objects.requireNonNull(x, "The x coordinate must not be null");
      this.y = Objects.requireNonNull(y, "The y coordinate must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: d
    /**
     ** Sets the private <code>d</code> coordinate for the elliptic curve point
     ** of the <code>EllipticKey</code> from the specified {@link JsonObject}
     ** representation of a <code>JsonWebKey</code>.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the
     ** coordinate's big endian representation.
     **
     ** @param  object           the {@link JsonObject} to parse.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object id {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder d(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, CD);
      // assume the object containes the encoded from of the data
      return d(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: d
    /**
     ** Sets the private <code>d</code> coordinate for the elliptic curve point
     ** of the <code>EllipticKey</code>.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the
     ** coordinate's big endian representation.
     **
     ** @param  value            the <code>d</code> coordinate for the elliptic
     **                          curve point of the <code>EllipticKey</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder d(final EncodedURL value) {
      this.d = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the private EC key, typically for a key located in a PKCS#11 store
     ** that doesn't expose the private key parameters (such as a smart card or
     ** HSM).
     **
     ** @param  value            the private EC key reference.
     **                          <br>
     **                          Its algorithm <b>must</b> be "EC".
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is <code>Builder</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder privateKey(final PrivateKey value) {

      if (value instanceof ECPrivateKey)
        return privateKey((ECPrivateKey) value);

      if (!Type.EC.id.equalsIgnoreCase(value.getAlgorithm()))
        throw new IllegalArgumentException("The private key algorithm must be EC");

      this.pkcs = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the private Elliptic Curve key. The alternative method
     ** is {@link #d}.
     **
     ** @param  value            the private EC key, used to obtain the private
     **                          <code>d</code> coordinate for the elliptic
     **                          curve point.
     **                          <code>null</code> if not specified (for a
     **                          public
     *             key).
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder privateKey(final ECPrivateKey value) {

      if (value != null) {
        this.d = EncodedURL.of(value.getParams().getCurve().getField().getFieldSize(), value.getS());
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: thumbprint (JsonWebKey.Builder)
    /**
     ** Sets the identifier (<code>kid</code>) of the <code>JsonWebKey</code> to
     ** its SHA-256 JSON Web Key thumbprint (RFC 7638).
     ** <br>
     ** The identifier can be used to match a specific key. This can be used,
     ** for instance, to choose a key within a {@link JsonWebKeySet} during key
     ** rollover.
     ** <br>
     ** The identifier may also correspond to a JWS/JWE <code>kid</code> header
     ** parameter value.
     **
     ** @param algorithm         the hash algorithm for the JSON Web Key
     **                          thumbprint computation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws JoseException    if the SHA-256 hash algorithm is not supported.
     */
    @Override
    public final Builder thumbprint(final String algorithm)
      throws JoseException {

      LinkedHashMap<String,String> parameter = new LinkedHashMap<>();
      parameter.put(Curve.TAG, this.c.id);
      parameter.put(KTY,  Type.EC.id);
      parameter.put(CX,   this.x.toString());
      parameter.put(CY,   this.y.toString());

      this.kid = compute(algorithm, parameter).toString();
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (JsonWebKey.Builder)
    /**
     ** Factory method to create a {@link EllipticKey} with the properties
     ** configured.
     **
     ** @return                  the created {@link EllipticKey} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is {@link EllipticKey}
     **
     ** @throws IllegalStateException if the JSON Web Key properties were
     **                               inconsistently specified.
     */
    @Override
    public final EllipticKey build() {
      return new EllipticKey(this.c, this.x, this.y, this.d, this.use, this.ops, this.kid, this.alg, this.x5u, this.x5t, this.x5t256, this.x5c, this.pkcs, this.keyStore);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link EllipticKey} <code>Builder</code> with
     ** the specified {@link Curve cryptographic curve} and the <code>x</code>
     ** and <code>y</code> coordinate for the elliptic curve point.
     **
     ** @param  curve            the cryptographic curve.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Curve}.
     ** @param  publicKey        the public EC key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link ECPublicKey}.
     **
     ** @return                  the created @link EllipticKey}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>curve</code>,
     **                              <code>x</code> or <code>y</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final Curve curve, final ECPublicKey publicKey) {
      // prevent bogus input
      return of(
        curve
      , EncodedURL.of(publicKey.getParams().getCurve().getField().getFieldSize(), publicKey.getW().getAffineX())
      , EncodedURL.of(publicKey.getParams().getCurve().getField().getFieldSize(), publicKey.getW().getAffineY())
      );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link EllipticKey} <code>Builder</code> with
     ** the specified {@link EllipticKey} as a template.
     **
     ** @param  other            the {@link EllipticKey} template.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EllipticKey}.
     **
     ** @return                  the created {@link EllipticKey}
     **                          <code>Builder</code> populated with the values
     **                          provided by the {@link EllipticKey} template
     **                          <code>other</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>other</code> is
     **                              <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public static Builder of(final EllipticKey other) {
      // prevent bogus input
      return of(other.c, other.x, other.y)
       // common
        .identifier(other.kid)
        .usage(other.use)
        .operation(other.ops)
        .algorithm(other.alg)
        .certificateURI(other.x5u)
        .sha1(other.x5t)
        .sha256(other.x5t256)
        .chain(other.x5c)
        .privateKey(other.pkcs)
        .keyStore(other.keyStore)
        // common
        .d(other.d)
      ;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link EllipticKey} <code>Builder</code> with
     ** the specified {@link Curve cryptographic curve} and the <code>x</code>
     ** and <code>y</code> coordinate for the elliptic curve point.
     **
     ** @param  curve            the cryptographic curve.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Curve}.
     ** @param  x                the <code>x</code> coordinate for the elliptic
     **                          curve point.
     **                          <br>
     **                          It is represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  y                the <code>y</code> coordinate for the elliptic
     **                          curve point.
     **                          <br>
     **                          It is represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the created @link EllipticKey}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>curve</code>,
     **                              <code>x</code> or <code>y</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final Curve curve, final EncodedURL x, final EncodedURL y) {
      return new Builder(curve, x, y);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web <code>EllipticKey</code> from the specified
   ** <code>EllipticKey</code>.
   **
   ** @param  other              the <code>EllipticKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>EllipticKey</code>.
   */
  @SuppressWarnings("unchecked")
  private EllipticKey(final EllipticKey other) {
    this(other.c, other.x, other.y, other.d, other.use, other.ops, other.kid, other.alg, other.x5u, other.x5t, other.x5t256, other.x5c, other.pkcs, other.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web <code>EllipticKey</code> with the specified
   ** parameters.
   **
   ** @param  curve              the cryptographic curve.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Curve}.
   ** @param  x                  the <code>x</code> coordinate for the elliptic
   **                            curve point.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding coordinate's big endian
   **                            representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  y                  the <code>y</code> coordinate for the elliptic
   **                            curve point.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding coordinate's big endian
   **                            representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  d                  the private <code>d</code> coordinate for the
   **                            elliptic curve point.
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding coordinate's big endian
   **                            representation.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  use                the key use or <code>null</code> if not
   **                            specified or if the key is intended for signing
   **                            as well as encryption.
   **                            <br>
   **                            Allowed object is {@link Usage}.
   ** @param  ops                the permitted key operations or
   **                            <code>null</code> if not specified or if
   **                            the key is intended for all operations.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Operation}.
   ** @param  kid                the key id or <code>null</code> if not
   **                            specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  alg                the intended JOSE algorithm for the key or
   **                            <code>null</code> if not specified.
   **                            specified.
   **                            <br>
   **                            Allowed object is {@link Algorithm}.
   ** @param  x5u                the X.509 certificate URL or <code>null</code>
   **                            if not specified.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @param  x5t                the X.509 certificate SHA-1 thumbprint or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5t256             the X.509 certificate SHA-256 thumbprint or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  x5c                the X.509 certificate chain or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type of {@link Encoded}.
   ** @param  pkcs               the private key to present.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link PrivateKey}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @throws NullPointerException if either <code>curve</code>, <code>x</code>
   **                              or <code>y</code> is <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  private EllipticKey(final Curve curve, final EncodedURL x, final EncodedURL y, final EncodedURL d, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final PrivateKey pkcs, final KeyStore keyStore) {
    // ensure inheritance
    super(Type.EC, use, ops, kid, alg, x5u, x5t, x5t256, x5c, keyStore);

    // initialize instance attributes
    this.c    = Objects.requireNonNull(curve, "The curve must not be null");
    this.x    = Objects.requireNonNull(x,     "The x coordinate must not be null");
    this.y    = Objects.requireNonNull(y,     "The y coordinate must not be null");
    this.d    = d;
    this.pkcs = pkcs;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ecPublicKey
  /**
   ** Returns a standard {@link ECPublicKey} representation of this
   ** <code>EllipticKey</code> JWK.
   **
   ** @param  provider            the specific JCA provider to use,
   **                             <code>null</code> implies the default one.
   **                             <br>
   **                             Allowed object is {@link Provider}.
   **
   ** @return                     the public Elliptic Curve key.
   **                             <br>
   **                             Possible object is {@link ECPublicKey}.
   **
   ** @throws JoseException       if EC is not supported by the underlying Java
   **                             Cryptography (JCA) provider or if the JWK
   **                             parameters are invalid for a public EC key.
   */
  public ECPublicKey ecPublicKey(final Provider provider)
    throws JoseException {

    final ECParameterSpec spec = Curve.MAP.get(this.c.id);
    if (spec == null)
      throw new JoseException("Couldn't get EC parameter spec for curve " + this.c.id);

    final ECPoint         w = new ECPoint(x.decodeBigInteger(), y.decodeBigInteger());
    final ECPublicKeySpec s = new ECPublicKeySpec(w, spec);
    try {
      final KeyFactory factory = (provider == null) ? KeyFactory.getInstance(Type.EC.id) : KeyFactory.getInstance(Type.EC.id, provider);
      return (ECPublicKey) factory.generatePublic(s);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new JoseException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ecPrivateKey
  /**
   ** Returns a standard {@link ECPrivateKey} representation of this
   ** <code>EllipticKey</code> JWK.
   **
   ** @param  provider            the specific JCA provider to use,
   **                             <code>null</code> implies the default one.
   **                             <br>
   **                             Allowed object is {@link Provider}.
   **
   ** @return                     the private Elliptic Curve key or
   **                             <code>null</code> if not specified.
   **                             <br>
   **                             Possible object is {@link ECPrivateKey}.
   **
   ** @throws JoseException       if EC is not supported by the underlying Java
   **                             Cryptography (JCA) provider or if the JWK
   **                             parameters are invalid for a private EC key.
   */
  public ECPrivateKey ecPrivateKey(final Provider provider)
    throws JoseException {

    // no private 'd' param
    if (this.d == null)
      return null;

    final ECParameterSpec spec = Curve.MAP.get(this.c.id);
    if (spec == null)
      throw new JoseException("Couldn't get EC parameter spec for curve " + this.c.id);

    ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(this.d.decodeBigInteger(), spec);
    try {
      KeyFactory keyFactory;
      if (provider == null) {
        keyFactory = KeyFactory.getInstance(Type.EC.id);
      }
      else {
        keyFactory = KeyFactory.getInstance(Type.EC.id, provider);
      }
      return (ECPrivateKey) keyFactory.generatePrivate(privateKeySpec);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new JoseException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ecKeyPair
  /**
   ** Returns a standard {@link KeyPair} representation of this
   ** <code>EllipticKey</code> JWK.
   **
   ** @param  provider            the specific JCA provider to use,
   **                             <code>null</code> implies the default one.
   **                             <br>
   **                             Allowed object is {@link Provider}.
   **
   ** @return                     the Elliptic Curve {@link KeyPair} or
   **                             <code>null</code> if not specified.
   **                             <br>
   **                             Possible object is {@link KeyPair}.
   **
   ** @throws JoseException       if EC is not supported by the underlying Java
   **                             Cryptography (JCA) provider or if the JWK
   **                             parameters are invalid for a private EC key.
   */
  public KeyPair ecKeyPair(final Provider provider)
    throws JoseException {

    if (this.pkcs != null) {
      // private key as PKCS#11 handle
      return new KeyPair(ecPublicKey(provider), this.pkcs);
    }
    else {
      return new KeyPair(ecPublicKey(provider), ecPrivateKey(provider));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey (Asymmetric)
  /**
   ** Returns a standard {@link PrivateKey} representation of this
   ** <code>EllipticKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the private Elliptic Curve key or
   **                            <code>null</code> if not specified.
   **                            <br>
   **                            Possible object is {@link PrivateKey}.
   **
   ** @throws JoseException      if conversion failed or is not supported by
   **                            the underlying Java Cryptography (JCA)
   **                            provider or key spec parameters are invalid
   **                            for a {@link PrivateKey}.
   */
  @Override
  public final PrivateKey privateKey()
    throws JoseException {

    PrivateKey prv = ecPrivateKey(null);
    // return private EC key as PKCS#11 handle, or private EC key with key
    // material
    return (prv == null) ? this.pkcs : prv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKey (Asymmetric)
  /**
   ** Returns a standard {@link PublicKey} representation of this
   ** <code>EllipticKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the public Elliptic Curve key.
   **                            <br>
   **                            Possible object is {@link PublicKey}.
   **
   ** @throws JoseException      if conversion failed or is not supported by
   **                            the underlying Java Cryptography (JCA)
   **                            provider or key spec parameters are invalid
   **                            for a {@link PublicKey}.
   */
  @Override
  public final PublicKey publicKey()
    throws JoseException {

    return ecPublicKey(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyPair (Asymmetric)
  /**
   ** Returns a standard {@link KeyPair} representation of this
   ** <code>Asymmetric</code> JSON Web Key (JWK).
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the standard {@link KeyPair} representation
   **                            of this <code>Asymmetric</code> JSON Web Key
   **                            (JWK) or <code>null</code> if not specified.
   **                            <br>
   **                            Possible object is {@link KeyPair}.
   **
   ** @throws JoseException      if conversion failed or is not supported by
   **                            the underlying Java Cryptography (JCA)
   **                            provider or key spec parameters are invalid
   **                            for a {@link PublicKey}.
   */
  @Override
  public final KeyPair keyPair()
    throws JoseException {

    return ecKeyPair(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match (Asymmetric)
  /**
   ** Returns <code>true</code> if the public key material of this
   ** <code>Asymmetric</code> JSON Web Key matches the public subject key info
   ** of the specified X.509 certificate.
   **
   ** @param  certificate        the X.509 certificate.
   **                            <br>
   **                            Must not be <code>null</code>.
   **
   ** @return                    <code>true</code> if the public key material of
   **                            this <code>Asymmetric</code> JSON Web Key
   **                            matches the public subject key info of the
   **                            specified X.509 certificate; otherwise
   **                            <code>false</code>.
   */
  @Override
  public final boolean match(final X509Certificate certificate) {
    if (this.certificate == null && certificate == null)
      return true;

    final X509Certificate own = (X509Certificate)this.certificate.get(0);
    try {
      final ECPublicKey publicKey = (ECPublicKey)own.getPublicKey();
      // compare Big Ints, base64url encoding may have padding!
      // https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.1.2
      return this.x.decodeBigInteger().equals(publicKey.getW().getAffineX())
          && this.y.decodeBigInteger().equals(publicKey.getW().getAffineY())
      ;
    }
    catch (ClassCastException x) {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive (JsonWebKey)
  /**
   ** Returns <code>true</code> if this JSON Web Key contains sensitive or
   ** private key material.
   **
   ** @return                    <code>true</code> if this JWK contains
   **                            sensitive key material; otherwise
   **                            <code>false</code>.
   */
  @Override
  public final boolean sensitive() {
    return this.d != null || this.pkcs != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredParameter (JsonWebKey)
  /**
   ** Returns the required JSON Web Key parameters.
   ** <br>
   ** Intended as input for JWK thumbprint computation.
   ** <br>
   ** See RFC 7638 for more information.
   **
   ** @return                    the required JSON Web Key parameters, sorted
   **                            alphanumerically by key name and ready for JSON
   **                            serialisation.
   **                            <br>
   **                            Possible object is {@link LinkedHashMap} where
   **                            each element is of type {@link String} as the
   **                            key and {@link String} for the value.
   */
  @Override
  public LinkedHashMap<String,?> requiredParameter() {
    // put mandatory parameter in sorted order
    final LinkedHashMap<String, String> required = new LinkedHashMap<>();
    required.put(Curve.TAG, this.c.id);
    required.put(CX,        this.x.toString());
    required.put(CY,        this.y.toString());
    required.put(SecureHeader.KID,       Type.EC.id);
    return required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Loads a public/private Elliptic Curve JSON Web Key (JWK) from the
   ** specified JCE key store.
   ** <p>
   ** <strong>Important:</strong>
   ** <br>
   ** The X.509 certificate is not validated!
   **
   ** @param  store              the {@link KeyStore}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object {@link KeyStore}.
   ** @param  alias              the alias of the key to lookup.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object {@link String}.
   ** @param  secret             the secret to unlock the private key if any,
   **                            empty or <code>null</code> if not required.
   **                            <br>
   **                            Allowed object array of <code>char</code>.
   **
   ** @return                    The public/private Elliptic Curve JSON Web Key
   **                            (JWK) or <code>null</code> if no key with the
   **                            specified alias was found.
   **                            <br>
   **                            Possible object is <code>EllipticKey</code>.
   **
   ** @throws JoseException      if octet sequence key loading failed.
   */
  public static EllipticKey load(final KeyStore store, final String alias, final char[] secret)
    throws JoseException {

    try {
      final Certificate certificate = store.getCertificate(alias);
      if (!(certificate instanceof X509Certificate))
        return null;

      final X509Certificate x509 = (X509Certificate)certificate;
      if (!(x509.getPublicKey() instanceof ECPublicKey))
        throw new JoseException("Couldn't load EC JWK: The key algorithm is not EC");

      EllipticKey jwk = EllipticKey.from(x509);
      // kid = alias
      jwk = Builder.of(jwk).identifier(alias).keyStore(store).build();

      // check for private counterpart
      Key key;
      try {
        key = store.getKey(alias, secret);
      }
      catch (UnrecoverableKeyException | NoSuchAlgorithmException e) {
        throw new JoseException("Couldn't retrieve private EC key (bad secret?): " + e.getMessage(), e);
      }
      if (key instanceof ECPrivateKey) {
        // simple file based key store
        return Builder.of(jwk).privateKey((ECPrivateKey)key).build();
      }
      else if (key instanceof PrivateKey && Type.EC.id.equalsIgnoreCase(key.getAlgorithm())) {
        // PKCS#11 store
        return Builder.of(jwk).privateKey((PrivateKey)key).build();
      }
      else {
        return jwk;
      }
    }
    catch (KeyStoreException e) {
      throw new JoseException("Couldn't retrieve private key (bad secret?): " + e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a public Elliptic Curve JSON Web Key from the
   ** specified X.509 certificate.
   ** <p>
   ** <strong>Important:</strong>
   ** <br>
   ** The X.509 certificate is not validated!
   ** <p>
   ** Sets the following JWK parameters:
   ** <ul>
   **   <li><b>crv</b> the curve is obtained from the subject public key info
   **                  algorithm parameters.
   **   <li><b>use</b> inferred by {@link Usage#from}.
   **   <li><b>kid</b> from the X.509 serial number (in base 10).
   **   <li><b>x5t256</b> certificate SHA-256 thumbprint.
   **   <li><b>x5c</b> certificate chain (this certificate only).
   ** </ul>
   **
   ** @param  certificate      the X.509 certificate.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link X509Certificate}.
   **
   ** @return                  the public Elliptic Curve key.
   **                          <br>
   **                          Possible object is <code>EllipticKey</code>.
   **
   ** @throws JoseException    if parsing failed.
   */
  public static EllipticKey from(final X509Certificate certificate)
    throws JoseException {

    // prevent bogus input
    if (! (certificate.getPublicKey() instanceof ECPublicKey))
      throw new JoseException("The public key of the X.509 certificate is not EC");

    final ECPublicKey publicKey = (ECPublicKey)certificate.getPublicKey();
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create an Elliptic Curve JWK from the specified JSON
   ** object representation.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed <code>EllipticKey</code>.
   **                            <br>
   **                            Possible object is <code>EllipticKey</code>.
   **
   ** @throws NullPointerException     if <code>object</code> is
   **                                  <code>null</code> of later on an object
   **                                  segment becomes <code>null</code>.
   ** @throws IllegalArgumentException if <code>object</code> represents the
   **                                  wrong key algorithm type.
   */
  public static EllipticKey from(final JsonObject object)
    throws IllegalArgumentException {

      // check algorithm type
    if (Type.EC != Type.from(object))
      throw new IllegalArgumentException("The algorithm type must be EC");

    // create a builder with the mandatory public key parameters assume the
    // object containes the encoded from of the data
    return Builder.of(Curve.from(object.getString(Curve.TAG)), EncodedURL.raw(object.getString(CX)), EncodedURL.raw(object.getString(CY)))
      // parse the common key parameters
      .identifier(object)
      .usage(object)
      .operation(object)
      .algorithm(EncryptionInstance.from(object))
      .certificateURI(object)
      .sha1(object)
      .sha256(object)
      .chain(SecureHeader.certificateChain(object, SecureHeader.X5C))
      // parse the optional key parameters
      .d(object)
      .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create and return a Elliptic Curve JWK as a copy of the
   ** specified JSON Web Key <code>other</code>.
   **
   ** @param  other              the <code>EllipticKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>EllipticKey</code>.
   **
   ** @return                    the copy of the  <code>EllipticKey</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>EllipticKey</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static EllipticKey of(final EllipticKey other) {
    return new EllipticKey(Objects.requireNonNull(other));
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
    return Objects.hash(super.hashCode(), this.c, this.x, this.y, this.d, this.pkcs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>EllipticKey</code>s values are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>EllipticKey</code>s values may be different even though they contain
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

    if (!(other instanceof EllipticKey))
      return false;

    if (!super.equals(other))
      return false;

    final EllipticKey that = (EllipticKey)other;
    return Objects.equals(this.c,    that.c)
        && Objects.equals(this.x,    that.x)
        && Objects.equals(this.y,    that.y)
        && Objects.equals(this.d,    that.d)
        && Objects.equals(this.pkcs, that.pkcs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify (overridden)
  /**
   ** Returns a JSON object representation of this <code>EllipticKey</code>.
   ** <p>
   ** This method is  intended to be called from extending classes.
   ** <br>
   ** Example:
   ** <pre>
   **   { "kty" : "EC"
   **   , "crv" : "P-256"
   **   , "x"   : "MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
   **   , "y"   : ""4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM""
   **   , "use" : "enc"
   **   , "kid" : "1"
   **   }
   ** </pre>
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  @Override
  public final JsonObjectBuilder jsonify() {
    // append Elliptic Curve specific attributes
    return super.jsonify().add(Curve.TAG, c.id).add(CX, x.toString()).add(CY, y.toString());
  }
}