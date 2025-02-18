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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   JSF.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JSF.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces;

import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;

import java.security.Principal;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import javax.el.ExpressionFactory;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.faces.FactoryFinder;

import javax.faces.event.PhaseId;
import javax.faces.event.ActionEvent;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.NamingContainer;

import javax.faces.component.visit.VisitResult;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitCallback;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.application.FacesMessage;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.NavigationHandler;

import org.apache.myfaces.trinidad.component.UIXCommand;
import org.apache.myfaces.trinidad.component.UIXComponent;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class JSF
// ~~~~~ ~~~
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
public class JSF {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String  BINDING_OPERATION_FAILED = "EXECUTE_OPERATION_BINDING_FAILED";
  public static final String  EVENT_HANDLER_FAILED     = "ERROR_IN_EVENT_HANDLER";

  private static final String ERROR_NO_VIEW            = "There is no UIViewRoot.";
  private static final String UNSUPPORTED_ENCODING     = "UTF-8 is apparently not supported on this machine.";
  private static final String MISSING_RESOURCE         = "???%s???";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class ContextSetter
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** Inner class so that the protected
   ** {@link FacesContext#setCurrentInstance(FacesContext)} method can be
   ** invoked.
   */
  private static abstract class ContextSetter extends FacesContext {
    protected static void setCurrentInstance(FacesContext context) {
      FacesContext.setCurrentInstance(context);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JSF</code>.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private JSF() {
    // do not instantiate
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
   **
   ** @see    Application#getDefaultLocale()
   */
  public static Locale defaultLocale() {
    return application().getDefaultLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Set the locale of the current view, which is to be used in localizing of
   ** the response.
   **
   ** @param  locale             the locale of the current view.
   **
   ** @throws IllegalStateException when there is no view (i.e. when it is
   **                               <code>null</code>). This can happen if the
   **                               method is called at the wrong moment in the
   **                               JSF lifecycle, e.g. before the view has been
   **                               restored/created.
   **
   ** @see    UIViewRoot#setLocale(Locale)
   */
  public static void locale(final Locale locale) {
    final UIViewRoot viewRoot = viewRoot();
    if (viewRoot == null)
      throw new IllegalStateException(ERROR_NO_VIEW);

    viewRoot.setLocale(locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
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
   **
   ** @see    UIViewRoot#getLocale()
   ** @see    ExternalContext#getRequestLocale()
   ** @see    Application#getDefaultLocale()
   ** @see    Locale#getDefault()
   */
  public static Locale locale() {
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
  // Method:   hasSession
  /**
   ** Returns whether the HTTP session has already been created.
   **
   ** @return                    <code>true</code> if the HTTP session has
   **                            already been created, otherwise
   **                            <code>false</code>.
   ** @see    ExternalContext#getSession(boolean)
   */
  public static boolean hasSession() {
    return session(false) != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionNew
  /**
   ** Returns whether the HTTP session has been created for the first time in
   ** the current request. This returns also <code>false</code> when there is no
   ** means of a HTTP session.
   **
   ** @return                    <code>true</code> if the HTTP session has been
   **                            created for the first time in the current
   **                            request, otherwise <code>false</code>.
   **
   ** @see    HttpSession#isNew()
   ** @see    ExternalContext#getSession(boolean)
   */
  public static boolean sessionNew() {
    final HttpSession session = session(false);
    return (session != null && session.isNew());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionTimedOut
  /**
   ** Returns whether the HTTP session has been timed out for the current
   ** request. This is helpful if you need to distinguish between a first-time
   ** request on a fresh session and a first-time request on a timed out
   ** session, for example to display "Oops, you have been logged out because
   ** your session has been timed out!".
   **
   ** @return                    <code>true</code> if the HTTP session has been
   **                            timed out for the current request, otherwise
   **                            <code>false</code>.
   **
   ** @see    HttpServletRequest#getRequestedSessionId()
   ** @see    HttpServletRequest#isRequestedSessionIdValid()
   */
  public static boolean sessionTimedOut() {
    final HttpServletRequest request = request();
    return request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionID
  /**
   ** Returns a string containing the unique identifier assigned to this
   ** session. The identifier is assigned by the servlet container and is
   ** implementation dependent.
   **
   ** @return                    the HTTP session ID.
   **
   ** @see HttpSession#getId()
   */
  public static String sessionID() {
    final HttpSession session = session(false);
    return (session != null) ? session.getId() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionScopeValue
  /**
   ** Returns the session scope attribute value associated with the given name.
   **
   ** @param  <T>                the expected type of the session scope
   **                            attribute.
   ** @param  name               the session scope attribute name.
   **
   ** @return                    the session scope attribute value associated
   **                            with the given name.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getSessionMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T sessionScopeValue(final String name) {
    return (T)sessionScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionScopeValue
  /**
   ** Convenience method for getting session scope attribute value.
   **
   ** @param  key                object key.
   ** @param  value              the value mapped by the data stored in the
   **                            current session scope for the current user.
   */
  public static void sessionScopeValue(final String key, final Object value) {
    sessionScope().put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSessionScopeValue
  /**
   ** Removes the session scope attribute value associated with the given name.
   **
   ** @param  <T>                the expected type of the session scope
   **                            attribute.
   ** @param  name               the session scope attribute name.
   **
   ** @return                    the session scope attribute value previously
   **                            associated with the given name, or
   **                            <code>null</code> if there is no such
   **                            attribute.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getSessionMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T removeSessionScopeValue(String name) {
    return (T)sessionScope().remove(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   session
  /**
   ** Returns the {@link HttpSession} from the current {@link FacesContext}.
   **
   ** @return                    the {@link HttpSession} from the current
   **                            {@link FacesContext}.
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
   ** @see ExternalContext#getSession(boolean)
   */
  public static HttpSession session(final boolean create) {
    final ExternalContext externalContext = context().getExternalContext();
    return (HttpSession)externalContext.getSession(create);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionScope
  /**
   ** Obtain the {@link Map} of data stored in the current session for the
   ** current user.
   **
   ** @return                    the {@link Map} of the data stored in the
   **                            current session for the current user.
   */
  public static Map<String, Object> sessionScope() {
    return sessionScope(context());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sessionScope
  /**
   ** Obtain the {@link Map} of data stored in the current session for the
   ** current user.
   **
   ** @param  context            the {@link FacesContext} containing the
   **                            session {@link Map}.
   * @return {@link Map} of the data stored in the current session for the
   *         current user.
   */
  public static Map<String, Object> sessionScope(final FacesContext context) {
    return context.getExternalContext().getSessionMap();
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
   ** (e.g. <code>*.xhtml</code>), then this returns the whole part after the
   ** context path, with a leading slash.
   **
   ** @return                    the HTTP request servlet path.
   **
   ** @see     ExternalContext#getRequestServletPath()
   */
  public static String requestServletPath() {
    return externalContext().getRequestServletPath();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestServletPath
  /**
   ** Returns the HTTP request path info.
   ** <p>
   ** If JSF is prefix mapped (e.g. <code>/faces/*</code>), then this returns the
   ** whole prefix mapping (e.g. <code>/faces</code>). If JSF is suffix mapped
   ** (e.g. <code>*.xhtml</code>), then this returns the whole part after the
   ** context path, with a leading slash.
   **
   ** @return                    the HTTP request path info.
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
  // Method:   requestScopeValue
  /**
   ** Sets the request scope attribute value associated with the given name.
   **
   ** @param  name               the request scope attribute name.
   ** @param  value              the request scope attribute value.
   **
   ** @see    ExternalContext#getRequestMap()
   */
  public static void requestScopeValue(final String name, final Object value) {
    requestScope().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestScopeValue
  /**
   ** Returns the request scope attribute value associated with the given name.
   **
   ** @param  <T>                the expected type of the reuest scope
   **                            attribute.
   ** @param  name               the request scope attribute name.
   **
   ** @return                    the request scope attribute value associated
   **                            with the given name.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    #requestScope()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T requestScopeValue(final String name) {
    return (T)requestScope().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeRequestScopeValue
  /**
   ** Removes the request scope attribute value associated with the given name.
   **
   ** @param  <T>                the expected type of the request scope
   **                            attribute.
   ** @param  name               the request scope attribute name.
   **
   ** @return                    the request scope attribute value previously
   **                            associated with the given name, or
   **                            <code>null</code> if there is no such
   **                            attribute.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getRequestMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T removeRequestScopeValue(final String name) {
    return (T)requestScope().remove(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestScope
  /**
   ** Returns the request scope map.
   **
   ** @return                    the request scope map.
   **
   ** @see    ExternalContext#getRequestMap()
   */
  public static Map<String, Object> requestScope() {
    return externalContext().getRequestMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestHeaderValue
  /**
   ** Obtain the request parameter map in the external content.
   **
   ** @param  name               the request scope parameter name.
   **
   ** @return                    the request parameter value previously
   **                            associated with the given name, or
   **                            <code>null</code> if there is no such
   **                            parameter.
   */
  public static String requestHeaderValue(final String name) {
    final Map<String, String> header = requestHeaderMap();
    return (header != null) ? header.get(name) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestHeaderMap
  /**
   ** Returns an immutable Map whose keys are the set of request headers
   ** names included in the current request, and whose values (of type String)
   ** are the first (or only) value for each header name returned by the
   ** underlying request.
   **
   ** @return                    the {@link Map} of request headers in the
   **                            external content.
   */
  public static Map<String, String> requestHeaderMap() {
    return externalContext().getRequestHeaderMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestParameterValue
  /**
   ** Obtain the request parameter map in the external content.
   **
   ** @param  name               the request scope parameter name.
   **
   ** @return                    the request parameter value previously
   **                            associated with the given name, or
   **                            <code>null</code> if there is no such
   **                            parameter.
   */
  public static String requestParameterValue(final String name) {
    final Map<String, String> parameter = requestParameterMap();
    return (parameter != null) ? parameter.get(name) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestParameterMap
  /**
   ** Returns an immutable Map whose keys are the set of request parameters
   ** names included in the current request, and whose values (of type String)
   ** are the first (or only) value for each parameter name returned by the
   ** underlying request.
   **
   ** @return                    the {@link Map} of request parameters in the
   **                            external content.
   */
  public static Map<String, String> requestParameterMap() {
    return externalContext().getRequestParameterMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns the HTTP servlet request.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the HTTP servlet request.
   **
   ** @see    ExternalContext#getRequest()
   */
  public static HttpServletRequest request() {
    return (HttpServletRequest)externalContext().getRequest();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Returns the {@link HttpServletResponse}.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the {@link HttpServletResponse}.
   **
   ** @see    ExternalContext#getResponse()
   */
  public static HttpServletResponse response() {
    return (HttpServletResponse)externalContext().getResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Returns the {@link HttpServletResponse} from a given {@link FacesContext}.
   **
   ** @param  context            the {@link FacesContext} from which to obtain
   **                            the {@link HttpServletResponse}.
   **
   ** @return                    the {@link HttpServletResponse} from a given
   **                            {@link FacesContext}.
   */
  public static HttpServletResponse response(final FacesContext context) {
    return (HttpServletResponse)context.getExternalContext().getResponse();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigate
  /**
   ** Perform the JSF navigation to the given outcome.
   **
   ** @param  outcome            the navigation outcome.
   **
   ** @see    Application#getNavigationHandler()
   ** @see    NavigationHandler#handleNavigation(FacesContext, String, String)
   */
  public static void navigate(final String outcome) {
    navigationHandler().handleNavigation(context(), null, outcome);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirectAction
  /**
   ** Redirect to a provided action url.
   ** <p>
   ** Performing any rewriting needed to ensure that it will correctly identify
   ** an addressable action in the current application.
   **
   ** @param  url                rhe URL to redirect the current response to.
   */
  public static void redirectAction(final String url) {
    final ExternalContext context = externalContext();
    redirect(context.encodeActionURL(url));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirect
  /**
   ** Redirect to a provided url.
   **
   ** @param  url                rhe URL to redirect the current response to.
   */
  public static void redirect(final String url) {
    final ExternalContext context = externalContext();
    try {
      context.redirect(context.encodeActionURL(url));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirect
  /**
   ** Sends a temporary (302) redirect to the given URL. If the given URL does
   ** not start with <code>http://</code>, <code>https://</code> or
   ** <code>/</code>, then the request context path will be prepended, otherwise
   ** it will be the unmodified redirect URL. So, when redirecting to another
   ** page in the same web application, always specify the full path from the
   ** context root on (which in turn does not need to start with <code>/</code>).
   ** <p>
   ** You can use {@link String#format(String, Object...)} placeholder
   ** <code>%s</code> in the redirect URL to represent placeholders for any
   ** request parameter values which needs to be URL-encoded. Here's a concrete
   ** example:
   ** <pre>
   **   Faces.redirect("other.xhtml?foo=%s&amp;bar=%s", foo, bar);
   ** </pre>
   **
   ** @param  url                rhe URL to redirect the current response to.
   ** @param  parameter          the request parameter values which you'd like
   **                            to put URL-encoded in the given URL.
   **
   ** @throws IOException        whenever something fails at I/O level. The
   **                            caller should preferably not catch it, but just
   **                            redeclare it in the action method. The
   **                            servletcontainer will handle it.
   ** @throws NullPointerException When url is <code>null</code>.
   **
   ** @see    ExternalContext#redirect(String)
   */
  public static void redirect(final String url, final String... parameter)
    throws IOException {

    final ExternalContext context = externalContext();
    context.redirect(String.format(normalizeRedirectURL(url), encodeURLParams(parameter)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showInformationMessage
  /**
   ** Append an information message text to the set of messages.
   ** <br>
   ** The message will not be bound to any component.
   **
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showInformationMessage(final String text) {
    return showInformationMessage(null, text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showInformationMessage
  /**
   ** Append an information message text to the set of messages associated with
   ** the specified component identifier, if componentId is not
   ** <code>null</code>. If componentId is <code>null</code>, the text is
   ** assumed to not be associated with any specific component instance.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showInformationMessage(final String componentId, final String text) {
    return showMessage(componentId, createMessage(FacesMessage.SEVERITY_INFO, text));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showWarningMessage
  /**
   ** Append a warning message text to the set of messages.
   ** <br>
   ** The message will not be bound to any component.
   **
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showWarningMessage(final String text) {
    return showWarningMessage(null, text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showWarningMessage
  /**
   ** Append a warning message text to the set of messages associated with the
   ** specified component identifier, if componentId is not <code>null</code>.
   ** If componentId is <code>null</code>, the text is assumed to not be
   ** associated with any specific component instance.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showWarningMessage(final String componentId, final String text) {
    return showMessage(componentId, createWarningMessage(text));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showErrorMessage
  /**
   ** Append an error message text to the set of messages.
   ** <br>
   ** The message will not be bound to any component.
   **
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showErrorMessage(final String text) {
    return showErrorMessage(null, text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showErrorMessage
  /**
   ** Append an error message text to the set of messages associated with the
   ** specified component identifier, if componentId is not <code>null</code>.
   ** If componentId is <code>null</code>, the text is assumed to not be
   ** associated with any specific component instance.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  text               the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showErrorMessage(final String componentId, final String text) {
    return showMessage(componentId, createErrorMessage(text));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  severity           the severity of the message.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showMessage(final String componentId, final FacesMessage.Severity severity, final String pattern, final Object param) {
    return showMessage(componentId, createMessage(severity, pattern, param));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  severity           the severity of the message.
   ** @param  text               the message text.
   **                            {@link ResourceBundle}.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showMessage(final String componentId, final FacesMessage.Severity severity, final String text) {
    return showMessage(componentId, createMessage(severity, text));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  severity           the severity of the message.
   ** @param  useBundle          if the resource bundle should be used to look
   **                            up a message.
   ** @param  messageKey         the key of the message in the
   **                            {@link ResourceBundle}.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showMessage(final String componentId, final FacesMessage.Severity severity, final boolean useBundle, final String messageKey, final Object param) {
    return showMessage(componentId, createMessage(severity, useBundle, messageKey, param));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showMessage
  /**
   ** Append a {@link FacesMessage} to the set of messages.
   ** <br>
   ** The message will not be bound to any component.
   **
   ** @param  message            the {@link FacesMessage} to append to te set
   **                            of messages.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showMessage(final FacesMessage message) {
    return showMessage(null, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   showMessage
  /**
   ** Append a {@link FacesMessage} to the set of messages associated with the
   ** specified component identifier, if componentId is not <code>null</code>.
   ** If componentId is <code>null</code>, this {@link FacesMessage} is assumed
   ** to not be associated with any specific component instance.
   **
   ** @param  componentId        the ID of the component to attach the message
   **                            to.
   ** @param  message            the message to be appended.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information
   */
  public static FacesMessage showMessage(final String componentId, final FacesMessage message) {
    context().addMessage(componentId, message);
    return message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createInformationMessage
  /**
   ** Generates a {@link FacesMessage} for information purpose from an entry in
   ** the {@link ResourceBundle} of the Faces application.
   **
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createInformationMessage(final String pattern, final Object... param) {
    return createMessage(FacesMessage.SEVERITY_INFO, pattern, param);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWarningMessage
  /**
   ** Generates a {@link FacesMessage} for warning purpose from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createWarningMessage(final String pattern, final Object... param) {
    return createMessage(FacesMessage.SEVERITY_WARN, pattern, param);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createErrorMessage
  /**
   ** Generates a {@link FacesMessage} for error purpose from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createErrorMessage(final String pattern, final Object... param) {
    return createMessage(FacesMessage.SEVERITY_ERROR, pattern, param);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  severity           the severity of the message.
   ** @param  text               the message text.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public static FacesMessage createMessage(final FacesMessage.Severity severity, final String text) {
    return createMessage(severity, text, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  severity           the severity of the message.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createMessage(final FacesMessage.Severity severity, final String pattern, final Object param) {
    return createMessage(severity, pattern, new Object[]{param});
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  severity           the severity of the message.
   ** @param  pattern            the message text containing placeholders to be
   **                            substituted by <code>param</code>.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createMessage(final FacesMessage.Severity severity, final String pattern, final Object... param) {
    return createMessage(severity, false, pattern, param);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createMessage
  /**
   ** Generates a {@link FacesMessage} from an entry in the
   ** {@link ResourceBundle} of the Faces application.
   **
   ** @param  severity           the severity of the message.
   ** @param  useBundle          if the resource bundle should be used to look
   **                            up a message.
   ** @param  text               the key of the message in the
   **                            {@link ResourceBundle}.
   ** @param  param              the parameters to go into the message.
   **
   ** @return                    the {@link FacesMessage} initialized with the
   **                            given information.
   */
  public static FacesMessage createMessage(final FacesMessage.Severity severity, final boolean useBundle, String text, final Object param) {
    if (useBundle)
      text = stringSafely(resourceBundle(), text, null);

    if (param != null) {
      Object[] params = { param };
      text = String.format(text, params);
    }

    final FacesMessage message = new FacesMessage();
    message.setSeverity(severity);
    message.setSummary(text);
    message.setDetail(text);
    return message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resource
  /**
   ** Returns a URL for an application resource mapped to the specified path, if
   ** it exists; otherwise, return <code>null</code>.
   **
   ** @param  path               the application resource path to return an
   **                            input stream for.
   **
   ** @return                    an input stream for an application resource
   **                            mapped to the specified path.
   **
   ** @throws MalformedURLException if the given <code>path</code> doesn't
   **                               evaluate to a well formed {@link URL}.
   **
   ** @see    ExternalContext#getResource(String)
   */
  public static URL resource(final String path)
    throws MalformedURLException {

    return externalContext().getResource(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceAsStream
  /**
   ** Returns an input stream for an application resource mapped to the
   ** specified path, if it exists; otherwise, return <code>null</code>.
   **
   ** @param  path               the application resource path to return an
   **                            input stream for.
   **
   ** @return                    an input stream for an application resource
   **                            mapped to the specified path.
   **
   ** @see     ExternalContext#getResourceAsStream(String)
   */
  public static InputStream resourceAsStream(final String path) {
    return externalContext().getResourceAsStream(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the property bundle that is defined under the
   ** application &lt;message-bundle&gt; element in the faces config.
   **
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String key) {
    return stringSafely(resourceBundle(), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the sepcified resource bundle with respect to
   ** the current {@link Locale}.
   **
   ** @param  bundleName         the name of the {@link ResourceBundle} the
   **                            desired resource is provided by.
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String bundleName, final String key) {
    final ResourceBundle bundle  = BundleFactory.getBundle(bundleName, viewRoot().getLocale());
    return stringSafely(bundle, key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Returns the {@link ResourceBundle} of the current JSF context.
   **
   ** @return                    the {@link ResourceBundle} of the current
   **                            context.
   */
  public static ResourceBundle resourceBundle() {
    return resourceBundle(context());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Returns the {@link ResourceBundle} of a given JSF context.
   **
   ** @param  context            the {@link FacesContext} of the current
   **                            application.
   **
   ** @return                    the {@link ResourceBundle} of the current
   **                            context.
   */
  public static ResourceBundle resourceBundle(final FacesContext context) {
    // get the current JSF application
    final Application application = context.getApplication();
    // get the locale of the current view
    final Locale      locale      = context.getViewRoot().getLocale();
    // load the resource bundle
    return BundleFactory.getBundle(application.getMessageBundle(), locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationInfo
  /**
   ** Returns the implementation information of currently loaded JSF
   ** implementation. E.g. "Mojarra 2.1.7-FCS".
   **
   ** @return                     the implementation information of currently
   **                             loaded JSF implementation.
   **
   ** @see    Package#getImplementationTitle()
   ** @see    Package#getImplementationVersion()
   */
  public static String implementationInfo() {
    Package jsfPackage = FacesContext.class.getPackage();
    return jsfPackage.getImplementationTitle() + " " + jsfPackage.getImplementationVersion();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverInfo
  /**
   ** Returns the server information of currently running application server
   ** implementation.
   **
   ** @return                    the server information of currently running
   **                            application server implementation.
   **
   ** @see    ServletContext#getServerInfo()
   */
  public static String serverInfo() {
    return servletContext().getServerInfo();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueFromExpression
  /**
   ** Evaluates EL expression and returns value.
   ** <p>
   ** <b>Note</b>: The expression method must not take any parameters.
   **
   ** @param  expression         the expression to parse.
   **
   ** @return                    the value of the {@link MethodExpression}.
   */
  public static Object valueFromExpression(final String expression) {
    return valueFromExpression(expression, new Class[0], new Object[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueFromExpression
  /**
   ** Evaluates EL expression and returns value.
   **
   ** @param  <T>                the expected type of the expression.
   ** @param  expression         the expression to parse.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   **
   ** @return                    the value of the {@link ValueExpression}.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T valueFromExpression(final String expression, final Class<T> clazz) {
    return (T)valueExpression(expression, clazz).getValue(expressionContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueFromExpression
  /**
   ** Evaluates EL expression and returns value.
   **
   ** @param  expression         the expression to parse.
   ** @param  types              the array of Class defining the types of the
   **                            parameters.
   ** @param  values             the array of Object defining the values of the
   **                            parameters.
   **
   ** @return                    the value of the {@link MethodExpression}.
   */
  public static Object valueFromExpression(final String expression, final Class[] types, final Object[] values) {
    return valueFromExpression(expression, Object.class, types, values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueFromExpression
  /**
   ** Returns MethodExpression based on the EL expression.
   ** <p>
   ** MethodExpression can then be used to invoke the method.
   **
   ** @param  <T>                the expected type of theexpression.
   ** @param  expression         the expression to parse.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   ** @param  types              the array of Class defining the types of the
   **                            parameters.
   ** @param  values             the array of Object defining the values of the
   **                            parameters.
   **
   ** @return                    the value of the {@link MethodExpression}.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T valueFromExpression(final String expression, final Class<?> clazz, final Class[] types, final Object[] values) {
    return (T)methodExpression(expression, clazz, types).invoke(expressionContext(), values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueExpression
  /**
   ** Parses and returns an expression into a {@link ValueExpression} for later
   ** evaluation. Use this method for expressions that refer to values.
   ** <p>
   ** This performs syntactic validation of the expression. If in doing so it
   ** detects errors, it raise an ELException.
   **
   ** @param  expression         the expression to parse.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   **
   ** @return                    the {@link ValueExpression}.
   */
  public static ValueExpression valueExpression(final String expression, final Class<?> clazz) {
    final ExpressionFactory factory = expressionFactory();
    final ELContext         context = expressionContext();
    return factory.createValueExpression(context, expression, clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   methodExpression
  /**
   ** Returns a {@link MethodExpression} from the current {@link FacesContext}.
   **
   ** @param  expression         the expression to parse.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   ** @param  types              the array of Class defining the types of the
   **                            parameters.
   **
   ** @return                    the {@link MethodExpression}.
   */
  public static MethodExpression methodExpression(final String expression, final Class<?> clazz, final Class[] types) {
    final ExpressionFactory factory = expressionFactory();
    final ELContext         context = expressionContext();
    return factory.createMethodExpression(context, expression, clazz, types);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshPage
  /**
   ** Reload an entire page.
   ** <p>
   ** <b>Note</b>:
   ** Page refresh done that way means sending Http Response - one Http Request
   ** can have only one Http Response. You can't use this code if you have
   ** already sent your response earlier for this particular Http Request.
   **
   ** @see    #context()
   ** @see    #viewRoot()
   ** @see    #viewHandler()
   */
  public static void refreshPage() {
    final FacesContext context = context();
    final String       page    = viewRoot().getId();
    final ViewHandler  handler = viewHandler();
    final UIViewRoot   root    = handler.createView(context, page);
    root.setViewId(page);
    context.setViewRoot(root);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewRootId
  /**
   ** Returns identifier of the current view root.
   **
   ** @return                    the identifier of current view root.
   **
   ** @see    FacesContext#getViewRoot()
   */
  public static String viewRootId() {
    return viewRoot().getId();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewRoot
  /**
   ** Returns the current view root.
   **
   ** @return                    the current view root.
   **
   ** @see    FacesContext#getViewRoot()
   */
  public static UIViewRoot viewRoot() {
    return context().getViewRoot();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewRoot
  /**
   ** Sets the current view root to the given view ID.
   ** <p>
   ** The view ID must start with a leading slash. If an invalid view ID is
   ** given, then the response will simply result in a 404.
   **
   ** @param  viewId             the ID of the view which needs to be set as the
   **                            current view root.
   **
   ** @see    ViewHandler#createView(FacesContext, String)
   ** @see    FacesContext#setViewRoot(UIViewRoot)
   */
  public static void viewRoot(final String viewId) {
    final FacesContext context = context();
    context.setViewRoot(viewHandler().createView(context, viewId));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   viewHandler
  /**
   ** Return the {@link ViewHandler} instance that will be utilized during the
   ** <i>Restore View</i> and <i>Render Response</i> phases of the request
   ** processing lifecycle.
   ** <p>
   ** If not explicitly set, a default implementation must be provided that
   ** performs the functions described in the {@link ViewHandler} description in
   ** the JavaServer Faces Specification.
   **
   ** @return                    the {@link ViewHandler} instance
   **
   ** @see    Application#getViewHandler()
   */
  public static ViewHandler viewHandler() {
    return application().getViewHandler();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navigationHandler
  /**
   ** Return the {@link NavigationHandler} instance that will be passed the
   ** outcome returned by any invoked application action for this web
   ** application.
   ** <br>
   ** If not explicitly set, a default implementation must be provided that
   ** performs the functions described in the {@link NavigationHandler} class
   ** description.
   **
   ** @return                    the {@link NavigationHandler} instance
   **
   ** @see    Application#getViewHandler()
   */
  public static NavigationHandler navigationHandler() {
    return application().getNavigationHandler();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expressionFactory
  /**
   ** Returns the expression factory singleton.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
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
   ** <i>Note that whenever you absolutely need this method to perform a general
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
  // Method:   applicationFromFactory
  /**
   ** Returns the JSF Application singleton from the FactoryFinder.
   ** <p>
   ** This method is an alternative for {@link #application()} for those
   ** situations where the {@link FacesContext} isn't available.
   **
   **
   ** @return                    the faces application singleton.
   **
   ** @see    FactoryFinder
   ** @see    FacesContext#getApplication()
   */
  public static Application applicationFromFactory() {
    return ((ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY)).getApplication();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAttribute
  /**
   ** Sets the application scope attribute value associated with the given name.
   ** @param  name               the application scope attribute name.
   ** @param  value              the application scope attribute value.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  public static void applicationAttribute(String name, Object value) {
    applicationMap().put(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAttribute
  /**
   ** Returns the application scope attribute value associated with the given
   ** name.
   **
   ** @param  <T>                the expected type of the application scope
   **                            attribute.
   ** @param  name               the application scope attribute name.
   **
   ** @return                    the application scope attribute value
   **                            associated with the given name.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T applicationAttribute(String name) {
    return (T)applicationMap().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAttribute
  /**
   ** Returns the application scope attribute value associated with the given
   ** name.
   **
   ** @param  <T>                the expected type of the application scope
   **                            attribute.
   ** @param  context            the faces context used for looking up the
   **                            attribute.
   ** @param  name               the application scope attribute name.
   **
   ** @return                    the application scope attribute value
   **                            associated with the given name.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T applicationAttribute(final FacesContext context, final String name) {
    return (T)context.getExternalContext().getApplicationMap().get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationAttribute
  /**
   ** Returns the application scope attribute value associated with the given
   ** name.
   *
   ** @param  <T>                the expected type of the application scope
   **                            attribute.
   ** @param  context            the servlet context used for looking up the
   **                            attribute.
   ** @param  name               the application scope attribute name.
   **
   ** @return                    the application scope attribute value
   **                            associated with the given name.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T applicationAttribute(final ServletContext context, final String name) {
    return (T)context.getAttribute(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeApplicationAttribute
  /**
   ** Removes the application scope attribute value associated with the given
   ** name.
   **
   ** @param  <T>                the expected type of the application scope
   **                            attribute.
   ** @param  name               the application scope attribute name.
   **
   ** @return                    the application scope attribute value
   **                            previously associated with the given name, or
   **                            <code>null</code> if there is no such
   **                            attribute.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T removeApplicationAttribute(String name) {
    return (T)applicationMap().remove(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   applicationMap
  /**
   ** Returns the application scope map.
   **
   ** @return                    the application scope map.
   **
   ** @see    ExternalContext#getApplicationMap()
   */
  public static Map<String, Object> applicationMap() {
    return externalContext().getApplicationMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   label
  /**
   ** Returns the <code>label</code> property from the specified component.
   **
   ** @param  component          the component of interest.
   **
   ** @return                    the label, if any, of the component.
   */
  public static Object label(final UIComponent component) {
    Object attribute = attribute(component, "label");
    if (attribute == null || (attribute instanceof String && ((String)attribute).length() == 0)) {
      attribute = component.getValueExpression("label");
    }
    // use the "clientId" if there was no label specified.
    if (attribute == null) {
      attribute = component.getClientId(context());
    }
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the named <code>attribute</code> property from the specified
   ** component.
   **
   ** @param  component          the component of interest.
   ** @param  name               the name of the attribute the value should be
   **                            returned for.
   **
   ** @return                    the named <code>attribute</code> property from
   **                            the specified component.
   */
  public static Object attribute(final UIComponent component, final String name) {
    return component.getAttributes().get("label");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   find
	/**
	 ** Returns the UI component matching the given <code>identifier</code> search
   ** expression.
   **
   ** @param  <T>                the expected type of the component.
   ** @param  identifier         the identifier search expression.
   **
   ** @return                    the {@link UIComponent} matching the given
   **                            <code>identifier</code> search expression.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
	 */
	@SuppressWarnings("unchecked")
	public static <T extends UIComponent> T find(final String identifier) {
		return findComponent(viewRoot(), identifier);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findComponent
	/**
	 ** Returns the UI component matching the given <code>identifier</code> search
   ** expression.
   **
   ** @param  <T>                the expected type of the component.
   ** @param  component          the {@link UIComponent} search context.
   ** @param  identifier         the identifier search expression.
   **
   ** @return                    the {@link UIComponent} matching the given
   **                            <code>identifier</code> search expression.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
	 */
  @SuppressWarnings({"unchecked", "cast"})
	public static <T extends UIComponent> T findComponent(final UIComponent component, final String identifier) {
    if (identifier.equals(component.getId()))
      return (T)component;

    UIComponent result = null;
    final Iterator<UIComponent> cursor = component.getFacetsAndChildren();
    while (cursor.hasNext() && (result == null)) {
      final UIComponent children = cursor.next();
      if (identifier.equals(children.getId())) {
        result = children;
        break;
      }
      result = findComponent(children, identifier);
      if (result != null)
        break;
    }
    return (T)result;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findRelatively
	/**
	 ** Returns the UI component matching the given <code>identifier</code> search
   ** expression relative to the point in the component tree of the given
   ** {@link UIComponent} <code>component</code>. For this search both parents
   ** and children are consulted, increasingly moving further away from the
   ** given component. Parents are consulted first, then children.
	 **
   ** @param  <T>                the expected type of the component.
   ** @param  component          the component from which the relative search is
   **                            started.
   ** @param  identifier         the identifier search expression.
   **
   ** @return                    the {@link UIComponent} matching the given
   **                            <code>identifier</code> search expression.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
	 */
  @SuppressWarnings({"unchecked", "cast"})
	public static <T extends UIComponent> T findRelatively(final UIComponent component, final String identifier) {

		if (Utility.empty(identifier))
			return null;

		// search first in the naming container parents of the given component
		UIComponent result = findInParents(component, identifier);

		if (result == null) {
			// if not in the parents, search from the root
			result = findInChildren(JSF.viewRoot(), identifier);
		}

		return (T)result;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findInParents
  /**
   ** Returns the UI component matching the given client ID search expression
   ** relative to the point in the component tree of the given component,
   ** searching only in its parents.
   **
   ** @param  <T>                the expected type of the component.
   ** @param  component          the component from which the relative search is
   **                            started.
   ** @param  clientId           the client ID search expression.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T extends UIComponent> T findInParents(final UIComponent component, final String clientId) {
    if (Utility.empty(clientId))
      return null;

    UIComponent parent = component;
    while (parent != null) {
      UIComponent result = null;
      if (parent instanceof NamingContainer) {
        try {
          result = parent.findComponent(clientId);
        }
        catch (IllegalArgumentException e) {
          continue;
        }
      }

      if (result != null)
        return (T)result;

      parent = parent.getParent();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findInChildren
  /**
   ** Returns the UI component matching the given client ID search expression
   ** relative to the point in the component tree of the given component,
   ** searching only in its children.
   **
   ** @param  <T>                the expected type of the component.
   ** @param  component          the component from which the relative search is
   **                            started.
   ** @param  clientId           the client ID search expression.
   **
   ** @return                    the UI component matching the given client ID
   **                            search expression.
   **
   ** @throws ClassCastException when <code>T</code> is of wrong type.
   **
   ** @see    UIComponent#findComponent(String)
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T extends UIComponent> T findInChildren(final UIComponent component, final String clientId) {

    if (Utility.empty(clientId))
      return null;

    for (UIComponent child : component.getChildren()) {
      UIComponent result = null;
      if (child instanceof NamingContainer) {
        try {
          result = child.findComponent(clientId);
        }
        catch (IllegalArgumentException e) {
          continue;
        }
      }

      if (result == null)
        result = findInChildren(child, clientId);

      if (result != null)
        return (T)result;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remoteUser
  /**
   ** Return the login name of the user making the current request if any;
   ** otherwise, return <code>null</code>.
   **
   ** @return                    the login name of the user making the current
   **                            request if any; otherwise, return
   **                            <code>null</code>.
   */
  public static String remoteUser() {
    return externalContext().getRemoteUser();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userPrincipalName
  /**
   ** Return the login name of the user making the current request if any;
   ** otherwise, return <code>null</code>.
   **
   ** @return                    the login name of the user making the current
   **                            request if any; otherwise, return
   **                            <code>null</code>.
   */
  public static String userPrincipalName() {
    return userPrincipal().getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userPrincipal
  /**
   ** Return the {@link Principal} of the user making the current request if
   ** any; otherwise, return <code>null</code>.
   **
   ** @return                    the {@link Principal} of the user making the
   **                            current request if any; otherwise, return
   **                            <code>null</code>.
   */
  public static Principal userPrincipal() {
    return request().getUserPrincipal();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   servletContext
  /**
   ** Returns the servlet context.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the servlet context.
   **
   ** @see    ExternalContext#getContext()
   */
  public static ServletContext servletContext() {
    return (ServletContext)externalContext().getContext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   externalContext
  /**
   ** Returns the current external context.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
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
  // Method:   expressionContext
  /**
   ** Returns the current expression context.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
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

  public static boolean visitTree(final String component) {
    if (StringUtility.isEmpty(component))
      return false;
    return UIXComponent.visitTree(visitContext(), viewRoot(), new VisitCallback() {
      public VisitResult visit(VisitContext context, UIComponent component) {
        try {
          if (component instanceof UIXCommand && component.getId().equals(component)) {
            final ActionEvent commandActionEvent = new ActionEvent(component);
            commandActionEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
            commandActionEvent.queue();
            return VisitResult.COMPLETE;
          }
          return VisitResult.ACCEPT;
        }
        catch (Exception e) {
          e.printStackTrace();
          return VisitResult.COMPLETE;
        }
      }
    }
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   visitContext
  /**
   ** Returns a context object that is used to hold state relating to performing
   ** a component tree visit.
   ** <p>
   ** This method can be used to obtain a VisitContext instance when all
   ** components should be visited with the default visit hints.
   **
   ** @return                    the current visit context.
   **
   ** @see    VisitContext#createVisitContext(FacesContext)
   */
  public static VisitContext visitContext() {
    return VisitContext.createVisitContext(context());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Sets the given faces context as current instance.
   ** <p>
   ** Use this if you have a custom {@link FacesContext} which you'd like to
   ** (temporarily) use as the current instance of the faces context.
   **
   ** @param  context            the faces context to be set as the current
   **                            instance.
   */
  public static void context(final FacesContext context) {
    ContextSetter.setCurrentInstance(context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the current faces context.
   ** <p>
   ** <i>Note that whenever you absolutely need this method to perform a general
   ** task, you might want to consider to submit a feature request to Faces in
   ** order to add a new utility method which performs exactly this general
   ** task.</i>
   **
   ** @return                    the current faces context.
   **
   ** @see    FacesContext#getCurrentInstance()
   */
  public static FacesContext context() {
    return FacesContext.getCurrentInstance();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   context
  /**
   ** Returns the faces context that's stored in an ELContext.
   ** <p>
   ** Note that this only works for an {@link ELContext} that is created in the
   ** context of JSF.
   **
   ** @param  context            the EL context to obtain the faces context
   **                            from.
   **
   ** @return                    the faces context that's stored in the given
   **                            {@link ELContext}.
   **
   ** @since 1.2
   */
  public static FacesContext context(final ELContext context) {
    return (FacesContext)context.getContext(FacesContext.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contextObject
  /**
   ** Evaluates the expression relative to the current {@link ELContext}, and
   ** returns the resulting value.
   ** <p>
   ** The resulting value is automatically coerced to the type returned by
   ** getExpectedType(), which was provided to the ExpressionFactory when this
   ** expression was created.
   ** <p>
   ** Note that this only works for an {@link ELContext} that is created in the
   ** context of JSF.
   **
   ** @param  <T>                the type of the expression to parse.
   ** @param  expression         the expression to parse.
   ** @param  clazz              the type the result of the expression will be
   **                            coerced to after evaluation.
   **
   ** @return                    the result of the expression evaluation.
   **
   ** @since 1.2
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> T contextObject(final String expression, final Class<T> clazz) {
    final ValueExpression value = valueExpression(expression, clazz);
    return (value == null) ? null : (T)value.getValue(expressionContext());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeRedirectURL
  /**
   ** Helper method to normalize the given URL for a redirect. If the given URL
   ** does not start with <code>http://</code>, <code>https://</code> or
   ** <code>/</code>, then the request context path will be prepended, otherwise
   ** it will be unmodified.
   */
  private static String normalizeRedirectURL(String url) {
    if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("/"))
      url = requestContextPath() + "/" + url;

    return url;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeURLParams
  /**
   ** Helper method to encode the given URL parameters using UTF-8.
   */
  private static Object[] encodeURLParams(final String... params) {
    if (params == null)
      return new Object[0];

    final Object[] encodedParams = new Object[params.length];
    for (int i = 0; i < params.length; i++) {
      try {
        encodedParams[i] = URLEncoder.encode(params[i], "UTF-8");
      }
      catch (UnsupportedEncodingException e) {
        throw new UnsupportedOperationException(UNSUPPORTED_ENCODING, e);
      }
    }
    return encodedParams;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSafely
  /**
   ** Internal method to proxy for resource keys that don't exist.
   **
   ** @param  bundle             the {@link ResourceBundle} the desired resource
   **                            is provided by.
   ** @param  key                string message key.
   ** @param  defaultValue       placeholder string if the key isn't mapped in
   **                            the resource bundle.
   **
   ** @return                    resource choice or placeholder error String
   */
  private static String stringSafely(final ResourceBundle bundle, final String key, final String defaultValue) {
    String resource = null;
    try {
      resource = bundle.getString(key);
    }
    catch (MissingResourceException e) {
      resource = (defaultValue != null) ? defaultValue : String.format(MISSING_RESOURCE, key);
    }
    return resource;
  }
}