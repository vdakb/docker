/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   HarvesterBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    HarvesterBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2012-28-10  DSteding    First release version
*/

package oracle.iam.identity.ots.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.ots.service.catalog.HarvesterError;
import oracle.iam.identity.ots.service.catalog.HarvesterMessage;

////////////////////////////////////////////////////////////////////////////////
// class HarvesterBundle_de
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class HarvesterBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // ARC-00001 - ARC-00010 task related errors
    { HarvesterError.UNHANDLED,                  "Eine unbehandelte Ausnahme ist aufgetreten: %1$s"}
  , { HarvesterError.GENERAL,                    "Allgemeiner Fehler: %1$s" }
  , { HarvesterError.ABORT,                      "Die Verarbeitung wird abgebrochen: %1$s"}
  , { HarvesterError.NOTIMPLEMENTED,             "Funktionalität ist nicht implementiert"}

     // ARC-00011 - ARC-00020 import process related erorrs
  , { HarvesterError.CATALOG_NOTFOUND,           "Katalogelement vom Typ [%1$s] mit dem Identifier [%2$s] existiert nicht" }
  , { HarvesterError.CATALOG_AMBIGUOUS,          "Katalogelement vom Typ [%1$s] mit dem Identifier [%2$s] ist mehrfach definiert" }
  , { HarvesterError.ROLE_NOTFOUND,              "Rolle [%1$s] existiert nicht" }
  , { HarvesterError.ROLE_AMBIGUOUS,             "Rolle [%1$s] ist mehrfach definiert" }
  , { HarvesterError.INSTANCE_NOTFOUND,          "Application Instance [%1$s] existiert nicht" }
  , { HarvesterError.INSTANCE_AMBIGUOUS,         "Application Instance [%1$s] ist mehrfach definiert" }
  , { HarvesterError.INSTANCE_LOOKUP_NOTFOUND,   "Application Instance für Resource Objekt [%1$s] und IT Resource [%2$s] existiert nicht" }
  , { HarvesterError.INSTANCE_LOOKUP_AMBIGUOUS,  "Application Instance für Resource Objekt [%1$s] und IT Resource [%2$s] ist mehrfach definiert" }
  , { HarvesterError.INSTANCE_MANDATORY,         "Ein oder mehrere Application Instance sind erforderlich" }
  , { HarvesterError.NAMEPACE_NOTFOUND,          "Entitlement Namespace [%1$s] existiert nicht innerhalb der Application Instance [%2$s]" }
  , { HarvesterError.NAMEPACE_AMBIGUOUS,         "Entitlement Namespace [%1$s] ist innerhalb des Application Instance [%2$s] mehrfach definiert" }
  , { HarvesterError.ENTITLEMENT_NOTFOUND,       "Berechtigung [%1$s] existiert nicht innerhalb des Application Instance Namespace [%2$s]" }
  , { HarvesterError.ENTITLEMENT_AMBIGUOUS,      "Berechtigung [%1$s] ist innerhalb des Application Instance Namespace [%2$s] mehrfach definiert" }
  , { HarvesterError.ENTITLEMENT_MANDATORY,      "Ein oder mehrere Berechtigungen sind erforderlich" }

     // ARC-00031 - ARC-00040 import process related erorrs
  , { HarvesterError.MODIFY_CATALOG,             "Katalog [%1$s] [%2$s] konnte nicht modifiziert werden" }
  , { HarvesterError.MODIFY_CATALOG_IGNORED,     "Änderung des Katalog [%1$s] [%2$s] wurde ignoriert" }
  , { HarvesterError.MODIFY_ROLE,                "Rolle [%1$s] konnte nicht modifiziert werden" }
  , { HarvesterError.MODIFY_ROLE_IGNORED,        "Änderung der Rolle [%1$s] wurde ignoriert" }
  , { HarvesterError.MODIFY_INSTANCE,            "Application Instance [%1$s] konnte nicht modifiziert werden" }
  , { HarvesterError.MODIFY_INSTANCE_IGNORED,    "Änderung der Application Instance [%1$s] wurde ignoriert" }
  , { HarvesterError.MODIFY_ENTITLEMENT,         "Berechtigung [%1$s] konnte nicht modifiziert werden" }
  , { HarvesterError.MODIFY_ENTITLEMENT_IGNORED, "Änderung der Berechtigung [%1$s] wurde ignoriert" }

     // ARC-00041 - ARC-00050 import process related erorrs
  , { HarvesterError.OBJECT_ELEMENT_NOTFOUND,    "%1$s [%2$s] existiert nicht" }
  , { HarvesterError.OBJECT_ELEMENT_EXISTS,      "%1$s [%2$s] existiert bereits im System. Die existierende Rolle wird modifiziert." }
  , { HarvesterError.OBJECT_ELEMENT_AMBIGUOUS,   "%1$s [%2$s] ist mehrfach definiert" }
  , { HarvesterError.OBJECT_ATTRIBUTE_MISSING,   "Zuwenig Attribute um %1$s [%2$s] zu erzeugen" }

     // ARC-00051 - ARC-00060 import process related errors
  , { HarvesterError.OPERATION_UNSUPPORTED,      "Operation [%1$s] kann für [%2$s] nicht ausgeführt werden" }
  , { HarvesterError.OPERATION_EXPORT_FAILED,    "Export von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_IMPORT_FAILED,    "Import von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_CREATE_FAILED,    "Erzeugen von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_DELETE_FAILED,    "Löschen von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_ENABLE_FAILED,    "Aktivierung von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_DISABLE_FAILED,   "Deaktivierung of %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_MODIFY_FAILED,    "Modifikation von %1$s [%2$s] ist fehlgeschlagen. Grund:\n%3$s"}
  , { HarvesterError.OPERATION_ASSIGN_FAILED,    "Zuweisung von %1$s [%2$s] zu %3$s [%4$s] ist fehlgeschlagen. Grund:\n%5$s"}
  , { HarvesterError.OPERATION_REVOKE_FAILED,    "Entzug von %1$s [%2$s] von %3$s [%4$s] ist fehlgeschlagen. Grund:\n%5$s"}

     // ARC-00061 - ARC-00070 request payload related errors
  , { HarvesterError.REQUEST_PAYLOAD_EMPTY,      "Payload ist leer" }
  , { HarvesterError.REQUEST_PAYLOAD_INCOMPLETE, "Payload verletzt Regeln des XML-Schema" }
  , { HarvesterError.REQUEST_APPLICATION_MISSED, "Ein oder mehrere Application Instanzen konnten nicht transformaiert werden" }
  , { HarvesterError.REQUEST_NAMESPACE_MISSED,   "Ein oder mehrere Namensräume von Berechtigungen konnten nicht transformaiert werden" }
  , { HarvesterError.REQUEST_ENTITLEMENT_MISSED, "Ein oder mehrere Berechtigungen konnten nicht transformaiert werden" }

     // ARC-01001 - ARC-01010 import process related messages
  , { HarvesterMessage.IMPORTING_BEGIN,          "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde gestartet ..." }
  , { HarvesterMessage.IMPORTING_COMPLETE,       "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde beendet" }
  , { HarvesterMessage.IMPORTING_STOPPED,        "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde mit Fehlermeldung [%4$s] abgebrochen" }
  , { HarvesterMessage.IMPORTING_SUCCESS,        "%1$s Einträge wurden für [%2$s] importiert" }
  , { HarvesterMessage.IMPORTING_ERROR,          "Eintrag [%1$s] wurde nicht für [%2$s] importiert" }
  , { HarvesterMessage.IMPORT_BEGIN,             "Import der Daten für [%1$s] wurde gestartet ..." }
  , { HarvesterMessage.IMPORT_COMPLETE,          "Import der Daten für [%1$s] wurde beendet" }
  , { HarvesterMessage.IMPORT_SKIP,              "Import der Daten auf Grund Benutzerkonfigutaion übersprungen" }
  , { HarvesterMessage.IMPORT_CATALOG_SUMMARY,   "[%1$s] Katalogeinträge wurden erfolgreich modifiziert, [%2$s] Katalogeinträge wurden ignoriert, [%3$s] Katalogeinträge sind fehlgeschlagen" }

     // ARC-01011 - ARC-01020 export process related messages
  , { HarvesterMessage.EXPORTING_BEGIN,          "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde gestartet ..." }
  , { HarvesterMessage.EXPORTING_COMPLETE,       "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde beendet" }
  , { HarvesterMessage.EXPORTING_STOPPED,        "[%2$s] für [%1$s] unter Verwendung von Datei [%3$s] wurde mit Fehlermeldung [%4$s] abgebrochen" }
  , { HarvesterMessage.EXPORTING_SUCCESS,        "%1$s Einträge wurden mit [%2$s] exportiert" }
  , { HarvesterMessage.EXPORTING_ERROR,          "Eintrag [%1$s] wurde nicht mit [%2$s] exportiert" }

  , { HarvesterMessage.COLLECTING_BEGIN,         "Starte Zusammenstellung der Daten ..." }
  , { HarvesterMessage.COLLECTING_COMPLETE,      "Zusammenstellung der Daten beendet" }

     // ARC-01031 - ARC-01040 object/entity operation related messages
  , { HarvesterMessage.OPERATION_CREATE_BEGIN,   "Erzeuge %1$s [%2$s] ..."}
  , { HarvesterMessage.OPERATION_CREATE_SUCCESS, "%1$s [%2$s] erzeugt"}
  , { HarvesterMessage.OPERATION_MODIFY_BEGIN,   "Modifiziere %1$s [%2$s] ..."}
  , { HarvesterMessage.OPERATION_MODIFY_SUCCESS, "%1$s [%2$s] modifiziert"}

     // ARC-01041 - ARC-01050 web service operation related messages
  , { HarvesterMessage.SERVICE_REQUEST_PAYLOAD,  "Serviceaufruf erfolgt mit Payload\n%1$s"}
  , { HarvesterMessage.SERVICE_RESPONSE_PAYLOAD, "Serviceantwort ist Payload\n%1$s"}
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