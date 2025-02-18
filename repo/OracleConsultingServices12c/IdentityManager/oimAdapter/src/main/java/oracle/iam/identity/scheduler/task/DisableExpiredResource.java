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

    File        :   DisableExpiredResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DisableExpiredResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.scheduler.SchedulerBundle;
import oracle.iam.identity.scheduler.SchedulerMessage;

////////////////////////////////////////////////////////////////////////////////
// class DisableExpiredResource
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DisableExpiredResource</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 ** <p>
 ** The task disables all users for a particular resource if the valid through
 ** date for that resource is reached.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DisableExpiredResource extends ExpiredResourceReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /** the task attribute to specifiy the resource object */
    TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute to specifiy the field of the process form */
  , TaskAttribute.build(RECONCILE_FIELD,  TaskAttribute.MANDATORY)
    /** the task attribute to specifiy the format of the field of the process form */
  , TaskAttribute.build(RECONCILE_FORMAT, TaskAttribute.MANDATORY)
    /** the task attribute to specifiy the matching rule of the field */
  , TaskAttribute.build(RECONCILE_MATCH,  TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** containing names of affected users held against user key */
  private List                         objectInstances = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DisableExpiredResource</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DisableExpiredResource() {
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
  @Override
  public void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(SchedulerBundle.format(SchedulerMessage.JOB_BEGIN, getName()));
    final String objectName = stringValue(RECONCILE_OBJECT);
    // resolve the resource and stop the process if the specified name is not
    // resolvable
    long[] objectKey = new long[1];
    objectKey[0] = findObject(objectName);
    if (objectKey[0] == -1L) {
      // stop the execution of the task, the following try / catch block will
      // immediatlly return if the task is stopped
      stop();
      // inform the user about the fact the we are not willing to perform
      final String reason = TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, TaskBundle.string(TaskMessage.ENTITY_RESOURCE), objectName);
      error(method, SchedulerBundle.format(SchedulerMessage.JOB_ABORT, getName(), reason));
    }

    try {
      // check if we should not go further
      if (isStopped())
        return;

      // get all users which have the resource object provisioned
      info(TaskBundle.string(TaskMessage.COLLECTING_BEGIN));
      this.objectInstances = findObjectInstance(objectKey[0]);
      info(TaskBundle.string(TaskMessage.COLLECTING_COMPLETE));
      if (this.objectInstances == null) {
        stop();
        String[] parameter = {
          TaskBundle.string(TaskMessage.ENTITY_RESOURCE)
        , objectName
        , TaskBundle.string(TaskMessage.ENTITY_IDENTITY)
        };
        info(SchedulerBundle.format(SchedulerMessage.NOTPROVISIONED, parameter));
      }

      // check if we should not go further
      if (isStopped())
        return;

      int size = this.objectInstances.size();
      if (size == 0)
        info(TaskBundle.string(TaskMessage.NOTHING_TO_CHANGE));
      else
        info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(size)));

      // validate the effort to do
      Iterator i = this.objectInstances.iterator();
      while (i.hasNext() && this.summary.success() < size) {
        // check if the current thread is able to execute or a stop signal is
        // pending
        if (isStopped())
          break;

        int remainingSize = size - this.summary.success();
        if (remainingSize > batchSize())
          remainingSize = batchSize();

        // compute the size of the batch
        long[] objectInstances = new long[remainingSize];
        long[] userKeys        = new long[remainingSize];
        for (int j = 0; (j < remainingSize && i.hasNext()); j++) {
          TaskData data      = (TaskData)i.next();
          objectInstances[j] = data.objectInstance;
          userKeys[j]        = data.userKey;
        }

        this.objectFacade.disableAppsForUsers(objectInstances, userKeys);
        incrementSuccess(remainingSize);
      }
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      if (this.objectInstances == null || this.objectInstances.size() == 0) {
        final String reason = SchedulerBundle.format(SchedulerMessage.NOTPROVISIONED, TaskBundle.string(TaskMessage.ENTITY_RESOURCE), objectName, TaskBundle.string(TaskMessage.ENTITY_IDENTITY));
        warning(SchedulerBundle.format(SchedulerMessage.JOB_ABORT, getName(), reason));
      }
      else
        info(SchedulerBundle.format(SchedulerMessage.JOB_COMPLETE, getName()));
    }
    catch (Exception e) {
      error(method, SchedulerBundle.format(SchedulerMessage.JOB_ABORT, getName(), e.getLocalizedMessage()));
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findObject
  /**
   ** Returns the internal identifier of a resource object for the specified
   ** name.
   **
   ** @param  objectName         the name of the resource to resolve.
   **
   ** @return                    the desired resource identifier for the
   **                            specified resource name.
   **                            If the resource name is not in the system
   **                            <code>-1L</code> will be returned.
   */
   private long findObject(String objectName) {

    final String method = "findObject";
    trace(method, SystemMessage.METHOD_ENTRY);

    long objectKey = -1L;
    try {
      Map filter = new HashMap(1);
      filter.put(ResourceObject.NAME, objectName);
      tcResultSet resultSet = this.objectFacade.findObjects((filter));

      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_RESOURCE), objectName};
      if (resultSet.getRowCount() == 1) {
        objectKey = resultSet.getLongValue(ResourceObject.KEY);
      }
      else if (resultSet.getRowCount() > 1) {
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      }
      else if (resultSet.getRowCount() == 0) {
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return objectKey;
   }
}