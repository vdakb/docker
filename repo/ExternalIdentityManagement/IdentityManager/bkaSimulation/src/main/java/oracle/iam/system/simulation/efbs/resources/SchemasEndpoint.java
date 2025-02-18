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

    File        :   SchemasEndpoint.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemasEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.resources;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;

import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.NotFoundException;

import oracle.iam.system.simulation.scim.AbstractContext;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.schema.Resource;

import oracle.iam.system.simulation.scim.v2.ResourceType;
import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

import oracle.iam.system.simulation.scim.v2.schema.SchemaResource;

import oracle.iam.system.simulation.scim.response.ResourcePreparer;

import oracle.iam.system.simulation.scim.schema.GenericResource;
import oracle.iam.system.simulation.scim.utility.SchemaFilterEvaluator;

@Path("Schemas")
@ResourceType(name="Schema", description="SCIM 2.0 Schema", schema=SchemaResource.class, discoverable=false)
public class SchemasEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition RESOURCE_TYPE_DEFINITION = ResourceTypeDefinition.fromJaxRsResource(SchemasEndpoint.class);

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
   ** Constructs a <code>SchemasEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SchemasEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Service SCIM request to retrieve a schema by ID.
   **
   ** @param  id                 the ID of the schema to retrieve.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uriInfo            the UriInfo of the request.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the retrieved schema.
   **                            <br>
   **                            Possible object is {@link Resource}.
   **
   ** @throws ServiceException   if the request fails.
   */
  @GET
  @Path("{id}")
  @Produces({AbstractContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
  public Resource get(@PathParam("id") final String id, @Context final UriInfo uriInfo)
    throws ServiceException {

    Filter                     filter    = Filter.or(Filter.eq("id", id), Filter.eq("name", id));
    SchemaFilterEvaluator      evaluator = new SchemaFilterEvaluator(RESOURCE_TYPE_DEFINITION);
    ResourcePreparer<Resource> preparer  = new ResourcePreparer<Resource>(RESOURCE_TYPE_DEFINITION, uriInfo);
    for (SchemaResource schema : schemas()) {
      final GenericResource generic = schema.generic();
      if (filter.accept(evaluator, generic.objectNode())) {
        preparer.resourceTypeLocation(generic);
        return schema;
      }
    }
    throw new NotFoundException("No schema defined with ID " + id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemas
  /**
   ** Returns all schemas defined at the service provider.
   ** <br>
   ** The default implementation will generate Schemas definitions based on the
   ** {@link ResourceType} of all JAX-RS resource classes with the
   ** {@link ResourceType} annotation.
   **
   ** @return                    all resource types defined at the service
   **                            provider.
   **                            <br>
   **                            Allowed object is {@link Collection} where
   **                            each element is of type
   **                            {@link SchemaResource}.
   */
  public Collection<SchemaResource> schemas() {
    Set<SchemaResource> schemas = new HashSet<SchemaResource>();
    for (Class<?> resourceClass : this.application.getClasses()) {
      ResourceTypeDefinition resourceTypeDefinition = ResourceTypeDefinition.fromJaxRsResource(resourceClass);
      if (resourceTypeDefinition != null && resourceTypeDefinition.discoverable()) {
        if (resourceTypeDefinition.core() != null) {
          schemas.add(resourceTypeDefinition.core());
        }
        for (SchemaResource schemaExtension : resourceTypeDefinition.extensions().keySet()) {
          schemas.add(schemaExtension);
        }
      }
    }
    for (Object resourceInstance : this.application.getSingletons()) {
      ResourceTypeDefinition resourceTypeDefinition = ResourceTypeDefinition.fromJaxRsResource(resourceInstance.getClass());
      if (resourceTypeDefinition != null && resourceTypeDefinition.discoverable()) {
        if (resourceTypeDefinition.core() != null) {
          schemas.add(resourceTypeDefinition.core());
        }
        for (SchemaResource schemaExtension : resourceTypeDefinition.extensions().keySet()) {
          schemas.add(schemaExtension);
        }
      }
    }
    return schemas;
  }
}