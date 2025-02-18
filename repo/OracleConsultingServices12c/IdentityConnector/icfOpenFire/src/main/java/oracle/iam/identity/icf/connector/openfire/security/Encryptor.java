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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Encryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Encryptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.security;

////////////////////////////////////////////////////////////////////////////////
// interface Encryptor
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Common interface for all encryptors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Encryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   seed
  /**
   ** Set the encryption secret.
   ** <br>
   ** This will apply the user-defined key, truncated or filled (via the default
   ** key) as needed  to meet the key length specifications.
   **
   ** @param  value              the encryption secret.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void seed(final String value);

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypt the clear text message.
   **
   ** @param  message            the message to be encrypted.
   **                            <br>
   **                            Allowed object is {@link String}..
   ** @param  vector             the initialization vector (IV) to use, or
   **                            <code>null</code> for the default
   **                            initialization value.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   **
   ** @return                    the result of encryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String encrypt(final String message, final byte[] vector);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt
  /**
   ** Encrypt the clear text message.
   **
   ** @param  message            the message to be encrypted.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of encryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String encrypt(final String message);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Decrypt an encrypted string message.
   **
   ** @param  encrypted          the message to be decypted.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  vector             the initialization vector (IV) to use, or
   **                            <code>null</code> for the default
   **                            initialization value.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>s.
   **
   ** @return                    the result of decryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String decrypt(final String encrypted, final byte[] vector);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt
  /**
   ** Decrypt an encrypted string message.
   **
   ** @param  encrypted          the message to be decypted.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of decryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String decrypt(final String encrypted);
}