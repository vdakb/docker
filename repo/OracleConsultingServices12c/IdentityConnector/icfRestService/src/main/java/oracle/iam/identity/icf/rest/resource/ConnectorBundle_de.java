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

    File        :   ConnectorBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConnectorBundle_de
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code german
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConnectorBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.SERVICE_HOST_LABEL,          "Service Domain" }
  , { Connector.Endpoint.SERVICE_HOST_HINT,           "Der Name oder die IP-Adresse des Dienstes, auf dem der Anbieter ausgeführt wird." }
  , { Connector.Endpoint.SERVICE_PORT_LABEL,          "Service Port" }
  , { Connector.Endpoint.SERVICE_PORT_HINT,           "Die TCP / IP-Portnummer, die für die Kommunikation mit dem Dienstanbieter verwendet wird." }
  , { Connector.Endpoint.SERVER_HOST_LABEL,           "Server Host" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "Der Name oder die IP-Adresse des Hosts, auf dem der Dienstanbieter ausgeführt wird." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Server Port" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "Die TCP / IP-Portnummer, die für die Kommunikation mit dem Dienstanbieter verwendet wird." }
  , { Connector.Endpoint.SECURE_LABEL,                "Secure Socket" }
  , { Connector.Endpoint.SECURE_HINT,                 "Secure Socket (SSL / TLS) ist eine Standardsicherheitstechnologie zum Erzeugen einer verschlüsselten Verbindung zwischen einem Server und einem Client -- normalerweise einem Webserver (Website) und einem Browser oder einem Mailserver und einem Mailclient (z. B. Outlook).\nMit SSL / TLS können vertrauliche Informationen wie Kreditkartennummern, Sozialversicherungsnummern und Anmeldeinformationen sicher übertragen werden." }
  , { Connector.Endpoint.CONTEXT_LABEL,               "Root Context" }
  , { Connector.Endpoint.CONTEXT_HINT,                "Root Context" }
  , { Connector.Endpoint.TYPE_CONTENT_LABEL,          "Content Type" }
  , { Connector.Endpoint.TYPE_CONTENT_HINT,           "Der HTTP-Header \"Content Type\" wird verwendet, um die Art der Daten im Hauptteil einer Entität anzugeben, indem Typ- und Untertyp-IDs angegeben werden, und falls erforderlisch weitere Zusatzinformationen, die für bestimmte Typen erforderlich sein können, bereitgestellt werden." }
  , { Connector.Endpoint.TYPE_ACCEPT_LABEL,           "Accept Type" }
  , { Connector.Endpoint.TYPE_ACCEPT_HINT,            "Der HTTP-Header \"Accept Type\" kann verwendet werden, um bestimmte Medientypen anzugeben, die für die Antwort akzeptabel sind. Accept-Header können verwendet werden, um anzuzeigen, dass die Anforderung speziell auf einen kleinen Satz gewünschter Typen beschränkt ist, wie im Fall der Anforderung eines Inline-Image's." }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_LABEL,  "Authorization Server" }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_HINT,   "Authorization Server" }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_LABEL, "Authentifizierungsschema" }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_HINT,  "Ein Authentifizierungsschema ist eine Methode oder eine Vorlage zum übergeben von Anmeldeinformationen von einem Benutzerkonto an ein Authentifizierungssystem. Diese Schemata unterscheiden sich in ihrer Sicherheit und Komplexität, wobei einige nur die Authentifizierung handhaben, während andere auch die Autorisierung handhaben können." }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_LABEL,     "Client Identifier" }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_HINT,      "Die Client-ID ist eine öffentliche ID für Anwendungen. Obwohl sie öffentlich ist, ist es ratsam, dass sie von Dritten nicht erraten werden kann. In vielen Implementierungen wird so etwas wie eine 32-stellige hexadezimale Zeichenfolge verwendet. Sie muss außerdem für alle Clients, die ein Autorisierungsserver verarbeitet, eindeutig sein. Wenn die Client-ID erraten werden kann, ist es einfacher, Phishing-Angriffe gegen beliebige Anwendungen zu erstellen." }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Principal Name" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Username of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Principal Password" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Credential of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_LABEL,    "Resource Owner Name" }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_HINT,     "Eine Entität, die den Zugriff auf eine geschützte Ressource autorisieren kann. Wenn der Besitzer einer Ressource eine Person ist, wird er als Benutzer bezeichnet." }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_LABEL,   "Resource Owner Credential" }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_HINT,    "Credential of the resource Owner" }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Gebietsschema Land"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Gebietsschema Land"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Gebietsschema Sprache"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Gebietsschema Sprache"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Gebietsschema Zeitzone"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Gebietsschema Zeitzone"}

  , { Connector.Feature.FETCH_SCHEMA_LABEL,           "Fetch Schema"}
  , { Connector.Feature.FETCH_SCHEMA_HINT,            "Whether the schema supported by this connector is always fetched from the Service Provider or is provided statically by the implementation."}
  , { Connector.Feature.RFC_9110_LABEL,               "RFC-9110"}
  , { Connector.Feature.RFC_9110_HINT,                "Entweder wird die Validierung der HTTP-Methoden gemäß den Regeln von RFC-9110 durchgeführt oder sie wird übersprungen."}
  , { Connector.Feature.ENTERPRICE_FEATURE_LABEL,     "Enterprise Funktion"}
  , { Connector.Feature.ENTERPRICE_FEATURE_HINT,      "Das Zielsystem ist für die Nutzung erweiterter Funktionalitäten lizenziert."}
  , { Connector.Feature.CONTEXT_SCHEMA_LABEL,         "Context URI Schema"}
  , { Connector.Feature.CONTEXT_SCHEMA_HINT,          "SCIM provides a resource type for \"Schemas\" resources.\nAn HTTP GET to this endpoint is used to discover the types of resources available on a SCIM service provider (for example, Users and Groups)."}
  , { Connector.Feature.CONTEXT_RESOURCE_LABEL,       "Context URI Resource"}
  , { Connector.Feature.CONTEXT_RESOURCE_HINT,        "SCIM provides a resource type for \"ResourceTypes\" resources.\nAn HTTP GET to this endpoint is used to retrieve information about resource schemas supported by a SCIM service provider."}
  , { Connector.Feature.CONTEXT_ACCOUNT_LABEL,        "Context URI User"}
  , { Connector.Feature.CONTEXT_ACCOUNT_HINT,         "SCIM provides a resource type for \"Users\" resources."}
  , { Connector.Feature.CONTEXT_GROUP_LABEL,          "Context URI Group"}
  , { Connector.Feature.CONTEXT_GROUP_HINT,           "SCIM provides a resource type for \"Groups\" resources."}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,     "Connection Timeout" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,      "Connection Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "Response Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "The timeout period the Service Client doesn't get a response.\\nWhen an service request is made by a client to a server and the server does not respond for some reason, the client waits forever for the server to respond until the TCP timeouts. On the client-side what the user experiences is esentially a process hang. In order to control the service request in a timely manner, a read timeout can be configured for the service provider.\\nIf this property is not specified, the default is to wait for the response until it is received." }
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