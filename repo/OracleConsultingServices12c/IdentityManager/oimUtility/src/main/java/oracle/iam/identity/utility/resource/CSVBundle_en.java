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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVBundle_en.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.utility.file.CSVError;
import oracle.iam.identity.utility.file.CSVMessage;

////////////////////////////////////////////////////////////////////////////////
// class CSVBundle_en
// ~~~~~ ~~~~~~~~~~~~
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
 */
public class CSVBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // OHF-00001 - OHF-00005 General error (Undefined)
    { SystemError.GENERAL,                           "General error: " }
  , { SystemError.UNHANDLED,                         "An unhandled exception has occured: "}
  , { SystemError.ABORT,                             "Execution aborted: %1$s"}
  , { SystemError.NOTIMPLEMENTED,                    "Feature is not yet implemented"}
  , { SystemError.CLASSNOTFOUND,                     "Class %1$s was not found in the classpath"}
  , { SystemError.CLASSNOTCREATE,                    "Class %1$s has not been created"}

     // OHF-00006 - OHF-00010 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,                  "Passed argument %1$s must not be null" }
  , { SystemError.ARGUMENT_BAD_TYPE,                 "Passed argument %1$s has a bad type" }
  , { SystemError.ARGUMENT_BAD_VALUE,                "Passed argument %1$s contains an invalid value" }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,            "Passed argument array size dont match expected length" }

     // OHF-00011 - OHF-00020 instance attribute related errors
  , { SystemError.ATTRIBUTE_IS_NULL,                 "State of attribute %1$s must not be null" }

     // OHF-00021 - OHF-00030 file related errors
  , { SystemError.FILE_MISSING,                      "Encountered problems to find file %1$s" }
  , { SystemError.FILE_NOT_FILE,                      "%1$s is not a file" }
  , { SystemError.FILE_OPEN,                         "Encountered problems to open file %1$s" }
  , { SystemError.FILE_CLOSE,                        "Encountered problems to close file %1$s" }
  , { SystemError.FILE_READ,                         "Encountered problems reading file %1$s" }
  , { SystemError.FILE_WRITE,                        "Encountered problems writing file %1$s" }

     // OIM-00081 - OIM-00090 dependency analyzer errors
  , { TaskError.DEPENDENCY_PARENT_CONFLICT,          "Ooops existing.predecessor %1$s is not null, but dependency list of the predecessor %2$s don't know the object"}

     // OIM-01051 - OIM-01070 dependency analysis messages
  , { TaskMessage.DEPENDENCY_RESOLVING,              "Resolving %1$s within %2$s" }
  , { TaskMessage.DEPENDENCY_CREATE_ROOT,            "Creating Dependency Root %1$s due to predecessor is empty" }
  , { TaskMessage.DEPENDENCY_CREATE_UNRESULVED,      "Creating unresolved node %1$s" }
  , { TaskMessage.DEPENDENCY_NODE_RESOLVED,          "Dependency between %1$s %2$s resolved" }
  , { TaskMessage.DEPENDENCY_NODE_INSERT_RESOLVED,   "Dependency between %1$s %2$s inserted in resolved" }
  , { TaskMessage.DEPENDENCY_NODE_ADDED_RESOLVED,    "Dependency between %1$s %2$s added to resolved" }
  , { TaskMessage.DEPENDENCY_NODE_MOVED_RESOLVED,    "Dependency between %1$s %2$s moved to resolved" }
  , { TaskMessage.DEPENDENCY_NODE_INSERT_UNRESOLVED, "Dependency between %1$s %2$s inserted in resolved" }
  , { TaskMessage.DEPENDENCY_NODE_ADDED_UNRESOLVED,  "Dependency between %1$s %2$s added to unresolved" }
  , { TaskMessage.DEPENDENCY_NODE_MOVED_UNRESOLVED,  "Dependency between %1$s %2$s moved to unresolved" }
  , { TaskMessage.DEPENDENCY_NODE_NOT_RESOLVED,      "Dependency %1$s is not resolvable in %2$s" }
  , { TaskMessage.DEPENDENCY_NODE_IS_ACTIVE,         "Node %1$s is already associated with a workung file" }

     // CSV-00061 - CSV-00070 file system naming and lookup related errors
  , { CSVError.FILENAME_MISSING,                     "File missing: %1$s needs a single filename on the command line !"}
  , { CSVError.FILEEXTENSION_IS_BAD,                 "Bad Extension: %1$s needs a single filename on the command line !"}
  , { CSVError.NOTAFOLDER,                           "Usage of %2$s as %1$s is invalid, because it's not a folder" }
  , { CSVError.NOTAFILE,                             "Usage of %2$s as %1$s is invalid, because it's not a file" }
  , { CSVError.NOTREADABLE,                          "Cannot use %2$s as %1$s, because it's not readable" }
  , { CSVError.NOTWRITABLE,                          "Cannot use %2$s as %1$s, because it's not writable" }
  , { CSVError.NOTCREATABLE,                         "Cannot create %1$s reason: %2$s" }
  , { CSVError.NOTCLOSEDINPUT,                       "Cannot close input stream %1$s" }
  , { CSVError.NOTCLOSEDOUTPUT,                      "Cannot close output stream %1$s" }

     // CSV-00071 - 00080 parsing related errors
  , { CSVError.INVALID_ELEMENT,                      "Invalid XML elemnet %1$s detected"}
  , { CSVError.INVALID_STATE,                        "Invalid parser state for XML elemnet %1$s occurred"}
  , { CSVError.INVALID_OPERATION,                    "Argument %1$s is invalid. Allowed values [ %2$s ]"}
  , { CSVError.NOT_ENLISTED_ATTRIBUTE,               "Attribute %1$s is not enlisted as an attribute."}
  , { CSVError.NOT_ENLISTED_IDENTIFIER,              "Attribute %1$s is not enlisted as an attribute."}

     // CSV-00081 - 000901 processing related errors
  , { CSVError.PARSER_ERROR,                         "Unexpected exception %1$s at line %2$s!\n%3$s"}
  , { CSVError.MISSING_HEADER,                       "The file seems not containing a header !"}
  , { CSVError.MISSING_SEPARATOR,                    "Malformed CSV stream: Missing separator after field on line %1$s"}
  , { CSVError.MISSING_QUOTE_OPEN,                   "Malformed CSV stream: Missing quote (\") at start of field on line %1$s"}
  , { CSVError.MISSING_QUOTE_CLOSE,                  "Malformed CSV stream: Missing quote (\") after field on line %1$s"}
  , { CSVError.EXECUTION_FAILED,                     "%1$s failed to %2$s %3$s !"}
  , { CSVError.HEADER_UNKNOWN,                       "Field %1$s not found in header line !"}
  , { CSVError.CONTENT_UNKNOWN,                      "Field %1$s has no content !"}
  , { CSVError.CONTENT_NOT_FOUND,                    "Field %1$s is unknown !"}

     // CSV-0000 generic message template
  , { CSVMessage.MESSAGE,                            "%1$s" }

     // CSV-00001 - CSV-00010 processing messages
  , { CSVMessage.VALIDATING,                         "Validating CSV descriptor" }
  , { CSVMessage.LOADING,                            "Loading ..." }
  , { CSVMessage.PROCESSING,                         "Processing ..." }
  , { CSVMessage.COMPLETED,                          "End of differences." }
  , { CSVMessage.IDENTICALLY,                        "Files are identical." }
  , { CSVMessage.LINECOUNT,                          "Line count of %1$s file: %2$s" }
  , { CSVMessage.ENDOFFILE,                          "End-Of-File reached for %1$s" }
  , { CSVMessage.EMPTYFILE,                          "File %1$s is empty" }
  , { CSVMessage.CHUNKSIZE,                          "%1$s entities written to %2$s" }
  , { CSVMessage.CHUNKMERGED,                        "Merge of chunk %1$s completed" }
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