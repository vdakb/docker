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

    File        :   SchedulerOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SchedulerOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.scheduler.type.Job;

import oracle.iam.identity.common.spi.JobHandler;

////////////////////////////////////////////////////////////////////////////////
// class SchedulerOperation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Configures a Scheduled Job Instance in Identity Manager with the specified
 ** parameter.
 ** <p>
 ** Works with Oracle Identity Manager 11.1.1 and later
 */
public class SchedulerOperation extends AbstractOperation {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SchedulerOperation</code> task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SchedulerOperation() {
    // ensure inheritance
    super();

    // initialize attribute instances
    this.handler = new JobHandler(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the Scheduled Job in Identity
   **                            Manager.
   */
  public void setName(final String name) {
    final JobHandler handler = (JobHandler)this.handler;
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

    final JobHandler handler = (JobHandler)this.handler;
    switch (handler.operation()) {
      case create: handler.create(this);
        break;
      case delete: handler.delete(this);
        break;
      case modify: handler.configure(this);
        break;
      default: error(ServiceResourceBundle.format(ServiceError.OBJECT_OPERATION_INVALID, handler.operation().id(), "JobHandler"));
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
   ** @throws BuildException   in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    this.handler.validate();

    // ensure inheritance
    super.validate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addJob
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>job</code>.
   **
   ** @param  job                the {@link Job} to add.
   **
   ** @throws BuildException     if the specified {@link Job} object is
   **                            already part of this operation.
   */
  public void addJob(final Job job)
    throws BuildException {

    final JobHandler handler = (JobHandler)this.handler;
    handler.addInstance(job.instance());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredScheduledJob
  /**
   ** Call by the ANT deployment to inject the argument for nested parameter
   ** <code>job</code>.
   **
   ** @param  job                the {@link Job} to add.
   **
   ** @throws BuildException     if the specified {@link Job} object is already
   **                            part of this operation.
   */
  public void addConfiguredJob(final Job job)
    throws BuildException {

    final JobHandler handler = (JobHandler)this.handler;
    handler.addInstance(job.instance());
  }
}