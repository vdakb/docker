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

    File        :   Catalog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Catalog.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.zero.service;

import javax.ejb.EJB;

import javax.security.auth.login.LoginException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import oracle.hst.platform.core.logging.Logger;

import oracle.hst.platform.rest.NotFoundException;
import oracle.hst.platform.rest.ForbiddenException;
import oracle.hst.platform.rest.BadRequestException;
import oracle.hst.platform.rest.UnauthorizedException;

import oracle.iam.identity.service.spi.Platform;

////////////////////////////////////////////////////////////////////////////////
// class Catalog
// ~~~~~ ~~~~~~~
/**
 ** The REST service endpoint query the Access Request Catalog.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Path(Catalog.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Catalog {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The path of the resource endpoint */
  public static final String   PATH   = "catalog";
  
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
   ** Constructs a <code>Catalog</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Catalog() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** REST service request to lookup a certain role represented by the
   ** {@link PathParam} <code>id</code> in Identity Manager using GET.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   ** <p>
   ** The result of the REST service request is ...
   **
   ** @param  id                 the identifier of a role to return.
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
   **                               <code>id</code> was not found at the
   **                               Service Provider.
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
  @Path("role/{id}")
  public Response role(final @PathParam("id") String id, final @Context SecurityContext security)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {
    
    // FIXME: Why the hell statement below can run without codebase grant
    //        Expected behavior is that the security manager kicks in and throws
    //        an Exception, unfortunately it does not
    /*
    <security-permission>
      <description>
        Allow reading the XL.HomeDir system property.
      </description>
      <security-permission-spec>
        grant {
          permission java.util.PropertyPermission "XL.HomeDir","read";
        };
      </security-permission-spec>
    </security-permission> 
    */
    final String home = System.getProperty("XL.HomeDir");
    return Response.status(Response.Status.OK).entity("{\"XL.HomeDir\":" + home + "}").build();
/*  
    final String method = "role";
    LOGGER.entering(THIS, method, id);
    try {
      // FIXME: only playground behavior
      if (security != null && security.getUserPrincipal() == null)
        this.platform.loginAdministrator();
      else
        this.platform.login(security.getUserPrincipal().getName());
    }
    catch (LoginException e) {
      throw UnauthorizedException.of(e.getMessage());
    }
    finally {
      this.platform.logout();
    }
    LOGGER.exiting(THIS, method,  id);
    return Response.status(Response.Status.OK).entity("{\"id\":" +id).build();
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** REST service request to lookup a certain application represented by the
   ** {@link PathParam} <code>id</code> in Identity Manager using GET.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   ** <p>
   ** The result of the REST service request is ...
   **
   ** @param  id                 the identifier of an application to return.
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
   **                               <code>id</code> was not found at the
   **                               Service Provider.
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
  @Path("application/{id}")
  public Response application(final @PathParam("id") String id, final @Context SecurityContext security)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {
  
    final String method = "application";
    LOGGER.entering(THIS, method, id);
    try {
      // FIXME: only playground behavior
      if (security != null && security.getUserPrincipal() == null)
        this.platform.loginAdministrator();
      else
        this.platform.login(security.getUserPrincipal().getName());
    }
    catch (LoginException e) {
      throw UnauthorizedException.of(e.getMessage());
    }
    finally {
      this.platform.logout();
    }
    LOGGER.exiting(THIS, method,  id);
    return Response.status(Response.Status.OK).entity("{\"id\":" + id + "}").build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** REST service request to lookup a certain entitlement represented by the
   ** {@link PathParam} <code>id</code> in Identity Manager using GET.
   ** <p>
   ** If the principal isn't equiped with the role <code>administrator</code>
   ** this method responed with a {@link ForbiddenException}.
   ** <p>
   ** The result of the REST service request is ...
   **
   ** @param  id                 the identifier of an entitlement to return.
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
   **                               <code>id</code> was not found at the
   **                               Service Provider.
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
  @Path("entitlement/{id}")
  public Response entitlement(final @PathParam("id") String id, final @Context SecurityContext security)
    throws NotFoundException
    ,      ForbiddenException
    ,      BadRequestException
    ,      UnauthorizedException {
  
    final String method = "role";
    LOGGER.entering(THIS, method, id);
    try {
      // FIXME: only playground behavior
      if (security != null && security.getUserPrincipal() == null)
        this.platform.loginAdministrator();
      else
        this.platform.login(security.getUserPrincipal().getName());
    }
    catch (LoginException e) {
      throw UnauthorizedException.of(e.getMessage());
    }
    finally {
      this.platform.logout();
    }
    LOGGER.exiting(THIS, method,  id);
    return Response.status(Response.Status.OK).entity("{\"id\":" + id + "}").build();
  }
}