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

    File        :   DeveloperReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DeveloperReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.gws.service.reconciliation.apigee;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.connector.service.EntityReconciliation;
import oracle.iam.identity.connector.service.DescriptorTransformer;

////////////////////////////////////////////////////////////////////////////////
// abstract class DeveloperReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
abstract class DeveloperReconciliation extends Service {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the object class for a organization entitlement */
  protected static final ObjectClass TENANT    = DescriptorTransformer.objectClass("TENANT");

  /** the object class for an developer acount */
  protected static final ObjectClass DEVELOPER = DescriptorTransformer.objectClass("DEVELOPER");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DeveloperReconciliation</code> job that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected DeveloperReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Service Provider.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected final void onExecution()
    throws TaskException {

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));
    // obtain the state of the job before any of the attributes are subject of
    // change
    final OperationOptions           options = operationOptions();
    final EntityReconciliation.Batch handler = new EntityReconciliation.Batch(this, this.threadPoolSize(), ignoreDublicates());
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
      this.connector.search(DEVELOPER, filter(), handler, options);
      // process the events which might be left open
      handler.finish();
    }
    catch (ConnectorException e) {
      throw operationalException(e);
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