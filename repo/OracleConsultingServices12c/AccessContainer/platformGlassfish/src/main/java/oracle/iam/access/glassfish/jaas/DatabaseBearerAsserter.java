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

    File        :   DatabaseBearerAsserter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBearerAsserter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.jaas;

import javax.security.auth.login.LoginException;

import oracle.iam.access.glassfish.realm.Database;
import oracle.iam.access.glassfish.realm.RealmError;
import oracle.iam.access.glassfish.realm.RealmBundle;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseBearerAsserter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DatabaseBearerAsserter</code> describes the interface implemented by
 ** authentication technology providers.
 ** <p>
 ** <code>DatabaseBearerAsserter</code> are plugged in under applications to
 ** provide a particular type of authentication.
 ** <p>
 ** The <code>LoginContext</code> is responsible for reading the
 ** <code>Configuration</code> and instantiating the appropriate
 ** <code>Login</code> modules. Each <code>DatabaseBearerAsserter</code> is
 ** initialized with a {@code Subject}, a {@code CallbackHandler}, shared
 ** <code>DatabaseBearerAsserter</code> state, and
 ** <code>DatabaseBearerAsserter</code> module-specific options.
 ** <p>
 ** The {@code Subject} represents the {@code Subject} currently being
 ** authenticated and is updated with relevant Credentials if authentication
 ** succeeds. <code>DatabaseBearerAsserter</code> use the
 ** {@code CallbackHandler} to communicate with users. The
 ** {@code CallbackHandler} may be used to prompt for usernames and passwords,
 ** for example.
 ** <p>
 ** The <code>DatabaseBearerAsserter</code> module-specific options represent
 ** the options configured for this <code>DatabaseBearerAsserter</code> module
 ** by an administrator or user in the login <code>Configuration</code>.
 ** <br>
 ** The options are defined by the <code>DatabaseBearerAsserter</code> itself
 ** and control the behavior within it. Options are defined using a key-value
 ** syntax, such as <i>debug=true</i>. The <code>DatabaseBearerAsserter</code>
 ** stores the options in particular attributes.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** There is no limit to the number of options a
 ** <code>DatabaseBearerAsserter</code> module chooses to define but are ignored
 ** if the option key is unknown.
 ** <p>
 ** The calling application sees the authentication process as a single
 ** operation. However, the authentication process within the
 ** <code>DatabaseBearerAsserter</code> module proceeds in two distinct phases.
 ** <br>
 ** In the first phase, the <code>DatabaseBearerAsserter</code>'s
 ** <code>login</code> method gets invoked. The <code>login</code> method for
 ** the <code>DatabaseBearerAsserter</code> module then performs the actual
 ** authentication (prompt for and verify a password for example) and saves its
 ** authentication status as private state information. Once finished, the
 ** <code>DatabaseBearerAsserter</code>'s <code>login</code> method either
 ** returns <code>true</code> (if it succeeded) or <code>false</code> (if it
 ** should be ignored), or throws a {@link LoginException} to specify a failure.
 ** <br>
 ** In the failure case, the <code>DatabaseBearerAsserter</code> does not retry
 ** the authentication or introduce delays. The responsibility of such tasks
 ** belongs to the application. If the application attempts to retry the
 ** authentication, the <code>DatabaseBearerAsserter</code>'s <code>login</code>
 ** method will be called again.
 ** <p>
 ** In the second phase, if the LoginContext's overall authentication succeeded
 ** (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>DatabaseAsserter</code> succeeded), then the <code>commit</code>
 ** method for the <code>DatabaseBearerAsserter</code> invoked.
 ** <br>
 ** The <code>commit</code> method for a <code>DatabaseBearerAsserter</code>
 ** checks its privately saved state to see if its own authentication succeeded.
 ** <br>
 ** If the overall <code>LoginContext</code> authentication succeeded and the
 ** <code>DatabaseBearerAsserter</code>'s own authentication succeeded, then the
 ** <code>commit</code> method associates the relevant Principals
 ** (authenticated identities) and Credentials (authentication data such as
 ** cryptographic keys) with the {@code Subject} located within the
 ** <code>DatabaseBearerAsserter</code>.
 ** <p>
 ** If the <code>LoginContext</code>'s overall authentication failed (the
 ** relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>DirectoryAsserter</code> did not succeed), then the <code>abort</code>
 ** method for each <code>DirectoryAsserter</code> gets invoked. In this case,
 ** the <code>DatabaseBearerAsserter</code> removes/destroys any authentication
 ** state originally saved.
 ** <p>
 ** Logging out a {@code Subject} involves only one phase.
 ** <br>
 ** The <code>LoginContext</code> invokes the
 ** <code>DatabaseBearerAsserter</code>'s <code>logout</code> method. The
 ** <code>logout</code> method for the <code>DatabaseBearerAsserter</code> then
 ** performs the logout procedures, such as removing Principals or Credentials
 ** from the {@code Subject} or logging session information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseBearerAsserter extends BearerAsserter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DatabaseBearerAsserter</code> login module that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public DatabaseBearerAsserter() {
    // ensure inheritance
    super();
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateUser (Asserter)
  /**
   ** Perform authentication decision.
   ** <p>
   ** Method returns silently on success and returns a {@link LoginException}
   ** on failure.
   **
   ** @throws LoginException     on authentication failure.
   */
  @Override
  public void authenticateUser()
    throws LoginException {

    // prevent bogus state
    if (!(this.currentRealm instanceof Database)) {
      throw new LoginException(RealmBundle.string(RealmError.REALM_BADTYPE));
    }
    commitUserAuthentication(assertPrincipal());
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertPrincipal
  /**
   ** Asserts the user principal based on the private credentials injected
   ** either by HTTP basic or form authentication mechanism.
   **
   ** @return                    <code>true</code> if the credentials provided
   **                            could be matched.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     on authentication failure.
   */
  protected String[] assertPrincipal()
    throws LoginException {

    return ((Database)this.currentRealm).assertUser(this.identity);
  }
}