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

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Scheduler Shared Library
    Subsystem   :   Virtual Resource Management

    File        :   ResolverBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ResolverBundle.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-02-10  DSteding    First release version
*/

package oracle.iam.identity.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.scheduler.policy.ResolverError;
import oracle.iam.identity.scheduler.policy.ResolverMessage;

////////////////////////////////////////////////////////////////////////////////
// class ResolverBundle
// ~~~~~ ~~~~~~~~~~~~~~
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
public class ResolverBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    // 00071 - 00080 object resolving related errors
    { ResolverError.TYPE_MISMATCH,                              "Mismatch in \"Type\" of %1$s loaded for %2$s: required %3$s found %4$s" }
  , { ResolverError.ORDER_MISMATCH,                             "Mismatch in \"Order For\" of %1$s loaded for %2$s: required %3$s found %4$s" }
  , { ResolverError.POLICY_FORMSIZE_MISMATCH,                   "Mismatch in number of \"Form Definition\" detected for %1$s: required %2$s found %3$s" }
  , { ResolverError.POLICY_FORM_MISMATCH,                       "Mismatch in \"Form Definition\" detected for %1$s: form that not match is %2$s" }
  , { ResolverError.NOT_PROVISIONED,                            "Resource Object [%1$s] not provisioned to account %2$s" }
  , { ResolverError.AMBIGUOS_PROVISIONED,                       "Resource Object [%1$s] provisioned ambigously for account %2$s" }

  , { ResolverError.NO_INBOUND_MAPPING,                         "Account attribute mapping empty after transformation" }
  , { ResolverError.NO_OUTBOUND_MAPPING,                        "Account attribute mapping empty after transformation" }

  , { ResolverMessage.PROVISION,                                "Provision" }
  , { ResolverMessage.REVOKE,                                   "Revoke" }
  , { ResolverMessage.ENABLE,                                   "Enable" }
  , { ResolverMessage.DISABLE,                                  "Disable" }

  , { ResolverMessage.PRECONDITION_STARTED,                     "Pre-Condition Checks of %1$s for %2$s started ..." }
  , { ResolverMessage.PRECONDITION_COMPLETED,                   "Pre-Condition Checks of %1$s for %2$s completed." }
  , { ResolverMessage.PRECONDITION_STOPPED,                     "Pre-Condition Checks of %1$s for %2$s stopped" }
  , { ResolverMessage.PRECONDITION_CONFIGURATION_BEGIN,         "   ... Check Configuration for definition %1$s started ..." }
  , { ResolverMessage.PRECONDITION_CONFIGURATION_COMPLETE,      "   ... Check Configuration for definition %1$s completed." }
  , { ResolverMessage.PRECONDITION_CONFIGURATION_FAILED,        "   ... Check Configuration for definition %1$s failed." }
  , { ResolverMessage.PRECONDITION_CONFIGURATION_MAPPING_EMPTY, "   ... Check Configuration for definition %1$s has no entitlements detected, execution will be stopped" }
  , { ResolverMessage.PRECONDITION_RESOURCE_MAPPING_BEGIN,      "   ... Check Resource Object Mapping for %1$s started ..." }
  , { ResolverMessage.PRECONDITION_RESOURCE_MAPPING_COMPLETE,   "   ... Check Resource Object Mapping for %1$s completed." }
  , { ResolverMessage.PRECONDITION_RESOURCE_MAPPING_FAILED,     "   ... Check Resource Object Mapping for %1$s failed." }
  , { ResolverMessage.PRECONDITION_RESOURCE_MAPPING_EMPTY,      "   ... Check Resource Object Mapping for %1$s has no resources detected, execution will be stopped" }
  , { ResolverMessage.PRECONDITION_ROLE_MAPPING_BEGIN,          "   ... Check Role Mapping for %1$s started ..." }
  , { ResolverMessage.PRECONDITION_ROLE_MAPPING_COMPLETE,       "   ... Check Role Mapping for %1$s completed." }
  , { ResolverMessage.PRECONDITION_ROLE_MAPPING_FAILED,         "   ... Check Role Mapping for %1$s failed." }
  , { ResolverMessage.PRECONDITION_ROLE_MAPPING_EMPTY,          "   ... Check Role Mapping for %1$s has no policies detected, execution will be stopped" }
  , { ResolverMessage.PRECONDITION_POLICY_MAPPING_BEGIN,        "   ... Check Access Policy Mapping for %1$s started ..." }
  , { ResolverMessage.PRECONDITION_POLICY_MAPPING_COMPLETE,     "   ... Check Access Policy Mapping for %1$s completed." }
  , { ResolverMessage.PRECONDITION_POLICY_MAPPING_FAILED,       "   ... Check Access Policy Mapping for %1$s failed." }
  , { ResolverMessage.PRECONDITION_POLICY_MAPPING_EMPTY,        "   ... Check Access Policy Mapping for %1$s has no policies detected, execution will be stopped" }

  , { ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_BEGIN,         "   ... Entitlement Mining for %1$s %2$s started ..." }
  , { ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_COMPLETE,      "   ... Entitlement Mining for %1$s %2$s completed." }
  , { ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_FAILED,        "   ... Entitlement Mining for %1$s %2$s failed." }
  , { ResolverMessage.MINIMG_ACCOUNT_ENTITLEMENT_EMPTY,         "   ... Entitlement Mining has no entitlemensts detected for %1$s %2$s" }

  , { ResolverMessage.ACCOUNT_RESOURCE_EMPTY,                   "%1$s %2$s has not a provisioned %3$s" }
  , { ResolverMessage.ACCOUNT_RESOURCE_NORESOURCES,             "%1$s %2$s has no resources enlisted to %3$s" }
  , { ResolverMessage.ACCOUNT_RESOURCE_CANDIATE,                "%1$s %2$s not matched; marked as candidate to retrofit" }

  , { ResolverMessage.OBJECT_PROVISIONING_NOACTION,             "%3$s %4$s is already done for %1$s %2$s" }
  , { ResolverMessage.OBJECT_PROVISIONING_STARTED,              "%3$s %4$s will be provisioned to %1$s %2$s" }
  , { ResolverMessage.OBJECT_PROVISIONING_ORDERED,              "%3$s %4$s ordered for %1$s %2$s" }
  , { ResolverMessage.OBJECT_PROVISIONING_PROVISIONED,          "%3$s %4$s provisioned to %1$s %2$s" }
  , { ResolverMessage.OBJECT_PROVISIONING_REVOKED,              "%3$s %4$s revoked from %1$s %2$s" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    ResolverBundle.class.getName()
  , Locale.getDefault()
  , ResolverBundle.class.getClassLoader()
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
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for %1$s
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
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for %2$s
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
   ** @param  argument1           the subsitution value for %1$s
   ** @param  argument2           the subsitution value for %2$s
   ** @param  argument3           the subsitution value for %3$s
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