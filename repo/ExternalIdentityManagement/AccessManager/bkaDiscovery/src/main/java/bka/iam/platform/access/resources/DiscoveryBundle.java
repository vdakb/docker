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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Identity Provider Discovery

    File        :   DiscoveryBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DiscoveryBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2019-03-03  DSteding    First release version
*/

package bka.iam.platform.access.resources;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class DiscoveryBundle
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Property bundle for MBean annotations.
 ** <p>
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
public class DiscoveryBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
    {"cfg.configuration",   "JMX config mbean for managing Identity Provider properties."}
  , {"cfg.action.save",     "Save Identity Provider configuration." }
  , {"cfg.action.refresh",  "Refresh Identity Provider configuration." }
  , {"idp.action.list",     "List all configured Identity Providers." }
  , {"idp.action.register", "Adds an Identity Provider." }
  , {"idp.action.remove",   "Removes an Identity Provider." }
  , {"idp.action.modify",   "Updates an Identity Provider." }
  , {"net.action.list",     "List all configured Network Associations." }
  , {"net.action.register", "Adds a Network Association." }
  , {"net.action.remove",   "Removes a Network Association." }
  , {"net.action.modify",   "Updates a Network Association." }

  , {"idp.error.register",  "An Identity Provider with the same identifier already registered." }
  , {"idp.error.modify",    "An Identity Provider with the identifier specified is not registered." }
  , {"idp.error.remove",    "An Identity Provider with the identifier specified is not registered." }

  , {"net.error.register",  "A Network Association with the same CIDR already registered." }
  , {"net.error.modify",    "A Network Association with the CIDR specified is not registered." }
  , {"net.error.remove",    "A Network Association with the CIDR specified is not registered." }
  , {"net.error.id",        "An Identity Provider with the identifier specified is not registered." }
  
  , {"otp.action.list",     "List all configured OTP Templates." }
  , {"otp.action.register", "Adds a OTP Template." }  
  , {"otp.action.remove",   "Removes a OTP Template." }
  , {"otp.action.modify",   "Updates a OTP Template." }
  , {"otp.error.register",  "OTP Template with same Locale already registered." }
  , {"otp.error.modify",    "OTP Template with this locale is not registered." }
  , {"otp.error.remove",    "OTP Template with this locale is not registered." }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    DiscoveryBundle.class.getName()
  , Locale.getDefault()
  , DiscoveryBundle.class.getClassLoader()
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
   ** @param  argument            the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   ** @param  argument3           the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public static String string(final String key, final Object argument1, final Object argument2, final Object argument3) {
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
  public static String string(final String key, final Object... arguments) {
    return RESOURCE.formatted(key, arguments);
  }
}