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

    File        :   DataAccessBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DataAccessBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.dataaccess.DataAccessError;
import oracle.iam.identity.utility.dataaccess.DataAccessMessage;

////////////////////////////////////////////////////////////////////////////////
// class DataAccessBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class DataAccessBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    { DataAccessMessage.USER,                    "Benutzer" }
  , { DataAccessMessage.GROUP,                   "Benutzergruppe" }
  , { DataAccessMessage.OBJECT,                  "Resource Objekt" }
  , { DataAccessMessage.PROCESSDEFINITION,       "Prozess Definition" }
  , { DataAccessMessage.PROCESSINSTANCE,         "Prozess Instanz" }
  , { DataAccessMessage.FORMDEFINITION,          "Form Definition" }
  , { DataAccessMessage.FORMINSTANCE,            "Form Instanz" }

  , { DataAccessMessage.MESSAGE,                 "%1$s" }

  , { DataAccessMessage.KEY_TORESOLVE,           "Auflösung von %1$s %2$s" }
  , { DataAccessMessage.KEY_RESOLVED,            "Für Systemschlüssel %1$s wurde %2$s %3$s ermittelt" }
  , { DataAccessMessage.NAME_TORESOLVE,          "Auflösung von %1$s %2$s" }
  , { DataAccessMessage.NAME_RESOLVED,           "Systemschlüssel für %1$s %2$s ist %3$s" }

  , { DataAccessError.NORESOURCE,                "Resource %1$s existiert nicht" }
  , { DataAccessError.NORESOURCEPROCESS,         "Resource %1$s does not have a process" }
  , { DataAccessError.NOPROCESSDEFINITION,       "Prozess Definition %1$s existiert nicht" }
  , { DataAccessError.NOPROCESSINSTANCE,         "Prozess Instanz %1$s existiert nicht" }
  , { DataAccessError.NOFORMDEFINITION,          "Form Definition %1$s existiert nicht" }
  , { DataAccessError.NOFORMACTIVATED,           "Form Definition %1$s is not activated" }
  , { DataAccessError.NOFORMINSTANCE,            "Form Instanz %1$s does not exists" }
  , { DataAccessError.NOFORMVERSION,             "Version der Form Definition %1$s existiert nicht" }
  , { DataAccessError.RESOURCEPROCESS_AMBIGUOUS, "Resource %1$s ist mehrfach definiert" }
  , { DataAccessError.RESOURCE_AMBIGUOUS,        "Resource %1$s ist mehrfach definiert" }
  , { DataAccessError.FORMVERSION_AMBIGUOUS,     "Version der Form Definition %1$s ist mehrfach definiert" }
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