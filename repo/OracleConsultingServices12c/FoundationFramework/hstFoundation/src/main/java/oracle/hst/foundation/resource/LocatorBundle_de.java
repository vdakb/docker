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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared naming facilities

    File        :   LocatorBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    LocatorBundle_de.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.resource;

import oracle.hst.foundation.naming.LocatorError;

////////////////////////////////////////////////////////////////////////////////
// class LocatorBundle_de
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
 */
public class LocatorBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // General error (Undefined)
    { LocatorError.GENERAL,                "Allgemeiner Fehler: %1$s" }
  , { LocatorError.UNHANDLED,              "Eine unbehandelte Ausnahme is aufgetreten: %1$s" }
  , { LocatorError.ABORT,                  "Die Verarbeitung wird abgebrochen: %1$s" }
  , { LocatorError.NOTIMPLEMENTED,         "Funktionalität ist nicht implementiert!" }
  , { LocatorError.CLASSNOTFOUND,          "Klasse %1$s konnte im Klassenpfad nicht gefunden werden!" }
  , { LocatorError.CLASSNOTCREATE,         "Klasse %1$s konnte nicht erzeugt werden!" }
  , { LocatorError.CLASSINVALID,           "Klasse %1$s muss eine Unterklasse von %2$s sein!" }
  , { LocatorError.CLASSCONSTRUCTOR,       "Klasse %1$s hat keinen Konstruktur für die Parameter %2$s!" }

    // Argument errors
  , { LocatorError.ARGUMENT_IS_NULL,       "Argument %1$s darf nicht null sein!" }
  , { LocatorError.ARGUMENT_BAD_TYPE,      "Argument %1$s hat nicht den erforderlichen Type!" }
  , { LocatorError.ARGUMENT_BAD_VALUE,     "Argument %1$s beinhaltet nicht den erforderlichen Wert!" }
  , { LocatorError.ARGUMENT_SIZE_MISMATCH, "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl!" }

    // JNDI naming and lookup related errors
  , { LocatorError.LOCATOR_INITIALIZE,     "Service Locator konnte nicht initialisiert werden!" }
  , { LocatorError.CONTEXT_CONNECTION,     "Verbindung zum JNDI-Kontext %1$s konnte nicht hergestellt werden!" }
  , { LocatorError.CONTEXT_INITIALIZE,     "JNDI-Kontext konnte nicht initialisiert werden!" }
  , { LocatorError.CONTEXT_CLOSE,          "JNDI-Kontext konnte nicht geschlossen werden!" }
  , { LocatorError.CONTEXT_ENVIRONMENT,    "Could not obtain environment from context!" }
  , { LocatorError.OBJECT_LOOKUP,          "Lookup failed in JNDI-Kontext for object %1$s!" }
  , { LocatorError.OBJECT_CREATION,        "Objekt %1$s konnte nicht erzeugt werden!" }
  , { LocatorError.OBJECT_ACCESS,          "Zugriff auf Objekt %1$s ist fehlgeschlagen!" }
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