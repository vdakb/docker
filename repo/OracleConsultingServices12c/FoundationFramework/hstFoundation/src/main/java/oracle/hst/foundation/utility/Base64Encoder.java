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

    File        :   Base64Encoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Base64Encoder.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.nio.CharBuffer;

import java.nio.charset.CharacterCodingException;

////////////////////////////////////////////////////////////////////////////////
// final class Base64Encoder
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Class encodes the bytes written to the OutPutStream to a Base64 encoded
 ** string.
 ** <br>
 ** The encoded string can be retrieved by as a whole by the toString() method
 ** or splitted into lines of 72 characters by the toStringArray() method.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Base64Encoder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Third octets in buffer. */
  private static final int    OCTET_3        = 3;

  /** Mask buffer to or with first octet. */
  private static final int    OCTET_1_MASK   = 0x00FFFF;

  /** Mask buffer to or with second octet. */
  private static final int    OCTET_2_MASK   = 0xFF00FF;

  /** Mask buffer to or with third octet. */
  private static final int    OCTET_3_MASK   = 0xFFFF00;

  /** Mask an octet. */
  private static final int    OCTET_MASK     = 0xFF;

  /** Number of bits to shift for one octet. */
  private static final int    SHIFT_1_OCTET  = 8;

  /** Number of bits to shift for two octet. */
  private static final int    SHIFT_2_OCTET  = 16;

  /** Mask a sextet. */
  private static final int    SEXTET_MASK    = 0x3F;

  /** Number of bits to shift for one sextet. */
  private static final int    SHIFT_1_SEXTET = 6;

  /** Number of bits to shift for two sextet. */
  private static final int    SHIFT_2_SEXTET = 12;

  /** Number of bits to shift for three sextet. */
  private static final int    SHIFT_3_SEXTET = 18;

  /** Array to convert sextet byte values into base64 characters. */
  private static final char[] MAP            = {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H' // 00-07
  , 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P' // 08-15
  , 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X' // 16-23
  , 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f' // 24-31
  , 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n' // 32-39
  , 'o', 'p', 'q', 'r', 's', 't', 'u', 'v' // 40-47
  , 'w', 'x', 'y', 'z', '0', '1', '2', '3' // 48-55
  , '4', '5', '6', '7', '8', '9', '+', '/' // 56-63
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** 24-bit buffer to translate 3 octets into 4 sextets. */
  private int                 buffer         = 0;

  /** Number of octets in buffer. */
  private int                 octets         = 0;

  /** Stream buffer for encoded characters waiting to be read. */
  private StringBuilder       stream         = new StringBuilder();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base64Encoder</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Base64Encoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base64Encoder</code> that translates all bytes of given
   ** array by appending each to octet buffer.
   ** <br>
   ** If buffer contains 3 octets its content will be encoded to 4 sextet byte
   ** values which are converted to a base64 character each. All characters are
   ** appended to a {@link StringBuffer}.
   **
   ** @param  bytes              the byte array to be encoded.
   **                            <br>
   **                            The encoded string can be retrieved by as a
   **                            whole by the toString() method or splitted into
   **                            lines of 72 characters by the toStringArray()
   **                            method.
   */
  public Base64Encoder(final byte[] bytes) {
    // ensure inheritance
    super();

    // initialize instance
    translate(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    final Base64Encoder enc = new Base64Encoder();
    enc.translate(bytes);
    return enc.byteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    return StringUtility.bytesToString(byteArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Reset Base64Encoder to its initial state.
   ** <br>
   ** Take care using this method as it throws all previously written bytes
   ** away.
   */
  public void reset() {
    this.buffer = 0;
    this.octets = 0;
    this.stream = new StringBuilder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translate
  /**
   ** Translate all bytes of given array by appending each to octet buffer.
   ** <br>
   ** If buffer contains 3 octets its content will be encoded to 4 sextet byte
   ** values which are converted to a base64 character each. All characters are
   ** appended to a {@link StringBuffer}.
   **
   ** @param  bytes              the byte array to be encoded.
   */
  public void translate(final byte[] bytes) {
    for (int i = 0; i < bytes.length; i++) {
      final byte b = bytes[i];

      if (this.octets == 0)
        this.buffer = (this.buffer & OCTET_1_MASK) | ((b & OCTET_MASK) << SHIFT_2_OCTET);
      else if (this.octets == 1)
        this.buffer = (this.buffer & OCTET_2_MASK) | ((b & OCTET_MASK) << SHIFT_1_OCTET);
      else
        this.buffer = (this.buffer & OCTET_3_MASK) | (b & OCTET_MASK);

      if ((++this.octets) == OCTET_3)
        encode();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteArray
  /**
   ** Returns Base64 encoded characters as an array.
   **
   ** @return                    Base64 encoded characters as an array.
   */
  public byte[] byteArray() {
    if (this.octets > 0)
      encodeWithPadding();

    try {
      // Base64 encoding ensures thats only ASCII characters contained in the
      // buffer hnace there isn't any need to encode the bytes to Unicode UTF-8
      return StringUtility.ASCII_ENCODER.encode(CharBuffer.wrap(this.stream)).array();
    }
    catch (CharacterCodingException e) {
      return new byte[0];
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode 4 sextets from buffer and add them to {@link StringBuilder}.
   */
  private void encode() {
    this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_3_SEXTET)]); // sextet 1
    this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_2_SEXTET)]); // sextet 2
    this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_1_SEXTET)]); // sextet 3
    this.stream.append(MAP[SEXTET_MASK & this.buffer]); // sextet 4
    this.buffer = 0;
    this.octets = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeWithPadding
  /**
   ** Encode the remaining sextets from buffer and add them to the
   ** {@link StringBuilder}.
   */
  private void encodeWithPadding() {
    // sextet 1
    this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_3_SEXTET)]);
    // sextet 2
    this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_2_SEXTET)]);
    // sextet 3
    if (this.octets <= 1)
      this.stream.append('=');
    else
      this.stream.append(MAP[SEXTET_MASK & (this.buffer >> SHIFT_1_SEXTET)]);
    // sextet 4
    if (this.octets <= 2)
      this.stream.append('=');
    else
      this.stream.append(MAP[SEXTET_MASK & this.buffer]);

    this.buffer = 0;
    this.octets = 0;
  }
}
