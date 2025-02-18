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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   ExpressionFactory.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExpressionFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.HashSet;

import java.io.Reader;

////////////////////////////////////////////////////////////////////////////////
// abstract class ExpressionFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Simplifies constructing instances of expressions passed later on to a
 ** Groovy shell for execution.
 ** <p>
 ** The factory returns a {@link Set} of property names ontains in a script.
 ** <br>
 ** This required to get back the related attributes from the connector bundle.
 ** <p>
 ** The conector framework filters the attributes returned in a connector object.
 ** The connctor object itself contains only attributes which are pass to the
 ** framework via set
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ExpressionFactory {

  //////////////////////////////////////////////////////////////////////////////
  // class StringReader
  // ~~~~~ ~~~~~~~~~~~~
  private static class StringReader extends Reader {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int          pos;
    private int          mark;
    private final String string;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>StringReader</code> with the string to read from.
     **
     ** @param  string           the string to read from.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private StringReader(final String string) {
      // enure inheritance
      super();

      // initialize instance
      this.string = string;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstrat base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (Reader)
    /**
     ** Reads characters into a portion of an array.
     ** <br>
     ** This method will block until some input is available, an I/O error
     ** occurs, or the end of the stream is reached.
     **
     ** @param  buffer           the destination buffer.
     **                          <br>
     **                          Allowed object is array of <code>char</code>.
     ** @param  offset           the offset at which to start storing
     **                          characters.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  length           the maximum number of characters to read.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the number of characters read, or -1 if the end
     **                          of the stream has been reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IOException      if an I/O error occurs.
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len) {
      if (this.pos >= this.string.length()) {
        return -1;
      }
      int chars = Math.min(string.length() - this.pos, len);
      System.arraycopy(string.toCharArray(), this.pos, cbuf, off, chars);
      this.pos += chars;
      return chars;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (Reader)
    /**
     ** Closes the stream and releases any system resources associated with it.
     ** Once the stream has been closed, further read(), ready(), mark(),
     ** reset(), or skip() invocations will throw an IOException.
     ** <br>
     ** Closing a previously closed stream has no effect.
     */
    @Override
    public void close() {
      // do nothing.
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: markSupported (overridden)
    /**
     ** Tells whether this stream supports the mark() operation.
     **
     ** @return                  always <code>true</code>.
     */
    @Override
    public boolean markSupported() {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mark (overridden)
    /**
     ** Marks the present position in the stream.
     ** <br>
     ** Subsequent calls to reset() will attempt to reposition the stream to
     ** this point.
     **
     ** @param limit             the limit on the number of characters that may
     **                          be read while still preserving the mark. After
     **                          reading this many characters, attempting to
     **                          reset the stream may fail.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public void mark(final int limit) {
      this.mark = this.pos;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: ready (overridden)
    /**
     ** Tells whether this stream is ready to be read.
     **
     ** @return                  always <code>true</code>.
     */
    @Override
    public boolean ready() {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: reset (overridden)
    /**
     ** Resets the stream.
     ** <br>
     ** If the stream has been marked, then attempt to reposition it at the
     ** mark. If the stream has not been marked, then attempt to reset it in
     ** some way appropriate to the particular stream, for example by
     ** repositioning it to its starting point. Not all character-input streams
     ** support the reset() operation, and some support reset() without
     ** supporting mark().
     */
    @Override
    public void reset() {
      this.pos = this.mark;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Reads a single character.
     ** <br>
     ** This method will block until a character is available, an I/O error
     ** occurs, or the end of the stream is reached.
     **
     ** @return                  the character read, as an integer in the range
     **                          0 to 65535 (<code>0x00-0xffff</code>), or -1 if
     **                          the end of the stream has been reached.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int read() {
      if (this.pos >= this.string.length()) {
        return -1;
      }
      return this.string.charAt(this.pos++);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Skips characters.
     ** <bR>
     ** This method will block until some characters are available, an I/O error
     ** occurs, or the end of the stream is reached.
     **
     ** @param  n                the number of characters to skip.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the number of characters actually skipped.
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    @Override
    public long skip(final long n) {
      final long chars = Math.min(this.string.length() - this.pos, n);
      this.pos += chars;
      return chars;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: unread
    /**
     ** Move the current read position back one character.
     */
    public void unread() {
      this.pos--;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ExpressionFactory</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ExpressionFactory()" and enforces use of the public method below.
   */
  private ExpressionFactory() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parse a expression string.
   **
   ** @param  expression         the expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Set} of binding variables from the
   **                            expression.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @throws RuntimeException   if the path string could not be parsed.
   */
  public static Set<String> parse(final String expression) {
    final Set<String>  stack = new HashSet<String>();
    final StringReader reader = new StringReader(expression.trim());
    String             token = null;
    do {
      token = next(reader, false);
      if (token != null && !(token.startsWith("'") || token.startsWith("$")))
        stack.add(token);
    } while (token != null);
    return stack;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Read a token. A token is either:
   ** <ul>
   **   <li>A path terminated by a space or an opening parenthesis.
   **   <li>A literal enclosed in single quotes
   ** </ul>
   **
   ** @param  reader             the string stream to parse.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   ** @param  literal            <code>true</code> if the parser in inside of a
   **                            character sequence that is enclosed in single
   **                            quotes.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the next token obtained from the specified
   **                            reader.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws RuntimeException   if the path string could not be parsed.
   */
  private static String next(final StringReader reader, boolean literal) {
    int c;
    do {
      // skip over any leading spaces.
      reader.mark(0);
      c = reader.read();
    } while (c == ' ');

    final StringBuilder b = new StringBuilder();
    while (c > 0) {
      if (c == ' ') {
        // terminating space
        // consume it and return token.
        return b.toString();
      }

      if (c == '\'') {
        if (!literal)
          // terminating opening quote. Consume it and return token.
          literal = true;
        else {
          b.append((char)c);
          return b.toString();
        }
      }
      if (c == '+' || c == '-') {
        // consume the aritmehtic characters
        // may be this will position the cursor on a whitespace which will than
        // consumed at the end of the loop
        c = reader.read();
      }
      else {
        // slurp the others as it is
        b.append((char)c);
      }
      c = reader.read();
    }
    return (b.length() > 0)  ? b.toString() : null;
  }
}