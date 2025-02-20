/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   ScimResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the ScimResource class.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.Unmarshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// abstract class ScimResource
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The ScimResource class represents a SCIM resource, extending the
 ** {@link Resource} class and adding specific functionality for handling common
 ** SCIM attributes.
 ** It provides methods for accessing mendatory SCIM attributes like "id",
 ** "meta", and "schema".
 **
 ** This class overrides the `toString()` method to provide a custom string
 ** representation for a SCIM resource, showing its attributes in a
 ** JSON-like format.
 **
 ** @author sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since 1.0.0.0
 */
public abstract class ScimResource extends Resource {
  
  //////////////////////////////////////////////////////////////////////////////
  // static attribute
  //////////////////////////////////////////////////////////////////////////////

  // "id" attribute defined in Section 3.1 of the SCIM RFC7643.
  public final static String ID                = "id";
  // "meta" attribute defined in Section 3 of the SCIM RFC7643.
  public final static String META              = "meta";
  // "meta" attribute defined in Section 3 of the SCIM RFC7643.
  public final static String META_CREATED      = "created";
  // "meta" attribute defined in Section 3 of the SCIM RFC7643.
  public final static String META_MODIFIED     = "lastModified";
  // "meta" attribute defined in Section 3 of the SCIM RFC7643.
  public final static String META_LOCATION     = "location";
  // "meta" attribute defined in Section 3 of the SCIM RFC7643.
  public final static String META_RESOURCETYPE = "resourceType";
  // "schema" attribute defined in Section 2.3 of the SCIM RFC7643.
  public final static String SCHEMA = "schemas";
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // A list that holds all the attributes of the resource.
  private ResourceDescriptor descriptor;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructor
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a ScimResource with a descriptor.
   **
   ** @param descriptor   The {@link ResourceDescriptor} that defines the schema
   **                     and attributes of this SCIM resource.
   **                     Allowed object is {@link ResourceDescriptor}.
   */
  public ScimResource(final ResourceDescriptor descriptor) {
    // ensure inheritance
    super();
    // Constructor initializes the list of attributes.
    if (descriptor != null)
      this.descriptor = descriptor;
    else
      this.descriptor = new ResourceDescriptor();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a ScimResource with a descriptor and a list of attributes.
   **
   ** @param descriptor   The {@link ResourceDescriptor} that defines the schema
   **                     and attributes of this SCIM resource.
   **                     Allowed object is {@link ResourceDescriptor}.
   ** @param attributes   The list of {@link Attribute} objects to initialize
   **                     the resource with.
   **                     Allowed object is {@link List}.
   */
  public ScimResource(final ResourceDescriptor descriptor, List<Attribute> attributes) {
    // ensure inheritance
    super(attributes);
    // Constructor initializes the list of attributes.
    this.descriptor = descriptor;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Instance methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
  ** Retrieves the "id" attribute of the SCIM resource.
  **
  ** @return            The "id" attribute of the resource as a string.
  **                    Possible object is {@link String}.
  */
  public String getId() {
    return getAttributeValue(ID);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
  ** Sets the "id" attribute of the SCIM resource.
  **
  ** @param id          The value to set as the "id" attribute.
  **                    Allowed object is {@link String}.
  */
  public void setId(final String id) {
    set(ID, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMeta
  /**
  ** Retrieves the "meta" attribute of the SCIM resource.
  **
  ** @return            The "meta" attribute of the resource.
  **                    Possible object is {@link Attribute}.
  */
  public Attribute getMeta() {
    return get(META);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchema
  /**
   ** Retrieves the "schema" attribute of the SCIM resource.
   **
   ** @return The "schema" attribute of the resource.
   ** Possible object is {@link Attribute}.
   */
  public Attribute getDeclaredSchema() {
    return get(SCHEMA);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSchema
  /**
   ** Add a value to the "schema" attribute of the SCIM resource.
   */
  public void addSchema(final String value) {
    Attribute schemaValue = get(SCHEMA);
    schemaValue.addValue(new AttributeValue(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getResourceDescriptor
  /**
  ** Returns the resource descriptor associated with this SCIM resource.
  **
  ** @return            The {@link ResourceDescriptor} defining the schema
  **                    and attributes of this resource.
  */
  @Override
  public ResourceDescriptor getResourceDescriptor() {
    return this.descriptor;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   setResourceDescriptor
  /**
  ** Sets the resource descriptor associated of the SCIM resource.
  **
  ** @param descriptor   The {@link ResourceDescriptor} that defines the schema
  **                     and attributes of this SCIM resource.
  */
  public void setResourceDescriptor(final ResourceDescriptor descriptor) {
    this.descriptor = descriptor;
  }
  
  public ResourceDescriptor getRegisterResourceDescriptor() {
    ResourceDescriptor availableDescriptor =  getResourceDescriptor();
    ResourceDescriptor registerDescriptor = new ResourceDescriptor();
    for (SchemaDescriptor schema : availableDescriptor.getSchema()) {
      for (String registerURI : getSchemaList()) {
        if (schema.getURI().equals(registerURI))
          registerDescriptor.addSchema(schema);
      }
    }
    
    return registerDescriptor;
  }
  
  private List<String> getSchemaList() {
    final List<String> schemaURIs = new ArrayList<String>();
    final Attribute schemaAttribute = getDeclaredSchema();
    if (schemaAttribute != null) {
      for (AttributeValue uri : schemaAttribute.getValues()) {
        schemaURIs.add(uri.getValueAsString());
      }
    }
    return schemaURIs;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Static Methods
  //////////////////////////////////////////////////////////////////////////////
  
  public static Attribute createSchema(final String... schemaUrns) {
    List<AttributeValue> schemas = new LinkedList<>();
    
    for (String urn : schemaUrns) {
      schemas.add(new AttributeValue(urn));
    }
    
    return new MultiValueSimpleAttribute(SCHEMA, schemas.toArray(new AttributeValue[0]));
  }
  
  public static Attribute createID(final String id) {
    return new SingularSimpleAttribute(ID, new AttributeValue(id));
  }
  
  public static Attribute createMeta(final Date created, final Date modified, final URI location, final String resourceType) {
    List<Attribute> metaAttribute = new LinkedList<>();
    
    if (created != null)
      metaAttribute.add(new SingularSimpleAttribute(META_CREATED, new AttributeValue(created)));
    if (modified != null)
      metaAttribute.add(new SingularSimpleAttribute(META_MODIFIED, new AttributeValue(modified)));
    if (location != null)
      metaAttribute.add(new SingularSimpleAttribute(META_LOCATION, new AttributeValue(location)));
    if (resourceType != null)
      metaAttribute.add(new SingularSimpleAttribute(META_RESOURCETYPE, new AttributeValue(resourceType)));
    
    return new SingularComplexAttribute(META, new AttributeValue(metaAttribute.toArray(new Attribute[0])));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Overriden Methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a string representation of the SCIM resource.
   ** The representation includes all the attributes of the resource, formatted
   ** in a JSON-like structure where each attribute is represented as a key-value pair.
   ** 
   ** @return              A string representation of the SCIM resource.
   **                      Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    
    Iterator<Attribute> attributes = this.iterator();
    while (attributes.hasNext()) {
      Attribute attribute = attributes.next();
      builder.append(attribute);
      
      if (attributes.hasNext()) {
        builder.append(",");
      }
    }
    builder.append("}");
    
    return builder.toString();
  }
  
  public abstract String[] getSchemaURIs();
  
  public <T extends ScimResource> T copyResourceWithAttribute(final List<String> attributeList, final Class<T> clazz)
    throws ScimException {
  
    final JsonNode jsonNode = Marshaller.resourceToJsonNode(this, null, new HashSet<String>(attributeList));
    try {
      System.out.println("COpyResource: " + new ObjectMapper().writeValueAsString(jsonNode));
    }
    catch (JsonProcessingException e) {
    }
    return Unmarshaller.jsonNodeToResource(jsonNode, descriptor, clazz);
  }

}
