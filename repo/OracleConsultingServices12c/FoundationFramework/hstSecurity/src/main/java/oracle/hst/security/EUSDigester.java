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

    File        :   EUSDigester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EUSDigester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.0.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;

import javax.crypto.spec.PBEKeySpec;

////////////////////////////////////////////////////////////////////////////////
// class EUSDigester
// ~~~~~ ~~~~~~~~~~~
/**
 ** A utility class to hash passwords and check passwords versus hashed values.
 ** <p>
 ** It uses a combination of hashing and unique salt. The algorithm used is
 ** PBKDF2WithHmacSHA1 which, although not the best for hashing password (vs.
 ** bcrypt) is still considered robust and
 ** <a href="https://security.stackexchange.com/a/6415/12614"> recommended by NIST</a>.
 ** <p>
 ** The hashed value has 144 bits. While it may seem useful to increase the
 ** number of output bytes, doing so can actually give an advantage to the
 ** attacker, as it introduces unnecessary (avoidable) slowness to the PBKDF2
 ** computation. 144 bits was chosen because it is
 ** <ol>
 **   <li>Less than SHA1's 160-bit output (to avoid unnecessary PBKDF2
 **       overhead)
 **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
 ** </ol>
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
 ** The hash format is two fields whithout a seperator character.
 ** Here are some example hashes (all of the password "Welcome1"):
 ** <pre>
 **   D8+YTwKT437rFtSBR7ZBbAm0MV9rd69IstTzScMstrdY5Ope5Yln7O70AnkQUuj8vBiIHpo5U5HxT7XP5Lz6hL5I0gqSKv15jaqXQat9UqM=
 **   omHB+0URUhISBI5QNZA+yaJJyfMtLBRSvzJ/f9soiZAtQ8bRgRBhvj4eTlTYjFCOCaGUWIhz3LG+YwvgGjQyyR+PNYf5BFw+BYstjitGwNk=
 **   cQjbT/HGwiQDnq5ahjjaeeT/tmBgM4xszPNVEeEQ6voj33sLKLHbdoWY6ScZ01BYy0LF9e0j37ZYIfNIAPoZTkK7Jsq+CLB0cXIL6bHX2Uw=
 **   5naM+KX4GQXWQ1Cau+giBVMhXYJ6d1jOxbHyouqqhAj0FBdGvCAGZdRDxRijoi+0XJS3ZzKIBtbYiYOgzrBTel4wNk+YACgJfjiiPtV+sRE=
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EUSDigester extends PBKDF2HMACDigester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of bytes of salt.
   ** <br>
   ** By default, 16 bytes, which is 128 bits. This is more than enough.
   ** <br>
   ** This constant should not be changed.
   */
  public static final int           SALT_SIZE      = 16;

  /** default digester algorithm tag */
  public static final String        TAG            = "{MR-SHA512}";


  /**
   ** The number of PBKDF2 iterations applied for obtaining the encryption
   ** key from the specified password.
   ** <br>
   ** By default, it is 4,096. To provide greater protection of passwords, at
   ** the expense of needing more processing power to validate passwords,
   ** increase the number of iterations. The number of iterations should not be
   ** decreased.
   ** <p>
   ** The NIST recommends at least 1,000 iterations:
   ** http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
   ** iOS 4.x reportedly uses 10,000:
   ** http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
   */
  public static final int                      HASH_ITERATION = 4096;

  /**
   ** The number of PBKDF2 output bytes.
   ** <br>
   ** By default, 64 bytes, which is 512 bits. While it may seem useful to
   ** increase the number of output bytes, doing so can actually give an
   ** advantage to the attacker, as it introduces unnecessary (avoidable)
   ** slowness to the PBKDF2 computation. 512 bits was chosen because it is
   ** <ol>
   **   <li>Less than SHA1's 160-bit output (to avoid unnecessary PBKDF2
   **       overhead)
   **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
   ** </ol>
   */
  public static final int                      HASH_SIZE = 64;

  /**
   ** default encryption algorithm will be PBKDF2WithHmacSHA512
   ** PBKDF2 with SHA-512 as the hashing algorithm. Note that the NIST
   ** specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
   */
  public static final String                   ALGORITHM = "PBKDF2WithHmacSHA512";

  private static ThreadLocal<SecretKeyFactory> SECRET    =
    new ThreadLocal<SecretKeyFactory>() {
      protected SecretKeyFactory initialValue() {
        try {
          return SecretKeyFactory.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
          // intentionally left blank
          ;
        }
        return null;
      }
    };

  private static final ThreadLocal<MessageDigest> DIGEST = new ThreadLocal<MessageDigest>() {
    protected MessageDigest initialValue() {
      try {
        return MessageDigest.getInstance("SHA-512");
      }
      catch (NoSuchAlgorithmException e) {
        // intentionally left blank
        ;
      }
      return null;
    }
  };

  /**
   ** the one and only instance of the <code>SaltedSHA1Digester</code>
   ** <p>
   ** Singleton Pattern
   */
  private static EUSDigester   instance       = new EUSDigester();

  private static byte[]        speedKey       = "AUTH_PBKDF2_SPEEDY_KEY".getBytes();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EUSDigester</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private EUSDigester() {
    // ensure inheritance
    super(TAG, HASH_SIZE, SECRET.get());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the sole instance of this credential digester.
   **
   ** @return                     the sole instance of this credential digester.
   */
  public static EUSDigester instance() {
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digest (overridden)
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
  @Override
  protected String digest(final char[] password) {
    // generate a long random salt using a CSPRNG.
    final byte[] salt = new byte[SALT_SIZE];
    PRNG.nextBytes(salt);

    // hash the password by prepend the salt to the given password and hash it
    // using the same hash function.
    return encode(hash(password, salt));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match (overridden)
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
  protected boolean match(final char[] password, final String expected) {
    final byte[] decoded = decode(expected);
    final int    length  = decoded.length - HASH_SIZE;
    final byte[] salt    = new byte[length];
    System.arraycopy(decoded, HASH_SIZE, salt, 0, length);
    return equals(decoded, hash(password, salt));
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
  private static byte[] hash(final char[] password, final byte[] salt) {
    final byte[] bytes = new byte[salt.length + speedKey.length];
    System.arraycopy(salt,     0, bytes, 0,           salt.length);
    System.arraycopy(speedKey, 0, bytes, salt.length, speedKey.length);
    final PBEKeySpec spec = new PBEKeySpec(password, bytes, HASH_ITERATION, HASH_SIZE * 8);
    try {
      final byte[] result = SECRET.get().generateSecret(spec).getEncoded();
      final byte[] sha512 = new byte[result.length + bytes.length];
      System.arraycopy(result, 0, sha512, 0,             result.length);
      System.arraycopy(bytes,  0, sha512, result.length, bytes.length);

      // create the hash from the concatenated value.
      final byte[] digest = DIGEST.get().digest(sha512);
      // append the salt to the hashed value
      final byte[] salted = new byte[digest.length + SALT_SIZE];
      System.arraycopy(digest, 0, salted, 0,             digest.length);
      System.arraycopy(bytes,  0, salted, digest.length, SALT_SIZE);
      return salted;
    }
    catch (InvalidKeySpecException e) {
      throw new AssertionError("Invalid key spec.", e);
    }
  }
}