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

    File        :   SignInHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SignInHandler.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces.shell.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.ResourceBundle;

import java.io.IOException;

import javax.faces.context.ExternalContext;

import javax.faces.event.ActionEvent;

import javax.faces.application.FacesMessage;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.hst.foundation.faces.JSF;
import oracle.hst.foundation.faces.ManagedBean;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class SignInHandler
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Internal use only. Backing bean for login actions.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SignInHandler extends ManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String SUCCESS_URL             = "successURL";
  public static final String SIGNOUT_URL             = "signoutURL";
  public static final String LOGIN_ERROR             = "loginError";
  public static final String FACES_PATH              = "/faces";
  public static final String LOCAL_SIGNIN            = "/adfAuthentication?success_url=%s";
  public static final String LOCAL_SIGNOUT           = "/adfAuthentication?logout=true&end_url=%s";

  private static final String FORWARD_ERROR_DETAIL   = "forward_error_detail";
  private static final String FORWARD_ERROR_MESSAGE  = "forward_error_message";
  private static final String REDIRECT_ERROR_DETAIL  = "redirect_error_detail";
  private static final String REDIRECT_ERROR_MESSAGE = "redirect_error_message";
  private static final String SIGNIN_ERROR_DETAIL    = "signin_error_detail";
  private static final String SIGNIN_ERROR_MESSAGE   = "signin_error_message";

  private static final String EXPRESSION             = "#{loginHandler}";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean        error;
  private String         username;
  private String         password;

  private ResourceBundle bundle    = BundleFactory.getBundle("oracle.hst.foundation.faces.shell.bundle.foundation", JSF.locale());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SignInHandler</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SignInHandler() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUsername
  /**
   ** Sets the value of the username property.
   **
   ** @param  value              the value of the username property.
   **                            Allowed object is {@link String}.
   */
  public void setUsername(final String value) {
    this.username = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUsername
  /**
   ** Returns the value of the username property.
   **
   ** @return                    the value of the username property.
   **                            Possible object is {@link String}.
   */
  public String getUsername() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPassword
  /**
   ** Sets the value of the password property.
   **
   ** @param  value              the value of the username property.
   **                            Allowed object is {@link String}.
   */
  public void setPassword(final String value) {
    this.password = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPassword
  /**
   ** Returns the value of the password property.
   **
   ** @return                    the value of the password property.
   **                            Possible object is {@link String}.
   */
  public String getPassword() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isSingleSignOn
  /**
   ** Determines if the application is authenticated by Oracle Access Manager.
   **
   ** @return                    <code>true</code> if the application is
   **                            authenticated by Oracle Access Manager.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isSingleSignOn() {
    return !StringUtility.isEmpty(JSF.requestHeaderValue("OAM_REMOTE_USER"));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isError
  /**
   ** Returns the value of the error property.
   **
   ** @return                    the value of the error property.
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isError() {
    if (!this.error) {
      String lerr = (String)JSF.requestScope().get(LOGIN_ERROR);
      if (lerr == null) {
        final Map<String, Object> sessionScope = JSF.sessionScope();
        lerr = (String)sessionScope.get(LOGIN_ERROR);
        if (lerr != null) {
          sessionScope.remove(LOGIN_ERROR);
        }
      }
      this.error = Boolean.valueOf(lerr).booleanValue();
    }
    return this.error;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** SignIn Handler Context that is in the current request scope.
   **
   ** @return                    SignIn Handler Context.
   **                            <code>null</code> if you are not running in
   **                            Shell env and not taking UI shell as parameter-
   */
  public static SignInHandler instance() {
    return JSF.valueFromExpression(EXPRESSION, SignInHandler.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Handle the events occured in the UIXForm component managing the login
   ** information in a user session.
   ** <p>
   ** Authenticate the user using the username and password and setting that
   ** user information into the session.
   ** <p>
   ** Further request processing will be forwarded to the URL which is
   ** configured as
   ** <pre>
   **   &lt;f:attribute name=&quot;successURL&quot; value=&quot;#{attrs.successURL}&quot;/&gt;
   ** </pre>
   ** and passed as the attribute <code>successURL</code> to the page template.
   **
   ** @param  event              the {@link ActionEvent} delivered from the
   **                            action occured in a UIXForm component.
   */
  public void submit(final ActionEvent event) {
    final String successor = (String)event.getComponent().getAttributes().get(SUCCESS_URL);
    final String landmmark = String.format(LOCAL_SIGNIN, StringUtility.isEmpty(successor) ? FACES_PATH + JSF.context().getViewRoot().getViewId() : successor);
    authenticate(landmmark);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirect
  /**
   ** Sends a temporary redirect response to the client using the specified
   ** redirect location URL.
   ** <br>
   ** This method can accept relative URLs; the servlet container will convert
   ** the relative URL to an absolute URL before sending the response to the
   ** client.
   ** <p>
   ** If the location is relative without a leading '/' the container interprets
   ** it as relative to the current request URI. If the location is relative
   ** with a leading '/' the container interprets it as relative to the servlet
   ** container root.
   **
   ** @param  location           the redirect location URL
   **                            If it is relative, it must be relative against
   **                            the current servlet.
   */
  public void redirect(final String location) {
    try {
      JSF.response().sendRedirect(location);
    }
    catch (IOException e) {
      redirectThrowable(e);
    }
    finally {
      JSF.context().responseComplete();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forward
  /**
   ** Forwards the request to another resource (servlet, JSP file, or HTML file)
   ** on the same context. This method allows one servlet to do preliminary
   ** processing of a request and another resource to generate the response.
   ** <p>
   ** The ServletRequest object has its path elements and parameters adjusted to
   ** match the path of the target resource.
   **
   ** @param  path               the String specifying the pathname to the
   **                            resource. If it is relative, it must be
   **                            relative against the current servlet.
   */
  public void forward(final String path) {
    final ExternalContext     context  = JSF.externalContext();
    final HttpServletRequest  request  = (HttpServletRequest)context.getRequest();
    final HttpServletResponse response = (HttpServletResponse)context.getResponse();
    forward(request, response, path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticate
  /**
   ** Authenticate the user using the username and password and setting that
   ** user information into the session.
   **
   ** @param  successURL         the String specifying the pathname to the
   **                            resource to be forwarded after successful
   **                            authetication. If it is relative, it must be
   **                            relative against the current servlet.
   */
  private void authenticate(final String successURL) {
    final HttpServletRequest  request  = JSF.request();
    final HttpServletResponse response = JSF.response();
    try {
      final Class<?>   authenticator = Class.forName("weblogic.servlet.security.ServletAuthentication");
      final Class<?>[] authnArgs     = new Class<?>[] {String.class, String.class, HttpServletRequest.class, HttpServletResponse.class};
      final Object[]   authnParam    = new Object[]   {this.username, this.password, request, response};
      final Method     authnMethod   = authenticator.getMethod("login", authnArgs);
      final Integer    authnOutput   = (Integer)authnMethod.invoke(null, authnParam);
      final Field      authnStatus   = authenticator.getDeclaredField("AUTHENTICATED");
      if (authnStatus.getInt(null) == authnOutput.intValue()) {
        final Class<?>[] sessionArgs   = new Class<?>[] {HttpServletRequest.class};
        final Object[]   sessionParam  = new Object[]{request};
        final Method     sessionMethod = authenticator.getMethod("generateNewSessionID", sessionArgs);
        sessionMethod.invoke(null, sessionParam);
        forward(request, response, successURL);
      }
      else {
        this.error = true;
      }
    }
    catch (ClassNotFoundException e) {
      authenticateThrowable(e);
    }
    catch (IllegalAccessException e) {
      authenticateThrowable(e);
    }
    catch (NoSuchMethodException e) {
      authenticateThrowable(e);
    }
    catch (InvocationTargetException e) {
      this.error = true;
      authenticateThrowable(e);
    }
    catch (NoSuchFieldException e) {
      authenticateThrowable(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forward
  /**
   ** Forwards the request to another resource (servlet, JSP file, or HTML file)
   ** on the server. This method allows one servlet to do preliminary processing
   ** of a request and another resource to generate the response.
   ** <p>
   ** The ServletRequest object has its path elements and parameters adjusted to
   ** match the path of the target resource.
   **
   ** @param  request            the {@link HttpServletRequest object that
   **                            represents the request the client makes of the
   **                            servlet.
   ** @param  response           the {@link HttpServletResponse} object that
   **                            represents the response the servlet returns to
   **                            the client .
   ** @param  path               the String specifying the pathname to the
   **                            resource. If it is relative, it must be
   **                            relative against the current servlet.
   */
  private void forward(final HttpServletRequest request, final HttpServletResponse response, final String path) {
    final RequestDispatcher dispatcher = request.getRequestDispatcher(path);
    try {
      dispatcher.forward(request, response);
    }
    catch (ServletException e) {
      forwardThrowable(e);
    }
    catch (IOException e) {
      forwardThrowable(e);
    }
    JSF.context().responseComplete();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateThrowable
  /**
   ** Reports any error which happend in a way that the frontend renders it
   ** properly.
   **
   ** @param  throwable          the {@link Throwable} which occured during
   **                            processing.
   */
  private void authenticateThrowable(final Throwable throwable) {
    facesThrowable(throwable, SIGNIN_ERROR_MESSAGE, SIGNIN_ERROR_DETAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   forwardThrowable
  /**
   ** Reports any error which happend in a way that the frontend renders it
   ** properly.
   **
   ** @param  throwable          the {@link Throwable} which occured during
   **                            processing.
   */
  private void forwardThrowable(final Throwable throwable) {
    facesThrowable(throwable, FORWARD_ERROR_MESSAGE, FORWARD_ERROR_DETAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   redirectThrowable
  /**
   ** Reports any error which happend in a way that the frontend renders it
   ** properly.
   **
   ** @param  throwable          the {@link Throwable} which occured during
   **                            processing.
   */
  private void redirectThrowable(final Throwable throwable) {
    facesThrowable(throwable, REDIRECT_ERROR_MESSAGE, REDIRECT_ERROR_DETAIL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facesThrowable
  /**
   ** Reports any error which happend in a way that the frontend renders it
   ** properly.
   **
   ** @param  throwable          the {@link Throwable} which occured during
   **                            processing.
   ** @param  title              the bundle key for the title part of the
   **                            message.
   ** @param  detail             the bundle key for the detailed part of the
   **                            message.
   */
  private void facesThrowable(final Throwable throwable, final String message, final String detail) {
    final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle.getString(message), String.format(bundle.getString(detail), throwable.getClass().getCanonicalName()));
    JSF.context().addMessage(null, msg);
    throwable.printStackTrace();
  }
}