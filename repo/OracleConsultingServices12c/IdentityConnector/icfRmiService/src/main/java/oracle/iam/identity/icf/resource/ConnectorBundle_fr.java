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

    File        :   ConnectorBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

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
    { Connector.Endpoint.SERVER_HOST_LABEL,           "Hôte du serveur" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "Le nom ou l'adresse IP de l'hôte sur lequel le fournisseur de services s'exécute." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Port de serveur" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "Le numéro de port TCP/IP utilisé pour communiquer avec le fournisseur de services." }
  , { Connector.Endpoint.SERVER_NAME_LABEL,           "Nom du serveur" }
  , { Connector.Endpoint.SERVER_NAME_HINT,            "Le nom du Manager Server utilisé pour communiquer avec le fournisseur de services." }
  , { Connector.Endpoint.SERVER_TYPE_LABEL,           "Type de serveur" }
  , { Connector.Endpoint.SERVER_TYPE_HINT,            "Type de Manager Server utilisé pour communiquer avec le fournisseur de services." }
  , { Connector.Endpoint.SECURE_SOCKET_LABEL,         "Prise sécurisée " }
  , { Connector.Endpoint.SECURE_SOCKET_HINT,          "Prise sécurisée " }
  , { Connector.Endpoint.ROOT_CONTEXT_LABEL,          "Contexte racine" }
  , { Connector.Endpoint.ROOT_CONTEXT_HINT,           "Contexte racine" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Nom principal" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Nom d'utilisateur du principal à authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Mot de passe principal" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Identifiant du principal pour s'authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.DOMAIN_USERNAME_LABEL,       "Domain Principal" }
  , { Connector.Endpoint.DOMAIN_USERNAME_HINT,        "Nom d'utilisateur de l'administrateur principal du domaine." }
  , { Connector.Endpoint.DOMAIN_PASSWORD_LABEL,       "Mot de passe du domaine" }
  , { Connector.Endpoint.DOMAIN_PASSWORD_HINT,        "Informations d’identification de l’administrateur principal du domaine." }
  , { Connector.Endpoint.LOGIN_CONFIG_LABEL,          "Login Config" }
  , { Connector.Endpoint.LOGIN_CONFIG_HINT,           "Login Config" }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Pays"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Pays"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Langue locale"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Langue locale"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Fuseau horaire local"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Fuseau horaire local"}

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