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
    Subsystem   :   Process Form migration

    File        :   PropagateProcessFormAttribute.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    UpdateProcessFormAttribute.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/
package bka.iam.identity.process;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

import java.util.stream.Collectors;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcProvisioningOperationsIntf;
import Thor.API.Operations.TaskDefinitionOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcTaskNotFoundException;

import oracle.iam.platform.Platform;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.api.UserManagerConstants;

import oracle.iam.identity.exception.UserSearchException;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.exception.GenericAppInstanceServiceException;
import oracle.iam.provisioning.exception.GenericProvisioningException;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskAttribute;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractSchedulerTask;

////////////////////////////////////////////////////////////////////////////////
// class PropagateProcessFormAttribute
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>PropagateProcessFormAttribute</code> invokes provisioning workflows
 ** from the associated process form attribute in oder to update the specified
 ** data on the target resource.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PropagateProcessFormAttribute extends AbstractSchedulerTask {
  
  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Process
  // ~~~~~ ~~~~~~~
  /**
   ** Member class to hold the user process record
   */
  class Process {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String objectName;
    private final long   processInstance;
    private final long   workflowTask;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     **
     */
    Process (String objectName, long processInstance, long workflowTask) {
      this.objectName      = objectName;
      this.processInstance = processInstance;
      this.workflowTask    = workflowTask;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: objectName
    /**
     ** Returns the objectName.
     **
     ** @return                  the objectName.
     */
    private String objectName() {
      return this.objectName;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: processInstance
    /**
     ** Returns the processInstance.
     **
     ** @return                  the processInstanceId.
     */
    private long processInstance() {
      return this.processInstance;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: workflowTask
    /**
     ** Returns the workflowTask.
     **
     ** @return                  the workflowTask.
     */
    private long workflowTask() {
      return this.workflowTask;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character "|".
     ** <br>
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                   the string representation of this instance.
     */
    public String toString() {
      StringBuilder buff = new StringBuilder("Process:\n");
      buff.append("| objectName      | ").append(this.objectName());
      buff.append("| processInstance | ").append(this.processInstance());
      buff.append("| workflowTask    | ").append(this.workflowTask());
      return buff.toString();
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  private static final String          LOGGER_CATEGORY  = "BKA.PROCESSFORM.UPDATE";
  
  /**
   ** Attribute to advise what is the resource object used by the implementing
   ** scheduled job.
   ** <br>
   ** This attribute is mandatory.
   **/
  private static final String          RESOURCE_OBJECT  = "Resource Object";

  /**
   ** Attributes that need to be updated on the target system.
   ** <br>
   ** This attribute is mandatory.
   */
  private static final String          ATTRIBUTES       = "Attributes";

  /**
   ** List of the users separated by semicolon that need to be updated
   ** <br>
   ** This attribute is optional.
   */
  private static final String          USERS            = "Users";
  
  /** the character to separate single values */
  private static final String          SEPARATOR        = ";";
  
  /** the prefix of the name of the provisioning workflow to invoke */
  private static final String          TASK_NAME_PREFIX = "Change";
  
  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attributes      = {
    /** the task attribute IT Resource */
    TaskAttribute.build(RESOURCE_OBJECT, TaskAttribute.MANDATORY)
    /** the task attribute with reconciliation target */
  , TaskAttribute.build(ATTRIBUTES,      TaskAttribute.MANDATORY)
    /** the task attribute with notification template */
  , TaskAttribute.build(USERS,           TaskAttribute.OPTIONAL)
  };
  
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PropagateProcessFormAttribute</code> scheduler
   ** instance that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PropagateProcessFormAttribute() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceName
  /**
   ** Returns the name of the Object Name resource on which attribute(s) will
   ** be populated.
   **
   ** @return                    the name of the resource Object Name on which
   **                            attribute(s) will be populated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String resourceObjectName() {
    return stringValue(RESOURCE_OBJECT);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the list of process form attribute names that will be
   ** propagated in the target system.
   **
   ** @return                    the list of process form attribute names that
   **                            will be propagated in the target system.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String formAttributes() {
    return stringValue(ATTRIBUTES);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   users
  /**
   ** Returns the list of users that need to be updated.
   **
   ** @return                    the list of users that need to be updated.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  protected final String users() {
    return stringValue(USERS);
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
   ** The entry point of the proagate process form attribute task to perform.
   **
   ** @throws TaskException in case an error does occur.
   */
  @Override
  protected void onExecution()
    throws TaskException {

    final Map<Long, Set<Process>> process  = new HashMap<Long, Set<Process>>();
    final List<Long>              users    = getUserKeys();
    final List<String>            tasks    = getTaskNameList();
    final List<Account>           accounts = getAccountFromResourceObject(resourceObjectName());
    for (Account account : accounts) {
      final Long usrKey = Long.parseLong(account.getUserKey());
      if (users.isEmpty() || users.contains(usrKey)) {
        final Set<Process> processes = new HashSet<Process>(tasks.size());
        for (String taskName : tasks) {
          final long procInstKey = Long.parseLong(account.getProcessInstanceKey());
          final long taskKey     = getTaskKey(procInstKey, taskName);
          if (taskKey != 0)
            processes.add(new Process(resourceObjectName(), procInstKey , taskKey));
        }
        if (process.containsKey(usrKey)) {
          process.get(usrKey).addAll(processes);
        } else {
          process.put(usrKey, processes);
        }
      }
    }
    inserProcessTask(process);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTaskKey
  /**
   ** Return the task key of the specifed process.
   ** <br>
   ** The methode expecting that the user processes provided by a {@link Map}.
   **
   ** @param  procInstKey        the key of the process instance in the system.
   ** 
   ** @param  taskName           the name of the task process.
   **
   ** @throws TaskException      if the operation fails.
   */
  private long getTaskKey(final long procInstKey, final String taskName) {
    final String method = "getTaskKey";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final TaskDefinitionOperationsIntf taskDefOp = Platform.getService(TaskDefinitionOperationsIntf.class);
    final HashMap<String, String>      param     = new HashMap<String, String>();
    param.put("Process Definition.Tasks.Task Name", taskName);
    long taskKey = 0L;
    try {
      final tcResultSet resultSet = taskDefOp.getTaskDetail(procInstKey, param);
      if (resultSet.getRowCount() != 0) {
        resultSet.goToRow(0);
        taskKey = resultSet.getLongValue("Process Definition.Tasks.Key");
      }
    }
    catch (tcAPIException e) {
      fatal(method, e);
    }
    catch (tcColumnNotFoundException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return taskKey;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   inserProcessTask
  /**
   ** Inserts the task in to the provisioning workflows of one or mor users.
   ** <br>
   ** The methode expecting that the user processes provided by a {@link Map}.
   **
   ** @param  processTable       the {@link Map} providing the association
   **                            between users and processes.
   **
   ** @throws TaskException      if the operation fails.
   */
  private void inserProcessTask(final Map<Long, Set<Process>> processTable)
    throws TaskException {

    final String method = "inserProcessTask";
    trace(method, SystemMessage.METHOD_ENTRY);
    final tcProvisioningOperationsIntf service = service(tcProvisioningOperationsIntf.class);
    try {
      for (Map.Entry<Long, Set<Process>> row : processTable.entrySet()) {
        final Set<Process> processes = row.getValue();
        for (Process process : processes) {
          debug(method, "Update user with key: " + row.getKey() + "%s with the following process " + process.toString());
          service.addProcessTaskInstance(process.workflowTask, process.processInstance);
        }
      }
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    catch (tcTaskNotFoundException e) {
      throw new TaskException(e);
    }
    finally {
      if (service != null) {
        service.close();
      }
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccountFromResourceObject
  /**
   ** Return the list of accounts for a given resource object name.
   **
   ** @param  resourceObjectName  the name of the resource object
   ** 
   */
  private List<Account> getAccountFromResourceObject(final String resourceObjectName) {
    final String method = "getAccountFromResourceObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final ApplicationInstanceService appInstServ = Platform.getService(ApplicationInstanceService.class);
    final ProvisioningService        prvServ     = Platform.getService(ProvisioningService.class);
    final List<Account>              accounts    = new ArrayList<Account>();
    
    SearchCriteria criteria = null;
    
    try {
      criteria =  new SearchCriteria(ApplicationInstance.OBJ_NAME, resourceObjectName, SearchCriteria.Operator.EQUAL);
      final List<ApplicationInstance> applicationInstanceList = appInstServ.findApplicationInstance(criteria, null);
      
      for (ApplicationInstance application : applicationInstanceList) {
        SearchCriteria criteriaProv =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), ProvisioningConstants.ObjectStatus.PROVISIONED.getId(), SearchCriteria.Operator.EQUAL);
        SearchCriteria criteriaEna  =  new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.ACCOUNT_STATUS.getId(), ProvisioningConstants.ObjectStatus.ENABLED.getId(), SearchCriteria.Operator.EQUAL);
        criteria                    =  new SearchCriteria(criteriaProv, criteriaEna, SearchCriteria.Operator.OR);
        accounts.addAll(prvServ.getProvisionedAccountsForAppInstance(application.getApplicationInstanceName(), criteria, null));
      }

      info(String.format("Found %d account that will be update", accounts.size()));
    }
    catch (GenericAppInstanceServiceException | GenericProvisioningException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return accounts;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTaskNameList
  /**
   ** Return the list of the process name link to the process form attribute.
   ** 
   */
  private List<String> getTaskNameList() {
    final String method = "getTaskNameList";
    trace(method, SystemMessage.METHOD_ENTRY);
    final List<String> formAttributeList = Arrays.asList(formAttributes().split(SEPARATOR));
    final List<String> taskNameList      = new ArrayList<String>();
    for (String attr : formAttributeList) {
      StringBuilder taskName = new StringBuilder();
      taskName.append(TASK_NAME_PREFIX).append(" ").append(attr);
      taskNameList.add(taskName.toString());
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return taskNameList;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUserKeys
  /**
   ** Return the list of the user key with the specified user logins in the job
   ** attribute.
   ** 
   */
  private List<Long> getUserKeys() {
    final String method = "getUserKeys";
    trace(method, SystemMessage.METHOD_ENTRY);
    
    final UserManager usrmgr    = service(UserManager.class);
    List<Long>  userKeys  = new ArrayList<Long>();
    try {
      if (users() != null) {
         // operate on users explicitly requested
        final List<String>    logins   = Arrays.asList(users().split(SEPARATOR));
        final SearchCriteria  criteria = new SearchCriteria(UserManagerConstants.AttributeName.USER_LOGIN.getId(), logins, SearchCriteria.Operator.IN);
        final Set<String>     retSet   = CollectionUtility.set(UserManagerConstants.AttributeName.USER_LOGIN.getId());
        userKeys = usrmgr.search(criteria, retSet, null).stream().map(e -> Long.parseLong(e.getEntityId())).collect(Collectors.toList());
      }
    }
    catch (UserSearchException e) {
      fatal(method, e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return userKeys;
  }
}