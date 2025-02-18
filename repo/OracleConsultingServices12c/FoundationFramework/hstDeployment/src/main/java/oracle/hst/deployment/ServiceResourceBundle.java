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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ServiceResourceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceResourceBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServiceResourceBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceResourceBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // task error messages
    { ServiceError.UNHANDLED,                                "Unhandled exception occured: %1$s" }
  , { ServiceError.GENERAL,                                  "Encounter some problems: %1$s" }
  , { ServiceError.ABORT,                                    "Execution aborted: %1$s"}
  , { ServiceError.NOTIMPLEMENTED,                           "Feature is not yet implemented"}
  , { ServiceError.CLASSNOTFOUND,                            "Class %1$s was not found in the classpath"}
  , { ServiceError.CLASSNOTCREATE,                           "Class %1$s has not been created"}
  , { ServiceError.CLASSINVALID,                             "Class %1$s must be a subclass of %2$s"}
  , { ServiceError.CLASSCONSTRUCTOR,                         "Class %1$s does not have constructor parameter %2$s"}
  , { ServiceError.CLASSCONSTRUCTORARG,                      "Class %1$s does not accept constructor parameter %2$s"}
  , { ServiceError.CLASSCONSTRUCTORNOARG,                    "Class %1$s does not accept a no-arg constructor"}

     // 00011 - 00020 task related errors
  , { ServiceError.CONSOLE_UNAVAILABLE,                      "The console is not available to accept input." }
  , { ServiceError.CONSOLE_INPUT,                            "An error occurred while accepting the input." }
  , { ServiceError.TASK_ATTRIBUTE_MISSING,                   "Missing task attribute %1$s" }
  , { ServiceError.TASK_ATTRIBUTE_MIXEDUP,                   "A nested element of type %1$s cannot defined if attribute %2$s is of %3$s" }
  , { ServiceError.TASK_ELEMENT_MISSING,                     "A nested element of type %1$s must be specified" }
  , { ServiceError.TASK_ELEMENT_MIXEDUP,                     "A nested element of type %1$s cannot mixed with element(s) of type %2$s" }
  , { ServiceError.TASK_ELEMENT_ONLYONCE,                    "A nested element of type %1$s can defined only once" }
  , { ServiceError.TASK_ELEMENT_UNEXPECTED,                  "Unexpected nested element of type %1$s occured" }
  , { ServiceError.TASK_ELEMENT_UNEXPECTED_CONTEXT,          "Unexpected nested element of type %1$s in context of %2$s occured" }

     // 00021 - 00030 type related errors
  , { ServiceError.TYPE_ATTRIBUTE_MANDATORY,                 "Type attribute %1$s must not be empty" }
  , { ServiceError.TYPE_ATTRIBUTE_FORMAT,                    "Type attribute %1$s has wrong format. Must be of format %2$s" }
  , { ServiceError.TYPE_ATTRIBUTE_VALUE,                     "%2$s is not a legal value for attribute %1$s. Must be one of format %3$s" }
  , { ServiceError.TYPE_ATTRIBUTE_MISSING,                   "Missing type attribute %1$s" }
  , { ServiceError.TYPE_ATTRIBUTE_MIXEDUP,                   "Type attribute %1$s cannot mixed with other attributes" }
  , { ServiceError.TYPE_REFERENCE_MISMATCH,                  "%1$s does not denote a %2$s. %3$s is a %4$s" }
  , { ServiceError.TYPE_REFERENCE_MISSING,                   "Missing type reference %1$s at %2$s" }
  , { ServiceError.TYPE_PARAMETER_EMPTY,                     "Specify at least one parameter" }
  , { ServiceError.TYPE_PARAMETER_MISSING,                   "Missing type parameter %1$s" }
  , { ServiceError.TYPE_PARAMETER_MANDATORY,                 "Type parameter %1$s must not be empty" }

     // 00031 - 00040 context related errors
  , { ServiceError.CONTEXT_MANDATORY,                        "Task requires a context to perform" }
  , { ServiceError.CONTEXT_CONNECTION,                       "Unable to establish connection. Reason: %1$s"}
  , { ServiceError.CONTEXT_ACCESS_DENIED,                    "Access denied for account %1$s. Either user name or credentials are wrong."}
  , { ServiceError.CONTEXT_INITIALIZE,                       "Could not initialize JNDI context !"}
  , { ServiceError.CONTEXT_CLOSE,                            "Could not close JNDI context !"}
  , { ServiceError.CONTEXT_ENVIRONMENT,                      "Could not obtain environment from context !"}

     // 00041 - 00050 XML structure related errors
  , { ServiceError.OBJECT_ELEMENT_MANDATORY,                 "Specify at least one element -- a single resource or a resource collection." }
  , { ServiceError.OBJECT_ELEMENT_ONLYONCE,                  "The object [%1$s] already added to this task." }
  , { ServiceError.OBJECT_PARAMETER_MANDATORY,               "Specify at least one parameter for operation." }
  , { ServiceError.OBJECT_PARAMETER_ONLYONCE,                "The parameter [%1$s] already added to this type." }
  , { ServiceError.OBJECT_ELEMENT_NOTFOUND,                  "%1$s [%2$s] does not exists in the system." }
  , { ServiceError.OBJECT_ELEMENT_AMBIGUOS,                  "%1$s [%2$s] is defined ambiguosly." }
  , { ServiceError.OBJECT_ELEMENT_EXISTS,                    "%1$s [%2$s] already exists in the system. Performing update only on object" }
  , { ServiceError.OBJECT_ELEMENT_ALREADYASSINGED,           "%1$s [%2$s] already assigned" }
  , { ServiceError.OBJECT_ELEMENT_NOTASSINGED,               "%1$s [%2$s] not assigned" }
  , { ServiceError.OBJECT_OPERATION_MANDATORY,               "You must specifiy an operation." }
  , { ServiceError.OBJECT_OPERATION_INVALID,                 "Operation %1$s invalid on %2$s." }

     // 00051 - 00060 server related errors
  , { ServiceError.CONTROL_OBJECTNAME_MALFORMED,             "Malformed ObjectName. Reason: %1$s" }
  , { ServiceError.CONTROL_SERVERNAME_NOTFOUND,              "Server %1$s does not exists" }
  , { ServiceError.CONTROL_ATTRIBUTENAME_NOTFOUND,           "Attribute %1$s does not exists" }
  , { ServiceError.CONTROL_BEANINSTANCE_NOTFOUND,            "Instance does not exists. Reason: %1$s" }
  , { ServiceError.CONTROL_QUERY_FAILED,                     "Could not query the mbeans server. Reason: %1$s" }

     // 00061 - 00070 regular expression related errors
  , { ServiceError.EXPRESSION_UNDEFINED_BITVALUES,           "Undefined bit values are set in the regular expression compile options" }
  , { ServiceError.EXPRESSION_INVALID,                       "An error is contained in the regular expression [%1$s]: %2$s" }

     // 00071 - 00080 metadata namespace related errors
  , { ServiceError.METADATA_CONFIG_ONLYONCE,                 "A metadata path collection already added." }
  , { ServiceError.CUSTCLASS_CONFIG_ONLYONCE,                "A customization configuration collection already added." }
  , { ServiceError.METADATA_NAMESPACE_ABSOLUE,               "Only absolute namespace paths are valid. [%1$s] is not an absolute path." }
  , { ServiceError.METADATA_NAMESPACE_NOTEXISTS,             "Namespace mapping not found in adf-config for [%1$s]" }
  , { ServiceError.METADATA_NAMESPACE_REFERENCE,             "Error occurred while accessing the Metadata Namespace %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_NAMESPACE_TYPE,                  "Error occurred while accessing the Metadata Namespace Type %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_NAMESPACE_MANDATORY,             "Specify at least one namespace path -- a file or a resource collection." }
  , { ServiceError.METADATA_NAMESPACE_ONLYONCE,              "The namespace [%1$s] already added to this task." }
  , { ServiceError.METADATA_CUSTCLASS_ONLYONCE,              "The customization class [%1$s] already added to this task." }

     // 00081 - 00090 metadata connection related errors
  , { ServiceError.METADATA_INSTANCE_CREATE,                 "Create instance to metadata store failed. Reason: %1$s" }
  , { ServiceError.METADATA_INSTANCE_CLOSE,                  "Closing instance to metadata store failed. Reason: %1$s" }
  , { ServiceError.METADATA_SESSION_CREATE,                  "Create session to metadata store failed. Reason: %1$s" }
  , { ServiceError.METADATA_SESSION_COMMIT,                  "Error occurred while commiting changes to MDS. Reason: %1$s" }

     // 00091 - 00100 metadata sandbox related errors
  , { ServiceError.METADATA_SANDBOX_CREATE,                  "Error occurred while creating sandbox [%1$s] in MDS. Reason: %2$s" }
  , { ServiceError.METADATA_SANDBOX_ACTIVATE,                "Error occurred while activation sandbox [%1$s] in MDS. Reason: %2$s" }
  , { ServiceError.METADATA_SANDBOX_NOTACTIVE,               "An active sandbox is required to perfomr this operation" }

     // 00101 - 00110 metadata document related errors
  , { ServiceError.METADATA_DOCUMENT_ONLYONCE,               "The Metadata Object [%1$s] already added to this task." }
  , { ServiceError.METADATA_DOCUMENT_CREATE,                 "Error occurred while creating the Metadata Object %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_DOCUMENT_TRANSFORMATION,         "Error occurred while transforming the Metadata Object %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_DOCUMENT_REFERENCE,              "Error occurred while accessing the Metadata Object %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_DOCUMENT_READONLY,               "Metadata Object [%1$s] in path [%2$s] is read only" }
  , { ServiceError.METADATA_DOCUMENT_UPDATE,                 "Error occurred while updating the Metadata Object %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_DOCUMENT_UPLOAD,                 "Error occurred while uploading the Metadata Object %1$s. Reason: %2$s" }
  , { ServiceError.METADATA_DOCUMENT_DOWNLOAD,               "Error occurred while downloading the Metadata Object %1$s. Reason: %2$s" }

     // 00111 - 00120 managed bean related errors
  , { ServiceError.MANAGEDBEAN_OBJECTNAME_MALFORMED,         "Provided String [%1$s] isn''t a valid ObjectName"}
  , { ServiceError.MANAGEDBEAN_DOMAIN_NOTFOUND,              "Unable to find the WebLogic Domain named [%1$s]"}
  , { ServiceError.MANAGEDBEAN_SERVER_NOTFOUND,              "Unable to find the Server named [%1$s]"}
  , { ServiceError.MANAGEDBEAN_INSTANCE_NOTFOUND,            "Managed Bean instance [%1$s] not found. Reason: %2$s"}
  , { ServiceError.MANAGEDBEAN_SIGNATURE_NOTFOUND,           "Managed Bean signature in instance [%1$s] seems not match. Reason: %2$s"}
  , { ServiceError.MANAGEDBEAN_ATTRIBUTE_NOTFOUND,           "Managed Bean attribute [%2$s] in instance [%1$s] not found. Reason: %3$s"}

     // 00121 - 00130 server security related errors
  , { ServiceError.SECURITY_REALM_NOTEXISTS,                 "Security realm [%1$s] does not exists."}
  , { ServiceError.SECURITY_AUTHENTICATOR_NOTEXISTS,         "Authenticator [%2$s] does not exists in security realm [%1$s]"}

    // 00131 - 00140 operational related errors
  , { ServiceError.OPERATION_UNSUPPORTED,                    "Action [%1$s] is not applicable on [%2$s]"}
  , { ServiceError.OPERATION_CREATE_FAILED,                  "Creation of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_DELETE_FAILED,                  "Deletion of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_ENABLE_FAILED,                  "Enable of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_DISABLE_FAILED,                 "Disable of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_MODIFY_FAILED,                  "Modification of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_VALIDATE_FAILED,                "Validation of %1$s [%2$s] failed. Reason: %3$s"}
  , { ServiceError.OPERATION_ASSIGN_FAILED,                  "Assignment of %1$s [%2$s] to %3$s [%4$s] failed. Reason: %5$s"}
  , { ServiceError.OPERATION_REVOKE_FAILED,                  "Revocation of %1$s [%2$s] from %3$s [%4$s] failed. Reason: %5$s"}
  , { ServiceError.OPERATION_REPORT_FAILED,                  "Report of %1$s [%2$s] failed. Reason: %3$s"}

    // 00141 - 00150 http status related errors
  , { ServiceError.HTTP_RESPONSE_CODE_400,                   "HTTP-400: The request could not be understood by the server due to malformed syntax. The client SHOULD NOT repeat the request without modifications."}
  , { ServiceError.HTTP_RESPONSE_CODE_401,                   "HTTP-401: Authorization failure."}
  , { ServiceError.HTTP_RESPONSE_CODE_403,                   "HTTP-403: The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort."}
  , { ServiceError.HTTP_RESPONSE_CODE_404,                   "HTTP-404: The requested resource [%1$s] could not be found but may be available in the future. Subsequent requests by the client are permissible."}
  , { ServiceError.HTTP_RESPONSE_CODE_408,                   "HTTP-408: The server timed out waiting for the request."}
  , { ServiceError.HTTP_RESPONSE_CODE_500,                   "HTTP-500: A generic error message, given when an unexpected condition was encountered and no more specific message is suitable."}
  , { ServiceError.HTTP_RESPONSE_CODE_501,                   "HTTP-501: The server either does not recognize the request method, or it lacks the ability to fulfill the request. Usually this implies future availability (e.g., a new feature of a web-service API)."}
  , { ServiceError.HTTP_RESPONSE_CODE_502,                   "HTTP-502: The server was acting as a gateway or proxy and received an invalid response from the upstream server."}
  , { ServiceError.HTTP_RESPONSE_CODE_503,                   "HTTP 503: The server either does not recognize the request method, or it lacks the ability to fulfill the request. Usually this implies future availability (e.g., a new feature of a web-service API)."}
  , { ServiceError.HTTP_RESPONSE_CODE_504,                   "HTTP 504: The server was acting as a gateway or proxy and did not receive a timely response from the upstream server."}

    // 00151 - 00160 http operation related errors
  , { ServiceError.HTTP_CONNECTION_MALFORMED_URI,            "The endpoint uri specified: [%1$s] is malformed. Reason: %2$s"}
  , { ServiceError.HTTP_CONNECTION_MALFORMED_URL,            "The endpoint url specified: [%1$s] is malformed. Reason: %2$s"}
  , { ServiceError.HTTP_CONNECTION_PROTOCOL,                 "The protocol [%1$s] is not supported by this service"}
  , { ServiceError.HTTP_CONNECTION_ENCODING,                 "The character encoding of the input data is not supported by HTTP! Reason: %2$s"}
  , { ServiceError.HTTP_CONNECTION_UNKNOWN_HOST,             "Host [%1$s] is unknown"}
  , { ServiceError.HTTP_CONNECTION_UNAVAILABLE,              "Connection to Target System is unavailable: [%1$s]"}
  , { ServiceError.HTTP_CONNECTION_REQUEST_TIMEOUT,          "Connection to Target System got timed out: [%1$s]"}
  , { ServiceError.HTTP_CONNECTION_SOCKET_TIMEOUT,           "Connection to Target System got timed out: [%1$s]"}
  , { ServiceError.HTTP_CONNECTION_RESPONSE_CONVERT,         "Response received from the server could not be read correctly! Reason: %1$s"}

    // 00171 - 00180 webservice related errors
  , { ServiceError.WEBSERVICE_PROVIDER_NOTFOUND,             "Unable to extract WebService Provider Description."}
  , { ServiceError.WEBSERVICE_PROVIDERPORT_NOTFOUND,         "Unable to extract WebService Provider Port Description."}
  , { ServiceError.WEBSERVICE_APPLICATION_NOTFOUND,          "Unable to find the Application."}
  , { ServiceError.WEBSERVICE_APPLICATION_AMBIGUOS,          "Try to use server name with application name. Multiple server targets deployed for application [%1$s]"}
  , { ServiceError.WEBSERVICE_APPLICATION_VERSION,           "Unable to resolve the Application Version ID for [%1$s]"}
  , { ServiceError.WEBSERVICE_WEBSERVICE_EMPTY,              "Unable to find any WebServices with the specified application."}
  , { ServiceError.WEBSERVICE_WEBSERVICE_NOTFOUND,           "Unable to find the specified Web Service [%2$s] in the specified application [%1$s]"}
  , { ServiceError.WEBSERVICE_WEBSERVICE_AMBIGUOS,           "Try using moduleType and moduleName parameters. Multiple WebServices found for the specified service name in the specified application [%1$s]."}
  , { ServiceError.WEBSERVICE_COMPOSITE_NOTFOUND,            "Unable to find the SOA Composite or Service."}
  , { ServiceError.WEBSERVICE_MODULE_TYPE_UNKNOWN,           "Unknown ServiceRefMapping module type."}
  , { ServiceError.WEBSERVICE_WEBSERVICE_PORT,               "Unable to find the specified WebService Port in the specified Application."}
  , { ServiceError.WEBSERVICE_POLICY_MANAGER,                "The wsm-pm is not installed or not available for WebService Manager Policy query."}
  , { ServiceError.WEBSERVICE_POLICY_INVALID,                "Invalid policy name URI [%1$s]."}
  , { ServiceError.WEBSERVICE_POLICY_NOTFOUND,               "Unable to find the WebService Manager Policy [%1$s]"}
  , { ServiceError.WEBSERVICE_POLICY_AMBIGUOS,               "Multiple WebService Manager Policies found for [%1$s]"}
  , { ServiceError.WEBSERVICE_POLICY_EMPTY,                  "Currently no policy with category owsm-security attached to WebService Port %1$s"}
  , { ServiceError.WEBSERVICE_POLICY_INCOMPATIBLE,           "Cannot configure! Please first detach the existing WLS Policy."}
  , { ServiceError.WEBSERVICE_POLICY_SERVER_CLIENT,          "Cannot attach server policy [%1$s] to client side."}
  , { ServiceError.WEBSERVICE_POLICY_CLIENT_SERVER,          "Cannot attach client policy [%1$s] to server side."}

    // 00181 - 00200 LDAP connectivity errors
  , { ServiceError.LDAP_CONNECTION_UNKNOWN_HOST,             "Host %1$s is unknown" }
  , { ServiceError.LDAP_CONNECTION_CREATE_SOCKET,            "Could create network connection to host [%1$s] on port [%2$s]" }
  , { ServiceError.LDAP_CONNECTION_ERROR,                    "Error encountered while connecting to Target System" }
  , { ServiceError.LDAP_CONNECTION_TIMEOUT,                  "Connection to Target System got timed out : [%1$s]" }
  , { ServiceError.LDAP_CONNECTION_NOT_AVAILABLE,            "The problem may be with physical connectivity or Target System is not alive" }
  , { ServiceError.LDAP_CONNECTION_SSL_HANDSHAKE,            "The SSL Certificate may not be generated for Target System or imported properly" }
  , { ServiceError.LDAP_CONNECTION_SSL_ERROR,                "Not able to invalidate SSL session." }
  , { ServiceError.LDAP_CONNECTION_SSL_DESELECTED,           "SSL option is not selected in IT Resource." }
  , { ServiceError.LDAP_CONNECTION_AUTHENTICATION,           "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { ServiceError.LDAP_CONNECTION_ENCODING_NOTSUPPORTED,    "URL Encoding [%1$s] not supported" }

    // 00201 - 00210 LDAP control errors
  , { ServiceError.LDAP_CONTROL_EXTENSION_EXISTS,            "Set of critical extensions are: " }
  , { ServiceError.LDAP_CONTROL_EXTENSION_NOT_EXISTS,        "Set of critical extensions absent" }
  , { ServiceError.LDAP_CONTROL_EXTENSION_SUPPORTED,         "Critical Extensions Supported." }
  , { ServiceError.LDAP_CONTROL_EXTENSION_NOT_SUPPORTED,     "Critical Extensions is not supported" }

    // 00211 - 00220 LDAP certificate errors
  , { ServiceError.LDAP_CERTIFICATE_FILE_NOT_FOUND,          "Certificate file [%1$s] is not available." }
  , { ServiceError.LDAP_CERTIFICATE_TYPE_NOT_AVAILABLE,      "Certificate type [%1$s] is not available in the default provider package or any of the other provider packages that were searched." }

    // 00221 - 00230 opss errors
  , { ServiceError.OPSS_CONTEXT_ERROR,                       "Error in getting context [%1$s]." }
  , { ServiceError.OPSS_CREDENTIALSTORE_ERROR,               "Error in accessing credentialstore [%2$s] in context [%1$s]." }
  , { ServiceError.OPSS_CREDENTIALSTORE_MISSING,             "Context [%1$s] is not associated with a credential store." }
  , { ServiceError.OPSS_CREDENTIALMAP_MISSING,               "Mapping [%2$s] does not exits in the credential store for context [%1$s]." }
  , { ServiceError.OPSS_CREDENTIALALIAS_MISSING,             "Alias [%3$s] does not exists in mapping [%2$s] of credential store for context [%1$s]." }
    
    // 00231 - 00240 filter errors
  , { ServiceError.FILTER_PROPERTY_FILE,                     "Property file [%1$s] could not be loaded. Reason %2$s" }
  , { ServiceError.FILTER_STYLESHEET_FILE,                   "Stylesheet file [%1$s] could not be loaded. Reason %2$s" }

     // 01001 - 01010 task related messages
  , { ServiceMessage.TASK_VALIDATING,                        "Validating task ..." }
  , { ServiceMessage.TASK_VALIDATED,                         "Task validated." }
  , { ServiceMessage.TASK_EXECUTING,                         "Executing task ..." }
  , { ServiceMessage.TASK_EXECUTED,                          "Task executed." }

     // 01011 - 01030 server connection related messages
  , { ServiceMessage.SERVER_CONNECTING,                      "Connecting to %1$s:%2$s" }
  , { ServiceMessage.SERVER_CONTEXT_CONNECTING,              "Connecting to %1$s" }
  , { ServiceMessage.SERVER_CONNECTED,                       "Connection established" }
  , { ServiceMessage.SERVER_DISCONNECTING,                   "Disconnecting from %1$s:%2$s" }
  , { ServiceMessage.SERVER_CONTEXT_DISCONNECTING,           "Disconnecting from %1$s" }
  , { ServiceMessage.SERVER_DISCONNECTED,                    "Connection closed" }

     // 01021 - 01030 server status related messages
  , { ServiceMessage.SERVER_CONTROL,                         "Operation %2$s on server %1$s invoked ..." }
  , { ServiceMessage.SERVER_CONTROL_COMPLETE,                "Operation %2$s on server %1$s completed successful" }
  , { ServiceMessage.SERVER_CONTROL_FAILED,                  "Operation %2$s on server %1$s completed with error" }
  , { ServiceMessage.SERVER_CONTROL_STATUS,                  "Server %1$s is in status %2$s" }

     // 01031 - 01040 server security related messages
  , { ServiceMessage.SECURITY_ENTITY_ROLE,                   "Role" }
  , { ServiceMessage.SECURITY_ENTITY_USER,                   "User" }
  , { ServiceMessage.SECURITY_ENTITY_ALIAS,                  "Alias" }
  , { ServiceMessage.SECURITY_ENTITY_CREDENTIAL,             "Credential" }
  , { ServiceMessage.SECURITY_ENTITY_POLICY,                 "Policy" }
  , { ServiceMessage.SECURITY_ENTITY_CODEBASE,               "Codebase" }
  , { ServiceMessage.SECURITY_ENTITY_PRINCIPAL,              "Principal" }
  , { ServiceMessage.SECURITY_ENTITY_PERMISSION,             "Permission" }

     // 01041 - 01050 managed bean operation related messages
  , { ServiceMessage.OPERATION_INVOKE,                       "Invoking operation %1$s"}
  , { ServiceMessage.OPERATION_INVOKE_SUCCESS,               "Operation %1$s completed successful"}
  , { ServiceMessage.OPERATION_INVOKE_ERROR,                 "Operation %1$s completed with error"}
  , { ServiceMessage.OPERATION_INVOKE_PAYLOAD,               "Operation %1$s sends payload %2$s"}

     // 01051 - 01070 object/entity operation related messages
  , { ServiceMessage.OPERATION_CREATE_BEGIN,                 "Creating %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_CREATE_SUCCESS,               "%1$s [%2$s] created"}
  , { ServiceMessage.OPERATION_CREATE_SKIPPED,               "Creating %1$s [%2$s] skipped"}
  , { ServiceMessage.OPERATION_DELETE_BEGIN,                 "Deleting %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_DELETE_SUCCESS,               "%1$s [%2$s] deleted"}
  , { ServiceMessage.OPERATION_DELETE_SKIPPED,               "Deleting %1$s [%2$s] skipped"}
  , { ServiceMessage.OPERATION_ENABLE_BEGIN,                 "Enabling %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_ENABLE_SUCCESS,               "%1$s [%2$s] enabled"}
  , { ServiceMessage.OPERATION_DISABLE_BEGIN,                "Disabling %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_DISABLE_SUCCESS,              "%1$s [%2$s] disabled"}
  , { ServiceMessage.OPERATION_MODIFY_BEGIN,                 "Modifying %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_MODIFY_SUCCESS,               "%1$s [%2$s] modified"}
  , { ServiceMessage.OPERATION_MODIFY_SKIPPED,               "Modifying %1$s [%2$s] skipped"}
  , { ServiceMessage.OPERATION_VALIDATE_BEGIN,               "Validating %1$s [%2$s] ..."}
  , { ServiceMessage.OPERATION_VALIDATE_SUCCESS,             "%1$s [%2$s] validated"}
  , { ServiceMessage.OPERATION_ASSIGN_BEGIN,                 "Assigning %1$s [%2$s] to %3$s [%4$s] ..."}
  , { ServiceMessage.OPERATION_ASSIGN_SUCCESS,               "%1$s [%2$s] to %3$s [%4$s] assigned"}
  , { ServiceMessage.OPERATION_REVOKE_BEGIN,                 "Revoking %1$s [%2$s] from %3$s [%4$s] ..."}
  , { ServiceMessage.OPERATION_REVOKE_SUCCESS,               "%1$s [%2$s] from %3$s [%4$s] revoked"}

     // 01071 - 01080 export documentation related messages
  , { ServiceMessage.DOCUMENT_CREATE,                        "Creating document for %1$s ..."}
  , { ServiceMessage.DOCUMENT_SUCCESS,                       "Document for %1$s created successful"}
  , { ServiceMessage.DOCUMENT_ERROR,                         "Document for %1$s completed with errors"}
  , { ServiceMessage.DOCUMENT_CONFIRMATION_TITLE,            "Export %1$s"}
  , { ServiceMessage.DOCUMENT_CONFIRMATION_MESSAGE,          "Export File %1$s already exists in\n%2$s.\n\nOverride the existing file?"}

     // 01081 - 01090 metadata connection related messages
  , { ServiceMessage.METADATA_INSTANCE_CREATE,               "Creating Metadata Instance..."}
  , { ServiceMessage.METADATA_INSTANCE_CREATED,              "Metadata Instance created"}
  , { ServiceMessage.METADATA_INSTANCE_CLOSE,                "Closing Metadata Instance..."}
  , { ServiceMessage.METADATA_INSTANCE_CLOSED,               "Metadata Instance closed"}
  , { ServiceMessage.METADATA_SESSION_CREATE,                "Creating Metadata Session..."}
  , { ServiceMessage.METADATA_SESSION_CREATED,               "Metadata Session created"}

     // 01091 - 01110 metadata documentation related messages
  , { ServiceMessage.METADATA_PATH_CREATE,                   "Metadata Object Path %1$s created in Metadata Store"}
  , { ServiceMessage.METADATA_PATH_EXISTS,                   "Metadata Object Path %1$s already exists in Metadata Store"}
  , { ServiceMessage.METADATA_PATH_NOTEXISTS,                "Metadata Object Path %1$s does not exists in Metadata Store"}
  , { ServiceMessage.METADATA_DOCUMENT_UPLOADED,             "%1$s: Metadata Object %2$s uploaded to Metadata Store"}
  , { ServiceMessage.METADATA_DOCUMENT_UPLOAD_DELETED,       "%1$s: Metadata Object %2$s deleted before uploading in to Metadata Store"}
  , { ServiceMessage.METADATA_DOCUMENT_DOWNLOADED,           "%1$s: Metadata Object %2$s downloaded from Metadata Store"}
  , { ServiceMessage.METADATA_DOCUMENT_REMOVED,              "%1$s: Metadata Object %2$s removed from Metadata Store"}
  , { ServiceMessage.METADATA_DOCUMENT_NOTEXISTS,            "Metadata Object %1$s does not exists within %2$s. Skip further processing"}
  , { ServiceMessage.METADATA_DOCUMENT_EMPTY,                "Metadata Object %1$s is empty. Skip further processing"}
  , { ServiceMessage.METADATA_DOCUMENT_OBJECT_MODE,          "Object Mode"}
  , { ServiceMessage.METADATA_DOCUMENT_DOCUMENT_MODE,        "Document Mode"}
  , { ServiceMessage.METADATA_DOCUMENT_STREAMED_MODE,        "Streamed Mode"}
  , { ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_TITLE,   "Write Metadata Object %1$s"}
  , { ServiceMessage.METADATA_DOCUMENT_CONFIRMATION_MESSAGE, "Metadata Object  File %1$s already exists in\n%2$s.\n\nOverride the existing file?"}

     // 01111 - 01120 http operations related messages
  , { ServiceMessage.HTTPSERVICE_LOCATION_MOVED,             "[%1$s] moved to [%2$s]."}
  , { ServiceMessage.HTTPSERVICE_LOCATION_MOVED_PERMANENT,   "[%1$s] permanently moved to [%2$s]."}
  , { ServiceMessage.HTTPSERVICE_DOWNLOAD_SKIPPED,           "[%1$s] not modified - hence not downloaded."}

     // 01121 - 01130 webservice operations related messages
  , { ServiceMessage.WEBSERVICE_RESTART,                     "Please restart application [%1$s] to uptake any policy or configuration change."}
  , { ServiceMessage.WEBSERVICE_SUBJECT_LOCATED,             "WebService Port Configuration %1$s located"}
  , { ServiceMessage.WEBSERVICE_POLICY_ATTACHED,             "Policy [%2$s] attached to WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_ALREADY_ATTACHED,     "Policy [%2$s] already attached on WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_DETACHED,             "Policy [%2$s] detached from WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_ALREADY_DETACHED,     "Policy [%2$s] already detached from WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_ENABLED,              "Policy [%2$s] enabled on WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_ALREADY_ENABLED,      "Policy [%2$s] already enabled on WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_DISABLED,             "Policy [%2$s] disabled on WebService Port Configiguration [%1$s]"}
  , { ServiceMessage.WEBSERVICE_POLICY_ALREADY_DISABLED,     "Policy [%2$s] already disabled on WebService Port Configiguration [%1$s]"}

     // 01131 - 01140 webservice reporting related messages
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_PREFIX,        "Policies for"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_ENTITY,        "Policy"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_CATEGORY,      "Category"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_STATUS,        "Secured"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_EMPTY,         "No match found"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_HEADER,        "-----------------------------------------------------------------------------------------------------"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_SEPARATOR,     "----------+-----------+------------------------------------------------------------------------------"}
  , { ServiceMessage.WEBSERVICE_POLICY_REPORT_FORMAT,        "%-10s| %-10s| %-80s"}

     // 01141 - 01150 fusion middleware reporting related messages
  , { ServiceMessage.FUSION_INVENTORY_ENTITY,                "Configuration"}
  , { ServiceMessage.FUSION_INVENTORY_EMPTY,                 "No match found"}
  , { ServiceMessage.FUSION_INVENTORY_SEPARATOR,             "----------------------------------------+------------------------------------------------------------"}
  , { ServiceMessage.FUSION_INVENTORY_FORMAT,                "%-40s| %-60s"}

     // 01151 - 01160 opss wallet related messages
   , { ServiceMessage.OPSS_CONTEXT_CREATE,                   "Creating Oracle Platform Security Context [%1$s] ..."}
   , { ServiceMessage.OPSS_CONTEXT_CREATED,                  "Oracle Platform Security Context [%1$s] created"}
   , { ServiceMessage.OPSS_CREDENTIALSTORE_OBTAIN,           "Obtain CredentialStore for [%1$s] ..."}
   , { ServiceMessage.OPSS_CREDENTIALSTORE_OBTAINED,         "CredentialStore obtained for [%1$s]"}
   , { ServiceMessage.OPSS_CREDENTIAL_OBTAIN,                "Obtain Credential for [%2$s] in [%1$s] ..."}
   , { ServiceMessage.OPSS_CREDENTIAL_OBTAINED,              "Credential obtained for [%2$s] in [%1$s]"}
};

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(ServiceResourceBundle.class.getName());

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

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s" occurrences in the string resource with the
   ** appropriate parameter.
   **
   ** @param  key                key into the resource array.
   ** @param  argument           the substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s" and "%2$s" occurrences in the string resource
   ** with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%1$s", "%2$s" and "%3$s" occurrences in the string
   ** resource with the appropriate parameter "n" from the parameters.
   **
   ** @param  key                key into the resource array.
   ** @param  argument1          the first substitution parameter.
   ** @param  argument2          the second substitution parameter.
   ** @param  argument3          the third substitution parameter.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                    the formatted String resource
   */
  public static String format(final String key, final Object... arguments) {
    return RESOURCE.formatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}