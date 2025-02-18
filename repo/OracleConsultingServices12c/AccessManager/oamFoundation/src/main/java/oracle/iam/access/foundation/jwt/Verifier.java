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

    Purpose     :   This file implements the interface
                    Verifier.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// interface Verifier
// ~~~~~~~~~ ~~~~~~~~
/**
 ** JSON Web Token Verifier.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Verifier {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** @param  algorithm          the algorithm required to verify the signature
   **                            on this JSON Web Token.
   **
   ** @return                    <code>true</code> if this <code>Verifier</code>
   **                            is able to verify a signature using the
   **                            specified algorithm.
   */
  boolean accept(final Algorithm algorithm);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verify
  /**
   ** Verify the signature of the encoded JSON Web Token payload.
   **
   ** @param  algorithm          the algorithm required to verify the signature
   **                            on this JSON Web Token.
   ** @param  message            The JSON Web Token message.
   **                            The header and claims, the first two segments
   **                            of the dot separated JSON Web Token.
   ** @param  signature          the signature to verify.
   **
   ** @throws InvalidSignaturenException If the signature is not valid.
   */
  void verify(final Algorithm algorithm, final byte[] message, final byte[] signature);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a proper verifier for the specified algorithm.
   **
   ** @param  algorithm          the {@link Algorithm} the verifier is based on.
   ** @param  secret             the secret.
   **
   ** @return                    the signer.
   */
  public static Verifier create(final Algorithm algorithm, final String secret) {
    switch (algorithm) {
      case HS256 :
      case HS384 :
      case HS512 : return new oracle.iam.access.foundation.jwt.hmca.Verifier(secret);
      case RS256 :
      case RS384 :
      case RS512 : return new oracle.iam.access.foundation.jwt.rsa.Verifier(secret);
      default    : throw new IllegalArgumentException();
    }
  }
}