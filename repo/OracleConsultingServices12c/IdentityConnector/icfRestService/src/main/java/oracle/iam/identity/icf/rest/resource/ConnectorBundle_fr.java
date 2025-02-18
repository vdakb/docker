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
    Subsystem   :   Generic WebService Connector

    File        :   ConnectorBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConnectorBundle_fr
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
public class ConnectorBundle_fr extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.SERVICE_HOST_LABEL,          "Domaine de services" }
  , { Connector.Endpoint.SERVICE_HOST_HINT,           "Le nom ou l'adresse IP du domaine sur lequel le fournisseur de services s'exécute." }
  , { Connector.Endpoint.SERVICE_PORT_LABEL,          "Port de service" }
  , { Connector.Endpoint.SERVICE_PORT_HINT,           "Le numéro de port TCP/IP utilisé pour communiquer avec le fournisseur de services." }
  , { Connector.Endpoint.SERVER_HOST_LABEL,           "Hôte du serveur" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "Le nom ou l'adresse IP de l'hôte sur lequel le fournisseur de services s'exécute." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Port de serveur" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "Le numéro de port TCP/IP utilisé pour communiquer avec le fournisseur de services." }
  , { Connector.Endpoint.SECURE_LABEL,                "Prise sécurisée " }
  , { Connector.Endpoint.SECURE_HINT,                 "Prise sécurisée " }
  , { Connector.Endpoint.CONTEXT_LABEL,               "Contexte racine" }
  , { Connector.Endpoint.CONTEXT_HINT,                "Contexte racine" }
  , { Connector.Endpoint.TYPE_CONTENT_LABEL,          "Type de contenu" }
  , { Connector.Endpoint.TYPE_CONTENT_HINT,           "Le champ d'en-tête \"Content Type\" est utilisé pour spécifier la nature des données dans le corps d'une entité, en donnant des identifiants de type et de sous-type, et en fournissant des informations auxiliaires qui peuvent être nécessaires pour certains types." }
  , { Connector.Endpoint.TYPE_ACCEPT_LABEL,           "Accepter le type" }
  , { Connector.Endpoint.TYPE_ACCEPT_HINT,            "Le champ d'en-tête de demande \"Accept Type\" peut être utilisé pour spécifier certains types de supports qui sont acceptables pour la réponse.\nLes en-têtes Accept peuvent être utilisés pour indiquer que la demande est spécifiquement limitée à un petit ensemble de types souhaités, comme dans le cas d'une demande d'image en ligne." }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_LABEL, "Schéma d'authentification" }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_HINT,  "Le type d'authentification" }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_LABEL,  "Serveur d'autorisation" }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_HINT,   "Serveur d'autorisation" }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_LABEL,     "Identifiant client" }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_HINT,      "L'identifiant client est un identifiant public pour les applications.\nMême s'il est public, il est préférable qu'il ne soit pas devinable par des tiers, donc de nombreuses implémentations utilisent quelque chose comme une chaîne hexadécimale de 32 caractères.\nIl doit également être unique pour tous les clients gérés par un serveur d'autorisation.\nSi l'ID client est devinable, cela facilite légèrement la création d'attaques de phishing contre des applications arbitraires." }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Nom principal" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Nom d'utilisateur du principal à authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Mot de passe principal" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Identifiant du principal pour s'authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_LABEL,    "Nom du propriétaire de la ressource" }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_HINT,     "Entité capable d'autoriser l'accès à une ressource protégée. Lorsque le propriétaire de la ressource est une personne, il est appelé utilisateur." }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_LABEL,   "Identifiant du propriétaire de la ressource" }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_HINT,    "Identifiant du propriétaire de la ressource." }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Pays"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Pays"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Langue locale"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Langue locale"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Fuseau horaire local"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Fuseau horaire local"}

  , { Connector.Feature.FETCH_SCHEMA_LABEL,           "Récupérer le schéma"}
  , { Connector.Feature.FETCH_SCHEMA_HINT,            "Si le schéma pris en charge par ce connecteur est toujours récupéré auprès du fournisseur de services ou fourni de manière statique par l'implémentation."}
  , { Connector.Feature.RFC_9110_LABEL,               "RFC-9110"}
  , { Connector.Feature.RFC_9110_HINT,                "Soit la validation des méthodes HTTP est effectuée selon les règles de la RFC-9110, soit elle est ignorée."}
  , { Connector.Feature.ENTERPRICE_FEATURE_LABEL,     "Fonctionnalité d'entreprise"}
  , { Connector.Feature.ENTERPRICE_FEATURE_HINT,      "Le système cible dispose d'une licence pour l'utilisation de fonctionnalités étendues."}
  , { Connector.Feature.CONTEXT_SCHEMA_LABEL,         "Schéma d'URI de contexte"}
  , { Connector.Feature.CONTEXT_SCHEMA_HINT,          "SCIM fournit un type de ressource pour les ressources \"Schémas\".\nUn HTTP GET vers ce point de terminaison est utilisé pour découvrir les types de ressources disponibles sur un fournisseur de services SCIM (par exemple, Utilisateurs et groupes)."}
  , { Connector.Feature.CONTEXT_RESOURCE_LABEL,       "Ressource URI de contexte"}
  , { Connector.Feature.CONTEXT_RESOURCE_HINT,        "SCIM fournit un type de ressource pour les ressources « ResourceTypes ».\nUn HTTP GET vers ce point de terminaison est utilisé pour récupérer des informations sur les schémas de ressources pris en charge par un fournisseur de services SCIM."}
  , { Connector.Feature.CONTEXT_ACCOUNT_LABEL,        "Utilisateur d'URI de contexte"}
  , { Connector.Feature.CONTEXT_ACCOUNT_HINT,         "SCIM fournit un type de ressource pour les ressources \"Utilisateurs\"."}
  , { Connector.Feature.CONTEXT_GROUP_LABEL,          "Groupe d'URI de contexte"}
  , { Connector.Feature.CONTEXT_GROUP_HINT,           "SCIM fournit un type de ressource pour les ressources \"Groupes\"."}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,     "Délai de connection dépassé" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,      "Délai de connection dépassé" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "Délai de réponse" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "Délai d'expiration pendant lequel le client de service n'obtient pas de réponse.\\nLorsqu'une demande de service est faite par un client à un serveur et que le serveur ne répond pas pour une raison quelconque, le client attend indéfiniment que le serveur réponde jusqu'à l'expiration du délai TCP. Côté client, l'expérience de l'utilisateur est essentiellement un blocage de processus. Afin de contrôler la demande de service en temps opportun, un délai de lecture peut être configuré pour le fournisseur de services.\\nSi cette propriété n'est pas spécifiée, la valeur par défaut est d'attendre la réponse jusqu'à ce qu'elle soit reçue." }
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