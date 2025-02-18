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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    FlatFileBundle.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.utility.file.FlatFileError;
import oracle.iam.identity.utility.file.FlatFileMessage;

////////////////////////////////////////////////////////////////////////////////
// class FlatFileBundle
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
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public class FlatFileBundle extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String CONTENT[][] = {
     // TXT-00001 - 00010 task related errors
    { FlatFileError.UNHANDLED,               "Unhandled exception occured: %1$s" }
  , { FlatFileError.GENERAL,                 "Encounter some problems: %1$s" }
  , { FlatFileError.ABORT,                   "Execution aborted: %1$s"}

     // TXT-00011 - 00020 file system naming and lookup related errors
  , { FlatFileError.FILENAME_MISSING,        "File missing: %1$s needs a single filename on the command line !"}
  , { FlatFileError.NOTAFOLDER,              "Usage of [%2$s] as [%1$s] is invalid, because it's not a folder" }
  , { FlatFileError.NOTAFILE,                "Usage of [%2$s] as [%1$s] is invalid, because it's not a file" }
  , { FlatFileError.NOTREADABLE,             "Cannot use [%2$s] as [%1$s], because it's not readable" }
  , { FlatFileError.NOTWRITABLE,             "Cannot use [%2$s] as [%1$s], because it's not writable" }
  , { FlatFileError.NOTCREATABLE,            "Cannot create [%1$s] reason: %2$s" }
  , { FlatFileError.NOTCLOSEDINPUT,          "Cannot close input stream [%1$s]" }
  , { FlatFileError.NOTCLOSEDOUTPUT,         "Cannot close output stream [%1$s]" }

     // TXT-00021 - 00030 parsing related errors
  , { FlatFileError.PARSER_ERROR,            "Unexpected exception %1$s at line %2$s!\n%3$s"}
  , { FlatFileError.EXECUTION_FAILED,        "%1$s failed to %2$s %3$s !"}
  , { FlatFileError.CONTENT_UNKNOWN,         "Field [%1$s] has no content !"}
  , { FlatFileError.CONTENT_NOT_FOUND,       "Field [%1$s] is unknown !"}
  , { FlatFileError.NOT_ENLISTED_ATTRIBUTE,  "Attribute [%1$s] is not enlisted as an attribute."}
  , { FlatFileError.NOT_ENLISTED_IDENTIFIER, "Attribute [%1$s] is not enlisted as an attribute."}

     // TXT-01000 generic message template
  , { FlatFileMessage.MESSAGE,               "%1$s" }

     // TXT-01001 - 01010 processing messages
  , { FlatFileMessage.VALIDATING,            "Validating FlatFile descriptor" }
  , { FlatFileMessage.LOADING,               "Loading ..." }
  , { FlatFileMessage.PROCESSING,            "Processing ..." }
  , { FlatFileMessage.COMPLETED,             "End of differences." }
  , { FlatFileMessage.IDENTICALLY,           "Files are identical." }
  , { FlatFileMessage.LINECOUNT,             "Line count of %1$s file: [%2$s]" }
  , { FlatFileMessage.ENDOFFILE,             "End-Of-File reached for [%1$s]" }
  , { FlatFileMessage.EMPTYFILE,             "File [%1$s] is empty" }
  , { FlatFileMessage.CHUNKSIZE,             "%1$s entities written to [%2$s]" }
  , { FlatFileMessage.CHUNKMERGED,           "Merge of chunk %1$s completed" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    FlatFileBundle.class.getName()
  , Locale.getDefault()
  , FlatFileBundle.class.getClassLoader()
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