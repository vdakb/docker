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

    File        :   Asymmetric.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Asymmetric.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth.jwk;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.security.cert.X509Certificate;

////////////////////////////////////////////////////////////////////////////////
// interface Asymmetric
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** The asymmetric (pair) JSON Web Key (JWK).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Asymmetric {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey
  /**
   ** Returns a standard {@link PrivateKey} representation of this
   ** <code>Asymmetric</code> JSON Web Key (JWK).
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                     the standard {@link PrivateKey}
   **                             representation of this
   **                             <code>Asymmetric</code> JSON Web Key
   **                             (JWK) or <code>null</code> if not specified.
   **                             <br>
   **                             Possible object is {@link PrivateKey}.
   **
   ** @throws JoseException       if conversion failed or is not supported by
   **                             the underlying Java Cryptography (JCA)
   **                             provider or key spec parameters are invalid
   **                             for a {@link PrivateKey}.
   */
  PrivateKey privateKey()
    throws JoseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicKey
  /**
   ** Returns a standard {@link PublicKey} representation of this
   ** <code>Asymmetric</code> JSON Web Key (JWK).
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the standard {@link PublicKey} representation
   **                            of this <code>Asymmetric</code> JSON Web Key
   **                            (JWK) or <code>null</code> if not specified.
   **                             <br>
   **                             Possible object is {@link PublicKey}.
   **
   ** @throws JoseException       if conversion failed or is not supported by
   **                             the underlying Java Cryptography (JCA)
   **                             provider or key spec parameters are invalid
   **                             for a {@link PublicKey}.
   */
  PublicKey publicKey()
    throws JoseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keyPair
  /**
   ** Returns a standard {@link KeyPair} representation of this
   ** <code>Asymmetric</code> JSON Web Key (JWK).
   ** <p>
   ** Uses the default JCA provider.
   **
   ** @return                    the standard {@link KeyPair} representation
   **                            of this <code>Asymmetric</code> JSON Web Key
   **                            (JWK) or <code>null</code> if not specified.
   **                             <br>
   **                             Possible object is {@link PublicKey}.
   **
   ** @throws JoseException       if conversion failed or is not supported by
   **                             the underlying Java Cryptography (JCA)
   **                             provider or key spec parameters are invalid
   **                             for a {@link PublicKey}.
   */
  KeyPair keyPair()
    throws JoseException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Returns <code>true</code> if the public key material of this
   ** <code>Asymmetric</code> JSON Web Key matches the public subject key info
   ** of the specified X.509 certificate.
   **
   ** @param  certificate        the X.509 certificate.
   **                            <br>
   **                            Must not be <code>null</code>.
   **
   ** @return                    <code>true</code> if the public key material of
   **                            this <code>Asymmetric</code> JSON Web Key
   **                            matches the public subject key info of the
   **                            specified X.509 certificate; otherwise
   **                            <code>false</code>.
   */
  boolean match(final X509Certificate certificate);
}