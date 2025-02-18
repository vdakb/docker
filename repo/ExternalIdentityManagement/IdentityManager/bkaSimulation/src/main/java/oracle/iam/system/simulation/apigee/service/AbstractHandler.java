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

    File        :   AbstractHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.service;

import com.sun.net.httpserver.Headers;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.nio.charset.Charset;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import oracle.hst.foundation.utility.Base64Decoder;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.NotImplementedException;

import oracle.iam.system.simulation.rest.schema.Resource;

import oracle.iam.system.simulation.scim.schema.Support;

import oracle.iam.system.simulation.apigee.schema.Error;

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
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  Principal principal = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Principal
  // ~~~~~ ~~~~~~~~~
  /**
   ** A generic principal request wrapper.
   */
  public class Principal {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
    
    final String username;
    final String password;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Principal</code> form the authentication token
     **
     ** @param  token            the HTTP authentication token.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Principal(final String authn) {
      if (authn == null) {
        this.username = null;
        this.password = null;
      }
      else {
        // split by "Basic"
        int    sp = authn.indexOf (' ');
        byte[] b  = Base64Decoder.decode(authn.substring(sp + 1).getBytes());
        String cr = new String (b);
        sp = cr.indexOf (':');
        this.username = cr.substring (0, sp);
        this.password = cr.substring (sp + 1);
      }
    }
  }

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

    final int status;
    final T   result;

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
    public Response(final int status, final T result) {
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
  public AbstractHandler(final HttpServer server, final String context) {
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

    this.principal = new Principal(t.getRequestHeaders().getFirst("Authorization"));
    try {
      final Response<T> response = dispatch(t);
      if (response.result != null) {
        final String message = response.result.toString();
        final byte[] encoded = message.getBytes(Charset.forName("UTF-8"));
        t.sendResponseHeaders(response.status, encoded.length);
        final OutputStream stream = t.getResponseBody();
        stream.write(encoded);
        stream.close();
      }
      else {
        t.sendResponseHeaders(response.status, 0);
      }
    }
    catch (ProcessingException e) {
      final Error  error    = new Error(e.error());
      final String response = Support.objectWriter().writeValueAsString(error);
      t.sendResponseHeaders(e.error().status().intValue(), response.length());
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
  // Method:    splitQuery
  public static Map<String, String> splitQuery(final String query)
    throws ProcessingException {

    final Map<String, String> mapping = new LinkedHashMap<String, String>();
    if (!StringUtility.isEmpty(query)) {
      try {
        final String[]            pairs   = query.split("&");
        for (String pair : pairs) {
          final int idx = pair.indexOf("=");
          mapping.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
      }
      catch (UnsupportedEncodingException e) {
        throw BadRequestException.invalidEncoding("Encoding not supprted");
      }
    }
    return mapping;
  }

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
    headers.add("Content-Type",                "application/json; charset=UTF-8");
    // no 'Access-Control-Allow-Origin' header is present on the requested
    // resource
    headers.add("Access-Control-Allow-Origin", "*");
    final String method = t.getRequestMethod();
    switch(method) {
      case "GET"    : return get(t);
      case "PUT"    : return put(t);
      case "POST"   : return post(t);
      case "DELETE" : return delete(t);
      default       : throw new NotImplementedException();
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
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected abstract Response<T> get(final HttpExchange t)
    throws ProcessingException;

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
   **                            a result of type {@link Resource}.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected abstract Response<T> post(final HttpExchange t)
    throws ProcessingException;

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
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected abstract Response<T> put(final HttpExchange t)
    throws ProcessingException;

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
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected abstract Response<T> delete(final HttpExchange t)
    throws ProcessingException;

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
    return new Response<T>(status, value);
  }
}