/*
    Oracle Deutschland GmbH

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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   RoleHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RoleHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.util.stream.Collectors;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.NotImplementedException;

import oracle.iam.system.simulation.resource.ServiceBundle;

import oracle.iam.system.simulation.rest.schema.Resource;

import oracle.iam.system.simulation.apigee.persistence.Account;
import oracle.iam.system.simulation.apigee.persistence.Provider;

////////////////////////////////////////////////////////////////////////////////
// class RoleHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** A handler which is invoked to process REST user role exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class RoleHandler extends AbstractHandler<Resource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleHandler</code> context handler.
   **
   ** @param  server             the HTTP server that handles this context
   **                            handler.
   **                            <br>
   **                            Allowed object is {@link HttpServer}.
   ** @param  contextURI         the context mapped to this handler by the HTTP
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private RoleHandler(final HttpServer server, final String context) {
    // ensure inheritance
    super(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:    get (AbstractHandler)
  /**
   ** Handle the given <code>GET</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> get(final HttpExchange t)
    throws ProcessingException {

    final String[] segments  = t.getRequestURI().getPath().split("/");
    final String   last      = segments[segments.length - 1];
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
//    final Resource result = Server.CONTEXT_ROLE.endsWith(last) ? Provider.instance.userroleSearch() : Provider.instance.userroleLookup(last);
//    return createResponse(200, result);
    return createResponse(200, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    post (AbstractHandler)
  /**
   ** Handle the given <code>POST</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response} for type
   **                            {@link User}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> post(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    switch (path[type - 1]) {
      case Server.CONTEXT_USER      : return createResponse(200, userroleAssign(path[type - 2], t.getRequestBody()));
      default                       : throw new NotImplementedException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    put (AbstractTransactionHandler)
  /**
   ** Handle the given <code>PUT</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> put(final HttpExchange t)
    throws ProcessingException {

    throw new NotImplementedException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    delete (AbstractTransactionHandler)
  /**
   ** Handle the given <code>DELETE</code>request and generate an appropriate
   ** response.
   ** <br>
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  @Override
  public Response<Resource> delete(final HttpExchange t)
    throws ProcessingException {

    final String[] path = t.getRequestURI().getPath().split("/");
    final int      type = path.length;
    // verify if a known resource is requested means the last part of the path
    // is not the request context mapped to this handler
    switch(path[type - 2]) {
      case Server.CONTEXT_USER      : Provider.instance.userroleRevoke(path[type - 5], path[type - 3], path[type - 1]);
                                      break;
      default                       : throw new NotImplementedException();
    }
    return createResponse(204, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attach
  /**
   ** Factory method to create a <code>RoleHandler</code> context handler and
   ** maps the handler created to the context URI {@link Provider#CONTEXT_USER}.
   ** <br>
   ** A HttpContext represents a mapping from a URI path to the exchange handler
   ** on a HttpServer.
   ** <br>
   ** Once created, all requests received by the server for the path will be
   ** handled by calling the given handler object. The context is identified by
   ** the path, and can later be removed from the server using this with the
   ** removeContext(String) method.
   ** <p>
   ** The path specifies the root URI path for this context. The first character
   ** of path <b>must</b> be '/'.
   ** <p>
   ** The class overview describes how incoming request URIs are mapped to
   ** {@link HttpContext} instances.
   **
   ** @param  server             the HTTP server that handles the
   **                            <code>RoleHandler</code> context handler.
   **                            <br>
   **                            Allowed object is {@link HttpServer}.
   ** @param  contextURI         the context mapped to this handler by the HTTP
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  static void attach(final HttpServer server, final String context) {
    new RoleHandler(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userroleAssign
  /**
   ** Assigns a userrole to a grantee.
   **
   ** @param  tenant             the identifier of the organization to associate
   **                            the resource to create with.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  role               the name of the userrole to populate the
   **                            members for.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link InputStream} to unmarshall from the
   **                            request.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws ProcessingException if the database operation fails.
   */
  private Resource userroleAssign(final String role, final InputStream resource)
    throws ProcessingException {

    // the id of the user to assign is in the POST body stream
    String   result = new BufferedReader(new InputStreamReader(resource)).lines().collect(Collectors.joining("\n"));
    try {
      result = URLDecoder.decode(result, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, "member", Account.RESOURCE));
    }

    // check wellformness
    final String[] member = result.split("=");
    if (member.length != 2)
      throw BadRequestException.invalidValue(ServiceBundle.string(ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID, "member", Account.RESOURCE));

    return Provider.instance.userroleAssign(role, member[1]);
  }
}