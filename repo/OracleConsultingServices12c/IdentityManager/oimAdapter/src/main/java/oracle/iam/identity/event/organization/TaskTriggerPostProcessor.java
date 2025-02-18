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

    File        :   TaskTriggerPostProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskTriggerPostProcessor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.event.organization;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.thortech.xl.audit.engine.AuditEngine;

import com.thortech.xl.dataobj.tcScheduleItem;

import com.thortech.xl.dataobj.util.ChangeSet;

import oracle.iam.platform.Platform;

import oracle.iam.platform.kernel.EventFailedException;

import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;

import oracle.iam.platform.entitymgr.EntityManager;

import oracle.iam.identity.vo.Identity;

import oracle.iam.identity.resources.LRB;

import oracle.iam.identity.orgmgmt.vo.Organization;

import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.persistence.DatabaseStatement;
import oracle.iam.identity.foundation.persistence.DatabaseConnection;

import oracle.iam.identity.foundation.event.AbstractPostProcessHandler;

////////////////////////////////////////////////////////////////////////////////
// class TaskTriggerPostProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** Implementation to get back the process triggers on organizations that was
 ** lost in transistion from Release 1 to Release 2.
 ** <p>
 ** The purpose of this event handler is to invoke all the process task that are
 ** registered in <code>Lookup.ACT_PROCESS_TRIGGERS</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class TaskTriggerPostProcessor extends AbstractPostProcessHandler {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TaskTriggerPostProcessor</code> event handler that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TaskTriggerPostProcessor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (PostProcessHandler)
  /**
   ** The implementation of this post process event handler in one-off
   ** orchestration.
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

    if ((orchestration.getOperation().equals(TaskTrigger.ACTION))) {
      final Identity before = (Identity)before(orchestration);
      final Identity actual = (Identity)actual(orchestration);
      try {
        processModify(processId, orchestration.getTarget().getEntityId(), before, actual, orchestration.getParameters());
      }
      finally {
        trace(method, SystemMessage.METHOD_EXIT);
      }
    }
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

    final String[]                        identities = orchestration.getTarget().getAllEntityId();
    final HashMap<String, Serializable>[] parameter  = orchestration.getBulkParameters();
    if ((identities != null) && (identities.length > 0)) {
      final Identity[] before = (Identity[])before(orchestration);
      final Identity[] actual = (Identity[])actual(orchestration);
      if (orchestration.getOperation().equals(TaskTrigger.ACTION)) {
        if ((before != null) && (actual != null))
          for (int i = 0; i < identities.length; i++)
            processModify(processId, identities[i], before[i], actual[i], parameter[i]);
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
  // Method:   actual
  /**
   ** Returns the attribute mapping that represents the actual values of the
   ** entity after the orchestration finished.
   **
   ** @param  orchestration      the {@link AbstractGenericOrchestration} that
   **                            provides contextual information about the
   **                            event.
   **
   ** @return                    the {@link Object} providing the identity
   **                            states.
   */
  private Object actual(final AbstractGenericOrchestration orchestration) {
    final Object before = before(orchestration);

    Object state = null;
    if (before instanceof Organization[]) {
      Organization[] o = (Organization[])before;
      Organization[] n = new Organization[o.length];
      for (int i = 0; i  < o.length; i++) {
        final HashMap<String, Object> mapping = new HashMap<String, Object>(o[i].getAttributes());
        mapping.putAll(orchestration.getParameters());
        final Organization organization = new Organization(o[i].getEntityId(), mapping);
        n[i] = organization;
      }
      state = n;
    }
    else {
      final HashMap<String, Object> mapping = new HashMap<String, Object>(((Organization)before).getAttributes());
      mapping.putAll(orchestration.getParameters());
      final Organization organization = new Organization(((Organization)before).getEntityId(), mapping);
      state = organization;
    }
    return state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Returns the attribute mapping that represents the existing values of the
   ** entity before the orchestration started.
   ** <p>
   ** The mapping is created by a pre-process handler or by the reconciliation
   ** event.
   **
   ** @param  orchestration      the {@link AbstractGenericOrchestration} that
   **                            provides contextual information about the
   **                            event.
   **
   ** @return                    the {@link Object} providing the identity
   **                            states.
   */
  private Object before(final AbstractGenericOrchestration orchestration) {
    final HashMap<String, Serializable> data = orchestration.getInterEventData();
    return (data != null) ? data.get(TaskTrigger.BEFORE) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changed
  /**
   ** Determines the changed attributes of a organization by comparing the
   ** values of the before state and after state.
   **
   ** @param  before             the attribute mapping that represents the
   **                            existing values of the entity before the
   **                            orchestration started.
   ** @param  parameter          the {@link Map} that provides contextual
   **                            information about the event.
   **
   ** @return                    the names of the attributes that are really
   **                            changed.
   */
  private Set<String> changed(final Identity before, final Map<String, Serializable> parameter) {
    final Set<String> changedAttrNames = new HashSet<String>();
    if ((before != null) && (parameter != null)) {
      for (String name : parameter.keySet()) {
        Object oldValue = before.getAttribute(name);
        Object newValue = parameter.get(name);
        if ((newValue == null && oldValue != null) || (newValue != null && oldValue == null)) {
          changedAttrNames.add(name);
        }
        else if (!newValue.equals(oldValue.toString()))
          changedAttrNames.add(name);
      }
    }
    return changedAttrNames;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processModify
  private void processModify(final long processID, final String identitfier, final Identity before, final Identity actual, final Map<String, Serializable> parameter)
    throws EventFailedException {

    final String method = "processModify";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> attributes = changed(before, parameter);
    if (attributes.size() > 0) {
      boolean successfullyAudited = false;
      final AuditEngine auditEngine = AuditEngine.getAuditEngine(database());
      auditEngine.startTransaction(TaskTrigger.ACTION, "oracle.iam.identity.event.organization.TaskTriggerPostProcessor", database());
      try {
        final Map<String, String> logical  = new HashMap<String, String>();
        final Map<String, String> physical = new HashMap<String, String>();
        prepareEntityMapping(attributes, logical, physical);

        final Map<String, String> instances   = workflows(identitfier, true);
        final List<String>        taskTrigger = taskTrigger(attributes, logical);
        final String              bulkTrigger = bulkTrigger();
        final boolean             bulkChange  = ((taskTrigger.size() > 1) && (bulkTrigger != null));
        if (bulkChange) {
          final ChangeSet changeSet = changeSet(before, actual, attributes, logical);
          insertTasks(instances, bulkTrigger, "", changeSet, bulkChange);
        }
        else {
          for (String trigger : taskTrigger) {
            final String oldValue = oldValue(before, physical, trigger);
            insertTasks(instances, trigger, oldValue, null, bulkChange);
          }
        }
        successfullyAudited = true;
      }
      catch (Exception e) {
        error(method, LRB.DEFAULT.getString("IAM-4058004", new Object[] { identitfier, e }));
        throw new EventFailedException(processID, "IAM-4058004", LRB.DEFAULT.getString("IAM-4058004", new Object[] { identitfier, e }), null, "TriggerProcesses", e);
      }
      finally {
        auditEngine.finishTransaction(successfullyAudited, database());
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   taskTrigger
  /**
   ** Returns the names of <code>Process Task</code>s that are triggered by
   ** changes on attributes.
   **
   ** @param  attributes         the {@link Set} of attributes that are changed.
   ** @param  mapping            the mapping of logical attribute names to
   **                            physical column names.
   **
   ** @return                    the {@link List} of <code>Process Task</code>s
   **                            that has to be triggered.
   */
  private List<String> taskTrigger(final Set<String> attributes, final Map<String, String> mapping) {
    final String method = "taskTrigger";
    trace(method, SystemMessage.METHOD_ENTRY);

    final Set<String> field = new HashSet<String> ();
    for (String name : attributes) {
      if (mapping.containsKey(name))
        field.add(mapping.get(name).toUpperCase());
    }

    Connection        connection = null;
    ResultSet         resultSet  = null;
    PreparedStatement statement  = null;

    final List<String> task  = new ArrayList<String>();
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      statement  = DatabaseStatement.createPreparedStatement(connection, "select lkv_encoded,lkv_decoded from lku lku left outer join lkv lkv on lku.lku_key=lkv.lku_key where lku_type_string_key=?");
      statement.setString(1, TaskTrigger.REGISTRY);
      resultSet  = statement.executeQuery();
      while (resultSet.next()) {
        String name  = resultSet.getString(1);
        String value = resultSet.getString(2);
        if (field.contains(name.toUpperCase()))
          task.add(value);
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    DatabaseStatement.closeResultSet(resultSet);
    DatabaseStatement.closeStatement(statement);
    DatabaseConnection.release(connection);
    trace(method, SystemMessage.METHOD_EXIT);
    return task;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulkTrigger
  /**
   ** Returns the name of the task to execute if the operation has to be done in
   ** bulk.
   **
   ** @return                    the name of the task to execute if the
   **                            operation has to be done in bulk.
   */
  private String bulkTrigger() {
    final String method = "bulkTrigger";
    trace(method, SystemMessage.METHOD_ENTRY);

    String bulkTaskName = null;
    Connection        connection = null;
    ResultSet         resultSet  = null;
    PreparedStatement statement  = null;
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      statement  = DatabaseStatement.createPreparedStatement(connection, "select lkv_encoded,lkv_decoded from lku lku left outer join lkv lkv on lku.lku_key=lkv.lku_key where lku_type_string_key=?");
      statement.setString(1, TaskTrigger.REGISTRY);
      resultSet  = statement.executeQuery();
      while (resultSet.next()) {
        String name  = resultSet.getString(1);
        String value = resultSet.getString(2);
        if (name.equals("BULK")) {
          bulkTaskName = value;
          break;
        }
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    DatabaseStatement.closeResultSet(resultSet);
    DatabaseStatement.closeStatement(statement);
    DatabaseConnection.release(connection);
    trace(method, SystemMessage.METHOD_EXIT);
    return bulkTaskName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workflows
  /**
   ** Build the provisioning workflows of an organization.
   **
   ** @return                    the provisioning workflows of an organization.
   */
  private Map<String, String> workflows(final String organizationKey, boolean checkCancel) {
    final String method = "workflows";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, String> processMap = new HashMap<String, String>();
    Connection          connection = null;
    ResultSet           resultSet  = null;
    PreparedStatement   statement  = null;
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      // get all processes were the user id provisioned to
      if (checkCancel)
        statement  = DatabaseStatement.createPreparedStatement(connection, "select orc_key, tos_key from orc where usr_key is null and act_key=? and tos_key is not null and orc_status not in ('X', 'PX')");
      else
        statement  = DatabaseStatement.createPreparedStatement(connection, "select orc_key, tos_key from orc where usr_key is null and act_key=? and tos_key is not null");
      statement.setLong(1, Long.parseLong(organizationKey));
      resultSet  = statement.executeQuery();
      while (resultSet.next())
        processMap.put(resultSet.getString(1), resultSet.getString(2));
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return processMap;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   insertTasks
  /**
   ** Inserts the task in to the provisioning workflows of one or mor users.
   ** <br>
   ** The methode expecting that the user processes provided by a {@link Map}.
   **
   ** @param  oldValue           the value changed.
   ** @param  process            the {@link Map} providing the association
   **                            between identities and processes.
   ** @param  taskName           the name of the provisioning task to trigger in
   **                            the provisioning process specified by
   **                            <code>process</code>.
   */
  private void insertTasks(final Map<String, String> process, final String taskName, final String oldValue, final ChangeSet changeSet, final boolean bulk) {
    final String method = "insertTasks";
    trace(method, SystemMessage.METHOD_ENTRY);

    Connection        connection  = null;
    ResultSet         resultSet   = null;
    PreparedStatement statement   = null;
    final Map<String, String> taskMap = tasksForName(taskName, process);
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      // statement to detect all process task that cannot be performed now due
      // to the System Validation Task is not completed
      statement  = DatabaseStatement.createPreparedStatement(connection, "select count(*) from sch sch, mil mil, osi osi left outer join rsc rsc on osi.rsc_key=rsc.rsc_key left outer join usr asgnusr on osi.osi_assigned_to_usr_key=asgnusr.usr_key left outer join ugp asgnugp on osi.osi_assigned_to_ugp_key=asgnugp.ugp_key, usr updusr, sta sta where osi.mil_key=mil.mil_key and sch.sch_key=osi.sch_key and sch.sch_updateby=updusr.usr_key and sch.sch_status!='C' and mil_name='System Validation' and osi.orc_key=?");
      for (String processKey : process.keySet()) {
        try {
          statement.setString(1, processKey);
          resultSet = statement.executeQuery();
          resultSet.next();
          if (resultSet.getInt(1) != 0)
            continue;
        }
        catch (Exception e) {
          error(method, "Error Retrieving Process Tasks");
          fatal(method, e);
        }
        finally {
          DatabaseStatement.closeResultSet(resultSet);
        }
        for (String taskKey : taskMap.keySet()) {
          if (process.get(processKey).equals(taskMap.get(taskKey))) {
            if (offlined(taskKey)) {
              debug(method, String.format("Task with key %s offlined.", taskKey));
              try {
                submitAsynchron(processKey, taskKey, oldValue);
              }
              catch (Exception e) {
                error(method, String.format("JMS Submission failed, insert task serially for key %s.", taskKey));
                error(method, e.getMessage());
                submitSynchron(processKey, taskKey, changeSet, oldValue, bulk);
              }
            }
            else {
              debug(method, String.format("Task with key %s not offlined.", taskKey));
              submitSynchron(processKey, taskKey, changeSet, oldValue, bulk);
            }
          }
        }
      }
      trace(method, SystemMessage.METHOD_EXIT);
    }
    catch (Exception e) {
      error(method, "Error Retrieving Process Tasks");
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tasksForName
  /**
   ** Returns the key mapping of the process task passed in as
   ** <code>taskName</code> within the specified process map.
   **
   ** @param  taskName           the name of the provisioning task to lookup in
   **                            the provisioning process specified by
   **                            <code>process</code>.
   ** @param  process            the name of the provisioning process containing
   **                            the task specified by <code>zaskName</code>.
   **
   ** @return                    the task definition key
   */
  private Map<String, String> tasksForName(final String taskName, final Map<String, String> process) {
    final String method = "tasksForName";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, String> taskMap = new HashMap<String, String>();
    Connection connection       = null;
    PreparedStatement statement = null;
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      statement  = DatabaseStatement.createPreparedStatement(connection, "select mil.mil_key from tos tos,mil mil where tos.tos_key = mil.tos_key and tos.tos_key=? and mil_is_soft_delete is null and mil_name=?");
      statement.setString(2, taskName);
      for (String name : process.values()) {
        if (!taskMap.containsValue(name)) {
          ResultSet resultSet = null;
          try {
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next())
              taskMap.put(resultSet.getString(1), name);
          }
          catch (Exception e) {
            fatal(method, e);
          }
          finally {
            DatabaseStatement.closeResultSet(resultSet);
          }
        }
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return taskMap;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeSet
  /**
   ** Returns the the {@link ChangeSet) with all the values.
   **
   ** @param  before             the state of the organizational
   **                            {@link Identity} representing the before image.
   ** @param  after              the state of the organizational
   **                            {@link Identity} representing the after image.
   ** @param  attributes         the {@link Set} of attributes that are changed.
   ** @param  mapping            the mapping of logical attribute names to
   **                            physical column names.
   **
   ** @return                    the {@link ChangeSet).
   */
  private ChangeSet changeSet(final Identity before, final Identity after, final Set<String> attributes, final Map<String, String> mapping) {
    final Map<String, String> oldValue = new HashMap<String, String>();
    final Map<String, String> newValue = new HashMap<String, String>();

    Set<String> changed = new HashSet<String>();
    for (String attribute : attributes) {
      final String physical = mapping.get(attribute);
      oldValue.put(physical, (String)before.getAttribute(attribute));
      newValue.put(physical, (String)after.getAttribute(attribute));
      changed.add(physical);
    }
    return new ChangeSet(changed, oldValue, newValue);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oldValue
  private String oldValue(final Identity currentUser, final Map<String, String> reverseAttributeMap, String taskName) {
    final String method = "oldValue";
    trace(method, SystemMessage.METHOD_ENTRY);

    String changedColumnName = null;
    Connection        connection = null;
    ResultSet         resultSet  = null;
    PreparedStatement statement  = null;
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      statement  = DatabaseStatement.createPreparedStatement(connection, "select lkv_encoded from lkv where lku_key=(select lku_key from lku where lku.lku_type_string_key=?) and lkv_decoded=?");
      statement.setString(1, TaskTrigger.REGISTRY);
      statement.setString(2, taskName);
      resultSet = statement.executeQuery();
      resultSet.next();
      changedColumnName = resultSet.getString(1);
    }
    catch (Exception e) {
      fatal(method, e);
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    String changedAttributeName = reverseAttributeMap.get(changedColumnName.toUpperCase());
    return currentUser.getAttribute(changedAttributeName) != null ? currentUser.getAttribute(changedAttributeName).toString() : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offlined
  private boolean offlined(final String taskKey) {
    final String method = "offlined";
    trace(method, SystemMessage.METHOD_ENTRY);

    boolean           offLined   = false;
    Connection        connection = null;
    ResultSet         resultSet  = null;
    PreparedStatement statement  = null;
    try {
      connection = DatabaseConnection.aquire(Platform.getOperationalDS());
      statement  = DatabaseStatement.createPreparedStatement(connection, "select mil.mil_offlined from mil mil where mil.mil_key =?");
      statement.setLong(1, Long.parseLong(taskKey));
      resultSet  = statement.executeQuery();
      resultSet.next();
      offLined = StringUtility.isEqual(resultSet.getString(1), "1");
    }
    catch (Exception e) {
      fatal(method, e);
      offLined = false;
    }
    finally {
      DatabaseStatement.closeResultSet(resultSet);
      DatabaseStatement.closeStatement(statement);
      DatabaseConnection.release(connection);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return offLined;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submitAsynchron
  private void submitAsynchron(final String processKey, final String taskKey, final String oldValue)
    throws Exception {

    final String method = "submitAsynchron";
    trace(method, SystemMessage.METHOD_ENTRY);
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submitSynchron
  private void submitSynchron(final String processKey, final String taskKey, final ChangeSet changeSet, final String oldValue, final boolean bulk) {
    final tcScheduleItem moScheduleItem = new tcScheduleItem(database(), "", processKey, taskKey, new byte[0], new byte[0]);
    moScheduleItem.setString("sch_status", "P");
    moScheduleItem.setString("osi_note", bulk ? changeSet.getXmlRepresentation() : oldValue);
    AuditEngine engine = AuditEngine.getAuditEngine(database());
    engine.pushReason("Event Handler", 0L);
    moScheduleItem.save();
    engine.popReason();
  }

  private void prepareEntityMapping(final Set<String> attributes, final Map<String, String> logical, final Map<String, String> physical)
    throws Exception {
    final EntityManager em  = service(EntityManager.class);
    Map<String, String> map = em.mapEntityAttributes(OrganizationManagerConstants.ORGANIZATION_ENTITY, attributes);
    for (String attribute : map.keySet()) {
      final String field = map.get(attribute);
      if (field != null) {
        logical.put(attribute, field.toUpperCase());
        physical.put(field.toUpperCase(), attribute);
      }
    }
  }
}