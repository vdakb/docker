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

    File        :   PatchUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    PatchUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2024-06-12  TSebo       First release version
*/
package bka.iam.identity.scim.extension.utils;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimException.ScimType;
import bka.iam.identity.scim.extension.exception.ScimMessage;
import bka.iam.identity.scim.extension.exception.resource.ScimBundle;
import bka.iam.identity.scim.extension.model.Operation;
import bka.iam.identity.scim.extension.model.Operation.OperationType;
import bka.iam.identity.scim.extension.parser.Marshaller;
import bka.iam.identity.scim.extension.rest.HTTPContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.core.entity.Path.Segment;
import oracle.hst.platform.rest.ProcessingException;
import oracle.hst.platform.rest.entity.PathParser;
import oracle.hst.platform.rest.schema.Support;
////////////////////////////////////////////////////////////////////////////////
// class PatchUtil
// ~~~~~ ~~~~~~~~~
/**
 ** The PatchUtil class allows SCIM client to patch data.
 ** Supported operations are add, replace, remove.
 ** For more information refer to PATCH (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>)
 ** 
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PatchUtil {
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Avoid to initiate an <code>PatchUtil</code>, all method are static
   */
  private PatchUtil() {
    super();
  }
    
  //////////////////////////////////////////////////////////////////////////////
  // Method:   applyPatch
  /**
   ** Apply a SCIM patch operation on the provided resource.
   ** <p>
   ** This method processes the patch operation according to its type
   ** (ADD, REMOVE, REPLACE) and modifies the resource accordingly.
   ** </p>
   ** 
   ** @param  resource     The SCIM resource to be patched.
   **                      Allowed object is {@link JsonNode}.
   ** @param  operation    The patch operation to apply.
   **                      Allowed object is {@link Operation}.
   ** 
   ** @return              The patched SCIM resource as a JSON node.
   **                      Allowed object is {@link JsonNode}.
   **
   ** @throws ScimException If an error occurs while applying the patch operation.
   */
  public static JsonNode applyPatch(final JsonNode resource, final Operation operation) throws ScimException {
    final ObjectNode    resourceNode = (ObjectNode) resource;
    final String        path         = operation.getPath();
    final ObjectMapper  mapper       = new ObjectMapper();
    final OperationType operationType = operation.getOperationType();  

    JsonNode value = null;
      
    if (!operationType.equals(OperationType.REMOVE)) {
      value = mapper.createObjectNode();
      Marshaller.attributeToJsonNode((ObjectNode) value, mapper, operation.getValue(), null, null, null, "");
    }
    
    switch (operationType) {
      case ADD:
        addValue(resourceNode, path, value.get("value"));
        break;
      case REMOVE:
        removeValue(resourceNode, path);
        break;
      case REPLACE:
        replaceValue(resourceNode, path, value.get("value"));
        break;
      default:
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_VALUE, ScimBundle.format(ScimMessage.PATCH_UNSUPPORTED_OPERATION, operationType));
    }
    return resourceNode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addValue
  /**
   ** This method adds a value to the specified path in the JSON resource.
   ** 
   ** @param  resourceNode The root JSON object to modify.
   **                      Allowed object is {@link ObjectNode}.
   ** @param  path         The JSON path where the value should be added.
   **                      Allowed object is {@link String}.
   ** @param  value        The value to be added.
   **                      Allowed object is {@link JsonNode}.
   **
   ** @throws Exception    If the path does not exist or an error occurs.
   */
  private static void addValue(final ObjectNode resourceNode, final String path, final JsonNode value) throws ScimException {
    List<JsonNode> targetNodes = new ArrayList<>();
    
    Path pathParsed = null;
    try {
      pathParsed = PathParser.path(path);
      targetNodes = Support.matchPath(pathParsed, resourceNode);
    }
    catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e);
    }
    catch (ParseException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_VALUE, e.getMessage());
    }
    
    if (targetNodes.isEmpty()) {
      //if (path.contains("[") && !value.isArray() && !value.isObject())
      //  throw new IllegalArgumentException("Cannot add a value Path: " + path + "does not exist");
      if (pathParsed != null) {
        
        ObjectNode       currentNode =  resourceNode;
        ObjectNode  newNode = null;
        if (pathParsed.schemaUrn() != null && !pathParsed.schemaUrn().isEmpty() ) {
          JsonNode  node = currentNode.get(pathParsed.schemaUrn());
          if (node == null) {
            newNode = new ObjectMapper().createObjectNode();
            currentNode.set(pathParsed.schemaUrn(), newNode);
            currentNode = newNode;
          }
          else {
            currentNode = (ObjectNode) node;
          }
          
        }
        for (int i = 0; i < pathParsed.size() - 1; i++) {
          Segment segment = pathParsed.segment(i);
          JsonNode  node = currentNode.get(segment.attribute());
          if (node == null) {
            newNode = new ObjectMapper().createObjectNode();
            currentNode.set(segment.attribute(), newNode);
            currentNode = newNode;
          }
          else {
            currentNode = (ObjectNode) node;
          }
        }
          ((ObjectNode) currentNode).set(pathParsed.segment(pathParsed.size() - 1).attribute(),  value);
      } else {
        resourceNode.set(path, value);
      }
    }
    else {
      JsonNode targetNode = targetNodes.get(0);
      if (targetNode.isArray()) {
        if (value.isArray()) {
          for (JsonNode node : value)
            ((ArrayNode) targetNode).add(node);
        }
        else {
          ((ArrayNode) targetNode).add(value);
          }
      }
      else {
        throw new ScimException(HTTPContext.StatusCode.CONFLICT,  ScimType.MUTABILITY, ScimBundle.format(ScimMessage.PATCH_DUPLICATE_VALUE, path));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeValue
  /**
   ** Removes a value from the specified path in the JSON resource.
   ** 
   ** @param  resourceNode The root JSON object to modify.
   **                      Allowed object is {@link ObjectNode}.
   ** @param  path         The JSON path where the value should be removed.
   **                      Allowed object is {@link String}.
   **
   ** @throws Exception    If the path does not exist or an error occurs.
   */
  private static void removeValue(final ObjectNode resourceNode, String path) throws ScimException {
    List<JsonNode> targetNodes = new ArrayList<>();
      
    try {
      targetNodes = Support.matchPath(PathParser.path(path), resourceNode);
    }
    catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, e);
    }
    catch (ParseException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_VALUE, e.getMessage());
    }
    
    if (!targetNodes.isEmpty()) {
      for (JsonNode targetNode : targetNodes) {
        final JsonNode parentNode = findParentNode(resourceNode, targetNode);
        if (path.startsWith("urn")) {
          path = path.substring(path.lastIndexOf(":") + 1);
        }
        final String key = path.substring(path.lastIndexOf('.') + 1);
        
        if (parentNode != null) {
          if (parentNode.isArray()) {
            if (targetNode.isArray())
              targetNode = ((ArrayNode) targetNode).get(0);
            List<JsonNode> nodeToKeep = new ArrayList<JsonNode>();
            for (JsonNode arrayNode : parentNode) {
              if (targetNode.equals(arrayNode))
                continue;
              nodeToKeep.add(arrayNode);
            }
            ((ArrayNode) parentNode).removeAll();
            ((ArrayNode) parentNode).addAll(nodeToKeep);
          }
          else {
            ((ObjectNode) parentNode).remove(key);
          }
          return;
        }
      }
    }
    throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_FILTER, ScimBundle.format(ScimMessage.PATCH_PATH_NOT_EXIST, path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceValue
  /**
   ** Replaces a value at the specified path in the JSON resource.
   ** 
   ** @param  resourceNode The root JSON object to modify.
   **                      Allowed object is {@link ObjectNode}.
   ** @param  path         The JSON path where the value should be replaced.
   **                      Allowed object is {@link String}.
   ** @param  value        The new value to set.
   **                      Allowed object is {@link JsonNode}.
   **
   ** @throws Exception    If the path does not exist or an error occurs.
   */
  private static void replaceValue(final ObjectNode resourceNode, String path, JsonNode value)
  throws ScimException {     
    System.out.println("ObjectNode value: " + resourceNode);
    if (path == null || path.isEmpty()) {
      if (!value.isObject()) {
        //RFC 7644. Section 3.5.2.3
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_SYNTAX, ScimBundle.string(ScimMessage.PATCH_RESOURCE_WIHTOUT_ATTRIBUTE));
      }
      // Merge the provided attributes into the resource (partial update, not full replace)
      resourceNode.setAll((ObjectNode) value);
      return;
    }
    
    List<JsonNode> targetNodes = new ArrayList<>();
    try {
      /*if(PathParser.path(path).schemaUrn() != null){
        JsonNode tempNode = resourceNode.path(PathParser.path(path).schemaUrn());
        for (JsonNode node : tempNode) {
          targetNodes.add(node);
          break;
        }*
        
      }
      else {*/
        targetNodes = Support.matchPath(PathParser.path(path), resourceNode);
      
      System.out.println("targetNodes: " + targetNodes);
    }
    catch (ParseException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_SYNTAX, e.getMessage());
    }
    catch (ProcessingException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_SYNTAX, e.getMessage());
    }
    catch (ClassCastException e) {
      throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_SYNTAX, e.getMessage());
    }
    
    if (!targetNodes.isEmpty()) {
      for (JsonNode targetNode : targetNodes) {
        JsonNode parentNode = findParentNode(resourceNode, targetNode);
        if (parentNode != null) {
          // Extract the key from the path
          if (path.startsWith("urn")) {
            path = path.substring(path.lastIndexOf(":") + 1);
          }
          String key = path.substring(path.lastIndexOf('.') + 1);
          if (parentNode.isArray()) {
            ((ArrayNode) parentNode).removeAll();
            if (value.isArray()) {
              for (JsonNode arrayNode : value)
                ((ArrayNode) parentNode).add((JsonNode)arrayNode);
              }
            else {
              ((ArrayNode) parentNode).add((JsonNode)value);
            }
          } else {
            ((ObjectNode) parentNode).set(key, value);
          }
          return;
        }
      }
    } else {
      addValue(resourceNode, path, value);
      return;
    }
    
    throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, ScimType.INVALID_FILTER, ScimBundle.format(ScimMessage.PATCH_PATH_NOT_EXIST, path));
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   findParentNode
  /**
   ** Finds the parent node of a given node within the JSON structure.
   ** 
   ** @param  root        The root JSON object.
   **                     Allowed object is {@link ObjectNode}.
   ** @param  targetNode  The target node whose parent is being searched.
   **                     Allowed object is {@link JsonNode}.
   **
   ** @return             The parent JSON node, or null if not found.
   */
  private static JsonNode findParentNode(ObjectNode root, JsonNode targetNode) {
    final Iterator<String> fieldIterator = root.fieldNames();
    
    if (targetNode.isArray() && targetNode.size() == 1)
      targetNode = targetNode.get(0);
    
    while (fieldIterator.hasNext()) {
      String fieldName = fieldIterator.next();
      JsonNode child = root.get(fieldName);
      
      // Compare pointer value and not JSON value
      if (child == targetNode) {
        return root; // Found the parent node
      }
      if (child.isObject()) {
        JsonNode found = findParentNode((ObjectNode) child, targetNode);
            
        if (found != null) {
          return found;
        }
      }
      if (child.isArray()) {
        for (JsonNode arrayElement : child) {
          // Compare pointer value and not JSON value
          if (arrayElement  == targetNode) {
            return child;
          }
          if (arrayElement.isObject()) {
            JsonNode found = findParentNode((ObjectNode) arrayElement, targetNode);
            if (found != null) {
              return found;
            }
          }
        }
      }
    }
    // Parent not found
    return null; 
  }
}
