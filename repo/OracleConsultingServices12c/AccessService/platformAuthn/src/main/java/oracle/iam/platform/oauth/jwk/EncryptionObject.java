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

    File        :   EncryptionObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EncryptionObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Objects;

import java.io.Serializable;

import oracle.hst.platform.core.annotation.ThreadSafety;

import oracle.iam.platform.oauth.AuthorizationError;
import oracle.iam.platform.oauth.AuthorizationBundle;
import oracle.iam.platform.oauth.AuthorizationException;

////////////////////////////////////////////////////////////////////////////////
// class EncryptionObject
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** JSON Web Encryption (JWE) object.
 ** <p>
 ** This class is thread-safe.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
public class EncryptionObject extends JoseObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-5627045587330344195")
  private static final long serialVersionUID = -4924129828830507384L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The header. */
  public final EncryptionHeader header;

  /** The cryptographic parts of a JSON Web Encryption (JWE) object. */
  private Subject               subject;

  /** The JWE object state. */
  private State                 state;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum State
  // ~~~~ ~~~~~
  /**
   ** Constraint of the states of a JSON Web Encryption (JWE) object.
   */
  public static enum State {
      /** The JWE object is created but not encrypted yet. */
      UNENCRYPTED
      /** The JWE object is encrypted. */
    , ENCRYPTED
      /** The JWE object is decrypted. */
    , DECRYPTED
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // final class Subject
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** The cryptographic parts of a JSON Web Encryption (JWE) object.
   ** <br>
   ** This class is an immutable simple wrapper for returning the cipher text,
   ** initialization vector (IV), encrypted key and integrity value from
   ** {@link EncryptionObject} implementations.
   */
  public static final class Subject implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-783810250204942083")
    private static final long serialVersionUID = -3972001138300848245L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The encrypted key (optional). */
    public final EncodedURL key;

    /** The initialization vector (optional). */
    public final EncodedURL i4v;

    /** The cipher text. */
    public final EncodedURL txt;

    /** The integrity value (optional). */
    public final EncodedURL itv;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Subject
    /**
     ** Constructs a new cryptographic JWE subject instance.
     **
     ** @param  encryptedKey     the encrypted key or <code>null</code> if not
     **                          required by the JWE algorithm.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  initialization   the initialization vector or <code>null</code>
     **                          if not required by the JWE algorithm.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  cipherText       the cipher text to decrypt.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     ** @param  integrity        the integrity value or <code>null</code> if not
     **                          required by the JWE algorithm.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @throws NullPointerException if <code>cipherText</code> is
     **                              <code>null</code>.
     */
    Subject(final EncodedURL encryptedKey, final EncodedURL initialization, final EncodedURL cipherText, final EncodedURL integrity) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.txt = Objects.requireNonNull(cipherText, "The cipher text must not be null");
      this.key = (encryptedKey == null   || encryptedKey.value   == null) ? null : encryptedKey;
      this.i4v = (initialization == null || initialization.value == null) ? null : initialization;
      this.itv = (integrity == null      || initialization.value == null) ? null : integrity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new to-be-encrypted JSON Web Encryption (JWE) object with
   ** the specified header and payload.
   ** <br>
   ** The initial state will be {@link State#UNENCRYPTED unencrypted}.
   **
   ** @param  header             the {@link EncryptionHeader}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncryptionHeader}.
   ** @param  payload            the {@link Payload} to encrypt.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Payload}.
   **
   ** @throws NullPointerException if <code>header</code> is <code>null</code>.
   */
  public EncryptionObject(final EncryptionHeader header, final Payload payload)
    throws IllegalArgumentException {

    // ensure inheritance
    super(Objects.requireNonNull(payload, "The payload must not be null"));

    // initialize instance attributes
    this.header         = Objects.requireNonNull(header, "The JWE header must not be null");
    this.subject        = null;
    this.state          = State.UNENCRYPTED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new <code>EncryptionObject</code> with the specified
   ** {@link EncodedURL} subjects.
   **
   **
   ** @param  header              the first part, corresponding to the JWE
   **                             header.
   **                             <br>
   **                             Must not be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   ** @param  encryptedKey        the second part, corresponding to the
   **                             encrypted key.
   **                             <br>
   **                             May be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   ** @param  initialization      the third part, corresponding to the
   **                             initialization vector.
   **                             <br>
   **                             May be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   ** @param  cipherText          the forth part, corresponding to the cipher
   **                             text.
   **                             <br>
   **                             May be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   ** @param  integrity           the fith part, corresponding to the integrity
   **                             value.
   **                             <br>
   **                             May be <code>null</code>.
   **                             <br>
   **                             Allowed object is {@link EncodedURL}.
   **
   ** @throws NullPointerException if <code>cipherText</code> is
   **                              <code>null</code>.
   */
  public EncryptionObject(final EncodedURL header, final EncodedURL encryptedKey, final EncodedURL initialization, final EncodedURL cipherText, final EncodedURL integrity) {
    // ensure inheritance
    super(null);

    // initialize instance attributes
    this.header         = EncryptionHeader.from(header);
    this.subject        = new Subject(encryptedKey, initialization, cipherText, integrity);
    // but not decrypted yet!
    this.state          = State.ENCRYPTED;
    segment(header, encryptedKey, initialization, cipherText, integrity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Returns the state of the JSON Web Encryption (JWE) object.
   **
   ** @return                    the state of the JSON Web Encryption (JWE)
   **                            object.
   **                            <br>
   **                            Possible object is {@link State}.
   */
  public final State state() {
    return this.state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Returns the subject of the JSON Web Encryption (JWE) object.
   **
   ** @return                    the subject of the JSON Web Encryption (JWE)
   **                            object.
   **                            <br>
   **                            Possible object is {@link Subject}.
   */
  public final Subject subject() {
    return this.subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   header (JoseObject)
  /**
   ** Returns the header of this JOSE object.
   **
   ** @return                    the header of this JOSE object.
   **                            <br>
   **                            Possible object is {@link EncryptionHeader}.
   */
  @Override
  public final EncryptionHeader header() {
    return this.header;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize (JoseObject)
  /**
   ** Serialises this JOSE object to its compact format consisting of
   ** {@link EncodedURL} subjects delimited by period ('.') characters.
   ** <p>
   ** It must be in a {@link State#ENCRYPTED encrypted} or
   ** {@link State#DECRYPTED decrypted} state.
   ** <pre>
   **   [header].[encryptedKey].[iv].[cipherText].[integrityValue]
   ** </pre>
   **
   ** @return                    the serialized JOSE object.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalStateException if the JOSE object is not in a state that
   **                               permits serialisation.
   */
  @Override
  public final String serialize() {
    ensureEncryptedDecrypted();

    final StringBuilder builder = new StringBuilder(this.header.encoded().toString());
    builder.append('.');

    if (this.subject.key != null)
      builder.append(this.subject.key.toString());
    builder.append('.');

    if (this.subject.i4v != null)
      builder.append(this.subject.i4v.toString());
    builder.append('.');

    builder.append(this.subject.txt.toString());
    builder.append('.');

    if (this.subject.itv != null)
      builder.append(this.subject.itv.toString());

    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a {@link EncryptionObject} from the specified
   ** string in compact format.
   ** <p>
   ** The parsed JWE object will be returned in a {@link State#ENCRYPTED} state.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>EncryptionObject</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>EncryptionObject</code>.
   **
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid JWE object.
   */
  public static EncryptionObject from(final String value)
    throws IllegalArgumentException {

    final EncodedURL[] subject = split(value);
    if (subject.length != 5)
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_ENCRYPTED_ENCODE));

    return new EncryptionObject(subject[0], subject[1], subject[2], subject[3], subject[4]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypts this JWE object with the specified encrypter.
   ** <br>
   ** The JWE object must be in an {@link State#UNENCRYPTED unencrypted} state.
   **
   ** @param  encrypter          The JWE encrypter.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link EncryptionInstance.Encrypter}.
   **
   ** @throws AuthorizationException if the JWE object couldn't be encrypted.
   ** @throws IllegalStateException  if the JWE object is not in an
   **                                {@link State#UNENCRYPTED unencrypted state}.
   */
  public synchronized void encrypt(final EncryptionInstance.Encrypter encrypter)
    throws AuthorizationException {

    ensureUnencrypted();
    ensureSupported(encrypter);
    this.subject = encrypter.encrypt(this.header, this.payload.asByte());
    this.state   = State.ENCRYPTED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Decrypts this JWE object with the specified decrypter.
   ** <br>
   ** The JWE object must be in a {@link State#ENCRYPTED encrypted} state.
   **
   ** @param  decrypter          The JWE encrypter.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link EncryptionInstance.Decrypter}.
   *
   * @throws IllegalStateException  if the JWE object is not in an
   *                                {@link State#ENCRYPTED encrypted state}.
   * @throws AuthorizationException if the JWE object couldn't be decrypted.
   */
  public synchronized void decrypt(final EncryptionInstance.Decrypter decrypter)
    throws AuthorizationException {

    ensureEncrypted();
    ensureAccepted(decrypter);
    this.payload = Payload.of(decrypter.decrypt(this.header, this.subject));
    this.state   = State.DECRYPTED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureSupported
  /**
   ** Ensures the specified JWE encrypter supports the algorithms of this
   ** JWE object.
   **
   ** @throws AuthorizationException if the JWE algorithms are not supported.
   */
  private void ensureSupported(final EncryptionInstance.Encrypter encrypter)
    throws AuthorizationException {

    if (!encrypter.supported().contains(this.header.alg))
      throw AuthorizationException.build(AuthorizationError.CRYPTO_ALGORITHM_UNSUPPORTED, this.header.alg.id);

    if (!encrypter.method().contains(this.header.enc))
      throw AuthorizationException.build(AuthorizationError.CRYPTO_ENCRYPTION_METHOD, this.header.enc.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureAccepted
  /**
   ** Ensures the specified JWE decrypter accepts the algorithms and the
   ** headers of this JWE object.
   **
   ** @throws AuthorizationException if the JWE algorithms or headers are not
   **                                accepted.
   */
  private void ensureAccepted(final EncryptionInstance.Decrypter decrypter)
    throws AuthorizationException {

    final EncryptionHeaderFilter filter = decrypter.filter();
    if (filter == null)
      return;

    if (!filter.acceptedAlgorithm().contains(this.header.alg))
      throw AuthorizationException.build(AuthorizationError.CRYPTO_DECRYPTION_ALGORITHM, this.header.alg.id);

    if (!filter.acceptedMethod().contains(this.header.enc))
      throw AuthorizationException.build(AuthorizationError.CRYPTO_DECRYPTION_METHOD, this.header.enc.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureEncrypted
  /**
   ** Ensures the current state is {@link State#ENCRYPTED encrypted}.
   **
   ** @throws IllegalStateException if the current state is not encrypted.
   */
  private void ensureEncrypted() {
    if (this.state != State.ENCRYPTED)
      throw new IllegalStateException(AuthorizationBundle.string(AuthorizationError.CRYPTO_STATE_ENCRYPTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureUnencrypted
  /**
   ** Ensures the current state is {@link State#UNENCRYPTED unencrypted}.
   **
   ** @throws IllegalStateException if the current state is not unencrypted.
   */
  private void ensureUnencrypted() {
    if (this.state != State.UNENCRYPTED)
      throw new IllegalStateException(AuthorizationBundle.string(AuthorizationError.CRYPTO_STATE_UNENCRYPTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureEncryptedDecrypted
  /**
   ** Ensures the current state is {@link State#ENCRYPTED encrypted} or
   ** {@link State#DECRYPTED decrypted}.
   **
   ** @throws IllegalStateException if the current state is not encrypted or
   **                                decrypted.
   */
  private void ensureEncryptedDecrypted() {
    if (state != State.ENCRYPTED && state != State.DECRYPTED)
      throw new IllegalStateException(AuthorizationBundle.string(AuthorizationError.CRYPTO_STATE_UNDEFINED));
  }
}