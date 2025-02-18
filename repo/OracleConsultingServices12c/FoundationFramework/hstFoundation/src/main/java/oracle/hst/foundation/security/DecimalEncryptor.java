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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared security functions

    File        :   DecimalEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    DecimalEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.math.BigDecimal;

////////////////////////////////////////////////////////////////////////////////
// interface DecimalEncryptor
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Common interface for all encryptors which receive a BigDecimal (arbitrary
 ** precision) message and return a BigDecimal result.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The size of the result of encrypting a number, depending on the algorithm,
 ** may be much bigger (in bytes) than the size of the encrypted number itself.
 ** For example, encrypting a 4-byte integer can result in an encrypted 16-byte
 ** number. This can lead the user into problems if the encrypted values are to
 ** be stored and not enough room has been provided.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface DecimalEncryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypt the input message.
   **
   ** @param  message            the message to be encrypted.
   **
   ** @return                    the result of encryption.
   */
  BigDecimal encrypt(final BigDecimal message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Decrypt an encrypted message.
   **
   ** @param  encrypted          the message to be decypted.
   **
   ** @return                    the result of decryption.
   */
  BigDecimal decrypt(final BigDecimal encrypted);
}