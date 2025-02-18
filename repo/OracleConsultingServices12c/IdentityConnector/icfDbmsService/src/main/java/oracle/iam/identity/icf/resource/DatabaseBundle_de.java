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

    File        :   DatabaseBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.dbms.DatabaseMessage;

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
 ** @since   1.0.0.0
 */
public class DatabaseBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // DBS-00051 - 00060 schema discovery errors
    { DatabaseError.SCHEMA_DESCRIPTOR_ERROR,     "Die Operation der Schemabeschreibung ist fehlgeschlagen: [%1$s]." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_EMPTY,     "Die Schemabeschreibung ist leer." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_NOTFOUND,  "Die Schemabeschreibung konnte nicht gefunden werden." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_INVALID,   "Die Schemabeschreibung ist ungültig." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PARSE,     "Beim Parsen der Schemabeschreibung ist ein Fehler aufgetreten: [%1$s]" }
  , { DatabaseError.SCHEMA_DESCRIPTOR_PRIMARY,   "System Identfier für Entity [%1$s] nicht definiert." }
  , { DatabaseError.SCHEMA_DESCRIPTOR_SECONDARY, "Eindeutiger Identfier für Entity \\\"%1$s\\\" nicht definiert\"." }

     // DBS-00061 - 00070 connectivity and statment errors
  , { DatabaseError.CONNECTION_NOT_CONNECTED,    "Not connected." }
  , { DatabaseError.CONNECTION_CLOSED,           "Connection closed." }
  , { DatabaseError.STATEMENT_CLOSED,            "Statement closed." }
  , { DatabaseError.STATEMENT_TIMEOUT,           "Statement timedout." }

     // DBS-00071 - 00080 access operational errors
  , { DatabaseError.SYNTAX_INVALID,              "Syntax-Fehler im SQL-Statement: [%1$s]." }
  , { DatabaseError.OPERATION_FAILED,            "Operation wurde durch die Datenbank nicht ausgeführt." }
  , { DatabaseError.OPERATION_NOT_SUPPORTED,     "Operation wird durch die Datenbank nicht unterstützt." }
  , { DatabaseError.INSUFFICIENT_PRIVILEGE,      "Benutzerkonto [%1$s] hat keine ausreichenden Berechtigungen zu Ausführung von [%2$s]." }
  , { DatabaseError.INSUFFICIENT_INFORMATION,    "Erforderliche Attributinformation für [%1$s] sind nicht vorhanden." }
  , { DatabaseError.SEARCH_CONDITION_FAILED,     "Bilden der Suchbedingung [%1$s] ist fehlgeschlagen." }

     // DBA-00081 - 00090 regular expresssion errors
  , { DatabaseError.EXPRESSION_BITVALUES,        "Undefined bit values are set in the regular expression compile options." }
  , { DatabaseError.EXPRESSION_INVALID,          "An error is contained in the regular expression [%1$s]: [%2$s]." }

     // DBS-00101 - 00120 object operational errors
  , { DatabaseError.OBJECT_NOT_CREATED,          "Eintrag für [%1$s] in [%2$s] wurde nicht erstellt." }
  , { DatabaseError.OBJECT_NOT_MODIFIED,         "Eintrag für [%1$s] in [%2$s] wurde nicht geändert." }
  , { DatabaseError.OBJECT_NOT_DELETED,          "Eintrag für [%1$s] in [%2$s] wurde nicht gelöscht." }
  , { DatabaseError.OBJECT_ALREADY_EXISTS,       "Eintrag [%1$s] existiert bereits in [%2$s]." }
  , { DatabaseError.OBJECT_NOT_EXISTS,           "Eintrag [%1$s] existiert nicht in [%2$s]." }
  , { DatabaseError.OBJECT_AMBIGUOUS,            "Eintrag [%1$s] ist mehrfach definiert für [%2$s]." }
  , { DatabaseError.PARENT_NOT_EXISTS,           "Übergeordneter Eintrag [%1$s] existiert nicht in [%2$s]." }
  , { DatabaseError.PARENT_AMBIGUOUS,            "Übergeordneter Eintrag [%1$s] ist mehrfach definiert für [%2$s]." }
  , { DatabaseError.PERMISSION_NOT_ASSIGNED,     "Berechtigung [%1$s] wurde [%2$s] nicht zugewiesen." }
  , { DatabaseError.PERMISSION_NOT_REMOVED,      "Berechtigung [%1$s] wurde [%2$s] nicht entzogen." }
  , { DatabaseError.PERMISSION_ALREADY_ASSIGNED, "Berechtigung wurde [%1$s] in [%2$s] bereits zugewiesen." }
  , { DatabaseError.PERMISSION_ALREADY_REMOVED,  "Berechtigung wurde [%1$s] in [%2$s] bereits entzogen." }

     // DBS-00091 - 00100 path expresssion errors
  , { DatabaseError.PATH_UNEXPECTED_EOS,         "Unexpected end of path string." }
  , { DatabaseError.PATH_UNEXPECTED_CHARACTER,   "Unerwartetes Zeichen [%1$s] an Position \"%2%d\" für Token beginnend bei \"%3%d\"." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_PATH,  "Attributpfad an Position \"%1%d\" erwartet." }
  , { DatabaseError.PATH_EXPECT_ATTRIBUTE_NAME,  "Attributname an Position \"%1%d\" erwartet." }

     // DBS-01001 - 01005 system related messages
  , { DatabaseMessage.CONNECTING_BEGIN,          "Verbindungsaufbau zu Service URL [%1$s] für Benutzer [%2$s] ..." }
  , { DatabaseMessage.CONNECTING_SUCCESS,        "Verbindung zu Service URL [%1$s] für Benutzer [%2$s] aufgebaut." }

     // DBS-01011 - 01020 system related messages
  , { DatabaseMessage.CONNECTION_ALIVE,          "Verbindung verfügbar" }

     // DBS-01021 - 01030 system related messages
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