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
    Subsystem   :   GlassFish Server Security Realm

    File        :   IndentityAsserter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IndentityAsserter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.jaas;

import java.io.ByteArrayInputStream;

import java.util.Map;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

import javax.security.auth.Subject;

import javax.security.auth.callback.CallbackHandler;

import javax.security.auth.login.LoginException;

import oracle.iam.platform.saml.AssertionProcessor;

import oracle.iam.platform.saml.v2.schema.Assertion;

////////////////////////////////////////////////////////////////////////////////
// abstract class IndentityAsserter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>IndentityAsserter</code> describes the interface implemented by
 ** authentication technology providers.
 ** <p>
 ** <code>IndentityAsserter</code> are plugged in under applications to provide
 ** a particular type of authentication.
 ** <p>
 ** The <code>LoginContext</code> is responsible for reading the
 ** <code>Configuration</code> and instantiating the appropriate
 ** <code>Login</code> modules. Each <code>IndentityAsserter</code> is
 ** initialized with a {@code Subject}, a {@code CallbackHandler}, shared
 ** <code>IndentityAsserter</code> state, and <code>IndentityAsserter</code>
 ** module-specific options.
 ** <p>
 ** The {@code Subject} represents the {@code Subject} currently being
 ** authenticated and is updated with relevant Credentials if authentication
 ** succeeds. <code>IndentityAsserter</code> use the {@code CallbackHandler}
 ** to communicate with users. The {@code CallbackHandler} may be used to prompt
 ** for usernames and passwords, for example.
 ** <p>
 ** The <code>IndentityAsserter</code> module-specific options represent the
 ** options configured for this <code>IndentityAsserter</code> module by an
 ** administrator or user in the login <code>Configuration</code>.
 ** <br>
 ** The options are defined by the <code>IndentityAsserter</code> itself and
 ** control the behavior within it. Options are defined using
 ** a key-value syntax, such as <i>debug=true</i>. The
 ** <code>IndentityAsserter</code> stores the options in particular
 ** attributes.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** There is no limit to the number of options a
 ** <code>IndentityAsserter</code> module chooses to define but are ignored if
 ** the option key is unknown.
 ** <p>
 ** The calling application sees the authentication process as a single
 ** operation. However, the authentication process within the
 ** <code>IndentityAsserter</code> module proceeds in two distinct phases.
 ** <br>
 ** In the first phase, the <code>IndentityAsserter</code>'s <code>login</code>
 ** method gets invoked. The <code>login</code> method for the
 ** <code>IndentityAsserter</code> module then performs the actual
 ** authentication (prompt for and verify a password for example) and saves its
 ** authentication status as private state information. Once finished, the
 ** <code>IndentityAsserter</code>'s <code>login</code> method either returns
 ** <code>true</code> (if it succeeded) or <code>false</code> (if it should be
 ** ignored), or throws a {@link LoginException} to specify a failure.
 ** <br>
 ** In the failure case, the <code>√</code> does not retry the authentication or
 ** introduce delays. The responsibility of such tasks belongs to the
 ** application. If the application attempts to retry the authentication, the
 ** <code>√</code>'s <code>login</code> method will be called again.
 ** <p>
 ** In the second phase, if the LoginContext's overall authentication succeeded
 ** (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>LoginModule</code> succeeded), then the <code>commit</code> method for
 ** the <code>IndentityAsserter</code> invoked.
 ** <br>
 ** The <code>commit</code> method for a <code>IndentityAsserter</code> checks
 ** its privately saved state to see if its own authentication succeeded.
 ** <br>
 ** If the overall <code>LoginContext</code> authentication succeeded and the
 ** <code>IndentityAsserter</code>'s own authentication succeeded, then the
 ** <code>commit</code> method associates the relevant Principals
 ** (authenticated identities) and Credentials (authentication data such as
 ** cryptographic keys) with the {@code Subject} located within the
 ** <code>IndentityAsserter</code>.
 ** <p>
 ** If the <code>LoginContext</code>'s overall authentication failed (the
 ** relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>LoginModule</code> did not succeed), then the <code>abort</code>
 ** method for each <code>LoginModule</code> gets invoked. In this case, the
 ** <code>IndentityAsserter</code> removes/destroys any authentication state
 ** originally saved.
 ** <p>
 ** Logging out a {@code Subject} involves only one phase.
 ** <br>
 ** The <code>LoginContext</code> invokes the
 ** <code>IndentityAsserter</code>'s <code>logout</code> method. The
 ** <code>logout</code> method for the <code>IndentityAsserter</code> then
 ** performs the logout procedures, such as removing Principals or Credentials
 ** from the {@code Subject} or logging session information.
 ** <p>
 ** Subclasses need to implement the authenticateUser() method and later call
 ** commitUserAuthentication().
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class IndentityAsserter extends Asserter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String PARAMETER_SIGNER = "signer";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private PublicKey           signer           = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IndentityAsserter</code> login module that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public IndentityAsserter() {
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   login (LoginModule)
  /**
   ** Method to authenticate a {@link Subject} (phase 1).
   ** <p>
   ** The implementation of this method authenticates a {@link Subject}.
   ** <br>
   ** For example, it may prompt for {@link Subject} information such as a
   ** username and password and then attempt to verify the password. This method
   ** saves the result of the authentication attempt as private state within the
   ** {@link Asserter}.
   ** <br>
   ** This method is only called (once!) after initialize.
   **
   ** @return                    <code>true</code> if the authentication
   **                            succeeded, or <code>false</code> if this
   **                            {@link Asserter} should be ignored.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     if the authentication fails.
   */
  @Override
  public boolean login()
    throws LoginException {
    
    // the access token to validate, typically submitted with a HTTP header like
    // Authorization: Bearer eyJraWQiOiJDWHVwIiwidHlwIjoiYXQrand0IiwiYWxnIjoi...
    // Create a JWT processor for the access tokens
    if (assertIdentity()) {
      try {
        final Assertion assertion = AssertionProcessor.parse(this.signer, new ByteArrayInputStream(this.identity.getBytes()));
        this.identity = assertion.subject().nameID().value();
        authenticateUser();
      }
      catch (Exception e) {
        e.printStackTrace(System.err);
        throw new LoginException(e.getLocalizedMessage());
      }
    }
    return true;
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Initialize this {@link Asserter}.
   ** <p>
   ** This method is called (once!) by the <code>LoginContext</code> after this
   ** {@link Asserter} has been instantiated.
   ** <br>
   ** The purpose of this method is to initialize this {@link Asserter} with the
   ** relevant information.
   ** <br>
   ** If this {@link Asserter} does not understand any of the data stored in
   ** <code>shared</code> or <code>option</code> parameters, they can be
   ** ignored.
   **
   ** @param  subject            the {@link Subject} this login attempt will
   **                            populate.
   **                            <br>
   **                            Allowed object is {@link Subject}.
   ** @param  handler            a {@link CallbackHandler} that can be used to
   **                            get the username and in authentication mode,
   **                            the user's credential.
   **                            <br>
   **                            Allowed object is {@link CallbackHandler}.
   ** @param  shared             the state shared with between login modules
   **                            when there are multiple authenticators
   **                            configured.
   **                            <br>
   **                            The current module does not use this parameter.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   ** @param  option             the options specified in the login
   **                            {@code Configuration} for this particular
   **                            {@link Asserter}.
   **                            <br>
   **                            For example, it can be used to pass in
   **                            configuration data (where is the database
   **                            holding user and group info) and to pass in
   **                            whether the login module is used for
   **                            authentication or to complete identity
   **                            assertion.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and any as the value.
   */
  @Override
  public void initialize(final Subject subject, final CallbackHandler handler, final Map<String, ?> shared, final Map<String, ?> option) {
    try {
      final byte[] bytes = Files.readAllBytes(Paths.get(stringProperty(option, PARAMETER_SIGNER)));
      this.signer = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
    }
    catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace(System.err);
      throw new IllegalArgumentException(e);
    }
    // ensure inheritance
    super.initialize(subject, handler, shared, option);
  }
}