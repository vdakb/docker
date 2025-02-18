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

    File        :   PBEDecimalEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PBEDecimalEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import java.math.BigDecimal;
import java.math.BigInteger;

import oracle.hst.foundation.utility.NumberUtility;

import oracle.hst.foundation.security.DecimalEncryptor;
import oracle.hst.foundation.security.EncryptionException;

////////////////////////////////////////////////////////////////////////////////
// class PBEDecimalEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Default implementation of the {@link DecimalEncryptor} interface.
 ** <p>
 ** This class lets the user specify the algorithm (and provider) to be used for
 ** encryption, the password to use, the number of hashing iterations and the
 ** salt generator that will be applied for obtaining the encryption key.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The size of the result of encrypting a number, depending on the algorithm,
 ** may be much bigger (in bytes) than the size of the encrypted number itself.
 ** For example, encrypting a 4-byte integer can result in an encrypted 16-byte
 ** number. This can lead the user into problems if the encrypted values are to
 ** be stored and not enough room has been provided.
 ** <p>
 ** This class is <i>thread-safe</i>.
 ** <p>
 ** <b><u>Configuration</u></b>
 ** <p>
 ** The algorithm, provider, password, key-obtention iterations and salt
 ** generator can take  values in any of these ways:
 ** <ul>
 **   <li>Using its default values (except for password).
 **   <li>Calling the corresponding <code>setter</code> methods.
 ** </ul>
 ** And the actual values to be used for initialization will be established by
 ** applying the following priorities:
 ** <ol>
 **   <li>First, the default values are considered (except for password).
 **   <li>Finally, if the corresponding <code>setter</code> method has been
 **       called on the encryptor itself for any of the configuration
 **       parameters, the values set by these calls override all of the above.
 ** </ol>
 ** <p>
 ** <b><u>Initialization</u></b>
 ** <p>
 ** Before it is ready to encrypt/decrypt digests, an object of this class has
 ** to be <i>initialized</i>. Initialization happens:
 ** <ul>
 **   <li>When <code>initialize</code> is called.
 **   <li>When <code>encrypt(...)</code> or <code>decrypt(...)</code> are called
 **       for the first time, if <code>initialize()</code> has not been called
 **       before.
 ** </ul>
 ** Once an encryptor has been initialized, trying to change its configuration
 ** will result in a NOP.
 ** <p>
 ** <b><u>Usage</u></b>
 ** <p>
 ** An encryptor may be used for:
 ** <ul>
 **   <li><i>Encrypting messages</i>, by calling the <code>encrypt(...)</code> method.
 **   <li><i>Decrypting messages</i>, by calling the <code>decrypt(...)</code> method.
 ** </ul>
 ** <b>If a random salt generator is used, two encryption results for the same
 ** message will always be different (except in the case of random salt
 ** coincidence)</b>. This may enforce security by difficulting brute force
 ** attacks on sets of data at a time and forcing attackers to perform a brute
 ** force attack on each separate piece of encrypted data.
 ** <p>
 ** To learn more about the mechanisms involved in encryption, read
 ** <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"  target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class PBEDecimalEncryptor extends    PBENumericEncryptor
                                 implements DecimalEncryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBEDecimalEncryptor</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PBEDecimalEncryptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBEDecimalEncryptor</code> with the specified password
   ** to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public PBEDecimalEncryptor(final String password) {
    // ensure inheritance
    super(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBEDecimalEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** This allows the password to be specified as a <i>cleanable</i> char[]
   ** instead of a String, in extreme security conscious environments in which
   ** no copy of the password as an immutable String should be kept in memory.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public PBEDecimalEncryptor(final char[] password) {
    // ensure inheritance
    super(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBEDecimalEncryptor</code> using the
   ** specified byte encryptor (constructor used for cloning).
   **
   ** @param  cryptor            the byte encryptor to use.
   */
  public PBEDecimalEncryptor(final PBEBinaryEncryptor cryptor) {
    // ensure inheritance
    super(cryptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (DecimalEncryptor)
  /**
   ** Encrypts a message using the specified configuration.
   ** <p>
   ** The mechanisms applied to perform the encryption operation are described
   ** in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** This encryptor uses a salt for each encryption operation. The size of the
   ** salt depends on the algorithm being used. This salt is used for creating
   ** the encryption key and, if generated by a random generator, it is also
   ** appended unencrypted at the beginning of the results so that a decryption
   ** operation can be performed.
   ** <p>
   ** <b>If a random salt generator is used, two encryption results for the same
   ** message will always be different (except in the case of random salt
   ** coincidence)</b>.
   ** <p>
   ** This may enforce security by difficulting brute force attacks on sets of
   ** data at a time and forcing attackers to perform a brute force attack on
   ** each separate piece of encrypted data.
   **
   ** @param  message            the {@link BigDecimal} message to be
   **                            encrypted.
   **
   ** @return                    the result of encryption.
   **
   ** @throws EncryptionException if the encryption operation fails or
   **                             initialization could not be correctly done
   **                             (for example, no password has been set),
   **                             ommitting any further information about the
   **                             cause for security reasons.
   */
  @Override
  public BigDecimal encrypt(final BigDecimal message) {
    // prevent bogus inpout
    if (message == null)
      return message;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // get the scale of the decimal number
      final int scale = message.scale();
      // get the number in binary form (without scale)
      final BigInteger unscaled = message.unscaledValue();
      // the unscaled BigInteger is converted into bytes and the
      // DefaultPBEByteEncryptor does the rest
      final byte[] bytes = this.cryptor.encrypt(unscaled.toByteArray());
      // the length of the encrypted message will be stored with the result
      // itself so that we can correctly rebuild the complete byte array when
      // decrypting (BigInteger will ignore all "0x0" bytes in the leftmost
      // side, and also "-0x1"  in the leftmost side will be translated as
      // signum).
      final byte[] length = NumberUtility.intToBytes(bytes.length);
      // append the length bytes to the encrypted message
      final byte[] result = PBEBinaryEncryptor.append(bytes, length);
      // finally, return a new number built from the encrypted bytes
      return new BigDecimal(new BigInteger(result), scale);
    }
    catch (Exception e) {
      // if encryption fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (DecimalEncryptor)
  /**
   ** Decrypts a message using the specified configuration.
   ** <p>
   ** The mechanisms applied to perform the decryption operation are described
   ** in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** If a random salt generator is used, this decryption operation will expect
   ** to find an unencrypted salt at the beginning of the encrypted input, so
   ** that the decryption operation can be correctly performed (there is no
   ** other way of knowing it).
   **
   ** @param  message            the {@link BigDecimal} message to be
   **                            decrypted.
   **
   ** @return                    the result of decryption.
   **
   ** @throws EncryptionException if the decryption operation fails or
   **                             initialization could not be correctly done
   **                             (for example, no password has been set),
   **                             ommitting any further information about the
   **                             cause for security reasons.
   */
  @Override
  public BigDecimal decrypt(final BigDecimal message) {
    // prevent bogus inpout
    if (message == null)
      return message;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // get the number in binary form (without scale)
      final BigInteger unscaled  = message.unscaledValue();
      // process the encrypted byte array (check size, pad if needed...)
      final byte[]     encrypted = process(unscaled.toByteArray(), message.signum());
      // let the byte encyptor decrypt
      byte[]           result    = this.cryptor.decrypt(encrypted);
      // finally, return a new number built from the encrypted bytes
      return new BigDecimal(new BigInteger(result), message.scale());
    }
    catch (Exception e) {
      // if encryption fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }
}