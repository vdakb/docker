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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   UserProfileOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserProfileOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.type.User;

import oracle.iam.identity.common.spi.UserHandler;

////////////////////////////////////////////////////////////////////////////////
// class UserProfileOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Basic task operations on user profiles in Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public class UserProfileOperation extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor

  /**
   ** Constructs a <code>UserProfileOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserProfileOperation() {
    // ensure inheritance
    super();

    // initialize attribute instances
    this.handler = new UserHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>userid</code>.
   **
   ** @param  name               the id of the user profile in Identity Manager.
   */
  public void setName(final String name) {
    final UserHandler handler = (UserHandler)this.handler;
    handler.name(name);
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

    final UserHandler handler = (UserHandler)this.handler;
    switch (handler.operation()) {
      case create: handler.create(this);
        break;
      case delete: handler.delete(this);
        break;
      case modify: handler.modify(this);
        break;
      case enable: handler.enable(this);
        break;
      case disable: handler.disable(this);
        break;
      default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, handler.operation().id(), UserManagerConstants.USER_ENTITY));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.handler.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addUserProfile

  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>profile</code>.
   **
   ** @param  profile            the {@link User} to add.
   **
   ** @throws BuildException     if the specified {@link User} object is
   **                            already part of this operation.
   */
  public void addUserProfile(final User profile)
    throws BuildException {

    final UserHandler handler = (UserHandler)this.handler;
    handler.addInstance(profile.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredUserProfile
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>profile</code>.
   **
   ** @param  profile            the {@link User} to add.
   **
   ** @throws BuildException     if the specified {@link User} object is
   **                            already part of this operation.
   */
  public void addConfiguredUser(final User profile)
    throws BuildException {

    final UserHandler handler = (UserHandler)this.handler;
    handler.addInstance(profile.instance());
  }
}