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

    File        :   AccessPolicyOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.type.AccessPolicy;

import oracle.iam.identity.common.spi.AccessPolicyHandler;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Basic task operations on Access Policies in Identity Manager.
 ** <p>
 ** Works with Oracle Identity Manager 12.2.1 and later
 */
public class AccessPolicyOperation extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>LookupOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicyOperation() {
    // ensure inheritance
    super();

    // initialize attribute instances
    this.handler = new AccessPolicyHandler(this);
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

    final AccessPolicyHandler handler = (AccessPolicyHandler)this.handler;
    switch (handler.operation()) {
      case create: handler.create(this);
        break;
      case modify: handler.modify(this);
        break;
      case delete: handler.delete(this);
        break;
      default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, handler.operation().id(), "AccessPolicy"));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPolicy
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>policy</code>.
   **
   ** @param  policy             the {@link AccessPolicy} to add.
   **
   ** @throws BuildException     if the specified {@link AccessPolicy} object
   **                            is already part of this operation.
   */
  public void addConfiguredPolicy(final AccessPolicy policy)
    throws BuildException {

    final AccessPolicyHandler handler = (AccessPolicyHandler)this.handler;
    handler.addInstance(policy.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.handler.validate();

    // ensure inheritance
    super.validate();
  }
}