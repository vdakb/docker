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
    Subsystem   :   JEE Server

    File        :   AbstractHandler.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.uid.service;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.system.simulation.BadRequestException;
import oracle.iam.system.simulation.ProcessingException;
import oracle.iam.system.simulation.NotImplementedException;

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
abstract class AbstractHandler implements HttpHandler {

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

    try {
       t.sendResponseHeaders(dispatch(t), 0);
    }
    catch (Exception e) {
      final String response= e.getLocalizedMessage();
      t.sendResponseHeaders(500, response.length());
      final OutputStream stream = t.getResponseBody();
      stream.write(response.getBytes());
      stream.close();
      throw new IOException(e);
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
   **                            Possible object is <code>int</code>.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected final int dispatch(final HttpExchange t)
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
   **                            Possible object is <code>int</code>.
   **
   ** @throws ProcessingException if the request could not be handled anyway.
   */
  protected abstract int get(final HttpExchange t)
    throws ProcessingException;
}