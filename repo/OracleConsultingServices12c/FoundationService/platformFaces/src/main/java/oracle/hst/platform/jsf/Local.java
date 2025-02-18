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

    File        :   Local.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Local.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/
package oracle.hst.platform.jsf;

import java.io.IOException;

import java.io.UncheckedIOException;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import javax.faces.lifecycle.Lifecycle;

import javax.faces.application.Application;
import javax.faces.application.ProjectStage;

////////////////////////////////////////////////////////////////////////////////
// abstract class Local
// ~~~~~~~~ ~~~~~ ~~~~~
/**
 ** Collection of utility methods for the JSF API that are mainly shortcuts for
 ** obtaining stuff from the provided {@link FacesContext} argument. In effect,
 ** it 'flattens' the hierarchy of nested objects.
 ** <p>
 ** The difference with {@link Faces} is that no one method of
 ** <code>Local</code> obtains the {@link FacesContext} from the current thread
 ** by {@link FacesContext#getCurrentInstance()}. This job is up to the caller.
 ** <br>
 ** This is more efficient in situations where multiple utility methods needs
 ** to be called at the same time. Invoking
 ** {@link FacesContext#getCurrentInstance()} is at its own an extremely cheap
 ** operation, however as it's to be obtained as a {@link ThreadLocal} variable,
 ** it's during the call still blocking all other running threads for some
 ** nanoseconds or so.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The methods which are <strong>directly</strong> available on
 ** {@link FacesContext} instance itself, such as
 ** {@link FacesContext#getExternalContext()},
 ** {@link FacesContext#getViewRoot()},
 ** {@link FacesContext#isValidationFailed()}, etc are not delegated by the this
 ** utility class, because it would design technically not make any sense to
 ** delegate a single-depth method call like follows:
 ** <pre>
 **   ExternalContext externalContext = Local.getExternalContext(context);
 ** </pre>
 ** instead of just calling it directly like follows:
 ** <pre>
 **   ExternalContext externalContext = context.getExternalContext();
 ** </pre>
 **
 ** <h3>Usage</h3>
 ** <p>
 ** Some examples (for the full list, check the API documentation):
 ** <pre>
 **   FacesContext context = Faces.context();
 **   User         user    = Local.sessionAttribute(context, "user");
 **   Item         item    = Local.evaluateExpressionGet(context, "#{item}");
 **   String       value   = Local.requestCookie(context, "cookieName");
 **   List&lt;Locale&gt; supported = Local.supportedLocales(context);
 **   Local.invalidateSession(context);
 **   Local.redirect(context, "login.jsf");
 ** </pre>
 **
 ** @see     Servlet
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Local {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Local</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Servlet()" and enforces use of the public method below.
   */
  private Local() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirect
  /**
   ** Sends a temporary (302) redirect to the given URL.
   ** <br>
   ** If the given URL does <b>not</b> start with <code>http://</code>,
   ** <code>https://</code> or <code>/</code>, then the request context path
   ** will be prepended, otherwise it will be the unmodified redirect URL. So,
   ** when redirecting to another page in the same web application, always
   ** specify the full path from the context root on (which in turn does not
   ** need to start with <code>/</code>).
   ** <pre>
   **   Local.redirect("other.jsf");
	 ** </pre>
	 ** <p>
   ** You can use {@link String#format(String, Object...)} placeholder
   ** <code>%s</code> in the redirect URL to represent placeholders for any
   ** request parameter values which needs to be URL-encoded. Here's a concrete
   ** example:
   ** <pre>
   **   Faces.redirect("other.jsf?foo=%s&amp;bar=%s", foo, bar);
	 ** </pre>
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
	 ** @param  url                The URL to redirect the current response to.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  parameter          the request parameter values which you'd like
   **                            to put URL-encoded in the given URL.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **                            
	 ** @throws NullPointerException when faces context is unavailable or given
   **                              url is <code>null</code>.
   ** @throws UncheckedIOException whenever something fails at I/O level.
   **
   ** @see    ExternalContext#redirect(String)
	 */
	public static void redirect(final FacesContext context, final String url, final Object... parameter) {
		final ExternalContext external = externalContext(context);
    // MyFaces also requires this for a redirect in current request which is
    // incorrect.
		external.getFlash().setRedirect(true);
		try {
			external.redirect(Servlet.prepareRedirectURL(request(context), url, parameter));
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirectPermanent
  /**
   ** Sends a permanent (301) redirect to the given URL.
   ** <br>
   ** If the given URL does <b>not</b> start with <code>http://</code>,
   ** <code>https://</code> or <code>/</code>, then the request context path
   ** will be prepended, otherwise it will be the unmodified redirect URL. So,
   ** when redirecting to another page in the same web application, always
   ** specify the full path from the context root on (which in turn does not
   ** need to start with <code>/</code>).
   ** <pre>
   **   Faces.redirectPermanent("other.jsf");
	 ** </pre>
	 ** <p>
   ** You can use {@link String#format(String, Object...)} placeholder
   ** <code>%s</code> in the redirect URL to represent placeholders for any
   ** request parameter values which needs to be URL-encoded. Here's a concrete
   ** example:
   ** <pre>
   **   Faces.redirect("other.jsf?foo=%s&amp;bar=%s", foo, bar);
	 ** </pre>
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
	 ** @param  url                The URL to redirect the current response to.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
	 ** @param  parameter          the request parameter values which you'd like
   **                            to put URL-encoded in the given URL.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **                            
	 ** @throws NullPointerException when faces context is unavailable or given
   **                              url is <code>null</code>.
   ** @throws UncheckedIOException whenever something fails at I/O level.
   **
   ** @see    ExternalContext#setResponseStatus(int)
   ** @see    ExternalContext#setResponseHeader(String, String)
	 */
	public static void redirectPermanent(final FacesContext context, final String url, final Object... parameter) {
    // MyFaces also requires this for a redirect in current request which is
    // incorrect.
		externalContext(context).getFlash().setRedirect(true);
		Servlet.redirectPermanent(response(context), Servlet.prepareRedirectURL(request(context), url, parameter));
		context.responseComplete();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referrer
  /**
   ** Returns the the referrer of the request.
	 ** <p>
	 ** This is also available in EL as <code>#{faces.referrer}</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the referrer of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException when faces context is unavailable.
   **
   ** @see    HttpServletRequest#getHeader(String)
	 */
	public static String referrer(final FacesContext context) {
		return Servlet.referrer(request(context));
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
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> if connection is secure,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @see     HttpServletRequest#isSecure()
	 */
	public static boolean secure(final FacesContext context) {
		return Servlet.secure(request(context));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns the HTTP servlet request.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   **
   ** @see    ExternalContext#getRequest()
   */
  public static HttpServletRequest request(final FacesContext context) {
    return (HttpServletRequest)externalContext(context).getRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ajaxRequest
	/**
	 ** Returns whether the current request is an ajax request.
	 ** <p>
	 ** This is also available in EL as <code>#{faces.ajaxRequest}</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> for an ajax request,
   **                            <code>false</code> for a non-ajax (synchronous)
   **                            request.
   **
	 ** @throws NullPointerException when faces context is unavailable.
	 */
	public static boolean ajaxRequest(final FacesContext context) {
		return context.getPartialViewContext().isAjaxRequest();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Returns the HTTP servlet response.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the HTTP servlet response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   **
   ** @see    ExternalContext#getResponse()
   */
	public static HttpServletResponse response(final FacesContext context) {
		return (HttpServletResponse)externalContext(context).getResponse();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   development
  /**
	 ** Returns <code>true</code> if we're in JSF development stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>Development</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> if we're in development
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean development(final FacesContext context) {
		return projectStage(context) == ProjectStage.Development;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTest
  /**
	 ** Returns <code>true</code> if we're in JSF system test stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>SystemTest</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> if we're in system test
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean systemTest(final FacesContext context) {
		return projectStage(context) == ProjectStage.SystemTest;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unitTest
  /**
	 ** Returns <code>true</code> if we're in JSF unit test stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>SystemTest</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> if we're in unit test
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean unitTest(final FacesContext context) {
		return projectStage(context) == ProjectStage.UnitTest;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   production
  /**
	 ** Returns <code>true</code> if we're in JSF production stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>Production</code>.
   **
   ** @param  context            the {@link FacesContext} reference in which
   **                            the caller is executing.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    <code>true</code> if we're in production
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean production(final FacesContext context) {
		return projectStage(context) == ProjectStage.Production;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesLifecycle
  /**
	 ** Returns the {@link Lifecycle} associated with current Faces application.
   **
   ** @param  context            the involved faces context.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the {@link Lifecycle} associated with current
   **                            Faces application.
   **                            <br>
   **                            Possible object is {@link Lifecycle}.
	 */
	public static Lifecycle facesLifecycle(final FacesContext context) {
		return Servlet.facesLifecycle(servletContext(context));
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalContext
  /**
   ** Returns the current external context.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.
   **
   ** @param  context            the involved faces context.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the current external context.
   **                            <br>
   **                            Possible object is {@link ExternalContext}.
   **
   ** @see    FacesContext#getExternalContext()
   */
	public static ExternalContext externalContext(final FacesContext context) {
		return context.getExternalContext();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   servletContext
  /**
   ** Returns the servlet context.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.
   **
   ** @param  context            the involved faces context.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the current external context.
   **                            <br>
   **                            Possible object is {@link ExternalContext}.
   **
   ** @see    ExternalContext#getContext()
	 */
	public static ServletContext servletContext(final FacesContext context) {
		return Local.servletContext(context);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectStage
  /**
   ** Returns the project stage.
   ** <br>
   ** This will return the <code>javax.faces.PROJECT_STAGE</code> context
   ** parameter in <code>web.xml</code>.
   **
   ** @param  context            the involved faces context.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   ** 
	 ** @return                    the project stage.
   **                            <br>
   **                            Possible object is {@link ProjectStage}.
   **
   ** @throws NullPointerException when faces context is unavailable.
   **
   ** @see Application#getProjectStage()
	 */
	public static ProjectStage projectStage(final FacesContext context) {
		return application(context).getProjectStage();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the JSF Application singleton.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.
   **
   ** @param  context            the involved faces context.
   **                            <br>
   **                            Allowed object is {@link FacesContext}.
   **
   ** @return                    the faces application singleton.
   **
   ** @see    FacesContext#getApplication()
   */
	public static Application application(final FacesContext context) {
		return context.getApplication();
	}
}
