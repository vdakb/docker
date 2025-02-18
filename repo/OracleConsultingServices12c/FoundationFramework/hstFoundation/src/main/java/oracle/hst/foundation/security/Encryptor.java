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

    File        :   BinaryEncryptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    BinaryEncryptor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security;

import java.security.Provider;

////////////////////////////////////////////////////////////////////////////////
// interface Encryptor
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** Common interface for all encryptors.
 ** <p>
 ** For a default implementation, see {@link DefaultBinaryEncryptor}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public interface Encryptor {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
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
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  provider           the {@link Provider} to be asked for the chosen
   **                            algorithm.
   */
  void provider(final Provider provider);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
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
  Provider provider();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName
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
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  name               the name of the security provider to be asked
   **                            for the digest algorithm.
   */
  void providerName(final String name);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   providerName
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
  String providerName();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
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
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  algorithm          the name of the algorithm to be used.
   */
  void algorithm(final String algorithm);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   algorithm
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
  String algorithm();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration
  /**
   ** Set the number of hashing iterations applied to obtain the encryption key.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  iteration          the number of iterations.
   */
  void iteration(final int iteration);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iteration
  /**
   ** Returns the number of hashing iterations applied to obtain the encryption
   ** key.
   ** <p>
   ** This mechanism is explained in <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
   **
   ** @return                    the number of iterations applied to obtain the
   **                            encryption key.
   */
  int iteration();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salt
  /**
   ** Sets the salt generator to be used. If no salt generator is specified, an
   ** instance of {@link oracle.hst.foundation.security.salt.SecureSalt} will be
   ** used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @param  generator          the salt generator to be used.
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
   ** @throws EncryptionException if initialization could not be correctly done
   **                             (for example, if the digest algorithm chosen
   **                             cannot be used).
   */
  void initialize();
}