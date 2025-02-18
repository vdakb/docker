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

    File        :   ResourcePreparer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourcePreparer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest.response;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.platform.core.entity.Path;

////////////////////////////////////////////////////////////////////////////////
// class ResourcePreparer
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Utility to prepare a resource to return to the client.
 ** <br>
 ** This includes:
 ** <ul>
 **   <li>Returning the attributes based on the returned constraint of the
 **       attribute definition in the schema.
 **   <li>Returning the attributes requested by the client using the request
 **       resource as well as the attributes or excludedAttributes query
 **       parameter.
 ** </ul>
 **
 ** @param  <T>                  the type of the returned resources.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourcePreparer<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final URI       base;
  private final Set<Path> attribute;
  private final boolean   excluded;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Private constructor used by unit-test.
   **
   ** @param attribute           the attributes query param.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param exclude             <code>true</code> if the attribute collection
   **                            needs to be treated as excluded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param baseUri             the resource type base URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   */
  public ResourcePreparer(final Set<Path> attribute, final boolean exclude, final URI baseUri) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.base      = baseUri;
    this.excluded  = exclude;
    this.attribute = attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ResourcePreparer</code>.
   **
   ** @param  <T>                the implementation type of the REST resource to
   **                            return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param attribute           the attributes query param.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param exclude             <code>true</code> if the attribute collection
   **                            needs to be treated as excluded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param baseUri             the resource type base URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    an instance of
   **                            <code>ResourcePreparer</code> populated with
   **                            the values provided.
   **                            <br>
   **                            Possible object is
   **                            <code>ResourcePreparer</code>.
   */
  public static final <T> ResourcePreparer build(final Set<Path> attribute, final boolean exclude, final URI baseUri) {
    return new ResourcePreparer<T>(attribute, exclude, baseUri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimRetrieved
  /**
   ** Trim attributes of the resources returned from a search or lookup
   ** operation based on schema and the request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode trimRetrieved(final ObjectNode returned) {
    return trim(returned, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimCreated
  /**
   ** Trim attributes of the resources returned from a create operation based on
   ** schema as well as the request resource and request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  request            the resource in the PUT or POST request or
   **                            <code>null</code> or other requests.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode trimCreated(final ObjectNode returned, final ObjectNode request) {
    return trim(returned, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimReplaced
  /**
   ** Trim attributes of the resources returned from a replace operation based
   ** on schema as well as the request resource and request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  request            the resource in the PUT or POST request or
   **                            <code>null</code> or other requests.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode trimReplaced(final ObjectNode returned, final ObjectNode request) {
    return trim(returned, request);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimModified
  /**
   ** Trim attributes of the resources returned from a modify operation based on
   ** schema as well as the patch request and request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode trimModified( final ObjectNode returned) {
    return trim(returned, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Trim attributes of the resources to return based on schema and the client
   ** request.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  request            the resource in the PUT or POST request or
   **                            <code>null</code> or other requests.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  private ObjectNode trim(final ObjectNode returned, final ObjectNode request) {
    Set<Path> filter = Collections.emptySet();
    if (request != null) {
      filter = new LinkedHashSet<Path>();
      collectAttributes(Path.build(), filter, request);
    }
    return ResourceTrimmer.build(filter, this.attribute, this.excluded).trim(returned);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttributes
  /**
   ** Collect a list of attributes in the object node.
   **
   ** @param  parentPath         the parent path of attributes in the object.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  paths              the set of paths to add to.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param  node               the object node to collect from.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   */
  private void collectAttributes(final Path parentPath, final Set<Path> paths, final ObjectNode node) {
    final Iterator<Map.Entry<String, JsonNode>> i = node.fields();
    while (i.hasNext()) {
      final Map.Entry<String, JsonNode> field = i.next();
      final Path                        path = parentPath.path(field.getKey());
      if (path.size() > 1) {
        // don't add a path for the extension schema object itself.
        paths.add(path);
      }
      if (field.getValue().isArray()) {
        collectAttributes(path, paths, (ArrayNode)field.getValue());
      }
      else if (field.getValue().isObject()) {
        collectAttributes(path, paths, (ObjectNode)field.getValue());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttributes
  /**
   ** Collect a list of attributes in the array node.
   **
   ** @param  parentPath         the parent path of attributes in the array.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  paths              the set of paths to add to.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param  node               the array node to collect from.
   **                            <br>
   **                            Allowed object is {@link ArrayNode}.
   */
  private void collectAttributes(final Path parentPath, final Set<Path> paths, final ArrayNode node) {
    for (JsonNode value : node) {
      if (value.isArray()) {
        collectAttributes(parentPath, paths, (ArrayNode)value);
      }
      else if (value.isObject()) {
        collectAttributes(parentPath, paths, (ObjectNode)value);
      }
    }
  }
}
