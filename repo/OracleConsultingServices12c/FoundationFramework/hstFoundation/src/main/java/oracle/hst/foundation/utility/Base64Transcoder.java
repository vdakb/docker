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

    File        :   Base64Transcoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Base64Transcoder.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

////////////////////////////////////////////////////////////////////////////////
// final class Base64Transcoder
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Class encodes the bytes written to the OutPutStream to a Base64 encoded
 ** string and decodes a Base64 encoded string back into the original byte
 ** representation that can be read as byte array.
 ** <br>
 ** The encoded string can be retrieved by as a whole by the toString() method
 ** or splitted into lines of 72 characters by the toStringArray() method.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Base64Transcoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base64Transcoder</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Base64Transcoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode given string into a encoded character array.
   **
   ** @param  string             the string to be encoded.
   **
   ** @return                    Base64 encoded characters as an array.
   */
  public static byte[] encode(final String string) {
    return encode(string.getBytes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode given byte array into a encoded character array.
   **
   ** @param  bytes              the byte array to be encoded.
   **
   ** @return                    Base64 encoded characters as an array.
   */
  public static byte[] encode(final byte[] bytes) {
    return Base64Encoder.encode(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode given string into a decoded byte array.
   **
   ** @param  string             the Base64 String to be decoded.
   **
   ** @return                    All decoded octets as byte array.
   */
  public static byte[] decode(final String string) {
    return decode(string.getBytes());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode given string into a decoded byte array.
   **
   ** @param  bytes              the byte array to be decoded.
   **
   ** @return                    All decoded octets as byte array.
   */
  public static byte[] decode(final byte[] bytes) {
    return Base64Decoder.decode(bytes);
  }
}
