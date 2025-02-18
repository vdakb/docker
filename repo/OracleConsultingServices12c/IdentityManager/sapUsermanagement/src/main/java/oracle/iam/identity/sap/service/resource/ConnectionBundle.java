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
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   ConnectionBundle.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectionBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.service.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.sap.control.ConnectionError;
import oracle.iam.identity.sap.control.ConnectionMessage;

////////////////////////////////////////////////////////////////////////////////
// class ConnectionBundle
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class ConnectionBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][]            = {
     // SAP-00001 - 00010 operations related errors
    { ConnectionError.UNHANDLED,                      "Unhandled exception occured: {0}" }
  , { ConnectionError.GENERAL,                        "Encounter some problems: {0}" }
  , { ConnectionError.ABORT,                          "Execution aborted: {0}"}

     // SAP-00011 - 00015 method argument related errors
  , { ConnectionError.ARGUMENT_IS_NULL,               "Passed argument \"{0}\" must not be null" }
  , { ConnectionError.ARGUMENT_BAD_TYPE,              "Passed argument \"{0}\" has a bad type" }
  , { ConnectionError.ARGUMENT_BAD_VALUE,             "Passed argument \"{0}\" contains an invalid value" }
  , { ConnectionError.ARGUMENT_SIZE_MISMATCH,         "Passed argument array size dont match expected length" }

     // SAP-00021 - 00030 instance state related errors
  , { ConnectionError.INSTANCE_ATTRIBUTE_IS_NULL,     "Invalid instance state: Attribute {0} must be initialized" }
  , { ConnectionError.INSTANCE_ILLEGAL_STATE,         "Invalide state of instance: Attribute {0} already initialized"}

     // SAP-00061 - 00070 destination related errors
  , { ConnectionError.SERVER_NOT_FOUND,               "JCo Server \"{0}\" not found" }
  , { ConnectionError.DESTINATION_NOT_FOUND,          "JCo Destination \"{0}\" not found" }
  , { ConnectionError.ENVIRONMENT_REGISTERATION,      "JCo Environment registration failed with \"{0}\"" }

     // SAP-00071 - 00080 connectivity related errors
  , { ConnectionError.CONNECTION_UNKNOWN_HOST,        "Host \"{0}\" is unknown" }
  , { ConnectionError.CONNECTION_CREATE_SOCKET,       "Could create network connection to host \"{0}\" on port \"{1}\"" }
  , { ConnectionError.CONNECTION_ERROR,               "Error encountered while connecting to Target System" }
  , { ConnectionError.CONNECTION_TIMEOUT,             "Connection to Target System got timed out : \"{0}\"" }
  , { ConnectionError.CONNECTION_NOT_AVAILABLE,       "The problem may be with physical connectivity or Target System is not alive" }
  , { ConnectionError.CONNECTION_SSL_HANDSHAKE,       "The SSL Certificate may not be generated for Target System or imported properly" }
  , { ConnectionError.CONNECTION_SSL_ERROR,           "Not able to invalidate SSL session." }
  , { ConnectionError.CONNECTION_SSL_DESELECTED,      "SSL option is not selected in IT Resource." }
  , { ConnectionError.CONNECTION_AUTHENTICATION,      "Principal Name \"{0}\" or Password is incorrect, system failed to get access with supplied credentials" }
  , { ConnectionError.CONNECTION_PERMISSION,          "Principal Name \"{0}\" lacks privileges to connect to \"{1}\"" }

     // SAP-00081 - 00090 function module related errors
  , { ConnectionError.FUNCTION_CREATE,                "Unable to create a JCO.Function object \"{0}\" because a connection to the SAP system is not available." }
  , { ConnectionError.FUNCTION_NOT_FOUND,             "JCO.Function object \"{0}\" not found" }
  , { ConnectionError.FUNCTION_NOT_INSTALLED,         "JCO.Function object \"{0}\" not installed" }
  , { ConnectionError.FUNCTION_NOT_REMOTABLE,         "JCO.Function object \"{0}\" is not remote enabled" }
  , { ConnectionError.FUNCTION_EXECUTE_RETRY,         "Failure during JCO.Function execution: Retry operation" }
  , { ConnectionError.FUNCTION_EXECUTE_ABORT,         "Failure during JCO.Function execution was not due to communication, protocol, or system failure: Abort operation" }
  , { ConnectionError.FUNCTION_EXECUTE_OTHER,         "Failure during JCO.Function execution was not due to JCo: Retry operation" }
  , { ConnectionError.FUNCTION_FILETRACE_WRITE,       "Error writing JCO.Function trace file. Reason {0}" }
  , { ConnectionError.FUNCTION_FILETRACE_CLOSE,       "Error closing JCO.Function trace file. Reason {0}" }

     // SAP-00091 - 00100 import parameter related errors
  , { ConnectionError.PARAMETER_TABLE_FORMAT_INVALID, "The format and entry do not match for table \"{0}\"'." }
  , { ConnectionError.PARAMETER_TABLE_NAME_INVALID,   "Invalid column name \"{0}\" for table \"{1}\"." }
  , { ConnectionError.PARAMETER_VALUE_LIST_EXPECTED,  "List expected for attribute \"{0}\"" }

     // SAP-01001 - 01010 connectivity related messages
  , { ConnectionMessage.CONNECTION_OPEN,              "Connecting to \"{0}\" ..." }
  , { ConnectionMessage.CONNECTION_ISOPEN,            "Already connected to \"{0}\"" }
  , { ConnectionMessage.CONNECTION_OPENED,            "Connected to \"{0}\"" }
  , { ConnectionMessage.CONNECTION_CLOSE,             "Disconnecting from \"{0}\" ..." }
  , { ConnectionMessage.CONNECTION_CLOSED,            "Disconnected from \"{0}\"" }
  , { ConnectionMessage.CONNECTION_VALID,             "Connection \"{0}\" is valid." }
  , { ConnectionMessage.CONNECTION_INVALID,           "Connection \"{0}\" is invalid." }

     // SAP-01011 - 01020 function module related messages
  , { ConnectionMessage.FUNCTION_EXECUTE_GENERIC,     "Executing function \"{0}\"" }
  , { ConnectionMessage.FUNCTION_EXECUTE_ACCOUNT,     "Executing function \"{0}\" for account \"{1}\"" }
  , { ConnectionMessage.FUNCTION_EXECUTE_RECREATE,    "Recreating function \"{0}\"" }

     // SAP-01011 - 01020 function module related messages
  , { ConnectionMessage.FUNCTION_IMPORT_PARAMTER,     "Setting segment \"{0}\" attribute name \"{1}\" with value \"{2}\" and update value \"{3}\"" }
  , { ConnectionMessage.FUNCTION_IMPORT_ATTRIBUTE,    "Attribute \"{0}\" = \"{1}\"" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ConnectionBundle.class.getName()
  , Locale.getDefault()
  , ConnectionBundle.class.getClassLoader()
  );

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

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   ** @param  argument3           the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object[] arguments) {
    return RESOURCE.formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}