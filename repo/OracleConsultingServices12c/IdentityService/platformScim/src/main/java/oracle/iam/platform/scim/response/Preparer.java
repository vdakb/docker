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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   Preparer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Preparer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.response;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashSet;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.entity.Path;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Metadata;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.request.PatchOperation;

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
 **   <li>Setting the meta.resourceType and meta.location attributes if not
 **       already set.
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
public class Preparer<T extends Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final URI                    base;
  private final ResourceTypeDefinition type;
  private final Set<Path>              attribute;
  private final boolean                excluded;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Private constructor used by unit-test.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
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
   */
  private Preparer(final ResourceTypeDefinition type, final Set<Path> attribute, final boolean exclude, final URI baseUri) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.type      = type;
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
   ** Factory method to create a new <code>ResourcePreparer</code> with the
   ** properties specified.
   **
   ** @param  <T>                the implementation type of the
   **                            {@link Resource} to return.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
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
   ** @return                    an instance of <code>Preparer</code> populated
   **                            with the values provided.
   **                            <br>
   **                            Possible object is <code>Preparer</code>.
   */
  public static final <T extends Resource> Preparer build(final ResourceTypeDefinition type, final Set<Path> attribute, final boolean exclude, final URI baseUri) {
    return new Preparer<T>(type, attribute, exclude, baseUri);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locate
  /**
   ** Sets the meta.resourceType and meta.location metadata attribute values.
   **
   ** @param  returned           the resource to set the attributes.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the resource enriched with the location
   **                            metadata.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public T locate(final T returned) {
    Metadata meta = returned.metadata();
    boolean metaUpdated = false;
    if (meta == null) {
      meta = new Metadata();
    }

    if (meta.resourceType() == null) {
      meta.resourceType(this.type.name());
      metaUpdated = true;
    }

    if (meta.location() == null) {
      String id = returned.id();
      if (id != null) {
        meta.location(UriBuilder.fromUri(this.base).segment(id).build());
      }
      else {
        meta.location(this.base);
      }
      metaUpdated = true;
    }

    if (metaUpdated) {
      returned.metadata(meta);
    }
    return returned;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retrieved
  /**
   ** Trim attributes of the resources returned from a search or retrieve
   ** operation based on schema and the request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link Generic}.
   */
  public Generic retrieved(final T returned) {
    return trim(returned, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Trim attributes of the resources returned from a create operation based on
   ** schema as well as the request resource and request parameters.
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
   **                            Possible object is {@link Generic}.
   */
  public Generic created(final T returned, final T request) {
    return trim(returned, request, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaced
  /**
   ** Trim attributes of the resources returned from a replace operation based
   ** on schema as well as the request resource and request parameters.
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
   **                            Possible object is {@link Generic}.
   */
  public Generic replaced(final T returned, final T request) {
    return trim(returned, request, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modified
  /**
   ** Trim attributes of the resources returned from a modify operation based on
   ** schema as well as the patch request and request parameters.
   **
   ** @param  returned           the resource to return.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  patch              the patch operations in the PATCH request or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link PatchOperation}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link Generic}.
   */
  public Generic modified( final T returned, final Iterable<PatchOperation> patch) {
    return trim(returned, null, patch);
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
   ** @param  patch              the patch operations in the PATCH request or
   **                            <code>null</code> for other requests.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link PatchOperation}.
   **
   ** @return                    the trimmed resource ready to return to the
   **                            client.
   **                            <br>
   **                            Possible object is {@link Generic}.
   */
  @SuppressWarnings("unchecked")
  private Generic trim(final T returned, final T request, final Iterable<PatchOperation> patch) {
    Set<Path> filter = Collections.emptySet();
    if (request != null) {
      filter = new LinkedHashSet<Path>();
      collectAttributes(Path.build(), filter, request.generic().objectNode());
    }

    if (patch != null) {
      filter = new LinkedHashSet<Path>();
      collectAttributes(filter, patch);
    }
    locate(returned);
    final Generic generic  = returned.generic();
    final Trimmer trimmer  = Trimmer.build(this.type, filter, this.attribute, this.excluded);
    final Generic prepared = Generic.build(trimmer.trim(generic.objectNode()));
    return prepared;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectAttributes
  /**
   ** Collect a list of attributes in the patch operation.
   **
   ** @param  paths              the set of paths to add to.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Path}.
   ** @param  patch              the patch operation to collect attributes from.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link PatchOperation}.
   */
  private void collectAttributes(final Set<Path> paths, final Iterable<PatchOperation> patch) {
    for (PatchOperation operation : patch) {
      Path path = Path.build();
      if (operation.path() != null) {
        path = this.type.normalizePath(operation.path()).withoutFilters();
        paths.add(path);
      }
      if (operation.node() != null) {
        if (operation.node().isArray()) {
          collectAttributes(path, paths, (ArrayNode)operation.node());
        }
        else if (operation.node().isObject()) {
          collectAttributes(path, paths, (ObjectNode)operation.node());
        }
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
      final Path                        path = parentPath.attribute(field.getKey());
      if (path.size() > 1 || path.namespace() == null) {
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
}