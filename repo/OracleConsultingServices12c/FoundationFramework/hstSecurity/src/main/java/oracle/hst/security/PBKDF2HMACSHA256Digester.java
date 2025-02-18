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

    File        :   PBKDF2HMACSHA1Digester.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PBKDF2HMACSHA1Digester.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    1.0.0.0            2011-05-14  DSteding    First release version
*/

package oracle.hst.security;

import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;

public class PBKDF2HMACSHA256Digester extends PBKDF2HMACDigester {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The number of PBKDF2 output bytes.
   ** <br>
   ** By default, 64 bytes, which is 256 bits. While it may seem useful to
   ** increase the number of output bytes, doing so can actually give an
   ** advantage to the attacker, as it introduces unnecessary (avoidable)
   ** slowness to the PBKDF2 computation. 256 bits was chosen because it is
   ** <ol>
   **   <li>Default SHA256's 256-bit output (to avoid unnecessary PBKDF2
   **       overhead)
   **   <li>A multiple of 6 bits, so that the base64 encoding is optimal.
   ** </ol>
   */
  public static final int                      SIZE      = 32;

  /** default digester algorithm tag */
  public static final String                   TAG       = "{PBKDF2-HMAC-SHA256}";

  /**
   ** default encryption algorithm will be PBKDF2WithHmacSHA1
   ** PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
   ** specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
   */
  public static final String                   ALGORITHM = "PBKDF2WithHmacSHA256";

  private static ThreadLocal<SecretKeyFactory> DIGEST    =
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

  /**
   ** the one and only instance of the <code>SaltedSHA1Digester</code>
   ** <p>
   ** Singleton Pattern
   */
  private static PBKDF2HMACSHA256Digester   instance     = new PBKDF2HMACSHA256Digester();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PBKDF2HMACSHA256Digester</code> handler that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private PBKDF2HMACSHA256Digester() {
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
  public static PBKDF2HMACSHA256Digester instance() {
    return instance;
  }
}