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

    System      :   Oracle Directory Service Foundation Library
    Subsystem   :   Common Shared Security Facilities

    File        :   PBECredential.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    PBECredential.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.0.0            2011-05-14  DSteding    First release version
*/

package oracle.iam.directory.foundation.security;

import java.util.Arrays;
import java.util.Base64;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;

////////////////////////////////////////////////////////////////////////////////
// class PBECredential
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A utility class to hash passwords and check passwords vs hashed values.
 ** <p>
 ** It uses a combination of hashing and unique salt. The algorithm used is
 ** PBKDF2WithHmacSHA1 which, although not the best for hashing password (vs.
 ** bcrypt) is still considered robust and
 ** <a href="https://security.stackexchange.com/a/6415/12614"> recommended by NIST</a>.
 ** <p>
 ** The hashed value has 160 bits.
 ** <p>
 ** Using the secure random option, this class complies with the statistical
 ** random number generator tests specified in FIPS 140-2, Security
 ** Requirements for Cryptographic Modules, secition 4.9.1.
 ** <p>
 ** Comparing the hashes in "length-constant" time ensures that an attacker
 ** cannot extract the hash of a password in an on-line system using a timing
 ** attack, then crack it off-line.
 ** <p>
 ** The standard way to check if two sequences of bytes (strings) are the same
 ** is to compare the first byte, then the second, then the third, and so on. As
 ** soon as you find a byte that isn't the same for both strings, you know they
 ** are different and can return a negative response immediately. If you make it
 ** through both strings without finding any bytes that differ, you know the
 ** strings are the same and can return a positive result. This means that
 ** comparing two strings can take a different amount of time depending on how
 ** much of the strings match.
 ** <p>
 ** For example, a standard comparison of the strings "xyzabc" and "abcxyz"
 ** would immediately see that the first character is different and wouldn't
 ** bother to check the rest of the string. On the other hand, when the strings
 ** "aaaaaaaaaaB" and "aaaaaaaaaaZ" are compared, the comparison algorithm scans
 ** through the block of "a" before it determines the strings are unequal. 
 ** <p>
 ** Suppose an attacker wants to break into an on-line system that rate limits
 ** authentication attempts to one attempt per second. Also suppose the attacker
 ** knows all of the parameters to the password hash (salt, hash type, etc),
 ** except for the hash and (obviously) the password. If the attacker can get a
 ** precise measurement of how long it takes the on-line system to compare the
 ** hash of the real password with the hash of a password the attacker provides,
 ** he can use the timing attack to extract part of the hash and crack it using
 ** an offline attack, bypassing the system's rate limiting. 
 ** <p>
 ** First, the attacker finds 256 strings whose hashes begin with every possible
 ** byte. He sends each string to the on-line system, recording the amount of
 ** time it takes the system to respond. The string that takes the longest will
 ** be the one whose hash's first byte matches the real hash's first byte. The
 ** attacker now knows the first byte, and can continue the attack in a similar
 ** manner on the second byte, then the third, and so on. Once the attacker
 ** knows enough of the hash, he can use his own hardware to crack it, without
 ** being rate limited by the system. 
 ** <p>
 ** It might seem like it would be impossible to run a timing attack over a
 ** network. However, it has been done, and has been
 ** <a href="https://crypto.stanford.edu/~dabo/papers/ssl-timing.pdf">shown to be practical</a>.
 ** <p>
 ** The hash format is five fields separated by the colon (':') character.
 ** <pre>
 **   algorithm:iteration:hashSize:salt:hash
 ** </pre>
 ** Where:
 ** <ul>
 **   <li><code>algorithm</code> is the name of the cryptographic hash function
 **       ("{SHA1}").
 **   <li><code>iteration</code> is the number of PBKDF2 iterations ("64000").
 **   <li><code>hashSize</code> is the length, in bytes, of the hash field
 **       (after decoding).
 **   <li><code>salt</code> is the salt, base64 encoded.
 **   <li><code>hash</code> is the PBKDF2 output, base64 encoded. It must encode
 **       <code>hashSize</code> bytes.
 ** </ul>
 ** Here are some example hashes (all of the password "Welcome1"):
 ** <pre>
 **   {SHA1}:64000:18:eFV7gEKCb8vaMaqqvaIOXVYdClVEARud:KUFq/KhLCV12sF5ou2Nz5xaV
 **   {SHA1}:64000:18:Qy0YmT6aDHCsmx2vEwa9V7eWq2FsGaGr:9sUwaKs1Gq2CO6GKDq1KdU9S
 **   {SHA1}:64000:18:to9iSXSe+zmv3zld9GINIdJWo0cEu/3j:pPzBGeckd4vDt8OliGMg5RHV
 **   {SHA1}:64000:18:H/TnP0oaoe5FBz2PYnTYvZljzjPGhVYD:yhttSKkPjRccNY7EMGOHa+8B
 **   {SHA1}:64000:18:cCm3kqcPFxHwDyulVQLSVzZiGGO44lnZ:R7pqABcxr3GNRSMMsQHQUKnG
 ** </pre>
 ** The hash length in bytes is included to prevent an accident where the hash
 ** gets truncated. For instance, if the hash were stored in a database column
 ** that wasn't big enough, and the database was configured to truncate it, the
 ** result when the hash gets read back would be an easy-to-break hash, since
 ** the PBKDF2 output is right at the end. Therefore, the length of the hash
 ** should not be determined solely from the length of the last field; it must
 ** be compared against the stored length.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PBECredential {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of bytes of salt.
   ** <br>
   ** By default, 24 bytes, which is 192 bits. This is more than enough.
   ** <br>
   ** This constant should not be changed.
   */
  public static final int     SALT_SIZE      = 24;

  /**
   ** The default algorithm to be used for secure random number generation:.
   */
  public static final String  SALT_ALGORITHM = "SHA1PRNG";

  /**
   ** The number of PBKDF2 iterations applied for obtaining the encryption
   ** key from the specified password.
   ** <br>
   ** By default, it is 64,000. To provide greater protection of passwords, at
   ** the expense of needing more processing power to validate passwords,
   ** increase the number of iterations. The number of iterations should not be
   ** decreased.
   ** <p>
   ** The NIST recommends at least 1,000 iterations:
   ** http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
   ** iOS 4.x reportedly uses 10,000:
   ** http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
   */
  public static final int     HASH_ITERATION = 64000;

  /**
   ** The number of PBKDF2 output bytes.
   ** <br>
   ** By default, 18 bytes, which is 144 bits. While it may seem useful to
   ** increase the number of output bytes, doing so can actually give an
   ** advantage to the attacker, as it introduces unnecessary (avoidable)
   ** slowness to the PBKDF2 computation. 144 bits was chosen because it is
   ** <ol>
   **   <li>Less than SHA1's 160-bit output (to avoid unnecessary PBKDF2
   **       overhead)
   **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
   ** </ol>
   */
  public static final int     HASH_SIZE      = 18;

  /**
   ** default encryption algorithm will be PBKDF2WithHmacSHA1
   ** PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
   ** specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
   */
  public static final String  HASH_TYPE      = "{SHA1}";

  /**
   ** default encryption algorithm will be PBKDF2WithHmacSHA1
   ** PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
   ** specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
   */
  public static final String  HASH_ALGORITHM = "PBKDF2WithHmacSHA1";

  /** The ammount of sections the hash value have. */
  private static final int    SECTION        = 5;

  /** The section index depicts the hash algorithm. */
  private static final int    ALGORITHM      = 0;

  /** The section index depicts the hash iterations. */
  private static final int    ITERATION      = 1;

  /** The section index depicts the hash size. */
  private static final int     LENGTH        = 2;

  /** The section index depicts the salt. */
  private static final int     SALT          = 3;

  /** The section index depicts the hash of the password. */
  private static final int     HASH          = 4;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static SecureRandom  PRNG;

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
    // Initialization of a salt generator is costly, and so default value will be
    // applied only in initialize(), if it finally becomes necessary.
    // VERY important to use SecureRandom instead of just Random
    try {
      PRNG = SecureRandom.getInstance(SALT_ALGORITHM);
      PRNG.setSeed(System.currentTimeMillis());
    }
    catch (NoSuchAlgorithmException e) {
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBECredential</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private PBECredential() {
    // ensure inheritance
    super();
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
   **                            Allowed object array of {@link String}.
   **
   ** @return                    the hashed password with a pinch of salt.
   **                            Possible object array of {@link String}.
   */
  public static String digest(final String password) {
    return digest(password.toCharArray());
  }

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
  public static String digest(final char[] password) {
    // generate a long random salt using a CSPRNG.
    final byte[] salt = new byte[SALT_SIZE];
    PRNG.nextBytes(salt);

    // hash the password by prepend the salt to the given password and hash it
    // using the same hash function.
    final byte[] hash = hash(password, salt, HASH_ITERATION, HASH_SIZE);
    // format: algorithm:iteration:length:salt:hash
    return HASH_TYPE + ":" + HASH_ITERATION + ":" + hash.length + ":" + encode(salt) + ":" + encode(hash);
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
  public static boolean match(final String password, final String expected) {
    return match(password.toCharArray(), expected);
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
  public static boolean match(final char[] password, final String expected) {
    // decode the hash into its parameters
    final String[] section = expected.split(":");
    if (section.length != SECTION) {
      throw new AssertionError("Fields are missing from the password hash.");
    }
    // currently, Java only supports SHA1.
    if (!section[ALGORITHM].equals(HASH_TYPE)) {
      throw new AssertionError("Unsupported hash type.");
    }
    int iterations = 0;
    try {
      iterations = Integer.parseInt(section[ITERATION]);
    }
    catch (NumberFormatException e) {
      throw new AssertionError("Could not parse the iteration count as an integer.", e);
    }
    if (iterations < 1) {
      throw new AssertionError("Invalid number of iterations. Must be >= 1.");
    }

    byte[] salt = null;
    try {
      salt = decode(section[SALT]);
    }
    catch (IllegalArgumentException e) {
      throw new AssertionError("Base64 decoding of salt failed.", e);
    }
    byte[] hash = null;
    try {
      hash = decode(section[HASH]);
    }
    catch (IllegalArgumentException e) {
      throw new AssertionError("Base64 decoding of hash failed.", e);
    }
    int length = 0;
    try {
      length = Integer.parseInt(section[LENGTH]);
    }
    catch (NumberFormatException e) {
      throw new AssertionError("Could not parse the hash size as an integer.", e);
    }
    if (length != hash.length) {
      throw new AssertionError("Hash length doesn't match stored hash length.");
    }
    // compute the hash of the provided password, using the same salt, iteration
    // count, and hash length
    final byte[] test = hash(password, salt, iterations, hash.length);
    // compare the hashes in constant time
    // the password is correct if both hashes match
    return equals(hash, test);
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
  private static byte[] hash(final char[] password, final byte[] salt, final int iterations, final int bytes) {
    final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
    // we don't need the char[] passwords anymore -> clean!
   clear(password);
   try {
      SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_ALGORITHM);
      return skf.generateSecret(spec).getEncoded();
    }
    catch (NoSuchAlgorithmException e) {
      throw new AssertionError("Hash algorithm not supported.", e);
   }
    catch (InvalidKeySpecException e) {
      throw new AssertionError("Invalid key spec.", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Zero-out an password char array.
   **
   ** @param  password           the char array to cleanup.
   */
  private static void clear(final char[] password) {
    if (password != null) {
      synchronized (password) {
        Arrays.fill(password, Character.MIN_VALUE);
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Comparing the hashes in "length-constant" time ensures that an attacker
   ** cannot extract the hash of a password in an on-line system using a
   ** timing attack, then crack it off-line.
   ** <p>
   ** The code uses the XOR "^" operator to compare integers for equality,
   ** instead of the "==" operator. The reason why is explained below. The
   ** result of XORing two integers will be zero if and only if they are exactly
   ** the same. This is because 0 XOR 0 = 0, 1 XOR 1 = 0, 0 XOR 1 = 1,
   ** 1 XOR 0 = 1. If we apply that to all the bits in both integers, the result
   ** will be zero only if all the bits matched. 
   ** <p>
   ** The reason we need to use XOR instead of the "==" operator to compare
   ** integers is that "==" is usually translated/compiled/interpreted as a
   ** branch.
   ** <p>
   ** The branching makes the code execute in a different amount of time
   ** depending on the equality of the integers and the CPU's internal branch
   ** prediction state. 
   **
   ** @param  lhs                the left-hand-side hash to compare.
   ** @param  rhs                the right-hand-side hash to compare.
   **
   ** @return                     <code>true</code> if the hashes are equal;
   **                             otherwise <code>false</code>
   */
  private static boolean equals(byte[] a, byte[] b) {
    int diff = a.length ^ b.length;
    for (int i = 0; i < a.length && i < b.length; i++)
      diff |= a[i] ^ b[i];
    return diff == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decodes a Base64 encoded String into a newly-allocated byte array using
   ** the {@link Base64} encoding scheme.
   ** <p>
   ** An invocation of this method has exactly the same effect as invoking
   ** {@code decode(src.getBytes(StandardCharsets.ISO_8859_1))}
   **
   ** @param  source             the string to decode.
   **
   ** @return                    a newly-allocated byte array containing the
   **                            decoded bytes.
   **
   ** @throws IllegalArgumentException if <code>source</code> is not in valid
   **                                  Base64 scheme.
   */
  private static byte[] decode(final String source)
    throws IllegalArgumentException {
 
    return Base64.getDecoder().decode(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encodes the specified byte array into a String using the {@link Base64}
   ** encoding scheme.
   ** <p>
   ** This method first encodes all input bytes into a base64 encoded byte array
   ** and then constructs a new String by using the encoded byte array and the
   ** {@link java.nio.charset.StandardCharsets#ISO_8859_1 ISO-8859-1} charset.
   ** <p>
   ** In other words, an invocation of this method has exactly the same effect
   ** as invoking {@code new String(encode(source), StandardCharsets.ISO_8859_1)}.
   **
   ** @param  source             the byte array to encode.
   **
   ** @return                     a String containing the resulting Base64
   **                             encoded characters.
   */
  private static String encode(final byte[] array) {
    return Base64.getEncoder().encodeToString(array);
  }
}