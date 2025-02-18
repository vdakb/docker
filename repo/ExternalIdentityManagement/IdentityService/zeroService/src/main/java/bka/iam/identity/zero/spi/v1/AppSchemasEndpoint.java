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
import bka.iam.identity.zero.api.AppSchemaFacade;
import bka.iam.identity.zero.model.AppInstance;
import bka.iam.identity.zero.model.SchemaAttribute;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.response.ErrorResponse;
//import javax.annotation.security.RolesAllowed;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementsEndpoint
// ~~~~~ ~~~~~~~~~~~~
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
@Path("/appschemas")
public class AppSchemasEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                     category = AppSchemasEndpoint.class.getName();
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
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  AppSchemaFacade facade;

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
  public AppSchemasEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service lookup request for OIM Entitlement using GET
   **
   ** @param  key                key of the entitlement to look up.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a
   **                            {@link SchemaAttribute}
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
  @RolesAllowed({"viewer", "administrator"})
  @Path("{key}")
  public Response lookup(@PathParam("key") String key) throws NotFoundException {
    final String method = "lookup";
    logger.entering(category, method, "key=", key);

    try {
      List<SchemaAttribute> result = this.facade.lookup(key);
      if (result.size() == 0) {
        logger.exiting(category, method, "Application Instance not found");
        // Maybe slightly misleading, although Application Schema should be returned this
        // null result means the appropriate Application Instance was not found.
        throw NotFoundException.of(AppInstance.RESOURCE_NAME, key);
      } else {
        logger.exiting(category, method, "Application Instance found");
        return Response.ok()
                       .entity(result)
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
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////
}