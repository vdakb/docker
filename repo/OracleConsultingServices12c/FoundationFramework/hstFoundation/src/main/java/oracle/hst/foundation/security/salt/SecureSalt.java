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

    File        :   SecureSalt.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SecureSalt.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.foundation.security.salt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import oracle.hst.foundation.security.Salt;

////////////////////////////////////////////////////////////////////////////////
// class SecureSalt
// ~~~~~ ~~~~~~~~~~
/**
 ** This implementation of {@link Salt} generates a <b>secure</b> random salt
 ** which can be used for for encryption or digesting.
 ** <p>
 ** The algorithm used for random number generation can be configured at
 ** instantiation time. If not, the default algorithm will be used.
 ** <p>
 ** This class is <i>thread-safe</i>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class SecureSalt implements Salt {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default algorithm to be used for secure random number generation:.
   */
  public static final String ALGORITHM = "SHA1PRNG";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final SecureRandom random;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecureSalt</code> using the default secure random number
   ** generation algorithm.
   **
   ** @throws NoSuchAlgorithmException if initialization could not be correctly
   **                                  done (for example, if the random
   **                                  algorithm chosen cannot be used).
   */
  public SecureSalt()
    throws NoSuchAlgorithmException {

    // ensure inheritance
    super();

    // initialize instance
    this.random = SecureRandom.getInstance(ALGORITHM);
    this.random.setSeed(System.currentTimeMillis());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate (Salt)
  /**
   ** This method will be called for requesting the generation of a new salt of
   ** the specified length.
   **
   ** @param  length             the requested length for the salt.
   **
   ** @return                    the generated salt.
   */
  @Override
  public byte[] generate(final int length) {
    final byte[] salt = new byte[length];
    synchronized (this.random) {
      this.random.nextBytes(salt);
    }
    return salt;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   includePlain (Salt)
  /**
   ** Determines if the digests and encrypted messages created with a specific
   ** salt generator will include (prepended) the unencrypted salt itself, so
   ** that it can be used for matching and decryption operations.
   ** <p>
   ** Generally, including the salt unencrypted in encryption results will be
   ** mandatory for randomly generated salts, or for those generated in a
   ** non-predictable manner. Otherwise, digest matching and decryption
   ** operations will always fail.
   ** <p>
   ** For fixed salts, inclusion will be optional (and in fact undesirable if we
   ** want to hide the salt value).
   **
   ** @return                    whether the plain (unencrypted) salt has to be
   **                            included in encryption results or not.
   */
  @Override
  public boolean includePlain() {
    return true;
  }
}