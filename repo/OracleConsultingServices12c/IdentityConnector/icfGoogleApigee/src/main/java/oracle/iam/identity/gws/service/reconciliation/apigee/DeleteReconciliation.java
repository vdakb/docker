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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   DeleteReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DeleteReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.gws.service.reconciliation.apigee;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.connector.service.EntityReconciliation;

////////////////////////////////////////////////////////////////////////////////
// class DeleteReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DeleteReconciliation</code> acts as the service end point for
 ** Identity Manager to reconcile deleted account information from a
 ** <code>Google Apigee Edge</code> Service Provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DeleteReconciliation extends Service {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,          TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT,     TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation descriptor */
  , TaskAttribute.build(RECONCILE_DESCRIPTOR, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,            TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DeleteReconciliation</code> jod that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DeleteReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the scheduled
   ** task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link TaskAttribute}.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Service Provider.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN,    reconcileObject(), getName(), resourceName()));
    // obtain the state of the job before any of the attributes are subject of
    // change
    final OperationOptions            options = operationOptions();
    final EntityReconciliation.Delete handler = new EntityReconciliation.Delete(this, this.threadPoolSize());
    // set the current date as the timestamp on which this task has been last
    // reconciled at the start of execution
    // setting it here to have it the next time this scheduled task will
    // run the changes made during the execution of this task
    // updating this attribute will not perform to write it back to the
    // scheduled job attributes it's still in memory; updateLastReconciled()
    // will persist the change that we do here only if the job completes
    // successful
    lastReconciled(systemTime());
    try {
      this.connector.search(ObjectClass.ACCOUNT, filter(), handler, options);
      // process the events which might be left open
      handler.finish();
    }
    finally {
      // inform the observing user about the overall result of this task
      if (isStopped()) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), "Veto"};
        warning(TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // if an exception occured
      else if (getResult() != null) {
        final String[] arguments = { reconcileObject(), getName(), resourceName(), getResult().getLocalizedMessage()};
        error("onExecution", TaskBundle.format(TaskMessage.RECONCILIATION_STOPPED, arguments));
      }
      // complete with success and write back timestamp
      else {
        // update the timestamp on the task
        updateLastReconciled();
        info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
      }
    }
  }
}