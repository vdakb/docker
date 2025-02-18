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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ServiceBundle_de.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

import oracle.hst.platform.core.utility.ListResourceBundle;

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
     // GWS-00001 - GWS-00010 General error (Undefined)
    { ServiceError.UNHANDLED,                         "Eine unbehandelte Ausnahme is aufgetreten: %1$s" }

     // GWS-00011 - 00020 method argument related errors
  , { ServiceError.ARGUMENT_IS_NULL,                  "Übergebenes Argument [%1$s] darf nicht null sein." }
  , { ServiceError.ARGUMENT_BAD_TYPE,                 "Übergebenes Argument [%1$s] ist vom falschen Typ." }
  , { ServiceError.ARGUMENT_BAD_VALUE,                "Übergebenes Argument [%1$s] enthält einen ungültigen Wert." }
  , { ServiceError.ARGUMENT_SIZE_MISMATCH,            "Größe des übergebenen Array-Arguments stimmt nicht mit der erwarteten Größe überein." }

     // GWS-00021 - 00030 instance state related errors
  , { ServiceError.INSTANCE_ATTRIBUTE_IS_NULL,        "Ungültiger Instanzstatus: Attribut [%1$s] muss initialisiert werden." }
  , { ServiceError.INSTANCE_ILLEGAL_STATE,            "Ungültiger Status der Instanz: Attribut [%1$s] bereits initialisiert."}

     // GWS-00031 - 00040 connectivity errors
  , { ServiceError.CONNECTION_UNKNOWN_HOST,           "Host [%1$s] ist nicht bekannt." }
  , { ServiceError.CONNECTION_CREATE_SOCKET,          "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { ServiceError.CONNECTION_SECURE_SOCKET,          "Die gesicherte Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { ServiceError.CONNECTION_CERTIFICATE_HOST,       "Es konnte kein gültiger Pfad zum Zertifikat für das angeforderte Ziel [%1$s] gefunden werden." }
  , { ServiceError.CONNECTION_ERROR,                  "Die Netzwerkverbindung kann nicht aufgebaut werden." }
  , { ServiceError.CONNECTION_TIMEOUT,                "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] wurde wegen Zeitüberschreitung abgebrochen." }
  , { ServiceError.CONNECTION_NOT_AVAILABLE,          "The problem may be with physical connectivity or Service Provider is not alive." }
  , { ServiceError.CONNECTION_AUTHENTICATION,         "Schlüssel ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Dienstanbieter hergestellt werden." }
  , { ServiceError.CONNECTION_AUTHORIZATION,          "Benutzerkonto [%1$s] ist nicht autorisiert." }

     // GWS-00041 - 00050 request method errors
  , { ServiceError.REQUEST_METHOD_NOTALLOWED,         "Method not allowed." }
  , { ServiceError.REQUEST_METHOD_NOTIMPLEMENTED,     "Method not implemented." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED, "Methode erwartet, dass eine ID ausgeführt wird." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID,  "Ressource gibt ID [%s] in der Nutzlast für Ressourcentyp [%s] an." }
  , { ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND,    "Ressource mit ID [%s] existiert nicht für Ressourcentyp [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EXISTS,      "Ressource mit dem Namen [%s] existiert bereits für den Ressourcentyp [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EMPTY,       "Leere Entität." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE,   "Attribut %s:%s darf nicht null sein." }

     // GWS-00051 - 00060 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,        "Unerwartetes Ende der Pfadzeichenfolge." }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,        "Unerwartetes Ende der Filterzeichenfolge." }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,         "Unerwartetes Zeichen [%s] an Position %d für Token beginnend bei %d." }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,             "Unerwartetes Token [%s]." }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,       "Nicht erkannter Attributoperator [%s] an Position %d. Erwartet: eq,ne,co,sw,ew,pr,gt,ge,lt,le!" }
  , { ServiceError.PATH_INVALID_FILTER,               "Ungültiger Wertefilter: [%s]." }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,           "'(' an Position %d erwartet." }
  , { ServiceError.PATH_INVALID_PARENTHESIS,          "Keine öffnende Klammer passend zur schließenden Klammer an Position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,        "Pfad zu Attribut an Position %d erwartet." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,        "Name eines Attribut an Position %d erwartet." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,       "Ungültiger Attributpfad an Position %d: [%s]" }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,       "Ungültiger Attributname ab Position %d: [%s]" }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,     "Ungültiger Vergleichswert an Position %d: [%s]" }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,          "Pfad kann nicht auf Unterattribute abzielen, die mehr als eine Ebene tiefer liegen." }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,         "Pfad kann keinen Wertfilter für Unterattribute enthalten." }
  , { ServiceError.PATH_INVALID_VALUE_ENCODING,       "Wert am Pfad [%s] ist keine gültige [%s]-Zeichenkette." }

     // GWS-00071 - 00080 request parameter errors
  , { ServiceError.PARAMETER_FILTER_NOTPERMITTED,     "Filtern nicht erlaubt." }
  , { ServiceError.PARAMETER_START_INVALID_VALUE,     "[%s] ist kein gültiger Wert für den Parameter startIndex." }
  , { ServiceError.PARAMETER_COUNT_INVALID_VALUE,     "[%s] ist kein gültiger Wert für den Parameter count." }
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,      "[%s] ist kein gültiger Wert für den Parameter sortBy: [%s]." }
  , { ServiceError.PARAMETER_ORDER_INVALID_VALUE,     "[%s] ist kein gültiger Wert für den Parameter sortOrder: [%s]." }

     // GWS-00081 - 0090 filtering errors
  , { ServiceError.FILTER_INCONSISTENT_METHOD,        "Methode ist inkonsistent: [%s]." }
  , { ServiceError.FILTER_INCONSISTENT_EXPRESSION,    "Ausdruck ist inkonsistent: [%s]." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_LT,      "Filter kleiner-als kann keine booleschen oder binären Attributwerte vergleichen." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_LE,      "Filter kleiner-oder-gleich-als kann keine booleschen oder binären Attributwerte vergleichen." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_GT,      "Filter größer-als kann keine booleschen oder binären Attributwerte vergleichen." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_GE,      "Filter größer-oder-gleich-als kann keine booleschen oder binären Attributwerte vergleichen." }
  , { ServiceError.FILTER_INVALID_TARGET_TYPE,        "Attribut [%s] hat keinen mehrwertigen oder komplexen Wert." }
  , { ServiceError.FILTER_INVALID_TARGET_MATCH,       "Attribut [%s] hat keinen Wert, der dem Filter [%s] entspricht." }

     // GWS-00091 - 00120 processing errors
  , { ServiceError.PROCESS_UNEXPECTED,                "Der Dienstanbieter ist auf eine unerwartete Bedingung gestoßen, die ihn daran gehindert hat, die Anforderung zu erfüllen." }
  , { ServiceError.PROCESS_UNAVAILABLE,               "Der Diensteanbieter kann die Anfrage aufgrund vorübergehender überlastung oder Wartung des Dienstes nicht bearbeiten. Der Dienstanbieter-REST-API-Dienst wird derzeit nicht ausgeführt." }
  , { ServiceError.PROCESS_AUTHORIZATION,             "Die Anfrage ist nicht autorisiert. Die in dieser Anforderung enthaltenen Authentifizierungsdaten fehlen oder sind ungültig." }
  , { ServiceError.PROCESS_FORBIDDEN,                 "Die Anfrage war gültig, aber der Server lehnt ie Aktion ab. Der Benutzer verfügt möglicherweise nicht über die erforderlichen Berechtigungen für eine Ressource oder benötigt ein Benutzerkonto." }
  , { ServiceError.PROCESS_NOT_ALLOWED,               "Die in der Anfrage angegebene HTTP-Methode wird für diesen Anfrage-URI nicht unterstützt." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,            "Der Dienstanbieter kann keine Antwort erzeugen, die mit der Liste akzeptabler Werte übereinstimmt, die in den proaktiven Inhaltsverhandlungsheadern der Anforderung definiert sind, und dass der Dienstanbieter nicht bereit ist, eine Standarddarstellung bereitzustellen." }
  , { ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED,     "Der Dienstanbieter weigert sich, die Anforderung anzunehmen, da das Format der Nutzdaten nicht unterstützt wird." }
  , { ServiceError.PROCESS_EXISTS,                    "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,                "%s" }
  , { ServiceError.PROCESS_PRECONDITION,              "Fehler beim Ändern. Die Ressource hat sich beim Dienstanbieter geändert. Begründung des Hersteller: \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,             "Fehler beim Ändern. Ressource hat sich nicht geändert. Begründung des Hersteller: \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,                 "Der Dienstanbieter weigert sich, eine Anfrage zu bearbeiten, da der Umfang der Nutzlast größer ist, als der Server verarbeiten will oder kann." }
  , { ServiceError.PROCESS_INVALID_VERSION,           "Die angegebene Version des SCIM-Protokolls wird nicht unterstützt." }
  , { ServiceError.PROCESS_TOO_MANY,                  "Der angegebene Filter liefert mehr Ergebnisse, als der Server berechnen oder verarbeiten kann. Zum Beispiel würde ein Filter wie \"(userName pr)\" alle Einträge mit einem \"userName\" zurückgeben und kann so durch den Dienstanbieter akzeptiert sein." }
  , { ServiceError.PROCESS_MUTABILITY,                "Die versuchte Änderung ist nicht mit der Veränderlichkeit oder dem aktuellen Zustand des Zielattributs kompatibel (z. B. Änderung eines \"unveränderlichen\" Attributs mit einem vorhandenen Wert)." }
  , { ServiceError.PROCESS_SENSITIVE,                 "Die angegebene Anfrage kann aufgrund der Weitergabe sensibler (z. B. personenbezogener) Informationen in einer Anfrage-URI nicht abgeschlossen werden." }
  , { ServiceError.PROCESS_UNIQUENESS,                "Einer oder mehrere der Attributwerte werden bereits verwendet oder sind reserviert." }
  , { ServiceError.PROCESS_NOTARGET,                  "Der angegebene \"Pfad\" ergab kein Attribut oder keinen Wert, der bearbeitet werden konnte. Dies tritt auf, wenn der angegebene Wert des Pfads einen Filter enthält, der keine übereinstimmung liefert." }
  , { ServiceError.PROCESS_INVALID_FILTER,            "%s" }
  , { ServiceError.PROCESS_INVALID_PATH,              "%s" }
  , { ServiceError.PROCESS_INVALID_SYNTAX,            "%s" }
  , { ServiceError.PROCESS_INVALID_VALUE,             "%s" }

     // GWS-00121 - 00130 patch operation errors
  , { ServiceError.PATCH_OPERATIONTYPE_UNKNOWN,       "Unbekannter Patch-Operationtyp [%s]" }
  , { ServiceError.PATCH_MULTIVALUE_NOTPERMITTED,     "Patch-Operation enthält mehrere Werte." }
  , { ServiceError.PATCH_OPERATION_VALUE_NOTNULL,     "Wertefeld darf nicht leer oder ein leerer Container sein."}
  , { ServiceError.PATCH_OPERATION_ADD_OBJECT,        "Wertefeld muss ein JSON-Objekt sein, das die hinzuzufügenden Attribute enthält." }
  , { ServiceError.PATCH_OPERATION_ADD_ARRAY,         "Wertefeld muss ein JSON-Array sein, das die hinzuzufügenden Attribute enthält." }
  , { ServiceError.PATCH_OPERATION_ADD_PATH,          "Pfad darf keinen Filter für Werteauswahl bei dieser Operation enthalten." }
  , { ServiceError.PATCH_OPERATION_REPLACE_VALUE,     "Wertefeld muss ein JSON-Objekt sein, das die zu ersetzenden Attribute enthält." }
  , { ServiceError.PATCH_OPERATION_REMOVE_PATH,       "Pfad darf bei Löschvorgängen nicht leer sein." }

     // GWS-00131 - 00140 resource operational state errors
  , { ServiceError.RESOURCE_EXISTS,                   "Ressource vom Typ [%2$s] mit der ID [%1$s] existiert bereits." }
  , { ServiceError.RESOURCE_NOT_EXISTS,               "Ressource vom Typ [%2$s] mit der ID [%1$s] existiert nicht." }
  , { ServiceError.RESOURCE_NOT_CREATED,              "Ressource des Typs [%2$s] mit der ID [%1$s] kann nicht erstellt werden." }
  , { ServiceError.RESOURCE_NOT_MODIFIED,             "Ressource vom Typ [%2$s] mit der ID [%1$s] kann nicht geändert werden." }
  , { ServiceError.RESOURCE_NOT_DELETED,              "Ressource vom Typ [%2$s] mit der ID [%1$s] kann nicht gelöscht werden." }
  , { ServiceError.RESOURCE_MATCH_IDENTIFIER,         "Der in den Nutzdatendaten angegebene Bezeichner [%1$s] stimmt nicht mit dem im Ressourcenpfad [%2$s] überein." }

     // GWS-00141 - 00150 http status related errors
  , { ServiceError.HTTP_CODE_NOP,                     "HTTP-NOP: Ein unbekannter Antwortstatus [%1$s] wurde vom Endpunkt empfangen."}
  , { ServiceError.HTTP_CODE_400,                     "HTTP-400: Die Anfrage konnte vom Server aufgrund einer fehlerhaften Syntax nicht verstanden werden. Der Client SOLLTE die Anfrage NICHT unverändert wiederholen."}
  , { ServiceError.HTTP_CODE_401,                     "HTTP-401: Die Anfrage wurde nicht abgeschlossen, da gültige Authentifizierungsdaten für die angeforderte Ressource fehlen."}
  , { ServiceError.HTTP_CODE_403,                     "HTTP-403: Die Anfrage war gültig, aber der Server lehnt eine Aktion ab. Der Benutzer verfügt möglicherweise nicht über die erforderlichen Berechtigungen für eine Ressource oder benötigt ein bestimmtes Konto."}
  , { ServiceError.HTTP_CODE_404,                     "HTTP-404: Die angeforderte Ressource konnte nicht gefunden werden, könnte aber in Zukunft verfügbar sein. Nachfolgende Anfragen des Clients sind zulässig."}
  , { ServiceError.HTTP_CODE_408,                     "HTTP-408: Der Server hat eine Zeitüberschreitung beim Warten auf die Anfrage."}
  , { ServiceError.HTTP_CODE_409,                     "HTTP-409: Die Anforderung konnte aufgrund eines Konflikts mit dem aktuellen Status der Zielressource nicht abgeschlossen werden."}
  , { ServiceError.HTTP_CODE_415,                     "HTTP-415: Der Server hat diese Anforderung abgelehnt, da die Entität der Anfrage in einem Format vorliegt, das von der angeforderten Ressource für die angeforderte Methode nicht unterstützt wird."}
  , { ServiceError.HTTP_CODE_500,                     "HTTP-500: Der Server hat einen internen Fehler oder eine Fehlkonfiguration festgestellt und konnte Ihre Anfrage nicht abschließen."}
  , { ServiceError.HTTP_CODE_501,                     "HTTP-501: Der Server erkennt entweder die Anfragemethode nicht, oder er ist nicht in der Lage, die Anfrage zu erfüllen. Normalerweise impliziert dies eine zukünftige Verfügbarkeit (z.B. eine neue Funktion einer Web-Service-API)."}
  , { ServiceError.HTTP_CODE_502,                     "HTTP-502: Der Server fungierte als Gateway oder Proxy und erhielt eine ungültige Antwort vom Upstream-Server."}
  , { ServiceError.HTTP_CODE_503,                     "HTTP 503: Der Server erkennt entweder die Anfragemethode nicht, oder er ist nicht in der Lage, die Anfrage zu erfüllen. Normalerweise impliziert dies eine zukünftige Verfügbarkeit (z.B. eine neue Funktion einer Web-Service-API)."}
  , { ServiceError.HTTP_CODE_504,                     "HTTP 504: Der Server fungierte als Gateway oder Proxy und erhielt nicht rechtzeitig eine Antwort vom Upstream-Server."}

     // GWS-00151 - 00160 authorization errors
  , { ServiceError.OAUTH_FLOW_NOT_FINISH,             "Die Autorisierung ist nicht abgeschlossen und das Zugriffstoken wurde nicht empfangen. Rufen Sie start() und dann finish() auf, um die Autorisierung durchzuführen." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,            "Ungültiger \"state\"-Parameter. Der in der Autorisierungsanfrage verwendete \"state\" stimmt nicht mit dem \"state\" aus der Autorisierungsantwort überein." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,           "Fehler beim Anfordern des Zugriffstokens. Antwortstatus: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,          "Fehler beim Aktualisieren eines Zugriffstokens. Antwortstatus: %1$s." }

     // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,              " ... und mehr ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,              "%s on thread %s\n" }
  , { ServiceMessage.LOGGER_CLIENT_REQUEST,           "Sende Client Anforderung" }
  , { ServiceMessage.LOGGER_CLIENT_RESPONSE,          "Client empfing Antwort" }
  , { ServiceMessage.LOGGER_SERVER_REQUEST,           "Server empfing eine Anforderung" }
  , { ServiceMessage.LOGGER_SERVER_RESPONSE,          "Server antwortete mit einer Antwort" }
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