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

    File        :   Signer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Signer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt.rsa;

import java.util.Objects;

import java.security.Signature;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.nio.charset.StandardCharsets;

import oracle.iam.access.foundation.jwt.Algorithm;

////////////////////////////////////////////////////////////////////////////////
// class Signer
// ~~~~~ ~~~~~~
/**
 ** Signing a JSON Web Token using an RSA Private key.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Signer implements oracle.iam.access.foundation.jwt.Signer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Algorithm algorithm;
  private PrivateKey      privateKey;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Signer</code> that supports the specified
   ** {@link Algorithm} to sign a JSON Web Token with the specified private key.
   **
   ** @param  algorithm          the crypto sign algorithm.
   ** @param  privateKey         the private key to sign.
   */
  public Signer(final Algorithm algorithm, final String privateKey) {
    // ensure inheritance
    this(algorithm, Utility.privateKeyFromPEM(privateKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Signer</code> that supports the specified
   ** {@link Algorithm} to sign a JSON Web Token with the specified private key.
   **
   ** @param  algorithm          the crypto sign algorithm.
   ** @param  privateKey         the private key to sign.
   */
  public Signer(final Algorithm algorithm, final PrivateKey privateKey) {
    // ensure inheritance
    super();

    Objects.requireNonNull(algorithm);
    Objects.requireNonNull(privateKey);
    this.algorithm  = algorithm;
    this.privateKey = privateKey;

//    int keyLength = this.privateKey.getModulus().bitLength();
//    if (keyLength < 2048) {
//      throw new InvalidKeyLengthException("Key length of [" + keyLength + "] is less than the required key length of 2048 bits.");
//    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Signer)
  /**
   ** Return the algorithm supported by this signer.
   **
   ** @return                    the supported algorithm.
   */
  @Override
  public Algorithm algorithm() {
    return this.algorithm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sign (Signer)
  /**
   ** Sign the provided message and return the signature.
   **
   ** @param  payload            the JSON Web Token payload to sign.
   **
   ** @return                    the message signature in a byte array.
   */
  public byte[] sign(String payload) {
    try {
      Signature signature = Signature.getInstance(algorithm.type());
      signature.initSign(privateKey);
      signature.update(payload.getBytes(StandardCharsets.UTF_8));
      return signature.sign();
    }
    catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
      throw new RuntimeException(e);
    }
  }
}