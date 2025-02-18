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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ResourceTrimmer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTrimmer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.rest.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class ResourceTrimmer
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Utility implementing the REST standard for returning attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourceTrimmer {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final boolean   excluded;
  private final Set<Path> request;
  private final Set<Path> attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ResourceTrimmer</code> for trimming returned
   ** attributes for a SCIM operation.
   **
   ** @param request             the attributes in the request object or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param attribute           the attributes from the <code>emit</code> or
   **                            <code>omit</code> query parameter.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param excluded            <code>true</code> if the queryAttributes came
   **                            from the excludedAttributes query parameter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private ResourceTrimmer(final Set<Path> request, final Set<Path> attribute, final boolean excluded) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.request   = request;
    this.attribute = attribute;
    this.excluded  = excluded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Returns the <code>ResourceTrimmer</code> based on the passed value.
   **
   ** @param request             the attributes in the request object or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param attribute           the attributes from the <code>emit</code> or
   **                            <code>omit</code> query parameter.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param excluded            <code>true</code> if the <code>attribute</code>
   **                            came from the <code>omit</code> query
   **                            parameter.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>JsonTrimmer</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public static ResourceTrimmer build(final Set<Path> request, final Set<Path> attribute, final boolean excluded) {
    return new ResourceTrimmer(request, attribute, excluded);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
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
  public ObjectNode trim(final ObjectNode node) {
    return trim(node, Path.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
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
  private ObjectNode trim(final ObjectNode node, final Path parentPath) {
    ObjectNode                            o = Support.nodeFactory().objectNode();
    Iterator<Map.Entry<String, JsonNode>> i = node.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      final Path                  path;
      if (parentPath.root()) {
        path = Path.build(field.getKey());
      }
      else {
        path = parentPath.path(field.getKey());
      }

      if (path.root() || returned(path)) {
        if (field.getValue().isArray()) {
          ArrayNode trimmedNode = trim((ArrayNode)field.getValue(), path);
          if (trimmedNode.size() > 0) {
            o.set(field.getKey(), trimmedNode);
          }
        }
        else if (field.getValue().isObject()) {
          ObjectNode trimmedNode = trim((ObjectNode)field.getValue(), path);
          if (trimmedNode.size() > 0) {
            o.set(field.getKey(), trimmedNode);
          }
        }
        else {
          o.set(field.getKey(), field.getValue());
        }
      }
    }
    return o;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
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
  private ArrayNode trim(final ArrayNode node, final Path parentPath) {
    final ArrayNode result = Support.nodeFactory().arrayNode();
    for (JsonNode value : node) {
      if (value.isArray()) {
        final ArrayNode trimmed = trim((ArrayNode)value, parentPath);
        if (trimmed.size() > 0) {
          result.add(trimmed);
        }
      }
      else if (value.isObject()) {
        final ObjectNode trimmed = trim((ObjectNode)value, parentPath);
        if (trimmed.size() > 0) {
          result.add(trimmed);
        }
      }
      else {
        result.add(value);
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returned
  /**
   ** Determine if the attribute specified by <code>path</code> should be
   ** returned.
   **
   ** @param  path               the path for the attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    <code>true</code> to return the attribute or
   **                            <code>false</code> to remove the attribute from
   **                            the returned resource..
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean returned(final Path path) {
    // return if it is not one of the excluded query attributes and no override
    // query attributes are provided.
    // if override query attributes are provided, only return if it is one of
    // them
    if (this.excluded) {
      return !matches(this.attribute, path);
    }
    else {
      return this.attribute == null || this.attribute.isEmpty() || matches(this.attribute, path);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matches
  private boolean matches(final Set<Path> paths, final Path path) {
    // exact path match
    if (paths.contains(path))
      return true;

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
