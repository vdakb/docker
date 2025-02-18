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

    File        :   FrameworkBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FrameworkBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FrameworkBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~
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
public class FrameworkBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // ICF-00031 - 00040 connectivity related errors
    { FrameworkError.CONNECTION_UNKNOWN_HOST,   "Serveur de connecteurs l'hôte [%1$s] est inconnu." }
  , { FrameworkError.CONNECTION_CREATE_SOCKET,  "Pourrait créer une connexion réseau au serveur de connecteurs sur l'hôte [%1$s] sur le port [%2$s]." }
  , { FrameworkError.CONNECTION_CREATE_SOCKET,  "Pourrait créer une connexion réseau sécurisée au serveur de connecteurs sur l'hôte [%1$s] sur le port [%2$s]." }
  , { FrameworkError.CONNECTION_ERROR,          "Erreur rencontrée lors de la connexion au serveur de connecteurs." }
  , { FrameworkError.CONNECTION_TIMEOUT,        "La connexion au serveur de connecteurs sur l'hôte [%1$s] sur le port [%2$s] a expiré : [%3$s]." }
  , { FrameworkError.CONNECTION_NOT_AVAILABLE,  "Le problème peut provenir de la connectivité physique ou du serveur de connecteurs sur l'hôte [%1$s] sur le port [%2$s] n'est pas actif." }
  , { FrameworkError.CONNECTION_AUTHENTICATION, "Les informations d'identification sont incorrectes, le système n'a pas pu accéder avec les informations d'identification fournies." }

     // ICF-00041 - 00050 connector locator related errors
  , { FrameworkError.CONNECTOR_BUNDLE_NOTFOUND, "Bundle de connecteurs [%1$s] introuvable." }
  , { FrameworkError.CONNECTOR_OPTION_MAPPING,  "L'option de configuration [%1$s] n'est pas transfürable." }
  , { FrameworkError.CONNECTOR_OPTION_REQUIRED, "L'option de configuration [%1$s] est requise." }
  , { FrameworkError.CONNECTOR_OPTION_NOTFOUND, "Valeur introuvable pour l'option de configuration [%1$s]." }

     // ICF-01001 - 01010 script action related messages
  , { FrameworkMessage.SCRIPT_ACTION_EMPTY,     "Aucune action à exécuter dans la phase %1$s.." }
  , { FrameworkMessage.SCRIPT_ACTION_START,     "Exécution de l'action [%2$s] dans la phase %1$s ..." }
  , { FrameworkMessage.SCRIPT_ACTION_SUCCESS,   "Exécution de l'action [%2$s] dans la phase %1$s a réussie." }
  , { FrameworkMessage.SCRIPT_ACTION_FAILED,    "Exécution de l'action [%2$s] dans la phase %1$s a échoué." }
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