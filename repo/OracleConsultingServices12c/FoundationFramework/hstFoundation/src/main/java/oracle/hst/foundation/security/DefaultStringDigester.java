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

    File        :   DefaultStringDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DefaultStringDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.security.Provider;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.Base64Transcoder;

////////////////////////////////////////////////////////////////////////////////
// class DefaultStringDigester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Default implementation of the {@link StringDigester} interface.
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
public class DefaultStringDigester implements StringDigester {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // Prefix and suffix to be added to encryption results (if any)
  private String                      prefix   = null;
  private String                      suffix   = null;

  // the ByteDigester that will be internally used.
  private final DefaultBinaryDigester digester;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DefaultStringDigester</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DefaultStringDigester() {
    // ensure inheritance
    super();

    // initialize instance
    this.digester = new DefaultBinaryDigester();
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
    this.digester.saltSize(size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prefix
  /**
   ** Sets the prefix to be added at the beginning of encryption results, and
   ** also to be expected at the beginning of plain messages provided for
   ** matching operations (raising an {@link DigesterException} if not).
   ** <p>
   ** By default, no prefix will be added to encryption results.
   **
   ** @param  prefix             the prefix to be set
   */
  public synchronized void prefix(final String prefix) {
    if (!initialized())
      this.prefix = prefix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suffix
  /**
   ** Sets the suffix to be added at the end of encryption results, and also to
   ** be expected at the end of plain messages provided for matching operations
   ** (raising an {@link DigesterException} if not).
   ** <p>
   ** By default, no suffix will be added to encryption results.
   **
   ** @param  suffix             the suffix to be set
   */
  public synchronized void suffix(final String suffix) {
    if (initialized())
      this.suffix = suffix;
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
   **                            algorithm
   */
  @Override
  public synchronized void provider(final Provider provider) {
    this.digester.provider(provider);
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
    return this.digester.provider();
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
    this.digester.providerName(name);
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
   **
   ** @return                    the name of the security provider to be asked
   **                            for the digest algorithm.
   */
  @Override
  public String providerName() {
    return this.digester.providerName();
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
    this.digester.algorithm(algorithm);
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
    return this.digester.algorithm();
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
    this.digester.iteration(iteration);
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
    return this.digester.iteration();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt (Encryptor)
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of <code>SecureSalt</code> will be used.
   **
   ** @param  generator          the salt generator to be used.
   */
  @Override
  public synchronized void salt(final Salt generator) {
    this.digester.salt(generator);
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
    return this.digester.salt();
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
    return this.digester.initialized();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Encryptor)
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
  @Override
  public synchronized void initialize() {
    // Double-check to avoid synchronization issues
    if (!this.initialized())
      this.digester.initialize();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest (ByteDigester)
  /**
   ** Performs a digest operation on a String message.
   ** <p>
   ** The steps taken for creating the digest are:
   ** <ol>
   **   <li>The String message is converted to a byte array.
   **   <li>A salt of the specified size is generated (see {@link Salt}).
   **   <li>The salt bytes are added to the message.
   **   <li>The hash function is applied to the salt and message altogether, and
   **       then to the results of the function itself, as many times as
   **       specified (iterations).
   **   <li>If specified by the salt generator (see {@link Salt#includePlain()}),
   **       the <i>undigested</i> salt and the final result of the hash function
   **       are concatenated and returned as a result.
   **   <li>The result of the concatenation is encoded in BASE64 (default) or
   **       HEXADECIMAL and returned as an ASCII String.
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
   ** @param  message            the String to be digested
   **
   ** @return                    the digest result.
   **
   ** @throws DigesterException if the digest operation fails, or if
   **                           initialization could not be correctly done (for
   **                           example, if the digest algorithm chosen cannot
   **                           be used) ommitting any further information about
   **                           the cause for security reasons.
   */
  @Override
  public String digest(final String message) {
    // prevent bogus inpout
    if (message == null)
      return null;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // normalize Unicode message to NFC form
      String normalizedMessage = StringUtility.normalizeNfc(message);

      // the input String is converted into bytes using MESSAGE_CHARSET
      // as a fixed charset to avoid problems with different platforms
      // having different default charsets (see MESSAGE_CHARSET doc).
      final byte[] messageBytes = StringUtility.toBytes(normalizedMessage);

      // The StandardByteDigester does its job.
      byte[] digest = this.digester.digest(messageBytes);

      // We build the result variable
      final StringBuilder result = new StringBuilder();

      if (this.prefix != null)
        // Prefix is added
        result.append(this.prefix);

      // We encode the result in BASE64 so that we obtain the safest result
      // String possible.
      digest = Base64Transcoder.encode(digest);
      result.append(new String(digest, StringUtility.ASCII.name()));

      if (this.suffix != null)
        // Suffix is added
        result.append(this.suffix);

      return result.toString();
    }
    catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new DigesterException();
    }
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
   ** @param  message            the message to be compared to the digest.
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
  public boolean match(final String message, final String digest) {
    String processedDigest = digest;
    if (processedDigest != null) {
      if (this.prefix != null) {
        if (!processedDigest.startsWith(this.prefix))
          throw new DigesterException("Digest does not start with required prefix \"" + this.prefix + "\"");

        processedDigest = processedDigest.substring(this.prefix.length());
      }
      if (this.suffix != null) {
        if (!processedDigest.endsWith(this.suffix))
          throw new DigesterException("Digest does not end with required suffix \"" + this.suffix + "\"");

        processedDigest = processedDigest.substring(0, processedDigest.length() - this.suffix.length());
      }
    }

    if (message == null)
      return (processedDigest == null);
    else if (processedDigest == null)
      return false;

    // check initialization
    if (!initialized())
      initialize();

    try {
      // normalize Unicode message to NFC form
      String normalizedMessage = StringUtility.normalizeNfc(message);

      // get a valid byte array from the message, in the fixed CHARSET that
      // the digest operations use.
      final byte[] messageBytes = normalizedMessage.getBytes(StringUtility.ASCII.name());

      // the BASE64 or HEXADECIMAL encoding is reversed and the digest is
      // converted into a byte array.
      // the digest must be a US-ASCII String BASE64-encoded
      byte[] digestBytes = processedDigest.getBytes(StringUtility.ASCII.name());
      digestBytes = Base64Transcoder.decode(digestBytes);

      // The StandardByteDigester is asked to match message to digest.
      return this.digester.match(messageBytes, digestBytes);
    }
    catch (Exception e) {
      // If digest fails, it is more secure not to return any information
      // about the cause in nested exceptions. Simply fail.
      throw new DigesterException();
    }
  }
}