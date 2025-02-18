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

    File        :   ServiceBundle_en.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ServiceBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.rest;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle_en
// ~~~~~ ~~~~~~~~~~~~~~~~
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
public class ServiceBundle_en extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GWS-00001 - GWS-00010 General error (Undefined)
    { ServiceError.UNHANDLED,                         "Unhandled exception occured: %1$s" }

     // GWS-00011 - 00015 method argument related errors
  , { ServiceError.ARGUMENT_IS_NULL,                  "Passed argument [%1$s] must not be null." }
  , { ServiceError.ARGUMENT_BAD_TYPE,                 "Passed argument [%1$s] has a bad type." }
  , { ServiceError.ARGUMENT_BAD_VALUE,                "Passed argument [%1$s] contains an invalid value." }
  , { ServiceError.ARGUMENT_SIZE_MISMATCH,            "Passed argument array size don't match expected length." }

     // GWS-00021 - 00030 instance state related errors
  , { ServiceError.INSTANCE_ATTRIBUTE_IS_NULL,        "Invalid instance state: Attribute [%1$s] must be initialized." }
  , { ServiceError.INSTANCE_ILLEGAL_STATE,            "Invalide state of instance: Attribute [%1$s] already initialized."}

     // GWS-00031 - 00040 connectivity errors
  , { ServiceError.CONNECTION_UNKNOWN_HOST,           "Host [%1$s] is unknown." }
  , { ServiceError.CONNECTION_CREATE_SOCKET,          "Could create network connection to host [%1$s] on port [%2$s]." }
  , { ServiceError.CONNECTION_SECURE_SOCKET,          "Could create secure network connection to host [%1$s] on port [%2$s]." }
  , { ServiceError.CONNECTION_CERTIFICATE_HOST,       "Unable to find valid certification path to requested target [%1$s]." }
  , { ServiceError.CONNECTION_ERROR,                  "Error encountered while connecting to Service Provider." }
  , { ServiceError.CONNECTION_TIMEOUT,                "The network connection to host [%1$s] on port [%2$s] timed out." }
  , { ServiceError.CONNECTION_NOT_AVAILABLE,          "The problem may be with physical connectivity or Service Provider is not alive." }
  , { ServiceError.CONNECTION_AUTHENTICATION,         "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials." }
  , { ServiceError.CONNECTION_AUTHORIZATION,          "Principal Name [%1$s] is not authorized." }

     // GWS-00061 - 00070 request method errors
  , { ServiceError.REQUEST_METHOD_NOTALLOWED,         "Method not allowed." }
  , { ServiceError.REQUEST_METHOD_NOTIMPLEMENTED,     "Method not implemented." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED, "Method expects an id to operate." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID,  "Resource specifies ID [%s] in payload for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND,    "Resource with ID [%s] does not exists for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EXISTS,      "Resource with name [%s] already exists for resource type [%s]." }
  , { ServiceError.REQUEST_METHOD_ENTITY_EMPTY,       "Empty Entity." }
  , { ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE,   "Attribute [%s]:%s must not be null." }

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
  , { ServiceError.PARAMETER_START_INVALID_VALUE,     "[%s] is not a valid value for the parameter startIndex." }
  , { ServiceError.PARAMETER_COUNT_INVALID_VALUE,     "[%s] is not a valid value for the parameter count." }
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,      "[%s] is not a valid value for the parameter sortBy: [%s]." }
  , { ServiceError.PARAMETER_ORDER_INVALID_VALUE,     "[%s] is not a valid value for the parameter sortOrder: [%s]." }

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
  , { ServiceError.PROCESS_UNEXPECTED,                "The Service Provider encountered an unexpected condition that prevented it from fulfilling the request." }
  , { ServiceError.PROCESS_UNAVAILABLE,               "The Service Provider is unable to handle the request due to temporary overloading or maintenance of the service. The Service Provider REST API service is not currently running." }
  , { ServiceError.PROCESS_AUTHORIZATION,             "The request is not authorized. The authentication credentials included with this request are missing or invalid." }
  , { ServiceError.PROCESS_FORBIDDEN,                 "The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort." }
  , { ServiceError.PROCESS_NOT_ALLOWED,               "The HTTP method specified in the request is not supported for this request URI." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,            "The Service Provider cannot produce a response matching the list of acceptable values defined in the request's proactive content negotiation headers, and that the Service Provider is unwilling to supply a default representation." }
  , { ServiceError.PROCESS_EXISTS,                    "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,                "%s" }
  , { ServiceError.PROCESS_PRECONDITION,              "Failed to modify. Resource has changed on the Service Provider. Vendor Reason: \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,             "Failed to modify. Resource has not changed. Vendor Reason: \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,                 "The Service Provider is refusing to process a request because the request payload is larger than the server is willing or able to process." }
  , { ServiceError.PROCESS_INVALID_VERSION,           "The specified SCIM protocol version is not supported." }
  , { ServiceError.PROCESS_TOO_MANY,                  "The specified filter yields many more results than the server is willing to calculate or process. For example, a filter such as \"(userName pr)\" by itself would return all entries with a \"userName\" and MAY not be acceptable to the service provider." }
  , { ServiceError.PROCESS_MUTABILITY,                "The attempted modification is not compatible with the target attribute's mutability or current state (e.g., modification of an \"immutable\" attribute with an existing value)." }
  , { ServiceError.PROCESS_SENSITIVE,                 "The specified request cannot be completed, due to the passing of sensitive (e.g., personal) information in a request URI." }
  , { ServiceError.PROCESS_UNIQUENESS,                "One or more of the attribute values are already in use or are reserved." }
  , { ServiceError.PROCESS_NOTARGET,                  "The specified \"path\" did not yield an attribute or attribute value that could be operated on. This occurs when the specified \"path\" value contains a filter that yields no match." }
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
  , { ServiceError.HTTP_CODE_NOP,                     "HTTP-NOP: An unknown response status [%1$s] was received from the endpoint."}
  , { ServiceError.HTTP_CODE_400,                     "HTTP-400: The request could not be understood by the server due to malformed syntax. The client SHOULD NOT repeat the request without modifications."}
  , { ServiceError.HTTP_CODE_401,                     "HTTP-401: The request has not been completed because it lacks valid authentication credentials for the requested resource."}
  , { ServiceError.HTTP_CODE_403,                     "HTTP-403: The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort."}
  , { ServiceError.HTTP_CODE_404,                     "HTTP-404: The requested resource could not be found but may be available in the future. Subsequent requests by the client are permissible."}
  , { ServiceError.HTTP_CODE_408,                     "HTTP-408: The server timed out waiting for the request."}
  , { ServiceError.HTTP_CODE_409,                     "HTTP-409: The request could not be completed due to a conflict with the current state of the target resource."}
  , { ServiceError.HTTP_CODE_415,                     "HTTP-415: The server refused this request because the request entity is in a format not supported by the requested resource for the requested method."}
  , { ServiceError.HTTP_CODE_500,                     "HTTP-500: The server encountered an internal error or misconfiguration and was unable to complete your request."}
  , { ServiceError.HTTP_CODE_501,                     "HTTP-501: The server either does not recognize the request method, or it lacks the ability to fulfill the request. Usually this implies future availability (e.g., a new feature of a web-service API)."}
  , { ServiceError.HTTP_CODE_502,                     "HTTP-502: The server was acting as a gateway or proxy and received an invalid response from the upstream server."}
  , { ServiceError.HTTP_CODE_503,                     "HTTP 503: The server is currently unable to handle the request due to a temporary overload or scheduled maintenance, which will likely be alleviated after some delay."}
  , { ServiceError.HTTP_CODE_504,                     "HTTP 504: The server was acting as a gateway or proxy and did not receive a timely response from the upstream server."}

     // GWS-00151 - 00160 authorization errors
  , { ServiceError.OAUTH_FLOW_NOT_FINISH,             "Authorization is not finished and access token was not received. Call start() and then finish() to perform the authorization." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,            "Invalid \"state\" parameter. \"state\" used in the authorization request does not match to the \"state\" from the authorization response." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,           "Error requesting access token. Response status: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,          "Error refreshing an access token. Response status: %1$s." }

    // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,              " ... and more ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,              "%s on thread [%s]\n" }
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