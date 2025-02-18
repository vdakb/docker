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

    File        :   ConnectorBundle_de.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_de.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
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
public class ConnectorBundle_de extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.DRIVER_LABEL,                  "Datenbank Treiber" }
  , { Connector.Endpoint.DRIVER_HINT,                   "Treiberklasse. Beispieltreiberwerte Oracle - oracle.jdbc.driver.OracleDriver, MSSQL - com.microsoft.sqlserver.jdbc.SQLServerDriver, MySQL - com.mysql.jdbc.Driver, DB2 - com.ibm.db2.jcc.DB2Driver, Sybase - com.sybase.jdbc2.jdbc.SybDriver" }
  , { Connector.Endpoint.HOST_LABEL,                    "Datenbank Host" }
  , { Connector.Endpoint.HOST_HINT,                     "Datenbank Host" }
  , { Connector.Endpoint.PORT_LABEL,                    "Datenbank Host" }
  , { Connector.Endpoint.PORT_HINT,                     "Datenbank Host" }
  , { Connector.Endpoint.SECURE_LABEL,                  "Secure Socket" }
  , { Connector.Endpoint.SECURE_HINT,                   "Secure Socket" }
  , { Connector.Endpoint.NAME_LABEL,                    "Datenbank Name" }
  , { Connector.Endpoint.NAME_HINT,                     "Datenbank Name" }
  , { Connector.Endpoint.SCHEMA_LABEL,                  "Datenbank Schema" }
  , { Connector.Endpoint.SCHEMA_HINT,                   "Datenbank Schema" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,      "Admin-Benutzername" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,       "Benutzername des Prinzipals zur Authentifizierung beim Dienstanbieter." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,      "Kennwort des Benutzers" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,       "Anmeldeinformationen des Prinzipals zur Authentifizierung beim Dienstanbieter." }
  , { Connector.Endpoint.COUNTRY_LABEL,                 "Gebietsschema Land"}
  , { Connector.Endpoint.COUNTRY_HINT,                  "Gebietsschema Land"}
  , { Connector.Endpoint.LANGUAGE_LABEL,                "Gebietsschema Sprache"}
  , { Connector.Endpoint.LANGUAGE_HINT,                 "Gebietsschema Sprache"}
  , { Connector.Endpoint.TIMEZONE_LABEL,                "Gebietsschema Zeitzone"}
  , { Connector.Endpoint.TIMEZONE_HINT,                 "Gebietsschema Zeitzone"}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,       "Connection Timeout" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,        "Connection Timeout" }
  , { Connector.Connection.CONNECT_RETRYCOUNT_LABEL,    "Connection Retry Count" }
  , { Connector.Connection.CONNECT_RETRYCOUNT_HINT,     "Connection Retry Count" }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, "Connection Retry Interval" }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL, "Connection Retry Interval" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,      "Response Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,      "The timeout period the Service Client doesn't get a response.\\nWhen an service request is made by a client to a server and the server does not respond for some reason, the client waits forever for the server to respond until the TCP timeouts. On the client-side what the user experiences is esentially a process hang. In order to control the service request in a timely manner, a read timeout can be configured for the service provider.\\nIf this property is not specified, the default is to wait for the response until it is received." }

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