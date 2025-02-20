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

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   PatchOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PatchOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.domain;

import java.util.List;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.fasterxml.jackson.core.Base64Variants;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.BooleanNode;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.scim.v2.schema.SchemaFactory;

////////////////////////////////////////////////////////////////////////////////
// abstract class PatchOperation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** An individual patch operation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="op")
@JsonSubTypes(
  { @JsonSubTypes.Type(value=PatchOperation.AddOperation.class, name="add")
  , @JsonSubTypes.Type(value=PatchOperation.RemoveOperation.class, name="remove")
  , @JsonSubTypes.Type(value=PatchOperation.ReplaceOperation.class, name="replace")
  }
)
public abstract class PatchOperation {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute(description="Attribute path", required=true, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT)
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
      ADD("add")
      /** The replace operation type. */
    , REPLACE("replace")
      /** The remove operation type. */
    , REMOVE("remove")
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
  // class AddOperation
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** The operation to add an attribute value by a patch operation.
   */
  public static final class AddOperation extends PatchOperation {

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
     ** @throws ProcessingException if the path is invalid.
     */
    @JsonCreator
    private AddOperation(final @JsonProperty(value="path") Path path, final @JsonProperty(value="value", required=true) JsonNode value)
      throws ProcessingException {

      // ensure inheritance
      super(path);

      if(value == null || value.isNull() || ((value.isArray() || value.isObject()) && value.size() == 0))
        throw BadRequestException.invalidSyntax("value field must not be null or an empty container");

      if(path == null && !value.isObject())
        throw BadRequestException.invalidSyntax("value field must be a JSON object containing the attributes to add");

      if(path != null) {
        for (Path.Element element : path) {
          if(element.filter() != null) {
            throw BadRequestException.invalidPath("path field for add operations must not include any value selection filters");
          }
        }
      }
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: node (overridden)
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
    // Method: value (overridden)
    /**
     ** Returns the value of the patch operation.
     **
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     **
     ** @return                  the value of the patch operation.
     **                          <br>
     **                          Possible object is <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException  if the value can not be parsed to the
     **                                  type specified by the Java class
     **                                  object.
     ** @throws ProcessingException      if the operation contains more than one
     **                                  value, in which case, the values method
     **                                  should be used to retrieve all values.
     */
    @Override
    public <T> T value(final Class<T> clazz)
        throws JsonProcessingException
        ,      ProcessingException {

      if (this.value.isArray())
        throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_MULTIVALUE_NOTPERMITTED));

      return Support.objectReader().treeToValue(this.value, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (overridden)
    /**
     ** Returns all values of the patch operation.
     **
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
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
      for (JsonNode node : this.value) {
        objects.add(Support.objectReader().treeToValue(node, clazz));
      }
      return objects;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (PatchOperation)
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
    // Method: apply (PatchOperation)
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
      int result = path() != null ? path().hashCode() : 0;
      result = 31 * result + this.value.hashCode();
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>User</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>User</code>s filter may be different even though they
     ** contain the same set of names with the same values, but in a different
     ** order.
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
      final AddOperation that = (AddOperation)other;
      if (path() != null ? !path().equals(that.path()) : that.path() != null) {
        return false;
      }
      return this.value.equals(that.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ReplaceOperation
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** The operation to replace an attribute value by a patch operation.
   */
  public static final class ReplaceOperation extends PatchOperation {

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
     ** @throws ProcessingException if the path is invalid.
     */
    @JsonCreator
    private ReplaceOperation(final @JsonProperty(value="path", required=true) Path path, final @JsonProperty(value="value", required=true) JsonNode value)
      throws ProcessingException {

      // ensure inheritance
      super(path);

      if (value == null || value.isNull() || ((value.isArray() || value.isObject()) && value.size() == 0))
        throw BadRequestException.invalidSyntax("value field must not be null or an empty container");

      if (path == null && !value.isObject())
        throw BadRequestException.invalidSyntax("value field must be a JSON object containing the attributes to replace");

      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: node (overridden)
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
    // Method: value (overridden)
    /**
     ** Returns the value of the patch operation.
     **
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     **
     ** @return                  the value of the patch operation.
     **                          <br>
     **                          Possible object is <code>&lt;T&gt;</code>.
     **
     ** @throws JsonProcessingException  if the value can not be parsed to the
     **                                  type specified by the Java class
     **                                  object.
     ** @throws ProcessingException      if the operation contains more than one
     **                                  value, in which case, the values method
     **                                  should be used to retrieve all values.
     */
    @Override
    public <T> T value(final Class<T> clazz)
        throws JsonProcessingException
        ,      ProcessingException {

      if(this.value.isArray())
        throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.PATCH_MULTIVALUE_NOTPERMITTED));

      return Support.objectReader().treeToValue(this.value, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (overridden)
    /**
     ** Returns all values of the patch operation.
     **
     ** @param  clazz            the Java class object used to determine the
     **                          type to return.
     **                          <br>
     **                          Allowed object is {@link Class}.
     ** @param  <T>              the generic type parameter of the Java class
     **                          used to determine the type to return.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
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
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (PatchOperation)
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
    // Method: apply (PatchOperation)
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
      int result = path() != null ? path().hashCode() : 0;
      result = 31 * result + this.value.hashCode();
      return result;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>User</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>User</code>s filter may be different even though they
     ** contain the same set of names with the same values, but in a different
     ** order.
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
      final ReplaceOperation that = (ReplaceOperation)other;
      if (path() != null ? !path().equals(that.path()) : that.path() != null) {
        return false;
      }
      return this.value.equals(that.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class RemoveOperation
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** The operation to replace an attribute value by a patch operation.
   */
  public static final class RemoveOperation extends PatchOperation {

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
     ** @throws ProcessingException if the path is invalid.
     */
    @JsonCreator
    private RemoveOperation(final @JsonProperty(value="path", required=true) Path path)
      throws ProcessingException {
      // ensure inheritance
      super(path);

      if(path == null)
        throw BadRequestException.invalidPath("path field must not be null for remove operations");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method: type (PatchOperation)
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
    // Method: apply (PatchOperation)
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
     ** Two <code>User</code>s filter are considered equal if and only if
     ** they represent the same properties. As a consequence, two given
     ** <code>User</code>s filter may be different even though they
     ** contain the same set of names with the same values, but in a different
     ** order.
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
      final RemoveOperation that = (RemoveOperation)other;
      if (path() != null ? !path().equals(that.path()) : that.path() != null) {
        return false;
      }
      return true;
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
   **                            Allowed object {@link Path}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public PatchOperation(final Path path)
    throws ProcessingException {

    // ensure inhritance
    super();

    if (path != null) {
      if (path.size() > 2)
        throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_VALUE_DEPTH));

      if (path.size() == 2) {
        final Filter filter = path.element(1).filter();
        // allow use of the special case "value" path to reference the value
        // itself.
        // any other value filter is for a sub-attribute, which is not
        // permitted.
        if (filter != null && !filter.path().element(0).attribute().equals("value"))
          throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.PATH_INVALID_VALUE_FILTER));
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
  public JsonNode node() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the patch operation.
   **
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class}.
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
   ** @throws ProcessingException      if the operation contains more than one
   **                                  value, in which case, the values method
   **                                  should be used to retrieve all values.
   */
  public <T> T value(final @SuppressWarnings("unused") Class<T> clazz)
    throws JsonProcessingException
    ,      ProcessingException {

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values
  /**
   ** Returns all values of the patch operation.
   **
   ** @param  clazz              the Java class object used to determine the
   **                            type to return.
   **                            <br>
   **                            Allowed object is {@link Class}.
   ** @param  <T>                the generic type parameter of the Java class
   **                            used to determine the type to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    the values of the patch operation.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            elementis of type <code>&lt;T&gt;</code>.
   **
   ** @throws JsonProcessingException  if the value can not be parsed to the
   **                                  type specified by the Java class object.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-collection-return")
  public <T> List<T> values(final @SuppressWarnings("unused") Class<T> clazz)
    throws JsonProcessingException {

    return null;
  }

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation build(final Type type, final String path, final JsonNode value)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid or an unknown operation
   **                             type is given.
   */
  public static PatchOperation build(final Type type, final Path path, final JsonNode value)
    throws ProcessingException {

    switch (type) {
      case ADD     : return add(path, value);
      case REMOVE  : return remove(path);
      case REPLACE : return replace(path, value);
      default      : throw BadRequestException.invalidSyntax(ServiceBundle.string(ServiceError.PATCH_OPERATIONTYPE_UNKNOWN, type));
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Boolean}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addBoolean(final String path, final List<Boolean> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addBoolean(final Path path, final List<Boolean> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Integer}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addInteger(final String path, final List<Integer> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addInteger(final Path path, final List<Integer> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Long}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addLong(final String path, final List<Long> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addLong(final Path path, final List<Long> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Double}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addDouble(final String path, final List<Double> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addDouble(final Path path, final List<Double> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Date}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addDate(final String path, final List<Date> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addDate(final Path path, final List<Date> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addString(final String path, final List<String> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addString(final Path path, final List<String> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link URI}.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addURI(final String path, final List<URI> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addURI(final Path path, final List<URI> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
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
   ** @param  values             the values to add.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>byte[]</code>.
   **
   ** @return                    the new add patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addBinary(final String path, final List<byte[]> values)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation addBinary(final Path path, final List<byte[]> values)
    throws ProcessingException {

    final ArrayNode node = Support.jsonNodeFactory().arrayNode();
    for (byte[] value : values) {
      node.add(Base64Variants.getDefaultVariant().encode(value));
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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation add(final JsonNode value)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation add(final String path, final JsonNode value)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation add(final Path path, final JsonNode value)
    throws ProcessingException {

    return new AddOperation(path, value);
  }
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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final ObjectNode value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final Boolean value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final Boolean value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final Integer value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final Integer value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final Long value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final Long value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final Double value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final Double value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final Date value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final Date value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final URI value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final URI value)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final String value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final JsonNode value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final String value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>byte[]</code>.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final String path, final byte[] value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>byte[]</code>.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final byte[] value)
    throws ProcessingException {

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
   **                            The value(s) must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    the new replace patch operation.
   **                            <br>
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation replace(final Path path, final JsonNode value)
    throws ProcessingException {

    return new ReplaceOperation(path, value);
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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation remove(final String path)
    throws ProcessingException {

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
   **                            Possibe object is {@link PatchOperation}.
   **
   ** @throws ProcessingException if the path is invalid.
   */
  public static PatchOperation remove(final Path path)
    throws ProcessingException {

    return new RemoveOperation(path);
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