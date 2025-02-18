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

    File        :   IdentityServerBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityServerBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.rmi.IdentityServerError;
import oracle.iam.identity.foundation.rmi.IdentityServerMessage;

////////////////////////////////////////////////////////////////////////////////
// class IdentityServerBundle
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
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
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class IdentityServerBundle extends ListResourceBundle  {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // RMI-00001 - 00010 operations related errors
    { IdentityServerError.UNHANDLED,                       "Unhandled exception occured: %1$s" }
  , { IdentityServerError.GENERAL,                         "Encounter some problems: %1$s" }
  , { IdentityServerError.ABORT,                           "Execution aborted: %1$s"}
  , { IdentityServerError.NOTIMPLEMENTED,                  "Feature is not yet implemented"}

     // RMI-00011 - 00020 context related errors
  , { IdentityServerError.CONTEXT_SERVERTYPE_NOTSUPPORTED, "Server Type [%1$s] not supported" }
  , { IdentityServerError.CONTEXT_ENCODING_NOTSUPPORTED,   "URL Encoding [%1$s] not supported" }
  , { IdentityServerError.CONTEXT_CONNECTION_ERROR,        "Unable to establish connection. Reason:\n%1$s"}
  , { IdentityServerError.CONTEXT_AUTHENTICATION,          "Principal Name [%1$s] or Password is incorrect, system failed to get access with supplied credentials" }
  , { IdentityServerError.CONTEXT_ACCESS_DENIED,           "Principal [%1$s] has insufficient privileges to perform operation [%2$s]" }

     // RMI-00021 - 00030 identity related errors
  , { IdentityServerError.IDENTITY_NOT_EXISTS,             "Identity [%1$s] does not exists" }
  , { IdentityServerError.IDENTITY_AMBIGUOUS,              "Identity [%1$s] defined ambiguously" }
  , { IdentityServerError.PERMISSION_NOT_EXISTS,           "Permission [%1$s] does not exists" }
  , { IdentityServerError.PERMISSION_AMBIGUOUS,            "Permission [%1$s] defined ambiguously" }
  , { IdentityServerError.ORGANIZATION_NOT_EXISTS,         "Organizational Scope [%1$s] does not exists" }
  , { IdentityServerError.ORGANIZATION_AMBIGUOUS,          "Organizational Scope [%1$s] defined ambiguously" }
  , { IdentityServerError.ROLE_NOT_EXISTS,                 "Role [%1$s] does not exists" }
  , { IdentityServerError.ROLE_AMBIGUOUS,                  "Role [%1$s] defined ambiguously" }
  , { IdentityServerError.ROLE_SEARCH_KEY_AMBIGUOUS,       "Search Key for role [%1$s] defined ambiguously" }

     // RMI-00031 - 00040 permission assignment errors
  , { IdentityServerError.PERMISSION_ACCESS_DENEID,        "Principal [%1$s] has insufficient privileges to perform operation [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_GRANTED,          "Not able to grant Permission [%1$s] to [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_REVOKED,          "Not able to revoke Permission [%1$s] from [%2$s]" }
  , { IdentityServerError.PERMISSION_NOT_MODIFIED,         "Not able to modify Permission [%1$s] for [%2$s]" }

    // RMI-01001 - 01010 system related messages
  , { IdentityServerMessage.CONNECTING_TO,                 "Connecting to [%1$s]" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    IdentityServerBundle.class.getName()
  , Locale.getDefault()
  , IdentityServerBundle.class.getClassLoader()
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