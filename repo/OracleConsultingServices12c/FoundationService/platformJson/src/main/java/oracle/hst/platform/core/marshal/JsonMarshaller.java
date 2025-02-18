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

    System      :   Oracle Marshalling Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   JsonMarshaller.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    JsonMarshaller.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.core.marshal;

import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.Collections;

import java.util.stream.Collectors;

import java.io.Reader;
import java.io.InputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonException;

////////////////////////////////////////////////////////////////////////////////
// abstract class JsonMarshaller
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Helper class containing convenience methods for marshal/unmarshall plain
 ** java objects into Json representations. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JsonMarshaller {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>JsonMarshaller</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new JsonMarshaller()" and enforces use of the public method below.
   */
  private JsonMarshaller() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serialize
  /**
   ** Serializes the specified map to a JSON object using the entity
	 ** mapping specified in {@link #readObject(String)}.
	 **
   ** @param  provider           the value proverider.
   **                            <br>
   **                            Must not be {@code null}.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any of the value.
	 *
	 * @return The JSON object as string.
	 */
	public static String serialize(final Map<String, ?> provider) {
    return provider.entrySet().stream().collect(JsonCollector.object()).build().toString();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Converts a string to a {@link Map}.
   **
   ** @param  payload            the {@link JsonObject} value to convert.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   **
   ** @return                    the resulting {@link Map},
   **                            <code>null</code> if conversion failed.
   **                            <br>
   **                            Possible object is {@link Map}.
   */
  public static Map<String, ?> read(final JsonObject payload)
    throws JsonException {

    return (payload == null) ? Collections.<String, Object>emptyMap() : payload.entrySet().stream().collect(Collectors.toMap(e ->  e.getKey(), e -> JsonMarshaller.inbound(e.getValue())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Converts a string to a {@link JsonObject}.
   **
   ** @param  value              the string value to convert.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the resulting {@link JsonObject},
   **                            <code>null</code> if conversion failed.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject readObject(final String value) {
    return readObject(new StringReader(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Converts a string to an {@link InputStream}.
   **
   ** @param  stream             the stream to convert.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the resulting {@link JsonObject},
   **                            <code>null</code> if conversion failed.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject readObject(final InputStream stream) {
    return Json.createReader(stream).readObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readObject
  /**
   ** Converts a string to an {@link Reader}.
   **
   ** @param  stream             the stream to convert.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Reader}.
   **
   ** @return                    the resulting {@link JsonObject},
   **                            <code>null</code> if conversion failed.
   **                            <br>
   **                            Possible object is {@link JsonObject}.
   */
  public static JsonObject readObject(final Reader stream) {
    return Json.createReader(stream).readObject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Obtain a "plain" Java {@link Boolean} value from a Json Object.
   **
   ** @param  object             the {@link JsonObject} to obtain the value
   **                            from.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the JSON element to return the
   **                            value for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Boolean} value mapped at
   **                            <code>element</code> in the given JSON
   **                            <code>object</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public static Boolean booleanValue(final JsonObject object, final String element) {
    return inboundBoolean(object.get(element));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Obtain a "plain" Java {@link Integer} value from a Json Object.
   **
   ** @param  object             the {@link JsonObject} to obtain the value
   **                            from.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the JSON element to return the
   **                            value for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Integer} value mapped at
   **                            <code>element</code> in the given JSON
   **                            <code>object</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public static Long longValue(final JsonObject object, final String element) {
    return inboundNumber(object.get(element)).longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doubleValue
  /**
   ** Obtain a "plain" Java {@link Double} value from a Json Object.
   **
   ** @param  object             the {@link JsonObject} to obtain the value
   **                            from.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the JSON element to return the
   **                            value for.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the {@link Integer} value mapped at
   **                            <code>element</code> in the given JSON
   **                            <code>object</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public static Double doubleValue(final JsonObject object, final String element) {
    return inboundNumber(object.get(element)).doubleValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Obtain a "plain" Java string value from a Json Object.
   **
   ** @param  object             the {@link JsonObject} to obtain the value
   **                            from.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the JSON element to return the
   **                            value for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string value mapped at <code>element</code>
   **                            in the given JSON <code>object</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String stringValue(final JsonObject object, final String element) {
    return inboundString(object.get(element));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Obtain a {@link JsonValue} from a Json Object mapped at
   ** <code>element</code>.
   **
   ** @param  object             the {@link JsonObject} to obtain the value
   **                            from.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  element            the name of the JSON element to return the
   **                            value for.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the {@link JsonValue} value mapped at
   **                            <code>element</code> in the given JSON
   **                            <code>object</code>.
   **                            <br>
   **                            Possible object is {@link JsonValue}.
   */
  public static JsonValue value(final JsonObject object, final String element) {
    return Objects.requireNonNull(object, "The Json Object must not be null").get(Objects.requireNonNull(element, "The Json Element Tag must not be null"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inbound
  /**
   ** Convert a {@link JsonValue} into a “plain” Java structure (using
   ** {@link Map} and {@link Set}).
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    a {@link Map}, {@link Set}, {@link String},
   **                            {@link Number}, {@link Boolean}, or
   **                            <code>null</code> depending.
   */
  public static Object inbound(final JsonValue value) {
    if (value == null)
      return null;

    // discover type to return 
    switch (value.getValueType()) {
      case ARRAY  : return inboundSet(value);
      case OBJECT : return inboundObject(value);
      case STRING : return inboundString(value);
      case NUMBER : return inboundNumber(value);
      case TRUE   : return Boolean.TRUE;
      case FALSE  : return Boolean.FALSE;
      case NULL   : return null;
      default     : throw new IllegalArgumentException("Unexpected type: " + value.getValueType());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundNumber
  /**
   ** Convert a {@link JsonValue} into a "plain" Java number.
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    the {@link Number} representation for the
   **                            {@link JsonValue}.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static JsonNumber inboundNumber(final JsonValue value) {
    return (JsonNumber)value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundString
  /**
   ** Convert a {@link JsonValue} into a "plain" Java string.
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    the {@link String} representation for the
   **                            {@link JsonValue}.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static String inboundString(final JsonValue value) {
    return (value == null || value.getValueType() == JsonValue.ValueType.NULL) ? null : ((JsonString)value).getString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundNumber
  /**
   ** Convert a {@link JsonValue} into a "plain" Java number.
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    the {@link Number} representation for the
   **                            {@link JsonValue}.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static Boolean inboundBoolean(final JsonValue value) {
    // discover type to return 
    switch (value.getValueType()) {
      case STRING : return Boolean.valueOf(value.toString());
      case TRUE   : return Boolean.TRUE;
      case NULL   :
      case FALSE  : 
      case ARRAY  :
      case OBJECT :
      case NUMBER :
      default     : return Boolean.FALSE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundObject
  /**
   ** Convert a {@link JsonValue} into a "plain" Java {@link Map}.
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    a {@link Map} representing the {@link JsonValue}.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and any for the value.
   */
  public static Map<String, ?> inboundObject(final JsonValue value) {
    return ((JsonObject)value).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> inbound(e.getValue())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundSet
  /**
   ** Convert a {@link JsonValue} into a "plain" Java {@link Set}.
   **
   ** @param  value              the {@link JsonValue} to convert.
   **                            <br>
   **                            Must not <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonValue}.
   **
   ** @return                    a {@link Set} representing the
   **                            {@link JsonValue}.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is any type.
   */
  public static Set<?> inboundSet(final JsonValue value) {
    return ((JsonArray)value).stream().map(JsonMarshaller::inbound).collect(Collectors.toSet());
  }
}