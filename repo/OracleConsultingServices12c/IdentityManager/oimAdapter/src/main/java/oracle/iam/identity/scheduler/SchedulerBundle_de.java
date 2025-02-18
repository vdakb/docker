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

    File        :   SchedulerBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    SchedulerBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.scheduler;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SchedulerBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SchedulerBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { SchedulerMessage.JOB_BEGIN,              "Executing Job %1$s ..." }
  , { SchedulerMessage.JOB_ABORT,              "Job %1$s aborted, due to %2$s" }
  , { SchedulerMessage.JOB_COMPLETE,           "Job %1$s completed" }

  , { SchedulerMessage.USER_NOTFOUND,          "The User with User Login %1$s does not exists" }
  , { SchedulerMessage.NOTPROVISIONED,         "The %1$s %2$s is not provisioned to any %3$s" }

  , { SchedulerMessage.USER_AFFECTED,          "Number of affected users are %1$s" }
  , { SchedulerMessage.USER_DELETED,           "User %1$s deleted" }
  , { SchedulerMessage.SET_DEPROVISONED_DATE,  "Deprovisioned date set to today to user %1$s" }
  , { SchedulerMessage.SET_EXPIRING_INDICATOR, "Expiring indicator set to user %1$s" }
  , { SchedulerMessage.DATE_COMPARISON,        "Dates to compare: requestedDate %1$s --> processDate %2$s -->  will be %3$s" }
  };

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }
}