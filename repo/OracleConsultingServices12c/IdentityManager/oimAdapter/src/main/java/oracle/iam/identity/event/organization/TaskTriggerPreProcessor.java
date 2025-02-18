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

    File        :   TaskTriggerPreProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskTriggerPreProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.organization;

import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.entitymgr.NoSuchEntityException;
import oracle.iam.platform.entitymgr.EntityManager;

import oracle.iam.platform.entitymgr.vo.EntityDefinition;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.identity.resources.LRB;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractPreProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class TaskTriggerPreProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation to get back the process triggers on organizations that was
 ** lost in transistion from Release 1 to Release 2.
 ** <p>
 ** The purpose of this event handler is to store the before image of an
 ** identity like organizations, roles and/or users in the orchestration inter
 ** event stage.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class TaskTriggerPreProcessor extends AbstractPreProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskTriggerPreProcessor</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TaskTriggerPreProcessor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PreProcessHandler)
  /**
   ** This function is called on one-off orchestration operations.<p>
   ** In this implementation we will generate a global unique id.
   ** <p>
   ** This handler will work for CREATE operations only.
   **
   ** @param  processID          the identifier of the orchestration process.
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
  public EventResult execute(final long processID, final long eventID, final Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String entityID = orchestration.getTarget().getEntityId();
    if (entityID != null) {
      try {
        // check if the inter event data are already exists to prevent
        // overriding of existing data but create a new mapping if it's not
        // exists so far
        HashMap<String, Serializable> data = orchestration.getInterEventData();
        if (data == null) {
          data = new HashMap<String, Serializable>();
          orchestration.setInterEventData(data);
        }
        // store the before image of the entity at the time the orchestration
        // data are not persisted
        final OrganizationManager facade = service(OrganizationManager.class);
        data.put(TaskTrigger.BEFORE, facade.getDetails(entityID, attributes(), false));
      }
      catch (Exception e) {
        warning(LRB.DEFAULT.getString("IAM-4051000", OrganizationManagerConstants.AttributeName.ID_FIELD, entityID));
      }
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
    final String[] identities = orchestration.getTarget().getAllEntityId();
    if ((identities != null) && (identities.length > 0)) {
      // check if the inter event data are already exists to prevent
      // overriding of existing data but create a new mapping if it's not
      // exists so far
      HashMap<String, Serializable> data = orchestration.getInterEventData();
      if (data == null) {
        data = new HashMap<String, Serializable>();
        orchestration.setInterEventData(data);
      }

      // store the before images of all entities at the time the orchestration
      // data are not persisted
      final Set<String> attributes   = attributes();
      final Identity[]  organization = new Identity[identities.length];
      int i = 0;
      for (String entityID : identities) {
        try {
          final OrganizationManager om = service(OrganizationManager.class);
          Identity o = om.getDetails(entityID, attributes, false);
          organization[(i++)] = o;
        }
        catch (Exception e) {
          warning(LRB.DEFAULT.getString("IAM-4051000", OrganizationManagerConstants.AttributeName.ID_FIELD, entityID));
        }
      }
      data.put(TaskTrigger.BEFORE, organization);
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
  // Method:   attributes
  /**
   ** Returns the attribute names of all attributes.
   **
   ** @return                    the attribute names of all attributes.
   */
  private Set<String> attributes() {
    final EntityManager entityManager = service(EntityManager.class);
    try {
      final EntityDefinition definition = entityManager.getEntityDefinition(OrganizationManagerConstants.ORGANIZATION_ENTITY);
      return definition.getAttributeNames();
    }
    catch (NoSuchEntityException e) {
      error("attributes", LRB.DEFAULT.getString("IAM-4051002"));
      return new HashSet<String>();
    }
  }
}