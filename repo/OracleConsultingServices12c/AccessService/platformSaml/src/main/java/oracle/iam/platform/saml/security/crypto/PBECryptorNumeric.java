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

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   PBECryptorNumeric.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PBECryptorNumeric.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.security.crypto;

import java.security.Provider;

import oracle.hst.platform.SecurityException;

import oracle.hst.platform.jca.Password;

////////////////////////////////////////////////////////////////////////////////
// abstract class PBECryptorNumeric
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Base implementation of an encryptor to handle numerical values.
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
 ** Before it is ready to encrypt/decrypt digests, an object of the subclasses
 ** has to be <i>initialized</i>. Initialization happens:
 ** <ul>
 **   <li>When <code>initialize</code> is called.
 **   <li>When <code>encrypt(...)</code> or <code>decrypt(...)</code> are called
 **       for the first time, if <code>initialize()</code> has not been called
 **       before.
 ** </ul>
 ** Once an encryptor has been initialized, trying to change its configuration
 ** will result in a NOP.
 ** <p>
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
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class PBECryptorNumeric implements Cryptor
                                        ,          Password.Guarded {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the PBEBinaryCryptor that will be internally used.
  protected final PBECryptorBinary cryptor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PBECryptorNumeric</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected PBECryptorNumeric() {
    // ensure inheritance
    this(new PBECryptorBinary());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PBECryptorNumeric</code> with the specified password
   ** to use.
   ** <p>
   ** <b>There is no default value for password</b>, so not setting this
   ** parameter from a call to <code>password</code> will result in an
   ** {@link SecurityException} being thrown during initialization.
   **
   ** @param  password           the password to be used.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   */
  protected PBECryptorNumeric(final char[] password) {
    // ensure inheritance
    this();

    // initialize instance
    password(password);
    initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBECryptorNumeric</code> using the specified byte
   ** cryptor (constructor used for cloning).
   **
   ** @param  cryptor            the byte encryptor to use.
   **                            <br>
   **                            Allowed object is {@link PBECryptorBinary}.
   */
  protected PBECryptorNumeric(final PBECryptorBinary cryptor) {
    // ensure inheritance
    super();

    // initialize instance
    this.cryptor = cryptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Cryptor)
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
   **                            <br>
   **                            Allowed object is {@link Provider}.
   */
  @Override
  public void provider(final Provider provider) {
    this.cryptor.provider(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider (Cryptor)
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
   **                            <br>
   **                            Possible object is {@link Provider}.
   */
  @Override
  public synchronized Provider provider() {
    return this.cryptor.provider();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Cryptor)
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void providerName(final String name) {
    this.cryptor.providerName(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName (Cryptor)
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
   **                            for the encryption algorithm.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String providerName() {
    return this.cryptor.providerName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Cryptor)
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
   **
   ** @param  algorithm          the name of the algorithm to be used.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public void algorithm(final String algorithm) {
    this.cryptor.algorithm(algorithm);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm (Cryptor)
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
  @Override
  public String algorithm() {
    return this.cryptor.algorithm();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Cryptor)
  /**
   ** Set the number of hashing iterations applied to obtain the encryption key.
   ** <p>
   * This mechanism is explained in
   * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @param  iteration          the number of iterations.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  public void iteration(final int iteration) {
    this.cryptor.iteration(iteration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Cryptor)
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
  @Override
  public int iteration() {
    return this.cryptor.iteration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Cryptor)
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of <code>SecureSalt</code> will be used.
   **
   ** @param  generator          the salt generator to be used.
   **                            <br>
   **                            Allowed object is {@link Salt}.
   */
  @Override
  public void salt(final Salt generator) {
    this.cryptor.salt(generator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Cryptor)
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
  @Override
  public final byte[] salt() {
    return this.cryptor.salt();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialized (Cryptor)
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean initialized() {
    return this.cryptor.initialized();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Cryptor)
  /**
   ** Initialize the encryptor.
   ** <p>
   ** This operation will consist in determining the actual configuration values
   ** to be used, and then initializing the encryptor with them.
   ** <p>
   ** Once a digester has been initialized, trying to change its configuration
   ** will result in NOP.
   **
   ** @throws SecurityException  if initialization could not be correctly done
   **                            (for example, if the encrpytion algorithm
   **                            chosen cannot be used).
   */
  @Override
  public void initialize() {
    this.cryptor.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password (Password.Guarded)
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
   ** {@link SecurityException} being thrown during initialization.
   **
   ** @param  password           the password to be used.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   */
  @Override
  public synchronized void password(final char[] password) {
    this.cryptor.password(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clean (Password.Guarded)
  /**
   ** Zero-out the password char array.
   **
   ** @param  password           the char array to cleanup.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   */
  @Override
  public final void clean(final char[] password) {
    this.cryptor.clean(password);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   process
  static byte[] process(final byte[] byteArray, final int sign) {
    // Check size
    if (byteArray.length > 4) {
      final int    initial   = byteArray.length;
      final byte[] encrypted = new byte[4];
      System.arraycopy(byteArray, (initial - 4), encrypted, 0, 4);

      final byte[] processed = new byte[initial - 4];
      System.arraycopy(byteArray, 0, processed, 0, (initial - 4));

      final int expected = bytesToInt(encrypted);
      if (expected < 0 || expected > safeSize())
        throw SecurityException.simpleFailed();

      // if expected and real sizes do not match, we will need to pad
      // (this happens because BigInteger removes 0x0's and -0x1's in
      // the leftmost side).
      if (processed.length != expected) {
        // BigInteger can have removed, in the leftmost side:
        //   o 0x0's : for not being significative
        //   o -0x1's: for being translated as the "signum"
        final int sizeDifference = (expected - processed.length);
        final byte[] padded = new byte[expected];
        for (int i = 0; i < sizeDifference; i++)
          padded[i] = (sign >= 0) ? (byte)0x0 : (byte)-0x1;

        // finally, the encrypted message bytes are represented
        // as they supposedly were when they were encrypted.
        System.arraycopy(processed, 0, padded, sizeDifference, processed.length);
        return padded;
      }
      return processed;
    }
    return byteArray.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intToBytes
  /**
   ** Converts an int to an array of bytes.
   **
   ** @param  number             the int to convert.
   **                            <br>
   **                            Allowed object <code>int</code>.
   **
   ** @return                    the converted byte array.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  protected static byte[] intToBytes(final int number) {
    return new byte[] {
      (byte)(0xff & (number >> 24))
    , (byte)(0xff & (number >> 16))
    , (byte)(0xff & (number >> 8))
    , (byte)(0xff & number)
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToInt
  /**
   ** Returns the int for an array of bytes.
   **
   ** @param  raw                the byte array to convert.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the int
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  protected static int bytesToInt(final byte[] raw) {
    // prevent boguxs input
    if (raw == null || raw.length == 0)
      throw new IllegalArgumentException("Cannot convert an empty array into an int");

    int result = (0xff & raw[0]);
    for (int i = 1; i < raw.length; i++)
      result = (result << 8) | (0xff & raw[i]);

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   safeSize
  /**
   ** In order to avoid Java heap size exceptions due to malformations or
   ** manipulation of encrypted data, we will only consider "safe" the
   ** allocation of numbers with a size in bytes less or equal to half the
   ** available free memory.
   ** <p>
   ** Available free memory is computed as current free memory (in the amount
   ** of memory currently allocated by the JVM) plus all the amount of memory
   ** that the JVM will be allowed to allocate in the future (until maxMemory).
   **
   ** @return                    the as "safe" considered size.
   **                            <br>
   **                            Possible object <code>int</code>.
   */
  private static int safeSize() {
    final long m  = Runtime.getRuntime().maxMemory();
    final long f = Runtime.getRuntime().freeMemory();
    final long t = Runtime.getRuntime().totalMemory();
    return (int)((f + (m - t)) / 2);
  }
}