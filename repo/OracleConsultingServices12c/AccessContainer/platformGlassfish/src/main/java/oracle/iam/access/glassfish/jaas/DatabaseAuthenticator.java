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

    File        :   DatabaseAuthenticator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseAuthenticator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2015-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.jaas;

import javax.security.auth.login.LoginException;

import com.sun.enterprise.security.BasePasswordLoginModule;

import oracle.iam.access.glassfish.realm.Database;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseAuthenticator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DatabaseAuthenticator</code> describes the interface implemented by
 ** authentication technology providers.
 ** <p>
 ** <code>DatabaseAuthenticator</code> are plugged in under applications to
 ** provide a particular type of authentication.
 ** <p>
 ** The <code>LoginContext</code> is responsible for reading the
 ** <code>Configuration</code> and instantiating the appropriate
 ** <code>Login</code> modules. Each <code>DatabaseAuthenticator</code> is
 ** initialized with a {@code Subject}, a {@code CallbackHandler}, shared
 ** <code>DatabaseAuthenticator</code> state, and
 ** <code>DatabaseAuthenticator</code> module-specific options.
 ** <p>
 ** The {@code Subject} represents the {@code Subject} currently being
 ** authenticated and is updated with relevant Credentials if authentication
 ** succeeds. <code>DatabaseAuthenticator</code> use the {@code CallbackHandler}
 ** to communicate with users. The {@code CallbackHandler} may be used to prompt
 ** for usernames and passwords, for example.
 ** <p>
 ** The <code>DatabaseAuthenticator</code> module-specific options represent the
 ** options configured for this <code>DatabaseAuthenticator</code> module by an
 ** administrator or user in the login <code>Configuration</code>.
 ** <br>
 ** The options are defined by the <code>DatabaseAuthenticator</code> itself and
 ** control the behavior within it. Options are defined using
 ** a key-value syntax, such as <i>debug=true</i>. The
 ** <code>DatabaseAuthenticator</code> stores the options in particular
 ** attributes.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** There is no limit to the number of options a
 ** <code>DatabaseAuthenticator</code> module chooses to define but are ignored
 ** if the option key is unknown.
 ** <p>
 ** The calling application sees the authentication process as a single
 ** operation. However, the authentication process within the
 ** <code>DatabaseAuthenticator</code> module proceeds in two distinct phases.
 ** <br>
 ** In the first phase, the <code>DatabaseAuthenticator</code>'s
 ** <code>login</code> method gets invoked. The <code>login</code> method for
 ** the <code>DatabaseAuthenticator</code> module then performs the actual
 ** authentication (prompt for and verify a password for example) and saves its
 ** authentication status as private state information. Once finished, the
 ** <code>DatabaseAuthenticator</code>'s <code>login</code> method either
 ** returns <code>true</code> (if it succeeded) or <code>false</code> (if it
 ** should be ignored), or throws a {@link LoginException} to specify a failure.
 ** <br>
 ** In the failure case, the <code>DatabaseAuthenticator</code> does not retry
 ** the authentication or introduce delays. The responsibility of such tasks
 ** belongs to the application. If the application attempts to retry the
 ** authentication, the <code>DatabaseAuthenticator</code>'s <code>login</code>
 ** method will be called again.
 ** <p>
 ** In the second phase, if the LoginContext's overall authentication succeeded
 ** (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>LoginModule</code> succeeded), then the <code>commit</code> method for
 ** the <code>DatabaseAuthenticator</code> invoked.
 ** <br>
 ** The <code>commit</code> method for a <code>DatabaseAuthenticator</code>
 ** checks its privately saved state to see if its own authentication succeeded.
 ** <br>
 ** If the overall <code>LoginContext</code> authentication succeeded and the
 ** <code>DatabaseAuthenticator</code>'s own authentication succeeded, then the
 ** <code>commit</code> method associates the relevant Principals
 ** (authenticated identities) and Credentials (authentication data such as
 ** cryptographic keys) with the {@code Subject} located within the
 ** <code>DatabaseAuthenticator</code>.
 ** <p>
 ** If the <code>LoginContext</code>'s overall authentication failed (the
 ** relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
 ** <code>LoginModule</code> did not succeed), then the <code>abort</code>
 ** method for each <code>LoginModule</code> gets invoked. In this case, the
 ** <code>DatabaseAuthenticator</code> removes/destroys any authentication state
 ** originally saved.
 ** <p>
 ** Logging out a {@code Subject} involves only one phase.
 ** <br>
 ** The <code>LoginContext</code> invokes the
 ** <code>DatabaseAuthenticator</code>'s <code>logout</code> method. The
 ** <code>logout</code> method for the <code>DatabaseAuthenticator</code> then
 ** performs the logout procedures, such as removing Principals or Credentials
 ** from the {@code Subject} or logging session information.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseAuthenticator extends BasePasswordLoginModule {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a {@link BasePasswordLoginModule} that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** This is followed by a call to the init() method.
   */
  public DatabaseAuthenticator() {
    // ensure inheritance
    super();
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   authenticateUser (BasePasswordLoginModule)
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
    if (!(this._currentRealm instanceof Database)) {
      throw new LoginException(sm.getString("jdbclm.badrealm"));
    }
    commitUserAuthentication(assertCredential());
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertCredential
  /**
   ** Asserts the user identity based on the private credentials injected
   ** either by HTTP basic or form authentication mechanism.
   **
   ** @return                    <code>true</code> if the credentials provided
   **                            could be matched.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws LoginException     on authentication failure.
   */
  protected String[] assertCredential()
    throws LoginException {

    final Database database = (Database)this._currentRealm;
    // a JDBC user must have a name not null and non-empty.
    if ( (this._username == null) || (this._username.length() == 0) ) {
      throw new LoginException(sm.getString("jdbclm.nulluser"));
    }
    return database.authenticateUser(this._username, bytes(getPasswordChar()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytes
  /**
   ** Converts a array of <code>char</code>s to an aquivalent array of
   ** <code>byte</code>s.
   **
   ** @param  source             the arry of <code>char</code>s to convert.
   **                            <br>
   **                            Allowed object is array of <code>char</code>.
   **
   ** @return                    the converted <code>source</code> as an array
   **                            of <code>byte</code>s.
   **                            <br>
   **                            Possible object is array of <code>byte</code>.
   */
  private static byte[] bytes(final char[] source) {
    final byte[] buffer = new byte[source.length << 1];
    for (int i = 0; i < buffer.length; i++) {
      int  pos = i << 1;
      buffer[pos]     = (byte)((buffer[i]&0xff00) >> 8);
      buffer[pos + 1] = (byte)(buffer[i]&0x00ff);
    }
    return buffer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chars
  /**
   ** Converts a array of <code>byte</code>s to an aquivalent array of
   ** <code>char</code>s.
   **
   ** @param  source             the arry of <code>byte</code>s to convert.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   **
   ** @return                    the converted <code>source</code> as an array
   **                            of <code>char</code>s.
   **                            <br>
   **                            Possible object is array of <code>char</code>.
   */
  private static char[] chars(final byte[] source) {
    final char[] buffer = new char[source.length >> 1];
    for (int i = 0; i < buffer.length; i++) {
      final int  pos = i << 1;
      buffer[i] = (char)(((source[pos] & 0x00ff) << 8) + (source[pos + 1] & 0x00ff));
    }
    return buffer;
  }
}