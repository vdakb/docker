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

    Copyright � 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   Unmarshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class Unmarshaller.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-26-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.parser;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimException.ScimType;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.MultiValueComplexAttribute;
import bka.iam.identity.scim.extension.model.MultiValueSimpleAttribute;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.Operation.OperationType;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SchemaAttribute;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.model.SingularComplexAttribute;
import bka.iam.identity.scim.extension.model.SingularSimpleAttribute;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.HTTPContext.StatusCode;
import bka.iam.identity.scim.extension.rest.OIMSchema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
////////////////////////////////////////////////////////////////////////////////
// class Unmarshaller
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The Unmarshaller class provides methods to parse JSON nodes and convert
 ** them into SCIM objects, including resources, attributes, and patch requests.
 ** 
 ** It supports the reverse operation of the {@link Marshaller}.
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Unmarshaller {
  
  //////////////////////////////////////////////////////////////////////////////
  // Schema Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToSchema
  /**
   ** Parses a JSON node into a {@link SchemaDescriptor}.
   **
   ** @param jsonNode The {@link JsonNode} to parse.
   **                 Allowed object is {@link JsonNode}.
   ** 
   ** @return         The parsed {@link SchemaDescriptor}.
   **                 Possible object is {@link SchemaDescriptor}.
   */
  public static SchemaDescriptor jsonNodetoSchema(JsonNode jsonNode) {
    // Initialize a new SchemaDescriptor object.
    SchemaDescriptor schemaDescriptor = new SchemaDescriptor();

    // Extract the "schemas" array from the JSON node and set the schema type.
    JsonNode schemas = jsonNode.get("schemas");
    if (schemas != null && schemas.isArray()) {
      if (schemas.get(0).asText().equals(SchemaDescriptor.SCHEMA)) {
        // TODO: Throw an exception
      }
    }

    schemaDescriptor.setURI(jsonNode.get("id").asText());
    schemaDescriptor.setName(jsonNode.get("name").asText());
    schemaDescriptor.setDescription(jsonNode.get("description").asText());

    // Extract the metadata ("meta") and set its fields.
    JsonNode metaNode = jsonNode.get("meta");
    if (metaNode != null) {
      schemaDescriptor.setMetaLocation(metaNode.get("location").asText());
      schemaDescriptor.setMetaResourceType(metaNode.get("resourceType").asText());
    }

    // Parse the "attributes" field, which is an array of SchemaAttributes.
    JsonNode attributesNode = jsonNode.get("attributes");
    if (attributesNode != null && attributesNode.isArray()) {
      List<SchemaAttribute> attributesList = new LinkedList<>();
      for (JsonNode attributeNode : attributesNode) {
        schemaDescriptor.add(jsonNodeToSchemaAttribute(attributeNode));
      }
    }

    // Return the populated SchemaDescriptor object.
    return schemaDescriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToSchemaAttribute
  /**
   ** Parses a JSON node into a {@link SchemaAttribute}.
   **
   ** @param node The {@link JsonNode} to parse.
   **             Allowed object is {@link JsonNode}.
   ** 
   ** @return     The parsed {@link SchemaAttribute}.
   **             Possible object is {@link SchemaAttribute}.
   */
  private static SchemaAttribute jsonNodeToSchemaAttribute(final JsonNode node) {
    final String  name        = node.get("name")        != null ? node.get("name").asText() : "";
    final String  type        = node.get("type")        != null ? node.get("type").asText() : "";
    // OIM Schema does not respect his own schema. Which runs in trouble when validating some time the resource.
    final Boolean multiValued = node.get("multiValued") != null ? node.get("multiValued").asBoolean() : type.equals("complex") ? true : false;
    final String  description = node.get("description") != null ? node.get("description").asText() : "";
    final String  mutability  = node.get("mutability")  != null ? node.get("mutability").asText() : "";
    final String  returned    = node.get("returned")    != null ? node.get("returned").asText() : "";
    final String  uniqueness  = node.get("uniqueness")  != null ? node.get("uniqueness").asText() : "";
    final Boolean required    = node.get("required")    != null ? node.get("required").asBoolean() : false;
    final Boolean caseExact   = node.get("caseExact")   != null ? node.get("caseExact").asBoolean() : false;
    final List<String> canonicalValues   = new ArrayList<String>();
    JsonNode canonicalValueNode = node.get("canonicalValues");
    if (canonicalValueNode != null  && canonicalValueNode.isArray()) {
      for (JsonNode jsonNode : canonicalValueNode) {
        canonicalValues.add(jsonNode.asText());
      }
    }
    
    // Create a new SchemaAttribute object with the parsed values.
    final SchemaAttribute schemaAttribute = new SchemaAttribute();
    
    schemaAttribute.setName(name);
    schemaAttribute.setType(type);
    schemaAttribute.setMultiValued(multiValued);
    schemaAttribute.setDescription(description);
    schemaAttribute.setMutability(mutability);
    schemaAttribute.setReturned(returned);
    schemaAttribute.setUniqueness(uniqueness);
    schemaAttribute.setRequired(required);
    schemaAttribute.setCaseExact(caseExact);
    schemaAttribute.setCanonicalValues(canonicalValues.toArray(new String[0]));
    
    // if any sub-attributes, parse it.
    JsonNode subAttributesNode = node.get("subAttributes");
    if (subAttributesNode != null && subAttributesNode.isArray()) {
      List<SchemaAttribute> subAttributesList = new LinkedList<>();
      for (JsonNode subAttributeNode : subAttributesNode) {
        SchemaAttribute att = jsonNodeToSchemaAttribute(subAttributeNode);
        subAttributesList.add(att);
        
      }
      schemaAttribute.setSubAttributes(subAttributesList.toArray(new SchemaAttribute[0]));
    }

    return schemaAttribute;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // SCIM Resource Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToListResponse
  /**
   ** Parses a JSON node representing a SCIM list response and converts it
   ** into a {@link ListResponse} object.
   **
   ** This method extracts the "Resources" array and other pagination details
   ** such as "startIndex" and "itemsPerPage" from the JSON node to construct
   ** the {@link ListResponse}.
   **
   ** @param jsonNode    The {@link JsonNode} representing the list response.
   **                    Allowed object is {@link JsonNode}.
   ** @param descriptor  The {@link ResourceDescriptor} that defines the schema
   **                    for the resources in the list.
   **                    Allowed object is {@link ResourceDescriptor}.
   ** @param clazz       The class type of the resources in the list.
   **                    Allowed object is {@link Class}.
   **
   ** @param <T>         The type of resources in the list, extending
   **                    {@link ScimResource}.
   **
   ** @return            The parsed {@link ListResponse} object containing the
   **                    resources and pagination details.
   **
   ** @throws ScimException If an error occurs during parsing or the JSON node
   **                       contains invalid data.
   */
  public static <T extends ScimResource> ListResponse<T> jsonNodeToListResponse(JsonNode jsonNode, ResourceDescriptor descriptor, Class<T> clazz)
  throws ScimException {
    
    // Extract optional metadata fields from the JSON node.
    long totalResults = jsonNode.has("totalResults") ? jsonNode.get("totalResults").asLong() : 1;
    int startIndex   = jsonNode.has("startIndex")   ? jsonNode.get("startIndex").asInt() : 0;
    int itemsPerPage = jsonNode.has("itemsPerPage") ? jsonNode.get("itemsPerPage").asInt() : 0;

    // Extract the "Resources" array from the JSON node.
    JsonNode resourcesArray = jsonNode.get("Resources");
    if (resourcesArray == null || !resourcesArray.isArray()) {
        throw new ScimException( HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.INVALID_JSON_STRUCTURE_ARRAY, "Resources"));
    }

    // Create a list to hold the parsed SCIM resources.
    List<T> resources = new ArrayList<>();

    // Iterate through the JSON array and parse each resource.
    for (JsonNode resourceNode : resourcesArray) {
        T resource = jsonNodeToResource(resourceNode, descriptor, clazz);
        resources.add(resource);
    }

    // Return the resources wrapped in a ListResponse object.
    return new ListResponse<>(resources, totalResults, startIndex, itemsPerPage);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToResource
  /**
   ** Parses a JSON node into a SCIM resource object.
   ** This method iterates through the fields of the provided JSON node and
   ** parses each field into its corresponding SCIM attribute, then adds it to
   ** the resource.
   **
   ** @param jsonNode    The {@link JsonNode} representing the SCIM resource.
   **                    Allowed object is {@link JsonNode}.
   ** @param descriptor  The {@link ResourceDescriptor} defining the schema for
   **                    the resource.
   **                    Allowed object is {@link ResourceDescriptor}.
   ** @param clazz       The class type of the SCIM resource to be parsed.
   **                    Allowed object is {@link Class}.
   **
   ** @param <T>         The type of the SCIM resource, extending {@link ScimResource}.
   **
   ** @return            The parsed SCIM resource object.
   **                    Possible object is {@link T}.
   **
   ** @throws ScimException If an error occurs during parsing or the JSON node
   **                       contains invalid data.
   */
  public static <T extends ScimResource> T jsonNodeToResource(JsonNode jsonNode, ResourceDescriptor descriptor, Class<T > clazz)
    throws ScimException {
    // Create a new SCIM resource object.
    final T resource;
    try {
      resource = clazz.getDeclaredConstructor(ResourceDescriptor.class).newInstance(descriptor);
      // Get the attribute names on the first level from the JSON node.
      final Iterator<String> fieldNames = jsonNode.fieldNames();  
      
      while (fieldNames.hasNext()) {
        // Get the current field name.
        final String fieldName = fieldNames.next();
        // Get the value of the field.
        final JsonNode value = jsonNode.get(fieldName);
        
        SchemaAttribute schemaAttribute = null;
        if (descriptor != null)
          schemaAttribute = descriptor.get(fieldName);
        
        if (!fieldName.equals(ScimResource.META) && !fieldName.equals(ScimResource.ID))
          if (descriptor != null && schemaAttribute == null)
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, fieldName));
        
        SchemaAttribute canonicalValueAttributeName = getCannonicalSubAttribute(schemaAttribute);
        if (canonicalValueAttributeName != null) {
          validateCanonicalValue(fieldName, value, canonicalValueAttributeName);
        }
      
        // Parse the field and add it to the resource.
        resource.add(jsonNodeToAttribute(schemaAttribute, fieldName, value));  
      }
      return resource;
    }
    catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ScimException(HTTPContext.StatusCode.INTERNAL_SERVER_ERROR, e);
    }
  }
  

  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToAttribute
  /**
   ** Parses a JSON node into a SCIM {@link Attribute}.
   **
   ** @param key  The name of the attribute.
   **             Allowed object is {@link String}.
   ** @param node The {@link JsonNode} representing the attribute value.
   **             Allowed object is {@link JsonNode}.
   ** 
   ** @return     The parsed {@link Attribute}.
   **             Possible object is {@link Attribute}.
   */
  public static Attribute jsonNodeToAttribute(final SchemaAttribute schemaAttribute, final String key, final JsonNode node)
    throws ScimException {
    if(node == null){
      return jsonNodeToSigular(schemaAttribute, key, NullNode.getInstance());
    }
    if (schemaAttribute != null && node.isArray() && schemaAttribute.getMultiValued() != null && !schemaAttribute.getMultiValued()) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_EXPECTED_SINGLE_VALUE, key));
    } else if (schemaAttribute != null && !node.isArray() && schemaAttribute.getMultiValued() != null && schemaAttribute.getMultiValued()) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_EXPECTED_MULTI_VALUE, key));
    }
    
    switch (node.getNodeType()) {
      case ARRAY:
        return jsonNodeToMultiValue(schemaAttribute, key, node); // Handle multi-value attributes.
      case OBJECT:
      case STRING:
      case BOOLEAN:
      case NUMBER:
        return jsonNodeToSigular(schemaAttribute, key, node);  // Handle singular attributes.
      default:
        return null;
    }

  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToMultiValue
  /**
   ** Parses a multi-value attribute from the {@link JsonNode}.
   ** This method determines whether the multi-value attribute is simple
   ** (containing basic types like String, Boolean, etc.) or complex
   ** (containing sub-attributes) and processes accordingly.
   ** 
   ** @param key       The name of the attribute.
   **                  Allowed object is {@link String}.
   ** @param jsonNode  The {@link JsonNode} representing the multi-value attribute.
   **                  Allowed object is {@link JsonNode}.
   ** 
   ** @return          The parsed {@link Attribute} representing the multi-value
   **                  attribute.
   **                  Possible object is {@link MultiValueSimpleAttribute} or
   **                  {@link MultiValueComplexAttribute}.
   */
  private static Attribute jsonNodeToMultiValue(SchemaAttribute schemaAttribute, final String key, final JsonNode jsonNode)
    throws ScimException {
    Boolean isSimple = true;
    // List to hold the parsed values.
    final LinkedList<AttributeValue> attributeValue = new LinkedList<AttributeValue>();
    Attribute attribute = null;
    
    // Iterate over each element in the multi-value array.
    for (JsonNode node : jsonNode) {
      switch(node.getNodeType()) {
        case STRING:
        case BOOLEAN:
        case NUMBER:
          attributeValue.add(jsonNodeToSimple(key, schemaAttribute, node));
          break;
        case OBJECT:
          attributeValue.add(jsonNodeToComplex(key, schemaAttribute, node));
          isSimple = false;
          break;
        default:
          break;
      }
    }
    
    
    // Return the correct attribute type based on whether the values are simple or complex.
    if (isSimple)
      attribute = new MultiValueSimpleAttribute(key, attributeValue.toArray(new AttributeValue[0]));
    else
      attribute = new MultiValueComplexAttribute(key, attributeValue.toArray(new AttributeValue[0]));
    
    return attribute;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToSigular
  /**
   ** Parses a singular attribute from the {@link JsonNode}.
   ** This method handles attributes that contain only a single value, which can
   ** either be simple (e.g., String, Boolean, Number) or complex (e.g., JSON
   ** object with sub-attributes).
   ** 
   ** @param key       The name of the attribute.
   **                  Allowed object is {@link String}.
   ** @param node      The {@link JsonNode} representing the singular attribute.
   **                  Allowed object is {@link JsonNode}.
   ** 
   ** @return          The parsed {@link Attribute} representing the singular
   **                  attribute.
   **                  Possible object is {@link SingularSimpleAttribute} or
   **                  {@link SingularComplexAttribute}.
   */
  private static Attribute jsonNodeToSigular(final SchemaAttribute schemaAttribute, final String key, final JsonNode node)
    throws ScimException {
    
    switch(node.getNodeType()) {
      case STRING:
      case BOOLEAN:
      case NUMBER:
        return new SingularSimpleAttribute(key, jsonNodeToSimple(key, schemaAttribute, node)); 
      case OBJECT:
        return new SingularComplexAttribute(key, jsonNodeToComplex(key, schemaAttribute, node));
      default:
       return null;
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToComplex
  /**
   ** Parses a complex attribute from the {@link JsonNode}.
   ** This method processes a JSON object and converts it into a list of
   ** sub-attributes, which are then encapsulated into an {@link AttributeValue}.
   ** 
   ** @param jsonNode  The {@link JsonNode} representing the complex attribute.
   **                  Allowed object is {@link JsonNode}.
   ** 
   ** @return          The parsed {@link AttributeValue} containing the
   **                  sub-attributes of the complex attribute.
   **                  Possible object is {@link AttributeValue}.
   */
  private static AttributeValue jsonNodeToComplex(final String attributeName, final SchemaAttribute schemaAttribute, final JsonNode jsonNode)
    throws ScimException {
    
    if (schemaAttribute != null && schemaAttribute.getSubAttributes() == null)
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.SUBATTRIBUTE_NOT_FOUND, attributeName));
    
    // Get field names from the JSON object.
    final Iterator<String> fieldNames = jsonNode.fieldNames();

    // List to hold sub-attributes.
    final LinkedList<Attribute> attribute = new LinkedList<Attribute>();
    
    while (fieldNames.hasNext()) {
      // Get the current field name
      final String key = fieldNames.next();
      // Get the value of the field.
      final JsonNode value = jsonNode.get(key);
      
      if (schemaAttribute != null && schemaAttribute.getSubSchemaAttribute(key) == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, key));
      }
      
      // Parse the field and add it to the attribute list.
      attribute.add(jsonNodeToAttribute(schemaAttribute != null ? schemaAttribute.getSubSchemaAttribute(key) : null, key, value));
    }
    
    return new AttributeValue(attribute.toArray(new Attribute[0]));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getCannonicalSubAttribute
  /**
   ** Checks if a given <code>SchemaAttribute</code> has any sub-attributes that
   ** contain canonical values.
   ** It iterates through all sub-attributes of the schema attribute and returns
   ** the first has a canonicalValues.
   **
   ** @param schemaAttribute The parent <code>SchemaAttribute</code> to check for
   **                        sub-attributes.
   ** 
   ** @return                returns the first sub <code>SchemaAttribute</code>
   **                        that has a canonicalValues.
   */
  private static SchemaAttribute getCannonicalSubAttribute(final SchemaAttribute schemaAttribute) {
    if (schemaAttribute == null)
      return null;
    SchemaAttribute[] subSchemaAttributes = schemaAttribute.getSubAttributes();
    if (subSchemaAttributes != null && subSchemaAttributes.length > 0) {
      for (int i = 0; i < subSchemaAttributes.length; i++) {
        if (subSchemaAttributes[i].getCanonicalValues().length > 0)
          return subSchemaAttributes[i];
      }
    }
    return null;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateCanonicalValue
  /**
   ** Validates that the values of a multi-valued attribute match the allowed
   ** canonical values and checks also for duplicates canonical value in the
   ** multi-valued attribute.
   ** 
   ** @param key             The attribute name in the JSON object that
   **                        corresponds to the multi-valued attribute.
   ** @param jsonNode        The JSON node representing the multi-valued
   **                        attribute.
   ** @param schemaAttribute The schema attribute containing the allowed
   **                        canonical values.
   ** 
   ** @throws ScimException  If any invalid or duplicate canonical values are 
   **                        found.
   */
  private static void validateCanonicalValue(final String key, final JsonNode jsonNode, final SchemaAttribute schemaAttribute) throws ScimException {
    final String[] canonicalValues = schemaAttribute.getCanonicalValues();
    if (jsonNode != null && jsonNode.isArray()) {
      Map<String, Integer> typeCountMap = new HashMap<>();
        
      for (JsonNode node : jsonNode) {
        if (node.isObject()) {
          JsonNode canonicalNode = node.get(schemaAttribute.getName());
                
          if (canonicalNode != null && canonicalNode.isTextual()) {
            String canonicalValue = canonicalNode.asText();
                      
            // Check if attribute value is an allowed canonical values
            if (!Arrays.asList(canonicalValues).contains(canonicalValue.toLowerCase())) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.VALUE_NOT_ALLOWED, key, canonicalValue));
            }
                      
            // track the occurrences of the canonical attribute
            typeCountMap.put(canonicalValue, typeCountMap.getOrDefault(canonicalValue, 0) + 1);
                      
            // throw an error if the canonical value occured more than 1
            if (typeCountMap.get(canonicalValue) > 1) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.DUPLICATE_CANONICAL_VALUE, schemaAttribute.getName(), canonicalValue, key));
            }
          }
        }
      }
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseSimple
  /**
   ** Parses a simple value (String, Boolean, or Number) from the JSON node.
   ** The canonical values are check only again a string value.
   **
   ** @param node            The JSON node representing the simple value.
   **                        Allowed object is {@link JsonNode}.
   **
   ** @return                The parsed simple value as an AttributeValue.
   **                        Possible object is {@link AttributeValue}.
   */
  private static AttributeValue jsonNodeToSimple(final String key, final SchemaAttribute schemaAttribute, JsonNode node)
    throws ScimException {
    
    switch (node.getNodeType()) {
      case STRING:
        if (schemaAttribute != null && !schemaAttribute.getType().equalsIgnoreCase("string") && !schemaAttribute.getType().equalsIgnoreCase("dateTime") && !schemaAttribute.getType().equalsIgnoreCase("reference"))
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_TYPE_MISMATCH, key, schemaAttribute.getType()));
        //return new AttributeValue(node.asText().replaceAll("\\\\", "\\\\\\\\").replaceAll("([\\x00-\\x1F\\x7F])", "\\\\$1").replace("\"", "\\\""));
        return new AttributeValue(node.asText());
      case BOOLEAN:
        if (schemaAttribute != null && !schemaAttribute.getType().equalsIgnoreCase("boolean"))
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_TYPE_MISMATCH, key, schemaAttribute.getType()));
        return new AttributeValue(node.asBoolean());  // Handle Boolean values.
      case NUMBER:
        if (schemaAttribute != null && !schemaAttribute.getType().equalsIgnoreCase("integer") && !schemaAttribute.getType().equalsIgnoreCase("decimal"))
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_TYPE_MISMATCH, key, schemaAttribute.getType()));
        return new AttributeValue(node.asInt());  // Handle Integer values.
      default:
        return null;
    }
  }
  

  
  //////////////////////////////////////////////////////////////////////////////
  // Patch Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToPatchRequest
  /**
   ** Parses a JSON node representing a Patch Request and converts it into a
   ** {@link PatchRequest} object.
   ** 
   ** This method extracts the "schemas" array and "Operations" from the JSON
   ** structure and populates a {@link PatchRequest} object accordingly.
   ** 
   ** @param jsonNode    The {@link JsonNode} containing the patch request data.
   **                    Allowed object is {@link JsonNode}.
   ** 
   ** @return            The parsed {@link PatchRequest} object.
   **                    Possible object is {@link PatchRequest}.
   ** 
   ** @throws ScimException  If the "schemas" or "Operations" fields are invalid
   **                        or missing.
   */
  public static PatchRequest jsonNodeToPatchRequest(JsonNode jsonNode)
    throws ScimException {
    // Retrieve the SCIM PatchRequest descriptor from the OIM schema.
    final ResourceDescriptor patchDescriptor = OIMSchema.getInstance().getResourceDescriptorByResourceType(PatchRequest.class);

    // Ensure that PatchRequest has a valid ResourceDescriptor.
    if (patchDescriptor == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "ResourceDescriptor for PatchRequest not found.");
    }

    final PatchRequest patchRequest = new PatchRequest(patchDescriptor);
    final Iterator<String> fieldNames = jsonNode.fieldNames();
    
      while (fieldNames.hasNext()) {
        // Get the current field name
        final String key = fieldNames.next();
        if (key.equals("schemas")) {
          // Validate schemas attribute
          JsonNode schemas = jsonNode.get("schemas");
          if (schemas == null || !schemas.isArray() || schemas.size() == 0) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "Missing or invalid schemas attribute.");
          }
          if (!schemas.get(0).asText().equals(PatchRequest.SCHEMAS[0])) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, 
                  ScimBundle.format(ScimMessage.INCORECT_SCHEMA, schemas.get(0).asText()));
          }
        } else if (key.equals("Operations")) {
          
          // Violation of the OIM Schema should be "Operations" (upper case 'O')
          SchemaAttribute schemaAttribute = patchDescriptor.get("operations");
      
          // Extract "Operations" node from the JSON request.
          JsonNode operationNode = jsonNode.get(key);
      
          // Ensure that "Operations" is present in JSON if expected in schema.
          if (operationNode == null) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, 
                  ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, key));
          }
      
          // Validate that "Operations" is an array if it's multi-valued in the schema.
          if (schemaAttribute.getMultiValued() != null && schemaAttribute.getMultiValued() && !operationNode.isArray()) {
              throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, 
                  ScimBundle.format(ScimMessage.ATTRIBUTE_EXPECTED_MULTI_VALUE, key));
          }
      
          // Process "Operations" array
          if (operationNode.isArray()) {
              for (JsonNode attributeNode : operationNode) {
                  patchRequest.addOperation(jsonNodeToOperation(schemaAttribute, attributeNode));
              }
          }
        } else {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, key));
        }
    }

    return patchRequest;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToOperation
  /**
   ** Parses a JSON node representing an individual SCIM operation and converts
   ** it into an {@link Operation} object.
   ** 
   ** This method extracts the "op" (operation type), "path", and "value" fields
   ** from the JSON node to construct the {@link Operation} object.
   ** 
   ** @param node    The {@link JsonNode} representing the operation to parse.
   **                Allowed object is {@link JsonNode}.
   ** 
   ** @return        The parsed {@link Operation} object.
   **                Possible object is {@link Operation}.
   ** 
   ** @throws ScimException  If the "op" field is missing or the operation is
   **                        invalid.
   */
  private static Operation jsonNodeToOperation(final SchemaAttribute schemaAttribute, final JsonNode node)
    throws ScimException {
    
    final Iterator<String> fieldNames = node.fieldNames();
    
    String type = null;
    String path = null;
    Attribute value = null;
    
    while (fieldNames.hasNext()) {
      // Get the current field name
      final String          key                 = fieldNames.next();
      final SchemaAttribute attributeDescriptor = schemaAttribute.getSubSchemaAttribute(key);
      System.out.println("Processing: " + key);
      if (attributeDescriptor == null)
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST,  ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, key));
      
      if (key.equals("op")) {
        type = node.has(key) ? node.get(key).asText() : null;
    
        
        if (Operation.OperationType.fromString(type) == null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_VALUE, "Invalid operation type: " + type);
        }
      } else if (key.equals("path")) {
        path = node.has("path") ? node.get("path").asText() : null;
        //if (!OperationType.REMOVE.getValue().equals(type) && (path == null || path.isEmpty())) {
        // throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "Missing 'path' attribute for " + type + " operation.");
        //}
      } else if (key.equals("value")) {
        // Validate "value" field (mandatory for add/replace, prohibited for remove)
        JsonNode valueNode = node.get("value");
        if (!OperationType.REMOVE.getValue().equals(type) && valueNode == null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "Missing 'value' for " + type + " operation.");
        }
        if (OperationType.REMOVE.getValue().equals(type) && valueNode != null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_SYNTAX, "The 'remove' operation must not contain a value.");
        }
        value = Unmarshaller.jsonNodeToAttribute(null, "value", valueNode);
      }
    }
    
    if (type == null || type.isEmpty()) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimException.ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.ATTRIBUTE_MANDATORY, "op"));  
    }
    
    return new Operation(OperationType.fromString(type), path, value);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // SCIM Exception Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonNodeToScimException
  /**
   * Parses a JSON node into a SCIM resource object.
   * This method iterates through the fields of the provided JSON node and
   * parses each field into its corresponding SCIM attribute, then adds it to
   * the resource.
   *
   * @param jsonNode The JSON node representing the SCIM resource.
   * Allowed object is {@link JsonNode}.
   *
   * @return The SCIM resource object corresponding to the JSON node.
   * Possible object is {@link ScimResource}.
   */
  public static ScimException jsonNodeToScimException(JsonNode jsonNode) {
    String schemas  = new String();
    String details  = new String();
    String status   = new String();
    String scimType = new String();
    // Create a new SCIM resource object.
    // Get the attribute names on the first level from the JSON node.
    final Iterator<String> fieldNames = jsonNode.fieldNames();  
    
    while (fieldNames.hasNext()) {
      // Get the current field name.
      final String fieldName = fieldNames.next();
      // Get the value of the field.
      final JsonNode value = jsonNode.get(fieldName);
      
      switch (fieldName) {
        case ScimException.SCHEMAS:
          schemas  = jsonNodeToStringArray(value)[0];
        case ScimException.DETAIL:
          details  = value.asText();
        case ScimException.STATUS:
          status   = value.asText();
        case ScimException.SCIMTYPE:
          scimType = value.asText();
        default:
          break;
      }
    }
    
    
    return new ScimException(StatusCode.getStatus(Integer.parseInt(status)), details);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseMultiValueSingular
  /**
   * Parses a multi-value attribute from the JSON node.
   * This method checks if the multi-value attribute is simple (i.e., containing
   * basic types like String, Boolean, etc.) or complex
   * (containing sub-attributes) and processes accordingly.
   *
   * @param jsonNode The JSON node representing the multi-value attribute.
   * Allowed object is {@link JsonNode}.
   *
   * @return The parsed multi-value attribute.
   * Possible object is {@link Attribute}.
   */
  private static String[] jsonNodeToStringArray(final JsonNode jsonNode) {
    // List to hold the parsed simple values.
    final LinkedList<String> attributeValue = new LinkedList<String>();
    
    // Iterate over each element in the multi-value array.
    for (JsonNode node : jsonNode) {
      switch(node.getNodeType()) {
        case STRING:
          attributeValue.add(node.asText());
          break;
        default:
          break;
      }
    }
    
    return attributeValue.toArray(new String[0]);
  }

}
