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

    File        :   Cryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Cryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Cryptor
// ~~~~~ ~~~~~~~
/**
 ** If an automated task needs to log in then we must provide a
 ** username/password. Since it's an automated task we can retrieve the
 ** username/password from something like a properties file.
 ** <p>
 ** A security best practice is to never store a password in plain text, we must
 ** encrypt it using an appropriate algorithm. The javax.crypto package provides
 ** those algorithms and AES is one of them.
 ** <p>
 ** To retrieve the original value from an encrypted string, you need to use a
 ** secret key habitually stored in a different location than the user/password
 ** file.
 */
public class Cryptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final String          KEYGEN_ALGORITHM     = "DESede/ECB/PKCS5Padding";
  protected static final int             KEYGEN_KEYSIZE       = 168;

  protected static final int             DEFAULT_IV           = 0;
  protected static final String          DEFAULT_UUID         = "56122A52-CE96-4C5C-835D-637FCD998CE6";
  protected static final String          DEFAULT_SEED         = "F4A11AEAAE9EF70723835B54014AC27CE5A4D6FE3B924062";

  private static final SecureRandom      secureRandom      = new SecureRandom();
  private static final Random            pseudoRandom      = new Random(secureRandom.nextLong());
  private static final Map<Key, Crypter> crypterCache      = new ConcurrentHashMap<Key, Crypter>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                    transformation;
  private final int                       keysize;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Key
  // ~~~~~ ~~~
  /**
   ** A class that represents an immutable universally unique identifier (UUID).
   ** A UUID represents a 128-bit value.
   */
  private static class Key {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final UUID uuid;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Key</code> as a type 4 (pseudo randomly generated)
     ** UUID.
     **
     ** The <code>Key</code> is generated using a cryptographically strong
     ** pseudo random number generator.
     */
    public Key() {
      this.uuid = UUID.randomUUID();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Key</code> using the specified byte array.
     ** <p>
     ** The <code>mostSigBits</code> of the byte array are used for the most
     ** significant 64 bits of the <code>KeyUUID</code> and the
     ** <code>leastSigBits</code> of the byte array becomes the least
     ** significant 64 bits of the <code>KeyUUID</code>.
     **
     ** @param  uuid
     */
    public Key(final byte[] uuid) {
      long hiBytes = 0L;
      long loBytes = 0L;
      for (int i = 0; i < 8; i++) {
        hiBytes = hiBytes << 8 | uuid[i] & 0xFF;
        loBytes = loBytes << 8 | uuid[(8 + i)] & 0xFF;
      }
      this.uuid = new UUID(hiBytes, loBytes);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Key</code> from the string standard
     ** representation as described in the {@link #toString} method.
     ** <p>
     ** The string representation has to be as described by this BNF:
     ** <blockquote>
     **   <pre>
     **     UUID                   = <time_low> "-" <time_mid> "-"
     **                              <time_high_and_version> "-"
     **                              <variant_and_sequence> "-"
     **                              <node>
     **     time_low               = 4*<hexOctet>
     **     time_mid               = 2*<hexOctet>
     **     time_high_and_version  = 2*<hexOctet>
     **     variant_and_sequence   = 2*<hexOctet>
     **     node                   = 6*<hexOctet>
     **     hexOctet               = <hexDigit><hexDigit>
     **     hexDigit               =
     **     "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     **     | "a" | "b" | "c" | "d" | "e" | "f"
     **     | "A" | "B" | "C" | "D" | "E" | "F"
     **   </pre>
     ** </blockquote>
     **
     ** @param  uuid             a string that specifies a <code>UUID</code>.
     **
     ** @throws IllegalArgumentException if name does not conform to the string
     **                                  representation as described above.
     */
    public Key(final String uuid)
      throws IllegalArgumentException {

      try {
        this.uuid = UUID.fromString(uuid);
      }
      catch (IllegalArgumentException e) {
        String message = String.format("CryptoManager failed to decode the key entry identifier \"%s\":  %s", new Object[] { uuid, e.getMessage() });
        throw new IllegalArgumentException(message, e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: length
    /**
     ** Return the byte length of this immutable universally unique identifier
     ** (UUID).
     ** <p>
     ** Return always <code>16</code> as the length.
     **
     ** @return                  the byte length of this immutable universally
     **                          unique identifier (UUID).
     */
    public static int length() {
      return 16;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: binary
    /**
     ** Returns the binary representation of this {@link UUID}.
     **
     ** @return                  the binary representation of this {@link UUID}.
     */
    public final byte[] binary() {
      byte[] uuid = new byte[16];
      long hiBytes = this.uuid.getMostSignificantBits();
      long loBytes = this.uuid.getLeastSignificantBits();
      for (int i = 7; i >= 0; i--) {
        uuid[i] = (byte)(int)hiBytes;
        hiBytes >>>= 8;
        uuid[(8 + i)] = (byte)(int)loBytes;
        loBytes >>>= 8;
      }
      return uuid;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns the hash code value for this <code>Cryptor</code>. To get the
     ** hash code of this <code>Cryptor</code>.
     ** <p>
     ** This ensures that <code>s1.equals(s2)</code> implies that
     ** <code>s1.hashCode()==s2.hashCode()</code> for any two
     ** <code>Cryptor</code> <code>s1</code> and <code>s2</code>, as required by
     ** the general contract of <code>Object.hashCode()</code>.
     **
     ** @return                  the hash code value for this
     **                          <code>Cryptor</code>.
     */
    @Override
    public int hashCode() {
      return this.uuid.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **
     ** @see    Object#equals(Object)
     */
    @Override
    public boolean equals(final Object other) {
      return ((other instanceof Key)) && (this.uuid.equals(((Key)other).uuid));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     **
     ** @return                  the string representation of this instance.
     */
    @Override
    public String toString() {
      return this.uuid.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Secret
  // ~~~~~ ~~~~~~
  /**
   ** A class that represents a secret key in a provider-independent fashion.
   */
  private static class Secret {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Key       uuid;
    private final SecretKey key;
    private final int       size;
    private boolean         compromised = false;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Secret</code> as a type 4 (pseudo randomly generated)
     ** UUID.
     **
     ** The <code>Secret</code> is generated using a cryptographically strong
     ** pseudo random number generator.
     **
     ** @param  transformation   the standard name of the requested key
     **                          transformation in the form of "algorithm" or
     **                          "algorithm/mode/padding".
     **                          See Appendix A in the Java Cryptography
     **                          Architecture Reference Guide for information
     **                          about standard algorithm names.
     ** @param  keysize          the keysize.
     **                          This is an algorithm-specific metric, specified
     **                          in number of bits.
     **
     ** @throws EncryptionException  if no Provider supports a KeyGeneratorSpi
     **                              implementation for the specified
     **                              <code>algorithm</code> or if transformation
     **                              is not a valid transformation, i.e. in the
     **                              form of "algorithm" or
     **                              "algorithm/mode/padding".
     */
    private Secret(final String transformation, final int keysize)
      throws EncryptionException {

      KeyGenerator generator;
      int          maxLength;
      try {
        // Obtain a KeyGenerator object that generates secret keys for the
        // specified algorithm. This call traverses the list of registered
        // security Providers, starting with the most preferred Provider. A new
        // KeyGenerator object encapsulating the KeyGeneratorSpi implementation
        // from the first Provider that supports the specified algorithm is
        // returned.
        generator = KeyGenerator.getInstance(transformation);
        // Obtain the maximum key length for the specified transformation
        // according to the installed JCE jurisdiction policy files. If JCE
        // unlimited strength jurisdiction policy files are installed,
        // Integer.MAX_VALUE will be obtained.
        maxLength = Cipher.getMaxAllowedKeyLength(transformation);
      }
      catch (NoSuchAlgorithmException e) {
        String message = String.format("Crypter failed to instantiate a KeyGenerator for transformation \"%s\"", new Object[] { transformation });
        throw new EncryptionException(message, e);
      }
      // initializes the key generator for a certain keysize, using a
      // randomness.
      generator.init(keysize, secureRandom);
      byte[] key = generator.generateKey().getEncoded();

      this.uuid        = new Key();
      this.key         = new SecretKeySpec(key, transformation);
      this.size        = keysize;
      this.compromised = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>SecretUUID</code> with the specified properties
     **
     ** @param  uuid             an immutable universally unique identifier
     **                          (UUID).
     ** @param  key              a secret key in a provider-independent fashion.
     **                          A raw secret key represented by a byte array
     **                          without key parameters associated with it,
     **                          e.g., DES or Triple DES keys.
     ** @param  size             the keysize.
     **                          This is an algorithm-specific metric, specified
     **                          in number of bits.
     ** @param  compromised      <code>true</code> if the secret is not
     **                          trustable due to may be tampered.
     */
    private Secret(final Key uuid, final SecretKey key, final int size, final boolean compromised) {
      this.uuid        = uuid;
      this.key         = key;
      this.size        = size;
      this.compromised = compromised;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: uuid
    public final Key uuid() {
      return this.uuid;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: key
    public SecretKey key() {
      return this.key;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: size
    public int size() {
      return this.size;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: compromised
    public boolean compromised() {
      return this.compromised;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setCompromised
    public void setCompromised() {
      this.compromised = true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Crypter
  // ~~~~~ ~~~~~~~
  /**
   ** A class that represents a secret key in a provider-independent fashion.
   */
  private static class Crypter extends Secret {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String transformation;
    private int          initialization = -1;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Crypter</code> as a type 4 (pseudo randomly
     ** generated) UUID.
     **
     ** The <code>Crypter</code> is generated using a cryptographically strong
     ** pseudo random number generator.
     **
     ** @param  transformation   the standard name of the requested key
     **                          transformation in the form of "algorithm" or
     **                          "algorithm/mode/padding".
     **                          See Appendix A in the Java Cryptography
     **                          Architecture Reference Guide for information
     **                          about standard algorithm names.
     ** @param  keysize          the keysize.
     **                          This is an algorithm-specific metric, specified
     **                          in number of bits.
     */
    private Crypter(final String transformation, final int keysize) {
      // ensure inheritance
      super(transformation, keysize);

      // initialize instance attributes
      this.transformation = transformation;
      this.initialization = -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Crypter</code> with the specified properties
     **
     ** @param  uuid             an immutable universally unique identifier
     **                          (UUID).
     ** @param  transformation   the standard name of the requested key
     **                          transformation in the form of "algorithm" or
     **                          "algorithm/mode/padding".
     **                          See Appendix A in the Java Cryptography
     **                          Architecture Reference Guide for information
     **                          about standard algorithm names.
     ** @param  key              a secret key in a provider-independent fashion.
     **                          A raw secret key represented by a byte array
     **                          without key parameters associated with it,
     **                          e.g., DES or Triple DES keys.
     ** @param  size             the keysize.
     **                          This is an algorithm-specific metric, specified
     **                          in number of bits.
     ** @param  initialization
     ** @param  compromised
     */
    private Crypter(final Key uuid, final String transformation, final SecretKey key, final int size, final int initialization, final boolean compromised) {
      // ensure inheritance
      super(uuid, key, size, compromised);

      // initialize instance attributes
      this.transformation = transformation;
      this.initialization = initialization;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: transformation
    /**
     ** Returns the transformation in the form of "algorithm" or
     ** "algorithm/mode/padding".
     **
     ** @return                  the transformation in the form of "algorithm"
     **                          or "algorithm/mode/padding".
     */
    public final String transformation() {
      return this.transformation;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: initialization
    /**
     ** Sets the initialization vector.
     **
     ** @param  initialization   the initialization vector.
     */
    public void initialization(final int initialization) {
      this.initialization = initialization;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: initialization
    /**
     ** Returns the initialization vector.
     **
     ** @return                  the initialization vector.
     */
    public int initialization() {
      return this.initialization;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Cryptor</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @throws NoSuchAlgorithmException if no Provider supports a
   **                                  SecretKeyFactorySpi implementation for
   **                                  the specified <code>algorithm</code>.
   ** @throws InvalidKeyException      if the given key material is shorter than
   **                                  24 bytes.
   ** @throws InvalidKeySpecException  if the key specification is inappropriate
   **                                  for the secret-key factory to produce a
   **                                  secret key.
   */
  public Cryptor()
    throws NoSuchAlgorithmException
    ,      InvalidKeyException
    ,      InvalidKeySpecException {

    // ensure inheritance
    this(KEYGEN_ALGORITHM, KEYGEN_KEYSIZE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CryptoManager</code> that allows use as a
   ** JavaBean.
   **
   ** @param  transformation     the standard name of the requested key
   **                            transformation in the form of "algorithm" or
   **                            "algorithm/mode/padding".
   **                            See Appendix A in the Java Cryptography
   **                            Architecture Reference Guide for information
   **                            about standard algorithm names.
   ** @param  keysize            the keysize.
   **                            This is an algorithm-specific metric, specified
   **                            in number of bits.
   **
   ** @throws NoSuchAlgorithmException if no Provider supports a
   **                                  SecretKeyFactorySpi implementation for
   **                                  the specified <code>algorithm</code>.
   ** @throws InvalidKeyException      if the given key material is shorter than
   **                                  24 bytes.
   ** @throws InvalidKeySpecException  if the key specification is inappropriate
   **                                  for the secret-key factory to produce a
   **                                  secret key.
   */
  public Cryptor(final String transformation, final int keysize)
    throws NoSuchAlgorithmException
    ,      InvalidKeyException
    ,      InvalidKeySpecException {

    // ensure inheritance
    this(transformation, keysize, StringUtility.hexToByte(DEFAULT_SEED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CryptoManager</code> that allows use as a
   ** JavaBean.
   **
   ** @param  transformation     the standard name of the requested key
   **                            transformation in the form of "algorithm" or
   **                            "algorithm/mode/padding".
   **                            See Appendix A in the Java Cryptography
   **                            Architecture Reference Guide for information
   **                            about standard algorithm names.
   ** @param  keysize            the keysize.
   **                            This is an algorithm-specific metric, specified
   **                            in number of bits.
   ** @param  material           the buffer with the DES-EDE key material.
   **                            The first 24 bytes of the buffer are copied to
   **                            protect against subsequent modification.
   **
   ** @throws NoSuchAlgorithmException if no Provider supports a
   **                                  SecretKeyFactorySpi implementation for
   **                                  the specified <code>algorithm</code>.
   ** @throws InvalidKeyException      if the given key material is shorter than
   **                                  24 bytes.
   ** @throws InvalidKeySpecException  if the key specification is inappropriate
   **                                  for the secret-key factory to produce a
   **                                  secret key.
   */
  public Cryptor(final String transformation, final int keysize, final byte[] material)
    throws NoSuchAlgorithmException
    ,      InvalidKeyException
    ,      InvalidKeySpecException {

    // ensure inheritance
    super();

    // initialize instance
    this.transformation = transformation;
    this.keysize        = keysize;

    final int separatorIndex  = transformation.indexOf('/');
    final String simpleAlgorithm = separatorIndex > 0 ? this.transformation.substring(0, separatorIndex) : this.transformation;
    final KeySpec   spec = new DESedeKeySpec(material);
    final SecretKey key  = SecretKeyFactory.getInstance(simpleAlgorithm).generateSecret(spec);
    importCipher(DEFAULT_UUID, transformation, key, keysize, DEFAULT_IV, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   crypter
  /**
   ** @param  uuid               an immutable universally unique identifier
   **                            (UUID).
   **
   ** @return                    the {@link Crypter} bound at <code>uuid</code>.
   */
  public static Crypter crypter(final Key uuid) {
    return crypterCache.get(uuid);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   crypter
  /**
   ** @param  transformation     the standard name of the requested key
   **                            transformation in the form of "algorithm" or
   **                            "algorithm/mode/padding".
   **                            See Appendix A in the Java Cryptography
   **                            Architecture Reference Guide for information
   **                            about standard algorithm names.
   ** @param  keysize            the keysize.
   **                            This is an algorithm-specific metric, specified
   **                            in number of bits.
   **
   ** @return                    the {@link Crypter} bound with
   **                            <code>transformation</code> and
   **                            <code>keysize</code>.
   */
  public static Crypter crypter(final String transformation, int keysize) {
    Crypter result = null;
    for (Map.Entry<Key, Crypter> i : crypterCache.entrySet()) {
      final Crypter crypter = i.getValue();
      if ((!crypter.compromised()) && (crypter.transformation().equals(transformation)) && (crypter.size() == keysize)) {
        result = crypter;
        break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   importCipher
  /**
   ** Imports and initialize a {@link Cipher} object.
   **
   ** @param  uuid               an immutable universally unique identifier
   **                            (UUID).
   ** @param  transformation     the standard name of the requested key
   **                            transformation in the form of "algorithm" or
   **                            "algorithm/mode/padding".
   **                            See Appendix A in the Java Cryptography
   **                            Architecture Reference Guide for information
   **                            about standard algorithm names.
   ** @param  secretKey          a secret key in a provider-independent fashion.
   **                            A raw secret key represented by a byte array
   **                            without key parameters associated with it,
   **                            e.g., DES or Triple DES keys.
   ** @param  keysize            the keysize.
   **                            This is an algorithm-specific metric, specified
   **                            in number of bits.
   ** @param  initialization     the number iterations applied for the
   **                            initialization vector of the  encryption key.
   ** @param  compromised        <code>true</code> if the secret is not
   **                            trustable due to may be tampered.
   **
   ** @return                    the {@link Crypter} using a cryptographically
   **                            strong pseudo random number generator.
   */
  public static Crypter importCipher(final String uuid, final String transformation, final SecretKey secretKey, final int keysize, final int initialization, final boolean compromised) {
    final Key  key     = new Key(uuid);
    Crypter    crypter = crypter(key);
    if (crypter != null) {
      if ((!crypter.transformation.equals(transformation)) || (crypter.size() != keysize) || (crypter.initialization != initialization)) {
        String message = String.format("CryptoManager detected a field mismatch between the key entry to be imported and an entry in the key cache that share the key identifier \"%s\"", new Object[] { uuid });
        throw new EncryptionException(message);
      }

      if ((compromised) && (!crypter.compromised())) {
        crypter.setCompromised();
      }
      return crypter;
    }

    crypter = new Crypter(key, transformation, secretKey, keysize, initialization, compromised);
    byte[] iv = null;
    if (initialization > 0) {
      iv = new byte[initialization / 8];
      pseudoRandom.nextBytes(iv);
    }
    // why this here without take care about the result
    cipher(crypter, 2, iv);
    crypterCache.put(crypter.uuid(), crypter);
    return crypter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generateCipher
  /**
   ** @param  transformation     the standard name of the requested key
   **                            transformation in the form of "algorithm" or
   **                            "algorithm/mode/padding".
   **                            See Appendix A in the Java Cryptography
   **                            Architecture Reference Guide for information
   **                            about standard algorithm names.
   ** @param  keysize            the keysize.
   **                            This is an algorithm-specific metric, specified
   **                            in number of bits.
   **
   ** @return                    the {@link Crypter} using a cryptographically
   **                            strong pseudo random number generator.
   */
  public static Crypter generateCipher(final String transformation, final int keysize) {
    // lookup the cipher from the cache
    Crypter crypter = crypter(transformation, keysize);
    if (crypter == null)
      crypter = new Crypter(transformation, keysize);

    Cipher  cipher  = cipher(crypter, 1, null);
    byte[]  iv      = cipher.getIV();
    crypter.initialization(iv == null ? 0 : iv.length * 8);
    crypterCache.put(crypter.uuid(), crypter);
    return crypter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cipher
  /**
   ** Creates and initialize a {@link Cipher} object that implements the
   ** algorithm specified {@link Crypter}.
   ** <p>
   ** This method traverses the list of registered security Providers, starting
   ** with the most preferred Provider. A new {@link Cipher} object
   ** encapsulating the CipherSpi implementation from the first Provider that
   ** supports the specified algorithm is returned.
   ** <br>
   ** <b>Note</b>: the list of registered providers may be retrieved via the
   **              Security.getProviders() method.
   **
   ** @param  secret
   ** @param  mode
   ** @param  initializationVector
   **
   ** @return                    a {@link Cipher} object that implements the
   **                            algorithm specified {@link Crypter}.
   **
   ** @throws EncryptionException
   */
  private static Cipher cipher(final Crypter secret, final int mode, final byte[] initializationVector) {
    Cipher cipher;
    try {
      final String[] fields = secret.transformation.split("/", 0);
      if ((fields.length > 3) && ("NONE".equals(fields[1]))) {
        assert (("RC4".equals(fields[0])) || ("ARCFOUR".equals(fields[0])));
        assert ("NoPadding".equals(fields[2]));
        cipher = Cipher.getInstance(fields[0]);
      }
      cipher = Cipher.getInstance(secret.transformation);
    }
    catch (GeneralSecurityException e) {
      String message = String.format("Crypter passed invalid Cipher transformation \"%s\":  %s", new Object[] { secret.transformation, e.getMessage() });
      throw new EncryptionException(message, e);
    }

    try {
      if (0 < secret.initialization) {
        byte[] iv;
        if ((1 == mode) && (null == initializationVector)) {
          iv = new byte[secret.initialization / 8];
          pseudoRandom.nextBytes(iv);
        }
        else {
          iv = initializationVector;
        }

        cipher.init(mode, secret.key(), new IvParameterSpec(iv));
      }
      else {
        cipher.init(mode, secret.key());
      }
    }
    catch (GeneralSecurityException ex) {
      String message = String.format("CryptoManager cannot initialize Cipher:  %s", new Object[] { ex.getMessage() });
      throw new EncryptionException(message, ex);
    }
    return cipher;
  }
}