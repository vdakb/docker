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

    File        :   DirectoryBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.connector.DirectoryError;
import oracle.iam.identity.icf.connector.DirectoryMessage;
import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;
////////////////////////////////////////////////////////////////////////////////
// class DirectoryBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
 ** @since   1.0.0.0
 */
public class DirectoryBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // BDS-00041 - 00050 control extension support related errors
    { DirectoryError.CONTROL_EXTENSION_NOT_EXISTS,    "Set of critical extensions absent" }
  , { DirectoryError.CONTROL_EXTENSION_NOT_SUPPORTED, "Kritische Erweiterung wird nicht unterstützt" }

    // BDS-00051 - 00060 naming related errors
  , { DirectoryError.ENCODING_NOT_SUPPORTED,          "Encoding Typ [%1$s] wird nicht unterstützt." }
  , { DirectoryError.OPERATION_NOT_SUPPORTED,         "Operation wird durch den Verzeichnisdienst nicht unterstützt. %2$s" }
  , { DirectoryError.OPERATION_NOT_PERMITTED,         "Erforderliche Berechtigung für Operation sind nicht vorhanden." }
  , { DirectoryError.INSUFFICIENT_INFORMATION,        "Erforderliche Attributinformation für [%1$s] sind nicht vorhanden." }
  , { DirectoryError.SEARCH_FILTER_INVALID,           "%s" }
  , { DirectoryError.OBJECT_NAME_INVALID,             "[%1$s] ergibt keinen gültigen Namen" }
  , { DirectoryError.PASSWORD_CHANGE_REQUIRES_TLS,    "Passwortoperation setzt Secur Socket Verbindung voraus." }

     // BDS-00091 - 00130 operational errors
  , { DirectoryError.ENTRY_EXISTS,                    "Eintrag [%1$s] existiert bereits. %2$s" }
  , { DirectoryError.ENTRY_AMBIGUOUS,                 "Eintrag ist mehrfach definiert für [%1$s]." }
  , { DirectoryError.ENTRY_NOT_FOUND,                 "Eintrag für [%1$s] wurde nicht gefunden." }
  , { DirectoryError.ENTRY_NOT_CREATED,               "Eintrag [%1$s] kann nicht erzeugt werden. %2$s" }
  , { DirectoryError.ENTRY_NOT_DELETED,               "Eintrag [%1$s] konnte nicht gelöscht werden. %2$s" }
  , { DirectoryError.ENTRY_NOT_ENABLED,               "Eintrag [%1$s] konnte nicht aktiviert werden. %2$s" }
  , { DirectoryError.ENTRY_NOT_DISABLED,              "Eintrag [%1$s] konnte nicht deaktiviert werden. %2$s" }
  , { DirectoryError.ENTRY_NOT_UPDATED,               "Eintrag [%1$s] konnte nicht modifiziert werden. %2$s" }
  , { DirectoryError.ENTRY_NOT_RENAMED,               "Eintrag [%1$s] konnte nicht umbenannt werden. %2$s" }
  , { DirectoryError.ENTRY_CONTEXT_NOT_EMPTY,         "Kontext [%1$s] ist nicht leer." }

  , { DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED,       "Schema Definition verletzt.%s" }
  , { DirectoryError.ATTRIBUTE_INVALID_DATA,          "Ungültige Daten für Attribut Typ." }
  , { DirectoryError.ATTRIBUTE_INVALID_TYPE,          "Ungültiger Attribut Typ. %1$s" }
  , { DirectoryError.ATTRIBUTE_INVALID_VALUE,         "Ungültiger Attribut Wert für [%1$s]: %2$s" }
  , { DirectoryError.ATTRIBUTE_INVALID_SIZE,          "Attribute ist nicht single-valued." }
  , { DirectoryError.ATTRIBUTE_IN_USE,                "Attribut wird noch verwendet." }
  , { DirectoryError.ATTRIBUTE_NOT_ASSIGNED,          "Attribut oder Attributezuweiung nicht erfolgt: [%1$s]." }
  , { DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED,      "Attribut oder Attributezuweiung existiert bereits: [%1$s]." }
  , { DirectoryError.ATTRIBUTE_NOT_REMOVED,           "Attribut oder Attributezuweiung nicht entfernt: [%1$s]." }
  , { DirectoryError.ATTRIBUTE_ALREADY_REMOVED,       "Attribut oder Attributezuweiung existiert entfernt: [%1$s]." }

    // BDS-00141 - 00150 changeLog related errors
  , { DirectoryError.CHANGELOG_NUMBER,                "Wert der Änderungsnummer [%1$s] konnte in rootDSE nicht gefunden werden" }

    // BDS-01001 - 01005 system related messages
  , { DirectoryMessage.CONNECTING_BEGIN,              "Verbindungsaufbau zu Service URL [%1$s] für Benutzer [%2$s] ..." }
  , { DirectoryMessage.CONNECTING_SUCCESS,            "Verbindung zu Service URL [%1$s] für Benutzer [%2$s] aufgebaut" }
  , { DirectoryMessage.ATTRIBUTE_ADDED,               "Attribut [%2$s] zu [%1$s] hinzugefügt" }
  , { DirectoryMessage.ATTRIBUTE_DELETED,             "Attribut [%2$s] von [%1$s] entfernt" }
  , { DirectoryMessage.ATTRIBUTE_TOMODIFY,            "Änderung von Attribut [%2$s] für [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_MODIFIED,            "Attribut [%2$s] für [%1$s] geändert" }
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