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

    File        :   ExpiredResourceReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ExpiredResourceReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcFormInstanceOperationsIntf;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.naming.User;
import oracle.iam.identity.foundation.naming.ResourceObject;
import oracle.iam.identity.foundation.naming.ProcessDefinition;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.scheduler.SchedulerMessage;
import oracle.iam.identity.scheduler.SchedulerBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class ExpiredResourceReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ExpiredResourceReconciliation</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class ExpiredResourceReconciliation extends CustomScheduledTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify the action
   ** that should take place if the resource is expired.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String RECONCILE_ACTION   = "Reconciliation Action";

  /**
   ** Attribute tag which must be defined on this task to specify the field of
   ** the process form to check the expiration.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String RECONCILE_FIELD    = "Reconciliation Field";

  /**
   ** Attribute tag which must be defined on this task to specify the format
   ** of the field of the process form to check the expiration.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String RECONCILE_FORMAT   = "Reconciliation Field Format";

  /**
   ** Attribute tag which must be defined on this task to specify the matching
   ** rule applied to the field of the process form to check the expiration.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String RECONCILE_MATCH    = "Reconciliation Field Match";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final String[] matchingRules   = { "yesterday", "today"};

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected tcObjectOperationsIntf       objectFacade = null;
  protected tcFormInstanceOperationsIntf formFacade   = null;

  protected SimpleDateFormat             dateFormat   = null;

  /** the default matching rule applied to the reconciliation field */
  protected String                       matchingRule = matchingRules[1];

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class TaskData
  // ~~~~~ ~~~~~~~~
  /**
   ** Member class to hold the user object record
   */
  class TaskData {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final long   userKey;
    protected final long   objectInstance;
    protected final long   processInstance;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     **
     */
    TaskData(long userKey, long objectInstance, long processInstance) {
      this.userKey         = userKey;
      this.objectInstance  = objectInstance;
      this.processInstance = processInstance;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ExpiredResourceReconciliation</code> task
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ExpiredResourceReconciliation() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** This method is invoked just before the thread operation will be executed.
   **
   ** @throws TaskException      the exception thrown if any goes wrong
   */
  public void initialize()
    throws TaskException {


    final String method = "initialize";
    debug(method, TaskBundle.format(TaskMessage.TASK_PARAMETER, this.toString()));

    // check the validity of the provided task attributes
    this.matchingRule = stringValue(RECONCILE_MATCH);

    boolean ruleValid = false;
    for (int i = 0; i < matchingRules.length; i++) {
      if (matchingRules[i].equalsIgnoreCase(this.matchingRule)) {
        ruleValid = true;
        break;
      }
    }

    if (!ruleValid)
      throw new TaskException(TaskError.ARGUMENT_BAD_VALUE, RECONCILE_MATCH);

    this.dateFormat = new SimpleDateFormat(stringValue(RECONCILE_FORMAT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution (overridden)
  /**
   ** The call back method just invoked before reconciliation takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void beforeExecution()
    throws TaskException {

    // ensure inheritance
    super.beforeExecution();

    // initialize the remote interface for the server conversation
    this.objectFacade = this.objectFacade();
    this.formFacade   = this.formInstanceFacade();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution (overridden)
  /**
   ** The call back method just invoked after execution finished.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void afterExecution()
    throws TaskException {

    if (this.objectFacade != null) {
      this.objectFacade.close();
      this.objectFacade = null;
    }

    if (this.formFacade != null) {
      this.formFacade.close();
      this.formFacade = null;
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processMatch
  /**
   ** Returns the process data associated with an instance of a process in the
   ** system. If the process data has not been saved yet, then it returns any
   ** values that need to be pre-filled into the form, according to rules or
   ** data flows.
   **
   ** @param  processInstance    the key of the process instance in the system.
   ** @param  requestedDate      the date values as the actions happened.
   **
   ** @return                    a {@link tcResultSet} containing one row,
   **                            holding data used by the provisioning process.
   **
   ** @throws Exception         if the process form data could not be retrieved.
   */
  protected boolean processMatch(final long processInstance, final Date requestedDate)
    throws Exception {

    final String method = "processMatch";
    trace(method, SystemMessage.METHOD_ENTRY);

    tcResultSet resultSet = null;
    try {
      resultSet = this.formFacade.getProcessFormData(processInstance);

      Date processDate = null;
      try {
        processDate = this.dateFormat.parse(resultSet.getStringValue(stringValue(RECONCILE_FIELD)));
      }
      catch (ParseException e) {
        // getting this exception we assuming that we have a date what is in the
        // future (a date like 9999-01-01)
        Calendar c = new GregorianCalendar();
        c.setTime(requestedDate);
        c.add(Calendar.DAY_OF_MONTH, 1);
        processDate = c.getTime();
      }
      int result = processDate.compareTo(requestedDate);
      String[] dates = {requestedDate.toString(), processDate.toString(), String.valueOf(result)};
      debug(method, SchedulerBundle.format(SchedulerMessage.DATE_COMPARISON, dates));
      // values are equal
      return (result < 1);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findObjectInstance
  /**
   ** Returns the internal identifier of all users that match the provided
   ** filter.
   **
   ** @param  objectKey          the internal key of a resource.
   **
   ** @return                    a {@link Map} with the users that has the
   **                            resource provisioned.
   **                            The {@link Map} is build with the internal key
   **                            as the hash key and the login name of each user
   **                            as the payload.
   */
  protected List<TaskData> findObjectInstance(long objectKey) {

    final String method = "findObjectInstance";
    trace(method, SystemMessage.METHOD_ENTRY);

    // build the filter for retrieving the provisioned resource objects
    // we are interested in all resource there status is in Provisoned or Enabled
    Map<String, String>  filter = new HashMap<String, String>(1);
    filter.put(ResourceObject.STATUS, ResourceObject.STATUS_PROVISIONED);

    List<TaskData> result = null;
    final Date currentDate = requestedDate();
    try {
      tcResultSet resultSet = this.objectFacade.getAssociatedUsers(objectKey, filter);
      result = new ArrayList<TaskData>(resultSet.getRowCount());
      for (int i = 0; i < resultSet.getRowCount(); i++) {
        resultSet.goToRow(i);
        if (processMatch(resultSet.getLongValue(ProcessDefinition.INSTANCE_KEY), currentDate)) {
          TaskData data = new TaskData(resultSet.getLongValue(User.KEY), resultSet.getLongValue(ResourceObject.OBJECT_INSTANCE), resultSet.getLongValue(ProcessDefinition.INSTANCE_KEY));
          result.add(data);
        }
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }

    filter.clear();
    filter.put(ResourceObject.STATUS, ResourceObject.STATUS_ENABLED);
    try {
      tcResultSet resultSet = this.objectFacade.getAssociatedUsers(objectKey, filter);
      for (int i = 0; i < resultSet.getRowCount(); i++) {
        resultSet.goToRow(i);
        if (processMatch(resultSet.getLongValue(ProcessDefinition.INSTANCE_KEY), currentDate)) {
          TaskData data = new TaskData(resultSet.getLongValue(User.KEY), resultSet.getLongValue(ResourceObject.OBJECT_INSTANCE), resultSet.getLongValue(ProcessDefinition.INSTANCE_KEY));
          result.add(data);
        }
      }
    }
    catch (Exception e) {
      fatal(method, e);
    }
    trace(method, SystemMessage.METHOD_EXIT);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   requestedDate
  /**
   ** Evaluates the value of the date to compare.
   ** <p>
   ** The result is trimmed to a date with the time value of midnight.
   **
   ** @return                    the value of the date to compare
   */
  protected Date requestedDate() {
    Calendar cal = new GregorianCalendar();

    if (stringValue(RECONCILE_MATCH).equals(matchingRules[0]))
      cal.add(Calendar.DAY_OF_MONTH, -1);

    // Set hour, minute, second, millis to midnight.
    cal.set(Calendar.HOUR,        0);
    cal.set(Calendar.MINUTE,      0);
    cal.set(Calendar.SECOND,      0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal.getTime();
  }
}