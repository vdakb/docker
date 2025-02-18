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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ServerSecurity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServerSecurity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.wls.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceException;

import oracle.hst.deployment.task.AbstractManagedBean;

import oracle.hst.deployment.spi.ServerSecurityHandler;

import oracle.hst.deployment.wls.type.User;
import oracle.hst.deployment.wls.type.Role;

////////////////////////////////////////////////////////////////////////////////
// class ServerSecurity
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Invokes the Security Configuration JMX Bean to manage users and roles in a
 ** Oracle WebLogic Server Domain.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServerSecurity extends AbstractManagedBean {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ServerSecurityHandler handler;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerSecurity</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerSecurity() {
    // ensure inheritance
    super();

    // initialize instance
    this.handler = new ServerSecurityHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractTask)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws ServiceException   if something goes wrong with the build
   */
  @Override
  public void onExecution()
    throws ServiceException {

    this.handler.execute(this.connection());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor Methods
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setRelam
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>realm</code>.
   **
   ** @param  realm              the name of the realm to operate on
   */
  public void setRelam(final String realm) {
    this.handler.realm(realm);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   setAuthenticator
  /**
   ** Call by the ANT kernel to inject the argument for parameter
   ** <code>authenticator</code>.
   **
   ** @param  authenticator      the name of the realm to operate on
   */
  public void setAuthenticator(final String authenticator) {
    this.handler.authenticator(authenticator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  @Override
  protected void validate() {
    this.handler.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredUser
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>user</code>.
   **
   ** @param  user               the {@link User} to manage in any operation.
   **
   ** @throws BuildException     if a {@link User} with the same key is
   **                            already assigned to the task.
   */
  public void addConfiguredUser(final User user)
    throws BuildException {

    this.handler.add(user.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRole
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>role</code>.
   **
   ** @param  role               the {@link Role} to manage in any operation.
   **
   ** @throws BuildException     if a {@link Role} with the same key is
   **                            already assigned to the task.
   */
  public void addConfiguredRole(final Role role)
    throws BuildException {

    this.handler.add(role.instance());
  }
}