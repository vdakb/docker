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

    File        :   UserExpirationWarning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UserExpirationWarning.


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

import oracle.iam.identity.scheduler.SchedulerMessage;
import oracle.iam.identity.scheduler.SchedulerBundle;

////////////////////////////////////////////////////////////////////////////////
// class UserExpirationWarning
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>UserExpirationWarning</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 ** <p>
 ** The task warns all users about expiring, whose attribute
 ** <code>???</code> reached the date, at whom this task is executed.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class UserExpirationWarning extends ProfileExpiration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserExpirationWarning</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserExpirationWarning() {
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

      tcEmailNotificationUtil emailNotification = new tcEmailNotificationUtil(getDataBase());
      PreparedStatementUtil   emailStatement    = new PreparedStatementUtil();
      emailStatement.setStatement(getDataBase(), EMAIL_QUERY);

      PreparedStatementUtil userStatement = new PreparedStatementUtil();
      // create a database statement to retrieve all user that match following
      // criterias
      // a) user is not yet deleted
      // b) user is marked as to deprovision but not yet deprovisioned
      userStatement.setStatement(getDataBase(), "select usr_key, usr_rowver from usr where " + stringValue(TIMESTAMP_COLUMN) + " < ?  and usr_status = 'Active' and nvl(usr_pwd_expired, 0) = 0");
      userStatement.setDate(1, currentDate);
      if (isStopped())
        return;

      userStatement.execute();
      tcDataSet userDataSet = userStatement.getDataSet();
      int i = userDataSet.getRowCount();
      info(SchedulerBundle.format(SchedulerMessage.USER_AFFECTED, String.valueOf(i)));
      if (isStopped())
        return;

      tcDataSet userMetaData = new tcDataSet();
      userMetaData.setQuery(getDataBase(), USER_QUERY);
      userMetaData.executeQuery();
      Map<String, String> notified = new HashMap<String, String>();
      notified.put(stringValue(NOTIFIED_COLUMN), stringValue(NOTIFIED_VALUE));
      for (int j = 0; j < i; ++j) {
        if (isStopped())
          return;
        userDataSet.goToRow(j);

        emailStatement.setLong(1, userDataSet.getLong(USER_KEY));
        emailStatement.execute();
        final String address = emailStatement.getDataSet().getString(USER_MAIL);
        if (!StringUtility.isEmpty(address)) {
          emailNotification.constructEmail(stringValue(TEMPALTE), userDataSet.getString(USER_KEY));
          emailNotification.sendEmail(address);
        }
        userMetaData.setByteArray(USER_VERSION, userDataSet.getByteArray(USER_VERSION));
        userMetaData.setDate(stringValue(TIMESTAMP_COLUMN), currentDate);
        userMetaData.setString(USER_KEY, userDataSet.getString(USER_KEY));
        if (isStopped())
          return;

        try {
          userFacade().updateUser(new tcMetaDataSet(userMetaData, getDataBase()), notified);
          info(SchedulerBundle.format(SchedulerMessage.SET_EXPIRING_INDICATOR, userDataSet.getString(USER_LOGIN)));
        }
        catch (Exception e) {
          error(method, e.getMessage());
        }
      }
      updateLastReconciled();
      info(SchedulerBundle.format(SchedulerMessage.JOB_COMPLETE, getName()));
    }
    catch (Exception e) {
      info(SchedulerBundle.format(SchedulerMessage.JOB_ABORT, getName()));
      throw new TaskException(e);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }
}