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

    File        :   DefaultBinaryDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultBinaryDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.util.Arrays;

import java.security.Provider;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import oracle.hst.foundation.security.salt.SecureSalt;

////////////////////////////////////////////////////////////////////////////////
// class DefaultBinaryDigester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Default implementation of the {@link BinaryDigester} interface.
 ** <p>
 ** This class lets the user specify the algorithm (and provider) to be used for
 ** creating digests, the size of the salt to be applied, the number of times
 ** the hash function will be applied (iterations) and the salt generator to be
 ** used.
 ** <p>
 ** This class avoids byte-conversion problems related to the fact of different
 ** platforms having different default charsets, and returns digests in the form
 ** of BASE64-encoded ASCII Strings.
 ** <p>
 ** This class is <i>thread-safe</i>.
 ** <p>
 ** <b><u>Configuration</u></b>
 ** <p>
 ** The algorithm, provider, salt size, iterations and salt generator can take
 ** values  in any of these ways:
 ** <ul>
 **   <li>Using its default values.
 **   <li>Calling the corresponding <code>setter</code> methods.
 ** </ul>
 ** And the actual values to be used for initialization will be established by
 ** applying the following priorities:
 ** <ol>
 **   <li>First, the default values are considered.
 **   <li>Finally, if the corresponding <code>setter</code> method has been
 **       called on the digester itself for any of the configuration parameters,
 **       the  values set by these calls override all of the above.
 ** </ol>
 ** <p>
 ** <b><u>Initialization</u></b>
 ** <p>
 ** Before it is ready to create digests, an object of this class has to be
 ** <i>initialized</i>. Initialization happens:
 ** <ul>
 **   <li>When <code>initialize</code> is called.
 **   <li>When <code>digest</code> or <code>matches</code> are called for the
 **       first time, if <code>initialize</code> has not been called before.
 ** </ul>
 ** Once a digester has been initialized, trying to change its configuration
 ** will result in a NOP.
 ** <p>
 ** <b><u>Usage</u></b>
 ** <p>
 ** A digester may be used in two different ways:
 ** <ul>
 **   <li>For <i>creating digests</i>, by calling the <code>digest</code>
 **       method.
 **   <li>For <i>matching digests</i>, this is, checking whether a digest
 **       corresponds adequately to a digest (as in password checking) or not,
 **       by calling the <code>matches</code> method.</li>
 ** </ul>
 ** The steps taken for creating digests are:
 ** <ol>
 **   <li>The String message is converted to a byte array.
 **   <li>A salt of the specified size is generated (see {@link oracle.hst.foundation.security.Salt})
 **   <li>The salt bytes are added to the message.
 **   <li>The hash function is applied to the salt and message altogether, and
 **       then to the results of the function itself, as many times as specified
 **       (iterations).
 **   <li>If specified by the salt generator (see (see {@link oracle.hst.foundation.security.Salt}),
 **       the <i>undigested</i> salt and the final result of the hash function
 **       are concatenated and returned as a result.
 **   <li>The result of the concatenation is encoded in BASE64 or HEXADECIMAL
 **       and returned as an ASCII String.
 ** </ol>
 ** Put schematically in bytes:
 ** <ul>
 **   <li>DIGEST = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code>
 **     <ul>
 **       <li><code><b>S</b></code>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.
 **       <li><code>ssb</code>: salt size in bytes.
 **       <li><code><b>X</b></code>: bytes resulting from hashing (see below).
 **     </ul>
 **   </li>
 **   <li><code>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code> = <code><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</code>
 **     <ul>
 **       <li><code><i>H</i></code>: Hash function (algorithm).
 **       <li><code>it</code>: Number of iterations.
 **       <li><code><b>Z</b></code>: Input for hashing (see below).
 **     </ul>
 **   </li>
 **   <li>
 **     <code>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</code> = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</code>
 **     <ul>
 **       <li><code><b>S</b></code>: salt bytes (plain, not digested).
 **       <li><code>ssb</code>: salt size in bytes.
 **       <li><code><b>M</b></code>: message bytes.
 **     </ul>
 **   </li>
 ** </ul>
 ** <b>If a random salt generator is used, two digests created for the same
 ** message will always be different (except in the case of random salt
 ** coincidence).</b>
 ** <br>
 ** Because of this, in this case the result of the <code>digest</code> method
 ** will contain both the <i>undigested</i> salt and the digest of the
 ** (salt + message), so that another digest operation can be performed with the
 ** same salt on a different message to check if both messages match (all of
 ** which will be managed automatically by the  <code>matches</code> method).
 ** <p>
 ** To learn more about the mechanisms involved in digest creation, read
 ** <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class DefaultBinaryDigester implements BinaryDigester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** default digest algorithm will be MD5 */
  public static final String ALGORITHM    = "MD5";

  /** the minimum recommended iterations for hashing are 1000 */
  public static final int    ITERATION    = 1000;

  /** the minimum recommended size for salt is 8 bytes */
  public static final int    SALT_SIZE    = 8;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // Algorithm to be used for hashing
  private String             algorithm    = ALGORITHM;

  // Number of hash iterations to be applied
  private int                iteration    = ITERATION;

  // Size of salt to be applied
  private int                saltSize     = SALT_SIZE;

  // if the salt size is set to a value higher than zero, this flag will
  // indicate that the salt mecanism has to be used.
  private boolean            salted       = true;

  // Salt generator to be used.
  // Initialization of a salt generator is costly, and so default value will be
  // applied only in initialize(), if it finally becomes necessary.
  private Salt               salt         = null;

  // Name of the java.security.Provider which will be asked for the selected
  // algorithm
  private String             providerName = null;

  // java.security.Provider instance which will be asked for the selected
  // algorithm
  private Provider           provider     = null;

  // MessageDigest to be used.
  //
  // IMPORTANT: MessageDigest is not a thread-safe class, and thus any use of
  // this variable will have to be adequately synchronized.
  private MessageDigest      digest       = null;

  // Length of the result digest for the specified algorithm.
  // This might be zero if this operation is not supported by the  algorithm
  // provider and the implementation is not cloneable.
  private int                digestLength  = 0;

  // Flag which indicates whether the digester has been initialized or not.
  //
  // Once initialized, all further modifications to its configuration will be
  // ignored.
  private boolean            initialized   = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DefaultBinaryDigester</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DefaultBinaryDigester() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saltSize
  /**
   ** Sets the size of the salt to be used to compute the digest.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** If salt size is set to zero, then no salt will be used.
   **
   ** @param  size               the size of the salt to be used, in bytes.
   */
  public synchronized void saltSize(final int size) {
    if (!initialized()) {
      this.saltSize = size;
      this.salted   = (saltSize > 0);
    }
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
  public synchronized void provider(final Provider provider) {
    if (initialized())
      this.provider = provider;
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
    return this.provider;
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
  public synchronized void providerName(final String name) {
    if (initialized())
      this.providerName = name;
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
    return this.providerName;
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
  public synchronized void algorithm(final String algorithm) {
    if (!initialized())
      this.algorithm = algorithm;
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
    return this.algorithm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration (Encryptor)
  /**
   ** Set the number of times the hash function will be applied recursively.
   ** <p>
   ** The hash function will be applied to its own results as many times as
   ** specified: <i>h(h(...h(x)...))</i>
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
    return this.salt.generate(this.saltSize);
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
    return this.initialized;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest (ByteDigester)
  /**
   ** Performs a digest operation on a byte array message.
   ** <p>
   ** The steps taken for creating the digest are:
   ** <ol>
   **   <li>A salt of the specified size is generated (see {@link Salt}).
   **   <li>The salt bytes are added to the message.
   **   <li>The hash function is applied to the salt and message altogether, and
   **       then to the results of the function itself, as many times as
   **       specified (iterations).
   **   <li>If specified by the salt generator (see
   **       {@link Salt#includePlain()}), the
   **       <i>undigested</i> salt and the final result of the hash function are
   **       concatenated and returned as a result.
   ** </ol>
   ** Put schematically in bytes:
   ** <ul>
   **   <li>DIGEST = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code>
   **     <ul>
   **       <li><code><b>S</b></code>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.
   **       <li><code>ssb</code>: salt size in bytes.
   **       <li><code><b>X</b></code>: bytes resulting from hashing (see below).
   **     </ul>
   **   </li>
   **   <li><code>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</code> = <code><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</code>
   **     <ul>
   **       <li><code><i>H</i></code>: Hash function (algorithm).
   **       <li><code>it</code>: Number of iterations.
   **       <li><code><b>Z</b></code>: Input for hashing (see below).
   **     </ul>
   **   </li>
   **   <li><code>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</code> = <code>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</code>
   **     <ul>
   **       <li><code><b>S</b></code>: salt bytes (plain, not digested).
   **       <li><code>ssb</code>: salt size in bytes.
   **       <li><code><b>M</b></code>: message bytes.
   **     </ul>
   **   </li>
   ** </ul>
   ** <b>If a random salt generator is used, two digests created for the same
   ** message will always be different (except in the case of random salt
   ** coincidence).</b>
   ** Because of this, in this case the result of the <code>digest</code> method
   ** will contain both the <i>undigested</i> salt and the digest of the
   ** (salt + message), so that another digest operation can be performed with
   ** the same salt on a different message to check if both messages match (all
   ** of which will be managed automatically by the <code>matches</code>
   ** method).
   **
   ** @param  binary             the byte array to be digested.
   **
   ** @return                    the digest result.
   **
   ** @throws DigesterException if the digest operation fails, or if
   **                           initialization could not be correctly done (for
   **                           example, if the digest algorithm chosen cannot
   **                           be used) ommitting any further information about
   **                           the cause for security reasons.
   */
  public byte[] digest(final byte[] binary) {
    // prevent bogus inpout
    if (binary == null)
      return binary;

    // check initialization
    if (!initialized())
      initialize();

    // create salt
    byte[] salt = null;
    if (this.salted)
      salt = this.salt.generate(this.saltSize);

    // create digest
    return digest(binary, salt);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match (ByteDigester)
  /**
   ** Checks a message against a given digest.
   ** <p>
   ** This method tells whether a message corresponds to a specific digest or
   ** not by getting the salt with which the digest was created and applying it
   ** to a digest operation performed on the message. If new and existing digest
   ** match, the message is said to match the digest.
   ** <p>
   ** This method will be used, for instance, for password checking in
   ** authentication processes.
   ** <p>
   ** A <code>null</code> message will only match a <code>null</code> digest.
   **
   ** @param  binary             the message to be compared to the digest.
   ** @param  digest             the digest to be compared to the digest.
   **
   ** @return                    <code>true</code> if the specified message
   **                            matches the digest, <code>false</code>
   **                            otherwise.
   **
   ** @throws DigesterException if the digest operation fails, or if
   **                           initialization could not be correctly done (for
   **                           example, if the digest algorithm chosen cannot
   **                           be used) ommitting any further information about
   **                           the cause for security reasons.
   */
  public boolean match(final byte[] binary, final byte[] digest) {
    // prevent bogus inpout
    if (binary == null)
      return (digest == null);
    else if (digest == null)
      return false;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // if we are using a salt, extract it to use it.
      byte[] salt = null;
      if (this.salted) {
        // if we are using a salt generator which specifies the salt to be
        // included into the digest itself, get it from there.
        // if not, the salt is supposed to be fixed and thus the salt generator
        // can be safely asked for it again.
        if (this.salt.includePlain()) {
          // compute size figures and perform length checks
          if (this.digestLength > 0) {
            if (digest.length != (this.digestLength + this.saltSize))
              throw new DigesterException();
          }
          else {
            // Salt size check behaviour cannot be set to lenient
            if (digest.length < this.saltSize)
              throw new DigesterException();
          }

          salt = new byte[this.saltSize];
          System.arraycopy(digest, 0, salt, 0, this.saltSize);
//          System.arraycopy(digest, digest.length - this.saltSize, salt, 0, this.saltSize);
        }
        else
          salt = this.salt.generate(this.saltSize);
      }

      // digest the message with the extracted digest.
      final byte[] encryptedMessage = digest(binary, salt);

      // if, using the same salt, digests match, then messages too.
      return (Arrays.equals(encryptedMessage, digest));
    }
    catch (Exception e) {
      // if digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new DigesterException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the digester.
   ** <p>
   ** This operation will consist in determining the actual configuration values
   ** to be used, and then initializing the digester with them.
   ** <p>
   ** Once a digester has been initialized, trying to change its configuration
   ** will result in NOP.
   **
   ** @throws DigesterException  if initialization could not be correctly done
   **                            (for example, if the digest algorithm chosen
   **                            cannot be used).
   */
  public synchronized void initialize() {
    // Double-check to avoid synchronization issues
    if (!this.initialized) {
      // MessageDigest is initialized the usual way, and the digester is marked
      // as "initialized" so that configuration cannot be changed in the future.
      try {
        // if the digester was not set a salt generator in any way, it is time to
        // apply its default value.
        if (this.salt == null)
          this.salt = new SecureSalt();

        if (this.provider != null) {
          this.digest = MessageDigest.getInstance(this.algorithm, this.provider);
        }
        else if (this.providerName != null) {
          this.digest = MessageDigest.getInstance(this.algorithm, this.providerName);
        }
        else {
          this.digest = MessageDigest.getInstance(this.algorithm);
        }
      }
      catch (NoSuchAlgorithmException e) {
        throw new DigesterException(e);
      }
      catch (NoSuchProviderException e) {
        throw new DigesterException(e);
      }


      // store the digest length (algorithm-dependent) and check the operation
      // is supported by the provider.
      this.digestLength = this.digest.getDigestLength();
      if (this.digestLength <= 0)
        throw new DigesterException("The configured algorithm (" + this.algorithm + ") or its provider do not allow knowing the digest length beforehand (getDigestLength() operation), which is not compatible with setting the salt size checking behaviour to \"lenient\".");

      this.initialized = true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** This method truly performs the digest operation, assuming that a salt has
   ** already been created (if needed) and the digester has already been
   ** initialized.
   **
   ** @param  binary             the byte array to be digested.
   ** @param  salt               the salt bytes to be added before the message
   **                            to be digested.
   **
   ** @return                    the digest result.
   */
  private byte[] digest(final byte[] binary, final byte[] salt) {
    try {
      byte[] digest = null;
      synchronized (this.digest) {
        this.digest.reset();
        if (salt != null) {
          // the salt bytes are added before the message to be digested
          this.digest.update(salt);
          this.digest.update(binary);
        }
        else {
          // no salt to be added
          this.digest.update(binary);
        }

        digest = this.digest.digest();
        for (int i = 0; i < (this.iteration - 1); i++) {
          this.digest.reset();
          digest = this.digest.digest(digest);
        }
      }
      // finally we build an array containing both the unhashed (plain) salt
      // and the digest of the (salt + message). This is done only
      // if the salt generator we are using specifies to do so.
      if (this.salt.includePlain() && salt != null) {
        // Insert unhashed salt before the hashing result (default behaviour)
        return append(salt, digest);
      }
      return digest;
    }
    catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new DigesterException();
    }
  }

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
  private static byte[] append(final byte[] first, final byte[] second) {
    final byte[] result = new byte[first.length + second.length];
    System.arraycopy(first,  0, result, 0,            first.length);
    System.arraycopy(second, 0, result, first.length, second.length);
    return result;
  }
}