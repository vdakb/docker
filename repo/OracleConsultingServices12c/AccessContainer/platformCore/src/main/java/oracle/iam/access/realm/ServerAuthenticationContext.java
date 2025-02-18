/*
    Oracle Deutschland BV & Co. KG

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
    Subsystem   :   JEE Server Autehntication

    File        :   ServerAuthenticationContext.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerAuthenticationContext.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.realm;

import java.util.Map;
import java.util.Collections;

import java.io.IOException;

import java.security.Principal;

import javax.security.auth.Subject;

import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;

import javax.security.auth.callback.CallbackHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.soteria.mechanisms.jaspic.Jaspic;

////////////////////////////////////////////////////////////////////////////////
// class ServerAuthenticationContext
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A convenience context that provides access to JASPIC Servlet Profile
 ** specific types and functionality.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerAuthenticationContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private CallbackHandler     handler;
  private Map<String, String> option;
	private MessageInfo         message; 
  private Subject             subject;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerAuthenticationContext</code> with the specified
   ** properties.
   **
   ** @param  handler            the handler that the runtime provided to
   **                            authentication context.
   **                            <br>
   **                            Allowed object is {@link CallbackHandler}.
   ** @param  subject            the subject for which authentication is to take
   **                            place.
   **                            <br>
   **                            Possible object is {@link Subject}.
   ** @param  message            the message info instance for the current
   **                            request.
   **                            <br>
   **                            Possible object is {@link MessageInfo}.
   ** @param  option             the module options that were set on the
   **                            authentication module to which this context
   **                            belongs.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public ServerAuthenticationContext(final CallbackHandler handler, final MessageInfo message, final Subject subject, final Map<String, String> option) {
    // ensure inheritance
    super();

    // ensure inheritance
    this.handler = handler;
    this.message = message;
    this.subject = subject;
    this.option  = (option != null) ? Collections.unmodifiableMap(option) : Collections.emptyMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handler
  /**
   ** Returns the handler that the runtime provided to authentication context.
   **
   ** @return                    the handler that the runtime provided to
   **                            authentication context.
   **                            <br>
   **                            Possible object is {@link CallbackHandler}.
   */
  public final CallbackHandler handler() {
    return this.handler;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   message
  /**
   ** Returns the message info instance for the current request.
   **
   ** @return                    the message info instance for the current
   **                            request.
   **                            <br>
   **                            Possible object is {@link MessageInfo}.
   */
  public MessageInfo message() {
    return this.message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Returns the subject for which authentication is to take place.
   **
   ** @return                    the subject for which authentication is to take
   **                            place.
   **                            <br>
   **                            Possible object is {@link Subject}.
   */
  public Subject subject() {
    return this.subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   option
  /**
   ** Returns the module options that were set on the authentication module to
   ** which this context belongs.
   **
   ** @return                    the module options that were set on the
   **                            authentication module to which this context
   **                            belongs.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} as the value.
   */
  public Map<String, String> option() {
    return this.option;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isProtected
  /**
   ** Checks if the current request is to a protected resource or not.
   ** <br>
   ** A protected resource is a resource (e.g. a Servlet, JSF page, JSP page
   ** etc.) for which a constraint has been defined in e.g.
   ** <code>web.xml</code>.
   **
   ** @return                    <code>true</code> if a protected resource was
   **                            requested, <code>false</code> if a public
   **                            resource was requested.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean isProtected() {
    return Jaspic.isProtectedResource(this.message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   request
  /**
   ** Returns the request object associated with the current request.
   **
   ** @return                    the request object associated with the current
   **                            request.
   **                            <br>
   **                            Possible object is {@link HttpServletRequest}.
   */
  public final HttpServletRequest request() {
    return (HttpServletRequest)this.message.getRequestMessage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   response
  /**
   ** Returns the response object associated with the current request.
   **
   ** @return                    the response object associated with the current
   **                            request.
   **                            <br>
   **                            Possible object is {@link HttpServletResponse}.
   */
  public final HttpServletResponse response() {
    return (HttpServletResponse)this.message.getResponseMessage();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doNothing
  /**
   ** Instructs the container to "do nothing".
   ** <p>
   ** This is a somewhat peculiar requirement of JASPIC, which incidentally
   ** almost no containers actually require or enforce.
   ** <p>
   ** When intending to do nothing, most JASPIC auth modules simply return
   ** "SUCCESS", but according to the JASPIC spec the handler <b>must</b> have
   ** been used when returning that status. Because of this JASPIC implicitly
   ** defines a "protocol" that must be followed in this case; invoking the
   ** CallerPrincipalCallback handler with a <code>null</code> as the username.
   ** <p>
   ** As a convenience this method returns SUCCESS, so this method can be used
   **  one fluent return statement from an auth module.
   *
   * @return {@link AuthStatus#SUCCESS}
   */
  public AuthStatus doNothing() {
    Jaspic.notifyContainerAboutLogin(this.subject, this.handler, (Principal)null, Collections.emptySet());
    return AuthStatus.SUCCESS;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanSubject
  /**
   ** Sets the response status to 404 (not found).
   ** <p>
   ** As a convenience this method returns SEND_FAILURE, so this method can be
   ** used in one fluent return statement from an auth module.
   **
   ** @return {@link AuthStatus#SEND_FAILURE}
   */
  public AuthStatus responseNotFound() {
    try {
      response().sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }
    return AuthStatus.SEND_FAILURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanSubject
  /**
   ** Remove method specific principals and credentials from the subject.
   ** <p>
   ** Called in response to a {@link HttpServletRequest#logout()} call.
   */
  public void cleanSubject() {
    Jaspic.cleanSubject(this.subject);
  }
}