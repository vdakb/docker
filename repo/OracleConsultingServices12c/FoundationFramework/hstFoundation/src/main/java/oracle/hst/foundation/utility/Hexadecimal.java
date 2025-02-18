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

    File        :   Hexadecimal.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Hexadecimal.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

////////////////////////////////////////////////////////////////////////////////
// abstract class Hexadecimal
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Miscellaneous hexadecimal utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Hexadecimal {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final byte[] encodingTable = {
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  /* set up the decoding table. */
  private static final byte[] decodingTable = new byte[128];

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    for (int i = 0; i < encodingTable.length; i++)
      decodingTable[encodingTable[i]] = (byte)i;

    decodingTable['A'] = decodingTable['a'];
    decodingTable['B'] = decodingTable['b'];
    decodingTable['C'] = decodingTable['c'];
    decodingTable['D'] = decodingTable['d'];
    decodingTable['E'] = decodingTable['e'];
    decodingTable['F'] = decodingTable['f'];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Hexadecimal</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Hexadecimal()" and enforces use of the public method below.
   */
  private Hexadecimal() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the input data producing a hexadecimal encoded byte array.
   **
   ** @param  data               the binary data to encode.
   **
   ** @return                    a byte array containing the hexadecimal encoded
   **                            data.
   */
  public static byte[] encode(final byte[] data) {
    return encode(data, 0, data.length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the input data producing a hexadecimal encoded byte array.
   **
   ** @param  data               the binary data to encode.
   ** @param  offset             the start offset in the data.
   ** @param  length             the number of bytes to write.
   **
   ** @return                    a byte array containing the hexadecimal encoded
   **                            data.
   */
  public static byte[] encode(final byte[] data, final int offset, final int length) {
    ByteArrayOutputStream straem = new ByteArrayOutputStream();
    try {
      encode(data, offset, length, straem);
    }
    catch (IOException e) {
      throw new RuntimeException("exception encoding Hex string: " + e);
    }
    return straem.toByteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the input data writing it to the given {@link OutputStream}.
   **
   ** @param  data               the binary data to encode.
   ** @param  stream             the {@link OutputStream} to write the binary
   **                            data to.
   **
   ** @return                    the number of bytes produced.
   **
   ** @throws IOException        if an I/O error occurs. In particular, an
   **                            {@link IOException} may be thrown if the passed
   **                            {@link OutputStream} has been closed.
   */
  public static int encode(final byte[] data, final OutputStream stream)
    throws IOException {

    return encode(data, 0, data.length, stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encode
  /**
   ** Encode the input data producing a hexadecimal encoded byte array by
   ** writing <code>length</code> bytes from the specified byte array starting
   ** at offset <code>offset</code> to the {@link OutputStream}.
   ** <p>
   ** The general contract for <code>encode(data, offset, lenght, stream)</code>
   ** is that some of the bytes in the array <code>data</code> are written to
   ** the  output stream in order; element <code>data[off]</code> is the first
   ** byte written and <code>data[offset + length - 1]</code> is the last byte
   ** written by this operation.
   **
   ** @param  data               the binary data to encode.
   ** @param  offset             the start offset in the data.
   ** @param  length             the number of bytes to write.
   ** @param  stream             the {@link OutputStream} to write the encoded
   **                            data to.
   **
   ** @return                    the number of bytes produced.
   **
   ** @throws IOException        if an I/O error occurs. In particular, an
   **                            {@link IOException} may be thrown if the passed
   **                            {@link OutputStream} has been closed.
   */
  public static int encode(final byte[] data, final int offset, final int length, final OutputStream stream)
    throws IOException {

    for (int i = offset; i < (offset + length); i++) {
      final int v = data[i] & 0xff;
      stream.write(encodingTable[(v >>> 4)]);
      stream.write(encodingTable[v & 0xf]);
    }
    return length * 2;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the hexadecimal encoded input data producing a binary byte array.
   **
   ** @param  data               the hexadecimal encoded data to decode.
   **
   ** @return                    a byte array containing the decoded binary
   **                            data.
   */
  public static byte[] decode(final byte[] data) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    try {
      decode(data, 0, data.length, stream);
    }
    catch (IOException e) {
      throw new RuntimeException("exception decoding Hex string: " + e);
    }

    return stream.toByteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the hexadecimal encoded String data producing a hexadecimal decoded
   ** byte array.
   ** <p>
   ** Whitespace will be ignored.
   **
   ** @param  data               the hexadecimal encoded String data.
   **
   ** @return                    a byte array representing the decoded data.
   */
  public static byte[] decode(final String data) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    try {
      decode(data, stream);
    }
    catch (IOException e) {
      throw new RuntimeException("exception decoding Hex string: " + e);
    }

    return stream.toByteArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the binary input data producing a hexadecimal decoded byte array to
   ** the given {@link OutputStream}, whitespace characters will be ignored.
   **
   ** @param  data               the hexadecimal encoded String data.
   ** @param  stream             the {@link OutputStream} to write the decoded
   **                            data to.
   **
   ** @return                    the number of bytes produced.
   **
   ** @throws IOException        if an I/O error occurs. In particular, an
   **                            {@link IOException} may be thrown if the passed
   **                            {@link OutputStream} has been closed.
   */
  public static int decode(final String data, final OutputStream stream)
    throws IOException {

    int end = data.length();
    while (end > 0) {
      if (!CharacterUtility.isWhitespace(data.charAt(end - 1)))
        break;
      end--;
    }

    int l = 0;
    int i = 0;
    while (i < end) {
      while (i < end && CharacterUtility.isWhitespace(data.charAt(i)))
        i++;

      final byte b1 = decodingTable[data.charAt(i++)];

      while (i < end && CharacterUtility.isWhitespace(data.charAt(i)))
        i++;

      final byte b2 = decodingTable[data.charAt(i++)];

      stream.write((b1 << 4) | b2);
      l++;
    }
    return l;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Decode the input data producing a hexadecimal decoded byte array by
   ** writing <code>length</code> bytes from the specified byte array starting
   ** at offset <code>offset</code> to the {@link OutputStream}.
   ** <p>
   ** The general contract for <code>encode(data, offset, lenght, stream)</code>
   ** is that some of the bytes in the array <code>data</code> are written to
   ** the  output stream in order; element <code>data[off]</code> is the first
   ** byte written and <code>data[offset + length - 1]</code> is the last byte
   ** written by this operation.
   **
   ** @param  data               the binary data to decode.
   ** @param  offset             the start offset in the data.
   ** @param  length             the number of bytes to write.
   ** @param  stream             the {@link OutputStream} to write the decoded
   **                            data to.
   **
   ** @return                    the number of bytes produced.
   **
   ** @throws IOException        if an I/O error occurs. In particular, an
   **                            {@link IOException} may be thrown if the passed
   **                            {@link OutputStream} has been closed.
   */
  public static int decode(final byte[] data, final int offset, final int length, final OutputStream stream)
    throws IOException {

    int end = offset + length;
    while (end > offset) {
      if (!CharacterUtility.isWhitespace((char)data[end - 1]))
        break;
      end--;
    }

    int l = 0;
    int i = offset;
    while (i < end) {
      while (i < end && CharacterUtility.isWhitespace((char)data[i]))
        i++;

      final byte b1 = decodingTable[data[i++]];
      while (i < end && CharacterUtility.isWhitespace((char)data[i]))
        i++;

      final byte b2 = decodingTable[data[i++]];
      stream.write((b1 << 4) | b2);
      l++;
    }
    return l;
  }
}