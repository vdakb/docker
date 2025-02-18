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

    File        :   ServerBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServerBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-06-21  DSteding    First release version
                                         fix several issues and add new ones
*/

package oracle.iam.identity.icf.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.iam.identity.icf.foundation.resource.ListResourceBundle;

import oracle.iam.identity.icf.jes.ServerError;
import oracle.iam.identity.icf.jes.ServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class ServerBundle
// ~~~~~ ~~~~~~~~~~~~
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
public class ServerBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // JES-00001 - 00010 connection related errors
    { ServerError.CONNECTION_SERVERTYPE_UNSUPPORTED, "The Server Type [%1$s] configured for the IT resource is not supported!" }
  , { ServerError.CONNECTION_LOGINCONFIG_NOTFOUND,   "The configuration of the login module was not found at path [%1$s]!" }
  , { ServerError.CONTEXT_ACCESS_DENIED,             "Access denied to perform operation in the context of logged in user" }

    // JES-00011 - 00020 filtering errors
  , { ServerError.FILTER_METHOD_INCONSISTENT,       "Translation method is inconsistent: %s" }
  , { ServerError.FILTER_EXPRESSION_INCONSISTENT,   "Translation expresssion is inconsistent: %s" }
  , { ServerError.FILTER_USAGE_INVALID_GE,          "Greater than or equal filter may not compare boolean or binary attribute values." }
  , { ServerError.FILTER_USAGE_INVALID_GT,          "Greater than filter may not compare boolean or binary attribute values." }
  , { ServerError.FILTER_USAGE_INVALID_LE,          "Less than or equal filter may not compare boolean or binary attribute values." }
  , { ServerError.FILTER_USAGE_INVALID_LT,          "Less than filter may not compare boolean or binary attribute values." }
 
    // JES-00021 - 00030 processing errors
  , { ServerError.PROCESS_EXISTS,                   "%s" }
  , { ServerError.PROCESS_NOT_EXISTS,               "%s" }
  , { ServerError.PROCESS_INVALID_FILTER,           "%s" }

    // JES-00031 - 0040 object errors
  , { ServerError.OBJECT_NOT_EXISTS,                "%1$s with %2$s [%3$s] does not exists at the Service Provider." }
  , { ServerError.OBJECT_AMBIGUOUS,                 "%1$s with %2$s [%3$s] is ambiguously defined at the Service Provider." }

    // JES-01001 - 01020 system related messages
  , { ServerMessage.CONFIGURING,                     "Configuring environment for service URL [%1$s] ..." }
  , { ServerMessage.CONFIGURED,                      "Environment for service URL [%1$s] configured." }
  , { ServerMessage.CONNECTING,                      "Connecting to service URL [%1$s] ..." }
  , { ServerMessage.CONNECTED,                       "Connection established to service URL [%1$s]." }
  , { ServerMessage.CONNECTION_ALIVE,                "Connection to service URL [%1$s] is alive." }
  , { ServerMessage.LOGGINGIN,                       "Log in into service URL [%1$s] as [%2$s] ..." }
  , { ServerMessage.LOGGEDIN,                        "[%2$s] was logged in to the service URL [%1$s]." }
  , { ServerMessage.LOGGINGOUT,                      "Logging out [%2$s] from service URL [%1$s] ..." }
  , { ServerMessage.LOGGEDOUT,                       "[%2$s] logged out from service URL [%1$s]." }
  , { ServerMessage.DISCONNECTING,                   "Close connection to service URL [%1$s] ..." }
  , { ServerMessage.DISCONNECTED,                    "Connection to service URL [%1$s] closed." }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ServerBundle.class.getName()
  , Locale.getDefault()
  , ServerBundle.class.getClassLoader()
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
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.stringFormat(key, arguments);
  }
}