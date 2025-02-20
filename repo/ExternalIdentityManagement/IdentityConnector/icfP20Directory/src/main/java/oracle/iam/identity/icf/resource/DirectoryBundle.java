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
    Subsystem   :   Generic Database Connector

    File        :   DirectoryBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.iam.identity.icf.connector.DirectoryError;
import oracle.iam.identity.icf.connector.DirectoryMessage;
import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;
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
 ** @since   1.0.0.0
 */
public class DirectoryBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GDS-00041 - 00050 control extension support related errors
    { DirectoryError.CONTROL_EXTENSION_NOT_EXISTS,    "Set of critical extensions absent" }
  , { DirectoryError.CONTROL_EXTENSION_NOT_SUPPORTED, "Critical Extensions is not supported" }

     // GDS-00051 - 00060 naming related errors
  , { DirectoryError.ENCODING_NOT_SUPPORTED,          "Encoding Type \"%1$s\" is not supported." }
  , { DirectoryError.OBJECT_CLASS_NOT_SUPPORTED,      "Object Class \"%1$s\" not supported by Directory Service." }
  , { DirectoryError.OPERATION_NOT_SUPPORTED,         "Operation not supported by Directory Service." }
  , { DirectoryError.OPERATION_NOT_PERMITTED,         "Required permission not granted for operation." }
  , { DirectoryError.INSUFFICIENT_INFORMATION,        "Required field information not provided for \"%1$s\"." }
  , { DirectoryError.SEARCH_FILTER_INVALID,           "%s" }
  , { DirectoryError.OBJECT_NAME_INVALID,             "\"%1$s\" evaluates not to a valid name" }
  , { DirectoryError.OBJECT_PATH_NOT_RESOLVED,        "Object hierarchy path \"%1$s\" not resolved. %2$s" }
  , { DirectoryError.OBJECT_PATH_NOT_EXISTS,          "Object hierarchy path \"%1$s\" does not exists. %2$s" }
  , { DirectoryError.PASSWORD_CHANGE_REQUIRES_TLS,    "Cannot update password in Directory Service without TLS." }

     // GDS-00101 - 00120 entry operation errors
  , { DirectoryError.ENTRY_EXISTS,                    "Entry already exists for \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_AMBIGUOUS,                 "Entry is defined ambiguously for \"%1$s\"." }
  , { DirectoryError.ENTRY_NOT_FOUND,                 "Entry not found for \"%1$s\"." }
  , { DirectoryError.ENTRY_NOT_CREATED,               "Not able to create entry \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_DELETED,               "Not able to delete entry \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_UPDATED,               "Not able to update entry \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_ENABLED,               "Not able to enable object \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_DISABLED,              "Not able to disable object \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_RENAMED,               "Not able to rename entry \"%1$s\". %2$s" }
  , { DirectoryError.ENTRY_NOT_MOVED,                 "Not able to move entry \"%1$s\". %2$s\"" }
  , { DirectoryError.ENTRY_CONTEXT_NOT_EMPTY,         "Context \"%1$s\" is not empty." }

     // GDS-00121 - 00140 attribute operation errors
  , { DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED,       "Attribute Schema violated. %1$s" }
  , { DirectoryError.ATTRIBUTE_INVALID_DATA,          "Invalid Data for Attribute Type." }
  , { DirectoryError.ATTRIBUTE_INVALID_TYPE,          "Invalid Attribute Type." }
  , { DirectoryError.ATTRIBUTE_INVALID_VALUE,         "Invalid Attribute Value for \"%1$s\": %2$s." }
  , { DirectoryError.ATTRIBUTE_INVALID_SIZE,          "More than one value retrieved for attribute." }
  , { DirectoryError.ATTRIBUTE_IN_USE,                "Attribute or value exists: " }
  , { DirectoryError.ATTRIBUTE_NOT_ASSIGNED,          "Unable to add attributes to the entry: \"%1$s\"." }
  , { DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED,      "Attributes already assigned to the entry: \"%1$s\"." }
  , { DirectoryError.ATTRIBUTE_NOT_REMOVED,           "Unable to remove attributes from the entry: \"%1$s\"." }
  , { DirectoryError.ATTRIBUTE_ALREADY_REMOVED,       "Attributes already removed from the entry: \"%1$s\"." }

     // GDS-00141 - 00150 changeLog related errors
  , { DirectoryError.CHANGELOG_NUMBER,                "Unable to find the change number \"%1$s\" value in rootDSE" }
  
    // GDS-01001 - 01010 connection related messages
  , { DirectoryMessage.CONNECTING_BEGIN,              "Connecting to service URL \"%1$s\" with user \"%2$s\" ..." }
  , { DirectoryMessage.CONNECTING_SUCCESS,            "Connection to service URL \"%1$s\" for user \"%2$s\" established" }
  , { DirectoryMessage.AUTHENTICATION_BEGIN,          "Authenticating account \"%2$s\" on service URL \"%1$s\" ..." }
  , { DirectoryMessage.AUTHENTICATION_SUCCESS,        "Account \"%2$s\" on service URL \"%1$s\" authenticated" }

    // GDS-01011 - 01020 operation related messages
  , { DirectoryMessage.ATTRIBUTE_ADDED,                "Attribute \"%2$s\" added to \"%1$s\"" }
  , { DirectoryMessage.ATTRIBUTE_DELETED,              "Attribute \"%2$s\" deleted from \"%1$s\"" }
  , { DirectoryMessage.ATTRIBUTE_TOMODIFY,             "About to modify attribute \"%2$s\" on \"%1$s\"" }
  , { DirectoryMessage.ATTRIBUTE_MODIFIED,             "Attribute \"%2$s\" on \"%1$s\" modified" }

  , {DirectoryMessage.CHANGELOG_LOGENTRY_BEGIN,        "Attempting to create sync delta for log entry \"%1$d\"" }
  , {DirectoryMessage.CHANGELOG_TARGETDN_NULL,         "Skipping log entry because it does not have a targetDN attribute" }
  , {DirectoryMessage.CHANGELOG_TARGETDN_SKIP,         "Skipping log entry because it does not match any of the base contexts to synchronize" }
  , {DirectoryMessage.CHANGELOG_TARGETDN_BASE,         "Skipping log entry because it is \"%1$s\" base context" }
  , {DirectoryMessage.CHANGELOG_TARGETDN_SCOPE,        "Skipping entry because it is outside of the container and scope provided in the operation options" }
  , {DirectoryMessage.CHANGELOG_TARGETDN_NOTEXISTS,    "Skipping entry because the modified entry is missing, not of the right object class, or not matching the search filter" }
  , {DirectoryMessage.CHANGELOG_TARGETRDN_NOTEXISTS,   "Skipping log entry because it does not have a newRdn attribute" }
  , {DirectoryMessage.CHANGELOG_FILTER_OBJECTCLASS,    "Skipping entry because no object class in the list of object classes to synchronize" }
  , {DirectoryMessage.CHANGELOG_FILTER_ATTRIBUTE,      "Skipping entry because no changed attributes in the list of attributes to synchronize" }
  , {DirectoryMessage.CHANGELOG_FILTER_MODIFIER,       "Skipping entry because modifiersName is in the list of modifiersName's to filter out" }
  , {DirectoryMessage.CHANGELOG_FILTER_DISABLED,       "Filtering by \"%1$s\" disabled" }
  , {DirectoryMessage.CHANGELOG_FILTER_NOTSET,         "Not filtering by \"%1$s\" because not set for this entry" }
  , {DirectoryMessage.CHANGELOG_FILTER_SWITCH,         "Not filtering by \"%1$s\" because not set for this entry, will use \"%2$s\" instead" }
  , {DirectoryMessage.CHANGELOG_CHGTYPE_DELETE,        "Creating sync delta for deleted entry" }
  , {DirectoryMessage.CHANGELOG_CHGTYPE_ATTRIBUTE,     "The identifying attribute can only represent a DN of a unique ID" }
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
  // Method:   string
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%n$s" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.stringFormat(key, arguments);
  }
}