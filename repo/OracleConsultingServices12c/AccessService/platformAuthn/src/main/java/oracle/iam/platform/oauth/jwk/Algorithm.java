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

    File        :   Algorithm.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Algorithm.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import oracle.hst.platform.core.marshal.JsonEnum;

////////////////////////////////////////////////////////////////////////////////
// enum Algorithm
// ~~~~ ~~~~~~~~~
/**
 ** The base class for algorithm names, with optional implementation
 ** requirement.
 ** <p>
 ** Includes constants for the following standard algorithm names:
 ** <ul>
 **   <li>{@link #NONE none}
 ** </ul>
 ** <br>
 ** Signature signing/verification
 ** <ul>
 **   <li>{@link #HS256 HS256}
 **   <li>{@link #HS384 HS384}
 **   <li>{@link #HS512 HS512}
 **   <li>{@link #RS256 RS256}
 **   <li>{@link #RS384 RS384}
 **   <li>{@link #RS512 RS512}
 **   <li>{@link #ES256 ES256}
 **   <li>{@link #ES384 ES384}
 **   <li>{@link #ES512 ES512}
 ** </ul>
 ** Encryption/Decryption algorithm
 ** <ul>
 **   <li>{@link #RSA1_5}
 **   <li>{@link #RSA_OAEP RSA-OAEP}
 **   <li>{@link #A128KW}
 **   <li>{@link #A256KW}
 **   <li>{@link #DIRECT dir}
 **   <li>{@link #ECDH_ES ECDH-ES}
 **   <li>{@link #ECDH_ES_A128KW ESDH-ES+A128KW}
 **   <li>{@link #ECDH_ES_A256KW ESDH-ES+A256KW}
 ** </ul>
 ** <br>
 ** Encryption/Decryption methods
 ** <ul>
 **   <li>{@link #A128CBC_HS256 A128CBC+HS256}
 **   <li>{@link #A256CBC_HS512 A256CBC+HS512}
 **   <li>{@link #A128GCM}
 **   <li>{@link #A256GCM}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Algorithm implements JsonEnum <Algorithm, String> {

    /** No algorithm (plain object without signature/encryption). */
    NONE("none", Requirement.REQUIRED)
    /*
     * Signing Algorithm
     */
    /** HMAC using SHA-256 hash algorithm (required). */
  , HS256("HS256", Requirement.REQUIRED)
    /** RSA using SHA-256 hash algorithm (recommended). */
  , RS256("RS256", Requirement.RECOMMENDED)
    /** HMAC using SHA-384 hash algorithm (optional). */
  , HS384("HS384", Requirement.OPTIONAL)
    /** HMAC using SHA-512 hash algorithm (optional). */
  , HS512("HS512", Requirement.OPTIONAL)
    /** RSA using SHA-384 hash algorithm (optional). */
  , RS384("RS384", Requirement.OPTIONAL)
    /** RSA using SHA-512 hash algorithm (optional). */
  , RS512("RS512", Requirement.OPTIONAL)
    /**
     ** ECDSA using P-256 curve and SHA-256 hash algorithm (recommended).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.1.2">RFC 7518, section 7.1.2. Initial Registry Contents</a>.
     */
  , ES256("ES256", Requirement.RECOMMENDED)
    /**
     ** ECDSA using P-384 curve and SHA-384 hash algorithm (optional).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.1.2">RFC 7518, section 7.1.2. Initial Registry Contents</a>.
     */
  , ES384("ES384", Requirement.OPTIONAL)
    /**
     ** ECDSA using P-521 curve and SHA-512 hash algorithm (optional).
     ** <br>
     ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-7.1.2">RFC 7518, section 7.1.2. Initial Registry Contents</a>.
     */
  , ES512("ES512", Requirement.OPTIONAL)
    /*
     * Encryption Algorithm
     */
    /** RSAES-PKCS1-V1_5 (RFC 3447) (required). */
  , RSA1_5("RSA1_5", Requirement.REQUIRED)
    /**
     ** RSAES using Optimal Assymetric Encryption Padding (OAEP) (RFC 3447), with
     ** the default parameters specified by RFC 3447 in section A.2.1
     ** (recommended).
     */
  , RSA_OAEP("RSA-OAEP", Requirement.RECOMMENDED)
    /**
     ** Advanced Encryption Standard (AES) Key Wrap Algorithm (RFC 3394) using
     ** 256 bit keys (recommended).
     */
  , A128KW("A128KW", Requirement.RECOMMENDED)
    /**
     ** Advanced Encryption Standard (AES) Key Wrap Algorithm (RFC 3394) using 256
     ** bit keys (recommended).
     */
  , A256KW("A256KW", Requirement.RECOMMENDED)
    /**
     ** Direct use of a shared symmetric key as the Content Master Key (CMK) for
     ** the block encryption step (rather than using the symmetric key to wrap
     ** the CMK) (recommended).
     */
  , DIRECT("dir", Requirement.RECOMMENDED)
    /**
     ** Elliptic Curve Diffie-Hellman Ephemeral Statis (RFC 6090) key agreement
     ** using the Concat KDF, as defined in section 5.8.1 of NIST.800-56A, where
     ** the Digest Method is SHA-256 and all OtherInfo parameters the empty bit
     ** string, with the agreed-upon key being used directly as the Content
     ** Master Key (CMK) (rather than being used to wrap the CMK) (recommended).
     */
  , ECDH_ES("ECDH-ES", Requirement.RECOMMENDED)
    /**
     ** Elliptic Curve Diffie-Hellman Ephemeral Static key agreement per
     ** "ECDH-ES", but where the agreed-upon key is used to wrap the Content
     ** Master Key (CMK) with the "A128KW" function (rather than being used
     ** directly as the CMK) (recommended).
     */
  , ECDH_ES_A128KW("ECDH-ES+A128KW", Requirement.RECOMMENDED)
    /**
     ** Elliptic Curve Diffie-Hellman Ephemeral Static key agreement per
     ** "ECDH-ES", but where the agreed-upon key is used to wrap the Content
     ** Master Key (CMK) with the "A256KW" function (rather than being used
     ** directly as the CMK) (recommended).
     */
  , ECDH_ES_A256KW("ECDH-ES+A256KW", Requirement.RECOMMENDED)
    /*
     * Encryption Methods
     */
    /**
     ** Composite AED algorithm using Advanced Encryption Standard (AES) in
     ** Cipher Block Chaining (CBC) mode with PKCS #5 padding (NIST.800-38A)
     ** with an integrity calculation using HMAC SHA-256, using a 256 bit CMK
     ** (and a 128 bit CEK) (required).
     */
  , A128CBC_HS256("A128CBC-HS256", Requirement.REQUIRED)
    /**
     ** Composite AED algorithm using Advanced Encryption Standard (AES) in
     ** Cipher Block Chaining (CBC) mode with PKCS #5 padding (NIST.800-38A)
     ** with an integrity calculation using HMAC SHA-384, using a 384 bit CMK
     ** (and a 384 bit CEK) (required).
     */
  , A256CBC_HS384("A256CBC-HS384", Requirement.OPTIONAL)
    /**
     ** Composite AED algorithm using Advanced Encryption Standard (AES) in
     ** Cipher Block Chaining (CBC) mode with PKCS #5 padding (NIST.800-38A)
     ** with an integrity calculation using HMAC SHA-512, using a 512 bit CMK
     ** (and a 256 bit CEK) (required).
     */
  , A256CBC_HS512("A256CBC-HS512", Requirement.REQUIRED)
    /**
     ** Advanced Encryption Standard (AES) in Galois/Counter Mode (GCM)
     ** (NIST.800-38D) using 128 bit keys (recommended).
     */
  , A128GCM("A128GCM", Requirement.RECOMMENDED)
    /**
     ** Advanced Encryption Standard (AES) in Galois/Counter Mode (GCM)
     ** (NIST.800-38D) using 256 bit keys (recommended).
     */
  , A256GCM("A256GCM", Requirement.RECOMMENDED);
  ;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The algorithm name. */
  public final String      id;

  /** The implementation requirement or <code>null</code> if not known. */
  public final Requirement requirement;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Requirement
  // ~~~~ ~~~~~~~~~~~
  /**
   ** Constraint of algorithm implementation requirements.
   ** <p>
   ** Refers to the requirement levels defined in RFC 2119.
   */
  public enum Requirement {
      /** The implementation of the algorithm is required. */
      REQUIRED
      /** The implementation of the algorithm is optional. */
    , OPTIONAL
      /** The implementation of the algorithm is recommended. */
    , RECOMMENDED
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>Algorithm</code> with the specified name and
   ** implementation requirement.
   **
   ** @param  id                 the <code>Algorithm</code> identifier.
   **                            <br>
   **                            Names are case sensitive.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  requirement        the implementation requirement,
   **                            <code>null</code> if not known.
   **                            <br>
   **                            Allowed object is {@link Requirement}.
   **
   ** @throws NullPointerException if <code>name</code> is <code>null</code>.
   */
  Algorithm(final String id, final Requirement requirement) {
    // initialize instance attributes
    this.id          = id;
    this.requirement = requirement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of (JsonEnum)
  /**
   ** Factory method to create a proper <code>Algorithm</code> constraint from
   ** the given string value.
   **
   ** @param  value              the string value the type constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Algorithm</code> constraint.
   **                            <br>
   **                            Possible object is <code>Algorithm</code>.
   **
   ** @throws IllegalArgumentException if no matching <code>Algorithm</code>
   **                                  could be found.
   */
  @Override
  public final Algorithm of(final String value) {
    return from(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (JsonEnum)
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

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  from
  /**
   ** Factory method to create a proper <code>Algorithm</code> constraint from
   ** the given string value.
   **
   ** @param  value              the string value the type constraint should be
   **                            returned for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Algorithm</code> constraint.
   **                            <br>
   **                            Possible object is <code>Algorithm</code>.
   **
   ** @throws IllegalArgumentException if no matching <code>Algorithm</code>
   **                                  could be found.
   */
  public static Algorithm from(final String value) {
    for (Algorithm cursor : Algorithm.values()) {
      if (cursor.id.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException("Unexpected algorithm type: " + value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for the <code>Algorithm</code> in its
   ** minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            literal.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return this.id;
  }
}