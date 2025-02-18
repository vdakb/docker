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
    Subsystem   :   Common Shared Utility

    File        :   SystemBundle_de.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SystemBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class SystemBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OSF-00001 - 00010 system related errors
    { SystemError.UNHANDLED,                       "Eine unbehandelte Ausnahme is aufgetreten: %1$s" }
  , { SystemError.GENERAL,                         "Allgemeiner Fehler: %1$s" }
  , { SystemError.ABORT,                           "Die Verarbeitung wird abgebrochen: %1$s" }
  , { SystemError.NOTIMPLEMENTED,                  "Funktionalität ist nicht implementiert." }
  , { SystemError.CLASSNOTFOUND,                   "Klasse %1$s konnte im Klassenpfad nicht gefunden werden." }
  , { SystemError.CLASSNOTCREATE,                  "Klasse %1$s konnte nicht erzeugt werden." }
  , { SystemError.CLASSNOACCESS,                   "Zugriff auf Klasses %1$s ist nicht erlaubt." }
  , { SystemError.CLASSINVALID,                    "Klasse %1$s muss eine Unterklasse von %2$s sein." }
  , { SystemError.CLASSMETHOD,                     "Klasse %1$s hat keine Methode für die Parameter %2$s." }
  , { SystemError.CLASSCONSTRUCTORDEFAULT,         "Klasse %1$s hat keinen Konstruktor ohne Argumente." }

     // OSF-00011 - 00020 method argument related errors
  , { SystemError.ARGUMENT_IS_NULL,                "Argument %1$s darf nicht null sein." }
  , { SystemError.ARGUMENT_BAD_TYPE,               "Argument %1$s hat nicht den erforderlichen Type." }
  , { SystemError.ARGUMENT_BAD_VALUE,              "Argument %1$s beinhaltet nicht den erforderlichen Wert." }
  , { SystemError.ARGUMENT_SIZE_MISMATCH,          "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl." }

     // OSF-00021 - 00030 instance state related errors
  , { SystemError.ATTRIBUTE_IS_NULL,               "Zustand des Attributes %1$s darf nicht null sein." }
  , { SystemError.INSTANCE_STATE,                  "Ungültiger Instanzzustand: Attributes %1$s ist bereits initialisiert."}

    // OSF-00031 - 00040 system property errors
  , { SystemError.PROPERTY_TYPE_INVALID,           "Systemeigenschaft vom Type [%1$s] kann nicht erstellt werden." }
  , { SystemError.PROPERTY_NAME_REQUIRED,          "Der Name der Eigenschaft wurde nicht festgelegt." }
  , { SystemError.PROPERTY_NAME_EXISTS,            "Es existiert bereits eine Eigenschaft mit dem Namen [%1$s]." }
  , { SystemError.PROPERTY_DEFAULT_REQUIRED,       "Der Standardwert für die Eigenschaften wurde nicht festgelegt." }
  , { SystemError.PROPERTY_MINIMUM_VALUE,          "Der konfigurierte Wert von [%1$s] ist kleiner als der Mindestwert von \"%2$d\" für die Eigenschaft [%3$s] - Standardwert [%4$s] wird stattdessen verwendet." }
  , { SystemError.PROPERTY_MINIMUM_DEFAULT,        "Der Mindestwert kann nicht größer als der Standardwert sein." }
  , { SystemError.PROPERTY_MINIMUM_COMPARABLE,     "Ein Mindestwert kann nur auf Eigenschaften angewendet werden, die Comparable implementieren." }
  , { SystemError.PROPERTY_MAXIMUM_VALUE,          "Der konfigurierte Wert von [%1$s] ist größer als der Maximalwert von \"%2$d\" für die Eigenschaft [%3$s] - Standardwert [%4$s] wird stattdessen verwendet." }
  , { SystemError.PROPERTY_MAXIMUM_DEFAULT,        "Der Maximalwert kann nicht kleiner als der Standardwert sein." }
  , { SystemError.PROPERTY_MAXIMUM_COMPARABLE,     "Ein Maximalwert kann nur auf Eigenschaften angewendet werden, die Comparable implementieren." }

    // OSF-00041 - 00050 reflection/inrospection errors
  , { SystemError.INTROSPECT_EXCEPTION_IGNORE,     "Ausnahme ignoriert!\nStattdessen wird [null] zurückzugeben.\n%1$s" }

     // OSF-00051 - 00060 security context related errors
  , { SystemError.SECURITY_INITIALIZE,             "Error initializing key manager factory (operation failed). Reason: [%1$s]." }
  , { SystemError.SECURITY_UNRECOVERABLE,          "Error initializing key manager factory (unrecoverable key). Reason: [%1$s]." }
  , { SystemError.SECURITY_PROVIDER,               "Error initializing key store (provider not registered)." }
  , { SystemError.SECURITY_ALGORITHM,              "Error initializing key manager factory (algorithm not supported). Reason: [%1$s]." }
  , { SystemError.SECURITY_KEYSTORE_PASSWORD,      "Neither key password nor key store password has been set for [%1$s] key store.\nIgnoring the key store configuration and skipping the key manager factory initialization.\nKey manager factory will not be configured in the current SSL context." }

     // OSF-00061 - 00070 trusted key store related errors
  , { SystemError.TRUSTED_IMPLEMENTATION,          "Fehler beim Initialisieren des Trusted Store (Implementierung [%1$s] nicht verfügbar)." }
  , { SystemError.TRUSTED_PROVIDER,                "Fehler beim Initialisieren des Trusted Store (Anbieter [%1$s] nicht registriert)." }
  , { SystemError.TRUSTED_ALGORITHM,               "Fehler beim Initialisieren des Trusted Store (Algorithmus [%1$s] zur Überprüfung der Integrität des Schlüsselspeichers nicht gefunden)." }
  , { SystemError.TRUSTED_FILE_NOTFOUND,           "Die Datei [%1$s] des Trusted Store kann nicht gefunden werden." }
  , { SystemError.TRUSTED_FILE_NOTLOADED,          "Fehler beim Laden des Trusted Store aus der Datei [%1$s]." }
  , { SystemError.TRUSTED_CERT_NOTLOADED,          "Trusted Store-Zertifikate können nicht geladen werden." }

     // OSF-00071 - 00080 identity key store related errors
  , { SystemError.IDENTITY_IMPLEMENTATION,         "Fehler beim Initialisieren des Identitätsspeichers (Implementierung [%1$s] nicht verfügbar)." }
  , { SystemError.IDENTITY_PROVIDER,               "Fehler beim Initialisieren des Identitätsspeichers (Anbieter [%1$s] nicht registriert)." }
  , { SystemError.IDENTITY_ALGORITHM,              "Fehler beim Initialisieren des Identitätsspeichers (Algorithmus [%1$s] zur Überprüfung der Integrität des Schlüsselspeichers nicht gefunden)." }
  , { SystemError.IDENTITY_FILE_NOTFOUND,          "Die Datei des Identitätsspeicher [%1$s] kann nicht gefunden werden." }
  , { SystemError.IDENTITY_FILE_NOTLOADED,         "Fehler beim Laden des Identitätsspeichers aus der Datei [%1$s]." }
  , { SystemError.IDENTITY_CERT_NOTLOADED,         "Zertifikate des Identitätsspeicher können nicht geladen werden." }

     // OSF-00081 - 00090 configuration related errors
  , { SystemError.PROPERTY_REQUIRED,               "Die Konfigurationseinstellung [%1$s] ist erforderlich." }

     // OSF-00091 - 00090 connectivity errors
  , { SystemError.CONNECTION_UNKNOWN_HOST,         "Host [%1$s] ist nicht bekannt." }
  , { SystemError.CONNECTION_CREATE_SOCKET,        "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { SystemError.CONNECTION_SECURE_SOCKET,        "Die gesicherte Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { SystemError.CONNECTION_CERTIFICATE_PATH,     "Es konnte kein gültiger Pfad zum Zertifikat für das angeforderte Ziel [%1$s] ermittelt werden." }
  , { SystemError.CONNECTION_ERROR,                "Die Verbindung zum Dienstanbieter kann nicht aufgebaut werden." }
  , { SystemError.CONNECTION_TIMEOUT,              "Die Verbindung zum Dienstanbieter wurde wegen Zeitüberschreitung abgebrochen: [%1$s]." }
  , { SystemError.CONNECTION_UNAVAILABLE,          "Das Problem kann an der physischen Verbindung liegen oder der Dienstanbieter ist nicht aktiv." }
  , { SystemError.CONNECTION_AUTHENTICATION,       "Benutzerkonto [%1$s] oder Kennwort ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Zielsystem hergestellt werden." }
  , { SystemError.CONNECTION_AUTHORIZATION,        "Benutzerkonto [%1$s] ist nicht autorisiert." }
  , { SystemError.CONNECTION_ENCODING_UNSUPPORTED, "URL-Codierung [%1$s] wird nicht unterstützt." }

     // OSF-00101 - 00110 path parsing errors
  , { SystemError.PATH_UNEXPECTED_EOF_STRING,      "Unerwartetes Ende der Pfadzeichenfolge." }
  , { SystemError.PATH_UNEXPECTED_EOF_FILTER,      "Unerwartetes Ende der Filterzeichenfolge." }
  , { SystemError.PATH_UNEXPECTED_CHARACTER,       "Unerwartetes Zeichen '%s' an Position %d für Token beginnend bei %d." }
  , { SystemError.PATH_UNRECOGNOIZED_OPERATOR,     "Unbekannter Attributoperator '%s' an Position %d. Erwartet: eq,ne,co,sw,ew,pr,gt,ge,lt,le!" }
  , { SystemError.PATH_INVALID_FILTER,             "Ungültiger Wertefilter: %s." }
  , { SystemError.PATH_EXPECT_PARENTHESIS,         "'(' an Position %d erwartet." }
  , { SystemError.PATH_INVALID_PARENTHESIS,        "Keine öffnende Klammer passend zur schließenden Klammer an Position %d." }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_PATH,      "Pfad zu Attribut an Position %d erwartet." }
  , { SystemError.PATH_EXPECT_ATTRIBUTE_NAME,      "Name eines Attribut an Position %d erwartet." }
  , { SystemError.PATH_INVALID_ATTRIBUTE_PATH,     "Ungültiger Attributpfad an Position %d: %s" }
  , { SystemError.PATH_INVALID_ATTRIBUTE_NAME,     "Ungültiger Attributname ab Position %d: %s" }
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
