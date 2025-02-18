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

    File        :   ConnectorBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

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
public class ConnectorBundle_de extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.SERVER_HOST_LABEL,           "Server Host" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "Der Name oder die IP-Adresse des Hosts, auf dem der Dienstanbieter ausgeführt wird." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Server Port" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "Die TCP / IP-Portnummer, die für die Kommunikation mit dem Dienstanbieter verwendet wird." }
  , { Connector.Endpoint.SERVER_TYPE_LABEL,           "Server Typ" }
  , { Connector.Endpoint.SERVER_TYPE_HINT,            "Der Typ des Manager-Servers, der für die Kommunikation mit dem Dienstanbieter verwendet wird." }
  , { Connector.Endpoint.SECURE_SOCKET_LABEL,         "Sichere Verbindung" }
  , { Connector.Endpoint.SECURE_SOCKET_HINT,          "Secure Socket (SSL / TLS) ist eine Standardsicherheitstechnologie zum Erzeugen einer verschlüsselten Verbindung zwischen einem Server und einem Client -- normalerweise einem Webserver (Website) und einem Browser oder einem Mailserver und einem Mailclient (z. B. Outlook).\nMit SSL / TLS können vertrauliche Informationen wie Kreditkartennummern, Sozialversicherungsnummern und Anmeldeinformationen sicher übertragen werden." }
  , { Connector.Endpoint.ROOT_CONTEXT_LABEL,          "Root Context" }
  , { Connector.Endpoint.ROOT_CONTEXT_HINT,           "Root Context" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Benutzerame" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Benutzername des Prinzipals zur Authentifizierung beim Dienstanbieter." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Kennwort des Benutzers" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Anmeldeinformationen des Prinzipals zur Authentifizierung beim Dienstanbieter." }
  , { Connector.Endpoint.DOMAIN_USERNAME_LABEL,       "Domain Administrator" }
  , { Connector.Endpoint.DOMAIN_USERNAME_HINT,        "Benutzername des Domain Administrators." }
  , { Connector.Endpoint.DOMAIN_PASSWORD_LABEL,       "Kennwort des Domain Administrator" }
  , { Connector.Endpoint.DOMAIN_PASSWORD_HINT,        "Anmeldeinformationen des Domain Administrator." }
  , { Connector.Endpoint.LOGIN_CONFIG_LABEL,          "Login Config" }
  , { Connector.Endpoint.LOGIN_CONFIG_HINT,           "Login Config" }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Gebietsschema Land"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Gebietsschema Land"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Gebietsschema Sprache"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Gebietsschema Sprache"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Gebietsschema Zeitzone"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Gebietsschema Zeitzone"}

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