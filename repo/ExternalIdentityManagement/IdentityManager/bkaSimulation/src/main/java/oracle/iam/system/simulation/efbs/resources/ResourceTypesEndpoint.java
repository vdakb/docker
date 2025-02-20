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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   eFBS SCIM Interface

    File        :   ResourceTypesEndpoint.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypesEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.resources;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.ForbiddenException;
import oracle.iam.system.simulation.BadRequestException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.scim.AbstractContext;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.domain.ListResponse;

import oracle.iam.system.simulation.scim.schema.Resource;
import oracle.iam.system.simulation.scim.schema.GenericResource;

import oracle.iam.system.simulation.scim.utility.SchemaFilterEvaluator;

import oracle.iam.system.simulation.scim.response.ResourcePreparer;

import oracle.iam.system.simulation.scim.v2.ResourceType;
import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

import oracle.iam.system.simulation.scim.v2.schema.ResourceTypeResource;

@Path("ResourceTypes")
@ResourceType(name="ResourceType", description="SCIM 2.0 Resource Type", schema=ResourceTypeResource.class, discoverable=false)
public class ResourceTypesEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition RESOURCE_TYPE_DEFINITION = ResourceTypeDefinition.fromJaxRsResource(ResourceTypesEndpoint.class);

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
   ** Constructs a <code>ResourceTypesEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceTypesEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Service SCIM request to retrieve a resource type by ID.
   **
   ** @param  id                 the ID of the resource type to retrieve.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uriInfo            the UriInfo of the request.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the retrieved resource type.
   **                            <br>
   **                            Possible object is {@link Resource}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  @GET
  @Path("{id}")
  @Produces({AbstractContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
  public Resource get(@PathParam("id") final String id, @Context final UriInfo uriInfo)
    throws ServiceException {

    final Filter                            filter     = Filter.or(Filter.eq("id", id), Filter.eq("name", id));
    final SchemaFilterEvaluator             evaluator  = new SchemaFilterEvaluator(RESOURCE_TYPE_DEFINITION);
    final ResourcePreparer<GenericResource> preperator = new ResourcePreparer<GenericResource>(RESOURCE_TYPE_DEFINITION, uriInfo);
    for (ResourceTypeResource cursor : types()) {
      final GenericResource resource = cursor.generic();
      if (filter.accept(evaluator, resource.objectNode())) {
        preperator.resourceTypeLocation(resource);
        return resource;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** Service SCIM request to retrieve all resource types defined at the service
   ** provider using GET.
   **
   ** @param  filter             the filter string used to request a subset of
   **                            resources.
   **                            <br>
   **                            Will throw 403 Forbidden if specified.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uriInfo            the UriInfo of the request.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    all resource types in a {@link ListResponse}
   **                            container.
   **                            <br>
   **                            Allowed object is {@link ListResponse} where
   **                            each element is of type {@link Resource}.
   **
   ** @throws ForbiddenException  if a filter is specified in the request.
   ** @throws BadRequestException if an attribute path specified by attributes
   **                             and excludedAttributes is invalid.
   */
  @Produces({AbstractContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
  public ListResponse<Resource> search(final @QueryParam(AbstractContext.QUERY_PARAMETER_FILTER) String filter, final @Context UriInfo uriInfo)
    throws ForbiddenException
    ,      BadRequestException {

    if (filter != null)
      throw ForbiddenException.notPermitted(ServiceBundle.string(ServiceError.PARAMETER_FILTER_NOTPERMITTED));

    // https://tools.ietf.org/html/draft-ietf-scim-api-19#section-4 says
    // query params should be ignored for discovery endpoints so we can't use
    // SimpleSearchResults.
    final ResourcePreparer<Resource> preparer = new ResourcePreparer<Resource>(RESOURCE_TYPE_DEFINITION, uriInfo);
    final Collection<Resource>       prepared = new ArrayList<Resource>();
    for(ResourceTypeResource cursor : types()) {
      Resource temporary = cursor.generic();
      preparer.resourceTypeLocation(cursor);
      prepared.add(temporary);
    }
    return new ListResponse<Resource>(prepared);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   types
  /**
   ** Retrieve all resource types defined at the service provider.
   ** <br>
   ** The default implementation will generate {@link ResourceType} definitions
   ** from all JAX-RS resource classes with the {@link ResourceType} annotation.
   **
   ** @return                    all resource types defined at the service
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link Collection} where
   **                            each element is of type
   **                            {@link ResourceTypeResource}.
   */
  public Collection<ResourceTypeResource> types() {
    final Set<ResourceTypeResource> resourceTypes = new HashSet<ResourceTypeResource>();
    for (Class<?> resourceClass : this.application.getClasses()) {
      ResourceTypeDefinition definition = ResourceTypeDefinition.fromJaxRsResource(resourceClass);
      if (definition != null && definition.discoverable()) {
        resourceTypes.add(definition.toScimResource());
      }
    }

    for (Object resourceInstance : this.application.getSingletons()) {
      ResourceTypeDefinition definition = ResourceTypeDefinition.fromJaxRsResource(resourceInstance.getClass());
      if (definition != null && definition.discoverable()) {
        resourceTypes.add(definition.toScimResource());
      }
    }
    return resourceTypes;
  }
}