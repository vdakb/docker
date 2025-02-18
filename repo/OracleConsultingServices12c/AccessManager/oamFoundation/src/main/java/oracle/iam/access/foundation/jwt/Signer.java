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

    Purpose     :   This file implements the interface
                    Signer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// interface Signer
// ~~~~~~~~~ ~~~~~~
/**
 ** JSON Web Token Signer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Signer {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
  /**
   ** Return the algorithm supported by this signer.
   **
   ** @return                    the supported algorithm.
   */
  Algorithm algorithm();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sign
  /**
   ** Sign the provided message and return the signature.
   **
   ** @param  payload            the JSON Web Token payload to sign.
   **
   ** @return                    the message signature in a byte array.
   */
  byte[] sign(final String payload);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a proper signer for the specified algorithm.
   **
   ** @param  algorithm          the {@link Algorithm} the signer is based on.
   ** @param  secret             the secret to sign.
   **
   ** @return                    the signer.
   */
  public static Signer create(final Algorithm algorithm, final String secret) {
    switch (algorithm) {
      case HS256 :
      case HS384 :
      case HS512 : return new oracle.iam.access.foundation.jwt.hmca.Signer(algorithm, secret);
      case RS256 :
      case RS384 :
      case RS512 : return new oracle.iam.access.foundation.jwt.rsa.Signer(algorithm, secret);
      default    : throw new IllegalArgumentException();
    }
  }
}