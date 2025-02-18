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
    Subsystem   :   Connector Bundle Integration

    File        :   FrameworkBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FrameworkBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FrameworkBundle_de
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
public class FrameworkBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // ICF-00031 - 00040 connectivity related errors
    { FrameworkError.CONNECTION_UNKNOWN_HOST,        "Connector Server Host [%1$s] ist nicht bekannt." }
  , { FrameworkError.CONNECTION_CREATE_SOCKET,       "Die Netzwerkverbindung zu Connector Server Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { FrameworkError.CONNECTION_SECURE_SOCKET,       "Die gesicherte Netzwerkverbindung zu Connector Server Host [%1$s] über Port [%2$s] kann nicht hergestellt werden." }
  , { FrameworkError.CONNECTION_ERROR,               "Die Verbindung zum Connector Server kann nicht aufgebaut werden." }
  , { FrameworkError.CONNECTION_TIMEOUT,             "Die Verbindung zum Connector Server Host [%1$s] über Port [%2$s] wurde wegen Zeitüberschreitung abgebrochen: [%3$s]." }
  , { FrameworkError.CONNECTION_NOT_AVAILABLE,       "The problem may be with physical connectivity or Connector Server at host [%1$s] on port [%2$s] is not alive." }
  , { FrameworkError.CONNECTION_AUTHENTICATION,      "Schlüssel ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Connector Server hergestellt werden." }

     // ICF-00041 - 00050 connector locator related errors
  , { FrameworkError.CONNECTOR_BUNDLE_NOTFOUND,      "Konnektor [%1$s] wurde nicht gefunden." }
  , { FrameworkError.CONNECTOR_OPTION_MAPPING,       "Konfigrationsoption [%1$s] kann nicht transferiert werden." }
  , { FrameworkError.CONNECTOR_OPTION_REQUIRED,      "Wert für Konfigrationsoption [%1$s] ist erforderlich." }
  , { FrameworkError.CONNECTOR_OPTION_NOTFOUND,      "Wert für Konfigrationsoption [%1$s] nicht vorhanden." }

     // ICF-00051 - 00060 reconciliation thread pool related errors
  , { FrameworkError.RECONCILIATION_POOL_SIZE,       "Größe eines Pools muss >= 1 sein." }
  , { FrameworkError.RECONCILIATION_POOL_CLOSED,     "Verarbeitung ist bereits beendet, weitere Datenabgleichsereignis können nicht hinzugefügt werden." }
  , { FrameworkError.RECONCILIATION_POOL_FINISHED,   "Verarbeitung ist bereits beendet, Vorgang kann nicht wiederholt werden." }

     // ICF-01001 - 01010 script action related messages
  , { FrameworkMessage.SCRIPT_ACTION_EMPTY,          "Keine Aktion zur Ausführung für Phase %1$s." }
  , { FrameworkMessage.SCRIPT_ACTION_START,          "Ausführung von Aktion [%2$s] in Phase %1$s ..." }
  , { FrameworkMessage.SCRIPT_ACTION_SUCCESS,        "Aktion [%2$s] in Phase %1$s erfolgreich ausgeführt." }
  , { FrameworkMessage.SCRIPT_ACTION_FAILED,         "Ausführung von Aktion [%2$s] in Phase %1$s fehlgeschlagen." }

     // ICF-01011 - 01020 reconciliation thread pool related messages
  , { FrameworkMessage.RECONCILIATION_POOL_ADD,      "Datenabgleichsereignis zu Pool im slot [%d] hinzugefügt." }
  , { FrameworkMessage.RECONCILIATION_POOL_ADDED,    "Datenabgleichsereignis %s zu Pool hinzugefügt." }
  , { FrameworkMessage.RECONCILIATION_POOL_LIMITS,   "Pool hat Füllstandsgrenze erreicht, Größe ist %d. Starte Bearbeitung ..." }
  , { FrameworkMessage.RECONCILIATION_POOL_COMPLETE, "Pool Bearbeitung abgeschlossen, submitting it to reconciliation engine, Größe ist [%d]." }

     // ICF-01021 - 01030 reconciliation event related messages
  , { FrameworkMessage.RECONCILIATION_EVENT_REGULAR, "Verarbeite formales Datenabgleichsereignis für Object mit UID [%1$s] für Ressource Objekt [%2$s]." }
  , { FrameworkMessage.RECONCILIATION_EVENT_CREATED, "[%d] Datenabgleichsereignisse wurden erfogreich erzeugt." }
  , { FrameworkMessage.RECONCILIATION_EVENT_FAILED,  "[%d] Datenabgleichsereignisse sind fehlgeschlagen." }
  , { FrameworkMessage.RECONCILIATION_EVENT_DELETE,  "Verarbeite löschendes Datenabgleichsereignisse [%d] für Ressource Objekt [%2$s]." }
  , { FrameworkMessage.RECONCILIATION_EVENT_NOTHING, "Keine Datenabgleichsereignisse für Löschung ermittelt." }
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