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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   eFBS SCIM Interface

    File        :   UserHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.debug;

import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import oracle.iam.system.simulation.scim.v2.schema.UserResource;

////////////////////////////////////////////////////////////////////////////////
// class UserHandler
// ~~~~~ ~~~~~~~~~~~
/**
 ** A handler which is invoked to process SCIM user resource exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class UserHandler extends AbstractHandler<UserResource> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UserHandler</code> context handler.
   **
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
  private UserHandler(final HttpServer server, final String context) {
    // ensure inheritance
    super(server, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:    processGet (AbstractHandler)
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
   */
  @Override
  public Response<UserResource> get(final HttpExchange t) {
    System.out.println(new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
    return createResponse(200, 1, 1, Provider.instance.accountSearch());
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
   **                            Possible object is {@link Response}.
   */
  @Override
  public Response<UserResource> post(final HttpExchange t) {
    System.out.println(new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
    return createResponse(200, Provider.dsteding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    put (AbstractHandler)
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
   */
  @Override
  public Response<UserResource> put(final HttpExchange t) {
    System.out.println(new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
    return createResponse(200, Provider.sstrecke);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    patch (AbstractHandler)
  /**
   ** Handle the given <code>PATCH</code>request and generate an appropriate
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
   */
  @Override
  public Response<UserResource> patch(final HttpExchange t) {
    System.out.println(new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
    return createResponse(204, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    delete (AbstractHandler)
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
   */
  @Override
  public Response<UserResource> delete(final HttpExchange t) {
    System.out.println(new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
    return createResponse(204, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attach
  /**
   ** Factory method to create a <code>UserHandler</code> context handler and
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
   **                            <code>UserHandler</code> context handler.
   **                            <br>
   **                            Allowed object is {@link HttpServer}.
   ** @param  contextURI         the context mapped to this handler by the HTTP
   **                            server.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  static void attach(final HttpServer server, final String context) {
    new UserHandler(server, context);
  }
}