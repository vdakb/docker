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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Verifier.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Verifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt.rsa;

import java.util.Objects;

import java.security.Signature;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import oracle.iam.access.foundation.jwt.Algorithm;
import oracle.iam.access.foundation.jwt.InvalidSignaturenException;

////////////////////////////////////////////////////////////////////////////////
// class Verifier
// ~~~~~ ~~~~~~~~
/**
 ** Verify a JSON Web Token with an RSA signature using an RSA Public Key.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Verifier implements oracle.iam.access.foundation.jwt.Verifier {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final PublicKey publicKey;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Verifier</code> to verify a JSON Web Token signature
   ** with the specified public key.
   **
   ** @param  publicKey          the public key to verify.
   */
  public Verifier(final String publicKey) {
    // ensure inheritance
    this(Utility.publicKeyFromPEM(publicKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Verifier</code> to verify a JSON Web Token signature
   ** with the specified public key.
   **
   ** @param  publicKey          the public key to verify.
   */
  public Verifier(final PublicKey publicKey) {
    // ensure inheritance
    super();

    Objects.requireNonNull(publicKey);
//    final int keyLength = publicKey.getModulus().bitLength();
//    if (keyLength < 2048)
//      throw new InvalidKeyLengthException("Key length of [" + keyLength + "] is less than the required key length of 2048 bits.");

    this.publicKey = publicKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept (Verifier)
  /**
   ** @param  algorithm          the algorithm required to verify the signature
   **                            on this JSON Web Token.
   **
   ** @return                    <code>true</code> if this {@link }Verifier} is
   **                            able to verify a signature using the specified
   **                            algorithm.
   */
  @Override
  public boolean accept(final Algorithm algorithm) {
    switch (algorithm) {
      case RS256 :
      case RS384 :
      case RS512 : return true;
      default    : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify (Verifier)
  /**
   ** Verify the signature of the encoded JSON Web Token payload.
   **
   ** @param  algorithm          the algorithm required to verify the signature
   **                            on this JSON Web Token.
   ** @param  payload            The JSON Web Token message.
   **                            The header and claims, the first two segments
   **                            of the dot separated JSON Web Token.
   ** @param  expected           the expected signature to verify.
   **
   * @throws InvalidSignaturenException If the signature is not valid.
   */
  @Override
  public void verify(final Algorithm algorithm, final byte[] payload, final byte[] expected) {
    Objects.requireNonNull(algorithm);
    Objects.requireNonNull(payload);
    Objects.requireNonNull(expected);

    try {
      final Signature signature = Signature.getInstance(algorithm.type());
      signature.initVerify(this.publicKey);
      signature.update(payload);
      if (!signature.verify(expected))
        throw new InvalidSignaturenException();
    }
    catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | SecurityException e) {
      throw new RuntimeException(e);
    }
  }
}