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

    File        :   RoleOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.type.Role;
import oracle.iam.identity.deployment.type.MemberShip;

import oracle.iam.identity.common.spi.RoleHandler;

////////////////////////////////////////////////////////////////////////////////
// class RoleOperation
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Basic task operations on roles in Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public class RoleOperation extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleOperation() {
    // ensure inheritance
    super();

    // initialize attribute instances
    this.handler = new RoleHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setCategory
  /**
   ** Called to inject the argument for parameter <code>type</code>.
   **
   ** @param  category           the category of the role in Identity Manager to
   **                            handle.
   */
  public void setCategory(final String category) {
    final RoleHandler handler = (RoleHandler)this.handler;
    handler.category(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for parameter
   ** <code>name</code>.
   **
   ** @param  name               the name of the role in Identity Manager to
   **                            handle.
   */
  public void setName(final String name) {
    final RoleHandler handler = (RoleHandler)this.handler;
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

    final RoleHandler handler = (RoleHandler)this.handler;
    switch (handler.operation()) {
      case create: handler.create(this);
        break;
      case delete: handler.delete(this);
        break;
      case modify: handler.modify(this);
        break;
      default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, handler.operation().id(), RoleManagerConstants.ROLE_ENTITY_NAME));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredMemberShip
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  membership         the composed membership action to perform.
   **
   ** @throws BuildException     if the specified {@link MemberShip} is already
   **                            part of the parameter mapping.
   */
  public void addConfiguredMemberShip(final MemberShip membership)
    throws BuildException {

    final RoleHandler handler = (RoleHandler)this.handler;
    handler.addMemberShip(membership.value(), membership.recipient());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredRole
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>profile</code>.
   **
   ** @param  role               the {@link Role} to add.
   **
   ** @throws BuildException     if the specified {@link Role} object is
   **                            already part of this operation.
   */
  public void addConfiguredRole(final Role role)
    throws BuildException {

    final RoleHandler handler = (RoleHandler)this.handler;
    handler.addInstance(role.instance());
  }
}