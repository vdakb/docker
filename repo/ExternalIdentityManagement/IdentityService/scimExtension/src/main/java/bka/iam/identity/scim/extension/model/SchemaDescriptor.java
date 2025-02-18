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

    File        :   SchemaDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    SchemaDescriptor.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
////////////////////////////////////////////////////////////////////////////////
// class SchemaDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The SchemaDescriptor class represents a SCIM schema for a resource. This
 ** class defines the structure of a SCIM resource's schema, including the list
 ** of attributes, the schema's URI, and metadata about the schema such as its
 ** description and whether it's a core schema.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SchemaDescriptor implements Iterable<SchemaAttribute> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // URI of the schema, identifying it uniquely.
  private URI schemaURI;

  // List of attributes that belong to the schema.
  private List<SchemaAttribute> attributes = new LinkedList<SchemaAttribute>();

  // Static constant representing the core schema identifier.
  public static String SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:Schema";

  // The name of the schema.
  private String name;

  // Location of the schema's metadata.
  private String metaLocation;

  // The type of resource for which this schema applies.
  private String metaResourceType;

  // Description of the schema's purpose or usage.
  private String description;

  // Indicates if this schema is a core schema for the associated resource.
  private Boolean coreSchema;


  public SchemaDescriptor() {
    //ensure inheritance
    super();
    coreSchema = false;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Getter and Setter Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchema
  /**
   ** Returns the schema constant, which identifies the SCIM schema.
   ** 
   ** @return The schema identifier as a string.
   **         Possible object is {@link String}.
   */
  public String getSchemas() {
    return SCHEMA;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURI
  /**
   ** Returns the URI scheme of the schema.
   ** 
   ** @param uri       The name of the attribute to retrieve.
   **                  Allowed object is {@link String}.
   **             
   ** @return          The scheme part of the URI.
   **                  Possible object is {@link String}.
   */
  public String setURI(final String uri) {
    try {
      this.schemaURI = new URI(uri);
      return this.schemaURI.getScheme();
    }
    catch (URISyntaxException e) {
      // TODO: throw error is not a URI
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getURI
  /**
   ** Returns the URI scheme of the schema.
   ** 
   ** @return The scheme part of the URI.
   **         Possible object is {@link String}.
   */
  public String getURI() {
    return schemaURI != null ? schemaURI.toString() : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an iterator over the list of SchemaAttribute objects.
   ** This allows iteration through the schema's attributes.
   ** 
   ** @return An iterator for the list of schema attributes.
   **         Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<SchemaAttribute> iterator() {
    return attributes.iterator();
  }
  
  public List<SchemaAttribute> getAttributes() {
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of attributes in this schema.
   ** 
   ** @return The number of attributes in the schema.
   **         Possible object is {@link Integer}.
   */
  public final int size() {
    return attributes.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves a schema attribute by its name.
   **
   ** @param <T>             The type of the attribute being returned, extending
   **                        {@link SchemaAttribute}.
   ** @param name            The name of the attribute to retrieve.
   **                        Allowed object is {@link String}.
   **
   ** @return                The attribute with the specified name, or null if
   **                        it does not exist.
   **                        Possible object is {@link SchemaAttribute}.
   */
  public final <T extends SchemaAttribute> T get(final String name) {
    for (SchemaAttribute attribute : attributes) {
      if (attribute.getName().equals(name)) {
        return (T) attribute;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   * Adds an attribute to the schema and returns it.
   *
   * @param attribute The attribute to be added to the schema.
   * Allowed object is {@link bka.iam.identity.scim.extension.model.SchemaAttribute}.
   *
   * @return The added attribute.
   * Possible object is {@link bka.iam.identity.scim.extension.model.SchemaAttribute}.
   */
  public SchemaAttribute add(final SchemaAttribute attribute) {
    this.attributes.add((SchemaAttribute)attribute);
    return get(attribute.getName());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAllAttributes
  /**
   ** Adds a collection of attributes to the schema. Each attribute is cloned
   ** to prevent external modifications.
   ** 
   ** @param attributes          A collection of attributes to add.
   **                            Allowed object is {@link Collection}.
   ** 
   ** @return                    True if the schema's attribute list was
   **                            modified, false otherwise.
   **                            Possible object is {@link boolean}.
   */
  public boolean addAllAttributes(final Collection<? extends SchemaAttribute> attributes) {
      for (SchemaAttribute attribute : attributes) {
          this.add(attribute.clone());
      }
      return !attributes.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCoreSchema
  /**
   ** Sets whether this schema is a core schema for the resource.
   ** 
   ** @param value        Whether the schema is a core schema (true/false).
   **                     Allowed object is {@link Boolean}.
   */
  public void setCoreSchema(Boolean value) {
    this.coreSchema = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCoreSchema
  /**
   ** Returns TRUE if this schema is a core schema for the associated resource.
   ** 
   ** @return            TRUE if this schema is a core schema, otherwise FALSE.
   **                    Possible object is {@link Boolean}.
   */
  public Boolean isCoreSchema() {
    return coreSchema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the schema.
   ** 
   ** @param name         The name of the schema.
   **                     Allowed object is {@link String}.
   */
  public void setName(final String name) {
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the name of the schema.
   ** 
   ** @return             The name of the schema.
   **                     Possible object is {@link String}.
   */
  public String getName() {
    return name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the description of the schema.
   ** 
   ** @param description  The description of the schema.
   **                     Allowed object is {@link String}.
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the description of the schema.
   ** 
   ** @return             The description of the schema.
   **                     Possible object is {@link String}.
   */
  public String getDescription() {
    return description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMetaLocation
  /**
   ** Sets the meta-location URL of the schema.
   ** 
   ** @param metaLocation     The meta-location URL of the schema.
   **                         Allowed object is {@link String}.
   */
  public void setMetaLocation(final String metaLocation) {
    this.metaLocation = metaLocation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMetaLocation
  /**
   ** Returns the meta-location URL of the schema.
   ** 
   ** @return                The meta-location URL of the schema.
   **                        Possible object is {@link String}.
   */
  public String getMetaLocation() {
    return metaLocation;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMetaResourceType
  /**
   ** Sets the resource type for the schema.
   ** 
   ** @param metaResourceType  The resource type.
   **                          Allowed object is {@link String}.
   */
  public void setMetaResourceType(final String metaResourceType) {
    this.metaResourceType = metaResourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMetaResourceType
  /**
   ** Returns the resource type for the schema.
   ** 
   ** @return             The resource type for the schema.
   **                     Possible object is {@link String}.
   */
  public String getMetaResourceType() {
    return metaResourceType;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequiredAttributeKey
  /**
   ** Returns a list of keys for all required attributes defined within this
   ** schema.
   ** It iterates through all the schema attributes and calls 
   ** {@link #getSubRequiredAttributeKey}to collect keys 
   ** for required attributes, including nested ones.
   ** 
   ** @return             A list of strings representing the keys of required
   **                     attributes.
   **         
   */
  public List<String> getRequiredAttributeKey() {
    List<String> keys = new ArrayList<String>();
    final Iterator<SchemaAttribute> schemaAttribute = this.iterator();
    while (schemaAttribute.hasNext()) {
      final SchemaAttribute attribute = schemaAttribute.next();
      keys.addAll(getSubRequiredAttributeKey(this.getURI(), attribute, true));
    }
    
    return keys;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSubRequiredAttributeKey
  /**
   ** Recursively collects keys for all required attributes, including nested
   ** attributes.
   ** If an attribute has no sub-attributes and is marked as required, its key
   ** is added to the list.
   ** If it has sub-attributes, the method is called recursively for each
   ** sub-attribute.
   **
   ** @param parentKey       The parent attribute name used as a prefix for
   **                        nested attributes.
   **                        Allowed object is {@link String}.
   ** @param schemaAttribute The attribute to be checked for required status
   **                        and sub-attributes.
   **                        Allowed object is {@link SchemaAttribute}.
   ** @param rootLevel       Boolean flag indicating whether the current level
   **                        is the root schema level.
   **                        Allowed object is {@link Boolean}.
   ** 
   ** @return                A list of keys representing required attributes
   **                        from this attribute onward.
   **                        Possible object is {@link List}.
   */
  private List<String> getSubRequiredAttributeKey(final String parentKey, final SchemaAttribute schemaAttribute, final Boolean rootLevel) {
    List<String> keys = new ArrayList<String>();
    if (schemaAttribute.getSubAttributes().length == 0) {
      if (schemaAttribute.getRequired()) {
        if (parentKey != null)
          keys.add(rootLevel ? parentKey + ":" + schemaAttribute.getName() : parentKey + "." + schemaAttribute.getName());
        else
          keys.add(schemaAttribute.getName());
      }
    }
    else {
      for (SchemaAttribute subSchemaAttribute : schemaAttribute.getSubAttributes()) {
        keys.addAll(getSubRequiredAttributeKey(rootLevel ? parentKey + ":" + schemaAttribute.getName() : parentKey + "." + schemaAttribute.getName(), subSchemaAttribute, false));
      }
    }
    
    return keys;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getReadWriteAttributeKey
  /**
   ** Returns a list of keys for all required attributes defined within this
   ** schema.
   ** It iterates through all the schema attributes and calls 
   ** {@link #getSubRequiredAttributeKey}to collect keys 
   ** for required attributes, including nested ones.
   ** 
   ** @return             A list of strings representing the keys of required
   **                     attributes.
   **         
   */
  public List<String> getReadWriteAttributeKey() {
    List<String> keys = new ArrayList<String>();
    final Iterator<SchemaAttribute> schemaAttribute = this.iterator();
    while (schemaAttribute.hasNext()) {
      final SchemaAttribute attribute = schemaAttribute.next();
      keys.addAll(getSubReadWriteAttributeKey(this.getURI(), attribute, true));
    }
    
    return keys;
  }
  
    //////////////////////////////////////////////////////////////////////////////
  // Method:   getSubReadWriteAttributeKey
  /**
   ** Recursively collects keys for all required attributes, including nested
   ** attributes.
   ** If an attribute has no sub-attributes and is marked as required, its key
   ** is added to the list.
   ** If it has sub-attributes, the method is called recursively for each
   ** sub-attribute.
   **
   ** @param parentKey       The parent attribute name used as a prefix for
   **                        nested attributes.
   **                        Allowed object is {@link String}.
   ** @param schemaAttribute The attribute to be checked for required status
   **                        and sub-attributes.
   **                        Allowed object is {@link SchemaAttribute}.
   ** @param rootLevel       Boolean flag indicating whether the current level
   **                        is the root schema level.
   **                        Allowed object is {@link Boolean}.
   ** 
   ** @return                A list of keys representing required attributes
   **                        from this attribute onward.
   **                        Possible object is {@link List}.
   */
  private List<String> getSubReadWriteAttributeKey(final String parentKey, final SchemaAttribute schemaAttribute, final Boolean rootLevel) {
    List<String> keys = new ArrayList<String>();
    System.out.println("Visiting " + schemaAttribute.getName() + " | SubAttribute lenght:" + schemaAttribute.getSubAttributes().length);
    if (schemaAttribute.getSubAttributes().length == 0) {
      if (schemaAttribute.getMutability().equals("readWrite")) {
        if (parentKey != null)
          keys.add(rootLevel ? parentKey + ":" + schemaAttribute.getName() : parentKey + "." + schemaAttribute.getName());
        else
          keys.add(schemaAttribute.getName());
      }
    }
    else {
      for (SchemaAttribute subSchemaAttribute : schemaAttribute.getSubAttributes()) {
        keys.addAll(getSubReadWriteAttributeKey(rootLevel ? parentKey + ":" + schemaAttribute.getName() : parentKey + "." + schemaAttribute.getName(), subSchemaAttribute, false));
      }
    }
    
    return keys;
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Provides a JSON representation of the schema descriptor, with null-safe
   ** handling for optional fields.
   ** 
   ** @return             A JSON string representation of the schema descriptor.
   **                     Possible object is {@link String}.
   */
  @Override
  public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append("{");
      builder.append("\"schemas\":[\"").append(getSchemas()).append("\"],");
      builder.append("\"id\":\"").append(getURI()).append("\",");
      if (!attributes.isEmpty()) {
          builder.append("\"attributes\":[");
          final Iterator<SchemaAttribute> iterator = attributes.iterator();
          while (iterator.hasNext()) {
              builder.append(iterator.next().toString());
              if (iterator.hasNext()) {
                  builder.append(",");
              }
          }
          builder.append("],");
      }
      builder.append("\"name\":\"").append(name != null ? name : "").append("\",");
      builder.append("\"meta\":{");
      builder.append("\"location\":\"").append(metaLocation != null ? metaLocation : "").append("\",");
      builder.append("\"resourceType\":\"").append(metaResourceType != null ? metaResourceType : "").append("\"");
      builder.append("},");
      builder.append("\"description\":\"").append(description != null ? description : "").append("\"");
      builder.append("}");
      return builder.toString();
  }
  
  /**
   * Clones this SchemaDescriptor, creating a new instance with the same attributes.
   * The clone will also deep-copy all SchemaAttribute objects in the attributes list.
   *
   * @return A new SchemaDescriptor instance that is a deep copy of the current one.
   */
  @Override
  public SchemaDescriptor clone() {
    // Create a new SchemaDescriptor
    SchemaDescriptor clonedSchemaDescriptor = new SchemaDescriptor();

    // Clone the URI
    clonedSchemaDescriptor.setURI(this.getURI());

    // Clone the name, metaLocation, metaResourceType, description, and coreSchema
    clonedSchemaDescriptor.setName(this.getName());
    clonedSchemaDescriptor.setMetaLocation(this.getMetaLocation());
    clonedSchemaDescriptor.setMetaResourceType(this.getMetaResourceType());
    clonedSchemaDescriptor.setDescription(this.getDescription());
    clonedSchemaDescriptor.setCoreSchema(this.isCoreSchema());

    // Clone the attributes list (deep clone of each SchemaAttribute)
    List<SchemaAttribute> clonedAttributes = new LinkedList<>();
    for (SchemaAttribute attribute : this.attributes) {
      clonedAttributes.add(attribute.clone()); // Assuming SchemaAttribute has a clone method
    }
    clonedSchemaDescriptor.attributes = clonedAttributes;

    return clonedSchemaDescriptor;
  }
}
