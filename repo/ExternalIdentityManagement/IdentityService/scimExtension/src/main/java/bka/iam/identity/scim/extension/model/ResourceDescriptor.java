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

    File        :   ResourceDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class
                    ResourceDescriptor.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
////////////////////////////////////////////////////////////////////////////////
// class ResourceDescriptor
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The ResourceDescriptor class represents a collection of SchemaDescriptors
 ** associated with a SCIM resource. This class is designed to model the
 ** relationship between resources and the schemas that define their attributes
 ** and structure.
 **
 ** The class implements the {@link Iterable} interface,
 ** allowing it to be iterated over to access the individual
 ** {@link SchemaDescriptor} objects that define the structure of the resource.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceDescriptor implements Iterable<SchemaDescriptor> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** A list of SchemaDescriptor objects that define the schema of the resource. */
  private final Set<SchemaDescriptor> descriptors;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a new ResourceDescriptor instance with an empty list of schemas.
   ** This constructor is used to create a ResourceDescriptor without any predefined
   ** schemas.
   */
  public ResourceDescriptor() {
    super();
    this.descriptors = new HashSet<>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Initializes a new ResourceDescriptor instance with a predefined set of schemas.
   ** 
   ** @param schema    The set of schemas to initialize with.
   **                  Allowed object is {@link Set}.
   */
  public ResourceDescriptor(Set<SchemaDescriptor> schema) {
    super();
    this.descriptors = schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an iterator over the list of SchemaDescriptor objects.
   ** This allows iteration over the schema list, making it easier to process each
   ** schema in the context of the SCIM resource.
   ** 
   ** @return An iterator for the schema list.
   **         Possible object is {@link Iterator}.
   */
  @Override
  public Iterator<SchemaDescriptor> iterator() {
    return descriptors.iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Getter and Setter Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSchema
  /**
   ** Adds a SchemaDescriptor to the list of schemas.
   **
   ** @param schemaDescriptor The SchemaDescriptor to add.
   **                         Allowed object is {@link SchemaDescriptor}.
   */
  public void addSchema(SchemaDescriptor schemaDescriptor) {
    if (schemaDescriptor.getURI().contains("core")) {
      schemaDescriptor.setCoreSchema(true);
    }
    this.descriptors.add(schemaDescriptor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSchema
  /**
   ** Returns the set of SchemaDescriptor objects that define the resource's schema.
   ** 
   ** @return The set of SchemaDescriptor objects.
   **         Possible object is {@link Set}.
   */
  public Set<SchemaDescriptor> getSchema() {
    return descriptors;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCoreSchema
  /**
   ** Retrieves the core schema descriptor for this resource, if available.
   ** 
   ** @return The core SchemaDescriptor, or null if not found.
   **         Possible object is {@link SchemaDescriptor}.
   */
  public SchemaDescriptor getCoreSchema() {
    if (this.descriptors.size() == 1) {
      SchemaDescriptor coreSchema = new ArrayList<SchemaDescriptor>(this.descriptors).get(0);
      coreSchema.setCoreSchema(true);
      return coreSchema;
    }
    for (SchemaDescriptor descriptor : this.descriptors) {
      if (descriptor.isCoreSchema()) {
        return descriptor;
      }
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCoreSchema
  /**
   ** Set Core schema on resource
   ** 
   ** @return true in case schema was found and set to core, otherwise return false
   **         
   */
  public boolean setCoreSchema(String schemaURN) {
    boolean updateStatus = false;
    if(this.descriptors != null && schemaURN != null){
      for(SchemaDescriptor schemaDescription : this.descriptors){
        if(schemaURN.equals(schemaDescription.getURI())){
          schemaDescription.setCoreSchema(true);
          updateStatus = true;
        }
      }
    }
    return updateStatus;
  }
  

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves a SchemaAttribute by its name, potentially searching through
   ** sub-attributes if applicable.
   ** 
   ** @param attributePathName The name of the attribute to retrieve.
   **                      Allowed object is {@link String}.
   ** 
   ** @return The matching SchemaAttribute, or null if not found.
   **         Possible object is {@link SchemaAttribute}.
   */
  public SchemaAttribute get(String attributePathName) {
    final Set<SchemaDescriptor> descriptors = getSchema();
    final SchemaDescriptor coreSchema = getCoreSchema();
    if (attributePathName.equals(ScimResource.SCHEMA)) {
      return getSchemaSchemaAttribute();
    }
    
    if (attributePathName.equals(ScimResource.META)) {
      return getMetaDataSchemaAttribute();
    }
    if (attributePathName.equals(ScimResource.ID)) {
      return getIDSchemaAttribute();
    }
    
    String coreSchemaURI = null;
    if (coreSchema != null) {
      coreSchemaURI = coreSchema.getURI();
    }

    if (!attributePathName.contains(":")) {
      attributePathName = coreSchemaURI + ":" + attributePathName;
    }
    final String[] pathAttribute = getAttributeFromURI(attributePathName);
    for (SchemaDescriptor descriptor : descriptors) {
      if (descriptor.getURI().equals(attributePathName)) {
        SchemaAttribute schema = new SchemaAttribute();
        schema.setName(attributePathName);
        schema.setReturned("default");
        schema.setRequired(false);
        schema.setMultiValued(false);
        schema.setSubAttributes(descriptor.getAttributes().toArray(new SchemaAttribute[0]));
        return schema;
      }
      if (pathAttribute[0].equals(descriptor.getURI())) {
        Iterator<SchemaAttribute> schemaAttribute = descriptor.iterator();
        while (schemaAttribute.hasNext()) {
          SchemaAttribute attribute = schemaAttribute.next();
          SchemaAttribute[] subAttribute = attribute.getSubAttributes();
          if (attribute.getName().equals(pathAttribute[1]) && pathAttribute.length > 2 && subAttribute != null) {
            return searchSubAttribute(pathAttribute, 2, attribute.getSubAttributes());
          } else if (attribute.getName().equals(pathAttribute[1])) {
            return attribute;
          }
        }
      }
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequiredAttributeName
  /**
   ** Collects and returns a list of all required attribute names across all 
   ** schema descriptors associated with this resource.
   ** 
   ** @return          A list of strings representing the names of all required
   **                  attributes  from all schema descriptors. 
   **                  Possible object is {@link List}.
   */
  public List<String> getRequiredAttributeName() {
    final List<String> keys = new ArrayList<String>();
    
    for (SchemaDescriptor descriptor : descriptors) {
      keys.addAll(descriptor.getRequiredAttributeKey());
    }
    
    System.out.println("Keys: " + keys);
    return keys;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequiredAttributeName
  /**
   ** Collects and returns a list of all required attribute names across all 
   ** schema descriptors associated with this resource.
   ** 
   ** @return          A list of strings representing the names of all required
   **                  attributes  from all schema descriptors. 
   **                  Possible object is {@link List}.
   */
  public List<String> getRequiredReadWriteName() {
    final List<String> keys = new ArrayList<String>();
    
    for (SchemaDescriptor descriptor : descriptors) {
      keys.addAll(descriptor.getReadWriteAttributeKey());
    }
    
    return keys;
  }
  
  private SchemaAttribute getMetaDataSchemaAttribute() {
    List<SchemaAttribute> subAttribute = new ArrayList<>();
    
    
    subAttribute.add(new SchemaAttribute("created", "dateTime", false, "The date and time the resource was added", "readOnly", "always", null, false, false, null, null));
    subAttribute.add(new SchemaAttribute("lastModified", "dateTime", false, "The most recent date and time the resource was modified", "readOnly", "always", null, false, false, null, null));
    subAttribute.add(new SchemaAttribute("location", "string", false, "The URI of the resource", "readOnly", "always", null, false, false, null, null));
    subAttribute.add(new SchemaAttribute("version", "string", false, "The version of the resource", "readOnly", "always", "global", false, true, null, null));
    subAttribute.add(new SchemaAttribute("resourceType", "string", false, "The type of the resource", "readOnly", "always", "global", false, true, null, null));

    
    return new SchemaAttribute("resourceType", "string", false, "The name of the resource type", "readOnly", "default", null, true, true, null, subAttribute.toArray(new SchemaAttribute[0]));
  }
  
  private SchemaAttribute getIDSchemaAttribute() {
    return new SchemaAttribute("id", "string", false, "The id of the resource type", "readOnly", "always", null, false, false, null, null);
  }
  
  private SchemaAttribute getSchemaSchemaAttribute() {
    return new SchemaAttribute("schemas", "string", true, "A list of URIs that indicate the namespaces of the SCIM schemas used in the representation of the resource.", "readOnly", "always", "none", true, true, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Helper Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchSubAttribute
  /**
   ** Recursively searches for a sub-attribute within a list of sub-attributes.
   ** 
   ** @param attributeName The full path of the attribute to search for.
   **                      Allowed object is {@link String[]}.
   ** @param pos           The current position in the attribute path.
   ** @param subAttribute  The array of sub-attributes to search within.
   **                      Allowed object is {@link SchemaAttribute[]}.
   ** 
   ** @return The matching SchemaAttribute, or null if not found.
   **         Possible object is {@link SchemaAttribute}.
   */
  private SchemaAttribute searchSubAttribute(final String[] attributeName, int pos, final SchemaAttribute[] subAttribute) {
    if (subAttribute == null) {
      return null;
    }
    for (SchemaAttribute attribute : subAttribute) {
      if (attributeName[pos].equals(attribute.getName())) {
        if (pos == attributeName.length - 1) {
          return attribute;
        } else {
          return searchSubAttribute(attributeName, ++pos, attribute.getSubAttributes());
        }
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeFromURI
  /**
   ** Parses the URI of an attribute to extract its path components.
   ** 
   ** @param URI The URI of the attribute to parse.
   **            Allowed object is {@link String}.
   ** 
   ** @return An array of strings representing the attribute's path.
   **         Possible object is {@link String[]}.
   */
  private String[] getAttributeFromURI(final String URI) {
    final ArrayList<String> pathAttribute = new ArrayList<>();

    int lastColonIndex = URI.lastIndexOf(":");
    if (lastColonIndex != -1) {
      String schema = URI.substring(0, lastColonIndex);
      pathAttribute.add(schema);
    }

    String[] attributes = URI.substring(lastColonIndex + 1).split("\\.");
    for (String attribute : attributes) {
      pathAttribute.add(attribute);
    }

    return pathAttribute.toArray(new String[0]);
  }
  
  /**
   * Clones this ResourceDescriptor, creating a new instance with the same set of schema descriptors.
   * 
   * @return A new ResourceDescriptor instance that is a deep copy of the current one.
   * @throws CloneNotSupportedException If the object cannot be cloned.
   */
  @Override
  public ResourceDescriptor clone() {
    // Create a new ResourceDescriptor
    ResourceDescriptor clonedDescriptor = new ResourceDescriptor();

    // Create a new set to hold the cloned schema descriptors
    Set<SchemaDescriptor> clonedSchemas = new HashSet<>();

    // Clone each SchemaDescriptor in the current descriptor set
    for (SchemaDescriptor schemaDescriptor : this.descriptors) {
      clonedSchemas.add(schemaDescriptor.clone());
    }

    // Set the cloned schemas to the new ResourceDescriptor
    clonedDescriptor.descriptors.clear();
    clonedDescriptor.descriptors.addAll(clonedSchemas);

    return clonedDescriptor;
  }
}
