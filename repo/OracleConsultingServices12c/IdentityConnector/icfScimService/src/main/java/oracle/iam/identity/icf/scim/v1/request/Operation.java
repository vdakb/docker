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
    Subsystem   :   Generic SCIM Library

    File        :   Operation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Operation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v1.request;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.scim.Path;
import oracle.iam.identity.icf.scim.Filter;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.v1.schema.SchemaFactory;

////////////////////////////////////////////////////////////////////////////////
// class Operation
// ~~~~~ ~~~~~~~~~
/**
 ** An individual patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Operation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path to the attribute to apply the patch operation */
  private final Path     path;

  /** The value of the attribute to apply the patch operation */
  private final JsonNode value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add/replace or to remove.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Operation(final Path path, final JsonNode value)
    throws ServiceException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (path != null) {
      if (path.size() > 2)
        throw ServiceException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_VALUE_DEPTH));

      if (path.size() == 2) {
        final Filter filter = path.element(1).filter();
        // allow use of the special case "value" path to reference the value
        // itself.
        // any other value filter is for a sub-attribute, which is not
        // permitted.
        if (filter != null && !filter.path().element(0).attribute().equals("value"))
          throw ServiceException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_VALUE_FILTER));
      }
    }

    // prevent bogus state
    if (value == null || value.isNull() || ((value.isArray() || value.isObject()) && value.size() == 0))
      throw ServiceException.invalidSyntax("Value field must not be null or an empty container");

      if (path == null && !value.isObject())
        throw ServiceException.invalidSyntax("Value field must be a JSON object containing the attributes to replace");

    // initialize instance attributes
    this.path  = path;
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path targeted by this operation.
   **
   ** @return                    the path targeted by this operation.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  @JsonIgnore
  public final Path path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   node
  /**
   ** Returns the value or values of the patch operation as a {@link JsonNode}.
   ** <br>
   ** The returned {@link JsonNode} is a copy so it may be altered without
   ** altering this operation.
   **
   ** @return                    the value or values of the patch operation, or
   **                            <code>null</code> if this operation is a remove
   **                            operation.
   **                            <br>
   **                            Possible object is {@link JsonNode}.
   */
  @JsonIgnore
  public JsonNode node() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the patch operation.
   **
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    the value of the patch operation.
   **                            <br>
   **                            Possible object is <code>&lt;T&gt;</code>.
   **
   ** @throws JsonProcessingException  if the value can not be parsed to the
   **                                  type specified by the Java class object.
   ** @throws IllegalArgumentException if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  public <T> T value(final Class<T> clazz)
    throws JsonProcessingException
    ,      IllegalArgumentException {

    return Support.objectReader().treeToValue(this.value, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns all values of the patch operation.
   **
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   **
   ** @return                    the values of the patch operation.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            elementis of type <code>&lt;T&gt;</code>.
   **
   ** @throws JsonProcessingException  if the value can not be parsed to the
   **                                  type specified by the Java class object.
   */
  public <T> List<T> values(final Class<T> clazz)
    throws JsonProcessingException {

    final ArrayList<T> objects = new ArrayList<T>(this.value.size());
    for(JsonNode node : this.value) {
      objects.add(Support.objectReader().treeToValue(node, clazz));
    }
    return objects;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final ObjectNode value)
    throws ServiceException {

    return build(null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value to replace.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Boolean value)
    throws ServiceException {

    return build(path, BooleanNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Integer value)
    throws ServiceException {

    return build(path, IntNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Long value)
    throws ServiceException {

    return build(path, LongNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Double value)
    throws ServiceException {

    return build(path, DoubleNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Date value)
    throws ServiceException {

    return build(path, Support.valueToNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final URI value)
    throws ServiceException {

    return replace(path, value.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final String value)
    throws ServiceException {

    return build(path, TextNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the values to replace.
   **                            The values must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final List<Object> value)
    throws ServiceException {

    final ArrayNode node = Support.objectMapper().createArrayNode();
    for (Object cursor : value) {
      node.add(Support.valueToNode(cursor));
    }
    return build(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>byte[]</code>.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final byte[] value)
    throws ServiceException {

    return replace(path, Base64Variants.getDefaultVariant().encode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Factory method to create a new patch remove operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the values to remove.
   **                            The values must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the new patch remove operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation remove(final String path, final List<Object> value)
    throws ServiceException {

    final ArrayNode node = Support.objectMapper().createArrayNode();
    for (Object cursor : value) {
      node.addObject().put("operation", "delete").put("value", String.class.cast(cursor));
    }
    return build(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to replace.
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation build(final String path, final JsonNode value)
    throws ServiceException {

    return new Operation(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   **
   ** @return                  a hash code value for this object.
   **                          <br>
   **                          Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
      int result = this.path != null ? this.path.hashCode() : 0;
      result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Operation</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Operation</code>s may be different even though they contain the same
   **set of names with the same values, but in a different order.
   **
   ** @param  other            the reference object with which to compare.
   **                          <br>
   **                          Allowed object is {@link Object}.
   **
   ** @return                  <code>true</code> if this object is the same as
   **                          the object argument; <code>false</code>
   **                          otherwise.
   **                          <br>
   **                          Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    final Operation that = (Operation)other;
    if (path() != null ? !path().equals(that.path()) : that.path() != null) {
      return false;
    }
    return this.value.equals(that.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character " " (space).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                   the string representation of this instance.
   */
  @Override
  public String toString() {
    try {
      return Support.objectWriter().writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Apply this patch operation to an {@link ObjectNode}.
   **
   ** @param  node               the {@link ObjectNode} to apply this patch
   **                            operation to.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @throws ServiceException    if the patch operation is invalid.
   */
  public void apply(final ObjectNode node)
    throws ServiceException {

    Support.addValue(this.path == null ? Path.build() : this.path, node, this.value);
    enforceNamespace(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceNamespace
  /**
   ** Implicitly add any schema URNs of any extended attributes that are missing
   ** from the schemas attribute.
   **
   ** @param  node               the {@link ObjectNode} to apply this patch
   **                            operation to.
   */
  protected void enforceNamespace(final ObjectNode node) {
    // Implicitly add the schema URN of any extended attributes to the
    // schemas attribute.
    JsonNode schemasNode = node.path(SchemaFactory.SCHEMAS.name());
    if (schemasNode.isArray()) {
      ArrayNode schemas = (ArrayNode)schemasNode;
      if (path() == null) {
        Iterator<String> i = node().fieldNames();
        while (i.hasNext()) {
          String field = i.next();
          if (Support.namespace(field)) {
            enforceNamespace(schemas, field);
          }
        }
      }
      else if (path().namespace() != null) {
        enforceNamespace(schemas, path().namespace());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enforceNamespace
  /**
   ** Enforce that all nodes in the JSON array passed in passed in look like a
   ** urn.
   ** <br>
   ** If the node value starts with <code>urn:</code> it will be left as it is,
   ** however if the node value starts with anything else, this method will
   ** prepend <code>urn:</code>.
   ** <br>
   ** This is mainly so that if we have a class that will be used as an
   ** extension schema, we will ensure that its schema will be a urn and
   ** distinguishable from all other unmmapped values.
   */
  private void enforceNamespace(final ArrayNode schemas, final String urn) {
    for (JsonNode node : schemas) {
      if (node.isTextual() && node.textValue().equalsIgnoreCase(urn)) {
        return;
      }
    }
    schemas.add(urn);
  }
}