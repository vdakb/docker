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

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Scheduler Operations

    File        :   RetryFailedProvisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RetryFailedProvisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcProvisioningOperationsIntf;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.scheduler.SchedulerBundle;
import oracle.iam.identity.scheduler.SchedulerMessage;

////////////////////////////////////////////////////////////////////////////////
// class RetryFailedProvisioning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>RetryFailedProvisioning</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 ** <p>
 ** The task retries all open tasks, that are failed and whose attribute
 ** <code>ProcessTask.Retry Count</code> has not reached the limit configured.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class RetryFailedProvisioning extends CustomScheduledTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /**
     ** the task attribute to specifiy if the object the open task belongs to.
     */
    TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,        TaskAttribute.MANDATORY)
  , TaskAttribute.build(BATCH_SIZE,       10)
  };

  private static final String          RESOURCE_NAME = "Objects.Name";
  private static final String          PROCESS_NAME  = "Process Definition.Tasks.Task Name";
  private static final String          PROCESS_TASK  = "Process Instance.Task Details.Key";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RetryFailedProvisioning</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RetryFailedProvisioning() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractSchedulerBaseTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(SchedulerBundle.format(SchedulerMessage.JOB_BEGIN, getName()));
    // remember the last execution time as the timestamp on which this job has
    // last reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    // Database Server current time is different from this system time
    lastReconciled(System.currentTimeMillis());

    final tcProvisioningOperationsIntf facade = processFacade();
    final Map<String, String>          filter = new HashMap<>();
    try {
      String resourceName  = null;
      String processName   = null;
      filter.put(RESOURCE_NAME, this.reconcileObject());
      final tcResultSet failed  = facade.getAssignedOpenProvisioningTasks(1L, filter, new String[]{"Rejected"});
      final int         size    = failed.getRowCount();
      if (size > 0) {
        final long task[] = new long[1];
        for (int i = 0; i < size; i ++) {
          failed.goToRow(i);
          resourceName = failed.getStringValue(RESOURCE_NAME);
          processName  = failed.getStringValue(PROCESS_NAME);
          task[0]      = failed.getLongValue(PROCESS_TASK);
          info("Retry task " + processName + " for " + resourceName + "...");
          try {
            facade.retryTask(task[0]);
            info("Task " + processName + " for " + resourceName + " retried.");
          }
          // capture any exception that prevents to retry the task for reproting
          // purpose only
          catch (Exception e) {
            fatal(method, e);
          }
          if (stopped())
            break;
        }
      }
      updateLastReconciled();
      info(SchedulerBundle.format(SchedulerMessage.JOB_COMPLETE, getName()));
    }
    // we don't need to classify specific exceptions due to any occurence is
    // toxic
    catch (Exception e) {
      info(SchedulerBundle.format(SchedulerMessage.JOB_ABORT, getName(), e.getMessage()));
      throw new TaskException(e);
    }
    finally {
      if (facade != null) {
        facade.close();
      }
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}