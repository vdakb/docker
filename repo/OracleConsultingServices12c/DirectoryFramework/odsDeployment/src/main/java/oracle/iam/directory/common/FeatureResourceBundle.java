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

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   FeatureResourceBundle.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureResourceBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.directory.common;

import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FeatureResourceBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated entrys by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FeatureResourceBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // ODS-00001 - 00010 general operational errors
    { FeatureError.UNHANDLED,                          "Unhandled exception occured: %1$s" }
  , { FeatureError.GENERAL,                            "Encounter some problems: %1$s" }
  , { FeatureError.ABORT,                              "Execution aborted: %1$s"}
  , { FeatureError.NOTIMPLEMENTED,                     "Feature is not yet implemented"}
  , { FeatureError.CLASSINVALID,                       "Class [%1$s] must be a subclass of %2$s"}
  , { FeatureError.CLASSCONSTRUCTOR,                   "Class [%1$s] does not have constructor parameter %2$s"}
  , { FeatureError.ARGUMENT_IS_NULL,                   "Passed argument [%1$s] must not be null" }
  , { FeatureError.ATTRIBUTE_IS_NULL,                  "Invalid instance state: Attribute [%1$s] must be initialized" }

     // ODS-00011 - 00020 file related errors
  , { FeatureError.FILE_MISSING,                       "Encountered problems to find file [%1$s]" }
  , { FeatureError.FILE_ENCODING_TYPE,                 "Encoding Type [%1$s] is not supported" }

     // ODS-00021 - 00040 export/import related errors
  , { FeatureError.EXPORT_ATTRIBUTE_ONLYONCE,          "The Atribute [%1$s] to export %2$s already added to this task." }
  , { FeatureError.EXPORT_FILE_MANDATORY,              "Specify at least one export -- a file or a resource collection." }
  , { FeatureError.EXPORT_FILE_ONLYONCE,               "The Export file [%1$s] already added to this task." }
  , { FeatureError.EXPORT_FILE_ISDIRECTORY,            "An export cannot be written to a directory." }
  , { FeatureError.EXPORT_FILE_NODIRECTORY,            "The directory [%1$s] does not exists." }
  , { FeatureError.EXPORT_FILE_NOPERMISSION,           "The export file [%1$s] is not writeable by the executing account." }
  , { FeatureError.EXPORT_FILE_SEARCH,                 "A search element must be provided for an export set." }
  , { FeatureError.EXPORT_FILE_DESCRIPTION,            "A description must be provided for an export set." }
  , { FeatureError.IMPORT_FILE_CONSTRAINT,             "A file must e specified to declare substitutions." }
  , { FeatureError.IMPORT_FILE_MANDATORY,              "Specify at least one import -- a file or a resource collection." }
  , { FeatureError.IMPORT_FILE_ONLYONCE,               "The Import file [%1$s] already added to this task." }
  , { FeatureError.IMPORT_FILE_NOTEXISTS,              "The import file [%1$s] does not exists." }
  , { FeatureError.IMPORT_FILE_ISDIRECTORY,            "An import cannot be made from a directory." }
  , { FeatureError.IMPORT_FILE_NOPERMISSION,           "The import file [%1$s] is not readable by the executing account." }
  , { FeatureError.IMPORT_FILE_FETCH,                  "Error in fetching import file %1$s. Reason %2$s" }

     // ODS-00041 - 00060 LDIF parsing related errors
  , { FeatureError.LDIF_LINE,                          "Line %2$s: %1$s" }
  , { FeatureError.LDIF_LINE_NOWHERE,                  "Continuation out of nowhere" }
  , { FeatureError.LDIF_UNEXPECTED,                    "Unexpected [%1$s]" }
  , { FeatureError.LDIF_EXPECTING_OID,                 "OID expected for control" }
  , { FeatureError.LDIF_EXPECTING_PREFIX,              "Expecting [%1$s] as a separator in line %2$s" }
  , { FeatureError.LDIF_EXPECTING_SEPARATOR,           "Expecting separator [%1$s] is missing in line %2$s" }
  , { FeatureError.LDIF_EXPECTING_ATTRIBUTE,           "Add operation needs a value for attribute [%1$s]" }
  , { FeatureError.LDIF_EXPECTING_CRITICALITY,         "Criticality for control must be true or false, not [%1$s]" }
  , { FeatureError.LDIF_CONSTRUCT_URL,                 "%1$s: cannot construct URL [%2$s]" }
  , { FeatureError.LDIF_CONSTRUCT_STRING,              "%1$s: cannot construct string [%2$s]" }
  , { FeatureError.LDIF_CHANGE_TYPE_UNKNOW,            "Unknown Change Type [%1$s]" }
  , { FeatureError.LDIF_CHANGE_TYPE_NOTSUPPORTED,      "Change Type [%1$s] not supported" }

     // ODS-00061 - 00080 DSML parsing related errors
  , { FeatureError.DSML_SEARCH_DESCRIPTOR,             "A search descriptor is required to perform this operation" }
  , { FeatureError.DSML_LISTENER_ONLYONE,              "Only one listener supported" }
  , { FeatureError.DSML_TAG_OPENING_NOT_RECOGNIZED,    "Document error at line %1$s, column %2$s: The opening tag [%3$s] is not recognized in this context" }
  , { FeatureError.DSML_TAG_CLOSING_NOT_RECOGNIZED,    "Document error at line %1$s, column %2$s: The closing tag [%3$s] is not recognized in this context " }
  , { FeatureError.DSML_UNEXPECTED_EVENT,              "Document error at line %1$s, column %2$s: The event [%3$s] isn\'t expected" }
  , { FeatureError.DSML_EXPECTIING_NAMESPACE,          "Document error at line %1$s, column %2$s: Document error: Expecting namespace declaration [%3$s]" }
  , { FeatureError.DSML_EXPECTIING_OPENING_TAG,        "Document error at line %1$s, column %2$s: Expecting opening tag [%3$s], found [%4$s] instead" }
  , { FeatureError.DSML_EXPECTIING_CLOSING_TAG,        "Document error at line %1$s, column %2$s: Expecting closing tag [%3$s], found [%4$s] instead" }
  , { FeatureError.DSML_EXPECTIING_ATTRIBUTE,          "Document error at line %1$s, column %2$s: The element [%3$s] is missing required attribute [%4$s]" }
  , { FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE,     "Document error at line %1$s, column %2$s: The element [%3$s] can only have one attribute [%4$s]" }
  , { FeatureError.DSML_VALUE_INVALID,                 "Document error at line %1$s, column %2$s: Element [%3$s] has invalid value [%4$s]" }
  , { FeatureError.DSML_ROOT_CLOSING_OUTSIDE,          "Document error at line %1$s, column %2$s: Closing tag [%3$s] was not expected, the document root has been closed" }
  , { FeatureError.DSML_ROOT_DOCUMENT_STILL_OPEN,      "Document error at line %1$s, column %2$s: The root element is still open at end of document" }

     // ODS-00081 - 00100 entry and attribute operational related errors
  , { FeatureError.NOT_SUPPORTED,                      "Operation not supported by Directory Service." }
  , { FeatureError.NOT_EMPTY,                          "Context [%1$s] is not empty." }
  , { FeatureError.OBJECT_CREATE,                      "Cannot create object for name [%2$s] in [%1$s]" }
  , { FeatureError.OBJECT_DELETE,                      "Cannot delete object bound to name [%2$s] from [%1$s]" }
  , { FeatureError.OBJECT_MODIFY,                      "Not able to modify object bound to name [%2$s] in [%1$s]" }
  , { FeatureError.OBJECT_RENAME,                      "Not able to rename object bound to name [%2$s] in [%1$s]" }
  , { FeatureError.OBJECT_EXISTS,                      "Name [%2$s] already bound to an object in [%1$s]" }
  , { FeatureError.OBJECT_NOT_EXISTS,                  "Name [%2$s] not bound to an object in [%1$s]" }
  , { FeatureError.OBJECT_AMBIGUOUS,                   "Name [%2$s] is defined ambiguously in [%1$s]" }
  , { FeatureError.OBJECT_ASSIGN,                      "Attributes cannot be assigned to object with name [%2$s] in [%1$s]" }
  , { FeatureError.ATTRIBUTE_SCHEMA,                   "Attribute not in mandatories or optionals of object classes" }
  , { FeatureError.ATTRIBUTE_DATA,                     "Invalid Data for Attribute Type" }
  , { FeatureError.ATTRIBUTE_TYPE,                     "Invalid Attribute Type" }
  , { FeatureError.ATTRIBUTE_SIZE,                     "More than one value retrieved for attribute" }
  , { FeatureError.ATTRIBUTE_IN_USE,                   "Attribute or value exists: " }


     // ODS-00101 - 00110 process definition related errors
  , { FeatureError.COMMAND_PROPERTY_ONLYONCE,          "The Property [%1$s] already added to this command." }
    , { FeatureError.COMMAND_EXCECUTION_FAILED,          "Exceution of [%1$s] failed! Reason [%2$s]" }

     // 01001 - 01020 entry and attribute operational related errors
  , { FeatureMessage.OBJECT_CREATE_BEGIN,              "Creating object with name [%2$s] in [%1$s] ..."}
  , { FeatureMessage.OBJECT_CREATE_SUCCESS,            "Object with name [%2$s] in [%1$s] created"}
  , { FeatureMessage.OBJECT_CREATE_SKIPPED,            "Skipped create of name [%2$s] in [%1$s]"}
  , { FeatureMessage.OBJECT_MODIFY_BEGIN,              "Modifying object bound to name [%2$s] in [%1$s] ..."}
  , { FeatureMessage.OBJECT_MODIFY_SUCCESS,            "Object bound to name [%2$s] in [%1$s] modified"}
  , { FeatureMessage.OBJECT_MODIFY_SKIPPED,            "Object bound to name [%2$s] in [%1$s] skipped. No changes detected"}
  , { FeatureMessage.OBJECT_DELETE_BEGIN,              "Deleting object bound to name [%2$s] from [%1$s] ..."}
  , { FeatureMessage.OBJECT_DELETE_SUCCESS,            "Object bound to name [%2$s] from [%1$s] deleted"}
  , { FeatureMessage.OBJECT_RENAME_BEGIN,              "Renaming object bound to name  [%1$s] ..."}
  , { FeatureMessage.OBJECT_RENAME_SUCCESS,            "Object bound to name  [%1$s] renamed"}
  , { FeatureMessage.OBJECT_MOVE_BEGIN,                "Moving object bound to name  [%1$s] ..."}
  , { FeatureMessage.OBJECT_MOVE_SUCCESS,              "Object bound to name  [%1$s] moved"}

     // 01021 - 01030 entry and attribute operational related errors
  , { FeatureMessage.EXPORT_OBJECT,                    "Exporting object with name [%2$s] from [%1$s] ..."}
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(FeatureResourceBundle.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)
  /**
   ** Returns an array, where each item in the array is a pair of entrys.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of entrys.
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
   ** <p>
   ** This will substitute "%1$s" occurrences in the string resource with the
   ** appropriate parameter.
   **
   ** @param  key                key into the resource array.
   ** @param  argument           the substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s" and "%2$s" occurrences in the string resource
   ** with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s", "%2$s" and "%3$s" occurrences in the string
   ** resource with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   ** @param  argument3          the third substitution parameter.
   **
   ** @return                    the formatted String resource
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
   ** @return                    the formatted String resource
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