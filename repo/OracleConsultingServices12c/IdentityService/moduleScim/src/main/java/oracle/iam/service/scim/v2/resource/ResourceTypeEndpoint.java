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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Generic SCIM Service

    File        :   ResourceTypeEndpoint.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ResourceTypeEndpoint.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.service.scim.v2.resource;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;
import oracle.hst.platform.rest.ServiceException;
import oracle.hst.platform.rest.ForbiddenException;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.schema.Generic;

import oracle.iam.platform.scim.response.Preparer;
import oracle.iam.platform.scim.response.ListResponse;

import oracle.iam.platform.scim.v2.ResourceTypeResource;

import oracle.iam.identity.scim.api.ResourceService;

////////////////////////////////////////////////////////////////////////////////
// class ResourceTypeEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** A handler which is invoked to process SCIM resource type exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Produces(ResourceContext.MEDIA_TYPE)
@Consumes(ResourceContext.MEDIA_TYPE)
@Path(ResourceContext.ENDPOINT_RESOURCE_TYPES)
public class ResourceTypeEndpoint extends AbstractEndpoint<ResourceTypeEndpoint> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition TYPE = ResourceTypeDefinition.of(ResourceTypeEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ResourceTypeEndpoint</code> service endpoint that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ResourceTypeEndpoint() {
    // ensure inheritance
    super(ResourceTypeEndpoint.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** SCIM Service request to retrieve all resource types defined at the Service
   ** Provider using GET.
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   ** 
   ** @return                    all resource types in a {@link ListResponse}
   **                            container.
   **                            <br>
   **                            Possible object is {@link ListResponse}.
   **
   ** @throws ServiceException   If an error occurs.
   */
  @GET
  @Produces({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
  public ListResponse<Generic> search(final @Context UriInfo context)
    throws ServiceException {

    final SearchControl control = SearchControl.build(TYPE, context);
    if (control.filter() != null)
      throw ForbiddenException.notPermitted(ServiceBundle.string(ServiceError.PARAMETER_FILTER_NOTPERMITTED));

    // https://tools.ietf.org/html/draft-ietf-scim-api-19#section-4 says
    // query params should be ignored for discovery endpoints so we can't use
    // SimpleSearchResults.
    final Collection<ResourceTypeResource> types    = types();
    final Collection<Generic>              prepared = new ArrayList<Generic>(types.size());
    final Preparer<Generic>                preparer = Preparer.<Generic>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
    for (ResourceTypeResource cursor : types) {
      final Generic resource = cursor.generic();
      preparer.locate(resource);
      prepared.add(resource);
    }
    return new ListResponse<Generic>(prepared);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   types
  /**
   ** Retrieve all resource types defined at the service provider.
   ** <p>
   **  implementation will generate ResourceType definitions from all JAX-RS
   ** resource classes with the ResourceType annotation.
   **
   ** @return                    all resource types defined at the service
   **                            provider.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link ResourceTypeResource}.
   **
   ** @throws ServiceException   if an error occurs.
   */
  protected Collection<ResourceTypeResource> types()
    throws ServiceException {

    final Set<ResourceTypeResource> types = new HashSet<ResourceTypeResource>();
    for (Class<?> resource : this.application.getClasses()) {
      final ResourceTypeDefinition definition = ResourceTypeDefinition.of(resource);
      if (definition != null && definition.discoverable()) {
        types.add(definition.resource());
      }
    }

    for (Object instance : this.application.getSingletons()) {
      final ResourceTypeDefinition definition = ResourceTypeDefinition.of(instance.getClass());
      if (definition != null && definition.discoverable()) {
        types.add(definition.resource());
      }
    }
    return types;
  }
}