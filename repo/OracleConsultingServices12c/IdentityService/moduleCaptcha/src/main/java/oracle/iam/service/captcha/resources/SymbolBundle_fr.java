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

    File        :   SymbolBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SymbolBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.resources;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class SymbolBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Java Resource Bundle for national language support.
 ** <p>
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  french
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SymbolBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {"page.meta.title",               "Service Captcha - Institut de technologie de Nuremberg Georg Simon Ohm" }
  , {"page.meta.name",                "TH Nuremberg" }

  , {"init.image.config",             "Impossible d'initialiser le service. Impossible de trouver la configuration dans %1$s." }
  , {"init.image.failed",             "Impossible d'initialiser le service. ?chec du chargement des ressources d'image." }

  , {"request.action.invalid",        "Action %1$s non valide dans la demande" }

  , {"start.option.invalid",          "Valeur non valide pour le nombre d'options ? afficher?: %1$d. La valeur par défaut %2$s est utilis?e." }

  , {"stream.index.invalid",          "Index d'image non valide sp?cifi? dans la demande?: %1$d" }
  , {"stream.request.invalid",        "Index %1$d non valide pour l'image demandée." }
  , {"stream.source.invalid",         "Impossible de trouver l'image %1$s." }

  , {"captcha.challenge.text",        "Cliquez ou appuyez sur le symbole %1$s." }
  , {"captcha.challenge.success",     "L'image ?tait valide." }
  , {"captcha.challenge.failed",      "L'image n'?tait PAS valide." }

  , {"captcha.image.airplane",        "Avion" }
  , {"captcha.image.balloons",        "Des Ballons" }
  , {"captcha.image.camera",          "Cam?ra" }
  , {"captcha.image.car",             "Auto" }
  , {"captcha.image.cat",             "Chatte" }
  , {"captcha.image.chair",           "Chaise" }
  , {"captcha.image.clip",            "Trombone" }
  , {"captcha.image.clock",           "Horloge" }
  , {"captcha.image.cloud",           "Nuage" }
  , {"captcha.image.computer",        "Ordinateur" }
  , {"captcha.image.envelope",        "Enveloppe" }
  , {"captcha.image.eye",             "?il" }
  , {"captcha.image.flag",            "Drapeau" }
  , {"captcha.image.folder",          "Dossier" }
  , {"captcha.image.foot",            "Le pied" }
  , {"captcha.image.graph",           "Diagramme " }
  , {"captcha.image.house",           "La Maison" }
  , {"captcha.image.key",             "Cl?" }
  , {"captcha.image.leaf",            "Feuillet" }
  , {"captcha.image.lightbulb",       "Ampoule" }
  , {"captcha.image.lock",            "Serrure" }
  , {"captcha.image.magnifyingglass", "Loupe" }
  , {"captcha.image.man",             "Homme" }
  , {"captcha.image.musicnote",       "Note de musique" }
  , {"captcha.image.pants",           "Pantalon" }
  , {"captcha.image.pencil",          "Crayon" }
  , {"captcha.image.printer",         "Imprimant" }
  , {"captcha.image.robot",           "Robot" }
  , {"captcha.image.scissors",        "Les ciseaux" }
  , {"captcha.image.sunglasses",      "Lunettes de soleil" }
  , {"captcha.image.tag",             "?tiqueter" }
  , {"captcha.image.tree",            "Arbre" }
  , {"captcha.image.truck",           "Camion" }
  , {"captcha.image.tshirt",          "T-shirt" }
  , {"captcha.image.umbrella",        "Parapluie" }
  , {"captcha.image.woman",           "Femme" }
  , {"captcha.image.world",           "Monde" }
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