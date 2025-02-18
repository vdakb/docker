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

    File        :   Faces.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Faces.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf;

import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.ArrayList;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import javax.faces.component.UIViewRoot;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;

import javax.servlet.ServletContext;

////////////////////////////////////////////////////////////////////////////////
// abstract class Faces
// ~~~~~~~~ ~~~~~ ~~~~~
/**
 ** Collection of utility methods for the JSF API that are mainly shortcuts for
 ** obtaining stuff from the thread local {@link FacesContext}. In effects, it
 ** 'flattens' the hierarchy of nested objects.
 ** <p>
 ** Do note that using the hierarchy is actually a better software design
 ** practice, but can lead to verbose code.
 ** <p>
 ** In addition, note that there's normally a minor overhead in obtaining the
 ** thread local {@link FacesContext}. In case client code needs to call methods
 ** in this class multiple times it's expected that performance will be slightly
 ** better if instead the {@link FacesContext} is obtained once and the required
 ** methods are called on that, although the difference is practically
 ** negligible when used in modern server hardware.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Faces {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Faces</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** This constructor is private to prevent other classes to use "new Faces()"
   ** and enforces use of the public method below.
   */
  private Faces() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultLocale
  /**
   ** Returns the default locale, or <code>null</code> if there is none.
   **
   ** @return                    the default locale, or <code>null</code> if
   **                            there is none.
   **                            <br>
   **                            Possible object is {@link Locale}.
   **
   ** @see    Application#getDefaultLocale()
   */
  public static Locale defaultLocale() {
    return application().getDefaultLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentLocale
  /**
   ** Returns the current locale.
   ** <p>
   ** If the locale set in the JSF view root is not <code>null</code>, then
   ** return it. Else if the client preferred locale is not <code>null</code>
   ** and is among supported locales, then return it. Else if the JSF default
   ** locale is not <code>null</code>, then return it. Else return the system
   ** default locale.
   **
   ** @return                    the current locale.
   **                            <br>
   **                            Possible object is {@link Locale}.
   **
   ** @see    UIViewRoot#getLocale()
   ** @see    ExternalContext#getRequestLocale()
   ** @see    Application#getDefaultLocale()
   ** @see    Locale#getDefault()
   */
  public static Locale currentLocale() {
    final FacesContext context  = context();
    final UIViewRoot   viewRoot = context.getViewRoot();

    Locale locale = null;
    // prefer the locale set in the view.
    if (viewRoot != null)
      locale = viewRoot.getLocale();

    // then the client preferred locale.
    if (locale == null) {
      final Locale clientLocale = context.getExternalContext().getRequestLocale();
      if (supportedLocales().contains(clientLocale))
        locale = clientLocale;
    }

    // then the JSF default locale.
    if (locale == null)
      locale = context.getApplication().getDefaultLocale();

    // finally the system default locale.
    if (locale == null)
      locale = Locale.getDefault();

    return locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supportedLocales
  /**
   ** Returns a list of all supported locales on this application, with the
   ** default locale as the first item, if any. This will return an empty list
   ** if there are no locales definied in <code>faces-config.xml</code>.
   **
   ** @return                    a list of all supported locales on this
   **                            application, with the default locale as the
   **                            first item, if any.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Locale}.
   **
   ** @see     Application#getDefaultLocale()
   ** @see     Application#getSupportedLocales()
   */
  public static List<Locale> supportedLocales() {
    final Application  application      = application();
    final Locale       defaultLocale    = application.getDefaultLocale();
    final List<Locale> supportedLocales = new ArrayList<Locale>();
    if (defaultLocale != null)
      supportedLocales.add(defaultLocale);

    Iterator<Locale> i = application.getSupportedLocales();
    while (i.hasNext()) {
      final Locale locale = i.next();
      if (!locale.equals(defaultLocale))
        supportedLocales.add(locale);
    }

    return supportedLocales;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Append a {@link FacesMessage} to the set of messages.
   ** <br>
   ** The {@link FacesMessage} is not associated with a client identifier.
   **
   ** @param  message            the message to be appended.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public static void message(final String message) {
    message(new FacesMessage(message));    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Append a {@link FacesMessage} to the set of messages.
   ** <br>
   ** The {@link FacesMessage} is not associated with a client identifier.
   **
   ** @param  message            the message to be appended.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   */
  public static void message(final FacesMessage message) {
    context().addMessage(null, message);    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Append a {@link FacesMessage} to the set of messages associated with the
   ** specified client identifier, if <code>id</code> is not <code>null</code>.
   ** If <code>id</code> is <code>null</code>, this {@link FacesMessage} is
   ** assumed to not be associated with any specific component instance.
   ** 
   ** @param  id                 the client identifier with which this message
   **                            is associated (if any).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to be appended.
   **                            <br>
   **                            Allowed object is {@link FacesMessage}.
   */
  public static void message(final String id, final  FacesMessage message) {
    context().addMessage(id, message);    
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
   ** @return                    <code>true</code> if connection is secure,
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @see     HttpServletRequest#isSecure()
	 */
	public static boolean secure() {
		return Local.secure(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   referrer
  /**
   ** Returns the the referrer of the request.
	 ** <p>
	 ** This is also available in EL as <code>#{faces.referrer}</code>.
   **
   ** @return                    the referrer of the request.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException when faces context is unavailable.
   **
   ** @see    HttpServletRequest#getHeader(String)
	 */
	public static String referrer() {
		return Local.referrer(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Returns the {@link HttpSession} from the current {@link FacesContext}.
   **
   ** @return                    the {@link HttpSession} from the current
   **                            {@link FacesContext}.
   **                            <br>
   **                            Possible object is {@link HttpSession}.
   */
  public static HttpSession session() {
    return session(true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Returns the {@link HttpSession} and creates one if one doesn't exist and
   ** <code>create</code> argument is <code>true</code>, otherwise don't create
   ** one and return <code>null</code>.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @param  create             whether a new session has to be created.
   **
   ** @return                    the {@link HttpSession} from the current
   **                            {@link FacesContext}.
   **                            <br>
   **                            Possible object is {@link HttpSession}.
   **
   ** @see ExternalContext#getSession(boolean)
   */
  public static HttpSession session(final boolean create) {
    final ExternalContext externalContext = context().getExternalContext();
    return (HttpSession)externalContext.getSession(create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestContextPath
  /**
   ** Returns the HTTP request context path.
   ** <p>
   ** It's the webapp context name, with a leading slash. If the webapp runs on
   ** context root, then it returns an empty string.
   **
   ** @return                    the HTTP request context path.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    ExternalContext#getRequestContextPath()
   */
  public static String requestContextPath() {
    return externalContext().getRequestContextPath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestServletPath
  /**
   ** Returns the HTTP request servlet path.
   ** <p>
   ** If JSF is prefix mapped (e.g. <code>/faces/*</code>), then this returns the
   ** whole prefix mapping (e.g. <code>/faces</code>). If JSF is suffix mapped
   ** (e.g. <code>*.jsf</code>), then this returns the whole part after the
   ** context path, with a leading slash.
   **
   ** @return                    the HTTP request servlet path.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see     ExternalContext#getRequestServletPath()
   */
  public static String requestServletPath() {
    return externalContext().getRequestServletPath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestPathInfo
  /**
   ** Returns the HTTP request path info.
   ** <p>
   ** If JSF is prefix mapped (e.g. <code>/faces/*</code>), then this returns the
   ** whole prefix mapping (e.g. <code>/faces</code>). If JSF is suffix mapped
   ** (e.g. <code>*.jsf</code>), then this returns the whole part after the
   ** context path, with a leading slash.
   **
   ** @return                    the HTTP request path info.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see     ExternalContext#getRequestPathInfo()
   */
  public static String requestPathInfo() {
    return externalContext().getRequestPathInfo();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestURL
  /**
   ** Returns the HTTP request URL.
   ** <p>
   ** This is the full request URL as the enduser sees in browser address bar.
   ** This does not include the request query string.
   **
   ** @return                    the HTTP request URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getRequestURL()
   */
  public static String requestURL() {
    return request().getRequestURL().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestURI
  /**
   ** Returns the HTTP request URI.
   ** <p>
   ** This is the part after the domain in the request URL, including the
   ** leading slash. This does not include the request query string.
   **
   ** @return                    the HTTP request URI.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getRequestURI()
   */
  public static String requestURI() {
    return request().getRequestURI();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestQueryString
  /**
   ** Returns the HTTP request query string.
   ** <p>
   ** This is the part after the <code>?</code> in the request URL as the
   ** enduser sees in browser address bar.
   **
   ** @return                    the HTTP request query string.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getQueryString()
   */
  public static String requestQueryString() {
    return request().getQueryString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestBaseURL
  /**
   ** Returns the HTTP request base URL.
   ** <p>
   ** This is the URL from the scheme, domain until with context path, including
   ** the trailing slash. This is the value you could use in HTML
   ** <code>&lt;base&gt;</code> tag.
   **
   ** @return                    the HTTP request base URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getRequestURL()
   ** @see    HttpServletRequest#getRequestURI()
   ** @see    HttpServletRequest#getContextPath()
   */
  public static String requestBaseURL() {
    final HttpServletRequest request = request();
    final String             url     = request.getRequestURL().toString();
    return url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestDomainURL
  /**
   ** Returns the HTTP request domain URL.
   ** <p>
   ** This is the URL with the scheme and domain, without any trailing slash.
   **
   ** @return                    the HTTP request domain URL.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see    HttpServletRequest#getRequestURL()
   ** @see    HttpServletRequest#getRequestURI()
   */
  public static String requestDomainURL() {
    final HttpServletRequest request = request();
    final String             url     = request.getRequestURL().toString();
    return url.substring(0, url.length() - request.getRequestURI().length());
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
   ** @return                    the HTTP servlet request.
   **                            <br>
   **                            Possible object is {@link HttpServletRequest}.
   **
   ** @see    ExternalContext#getRequest()
   */
  public static HttpServletRequest request() {
    return (HttpServletRequest)externalContext().getRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ajaxRequest
	/**
	 ** Returns whether the current request is an ajax request.
	 ** <p>
	 ** This is also available in EL as <code>#{faces.ajaxRequest}</code>.
   **
   ** @return                    <code>true</code> for an ajax request,
   **                            <code>false</code> for a non-ajax (synchronous)
   **                            request.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
	 ** @throws NullPointerException when faces context is unavailable.
	 */
	public static boolean ajaxRequest() {
		return Local.ajaxRequest(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Returns the {@link HttpServletResponse}.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the {@link HttpServletResponse}.
   **                            <br>
   **                            Possible object is {@link HttpServletResponse}.
   **
   ** @see    ExternalContext#getResponse()
   */
  public static HttpServletResponse response() {
    return Local.response(context());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   projectStage
  /**
   ** Returns the project stage.
   ** <br>
   ** This will return the <code>javax.faces.PROJECT_STAGE</code> context
   ** parameter in <code>web.xml</code>.
   ** 
	 ** @return                    the project stage.
   **                            <br>
   **                            Possible object is {@link ProjectStage}.
   **
   ** @throws NullPointerException when faces context is unavailable.
   **
   ** @see Application#getProjectStage()
	 */
	public static ProjectStage projectStage() {
		return Local.projectStage(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   development
  /**
   ** Returns whether we're in development stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>Development</code>.
   **
   ** @return                    <code>true</code> if we're in development
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean development() {
		return Local.development(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTest
  /**
   ** Returns whether we're in system test stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>SystemTest</code>.
   **
   ** @return                    <code>true</code> if we're in system test
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean systemTest() {
		return Local.systemTest(context());
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
   ** @return                    <code>true</code> if we're in unit test
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean unitTest() {
		return Local.unitTest(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   production
  /**
   ** Returns whether we're in production stage.
   ** <br>
   ** This will be the case when the <code>javax.faces.PROJECT_STAGE</code>
   ** context parameter in <code>web.xml</code> is set to
   ** <code>Production</code>.
   **
   ** @return                    <code>true</code> if we're in production
   **                            stage, otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 **
   ** @see     Application#getProjectStage()
	 */
	public static boolean production() {
		return Local.production(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expressionFactory
  /**
   ** Returns the expression factory singleton.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the expression factory singleton.
   **
   ** @see    FacesContext#getApplication()
   */
  public static ExpressionFactory expressionFactory() {
    return application().getExpressionFactory();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the JSF Application singleton.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the faces application singleton.
   **
   ** @see    FacesContext#getApplication()
   */
  public static Application application() {
    return context().getApplication();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalContext
  /**
   ** Returns the current external context.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the current external context.
   **
   ** @see    FacesContext#getExternalContext()
   */
  public static ExternalContext externalContext() {
    return context().getExternalContext();
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
   ** @return                    the current external context.
   **                            <br>
   **                            Possible object is {@link ExternalContext}.
   **
   ** @see    ExternalContext#getContext()
	 */
	public static ServletContext servletContext() {
		return Local.servletContext(context());
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expressionContext
  /**
   ** Returns the current expression context.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <i>Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the current expression context.
   **
   ** @see    FacesContext#getExternalContext()
   */
  public static ELContext expressionContext() {
    return context().getELContext();
  }

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
   **   Faces.redirect("other.jsf");
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
   **
   ** @see    ExternalContext#redirect(String)
	 */
	public static void redirect(final String url, final Object... parameter) {
		Local.redirect(context(), url, parameter);
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
   **
   ** @see    ExternalContext#setResponseStatus(int)
   ** @see    ExternalContext#setResponseHeader(String, String)
	 */
	public static void redirectPermanent(final String url, final Object... parameter) {
		Local.redirectPermanent(context(), url, parameter);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the current faces context.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** Whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.
   **
   ** @return                    the current faces context.
   **
   ** @see    FacesContext#getCurrentInstance()
   */
  public static FacesContext context() {
    return FacesContext.getCurrentInstance();
  }
}