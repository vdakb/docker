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
import bka.iam.identity.zero.api.AccountsFacade;
import bka.iam.identity.zero.model.Identity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.response.ErrorResponse;
import oracle.hst.platform.rest.response.ListResponse;

import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.Schema;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.service.api.RequestBundle;
import oracle.iam.service.api.RequestMessage;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementsEndpoint
// ~~~~~ ~~~~~~~~~~~~
/**
 ** A JAX-RS resource implementation of /identity endpoint.
 ** <br>
 ** Methods here are intercepted and/or decorated.
 ** <p>
 **
 ** @author  adrien.farkas@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path("/accounts")
public class AccountsEndpoint extends Endpoint {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String                     category = AccountsEndpoint.class.getName();
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
  AccountsFacade facade;

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
  public AccountsEndpoint() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** REST service to list accounts from and application instance using GET.
   ** Used as a dummy testing endpoint for the EJB method, therefore no paging
   ** is implemented.
   ** 
   ** Internal use only!
   **
   ** @param  appName            name of the application instance} to
   **                            retrieve accounts from. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  orgName            name of the organization to use for
   **                            retrieving identitied (users) from. If null then
   **                            accounts are listed from the application instance
   **                            and only account owners are returned. If 
   **                            null all identities from the organization
   **                            are retrieved and their associated accounts are
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
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
  @RolesAllowed({"internal"})
  @Path("st/appInstance/{appName}")
  public Response list(final @PathParam("appName") String appName,
                       final @QueryParam("org") String orgName,
                       final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "list";
    logger.entering(category, method, "appName=", appName, "orgName=", orgName);

    try {
      final String userName = security.getUserPrincipal().getName();
      List<Identity> accounts = this.facade.list(appName, orgName, userName);
      logger.exiting(category, method, accounts.size() + " accounts found, returning");
      return Response.ok()
                     .entity(accounts)
                     .build();
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      // TODO: map it properly
      logger.debug(category, method, "Cause: " + cause.getClass().getName());
      if (cause instanceof oracle.iam.platform.authopss.exception.AccessDeniedException) {
        String message = "Permission denied";
        logger.throwing(category, method, cause);
        ErrorResponse response = new ErrorResponse(403);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.FORBIDDEN)
                       .entity(response.jsonify())
                       .build();
      }
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
  // Method:   list
  /**
   ** REST service to list accounts from and application instance using GET.
   ** Paging is implemented.
   **
   ** @param  appName            name of the application instance} to
   **                            retrieve accounts from. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
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
  @RolesAllowed({"administrator"})
  @Path("appInstance/{appName}")
  public Response list(final @PathParam("appName") String appName,
                       final @QueryParam(START) String requestedStart,
                       final @QueryParam(ITEMS) String requestedItems,
                       final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "list";
    logger.entering(category, method, "appName=", appName,
                    "requestedStart=", requestedStart,
                    "requestedItems=", requestedItems);

    // Set default values first, maybe they'll be overridden later
    int start = AccountsFacade.START;
    int items = AccountsFacade.ITEMS;
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

    ListResponse<AccountEntity> resultSet = null;
    try {
      final String userName = security.getUserPrincipal().getName();
      List<AccountEntity> accounts = this.facade.list(appName, userName, start, items);
      
      // Non-paging version
//      JsonArrayBuilder accountArray = Json.createArrayBuilder();
//      for (AccountEntity account : accounts) {
//        accountArray.add(Schema.marshalAccount(account));
//      }
//      JsonObjectBuilder result = Json.createObjectBuilder();
//      result.add("accounts", accountArray);
//      return Response.ok()
//                     .entity(result.build())
//                     .build();

      // Paging version
      JsonArrayBuilder accountArray = Json.createArrayBuilder();
      Iterator it = accounts.iterator();
      List<String> pagedResult = new ArrayList<>();
      while (it.hasNext()) {
        pagedResult.add(Schema.marshalAccount((AccountEntity) it.next()).toString());
      }
      JsonObjectBuilder result = Json.createObjectBuilder();
      result.add("accounts", accountArray);
      return Response.ok()
                     .entity(new ListResponse<AccountEntity>(-1L, start, accounts.size(), accounts))
                     .build();
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
  // Method:   getAccounts
  /**
   ** REST service to get accounts for a particular OIM User whose login name
   ** is supplied as a path param.
   ** 
   ** Internal use only!
   **
   ** @param  targetUserName     name of the application instance} to
   **                            retrieve accounts from. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
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
  @RolesAllowed({"internal"})
  @Path("jt/users/{userName}")
  public Response getAccounts(final @PathParam("userName") String targetUserName,
//                       final @QueryParam(START) String requestedStart,
//                       final @QueryParam(ITEMS) String requestedItems,
                       final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "getAccounts";
    logger.entering(category, method, "targetUserName=", targetUserName);

//    // Set default values first, maybe they'll be overridden later
//    int start = AccountsFacade.START;
//    int items = AccountsFacade.ITEMS;
//    try {
//      if (requestedStart != null) {
//        logger.debug(category, method, "Value for start supplied, setting to: " + Integer.valueOf(requestedStart));
//        start = Integer.valueOf(requestedStart);
//      } else {
//        logger.trace(category, method, "Value for start is null, using default " + start);
//      }
//      if (requestedItems != null) {
//        logger.debug(category, method, "Value for items supplied, setting to: " + Integer.valueOf(requestedItems));
//        items = Integer.valueOf(requestedItems);
//        // Sanity check
//        if (items > 1000) {
//          items = 1000;
//        }
//      } else {
//        logger.trace(category, method, "Value for items is null, using default " + items);
//      }
//    } catch (NumberFormatException e) {
//      logger.debug(category, method, "Invalid number format, sticking with defaults");
//    }

    try {
      final String userName = security.getUserPrincipal().getName();
      Map<String, List<AccountEntity>> accounts = this.facade.listAccounts(targetUserName, userName);
      logger.debug(category, method, "Retrieved results: " + accounts);

      logger.debug(category, method, "Going to beautify results");
      JsonObjectBuilder result = Json.createObjectBuilder();
      JsonArrayBuilder accountsArray = Json.createArrayBuilder();
      for (String appInstanceName : accounts.keySet()) {
        logger.debug(category, method, "Processing accounts in application " + appInstanceName);

        JsonArrayBuilder accountArray = Json.createArrayBuilder();
        for (AccountEntity account : accounts.get(appInstanceName)) {
          logger.debug(category, method, "Processing account: " + account);
          accountArray.add(Schema.marshalAccount(account));
        }
        JsonObjectBuilder appInstanceResult = Json.createObjectBuilder();
        appInstanceResult.add("applicationInstancee", appInstanceName);
        appInstanceResult.add("accounts", accountArray);
        accountsArray.add(appInstanceResult);
      }
      result.add("accounts", accountsArray);
      // Non-paging version
//      JsonArrayBuilder accountArray = Json.createArrayBuilder();
//      for (AccountEntity account : accounts) {
//        accountArray.add(Schema.marshalAccount(account));
//      }
//      JsonObjectBuilder result = Json.createObjectBuilder();
//      result.add("accounts", accountArray);
      return Response.ok()
                     .entity(result.build())
                     .build();

      // Paging version
//      JsonArrayBuilder accountArray = Json.createArrayBuilder();
//      Iterator it = accounts.iterator();
//      List<String> pagedResult = new ArrayList<>();
//      while (it.hasNext()) {
//        pagedResult.add(Schema.marshalAccount((AccountEntity) it.next()).toString());
//      }
//      JsonObjectBuilder result = Json.createObjectBuilder();
//      result.add("accounts", accountArray);
//      return Response.ok()
//                     .entity(new ListResponse<String>(-1L, start, accounts.size(), pagedResult))
//                     .build();
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      // TODO: map it properly
      logger.debug(category, method, "Cause: " + cause.getClass().getName());
      if (cause instanceof oracle.iam.platform.authopss.exception.AccessDeniedException) {
        String message = "Permission denied";
        logger.throwing(category, method, cause);
        ErrorResponse response = new ErrorResponse(403);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.FORBIDDEN)
                       .entity(response.jsonify())
                       .build();
      }
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
  // Method:   getAccount
  /**
   ** REST service to get accounts for a particular OIM User whose login name
   ** is supplied as a path param.
   ** 
   ** Internal use only!
   **
   ** @param  accountId          ID of the account to retrieve. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
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
  @RolesAllowed({"internal"})
  @Path("jt/a/{accountId}")
  public Response getAccount(final @PathParam("accountId") long accountId,
                             final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "getAccount";
    logger.entering(category, method, "accountId", accountId);

    try {
      final String userName = security.getUserPrincipal().getName();
      Account account = this.facade.getAccountById(accountId, userName);
      logger.debug(category, method, "Retrieved result: " + account);

      if (account == null) {
        String message = "Account not found";
        ErrorResponse response = new ErrorResponse(404);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(response.jsonify())
                       .build();
      }
      logger.debug(category, method, "Going to beautify results");
      JsonObjectBuilder result = Json.createObjectBuilder();
      result.add("account", account.toString());
      return Response.ok()
                     .entity(result.build())
                     .build();
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      // TODO: map it properly
      logger.debug(category, method, "Cause: " + cause.getClass().getName());
      if (cause instanceof oracle.iam.platform.authopss.exception.AccessDeniedException) {
        String message = "Permission denied";
        logger.throwing(category, method, cause);
        ErrorResponse response = new ErrorResponse(403);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.FORBIDDEN)
                       .entity(response.jsonify())
                       .build();
      }
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
  // Method:   getAccount
  /**
   ** REST service to get accounts for a particular OIM User whose login name
   ** is supplied as a path param.
   ** 
   ** Internal use only!
   **
   ** @param  accountId          ID of the account to retrieve. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
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
  @RolesAllowed({"internal"})
  @Path("jt/ae/{accountId}")
  public Response getAccountEntity(final @PathParam("accountId") long accountId,
                             final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "getAccountEntity";
    logger.entering(category, method, "accountId", accountId);

    try {
      final String userName = security.getUserPrincipal().getName();
      AccountEntity account = this.facade.getAccountEntityById(accountId, userName);
      logger.debug(category, method, "Retrieved result: " + account);

      if (account == null) {
        String message = "Account not found";
        ErrorResponse response = new ErrorResponse(404);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.NOT_FOUND)
                       .entity(response.jsonify())
                       .build();
      }
      logger.debug(category, method, "Going to beautify results");
      JsonObjectBuilder result = Json.createObjectBuilder();
      result.add("account", Schema.marshalAccount(account));
      return Response.ok()
                     .entity(result.build())
                     .build();
    } catch (EJBException e) {
      Throwable cause = e.getCause();
      // TODO: map it properly
      logger.debug(category, method, "Cause: " + cause.getClass().getName());
      if (cause instanceof oracle.iam.platform.authopss.exception.AccessDeniedException) {
        String message = "Permission denied";
        logger.throwing(category, method, cause);
        ErrorResponse response = new ErrorResponse(403);
        response.description(message).type("").detail("");
        return Response.status(Response.Status.FORBIDDEN)
                       .entity(response.jsonify())
                       .build();
      }
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
  // Method:   processAccountEntity
  /**
   ** REST service to list accounts from and application instance using GET.
   ** Used as a dummy testing endpoint for the EJB method, therefore no paging
   ** is implemented.
   **
   ** Internal use only!
   **
   ** @param  appInstanceName    name of the application instance} to
   **                            retrieve accounts from. Cannot be null.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  inputEntity        instance of the {@link AccountEntity} object
   **                            to process.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   **
   ** @param  security           Guess.
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    a JAX-RS Response object containing the
   **                            lookup result.
   **                            <br>
   **                            A succesful response for this operation should
   **                            contain a status code of 200 and a list of 
   **                            {@link Identity}s
   **                            in the entity body, unsuccessful response will
   **                            hold bo entity and hava a Response.NOT_FOUND
   **                            status code.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException  if the OIM Entitlement referenced by key does
   **                            not exist.
   */
  @POST
  @Consumes("application/json")
  @Produces("application/json")
  @RolesAllowed({"administrator", "internal"})
  @Path("process/{appInstanceName}")
  public Response process(final @PathParam("appInstanceName") String appInstanceName,
                          final AccountEntity inputEntity,
                          final @Context SecurityContext security)
  throws NotFoundException {
    final String method = "process";
    logger.entering(category, method, "appInstanceName=", appInstanceName, "inputEntity=", inputEntity);

    try {
      final String userName = security.getUserPrincipal().getName();
      List<String> messages = new ArrayList<>();
      List<RequestMessage> processResults = this.facade.processAccountEntity(inputEntity, appInstanceName, userName);
//      List<String> processResults = this.facade.processAccountEntity(inputEntity, appInstanceName, userName);
      logger.exiting(category, method, processResults.size() + " results found, returning");
      for (RequestMessage mes : processResults) {
        messages.add(RequestBundle.string(mes.code(), mes.parameters()));
      }
      return Response.ok()
                     .entity(messages)
                     .build();
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
}