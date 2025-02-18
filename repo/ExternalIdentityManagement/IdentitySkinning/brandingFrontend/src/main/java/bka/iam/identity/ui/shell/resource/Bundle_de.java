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
    Subsystem   :   Branding Customization

    File        :   Bundle_de.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Bundle_de.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet     First release version
*/

package bka.iam.identity.ui.shell.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

import bka.iam.identity.ui.BrandingError;

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
     // BB-00001 - 00010 system related errors
    { BrandingError.GENERAL,                         "Allgemeiner Fehler: %1$s" }
  , { BrandingError.UNHANDLED,                       "Eine unbehandelte Ausnahme is aufgetreten: %1$s"}
  , { BrandingError.ABORT,                           "Die Verarbeitung wird abgebrochen: %1$s"}
  , { BrandingError.NOTIMPLEMENTED,                  "Funktionalität ist nicht implementiert"}

     // BB-00011 - 00020 method argument related errors
  , { BrandingError.ARGUMENT_IS_NULL,                "Argument %1$s darf nicht null sein" }
  , { BrandingError.ARGUMENT_BAD_TYPE,               "Argument %1$s hat nicht den erforderlichen Type" }
  , { BrandingError.ARGUMENT_BAD_VALUE,              "Argument %1$s beinhaltet nicht den erforderlichen Wert" }
  , { BrandingError.ARGUMENT_SIZE_MISMATCH,          "Die Anzahl der übergebenen Argumente entspricht nicht der erwarteten Anzahl" }

     // BB-00021 - 00030 instance attribute related errors
  , { BrandingError.ATTRIBUTE_IS_NULL,               "Zustand des Attributes %1$s darf nicht null sein" }

     // BB-00032 - 00040 file related errors
  , { BrandingError.FILE_MISSING,                    "Die Datei %1$s konnte nicht gefunden werden" }
  , { BrandingError.FILE_IS_NOT_A_FILE,              "%1$s ist keine Datei" }
  , { BrandingError.FILE_OPEN,                       "Beim Öffnen der Datei %1$s ist ein Problem aufgetreten" }
  , { BrandingError.FILE_CLOSE,                      "Beim Schließen der Datei %1$s ist ein Problem aufgetreten" }
  , { BrandingError.FILE_READ,                       "Beim Lesen der Datei %1$s ist ein Problem aufgetreten" }
  , { BrandingError.FILE_WRITE,                      "Beim Schreiben der Datei %1$s ist ein Problem aufgetreten" }

    // BB-00041 - 00050 file related errors
  , { BrandingError.BRANDING_CONFIGURATION_PROPERTY, "Anforderungsmodell kann nicht konfiguriert werden, da die Systemeigenschaft %1$s fehlt." }
  , { BrandingError.BRANDING_CONFIGURATION_STREAM,   "Anforderungsmodell kann aufgrund eines Stream-Fehlers nicht konfiguriert werden %1$s." }
  , { BrandingError.BRANDING_CONFIGURATION_PARSING,  "Anforderungsmodell kann aufgrund eines Parserfehlers nicht konfiguriert werden %1$s." }
  , { BrandingError.BRANDING_NOT_DEFINED,            "Die Komponente %l$s ist nicht definiert."}
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