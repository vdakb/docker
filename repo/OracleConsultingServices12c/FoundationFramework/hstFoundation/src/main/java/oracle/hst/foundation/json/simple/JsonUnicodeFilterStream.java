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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Simple JSON Parser

    File        :   JsonDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonDeserializer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

import java.nio.charset.Charset;

///////////////////////////////////////////////////////////////////////////////
// final class JsonUnicodeFilterStream
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A <code>JsonUnicodeFilterStream</code> contains some other input stream,
 ** which it uses as its  basic source of data, possibly transforming the data
 ** along the way or providing  additional functionality.
 ** <br>
 ** The class <code>JsonUnicodeFilterStream</code> itself simply overrides all
 ** methods of <code>FilterInputStream</code> with versions that pass all
 ** requests to the contained  input stream.
 */
final class JsonUnicodeFilterStream extends FilterInputStream {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Charset UTF_8    = Charset.forName("UTF-8");
  private static final Charset UTF_16BE = Charset.forName("UTF-16BE");
  private static final Charset UTF_16LE = Charset.forName("UTF-16LE");
  private static final Charset UTF_32LE = Charset.forName("UTF-32LE");
  private static final Charset UTF_32BE = Charset.forName("UTF-32BE");

  private static final byte   FF        = (byte)0xFF;
  private static final byte   FE        = (byte)0xFE;
  private static final byte   EF        = (byte)0xEF;
  private static final byte   BB        = (byte)0xBB;
  private static final byte   BF        = (byte)0xBF;
  private static final byte   NUL       = (byte)0x00;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final byte[]  buffer = new byte[4];
  private int           length;
  private int           cursor;
  private final Charset charset;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>JsonUnicodeFilterStream</code> by remember the argument
   ** <code>stream</code> for later use.
   **
   ** @param  stream             the underlying input stream, or
   **                            <code>null</code> if this instance is to be
   **                            created without an underlying stream.
   */
  public JsonUnicodeFilterStream(final InputStream stream) {
    // ensure inheritance
    super(stream);

    // initialize instance
    this.charset = detect();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   charset
  /**
   ** Returns the {@link Charset} this stream has detected in the underlying
   ** stream.
   **
   ** @return                    the {@link Charset} this stream has detected in
   **                            the underlying stream.
   **                            Possible object {@link Charset}.
   */
  Charset charset() {
    return this.charset;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (overridden)
  /**
   ** Reads the next byte of data from this input stream.
   ** <br>
   ** The value byte is returned as an <code>int</code> in the range
   ** <code>0</code> to <code>255</code>. If no byte is available because the
   ** end of the stream has been reached, the value <code>-1</code> is returned.
   ** This method blocks until input data is available, the end of the stream is
   ** detected, or an exception is thrown.
   ** <p>
   ** This method simply performs <code>in.read()</code> and returns the result.
   **
   ** @return                    the next byte of data, or <code>-1</code> if
   **                            the end of the stream is reached.
   **
   ** @throws  IOException      if an I/O error occurs.
   **
   ** @see      FilterInputStream#in
   */
  @Override
  public int read()
    throws IOException {

    if (this.cursor < this.length)
      return this.buffer[this.cursor++];

    return this.in.read();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (overridden)
  /**
   ** Reads up to <code>length</code> bytes of data from this input stream into
   ** an array of bytes. If <code>length</code> is not zero, the method blocks
   ** until some input is available; otherwise, no bytes are read and
   ** <code>0</code> is returned.
   ** <p>
   ** This method simply performs <code>in.read(buffer, offset, length)</code>
   ** and returns the result.
   **
   ** @param  buffer             the buffer into which the data is read.
   ** @param  offset             the start offset in the destination array
   **                            <code>buffer</code>.
   ** @param  length             the maximum number of bytes read.
   **
   ** @return                    the total number of bytes read into the buffer,
   **                            or <code>-1</code> if there is no more data
   **                            because the end of the stream has been reached.
   **
   **
   ** @throws NullPointerException      if <code>buffer</code> is
   **                                   <code>null</code>.
   ** @throws IndexOutOfBoundsException if <code>offset</code> is negative,
   **                                   <code>length</code> is negative, or
   **                                   <code>length</code> is greater than
   **                                   <code>buffer.length - offset</code>.
   ** @throws IOException               if an I/O error occurs.
   **
   ** @see    FilterInputStream#in
   */
  @Override
  public int read(final byte buffer[], final int offset, final int length)
    throws IOException {

    if (this.cursor < this.length) {
      if (length == 0)
        return 0;

      if (offset < 0 || length < 0 || length > buffer.length - offset) {
        throw new IndexOutOfBoundsException();
      }
      int min = Math.min(this.length - this.cursor, length);
      System.arraycopy(this.buffer, this.cursor, buffer, offset, min);
      this.cursor += min;
      return min;
    }
    return this.in.read(buffer, offset, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   detect
  private Charset detect() {
    fillBuf();
    if (this.length < 2) {
      throw new JsonException("Cannot auto-detect encoding, not enough chars");
    }
    else if (this.length == 4) {
      // Use BOM to detect encoding
      if (this.buffer[0] == NUL && this.buffer[1] == NUL && this.buffer[2] == FE && this.buffer[3] == FF) {
        this.cursor = 4;
        return UTF_32BE;
      }
      else if (this.buffer[0] == FF && this.buffer[1] == FE && this.buffer[2] == NUL && this.buffer[3] == NUL) {
        this.cursor = 4;
        return UTF_32LE;
      }
      else if (this.buffer[0] == FE && this.buffer[1] == FF) {
        this.cursor = 2;
        return UTF_16BE;
      }
      else if (this.buffer[0] == FF && this.buffer[1] == FE) {
        this.cursor = 2;
        return UTF_16LE;
      }
      else if (this.buffer[0] == EF && this.buffer[1] == BB && this.buffer[2] == BF) {
        this.cursor = 3;
        return UTF_8;
      }
      // No BOM, just use JSON RFC's encoding algo to auto-detect
      if (this.buffer[0] == NUL && this.buffer[1] == NUL && this.buffer[2] == NUL) {
        return UTF_32BE;
      }
      else if (this.buffer[0] == NUL && this.buffer[2] == NUL) {
        return UTF_16BE;
      }
      else if (this.buffer[1] == NUL && this.buffer[2] == NUL && this.buffer[3] == NUL) {
        return UTF_32LE;
      }
      else if (this.buffer[1] == NUL && this.buffer[3] == NUL) {
        return UTF_16LE;
      }
    }
    return UTF_8;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fill
  private void fillBuf() {
    try {
      final int b1 = this.in.read();
      if (b1 == -1)
        return;

      final int b2 = this.in.read();
      if (b2 == -1) {
        this.length    = 1;
        this.buffer[0] = (byte)b1;
        return;
      }

      final int b3 = this.in.read();
      if (b3 == -1) {
        this.length    = 2;
        this.buffer[0] = (byte)b1;
        this.buffer[1] = (byte)b2;
        return;
      }

      final int b4 = in.read();
      if (b4 == -1) {
        this.length    = 3;
        this.buffer[0] = (byte)b1;
        this.buffer[1] = (byte)b2;
        this.buffer[2] = (byte)b3;
        return;
      }
      this.length = 4;
      this.buffer[0] = (byte)b1;
      this.buffer[1] = (byte)b2;
      this.buffer[2] = (byte)b3;
      this.buffer[3] = (byte)b4;
    }
    catch (IOException ioe) {
      throw new JsonException("I/O error while auto-detecting the encoding of stream", ioe);
    }
  }
}