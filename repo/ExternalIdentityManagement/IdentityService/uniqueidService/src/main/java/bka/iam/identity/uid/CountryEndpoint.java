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
    Subsystem   :   Unique Identifier Service

    File        :   CountryEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CountryEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;

import javax.json.JsonObject;

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

import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.ForbiddenException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ResourceConflictException;

import bka.iam.identity.igs.api.Endpoint;

import bka.iam.identity.uid.api.CountryFacade;

import bka.iam.identity.uid.model.Country;

import bka.iam.identity.uid.marshal.Schema;

////////////////////////////////////////////////////////////////////////////////
// class CountryEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The REST service {@link Endpoint} for {@link Country} transactions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(CountryEndpoint.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CountryEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path of the resource endpoint */
  public static final String PATH = "country";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  CountryFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CountryEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CountryEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Service REST request to retrieve all {@link Country}s paginated by the
   ** specified parameters.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the collection of {@link Country}s.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws BadRequestException  if the value for parameter
   **                              <code>startIndex</code> or <code>count</code>
   **                              isn't evaluated as a integer number.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @RolesAllowed("viewer")
  public Response list(final @Context UriInfo context)
    throws BadRequestException
   ,       PersistenceException {

    final SearchRequest         request  = searchRequest(context);
    final SearchResult<Country> result   = this.facade.list(request);
    // JPA's start index is 0-base but the REST API operates with an 1-based
    // index
    final Schema                trimmer  = Schema.build(resourceTrimmer(context));
    final JsonObject            response = Schema.country(trimmer, request.start() + 1, result);
    return Response.status(Response.Status.OK).entity(response).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain {@link Country} by its
   ** <code>id</code>.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  id                 the identifier of the {@link Country} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Country} mapped at <code>id</code>
   **                            wrapped in a {@link Response} or the
   **                            appropriate error message about not found.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException    if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for a country identifier have been violated.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @Path("{id}")
  @RolesAllowed("viewer")
  public Response lookup(final @PathParam("id") String id, final @Context UriInfo context)
    throws NotFoundException
    ,      BadRequestException
    ,      PersistenceException {

    final Country subject = this.facade.lookup(id);
    if (subject == null)
      throw notFound(Country.NAME, id);

    final Schema trimmer = Schema.build(resourceTrimmer(context));
    return Response.status(Response.Status.OK).entity(trimmer.convert(subject)).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service request to create a certain {@link Country} using POST.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  resource           an object that represents the resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 (created) and a
   **                            {@link Response} in the entity body
   **                            (the composed identifier just created).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws BadRequestException       when either the syntactic or semantic
   **                                   rules for a country identifier have been
   **                                   violated.
   ** @throws PersistenceException      if the entity manager has been closed or
   **                                   the criteria query is found to be
   **                                   invalid.
   ** @throws ResourceConflictException if an entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider.
   */
  @POST
  @RolesAllowed("administrator")
  public Response create(final JsonObject resource, final @Context UriInfo context)
    throws BadRequestException
    ,      PersistenceException
    ,      ResourceConflictException {

    final Country entity = Schema.country(resource);
    // prevent bogus input
    SurrogateSchema.validate(entity);

    if (this.facade.lookup(entity.getId()) != null)
      throw conflict(Country.NAME, entity.getId());

    final Schema trimmer = Schema.build(resourceTrimmer(context));
    // merge the resource into the entity found and persist the changes
    return Response
      .status(Response.Status.OK)
      .entity(
        trimmer.convert(this.facade.create(entity))
       )
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain {@link Country} using PUT.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  id                 the identifier of the {@link Country} to
   **                            modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           an object that represents the resource to
   **                            modify.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 (created) and a
   **                            {@link Response} in the entity body
   **                            (the composed identifier just created).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException    if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for a country identifier have been violated.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @PUT
  @Path("{id}")
  @RolesAllowed("administrator")
  public Response modify(final @PathParam("id") String id, final JsonObject resource, final @Context UriInfo context)
    throws NotFoundException
    ,      BadRequestException
    ,      PersistenceException {

    final Country entity = this.facade.lookup(id);
    if (entity == null)
      throw notFound(Country.NAME, id);

    final Schema trimmer = Schema.build(resourceTrimmer(context));
    // merge the resource into the entity found and persist the changes
    return Response
      .status(Response.Status.OK)
      .entity(
         trimmer.convert(
           this.facade.modify(
             Schema.country(entity, resource)
           )
         )
       )
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service request to delete a certain {@link Country} using DELETE.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  id                 the identifier of the {@link Country} to
   **                            delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 (created) and a
   **                            {@link Response} in the entity body
   **                            (the composed identifier just created).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException    if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for a country identifier have been violated.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @DELETE
  @Path("{id}")
  @RolesAllowed("administrator")
  public Response delete(final @PathParam("id") String id)
    throws NotFoundException
    ,      BadRequestException
    ,      PersistenceException {

    final Country entity = this.facade.lookup(id);
    if (entity == null)
      throw notFound(Country.NAME, id);
    if (!entity.getActive())
      throw mutability();

    // set the state of the entity to inactive (active = false) ...
    entity.setActive(false);
    // ... and persist the change
    this.facade.modify(entity);
    return Response.status(Response.Status.OK).entity(entity).build();
  }
}