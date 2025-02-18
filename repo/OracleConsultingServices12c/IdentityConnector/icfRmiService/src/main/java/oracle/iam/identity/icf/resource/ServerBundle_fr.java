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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Java Enterprise Service Connector Library

    File        :   ServerBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.jes.ServerError;
import oracle.iam.identity.icf.jes.ServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServerBundle_fr
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class ServerBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // JES-00001 - 00010 connection related errors
    { ServerError.CONNECTION_SERVERTYPE_UNSUPPORTED, "Le type de serveur [%1$s] configuré pour la ressource informatique n'est pas pris en charge !" }
  , { ServerError.CONNECTION_LOGINCONFIG_NOTFOUND,   "La configuration du module de connexion n'a pas été trouvée dans le chemin [%1$s] !" }
  , { ServerError.CONTEXT_ACCESS_DENIED,             "Accès refusé pour effectuer une opération dans le contexte d'un utilisateur connecté !" }

    // JES-00011 - 00020 filtering errors
  , { ServerError.FILTER_METHOD_INCONSISTENT,       "La méthode de traduction est incohérente: %s" }
  , { ServerError.FILTER_EXPRESSION_INCONSISTENT,   "L'expression de traduction est incohérente: %s" }
  , { ServerError.FILTER_USAGE_INVALID_GE,          "Le filtre supérieur ou égal peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServerError.FILTER_USAGE_INVALID_GT,          "Le filtre supérieur à peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServerError.FILTER_USAGE_INVALID_LE,          "Le filtre inférieur ou égal peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServerError.FILTER_USAGE_INVALID_LT,          "Le filtre Inférieur à peut ne pas comparer les valeurs d'attributs booléens ou binaires." }

    // JES-00021 - 00040 processing errors
  , { ServerError.PROCESS_EXISTS,                   "%s" }
  , { ServerError.PROCESS_NOT_EXISTS,               "%s" }
  , { ServerError.PROCESS_INVALID_FILTER,           "%s" }

    // JES-00031 - 00040 object errors
  , { ServerError.OBJECT_NOT_EXISTS,                "%1$s avec %2$s [%3$s] n'existe pas chez le fournisseur de services." }
  , { ServerError.OBJECT_AMBIGUOUS,                 "%1$s avec %2$s [%3$s] est défini de manière ambiguë chez le fournisseur de services." }

    // JES-01001 - 01020 system related messages
  , { ServerMessage.CONFIGURING,                     "Configurer l'environnement pour l'URL du service [%1$s] ..." }
  , { ServerMessage.CONFIGURED,                      "Environnement configuré pour l'URL du service [%1$s]." }
  , { ServerMessage.CONNECTING,                      "Connexion à l'URL de service [%1$s] ..." }
  , { ServerMessage.CONNECTED,                       "Connexion établie à l'URL du service [%1$s]." }
  , { ServerMessage.CONNECTION_ALIVE,                "La connexion à l'URL du service [%1$s] est active." }
  , { ServerMessage.LOGGINGIN,                       "Connectez-vous à l'URL du service [%1$s] en tant que [%2$s] ..." }
  , { ServerMessage.LOGGEDIN,                        "[%2$s] était connecté à l'URL du service [%1$s]." }
  , { ServerMessage.LOGGINGOUT,                      "Déconnexion [%2$s] de l'URL du service [%1$s] ..." }
  , { ServerMessage.LOGGEDOUT,                       "[%2$s] s'est déconnecté de l'URL du service [%1$s]." }
  , { ServerMessage.DISCONNECTING,                   "Fermer la connexion à l'URL du service [%1$s]..." }
  , { ServerMessage.DISCONNECTED,                    "Connexion à l'URL du service [%1$s] fermée." }
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