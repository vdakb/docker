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

    File        :   PBEStringEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PBEStringEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import java.security.Provider;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.hst.foundation.security.Salt;
import oracle.hst.foundation.security.StringEncryptor;
import oracle.hst.foundation.security.SecurePasswordHandler;
import oracle.hst.foundation.security.EncryptionException;

////////////////////////////////////////////////////////////////////////////////
// class PBEStringEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Default implementation of the {@link StringEncryptor} interface.
 ** <p>
 ** This class lets the user specify the algorithm (and provider) to be used for
 ** encryption, the password to use, the number of hashing iterations and the
 ** salt generator that will be applied for obtaining the encryption key.
 ** <p>
 ** This class avoids byte-conversion problems related to the fact of different
 ** platforms having different default charsets, and returns encryption results
 ** in the form of BASE64-encoded ASCII Strings.
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
 ** To learn more about the mechanisms involved in encryption, read <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127"  target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class PBEStringEncryptor implements StringEncryptor
                                ,          SecurePasswordHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the PBEByteEncryptor that will be internally used.
  private final PBEBinaryEncryptor cryptor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBEStringEncryptor</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PBEStringEncryptor() {
    // ensure inheritance
    this(new PBEBinaryEncryptor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PBEStringEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public PBEStringEncryptor(final String password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PBEStringEncryptor</code> with the specified
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
  public PBEStringEncryptor(final char[] password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBEStringEncryptor</code> using the
   ** specified byte encryptor (constructor used for cloning).
   **
   ** @param  cryptor            the binary encryptor to use.
   */
  public PBEStringEncryptor(final PBEBinaryEncryptor cryptor) {
    // ensure inheritance
    super();

    // initialize instance
    this.cryptor = cryptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Encryptor)
  /**
   ** Sets the security provider to be asked for the digest algorithm. The
   ** provider does not have to be registered at the security infrastructure
   ** beforehand, and its being used here will not result in its being
   ** registered.
   ** <p>
   ** If this method is called, calling {@link #providerName(String)} becomes
   ** unnecessary.
   ** <p>
   ** If no provider name / provider is explicitly set, the default JVM provider
   ** will be used.
   **
   ** @param  provider           the {@link Provider} to be asked for the chosen
   **                            algorithm.
   */
  @Override
  public void provider(final Provider provider) {
    this.cryptor.provider(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Encryptor)
  /**
   ** Returns the security provider to be asked for the digest algorithm. The
   ** provider may be registered at the security infrastructure beforehand, and
   ** its being used here will not result in its being registered.
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
   */
  @Override
  public synchronized Provider provider() {
    return this.cryptor.provider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Encryptor)
  /**
   ** Sets the name of the security provider to be asked for the digest
   ** algorithm. This security provider has to be registered beforehand at the
   ** JVM security framework.
   ** <p>
   ** The provider can also be set with the {@link #provider(Provider)} method,
   ** in which case it will not be necessary neither registering the provider
   ** beforehand, nor calling this {@link #providerName(String)} method to
   ** specify a provider name.
   ** <p>
   ** Note that a call to {@link #provider(Provider)} overrides any value set by
   ** this method.
   ** <p>
   ** If no provider name / provider is explicitly set, the default JVM provider
   ** will be used.
   **
   ** @param  name               the name of the security provider to be asked
   **                            for the digest algorithm.
   */
  @Override
  public void providerName(final String name) {
    this.cryptor.providerName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Encryptor)
  /**
   ** Returns the name of the security provider to be asked for the digest
   ** algorithm. This security provider may not be registered beforehand at the
   ** JVM security framework.
   ** <p>
   ** The provider can also be returned with the {@link #provider()} method, in
   ** which case it will not be necessary neither registering the provider
   ** beforehand, nor calling this {@link #providerName(String)} method to
   ** specify a provider name.
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @return                    the name of the security provider to be asked
   **                            for the digest algorithm.
   */
  @Override
  public String providerName() {
    return this.cryptor.providerName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Encryptor)
  /**
   ** Sets the algorithm to be used for digesting, like <code>MD5</code> or
   ** <code>SHA-1</code>.
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
   ** For valid names in the Sun JVM, see <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   **
   ** @param  algorithm          the name of the algorithm to be used.
   */
  @Override
  public void algorithm(final String algorithm) {
    this.cryptor.algorithm(algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Encryptor)
  /**
   ** Returns the algorithm to be used for digesting, like <code>MD5</code> or
   ** <code>SHA-1</code>.
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
   ** For valid names in the Sun JVM, see <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   **
   ** @return                    the name of the algorithm to be used.
   */
  @Override
  public String algorithm() {
    return this.cryptor.algorithm();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Encryptor)
  /**
   ** Set the number of hashing iterations applied to obtain the encryption key.
   ** <p>
   * This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @param  iteration          the number of iterations.
   */
  @Override
  public void iteration(final int iteration) {
    this.cryptor.iteration(iteration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Encryptor)
  /**
   ** Returns the number of hashing iterations applied to obtain the encryption
   ** key.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @return                    the number of iterations applied to obtain the
   **                            encryption key.
   */
  @Override
  public int iteration() {
    return this.cryptor.iteration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of <code>SecureSalt</code> will be used.
   **
   ** @param  generator          the salt generator to be used.
   */
  public void salt(final Salt generator) {
    this.cryptor.salt(generator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Encryptor)
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
   */
  @Override
  public final byte[] salt() {
    return this.cryptor.salt();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialized (Encryptor)
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
   */
  @Override
  public boolean initialized() {
    return this.cryptor.initialized();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Encryptor)
  /**
   ** Initialize the encryptor.
   ** <p>
   ** This operation will consist in determining the actual configuration values
   ** to be used, and then initializing the encryptor with them.
   ** <p>
   ** Once a digester has been initialized, trying to change its configuration
   ** will result in NOP.
   **
   ** @throws EncryptionException if initialization could not be correctly done
   **                             (for example, if the digest algorithm chosen
   **                             cannot be used).
   */
  @Override
   public void initialize() {
      this.cryptor.initialize();
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (PasswordHandler)
  /**
   ** Sets the password to be used.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  @Override
  public synchronized void password(final String password) {
    this.cryptor.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (SecurePasswordHandler)
  /**
   ** Sets the password to be used, as a char[].
   ** <p>
   ** This allows the password to be specified as a <i>cleanable</i> char[]
   ** instead of a String, in extreme security conscious environments in which
   ** no copy of the password as an immutable String should be kept in memory.
   ** <p>
   ** <b>Important</b>: the array specified as a parameter WILL BE COPIED in
   ** order to be stored as encryptor configuration. The caller of this method
   ** will therefore be responsible for its cleaning.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  @Override
  public synchronized void password(final char[] password) {
    this.cryptor.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clean (SecurePasswordHandler)
  /**
   ** Zero-out the password char array.
   **
   ** @param  password           the char array to cleanup.
   */
  @Override
  public final void clean(final char[] password) {
    this.cryptor.clean(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (StringEncryptor)
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
   ** @param  message            the String message to be encrypted.
   **
   ** @return                    the result of encryption.
   **
   ** @throws EncryptionException if the decryption operation fails or
   **                             initialization could not be correctly done
   **                             (for example, no password has been set),
   **                             ommitting any further information about the
   **                             cause for security reasons.
   */
  @Override
  public String encrypt(final String message) {
    // prevent bogus inpout
    if (message == null)
      return message;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // the input String is converted into bytes using
      // {@link StringUtility#UNICODE} as a fixed charset to avoid problems with
      // different platforms having different default charsets. The
      // DefaultPBEByteEncryptor does than the job.
      byte[] bytes = this.cryptor.encrypt(StringUtility.toBytes(message));
      // we encode the result always BASE64 so that we obtain the safest result
      // String possible.
      bytes = Base64Transcoder.encode(bytes);
      return new String(bytes, StringUtility.ASCII);
    }
    catch (Exception e) {
      // if encryption fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (StringEncryptor)
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
   ** @param  message            the String message to be decrypted.
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
  public String decrypt(final String message) {
    // prevent bogus inpout
    if (message == null)
      return message;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // decode BASE64-encoded input to bytes
      byte[] encrypted = StringUtility.toAscii(message);
      encrypted = Base64Transcoder.decode(encrypted);

      // let the byte encyptor decrypt
      final byte[] decrypted = this.cryptor.decrypt(encrypted);
      // return the resulting decrypted String, using ENCODING as charset to
      // maintain between encryption and decyption processes.
      return new String(decrypted, StringUtility.UNICODE);
    }
    catch (Exception e) {
      // if decryption fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }
}