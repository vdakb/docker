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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Common shared runtime facilities

    File        :   CrossOrigin.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CrossOrigin.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.http;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// class CrossOrigin
// ~~~~~ ~~~~~~~~~~~
/**
 ** Implementation of the
 ** <a href="http://dev.w3.org/2006/waf/access-control/">cross-origin resource sharing</a>.
 ** <p>
 ** Cross-Origin Resource Sharing (CORS) is an HTTP-header based mechanism that
 ** allows a server to indicate any other origins (domain, scheme, or port) than
 ** its own from which a browser should permit loading of resources.
 ** <br>
 ** CORS also relies on a mechanism by which browsers make a <i>preflight</i>
 ** request to the server hosting the cross-origin resource, in order to check
 ** that the server will permit the actual request. In that <i>preflight</i>,
 ** the browser sends headers that indicate the HTTP method and headers that
 ** will be used in the actual request.
 ** <p>
 ** For security reasons, browsers restrict cross-origin HTTP requests initiated
 ** from scripts. For example, <code>XMLHttpRequest</code> and the Fetch API
 ** follow the same-origin policy. This means that a web application using those
 ** APIs can only request resources from the same origin the application was
 ** loaded from unless the response from other origins includes the right CORS
 ** headers.
 ** <p>
 ** The CORS mechanism supports secure cross-origin requests and data transfers
 ** between browsers and servers. Modern browsers use CORS in APIs such as
 ** <code>XMLHttpRequest</code> or Fetch to mitigate the risks of cross-origin
 ** HTTP requests.
 ** <p>
 ** A typical example is to use this filter to allow cross-domain
 ** <a href="http://cometd.org">cometd</a> communication using the standard long
 ** polling transport instead of the JSONP transport (that is less efficient and
 ** less reactive to failures).
 ** <p>
 ** This filter allows the following configuration parameters:
 ** <ul>
 **   <li><b>allowedOrigin</b>   - a comma separated list of origins that are
 **                                allowed to access the resources.
 **                                <br>
 **                                Default value is <b>*</b>, meaning all
 **                                origins.
 **   <li><b>allowedMethod</b>   - a comma separated list of HTTP methods that
 **                                are allowed to be used when accessing the
 **                                resources.
 **                                <br>
 **                                Default value is <b>GET,POST</b>.
 **   <li><b>allowedHeader</b>   - a comma separated list of HTTP headers that
 **                                are allowed to be specified when accessing
 **                                the resources.
 **                                <br>
 **                                Default values are:
 **                                <ul>
 **                                  <li><b>Origin</b>
 **                                  <li><b>Content-Type</b>
 **                                  <li><b>Accept</b>
 **                                  <li><b>X-Requested-With</b>
 **                                </ul>.
 **   <li><b>allowCredential</b> - a boolean indicating if the resource allows
 **                                requests with credentials.
 **                                <br>
 **                                Default value is <b>false</b>.
 **   <li><b>preflighAge</b>     - the number of seconds that preflight requests
 **                                can be cached by the client.
 **                                <br>
 **                                Default value is <b>1800</b> seconds, or 30
 **                                minutes.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CrossOrigin extends AbstractFilter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String ORIGIN_ANY          = "*";
  /**
   ** The Origin header indicates where the cross-origin request or preflight
   ** request originates from.
   */
  private static final String ORIGIN_HEADER       = "Origin";
  /**
   ** The Access-Control-Request-Method header indicates which method will be
   ** used in the actual request as part of the preflight request.
   */
  private static final String REQUEST_ACRM        = "Access-Control-Request-Method";
  /**
   ** The Access-Control-Request-Headers header indicates which headers will be
   ** used in the actual request as part of the preflight request.
   */
  private static final String REQUEST_ACRH        = "Access-Control-Request-Headers";
  /**
   ** The Access-Control-Allow-Origin header indicates whether a resource can be
   ** shared based by returning the value of the Origin request header,
   ** <code>*</code>, or <code>null</code> in the response.
   */
  private static final String RESPONSE_ACAO       = "Access-Control-Allow-Origin";
  /**
   ** The Access-Control-Allow-Methods header indicates, as part of the response
   ** to a preflight request, which methods can be used during the actual
   ** request.
   */
  private static final String RESPONSE_ACAM       = "Access-Control-Allow-Methods";
  /**
   ** The Access-Control-Allow-Headers header indicates, as part of the response
   ** to a preflight request, which header field names can be used during the
   ** actual request.
   */
  private static final String RESPONSE_ACAH       = "Access-Control-Allow-Headers";
  /**
   ** The Access-Control-Max-Age header indicates how long the results of a
   ** preflight request can be cached in a preflight result cache.
   */
  private static final String RESPONSE_ACMA       = "Access-Control-Max-Age";
  /**
   ** The Access-Control-Allow-Credentials header indicates whether the response
   ** to request can be exposed when the omit credentials flag is unset. When
   ** part of the response to a preflight request it indicates that the actual
   ** request can include user credentials.
   */
  private static final String RESPONSE_ACAC       = "Access-Control-Allow-Credentials";

  private static final String ALLOWED_ORIGIN      = "allowedOrigin";
  private static final String ALLOWED_METHOD      = "allowedMethod";
  private static final String ALLOWED_HEADER      = "allowedHeader";
  private static final String PREFLIGHT_MAX_AGE   = "preflightMaxAge";
  private static final String ALLOWED_CREDENTIAL  = "allowCredential";

  private static final List<String> SIMPLE_METHOD = Arrays.asList("GET", "POST", "HEAD");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean      any          = false;
  private boolean      credential   = true;
  private int          preflightAge = 0;
  private List<String> origin       = new ArrayList<String>();
  private List<String> method       = new ArrayList<String>();
  private List<String> header       = new ArrayList<String>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CrossOrigin</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CrossOrigin() {
   // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterInit
  /**
   ** Called by the web container to indicate that it is being placed into
   ** service. The servlet container calls the init method exactly once after
   ** instantiating the filter. The init method must complete successfully
   ** before the filter is asked to do any filtering work.
   ** <p>
   ** The web container cannot place the filter into service if the init method
   ** either
   ** <ol>
   **   <li>throws a ServletException
   **   <li>Does not return within a time period defined by the web container.
   ** </ol>
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @Override
  public final void afterInit()
    throws ServletException {

    String allowed = parameter(ALLOWED_ORIGIN);
    if (allowed == null)
      allowed = ORIGIN_ANY;
    String[] origin = allowed.split(",");
    for (String cursor : origin) {
      cursor = cursor.trim();
      if (cursor.length() > 0) {
        if (ORIGIN_ANY.equals(allowed)) {
          this.any = true;
          this.origin.clear();
          break;
        }
        else {
          this.origin.add(cursor);
        }
      }
    }
    allowed = parameter(ALLOWED_METHOD);
    if (allowed == null)
      allowed = "GET,POST";
    this.method.addAll(Arrays.asList(allowed.split(",")));

    allowed = parameter(ALLOWED_HEADER);
    if (allowed == null)
      allowed = "Origin,Content-Type,Accept,X-Requested-With";
    this.header.addAll(Arrays.asList(allowed.split(",")));

    allowed = parameter(ALLOWED_CREDENTIAL);
    if (allowed == null)
      allowed = "false";
    this.credential = Boolean.parseBoolean(allowed);

    allowed = parameter(PREFLIGHT_MAX_AGE);
    if (allowed == null)
      // default is 30 minutes
      allowed = "1800";
    try {
      this.preflightAge = Integer.parseInt(allowed);
    }
    catch (NumberFormatException x) {
      this.preflightAge = 1800;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destroy (Filter)
  /**
   ** Called by the web container to indicate to a filter that it is being taken
   ** out of service. This method is only called once all threads within the
   ** filter's {@link #doFilter(ServletRequest, ServletResponse, FilterChain)}
   ** method have exited or after a timeout period has passed. After the web
   ** container calls this method, it will not call the
   ** {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} method
   ** again on this instance of the filter.
   ** <p>
   ** This method gives the filter an opportunity to clean up any resources that
   ** are being held (for example, memory, file handles, threads) and make sure
   ** that any persistent state is synchronized with the filter's current state
   ** in memory.
   */
  @Override
  public final void destroy() {
    this.any          = false;
    this.preflightAge = 0;
    this.credential   = false;
    this.origin.clear();
    this.method.clear();
    this.header.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chain (overidden)
  /**
   ** This method is called by the container each time a request/response pair
   ** is passed through the chain due to a client request for a resource at the
   ** end of the chain. The {@link FilterChain} <code>chain</code> passed in to
   ** this method allows the {@link Filter} to pass on the request and response
   ** to the next entity in the chain.
   **
   ** @param  request            the {@link HttpServletRequest} object contains
   **                            the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the {@link HttpServletResponse} object contains
   **                            the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  chain              the {@link FilterChain} for invoking the next
   **                            filter or the resource.
   **                            <br>
   **                            Allowed object is {@link FilterChain}.
   **
   ** @throws Exception          if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @Override
  public final void chain(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
    throws Exception {

    
    String origin = request.getHeader(ORIGIN_HEADER);
    // is it a cross origin request ?
    if (origin != null && applicable(request)) {
      if (matches(origin)) {
        if (simpleRequest(request)) {
          response.setHeader(RESPONSE_ACAO, origin);
          if (this.credential)
            response.setHeader(RESPONSE_ACAC, "true");
        }
        else {
          preflightResponse(request, response, origin);
        }
      }
      else {
//        LOG.debug("Cross-origin request to " + request.getRequestURI() + " with origin " + origin + " does not match allowed origins " + allowedOrigins);
        ;
      }
    }
    super.chain(request, response, chain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicable
  /**
   ** Verify that the headers are applicable at the response the request belongs
   ** to.
   ** WebSocket clients such as Chrome 5 implement a version of the WebSocket
   ** protocol that does not accept extra response headers on the upgrade
   ** response.
   **
   ** @param  request            the {@link HttpServletRequest} send by a client
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    <code>true</code> if the reponse can be
   **                            rewritten.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  protected boolean applicable(final HttpServletRequest request) {
    // WebSocket clients such as Chrome 5 implement a version of the WebSocket
    // protocol that does not accept extra response headers on the upgrade
    // response
    return !("Upgrade".equalsIgnoreCase(request.getHeader("Connection"))
         && "WebSocket".equalsIgnoreCase(request.getHeader("Upgrade")));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   simpleRequest
  /**
   ** A method is said to be a simple method if it is a case-sensitive match for
   ** one of the following:
   ** <ul>
   **   <li>GET
   **   <li>HEAD
   **   <li>POST
   ** </ul>
   **
   ** @param  request            the {@link HttpServletRequest} send by a client
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    <code>true</code> if the method of the client
   **                            request is on e of
   **                            <ul>
   **                              <li>GET
   **                              <li>HEAD
   **                              <li>POST
   **                            </ul>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  private boolean simpleRequest(final HttpServletRequest request) {
    String method = request.getMethod();
    if (SIMPLE_METHOD.contains(method)) {
      // Section 6.1 says that for a request to be simple, custom request
      // headers must be simple.
      // Here for simplicity we just check if there is a
      // Access-Control-Request-Method header, which is required for preflight
      // requests
      // TODO: can be done better
      return request.getHeader(REQUEST_ACRM) == null;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preflightResponse
  /**
   ** Implementation of section 5.2
   **
   ** @param  request
   ** @param  response
   ** @param  origin
   */
  private void preflightResponse(final HttpServletRequest request, final HttpServletResponse response, final String origin) {
    // section 5.2.3 and 5.2.5
    boolean methodAllowed = allowedMethod(request);
    if (!methodAllowed)
      return;
    // section 5.2.4 and 5.2.6
    boolean headersAllowed = allowedHeader(request);
    if (!headersAllowed)
      return;
    // section 5.2.7
    response.setHeader(RESPONSE_ACAO, origin);
    if (this.credential)
      response.setHeader(RESPONSE_ACAC, "true");
    // section 5.2.8
    if (this.preflightAge > 0)
      response.setHeader(RESPONSE_ACMA, String.valueOf(this.preflightAge));
    // section 5.2.9
    response.setHeader(RESPONSE_ACAM, commify(this.method));
    // section 5.2.10
    response.setHeader(RESPONSE_ACAH, commify(this.header));
  }

  private boolean allowedMethod(final HttpServletRequest request) {
    final String method = request.getHeader(REQUEST_ACRM);
    return (method != null) ? this.method.contains(method) : false;
  }

  private boolean allowedHeader(final HttpServletRequest request) {
    String header = request.getHeader(REQUEST_ACRH);
    boolean result = true;
    if (header != null) {
      String[] headers = header.split(",");
      for (String cursor : headers) {
        boolean headerAllowed = false;
        for (String allowed : this.header) {
          if (cursor.trim().equalsIgnoreCase(allowed.trim())) {
            headerAllowed = true;
            break;
          }
        }
        if (!headerAllowed) {
          result = false;
          break;
        }
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   matches
  private boolean matches(final String origin) {
    if (this.any)
      return true;

    for (String cursor : this.origin) {
      if (cursor.equals(origin))
        return true;
    }
    return false;
  }

  private String commify(final List<String> strings) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < strings.size(); ++i) {
      if (i > 0)
        builder.append(",");
      builder.append(strings.get(i));
    }
    return builder.toString();
  }
}
