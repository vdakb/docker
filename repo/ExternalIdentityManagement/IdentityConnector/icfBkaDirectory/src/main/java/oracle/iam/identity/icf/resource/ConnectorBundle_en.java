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
    Subsystem   :   Directory Account Connector

    File        :   ConnectorBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.resource;

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
    { Connector.Bundle.NAME,                             "BKA Directory Services Connector" }
  , { Connector.Bundle.VERSION,                          "12.1.3" }

  , { Connector.Endpoint.HOST_LABEL,                     "Server Name" }
  , { Connector.Endpoint.HOST_HINT,                      "Server Name" }
  , { Connector.Endpoint.PORT_LABEL,                     "Server Port" }
  , { Connector.Endpoint.PORT_HINT,                      "Server Port" }
  , { Connector.Endpoint.ROOT_CONTEXT_LABEL,             "Root Context" }
  , { Connector.Endpoint.ROOT_CONTEXT_HINT,              "Root Context" }
  , { Connector.Endpoint.SECURE_LABEL,                   "Secure Socket" }
  , { Connector.Endpoint.SECURE_HINT,                    "Secure Socket" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,       "Principal Name" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,        "Username of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,       "Principal Password" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,        "Credential of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.COUNTRY_LABEL,                  "Locale Country"}
  , { Connector.Endpoint.COUNTRY_HINT,                   "Locale Country"}
  , { Connector.Endpoint.LANGUAGE_LABEL,                 "Locale Language"}
  , { Connector.Endpoint.LANGUAGE_HINT,                  "Locale Language"}
  , { Connector.Endpoint.TIMEZONE_LABEL,                 "Locale TimeZone"}
  , { Connector.Endpoint.TIMEZONE_HINT,                  "Locale TimeZone"}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,        "Connection Timeout" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,         "The timeout period to establish a connection to the Service Provider endpoint.\nThis property affects the TCP timeout when opening a connection to a content provider. When connection pooling has been requested, this property also specifies the maximum wait time or a connection when all connections in pool are in use and the maximum pool size has been reached.\\nIf this property has not been specified, the content provider will wait indefinitely for a pooled connection to become available, and to wait for the default TCP timeout to take effect when creating a new connection." }
  , { Connector.Connection.CONNECT_RETRYCOUNT_LABEL,     "Connection Retry Count" }
  , { Connector.Connection.CONNECT_RETRYCOUNT_HINT,      "The number of consecutive attempts to be made at establishing a connection with the Service Provider endpoint." }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL,  "Connection Retry Interval" }
  , { Connector.Connection.CONNECT_RETRYINTERVAL_LABEL,  "The interval (in milliseconds) between consecutive attempts at establishing a connection with the Service Provider endpoint." }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,       "Response Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,       "The timeout period the Service Client doesn't get a response.\\nWhen an service request is made by a client to a server and the server does not respond for some reason, the client waits forever for the server to respond until the TCP timeouts. On the client-side what the user experiences is esentially a process hang. In order to control the service request in a timely manner, a read timeout can be configured for the service provider.\\nIf this property is not specified, the default is to wait for the response until it is received." }

  , { Connector.Feature.URL_ENCODING_LABEL,              "URL Encoding" }
  , { Connector.Feature.URL_ENCODING_HINT,               "URL Encoding" }
  , { Connector.Feature.CONTEXT_FACTORY_LABEL,           "Context Factory" }
  , { Connector.Feature.CONTEXT_FACTORY_HINT,            "Context Factory" }
  , { Connector.Feature.SECURITY_PROVIDER_LABEL,         "Security Provider" }
  , { Connector.Feature.SECURITY_PROVIDER_HINT,          "Security Provider" }
  , { Connector.Feature.FAILOVER_LABEL,                  "Failover" }
  , { Connector.Feature.FAILOVER_HINT,                   "Failover" }
  , { Connector.Feature.REFERENTIAL_INTEGRITY_LABEL,     "Referential Integrity" }
  , { Connector.Feature.REFERENTIAL_INTEGRITY_HINT,      "Referential integrity is a mechanism for ensuring that all references are properly maintained after delete, rename, or move operations." }
  , { Connector.Feature.TIMESTAMP_FORMAT_LABEL,          "Timestamp Format" }
  , { Connector.Feature.TIMESTAMP_FORMAT_HINT,           "Timestamp Format" }
  , { Connector.Feature.SIMPLEPAGE_CONTROL_LABEL,        "Simple Page Control" }
  , { Connector.Feature.SIMPLEPAGE_CONTROL_HINT,         "Simple Page Control" }
  , { Connector.Feature.VIRUALLIST_CONTROL_LABEL,        "Virtual List Control" }
  , { Connector.Feature.VIRUALLIST_CONTROL_HINT,         "Virtual List Control" }
  , { Connector.Feature.SCHEMA_CONTAINER_LABEL,          "Schema Container" }
  , { Connector.Feature.SCHEMA_CONTAINER_HINT,           "Schema Container" }
  , { Connector.Feature.CHANGELOG_CONTAINER_LABEL,       "ChangeLog Container" }
  , { Connector.Feature.CHANGELOG_CONTAINER_HINT,        "ChangeLog Container" }
  , { Connector.Feature.CHANGELOG_CHANGETYPE_LABEL,      "ChangeLog Change Type" }
  , { Connector.Feature.CHANGELOG_CHANGETYPE_HINT,       "ChangeLog Change Type" }
  , { Connector.Feature.CHANGELOG_TARGETGUID_LABEL,      "ChangeLog Target GUID" }
  , { Connector.Feature.CHANGELOG_TARGETGUID_HINT,       "ChangeLog Target GUID" }
  , { Connector.Feature.CHANGELOG_TARGETDN_LABEL,        "ChangeLog Target DN" }
  , { Connector.Feature.CHANGELOG_TARGETDN_HINT,         "ChangeLog Target DN" }
  , { Connector.Feature.OBJECTCLASS_NAME_LABEL,          "Object Class Name" }
  , { Connector.Feature.OBJECTCLASS_NAME_HINT,           "Object Class Name" }
  , { Connector.Feature.BINARY_LABEL,                    "Binary Attributes" }
  , { Connector.Feature.BINARY_HINT,                     "Binary Attributes" }
  , { Connector.Feature.DISTINGUISHED_NAME_LABEL,        "Distinguished Name" }
  , { Connector.Feature.DISTINGUISHED_NAME_HINT,         "Distinguished Name" }
  , { Connector.Feature.DISTINGUISHED_NAME_PREFIX_LABEL, "Distinguished Name Prefix" }
  , { Connector.Feature.DISTINGUISHED_NAME_PREFIX_HINT,  "Distinguished Name Prefix" }
  , { Connector.Feature.ENTRY_IDENTIFIER_PREFIX_LABEL,   "Entry IdentifierPrefix" }
  , { Connector.Feature.ENTRY_IDENTIFIER_PREFIX_HINT,    "Entry Identifier Prefix" }
  , { Connector.Feature.ENTRY_STATUS_PREFIX_LABEL,       "Entry Status Prefix" }
  , { Connector.Feature.ENTRY_STATUS_PREFIX_HINT,        "Entry Status Prefix" }
  , { Connector.Feature.ENTRY_CREATOR_PREFIX_LABEL,      "Entry Creator Prefix" }
  , { Connector.Feature.ENTRY_CREATOR_PREFIX_HINT,       "Entry Creator Prefix" }
  , { Connector.Feature.ENTRY_CREATED_PREFIX_LABEL,      "Entry Created Prefix" }
  , { Connector.Feature.ENTRY_CREATED_PREFIX_HINT,       "Entry Created Prefix" }
  , { Connector.Feature.ENTRY_MODIFIER_PREFIX_LABEL,     "Entry Modifier Prefix" }
  , { Connector.Feature.ENTRY_MODIFIER_PREFIX_HINT,      "Entry Modifier Prefix" }
  , { Connector.Feature.ENTRY_MODIFIED_PREFIX_LABEL,     "Entry Modified Prefix" }
  , { Connector.Feature.ENTRY_MODIFIED_PREFIX_HINT,      "Entry Modified Prefix" }
  , { Connector.Feature.ROLE_CLASS_LABEL,                "Role Object Class" }
  , { Connector.Feature.ROLE_CLASS_HINT,                 "Role Object Class" }
  , { Connector.Feature.ROLE_PREFIX_LABEL,               "Role Object Prefix" }
  , { Connector.Feature.ROLE_PREFIX_HINT,                "Role Object Prefix" }
  , { Connector.Feature.ROLE_MEMBER_PREFIX_LABEL,        "Role Member Prefix" }
  , { Connector.Feature.ROLE_MEMBER_PREFIX_HINT,         "Role Member Prefix" }
  , { Connector.Feature.GROUP_CLASS_LABEL,               "Group Object Class" }
  , { Connector.Feature.GROUP_CLASS_HINT,                "Group Object Class" }
  , { Connector.Feature.GROUP_PREFIX_LABEL,              "Group Object Prefix" }
  , { Connector.Feature.GROUP_PREFIX_HINT,               "Group Object Prefix" }
  , { Connector.Feature.GROUP_MEMBER_PREFIX_LABEL,       "Group Member Prefix" }
  , { Connector.Feature.GROUP_MEMBER_PREFIX_HINT,        "Group Member Prefix" }
  , { Connector.Feature.ACCOUNT_CLASS_LABEL,             "Account Object Class" }
  , { Connector.Feature.ACCOUNT_CLASS_HINT,              "Account Object Class" }
  , { Connector.Feature.ACCOUNT_PREFIX_LABEL,            "Account Object Prefix" }
  , { Connector.Feature.ACCOUNT_PREFIX_HINT,             "Account Object Prefix" }
  , { Connector.Feature.ACCOUNT_ROLE_PREFIX_LABEL,       "Account Member Prefix" }
  , { Connector.Feature.ACCOUNT_ROLE_PREFIX_HINT,        "Account Role Prefix" }
  , { Connector.Feature.ACCOUNT_GROUP_PREFIX_LABEL,      "Account Role Prefix" }
  , { Connector.Feature.ACCOUNT_GROUP_PREFIX_HINT,       "Account Group Prefix" }
  , { Connector.Feature.ACCOUNT_CREDENTIAL_PREFIX_LABEL, "Account Group Prefix" }
  , { Connector.Feature.ACCOUNT_CREDENTIAL_PREFIX_HINT,  "Account Member Prefix" }
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