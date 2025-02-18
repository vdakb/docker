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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Bundle_de.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_de.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestMessage;

////////////////////////////////////////////////////////////////////////////////
// class Bundle_de
// ~~~~~ ~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // FIM-00001 - 00010 system related errors
    { RequestError.GENERAL,                        "Allgemeiner Fehler: %1$s" }
  , { RequestError.UNHANDLED,                      "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { RequestError.ABORT,                          "Die Verarbeitung wird abgebrochen: %1$s"}
  , { RequestError.NOTIMPLEMENTED,                 "Funktionalität ist nicht implementiert"}

     // FIM-00011 - 00020 method argument related errors
  , { RequestError.ARGUMENT_IS_NULL,               "Argument %1$s darf nicht null sein" }
  , { RequestError.ARGUMENT_BAD_TYPE,              "Argument %1$s hat nicht den erforderlichen Type" }
  , { RequestError.ARGUMENT_BAD_VALUE,             "Argument %1$s beinhaltet nicht den erforderlichen Wert" }
  , { RequestError.ARGUMENT_SIZE_MISMATCH,         "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl" }

     // FIM-00021 - 00030 instance attribute related errors
  , { RequestError.ATTRIBUTE_IS_NULL,              "Zustand des Attributes %1$s darf nicht null sein" }

     // FIM-00032 - 00040 file related errors
  , { RequestError.FILE_MISSING,                   "Die Datei %1$s konnte nicht gefunden werden" }
  , { RequestError.FILE_IS_NOT_A_FILE,             "%1$s ist keine Datei" }
  , { RequestError.FILE_OPEN,                      "Beim Öffnen der Datei %1$s ist ein Problem aufgetreten" }
  , { RequestError.FILE_CLOSE,                     "Beim Schließen der Datei %1$s ist ein Problem aufgetreten" }
  , { RequestError.FILE_READ,                      "Beim Lesen der Datei %1$s ist ein Problem aufgetreten" }
  , { RequestError.FILE_WRITE,                     "Beim Schreiben der Datei %1$s ist ein Problem aufgetreten" }

     // FIM-00041 - 00050 request related errors
  , { RequestError.REQUEST_CONFIGURATION_PROPERTY, "Anforderungsmodell kann nicht konfiguriert werden; Systemkonfigurationselement %1$s existiert nicht." }
  , { RequestError.REQUEST_CONFIGURATION_STREAM,   "Anforderungsmodell kann auf Grund von Dateifehler %1$s nicht konfiguriert werden." }
  , { RequestError.REQUEST_CONFIGURATION_PARSING,  "Anforderungsmodell kann auf Grund von Parserfehler %1$s nicht konfiguriert werden." }
  , { RequestError.REQUEST_APPLICATION_LOOKUP,     "<html><body>Die Suche nach Applikation <b>%1$s</b> ist fehlgeschlagen.</body></html>" }
  , { RequestError.REQUEST_APPLICATION_NOTFOUND,   "<html><body>Applikation <b>%1$s</b> existiert nicht.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_NOTFOUND,   "<html><body>Berechtigung <b>%1$s</b> existiert nicht.</body></html>" }
  , { RequestError.REQUEST_ENTITLEMENT_AMBIGUOUS,  "<html><body>Berechtigung <b>%1$s</b> mehrfach definiert.</body></html>" }
  , { RequestError.REQUEST_SELECTION_VIOLATED,     "<html><body>Bitte wählen Sie eine Vorlage für das Benutzerkonto in der Umgebung <b>%1$s</b> aus.</body></html>" }
  , { RequestError.REQUEST_PREDECESSOR_VIOLATED,   "<html><body>Bitte fordern Sie ein <b>Fachanwenderkonto</b> in der Umgebung <b>%1$s</b> für %2$s an,<br/>bevor Sie ein Benutzerkonto als <b>%3$s</b> in dieser Umgebung anfordern.</body></html>" }
  , { RequestError.REQUEST_FAILED,                 "<html><body>Fehler bei der Anforderung von <b>%2$s</b> in der Umgebung <b>%1$s</b> für %3$s auf Grund von<br/><br/>%4$s</br></body></html>" }

     // FIM-01041 - 01050 request related messages
  , { RequestMessage.REQUEST_SUBMIT_SUCCESS,       "Anforderung(en) %1$s wurde zur Genehmigung weitergeleitet." }
  , { RequestMessage.REQUEST_COMPLETE_SUCCESS,     "Zugriffsanforderung erfolgreich abgeschlossen." }

  // EVAL-00051 - 00060 evaluation related errors
  , { RequestError.EVALUATION_INTERNAL_ERROR,      "Aufgrund eines internen Fehlers ist die Auswertung der Zugriffsrichtlinie fehlgeschlagen." }
  , { RequestError.EVALUATION_UNKNOWN_ERROR,       "Die Auswertung der Zugriffsrichtlinie hat ein unbekanntes Ergebnis ergeben. Prozess: %1$s" }

  // EVAL-01051 - 01060 evaluation related messages
  , { RequestMessage.EVALUATION_SUCCEEDED,         "Die Auswertung der Zugriffsrichtlinie wurde erfolgreich abgeschlossen." }
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