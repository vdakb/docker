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

    File        :   ProcessAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProcessAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcTaskNotFoundException;

import Thor.API.Operations.tcProvisioningOperationsIntf;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import com.thortech.xl.util.adapters.tcUtilXellerateOperations;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.EntityAdapter;

import oracle.iam.identity.foundation.naming.ProcessDefinition;
import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// class ProcessAdapter
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager Process
 ** Entities.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ProcessAdapter extends EntityAdapter {

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
      buff.append("objectName: ");
      buff.append(this.objectName());
      buff.append(" | processInstance: ");
      buff.append(this.processInstance());
      buff.append(" | workflowTask: ");
      buff.append(this.workflowTask());
      return buff.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProcessAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public ProcessAdapter(final tcDataProvider provider) {
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public ProcessAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processOrdered
  /**
   ** Validates if the specified process is ordered for the specified user
   ** profile
   **
   ** @param  processName        the name of the process to validate.
   ** @param  userKey            the internal identifier of the user profile to
   **                            validate.
   **
   ** @return                    {@link #SUCCESS}  if the specified account was
   **                            succesfully change to a regular account;
   **                            otherwise {@link #FAILURE}.
   */
  public String processOrdered(final String processName, final String userKey) {
    final String method = "processOrdered";
    trace(method, SystemMessage.METHOD_ENTRY);

    String response;
    try {
      response = tcUtilXellerateOperations.checkProcessOrderedForUser(this.provider(), processName, userKey);
    }
    catch (Exception e) {
      response = TaskError.UNHANDLED;
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateProcessData
  /**
   ** Obtains a process data field
   **
   ** @param  userKey            the internal identifier of the user profile to
   **                            obtain the data from.
   ** @param  sourceProcess      the name of the process providing the data.
   ** @param  sourceField        the physical name of the process form field
   **                            providing the value.
   **
   ** @return                    the field value for the specified process
   **                            information.
   */
  public String populateProcessData(final String userKey, final String sourceProcess, final String sourceField) {
    final String method = "populateProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String processFormName = processFormName(userKey, sourceProcess);
    if (processFormName.startsWith("-"))
      return processFormName;

    final String processValue = processFieldValue(userKey, processFormName.toLowerCase(), sourceField.toLowerCase());
    trace(method, SystemMessage.METHOD_EXIT);
    return processValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagateProcessData
  /**
   ** Propagates a process data field value to another process
   **
   ** @param  userKey            the internal identifier of the user profile to
   **                            obtain the data from.
   ** @param  sourceProcess      the name of the process providing the data.
   ** @param  sourceField        the physical name of the process form field
   **                            to providing the value.
   ** @param  targetProcess      the name of the process revieving the data.
   ** @param  targetField        the physical name of the process form field
   **                            to revieving the value.
   **
   ** @return                    the field value for the specified process
   **                            information.
   */
  public String propagateProcessData(final String userKey, final String sourceProcess, final String sourceField, final String targetProcess, final String targetField) {
    final String method = "propagateProcessData";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String sourceFormName = processFormName(userKey, sourceProcess);
    if (sourceFormName.startsWith("-"))
      return sourceFormName;

    final String targetFormName = processFormName(userKey, targetProcess);
    if (targetFormName.startsWith("-"))
      return targetFormName;

    trace(method, SystemMessage.METHOD_EXIT);
    return method;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triggerTask
  /**
   ** Triggers a specific task for the provided user.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** This mehod triggers the specified task name on all processes for the user.
   **
   ** @param  userKey            the internal identifier of the user.
   ** @param  taskName           the name of the workflow task to trigger.
   **
   ** @throws Exception          if the operation fails.
   */
  public void triggerTask(final String userKey, String taskName)
    throws Exception {

    triggerTask(userKey, null, taskName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triggerTask
  /**
   ** Triggers a specific task that is defined on a particular process for the
   ** provided user.
   **
   ** @param  userKey            the internal identifier of the user.
   ** @param  processName        the name of the process were the task should
   **                            be triggered.
   ** @param  taskName           the name of the workflow task to trigger.
   **
   ** @throws Exception          if the operation fails.
   */
  public void triggerTask(final String userKey, final String processName, final String taskName)
    throws Exception {

    final String method = "trigger";
    trace(method, SystemMessage.METHOD_ENTRY);
    List<Long> users = new ArrayList<Long>(1);
    users.add(Long.parseLong(userKey));
    triggerTask(processName, taskName);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   triggerTask
  /**
   ** Triggers a specific task that is defined on a particular process for all
   ** users.
   **
   ** @param  users              the collection if identities to trigger the
   **                            task for.
   ** @param  processName        the name of the process were the task should
   **                            be triggered.
   ** @param  taskName           the name of the workflow task to trigger.
   **
   ** @throws Exception          if the operation fails.
   */
  public void triggerTask(List<Long> users, final String processName, final String taskName)
    throws Exception {

    final String method = "trigger";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<Long, Set<Process>> process = buildUserProcessTable(users, processName, taskName);
    insertProcessTask(process);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildWorkflow
  protected long buildWorkflow(final Map<String, Long> workflow, final String processName, final String taskName)
    throws Exception {

    if (processName == null)
      throw new Exception("Provisioning Process Name is not permitted to be null");

    // check to see if we have already lookuped this key up
    if (workflow.containsKey(processName)) {
      return workflow.get(processName).longValue();
    }
    else if (this.provider() != null) {
      long id = findProcessTask(processName, taskName);
      if (id != 0)
        workflow.put(processName, new Long(id));
      return id;
    }
    else
      throw new Exception("Unable to get WF task id since the database was unavailable to make a query and the default WF task id has not been set");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildUserProcessTable
  /**
   ** Inserts the task in to the provisioning workflows of one or mor users.
   ** <br>
   ** The methode expecting that the user processes provided by a {@link Map}.
   **
   ** @throws Exception
   */
  private Map<Long, Set<Process>> buildUserProcessTable(List<Long> users, final String processName, final String taskName)
    throws Exception {

    Map<String, Long>       processCache = new HashMap<String, Long>();
    Map<Long, Set<Process>> processMap   = new HashMap<Long, Set<Process>>(users.size());
    Iterator<Long> i = users.iterator();
    while (i.hasNext()) {
      Long        user    = i.next();
      // get all processes were the user id provisioned to
      tcResultSet objects = userFacade().getObjects(user.longValue());
      // loop through the result set, pulling out the process instance keys of
      // the provisioned objects
      Set<Process> processes = new HashSet<Process>(objects.getRowCount());
      for (int j = 0; j < objects.getRowCount(); j++) {
        objects.goToRow(j);
        long        objectKey    = objects.getLongValue(ResourceObject.KEY);
        String      objectName   = objects.getStringValue(ResourceObject.NAME);
        String      objectStatus = objects.getStringValue(ResourceObject.STATUS);
        String      process      = findProcess(objectFacade().getProcessesForObject(objectKey));
        if ((processName == null) || (processName.equals(process))) {
          long workflowTask = buildWorkflow(processCache, processName, taskName);
          if (((objectStatus.equals("Provisioned")) || objectStatus.equals("Enabled")) && (workflowTask != 0))
            processes.add(new Process(objectName, objects.getLongValue(ProcessDefinition.KEY), workflowTask));
        }
        if (processes.size() > 0)
         processMap.put(user, processes);
      }
    }
    return processMap;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findProcess
  /**
   ** Returns name of the provisioning workflow from the specified result set.
   ** <br>
   ** The method assumes that the passed result set contains the processes
   ** associated with and obtained from a Resource Object.
   **
   ** @param  resultSet          the name of the provisioning process containing
   **                            the task.
   **
   ** @return                    Task Definition Key
   */
  private String findProcess(tcResultSet resultSet)
    throws Exception {

    final String method = "findProcess";
    trace(method, SystemMessage.METHOD_ENTRY);

    try {
      for (int i = 0; i < resultSet.getRowCount(); i++) {
        resultSet.goToRow(i);
        String processType = resultSet.getStringValue(ProcessDefinition.TYPE);
        // Check if we has a provisioning process ...
        if ("Provisioning".equals(processType))
          // and return the name of the workflow
          return resultSet.getStringValue(ProcessDefinition.NAME);
      }
      return null;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findProcessTask
  /**
   ** Returns the key of the process task passed to the constructor within the
   ** specified process.
   **
   ** @param  processName        the name of the provisioning process containing
   **                            the task specified by <code>zaskName</code>.
   ** @param  taskName           the name of the provisioning task zo lookup in
   **                            the provisioning process specified by
   **                            <code>processName</code>.
   **
   ** @return                    the task definition key
   */
  private long findProcessTask(final String processName, final String taskName) {
    final String method = "findProcessTask";
    trace(method, SystemMessage.METHOD_ENTRY);

    final  String query = "SELECT MIL.MIL_KEY FROM MIL INNER JOIN TOS ON MIL.TOS_KEY = TOS.TOS_KEY INNER JOIN PKG ON TOS.PKG_KEY = PKG.PKG_KEY WHERE (PKG.PKG_NAME = '" + processName + "') AND (MIL.MIL_NAME = '" + taskName + "')";
    try {
      tcDataSet dataSet = new tcDataSet();
      dataSet.setQuery(provider(), query);
      dataSet.executeQuery();
      if (dataSet.getRowCount() == 0)
        return 0;

      dataSet.goToRow(0);
      return (dataSet.getLong("MIL_KEY"));
    }
    catch (Exception e) {
      error(method, e.getMessage());
      return 0;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertProcessTask
  /**
   ** Inserts the task in to the provisioning workflows of one or mor users.
   ** <br>
   ** The methode expecting that the user processes provided by a {@link Map}.
   **
   ** @param  process            the {@link Map} providing the association
   **                            between users and processes.
   **
   ** @throws TaskException      if the operation fails.
   */
  private void insertProcessTask(Map<Long, Set<Process>> process)
    throws TaskException {

    final String method = "insertTasks";
    trace(method, SystemMessage.METHOD_ENTRY);

    tcProvisioningOperationsIntf processFacade = null;
    try {
      processFacade    = service(tcProvisioningOperationsIntf.class);
      final Iterator<Long> i = process.keySet().iterator();
      while (i.hasNext()) {
        // take the internal user identifier
        final Set<Process>      processes = process.get(i.next());
        final Iterator<Process> j         = processes.iterator();
        while (j.hasNext()) {
          final Process cursor = j.next();
          processFacade.addProcessTaskInstance(cursor.workflowTask, cursor.processInstance);
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
      if (processFacade != null) {
        processFacade.close();
      }
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processInstance
  private long processInstance(final String userKey, final String processName) {
    final String method = "processInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    PreparedStatementUtil statement = new PreparedStatementUtil();
    long                  instance  = -1L;
    try {
      statement.setStatement(this.provider(), "select orc.orc_key, orc.tos_key from orc orc, pkg pkg, sta sta where pkg.pkg_name = ? and pkg.pkg_key = orc.pkg_key and orc.usr_key = ? and orc.orc_status=sta.sta_status and sta.sta_bucket != ?");
      statement.setString(1, processName);
      statement.setString(2, userKey);
      statement.setString(3, "Cancelled");
      statement.execute();

      tcDataSet dataSet = statement.getDataSet();
      if (dataSet.isEmpty())
        return -20L;
      if (dataSet.isNull("orc_key"))
        return -20L;
      if (dataSet.isNull("tos_key"))
        return -30L;

      instance = dataSet.getLong("tos_key");
    }
    catch (tcDataAccessException e) {
      fatal(method, e);
    }
    catch (tcDataSetException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFormName
  private String processFormName(final String userKey, final String processName) {
    final String method = "processFormName";
    trace(method, SystemMessage.METHOD_ENTRY);

    String                processForm = null;
    try {
      long tos = processInstance(userKey, processName);
      if (tos < 0)
        return String.valueOf(tos);

      PreparedStatementUtil statement   = new PreparedStatementUtil();
      statement.setStatement(this.provider(), "select tos.sdk_key, sdk.sdk_name from tos tos, sdk sdk where tos_key = ? and sdk.sdk_key = tos.sdk_key");
      statement.setLong(1, tos);
      statement.execute();
      tcDataSet dataSet = statement.getDataSet();
      if (dataSet.isEmpty())
        return "-40";
      if (dataSet.isNull("sdk_name"))
        return "-40";

      processForm = dataSet.getString("sdk_name");
    }
    catch (tcDataAccessException e) {
      fatal(method, e);
    }
    catch (tcDataSetException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return processForm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFieldValue
  private String processFieldValue(final String userKey, final String processFormName, final String processField) {
    final String method = "processFieldValue";
    trace(method, SystemMessage.METHOD_ENTRY);

    StringBuilder query = new StringBuilder();
    query.append("select ").append(processFormName).append(".").append(processField);
    query.append(" from ").append(processFormName).append(" ").append(processFormName).append(", orc orc, sta sta");
    query.append(" where ").append(processFormName).append(".orc_key = orc.orc_key and orc.usr_key = ? and orc.orc_status = sta.sta_status and sta.sta_bucket != ?");

    String processValue = SystemConstant.EMPTY;
    try {
      PreparedStatementUtil statement = new PreparedStatementUtil();
      statement.setStatement(this.provider(), query.toString());
      statement.setString(1, userKey);
      statement.setString(2, "Cancelled");
      statement.execute();

      tcDataSet dataSet = statement.getDataSet();
      if (dataSet.isEmpty())
        return processValue;
      if (dataSet.isNull(processField))
        return processValue;

      processValue = dataSet.getString(processField);
    }
    catch (tcDataAccessException e) {
      fatal(method, e);
    }
    catch (tcDataSetException e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return processValue;
  }
}