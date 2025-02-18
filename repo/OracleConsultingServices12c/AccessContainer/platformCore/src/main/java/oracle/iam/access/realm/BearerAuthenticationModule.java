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

    File        :   BearerAuthenticationModule.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    BearerAuthenticationModule.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.realm;

import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

////////////////////////////////////////////////////////////////////////////////
// class BearerAuthenticationModule
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Authentication module that authenticates based on a bearer token in the
 ** request.
 ** <p>
 ** Token to username/roles mapping is delegated to an implementation of
 ** {@link TokenAuthenticator}, which should be registered as CDI bean.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This module makes the simplifying assumption that CDI is available in a SAM.
 ** Unfortunately this is not true for every implementation.
 ** See https://java.net/jira/browse/JASPIC_SPEC-14
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class BearerAuthenticationModule extends ServerAuthenticationModule {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BearerAuthenticationModule</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public BearerAuthenticationModule() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRequest (overridden)
  /**
   ** Authenticate a received service request.
   ** <br>
   ** This method is called to transform the mechanism-specific request message
   ** acquired by calling getRequestMessage (on messageInfo) into the validated
   ** application message to be returned to the message processing runtime. If
   ** the received message is a (mechanism-specific) meta-message, the method
   ** implementation must attempt to transform the meta-message into a
   ** corresponding mechanism-specific response message, or to the validated
   ** application request message. The runtime will bind a validated application
   ** message into the the corresponding service invocation.
   ** <p>
   ** This method conveys the outcome of its message processing either by
   ** returning an {@link AuthStatus} value or by throwing an
   ** {@link AuthException}.
   **
   ** @param  request            a contextual object that encapsulates the
   **                            client request, and may be used to save state
   **                            across a sequence of calls made to the methods
   **                            of this interface for the purpose of completing
   **                            a secure message exchange.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           a contextual object that encapsulates the
   **                            server response objects.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  context            a {@link Subject} that represents the recipient
   **                            of the service request, or <code>null</code>.
   **                            <br>
   **                            It may be used by the method implementation as
   **                            the source of <code>Principal</code>s or
   **                            credentials to be used to validate the request.
   **                            <br>
   **                            If the {@link Subject} is not
   **                            <code>null</code>, the method implementation
   **                            may add additional <code>Principal</code>s or
   **                            credentials (pertaining to the recipient of the
   **                            service request) to the @link Subject}.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   */
  @Override
  @SuppressWarnings("unused")
  public AuthStatus validateRequest(final HttpServletRequest request, final HttpServletResponse response, final ServerAuthenticationContext context)
    throws AuthException {

    String token = token(request);
    if (token != null) {
      // if a token is present, authenticate with it whether this is strictly
      // required or not.
    }
    return (context.isProtected()) ? context.responseNotFound() : context.doNothing();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Factory nethod to create a {@link Token} Authentication
   ** <code>Credential</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   **
   ** @return                    the {@link Token} Authentication
   **                            <code>Credential</code> populated with the
   **                            given <code>token</code>.
   **                            <br>
   **                            Possible objject is {@link Token}.
   */
  static String token(final HttpServletRequest request) {
    String credential = extract(request, "Bearer");
    // if authorization header is not used, check query parameter where token
    // can be passed as well
    if (credential == null) {
      credential = request.getParameter("access_token");
    }
    return credential;
  }
}