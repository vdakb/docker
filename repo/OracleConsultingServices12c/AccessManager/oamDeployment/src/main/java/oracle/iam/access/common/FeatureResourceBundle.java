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

package oracle.iam.access.common;

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
    { FeatureError.OPERATION_PRINT_FAILED,        "Printing of %1$s [%2$s] failed. Reason: %3$s"}

     // 00021 - 00030 identity store related errors
  , { FeatureError.COMMON_PORTNUMBER_INVALID,     "Port number can't be negative for %1$s." }

     // 00031 - 00040 instance element related errors
  , { FeatureError.INSTANCE_MANDATORY,            "Specify at least one %1$s instance." }
  , { FeatureError.INSTANCE_ONLYONCE,             "The %1$s [%2$s] already added to this task." }
  , { FeatureError.INSTANCE_EXISTS,               "The %1$s [%2$s] already exists." }
  , { FeatureError.INSTANCE_NOTEXISTS,            "The %1$s [%2$s] does not exists." }
  , { FeatureError.INSTANCE_CREATE,               "The %1$s [%2$s] was not created: Reason %2$s." }
  , { FeatureError.INSTANCE_MODIFY,               "The %1$s [%2$s] was not modified: Reason %2$s." }
  , { FeatureError.INSTANCE_DELETE,               "The %1$s [%2$s] was not deleted: Reason %2$s." }

     // 00041 - 00050 type element related errors
  , { FeatureError.PARTNER_TYPE_MANDATORY,        "Specify at least one %1$s type." }
  , { FeatureError.PARTNER_TYPE_ONLYONCE,         "The %1$s [%2$s] already added to this task." }

    // 00051 - 00060 parameter element related errors
  , { FeatureError.PARAMETER_VALUE_INVALID,       "%1$s parameter [%2$s] has invalid value. [%3$s]" }
  , { FeatureError.PARAMETER_UNMODIFIABLE,        "%1$s parameter [%2$s] cannot be modified." }

     // 00061 - 00070 server related errors
  , { FeatureError.DOMAIN_OBJECTNAME_MALFORMED,   "Malformed ObjectName. Reason: %1$s" }
  , { FeatureError.DOMAIN_RUNTIMEBEAN_NOTFOUND,   "Runtime MBean %1$s does not exists" }
  , { FeatureError.DOMAIN_CONFIGBEAN_NOTFOUND,    "Configuration MBean %1$s does not exists" }
  , { FeatureError.DOMAIN_ATTRIBUTENAME_NOTFOUND, "Attribute %1$s does not exists" }
  , { FeatureError.DOMAIN_BEANINSTANCE_NOTFOUND,  "Instance does not exists. Reason: %1$s" }
  , { FeatureError.DOMAIN_QUERY_FAILED,           "Could not query the mbeans server. Reason: %1$s" }

    // 00071 - 00080 registration related errors
  , { FeatureError.REMOTE_BINDING_CONTEXT,        "JAXB instance could not be created: Reason %1$s" }
  , { FeatureError.REMOTE_BINDING_SCHEMA,         "Error in setting the validation schema: Reason %1$s" }
  , { FeatureError.REMOTE_BINDING_VALIDATION,     "Error in setting the validation event handler: Reason %1$s" }
  , { FeatureError.REMOTE_BINDING_VIOLATION,      "Document error at line %1$s, column %2$s: [%3$s]" }
  , { FeatureError.REMOTE_BINDING_MARSHAL,        "Error in marshalling the XML data: Reason %1$s" }
  , { FeatureError.REMOTE_BINDING_UNMARSHAL,      "Error in unmarshalling the XML data: Reason %1$s" }
  , { FeatureError.REMOTE_BINDING_ENCODING,       "Unsuppported character encoding [%1$s] being used"}
  , { FeatureError.REMOTE_BINDING_FILE_OPEN,      "Cannot open the file [%1$s]" }

     // 01011 - 01020 common messages
  , { FeatureMessage.SERVICE_REQUEST_PAYLOAD,     "Service Request Payload:\n%1$s" }
  , { FeatureMessage.SERVICE_RESPONSE_PAYLOAD,    "Service Response Payload:\n%1$s" }

     // 01021 - 01030 common messages
  , { FeatureMessage.OPERATION_PRINT_BEGIN,       "Printing %1$s [%2$s] ..." }
  , { FeatureMessage.OPERATION_PRINT_SUCCESS,     "%1$s [%2$s] end of report"}
  , { FeatureMessage.OPERATION_PRINT_SKIPPED,     "Printing %1$s [%2$s] skipped"}
  , { FeatureMessage.OPERATION_PRINT_NOTFOUND,    "%1$s [%2$s] not found"}

     // 01031 - 01040 available service status messages
  , { FeatureMessage.ACCESS_SERVICE,              "Access Service" }
  , { FeatureMessage.ACCESS_SERVICE_STATUS,       "Status" }
  , { FeatureMessage.ACCESS_SERVICE_ENABLED,      "enabled" }
  , { FeatureMessage.ACCESS_SERVICE_DISABLED,     "disabled"}
  , { FeatureMessage.ACCESS_SERVICE_UNKNOWN,      "unknown"}

     // 01041 - 01050 identity store messages
  , { FeatureMessage.IDENTITY_STORE,              "Identity Store" }
  , { FeatureMessage.IDENTITY_STORE_PROPERTY,     "Property" }
  , { FeatureMessage.IDENTITY_STORE_VALUE,        "Value" }

     // 01051 - 01060 federation partner messages
  , { FeatureMessage.FEDERATION_PARTNER,          "Federation Partner" }
  , { FeatureMessage.FEDERATION_PARTNER_TYPE,     "Type" }

     // 01061 - 01070 agent messages
  , { FeatureMessage.AGENT_PROPERTY,              "Property" }
  , { FeatureMessage.AGENT_VALUE,                 "Value" }
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