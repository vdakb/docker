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
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.option.AttributeVisitor;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.parser.Unmarshaller;
import bka.iam.identity.scim.extension.rest.HTTPContext;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
  
  public final String getAttributeValue(final String path) {
    final List<String> attributeName = new ArrayList<>();
    attributeName.add(path);
    
    final AttributeVisitor visitor = new AttributeVisitor(attributeName);
    List<Attribute> selectedAttribute = visitor.visit(this);
    if (selectedAttribute.size() > 0) {
      Attribute attribute = selectedAttribute.get(0);
      if (attribute.getValue() != null)
        return (String) attribute.getValue().getValue();
      return attribute.getValues().toString();
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSchema
  /**
   ** Add a value to the "schema" attribute of the SCIM resource.
   */
  public void addSchema(final String value) {
    Attribute schemaValue = get(SCHEMA);
    if (schemaValue != null)
      schemaValue.addValue(new AttributeValue(value));
    else {
      this.add(new MultiValueSimpleAttribute(SCHEMA, Arrays.asList(new AttributeValue(value)).toArray(new AttributeValue[0])));
    }
  }
  
  public void addAttribute(String key, final Object value)
    throws ScimException {
    final List<String> path = new LinkedList<>();
  
    for (String schema : getSchemaList()) {
      if (key.startsWith(schema)) {
        path.add(schema);
        key = key.substring(schema.length() + 1);
        break;
      }
    }
  
    Collections.addAll(path, key.split("\\."));
    SchemaAttribute schemaAttribute = null;
    Attribute currentAttribute = null;
    AttributeValue currentAttributeValue = null;

  
    for (int i = 0; i < path.size(); i++) {
      String currentPath = path.get(i);
     
      
      boolean isMultiValueComplex = path.get(i).matches("(.+)\\[((.+))\\]");
      String index = "-1";
      
      if (isMultiValueComplex) {
        index = currentPath.substring(currentPath.indexOf("[") + 1, currentPath.indexOf("]"));
        currentPath = currentPath.substring(0, currentPath.indexOf("[")); 
      }
      
       if (i == 0) {
        schemaAttribute = this.descriptor.get(currentPath);
        currentAttribute = this.get(currentPath);
      } else {
        schemaAttribute = schemaAttribute.getSubSchemaAttribute(currentPath);
      }
        
      if (schemaAttribute == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_GATEWAY, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, currentPath));
      }
  
      // Check schema attribute exist
  
      if (i == path.size() - 1) {
        if (currentAttribute == null) {
          Attribute attribute = null;
          if (schemaAttribute.getType().equalsIgnoreCase("complex")) {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MCV
              System.out.println("Add Multi Complexe value: " + value);
              currentAttribute = new MultiValueComplexAttribute(currentPath, (AttributeValue[]) value);
              this.add(currentAttribute);
              
            } else {
              // Create SCV
              currentAttributeValue = new AttributeValue(new Attribute[0]);
              attribute = new SingularComplexAttribute(currentPath, currentAttributeValue);
              currentAttribute = attribute;
              this.add(currentAttribute);
            }
          } else {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MVS
            } else {
              // Create SSV
                attribute = new SingularSimpleAttribute(currentPath, new AttributeValue(value));
                currentAttributeValue = attribute.getValue();
                currentAttribute = attribute;
                this.add(currentAttribute);
            }
          }
  
        } else {
          if (schemaAttribute.getType().equalsIgnoreCase("complex")) {
            if (schemaAttribute.getMultiValued() == true) {
              List<AttributeValue> attributeValues = new LinkedList<AttributeValue>(Arrays.asList(currentAttribute.getValues()));
              if (value != null)
                attributeValues.add((AttributeValue) value);
              currentAttribute.setValue(attributeValues.toArray(new AttributeValue[0]));
            } else {
              // Create SCV
            }
          } else {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MVS
            } else {
              // Create SSV
              List<Attribute> attributes = new ArrayList<>(Arrays.asList(currentAttributeValue.getSubAttributes()));
                Attribute newAttribute = new SingularSimpleAttribute(currentPath, new AttributeValue(value));
                attributes.add(newAttribute);
              List<AttributeValue> updatedAttributeValue = new ArrayList<AttributeValue>();
              for (AttributeValue attrValue : currentAttribute.getValues()) {
                if (attrValue == currentAttributeValue) {
                  updatedAttributeValue.add(new AttributeValue(attributes.toArray(new Attribute[0])));
                }
                else
                  updatedAttributeValue.add(attrValue);
              }
              currentAttribute.setValue(updatedAttributeValue.toArray(new AttributeValue[0]));
              currentAttribute = newAttribute;
              
            }
          }
        }
        return;
      } else {
        if (currentAttribute == null) {
          Attribute attribute = null;
          if (schemaAttribute.getType().equalsIgnoreCase("complex")) {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MCV
              List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
              // Handle special index
              SchemaAttribute schemaAttributeWithCannonicalValue = getSchemaAttributeWithCanonicalValue(schemaAttribute);
              if (schemaAttributeWithCannonicalValue == null) {
                for (int j = 0; j < Integer.parseInt(index) + 1; j++) {
                  attributeValues.add(new AttributeValue(new Attribute[0]));
                }
                MultiValueComplexAttribute multiValueComplexAttr = new MultiValueComplexAttribute(currentPath, attributeValues.toArray(new AttributeValue[0]));
                currentAttributeValue = multiValueComplexAttr.getValues()[Integer.parseInt(index)];
                currentAttribute = multiValueComplexAttr;
              }
              else {
                List<String> cannonicalValue = Arrays.asList(schemaAttributeWithCannonicalValue.getCanonicalValues());
                  attribute = new SingularSimpleAttribute(schemaAttributeWithCannonicalValue.getName(), new AttributeValue(index));
                  currentAttributeValue = new AttributeValue(attribute);
                  attributeValues.add(currentAttributeValue);
                  MultiValueComplexAttribute multiValueComplexAttr = new MultiValueComplexAttribute(currentPath, attributeValues.toArray(new AttributeValue[0]));
                  currentAttribute = multiValueComplexAttr;
              }
              
              
              this.add(currentAttribute);
              
            } else {
              // Create SCV
              currentAttributeValue = new AttributeValue(new Attribute[0]);
              attribute = new SingularComplexAttribute(currentPath, currentAttributeValue);
              currentAttribute = attribute;
              this.add(currentAttribute);
            }
          } else {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MVS
            } else {
              // Create SSV
              attribute = new SingularSimpleAttribute(currentPath, new AttributeValue(value));
            }
          }
  
          //
          continue;
        } else {
          Attribute attribute = null;
          if (schemaAttribute.getType().equalsIgnoreCase("complex")) {
            if (schemaAttribute.getMultiValued() == true) {
              List<AttributeValue> attributeValues = new LinkedList<AttributeValue>(Arrays.asList(currentAttribute.getValues()));
              SchemaAttribute schemaAttributeWithCannonicalValue = getSchemaAttributeWithCanonicalValue(schemaAttribute);
              if (schemaAttributeWithCannonicalValue == null) {
                if (Integer.parseInt(index) > currentAttribute.getValues().length - 1) {
                  attributeValues.add(new AttributeValue(new Attribute[0]));
                  currentAttribute.setValue(attributeValues.toArray(new AttributeValue[0]));
                }
                currentAttributeValue = currentAttribute.getValues()[Integer.parseInt(index)];
              } else {
                boolean foundIndex = false;
                for (int j = 0; j < currentAttribute.getValues().length; j++) {
                  if (foundIndex)
                    break;
                  AttributeValue attributeValue = currentAttribute.getValues()[j];
                  for (Attribute subAttribute : attributeValue.getSubAttributes()) {
                    if (foundIndex) {
                      break;
                    }
                    if (subAttribute.getName().equalsIgnoreCase(schemaAttributeWithCannonicalValue.getName())
                        && subAttribute.getValue().getValueAsString().equalsIgnoreCase(index)) {
                      currentAttributeValue = attributeValue;
                      foundIndex = true;
                      break;
                    }
                  }
                }
                if (!foundIndex) {
                  attribute = new SingularSimpleAttribute(schemaAttributeWithCannonicalValue.getName(), new AttributeValue(index));
                  currentAttributeValue = new AttributeValue(attribute);
                  attributeValues.add(currentAttributeValue);
                  currentAttribute.setValue(attributeValues.toArray(new AttributeValue[0]));
                }
              }
              // Create MCV
            } else {
              // Create SCV
              currentAttributeValue = currentAttribute.getValue();
            }
          } else {
            if (schemaAttribute.getMultiValued() == true) {
              // Create MVS
            } else {
              // Create SSV
            }
          }
  
        }
      }
    }
    return;
  }
  
  public Object getAttribute(String key) {
    final List < String > path = new LinkedList < >();
  
    for (String schema: getSchemaList()) {
      if (key.startsWith(schema)) {
        path.add(schema);
        key = key.substring(schema.length() + 1);
        break;
      }
    }
  
    Collections.addAll(path, key.split("\\."));
    SchemaAttribute schemaAttribute = null;
    Attribute currentAttribute = null;
    AttributeValue currentAttributeValue = null;
  
    for (int i = 0; i < path.size(); i++) {
      String currentPath = path.get(i);
  
      boolean isMultiValueComplex = path.get(i).matches("(.+)\\[((.+))\\]");
      String index = "-1";
  
      if (isMultiValueComplex) {
        index = currentPath.substring(currentPath.indexOf("[") + 1, currentPath.indexOf("]"));
        currentPath = currentPath.substring(0, currentPath.indexOf("["));
      }
  
      if (i == 0) {
        schemaAttribute = this.descriptor.get(currentPath);
        currentAttribute = this.get(currentPath);
      } else {
        schemaAttribute = schemaAttribute.getSubSchemaAttribute(currentPath);
      }
  
      /*if (schemaAttribute == null) {
        throw new ScimException(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, );
      }*/
  
      // Check schema attribute exist
  
      if (i == path.size() - 1) {
        if (currentAttribute == null) {
          return null;
        }
        else if (currentAttributeValue == null) {
          return returnValueFromSchemaType(schemaAttribute, currentAttribute);
        }
        else {
          for (Attribute attribute: currentAttributeValue.getSubAttributes()) {
            if (attribute.getName().equalsIgnoreCase(currentPath)) return returnValueFromSchemaType(schemaAttribute, attribute);
          }
        }
      } else {
        if (currentAttribute == null) {
          return null;
        } else {
          Attribute attribute = null;
          if (schemaAttribute.getType().equalsIgnoreCase("complex")) {
            if (schemaAttribute.getMultiValued() == true) {
              SchemaAttribute schemaAttributeWithCannonicalValue = getSchemaAttributeWithCanonicalValue(schemaAttribute);
              if (schemaAttributeWithCannonicalValue == null) {
                if (Integer.parseInt(index) > currentAttribute.getValues().length) {
                  return null;
                }
                currentAttributeValue = currentAttribute.getValues()[Integer.parseInt(index)];
              } else {
                boolean foundIndex = false;
                for (int j = 0; j < currentAttribute.getValues().length; j++) {
                  if (foundIndex) break;
                  AttributeValue attributeValue = currentAttribute.getValues()[j];
                  for (Attribute subAttribute: attributeValue.getSubAttributes()) {
                    if (foundIndex) {
                      break;
                    }
                    if (subAttribute.getName().equalsIgnoreCase(schemaAttributeWithCannonicalValue.getName()) && subAttribute.getValue().getValueAsString().equalsIgnoreCase(index)) {
                      currentAttributeValue = attributeValue;
                      foundIndex = true;
                      break;
                    }
                  }
                }
                if (!foundIndex) {
                  return null;
                }
              }
              // Create MCV
            } else {
              // Create SCV
              currentAttributeValue = currentAttribute.getValue();
            }
          } else {
            if (schemaAttribute.getMultiValued() == true) {
              return null;
            } else {
              return null;
            }
          }
  
        }
      }
    }
    return null;
  }
  
  private Object returnValueFromSchemaType(final SchemaAttribute schemaAttribute, final Attribute attribute) {
    switch (schemaAttribute.getType()) {
      case "string":
        return attribute.getValue().getStringValue();
      case "boolean":
        return attribute.getValue().getBooleanValue();
      case "decimal":
        return attribute.getValue().getDecimalValue();
      case "integer":
        return attribute.getValue().getIntegerValue();
      case "dateTime":
        return attribute.getValue().getDateTimeValue();
      case "binary":
        return attribute.getValue().getBinaryValue();
      case "reference":
        return attribute.getValue().getReferenceValue();
      case "complex":
        return attribute.getValues();
      default:
        return attribute;
    }
  }

  private SchemaAttribute getSchemaAttributeWithCanonicalValue(final SchemaAttribute schemaAttribute) {
    SchemaAttribute[] subSchemaAttributes = schemaAttribute.getSubAttributes();
    
    for (SchemaAttribute subSchemaAttribute : subSchemaAttributes) {
      if (subSchemaAttribute.getCanonicalValues() != null && subSchemaAttribute.getCanonicalValues().length > 0) {
        return subSchemaAttribute;
      }
    }
    
    return null;
    // throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, "Cannot find cannonical value");
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
    return Unmarshaller.jsonNodeToResource(jsonNode, descriptor, clazz);
  }

}
