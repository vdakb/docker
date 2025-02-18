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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   SymbolBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SymbolBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.resources;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SymbolBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Java Resource Bundle for national language support.
 ** <p>
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>country  code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SymbolBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {"page.meta.title",               "Captcha Service - Technische Hochschule Nürnberg Georg Simon Ohm" }
  , {"page.meta.name",                "TH Nürnberg" }

  , {"init.image.config",             "Dienst kann nicht initialisiert werden. Konfiguration in %1$s konnte nicht gefunden werden." }
  , {"init.image.failed",             "Dienst kann nicht initialisiert werden. Fehler beim Laden der Bildressourcen." }

  , {"request.action.invalid",        "Ungültige Aktion %1$s in der Anfrage." }

  , {"start.option.invalid",          "Ungültiger Wert für die Anzahl der anzuzeigenden Optionen: %1$d. Der Standardwert %2$s wird verwendet." }

  , {"stream.index.invalid",          "Ungültiger Index für Bild in Anfrage angegeben: %1$d." }
  , {"stream.request.invalid",        "Index %1$d ungültig für angefordertes Bild." }
  , {"stream.source.invalid",         "Bild %1$s ist nicht vorhanden." }

  , {"captcha.challenge.text",        "Klicken oder tippen Sie auf das Symbol %1$s." }
  , {"captcha.challenge.success",     "Bild war gültig." }
  , {"captcha.challenge.failed",      "Bild war NICHT gültig." }

  , {"captcha.image.airplane",        "Flugzeug" }
  , {"captcha.image.balloons",        "Ballons" }
  , {"captcha.image.camera",          "Kamera" }
  , {"captcha.image.car",             "Auto" }
  , {"captcha.image.cat",             "Katze" }
  , {"captcha.image.chair",           "Stuhl" }
  , {"captcha.image.clip",            "Büroklammer" }
  , {"captcha.image.clock",           "Wecker" }
  , {"captcha.image.cloud",           "Wolke" }
  , {"captcha.image.computer",        "Rechner" }
  , {"captcha.image.envelope",        "Briefumschlag" }
  , {"captcha.image.eye",             "Auge" }
  , {"captcha.image.flag",            "Flagge" }
  , {"captcha.image.folder",          "Ordner" }
  , {"captcha.image.foot",            "Fuß" }
  , {"captcha.image.graph",           "Diagramm" }
  , {"captcha.image.house",           "Haus" }
  , {"captcha.image.key",             "Schlüssel" }
  , {"captcha.image.leaf",            "Blatt" }
  , {"captcha.image.lightbulb",       "Glühbirne" }
  , {"captcha.image.lock",            "Schloss" }
  , {"captcha.image.magnifyingglass", "Lupe" }
  , {"captcha.image.man",             "Mann" }
  , {"captcha.image.musicnote",       "Musiknote" }
  , {"captcha.image.pants",           "Hose" }
  , {"captcha.image.pencil",          "Bleistift" }
  , {"captcha.image.printer",         "Drucker" }
  , {"captcha.image.robot",           "Roboter" }
  , {"captcha.image.scissors",        "Schere" }
  , {"captcha.image.sunglasses",      "Sonnenbrille" }
  , {"captcha.image.tag",             "Etikett" }
  , {"captcha.image.tree",            "Baum" }
  , {"captcha.image.truck",           "Lastwagen" }
  , {"captcha.image.tshirt",          "T-Shirt" }
  , {"captcha.image.umbrella",        "Regenschirm" }
  , {"captcha.image.woman",           "Frau" }
  , {"captcha.image.world",           "Welt" }
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