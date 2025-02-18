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

    File        :   EntitlementsEndpoint.java

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
import bka.iam.identity.zero.api.OIMEntitlementFacade;
import bka.iam.identity.zero.model.AppEntitlements;
import bka.iam.identity.zero.model.OIMEntitlement;

import java.util.Iterator;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.ServiceException;
import oracle.hst.platform.rest.response.ErrorResponse;
import oracle.hst.platform.rest.response.ListResponse;

import oracle.iam.identity.igs.model.EntitlementEntity;
import oracle.iam.identity.igs.model.Schema;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementsEndpoint
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /entitlements endpoint.
 ** <br>
 ** Methods here are intercepted and/or decorated.
 ** <p>
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/entitlements")
public class EntitlementsEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                     category = EntitlementsEndpoint.class.getName();
  private static final Logger                     logger   = Logger.create(category);
  
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
  /**
   ** The property name to identify the format to return.
   */
  public static final String  NEW_WAY = "newWay";
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  OIMEntitlementFacade facade;

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
  public EntitlementsEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   search
  /**
   ** REST service search request for listing OIM Entitlements using GET.
   **
   ** @param  sc                 the request security context.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @param  context            the request context.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
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
   ** @param newWay              a {@link String} signature distinguisher to use the new
   **                            return format.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            Possible object is {@link ListResponse}.
   **
   ** @throws ServiceException   if the conversion of the search result
   **                            into a search response fails.
   */
  @GET
  @Produces("application/json")
  @RolesAllowed({"viewer", "administrator", "internal"})
  public Object search(final @Context SecurityContext sc,
                       final @Context UriInfo context,
                       final @QueryParam(START) String requestedStart,
                       final @QueryParam(ITEMS) String requestedItems,
                       final @QueryParam(FILTER) String filter,
                       final @QueryParam(NEW_WAY) String newWay
                      ) throws ServiceException {
    
    final String method = "search";
    logger.entering(category, method);
    
    // Set default values first, maybe they'll be overridden later
    int start = OIMEntitlementFacade.START;
    int items = OIMEntitlementFacade.ITEMS;
    try {
      if (requestedStart != null) {
        logger.debug(category, method, "Value for start supplied, setting to: " + Integer.valueOf(requestedStart));
        start = Integer.valueOf(requestedStart);
      } else {
        logger.trace(category, method, "Value for start is null, using default " + start);
      }
      if (requestedItems != null) {
        logger.debug(category, method, "Value for items supplied, setting to: " + Integer.valueOf(requestedItems));
        items = Integer.valueOf(requestedItems);
        // Sanity check
        if (items > 1000) {
          items = 1000;
        }
      } else {
        logger.trace(category, method, "Value for items is null, using default " + items);
      }
    } catch (NumberFormatException e) {
      logger.debug(category, method, "Invalid number format, sticking with defaults");
    }

    try {
      if (newWay == null || !newWay.equals("true")) {
        ListResponse<AppEntitlements> result = this.facade.list(start, items, filter);
        logger.exiting(category, method, result);
        return Response.status(Response.Status.OK)
                       .entity(result)
                       .build();
      } else {
        ListResponse<EntitlementEntity> entitlements = this.facade.list(start, items, filter, "");
        Iterator<EntitlementEntity> it = entitlements.iterator();
        final JsonArrayBuilder collector = Json.createArrayBuilder();
        while (it.hasNext()) {
          collector.add(Schema.marshalEntitlement(it.next()));
        }
        JsonObjectBuilder result = Json.createObjectBuilder();
        result.add("entitlement", collector);
        return Response.ok()
                       .entity(result.build())
                       .build();
      }
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      logger.throwing(category, method, cause);
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
   ** REST service lookup request for OIM Entitlement using GET
   **
   ** @param  key                key of the entitlement to look up.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param newWay              a {@link String} signature distinguisher to use the new
   **                            return format.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link OIMEntitlement}
   **                            in the entity body, unsuccessful response will
   **                            hold bo entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException  if the OIM Entitlement referenced by key does
   **                            not exist.
   */
  @GET
  @Produces("application/json")
  @RolesAllowed({"viewer", "administrator", "internal"})
  @Path("{key}")
  public Response lookup(final @PathParam("key") String key,
                         final @QueryParam(NEW_WAY) String newWay
                        ) throws NotFoundException {
    final String method = "lookup";
    logger.entering(category, method, "key=", key);

    try {
      if (newWay == null || !newWay.equals("true")) {
        OIMEntitlement result = this.facade.lookup(key);
        if (result == null) {
          logger.exiting(category, method, "Entitlement not found");
          throw NotFoundException.of(OIMEntitlement.RESOURCE_NAME, key);
        } else {
          logger.exiting(category, method, "Entitlement found");
          return Response.ok()
                         .entity(result)
                         .build();
        }
      } else {
        EntitlementEntity result = this.facade.lookup(key, "true");
        if (result == null) {
          logger.exiting(category, method, "Entitlement not found");
          throw NotFoundException.of(OIMEntitlement.RESOURCE_NAME, key);
        } else {
          logger.exiting(category, method, "Entitlement found");
          return Response.ok()
                         .entity(Schema.marshalEntitlement(result).toString())
                         .build();
        }      }
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      String message = cause.getMessage();
      logger.throwing(category, method, cause);
      ErrorResponse response = new ErrorResponse(503);
      response.description(message).type("").detail("");
      return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                     .entity(response.jsonify())
                     .build();
    }
  }
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
}