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

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;

import oracle.iam.system.simulation.ServiceException;
import oracle.iam.system.simulation.scim.AbstractContext;
import oracle.iam.system.simulation.scim.schema.Resource;

import oracle.iam.system.simulation.scim.v2.ResourceType;
import oracle.iam.system.simulation.scim.v2.ResourceTypeDefinition;

import oracle.iam.system.simulation.scim.v2.schema.ServiceProviderConfig;

// ServiceProvider 	
@Path("ServiceProvider")
@ResourceType(name="ServiceProvider", description="SCIM 2.0 ServiceProvider", schema=ServiceProviderConfig.class, discoverable=false)
public class ServiceProviderEndpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition RESOURCE_TYPE_DEFINITION = ResourceTypeDefinition.fromJaxRsResource(ServiceProviderEndpoint.class);

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
   ** Constructs a <code>ServiceProviderEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServiceProviderEndpoint() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Service SCIM request to retrieve a service provider configuration by ID.
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

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Service SCIM request to retrieve a service provider configuration by ID.
   **
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
   ** @return                    all resource types in a {@link Resource}
   **                            container.
   **                            <br>
   **                            Possible object is {@link Resource}.
   **
   ** @throws ServiceException   if the request fails.
   */
  @Produces({AbstractContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
  public Resource search(@QueryParam(AbstractContext.QUERY_PARAMETER_FILTER) final String filter, @Context final UriInfo uriInfo)
    throws ServiceException {

    return null;
  }
}