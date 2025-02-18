/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   HarvesterBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HarvesterBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.ots.service.catalog.HarvesterError;
import oracle.iam.identity.ots.service.catalog.HarvesterMessage;

////////////////////////////////////////////////////////////////////////////////
// class HarvesterBundle_en
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class HarvesterBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // ARC-00001 - ARC-00010 task related errors
    { HarvesterError.UNHANDLED,                  "Unhandled exception occured: %1$s" }
  , { HarvesterError.GENERAL,                    "Encounter some problems: %1$s" }
  , { HarvesterError.ABORT,                      "Execution aborted: %1$s"}
  , { HarvesterError.NOTIMPLEMENTED,             "Feature is not yet implemented"}

     // ARC-00011 - ARC-00020 import process related erorrs
  , { HarvesterError.CATALOG_NOTFOUND,           "Harvester item of type [%1$s] and identifier [%2$s] does not exists" }
  , { HarvesterError.CATALOG_AMBIGUOUS,          "Harvester item of type [%1$s] and identifier [%2$s] defined ambigously" }
  , { HarvesterError.ROLE_NOTFOUND,              "Role [%1$s] does not exists" }
  , { HarvesterError.ROLE_AMBIGUOUS,             "Role [%1$s] defined ambigously" }
  , { HarvesterError.INSTANCE_MANDATORY,         "One or more application instances required" }
  , { HarvesterError.INSTANCE_NOTFOUND,          "Application Instance [%1$s] does not exists" }
  , { HarvesterError.INSTANCE_AMBIGUOUS,         "Application Instance [%1$s] defined ambigously" }
  , { HarvesterError.INSTANCE_LOOKUP_NOTFOUND,   "Application Instance not exists for Resource Object [%1$s] and IT Resource [%2$s]" }
  , { HarvesterError.INSTANCE_LOOKUP_AMBIGUOUS,  "Application Instance ambigously defined for Resource Object [%1$s] and IT Resource [%2$s]" }
  , { HarvesterError.NAMEPACE_NOTFOUND,          "Entitlement Namespace [%1$s] does not exists in Application Instance [%2$s]" }
  , { HarvesterError.NAMEPACE_AMBIGUOUS,         "Entitlement Namespace [%1$s] defined ambigously in Application Instance [%2$s]" }
  , { HarvesterError.ENTITLEMENT_NOTFOUND,       "Entitlement [%1$s] does not exists in Application Instance Namespace [%2$s]" }
  , { HarvesterError.ENTITLEMENT_AMBIGUOUS,      "Entitlement [%1$s] defined ambigously in Application Instance Namespace [%2$s]" }
  , { HarvesterError.ENTITLEMENT_MANDATORY,      "One or more entitlements required" }

     // ARC-00031 - ARC-00040 import process related erorrs
  , { HarvesterError.MODIFY_CATALOG,             "Unable to modify Catalog [%1$s] [%2$s]" }
  , { HarvesterError.MODIFY_CATALOG_IGNORED,     "Modification of Catalog [%1$s] [%2$s] ignored" }
  , { HarvesterError.MODIFY_ROLE,                "Unable to modify Role [%1$s]" }
  , { HarvesterError.MODIFY_ROLE_IGNORED,        "Modification of Role [%1$s] ignored" }
  , { HarvesterError.MODIFY_INSTANCE,            "Unable to modify Application Instance [%1$s]" }
  , { HarvesterError.MODIFY_INSTANCE_IGNORED,    "Modification of Application Instance [%1$s] ignored" }
  , { HarvesterError.MODIFY_ENTITLEMENT,         "Unable to modify Entitlement [%1$s]" }
  , { HarvesterError.MODIFY_ENTITLEMENT_IGNORED, "Modification of Entitlement [%1$s] ignored" }

     // ARC-00041 - ARC-00050 import process related errors
  , { HarvesterError.OBJECT_ELEMENT_NOTFOUND,    "%1$s [%2$s] does not exists" }
  , { HarvesterError.OBJECT_ELEMENT_EXISTS,      "%1$s [%2$s] already exists in the system. Performing update only on object" }
  , { HarvesterError.OBJECT_ELEMENT_AMBIGUOUS,   "%1$s [%2$s] defined ambigously" }
  , { HarvesterError.OBJECT_ATTRIBUTE_MISSING,   "Not enough attributes to create %1$s [%2$s]" }

     // ARC-00051 - ARC-00060 import process related errors
  , { HarvesterError.OPERATION_UNSUPPORTED,      "Action [%1$s] is not applicable on [%2$s]" }
  , { HarvesterError.OPERATION_EXPORT_FAILED,    "Export of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_IMPORT_FAILED,    "Import of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_CREATE_FAILED,    "Creation of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_DELETE_FAILED,    "Deletion of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_ENABLE_FAILED,    "Enable of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_DISABLE_FAILED,   "Disable of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_MODIFY_FAILED,    "Modification of %1$s [%2$s] failed. Reason:\n%3$s"}
  , { HarvesterError.OPERATION_ASSIGN_FAILED,    "Assignment of %1$s [%2$s] to %3$s [%4$s] failed. Reason:\n%5$s"}
  , { HarvesterError.OPERATION_REVOKE_FAILED,    "Revocation of %1$s [%2$s] from %3$s [%4$s] failed. Reason:\n%5$s"}

     // ARC-00061 - ARC-00070 request payload related errors
  , { HarvesterError.REQUEST_PAYLOAD_EMPTY,      "Payload is empty" }
  , { HarvesterError.REQUEST_PAYLOAD_INCOMPLETE, "Payload violates XML schema" }
  , { HarvesterError.REQUEST_APPLICATION_MISSED, "One or more application instances couldn't be transformed" }
  , { HarvesterError.REQUEST_NAMESPACE_MISSED,   "One or more entitlement namespaces couldn't be transformed" }
  , { HarvesterError.REQUEST_ENTITLEMENT_MISSED, "One or more entitlements couldn't be transformed" }


     // ARC-01001 - ARC-01010 import process related messages
  , { HarvesterMessage.IMPORTING_BEGIN,          "[%2$s] for [%1$s] using file [%3$s] started ..." }
  , { HarvesterMessage.IMPORTING_COMPLETE,       "[%2$s] for [%1$s] using file [%3$s] completed" }
  , { HarvesterMessage.IMPORTING_STOPPED,        "[%2$s] for [%1$s] using file [%3$s] stopped with [%4$s]" }
  , { HarvesterMessage.IMPORTING_SUCCESS,        "%1$s entries was imported for [%2$s]" }
  , { HarvesterMessage.IMPORTING_ERROR,          "Entry [%1$s] was not imported for [%2$s]" }
  , { HarvesterMessage.IMPORT_BEGIN,             "Starting import of data for [%1$s] ..." }
  , { HarvesterMessage.IMPORT_COMPLETE,          "Import of data for [%1$s] completed" }
  , { HarvesterMessage.IMPORT_SKIP,              "Import skipped due to user request" }
  , { HarvesterMessage.IMPORT_CATALOG_SUMMARY,   "[%1$s] catalog entries are modified, [%2$s] catalog entries are ignored, [%3$s] catalog entries are failed" }

     // ARC-01011 - ARC-01020 export process related messages
  , { HarvesterMessage.EXPORTING_BEGIN,          "[%2$s] for [%1$s] using file [%3$s] started ..." }
  , { HarvesterMessage.EXPORTING_COMPLETE,       "[%2$s] for [%1$s] using file [%3$s] completed" }
  , { HarvesterMessage.EXPORTING_STOPPED,        "[%2$s] for [%1$s] using file [%3$s] stopped" }
  , { HarvesterMessage.EXPORTING_SUCCESS,        "%1$s entries was exported for [%2$s]" }
  , { HarvesterMessage.EXPORTING_ERROR,          "Entry [%1$s] was not exported for [%2$s]" }

  , { HarvesterMessage.COLLECTING_BEGIN,         "Starting collecting data ..." }
  , { HarvesterMessage.COLLECTING_COMPLETE,      "Collecting data completed" }

     // ARC-01031 - ARC-01040 object/entity operation related messages
  , { HarvesterMessage.OPERATION_CREATE_BEGIN,   "Creating %1$s [%2$s] ..."}
  , { HarvesterMessage.OPERATION_CREATE_SUCCESS, "%1$s [%2$s] created"}
  , { HarvesterMessage.OPERATION_MODIFY_BEGIN,   "Modifying %1$s [%2$s] ..."}
  , { HarvesterMessage.OPERATION_MODIFY_SUCCESS, "%1$s [%2$s] modified"}

     // ARC-01041 - ARC-01050 web service operation related messages
  , { HarvesterMessage.SERVICE_REQUEST_PAYLOAD,  "Service will be invoked with request payload\n%1$s"}
  , { HarvesterMessage.SERVICE_RESPONSE_PAYLOAD, "Service response payload is\n%1$s"}
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