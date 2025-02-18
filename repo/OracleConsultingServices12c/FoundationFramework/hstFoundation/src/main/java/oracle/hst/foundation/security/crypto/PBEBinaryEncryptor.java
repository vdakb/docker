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

    File        :   PBEBinaryEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PBEBinaryEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.crypto;

import java.security.Provider;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.foundation.security.Salt;
import oracle.hst.foundation.security.BinaryEncryptor;
import oracle.hst.foundation.security.EncryptionException;
import oracle.hst.foundation.security.SecurePasswordHandler;

import oracle.hst.foundation.security.salt.SecureSalt;

////////////////////////////////////////////////////////////////////////////////
// class PBEBinaryEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Default implementation of the {@link BinaryEncryptor} interface.
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
public class PBEBinaryEncryptor implements BinaryEncryptor
                                ,          SecurePasswordHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default encryption algorithm will be PBEWithMD5AndDES */
  public static final String ALGORITHM     = "PBEWithMD5AndDES";

  /**
   ** the minimum recommended iterations for applied for obtaining the
   ** encryption key from the specified password, are 1000
   */
  public static final int    ITERATION     = 1000;

  /**
   ** the minimum recommended salt size, only used if the chosen encryption
   ** algorithm is not a block algorithm and thus block size cannot be used as
   ** salt size is 8.
   */
  public static final int    SALT_SIZE     = 8;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // algorithm to be used for hashing
  private String             algorithm     = ALGORITHM;

  // number of hash iterations to be applied
  private int                iteration     = ITERATION;

  // size of salt to be applied
  private int                saltSize      = SALT_SIZE;

  // Salt generator to be used.
  // Initialization of a salt generator is costly, and so default value will be
  // applied only in initialize(), if it finally becomes necessary.
  private Salt               salt          = null;

  // generated salt.
  private byte[]             saltBytes     = null;

  // Name of the java.security.Provider which will be asked for the selected
  // algorithm
  private String             providerName  = null;

  // java.security.Provider instance which will be asked for the selected
  // algorithm
  private Provider           provider      = null;

  // password to be applied.
  // This will NOT have a default value.
  // If none is set during configuration, an exception will be thrown.
  private char[]             password      = null;

  // encryption key generated.
  private SecretKey          key           = null;

  // Flag which indicates whether the cryptor has been initialized or not.
  //
  // Once initialized, all further modifications to its configuration will be
  // ignored.
  private boolean            initialized   = false;

  // ciphers to be used for encryption and decryption.
  private Cipher             encryptCipher = null;
  private Cipher             decryptCipher = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBEBinaryEncryptor</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   */
  public PBEBinaryEncryptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PBEBinaryEncryptor</code> with the specified
   ** password to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** EncryptionException being thrown during initialization.
   **
   ** @param  password           the password to be used.
   */
  public PBEBinaryEncryptor(final String password) {
    // ensure inheritance
    super();

    // initialize instance
    password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PBEBinaryEncryptor</code> with the specified
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
  public PBEBinaryEncryptor(final char[] password) {
    // ensure inheritance
    super();

    // initialize instance
    password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Encryptor)
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
   **
   ** @param  provider           the {@link Provider} to be asked for the chosen
   **                            algorithm.
   */
  @Override
  public synchronized void provider(final Provider provider) {
    if (initialized())
      this.provider = provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Encryptor)
  /**
   ** Returns the security provider to be asked for the encryption algorithm.
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
   */
  @Override
  public synchronized Provider provider() {
    return this.provider;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Encryptor)
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
   ** Note that a call to {@link #provider(Provider)} overrides any value set by
   ** this method.
   ** <p>
   ** If no provider name / provider is explicitly set, the default JVM provider
   ** will be used.
   **
   ** @param  name               the name of the security provider to be asked
   **                            for the encryption algorithm.
   */
  @Override
  public synchronized void providerName(final String name) {
    if (initialized())
      this.providerName = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Encryptor)
  /**
   ** Returns the name of the security provider to be asked for the encryption
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
   **                            for the encryption algorithm.
   */
  @Override
  public String providerName() {
    return this.providerName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Encryptor)
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
   ** For valid names in the Sun JVM, see <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   **
   ** @param  algorithm          the name of the algorithm to be used.
   */
  @Override
  public synchronized void algorithm(final String algorithm) {
    if (!initialized())
      this.algorithm = algorithm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Encryptor)
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
   ** For valid names in the Sun JVM, see <a target="_blank" href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java Cryptography Architecture API Specification &amp; Reference</a>.
   **
   ** @return                    the name of the algorithm to be used.
   */
  @Override
  public String algorithm() {
    return this.algorithm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Encryptor)
  /**
   ** Set the number of hashing iterations applied to obtain the encryption key.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @param  iteration          the number of iterations.
   */
  @Override
  public synchronized void iteration(final int iteration) {
    if (!initialized())
      this.iteration = iteration;
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
    return this.iteration;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Encryptor)
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of {@link SecureSalt} will be used.
   **
   ** @param  generator          the salt generator to be used.
   */
  @Override
  public synchronized void salt(final Salt generator) {
    if (!initialized())
      this.salt = generator;
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
    return this.saltBytes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialized (Encryptor)
  /**
   ** Returns <code>true</code> if the cryptor has already been initialized,
   ** <code>false</code> if not.
   ** <p>
   ** Initialization happens:
   ** <ul>
   **   <li>When <code>initialize</code> is called.
   **   <li>When <code>encrypt</code> or <code>decrypt</code> are called for the
   **       first time, if <code>initialize</code> has not been called before.
   ** </ul>
   ** Once a cryptor has been initialized, trying to change its configuration
   ** (algorithm, provider, salt size, iterations or salt generator) will result
   ** nop.
   **
   ** @return                    <code>true</code> if the cryptor has already
   **                            been initialized, <code>false</code> if not.
   */
  @Override
  public boolean initialized() {
    return this.initialized;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Encryptor)
  /**
   ** Initialize the encryptor.
   ** <p>
   ** This operation will consist in determining the actual configuration values
   ** to be used, and then initializing the encryptor with them.
   ** <p>
   ** Once a cryptor has been initialized, trying to change its configuration
   ** will result in NOP.
   **
   ** @throws EncryptionException if initialization could not be correctly done
   **                             (for example, if the encryption algorithm
   **                             chosen cannot be used).
   */
  @Override
   public synchronized void initialize() {
     // password cannot be null.
     if (this.password == null)
       throw new EncryptionException("Password not set for Password Handler " + ClassUtility.shortName(this));

    // double-check to avoid synchronization issues
    if (!this.initialized) {
      try {
        // if the encryptor was not set a salt generator in any way, it is time to
        // apply its default value.
        if (this.salt == null)
          this.salt = new SecureSalt();

        // normalize password to NFC form
        final char[] normalizedPassword = StringUtility.normalizeNfc(this.password);

        // encryption and decryption Ciphers are created the usual way.
        PBEKeySpec pbeKeySpec = new PBEKeySpec(normalizedPassword);

        // we don't need the char[] passwords anymore -> clean!
        clean(this.password);
        clean(normalizedPassword);

        if (this.provider != null) {
          this.key           = SecretKeyFactory.getInstance(this.algorithm, this.provider).generateSecret(pbeKeySpec);
          this.encryptCipher = Cipher.getInstance(this.algorithm, this.provider);
          this.decryptCipher = Cipher.getInstance(this.algorithm, this.provider);
        }
        else if (this.providerName != null) {
          this.key           = SecretKeyFactory.getInstance(this.algorithm, this.providerName).generateSecret(pbeKeySpec);
          this.encryptCipher = Cipher.getInstance(this.algorithm, this.providerName);
          this.decryptCipher = Cipher.getInstance(this.algorithm, this.providerName);
        }
        else {
          this.key           = SecretKeyFactory.getInstance(this.algorithm).generateSecret(pbeKeySpec);
          this.encryptCipher = Cipher.getInstance(this.algorithm);
          this.decryptCipher = Cipher.getInstance(this.algorithm);
        }
      }
      catch (Throwable t) {
        throw new EncryptionException(t);
      }
      // the salt size for the chosen algorithm is set to be equal to the
      // algorithm's block size (if it is a block algorithm).
      int blockSize = this.encryptCipher.getBlockSize();
      if (blockSize > 0)
        this.saltSize = blockSize;

      this.initialized = true;
    }
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
    // prevent bogus input
    if (password == null || password.length() == 0)
      throw new EncryptionException("Password cannot be set to null or empty for Password Handler " + ClassUtility.shortName(this));

    this.password(password.toCharArray());
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
    // prevent bogus input
    if (password == null || password.length == 0)
      throw new EncryptionException("Password cannot be set to null or empty for Password Handler " + ClassUtility.shortName(this));

    if (!initialized()) {
      if (this.password != null) {
        // we clean the old password, if there is one.
        clean(this.password);
      }
      this.password = new char[password.length];
      System.arraycopy(password, 0, this.password, 0, password.length);
    }
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
    if (password != null) {
      synchronized (password) {
        final int pwdLength = password.length;
        for (int i = 0; i < pwdLength; i++) {
          password[i] = '\0';
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (BinaryEncryptor)
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
   ** @param  binary             the byte array message to be encrypted.
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
  public byte[] encrypt(final byte[] binary) {
    // prevent bogus inpout
    if (binary == null)
      return binary;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // create salt
      final byte[] salt = this.salt.generate(this.saltSize);

      // perform encryption using the Cipher
      final PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, this.iteration);

      byte[] load = null;
      synchronized (this.encryptCipher) {
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, this.key, parameterSpec);
        load = this.encryptCipher.doFinal(binary);
      }

      // finally we build an array containing both the unencrypted salt
      // and the result of the encryption. This is done only
      // if the salt generator we are using specifies to do so by inserting
      // unhashed salt before the encryption result
      return (this.salt.includePlain()) ? append(salt, load) : load;
    }
    catch (InvalidKeyException e) {
      // the problem could be not having the unlimited strength policies
      // installed, so better give a usefull error message.
      handleInvalidKey(e);
      throw new EncryptionException();
    }
    catch (Exception e) {
      // if encryption fails, it is more secure not to return any
      // information about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (BinaryEncryptor)
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
   ** @param  binary             the byte array message to be decrypted.
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
  public byte[] decrypt(final byte[] binary) {
    // prevent bogus inpout
    if (binary == null)
      return binary;

    // check initialization
    if (!initialized())
      initialize();

    if (this.salt.includePlain()) {
      // check that the received message is bigger than the salt
      if (binary.length <= this.saltSize)
        throw new EncryptionException();
    }

    try {
      // if we are using a salt generator which specifies the salt to be
      // included into the encrypted message itself, get it from there. If not,
      // the salt is supposed to be fixed and thus the salt generator can be
      // safely asked for it again.
      byte[] salt = null;
      byte[] load = null;
      if (this.salt.includePlain()) {
        final int saltStart   = 0;
        final int saltSize    = (this.saltSize < binary.length ? this.saltSize : binary.length);
        final int kernelStart = (this.saltSize < binary.length ? this.saltSize : binary.length);
        final int kernelSize  = (this.saltSize < binary.length ? (binary.length - this.saltSize) : 0);

        salt = new byte[saltSize];
        load = new byte[kernelSize];

        System.arraycopy(binary, saltStart, salt, 0, saltSize);
        System.arraycopy(binary, kernelStart, load, 0, kernelSize);
      }
      else {
        salt = this.salt.generate(this.saltSize);
        load = binary;
      }

      // perform decryption using the Cipher
      final PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, this.iteration);

      byte[] decrypted = null;

      synchronized (this.decryptCipher) {
        this.decryptCipher.init(Cipher.DECRYPT_MODE, this.key, parameterSpec);
        decrypted = this.decryptCipher.doFinal(load);
      }
      // return the results
      return decrypted;
    }
    catch (InvalidKeyException e) {
      // the problem could be not having the unlimited strength policies
      // installed, so better give a usefull error message.
      handleInvalidKey(e);
      throw new EncryptionException();
    }
    catch (Exception e) {
      // if decryption fails, it is more secure not to return any
      // information about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends to arrays
   **
   ** @param  first              the array starting the returned result
   ** @param  second             the array appended to <code>first</code> in
   **                            the returned result.
   **
   ** @return                    the resulting array starting with the content
   **                            of <code>first</code> and appended with
   **                            <code>second</code>.
   */
  protected static byte[] append(final byte[] first, final byte[] second) {
    final byte[] result = new byte[first.length + second.length];
    System.arraycopy(first,  0, result, 0,            first.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleInvalidKey
  /**
   ** Method used to provide an useful error message in the case that the user
   ** tried to use a strong PBE algorithm like TripleDES and he/she has not
   ** installed the Unlimited Strength Policy files (the default  message for
   ** this is simply "invalid key size", which does not provide enough clues for
   ** the user to know what is really going on).
   */
  private void handleInvalidKey(final InvalidKeyException e) {
    if ((e.getMessage() != null) && ((e.getMessage().toUpperCase().indexOf("KEY SIZE") != -1)))
      throw new EncryptionException("Encryption raised an exception. A possible cause is you are using strong encryption algorithms and you have not installed the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files in this Java Virtual Machine");
  }
}