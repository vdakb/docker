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

    File        :   AbstractFilter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractFilter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.platform.http;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFilter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** A Servlet Filter that enables AOP-style &quot;around&quot; advice for a
 ** {@link ServletRequest} via
 ** {@link #beforeHandle(HttpServletRequest, HttpServletResponse) beforeHandle},
 ** {@link #afterHandle(HttpServletRequest, HttpServletResponse) afterHandle},
 ** and {@link #afterCompletion(HttpServletRequest, HttpServletResponse, Exception) afterCompletion}
 ** hooks.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractFilter implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link FilterConfig} provided by the Servlet container at start-up.
   */
  protected FilterConfig config;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractFilter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractFilter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config
  /**
   ** Returns the servlet container specified {@link FilterConfig} instance
   ** provided at {@link #init(FilterConfig) startup}.
   **
   ** @return                    the servlet container specified
   **                            {@link FilterConfig} instance provided at
   **                            start-up.
   **                            <br>
   **                            Possible object is {@link FilterConfig}.
   */
  public final FilterConfig config() {
    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameter
  /**
   ** Returns the value for the named <code>init-param</code>, or
   ** <code>null</code> if there was no <code>init-param</code> specified by
   ** that name.
   **
   ** @param  name               the name of the <code>init-param</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value for the named
   **                            <code>init-param</code>, or <code>null</code>
   **                            if there was no <code>init-param</code>
   **                            specified by that name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected String parameter(final String name) {
    return this.config != null ? this.config.getInitParameter(name) : null;
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
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  public final void init(final FilterConfig config)
    throws ServletException {

    this.config = config;
    try {
      afterInit();
    }
    catch (Exception e) {
      if (e instanceof ServletException) {
        throw (ServletException)e;
      }
      else {
        throw new ServletException(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doFilter (Filter)
  /**
   ** This method is called by the container each time a request/response pair
   ** is passed through the chain due to a client request for a resource at the
   ** end of the chain. The {@link FilterChain} <code>chain</code> passed in to
   ** this method allows the {@link Filter} to pass on the request and response
   ** to the next entity in the chain.
   ** <p>
   ** Actually implements the chain execution logic, utilizing
   ** {@link #beforeHandle(HttpServletRequest, HttpServletResponse) before},
   ** {@link #afterHandle(HttpServletRequest, HttpServletResponse) after}, and
   ** {@link #afterCompletion(HttpServletRequest, HttpServletResponse, Exception) completion}
   ** hooks.
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

    final HttpServletRequest  req = (HttpServletRequest)request;
    final HttpServletResponse rsp = (HttpServletResponse)response;
    Exception exception = null;
    try {
      if (beforeHandle(req, rsp)) {
        chain(req, rsp, chain);
      }
      afterHandle(req, rsp);
    }
    catch (Exception e) {
      exception = e;
    }
    finally {
      cleanup(req, rsp, exception);
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
   ** <p>
   ** Default no-op implementation that can be overridden by subclasses for
   ** custom cleanup behavior.
   */
  @Override
  public void destroy() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterInit
  /**
   ** Method to be overridden by subclasses to perform initialization logic at
   ** start-up.
   ** <br>
   ** The {@link FilterConfig} will be accessible (and non-<code>null</code>) at
   ** the time this method is invoked via the {@link #config() config()} method.
   ** <p>
   ** <code>init-param</code> values may be conveniently obtained via the
   ** {@link #parameter(String)} method.
   **
   ** @throws Exception          if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  protected void afterInit()
    throws Exception {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterCompletion
  /**
   ** Called in all cases in a <code>finally</code> block even if
   ** {@link #beforeHandle beforeHandle} returns <code>false</code>or if an
   ** exception is thrown during filter chain processing.
   ** <br>
   ** Can be used for resource cleanup if so desired.
   ** <p>
   ** The default implementation does nothing (no-op) and exists as a template
   ** method for subclasses.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  exception          any exception thrown during
   **                            {@link #beforeHandle beforeHandle},
   **                            {@link #chain chain}, or
   **                            {@link #afterHandle afterHandle} execution, or
   **                            <code>null</code> if no exception was thrown
   **                            (i.e. the chain processed successfully).
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @SuppressWarnings("unused")
  public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Exception exception)
    throws ServletException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeHandle
  /**
   ** Returns <code>true</code> if the filter chain should be allowed to
   ** continue, <code>false</code> otherwise.
   ** <br>
   ** It is called before the chain is actually consulted/executed.
   ** <p>
   ** The default implementation returns <code>true</code> always and exists as
   ** a template method for subclasses.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   **
   ** @return                    <code>true</code> if the filter chain should be
   **                            allowed to continue, <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @SuppressWarnings("unused")
  protected boolean beforeHandle(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException {

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterHandle
  /**
   ** Allows 'post' advice logic to be called, but only if no exception occurs
   ** during filter chain execution.
   ** <br>
   ** That is, if {@link #chain chain} throws an exception, this method will
   ** never be called. Be aware of this when implementing logic.
   ** <br>
   ** Most resource 'cleanup' behavior is often done in the
   ** {@link #afterCompletion(HttpServletRequest, HttpServletResponse, Exception) afterCompletion(request,response,exception)}
   ** implementation, which is guaranteed to be called for every request, even
   ** when the chain processing throws an Exception.
   ** <p>
   ** The default implementation does nothing (no-op) and exists as a template
   ** method for subclasses.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @SuppressWarnings("unused")
  protected void afterHandle(final HttpServletRequest request, final HttpServletResponse response)
    throws ServletException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chain
  /**
   ** Actually executes the specified filter chain by calling
   ** <code>chain.doFilter(request,response);</code>.
   ** <p>
   ** Can be overridden by subclasses for custom logic.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
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
  protected void chain(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
    throws Exception {

    chain.doFilter(request, response);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Executes cleanup logic in the <code>finally</code> code block in the
   ** {@link #chain(HttpServletRequest, HttpServletResponse, FilterChain) chain}
   ** implementation.
   ** <p>
   ** This implementation specifically calls
   ** {@link #afterCompletion(HttpServletRequest, HttpServletResponse, Exception) after}
   ** as well as handles any exceptions properly.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  cause              any exception thrown during the
   **                            {@code FilterChain} or
   **                            {@link #beforeHandle beforeHandle},
   **                            {@link #chain chain}, or
   **                            {@link #afterHandle afterHandle} execution, or
   **                            <code>null</code> if no exception was thrown
   **                            (i.e. the chain processed successfully).
   **
   ** @throws IOException        if an I/O related error has occurred during the
   **                            processing.
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  protected void cleanup(final HttpServletRequest request, final HttpServletResponse response, final Exception cause)
    throws IOException
    ,      ServletException {

    Exception exception = cause;
    try {
      afterCompletion(request, response, exception);
    }
    catch (Exception e) {
      if (exception == null) {
        exception = e;
      }
    }
    if (exception != null) {
      if (exception instanceof ServletException) {
        throw (ServletException)exception;
      }
      else if (exception instanceof IOException) {
        throw (IOException)exception;
      }
      else {
        throw new ServletException(exception);
      }
    }
  }
}