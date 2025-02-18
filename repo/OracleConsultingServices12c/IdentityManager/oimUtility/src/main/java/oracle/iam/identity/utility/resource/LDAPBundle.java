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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   LDAPBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDAPBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.file.LDAPError;
import oracle.iam.identity.utility.file.LDAPMessage;

////////////////////////////////////////////////////////////////////////////////
// class LDAPBundle
// ~~~~~ ~~~~~~~~~~
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
 */
public class LDAPBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // LDAP-00061 - LDAP-00080 file system naming and lookup related errors
    { LDAPError.FILENAME_MISSING,     "File missing: %1$s needs a single filename on the command line !"}
  , { LDAPError.FILEEXTENSION_IS_BAD, "Bad Extension: %1$s needs a single filename on the command line !"}
  , { LDAPError.NOTEXISTS,            "%1$s is invalid, because it doesn't exists" }
  , { LDAPError.NOTAFOLDER,           "Usage of %2$s as %1$s is invalid, because it's not a folder" }
  , { LDAPError.NOTAFILE,             "Usage of %2$s as %1$s is invalid, because it's not a file" }
  , { LDAPError.NOTREADABLE,          "Cannot use %2$s as %1$s, because it's not readable" }
  , { LDAPError.NOTWRITABLE,          "Cannot use %2$s as %1$s, because it's not writable" }
  , { LDAPError.NOTCREATABLE,         "Cannot create %1$s reason: %2$s" }
  , { LDAPError.NOTCLOSEDINPUT,       "Cannot close input stream %1$s" }
  , { LDAPError.NOTCLOSEDOUTPUT,      "Cannot close output stream %1$s" }

  , { LDAPError.SEPARATOR,            "Separator [%1$s] missing in [%2$s]" }
  , { LDAPError.OID_REQUIRED,         "OID required for control" }
  , { LDAPError.CRITICALITY_REQUIRED, "Criticality for control must be true or false, not [%1$s]" }
  , { LDAPError.DELETE_REQUIRED,      "Delete operation does not expect any value" }
  , { LDAPError.ATTRIBUTE_REQUIRED,   "Add operation needs a value for attribute [%1$s]" }

    // LDAP-01000 generic message template
  , { LDAPMessage.MESSAGE,            "%1$s" }

    // LDAP-01001 - LDAP-01010 processing messages
  , { LDAPMessage.EXPORT_BEGIN,       "Exporting to file %1$s" }
  , { LDAPMessage.EXPORT_FINISHED,    "File %1$s exported\n %2$s entries succeeded, %3$s entries skipped" }
  , { LDAPMessage.IMPORT_BEGIN,       "Importing from file %1$s" }
  , { LDAPMessage.IMPORT_FINISHED,    "File %1$s imported\n %2$s entries succeeded, %3$s entries skipped" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    LDAPBundle.class.getName()
  , Locale.getDefault()
  , LDAPBundle.class.getClassLoader()
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