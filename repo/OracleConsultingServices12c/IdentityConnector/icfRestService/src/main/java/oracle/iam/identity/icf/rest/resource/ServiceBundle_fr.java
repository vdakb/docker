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
    Subsystem   :   Generic REST Library

    File        :   ServiceBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle_fr
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
public class ServiceBundle_fr extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GWS-00031 - 00040 authorization errors
    { ServiceError.OAUTH_FLOW_NOT_FINISH,          "L'autorisation n'est pas terminée et le jeton d'accès n'a pas été reçu. Appelez start() puis finish() pour effectuer l'autorisation." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,         "Paramètre \"état\" non valide. « état » utilisé dans la demande d'autorisation ne correspond pas à « l'état » de la réponse d'autorisation." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,        "Erreur lors de la demande du jeton d'accès. État de la réponse: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,       "Erreur lors de l'actualisation d'un jeton d'accès. État de la réponse: %1$s." }

     // GWS-00061 - 00080 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,     "Chaîne de fin de chemin inattendue." }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,     "Fin inattendue de la chaîne de filtrage." }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,      "Caractère inattendu '%s' à la position %d pour le jeton commençant à %d." }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,          "Jeton inattendu '%s'." }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,    "Opérateur d'attribut non reconnu '%s' à la position %d. Attendu : eq,ne,co,sw,ew,pr,gt,ge,lt,le." }
  , { ServiceError.PATH_INVALID_FILTER,            "Filtre de valeur non valide: %s." }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,        "'(' attendu à la position %d." }
  , { ServiceError.PATH_INVALID_PARENTHESIS,       "Aucune parenthèse ouvrante ne correspond à la parenthèse fermante à la position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,     "Chemin d'attribut attendu à la position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,     "Nom d'attribut attendu à la position %d." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,    "Chemin d'attribut non valide à la position %d: %s." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,    "Nom d'attribut non valide commençant à la position %d: %s.\"" }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,  "Valeur de comparaison non valide à la position %d: %s." }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,       "Le chemin ne peut pas cibler des sous-attributs de plus d'un niveau." }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,      "Le chemin ne peut pas inclure de filtre de valeur sur les sous-attributs." }

     // GWS-00081 - 00090 request parameter errors
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,   "'%s' n'est pas une valeur valide pour le paramètre sortBy: %s." }

    // GWS-00091 - 00100 filtering errors
  , { ServiceError.FILTER_METHOD_INCONSISTENT,     "La méthode de traduction est incohérente: %s" }
  , { ServiceError.FILTER_EXPRESSION_INCONSISTENT, "L'expression de traduction est incohérente: %s" }
  , { ServiceError.FILTER_USAGE_INVALID_GE,        "Le filtre supérieur ou égal peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServiceError.FILTER_USAGE_INVALID_GT,        "Le filtre supérieur à peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServiceError.FILTER_USAGE_INVALID_LE,        "Le filtre inférieur ou égal peut ne pas comparer les valeurs d'attributs booléens ou binaires." }
  , { ServiceError.FILTER_USAGE_INVALID_LT,        "Le filtre Inférieur à peut ne pas comparer les valeurs d'attributs booléens ou binaires." }

     // GWS-00101 - 00120 processing errors
  , { ServiceError.PROCESS_UNEXPECTED,             "Le fournisseur de services a rencontré une condition inattendue qui l'a empêché de répondre à la demande." }
  , { ServiceError.PROCESS_UNAVAILABLE,            "Le Prestataire n'est pas en mesure de traiter la demande en raison d'une surcharge temporaire ou d'une maintenance du service. Le service API REST du fournisseur de services n'est pas en cours d'exécution." }
  , { ServiceError.PROCESS_AUTHORIZATION,          "La demande n'est pas autorisée. Les informations d'authentification incluses dans cette demande sont manquantes ou non valides." }
  , { ServiceError.PROCESS_FORBIDDEN,              "\"La demande était valide, mais le serveur refuse l'action. L'utilisateur peut ne pas avoir les autorisations nécessaires pour une ressource, ou peut avoir besoin d'un compte quelconque." }
  , { ServiceError.PROCESS_NOT_ALLOWED,            "La méthode HTTP spécifiée dans la demande n'est pas prise en charge pour cet URI de demande." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,         "Le fournisseur de services ne peut pas produire une réponse correspondant à la liste des valeurs acceptables définies dans les en-têtes de négociation de contenu proactive de la demande, et que le fournisseur de services n'est pas disposé à fournir une représentation par défaut." }
  , { ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED,  "Le fournisseur de services refuse d'accepter la demande car le format de la charge utile est dans un format non pris en charge." }
  , { ServiceError.PROCESS_EXISTS,                 "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,             "%s" }
  , { ServiceError.PROCESS_PRECONDITION,           "Échec de la modification. La ressource a changé sur le fournisseur de services. Raison du fournisseur : \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,          "Échec de la modification. La ressource n'a pas changé. Raison du fournisseur : \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,              "Le fournisseur de services refuse de traiter une demande car la charge utile de la demande est plus importante que ce que le serveur est disposé ou capable de traiter." }
  , { ServiceError.PROCESS_INVALID_VERSION,        "La version du protocole SCIM spécifiée n'est pas prise en charge." }
  , { ServiceError.PROCESS_TOO_MANY,               "Le filtre spécifié donne beaucoup plus de résultats que ce que le serveur est prêt à calculer ou à traiter. Par exemple, un filtre tel que \"(userName pr)\" par lui-même renverrait toutes les entrées avec un \"userName\" et PEUT ne pas être acceptable pour le fournisseur de service." }
  , { ServiceError.PROCESS_MUTABILITY,             "La tentative de modification n'est pas compatible avec la mutabilité ou l'état actuel de l'attribut cible (par exemple, la modification d'un attribut \"immuable\" avec une valeur existante)." }
  , { ServiceError.PROCESS_SENSITIVE,              "La demande spécifiée ne peut pas être complétée, en raison de la transmission d'informations sensibles (par exemple, personnelles) dans un URI de demande." }
  , { ServiceError.PROCESS_UNIQUENESS,             "Une ou plusieurs valeurs d'attribut sont déjà utilisées ou sont réservées." }
  , { ServiceError.PROCESS_NOTARGET,               "Le \"chemin\" spécifié n'a pas donné d'attribut ou de valeur d'attribut qui pourrait être exploité. Cela se produit lorsque la valeur \"path\" spécifiée contient un filtre qui ne donne aucune correspondance." }
  , { ServiceError.PROCESS_INVALID_FILTER,         "%s" }
  , { ServiceError.PROCESS_INVALID_PATH,           "%s" }
  , { ServiceError.PROCESS_INVALID_SYNTAX,         "%s" }
  , { ServiceError.PROCESS_INVALID_VALUE,          "%s" }

    // GWS-00131 - 00140 object errors
  , { ServiceError.OBJECT_NOT_EXISTS,              "%1$s n'est pas mappé avec [%3$s] pour [%2$s] chez le fournisseur de services." }
  , { ServiceError.OBJECT_AMBIGUOUS,               "%1$s est mappé de manière ambiguë avec [%3$s] pour [%2$s] chez le fournisseur de services." }

    // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,           " ... et plus ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,           "%s sur le fil %s\n" }
  , { ServiceMessage.LOGGER_CLIENT_REQUEST,        "Envoi de la demande client" }
  , { ServiceMessage.LOGGER_CLIENT_RESPONSE,       "Réponse client reçue" }
  , { ServiceMessage.LOGGER_SERVER_REQUEST,        "Le serveur a reçue une demande" }
  , { ServiceMessage.LOGGER_SERVER_RESPONSE,       "Le serveur a répondu avec une réponse" }

    // GWS-01011 - 01020 connection messages
  , { ServiceMessage.CONNECTING_BEGIN,             "Connexion à l'URL contextuelle [%1$s] avec l'utilisateur [%2$s]..." }
  , { ServiceMessage.CONNECTING_SUCCESS,           "Connexion à l'URL de contexte [%1$s] pour l'utilisateur [%2$s] établie." }
  , { ServiceMessage.OPERATION_BEGIN,              "L'opération [%1$s] sur le contexte [%2$s] avec l'utilisateur [%3$s] démarre..." }
  , { ServiceMessage.OPERATION_SUCCESS,            "L'opération [%1$s] sur le contexte [%2$s] avec l'utilisateur [%3$s] a réussie." }
  , { ServiceMessage.AUTHENTICATION_BEGIN,         "Authentification du compte [%2$s] sur l'URL de contexte [%1$s]..." }
  , { ServiceMessage.AUTHENTICATION_SUCCESS,       "Compte [%2$s] sur l'URL de contexte [%1$s] authentifié." }

     // GWS-01021 - 01030 reconciliation process related messages
  , { ServiceMessage.NOTHING_TO_CHANGE,            "Aucun enregistrement disponible à récupérer auprès du fournisseur de services." }

     // GWS-01031 - 01040 provisioning process related messages
  , { ServiceMessage.UNSPECIFIED_ERROR,            "Aucune autre information dans le message d'erreur." }
  , { ServiceMessage.STATUS_NOT_PROVIDED,          "Indicateur d'état non fourni ; défini comme activé." }
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