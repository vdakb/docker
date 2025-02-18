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

    File        :   SaltedSHA256Digester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    SaltedSHA256Digester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.0.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

////////////////////////////////////////////////////////////////////////////////
// class SaltedSHA256Digester
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This class defines a Directory Server password storage scheme based on the
 ** SHA-256 algorithm defined in FIPS 180-1.
 ** <p>
 ** This is a one-way digest algorithm so there is no way to retrieve the
 ** original clear-text version of the password from the hashed value (although
 ** this means that it is not suitable for things that need the clear-text
 ** password like DIGEST-MD5).
 ** <p>
 ** The values that it generates are also salted, which protects against
 ** dictionary attacks. It does this by generating a 64-bit random salt which is
 ** appended to the clear-text value.
 ** <p>
 ** A SHA-256 hash is then generated based on this, the salt is appended to the
 ** hash, and then the entire value is base64-encoded.
 ** Here are some example hashes (all of the password "Welcome1"):
 ** <pre>
 **   2cdhPPUrQB5JzvUSVKN7BdvqWjJ48Z7DVx1E89u63gA9TX6a/mI9sw==
 **   6fEuWfYsf0OQMH/wSJdC7R9EAlFelVHgJ6pAmX/0rZnGnxpw3lRv5g==
 **   lZws7eetRhoCiFxRwNJzc597CpUeIJhNZw7bm8P0eBRYH6ZunHcYCA==
 **   0yshHzNHd6bS0qEHeSPTWTNe+NQPs/97591iaLLvq4K1c6xXa0LJ9A==
 **   8COgAubUYedFn+e/enGQcVbSTrpSD/gBOVMigrp0AbjePh6NioMgmw==
 ** </pre>
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
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SaltedSHA256Digester extends SaltedSHADigester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of SHA-256 output bytes.
   ** <br>
   ** By default, 64 bytes, which is 256 bits. While it may seem useful to
   ** increase the number of output bytes, doing so can actually give an
   ** advantage to the attacker, as it introduces unnecessary (avoidable)
   ** slowness to the PBKDF2 computation. 512 bits was chosen because it is
   ** <ol>
   **   <li>Default SHA256's 256-bit output (to avoid unnecessary SHA-256
   **       overhead)
   **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
   ** </ol>
   */
  public static final int                   SIZE      = 32;

  /** default digester algorithm tag */
  public static final String                TAG       = "{SSHA256}";

  /**
   ** default encryption algorithm will be SHA-512 as the hashing algorithm.
   ** <br>
   ** Note that the NIST specifically names SHA-1 as an acceptable hashing
   ** algorithm for PBKDF2
   */
  public static final String                ALGORITHM = "SHA-256";

  private static ThreadLocal<MessageDigest> DIGEST    =
    new ThreadLocal<MessageDigest>() {
      protected MessageDigest initialValue() {
        try {
          return MessageDigest.getInstance(ALGORITHM);
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
  private static SaltedSHA256Digester       instance       = new SaltedSHA256Digester();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SaltedSHA256Digester</code> handler that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private SaltedSHA256Digester() {
    // ensure inheritance
    super(TAG, SIZE, DIGEST.get());
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
  public static SaltedSHA256Digester instance() {
    return instance;
  }
}