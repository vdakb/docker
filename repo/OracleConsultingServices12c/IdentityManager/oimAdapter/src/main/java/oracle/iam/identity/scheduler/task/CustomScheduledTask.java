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

    File        :   CustomScheduledTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CustomScheduledTask.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import oracle.iam.identity.foundation.reconciliation.AbstractReconciliationTask;

////////////////////////////////////////////////////////////////////////////////
// class CustomScheduledTask
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>CustomScheduledTask</code> implements the base functionality
 ** of a service end point for the Oracle Identity Manager Scheduler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class CustomScheduledTask extends AbstractReconciliationTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  public static final String    LOGGER_CATEGORY          = "OCS.SCHEDULER.TASK";

  /** Name of the dataaccess object of a user entry. */
  protected static final String USER                     = "usr";

  /** Column name of the system identifier of a user entry. */
  protected static final String USER_KEY                 = "usr_key";

  /** Column name of the unique identifier of a user entry. */
  protected static final String USER_LOGIN               = "usr_login";

  /** Column name of the status of a user entry. */
  protected static final String USER_STATUS              = "usr_status";

  /** Column name of the locking status of a user entry. */
  protected static final String USER_LOCKED              = "usr_locked";

  /** Column name of the eMail address of a user entry. */
  protected static final String USER_MAIL                = "usr_email";

  /** Column name of the first name of a user entry. */
  protected static final String USER_FIRST_NAME          = "usr_first_name";

  /** Column name of the last name of a user entry. */
  protected static final String USER_LAST_NAME           = "usr_last_name";

  /** Column name of the last name of a user entry. */
  protected static final String USER_PASSWORD_EXPIRED    = "usr_pwd_expired";

  /** Column name of the date a user entry has to be deprovisioned. */
  protected static final String USER_DEPROVISIONING_DATE = "usr_deprovisioning_date";

  /** Column name of the date a user entry was deprovisioned. */
  protected static final String USER_DEPROVISIONED_DATE  = "usr_deprovisioned_date";

  /** Column name of the row version of a user entry. */
  protected static final String USER_VERSION             = "usr_rowver";

  protected static final String USER_QUERY               = "select usr_key, usr_rowver from usr where 1=2";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CustomScheduledTask</code> task adapter that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CustomScheduledTask() {
    // ensure inheritance
    super(LOGGER_CATEGORY);
  }
}