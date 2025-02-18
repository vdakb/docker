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

    File        :   PasswordGenerator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    PasswordGenerator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.spi;

import java.util.HashMap;

import oracle.iam.platform.Platform;

import oracle.iam.platform.entitymgr.EntityManager;

import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.OrchestrationTarget;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.passwordmgmt.internal.api.RandomPasswordGenerator;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class PasswordGenerator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>PasswordGenerator</code> act as the service end point for the
 ** Oracle Identity Manager to generate a rendom password.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class PasswordGenerator extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.USR.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PasswordGenerator</code> event handler that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PasswordGenerator() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** This function is called on one-off orchestration operations.
   ** <p>
   ** In this implementation we will generate a global unique id.
   ** <p>
   ** This handler will work for CREATE operations only.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventID            the identifier of the orchestration event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestration parameters, operation.
   **
   ** @return                    a {@link EventResult} indicating the
   **                            operation has proceed.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public EventResult execute(final long processId, final long eventID, final Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final OrchestrationTarget target   = orchestration.getTarget();
    final String              entityID = target.getEntityId();
    // Identity Manager R2 PS3 had a totally different behavior in generating
    // passwords hence we need to adopt those changes in our implementation
    // There is no need anymore to populate a UserInfo object
    final User user = new User(entityID);
    if (null != entityID)
      user.setAttribute(UserManagerConstants.AttributeName.USER_KEY.getId(), entityID);

    final RandomPasswordGenerator    passwordGenerator = Platform.getBean(RandomPasswordGenerator.class);
    // passing a null as the password policy will hopefully switch off the
    // password validation
    final String                     randomPassword    = new String(passwordGenerator.generatePassword(user));
    final EntityManager              entityManager     = Platform.getService(EntityManager.class);

    final HashMap<String, Object> attribute = new HashMap<String, Object>();
    attribute.put("usr_password", randomPassword); // encryptedPassword);
    try {
      entityManager.modifyEntity(target.getType(), entityID, attribute);
    }
    catch (Exception e) {
      fatal(method, e);
    }

    trace(method, SystemMessage.METHOD_EXIT);
    // Event Result is a way for the event handler to notify the kernel of any
    // failures or errors and also if any subsequent actions need to be taken
    // (immediately or in a deferred fashion). It can also be used to indicate
    // if the kernel should restart this orchestration or veto it if the event
    // handler doesn't want to notify the kernel of anything it shouldn't return
    // a null value  instead an empty EventResult object
    return new EventResult();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** This function is called on bulk orchestration operations.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event
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

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cancel (overridden)
  /**
   ** Method containing the logic that need to be executed if the orchestration
   ** is cancelled.
   ** <p>
   ** This would be called only for asynchronous actions.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestartion event
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   **
   ** @return                    the outcome of the event handler.
   **                            If the event handler is defined to execute in a
   **                            synchronous mode, it must return a result.
   **                            If it is defined execute in asynchronous mode,
   **                            it must return <code>null</code>.
   */
  @Override
  public boolean cancel(final long processId, final long eventId, final AbstractGenericOrchestration orchestration) {
    throw new RuntimeException("Method not implemented");
  }
}