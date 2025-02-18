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

    File        :   RoleNotification.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleNotification.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.role;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.iam.platform.authz.exception.AccessDeniedException;

import oracle.iam.platform.pluginframework.Plugin;
import oracle.iam.platform.pluginframework.PluginFramework;
import oracle.iam.platform.pluginframework.PluginStoreException;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.MANAGER_KEY;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;

import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_KEY_PARAM;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.USER_KEY_PARAM;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.USER_KEYS_PARAM;

import static oracle.iam.identity.rolemgmt.utils.RoleManagerUtils.getParameterValue;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.EventMessage;

import oracle.iam.identity.foundation.resource.EventBundle;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class RoleNotification
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This custom post process event handler sends notification to user/manager
 ** when a role is granted to the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class RoleNotification extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY           = "OCS.USR.PROVISIONING";

  private static final String TEMPLATE_ASSIGNED         = "ocs.notification.template.assigned";
  private static final String TEMPLATE_REVOKED          = "ocs.notification.template.revoked";

  private static final String DEFAULT_TEMPLATE_ASSIGNED = "OCS Role Assigned";
  private static final String DEFAULT_TEMPLATE_REVOKED  = "OCS Role Revoked";

  //////////////////////////////////////////////////////////////////////////////
  // instace attributes
  //////////////////////////////////////////////////////////////////////////////

  private String templateAssigend = DEFAULT_TEMPLATE_ASSIGNED;
  private String templateRevoked  = DEFAULT_TEMPLATE_REVOKED;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleNotification</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleNotification() {
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

    // retrieving role's key assigned to the user
    final String roleKey = (String)getParameterValue(orchestration, ROLE_KEY_PARAM);
    String       userKey = null;
    // workaround to find user identifier as this operation is being assumed as
    // bulk  orchestration and hence user key is stored as array list
    Object subject = getParameterValue(orchestration, USER_KEYS_PARAM);
    if (subject == null) {
      subject = getParameterValue(orchestration, USER_KEY_PARAM);
      if (subject != null)
        userKey = (String)subject;
    }
    else {
      userKey = ((ArrayList<String>)subject).get(0);
    }

    try {
      trace(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
      final NotificationEvent event = createNotificationEvent(userKey, roleKey, null);
      trace(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_SEND));
      sendNotification(event);
    }
    catch (Exception e) {
      throw new EventFailedException(processId, "arg2", "arg3", "arg4", "arg5", e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
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
   **                            <code>EventHandler</code> obtained from the
   **                            descriptor and send by the Orchestration.
   */
  @Override
  public void initialize(final HashMap<String, String> parameter) {
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final Plugin plugin = PluginFramework.getPluginRegistry().getPlugin(EVENT, this.getClass().getName());
      final Map    data   = plugin.getMetadata();

      this.templateAssigend = (String)data.get(TEMPLATE_ASSIGNED);
      if (StringUtility.isEmpty(templateAssigend))
        this.templateAssigend = DEFAULT_TEMPLATE_ASSIGNED;

      this.templateRevoked = (String)data.get(TEMPLATE_REVOKED);
      if (StringUtility.isEmpty(templateRevoked))
        this.templateRevoked = DEFAULT_TEMPLATE_REVOKED;
    }
    catch (PluginStoreException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotificationEvent
  /**
   ** This method creates/configure event object corresponding to the event when
   ** a role is assigned to a user.
   **
   ** @param  userKey
   ** @param  roleKey
   ** @param  senderId
   **
   ** @return                    the {@link NotificationEvent} to send out.
   **
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   ** @throws AccessDeniedException
   */
  private NotificationEvent createNotificationEvent(String userKey, String roleKey, String senderId)
    throws NoSuchUserException
    ,      UserLookupException
    ,      AccessDeniedException {

    final NotificationEvent event = new NotificationEvent();
    // get user IDs to whom notification is to be sent and set it in the event
    // object being created
    event.setUserIds(createRecipient(userKey));

    // set template name to be used to send notification for this event
    event.setTemplateName(this.templateAssigend);

    // setting senderId as null here and hence default sender ID would get
    // picked up
    event.setSender(senderId);

    // create a map with key value pair for the parameters declared at time of
    // configuring notification event
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("usr_key",  userKey);
    map.put("role_key", roleKey);
    event.setParams(map);

    return event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRecipient
  /**
   ** This method creates a string array containing list of user IDs to whom
   ** notification is to be sent
   **
   ** @param  indentifier        the system identifier of an identity in Oracle
   **                            Identity Manager.
   **
   ** @return                     a array of strings containing list of user IDs
   **                             to whom notification is to be sent.
   **
   ** @throws NoSuchUserException
   ** @throws UserLookupException
   ** @throws AccessDeniedException
   */
  private String[] createRecipient(final String indentifier)
    throws NoSuchUserException
    ,      UserLookupException
    ,      AccessDeniedException {

    final UserManager facade = service(UserManager.class);

    Set<String> attributes = new HashSet<String>();

    // sending notification to both the user and his/her manager
    attributes.add(MANAGER_KEY.getId());
    attributes.add(USER_LOGIN.getId());

    // retrieving User ID and add it to the list of recipients
    User identity = facade.getDetails(indentifier, attributes, false);
    final List<String> recipient = new ArrayList<String>();
    recipient.add(identity.getAttribute(USER_LOGIN.getId()).toString());

    // retrieving Manager ID and add it to the list of recipients if its set
    if (identity.getAttribute(MANAGER_KEY.getId()) != null) {
      identity = facade.getDetails(identity.getAttribute(MANAGER_KEY.getId()).toString(), attributes, false);
      recipient.add(identity.getAttribute(USER_LOGIN.getId()).toString());
    }
    return recipient.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRecipient
  /**
   ** Call notification Engine passing an event object to it.
   **
   ** @param  event
   **
   ** @throws NotificationException
   */
  private void sendNotification(final NotificationEvent event)
    throws NotificationException {

    try {
      // call notify method of NotificationService to pass on the event to
      // notification engine
      NotificationService facade = service(NotificationService.class);
      facade.notify(event);
    }
    catch (EventException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (UnresolvedNotificationDataException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (TemplateNotFoundException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (MultipleTemplateException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (NotificationResolverNotFoundException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (UserDetailsNotFoundException e) {
      throw new NotificationException(e.getMessage(), e.getCause());
    }
    catch (NotificationException e) {
      throw e;
    }
  }
}