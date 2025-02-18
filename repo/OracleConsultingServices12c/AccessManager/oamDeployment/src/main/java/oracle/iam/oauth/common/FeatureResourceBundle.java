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

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   FeatureException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FeatureException.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.oauth.common;

import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class FeatureResourceBundle
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
public class FeatureResourceBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {

     // 00011 - 00020 operational errors
    { FeatureError.OPERATION_PRINT_FAILED,     "Reporting of %1$s [%2$s] failed. Reason: %3$s"}

     // 00021 - 00030 instance element related errors
  , { FeatureError.INSTANCE_MANDATORY,         "Specify at least one %1$s instance." }
  , { FeatureError.INSTANCE_ONLYONCE,          "The %1$s [%2$s] already added to this task." }

     // 01011 - 01020 common messages
  , { FeatureMessage.SERVICE_REQUEST_PAYLOAD,  "Service Request Payload:\n%1$s" }
  , { FeatureMessage.SERVICE_RESPONSE_PAYLOAD, "Service Response Payload:\n%1$s" }

     // 01021 - 01030 common messages
  , { FeatureMessage.OPERATION_REPORT_BEGIN,   "Report %1$s [%2$s] ..." }
  , { FeatureMessage.OPERATION_REPORT_SUCCESS, "%1$s [%2$s] end of report"}
  , { FeatureMessage.OPERATION_REPORT_SKIPPED, "Reporting %1$s [%2$s] skipped"}

     // 01031 - 01040 identity domain property messages
  , { FeatureMessage.IDENTITY_DOMAIN,           "Identity Domain" }
  , { FeatureMessage.IDENTITY_DOMAIN_PROPERTY,  "Property" }
  , { FeatureMessage.IDENTITY_DOMAIN_VALUE,     "Value" }
  , { FeatureMessage.RESOURCE_SERVER,           "Resource Server" }
  , { FeatureMessage.RESOURCE_SERVER_PROPERTY,  "Property" }
  , { FeatureMessage.RESOURCE_SERVER_VALUE,     "Value" }
  , { FeatureMessage.RESOURCE_CLIENT,           "Resource Client" }
  , { FeatureMessage.RESOURCE_CLIENT_PROPERTY,  "Property" }
  , { FeatureMessage.RESOURCE_CLIENT_VALUE,     "Value" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(FeatureResourceBundle.class.getName());

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