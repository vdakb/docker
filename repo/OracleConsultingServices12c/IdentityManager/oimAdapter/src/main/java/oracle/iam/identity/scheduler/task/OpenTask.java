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

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Common Scheduler Operations

    File        :   OpenTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    OpenTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-06  TSebo    First release version
*/
package oracle.iam.identity.scheduler.task;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcAttributeNotFoundException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Operations.tcProvisioningOperationsIntf;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.AbstractSchedulerTask;
import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.scheduler.SchedulerBundle;
import oracle.iam.identity.scheduler.SchedulerMessage;
import oracle.iam.identity.scheduler.type.TaskDetail;
import oracle.iam.platform.Platform;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.vo.Request;
import oracle.iam.selfservice.exception.UserLookupException;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;

////////////////////////////////////////////////////////////////////////////////
// class OpenTask
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>OpenTask</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 ** <p>
 ** The task can exetufe following operation on open tasks:
 ** <ul>
 **  <li> R  - Retry</li>
 **  <li> MC - Manual Complete</li>
 **  <li> UC - Unsucesfull Complete</li>
 **  <li> X -  Cancel</li>
 ** </ul>
 ** <p>
 ** Open tasks can be filtered based on the:
 ** <ul>
 **  <li>Task ID</li>
 **  <li>Task Name</li>
 **  <li>Task Status</li>
 **  <li>Task Reason</li>
 **  <li>Task Note</li>
 **  <li>Appliaction Instance Name</li>
 **  <li>Appliaction Instance Type  (Online|Offline)</li>
 **  <li>Task Beneficiary</li>
 **  <li>Start Date From</li>
 **  <li>Start Date To</li>
 **  <li>Request ID</li>
 ** </ul>
 ** <p>
 ** In the Justification parameter can be provided information which is put on the task Note when the task is processed.
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 */
public class OpenTask extends AbstractSchedulerTask {


  // Date format
  private static final String DATE_FORMAT = "DD.MM.YYYY";

  // Scheduled tasks input parameters
  private static final String PARAM_SCHEDULE_TASK_NAME = "Schedule Task Name";
  private static final String PARAM_OPERAION = "Operation"; // R|MC|UC|X
  private static final String PARAM_BATCH_SIZE = "Batch Size";
  private static final String PARAM_SLEEP_TIME = "Sleep Time";
  private static final String PARAM_TAKS_ID = "Task ID";
  private static final String PARAM_TASK_NAME = "Task Name";
  private static final String PARAM_TASK_STATUS = "Task Status"; //Rejected|Pending
  private static final String PARAM_APPLICATION_NAME = "Application Name";
  private static final String PARAM_APPLICATION_TYPE = "Application Type";
  private static final String PARAM_BENEFICIARY = "Beneficiary";
  private static final String PARAM_START_DATE_FROM = "Start Date From"; // Format DD.MM.YYYY
  private static final String PARAM_START_DATE_TO = "Start Date To"; // Format DD.MM.YYYY
  private static final String PARAM_TASK_REASON = "Task Reason";
  private static final String PARAM_TASK_NOTE = "Task Note";
  private static final String PARAM_REQUEST_ID = "Request ID";
  private static final String PARAM_JUSTIFICATION = "Justification";

  // OIM Job Name
  protected String jobName;

  // Execution parameters
  private int batchSize;
  private int sleepTime;
  private String operation;

  // Filter parameters
  private String taskID;
  private String taskName;
  private String taskStatus;
  private String applicationName;
  private String applicationType;
  private String beneficiaryLogin;
  private String taskReason;
  private String taskNote;
  private String fromDate;
  private String toDate;
  private String requestKey;

  // Justifcation - why was open task proccessed
  private String justification;

  // SQL query which give us all needed data regarding OpenTasks from OIM DB
  private static final String SQL_PROVISIONING_TASKS =
    "SELECT SCH.SCH_KEY, APP_INSTANCE.APP_INSTANCE_NAME, MIL.MIL_NAME, " 
    + "       SCH.SCH_STATUS, USR.USR_LOGIN, OSI.OSI_ASSIGNED_DATE, " 
    + "       SCH.SCH_ACTUAL_START,SCH.SCH_ROWVER, " 
    + "       SCH.SCH_NOTE, SCH.SCH_REASON, OSI.REQUEST_KEY " 
    + "FROM OTI " 
    + "JOIN SCH ON OTI.SCH_KEY = SCH.SCH_KEY " 
    + "JOIN OSI ON SCH.SCH_KEY=OSI.SCH_KEY " 
    + "JOIN MIL ON OSI.MIL_KEY=MIL.MIL_KEY " 
    + "JOIN ORC ON ORC.ORC_KEY=OSI.ORC_KEY " 
    + "JOIN OIU ON OIU.ORC_KEY=ORC.ORC_KEY " 
    + "JOIN USR ON OIU.USR_KEY=USR.USR_KEY " 
    + "JOIN APP_INSTANCE ON OIU.APP_INSTANCE_KEY = APP_INSTANCE.APP_INSTANCE_KEY";


  /** the category of the logging facility to use */
  private static final String LOGGER_CATEGORY = "OCS.SCHEDULER.TASK";


  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   **/
  private static final TaskAttribute[] attributes = {
    /** Schedule Task Name */
    TaskAttribute.build(PARAM_SCHEDULE_TASK_NAME, TaskAttribute.MANDATORY)
    /**
     ** Type of operation executed on open tasks R|MC|UC|X
     **/
    , TaskAttribute.build(PARAM_OPERAION, TaskAttribute.MANDATORY)
    /**
     ** How many provisioing tasks are processed in one batch
     **/
    , TaskAttribute.build(PARAM_BATCH_SIZE, TaskAttribute.MANDATORY)
    /**
     ** How many second with job wait to take next batch [s]
     */
    , TaskAttribute.build(PARAM_SLEEP_TIME, TaskAttribute.MANDATORY)
    /**
     ** Filter open task based on: Provisioning task key (SCH_KEY)
     **/
    , TaskAttribute.build(PARAM_TAKS_ID, TaskAttribute.OPTIONAL)
    /**
     ** Filter open task based on: Name of provisioning task (MIL_NAME)
     **/
    , TaskAttribute.build(PARAM_TASK_NAME, TaskAttribute.OPTIONAL)
    /**
     ** Filter open task based on: Status of provisioning task Rejected|Pending
     **/
    , TaskAttribute.build(PARAM_TASK_STATUS, TaskAttribute.MANDATORY)
    /**
     ** Filter open task based on: Application Instance Name
     **/
    , TaskAttribute.build(PARAM_APPLICATION_NAME, TaskAttribute.OPTIONAL)
    /**
     ** Filter open task based on: Application Instance Type: Online|Offline
     **/
    , TaskAttribute.build(PARAM_APPLICATION_TYPE, TaskAttribute.MANDATORY)
    /**
    ** Filter open task based on: Beneficiary Login name
    **/
    , TaskAttribute.build(PARAM_BENEFICIARY, TaskAttribute.OPTIONAL)
    /**
    ** Filter open task based on: Start Date From in format DD.MM.YYYY
    **/
    , TaskAttribute.build(PARAM_START_DATE_FROM, TaskAttribute.OPTIONAL)
    /**
    ** Filter open task based on: Start Date To in format DD.MM.YYYY
    **/
    , TaskAttribute.build(PARAM_START_DATE_TO, TaskAttribute.OPTIONAL)
    /**
    ** Filter open task based on: Provisioning Task Reason
    **/
    , TaskAttribute.build(PARAM_TASK_REASON, TaskAttribute.OPTIONAL)
    /**
    ** Filter open task based on: Provisioning Task Task Note
    **/
    , TaskAttribute.build(PARAM_TASK_NOTE, TaskAttribute.OPTIONAL)
    /**
    ** Filter open task based on: Request ID
    **/
    , TaskAttribute.build(PARAM_REQUEST_ID, TaskAttribute.OPTIONAL)
    /**
    ** Justification text put on the task note
    **/
    , TaskAttribute.build(PARAM_JUSTIFICATION, TaskAttribute.OPTIONAL)
  };

   //////////////////////////////////////////////////////////////////////////////
   // Method:   Ctor
  /**
  ** Constructs an empty <code>OpenTask</code> task
  ** that allows use as a JavaBean.
  ** <br>
  ** Zero argument constructor required by the framework.
  ** <br>
  ** Default Constructor
  */
  public OpenTask() {
    super(LOGGER_CATEGORY);
  }

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

  /**
   * Check and parse OIM input parameters
   * @throws TaskException exeption is thrown in case input parameters are not valid
   */
  @Override
  protected void initialize() throws TaskException {
    super.initialize();
    final String method = "initialize";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    // Formatter used to validate if the DATE parameter use corrent data format
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    this.jobName = stringValue(PARAM_SCHEDULE_TASK_NAME);

    this.batchSize = integerValue(PARAM_BATCH_SIZE);
    this.sleepTime = integerValue(PARAM_SLEEP_TIME);

    // Validate Operation parameter is must be one of the following values
    // R|MC|UC|X
    this.operation = stringValue(PARAM_OPERAION);
    if ("R".equals(operation) || "MC".equals(operation) || "UC".equals(operation) || "X".equals(operation)) {
      debug(method,"Parameter " + PARAM_OPERAION + " has valid value: " + operation);
    } else {
      String[] params = {this.jobName, PARAM_OPERAION, operation};
      throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.OPERATION_MISMATCH,params);
    }

    // Validate Task Status parameter, is must be one of the following values
    // Rejected|Pending
    String status = stringValue(PARAM_TASK_STATUS);
    if ("Rejected".equals(status)) {
      this.taskStatus = "R";
      debug(method,"Parameter " + PARAM_TASK_STATUS + " has valid value: " + status);
    } else if ("Pending".equals(status)) {
      this.taskStatus = "P";
      debug(method,"Parameter " + PARAM_TASK_STATUS + " has valid value: " + status);
    } else {   
      String[] params = {this.jobName, PARAM_TASK_STATUS, status};
  //      String errorMessage = SchedulerBundle.format(SchedulerMessage.STATUS_MISMATCH,params);
      throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.STATUS_MISMATCH,params);
    }

    this.taskID          = stringValue(PARAM_TAKS_ID);
    this.taskName        = stringValue(PARAM_TASK_NAME);
    this.applicationName = stringValue(PARAM_APPLICATION_NAME);

    // Validate Application Type valid values are Online|Offline
    String appType = stringValue(PARAM_APPLICATION_TYPE);
    if (appType != null && appType.trim().length() > 0) {
      if ("Online".equalsIgnoreCase(appType)) {
        this.applicationType = "DOBBased";
      } else if ("Offline".equalsIgnoreCase(appType)) {
        this.applicationType = "Disconnected";
      } else {
        String[] params = {this.jobName, PARAM_APPLICATION_TYPE, appType};
        throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.APPTYPE_MISMATCH,params);
      }
    }

    this.beneficiaryLogin = stringValue(PARAM_BENEFICIARY);
    this.taskReason       = stringValue(PARAM_TASK_REASON);
    this.taskNote         = stringValue(PARAM_TASK_NOTE);
    this.requestKey       = stringValue(PARAM_REQUEST_ID);

    // Validate if the fromData has correct format
    String from = stringValue(PARAM_START_DATE_FROM);
    if (from != null && from.trim().length() > 0) {
      try {
        formatter.parse(from);
        this.fromDate = from;
        debug(method,"Parameter " + PARAM_START_DATE_FROM + " has valid value: " + from);
      } catch (ParseException e) {
        String[] params = {this.jobName, PARAM_START_DATE_FROM, from};
        throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.DATE_MISMATCH,params);
      }
    }

    // Validate if the toDate has correct format
    String to = stringValue(PARAM_START_DATE_TO);
    if (to != null && to.trim().length() > 0) {
      try {
        formatter.parse(to);
        this.toDate = to;
        debug(method,"Parameter " + PARAM_START_DATE_TO + " has valid value: " + to);
      } catch (ParseException e) {
        String[] params = {this.jobName, PARAM_START_DATE_TO, to};
        throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.DATE_MISMATCH,params);
      }
    }

    // Justification message added on task note
    this.justification = stringValue(PARAM_JUSTIFICATION);

    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }




  @Override
  protected void onExecution() throws TaskException {
    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    Thor.API.Operations.tcProvisioningOperationsIntf tcProvService =
      Platform.getService(Thor.API.Operations.tcProvisioningOperationsIntf.class);

    try {
      // Find all open taks based on input parameters
      List<TaskDetail> openTasks = findRejectedOpenTasks();
      debug(method,"OIM Scheduled job: \""+this.jobName+"\" number of identified open tasks: "+openTasks);

      String userName = Platform.getService(AuthenticatedSelfService.class)
                                .getProfileDetails(null)
                                .getLogin();

      if ("R".equals(this.operation)) {
        // Retry open tasks
        retry(tcProvService, openTasks, justification, userName, batchSize, sleepTime);
      } else if ("MC".equals(this.operation)) {
        // Manual complete
        manuallyComplete(tcProvService, openTasks, justification, userName, batchSize, sleepTime);
      } else if ("UC".equals(this.operation)) {
        // Unsuccesfully completed
        setStatus(tcProvService, openTasks, "UC", justification, userName, batchSize, sleepTime);
      } else if ("X".equals(this.operation)) {
        // Cancel task
        setStatus(tcProvService, openTasks, "X", justification, userName, batchSize, sleepTime);
      }


    } catch (SQLException e) {
      String[] params = {this.jobName, e.getMessage()};
      throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.SQL_EXEPTION,params);
    } catch (UserLookupException e) {
      String[] params = {this.jobName, e.getMessage()};
      throw new TaskException(SchedulerBundle.RESOURCE, SchedulerMessage.USER_LOOKUP_EXEPTION,params);
    }


    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }


  

  /**
   * Find all open tasks based on the input parameters
   * @return found open tasks based on filter parameters
   * @throws SQLException in case there is a problem with DB
   */
  public List<TaskDetail> findRejectedOpenTasks() throws SQLException {
    final String method = "findRejectedOpenTasks";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    
    List<TaskDetail> tasksDetails = new ArrayList<TaskDetail>();

    // Create filter for open taks
    StringBuffer filter = new StringBuffer(" WHERE 1=1");
    if (taskID != null && this.taskID.trim().length() > 0) {
      filter.append(" AND SCH.SCH_KEY=").append(taskID).append(" ");
    }
    if (taskName != null && taskName.trim().length() > 0) {
      filter.append(" AND MIL.MIL_NAME='").append(taskName).append("' ");
    }
    if (taskStatus != null && taskStatus.trim().length() > 0) {
      filter.append(" AND SCH.SCH_STATUS='").append(taskStatus).append("' ");
    }
    if (applicationName != null && applicationName.trim().length() > 0) {
      filter.append(" AND APP_INSTANCE.APP_INSTANCE_NAME='").append(applicationName).append("' ");
    }
    if (applicationType != null && applicationType.trim().length() > 0) {
      filter.append(" AND APP_INSTANCE.APP_INSTANCE_TYPE='").append(applicationType).append("' ");
    }
    if (beneficiaryLogin != null && beneficiaryLogin.trim().length() > 0) {
      filter.append(" AND USR.USR_LOGIN='").append(beneficiaryLogin).append("' ");
    }
    if (taskReason != null && taskReason.trim().length() > 0) {
      filter.append(" AND REGEXP_LIKE(SCH.SCH_REASON,'").append(taskReason).append("') ");
    }
    if (taskNote != null && taskNote.trim().length() > 0) {
      filter.append(" AND REGEXP_LIKE(SCH.SCH_NOTE,'").append(taskNote).append("') ");
    }
    if (fromDate != null && fromDate.trim().length() > 0) {
      filter.append(" AND OSI.OSI_ASSIGNED_DATE > to_date('").append(fromDate).append("','").append(DATE_FORMAT).append("') ");
    }
    if (toDate != null && toDate.trim().length() > 0) {
      filter.append(" AND OSI.OSI_ASSIGNED_DATE < to_date('").append(toDate).append("','").append(DATE_FORMAT).append("') ");
    }
    if (requestKey != null && requestKey.trim().length() > 0) {
      filter.append(" AND OSI.REQUEST_KEY='").append(requestKey).append("' ");
    }

    String sql = OpenTask.SQL_PROVISIONING_TASKS + filter.toString();
    debug(method,"OIM Scheduled job: " + this.jobName + " SQL:" + sql);

    Date startDate = new Date();
    DataSource oimDS = null;
    Connection oim = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      oimDS = Platform.getOperationalDS();
      oim = oimDS.getConnection();
      stmt = oim.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        tasksDetails.add(new TaskDetail(rs.getLong("SCH_KEY"), rs.getString("APP_INSTANCE_NAME"),
                                        rs.getString("MIL_NAME"), rs.getString("SCH_STATUS"), rs.getString("USR_LOGIN"),
                                        rs.getBytes("SCH_ROWVER"), rs.getDate("OSI_ASSIGNED_DATE"),
                                        rs.getDate("SCH_ACTUAL_START"),
                                        (rs.getClob("SCH_NOTE") == null ? null :
                                         rs.getClob("SCH_NOTE").getSubString(1, (int) rs.getClob("SCH_NOTE").length())),
                                        rs.getString("SCH_REASON"), rs.getString("REQUEST_KEY")));
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (oim != null) {
        oim.close();
      }
    }
    Date endDate = new Date();
    info("OIM Scheduled job: \"" + this.jobName + "\" execution of SQL took " + 
         (endDate.getTime() - startDate.getTime()) + " [ms], " + tasksDetails.size() + " provisioning tasks has been returned");
    
    
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);

    return tasksDetails;
  }


  /**
   * Provide more informations to TaskDetail value holder.
   * Task_Reason,
   * Task_Note,
   * Requst_key
   * Request_justification,
   * Request_FailuerReason
   * @param tcProvService OIM Provisioning service
   * @param reqService  OIM Request service
   * @param taskDetail curretnly obtained information about task 
   * @param status filter task based on the statuses
   * @return All information about task in object TaskDetail
   * @throws tcAPIException Generic OIM API exception
   * @throws tcColumnNotFoundException Column not found
   * @throws tcUserNotFoundException  User not found
   * @throws tcAttributeNotFoundException Attribute not found on task
   */
  protected TaskDetail populateTaskDetails(Thor.API.Operations.tcProvisioningOperationsIntf tcProvService,
                                           RequestService reqService, 
                                           TaskDetail taskDetail,
                                           String[] status) throws tcAPIException, tcAPIException,
                                                                   tcColumnNotFoundException, tcUserNotFoundException,
                                                                   tcAttributeNotFoundException {

    final String method = "populateTaskDetails";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    Thor.API.tcResultSet taskDetails;
    // Get Note and reason
    Date startDetailsDate = new Date();
    taskDetails = tcProvService.getProvisioningTaskDetails(taskDetail.getProcessTaskKey());
    if (taskDetails != null && taskDetails.getRowCount() > 0) {
      taskDetails.goToRow(0);
      //this.printResultSet(taskDetails);
      taskDetail.setNote(taskDetails.getStringValue("Process Instance.Task Details.Note"));
      taskDetail.setReason(taskDetails.getStringValue("Process Instance.Task Details.Reason"));

      taskDetail.setCreationDate(taskDetails.getDate("Process Instance.Task Details.Creation Date"));
      taskDetail.setStartDate(taskDetails.getDate("Process Instance.Task Details.Actual Start Date"));
      taskDetail.setTaskName(taskDetails.getStringValue("Process Definition.Tasks.Task Name"));
      String taskStatus = taskDetails.getStringValue("Process Instance.Task Details.Status");
      taskDetail.setStatus(taskStatus);
      taskDetail.setVersion(taskDetails.getByteArrayValue("Process Instance.Task Details.Row Version"));

      Date endDetailsDate = new Date();
      Date startAssignedDate = new Date();

      // Get RequestKey
      Map<String, String> filter = new HashMap<String, String>();
      filter.put("Process Instance.Task Details.Key", "" + taskDetail.getProcessTaskKey());
      taskDetails = tcProvService.getAssignedProvisioningTasks(1, filter, status);
      if (taskDetails != null && taskDetails.getRowCount() > 0) {
        taskDetails.goToRow(0);
        //printResultSet(taskDetails);
        taskDetail.setRequestKey(taskDetails.getStringValue("REQ_KEY"));
        taskDetail.setAccountName(taskDetails.getStringValue("Objects.Name"));
        taskDetail.setBeneficiary(taskDetails.getStringValue("Process Instance.Task Information.Target User"));
      } else {
        warning(" populateTaskDetails -> Provisioning Task Key [" + taskDetail.getProcessTaskKey() +
                "] exist in OIM, but is in status " + taskStatus + ". Expected statuses: " + Arrays.toString(status));
        timerStop(method);
        trace(method, SystemMessage.METHOD_EXIT);
        return null;
      }
      Date endAssignedDate = new Date();

      // Get additional data in case REQUEST_KEY is presented
      long getBasicRequestDataDuration = 0;
      if (taskDetail.getRequestKey() != null && taskDetail.getRequestKey().length() > 0) {
        try {
          Date startRequest = new Date();
          Request request = reqService.getBasicRequestData(taskDetail.getRequestKey());
          taskDetail.setRequestJustification(request.getJustification());
          taskDetail.setRequestFailureReason(request.getReasonForFailure());
          Date endRequest = new Date();
          getBasicRequestDataDuration = endRequest.getTime() - startRequest.getTime();
        } catch (Exception e) {
          fatal("RequestService has failed with error: " + e, e);
        }
      }
      debug(method,
            "populateTaskDetails -> Provisioning Task Key [" + taskDetail.getProcessTaskKey() +
            "] Duration of getProvisioningTaskDetails: " + (endDetailsDate.getTime() - startDetailsDate.getTime()) +
            " [ms]," + " getAssignedProvisioningTasks: " + (endAssignedDate.getTime() - startAssignedDate.getTime()) +
            " [ms]" +
            (getBasicRequestDataDuration > 0 ? ", getBasicRequestData: " + getBasicRequestDataDuration + " [ms] " :
             ""));
    } else {
      warning("populateTaskDetails -> Provisioning Task Key [" + taskDetail.getProcessTaskKey() +
              "] doesn't exist in OIM");
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
      return null;
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return taskDetail;
  }


  /**
   * Set new status of the provisioning tasks
   * @param tcProvService OIM provisioning service API
   * @param tasks List of provisioning tasks
   * @param status Status code which should be one from the following list [C,MC,P,PX,R,S,UC,W,X]
   * @param justification Operation justification. This information is addeed to task note.
   * @param userName Logged in user. This information is addeed to task note.
   * @param batchSize Number of processed tasks in one batch.
   * @param sleepTime Sleep time between batches in seconds
   */
  protected void setStatus(Thor.API.Operations.tcProvisioningOperationsIntf tcProvService, final List<TaskDetail> tasks,
                           final String status, final String justification, final String userName, long batchSize,
                           long sleepTime) {

    final String method = "setStatus";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    int success = 0;
    int fail = 0;
    int counter = 0;
    info("=============== OPEN TASKS - SET STATUS ==================================================");
    for (TaskDetail task : tasks) {
      try {
        // Operation on open tasks is executed in batches
        if (batchSize > 0 && sleepTime > 0 && counter == batchSize) {
          debug(method, "Entering to sleeping mode for: " + sleepTime + " [s]");
          Thread.sleep(sleepTime * 1000);
          counter = 0;
        }

        // Check if status can be set
        if (isStatusAvailable(tcProvService, task.getProcessTaskKey(),
                              status)) {
          // Set status
          String note =
            "----- Added by " + (userName == null ? "" : userName.toUpperCase()) + " on " + new Date() + " -----\n";
          note += "Operation: Set Status to " + status + "\n";
          note += "Justification: " + justification + "\n";


          Map<String, String> data = new HashMap<String, String>();
          data.put("Process Instance.Task Details.Note",
                   task.getNote() == null ? note : task.getNote() + "\n\n" + note);
          data.put("Process Instance.Task Details.Status", status);
          tcProvService.updateTask(task.getProcessTaskKey(), task.getVersion(), data);
          info("setStatus -> Provisioning Task Key [" + task.getProcessTaskKey() + "] is set to status [" + status +"]");
          success++;
        } else {
          warning("setStatus -> Provisioning Task Key  [" + task.getProcessTaskKey() + "], status [" + status + "], is not available");
          fail++;
        }
      } catch (Exception e) {
        fail++;
        fatal("setStatus -> Provisioning task Key [" + task.getProcessTaskKey() + "],  wasn't not set to status [" + status + "] due: " + e.getMessage(), e);
      }
      counter++;
    }
    warning(this.jobName + ": SET_STATUS -> Out of " + tasks.size() + " provisioning tasks, " + success +" have succeeded and " + fail + " have failed to set status to: " + status);
    info("==============================================================================================");
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  /**
       *
   */

  /**
   * Set provisioning task response to value @param response to all
   * provisioning tasks listed in the input parameter @param tasks<BR>
   * Justification is added to task note
   * @param tcProvService OIM provisioning service API
   * @param tasks List of the open tasks
   * @param response Response string. This value must be defined in
   *        the desigh console as valid response.
   *        Otherwise UNKONW response code is used and task will be rejected
   * @param justification Operation justification. This information is addeed to task note.
   * @param userName Logged in user. This information is addeed to task note.
   * @param batchSize Number of processed tasks in one batch.
   * @param sleepTime Sleep time between batches in seconds
   */
  protected void setResponse(tcProvisioningOperationsIntf tcProvService, final List<TaskDetail> tasks,
                             final String response, final String justification, final String userName, long batchSize,
                             long sleepTime) {
    final String method = "setResponse";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    int success = 0;
    int fail = 0;
    int counter = 0;
    info("=============== OPEN TASKS - SET RESPONSE ==================================================");
    for (TaskDetail task : tasks) {
      try {
        // Operation on open tasks is executed in batches
        if (batchSize > 0 && sleepTime > 0 && counter == batchSize) {
          debug(method, "Entering to sleeping mode for: " + sleepTime + " [s]");
          Thread.sleep(sleepTime * 1000);
          counter = 0;
        }

        String note = "----- Added by " + (userName == null ? "" : userName.toUpperCase()) + " on " + new Date() + " -----\n";
        note += "Operation: Set Response to " + response + "\n";
        note += "Justification: " + justification + "\n";

        Map<String, String> data = new HashMap<String, String>();
        data.put("Process Instance.Task Details.Note", task.getNote() == null ? note : task.getNote() + "\n\n" + note);
        data.put("Process Instance.Task Details.Data", response);
        tcProvService.updateTask(task.getProcessTaskKey(), task.getVersion(), data);
        info("setResponse -> Provisioning task Key [" + task.getProcessTaskKey() + "], response is set to  [" + response + "]");
        success++;
      } catch (Exception e) {
        fail++;
        fatal("setResponse -> Provisioning Task Key [" + task.getProcessTaskKey() + "], unable to set response: [" + response + "], due to exception: " + e.getMessage(), e);
      }
      counter++;
    }
    warning(this.jobName + ": SET_RESPONSE -> Out of " + tasks.size() + " provisioning tasks, " + success +
            " have succeeded and " + fail + " have failed to set response to: " + response);
    info("==============================================================================================");
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }


  /**
   * Manual Complete on multiple open provisioning tasks
   * @param tcProvService OIM provisioning service API
   * @param tasks List of the open tasks
   * @param justification Operation justification. This information is addeed to task note.
   * @param userName Logged in user. This information is addeed to task note.
   * @param batchSize Number of processed tasks in one batch.
   * @param sleepTime Sleep time between batches in seconds
   */
  protected void manuallyComplete(Thor.API.Operations.tcProvisioningOperationsIntf tcProvService,
                                  List<TaskDetail> tasks, String justification, String userName, long batchSize,
                                  long sleepTime) {

    final String method = "manuallyComplete";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    int success = 0;
    int fail = 0;
    int counter = 0;
    info("=============== OPEN TASKS - MC ==================================================");
    for (TaskDetail task : tasks) {
      long[] taskIds = new long[1];
      taskIds[0] = task.getProcessTaskKey();
      try {

        // Operation on open tasks is executed in batches
        if (batchSize > 0 && sleepTime > 0 && counter == batchSize) {
          debug(method, "Entering to sleeping mode for: " + sleepTime + " [s]");
          Thread.sleep(sleepTime * 1000);
          counter = 0;
        }
        // Create Provisioning Task Note
        String note = "----- Added by " + (userName == null ? "" : userName.toUpperCase()) + " on " + new Date() + " -----\n";
        note += "Operation: Manual Complete \n";
        note += "Justification: " + justification + "\n";
        // Update Provisioning Task Note
        Map<String, String> data = new HashMap<String, String>();
        data.put("Process Instance.Task Details.Note", task.getNote() == null ? note : task.getNote() + "\n\n" + note);
        tcProvService.updateTask(task.getProcessTaskKey(), task.getVersion(), data);
        // Manual Complete rejected provisioning task
        tcProvService.setTasksCompletedManually(taskIds);
        success++;
        info(this.jobName + ": manuallyComplete -> Provisioning Task Key [" + task.getProcessTaskKey() + "] was manually completed");
      } catch (Exception e) {
        fatal("manuallyComplete ->Provisioning Task Key [" + task.getProcessTaskKey() +"] failed to be manually completed:  " + e.getMessage(), e);
        fail++;
      }
      counter++;
    }
    warning(this.jobName + ": MANUAL_COMPLETE -> Out of " + tasks.size() + " open tasks, " + success +
            " have succeeded and " + fail + " have failed to manually complete");
    info("==================================================================================");
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  /**
   * Retry rejected open provisioning task
   * @param tcProvService OIM provisioning service API
   * @param tasks List of the open tasks
   * @param justification Operation justification. This information is addeed to task note.
   * @param userName Logged in user. This information is addeed to task note.
   * @param batchSize Number of processed tasks in one batch.
   * @param sleepTime Sleep time between batches in seconds
   */
  protected void retry(Thor.API.Operations.tcProvisioningOperationsIntf tcProvService, List<TaskDetail> tasks,
                       String justification, String userName, long batchSize, long sleepTime) {
    final String method = "manuallyComplete";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    info("=============== OPEN TASKS - RETRY ==================================================");

    int success = 0;
    int fail = 0;
    int counter = 0;
    for (TaskDetail task : tasks) {

      try {
        // Operation on open tasks is executed in batches
        if (batchSize > 0 && sleepTime > 0 && counter == batchSize) {
          debug(method, "Entering to sleeping mode for: " + sleepTime + " [s]");
          Thread.sleep(sleepTime * 1000);
          counter = 0;
        }
        // Create Provisioning Task Note
        String note =
          "----- Added by " + (userName == null ? "" : userName.toUpperCase()) + " on " + new Date() + " -----\n";
        note += "Operation: Retry \n";
        note += "Justification: " + justification + "\n";
        // Update Provisioning Task Note
        Map<String, String> data = new HashMap<String, String>();
        data.put("Process Instance.Task Details.Note", task.getNote() == null ? note : task.getNote() + "\n\n" + note);
        tcProvService.updateTask(task.getProcessTaskKey(), task.getVersion(), data);
        // Retry rejected provisioning task
        tcProvService.retryTask(task.getProcessTaskKey());
        info("INFO: retry -> Provisioning Task Key [" + task.getProcessTaskKey() + "] was succesfully retried");
        success++;
      } catch (Exception e) {
        fatal("WARNING: retry ->Provisioning Task Key [" + task.getProcessTaskKey() + "] failed during retry: " + e.getMessage(), e);
        fail++;
      }
      counter++;
    }
    warning(this.jobName + ": RETRY -> Out of " + tasks.size() + " open tasks, " + success + " have succeeded and " +  fail + " have failed to retry task");
    info("==================================================================================");
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
  }


  /**
   * Return true in case status can be set on provisioning task
   * @param tcProvService  OIM provisioning service API
   * @param processTaskKey OIM process task key
   * @param status Status e.g. UC
   * @return true in case status can be set on provisioning task
   */
  protected boolean isStatusAvailable(Thor.API.Operations.tcProvisioningOperationsIntf tcProvService,
                                      long processTaskKey, String status) {
    final String method = "isStatusAvailable";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    Thor.API.tcResultSet statuses;
    List<String> availableStatuses = new ArrayList<String>();
    try {

      statuses = tcProvService.getTasksAvailableForUpdate(processTaskKey);
      for (int i = 0; i < statuses.getRowCount(); ++i) {
        statuses.goToRow(i);
        String availableStatus = statuses.getStringValue("Status.Status");
        availableStatuses.add(availableStatus);
        if (availableStatus != null && availableStatus.equals(status)) {
          timerStop(method);
          trace(method, SystemMessage.METHOD_EXIT);
          return true;
        }
      }
      warning("isStatusAvailable -> Provisioning task Key [" + processTaskKey + "], status  [" + status +  "] is not in the list of the available statuses: " + availableStatuses);
    } catch (Exception e) {
      fatal("isStatusAvailable -> Provisioning task Key [" + processTaskKey + "], unable to evaluate status: [" +  status + "], due to exception: " + e.getMessage(), e);
    }
    timerStop(method);
    trace(method, SystemMessage.METHOD_EXIT);
    return false;
  }
}