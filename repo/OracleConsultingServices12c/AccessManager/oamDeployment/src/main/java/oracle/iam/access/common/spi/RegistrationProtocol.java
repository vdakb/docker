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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   RegistrationProtocol.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RegistrationProtocol.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;

//////////////////////////////////////////////////////////////////////////////
// abstract class RegistrationProtocol
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>RegistrationProtocol</code> provide general methods to invoke web
 ** based services like on a <code>Servlet</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class RegistrationProtocol {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the context path mapped to the servlet */
  static final String CONTEXT_PATH            = "/rreg/rreg";

  /** the protocol each HTTP server is using */
  static final String PROTOCOL_DEFAULT        = "http";

  /** the protocol each HTTP server is using over SSL */
  static final String PROTOCOL_DEFAULT_SECURE = "https";

  static final String CHARSET_NAME            = "UTF-8";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Response
  // ~~~~~ ~~~~~~~~
  /**
   ** After receiving and interpreting a request message, a server responds with
   ** an HTTP response message.
   */
  public static class Response {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int    code;
    private final String phrase;

    private String       content;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Response</code> for the specified HTTP
     ** <code>code</code> and response phrase.
     **
     ** @param  code             the HTTP code of the <code>Response</code>.
     ** @param  phrase           the content of the <code>Response</code>.
     */
    public Response(final int code, final String phrase) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.code   = code;
      this.phrase = phrase;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: code
    /**
     ** Returns the HTTP code of the <code>Response</code>.
     **
     ** @return                    the HTTP code of the <code>Response</code>.
     */
    public final int code() {
      return this.code;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: phrase
    /**
     ** Returns the HTTP phrase of the <code>Response</code>.
     **
     ** @return                    the HTTP phrase of the <code>Response</code>.
     */
    public final String phrase() {
      return this.phrase;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: content
    /**
     ** Returns the HTTP content of the <code>Response</code>.
     **
     ** @return                    the HTTP content of the
     **                            <code>Response</code> if any.
     */
    public final String content() {
      return this.content;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final URL    endpoint;
  private final String payload;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class HTTP
  // ~~~~~ ~~~~
  /**
   ** ...
   */
  private static class HTTP extends RegistrationProtocol {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Create a new <code>HTTP</code> which will send the specified
     ** <code>payload</code> message to the Web Server.
     **
     ** @param  endpoint         the endpoint {@link URL} to access.
     ** @param  payload          the payload message.
     */
    public HTTP(final URL endpoint, final String payload) {
      // ensure inheritance
      super(endpoint, payload);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: transmit (RegistrationProtocol)
    /**
     ** Takes in the <code>serviceURL</code> string and sends the payload over
     ** the desired protocol to the server. Also receives the synchronous
     ** response and returns it for further processing.
     **
   ** @return                    the response from the Web Server on the
   **                            request.
     **
     ** @throws ServiceException if the POST method used to transmit could not
     **                          be configured correctly or fails at exceution
     **                          time in general.
     */
    @Override
    public final Response transmit()
      throws ServiceException {

      return executeRequest(createRequest());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class HTTPS
  // ~~~~~ ~~~~~
  /**
   ** ...
   */
  private static class HTTPS extends RegistrationProtocol {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Create a new <code>HTTPS</code> which will send the specified
     ** <code>payload</code> message to the Web Server.
     **
     ** @param  endpoint         the endpoint {@link URL} to access.
     ** @param  payload          the payload message.
     */
    public HTTPS(final URL endpoint, final String payload) {
      // ensure inheritance
      super(endpoint, payload);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: transmit (RegistrationProtocol)
    /**
     ** Takes in the <code>serviceURL</code> string and sends the payload over
     ** the desired protocol to the server. Also receives the synchronous
     ** response and returns it for further processing.
     **
     ** @return                  the response from the Web Server on the
     **                          request.
     **
     ** @throws ServiceException if the POST method used to transmit could not
     **                          be configured correctly or fails at exceution
     **                          time in general.
     */
    @Override
    public final Response transmit()
      throws ServiceException {

      return executeRequest(createRequest());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>RegistrationProtocol</code> which will send the
   ** specified <code>payload</code> message to the Web Server.
   **
   ** @param  endpoint           the endpoint {@link URL} to access.
   ** @param  payload            the payload request message.
   */
  private RegistrationProtocol(final URL endpoint, final String payload) {
    // ensure inheritance
    super();

    // initialize instance attribute
    this.endpoint = endpoint;
    this.payload  = payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create the appropriate protocol handler for the
   ** specified endpoint.
   **
   ** @param  serviceURL         the endpoint service address to invoke.
   ** @param  payload            the payload to transfer to the endpoint.
   **
   **  @return                   the {@link RegistrationProtocol} for this
   **                            operation.
   **
   ** @throws ServiceException   if the HTTP protocol could not be determind
   **                            or the URL is malformed in generall.
   */
  public static RegistrationProtocol create(final String serviceURL, final String payload)
    throws ServiceException {

    try {
      // convert the specified endpoint address in an URl to enforce exceptions
      // if its malformed
      final URL    endpoint = new URL(serviceURL);
      final String protocol = endpoint.getProtocol();
      if (PROTOCOL_DEFAULT.equalsIgnoreCase(protocol)) {
        return new HTTP(endpoint, payload);
      }
      else if (PROTOCOL_DEFAULT_SECURE.equalsIgnoreCase(protocol)) {
        return new HTTPS(endpoint, payload);
      }
      else {
        throw new ServiceException(ServiceError.HTTP_CONNECTION_PROTOCOL, protocol);
      }
    }
    catch (MalformedURLException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_MALFORMED_URL, serviceURL);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertFromStream
  public static String convertFromStream(final InputStream stream)
    throws ServiceException {

    final StringBuilder  builder = new StringBuilder();
    try {
      final BufferedReader reader  = new BufferedReader(new InputStreamReader(stream, CHARSET_NAME));
      String line = null;
      while ((line = reader.readLine()) != null) {
        builder.append(line + "\n");
      }
    }
    catch (IOException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_RESPONSE_CONVERT, e.getLocalizedMessage());
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  transmit
  /**
   ** Takes in the <code>serviceURL</code> string and sends the payload over
   ** the desired protocol to the server. Also receives the synchronous
   ** response and returns it for further processing.
   **
   ** @return                    the response from the Web Server on the
   **                            request.
   **
   ** @throws ServiceException   if the POST method used to transmit could not
   **                            be configured correctly or fails at exceution
   **                            time in general.
   */
  public abstract Response transmit()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:  createRequest
  /**
   ** Factory method to create a {@link HttpURLConnection} which is used to
   ** request that the origin server accept the entity enclosed in the request
   ** as a new subordinate of the resource identified by the Request-URI in the
   ** Request-Line.
   ** <br>
   ** POST is designed to allow a uniform method to cover the following
   ** functions:
   ** <ul>
   **   <li>Annotation of existing resources
   **   <li>Posting a message to a bulletin board, newsgroup, mailing list, or
   **       similar group of articles
   **   <li>Providing a block of data, such as the result of submitting a form,
   **       to a data-handling process
   **   <li>Extending a database through an append operation
   ** </ul>
   **
   **
   ** @return                    a configured {@link HttpURLConnection} ready
   **                            for use.
   **
   ** @throws ServiceException   if the POST method to create could not be
   **                            configured correctly.
   */
  protected HttpURLConnection createRequest()
    throws ServiceException {

    HttpURLConnection request = null;
    // setting request, content-type and charset
    try {
      final URL context = new URL(this.endpoint, CONTEXT_PATH);
      request = (HttpURLConnection)context.openConnection();
      request.setRequestMethod("POST");
      // use basic authentication if requested
		  request.setRequestProperty("Accept",       "application/xml");
      request.setRequestProperty("Content-Type", "application/xml");
      // use post mode
      request.setDoOutput(true);
      request.setAllowUserInteraction(false);
      request.getOutputStream().write(this.payload.getBytes(CHARSET_NAME), 0, this.payload.length());
    }
    catch (MalformedURLException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_MALFORMED_URL, CONTEXT_PATH);
    }
    catch (ProtocolException e) {
      if (request != null)
        request.disconnect();
      throw new ServiceException(ServiceError.HTTP_CONNECTION_PROTOCOL, "POST");
    }
    catch (IOException e) {
      if (request != null)
        request.disconnect();
      throw new ServiceException(e);
    }
    return request;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  executeRequest
  /**
   ** Executes the given HTTP method.
   **
   ** @param  request            the {@link HttpURLConnection} request to
   **                            execute.
   **
   ** @return                    the response of the exceution as a
   **                            {@link String}.
   **
   ** @throws ServiceException   if a protocol exception occurs.
   **                            Usually protocol exceptions cannot be recovered
   **                            from or if an I/O (transport) error occurs.
   */
  protected Response executeRequest(final HttpURLConnection request)
    throws ServiceException {

    // prevent bogus input
    if (request == null)
      throw new NullPointerException("request");

    Response response = null;
    try {
      request.connect();
      response = new Response(request.getResponseCode(), request.getResponseMessage());
      switch (response.code) {
        case HttpURLConnection.HTTP_OK              : // taking the response in the form of a stream and converting it to a
                                                      // string
                                                      response.content = convertFromStream(request.getInputStream());
                                                      break;
        default                                     : break;
      }
    }
    catch (SocketTimeoutException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_SOCKET_TIMEOUT, e.getMessage());
    }
    catch (UnknownHostException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_UNKNOWN_HOST, e.getMessage());
    }
    catch (IOException e){
      throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    finally {
      request.disconnect();
    }
    return response;
  }
}