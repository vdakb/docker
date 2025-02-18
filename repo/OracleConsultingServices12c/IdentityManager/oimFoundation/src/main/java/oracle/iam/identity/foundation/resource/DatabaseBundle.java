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

    File        :   DatabaseBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseMessage;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseBundle
// ~~~~~ ~~~~~~~~~~~~~~
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
public class DatabaseBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00001 - 00010 operations related errors
    { DatabaseError.UNHANDLED,                   "Unhandled exception occured: %1$s" }
  , { DatabaseError.GENERAL,                     "Encounter some problems: %1$s" }
  , { DatabaseError.ABORT,                       "Execution aborted: %1$s"}

     // DBS-00011 - 00015 method argument related errors
  , { DatabaseError.ARGUMENT_IS_NULL,            "Passed argument [%1$s] must not be null" }
  , { DatabaseError.ARGUMENT_BAD_TYPE,           "Passed argument [%1$s] has a bad type" }
  , { DatabaseError.ARGUMENT_BAD_VALUE,          "Passed argument [%1$s] contains an invalid value" }
  , { DatabaseError.ARGUMENT_SIZE_MISMATCH,      "Passed argument array size don't match expected length" }

     // DBS-00021 - 00030 instance state related errors
  , { DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL,  "Invalid instance state: Attribute %1$s must be initialized" }
  , { DatabaseError.INSTANCE_ILLEGAL_STATE,      "Invalide state of instance: Attribute %1$s already initialized"}

     // DBS-00061 - 00070 connectivity errors
  , { DatabaseError.CONNECTION_UNKNOWN_HOST,     "Host [%1$s] is unknown" }
  , { DatabaseError.CONNECTION_CREATE_SOCKET,    "Could create network connection to host [%1$s] on port [%2$s]" }
  , { DatabaseError.CONNECTION_ERROR,            "Error encountered while connecting to Target System" }
  , { DatabaseError.CONNECTION_TIMEOUT,          "Connection to Target System got timed out : [%1$s]" }
  , { DatabaseError.CONNECTION_NOT_AVAILABLE,    "The problem may be with physical connectivity or Target System is not alive" }
  , { DatabaseError.CONNECTION_SSL_HANDSHAKE,    "The SSL Certificate may not be generated for Target System or imported properly" }
  , { DatabaseError.CONNECTION_SSL_ERROR,        "Not able to invalidate SSL session." }
  , { DatabaseError.CONNECTION_SSL_DESELECTED,   "SSL option is not selected in IT Resource." }
  , { DatabaseError.CONNECTION_AUTHENTICATION,   "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { DatabaseError.CONNECTION_PERMISSION,       "Principal Name [%1$s] lacks privileges to connect to [%2$s]" }

     // DBS-00071 - 00080 access operational errors
  , { DatabaseError.SYNTAX_INVALID,              "There is an error in the SQL syntax: [%1$s]" }
  , { DatabaseError.OPERATION_FAILED,            "Operation reported as failed by Database Service." }
  , { DatabaseError.OPERATION_NOT_SUPPORTED,     "Operation not supported by Database Service." }
  , { DatabaseError.INSUFFICIENT_PRIVILEGE,      "Principal [%1$s] has insufficient privileges to perform operation [%2$s]" }
  , { DatabaseError.INSUFFICIENT_INFORMATION,    "Required field information not provided for [%1$s]." }
  , { DatabaseError.SEARCH_CONDITION_FAILED,     "Build of search condition [%1$s] failed" }

     // DBS-00091 - 00130 object operational errors
  , { DatabaseError.OBJECT_NOT_CREATED,          "Cannot create entry [%1$s] in [%2$s]" }
  , { DatabaseError.OBJECT_NOT_MODIFIED,         "Cannot modify entry [%1$s] in [%2$s]" }
  , { DatabaseError.OBJECT_NOT_DELETED,          "Cannot delete entry [%1$s] in [%2$s]" }
  , { DatabaseError.OBJECT_ALREADY_EXISTS,       "Entry already exists for [%1$s] in [%2$s]" }
  , { DatabaseError.OBJECT_NOT_EXISTS,           "Entry does not exists for [%1$s] in [%2$s]" }
  , { DatabaseError.OBJECT_AMBIGUOUS,            "Entry is defined ambiguously for [%1$s] in [%2$s]" }
  , { DatabaseError.PARENT_NOT_EXISTS,           "Superior Entry does not exists for [%1$s] in [%2$s]" }
  , { DatabaseError.PARENT_AMBIGUOUS,            "Superior Entry is defined ambiguously for [%1$s] in [%2$s]" }
  , { DatabaseError.PERMISSION_NOT_ASSIGNED,     "Not able to assign permission [%1$s] to [%2$s]" }
  , { DatabaseError.PERMISSION_NOT_REMOVED,      "Not able to remove permission [%1$s] from [%2$s]" }
  , { DatabaseError.PERMISSION_ALREADY_ASSIGNED, "Permission [%1$s] already assigned to [%2$s]" }
  , { DatabaseError.PERMISSION_ALREADY_REMOVED,  "Permission [%1$s] already removed from [%2$s]" }

     // DBS-01001 - 01010 system related messages
  , { DatabaseMessage.CONNECTING_TO,             "Connecting to [%1$s]" }

     // DBS-01011 - 01020 system related messages
  , { DatabaseMessage.EXECUTE_STATEMENT,         "Executing: %1$s" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    DatabaseBundle.class.getName()
  , Locale.getDefault()
  , DatabaseBundle.class.getClassLoader()
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