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

    File        :   DirectoryBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.ldap.DirectoryError;
import oracle.iam.identity.foundation.ldap.DirectoryMessage;

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
 */
public class DirectoryBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GDS-00001 - 00007 General error (Undefined)
    { DirectoryError.GENERAL,                          "Allgemeiner Fehler: %1$s" }
  , { DirectoryError.UNHANDLED,                        "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { DirectoryError.ABORT,                            "Die Verarbeitung wird abgebrochen: %1$s"}
  , { DirectoryError.NOTIMPLEMENTED,                   "Funktionalität ist nicht implementiert"}

     // GDS-00011 - 00012 method argument related errors
  , { DirectoryError.ARGUMENT_IS_NULL,                 "Argument [%1$s] darf nicht null sein" }

     // GDS-00021 - 00030 instance state related errors
  , { DirectoryError.INSTANCE_ATTRIBUTE_IS_NULL,       "Ungültiger Instanzzustand: Attribute [%1$s] ist nicht initialisiert" }

     // GDS-00031 - 00040 file related errors
  , { DirectoryError.FILE_MISSING,                     "Encountered problems to find file [%1$s]" }
  , { DirectoryError.FILE_IS_NOT_A_FILE,               "[%1$s] is not a file" }
  , { DirectoryError.FILE_OPEN,                        "Encountered problems to open file [%1$s]" }
  , { DirectoryError.FILE_CLOSE,                       "Encountered problems to close file [%1$s]" }
  , { DirectoryError.FILE_READ,                        "Encountered problems reading file [%1$s]" }
  , { DirectoryError.FILE_WRITE,                       "Encountered problems writing file [%1$s]" }

     // GDS-00061 - 00070 connectivity errors
  , { DirectoryError.CONNECTION_UNKNOWN_HOST,          "Host [%1$s] ist nicht bekannt" }
  , { DirectoryError.CONNECTION_CREATE_SOCKET,         "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden" }
  , { DirectoryError.CONNECTION_ERROR,                 "Die Verbindung zum Zielsystem kann nicht aufgebaut werden" }
  , { DirectoryError.CONNECTION_TIMEOUT,               "Die Verbindung zum Zielsystem wurde wegen Zeitüberschreitung abgebrochen: [%1$s]" }
  , { DirectoryError.CONNECTION_NOT_AVAILABLE,         "The problem may be with physical connectivity or Target System is not alive" }
  , { DirectoryError.CONNECTION_SSL_HANDSHAKE,         "Das SSL Zertifikat ist möglicherwiese nicht für das Zielsystem odnungsgemäß generiert worden" }
  , { DirectoryError.CONNECTION_SSL_ERROR,             "Not able to invalidate SSL session." }
  , { DirectoryError.CONNECTION_SSL_DESELECTED,        "Option SSL option ist in der IT Resource nicht ausgewählt." }
  , { DirectoryError.CONNECTION_AUTHENTICATION,        "Administrator [%1$s] oder Kennwort ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Zielsystem hergestellt werden" }
  , { DirectoryError.CONNECTION_ENCODING_NOTSUPPORTED, "URL Encoding [%1$s] wird nicht unterstützt" }

     // GDS-00071 - 00080 certificate related errors
  , { DirectoryError.CERTIFICATE_FILE_NOT_FOUND,       "Datei für Zertifikate [%1$s] ist nicht verfügbar." }
  , { DirectoryError.CERTIFICATE_TYPE_NOT_AVAILABLE,   "Zertifikatstype [%1$s] wird nicht durch einen Standard-Provider oder einem anderen Provider die durchsucht wurden bereitgestellt." }

     // GDS-00081 - 00090 control extension support related errors
  , { DirectoryError.CONTROL_EXTENSION_EXISTS,         "Set of critical extensions are: " }
  , { DirectoryError.CONTROL_EXTENSION_NOT_EXISTS,     "Set of critical extensions absent" }
  , { DirectoryError.CONTROL_EXTENSION_SUPPORTED,      "Kritische Erweiterung wird unterstützt." }
  , { DirectoryError.CONTROL_EXTENSION_NOT_SUPPORTED,  "Kritische Erweiterung wird nicht unterstützt" }

     // GDS-00091 - 00130 operational errors
  , { DirectoryError.ENCODING_TYPE_NOT_SUPPORTED,      "Encoding Typ [%1$s] wird nicht unterstützt" }
  , { DirectoryError.OBJECT_NOT_CREATED,               "Eintrag [%1$s] kann nicht erzeugt werden" }
  , { DirectoryError.OBJECT_NOT_DELETED,               "Eintrag [%1$s] konnte nicht gelöscht werden" }
  , { DirectoryError.OBJECT_ALREADY_EXISTS,            "Eintrag [%1$s] existiert bereits" }
  , { DirectoryError.OBJECT_NOT_EXISTS,                "Eintrag [%1$s] existiert nicht" }
  , { DirectoryError.OBJECT_NOT_ASSIGNED,              "Attribute können Eintrag [%1$s] nicht zugewiesen werden" }
  , { DirectoryError.OBJECT_ALREADY_ASSIGNED,          "Attribute wurden Eintrag [%1$s] bereits zugewiesen" }
  , { DirectoryError.OBJECT_ALREADY_REMOVED,           "Attribute wurden von Eintrag [%1$s] bereits entfernt" }
  , { DirectoryError.OBJECT_AMBIGUOUS,                 "Eintrag ist mehrfach definiert für [%1$s]" }
  , { DirectoryError.OBJECT_NOT_ENABLED,               "Eintrag [%1$s] konnte nicht aktiviert werden" }
  , { DirectoryError.OBJECT_NOT_DISABLED,              "Eintrag [%1$s] konnte nicht deaktiviert werden" }
  , { DirectoryError.OBJECT_NOT_UPDATED,               "Eintrag [%1$s] konnte nicht modifiziert werden" }
  , { DirectoryError.OBJECT_NOT_RENAMED,               "Eintrag [%1$s] konnte nicht umbenannt werden" }
  , { DirectoryError.ATTRIBUTE_SCHEMA_VIOLATED,        "Attribute not in mandatories or optionals of object classes" }
  , { DirectoryError.ATTRIBUTE_INVALID_DATA,           "Ungültige Daten für Attribut Typ" }
  , { DirectoryError.ATTRIBUTE_INVALID_TYPE,           "Ungültiger Attribut Typ" }
  , { DirectoryError.ATTRIBUTE_INVALID_SIZE,           "Attribute ist nicht single-valued" }
  , { DirectoryError.ATTRIBUTE_IN_USE,                 "Attribut wird noch verwendet" }
  , { DirectoryError.ATTRIBUTE_NOT_ASSIGNED,           "Attribut oder Attributezuweiung nicht erfolgt: [%1$s]" }
  , { DirectoryError.ATTRIBUTE_ALREADY_ASSIGNED,       "Attribut oder Attributezuweiung existiert bereits: [%1$s]" }
  , { DirectoryError.INSUFFICIENT_INFORMATION,         "Erforderliche Attributinformation für [%1$s] sind nicht vorhanden." }
  , { DirectoryError.OPERATION_NOT_SUPPORTED,          "Operation wird durch den Verzeichnisdienst nicht unterstützt." }
  , { DirectoryError.CONTEXT_NOT_EMPTY,                "Kontext [%1$s] ist nicht leer." }
  , { DirectoryError.PASSWORD_CHANGE_REQUIRES_SSL,     "Passwortoperation setzt Secur Socket Verbindung voraus" }
  , { DirectoryError.HIERARCHY_PATH_NOT_RESOLVED,      "Target hierarchy path [%1$s] not resolved}" }
  , { DirectoryError.ROLE_PATH_NOT_RESOLVED,           "Target role path [%1$s] not resolved}" }
  , { DirectoryError.GROUP_PATH_NOT_RESOLVED,          "Target group path [%1$s] not resolved}" }
  , { DirectoryError.HIERARCHY_PATH_NOT_EXISTS,        "Target hierarchy path [%1$s] does not exists}" }

    // GDS-00141 - 00150 file parsing related errors
  , { DirectoryError.LINE,                             "Zeile %1$s: %2$s" }
  , { DirectoryError.UNEXPECTED,                       "Unerwartet [%1$s]" }
  , { DirectoryError.MISSING_SEPARATOR,                "Ein gültiges Trennzeichen wurde erwartet" }
  , { DirectoryError.INVALID_SEPARATOR,                "[%1$s] ist kein gültiges Trennzeichen" }
  , { DirectoryError.EXPECTING_SEPARATOR,              "Erwartetes Trennzeichen [%1$s] ist in Zeile %2$s nicht vorhanden" }
  , { DirectoryError.EXPECTING_PREFIX,                 "Zeichen [%1$s] wurde erwartet in Zeile %2$s" }
  , { DirectoryError.LINE_PARSER_NOWHERE,              "Continuation out of nowhere" }
  , { DirectoryError.CONSTRUCT_STRING,                 "%1$s: Zeichenkette [%2$s] kann nicht erzeugt werden" }
  , { DirectoryError.CONSTRUCT_URL,                    "%1$s: URL [%2$s] kann nicht erzeugt werden" }
  , { DirectoryError.CHANGE_TYPE_UNKNOW,               "Unbekannter Change Type [%1$s]" }
  , { DirectoryError.CHANGE_OPERATION_UNKNOW,          "Unbekannte Change Operation [%1$s]" }
  , { DirectoryError.CHANGE_TYPE_NOTSUPPORTED,         "Change Type [%1$s] wird nicht unterstützt" }

    // GDS-01001 - 01005 system related messages
  , { DirectoryMessage.CONNECTING_TO,                  "Verbindungsaufbau zu [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_ADDED,                "Attribut [%2$s] zu [%1$s] hinzugefügt" }
  , { DirectoryMessage.ATTRIBUTE_DELETED,              "Attribut [%2$s] von [%1$s] entfernt" }
  , { DirectoryMessage.ATTRIBUTE_TOMODIFY,             "Änderung von Attribut [%2$s] für [%1$s]" }
  , { DirectoryMessage.ATTRIBUTE_MODIFIED,             "Attribut [%2$s] für [%1$s] geändert" }
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