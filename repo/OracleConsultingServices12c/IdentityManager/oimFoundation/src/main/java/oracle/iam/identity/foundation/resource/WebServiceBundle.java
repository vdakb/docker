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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   WebServiceBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.soap.WebServiceError;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceBundle
// ~~~~~ ~~~~~~~~~~~~~~~~
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
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
public class WebServiceBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // SOA-00001 - 00010 General error (Undefined)
    { WebServiceError.UNHANDLED,                        "Unhandled exception occured: %1$s" }
  , { WebServiceError.GENERAL,                          "Encounter some problems: %1$s" }
  , { WebServiceError.ABORT,                            "Execution aborted: %1$s"}
  , { WebServiceError.NOTIMPLEMENTED,                   "Feature is not yet implemented"}

     // SOA-WebServiceError - 00012 method argument related errors
  , { WebServiceError.ARGUMENT_IS_NULL,                 "Passed argument [%1$s] must not be null" }

     // SOA-00021 - 00030 instance state related errors
  , { WebServiceError.INSTANCE_ATTRIBUTE_IS_NULL,       "Invalid instance state: Attribute [%1$s] must be initialized" }

     // SOA-00061 - 00080 connectivity errors
  , { WebServiceError.CONNECTION_UNKNOWN_HOST,          "Host %1$s is unknown" }
  , { WebServiceError.CONNECTION_CREATE_SOCKET,         "Could create network connection to host [%1$s] on port [%2$s]" }
  , { WebServiceError.CONNECTION_ERROR,                 "Error encountered while connecting to Target System" }
  , { WebServiceError.CONNECTION_TIMEOUT,               "Connection to Target System got timed out : [%1$s]" }
  , { WebServiceError.CONNECTION_UNAVAILABLE,           "%1$s\nThe problem may be with physical connectivity or Target System is not alive" }
  , { WebServiceError.CONNECTION_SSL_HANDSHAKE,         "The SSL Certificate may not be generated for Target System or imported properly" }
  , { WebServiceError.CONNECTION_SSL_ERROR,             "Not able to invalidate SSL session." }
  , { WebServiceError.CONNECTION_SSL_DESELECTED,        "SSL option is not selected in IT Resource." }
  , { WebServiceError.CONNECTION_AUTHENTICATION,        "Principal Name [%1$s] or Password is incorrect\nSystem failed to get access with supplied credentials" }
  , { WebServiceError.CONNECTION_AUTHORIZATION,         "Principal Name [%1$s] access denied" }
  , { WebServiceError.CONNECTION_ENCODING_NOTSUPPORTED, "URL Encoding [%1$s] not supported" }

     // SOA-00081 - 00090 connectivity errors
  , { WebServiceError.SERVICE_AUTHENTICATION,           "Principal Name [%1$s] or Password is incorrect, service failed to authenticate with supplied credentials" }
  , { WebServiceError.SERVICE_AUTHORIZATION,            "Principal Name [%1$s] access denied" }
  , { WebServiceError.SERVICE_UNAVAILABLE,              "Requested Service is not available" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    WebServiceBundle.class.getName()
  , Locale.getDefault()
  , WebServiceBundle.class.getClassLoader()
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
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for <code>%1$s</code>.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for <code>%1$s</code>.
   ** @param  argument2           the subsitution value for <code>%2$s</code>.
   ** @param  argument3           the subsitution value for <code>%3$s</code>.
   **
   ** @return                     the formatted String resource
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
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object[] arguments) {
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