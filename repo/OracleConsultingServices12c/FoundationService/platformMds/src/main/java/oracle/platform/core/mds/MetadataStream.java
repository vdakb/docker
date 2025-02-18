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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Metadata Store Library

    File        :   Metadata.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Metadata.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.platform.core.mds;

import java.util.Objects;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

////////////////////////////////////////////////////////////////////////////////
// class MetadataStream
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>MetadataStream</code> implements the base functionality of an
 ** Oracle Metadata Service Document Stream.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class MetadataStream extends InputStream {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final Reader  reader;
  final String  encoding;

  char[]        chars;
  int           length;
  char[]        surrogate = new char[2];
  byte[]        bytes;
  int           offset;
  boolean       eof       = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MetadataStream</code> that fetches a metadata document
   ** through the specified {@link Reader} and honor the specified
   ** <code>encoding</code>.
   **
   ** @param  reader             the {@link Reader} to the metadata document.
   **                            be used as the template for parsing.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   ** @param  encoding           the character encoding for a byte stream.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if <code>reader</code> or
   **                              <code>encoding</code> is <code>null</code>.
   */
  public MetadataStream(final Reader reader, final String encoding)
    throws RuntimeException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (Charset.forName(Objects.requireNonNull(encoding)) == null)
      throw new RuntimeException("No Charset for '" + encoding + '\'');

    // initialize instance attributes
    this.reader   = reader;
    this.encoding = encoding;
    this.length   = 8192;
    this.chars    = new char[this.length];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (InputStream)
  /**
   ** Reads the next byte of data from the input stream.
   ** <br>
   ** The value byte is returned as an <code>int</code> in the range
   ** <code>0</code> to <code>255</code>. If no byte is available because the
   ** end of the stream has been reached, the value <code>-1</code> is returned.
   ** This method blocks until input data is available, the end of the stream is
   ** detected, or an exception is thrown.
   **
   ** @return                    the next byte of data, or <code>-1</code> if
   **                            the end of the stream is reached.
   **
   ** @throws IOException        if an I/O error occurs.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int read()
    throws IOException {

    byte[] buf = new byte[1];
    int    n   = read(buf, 0, 1);
    if (n == -1) {
      return -1;
    }
    return buf[0] & 0xFF;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (overridden)
  /**
   ** Reads up to <code>length</code> bytes of data from the input stream into
   ** an array of bytes. An attempt is made to read as many as
   ** <code>length</code> bytes, but a smaller number may be read.
   ** <br>
   ** The number of bytes actually read is returned as an integer.
   ** <p>
   ** This method blocks until input data is available, end of file is detected,
   ** or an exception is thrown.
   ** <p>
   ** If <code>length</code> is zero, then no bytes are read and <code>0</code>
   ** is returned; otherwise, there is an attempt to read at least one byte. If
   ** no byte is available because the stream is at end of file, the value
   ** <code>-1</code> is returned; otherwise, at least one byte is read and
   ** stored into <code>buffer</code>.
   ** <p>
   ** The first byte read is stored into element <code>buffer[offset]</code>,
   ** the next one into <code>buffer[offset + 1]</code>, and so on. The number
   ** of bytes read is, at most, equal to <code>length</code>. Let <i>k</i> be
   ** the number of bytes actually read; these bytes will be stored in elements
   ** <code>buffer[offset]</code> through <code>buffer[offset + </code><i>k</i><code>-1]</code>,
   ** leaving elements <code>buffer[offset + </code><i>k</i><code>]</code>
   ** through <code>buffer[offset + length - 1]</code> unaffected.
   ** <p>
   ** In every case, elements <code>buffer[0]</code> through
   ** <code>buffer[offset]</code> and elements
   ** <code>buffer[offset + length]</code> through
   ** <code>buffer[buffer.length - 1]</code> are unaffected.
   **
   ** @param  buffer             the buffer into which the data is read.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   ** @param  offset             the start offset in array <code>b</code> at
   **                            which the data is written.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  length             the maximum number of bytes to read.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the total number of bytes read into the buffer,
   **                            or <code>-1</code> if there is no more data
   **                            because the end of the stream has been reached.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws IOException               if the first byte cannot be read for any
   **                                   reason other than end of file, or if the
   **                                   input stream has been closed, or if some
   **                                   other I/O error occurs.
   ** @throws NullPointerException      if <code>b</code> is <code>null</code>.
   ** @throws IndexOutOfBoundsException if <code>off</code> is negative,
   **                                   <code>len</code> is negative, or
   **                                   <code>len</code> is greater than
   **                                   <code>b.length - off</code>.
   */
  @Override
  public int read(final byte[] buffer, int offset, int length)
    throws IOException {

    // prevent bogus input
    if (buffer == null)
      throw new NullPointerException();

    // prevent bogus input
    if ((offset < 0) || (offset > buffer.length) || (length < 0) || (offset + length > buffer.length) || (offset + length < 0))
      throw new IndexOutOfBoundsException();

    // prevent bogus input
    if (length == 0)
      return 0;

    // prevent bogus state
    if (this.eof)
      return -1;

    int n = -1;
    if (this.bytes != null) {
      n = this.bytes.length - this.offset;
      if (n >= length) {
        System.arraycopy(this.bytes, this.offset, buffer, offset, length);
        if (n == length) {
          this.bytes = null;
        }
        this.offset += length;
        return length;
      }
      System.arraycopy(this.bytes, this.offset, buffer, offset, n);
      length -= n;
      offset += n;
      this.bytes  = null;
      this.offset = 0;
    }

    if ((this.chars == null) || (this.chars.length < length)) {
      this.chars = new char[length];
    }

    this.length = this.reader.read(this.chars);
    if (this.length == -1) {
      this.eof = true;
      return n;
    }

    String surrogateStr = null;
    // determines if the first char value is a Unicode low-surrogate code unit
    // (also known as trailing-surrogate code unit)
    if (surrogateLow(this.chars[0])) {
      this.surrogate[1] = this.chars[0];
      // determines whether the surrogatePair values is a valid Unicode
      // surrogate pair
      if (surrogatePair(this.surrogate)) {
        surrogateStr = new String(this.surrogate, 0, 2);
      }
      this.surrogate = new char[2];
    }

    int isLastHighSurrogate = 0;
    // determines if the last char value is a Unicode high-surrogate code unit
    // (also known as leading-surrogate code unit)
    if (surrogateHigh(this.chars[(this.length - 1)])) {
      this.surrogate[0] = this.chars[(this.length - 1)];
      isLastHighSurrogate = 1;
    }

    String str = "";
    if (surrogateStr != null) {
      if (this.length > 1) {
        str = new String(this.chars, 1, this.length - isLastHighSurrogate);
        str = surrogateStr.concat(str);
      }
      else {
        str = surrogateStr;
      }
    }
    else {
      str = new String(this.chars, 0, this.length - isLastHighSurrogate);
    }
    this.bytes = str.getBytes(this.encoding);
    this.offset = 0;

    if (this.bytes.length <= length) {
      int byteCount = this.bytes.length;
      System.arraycopy(this.bytes, 0, buffer, offset, byteCount);
      this.bytes = null;
      if (n != -1) {
        byteCount += n;
      }
      return byteCount;
    }

    System.arraycopy(this.bytes, 0, buffer, offset, length);
    this.offset += length;

    if (n != -1) {
      length += n;
    }
    return length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   surrogatePair
  /**
   ** Determines whether the specified <code>pair</code> of <code>char</code>
   ** values is a valid Unicode surrogate pair.
   ** <p>
   ** This method is equivalent to the expression:
   ** <pre>
   **   surrogateHigh(pair[0]) && surrogateLow(pair[1])
   ** </pre>
   **
   ** @param  c                  the character pair to be tested.
   **                            <br>
   **                            Allowed object is array of  <code>char</code>.
   **
   ** @return                    <code>true</code> if the <code>pair[0]</code>
   **                            and <code>pair[1]</code> represent a valid
   **                            surrogate pair; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean surrogatePair(final char[] pair) {
    return Character.isSurrogatePair(pair[0], pair[1]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   surrogateHigh
  /**
   ** Determines if the given char value is a Unicode high-surrogate code unit
   ** (also known as leading-surrogate code unit).
   ** <p>
   ** Such values do not represent characters by themselves but are used in the
   ** representation of supplementary characters in the UTF-16 encoding.
   **
   ** @param  c                  the character to be tested.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    <code>true</code> if <code>c</code> is between
   **                            {@link Character#MIN_HIGH_SURROGATE} and
   **                            {@link Character#MAX_HIGH_SURROGATE}.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean surrogateHigh(final char c) {
    return Character.isHighSurrogate(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   surrogateLow
  /**
   ** Determines if the given char value is a Unicode low-surrogate code unit
   ** (also known as trailing-surrogate code unit).
   ** <p>
   ** Such values do not represent characters by themselves but are used in the
   ** representation of supplementary characters in the UTF-16 encoding.
   **
   ** @param  c                  the character to be tested.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    <code>true</code> if <code>c</code> is between
   **                            {@link Character#MIN_LOW_SURROGATE} and
   **                            {@link Character#MAX_LOW_SURROGATE}.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean surrogateLow(final char c) {
    return Character.isLowSurrogate(c);
  }
}