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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   ServiceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.system.simulation.ServiceError;
import oracle.iam.system.simulation.ServiceMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServiceBundle
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>language code common
 **   <li>region   code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // OCS-00001 - 00010 General error (Undefined)
    { ServiceError.UNHANDLED,                         "Unhandled exception occured: [%1$s]" }

     // OCS-00011 - 00015 method argument related errors
  , { ServiceError.ARGUMENT_IS_NULL,                  "Passed argument [%1$s] must not be null" }
  , { ServiceError.ARGUMENT_BAD_TYPE,                 "Passed argument [%1$s] has a bad type" }
  , { ServiceError.ARGUMENT_BAD_VALUE,                "Passed argument [%1$s] contains an invalid value" }
  , { ServiceError.ARGUMENT_SIZE_MISMATCH,            "Passed argument array size don't match expected length" }

     // OCS-00021 - 00030 instance state related errors
  , { ServiceError.INSTANCE_ATTRIBUTE_IS_NULL,        "Invalid instance state: Attribute %1$s must be initialized" }
  , { ServiceError.INSTANCE_ILLEGAL_STATE,            "Invalide state of instance: Attribute %1$s already initialized"}

     // OCS-00031 - 00040 connectivity errors
  , { ServiceError.CONNECTION_UNKNOWN_HOST,             "Host %1$s is unknown" }
  , { ServiceError.CONNECTION_CREATE_SOCKET,            "Could create network connection to host [%1$s] on port [%2$s]" }
  , { ServiceError.CONNECTION_SECURE_SOCKET,            "Could create secure network connection to host [%1$s] on port [%2$s]" }
  , { ServiceError.CONNECTION_CERTIFICATE_HOST,         "Unable to find valid certification path to requested target [%1$s]" }
  , { ServiceError.CONNECTION_ERROR,                    "Error encountered while connecting to Service Provider" }
  , { ServiceError.CONNECTION_TIMEOUT,                  "Connection to Service Provider got timed out; [%1$s]" }
  , { ServiceError.CONNECTION_NOT_AVAILABLE,            "The problem may be with physical connectivity or Service Provider is not alive" }
  , { ServiceError.CONNECTION_AUTHENTICATION,           "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { ServiceError.CONNECTION_AUTHORIZATION,            "Principal Name [%1$s] is not authorized" }

     // OCS-00041 - 00050 operational state errors
  , { ServiceError.UNSUPPORTED,                       "Connector unwilling to perform: [%1$s] of type [%2$s] is not supported" }
  , { ServiceError.OBJECT_CLASS_MISSING,              "Connector unwilling to perform: ObjectClass is missing for operation" }
  , { ServiceError.UNIQUE_IDENTIFIER_MISSING,         "Connector unwilling to perform: Unique Identifier is missing for operation" }
  , { ServiceError.OBJECT_NOT_CREATED,                "Cannot create [%1$s] [%2$s]" }
  , { ServiceError.OBJECT_NOT_DELETED,                "Cannot delete [%1$s] [%2$s]" }
  , { ServiceError.OBJECT_ALREADY_EXISTS,             "[%1$s] already exists for [%2$s]" }
  , { ServiceError.OBJECT_NOT_EXISTS,                 "[%1$s] does not exists for [%2$s]" }
  , { ServiceError.ATTRIBUTE_EMPTY,                   "Attribute set passed is empty" }
  , { ServiceError.ATTRIBUTE_NAME_EMPTY,              "Connector unwilling to perform: [%1$s] attribute required for operation" }

     // OCS-00051 - 00060 marshalling errors
  , { ServiceError.PATH_UNEXPECTED_EOF_STRING,        "Unexpected end of path string" }
  , { ServiceError.PATH_UNEXPECTED_EOF_FILTER,        "Unexpected end of filter string" }
  , { ServiceError.PATH_UNEXPECTED_CHARACTER,         "Unexpected character '%s' at position %d for token starting at %d" }
  , { ServiceError.PATH_UNEXPECTED_TOKEN,             "Unexpected token '%s'" }
  , { ServiceError.PATH_UNRECOGNOIZED_OPERATOR,       "Unrecognized attribute operator '%s' at position %d. Expected: eq,ne,co,sw,ew,pr,gt,ge,lt,le" }
  , { ServiceError.PATH_INVALID_FILTER,               "Invalid value filter: %s" }
  , { ServiceError.PATH_EXPECT_PARENTHESIS,           "Expected '(' at position %d" }
  , { ServiceError.PATH_INVALID_PARENTHESIS,          "No opening parenthesis matching closing parenthesis at position %d" }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_PATH,        "Attribute path expected at position %d" }
  , { ServiceError.PATH_EXPECT_ATTRIBUTE_NAME,        "Attribute name expected at position %d" }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_PATH,       "Invalid attribute path at position %d: %s" }
  , { ServiceError.PATH_INVALID_ATTRIBUTE_NAME,       "Invalid attribute name starting at position %d: %s" }
  , { ServiceError.PATH_INVALID_COMPARISON_VALUE,     "Invalid comparison value at position %d: %s" }
  , { ServiceError.PATH_INVALID_VALUE_DEPTH,          "Path can not target sub-attributes more than one level deep" }
  , { ServiceError.PATH_INVALID_VALUE_FILTER,         "Path can not include a value filter on sub-attributes" }
  , { ServiceError.PATH_INVALID_VALUE_ENCODING,       "Value at path %s is not a valid %s string" }

     // OCS-00061 - 00070 request method errors
  , { ServiceError.REQUEST_METHOD_NOTALLOWED,         "Method not allowed" }
  , { ServiceError.REQUEST_METHOD_NOTIMPLEMENTED,     "Method not implemented" }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_REQUIRED, "Method expects an id to operate" }
  , { ServiceError.REQUEST_METHOD_ENTITY_ID_INVALID,  "Resource specifies ID %s in payload for resource type %s" }
  , { ServiceError.REQUEST_METHOD_ENTITY_NOTFOUND,    "Resource with ID %s does not exists for resource type %s" }
  , { ServiceError.REQUEST_METHOD_ENTITY_EXISTS,      "Resource with name %s already exists for resource type %s" }
  , { ServiceError.REQUEST_METHOD_ENTITY_EMPTY,       "Empty Entity" }
  , { ServiceError.REQUEST_METHOD_ENTITY_ATTRIBUTE,   "Attribute %s:%s must not be null" }

     // OCS-00081 - 00090 request parameter errors
  , { ServiceError.PARAMETER_FILTER_NOTPERMITTED,     "Filtering not allowed" }
  , { ServiceError.PARAMETER_SORT_INVALID_VALUE,      "''%s'' is not a valid value for the sortBy parameter: %s" }

     // OCS-00091 - 00100 filtering errors
  , { ServiceError.FILTER_INCONSISTENT_METHOD,         "Translation method is inconsistent: %s" }
  , { ServiceError.FILTER_INCONSISTENT_EXPRESSION,     "Translation expresssion is inconsistent: %s" }

     // OCS-00101 - 00110 patch operation errors
  , { ServiceError.PATCH_OPERATIONTYPE_UNKNOWN,       "Unkown patch operation type %s" }
  , { ServiceError.PATCH_MULTIVALUE_NOTPERMITTED,     "Patch operation contains multiple values" }

     // OCS-00111 - 00130 http status related errors
  , { ServiceError.HTTP_RESPONSE_CODE_NOP,            "HTTP-NOP: An unknown response status [%1$s] was received from the endpoint."}
  , { ServiceError.HTTP_RESPONSE_CODE_400,            "HTTP-400: The request could not be understood by the server due to malformed syntax. The client SHOULD NOT repeat the request without modifications."}
  , { ServiceError.HTTP_RESPONSE_CODE_401,            "HTTP-401: Authorization failure."}
  , { ServiceError.HTTP_RESPONSE_CODE_403,            "HTTP-403: The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource, or may need an account of some sort."}
  , { ServiceError.HTTP_RESPONSE_CODE_404,            "HTTP-404: The requested resource [%1$s] could not be found but may be available in the future. Subsequent requests by the client are permissible."}
  , { ServiceError.HTTP_RESPONSE_CODE_408,            "HTTP-408: The server timed out waiting for the request."}
  , { ServiceError.HTTP_RESPONSE_CODE_500,            "HTTP-500: A generic error message, given when an unexpected condition was encountered and no more specific message is suitable."}
  , { ServiceError.HTTP_RESPONSE_CODE_501,            "HTTP-501: The server either does not recognize the request method, or it lacks the ability to fulfill the request. Usually this implies future availability (e.g., a new feature of a web-service API)."}
  , { ServiceError.HTTP_RESPONSE_CODE_502,            "HTTP-502: The server was acting as a gateway or proxy and received an invalid response from the upstream server."}
  , { ServiceError.HTTP_RESPONSE_CODE_503,            "HTTP 503: The server either does not recognize the request method, or it lacks the ability to fulfill the request. Usually this implies future availability (e.g., a new feature of a web-service API)."}
  , { ServiceError.HTTP_RESPONSE_CODE_504,            "HTTP 504: The server was acting as a gateway or proxy and did not receive a timely response from the upstream server."}

     // OCS-01011 - 01020 command line status messages
  , { ServiceMessage.CATEGORY_LABEL,                  "Category"}
  , { ServiceMessage.PROPERTY_LABEL,                  "Property"}
  , { ServiceMessage.PROPERTY_VALUE,                  "Value"}
  , { ServiceMessage.DATABASE_CATEGORY,               "Database"}
  , { ServiceMessage.DATABASE_HOSTNAME,               "Hostname"}
  , { ServiceMessage.DATABASE_LISTENER,               "Port Number"}
  , { ServiceMessage.DATABASE_SERVICE,                "Service Name"}
  , { ServiceMessage.DATABASE_USERNAME,               "Principal"}
  , { ServiceMessage.SERVICES_CATEGORY,               "Services"}
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ServiceBundle.class.getName()
  , Locale.getDefault()
  , ServiceBundle.class.getClassLoader()
  );

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
  // Method:   string
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%n$s" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                    the formatted String resource
   */
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }
}