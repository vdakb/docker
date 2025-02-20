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

    Copyright 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   IdentityPreProcessor.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityPreProcessor.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  nkolandj    First release version
*/

package bka.iam.identity.event.usr;

import java.util.Date;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.PROVISIONINGDATE;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_ORGANIZATION;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.ACCOUNT_START_DATE;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.DateUtility;

import oracle.iam.identity.foundation.event.AbstractPreProcessHandler;

import bka.iam.identity.event.OrchestrationHandler;

////////////////////////////////////////////////////////////////////////////////
// class IdentityPreProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Offers common methods to handle identity objects in a
 ** <code>PreProcessEventHandler</code>.
 ** <p>
 ** This processor evaluates the defaults for standard attributes
 ** <ul>
 **   <li>Identity Start Date
 **   <li>Identity Provisioning Date
 **   <li>Identity Locale
 **   <li>Identity Timezone
 ** </ul>
 ** Furthermore it evaluates the attribute value for the custom attribute
 ** <code>participant</code> that have a important role for the provisioning to
 ** the Directory Services involved.
 ** <br>
 ** The value for the custom attribute <code>participant</code> ist derived from
 ** the <code>Organization Name</code> the identity to take in account is
 ** assinged to. The value is simply copied to the custom attribute without
 ** further validation.
 ** <br>
 ** The validation handler plugged-in takes care about the correctness of this
 ** value.
 ** <p>
 ** Following assumptions are made:
 ** <br>
 ** <ol>
 **   <li><b>Any</b> user which is permitted to enter identities manually is not
 **       permitted to enter those identities outside of his own scope.
 **   <li><b>Only</b> members of the Administrator role are permitted to enter
 **       identities manually outside of any scope.
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class IdentityPreProcessor extends AbstractPreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the flag determines the orchestration originates to manual interaction */
  static final String ORCHESTRATION_ORIGIN     = "bka.usr.identity";
  static final String ORCHESTRATION_PREPROCESS = ORCHESTRATION_ORIGIN + ".pre-processor";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentityPreProcessor</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityPreProcessor() {
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
    control.put(ORCHESTRATION_ORIGIN, ORCHESTRATION_PREPROCESS);
    // check if we are in the expected phase of identity administration
    switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
      // perform all actions that are necessary for create and modify of the
      // existing identity
      case CREATE  : onCreate(orchestration.getParameters());
                     break;
      case MODIFY  : onModify(orchestration.getTarget().getEntityId(), orchestration.getParameters());
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

    // obtain event parameters to be able process all events in bulk
    final String[]                        entityID  = orchestration.getTarget().getAllEntityId();
    final HashMap<String, Serializable>[] parameter = orchestration.getBulkParameters();
    
    final HashMap<String, Serializable> control = orchestration.getInterEventData();
    control.put(ORCHESTRATION_ORIGIN, ORCHESTRATION_PREPROCESS);

    // process all events in bulk
    for (int i = 0; i < entityID.length; i++) {
      // check if we are in the expected phase of identity administration
      switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
        // perform all actions that are necessary for create and modify of the
        // existing identity
        case CREATE  : onCreate(parameter[i]);
                       break;
        case MODIFY  : onModify(entityID[i], parameter[i]);
                       break;
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
  // Method:   onCreate
  /**
   ** Perform all actions belonging to create an identity manually.
   **
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. Key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   */
  static void onCreate(final HashMap<String, Serializable> parameter) {
    final Date today = DateUtility.today();
    // check if the identity start date is set and put the default in the
    // orchestration parameters when not set
    if (!parameter.containsKey(ACCOUNT_START_DATE.getId())) {
      parameter.put(ACCOUNT_START_DATE.getId(), today);
    }
    // check if the identity provisioning date is set and put the default in the
    // orchestration parameters when not set
    if (!parameter.containsKey(PROVISIONINGDATE.getId())) {
      parameter.put(PROVISIONINGDATE.getId(), today);
    }
    // check if the identity locale is set and put the default in the
    // orchestration parameters when not set
    if (!parameter.containsKey(UserManagerConstants.AttributeName.LOCALE.getId())) {
      parameter.put(UserManagerConstants.AttributeName.LOCALE.getId(), OrchestrationHandler.ATTRIBUTE_LOCALE_DEFAULT);
    }
    // check if the identity participant is set and put the organization name in
    // the orchestration parameters when not set
    // the validation handler will complain about the value if its not match the
    // pattern allowed for this attribute
    if (!parameter.containsKey(OrchestrationHandler.ATTRIBUTE_PARTICIPANT)) {
      final String organization = OrchestrationHandler.organization(fetchOrchestrationData(parameter, USER_ORGANIZATION.getId()), false);
      parameter.put(OrchestrationHandler.ATTRIBUTE_PARTICIPANT, organization);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onModify
  /**
   ** Perform all actions belonging to modify an identity manually.
   **
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. Key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   */
  static void onModify(final String entityId, final HashMap<String, Serializable> parameter) {
    if (!parameter.containsKey(OrchestrationHandler.ATTRIBUTE_PARTICIPANT)) {
      String organization = null;
      if (!parameter.containsKey(USER_ORGANIZATION.getId())) {
        organization = OrchestrationHandler.organization(String.valueOf(OrchestrationHandler.userOrganization(entityId, false)), false);
      }
      else {
        organization = OrchestrationHandler.organization(fetchOrchestrationData(parameter, USER_ORGANIZATION.getId()), false);
      }
      parameter.put(OrchestrationHandler.ATTRIBUTE_PARTICIPANT, organization);
    }
  }
}