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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Workflow Facility

    File        :   SecurityPrincipal.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SecurityPrincipal.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.context;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import java.util.Calendar;

import oracle.security.jps.JpsException;
import oracle.security.jps.JpsContext;
import oracle.security.jps.JpsContextFactory;

import oracle.security.jps.service.credstore.Credential;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.PasswordCredential;
import oracle.security.jps.service.credstore.CredStoreException;
import oracle.security.jps.service.credstore.CredentialExpiredException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class SecurityPrincipal
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>SecurityPrincipal</code> defines the value holder that can be passed
 ** to all instances of {@link ServiceProvider} to establish a connection
 ** context.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SecurityPrincipal implements PasswordCredential {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String MAP              = "oracle.oim.sysadminMap";
  public static final String KEY              = "sysadmin";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7886848285257888348")
  private static final long  serialVersionUID = -8184232415049766398L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the identity of the User (that is, a <code>User</code> defined in a J2EE
   ** Server security realm) for authentication purposes.
   */
  private final String username;
  private final String password;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> event handler that allows use
   ** as a JavaBean.
   ** <p>
   ** @param  username           the name of the administrative user.
   ** @param  password           the password of the administrative user.
   */
  public SecurityPrincipal(final String username, final char[] password) {
    // ensure inheritance
    this(username, new String(password));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SecurityPrincipal</code> event handler that allows use
   ** as a JavaBean.
   ** <p>
   ** @param  username           the name of the administrative user.
   ** @param  password           the password of the administrative user.
   */
  public SecurityPrincipal(final String username, final String password) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.username = username;
    this.password = password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns either the password for the <code>User</code> defined or an object
   ** that implements the vendor specific ACL interface.
   **
   ** @return                    the password for the <code>User</code> defined
   **                            or an object that implements the vendor
   **                            specific ACL interface.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription (Credential)
  /**
   ** Sets the description for this credential.
   **
   ** @param  description        the description for this credential to set.
   */
  @Override
  public final void setDescription(final String description) {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription (Credential)
  /**
   ** Returns the description for this credential.
   **
   ** @return                    the description for this credential.
   */
  @Override
  public final String getDescription() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (PasswordCredential)
  /**
   ** Returns the identity of the user (that is, a <code>User</code> defined in
   ** a J2EE Server security realm) for authentication purposes.
   **
   ** @return                    the identity of the user to use.
   */
  @Override
  public final String getName() {
    return this.username;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName (PasswordCredential)
  /**
   ** Returns the identity of the user (that is, a <code>User</code> defined in
   ** a J2EE Server security realm) for authentication purposes.
   **
   ** @return                    the identity of the user to use.
   */
  @Override
  public final char[] getPassword() {
    return this.password.toCharArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExpiryTime (PasswordCredential)
  /**
   ** Sets the expiration time for this credential.
   **
   ** @param  expiryTime         the expiry time of this credential to set.
   **                            Must be non-<code>null</code> and after current
   **                            time.
   */
  @Override
  public final void setExpiryTime(final Calendar expiryTime) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExpiryTime (PasswordCredential)
  /**
   ** Returns the expiration time if credential is created with expiration time.
   ** If credential is created without expiration time, this method returns
   ** <code>null</code>.
   */
  @Override
  public final Calendar getExpiryTime() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credential
  /**
   ** Returns the {@link SecurityPrincipal} objects associated with the default
   ** mapName and key. Returns null if this Credential is not found.
   ** <p>
   ** This is equivalent to getCredentialMap(mapName).getCredential(key). You
   ** need read CredentialAccessPermission permission to execute this API.
   **
   ** @return                    the {@link SecurityPrincipal} objects
   **                            associated with the default mapName and key.
   **                            <br>
   **                            May be <code>null</code> if the
   **                            {@link SecurityPrincipal} is not found.
   **
   ** @throws TaskException      if access to the credentiel store fails.
   */
  public static SecurityPrincipal credential()
    throws TaskException {

    // get system credentials from credential security framework
    SecurityPrincipal principal = null;
    try {
      final JpsContext      context    = JpsContextFactory.getContextFactory().getContext();
      final CredentialStore store      = context.getServiceInstance(CredentialStore.class);
      final Credential      credential = AccessController.doPrivileged(
        new PrivilegedExceptionAction<Credential>() {
          public Credential run() throws JpsException {
            return store.getCredential(MAP, KEY);
          }
        }
      );
      if (credential instanceof PasswordCredential) {
        final PasswordCredential password = (PasswordCredential)credential;
        principal = new SecurityPrincipal(password.getName(), password.getPassword());
      }
    }
    catch (CredentialExpiredException e) {
      throw new TaskException(e);
    }
    catch (CredStoreException e) {
      throw new TaskException(e);
    }
    catch (JpsException e) {
      throw new TaskException(e);
    }
    catch (PrivilegedActionException e) {
      throw new TaskException(e);
    }
    return principal;
  }
}