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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Federated Identity Management
    Subsystem   :   Federal Criminal Police Office Frontend Customizations

    File        :   ConsoleBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConsoleBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-02-23  DSteding    First release version
*/

package bka.iam.identity.resource;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConsoleBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code french
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConsoleBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    /** Constant used to demarcate catalog entity related messages. */
    {Console.USR_ANONYMIZED_LABEL,   "Connexion anonyme"}
  , {Console.USR_ANONYMIZED_HINT,    "Le nom de connexion utilisé par cette identité pour masquer la véritable identité dans les communications sortantes."}
  , {Console.USR_UNIFIED_LABEL,      "Connexion unifiée"}
  , {Console.USR_UNIFIED_HINT,       "Le login utilisé par cette identité pour être authentifié et autorisé par les applications et services P20."}
  , {Console.USR_PARTICIPANT_TITLE,  "Participant"}
  , {Console.USR_PARTICIPANT_LABEL,  "Participant"}
  , {Console.USR_ORGANIZATION_LABEL, "Unité organisationnelle"}
  , {Console.USR_ORGANIZATION_HINT,  "Unité organisationnelle"}
  , {Console.USR_DIVISION_LABEL,     "Division"}
  , {Console.USR_DIVISION_HINT,      "Division"}
  , {Console.USR_DEPARTMENT_LABEL,   "Département"}
  , {Console.USR_DEPARTMENT_HINT,    "Département"}
  , {Console.USR_GENERATED_LABEL,    "Identifiant généré"}
  , {Console.USR_GENERATED_HINT,     "Identifiant généré par la source de données à partir de laquelle l'identité a été obtenue."}
  , {Console.USR_GENERATION_LABEL,   "Nom d'utilisateur principal"}
  , {Console.USR_GENERATION_HINT,    "Ce nom est utilisé ultérieurement pour identifier l'utilisateur à des fins d'authentification.\nUn nom d'utilisateur principal n'est pas la même chose qu'une adresse e-mail.\nParfois, un nom d'utilisateur principal peut correspondre à l'adresse e-mail d'un utilisateur, mais ce n'est pas une règle générale."}
  , {Console.USR_JOBROLE_LABEL,      "Rôle de l'emploi"}
  , {Console.USR_JOBROLE_HINT,       "Le rôle/rang que l'identité a dans la structure organisationnelle."}
  , {Console.USR_QUALIFIED_LABEL,    "Identifiant qualifié"}
  , {Console.USR_QUALIFIED_HINT,     "Identifiant qualifié"}

    /** Constant used to demarcate authorization entity related messages. */
  , {Console.ACC_BADGE_TITLE,        "Autorisation"}
  , {Console.ACC_BADGE_HINT,         "Voir à qui ont été accordés des comptes d'utilisateurs et des droits."}
  , {Console.ACC_APPLICATION_MENU,   "Applications"}
  , {Console.ACC_ENTITLEMENT_MENU,   "Droits"}

    /** Constant used to demarcate catalog entity related messages. */
  , {Console.CAT_BADGE_TITLE,        "Catalogue"}
  , {Console.CAT_BADGE_HINT,         "Gérer les éléments du catalogue"}

    /** Constant used to demarcate eFBS entity related messages. */
  , {Console.FBS_TITLE,              "Request eFBS"}
  , {Console.FBS_ACTION_LABEL,       "Request eFBS"}
  , {Console.FBS_ACTION_HINT,        "Requesting special accounts for the application system eFBS."}

  /** Constant used to demarcate access policy evaluation related messages. */
  , {Console.EVAL_ACTION_LABEL,       "Évaluer" }
  , {Console.EVAL_ACTION_HINT,        "Évaluer immédiatement les politiques d'accés pour l'utilisateur." }
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