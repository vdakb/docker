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

    System      :   Oracle Identity Manager Plugin Shared Library
    Subsystem   :   Common Shared Plugin

    File        :   AccessPolicyProvisioningAction.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyProvisioningAction.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.spi;

import java.util.ArrayList;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeValues;

import oracle.iam.accesspolicy.impl.util.AccessPolicyUtil;

import oracle.iam.accesspolicy.exception.UserPolicyEvalLifecycleMgmtException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyProvisioningAction
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This custom post process action handler to provision accounts based on
 ** <code>Access Policies</code>.
 ** <p>
 ** The specific implementation instantiate a custom evalutator which takes
 ** care about primary accounts only.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyProvisioningAction extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String EVENT_NAME = "AccessPolicyProvisioningAction";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>AccessPolicyProvisioningAction</code> event handler
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicyProvisioningAction() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** The implementation of this post process event handler in one-off
   ** orchestration.
   ** <p>
   ** All User prepopulate events are handled in this method
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @return                    a {@link EventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public EventResult execute(long processId, long eventId, Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String      userKey     = orchestration.getTarget().getEntityId();
    final EventResult eventResult = new EventResult();
    try {
      // Check if policy evaluation needs to be triggered
      if (isPolicyEvaluationNeeded(AccessPolicyUtil.getUserDetails(userKey))) {
        final ArrayList<Orchestration> consequences = AccessPolicyUtil.evaluatePoliciesForUser(userKey, false);
        // flag processInSequence = true, ie child orchs runs in sequence in sync mode, no JMS involved
        eventResult.setImmediateChanges(consequences, true); 
      }
    }
    catch (Exception e) {
      error("IAM-4030255", String.valueOf(userKey));
      error("IAM-4030000", AccessPolicyUtil.getPrintableStackTrace(e));
      throw AccessPolicyUtil.createEventFailedException("IAM-4030255", null, EVENT_NAME, e, new Object[]{userKey});
    }

    trace(method, SystemMessage.METHOD_EXIT);
    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    return eventResult;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** The implementation of this post process event handler for bulk
   ** orchestration.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestration parameters, operation.
   **
   ** @return                    a {@link BulkEventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public BulkEventResult execute(final long processId, final long eventId, final BulkOrchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    trace(method, SystemMessage.METHOD_EXIT);
    // even if you don't implement a bulk handler you generally want to return
    // the BulkEventResult class otherwise bulk orchestrations will error out
    // and orphan
    return new BulkEventResult();
  }

  /**
   *
   * @param userKey
   * @param orchestration
   * @return
   * @throws UserPolicyEvalLifecycleMgmtException
   */
  private boolean isPolicyEvaluationNeeded(final User user)
    throws UserPolicyEvalLifecycleMgmtException {

    final String method = "isPolicyEvaluationNeeded";
    trace(method, SystemMessage.METHOD_ENTRY);
    String userStatus = user.getStatus();
    // Bug 22458505 - "EVALUATE USER POLCIES" JOB SETTING POLICY_EVAL_NEEDED='0' FOR DEFFERED USER
    // "Disabled" and "Disabled Until Start Date" are treated as Active.
    boolean activeUser = (!StringUtility.isEmpty(userStatus) && !userStatus.equalsIgnoreCase(AttributeValues.USER_STATUS_DELETED.getId()) && !userStatus.equalsIgnoreCase("Rejected"));
    trace(method, SystemMessage.METHOD_EXIT);
    return activeUser;
  }
}