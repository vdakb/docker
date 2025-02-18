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

    File        :   Marshaller.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the class Marshaller.
                    

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-26-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.parser;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.AttributeValue;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.PatchRequest;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ResourceDescriptor;
import bka.iam.identity.scim.extension.model.SchemaAttribute;
import bka.iam.identity.scim.extension.model.SchemaDescriptor;
import bka.iam.identity.scim.extension.model.ScimResource;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
////////////////////////////////////////////////////////////////////////////////
// class Marshaller
// ~~~~~ ~~~~~~~~~~
/**
 ** The Marshaller class is responsible for converting SCIM objects
 ** object into its JSON representation.
 ** 
 ** It is the reverse process of {@link Unmarshaller}.
 ** 
 ** @author  sylveret.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Marshaller {
  
  //////////////////////////////////////////////////////////////////////////////
  // Schema Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   SchemaToJsonNode
  /**
   ** Converts a SchemaDescriptor object into a JsonNode for serialization.
   ** This method creates the JSON structure that represents the schema,
   ** including its attributes and metadata.
   **
   ** @param schema   The {@link SchemaDescriptor} object to be converted.
   **                 Allowed object is {@link SchemaDescriptor}.
   **
   ** @return         A {@link JsonNode} containing the serialized schema.
   **                 Possible object is {@link JsonNode}.
   */
  public static JsonNode schemaToJsonNode(final SchemaDescriptor schema) {
    // Initialize ObjectMapper to create the JSON structure.
    final ObjectMapper mapper   = new ObjectMapper();
    // Create an ObjectNode to hold the schema data.
    final ObjectNode schemaNode = mapper.createObjectNode();

    schemaNode.putArray("schemas").add(schema.getSchemas());
    schemaNode.put("id", schema.getURI());
    
    // Add "attributes" field which contains the list of attributes.
    final ArrayNode attributesNode = mapper.createArrayNode();
    for (SchemaAttribute attribute : schema) {
       // Convert each attribute to JSON
      attributesNode.add(attributeToJsonNode(mapper, attribute));
    }
    schemaNode.set("attributes", attributesNode);
    schemaNode.put("name", schema.getName());
    final ObjectNode metaNode = mapper.createObjectNode();
    metaNode.put("location", schema.getMetaLocation());
    metaNode.put("resourceType", schema.getMetaResourceType());
    schemaNode.set("meta", metaNode);
    schemaNode.put("description", schema.getDescription());

    return schemaNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeToJsonNode
  /**
   ** Converts a SchemaAttribute object into a JsonNode for serialization.
   ** This method creates the JSON structure for an individual attribute,
   ** including its name, type, and other properties.
   **
   ** @param mapper    The {@link ObjectMapper} used for creating JSON nodes.
   **                  Allowed object is {@link ObjectMapper}.
   ** @param attribute The {@link SchemaAttribute} object to be serialized.
   **                  Allowed object is {@link SchemaAttribute}.
   **
   ** @return          A {@link JsonNode} containing the serialized attribute.
   **                  Possible object is {@link JsonNode}.
   */
  private static JsonNode attributeToJsonNode(ObjectMapper mapper, SchemaAttribute attribute) {
    // Create an ObjectNode for the attribute.
    final ObjectNode attributeNode = mapper.createObjectNode();

    attributeNode.put("name", attribute.getName());
    attributeNode.put("type", attribute.getType());
    attributeNode.put("description", attribute.getDescription());
    attributeNode.put("mutability", attribute.getMutability());

    attributeNode.put("returned", attribute.getReturned());
    attributeNode.put("uniqueness", attribute.getUniqueness());
    attributeNode.put("required", attribute.getRequired());
    attributeNode.put("multiValued", attribute.getMultiValued());
    attributeNode.put("caseExact", attribute.getCaseExact());

    // Handle sub-attributes (if any) for complex attributes.
    if (attribute.getSubAttributes() != null && attribute.getSubAttributes().length > 0) {
      ArrayNode subAttributesNode = mapper.createArrayNode();
      for (SchemaAttribute subAttribute : attribute.getSubAttributes()) {
        subAttributesNode.add(attributeToJsonNode(mapper, subAttribute));
      }
      attributeNode.set("subAttributes", subAttributesNode);
    }

    return attributeNode;
  }
  
  
  //////////////////////////////////////////////////////////////////////////////
  // SCIM Resource Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   listResourceToJsonNode
  /**
   ** Converts a {@link ListResponse} of {@link ScimResource} objects into a
   ** JSON node.
   **
   ** @param listResource  The {@link ListResponse} to convert.
   **                      Allowed object is {@link ListResponse}.
   ** 
   ** @return              The JSON representation of the list response.
   **                      Possible object is {@link JsonNode}.
   */
  public static JsonNode listResourceToJsonNode(final ListResponse<? extends ScimResource> listResource, final Set<String> omit, final Set<String> emit, Integer count, Integer startItem)
    throws ScimException {
    // Create a new ObjectMapper to handle the conversion.
    final ObjectMapper mapper = new ObjectMapper();
    // Create a root JSON object node.
    final ObjectNode node = mapper.createObjectNode();
    
    // Add the schemas attribute.
    ArrayNode schemasNode = mapper.createArrayNode();
    schemasNode.add(ListResponse.SCHEMAS[0]);
    node.set("schemas", schemasNode);
    
    // Add the totalResults attribute.
    node.put("totalResults", listResource.getTotalResult());
    
    // Add the Resources sub attribute
    ArrayNode resourcesNode = mapper.createArrayNode();
    
    int startIndex = startItem != null ? startItem : 1;
    long itemsPerPage = count != null ? count : listResource.getTotalResult();
    long endIndex = Math.min(startIndex + itemsPerPage - 1, listResource.getResources().size());
    // Iterate through the attributes of the resource.
    if (startIndex > 0 && startIndex <= listResource.getResources().size()) {
        for (int i = startIndex - 1; i < endIndex; i++) {
            ScimResource resource = listResource.getResources().get(i);
            JsonNode resourceNode = resourceToJsonNode(resource, omit, emit);
            resourcesNode.add(resourceNode);
        }
    }
    
    System.out.println("After startIndex: " + startIndex);
    System.out.println("After itemsPerPage: " + itemsPerPage);
    System.out.println("After endIndex: " + endIndex);
    
    System.out.println("Original startIndex: " + listResource.getStartIndex());
    System.out.println("Original itemsPerPage: " + listResource.getItemsPerPage());
      
    // Add the itemsPerPage attribute.
    if (listResource.getItemsPerPage() != 0 && listResource.getTotalResult() != listResource.getItemsPerPage())
      node.put("itemsPerPage", listResource.getItemsPerPage());
    else if (startItem != null || count != null)
     node.put("itemsPerPage", resourcesNode.size());
    // Add the startIndex attribute.
    if (listResource.getStartIndex() != 0)
      node.put("startIndex", listResource.getStartIndex());
    else if (startItem != null || count != null)
     node.put("startIndex", startIndex);
    
    node.set("Resources", resourcesNode);
    
    return node;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceToJsonNode
  /**
   ** Converts a {@link Resource} into a {@link JsonNode}.
   ** This method iterates through all the attributes of the resource
   ** and converts them into their respective JSON representations.
   ** 
   ** @param resource  The {@link Resource} to convert.
   **                  Allowed object is {@link Resource}.
   ** 
   ** @return          The JSON representation of the resource.
   **                  Possible object is {@link JsonNode}.
   */
  public static JsonNode resourceToJsonNode(final Resource resource, Set<String> omit, Set<String> emit)
    throws ScimException {
    final ObjectMapper       mapper             = new ObjectMapper();
    final ObjectNode         node               = mapper.createObjectNode();
    final ResourceDescriptor resourceDescriptor = resource.getResourceDescriptor();
    
    final String coreURI = resourceDescriptor.getCoreSchema() != null ? resourceDescriptor.getCoreSchema().getURI() : null;
    
    if ((omit != null && !omit.isEmpty()) && (emit != null && !emit.isEmpty())) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.string(ScimMessage.INVALID_QUERY_PARAMETERS));
    }
    
    if (omit == null) {
        omit = new HashSet<>();
    }
    else {
      // Check if all attributes exist
      for (String attributePath : omit) {
        if (resourceDescriptor.get(attributePath) == null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, attributePath));
        }
      }
      if (coreURI != null)
        omit = omit.stream().map(elt -> elt.startsWith(coreURI) ? elt.substring(coreURI.length() + 1) : elt).collect(Collectors.toSet());
    }
    if (emit == null) {
        emit = new HashSet<>();
    }
    else {
      // Check if all attributes exist
      for (String attributePath : emit) {
        if (resourceDescriptor.get(attributePath) == null) {
          throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimBundle.format(ScimMessage.ATTRIBUTE_SCHEMA_NOTFOUND, attributePath));
        }
      }
      if (coreURI != null)
        emit = emit.stream().map(elt -> elt.startsWith(coreURI) ? elt.substring(coreURI.length() + 1) : elt).collect(Collectors.toSet());
    }

    final Iterator<Attribute> attributes = resource.iterator();
    while (attributes.hasNext()) {
        Attribute attribute = attributes.next();
        String fullPath = attribute.getName();
        SchemaAttribute schemaAttribute = resourceDescriptor.get(attribute.getName());
        // Handle "default" attributes based on omit and emit
        if (schemaAttribute != null && schemaAttribute.getReturned().equalsIgnoreCase("default")) {
            if (emit.contains(fullPath) || emit.stream().anyMatch(path -> path.startsWith(fullPath))) {
                attributeToJsonNode(node, mapper, attribute, schemaAttribute, omit, emit, fullPath);
            } else if (!omit.contains(fullPath)) {
                attributeToJsonNode(node, mapper, attribute, schemaAttribute, omit, emit, fullPath);
            }
        } else if (schemaAttribute == null) {
           if (!omit.contains(fullPath) || emit.stream().anyMatch(path -> path.startsWith(fullPath))) {
            attributeToJsonNode(node, mapper, attribute, null, omit, emit, fullPath);
           }
        }
      
       // Always include "always" attributes
        if (schemaAttribute != null && schemaAttribute.getReturned().equalsIgnoreCase("always")) {
            attributeToJsonNode(node, mapper, attribute, schemaAttribute, omit, emit, fullPath);
            continue;
        }

    }

    return node;
}
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeToJsonNode
  /**
   * Converts an individual SCIM attribute into its corresponding JSON
   * representation.
   * This method handles different attribute types: multi-value, complex
   * and simple attributes.
   *
   * @param node The JSON object node to which the attribute should be
   * added.
   * Allowed object is {@link ObjectNode}.
   * @param mapper The ObjectMapper instance to handle the conversion.
   * Allowed object is {@link ObjectMapper}.
   * @param attribute The SCIM attribute to be converted into a JSON node.
   * Allowed object is {@link Attribute}.
   */
  public static void attributeToJsonNode(final ObjectNode node, final ObjectMapper mapper, final Attribute attribute, final SchemaAttribute schemaAttribute, Set<String> omit, final Set<String> emit, final String parentPath)
    throws ScimException {
    String fullPath = parentPath;
    
    
    if (schemaAttribute != null && !schemaAttribute.getReturned().equalsIgnoreCase("always") && isExcluded(fullPath, omit)) {
      return;
    }
    
    if ((schemaAttribute != null && (!schemaAttribute.getReturned().equalsIgnoreCase("always"))&& emit != null && !emit.isEmpty() && !isEmitted(fullPath, emit))) {
        return;
    }
    
    if (attribute.isMultiValue() && attribute.isComplex()) {
      node.set(attribute.getName(), multiValueComplexAttributeToJsonNode(mapper, attribute.getValues(), schemaAttribute, omit, emit, fullPath));
    }
    else if (attribute.isMultiValue()) {
      node.set(attribute.getName(), multiValueSimpleAttributeToJsonNode(mapper, attribute.getValues()));
    }
    else if (attribute.isComplex()) {
      node.set(attribute.getName(), singularComplexAttributeToJsonNode(mapper, attribute.getValue(), schemaAttribute, omit, emit, fullPath));
    }
    else {
      singularSimpleAttributeToJsonNode(node, attribute.getName(), attribute.getValue());
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSingularComplexAttributeToJsonNode
  /**
   * Converts a singular complex attribute into its corresponding JSON node.
   * The method recursively converts sub-attributes and adds them to the JSON
   * node.
   *
   * @param mapper The ObjectMapper instance to handle the conversion.
   * Allowed object is {@link ObjectMapper}.
   * @param values The value of the complex attribute, containing
   * sub-attributes.
   * Allowed object is {@link AttributeValue}.
   *
   * @return The JSON node representing the singular complex attribute.
   * Possible object is {@link ObjectNode}.
   */
  private static ObjectNode singularComplexAttributeToJsonNode(final ObjectMapper mapper, final AttributeValue values, final SchemaAttribute schemaAttribute, Set<String> omit, final Set<String> emit, final String parentPath)
    throws ScimException {
    // Create a new JSON object node for the complex attribute.
    final ObjectNode node = mapper.createObjectNode();  
    
    for (Attribute attribute : values.getSubAttributes()) {
      String fullPath = appendAttributeToPatch(parentPath, attribute.getName(), schemaAttribute != null ? schemaAttribute.getSubSchemaAttribute(attribute.getName()) : null);
      if (isExcluded(fullPath, omit)) {
        continue;
      }
      
      // Check if the full path should be emitted
      if (emit != null && (!emit.isEmpty()) && (!isEmitted(fullPath, emit))) {
        continue;
      }
      // Convert each sub-attribute and add it to the node.
      SchemaAttribute subSchemaAttribute = schemaAttribute != null ? schemaAttribute.getSubSchemaAttribute(attribute.getName()) : null;

      attributeToJsonNode(node, mapper, attribute, subSchemaAttribute, omit, emit, fullPath);

    }

    return node;
  }
  
  private static String appendAttributeToPatch(String path, String attributeName, SchemaAttribute schemaAttribute)
    throws ScimException {
    final StringBuilder nwPath = new StringBuilder();
    
    
    if (path.isEmpty())
      nwPath.append(attributeName);
    else {
      nwPath.append(path);
      if (OIMSchema.getInstance().getAllSchemaURIs().contains(nwPath.toString())) {
        nwPath.append(":");
      }
      else {
        nwPath.append(".");
      }
      nwPath.append(attributeName);
    }
    
    return nwPath.toString();
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueSimpleAttributeToJsonNode
  /**
   ** Converts a multi-value simple attribute into its corresponding JSON array
   ** node.
   ** 
   ** @param mapper  The ObjectMapper instance to handle the conversion.
   **                Allowed object is {@link ObjectMapper}.
   ** @param values  The values of the multi-value simple attribute.
   **                Allowed object is {@link AttributeValue[]} array.
   ** 
   ** @return The JSON array node representing the multi-value simple attribute.
   **         Possible object is {@link ArrayNode}.
   */
  private static ArrayNode multiValueSimpleAttributeToJsonNode(final ObjectMapper mapper, final AttributeValue[] values) {
    // Create a new JSON array node.
    final ArrayNode node = mapper.createArrayNode(); 
    for (AttributeValue value : values) {
      // Convert each value and add it to the array node.
      multiSimpleToJsonNode(node, value);
    }
    
    return node;
  }
        
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiSimpleToJsonNode
  /**
   * Converts a simple value in a multi-value simple attribute into a JSON node.
   * The method handles different types of simple values, such as String,
   * Integer and so on.
   *
   * @param node The JSON array node to which the value should be added.
   * Allowed object is {@link ArrayNode}.
   * @param value The value to be converted into a JSON node.
   * Allowed object is {@link AttributeValue}.
   */
  private static void multiSimpleToJsonNode(final ArrayNode node, final AttributeValue value) {
    if (value.isString())
      node.add(value.getStringValue());
    else if (value.isReference())
      node.add(value.getStringValue());
    else if (value.isInteger())
      node.add(value.getIntegerValue());
    else if (value.isBinary())
      node.add(value.getBinaryValue());
    else if (value.isBoolean())
      node.add(value.getBooleanValue());
    else if (value.isDateTime())
      node.add(value.getStringValue());
    else if (value.isDecimal())
      node.add(value.getDecimalValue());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMultiValueComplexAttributeToJsonNode
  /**
   ** Converts a multi-value complex attribute into its corresponding JSON array node.
   ** The method recursively processes each value and its sub-attributes.
   ** 
   ** @param mapper    The ObjectMapper instance to handle the conversion.
   **                  Allowed object is {@link ObjectMapper}.
   ** @param values    The values of the multi-value complex attribute.
   **                  Allowed object is {@link AttributeValue[]} array.
   ** 
   ** @return          The JSON array node representing the multi-value complex
   **                  attribute.
   **                  Possible object is {@link ArrayNode}.
   */
  private static JsonNode multiValueComplexAttributeToJsonNode(final ObjectMapper mapper, final AttributeValue[] values, SchemaAttribute schemaAttribute, Set<String> omit, final Set<String> emit, final String parentPath)
    throws ScimException {
    // Create a new JSON array node
    final ArrayNode arrayNode = mapper.createArrayNode();
    
    for (AttributeValue value : values) {
      // Create a new object node for each complex value.
      final ObjectNode objectNode = mapper.createObjectNode();
      
      // In case of MVC does contains complex attribute
      if (value.getSubAttributes() == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "Attribute value " + value + " is not a complex attributes");
      }
      for (Attribute attribute : value.getSubAttributes()) {
        String fullPath = appendAttributeToPatch(parentPath, attribute.getName(), schemaAttribute != null ? schemaAttribute.getSubSchemaAttribute(attribute.getName()) : null);
        final SchemaAttribute schemaSubAttribute = schemaAttribute != null ? schemaAttribute.getSubSchemaAttribute(attribute.getName()) : null;
        
        if (schemaSubAttribute != null && !schemaSubAttribute.getReturned().equalsIgnoreCase("always") && isExcluded(fullPath, omit)) {
          continue;
        }
        
        if (emit !=null && !emit.isEmpty() && !isEmitted(fullPath, emit)) {
          continue;
        }
        
        // Convert each sub-attribute and add it to the object node.
        attributeToJsonNode(objectNode, mapper, attribute, schemaAttribute, omit, emit, fullPath);
      }
      arrayNode.add(objectNode);
    }
    
    return arrayNode;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addSingularSimpleAttributeToJsonNode
  /**
   ** Converts a singular simple attribute into its corresponding JSON
   ** representation.
   ** The method handles different simple types, such as String, Integer and so
   ** on.
   **
   ** @param node       The JSON object node to which the value should be added.
   **                   Allowed object is {@link ObjectNode}.
   ** @param name       The name of the attribute.
   **                   Allowed object is {@link String}.
   ** @param value      The value of the attribute to be converted into a JSON node.
   **                   Allowed object is {@link AttributeValue}.
   */
  private static void singularSimpleAttributeToJsonNode(final ObjectNode node, final String name, final AttributeValue value) {
    if (value.isString())
      node.put(name, value.getStringValue());
    else if (value.isInteger())
      node.put(name, value.getIntegerValue());
    else if (value.isBinary())
      node.put(name, value.getBinaryValue());
    else if (value.isBoolean())
      node.put(name, value.getBooleanValue());
    else if (value.isDecimal())
      node.put(name, value.getDecimalValue());
    else if (value.isReference())
      node.put(name, value.getReferenceValue().toString());
    else if (value.isDateTime()) {
      final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      dateFormat.setTimeZone(TimeZone.getDefault());
      node.put(name, dateFormat.format(value.getDateTimeValue()));
    }
  }
  
  private static boolean isExcluded(String path, Set<String> omit) {
    if(omit != null){
      for (String excludedPath : omit) {
        if (path.equals(excludedPath) || path.startsWith(excludedPath)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isEmitted(String path, Set<String> emit) {
    if(emit != null){
      for (String emittedPath : emit) {
        if (path.equals(emittedPath) || emittedPath.startsWith(path) || path.contains(emittedPath)) {
          return true;
        }
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Patch Marshaller
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchRequestToJsonNode
  /**
   ** Converts a {@link PatchRequest} into a JSON representation.
   **
   ** @param patchRequest The {@link PatchRequest} to convert.
   **                     Allowed object is {@link PatchRequest}.
   ** 
   ** @return             The JSON representation of the patch request.
   **                     Possible object is {@link ObjectNode}.
   ** 
   ** @throws ScimException If the patch request is invalid.
   */
  public static ObjectNode patchRequestToJsonNode(PatchRequest patchRequest)
  throws ScimException {
    
    final ObjectMapper mapper = new ObjectMapper();
    if (patchRequest == null) {
      //TODO: modify exception
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "");
    }

    // Create the root JSON node
    ObjectNode rootNode = mapper.createObjectNode();

    // Add "schemas" field
    ArrayNode schemasNode = mapper.createArrayNode();
    for (String schema : patchRequest.getSchemas()) {
      schemasNode.add(schema);
    }
    rootNode.set("schemas", schemasNode);

    // Add "Operations" field
    ArrayNode operationsNode = mapper.createArrayNode();
    for (Operation operation : patchRequest.getOperations()) {
      operationsNode.add(patchOperationToJsonNode(operation, mapper));
    }
    rootNode.set("Operations", operationsNode);

    return rootNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   patchOperationToJsonNode
  /**
   ** Converts an {@link Operation} object into a JSON representation.
   **
   ** @param operation   The {@link Operation} to convert.
   **                    Allowed object is {@link Operation}.
   ** @param mapper      The {@link ObjectMapper} used for JSON conversion.
   **                    Allowed object is {@link ObjectMapper}.
   ** 
   ** @return            The JSON representation of the operation.
   **                    Possible object is {@link ObjectNode}.
   ** 
   ** @throws ScimException If the operation is invalid.
   */
  private static ObjectNode patchOperationToJsonNode(Operation operation, ObjectMapper mapper)
        throws ScimException {
    if (operation == null) {
      //TODO: modify exception
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, "");
    }

    // Create the operation JSON node
    ObjectNode operationNode = mapper.createObjectNode();

    // Add "op" field
    operationNode.put("op", operation.getOperationType().toString());

    // Add "path" field if present
    if (operation.getPath() != null && !operation.getPath().isEmpty()) {
        operationNode.put("path", operation.getPath());
    }

    // Add "value" field
    if (operation.getValue() != null) {
      Marshaller.attributeToJsonNode(operationNode, mapper, operation.getValue(), null , null, null, "");
    }

    return operationNode;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // SCIM Error Marshaller
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   toJsonNode
  /**
   * Converts a ScimException object into a JSON node.
   *
   * @param exception        The ScimException to be marshalled.
   *                         Allowed object is {@link ScimException}.
   *
   * @return                 A JSON representation of the ScimException.
   *                         Possible object is {@link ObjectNode}.
   */
  public static ObjectNode scimErrorToJsonNode(ScimException exception) {
    // Create a JSON object node using Jackson's JsonNodeFactory.
    ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

    // Add the SCIM schemas.
    rootNode.putArray(ScimException.SCHEMAS).add(exception.getSchemas());

    // Add the detail message.
    rootNode.put(ScimException.DETAIL, exception.getDetails());

    // Add the HTTP status code.
    rootNode.put(ScimException.STATUS, String.valueOf(exception.getHttpCode().getStatusCode()));

    // Add the error type if present.
    if (exception.getScimType() != null) {
      rootNode.put(ScimException.SCIMTYPE, exception.getScimType());
    }

    return rootNode;
  }
}
