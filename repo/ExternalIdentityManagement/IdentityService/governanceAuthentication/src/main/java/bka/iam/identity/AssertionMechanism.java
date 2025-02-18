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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   AssertionMechanism.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionMechanism.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.enterprise.inject.spi.CDI;

import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.AuthenticationException;

import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;

import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.identitystore.CredentialValidationResult;

////////////////////////////////////////////////////////////////////////////////
// abstract class AssertionMechanism
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>AssertionMechanism</code> implements
 ** {@link HttpAuthenticationMechanism} for obtaining a caller's credentials
 ** from an Authentication Provider, using the HTTP protocol where necessary.
 ** <p>
 ** This is used to help in securing Servlet endpoints, including endpoints that
 ** may be build on top of Servlet like JAX-RS endpoints and JSF views.
 ** <br>
 ** It specifically is not used for endpoints such as remote EJB beans or (JMS)
 ** message driven beans.
 ** <p>
 ** A {@link HttpAuthenticationMechanism} is essentially a Servlet specific and
 ** CDI enabled version of the ServerAuthModule that adheres to the Servlet
 ** Container Profile. See the JASPIC spec for further details on this.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AssertionMechanism implements HttpAuthenticationMechanism {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link HttpAuthenticationMechanism} that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public AssertionMechanism() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRequest (HttpAuthenticationMechanism)
  /**
   ** Authenticate an HTTP request.
   ** <p>
   ** This method is called in response to an HTTP client request for a
   ** resource, and is always invoked before any Filter or HttpServlet.
   ** <br>
   ** Additionally this method is called in response to
   ** HttpServletRequest.authenticate(HttpServletResponse)
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** By default this method is always called for every request, independent of
   ** whether the request is to a protected or non-protected resource, or
   ** whether a caller was successfully authenticated before within the same
   ** HTTP session or not.
   ** <p>
   ** A CDI/Interceptor spec interceptor can be used to prevent calls to this
   ** method if needed. See {@code AutoApplySession} and {@code RememberMe} for
   ** two examples.
   **
   ** @param  request            contains the request the client has made.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           contains the response that will be send to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  context            the context for interacting with the container.
   **                            <br>
   **                            Allowed object is {@link HttpMessageContext}.
   **
   ** @return                    the completion status of the processing
   **                            performed by this method.
   **                            <br>
   **                            Possible object is
   **                            {@link AuthenticationStatus}.
   **
   ** @throws AuthenticationException if the validation of any token obtained
   **                                 from the request fails.
   */
  @Override
  public AuthenticationStatus validateRequest(final HttpServletRequest request, final HttpServletResponse response, final HttpMessageContext context)
    throws AuthenticationException {

    // as long as we don't know who it is do nothing
    if (authenticated(context))
      return context.doNothing();

    final IdentityStoreHandler handler = CDI.current().select(IdentityStoreHandler.class).get();
    try {
      final CredentialValidationResult current = handler.validate(token(request));
      // communicate the details of the authenticated user to the container
      // in many cases the underlying handler will just store the details and
      // the container will actually handle the login after we return from this
      // method.
      if (current.getStatus() == CredentialValidationResult.Status.VALID)
        return context.notifyContainerAboutLogin(current.getCallerPrincipal(), current.getCallerGroups());
      else {
        response(response, 403, "forbidden", "Ooops");
        return context.responseUnauthorized();
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
      response(response, 403, "forbidden", e);
      return context.responseUnauthorized();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanSubject (HttpAuthenticationMechanism)
  /**
   ** Remove mechanism specific principals and credentials from the subject and
   ** any other state the mechanism might have used.
   ** <p>
   ** This method is called in response to HttpServletRequest.logout() and gives
   ** the authentication mechanism the option to remove any state associated
   ** with an earlier established authenticated identity. For example, an
   ** authentication mechanism that stores state within a cookie can send remove
   ** that cookie here.
   **
   ** @param  request            contains the request the client has made.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           contains the response that will be send to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  context            the context for interacting with the container.
   **                            <br>
   **                            Allowed object is {@link HttpMessageContext}.
   */
  @Override
  public void cleanSubject(final HttpServletRequest request, final HttpServletResponse response, final HttpMessageContext context) {
    context.cleanClientSubject();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Factory method to create a authentication <code>Credential</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   **
   ** @return                    the authentication <code>Credential</code>
   **                            populated from the given <code>request</code>.
   **                            <br>
   **                            Possible object is {@link AssertionCredential}.
   */
  protected abstract AssertionCredential token(final HttpServletRequest request);

  //////////////////////////////////////////////////////////////////////////////
  // Method:  extract
  /**
   ** Parses a value of the <code>Authorization</code> header in the form of
   ** <code>Basic a892bf3e284da9bb40648ab10</code> or
   ** <code>Bearer a892bf3e284da9bb40648ab10</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   ** @param  header             the authorization header; e.g.
   **                            <code>authorization</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  scheme             the authorization scheme; e.g.
   **                            <code>Bearer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a token string representing an identity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static String extract(final HttpServletRequest request, final String header, final String scheme) {
    // get the Authorization header from the request
    String authorization = header(request, header);
    // prevent bogus state
    if (authorization == null)
      return null;

    // check if the Authorization header is valid
    // it must not be null and must be prefixed with scheme plus a whitespace
    // the authentication scheme comparison must be case-insensitive
    final int space = authorization.indexOf(' ');
    if (space < 1)
      return null;

    return scheme.equalsIgnoreCase(authorization.substring(0, space)) ? authorization.substring(space + 1) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  header
  /**
   ** Parses a value of the <code>Authorization</code> header in the form of
   ** <code>Basic a892bf3e284da9bb40648ab10</code> or
   ** <code>Bearer a892bf3e284da9bb40648ab10</code>.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   ** @param  header             the value of the <code>Authorization</code>
   **                            header.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a token string representing an identity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  static String header(final HttpServletRequest request, final String header) {
    return request.getHeader(header);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticated
  /**
   ** Determines if the current context is authenticated.
   **
   ** @param  context            the context for interacting with the container.
   **                            <br>
   **                            Allowed object is {@link HttpMessageContext}.
   **
   ** @return                    <code>true</code> if the current context is
   **                            authenticated; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  static boolean authenticated(final HttpMessageContext context) {
    return context.getAuthParameters().getCredential() != null;
  }
  
  static void response(final HttpServletResponse response, final int status, final String type, final Throwable t) {
    t.printStackTrace(System.err);
    response(response, status, type, t.getMessage());
  }

  static void response(final HttpServletResponse response, final int status, final String type, final String message) {
    try {
      response.setStatus(status);
      response.getOutputStream().print(response(status, type, message));
    }
    catch (IOException e) {
      e.printStackTrace(System.err);
    }
  }

  static String response(final int status, final String type, final String detail) {
    final StringBuilder builder = new StringBuilder("{");
    builder.append("\"status\":\"" + status + "\"" );
    builder.append("\"type\":\"" + type + "\"" );
    builder.append("\"detail:\"" + detail + "\"" );
    return builder.append("}").toString();
  }
}