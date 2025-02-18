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

package oracle.iam.identity.icf.scim.v2.request;

import java.util.List;
import java.util.Date;
import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.rest.resource.ServiceBundle;

import oracle.iam.identity.icf.scim.Path;
import oracle.iam.identity.icf.scim.Filter;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.annotation.Attribute;

import oracle.iam.identity.icf.scim.v2.schema.SchemaFactory;

import static oracle.iam.identity.icf.scim.annotation.Definition.Returned;
import static oracle.iam.identity.icf.scim.annotation.Definition.Mutability;

////////////////////////////////////////////////////////////////////////////////
// abstract class Operation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** An individual patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="op")
@JsonSubTypes(
  { @JsonSubTypes.Type(value=Operation.Addition.class, name=Operation.ADD)
  , @JsonSubTypes.Type(value=Operation.Remove.class,   name=Operation.REMOVE)
  , @JsonSubTypes.Type(value=Operation.Replace.class,  name=Operation.REPLACE)
  }
)
public abstract class Operation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String ADD     = "add";
  static final String REMOVE  = "remove";
  static final String REPLACE = "replace";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("path")
  @Attribute(description="Attribute path", required=true, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT)
  private final Path path;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** This enumeration defines the set of possible operation types that may be
   ** used for SCIM 2 patch operations.
   */
  public static enum Type {
      /** The add operation type. */
      ADD(Operation.ADD),

    /** The replace operation type. */
    REPLACE(Operation.REPLACE),

    /** The remove operation type. */
    REMOVE(Operation.REMOVE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The lower case string value for this operation type. */
    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Type</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in SCIM operations)
     **                          of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this operation type.
     **
     ** @return                  a string representation of this operation type.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return value();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Addition
  // ~~~~~ ~~~~~~~~
  /**
   ** The operation to add an attribute value by a patch operation.
   */
  public static final class Addition extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    private final JsonNode value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new add patch operation.
     **
     ** @param  path             the path targeted by this patch operation.
     **                          <br>
     **                          Allowed object is {@link Path}.
     ** @param  value            the value(s) to add.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     **
     ** @throws ServiceException if the path is invalid.
     */
    @JsonCreator
    private Addition(final @JsonProperty(value="path") Path path, final @JsonProperty(value="value", required=true) JsonNode value)
      throws ServiceException {

      // ensure inheritance
      super(path);

      if (value == null || value.isNull() || ((value.isArray() || value.isObject()) && value.size() == 0))
        throw ServiceException.invalidSyntax("Value field must not be null or an empty container");

      if (path == null && !value.isObject())
        throw ServiceException.invalidSyntax("Value field must be a JSON object containing the attributes to add");

      if (path != null) {
        final Iterator<Path.Element> i = path.iterator();
        while (i.hasNext()) {
          if(i.next().filter() != null)
            throw ServiceException.invalidPath("Path field for add operations must not include any value selection filters");
        }
      }
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (Operation)
    /**
     ** Returns the operation type.
     **
     ** @return                  the operation type.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    public final Type type() {
      return Type.ADD;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: node (Operation)
    /**
     ** Returns the value or values of the patch operation as a {@link JsonNode}.
     ** <br>
     ** The returned {@link JsonNode} is a copy so it may be altered without
     ** altering this operation.
     **
     ** @return                  the value or values of the patch operation, or
     **                          <code>null</code> if this operation is a remove
     **                          operation.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     */
    @Override
    public JsonNode node() {
      return this.value.deepCopy();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (Operation)
    /**
     ** Returns the value of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the value of the patch operation.
     **                          <br>
     **                          Possible object is <code>&lt;T&gt;</code>.
     **
     ** @throws ServiceException         if the operation contains more than one
     **                                  value, in which case, the values method
     **                                  should be used to retrieve all values.
     ** @throws JsonProcessingException  if the value can not be parsed to the
     **                                  type specified by the Java class
     **                                  object.
     */
    @Override
    public <T> T value(final Class<T> clazz)
        throws ServiceException
        ,      JsonProcessingException {

      if (this.value.isArray())
        throw ServiceException.invalidValue("Patch operation contains multiple values");

      return Support.objectReader().treeToValue(this.value, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (Operation)
    /**
     ** Returns all values of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the values of the patch operation.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          elementis of type <code>&lt;T&gt;</code>.
     **
     ** @throws ServiceException        if the path is invalid.
     ** @throws JsonProcessingException if the value can not be parsed to the
     **                                 type specified by the Java class object.
     */
    @Override
    public <T> List<T> values(final Class<T> clazz)
      throws ServiceException
      ,      JsonProcessingException {

      // RFC 7644 3.5.2.1. Add Operation (3rd bullet)
      // If the target location specifies a complex attribute, a set of
      // sub-attributes SHALL be specified in the "value" parameter.
      if (!this.value.isArray())
        throw ServiceException.invalidValue("Value field must be a JSON array containing the attributes to add.");

      final ArrayList<T> objects = new ArrayList<T>(value.size());
      for (JsonNode node : this.value) {
        objects.add(Support.objectReader().treeToValue(node, clazz));
      }
      return objects;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Operation)
    /**
     ** Apply this patch operation to an {@link ObjectNode}.
     **
     ** @param  node             the {@link ObjectNode} to apply this patch
     **                          operation to.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    public void apply(final ObjectNode node)
      throws ServiceException {

      Support.addValue(path() == null ? Path.build() : path(), node, this.value);
      enforceNamespace(node);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
      return Objects.hash(path(), this.value.hashCode());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Addition</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>Addition</code>s filter may be different even though they contain
     ** the same set of names with the same values, but in a different order.
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
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Addition that = (Addition)other;
      return Objects.equals(path(),     that.path())
          && Objects.equals(this.value, that.value)
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Replace
  // ~~~~~ ~~~~~~~
  /**
   ** The operation to replace an attribute value by a patch operation.
   */
  public static final class Replace extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    private final JsonNode value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new replace patch operation.
     **
     ** @param  path             the path targeted by this patch operation.
     **                          <br>
     **                          Allowed object is {@link Path}.
     ** @param  value            the value(s) to add.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     **
     ** @throws ServiceException if the path is invalid.
     */
    @JsonCreator
    private Replace(final @JsonProperty(value="path") Path path, final @JsonProperty(value="value", required=true) JsonNode value)
      throws ServiceException {

      // ensure inheritance
      super(path);

      if (value == null || value.isNull() || ((value.isArray() || value.isObject()) && value.size() == 0))
        throw ServiceException.invalidSyntax("Value field must not be null or an empty container");

      if (path == null && !value.isObject())
        throw ServiceException.invalidSyntax("Value field must be a JSON object containing the attributes to replace");

      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (Operation)
    /**
     ** Returns the operation type.
     **
     ** @return                  the operation type.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    public final Type type() {
      return Type.REPLACE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: node (Operation)
    /**
     ** Returns the value or values of the patch operation as a {@link JsonNode}.
     ** <br>
     ** The returned {@link JsonNode} is a copy so it may be altered without
     ** altering this operation.
     **
     ** @return                  the value or values of the patch operation, or
     **                          <code>null</code> if this operation is a remove
     **                          operation.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     */
    @Override
    public JsonNode node() {
      return this.value.deepCopy();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (Operation)
    /**
     ** Returns the value of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the value of the patch operation.
     **                          <br>
     **                          Possible object is <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException  if the value can not be parsed to the
     **                                  type specified by the Java class
     **                                  object.
     ** @throws IllegalArgumentException if the operation contains more than one
     **                                  value, in which case, the values method
     **                                  should be used to retrieve all values.
     */
    @Override
    public <T> T value(final Class<T> clazz)
        throws JsonProcessingException
        ,      ServiceException {

      if(this.value.isArray())
        throw ServiceException.invalidValue("Patch operation contains multiple values");

      return Support.objectReader().treeToValue(this.value, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (Operation)
    /**
     ** Returns all values of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the values of the patch operation.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          elementis of type <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException if the value can not be parsed to the
     **                                 type specified by the Java class object.
     */
    @Override
    public <T> List<T> values(final Class<T> clazz)
      throws JsonProcessingException {

      final ArrayList<T> objects = new ArrayList<T>(value.size());
      for(JsonNode node : this.value) {
        objects.add(Support.objectReader().treeToValue(node, clazz));
      }
      return objects;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Operation)
    /**
     ** Apply this patch operation to an {@link ObjectNode}.
     **
     ** @param  node             the {@link ObjectNode} to apply this patch
     **                          operation to.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    public void apply(final ObjectNode node)
      throws ServiceException {

      Support.replaceValue(path() == null ? Path.build() : path(), node, this.value);
      enforceNamespace(node);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
      return Objects.hash(path(), this.value.hashCode());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Replace</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>Replace</code>s filter may be different even though they contain
     ** the same set of names with the same values, but in a different order.
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
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Replace that = (Replace)other;
      return Objects.equals(path(),     that.path())
          && Objects.equals(this.value, that.value)
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Remove
  // ~~~~~ ~~~~~~
  /**
   ** The operation to remove an attribute value by a patch operation.
   */
  public static final class Remove extends Operation {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new remove patch operation.
     **
     ** @param  path             the path targeted by this patch operation.
     **                          <br>
     **                          Allowed object is {@link Path}.
     ** @param  value            the value(s) to add.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     **
     ** @throws ServiceException if the path is invalid.
     */
    @JsonCreator
    private Remove(final @JsonProperty(value="path", required=true) Path path)
      throws ServiceException {
      // ensure inheritance
      super(path);

      if(path == null)
        throw ServiceException.invalidPath("Path field must not be null for remove operations");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (Operation)
    /**
     ** Returns the operation type.
     **
     ** @return                  the operation type.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    public final Type type() {
      return Type.REMOVE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: node (Operation)
    /**
     ** Returns the value or values of the patch operation as a {@link JsonNode}.
     ** <br>
     ** The returned {@link JsonNode} is a copy so it may be altered without
     ** altering this operation.
     **
     ** @return                  the value or values of the patch operation, or
     **                          <code>null</code> if this operation is a remove
     **                          operation.
     **                          <br>
     **                          Possible object is {@link JsonNode}.
     */
    @Override
    public JsonNode node() {
      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (Operation)
    /**
     ** Returns the value of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the value of the patch operation.
     **                          <br>
     **                          Possible object is <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException  if the value can not be parsed to the
     **                                  type specified by the Java class
     **                                  object.
     ** @throws ServiceException         if the operation contains more than one
     **                                  value, in which case, the values method
     **                                  should be used to retrieve all values.
     */
    @Override
    public <T> T value(final Class<T> clazz)
        throws JsonProcessingException
        ,      ServiceException {

      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (Operation)
    /**
     ** Returns all values of the patch operation.
     **
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  the values of the patch operation.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          elementis of type <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException if the value can not be parsed to the
     **                                 type specified by the Java class object.
     */
    @Override
    @SuppressWarnings("oracle.jdeveloper.java.null-collection-return")
    public <T> List<T> values(final Class<T> clazz)
      throws JsonProcessingException {

      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Operation)
    /**
     ** Apply this patch operation to an {@link ObjectNode}.
     **
     ** @param  node             the {@link ObjectNode} to apply this patch
     **                          operation to.
     **                          <br>
     **                          Allowed object is {@link ObjectNode}.
     **
     ** @throws ServiceException if the filter is not valid for matching.
     */
    @Override
    public void apply(final ObjectNode node)
      throws ServiceException {

      Support.removeValue(path(), node);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
      return path() != null ? path().hashCode() : 0;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Remove</code>s filter are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Remove</code>s filter may be different even though they contain
     ** the same set of names with the same values, but in a different order.
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
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Remove that = (Remove)other;
      return Objects.equals(path(), that.path());
    }
  }

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
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public Operation(final Path path)
    throws ServiceException {

    // ensure inheritance
    super();

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
    this.path = path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the operation type.
   **
   ** @return                    the operation type.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  @JsonIgnore
  public abstract Type type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path targeted by this operation.
   **
   ** @return                    the path targeted by this operation.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
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
  public abstract JsonNode node();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the patch operation.
   **
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the value of the patch operation.
   **                            <br>
   **                            Possible object is <code>&lt;T&gt;</code>.
   **
   ** @throws ServiceException         if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   ** @throws JsonProcessingException  if the value can not be parsed to the
   **                                  type specified by the Java class object.
   */
  public abstract <T> T value(final Class<T> clazz)
    throws ServiceException
    ,      JsonProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns all values of the patch operation.
   **
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   **
   ** @return                    the values of the patch operation.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            elementis of type <code>&lt;T&gt;</code>.
   **
   ** @throws ServiceException        if the path is invalid.
   ** @throws JsonProcessingException if the value can not be parsed to the
   **                                 type specified by the Java class object.
   */
  public abstract <T> List<T> values(final Class<T> clazz)
    throws ServiceException
    ,      JsonProcessingException;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new patch operation based on the parameters
   ** provided.
   **
   ** @param  type               the operation type.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s).
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation build(final Type type, final String path, final JsonNode value)
    throws ServiceException {

    return build(type, Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new patch operation based on the parameters provided.
   **
   ** @param  type               the operation type.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s).
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid or an unknown operation
   **                            type is given.
   */
  public static Operation build(final Type type, final Path path, final JsonNode value)
    throws ServiceException {

    switch (type) {
      case ADD     : return add(path, value);
      case REMOVE  : return remove(path);
      case REPLACE : return replace(path, value);
      default      : throw new IllegalArgumentException("Unknown patch operation type " + type);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBoolean(final String path, final Boolean value)
    throws ServiceException {

    return addBoolean(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBoolean(final Path path, final Boolean value)
    throws ServiceException {

    return add(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBoolean(final String path, final List<Boolean> values)
    throws ServiceException {

    return addBoolean(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBoolean(final Path path, final List<Boolean> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Boolean value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addInteger(final String path, final Integer value)
    throws ServiceException {

    return addInteger(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addInteger(final Path path, final Integer value)
    throws ServiceException {

    return add(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addInteger(final String path, final List<Integer> values)
    throws ServiceException {

    return addInteger(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addInteger(final Path path, final List<Integer> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Integer value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addLong(final String path, final Long value)
    throws ServiceException {

    return addLong(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addLong(final Path path, final Long value)
    throws ServiceException {

    return add(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addLong(final String path, final List<Long> values)
    throws ServiceException {

    return addLong(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addLong
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addLong(final Path path, final List<Long> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Long value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDouble(final String path, final Double value)
    throws ServiceException {

    return addDouble(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDouble(final Path path, final Double value)
    throws ServiceException {

    return add(path, Support.nodeFactory().numberNode(value));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDouble(final String path, final List<Double> values)
    throws ServiceException {

    return addDouble(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDouble
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDouble(final Path path, final List<Double> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Double value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDate(final String path, final Date value)
    throws ServiceException {

    return addDate(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDate(final Path path, final Date value)
    throws ServiceException {

//    return add(path, Support.nodeFactory().booleanNode(value));
    // force NPE on usage
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDate(final String path, final List<Date> values)
    throws ServiceException {

    return addDate(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addDate
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addDate(final Path path, final List<Date> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Date value : values) {
      node.add(Support.valueToNode(value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addString(final String path, final String value)
    throws ServiceException {

    return addString(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addString(final Path path, final String value)
    throws ServiceException {

    return add(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addString(final String path, final List<String> values)
    throws ServiceException {

    return addString(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addString(final Path path, final List<String> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (String value : values) {
      node.add(value);
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addURI(final String path, final URI value)
    throws ServiceException {

    return addURI(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addURI(final Path path, final URI value)
    throws ServiceException {

    return add(path, Support.nodeFactory().textNode(value.toString()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addURI(final String path, final List<URI> values)
    throws ServiceException {

    return addURI(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addURI
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addURI(final Path path, final List<URI> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (URI value : values) {
      node.add(value.toString());
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBinary(final String path, final byte[] value)
    throws ServiceException {

    return addBinary(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the single value to add.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBinary(final Path path, final byte[] value)
    throws ServiceException {

    return add(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>byte[]</code>.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBinary(final String path, final List<byte[]> values)
    throws ServiceException {

    return addBinary(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBinary
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>byte[]</code>.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addBinary(final Path path, final List<byte[]> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (byte[] value : values) {
      node.add(Base64Variants.getDefaultVariant().encode(value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPair
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value             the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addPair(final String path, final Pair<String, Object> value)
    throws ServiceException {

    return addPair(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPair
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addPair(final Path path, final Pair<String, Object> value)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    node.addObject().putPOJO(value.tag, value.value);
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPair
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value             the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addPair(final String path, final List<Pair<String, Object>> value)
    throws ServiceException {

    return addPair(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addPair
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation addPair(final Path path, final List<Pair<String, Object>> value)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Pair<String, Object> cursor : value) {
      node.addObject().set(cursor.tag, (JsonNode)Support.valueToNode(cursor.value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link Operation}.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final Object value)
    throws ServiceException {

    return add((Path)null, (JsonNode)Support.valueToNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Object}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final String path, final List<?> values)
    throws ServiceException {

    return add(Path.from(path), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final Path path, final List<?> values)
    throws ServiceException {

    final ArrayNode node = Support.nodeFactory().arrayNode();
    for (Object value : values) {
      node.add((JsonNode)Support.valueToNode(value));
    }
    return add(path, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final JsonNode value)
    throws ServiceException {

    return add((Path)null, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            Path string examples:
   **                            "{@code userName eq 'bjensen'}"
   **                            "{@code userName}"
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final String path, final JsonNode value)
    throws ServiceException {

    return add(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Factory method to create a new add patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation add(final Path path, final JsonNode value)
    throws ServiceException {

    return new Addition(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final ObjectNode value)
    throws ServiceException {

    return replace((Path)null, value);
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Boolean value)
    throws ServiceException {

    return replace(path, BooleanNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Boolean value)
    throws ServiceException {

    return replace(path, BooleanNode.valueOf(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Integer value)
    throws ServiceException {

    return replace(path, IntNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Integer value)
    throws ServiceException {

    return replace(path, IntNode.valueOf(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Long value)
    throws ServiceException {

    return replace(path, LongNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Long value)
    throws ServiceException {

    return replace(path, LongNode.valueOf(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Double value)
    throws ServiceException {

    return replace(path, DoubleNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Double value)
    throws ServiceException {

    return replace(path, DoubleNode.valueOf(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Date value)
    throws ServiceException {

    return replace(path, Support.valueToNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Date value)
    throws ServiceException {

    return replace(path, Support.valueToNode(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
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
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final URI value)
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final String value)
    throws ServiceException {

    return replace(path, TextNode.valueOf(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final Pair<String, ?> value)
    throws ServiceException {

    return replace(path, Support.valueToNode(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final Pair<String, ?> value)
    throws ServiceException {

    return replace(path, Support.valueToNode(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final JsonNode value)
    throws ServiceException {

    return replace(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final String value)
    throws ServiceException {

    return replace(path, TextNode.valueOf(value));
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
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>byte[]</code>.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final String path, final byte[] value)
    throws ServiceException {

    return replace(path, Base64Variants.getDefaultVariant().encode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>byte[]</code>.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final byte[] value)
    throws ServiceException {

    return replace(path, Base64Variants.getDefaultVariant().encode(value));
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
   **                            <br>
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
  public static Operation replace(final String path, final Object value)
    throws ServiceException {

    return replace(Path.from(path), Support.valueToNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Factory method to create a new replace patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            The path must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to replace.
   **                            <br>
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation replace(final Path path, final JsonNode value)
    throws ServiceException {

    return new Replace(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Factory method to create a new remove patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new remove patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation remove(final String path)
    throws ServiceException {

    return remove(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Factory method to create a new remove patch operation.
   **
   ** @param  path               the path targeted by this patch operation.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the new remove patch operation.
   **                            <br>
   **                            Possibe object is <code>Operation</code>.
   **
   ** @throws ServiceException   if the path is invalid.
   */
  public static Operation remove(final Path path)
    throws ServiceException {

    return new Remove(path);
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
   **                            <br>
   **                            Possibe object is {@link String}.
   */
  @Override
  public String toString() {
    try {
      return Support.objectWriter().withDefaultPrettyPrinter().writeValueAsString(this);
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
   ** @throws ServiceException   if the patch operation is invalid.
   */
  public abstract void apply(final ObjectNode node)
    throws ServiceException;

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
    // implicitly add the schema URN of any extended attributes to the
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