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

    File        :   MACProvider.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    MACProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.HashSet;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import oracle.hst.platform.core.annotation.ThreadSafety;

import oracle.iam.platform.oauth.AuthorizationError;
import oracle.iam.platform.oauth.AuthorizationException;

////////////////////////////////////////////////////////////////////////////////
// abstract class MACProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** The base class for Message Authentication Code (MAC) signers and verifiers
 ** of {@link oracle.iam.platform.oauth.jwk.SignatureObject JWS objects}.
 ** <p>
 ** Supports the following JSON Web Algorithms (JWA)s:
 ** <ul>
 **   <li>{@link Algorithm#HS256}
 **   <li>{@link Algorithm#HS384}
 **   <li>{@link Algorithm#HS512}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class MACProvider extends SignatureProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The supported JWS algorithms. */
  private static final Set<Algorithm> SUPPORTED;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  /** Initializes the reserved parameter name set. */
  static {
    final Set<Algorithm> alg = new HashSet<Algorithm>();
    alg.add(Algorithm.HS256);
    alg.add(Algorithm.HS384);
    alg.add(Algorithm.HS512);
    SUPPORTED = alg;
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The shared secret. */
  final byte[] material;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Signer
  // ~~~~~ ~~~~~~
  /**
   ** Implementation of a Message Authentication Code (MAC) signer for
   ** {@link oracle.iam.platform.oauth.jwk.SignatureObject JWS object}s.
   ** <p>
   ** Supports the following JSON Web Algorithms (JWA)s:
   ** <ul>
   **   <li>{@link Algorithm#HS256}
   **   <li>{@link Algorithm#HS384}
   **   <li>{@link Algorithm#HS512}
   ** </ul>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  private static class Signer extends    MACProvider
                              implements SignatureInstance.Signer {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a Message Authentication (MAC) signer.
     **
     ** @param  secret           the shared secret.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @throws IllegalArgumentException if <code>secret</code> is
     **                                  <code>null</code> or empty.
     */
    private Signer(final byte[] secret)
      throws IllegalArgumentException {

      // ensure inheritance
      super(secret);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: sign (Signature.Signer)
    /**
     ** Signs the specified {@link SignatureObject#content() signable content}
     ** of a {@link SignatureObject JWS object}.
     **
     ** @param header            the JSON Web Signature (JWS) header.
     **                          Must specify a supported JWS algorithm and must
     **                          not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param content           the content to sign.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the resulting signature part (third part) of
     **                          the JWS object.
     **                          <br>
     **                          Possible object is {@link EncodedURL}.
     **
     ** @throws AuthorizationException if the JWS algorithm is not supported or
     **                                if signing failed for some other reason.
     */
    @Override
    public final EncodedURL sign(final SignatureHeader header, final byte[] content)
      throws AuthorizationException {

      final Mac mac = mac(header.alg);
      try {
        mac.init(new SecretKeySpec(this.material, mac.getAlgorithm()));
      }
      catch (InvalidKeyException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_HMCA_INVALIDKEY, e.getMessage());
      }
      mac.update(content);
      return EncodedURL.of(mac.doFinal());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Verifier
  // ~~~~~ ~~~~~~~~
  /**
   ** Implementation of a Message Authentication Code (MAC) verifier for
   ** {@link oracle.iam.platform.oauth.jwk.SignatureObject JWS object}s.
   ** <p>
   ** Supports the following JSON Web Algorithms (JWA)s:
   ** <ul>
   **   <li>{@link Algorithm#HS256}
   **   <li>{@link Algorithm#HS384}
   **   <li>{@link Algorithm#HS512}
   ** </ul>
   ** Accepts the following JWS header parameters:
   ** <ul>
   **   <li><code>alg</code>
   **   <li><code>typ</code>
   **   <li><code>cty</code>
   ** </ul>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  private static class Verifier extends    MACProvider
                                implements SignatureInstance.Verifier {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The JWS header filter. */
    private final Filter filter;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a Message Authentication (MAC) verifier.
     **
     ** @param  secret           the shared secret.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @throws IllegalArgumentException if <code>secret</code> is
     **                                  <code>null</code> or empty.
     */
    public Verifier(final byte[] secret)
      throws IllegalArgumentException {

      // ensure inheritance
      super(secret);

      // initialize instance attributes
      this.filter = new Filter(SUPPORTED);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter (Signature.Verifier)
    /**
     ** Returns the Json Web Signature (JWS) header filter associated with the
     ** verifier.
     ** <br>
     ** Specifies the names of those
     ** {@link #supported() supported JWS algorithms} and header parameters that
     ** the verifier is configured to accept.
     ** <p>
     ** Attempting to {@link #verify() verify} a JWS object signature with an
     ** algorithm or header parameter that is not accepted must result in a
     ** {@link AuthorizationException}.
     **
     ** @return                  the Json Web Signature (JWS) header filter
     **                          associated with the verifier
     **                          <br>
     **                          Possible object is
     **                          {@link SignatureHeaderFilter}.
     */
    @Override
    public final SignatureHeaderFilter filter() {
      return this.filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: verify (Signature.Verifier)
    /**
     ** Verifies the specified {@link SignatureObject#signature() signature} of
     ** a {@link SignatureObject JWS object}.
     **
     ** @param  header           the JSON Web Signature (JWS) header.
     **                          <br>
     **                          Must specify an accepted JWS algorithm and must
     **                          contain only accepted header parameters,
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link SignatureHeader}.
     ** @param  content          the signed content..
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     ** @param signature         the signature subject of the JWS object.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link EncodedURL}.
     **
     ** @return                  <code>true</code> if the signature was
     **                          successfully verified; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws AuthorizationException if the JWS algorithm is not accepted, if
     **                                a header parameter is not accepted, or if
     **                                signature verification failed for some
     **                                other reason.
     */
    @Override
    public final boolean verify(final SignatureHeader header, final byte[] content, final EncodedURL signature)
      throws AuthorizationException {

      final Mac mac = mac(header.alg);
      try {
        mac.init(new SecretKeySpec(material(), mac.getAlgorithm()));
      }
      catch (InvalidKeyException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_HMCA_INVALIDKEY, e.getMessage());
      }
      mac.update(content);
      final EncodedURL expected = EncodedURL.of(mac.doFinal());
      return (expected.equals(signature));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a Message Authentication (MAC) provider.
   **
   ** @param  material           the shared secret.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @throws IllegalArgumentException if <code>secret</code> is
   **                                  <code>null</code> or empty.
   */
  private MACProvider(final byte[] material)
    throws IllegalArgumentException {

    // ensure inheritance
    super(SUPPORTED);

    // prevent bogus input
    if (material == null)
      throw new IllegalArgumentException("The shared secret must not be null");

    // initialize instance attributes
    this.material = material;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secret
  /**
   ** Returns the shared secret of this Message Authentication (MAC) provider.
   **
   ** @return                    the shared secret of this Message
   **                            Authentication (MAC) provider.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  public final byte[] material() {
    return this.material;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signer
  /**
   ** Factory method to create a Message Authentication Code (MAC) signer for
   ** the specified HMAC-based JSON Web Algorithm (JWA).
   **
   ** @param  secret             the shared secret.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the {@link SignatureInstance.Signer} with the
   **                            shared secret populated.
   **                            <br>
   **                            Possible object is
   **                            {@link SignatureInstance.Signer}.
   */
  public static SignatureInstance.Signer signer(final byte[] secret) {
    return new Signer(secret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifier
  /**
   ** Factory method to create a Message Authentication Code (MAC) verifier for
   ** the specified HMAC-based JSON Web Algorithm (JWA).
   **
   ** @param  secret             the shared secret.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the {@link SignatureInstance.Verifier} with the
   **                            shared secret populated.
   **                            <br>
   **                            Possible object is
   **                            {@link SignatureInstance.Verifier}.
   */
  public static SignatureInstance.Verifier verifier(final byte[] secret) {
    return new Verifier(secret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mac
  /**
   ** Factory method to returns a Message Authentication Code (MAC) service for
   ** the specified HMAC-based JSON Web Algorithm (JWA).
   **
   ** @param  algorithm          the JSON Web Algorithm (JWA).
   **                            <br>
   **                            Must not be supported.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Algorithm}.
   **
   ** @return                    A MAC service instance.
   **                            <br>
   **                            Possible object is {@link Mac}.
   **
   ** @throws AuthorizationException if the algorithm is not supported.
   */
  protected static Mac mac(final Algorithm algorithm)
    throws AuthorizationException {

    try {
      // the internal crypto provider uses different algorithm names
      switch (algorithm) {
        case HS256 : return Mac.getInstance("HMACSHA256");
        case HS384 : return Mac.getInstance("HMACSHA384");
        case HS512 : return Mac.getInstance("HMACSHA512");
        default    : throw AuthorizationException.build(AuthorizationError.SIGNING_HMCA_INVALIDTYPE);
      }
    }
    catch (NoSuchAlgorithmException e) {
      throw AuthorizationException.build(AuthorizationError.SIGNING_ALGORITHM_EXCEPTION, e.getMessage());
    }
  }
}