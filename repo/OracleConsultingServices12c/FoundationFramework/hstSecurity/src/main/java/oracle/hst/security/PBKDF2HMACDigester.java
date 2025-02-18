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

    File        :   PBKDF2HMACDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PBKDF2HMACDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.0.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.security.SecureRandom;

import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;

////////////////////////////////////////////////////////////////////////////////
// class PBKDF2HMACDigester
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This class defines a password storage scheme based on the secure hash
 ** algorithms defined in FIPS 180-2.
 ** <p>
 ** This is a one-way digest algorithm so there is no way to retrieve the
 ** original clear-text version of the password from the hashed value (although
 ** this means that it is not suitable for things that need the clear-text
 ** password like DIGEST-MD5).
 ** <p>
 ** The values that it generates are also salted, which protects against
 ** dictionary attacks. It does this by generating a 64-bit random salt which is
 ** appended to the clear-text value.
 ** <br>
 ** A secure hash is then generated based on this, the salt is appended to the
 ** hash, and then the entire value is base64-encoded.
 ** Using the secure random option, this class complies with the statistical
 ** random number generator tests specified in FIPS 140-2, Security
 ** Requirements for Cryptographic Modules, secition 4.9.1.
 ** <p>
 ** Comparing the hashes in "length-constant" time ensures that an attacker
 ** cannot extract the hash of a password in an on-line system using a timing
 ** attack, then crack it off-line.
 */
class PBKDF2HMACDigester extends    Digester
                         implements Credential {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of bytes of salt.
   ** <br>
   ** By default, 8 bytes, which is 64 bits. This is more than enough.
   ** <br>
   ** This constant should not be changed.
   */
  public static final int     SALT_SIZE      = 8;

  /**
   ** The number of PBKDF2 iterations applied for obtaining the encryption
   ** key from the specified password.
   ** <br>
   ** By default, it is 10,000. To provide greater protection of passwords, at
   ** the expense of needing more processing power to validate passwords,
   ** increase the number of iterations. The number of iterations should not be
   ** decreased.
   ** <p>
   ** The NIST recommends at least 1,000 iterations:
   ** http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
   ** iOS 4.x reportedly uses 10,000:
   ** http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
   */
  public static final int     HASH_ITERATION = 10000;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static SecureRandom PRNG;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  /*
   * Static block to take care of one time secureRandom seed.
   * It takes a few seconds to initialize SecureRandom. You mightwant to
   * consider removing this static block or replacing it with a "time since
   * first loaded" seed to reduce this time.
   * This block will run only once per JVM instance.
   */
  static {
    // Initialization of a salt generator is costly, and so default value will
    // be applied only in initialize(), if it finally becomes necessary.
    // VERY important to use SecureRandom instead of just Random
    PRNG = new SecureRandom();
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The number of hash output bytes. */
  final int                                     hashSize;

  /** The digester algorithm tag like {SSHA-512}*/
  final String                                  digesterTag;

  final SecretKeyFactory                        digester;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBKDF2HMACDigester</code> handler with the specified
   ** properties.
   **
   ** @param  digesterTag        the hash schema prefix to prepend at a encoded
   **                            hash value.
   ** @param  size               the size of a generated hash.
   ** @param  digester           the hash algorithm to use.
   */
  protected PBKDF2HMACDigester(final String digesterTag, final int size, final SecretKeyFactory digester) {
    // ensure inheritance
    super();

    // initalize instance attributes
    this.hashSize    = size;
    this.digester    = digester;
    this.digesterTag = digesterTag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest (CredentialDigester)
  /**
   ** Returns a salted and hashed password using the provided hash.
   ** <p>
   ** This algorithm follows additional references of the standard practice in
   ** password hashing directly from
   ** <a href="https://www.owasp.org/index.php/Hashing_Java">OWASP</a>.
   ** <br>
   ** <b>Note</b>: Side effect: the password is destroyed (the char[] is filled
   ** with zeros)
   **
   ** @param  password           the character sequence (aka password) to be
   **                            hashed.
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the hashed password with a pinch of salt.
   **                            Possible object array of {@link String}.
   */
  @Override
  public String digest(final String password) {
    return digest(password.toCharArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match (CredentialDigester)
  /**
   ** Matchs an plain text password against an hashed one (a digest) to verify
   ** if they match.
   ** <p>
   ** Returns <code>true</code> if the given password and salt match the hashed
   ** value, false otherwise.
   ** <br>
   ** <b>Note</b>: Side effect: the password is destroyed (the char[] is filled
   ** with zeros)
   **
   ** @param  password           the plain password to be compared to the
   **                            expected hash.
   **                            Allowed object array of <code>char</code>s.
   ** @param  expected           the expected password to be compared to the
   **                            plain password.
   **                            Allowed object array of <code>char</code>s.
   **
   ** @return                    <code>true</code> if passwords match,
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean match(final String password, final String expected) {
    return match(password.toCharArray(), expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest
  /**
   ** Returns a salted and hashed password using the provided hash.
   ** <p>
   ** This algorithm follows additional references of the standard practice in
   ** password hashing directly from
   ** <a href="https://www.owasp.org/index.php/Hashing_Java">OWASP</a>.
   ** <br>
   ** <b>Note</b>: Side effect: the password is destroyed (the char[] is filled
   ** with zeros)
   **
   ** @param  password           the character sequence (aka password) to be
   **                            hashed.
   **                            Allowed object array of <code>char</code>s.
   **
   ** @return                    the hashed password with a pinch of salt.
   **                            Possible object array of {@link String}.
   */
  protected String digest(final char[] password) {
    // generate a long random salt using a CSPRNG.
    final byte[] salt = new byte[SALT_SIZE];
    PRNG.nextBytes(salt);
    byte[] digest = hash(password, salt);
    byte[] salted = new byte[digest.length + SALT_SIZE];
    System.arraycopy(digest, 0, salted, 0,             digest.length);
    System.arraycopy(salt,   0, salted, digest.length, SALT_SIZE);
    // base64-the whole thing
    return encode(salted);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Matchs an plain text password against an hashed one (a digest) to verify
   ** if they match.
   ** <p>
   ** Returns <code>true</code> if the given password and salt match the hashed
   ** value, false otherwise.
   ** <br>
   ** <b>Note</b>: Side effect: the password is destroyed (the char[] is filled
   ** with zeros)
   **
   ** @param  password           the plain password to be compared to the
   **                            expected hash.
   **                            Allowed object array of <code>char</code>s.
   ** @param  expected           the expected password to be compared to the
   **                            plain password.
   **                            Allowed object array of <code>char</code>s.
   **
   ** @return                    <code>true</code> if passwords match,
   **                            <code>false</code> otherwise.
   */
  protected boolean match(final char[] password, final String expected) {
    // Base64-decode the expected value and take the last 8 bytes as the salt.
    final byte[] decoded = decode(expected);
    final int    length  = decoded.length - this.hashSize;
    final byte[] salt    = new byte[length];
    final byte[] digest  = new byte[this.hashSize];

    System.arraycopy(decoded, 0,             digest, 0, this.hashSize);
    System.arraycopy(decoded, this.hashSize, salt,   0, length);

    // create the hash from the key spec.
    byte[] salted = hash(password, salt);
    // compare both hashes in "length-constant" time
    return equals(digest, salted);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hash
  /**
   ** Returns a salted and hashed password using the provided hash.
   ** <p>
   ** This algorithm follows additional references of the standard practice in
   ** password hashing directly from
   ** <a href="https://www.owasp.org/index.php/Hashing_Java">OWASP</a>.
   ** <br>
   ** <b>Note</b>: Side effect: the password is destroyed (the char[] is filled
   ** with zeros)
   **
   ** @param  password           the character sequence (aka password) to be
   **                            hashed.
   **                            Allowed object array of <code>char</code>s.
   ** @param  salt               a 16 bytes salt, ideally obtained with the
   **                            {@link nextSalt} method.
   **                            Allowed object array of <code>byte</code>s.
   **
   ** @return                    the hashed password with a pinch of salt.
   **                            Possible object array of <code>char</code>s.
   */
  private byte[] hash(final char[] password, final byte[] salt) {
    final PBEKeySpec spec = new PBEKeySpec(password, salt, HASH_ITERATION, this.hashSize * 8);
    // we don't need the char[] passwords anymore -> clean!
    //clear(password);
    try {
     // create the hash from the key spec.
     return this.digester.generateSecret(spec).getEncoded();
    }
    catch (InvalidKeySpecException e) {
      throw new AssertionError("Invalid key spec.", e);
    }
  }
}