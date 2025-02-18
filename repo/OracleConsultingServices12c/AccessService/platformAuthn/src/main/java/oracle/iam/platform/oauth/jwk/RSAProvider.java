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

    File        :   RSAProvider.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RSAProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.util.Set;
import java.util.Objects;

import java.security.Signature;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

import oracle.hst.platform.core.annotation.ThreadSafety;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.iam.platform.oauth.AuthorizationError;
import oracle.iam.platform.oauth.AuthorizationException;

////////////////////////////////////////////////////////////////////////////////
// abstract class RSAProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** The base class for RSA Signature-Scheme-with-Appendix (RSASSA) signers and
 ** verifiers of {@link SignatureObject JWS objects}.
 ** <p>
 ** Expects a public RSA key.
 ** <p>
 ** Refere to RFC 7518
 ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-3.3">RFC 7518, section 3.3. Digital Signature with RSASSA-PKCS1-v1_5</a>
 ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-3.5">RFC 7518, section 3.5. Digital Signature with RSASSA-PSS</a>
 ** for more information.
 ** <p>
 ** Supports the following JSON Web Algorithms (JWA)s:
 ** <ul>
 **   <li>{@link Algorithm#RS256}
 **   <li>{@link Algorithm#RS384}
 **   <li>{@link Algorithm#RS512}
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class RSAProvider extends SignatureProvider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The supported JWS algorithms. */
  private static final Set<Algorithm> SUPPORTED = CollectionUtility.set(Algorithm.RS256, Algorithm.RS384, Algorithm.RS512);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Signer
  // ~~~~~ ~~~~~~
  /**
   ** Implementation of a RSA Signature-Scheme-with-Appendix (RSASSA) signer for
   ** {@link oracle.iam.platform.oauth.jwk.SignatureObject JWS object}s.
   ** <p>
   ** Supports the following JSON Web Algorithms (JWA)s:
   ** <ul>
   **   <li>{@link Algorithm#RS256}
   **   <li>{@link Algorithm#RS384}
   **   <li>{@link Algorithm#RS512}
   ** </ul>
   ** This class is thread-safe.
   */
  @ThreadSafety(level=ThreadSafety.Level.COMPLETELY)
  private static class Signer extends    RSAProvider
                              implements SignatureInstance.Signer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The private RSA key. */
    private final RSAPrivateKey material;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a RSA Signature-Scheme-with-Appendix (RSASSA) signer.
     **
     ** @param  material         the private RSA key.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPrivateKey}.
     **
     ** @throws NullPointerException if <code>secret</code> is
     **                              <code>null</code>.
     */
    private Signer(final RSAPrivateKey material) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.material = Objects.requireNonNull(material, "The RSA private key material must not be null");
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

      final Signature signer = rsa(header.alg);
      try {
        signer.initSign(this.material);
        signer.update(content);
        return EncodedURL.of(signer.sign());
      }
      catch (InvalidKeyException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_PRIVATEKEY, e.getMessage());
      }
      catch (SignatureException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_SIGNATURE, e.getMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Verifier
  // ~~~~~ ~~~~~~~~
  /**
   ** Implementation of a RSA Signature-Scheme-with-Appendix (RSASSA) verifier
   ** for {@link SignatureObject JWS object}s.
   ** <p>
   ** Refere to RFC 7518
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-3.3">RFC 7518, section 3.3. Digital Signature with RSASSA-PKCS1-v1_5</a>
   ** @see <a href="https://datatracker.ietf.org/doc/html/rfc7518#section-3.5">RFC 7518, section 3.5.  Digital Signature with RSASSA-PSS</a>
   ** for more information.
   ** <p>
   ** Supports the following JSON Web Algorithms (JWA)s:
   ** <ul>
   **   <li>{@link Algorithm#RS256}
   **   <li>{@link Algorithm#RS384}
   **   <li>{@link Algorithm#RS512}
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
  private static class Verifier extends    RSAProvider
                                implements SignatureInstance.Verifier {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The public RSA key. */
    private final RSAPublicKey material;

    /** The JWS header filter. */
    private final Filter       filter;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a RSA Signature-Scheme-with-Appendix (RSASSA) verifier.
     **
     ** @param  material         the RSA JSON Web Key (JWK) material.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAKey}.
     **
     ** @throws NullPointerException if <code>material</code> is
     **                              <code>null</code>.
     ** @throws JoseException        if conversion failed or is not supported by
     **                              the underlying Java Cryptography (JCA)
     **                              provider or key spec parameters are invalid
     **                              for a {@link PublicKey}.
     */
    public Verifier(final RSAKey material)
      throws JoseException {

      // ensure inheritance
      this(Objects.requireNonNull(material, "The RSA key material must not be null").rsaPublicKey());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Contructs a RSA Signature-Scheme-with-Appendix (RSASSA) verifier.
     **
     ** @param  material         the RSA public key material.
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link RSAPublicKey}.
     **
     ** @throws NullPointerException if <code>secret</code> is
     **                              <code>null</code>.
     */
    public Verifier(final RSAPublicKey material) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.material = Objects.requireNonNull(material, "The RSA key material must not be null");
      this.filter   = new Filter(SUPPORTED);
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
     ** {@link JOSEException}.
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

      final Signature verifier = rsa(header.alg);
      try {
        verifier.initVerify(this.material);
        verifier.update(content);
        return verifier.verify(signature.decode());
      }
      catch (InvalidKeyException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_PUBLICKEY, e.getMessage());
      }
      catch (SignatureException e) {
        throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_SIGNATURE, e.getMessage());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Contructs a RSA Signature-Scheme-with-Appendix (RSASSA).
   */
  protected RSAProvider() {
    // ensure inheritance
    super(SUPPORTED);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   signer
  /**
   ** Factory method to create a RSA Signature-Scheme-with-Appendix (RSASSA)
   ** signer for the specified {@link RSAPrivateKey}.
   **
   ** @param  secret             the private RSA key.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPrivateKey}.
   **
   ** @return                    the {@link SignatureInstance.Signer} with the
   **                            shared secret populated.
   **                            <br>
   **                            Possible object is
   **                            {@link SignatureInstance.Signer}.
   **
   ** @throws NullPointerException if <code>secret</code> is <code>null</code>.
   */
  public static SignatureInstance.Signer signer(final RSAPrivateKey secret) {
    return new Signer(secret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifier
  /**
   ** Factory method to create a RSA Signature-Scheme-with-Appendix (RSASSA)
   ** verifier for the specified {@link RSAKey}.
   **
   ** @param  material           the RSA JSON Web Key (JWK) material.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAKey}.
   **
   ** @return                    the {@link SignatureInstance.Verifier} with the
   **                            shared secret populated.
   **                            <br>
   **                            Possible object is
   **                            {@link SignatureInstance.Verifier}.
   **
   ** @throws NullPointerException if <code>secret</code> is <code>null</code>.
   ** @throws JoseException        if conversion failed or is not supported by
   **                              the underlying Java Cryptography (JCA)
   **                              provider or key spec parameters are invalid
   **                              for a {@link RSAKey material}.
   */
  public static SignatureInstance.Verifier verifier(final RSAKey material)
    throws JoseException {

    return new Verifier(material);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifier
  /**
   ** Factory method to create a RSA Signature-Scheme-with-Appendix (RSASSA)
   ** verifier for the specified {@link RSAPublicKey}.
   **
   ** @param  material           the public RSA key.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link RSAPublicKey}.
   **
   ** @return                    the {@link SignatureInstance.Verifier} with the
   **                            shared secret populated.
   **                            <br>
   **                            Possible object is
   **                            {@link SignatureInstance.Verifier}.
   **
   ** @throws NullPointerException if <code>secret</code> is <code>null</code>.
   */
  public static SignatureInstance.Verifier verifier(final RSAPublicKey material) {
    return new Verifier(material);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rsa
  /**
   ** Factory method to returns an RSA signer and verifier for the specified
   ** RSASSA-based JSON Web Algorithm (JWA).
   **
   ** @param  algorithm          the JSON Web Algorithm (JWA).
   **                            <br>
   **                            Must not be supported.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Algorithm}.
   **
   ** @return                    an RSA signer and verifier instance.
   **                            <br>
   **                            Possible object is {@link Signature}.
   **
   ** @throws AuthorizationException if the algorithm is not supported.
   */
  protected static Signature rsa(final Algorithm algorithm)
    throws AuthorizationException {

    try {
      // the internal crypto provider uses different alg names
      switch (algorithm) {
        case RS256 : return Signature.getInstance("SHA256withRSA");
        case RS384 : return Signature.getInstance("SHA384withRSA");
        case RS512 : return Signature.getInstance("SHA512withRSA");
        default    : throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_INVALIDTYPE);
      }
    }
    catch (NoSuchAlgorithmException e) {
      throw AuthorizationException.build(AuthorizationError.SIGNING_RSA_UNSUPPORTED, e.getMessage());
    }
  }
}