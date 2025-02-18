/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Provisioning

    File        :   TenantEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TenantEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.spi.v2;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import javax.ejb.EJB;

import oracle.hst.platform.rest.PATCH;
import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ResourceConflictException;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.identity.scim.api.ResourceService;

import oracle.iam.platform.scim.annotation.ResourceType;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.request.PatchRequest;
import oracle.iam.platform.scim.request.SearchRequest;
import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.response.Preparer;
import oracle.iam.platform.scim.response.SearchResponse;

import bka.iam.identity.igs.model.Tenant;

import bka.iam.identity.igs.api.UserFacade;
import bka.iam.identity.igs.api.TenantFacade;

import bka.iam.identity.scim.dto.TenantSchema;
import bka.iam.identity.scim.dto.TenantConverter;
import bka.iam.identity.scim.dto.TenantMergeFilter;

import bka.iam.identity.scim.v2.TenantResource;

////////////////////////////////////////////////////////////////////////////////
// class TenantEndpoint
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /Tenants endpoint.
 ** <br>
 ** Methods here are intercepted and/or decorated.
 ** <p>
 ** <a name="emit"></a>
 ** <br>
 ** <b>Notes:</b>
 ** <br>
 ** The parameters <i>emit</i> and <i>omit</i> found in methods of this
 ** implementation are aimed at specifying the "attributes" and
 ** "excludedAttributes" query params regarded in section 3.9 of SCIM spec
 ** protocol document (RFC 7644).
 */
@Path(TenantEndpoint.ENDPOINT)
@Produces({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@Consumes({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@ResourceType(name=TenantResource.NAME, description="SCIM 2.0 Tenant Endpoint (https://www.rfc-editor.org/rfc/rfc7644#section-3.2)", schema=TenantResource.class, discoverable=true)
public class TenantEndpoint extends AbstractEndpont<TenantResource> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final  String                 ENDPOINT = "Tenants";

  private static final ResourceTypeDefinition TYPE     = ResourceTypeDefinition.of(TenantEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  UserFacade   user;
  @EJB
  TenantFacade tenant;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TenantEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TenantEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (ResourceService)
  /**
   ** REST service search request for {@link TenantResource} resources using GET
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.2">section 3.4.2 of RFC 7644</a>)
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.ListResponse ListResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM resources).
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   **
   ** @throws ProcessingException  if the conversion of the search result
   **                              into a search response fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @Override
  @RolesAllowed("administrator")
  public SearchResponse search(final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    return search(SearchControl.build(TYPE, context), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (ResourceService)
  /**
   ** REST service search request for SCIM {@link TenantResource} using POST
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.3">section 3.4.3 of RFC 7644</a>)
   **
   ** @param  request            An object containing the parameters for the
   **                            query to execute.
   **                            <br>
   **                            These are the same parameters passed in the URL
   **                            for searches, for example in
   **                            {@link #search(UriInfo)}
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.ListResponse ListResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM resources).
   **
   ** @throws ProcessingException  if the conversion of the search result
   **                              into a search response fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @POST
  @Override
  @Path(".search")
  @RolesAllowed("administrator")
  public SearchResponse search(final SearchRequest request, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    return search(SearchControl.build(TYPE, request), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup (ResourceService)
  /**
   ** REST service request to retrieve a certain {@link TenantResource} by its
   ** <code>id</code> using GET (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.1">section 3.4.1 of RFC 7644</a>)
   **
   ** @param  id                 the identifier of the {@link Resource} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Generic} mapped at <code>id</code>
   **                            at the persistence layer.
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException if the request fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @Override
  @RolesAllowed("administrator")
  @Path(ResourceService.IDENTIFIER)
  public Generic lookup(final @PathParam(Resource.IDENTIFIER) String id, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    return preparer(context).retrieved(TenantConverter.convert(lookup(id)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (ResourceService)
  /**
   ** REST service request to create a certain SCIM {@link TenantResource} using
   ** POST  (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.3">section 3.3 of RFC 7644</a>)
   **
   ** @param  resource           an object that represents the SCIM resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 201 (created) and a
   **                            {@link Generic} in the entity body (the
   **                            resource just created).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws BadRequestException       if the value required are not provided
   **                                   by the payload.
   ** @throws PersistenceException      if the entity manager has been closed or
   **                                   the criteria query is found to be
   **                                   invalid.
   ** @throws ResourceConflictException if an entity is mapped with identifier
   **                                   <code>TenantResource.id</code> at the
   **                                   Service Provider.
   */
  @POST
  @Override
  @RolesAllowed("administrator")
  public Generic create(final TenantResource resource, final @Context UriInfo context)
    throws BadRequestException
    ,      ResourceConflictException
    ,      PersistenceException {

    // prevent bogus input
    if (resource.id() == null)
      throw requiredValue(TenantSchema.Attribute.ID.outbound);

    // prevent bogus input
    if (resource.displayName() == null)
      throw requiredValue(TenantSchema.Attribute.NAME.outbound);

    if (this.tenant.lookup(resource.id()) != null)
      throw conflict(Tenant.NAME, resource.id());

    final Tenant result = this.tenant.create(TenantConverter.convert(resource));
    return preparer(context).created(TenantConverter.convert(result), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace (ResourceService)
  /**
   ** REST service request to modify a certain SCIM {@link TenantResource}
   ** using PUT (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.1">section 3.5.1 of RFC 7644</a>).
   ** <p>
   ** This operation is not suitable to delete/remove/nullify attributes. For
   ** this purpose you can use the PATCH operation instead. PUT is intended to
   ** do replacements using the (not-null) values supplied in the
   ** <code>resource</code> argument.
   ** <p>
   ** To learn more about how modify works, read the replacement rules found at
   ** {@link TenantMergeFilter#merge(TenantResource)}.
   **
   ** @param  id                 the identifier of the {@link TenantResource}
   **                            to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           an object that contains the data to update on a
   **                            destination resource.
   **                            <br>
   **                            There is no need to supply a full resource,
   **                            just provide one with the attributes which are
   **                            intended to be replaced in the destination.
   **                            <br>
   **                            Allowed object is {@link TenantResource}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link Generic} in the entity body (the
   **                            resource after the modify took place).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException  if the request fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @PUT
  @Override
  @RolesAllowed("administrator")
  @Path(ResourceService.IDENTIFIER)
  public Generic replace(final @PathParam(Resource.IDENTIFIER) String id, final TenantResource resource, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    if (!StringUtility.equal(id, resource.id()))
      throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.RESOURCE_MATCH_IDENTIFIER, resource.id(), id));

    final Tenant origin = lookup(id);
    final Tenant entity = TenantMergeFilter.build(this.user, origin).merge(resource);
    if (origin.getVersion().equals(entity.getVersion())) {

    }
    final Tenant result = this.tenant.modify(entity);
    return preparer(context).replaced(TenantConverter.convert(result), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain SCIM {@link TenantResource}
   ** using PATCH (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>).
   **
   ** @param  id                 the identifier of the resource to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  request            a {@link PatchRequest} that contains the
   **                            operations to apply upon the resource being
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link PatchRequest}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 and a
   **                            {@link Generic} in the entity body (the
   **                            resource after the modify took place).
   **                            <br>
   **                            Possible object is {@link Generic}.
   **
   ** @throws ProcessingException  if the request fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @PATCH
  @Override
  @RolesAllowed("administrator")
  @Path(ResourceService.IDENTIFIER)
  public Generic modify(final @PathParam(Resource.IDENTIFIER) String id, final PatchRequest request, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    final TenantMergeFilter filter = TenantMergeFilter.build(this.user, lookup(id));
    for (PatchOperation operation : request) {
      filter.apply(operation);
    }
    final Tenant result = this.tenant.modify(filter.entity());
    return preparer(context).modified(TenantConverter.convert(result), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete (ResourceService)
  /**
   ** REST service request to remove a certain SCIM user resource by its
   ** <code>id</code> using PATCH (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.6">section 3.6 of RFC 7644</a>).
   **
   ** @param  id                 the identifier of the user resource to remove.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 204 (no content).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @DELETE
  @Override
  @RolesAllowed("administrator")
  @Path(ResourceService.IDENTIFIER)
  public Response delete(final @PathParam(Resource.IDENTIFIER) String id, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    this.tenant.delete(lookup(id));
    return Response.noContent().build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** JPA request to search for {@link User}s.
   **
   ** @param  control            the control structure of the search.
   **                            <br>
   **                            Allowed object is {@link SearchControl}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link oracle.iam.platform.scim.response.ListResponse ListResponse}
   **                            in the entity body (holding a collection of
   **                            SCIM resources).
   **                            <br>
   **                            Possible object is {@link SearchResponse}.
   **
   ** @throws ProcessingException  if the conversion of the search result
   **                              into a search response fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @SuppressWarnings("unchecked")
  private SearchResponse search(final SearchControl control, final UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    // whether the attribute to apply the search filter is permitted
    SearchFilter filter = null;
    if (control.filter() != null) {
      filter = translate(control.filter(), TenantSchema.instance);
    }
    // whether the attribute to apply the sort option is permitted
    SortOption option = null;
    if (control.sort() != null) {
      option = translate(control.sort(), control.order(), TenantSchema.instance);
    }

    final SearchResult<Tenant>       resultSet = this.tenant.list(control.start(), control.count(), Boolean.TRUE, filter, option);
    final Collection<TenantResource> resource  = TenantConverter.convertOutbound(resultSet);
    return SearchResponse.<TenantResource>build(TYPE, Long.valueOf(resultSet.total()), control, ResourceService.location(TYPE, context)).addAll(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link Tenant} by its identifier from
   ** the persistence layer.
   **
   ** @param  id                 the identifier of the {@link Tenant} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Tenant} mapped at <code>id</code> at
   **                            the persistence layer.
   **                            <br>
   **                            Possible object is {@link Tenant}.
   **
   ** @throws ProcessingException  if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider or the
   **                              request fails in general.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  private Tenant lookup(final String id)
    throws ProcessingException
    ,      PersistenceException {

    final Tenant entity = this.tenant.lookup(id);
    if (entity == null)
      throw NotFoundException.of(TenantResource.NAME, id);

    return entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preparer
  /**
   ** Factory method to create the {@link Preparer} required to generate an
   ** operation result.
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Preparer} approprite for an
   **                            operation.
   **                            <br>
   **                            Possible object is {@link Preparer} for type
   **                            {@link TenantResource}.
    **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> in the query
   **                             parameters of the request context is invalid.
   */
  @SuppressWarnings("unchecked")
  private Preparer<TenantResource> preparer(final @Context UriInfo context)
    throws BadRequestException {

    final SearchControl control = SearchControl.build(TYPE, context);
    return Preparer.<TenantResource>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
  }
}