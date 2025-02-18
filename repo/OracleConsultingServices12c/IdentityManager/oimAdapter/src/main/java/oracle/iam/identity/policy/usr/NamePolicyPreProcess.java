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

    File        :   NamePolicyPreProcess.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    NamePolicyPreProcess.


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

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.iam.identity.utils.Constants;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.usermgmt.utils.UserManagerUtils;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.Replace;

import oracle.iam.identity.foundation.event.AbstractPreProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class NamePolicyPreProcess
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Offers common methods to handle identity objects in a
 ** <code>PreProcessEventHandler</code>.
 ** <p>
 ** The EventHandler evaluates the <code>Display Name</code> and
 ** <code>Common Name</code> based on <code>First Name</code>,
 ** <code>Last Name</code>, <code>Middle Name</code> and
 ** <code>Employee Type</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class NamePolicyPreProcess extends AbstractPreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>NamePolicyPreProcess</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public NamePolicyPreProcess() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** The implementation of this pre process event handler in one-off
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

    final HashMap<String, Serializable> control = orchestration.getInterEventData();
    // check if we are in the expected phase of identity administration
    switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
      // perform all actions that are necessary for create and modify of the
      // existing identity
      case CREATE  : preCreate(processId, orchestration.getParameters());
                     control.put(NamePolicyHandler.ORCHESTRATION_ORIGIN, NamePolicyHandler.ORCHESTRATION_PREPROCESS);
                     break;
      case MODIFY  : preModify(orchestration.getParameters(), (User)control.get(UserManagerUtils.CURRENT_USER_ATTR));
                     control.put(NamePolicyHandler.ORCHESTRATION_ORIGIN, NamePolicyHandler.ORCHESTRATION_PREPROCESS);
                     break;
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
  // Method:   execute (PreProcessHandler)
  /**
   ** The implementation of this pre process event handler for bulk
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

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preCreate
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   */
  static void preCreate(final long processId, final HashMap<String, Serializable> parameter)
    throws EventFailedException {

    final String method = "preCreate";

    final String firstName = Handler.stringValue(UserManagerConstants.AttributeName.FIRSTNAME.getId(), parameter);
    if (NamePolicy.empty(firstName))
      throw Handler.exception(null, method, NamePolicyError.IDENTITY_PRE_PROCESS_DATA, new Object[] { processId, UserManagerConstants.AttributeName.FIRSTNAME.getId()});

    final String lastName = Handler.stringValue(UserManagerConstants.AttributeName.LASTNAME.getId(), parameter);
    if (NamePolicy.empty(lastName))
      throw Handler.exception(null, method, NamePolicyError.IDENTITY_PRE_PROCESS_DATA, new Object[] { processId, UserManagerConstants.AttributeName.LASTNAME.getId()});

    final String employeeType  = Handler.stringValue(UserManagerConstants.AttributeName.EMPTYPE.getId(), parameter);
    if (NamePolicy.empty(employeeType))
      throw Handler.exception(null, method, NamePolicyError.IDENTITY_PRE_PROCESS_DATA, new Object[] { processId, UserManagerConstants.AttributeName.EMPTYPE.getId()});

    updateProfile(parameter, firstName, lastName, Handler.stringValue(UserManagerConstants.AttributeName.MIDDLENAME.getId(), parameter), employeeType);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preModify
  /**
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   ** @param  before             a {@link User} containing orchestration
   **                            values. This data is not used for carrying out
   **                            the operation rather this data is used for communication between handlers
   */
  static void preModify(final HashMap<String, Serializable> parameter, final User before) {
    final String firstName    = parameter.containsKey(UserManagerConstants.AttributeName.FIRSTNAME.getId())  ? Handler.stringValue(UserManagerConstants.AttributeName.FIRSTNAME.getId(),  parameter) : before.getFirstName();
    final String lastName     = parameter.containsKey(UserManagerConstants.AttributeName.LASTNAME.getId())   ? Handler.stringValue(UserManagerConstants.AttributeName.LASTNAME.getId(),   parameter) : before.getLastName();
    final String middleName   = parameter.containsKey(UserManagerConstants.AttributeName.MIDDLENAME.getId()) ? Handler.stringValue(UserManagerConstants.AttributeName.MIDDLENAME.getId(), parameter) : before.getMiddleName();
    final String employeeType = parameter.containsKey(UserManagerConstants.AttributeName.EMPTYPE.getId())    ? Handler.stringValue(UserManagerConstants.AttributeName.EMPTYPE.getId(),    parameter) : before.getEmployeeType();

    updateProfile(parameter, firstName, lastName, middleName, employeeType);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updateProfile
  /**
   ** @param  firstName          the <code>First Name</code> value of an
   **                            identity used to apply the naming policies.
   ** @param  lastName           the <code>Last Name</code> value of an
   **                            identity used to apply the naming policies.
   ** @param  middleName         the <code>Middle Name</code> value of an
   **                            identity used to apply the naming policies.
   ** @param  employeeType       the <code>Employee Type</code> value of an
   **                            identity used to apply the naming policies.
   */
  static void updateProfile(final HashMap<String, Serializable> parameter, final String firstName, final String lastName, final String middleName, final String employeeType)
    throws EventFailedException {

    final Replace                 parser      = NamePolicy.parser(firstName, lastName, middleName, employeeType);
    final HashMap<String, String> displayName = new HashMap<String, String>();
    displayName.put(Constants.MLS_BASE_VALUE, NamePolicy.build(parser, NamePolicy.Rule.DN));

    parameter.put(UserManagerConstants.AttributeName.COMMONNAME.getId(),  NamePolicy.normalized(parser, NamePolicy.Rule.CN));
    parameter.put(UserManagerConstants.AttributeName.DISPLAYNAME.getId(), displayName);
  }
}