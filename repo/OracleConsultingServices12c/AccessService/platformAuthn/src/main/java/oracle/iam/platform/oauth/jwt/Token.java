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

    File        :   Token.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Token.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwt;

import java.util.Objects;

import javax.json.JsonObject;

import javax.json.stream.JsonParsingException;

import oracle.hst.platform.core.annotation.ThreadSafety;

import oracle.hst.platform.core.marshal.JsonMarshaller;

import oracle.iam.platform.oauth.jwk.Header;
import oracle.iam.platform.oauth.jwk.Payload;
import oracle.iam.platform.oauth.jwk.Algorithm;
import oracle.iam.platform.oauth.jwk.JoseObject;
import oracle.iam.platform.oauth.jwk.EncodedURL;
import oracle.iam.platform.oauth.jwk.PlainHeader;
import oracle.iam.platform.oauth.jwk.PlainObject;
import oracle.iam.platform.oauth.jwk.SignatureHeader;
import oracle.iam.platform.oauth.jwk.SignatureObject;
import oracle.iam.platform.oauth.jwk.EncryptionHeader;
import oracle.iam.platform.oauth.jwk.EncryptionObject;

import oracle.iam.platform.oauth.AuthorizationError;
import oracle.iam.platform.oauth.AuthorizationBundle;

////////////////////////////////////////////////////////////////////////////////
// class Token
// ~~~~~ ~~~~~
/**
 ** JSON Web Token (JWT) interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Token {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Plain
  // ~~~~~ ~~~~~ ~~~~~
  /**
   ** Plain JSON Web Token (JWT).
   ** <p>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  public static final class Plain extends    PlainObject
                                  implements Token {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-5143621703609198185")
    private static final long serialVersionUID = 7854816781510688332L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new plain JSON Web Token (JWT) with the specified claim
     ** set.
     **
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     */
    private Plain(final Claim claim) {
      // ensure inhritance
      super(Payload.of(claim));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new plain JSON Web Token (JWT) with the specified
     ** {@link PlainHeader} and claim set.
     **
     ** @param  header           the plain header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link PlainHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     */
    private Plain(final PlainHeader header, final Claim claim) {
      // ensure inhritance
      super(header, Payload.of(claim));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new plain JSON Web Token (JWT) with the specified
     ** {@link EncodedURL} subjects.
     **
     ** @param  header           the first part, corresponding to the plain
     **                          header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  claim            the econd part, corresponding to the claim.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     */
    private Plain(final EncodedURL header, final EncodedURL claim) {
      // ensure inhritance
      super(header, claim);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new plain JSON Web Token (JWT) with the
     ** specified claim set.

     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  the <code>Signed</code> with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Signed</code>.
     */
    public static Plain of(final Claim claim) {
      return new Plain(claim);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new plain JSON Web Token (JWT) with the specified
     ** {@link PlainHeader} and claim set.
     **
     ** @param  header           the plain header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link PlainHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  the <code>Plain</code> with the specified
     **                          serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Plain</code>.
     */
    public static Plain of(final PlainHeader header, final Claim claim) {
      return new Plain(header, claim);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a plain text JSON Web Token (JWT) with the
     ** specified serialized subjects.
     **
     ** @param  header           the first part, corresponding to the plain
     **                          header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  claim            the second part, corresponding to the claim.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Plain</code> token with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is
     **                          <code>Plain</code>.
     **
     ** @throws NullPointerException     if either <code>header</code> or
     **                                  <code>claim</code> is
     **                                  <code>null</code>.
     ** @throws IllegalArgumentException if the specified JSON object doesn't
     **                                  represent a valid JWS header.
     */
    public static Plain of(final EncodedURL header, final EncodedURL claim)
      throws IllegalArgumentException {

      // prevent bogus input
      if (header == null)
        throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_PLAIN_HEADER));

      // prevent bogus input
      if (claim == null)
        throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_PLAIN_PAYLOAD));

      return new Plain(header, claim);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Encrypted
  // ~~~~~ ~~~~~ ~~~~~~~~~
  /**
   ** Encrypted JSON Web Token (JWT).
   ** <p>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  public static final class Encrypted extends    EncryptionObject
                                      implements Token {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6540976049030028489")
    private static final long serialVersionUID = -6217001972308374461L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new to-be-encrypted JSON Web Token (JWT) with the specified
     ** {@link EncryptionHeader} and claim set.
     ** <br>
     ** The initial state will be
     ** {@link EncryptionObject.State#UNENCRYPTED unencrypted}.
     **
     ** @param  header           the encryption header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncryptionHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     */
    private Encrypted(final EncryptionHeader header, final Claim claim) {
      // ensure inhritance
      super(header, Payload.of(claim));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Token</code> with the specified
     ** {@link EncodedURL} subjects.
     ** <br>
     ** The initial state will be
     ** {@link EncryptionObject.State#ENCRYPTED encrypted}.
     **
     ** @param  header           the first part, corresponding to the JWE
     **                          header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  encryptedKey     the second part, corresponding to the
     **                          encrypted key.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  initialization   the third part, corresponding to the
     **                          initialization vector.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  cipherText       the forth part, corresponding to the cipher
     **                          text.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  integrity        the fith part, corresponding to the integrity
     **                          value.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     */
    private Encrypted(final EncodedURL header, final EncodedURL encryptedKey, final EncodedURL initialization, final EncodedURL cipherText, final EncodedURL integrity) {
      // ensure inhritance
      super(header, encryptedKey, initialization, cipherText, integrity);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new to-be-encrypted JSON Web Token (JWT) with
     ** the specified {@link EncryptionHeader} and claim set.
     ** <br>
     ** The initial state will be
     ** {@link EncryptionObject.State#UNENCRYPTED unencrypted}.
     **
     ** @param  header           the encryption header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncryptionHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  the <code>Encrypted</code> with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Encrypted</code>.
     */
    public static Encrypted of(final EncryptionHeader header, final Claim claim) {
      return new Encrypted(header, claim);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a plain text JSON Web Token (JWT) with the
     ** specified serialized subjects.
     ** <br>
     ** The initial state will be
     ** {@link EncryptionObject.State#ENCRYPTED encrypted}.
     **
     ** @param  header           the first part, corresponding to the JWE
     **                          header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  encryptedKey     the second part, corresponding to the
     **                          encrypted key.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  initialization   the third part, corresponding to the
     **                          initialization vector.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  cipherText       the forth part, corresponding to the cipher
     **                          text.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  integrity        the fith part, corresponding to the integrity
     **                          value.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Encrypted</code> with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Encrypted</code>.
     **
     ** @throws NullPointerException if one of <code>header</code>,
     **                              <code>encryptedKey</code>,
     **                              <code>initialization</code>,
     **                              <code>cipherText</code>,
     **                              <code>integrity</code> is
     **                              <code>null</code>.
     */
    public static Encrypted of(final EncodedURL header, final EncodedURL encryptedKey, final EncodedURL initialization, final EncodedURL cipherText, final EncodedURL integrity)
      throws IllegalArgumentException {

      // prevent bogus input
      Objects.requireNonNull(header,         "The JWE header must not be null");
      Objects.requireNonNull(encryptedKey,   "The JWE encrypted key must not be null");
      Objects.requireNonNull(initialization, "The JWE initialization vector must not be null");
      Objects.requireNonNull(cipherText,     "The JWE cipher text must not be null");
      Objects.requireNonNull(integrity,      "The JWE integrity value must not be null");

      return new Encrypted(header, encryptedKey, initialization, cipherText, integrity);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Signed
  // ~~~~~ ~~~~~ ~~~~~~
  /**
   ** Encrypted JSON Web Token (JWT).
   ** <p>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  public static final class Signed extends    SignatureObject
                                   implements Token {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4329099375977830269")
    private static final long serialVersionUID = -4935863172798620134L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new to-be-signed JSON Web Token (JWT) with the specified
     ** {@link SignatureHeader} and claim set.
     ** <br>
     ** The initial state will be
     ** {@link SignatureObject.State#UNSIGNED unsigned}.
     **
     ** @param  header           the signature header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     */
    private Signed(final SignatureHeader header, final Claim claim) {
      // ensure inhritance
      super(header, Payload.of(claim));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new signed JSON Web Token (JWT) object with the specified
     ** serialized subjects.
     ** <br>
     ** The initial state will be {@link SignatureObject.State#SIGNED signed}.
     **
     ** @param  header           the first part, correspondending to the
     **                          JWS header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  payload          the second part, correspondending to the
     **                          claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  signature        the third part, correspondending to the
     **                          signature.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws IllegalArgumentException if either <code>header</code> or
     **                                  <code>payload</code> is
     **                                  <code>null</code> or empty.
     */
    private Signed(final EncodedURL header, final EncodedURL payload, final EncodedURL signature)
      throws IllegalArgumentException {

      // ensure inheritance
      super(header, payload, signature);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  from
    /**
     ** Factory method to create a signed JSON Web Token (JWT) from the
     ** specified string in compact format.
     ** <p>
     ** The parsed signed JSON Web Token (JWT) will be returned in a
     ** {@link State#SIGNED} state.
     **
     ** @param  value              the string to parse.
     **                            <br>
     **                            Must not be <code>null</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the parsed <code>Signed</code> token.
     **                            <br>
     **                            Possible object is <code>Signed</code> token.
     **
     ** @throws IllegalArgumentException if the string couldn't be parsed to a
     **                                  valid JWS object.
     */
    public static Signed from(final String value)
      throws IllegalArgumentException {

      final EncodedURL[] subject = split(value);
      if (subject.length != 3)
        throw new IllegalArgumentException("Unexpected number of encoded subjects, must be three");

      return of(subject[0], subject[1], subject[2]);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a to-be-signed JSON Web Token (JWT) with the
     ** specified {@link SignatureHeader} and claim set.
     ** <br>
     ** The initial state will be
     ** {@link SignatureObject.State#UNSIGNED unsigned}.
     **
     ** @param  header           the signature header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param  claim            the claim set.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Claim}.
     **
     ** @return                  the <code>Signed</code> with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Signed</code>.
     */
    public static Signed of(final SignatureHeader header, final Claim claim) {
      return new Signed(header, claim);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a signed JSON Web Token (JWT) with the
     ** specified serialized subjects.
     ** <br>
     ** The initial state will be {@link SignatureObject.State#SIGNED signed}.
     **
     ** @param  header           the first part, correspondending to the
     **                          JWS header.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  payload          the second part, correspondending to the
     **                          payload.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  signature        the third part, correspondending to the
     **                          signature.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  the <code>Signed</code> with the
     **                          specified serialized subjects populated.
     **                          <br>
     **                          Possible object is <code>Signed</code>.
     **
     ** @throws IllegalArgumentException if the specified JSON object doesn't
     **                                  represent a valid JWS header.
     */
    public static Signed of(final EncodedURL header, final EncodedURL payload, final EncodedURL signature)
      throws IllegalArgumentException {

      return new Signed(header, payload, signature);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header
  /**
   ** Returns the JOSE header of this JSON Web Token (JWT).
   **
   ** @return                    the header of this JSON Web Token (JWT).
   **                            <br>
   **                            Possible object is {@link Header}.
   */
  Header header();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   claim
  /**
   ** Returns the claim set of this JSON Web Token (JWT).
   **
   ** @return                    the claims set or <code>null</code> if not
   **                            available (for an encrypted JWT that isn't
   **                            decrypted).
   **                            <br>
   **                            Possible object is {@link Claim}.
   **
   ** @throws IllegalStateException if payload of the plain/JWS/JWE object
   **                               doesn't represent a valid JSON object and a
   **                               JWT claim set.
   */
  default Claim claim()
    throws IllegalStateException {

    final Payload payload = payload();
    return (payload == null) ? Claim.builder().build() : Claim.builder(payload.asMap()).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  /**
   ** Returns the payload of this JSON Web Token (JWT).
   **
   ** @return                    the {@link Payload} or <code>null</code> if not
   **                            available (e.g for an encrypted JWE object).
   **                            <br>
   **                            Possible object is {@link Payload}.
   */
  Payload payload();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  /**
   ** Serializes the JSON Web Token (JWT) to its compact format consisting of
   ** EncodedURL subjects delimited by period ('.') characters.
   **
   ** @return                    the serialized JSON Web Token (JWT).
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalStateException if the JOSE object is not in a state that
   **                               permits serialization.
   */
  String serialize()
    throws IllegalStateException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to parses and create an unsecured (plain), signed or
   ** encrypted JSON Web Token (JWT) from the specified string in compact
   ** format.
   **
   ** @param  value            the string representing the
   **                          <code>Token</code>.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the corresponding {@link Plain}, {@link Signed}
   **                          or {@link Encrypted} token instance.
   **                          Possible object is <code>Token.Plain</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalStateException    if a JSON representation cannot be
   **                                  created due to incorrect representation.
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid unsecured, signed or encrypted JWT.
   */
  static Token from(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    final int first = Objects.requireNonNull(value, AuthorizationBundle.string(AuthorizationError.ARGUMENT_IS_NULL, "value")).indexOf(".");
    if (first == -1)
      throw new IllegalArgumentException("Invalid JWT serialization: Missing dot delimiter(s)");

    JsonObject payload = null;
    // assume the alue containes the encoded from of the data
    EncodedURL header  = EncodedURL.raw(value.substring(0, first));
    try {
      payload = JsonMarshaller.readObject(header.decodeString());

    }
    catch (JsonParsingException e) {
      throw new IllegalStateException("Invalid unsecured/signed/emcrypted header: " + e.getMessage());
    }

    final Algorithm alg = Header.algorithm(payload);
    if (alg.equals(Algorithm.NONE)) {
      return plain(value);
    }
    switch (alg) {
      case NONE           : return plain(value);
      case HS256          :
      case HS384          :
      case HS512          :
      case RS256          :
      case RS384          :
      case RS512          :
      case ES256          :
      case ES384          :
      case ES512          : return signed(value);
      case RSA1_5         :
      case RSA_OAEP       :
      case A128KW         :
      case A256KW         :
      case DIRECT         :
      case ECDH_ES        :
      case ECDH_ES_A128KW :
      case ECDH_ES_A256KW : return encrypted(value);
      default             : throw new AssertionError("Unexpected algorithm type: " + alg);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plain
  /**
   ** Factory method to create a <code>Token</code> from the specified
   ** string representation.
   **
   ** @param  value            the string representing the
   **                          <code>Token</code>.
   **                          <br>
   **                          Must not be <code>null</code>.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the <code>Token</code>.
   **                          <br>
   **                          Possible object is <code>Token.Plain</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid  plain JWT.
   **
   */
  public static Token.Plain plain(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(value, AuthorizationBundle.string(AuthorizationError.ARGUMENT_IS_NULL, "value"));

    final EncodedURL[] subject = JoseObject.split(value);
    if (!subject[2].toString().isEmpty())
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_PLAIN_ENCODE));

    return Plain.of(subject[0], subject[1]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypted
  /**
   ** Factory method to parse and create an encrypted JSON Web Token (JWT)
   ** received over network from the specified string representation..
   ** <br>
   ** Use this method if you have previous knowledge that this is a encrypted
   ** JWT, otherwise use {@link #plain(String)}.
   **
   ** @param  value              the string representing the
   **                            <code>Encrypted</code> JWT.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Token</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>Token.Encrypted</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid encrypted JWT.
   **
   */
  static Token.Encrypted encrypted(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(value, AuthorizationBundle.string(AuthorizationError.ARGUMENT_IS_NULL, "value"));

    final EncodedURL[] subject = JoseObject.split(value);
    if (subject.length != 5)
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_ENCRYPTED_ENCODE));

    return Encrypted.of(subject[0], subject[1], subject[2], subject[3], subject[4]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signed
  /**
   ** Factory method to parse and create a signed JSON Web Token (JWT) received
   ** over network from the specified string representation.
   ** <p>
   ** The expected content is
   ** <code>header_base64.payload_base64.signature_base64</code> where base64 is
   ** base64 URL encoding.
   ** <br>
   ** Use this method if you have previous knowledge that this is a signed JWT,
   ** otherwise use {@link #plain(String)}.
   ** <p>
   ** This method does <b>no</b> validation of content at all, only validates
   ** that the content is correctly formatted:
   ** <ul>
   **   <li>correct format of string (e.g. base64.base64.base64)</li>
   **   <li>each base64 part is actually base64 URL encoded</li>
   **   <li>header and payload are JSON objects</li>
   ** </ul>
   **
   ** @param  value              the string representing the
   **                            <code>Signed</code> JWT.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a signed <code>Token</code> instance that can
   **                            be used to obtain the
   **                            {@code #payload() instance} and to
   **                            {@code #verify(JsonWebKeySet) verify} the
   **                            signature.
   **                            <br>
   **                            Possible object is <code>Token.Signed</code>.
   **
   ** @throws NullPointerException     if <code>value</code> is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid signed JWT.
   */
  static Token.Signed signed(final String value)
    throws IllegalArgumentException {

    // prevent bogus input
    Objects.requireNonNull(value, AuthorizationBundle.string(AuthorizationError.ARGUMENT_IS_NULL, "value"));

    final EncodedURL[] subject = JoseObject.split(value);
    if (subject.length != 3)
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_ENCODE));

    return Signed.of(subject[0], subject[1], subject[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signed
  /**
   ** Factory method to create a to-be-signed JSON Web Token (JWT) with the
   ** specified header and claim set.
   ** <br>
   ** The initial state will be {@link SignatureObject.State#UNSIGNED unsigned}.
	 **
	 ** @param  header             the signature header.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureHeader}.
	 ** @param  claim              the token claim set.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Claim}.
   **
   ** @return                    a unsigned <code>Token</code> instance that can
   **                            be used to obtain the
   **                            {@code #payload() instance} and to
   **                            {@code #verify(JsonWebKeySet) verify} the
   **                            signature.
   **                            <br>
   **                            Possible object is <code>Token.Signed</code>.
	 */
	static Token.Signed signed(final SignatureHeader header, final Claim claim) {
    return Signed.of(header, claim);
	}
}