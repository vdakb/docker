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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   XMLFormatter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLFormatter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.74  2018-05-15  DSteding    First release version
*/

package oracle.jdeveloper.workspace.iam.xml;

import java.io.Writer;
import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedWriter;

import java.nio.charset.CharsetEncoder;

import oracle.ide.Ide;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.workspace.iam.utility.MimeCodec;
import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

////////////////////////////////////////////////////////////////////////////////
// class XMLFormatter
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>XMLFormatter</code> object is used to format output as XML
 ** indented with a configurable indent level. This is used to write start and
 ** end tags, as well as attributes and values to the given writer. The output
 ** is written directly to the stream with an indentation for each element
 ** appropriate to its position in the document hierarchy. If the indent is set
 ** to zero then no indent is performed and all XML will appear on the same
 ** line.
 ** <p>
 ** The buffer size may be specified, or the default size may be accepted. The
 ** default is large enough for most purposes.
 **
 ** @see     XMLIndenter
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   12.2.1.3.42.60.74
 */
class XMLFormatter implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the prefix used when declaring an XML namespace. */
  private static final char[]      NAMESPACE_PREFIX = { 'x', 'm', 'l', 'n', 's' };

  /** open a comment section within the document. */
  private static final char[]      COMMENT_OPEN     = { '<', '!', '-', '-', ' ' };

  /** close a comment section within the document. */
  private static final char[]      COMMENT_CLOSE    = { ' ', '-', '-', '>' };

  /** the default buffer size of the character-output stream */
  private static int               BUFFER_SIZE      = 8192;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7109044484823175691")
  private static final long        serialVersionUID = 6425042195646348578L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** represents the prolog to insert at the start of the document. */
  private transient String         prolog;

  /** creates the indentations that are used buffer the XML file. */
  private transient XMLIndenter    indenter;

  /** output buffer used to write the generated XML result to. */
  private transient Buffer         buffer;

  /** the writer that is used to write the XML document. */
  private transient Writer         result;

  /** the character encoding to write the XML document. */
  private transient CharsetEncoder encoder;

  /** creates the codec to escape predefined entities. */
  private transient MimeCodec      codec;

  /** represents the current type of content that was written. */
  private transient Tag            cursor;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Tag
  // ~~~~ ~~~
  /**
   ** This is used to enumerate the different types of tag that can be written.
   ** Each tag represents a state for the writer. After a specific tag type has
   ** been written the state of the writer is updated.
   ** This is needed to write well formed XML text.
  */
  private enum Tag {
      COMMENT
    , START
    , TEXT
    , END
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-2801576677636681426")
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Buffer
  // ~~~~~ ~~~~~~
  /**
   ** This is primarily used to replace the <code>StringBuffer</code> class, as
   ** a way for the {@link XMLFormatter} to store the start tag for an XML
   ** element.
   ** <br>
   ** This enables the start tag of the current element to be removed without
   ** disrupting any of the other nodes within the document. Once the contents
   ** of the output buffer have been filled its contents can be emitted to the
   ** writer object.
   */
  class Buffer implements CharSequence {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the characters that this buffer has accumulated. */
    private StringBuilder text;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructor for <code>Buffer</code>.
     ** <p>
     ** The default <code>Buffer</code> stores 16 characters before a resize is
     ** needed to append extra characters.
     */
    public Buffer() {
      // ensure inheritance
      super();

      // intialize instance attributes
      this.text = new StringBuilder();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: length (CharSequence)
    /**
     ** Returns the length (character count).
     **
     ** @return                    the length of the sequence of characters
     **                            currently represented by this object.
     */
    public int length() {
      return this.text.length();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: charAt (CharSequence)
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
     ** @param  index            the index of the desired <code>char</code>
     **                          value.
     **
     ** @return                  the <code>char</code> value at the specified
     **                          index.
     **
     ** @throws IndexOutOfBoundsException  if <code>index</code> is negative or
     **                                    greater than or equal to
     **                                    <code>length()</code>.
     */
    public char charAt(int index) {
      return this.text.charAt(index);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: subSequence (CharSequence)
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
     ** @param  start            the start index, inclusive.
     ** @param  end              the end index, exclusive.
     ** @return                  the specified subsequence.
     **
     ** @throws IndexOutOfBoundsException if <code>start</code> or
     **                                   <code>end</code> are negative, if
     **                                   <code>end</code> is greater than
     **                                   <code>length()</code>, or if
     **                                   <code>start</code> is greater than
     **                                   <code>end</code>.
     */
    public CharSequence subSequence(int start, int end) {
      return this.text.subSequence(start, end);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the string representation of the <code>boolean</code> argument
     ** to this sequence.
     ** <p>
     ** The overall effect is exactly as if the argument were converted to a
     ** string by the method {@link String#valueOf(boolean)}, and the characters
     ** of that string were then {@link #append(String) appended} to this
     ** character sequence.
     **
     ** @param  b                an <code>boolean</code> value.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final boolean b) {
      this.text.append(b);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   append
    /**
     ** Appends the string representation of the <code>int</code> argument to
     ** this sequence.
     ** <p>
     ** The overall effect is exactly as if the argument were converted to a
     ** string by the method {@link String#valueOf(int)}, and the characters of
     ** that string were then {@link #append(String) appended} to this character
     ** sequence.
     **
     ** @param  i                an <code>int</code> value.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final int i) {
      this.text.append(i);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
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
     ** @param  l                an <code>long</code> value.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final long l) {
      this.text.append(l);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the string representation of the <code>float</code> argument to
     ** this sequence.
     ** <p>
     ** The overall effect is exactly as if the argument were converted to a
     ** string by the method {@link String#valueOf(float)}, and the characters
     ** of that string were then {@link #append(String) appended} to this
     ** character sequence.
     **
     ** @param  f                  an <code>float</code> value.
     **
     ** @return                    a reference to this object.
     */
    public Buffer append(final float f) {
      this.text.append(f);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the string representation of the <code>double</code> argument to
     ** this sequence.
     ** <p>
     ** The overall effect is exactly as if the argument were converted to a
     ** string by the method {@link String#valueOf(double)}, and the characters
     ** of that string were then {@link #append(String) appended} to this
     ** character sequence.
     **
     ** @param  d                 an <code>double</code> value.
     **
     ** @return                   a reference to this object.
     */
    public Buffer append(final double d) {
      this.text.append(d);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** This will add a <code>char</code> to the end of the buffer.
     ** <p>
     ** The buffer will not overflow with repeated uses of the
     ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
     ** will allow the buffer to dynamically grow in size to accommodate more
     ** characters.
     **
     ** @param  ch               the character to be appended to the buffer.
     */
    public Buffer append(final char ch) {
      this.text.append(ch);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** This will add a <code>char</code> array to the buffer.
     ** <p>
     ** The buffer will not overflow with repeated uses of the
     ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
     ** will allow the buffer to dynamically grow in size to accommodate large
     ** character arrays.
     **
     ** @param  value            the character array to be appended to this.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final char[] value) {
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
     ** @param  value            the character array to be appended to this
     ** @param  offset           the read offset for the array to begin reading
     ** @param  len              the number of characters to append to this
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final char[] value, final int offset, final int len) {
      this.text.append(value, offset, len);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** This will add a <code>String</code> to the end of the buffer.
     ** <p>
     ** The buffer will not overflow with repeated uses of the
     ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
     ** will allow the buffer to dynamically grow in size to accommodate large
     ** string objects.
     **
     ** @param  value            the string to be appended to this output
     **                          buffer.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final String value) {
      this.text.append(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** This will add a <code>String</code> to the end of the buffer.
     ** <p>
     ** The buffer will not overflow with repeated uses of the
     ** <code>append</code>, it uses an <code>ensureCapacity</code> method which
     ** will allow the buffer to dynamically grow in size to accommodate large
     ** string objects.
     **
     ** @param  value            the string to be appended to the output buffer.
     ** @param  offset           the offset to begin reading from the string.
     ** @param  len              the number of characters to append to this.
     */
    public Buffer append(final String value, final int offset, final int len) {
      this.text.append(value, offset, len);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the specified <code>CharSequence</code> to this sequence.
     ** <p>
     ** The characters of the {@link CharSequence} argument are appended, in
     ** order, to this sequence, increasing the length of this sequence by the
     ** length of the argument.
     ** <p>
     ** Let <i>n</i> be the length of this character sequence just prior to
     ** execution of the <code>append</code> method. Then the character at index
     ** <i>k</i> in the new character sequence is equal to the character at
     ** index <i>k</i> in the old character sequence, if <i>k</i> is less than
     ** <i>n</i>; otherwise, it is equal to the character at index <i>k-n</i> in
     ** the argument <code>buffer</code>.
     ** <p>
     ** If <code>sequence</code> is <code>null</code>, then this method appends
     ** characters as if the <code>sequence</code> parameter was a sequence
     ** containing the four characters <code>"null"</code>.
     **
     ** @param  sequence         the sequence to append.
     */
    public Buffer append(final CharSequence sequence) {
      this.text.append(sequence);
      return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends a subsequence of the specified <code>CharSequence</code> to this
     ** sequence.
     ** <p>
     ** Characters of the argument <code>sequence</code>, starting at index
     ** <code>start</code>, are appended, in order, to the contents of this
     ** sequence up to the (exclusive) index <code>end</code>. The length of
     ** this sequence is increased by the value of <code>end - start</code>.
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
     ** @param  sequence         the sequence to append.
     ** @param  start            the starting index of the subsequence to be
     **                          appended.
     ** @param  end              the end index of the subsequence to be
     **                          appended.
     **
     ** @return                  a reference to this object.
     **
     ** @throws IndexOutOfBoundsException if <code>start</code> is negative, or
     **                                   <code>start</code> is greater than
     **                                   <code>end</code> or <code>end</code> is
     **                                   greater than
     **                                   <code>sequence.length()</code>.
     */
    public Buffer append(final CharSequence sequence, int start, int end) {
      this.text.append(sequence, start, end);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the specified {@link StringBuffer} to this sequence.
     ** <p>
     ** The characters of the {@link StringBuffer} argument are appended, in
     ** order, to this sequence, increasing the length of this sequence by the
     ** length of the argument. If <code>buffer</code> is <code>null</code>,
     ** then the four characters <code>"null"</code> are appended to this
     ** sequence.
     ** <p>
     ** Let <i>n</i> be the length of this character sequence just prior to
     ** execution of the <code>append</code> method. Then the character at index
     ** <i>k</i> in the new character sequence is equal to the character at
     ** index <i>k</i> in the old character sequence, if <i>k</i> is less than
     ** <i>n</i>; otherwise, it is equal to the character at index <i>k-n</i> in
     ** the argument <code>buffer</code>.
     **
     ** @param  buffer           the {@link StringBuffer} to append.
     **
     ** @return                  a reference to this object.
     */
    public Buffer append(final StringBuffer buffer) {
      this.text.append(buffer);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: flush
    /**
     ** This method is used to write the contents of the buffer to the specified
     ** <code>Writer</code> object and clears the text buffer.
     ** <p>
     ** This is used when the XML element is to be committed to the resulting
     ** XML document.
     **
     ** @param  out              this is the writer to write the buffered text
     **                          to.
     **
     ** @throws IOException      if the character sequences cannot written to
     **                          the stream.
     */
    public void flush(final Writer out)
      throws IOException {

      write(out);
      clear();
      out.flush();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear
    /**
     ** This will empty the <code>OutputBuffer</code> so that it does not
     ** contain any content. This is used to that when the buffer is written to
     ** a specified <code>Writer</code> object nothing is written out.
     ** <p>
     ** This allows XML elements to be removed.
     */
    public void clear() {
      this.text.setLength(0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: write
    /**
     ** This method is used to write the contents of the buffer to the specified
     ** <code>Writer</code> object. This is used when the XML element is to be
     ** committed to the resulting XML document.
     **
     ** @param  out              this is the writer to write the buffered text
     **                          to.
     **
     ** @throws IOException      if the character sequences cannot written to
     **                          the stream.
     */
    public void write(final Writer out)
      throws IOException {

      out.append(this.text);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormatter</code> object.
   ** <p>
   ** This creates an object that can be used to write XML in an indented format
   ** to the specified writer. The XML written will be well formed.
   ** <p>
   ** Creates a buffered character-output stream that uses a default-sized
   ** output buffer.
   **
   ** @param  result             the {@link Writer} where the XML should be
   **                            written to.
   ** @param  format             the {@link XMLFormat} object to use.
   */
  public XMLFormatter(final Writer result, final XMLFormat format) {
    // ensure inheritance
    this(result, BUFFER_SIZE, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>XMLFormatter</code> object.
   ** <p>
   ** This creates an object that can be used to write XML in an indented format
   ** to the specified writer. The XML written will be well formed.
   ** <p>
   ** Creates a buffered character-output stream of the given size.
   **
   ** @param  result             the {@link Writer} where the XML should be
   **                            written to.
   ** @param  bufferSize         the output-buffer size, a positive integer.
   ** @param  format             the {@link XMLFormat} object to use.
   **
   ** @throws IllegalArgumentException  if <code>bufferSize</code> is less or
   **                                   equal to <code>0</code>.
   */
  public XMLFormatter(final Writer result, final int bufferSize, final XMLFormat format) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.result   = new BufferedWriter(result, bufferSize);
    this.indenter = new XMLIndenter(format);
    this.buffer   = new Buffer();
    this.prolog   = format.prolog();
    this.encoder  = format.encoder();
    this.codec    = format.codec();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  close
  /**
   ** Closes the stream, flushing it first. Once the stream has been closed,
   ** further write() or flush() invocations will cause an XMLException to be
   ** thrown. Closing a previously closed stream has no effect.
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void close()
    throws IOException {

    flush();
    this.result.close();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  flush
  /**
   ** This is used to flush the writer when the XML if it has been buffered.
   ** <p>
   ** The flush method is used by the node writer after an end element has been
   ** written. Flushing ensures that buffering does not affect the result of the
   ** node writer.
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void flush()
    throws IOException {

    this.buffer.flush(this.result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeProlog
  /**
   ** This is used to write a prolog to the specified output.
   ** <p>
   ** This is only written if the specified <code>Format</code> object has been
   ** given a non-<code>null</code> prolog. If no prolog is specified then no
   ** prolog is written to the generated XML.
   **
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void writeProlog()
    throws IOException {

    if (this.prolog != null) {
      write(this.prolog);
      write('\n');
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeComment
  /**
   ** This is used to write any comments that have been set.
   ** <p>
   ** The comment will typically be written at the start of an element to
   ** describe the purpose of the element or include debug data that can be used
   ** to determine any issues in serialization.
   **
   ** @param  comment            the comment that is to be written.
   */
  public void writeComment(final String comment) {
    final String text = this.indenter.peek();

    if (this.cursor == Tag.START)
      append('>');

    if (text != null) {
      append(text);
      append(COMMENT_OPEN);
      append(comment);
      append(COMMENT_CLOSE);
    }
    this.cursor = Tag.COMMENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeStart
  /**
   ** This method is used to write a start tag for an element.
   ** <p>
   ** If a start tag was written before this then it is closed. Before the start
   ** tag is written an indent is generated and placed in front of the tag, this
   ** is done for all but the first start tag.
   **
   ** @param  name               the name of the start tag to be written.
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void writeStart(final String name, final String prefix)
    throws IOException {

    final String text = indenter.push();
    if (this.cursor == Tag.START)
      append('>');

    flush();
    append(text);
    append('<');

    if (!StringUtility.empty(prefix)) {
      append(prefix);
      append(':');
    }
    append(name);
    this.cursor = Tag.START;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeEnd
  /**
   ** This is used to write an end element tag to the writer.
   ** <p>
   ** This will close the element with a short <code>/&gt;</code> if the last
   ** tag written was a start tag. However if an end tag or some text was
   ** written then a full end tag is written.
   **
   ** @param  name               the name of the element to be closed.
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void writeEnd(final String name, final String prefix)
    throws IOException {

    final String text = this.indenter.pop();
    if (this.cursor == Tag.START) {
      write('/');
      write('>');
    }
    else {
      if (this.cursor != Tag.TEXT) {
        write(text);
      }
      if (this.cursor != Tag.START) {
        write('<');
        write('/');
        write(name, prefix);
        write('>');
      }
    }
    this.cursor = Tag.END;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeNamespace
  /**
   ** This is used to write the namespace to the element.
   ** <p>
   ** This will write the special attribute using the prefix and reference
   ** specified. This will escape the reference if it is required.
   **
   ** @param  reference          the namespace URI reference to use.
   ** @param  prefix             the prefix to used for the namespace.
   **
   ** @throws IOException        if the character sequences cannot written to
   **                            the stream.
   */
  public void writeNamespace(final String reference, final String prefix)
    throws IOException {

    // prevent bogus state
    if (this.cursor != Tag.START) {
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.string(ComponentBundle.XML_OUTPUT_START_TAG)
      , ComponentBundle.string(ComponentBundle.TEMPALTE_TITLE)
      , null
      );
      return;
    }

    write(' ');
    write(NAMESPACE_PREFIX);

    if (!StringUtility.empty(prefix)) {
      write(':');
      write(prefix);
    }
    write('=');
    write('"');
    write(reference);
    write('"');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeAttribute
  /**
   ** This is used to write a name value attribute pair.
   ** <p>
   ** If the last tag written was not a start tag then this throws an exception.
   ** <br>
   ** All attribute values written are enclosed in double quotes.
   **
   ** @param  name               the name of the attribute to be written.
   ** @param  value              the value to assigne to the attribute.
   **
   ** @throws IOException        if the character cannot written to the stream.
   */
  public void writeAttribute(String name, String value, String prefix)
    throws IOException {

    // prevent bogus state
    if (this.cursor != Tag.START) {
      MessageDialog.error(
        Ide.getMainWindow()
      , ComponentBundle.string(ComponentBundle.XML_OUTPUT_START_TAG)
      , ComponentBundle.string(ComponentBundle.XML_TITLE)
      , null
      );
      return;
    }

    write(' ');
    write(name, prefix);
    write('=');
    write('"');
    write(this.codec.escape(value));
    write('"');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeText
  /**
   ** This is used to write the specified text value to the writer.
   ** <p>
   ** If the last tag written was a start tag then it is closed.
   ** By default this will escape any illegal XML characters.
   **
   ** @throws IOException        if the character cannot written to the stream.
   */
  public void writeText(final String text)
    throws IOException {

    writeText(text, XMLOutputMode.ESCAPE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  writeText
  /**
   ** This is used to write the specified text value to the writer.
   ** <p>
   ** If the last tag written was a start tag then it is closed. This will use
   ** the output mode specified.
   **
   ** @param  text               the text to write to the output.
   ** @param  mode               the {@link XMLOutputMode} how to write the
   **                            data.
   **
   ** @throws IOException        if the character cannot written to the stream.
   */
  public void writeText(final String text, final XMLOutputMode mode)
    throws IOException {

    if (this.cursor == Tag.START)
      write('>');
    if (mode == XMLOutputMode.DATA)
      data(text);
    else
      write(this.codec.escape(text));

    this.cursor = Tag.TEXT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  append
  /**
   ** This is used to buffer a character to the output stream without any
   ** translation. This is used when buffering the start tags so that they can
   ** be reset without affecting the resulting document.
   **
   ** @param  ch                 the character to be written to the output.
   */
  private void append(final char ch) {
    this.buffer.append(ch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  append
  /**
   ** This is used to buffer characters to the output stream without any
   ** translation. This is used when buffering the start tags so that they can
   ** be reset without affecting the resulting document.
   **
   ** @param  plain              the string that is to be buffered.
   */
  private void append(final char[] plain) {
    this.buffer.append(plain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  append
  /**
   ** This is used to buffer characters to the output stream without any
   ** translation. This is used when buffering the start tags so that they can
   ** be reset without affecting the resulting document.
   **
   ** @param  plain              the string that is to be buffered.
   */
  private void append(final String plain) {
    this.buffer.append(plain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  data
  /**
   ** This method is used to write the specified text as a <code>CDATA</code>
   ** block within the XML element.
   ** <p>
   ** This is typically used when the value is large or if it must be preserved
   ** in a format that will not be affected by other XML parsers. For large text
   ** values this is also faster than performing a character by character
   ** escaping.
   **
   ** @param  value              the text value to be written as
   **                            <code>CDATA</code> block.
   **
   ** @throws IOException        if the character cannot written to the stream.
   */
  private void data(final String value)
    throws IOException {

    write("<![CDATA[");
    write(value);
    write("]]>");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  write
  /**
   ** This is used to write a character to the output stream without any
   ** translation.
   ** <p>
   ** This is used when writing the start tags and end tags, this is also used
   ** to write attribute names.
   **
   ** @param  ch                 the character to be written to the output.
   **
   ** @throws IOException        if the character cannot written to the stream.
   */
  private void write(final char ch)
    throws IOException {

    this.buffer.write(this.result);
    this.buffer.clear();
    this.result.write(ch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  write
  /**
   ** This is used to write plain text to the output stream without any
   ** translation.
   ** <p>
   ** This is used when writing the start tags and end tags, this
   ** is also used to write attribute names.
   **
   ** @param  plain              the text to be written to the output.
   **
   ** @throws IOException        if the characters cannot written to the stream.
   */
  private void write(final char[] plain)
    throws IOException {

    this.buffer.write(this.result);
    this.buffer.clear();
    this.result.write(plain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  write
  /**
   ** This is used to write plain text to the output stream without any
   ** translation.
   ** <p>
   ** This is used when writing the start tags and end tags, this is also used
   ** to write attribute names.
   **
   ** @param  plain              the text to be written to the output.
   **
   ** @throws IOException        if the string cannot written to the stream.
   */
  private void write(final String plain)
    throws IOException {

    this.buffer.write(this.result);
    this.buffer.clear();
    this.result.write(plain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  write
  /**
   ** This is used to write plain text to the output stream without any
   ** translation.
   ** <p>
   ** This is used when writing the start tags and end tags, this is also used
   ** to write attribute names.
   **
   ** @param  plain              the text to be written to the output.
   ** @param  prefix             the namespace prefix to be written.
   **
   ** @throws IOException        if the string cannot written to the stream.
   */
  private void write(final String plain, final String prefix)
    throws IOException {

    this.buffer.write(this.result);
    this.buffer.clear();

    if (!StringUtility.empty(prefix)) {
      this.result.write(prefix);
      this.result.write(':');
    }
    this.result.write(plain);
  }
}