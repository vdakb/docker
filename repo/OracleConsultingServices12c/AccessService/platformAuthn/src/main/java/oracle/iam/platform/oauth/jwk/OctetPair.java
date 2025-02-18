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

    File        :   OctetPair.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OctetPair.


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

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.cert.X509Certificate;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// final class OctetPair
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Public {@link JsonWebKey.Type#OKP Octet key pair}
 ** {@link JsonWebKey JSON WebKey} used to represent Edwards-curve keys.
 ** <p>
 ** Supported curves:
 ** <ul>
 **   <li>{@link Curve#ED448 Ed448}
 **   <li>{@link Curve#ED25519 Ed25519}
 **   <li>{@link Curve#X448 X448}
 **   <li>{@link Curve#X25519 X25519}
 ** </ul>
 ** <p>
 ** Example JSON object representation of a public OKP JWK:
 ** <pre>
 **   { "kty" : "OKP"
 **   , "crv" : "Ed25519"
 **   , "x"   : "11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo"
 **   , "use" : "sig"
 **   , "kid" : "1"
 **   }
 ** </pre>
 ** <p>
 ** Example JSON object representation of a private OKP JWK:
 ** <pre>
 **   { "kty" : "OKP"
 **   , "crv" : "Ed25519"
 **   , "x"   : "11qYAYKxCrfVS_7TyWQHOg7hcvPapiMlrwIaaPcHURo"
 **   , "d"   : "nWGxne_9WmC6hEr0kuwsxERJxWl7MmkZcDusAxyuf2A"
 **   , "use" : "sig"
 **   , "kid" : "1"
 **   }
 ** </pre>
 ** <p>
 ** Use the builder factory method to create a new OKP JWK:
 ** <pre>
 **   final OctetPair key = OctetPair.Builder.of(Curve.Ed25519, x)
 **     .identifier("1")
 **     .usage(JsonWebKey.Usage.SIGNATURE)
 **     .build();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OctetPair extends    JsonWebKey
                       implements Asymmetric {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** JWK parameter for public key.
   **
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc8037#section-2">RFC 8037 "x" (OKP Public Key) Parameter</a>
   */
  public static final String PUBLIC           = "x";

  /**
   ** JWK parameter for private key.
   **
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc8037#section-2">RFC 8037 "d" (OKP Private Key) Parameter</a>
   */
  public static final String PRIVATE          = "d";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3915502963331681610")
  private static final long  serialVersionUID = -8578733695965452893L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The curve name. */
  public final Curve         curve;

  /** The public 'x' parameter. */
  public final EncodedURL    x;

  /** The private 'd' parameter. */
  public final EncodedURL    d;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing {@link OctetPair Key Pair}
   ** {@link JsonWebKey JSON Web Key (JWK)}.
   ** <p>
   ** Example usage:
   ** <pre>
   **   final OctetPair key = OctetPair.Builder.of(n, e)
   **     .identifier("1")
   **      .d(d)
   **     .algorithm(Algorithm.ES512)
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends JsonWebKey.Builder<Builder, OctetPair> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The curve name. */
    private final Curve      curve;

    /** The public 'x' parameter. */
    private final EncodedURL x;

    /** The private 'd' parameter, optional. */
    private EncodedURL       d;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new Octet Key Pair JWK builder.
     **
     ** @param  curve            the cryptographic curve.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Curve}.
     ** @param  x                the public <code>x</code> parameter for the
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
    private Builder(final Curve curve, final EncodedURL x) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.curve = Objects.requireNonNull(curve, "The curve must not be null");
      this.x     = Objects.requireNonNull(x,     "The public x parameter must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: d
    /**
     ** Sets the private <code>d</code> parameter for the curve point of the
     ** <code>OctetPair</code>from the specified {@link JsonObject}
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
      final String value = JsonMarshaller.stringValue(object, PRIVATE);
      // assume the object containes the encoded from of the data
      return d(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: d
    /**
     ** Sets the private <code>d</code> parameter for the curve point of the
     ** <code>OctetPair</code>.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the
     ** coordinate's big endian representation.
     **
     ** @param  value            the private <code>d</code> parameter for the
     **                          curve point.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          May be <code>null</code>.
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

      // put mandatory parameter in sorted order
      LinkedHashMap<String,String> parameter = new LinkedHashMap<>();
      parameter.put(Curve.TAG, this.curve.id);
      parameter.put(KTY,       Type.OKP.id);
      parameter.put(PUBLIC,    this.x.toString());
      this.kid = compute(algorithm, parameter).toString();
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (JsonWebKey.Builder)
    /**
     ** Factory method to create a {@link OctetPair} with the properties
     ** configured.
     **
     ** @return                  the created {@link OctetPair} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is {@link OctetPair}
     **
     ** @throws IllegalStateException if the JSON Web Key properties were
     **                               inconsistently specified.
     */
    @Override
    public final OctetPair build() {
      return new OctetPair(this.curve, this.x, this.d, this.use, this.ops, this.kid, this.alg, this.x5u, this.x5t, this.x5t256, this.x5c, this.keyStore);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link OctetPair} <code>Builder</code> with
     ** the specified encoded key value.
     **
     ** @param  curve            the cryptographic curve.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Curve}.
     ** @param  x                the public <code>x</code> parameter for the
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
     ** @return                  the created @link OctetPair}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>k</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final Curve curve, final EncodedURL x) {
      // prevent bogus input
      return new Builder(curve, x);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web Key code>OctetPair</code> from the specified
   ** <code>OctetPair</code>.
   **
   ** @param  other              the <code>OctetPair</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>OctetPair</code>.
   */
  @SuppressWarnings("unchecked")
  private OctetPair(final OctetPair other) {
    this(other.curve, other.x, other.d, other.use, other.ops, other.kid, other.alg, other.x5u, other.x5t, other.x5t256, other.x5c, other.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  OctetPair
  /**
   ** Constructs a new JSON Web Key <code>OctetPair</code> with the specified
   ** parameters.
   **
   ** @param  curve              the cryptographic curve.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Curve}.
   ** @param  x                  the <code>x</code> parameter for the curve
   **                            point.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding coordinate's big endian
   **                            representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  d                  the private <code>d</code> parameter for the
   **                            curve point.
   **                            <br>
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
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   */
  @SuppressWarnings("unchecked")
  private OctetPair(final Curve curve, final EncodedURL x, final EncodedURL d, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    // ensure inheritance
    super(Type.OKP, use, ops, kid, alg, x5u, x5t, x5t256, x5c, keyStore);

    // initialize instance attributes
    this.curve = curve;
    this.x     = x;
    this.d     = d;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey (Asymmetric)
  /**
   ** Returns a standard {@link PrivateKey} representation of this
   ** <code>OctetPair</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                     the private octet key or <code>null</code> if
   **                             not specified.
   **                             <br>
   **                             Possible object is {@link PrivateKey}.
   **
   ** @throws JoseException       if conversion failed or is not supported by
   **                             the underlying Java Cryptography (JCA)
   **                             provider or key spec parameters are invalid
   **                             for a {@link PrivateKey}.
   */
  @Override
  public final PrivateKey privateKey()
    throws JoseException {

    throw new JoseException("Export to java.security.PrivateKey not supported");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKey (Asymmetric)
  /**
   ** Returns a standard {@link PublicKey} representation of this
   ** <code>OctetPair</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the public octet key.
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

    throw new JoseException("Export to java.security.PublicKey not supported");
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

    throw new JoseException("Export to java.security.KeyPair not supported");
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
    // X.509 certs don't support OKP yet
    return false;
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
    return this.d != null;
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
    required.put(Curve.TAG, this.curve.id);
    required.put(KTY,       this.kty.id);
    required.put(PUBLIC,    this.x.toString());
    return required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a public RSA JSON Web Key from the specified
   ** JSON object representation.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed <code>OctetPair</code>.
   **                            <br>
   **                            Possible object is <code>OctetPair</code>.
   **
   ** @throws IllegalArgumentException if on of the parsed properties is
   **                                  required but is <code>null</code>
   **                                  or empty or does not match.
   */
  public static OctetPair from(final JsonObject object)
    throws IllegalArgumentException {

    // check algorithm type
    if (Type.OKP != Type.from(object))
      throw new IllegalArgumentException("The algorithm type must be OKP");

    // create a builder with the mandatory public key parameters assume the
    // object containes the encoded from of the data
    return Builder.of(Curve.from(object), EncodedURL.raw(JsonMarshaller.stringValue(object, PUBLIC)))
      .identifier(object)
      .usage(object)
      .operation(object)
      .algorithm(EncryptionInstance.from(object))
      .certificateURI(object)
      .sha1(object)
      .sha256(object)
      .chain(SecureHeader.certificateChain(object, SecureHeader.X5C))
      // parse the optional private key parameters
      .d(object)
      .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create and return aN octet JWK pair as a copy of the
   ** specified JSON Web Key <code>other</code>.
   **
   ** @param  other              the <code>OctetPair</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>OctetPair</code>.
   **
   ** @return                    the copy of the  <code>OctetPair</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>OctetPair</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static OctetPair of(final OctetPair other) {
    return new OctetPair(Objects.requireNonNull(other));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  /**
   ** Returns a JSON object representation of this <code>OctetPair</code>.
   ** <p>
   ** This method is intended to be called from extending classes.
   ** <br>
   ** The result is guaranteed to be a valid input for the method
   ** {@code JsonReader#readObject(Reader)} and to create a value that is
   ** <em>equal</em> to this object.
   ** <br>
   ** Example:
   ** <pre>
   **   { "kty" : "RSA"
   **   , "use" : "sig"
   **   , "kid" : "fd28e025-8d24-48bc-a51a-e2ffc8bc274b"
   **   }
   ** </pre>
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  public JsonObjectBuilder jsonify() {
    final JsonObjectBuilder builder = super.jsonify()
      // append OKP specific attributes
     .add(Curve.TAG, this.curve.id)
     .add(PUBLIC,    this.x.toString())
    ;

    if (this.d != null) {
      builder.add(PRIVATE, this.d.toString());
    }
    return builder;
  }
}