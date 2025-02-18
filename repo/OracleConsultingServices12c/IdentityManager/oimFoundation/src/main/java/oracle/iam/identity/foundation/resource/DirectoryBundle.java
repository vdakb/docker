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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Resource Facility

    File        :   DirectoryBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryMessage;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryBundle
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code common
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class DirectoryBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GDS-00001 - 00010 General error (Undefined)
    { DirectoryError.UNHANDLED,                        "Unhandled exception occured: [%1$s]" }
  , { DirectoryError.GENERAL,                          "Encounter some problems: [%1$s]" }
  , { DirectoryError.ABORT,                            "Execution aborted: [%1$s]"}
  , { DirectoryError.NOTIMPLEMENTED,                   "Feature is not yet implemented"}

     // GDS-00011 - 00012 method argument related errors
  , { DirectoryError.ARGUMENT_IS_NULL,                 "Passed argument [%1$s] must not be null" }

     // GDS-00021 - 00030 instance state related errors
  , { DirectoryError.INSTANCE_ATTRIBUTE_IS_NULL,       "Invalid instance state: Attribute [%1$s] must be initialized" }

     // GDS-00031 - 00040 file related errors
  , { DirectoryError.FILE_MISSING,                     "Encountered problems to find file [%1$s]" }
  , { DirectoryError.FILE_IS_NOT_A_FILE,               "[%1$s] is not a file" }
  , { DirectoryError.FILE_OPEN,                        "Encountered problems to open file [%1$s]" }
  , { DirectoryError.FILE_CLOSE,                       "Encountered problems to close file [%1$s]" }
  , { DirectoryError.FILE_READ,                        "Encountered problems reading file [%1$s]" }
  , { DirectoryError.FILE_WRITE,                       "Encountered problems writing file [%1$s]" }

     // GDS-00061 - 00070 connectivity errors
  , { DirectoryError.CONNECTION_UNKNOWN_HOST,          "Host [%1$s] is unknown" }
  , { DirectoryError.CONNECTION_CREATE_SOCKET,         "Could create network connection to host [%1$s] on port [%2$s]" }
  , { DirectoryError.CONNECTION_ERROR,                 "Error encountered while connecting to Target System" }
  , { DirectoryError.CONNECTION_TIMEOUT,               "Connection to Target System got timed out: [%1$s]" }
  , { DirectoryError.CONNECTION_NOT_AVAILABLE,         "The problem may be with physical connectivity or Target System is not alive" }
  , { DirectoryError.CONNECTION_SSL_HANDSHAKE,         "The SSL Certificate may not be generated for Target System or imported properly" }
  , { DirectoryError.CONNECTION_SSL_ERROR,             "Not able to invalidate SSL session." }
  , { DirectoryError.CONNECTION_SSL_DESELECTED,        "SSL option is not selected in IT Resource." }
  , { DirectoryError.CONNECTION_AUTHENTICATION,        "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { DirectoryError.CONNECTION_ENCODING_NOTSUPPORTED, "URL Encoding [%1$s] not supported" }

     // GDS-00071 - 00080 certificate related errors
  , { DirectoryError.CERTIFICATE_FILE_NOT_FOUND,       "Certificate file [%1$s] is not available." }
  , { DirectoryError.CERTIFICATE_TYPE_NOT_AVAILABLE,   "Certificate type [%1$s] is not available in the default provider package or any of the other provider packages that were searched." }

     // GDS-00081 - 00090 control extension support related errors
  , { DirectoryError.CONTROL_EXTENSION_EXISTS,         "Set of critical extensions are: " }
  , { DirectoryError.CONTROL_EXTENSION_NOT_EXISTS,     "Set of critical extensions absent" }
  , { DirectoryError.CONTROL_EXTENSION_SUPPORTED,      "Critical Extensions Supported." }
  , { DirectoryError.CONTROL_EXTENSION_NOT_SUPPORTED,  "Critical Extensions is not supported" }

     // GDS-00091 - 00130 operational errors
  , { DirectoryError.ENCODING_TYPE_NOT_SUPPORTED,      "Encoding Type [%1$s] is not supported" }
  , { DirectoryError.OBJECT_NOT_CREATED,               "Cannot create object [%1$s]" }
  , { DirectoryError.OBJECT_NOT_DELETED,               "Cannot delete object [%1$s]" }
  , { DirectoryError.OBJECT_ALREADY_EXISTS,            "Object already exists for [%1$s]" }
  , { DirectoryError.OBJECT_NOT_EXISTS,                "Object does not exists for [%1$s]" }
  , { DirectoryError.OBJECT_NOT_ASSIGNED,              "Attributes cannot be assigned to object [%1$s]" }
  , { DirectoryError.OBJECT_ALREADY_ASSIGNED,          "Attributes already assigned to [%1$s]" }
  , { DirectoryError.OBJECT_ALREADY_REMOVED,           "Attributes already removed from [%1$s]" }
  , { DirectoryError.OBJECT_AMBIGUOUS,                 "Object is defined ambiguously for [%1$s]" }
  , { DirectoryError.OBJECT_NOT_ENABLED,               "Not able to enable object [%1$s]" }
  , { DirectoryError.OBJECT_NOT_DISABLED,              "Not able to disable object [%1$s]" }
  , { DirectoryError.OBJECT_NOT_UPDATED,               "Not able to update object [%1$s]" }
  , { DirectoryError.OBJECT_NOT_RENAMED,               "Not able to rename object [%1$s]" }
  , { DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED,        "Attribute not in mandatories or optionals of object classes" }
  , { DirectoryError.ATTRIBUTE_INVALID_DATA,           "Invalid Data for Attribute Type" }
  , { DirectoryError.ATTRIBUTE_INVALID_TYPE,           "Invalid Attribute Type" }
  , { DirectoryError.ATTRIBUTE_INVALID_SIZE,           "More than one value retrieved for attribute" }
  , { DirectoryError.ATTRIBUTE_IN_USE,                 "Attribute or value exists: " }
  , { DirectoryError.ATTRIBUTE_NOT_ASSIGNED,           "Unable to add attributes to the object: [%1$s]" }
  , { DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED,       "Attributes already assigned to the object: [%1$s]" }
  , { DirectoryError.INSUFFICIENT_INFORMATION,         "Required field information not provided for [%1$s]." }
  , { DirectoryError.OPERATION_NOT_SUPPORTED,          "Operation not supported by Directory Service." }
  , { DirectoryError.CONTEXT_NOT_EMPTY,                "Context [%1$s] is not empty." }
  , { DirectoryError.CONTEXT_NOT_EMPTY,                "Context [%1$s] is not empty." }
  , { DirectoryError.PASSWORD_CHANGE_REQUIRES_SSL,     "Cannot update password in Directory Service without SSL" }
  , { DirectoryError.HIERARCHY_PATH_NOT_RESOLVED,      "Target hierarchy path [%1$s] not resolved}" }
  , { DirectoryError.ROLE_PATH_NOT_RESOLVED,           "Target role path [%1$s] not resolved}" }
  , { DirectoryError.GROUP_PATH_NOT_RESOLVED,          "Target group path [%1$s] not resolved}" }
  , { DirectoryError.HIERARCHY_PATH_NOT_EXISTS,        "Target hierarchy path [%1$s] does not exists}" }

     // GDS-00141 - 00160 file parsing related errors
  , { DirectoryError.LINE,                             "Line %2$s: %1$s" }
  , { DirectoryError.UNEXPECTED,                       "Unexpected [%1$s]" }
  , { DirectoryError.MISSING_SEPARATOR,                "A valid separator was expected" }
  , { DirectoryError.INVALID_SEPARATOR,                "[%1$s] is not a valid separator" }
  , { DirectoryError.EXPECTING_SEPARATOR,              "Expecting separator [%1$s] is missing in line %2$s" }
  , { DirectoryError.EXPECTING_PREFIX,                 "Expecting [%1$s] in line %2$s" }
  , { DirectoryError.LINE_PARSER_NOWHERE,              "Continuation out of nowhere" }
  , { DirectoryError.CONSTRUCT_STRING,                 "%1$s: cannot construct string [%2$s]" }
  , { DirectoryError.CONSTRUCT_URL,                    "%1$s: cannot construct URL [%2$s]" }
  , { DirectoryError.CHANGE_TYPE_UNKNOW,               "Unknown Change Type [%1$s]" }
  , { DirectoryError.CHANGE_OPERATION_UNKNOW,          "Unknown Change Operation [%1$s]" }
  , { DirectoryError.CHANGE_TYPE_NOTSUPPORTED,         "Change Type [%1$s] not supported" }

    // GDS-01001 - 01010 system related messages
  , { DirectoryMessage.CONNECTING_TO,                  "Connecting to [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_ADDED,                "Attribute [%2$s] added to [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_DELETED,              "Attribute [%2$s] deleted from [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_TOMODIFY,             "About to modify attribute [%2$s] on [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_MODIFIED,             "Attribute [%2$s] on [%1$s] modified" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    DirectoryBundle.class.getName()
  , Locale.getDefault()
  , DirectoryBundle.class.getClassLoader()
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
  @Override
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
   ** @param  argument            the subsitution value for <code>%1$s</code>.
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
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
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
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   ** @param  argument3           the subsitution value for <code>%3$s</code>.
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