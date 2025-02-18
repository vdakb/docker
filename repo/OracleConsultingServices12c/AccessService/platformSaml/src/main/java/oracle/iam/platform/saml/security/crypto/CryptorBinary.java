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

    File        :   CryptorBinary.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CryptorBinary.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.security.crypto;

import java.security.Provider;

import oracle.hst.platform.SecurityException;

import oracle.hst.platform.jca.Password;

////////////////////////////////////////////////////////////////////////////////
// class CryptorBinary
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Utility class for easily performing normal-strength encryption of binaries
 ** (byte arrays) objects.
 ** <p>
 ** This class internally holds a {@link PBECryptorBinary} configured this way:
 ** <ul>
 **   <li>Algorithm: <code>PBEWithMD5AndDES</code>.</li>
 **   <li>Key obtention iterations: <code>1000</code>.</li>
 ** </ul>
 ** The required steps to use it are:
 ** <ol>
 **   <li>Create an instance (using <code>new</code>).
 **   <li>Set a password (using <code> {@link #password(char[])} </code>).
 **   <li>Perform the desired <code> {@link #encrypt(byte[])} </code> or
 **       <code> {@link #decrypt(byte[])} </code> operations.
 ** </ol>
 ** <p>
 ** This class is <i>thread-safe</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CryptorBinary implements Cryptor.Binary
                           ,          Password.Guarded {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the PBECryptorBinary that will be internally used.
  private final PBECryptorBinary cryptor = new PBECryptorBinary();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>CryptorBinary</code> with the specified password to
   ** use.
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
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   */
  private CryptorBinary(final char[] password) {
    // ensure inheritance
    super();

    // initialize instance
    password(password);
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
  public void initialize()
     throws SecurityException {

     this.cryptor.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encrypt (Cryptor.Binary)
  /**
   ** Encrypts a byte array.
   **
   ** @param  binary             the byte array message to be encrypted.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the result of encryption.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   **
   ** @throws SecurityException  if the encryption operation fails or
   **                            initialization could not be correctly done (for
   **                            example, no password has been set), ommitting
   **                            any further information about the cause for
   **                            security reasons.
   **
   ** @see    PBECryptorBinary#encrypt(byte[])
   */
  @Override
  public byte[] encrypt(final byte[] binary) {
    return this.cryptor.encrypt(binary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decrypt (Cryptor.Binary)
  /**
   ** Decrypts a byte array.
   **
   ** @param  binary             the byte array message to be decrypted.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the result of decryption.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   **
   ** @throws SecurityException  if the encryption operation fails or
   **                            initialization could not be correctly done (for
   **                            example, no password has been set), ommitting
   **                            any further information about the cause for
   **                            security reasons.
   **
   ** @see    PBECryptorBinary#decrypt(byte[])
   */
  @Override
  public byte[] decrypt(final byte[] binary) {
    return this.cryptor.decrypt(binary);
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
   ** EncryptionException being thrown during initialization.
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

  ////////////////////////////////////////////////////////////////////////////
  // Method: build
  /**
   ** Factory method to create a new <code>CryptorBinary</code> instance that
   ** don't need a password to proceed (e.g. public key generation only).
   **
   ** @return                    a new <code>CryptorBinary</code> instance.
   **                            <br>
   **                            Possible object is <code>CryptorBinary</code>.
   */
  public static final CryptorBinary build() {
    return build(null);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: build
  /**
   ** Factory method to create a new <code>CryptorBinary</code> instance with
   ** the specified password to use.
   **
   ** @param  password           the password to be used.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    a new <code>CryptorBinary</code> instance.
   **                            <br>
   **                            Possible object is <code>CryptorBinary</code>.
   */
  public static final CryptorBinary build(final char[] password) {
    return new CryptorBinary(password);
  }
}