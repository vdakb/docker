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

    File        :   SurrogateEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SurrogateEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid;

import java.util.Set;

import java.io.StringReader;

import javax.annotation.security.RolesAllowed;

import javax.json.Json;
import javax.json.JsonObject;

import javax.ejb.EJB;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.ForbiddenException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ServerErrorException;
import oracle.hst.platform.rest.ResourceConflictException;

import bka.iam.identity.igs.api.Endpoint;
import bka.iam.identity.igs.api.EndpointError;
import bka.iam.identity.igs.api.EndpointBundle;
import bka.iam.identity.igs.spi.IdentifierGenerator;

import bka.iam.identity.uid.api.TypeFacade;
import bka.iam.identity.uid.api.StateFacade;
import bka.iam.identity.uid.api.TenantFacade;
import bka.iam.identity.uid.api.CountryFacade;
import bka.iam.identity.uid.api.SurrogateFacade;
import bka.iam.identity.uid.api.ParticipantFacade;
import bka.iam.identity.uid.api.ParticipantTypeFacade;

import bka.iam.identity.uid.model.Surrogate;

import bka.iam.identity.uid.marshal.Schema;

import java.text.ParseException;

import javax.ws.rs.core.MultivaluedMap;

import oracle.hst.platform.core.entity.Filter;
import oracle.hst.platform.jpa.SortOption;

////////////////////////////////////////////////////////////////////////////////
// class SurrogateEndpoint
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The REST service {@link Endpoint} for {@link Surrogate} transactions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(SurrogateEndpoint.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SurrogateEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path of the resource endpoint */
  public static final String       PATH     = "uid";

  /** The permission required to generate an unqiue identifier */
  public static final String       GENERATE = "uid.generate";
  /** The permission required to register an unqiue identifier */
  public static final String       REGISTER = "uid.register";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  SurrogateFacade           uid;

  @EJB
  ParticipantTypeFacade     ptt;

  @EJB
  CountryFacade             cnt;

  @EJB
  StateFacade               sta;

  @EJB
  ParticipantFacade         pts;

  @EJB
  TypeFacade                typ;

  @EJB
  TenantFacade              tnt;

  final IdentifierGenerator eid = IdentifierGenerator.of(SurrogateSchema.EID.maximum);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SurrogateEndpoint</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SurrogateEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Service REST request to retrieve all {@link Surrogate}s paginated by the
   ** specified parameters.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the collection of {@link Surrogate}s.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ForbiddenException   the user is not permitted to operate on the
   **                              tenant derived from the UID.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for an unique identifier have been violated.
   ** @throws ServerErrorException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @RolesAllowed({"generate", "register"})
  public Response list(final @Context UriInfo context)
    throws ForbiddenException
    ,      BadRequestException
    ,      ServerErrorException {

    try {
      // contraint the result set to the tenants permitted
      final Set<String>             allowed = this.tnt.permitted();
      final SearchRequest           request = constraintRequest(context, allowed);
      final SearchResult<Surrogate> result  = (allowed.size() == 0) ? new SearchResult<Surrogate>(0) : this.uid.list(request);
      final Schema                  trimmer = Schema.build(resourceTrimmer(context));
      // JPA's start index is 0-base but the REST API operates with an 1-based
      // index
      final JsonObject response = Schema.surrogate(trimmer, request.start() + 1, result);
      return Response.status(Response.Status.OK).entity(response).build();
    }
    catch (PersistenceException e) {
      throw ServerErrorException.internal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to retrieve a certain {@link Surrogate} by its
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Surrogate} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the {@link Surrogate} mapped at <code>id</code>
   **                            wrapped in a {@link Response} or the
   **                            appropriate error message about not found.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException    if no entity is mapped with identifier
   **                              <code>id</code> at the Service Provider.
   ** @throws ForbiddenException   the user is not permitted to operate on the
   **                              tenant derived from the UID.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for an unique identifier have been violated.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @GET
  @Path("{id}")
  @RolesAllowed({"generate", "register"})
  public Response lookup(final @PathParam("id") String id, final @Context UriInfo context)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      PersistenceException {

    if (id == null || id.length() < SurrogateSchema.MIN || id.length() > SurrogateSchema.MAX)
      throw BadRequestException.invalidPath(id);

    final JsonObject   model   = SurrogateSchema.slice(id);
    final String       tenant  = SurrogateSchema.tenant(model);
    if (!this.tnt.permitted(tenant, Boolean.TRUE, Boolean.TRUE))
      throw ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.TENANT_LOOKUP_NOT_PERMITTED, tenant));

    final Surrogate subject = this.uid.lookup(tenant, model.getString(SurrogateSchema.TID.json, null), model.getString(SurrogateSchema.EID.json, null));
    if (subject == null)
      throw notFound(Surrogate.NAME, id);

    final Schema trimmer = Schema.build(resourceTrimmer(context));
    return Response.status(Response.Status.OK).entity(trimmer.convert(subject)).build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   generate
  /**
   ** REST service request to validate and compose a certain {@link Surrogate}
   ** using POST leveraging the provided {@link JsonObject}.
   **
   ** @param  resource           an object that represents the
   **                            {@link JsonObject} resource to create.
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
   ** @throws NotFoundException         if no entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider
   **                                   for a specific segment of the unique
   **                                   identifier.
   ** @throws ForbiddenException        the user is not permitted to operate on
   **                                   the tenant derived from the UID.
   ** @throws BadRequestException       when either the syntactic or semantic
   **                                   rules for a unique identifier have been
   **                                   violated.
   ** @throws ServerErrorException      if the entity manager has been closed or
   **                                   the criteria query is found to be
   **                                   invalid.
   ** @throws ResourceConflictException if an entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider.
   */
  @POST
  @RolesAllowed("generate")
  public Response generate(final JsonObject resource, final @Context UriInfo context)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      ServerErrorException
    ,      ResourceConflictException {

    // validate the lexicographical correctness
    SurrogateSchema.validate(resource);
    // validate the constraints
    validate(resource);

    // ensure that the requester is permitted on the resolved tenant
    final String tenant = SurrogateSchema.tenant(resource);
    try {
      if (!this.tnt.permitted(tenant, Boolean.TRUE, Boolean.FALSE))
        throw ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.TENANT_GENERATE_NOT_PERMITTED, tenant));

      String eid = resource.getString(SurrogateSchema.EID.json, null);
      // if the resource does not provide an external ID a generator kicks in
      // that fills this field with the required informations
      if (eid ==  null) {
        eid = this.eid.randomAlphaNumeric();
      }

      // ensure that the unique identifier is unique across all existing
      // identifiers
      Surrogate entity = this.uid.lookup(tenant, resource.getString(SurrogateSchema.TID.json), eid);
      if (entity != null)
        // post the identifier string only to avoid multiple taggings with
        // square brackets
        throw conflict(SurrogateSchema.UID, entity.getId().toString());

      entity = SurrogateSchema.compose(resource, eid);
      // mark the surrogate requested as generated
      entity.setState(Surrogate.State.GENERATED.value);

      final Schema trimmer = Schema.build(resourceTrimmer(context));
      return Response.status(Response.Status.OK).entity(trimmer.convert(this.uid.create(entity))).build();
    }
    catch (PersistenceException e) {
      throw ServerErrorException.internal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   register
  /**
   ** REST service request to validate and register a certain {@link Surrogate}
   ** using PUT leveraging the provided {@link String}.
   ** <p>
   ** This operation is not suitable to delete/remove/nullify attributes. For
   ** this purpose you can use the PATCH operation instead. PUT is intended to
   ** do replacements using the (not-null) values supplied in the
   ** <code>resource</code> argument.
   **
   ** @param  resource           an object that contains the data to register a
   **                            Unique Identifier resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    A succesful response for this operation
   **                            contains a status code of 200.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException         if no entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider
   **                                   for a specific segment of the unique
   **                                   identifier.
   ** @throws ForbiddenException        the user is not permitted to operate on
   **                                   the tenant derived from the UID.
   ** @throws BadRequestException       when either the syntactic or semantic
   **                                   rules for a unique identifier have been
   **                                   violated.
   ** @throws ServerErrorException      if the entity manager has been closed or
   **                                   the criteria query is found to be
   **                                   invalid.
   ** @throws ResourceConflictException if an entity is mapped with identifier
   **                                   <code>id</code> at the Service Provider.
   */
  @PUT
  @RolesAllowed("register")
  public Response register(final String resource, final @Context UriInfo context)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      ServerErrorException
    ,      ResourceConflictException {

    final String id = Json.createReader(new StringReader(resource)).readObject().getString(SurrogateSchema.UID, null);
    if (id == null || id.length() < SurrogateSchema.MIN || id.length() > SurrogateSchema.MAX)
      throw BadRequestException.invalidPath(id);

    final JsonObject model = SurrogateSchema.slice(id);
    // validate the lexicographical correctness
    SurrogateSchema.validate(model);
    // validate the constraints
    validate(model);
    // extra validation step is required here for external ID
    SurrogateSchema.validateExternalId(model);
    // ensure that the requester is permitted on the resolved tenant
    final String tenant = SurrogateSchema.tenant(model);
    try {
      if (!this.tnt.permitted(tenant, Boolean.FALSE, Boolean.TRUE))
        throw ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.TENANT_REGISTER_NOT_PERMITTED, tenant));

      // ensure that the unique identifier is unique across all existing
      // identifiers
      Surrogate entity = this.uid.lookup(tenant, model.getString(SurrogateSchema.TID.json), model.getString(SurrogateSchema.EID.json));
      if (entity != null)
        throw conflict(SurrogateSchema.UID, id);

      entity = SurrogateSchema.compose(model, model.getString(SurrogateSchema.EID.json));
      // mark the surrogate requested as registered
      entity.setState(Surrogate.State.REGISTERED.value);

      final Schema trimmer = Schema.build(resourceTrimmer(context));
      return Response.status(Response.Status.OK).entity(trimmer.convert(this.uid.create(entity))).build();
    }
    catch (PersistenceException e) {
      throw ServerErrorException.internal(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service request to delete a certain {@link Surrogate} using DELETE.
   ** <p>
   ** This method isn't allowed by the service hence we responed with a
   ** {@code ForbiddenException}.
   **
   ** @param  id                 the identifier of the {@link Surrogate} to
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
   ** @throws ForbiddenException   the user is not permitted to operate on the
   **                              tenant derived from the UID.
   ** @throws BadRequestException  when either the syntactic or semantic rules
   **                              for an unique identifier have been violated.
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @DELETE
  @Path("{id}")
  @RolesAllowed({"generate", "register"})
  public Response delete(final @PathParam("id") String id)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      PersistenceException {

    final JsonObject model  = SurrogateSchema.slice(id);
    // ensure that the requester is permitted on the resolved tenant
    final String     tenant = SurrogateSchema.tenant(model);
    if (!this.tnt.permitted(tenant, Boolean.TRUE, Boolean.TRUE))
      throw ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.TENANT_MODIFY_NOT_PERMITTED, tenant));

    Surrogate entity = this.uid.lookup(tenant, model.getString(SurrogateSchema.TID.json), model.getString(SurrogateSchema.EID.json));
    if (entity == null)
      throw notFound(Surrogate.NAME, id);
    if (entity.getState() == 0)
      throw mutability();

    // set the state of the entity to inactive (state = 0) ...
    entity.setState(0);
    // ... and persist the change
    this.uid.modify(entity);
    return Response.status(Response.Status.NO_CONTENT).build();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   constraintRequest
  /**
   ** Factory method to create a {@link SearchRequest} from the given request
   ** query <code>contet</code>.
   **
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   ** @param  tenant             the collection of tenants permitted for the
   **                            authenticated principal.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the JPA {@link SearchRequest} parsed from the
   **                            query part of an HTTP request.
   **                            Possible object is {@link SearchRequest}.
   **
   ** @throws ForbiddenException  the user is not permitted to operate on the
   **                             tenant derived from the filter.
   ** @throws BadRequestException if the value for parameter
   **                             <code>startIndex</code> or <code>count</code>
   **                             isn't evaluated as a integer number.
   */
  protected static SearchRequest constraintRequest(final UriInfo context, final Set<String> tenant)
    throws BadRequestException
    ,      ForbiddenException {

    try {
      String filter = null;
      final MultivaluedMap<String, String> query = context.getQueryParameters();
      if (!query.containsKey(FILTER)) {
        // no need to check for size == 0 due to its at performing the search
        if (tenant.size() != 0) {
          filter = tenantFilter(tenant);
        }
      }
      else {
        final String temp = filter(query);
        // its easy if tenant is not contained in the filter criteria
        if (!temp.contains("tenant")) {
          final StringBuilder builder = new StringBuilder(temp);
          builder.append(" and ").append(tenantFilter(tenant));
          filter = builder.append(" and ").append(tenantFilter(tenant)).toString();
        }
        // FIXME: don't let pass through the unmodified filter
        else {
          int pos = temp.indexOf("tenant") + "tenant".length() + 1;
          while (pos < temp.length()) {
            // get the position and move it to the start of the operator
            // verify the operator (has always a length of 2)
            final String op = temp.substring(pos, pos + 2);
            // any othe operator as equal is rejected
            if (!"eq".equals(op))
              throw (BadRequestException.invalidFilter("tenant"));

            // verify the value by moving the position after the first quote
            pos = pos + 4;
            // find the second quote
            int end = temp.indexOf('"', pos);
            // if there is no second quote the filter is invalid
            if (end == -1)
              throw (BadRequestException.invalidFilter("tenant"));

            final String val = temp.substring(pos, end);
            if (!tenant.contains(val))
              throw ForbiddenException.notPermitted(EndpointBundle.string(EndpointError.TENANT_SEARCH_NOT_PERMITTED, val));
          
            pos = temp.indexOf("tenant", pos);
            if (pos == -1)
              pos = temp.length();
            else
              pos += "tenant".length() + 1;
          }
          filter = temp;
        }
      }

      return SearchRequest.of(
       // JPA's start index is 0-base but the REST API should look like SCIM
       // hence the start index is 1-based
        start(query)
      , count(query)
      , Boolean.TRUE
      , translate(Filter.from(filter))
      , SortOption.by(SortOption.Order.by(sortBy(query), order(query)))
      );
    }
    catch (ParseException e) {
      throw (BadRequestException.invalidFilter(e.getLocalizedMessage()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** Validate if the given {@link JsonObject} <code>resource</code> satisfies
   ** the requirements to be a valid resource.
   **
   ** @param  resource           the {@link JsonObject} to validate.
   **                            <br>
   **                            Allowed object is {@link JsonObject}.
   */
  private void validate(final JsonObject resource)
    throws NotFoundException
    ,      PersistenceException {

    String value = resource.getString(SurrogateSchema.PTT.json);
    if (this.ptt.lookup(value) == null)
      throw notFound(SurrogateSchema.PTT.name, value);

    value = resource.getString(SurrogateSchema.CID.json);
    if (this.cnt.lookup(value) == null)
      throw notFound(SurrogateSchema.CID.name, value);

    value = resource.getString(SurrogateSchema.SID.json);
    if (this.sta.lookup(value) == null)
      throw notFound(SurrogateSchema.SID.name, value);

    value = resource.getString(SurrogateSchema.PTS.json);
    if (this.pts.lookup(value) == null)
      throw notFound(SurrogateSchema.PTS.name, value);

    value = resource.getString(SurrogateSchema.TID.json);
    if (this.typ.lookup(value) == null)
      throw notFound(SurrogateSchema.TID.name, value);

    value = SurrogateSchema.tenant(resource);
    if (this.tnt.lookup(value) == null)
      throw notFound("tenant", value);
  }
  
  private static String tenantFilter(final Set<String> allowed) {
    // formulate a filter that contains the tenants permitted
    int i = 0;
    final StringBuilder builder = new StringBuilder("(");
    for (String tenant : allowed) {
      if (i++ > 0)
        builder.append(" or ");
      builder.append("tenant").append(" eq ").append(tenant);
    }
    return builder.append(")").toString();
  }
}