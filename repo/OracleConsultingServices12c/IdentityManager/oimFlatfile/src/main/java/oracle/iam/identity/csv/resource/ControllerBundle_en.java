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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   CSV Flatfile Connector

    File        :   ControllerBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ControllerBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.csv.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.csv.service.ControllerMessage;
import oracle.iam.identity.csv.service.provisioning.ProvisioningMessage;

////////////////////////////////////////////////////////////////////////////////
// class ControllerBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class ControllerBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // CSV-00021 - 00030 file handling related messages
    { ControllerMessage.CREATING_URL,            "About to create URL to %1$s" }
  , { ControllerMessage.CREATING_FOLDER,         "About to create file handle to %1$s" }
  , { ControllerMessage.CREATING_FILE,           "About to create file handle to %1$s" }

     // CSV-00031 - 00040 provisioning process related messages
  , { ControllerMessage.COMMENT_START,           " --- Provisioning of %1$s startet %2$s ---" }
  , { ControllerMessage.COMMENT_END,             " --- Provisioning of %1$s completed at %2$s ---" }

     // CSV-01081 - 01090 lookup reconciliation related messages
  , { ControllerMessage.CREATE_VALUE,            "Not able to add [%2$s] to Lookup Definition [%1$s]." }
  , { ControllerMessage.REMOVE_VALUE,            "Not able to remove [%2$s] from Lookup Definition [%1$s]." }
  , { ControllerMessage.DUPLICATE_VALUE,         "New value [%2$s] is same as existing value in Lookup Definition [%1$s]." }

     // CSV-01091 - 01100 reconciliation process related messages
  , { ControllerMessage.RECONCILE_RESOLVABLES,   "Process remaining resolved dependency entries for %1$s" }
  , { ControllerMessage.RECONCILE_UNRESOLVABLES, "Process remaining unresolved dependency entries for %1$s" }
  , { ControllerMessage.RECONCILE_REMAININGROOT, "Process remaining entries for %1$s" }
  , { ControllerMessage.RECONCILE_REMAININGNODE, "Process depended entry %1$s" }

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