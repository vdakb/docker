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
    Subsystem   :   Generic Database Connector

    File        :   ConnectorBundle_fr.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_fr.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
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
    { Connector.Endpoint.DRIVER_LABEL,                  "Pilote de connecteur " }
  , { Connector.Endpoint.DRIVER_HINT,                   "Classe de conducteur. Exemples de valeurs de pilote Oracle - oracle.jdbc.driver.OracleDriver, MSSQL - com.microsoft.sqlserver.jdbc.SQLServerDriver, MySQL - com.mysql.jdbc.Driver, DB2 - com.ibm.db2.jcc.DB2Driver, Sybase - com .sybase.jdbc2.jdbc.SybDriver" }
  , { Connector.Endpoint.HOST_LABEL,                    "Hôte de connecteur" }
  , { Connector.Endpoint.HOST_HINT,                     "Hôte de connecteur" }
  , { Connector.Endpoint.PORT_LABEL,                    "Hôte de connecteur" }
  , { Connector.Endpoint.PORT_HINT,                     "Hôte de connecteur" }
  , { Connector.Endpoint.SECURE_LABEL,                  "Prise sécurisée  " }
  , { Connector.Endpoint.SECURE_HINT,                   "Prise sécurisée  " }
  , { Connector.Endpoint.NAME_LABEL,                    "Nom du connecteur" }
  , { Connector.Endpoint.NAME_HINT,                     "Nom du connecteur" }
  , { Connector.Endpoint.SCHEMA_LABEL,                  "Schéma de connecteur" }
  , { Connector.Endpoint.NAME_HINT,                     "Schéma de connecteur" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,      "Nom principal" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,       "Nom d'utilisateur du principal à authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,      "Mot de passe principal" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,       "Identifiant du principal pour s'authentifier auprès du fournisseur de services." }
  , { Connector.Endpoint.COUNTRY_LABEL,                 "Pays"}
  , { Connector.Endpoint.COUNTRY_HINT,                  "Pays"}
  , { Connector.Endpoint.LANGUAGE_LABEL,                "Langue locale"}
  , { Connector.Endpoint.LANGUAGE_HINT,                 "Langue locale"}
  , { Connector.Endpoint.TIMEZONE_LABEL,                "Fuseau horaire local"}
  , { Connector.Endpoint.TIMEZONE_HINT,                 "Fuseau horaire local"}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,       "Délai de connection dépassé" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,        "Délai de connection dépassé" }
  , { Connector.Connection.CONNECT_RETRYCOUNT_LABEL,    "Nombre de tentatives de connexion" }
  , { Connector.Connection.CONNECT_RETRYCOUNT_HINT,     "Nombre de tentatives de connexion" }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, "Intervalle de nouvelle tentative de connexion" }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, "Intervalle de nouvelle tentative de connexion" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,      "Délai de réponse " }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,      "Délai d'expiration pendant lequel le client de service n'obtient pas de réponse.\nLorsqu'une demande de service est faite par un client à un serveur et que le serveur ne répond pas pour une raison quelconque, le client attend indéfiniment que le serveur réponde jusqu'à l'expiration du délai TCP. Côté client, l'expérience de l'utilisateur est essentiellement un blocage de processus. Afin de contrôler la demande de service en temps opportun, un délai de lecture peut être configuré pour le fournisseur de services.\nSi cette propriété n'est pas spécifiée, la valeur par défaut est d'attendre la réponse jusqu'à ce qu'elle soit reçue." }

  , { Connector.Feature.SYSTEM_TIMESTAMP_LABEL,         "System Timestamp" }
  , { Connector.Feature.SYSTEM_TIMESTAMP_HINT,          "System Timestamp" }
  , { Connector.Feature.ENFORCE_AUTOCOMMIT_LABEL,       "Enforce AutoCommit" }
  , { Connector.Feature.ENFORCE_AUTOCOMMIT_HINT,        "Enforce AutoCommit" }
  , { Connector.Feature.ROWNUM_ATTRIBUTE_LABEL,         "Row Number Pseudo Column" }
  , { Connector.Feature.ROWNUM_ATTRIBUTE_HINT,          "Row Number Pseudo Column" }
  , { Connector.Feature.ENTRY_IDENTIFIER_LABEL,         "Entry Identifier Prefix" }
  , { Connector.Feature.ENTRY_IDENTIFIER_HINT,          "Entry Identifier Prefix" }
  , { Connector.Feature.ENTRY_UNIQUENAME_LABEL,         "Entry Unique Name Prefix" }
  , { Connector.Feature.ENTRY_UNIQUENAME_HINT,          "Entry Unique Name Prefix" }
  , { Connector.Feature.ENTRY_PASSWORD_LABEL,           "Entry Password Prefix" }
  , { Connector.Feature.ENTRY_PASSWORD_HINT,            "Entry Password Prefix" }
  , { Connector.Feature.ENTRY_STATUS_LABEL,             "Entry Status Prefix" }
  , { Connector.Feature.ENTRY_STATUS_HINT,              "Entry Sttaus Prefix" }
  , { Connector.Feature.ENTRY_CREATOR_LABEL,            "Entry Creator Prefix" }
  , { Connector.Feature.ENTRY_CREATOR_HINT,             "Entry Creator Prefix" }
  , { Connector.Feature.ENTRY_CREATED_LABEL,            "Entry Created Prefix" }
  , { Connector.Feature.ENTRY_CREATED_HINT,             "Entry Created Prefix" }
  , { Connector.Feature.ENTRY_MODIFIER_LABEL,           "Entry Modifier Prefix" }
  , { Connector.Feature.ENTRY_MODIFIER_HINT,            "Entry Modifier Prefix" }
  , { Connector.Feature.ENTRY_MODIFIED_LABEL,           "Entry Modified Prefix" }
  , { Connector.Feature.ENTRY_MODIFIED_HINT,            "Entry Modified Prefix" }
  , { Connector.Feature.FETCH_SCHEMA_LABEL,             "Fetch Schema"}
  , { Connector.Feature.FETCH_SCHEMA_HINT,              "Whether the schema supported by this connector is always fetched from the Service Provider or is provided statically by the implementation."}
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