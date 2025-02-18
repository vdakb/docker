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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   GlassFish Server Security Realm

    File        :   RealmBundle.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RealmBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.access.glassfish.realm;

import java.util.ResourceBundle;
import java.util.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class RealmBundle
// ~~~~~ ~~~~~~~~~~~
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
public class RealmBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String[][] CONTENT = {
    // IAD-00011 - 00020 method argument related errors
    { RealmError.ARGUMENT_IS_NULL,       "Passed argument %1$s must not be null!" }
  , { RealmError.ARGUMENT_BAD_TYPE,      "Passed argument %1$s has a bad type!" }
  , { RealmError.ARGUMENT_BAD_VALUE,     "Passed argument %1$s contains an invalid value!" }

    // IAD-00021 - 00030 configuration property related errors
  , { RealmError.PROPERTY_IS_NULL,       "Configutration property %1$s must not be null!" }
  , { RealmError.PROPERTY_BAD_TYPE,      "Configutration property %1$s has a bad type!" }
  , { RealmError.PROPERTY_BAD_VALUE,     "Configutration property %1$s contains an invalid value!" }

    // IAD-00061 - 00070 naming and lookup related errors
  , { RealmError.LOCATOR_INITIALIZE,     "Could not initialize service locator!" }
  , { RealmError.CONTEXT_CONNECTION,     "Could connect to JNDI context at %1$s!" }
  , { RealmError.CONTEXT_INITIALIZE,     "Could not initialize JNDI context!" }
  , { RealmError.CONTEXT_CLOSE,          "Could not close JNDI context!" }
  , { RealmError.CONTEXT_ENVIRONMENT,    "Could not obtain environment from context!" }
  , { RealmError.OBJECT_LOOKUP,          "Lookup failed in JNDI context for object %1$s!" }
  , { RealmError.OBJECT_CREATION,        "Could not create object %1$s!" }
  , { RealmError.OBJECT_ACCESS,          "Access to object %1$s has been failed!" }

    // IAD-00081 - 00090 realm related errors
  , { RealmError.REALM_BADTYPE,          "Bad type of Realm!" }

    // IAD-00081 - 00090 user lookup related errors
  , { RealmError.USER_REQUIRED,          "User name required!" }
  , { RealmError.USER_NOTFOUND,          "No user found for username [%1$s]!" }

    // IAD-00091 - 00100 bearer token related errors
  , { RealmError.TOKEN_EXPIRED,          "Token expired!" }
  , { RealmError.TOKEN_NOTBEFORE,        "Token violates not-before rule!" }
  , { RealmError.TOKEN_SIGNATURE,        "Token signature invalid!" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final RealmBundle RESOURCE = (RealmBundle)ResourceBundle.getBundle(RealmBundle.class.getName());

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
   **                            <br>
   **                            Possible Object is array of {@link Object}.
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
    return RESOURCE.format(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "%s" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                the key for the desired string pattern
   ** @param  arguments          the array of substitution arguments.
   **
   ** @return                    the formatted String resource
   */
  public String format(final String key, final Object... arguments) {
    int count = arguments == null ? 0 : arguments.length;
    if (count == 0)
      return this.getString(key);

    for (int i = 0; i < count; ++i) {
      if (arguments[i] == null)
        arguments[i] = "(null)";
    }
    return String.format(internal(key), arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internal
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                the key for the desired string.
   **
   ** @return                    the object for the given key.
   */
  protected final String internal(final String key) {
    return getObject(key).toString();
  }
}