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
    Subsystem   :   Common Shared Data Access Facilities

    File        :   DataAccessBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DataAccessBundle_en.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.dataaccess.DataAccessError;
import oracle.iam.identity.utility.dataaccess.DataAccessMessage;

////////////////////////////////////////////////////////////////////////////////
// class DataAccessBundle_en
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
 ** @version 1.0.0.0
 */
public class DataAccessBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // ????? - ????? commmon errors and messages
    { DataAccessMessage.USER,                    "User" }
  , { DataAccessMessage.GROUP,                   "Group" }
  , { DataAccessMessage.OBJECT,                  "Resource Object" }
  , { DataAccessMessage.PROCESSDEFINITION,       "Process Definition" }
  , { DataAccessMessage.PROCESSINSTANCE,         "Process Instance" }
  , { DataAccessMessage.FORMDEFINITION,          "Form Definition" }
  , { DataAccessMessage.FORMINSTANCE,            "Form Instance" }

  , { DataAccessMessage.MESSAGE,                 "%1$s" }

  , { DataAccessMessage.KEY_TORESOLVE,           "Try to resolve %1$s %2$s" }
  , { DataAccessMessage.KEY_RESOLVED,            "Internal key %2$s was resolved to %1$s %3$s" }
  , { DataAccessMessage.NAME_TORESOLVE,          "Try to resolve %1$s %2$s" }
  , { DataAccessMessage.NAME_RESOLVED,           "Internal key for %1$s %2$s is %3$s" }

  , { DataAccessError.NORESOURCE,                "Resource %1$s does not exists" }
  , { DataAccessError.NORESOURCEPROCESS,         "Resource %1$s does not have a process" }
  , { DataAccessError.NOPROCESSDEFINITION,       "Process Definition %1$s does not exists" }
  , { DataAccessError.NOPROCESSINSTANCE,         "Process Instance %1$s does not exists" }
  , { DataAccessError.NOFORMDEFINITION,          "Form Definition %1$s does not exists" }
  , { DataAccessError.NOFORMACTIVATED,           "Form Definition %1$s is not activated" }
  , { DataAccessError.NOFORMINSTANCE,            "Form Instance %1$s does not exists" }
  , { DataAccessError.NOFORMVERSION,             "Version of Form Definition %1$s does not exists" }
  , { DataAccessError.RESOURCEPROCESS_AMBIGUOUS, "Resource %1$s is ambiguous defined" }
  , { DataAccessError.RESOURCE_AMBIGUOUS,        "Resource %1$s is ambiguous defined" }
  , { DataAccessError.FORMVERSION_AMBIGUOUS,     "Version of Form Definition %1$s is ambiguous defined" }
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