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

    File        :   IdentityAsserter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityAsserter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.oam;

import java.util.Set;
import java.util.Map;

import java.util.logging.Logger;

import java.io.IOException;
import java.io.FileInputStream;

import java.security.cert.X509Certificate;

import javax.security.enterprise.CallerPrincipal;

import javax.security.auth.Subject;

import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.AuthException;

import javax.security.auth.message.module.ServerAuthModule;

import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.callback.CallerPrincipalCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bka.iam.identity.AssertionStore;
import bka.iam.identity.AssertionCredential;

import bka.iam.identity.jmx.TokenAsserterConfiguration;
import bka.iam.identity.jmx.TokenAsserterConfigurationMBean;

import javax.servlet.http.HttpSession;

import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.jca.cert.PEM;

import oracle.iam.platform.saml.AssertionException;
import oracle.iam.platform.saml.AssertionProcessor;

import oracle.iam.platform.saml.v2.schema.Assertion;
import oracle.iam.platform.saml.v2.schema.Conditions;

////////////////////////////////////////////////////////////////////////////////
// class IdentityAsserter
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityAsserter</code> validates requests send by client and
 ** secures responses to the client by implementing JASPIC.
 ** <p>
 ** JASPIC stands for <i>Java Authentication Service Provider Interface</i> and
 ** seeks to define a standard interface by which authentication modules may be
 ** integrated with containers and such that these modules may establish the
 ** authentication identities used by containers.
 ** <p>
 ** Concretly it defines how an athenticated module is integrated into a Java EE
 ** container. It is part of the platform since Java EE 6 (2009).
 ** <p>
 ** The original JSR for JASPIC was created back in 2002 but it wasn't completed
 ** until 2007 and wasn't included in Java EE until Java EE 6 back in 2009.
 ** <p>
 ** JSR 196 defines a standard service-provider interface (SPI) and standardises
 ** how an authentication module is integrated into a Java EE container. It is
 ** supported by all the popular web containers and is mandatory for the full
 ** Java EE 6 profile. It provides a message processing model and details a
 ** number of interaction points on the client and server.
 ** <p>
 ** A compatible web container will use the SPI at these points to delegate the
 ** corresponding message security processing to a <b>S</b>erver
 ** <b>A</b>uthentication <b>M</b>odule (SAM).
 ** <p>
 ** The security-related specifications are:
 ** <ul>
 **   <li><a href="https://github.com/jefrajames/jaspiclab/blob/master">JAAS</a>
 **       <br>
 **       Java Authentication and Authorization Service which is part of Java SE
 **       since J2SDK 1.4 (2002). Among other things, JAAS defines the core
 **       concepts of Java Security: Subject (such as a person, an entity, a
 **       service), Principal (the id of subject: such as a name, a SSN) and
 **       Credential (security-related attributes of a subject).
 **   <li><a href="https://jcp.org/en/jsr/detail?id=115">JACC (JSR 115)</a>
 **       <br>
 **       Java Authorization Contract for Containers which seeks to define a
 **       contract between containers and authorization service providers that
 **       will result in the implementation of providers for use by containers.
 **   <li>more recently <a href="https://javaee.github.io/security-spec/">Java EE Security (JSR 375)</a>:
 **       <br>
 **       The goal of this JSR is to improve the Java EE platform by ensuring
 **       the Security API aspect is useful in the modern cloud/PaaS application
 **       paradigm.
 ** </ul>
 ** The implementation assumes that may be used to secure different requests as
 ** different clients. It's also assumed that it may be used concurrently by
 ** multiple callers. For that purpose the module save and restore any state
 ** properly as necessary.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityAsserter extends    AssertionStore<TokenAsserterConfiguration>
                              implements ServerAuthModule {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String THIS             = IdentityAsserter.class.getName();
  private static final Logger LOGGER           = Logger.getLogger(THIS);

  private static final String SUBJECT          = "igs.subject";
  private static final String PRINCIPAL        = "igs.principal";
  private static final String PERMISSION       = "igs.permission";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:9207382029950611269")
  private static final long   serialVersionUID = 565975100792679476L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
	protected CallbackHandler   handler;

	protected final Class<?>[]  supported        = new Class[]{
    HttpServletRequest.class
  , HttpServletResponse.class
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityAsserter</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityAsserter() {
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
   ** delegation class {@code ServerAuthContext} can choose the right SAM to
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
   ** <p>
   ** Any configuration parameter needs to be provided by the option mapping.
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
		config().extend(option);
    config().validate();
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
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** WebLogic 12c calls this before Servlet is called, Geronimo v3 after, JBoss
   ** EAP 6 and GlassFish 3.1.2.2 don't call this at all.
   ** <br>
   ** WebLogic (seemingly) only continues if SEND_SUCCESS is returned, Geronimo
   ** completely ignores return value.
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
   **                              <li><b>AuthStatus.SUCCESS</b>
   **                                  <br>
   **                                  when the application request message was
   **                                  successfully validated. The validated
   **                                  request message is available by calling
   **                                  getRequestMessage on
   **                                  <code>message</code>.
   **                              <li><b>AuthStatus.SEND_SUCCESS</b>
   **                                  <br>
   **                                  indicate that validation/processing of
   **                                  the request message successfully
   **                                  produced the secured application
   **                                  response message (in
   **                                  <code>message</code>). The secured
   **                                  response message is available by
   **                                  calling getResponseMessage on
   **                                  <code>message</code>.
   **                              <li><b>AuthStatus.SEND_CONTINUE</b>
   **                                  <br>
   **                                  indicate that message validation is
   **                                  incomplete, and that a preliminary
   **                                  response was returned as the response
   **                                  message in <code>message</code>. When
   **                                  this status value is returned to
   **                                  challenge an application request message,
   **                                  the challenged request must be saved by
   **                                  the authentication module such that it
   **                                  can be recovered when the module's
   **                                  validateRequest message is called to
   **                                  process the request returned for the
   **                                  challenge.
   **                              <li><b>AuthStatus.SEND_FAILURE</b>
   **                                  <br>
   **                                  indicate that message validation failed
   **                                  and that an appropriate failure response
   **                                  message is available by calling
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

    if (subject != null) {
      subject.getPrincipals().clear();
    }
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
   **
   ** @return                    an {@link AuthStatus} object representing the
   **                            completion status of the processing performed
   **                            by the method. The {@link AuthStatus} values
   **                            that may be returned by this method are defined
   **                            as follows:
   **                            <ul>
   **                              <li><b>AuthStatus.SUCCESS</b>
   **                                  <br>
   **                                  when the application request message was
   **                                  successfully validated. The validated
   **                                  request message is available by calling
   **                                  getRequestMessage on
   **                                  <code>message</code>.
   **                              <li><b>AuthStatus.SEND_SUCCESS</b>
   **                                  <br>
   **                                  indicate that validation/processing of
   **                                  the request message successfully
   **                                  produced the secured application
   **                                  response message (in
   **                                  <code>message</code>). The secured
   **                                  response message is available by
   **                                  calling getResponseMessage on
   **                                  <code>message</code>.
   **                              <li><b>AuthStatus.SEND_CONTINUE</b>
   **                                  <br>
   **                                  indicate that message validation is
   **                                  incomplete, and that a preliminary
   **                                  response was returned as the response
   **                                  message in <code>message</code>. When
   **                                  this status value is returned to
   **                                  challenge an application request message,
   **                                  the challenged request must be saved by
   **                                  the authentication module such that it
   **                                  can be recovered when the module's
   **                                  validateRequest message is called to
   **                                  process the request returned for the
   **                                  challenge.
   **                              <li><b>AuthStatus.SEND_FAILURE</b>
   **                                  <br>
   **                                  indicate that message validation failed
   **                                  and that an appropriate failure response
   **                                  message is available by calling
   **                                  getResponseMessage on
   **                                  <code>message</code>.
   **                            </ul>
   **
   ** @throws AuthException      when the message processing failed without
   **                            establishing a failure response message (in
   **                            <code>message</code>).
   */
	@Override
  @SuppressWarnings("unchecked")
  public AuthStatus validateRequest(final MessageInfo message, final Subject source, final Subject recipient)
    throws AuthException {

    final String method = "validateRequest";
    LOGGER.entering(THIS, method);
    final HttpServletRequest request = (HttpServletRequest)message.getRequestMessage();
    // extract the token credential form the request header
    final AssertionCredential credential = token(request);
    // if the credential does not provide a token the authentication request
    // needs to fail due to nothing to validate
    if (credential == null) {
      // we must set the HTTP status on the response ourself what happens
      // otherwise is not-so-standard and not easy to determine beforehand, as
      // validateRequest may be invoked up to 3 times for a single request
      final HttpServletResponse response = (HttpServletResponse)message.getResponseMessage();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      // it looks like at least at Payara the token is provided not at the first
      // time the service is called; the hack below tries to circumvent this
      // behavior
      LOGGER.exiting(THIS, method, AuthStatus.SEND_FAILURE);
      return AuthStatus.SEND_FAILURE;
    }

    HttpSession session = request.getSession(false);
    if (session != null) {
      final Subject         subject   = (Subject)session.getAttribute(SUBJECT);
      final CallerPrincipal principal = (CallerPrincipal)session.getAttribute(PRINCIPAL);
      if (subject != null && principal != null) {
        // caller authenticated before, re-apply authentication for this request
        final AuthStatus status = login(source, principal, (Set<String>)session.getAttribute(PERMISSION));
        LOGGER.exiting(THIS, method,  status);
        return status;
      }
    }
    LOGGER.fine("Looks like a new session needs to be born");
    try {
      // validate the token
      final CallerPrincipal principal = validate(credential);
      session = request.getSession();
      // save the Subject...
      session.setAttribute(SUBJECT,    source);
      // save the principal
      session.setAttribute(PRINCIPAL,  principal);
      final Set<String> roles = authorize(principal.getName());
      // save the permissions assigned
      session.setAttribute(PERMISSION, roles);
      LOGGER.fine(roles.toString());
      LOGGER.exiting(THIS, method, AuthStatus.SEND_SUCCESS);
      return AuthStatus.SEND_SUCCESS;
    }
    catch (bka.iam.identity.AssertionException e) {
      LOGGER.severe(e.getLocalizedMessage());
      LOGGER.exiting(THIS, method);
      throw (AuthException)new AuthException().initCause(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   config (AssertionStore)
  /**
   ** Retrieve the configuration singleton.
   **
   ** @return                    the configuration singleton.
   **                            <br>
   **                            Possible object is
   **                            {@link TokenAsserterConfiguration}.
   */
  @Override
  protected TokenAsserterConfiguration config() {
    if (this.config == null)
      this.config = new TokenAsserterConfiguration("igs-sam");

    return this.config;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate  (AssertionStore)
  /**
   ** Given a user-provided token credential, return an optional principal.
   ** <p>
   ** If the credentials are valid and map to a principal, returns a
   ** {@link CallerPrincipal}.
   **
   ** @param  credential         a user-provided token credential.
   **                            <br>
   **                            Allowed object is {@link AssertionCredential}.
   **
   ** @return                    an authenticated principal.
   **                            <br>
   **                            Possible object is {@link CallerPrincipal}.
   **
   ** @throws AssertionException if the credentials cannot be authenticated due
   **                            to an underlying error.
   */
  @Override
  protected CallerPrincipal validate(final AssertionCredential credential)
    throws bka.iam.identity.AssertionException {
    
    final String method = "validate";
    LOGGER.entering(THIS, method);

    final TokenAsserterConfiguration config = config();
    String principal = null;
    if ("plain".equals(config.getAssertionType())) {
      principal = credential.token;
    }
    else if ("assertion".equals(config.getAssertionType())) {
      try {
        X509Certificate certificate = null;
        if (config.contains(TokenAsserterConfigurationMBean.SIGNING_LOCATION)) {
          certificate = PEM.certificate(new FileInputStream(config.propertyString(TokenAsserterConfigurationMBean.SIGNING_LOCATION)));
        }
        else if (config.contains(TokenAsserterConfigurationMBean.SIGNING_MATERIAL)) {
          certificate = PEM.certificate(config.propertyString(TokenAsserterConfigurationMBean.SIGNING_MATERIAL));
        }

        final Assertion assertion = AssertionProcessor.parse(certificate.getPublicKey(), credential.token);
        AssertionProcessor.verify(assertion, CollectionUtility.set(Assertion.ISSUE_INSTANT, Conditions.AFTER, Conditions.BEFORE), 60L);
        principal = assertion.subject().nameID().spProvidedID();
      }
      catch (IOException e) {
        LOGGER.severe(e.getLocalizedMessage());
        LOGGER.exiting(THIS, method);
        throw bka.iam.identity.AssertionException.unhandled(e);
      }
      catch (AssertionException e) {
        LOGGER.severe(e.getLocalizedMessage());
        LOGGER.exiting(THIS, method);
        throw bka.iam.identity.AssertionException.abort(e);
      }
    }
    // verify if the user is authentic (aka registered in the identiity store)
    final CallerPrincipal cp = authenticate(principal) ? new CallerPrincipal(principal) : null;
    LOGGER.exiting(THIS, method, cp == null ? null : cp.getName());
    return cp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   token
  /**
   ** Factory method to create a {@link AssertionCredential} from the configured
   ** request header.
   **
   ** @param  request            the context of the incomming request.
   **                            <br>
   **                            Allowed object is
   **                            {@link HttpServletRequest}.
   **
   ** @return                    the{@link AssertionCredential} populated with
   **                            the value of the configured request header, or
   **                            <code>null</code> if the request header not
   **                            mapped.
   **                            <br>
   **                            Possible object is
   **                            {@link AssertionCredential}.
   */
  protected final AssertionCredential token(final HttpServletRequest request) {
    // get the Authorization header from the request
    final String token = request.getHeader(config().getAssertionHeader());
    // check if the Authorization header is valid
    // it must not be null and must be prefixed with "Bearer" plus a whitespace
    // the authentication scheme comparison must be case-insensitive
    return (token == null) ? null : new AssertionCredential(token.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login
  /**
   ** Execute the handlers passed to this module this will typically add the
   ** principal and the roles in an application server specific way to the JAAS
   ** Subject.
   ** @param  subject            a {@link Subject} that represents the source of
   **                            the service request. It is used by the method
   **                            implementation to store <code>Principal</code>s
   **                            and credentials validated in the request.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   ** @param  principal          the principal that will be distinguished as the
   **                            caller principal.
   **                            May be <code>null</code>. 
   **                            <br>
   **                            Allowed object is {@link CallerPrincipal}.
   ** @param  permission         a collection of strings, where each element
   **                            contains the name of a group that will be used
   **                            to create a corresponding group principal
   **                            within the Subject <code>subject</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    an {@link AuthStatus} object representing the
   **                            completion status of the processing performed
   **                            by the method. The {@link AuthStatus} values
   **                            that may be returned by this method are defined
   **                            as follows:
   **                            <ul>
   **                              <li><b>AuthStatus.SUCCESS</b>
   **                                  <br>
   **                                  when the application request message was
   **                                  successfully validated. The validated
   **                                  request message is available by calling
   **                                  getRequestMessage on
   **                                  <code>message</code>.
   **                            </ul>
   **
   ** @throws AuthException      when the message processing failed without
   **                            establishing a failure response message.
   */
  private AuthStatus login(final Subject subject, final CallerPrincipal principal, final Set<String> permission)
    throws AuthException {

    final String method = "login";
    LOGGER.entering(THIS, method, principal.getName());
    LOGGER.entering(THIS, method, permission.toArray());
    try {
      // the JASPIC protocol for "authenticated user"
      this.handler.handle(
        new Callback[] {
          // create a handler (kind of directive) to add the caller principal
          // (aka user principal basically user name, or user id)
          // this will be the name of the principal returned by e.g.
          // HttpServletRequest#getUserPrincipal
          new CallerPrincipalCallback(subject, principal)
          // create a handler to add the group (aka role)
          // this is what e.g. HttpServletRequest#isUserInRole and @RolesAllowed
          // test for
        , new GroupPrincipalCallback(subject, permission.toArray(new String[0]))
        }
      );
      LOGGER.entering(THIS, method, AuthStatus.SUCCESS);
      return AuthStatus.SUCCESS;
    }
    catch (IOException | UnsupportedCallbackException e) {
      throw (AuthException)new AuthException().initCause(e);
    }
  }
}