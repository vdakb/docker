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

    File        :   RSAKey.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RSAKey.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.Serializable;

import java.math.BigInteger;

import java.net.URI;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.MessageDigest;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateEncodingException;

import java.security.spec.RSAPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAOtherPrimeInfo;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAMultiPrimePrivateCrtKeySpec;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAMultiPrimePrivateCrtKey;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// final class RSAKey
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** Public  and private {@link Type#RSA RSA}
 ** {@link JsonWebKey JSON Web Key (JWK)}.
 ** <p>
 ** Provides RSA JWK import from / export to the following standard Java
 ** interfaces and classes:
 ** <ul>
 **   <li>{@link java.security.interfaces.RSAPublicKey}
 **   <li>{@link java.security.interfaces.RSAPrivateKey}
 **     <ul>
 **       <li>{@link java.security.interfaces.RSAPrivateCrtKey}
 **       <li>{@link java.security.interfaces.RSAMultiPrimePrivateCrtKey}
 **     </ul>
 **   <li>{@link java.security.PrivateKey} for an RSA key in a PKCS#11 store
 **   <li>{@link java.security.KeyPair}
 ** </ul>
 ** <p>
 ** Example JSON object representation of a public RSA JWK:
 ** <pre>
 **   { "kty" : "RSA"
 **   , "n"   : "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx
 **              4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMs
 **              tn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2
 **              QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbI
 **              SD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqb
 **              w0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw"
 **   , "e"   : "AQAB"
 **   , "alg" : "RS256"
 **   , "kid" : "4711"
 **   }
 ** </pre>
 ** <p>
 ** Example JSON object representation of a public and private RSA JWK (with
 ** both the first and the second private key representations):
 ** <pre>
 **   { "kty" : "RSA"
 **   , "n"   : "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx
 **              4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMs
 **              tn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2
 **              QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbI
 **              SD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqb
 **              w0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw"
 **   , "e"   : "AQAB"
 **   , "d"   : "X4cTteJY_gn4FYPsXB8rdXix5vwsg1FLN5E3EaG6RJoVH-HLLKD9
 **              M7dx5oo7GURknchnrRweUkC7hT5fJLM0WbFAKNLWY2vv7B6NqXSzUvxT0_YSfqij
 **              wp3RTzlBaCxWp4doFk5N2o8Gy_nHNKroADIkJ46pRUohsXywbReAdYaMwFs9tv8d
 **              _cPVY3i07a3t8MN6TNwm0dSawm9v47UiCl3Sk5ZiG7xojPLu4sbg1U2jx4IBTNBz
 **              nbJSzFHK66jT8bgkuqsk0GjskDJk19Z4qwjwbsnn4j2WBii3RL-Us2lGVkY8fkFz
 **              me1z0HbIkfz0Y6mqnOYtqc0X4jfcKoAC8Q"
 **   , "p"   : "83i-7IvMGXoMXCskv73TKr8637FiO7Z27zv8oj6pbWUQyLPQBQxtPV
 **              nwD20R-60eTDmD2ujnMt5PoqMrm8RfmNhVWDtjjMmCMjOpSXicFHj7XOuVIYQyqV
 **              WlWEh6dN36GVZYk93N8Bc9vY41xy8B9RzzOGVQzXvNEvn7O0nVbfs"
 **   , "q"   : "3dfOR9cuYq-0S-mkFLzgItgMEfFzB2q3hWehMuG0oCuqnb3vobLyum
 **              qjVZQO1dIrdwgTnCdpYzBcOfW5r370AFXjiWft_NGEiovonizhKpo9VVS78TzFgx
 **              kIdrecRezsZ-1kYd_s1qDbxtkDEgfAITAG9LUnADun4vIcb6yelxk"
 **   , "dp"  : "G4sPXkc6Ya9y8oJW9_ILj4xuppu0lzi_H7VTkS8xj5SdX3coE0oim
 **              YwxIi2emTAue0UOa5dpgFGyBJ4c8tQ2VF402XRugKDTP8akYhFo5tAA77Qe_Nmtu
 **              YZc3C3m3I24G2GvR5sSDxUyAN2zq8Lfn9EUms6rY3Ob8YeiKkTiBj0"
 **   , "dq"  : "s9lAH9fggBsoFR8Oac2R_E2gw282rT2kGOAhvIllETE1efrA6huUU
 **              vMfBcMpn8lqeW6vzznYY5SSQF7pMdC_agI3nG8Ibp1BUb0JUiraRNqUfLhcQb_d9
 **              GF4Dh7e74WbRsobRonujTYN1xCaP6TO61jvWrX-L18txXw494Q_cgk"
 **   , "qi"  : "GyM_p6JrXySiz1toFgKbWV-JdI3jQ4ypu9rbMWx3rQJBfmt0FoYzg
 **              UIZEVFEcOqwemRN81zoDAaa-Bk0KWNGDjJHZDdDmFhW3AN7lI-puxk_mHZGJ11rx
 **              yR8O55XLSe3SPmRfKwZI6yU24ZxvQKFYItdldUKGzO6Ia6zTKhAVRU"
 **   , "alg" : "RS256"
 **   , "kid" : "4711"
 **   }
 ** </pre>
 ** <p>
 ** Use the builder to create a new RSA JWK:
 ** <pre>
 **   RSAKey key = RSAKey.Builder.of(n, e)
 **    .identifier("4711")
 **    .usage(KeyUse.SIGNATURE)
 **    .build();
 ** </pre>
 ** <p>
 ** @see <a href="https://datatracker.ietf.org/doc/html/rfc3447">RFC 3447</a>
 ** @see <a href="https://en.wikipedia.org/wiki/RSA_%28cryptosystem%29">RSA (cryptosystem)</a>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class RSAKey extends    JsonWebKey
                          implements Asymmetric {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** JWK parameter for public key modulus.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.1.1">RFC 7518, section 6.3.1.1. "n" (Modulus) Parameter</a>
   */
  public static final String MOD              = "n";

  /**
   ** JWK parameter for public key exponent.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.1.2">RFC 7518, section 6.3.1.2 "e" (Exponent) Parameter.</a>
   */
  public static final String EXP              = "e";

  /**
   ** JWK parameter for RSA private key first prime factor.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.1">RFC 7518, section 6.3.2.1 "d" (Private Exponent) Parameter</a>
   */
  public static final String PXP              = "d";

  /**
   ** JWK parameter for RSA private key first prime factor.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.2">RFC 7518, section 6.3.2.2 "p" (First Prime Factor) Parameter</a>
   */
  public static final String FPF              = "p";

  /**
   ** JWK parameter for RSA private key second prime factor.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.3">RFC 7518, section 6.3.2.3 "q" (Second Prime Factor) Parameter</a>
   */
  public static final String SPF              = "q";

  /**
   ** JWK parameter for Chinese Remainder Theorem (CRT) first factor exponent.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.4">RFC 7518, section 6.3.2.4 "dp" (First Factor CRT Exponent) Parameter</a>
   */
  public static final String CDP              = "dp";

  /**
   ** JWK parameter for Chinese Remainder Theorem (CRT) second factor exponent.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.5">RFC 7518, section 6.3.2.5 "dq" (Second Factor CRT Exponent) Parameter</a>
   */
  public static final String CDQ              = "dq";

  /**
   ** JWK parameter for Chinese Remainder Theorem (CRT) first coefficient.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.6">RFC 7518, section 6.3.2.6 "qi" (First CRT Coefficient) Parameter</a>
   */
  public static final String CQI              = "qi";

  /**
   ** JWK parameter for RSA Other Primes Info.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.7">RFC 7518, section 6.3.2.7 "oth" (Other Primes Info) Parameter</a>
   */
  public static final String OTH              = "oth";

  /**
   ** JWK parameter for RSA Other Primes Info prime factor.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.7.1">RFC 7518, section 6.3.2.7.1 "r" (Other Primes Info - Prime Factor)</a>
   */
  public static final String OTR              = "r";

  /**
   ** JWK parameter for RSA Other Primes Info factor CRT exponent.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.7.2">RFC 7518, section 6.3.2.7.2 "d" (Other Primes Info - (Factor CRT Exponent))</a>
   */
  public static final String OTD              = "d";

  /**
   ** JWK parameter for RSA Other Primes Info factor CRT coeffcient.
   ** <br>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-6.3.2.7.3">RFC 7518, section 6.3.2.7.3 "t" (Other Primes Info - (Factor CRT Coefficient))</a>
   */
  public static final String OTT              = "t";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:936509975723675382")
  private static final long  serialVersionUID = -2877065846909915769L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The modulus value for the RSA public key. */
  public final EncodedURL    n;

  /** The exponent value for the RSA public key. */
  public final EncodedURL    e;

  /** The exponent value for the RSA private key. */
  public final EncodedURL    d;

  /** The first prime factor value for the RSA private key. */
  public final EncodedURL    p;

  /** The second prime factor value for the RSA private key. */
  public final EncodedURL    q;

  /**
   ** The first factor Chinese Remainder Theorem exponent of the RSA private
   ** key.
   */
  public final EncodedURL    dp;

  /**
   ** The second factor Chinese Remainder Theorem exponent of the RSA private
   ** key.
   */
  public final EncodedURL    dq;

  /**
   ** The first factor Chinese Remainder Theorem coefficient of the RSA
   ** private key.
   */
  public final EncodedURL    qi;

  /**
   ** The other prime information of the RSA private key, should they exist.
   ** <br>
   ** When only two primes have been used (the normal case), this parameter
   ** <b>MUST</b> be omitted.
   ** <br>
   ** When three or more primes have been used, the number of array elements
   ** <b>MUST</b> be the number of primes used minus two.
   */
  public final List<Other>   oth;

  /**
   ** Private PKCS#11 key handle.
   */
  public final PrivateKey    pkcs;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Builder for constructing RSA {@link JsonWebKey JSON Web Key (JWK)}.
   ** <p>
   ** Example usage:
   ** <pre>
   **   final RSAKey key = RSAKey.Builder.of(n, e)
   **     .identifier("456")
   **     .privateExponent(d)
   **     .algorithm(Algorithm.RS512)
   **     .build()
   **   ;
   ** </pre>
   */
  public static final class Builder extends JsonWebKey.Builder<Builder, RSAKey> {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    /** The modulus value for the RSA public key. */
    private final EncodedURL n;

    /** The exponent value for the RSA public key. */
    private final EncodedURL e;

    /** The exponent of the RSA private key. */
    private EncodedURL       d;

    /** The first prime factor of the RSA private key. */
    private EncodedURL       p;

    /** The second prime factor of the RSA private key. */
    private EncodedURL       q;

    /**
     ** The first factor Chinese Remainder Theorem exponent of the RSA private
     ** key.
     */
    private EncodedURL       dp;

    /**
     ** The second factor Chinese Remainder Theorem exponent of the RSA private
     ** key.
     */
    private EncodedURL       dq;

    /**
     ** The first factor Chinese Remainder Theorem coefficient of the RSA
     ** private key.
     */
    private EncodedURL       qi;

    /**
     ** The other primes information of the private RSA key, should they exist.
     ** <br>
     ** When only two primes have been used (the normal case), this parameter
     ** <b>MUST</b> be omitted.
     ** <br>
     ** When three or more primes have been used, the number of array elements
     ** <b>MUST</b> be the number of primes used minus two.
     */
    private List<Other>      oth;

    /** The private RSA key, as PKCS#11 handle, optional. */
    private PrivateKey       pkcs;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Builder
    /**
     ** Constructs a new RSA JWK builder.
     **
     ** @param  n                the modulus value for the RSA public key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  e                the exponent value for the RSA public key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws NullPointerException if either <code>n</code> or <code>e</code>
     **                              is <code>null</code>.
     */
    private Builder(final EncodedURL n, final EncodedURL e) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.n = Objects.requireNonNull(n, "The modulus value must not be null");
      this.e = Objects.requireNonNull(e, "The public exponent value must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateExponent
    /**
     ** Sets the private exponent <code>d</code> of the RSA key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder privateExponent(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, PXP);
      // assume the object containes the encoded from of the data
      return privateExponent(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateExponent
    /**
     ** Sets the private exponent <code>d</code> of the RSA key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the private exponent <code>d</code> of the RSA
     **                          key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder privateExponent(final EncodedURL value) {
      this.d = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeP
    /**
     ** Sets first prime factor <code>p</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder primeP(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, FPF);
      // assume the object containes the encoded from of the data
      return primeP(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeP
    /**
     ** Sets first prime factor <code>p</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the first prime factor <code>p</code> of the
     **                          RSA private key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder primeP(final EncodedURL value) {
      this.p = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeQ
    /**
     ** Sets second prime factor <code>q</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder primeQ(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, SPF);
      // assume the object containes the encoded from of the data
      return primeQ(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeQ
    /**
     ** Sets second prime factor <code>q</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the second prime factor <code>q</code> of the
     **                          RSA private key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder primeQ(final EncodedURL value) {
      this.q = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeExponentP
    /**
     ** Sets the first factor Chinese Remainder Theorem (CRT) exponent
     ** <code>dp</code> of the RSA private key.
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder primeExponentP(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, CDP);
      // assume the object containes the encoded from of the data
      return primeExponentP(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeExponentP
    /**
     ** Sets the first factor Chinese Remainder Theorem (CRT) exponent
     ** <code>dp</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the first factor Chinese Remainder Theorem
     **                          (CRT) exponent <code>dp</code> of the RSA
     **                          private key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder primeExponentP(final EncodedURL value) {
      this.dp = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeExponentQ
    /**
     ** Sets the second factor Chinese Remainder Theorem (CRT) exponent
     ** <code>dq</code> of the RSA private key.
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder primeExponentQ(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, CDQ);
      // assume the object containes the encoded from of the data
      return primeExponentQ(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeExponentQ
    /**
     ** Sets the second factor Chinese Remainder Theorem (CRT) exponent
     ** <code>dq</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the second factor Chinese Remainder Theorem
     **                          (CRT) exponent <code>dq</code> of the RSA
     **                          private key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder primeExponentQ(final EncodedURL value) {
      this.dq = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeCoefficientQI
    /**
     ** Sets the second factor Chinese Remainder Theorem (CRT) exponent
     ** <code>qi</code> of the RSA private key.
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
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
    public final Builder primeCoefficientQI(final JsonObject object) {
      final String value = JsonMarshaller.stringValue(object, CQI);
      // assume the object containes the encoded from of the data
      return primeCoefficientQI(value == null ? null : EncodedURL.raw(value));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: primeCoefficientQI
    /**
     ** Sets the first Chinese Remainder Theorem (CRT) coefficient
     ** <code>qi</code> of the RSA private key.
     ** <p>
     ** It is represented as {@link EncodedURL} value encoding of the value's
     ** big endian representation.
     **
     ** @param  value            the first Chinese Remainder Theorem (CRT)
     **                          coefficient <code>qi</code> of the RSA key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder primeCoefficientQI(final EncodedURL value) {
      this.qi = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: otherFactor
    /**
     ** Sets the other primes information <code>oth</code> of the RSA private
     ** key.
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
    public final Builder otherFactor(final JsonObject object) {
      if (object.containsKey(OTH)) {
        final JsonArray array = object.getJsonArray(OTH);
        if (array != null) {
          this.oth = new ArrayList<>(array.size());
          for (Object cursor : array) {
            if (cursor instanceof JsonObject) {
              final JsonObject other = (JsonObject)cursor;
              this.oth.add(new Other(EncodedURL.raw(other.getString(OTR)), EncodedURL.raw(other.getString(OTD)), EncodedURL.raw(other.getString(OTT))));
            }
          }
        }
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: otherFactor
    /**
     ** Sets the other primes information <code>oth</code> of the RSA private
     ** key.
     **
     ** @param  value            the other primes information <code>oth</code>
     **                          of the RSA.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Other}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder otherFactor(final List<Other> value) {
      this.oth = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the private RSA key, typically for a key located in a
     **
     ** @param  value            the {@link PrivateKey} reference.
     **                          <br>
     **                          Its algorithm must be <b>RSA</b>.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link PrivateKey}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder privateKey(final PrivateKey value) {
      if (value instanceof RSAPrivateKey)
        return privateKey((RSAPrivateKey)value);

      if (!JsonWebKey.Type.RSA.id.equalsIgnoreCase(value.getAlgorithm()))
        throw new IllegalArgumentException("The private key algorithm must be RSA");

      this.pkcs = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the {@link PrivateKey} of the RSA private key using the first
     ** representation.
     **
     ** @param  value            the {@link RSAPrivateKey} of the RSA key, used
     **                          to obtain the private exponent <code>d</code>.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPrivateKey}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder privateKey(final RSAPrivateKey value) {
      if (value instanceof RSAPrivateCrtKey) {
        return privateKey((RSAPrivateCrtKey)value);
      }
      else if (value instanceof RSAMultiPrimePrivateCrtKey) {
        return this.privateKey((RSAMultiPrimePrivateCrtKey)value);
      }
      else {
        this.d = EncodedURL.of(value.getPrivateExponent());
        return this;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the {@link PrivateKey} of the RSA private key using the second
     ** representation.
     ** <br>
     ** (see RFC 3447, section 3.2)
     **
     ** @param  value            the {@link RSAPrivateCrtKey} of the RSA key,
     **                          used to obtain:
     **                          <ol>
     **                            <li>the private exponent (<code>d</code>)
     **                            <li>the first prime factor (<code>p</code>)
     **                            <li>the second prime factor (<code>q</code>)
     **                            <li>the first factor CRT exponent
     **                                (<code>dp</code>)
     **                            <li>the second factor CRT exponent
     **                                (<code>dq</code>)
     **                            <li>the first CRT coefficient
     **                                (<code>qi</code>)
     **                          </ol>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPrivateCrtKey}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder privateKey(final RSAPrivateCrtKey value) {
      this.d  = EncodedURL.of(value.getPrivateExponent());
      this.p  = EncodedURL.of(value.getPrimeP());
      this.q  = EncodedURL.of(value.getPrimeQ());
      this.dp = EncodedURL.of(value.getPrimeExponentP());
      this.dq = EncodedURL.of(value.getPrimeExponentQ());
      this.qi = EncodedURL.of(value.getCrtCoefficient());
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: privateKey
    /**
     ** Sets the {@link PrivateKey} of the RSA private key using the second
     ** representation with optional other primes info.
     ** <br>
     ** (see RFC 3447, section 3.2)
     **
     ** @param  value            the {@link RSAMultiPrimePrivateCrtKey} of the
     **                          RSA key, used to obtain:
     **                          <ol>
     **                            <li>the private exponent (<code>d</code>)
     **                            <li>the first prime factor (<code>p</code>)
     **                            <li>the second prime factor (<code>q</code>)
     **                            <li>the first factor CRT exponent
     **                                (<code>dp</code>)
     **                            <li>the second factor CRT exponent
     **                                (<code>dq</code>)
     **                            <li>the first CRT coefficient
     **                                (<code>qi</code>)
     **                            <li>the other primes info (<code>oth</code>)
     **                          </ol>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is
     **                          {@link RSAMultiPrimePrivateCrtKey}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder privateKey(final RSAMultiPrimePrivateCrtKey value) {
      this.d   = EncodedURL.of(value.getPrivateExponent());
      this.p   = EncodedURL.of(value.getPrimeP());
      this.q   = EncodedURL.of(value.getPrimeQ());
      this.dp  = EncodedURL.of(value.getPrimeExponentP());
      this.dq  = EncodedURL.of(value.getPrimeExponentQ());
      this.qi  = EncodedURL.of(value.getCrtCoefficient());
      this.oth = Other.from(value.getOtherPrimeInfo());
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
      final LinkedHashMap<String, String> required = new LinkedHashMap<>();
      required.put(EXP, this.e.toString());
      required.put(KTY, Type.RSA.id);
      required.put(MOD, this.n.toString());
      this.kid = compute(algorithm, required).toString();
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (JsonWebKey.Builder)
    /**
     ** Factory method to create a {@link RSAKey} with the properties
     ** configured.
     **
     ** @return                  the created {@link RSAKey} populated with the
     **                          properties configured.
     **                          <br>
     **                          Possible object is {@link RSAKey}
     **
     ** @throws IllegalStateException if the JSON Web Key properties were
     **                               inconsistently specified.
     */
    @Override
    public final RSAKey build() {
      return new RSAKey(this.n, this.e, this.d, this.p, this.q, this.dp, this.dq, this.qi, this.oth, this.use, this.ops, this.kid, this.alg, this.x5u, this.x5t, this.x5t256, this.x5c, this.keyStore);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link RSAKey} <code>Builder</code> with
     ** the specified.
     **
     ** @param  publicKey        the RSA public key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPublicKey}.
     ** @param  privateKey       the RSA private key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPrivateKey}.
     **
     ** @return                  the created @link RSAKey} <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>curve</code>,
     **                              <code>x</code> or <code>y</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final RSAPublicKey publicKey, final RSAPrivateKey privateKey) {
      // prevent bogus input
      return of(publicKey).privateExponent(EncodedURL.of(privateKey.getPrivateExponent()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link RSAKey} <code>Builder</code> with
     ** the specified.
     **
     ** @param  publicKey        the RSA public key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPublicKey}.
     ** @param  privateKey       the RSA private key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link PrivateKey}.
     **
     ** @return                  the created @link RSAKey} <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>curve</code>,
     **                              <code>x</code> or <code>y</code> is
     **                              <code>null</code>.
     */
    public static Builder of(final RSAPublicKey publicKey, final PrivateKey privateKey) {
      // prevent bogus input
      return of(publicKey).privateKey(privateKey);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link RSAKey} <code>Builder</code> with
     ** the specified.
     **
     ** @param  publicKey        the RSA public key to represent.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPublicKey}.
     **
     ** @return                  the created @link RSAKey} <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public static Builder of(final RSAPublicKey publicKey) {
      return new Builder(
        EncodedURL.of(publicKey.getModulus())
      , EncodedURL.of(publicKey.getPublicExponent())
      );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link RSAKey} <code>Builder</code> with
     ** the specified {@link RSAKey} as a template.
     **
     ** @param  other            the {@link RSAKey} template.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAKey}.
     **
     ** @return                  the created {@link RSAKey} <code>Builder</code>
     **                          populated with the values provided by the
     **                          {@link RSAKey} template <code>other</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>other</code> is
     **                              <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    public static Builder of(final RSAKey other) {
      // prevent bogus input
      return of(other.n, other.e)
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
        .privateExponent(other.d)
        .primeP(other.p)
        .primeQ(other.q)
        .primeExponentP(other.dp)
        .primeExponentQ(other.dq)
        .primeCoefficientQI(other.qi)
        .otherFactor(other.oth)
        ;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a {@link RSAKey} <code>Builder</code> with
     ** the specified.
     **
     ** @param  n                the modulus value for the RSA public key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  e                the exponent value for the RSA public key.
     **                          <br>
     **                          It is  represented as {@link EncodedURL} value
     **                          encoding value's big endian representation.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the created @link RSAKey} <code>Builder</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if either <code>n</code> or <code>e</code>
     **                              is <code>null</code>.
     */
    public static Builder of(final EncodedURL n, final EncodedURL e) {
      // prevent bogus input
      return new Builder(
        Objects.requireNonNull(n, "The modulus value must not be null")
      , Objects.requireNonNull(e, "The exponent value must not be null")
      );
    }

  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Other
  // ~~~~~ ~~~~~ ~~~~~
  /**
   ** <code>Other</code> Prime Info, represents the private <code>oth</code>
   ** parameter of a RSA JSON Web Key.
   ** <p>
   ** This class is immutable.
   */
  public static final class Other implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-4302568619764738814")
    private static final long serialVersionUID = 4015679286397189211L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The prime factor. */
    public final EncodedURL r;

    /** The factor Chinese Remainder Theorem (CRT) exponent. */
    public final EncodedURL d;

    /** The factor Chinese Remainder Theorem (CRT) coefficient. */
    public final EncodedURL t;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new <code>Other</code> Prime Info from the specified
     ** {@link RSAOtherPrimeInfo} instance.
     **
     ** @param  value            the RSA Other Primes Info instance.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAOtherPrimeInfo}.
     */
    public Other(final RSAOtherPrimeInfo value) {
      // ensure inheritance
      this(EncodedURL.of(value.getPrime()), EncodedURL.of(value.getExponent()), EncodedURL.of(value.getCrtCoefficient()));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a new <code>Other</code> Prime Info with the specified
     ** encoded values.
     **
     ** @param  r                the prime factor.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  d                the Chinese Remainder Theorem (CRT) factor
     **                          exponent.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  t                the Chinese Remainder Theorem (CRT)
     **                          coefficient.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws NullPointerException if either <code>r</code>, <code>d</code> or
     **                              <code>t</code> is <code>null</code>.
     */
    private Other(final EncodedURL r, final EncodedURL d, final EncodedURL t) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.r = Objects.requireNonNull(r, "The prime factor must not be null");
      this.d = Objects.requireNonNull(d, "The factor CRT exponent must not be null");
      this.t = Objects.requireNonNull(t, "The factor CRT coefficient must not be null");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Converts the specified array of {@link RSAOtherPrimeInfo} instances to a
     ** list of JWK Other Prime Infos.
     **
     ** @param  source           the array of RSA Other Primes Info instances.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of
     **                          {@link RSAOtherPrimeInfo}.
     **
     ** @return                  the corresponding collection of JWK
     **                          {@link Other} Prime Infos, or empty list if the
     **                          array was <code>null</code>.
     */
    private static List<Other> from(final RSAOtherPrimeInfo[] source) {
      List<Other> list = new ArrayList<>();
      if (source == null) {
        // return empty list
        return list;
      }
      for (RSAOtherPrimeInfo cursor : source) {
        list.add(new Other(cursor));
      }
      return list;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   jsonify (overridden)
    /**
     ** Returns a JSON object representation of this <code>Other</code> Prime
     ** Info instance.
     ** <p>
     ** This method is  intended to be called from extending classes.
     ** <br>
     ** Example:
     ** <pre>
     **   { "r" : "???"
     **   , "d" : "???"
     **   , "t" : "???"
     **   }
     ** </pre>
     **
     ** @return                    the JSON object representation.
     **                            <br>
     **                            Possible object is {@link JsonObjectBuilder}.
     */
    public final JsonObjectBuilder jsonify() {
      return Json.createObjectBuilder().add(OTR, r.toString()).add(OTD, d.toString()).add(OTT, t.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new JSON Web <code>RSAKey</code> from the specified
   ** <code>RSAKey</code>.
   **
   ** @param  other              the <code>RSAKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>RSAKey</code>.
   */
  @SuppressWarnings("unchecked")
  private RSAKey(final RSAKey other) {
    this(other.n, other.e, other.d, other.p, other.q, other.dp, other.dq, other.qi, other.oth, other.use, other.ops, other.kid, other.alg, other.x5u, other.x5t, other.x5t256, other.x5c, other.keyStore);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  RSAKey
  /**
   ** Constructs a new JSON Web <code>RSAKey</code> with the specified
   ** parameters.
   **
   ** @param  n                  the required modulus value for the RSA public
   **                            key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  e                  the required exponent value for the RSA public
   **                            key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  d                  the optional exponent value for the RSA private
   **                            key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  p                  the optional first prime factor value for the
   **                            RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  q                  the optional second prime factor value for the
   **                            RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  dp                 the first factor Chinese Remainder Theorem
   **                            exponent value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  dq                 the second factor Chinese Remainder Theorem
   **                            exponent value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  qi                 the second factor Chinese Remainder Theorem
   **                            coefficient value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  oth                The other optional prime information for the
   **                            RSA private key, should they exist.
   **                            <br>
   **                            Allowed object is array of {@link Other}.
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
   **
   ** @throws NullPointerException if either <code>n</code> or
   **                              <code>e</code> is <code>null</code>.
   */
  @SuppressWarnings("unchecked")
  private RSAKey(final EncodedURL n, final EncodedURL e, final EncodedURL d, final EncodedURL p, final EncodedURL q, final EncodedURL dp, final EncodedURL dq, final EncodedURL qi, final List<Other> oth, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    // ensure inheritance
    super(Type.RSA, use, ops, kid, alg, x5u, x5t, x5t256, x5c, keyStore);

    // initialize instance attributes
    this.n    = Objects.requireNonNull(n,  "The modulus value must not be null");
    this.e    = Objects.requireNonNull(e,  "The exponent value must not be null");
    this.d    = d;

    this.pkcs = null;//pkcs; // PKCS#11 handle
    // check consistency
    if (p != null && q != null && dp != null && dq != null && qi != null) {
      // full qualified
      this.p   = p;
      this.q   = q;
      this.dp  = dp;
      this.dq  = dq;
      this.qi  = qi;
      this.oth = oth;
    }
    else if (p != null || q != null || dp != null || dq != null || qi != null) {
      if (p == null)
        throw new IllegalArgumentException("Incomplete second private (CRT) representation: The first prime factor must not be null");
      else if (q == null)
        throw new IllegalArgumentException("Incomplete second private (CRT) representation: The second prime factor must not be null");
      else if (dp == null)
        throw new IllegalArgumentException("Incomplete second private (CRT) representation: The first factor CRT exponent must not be null");
      else if (dq == null)
        throw new IllegalArgumentException("Incomplete second private (CRT) representation: The second factor CRT exponent must not be null");
      else
        throw new IllegalArgumentException("Incomplete second private (CRT) representation: The first CRT coefficient must not be null");
    }
    else {
      this.p   = null;
      this.q   = null;
      this.dp  = null;
      this.dq  = null;
      this.qi  = null;
      this.oth = null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rsaPrivateKey
  /**
   ** Returns a standard {@link RSAPrivateKey} representation of this
   ** <code>RsaKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the private RSA key or <code>null</code> if not
   **                            specified.
   **                            <br>
   **                            Possible object is {@link RSAPrivateKey}.
   **
   ** @throws JoseException      if conversion failed or is not supported by
   **                            the underlying Java Cryptography (JCA)
   **                            provider or key spec parameters are invalid
   **                            for a {@link PrivateKey}.
   */
  public final PrivateKey rsaPrivateKey()
    throws JoseException {

    // no private key
    if (this.d == null)
      return null;


    BigInteger m = this.n.decodeBigInteger();
    BigInteger d = this.d.decodeBigInteger();

    RSAPrivateKeySpec spec;

    if (this.p == null) {
      // Use 1st representation
      spec = new RSAPrivateKeySpec(m, d);
    }
    else {
      // use 2nd (CRT) representation
      BigInteger publicExponent = this.e.decodeBigInteger();
      BigInteger primeP         = this.p.decodeBigInteger();
      BigInteger primeQ         = this.q.decodeBigInteger();
      BigInteger primeExponentP = this.dp.decodeBigInteger();
      BigInteger primeExponentQ = this.dq.decodeBigInteger();
      BigInteger crtCoefficient = this.qi.decodeBigInteger();

      if (this.oth != null && !this.oth.isEmpty()) {
        // construct other info spec
        final RSAOtherPrimeInfo[] other = new RSAOtherPrimeInfo[this.oth.size()];
        for (int i = 0; i < this.oth.size(); i++) {
          final Other cursor = this.oth.get(i);
          other[i] = new RSAOtherPrimeInfo(cursor.r.decodeBigInteger(), cursor.d.decodeBigInteger(), cursor.t.decodeBigInteger());
        }
        spec = new RSAMultiPrimePrivateCrtKeySpec(m, publicExponent, d, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient, other);
      }
      else {
        // construct spec with no other info
        spec = new RSAPrivateCrtKeySpec(m, publicExponent, d, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
      }
    }

    try {
      final KeyFactory factory = KeyFactory.getInstance("RSA");
      return (RSAPrivateKey)factory.generatePrivate(spec);
    }
    catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      throw new JoseException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rsaPublicKey
  /**
   ** Returns a standard {@link RSAPublicKey} representation of this
   ** <code>RsaKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the public RSA key.
   **                            <br>
   **                            Possible object is {@link RSAPublicKey}.
   **
   ** @throws JoseException      if conversion failed or is not supported by
   **                            the underlying Java Cryptography (JCA)
   **                            provider or key spec parameters are invalid
   **                            for a {@link PublicKey}.
   */
  public final RSAPublicKey rsaPublicKey()
    throws JoseException {

    final RSAPublicKeySpec spec = new RSAPublicKeySpec(this.n.decodeBigInteger(), this.e.decodeBigInteger());
    try {
      final KeyFactory factory = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) factory.generatePublic(spec);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new JoseException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey (Asymmetric)
  /**
   ** Returns a standard {@link PrivateKey} representation of this
   ** <code>RsaKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the private RSA key or <code>null</code> if not
   **                            specified.
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

    PrivateKey prv = rsaPrivateKey();
    // return private RSA key as PKCS#11 handle, or private RSA key with key
    // material
    return (prv == null) ? this.pkcs : prv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKey (Asymmetric)
  /**
   ** Returns a standard {@link PublicKey} representation of this
   ** <code>RsaKey</code> JWK.
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the public RSA key.
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

    return rsaPublicKey();
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

    return new KeyPair(rsaPublicKey(), privateKey());
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

    final X509Certificate own = (X509Certificate)this.certificate.get(0);
    try {
      final RSAPublicKey key = (RSAPublicKey)own.getPublicKey();
      // compare Big Ints, base64url encoding may have padding!
      // https://datatracker.ietf.org/doc/html/rfc7518#section-6.2.1.2
      return this.e.decodeBigInteger().equals(key.getPublicExponent())
          && this.n.decodeBigInteger().equals(key.getModulus())
      ;
    }
    catch (ClassCastException ex) {
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
    // check if 1st or 2nd form params are specified, or PKCS#11 handle
    return this.d != null || this.p != null || this.pkcs != null;
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
    required.put(EXP, this.e.toString());
    required.put(KTY, this.kty.id);
    required.put(MOD, this.n.toString());
    return required;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Loads a public/private RSA JSON Web Key (JWK) from the specified JCE key
   ** store.
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
   ** @return                    The public/private RSA JSON Web Key (JWK) or
   **                            <code>null</code> if no key with the specified
   **                            alias was found.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws JoseException      if octet sequence key loading failed.
   */
  public static RSAKey load(final KeyStore store, final String alias, final char[] secret)
    throws JoseException {

    try {
      final Certificate certificate = store.getCertificate(alias);
      if (!(certificate instanceof X509Certificate))
        return null;

      final X509Certificate x509 = (X509Certificate)certificate;
      if (!(x509.getPublicKey() instanceof RSAPublicKey))
        throw new JoseException("Couldn't load RSA JWK: The key algorithm is not RSA");

      RSAKey jwk = from(x509);
      jwk = Builder.of(jwk).identifier(alias).keyStore(store).build();

      // check for private counterpart
      Key key;
      try {
        key = store.getKey(alias, secret);
      }
      catch (UnrecoverableKeyException | NoSuchAlgorithmException e) {
        throw new JoseException("Couldn't retrieve private RSA key (bad secret?): " + e.getMessage(), e);
      }
      if (key instanceof RSAPrivateKey) {
        // simple file based key store
        return Builder.of(jwk).privateKey((RSAPrivateKey)key).build();
      }
      else if (key instanceof PrivateKey && Type.RSA.id.equalsIgnoreCase(key.getAlgorithm())) {
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
   ** Factory method to create a public RSA JSON Web Key from the specified
   ** X.509 certificate.
   ** <p>
   ** <strong>Important:</strong>
   ** <br>
   ** The X.509 certificate is not validated!
   ** <p>
   ** Sets the following JWK parameters:
   ** <ul>
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
   ** @return                  the public RSA key.
   **                          <br>
   **                          Possible object is <code>RSAKey</code>.
   **
   ** @throws JoseException    if parsing failed.
   */
  public static RSAKey from(final X509Certificate certificate)
    throws JoseException {

    // prevent bogus input
    if (!(certificate.getPublicKey() instanceof RSAPublicKey))
      throw new JoseException("The public key of the X.509 certificate is not RSA");

    final RSAPublicKey publicKey = (RSAPublicKey)certificate.getPublicKey();
    try {
      final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
      return RSAKey.Builder.of(publicKey)
        .usage(Usage.from(certificate))
        .identifier(certificate.getSerialNumber().toString(10))
        .chain(CollectionUtility.list(EncodedURL.of(certificate.getEncoded())))
        .sha256(EncodedURL.of(sha256.digest(certificate.getEncoded())))
        .build();
    }
    catch (NoSuchAlgorithmException e) {
      throw new JoseException("Couldn't encode x5t parameter: " + e.getMessage(), e);
    }
    catch (CertificateEncodingException e) {
      throw new JoseException("Couldn't encode x5c parameter: " + e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a public RSA JSON Web Key from the specified
   ** JSON object representation.
   **
   ** @param  object           the JSON object to parse.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link JsonObject}.
   **
   ** @return                  the parsed <code>RSAKey</code>.
   **                          <br>
   **                          Possible object is <code>RSAKey</code>.
   **
   ** @throws IllegalArgumentException if on of the parsed properties is
   **                                  required but is <code>null</code>
   **                                  or empty or does not match.
   */
  public static RSAKey from(final JsonObject object)
    throws IllegalArgumentException {

      // check algorithm type
    if (Type.RSA != Type.from(object))
      throw new IllegalArgumentException("The algorithm type must be RSA");

    // assume the object containes the encoded from of the data
    return Builder.of(EncodedURL.raw(JsonMarshaller.stringValue(object, MOD)), EncodedURL.raw(JsonMarshaller.stringValue(object, EXP)))
      // parse the common key parameters
      .identifier(object)
      .usage(object)
      .operation(object)
      .algorithm(SignatureInstance.from(object))
      .certificateURI(object)
      .sha1(object)
      .sha256(object)
      .chain(SecureHeader.certificateChain(object, SecureHeader.X5C))
      // parse the optional key parameters
      .privateExponent(object)
      .primeP(object)
      .primeQ(object)
      .primeExponentP(object)
      .primeExponentQ(object)
      .primeCoefficientQI(object)
      .otherFactor(object)
      .build();
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a public/private <code>RSAKey</code> JSON Web Key
   ** with the specified property.
   ** <br>
   ** The private RSA key is specified by its first representation
   ** (see RFC 3447, section 3.2).
   **
   ** @param  publicKey          the public RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPublicKey}.
   ** @param  privateKey         the private RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPrivateKey}.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>modulus</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final RSAPublicKey publicKey, final RSAPrivateKey privateKey, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(EncodedURL.of(publicKey.getModulus()), EncodedURL.of(publicKey.getPublicExponent()))
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .privateExponent(EncodedURL.of(privateKey.getPrivateExponent()))
      .keyStore(keyStore)
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a public/private <code>RSAKey</code> JSON Web Key
   ** with the specified property.
   ** <br>
   ** The private RSA key is specified by its second representation
   ** (see RFC 3447, section 3.2).
   **
   ** @param  publicKey          the public RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPublicKey}.
   ** @param  privateKey         the private RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPrivateCrtKey}.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>modulus</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final RSAPublicKey publicKey, final RSAPrivateCrtKey privateKey, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(EncodedURL.of(publicKey.getModulus()), EncodedURL.of(publicKey.getPublicExponent()))
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .privateExponent(EncodedURL.of(privateKey.getPrivateExponent()))
      .primeP(EncodedURL.of(privateKey.getPrimeP()))
      .primeQ(EncodedURL.of(privateKey.getPrimeQ()))
      .primeExponentP(EncodedURL.of(privateKey.getPrimeExponentP()))
      .primeExponentQ(EncodedURL.of(privateKey.getPrimeExponentQ()))
      .primeCoefficientQI(EncodedURL.of(privateKey.getCrtCoefficient()))
      .keyStore(keyStore)
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a public/private <code>RSAKey</code> JSON Web Key
   ** with the specified property.
   ** <br>
   ** The private RSA key is specified by its second representation with
   ** optional other primes info (see RFC 3447, section 3.2).
   **
   ** @param  publicKey          the public RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPublicKey}.
   ** @param  privateKey         the private RSA key to represent.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link RSAMultiPrimePrivateCrtKey}.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>modulus</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final RSAPublicKey publicKey, final RSAMultiPrimePrivateCrtKey privateKey, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(EncodedURL.of(publicKey.getModulus()), EncodedURL.of(publicKey.getPublicExponent()))
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .privateExponent(EncodedURL.of(privateKey.getPrivateExponent()))
      .primeP(EncodedURL.of(privateKey.getPrimeP()))
      .primeQ(EncodedURL.of(privateKey.getPrimeQ()))
      .primeExponentP(EncodedURL.of(privateKey.getPrimeExponentP()))
      .primeExponentQ(EncodedURL.of(privateKey.getPrimeExponentQ()))
      .primeCoefficientQI(EncodedURL.of(privateKey.getCrtCoefficient()))
      .keyStore(keyStore)
      .otherFactor(Other.from(privateKey.getOtherPrimeInfo()))
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>RSAKey</code> JSON Web Key with the
   ** specified property.
   **
   ** @param  n                  the modulus value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  e                  the exponent value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>modulus</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final EncodedURL n, final EncodedURL e, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(n, e)
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .keyStore(keyStore)
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>RSAKey</code> JSON Web Key with the
   ** specified property.
   **
   ** @param  n                  the modulus value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  e                  the exponent value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  d                  the optional exponent value for the RSA private
   **                            key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>n</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final EncodedURL n, final EncodedURL e, final EncodedURL d, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(n, e)
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .privateExponent(d)
      .keyStore(keyStore)
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>RSAKey</code> JSON Web Key with the
   ** specified property.
   **
   ** @param  n                  the modulus value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  e                  the exponent value for the RSA public key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL}
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  d                  the optional exponent value for the RSA private
   **                            key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  p                  the optional first prime factor value for the
   **                            RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  q                  the optional second prime factor value for the
   **                            RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  dp                 the first factor Chinese Remainder Theorem
   **                            exponent value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  dq                 the second factor Chinese Remainder Theorem
   **                            exponent value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  qi                 the second factor Chinese Remainder Theorem
   **                            coefficient value for the RSA private key.
   **                            <br>
   **                            It is  represented as {@link EncodedURL} value
   **                            encoding value's big endian representation.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  oth                The other optional prime information for the
   **                            RSA private key, should they exist.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Other}.
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
   **                            Allowed object is array of {@link Encoded}.
   ** @param  keyStore           the reference to the underlying key store or
   **                            <code>null</code> if none.
   **                            <br>
   **                            Allowed object is {@link KeyStore}.
   **
   ** @return                    the created <code>RSAKey</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>n</code> or
   **                              <code>exponent</code> is <code>null</code> or
   **                              empty.
   */
  public static RSAKey of(final EncodedURL n, final EncodedURL e, final EncodedURL d, final EncodedURL p, final EncodedURL q, final EncodedURL dp, final EncodedURL dq, final EncodedURL qi, final List<Other> oth, final Usage use, final Set<Operation> ops, final String kid, final Algorithm alg, final URI x5u, final EncodedURL x5t, final EncodedURL x5t256, final List<Encoded> x5c, final KeyStore keyStore) {
    return RSAKey.Builder.of(n, e)
      // the common key parameters
      .identifier(kid).usage(use).operation(ops).algorithm(alg).certificateURI(x5u).sha1(x5t).sha256(x5t256).chain(x5c)
      // the optional key parameters
      .privateExponent(d)
      .primeP(p)
      .primeQ(q)
      .primeExponentP(dp)
      .primeExponentQ(dq)
      .primeCoefficientQI(qi)
      .otherFactor(oth)
      .keyStore(keyStore)
      .build()
      ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>RSAKey</code> JSON Web Key as a copy of
   ** the specified JSON Web Key <code>other</code>.
   **
   ** @param  other              the <code>RSAKey</code> to copy.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>RSAKey</code>.
   **
   ** @return                    the copy of the  <code>RSAKey</code>
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>RSAKey</code>.
   **
   ** @throws NullPointerException if either <code>other</code> is
   **                              <code>null</code>.
   */
  public static RSAKey of(final RSAKey other) {
    return new RSAKey(Objects.requireNonNull(other));
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
    return Objects.hash(super.hashCode(), this.n, this.e, this.d, this.p, this.q, this.dp, this.dq, this.qi, this.oth, this.pkcs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>RSAKey</code>s values are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>RSAKey</code>s values may be different even though they contain the
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

    if (!(other instanceof RSAKey))
      return false;

    if (!super.equals(other))
      return false;

    final RSAKey that = (RSAKey)other;
    return Objects.equals(this.n,    that.n)
        && Objects.equals(this.e,    that.e)
        && Objects.equals(this.d,    that.d)
        && Objects.equals(this.p,    that.p)
        && Objects.equals(this.q,    that.q)
        && Objects.equals(this.dp,   that.dp)
        && Objects.equals(this.dq,   that.dq)
        && Objects.equals(this.qi,   that.qi)
        && Objects.equals(this.oth,  that.oth)
        && Objects.equals(this.pkcs, that.pkcs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify (overridden)
  /**
   ** Returns a JSON object representation of this <code>RSAKey</code>.
   ** <p>
   ** This method is  intended to be called from extending classes.
   ** <br>
   ** Example:
   ** <pre>
   **   { "kty" : "RSA"
   **   , "n"   : "0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx
   **              4cbbfAAtVT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMs
   **              tn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FDW2
   **              QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbI
   **              SD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqb
   **              w0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw"
   **   , "e"   : "AQAB"
   **   , "kid" : "2011-04-29"
   **   }
   ** </pre>
   **
   ** @return                    the JSON object representation.
   **                            <br>
   **                            Possible object is {@link JsonObjectBuilder}.
   */
  @Override
  public final JsonObjectBuilder jsonify() {
    final JsonObjectBuilder builder = super.jsonify();
    // append RSA public key specific attributes
    builder.add(MOD, this.n.toString()).add(EXP, this.e.toString());
    if (this.d != null) {
      builder.add(PXP, this.d.toString());
    }
    if (this.p != null) {
      builder.add(FPF, this.p.toString());
    }
    if (this.q != null) {
      builder.add(SPF, this.q.toString());
    }
    if (this.dp != null) {
      builder.add(CDP, this.dp.toString());
    }
    if (this.dq != null) {
      builder.add(CDQ, this.dq.toString());
    }
    if (this.qi != null) {
      builder.add(CQI, this.qi.toString());
    }
    if (this.oth != null && !this.oth.isEmpty()) {
      final JsonArrayBuilder array = Json.createArrayBuilder();
      for (Other cursor : this.oth) {
        array.add(cursor.jsonify()).build();
      }
      builder.add(OTH, array);
    }
    return builder;
  }
}