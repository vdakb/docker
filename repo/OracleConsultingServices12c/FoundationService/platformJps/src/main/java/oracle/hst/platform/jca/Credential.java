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

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Credential.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    Credential.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.jca;

import java.security.PublicKey;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

////////////////////////////////////////////////////////////////////////////////
// interface Credential
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** A credential for an entity.
 ** <br>
 ** A particular credential may contain either asymmetric key information (a
 ** public key  and optionally the corresponding private key), or a symmetric
 ** (secret) key, but never both.
 ** <p>
 ** With asymmetric key-based credentials, local entity credentials will usually
 ** contain both a public and private key while peer credentails will normally
 ** contain only a public key.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Credential {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Usage
  // ~~~~ ~~~~~
  /**
   ** Credential usage types.
   */
  static enum Usage {
      /** Denotes that the purpose of the key was not specified. */
      UNSPECIFIED
      /** Key used for encryption processes. */
    , ENCRYPTION
      /** Key used for signature processes including TLS/SSL. */
    , SIGNING
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the primary type of the credential instance.
   ** <br>
   ** This will usually be the primary sub-interface of {@link Credential}
   ** implemented by an implementation.
   **
   ** @return                    the credential type.
   **                            <br>
   **                            Possible object is {@link Class} for type
   **                            <code>Credential</code>.
   */
  public Class<? extends Credential> type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityId
  /**
   ** Returns the unique ID of the entity this credential is for.
   **
   ** @return                    the unique ID of the entity this credential is
   **                            for.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String entityId();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Returns usage type of this credential.
   **
   ** @return                    the usage type of this credential.
   **                            <br>
   **                            Possible object is {@link Usage}.
   */
  Usage usage();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   usage
  /**
   ** Returns the public key for the entity.
   **
   ** @return                    the public key for the entity.
   **                            <br>
   **                            Possible object is {@link PublicKey}.
   */
  PublicKey publicKey();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   privateKey
  /**
   ** Returns the private key for the entity if there is one.
   **
   ** @return                    the private key for the entity.
   **                            <br>
   **                            Possible object is {@link PrivateKey}.
   */
  PrivateKey privateKey();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secretKey
  /**
   ** Returns the secret key for this entity.
   **
   ** @return                    the secret key for this entity.
   **                            <br>
   **                            Possible object is {@link PrivateKey}.
   */
  SecretKey secretKey();
}