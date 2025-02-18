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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AbstractRESTfulHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractRESTfulHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.Map;

import java.io.PrintStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLEncoder;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceOperation;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.type.APIServerContext;

////////////////////////////////////////////////////////////////////////////////
// class AbstractRESTfulHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Invokes the Runtime REST API to manage configuration stores in Oracle
 ** WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractRESTfulHandler extends AbstractServletHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

   public static final String CHARSET_NAME = "UTF-8";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String      contextURI;

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

    private int    code;
    private String message;
    private String content;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Response</code> with the specified HTTP response code
     ** and message.
     **
     ** @param  code             the HTTP resopnse code.
     ** @param  message          the HTTP resopnse message.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Response(final int code, final String message) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.code    = code;
      this.message = message;
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
    // Method: message
    /**
     ** Returns the HTTP message of the <code>Response</code>.
     **
     ** @return                    the HTTP message of the
     **                            <code>Response</code> if any.
     */
    public final String message() {
      return this.message;
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

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionlity
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character "," (comma).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                    the string representation of this instance.
     */
    @Override
    public String toString() {
      return String.format("code=%d,message=%s,content=%s", this.code, this.message, this.content);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>AbstractRESTfulHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   ** @param  contextURI         the URI of the REST API.
   ** @param  operation          the {@link ServiceOperation} to execute either
   **                            <ul>
   **                              <li>{@link ServiceOperation#create}
   **                              <li>{@link ServiceOperation#delete}
   **                              <li>{@link ServiceOperation#modify}
   **                            </ul>
   */
  protected AbstractRESTfulHandler(final ServiceFrontend frontend, final String contextURI, final ServiceOperation operation) {
    // ensure inheritance
    super(frontend);

    // initiallize instance attributes
    this.operation(operation);
    this.contextURI = contextURI;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertFromStream
  public static String convertFromStream(final InputStream stream)
    throws ServiceException {

    final StringBuilder builder = new StringBuilder();
    try {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, CHARSET_NAME));
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
  // Method:   invoke
  /**
   ** Serialize the given {@link Map} <code>payload</code> to a HTTP request
   ** query string.
   **
   ** @param  payload            the parameter mapping to pass to serialize.
   **
   ** @return                    a encoded HTTP request query string.
   **
   ** @throws ServiceException   if the payload could not be encoded correctly.
   */
  public static String queryEncode(final Map<String, Object> payload)
    throws  ServiceException {

    final StringBuilder query = new StringBuilder("?");
    try {
      int i = 0;
      for (Map.Entry<String, Object> cursor : payload.entrySet()) {
        if (i++ > 0)
          query.append(",");
        query.append(URLEncoder.encode(String.format("%s=%s", cursor.getKey(), cursor.getValue()), CHARSET_NAME));
      }
    }
    catch (UnsupportedEncodingException e) {
      throw new ServiceException(ServiceError.HTTP_CONNECTION_ENCODING, e.getLocalizedMessage());
    }
    return query.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invoke
  /**
   ** Invokes an operation on a REST API.
   **
   ** @param  request            the {@link HttpURLConnection} this task
   **                            will use to do the work.
   **
   ** @return                    the object returned by the operation, which
   **                            represents the result of invoking the operation
   **                            on the REST API specified.
   **
   ** @throws ServiceException   a communication problem occurred when talking
   **                            to the HTTP server.
   */
  protected Response invoke(final HttpURLConnection request)
    throws ServiceException {

    // prevent bogus input
    if (request == null)
      throw new NullPointerException("request");

    debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE, request.toString()));
    Response response = null;
    try {
      request.connect();
      response = new Response(request.getResponseCode(), request.getResponseMessage());
      switch (response.code) {
        case HttpURLConnection.HTTP_OK              : // obtain result and capture it in a String
                                                      // for later
                                                      response.content = convertFromStream(request.getInputStream());
                                                      debug(ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_SUCCESS, request.getInputStream()));
                                                      break;
        case HttpURLConnection.HTTP_BAD_REQUEST     : response.message  = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_400);
                                                      // the API may be responed with detailed information about the error
                                                      response.content = convertFromStream(request.getErrorStream());
                                                      break;
        case HttpURLConnection.HTTP_UNAUTHORIZED    : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_401);
                                                      break;
        case HttpURLConnection.HTTP_FORBIDDEN       : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_403);
                                                      break;
        case HttpURLConnection.HTTP_NOT_FOUND       : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_404);
                                                      break;
        case HttpURLConnection.HTTP_CLIENT_TIMEOUT  : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_408);
                                                      break;
        case HttpURLConnection.HTTP_INTERNAL_ERROR  : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_500);
                                                      break;
        case HttpURLConnection.HTTP_NOT_IMPLEMENTED : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_501);
                                                      break;
        case HttpURLConnection.HTTP_BAD_GATEWAY     : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_502);
                                                      break;
        case HttpURLConnection.HTTP_UNAVAILABLE     : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_503);
                                                      break;
        case HttpURLConnection.HTTP_GATEWAY_TIMEOUT : response.message = ServiceResourceBundle.string(ServiceError.HTTP_RESPONSE_CODE_504);
                                                      break;
        default                                     : break;
      }
    }
    catch (SocketTimeoutException e) {
      response = new Response(HttpURLConnection.HTTP_CLIENT_TIMEOUT, ServiceResourceBundle.format(ServiceError.HTTP_CONNECTION_SOCKET_TIMEOUT, e.getLocalizedMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.HTTP_CONNECTION_SOCKET_TIMEOUT, response.message);
    }
    catch (UnknownHostException e) {
      response = new Response(HttpURLConnection.HTTP_CLIENT_TIMEOUT, ServiceResourceBundle.format(ServiceError.HTTP_CONNECTION_UNKNOWN_HOST, e.getMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    catch (ConnectException e) {
      response = new Response(HttpURLConnection.HTTP_UNAVAILABLE, ServiceResourceBundle.format(ServiceError.HTTP_CONNECTION_UNAVAILABLE, e.getMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    catch (IOException e) {
      response = new Response(HttpURLConnection.HTTP_INTERNAL_ERROR, ServiceResourceBundle.format(ServiceMessage.OPERATION_INVOKE_ERROR, e.getLocalizedMessage()));
      if (failonerror())
        throw new ServiceException(ServiceError.UNHANDLED, e);
    }
    finally {
      request.disconnect();
    }
    return response;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** form-urlencoded requests.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>DELETE
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   ** @param  identifier         the name of the parameter send to the server
   **                            within the {@link URL}.
   ** @param  value              the value for <code>identifier</code> send to
   **                            the server within the {@link URL}.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected final HttpURLConnection connection(final APIServerContext context, final String requestMethod, final String identifier, final String value)
    throws  ServiceException {

    return connection(context, requestMethod, new String[][]{{identifier, value}});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** form-urlencoded requests.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>DELETE
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   ** @param parameter           the payload send to the server within the
   **                            {@link URL}.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected final HttpURLConnection connection(final APIServerContext context, final String requestMethod, final String[][] parameter)
    throws  ServiceException {

    final StringBuilder builder = new StringBuilder(this.contextURI);
    if (parameter != null && parameter.length > 0) {
      builder.append(SystemConstant.QUESTION);
      for (int i = 0; i < parameter.length; i++) {
        if (i > 0)
          builder.append('&');
        builder.append(String.format("%s=%s", parameter[i][0], parameter[i][1]));
      }
    }

    final URL contextURL = context.contextURL(builder.toString());
    // "application/x-www-form-urlencoded" is the default hence needs not to be
    // declared
    return prepare(contextURL, requestMethod, context.basicAuthentication(), "application/json", context.timeoutConnection(), context.timeoutResponse());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** form-urlencoded requests.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>DELETE
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   ** @param parameter           the payload send to the server within the
   **                            {@link URL}.
   ** @param payload             the payload to send with the request.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected final HttpURLConnection connection(final APIServerContext context, final String requestMethod, final String[][] parameter, final String payload)
    throws  ServiceException {

    final StringBuilder builder = new StringBuilder(this.contextURI);
    if (parameter != null && parameter.length > 0) {
      builder.append(SystemConstant.QUESTION);
      for (int i = 0; i < parameter.length; i++) {
        if (i > 0)
          builder.append('&');
        builder.append(String.format("%s=%s", parameter[i][0], parameter[i][1]));
      }
    }

    final URL contextURL = context.contextURL(builder.toString());
    final HttpURLConnection connection = prepare(contextURL, requestMethod, context.basicAuthentication(), "application/json", context.timeoutConnection(), context.timeoutResponse());
    PrintStream stream = null;
    try {
      stream = new PrintStream(connection.getOutputStream());
      stream.print(payload);
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
    finally {
      if (stream != null)
        stream.flush();
        stream.close();
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connection
  /**
   ** Factory method to create a {@link HttpURLConnection} for the purpose of
   ** content type application/json.
   **
   ** @param  context            the {@link APIServerContext} providing
   **                            information how to locate the requested
   **                            resources and the credentials required to
   **                            authenticate.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>PUT
   **                              <li>POST
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   ** @param payload             the payload send to the server in the content
   **                            body.
   **
   ** @return                    a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  protected final HttpURLConnection connection(final APIServerContext context, final String requestMethod, final String payload)
    throws  ServiceException {

    final HttpURLConnection connection = prepare(context.contextURL(this.contextURI), requestMethod, context.basicAuthentication(), "application/json", context.timeoutConnection(), context.timeoutResponse());
    PrintStream stream = null;
    try {
      stream = new PrintStream(connection.getOutputStream());
      stream.print(payload);
    }
    catch (IOException e) {
      throw new ServiceException(e);
    }
    finally {
      if (stream != null)
        stream.flush();
        stream.close();
    }
    return connection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepare
  /**
   ** Factory method to prepare a common {@link HttpURLConnection}.
   **
   ** @param  context            the {@link URL} to locate the requested
   **                            resources.
   ** @param  requestMethod      the HTTP method.
   **                            <ul>
   **                              <li>GET
   **                              <li>PUT
   **                              <li>POST
   **                              <li>HEAD
   **                              <li>TRACE
   **                              <li>DELETE
   **                              <li>OPTIONS
   **                            </ul>
   **                            are legal, subject to protocol restrictions.
   ** @param credential          the credentials required to authenticate.
   **
   ** @return                   a common {@link HttpURLConnection}.
   **
   ** @throws ServiceException  if the {@link HttpURLConnection} cannot be
   **                           created.
   */
  private HttpURLConnection prepare(final URL context, final String requestMethod, final String credential, final String contentType, final int connectTimeOutResponse, final int requestTimeOutResponse)
    throws  ServiceException {

    HttpURLConnection connection = null;
    try {
      connection = (HttpURLConnection)context.openConnection();
      connection.setRequestMethod(requestMethod);
      // use basic authentication if requested
      connection.setRequestProperty("Authorization", credential);
		  connection.setRequestProperty("Accept",       "application/json");
      if (contentType != null)
        connection.setRequestProperty("Content-Type", contentType);
      connection.setConnectTimeout(connectTimeOutResponse);
      connection.setReadTimeout(requestTimeOutResponse);
      // use post mode
      connection.setDoOutput(true);
      connection.setAllowUserInteraction(false);
    }
    catch (ProtocolException e) {
      if (connection != null)
        connection.disconnect();
      throw new ServiceException(ServiceError.HTTP_CONNECTION_PROTOCOL, requestMethod);
    }
    catch (IOException e) {
      if (connection != null)
        connection.disconnect();
      throw new ServiceException(e);
    }
    return connection;
  }
}