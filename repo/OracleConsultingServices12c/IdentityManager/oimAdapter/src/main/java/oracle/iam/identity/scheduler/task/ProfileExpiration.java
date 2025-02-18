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

    File        :   ProfileExpiration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProfileExpiration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler.task;

import oracle.iam.identity.foundation.TaskAttribute;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProfileExpiration
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>ProfileExpiration</code> implements the base functionality of a
 ** service end point for the Oracle Identity Manager Scheduler.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class ProfileExpiration extends CustomScheduledTask {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this task to specify the template
   ** that is used to generate the eMail Notification.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String TEMPALTE            = "Email Definition Name";

  /**
   ** Attribute tag which must be defined on this task to specify the threshold
   ** the user has to be notified about upcoming expiration.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String TIMESTAMP_THRESHOLD = "TimeStamp Threshold";

  /**
   ** Attribute tag which must be defined on this task to specify the column
   ** that is used to check for expiration.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String TIMESTAMP_COLUMN    = "TimeStamp Field";

  /**
   ** Attribute tag which must be defined on this task to specify the column
   ** that indicates the user was already informed.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String NOTIFIED_COLUMN     = "Notified Field";

  /**
   ** Attribute tag which must be defined on this task to specify the value that
   ** has to be written to the indicator field.
   ** <br>
   ** This attribute is mandatory.
   */
  protected static final String NOTIFIED_VALUE      = "Notified Value";

  /**
   ** The query used to obtain the e-Mail address of a user profile
   */
  protected static final String EMAIL_QUERY         = "select usr.usr_login, usr.usr_email from usr usr where usr.usr_key = ?";

  /**
   ** the vector with attributes which are valid to define on the task
   ** definition
   */
  private static final TaskAttribute[] attribute  = {
    /** the task attribute to specifiy if eMail template */
    TaskAttribute.build(TEMPALTE,            TaskAttribute.MANDATORY)
    /** the task attribute that holds the value of the last run */
  , TaskAttribute.build(TIMESTAMP,           TaskAttribute.MANDATORY)
    /** the task attribute to specifiy if timestamp threshold */
  , TaskAttribute.build(TIMESTAMP_THRESHOLD, TaskAttribute.MANDATORY)
    /** the task attribute to specifiy if eMail template */
  , TaskAttribute.build(TIMESTAMP_COLUMN,    TaskAttribute.MANDATORY)
    /** the task attribute to specifiy if a user is already informed */
  , TaskAttribute.build(NOTIFIED_COLUMN,     TaskAttribute.MANDATORY)
    /** the task attribute to specifiyf the value that user is already informed */
  , TaskAttribute.build(NOTIFIED_VALUE,      TaskAttribute.MANDATORY)
  };

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ProfileExpiration</code> task that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ProfileExpiration() {
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
}