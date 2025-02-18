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
    Subsystem   :   Common Shared Utility

    File        :   EntityParser.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.io.IOException;
import java.io.Reader;

import java.text.ParseException;

import java.util.LinkedList;
import java.util.Stack;

import oracle.hst.platform.core.SystemBundle;
import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class FilterParser
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A parser for filter expressions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class EntityParser {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class StringReader
  // ~~~~ ~~~~~~~~~~~~~ 
  private static final class StringReader extends Reader {

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
      if (pos >= string.length()) {
        return -1;
      }
      int chars = Math.min(string.length() - pos, len);
      System.arraycopy(string.toCharArray(), pos, cbuf, off, chars);
      pos += chars;
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
   ** Constructs an empty <code>EntityParser</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new EntityParser()" and enforces use of the public method below.
   */
  private EntityParser() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Parse a path string.
   **
   ** @param  expression         the path expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the path from the expression.
   **                            <br>
   **                            Possible object is {@link Path}.
   **
   ** @throws ParseException     if the path string could not be parsed.
   */
  public static Path path(final String expression)
    throws ParseException {

    Path path = Path.build();
    if (StringUtility.empty(expression)) {
      return path;
    }

    String       trimmed = expression.trim();
    StringReader reader  = new StringReader(trimmed);
    String       token   = pathToken(reader);
    while (token != null) {
      if (token.isEmpty()) {
        // the only time this is allowed to occur is if the previous attribute
        // had a value filter, in which case, consume the token and move on
        if (path.root() || path.segment(path.size() - 1).filter() == null)
          throw new ParseException(SystemBundle.string(SystemError.PATH_EXPECT_ATTRIBUTE_NAME, reader.mark), reader.pos);
      }
      else {
        String attributeName = token;
        Filter valueFilter   = null;
        try {
          if (attributeName.endsWith("[")) {
            // there is a value path
            attributeName = attributeName.substring(0, attributeName.length() - 1);
            valueFilter   = filter(reader, true);
          }
          path = path.path(attributeName, valueFilter);
        }
        catch(ParseException e) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_INVALID_FILTER, e.getMessage()), reader.pos);
        }
        catch(Exception e) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_INVALID_ATTRIBUTE_NAME, reader.mark, e.getMessage()), reader.pos);
        }
      }
      token = pathToken(reader);
    }
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Parse a filter string.
   **
   ** @param  expression         the filter expression to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the filter from the expression.
   **                            <br>
   **                            Possible object is {@link Path}.
   **
   ** @throws ParseException     if the filter expression could not be parsed.
   */
  public static Filter filter(final String expression)
    throws ParseException {

    // prevent bogus input
    return StringUtility.empty(expression) ? null : filter(new StringReader(expression.trim()), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isUrn
  /**
   ** Returns true if the string passed in appears to be a urn.
   ** That determination is made by looking to see if the string
   ** starts with "{@code urn:}".
   **
   ** @param string the string to check.
   ** @return true if it's a urn, or false if not.
   */
  public static boolean isUrn(String string){
    return (string == null? false : string.toLowerCase().startsWith("urn:") && string.length() > 4);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Read a filter from the reader.
   **
   ** @param  reader             the reader to read the filter from.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   ** @param  isValueFilter      whether to read the filter as a value filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the parsed filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the filter expression could not be parsed.
   */
  @SuppressWarnings({"unchecked", "cast"})
  private static <T> Filter<T> filter(final StringReader reader, final boolean isValueFilter)
    throws ParseException {

    // prevent bogus input
    if (reader == null)
      return null;

    final Stack<Filter<T>> output = new Stack<Filter<T>>();
    final Stack<String>    token  = new Stack<String>();

    String previous = null;
    String current  = filterToken(reader, isValueFilter);
    while (current != null) {
      if (current.equals("(") && expectsNewFilter(previous)) {
        token.push(current);
      }
      else if (current.equalsIgnoreCase(Filter.Type.NOT.value()) && expectsNewFilter(previous)) {
        // "not" should be followed by an (
        String nextToken = filterToken(reader, isValueFilter);
        if (nextToken == null) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), reader.pos);
        }
        if (!nextToken.equals("(")) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_EXPECT_PARENTHESIS, reader.mark), reader.pos);
        }
        token.push(current);
      }
      else if (current.equals(")") && !expectsNewFilter(previous)) {
        String operator = closeGrouping(token, output, false);
        if (operator == null) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_INVALID_PARENTHESIS, reader.mark), reader.pos);
        }
        if (operator.equalsIgnoreCase(Filter.Type.NOT.value())) {
          // treat "not" the same as "(" except wrap everything in a not filter.
          output.push(Filter.not(output.pop()));
        }
      }
      else if (current.equalsIgnoreCase(Filter.Type.AND.value()) && !expectsNewFilter(previous)) {
        // and has higher precedence than or.
        token.push(current);
      }
      else if (current.equalsIgnoreCase(Filter.Type.OR.value()) && !expectsNewFilter(previous)) {
        // pop all the pending ands first before pushing or.
        final LinkedList<Filter<T>> and = new LinkedList<Filter<T>>();
        while (!token.isEmpty()) {
          if (token.peek().equalsIgnoreCase(Filter.Type.AND.value())) {
            token.pop();
            and.addFirst(output.pop());
          }
          else {
            break;
          }
          if (!and.isEmpty()) {
            and.addFirst(output.pop());
            output.push(Filter.and(and));
          }
        }
        token.push(current);
      }
      else if (current.endsWith("[") && expectsNewFilter(previous)) {
        // this is a complex value filter.
        final Path path;
        try {
          path = path(current.substring(0, current.length() - 1));
        }
        catch (final ParseException e) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()), reader.pos);
        }

        if (path.root())
          throw new ParseException(SystemBundle.string(SystemError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark), reader.pos);

        output.push(Filter.complex(path, filter(reader, true)));
      }
      else if (isValueFilter && current.equals("]") && !expectsNewFilter(previous)) {
        break;
      }
      else if (expectsNewFilter(previous)) {
        // this must be an attribute path followed by operator and maybe value
        final Path path;
        try {
          path = path(current);
        }
        catch (final ParseException e) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()), reader.pos);
        }

        if (path.root())
          throw new ParseException(SystemBundle.string(SystemError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark), reader.pos);

        String op = filterToken(reader, isValueFilter);
        if (op == null)
          throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), reader.pos);

        if (op.equalsIgnoreCase(Filter.Type.PR.value())) {
          output.push(Filter.pr(path));
        }
        else {
          T value = (T)valueToken(reader);
          if (value == null)
            throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), reader.pos);

          final Filter.Type type = Filter.Type.from(op);
          switch (type) {
            case EQ : output.push(Filter.eq(path, value));
                      break;
            case NE : output.push(Filter.not(Filter.eq(path, value)));
                      break;
            case GT : output.push(Filter.gt(path, value));
                      break;
            case GE : output.push(Filter.ge(path, value));
                      break;
            case LT : output.push(Filter.lt(path, value));
                      break;
            case LE : output.push(Filter.le(path, value));
                      break;
            case SW : output.push(Filter.sw(path, value));
                      break;
            case EW : output.push(Filter.ew(path, value));
                      break;
            case CO : output.push(Filter.co(path, value));
                      break;
            default : new ParseException(SystemBundle.string(SystemError.PATH_UNRECOGNOIZED_OPERATOR, op, reader.mark), reader.pos);
          }
        }
      }
      else {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_CHARACTER, current, reader.pos, reader.mark), reader.pos);
      }
      previous = current;
      current  = filterToken(reader, isValueFilter);
    }
    closeGrouping(token, output, true);

    if (output.isEmpty())
      throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), reader.pos);

    return output.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pathToken
  /**
   ** Read a path token. A token is either:
   ** <ul>
   **   <li>An attribute name terminated by a period.
   **   <li>An attribute name terminated by an opening brace.
   ** </ul>
   **
   ** @param  reader             the reader to read from.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   **
   ** @return                    the token at the current position, or
   **                            <code>null</code> if the end of the input has
   **                            been reached.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  private static String pathToken(final StringReader reader)
    throws ParseException {

    reader.mark(0);
    int c = reader.read();
    final StringBuilder b = new StringBuilder();
    while (c > 0) {
      if (c == '.') {
        if (reader.pos >= reader.string.length()) {
          // there is nothing after the period
          throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_STRING), reader.pos);
        }
        // terminating period
        // consume it and return token
        return b.toString();
      }
      else if (c == '[') {
        // terminating opening brace
        // consume it and return token
        b.append((char)c);
        return b.toString();
      }
      else if (c == '-' || c == '_' || c == '$' || Character.isLetterOrDigit(c)) {
        b.append((char)c);
      }
      else {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_CHARACTER, (char)c, reader.pos - 1, reader.mark), reader.pos - 1);
      }
      c = reader.read();
    }
    return (b.length() > 0) ?  b.toString() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterToken
  /**
   ** Read a filter token. A token is either:
   ** <ul>
   **   <li>An attribute path terminated by a space or an opening parenthesis.
   **   <li>An attribute path terminated by an opening brace.
   **   <li>An operator terminated by a space or an opening parenthesis.
   **   <li>An opening parenthesis.
   **   <li>An closing parenthesis.
   **   <li>An closing brace.
   ** </ul>
   **
   ** @param  reader             the reader to read from.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   ** @param  isValueFilter      whether to read the token for a value filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the token at the current position, or
   **                            <code>null</code> if the end of the input has
   **                            been reached.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   * @throws ParseException      if the filter expression could not be parsed.
   */
  private static String filterToken(final StringReader reader, final boolean isValueFilter)
    throws ParseException {

    final StringBuilder b = new StringBuilder();
    int c = nextToken(reader);
    while (c > 0) {
      if (c == ' ') {
        // terminating space
        // consume it and return token
        return b.toString();
      }
      if (c == '(' || c == ')') {
        if (b.length() > 0) {
          // do not consume the parenthesis
          reader.unread();
        }
        else {
          b.append((char)c);
        }
        return b.toString();
      }
      if (!isValueFilter && c == '[') {
        // terminating opening brace. Consume it and return token
        b.append((char)c);
        return b.toString();
      }
      if (isValueFilter && c == ']') {
        if (b.length() > 0) {
          // do not consume the closing brace
          reader.unread();
        }
        else {
          b.append((char)c);
        }
        return b.toString();
      }
      if (c == '-' || c == '_' || c == '.' || c == ':' || c == '$' || Character.isLetterOrDigit(c)) {
        b.append((char)c);
      }
      else {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_CHARACTER, (char)c, reader.pos - 1, reader.mark), reader.pos - 1);
      }
      c = reader.read();
    }
    return (b.length() > 0)  ? b.toString() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueToken
  /**
   ** Read a value token. A token is either:
   ** <ul>
   **   <li>A value enclosed in single quotes that evaluates to a string.
   **   <li>A value enclosed in double quotes that evaluates to a string.
   **   <li>A value enclosed in spaces evaluates to a numeric value.
   ** </ul>
   **
   ** @param  reader             the reader to read from.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   **
   ** @return                    the token at the current position, or
   **                            <code>null</code> if the end of the input has
   **                            been reached.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws ParseException     if the value expression could not be parsed.
   */
  private static Object valueToken(final StringReader reader)
    throws ParseException {

    int q = 0;
    int c = nextToken(reader);
    final StringBuilder b = new StringBuilder();
    while (c > 0) {
      if (c == ' ' && q == 0) {
        // terminating space
        // consume it and return token
        return b.toString();
      }
      // a literal value starts end ends either with a single or double quote
      if (q == 0 && (c == '\'' || c == '"')) {
        q = c;
        c = reader.read();
        // scan for the next same quote
        while (c > 0 && c != q) {
          b.append((char)c);
          c = reader.read();
        }
        break;
      }
      // a numeric value is not enclosed by single or double quotes
      else if (c == '-' || Character.isLetterOrDigit(c)) {
        b.append((char)c);
      }
      // but may be at the last part of the expression
      else if (c == ')') {
        // do not consume the closing parenthesis
        reader.unread();
        break;
      }
      else {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_CHARACTER, (char)c, reader.pos - 1, reader.mark), reader.pos - 1);
      }
      c = reader.read();
    }
    return (b.length() > 0)  ? b.toString() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   closeGrouping
  /**
   ** Close a grouping of filters enclosed by parenthesis.
   **
   ** @param  operators          the stack of operators tokens.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   ** @param  output             the stack of output tokens.
   **                            <br>
   **                            Allowed object is {@link Stack} where each
   **                            element is of type {@link String}.
   ** @param  isAtTheEnd         whether the end of the filter string was
   **                            reached.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the last operator encountered that signaled the
   **                            end of the group.
   **
   ** @throws ParseException     if the filter string could not be parsed.
   */
  private static <T> String closeGrouping(final Stack<String> operators, final Stack<Filter<T>> output, final boolean isAtTheEnd)
    throws ParseException {

    String                operator = null;
    String                repeatingOperator = null;
    LinkedList<Filter<T>> components = new LinkedList<Filter<T>>();
    // iterate over the logical operators on the stack until either there are
    // no more operators or an opening parenthesis or not is found.
    while (!operators.isEmpty()) {
      operator = operators.pop();
      if (operator.equals("(") || operator.equalsIgnoreCase(Filter.Type.NOT.value())) {
        if (isAtTheEnd) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), -1);
        }
        break;
      }
      if (repeatingOperator == null) {
        repeatingOperator = operator;
      }
      if (!operator.equals(repeatingOperator)) {
        if (output.isEmpty()) {
          throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), -1);
        }
        components.addFirst(output.pop());
        if (repeatingOperator.equalsIgnoreCase(Filter.Type.AND.value())) {
          output.push(Filter.and(components));
        }
        else {
          output.push(Filter.or(components));
        }
        components.clear();
        repeatingOperator = operator;
      }
      if (output.isEmpty()) {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), -1);
      }
      components.addFirst(output.pop());
    }

    if (repeatingOperator != null && !components.isEmpty()) {
      if (output.isEmpty()) {
        throw new ParseException(SystemBundle.string(SystemError.PATH_UNEXPECTED_EOF_FILTER), -1);
      }
      components.addFirst(output.pop());
      if (repeatingOperator.equalsIgnoreCase(Filter.Type.AND.value())) {
        output.push(Filter.and(components));
      }
      else {
        output.push(Filter.or(components));
      }
    }
    return operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expectsNewFilter
  /**
   ** Whether a new filter token is expected given the previous token.
   **
   ** @param  token              the previous filter token.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    whether a new filter token is expected.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean expectsNewFilter(final String token) {
    return token == null || token.equals("(") || token.equalsIgnoreCase(Filter.Type.NOT.value()) || token.equalsIgnoreCase(Filter.Type.AND.value()) || token.equalsIgnoreCase(Filter.Type.OR.value());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextToken
  /**
   ** Move the position of the reader to the next non-space character.
   **
   ** @param  reader             the reader to position.
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   **
   ** @return                    the next available non-space character.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  private static int nextToken(final StringReader reader) {
    int c;
    do {
      // skip over any leading spaces
      reader.mark(0);
      c = reader.read();
    } while (c == ' ');
    return c;
  }
}