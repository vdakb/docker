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

    File        :   UserExpirationNotification.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserExpirationNotification.


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

import Thor.API.tcMetaDataSet;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import com.thortech.xl.dataobj.util.tcEmailNotificationUtil;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.scheduler.SchedulerBundle;
import oracle.iam.identity.scheduler.SchedulerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ProfileExpirationNotification
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProfileExpirationNotification</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler.
 ** <p>
 ** The task sends out email notifications all users about expiring, whose
 ** attributes <code>???</code> reached the date, at whom this task is executed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ProfileExpirationNotification extends ProfileExpiration {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProfileExpirationNotification</code> task that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProfileExpirationNotification() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution (AbstractSchedulerBaseTask)
  /**
   ** The entry point of the reconciliation task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  public void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);

    info(SchedulerBundle.format(SchedulerMessage.JOB_BEGIN, getName()));
    Date currentDate = new Date(System.currentTimeMillis());
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
      statement.setStatement(getDataBase(), "select usr_key, usr_mail, usr_rowver from usr where " + stringValue(TIMESTAMP_COLUMN) + " < ?  and usr_status = 'Active' and nvl(usr_pwd_expired, 0) = 0");
      statement.setDate(1, currentDate);
      if (isStopped())
        return;

      statement.execute();
      tcDataSet userDataSet = statement.getDataSet();
      int i = userDataSet.getRowCount();
      info(SchedulerBundle.format(SchedulerMessage.USER_AFFECTED, String.valueOf(i)));
      if (i > 0) {

        tcEmailNotificationUtil notification = new tcEmailNotificationUtil(getDataBase());

        tcDataSet dataSet = new tcDataSet();
        dataSet.setQuery(getDataBase(), "select " + stringValue(TIMESTAMP_COLUMN) + " from usr where 1=2");
        dataSet.executeQuery();
        if (isStopped())
          return;

        tcDataSet userMetaData = new tcDataSet();
        userMetaData.setQuery(getDataBase(), USER_QUERY);
        userMetaData.executeQuery();
        Map notified = new HashMap();
        notified.put(stringValue(NOTIFIED_COLUMN), stringValue(NOTIFIED_VALUE));

        for (int j = 0; j < i; ++j) {
          if (isStopped())
            return;
          userDataSet.goToRow(j);
          final String userkey = userDataSet.getString(USER_KEY);
          final String address = userDataSet.getString(USER_MAIL);
          if (!StringUtility.isEmpty(address)) {
            notification.constructEmail(stringValue(TEMPALTE), userkey);
            notification.sendEmail(address);
          }
          userMetaData.setString(USER_KEY, userkey);
          userMetaData.setByteArray(USER_VERSION, userDataSet.getByteArray(USER_VERSION));
          try {
            userFacade().updateUser(new tcMetaDataSet(userMetaData, getDataBase()), notified);
            info(SchedulerBundle.format(SchedulerMessage.SET_EXPIRING_INDICATOR, userDataSet.getString(USER_LOGIN)));
          }
          catch (Exception e) {
            error(method, e.getMessage());
          }
        }
      }
      updateLastReconciled();
      info(SchedulerBundle.format(SchedulerMessage.JOB_COMPLETE, getName()));
    }
    catch (Exception e) {
      final String[] arguments = { getImplementation(), e.getLocalizedMessage() };
      error(method, SchedulerBundle.format(SchedulerMessage.JOB_ABORT, arguments));
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}