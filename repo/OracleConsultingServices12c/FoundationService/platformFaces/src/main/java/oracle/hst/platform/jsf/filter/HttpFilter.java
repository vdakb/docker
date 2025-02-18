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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Java Server Faces Feature

    File        :   HttpFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    HttpFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// abstract class HttpFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** The <code>HttpFilter</code> is abstract filter specifically for HTTP
 ** requests. It provides a convenient abstract
 ** {@link #apply(HttpServletRequest, HttpServletResponse, HttpSession, FilterChain)}
 ** method directly providing the HTTP servlet request, response and session, so
 ** that there's no need to cast them everytime in the
 ** {@link #doFilter(ServletRequest, ServletResponse, FilterChain)}
 ** implementation.
 ** <br>
 ** Also, default implementations of {@link #init(FilterConfig)} and
 ** {@link #destroy()} are provided, so that there's no need to implement them
 ** every time even when not really needed.
 ** <p>
 ** It's a bit the idea of using the convenient {@link HttpServlet} abstract
 ** servlet class instead of the barebones {@link Servlet} interface.
 ** <h3>Usage</h3>
 ** <p>
 ** To use it, just let your custom filter extend from {@link HttpFilter}
 ** instead of implement {@link Filter}.
 ** For example:
 ** <pre>
 **   &#64;WebFilter("/app/*")
 **   public class LoginFilter extends HttpFilter {
 **     &#64;Override
 **     public void doFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session, FilterChain chain)
 **       throws ServletException, IOException {
 **       if (session != null &amp;&amp; session.getAttribute("user") != null) {
 **         chain.doFilter(request, response);
 **       }
 **       else {
 **         Servlets.facesRedirect(request, response, "signin.jsf");
 **       }
 **    }
 **   }
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class HttpFilter implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

 	private FilterConfig config;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>HttpFilter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected HttpFilter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the {@link ServletContext} reference in which the caller is
   ** executing.
   **
   ** @return                    the {@link ServletContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Possible object is {@link ServletContext}.
   */
  public ServletContext context() {
    return this.config == null ? null : this.config.getServletContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns a string containing the value of the named initialization
   ** parameter, or <code>null</code> if the initialization parameter does not
   ** exist.
   **
   ** @param  name               a string specifying the name of the
   **                            initialization parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String parameter(final String name) {
    return this.config == null ? null : this.config.getInitParameter(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Filter)
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
   ** @param  config             a {@link FilterConfig} object containing the
   **                            filter's configuration and initialization
   **                            parameters.
   **                            <br>
   **                            Allowed object is {@link FilterConfig}.
   **
   ** @throws ServletException  if an exception occurs that interferes with the
   **                           filter's normal operation.
   */
  @Override
  public void init(final FilterConfig config)
    throws ServletException {

		this.config = config;
		initialize();
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
    this.config = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doFilter (Filter)
  /**
   ** This method is called by the container each time a request/response pair
   ** is passed through the chain due to a client request for a resource at the
   ** end of the chain. The {@link FilterChain} <code>chain</code> passed in to
   ** this method allows the {@link Filter} to pass on the request and response
   ** to the next entity in the chain.
   **
   ** @param  request            the {@link ServletRequest} object contains the
   **                            client's request.
   **                            <br>
   **                            Allowed object is {@link ServletRequest}.
   ** @param  response           the {@link ServletResponse} object contains the
   **                            filter's response.
   **                            <br>
   **                            Allowed object is {@link ServletResponse}.
   ** @param  chain              the {@link FilterChain} for invoking the next
   **                            filter or the resource.
   **                            <br>
   **                            Allowed object is {@link FilterChain}.
   **
   ** @throws IOException        if an I/O related error has occurred during the
   **                            processing.
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @Override
  public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
    throws IOException
    ,      ServletException {

		// simply cast and forward
    apply((HttpServletRequest)request, (HttpServletResponse)response, ((HttpServletRequest)request).getSession(false), chain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
	 ** Convenience method without {@link FilterConfig} parameter which will be
   ** called by {@link #init(FilterConfig)}.
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  protected abstract void initialize()
    throws ServletException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
	/**
	 ** Filter the HTTP request.
   ** <br>
   ** The session argument is <code>null</code> if there is no session.
   **
   ** @param  request            the {@link HttpServletRequest} object contains
   **                            the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the {@link HttpServletResponse} object contains
   **                            the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  session            the HTTP session, if any, else
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link HttpSession}.
   ** @param  chain              the {@link FilterChain} for invoking the next
   **                            filter or the resource.
   **                            <br>
   **                            Allowed object is {@link FilterChain}.
   **
   ** @throws IOException        if an I/O related error has occurred during the
   **                            processing.
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   **
	 ** @see    Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	protected abstract void apply(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session, final FilterChain chain)
    throws ServletException
    ,      IOException;
}