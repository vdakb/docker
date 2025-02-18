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

    File        :   UserEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.scim.spi.v2;

import java.util.Collection;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.core.utility.StringUtility;

import oracle.hst.platform.jpa.SortOption;
import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.rest.PATCH;
import oracle.hst.platform.rest.ServiceError;
import oracle.hst.platform.rest.ServiceBundle;

import oracle.iam.platform.scim.SearchControl;
import oracle.iam.platform.scim.ResourceContext;
import oracle.iam.platform.scim.NotFoundException;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.schema.Generic;
import oracle.iam.platform.scim.schema.Resource;

import oracle.iam.platform.scim.annotation.ResourceType;

import oracle.iam.platform.scim.v2.UserResource;
import oracle.iam.platform.scim.v2.EnterpriseUserResource;

import oracle.iam.platform.scim.request.PatchRequest;
import oracle.iam.platform.scim.request.SearchRequest;
import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.response.Preparer;
import oracle.iam.platform.scim.response.SearchResponse;

import oracle.iam.identity.scim.api.ResourceService;

import bka.iam.identity.igs.model.User;

import bka.iam.identity.igs.api.UserFacade;

import bka.iam.identity.scim.dto.UserSchema;
import bka.iam.identity.scim.dto.UserConverter;
import bka.iam.identity.scim.dto.UserMergeFilter;

import bka.iam.identity.scim.v2.ClaimResource;

////////////////////////////////////////////////////////////////////////////////
// class UserEndpoint
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /Users endpoint.
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
 ** <p>
 ** Every SCIM service operation that returns resources (e.g Users, Groups,
 ** etc.) offers the possibility to specify which attributes can be included for
 ** every resource part of the response. The default behavior is returning those
 ** attributes that according to the resource Schema have returnability =
 ** "always" in addition to those with returnability = "default".
 ** <p>
 ** <i><b>emit</b></i> is used to override the default attribute set, so
 ** when supplying a not-null or not empty String, the attributes included in
 ** the resource(s) of the response will be those with returnability = "always"
 ** as well as those specified by <i>emit</i>.
 ** <p>
 ** This parameter consists of  a comma-separated list of attribute names. An
 ** example of a valid value for <i>emit</i> when the resource of interest
 ** is User, could be: <code>userName, active, name.familyName, addresses,
 ** emails.value, emails.type, urn:ietf:params:scim:schemas:extension:hcm:2.0:User:myCustomAttribute</code>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Attributes marked with returnability = "never" (such as a User password)
 ** will always be filtered out from the output, so including such attributes in
 ** <i>emit</i> has no effect.
 ** <p>
 ** <i><b>omit</b></i> is used to specify the set of attributes that should be
 ** excluded from the default attribute set. In this sense, the resources found
 ** in the response will include the attributes whose returnability = "always"
 ** in addition to those with returnability = "default" except for those
 ** included in <i>omit</i>. As with <i>emit</i>, this parameter must be in the
 ** form of a comma-separated list of attribute names.
 ** <p>
 ** <i>emit</i> and <i>omit</i> are mutually exclusive: if both are provided
 ** only <i>emit</i> will be taken into account to compute the output attribute
 ** set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(ResourceContext.ENDPOINT_USERS)
@Produces({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@Consumes({ResourceContext.MEDIA_TYPE, MediaType.APPLICATION_JSON})
@ResourceType(name=ResourceContext.RESOURCE_TYPE_USER, description="SCIM 2.0 User Endpoint (https://www.rfc-editor.org/rfc/rfc7644#section-3.2)", schema=UserResource.class, required={EnterpriseUserResource.class, ClaimResource.class})
public class UserEndpoint extends AbstractEndpont<UserResource> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ResourceTypeDefinition TYPE = ResourceTypeDefinition.of(UserEndpoint.class);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  UserFacade  facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search (ResourceService)
  /**
   ** REST service search request for User resources using GET (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.2">section 3.4.2 of RFC 7644</a>)
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
   ** REST service search request for SCIM user resources using POST (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.3">section 3.4.3 of RFC 7644</a>)
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
   ** REST service request to retrieve a certain {@link UserResource} by its
   ** <code>id</code> using GET (as per
   ** <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.4.1">section 3.4.1 of RFC 7644</a>)
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
   ** @throws ProcessingException  if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider or the
   **                              request fails in general.
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

    return preparer(context).retrieved(UserConverter.convert(lookup(id)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create (ResourceService)
  /**
   ** REST service request to create a certain SCIM {@link UserResource} using
   ** POST (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.3">section 3.3 of RFC 7644</a>)
   **
   ** @param  resource           an object that represents the SCIM resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
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
   ** @throws ProcessingException  if the request fails.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @POST
  @Override
  @RolesAllowed("administrator")
  public Generic create(final UserResource resource, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    // prevent bogus input
    if (resource.userName() == null)
      throw requiredValue(UserSchema.Attribute.USERNAME.outbound);

    // RFC 7644 3.3. Creating Resources (1st bullet)
    // In the request body, attributes whose mutability is "readOnly" (see
    // Sections 2.2 and 7 of [RFC7643]) SHALL be ignored.
    if (resource.id() != null) {
      resource.id(null);
    }

    if (this.facade.lookup(resource.userName()) != null)
      throw conflict(User.NAME, resource.userName());

    final User result = this.facade.create(UserConverter.convert(resource));
    return preparer(context).created(UserConverter.convert(result), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace (ResourceService)
  /**
   ** REST service request to modify a certain SCIM {@link UserResource} using
   ** PUT (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.1">section 3.5.1 of RFC 7644</a>).
   ** <p>
   ** This operation is not suitable to delete/remove/nullify attributes. For
   ** this purpose you can use the PATCH operation instead. PUT is intended to
   ** do replacements using the (not-null) values supplied in the
   ** <code>resource</code> argument.
   ** <p>
   ** To learn more about how modify works, read the replacement rules found at
   ** {@link UserMergeFilter#merge(UserResource)}.
   **
   ** @param  id                 the identifier of the {@link UserResource}
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
   **                            Allowed object is {@link UserResource}.
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
  public Generic replace(final @PathParam(Resource.IDENTIFIER) String id, final UserResource resource, final @Context UriInfo context)
    throws ProcessingException
    ,      PersistenceException {

    if (!StringUtility.equal(id, resource.id()))
      throw BadRequestException.invalidPath(ServiceBundle.string(ServiceError.RESOURCE_MATCH_IDENTIFIER, resource.id(), id));

    final User origin = lookup(id);
    final User entity = UserMergeFilter.build(origin).merge(resource);
    if (origin.getVersion().equals(entity.getVersion())) {

    }
    final User result = this.facade.modify(entity);
    return preparer(context).replaced(UserConverter.convert(result), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain SCIM {@link UserResource}
   ** using PATCH
   ** (as per <a href="https://datatracker.ietf.org/doc/html/rfc7644#section-3.5.2">section 3.5.2 of RFC 7644</a>).
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

    final UserMergeFilter filter = UserMergeFilter.build(lookup(id));
    for (PatchOperation operation : request) {
      filter.apply(operation);
    }
    final User result = this.facade.modify(filter.entity());
    return preparer(context).modified(UserConverter.convert(result), null);
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
   ** @throws ProcessingException  if the request fails.
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

    this.facade.delete(lookup(id));
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
      // convert the SCIM filter in a JPA equivalent
      filter = translate(control.filter(), UserSchema.instance);
    }
    // whether the attribute to apply the sort option is permitted
    SortOption option = null;
    if (control.sort() != null) {
      // convert the SCIM sort control in a JPA equivalent
      option = translate(control.sort(), control.order(), UserSchema.instance);
    }

    final SearchResult<User>       resultSet = this.facade.list(control.start(), control.count(), Boolean.TRUE, filter, option);
    final Collection<UserResource> resource  = UserConverter.convertOutbound(resultSet);
    return SearchResponse.<UserResource>build(TYPE, Long.valueOf(resultSet.total()), control, ResourceService.location(TYPE, context)).addAll(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link User} by its identifier from the
   ** persistence layer.
   **
   ** @param  id                 the identifier of the {@link User} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link User} mapped at <code>id</code> at
   **                            the persistence layer.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException  if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider or the
   **                              request fails in general.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  private User lookup(final String id)
    throws ProcessingException
    ,      PersistenceException {

    return lookup(Long.valueOf(id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link User} by its identifier from the
   ** persistence layer.
   **
   ** @param  id                 the identifier of the {@link User} to lookup.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the {@link User} mapped at <code>id</code> at
   **                            the persistence layer.
   **                            <br>
   **                            Possible object is {@link User}.
   **
   ** @throws ProcessingException  if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider or the
   **                              request fails in general.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  private User lookup(final Long id)
    throws ProcessingException
    ,      PersistenceException {

    final User entity = this.facade.lookup(id);
    if (entity == null)
      throw NotFoundException.of(ResourceContext.RESOURCE_TYPE_USER, id);

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
   **                            {@link UserResource}.
    **
   ** @throws BadRequestException if an attribute path specified by
   **                             <code>attributes</code> and
   **                             <code>excludedAttributes</code> in the query
   **                             parameters of the request context is invalid.
   */
  @SuppressWarnings("unchecked")
  private Preparer<UserResource> preparer(final @Context UriInfo context)
    throws BadRequestException {

    final SearchControl control = SearchControl.build(TYPE, context);
    return Preparer.<UserResource>build(TYPE, control.attribute(), control.exclude(), ResourceService.location(TYPE, context));
  }
}