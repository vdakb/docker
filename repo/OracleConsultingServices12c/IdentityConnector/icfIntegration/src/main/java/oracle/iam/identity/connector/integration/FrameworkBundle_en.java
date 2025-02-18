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
    Subsystem   :   Connector Bundle Integration

    File        :   FrameworkBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FrameworkBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FrameworkBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FrameworkBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // ICF-00031 - 00040 connectivity related errors
    { FrameworkError.CONNECTION_UNKNOWN_HOST,        "Connector Server Host [%1$s] is unknown." }
  , { FrameworkError.CONNECTION_CREATE_SOCKET,       "Could create network connection to Connector Server at host [%1$s] on port [%2$s]." }
  , { FrameworkError.CONNECTION_SECURE_SOCKET,       "Could create secure network connection to Connector Server at host [%1$s] on port [%2$s]." }
  , { FrameworkError.CONNECTION_ERROR,               "Error encountered while connecting to Connector Server." }
  , { FrameworkError.CONNECTION_TIMEOUT,             "Connection to Connector Server at host [%1$s] on port [%2$s] got timed out: [%3$s]." }
  , { FrameworkError.CONNECTION_NOT_AVAILABLE,       "The problem may be with physical connectivity or Connector Server at host [%1$s] on port [%2$s] is not alive." }
  , { FrameworkError.CONNECTION_AUTHENTICATION,      "Credential is incorrect, system failed to get access with supplied credential." }

     // ICF-00041 - 00050 connector locator related errors
  , { FrameworkError.CONNECTOR_BUNDLE_NOTFOUND,      "Connector Bundle [%1$s] not found." }
  , { FrameworkError.CONNECTOR_OPTION_MAPPING,       "Configuration option [%1$s] is not transferable." }
  , { FrameworkError.CONNECTOR_OPTION_REQUIRED,      "Configuration option [%1$s] is required." }
  , { FrameworkError.CONNECTOR_OPTION_NOTFOUND,      "Value not found for configuration option [%1$s]." }

     // ICF-00051 - 00060 reconciliation thread pool related errors
  , { FrameworkError.RECONCILIATION_POOL_SIZE,       "Pool size has to be >= 1." }
  , { FrameworkError.RECONCILIATION_POOL_CLOSED,     "Pool was closed already, cannot add any more events." }
  , { FrameworkError.RECONCILIATION_POOL_FINISHED,   "Pool was closed already, cannot call finish again." }

     // ICF-01001 - 01010 script action related messages
  , { FrameworkMessage.SCRIPT_ACTION_EMPTY,           "No action to execute in phase %1$s." }
  , { FrameworkMessage.SCRIPT_ACTION_START,           "Executing action [%2$s] in phase %1$s ..." }
  , { FrameworkMessage.SCRIPT_ACTION_SUCCESS,         "Execution of action [%2$s] in phase %1$s succeeded." }
  , { FrameworkMessage.SCRIPT_ACTION_FAILED,          "Execution of action [%2$s] in phase %1$s failed." }

     // ICF-01011 - 01020 reconciliation thread pool related messages
  , { FrameworkMessage.RECONCILIATION_POOL_ADD,      "Event added to the pool at slot [%d]." }
  , { FrameworkMessage.RECONCILIATION_POOL_ADDED,    "Event %s added to the pool." }
  , { FrameworkMessage.RECONCILIATION_POOL_LIMITS,   "Pool reached limit, size is %d. Processing ..." }
  , { FrameworkMessage.RECONCILIATION_POOL_COMPLETE, "Pool is complete, submitting it to reconciliation engine, size of the pool is [%d]." }

     // ICF-01021 - 01030 reconciliation event related messages
  , { FrameworkMessage.RECONCILIATION_EVENT_REGULAR, "Process reconciliation event for object with UID [%s]." }
  , { FrameworkMessage.RECONCILIATION_EVENT_CREATED, "[%d] reconciliation events were successfully created." }
  , { FrameworkMessage.RECONCILIATION_EVENT_FAILED,  "[%d] reconciliation events are failed." }
  , { FrameworkMessage.RECONCILIATION_EVENT_DELETE,  "Process reconciliation delete event with id [%d]." }
  , { FrameworkMessage.RECONCILIATION_EVENT_NOTHING, "No accounts to be deleted." }
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