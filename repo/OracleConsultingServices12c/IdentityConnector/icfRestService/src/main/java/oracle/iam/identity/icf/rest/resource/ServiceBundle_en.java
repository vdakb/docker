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
    Subsystem   :   Generic REST Library

    File        :   ServiceBundle_en.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle_en.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.rest.resource;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.rest.ServiceError;
import oracle.iam.identity.icf.rest.ServiceMessage;

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
     // GWS-00031 - 00040 authorization errors
    { ServiceError.OAUTH_FLOW_NOT_FINISH,          "Authorisation is not finished and access token was not received. Call start() and then finish() to perform the authorisation." }
  , { ServiceError.OAUTH_FLOW_WRONG_STATE,         "Invalid \"state\" parameter. \"state\" used in the authorisation request does not match to the \"state\" from the authorisation response." }
  , { ServiceError.OAUTH_FLOW_ACCESS_TOKEN,        "Error requesting access token. Response status: %1$s." }
  , { ServiceError.OAUTH_FLOW_REFRESH_TOKEN,       "Error refreshing an access token. Response status: %1$s." }

     // GWS-00061 - 00080 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,     "Unexpected end of path string." }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,     "Unexpected end of filter string." }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,      "Unexpected character '%s' at position %d for token starting at %d." }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,          "Unexpected token '%s'." }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,    "Unrecognized attribute operator '%s' at position %d. Expected: eq,ne,co,sw,ew,pr,gt,ge,lt,le." }
  , { ServiceError.PATH_INVALID_FILTER,            "Invalid value filter: %s." }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,        "Expected '(' at position %d." }
  , { ServiceError.PATH_INVALID_PARENTHESIS,       "No opening parenthesis matching closing parenthesis at position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,     "Attribute path expected at position %d." }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,     "Attribute name expected at position %d." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,    "Invalid attribute path at position %d: %s." }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,    "Invalid attribute name starting at position %d: %s." }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,  "Invalid comparison value at position %d: %s" }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,       "Path can not target sub-attributes more than one level deep." }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,      "Path can not include a value filter on sub-attributes." }

     // GWS-00081 - 00090 request parameter errors
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,   "'%s' is not a valid value for the sortBy parameter: %s." }

    // GWS-00091 - 00100 filtering errors
  , { ServiceError.FILTER_METHOD_INCONSISTENT,     "Translation method is inconsistent: %s" }
  , { ServiceError.FILTER_EXPRESSION_INCONSISTENT, "Translation expresssion is inconsistent: %s" }
  , { ServiceError.FILTER_USAGE_INVALID_GE,        "Greater than or equal filter may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_USAGE_INVALID_GT,        "Greater than filter may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_USAGE_INVALID_LE,        "Less than or equal filter may not compare boolean or binary attribute values." }
  , { ServiceError.FILTER_USAGE_INVALID_LT,        "Less than filter may not compare boolean or binary attribute values." }

     // GWS-00101 - 00120 processing errors
  , { ServiceError.PROCESS_UNEXPECTED,             "The Service Provider encountered an unexpected condition that prevented it from fulfilling the request." }
  , { ServiceError.PROCESS_UNAVAILABLE,            "The Service Provider is unable to handle the request due to temporary overloading or maintenance of the service. The Service Provider REST API service is not currently running." }
  , { ServiceError.PROCESS_AUTHORIZATION,          "The request is not authorized. The authentication credentials included with this request are missing or invalid." }
  , { ServiceError.PROCESS_FORBIDDEN,              "The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort." }
  , { ServiceError.PROCESS_NOT_ALLOWED,            "The HTTP method specified in the request is not supported for this request URI." }
  , { ServiceError.PROCESS_NOT_ACCEPTABLE,         "The Service Provider cannot produce a response matching the list of acceptable values defined in the request's proactive content negotiation headers, and that the Service Provider is unwilling to supply a default representation." }
  , { ServiceError.PROCESS_MEDIATYPE_UNSUPPORTED,  "The Service Provider refuses to accept the request because the payload format is in an unsupported format." }
  , { ServiceError.PROCESS_EXISTS,                 "%s" }
  , { ServiceError.PROCESS_EXISTS_NOT,             "%s" }
  , { ServiceError.PROCESS_PRECONDITION,           "Failed to modify. Resource has changed on the Service Provider. Vendor Reason: \"%s\"" }
  , { ServiceError.PROCESS_POSTCONDITION,          "Failed to modify. Resource has not changed. Vendor Reason: \"%s\"" }
  , { ServiceError.PROCESS_TOO_LARGE,              "The Service Provider is refusing to process a request because the request payload is larger than the server is willing or able to process." }
  , { ServiceError.PROCESS_INVALID_VERSION,        "The specified SCIM protocol version is not supported." }
  , { ServiceError.PROCESS_TOO_MANY,               "The specified filter yields many more results than the server is willing to calculate or process. For example, a filter such as \"(userName pr)\" by itself would return all entries with a \"userName\" and MAY not be acceptable to the service provider." }
  , { ServiceError.PROCESS_MUTABILITY,             "The attempted modification is not compatible with the target attribute's mutability or current state (e.g., modification of an \"immutable\" attribute with an existing value)." }
  , { ServiceError.PROCESS_SENSITIVE,              "The specified request cannot be completed, due to the passing of sensitive (e.g., personal) information in a request URI." }
  , { ServiceError.PROCESS_UNIQUENESS,             "One or more of the attribute values are already in use or are reserved." }
  , { ServiceError.PROCESS_NOTARGET,               "The specified \"path\" did not yield an attribute or attribute value that could be operated on. This occurs when the specified \"path\" value contains a filter that yields no match." }
  , { ServiceError.PROCESS_INVALID_FILTER,         "%s" }
  , { ServiceError.PROCESS_INVALID_PATH,           "%s" }
  , { ServiceError.PROCESS_INVALID_SYNTAX,         "%s" }
  , { ServiceError.PROCESS_INVALID_VALUE,          "%s" }

    // GWS-00131 - 00140 object errors
  , { ServiceError.OBJECT_NOT_EXISTS,              "%1$s is not mapped with [%3$s] for [%2$s] at the Service Provider." }
  , { ServiceError.OBJECT_AMBIGUOUS,               "%1$s is ambiguously mapped with [%3$s] for [%2$s] at the Service Provider." }

    // GWS-01001 - 01010 logging related messages
  , { ServiceMessage.LOGGER_ELIPSE_MORE,           " ... and more ..." }
  , { ServiceMessage.LOGGER_THREAD_NAME,           "%s on thread %s\n" }
  , { ServiceMessage.LOGGER_CLIENT_REQUEST,        "Sending client request" }
  , { ServiceMessage.LOGGER_CLIENT_RESPONSE,       "Client response received" }
  , { ServiceMessage.LOGGER_SERVER_REQUEST,        "Server has received a request" }
  , { ServiceMessage.LOGGER_SERVER_RESPONSE,       "Server responded with a response" }

    // GWS-01011 - 01020 connection messages
  , { ServiceMessage.CONNECTING_BEGIN,             "Connecting to context URL [%1$s] with user [%2$s]..." }
  , { ServiceMessage.CONNECTING_SUCCESS,           "Connection to context URL [%1$s] for user [%2$s] established." }
  , { ServiceMessage.OPERATION_BEGIN,              "Operation [%1$s] on context [%2$s] with user [%3$s] starts..." }
  , { ServiceMessage.OPERATION_SUCCESS,            "Operation [%1$s] on context [%2$s] with user [%3$s] succeeded." }
  , { ServiceMessage.AUTHENTICATION_BEGIN,         "Authenticating account [%2$s] on context URL [%1$s]..." }
  , { ServiceMessage.AUTHENTICATION_SUCCESS,       "Account [%2$s] on context URL [%1$s] authenticated." }

     // GWS-01021 - 01030 reconciliation process related messages
  , { ServiceMessage.NOTHING_TO_CHANGE,            "No Records available to be fetched from Service Provider." }

     // GWS-01031 - 01040 provisioning process related messages
  , { ServiceMessage.UNSPECIFIED_ERROR,            "Unspecified error details in response" }
  , { ServiceMessage.STATUS_NOT_PROVIDED,          "Status indicator not provided; set as activated." }
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