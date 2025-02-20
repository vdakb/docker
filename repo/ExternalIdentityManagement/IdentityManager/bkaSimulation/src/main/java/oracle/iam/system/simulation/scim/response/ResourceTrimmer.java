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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   ResourceTrimmer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTrimmer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.response;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.system.simulation.scim.Path;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.scim.annotation.Definition;

import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

////////////////////////////////////////////////////////////////////////////////
// class ResourceTrimmer
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Utility implementing the SCIM standard for returning attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceTrimmer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final boolean excluded;
  private final ResourceTypeDefinition resourceType;
  private final Set<Path>              requestAttributes;
  private final Set<Path>              queryAttributes;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ResourcePreparer</code> for trimming returned
   ** attributes for a SCIM operation.
   **
   ** @param resourceType        the resource type definition for resources to
   **                            trim.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param requestAttributes   the attributes in the request object or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param queryAttributes     the attributes from the 'attributes' or
   **                            'excludedAttributes' query parameter.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param excluded            <code>true</code> if the queryAttributes came
   **                            from the excludedAttributes query parameter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public ResourceTrimmer(final ResourceTypeDefinition resourceType, final Set<Path> requestAttributes, final Set<Path> queryAttributes, final boolean excluded) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.resourceType      = resourceType;
    this.requestAttributes = requestAttributes;
    this.queryAttributes   = queryAttributes;
    this.excluded          = excluded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimObjectNode
  /**
   ** Trim attributes of the object node to return.
   **
   ** @param  node               the object node to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the trimmed object node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode trimObjectNode(final ObjectNode node) {
    return trimObjectNode(node, Path.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimObjectNode
  /**
   ** Trim attributes of an inner object node to return.
   **
   ** @param  node               the object node to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  parentPath         the parent path of attributes in the object.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the trimmed object node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  private ObjectNode trimObjectNode(final ObjectNode node, final Path parentPath) {
    ObjectNode                            objectToReturn = Support.jsonNodeFactory().objectNode();
    Iterator<Map.Entry<String, JsonNode>> i = node.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      final Path                  path;
      if (parentPath.root() && parentPath.namespace() == null && Support.namespace(field.getKey())) {
        path = Path.build(field.getKey());
      }
      else {
        path = parentPath.attribute(field.getKey());
      }

      if (path.root() || shouldReturn(path)) {
        if (field.getValue().isArray()) {
          ArrayNode trimmedNode = trimArrayNode((ArrayNode)field.getValue(), path);
          if (trimmedNode.size() > 0) {
            objectToReturn.set(field.getKey(), trimmedNode);
          }
        }
        else if (field.getValue().isObject()) {
          ObjectNode trimmedNode = trimObjectNode((ObjectNode)field.getValue(), path);
          if (trimmedNode.size() > 0) {
            objectToReturn.set(field.getKey(), trimmedNode);
          }
        }
        else {
          objectToReturn.set(field.getKey(), field.getValue());
        }
      }
    }
    return objectToReturn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimArrayNode
  /**
   ** Trim attributes of the values in the array node to return.
   *
   ** @param  node               the array node to return.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   ** @param  parentPath         the parent path of attributes in the array.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the trimmed aray node ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ArrayNode}.
   */
  private ArrayNode trimArrayNode(final ArrayNode node, final Path parentPath) {
    ArrayNode arrayToReturn = Support.jsonNodeFactory().arrayNode();
    for (JsonNode value : node) {
      if (value.isArray()) {
        ArrayNode trimmedNode = trimArrayNode((ArrayNode)value, parentPath);
        if (trimmedNode.size() > 0) {
          arrayToReturn.add(trimmedNode);
        }
      }
      else if (value.isObject()) {
        ObjectNode trimmedNode = trimObjectNode((ObjectNode)value, parentPath);
        if (trimmedNode.size() > 0) {
          arrayToReturn.add(trimmedNode);
        }
      }
      else {
        arrayToReturn.add(value);
      }
    }
    return arrayToReturn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shouldReturn
  /**
   ** Determine if the attribute specified by the path should be returned.
   **
   ** @param  path               the path for the attribute.
   **
   ** @return                   <code>true</code> to return the attribute or
   **                           <code>false</code> to remove the attribute from
   **                           the returned resource..
   */
  private boolean shouldReturn(final Path path) {
    Definition          definition = this.resourceType.attributeDefinition(path);
    Definition.Returned returned   = definition == null ? Definition.Returned.DEFAULT : definition.returned();

    switch (returned) {
      case ALWAYS  : return true;
      case NEVER   : return false;
      case REQUEST : // return only if it was one of the request attributes or
                     // if there are no request attributes, then only if it was
                     // one of the override query attributes.
                     return pathContains(this.requestAttributes, path) || (this.requestAttributes.isEmpty() && !this.excluded && pathContains(this.queryAttributes, path));
      default      : // return if it is not one of the excluded query attributes
                     // and no override query attributes are provided.
                     // If override query attributes are provided, only return
                     // if it is one of them.
                     if (this.excluded) {
                       return !pathContains(this.queryAttributes, path);
                     }
                     else {
                       return this.queryAttributes.isEmpty() || pathContains(this.queryAttributes, path);
                    }
    }
  }

  private boolean pathContains(final Set<Path> paths, final Path path) {
    // Exact path match
    if (paths.contains(path)) {
      return true;
    }

    if (!this.excluded) {
      // see if a sub-attribute of the given path is included in the list
      // ie. include name if name.givenName is in the list.
      for (Path p : paths) {
        if (p.size() > path.size() && path.equals(p.sub(path.size()))) {
          return true;
        }
      }
    }
    // see if the parent attribute of the given path is included in the list
    // ie. include name.{anything} if name is in the list.
    for (Path p = path; p.size() > 0; p = p.sub(p.size() - 1)) {
      if (paths.contains(p)) {
        return true;
      }
    }
    return false;
  }
}
