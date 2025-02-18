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

    File        :   Servlet.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Servlet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf;

import java.lang.reflect.Array;

import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.HashSet;
import java.util.Collection;

import java.util.concurrent.TimeUnit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.faces.FactoryFinder;

import javax.faces.context.FacesContext;

import javax.faces.webapp.FacesServlet;

import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Servlet
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Collection of utility methods for the Servlet API in general. Most of them
 ** are internally used by {@link Faces}, however they may also be useful in a
 ** "plain vanilla" servlet or servlet filter.
 ** <p>
 ** There are as of now also five special methods related to JSF without needing
 ** a {@link FacesContext}:
 ** <ul>
 **   <li>The {@link #facesLifecycle(ServletContext)} which returns the JSF
 **       lifecycle, allowing you a.o. to programmatically register JSF
 **       application's phase listeners.
 **   <li>The {@link #facesAjaxRequest(HttpServletRequest)} which is capable of
 **       checking if the current request is a JSF ajax request.
 **   <li>The {@link #facesResourceRequest(HttpServletRequest)} which is capable
 **       of checking if the current request is a JSF resource request.
 **   <li>The
 **       {@link #facesRedirect(HttpServletRequest, HttpServletResponse, String, Object...)}
 **       which is capable of distinguishing JSF ajax requests from regular
 **       requests and altering the redirect logic on it, exactly like as
 **       {@code ExternalContext#redirect(String)} does.
 **       <br>
 **       In other words, this method behaves exactly the same as
 **       {@link Faces#redirect(String, Object...)}.
 **   <li>The {@link #facesDevelopment(ServletContext)} which is capable of
 **       checking if the current JSF application configuration is set to
 **       development project stage.
 ** </ul>
 ** Those methods can be used in for example a servlet filter.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Servlet {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

	private static final String      FACES_AJAX_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"%s\"></redirect></partial-response>";
  private static final Set<String> FACES_AJAX_HEADERS  = new HashSet<String>(){{ add("partial/ajax");add("partial/process");}};

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static Boolean development;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Servlet</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Servlet()" and enforces use of the public method below.
   */
  private Servlet() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesLifecycle
  /**
	 ** Returns the {@link Lifecycle} associated with current Faces application.
   **
   ** @param  context            the involved servlet context.
   **                            <br>
   **                            Allowed object is {@link ServletContext}.
   **
   ** @return                    the {@link Lifecycle} associated with current
   **                            Faces application.
   **                            <br>
   **                            Possible object is {@link Lifecycle}.
   **
   ** @see    LifecycleFactory#getLifecycle(String)
	 */
	public static Lifecycle facesLifecycle(final ServletContext context) {
		String lifecycleId = coalesce(context.getInitParameter(FacesServlet.LIFECYCLE_ID_ATTR), LifecycleFactory.DEFAULT_LIFECYCLE);
		return ((LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY)).getLifecycle(lifecycleId);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesRedirect
  /**
	 ** Sends a temporary (302) JSF redirect to the given URL, supporting JSF ajax
   ** requests.
   ** <br>
   ** This does exactly the same as {@link Faces#redirect(String, Object...)},
   ** but without the need for a {@link FacesContext}. The major advantage is
   ** that you can perform the job inside a servlet filter or even a plain
   ** vanilla servlet, where the {@link FacesContext} is normally not available.
   ** <br>
   ** This method also recognizes JSF ajax requests which requires a special XML
   ** response in order to successfully perform the redirect.
	 ** <p>
	 ** If the given URL does <b>not</b> start with <code>http://</code>,
   ** <code>https://</code> or <code>/</code>, then the request context path
   ** will be prepended, otherwise it will be the unmodified redirect URL. So,
   ** when redirecting to another page in the same web application, always
   ** specify the full path from the context root on (which in turn does not
   ** need to start with <code>/</code>).
	 ** <pre>
	 **   Servlet.facesRedirect(request, response, "some.jsf");
	 ** </pre>
	 ** You can use {@link String#format(String, Object...)} placeholder
   ** <code>%s</code> in the redirect URL to represent placeholders for any
   ** request parameter values which needs to be URL-encoded. Here's a concrete
   ** example:
	 ** <pre>
	 **   Servlet.facesRedirect(request, response, "some.jsf?foo=%s&amp;bar=%s", foo, bar);
	 ** </pre>
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  response           The involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  url                The URL to redirect the current response to.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  parameter          the request parameter values which you'd like
   **                            to put URL-encoded in the given URL.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @throws UncheckedIOException whenever something fails at I/O level.
	 */
	public static void facesRedirect(final HttpServletRequest request, final HttpServletResponse response, final String url, final Object... parameter) {
		String redirectURL = prepareRedirectURL(request, url, parameter);

		try {
			if (facesAjaxRequest(request)) {
				noneCacheHeader(response);
				response.setContentType("text/xml");
				response.setCharacterEncoding(StandardCharsets.UTF_8.name());
				response.getWriter().printf(FACES_AJAX_RESPONSE, redirectURL);
			}
			else {
				response.sendRedirect(redirectURL);
			}
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesAjaxRequest
  /**
	 ** Returns <code>true</code> if the given HTTP servlet request is a JSF ajax
   ** request.
   ** <br>
   ** This does exactly the same as {@link Faces#ajaxRequest()}, but then
   ** without the need for a {@link FacesContext}.
   ** <br>
   ** The major advantage is that you can perform the job inside a servlet
   ** filter, where the {@link FacesContext} is normally not available.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    <code>true</code> if the given HTTP servlet
   **                            request is a JSF ajax request.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean facesAjaxRequest(final HttpServletRequest request) {
		return FACES_AJAX_HEADERS.contains(request.getHeader("Faces-Request"));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesResourceRequest
  /**
	 ** Returns <code>true</code> if the given HTTP servlet request is a JSF
   ** resource request. E.g. this request will trigger the JSF
   ** {@link ResourceHandler} for among others CSS/JS/image resources.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    <code>true</code> if the given HTTP servlet
   **                            request is a JSF resource request.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
	 ** @see ResourceHandler#RESOURCE_IDENTIFIER
	 */
	public static boolean facesResourceRequest(final HttpServletRequest request) {
		return requestURI(request).startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesDevelopment
  /**
	 ** Returns <code>true</code> if we're in JSF development stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>Development</code>.
   **
   ** @param  context            the {@link ServletContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link ServletContext}.
   **
   ** @return                    <code>true</code> if we're in development
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean facesDevelopment(final ServletContext context) {
		if (development == null) {
			development = ProjectStage.Development.name().equals(context.getInitParameter(ProjectStage.PROJECT_STAGE_PARAM_NAME));
		}
		return development;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestURI
  /**
	 ** Returns the HTTP request URI, regardless of any forward or error dispatch.
   ** <br>
   ** This is the part after the domain in the request URL, including the
   ** leading slash.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @return                    the HTTP request URI, regardless of any forward
   **                            or error dispatch.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getRequestURI()
	 ** @see    RequestDispatcher#FORWARD_REQUEST_URI
	 ** @see    RequestDispatcher#ERROR_REQUEST_URI
	 */
	public static String requestURI(final HttpServletRequest request) {
		return coalesce((String)request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI), (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI), request.getRequestURI());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cacheHeader
  /**
	 ** Set the cache headers.
   ** <br>
   ** If the <code>expires</code> argument is larger than 0 seconds, then the
   ** following headers will be set:
   ** <ul>
   **   <li><code>Cache-Control: public,max-age=[expiration time in seconds],must-revalidate</code>
   **   <li><code>Expires: [expiration date of now plus expiration time in seconds]</code>
   ** </ul>
   ** Else the method will delegate to
   ** {@link #noneCacheHeader(HttpServletResponse)}.
   **
   ** @param  response           the HTTP servlet response to set the headers
   **                            on.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  expires            the expire time in seconds (not milliseconds!).
   **                            <br>
   **                            Allowed object is <code>long</code>.
	 */
	public static void cacheHeader(final HttpServletResponse response, final long expires) {
		if (expires > 0) {
			response.setHeader("Cache-Control", "public,max-age=" + expires + ",must-revalidate");
			response.setDateHeader("Expires", System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expires));
      // explicitly set pragma to prevent container from overriding it
			response.setHeader("Pragma", "");
		}
		else {
			noneCacheHeader(response);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noneCacheHeader
  /**
	 ** Set the no-cache headers.
   ** <br>
   ** The following headers will be set:
   ** <ul>
   **   <li><code>Cache-Control: no-cache,no-store,must-revalidate</code>
   **   <li><code>Expires: [expiration date of 0]</code>
   **   <li><code>Pragma: no-cache</code>
   ** </ul>
   **
   ** @param  response           the HTTP servlet response to set the headers
   **                            on.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 */
	public static void noneCacheHeader(final HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
		response.setDateHeader("Expires", 0);
    // backwards compatibility for HTTP 1.0
		response.setHeader("Pragma", "no-cache");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestCookieValue
  /**
	 ** Returns the value of the HTTP request cookie associated with the given
   ** name.
   ** <br>
   ** The value is implicitly URL-decoded with a charset of UTF-8.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  name               the HTTP request cookie name.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 **
   ** @return                    the value of the HTTP request cookie associated
   **                            with the given name.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
   **
   ** @see    HttpServletRequest#getCookies()
	 */
	public static String requestCookieValue(final HttpServletRequest request, final String name) {
		final Cookie cookie = requestCookie(request, name);
    return (cookie == null) ? null : decodeURL(cookie.getValue());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestCookie
  /**
	 ** Returns the value of the HTTP request cookie associated with the given
   ** name.
   ** <br>
   ** The value is implicitly URL-decoded with a charset of UTF-8.
    **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  name               the HTTP request cookie name.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 **
   ** @return                    the value of the HTTP request cookie associated
   **                            with the given name.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
   **
   ** @see    HttpServletRequest#getCookies()
	 */
	public static Cookie requestCookie(final HttpServletRequest request, final String name) {
		final Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseCookie
  /**
	 ** Add a cookie with given name, value and maxage to the HTTP response.
   ** <br>
   ** The cookie value will implicitly be URL-encoded with UTF-8 so that any
   ** special characters can be stored.
   ** <br>
   ** The cookie will implicitly be set in the domain and path of the current
   ** request URL.
   ** <br>
   ** The cookie will implicitly be set to HttpOnly as JavaScript is not
   ** supposed to manipulate server-created cookies.
   ** <br>
   ** The cookie will implicitly be set to secure when the current request is a
   ** HTTPS request.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 ** @param  name               the cookie name.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  value              the cookie value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  maxAge             the maximum age of the cookie, in seconds.
   **                            If this is <code>0</code>, then the cookie will
   **                            be removed.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name and path must be exactly the same as
   **                            it was when the cookie was created. If this is
   **                            <code>-1</code> then the cookie will become a
   **                            session cookie and thus live as long as the
   **                            established HTTP session.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
   **
   ** @see HttpServletResponse#addCookie(Cookie)
	 */
	public static void responseCookie(final HttpServletRequest request, final HttpServletResponse response, final String name, final String value, final int maxAge) {
		responseCookie(request, response, name, value, requestHostName(request), null, maxAge);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseCookie
  /**
	 ** Remove the cookie with given name and path from the HTTP response.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** The name and path must be exactly the same as it was when the cookie was
   ** created.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 ** @param  name               the cookie name.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  path               the cookie path.
   **                            If this is <code>/</code>, then the cookie is
   **                            available in all pages of the webapp.
   **                            <br>
   **                            If this is <code>/somespecificpath</code>, then
   **                            the cookie is only available in pages under the
   **                            specified path.
   **
   ** @see HttpServletResponse#addCookie(Cookie)
	 */
	public static void responseCookie(final HttpServletRequest request, final HttpServletResponse response, final String name, final String path) {
		responseCookie(request, response, name, null, path, 0);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseCookie
  /**
	 ** Add a cookie with given name, value, path and maxage to the HTTP response.
   ** <br>
   ** The cookie value will implicitly be URL-encoded with UTF-8 so that any
   ** special characters can be stored.
   ** <br>
	 * The cookie will implicitly be set in the domain of the current request URL.
   ** <br>
	 ** The cookie will implicitly be set to HttpOnly as JavaScript is not
   ** supposed to manipulate server-created cookies.
   ** <br>
	 ** The cookie will implicitly be set to secure when the current request is a
   ** HTTPS request.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 ** @param  name               the cookie name.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  value              the cookie value.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  path               the cookie path.
   **                            If this is <code>/</code>, then the cookie is
   **                            available in all pages of the webapp.
   **                            <br>
   **                            If this is <code>/somespecificpath</code>, then
   **                            the cookie is only available in pages under the
   **                            specified path.
   ** @param  maxAge             the maximum age of the cookie, in seconds.
   **                            If this is <code>0</code>, then the cookie will
   **                            be removed.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name and path must be exactly the same as
   **                            it was when the cookie was created. If this is
   **                            <code>-1</code> then the cookie will become a
   **                            session cookie and thus live as long as the
   **                            established HTTP session.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
   **
   ** @see HttpServletResponse#addCookie(Cookie)
	 */
	public static void responseCookie(final HttpServletRequest request, final HttpServletResponse response, final String name, final String value, final String path, final int maxAge) {
		responseCookie(request, response, name, value, requestHostName(request), path, maxAge);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   responseCookie
  /**
	 ** Add a cookie with given name, value, path and maxage to the HTTP response.
   ** <br>
   ** The cookie value will implicitly be URL-encoded with UTF-8 so that any
   ** special characters can be stored.
   ** <br>
	 * The cookie will implicitly be set in the domain of the current request URL.
   ** <br>
	 ** The cookie will implicitly be set to HttpOnly as JavaScript is not
   ** supposed to manipulate server-created cookies.
   ** <br>
	 ** The cookie will implicitly be set to secure when the current request is a
   ** HTTPS request.
   **
   ** @param  request            the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 ** @param  name               the cookie name.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  value              the cookie value.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  domain             the cookie domain.
   **                            <br>
   **                            You can use <code>.example.com</code> (with a
   **                            leading period) if you'd like the cookie to be
   **                            available to all subdomains of the domain.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            You cannot set it to a different domain.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  path               the cookie path.
   **                            If this is <code>/</code>, then the cookie is
   **                            available in all pages of the webapp.
   **                            <br>
   **                            If this is <code>/somespecificpath</code>, then
   **                            the cookie is only available in pages under the
   **                            specified path.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  maxAge             the maximum age of the cookie, in seconds.
   **                            If this is <code>0</code>, then the cookie will
   **                            be removed.
   **                            <br>
   **                            <b>Note</b>:
   **                            <br>
   **                            The name and path must be exactly the same as
   **                            it was when the cookie was created. If this is
   **                            <code>-1</code> then the cookie will become a
   **                            session cookie and thus live as long as the
   **                            established HTTP session.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
   **
   ** @see HttpServletResponse#addCookie(Cookie)
	 */
	public static void responseCookie(final HttpServletRequest request, final HttpServletResponse response, final String name, final String value, final String domain, final String path, final int maxAge) {
		Cookie cookie = new Cookie(name, encodeURL(value));
    cookie.setDomain(domain);
		if (path != null) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(true);
		cookie.setSecure(secure(request));
		response.addCookie(cookie);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirectPermanent
  /**
   ** Sends a permanent (301) redirect to the given URL.
   ** 
   ** @param  response           the involved HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
	 ** @param  url                The URL to redirect the current response to.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @see    HttpServletResponse#setStatus(int)
   ** @see    HttpServletResponse#setHeader(String, String)
   */
	public static void redirectPermanent(HttpServletResponse response, String url) {
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location", url);
		response.setHeader("Connection", "close");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referrer
  /**
   ** Returns the the referrer of the request.
   **
   ** @param request             the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the referrer of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @see    HttpServletRequest#getHeader(String)
	 */
	public static String referrer(final HttpServletRequest request) {
    // yes, typo is set in stone
    // see https://en.wikipedia.org/wiki/HTTP_referer#Etymology
    return request.getHeader("Referer");
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secure
  /**
	 ** Returns <code>true</code> if connection is secure, <code>false</code>
   ** otherwise.
   ** <br>
   ** This method will first check if {@link HttpServletRequest#isSecure()}
   ** returns <code>true</code>, and if not <code>true</code>, check if the
   ** <code>X-Forwarded-Proto</code> is present and equals to
   ** <code>https</code>.
   **
   ** @param request             the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if connection is secure,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @see     HttpServletRequest#isSecure()
	 */
	public static boolean secure(HttpServletRequest request) {
		return request.isSecure() || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestHostName
  /**
	 ** Returns the HTTP request hostname.
   ** <br>
   ** This is the entire domain, without any scheme and slashes.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** This value is extracted from the request URL, not from
   ** {@link HttpServletRequest#getServerName()} as its outcome can be
   ** influenced by proxies.
   **
   ** @param request             the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the HTTP request hostname.
   **
   ** @throws IllegalArgumentException when the URL is malformed.
   **                                  <br>
   **                                  This is however unexpected as the request
   **                                  would otherwise not have hit the server
   **                                  at all.
   **
   ** @see    HttpServletRequest#getRequestURL()
	 */
	public static String requestHostName(final HttpServletRequest request) {
		try {
			return new URL(request.getRequestURL().toString()).getHost();
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestDomainURL
  /**
	 ** Returns the HTTP request domain URL.
   ** <br>
   ** This is the URL with the scheme and domain, without any trailing slash.
   **
   ** @param request             the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the HTTP request domain URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IllegalArgumentException when the URL is malformed.
   **                                  <br>
   **                                  This is however unexpected as the request
   **                                  would otherwise not have hit the server
   **                                  at all.
   **
   ** @see    HttpServletRequest#getRequestURL()
	 */
	public static String requestDomainURL(final HttpServletRequest request) {
		try {
			final URL    url = new URL(request.getRequestURL().toString());
			final String fqu = url.toString();
			return fqu.substring(0, fqu.length() - url.getPath().length());
		}
		catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeURL
  /**
	 ** URL-encode the given string using UTF-8.
   **
   ** @param  string             the string to be URL-ncoded using UTF-8.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the given string, URL-encoded using UTF-8, or
   **                            <code>null</code> if <code>null</code> was
   **                            given.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
	 */
	public static String encodeURL(final String string) {
		if (string == null) {
			return null;
		}

		try {
			return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
		}
		catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("UTF-8 is apparently not supported on this platform.", e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeURL
  /**
	 ** URL-decode the given string using UTF-8.
   **
   ** @param  string             the string to be URL-decode using UTF-8.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the given string, URL-decode using UTF-8, or
   **                            <code>null</code> if <code>null</code> was
   **                            given.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws UnsupportedOperationException when this platform does not support
   **                                       UTF-8.
	 */
	public static String decodeURL(final String string) {
    // prevent bogus inout
		if (string == null) {
			return null;
		}

		try {
			return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
		}
		catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException("UTF-8 is apparently not supported on this platform.", e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   prepareRedirectURL
	/**
	 ** Helper method to prepare redirect URL.
   ** <br>
   ** Package-private so that {@link Faces} can also use it.
   **
   ** @param request             the involved HTTP servlet request.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  url                The URL to redirect the current response to.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  parameter          the request parameter values which you'd like
   **                            to put URL-encoded in the given URL.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the string representation of the redirected
   **                            URL.
   **                            <br>
   **                            Possible object is {@link String}.
	 */
	static String prepareRedirectURL(final HttpServletRequest request, final String url, final Object... parameter) {
		String redirectURL = url;

		if (!startsWithOneOf(url, "http://", "https://", "/")) {
			redirectURL = request.getContextPath() + "/" + url;
		}

		if (empty(parameter)) {
			return redirectURL;
		}

		final Object[] encoded = new Object[parameter.length];
		for (int i = 0; i < parameter.length; i++) {
			encoded[i] = (parameter[i] instanceof String) ? encodeURL((String) parameter[i]) : parameter[i];
		}

		return String.format(redirectURL, encoded);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coalesce
  /**
	 ** Returns the first non-<code>null</code> object of the argument list, or
   ** <code>null</code> if there is no such element.
   ** <br>
   ** Package-private so that {@link Faces} can also use it.
   **
   ** @param  <T>                the generic object type.
   **                            <br>
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  value              the argument list of objects to be tested for
   **                            non-<code>null</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the first non-<code>null</code> object of the
   **                            argument list, or <code>null</code> if there is
   **                            no such element.
   **                            <br>
   **                            Possible object is <code>T</code>.
	 */
	@SafeVarargs
	static <T> T coalesce(final T... value) {
		for (T cursor : value) {
			if (cursor != null) {
				return cursor;
			}
		}
		return null;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oneOf
  /**
	 ** Returns <code>true</code> if the given object equals one of the given
   ** objects.
   **
   ** @param  <T>                the generic object type.
   **                            <br>
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
	 ** @param  value              the object to be checked if it equals one of
   **                            the given objects.
   **                            <br>
   **                            Allowed object is <code>T</code>.
	 ** @param  option             the argument list of objects to be tested for
   **                            equality.
   **                            <br>
   **                            Allowed object is array of <code>T</code>.
   **
   ** @return                    <code>true</code> if the given object equals
   **                            one of the given objects.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	@SafeVarargs
	public static <T> boolean oneOf(final T value, final T... option) {
		for (Object other : option) {
			if (Objects.equals(value, other)) {
				return true;
			}
		}
		return false;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWithOneOf
  /**
	 ** Returns <code>true</code> if the given string starts with one of the given
   ** prefixes.
   **
	 ** @param  value              the object to be checked if it equals one of
   **                            the given prefixes.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  prefix             the argument list of prefixes to be checked.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the given string starts
   **                            with one of the given prefixes.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean startsWithOneOf(final String value, String... prefix) {
		for (String cursor : prefix) {
			if (value.startsWith(cursor)) {
				return true;
			}
		}
		return false;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWithOneOf
  /**
	 ** Returns <code>true</code> if the given string ends with one of the given
   ** options.
   **
	 ** @param  value              the object to be checked if it equals one of
   **                            the given suffixes.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  suffix             the argument list of suffixes to be checked.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the given string starts
   **                            with one of the given suffixes.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean endsWithOneOf(final String value, String... suffix) {
		for (String cursor : suffix) {
			if (value.endsWith(cursor)) {
				return true;
			}
		}
		return false;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
	 ** Returns <code>true</code> if at least one value is empty.
   **
	 ** @param  value              the value to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if any value is empty and
   **                            <code>false</code> if no values are empty.
	 */
	public static boolean empty(final Object... value) {
		for (Object cursor : value) {
			if (empty(cursor)) {
				return true;
			}
		}

		return false;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
	 ** Returns <code>true</code> if the given object is <code>null</code> or an
   ** empty array or has an empty toString() result.
   **
	 ** @param  value              the value to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the given object is
   **                            <code>null</code> or an empty array or has an
   **                            empty toString() result.
	 */
	public static boolean empty(final Object value) {
		if (value == null) {
			return true;
		}
		else if (value instanceof String) {
			return empty((String) value);
		}
		else if (value instanceof Collection) {
			return empty((Collection<?>) value);
		}
		else if (value instanceof Map) {
			return empty((Map<?, ?>) value);
		}
		else if (value.getClass().isArray()) {
			return Array.getLength(value) == 0;
		}
		else {
			return value.toString() == null || value.toString().isEmpty();
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
	 ** Returns <code>true</code> if the given string is <code>null</code> or is
   ** empty.
   **
	 ** @param  value              the string to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the given string is
   **                            <code>null</code> or empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final String value) {
		return value == null || value.isEmpty();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
	 ** Returns <code>true</code> if the given map is <code>null</code> or
   ** is empty.
   **
	 ** @param  value              the map to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type any for the key and any as
   **                            the value.
   **
   ** @return                    <code>true</code> if the given map is
   **                            <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final Map<?, ?> value) {
		return value == null || value.isEmpty();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
	 ** Returns <code>true</code> if the given collection is <code>null</code> or
   ** is empty.
   **
	 ** @param  value              the collection to be checked on emptiness.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type any.
   **
   ** @return                    <code>true</code> if the given collection is
   **                            <code>null</code> or is empty.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean empty(final Collection<?> value) {
		return value == null || value.isEmpty();
	}
}