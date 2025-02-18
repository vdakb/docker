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

    File        :   Digester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Digester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.1.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.util.Arrays;
import java.util.Base64;

////////////////////////////////////////////////////////////////////////////////
// class Digester
// ~~~~~ ~~~~~~~~
/**
 ** Common interface for all util classes aimed at password hashing.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
class Digester {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Digester</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Digester() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Zero-out an password char array.
   **
   ** @param  password           the char array to cleanup.
   */
  protected static void clear(final char[] password) {
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
   ** @return                    <code>true</code> if the hashes are equal;
   **                            otherwise <code>false</code>
   */
  protected static boolean equals(byte[] lhs, byte[] rhs) {
    int diff = lhs.length ^ rhs.length;
    for (int i = 0; i < lhs.length && i < rhs.length; i++)
      diff |= lhs[i] ^ rhs[i];
    return diff == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decodes a Base64 encoded String into a newly-allocated byte array using
   ** the {@link Base64} encoding scheme.
   ** <p>
   ** An invocation of this method has exactly the same effect as invoking
   ** <code>decode(src.getBytes(StandardCharsets.ISO_8859_1))</code>.
   **
   ** @param  source             the string to decode.
   **
   ** @return                    a newly-allocated byte array containing the
   **                            decoded bytes.
   **
   ** @throws IllegalArgumentException if <code>source</code> is not in valid
   **                                  Base64 scheme.
   */
  protected static byte[] decode(final String source)
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
   ** as invoking
   ** <code>new String(encode(source), StandardCharsets.ISO_8859_1)</code>.
   **
   ** @param  source             the byte array to encode.
   **
   ** @return                     a String containing the resulting Base64
   **                             encoded characters.
   */
  protected static String encode(final byte[] source) {
    return Base64.getEncoder().encodeToString(source);
  }
}