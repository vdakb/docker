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

    File        :   Advanced.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Advanced.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.security;

import java.nio.charset.StandardCharsets;

import java.security.Key;

import java.util.Base64;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

////////////////////////////////////////////////////////////////////////////////
// class Advanced
// ~~~~~ ~~~~~~~~
/**
 ** Utility class providing symmetric AES encryption/decryption.
 ** <p>
 ** To strengthen the encrypted result, use the {@link #seed} method to provide
 ** a custom key prior to invoking the {@link #encrypt} or {@link #decrypt}
 ** methods.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Advanced implements Encryptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** PKCS5 padding is not valid for AES, but Java still provides it which means
   ** that Java is lying and is actually using PKCS7 padding in which case
   ** PKCS5Padding and PKCS7Padding are the same for all intends and purposes.
   ** <p>
   ** Openfire uses BouncyCastleProvider as its security provider.
   ** Integrating this security provider into the connector bundle goes beyond
   ** the target size of a connector bundle.
   ** <p>
   ** As stated above, the Java implementation offers an adequate replacement,
   ** even if it is obviously not implemented properly, due to the wrong name
   ** for the encryption algorithm used.
   ** <p>
   ** That's why it's here <code>AES/CBC/PKCS5Padding</code> instead of
   ** <code>AES/CBC/PKCS7Padding</code> declared.
   */
  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

  private static final byte[] VECTOR    = {
    (byte)0xcd, (byte)0x91, (byte)0xa7, (byte)0xc5
  , (byte)0x27, (byte)0x8b, (byte)0x39, (byte)0xe0
  , (byte)0xfa, (byte)0x72, (byte)0xd0, (byte)0x29
  , (byte)0x83, (byte)0x65, (byte)0x9d, (byte)0x74
  };

  private static final byte[] DEFAULT   = {
    (byte)0xf2, (byte)0x46, (byte)0x5d, (byte)0x2a
  , (byte)0xd1, (byte)0x73, (byte)0x0b, (byte)0x18
  , (byte)0xcb, (byte)0x86, (byte)0x95, (byte)0xa3
  , (byte)0xb1, (byte)0xe5, (byte)0x89, (byte)0x27
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private byte[] secret = DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Advanced</code> encryption cipher using the
   ** {@link #DEFAULT} secret.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Advanced() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Advanced</code> encryption cipher using the specified
   ** <code>secret</code> (oversized values will be cut).
   **
   ** @param  secret             the seeded encryption secret.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Advanced(final String secret) {
    // ensure inheritance
    this();

    // initialize instance
    seed(secret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secret
  /**
   ** Set the encryption key.
   ** <p>
   ** This will apply the user-defined key, truncated or filled (via the default
   ** key) as needed to meet the key length specifications.
   **
   ** @param  secret             the encryption key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  private void secret(final byte[] secret) {
    this.secret = adjust(secret);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   seed (Encryptor)
  /**
   ** Set the encryption secret.
   ** <br>
   ** This will apply the user-defined key, truncated or filled (via the default
   ** key) as needed  to meet the key length specifications.
   **
   ** @param  value              the seeded encryption secret.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void seed(final String value) {
    if (value == null) {
      this.secret = null;
      return;
    }
    secret(value.getBytes(StandardCharsets.UTF_8));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (Encryptor)
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
  @Override
  public final String encrypt(final String message) {
    return encrypt(message, VECTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (Encryptor)
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
  @Override
  public final String encrypt(final String message, final byte[] vector) {
    // prevent bogus input
    if (message == null)
      return null;

    byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
    return Base64.getEncoder().encodeToString(cipher(bytes, this.secret, vector == null ? VECTOR : vector, Cipher.ENCRYPT_MODE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (Encryptor)
  /**
   ** Decrypt an encrypted string message.
   **
   ** @param  message            the message to be decypted.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of decryption.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String decrypt(final String message) {
    return decrypt(message, VECTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (Encryptor)
  /**
   ** Decrypt an encrypted string message.
   **
   ** @param  message            the message to be decypted.
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
  @Override
  public final String decrypt(final String message, final byte[] vector) {
    // prevent bogus input
    if (message == null)
      return null;

    byte[] bytes = cipher(Base64.getDecoder().decode(message), this.secret, vector == null ? VECTOR : vector, Cipher.DECRYPT_MODE);
    return (bytes == null) ? null : new String(bytes, StandardCharsets.UTF_8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adjust
  /**
   ** Validates an optional user-defined encryption key.
   ** <p>
   ** Only the first sixteen bytes of the input array will be used for the key.
   ** <br>
   ** It will be filled (if necessary) to a minimum length of sixteen.
   **
   ** @param  secret             the user-defined encryption key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    a valid encryption key, or <code>null</code>
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  private byte[] adjust(final byte[] key) {
    if (key == null)
      return key;

    final byte[] result = new byte[DEFAULT.length];
    for (int x = 0; x < DEFAULT.length; x++) {
      result[x] = x < key.length ? key[x] : DEFAULT[x];
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cipher
  /**
   ** Symmetric encrypt/decrypt routine.
   **
   ** Encrypts or decrypts data in a single-part operation.
   ** <p>
   ** The bytes in the <code>message</code> buffer, are processed, with padding
   ** (if requested) being applied. If an AEAD mode such as GCM/CCM is being
   ** used, the authentication tag is appended in the case of encryption, or
   ** verified in the case of decryption. The result is return in a new buffer.
   **
   ** @param  message            the input buffer to be encrypt/decrypt.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  key                the encryption key.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  vector             the initialization vector (IV).
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  mode               the operation mode of the cipher (this is one of
   **                            the following:
   **                            <ul>
   **                              <li>ENCRYPT_MODE
   **                              <li>DECRYPT_MODE
   **                              <li>WRAP_MODE
   **                              <li>UNWRAP_MODE
   **                             </ul>
   **
   ** @return                     the encrypted/decrypted message, or
   **                             <code>null</code> if oparation fails.
   */
  private byte[] cipher(final byte[] message, final byte[] key, final byte[] iv, final int mode) {
    byte[] result = null;
    try {
      // create AES encryption key
      final Key    spec   = new SecretKeySpec(key, "AES");
      // create AES Cipher
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // initialize AES Cipher and convert
      cipher.init(mode, spec, new IvParameterSpec(iv));
      result = cipher.doFinal(message);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return result;
  }
}