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
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLOutputBuffer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOutputBuffer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Writer;
import java.io.IOException;
import java.io.Serializable;

import oracle.hst.foundation.SystemError;

////////////////////////////////////////////////////////////////////////////////
// class XMLOutputBuffer
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This is primarily used to replace the <code>StringBuffer</code> class, as a
 ** way for the {@link XMLFormatter} to store the start tag for an XML element.
 ** This enables the start tag of the current element to be removed without
 ** disrupting any of the other nodes within the document. Once the contents of
 ** the output buffer have been filled its contents can be emitted to the writer
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
class XMLOutputBuffer implements CharSequence
                      ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7380167038479406788")
  private static final long serialVersionUID = -6833368797398717305L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the characters that this buffer has accumulated. */
  private StringBuilder     text;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for <code>XMLOutputBuffer</code>.
   ** <p>
   ** The default  <code>XMLOutputBuffer</code> stores 16 characters before a
   ** resize is needed to append extra characters.
   */
  public XMLOutputBuffer() {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.text = new StringBuilder();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length (CharSequence)
  /**
   ** Returns the length (character count).
   **
   ** @return                    the length of the sequence of characters
   **                            currently represented by this object.
   */
  public int length() {
    return this.text.length();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   charAt (CharSequence)
  /**
   ** Returns the <code>char</code> value in this sequence at the specified
   ** index. The first <code>char</code> value is at index <code>0</code>, the
   ** next at index <code>1</code>, and so on, as in array indexing.
   ** <p>
   ** The index argument must be greater than or equal to <code>0</code>, and
   ** less than the length of this sequence.
   ** <p>
   ** If the <code>char</code> value specified by the index is a
   ** <a href="Character.html#unicode">surrogate</a>, the surrogate value is
   ** returned.
   **
   ** @param  index              the index of the desired <code>char</code>
   **                            value.
   **
   ** @return                    the <code>char</code> value at the specified
   **                            index.
   **
   ** @throws IndexOutOfBoundsException  if <code>index</code> is negative or
   **                                    greater than or equal to
   **                                    <code>length()</code>.
   */
  public char charAt(final int index) {
    return this.text.charAt(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subSequence (CharSequence)
  /**
   ** Returns a new character sequence that is a subsequence of this sequence.
   ** <p>
   ** An invocation of this method of the form
   ** <pre>
   **   sb.subSequence(begin,&nbsp;end)
   ** </pre>
   ** behaves in exactly the same way as the invocation
   ** <pre>
   **   sb.substring(begin,&nbsp;end)
   ** </pre>
   **
   ** @param  start              the start index, inclusive.
   ** @param  end                the end index, exclusive.
   ** @return                    the specified subsequence.
   **
   ** @throws IndexOutOfBoundsException if <code>start</code> or
   **                                   <code>end</code> are negative, if
   **                                   <code>end</code> is greater than
   **                                   <code>length()</code>, or if
   **                                   <code>start</code> is greater than
   **                                   <code>end</code>.
   */
  public CharSequence subSequence(final int start, final int end) {
    return this.text.subSequence(start, end);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the string representation of the <code>boolean</code> argument to
   ** this sequence.
   ** <p>
   ** The overall effect is exactly as if the argument were converted to a
   ** string by the method {@link String#valueOf(boolean)}, and the characters
   ** of that string were then {@link #append(String) appended} to this
   ** character sequence.
   **
   ** @param  b                  an <code>boolean</code> value.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final boolean b) {
    this.text.append(b);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the string representation of the <code>int</code> argument to this
   ** sequence.
   ** <p>
   ** The overall effect is exactly as if the argument were converted to a
   ** string by the method {@link String#valueOf(int)}, and the characters of
   ** that string were then {@link #append(String) appended} to this character
   ** sequence.
   **
   ** @param  i                  an <code>int</code> value.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final int i) {
    this.text.append(i);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the string representation of the <code>long</code> argument to
   ** this sequence.
   ** <p>
   ** The overall effect is exactly as if the argument were converted to a
   ** string by the method {@link String#valueOf(long)}, and the characters of
   ** that string were then {@link #append(String) appended} to this character
   ** sequence.
   **
   ** @param  l                  an <code>long</code> value.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final long l) {
    this.text.append(l);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the string representation of the <code>float</code> argument to
   ** this sequence.
   ** <p>
   ** The overall effect is exactly as if the argument were converted to a
   ** string by the method {@link String#valueOf(float)}, and the characters of
   ** that string were then {@link #append(String) appended} to this character
   ** sequence.
   **
   ** @param  f                  an <code>float</code> value.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final float f) {
    this.text.append(f);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the string representation of the <code>double</code> argument to
   ** this sequence.
   ** <p>
   ** The overall effect is exactly as if the argument were converted to a
   ** string by the method {@link String#valueOf(double)}, and the characters of
   ** that string were then {@link #append(String) appended} to this character
   ** sequence.
   **
   ** @param  d                  an <code>double</code> value.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final double d) {
    this.text.append(d);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** This will add a <code>char</code> to the end of the buffer.
   ** <p>
   ** The buffer will not overflow with repeated uses of the
   ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
   ** will allow the buffer to dynamically grow in size to accommodate more
   ** characters.
   **
   ** @param  ch                 the character to be appended to the buffer.
   */
  public XMLOutputBuffer append(final char ch) {
    this.text.append(ch);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** This will add a <code>char</code> array to the buffer.
   ** <p>
   ** The buffer will not overflow with repeated uses of the
   ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
   ** will allow the buffer to dynamically grow in size to accommodate large
   ** character arrays.
   **
   ** @param  value              the character array to be appended to this.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final char[] value) {
    this.text.append(value, 0, value.length);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** This will add a <code>char</code> array to the buffer.
   ** <p>
   ** The buffer will not overflow with repeated uses of the
   ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
   ** will allow the buffer to dynamically grow in size to accommodate large
   ** character arrays.
   **
   ** @param  value              the character array to be appended to this
   ** @param  offset             the read offset for the array to begin reading
   ** @param  len                the number of characters to append to this
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final char[] value, final int offset, final int len) {
    this.text.append(value, offset, len);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** This will add a <code>String</code> to the end of the buffer.
   ** <p>
   ** The buffer will not overflow with repeated uses of the
   ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
   ** will allow the buffer to dynamically grow in size to accommodate large
   ** string objects.
   **
   ** @param  value              the string to be appended to this output
   **                            buffer.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final String value) {
    this.text.append(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** This will add a <code>String</code> to the end of the buffer.
   ** <p>
   ** The buffer will not overflow with repeated uses of the
   ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
   ** will allow the buffer to dynamically grow in size to accommodate large
   ** string objects.
   **
   ** @param  value              the string to be appended to the output buffer.
   ** @param  offset             the offset to begin reading from the string.
   ** @param  len                the number of characters to append to this.
   */
  public void append(final String value, final int offset, final int len) {
    this.text.append(value, offset, len);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the specified <code>CharSequence</code> to this sequence.
   ** <p>
   ** The characters of the {@link CharSequence} argument are appended, in
   ** order, to this sequence, increasing the length of this sequence by the
   ** length of the argument.
   ** <p>
   ** Let <i>n</i> be the length of this character sequence just prior to
   ** execution of the <code>append</code> method. Then the character at index
   ** <i>k</i> in the new character sequence is equal to the character at index
   ** <i>k</i> in the old character sequence, if <i>k</i> is less than <i>n</i>;
   ** otherwise, it is equal to the character at index <i>k-n</i> in the
   ** argument <code>buffer</code>.
   ** <p>
   ** If <code>sequence</code> is <code>null</code>, then this method appends
   ** characters as if the <code>sequence</code> parameter was a sequence
   ** containing the four characters <code>"null"</code>.
   **
   ** @param  sequence           the sequence to append.
   */
  public XMLOutputBuffer append(final CharSequence sequence) {
    this.text.append(sequence);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends a subsequence of the specified <code>CharSequence</code> to this
   ** sequence.
   ** <p>
   ** Characters of the argument <code>sequence</code>, starting at index
   ** <code>start</code>, are appended, in order, to the contents of this
   ** sequence up to the (exclusive) index <code>end</code>. The length of this
   ** sequence is increased by the value of <code>end - start</code>.
   ** <p>
   ** Let <i>n</i> be the length of this character sequence just prior to
   ** execution of the <code>append</code> method. Then the character at index
   ** <i>k</i> in this character sequence becomes equal to the character at
   ** index <i>k</i> in this sequence, if <i>k</i> is less than <i>n</i>;
   ** otherwise, it is equal to the character at index <i>k+start-n</i> in the
   ** argument <code>sequence</code>.
   ** <p>
   ** If <code>sequence</code> is <code>null</code>, then this method appends
   ** characters as if the <code>sequence</code> parameter was a sequence
   ** containing the four characters <code>"null"</code>.
   **
   ** @param  sequence           the sequence to append.
   ** @param  start              the starting index of the subsequence to be
   **                            appended.
   ** @param  end                the end index of the subsequence to be
   **                            appended.
   **
   ** @return                    a reference to this object.
   **
   ** @throws IndexOutOfBoundsException if <code>start</code> is negative, or
   **                                   <code>start</code> is greater than
   **                                   <code>end</code> or <code>end</code> is
   **                                   greater than
   **                                   <code>sequence.length()</code>.
   */
  public XMLOutputBuffer append(final CharSequence sequence, final int start, final int end) {
    this.text.append(sequence, start, end);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   append
  /**
   ** Appends the specified {@link StringBuffer} to this sequence.
   ** <p>
   ** The characters of the {@link StringBuffer} argument are appended, in
   ** order, to this sequence, increasing the length of this sequence by the
   ** length of the argument. If <code>buffer</code> is <code>null</code>, then
   ** the four characters <code>"null"</code> are appended to this sequence.
   ** <p>
   ** Let <i>n</i> be the length of this character sequence just prior to
   ** execution of the <code>append</code> method. Then the character at index
   ** <i>k</i> in the new character sequence is equal to the character at index
   ** <i>k</i> in the old character sequence, if <i>k</i> is less than <i>n</i>;
   ** otherwise, it is equal to the character at index <i>k-n</i> in the
   ** argument <code>buffer</code>.
   **
   ** @param  buffer             the {@link StringBuffer} to append.
   **
   ** @return                    a reference to this object.
   */
  public XMLOutputBuffer append(final StringBuffer buffer) {
    this.text.append(buffer);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flush
  /**
   ** This method is used to write the contents of the buffer to the specified
   ** <code>Writer</code> object and clears the text buffer.
   ** <p>
   ** This is used when the XML element is to be committed to the resulting XML
   ** document.
   **
   ** @param  out                this is the writer to write the buffered text
   **                            to.
   **
   ** @throws XMLException       thrown if there is an I/O problem
   */
  public void flush(final Writer out)
    throws XMLException {

    write(out);
    clear();
    try {
      out.flush();
    }
    catch (IOException e) {
      throw new XMLException(SystemError.UNHANDLED, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** This will empty the <code>OutputBuffer</code> so that it does not contain
   ** any content. This is used to that when the buffer is written to a
   ** specified <code>Writer</code> object nothing is written out.
   ** <p>
   ** This allows XML elements to be removed.
   */
   public void clear(){
     this.text.setLength(0);
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** This method is used to write the contents of the buffer to the specified
   ** <code>Writer</code> object. This is used when the XML element is to be
   ** committed to the resulting XML document.
   **
   ** @param  out                this is the writer to write the buffered text
   **                            to.
   **
   ** @throws XMLException       thrown if there is an I/O problem
   */
  public void write(final Writer out)
    throws XMLException {

    try {
      out.append(this.text);
    }
    catch (IOException e) {
      throw new XMLException(SystemError.UNHANDLED, e);
    }
  }
}
