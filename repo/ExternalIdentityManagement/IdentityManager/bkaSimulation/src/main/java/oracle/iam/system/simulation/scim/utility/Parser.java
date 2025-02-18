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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Parser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Parser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.utility;

import java.util.Stack;
import java.util.LinkedList;

import java.io.Reader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class Parser
// ~~~~~ ~~~~~~
/**
 ** A parser for SCIM filter expressions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Parser {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class StringReader
  // ~~~~ ~~~~~~~~~~~~
  /**
   ** A simple implemantation of the {@link Reader} interface to walk through
   ** {@link String}s.
   */
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
   ** @throws BadRequestException if the path string could not be parsed.
   */
  public static Path path(final String expression)
    throws BadRequestException {

    Path path = Path.build();
    if (StringUtility.isEmpty(expression)) {
      return path;
    }

    String       trimmed = expression.trim();
    StringReader reader  = new StringReader(trimmed);
    if (Support.namespace(trimmed)) {
      // the attribute name is prefixed with the schema URN.
      // Find the last ":" before any open brackets. Everything to the left is
      // the schema URN, everything on the right is the attribute name plus a
      // potential value filter.
      int j = trimmed.indexOf('[');
      int i = (j >= 0) ? trimmed.substring(0, j).lastIndexOf(':') : trimmed.lastIndexOf(':');
      String namespace     = trimmed.substring(0, i++);
      String attributeName = trimmed.substring(i, trimmed.length());
      path = Path.build(namespace);
      if(attributeName.isEmpty()) {
        // the trailing colon signifies that this is an extension root.
        return path;
      }
      reader = new StringReader(attributeName);
    }
    String token;
    while ((token = readPathToken(reader)) != null) {
      if (token.isEmpty()) {
        // the only time this is allowed to occur is if the previous attribute
        // had a value filter, in which case, consume the token and move on.
        if (path.root() || path.element(path.size() - 1).filter() == null)
          throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_EXPECT_ATTRIBUTE_NAME, reader.mark));
      }
      else {
        String attributeName = token;
        Filter valueFilter   = null;
        try {
          if (attributeName.endsWith("[")) {
            // there is a value path.
            attributeName = attributeName.substring(0, attributeName.length() - 1);
            valueFilter   = readFilter(reader, true);
          }
          path = path.attribute(attributeName, valueFilter);
        }
        catch(BadRequestException e) {
          throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_FILTER, e.getMessage()));
        }
        catch(Exception e) {
          throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_ATTRIBUTE_NAME, reader.mark, e.getMessage()));
        }
      }
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
   ** @throws BadRequestException if the filter string could not be parsed.
   */
  public static Filter filter(final String expression)
    throws BadRequestException {

    return readFilter(new StringReader(expression.trim()), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readPathToken
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
   ** @throws BadRequestException if the path string could not be parsed.
   */
  private static String readPathToken(final StringReader reader)
    throws BadRequestException {

    reader.mark(0);
    int c = reader.read();
    final StringBuilder b = new StringBuilder();
    while (c > 0) {
      if (c == '.') {
        if (reader.pos >= reader.string.length()) {
          // there is nothing after the period.
          throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_STRING));
        }
        // terminating period
        // consume it and return token
        return b.toString();
      }
      else if (c == '[') {
        // terminating opening brace
        // consume it and return token.
        b.append((char)c);
        return b.toString();
      }
      else if (c == '-' || c == '_' || c == '$' || Character.isLetterOrDigit(c)) {
        b.append((char)c);
      }
      else {
        throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_CHARACTER, (char)c, reader.pos - 1, reader.mark));
      }
      c = reader.read();
    }
    return (b.length() > 0) ?  b.toString() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFilter
  /**
   ** Read a filter from the reader.
   **
   ** @param  reader             the reader to read the filter from.
   ** @param  isValueFilter      whether to read the filter as a value filter.
   **
   ** @return                    the parsed filter.
   **
   ** @throws BadRequestException if the filter string could not be parsed.
   */
  private static Filter readFilter(final StringReader reader, final boolean isValueFilter)
    throws BadRequestException {

    final Stack<Filter> outputStack     = new Stack<Filter>();
    final Stack<String> precedenceStack = new Stack<String>();

    String token;
    String previousToken = null;

    while ((token = readFilterToken(reader, isValueFilter)) != null) {
      if (token.equals("(") && expectsNewFilter(previousToken)) {
        precedenceStack.push(token);
      }
      else if (token.equalsIgnoreCase(Filter.Type.NOT.value()) && expectsNewFilter(previousToken)) {
        // "not" should be followed by an (
        String nextToken = readFilterToken(reader, isValueFilter);
        if (nextToken == null) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
        }
        if (!nextToken.equals("(")) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_PARENTHESIS, reader.mark));
        }
        precedenceStack.push(token);
      }
      else if (token.equals(")") && !expectsNewFilter(previousToken)) {
        String operator = closeGrouping(precedenceStack, outputStack, false);
        if (operator == null) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_PARENTHESIS, reader.mark));
        }
        if (operator.equalsIgnoreCase(Filter.Type.NOT.value())) {
          // Treat "not" the same as "(" except wrap everything in a not filter.
          outputStack.push(Filter.not(outputStack.pop()));
        }
      }
      else if (token.equalsIgnoreCase(Filter.Type.AND.value()) && !expectsNewFilter(previousToken)) {
        // and has higher precedence than or.
        precedenceStack.push(token);
      }
      else if (token.equalsIgnoreCase(Filter.Type.OR.value()) && !expectsNewFilter(previousToken)) {
        // pop all the pending ands first before pushing or.
        LinkedList<Filter> andComponents = new LinkedList<Filter>();
        while (!precedenceStack.isEmpty()) {
          if (precedenceStack.peek().equalsIgnoreCase(Filter.Type.AND.value())) {
            precedenceStack.pop();
            andComponents.addFirst(outputStack.pop());
          }
          else {
            break;
          }
          if (!andComponents.isEmpty()) {
            andComponents.addFirst(outputStack.pop());
            outputStack.push(Filter.and(andComponents));
          }
        }
        precedenceStack.push(token);
      }
      else if (token.endsWith("[") && expectsNewFilter(previousToken)) {
        // this is a complex value filter.
        final Path filterAttribute;
        try {
          filterAttribute = path(token.substring(0, token.length() - 1));
        }
        catch (final BadRequestException e) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()));
        }

        if (filterAttribute.root())
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark));

        outputStack.push(Filter.complex(filterAttribute, readFilter(reader, true)));
      }
      else if (isValueFilter && token.equals("]") && !expectsNewFilter(previousToken)) {
        break;
      }
      else if (expectsNewFilter(previousToken)) {
        // this must be an attribute path followed by operator and maybe value.
        final Path filterAttribute;
        try {
          filterAttribute = path(token);
        }
        catch (final BadRequestException e) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()));
        }

        if (filterAttribute.root())
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark));

        String op = readFilterToken(reader, isValueFilter);
        if (op == null)
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

        if (op.equalsIgnoreCase(Filter.Type.PR.value())) {
          outputStack.push(Filter.pr(filterAttribute));
        }
        else {
          ValueNode valueNode;
          try {
            // mark the beginning of the JSON value so we can later reset back
            // to this position and skip the actual chars that were consumed
            // by Jackson. The Jackson parser is buffered and reads everything
            // until the end of string.
            reader.mark(0);
            ScimFactory factory = (ScimFactory)Support.objectReader().getFactory();
            JsonParser  parser  = factory.createScimFilterParser(reader);
            // the object mapper will return a Java null for JSON null.
            // Have to distinguish between reading a JSON null and encountering
            // the end of string.
            if (parser.getCurrentToken() == null && parser.nextToken() == null) {
              // End of string.
              valueNode = null;
            }
            else {
              valueNode = parser.readValueAsTree();
              // this is actually a JSON null. Use NullNode.
              if (valueNode == null) {
                valueNode = Support.jsonNodeFactory().nullNode();
              }
            }
            // reset back to the beginning of the JSON value.
            reader.reset();
            // rkip the number of chars consumed by JSON parser.
            reader.skip(parser.getCurrentLocation().getCharOffset());
          }
          catch (IOException e) {
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_COMPARISON_VALUE, reader.mark, e.getMessage()));
          }

          if (valueNode == null)
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

          if (op.equalsIgnoreCase(Filter.Type.EQ.value())) {
            outputStack.push(Filter.eq(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.NE.value())) {
            outputStack.push(Filter.not(Filter.eq(filterAttribute, valueNode)));
          }
          else if (op.equalsIgnoreCase(Filter.Type.CO.value())) {
            outputStack.push(Filter.co(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.SW.value())) {
            outputStack.push(Filter.sw(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.EW.value())) {
            outputStack.push(Filter.ew(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.GT.value())) {
            outputStack.push(Filter.gt(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.GE.value())) {
            outputStack.push(Filter.ge(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.LT.value())) {
            outputStack.push(Filter.lt(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.LE.value())) {
            outputStack.push(Filter.le(filterAttribute, valueNode));
          }
          else {
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNRECOGNOIZED_OPERATOR, op, reader.mark));
          }
        }
      }
      else {
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_CHARACTER, token, reader.mark));
      }
      previousToken = token;
    }

    closeGrouping(precedenceStack, outputStack, true);

    if (outputStack.isEmpty())
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

    return outputStack.pop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFilterToken
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
   * @throws BadRequestException if the filter string could not be parsed.
   */
  private static String readFilterToken(final StringReader reader, final boolean isValueFilter)
    throws BadRequestException {

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
      if (c == '(' || c == ')') {
        if (b.length() > 0) {
          // do not consume the parenthesis.
          reader.unread();
        }
        else {
          b.append((char)c);
        }
        return b.toString();
      }
      if (!isValueFilter && c == '[') {
        // terminating opening brace. Consume it and return token.
        b.append((char)c);
        return b.toString();
      }
      if (isValueFilter && c == ']') {
        if (b.length() > 0) {
          // do not consume the closing brace.
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
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_CHARACTER, (char)c, reader.pos - 1, reader.mark));
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
   ** @throws BadRequestException if the filter string could not be parsed.
   */
  private static String closeGrouping(final Stack<String> operators, final Stack<Filter> output, final boolean isAtTheEnd)
    throws BadRequestException {

    String             operator = null;
    String             repeatingOperator = null;
    LinkedList<Filter> components = new LinkedList<Filter>();
    // iterate over the logical operators on the stack until either there are
    // no more operators or an opening parenthesis or not is found.
    while (!operators.isEmpty()) {
      operator = operators.pop();
      if (operator.equals("(") || operator.equalsIgnoreCase(Filter.Type.NOT.value())) {
        if (isAtTheEnd) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
        }
        break;
      }
      if (repeatingOperator == null) {
        repeatingOperator = operator;
      }
      if (!operator.equals(repeatingOperator)) {
        if (output.isEmpty()) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
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
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
      }
      components.addFirst(output.pop());
    }

    if (repeatingOperator != null && !components.isEmpty()) {
      if (output.isEmpty()) {
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
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
   *
   * @param previousToken The previous filter token.
   * @return Whether a new filter token is expected.
   */
  private static boolean expectsNewFilter(final String token) {
    return token == null || token.equals("(") || token.equalsIgnoreCase(Filter.Type.NOT.value()) || token.equalsIgnoreCase(Filter.Type.AND.value()) || token.equalsIgnoreCase(Filter.Type.OR.value());
  }
}