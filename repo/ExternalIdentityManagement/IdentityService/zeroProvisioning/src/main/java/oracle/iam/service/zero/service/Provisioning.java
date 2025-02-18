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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.zero.service;

//import javax.annotation.security.RolesAllowed;

import javax.ejb.EJB;

import javax.json.JsonObject;

import javax.security.auth.login.LoginException;

import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.ForbiddenException;

import oracle.hst.platform.rest.UnauthorizedException;

import oracle.iam.identity.service.spi.Platform;

import oracle.iam.service.api.RequestAdapter;
import oracle.iam.service.api.RequestException;

import oracle.iam.identity.igs.model.Risk;
import oracle.iam.identity.igs.model.Entity;
import oracle.iam.identity.igs.model.AccountEntity;
import oracle.iam.identity.igs.model.ApplicationEntity;
import oracle.iam.identity.igs.model.EntitlementEntity;

////////////////////////////////////////////////////////////////////////////////
// class Provisioning
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The REST service endpoint apply provisioning transactions.
 ** <p>
 ** Intended functionality of <code>Provisioning</code> is to play the role of
 ** a service gatekeeper on top of the provisioning API provided by Identity
 ** Manager.
 ** <p>
 ** Any request that belongs to provisioning of any kind has to pass through
 ** this service. Bypass this service isn't an option.
 ** <p>
 ** In the service are all functionalities implementes checks for:
 ** <ol>
 **   <li>Completeness of a provisioning order.
 **   <li>Consistency of a provisioning order.
 **   <li>Integrity of a provisioning order.
 **   <li>Applicability of a provisioning order.
 ** </ol>
 ** <p>
 ** <strong>Completeness</strong>
 ** <strong>-------------</strong>
 ** <br>
 ** Completness ... TDB
 ** <p>
 ** <strong>Consistency</strong>
 **  ** <strong>-------------</strong>
 ** <br>
 ** The purpose of the consistency check is to check whether any partial
 ** information is available in the Identity Manager. Incorrectly named data or
 ** data that does not exist at all will inevitably result in the provisioning
 ** order being rejected.
 ** <p>
 ** <strong>Integrity</strong>
 ** <strong>-------------</strong>
 ** <br>
 ** Integrity ... TDB
 ** <p>
 ** <strong>Applicability</strong>
 ** <strong>-------------</strong>
 ** <br>
 ** Applicability of a provisioning order dertermins that there are no conflicts
 ** belonging to an account.
 ** <br>
 ** The first requirement that must be met for the acceptance of a provisioning
 ** order through this service is the existence of an identity to which the user
 ** account to be provisioned can be assigned.
 ** <br>
 ** Furthermore checking applicability includes the assignment of permissions
 ** that have already been assigned to a user account in the past or, on the
 ** other hand, the withdrawal of permissions that have not been assigned to the
 ** user account.
 ** <br>
 ** If any of the checks mentioned above fails the entire request is rejected
 ** with an appropriate error response.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(Provisioning.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Provisioning {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path of the resource endpoint */
  public static final String   PATH   = "request";
  
  private static final String  THIS   = Provisioning.class.getName();
  private static final Logger  LOGGER = Logger.create(THIS);

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @EJB
  private Platform platform;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provisioning</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Provisioning() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** REST service request to lookup a certain account represented by
   ** {@link PathParam} the <code>id</code> in the application represented by
   ** {@link PathParam} the <code>application</code> using GET.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   ** <p>
   ** The result of the REST service request is ...
   **
   **
   ** @param  application        the identifier of an application to revoke the
   **                            account from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the identifier of the account to revoke.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  security           the security context providing the user
   **                            information.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 200 (created) and a
   **                            {@link Response} in the entity body
   **                            (the account metadata).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException     if no entity is mapped with identifier
   **                               <code>application</code> or <code>id</code>
   **                               was not found at the Service Provider.
   ** @throws BadRequestException   if the value for a parameter or the request
   **                               body content is invalid.
   ** @throws ForbiddenException    if the principal isn't equiped with the role
   **                               <code>viewer</code>.
   ** @throws UnauthorizedException if the user belonging to the
   **                               {@link SecurityContext} cannot login to
   **                               Identity Manager.
   */
  @GET
  //@RolesAllowed("viewer")
  @Path("{application}/{id}")
  public Response lookup(final @PathParam("application") String application, final @PathParam("id") String id, final @Context SecurityContext security)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {
  
    final String method = "lookup";
    LOGGER.entering(THIS, method, application, id);
    try {
      // FIXME: only for testing purpose how the service reacts on REST exceptions
      if (StringUtility.equalIgnoreCase(application, "throwitaway")) {
        final BadRequestException e = BadRequestException.invalidValue("application");
        LOGGER.throwing(THIS, method, e);
        throw e;
      }

      // FIXME: only for testing purpose how the service reacts on REST exceptions
      if (StringUtility.equalIgnoreCase(id, "blue")) {
        final NotFoundException e = NotFoundException.of("id", "blue");
        LOGGER.throwing(THIS, method, e);
        throw e;
      }

      // FIXME: responed with a fake account for testing purpose
      final AccountEntity entity = Entity.account(id);
      entity.value("firstName", "Alfons");
      entity.value("lastName",  "Zitterbacke");
      entity.namespace(
        Entity.namespace("group")
          .element(
            CollectionUtility.list(
              Entity.entitlement(EntitlementEntity.Action.assign, Risk.none)
                .value("name", "cn=Dude,dc=example,dc=com")
            , Entity.entitlement(EntitlementEntity.Action.assign, Risk.none)
                .value("name", "cn=Other Dude,dc=example,dc=com")
            )
          )
      );
      return Response.status(Response.Status.OK).entity(entity).build();
    }
    finally {
      LOGGER.exiting(THIS, method, application, id);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service request to provision a certain account represented by the
   ** given {@link JsonObject} using POST.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  resource           an object that represents the resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link ApplicationEntity}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   ** @param  security           the security context providing the user
   **                            information.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
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
   ** @throws BadRequestException   if the value for a parameter or the request
   **                               body content is invalid.
   ** @throws ForbiddenException    if the principal isn't equiped with the role
   **                               <code>administrator</code>.
   ** @throws UnauthorizedException if the user belonging to the
   **                               {@link SecurityContext} cannot login to
   **                               Identity Manager.
   */
  @POST
  //@RolesAllowed("administrator")
  public Response create(final ApplicationEntity resource, final @Context UriInfo context, final @Context SecurityContext security)
    throws BadRequestException
    ,      ForbiddenException
    ,      UnauthorizedException {

    final String method = "create";
    LOGGER.entering(THIS, method, resource);
    try {
      // FIXME: mirror the resource into an entity without persisisting changes
      return Response.status(Response.Status.OK).entity(resource).build();
    }
    finally {
      LOGGER.exiting(THIS, method, resource);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** REST service request to provision a certain account represented by the
   ** given {@link JsonObject} using POST.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  application        the identifier of an application to provision
   **                            the account represented by
   **                            <code>resource</code> to.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  account            an object that represents the resource to
   **                            create.
   **                            <br>
   **                            Allowed object is {@link AccountEntity}.
   ** @param  context            the request context providing the query
   **                            parameter.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   ** @param  security           the security context providing the user
   **                            information.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
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
   ** @throws BadRequestException   if the value for a parameter or the request
   **                               body content is invalid.
   ** @throws ForbiddenException    if the principal isn't equiped with the role
   **                               <code>administrator</code>.
   ** @throws UnauthorizedException if the user belonging to the
   **                               {@link SecurityContext} cannot login to
   **                               Identity Manager.
   */
  @POST
  @Path("{application}")
  //@RolesAllowed("administrator")
  public Response create(final @PathParam("application") String application, final AccountEntity account, final @Context UriInfo context, final @Context SecurityContext security)
    throws ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {

    final String method = "create";
    LOGGER.entering(THIS, method, application, account);
    try {
      // FIXME: only for testing purpose how the service reacts on REST exceptions
      if (StringUtility.equalIgnoreCase(application, "throwitaway")) {
        final BadRequestException e = BadRequestException.invalidValue("application");
        LOGGER.throwing(THIS, method, e);
        throw e;
      }

      // FIXME: mirror the resource into an entity without persisisting changes
      final ApplicationEntity entity = Entity.application(application).element(account);
      return Response.status(Response.Status.OK).entity(entity).build();
    }
    finally {
      LOGGER.exiting(THIS, method, application, account);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** REST service request to delete a certain account using DELETE.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   **
   ** @param  application        the identifier of an application to revoke the
   **                            account from.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  id                 the identifier of the account to revoke.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  security           the security context providing the user
   **                            information.
   **                            <br>
   **                            Allowed object is {@link SecurityContext}.
   **
   ** @return                    an object abstracting the response obtained
   **                            from the server to this request.
   **                            <br>
   **                            A succesful response for this operation
   **                            contains a status code of 204 (no content).
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws NotFoundException     if no entity is mapped with identifier
   **                               <code>application</code> or <code>id</code>
   **                               was not found at the Service Provider.
   ** @throws BadRequestException   if the value for a parameter or the request
   **                               body content is invalid.
   ** @throws ForbiddenException    if the principal isn't equiped with the role
   **                               <code>administrator</code>.
   ** @throws UnauthorizedException if the user belonging to the
   **                               {@link SecurityContext} cannot login to
   **                               Identity Manager.
   */
  @DELETE
  @Path("{application}/{id}")
  //@RolesAllowed("administrator")
  public Response revoke(final @PathParam("application") String application, final @PathParam("id") String id, final @Context SecurityContext security)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {

    final String method = "revoke";
    LOGGER.entering(THIS, method, application, application, id);

    final RequestAdapter adapter = RequestAdapter.build(this.platform);

    try {
      // FIXME: only playground behavior
      if (security != null && (security.getUserPrincipal() == null || "anonymous".equals(security.getUserPrincipal().getName())))
        this.platform.loginAdministrator();
      else
        this.platform.login(security.getUserPrincipal().getName());

      // FIXME: only for testing purpose how the service reacts on REST exceptions
      try {
        adapter.lookupApplication(application);
      }
      catch (RequestException e) {
        final NotFoundException x = NotFoundException.of("application", application);
        LOGGER.throwing(THIS, method, x);
        throw x;
      }

      // FIXME: only for testing purpose how the service reacts on REST exceptions
      if (StringUtility.equalIgnoreCase(id, "green")) {
        final NotFoundException e = NotFoundException.of("account", "green");
        LOGGER.throwing(THIS, method, e);
        throw e;
      }

      return Response.status(Response.Status.NO_CONTENT).build();
    }
    catch (LoginException e) {
      throw UnauthorizedException.of(e.getMessage());
    }
    finally {
      LOGGER.exiting(THIS, method, application, application, id);
    }
  }
}