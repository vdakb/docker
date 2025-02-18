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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   NamePolicyPostProcess.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NamePolicyPostProcess.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.policy.usr;

import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.usermgmt.utils.UserManagerUtils;

import oracle.iam.identity.exception.AccessDeniedException;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserModifyException;
import oracle.iam.identity.exception.ValidationFailedException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.Replace;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class NamePolicyPostProcess
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>NamePolicyPostProcess</code> act as the service end point for
 ** Oracle Identity Manager to post-process user form values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class NamePolicyPostProcess extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NamePolicyPostProcess</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NamePolicyPostProcess() {
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
   ** In this implementation we will generate a global unique id.
   **
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event
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
  public EventResult execute(final long processId, final long eventId, final Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final HashMap<String, Serializable> data = orchestration.getInterEventData();
    if (!data.containsKey(NamePolicyHandler.ORCHESTRATION_ORIGIN)) {
      final HashMap<String, Serializable> parameter = orchestration.getParameters();
      if (!(parameter.containsKey(UserManagerConstants.AttributeName.DISPLAYNAME.getId()) || parameter.containsKey(UserManagerConstants.AttributeName.COMMONNAME.getId()))) {
        // get the first, last name and type of an identity how they are know
        // until this handler is executed
        final User before = (User)data.get(UserManagerUtils.CURRENT_USER_ATTR);
        // get the first, last name and type of an identity how they will be
        // after this handler is executed
        final User after  = (User)data.get(UserManagerUtils.NEW_USER_STATE);
        // check if we are in the expected phase of identity administration
        switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
          // perform all actions that are necessary for create and modify of the
          // existing identity
          case CREATE  : postCreate(processId, eventId, after);
                         break;
          case MODIFY  : postModify(processId, eventId, parameter, before, after);
                         break;
        }
      }
    }
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
   ** The implementation of this post process event handler for bulk
   ** orchestration.
   **
   ** @param  processID          the identifier of the orchestration process.
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
  public BulkEventResult execute(final long processID, final long eventId, final BulkOrchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final HashMap<String, Serializable> data = orchestration.getInterEventData();
    if (!data.containsKey(NamePolicyHandler.ORCHESTRATION_ORIGIN)) {
      final UserManagerConstants.Operations operation = UserManagerConstants.Operations.valueOf(orchestration.getOperation());
      final HashMap<String, Serializable>[] parameter = orchestration.getBulkParameters();
      final String[]                        entities  = orchestration.getTarget().getAllEntityId();
      final Identity[]                      after     = (Identity[])data.get(UserManagerUtils.NEW_USER_STATE);
      final Identity[]                      before    = (Identity[])data.get(UserManagerUtils.CURRENT_USER_ATTR);
      for (int i = 0; i < entities.length; i++) {
        // check if we are in the expected phase of identity administration
        switch (operation) {
          // perform all actions that are necessary for create and modify of the
          // existing identity
          case CREATE  : postCreate(processID, eventId, after[i]);
                         break;
          case MODIFY  : postModify(processID, eventId, parameter[i], before[i], after[i]);
                         break;
        }
      }
    }
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
  // Method:   postCreate
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event.
   ** @param  after              a {@link Identity} containing orchestration
   **                            values. These are the values that are used for
   **                            carrying out the operation.
   */
  static void postCreate(final long processId, final long eventId, final Identity after) {
    final String firstName    = (String)after.getAttribute(UserManagerConstants.AttributeName.FIRSTNAME.getId());
    final String lastName     = (String)after.getAttribute(UserManagerConstants.AttributeName.LASTNAME.getId());
    final String middleName   = (String)after.getAttribute(UserManagerConstants.AttributeName.MIDDLENAME.getId());
    final String employeeType = (String)after.getAttribute(UserManagerConstants.AttributeName.EMPTYPE.getId());
    updateIdentity(processId, eventId, after.getEntityId(), firstName, lastName, middleName, employeeType);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   postModify
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   ** @param  before             a {@link Identity} containing orchestration
   **                            values. This data is not used for carrying out
   **                            the operation rather this data is used for
   **                            communication between handlers.
   ** @param  after              a {@link Identity} containing orchestration
   **                            values. These are the values that are used for
   **                            carrying out the operation.
   */
  static void postModify(final long processId, final long eventId, final HashMap<String, Serializable> parameter, final Identity before, final Identity after)
    throws EventFailedException {

    if (parameter.containsKey(UserManagerConstants.AttributeName.FIRSTNAME.getId()) || parameter.containsKey(UserManagerConstants.AttributeName.LASTNAME.getId()) || parameter.containsKey(UserManagerConstants.AttributeName.MIDDLENAME.getId()) || parameter.containsKey(UserManagerConstants.AttributeName.EMPTYPE.getId())) {
      final String firstName    = parameter.containsKey(UserManagerConstants.AttributeName.FIRSTNAME.getId())  ? (String)after.getAttribute(UserManagerConstants.AttributeName.FIRSTNAME.getId())  : (String)before.getAttribute(UserManagerConstants.AttributeName.FIRSTNAME.getId());
      final String lastName     = parameter.containsKey(UserManagerConstants.AttributeName.LASTNAME.getId())   ? (String)after.getAttribute(UserManagerConstants.AttributeName.LASTNAME.getId())   : (String)before.getAttribute(UserManagerConstants.AttributeName.LASTNAME.getId());
      final String middleName   = parameter.containsKey(UserManagerConstants.AttributeName.MIDDLENAME.getId()) ? (String)after.getAttribute(UserManagerConstants.AttributeName.MIDDLENAME.getId()) : (String)before.getAttribute(UserManagerConstants.AttributeName.MIDDLENAME.getId());
      final String employeeType = parameter.containsKey(UserManagerConstants.AttributeName.EMPTYPE.getId())    ? (String)after.getAttribute(UserManagerConstants.AttributeName.EMPTYPE.getId())    : (String)before.getAttribute(UserManagerConstants.AttributeName.EMPTYPE.getId());
      updateIdentity(processId, eventId, after.getEntityId(), firstName, lastName, middleName, employeeType);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateIdentity
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  eventId            the identifier of the orchestration event.
   ** @param  entityId           the system identifier of the identity the
   **                            orchestration event belongs to.
   ** @param  firstName          the <code>First Name</code> value of an
   **                            identity used to apply the naming policies.
   ** @param  lastName           the <code>Last Name</code> value of an
   **                            identity used to apply the naming policies.
   ** @param  employeeType       the <code>Employee Type</code> value of an
   **                            identity used to apply the naming policies.
   */
  static void updateIdentity(final long processId, final long eventId, final String entityId, final String firstName, final String lastName, final String middleName, final String employeeType)
    throws EventFailedException {

    final String  method = "updateIdentity";
    final Replace parser = NamePolicy.parser(firstName, lastName, middleName, employeeType);
    final User    update = new User(entityId);
    // process Common Name
    update.setCommonName(NamePolicy.normalized(parser, NamePolicy.Rule.CN));
    // process Display Name
    update.setDisplayName(NamePolicy.build(parser, NamePolicy.Rule.DN));
    try {
      Platform.getService(UserManager.class).modify(update);
    }
    catch (AccessDeniedException e) {
      throw Handler.exception(e, method, NamePolicyError.IDENTITY_POST_PROCESS_SIMPLE, new Object[] { processId, eventId });
    }
    catch (ValidationFailedException e) {
      throw Handler.exception(e, method, NamePolicyError.IDENTITY_POST_PROCESS_SIMPLE, new Object[] { processId, eventId });
    }
    catch (UserModifyException e) {
      throw Handler.exception(e, method, NamePolicyError.IDENTITY_POST_PROCESS_SIMPLE, new Object[] { processId, eventId });
    }
    catch (NoSuchUserException e) {
      throw Handler.exception(e, method, NamePolicyError.IDENTITY_POST_PROCESS_SIMPLE, new Object[] { processId, eventId });
    }
  }
}