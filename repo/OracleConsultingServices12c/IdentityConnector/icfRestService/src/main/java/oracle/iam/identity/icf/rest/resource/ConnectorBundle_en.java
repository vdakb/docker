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

    File        :   ConnectorBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConnectorBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code english
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ConnectorBundle_en extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.SERVICE_HOST_LABEL,          "Service Domain" }
  , { Connector.Endpoint.SERVICE_HOST_HINT,           "The name or IP address of the domain where the Service Provider is running." }
  , { Connector.Endpoint.SERVICE_PORT_LABEL,          "Service Port" }
  , { Connector.Endpoint.SERVICE_PORT_HINT,           "The TCP/IP port number used to communicate with the Service Provider." }
  , { Connector.Endpoint.SERVER_HOST_LABEL,           "Server Host" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "The name or IP address of the host where the Service Provider is running." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Server Port" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "The TCP/IP port number used to communicate with the Service Provider." }
  , { Connector.Endpoint.SECURE_LABEL,                "Secure Socket" }
  , { Connector.Endpoint.SECURE_HINT,                 "Secure Socket" }
  , { Connector.Endpoint.CONTEXT_LABEL,               "Root Context" }
  , { Connector.Endpoint.CONTEXT_HINT,                "Root Context" }
  , { Connector.Endpoint.TYPE_CONTENT_LABEL,          "Content Type" }
  , { Connector.Endpoint.TYPE_CONTENT_HINT,           "The \"Content Type\" header field is used to specify the nature of the data in the body of an entity, by giving type and subtype identifiers, and by providing auxiliary information that may be required for certain types." }
  , { Connector.Endpoint.TYPE_ACCEPT_LABEL,           "Accept Type" }
  , { Connector.Endpoint.TYPE_ACCEPT_HINT,            "The \"Accept Type\" request-header field can be used to specify certain media types which are acceptable for the response.\nAccept headers can be used to indicate that the request is specifically limited to a small set of desired types, as in the case of a request for an in-line image." }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_LABEL, "Authentication Scheme" }
  , { Connector.Endpoint.AUTHENTICATION_SCHEME_HINT,  "The type of authentication" }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_LABEL,  "Authorization Server" }
  , { Connector.Endpoint.AUTHORIZATION_SERVER_HINT,   "Authorization Server" }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_LABEL,     "Client Identifier" }
  , { Connector.Endpoint.CLIENT_IDENTIFIER_HINT,      "The client identifier a public identifier for applications.\nEven though it's public, it's best that it isn't guessable by third parties, so many implementations use something like a 32-character hex string.\nIt must also be unique across all clients that an authorization server handles.\nIf the client ID is guessable, it makes it slightly easier to craft phishing attacks against arbitrary applications." }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Principal Name" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Username of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Principal Password" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Credential of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_LABEL,    "Resource Owner Name" }
  , { Connector.Endpoint.RESOURCE_OWNERNAME_HINT,     "An entity capable of authorizing access to a protected resource. When the resource owner is a person, it is called an user." }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_LABEL,   "Resource Owner Credential." }
  , { Connector.Endpoint.RESOURCE_CREDENTIAL_HINT,    "Credential of the resource owner." }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Locale Country"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Locale Country"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Locale Language"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Locale Language"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Locale TimeZone"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Locale TimeZone"}

  , { Connector.Feature.FETCH_SCHEMA_LABEL,           "Fetch Schema"}
  , { Connector.Feature.FETCH_SCHEMA_HINT,            "Whether the schema supported by this connector is always fetched from the Service Provider or is provided statically by the implementation."}
  , { Connector.Feature.RFC_9110_LABEL,               "RFC-9110"}
  , { Connector.Feature.RFC_9110_HINT,                "Either the validation of the HTTP methods is performed according to the rules of RFC-9110 or it is skipped."}
  , { Connector.Feature.ENTERPRICE_FEATURE_LABEL,     "Enterprise Feature"}
  , { Connector.Feature.ENTERPRICE_FEATURE_HINT,      "The target system is licensed for the use of extended functionalities."}
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