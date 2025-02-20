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

    Copyright 2019 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Common Shared Plugin

    File        :   PasswordNotification.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PasswordNotification.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2019  DSteding    First release version
*/

package bka.iam.identity.event.usr;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

import oracle.iam.conf.exception.SystemConfigurationServiceException;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;

import oracle.iam.platform.context.ContextManager;

import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;

import oracle.iam.platform.utils.crypto.CryptoUtil;
import oracle.iam.platform.utils.crypto.CryptoException;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.utils.Utils;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.usermgmt.impl.UserMgrUtil;

import oracle.iam.identity.usermgmt.utils.UserManagerUtils;

import oracle.iam.passwordmgmt.vo.UserInfo;
import oracle.iam.passwordmgmt.vo.Constants;

import oracle.iam.passwordmgmt.domain.repository.UserRepository;
import oracle.iam.passwordmgmt.domain.repository.DBUserRepository;

import oracle.iam.passwordmgmt.exception.UserNotFoundException;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.SEND_PASSWORD_NOTIFICATION_FLAG;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.PASSWORD;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;

import oracle.iam.identity.usermgmt.internal.api.UserManagerInternal;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

import bka.iam.identity.event.OrchestrationError;
import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationHandler;

////////////////////////////////////////////////////////////////////////////////
// class PasswordNotification
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>PasswordNotification</code> performs all actions required to notify
 ** users about a change on their account password in Identity Manager.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PasswordNotification extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribtes
  //////////////////////////////////////////////////////////////////////////////

  private static final String SEPARATOR                   = ",";

  private static final String TEMPLATE_PASSWORD_RESET     = "bka-password-reset";
  private static final String TEMPLATE_PASSWORD_GENERATED = "bka-password-generated";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PasswordNotification</code> event handler that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PasswordNotification() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
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
  public EventResult execute(final long processId, final long eventId, final Orchestration orchestration) {
    final String method = "execute";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean send = Boolean.parseBoolean(fetchOrchestrationData(orchestration, UserManagerConstants.SEND_NOTIFICATION));
    // don't spend any effort in further processing if its not required
    if (!send)
      processEvent(orchestration);

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
  // Method:   processEvent (PostProcessHandler)
  /**
   ** The implementation of this pre process event handler in one-off
   ** orchestration.
   ** <p>
   ** All User prepopulate events are handled in this method
   **
   ** @param  orchestration      the object containing contextual information
   **                            such as orchestartion parameters, operation.
   */
  protected void processEvent(final Orchestration orchestration) {
    final String method = "processEvent";
    trace(method, SystemMessage.METHOD_ENTRY);

    Object       recipientObj = fetchOrchestrationData(orchestration, UserManagerConstants.SEND_NOTIFICATION_TO);
    List<String> recipient    = null;
    if (recipientObj != null)
      recipient = Arrays.asList(new String((char[])recipientObj).split(SEPARATOR));

    String userID = fetchOrchestrationData(orchestration, USER_LOGIN.getId());
    try  {
      if (userID == null) {
        String userKey = orchestration.getTarget().getEntityId();
        userID = UserMgrUtil.getUserLoginFromId(userKey);
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    final UserRepository repository = new DBUserRepository();
    final Identity       identity   = (Identity)orchestration.getInterEventData().get(UserManagerUtils.NEW_USER_STATE);
    final UserInfo       user       = (identity == null) ? repository.getUserAndManagerInfo(userID) : convert(identity);
    final String         encrypted  = fetchOrchestrationData(orchestration, PASSWORD.getId());
    // check if we are in the expected phase of identity administration
    switch (UserManagerConstants.Operations.valueOf(orchestration.getOperation())) {
      // perform all actions belobging to password notification that are
      // necessary after create an identity
      case CREATE          : onCreate(user, encrypted, orchestration.getInterEventData().containsKey(Constants.POLICY_DATA_KEY), recipient);
                             break;
      case RESET_PASSWORD  :
      case CHANGE_PASSWORD : onChange(user, encrypted, recipient);
                             break;
        
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onCreate
  /**
   ** Perform all actions belonging to password notification that are necessary
   ** after create an identity.
   **
   ** @param  identity           the affected user identity.
   ** @param  encrypted          the encrypted password of the affected user
   **                            identity.
   */
  void onCreate(final UserInfo identity, final String encrypted, final boolean generated, final List recipient) {
    final String method = "onCreate";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      // Looks like it's a bug
      // for EE Auto-generated user password (PASSWORD_GENERATED) should be set
      // in  ResetPasswordPreProcessHandler but it isn't
      // therefor we assume that it generated if a password policy is mapped in
      // the process interchange data
//      boolean isPwdAutoGen = false;
//      String  passwdGen    = (String)identity.getAttribute(PASSWORD_GENERATED.getId());
//      if (passwdGen != null && passwdGen.equalsIgnoreCase(UserManagerConstants.AttributeValues.PWD_AUTO_GENERATED_TRUE.getId())) {
//        isPwdAutoGen = true;
//      }
      if (generated) {
        final char[] password = CryptoUtil.getDecryptedPassword(encrypted, null);
        if (password != null) {
          // password is sent as a char[] for further processing to prevent
          // password logging
          notifyRecipient(identity, password, TEMPLATE_PASSWORD_GENERATED, recipient);
        }
      }
    }
    catch (CryptoException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onChange
  /**
   ** Perform all actions belonging to password notification that are necessary
   ** after an administrator resets or change a password.
   **
   ** @param  identity           the affected user identity.
   ** @param  encrypted          the encrypted password of the affected user
   **                            identity.
   ** @param  recipient          the affected user identity.
   */
  void onChange(final UserInfo identity, final String encrypted, final List recipient) {
    final String method = "onChange";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      final String notification = (String)ContextManager.getValue(SEND_PASSWORD_NOTIFICATION_FLAG);
      if ((notification == null) || (Boolean.valueOf(notification))) {
        final char[] password = CryptoUtil.getDecryptedPassword(encrypted, null);
        if (password != null) {
          // password is sent as a char[] for further processing to prevent
          // password logging
          notifyRecipient(identity, password, TEMPLATE_PASSWORD_RESET, recipient);
        }
      }
    }
    catch (CryptoException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyRecipient
  /**
   ** Perform all actions belonging to password notification.
   **
   ** @param  identity           the affected user identity.
   ** @param  password           the unencrypted password of the affected user
   **                            identity.
   ** @param  template           the email template used to form the email sent
   **                            to recipients.
   ** @param  recipient          the recipients of the email to sent.
   */
  private void notifyRecipient(final UserInfo identity, final char[] password, final String template, final List recipient) {
    final String method = "notifyRecipient";
    trace(method, SystemMessage.METHOD_ENTRY);

    final NotificationEvent event = new NotificationEvent();
    // set template name to be used to send notification
    event.setTemplateName(template);
    // setting senderId as null here hence default sender ID would get picked up
    event.setSender(null);

    final String userID           = identity.getLoginID();
    final String userEmailID      = identity.getUserEmailID();
    final String managerID        = identity.getManagerLoginID();
    final String managerEmailID   = identity.getManagerEmailID();
    // when the value of this property is TRUE, the email notification for reset
    // password is sent to other recipients if the email ID of the user is not
    // specified.
    boolean notifyPasswordToOther = true;

    // copies the user's manager in the email notification that is sent when a
    // user is created if value is TRUE
    boolean notifyUserCreateToOther = false;
    try {
      notifyPasswordToOther = OrchestrationHandler.systemProperty(Constants.NOTIFY_PASSWORD_TO_OTHER_SYSTEM_PROPERTY).equalsIgnoreCase("true");
    }
    catch (SystemConfigurationServiceException e) {
      error(method, OrchestrationBundle.format(OrchestrationError.PROPERTY_NOTFOUND, Constants.NOTIFY_PASSWORD_TO_OTHER_SYSTEM_PROPERTY));
    }

    // just for user create
    if (TEMPLATE_PASSWORD_GENERATED.equalsIgnoreCase(template)) {
      if (userEmailID != null && !userEmailID.isEmpty()) {
        // in case of user create with auto-gen password , system property
        // -XL.NotifyUserCreateToOther should be taken into account
        // if XL.NotifyUserCreateToOther = true , send notification to both user
        // and manager else send notification to user
        if (notifyUserCreateToOther && managerEmailID != null && !managerEmailID.isEmpty()) {
          event.setUserIds(new String[] { userID, managerID });
        }
        else
          event.setUserIds(new String[] { userID });
      }
      else if (notifyUserCreateToOther && managerEmailID != null && !managerEmailID.isEmpty()) {
        event.setUserIds(new String[] { managerID });
      }
    }
    // in case of CHANGE PASSWORD/RESET PASSWORD ONLY
    else if (TEMPLATE_PASSWORD_RESET.equalsIgnoreCase(template)) {
     if (userEmailID != null && !userEmailID.isEmpty()) {
        event.setUserIds(new String[] { userID });
      }
      else if (notifyPasswordToOther && managerEmailID != null && !managerEmailID.isEmpty()) {
        event.setUserIds(new String[] { managerID });
      }
    }

    final HashMap<String, Object> data = new HashMap<String, Object>();
    data.put(Constants.FIRST_NAME,           (identity.getFirstName() == null) ? new String("") : identity.getFirstName());
    data.put(Constants.LAST_NAME,            (identity.getLastName() == null) ? new String("") : identity.getLastName());
    data.put(Constants.USER_ID,              identity.getLoginID());
    data.put(Constants.USER_KEY,             identity.getUserKey());
    data.put(Constants.USER_MANAGER_ID,      identity.getManagerLoginID());
    data.put(Constants.USER_PASSWORD,        password);
    data.put(Constants.USER_EMAIL,           userEmailID);
    data.put(Constants.USER_MANAGER_EMAIL,   managerEmailID);
    data.put(Constants.SEND_NOTIFICATION_TO, recipient);
    data.put(Constants.DISPLAY_NAME,         identity.getDisplayName());

    // Setting NON MT User Login ID and Tenant Name in Notification Data to be
    // used in email template
    if (Utils.isMTFriendly()) {
      data.put(Constants.NON_MT_USER_LOGIN, identity.getNonMTUserLoginID());
      data.put(Constants.TENANT_NAME,       identity.getTenantName());
    }
    // set the values in event object
    event.setParams(data);

    // send the notification
    final NotificationService service = service(oracle.iam.notification.api.NotificationService.class);
    try {
      // Look to send notification only if sendNotificationTo has one or more emails or user or manager has email
      if ((recipient != null && !recipient.isEmpty()) || (event.getUserIds() != null && event.getUserIds().length > 0)) {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts the given {@link Identity} to a lightweight {@link UserInfo}
   ** instance.
   **
   ** @param  identity           the {@link Identity} to convert.
   **
   ** @return                    a {@link UserInfo} belonging to the identity.
   */
  private UserInfo convert(final Identity identity) {
    UserInfo user = null;
    if (identity != null) {
      user = new UserInfo();
      user.setLoginID((String)identity.getAttribute(UserManagerConstants.AttributeName.USER_LOGIN.getId()));
      String firstName = (String)identity.getAttribute(UserManagerConstants.AttributeName.FIRSTNAME.getId());
      user.setFirstName((firstName == null) ? new String("") : firstName);

      user.setLastName((String)identity.getAttribute(UserManagerConstants.AttributeName.LASTNAME.getId()));
      user.setUserEmailID((String)identity.getAttribute(UserManagerConstants.AttributeName.EMAIL.getId()));
      user.setUserKey(identity.getAttribute(UserManagerConstants.AttributeName.USER_KEY.getId()).toString());
      user.setDisplayName(oracle.iam.configservice.api.LocaleUtil.getStringValue(identity.getAttribute(UserManagerConstants.AttributeName.DISPLAYNAME.getId())));

      // setting NON MT User Login ID and Tenant Name in Notification Data to be
      // used in Email template
      if (Utils.isMTFriendly()) {
        user.setNonMTUserLoginID(identity.getAttribute(UserManagerConstants.AttributeName.NON_MT_USER_LOGIN.getId()).toString());
        user.setTenantName(identity.getAttribute(UserManagerConstants.AttributeName.TENANT_NAME.getId()).toString());
      }

      Object managerKey = identity.getAttribute(UserManagerConstants.AttributeName.MANAGER_KEY.getId());
      if (managerKey != null) {
        user.setManagerKey(identity.getAttribute(UserManagerConstants.AttributeName.MANAGER_KEY.getId()).toString());
        manager(user);
      }
    }
    return user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manager
  /**
   ** @param  processId          the identifier of the orchestration process.
   ** @param  info               a {@link UserInfo} belonging to the identity.
   */
  private void manager(final UserInfo user) {
    final String method = "manager";
    HashSet<String> attrs = new HashSet<String>();
    attrs.add(UserManagerConstants.AttributeName.EMAIL.getId());
    attrs.add(UserManagerConstants.AttributeName.USER_LOGIN.getId());
    Identity identity = null;
    try {
      final UserManagerInternal facade = service(UserManagerInternal.class);
      identity = facade.getDetails(user.getManagerKey(), attrs, false);
    }
    catch (Exception e) {
      throw eventFailed(e.getCause() == null ? e : e.getCause(), method, "IAM-3040020", oracle.iam.selfservice.self.resources.LRB.DEFAULT);
    }

    if (identity != null) {
      user.setManagerLoginID((String)identity.getAttribute(UserManagerConstants.AttributeName.USER_LOGIN.getId()));
      user.setManagerEmailID((String)identity.getAttribute(UserManagerConstants.AttributeName.EMAIL.getId()));
    }
    else {
      UserNotFoundException e = new UserNotFoundException();
      e.setErrorCode("IAM-3040005");
      throw e;
    }
  }
}