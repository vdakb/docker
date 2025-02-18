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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   MemberShipPostProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    MemberShipPostProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event.ugp;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import java.io.Serializable;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.pluginframework.Plugin;
import oracle.iam.platform.pluginframework.PluginFramework;
import oracle.iam.platform.pluginframework.PluginStoreException;

import oracle.iam.identity.rolemgmt.api.RoleManagerConstants;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.EventMessage;

import oracle.iam.identity.foundation.resource.EventBundle;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationMessage;
import bka.iam.identity.event.OrchestrationHandler;

////////////////////////////////////////////////////////////////////////////////
// class MemberShipPostProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This custom post process event handler sends notification to user/manager
 ** when a role is granted to the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MemberShipPostProcessor extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TEMPLATE_GRANTED_PARAMETER = "template.role.granted";
  private static final String TEMPLATE_REVOKED_PARAMETER = "template.role.revoked";

  private static final String TEMPLATE_GRANTED_DEFAULT   = "bka-role-granted";
  private static final String TEMPLATE_REVOKED_DEFAULT   = "bka-role-revoked";

  //////////////////////////////////////////////////////////////////////////////
  // instace attributes
  //////////////////////////////////////////////////////////////////////////////

  private String templateGranted                         = TEMPLATE_GRANTED_DEFAULT;
  private String templateRevoked                         = TEMPLATE_REVOKED_DEFAULT;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>MemberShipPostProcessor</code> notification handler
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public MemberShipPostProcessor() {
    // ensure inheritance
    super(OrchestrationHandler.LOGGER_CATEGORY);
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

    // process notification for this event
    processEvent(orchestration.getOperation(), orchestration.getParameters());

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

    // obtain event parameters to be able process all events in bulk
    final String[]                        entityID   = orchestration.getTarget().getAllEntityId();
    final HashMap<String, Serializable>[] parameters = orchestration.getBulkParameters();

    // Send notification for all events
    for (int i = 0; i < entityID.length; i++) {
      processEvent(orchestration.getOperation(), parameters[i]);
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
  // Method:   initialize (overridden)
  /**
   ** Called during creation of the orchestration engine at server startup.
   **
   ** @param  parameter          the parameter mapping passed to the
   **                            {@link AbstractPostProcessHandler} obtained
   **                            from the descriptor and send by the
   **                            <code>Orchestration</code>.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final Plugin plugin = PluginFramework.getPluginRegistry().getPlugin(EVENT, this.getClass().getName());
      final Map    data   = plugin.getMetadata();

      if (data.containsKey(TEMPLATE_GRANTED_PARAMETER)) {
        this.templateGranted = (String)data.get(TEMPLATE_GRANTED_PARAMETER);
        if (StringUtility.isEmpty(this.templateGranted))
          this.templateGranted = TEMPLATE_GRANTED_DEFAULT;
      }

      if (data.containsKey(TEMPLATE_REVOKED_PARAMETER)) {
        this.templateRevoked = (String)data.get(TEMPLATE_REVOKED_PARAMETER);
        if (StringUtility.isEmpty(this.templateRevoked))
          this.templateRevoked = TEMPLATE_REVOKED_DEFAULT;
      }
    }
    catch (PluginStoreException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processEvent
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  parameter          a {@link HashMap} containing orchestration
   **                            parameters. Key of the {@link HashMap} contains
   **                            parameter name and value is either a
   **                            ContextAware Object or Object. These are the
   **                            values that are used for carrying out the
   **                            operation.
   */
  private void processEvent(final String operation, final HashMap<String, Serializable> parameters) {
    final String method = "processEvent";
    trace(method, SystemMessage.METHOD_ENTRY);

    // obtain role's key assigned to the identity
    final String roleKey = fetchOrchestrationData(parameters, RoleManagerConstants.ROLE_KEY_PARAM);
    // Get the user key from the parameters attribute
    // Role can be requested for more than one user, this is reason why the
    // parameter is type of List.
    // In the list are all identities who are granted to or revoked from role
    final List<String> subjects = (List<String>)parameters.get(RoleManagerConstants.USER_KEYS_PARAM);
    if (subjects != null) {
      // send notification to the all users which are part of the operation
      for (String cursor : subjects) {
        try {
          // check if we are in the expected phase of identity administration
          if (CREATE.equals(operation)) {
            debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
            final NotificationEvent event = createNotificationEvent(this.templateGranted, cursor, cursor, roleKey);
            if (null != event) {
              notifyRecipient(event);
              debug(method, OrchestrationBundle.format(OrchestrationMessage.NOTIFICATION_ROLE_GRANTED, roleKey, cursor, this.templateGranted));
            }
          }
          else if (DELETE.equals(operation)) {
            debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
            final NotificationEvent event = createNotificationEvent(this.templateRevoked, cursor, cursor, roleKey);
            if (null != event) {
              notifyRecipient(event);
              debug(method, OrchestrationBundle.format(OrchestrationMessage.NOTIFICATION_ROLE_REVOKED, roleKey, cursor, this.templateRevoked));
            }
          }
        }
        catch (Exception e) {
          throw eventFailed(e, method, OrchestrationError.NOTIFICATION_FAILED, OrchestrationBundle.RESOURCE);
        }
        finally {
          trace(method, SystemMessage.METHOD_EXIT);
        }
      }
    }

    trace(method, SystemMessage.METHOD_EXIT);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotificationEvent
  /**
   ** This method creates/configure event object corresponding to the event when
   ** a role is granted to or revoked from a user.
   **
   ** @param  userKey
   ** @param  roleKey
   **
   ** @return                    the {@link NotificationEvent} to send out.
   */
  private NotificationEvent createNotificationEvent(final String template, final String userKey, final String recipient, final String roleKey) {
    NotificationEvent event = null;
    if (null != recipient) {
      event = new NotificationEvent();
      // set user identifier to whom notification is to be sent and set it in the event
      // object being created
      event.setUserIds(new String[] { recipient });
      // set template name to be used to send notification for this event
      event.setTemplateName(template);
      // setting senderId as null here and hence default sender ID would get
      // picked up
      event.setSender(null);

      // create a map with key value pair for the parameters declared at time of
      // configuring notification event
      final HashMap<String, Object> map = new HashMap<String, Object>();
      map.put(RoleManagerConstants.USER_KEYS_PARAM, userKey);
      map.put(RoleManagerConstants.ROLE_KEY_PARAM, roleKey);
      event.setParams(map);
    }
    return event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyRecipient
  /**
   ** Call notification Engine passing an event object to it.
   **
   ** @param  event
   **
   ** @throws NotificationException
   */
  private void notifyRecipient(final NotificationEvent event)
    throws NotificationException {

    final String method = "notifyRecipient";
    trace(method, SystemMessage.METHOD_ENTRY);

    // send the notification
    final NotificationService service = service(oracle.iam.notification.api.NotificationService.class);
    try {
      // Look to send notification only if sendNotificationTo has one or more emails or user or manager has email
      if (event.getUserIds() != null && event.getUserIds().length > 0) {
        service.notify(event);
      }
    }
    catch (EventException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_FAILED), e);
    }
    catch (UnresolvedNotificationDataException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_UNRESOLVED_DATA), e);
    }
    catch (TemplateNotFoundException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_TEMPLATE_NOTFOUND), e);
    }
    catch (MultipleTemplateException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_TEMPLATE_AMBIGOUS), e);
    }
    catch (NotificationResolverNotFoundException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_RESOLVER_NOTFOUND), e);
    }
    catch (UserDetailsNotFoundException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_IDENTITY_NOTFOUND), e);
    }
    catch (NotificationException e) {
      error(method, OrchestrationBundle.string(OrchestrationError.NOTIFICATION_EXCEPTION), e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}