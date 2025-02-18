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

    File        :   Router.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Router.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.view;

import java.util.Map;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

////////////////////////////////////////////////////////////////////////////////
// class Router
// ~~~~~ ~~~~~~
/**
 ** The <code>Router</code> is in charged of transforming the "pretty"
 ** URL from the browser and transform it to call html page in our folder's
 ** structure.
 ** <br>
 ** It's using the forward from the dispatcher to navigate to the html page
 ** without changing the URL in the browser.
 ** <p>
 ** Java Server Faces (JSF) is a very useful server-side rendering framework to
 ** create HTML UI in Java projects. It's part of the Java EE specifications.
 ** One of the drawbacks of Java Server Faces is the generated URLs for our
 ** pages, they follow as pattern the structure of the folders in the project.
 ** <p>
 ** From a UIX perspective, having URL's like
 ** <code>/pages/user/user-list.xhml</code> looks ugly, it's better to have an
 ** URL like '/users', '/user/new'. Also, from a security perspective, we are
 ** exposing our folder's structure to other people, and they can use that
 ** information to find a vulnerability.
 ** <p>
 ** That's why there are some third party libraries for JSF like pretty-faces,
 ** that allows to use friendly and nice URL's in our JSF application. However,
 ** they usually offer a bunch of functionalities (patterns, mapping path
 ** params, and more) that are not necesary in some of the applications.
 ** Therefore, we have created a very simple rewrite URL for JSF and avoid
 ** overload the application with things that not needed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Router implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String,String> route;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Router</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Router() {
   // ensure inheritance
    super();
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
  public final void init(final FilterConfig config)
    throws ServletException {

    this.route = Path.instance().page();
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
    this.route = null;
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

    HttpServletRequestWrapper wrapper   = new HttpServletRequestWrapper((HttpServletRequest)request);
    String                    pretty    = wrapper.getRequestURI().substring(wrapper.getContextPath().length());
    String                    pagepath  = pretty;
    String                    parameter = "";
    int                       query = pretty.indexOf('?');
    if (query != -1) {
      pagepath  = pretty.substring(query);
      parameter = pretty.substring(0, query);
    }

    if (this.route.containsKey(pretty)) {
      final String origin = this.route.get(pretty) + parameter;
      wrapper.getRequestDispatcher(origin).forward(request, response);
    }
    else {
      chain.doFilter(wrapper, response);
    }
  }
}