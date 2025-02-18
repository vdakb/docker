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

    File        :   DatabaseBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseMessage;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~
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
public class DatabaseBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00051 - 00060 schema discovery errors
    { DatabaseError.SCHEMA_DESCRIPTOR_ERROR,     "The schema descriptor operation failed: [%1$s]." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_EMPTY,     "The schema descriptor is empty." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_NOTFOUND,  "The schema descriptor could not be located." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_INVALID,   "The chema descriptor is invalid." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PARSE,     "Error occured while parsing schema file: [%1$s]" }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PRIMARY,   "System Identifier not defined for entity [%1$s]." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_SECONDARY, "Unique Identifier not defined for entity [%1$s]." }

     // DBS-00061 - 00070 connectivity and statment errors
  , { DatabaseError.CONNECTION_NOT_CONNECTED,    "Not connected." }
  , { DatabaseError.CONNECTION_CLOSED,           "Connection closed." }
  , { DatabaseError.STATEMENT_CLOSED,            "Statement closed." }
  , { DatabaseError.STATEMENT_TIMEOUT,           "Statement timedout." }

     // DBS-00071 - 00080 access operational errors
  , { DatabaseError.SYNTAX_INVALID,              "There is an error in the SQL syntax: [%1$s]." }
  , { DatabaseError.OPERATION_FAILED,            "Operation reported as failed by Database Service." }
  , { DatabaseError.OPERATION_NOT_SUPPORTED,     "Operation not supported by Database Service." }
  , { DatabaseError.INSUFFICIENT_PRIVILEGE,      "Principal [%1$s] has insufficient privileges to perform operation [%2$s]." }
  , { DatabaseError.INSUFFICIENT_INFORMATION,    "Required field information not provided for [%1$s]." }
  , { DatabaseError.SEARCH_CONDITION_FAILED,     "Build of search condition [%1$s] failed." }

     // DBS-00081 - 00090 regular expresssion errors
  , { DatabaseError.EXPRESSION_BITVALUES,        "Undefined bit values are set in the regular expression compile options." }
  , { DatabaseError.EXPRESSION_INVALID,          "An error is contained in the regular expression [%1$s]: [%2$s]." }

     // DBS-00101 - 00120 object operational errors
  , { DatabaseError.OBJECT_NOT_CREATED,          "Entry [%1$s] was not created in [%2$s]." }
  , { DatabaseError.OBJECT_NOT_MODIFIED,         "Entry [%1$s] was not modified in [%2$s]." }
  , { DatabaseError.OBJECT_NOT_DELETED,          "Entry [%1$s] was not deleted in [%2$s]." }
  , { DatabaseError.OBJECT_ALREADY_EXISTS,       "Entry [%1$s] already exists in [%2$s]." }
  , { DatabaseError.OBJECT_NOT_EXISTS,           "Entry [%1$s] does not exists in [%2$s]." }
  , { DatabaseError.OBJECT_AMBIGUOUS,            "Entry [%1$s] is defined ambiguously in [%2$s]." }
  , { DatabaseError.PARENT_NOT_EXISTS,           "Superior entry [%1$s] does not exists for [%1$s] in [%2$s]." }
  , { DatabaseError.PARENT_AMBIGUOUS,            "Superior entry [%1$s] is defined ambiguously for [%1$s] in [%2$s]." }
  , { DatabaseError.PERMISSION_NOT_ASSIGNED,     "Not able to assign permission [%1$s] to [%2$s]." }
  , { DatabaseError.PERMISSION_NOT_REMOVED,      "Not able to remove permission [%1$s] from [%2$s]." }
  , { DatabaseError.PERMISSION_ALREADY_ASSIGNED, "Permission [%1$s] already assigned in [%2$s]." }
  , { DatabaseError.PERMISSION_ALREADY_REMOVED,  "Permission [%1$s] already removed  in [%2$s]." }

     // DBS-00091 - 00100 path expresssion errors
  , { DatabaseError.PATH_UNEXPECTED_EOS,         "Unexpected end of path string." }
  , { DatabaseError.PATH_UNEXPECTED_CHARACTER,   "Unexpected character [%1$s] at position \"%2%d\" for token starting at \"%3%d\"." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_PATH,  "Attribute path expected at position \"%1%d\"." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_NAME,  "Attribute name expected at position \"%1%d\"." }

     // DBS-01001 - 01010 system related messages
  , { DatabaseMessage.CONNECTING_BEGIN,          "Connecting to service URL [%1$s] with user [%2$s] ..." }
  , { DatabaseMessage.CONNECTING_SUCCESS,        "Connection to service URL [%1$s] for user [%2$s] established." }

     // DBS-01011 - 01020 system related messages
  , { DatabaseMessage.CONNECTION_ALIVE,          "Connection alive" }

     // DBS-01021 - 01030 system related messages
  , { DatabaseMessage.EXECUTE_STATEMENT,         "Executing: %1$s" }
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
  @Override
  public Object[][] getContents() {
    return CONTENT;
  }
}