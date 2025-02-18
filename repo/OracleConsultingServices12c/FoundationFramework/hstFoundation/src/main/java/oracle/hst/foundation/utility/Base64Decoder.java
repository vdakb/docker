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

    File        :   Base64Decoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Base64Decoder.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.io.ByteArrayOutputStream;

////////////////////////////////////////////////////////////////////////////////
// final class Base64Decoder
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Class decodes a Base64 encoded string back into the original byte
 ** representation that can be read as byte array.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Base64Decoder {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Mask buffer to or with first sextet. */
  private static final int    SEXTET_1_MASK = 0x03FFFF;

  /** Mask buffer to or with second sextet. */
  private static final int    SEXTET_2_MASK = 0xFC0FFF;

  /** Mask buffer to or with third sextet. */
  private static final int    SEXTET_3_MASK = 0xFFF03F;

  /** Mask buffer to or with forth sextet. */
  private static final int    SEXTET_4_MASK = 0xFFFFC0;

  /** Number of bits to shift for one sextet. */
 private static final int     SHIFT_1_SEXTET = 6;

  /** Number of bits to shift for two sextet. */
  private static final int    SHIFT_2_SEXTET = 12;

  /** Number of bits to shift for three sextet. */
  private static final int    SHIFT_3_SEXTET = 18;

  /** Second sextets in buffer. */
  private static final int    SEXTET_2       = 2;

  /** Third sextets in buffer. */
  private static final int    SEXTET_3       = 3;

  /** Forth sextets in buffer. */
  private static final int    SEXTET_4       = 4;

  /** Mask an octet. */
  private static final int    OCTET_MASK     = 0xFF;

  /** Number of bits to shift for one octet. */
  private static final int    SHIFT_1_OCTET  = 8;

  /** Number of bits to shift for two octet. */
  private static final int    SHIFT_2_OCTET  = 16;

  /** White space character (out of range 0 - 63). */
  private static final byte   SPC            = 127;

  /** Padding character (out of range 0 - 63). */
  private static final byte   PAD            = 64;

  /** Array to translate base64 characters into sextet byte values. */
  private static final byte[] MAP            = {
    SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 00-07
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 08-0F
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 10-17
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 18-1F
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 20-27
  , SPC, SPC, SPC,  62, SPC, SPC, SPC,  63 // 28-2F '   +   /'
  ,  52,  53,  54,  55,  56,  57,  58,  59 // 30-37 '01234567'
  ,  60,  61, SPC, SPC, SPC, PAD, SPC, SPC // 38-3F '89   =  '
  , SPC,   0,   1,   2,   3,   4,   5,   6 // 40-47 ' ABCDEFG'
  ,   7,   8,   9,  10,  11,  12,  13,  14 // 48-4F 'HIJKLMNO'
  ,  15,  16,  17,  18,  19,  20,  21,  22 // 50-57 'PQRSTUVW'
  ,  23,  24,  25, SPC, SPC, SPC, SPC, SPC // 58-5F 'XYZ     '
  , SPC,  26,  27,  28,  29,  30,  31,  32 // 60-67 ' abcdefg'
  ,  33,  34,  35,  36,  37,  38,  39,  40 // 68-6F 'hijklmno'
  ,  41,  42,  43,  44,  45,  46,  47,  48 // 70-77 'pqrstuvw'
  ,  49,  50,  51, SPC, SPC, SPC, SPC, SPC // 78-7F 'xyz     '
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 80-87
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 88-8F
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 90-97
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // 98-9F
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // A0-A7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // A8-AF
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // B0-B7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // B8-BF
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // C0-C7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // C8-CF
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // D0-D7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // D8-DF
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // E0-E7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // E8-EF
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // F0-F7
  , SPC, SPC, SPC, SPC, SPC, SPC, SPC, SPC // F8-FF
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** 24-bit buffer to translate 4 sextets into 3 octets. */
  private int                   buffer     = 0;

  /** Number of octets in buffer. */
  private int                   sextets    = 0;

  /** Stream buffer for decoded octets waiting to be read. */
  private ByteArrayOutputStream stream     = new ByteArrayOutputStream();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base64Decoder</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Base64Decoder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Base64Decoder</code> that translates every base64
   ** character from given string into a sextet byte value by using above
   ** translation array. The sextets are then shiftet into an buffer until the
   ** buffer contains 4 sextets which are then decoded into 3 octets.
   ** <br>
   ** The translate and decode process is continued until all characters of
   ** given string are evaluated. If there are remaing sextets in the buffer
   ** they also will be converted into octets at the end. All the converted
   ** octets are added to the list for later read.
   **
   ** @param  bytes              the byte array to be encoded.
   **                            <br>
   **                            The decoded string can be retrieved by as a
   **                            whole by the toString() method or splitted into
   **                            lines of 72 characters by the toStringArray()
   **                            method.
   */
  public Base64Decoder(final byte[] bytes) {
    // ensure inheritance
    super();

    // initialize instance
    translate(bytes);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    final Base64Decoder dec = new Base64Decoder();
    dec.translate(bytes);
    return dec.byteArray();
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
  // Method:   translate
  /**
   ** Translate every base64 character from given string into a sextet byte
   ** value by using above translation array. The sextets are then shiftet into
   ** an buffer until the buffer contains 4 sextets which are then decoded into
   ** 3 octets.
   ** <br>
   ** The translate and decode process is continued until all characters of
   ** given string are evaluated. If there are remaing sextets in the buffer
   ** they also will be converted into octets at the end. All the converted
   ** octets are added to the list for later read.
   **
   ** @param  bytes              the byte array to be decoded.
   */
  public void translate(final byte[] bytes) {
    int len = bytes.length;
    int index = 0;
    int data = MAP[bytes[index]];
    while ((index < len) && (data != PAD)) {
      if (data != SPC) {
        if (this.sextets == 0)
          this.buffer = (this.buffer & SEXTET_1_MASK) | (data << SHIFT_3_SEXTET);
        else if (this.sextets == 1)
          this.buffer = (this.buffer & SEXTET_2_MASK) | (data << SHIFT_2_SEXTET);
        else if (this.sextets == 2)
          this.buffer = (this.buffer & SEXTET_3_MASK) | (data << SHIFT_1_SEXTET);
        else
          this.buffer = (this.buffer & SEXTET_4_MASK) | data;

        if ((++this.sextets) == SEXTET_4)
          decode();
      }

      if (++index < len)
        data = MAP[bytes[index]];
    }

    if (this.sextets > 0)
      decodeWithPadding();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteArray
  /**
   ** Returns all decoded octets as byte array.
   **
   ** @return                    all decoded octets as byte array.
   */
  public byte[] byteArray() {
    return this.stream.toByteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode 3 octets from buffer and add them to list of octets to read.
   */
  private void decode() {
    // octet 1
    this.stream.write((this.buffer >> SHIFT_2_OCTET) & OCTET_MASK);
    // octet 2
    this.stream.write((this.buffer >> SHIFT_1_OCTET) & OCTET_MASK);
    // octet 3
    this.stream.write(this.buffer & OCTET_MASK);
    this.buffer = 0;
    this.sextets = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeWithPadding
  /**
   ** Decode the remaining octets from buffer and add them to list of octets to
   ** read.
   */
  private void decodeWithPadding() {
    // octet 1
    if (this.sextets >= SEXTET_2)
      this.stream.write((this.buffer >> SHIFT_2_OCTET) & OCTET_MASK);
    // octet 2
    if (this.sextets >= SEXTET_3)
      this.stream.write((this.buffer >> SHIFT_1_OCTET) & OCTET_MASK);
    // octet 3
    if (this.sextets >= SEXTET_4)
      this.stream.write(this.buffer & OCTET_MASK);

    this.buffer = 0;
    this.sextets = 0;
  }
}
