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

    File        :   AbstractHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.debug;

import java.util.List;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpServer;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.NotImplementedException;

import oracle.iam.system.simulation.rest.domain.ErrorResponse;
import oracle.iam.system.simulation.scim.domain.ListResponse;

import oracle.iam.system.simulation.scim.schema.Support;
import oracle.iam.system.simulation.scim.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class AbstractHandler
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** A generic handler which is invoked to process http exchanges.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractHandler<T extends Resource> implements HttpHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Response
  // ~~~~~ ~~~~~~~~
  /**
   ** A generic reponse result wrapper.
   */
  public class Response<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    
    final int             status;
    final ListResponse<T> result;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Response</code> with a status and a
     ** {@link ListResponse} of type <code>T</code>
     **
     ** @param  status           the HTTP status of the operation.
     **                          <br>
     **                          Allowed object is {@link HttpExchange}.
     ** @param  result           the collection of results of an operation.
     **                          <br>
     **                          Allowed object is {@link ListResponse}.
     */
    private Response(final int status, final ListResponse<T> result) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.status = status;
      this.result = result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractHandler</code> context handler.
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
  protected AbstractHandler(final HttpServer server, final String context) {
    // ensure inheritance
    super();

    // attach context handler to HTTP server
    server.createContext(context, this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:    handle (HttpHandler)
  /**
   ** Handle the given request and generate an appropriate response.
   ** See {@link HttpExchange} for a description of the steps involved in
   ** handling an exchange.
   **
   ** @param  t                  the exchange containing the request from the
   **                            client and used to send the response.
   **                            <br>
   **                            Allowed object is {@link HttpExchange}.
   **
   ** @throws IOException        if <code>t</code> is <code>null</code>.
   */
  @Override
  public final void handle(final HttpExchange t)
    throws IOException {

    try {
      final Response response = dispatch(t);
      if (response.result != null) {
        final String          message = Support.objectWriter().writeValueAsString(response.result);
        t.sendResponseHeaders(response.status, message.length());
        final OutputStream stream = t.getResponseBody();
        stream.write(message.getBytes());
        stream.close();
      }
      else {
        t.sendResponseHeaders(response.status, 0);
      }
    }
    catch (ProcessingException e) {
      final ErrorResponse error   = e.error();
      final String        response = Support.objectWriter().writeValueAsString(error);
      t.sendResponseHeaders(error.status().intValue(), response.length());
      final OutputStream stream = t.getResponseBody();
      stream.write(response.getBytes());
      stream.close();
    }
    catch (IOException e) {
      final String response= e.getLocalizedMessage();
      t.sendResponseHeaders(500, response.length());
      final OutputStream stream = t.getResponseBody();
      stream.write(response.getBytes());
      stream.close();
      throw e;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:    dispatch
  /**
   ** Handle the given request and generate an appropriate response.
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
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected final Response<T> dispatch(final HttpExchange t)
    throws ProcessingException {
      // add the header to avoid error:
      final Headers headers = t.getResponseHeaders();
      headers.add("Content-Type",                "application/scim+json");
      // no 'Access-Control-Allow-Origin' header is present on the requested
      // resource
      headers.add("Access-Control-Allow-Origin", "*");
      final String method = t.getRequestMethod();
      if ("GET".equals(method)) {
        return get(t);
      }
      else if ("PUT".equals(method)) {
        return put(t);
      }
      else if ("POST".equals(method)) {
        return post(t);
      }
      else if ("PATCH".equals(method)) {
        return patch(t);
      }
      else if ("DELETE".equals(method)) {
        return delete(t);
    }
    else  {
      throw new NotImplementedException();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    get
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
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   */
  protected abstract Response<T> get(final HttpExchange t);

  //////////////////////////////////////////////////////////////////////////////
  // Method:    put
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
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   */
  protected abstract Response<T> put(final HttpExchange t);

  //////////////////////////////////////////////////////////////////////////////
  // Method:    post
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
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   */
  protected abstract Response<T> post(final HttpExchange t);

  //////////////////////////////////////////////////////////////////////////////
  // Method:    patch
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
   ** @param collector           the result of the operation.
   **                            <br>
   **                            Allowed object is {@link ListResponse}.
   **
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   */
  protected abstract Response<T> patch(final HttpExchange t);

  //////////////////////////////////////////////////////////////////////////////
  // Method:    delete
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
   ** @return                    the process result of the operation to be
   **                            returned to the client.
   **                            <br>
   **                            Possible object is {@link Response} that wraps
   **                            a collection of results of type <code>T</code>.
   */
  protected abstract Response<T> delete(final HttpExchange t);

  //////////////////////////////////////////////////////////////////////////////
  // Method:    createResponse
  /**
   ** Factory method to create a process response
   **
   ** @param  status             the HTTP status of the process.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  resource           the resource element is of type <code>T</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @return                    the composed process result.
   */
  protected Response<T> createResponse(final int status, final T value) {
    if (value == null) {
      return new Response<T>(status, new ListResponse<T>(1, null, 1, 0));
    }
    else
      return createResponse(status, 1, 1, CollectionUtility.list(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    createResponse
  /**
   ** Factory method to create a process response
   **
   ** @param  status             the HTTP status of the process.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  total              the total number of results returned.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  start              the 1-based index of hte first result in the
   **                            current set of list results.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  resource           the {@link List} of results where
   **                            each element is of type <code>T</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the composed process result.
   **                            <br>
   **                            Possible object is {@link Response}}.
   */
  protected Response<T> createResponse(final int status, final int total, final int start, final List<T> resource) {
    return new Response<T>(status, new ListResponse<T>(total, resource, Integer.valueOf(start), Integer.valueOf(resource.size())));
  }
}