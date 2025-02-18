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
    Subsystem   :   Generic REST Library

    File        :   Support.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Support.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;

import java.text.ParseException;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.core.utility.DateUtility;
import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.rest.ProcessingException;
import oracle.hst.platform.rest.entity.Evaluator;
import oracle.hst.platform.rest.marshal.MapperFactory;

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
    // Method: visitInnerNode
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
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    abstract JsonNode visitInnerNode(final ObjectNode parent, final String field, final Filter<JsonNode> filter)
      throws ProcessingException;

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
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    abstract void visitLeafNode(final ObjectNode parent, final String field, final Filter filter)
      throws ProcessingException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: filterArray
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
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    ArrayNode filterArray(final ArrayNode array, final Filter<JsonNode> filter, final boolean remove)
      throws ProcessingException {

      final ArrayNode  matchingArray = nodeFactory().arrayNode();
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
  // class MatchVisitor
  // ~~~~~ ~~~~~~~~~~~~
  protected static final class MatchVisitor extends NodeVisitor {

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
     ** Constructs a new <code>MatchVisitor</code>.
     **
     ** @param  remove           <code>true</code> to remove the gathered values
     **                          from the container node or <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    protected MatchVisitor(final boolean remove) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.remove = remove;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: visitInnerNode (NodeVisitor)
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
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    @Override
    JsonNode visitInnerNode(final ObjectNode parent, final String field, final Filter<JsonNode> valueFilter)
      throws ProcessingException {

      final JsonNode node = parent.path(field);
      if (node.isArray() && valueFilter != null) {
        return filterArray((ArrayNode)node, valueFilter, false);
      }
      return node;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: visitLeafNode
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
     ** @throws ProcessingException if the filter is not valid for matching.
     */
    @Override
    void visitLeafNode(final ObjectNode parent, final String field, final Filter valueFilter)
      throws ProcessingException {

      final JsonNode node = parent.path(field);
      if (node.isArray()) {
        ArrayNode arrayNode = (ArrayNode)node;

        if (valueFilter != null) {
          arrayNode = filterArray((ArrayNode)node, valueFilter, this.remove);
        }
        
        // Add array regardless length of array
        this.values.add(arrayNode);

        if (this.remove && (valueFilter == null || node.size() == 0)) {
          // there are no more values left after removing the matching values
          // just remove the field
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
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    return MapperFactory.instance;
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
    return MapperFactory.instance.reader();
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
    return MapperFactory.instance.writer();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nodeFactory
  /**
   ** Returns the SCIM compatible Jackson {@link JsonNodeFactory} that may be
   ** used to create tree model JsonNode instances.
   **
   ** @return                    the Jackson {@link JsonNodeFactory}.
   **                            <br>
   **                            Possible object is {@link JsonNodeFactory}.
   */
  public static JsonNodeFactory nodeFactory() {
    return MapperFactory.instance.getNodeFactory();
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
    return MapperFactory.instance.valueToTree(value);
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

    return MapperFactory.instance.treeToValue(node, type);
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

    final CollectionType collectionType = MapperFactory.instance.getTypeFactory().constructCollectionType(List.class, type);
    try {
      return MapperFactory.instance.readValue(MapperFactory.instance.treeAsTokens(node), collectionType);
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
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as the first argument is less than, equal to,
   **                            or greater than the second.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int compareTo(final JsonNode lhs, final JsonNode rhs) {
    if (lhs.isTextual() && rhs.isTextual()) {
      Date d1 = dateValue(lhs);
      Date d2 = dateValue(rhs);
      if (d1 != null && d2 != null) {
        return d1.compareTo(d2);
      }
      else {
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
    // compare everything else lexicographically
    return lhs.asText().compareTo(rhs.asText());
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
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  public static List<JsonNode> matchPath(final Path path, final ObjectNode node)
    throws ProcessingException {

    // Handle schema extension
    ObjectNode  objNode = node;
    if(path.schemaUrn() != null){
      objNode = (ObjectNode)node.path(path.schemaUrn());
    }

    final MatchVisitor visitor = new MatchVisitor(false);
    traverse(visitor, objNode, 0, path);
    return visitor.values;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePath
  /**
   ** Remove and return all JSON nodes referenced by the provided path.
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
   ** Calling getValues with path of books.authors will remove a list of all
   ** ArrayNodes of the "<code>authors</code>" field in the "<code>books</code>"
   ** array:
   ** <pre>
   **   [ ArrayNode(["Bill Martin, Jr.", "Eric Carle"]), ArrayNode(["Dr. Seuss"]) ]
   ** </pre>
   **
   ** @param  path               the path to the attributes whose values to
   **                            remove.
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
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  public static List<JsonNode> removePath(final Path path, final ObjectNode node)
    throws ProcessingException {

    // Handle schema extension
    ObjectNode  objNode = node;
    if(path.schemaUrn() != null){
      objNode = (ObjectNode)node.path(path.schemaUrn());
    }

    final MatchVisitor visitor = new MatchVisitor(true);
    traverse(visitor, objNode, 0, path);
    return visitor.values;
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
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  private static void traverse(final NodeVisitor visitor, final ObjectNode node, final int index, final Path path)
    throws ProcessingException {

    String field       = null;
    Filter valueFilter = null;
    int    pathDepth   = path.size();
  
    if (path.size() > 0) {
      Path.Segment segment = path.segment(index);
      field       = segment.attribute();
      valueFilter = segment.filter();
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
      // die silently
      return null;
    }
    catch (ParseException e) {
      // die silently
      return null;
    }
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
  private static Date nodeToDateValue(final JsonNode node)
    throws IllegalArgumentException
    ,      ParseException {

    // prevent bogus input
    if (!node.isTextual())
      throw new IllegalArgumentException("non-textual node cannot be parsed as DateTime type");

    String text = node.textValue().trim();
    return DateUtility.parseDate(text, DateUtility.XML8601_ZULU).getTime();
  }
}