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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Bundle_en.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_en.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestMessage;

////////////////////////////////////////////////////////////////////////////////
// class Bundle_en
// ~~~~~ ~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // FIM-00001 - 00010 system related errors
    { RequestError.GENERAL,                        "General error: %1$s" }
  , { RequestError.UNHANDLED,                      "An unhandled exception has occured: %1$s"}
  , { RequestError.ABORT,                          "Execution aborted due to reason: %1$s"}
  , { RequestError.NOTIMPLEMENTED,                 "Feature is not yet implemented."}

     // FIM-00011 - 00020 method argument related errors
  , { RequestError.ARGUMENT_IS_NULL,               "Passed argument %1$s must not be null." }
  , { RequestError.ARGUMENT_BAD_TYPE,              "Passed argument %1$s has a bad type." }
  , { RequestError.ARGUMENT_BAD_VALUE,             "Passed argument %1$s contains an invalid value." }
  , { RequestError.ARGUMENT_SIZE_MISMATCH,         "Passed argument array size dont match expected length." }

     // FIM-00021 - 00030 instance attribute related errors
  , { RequestError.ATTRIBUTE_IS_NULL,              "State of attribute %1$s must not be null." }

     // FIM-00032 - 00040 file related errors
  , { RequestError.FILE_MISSING,                   "Encountered problems to find file %1$s." }
  , { RequestError.FILE_IS_NOT_A_FILE,             "%1$s is not a file." }
  , { RequestError.FILE_OPEN,                      "Encountered problems to open file %1$s." }
  , { RequestError.FILE_CLOSE,                     "Encountered problems to close file %1$s." }
  , { RequestError.FILE_READ,                      "Encountered problems reading file %1$s." }
  , { RequestError.FILE_WRITE,                     "Encountered problems writing file %1$s." }

     // FIM-00041 - 00050 request related errors
  , { RequestError.REQUEST_CONFIGURATION_PROPERTY, "Cannot configure request model due to System Property %1$s is missing." }
  , { RequestError.REQUEST_CONFIGURATION_STREAM,   "Cannot configure request model due to stream error %1$s." }
  , { RequestError.REQUEST_CONFIGURATION_PARSING,  "Cannot configure request model due to parser error %1$s." }
  , { RequestError.REQUEST_APPLICATION_LOOKUP,     "<html><body>Lookup for Application <b>%1$s</b> failed.</body></html>" }
  , { RequestError.REQUEST_APPLICATION_NOTFOUND,   "<html><body>Application <b>%1$s</b> not found.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_NOTFOUND,   "<html><body>Entitlement <b>%1$s</b> not found.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_AMBIGUOUS,  "<html><body>Entitlement <b>%1$s</b> ambiguously defined.</body></html>" }
  , { RequestError.REQUEST_SELECTION_VIOLATED,     "<html><body>Please select an account template in environment <b>%1$s</b>.</body></html>" }
  , { RequestError.REQUEST_PREDECESSOR_VIOLATED,   "<html><body>Please request an <b>regular</b> account in environment <b>%1$s</b> for %2$s<br/>before you request an account as <b>%3$s</b> in the enviroment.</body></html>" }
  , { RequestError.REQUEST_FAILED,                 "<html><body>Submitting request of <b>%2$s</b> in <b>%1$s</b> for user %3$s failed due to error<br/><br/>%4$s<br/>.</body></html>" }

     // FIM-01041 - 01050 request related messages
  , { RequestMessage.REQUEST_SUBMIT_SUCCESS,       "Request %1$s was submitted for approval." }
  , { RequestMessage.REQUEST_COMPLETE_SUCCESS,     "Request for access completed successfully." }

  // EVAL-00051 - 00060 evaluation related errors
  , { RequestError.EVALUATION_INTERNAL_ERROR,      "Access policy evaluation has failed due to an internal error." }
  , { RequestError.EVALUATION_UNKNOWN_ERROR,       "Access policy evaluation has produced an unknown result. Process: %1$s." }

  // EVAL-01051 - 01060 evaluation related messages
  , { RequestMessage.EVALUATION_SUCCEEDED,         "Access policy evaluation has been successfully completed." }
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