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

    File        :   Salt.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Salt.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml.security.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Arrays;

////////////////////////////////////////////////////////////////////////////////
// interface Salt
// ~~~~~~~~~ ~~~~
/**
 ** Common interface for all salt generators which can be applied in digest or
 ** encryption operations.
 ** <p>
 ** <b>Every implementation of this interface must be thread-safe</b>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Salt {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Zero
  // ~~~~~ ~~~~
  /**
   ** This implementation of {@link Salt} always returns a salt of the required
   ** length, filled with <i>zero</i> bytes.
   ** <p>
   ** This class is <i>thread-safe</i>.
   */
  static final class Zero implements Salt {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ZeroSalt</code>.
     */
    private Zero() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generate (Salt)
    /**
     ** This method will be called for requesting the generation of a new salt
     ** of the specified length.
     **
     ** @param  length           the requested length for the salt.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the generated salt.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     */
    @Override
    public byte[] generate(final int length) {
      final byte[] salt = new byte[length];
      Arrays.fill(salt, (byte)0);
      return salt;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Secure
  // ~~~~~ ~~~~~~
  /**
   ** This implementation of {@link Salt} generates a <b>secure</b> random salt
   ** which can be used for for encryption or digesting.
   ** <p>
   ** The algorithm used for random number generation can be configured at
   ** instantiation time. If not, the default algorithm will be used.
   ** <p>
   ** This class is <i>thread-safe</i>.
   */
  static final class Secure implements Salt {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The default algorithm to be used for secure random number generation:.
     */
    static final String ALGORITHM = "SHA1PRNG";

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    private final SecureRandom random;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Secure</code> {@link Salt} using the default secure
     ** random number generation algorithm.
     **
     ** @throws NoSuchAlgorithmException if initialization could not be
     **                                  correctly done (for example, if the
     **                                  random algorithm chosen cannot be
     **                                  used).
     */
    private Secure()
      throws NoSuchAlgorithmException {

      // ensure inheritance
      super();

      // initialize instance
      this.random = SecureRandom.getInstance(ALGORITHM);
      this.random.setSeed(System.currentTimeMillis());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: generate (Salt)
    /**
     ** This method will be called for requesting the generation of a new salt
     ** of the specified length.
     **
     ** @param  length           the requested length for the salt.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the generated salt.
     **                          <br>
     **                          Possible object is array of <code>byte</code>.
     */
    @Override
    public byte[] generate(final int length) {
      final byte[] salt = new byte[length];
      synchronized (this.random) {
        this.random.nextBytes(salt);
      }
      return salt;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   include (Salt)
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
     ** For fixed salts, inclusion will be optional (and in fact undesirable if
     ** we want to hide the salt value).
     **
     ** @return                  whether the plain (unencrypted) salt has to be
     **                          included in encryption results or not.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean include() {
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** This method will be called for requesting the generation of a new salt of
   ** the specified length.
   **
   ** @param  length             the requested length for the salt.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the generated salt.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  byte[] generate(final int length);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   include
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  default boolean include() {
    return false;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: zero
  /**
   ** Factory method to create a new <code>Salt</code> instance that always
   ** returns a salt of the required length, filled with <i>zero</i> bytes.
   **
   ** @return                  a new <code>Salt</code> instance.
   **                          <br>
   **                          Possible object is <code>Salt</code>.
   */
  static Salt zero() {
    return new Zero();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: secure
  /**
   ** Factory method to create a new <code>Salt</code> instance that generates a
   ** <b>secure</b> random salt which can be used for for encryption or
   ** digesting.
   **
   ** @return                  a new <code>Salt</code> instance.
   **                          <br>
   **                          Possible object is <code>Salt</code>.
   **
   ** @throws NoSuchAlgorithmException if initialization could not be correctly
   **                                  done (for example, if the random
   **                                  algorithm chosen cannot be used).
   */
  static Salt secure()
      throws NoSuchAlgorithmException {

      return new Secure();
  }
}