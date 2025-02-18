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

    File        :   JsonParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonParser.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.json.simple;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.InputStreamReader;

import java.math.BigDecimal;

import java.nio.charset.Charset;

////////////////////////////////////////////////////////////////////////////////
// class JsonParser
// ~~~~~ ~~~~~~~~~~
/**
 ** Reading and writing JSON are critical operations, since the server
 ** processes and creates JSON messages for a large number of clients at a
 ** high rate. For this reason, we need something fast for this job. When we
 ** switched to JSON, we included the org.codehaus.jackson parser, which is
 ** reasonably small but not famous for its performance.
 ** <p>
 ** There are many better JSON libraries out there, but most do much more than
 ** we need. We really only need a bare-bones parser that can read JSON into a
 ** simple Java representation and generate JSON from Java. As we like to keep
 ** the core library self-contained, we don't want a dependency to an external
 ** JSON library.
 ** <p>
 ** Why is JSON parsing so easy? That's because the first character of every
 ** token uniquely defines its type ('<code>[</code>' for an array,
 ** '<code>"</code>' for a string, '<code>t</code>' or '<code>f</code>' for a
 ** boolean, and so forth). There's no backtracking involved. It went so well
 ** that I decided to continue and create a JSON parser tailored to our need.
 ** Which are:
 ** <ul>
 **   <li><b>Fast</b>           - we read and create so much JSON that the parser
 **                               directly affects the server performance.
 **   <li><b>Lightweight</b>    - it should deal with memory sparingly as we deal
 **                               with lots of messages.
 **  <li><b>Minimal</b>         - the less code the better, as we have to maintain
 **                               it.
 **  <li><b>Simpleto use</b>    - we'll expose the API for custom component
 **                               developers, so it should be simple and clear.
 **  <li><b>No dependencies</b> - only Java 5
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonParser {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int MIN_BUFFER_SIZE     = 10;
  private static final int DEFAULT_BUFFER_SIZE = 1024;

  private final Reader     stream;
  private final char[]     buffer;
  private int              bufferOffset;
  private int              index;
  private int              limit;
  private int              line;
  private int              lineOffset;
  private int              current;
  private StringBuilder    captureBuffer;
  private int              captureStart;

  /*
   * |                      bufferOffset
   *                        v
   * [a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t]        < input
   *                       [l|m|n|o|p|q|r|s|t|?|?]    < buffer
   *                          ^               ^
   *                       |  index           fill
   */

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a JSON character-stream reader whose parse the given string.
   **
   ** @param  payload            the payload to parse.
   */
  JsonParser(final String payload) {
    this(new StringReader(payload), Math.max(MIN_BUFFER_SIZE, Math.min(DEFAULT_BUFFER_SIZE, payload.length())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonParser</code> reader from a byte stream.
   ** <br>
   ** The character encoding of the stream is determined as described in
   ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   */
  JsonParser(final InputStream stream) {
    // ensure inheritance
    this(stream, DEFAULT_BUFFER_SIZE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonParser</code> reader from a byte stream.
   ** <br>
   ** The character encoding of the stream is determined as described in
   ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   ** @param  buffersize         the amount of memory aquired for parsing.
   */
  JsonParser(final InputStream stream, final int buffersize) {
    // ensure inheritance
    super();

    // initialize instance attributes
    final JsonUnicodeFilterStream filter = new JsonUnicodeFilterStream(stream);
    this.stream       = new InputStreamReader(filter, filter.charset());
    this.buffer       = new char[buffersize];
    this.line         = 1;
    this.captureStart = -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonParser</code> reader from a byte stream.
   ** <br>
   ** The bytes of the stream are decoded to characters using the specified
   ** charset.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   ** @param  charset            a charset for decoding purpose.
   */
  JsonParser(final InputStream stream, final Charset charset) {
    // ensure inheritance
    this(new InputStreamReader(stream, charset));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a JSON character-stream reader whose delegates is the given
   ** {@link Reader}.
   **
   ** @param  reader             the delegate used for the operations.
   */
  JsonParser(final Reader reader) {
    this(reader, DEFAULT_BUFFER_SIZE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a JSON character-stream reader whose delegates is the given
   ** {@link Reader}.
   **
   ** @param  reader             the delegate used for the operations.
   ** @param  buffersize         the amount of memory aquired for parsing.
   */
  JsonParser(final Reader reader, final int buffersize) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.stream       = reader;
    this.buffer       = new char[buffersize];
    this.line         = 1;
    this.captureStart = -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext (JsonParser)
  /**
   ** Returns <code>true</code> if there are more parsing states.
   ** <br>
   ** This method returns <code>false</code> if the parser reaches the end of
   ** the JSON text.
   **
   ** @return                    <code>true</code> if there are more parsing
   **                            states; otherwise <code>false</code>.
   **
   ** @throws JsonException       if an i/o error occurs (IOException would be
   **                             cause of JsonException).
   ** @throws JsonParserException if the parser encounters invalid JSON when
   **                             advancing to next state.
   */
  public final boolean hasNext() {
    return !eot();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Closes this parser and frees any resources associated with the parser.
   ** <br>
   ** This method closes the underlying input source.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public final void close()
    throws JsonException {

    try {
      this.stream.close();
    }
    catch (IOException e) {
      throw new JsonException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Parse a payload in JSON format and return the appropriate key/value
   ** mapping.
   **
   ** @return                    the appropriate key/value mapping.
   **
   ** @throws JsonException       if an i/o error occurs (IOException would
   **                             be cause of JsonException).
   ** @throws JsonParserException if the parser encounters invalid JSON when
   **                             advancing to next state.
   */
  public JsonValue next()
    throws JsonException
    ,      JsonParserException {

    JsonValue result = null;
    try {
      read();
      skipWhiteSpace();
      result = readValue();
      skipWhiteSpace();
      if (!eot())
        throw error(JsonParserException.UNEXPECTED_CHAR, this.current);
    }
    catch (IOException e) {
      throw new JsonException(e);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readValue
  /**
   ** Parse a payload in JSON format to return the a JSON value.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readValue()
    throws IOException {

    switch (this.current) {
      case 'n' : return readNull();
      case 't' : return readTrue();
      case 'f' : return readFalse();
      case '"' : return readString();
      case '[' : return readArray();
      case '{' : return readObject();
      case '-' :
      case '0' :
      case '1' :
      case '2' :
      case '3' :
      case '4' :
      case '5' :
      case '6' :
      case '7' :
      case '8' :
      case '9' : return readNumber();
      default  : throw expectedToken("value");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readNull
  /**
   ** Parse a payload in JSON format to return the a JSON literal.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readNull()
    throws IOException {

    read();
    readRequiredChar('u');
    readRequiredChar('l');
    readRequiredChar('l');
    return JsonLiteral.NULL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readTrue
  /**
   ** Parse a payload in JSON format to return the a JSON literal.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readTrue()
    throws IOException {

    read();
    readRequiredChar('r');
    readRequiredChar('u');
    readRequiredChar('e');
    return JsonLiteral.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFalse
  /**
   ** Parse a payload in JSON format to return the a JSON literal.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readFalse()
    throws IOException {

    read();
    readRequiredChar('a');
    readRequiredChar('l');
    readRequiredChar('s');
    readRequiredChar('e');
    return JsonLiteral.FALSE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readString
  /**
   ** Parse a payload in JSON format to return the a JSON string.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readString()
    throws IOException {

    return new JsonString(readStringInternal());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readNumber
  /**
   ** Parse a payload in JSON format to return the a JSON number.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonValue readNumber()
    throws IOException {

    startCapture();
    readChar('-');
    int firstDigit = this.current;
    if (!readDigit())
      throw expectedToken("digit");

    if (firstDigit != '0')
      while (readDigit());

    fractal();
    exponent();
    return new JsonBigDecimal(new BigDecimal(endCapture()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readArray
  /**
   ** Parse a payload in JSON format to return the a JSON array.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonArray readArray()
    throws IOException {

    read();
    final JsonArray array = new JsonArray();
    skipWhiteSpace();
    if (readChar(']'))
      return array;

    do {
      skipWhiteSpace();
      array.add(readValue());
      skipWhiteSpace();
    } while (readChar(','));

    if (!readChar(']'))
      throw expectedToken("',' or ']'");

    return array;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Parse a payload in JSON format to return the a JSON object.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  private JsonObject readObject()
    throws IOException {

    read();
    JsonObject object = new JsonObject();
    skipWhiteSpace();
    if (readChar('}'))
      return object;

    do {
      skipWhiteSpace();
      String name = readName();
      skipWhiteSpace();
      if (!readChar(':'))
        throw expectedToken("':'");

      skipWhiteSpace();
      object.add(name, readValue());
      skipWhiteSpace();
    } while (readChar(','));

    if (!readChar('}'))
      throw expectedToken("',' or '}'");

    return object;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readName
  /**
   ** Parse a the value name from a payload in JSON format and return the key.
   **
   ** @return                    the appropriate key for later retrieval.
   */
  private String readName()
    throws IOException {

    if (this.current != '"')
      throw expectedToken("name");

    return readStringInternal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readStringInternal
  /**
   ** Parse a payload in JSON format to return the a string.
   **
   ** @return                    the appropriate {@link String}.
   */
  private String readStringInternal()
    throws IOException {

    read();
    startCapture();
    while (this.current != '"') {
      if (this.current == '\\') {
        suspendCapture();
        readEscape();
        startCapture();
      }
      else if (this.current < 0x20)
        throw expectedToken("valid string character");
      else
        read();
    }
    final String string = endCapture();
    read();
    return string;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fractal
  /**
   ** Parse a payload in JSON format to return the fractional part of a JSON
   ** number.
   **
   ** @return                    <code>true</code> if a fractional part was
   **                            detected; otherwise <code>false</code>.
   */
  private boolean fractal()
    throws IOException {

    if (!readChar('.'))
      return false;

    if (!readDigit())
      throw expectedToken("digit");

    while (readDigit());
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exponent
  /**
   ** Parse a payload in JSON format to return the exponent of a JSON number.
   **
   ** @return                    <code>true</code> if an exponent was detected;
   **                            otherwise <code>false</code>.
   */
  private boolean exponent()
    throws IOException {

    if (!readChar('e') && !readChar('E'))
      return false;

    if (!readChar('+'))
      readChar('-');

    if (!readDigit())
      throw expectedToken("digit");

    while (readDigit());
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readRequiredChar
  /**
   ** Parse a payload in JSON format and return the appropriate key/value
   ** mapping.
   */
  private void readRequiredChar(final char ch)
    throws IOException {

    if (!readChar(ch))
      throw expectedToken("'" + ch + "'");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readChar
  private boolean readChar(final char ch)
    throws IOException {

    if (this.current != ch)
      return false;

    read();
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readDigit
  private boolean readDigit()
    throws IOException {

    if (!digit())
      return false;

    read();
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skipWhiteSpace
  /**
   ** Move forward the capture buffer pointer to the next regular character.
   */
  private void skipWhiteSpace()
    throws IOException {

    while (isWhiteSpace())
      read();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEscape
  /**
   ** Reads the next portion form the the character stream in the capture
   ** buffer to append the control charactes in the capturing buffer.
   */
  private void readEscape()
    throws IOException {
    read();

    switch (this.current) {
      case '"' : case '/': case '\\': this.captureBuffer.append((char)this.current);
                 break;
      case 'b' : this.captureBuffer.append('\b');
                 break;
      case 'f' : this.captureBuffer.append('\f');
                 break;
      case 'n' : this.captureBuffer.append('\n');
                 break;
      case 'r' : this.captureBuffer.append('\r');
                 break;
      case 't' : this.captureBuffer.append('\t');
                 break;
      case 'u' : char[] hexChars = new char[4];
                 for (int i = 0; i < 4; i++) {
                   read();
                   if (!hexDigit())
                    throw expectedToken("hexadecimal digit");
                   hexChars[i] = (char)current;
                 }
                 this.captureBuffer.append((char)Integer.parseInt(String.valueOf(hexChars), 16));
                 break;
      default  : throw expectedToken("valid escape sequence");
    }
    read();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Reads the next portion form the the character stream in the capture
   ** buffer.
   */
  private void read()
    throws IOException {

    if (eot())
      throw error(JsonParserException.UNEXPECTED_EOF, this.current);

    if (this.index == this.limit) {
      if (this.captureStart != -1) {
        this.captureBuffer.append(this.buffer, this.captureStart, this.limit - this.captureStart);
        this.captureStart = 0;
      }
      this.bufferOffset += this.limit;
      this.limit  = this.stream.read(this.buffer, 0, this.buffer.length);
      this.index = 0;
      if (this.limit == -1) {
        this.current = -1;
        return;
      }
    }
    if (this.current == '\n') {
      this.line++;
      this.lineOffset = this.bufferOffset + this.index;
    }
    this.current = this.buffer[this.index++];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startCapture
  /**
   ** Starts capturing the character stream by setting the buffer pointer
   ** accordingly.
   */
  private void startCapture() {
    if (this.captureBuffer == null)
      this.captureBuffer = new StringBuilder();

    this.captureStart = this.index - 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endCapture
  /**
   ** Stops capturing the character stream by setting the buffer pointer
   ** accordingly and returns the captured results.
   **
   ** @return                    the string captured from the character stream
   **                            during parsing.
   */
  private String endCapture() {
    int end = this.current == -1 ? this.index : this.index - 1;
    String captured;
    if (this.captureBuffer.length() > 0) {
      this.captureBuffer.append(this.buffer, this.captureStart, end - this.captureStart);
      captured = this.captureBuffer.toString();
      this.captureBuffer.setLength(0);
    }
    else {
      captured = new String(this.buffer, this.captureStart, end - this.captureStart);
    }
    this.captureStart = -1;
    return captured;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   suspendCapture
  /**
   ** Stops capturing the character stream by setting the buffer pointer
   ** accordingly and resets the buffer pointer.
   */
  private void suspendCapture() {
    int end = this.current == -1 ? this.index : this.index - 1;
    this.captureBuffer.append(this.buffer, this.captureStart, end - this.captureStart);
    this.captureStart = -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expectedToken
  /**
   ** Handles unexcepted parsing result
   **
   ** @param  expected           the token which was expected but not obtained
   **                            from the character stream.
   **
   ** @return                    a {@link JsonParserException} with the proper
   **                            reason.
   */
  private JsonParserException expectedToken(final String expected) {
    return eot() ? error(JsonParserException.UNEXPECTED_EOF, this.current) : error(JsonParserException.UNEXPECTED_TOKEN, expected);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Handles unexcepted parsing result
   **
   ** @param  type               the type of error message to throw.
   ** @param  subject            the subject of the error message to throw.
   **
   ** @return                    a {@link ParseException} with the proper
   **                            reason.
   */
  private JsonParserException error(final int type, final Object subject) {
    final int absolute = this.bufferOffset + this.index;
    final int column   = absolute - this.lineOffset;
    final int offset   = eot() ? absolute : absolute - 1;
    return new JsonParserException(offset, this.line, column - 1, type, subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isWhiteSpace
  /**
   ** Detects whether the current character in the stream is a white space.
   **
   ** @return                    <code>true</code> if the current character in
   **                            the stream is a white space; <code>false</code>
   **                            otherwise.
   */
  private boolean isWhiteSpace() {
    switch (this.current) {
      // intentionally falling through
      // all white space characters are treated the same
      case ' '  :
      case '\r' :
      case '\n' :
      case '\t' : return true;
      default   : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexDigit
  /**
   ** Detects whether the current character in the stream is a hexadecimal
   ** digit.
   **
   ** @return                    <code>true</code> if the current character in
   **                            the stream is a hexadecimal digit;
   **                            <code>false</code> otherwise.
   */
  private boolean hexDigit() {
    if (digit())
      return true;
    switch (this.current) {
      // intentionally falling through
      // all hexadecimal characters are treated the same
      case 'a' :
      case 'b' :
      case 'c' :
      case 'd' :
      case 'e' :
      case 'f' :
      case 'A' :
      case 'B' :
      case 'C' :
      case 'D' :
      case 'E' :
      case 'F' : return true;
      default  : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digit
  /**
   ** Detects whether the current character in the stream is a digit.
   **
   ** @return                    <code>true</code> if the current character in
   **                            the stream is a digit; <code>false</code>
   **                            otherwise.
   */
  private boolean digit() {
    switch (this.current) {
      // intentionally falling through
      // all digit characters are treated the same
      case '0' :
      case '1' :
      case '2' :
      case '3' :
      case '4' :
      case '5' :
      case '6' :
      case '7' :
      case '8' :
      case '9' :
      case '.' : return true;
      default  : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eot
  /**
   ** Detects whether there isn't any character available anymore.
   **
   ** @return                    <code>true</code> if there isn't any character
   **                            available anymore.
   */
  private boolean eot() {
    return this.current == -1;
  }
}