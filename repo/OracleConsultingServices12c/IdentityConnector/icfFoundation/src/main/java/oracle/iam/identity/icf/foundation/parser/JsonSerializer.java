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

    File        :   JsonSerializer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonSerializer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

import java.math.BigInteger;
import java.math.BigDecimal;

import java.util.Map;
import java.util.Stack;
import java.util.Iterator;

import java.util.concurrent.atomic.AtomicLong;

import java.io.Writer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import java.nio.charset.Charset;

import oracle.iam.identity.icf.foundation.object.Pair;

////////////////////////////////////////////////////////////////////////////////
// final class JsonSerializer
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Writes JSON data to an output source in a streaming way.
 ** <br>
 ** The class {@link JsonContext} contains methods to create serializers for
 ** character or output streams ({@link OutputStream} and {@link Writer}).
 ** <p>
 ** The following example shows how to create a JSON serializer:
 ** <pre>
 **   JsonSerializer serializer = JsonContext.createSerializer(...);
 ** </pre>
 ** The class {@link JsonSerializer.Factory} also contains methods to create
 ** {@link JsonSerializer} instances. {@link JsonSerializer.Factory} should be
 ** used when creating multiple serializer instances, as in the following
 ** example:
 ** <pre>
 **   JsonSerializerFactory factory     = JsonContext.createSerializerFactory();
 **   JsonSerializer        serializer1 = factory.createSerializer(...);
 **   JsonSerializer        serializer2 = factory.createSerializer(...);
 ** </pre>
 ** JSON objects can be created using {@link JsonSerializer} by calling the
 ** {@link #writeStartObject()} method and then adding name/value pairs with the
 ** <code>write</code> method.
 ** <p>
 ** The following example shows how to generate an empty JSON object:
 ** <pre>
 **   JsonSerializer serializer = ...;
 **   serializer.writeStartObject().writeEnd().close();
 ** </pre>
 ** JSON arrays can be created using {@link JsonSerializer} by calling the
 ** {@link #writeStartArray()} method and then adding values with the
 ** <code>write</code> method.
 ** <p>
 ** The following example shows how to generate an empty JSON array:
 ** <pre>
 **   JsonSerializer serializer = ...;
 **   serializer.writeStartArray().writeEnd().close();
 ** </pre>
 ** {@link JsonSerializer} methods can be chained as in the following example:
 ** <pre>
 **   serializer
 **     .writeStartObject()
 **       .write("firstName", "John")
 **       .write("lastName", "Smith")
 **       .write("age", 25)
 **       .writeStartObject("address")
 **         .write("streetAddress", "21 2nd Street")
 **         .write("city", "New York")
 **         .write("state", "NY")
 **         .write("postalCode", "10021")
 **       .writeEnd()
 **       .writeStartArray("phoneNumber")
 **         .writeStartObject()
 **           .write("type", "home")
 **           .write("number", "212 555-1234")
 **         .writeEnd()
 **         .writeStartObject()
 **           .write("type", "fax")
 **           .write("number", "646 555-4567")
 **         .writeEnd()
 **       .writeEnd()
 **     .writeEnd();
 **   serializer.close();
 ** </pre>
 ** The example code above generates the following JSON (or equivalent):
 ** <pre>
 **   {
 **     "firstName": "John"
 **   , "lastName": "Smith"
 **   , "age": 25
 **   , "address": {
 **       "streetAddress": "21 2nd Street"
 **     , "city": "New York"
 **     , "state": "NY"
 **     , "postalCode": "10021"
 **     },
 **     "phoneNumber": [
 **       {"type": "home", "number": "212 555-1234"}
 **     , {"type": "fax", "number": "646 555-4567"}
 **     ]
 **   }
 ** </pre>
 ** The generated JSON text is strictly conform to the grammar defined in
 ** <a href="http://www.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
 ** <p>
 ** Serializers only encode well-formed JSON strings. In particular:
 ** <ul>
 **   <li>The serializer must have exactly one top-level array or object.
 **   <li>Lexical scopes must be balanced: every call to
 **       {@link #writeStartArray()} and {@link #writeStartObject()} must have a
 **       matching call to {@link #writeEnd}.
 **  <li>Arrays may not contain keys (property names).
 **  <li>Objects must alternate keys (property names) and values.
 **  <li>Values are inserted with either literal {@link #write(JsonValue) value}
 **      calls, or by nesting arrays or objects.
 ** </ul>
 ** Calls that would result in a malformed JSON string will fail with a
 ** {@link JsonException}.
 ** <p>
 ** This class provides no facility for pretty-printing (ie. indenting) output.
 ** To encode indented output, use {@link JsonObject#toString()} or
 ** {@link JsonArray#toString()}.
 ** <p>
 ** Some implementations of the API support at most 20 levels of nesting.
 ** Attempts to create more than 20 levels of nesting may fail with a
 ** {@link JsonException}.
 ** <p>
 ** Each marshaller may be used to encode a single top level value. Instances of
 ** this class are not thread safe. Although this class is nonfinal, it was not
 ** designed for inheritance and should not be subclassed. In particular,
 ** self-use by overrideable methods is not specified. See <i>Effective Java</i>
 ** Item 17, "Design and Document or inheritance or else prohibit it" for
 ** further information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class JsonSerializer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** a string containing a full set of spaces for a single level of
   ** indentation for no pretty printing.
   */
  private static final String INDENT = "  ";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** indicates pretty printing */
  private boolean             indent = false;

  /**
   ** the output data, containing at most one top-level array or object.
   */
  private final Delegate      stream;

  /**
   ** Unlike the original implementation, this stack isn't limited to 20 levels
   ** of nesting.
   */
  private final Stack<Scope>  scope  = new Stack<Scope>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Scope
  // ~~~~ ~~~~~
  /**
   ** Lexical scoping elements within this serializers, necessary to insert the
   ** appropriate separator characters (ie. commas and colons) and to detect
   ** nesting errors.
   */
  enum Scope {
      /**
       ** A special bracketless array needed by JsonContext.join() and
       ** JsonContext.quote() only.
       ** <p>
       ** <b>Not</b> used for JSON encoding.
       */
      NULL
      /**
       ** An array with no elements requires no separators or newlines before it
       ** is closed.
       */
    , ARRAY_EMPTY
      /**
       ** An array with at least one value requires a comma and newline before
       ** the next element.
       */
    , ARRAY_NONEMPTY
      /**
       ** An entity with no keys or values requires no separators or newlines
       ** before it is closed.
       */
    , ENTITY_EMPTY
      /**
       ** An entity with at least one name/value pair requires a comma and
       ** newline before the next element.
       */
    , ENTITY_NONEMPTY
      /**
       ** An entity whose most recent element is a key.
       ** <br>
       ** The next element must be a value.
       */
    , DANGLING
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Stream
  // ~~~~~ ~~~~~~
  /**
   ** Writes a JSON {@link JsonObject object} or {@link JsonArray array}
   ** structure to an output source.
   ** <p>
   ** The class {@link javax.json.Json} contains methods to create writers from
   ** output sources ({@link java.io.OutputStream} and {@link java.io.Writer}).
   ** <p>
   ** The following example demonstrates how write an empty JSON object:
   ** <pre>
   **   JsonWriter writer = Json.createWriter(...);
   **   writer.writeObject(Json.createObjectBuilder().build());
   **   writer.close();
   ** </pre>
   ** The class {@link JsonWriterFactory} also contains methods to create
   ** {@link JsonWriter} instances. A factory instance can be used to create
   ** multiple writer instances with the same configuration. This the preferred
   ** way to create multiple instances. A sample usage is shown in the following
   ** example:
   ** <pre>
   **   JsonWriterFactory factory = Json.createWriterFactory(config);
   **   JsonWriter        writer1 = factory.createWriter(...);
   **   JsonWriter        writer2 = factory.createWriter(...);
   ** </pre>
   */
  static class Stream {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** indicates that the stream is closed already */
    private boolean              closed;

    private final JsonSerializer serializer;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link OutputStream}.
     **
     ** @param  stream           an {@link OutputStream} to which JSON is
     **                          written.
     */
    Stream(final OutputStream stream) {
      // ensure inheritance
      this(stream, JsonContext.CHARSET_DEFAULT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link OutputStream}.
     ** <br>
     ** The stream is configured with the specified map of provider specific
     ** configuration properties.
     **
     ** @param  stream           an {@link OutputStream} to which JSON is
     **                          written.
     ** @param  charset          a charset for encoding purpose.
     ** @param  config           a {@link Map} of provider specific properties
     **                          to configure the JSON writer.
     **                          The {@link Map} may be empty or
     **                          <code>null</code>.
     */
    Stream(final OutputStream stream, final Charset charset) {
       // ensure inheritance
      super();

      // initialize instance attributes
      this.serializer = new JsonSerializer(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default JSON character <code>Stream</code> that's backed by
     ** the specified {@link Writer}.
     **
     ** @param  stream           an {@link Writer} to which JSON is written.
     */
    Stream(final Writer stream) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.serializer = new JsonSerializer(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: close
    /**
     ** Closes this JSON writer and frees any resources associated with the
     ** writer. This method closes the underlying output source.
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
    // Method: write
    /**
     ** Writes the specified JSON {@link JsonObject} or {@link JsonArray} to the
     ** output source.
     ** <br>
     ** This method needs to be called only once for a writer instance.
     **
     ** @param  value            the JSON array or object that is to be written
     **                          to the output source.
     **
     ** @throws JsonException         if the specified JSON object cannot be
     **                               written due to i/o error (IOException
     **                               would be cause of JsonException).
     ** @throws IllegalStateException if writeArray, writeObject, write or close
     **                               method is already called.
     */
    public final void write(final JsonValue value) {
      switch (value.type()) {
        case ARRAY  : writeArray((JsonArray)value);
                      break;
        case OBJECT : writeObject((JsonObject)value);
                      break;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: writeArray
    /**
     ** Writes the specified JSON {@link JsonArray} to the output source.
     ** <br>
     ** This method needs to be called only once for a writer instance.
     **
     ** @param  array            the JSON array that is to be written to the
     **                          output source.
     **
     ** @throws JsonException         if the specified JSON object cannot be
     **                               written due to i/o error (IOException
     **                               would be cause of JsonException).
     ** @throws IllegalStateException if writeArray, writeObject, write or close
     **                               method is already called.
     */
    public final void writeArray(final JsonArray array) {
      // prevent bogus state
      if (this.closed)
        throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");

      // prevent further usage
      this.closed = true;

      // spool out
      this.serializer.writeStartArray();
      for (JsonValue value : array) {
        this.serializer.write(value);
      }
      this.serializer.writeEnd();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: writeObject (JsonWriter)
    /**
     ** Writes the specified JSON {@link JsonObject} to the output source.
     ** <br>
     ** This method needs to be called only once for a writer instance.
     **
     ** @param  object           the JSON object that is to be written to the
     **                          output source.
     **
     ** @throws JsonException         if the specified JSON object cannot be
     **                               written due to i/o error (IOException
     **                               would be cause of JsonException).
     ** @throws IllegalStateException if writeArray, writeObject, write or close
     **                               method is already called.
     */
    public final void writeObject(final JsonObject object) {
      // prevent bogus state
      if (this.closed)
        throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");

      // prevent further usage
      this.closed = true;

      // spool out
      this.serializer.writeStartArray();
      for (Iterator<Pair<String, JsonValue>> i = object.iterator(); i.hasNext(); ) {
        final Pair<String, JsonValue> cursor = i.next();
        this.serializer.write(cursor.getKey(), cursor.getValue());
      }
      this.serializer.writeEnd();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Factory
  // ~~~~~ ~~~~~~~
  /**
   ** Factory to create {@link JsonWriter} instances.
   ** <br>
   ** If a factory instance is configured with some configuration, that would be
   ** used to configure the created writer instances.
   ** <p>
   ** {@link JsonWriter} can also be created using {@link Json}'s
   ** <code>createWriter</code> methods. If multiple writer instances are
   ** created, then creating them using a writer factory is preferred.
   ** <p>
   ** <b>For example:</b>
   ** <pre>
   **   JsonWriterFactory factory = Json.createWriterFactory(...);
   **   JsonWriter        writer1 = factory.createWriter(...);
   **   JsonWriter        writer2 = factory.createWriter(...);
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
     ** {@link JsonGeneratorFactory} and {@link JsonWriter}.
     ** <br>
     ** The {@link JsonGeneratorFactory}s and {@link JsonWriter}s created by
     ** this factory are configured with the specified map of provider specific
     ** configuration properties.
     **
     ** @param  config           a {@link Map} of provider specific properties
     **                          to configure the JSON readers and writers.
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
    // Method: createSerializer
    /**
     ** Creates a JSON serializer to write JSON text to a byte stream.
     ** <br>
     ** Characters written to the stream are encoded into bytes using UTF-8
     ** encoding.
     ** <br>
     ** The serializer is configured with the factory's configuration.
     **
     ** @param  stream           an i/o stream to which JSON is written.
     **
     ** @return                  the JSON serializer to write JSON text to the
     **                          byte stream <code>stream</code>.
     */
    public final JsonSerializer createSerializer(final OutputStream stream) {
      return new JsonSerializer(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createSerializer
    /**
     ** Creates a JSON serializer to write JSON text to a byte stream.
     ** <br>
     ** Characters written to the stream are encoded into bytes using the
     ** specified charset.
     ** The serializer is configured with the factory's configuration.
     ** <br>
     ** The serializer is configured with the factory's configuration.
     **
     ** @param  stream           an i/o stream to which JSON is written.
     ** @param  charset          a charset for encoding purpose.
     **
     ** @return                  the JSON serializer to write JSON text to the
     **                          byte stream <code>stream</code>.
     */
    public final JsonSerializer createSerializer(final OutputStream stream, final Charset charset) {
      return new JsonSerializer(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: createSerializer
    /**
     ** Creates a JSON serializer to write JSON text to a character stream.
     ** <br>
     ** The serializer is configured with the factory's configuration.
     **
     ** @param  stream           an writer to which JSON is written
     **
     ** @return                  the JSON serializer to write JSON text to the
     **                          byte stream <code>stream</code>.
     */
    public final JsonSerializer createSerializer(final Writer stream) {
      return new JsonSerializer(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createWriter
    /**
     ** Creates a JSON writer to write a JSON {@link JsonObject object} or
     ** {@link JsonArray array} structure to the specified byte stream.
     ** <br>
     ** Characters written to the stream are encoded into bytes using UTF-8
     ** encoding.
     **
     ** @param  stream           an i/o stream to which JSON object or array is
     **                          written.
     **
     ** @return                  a JSON writer.
     */
    public final Stream createWriter(final OutputStream stream) {
      return new Stream(stream);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createWriter
    /**
     ** Creates a JSON writer from a byte stream.
     ** <br>
     ** The bytes of the stream are encoded to characters using the specified
     ** charset.
     **
     ** @param  stream           an i/o stream to which JSON object or array is
     **                          written.
     ** @param  charset          a charset for deconding purpose.
     **
     ** @return                  a JSON writer.
     */
    public final Stream createWriter(final OutputStream stream, final Charset charset) {
      return new Stream(stream, charset);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   createWriter
    /**
     ** Creates a JSON writer to write a JSON {@link JsonObject object} or
     ** {@link JsonArray array} structure to the specified character stream.
     **
     ** @param  stream           an i/o writer to which JSON object or array is
     **                          written.
     **
     ** @return                  a JSON writer.
     */
    public final Stream createWriter(final Writer stream) {
      return new Stream(stream);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Delegate
  // ~~~~~ ~~~~~~~~
  /**
   **
   */
  private class Delegate extends Writer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Writer     stream;
    private final AtomicLong length;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default character stream <code>Delegate</code> thats backed
     ** by the specified {@link Writer}.
     **
     ** @param  stream           the {@link Writer} to backing this
     **                         {@link Writer}.
     */
    Delegate(final Writer stream) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.stream = stream;
      this.length = new AtomicLong();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: length
    /**
     ** Returns the numbers of characters written to the delegated stream.
     **
     ** @return                  the numbers of characters written to the
     **                          delegated stream.
     */
    long length() {
      return this.length.get();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: close (Writer)
    /**
     ** Closes the stream, flushing it first.
     ** <br>
     ** Once the stream has been closed, further write() or flush() invocations
     ** will cause an {@link IOException} to be thrown.
     ** <br>
     ** Closing a previously closed stream has no effect.
     **
     ** @throws IOException       if an I/O error occurs
     */
    @Override
    public final void close()
      throws IOException {

      this.stream.close();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: flush (Writer)
    /**
     ** Flushes the stream.
     ** <p>
     ** If the stream has saved any characters from the various write() methods
     ** in a buffer, write them immediately to their intended destination. Then,
     ** if that destination is another character or byte stream, flush it. Thus
     ** one flush() invocation will flush all the buffers in a chain of
     ** {@link Writer}s and {@link OutputStream}s.
     ** <p>
     ** If the intended destination of this stream is an abstraction provided by
     ** the underlying operating system, for example a file, then flushing the
     ** stream guarantees only that bytes previously written to the stream are
     ** passed to the operating system for writing; it does not guarantee that
     ** they are actually written to a physical device such as a disk drive.
     **
     ** @throws IOException      if an I/O error occurs
     */
    public final void flush()
      throws IOException {

      this.stream.flush();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: write (Writer)
    /**
     ** Writes a portion of an array of characters.
     **
     ** @param  buffer           an array of characters to be written.
     ** @param  offset           the offset from which to start writing
     **                          characters.
     ** @param  length           the number of characters to write.
     **
     ** @throws IOException      if an I/O error occurs
     */
    public final void write(final char buffer[], final int offset, final int length)
      throws IOException {

      this.stream.write(buffer, offset, length);
      this.length.addAndGet(length);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonSerializer</code> write JSON text to a byte
   ** stream.
   ** <br>
   ** Characters * written to the stream are encoded into bytes using UTF-8
   ** encoding.
   ** <br>
   ** The serializer is configured with the factory's configuration.
   **
   ** @param  stream             an i/o stream to which JSON is written.
   */
  JsonSerializer(final OutputStream stream) {
    this(stream, JsonContext.CHARSET_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonSerializer</code> write JSON text to a byte
   ** stream.
   ** <br>
   ** Characters written to the stream are encoded into bytes using the
   ** specified charset.
   ** <br>
   ** The serializer is configured with the factory's configuration.
   **
   ** @param  stream             an i/o stream to which JSON is written.
   ** @param  charset            a charset for encoding purpose.
   */
  JsonSerializer(final OutputStream stream, final Charset encoding) {
    this(new OutputStreamWriter(stream, encoding));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>JsonSerializer</code> to write JSON text to a
   ** character stream.
   ** <br>
   ** The serializer is configured with the factory configuration.
   **
   ** @param  stream             an i/o writer to which JSON is written.
   */
  JsonSerializer(Writer stream) {
    // ensure inheritance
    super();

    if (!(stream instanceof BufferedWriter))
      stream = new BufferedWriter(stream);

    // initialize instance
    this.indent = false;
    this.stream = new Delegate(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (JsonGenerator)
  /**
   ** Closes this serializer and frees any resources associated with it.
   ** <br>
   ** This method closes the underlying output source.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public void close() {
    // prevent bogus state
//    if (!this.scope.isEmpty())
//      throw new JsonException("Nesting problem");

    try {
      this.stream.close();
    }
    catch (IOException e) {
      throw new JsonException("I/O error while closing JsonGenerator", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   flush (JsonGenerator)
  /**
   ** Flushes the underlying output source. If the serializer has saved any
   ** characters in a buffer, writes them immediately to the underlying output
   ** source before flushing it.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public void flush() {
    try {
      this.stream.flush();
    }
    catch (IOException e) {
      throw new JsonException("I/O error while flushing JsonGenerator", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON value within the current array
   ** context.
   **
   ** @param  value              a <code>JsonValue</code> to be written in
   **                            current JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final JsonValue value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    switch (value.type()) {
      case NULL   : writeNull();
                    break;
      case TRUE   : write(true);
                    break;
      case FALSE  : write(false);
                    break;
      case STRING : write((JsonString)value);
                    break;
      case NUMBER : write((JsonNumber)value);
                    break;
      case ARRAY  : write((JsonArray)value);
                    break;
      case OBJECT : write((JsonObject)value);
                    break;
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/string value pair in the current object context.
   ** <br>
   ** The specified value is written as JSON string value.
   **
   ** @param  name               a name in the JSON name/string pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/string pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final JsonValue value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    switch (value.type()) {
      case NULL   : writeNull(name);
                    break;
      case TRUE   : write(name, true);
                    break;
      case FALSE  : write(name, false);
                    break;
      case STRING : write(name, (JsonString)value);
                    break;
      case NUMBER : write(name, (JsonNumber)value);
                    break;
      case ARRAY  : write(name, (JsonArray)value);
                    break;
      case OBJECT : write(name, (JsonObject)value);
                    break;
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartArray (JsonGenerator)
  /**
   ** Writes the JSON name/start array character pair with in the current object
   ** context.
   ** <br>
   ** It starts a new child array context within which JSON values can be
   ** written to the array.
   ** <br>
   ** <b>Note:</b>
   ** <br>
   ** Each call to this method must be paired with a call to {@link #writeEnd}.
   **
   ** @param  name               a name within the JSON name/array pair to be
   **                            written.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  // @Override
  public JsonSerializer writeStartArray(final String name)
    throws JsonException {

    try {
      return property(name).writeStartArray();
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing start of JSON array", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartArray (JsonGenerator)
  /**
   ** Writes the JSON start array character.
   ** <br>
   ** It starts a new child array context within which JSON values can be
   ** written to the array. This method is valid only in an array context or in
   ** no context (when a context is not yet started).
   ** <br>
   ** <b>Note:</b>
   ** <br>
   ** This method can only be called once in no context.
   ** <br>
   ** <b>Note:</b>
   ** <br>
   ** Each call to this method must be paired with a call to {@link #writeEnd()}.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer writeStartArray()
    throws JsonException {

    try {
      return open(Scope.ARRAY_EMPTY, "[");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing start of JSON array", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartObject (JsonGenerator)
  /**
   ** Writes the JSON name/start object character pair in the current object
   ** context. It starts a new child object context within which JSON name/value
   ** pairs can be written to the object.
   **
   ** @param  name               a name within the JSON name/object pair to be
   **                            written.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer writeStartObject(final String name)
    throws JsonException {

    try {
      return property(name).writeStartObject();
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing start of JSON object", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeStartObject (JsonGenerator)
  /**
   ** Writes the JSON start object character.
   ** <br>
   ** It starts a new child object context within which JSON name/value pairs
   ** can be written to the object. This method is valid only in an array
   ** context or in no context (when a context is not yet started).
   ** <b>Note:</b>
   ** <br>
   ** This method can only be called once in no context.
   ** <br>
   ** <b>Note:</b>
   ** <br>
   ** Each call to this method must be paired with a call to {@link #writeEnd()}.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer writeStartObject()
    throws JsonException {

    try {
      return open(Scope.ENTITY_EMPTY, "{");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing start of JSON object", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writeEnd (JsonGenerator)
  /**
   ** Writes the end of the current context.
   ** <br>
   ** If the current context is an array context, this method writes the
   ** end-of-array character (']').
   ** <br>
   ** If the current context is an object context, this method writes the
   ** end-of-object character ('}').
   ** <br>
   ** After writing the end of the current context, the parent context becomes
   ** the new current context.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer writeEnd()
    throws JsonException {

    try {
      switch (this.scope.peek()) {
        case ENTITY_EMPTY    :
        case ENTITY_NONEMPTY : return close(Scope.ENTITY_EMPTY, Scope.ENTITY_NONEMPTY, "}");
        case ARRAY_EMPTY     :
        case ARRAY_NONEMPTY  : return close(Scope.ARRAY_EMPTY, Scope.ARRAY_NONEMPTY, "]");
        default              : throw new JsonException("Nesting problem");
      }
    }
    catch (IOException e) {
      throw new JsonException("I/O error writing end of JSON structure", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON <code>true</code> or <code>false</code> in the current array
   ** context.
   ** <br>
   ** If value is <code>true</code>, it writes the JSON <code>true</code> value,
   ** otherwise  it writes the JSON <code>false</code> value.
   **
   **
   ** @param  value              a <code>boolean</code> to be written in current
   **                            JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final boolean value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(value ? "true" : "false");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while boolean value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/boolean value pair in the current object context.
   ** <br>
   ** If value is <code>true</code>, it writes the JSON <code>true</code> value,
   ** otherwise  it writes the JSON <code>false</code> value.
   **
   ** @param  name               a name in the JSON name/boolean pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/boolean pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final boolean value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(value ? "true" : "false");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while boolean value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON number value within current array
   ** context.
   ** <br>
   ** The string <code>new BigDecimal(value).toString()</code> is used as the
   ** text value for writing.
   **
   ** @param  value              a <code>int</code> to be written in current
   **                            JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final int value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(String.valueOf(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while int value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/number value pair in the current object context.
   ** <br>
   ** The specified value is written as a JSON number value. The string
   ** <code>new BigDecimal(value).toString()</code> is used as the text value
   ** for writing.
   **
   ** @param  name               a name in the JSON name/number pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/number pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final int value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(String.valueOf(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while int value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON number value within current array
   ** context.
   ** <br>
   ** The string <code>new BigDecimal(value).toString()</code> is used as the
   ** text value for writing.
   **
   ** @param  value              a <code>long</code> to be written in current
   **                            JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final long value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(String.valueOf(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while long value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/number value pair in the current object context.
   ** <br>
   ** The specified value is written as a JSON number value. The string
   ** <code>new BigDecimal(value).toString()</code> is used as the text value
   ** for writing.
   **
   ** @param  name               a name in the JSON name/number pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/number pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final long value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(String.valueOf(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while long value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON number value within current array
   ** context.
   ** <br>
   ** The string <code>new BigDecimal(value).toString()</code> is used as the
   ** text value for writing.
   **
   ** @param  value              a <code>double</code> to be written in current
   **                            JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  // @Override
  public JsonSerializer write(final double value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while double value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/number value pair in the current object context.
   ** <br>
   ** The specified value is written as a JSON number value. The string
   ** <code>new BigDecimal(value).toString()</code> is used as the text value
   ** for writing.
   **
   ** @param  name               a name in the JSON name/number pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/number pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final double value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing double value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON number value within current array
   ** context.
   ** <br>
   ** The string <code>new BigDecimal(value).toString()</code> is used as the
   ** text value for writing.
   **
   ** @param  value              a <code>BigInteger</code> to be written in
   **                            current JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final BigInteger value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing BigInteger value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/number value pair in the current object context.
   ** <br>
   ** The specified value is written as a JSON number value. The string
   ** <code>new BigDecimal(value).toString()</code> is used as the text value
   ** for writing.
   **
   ** @param  name               a name in the JSON name/number pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/number pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final BigInteger value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing BigInteger value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON number value within current array
   ** context.
   ** <br>
   ** The specified value's <code>toString()</code> is used as the the text
   ** value for writing.
   **
   ** @param  value              a <code>BigDecimal</code> to be written in
   **                            current JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException        if an i/o error occurs (IOException would be
   **                              cause of JsonException).
   */
  public JsonSerializer write(final BigDecimal value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing BigDecimal value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/number value pair in the current object context.
   ** <br>
   ** The specified value's <code>toString()</code> is used as the the text
   ** value for writing.
   **
   ** @param  name               a name in the JSON name/number pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/number pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException      if an i/o error occurs (IOException would be
   **                            cause of JsonException).
   */
  public JsonSerializer write(final String name, final BigDecimal value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(JsonContext.toString(value));
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing BigDecimal value in JSON object", e);
    }
    return this;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonNumber} to the output source.
   **
   ** @param  value              the JSON number that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final JsonNumber value) {
    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append(value.toString());
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing a number", e);
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonNumber} to the output source.
   **
   ** @param  name               a name in the JSON number to be written to the
   **                            output source.
   ** @param  value              the JSON number that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final String name, final JsonNumber value) {
    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append(value.toString());
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing a number", e);
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonString} to the output source.
   **
   ** @param  value              the JSON string that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final JsonString value) {
    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    write(value.string());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonString} to the output source.
   **
   ** @param  name               a name in the JSON string to be written to the
   **                            output source.
   ** @param  value              the JSON string that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final String name, final JsonString value) {
    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    write(name, value.string());
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonArray} to the output source.
   **
   ** @param  name               a name in the JSON array to be written to the
   **                            output source.
   ** @param  array              the JSON array that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final String name, final JsonArray array) {
    // spool out
    writeStartArray(name);
    for (JsonValue child : array)
      write(child);
    writeEnd();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonArray} to the output source.
   **
   ** @param  array              the JSON array that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final JsonArray array) {
    // spool out
    writeStartArray();
    for(JsonValue child : array)
      write(child);
    writeEnd();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonObject} to the output source.
   **
   ** @param  name               a name in the JSON object to be written to the
   **                            output source.
   ** @param  object             the JSON object that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final String name, final JsonObject object) {
    // spool out
    writeStartObject(name);
    for (Iterator<Pair<String, JsonValue>> i = object.iterator(); i.hasNext();) {
      final Pair<String, JsonValue> cursor = i.next();
      write(cursor.getKey(), cursor.getValue());
    }
    writeEnd();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified JSON {@link JsonObject} to the output source.
   **
   ** @param  object             the JSON object that is to be written to the
   **                            output source.
   **
   ** @throws JsonException         if the specified JSON object cannot be
   **                               written due to i/o error (IOException
   **                               would be cause of JsonException).
   ** @throws IllegalStateException if writeArray, writeObject, write or close
   **                               method is already called.
   */
  public final void write(final JsonObject object) {
    // spool out
    writeStartObject();
    for (Iterator<Pair<String, JsonValue>> i = object.iterator(); i.hasNext();) {
      final Pair<String, JsonValue> cursor = i.next();
      write(cursor.getKey(), cursor.getValue());
    }
    writeEnd();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON null value within the current array context.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException        if an i/o error occurs (IOException would be
   **                              cause of JsonException).
   */
  public JsonSerializer writeNull()
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      this.stream.append("null");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing null value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/null value pair in an current object context.
   **
   ** @param  name               a name in the JSON name/string pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException        if an i/o error occurs (IOException would be
   **                              cause of JsonException).
   */
  public JsonSerializer writeNull(final String name)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      this.stream.append("null");
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing null value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes the specified value as a JSON string value within current array
   ** context.
   ** <br>
   ** The specified value is written as JSON string value.
   **
   ** @param  value              a <code>String</code> to be written in
   **                            current JSON array.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException        if an i/o error occurs (IOException would be
   **                              cause of JsonException).
   */
  public JsonSerializer write(final String value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      valueSeparator();
      escpape(value);
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing string value in JSON array", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Writes a JSON name/string value pair in the current object context.
   ** <br>
   ** The specified value is written as JSON string value.
   **
   ** @param  name               a name in the JSON name/string pair to be
   **                            written in current JSON object.
   ** @param  value              a value in the JSON name/string pair to be
   **                            written in current JSON object.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws JsonException        if an i/o error occurs (IOException would be
   **                              cause of JsonException).
   */
  public JsonSerializer write(final String name, final String value)
    throws JsonException {

    // prevent bogus state
    if (this.scope.isEmpty())
      throw new JsonException("Nesting problem");

    try {
      property(name).valueSeparator();
      escpape(value);
    }
    catch (IOException e) {
      throw new JsonException("I/O error while writing string value in JSON object", e);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Enters a new scope by appending any necessary whitespace and the given
   ** bracket.
   ** <p>
   ** Method is package protected to allow only {@link JSonContext} access to
   ** it.
   **
   ** @param  empty              the {@link Scope} to push to the stack.
   ** @param  bracket            the character sequence to open the current
   **                            entity.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private JsonSerializer open(final Scope empty, final String bracket)
    throws IOException {

    if (this.scope.isEmpty() && this.stream.length() > 0)
      throw new JsonException("Nesting problem: multiple top-level roots");

    valueSeparator();
    this.scope.push(empty);
    this.stream.append(bracket);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Closes the current scope by appending any necessary whitespace and the
   ** given bracket.
   ** <p>
   ** Method is package protected to allow only {@link JSonContext} access to
   ** it.
   **
   ** @param  empty              the expected {@link Scope} of the empty entity.
   ** @param  nonempty           the expected {@link Scope} of the non-empty
   **                            entity.
   ** @param  bracket            the character sequence to close the current
   **                            entity.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private JsonSerializer close(final Scope empty, final Scope nonempty, final String bracket)
    throws IOException {

    // remove the scope from the stack
    final Scope context = this.scope.pop();
    if (context != nonempty && context != empty)
      throw new JsonException("Nesting problem");

    if (context == nonempty)
      newline();

    this.stream.append(bracket);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   property
  /**
   ** Encodes the property name to this marshaller.
   **
   ** @param  name               the name of the forthcoming value.
   **                            Must not be <code>null</code>.
   **
   ** @return                    the {@link JsonSerializer} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link JsonSerializer}.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private JsonSerializer property(final String name)
    throws IOException {

    // prevent bogus input
    if (name == null)
      throw new JsonException("Names must be non-null");

    entitySeparator();
    escpape(name);
    return this;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitySeparator
  /**
   ** Inserts any necessary separators and whitespace before a name.
   ** <br>
   ** Also adjusts the stack to expect the key's value.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private void entitySeparator()
    throws IOException {

    final Scope context = this.scope.pop();
    // first in object
    if (context == Scope.ENTITY_NONEMPTY)
      this.stream.append(',');
    // not in an object!
    else if (context != Scope.ENTITY_EMPTY)
      throw new JsonException("Nesting problem");

    newline();
    this.scope.push(Scope.DANGLING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueSeparator
  /**
   ** Inserts any necessary separators and whitespace before a literal value,
   ** inline array, or inline object. Also adjusts the stack to expect either a
   ** closing bracket or another element.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private void valueSeparator()
    throws IOException {

    if (this.scope.isEmpty())
      return;

    final Scope context = this.scope.pop();
    // first in array
    if (context == Scope.ARRAY_EMPTY) {
      newline();
      this.scope.push(Scope.ARRAY_NONEMPTY);
    }
    // another in array
    else if (context == Scope.ARRAY_NONEMPTY) {
      this.stream.append(',');
      newline();
    }
    // value for key
    else if (context == Scope.DANGLING) {
      this.stream.append(this.indent ? ":" : ": ");
      this.scope.push(Scope.ENTITY_NONEMPTY);
    }
    else if (context != Scope.NULL) {
      throw new JsonException("Nesting problem");
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   escpape
  /**
   ** Escapes <code>String</code> <code>value</code> to this serializer.
   ** <p>
   ** Method is package protected to allow only {@link JSonContext} access to
   ** it.
   **
   ** @param  value              a <code>String</code> value.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  void escpape(final String value)
    throws IOException {

    this.stream.append("\"");
    for (int i = 0, length = value.length(); i < length; i++) {
      char c = value.charAt(i);
      // from RFC 4627, "All Unicode characters may be placed within the
      // quotation marks except for the characters that must be escaped:
      // quotation mark, reverse solidus, and the control characters
      // (U+0000 through U+001F)."
      switch (c) {
        case '"'  :
        case '\\' :
        case '/'  : this.stream.append('\\').append(c);
                    break;
        case '\t' : this.stream.append("\\t");
                    break;
        case '\b' : this.stream.append("\\b");
                    break;
        case '\n' : this.stream.append("\\n");
                    break;
        case '\r' : this.stream.append("\\r");
                    break;
        case '\f' : this.stream.append("\\f");
                    break;
        default   : if (c <= 0x1F) {
                      this.stream.append(String.format("\\u%04x", (int)c));
                    }
                    else {
                      this.stream.append(c);
                    }
                    break;
      }
    }
    this.stream.append("\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   newline
  /**
   ** Write the indentaion if configured.
   **
   ** @throws IOException        if an i/o error occurs (IOException should be
   **                            cause of a JsonException)
   */
  private void newline()
    throws IOException {

    // if indention wasn't initialize there is nothing to do here
    if (this.indent) {
      this.stream.append("\n");
      for (int i = 0; i < this.scope.size(); i++) {
        this.stream.append(INDENT);
      }
    }
  }
}