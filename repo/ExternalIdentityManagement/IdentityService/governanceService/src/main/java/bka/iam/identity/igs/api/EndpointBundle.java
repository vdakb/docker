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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   EndpointBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    EndpointBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.api;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.util.Locale;

////////////////////////////////////////////////////////////////////////////////
// class EndpointBundle
// ~~~~~ ~~~~~~~~~~~~~~
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
public class EndpointBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
     // UID-00011 - 00020 method argument related errors
    { EndpointError.ARGUMENT_IS_NULL,              "Passed argument [%1$s] must not be null." }
  , { EndpointError.ARGUMENT_BAD_TYPE,             "Passed argument [%1$s] has a bad type." }
  , { EndpointError.ARGUMENT_BAD_VALUE,            "Passed argument [%1$s] contains an invalid value." }
  , { EndpointError.ARGUMENT_LENGTH_MISMATCH,      "Length of the passed value for argument [%1$s]does not match expected length." }
  , { EndpointError.ARGUMENT_SIZE_MISMATCH,        "Array size for passed argument [%1$s] does not match expected size." }

     // UID-00021 - 00030 method invokation related errors
  , { EndpointError.METHOD_NOT_PERMITTED,          "You are getting this error because you called a method that is not permitted for this endpoint." }

     // UID-00031 - 00040 operation related errors
  , { EndpointError.OPERATION_NOT_PERMITTED,       "You are getting this error because you invoked an operation that is not allowed for the current state of the entity." }

     // 00041 - 00050 tenant related errors
  , { EndpointError.TENANT_SEARCH_NOT_PERMITTED,   "You are not permitted to search for unique identifier in tenant [%1$s]." }
  , { EndpointError.TENANT_LOOKUP_NOT_PERMITTED,   "You are not permitted to search for unique identifier in tenant [%1$s]." }
  , { EndpointError.TENANT_MODIFY_NOT_PERMITTED,   "You are not permitted to modify unique identifiers in tenant [%1$s]." }
  , { EndpointError.TENANT_GENERATE_NOT_PERMITTED, "You are not permitted to generate an unique identifier in tenant [%1$s]." }
  , { EndpointError.TENANT_REGISTER_NOT_PERMITTED, "You are not permitted to register an unique identifier in tenant [%1$s]." }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(EndpointBundle.class.getName(), Locale.getDefault());

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
   **                            Allowed Object is array of {@link String}.
   **
   ** @return                    the String resource
   **                            <br>
   **                            Possible Object is array of {@link String}.
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
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object... arguments) {
    for (int i = 0; i < arguments.length; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    return String.format(RESOURCE.getString(key), arguments);
  }
}