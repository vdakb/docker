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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Access Policy House Keeping

    File        :   AccessPolicyHouseKeeping.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    AccessPolicyHouseKeeping.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.service.health;

import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.Operations.tcAccessPolicyOperationsIntf;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;

import oracle.iam.connectors.icfcommon.ResourceConfig;
import oracle.iam.connectors.icfcommon.ConnectorFactory;
import oracle.iam.connectors.icfcommon.ResourceConfigFactory;

import oracle.iam.connectors.icfcommon.util.TypeUtil;

import oracle.iam.accesspolicy.vo.ChildRecord;
import oracle.iam.accesspolicy.vo.DefaultData;
import oracle.iam.accesspolicy.vo.AccessPolicy;
import oracle.iam.accesspolicy.vo.ChildAttribute;
import oracle.iam.accesspolicy.vo.AccessPolicyElement;

import oracle.iam.accesspolicy.api.AccessPolicyService;

import oracle.iam.accesspolicy.exception.AccessPolicyServiceException;

import static oracle.iam.accesspolicy.utils.Constants.POLICY_KEY;

import oracle.iam.identity.rolemgmt.api.RoleManager;

import oracle.iam.identity.usermgmt.vo.User;

import oracle.iam.identity.usermgmt.api.UserManager;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.USER_LOGIN;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.EventMessage;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractSchedulerTask;

import oracle.iam.identity.foundation.resource.TaskBundle;
import oracle.iam.identity.foundation.resource.EventBundle;

import bka.iam.identity.ProcessError;
import bka.iam.identity.ProcessMessage;
import bka.iam.identity.ProcessException;

import bka.iam.identity.resources.ProcessBundle;

import bka.iam.identity.service.health.type.Factory;
import bka.iam.identity.service.health.type.Descriptor;

////////////////////////////////////////////////////////////////////////////////
// class AccessPolicyHouseKeeping
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AccessPolicyHouseKeeping</code> removes entitlements and
 ** multi-valued attributes that are no longer avalaible in the target system
 ** belonging to access policies.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AccessPolicyHouseKeeping extends AbstractSchedulerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute to advise what is the resource object used by the implementing
   ** scheduled job.
   ** <br>
   ** This attribute is mandatory.
   **/
  private static final String          RECONCILE_OBJECT = "Reconciliation Object";

  /**
   ** Attribute to advise which named IT Resource should be used by the
   ** implementing job instance.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          IT_RESOURCE      = "IT Resource";

  /**
   ** Attribute to advise which on which template the notification to send is
   ** based on.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          TEMPLATE        = "Notification Template";

  /**
   ** Attribute to advise whether the values that not exists anymore are removed
   ** from the access policie.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          CLEANUP         = "Cleanup Policy";

  /** the category of the logging facility to use */
  private static final String          LOGGER_CATEGORY = "BKA.SYSTEM.HEALTH";
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    /** the task attribute IT Resource */
    TaskAttribute.build(IT_RESOURCE,      TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(RECONCILE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute with notification template */
  , TaskAttribute.build(TEMPLATE,         TaskAttribute.MANDATORY)
    /** the task attribute with notification template */
  , TaskAttribute.build(CLEANUP,          TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private AccessPolicyService accessPolicyService;

  private ResourceConfig      resourceConfig;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Handler
  // ~~~~~ ~~~~~~~
  /**
   ** A simple {@link ResultsHandler} to collect a search result in memory.
   */
  private class Handler implements ResultsHandler  {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String  endpoint;
    private final boolean prefix;

    private List<String>  values = new ArrayList<String>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Handler</code> instance that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Handler(final String endpoint, final boolean prefix) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.endpoint = endpoint;
      this.prefix   = prefix;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object           an object returned from the search.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean handle(final ConnectorObject connectorObject) {
      // TODO: Handle MutiValue attributes
      this.values.add(this.prefix ? String.format("%s~%s", this.endpoint, connectorObject.getName().getNameValue()) : connectorObject.getName().getNameValue());
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AccessPolicyHouseKeeping</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccessPolicyHouseKeeping() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconciliationObject
  /**
   ** Returns the name of the resource which is handled by the housekeeping
   ** process.
   **
   ** @return                    the name of the resource which is handled by
   **                            the housekeeping process.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String reconciliationObject() {
    return stringValue(RECONCILE_OBJECT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Returns the name of the IT ressource which is used to clean AP.
   **
   ** @return                    the name of the IT ressource which is used to
   **                            cleanup Access Policies.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceName() {
    return stringValue(IT_RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractReconciliationTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **                            <br>
   **                            Possible object is array of
   **                            {@link TaskAttribute}.
   */
  @Override
  protected TaskAttribute[] attributes() {
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the access policy cleaning task to perform.
   **
   ** @throws TaskException in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final String method = "onExecution";
    info(TaskBundle.format(TaskMessage.SCHEDULE_JOB_STARTED, RECONCILE_OBJECT, getName(), reconciliationObject()));

    final Descriptor resource = Factory.build(resourceName());
    if (resource == null)
      throw new ProcessException(ProcessError.HOUSEKEEPING_DESCRIPTOR_NOTFOUND, resourceName());

    this.accessPolicyService = service(AccessPolicyService.class);

    // get all access policies which are used by the resource object
    final List<AccessPolicy> accessPolicies = getAPfromRessouceObject(reconciliationObject());
    // if the resource object does not belong to an access policy the job is done
    if (CollectionUtility.empty(accessPolicies))
     return;

    // get entitlements on the target system
    final List<String> entitlementsList = search(connectorFacade(), resource);
    // compare entitlements from each access policies and entitlements on the
    // target system. If we found entitlement inside the access policies that
    // not belong on the target system, we remove this entitlement inside the access policy
    for (AccessPolicy cursor : accessPolicies) {
      for (AccessPolicyElement element : cursor.getPolicyElements()) {
        DefaultData       defaultData = element.getDefaultData();
        List<ChildRecord> childData   = defaultData.getChildData();
        List<String>      obsolete    = new ArrayList<String>();
        for (ChildRecord record : childData) {
          for (ChildAttribute attr : record.getAttributes()) {
            obsolete.add(attr.getAttributeValue());
          }
        }
        // compare entitlement on the target system and the access policy
        obsolete.removeAll(entitlementsList);
        // if the list is not empty this means that some non existing
        // entitlements have been found inside this access policy
        if (!obsolete.isEmpty()) {
          warning(method, ProcessBundle.format(ProcessMessage.POLICY_ENTITLEMENT_NOTEXIST, cursor.getName(), obsolete, resourceName()));
          if (booleanValue(CLEANUP)) {
            // removing all entitlements that not exists anymore
            for (ChildRecord childRecord : childData) {
              for (ChildAttribute attr : childRecord.getAttributes()) {
                if (obsolete.contains(attr.getAttributeValue())) {
                  attr.markForDelete();
                }
              }
            }
            try {
              this.accessPolicyService.updateAccessPolicy(cursor);
            }
            catch (Exception e) {
              fatal(method, e);
            }
          }
          // send notification
          try {
            debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
            final NotificationEvent event = cursor.getOwnerType().getID().equalsIgnoreCase(AccessPolicy.OwnerType.ROLE.getID()) ?  createNotificationEvent(null, cursor.getOwnerId(), cursor.getEntityId(), obsolete) : createNotificationEvent(cursor.getOwnerId(), null, cursor.getEntityId(), obsolete);
            debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_SEND));
            sendNotification(event);
          }
          catch (Exception e) {
            // somethings went wrong with notification but it's a non blocking
            // exception
            fatal(method, e);
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    try {
      this.resourceConfig = (ResourceConfig)ResourceConfigFactory.getResourceConfig(resourceName(), reconciliationObject(), null, provider());
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAPfromRessouceObject
  /**
   ** Return <code>Access Policies</code> object list which belong to the
   ** given ressource object.
   **
   ** @param  resourceName       the name of the <code>Resource Object</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link List} of
   **                            <code>Access Policies</code> which belong to
   **                            the given ressource object.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link AccessPolicy}.
   **
   ** @throws TaskException      either the <code>Resource Object</code> does
   **                            not exists for the given name or the population
   **                            of the <code>Access Policies</code> fails.
   */
  protected List<AccessPolicy> getAPfromRessouceObject(final String resourceName)
    throws TaskException {

    final String method = "getAPfromRessouceObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    debug(method, String.format("Searching for access policies belong to %s", resourceName));
    final List<AccessPolicy> collector = new ArrayList<AccessPolicy>();
    tcAccessPolicyOperationsIntf facade    = null;
    try {
      facade = service(tcAccessPolicyOperationsIntf.class);
      final tcResultSet resultSet = facade.getAccessPolicyByResourceName(resourceName);
      for (int j = 0; j < resultSet.getRowCount(); j++) {
        try {
          resultSet.goToRow(j);
          collector.add(this.accessPolicyService.getAccessPolicy(resultSet.getStringValue("Access Policies.Key"), true));
        }
        catch (tcColumnNotFoundException | AccessPolicyServiceException e) {
          fatal(method, e);
        }
      }
    }
    catch (tcAPIException e) {
      throw TaskException.unhandled(e);
    }
    catch (tcObjectNotFoundException e) {
      throw TaskException.general(e);
    }
    finally {
      timerStop(method);
      if (facade != null)
        facade.close();
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: search
  /**
   ** Return the list of the right which belongs to the target system
   **
   ** @param  connectorFacade    the connector facade of the connector.
   **                            <br>
   **                            Allowed object is {@link ConnectorFacade}.
   ** @param  descriptor         the descriptor with the additonal
   **                            <code>IT Resource</code> properties.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   **
   ** @return                    the {@link List} of name from the target system
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  protected List<String> search(final ConnectorFacade connectorFacade, final Descriptor descriptor) {
    final String method = "search";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final List<String> collector = new ArrayList<String>();
    // TODO: how get results without to know if they prefixed
    final Handler handler         = new Handler(this.resourceConfig.getITResource().getKey(), true);
    for (Descriptor.Type cursor : descriptor.type()) {
      collector.addAll(search(connectorFacade, cursor));
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return handler.values;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method: search
  /**
   ** Return the list of the entitlements which belongs to the target system.
   **
   ** @param  connectorFacade    the connector facade of the connector.
   **                            <br>
   **                            Allowed object is {@link ConnectorFacade}.
   ** @param  type               the type descriptor of the entitlement.
   **                            <br>
   **                            Allowed object is {@link Descriptor.Type}.
   **
   ** @return                    the {@link List} of name from the target system
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  protected List<String> search(final ConnectorFacade connectorFacade, final Descriptor.Type type) {
    final String method = "search";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    final Handler     handler = new Handler(this.resourceConfig.getITResource().getKey(), type.prefix());
    final ObjectClass clazz   = TypeUtil.convertObjectType(type.name());
    if (connectorFacade != null) {
      connectorFacade.search(clazz, null, handler, null);
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return handler.values;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorFacade
  /**
   ** Returns the {@link ConnectorFacade} based on configuration map passed.
   **
   ** @return                    the {@link ConnectorFacade} object based on
   **                            Bundle Name, Bundle Version, Connector Name.
   **                            <br>
   **                            Possible object is {@link ConnectorFacade}.
   **
   ** @throws TaskException      if not able to find the {@link ConnectorInfo}
   **                            by the {@link ConnectorKey}.
   */
  private ConnectorFacade connectorFacade()
    throws TaskException {

    final String method = "connectorFacade";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final ConnectorFacade connectorFacade = ConnectorFactory.createConnectorFacade(this.resourceConfig);
    if (connectorFacade == null) {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
      throw new ProcessException(ProcessError.HOUSEKEEPING_CONNECTOR_NOTFOUND, resourceName());
    }

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return connectorFacade;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createNotificationEvent
  /**
   ** This method creates/configure event object corresponding to the event when
   ** entitlement(s) which belong to an access policy has(have) been deleted.
   **
   ** @param  userKey            the identifier of a {@link User} in Identity
   **                            Manager.
   ** @param  roleKey            the roleKey in Identity Manager.
   ** @param  policyKey          the accesspolicyKey in Oracle Identity Manager
   ** @param  entitlements       deleted entitlements
   **
   ** @return                   the {@link NotificationEvent} to send out.
   */
  private NotificationEvent createNotificationEvent(final String userKey, final String roleKey, final String policyKey, final List<String> entitlements) {
    final String method = "createNotificationEvent";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    NotificationEvent event = new NotificationEvent();
    // set user identifier to whom notification is to be sent and set it in the event
    // object being created
    event.setUserIds(createRecipient(userKey, roleKey));
    // set template name to be used to send notification for this event
    event.setTemplateName(stringValue(TEMPLATE));
    // setting senderId as null here hence default sender ID would get picked up
    event.setSender(null);

    // create a map with key value pair for the parameters declared at time of
    // configuring notification event
    final HashMap<String, Object> map = new HashMap<String, Object>();
    map.put(POLICY_KEY,   policyKey);
    // custom parameters
    map.put("endpoint", resourceConfig.getITResource().getName());
    map.put("changed",  entitlements);

    event.setParams(map);
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return event;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRecipient
  /**
   ** This method creates a string array containing list of user IDs to whom
   ** notification is to be sent
   **
   ** @param  userKey        the userkey in Oracle Identity Manager
   ** @param  roleKey        the rolekey in Oracle Identity Manager
   **
   ** @return                a array of strings containing list of user IDs
   **                        to whom notification is to be sent.
   */
  private String[] createRecipient(final String userKey, final String roleKey) {
    final String  method  = "createRecipient";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final UserManager  userManager = service(UserManager.class);
    final RoleManager  roleManager = service(RoleManager.class);
    final List<String> recipient   = new ArrayList<String>();
    try {
      if (userKey != null) {
        Set<String> attributes = new HashSet<String>();
        attributes.add(USER_LOGIN.getId());

        // retrieving User ID
        User identity = userManager.getDetails(userKey, attributes, false);
        // if exists add it to the list of recipients
        recipient.add(identity.getAttribute(USER_LOGIN.getId()).toString());
      }
      if (roleKey != null)  {
        // retrieving User list from roleKey
        List<User> members = roleManager.getRoleMembers(roleKey, true);

        // if exists add user IDs to the list of recipients
        for (User member : members) {
          recipient.add(member.getLogin());
        }
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return recipient.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sendNotification
  /**
   ** Call notification engine passing an event object to it.
   **
   ** @param  event              the {@link NotificationEvent} to pass to the
   **                            engine.
   **
   ** @throws TaskException     if the operations fails in gerneral.
   */
  private void sendNotification(final NotificationEvent event)
    throws TaskException {

    final String method = "sendNotification";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // call notify method of NotificationService to pass on the event to
      // notification engine
      NotificationService facade = service(NotificationService.class);
      facade.notify(event);
    }
    catch (EventException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_FAILED, e);
    }
    catch (UnresolvedNotificationDataException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_UNRESOLVED_DATA, e);
    }
    catch (TemplateNotFoundException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_TEMPLATE_NOTFOUND, e);
    }
    catch (MultipleTemplateException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_TEMPLATE_AMBIGOUS, e);
    }
    catch (NotificationResolverNotFoundException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_RESOLVER_NOTFOUND, e);
    }
    catch (UserDetailsNotFoundException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_IDENTITY_NOTFOUND, e);
    }
    catch (NotificationException e) {
      throw new ProcessException(ProcessError.NOTIFICATION_EXCEPTION, e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}