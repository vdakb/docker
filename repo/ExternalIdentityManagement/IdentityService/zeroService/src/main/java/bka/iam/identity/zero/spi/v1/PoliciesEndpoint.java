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
    Subsystem   :   ZeRo Service

    File        :   AccessPolicyEndpoint.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Adrien Farkas

    Purpose     :   This file implements the class
                    UserEndpoint.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-17-03  AFarkas     First release version
*/

package bka.iam.identity.zero.spi.v1;

import bka.iam.identity.igs.api.Endpoint;
import bka.iam.identity.zero.api.PolicyFacade;

import java.util.Iterator;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.response.ErrorResponse;
import oracle.hst.platform.rest.response.ListResponse;

import oracle.iam.identity.igs.model.PolicyEntity;
import oracle.iam.identity.igs.model.Schema;

////////////////////////////////////////////////////////////////////////////////
// class PolicyEndpoint
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /appschemas endpoint.
 ** <br>
 ** Methods here are intercepted and/or decorated.
 ** <p>
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/AccessPolicies")
public class PoliciesEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CLASS   = PoliciesEndpoint.class.getName();
  private static final Logger LOGGER  = Logger.create(CLASS);
  
  /**
   ** The property name of the 1-based index of the first query result.
   */
  public static final String  START   = "start";
  /**
   ** The property name of the integer indicating the desired maximum number of
   ** query results per page.
   */
  public static final String  ITEMS   = "items";
  /**
   ** The property name of the filter string used to request a subset of
   ** resources.
   */
  public static final String  FILTER  = "filter";
  /**
   ** The property name of the string indicating the order in which the sortBy
   ** parameter is applied.
   */
  public static final String  ORDER   = "sortOrder";
  /**
   ** The property name of the string indicating the attribute whose value shall
   ** be used to order the returned responses.
   */
  public static final String  SORT    = "sortBy";
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  PolicyFacade facade;

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
  public PoliciesEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** REST service list request for Access Policies using GET
   **
   ** @param  requestedStart     requested paging start item.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @param  requestedItems     requested paging number of items. Can be lower
   **                            if there are less results than start + items.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @param  filter             SCIM-like filter for filtering resulting
   **                            objects to be returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  sortBy             name of the attribute(s) to use to sort the resulting
   **                            data.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  sortOrder          ordering to use for the attributes names. Possible
   **                            values are "ascending" (default when not supplied) and
   **                            "descending".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  isSpecial          flag whether to list "special" policies.
   **                            <br>
   **                            Allowed object is {@link String} representation
   **                            of {@link Boolean}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link ListResponse}
   **                            in the entity body, unsuccessful response will
   **                            hold bo entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed({"internal"})
  public Response list(final @QueryParam(START) String requestedStart,
                       final @QueryParam(ITEMS) String requestedItems,
                       final @QueryParam(FILTER) String filter,
                       final @QueryParam(SORT) String sortBy,
                       final @QueryParam(ORDER) String sortOrder,
                       final @QueryParam("special") String isSpecial) {
    final String method = "list";
    LOGGER.entering(CLASS, method,
                    "requestedStart=", requestedStart,
                    "requestedItems=", requestedItems,
                    "filter", filter,
                    "isSpecial=", isSpecial);

    int start = PolicyFacade.START;
    int items = PolicyFacade.ITEMS;
    try {
      if (requestedStart != null) {
        LOGGER.debug(CLASS, method, "Value for start supplied, setting to: " + Integer.valueOf(requestedStart));
        start = Integer.valueOf(requestedStart);
      } else {
        LOGGER.trace(CLASS, method, "Value for start is null, using default " + start);
      }
      if (requestedItems != null) {
        LOGGER.debug(CLASS, method, "Value for items supplied, setting to: " + Integer.valueOf(requestedItems));
        items = Integer.valueOf(requestedItems);
        // Sanity check
        if (items > 1000) {
          items = 1000;
        }
      } else {
        LOGGER.trace(CLASS, method, "Value for items is null, using default " + items);
      }
    } catch (NumberFormatException e) {
      LOGGER.debug(CLASS, method, "Invalid number format, sticking with defaults");
    }

    try {
      ListResponse<PolicyEntity> policies = this.facade.list(start, items, filter, sortBy, sortOrder, Boolean.valueOf(isSpecial));
      Iterator<PolicyEntity> policyIterator = policies.iterator();
      final JsonArrayBuilder collector = Json.createArrayBuilder();
      while (policyIterator.hasNext()) {
        collector.add(Schema.marshalPolicy(policyIterator.next()));
      }
      JsonObjectBuilder result = Json.createObjectBuilder();
      result.add("policy", collector);
      return Response.ok()
                     .entity(result.build())
//                     .entity(policies)
                     .build();
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      if (message == null || "".equals(message))
        message = e.getMessage();
      LOGGER.throwing(CLASS, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service lookup request for Access Policy using GET
   **
   ** @param  policyName         name of the access policy to look up.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  special            flag whether to list "special" policies.
   **                            <br>
   **                            Allowed object is {@link String} representation
   **                            of {@link Boolean}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link PolicyEntity}
   **                            in the entity body, unsuccessful response will
   **                            hold bo entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException  if the Access Policy referenced by name does
   **                            not exist.
   */
  @GET
  @Produces("application/json")
  @RolesAllowed({"internal"})
  @Path("{name}")
  public Response lookup(final @PathParam("name") String policyName,
                         final @QueryParam("special") String special) throws NotFoundException {
    final String method = "lookup";
    LOGGER.entering(CLASS, method, "policyName=", policyName);

    try {
      PolicyEntity policy = this.facade.lookup(policyName, Boolean.valueOf(special));
      if (policy == null) {
        LOGGER.exiting(CLASS, method, "Access Policy not found");
        // Maybe slightly misleading, although Application Schema should be returned this
        // null result means the appropriate Application Instance was not found.
        throw NotFoundException.of(bka.iam.identity.zero.model.AccessPolicy.RESOURCE_NAME, policyName);
      } else {
        LOGGER.exiting(CLASS, method, "Going to marshal result: " + policy);
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("policy", Schema.marshalPolicy(policy));
        return Response.ok()
                       .entity(result.build())
                       .build();
      }
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      if (message == null || "".equals(message))
        message = e.getMessage();
      LOGGER.throwing(CLASS, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** REST service delete Access Policies using DELETE
   **
   ** @param  policyName         Name of the access policy to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  special            flag whether to list "special" policies.
   **                            <br>
   **                            Allowed object is {@link String} representation
   **                            of {@link Boolean}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 204 and an empty
   **                            body, unsuccessful response will
   **                            hold no entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException  if the Access Policy referenced by name does
   **                            not exist.
   */
  @DELETE
  @Produces("application/json")
  @RolesAllowed({"internal"})
  @Path("{name}")
  public Response delete(final @PathParam("name") String policyName,
                         final @QueryParam("special") String special)
  throws NotFoundException {
    final String method = "delete";
    LOGGER.entering(CLASS, method, "policyName=", policyName, "special=", special);
    try {
      PolicyFacade.Result result = this.facade.delete(policyName, Boolean.valueOf(special));
      LOGGER.debug(CLASS, method, "result=" + result);
      if (result == null) {
        ErrorResponse response = new ErrorResponse(503);
        response.description("").type("").detail("");
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                       .entity(response.jsonify())
                       .build();
      } else {
        if (result.equals(PolicyFacade.Result.ok)) {
          // All okay, means return "204 No Data" with no body
          return Response.noContent()
                         .build();
        } else {
          LOGGER.exiting(CLASS, method, "Application Instance not found");
          // Maybe slightly misleading, although Application Schema should be returned this
          // null result means the appropriate Application Instance was not found.
          throw NotFoundException.of(bka.iam.identity.zero.model.AccessPolicy.RESOURCE_NAME, policyName);
        }
      }
    } catch (EJBException e) {
      LOGGER.throwing(CLASS, method, e);
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      if (message == null || "".equals(message))
        message = e.getMessage();
      LOGGER.throwing(CLASS, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message == null ? "" : message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** REST service lookup request for Access Policies using GET
   **
   ** @param  policyName         Name of the access policy to modify.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  inputEntity        JSON representation of the policy to be created.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  special            flag whether to list "special" policies.
   **                            <br>
   **                            Allowed object is {@link String} representation
   **                            of {@link Boolean}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and an updated
   **                            {@link PolicyEntity}
   **                            in the entity body, unsuccessful response will
   **                            hold bo entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException  if the Access Policy referenced by name does
   **                            not exist.
   */
  @PUT
  @Produces("application/json")
  @RolesAllowed({"internal"})
  @Path("{name}")
  public Response modify(final @PathParam("name") String policyName,
                         final @QueryParam("special") String special,
                         final PolicyEntity inputEntity) throws NotFoundException {
    final String method = "modify";
    LOGGER.entering(CLASS, method, "policyName=", policyName, "inputEntity=", inputEntity);
    
    try {
      // First, confirm the policy does exist, returning
      PolicyEntity policy = this.facade.lookup(policyName, Boolean.valueOf(special));
      if (policy == null) {
        LOGGER.exiting(CLASS, method, "Access Policy not found");
        // Maybe slightly misleading, although Application Schema should be returned this
        // null result means the appropriate Application Instance was not found.
        throw NotFoundException.of(bka.iam.identity.zero.model.AccessPolicy.RESOURCE_NAME, policyName);
      }
      policy = this.facade.modify(policyName, inputEntity, Boolean.valueOf(special));
      LOGGER.debug(CLASS, method, "Received from facade: " + policy);
      if (policy == null) {
        String message = "Policy not modified";
        LOGGER.exiting(CLASS, method, message);
        throw new EJBException(message);
//          NotFoundException.of(bka.iam.identity.zero.model.AccessPolicy.RESOURCE_NAME, name);
      } else {
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("policy", Schema.marshalPolicy(policy));
        return Response.ok()
                       .entity(result.build())
                       .build();
      }
    } catch (EJBException e) {
      LOGGER.throwing(CLASS, method, e);
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      if (message == null || "".equals(message))
        message = e.getMessage();
      LOGGER.throwing(CLASS, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message == null ? "" : message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service create Access Policy using POST
   **
   ** @param  inputEntity         JSON representation of the policy to be created.
   **                             <br>
   **                             Allowed object is {@link String}.
   **
   ** @param  special             flag whether to list "special" policies.
   **                             <br>
   **                             Allowed object is {@link String} representation
   **                             of {@link Boolean}.
   **
   ** @return                     a JAX-RS Response object containing the
   **                             lookup result.
   **                             <br>
   **                             A succesful response for this operation should
   **                             contain a status code of 200 and a
   **                             {@link PolicyEntity}
   **                             in the entity body, unsuccessful response will
   **                             hold bo entity and hava a Response.NOT_FOUND
   **                             status code.
   **                             <br>
   **                             Possible object is {@link Response}.
   **
   ** @throws BadRequestException if the request contains validation errors and cannnot
   **                             be processed.
   */
  @POST
  @Produces("application/json")
  @RolesAllowed({"internal"})
  public Response create(final PolicyEntity inputEntity,
                         final @QueryParam("special") String special)
  throws BadRequestException {
    final String method = "create";
    LOGGER.entering(CLASS, method, "inputEntity=", inputEntity);
    
    String name = inputEntity.id();
    
    try {
      PolicyEntity policy = this.facade.create(inputEntity, Boolean.valueOf(special));
      if (policy == null) {
        String message = "Policy not created";
        LOGGER.exiting(CLASS, method, message);
        throw BadRequestException.invalidValue(message);
      } else {
        LOGGER.exiting(CLASS, method, "Going to marshal result: " + policy);
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("policy", Schema.marshalPolicy(policy));
        return Response.ok()
                       .entity(result.build())
                       .build();
      }
    } catch (EJBException e) {
      LOGGER.throwing(CLASS, method, e);
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      if (message == null || "".equals(message))
        message = e.getMessage();
      LOGGER.throwing(CLASS, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message == null ? "" : message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
}