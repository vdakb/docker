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
    Subsystem   :   Anonymous Identifier Service

    File        :   IdentifierEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentifierEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid;

import javax.annotation.security.RolesAllowed;

import javax.json.JsonObject;

import javax.ejb.EJB;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.ForbiddenException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ResourceConflictException;

import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceException;

import bka.iam.identity.igs.api.Endpoint;

import bka.iam.identity.igs.spi.IdentifierGenerator;

import bka.iam.identity.pid.model.Identifier;

import bka.iam.identity.pid.api.IdentifierFacade;

import bka.iam.identity.pid.marshal.Schema;

////////////////////////////////////////////////////////////////////////////////
// class IdentifierEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The REST service {@link IdentifierEndpoint} for {@link Identifier}
 ** transactions.
 **
 ** @author dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since 1.0.0.0
 */
@Path(IdentifierEndpoint.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IdentifierEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path of the resource endpoint */
  public static final String PATH = "pid";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  IdentifierFacade           facade;

  final IdentifierGenerator  sequence = IdentifierGenerator.of(6);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentifierEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentifierEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Service REST request to retrieve all identifiers paginated by the
   ** specified parameters.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Response}.
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

    final SearchRequest            request  = searchRequest(context);
    final SearchResult<Identifier> result   = this.facade.list(request);
    // JPA's start index is 0-base but the REST API operates with an 1-based
    // index
    final Schema                trimmer  = Schema.build(resourceTrimmer(context));
    final JsonObject            response = Schema.identifier(trimmer, request.start() + 1, result);
    return Response.status(Response.Status.OK).entity(response).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain {@link Identifier} by its
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Identifier} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Identifier} mapped at
   **                            <code>id</code> wrapped in a {@link Response}
   **                            or the appropriate error message about not
   **                            found.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException    if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider.
   ** @throws BadRequestException  if the value for parameter
   **                              <code>startIndex</code> or <code>count</code>
   **                              isn't evaluated as a integer number.
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

    final Identifier subject = this.facade.lookup(id);
    if (subject == null)
      throw notFound(Identifier.NAME, id);

    final Schema trimmer = Schema.build(resourceTrimmer(context));
    return Response.status(Response.Status.OK).entity(trimmer.convert(subject)).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** Service REST request a randomly generate {@link Identifier} character
   ** sequence.
   **
   ** @param  prefix             the custom prefix to prepend at the identifier
   **                            generated.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  usedby             the required parameter of the identity for whom
   **                            the identifier needs to be generated.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Identifier} generated.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws NotFoundException         if no entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider.
   ** @throws BadRequestException       if the value for parameter
   **                                   <code>prefix</code> or
   **                                   <code>usedby</code> is empty.
   ** @throws PersistenceException      if the entity manager has been closed or
   **                                   the criteria query is found to be
   **                                   invalid.
   ** @throws ResourceConflictException if an entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider.
   */
  @POST
  @RolesAllowed("administrator")
  @Produces(MediaType.TEXT_PLAIN)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response generate(final @FormParam("prefix") String prefix, final @FormParam("usedby") String usedby)
    throws NotFoundException
    ,      BadRequestException
    ,      PersistenceException
    ,      ResourceConflictException {

    // prevent bogus input
    if (prefix == null || prefix.trim().isEmpty())
      throw BadRequestException.entityEmpty("prefix");

    // prevent bogus input
    if (usedby == null || usedby.trim().isEmpty())
      throw BadRequestException.entityEmpty("usedby");

    int        attempts = 3;
    Identifier subject  = null;
    do {
      final String id = String.format("%s%s", prefix, this.sequence.randomAlphaNumeric());
      subject = this.facade.lookup(id);
      if (subject == null) {
        subject = Identifier.build(id).setUsedBy(usedby);
        this.facade.create(subject);
      }
    } while (subject == null && (attempts--) > 0);

    if (subject == null)
      throw ResourceConflictException.of("id");

    return Response.status(Response.Status.OK).entity(subject.getId()).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service request to modify a certain {@link Identifier} using PUT.
   ** <p>
   ** This method isn't allowed by the service hence we responed with a
   ** {@link ForbiddenException}.
   **
   ** @param  id                 the identifier of the {@link Identifier} to
   **                            modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           an object that represents the resource to
   **                            modify.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.   **
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
   ** @throws NotFoundException   if no entity is mapped with identifier
   **                             <code>id</code> at the Service Provider.
   ** @throws BadRequestException if the value for parameter
   **                             <code>startIndex</code> or <code>count</code>
   **                             isn't evaluated as a integer number.
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

    Identifier entity = this.facade.lookup(id);
    if (entity == null)
      throw notFound(Identifier.NAME, id);

    if (!entity.getActive())
      throw mutability();

    // merge the resource into the entity found and persist the changes
    entity = this.facade.modify(Schema.identifier(entity, resource));
    // form the response sedn back to the client
    final Schema trimmer = Schema.build(resourceTrimmer(context));
    return Response
      .status(Response.Status.OK)
      .entity(
        trimmer.convert(entity)
      )
      .build()
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service request to deactivate a certain {@link Identifier} using
   ** DELETE.
   **
   ** @param  id                 the identifier of the {@link Identifier} to
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
   ** @throws BadRequestException  if the value for parameter
   **                              <code>startIndex</code> or <code>count</code>
   **                              isn't evaluated as a integer number.
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

    final Identifier subject = this.facade.lookup(id);
    if (subject == null)
      throw notFound(Identifier.NAME, id);
    if (!subject.getActive())
      throw mutability();

    // set the state of the entity to deactivated ...
    subject.setActive(false);
    // ... and persist the change
    this.facade.modify(subject);
    return Response.status(Response.Status.NO_CONTENT).build();
  }
}