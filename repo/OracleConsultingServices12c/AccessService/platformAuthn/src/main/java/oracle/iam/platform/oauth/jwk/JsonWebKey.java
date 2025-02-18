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

    File        :   JsonWebKey.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    JsonWebKey.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.Objects;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.util.stream.Collectors;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.nio.charset.StandardCharsets;

import java.net.URI;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.jca.cert.DER;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.core.marshal.JsonEnum;
import oracle.hst.platform.core.marshal.JsonMarshaller;

////////////////////////////////////////////////////////////////////////////////
// abstract class JsonWebKey
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The base abstract class for JSON Web Key (JWK).
 ** <br>
 ** A JWK is a representation of data needed to sign, encrypt, verify and /or
 ** decrypt data (e.g a public and/or private key; password for symmetric
 ** ciphers).
 ** <br>
 ** It serializes to a JSON object.
 ** <p>
 ** The following JSON object members are common to all Json Web Key types:
 ** <ul>
 **   <li>{@link #kty kty} (required)
 **   <li>{@link #use use} (optional)
 **   <li>{@link #ops key_ops} (optional)
 **   <li>{@link #x5u x5u} (optional)
 **   <li>{@link #x5t x5t} (optional)
 **   <li>{@link #x5t256 x5t#S256} (optional)
 **   <li>{@link #x5c x5c} (optional)
 **   <li>{@link #keyStore} (optional)
 ** </ul>
 ** <p>
 ** Example Json Web Key (of the Elliptic Curve type):
 ** <pre>
 **   { "kty" : "EC"
 **   , "crv" : "P-256"
 **   , "x"   : "MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
 **   , "y"   : "4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM"
 **   , "use" : "enc"
 **   , "kid" : "1"
 **   }
 ** </pre>
 **
 ** @param  <T>                the type of the <code>JsonWebKey</code>
 **                            implementation.
 **                            <br>
 **                            This parameter is used for convenience to allow
 **                            better implementations of
 **                            <code>JsonWebKey</code>s derived from this
 **                            abstract class (<code>JsonWebKey</code>s can
 **                            return their own specific type instead of type
 **                            defined by this abstract class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class JsonWebKey<T extends JsonWebKey> implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The MIME type of JWK objects:
   ** <code>application/jwk+json; charset=UTF-8</code>
   */
  public static final String MIME              = "application/jwk+json; charset=UTF-8";

  /**
   ** JWK parameter for key type (kty).
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.1">RFC 7517, section 4.1 "kty" (Key Type) Parameter.</a>
   */
  public static final String KTY               = "kty";

  /**
   ** JWK parameter for key usage (use).
   ** <br>
   ** The "use" (public key use) parameter identifies the intended use of the
   ** public key.
   ** <br>
   ** The "use" parameter is employed to indicate whether a public key is used
   ** for encrypting data or verifying the signature on data.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.2 "use" (Public Key Use) Parameter.</a>
   */
  public static final String USE              = "use";

  /**
   ** JWK parameters for permitted operations.
   ** <br>
   ** The "key_ops" (key operations) parameter identifies the operation(s) for
   ** which the key is intended to be used. The "key_ops" parameter is intended
   ** for use cases in which public, private, or symmetric keys may be present.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3 "key_ops" (Key Operations) Parameter.</a>
   */
  public static final String OPS              = "key_ops";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8559970325939941462")
  private static final long  serialVersionUID = -4600301327598364528L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The algorithm type, required.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.1">RFC 7517, section 4.1 "kty" (Key Type) Parameter.</a>
   */
  public final Type                  kty;

  /**
   ** The use, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.2 "use" (Public Key Use) Parameter.</a>
   */
  public final Usage                 use;

  /**
   ** The operation, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.3 "key_ops" (Key Operations) Parameter.</a>
   */
  public final Set<Operation>        ops;

  /**
   ** The intended JOSE algorithm for the key, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.4">RFC 7517, section 4.4 "alg" (Algorithm) Parameter.</a>
   */
  public final Algorithm             alg;

  /**
   ** The key ID, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.5">RFC 7517, section 4.5 "kid" (Key ID) Parameter.</a>
   */
  public final String                kid;

  /**
   ** The X.509 certificate URL, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.6">RFC 7517, section 4.6 "x5u" (X.509 Certificate URL) Parameter.</a>
   */
  public final URI                   x5u;

  /**
   ** The X.509 certificate chain, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.7">RFC 7517, section 4.7 "x5c" (X.509 Certificate Chain) Parameter.</a>
   */
  public final List<Encoded>         x5c;

  /**
   ** The X.509 certificate SHA-1 thumbprint, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.8">RFC 7517, section 4.8 "x5t" (X.509 Certificate SHA-1 Thumbprint) Parameter.</a>
   */
//  @Deprecated
  public final EncodedURL            x5t;

  /**
   ** The X.509 certificate SHA-256 thumbprint, optional.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.9">RFC 7517, section 4.9 "x5tS256" (X.509 Certificate SHA-256 Thumbprint) Parameter.</a>
   */
  public final EncodedURL            x5t256;
  /**
   ** The parsed X.509 certificate chain, optional.
   */
  public final List<X509Certificate> certificate;

  /**
   ** Reference to the underlying key store, optional.
   */
  public final KeyStore              keyStore;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Type
  // ~~~~~ ~~~~~ ~~~~
  /**
   ** Represents the algorithm type name <code>alg</code> parameter in JSON Web
   ** Keys (JWKs).
   ** <br>
   ** This class is immutable.
   ** <p>
   ** Includes constants for the following standard algorithm families:
   ** <ul>
   **   <li>{@link #EC}
   **   <li>{@link #RSA}
   **   <li>{@link #OCT}
   **   <li>{@link #OKP}
   ** </ul>
   ** <p>
   ** Additional algorithm type names can be defined using the constructors.
   */
  public static enum Type implements JsonEnum<Type, String> {
      /**
       ** Key type of RSA (RFC 3447) algorithm type (required).
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.1">RFC 7518, section 6.1. "kty" (Key Type) Parameter Values</a>
       */
      RSA("RSA", Algorithm.Requirement.REQUIRED)
      /**
       ** Key type of Elliptic Curve (DSS) algorithm type (recommended).
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.1">RFC 7518, section 6.1. "kty" (Key Type) Parameter Values</a>
       */
    , EC("EC", Algorithm.Requirement.RECOMMENDED)
      /**
       ** Key type of Octet Public Key algorithm type (optional).
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.1">RFC 7518, section 6.1. "kty" (Key Type) Parameter Values</a>
       */
    , OCT("oct", Algorithm.Requirement.OPTIONAL)
      /**
       ** Key type of Octet Key Pair algorithm type (optional).
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.1">RFC 7518, section 6.1. "kty" (Key Type) Parameter Values</a>
       */
    , OKP("OKP", Algorithm.Requirement.OPTIONAL)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The algorithm identifier. */
    public final String                id;

    /** The implementation requirement or <code>null</code> if not known. */
    public final Algorithm.Requirement requirement;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new algorithm type with the specified name and
     ** implementation requirement.
     **
     ** @param  id                 the algorithm identifier.
     **                            <br>
     **                            Names are case sensitive.
     **                            <br>
     **                            Must not be <code>null</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     ** @param  requirement        the implementation requirement,
     **                            <code>null</code> if not known.
     **                            <br>
     **                            Allowed object is
     **                            {@link Algorithm.Requirement}.
     */
    Type(final String id, final Algorithm.Requirement requirement) {
      // initialize instance attributes
      this.id          = Objects.requireNonNull(id, "The algorithm identifier must not be null");
      this.requirement = requirement;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Type</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     **
     ** @throws IllegalArgumentException if no matching <code>Type</code> could
     **                                  be found.
     */
    @Override
    public final Type of(final String value) {
      return from(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value that identifies the <code>JsonEnum</code>
     **
     ** @return                    a value that can be used later in
     **                            {@link #of(Object)}
     **                            <br>
     **                            Possible object is {@link String}.
     */
    @Override
    public final String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a <code>Type</code> from the specified JSON
     ** object representation.
     **
     ** @param  object             the JSON objects to parse.
     **                            <br>
     **                            Must not be <code>null</code>.
     **                            <br>
     **                            Allowed object is {@link JsonObject}.
     **
     ** @return                    the parsed cryptographic <code>Type</code>.
     **                            <br>
     **                            Possible object is <code>Type</code>.
     **
     ** @throws NullPointerException     if <code>value</code> is
     **                                  <code>null</code> or.
     ** @throws IllegalArgumentException if no matching <code>Type</code> could
     **                                  be found.
     */
    public static Type from(final JsonObject object) {
      return from(JsonMarshaller.stringValue(object, KTY));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     **
     ** @throws NullPointerException     if <code>value</code> is
     **                                  <code>null</code> or.
     ** @throws IllegalArgumentException if no matching <code>Type</code> could
     **                                  be found.
     */
    public static Type from(final String value) {
      // prevent bogus input
      Objects.requireNonNull(value, "The algorithm type string must not be null");

      for (Type cursor : Type.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Usage
  // ~~~~ ~~~~~
  /**
   ** Constraint of JSON Web Key usage.
   */
  public enum Usage {
      /**
       ** The usage as Signature.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.2.</a>
       */
      SIGNATURE("sig")
      /**
       ** The usage for Encryption.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.2.</a>
       */
    , ENCRYPTION("enc")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Usage</code> with a constraint value.
     **
     ** @param  value            the constraint name of the operation.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Usage(final String value) {
      this.id = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Infers the use of the public key in the specified X.509 certificate.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** There is no standard algorithm for mapping PKIX key usage to JSON Web
     ** Key usage. See RFC 2459, section 4.2.1.3, as well as the underlying code
     ** for the chosen algorithm to infer JSON Web Key usage.
     **
     ** @param  certificate      the X.509 certificate.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link X509Certificate}.
     **
     ** @return                  the inferred JSON Web Key usage,
     **                          <code>null</code> if the usage is not specified
     **                          by the X.509 certificate or the use maps to
     **                          both {@link #SIGNATURE} and
     **                          {@link #ENCRYPTION}.
     **                          <br>
     **                          Possible object is <code>Usage</code>.
     */
    public static final Usage from( final X509Certificate certificate) {
      if (certificate.getKeyUsage() == null)
        return null;

      final Set<Usage> visited = new HashSet<>();
      // https://datatracker.ietf.org/doc/html/rfc2459#section-4.2.1.3
      // digitalSignature || nonRepudiation
      if (certificate.getKeyUsage()[0] || certificate.getKeyUsage()[1])
        visited.add(SIGNATURE);
      // digitalSignature && keyEncipherment
      // (e.g. RSA TLS certificate for authenticated encryption)
      if (certificate.getKeyUsage()[0] && certificate.getKeyUsage()[2])
        visited.add(ENCRYPTION);
      // digitalSignature && keyAgreement
      // (e.g. EC TLS certificate for authenticated encryption)
      if (certificate.getKeyUsage()[0] && certificate.getKeyUsage()[4])
        visited.add(ENCRYPTION);
      // keyEncipherment || dataEncipherment || keyAgreement
      if (certificate.getKeyUsage()[2] || certificate.getKeyUsage()[3] || certificate.getKeyUsage()[4])
        visited.add(ENCRYPTION);
      // keyCertSign || cRLSign
      if (certificate.getKeyUsage()[5] || certificate.getKeyUsage()[6])
        visited.add(SIGNATURE);
      // cannot map cert usage to singular JWK use value
      return (visited.size() == 1) ? visited.iterator().next() : null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Operation
  // ~~~~ ~~~~~~~~~
  /**
   ** Constraint of key operation.
   */
  public enum Operation implements JsonEnum<Operation, String> {
      /**
       ** Encrypt content.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
      ENCRYPT("encrypt")
      /**
       ** Decrypt content and validate decryption, if applicable.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , DECRYPT("decrypt")
      /**
       ** Compute digital signature or MAC.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , SIGNATURE("sign")
       /**
       ** Verify digital signature or MAC.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , VERIFY("verify")
      /**
       ** Encrypt key.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , WRAPKEY("wrapKey")
      /**
       ** Decrypt key and validate decryption, if applicable.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , UNWRAPKEY("unwrapKey")
      /**
       ** Derive key.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , DERIVEKEY("deriveKey")
      /**
       ** Derive bits not to be used as a key.
       ** <br>
       ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
       */
    , DERIVEBITS("deriveBits")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Operation</code> with a constraint value.
     **
     ** @param  value            the constraint name of the operation.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Operation(final String value) {
      this.id = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a proper <code>Operation</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Operation</code> constraint.
     **                          <br>
     **                          Possible object is <code>Operation</code>.
     **
     ** @throws IllegalArgumentException if no matching <code>Operation</code>
     **                                  could be found.
     */
    @Override
    public final Operation of(final String value) {
      return from(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Returns the value that identifies the <code>JsonEnum</code>
     **
     ** @return                  a value that can be used later in
     **                          {@link #of(Object)}
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String id() {
      return this.id;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Operation</code> constraint from
     ** the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Operation</code> constraint.
     **                          <br>
     **                          Possible object is <code>Operation</code>.
     **
     ** @throws IllegalArgumentException if no matching <code>Operation</code>
     **                                  could be found.
     */
    public static Operation from(final String value) {
      for (Operation cursor : Operation.values()) {
        if (cursor.id.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Builder
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Abstract <code>Builder</code> for constructing JSON Web Keys.
   ** <p>
   ** This implementation provides the mutator methods to the common JSON Web
   ** Key properties:
   ** <ul>
   **   <li>{@link #kty kty} (required)
   **   <li>{@link #use use} (optional)
   **   <li>{@link #ops key_ops} (optional)
   **   <li>{@link #x5u x5u} (optional)
   **   <li>{@link #x5t x5t} (optional)
   **   <li>{@link #x5t256 x5t#256} (optional)
   **   <li>{@link #x5c x5c} (optional)
   **   <li>{@link #keyStore} (optional)
   ** </ul>
   **
   ** @param  <B>                the type of the <code>Builder</code>
   **                            implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of <code>Builder</code>s
   **                            derived from this abstract class
   **                            (<code>Builder</code>s can return their own
   **                            specific type instead of type defined by this
   **                            abstract class only).
   **                            <br>
   **                            Allowed object is <code>&lt;B&gt;</code>.
   */
  public static abstract class Builder<B extends Builder, T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The use, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.2">RFC 7517, section 4.2.</a>
     */
    protected Usage          use;

    /**
     ** The operation, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.3">RFC 7517, section 4.3.</a>
     */
    protected Set<Operation> ops;

    /**
     ** The intended algorithm for the key, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.4">RFC 7517, section 4.4.</a>
     */
    protected Algorithm      alg;

    /**
     ** The key ID, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.5">RFC 7517, section 4.5.</a>
     */
    protected String         kid;

    /**
     ** The X.509 certificate URL, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.6">RFC 7517, section 4.6.</a>
     */
    protected URI            x5u;

    /**
     ** The X.509 certificate chain, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.7">RFC 7517, section 4.7.</a>
     */
    protected List<Encoded>  x5c;

    /**
     ** The X.509 certificate SHA-1 thumbprint, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.8">RFC 7517, section 4.8".</a>
     */
//    @Deprecated
    protected EncodedURL     x5t;

    /**
     ** The X.509 certificate SHA-256 thumbprint, optional.
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7517#section-4.9">RFC 7517, section 4.9".</a>
     */
    protected EncodedURL     x5t256;

    /**
     ** Reference to the underlying key store, optional.
     */
    protected KeyStore       keyStore;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Builder
    /**
     ** Creates a JSON Web Key builder.
     */
    protected Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: usage
    /**
     ** Sets the <code>use</code> property of the <code>JsonWebKey</code> from
     ** the JSON object representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B usage(final JsonObject object) {
      return usage(JsonWebKey.usage(object));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: usage
    /**
     ** Sets the <code>use</code> property of the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>use</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link Usage}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B usage(final Usage value) {
      this.use = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operation
    /**
     ** Sets the <code>key_ops</code> property of the <code>JsonWebKey</code>
     ** from the JSON object representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B operation(final JsonObject object) {
      return operation(JsonWebKey.operation(object));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: operation
    /**
     ** Sets the <code>key_ops</code> property of the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>key_ops</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link Usage}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B operation(final Set<Operation> value) {
      this.ops = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Sets the <code>kid</code> property of the <code>JsonWebKey</code> from
     ** the JSON object representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B identifier(final JsonObject object) {
      return identifier(JsonMarshaller.stringValue(object, SecureHeader.KID));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: identifier
    /**
     ** Sets the <code>kid</code> property of the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>kid</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link Type}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B identifier(final String value) {
      this.kid = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: algorithm
    /**
     ** Sets the <code>alg</code> property of the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>alg</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link Algorithm}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B algorithm(final Algorithm value) {
      this.alg = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: certificateURI
    /**
     ** Sets he X.509 certificate URL <code>x5u</code> of the
     ** <code>JsonWebKey</code> from the JSON object representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B certificateURI(final JsonObject object) {
      return certificateURI(Header.certificateURI(object, SecureHeader.X5U));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: certificateURI
    /**
     ** Sets he X.509 certificate URL <code>x5u</code> of the
     ** <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>x5u</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link URI}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B certificateURI(final URI value) {
      this.x5u = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sha1
    /**
     ** Sets the X.509 certificate SHA-1 thumbprint <code>x5t</code> property of
     ** the <code>JsonWebKey</code> from the JSON object representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B sha1(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, SecureHeader.X5T);
      // assume the value containes the encoded from of the data
      return sha1(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sha1
    /**
     ** Sets the X.509 certificate SHA-1 thumbprint <code>x5t</code> property of
     ** the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>x5t</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B sha1(final EncodedURL value) {
      this.x5t = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sha256
    /**
     ** Sets the X.509 certificate SHA-256 thumbprint <code>x5t#S256</code>
     ** property of the <code>JsonWebKey</code> from the JSON object
     ** representation.
     **
     ** @param  object           the JSON object representation of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link JsonObject}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    public final B sha256(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, SecureHeader.X5TS256);
      // assume the value containes the encoded from of the data
      return sha256(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: sha256
    /**
     ** Sets the X.509 certificate SHA-256 thumbprint <code>x5t#S256</code>
     ** property of the <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>x5t#S256</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B sha256(final EncodedURL value) {
      this.x5t256 = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: chain
    /**
     ** Sets the X.509 certificate chain <code>x5c</code> property of the
     ** <code>JsonWebKey</code>.
     **
     ** @param  value            the <code>x5c</code> property of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type of {@link Encoded}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B chain(final List<Encoded> value) {
      this.x5c = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: keyStore
    /**
     ** Sets the underlying key store of the <code>JsonWebKey</code>.
     **
     ** @param  value            the underlying key store of the
     **                          <code>JsonWebKey</code>.
     **                          <br>
     **                          Allowed object is {@link KeyStore}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     */
    @SuppressWarnings({"cast", "unchecked"})
    public final B keyStore(final KeyStore value) {
      this.keyStore = value;
      return (B)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: thumbprint
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
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>B</code>.
     **
     ** @throws JoseException    if the SHA-256 hash algorithm is not supported.
     */
    public B thumbprint()
      throws JoseException {

      return thumbprint("SHA-256");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: thumbprint
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
     **                          Possible object is <code>B</code>.
     **
     ** @throws JoseException    if the SHA-256 hash algorithm is not supported.
     */
    public abstract B thumbprint(final String algorithm)
      throws JoseException;

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a {@link JsonWebKey} with the properties
     ** configured.
     **
     ** @return                  the created {@link JsonWebKey} populated with
     **                          the properties configured.
     **                          <br>
     **                          Possible object is <code>T</code>
     **
     ** @throws IllegalStateException if the JSON Web Key properties were
     **                               inconsistently specified.
     */
    public abstract T build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>JsonWebKey</code> from the specified
   ** <code>JsonWebKey</code>.
   **
   ** @param  other              the <code>JsonWebKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>JsonWebKey</code> of type
   **                            <code>T</code>.
   */
  protected JsonWebKey(final JsonWebKey<T> other) {
    this(other.kty, other.use, other.ops, other.kid, other.alg, other.x5u, other.x5t, other.x5t256, other.x5c, other.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>JsonWebKey</code> with the specified parameters.
   **
   ** @param  kty                the algorithm type.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  use                the key use or <code>null</code> if not
   **                            specified or if the key is intended for
   **                            signing as well as encryption.
   **                            <br>
   **                            Allowed object is {@link Usage}.
   ** @param  ops                the permitted key operations or
   **                            <code>null</code> if not specified or if
   **                            the key is intended for all operations.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Operation}.
   ** @param  kid                the key id <code>null</code> if not specified.
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
   */
  protected JsonWebKey(final Type kty, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.kty         = Objects.requireNonNull(kty, "The algorithm type must not be null");
    this.use         = use;
    this.ops         = CollectionUtility.unmodifiable(ops);
    this.kid         = kid;
    this.alg         = alg;
    this.x5u         = x5u;
    this.x5t         = x5t;
    this.x5t256      = x5t256;
    this.x5c         = x5c;
    this.certificate = (x5c == null || x5c.size() == 0) ? null : parse(x5c);
    this.keyStore    = keyStore;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  /**
   ** Returns a JSON object representation of this <code>JsonWebKey</code>.
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
    final JsonObjectBuilder builder = Json.createObjectBuilder().add(KTY, this.kty.id);
    if (this.use != null)
      builder.add(USE, this.use.id);
    if (this.ops != null) {
      final JsonArrayBuilder array = Json.createArrayBuilder();
      for (Operation cursor : this.ops) {
        array.add(cursor.id);
      }
      builder.add(OPS, array);
    }
    if (this.alg != null)
      builder.add(SecureHeader.ALG, this.alg.id);
    if (this.kid != null)
      builder.add(SecureHeader.KID, this.kid);
    if (this.x5u != null)
      builder.add(SecureHeader.X5U, this.x5u.toString());
    if (this.x5t != null)
      builder.add(SecureHeader.X5T, this.x5t.toString());
    if (this.x5t256 != null)
      builder.add(SecureHeader.X5TS256, this.x5t256.toString());
    if (this.x5c != null) {
      final JsonArrayBuilder array = Json.createArrayBuilder();
      for (Encoded cursor : this.x5c) {
        array.add(cursor.toString());
      }
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a <code>JsonWebKey</code> from the specified string representation.
   ** <br>
   ** The <code>JsonWebKey</code> must be resolvable to an {@link EllipticKey}
   ** or a {@link RSAKey}.
   **
   ** @param  value              the JSON object string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>JsonWebKey</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKey</code>.
   **
   ** @throws NullPointerException if <code>value</code> is <code>null</code> or
   **                              empty.
   */
  public static JsonWebKey from(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    return from(JsonMarshaller.readObject(Objects.requireNonNull(value, "The Json Object string must not be null")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parses a <code>JsonWebKey</code> from the specified {@link JsonObject}
   ** representation.
   ** <br>
   ** The <code>JsonWebKey</code> must be an {@link EllipticKey} or an
   ** {@link RSAKey}.
   **
   ** @param  object             the JSON object to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the parsed <code>JsonWebKey</code>.
   **                            <br>
   **                            Possible object is <code>JsonWebKey</code>.
   **
   ** @throws NullPointerException if <code>object</code> is <code>null</code>
   **                              or empty.
   */
  public static JsonWebKey from(final JsonObject object)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(object, "The Json Object must not be null");
    switch (Type.from(JsonMarshaller.stringValue(object, KTY))) {
      case EC  : return EllipticKey.from(object);
      case RSA : return RSAKey.from(object);
      case OCT : return OctetKey.from(object);
      case OKP : return OctetPair.from(object);
      // can never happens, but syntactically required
      default  : return null;
    }
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
    return Objects.hash(this.kty, this.use, this.ops, this.alg, this.kid, this.x5u, this.x5t, this.x5t256, this.x5c, this.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>JsonWebKey</code>s values are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>JsonWebKey</code>s values may be different even though they contain
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

    if (!(other instanceof JsonWebKey))
      return false;

    if (!super.equals(other))
      return false;

    final JsonWebKey that = (JsonWebKey)other;
    return Objects.equals(this.kty,      that.kty)
        && Objects.equals(this.use,      that.use)
        && Objects.equals(this.ops,      that.ops)
        && Objects.equals(this.alg,      that.alg)
        && Objects.equals(this.kid,      that.kid)
        && Objects.equals(this.x5u,      that.x5u)
        && Objects.equals(this.x5t,      that.x5t)
        && Objects.equals(this.x5t256,   that.x5t256)
        && Objects.equals(this.x5c,      that.x5c)
        && Objects.equals(this.keyStore, that.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>JsonWebKey</code> in its
   ** minimal form, without any additional whitespace.
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
   ** @return                    a string representation that represents this
   **                            literal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return jsonify().build().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredParameter
  /**
   ** Computes the thumbprint for the specified required JWK parameters.
   **
   ** @param  algorithm          the hash algorithm.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            <br>
   **                            Allowed object is  {@link String}.
   ** @param  parameter          the required JSON Web Key parameters, sorted
   **                            alphanumerically by key name and ready for JSON
   **                            serialisation.
   **                            <br>
   **                            Possible object is {@link LinkedHashMap} where
   **                            each element is of type {@link String} as the
   **                            key and {@link String} for the value.
   **
   ** @return                    the JSON Web Key (JWK) thumbprint.
   **                            <br>
   **                            Possible object is  {@link EncodedURL}.
   **
   ** @throws JoseException      if the hash algorithm is not supported.
   */
  public static EncodedURL compute(final String algorithm, final LinkedHashMap<String,?> parameter)
    throws JoseException {

    final String        json  = JsonMarshaller.serialize(parameter);
    final MessageDigest digest;
    try {
      digest = MessageDigest.getInstance(algorithm);
    }
    catch (NoSuchAlgorithmException e) {
      throw new JoseException("Couldn't compute JWK thumbprint: Unsupported hash algorithm: " + e.getMessage(), e);
    }
    digest.update(json.getBytes(StandardCharsets.UTF_8));
    return EncodedURL.of(digest.digest());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sensitive
  /**
   ** Returns <code>true</code> if this JSON Web Key contains sensitive or
   ** private key material.
   **
   ** @return                    <code>true</code> if this JWK contains
   **                            sensitive key material; otherwise
   **                            <code>false</code>.
   */
  public abstract boolean sensitive();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requiredParameter
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
  public abstract LinkedHashMap<String,?> requiredParameter();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Parses a key usage (<code>use</code>) parameter from the specified
   ** {@link JsonObject} representation of a <code>JsonWebKey</code>.
   ** <p>
   ** Allowed values are:
   ** <ul>
   **   <li>sig - signatures or MAC
   **   <li>enc - encryption
   ** </ul>
   **
   ** @param  object             the {@link JsonObject} to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object id {@link JsonObject}.
   **
   ** @return                    the key use or <code>null</code> if not
   **                            specified.
   **                            <br>
   **                            Possible object is {@link Usage}.
   **
   ** @throws IllegalArgumentException if <code>use</code> is <code>null</code>
   **                                  or empty or does not match.
   */
  static Usage usage(final JsonObject object)
    throws IllegalArgumentException {

    if (!object.containsKey(USE))
      return null;

    final String use = object.getString(USE);
    // sig - signatures or MAC
    // enc - encryption
    if (Usage.SIGNATURE.id.equals(use))
      return Usage.SIGNATURE;
    else if (Usage.ENCRYPTION.id.equals(use))
      return Usage.ENCRYPTION;
    else
      throw new IllegalArgumentException("Invalid or unsupported key use \"use\" parameter, must be \"sig\" or \"enc\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operation
  /**
   ** Parses a key operation (<code>key_ops</code>) parameter from the specified
   ** {@link JsonObject} representation of a <code>JsonWebKey</code>.
   ** <p>
   ** Allowed values are:
   ** <ul>
   **   <li>sign       - compute digital signature or MAC
   **   <li>verify     - verify digital signature
   **   <li>encrypt    - encrypt content
   **   <li>decrypt    - decrypt content
   **   <li>wrapKey    - encrypt key
   **   <li>unwrapKey  - decrypt key
   **   <li>deriveKey  - ederive key
   **   <li>deriveBits - derive bits not to be used as a key
   ** </ul>
   **
   ** @param  object             the {@link JsonObject} to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object id {@link JsonObject}.
   **
   ** @return                    the permitted key operations or
   **                            <code>null</code> if not specified or if
   **                            the key is intended for all operations.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Operation}.
   **
   ** @throws IllegalArgumentException if <code>key_ops</code> is
   **                                  <code>null</code> or empty or does not
   **                                  match.
   */
  static Set<Operation> operation(final JsonObject object)
    throws IllegalArgumentException {

    if (!object.containsKey(OPS))
      return Collections.<Operation>emptySet();

    final JsonArray o = object.getJsonArray(OPS);
    if (o.size() == 0)
      return Collections.<Operation>emptySet();

    final Set<Operation> c = new LinkedHashSet<Operation>();
    // sign       - compute digital signature or MAC
    // verify     - verify digital signature
    // encrypt    - encrypt content
    // decrypt    - decrypt content
    // wrapKey    - encrypt key
    // unwrapKey  - decrypt key
    // deriveKey  - ederive key
    // deriveBits - derive bits not to be used as a key
    o.forEach(
      i -> {
        if (!(i.getValueType() == JsonValue.ValueType.STRING))
          throw new IllegalArgumentException("The \"key_ops\" JSON array must contain JSON strings only");

        try {
          c.add(Operation.from(((JsonString)i).getString()));
        }
        catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Invalid key_ops: " + e.getMessage());
        }
      }
    );
    return c;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses a X.509 certificate chain from the specified DER-encoded
   ** representation.
   ** <br>
   ** PEM-encoded objects that are not X.509 certificates ignored.
   ** Requires BouncyCastle.
   **
   ** @param  value              the PEM-encoded X.509 certificate chain string.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type of {@link Encoded}.
   **
   ** @return                    the X.509 certificate chain, <code>null</code>
   **                            if no certificates are found.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type of {@link X509Certificate}.
   */
  static List<X509Certificate> parse(final List<Encoded> value) {
    final DER.Decoder       decoder = DER.decoder();
    return value.stream().map(e -> decoder.read(e.decode()).certificate()).collect(Collectors.toList());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses a X.509 certificate chain from the specified PEM-encoded
   ** representation.
   ** <br>
   ** PEM-encoded objects that are not X.509 certificates ignored.
   ** Requires BouncyCastle.
   **
   ** @param  file               the PEM-encoded X.509 certificate chain file.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object id {@link File}.
   **
   ** @return                    the X.509 certificate chain, <code>null</code>
   **                            if no certificates are found.
   **                            <br>
   **                            Possible object is array of
   **                            {@link X509Certificate}.
   **
   ** @throws IOException          if an I/O error occured.
   ** @throws CertificateException if a certificate error occured.
   */
  @SuppressWarnings("unused")
  static X509Certificate[] parse(final File file) {
    return new X509Certificate[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses a X.509 certificate chain from the specified PEM-encoded
   ** representation.
   ** <br>
   ** PEM-encoded objects that are not X.509 certificates ignored.
   **
   ** @param  alue               the PEM-encoded X.509 certificate chain string.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object id {@link String}.
   **
   ** @return                    the X.509 certificate chain, <code>null</code>
   **                            if no certificates are found.
   **                            <br>
   **                            Possible object is array of
   **                            {@link X509Certificate}.
   **
   ** @throws IOException          if an I/O error occured.
   ** @throws CertificateException if a certificate error occured.
   */
  @SuppressWarnings("unused")
  static X509Certificate[] parse(final String value) {
    return new X509Certificate[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Loads a JSON Web Key (JWK) from the specified JCE key store.
   ** <br>
   ** The JWK can be a public/private {@link RSAKey RSA key}, a public/private
   ** {@link EllipticKey EC key}, or a {@link OctetKey secret key}.
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
   ** @return                    the public/private RSA or EC JSON Web Key
   **                            (JWK), or secret JSON Web Key (JWK), or
   **                            <code>null</code> if no key with the specified
   **                            alias was found.
   **
   ** @throws JoseException      if RSA or EC key loading failed.
   */
  public static JsonWebKey load(final KeyStore store, final String alias, final char[] secret)
    throws JoseException {

    try {
      java.security.cert.Certificate cert = store.getCertificate(alias);
      if (cert == null) {
        // try secret key
        return OctetKey.load(store, alias, secret);
      }
      else if (cert.getPublicKey() instanceof RSAPublicKey) {
        return null;
      }
      else if (cert.getPublicKey() instanceof ECPublicKey) {
        return null;
      }
      else {
        throw new JoseException("Unsupported public key algorithm: " + cert.getPublicKey().getAlgorithm());
      }
    }
    catch (KeyStoreException e) {
      throw new JoseException("ORA-00600", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Computes the SHA-256 thumbprint for this JSON Web Key (JWK).
   **
   ** @return                    the JSON Web Key (JWK) thumbprint.
   **                            <br>
   **                            Possible object is  {@link EncodedURL}.
   **
   ** @throws JoseException      if the SHA-256 hash algorithm is not
   **                            supported.
   */
  public EncodedURL compute()
    throws JoseException {

    return compute("SHA-256");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute
  /**
   ** Computes the thumbprint for this JSON Web Key (JWK).
   **
   ** @param  algorithm          the hash algorithm.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            <br>
   **                            Allowed object is  {@link String}.
   **
   ** @return                    the JSON Web Key (JWK) thumbprint.
   **                            <br>
   **                            Possible object is  {@link EncodedURL}.
   **
   ** @throws JoseException      if the SHA-256 hash algorithm is not
   **                            supported.
   */
  public EncodedURL compute(final String algorithm)
    throws JoseException {

    return compute(algorithm, requiredParameter());
  }
}