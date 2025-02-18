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

    File        :   CacheControl.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    CacheControl.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.hst.platform.jsf.Servlet;

////////////////////////////////////////////////////////////////////////////////
// class CacheControl
// ~~~~~ ~~~~~~~~~~~~
/**
 ** This filter will control the cache-related headers of the response.
 ** <br>
 ** Cache-related headers have a major impact on performance (network bandwidth
 ** and server load) and user experience (up to date content and non-expired
 ** views).
 ** <p>
 ** By default, when no initialization parameters are specified, the filter will
 ** instruct the client (generally, the webbrowser) to <strong>not</strong>
 ** cache the response. This is recommended on dynamic pages with stateful forms
 ** with a <code>javax.faces.ViewState</code> hidden field. If such a page were
 ** cached, and the enduser navigates to it by webbrowser's back button, and
 ** then re-submits it, then the enduser would face a
 ** <a href="https://stackoverflow.com/a/3642969/157882"><code>ViewExpiredException</code></a>.
 ** <p>
 ** However, on stateless resources, caching the response would be beneficial.
 ** Set the expire time to the same time as you'd like to use as refresh
 ** interval of the resource, which can be 10 seconds (to avoid F5-madness on
 ** resources which are subject to quick changes), but also minutes or even
 ** hours, days or weeks. For example, a list of links, a news page, a
 ** JS/CSS/image file, etc.
 ** <p>
 ** Any sane server and client adheres the following rules as to caching:
 ** <ul>
 **   <li>When the enduser performs page-to-page navigation, or when the enduser
 **       selects URL in address bar and presses enter key again, while the
 **       resource is cached, then the client will just load it from the cache
 **       without hitting the server in any way.
 **   <li>Or when the cache is expired, or when the enduser does a soft-refresh
 **       by pressing refresh button or F5 key, then:
 **       <ul>
 **         <li>When the <code>ETag</code> or <code>Last-Modified</code> header
 **             is present on cached resource, then the client will perform a
 **             so-called conditional GET request with
 **             <code>If-None-Match</code> or <code>If-Modified-Since</code>
 **             headers. If the server responds with HTTP status 304
 **             ("not modified") along with the updated cache-related headers,
 **             then the client will keep the resource in cache and expand its
 **             expire time based on the headers.
 **             <br>
 **             <b>Note</b>:
 **             <br>
 **             <code>ETag</code> takes precedence over
 **             <code>Last-Modified</code> when both are present and
 **             consequently <code>If-None-Match</code> takes precedence over
 **             <code>If-Modified-Since</code> when both are present.
 **         <li>When those headers are <strong>not</strong> present, then the
 **             behavior is the same as during a hard-refresh.
 **       </ul>
 **   <li>Or when the resource is not cached, or when the enduser does a
 **       hard-refresh by pressing <code>Ctrl</code> key along with refresh
 **       button or F5, then the webbrowser will perform a fresh new request
 **       and purge any cached resource.
 ** </ul>
 ** <b>Important notice</b>:
 ** <br>
 ** This filter automatically skips JSF resources, such as the ones served by
 ** <code>&lt;h:outputScript&gt;</code>,
 ** <code>&lt;h:outputStylesheet&gt;</code>, <code>@ResourceDependency</code>,
 ** etc. Their cache-related headers are namely
 ** <a href="https://stackoverflow.com/q/15057932/157882">already</a> controlled
 ** by the <code>ResourceHandler</code> implementation. In Mojarra and MyFaces,
 ** the default expiration time is 1 week (604800000 milliseconds), which can be
 ** configured by a <code>web.xml</code> context parameter with the following
 ** name and a value in milliseconds, e.g. <code>3628800000</code> for 6 weeks:
 ** <ul>
 **   <li>Mojarra: <code>com.sun.faces.defaultResourceMaxAge</code>
 **   <li>MyFaces: <code>org.apache.myfaces.RESOURCE_MAX_TIME_EXPIRES</code>
 ** </ul>
 ** <p>
 ** It would not make sense to control their cache-related headers with this
 ** filter as they would be overridden anyway.
 **
 ** <h3>Configuration</h3>
 ** <p>
 ** This filter supports the <code>expires</code> initialization parameter which
 ** must be a number between 0 and 999999999 with optionally the 'w', 'd', 'h',
 ** 'm' or 's' suffix standing for respectively 'week', 'day', 'hour', 'minute'
 ** and 'second'. For example: '6w' is 6 weeks. The default suffix is 's'. So,
 ** when the suffix is omitted, it's treated as seconds. For example: '86400' is
 ** 86400 seconds, which is effectively equal to '86400s', '1440m', '24h' and
 ** '1d'.
 ** <p>
 ** Imagine that you've the following resources:
 ** <ul>
 **   <li>All <code>/forum/*</code> pages: cache 10 seconds.
 **   <li>All <code>*.pdf</code> and <code>*.zip</code> files: cache 2 days.
 **   <li>All other pages: no cache.
 ** </ul>
 ** <p>
 ** Then you can configure the filter as follows (filter name is fully free to
 ** your choice, but keep it sensible):
 ** <pre>
 **   &lt;filter&gt;
 **     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 **     &lt;filter-class&gt;oracle.hst.platform.jsf.filter.CacheControl&lt;/filter-class&gt;
 **   &lt;/filter&gt;
 **   &lt;filter&gt;
 **     &lt;filter-name&gt;cache10seconds&lt;/filter-name&gt;
 **     &lt;filter-class&gt;oracle.hst.platform.jsf.filter.CacheControlr&lt;/filter-class&gt;
 **     &lt;init-param&gt;
 **       &lt;param-name&gt;expires&lt;/param-name&gt;
 **       &lt;param-value&gt;10s&lt;/param-value&gt;
 **     &lt;/init-param&gt;
 **   &lt;/filter&gt;
 **   &lt;filter&gt;
 **     &lt;filter-name&gt;cache2days&lt;/filter-name&gt;
 **     &lt;filter-class&gt;oracle.hst.platform.jsf.filter.CacheControl&lt;/filter-class&gt;
 **     &lt;init-param&gt;
 **       &lt;param-name&gt;expires&lt;/param-name&gt;
 **       &lt;param-value&gt;2d&lt;/param-value&gt;
 **     &lt;/init-param&gt;
 **  &lt;/filter&gt;
 **
 **  &lt;filter-mapping&gt;
 **     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 **     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 **   &lt;/filter-mapping&gt;
 **   &lt;filter-mapping&gt;
 **     &lt;filter-name&gt;cache10seconds&lt;/filter-name&gt;
 **     &lt;url-pattern&gt;/forum/*&lt;/url-pattern&gt;
 **   &lt;/filter-mapping&gt;
 **   &lt;filter-mapping&gt;
 **     &lt;filter-name&gt;cache2days&lt;/filter-name&gt;
 **     &lt;url-pattern&gt;*.pdf&lt;/url-pattern&gt;
 **     &lt;url-pattern&gt;*.zip&lt;/url-pattern&gt;
 **   &lt;/filter-mapping&gt;
 ** </pre>
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Üut the more specific URL patterns in the end of filter mappings. Due to the
 ** way how filters work, there's unfortunately no simple way to skip the filter
 ** on <code>/*</code> when e.g. <code>*.pdf</code> is matched. You can always
 ** map the no cache filter specifically to <code>FacesServlet</code> if you
 ** intend to disable caching on <b>all</b> JSF pages. Here's an example
 ** assuming that you've configured the <code>FacesServlet</code> with a servlet
 ** name of <code>java-server-faces</code>:
 ** <pre>
 **   &lt;filter-mapping&gt;
 **     &lt;filter-name&gt;noCache&lt;/filter-name&gt;
 **     &lt;servlet-name&gt;java-server-faces&lt;/servlet-name&gt;
 **   &lt;/filter-mapping&gt;
 ** </pre>
 **
 ** <h3>Actual headers</h3>
 ** <p>
 ** If the <code>expires</code> init param is set with a value which represents
 ** a time larger than 0 seconds, then the following headers will be set:
 ** <ul>
 **   <li><code>Cache-Control: public,max-age=[expiration time in seconds],must-revalidate</code>
 **   <li><code>Expires: [expiration date of now plus expiration time in seconds]</code>
 ** </ul>
 ** <p>
 ** If the <code>expires</code> init param is absent, or set with a value which
 ** represents a time equal to 0 seconds, then the following headers will be
 ** set:
 ** <ul>
 **   <li><code>Cache-Control: no-cache,no-store,must-revalidate</code>
 **   <li><code>Expires: [expiration date of 0]</code>
 **   <li><code>Pragma: no-cache</code>
 ** </ul>
 **
 ** <h3>JSF development stage</h3>
 ** <p>
 ** To speed up development, caching by this filter is <b>disabled</b> when JSF
 ** project stage is set to <code>Development</code> as per
 ** {@link Servlet#facesDevelopment(javax.servlet.ServletContext)}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CacheControl extends HttpFilter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private long expires = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CacheControl</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CacheControl() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize  (HttpFilter)
  /**
   ** Called by the web container to indicate that it is being placed into
   ** service. The servlet container calls the init method exactly once after
   ** instantiating the filter. The init method must complete successfully
   ** before the filter is asked to do any filtering work.
   ** <p>
   ** The web container cannot place the filter into service if the initialize
   ** method either
   ** <ol>
   **   <li>throws a ServletException
   **   <li>Does not return within a time period defined by the web container.
   ** </ol>
   **
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @Override
  protected void initialize()
    throws ServletException {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply  (HttpFilter)
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
   **
   ** @throws IOException        if an I/O related error has occurred during the
   **                            processing.
   ** @throws ServletException   if an exception occurs that interferes with the
   **                            filter's normal operation.
	 */
  protected void apply(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session, final FilterChain chain)
    throws ServletException
    ,      IOException {

 		if (!Servlet.facesResourceRequest(request)) {
			Servlet.cacheHeader(response, expires);
		}
		chain.doFilter(request, response);
  }

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

		// don't cache during development
    if (Servlet.facesDevelopment(context())) {
			return;
		}

    String expiresParam = parameter("expires");
    if (expiresParam != null) {
			if (!expiresParam.matches("[0-9]{1,9}[wdhms]?")) {
				throw new ServletException(String.format("ERROR_EXPIRES", expiresParam));
			}
			String[] parts = expiresParam.split("(?=[wdhms])");
			long number = Long.parseLong(parts[0]);
      this.expires = number;
    }
  }
}