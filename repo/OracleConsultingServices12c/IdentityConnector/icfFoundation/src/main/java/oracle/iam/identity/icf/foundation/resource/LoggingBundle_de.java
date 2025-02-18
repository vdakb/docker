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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   LoggingBundle_de.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LoggingBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.resource;

import oracle.iam.identity.icf.foundation.logging.LoggerError;
import oracle.iam.identity.icf.foundation.logging.LoggerMessage;

////////////////////////////////////////////////////////////////////////////////
// class LoggingBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class LoggingBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OCL-00061 - 00070 system timer related errors
    { LoggerError.TIMER_UNSUPPORTED,   "Task info is not being kept!" }
  , { LoggerError.TIMER_STILL_RUNNING, "Can't start SystemWatch: it's already running" }
  , { LoggerError.TIMER_NOT_RUNNING,   "Can't stop SystemWatch: it's not running" }
  , { LoggerError.TIMER_NO_TASK,       "No tests run: can't get last interval" }
  , { LoggerError.TIMER_TASK_SWITCH,   "The task list is always kept to be able to calculate the min, max and average" }

     // OCL-01001 - 01010 system timer related messages
  , { LoggerMessage.TIMER_SUMMARY,     "\n\n%1$s: Verbrauch insgesamt [ms] = %2$s\n"}
  , { LoggerMessage.TIMER_ELAPSED,     "Verbrauch [ms]" }
  , { LoggerMessage.TIMER_PERCENT,     "    [%]" }
  , { LoggerMessage.TIMER_COUNTER,     "    [#]" }
  , { LoggerMessage.TIMER_METHOD,      "Thread::Class-Method" }
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