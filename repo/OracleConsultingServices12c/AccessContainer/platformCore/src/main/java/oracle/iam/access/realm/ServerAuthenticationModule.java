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

    File        :   ServerAuthenticationModule.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerAuthenticationModule.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.realm;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.security.auth.Subject;

import javax.security.auth.callback.CallbackHandler;

import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessagePolicy;

import javax.security.auth.message.module.ServerAuthModule;

////////////////////////////////////////////////////////////////////////////////
// class ServerAuthenticationModule
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ServerAuthenticationModule</code> validates client requests and
 ** secures responses to the client.
 ** <p>
 ** The implementation assumes that may be used to secure different requests as
 ** different clients. It's also assumed that it may be used concurrently by
 ** multiple callers. For that purpose the module save and restore any state
 ** properly as necessary.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerAuthenticationModule implements ServerAuthModule {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	private CallbackHandler     handler;
	private Map<String, String> option;

	private final Class<?>[]    supported = new Class[]{
    HttpServletRequest.class
  , HttpServletResponse.class
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerAuthenticationModule</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public ServerAuthenticationModule() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSupportedMessageTypes (ServerAuthModule)
	/**
	 ** A Servlet Container Profile compliant implementation should return
   ** {@link HttpServletRequest} and {@link HttpServletResponse}, so the
   ** delegation class {@link ServerAuthContext} can choose the right SAM to
   ** delegate to.
   **
   ** @return                    an array of {@link Class} objects, with at
   **                            least one element defining a message type
   **                            supported by the module.
   **                            <br>
   **                            Possible object is array of {@link Class}.
	 */
	@Override
	public Class<?>[] getSupportedMessageTypes() {
		return this.supported;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (ServerAuthModule)
  /**
   ** Initialize this module with request and response message policies to
   ** enforce, a {@link CallbackHandler}, and any module-specific configuration
   ** properties.
   ** <p>
   ** The request policy and the response policy must not <b>both</b> be
   ** <code>null</code>.
   **
   ** @param  request            the request policy this module must enforce, or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link MessagePolicy}.
   ** @param  response           the response policy this module must enforce,
   **                            or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link MessagePolicy}.
   ** @param  handler            the {@link CallbackHandler} used to request
   **                            information.
   **                            <br>
   **                            Allowed object is {@link CallbackHandler}.
   ** @param  option             the key-value pairs of module-specific
   **                            configuration properties.
   **                            <br>
   **                            Allowed object is {@link Map}.
   **
   ** @throws AuthException      if module initialization fails, including for
   **                            the case where the options argument contains
   **                            elements that are not supported by the module.
   */
	@Override
	@SuppressWarnings("unchecked")
	public final void initialize(final MessagePolicy request, final MessagePolicy response, final CallbackHandler handler, final Map option)
    throws AuthException {

		this.handler = handler;
		this.option  = option;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureResponse (ServerAuth)
  /**
   ** Secure a service response before sending it to the client.
   ** <br>
   ** This method is called to transform the response message acquired by
   ** calling getResponseMessage (on message) into the mechanism-specific form
   ** to be sent by the runtime.
   ** <br>
   ** This method conveys the outcome of its message processing either by
   ** returning an AuthStatus value or by throwing an AuthException.
   **
   ** @param  message            a contextual object that encapsulates the
   **                            client request and server response objects, and
   **                            that may be used to save state across a
   **                            sequence of calls made to the methods of this
   **                            interface for the purpose of completing a
   **                            secure message exchange.
   **                            <br>
   **                            Allowed object is {@link MessageInfo}.
   ** @param  recipient          a {@link Subject} that represents the source of
   **                            the service response, or <code>null</code>. It
   **                            may be used by the method implementation to
   **                            retrieve Principals and credentials necessary
   **                            to secure the response. If the {@link Subject}
   **                            is not <code>null</code>, the method
   **                            implementation may add additional Principals or
   **                            credentials (pertaining to the source of the
   **                            service response) to the {@link Subject}.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   ** @return                    An {@link AuthStatus} object representing the
   **                            completion status of the processing performed
   **                            by the method. The {@link AuthStatus} values
   **                            that may be returned by this method are defined
   **                            as follows:
   **                            <ul>
   **                              <li>AuthStatus.SUCCESS
   **                                  <br>
   **                                  The application request message was
   **                                  successfully validated. The validated
   **                                  request message is available by calling
   **                                  getRequestMessage on
   **                                  <code>message</code>.
   **                              <li>AuthStatus.SEND_SUCCESS
   **                                  <br>
   **                                  To indicate that validation/processing of
   **                                  the request message successfully produced
   **                                  the secured application response message
   **                                  (in <code>message</code>). The secured
   **                                  response message is available by calling
   **                                  getResponseMessage on
   **                                  <code>message</code>.
   **                              <li>AuthStatus.SEND_CONTINUE
   **                                  <br>
   **                                  To indicate that message validation is
   **                                  incomplete, and that a preliminary
   **                                  response was returned as the response
   **                                  message in messageInfo. When this status
   **                                  value is returned to challenge an
   **                                  application request message, the
   **                                  challenged request must be saved by the
   **                                  authentication module such that it can be
   **                                  recovered when the module's
   **                                  validateRequest message is called to
   **                                  process the request returned for the
   **                                  challenge.
   **                              <li>AuthStatus.SEND_FAILURE
   **                                  To indicate that message validation
   **                                  failed and that an appropriate failure
   **                                  response message is available by calling
   **                                  getResponseMessage on
   **                                  <code>message</code>.
   **                            </ul>
   **
   ** @throws AuthException      when the message processing failed without
   **                            establishing a failure response message
   **                            (in <code>message</code>).
   */
	@Override
	public AuthStatus secureResponse(final MessageInfo message, final Subject recipient)
    throws AuthException {

		return AuthStatus.SEND_SUCCESS;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanSubject (ServerAuth)
  /**
   ** Remove method specific principals and credentials from the subject.
   ** <p>
   ** Called in response to a {@link HttpServletRequest#logout()} call.
   **
   ** @param  message            a contextual object that encapsulates the
   **                            client request and server response objects, and
   **                            that may be used to save state across a
   **                            sequence of calls made to the methods of this
   **                            interface for the purpose of completing a
   **                            secure message exchange.
   **                            <br>
   **                            Allowed object is {@link MessageInfo}.
   ** @param  subject            the {@link Subject} instance from which the
   **                            <code>Principal</code>s and credentials are to
   **                            be removed.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   */
  @Override
  public void cleanSubject(final MessageInfo message, final Subject subject)
    throws AuthException {

    ServerAuthenticationContext context = new ServerAuthenticationContext(this.handler, message, subject, this.option);
    cleanSubject(context.request(), context.response(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRequest (ServerAuth)
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
   ** @param  message            a contextual object that encapsulates the
   **                            client request and server response objects, and
   **                            that may be used to save state across a
   **                            sequence of calls made to the methods of this
   **                            interface for the purpose of completing a
   **                            secure message exchange.
   **                            <br>
   **                            Allowed object is {@link MessageInfo}.
   ** @param  source             a {@link Subject} that represents the source of
   **                            the service request. It is used by the method
   **                            implementation to store <code>Principal</code>s
   **                            and credentials validated in the request.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   ** @param  recipient          a {@link Subject} that represents the recipient
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
	public AuthStatus validateRequest(final MessageInfo message, final Subject source, final Subject recipient)
    throws AuthException {

		final ServerAuthenticationContext context = new ServerAuthenticationContext(this.handler, message, source, this.option);
		return validateRequest(context.request(), context.response(), context);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateRequest
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
   ** @param  request            contains the request the client has made.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           contains the response that will be send to the
   **                            client.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  context            the context for interacting with the container.
   **                            <br>
   **                            Allowed object is
   **                            {@link ServerAuthenticationContext}.
   */
  @SuppressWarnings("unused")
  public AuthStatus validateRequest(final HttpServletRequest request, final HttpServletResponse response, final ServerAuthenticationContext context)
    throws AuthException {

    throw new IllegalStateException("Not implemented");
  }

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
   ** @param  scheme             the authorization scheme.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a token string representing an identity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String extract(final HttpServletRequest request, final String scheme) {
    // get the Authorization header from the request
    String authorization = header(request, "authorization");
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
  public static String header(final HttpServletRequest request, final String header) {
    return request.getHeader(header);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanSubject
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
   **                            Allowed object is
   **                            {@link ServerAuthenticationContext}.
   */
  @SuppressWarnings("unused")
  public void cleanSubject(final HttpServletRequest request, final HttpServletResponse response, final ServerAuthenticationContext context) {
    context.cleanSubject();
  }
}