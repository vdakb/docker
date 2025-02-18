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

    File        :   SignatureObject.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SignatureObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Objects;

import java.io.UnsupportedEncodingException;

import oracle.hst.platform.core.annotation.ThreadSafety;

import oracle.iam.platform.oauth.AuthorizationError;
import oracle.iam.platform.oauth.AuthorizationBundle;
import oracle.iam.platform.oauth.AuthorizationException;

////////////////////////////////////////////////////////////////////////////////
// class SignatureObject
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** JSON Web Signature (JWS) object.
 ** <p>
 ** This class is thread-safe.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
public class SignatureObject extends JoseObject {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8207801428679796341")
  private static final long    serialVersionUID = -4861535042091998711L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The header. */
  public final SignatureHeader header;

  /** The signature or <code>null</code> if not signed. */
  private EncodedURL           signature;

  /** The JWE object state. */
  private State                state;

  /**
   ** The signable content of this JWS object.
   ** <p>
   ** Format:
   ** <pre>
   **   [header].[payload]
   ** </pre>
   */
  private byte[]               content;

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
      /** The JWS object is created but not signed yet. */
      UNSIGNED
      /** The JWS object is signed but its signature is not verified. */
    , SIGNED
      /** The JWS object is signed and its signature was successfully verified. */
    , VERIFIED
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a new to-be-signed JSON Web Signature (JWS) object with the
   ** specified {@link SignatureHeader} <code>header</code> and {@link Payload}
   ** <code>paylaod</code>.
   ** <br>
   ** The initial state will be {@link State#UNSIGNED unsigned}.
   **
   ** @param  header             the {@link SignatureHeader}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureHeader}.
   ** @param  payload            the {@link Payload} to sign.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Payload}.
   */
  protected SignatureObject(final SignatureHeader header, final Payload payload) {
    // ensure inheritance
    super(Objects.requireNonNull(payload, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_PAYLOAD)));

    // initialize instance attributes
    this.header    = Objects.requireNonNull(header, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_SIGNATURE));
    this.signature = null;
    this.state     = State.UNSIGNED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new signed JSON Web Signature (JWS) object with the specified
   ** serialized subjects.
   ** <br>
   ** The initial state will be {@link State#SIGNED unsigned}.
   **
   ** @param  header             the first part, correspondending to the
   **                            JWS header.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  payload            the second part, correspondending to the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  signature          the third part, correspondending to the
   **                            signature.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  protected SignatureObject(final EncodedURL header, final EncodedURL payload, final EncodedURL signature) {
    // ensure inheritance
    super(Payload.of(payload));

    // initialize instance attributes
    this.header    = SignatureHeader.from(header);
    this.signature = Objects.requireNonNull(signature, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_SIGNATURE));
    // but signature not verified yet!
    this.state     = State.SIGNED;

    content(header, payload);
    segment(header, payload, signature);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signature
  /**
   ** Returns the signature of this JWS object.
   **
   ** @return                    the signature of this JWS object.
   **                            <br>
   **                            Possible object is {@link EncodedURL}.
   */
  public EncodedURL signature() {
    return this.signature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   content
  /**
   ** Returns the signable content of this JWS object.
   ** <p>
   ** Format:
   ** <pre>
   **   [header].[payload]
   ** </pre>
   **
   ** @return                    the signable content, ready for passing to the
   **                            signing or verification service.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public byte[] content() {
    return this.content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   state
  /**
   ** Returns the state of the JSON Web Signature (JWS) object.
   **
   ** @return                    the state of the JSON Web Signature (JWS)
   **                            object.
   **                            <br>
   **                            Possible object is {@link State}.
   */
  public final State state() {
    return this.state;
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
   **                            Possible object is {@link SignatureHeader}.
   */
  @Override
  public final SignatureHeader header() {
    return this.header;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize (JoseObject)
  /**
   ** Serialises this JOSE object to its compact format consisting of
   ** {@link EncodedURL} subjects delimited by period ('.') characters.
   ** <p>
   ** It must be in a {@link State#SIGNED signed} or
   ** {@link State#VERIFIED verified} state.
   ** <pre>
   **   [header].[signature]
   ** </pre>
   **
   ** @return                    the serialized JOSE object.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalStateException  if the JWS object is not in an
   **                                {@link State#SIGNED signed state} or
   **                                {@link State#VERIFIED verified state}.
   */
  @Override
  public final String serialize() {
    // ensures the current state is signed or verifiied to prevent bogus state
    if (state != State.SIGNED && state != State.VERIFIED)
      throw new IllegalStateException(AuthorizationBundle.string(AuthorizationError.SIGNING_STATE_UNDEFINED));

    final StringBuilder builder = new StringBuilder(this.header.encoded().toString());
    builder.append('.');
    builder.append(payload().encoded().toString());
    builder.append('.');
    builder.append(this.signature.toString());
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a {@link SignatureObject} from the specified
   ** string in compact format.
   ** <p>
   ** The parsed JWS object will be returned in a {@link State#SIGNED} state.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed <code>SignatureObject</code>.
   **                            <br>
   **                            Possible object is <code>SignatureObject</code>.
   **
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid JWS object.
   */
  public static SignatureObject from(final String value)
    throws IllegalArgumentException {

    final EncodedURL[] subject = split(value);
    if (subject.length != 3)
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_ENCODE));

    return of(subject[0], subject[1], subject[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Factory method to create a {@link SignatureObject} from the specified
   ** string in compact format.
   ** <p>
   ** The parsed JWS object will be returned in a {@link State#SIGNED} state.
   **
   ** @param  value              the string to parse.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  detached           the {@link Payload} to sign or verify.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Payload}.
   **
   ** @return                    the parsed <code>SignatureObject</code>.
   **                            <br>
   **                            Possible object is <code>SignatureObject</code>.
   **
   ** @throws NullPointerException     if one of the header segments is
   **                                  <code>null</code>.
   ** @throws IllegalArgumentException if the string couldn't be parsed to a
   **                                  valid JWS object.
   */
  public static SignatureObject from(final String value, final Payload detached)
    throws IllegalArgumentException {

    final EncodedURL[] subject = split(value);
    if (subject.length != 3)
      throw new IllegalArgumentException(AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_ENCODE));

		if (!subject[1].toString().isEmpty())
			throw new IllegalArgumentException("The encoded payload must be empty");

    return of(subject[0], Objects.requireNonNull(detached, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_PAYLOAD)).encoded(), subject[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a to-be-signed JSON Web Signature (JWS) object
   ** with the specified {@link SignatureHeader} <code>header</code> and
   ** {@link Payload} <code>paylaod</code>.
   ** <br>
   ** The initial state will be {@link State#UNSIGNED unsigned}.
   **
   ** @param  header             the {@link SignatureHeader}.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link SignatureHeader}.
   ** @param  payload            the {@link Payload} to sign.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Payload}.
   **
   ** @return                    the <code>SignatureObject</code> with the
   **                            {@link SignatureHeader} <code>header</code> and
   **                            {@link Payload} <code>paylaod</code> populated.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureObject</code>.
   **
   ** @throws NullPointerException if the either <code>header</code>,
   **                              <code>payload</code> is <code>null</code>.
   */
  public static SignatureObject of(final SignatureHeader header, final Payload payload) {
    // prevent bogus input
    return new SignatureObject(
      Objects.requireNonNull(header,  AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_HEADER))
    , Objects.requireNonNull(payload, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_PAYLOAD))
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a signed JSON Web Signature (JWS) object with the
   ** specified serialized subjects.
   ** <br>
   ** The initial state will be {@link State#SIGNED unsigned}.
   **
   ** @param  header             the first part, correspondending to the
   **                            JWS header.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  payload            the second part, correspondending to the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  signature          the third part, correspondending to the
   **                            signature.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   **
   ** @return                    the <code>SignatureObject</code> with the
   **                            specified serialized subjects populated.
   **                            <br>
   **                            Possible object is
   **                            <code>SignatureObject</code>.
   **
   ** @throws NullPointerException if the either <code>header</code>,
   **                              <code>payload</code> or
   **                              <code>signature</code> is <code>null</code>.
   */
  public static SignatureObject of(final EncodedURL header, final EncodedURL payload, final EncodedURL signature) {
    // prevent bogus input
    return new SignatureObject(
      Objects.requireNonNull(header,    AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_HEADER))
    , Objects.requireNonNull(payload,   AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_PAYLOAD))
    , Objects.requireNonNull(signature, AuthorizationBundle.string(AuthorizationError.TOKEN_SIGNED_SIGNATURE))
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sign
  /**
   ** Signs this JWS object with the specified signer.
   ** <br>
   ** The JWS object must be in a {@link State#UNSIGNED unsigned} state.
   **
   ** @param  signer             the JWS signer.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link SignatureInstance.Signer}.
   **
   ** @throws AuthorizationException if the JWS object couldn't be signed.
   ** @throws IllegalStateException  if the JWS object is not in an
   **                                {@link State#UNSIGNED unsigned state}.
   */
  public synchronized void sign(final SignatureInstance.Signer signer)
    throws AuthorizationException {

    // prevent bogus state
    if (this.state != State.UNSIGNED)
      throw AuthorizationException.build(AuthorizationError.SIGNING_STATE_UNSIGNED);

    // ensures the specified JWS signer supports the algorithms required
    if (!signer.supported().contains(this.header.alg))
      throw AuthorizationException.build(AuthorizationError.SIGNING_ALGORITHM_UNSUPPORTED, this.header.alg.id);

    this.signature = signer.sign(header(), content());
    this.state     = State.SIGNED;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Verifies the signature of this JWS object with the specified verifier.
   ** <br>
   ** The JWS object must be in a {@link State#SIGNED signed} state.
   **
   ** @param  verifier           the JWS verifier.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is
   **                            {@link SignatureInstance.Verifier}.
   **
   ** @return                    <code>true</code> if the signature was
   **                            successfully verified; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws AuthorizationException if the JWS object couldn't be verified.
   ** @throws IllegalStateException  if the JWS object is not in an
   **                                {@link State#SIGNED signed state} or
   **                                {@link State#VERIFIED verified state}.
   */
  public synchronized boolean verify(final SignatureInstance.Verifier verifier)
    throws AuthorizationException {

    // ensures the current state is signed or verifiied to prevent bogus state
    if (this.state != State.SIGNED && state != State.VERIFIED)
      throw AuthorizationException.build(AuthorizationError.SIGNING_STATE_UNDEFINED);

    ensureAccepted(verifier);
    boolean verified = verifier.verify(this.header, this.content, this.signature);
    if (verified)
      this.state = State.VERIFIED;

    return verified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   content
  /**
   ** Sets the signable content of this JWS object.
   ** <p>
   ** Format:
   ** <pre>
   **   [header].[payload]
   ** </pre>
   **
   ** @param  header             the first part, correspondending to the
   **                            JWS header.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   ** @param  payload            the second part, correspondending to the
   **                            payload.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link EncodedURL}.
   */
  private void content(final EncodedURL header, final EncodedURL payload) {
    final StringBuilder builder = new StringBuilder(header.toString());
    builder.append('.');
    builder.append(payload.toString());

    try {
      this.content = builder.toString().getBytes(EncodedURL.CHARSET);
    }
    catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureAccepted
  /**
   ** Ensures the specified JWS verifier accepts the algorithms and the
   ** headers of this JWS object.
   **
   ** @throws AuthorizationException if the JWS algorithms or headers are not
   **                                accepted.
   */
  private void ensureAccepted(final SignatureInstance.Verifier verifier)
    throws AuthorizationException {

    final SignatureHeaderFilter filter = verifier.filter();
    if (filter == null)
      return;

    final Set<Algorithm> algorithm = filter.acceptedAlgorithm();
    if (!algorithm.contains(this.header.alg))
      throw AuthorizationException.build(AuthorizationError.SIGNING_ALGORITHM_NOTACCEPTED, this.header.alg.id());
  }
}