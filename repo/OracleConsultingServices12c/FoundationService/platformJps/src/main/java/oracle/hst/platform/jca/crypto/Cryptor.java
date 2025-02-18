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

    File        :   Cryptor.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Cryptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.jca.crypto;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.security.Provider;

import oracle.hst.platform.SecurityException;

////////////////////////////////////////////////////////////////////////////////
// interface Cryptor
// ~~~~~~~~~ ~~~~~~~
/**
 ** Common interface for all cryptors.
 ** <p>
 ** For a default implementation, see {@link CryptorBinary}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Cryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Binary
  // ~~~~~~~~~ ~~~~~~
  /**
   * Common interface for all encryptors which receive a byte array message and
   * return a byte array result.
   * <p>
   * For a default implementation, see {@link CryptorBinary}.
   * <p>
   * Implementations are intended to be thread-safe.
   */
  public interface Binary extends Cryptor {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encrypt
    /**
     ** Encrypt the input message.
     **
     ** @param  binary           the message to be encrypted.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the result of encryption.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     **
     ** @throws SecurityException if the encryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    byte[] encrypt(final byte[] binary)
      throws SecurityException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrypt
    /**
     ** Decrypt an encrypted message.
     **
     ** @param  binary           the message to be decypted.
     **                          <br>
     **                          Allowed object is array of <code>byte</code>.
     **
     ** @return                  the result of decryption.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     **
     ** @throws SecurityException if the decryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    byte[] decrypt(final byte[] binary)
      throws SecurityException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Integer
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** Common interface for all encryptors which receive a {@link BigInteger}
   ** (arbitrary precision) message and return a {@link BigInteger} result.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** The size of the result of encrypting a number, depending on the algorithm,
   ** may be much bigger (in bytes) than the size of the encrypted number
   ** itself.
   ** <br>
   ** For example, encrypting a 4-byte integer can result in an encrypted
   ** 16-byte number. This can lead the user into problems if the encrypted
   ** values are to be stored and not enough room has been provided.
   */
  public interface Integer {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encrypt
    /**
     ** Encrypt the input message.
     **
     ** @param  message          the message to be encrypted.
     **                          <br>
     **                          Allowed object is {@link BigInteger}.
     **
     ** @return                  the result of encryption.
     **                          <br>
     **                          Possible object is {@link BigInteger}.
     **
     ** @throws SecurityException if the encryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    BigInteger encrypt(final BigInteger message)
      throws SecurityException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrypt
    /**
     ** Decrypt an encrypted message.
     **
     ** @param  encrypted        the message to be decypted.
     **                          <br>
     **                          Allowed object is {@link BigInteger}.
     **
     ** @return                  the result of decryption.
     **                          <br>
     **                          Possible object is {@link BigInteger}.
     **
     ** @throws SecurityException if the decryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    BigInteger decrypt(final BigInteger encrypted)
      throws SecurityException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Decimal
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** Common interface for all encryptors which receive a {@link BigDecimal}
   ** (arbitrary precision) message and return a {@link BigDecimal} result.
   ** <p>
   ** <b>Important</b>:
   ** <br>
   ** The size of the result of encrypting a number, depending on the algorithm,
   ** may be much bigger (in bytes) than the size of the encrypted number
   ** itself.
   ** <br>
   ** For example, encrypting a 4-byte integer can result in an encrypted
   ** 16-byte number. This can lead the user into problems if the encrypted
   ** values are to be stored and not enough space has been provided.
   ** <p>
   ** Implementations are intended to be thread-safe.
   */
  public interface Decimal {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encrypt
    /**
     ** Encrypt the input message.
     **
     ** @param  message          the message to be encrypted.
     **                          <br>
     **                          Allowed object is {@link BigDecimal}.
     **
     ** @return                  the result of encryption.
     **                          <br>
     **                          Possible object is {@link BigDecimal}.
     **
     ** @throws SecurityException if the encryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    BigDecimal encrypt(final BigDecimal message)
      throws SecurityException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrypt
    /**
     ** Decrypt an encrypted message.
     **
     ** @param  encrypted        the message to be decypted.
     **                          <br>
     **                          Allowed object is {@link BigDecimal}.
     **
     ** @return                  the result of decryption.
     **                          <br>
     **                          Possible object is {@link BigDecimal}.
     **
     ** @throws SecurityException if the decryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    BigDecimal decrypt(final BigDecimal encrypted)
      throws SecurityException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Character
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Common interface for all encryptors which receive a String message and
   ** return a String result.
   ** <p>
   ** Implementations are intended to be thread-safe.
   */
  public interface Character extends Cryptor {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: encrypt
    /**
     ** Encrypt the clear text message.
     **
     ** @param  message          the message to be encrypted.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the result of encryption.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws SecurityException if the encryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    String encrypt(final String message)
      throws SecurityException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrypt
    /**
     ** Decrypt an encrypted string message.
     **
     ** @param  encrypted        the message to be decypted.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the result of decryption.
     **                          <br>
     **                          Possible object is {@link String}.
     **
     ** @throws SecurityException if the decryption operation fails or
     **                           initialization could not be correctly done
     **                           (for example, no message has been set),
     **                           ommitting any further information about the
     **                           cause for security reasons.
     */
    String decrypt(final String encrypted)
      throws SecurityException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Sets the security provider to be asked for the encryption algorithm. The
   ** provider does not have to be registered at the security infrastructure
   ** beforehand, and its being used here will not result in its being
   ** registered.
   ** <p>
   ** If this method is called, calling {@link #providerName(String)} becomes
   ** unnecessary.
   ** <p>
   ** If no provider name / provider is explicitly set, the default JVM provider
   ** will be used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  provider           the {@link Provider} to be asked for the chosen
   **                            algorithm.
   **                            <br>
   **                            Allowed object is {@link Provider}.
   */
  void provider(final Provider provider);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the security provider to be asked for the encryption algorithm.
   ** <br>
   ** The provider may be registered at the security infrastructure beforehand,
   ** and its being used here will not result in its being registered.
   ** <p>
   ** If no provider name / provider is returned, the default JVM provider will
   ** be used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @return                    the {@link Provider} to be asked for the chosen
   **                            algorithm.
   **                            <br>
   **                            Possible object is {@link Provider}.
   */
  Provider provider();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName
  /**
   ** Sets the name of the security provider to be asked for the encryption
   ** algorithm. This security provider has to be registered beforehand at the
   ** JVM security framework.
   ** <p>
   ** The provider can also be set with the {@link #provider(Provider)} method,
   ** in which case it will not be necessary neither registering the provider
   ** beforehand, nor calling this {@link #providerName(String)} method to
   ** specify a provider name.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** A call to {@link #provider(Provider)} overrides any value set by this
   ** method.
   ** <p>
   ** If no provider name / provider is explicitly set, the default JVM provider
   ** will be used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  name               the name of the security provider to be asked
   **                            for the digest algorithm.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void providerName(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName
  /**
   ** Returns the name of the security provider to be asked for the encryption
   ** algorithm. This security provider may not be registered beforehand at the
   ** JVM security framework.
   ** <p>
   ** The provider can also be returned with the {@link #provider()} method, in
   ** which case it will not be necessary neither registering the provider
   ** beforehand, nor calling this {@link #providerName(String)} method to
   ** specify a provider name.
   **
   ** @return                    the name of the security provider to be asked
   **                            for the digest algorithm.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String providerName();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
  /**
   ** Sets the algorithm to be used for encrypting/decrypting, like
   ** <code>DES</code> or <code>AES</code>.
   ** <p>
   ** This algorithm has to be supported by your security infrastructure, and it
   ** should be allowed as an algorithm for creating java.security.MessageDigest
   ** instances.
   ** <p>
   ** If you are specifying a security provider with {@link #provider(Provider)}
   ** or {@link #providerName(String)}, this algorithm should be supported by
   ** your specified provider.
   ** <p>
   ** If you are not specifying a provider, you will be able to use those
   ** algorithms provided by the default security provider of your JVM vendor.
   ** For valid names in the Sun JVM, see
   ** <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  algorithm          the name of the algorithm to be used.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  void algorithm(final String algorithm);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
  /**
   ** Returns the algorithm to be used for encrypting/decrypting, like
   ** <code>DES</code> or <code>AES</code>.
   ** <p>
   ** This algorithm has to be supported by your security infrastructure, and it
   ** should be allowed as an algorithm for creating java.security.MessageDigest
   ** instances.
   ** <p>
   ** If you are specifying a security provider with {@link #provider(Provider)}
   ** or {@link #providerName(String)}, this algorithm should be supported by
   ** your specified provider.
   ** <p>
   ** If you are not specifying a provider, you will be able to use those
   ** algorithms provided by the default security provider of your JVM vendor.
   ** For valid names in the Sun JVM, see
   ** <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   **
   ** @return                    the name of the algorithm to be used.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  String algorithm();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration
  /**
   ** Set the number of hashing iterations applied to obtain the encryption key.
   ** <p>
   ** This mechanism is explained in
   ** <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  iteration          the number of iterations.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  void iteration(final int iteration);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration
  /**
   ** Returns the number of hashing iterations applied to obtain the encryption
   ** key.
   ** <p>
   ** This mechanism is explained in
   ** <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @return                    the number of iterations applied to obtain the
   **                            encryption key.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int iteration();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of {@link Salt.Secure} will be used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  generator          the salt generator to be used.
   **                            <br>
   **                            Allowed object is {@link Salt}.
   */
  void salt(final Salt generator);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt
  /**
   ** Returns the generated salt. If no salt generator is specified, it will
   ** return <code>null</code>.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** This method should be called after the initialization phase of the
   ** <code>Encryptor</code> was called to ensure that the size of the salt is
   ** fixed.
   **
   ** @return                    the generated salt.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  byte[] salt();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialized
  /**
   ** Returns <code>true</code> if the digester has already been initialized,
   ** <code>false</code> if not.
   ** <p>
   ** Initialization happens:
   ** <ul>
   **   <li>When <code>initialize</code> is called.
   **   <li>When <code>digest</code> or <code>matches</code> are called for the
   **       first time, if <code>initialize</code> has not been called before.
   ** </ul>
   ** Once a digester has been initialized, trying to change its configuration
   ** (algorithm, provider, salt size, iterations or salt generator) will result
   ** nop.
   **
   ** @return                    <code>true</code> if the digester has already
   **                            been initialized, <code>false</code> if not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean initialized();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the encryptor.
   ** <p>
   ** This operation will consist in determining the actual configuration values
   ** to be used, and then initializing the encryptor with them.
   ** <p>
   ** Once a digester has been initialized, trying to change its configuration
   ** will result in NOP.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @throws SecurityException if initialization could not be correctly done
   **                           (for example, if the digest algorithm chosen
   **                           cannot be used).
   */
  void initialize()
    throws SecurityException;
}