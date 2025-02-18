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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   NumberUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NumberUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class NumberUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous mumerical utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class NumberUtility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>NumberUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new NumberUtility()" and enforces use of the public method below.
   */
  private NumberUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEqual
  /**
   ** Compares two {@link Integer}s if they are identically.
   **
   ** @param  a                  the first {@link Integer} to be tested for
   **                            equality.
   ** @param  b                  the second {@link Integer} to be tested for
   **                            equality.
   **
   ** @return                    <code>true</code> if the {@link Integer}s are
   **                            identically; otherwise <code>false</code>.
   */
  public static boolean isEqual(final Integer a, final Integer b) {
    if (a == b)
      return true;
    else if (a == null && b == null)
      return true;
    else if ((a == null && b != null) || (a != null && b == null))
      return false;
    else
      return a.equals(b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEqual
  /**
   ** Compares two {@link Float}s if they are identically.
   **
   ** @param  a                  the first {@link Float} to be tested for
   **                            equality.
   ** @param  b                  the second {@link Float} to be tested for
   **                            equality.
   **
   ** @return                    <code>true</code> if the {@link Float}s are
   **                            identically; otherwise <code>false</code>.
   */
  public static boolean isEqual(final Float a, final Float b) {
    if (a == b)
      return true;
    else if (a == null && b == null)
      return true;
    else if ((a == null && b != null) || (a != null && b == null))
      return false;
    else
      return a.equals(b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEqual
  /**
   ** Compares two {@link Double}s if they are identically.
   **
   ** @param  a                  the first {@link Double} to be tested for
   **                            equality.
   ** @param  b                  the second {@link Double} to be tested for
   **                            equality.
   **
   ** @return                    <code>true</code> if the {@link Double}s are
   **                            identically; otherwise <code>false</code>.
   */
  public static boolean isEqual(final Double a, final Double b) {
    if (a == b)
      return true;
    else if (a == null && b == null)
      return true;
    else if ((a == null && b != null) || (a != null && b == null))
      return false;
    else
      return a.equals(b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intToBytes
  /**
   ** Converts an int to an array of bytes.
   **
   ** @param  number             the int to convert.
   **
   ** @return                    the converted byte array.
   */
  public static byte[] intToBytes(final int number) {
    return new byte[] {
      (byte)(0xff & (number >> 24))
    , (byte)(0xff & (number >> 16))
    , (byte)(0xff & (number >> 8))
    , (byte)(0xff & number)
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToInt
  /**
   ** Returns the int for an array of bytes.
   **
   ** @param  raw                the byte array to convert.
   **
   ** @return                    the int
   */
  public static int bytesToInt(final byte[] raw) {
    // prevent boguxs input
    if (raw == null || raw.length == 0)
      throw new IllegalArgumentException("Cannot convert an empty array into an int");

    int result = (0xff & raw[0]);
    for (int i = 1; i < raw.length; i++)
      result = (result << 8) | (0xff & raw[i]);

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseLong
  /**
   ** Decode the hexadecimal encoded input data producing a <code>long</code>
   ** value.
   **
   ** @param  data               the hexadecimal encoded data to decode.
   **
   ** @return                    a byte array containing the decoded binary
   **                            data.
   */
  public static long parseLong(final String data) {
    if ((data.charAt(1) == 'x') || (data.charAt(1) == 'X'))
      return Long.parseLong(data.substring(2), 16);

    return Long.parseLong(data, 16);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeInt
  /**
   ** Decode the hexadecimal encoded input data producing a <code>int</code>
   ** value.
   **
   ** @param  data               the hexadecimal encoded data to decode.
   **
   ** @return                    a byte array containing the decoded binary
   **                            data.
   */
  public static int decodeInt(final String data) {
    if ((data.charAt(1) == 'x') || (data.charAt(1) == 'X'))
      return Integer.parseInt(data.substring(2), 16);

    return Integer.parseInt(data, 16);
  }
}