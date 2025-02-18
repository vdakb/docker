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

    File        :   ResourceTypeDefinition.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypeDefinition.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim.v2;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.net.URI;
import java.net.URISyntaxException;

import oracle.iam.identity.icf.foundation.SystemError;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

import oracle.iam.identity.icf.scim.Path;

import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.v2.schema.SchemaFactory;
import oracle.iam.identity.icf.scim.v2.schema.SchemaResource;
import oracle.iam.identity.icf.scim.v2.schema.ResourceTypeResource;

////////////////////////////////////////////////////////////////////////////////
// class ResourceTypeDefinition
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Declaration of a resource type including all schemas.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceTypeDefinition {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String                         id;
  private final String                         name;
  private final String                         description;
  private final String                         endpoint;
  private final SchemaResource                 core;
  private final Map<SchemaResource, Boolean>   extensions;
  private final Map<Path, Definition>          attributeNotationMap;
  private final boolean                        discoverable;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** Builder for creating a ResourceTypeDefinition.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String        name;
    private final String        endpoint;

    private String              id;
    private String              description;
    private SchemaResource      core;
    private Set<SchemaResource> required     = new HashSet<SchemaResource>();
    private Set<SchemaResource> optional     = new HashSet<SchemaResource>();
    private boolean             discoverable = true;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new builder.
     **
     ** @param  name             the name of the resource type.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  endpoint         the endpoint of the resource type.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Builder(final String name, final String endpoint) {
  	  // ensure inheritance
      super();

      // prevent bogus input
      this.name     = Objects.requireNonNull(name, SystemBundle.string(SystemError.ARGUMENT_IS_NULL, "name"));
      this.endpoint = Objects.requireNonNull(endpoint, SystemBundle.string(SystemError.ARGUMENT_IS_NULL, "endpoint"));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: id
    /**
     ** Sets the ID of the resource type.
     **
     ** @param  value            the ID of the resource type.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder id(final String value) {
      this.id = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: description
    /**
     ** Sets the description of the resource type.
     **
     ** @param  value            the description of the resource type.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder description(final String value) {
      this.description = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: core
    /**
     ** Sets the core schema of the resource type.
     **
     ** @param  value            the core schema of the resource type.
     **                          <br>
     **                          Allowed object is {@link SchemaResource}.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder core(final SchemaResource value) {
      this.core = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addRequired
    /**
     ** Adds a required schema extension for a resource type.
     **
     ** @param  value            the required schema extension for the resource
     **                          type.
     **                          <br>
     **                          Allowed object is {@link SchemaResource}.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder addRequired(final SchemaResource value) {
      this.required.add(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: addOptional
    /**
     ** Adds a operation schema extension for a resource type.
     **
     ** @param  value            the operation schema extension for the resource
     **                          type.
     **                          <br>
     **                          Allowed object is {@link SchemaResource}.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder addOptional(final SchemaResource value) {
      this.optional.add(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: discoverable
    /**
     ** Sets whether this resource type is discoverable over the /ResourceTypes
     ** endpoint.
     **
     ** @param  value            <code>true</code> this resource type is
     **                          discoverable over the /ResourceTypes endpoint
     **                          or <code>false</code> otherwise.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  this <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is {@link Builder}.
     */
    public Builder discoverable(final boolean value) {
      this.discoverable = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Build the {@link ResourceTypeDefinition}.
     **
     ** @return                  the newly created definition.
     **                          <br>
     **                          Possible object is
     **                          {@link ResourceTypeDefinition}.
     */
    public ResourceTypeDefinition build() {
      final Map<SchemaResource, Boolean> extensions = new HashMap<SchemaResource, Boolean>(this.required.size() + this.optional.size());
      for (SchemaResource schema : this.required) {
        extensions.put(schema, true);
      }
      for (SchemaResource schema : this.optional) {
        extensions.put(schema, false);
      }
      return new ResourceTypeDefinition(this.id, this.name, this.description, this.endpoint, this.core, extensions, discoverable);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>ResourceType</code>.
   **
   **
   ** @param  id                 the ID of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  description        the description of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  endpoint           the endpoint of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  core               the core schema for the resource type.
   **                            <br>
   **                            Allowed object is {@link SchemaResource}.
   ** @param  extensions         a {@link Map of schema extensions to whether
   **                            it is required for the resource type.
   ** @param  discoverable       <code>true</code> this resource type is
   **                            discoverable over the /ResourceTypes endpoint
   **                            or <code>false</code> otherwise.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private ResourceTypeDefinition(final String id, final String name, final String description, final String endpoint, final SchemaResource core, final Map<SchemaResource, Boolean> extension, final boolean discoverable) {
    this.id                   = id;
    this.name                 = name;
    this.description          = description;
    this.endpoint             = endpoint;
    this.core                 = core;
    this.extensions           = Collections.unmodifiableMap(extension);
    this.discoverable         = discoverable;
    this.attributeNotationMap = new HashMap<Path, Definition>();

    // add the common attributes
    buildAttributeNotationMap(Path.build(), SchemaFactory.COMMON);

    // Add the core attributes
    if (core != null) {
      buildAttributeNotationMap(Path.build(), core.attribute());
    }

    // Add the extension attributes
    for (SchemaResource cursor : extension.keySet()) {
      buildAttributeNotationMap(Path.build(), cursor.attribute());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the resource type name.
   **
   ** @return                    the resource type name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the resource type.
   **
   ** @return                    the description of the resource type.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String description() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Returns the resource type's endpoint.
   **
   ** @return                    the resource type's endpoint.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String endpoint() {
    return this.endpoint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   core
  /**
   ** Returns the resource type's schema.
   **
   ** @return                    the resource type's schema.
   **                            <br>
   **                            Possible object is {@link SchemaResource}.
   */
  public SchemaResource core() {
    return this.core;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extensions
  /**
   ** Returns the resource type's schema extensions.
   **
   ** @return                    the resource type's schema extensions.
   **                            <br>
   **                            Possible object is {@link SchemaResource}.
   */
  public Map<SchemaResource, Boolean> extensions() {
    return this.extensions;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: discoverable
  /**
   ** Whether this resource type and its associated schemas should be
   ** discoverable using the SCIM 2 standard /resourceTypes and /schemas
   ** endpoints.
   **
   ** @return                <code>true</code> this resource type is
   **                        discoverable over the /ResourceTypes endpoint
   **                        or <code>false</code> otherwise.
   **                        <br>
   **                        Possible object is <code>boolean</code>.
   */
  public boolean discoverable() {
    return this.discoverable;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method: getAttributeDefinition
  /**
   ** Retrieve the attribute definition for the attribute in the path.
   **
   ** @param  path               the attribute path.
   **
   ** @return                    the attribute definition or <code>null</code>
   **                            if there is no attribute defined for the path.
   */
  public Definition attributeDefinition(final Path path) {
    return this.attributeNotationMap.get(normalizePath(path).withoutFilters());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method the create a {@link Builder}.
   **
   ** @param  name               the name of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  endpoint           the endpoint of the resource type.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Builder} populated with the name of
   **                            the resource type and the appropriate endpoint. 
   **                            <br>
   **                            Possible object is {@link Builder}.
   */
  public static Builder builder(final String name, final String endpoint) {
    return new Builder(name, endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizePath
  /**
   ** Normalize a path by removing the schema URN for core attributes.
   **
   ** @param  path               the path to normalize.
   **
   ** @return                    the normalized path.
   */
  public Path normalizePath(final Path path) {
    if (path.namespace() != null && this.core != null && path.namespace().equalsIgnoreCase(this.core.id())) {
      return Path.build().attribute(path);
    }
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fromJaxRsResource
  /**
   ** Create a new instance representing the resource type implemented by a root
   ** JAX-RS resource class.
   **
   ** @param  resource           a root resource whose {@link ResourceType}
   **                            and {@link javax.ws.rs.Path} values will be
   **                            used to initialize the
   **                            {@link ResourceTypeDefinition}.
   **
   ** @return                    a new {@link ResourceTypeDefinition} or
   **                            <code>null</code> if resource is not annotated
   **                            with {@link ResourceType} and
   **                            {@link javax.ws.rs.Path}.
   */
  public static ResourceTypeDefinition fromJaxRsResource(final Class<?> resource) {
    Class<?>     c = resource;
    ResourceType resourceType;
    do {
      resourceType = c.getAnnotation(ResourceType.class);
      c = c.getSuperclass();
    } while (c != null && resourceType == null);

    c = resource;
    javax.ws.rs.Path path;
    do {
      path = c.getAnnotation(javax.ws.rs.Path.class);
      c = c.getSuperclass();
    } while (c != null && path == null);

    if (resourceType == null || path == null) {
      return null;
    }

    try {
      ResourceTypeDefinition.Builder builder = new Builder(resourceType.name(), path.value());
      builder.description(resourceType.description());
      builder.core(SchemaFactory.schema(resourceType.schema()));
      builder.discoverable(resourceType.discoverable());

      for (Class<?> cursor : resourceType.optional()) {
        builder.addOptional(SchemaFactory.schema(cursor));
      }

      for (Class<?> cursor : resourceType.required()) {
        builder.addRequired(SchemaFactory.schema(cursor));
      }
      return builder.build();
    }
    catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttributeNotationMap
  /**
   * Retrieve the ResourceType SCIM resource that represents this definition.
   *
   * @return The ResourceType SCIM resource that represents this definition.
   */
  public ResourceTypeResource toScimResource() {
    try {
      URI coreSchemaUri = null;
      if (this.core != null) {
        coreSchemaUri = new URI(this.core.id());
      }
      List<ResourceTypeResource.Extension> extension = null;
      if (this.extensions.size() > 0) {
        extension = new ArrayList<ResourceTypeResource.Extension>(this.extensions.size());
        for (Map.Entry<SchemaResource, Boolean> cursor : this.extensions.entrySet()) {
          extension.add(new ResourceTypeResource.Extension(URI.create(cursor.getKey().id()), cursor.getValue()));
        }
      }
      return new ResourceTypeResource(this.id == null ? this.name : this.id, this.name, this.description, URI.create(this.endpoint), coreSchemaUri, extension);
    }
    catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildAttributeNotationMap
  private void buildAttributeNotationMap(final Path parentPath, final Collection<Definition> attributes) {
    for (Definition cursor : attributes) {
      Path path = parentPath.attribute(cursor.name());
      this.attributeNotationMap.put(path, cursor);
      if (cursor.attributes() != null) {
        buildAttributeNotationMap(path, cursor.attributes());
      }
    }
  }
}