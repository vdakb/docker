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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Algorithm.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Algorithm.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

////////////////////////////////////////////////////////////////////////////////
// enum Algorithm
// ~~~~ ~~~~~~~~~
/**
 ** Available JSON Web Algorithms (JWA) as described in RFC 7518 available for
 ** this JSON Web Token implementation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum Algorithm {
  /** HMAC using SHA-256 */
  HS256("HmacSHA256"),

  /** HMAC using SHA-384 */
  HS384("HmacSHA384"),

  /** HMAC using SHA-512 */
  HS512("HmacSHA512"),

  /** RSASSA-PKCS1-v1_5 using SHA-256 */
  RS256("SHA256withRSA"),

  /** RSASSA-PKCS1-v1_5 using SHA-384 */
  RS384("SHA384withRSA"),

  /** RSASSA-PKCS1-v1_5 using SHA-512 */
  RS512("SHA512withRSA"),

  /** No digital signature or MAC performed. */
  none("None");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String type;

  //////////////////////////////////////////////////////////////////////////////
    // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>Algorithm</code>.
   **
   ** @param  type               the type of the algorithm to represent.
   */
  Algorithm(final String type) {
    this.type = type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the value of the algorithm type property.
   **
   ** @return                    the value of the algorithm type property.
   **                            Possible object is {@link String}.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromValue
  /**
   ** Factory method to create a proper algorithm from the given string value.
   **
   ** @param  value              the string value the algorithm should be
   **                            returned for.
   **
   ** @return                    the algorithm.
   */
  public static Algorithm fromValue(final String value) {
    for (Algorithm cursor : Algorithm.values()) {
      if (cursor.type.equals(value))
        return cursor;
    }
    throw new IllegalArgumentException(value);
  }
}