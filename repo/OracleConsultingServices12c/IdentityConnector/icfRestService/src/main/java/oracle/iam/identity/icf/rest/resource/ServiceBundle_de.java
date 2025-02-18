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
    Subsystem   :   Generic WebService Connector

    File        :   ServiceBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class ServiceBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GWS-00031 - 00040 authorization errors
    { ServiceError.OAUTH_FLOW_NOT_FINISH,          "Die Autorisierung ist nicht abgeschlossen und das Zugriffstoken wurde nicht empfangen. Rufen Sie start() und dann finish() auf, um die Autorisierung durchzuführen." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,         "Ungültiger \"state\"-Parameter. Der in der Autorisierungsanfrage verwendete \"state\" stimmt nicht mit dem \"state\" aus der Autorisierungsantwort überein." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,        "Fehler beim Anfordern des Zugriffstokens. Antwortstatus: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,       "Fehler beim Aktualisieren eines Zugriffstokens. Antwortstatus: %1$s." }

     // GWS-00061 - 00080 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,     "Unerwartetes Ende der Zeichenfolge im Pfad." }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,     "Unerwartetes Ende der Zeichenfolge im Filter." }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,      "Unerwartetes Zeichen '%s' an Position %d für Token beginnend bei %d." }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,          "Unerwartetes Token '%s'." }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,    "Unbekannter Attributoperator '%s' an Position %d. Erwartet: eq,ne,co,sw,ew,pr,gt,ge,lt,le." }
  , { ServiceError.PATH_INVALID_FILTER,            "Ungültiger Filter für Wert: %s." }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,        "'(' an Position %d erwartet." }
  , { ServiceError.PATH_INVALID_PARENTHESIS,       "Keine öffnende Klammer passt zu schließender Klammer an Position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,     "Attributpfad an Position %d erwartet." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,     "Attributname an Position %d erwartet." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,    "Ungültiger Attributpfad an Position %d: %s." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,    "Ungültiger Attributname ab Position %d: %s." }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,  "Ungültiger Vergleichswert an Position %d: %s." }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,       "Der Pfad kann nicht auf Unterattribute mit einer Tiefe von mehr als einer Ebene ausgerichtet werden." }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,      "Der Pfad darf keinen Wertfilter für Unterattribute enthalten." }

     // GWS-00081 - 00090 request parameter errors
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,   "'%s' ist kein gültiger Wert für den sortBy-Parameter: %s." }

    // GWS-00091 - 00100 filtering errors
  , { ServiceError.FILTER_METHOD_INCONSISTENT,     "übersetzungsmethode ist inkonsistent: %s" }
  , { ServiceError.FILTER_EXPRESSION_INCONSISTENT, "übersetzungsausdruck ist inkonsistent: %s" }
  , { ServiceError.FILTER_USAGE_INVALID_GE,        "Der Filter \"Größer oder gleich\" vergleicht möglicherweise keine booleschen oder binären Attributwerte." }
  , { ServiceError.FILTER_USAGE_INVALID_GT,        "Der Filter \"Größer als\" vergleicht keine booleschen oder binären Attributwerte." }
  , { ServiceError.FILTER_USAGE_INVALID_LE,        "Der Filter \"Kleiner oder gleich\" vergleicht keine booleschen oder binären Attributwerte." }
  , { ServiceError.FILTER_USAGE_INVALID_LT,        "Der Filter \"Kleiner als\" vergleicht keine booleschen oder binären Attributwerte." }

     // GWS-00101 - 00120 processing errors
  , { ServiceError.PROCESS_UNEXPECTED,             "Der Dienstanbieter ist auf eine unerwartete Bedingung gestoßen, die ihn daran gehindert hat, die Anforderung zu erfüllen." }
  , { ServiceError.PROCESS_UNAVAILABLE,            "Der Diensteanbieter kann die Anfrage aufgrund vorübergehender überlastung oder Wartung des Dienstes nicht bearbeiten. Der Dienstanbieter-REST-API-Dienst wird derzeit nicht ausgeführt." }
  , { ServiceError.PROCESS_AUTHORIZATION,          "Die Anfrage ist nicht autorisiert. Die in dieser Anforderung enthaltenen Authentifizierungsdaten fehlen oder sind ungültig." }
  , { ServiceError.PROCESS_FORBIDDEN,              "Die Anfrage war gültig, aber der Server lehnt ie Aktion ab. Der Benutzer verfügt möglicherweise nicht über die erforderlichen Berechtigungen für eine Ressource oder benötigt ein Benutzerkonto." }
  , { ServiceError.PROCESS_NOT_ALLOWED,            "Die in der Anfrage angegebene HTTP-Methode wird für diesen Anfrage-URI nicht unterstützt." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,         "Der Dienstanbieter kann keine Antwort erzeugen, die mit der Liste akzeptabler Werte übereinstimmt, die in den proaktiven Inhaltsverhandlungsheadern der Anforderung definiert sind, und dass der Dienstanbieter nicht bereit ist, eine Standarddarstellung bereitzustellen." }
  , { ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED,  "Der Dienstanbieter weigert sich, die Anforderung anzunehmen, da das Format der Nutzdaten nicht unterstützt wird." }
  , { ServiceError.PROCESS_EXISTS,                 "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,             "%s" }
  , { ServiceError.PROCESS_PRECONDITION,           "Fehler beim Ändern. Die Ressource hat sich beim Dienstanbieter geändert. Begründung des Hersteller: \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,          "Fehler beim Ändern. Ressource hat sich nicht geändert. Begründung des Hersteller: \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,              "Der Dienstanbieter weigert sich, eine Anfrage zu bearbeiten, da der Umfang der Nutzlast größer ist, als der Server verarbeiten will oder kann." }
  , { ServiceError.PROCESS_INVALID_VERSION,        "Die angegebene Version des SCIM-Protokolls wird nicht unterstützt." }
  , { ServiceError.PROCESS_TOO_MANY,               "Der angegebene Filter liefert mehr Ergebnisse, als der Server berechnen oder verarbeiten kann. Zum Beispiel würde ein Filter wie \"(userName pr)\" alle Einträge mit einem \"userName\" zurückgeben und kann so durch den Dienstanbieter akzeptiert sein." }
  , { ServiceError.PROCESS_MUTABILITY,             "Die versuchte Änderung ist nicht mit der Veränderlichkeit oder dem aktuellen Zustand des Zielattributs kompatibel (z. B. Änderung eines \"unveränderlichen\" Attributs mit einem vorhandenen Wert)." }
  , { ServiceError.PROCESS_SENSITIVE,              "Die angegebene Anfrage kann aufgrund der Weitergabe sensibler (z. B. personenbezogener) Informationen in einer Anfrage-URI nicht abgeschlossen werden." }
  , { ServiceError.PROCESS_UNIQUENESS,             "Einer oder mehrere der Attributwerte werden bereits verwendet oder sind reserviert." }
  , { ServiceError.PROCESS_NOTARGET,               "Der angegebene \"Pfad\" ergab kein Attribut oder keinen Wert, der bearbeitet werden konnte. Dies tritt auf, wenn der angegebene Wert des Pfads einen Filter enthält, der keine übereinstimmung liefert." }
  , { ServiceError.PROCESS_INVALID_FILTER,         "%s" }
  , { ServiceError.PROCESS_INVALID_PATH,           "%s" }
  , { ServiceError.PROCESS_INVALID_SYNTAX,         "%s" }
  , { ServiceError.PROCESS_INVALID_VALUE,          "%s" }

    // GWS-00131 - 00140 object errors
  , { ServiceError.OBJECT_NOT_EXISTS,              "%1$s ist beim Dienstanbieter nicht mit [%3$s] für [%2$s] verknüpft." }
  , { ServiceError.OBJECT_AMBIGUOUS,               "%1$s ist beim Dienstanbieter mehrdeutig mit [%3$s] für [%2$s] zugeordnet." }

     // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,           " ... und mehr ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,           "%s on thread %s\n" }
  , { ServiceMessage.LOGGER_CLIENT_REQUEST,        "Sende Client Anforderung" }
  , { ServiceMessage.LOGGER_CLIENT_RESPONSE,       "Client empfing Antwort" }
  , { ServiceMessage.LOGGER_SERVER_REQUEST,        "Server empfing eine Anforderung" }
  , { ServiceMessage.LOGGER_SERVER_RESPONSE,       "Server antwortete mit einer Antwort" }

     // GWS-01011 - 01020 connection messages
  , { ServiceMessage.CONNECTING_BEGIN,             "Verbindung zu Kontext URL [%1$s] mit Benutzer [%2$s] wird hergestellt..." }
  , { ServiceMessage.CONNECTING_SUCCESS,           "Verbindung zu Kontext URL [%1$s] für Benutzer [%2$s] wurde hergestellt." }
  , { ServiceMessage.OPERATION_BEGIN,              "Operation [%1$s] an Kontext URL [%2$s] mit Benutzer [%3$s] wurde gestarted..." }
  , { ServiceMessage.OPERATION_SUCCESS,            "Operation [%1$s] an Kontext URL [%2$s] mit Benutzer [%3$s] erfolgreich." }
  , { ServiceMessage.AUTHENTICATION_BEGIN,         "Authentisierung von Benutzer [%2$s] an Kontext URL [%1$s]..." }
  , { ServiceMessage.AUTHENTICATION_SUCCESS,       "Benutzer [%2$s] an Kontext URL [%1$s] authentisiert." }

     // GWS-01021 - 01030 reconciliation process related messages
  , { ServiceMessage.NOTHING_TO_CHANGE,            "Keine Änderungen im Diensteanbeiter vorgefunden." }

     // GWS-01031 - 01040 provisioning process related messages
  , { ServiceMessage.UNSPECIFIED_ERROR,            "Keine weiteren Angaben in der Fehlermeldung." }
  , { ServiceMessage.STATUS_NOT_PROVIDED,          "Status für Benutzerkonto nicht spezifiziert; setze als Aktiviert." }
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