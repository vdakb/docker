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

package oracle.iam.access.foundation.jwt.hmca;

import java.util.Objects;

import java.nio.charset.StandardCharsets;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;

import oracle.iam.access.foundation.jwt.Algorithm;

////////////////////////////////////////////////////////////////////////////////
// class Signer
// ~~~~~ ~~~~~~
/**
 ** Signing a JSON Web Token using an HMAC.
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
  private final byte[]    secret;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Signer</code> that supports the specified
   ** {@link Algorithm} to sign a JSON Web Token with the specified secret.
   **
   ** @param  algorithm          the crypto sign algorithm.
   ** @param  secret             the secret to sign.
   */
  public Signer(final Algorithm algorithm, final String secret) {
    // ensure inheritance
    super();

    Objects.requireNonNull(algorithm);
    Objects.requireNonNull(secret);
    this.algorithm = algorithm;
    this.secret    = secret.getBytes(StandardCharsets.UTF_8);
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
  public byte[] sign(final String payload) {
    Objects.requireNonNull(algorithm);
    Objects.requireNonNull(secret);

    try {
      final Mac mac = Mac.getInstance(algorithm.type());
      mac.init(new SecretKeySpec(secret, algorithm.type()));
      return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
    }
    catch (InvalidKeyException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}