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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   Parser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Parser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.marshal;

import java.util.Set;
import java.util.Stack;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;

import java.io.Reader;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.hst.platform.rest.marshal.FilterFactory;

import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.schema.Support;

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
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A {@link ThreadLocal} parser options object, to configure nonstandard
   ** settings such as permitting semicolons in attribute names.
   */
  private static final ThreadLocal<Option> option = ThreadLocal.withInitial(Option::new);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Option
  // ~~~~ ~~~~~~~
  public static final class Option {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Set<Character> extended = new HashSet<>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty parser <code>Option</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Option() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear
    /**
     ** Clear the set of extended naming characters currently allowed within
     ** attribute names, so that only standard attribute naming characters are
     ** allowed.
     **
     ** @return                  the updated <code>Option</code> to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option clear() {
      this.extended.clear();
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: is
    /**
     ** Indicate whether a given character is in the set of extended naming
     ** characters currently allowed within attribute names.
     **
     ** @param  c                the desired character.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  <code>true</code> if the character is in the
     **                          extended set.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean is(final int c) {
      return is((char)c);
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: is
    /**
     ** Indicate whether a given character is in the set of extended naming
     ** characters currently allowed within attribute names.
     **
     ** @param  c                the desired character.
     **                          <br>
     **                          Allowed object is <code>char</code>.
     **
     ** @return                  <code>true</code> if the character is in the
     **                          extended set.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean is(final char c) {
      return this.extended.contains(c);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add characters (such as semicolons) to the set of extended naming
     ** characters currently allowed within attribute names.
     **
     ** @param  c                the extended characters to be added.
     **                          <br>
     **                          Allowed object is array of {@code Character}.
     **
     ** @return                  the updated <code>Option</code> to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Option</code>.
     */
    public Option add(final Character... c) {
      for (Character cursor : c) {
        this.extended.add(cursor);
      }
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get
    /**
     ** Returns the set of extended naming characters currently allowed within
     ** attribute names.
     **
     ** @return                  the extended set.
     **                          <br>
     **                          By default this will be empty, indicating that
     **                          only standard attribute naming characters are
     **                          allowed.
     **                          <br>
     **                          Possible object is {@link Set} where each.
     **                          element is of type {@link Character}
     */
    public Set<Character> get() {
      return Collections.unmodifiableSet(this.extended);
    }
  }

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
      // ensure inheritance
      super();

      // initialize instance attributes
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
    public int read(final char[] buffer, final int offset, final int length) {
      if (this.pos >= this.string.length()) {
        return -1;
      }
      int chars = Math.min(this.string.length() - this.pos, length);
      System.arraycopy(this.string.toCharArray(), this.pos, buffer, offset, chars);
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
     **                          <br>
     **                          Possible object is <code>boolean</code>.
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
     **                          <br>
     **                          Possible object is <code>boolean</code>.
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
      return (this.pos >= this.string.length()) ? -1 : this.string.charAt(this.pos++);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (overridden)
    /**
     ** Skips characters.
     ** <br>
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
  // Method:   option
  /**
   ** Returns the current {@link Option} within the current thread.
   **
   ** @return                    the current parser options.
   **                            <br>
   **                            Possible object is {@link Option}.
   */
  public static Option option() {
    return Parser.option.get();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  /**
   ** Set new {@link Option} within the current thread.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** SCIM server implementations are not guaranteed to support a given option.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** These should be reset as soon as they are no longer needed.
   ** <pre>
   **   Set&lt;Parser.Option&gt; origin = Parser.option(newOptions);
   **   try {
   **     performWhateverProcessing();
   **   }
   **   finally {
   **     Parser.option(origin);
   **   }
   ** </pre>
   **
   ** @param  option             the new parser options.
   **                            <br>
   **                            Allowed object is {@link Option}.
   **
   ** @return                    the prior parser options.
   **                            <br>
   **                            Possible object is {@link Option}.
   */
  public static Option option(final Option option) {
    Option origin = Parser.option.get();
    Parser.option.set(option);
    return origin;
  }

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
    if (StringUtility.empty(expression)) {
      return path;
    }

    String       trimmed = expression.trim();
    StringReader reader  = new StringReader(trimmed);
    if (Support.namespace(trimmed)) {
      // the attribute name is prefixed with the schema URN.
      // find the last ":" before any open brackets. Everything to the left is
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
        if (path.empty() || path.element(path.size() - 1).filter() == null)
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

    final Option option = Parser.option();

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
      else if (c == '-' || c == '_' || c == '$' || Character.isLetterOrDigit(c) || option.is(c)) {
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
   **                            <br>
   **                            Allowed object is {@link StringReader}.
   ** @param  valueFilter        whether to read the filter as a value filter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the parsed filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws BadRequestException if the filter string could not be parsed.
   */
  private static Filter readFilter(final StringReader reader, final boolean valueFilter)
    throws BadRequestException {

    final Stack<Filter> output     = new Stack<Filter>();
    final Stack<String> precedence = new Stack<String>();

    String token;
    String previous = null;

    while ((token = readFilterToken(reader, valueFilter)) != null) {
      if (token.equals("(") && expectNew(previous)) {
        precedence.push(token);
      }
      else if (token.equalsIgnoreCase(Filter.Type.NOT.value()) && expectNew(previous)) {
        // "not" should be followed by an (
        String nextToken = readFilterToken(reader, valueFilter);
        if (nextToken == null) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
        }
        if (!nextToken.equals("(")) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_PARENTHESIS, reader.mark));
        }
        precedence.push(token);
      }
      else if (token.equals(")") && !expectNew(previous)) {
        String operator = closeGrouping(precedence, output, false);
        if (operator == null) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_PARENTHESIS, reader.mark));
        }
        if (operator.equalsIgnoreCase(Filter.Type.NOT.value())) {
          // treat "not" the same as "(" except wrap everything in a not filter.
          output.push(Filter.not(output.pop()));
        }
      }
      else if (token.equalsIgnoreCase(Filter.Type.AND.value()) && !expectNew(previous)) {
        // and has higher precedence than or.
        precedence.push(token);
      }
      else if (token.equalsIgnoreCase(Filter.Type.OR.value()) && !expectNew(previous)) {
        // pop all the pending ands first before pushing or.
        LinkedList<Filter> andComponents = new LinkedList<Filter>();
        while (!precedence.isEmpty()) {
          if (precedence.peek().equalsIgnoreCase(Filter.Type.AND.value())) {
            precedence.pop();
            andComponents.addFirst(output.pop());
          }
          else {
            break;
          }
          if (!andComponents.isEmpty()) {
            andComponents.addFirst(output.pop());
            output.push(Filter.and(andComponents));
          }
        }
        precedence.push(token);
      }
      else if (token.endsWith("[") && expectNew(previous)) {
        // this is a complex value filter.
        final Path filterAttribute;
        try {
          filterAttribute = path(token.substring(0, token.length() - 1));
        }
        catch (final BadRequestException e) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()));
        }

        if (filterAttribute.empty())
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark));

        output.push(Filter.complex(filterAttribute, readFilter(reader, true)));
      }
      else if (valueFilter && token.equals("]") && !expectNew(previous)) {
        break;
      }
      else if (expectNew(previous)) {
        // this must be an attribute path followed by operator and maybe value.
        final Path filterAttribute;
        try {
          filterAttribute = path(token);
        }
        catch (final BadRequestException e) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_ATTRIBUTE_PATH, reader.mark, e.getMessage()));
        }

        if (filterAttribute.empty())
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_EXPECT_ATTRIBUTE_PATH, reader.mark));

        String op = readFilterToken(reader, valueFilter);
        if (op == null)
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

        if (op.equalsIgnoreCase(Filter.Type.PR.value())) {
          output.push(Filter.pr(filterAttribute));
        }
        else {
          ValueNode valueNode;
          try {
            // mark the beginning of the JSON value so we can later reset back
            // to this position and skip the actual chars that were consumed
            // by Jackson. The Jackson parser is buffered and reads everything
            // until the end of string.
            reader.mark(0);
            FilterFactory factory = (FilterFactory)Support.objectReader().getFactory();
            JsonParser  parser  = factory.createFilterParser(reader);
            // the object mapper will return a Java null for JSON null.
            // Have to distinguish between reading a JSON null and encountering
            // the end of string.
            if (parser.getCurrentToken() == null && parser.nextToken() == null) {
              // end of string.
              valueNode = null;
            }
            else {
              valueNode = parser.readValueAsTree();
              // this is actually a JSON null. Use NullNode.
              if (valueNode == null) {
                valueNode = Support.nodeFactory().nullNode();
              }
            }
            // reset back to the beginning of the JSON value.
            reader.reset();
            // skip the number of chars consumed by JSON parser.
            reader.skip(parser.getCurrentLocation().getCharOffset());
          }
          catch (IOException e) {
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_INVALID_COMPARISON_VALUE, reader.mark, e.getMessage()));
          }

          if (valueNode == null)
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

          if (op.equalsIgnoreCase(Filter.Type.EQ.value())) {
            output.push(Filter.eq(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.NE.value())) {
            output.push(Filter.not(Filter.eq(filterAttribute, valueNode)));
          }
          else if (op.equalsIgnoreCase(Filter.Type.CO.value())) {
            output.push(Filter.co(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.SW.value())) {
            output.push(Filter.sw(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.EW.value())) {
            output.push(Filter.ew(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.GT.value())) {
            output.push(Filter.gt(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.GE.value())) {
            output.push(Filter.ge(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.LT.value())) {
            output.push(Filter.lt(filterAttribute, valueNode));
          }
          else if (op.equalsIgnoreCase(Filter.Type.LE.value())) {
            output.push(Filter.le(filterAttribute, valueNode));
          }
          else {
            throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNRECOGNOIZED_OPERATOR, op, reader.mark));
          }
        }
      }
      else {
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_CHARACTER, token, reader.pos, reader.mark));
      }
      previous = token;
    }

    closeGrouping(precedence, output, true);

    if (output.isEmpty())
      throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));

    return output.pop();
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
   ** @param  valueFilter        whether to read the token for a value filter.
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
  private static String readFilterToken(final StringReader reader, final boolean valueFilter)
    throws BadRequestException {

    final Option option = Parser.option();

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
      if (!valueFilter && c == '[') {
        // terminating opening brace. Consume it and return token.
        b.append((char)c);
        return b.toString();
      }
      if (valueFilter && c == ']') {
        if (b.length() > 0) {
          // do not consume the closing brace.
          reader.unread();
        }
        else {
          b.append((char)c);
        }
        return b.toString();
      }
      if (c == '-' || c == '_' || c == '.' || c == ':' || c == '$' || Character.isLetterOrDigit(c) || option.is(c)) {
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
  // Method:   readFilterToken
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
   **                            element is of type {@link Filter}.
   ** @param  isAtTheEnd         whether the end of the filter string was
   **                            reached.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the last operator encountered that signaled the
   **                            end of the group.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws BadRequestException if the filter string could not be parsed.
   */
  private static String closeGrouping(final Stack<String> operators, final Stack<Filter> output, final boolean isAtTheEnd)
    throws BadRequestException {

    String             operator   = null;
    String             repeating  = null;
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
      if (repeating == null) {
        repeating = operator;
      }
      if (!operator.equals(repeating)) {
        if (output.isEmpty()) {
          throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
        }
        components.addFirst(output.pop());
        if (repeating.equalsIgnoreCase(Filter.Type.AND.value())) {
          output.push(Filter.and(components));
        }
        else {
          output.push(Filter.or(components));
        }
        components.clear();
        repeating = operator;
      }
      if (output.isEmpty()) {
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
      }
      components.addFirst(output.pop());
    }

    if (repeating != null && !components.isEmpty()) {
      if (output.isEmpty()) {
        throw BadRequestException.invalidFilter(ServiceBundle.string(ServiceError.PATH_UNEXPECTED_EOF_FILTER));
      }
      components.addFirst(output.pop());
      if (repeating.equalsIgnoreCase(Filter.Type.AND.value())) {
        output.push(Filter.and(components));
      }
      else {
        output.push(Filter.or(components));
      }
    }
    return operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expectNew
  /**
   ** Whether a new filter token is expected given the previous token.
   **
   ** @param   previous          the previous filter token.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    whether a new filter token is expected.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private static boolean expectNew(final String previous) {
    return previous == null || previous.equals("(") || previous.equalsIgnoreCase(Filter.Type.NOT.value()) || previous.equalsIgnoreCase(Filter.Type.AND.value()) || previous.equalsIgnoreCase(Filter.Type.OR.value());
  }
}