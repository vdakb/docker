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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic REST Service Integration

    File        :   IdentityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.service.reconciliation.scim;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> acts as the service end point for
 ** Identity Manager to reconcile identity information from a Service Provider.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityReconciliation extends Service {

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
   ** Constructs an empty <code>IdentityReconciliation</code> scheduler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityReconciliation() {
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
   ** task definition of Identity Manager.
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

    info(TaskBundle.format(TaskMessage.RECONCILIATION_BEGIN, reconcileObject(), getName(), resourceName()));
    info(TaskBundle.format(TaskMessage.RECONCILIATION_COMPLETE, reconcileObject(), getName(), resourceName()));
  }
}