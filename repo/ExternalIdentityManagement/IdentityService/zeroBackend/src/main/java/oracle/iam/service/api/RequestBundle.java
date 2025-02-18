/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   RequestBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RequestBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.service.api;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.platform.core.utility.ListResourceBundle;

///////////////////////////////////////////////////////////////////////////////
// class RequestBundle
// ~~~~~ ~~~~~~~~~~~~~
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
public class RequestBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // FED-00001 - 00010 system related errors
    { RequestMessage.OK,                            "Request %1$s submitted" }
    // FED-00001 - 00010 system related errors
  , { RequestMessage.GENERAL,                       "General error: %1$s" }
  , { RequestMessage.UNHANDLED,                     "An unhandled exception has occured: %1$s"}
  , { RequestMessage.ABORT,                         "Execution aborted due to reason: %1$s"}
  , { RequestMessage.NOTIMPLEMENTED,                "Feature is not yet implemented"}

     // FED-00011 - 00020 method argument related errors
  , { RequestMessage.ARGUMENT_IS_NULL,              "Passed argument %1$s must not be null." }
  , { RequestMessage.ARGUMENT_BAD_TYPE,             "Passed argument %1$s has a bad type." }
  , { RequestMessage.ARGUMENT_BAD_VALUE,            "Passed argument %1$s contains an invalid value." }
  , { RequestMessage.ARGUMENT_SIZE_MISMATCH,        "Passed argument array size dont match expected length." }

     // FED-00021 - 00030 instance attribute related errors
  , { RequestMessage.ATTRIBUTE_IS_NULL,             "State of attribute %1$s must not be null." }

     // FED-00041 - 00050 request related errors
  , { RequestMessage.REQUEST_APPLICATION_NOTFOUND,  "Application %1$s does not exists." }
  , { RequestMessage.REQUEST_FAILED,                "Submitting request of %2$s in %1$s for beneficiary %3$s failed due to error\n\n%4$s." }

    // FED-00050 +  validation messages
  , { RequestMessage.NO_NAMING_ATTRIBUTE,           "Could not identify account identifying attribute for application %1$s" }
  , { RequestMessage.ATTR_NOT_FOUND,                "Attribute %1$s referred to in the request does not exist for application %2$s" }
  , { RequestMessage.USER_NOT_FOUND,                "User %1$s not found" }
  , { RequestMessage.ACCOUNT_NOT_IDENTIFIED,        "Could not identify account for user %1$s in application %2$s" }
  , { RequestMessage.ACCOUNT_ALREADY_EXISTS,        "Account already exists for user %1$s in application %2$s" }
  , { RequestMessage.ENTITLEMENT_NOT_UNIQUE,        "Entitlement %1$s could not be uniquely identified" }
  , { RequestMessage.ENTITLEMENT_NOT_ASSIGNED,      "Entitlement %1$s not assigned to user %2$s" }
  , { RequestMessage.ENTITLEMENT_ALREADY_ASSIGNED,  "Entitlement %1$s is already assigned to user %2$s" }
  , { RequestMessage.ACCOUNT_NOT_DISABLED,          "Account %1$s in application %2$s not disabled, cannot enable" }
  , { RequestMessage.ACCOUNT_ALREADY_DISABLED,      "Account %1$s in application %2$s already disabled, cannot disable" }
  , { RequestMessage.NAMESPACE_UNKNOWN,             "Namespace %1$s not known in application %2$s" }
  , { RequestMessage.NAMESPACE_ATTRIBUTE_UNKNOWN,   "Attribute id %1$s not known in namespace %2$s" }
  , { RequestMessage.NO_REQUIRED_ATTRIBUTE,         "Required field %1$s is not present among the entitlement attributes" }
  , { RequestMessage.NO_NAMESPACE_ENT_ATTRIBUTE,    "Could not uniquely identify entitlement field name for the namespace %1$s " }
  , { RequestMessage.REQUEST_PENDING,               "Request is already pending, ID %1$s" }
  };
    
  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    RequestBundle.class.getName()
  , Locale.getDefault()
  , RequestBundle.class.getClassLoader()
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
   ** @param  key                the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final String key) {
    return string(DEFAULT, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   ** @param  key                the key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final Locale locale, final String key) {
    return bundle(RequestBundle.class, locale).getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the formatted string resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final String key, final Object... arguments) {
    return string(DEFAULT, key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  locale             the {@link Locale} for which a resource is
   **                            desired.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   ** @param  key                key into the resource array.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  arguments          the array of substitution parameters.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the formatted string resource.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String string(final Locale locale, final String key, final Object... arguments) {
    return format(string(locale, key), arguments);
  }
}