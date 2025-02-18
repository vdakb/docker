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

    File        :   StringEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    StringEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

////////////////////////////////////////////////////////////////////////////////
// interface StringEncryptor
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Common interface for all encryptors which receive a String message and
 ** return a String result.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface StringEncryptor extends Encryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypt the clear text message.
   **
   ** @param  message            the message to be encrypted.
   **
   ** @return                    the result of encryption.
   */
  String encrypt(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Decrypt an encrypted string message.
   **
   ** @param  encrypted          the message to be decypted.
   **
   ** @return                    the result of decryption.
   */
  String decrypt(final String encrypted);
}