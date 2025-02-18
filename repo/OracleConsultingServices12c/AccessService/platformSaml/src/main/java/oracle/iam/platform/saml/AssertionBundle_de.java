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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AssertionBundle_de.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AssertionBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.saml;

import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AssertionBundle_de
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
public class AssertionBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OAM-00001 - 00010 General error (Undefined)
    { AssertionError.UNHANDLED,                       "Eine unbehandelte Ausnahme is aufgetreten: [%1$s]" }
  , { AssertionError.GENERAL,                         "Allgemeiner Fehler: %1$s" }
  , { AssertionError.ABORT,                           "Die Verarbeitung wird abgebrochen: [%1$s]" }
  , { AssertionError.NOTIMPLEMENTED,                  "Funktionalität ist nicht implementiert!" }

     // OAM-00011 - 00020 XML parser related errors
  , { AssertionError.PARSER_FATAL,                    "Schwerwiegnd: [%1$s]" }
  , { AssertionError.PARSER_ERROR,                    "Fehler: [%1$s]" }
  , { AssertionError.PARSER_WARNING,                  "Warnung: [%1$s]" }
  , { AssertionError.PARSER_FEATURE,                  "Funktion [%1$s] wird durch XML-Parser nicht untertstützt!" }

     // OAM-00021 - 00030 signature related errors
  , { AssertionError.SIGNATURE_MISSING,               "Keine XML-Signatur gefunden, Validierung verworfen!" }
  , { AssertionError.SIGNATURE_CORE_FAILED,           "Grundlegende Überprüfung der Signatur fehlgeschlagen!" }

     // OAM-00041 - 00050 token validation related errors
  , { AssertionError.TOKEN_ISSUED,                    "Token wurde für die Zukunft [%1$s] ausgegeben!" }
  , { AssertionError.TOKEN_NOTAFTER,                  "Token ist abgelaufen [%1$s]!" }
  , { AssertionError.TOKEN_NOTBEFORE,                 "Token wird vor seiner erlaubten Verwendung [%1$s] verwendet!" }

     // OAM-01001 - 01010 XML parser related messages
  , { AssertionMessage.PARSER_ENTITY_RESOLVED,        "Auflösung der Entität Abfrage: System=[%1$s] Öffentlich=[%2$s] Rückgabe=[%3$s]." }

     // OAM-01011 - 01020 signature related messages
  , { AssertionMessage.SIGNATURE_CORE_PASSED,         "Signatur hat grundlegende Überprüfung bestanden." }
  , { AssertionMessage.SIGNATURE_CORE_STATUS,         "Status der Signaturüberprüfung: %1$s" }
  , { AssertionMessage.SIGNATURE_STATUS_VALID,        "gültig" }
  , { AssertionMessage.SIGNATURE_STATUS_INVALID,      "ungültig" }
  , { AssertionMessage.SIGNATURE_STATUS_VALIDATED,    "bestätigt" }
  , { AssertionMessage.SIGNATURE_STATUS_NOTVALIDATED, "nicht bestätigt" }
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