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

    File        :   ResourceTypeEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypeEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.scim.spi.v2;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Application;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.ForbiddenException;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.SchemaFilterEvaluator;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.response.Preparer;
import oracle.iam.platform.scim.response.SearchResponse;

import oracle.iam.platform.scim.annotation.ResourceType;

import oracle.iam.platform.scim.v2.ResourceTypeResource;

import oracle.iam.identity.scim.api.ResourceService;

import oracle.iam.identity.scim.api.v2.ResourceTypeService;
import oracle.iam.platform.scim.v2.SchemaResource;

////////////////////////////////////////////////////////////////////////////////
// class ResourceTypeEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A JAX-RS servive provider implementation servicing the SCIM Resource Types
 ** endpoint.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Consumes(ResourceContext.MEDIA_TYPE)
@Produces(ResourceContext.MEDIA_TYPE)
@Path(ResourceContext.ENDPOINT_RESOURCE_TYPES)
@ResourceType(name=ResourceContext.RESOURCE_TYPE_RESOURCE, description="SCIM 2.0 Resource Type Endpoint (https://tools.ietf.org/html/rfc7643#section-8.6)", schema=ResourceTypeResource.class)
public class ResourceTypeEndpoint implements ResourceTypeService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition TYPE = ResourceTypeDefinition.of(ResourceTypeEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Context
  private Application application;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceTypeEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceTypeEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (ResourceTypeService)
  /**
   ** REST service search request for SCIM resources using GET (see section
   ** 3.4.2 of RFC 7644).
   ** <p>
   ** An HTTP GET to this endpoint is used to discover the types of resources
   ** available on a SCIM Service Provider (e.g., Users and Groups).
   ** <br>
   ** Each resource type defines the endpoints, the core schema URI that defines
   ** the resource, and any supported schema extensions.
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation contain
   **                            a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.SearchResponse SearchResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM schema resources).
   **
   ** @throws ProcessingException if the request fails.
   */
  @GET
  @Override
  @SuppressWarnings("unchecked")
  public SearchResponse search(final @Context UriInfo context)
    throws ProcessingException {

    final SearchControl control = SearchControl.build(TYPE, context);
    // https://tools.ietf.org/html/draft-ietf-scim-api-19#section-4 says
    // if a "filter" is provided, the service provider SHOULD respond with HTTP
    // Status 403 (FORBIDDEN) to ensure clients cannot incorrectly assume any
    // matching conditions specified in a filter are true.
    if(control.filter() != null)
      throw ForbiddenException.notPermitted("Filtering not allowed");

    final Collection<ResourceTypeResource>  type    = discover();
    final List<Generic>                     result  = new ArrayList<Generic>(type.size());
    // https://tools.ietf.org/html/draft-ietf-scim-api-19#section-4 says
    // query params should be ignored for discovery endpoints so we can't use
    // SearchResponse
    final Preparer<Generic>                 prepare = Preparer.<Generic>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
    for (ResourceTypeResource cursor : type) {
      result.add(prepare.locate(cursor.generic()));
    }
    return SearchResponse.<SchemaResource>build(TYPE, Long.valueOf(result.size()), control, ResourceService.location(TYPE, context)).addAll(result);
//    return new ListResponse<Generic>(result.size(), 1, result.size(), result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain SCIM {@link Generic} by its
   ** <code>id</code> using GET (as per section 3.4.1 of RFC 7644).
   **
   ** @param  id                 the identifier of the {@link Generic} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Generic} mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   */
  @GET
  @Override
  @Path(ResourceService.IDENTIFIER)
  @SuppressWarnings("unchecked")
  public Generic lookup(final @PathParam(Resource.IDENTIFIER) String id, final @Context UriInfo context)
    throws ProcessingException {

    final Filter                filter    = Filter.or(Filter.eq("id", id), Filter.eq("name", id));
    final SchemaFilterEvaluator evaluator = new SchemaFilterEvaluator(TYPE);
    final SearchControl         control   = SearchControl.build(TYPE, context);
    final Preparer<Generic>     preparer  = Preparer.<Generic>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
    for (ResourceTypeResource cursor : discover()) {
      final Generic resource = cursor.generic();
      if (filter.accept(evaluator, resource.objectNode())) {
        preparer.locate(resource);
        return resource;
      }
    }
    throw NotFoundException.of(ResourceContext.RESOURCE_TYPE_RESOURCE, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   discover
  /**
   ** Retrieve all resource types defined at the service provider.
   ** <br>
   ** The default implementation will generate {@link ResourceType} definitions
   ** from all JAX-RS resource classes with the {@link ResourceType} annotation.
   **
   ** @return                    All resource types defined at the service
   **                            provider.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link ResourceTypeResource}.
   */
  private Collection<ResourceTypeResource> discover() {
    final Set<ResourceTypeResource> collector = new HashSet<ResourceTypeResource>();
    for (Class<?> cursor : this.application.getClasses()) {
      final ResourceTypeDefinition definition = ResourceTypeDefinition.of(cursor);
      if (definition != null && definition.discoverable()) {
        collector.add(definition.resource());
      }
    }
    for (Object resourceInstance : application.getSingletons()) {
      ResourceTypeDefinition resourceTypeDefinition = ResourceTypeDefinition.of(resourceInstance.getClass());
      if (resourceTypeDefinition != null && resourceTypeDefinition.discoverable()) {
        collector.add(resourceTypeDefinition.resource());
      }
    }
    return collector;
  }
}