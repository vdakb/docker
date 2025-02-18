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

    System      :   Foundation Configuration Extension
    Subsystem   :   Common Shared Utility

    File        :   AbstractConfigurationFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConfigurationFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.config;

import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.io.InputStream;
import java.io.IOException;

import java.util.Iterator;

import java.util.ListIterator;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonException;

import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;

import javax.validation.Validator;
import javax.validation.ConstraintViolation;

////////////////////////////////////////////////////////////////////////////////
// class AbstractConfigurationFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A generic factory class for loading configuration files, binding them to
 ** configuration objects, and validating their constraints. Allows for
 ** overriding configuration parameters from system properties.
 **
 ** @param  <T>                  the type of the configuration objects to
 **                              produce.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractConfigurationFactory<T extends Configuration> implements ConfigurationFactory<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Pattern DOT         = Pattern.compile("\\\\\\.");
  private static final Pattern COMMA       = Pattern.compile("\\\\,");

  private static final Pattern DOT_SPLIT   = Pattern.compile("(?<!\\\\)\\.");
  private static final Pattern COMMA_SPLIT = Pattern.compile("(?<!\\\\),");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Class<T>  clazz;
  private final String    prefix;
  private final Validator validator;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConfigurationFactory</code> that allows use as
   ** a JavaBean.
   **
   ** @param  clazz              the configuration {@link Class}.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>t</code>.
   ** @param  validator          the validator to use.
   **                            <br>
   **                            Allowed object is {@link Validator}.
   ** @param  prefix             the system property name prefix used by
   **                            overrides.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public AbstractConfigurationFactory(final Class<T> clazz, final Validator validator, final String prefix) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.clazz     = clazz;
    this.prefix    = prefix;
    this.validator = validator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Loads, parses, binds, and validates a configuration object from an empty
   ** document.
   **
   ** @return                    a validated configuration object.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException            if there is an error reading the file.
   ** @throws ConfigurationException if there is an error parsing or validating
   **                                the file.
   */
  public T build()
    throws IOException
    ,      ConfigurationException {

    try {
      final T          type = this.clazz.getDeclaredConstructor().newInstance();
      final JsonObject node = type.serialize();
      return build(node, "Default Configuration");
    }
    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
      throw new IllegalArgumentException("Unable to create an instance of the configuration class: '" + this.clazz.getCanonicalName() + "'", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build (ConfigurationFactory)
  /**
   ** Loads, parses, binds, and validates a configuration object.
   **
   ** @param  provider           the provider to to use for reading
   **                            configuration files.
   **                            <br>
   **                            Allowed object is
   **                            {@link ConfigurationProvider}.
   ** @param  path               the path of the configuration file.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a validated configuration object.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IOException            if there is an error reading the file.
   ** @throws ConfigurationException if there is an error parsing or validating
   **                                the file.
   */
  @Override
  public T build(final ConfigurationProvider provider, final String path)
    throws IOException
    ,      ConfigurationException {

    try (InputStream input = provider.open(Objects.requireNonNull(path))) {
      final JsonObject root = createReader(input).readObject();
      if (root == null)
        throw ConfigurationException.abort(ConfigurationBundle.string(ConfigurationError.PATH_EMPTY, path));

      return build(root, path);
    }
    catch (Throwable t) {
      throw ConfigurationException.general("message");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createReader
  /**
   ** Creates a JSON parser from a byte stream.
   ** <br>
   ** The character encoding of the stream is determined as specified in
   ** RFC 4627.
   **
   ** @param  stream             the i/o stream from which JSON is to be read.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    a JSON reader.
   **                            <br>
   **                            Possible object is {@link JsonReader}.
   **
   ** @throws JsonException      if encoding cannot be determined or i/o error
   **                            (IOException would be cause of JsonException).
   */
  protected JsonReader createReader(final InputStream stream)
    throws JsonException {

    return Json.createReader(stream);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  protected T build(final JsonObject node, final String path)
    throws IOException
    ,      ConfigurationException {

    // create a json builder by transfering all values from node to that builder
    final JsonObjectBuilder builder = Json.createObjectBuilder();
    for (Map.Entry<String, JsonValue> n : node.entrySet()) {
      builder.add(n.getKey(), n.getValue());
    }
    
    // obtain the optional system properties configured and override the value
    // contained in the builder above with the values of system properties that
    // match
    for (Map.Entry<Object, Object> p : System.getProperties().entrySet()) {
      final String fqpn = (String)p.getKey();
      if (fqpn.startsWith(this.prefix)) {
        final String name = fqpn.substring(this.prefix.length());
        addOverride(node, name, System.getProperty(fqpn));
      }
    }

    try {
      final T config = this.clazz.getDeclaredConstructor().newInstance();
      config.deserialize(node);//mapper.readValue(new TreeTraversingParser(node, mapper), klass);
      validate(path, config);
      return config;
    }
    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | NoSuchMethodException | InvocationTargetException e) {
      throw new IllegalArgumentException("Unable to create an instance of the configuration class: '" + this.clazz.getCanonicalName() + "'", e);
    }
    catch (JsonException e) {
      throw ConfigurationException.build(ConfigurationBundle.string(ConfigurationError.PARSING_FAILED), e);
    }
    /*
    catch (UnrecognizedPropertyException e) {
      final List<String> properties = e.getKnownPropertyIds().stream().map(Object::toString).collect(Collectors.toList());
      throw ConfigurationParsingException.builder("Unrecognized field").setFieldPath(e.getPath()).setLocation(e.getLocation()).addSuggestions(properties).setSuggestionBase(e.getPropertyName()).setCause(e).build(path);
    }
    catch (InvalidFormatException e) {
      final String sourceType = e.getValue().getClass().getSimpleName();
      final String targetType = e.getTargetType().getSimpleName();
      throw ConfigurationParsingException.builder("Incorrect type of value").setDetail("is of type: " + sourceType + ", expected: " + targetType).setLocation(e.getLocation()).setFieldPath(e.getPath()).setCause(e).build(path);
    }
    catch (JsonMappingException e) {
      throw ConfigurationParsingException.builder("Failed to parse configuration").setDetail(e.getMessage()).setFieldPath(e.getPath()).setLocation(e.getLocation()).setCause(e).build(path);
    }
    */
  }

  protected void addOverride(final JsonObject root, String name, String value) {
    JsonValue cursor = root;
    final List<String> parts = Arrays.stream(DOT_SPLIT.split(name)).map(String::trim).map(key -> DOT.matcher(key).replaceAll(".")).collect(Collectors.toList());
    for (int i = 0; i < parts.size(); i++) {
      final String key = parts.get(i);
      if (!cursor.getValueType().equals(JsonValue.ValueType.OBJECT))
        throw new IllegalArgumentException("Unable to override " + name + "; it's not a valid path.");

      final JsonObject obj  = (JsonObject)cursor;
      final String     path = String.join(".", parts.subList(i, parts.size()));
      if (obj.containsKey(path) && !path.equals(key)) {
        obj.put(path, Json.createObjectBuilder().add(path, value).build());
        return;
      }
      final boolean moreParts = i < parts.size() - 1;
      if (key.matches(".+\\[\\d+\\]$")) {
        final int s     = key.indexOf('[');
        final int index = Integer.parseInt(key.substring(s + 1, key.length() - 1));
        JsonValue node  = obj.get(key.substring(0, s));
        if (node == null)
          throw new IllegalArgumentException("Unable to override " + name + "; node with index not found.");

        if (node.getValueType() != JsonStructure.ValueType.ARRAY)
          throw new IllegalArgumentException("Unable to override " + name +"; node with index is not an array.");

        JsonArray array = (JsonArray)node;
        if (index >= array.size())
          throw new ArrayIndexOutOfBoundsException("Unable to override " + name + "; index is greater than size of array.");

        if (moreParts) {
          node   = array.get(index);
          cursor = node;
        }
        else {
          final ListIterator<JsonValue> j = array.listIterator();
          j.add(Json.createObjectBuilder().add(path, value).build());
          return;
        }
      }
      else if (moreParts) {
        JsonValue node = obj.get(key);
        if (node == null) {
          node = obj.objectNode();
          obj.set(key, node);
        }
        if (node.getValueType().equals(JsonStructure.ValueType.ARRAY))
          throw new IllegalArgumentException("Unable to override " + name +"; target is an array but no index specified");

        cursor = node;
      }

      if (!moreParts) {
        if ((cursor.get(key) != null && cursor.get(key).isArray()) || (cursor.get(key) == null && configurationMetadata.isCollectionOfStrings(name))) {
          JsonArray arrayNode = (JsonArray)obj.get(key);
          if (arrayNode == null) {
            arrayNode = obj.arrayNode();
            obj.set(key, arrayNode);
          }
          arrayNode.removeAll();
          Arrays.stream(COMMA_SPLIT.split(value)).map(String::trim).map(val -> COMMA.matcher(val).replaceAll(",")).forEach(arrayNode::add);
        }
        else {
          obj.put(key, value);
        }
      }
*/
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validates the configuration <code>config</code>
   **
   ** @param  path               the path to the configuration the
   **                            <code>error</code>(s) detected within.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  config             the configuration to validate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws ValidationException if contraint violations detected.
   */
  private void validate(final String path, final T config)
    throws ValidationException {

    if (this.validator != null) {
      // validate all constraints on config
      final Set<ConstraintViolation<T>> violations = this.validator.validate(config);
      if (!violations.isEmpty()) {
        throw ValidationException.build(path, violations);
      }
    }
  }
}
