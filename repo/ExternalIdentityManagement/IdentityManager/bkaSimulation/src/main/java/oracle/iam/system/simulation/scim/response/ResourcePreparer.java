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

    File        :   ResourcePreparer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourcePreparer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.response;

import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import java.net.URI;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.scim.Path;
import oracle.iam.system.simulation.scim.AbstractContext;

import oracle.iam.system.simulation.scim.domain.PatchOperation;

import oracle.iam.system.simulation.scim.schema.Metadata;
import oracle.iam.system.simulation.scim.schema.Resource;
import oracle.iam.system.simulation.scim.schema.GenericResource;

import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

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
 **       resource as well as the attributes or excludedAttributes query parameter.
 **   <li>Setting the meta.resourceType and meta.location attributes if not
 **       already set.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ResourcePreparer<T extends Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final URI                    baseUri;
  private final ResourceTypeDefinition resourceType;
  private final Set<Path>              queryAttributes;
  private final boolean                excluded;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new <code>ResourcePreparer</code> for preparing returned
   ** resources for a SCIM operation.
   **
   ** @param  type               the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param  request            the UriInfo for the request.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @throws BadRequestException if an attribute path specified by attributes
   **                             and excludedAttributes is invalid.
   */
  public ResourcePreparer(final ResourceTypeDefinition type, final UriInfo request)
    throws BadRequestException {

    // ensure inheritance
    this(type, request.getQueryParameters().getFirst(AbstractContext.QUERY_PARAMETER_ATTRIBUTES), request.getQueryParameters().getFirst(AbstractContext.QUERY_PARAMETER_EXCLUDED), request.getBaseUriBuilder().path(type.endpoint()).buildFromMap(singleValuedMapFromMultivaluedMap(request.getPathParameters())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Private constructor used by unit-test.
   *
   ** @param  resourceType       the resource type definition for resources to
   **                            prepare.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   ** @param attributesString    the attributes query param.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param excluded            the excludedAttributes query param.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param baseUri             the resource type base URI.
   **                            <br>
   **                            Allowed object is {@link URI}.
   ** @throws BadRequestException if an attribute path specified by attributes
   **                             and excludedAttributes is invalid.
   */
  ResourcePreparer(final ResourceTypeDefinition resourceType, final String attributesString, final String excludedAttributesString, final URI baseUri)
    throws BadRequestException {

    // ensure inheritance
    super();

    // initialize instance attributes
    if (attributesString != null && !attributesString.isEmpty()) {
      Set<String> attributeSet = CollectionUtility.set(StringUtility.splitCSV(attributesString));
      this.queryAttributes = new LinkedHashSet<Path>(attributeSet.size());
      for (String attribute : attributeSet) {
        Path normalizedPath = resourceType.normalizePath(Path.from(attribute)).withoutFilters();
        this.queryAttributes.add(normalizedPath);
      }
      this.excluded = false;
    }
    else if (excludedAttributesString != null && !excludedAttributesString.isEmpty()) {
      Set<String> attributeSet = CollectionUtility.set(StringUtility.splitCSV(excludedAttributesString));
      this.queryAttributes = new LinkedHashSet<Path>(attributeSet.size());
      for (String attribute : attributeSet) {
        Path normalizedPath = resourceType.normalizePath(Path.from(attribute)).withoutFilters();
        this.queryAttributes.add(normalizedPath);
      }
      this.excluded = true;
    }
    else {
      this.excluded        = true;
      this.queryAttributes = Collections.emptySet();
    }
    this.baseUri      = baseUri;
    this.resourceType = resourceType;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceTypeLocation
  /**
   ** Sets the meta.resourceType and meta.location metadata attribute values.
   **
   ** @param  returned           the resource to set the attributes.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   */
  public void resourceTypeLocation(final T returned) {
    Metadata meta = returned.metadata();
    boolean metaUpdated = false;
    if (meta == null) {
      meta = new Metadata();
    }

    if (meta.resourceType() == null) {
      meta.resourceType(this.resourceType.name());
      metaUpdated = true;
    }

    if (meta.location() == null) {
      String id = returned.id();
      if (id != null) {
        UriBuilder locationBuilder = UriBuilder.fromUri(this.baseUri);
        locationBuilder.segment(id);
        meta.location(locationBuilder.build());
      }
      else {
        meta.location(this.baseUri);
      }
      metaUpdated = true;
    }

    if (metaUpdated) {
      returned.metadata(meta);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimRetrieved
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
   **                            Possible object is {@link GenericResource}.
   */
  public GenericResource trimRetrieved(final T returned) {
    return trim(returned, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimCreated
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
   **                            Possible object is {@link GenericResource}.
   */
  public GenericResource trimCreated(final T returned, final T request) {
    return trim(returned, request, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimReplaced
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
   **                            Possible object is {@link GenericResource}.
   */
  public GenericResource trimReplaced(final T returned, final T request) {
    return trim(returned, request, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimModified
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
   **                            Possible object is {@link GenericResource}.
   */
  public GenericResource trimModified( final T returned, final Iterable<PatchOperation> patch) {
    return trim(returned, null, patch);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   singleValuedMapFromMultivaluedMap
  private static Map<String, String> singleValuedMapFromMultivaluedMap(final MultivaluedMap<String, String> multivaluedMap) {
    final Map<String, String> returnMap = new LinkedHashMap<String, String>();
    for (String k : multivaluedMap.keySet()) {
      returnMap.put(k, multivaluedMap.getFirst(k));
    }
    return returnMap;
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
   **                            Possible object is {@link GenericResource}.
   */
  @SuppressWarnings("unchecked")
  private GenericResource trim(final T returned, final T request, final Iterable<PatchOperation> patchOperations) {
    Set<Path> requestAttributes = Collections.emptySet();
    if (request != null) {
      requestAttributes = new LinkedHashSet<Path>();
      collectAttributes(Path.build(), requestAttributes, request.generic().objectNode());
    }

    if (patchOperations != null) {
      requestAttributes = new LinkedHashSet<Path>();
      collectAttributes(requestAttributes, patchOperations);
    }
    resourceTypeLocation(returned);
    final GenericResource generic  = returned.generic();
    final ResourceTrimmer trimmer  = new ResourceTrimmer(this.resourceType, requestAttributes, this.queryAttributes, this.excluded);
    final GenericResource prepared = new GenericResource(trimmer.trimObjectNode(generic.objectNode()));
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
        path = this.resourceType.normalizePath(operation.path()).withoutFilters();
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