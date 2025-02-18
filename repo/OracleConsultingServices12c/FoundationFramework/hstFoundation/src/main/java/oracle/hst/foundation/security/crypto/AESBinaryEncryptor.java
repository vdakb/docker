package oracle.hst.foundation.security.crypto;

import java.security.Key;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

import oracle.hst.foundation.security.BinaryEncryptor;
import oracle.hst.foundation.security.EncryptionException;
import oracle.hst.foundation.security.Salt;

////////////////////////////////////////////////////////////////////////////////
// class AESBinaryEncryptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Utility class for easily performing high-strength encryption of binaries
 ** (byte arrays) objects.
 ** <p>
 ** This class internally holds a {@link BinaryEncryptor} configured this way:
 ** <ul>
 **  <li>Algorithm: <code>AES/CBC/PKCS5Padding</code>.</li>
 **  <li>Key obtention iterations: <code>0</code>.</li>
 ** </ul>
 ** <p>
 ** To use this class, you may need to download and install the
 ** <a href="http://java.sun.com/javase/downloads" target="_blank"><i>Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy  Files</i></a>.
 ** <p>
 ** This class is <i>thread-safe</i>.
 ** <p>
 ** AES is the successor of DES as standard symmetric encryption algorithm for
 ** US federal organizations (and as standard for pretty much everybody else,
 ** too).
 ** <br>
 ** AES accepts keys of 128, 192 or 256 bits (128 bits is already very
 ** unbreakable), uses 128-bit blocks (so no issue there), and is efficient in
 ** both software and hardware.
 ** <p>
 ** It was selected through an open competition involving hundreds of
 ** cryptographers during several years. Basically, you cannot have better than
 ** that.
 ** <b>Note</b>
 ** A block cipher is a box which encrypts "blocks" (128-bit chunks of data with
 ** AES). When encrypting a "message" which may be longer than 128 bits, the
 ** message must be split into blocks, and the actual way you do the split is
 ** called the <a href="http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation">mode of operation</a>
 ** or "chaining". The naive mode (simple split) is called ECB and has issues.
 ** Using a block cipher properly is not easy, and it is more important than
 ** selecting between, e.g., AES or 3DES.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class AESBinaryEncryptor implements BinaryEncryptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default encryption algorithm will be PBEWithMD5AndDES */
  public static final String  ALGORITHM    = "AES";

  /**
   ** the minimum recommended iterations for applied for obtaining the
   ** encryption key from the specified password, are 1000
   */
  public static final int     ITERATION     = 0;

  /**
   ** the minimum recommended salt size, only used if the chosen encryption
   ** algorithm is not a block algorithm and thus block size cannot be used as
   ** salt size is 8.
   */
  public static final int     SALT_SIZE     = 0;

  private final static byte[] defaultKeyBytes = {
    (byte)0x23,(byte)0x65,(byte)0x87,(byte)0x22
  , (byte)0x59,(byte)0x78,(byte)0x54,(byte)0x43
  , (byte)0x64,(byte)0x05,(byte)0x6A,(byte)0xBD
  , (byte)0x34,(byte)0xA2,(byte)0x34,(byte)0x57
  };
  private final static byte[] defaultIvBytes  = {
    (byte)0x51,(byte)0x65,(byte)0x22,(byte)0x23
  , (byte)0x64,(byte)0x05,(byte)0x6A,(byte)0xBE
  , (byte)0x51,(byte)0x65,(byte)0x22,(byte)0x23
  , (byte)0x64,(byte)0x05,(byte)0x6A,(byte)0xBE
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Key             key;
  private final IvParameterSpec iv;

  // ciphers to be used for encryption and decryption.
  private Cipher                encryptCipher = null;
  private Cipher                decryptCipher = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AESBinaryEncryptor</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AESBinaryEncryptor() {
    // ensure inheritance
    this(false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AESBinaryEncryptor</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @param  defaultKey         <code>true</code> if the default key bytes
   **                            should be used; otherwise a new key is
   **                            generated.
   */
  public AESBinaryEncryptor(final boolean defaultKey) {
    // ensure inheritance
    super();

    // initialize instance
    this.iv = new IvParameterSpec(defaultIvBytes);
     try {
       this.key           = defaultKey ? new SecretKeySpec(defaultKeyBytes, ALGORITHM) : KeyGenerator.getInstance(ALGORITHM).generateKey();
       this.encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       this.decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
     }
     catch (RuntimeException e) {
       throw e;
     }
     catch (Exception e) {
       throw new RuntimeException(e);
    }
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
    // intentionally left blank
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
    return null;
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
    return null;
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
    // intentionally left blank
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
    // intentionally left blank
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
    return ALGORITHM;
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
    // intentionally left blank
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
    return ITERATION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Encryptor)
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of {@link Salt} will be used.
   **
   ** @param  generator          the salt generator to be used.
   */
  @Override
  public synchronized void salt(final Salt generator) {
    // intentionally left blank
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
    return null;
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
    return true;
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

    try {
      synchronized (this.encryptCipher) {
        this.encryptCipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        return this.encryptCipher.doFinal(binary);
      }
    }
    catch (Exception e) {
      // if encryption fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
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

    try {
      synchronized (this.decryptCipher) {
        this.decryptCipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        return this.decryptCipher.doFinal(binary);
      }
    }
    catch (Exception e) {
      // if decryption fails, it is more secure not to return any
      // information about the cause in nested exceptions. Simply fail.
      throw new EncryptionException();
    }
  }
}