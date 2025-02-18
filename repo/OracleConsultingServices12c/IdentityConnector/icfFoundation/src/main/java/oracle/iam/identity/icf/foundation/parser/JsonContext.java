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

    File        :   JsonContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JsonContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.parser;

import java.util.Map;
import java.util.Collections;

import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.charset.Charset;

////////////////////////////////////////////////////////////////////////////////
// final class JsonContext
// ~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Service provider for JSON processing objects.
 ** <p>
 ** All the methods in this class are safe for use by multiple concurrent
 ** threads.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class JsonContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Double  NEGATIVE_ZERO   = -0d;

  static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** Factory to create {@link JsonObject.Builder} and {@link JsonArray.Builder}
   ** instances. If a factory instance is configured with some configuration,
   ** that would be used to configure the created builder instances.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Map<String, ?> config;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default connector <code>Builder</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     **
     ** @param  config           a {@link Map} of provider specific properties
     **                          to configure the JSON generators and writers.
     **                          The {@link Map} may be empty or
     **                          <code>null</code>.
     */
    Builder(final Map<String, ?> config) {
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

    //////////////////////////////////////////////////////////////////////////////
    // Method: objectBuilder
    /**
     ** Creates a {@link JsonObject.Builder} instance that is used to build
     ** {@link JsonObject}.
     **
     ** @return                  a JSON object builder.
     */
    public final JsonObject.Builder objectBuilder() {
      return new JsonObject.Builder();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: arrayBuilder
    /**
     ** Creates a {@link JsonArray.Builder} instance that is used to build
     ** {@link JsonArray}.
     **
     ** @return                  a JSON array builder.
     */
    public final JsonArray.Builder arrayBuilder() {
      return new JsonArray.Builder();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JsonContext</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  JsonContext() {
    // prevent subclasses outside of this package
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Creates a {@link JsonObject.Builder} and {@link JsonArray.Builder} factory
   ** instances. If a factory instance is configured with some configuration,
   ** that would be used to configure the created builder instances.
   **
   ** @return                    a factory instance for
   **                            {@link JsonObject.Builder}s and
   **                            {@link JsonArray.Builder}s.
   **
   */
  public static Builder builder() {
    return builder(Collections.emptyMap());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Creates a {@link JsonObject.Builder} and {@link JsonArray.Builder} factory
   ** instances. If a factory instance is configured with some configuration,
   ** that would be used to configure the created builder instances.
   **
   ** @param  config             a {@link Map} of provider specific properties
   **                            to configure the JSON builders.
   **                            The {@link Map} may be empty or
   **                            <code>null</code>.
   **
   ** @return                    a factory instance for
   **                            {@link JsonObject.Builder}s and
   **                            {@link JsonArray.Builder}s.
   **
   */
  public static Builder builder(final Map<String, ?> config) {
    return new Builder(config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serializer
  /**
   ** Creates a JSON generator for writing JSON text to a byte stream.
   **
   ** @param  stream             an i/o stream to which JSON is written.
   **
   ** @return                    a JSON parser generator.
   **
   */
  public static JsonSerializer serializer(final OutputStream stream) {
    return new JsonSerializer(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serializer
  /**
   ** Creates a JSON generator for writing JSON text to a character stream.
   **
   ** @param  stream             an i/o writer to which JSON is written.
   **
   ** @return                    a JSON parser generator.
   **
   */
  public static JsonSerializer serializer(final Writer stream) {
    return new JsonSerializer(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writer
  /**
   ** Creates a JSON writer to write a JSON {@link JsonObject object} or
   ** {@link JsonArray array} structure to the specified character stream.
   **
   ** @param  stream             an i/o writer to which JSON object or array is
   **                            written
   **
   ** @return                    a JSON writer.
   */
  public static JsonSerializer.Stream writer(final Writer stream) {
    return new JsonSerializer.Stream(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writer
  /**
   ** Creates a JSON writer to write a JSON {@link JsonObject object} or
   ** {@link JsonArray array} structure to the specified byte stream.
   ** <br>
   ** Characters written to the stream are encoded into bytes using UTF-8
   ** encoding.
   **
   ** @param  stream             an i/o stream to which JSON object or array is
   **                            written
   **
   ** @return                    a JSON writer.
   */
  public static JsonSerializer.Stream writer(final OutputStream stream) {
    return new JsonSerializer.Stream(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   writerFactory
  /**
   ** Creates a writer factory for creating {@link JsonSerializer.Factory}
   ** objects.
   ** <br>
   ** The factory is configured with the specified {@link Map} of provider
   ** specific configuration properties. Provider implementations should ignore
   ** any unsupported configuration properties specified in the {@link Map}.
   **
   ** @param  config             a {@link Map} of provider specific properties
   **                            to configure the JSON writers.
   **                            The {@link Map} may be empty or
   **                            <code>null</code>.
   **
   ** @return                    a JSON writer factory.
   */
  public static JsonSerializer.Factory writerFactory(final Map<String, ?> config) {
    return new JsonSerializer.Factory(config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserializer
  /**
   ** Creates a JSON parser from a character stream.
   **
   ** @param  stream             an i/o stream from which JSON is to be read.
   **
   ** @return                    a JSON parser
   */
  public static JsonParser deserializer(final Reader stream) {
    return new JsonParser(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deserializer
  /**
   ** Creates a JSON parser from a character stream.
   **
   ** @param  stream             an i/o stream from which JSON is to be read.
   **
   ** @return                    a JSON parser
   **
   ** @throws JsonException      if encoding cannot be determined or i/o error
   **                            (IOException would be cause of JsonException).
   */
  public static JsonParser deserializer(final InputStream stream) {
    return new JsonParser(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reader
  /**
   ** Creates Creates a JSON reader from a character stream.
   **
   ** @param  stream             a reader from which JSON is to be read.
   **
   ** @return                    a JSON reader.
   */
  public static JsonDeserializer.Stream reader(final Reader stream) {
    return new JsonDeserializer.Stream(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reader
  /**
   ** Creates a JSON reader from a byte stream.
   ** <br>
   ** The character encoding of the stream is determined as described in
   ** <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC 4627</a>.
   **
   ** @param  stream             a byte stream from which JSON is to be read
   **
   ** @return                    a JSON reader.
   */
  public static JsonDeserializer.Stream reader(final InputStream stream) {
    return new JsonDeserializer.Stream(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  readerFactory
  /**
   ** Creates a reader factory for creating {@link JsonDeserializer.Factory}
   ** objects.
   ** <br>
   ** The factory is configured with the specified {@link Map} of provider
   ** specific configuration properties. Provider implementations should ignore
   ** any unsupported configuration properties specified in the {@link Map}.
   **
   ** @param  config             a {@link Map} of provider specific properties
   **                            to configure the JSON readers.
   **                            The {@link Map} may be empty or
   **                            <code>null</code>.
   **
   ** @return                    a JSON reader factory.
   */
  public static JsonDeserializer.Factory readerFactory(final Map<String,?> config) {
    return new JsonDeserializer.Factory(config);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Encodes the number as a JSON string.
   **
   ** @param  value              a finite value.
   **                            Must not be {@link Double#isNaN() NaNs} or
   **                            {@link Double#isInfinite() infinities}.
   */
  static String toString(final Number number)
    throws JsonException {

    // prevent bogus input
    if (number == null)
      throw new JsonException("Number must be non-null");

    // the original returns "-0" instead of "-0.0" for negative zero
    if (number.equals(NEGATIVE_ZERO))
      return "-0";

    double doubleValue = checkDouble(number.doubleValue());
    long   longValue   = number.longValue();
    return (doubleValue == (double)longValue) ? Long.toString(longValue) : number.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkDouble
  /**
   ** Returns the input if it is a JSON-permissible value; otherwise throws
   ** JsonException.
   **
   ** @param  value              a finite value.
   **                            Must not be {@link Double#isNaN() NaNs} or
   **                            {@link Double#isInfinite() infinities}.
   **
   ** @return                    the finite value.
   **                            Must not be {@link Double#isNaN() NaNs} or
   **                            {@link Double#isInfinite() infinities}.
   */
  static double checkDouble(final double value)
    throws JsonException {

    if (Double.isInfinite(value) || Double.isNaN(value))
      throw new JsonException("Forbidden numeric value: " + value);

    return value;
  }
}