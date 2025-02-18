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

    System      :   Foundation Service Extension
    Subsystem   :   Generic REST Library

    File        :   ServiceBundle_fr.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

import oracle.hst.platform.core.utility.ListResourceBundle;

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
     // GWS-00001 - GWS-00010 General error (Undefined)
    { ServiceError.UNHANDLED,                         "Une exception non gérée s'est produite : %1$s" }

     // GWS-00031 - 00040 connectivity errors
  , { ServiceError.CONNECTION_UNKNOWN_HOST,           "L'hôte %1$s est inconnu." }
  , { ServiceError.CONNECTION_CREATE_SOCKET,          "Pourrait créer une connexion réseau pour héberger [%1$s] sur le port [%2$s]." }
  , { ServiceError.CONNECTION_SECURE_SOCKET,          "Pourrait créer une connexion réseau sécurisée pour héberger [%1$s] sur le port [%2$s]." }
  , { ServiceError.CONNECTION_CERTIFICATE_HOST,       "Impossible de trouver le chemin de certification valide vers la cible demandée [%1$s]." }
  , { ServiceError.CONNECTION_ERROR,                  "Erreur rencontrée lors de la connexion au fournisseur de services." }
  , { ServiceError.CONNECTION_TIMEOUT,                "La connexion réseau à l'hôte [%1$s] sur le port [%2$s] a expiré." }
  , { ServiceError.CONNECTION_NOT_AVAILABLE,          "Le problème peut être lié à la connectivité physique ou le fournisseur de services n'est pas actif." }
  , { ServiceError.CONNECTION_AUTHENTICATION,         "Le nom principal [%1$s] ou le mot de passe est incorrect, le système n'a pas pu accéder avec les informations d'identification fournies." }
  , { ServiceError.CONNECTION_AUTHORIZATION,          "Le nom principal [%1$s] n'est pas autorisé." }

     // GWS-00061 - 00070 request method errors
  , { ServiceError.REQUEST_METHOD_NOTALLOWED,         "Method not allowed." }
  , { ServiceError.REQUEST_METHOD_NOTIMPLEMENTED,     "Method not implemented." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED, "Method expects an id to operate." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID,  "Resource specifies ID [%s] in payload for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND,    "Resource with ID [%s] does not exists for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EXISTS,      "Resource with name [%s] already exists for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EMPTY,       "Empty Entity." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE,   "Attribute %s:%s must not be null." }

     // GWS-00051 - 00060 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,        "Unexpected end of path string." }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,        "Unexpected end of filter string." }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,         "Unexpected character [%s] at position %d for token starting at %d." }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,             "Unexpected token [%s]" }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,       "Unrecognized attribute operator [%s] at position %d. Expected: eq,ne,co,sw,ew,pr,gt,ge,lt,le!" }
  , { ServiceError.PATH_INVALID_FILTER,               "Invalid value filter: [%s]!" }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,           "Expected '(' at position %d!" }
  , { ServiceError.PATH_INVALID_PARENTHESIS,          "No opening parenthesis matching closing parenthesis at position %d!" }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,        "Attribute path expected at position %d!" }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,        "Attribute name expected at position %d!" }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,       "Invalid attribute path at position %d: [%s]!" }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,       "Invalid attribute name starting at position %d: [%s]!" }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,     "Invalid comparison value at position %d: [%s]!" }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,          "Path can not target sub-attributes more than one level deep." }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,         "Path can not include a value filter on sub-attributes." }
  , { ServiceError.PATH_INVALID_VALUE_ENCODING,       "Value at path [%s] is not a valid [%s] string." }

     // GWS-00071 - 00080 request parameter errors
  , { ServiceError.PARAMETER_FILTER_NOTPERMITTED,     "Filtering not allowed." }
  , { ServiceError.PARAMETER_START_INVALID_VALUE,     "[%s] n'est pas une valeur valide pour le paramètre startIndex." }
  , { ServiceError.PARAMETER_COUNT_INVALID_VALUE,     "[%s] n'est pas une valeur valide pour le paramètre count." }
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,      "[%s] n'est pas une valeur valide pour le paramètre sortBy: [%s]." }
  , { ServiceError.PARAMETER_ORDER_INVALID_VALUE,     "[%s] n'est pas une valeur valide pour le paramètre sortOrder: [%s]." }

     // GWS-00081 - 0090 filtering errors
  , { ServiceError.FILTER_INCONSISTENT_METHOD,        "Filter Translation method is inconsistent: [%s]." }
  , { ServiceError.FILTER_INCONSISTENT_EXPRESSION,    "Filter Translation expresssion is inconsistent: [%s]." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_LT,      "Filter less-than may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_LE,      "Filter less-than-or-equal may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_GT,      "Filter greater-than may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_INVALID_VALUE_TYPE_GE,      "Filter greater-than-or-equal may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_INVALID_TARGET_TYPE,        "Attribute [%s] does not have a multi-valued or complex value." }
  , { ServiceError.FILTER_INVALID_TARGET_MATCH,       "Attribute [%s] does not have a value matching the filter [%s]." }

     // GWS-00091 - 00120 processing errors
  , { ServiceError.PROCESS_UNEXPECTED,                "Le fournisseur de services a rencontré une condition inattendue qui l'a empêché de répondre à la demande." }
  , { ServiceError.PROCESS_UNAVAILABLE,               "Le Prestataire n'est pas en mesure de traiter la demande en raison d'une surcharge temporaire ou d'une maintenance du service. Le service API REST du fournisseur de services n'est pas en cours d'exécution." }
  , { ServiceError.PROCESS_AUTHORIZATION,             "La demande n'est pas autorisée. Les informations d'authentification incluses dans cette demande sont manquantes ou non valides." }
  , { ServiceError.PROCESS_FORBIDDEN,                 "La demande était valide, mais le serveur refuse l'action. L'utilisateur peut ne pas avoir les autorisations nécessaires pour une ressource, ou peut avoir besoin d'un compte quelconque." }
  , { ServiceError.PROCESS_NOT_ALLOWED,               "La méthode HTTP spécifiée dans la demande n'est pas prise en charge pour cet URI de demande." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,            "Le fournisseur de services ne peut pas produire une réponse correspondant à la liste des valeurs acceptables définies dans les en-têtes de négociation de contenu proactive de la demande, et que le fournisseur de services n'est pas disposé à fournir une représentation par défaut." }
  , { ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED,     "Le fournisseur de services refuse d'accepter la demande car le format de la charge utile est dans un format non pris en charge." }
  , { ServiceError.PROCESS_EXISTS,                    "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,                "%s" }
  , { ServiceError.PROCESS_PRECONDITION,              "Échec de la modification. La ressource a changé sur le fournisseur de services. Raison du fournisseur : \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,             "Échec de la modification. La ressource n'a pas changé. Raison du fournisseur : \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,                 "Le fournisseur de services refuse de traiter une demande car la charge utile de la demande est plus importante que ce que le serveur est disposé ou capable de traiter." }
  , { ServiceError.PROCESS_INVALID_VERSION,           "La version du protocole SCIM spécifiée n'est pas prise en charge." }
  , { ServiceError.PROCESS_TOO_MANY,                  "Le filtre spécifié donne beaucoup plus de résultats que ce que le serveur est prêt à calculer ou à traiter. Par exemple, un filtre tel que \"(userName pr)\" par lui-même renverrait toutes les entrées avec un \"userName\" et PEUT ne pas être acceptable pour le fournisseur de service." }
  , { ServiceError.PROCESS_MUTABILITY,                "La tentative de modification n'est pas compatible avec la mutabilité ou l'état actuel de l'attribut cible (par exemple, la modification d'un attribut \"immuable\" avec une valeur existante)." }
  , { ServiceError.PROCESS_SENSITIVE,                 "La demande spécifiée ne peut pas être complétée, en raison de la transmission d'informations sensibles (par exemple, personnelles) dans un URI de demande." }
  , { ServiceError.PROCESS_UNIQUENESS,                "Une ou plusieurs valeurs d'attribut sont déjà utilisées ou sont réservées." }
  , { ServiceError.PROCESS_NOTARGET,                  "Le \"chemin\" spécifié n'a pas donné d'attribut ou de valeur d'attribut qui pourrait être exploité. Cela se produit lorsque la valeur \"path\" spécifiée contient un filtre qui ne donne aucune correspondance." }
  , { ServiceError.PROCESS_INVALID_FILTER,            "%s" }
  , { ServiceError.PROCESS_INVALID_PATH,              "%s" }
  , { ServiceError.PROCESS_INVALID_SYNTAX,            "%s" }
  , { ServiceError.PROCESS_INVALID_VALUE,             "%s" }

     // GWS-00121 - 00130 patch operation errors
  , { ServiceError.PATCH_OPERATIONTYPE_UNKNOWN,       "Unkown patch operation type [%s]." }
  , { ServiceError.PATCH_MULTIVALUE_NOTPERMITTED,     "Patch operation contains multiple values." }
  , { ServiceError.PATCH_OPERATION_VALUE_NOTNULL,     "Value field must not be null or an empty container."}
  , { ServiceError.PATCH_OPERATION_ADD_OBJECT,        "Value field must be a JSON object containing the attributes to add." }
  , { ServiceError.PATCH_OPERATION_ADD_ARRAY,         "Value field must be a JSON array containing the attributes to add." }
  , { ServiceError.PATCH_OPERATION_ADD_PATH,          "Path field for add operations must not include any value selection filters." }
  , { ServiceError.PATCH_OPERATION_REPLACE_VALUE,     "Value field must be a JSON object containing the attributes to replace." }
  , { ServiceError.PATCH_OPERATION_REMOVE_PATH,       "Path field must not be null for remove operations." }

     // GWS-00131 - 00140 resource operational state errors
  , { ServiceError.RESOURCE_EXISTS,                   "Resource of type [%2$s] with id [%1$s] already exists." }
  , { ServiceError.RESOURCE_NOT_EXISTS,               "Resource of type [%2$s] with id [%1$s] does not exists." }
  , { ServiceError.RESOURCE_NOT_CREATED,              "Resource of type [%2$s] with id [%1$s] can not be created." }
  , { ServiceError.RESOURCE_NOT_MODIFIED,             "Resource of type [%2$s] with id [%1$s] can not be modified." }
  , { ServiceError.RESOURCE_NOT_DELETED,              "Resource of type [%2$s] with id [%1$s] can not be deleted." }
  , { ServiceError.RESOURCE_MATCH_IDENTIFIER,         "Identifier [%1$s] given in the payload does not match the one in the resource path [%2$s]." }

     // GWS-00141 - 00150 http status related errors
  , { ServiceError.HTTP_CODE_NOP,                     "HTTP-NOP: Un état de réponse inconnu [%1$s] a été reçu du point de terminaison."}
  , { ServiceError.HTTP_CODE_400,                     "HTTP-400: La requête n'a pas pu être comprise par le serveur en raison d'une syntaxe incorrecte. Le client NE DEVRAIT PAS répéter la demande sans modifications."}
  , { ServiceError.HTTP_CODE_401,                     "HTTP-401: La requête n'a pas abouti car il manque des informations d'authentification valides pour la ressource demandée."}
  , { ServiceError.HTTP_CODE_403,                     "HTTP-403: La requête était valide, mais le serveur refuse d'agir. L'utilisateur peut ne pas disposer des autorisations nécessaires pour une ressource ou avoir besoin d'un compte quelconque."}
  , { ServiceError.HTTP_CODE_404,                     "HTTP-404: La ressource demandée est introuvable mais peut être disponible à l'avenir. Les demandes ultérieures du client sont autorisées."}
  , { ServiceError.HTTP_CODE_408,                     "HTTP-408: Le serveur a expiré en attendant la demande."}
  , { ServiceError.HTTP_CODE_409,                     "HTTP-409: La requête n'a pas pu être exécutée en raison d'un conflit avec l'état actuel de la ressource cible."}
  , { ServiceError.HTTP_CODE_415,                     "HTTP-415: Le serveur a rejeté cette demande car l'entité de la demande est dans un format non pris en charge par la ressource demandée pour la méthode demandée."}
  , { ServiceError.HTTP_CODE_500,                     "HTTP-500: Le serveur a rencontré une erreur interne ou une mauvaise configuration et n'a pas pu traiter votre demande."}
  , { ServiceError.HTTP_CODE_501,                     "HTTP-501: Soit le serveur ne reconnaît pas la méthode de requête, soit il n'a pas la capacité de répondre à la requête. Cela implique généralement une disponibilité future (par exemple, une nouvelle fonctionnalité d'une API de service Web)."}
  , { ServiceError.HTTP_CODE_502,                     "HTTP-502: Le serveur agissait en tant que passerelle ou proxy et a reçu une réponse non valide du serveur en amont."}
  , { ServiceError.HTTP_CODE_503,                     "HTTP 503: Le serveur est actuellement incapable de traiter la demande en raison d'une surcharge temporaire ou d'une maintenance planifiée, qui sera probablement atténuée après un certain délai."}
  , { ServiceError.HTTP_CODE_504,                     "HTTP 504: Le serveur agissait en tant que passerelle ou proxy et n'a pas reçu de réponse rapide du serveur en amont."}

     // GWS-00151 - 00160 authorization errors
  , { ServiceError.OAUTH_FLOW_NOT_FINISH,             "L'autorisation n'est pas terminée et le jeton d'accès n'a pas été reçu. Appelez start() puis finish() pour effectuer l'autorisation." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,            "Paramètre \"état\" non valide. « état » utilisé dans la demande d'autorisation ne correspond pas à « l'état » de la réponse d'autorisation." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,           "Erreur lors de la demande du jeton d'accès. État de la réponse: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,          "Erreur lors de l'actualisation d'un jeton d'accès. État de la réponse: %1$s." }

     // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,              " ... and more ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,              "%s on thread %s\n" }
  , { ServiceMessage.LOGGER_CLIENT_REQUEST,           "Sending client request" }
  , { ServiceMessage.LOGGER_CLIENT_RESPONSE,          "Client response received" }
  , { ServiceMessage.LOGGER_SERVER_REQUEST,           "Server has received a request" }
  , { ServiceMessage.LOGGER_SERVER_RESPONSE,          "Server responded with a response" }
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