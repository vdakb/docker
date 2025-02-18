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

    File        :   LDAPBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.file.LDAPError;
import oracle.iam.identity.utility.file.LDAPMessage;

////////////////////////////////////////////////////////////////////////////////
// class LDAPBundle_de
// ~~~~~ ~~~~~~~~~~~~~
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
public class LDAPBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // LDAP-00061 - LDAP-00080 file system naming and lookup related errors
    { LDAPError.FILENAME_MISSING,     "File missing: %1$s needs a single filename on the command line !"}
  , { LDAPError.FILEEXTENSION_IS_BAD, "Bad Extension: %1$s needs a single filename on the command line !"}
  , { LDAPError.NOTEXISTS,            "%1$s is invalid, because it doesn't exists" }
  , { LDAPError.NOTAFOLDER,           "Usage of %2$s as %1$s is invalid, because it's not a folder" }
  , { LDAPError.NOTAFILE,             "Usage of %2$s as %1$s is invalid, because it's not a file" }
  , { LDAPError.NOTREADABLE,          "Cannot use %2$s as %1$s, because it's not readable" }
  , { LDAPError.NOTWRITABLE,          "Cannot use %2$s as %1$s, because it's not writable" }
  , { LDAPError.NOTCREATABLE,         "Cannot create %1$s reason: %2$s" }
  , { LDAPError.NOTCLOSEDINPUT,       "Cannot close input stream %1$s" }
  , { LDAPError.NOTCLOSEDOUTPUT,      "Cannot close output stream %1$s" }

    // LDAP-00081 - 00090 parsing related errors
  , { LDAPError.SEPARATOR,            "Separator [%1$s] missing in [%2$s]" }
  , { LDAPError.OID_REQUIRED,         "OID required for control" }
  , { LDAPError.CRITICALITY_REQUIRED, "Criticality for control must be true or false, not [%1$s]" }
  , { LDAPError.DELETE_REQUIRED,      "Delete operation does not expect any value" }
  , { LDAPError.ATTRIBUTE_REQUIRED,   "Add operation needs a value for attribute [%1$s]" }

    // LDAP-01000 generic message template
  , { LDAPMessage.MESSAGE,            "%1$s" }

    // LDAP-01001 - LDAP-01010 processing messages
  , { LDAPMessage.EXPORT_BEGIN,       "Exporting to file %1$s" }
  , { LDAPMessage.EXPORT_FINISHED,    "File %1$s exported\n %2$s entries succeeded, %3$s entries skipped" }
  , { LDAPMessage.IMPORT_BEGIN,       "Importing from file %1$s" }
  , { LDAPMessage.IMPORT_FINISHED,    "File %1$s imported\n %2$s entries succeeded, %3$s entries skipped" }
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