/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AuthorizationBundle.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AuthorizationBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.platform.core.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class AuthorizationBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
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
public class AuthorizationBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // GWS-00001 - 00010 operations related errors
    { AuthorizationError.UNHANDLED,                     "Unhandled exception occured: %1$s" }
  , { AuthorizationError.GENERAL,                       "Encounter some problems: %1$s" }
  , { AuthorizationError.ABORT,                         "Execution aborted: %1$s" }
  , { AuthorizationError.NOTIMPLEMENTED,                "Feature is not yet implemented!" }

     // GWS-00011 - 00020 method argument related errors
  , { AuthorizationError.ARGUMENT_IS_NULL,              "Passed argument %1$s must not be null!" }
  , { AuthorizationError.ARGUMENT_BAD_TYPE,             "Passed argument %1$s has a bad type!" }
  , { AuthorizationError.ARGUMENT_BAD_VALUE,            "Passed argument %1$s contains an invalid value!" }
  , { AuthorizationError.ARGUMENT_SIZE_MISMATCH,        "Passed argument array size dont match expected length!" }

     // GWS-00031 - 00040 authorization errors
  , { AuthorizationError.OAUTH_FLOW_NOT_FINISH,         "Authorization is not finished and access token was not received. Call start() and then finish() to perform the authorization." }
  , { AuthorizationError.OAUTH_FLOW_WRONG_STATE,        "Invalid \"state\" parameter. \"state\" used in the authorization request does not match to the \"state\" from the authorization response." }
  , { AuthorizationError.OAUTH_FLOW_ACCESS_TOKEN,       "Error requesting access token. Response status: %1$s." }
  , { AuthorizationError.OAUTH_FLOW_REFRESH_TOKEN,      "Error refreshing an access token. Response status: %1$s." }

     // GWS-00041 - 00050 processing errors
  , { AuthorizationError.PROCESS_UNEXPECTED,            "The Service Provider encountered an unexpected condition that prevented it from fulfilling the request." }
  , { AuthorizationError.PROCESS_UNAVAILABLE,           "The Service Provider is unable to handle the request due to temporary overloading or maintenance of the service. The Service Provider REST API service is not currently running." }
  , { AuthorizationError.PROCESS_AUTHORIZATION,         "The request is not authorized. The authentication credentials included with this request are missing or invalid." }
  , { AuthorizationError.PROCESS_NOT_ACCEPTABLE,        "The Service Provider cannot produce a response matching the list of acceptable values defined in the request's proactive content negotiation headers, and that the Service Provider is unwilling to supply a default representation." }
  , { AuthorizationError.PROCESS_MEDIATYPE_UNSUPPORTED, "The Service Provider refuses to accept the request because the payload format is in an unsupported format." }
  , { AuthorizationError.PROCESS_EXISTS_NOT,            "%s" }
  , { AuthorizationError.PROCESS_INVALID_VALUE,         "%s" }

     // GWS-00051 - 00060 token errors
  , { AuthorizationError.TOKEN_PLAIN_OBJECT,            "Payload of plain JOSE object is not a valid JSON object." }
  , { AuthorizationError.TOKEN_PLAIN_HEADER,            "The plain token header must not be null" }
  , { AuthorizationError.TOKEN_PLAIN_PAYLOAD,           "The plain token payload must not be null" }
  , { AuthorizationError.TOKEN_PLAIN_ENCODE,            "Unexpected third encoded part in the plain token object" }
  , { AuthorizationError.TOKEN_SIGNED_HEADER,           "The signed token header must not be null" }
  , { AuthorizationError.TOKEN_SIGNED_PAYLOAD,          "The signed token payload must not be null" }
  , { AuthorizationError.TOKEN_SIGNED_SIGNATURE,        "The signed token signature must not be null" }
  , { AuthorizationError.TOKEN_SIGNED_ENCODE,           "Unexpected number of encoded subjects, must be three" }
  , { AuthorizationError.TOKEN_ENCRYPTED_ENCODE,        "Unexpected number of encoded subjects, must be five" }

     // GWS-00061 - 00070 token encryption/decryption errors
  , { AuthorizationError.CRYPTO_STATE_UNDEFINED,        "The JWE object must be in an encrypted or decrypted state" }
  , { AuthorizationError.CRYPTO_STATE_ENCRYPTED,        "The JWE object must be in an encrypted state" }
  , { AuthorizationError.CRYPTO_STATE_UNENCRYPTED,      "The JWE object must be in an unencrypted state" }
  , { AuthorizationError.CRYPTO_ENCRYPTION_METHOD,      "The [%1$s] encryption method is not supported by the JWE encrypter" }
  , { AuthorizationError.CRYPTO_ALGORITHM_UNSUPPORTED,  "The [%1$s] algorithm is not supported by the JWE encrypter" }
  , { AuthorizationError.CRYPTO_DECRYPTION_METHOD,      "The [%1$s] decryption method is not accepted by the JWE decrypter" }
  , { AuthorizationError.CRYPTO_DECRYPTION_ALGORITHM,   "The [%1$s] algorithm is not accepted by the JWE decrypter" }

     // GWS-00071 - 00090 token signing errors
  , { AuthorizationError.SIGNING_SIGNER_METHOD,         "The [%1$s] signing method is not supported by the JWS signer" }
  , { AuthorizationError.SIGNING_ALGORITHM_EXCEPTION,   "The algorithm is not supported by the JWS signer:\n%1$s" }
  , { AuthorizationError.SIGNING_ALGORITHM_UNSUPPORTED, "The [%1$s] algorithm is not supported by the JWS signer" }
  , { AuthorizationError.SIGNING_ALGORITHM_NOTACCEPTED, "The [%1$s] algorithm is not accepted by the JWS signer" }
  , { AuthorizationError.SIGNING_HMCA_UNSUPPORTED,      "Unsupported HMAC algorithm:\n%1$s" }
  , { AuthorizationError.SIGNING_HMCA_INVALIDTYPE,      "Unsupported HMAC algorithm, must be HS256, HS384 or HS512" }
  , { AuthorizationError.SIGNING_HMCA_INVALIDKEY,       "Invalid HMAC key:\n%1$s" }
  , { AuthorizationError.SIGNING_RSA_UNSUPPORTED,       "Unsupported RSASSA algorithm:\n%1$s" }
  , { AuthorizationError.SIGNING_RSA_INVALIDTYPE,       "Unsupported RSASSA algorithm, must be RS256, RS384 or RS512" }
  , { AuthorizationError.SIGNING_RSA_SIGNATURE,         "RSA signature exception:\n%1$s" }
  , { AuthorizationError.SIGNING_RSA_PRIVATEKEY,        "Invalid private RSA key:\n%1$s" }
  , { AuthorizationError.SIGNING_RSA_PUBLICKEY,         "Invalid public RSA key:\n%1$s" }
  , { AuthorizationError.SIGNING_STATE_UNDEFINED,       "The signature object must be in a signed or verified state" }
  , { AuthorizationError.SIGNING_STATE_UNSIGNED,        "The signature object must be in an unsigned state" }

     // GWS-00091 - 00100 token validation related errors
  , { AuthorizationError.TOKEN_EXPIRED,                 "Token expired!" }
  , { AuthorizationError.TOKEN_NOTBEFORE,               "Token violates not-before rule!" }
  , { AuthorizationError.TOKEN_SIGNATURE,               "Token signature invalid!" }

     // GWS-01001 - 01010 logging related messages
  , { AuthorizationMessage.LOGGER_ELIPSE_MORE,          " ... and more ..." }
  , { AuthorizationMessage.LOGGER_THREAD_NAME,          "%s on thread %s\n" }
  , { AuthorizationMessage.LOGGER_CLIENT_REQUEST,       "Sending client request" }
  , { AuthorizationMessage.LOGGER_CLIENT_RESPONSE,      "Client response received" }
  , { AuthorizationMessage.LOGGER_SERVER_REQUEST,       "Server has received a request" }
  , { AuthorizationMessage.LOGGER_SERVER_RESPONSE,      "Server responded with a response" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    AuthorizationBundle.class.getName()
  , Locale.getDefault()
  , AuthorizationBundle.class.getClassLoader()
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the String resource
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                the key for the desired string pattern.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution arguments.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the formatted String resource
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final String key, final Object... arguments) {
    int count = arguments == null ? 0 : arguments.length;
    if (count == 0)
      return string(key);

    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    final String result = String.format(RESOURCE.getObject(key).toString(), arguments);
    for (int i = 0; i < count; ++i) {
      if (arguments[i] == "(null)")
        arguments[i] = null;
    }
    return result;
  }
}