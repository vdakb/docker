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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   Support.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Support.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.lang.reflect.Field;

import java.lang.annotation.Annotation;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collection;

import java.text.ParseException;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import com.fasterxml.jackson.databind.type.CollectionType;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ServiceException;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.annotation.Schema;
import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.object.Evaluator;
import oracle.iam.system.simulation.scim.utility.MapperFactory;

////////////////////////////////////////////////////////////////////////////////
// abstract class Support
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** <code>Support</code> implements utility methods to manipulate JSON nodes
 ** using paths.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Support {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ObjectMapper MAPPER = MapperFactory.objectMapper();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NodeVisitor
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** This class represents an element of the path.
   */
  public abstract static class NodeVisitor {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitInnerNode
    /**
     ** Visit a node referenced by an path element before that last element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @return                  the {@link JsonNode} referenced by the element
     **                          in the parent.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    abstract JsonNode visitInnerNode(final ObjectNode parent, final String field, final Filter filter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitLeafNode
    /**
     ** Visit a node referenced by the last path element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    abstract void visitLeafNode(final ObjectNode parent, final String field, final Filter filter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method:   filterArray
    /**
     ** Visit a array referenced by an path element before that last element.
     **
     ** @param  array            the {@link ArrayNode} to filter.
     **                          <br>
     **                          Allowed object is {@link ArrayNode}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     ** @param  remove           <code>true</code> to remove matching values or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the matching values.
     **                          <br>
     **                          Possible object is {@link ArrayNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    ArrayNode filterArray(final ArrayNode array, final Filter filter, final boolean remove)
      throws ServiceException {

      final ArrayNode  matchingArray = jsonNodeFactory().arrayNode();
      final Iterator<JsonNode> i = array.elements();
      while (i.hasNext()) {
        final JsonNode node = i.next();
        if (Evaluator.evaluate(filter, node)) {
          matchingArray.add(node);
          if (remove) {
            i.remove();
          }
        }
      }
      return matchingArray;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class GatheringNodeVisitor
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  private static final class GatheringNodeVisitor extends NodeVisitor {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final List<JsonNode> values = new LinkedList<JsonNode>();
    final boolean        remove;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>GatheringNodeVisitor<c/ode>.
     **
     ** @param  remove           <code>true</code> to remove the gathered values
     **                          from the container node or <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    private GatheringNodeVisitor(final boolean remove) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.remove = remove;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base calsses
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitInnerNode (NodeVisitor)
    /**
     ** Visit a node referenced by an path element before that last element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @return                  the {@link JsonNode} referenced by the element
     **                          in the parent.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    JsonNode visitInnerNode(final ObjectNode parent, final String field, final Filter valueFilter)
      throws ServiceException {

      final JsonNode node = parent.path(field);
      if (node.isArray() && valueFilter != null) {
        return filterArray((ArrayNode)node, valueFilter, false);
      }
      return node;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitLeafNode
    /**
     ** Visit a node referenced by the last path element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    void visitLeafNode(final ObjectNode parent, final String field, final Filter valueFilter)
      throws ServiceException {

      final JsonNode node = parent.path(field);
      if (node.isArray()) {
        ArrayNode arrayNode = (ArrayNode)node;

        if (valueFilter != null) {
          arrayNode = filterArray((ArrayNode)node, valueFilter, this.remove);
        }
        if (arrayNode.size() > 0) {
          this.values.add(arrayNode);
        }

        if (this.remove && (valueFilter == null || node.size() == 0)) {
          // There are no more values left after removing the matching values.
          // Just remove the field.
          parent.remove(field);
        }
      }
      else if (node.isObject() || node.isValueNode()) {
        this.values.add(node);
        if (this.remove) {
          parent.remove(field);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class UpdatingNodeVisitor
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~
  public static class UpdatingNodeVisitor extends NodeVisitor {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the updated value */
    protected final JsonNode value;

    /** wehether to append or replace array values */
    protected final boolean  append;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>UpdatingNodeVisitor</code>.
     **
     ** @param  value            the update value.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     ** @param  append           <code>true</code> to append the update value or
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    protected UpdatingNodeVisitor(final JsonNode value, final boolean append) {
      this.value  = value.deepCopy();
      this.append = append;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base calsses
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitInnerNode (NodeVisitor)
    /**
     ** Visit a node referenced by an path element before that last element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @return                  the {@link JsonNode} referenced by the element
     **                          in the parent.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    protected JsonNode visitInnerNode(final ObjectNode parent, final String field, final Filter filter)
      throws ServiceException {

      final JsonNode node = parent.path(field);
      if (node.isValueNode() || ((node.isMissingNode() || node.isNull()) && filter != null))
        throw BadRequestException.invalidTarget("Attribute " + field + " does not have a multi-valued or complex value");

      if (node.isMissingNode() || node.isNull()) {
        // create the missing node as an JSON object node.
        ObjectNode newObjectNode = jsonNodeFactory().objectNode();
        parent.set(field, newObjectNode);
        return newObjectNode;
      }
      else if (node.isArray()) {
        ArrayNode arrayNode = (ArrayNode)node;
        if (filter != null) {
          arrayNode = filterArray((ArrayNode)node, filter, false);
          if (arrayNode.size() == 0)
            throw BadRequestException.invalidTarget("Attribute " + field + " does not have a value matching the filter " + filter);
        }
        return arrayNode;
      }
      return node;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   visitLeafNode
    /**
     ** Visit a node referenced by the last path element.
     **
     ** @param  parent           the parent container {@link ObjectNode}.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  field            the field to visit.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  filter           the filter for the value(s) to visit.
     **                          <br>
     **                          Allowed object is {@link Filter}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    protected void visitLeafNode(final ObjectNode parent, final String field, final Filter filter)
      throws ServiceException {

      if (field != null) {
        final JsonNode node = parent.path(field);
        if (!this.append && filter != null) {
          // in replace mode, a value filter requires that the target node
          // be an array and that we can find matching value(s)
          boolean matchesFound = false;
          if (node.isArray()) {
            for (int i = 0; i < node.size(); i++) {
              if (Evaluator.evaluate(filter, node.get(i))) {
                matchesFound = true;
                if (node.get(i).isObject() && value.isObject()) {
                  updateNode((ObjectNode)node.get(i), null, value);
                }
                else {
                  ((ArrayNode)node).set(i, value);
                }
              }
            }
          }
          // exception: this allows filters on singular values if
          // and only if the filter uses the "value" attribute to
          // reference the value of the value node.
          else if (Evaluator.evaluate(filter, node)) {
            matchesFound = true;
            updateNode(parent, field, value);
          }
          if (!matchesFound)
            throw BadRequestException.invalidTarget("Attribute " + field + " does not have a value matching the filter " + filter.toString());
          return;
        }
      }
      updateNode(parent, field, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   updateNode
    /**
     ** Update the value(s) of the field specified by the key in the parent
     ** container node.
     **
     ** @param  parent           the container node.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     ** @param  key              the key of the field to update.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the update value.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     */
    protected void updateNode(final ObjectNode parent, final String key, final JsonNode value) {
      if (value.isNull() || value.isArray() && value.size() == 0) {
        // draft-ietf-scim-core-schema section 2.4 states "Unassigned
        // attributes, the null value, or empty array (in the case of
        // a multi-valued attribute) SHALL be considered to be
        // equivalent in "state".
        return;
      }
      // When key is null, the node to update is the parent it self.
      JsonNode node = key == null ? parent : parent.path(key);
      if (node.isObject()) {
        if (value.isObject()) {
          // Go through the fields of both objects and merge them.
          ObjectNode                            targetObject = (ObjectNode)node;
          ObjectNode                            valueObject = (ObjectNode)value;
          Iterator<Map.Entry<String, JsonNode>> i = valueObject.fields();
          while (i.hasNext()) {
            Map.Entry<String, JsonNode> field = i.next();
            updateNode(targetObject, field.getKey(), field.getValue());
          }
        }
        else {
          // Replace the field.
          parent.set(key, value);
        }
      }
      else if (node.isArray()) {
        if (value.isArray() && this.append) {
          // Append the new values to the existing ones.
          ArrayNode targetArray = (ArrayNode)node;
          ArrayNode valueArray = (ArrayNode)value;
          for (JsonNode valueNode : valueArray) {
            boolean valueFound = false;
            for (JsonNode targetNode : targetArray) {
              if (valueNode.equals(targetNode)) {
                valueFound = true;
                break;
              }
            }
            if (!valueFound) {
              targetArray.add(valueNode);
            }
          }
        }
        else {
          // Replace the field.
          parent.set(key, value);
        }
      }
      else {
        // Replace the field.
        parent.set(key, value);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns <code>true</code> if the string passed in appears to be a
   ** namespace urn.
   ** <br>
   ** That determination is made by looking to see if the string starts with
   ** <code>urn:</code>.
   **
   ** @param  string             the string to check.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if it's a namespace urn, or
   **                            <code>false</code> if not.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean namespace(final String string) {
    return StringUtility.startsWithIgnoreCase(string, "urn:") && string.length() > 4;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the schema namespace urn from a java <code>Class</code>.
   **
   ** @param  clazz              the {@link Class} of the object.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the schema namespace urn for the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String namespace(final Class<?> clazz) {
    // the schema id is the urn.
    // just make sure it begins with urn: ... the field called name is just a
    // human friendly name for the object.
    String id = annotatedId(clazz);
    if (StringUtility.isEmpty(id))
      id = clazz.getCanonicalName();

    // if this doesn't appear to be a urn, stick the "urn:" prefix on it, and
    // use it as a urn anyway.
    return enforceUrn(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceUrn
  /**
   ** Enforce the string passed in to look like a urn.
   ** <br>
   ** If the string starts with <code>urn:</code> it will be returned as is,
   ** however if the string starts with anything else, this method will prepend
   ** <code>urn:</code>.
   ** <br>
   ** This is mainly so that if we have a class that will be used as an
   ** extension schema, we will ensure that its schema will be a urn and
   ** distinguishable from all other unmmapped values.
   **
   ** @param  string             the string to force to be a urn.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the namespace urn.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String enforceUrn(final String string) {
    return (namespace(string)) ? string : "urn:" + string;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotatedId
  /**
   ** Returns the id of the schema from the annotation of the class passed in.
   **
   ** @param  clazz              the {@link Class} to find the schema id
   **                            property of the annotation from.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the id of the schema, or <code>null</code> if
   **                            it was not provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String annotatedId(final Class<?> clazz) {
    return annotatedId(annotatedSchema(clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotatedName
  /**
   ** Returns the name property from the annotation of the class passed in.
   **
   ** @param  clazz              the {@link Class} to find the schema name
   **                            property of the annotation from.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the name of the schema.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String annotatedName(final Class<?> clazz) {
    return annotatedName(annotatedSchema(clazz));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotatedSchema
  /**
   ** Returns the name property from the annotation of the class passed in.
   **
   ** @param  clazz              the {@link Class} to find the schema name
   **                            property of the annotation from.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the name of the schema.
   **                            <br>
   **                            Possible object is {@link Schema}.
   */
  public static Schema annotatedSchema(final Class<?> clazz) {
    return clazz.getAnnotation(Schema.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fieldAnnotation
  /**
   ** Searches for a <code>Field</code> that corresponds to the path passed
   ** (using {@link #lookup(Class, String) lookup}) and tries to find an
   ** annotation attached to such field that matches the
   ** <code>annotationClass</code> parameter.
   **
   ** @param  path               a string denoting a path to a target attribute.
   **                            Examples of valid paths can be: displayName,
   **                            name.givenName, addresses.locality when
   **                            <code>UserResource</code> class is used for
   **                            <code>resourceClass</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resourceClass      the {@link Class} to start the search from.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  annotationClass    the {@link Class} annotation to be inspected
   **                            for the field.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  <T>                the Type parameter for
   **                            <code>annotationClass</code>.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>..
   **
   ** @return                    an object of type <code>T</code>, or
   **                            <code>null</code> if no annotation was found or
   **                            the field itself couldn't be found.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static <T extends Annotation> T fieldAnnotation(final String path, final Class resourceClass, final Class<T> annotationClass) {
    final Field f = lookup(resourceClass, path);
    return f == null ? null : f.getAnnotation(annotationClass);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Inspects a class to search for a field that corresponds to the path passed
   ** using dot notation.
   ** <br>
   ** Every piece of the path (separated by the a dot '.') is expected to have a
   ** field with the same name in the class inspected. When such a field is
   ** found, the remainder of the path is processed using the class associated
   ** to the field, until the path is fully consumed.
   ** <p>
   ** This method starts from an initial class and visits ascendingly the class
   ** hierarchy with a route determined by the components found in the path
   ** parameter.
   **
   ** @param  clazz              the {@link Class} to start the search from.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  path               a string denoting a path to a target attribute.
   **                            Examples of valid paths can be: displayName,
   **                            name.givenName, addresses.locality.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a Field that represents the terminal portion of
   **                            the path, for instance "locality" field for
   **                            "addresses.locality".
   **                            <br>
   **                            If no such field is found (because at some
   **                            point, there was no route to go),
   **                            <code>null</code> is returned.
   **                            <br>
   **                            Possible object is {@link Field}.
   */
  public static Field lookup(final Class<?> clazz, final String path) {
    Field f = null;

    Class<?> current = clazz;
    for (String part : path.split("\\.")) {
      f = findField(current, part);

      if (f != null) {
        current = f.getType();
        if (Collection.class.isAssignableFrom(current)) {
          final Attribute annotation = f.getAnnotation(Attribute.class);
          if (annotation != null)
            current = annotation.multiValueClass();
        }
      }
      else
        break;
    }
    return f;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findField
  /**
   ** Returns a java {@link Field} for a particular name.
   ** <br>
   ** If needed, this method will search through super classes.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The field does not need to be public.
   **
   ** @param  clazz              the java {@link Class} to search.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  name               the name of the field to find.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the java {@link Field}.
   **                            <br>
   **                            Possible object is {@link Field}.
   */
  public static Field findField(final Class<?> clazz, final String name) {
    Class<?> cursor = clazz;
    while (cursor != null) {
      Field[] fields = cursor.getDeclaredFields();
      for (Field field : fields) {
        if (field.getName().equals(name)) {
          return field;
        }
      }
      cursor = cursor.getSuperclass();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectMapper
  /**
   ** Creates an configured SCIM compatible Jackson {@link ObjectMapper}.
   ** <br>
   ** Creating new ObjectMapper instances are expensive so instances should be
   ** shared if possible.
   ** <br>
   ** Alternatively, consider using one of the getObjectReader, getObjectWriter,
   ** getJsonNodeFactory, or valueToTree methods which uses the SDK's
   ** ObjectMapper singleton.
   **
   ** @return                    an Object Mapper with the correct options set
   **                            for serializing and deserializing SCIM JSON
   **                            objects.
   **                            <br>
   **                            Possible object is {@link ObjectMapper}.
   */
  public static ObjectMapper objectMapper() {
    return MAPPER;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectReader
  /**
   ** Factory method to create a SCIM compatible Jackson {@link ObjectReader}
   ** with default settings.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** The resulting instance is NOT usable as is, without defining expected
   ** value type with ObjectReader.forType.
   **
   ** @return                    a Jackson {@link ObjectReader} with default
   **                            settings.
   **                            <br>
   **                            Possible object is {@link ObjectReader}.
   */
  public static ObjectReader objectReader() {
    return MAPPER.reader();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   objectWriter
  /**
   ** Factory method to create a SCIM compatible Jackson {@link ObjectWriter}
   ** with default settings.
   **
   ** @return                    a Jackson {@link ObjectWriter} with default
   **                            settings.
   **                            <br>
   **                            Possible object is {@link ObjectWriter}.
   */
  public static ObjectWriter objectWriter() {
    return MAPPER.writer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeFactory
  /**
   ** Returns the SCIM compatible Jackson {@link JsonNodeFactory} that may be
   ** used to create tree model JsonNode instances.
   **
   ** @return                    the Jackson {@link JsonNodeFactory}.
   **                            <br>
   **                            Possible object is {@link JsonNodeFactory}.
   */
  public static JsonNodeFactory jsonNodeFactory() {
    return MAPPER.getNodeFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueToNode
  /**
   ** Utility method to transforms a POJO to Jackson JSON node.
   ** <br>
   ** This behaves exactly the same as Jackson's ObjectMapper.valueToTree.
   **
   ** @param  <T>                the actual node type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  value              the POJO to transforms.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the transformed {@link JsonNode}.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static <T extends JsonNode> T valueToNode(final Object value) {
    return MAPPER.valueToTree(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nodeToValue
  /**
   ** Utility method to transforms Jackson JSON node to a POJO.
   ** <br>
   ** This behaves exactly the same as Jackson's ObjectMapper.treeToValue.
   **
   ** @param  <T>                the actual node type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  node               the {@link JsonNode} to transforms.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  type               the {@link Class} type of the value to
   **                            transforms.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the transformed POJO.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws JsonProcessingException if an error occurs while binding the JSON
   **                                 node to the value type.
   */
  public static <T> T nodeToValue(final JsonNode node, final Class<T> type)
    throws JsonProcessingException {

    return MAPPER.treeToValue(node, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nodeToValue
  /**
   ** Utility method to transforms Jackson JSON node to a {@link List} of
   ** POJO's.
   ** <br>
   ** This behaves exactly the same as Jackson's ObjectMapper.treeToValue.
   **
   ** @param  <T>                the actual node type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  node               the {@link ArrayNode} to transforms.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   ** @param  type               the {@link Class} type of the value to
   **                            transforms.
   **                            <br>
   **                            Allowed object is {@link Class}.
   **
   ** @return                    the transformed {@link List} POJO.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws JsonProcessingException if an error occurs while binding the JSON
   **                                 node to the value type.
   */
  public static <T> List<T> nodeToValue(final ArrayNode node, final Class<T> type)
    throws JsonProcessingException {

    final CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(List.class, type);
    try {
      return MAPPER.readValue(MAPPER.treeAsTokens(node), collectionType);
    }
    catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo
  /**
   ** Compares two {@link JsonNode}s for order.
   ** <br>
   ** Nodes containing datetime and numerical values are ordered accordingly.
   ** Otherwise, the values' string representation will be compared
   ** lexicographically.
   **
   ** @param  lhs                the first node to be compared.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  rhs                the second node to be compared.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  definition         the attribute definition of the attribute whose
   **                            values to compare or <code>null</code> to
   **                            compare string values using case insensitive
   **                            matching.
   **                            <br>
   **                            Allowed object is {@link Definition}.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as the first argument is less than, equal to,
   **                            or greater than the second.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int compareTo(final JsonNode lhs, final JsonNode rhs, final Definition definition) {
    if (lhs.isTextual() && rhs.isTextual()) {
      Date d1 = dateValue(lhs);
      Date d2 = dateValue(rhs);
      if (d1 != null && d2 != null) {
        return d1.compareTo(d2);
      }
      else {
        if (definition != null && definition.type() == Definition.Type.STRING && definition.caseExact()) {
          return lhs.textValue().compareTo(rhs.textValue());
        }
        return StringUtility.compareIgnoreCase(lhs.textValue(), rhs.textValue());
      }
    }

    if (lhs.isNumber() && rhs.isNumber()) {
      if (lhs.isBigDecimal() || rhs.isBigDecimal()) {
        return lhs.decimalValue().compareTo(rhs.decimalValue());
      }

      if (lhs.isFloatingPointNumber() || rhs.isFloatingPointNumber()) {
        return Double.compare(lhs.doubleValue(), rhs.doubleValue());
      }

      if (lhs.isBigInteger() || rhs.isBigInteger()) {
        return lhs.bigIntegerValue().compareTo(rhs.bigIntegerValue());
      }

      return Long.compare(lhs.longValue(), rhs.longValue());
    }
    // Compare everything else lexicographically
    return lhs.asText().compareTo(rhs.asText());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nodeToDateValue
  /**
   ** Utility method to convert a Jackson node to a {@link Date} object.
   **
   ** @param  node               the {@link JsonNode} to convert.
   **                            The node must be textual.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the converted {@link Date} value.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws IllegalArgumentException if the node is not textual or its value
   **                                  cannot be parsed as a SCIM DateTime
   **                                  value.
   ** @throws ParseException           if a date/time component is out of range
   **                                  or cannot be parsed.
   */
  public static Date nodeToDateValue(final JsonNode node)
    throws IllegalArgumentException
    ,      ParseException {

    // prevent bogus input
    if (!node.isTextual())
      throw new IllegalArgumentException("non-textual node cannot be parsed as DateTime type");

    String text = node.textValue().trim();
    return DateUtility.parseDate(text, DateUtility.XML8601_ZULU).getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addValue
  /**
   ** Add a new value at the provided path to the provided JSON node.
   ** <br>
   ** If the path contains any value filters, they will be ignored. The
   ** following processing rules are applied depending on the path and value to
   ** add:
   ** <ul>
   **   <li>If the path is a root path and targets the core or extension
   **       attributes, the value must be a JSON object containing the set of
   **       attributes to be added to the resource.
   **   <li>If the path does not exist, the attribute and value is added.
   **   <li>If the path targets a complex attribute (an attribute whose value is
   **       a JSON Object), the value must be a JSON object containing the set
   **       of sub-attributes to be added to the complex value.
   **   <li>If the path targets a multi-valued attribute (an attribute whose
   **       value if a JSON Array), the value to add must be a JSON array
   **       containing the set of values to be added to the attribute.
   **   <li>If the path targets a single-valued attribute, the existing value is
   **       replaced.
   **   <li>If the path targets an attribute that does not exist (has not
   **       value), the attribute is added with the new value.
   **   <li>If the path targets an existing attribute, the value is replaced.
   **   <li>If the path targets an existing attribute which already contains the
   **       value specified, no changes will be made to the node.
   ** </ul>
   **
   ** @param  path               the path to the attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  node               the JSON object node containing the attribute.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  value              the value to add.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static void addValue(final Path path, final ObjectNode node, final JsonNode value)
    throws ServiceException {

    final UpdatingNodeVisitor visitor = new UpdatingNodeVisitor(value, true);
    traverse(visitor, node, 0, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeValue
  /**
   ** Remove the value at the provided path.
   ** <br>
   ** The following processing rules are applied:
   ** <ul>
   **   <li>If the path targets a single-valued attribute, the attribute and its
   **       associated value is removed.
   **   <li>If the path targets a multi-valued attribute and no value filter is
   **       specified, the attribute and all values are removed.
   **   <li>If the path targets a multi-valued attribute and a value filter is
   **       specified, the values matched by the filter are removed. If after
   **       removal of the selected values, no other values remain, the
   **        multi-valued attribute is removed.
   ** </ul>
   **
   ** @param  path               the path to the attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  node               the JSON object node containing the attribute.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the list of nodes that were removed.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static List<JsonNode> removeValue(final Path path, final ObjectNode node)
    throws ServiceException {

    final GatheringNodeVisitor visitor = new GatheringNodeVisitor(true);
    traverse(visitor, node, 0, path);
    return visitor.values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceValue
  /**
   ** Update the value at the provided path.
   ** <br>
   ** The following processing rules are applied:
   ** <ul>
   **   <li>If the path is a root path and targets the core or extension
   **       attributes, the value must be a JSON object containing the set of
   **       attributes to be replaced on the resource.
   **   <li>If the path targets a single-valued attribute, the attribute's value
   **       is replaced.
   **   <li>If the path targets a multi-valued attribute and no value filter is
   **       specified, the attribute and all values are replaced.
   **  <li>If the path targets an attribute that does not exist, treat the
   **      operation as an add.
   **  <li>If the path targets a complex attribute (an attribute whose value is
   **      a JSON Object), the value must be a JSON object containing the set of
   **      sub-attributes to be replaced in the complex value.
   **  <li>If the path targets a multi-valued attribute and a value filter is
   **      specified that matches one or more values of the multi-valued
   **      attribute, then all matching record values will be replaced.
   **  <li>If the path targets a complex multi-valued attribute with a value
   **      filter and a specific sub-attribute (for example,
   **      "addresses[type eq "work"].streetAddress"), the matching
   **      sub-attribute of all matching records is replaced.
   **  <li>If the path targets a multi-valued attribute for which a value filter
   **      is specified and no records match was made, the NoTarget exception
   **      will be thrown.
   ** </ul>
   **
   ** @param  path               the path to the attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  node               the JSON object node containing the attribute.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  value              the replacement value.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static void replaceValue(final Path path, final ObjectNode node, final JsonNode value)
    throws ServiceException {

    final UpdatingNodeVisitor visitor = new UpdatingNodeVisitor(value, false);
    traverse(visitor, node, 0, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns a single value (node) from an {@link ObjectNode} at the supplied
   ** path.
   ** <br>
   ** It is expected that there will only be one matching path. If there are
   ** multiple matching paths (for example a path with filters can match
   ** multiple nodes), an exception will be thrown.
   ** <br>
   ** For example, given the following {@link ObjectNode}:
   ** <pre>
   **   {
   **     "name":"Bob",
   **   , "favoriteColors":["red","green","blue"]
   **   }
   ** </pre>
   ** value(Path.from("name") will return a <code>TextNode</code> containing
   ** "<code>Bob</code>"
   ** <br>
   ** value(Path.fromString("favoriteColors")) will return an {@link ArrayNode}
   ** containing <code>TextNode</code>s with the following values -
   ** "<code>red</code>", "<code>green</code>", and "<code>blue</code>".
   **
   ** @param  path               the path to the attributes whose values to
   **                            retrieve.
   ** @param  node               the {@link ObjectNode} to find the path in.
   **
   ** @return                    the node located at the path, or a NullNode.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static JsonNode value(final Path path, final ObjectNode node)
    throws ServiceException {

    final GatheringNodeVisitor visitor = new GatheringNodeVisitor(false);
    traverse(visitor, node, 0, path);
    if(visitor.values.isEmpty()) {
      return NullNode.getInstance();
    }
    else {
      return visitor.values.get(0);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matchPath
  /**
   ** Returns all JSON nodes referenced by the provided path.
   ** <br>
   ** If the path traverses through a JSON array, all nodes the array will be
   ** traversed.
   ** <br>
   ** For example, given the following {@link ObjectNode}:
   ** <pre>
   **   {
   **     "emails": [
   **       { "type": "work" , "value": "bob@work.com" }
   **     , { "type": "home",  "value": "bob@home.com" }
   **     ]
   **   }
   ** </pre>
   ** Calling getValues with path of emails.value will return a list of all
   ** TextNodes of the "<code>value</code>" field in the "<code>emails</code>"
   ** array:
   ** <pre>
   **   [ TextNode("bob@work.com"), TextNode("bob@home.com") ]
   ** </pre>
   ** However, if the last element of the path references a JSON array, the
   ** entire ArrayNode will returned. For example given the following
   ** ObjectNode:
   ** <pre>
   **   {
   **     "books": [
   **       { "title": "Brown Bear, Brown Bear, What Do You See?"
   **       , "authors": ["Bill Martin, Jr.", "Eric Carle"]
   **        }
   **     , { "title": "The Cat In The Hat"
   **        , "authors": ["Dr. Seuss"]
   **       }
   **      ]
   **   }
   ** </pre>
   ** Calling getValues with path of books.authors will return a list of all
   ** ArrayNodes of the "<code>authors</code>" field in the "<code>books</code>"
   ** array:
   ** <pre>
   **   [ ArrayNode(["Bill Martin, Jr.", "Eric Carle"]), ArrayNode(["Dr. Seuss"]) ]
   ** </pre>
   **
   ** @param  path               the path to the attributes whose values to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  node               the JSON node representing the SCIM resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @return                    the {@link List} of all JSON nodes referenced
   **                            by the provided path.
   **                            <br>
   **                            Possible object is {@link List} where each node
   **                            is of type {@link JsonNode}.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static List<JsonNode> matchPath(final Path path, final ObjectNode node)
    throws ServiceException {

    final GatheringNodeVisitor visitor = new GatheringNodeVisitor(false);
    traverse(visitor, node, 0, path);
    return visitor.values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traverseValues
  /**
   ** Recursively travers JSON nodes based on a path using the provided node
   ** visitor.
   **
   ** @param  visitor            the {@link NodeVisitor} to use to handle the
   **                            traversed nodes.
   **                            <br>
   **                            Allowed object is {@link NodeVisitor}.
   ** @param  node               the JSON node representing the SCIM resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  path               the path to the attributes whose values to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  public static void traverseValues(final NodeVisitor visitor, final ObjectNode node, final Path path)
    throws ServiceException {

    traverse(visitor, node, 0, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traverse
  /**
   ** Internal method to recursively gather values based on path.
   **
   ** @param  visitor            the {@link NodeVisitor} to use to handle the
   **                            traversed nodes.
   **                            <br>
   **                            Allowed object is {@link NodeVisitor}.
   ** @param  node               the JSON node representing the SCIM resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  index              the index to the current path element.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  path               the path to the attributes whose values to
   **                            retrieve.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  private static void traverse(final NodeVisitor visitor, final ObjectNode node, final int index, final Path path)
    throws ServiceException {

    String field       = null;
    Filter valueFilter = null;
    int    pathDepth   = path.size();
    if (path.namespace() != null) {
      if (index > 0) {
        Path.Element element = path.element(index - 1);
        field = element.attribute();
        valueFilter = element.filter();
      }
      else {
        field = path.namespace();
      }
      pathDepth += 1;
    }
    else if (path.size() > 0) {
      Path.Element element = path.element(index);
      field = element.attribute();
      valueFilter = element.filter();
    }

    if (index < pathDepth - 1) {
      JsonNode child = visitor.visitInnerNode(node, field, valueFilter);
      if (child.isArray()) {
        for (JsonNode value : child) {
          if (value.isObject()) {
            traverse(visitor, (ObjectNode)value, index + 1, path);
          }
        }
      }
      else if (child.isObject()) {
        traverse(visitor, (ObjectNode)child, index + 1, path);
      }
    }
    else {
      visitor.visitLeafNode(node, field, valueFilter);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dateValue
  /**
   ** Try to parse out a date from a JSON text node.
   **
   ** @param  node               the JSON node to parse.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    a parsed date instance or <code>null</code> if
   **                            the text is not an xsd:dateTime formatted date
   **                            and time string.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  private static Date dateValue(final JsonNode node) {
    try {
      return nodeToDateValue(node);
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
      return null;
    }
    catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotatedId
  /**
   ** Returns the id property from schema annotation.
   ** <br>
   ** If the the id attribute was <code>null</code>, a schema id is generated.
   **
   ** @param  annotation         the SCIM Schema annotation.
   **                            <br>
   **                            Allowed object is {@link Schema}.
   **
   ** @return                    the id of the schema, or <code>null</code> if it
   **                            was not provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String annotatedId(final Schema annotation) {
    return (annotation != null) ? annotation.id() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   annotatedName
  /**
   ** Returns the name property from schema annotation.
   **
   ** @param  annotation         the SCIM Schema annotation.
   **                            <br>
   **                            Allowed object is {@link Schema}.
   **
   ** @return                    the name of the schema or a generated name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String annotatedName(final Schema annotation) {
    if (annotation != null) {
      return annotation.name();
    }
    return null;
  }
}