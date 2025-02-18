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
    Subsystem   :   Foundation Shared Library

    File        :   JsonDeserializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonDeserializer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

import java.util.Map;

import java.io.Reader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.CharBuffer;

import java.nio.charset.Charset;

////////////////////////////////////////////////////////////////////////////////
// final class JsonDeserializer
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Provides forward, read-only access to JSON data in a streaming way.
 ** This is the most efficient way for reading JSON data.
 ** <br>
 ** The class {@link JsonContext} contains methods to create parsers from
 ** input streams ({@link InputStream} and {@link Reader}).
 ** <p>
 ** The following example demonstrates how to create a parser from a string
 ** that contains an empty JSON array:
 ** <pre>
 **   JsonParser parser = Json.createParser(new StringReader("[]"));
 ** </pre>
 ** <p>
 ** The class {@link JsonContext} also contains methods to create
 ** {@link JsonDeserializer} instances. {@link JsonDeserializer.Factory} is
 ** preferred when creating multiple parser instances. A sample usage is shown
 ** in the following example:
 ** <pre>
 **   JsonDeserializer.Factory factory = JsonContext.readerFactory(config);
 **   JsonDeserializer         parser1 = factory.createParser(...);
 **   JsonDeserializer         parser2 = factory.createParser(...);
 ** </pre>
 ** <p>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class JsonDeserializer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the input data, containing at most one top-level array or object.
   */
  private final Delegate stream;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Stream
  // ~~~~~ ~~~~~~
  /**
   ** Reads a JSON {@link JsonObject} or a {@link JsonArray} structure from an
   ** input source.
   ** <p>
   ** The class {@link javax.json.Json} contains methods to create readers from
   ** input sources ({@link java.io.InputStream} and {@link java.io.Reader}).
   ** <p>
   ** The following example demonstrates how to read an empty JSON array from a
   ** string:
   ** <pre>
   **   JsonReader reader = Json.createReader(new StringReader("[]"));
   **   JsonArray  array  = reader.readArray();
   **   reader.close();
   ** </pre>
   ** The class {@link JsonReaderFactory} also contains methods to create
   ** {@link JsonReader} instances. A factory instance can be used to create
   ** multiple reader instances with the same configuration. This the preferred
   ** way to create multiple instances. A sample usage is shown in the following
   ** example:
   ** <pre>
   **   JsonDeserializer.Factory factory = JsonContext.readerFactory(config);
   **   JsonDeserializer.Stream  reader1 = factory.createReader(...);
   **   JsonDeserializer.Stream  reader2 = factory.createReader(...);
   ** </pre>
   */
  static class Stream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** indicates that the stream is closed already */
    private boolean                closed;

    private final JsonDeserializer serializer;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link InputStream}.
     **
     ** @param  stream           an {@link InputStream} from which JSON is read.
     */
    Stream(final InputStream stream) {
      // ensure inheritance
      this(stream, JsonContext.CHARSET_DEFAULT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link InputStream}.
     ** <br>
     ** The stream is configured with the specified map of provider specific
     ** configuration properties.
     **
     ** @param  stream           an {@link InputStream} to which JSON is read.
     ** @param  charset          a charset for encoding purpose.
     */
    Stream(final InputStream stream, final Charset charset) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.serializer = new JsonDeserializer(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link Reader}.
     **
     ** @param  stream           an {@link Reader} from which JSON is read.
     */
    Stream(final Reader stream) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.serializer = new JsonDeserializer(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: close
    /**
     ** Closes this reader and frees any resources associated with the reader.
     ** <br>
     ** This method closes the underlying input source.
     **
     ** @throws  JsonException   if an i/o error occurs (IOException would be
     **                          cause of JsonException).
     */
    public final void close() {
     // prevent further usage
      this.closed = true;
      this.serializer.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read (JsonReader)
    /**
     ** Returns a JSON array or object that is represented in the input source.
     ** <br>
     ** This method needs to be called only once for a reader instance.
     **
     ** @return                  a JSON object or array instance.
     **
     ** @throws JsonException        if a JSON object or array cannot be
     **                              created due to i/o error (IOException
     **                              would be cause of JsonException)
     ** @throws JsonParsingException if a JSON object or array cannot be
     **                               created due to incorrect representation.
    ** @throws IllegalStateException if read, readObject, readArray or close
    **                               method is already called.
     */
    public final JsonValue read() {
      // prevent bogus state
      if (this.closed)
        throw new IllegalStateException("read/readObject/readArray/close method is already called.");

      // prevent further usage
      this.closed = true;

      // fetch into
      if (this.serializer.hasNext()) {
      }
      throw new JsonException("Cannot read JSON, possibly empty stream");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Factory
  // ~~~~~ ~~~~~~~
  /**
   ** Factory to create {@link JsonReader} instances.
   ** <br>
   ** If a factory instance is configured with some configuration, that would be
   ** used to configure the created reader instances.
   ** <p>
   ** {@link JsonReader} can also be created using {@link Json}'s
   ** <code>createReader</code> methods. If multiple reader instances are
   ** created, then creating them using a reader factory is preferred.
   ** <p>
   ** <b>For example:</b>
   ** <pre>
   **   JsonReaderFactory factory = Json.createReaderFactory(...);
   **   JsonReader        reader1 = factory.createReader(...);
   **   JsonReader        reader2 = factory.createReader(...);
   ** </pre>
   ** <p>
   ** All the methods in this class are safe for use by multiple concurrent
   ** threads.
   */
  static final class Factory {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Map<String, ?> config;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a JSON <code>Factory</code> that creates instances of
     ** {@link JsonReader}.
     ** <br>
     ** The {@link JsonReader}s created by this factory are configured with the
     ** specified map of provider specific configuration properties.
     **
     ** @param  config           a {@link Map} of provider specific properties
     **                          to configure the JSON reader.
     **                          The {@link Map} may be empty or
     **                          <code>null</code>.
     */
    Factory(final Map<String, ?> config) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.config = config;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: config
    /**
     ** Returns a read-only map of supported provider specific configuration
     ** properties that are used to configure the JSON builders.
     ** <br>
     ** If there are any specified configuration properties that are not
     ** supported by the provider, they won't be part of the returned
     ** {@link Map}.
     **
     ** @return                  a {@link Map} of supported provider specific
     **                          properties that are used to configure the
     **                          created builders.
     **                          The {@link Map} may be empty but not
     **                          <code>null</code>.
     */
    public final Map<String, ?> config() {
      return this.config;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createParser
    /**
     ** Creates a JSON parser from the specified byte stream.
     ** <br>
     ** The character encoding of the stream is determined as specified in
     ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
     **
     ** @param  stream           a byte stream from which JSON is to be read.
     **
     ** @throws JsonException    if encoding cannot be determined or i/o error
     **                          (IOException would be cause of JsonException).
     */
    public final JsonDeserializer createParser(final InputStream stream) {
      return new JsonDeserializer(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createParser
    /**
     ** Creates a JSON parser from the specified byte stream.
     ** <br>
     ** The bytes of the stream are decoded to characters using the specified
     ** charset.
     **
     ** @param  stream           a byte stream from which JSON is to be read.
     ** @param  charset          a charset for decoding purpose.
     **
     ** @throws JsonException    if encoding cannot be determined or i/o error
     **                          (IOException would be cause of JsonException).
     */
    public final JsonDeserializer createParser(final InputStream stream, final Charset charset) {
      return new JsonDeserializer(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createReader (JsonReaderFactory)
    /**
     ** Creates a JSON reader from a byte stream.
     ** <br>
     ** The byte decoding of the stream is determined as described in
     ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
     **
     ** @param  stream           a byte stream from which JSON is to be read.
     **
     ** @return                  a JSON reader.
     */
    public final JsonDeserializer.Stream createReader(final InputStream stream) {
      return new JsonDeserializer.Stream(stream);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   createReader (JsonReaderFactory)
    /**
     ** Creates a JSON reader from a byte stream.
     ** <br>
     ** The bytes of the stream are decoded to characters using the specified
     ** charset.
     **
     ** @param  stream           a byte stream from which JSON is to be read.
     ** @param  charset          a charset for deconding purpose.
     **
     ** @return                  a JSON reader.
     */
    public final JsonDeserializer.Stream createReader(final InputStream stream, final Charset charset) {
      return new Stream(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createReader
    /**
     ** Creates a JSON reader from a character stream.
     **
     ** @param  stream           a reader from which JSON is to be read.
     **
     ** @return                  a JSON reader.
     */
    public final JsonDeserializer.Stream createReader(final Reader stream) {
      return new JsonDeserializer.Stream(stream);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Delegate
  // ~~~~~ ~~~~~~~~
  /**
   **
   */
  private class Delegate {

    /** Buffer a whole lot of long values at the same time. */
    static final int BUFFSIZE = 0x800 * 8; // 8192
    static final int DATASIZE = 0x8000 * BUFFSIZE;
    /**
     * Maximum buffer size for map files.
     */
    static final int MAXIMUM_BUFFER_SIZE = 8000000;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the underliying character stream */
    private final Reader     stream;

    /** the character buffer in an approriate size */
    private final CharBuffer buffer;

    /** the length of the character buffer fetched from the underlying stream */
    private int              length;

    /** the actual position in the character buffer */
    private int              position = 0;

    /** the actual JSON event the input stream */
    private long             offset   = 0;

    /** the line number of the current JSON event in the input stream */
    private long             line     = 1;

    /** the column number of the current JSON event in the input stream */
    private long             column   = 1;

    /** the last char successfully taken from the stream */
    private char             last;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default character stream <code>Delegate</code> thats backed
     ** by the specified {@link Reader}.
     ** <br>
     ** The buufer used has a fixed size of <code>8192</cde> ccharacters.
     **
     ** @param  stream           the {@link Reader} to backing this
     **                         {@link Reader}.
     */
    Delegate(Reader stream) {
      // ensure inheritance
      this(stream, BUFFSIZE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default character stream <code>Delegate</code> thats backed
     ** by the specified {@link Reader}.
     **
     ** @param  stream           the {@link Reader} to backing this
     **                          {@link Reader}.
     ** @param  size             the ubuffer sizeto allocate.
     */
    Delegate(Reader stream, final int size) {
      // ensure inheritance
      super();

      if (!(stream instanceof BufferedReader))
        stream = new BufferedReader(stream);

      // initialize instance attributes
      this.stream = stream;
      this.buffer = CharBuffer.allocate(size).asReadOnlyBuffer();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   hasRemaining
    /**
     ** Tells whether there are any elements between the current position and
     ** the limit.
     **
     ** @return                  <code>true</code> if, and only if, there is at
     **                          least one element remaining in the character
     **                          buffer.
     */
    public final boolean hasRemaining() {
      return this.buffer.hasRemaining();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (overridden)
    /**
     ** Closes the stream and releases any system resources associated with it.
     ** <br>
     ** Once the stream has been closed, further read(), ready(), mark(),
     ** reset(), or skip() invocations will throw an IOException.
     ** <br>
     ** Closing a previously closed stream has no effect.
     **
     ** @throws IOException       if an I/O error occurs
     */
    public final void close()
      throws IOException {

      this.stream.close();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   startCapture
    /**
     ** Starts capturing the character stream by setting the buffer pointer
     ** accordingly.
     */
    private void startCapture() {
/*
      if (this.captureBuffer == null)
        this.captureBuffer = new StringBuilder();

      this.captureStart = this.index - 1;
*/
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
      int    end = this.length == -1 ? this.position : this.position - 1;
      String captured;
      /*
      if (this.captureBuffer.length() > 0) {
        this.captureBuffer.append(this.buffer, this.captureStart, end - this.captureStart);
        captured = this.captureBuffer.toString();
        this.captureBuffer.setLength(0);
      }
      else {
        captured = new String(this.buffer, this.captureStart, end - this.captureStart);
      }
      this.captureStart = -1;
      */
      return "";//captured;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: suspendCapture
    /**
     ** Stops capturing the character stream by setting the buffer pointer
     ** accordingly and resets the buffer pointer.
     */
    private void suspendCapture() {
      int end = this.length == -1 ? this.position : this.position - 1;
//      this.captureBuffer.append(this.buffer, this.captureStart, end - this.captureStart);
//      this.captureStart = -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: next()
    /**
     ** Reads the next byte of data from the input stream.
     ** <br>
     ** The value byte is returned as an <code>int</code> in the range
     ** <code>0</code> to <code>255</code>. If no byte is available because the
     ** end of the stream has been reached, the value <code>-1</code> is returned.
     ** This method blocks until input data is available, the end of the stream is
     ** detected, or an exception is thrown.
     */
    private char next()
      throws IOException {

      // verify if enough chaacters are in the buffer
      if (this.position == this.length)
        read();

      char ch = this.buffer.get();
      if (ch == '\r' || (this.last != '\r' && ch == '\n')) {
        ++this.line;
        this.column = 1;
      }
      else if (this.last != '\r' || ch != '\n') {
        ++this.column;
      }
      this.offset++;
      this.position++;
      this.last = ch;
      return this.last;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   consumeEOL
    /**
     ** Advances the position until after the next newline character.
     */
    private void consumeEOL()
      throws IOException {

      int c = next();
      if (c == '\r' || c == '\n') {
        consumeWhiteSpace();
        return;
      }
      consumeEOL();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   consumeWhiteSpace
    /**
     ** Move forward the capture buffer pointer to the next regular character.
     */
    private void consumeWhiteSpace() {
      char ch;
      while (true) {
        ch = this.buffer.get();
        switch (ch) {
          case ' '  :
          case '\t' :
          case '\r' :
          case '\n' : continue;
          default   : pushBack();
                      return;
        }
      }
    }

    private void pushBack() {
      // remember the character and adjust the buffer pointer
      this.last = this.buffer.get();
      this.offset--;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: read
    /**
     ** Reads a portion of characters in an array.
     **
     ** @param  buffer           an array of characters to be read into.
     ** @param  offset           the offset at which to start storing
     **                          characters.
     ** @param  length           the maximum number of characters to read.
     **
     ** @return                  the number of characters read, or
     **                          <code>-1</code> if the end of the stream has
     **                          been reached
     **
     ** @throws IOException      if an I/O error occurs
     */
    public final void read()
      throws IOException {

      this.length   = this.stream.read(this.buffer);
      this.position = 0;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonDeserializer</code> reader from a byte stream.
   ** <br>
   ** The character encoding of the stream is determined as described in
   ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   */
  JsonDeserializer(final InputStream stream) {
    // ensure inheritance
    super();

    // initialize instance attributes
    final FilterUnicodeStream filter = new FilterUnicodeStream(stream);
    this.stream = new Delegate(new InputStreamReader(filter, filter.charset()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonDeserializer</code> reader from a byte stream.
   ** <br>
   ** The bytes of the stream are decoded to characters using the specified
   ** charset.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   ** @param  charset            a charset for decoding purpose.
   */
  JsonDeserializer(final InputStream stream, final Charset charset) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.stream = new Delegate(new InputStreamReader(stream, charset));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonDeserializer</code> reader from a character
   ** stream.
   **
   ** @param  stream             a character stream from which JSON is to be
   **                            read.
   */
  JsonDeserializer(final Reader stream) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.stream = new Delegate(stream);
  }

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
      throw new JsonException("I/O error while closing JSON tokenizer", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext
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
    return this.stream.last != -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next
  /**
   ** Returns the [@link JsonValue} for the next parsing state.
   **
   ** @return                        the next parsed {@link JsonValue}.
   **
   ** @throws JsonException          if an i/o error occurs (IOException would
   **                                be cause of JsonException).
   ** @throws JsonParserException    if the parser encounters invalid JSON when
   **                                advancing to next state.
   */
  public final JsonValue next()
    throws JsonException
    ,      JsonParserException {

    try {
      this.stream.consumeWhiteSpace();
      char token = this.stream.next();
      switch(token) {
//        case -1   : throw expected("");
        case '['  : return readArray();
        case '{'  : return readObject();
  /*
        case '\'' :
        case '"'  : return readString((char)ch);
  */
        default   : throw error(JsonParserException.UNEXPECTED_TOKEN, this);
      }
    }
    catch (IOException e) {
      throw fatal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readArray
  /**
   ** Reads a sequence of values and the trailing closing brace ']' of an array.
   ** The opening brace '[' should have already been consumed. Note that "[]"
   ** yields an empty array, but "[,]" returns a two-element array equivalent to
   ** "[null,null]".
   **
   ** @return                    the parsed {@link JsonArray}.
   **
   ** @throws JsonException       if an i/o error occurs (IOException would be
   **                             cause of JsonException).
   ** @throws JsonParserException if the parser encounters invalid JSON when
   **                             advancing to next state.
   */
  private JsonArray readArray()
    throws JsonException
    ,      JsonParserException {

    final JsonArray result = new JsonArray();
    // to cover input that ends with ",]".
    boolean hasTrailingSeparator = false;
    try {
      while (true) {
        switch (this.stream.next()) {
//          case -1  : throw expected("Unterminated array");
          case ']' : if (hasTrailingSeparator) {
                       result.add(JsonLiteral.NULL);
                      }
                      return result;
          case ',' :
          case ';' : // a separator without a value first means "null".
                     result.add(JsonLiteral.NULL);
                     hasTrailingSeparator = true;
                     continue;
        }
        result.add(next());
        switch (this.stream.next()) {
          case ']' : return result;
          case ',' :
          case ';' : hasTrailingSeparator = true;
                     continue;
          default  : throw expected("Unterminated array");
        }
      }
    }
    catch (IOException e) {
      throw fatal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Reads a sequence of key/value pairs and the trailing closing brace '}' of
   ** an object. The opening brace '{' should have already been consumed.
   **
   ** @return                    the parsed {@link JsonObject}.
   **
   ** @throws JsonException       if an i/o error occurs (IOException would be
   **                             cause of JsonException).
   ** @throws JsonParserException if the parser encounters invalid JSON when
   **                             advancing to next state.
   */
  private JsonObject readObject()
    throws JsonException
    ,      JsonParserException {

    final JsonObject result = new JsonObject();
    try {
      // peek to see if this is the empty object
      char first = this.stream.next();
      if (first == '}')
        return result;

      while (true) {
        Object name = next();
        if (!(name instanceof String)) {
          if (name == null) {
            throw expected("Names cannot be null");
          }
          else {
            throw expected("Names must be strings, but " + name + " is of type " + name.getClass().getName());
          }
        }
        // expect the name/value separator to be either a colon ':', an
        // equals sign '=', or an arrow "=>".
        // the last two are bogus but we include them because that's what the
        // original implementation did.
        char separator = this.stream.next();
        if (separator != ':' && separator != '=') {
          throw expected("Expected ':' after " + name);
        }

        result.add((String)name, next());
        switch (this.stream.next()) {
          case '}' : return result;
          case ';' :
          case ',' : continue;
          default  : throw expected("Unterminated object");
        }
      }
    }
    catch (IOException e) {
      throw fatal(e);
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

    expect('u');
    expect('l');
    expect('l');
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

    expect('r');
    expect('u');
    expect('e');
    return JsonLiteral.TRUE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFalse
  /**
   ** Parse a payload in JSON format to return the a JSON literal
   ** <code>false</code>.
   */
  private JsonValue readFalse()
    throws IOException {

    expect('a');
    expect('l');
    expect('s');
    expect('e');
    return JsonLiteral.FALSE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readString
  /**
   ** Parse a payload in JSON format to return the a JSON string.
   **
   ** @return                    the appropriate {@link JsonValue}.
   */
  /*
  private JsonValue readString()
    throws IOException {

    return new JsonString(readStringInternal());
  }
  */
  //////////////////////////////////////////////////////////////////////////////
  // Method:   readStringInternal
  /**
   ** Parse a payload in JSON format to return the a string.
   **
   ** @return                    the appropriate {@link String}.
   */
  /*
  private String readStringInternal(final char quote)
    throws IOException {

    consume(quote);
    while (this.stream.last != quote) {
      if (this.current == '\\') {
        readEscape();
      }
      else if (this.stream.last < 0x20)
        throw expected("valid string character");
      else
        read();
    }
    read();
    return this.stream.token();
  }
  */

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readEscape
  /**
   ** Reads the next portion form the the character stream in the capture
   ** buffer to append the control charactes in the capturing buffer.
   */
/*
  private void readEscape()
    throws IOException {

    read();

    switch (this.stream.last) {
      case '"'  :
      case '/'  :
      case '\\' : this.captureBuffer.append((char)this.current);
                  break;
      case 'b'  : this.captureBuffer.append('\b');
                  break;
      case 'f'  : this.captureBuffer.append('\f');
                  break;
      case 'n'  : this.captureBuffer.append('\n');
                  break;
      case 'r'  : this.captureBuffer.append('\r');
                  break;
      case 't'  : this.captureBuffer.append('\t');
                  break;
      case 'u'  : char[] hexChars = new char[4];
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
*/
  //////////////////////////////////////////////////////////////////////////////
  // Method:   readNumber
  /**
   ** Parse a payload in JSON format to return the a JSON number.
   */
  private void readNumber()
    throws IOException {

    consume('-');
    int firstDigit = this.stream.last;
    if (!readDigit())
      throw expected("digit");

    if (firstDigit != '0')
      while (readDigit());

    readFractal();
    readExponent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readFractal
  /**
   ** Parse a payload in JSON format to return the fractional part of a JSON
   ** number.
   **
   ** @return                    <code>true</code> if a fractional part was
   **                            detected; otherwise <code>false</code>.
   */
  private void readFractal()
    throws IOException {

    if (!consume('.'))
      return;

    if (!readDigit())
      throw expected("digit");

    while (readDigit());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readExponent
  /**
   ** Parse a payload in JSON format to return the exponent of a JSON number.
   */
  private void readExponent()
    throws IOException {

    if (!consume('e') && !consume('E'))
      return;

    if (!consume('+'))
      consume('-');

    if (!readDigit())
      throw expected("digit");

    while (readDigit());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readDigit
  private boolean readDigit()
    throws IOException {

    if (!digit())
      return false;

    this.stream.next();
    return true;
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
    switch (this.stream.last) {
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
    switch (this.stream.last) {
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
  // Method:   expect
  /**
   ** Parse a payload in JSON format and return the appropriate key/value
   ** mapping.
   */
  private void expect(final char ch) {
    if (!consume(ch))
      throw expected("'" + ch + "'");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   consume
  private boolean consume(final char ch) {
    if (this.stream.last != ch)
      return false;

    this.stream.position++;
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expected
  /**
   ** Handles unexcepted parsing result
   **
   ** @param  expected           the token which was expected but not obtained
   **                            from the character stream.
   **
   ** @return                    a {@link JsonParserException} with the proper
   **                            reason.
   */
  private JsonParserException expected(final String expected) {
    return this.stream.last == -1 ? error(JsonParserException.UNEXPECTED_EOF, this.stream.last) : error(JsonParserException.UNEXPECTED_TOKEN, expected);
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
    return new JsonParserException(this.stream.offset, this.stream.line, this.stream.column - 1, type, subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Handles unexcepted i/o result
   **
   ** @param  subject            the subject of the error message to throw.
   **
   ** @return                    a {@link JsonException} with the proper
   **                            cause.
   */
  private JsonException fatal(final Throwable subject) {
    return new JsonException(subject);
  }
}