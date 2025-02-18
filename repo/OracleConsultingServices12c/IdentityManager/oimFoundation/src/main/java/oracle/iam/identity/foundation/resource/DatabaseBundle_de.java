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

    File        :   DatabaseBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.persistence.DatabaseError;
import oracle.iam.identity.foundation.persistence.DatabaseMessage;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~
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
public class DatabaseBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00001 - 00010 operations related errors
    { DatabaseError.UNHANDLED,                   "Eine unbehandelte Ausnahme ist aufgetreten: %1$s"}
  , { DatabaseError.GENERAL,                     "Allgemeiner Fehler: %1$s" }
  , { DatabaseError.ABORT,                       "Die Verarbeitung wird abgebrochen: %1$s"}

     // DBS-00011 - 00015 method argument related errors
  , { DatabaseError.ARGUMENT_IS_NULL,            "Argument [%1$s] darf nicht null sein" }
  , { DatabaseError.ARGUMENT_BAD_TYPE,           "Argument [%1$s] hat nicht den erforderlichen Type" }
  , { DatabaseError.ARGUMENT_BAD_VALUE,          "Argument [%1$s] beinhaltet nicht den erforderlichen Wert" }
  , { DatabaseError.ARGUMENT_SIZE_MISMATCH,      "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl" }

     // DBS-00021 - 00030 instance state related errors
  , { DatabaseError.INSTANCE_ATTRIBUTE_IS_NULL,  "Ungültiger Instanzzustand: Attribute [%1$s] ist nicht initialisiert" }
  , { DatabaseError.INSTANCE_ILLEGAL_STATE,      "Ungültiger Instanzzustand: Attribute [%1$s] ist bereits initialisiert" }

     // DBS-00061 - 00070 connectivity errors
  , { DatabaseError.CONNECTION_UNKNOWN_HOST,     "Host [%1$s] ist nicht bekannt" }
  , { DatabaseError.CONNECTION_CREATE_SOCKET,    "Die Netzwerkverbindung zu Host [%1$s] über Port [%2$s] kann nicht hergestellt werden" }
  , { DatabaseError.CONNECTION_ERROR,            "Die Verbindung zum Zielsystem kann nicht aufgebaut werden" }
  , { DatabaseError.CONNECTION_TIMEOUT,          "Die Verbindung zum Zielsystem konnte wegen Zeitüberschreitung nicht aufgebaut werden" }
  , { DatabaseError.CONNECTION_NOT_AVAILABLE,    "The problem may be with physical connectivity or Target System is not alive" }
  , { DatabaseError.CONNECTION_SSL_HANDSHAKE,    "Das SSL Zertifikat ist möglicherwiese nicht odnungsgemäß für das Zielsystem generiert worden" }
  , { DatabaseError.CONNECTION_SSL_ERROR,        "Not able to invalidate SSL session." }
  , { DatabaseError.CONNECTION_SSL_DESELECTED,   "SSL option is not selected in IT Resource." }
  , { DatabaseError.CONNECTION_AUTHENTICATION,   "Benutzerkonto [%1$s] oder Kennwort ist inkorrekt, mit den angegebenen Information konnte keine Verbindung mit dem Zielsystem hergestellt werden" }
  , { DatabaseError.CONNECTION_PERMISSION,       "Benutzerkonto [%1$s] hat keine ausreichenden Berechtigungen für [%2$s]" }

     // DBS-00071 - 00080 access operational errors
  , { DatabaseError.SYNTAX_INVALID,              "Syntax-Fehler im SQL-Statement: [%1$s]" }
  , { DatabaseError.OPERATION_FAILED,            "Operation wurde durch die Datenbank nicht ausgeführt." }
  , { DatabaseError.OPERATION_NOT_SUPPORTED,     "Operation wird durch die Datenbank nicht unterstützt." }
  , { DatabaseError.INSUFFICIENT_PRIVILEGE,      "Benutzerkonto [%1$s] hat keine ausreichenden Berechtigungen zu Ausführung von [%2$s]" }
  , { DatabaseError.INSUFFICIENT_INFORMATION,    "Erforderliche Attributinformation für [%1$s] sind nicht vorhanden." }
  , { DatabaseError.SEARCH_CONDITION_FAILED,     "Bilden der Suchbedingung [%1$s] ist fehlgeschlagen." }

     // DBS-00091 - 00130 object operational errors
  , { DatabaseError.OBJECT_NOT_CREATED,          "Eintrag [%1$s] in [%2$s] kann nicht erzeugt werden " }
  , { DatabaseError.OBJECT_NOT_MODIFIED,         "Eintrag [%1$s] konnte in [%2$s] nicht geändert werden" }
  , { DatabaseError.OBJECT_NOT_DELETED,          "Eintrag [%1$s] konnte in [%2$s] nicht gelöscht werden" }
  , { DatabaseError.OBJECT_ALREADY_EXISTS,       "Eintrag [%1$s] existiert bereits in [%2$s]" }
  , { DatabaseError.OBJECT_NOT_EXISTS,           "Eintrag [%1$s] existiert nicht in [%2$s]" }
  , { DatabaseError.OBJECT_AMBIGUOUS,            "Eintrag ist mehrfach definiert für [%1$s]" }
  , { DatabaseError.PARENT_NOT_EXISTS,           "übergeordneter Eintrag [%1$s] existiert nicht in [%2$s]" }
  , { DatabaseError.PARENT_AMBIGUOUS,            "übergeordneter Eintrag ist mehrfach definiert für [%1$s]" }
  , { DatabaseError.PERMISSION_NOT_ASSIGNED,     "Berechtigung [%1$s] wurde [%2$s] nicht zugewiesen" }
  , { DatabaseError.PERMISSION_NOT_REMOVED,      "Berechtigung [%1$s] wurde [%2$s] nicht entzogen" }
  , { DatabaseError.PERMISSION_ALREADY_ASSIGNED, "Berechtigung [%1$s] wurde [%2$s] bereits zugewiesen" }
  , { DatabaseError.PERMISSION_ALREADY_REMOVED,  "Berechtigung [%1$s] wurde [%2$s] bereits entzogen" }

     // DBS-01001 - 01005 system related messages
  , { DatabaseMessage.CONNECTING_TO,             "Verbindungsaufbau zu [%1$s]" }

     // DBS-01011 - 01020 system related messages
  , { DatabaseMessage.EXECUTE_STATEMENT,         "Ausführen von: %1$s" }
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