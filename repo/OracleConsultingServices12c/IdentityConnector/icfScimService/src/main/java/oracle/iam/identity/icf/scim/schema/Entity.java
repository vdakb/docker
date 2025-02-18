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

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
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

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.Path;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

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
 ** {@link #extension} method or the {@link #extensionValues},
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
@JsonPropertyOrder({"schemas", Entity.PRIMARY, "externalId"})
public class Entity<T extends Entity> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the primary identifier property. */
  @JsonIgnore(true)
  public static final String PRIMARY = "id";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(PRIMARY)
  @Attribute(description="A unique identifier for a SCIM resource as defined by the service provider", caseExact=true, mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.ALWAYS, uniqueness=Definition.Uniqueness.SERVER)
  private String           id;

  @JsonProperty("externalId")
  @Attribute(description="A String that is an identifier for the resource as defined by the provisioning client", caseExact=true)
  private String           externalId;

  // Note that "meta" is a complex attribute, so it is represented by the
  // Metadata class.
  @JsonProperty(Metadata.PREFIX)
  @Attribute(description="A complex attribute containing resource metadata", mutability = Definition.Mutability.READ_ONLY)
  private Metadata         metadata;

  // mutability = Definition.Mutability.READ_ONLY,
  // This should not be READ_ONLY as the spec says, ie. if upon creation only
  // the default schema is provided and then via an update a custom attribute is
  // specified, the schemas attributes needs to be updated!
  @JsonProperty("schemas")
  @Attribute(description="The schemas attribute is a REQUIRED attribute and is an array of Strings containing URIs that are used to indicate the namespaces of the SCIM schemas that define the attributes present in the current JSON structure", required=true, returned=Definition.Returned.ALWAYS, multiValueClass=String.class)
  private Set<String>      namespace = new HashSet<String>();

  // extensions belonging schema extension which by nature are of unknown type
  // therfore any extension (enterprise user is one of them) are impelemented as
  // JSON object nodes to keep it flexible
  private final ObjectNode extension = Support.nodeFactory().objectNode();

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
  protected Entity() {
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
  protected Entity(final String id) {
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
   **                            Possible object is <code>Entity</code> for type
   **                            <code>T</code>.
   */
  @Override
  @JsonSetter("id")
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
  @JsonGetter("id")
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifierExternal (Resource)
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
   **                            Possible object is <code>Entity</code> for type
   **                            <code>T</code>.
   */
  @Override
  @JsonSetter("externalId")
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
  @JsonGetter("externalId")
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
   **                            Possible object is <code>Entity</code> for type
   **                            <code>T</code>.
   */
  @Override
  @JsonSetter("schemas")
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
  @JsonGetter("schemas")
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
   **                            Possible object is <code>Entity</code> for type
   **                            <code>T</code>.
   */
  @Override
  @JsonSetter("meta")
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
  @JsonGetter("meta")
  public final Metadata metadata() {
    return this.metadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generic (Resource)
  /**
   ** Returns the {@link GenericResource} representation of this
   ** <code>Entity</code>.
   ** <br>
   ** If this <code>Entity</code> is already a {@link GenericResource}, this
   ** same instance will be returned.
   **
   ** @return                    the {@link GenericResource} representation of
   **                            this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link GenericResource}.
   */
  public final GenericResource generic() {
    final ObjectNode node = Support.valueToNode(this);
    return new GenericResource(node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension (Resource)
  /**
   ** Returns the {@link ObjectNode} that contains all extension attributes.
   **
   ** @return                    an {@link ObjectNode} providing access to all
   **                            extension attributes.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  @Override
  @JsonIgnore
  public ObjectNode extension() {
    return this.extension;
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
    this.extension.set(namespace, Support.valueToNode(extension));
    this.namespace.add(namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extension
  /**
   ** Returns a SCIM extension based on the annotations of the class provided.
   ** <br>
   ** The returned value will be converted to a POJO of the type specified.
   **
   ** @param  <T>                the type of resource to return.
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
   */
  @JsonIgnore
  public <T> T extension(final Class<T> clazz) {
    try {
      JsonNode extensionNode = this.extension.path(namespace(clazz));
      if (extensionNode.isMissingNode()) {
        return null;
      }
      else {
        return Support.nodeToValue(extensionNode, clazz);
      }
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
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
   ** @throws ServiceException   if the key is not an extension attribute
   **                            namespace.
   **                            (the name doesn't start with
   **                            "<code>urn:</code>").
   */
  @JsonAnySetter
  protected void any(final String name, final JsonNode value)
    throws ServiceException {

    if(Support.namespace(name) && value.isObject()) {
      this.extension.set(name, value);
    }
    else {
      String message    = "Core attribute " + name +  " is undefined";
      Schema annotation = getClass().getAnnotation(Schema.class);
      if(annotation != null) {
        message += " for schema " + annotation.id();
      }
      throw ServiceException.invalidSyntax(message);
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
    final Map<String, Object> map = new HashMap<String, Object>(this.extension.size());
    final Iterator<Map.Entry<String, JsonNode>> i = this.extension.fields();
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
    if (this.extension.remove(namespace) == null) {
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

    return Support.matchPath(path, this.extension);
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
   **                          for matching.
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
   **                           valid for matching.
   */
  public void addExtensionValue(final Path path, final ArrayNode value)
    throws ServiceException {

    Support.addValue(path, this.extension, value);
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
   **                           valid for matching.
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
   **                            valid for matching.
   */
  public boolean removeExtensionValue(final Path path)
    throws ServiceException {

    final List<JsonNode> nodes = Support.removeValue(path, this.extension);
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

    Support.replaceValue(path, this.extension, value);
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
    final String schema = Support.namespace(getClass());
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