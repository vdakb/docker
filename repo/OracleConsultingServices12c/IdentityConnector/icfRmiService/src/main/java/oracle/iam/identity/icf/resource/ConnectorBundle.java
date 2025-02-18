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

    File        :   ConnectorBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ConnectorBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class ConnectorBundle
// ~~~~~ ~~~~~~~~~~~~~~~
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
public class ConnectorBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    { Connector.Endpoint.SERVER_HOST_LABEL,           "Server Host" }
  , { Connector.Endpoint.SERVER_HOST_HINT,            "The name or IP address of the host where the Service Provider is running." }
  , { Connector.Endpoint.SERVER_PORT_LABEL,           "Server Port" }
  , { Connector.Endpoint.SERVER_PORT_HINT,            "The TCP/IP port number used to communicate with the Service Provider." }
  , { Connector.Endpoint.SERVER_NAME_LABEL,           "Server Name" }
  , { Connector.Endpoint.SERVER_NAME_HINT,            "The name of the Manager Server used to communicate with the Service Provider." }
  , { Connector.Endpoint.SERVER_TYPE_LABEL,           "Server Type" }
  , { Connector.Endpoint.SERVER_TYPE_HINT,            "The type of the Manager Server used to communicate with the Service Provider." }
  , { Connector.Endpoint.SECURE_SOCKET_LABEL,         "Secure Socket" }
  , { Connector.Endpoint.SECURE_SOCKET_HINT,          "Secure Socket (SSL/TLS) is a standard security technology for establishing an encrypted link between a server and a client - typically a web server (website) and a browser, or a mail server and a mail client (e.g., Outlook).\nSSL/TLS allows sensitive information such as credit card numbers, social security numbers, and login credentials to be transmitted securely." }
  , { Connector.Endpoint.ROOT_CONTEXT_LABEL,          "Root Context" }
  , { Connector.Endpoint.ROOT_CONTEXT_HINT,           "Root Context" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_LABEL,    "Principal Name" }
  , { Connector.Endpoint.PRINCIPAL_USERNAME_HINT,     "Username of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_LABEL,    "Principal Password" }
  , { Connector.Endpoint.PRINCIPAL_PASSWORD_HINT,     "Credential of the principal to authenticate at the service provider." }
  , { Connector.Endpoint.DOMAIN_USERNAME_LABEL,       "Domain Principal" }
  , { Connector.Endpoint.DOMAIN_USERNAME_HINT,        "Username of the domain administrator principal." }
  , { Connector.Endpoint.DOMAIN_PASSWORD_LABEL,       "Domain Password" }
  , { Connector.Endpoint.DOMAIN_PASSWORD_HINT,        "Credential of the domain administrator principal." }
  , { Connector.Endpoint.LOGIN_CONFIG_LABEL,          "Login Config" }
  , { Connector.Endpoint.LOGIN_CONFIG_HINT,           "Login Config" }
  , { Connector.Endpoint.COUNTRY_LABEL,               "Locale Country"}
  , { Connector.Endpoint.COUNTRY_HINT,                "Locale Country"}
  , { Connector.Endpoint.LANGUAGE_LABEL,              "Locale Language"}
  , { Connector.Endpoint.LANGUAGE_HINT,               "Locale Language"}
  , { Connector.Endpoint.TIMEZONE_LABEL,              "Locale TimeZone"}
  , { Connector.Endpoint.TIMEZONE_HINT,               "Locale TimeZone"}

  , { Connector.Connection.CONNECT_TIMEOUT_LABEL,     "Connection Timeout" }
  , { Connector.Connection.CONNECT_TIMEOUT_HINT,      "Connection Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "Response Timeout" }
  , { Connector.Connection.RESPONSE_TIMEOUT_LABEL,    "The timeout period the Service Client doesn't get a response.\\nWhen an service request is made by a client to a server and the server does not respond for some reason, the client waits forever for the server to respond until the TCP timeouts. On the client-side what the user experiences is esentially a process hang. In order to control the service request in a timely manner, a read timeout can be configured for the service provider.\\nIf this property is not specified, the default is to wait for the response until it is received." }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ConnectorBundle.class.getName()
  , Locale.getDefault()
  , ConnectorBundle.class.getClassLoader()
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
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.stringFormat(key, arguments);
  }
}