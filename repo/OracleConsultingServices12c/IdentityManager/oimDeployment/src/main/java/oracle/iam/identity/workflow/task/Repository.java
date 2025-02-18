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

    File        :   Repository.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Repository.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.workflow.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.deployment.task.AbstractOperation;

import oracle.iam.identity.workflow.type.Workflow;

import oracle.iam.identity.common.spi.WorkflowHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class Repository
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Base class to operate on the Workflow Repository of Identity Manager.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** Normaly the class will not be public. Unfortunately ANT has a problem in
 ** introspection if the class is package protected hence it will be exposed as
 ** public.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Repository extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Repository</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Repository() {
    // ensure inheritance
    super();

    // initialize the service provider instance
    // initialize attribute instances
    this.handler = new WorkflowHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPartition
  /**
   ** Sets the name of the partition of the workflow.
   **
   ** @param  partition          the name of the partition of the workflow.
   */
  public final void setPartition(final String partition) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.partition(partition);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the name of the workflow.
   **
   ** @param  name               the name of the workflow.
   */
  public final void setName(final String name) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRevision
  /**
   ** Sets the version of the workflow.
   **
   ** @param  revision           the revision ID of the composite related to
   **                            Identity Manager and/or Oracle SOA Suite.
   */
  public final void setRevision(final String revision) {
    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.revision(revision);
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

    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    switch (handler.operation()) {
      case register: handler.register(this);
        break;
      case enable: handler.enable(this);
        break;
      case disable: handler.disable(this);
        break;
      case print: handler.print(this);
        break;
      default: final String[] arguments = { handler.operation().id(), WorkflowHandler.PREFIX };
        error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredWorkflow
  /**
   ** Call by the ANT deployment to inject the argument for nested element
   ** <code>workflow</code>.
   **
   ** @param  workflow           the {@link Workflow} definition to add.
   **
   ** @throws ServiceException   if the specified {@link Workflow} object is
   **                            already part of this operation.
   */
  public void addConfiguredWorkflow(final Workflow workflow)
    throws ServiceException {

    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.addInstance(workflow.instance());
  }

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
  // Method:   register
  /**
   ** Registers a new workflow in Identity Manager.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void register()
    throws ServiceException {

    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.register(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enable
  /**
   ** Enables an existing workflow in Identity Manager.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void enable()
    throws ServiceException {

    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.enable(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disable
  /**
   ** Disables an existing workflow in Identity Manager.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  public void disable()
    throws ServiceException {

    final WorkflowHandler handler = (WorkflowHandler)this.handler;
    handler.disable(this);
  }
}