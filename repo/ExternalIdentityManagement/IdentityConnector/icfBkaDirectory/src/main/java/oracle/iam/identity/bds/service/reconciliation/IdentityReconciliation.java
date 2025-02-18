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
    Subsystem   :   Generic Directory Connector

    File        :   IdentityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.bds.service.reconciliation;

import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class IdentityReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccountReconciliation</code> acts as the service end point
 ** for Identity Manager to reconcile identity information from a
 ** <code>Service Provider</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IdentityReconciliation extends EntryReconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined on this task to specify that the
   ** organization should be derived from the distinguished name of the
   ** account entry.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String MAINTAIN_HIERARCHY    = "Maintain Hierarchy";

  /**
   ** Attribute tag which may be defined on this task to specify which default
   ** organization should a onboarded user be assign if the loaded organization
   ** could not be resolved.
   ** <br>
   ** This attribute is optional.
   */
  protected static final String IDENTITY_ORGANIZATION = "Identity Organization";

  /**
   ** Attribute tag which may be defined on this task to specify which
   ** role name should assigned to a onboarded user if no value is loaded from
   ** the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_ROLE         = "Identity Role";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity type value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_TYPE         = "Identity Type";

  /**
   ** Attribute tag which must be defined on this task to specify which
   ** identity status value should assigned to a onboarded user if no value is
   ** loaded from the source system.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String IDENTITY_STATUS       = "Identity Status";

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
    /** the task attribute to specify the default role for on-boarded users */
  , TaskAttribute.build(IDENTITY_ROLE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the default type for on-boarded users */
  , TaskAttribute.build(IDENTITY_TYPE,         TaskAttribute.MANDATORY)
    /** the task attribute to specify the default status for on-boarded users */
  , TaskAttribute.build(IDENTITY_STATUS,       TaskAttribute.MANDATORY)
    /**
     ** the task attribute to specify that the organizational hierarchy should
     ** be resolved
     */
  , TaskAttribute.build(MAINTAIN_HIERARCHY,    TaskAttribute.MANDATORY)
    /** the task attribute to specify the default organization for on-boarded users */
  , TaskAttribute.build(IDENTITY_ORGANIZATION, TaskAttribute.MANDATORY)
    /**
     ** the task attribute that specifies the search scope of the query
     ** <br>
     ** Allowed values are OneLevel | SubTree | Object
     */
  , TaskAttribute.build(SEARCH_SCOPE,         TaskAttribute.MANDATORY)
    /** the task attribute that specifies the search base of the query */
  , TaskAttribute.build(SEARCH_BASE,          SystemConstant.EMPTY)
    /** the task attribute that specifies the filter applied to the query */
  , TaskAttribute.build(SEARCH_FILTER,        SystemConstant.EMPTY)
    /** the task attribute that specifies the sort order applied to the query */
  , TaskAttribute.build(SEARCH_ORDER,         SystemConstant.EMPTY)
    /**
     ** the task attribute that specifies that the filter should be also use the
     ** timestamp attributes in the search to decrease the result set size in
     ** operational mode
     */
  , TaskAttribute.build(INCREMENTAL,          SystemConstant.TRUE)
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
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerTask)
  /**
   ** Reconciles the changed entries in the Directory Service.
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