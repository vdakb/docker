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
    Subsystem   :   Identity Manager Health Reporting

    File        :   IncidentDigestObserver.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IncidentDigestObserver.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.service.health;

import java.util.List;
import java.util.HashMap;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.dataaccess.tcDataSetException;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import oracle.iam.platform.authopss.vo.AdminRole;
import oracle.iam.platform.authopss.vo.AdminRoleVO;
import oracle.iam.platform.authopss.api.AdminRoleService;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.notification.vo.NotificationEvent;

import oracle.iam.notification.api.NotificationService;

import oracle.iam.notification.exception.EventException;
import oracle.iam.notification.exception.NotificationException;
import oracle.iam.notification.exception.MultipleTemplateException;
import oracle.iam.notification.exception.TemplateNotFoundException;
import oracle.iam.notification.exception.UserDetailsNotFoundException;
import oracle.iam.notification.exception.UnresolvedNotificationDataException;
import oracle.iam.notification.exception.NotificationResolverNotFoundException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.EventMessage;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.resource.EventBundle;

import oracle.iam.identity.foundation.AbstractSchedulerTask;

import bka.iam.identity.ProcessError;

import bka.iam.identity.ProcessException;

import bka.iam.identity.resources.ProcessBundle;

////////////////////////////////////////////////////////////////////////////////
// class IncidentDigestObserver
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>IncidentDigestObserver</code> verifies specific metrics that are
 ** important to be solved by an administratorin Identity Manager and sending out
 ** information about it.
 ** <br>
 ** The metrics observed are:
 ** <ol>
 **   <li>Amount of Open Tasks
 **   <li>Amount of Scheduler Jobs where leates execution have an exception.
 **   <li>Amount of Failed Reconciliation Events
 **     <ul>
 **       <li>Amount of Reconciliation Events in state No user match found.
 **       <li>Amount of Reconciliation Events in state Data Validation Failed.
 **     </ul>
 **   </li>
 **   <li>Amount of failed Orchestration Processes.
 ** </ol>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class IncidentDigestObserver extends AbstractSchedulerTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The default name of the Admin Role to whose members the notification is
   ** sent.
   */
  private static final String          ADMINROLE_DEFAULT = "OrclOIMSystemAdministrator";

  /**
   ** Attribute to advise which on which template the notification to send is
   ** based on.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          TEMPLATE          = "Notification Template";

  /**
   ** Attribute to advise the period (in days) to be considered retrospectively. 
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          PERIOD            = "Reporting Period";

  /**
   ** Attribute to advise to whom the notifictaion have to be sent.
   ** <br>
   ** The value of the attribute is the name of an Admin Role that ususally it
   ** is <code>OrclOIMSystemAdministrator</code>.
   ** <br>
   ** This attribute is optional.
   */
  private static final String          ADMINROLE         = "Admin Role";

  /**
   ** Attribute to advise that the amount of <code>Open Task</code> has to be
   ** reported.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          OPENTASK          = "Open Tasks";

  /**
   ** Attribute to advise that the amount of <code>Reconciliation Event</code>s
   ** has to be reported that are in state <code>No User Match Found</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          USERMATCH         = "No User Match Found";

  /**
   ** Attribute to advise that the amount of <code>Reconciliation Event</code>s
   ** has to be reported that are in state <code>Data Validation Failed</code>.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          DATAVALIDATION    = "Data Validation Failed";

  /**
   ** Attribute to advise that the amount of <code>Job</code>s has to be
   ** reported that are stopped with exception.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          JOBFAILED         = "Failed Jobs";

  /**
   ** Attribute to advise that the amount of <code>Job</code>s has to be
   ** reported that are stopped with exception.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          JOBINTERRUPTED    = "Interrupted Jobs";

  /**
   ** Attribute to advise that the amount of
   ** <code>Orchestration Process</code>es has to be reported that are failed.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          ORCHESTRATION     = "Orchestration Process";

  /** the category of the logging facility to use */
  private static final String          LOGGER_CATEGORY   = "BKA.SYSTEM.HEALTH";

  /** the mapping key of the gathered metrics in the Notification Event */
  private static final String          PARAMETER_METRIC  = "metric";

  /** the period (in days) to be considered retrospectively */
  private static final String          PARAMETER_PERIOD  = "period";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    /** the task attribute with notification template */
    TaskAttribute.build(TEMPLATE,       TaskAttribute.MANDATORY)
    /**
     ** the period (in days) to be considered retrospectively for reporting
     ** purpose
     */
  , TaskAttribute.build(PERIOD,         TaskAttribute.MANDATORY)
    /** the amount of <code>Open Task</code> has to be reported */
  , TaskAttribute.build(OPENTASK,       TaskAttribute.MANDATORY)
    /**
     ** the amount of <code>Reconciliation Event</code>s has to be reported that
     ** are in state <code>No User Match Found</code>
     */
  , TaskAttribute.build(USERMATCH,      TaskAttribute.MANDATORY)
    /**
     ** whether the amount of <code>Reconciliation Event</code>s has to be
     ** reported that are in state <code>Data Validation Failed</code>
     */
  , TaskAttribute.build(DATAVALIDATION, TaskAttribute.MANDATORY)
    /**
     ** whether the amount of <code>Jobs</code>s has to be reported that are in
     ** state <code>failed</code>
     */
  , TaskAttribute.build(JOBFAILED,      TaskAttribute.MANDATORY)
    /**
     ** whether the amount of <code>Jobs</code>s has to be reported that are in
     ** state <code>interrupted</code>
     */
  , TaskAttribute.build(JOBINTERRUPTED, TaskAttribute.MANDATORY)
    /**
     ** whether the amount of <code>Orchestration</code>s has to be reported
     ** that are in state <code>failed</code>
     */
  , TaskAttribute.build(ORCHESTRATION,  TaskAttribute.MANDATORY)
    /** the name of the Admin Role to whose members the notification is sent. */
  , TaskAttribute.build(ADMINROLE,      ADMINROLE_DEFAULT)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes attributes
  //////////////////////////////////////////////////////////////////////////////

  final HashMap<String, Object> metric = new HashMap<String, Object>();;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IncidentDigestObserver</code> scheduler job
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IncidentDigestObserver() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
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
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    if (booleanValue(OPENTASK, true))
      openTask();
    if (booleanValue(USERMATCH, true))
      nouserMatchFound();
    if (booleanValue(DATAVALIDATION, true))
      dataValidationFailed();
    if (booleanValue(JOBFAILED, true))
      failedJob();
    if (booleanValue(JOBINTERRUPTED, true))
      interruptedJob();
    if (booleanValue(ORCHESTRATION, true))
      failedOrchestration();

    // verify if we got any result
    if (this.metric.size() > 0) {
      final List<String> recipient = createRecipient(stringValue(ADMINROLE));
      if (recipient != null || recipient.size() > 0) {
        debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_CREATE));
        final NotificationEvent event = new NotificationEvent();
        // set template name to be used to send notification for this event
        event.setTemplateName(stringValue(TEMPLATE));
        // setting senderId as null here hence default sender ID would get
        // picked up
        event.setSender(null);
        // assign the map with key value pair for the parameters declared at
        // time of configuring notification event
        final HashMap<String, Object> data = new HashMap<String, Object>();
        data.put(PARAMETER_PERIOD, new Integer(integerValue(PERIOD)));
        data.put(PARAMETER_METRIC, this.metric);
        event.setParams(data);
        // send e-mail per recipient
        for (String cursor : recipient) {
          final String[] internal = { cursor };
          event.setUserKeys(internal);
          // push the user key to the data map so the resolver is able to pick
          // up the personal data of the identity to notify
          data.put(UserManagerConstants.USER_ENTITY, cursor);
          // send notification
          try {
            debug(method, EventBundle.string(EventMessage.NOTIFICATION_EVENT_SEND));
            sendNotification(event);
          }
          catch (Exception e) {
            // something went wrong with notification but it's a non blocking
            // exception
            fatal(method, e);
          }
        }
      }
      else {
        error(method, ProcessBundle.string(ProcessError.NOTIFICATION_RECIPIENT_EMPTY));
      }
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   openTask
  /**
   ** Determines the amount of open task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void openTask()
    throws TaskException  {

    final String method = "openTask";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM oti WHERE sch_status = ? AND TRUNC(sch_actual_start) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "R");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nouserMatchFound
  /**
   ** Determines the amount of <code>Reconciliation Event</code>s thar are in
   ** state of <code>No User Match Found</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void nouserMatchFound()
    throws TaskException  {

    final String method = "nouserMatchFound";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    
    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM recon_events WHERE re_status = ? AND TRUNC(re_create) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "No User Match Found");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataValidationFailed
  /**
   ** Determines the amount of <code>Reconciliation Event</code>s thar are in
   ** state of <code>Data Validation Failed</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void dataValidationFailed()
    throws TaskException  {

    final String method = "dataValidationFailed";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM recon_events WHERE re_status = ? AND TRUNC(re_create) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "Data Validation Failed");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failedJob
  /**
   ** Determines the amount of <code>Job</code>s thar are in stopped with
   ** exception.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void failedJob()
    throws TaskException  {

    final String method = "failedJob";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM job_history WHERE status = ? AND TRUNC(job_start_time) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "6");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interruptedJob
  /**
   ** Determines the amount of <code>Job</code>s thar are interrupted.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void interruptedJob()
    throws TaskException  {

    final String method = "interruptedJob";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM job_history WHERE status = ? AND TRUNC(job_start_time) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "7");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failedOrchestration
  /**
   ** Determines the amount of <code>Job</code>s thar are in stopped with
   ** excpetion.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected void failedOrchestration()
    throws TaskException  {

    final String method = "failedOrchestration";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final PreparedStatementUtil statement = new PreparedStatementUtil();
    statement.setStatement(provider(), "SELECT count(*) as \"amount\" FROM orchprocess WHERE status = ? AND TRUNC(createdon) BETWEEN TRUNC(sysdate) - ? AND TRUNC(sysdate)");
    statement.setString(1, "FAILED");
    statement.setInt(2,    integerValue(PERIOD));
    try {
      statement.execute();
      final tcDataSet dataSet = statement.getDataSet();
      dataSet.goToRow(0);
      this.metric.put(method, dataSet.getString("amount"));
    }
    catch (tcDataAccessException e) {
      throw TaskException.general(e);
    }
    catch (tcDataSetException e) {
      throw TaskException.abort(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRecipient
  /**
   ** This method creates a {@link List}} of strings containing the user login
   ** names of identities to whom notification is to be sent.
   ** <b>Note</b>:
   ** <br>
   ** The super user of Identity Manager is filtered out form the list of
   ** recipients.
   **
   ** @param  adminRole          the name if the <code>Admin Role</code> in
   **                            Identity Manager containing the recipeints.
   **
   ** @return                    a {@link List}} of strings containing the user
   **                            login names of identities to whom notification
   **                            is to be sent.
   */
  private List<String> createRecipient(final String adminRole) {
    final String  method  = "createRecipient";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    List<String> member = null;
    final AdminRoleService service = service(AdminRoleService.class);
    try {
      if (adminRole != null)  {
        // obtain admin role by name
        final AdminRole   role   = service.getAdminRole(adminRole);
        final AdminRoleVO detail = service.getAdminRoleVO(String.valueOf(role.getRoleId()));
        member = detail.getUserAssignedStatic();
        // remove any system user due to they don't have almost a e-mail address
        member.remove("1"); // xelsysadm
        member.remove("2"); // xeloperator
        member.remove("3"); // weblogic
        member.remove("4"); // oiminternal
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return member;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sendNotification
  /**
   ** Call notification engine passing an event object to it.
   **
   ** @param  event              the {@link NotificationEvent} to pass to the
   **                            engine.
   **
   ** @throws TaskException      if the operations fails in gerneral.
   */
  private void sendNotification(final NotificationEvent event)
    throws TaskException {

    final String method = "sendNotification";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    try {
      // call notify method of NotificationService to pass on the event to
      // notification engine
      service(NotificationService.class).notify(event);
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