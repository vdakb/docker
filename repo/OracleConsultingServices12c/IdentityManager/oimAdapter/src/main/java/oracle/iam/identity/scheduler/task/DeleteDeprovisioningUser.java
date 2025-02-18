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

    File        :   DeleteDeprovisioningUser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DeleteDeprovisioningUser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import java.util.Map;
import java.util.HashMap;

import java.sql.Date;
import java.sql.Timestamp;

import Thor.API.tcMetaDataSet;

import Thor.API.Operations.tcUserOperationsIntf;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.orb.dataaccess.tcDataAccessException;

import oracle.hst.foundation.SystemMessage;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskAttribute;

import oracle.iam.identity.scheduler.SchedulerMessage;
import oracle.iam.identity.scheduler.SchedulerBundle;

////////////////////////////////////////////////////////////////////////////////
// class DeleteDeprovisioningUser
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>DeleteDeprovisioningUser</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 ** <p>
 ** The task deletes all users, whose attributes
 ** <code>Users.Deprovisioning Date</code> reached the date, at whom this task
 ** is executed.
 ** <br>
 ** The task raise a reconciliation event to delete the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class DeleteDeprovisioningUser extends CustomScheduledTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify if an affected
   ** user should also be deleted.
   ** <br>
   ** This attribute is mandatory.
   */
  public static final String DELETE_USER         = "Delete User";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /** the task attribute to specifiy if the user should also be deleted */
    TaskAttribute.build(DELETE_USER, TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,   TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  tcUserOperationsIntf userFacade = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DeleteDeprovisioningUser</code> task adapter
   ** that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DeleteDeprovisioningUser() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes (AbstractSchedulerBaseTask)
  /**
   ** Returns the array with names which should be populated from the
   ** scheduled task definition of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   */
  protected TaskAttribute[] attributes() {
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  public void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(SchedulerBundle.format(SchedulerMessage.JOB_BEGIN, getName()));
    Date currentDate    = new Date(lastReconciled().getTime());
    // remember the last execution time as the timestamp on which this job has
    // last reconciled at start
    // setting it at this time that we have next time this scheduled task will
    // run the changes made during the execution of this task
    // Database Server current time is different from this system time
    lastReconciled(System.currentTimeMillis());
    try {
      // check if wo should not go further
      if (isStopped())
        return;

      PreparedStatementUtil statement = new PreparedStatementUtil();
      // create a database statement to retrieve all user that match following
      // criterias
      // a) user is not yet deleted
      // b) user is marked as to deprovision but not yet deprovisioned
      statement.setStatement(getDataBase(), "select usr_key, usr_login, usr_rowver from usr where usr_deprovisioning_date < ? and usr_deprovisioned_date is null");
      statement.setDate(1, currentDate);
      statement.execute();
      tcDataSet dataset = statement.getDataSet();
      int i = dataset.getRowCount();
      debug(method, SchedulerBundle.format(SchedulerMessage.USER_AFFECTED, String.valueOf(i)));

      // check if wo should not go further
      if (!isStopped() && i > 0)
        processSubject(dataset);

      updateLastReconciled();
    }
    catch (tcDataAccessException e) {
      throw new TaskException(e);
    }
    catch (tcDataSetException e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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

    this.userFacade = userFacade();
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

    if (this.userFacade != null) {
      this.userFacade.close();
      this.userFacade = null;
    }

    // ensure inheritance
    super.afterExecution();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for an particular
   ** subject.
   **
   ** @param  subject            the {@link tcDataSet} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  private void processSubject(final tcDataSet subject)
    throws TaskException {

    // check if the current thread is able to execute or a stop
    // signal is pending
    if (isStopped())
       return;

    final String method = "processSubject";
    trace(method, SystemMessage.METHOD_ENTRY);

    Map<String, String> data = new HashMap<String, String>();
    // reconcile data
    final Date currentDate = new Date(System.currentTimeMillis());
    try {
      for (int j = 0; j < subject.getRowCount(); j++) {
        if (isStopped())
          return;

        subject.goToRow(j);

        tcDataSet userAttributes = new tcDataSet();
        userAttributes.setQuery(getDataBase(), USER_QUERY);
        userAttributes.executeQuery();
        userAttributes.setByteArray(USER_VERSION, subject.getByteArray(USER_VERSION));
        userAttributes.setString(USER_KEY, subject.getString(USER_KEY));
        data.put(USER_DEPROVISIONED_DATE, (new Timestamp(currentDate.getTime())).toString());
        if (isStopped())
          return;

        final String username = subject.getString(USER_LOGIN);
        this.userFacade.updateUser(new tcMetaDataSet(userAttributes, getDataBase()), data);
        info(SchedulerBundle.format(SchedulerMessage.SET_DEPROVISONED_DATE, username));

        if (booleanValue(DELETE_USER)) {
          this.userFacade.deleteUser(subject.getLong(USER_KEY));
          info(SchedulerBundle.format(SchedulerMessage.USER_DELETED, username));
        }
      }
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}