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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.node.ArrayNode;

import oracle.hst.platform.rest.ServiceException;

import oracle.iam.platform.scim.BadRequestException;

import oracle.iam.platform.scim.entity.Path;

import oracle.iam.platform.scim.annotation.Schema;
import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// class Entity
// ~~~~~ ~~~~~~
/**
 ** The base SCIM entity.
 ** <br>
 ** This object contains all of the attributes required of SCIM objects.
 ** <p>
 ** <code>Entity</code> is used when the schema is known ahead of time. In that
 ** case a developer can derive a class from <code>Entity</code> and annotate
 ** the class. The class should be a Java bean. This will make it easier to work
 ** with the SCIM object since you will just have plain old getters and setters
 ** for core attributes. Extension attributes cannot be bound to members of the
 ** class but they can still be accessed using the
 ** {@link #extensionObjectNode} method or the {@link #extensionValues},
 ** {@link #replaceExtensionValue}, and {@link #addExtensionValue} methods.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonPropertyOrder({Resource.SCHEMA, Resource.IDENTIFIER, Resource.EXTERNAL})
public class Entity<T extends Entity> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(Resource.IDENTIFIER)
  @Attribute(description="A unique identifier for a SCIM resource as defined by the service provider", required=false, caseExact=true, mutability=Mutability.READ_ONLY, returned=Returned.ALWAYS, uniqueness=Uniqueness.SERVER)
  private String           id;

  @JsonProperty(Resource.EXTERNAL)
  @Attribute(description="A String that is an identifier for the resource as defined by the provisioning client", caseExact=true)
  private String           externalId;

  // Note that "meta" is a complex attribute, so it is represented by the
  // Metadata class.
  @JsonProperty(Resource.METADATA)
  @Attribute(description="A complex attribute containing resource metadata", mutability = Mutability.READ_ONLY)
  private Metadata         metadata;

  // this should not be READ_ONLY as the spec says, ie. if upon creation only
  // the default schema is provided and then via an update a custom attribute is
  // specified, the schemas attributes needs to be updated!
  @JsonProperty(Resource.SCHEMA)
  @Attribute(description="The schemas attribute is a REQUIRED attribute and is an array of Strings containing unique URI's that are allows introspection of the supported schema version for a SCIM representation as well any schema extensions supported by that representation. All representations of SCIM schema MUST include a non-zero value array with value(s) of the URI's supported by that representation. Duplicate values MUST NOT be included. Value order is not specified and MUST not impact behavior.", required=true, returned=Returned.ALWAYS, multiValueClass=String.class)
  private Set<String>      namespace           = new HashSet<String>();

  private final ObjectNode extensionObjectNode = Support.nodeFactory().objectNode();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Entity</code> SCIM Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Entity() {
	// ensure inheritance
    this(null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>Entity</code> SCIM Resource, and sets the namespace
   ** if the class extending this one is annotated.
   **
   ** @param  id                 the ID fo the <code>Entity</code> SCIM
   **                            Resource.
   */
  public Entity(final String id) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id = id;

    // initialize instance
    addNamespace();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Sets the identifier of the object.
   **
   ** @param  id                 the identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entity</code>.
   */
  @Override
  @JsonSetter(Resource.IDENTIFIER)
  @SuppressWarnings({"cast", "unchecked"})
  public T id(final String id) {
    this.id = id;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id (Resource)
  /**
   ** Returns the identifier of the object.
   **
   ** @return                    the identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  @JsonGetter(Resource.IDENTIFIER)
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Sets the external identifier of the object.
   **
   ** @param  id                 the external identifier of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entity</code>.
   */
  @Override
  @JsonSetter(Resource.EXTERNAL)
  @SuppressWarnings({"cast", "unchecked"})
  public T externalId(final String id) {
    this.externalId = id;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalId (Resource)
  /**
   ** Returns the external identifier of the object.
   **
   ** @return                    the external identifier of the object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  @JsonGetter(Resource.EXTERNAL)
  public String externalId() {
    return this.externalId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace  (Resource)
  /**
   ** Sets the schema namespaces for this object.
   ** <br>
   ** This set should contain all schema namespaces including the one for this
   ** object and all extensions.
   **
   ** @param  namespace          a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Allowed object is {@link Collection} of
   **                            {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  @JsonSetter(Resource.SCHEMA)
  @SuppressWarnings({"cast", "unchecked"})
  public T namespace(final Collection<String> namespace) {
    this.namespace = new HashSet<String>(namespace);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace (Resource)
  /**
   ** Returns the schema namespaces for this object.
   ** <br>
   ** This includes the one for the class that extends this class (taken from
   ** the annotation), as well as any that are present in the extensions.
   **
   ** @return                    a set containing the schema namespaces for this
   **                            object.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            {@link String}.
   */
  @Override
  @JsonGetter(Resource.SCHEMA)
  public Collection<String> namespace() {
    if(this.namespace == null)
      this.namespace = new HashSet<String>();

    return this.namespace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Sets metadata for the object.
   **
   ** @param  metadata           the {@link Metadata} about the object.
   **                            <br>
   **                            Allowed object is {@link Metadata}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @Override
  @JsonSetter(Resource.METADATA)
  @SuppressWarnings({"cast", "unchecked"})
  public final T metadata(final Metadata metadata) {
    this.metadata = metadata;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata (Resource)
  /**
   ** Returns metadata about the object.
   **
   ** @return                    the {@link Metadata} about the object.
   **                            <br>
   **                            Possible object is {@link Metadata}.
   */
  @Override
  @JsonGetter(Resource.METADATA)
  public final Metadata metadata() {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic (Resource)
  /**
   ** Returns the {@link Generic} representation of this <code>Entity</code>.
   ** <br>
   ** If this <code>Entity</code> is already a {@link Generic}, this same
   ** instance will be returned.
   **
   ** @return                    the {@link Generic} representation of this
   **                            <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link Generic}.
   */
  @Override
  public final Generic generic() {
    return Generic.build(Support.valueToNode(this));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.externalId, this.metadata, this.namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entities</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entities</code> may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Entity that = (Entity)other;
    return Objects.equals(this.id,         that.id)
        && Objects.equals(this.externalId, that.externalId)
        && Objects.equals(this.metadata,   that.metadata)
        && Objects.equals(this.namespace,  that.namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extensionObjectNode
  /**
   ** Returns the {@link ObjectNode} that contains all extension attributes.
   **
   ** @return                    an {@link ObjectNode} providing access to all
   **                            extension attributes.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  @JsonIgnore
  public ObjectNode extensionObjectNode() {
    return this.extensionObjectNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension
  /**
   ** Sets a SCIM extension to the given value based on the annotations of the
   ** class provided.
   ** <br>
   ** The value will be set for an extension named based on the annotations of
   ** the class supplied.
   **
   ** @param  <T>                the type of object.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  extension          the value to set.
   **                            This also is used to determine what the
   **                            extension's urn is.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  @JsonIgnore
  public <T> void extension(final T extension) {
    final String namespace = namespace(extension.getClass());
    this.extensionObjectNode.set(namespace, Support.valueToNode(extension));
    this.namespace.add(namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension
  /**
   ** Returns a SCIM extension based on the annotations of the class provided.
   ** <br>
   ** The returned value will be converted to a POJO of the type specified.
   **
   ** @param  <T>                the type of object to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the class used to determine the type of the
   **                            object returned and the schema of the
   **                            extension.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the matching extension object, or
   **                            <code>null</code> if no extension of that type
   **                            exists.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws BadRequestException if an error occurs while binding the JSON node
   **                             to the value type.
   */
  @JsonIgnore
  public <T> T extension(final Class<T> clazz)
    throws BadRequestException {

    try {
      JsonNode extensionNode = this.extensionObjectNode.path(namespace(clazz));
      if (extensionNode.isMissingNode()) {
        return null;
      }
      else {
        return Support.nodeToValue(extensionNode, clazz);
      }
    }
    catch (JsonProcessingException e) {
      throw BadRequestException.invalidPath(e.getMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   any
  /**
   ** This method is used during JSON deserialization.
   ** <br>
   ** It will be called in the event that a value is given for an field that is
   ** not defined in the class.
   **
   ** @param  name               the name of the field.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the field.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws BadRequestException if the key is not an extension attribute
   **                             namespace.
   **                             (the name doesn't start with
   **                             "<code>urn:</code>").
   */
  @JsonAnySetter
  protected void any(final String name, final JsonNode value)
    throws BadRequestException {

    if(Support.namespace(name) && value.isObject()) {
      this.extensionObjectNode.set(name, value);
    }
    else {
      String message    = "Core attribute " + name +  " is undefined";
      Schema annotation = this.getClass().getAnnotation(Schema.class);
      if(annotation != null) {
        message += " for schema " + annotation.id();
      }
      throw BadRequestException.invalidSyntax(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   any
  /**
   ** Used to get values that were deserialized from json where there was no
   ** matching field in the class.
   **
   ** @return                    the value of the field.
   **                            <br>
   **                            Possible object is {@link Map}.
   */
  @JsonAnyGetter
  protected Map<String, Object> any() {
    final Map<String, Object> map = new HashMap<String, Object>(this.extensionObjectNode.size());
    final Iterator<Map.Entry<String, JsonNode>> i = this.extensionObjectNode.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      map.put(field.getKey(), field.getValue());
    }
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character " " (space).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   **                            <br>
   **                            Possible object is {@link String}.
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
  // Method:   removeExtension
  /**
   ** Removes a SCIM extension.
   ** <br>
   ** The extension urn is based on the annotations of the class provided.
   **
   ** @param  <T>                the type of the class object.
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  clazz              the class used to determine the schema urn.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    <code>true</code> if the extension was removed,
   **                            or <code>false</code> if the extension was not
   **                            present.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public <T> boolean removeExtension(final Class<T> clazz) {
    final String namespace = namespace(clazz);
    if (this.extensionObjectNode.remove(namespace) == null) {
      return false;
    }
    else {
      this.namespace.remove(namespace);
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extensionValues
  /**
   ** Returns all JSON nodes of the extension attribute referenced by the
   ** provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#matchPath(Path, ObjectNode)} method:
   ** Support.getValues(Path.from(path), extensionObjectNode()).
   ** <p>
   ** The {@link Support#nodeToValue(JsonNode, Class)} method may be used
   ** to bind the retrieved JSON node into specific value type instances.
   **
   ** @param  path               the path to the attribute whose value to
   **                            return.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    a {@link List} of all JSON nodes referenced by
   **                            the provided path.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link JsonNode}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't
   **                            valid for matching.
   */
  public List<JsonNode> extensionValues(final Path path)
    throws ServiceException {

    return Support.matchPath(path, this.extensionObjectNode);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExtensionValue
  /**
   ** Add new values to the extension attribute at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#addValue(Path, ObjectNode, JsonNode)} method.
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to
   ** convert the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to add.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   **
   ** @throws ServiceException  if the path expression could not be parsed
   **                           or the filter derived from the path isn't valid
   **                           for matching.
   */
  public void addExtensionValue(final String path, final ArrayNode value)
    throws ServiceException {

    addExtensionValue(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addExtensionValue
  /**
   ** Add new values to the extension attribute at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#addValue(Path, ObjectNode, JsonNode)} method.
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to
   ** convert the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to add.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to add.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   **
   ** @throws ServiceException   if the filter derived from the path isn't
   **                            valid for matching.
   */
  public void addExtensionValue(final Path path, final ArrayNode value)
    throws ServiceException {

    Support.addValue(path, this.extensionObjectNode, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeExtensionValue
  /**
   ** Removes values of the extension attribute at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#removeValue(Path, ObjectNode)} method.
   **
   ** @param  path               the path to the attribute whose values to
   **                            remove.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    whether one or more values where removed.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ServiceException   if the path expression could not be parsed
   **                            or the filter derived from the path isn't
   **                            valid for matching.
   */
  public boolean removeExtensionValue(final String path)
    throws ServiceException {

    return removeExtensionValue(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeExtensionValue
  /**
   ** Removes values of the extension attribute at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#removeValue(Path, ObjectNode)} method:
   ** Support.removeValue(Path.from(path), extensionObjectNode(), values).
   **
   ** @param  path               the path to the attribute whose values to
   **                            remove.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    whether one or more values where removed.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ServiceException   if the filter derived from the path isn't
   **                           valid for matching.
   */
  public boolean removeExtensionValue(final Path path)
    throws ServiceException {

    final List<JsonNode> nodes = Support.removeValue(path, this.extensionObjectNode);
    return !nodes.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceExtensionValue
  /**
   ** Update the value of the extension attribute at the provided path.
   ** <br>
   ** Equivalent to using the
   ** {@link Support#replaceValue(Path, ObjectNode, JsonNode)} method:
   ** Support.replaceValues(Path.from(path), extensionObjectNode(), value).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to
   ** convert the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to
   **                            set.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to set.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws ServiceException   if the path expression could not be parsed
   **                            or the filter derived from the path isn't
   **                            valid for matching.
   */
  public void replaceExtensionValue(final String path, final JsonNode value)
    throws ServiceException {

    replaceExtensionValue(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceExtensionValue
  /**
   ** Update the value of the extension attribute at the provided path.
   ** <br>
   **  Equivalent to using the
   ** {@link Support#replaceValue(Path, ObjectNode, JsonNode)} method:
   ** Support.replaceValues(path, extensionObjectNode(), value).
   ** <p>
   ** The {@link Support#valueToNode(Object)} method may be used to
   ** convert the given value instance to a JSON node.
   **
   ** @param  path               the path to the attribute whose values to
   **                            set.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the value(s) to set.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @throws ServiceException   if the path expression could not be parsed
   **                            or the filter derived from the path isn't
   **                            valid for matching.
   */
  public void replaceExtensionValue(final Path path, final JsonNode value)
    throws ServiceException {

    Support.replaceValue(path, this.extensionObjectNode, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   checkRequired
  /**
   ** Verifies if the specified tagged-value pairs <code>properties</code> as
   ** a mapping for each key specified by <code>required</code>.
   **
   ** @param  properties         the tagged-value pairs to verify for integrity.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   ** @param  required           the mapping keys for which a value is required
   **                            in <code>properties</code>.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @throws IllegalStateException if the <code>properties</code> doesn't
   **                               contains a value for each key specified by
   **                               <code>required</code>.
   */
  protected void checkRequired(final Map<String, Object> properties, final String... required) {
    for (final String cursor : required) {
      if (!properties.containsKey(cursor))
        throw new IllegalStateException(String.format("Missing required creator property '%s'", cursor));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addNamespace
  /**
   ** Adds the namespace urn of this class to the list of schemas for this
   ** object.
   ** <br>
   ** This is taken from the schema annotation of a class that extends this
   ** class. If the class has no schema annotation, no schema namespace urn will
   ** be added.
   */
  private void addNamespace() {
    final String schema = Support.namespace(this.getClass());
    if ((schema != null) && (!schema.isEmpty())) {
      namespace().add(schema);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the schema namespace urn from a java <code>Class</code>.
   **
   ** @param  clazz              the {@link Class} of the object.
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the schema namespace urn for the object.
   */
  private <T> String namespace(final Class<T> clazz) {
    final String namespace = Support.namespace(clazz);
    if (namespace == null)
      throw new IllegalArgumentException("Unable to determine the extension class schema.");

    return namespace;
  }
}