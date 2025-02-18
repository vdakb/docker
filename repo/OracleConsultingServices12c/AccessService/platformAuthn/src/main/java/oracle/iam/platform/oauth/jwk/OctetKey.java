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

    File        :   OctetKey.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    OctetKey.


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
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.crypto.SecretKey;

import javax.crypto.spec.SecretKeySpec;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// final class OctetKey
// ~~~~~ ~~~~~ ~~~~~~~~
/**
 ** Public {@link JsonWebKey.Type#OCT Octet sequence}
 ** {@link JsonWebKey JSON Web Key} represents a symmetric key
 ** <p>
 ** Example JSON object representation of an octet sequence JWK:
 ** <pre>
 **   { "kty" : "OCT"
 **   , "alg" : "A128KW"
 **   , "k"   : "GawgguFyGrWKav7AX4VKUg"
 **   }
 ** </pre>
 ** <p>
 ** Use the builder factory method to create a new OCT JWK:
 ** <pre>
 **   final OctetKey key = OctetKey.Builder.of(bytes)
 **     .identifier("123")
 **     .build();
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class OctetKey extends    JsonWebKey
                            implements SecureKey {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Used with {@link #KTY}
   **
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.4.1">RFC 7518 "k" (OCT Key Value) Parameter</a>
   */
  public static final String OCT              = "k";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2604917016945289711")
  private static final long  serialVersionUID = -6037610854440489608L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The key parameter. */
  private final EncodedURL   k;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing octet sequence {@link JsonWebKey JSON Web Key (JWK)}.
   ** <p>
   ** Example usage:
   ** <pre>
   **   final OctetKey key = OctetKey.Builder.of(k)
   **     .identifier("113")
   **     .algorithm(Algorithm.HS512)
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends JsonWebKey.Builder<Builder, OctetKey> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The key parameter. */
    private final EncodedURL k;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Builder
    /**
     ** Constructs a new octet sequence JWK builder.
     **
     ** @param  k                the key value.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws NullPointerException if either <code>k</code> is
     **                              <code>null</code>.
     */
    private Builder(final EncodedURL k) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.k = Objects.requireNonNull(k, "The key value must not be null");
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
      parameter.put(OCT, this.k.toString());
      parameter.put(KTY, Type.OCT.id);
      this.kid = compute(algorithm, parameter).toString();
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (JsonWebKey.Builder)
    /**
     ** Factory method to create a {@link OctetKey} with the properties
     ** configured.
     **
     ** @return                  the created {@link OctetKey} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is {@link OctetKey}
     **
     ** @throws IllegalStateException if the JSON Web Key properties were
     **                               inconsistently specified.
     */
    @Override
    public final OctetKey build() {
      return new OctetKey(this.k, this.use, this.ops, this.kid, this.alg, this.x5u, this.x5t, this.x5t256, this.x5c, this.keyStore);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link OctetKey} <code>Builder</code> for
     ** the specified {@link SecretKey} value.
     **
     ** @param  secretKey        the secret key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SecretKey}.
     **
     ** @return                  the created @link OctetKey}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public static Builder of(final SecretKey secretKey) {
      return of(secretKey.getEncoded());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link OctetKey} <code>Builder</code> with
     ** the specified key value material.
     **
     ** @param  k                the key value material.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the created @link OctetKey}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>k</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final byte[] k) {
      return of(EncodedURL.of(k));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link OctetKey} <code>Builder</code> with
     ** the specified encoded key value.
     **
     ** @param  k                the key value.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding coordinate's big endian
     **                          representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the created @link OctetKey}
     **                          <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>k</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final EncodedURL k) {
      // prevent bogus input
      return new Builder(k);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web <code>OctetKey</code> from the specified
   ** <code>OctetKey</code>.
   **
   ** @param  other              the <code>OctetKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>OctetKey</code>.
   */
  @SuppressWarnings("unchecked")
  private OctetKey(final OctetKey other) {
    this(other.k, other.use, other.ops, other.kid, other.alg, other.x5u, other.x5t, other.x5t256, other.x5c, other.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  OctetKey
  /**
   ** Constructs a new JSON Web <code>OctetKey</code> with the specified
   ** parameters.
   **
   ** @param  k                  the key value.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding coordinate's big endian
   **                            representation.
   **                            <br>
   **                            Must not be <code>null</code>.
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
  private OctetKey(final EncodedURL k, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    // ensure inheritance
    super(Type.OCT, use, ops, kid, alg, x5u, x5t, x5t256, x5c, keyStore);

    // initialize instance attributes
    this.k = Objects.requireNonNull(k, "The key value must not be null");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Returns a copy of this octet sequence key value as a byte array.
   **
   ** @return                    the key value as a byte array.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public byte[] decode() {
    return this.k.decode();
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
    return true;
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
    required.put(OCT, this.k.toString());
    required.put(KTY, this.kty.id);
    return required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secretKey (SecureKey)
  /**
   ** Returns a secret key representation of this octet sequence key without a
   ** Java Cryptography Architecture (JCA) algorithm.
   **
   ** @return                  the secret key representation without a
   **                          Java Cryptography Architecture (JCA) algorithm.
   **                          <br>
   **                          Possible object is {@link SecretKey}.
   */
  @Override
  public final SecretKey secretKey() {
    return secretKey(Algorithm.NONE.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Loads an octet sequence JSON Web Key (JWK) from the specified JCE key
   ** store.
   ** <p>
   ** <strong>Important:</strong>
   ** <br>
   ** The key is not validated!
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
   ** @return                    The octet sequence JSON Web Key (JWK) or
   **                            <code>null</code> if no key with the specified
   **                            alias was found.
   **                            <br>
   **                            Possible object array of <code>OctetKey</code>.
   **
   ** @throws JoseException      if octet sequence key loading failed.
   */
  public static OctetKey load(final KeyStore store, final String alias, final char[] secret)
    throws JoseException {

    try {
      final Key key = store.getKey(alias, secret);
      return (key instanceof SecretKey) ? Builder.of((SecretKey)key).identifier(alias).keyStore(store).build() : null;
    }
    catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
      throw new JoseException("Couldn't retrieve secret key (bad pin?): " + e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a public Octet Key sequence JSON Web Key from the
   ** specified JSON object representation.
   **
   ** @param  object           the JSON object to parse.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link JsonObject}.
   **
   ** @return                  the parsed <code>OctetKey</code>.
   **                          <br>
   **                          Possible object is <code>OctetKey</code>.
   **
   ** @throws IllegalArgumentException if on of the parsed properties is
   **                                  required but is <code>null</code>
   **                                  or empty or does not match.
   */
  public static OctetKey from(final JsonObject object)
    throws IllegalArgumentException {

    // check algorithm type
    if (Type.OCT != Type.from(object))
      throw new IllegalArgumentException("The algorithm type must be oct");

    // assume the object containes the encoded from of the data
    return Builder.of(EncodedURL.raw(JsonMarshaller.stringValue(object, OCT)))
      .identifier(object)
      .usage(object)
      .operation(object)
      .algorithm(EncryptionInstance.from(object))
      .certificateURI(object)
      .sha1(object)
      .sha256(object)
      .chain(SecureHeader.certificateChain(object, SecureHeader.X5C))
      .build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create and return an octet sequence JWK as a copy of the
   ** specified JSON Web Key <code>other</code>.
   **
   ** @param  other              the <code>OctetKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>OctetKey</code>.
   **
   ** @return                    the copy of the  <code>OctetKey</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>OctetKey</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static OctetKey of(final OctetKey other) {
    return new OctetKey(Objects.requireNonNull(other));
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
    return Objects.hash(super.hashCode(), k);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>OctetKey</code>s values are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>OctetKey</code>s values may be different even though they contain
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

    if (!(other instanceof OctetKey))
      return false;

    if (!super.equals(other))
      return false;

    final OctetKey that = (OctetKey)other;
    return Objects.equals(this.k, that.k);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify (overridden)
  /**
   ** Returns a JSON object representation of this <code>OctetKey</code>.
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
  @Override
  public JsonObjectBuilder jsonify() {
    return super.jsonify().add(OCT, this.k.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secretKey
  /**
   ** Returns a secret key representation of this octet sequence key with
   ** specified Java Cryptography Architecture (JCA) algorithm.
   **
   ** @param  algorithm        the JCA algorithm.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the secret key representation for the specified.
   **                          Java Cryptography Architecture (JCA) algorithm.
   **                          <br>
   **                          Possible object is {@link SecretKey}.
   */
  public SecretKey secretKey(final String algorithm) {
    return new SecretKeySpec(decode(), algorithm);
  }
}