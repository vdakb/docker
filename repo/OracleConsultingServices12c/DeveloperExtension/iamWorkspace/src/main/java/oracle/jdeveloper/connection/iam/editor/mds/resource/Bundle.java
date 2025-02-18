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

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   Bundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.editor.mds.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import oracle.ide.util.ArrayResourceBundle;

import oracle.jdeveloper.workspace.iam.utility.IconFactory;

////////////////////////////////////////////////////////////////////////////////
// class Bundle
// ~~~~~ ~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 ** <p>
 ** The resources provided by this bundle By using numeric references rather
 ** than string references, it requires less overhead and provides better
 ** performance than ListResourceBundle and PropertyResourceBundle.
 ** <br>
 ** See ResourceBundle for more information about resource bundles in general.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   12.2.1.3.42.60.84
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Bundle extends ArrayResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int RESOURCE_DOCUMENT_LABEL    =   0;
  public static final int RESOURCE_DOCUMENT_FETCH    =   1;

  public static final int RESOURCE_REMOVE_TITLE      =   2;
  public static final int RESOURCE_REMOVE_MESSAGE    =   3;
  public static final int RESOURCE_REMOVE_SUCCESS    =   4;
  public static final int RESOURCE_REMOVE_FAILED     =   5;

  public static final int RESOURCE_DELETE_TITLE      =   6;
  public static final int RESOURCE_DELETE_MESSAGE    =   7;
  public static final int RESOURCE_DELETE_SUCCESS    =   8;
  public static final int RESOURCE_DELETE_FAILED     =   9;

  public static final int RESOURCE_FOLDER_TITLE      =  10;
  public static final int RESOURCE_FOLDER_FAILED     =  11;
  public static final int RESOURCE_FOLDER_NAME_LABEL =  12;
  public static final int RESOURCE_FOLDER_NAME_SPACE =  13;

  public static final int RESOURCE_EXPORT_TITLE      =  14;
  public static final int RESOURCE_EXPORT_SUCCESS    =  15;
  public static final int RESOURCE_EXPORT_FAILED     =  16;

  public static final int RESOURCE_IMPORT_TITLE      =  17;
  public static final int RESOURCE_IMPORT_SUCCESS    =  18;
  public static final int RESOURCE_IMPORT_FAILED     =  19;

  public static final int RESOURCE_UPDATE_TITLE      =  20;
  public static final int RESOURCE_UPDATE_SUCCESS    =  21;
  public static final int RESOURCE_UPDATE_FAILED     =  22;

  public static final int RESOURCE_RENAME_TITLE      =  23;
  public static final int RESOURCE_RENAME_LABEL      =  24;
  public static final int RESOURCE_RENAME_SUCCESS    =  25;
  public static final int RESOURCE_RENAME_FAILED     =  26;

  public static final int RESOURCE_PURGE_TITLE       =  27;
  public static final int RESOURCE_PURGE_MESSAGE     =  28;
  public static final int RESOURCE_PURGE_LABEL       =  29;
  public static final int RESOURCE_PURGE_SUCCESS     =  30;
  public static final int RESOURCE_PURGE_FAILED      =  31;

  private static final String CONTENT[] = {
    /*   0 */ "Metadata Document file"
  , /*   1 */ "Retrieving Metadata Document failed. {0}"

  , /*   2 */ "Remove Connection"
  , /*   3 */ "Are you sure you want to remove \"{0}\" from this catalog?"
  , /*   4 */ "Removed \"{0}\" successfully."
  , /*   5 */ "Error removing the connection. {0}"

  , /*   6 */ "Delete Resource"
  , /*   7 */ "Are you sure you want to delete \"{0}\"?"
  , /*   8 */ "Deleted \"{0}\" successfully."
  , /*   9 */ "Error deleting the resource. {0}"

  , /*  10 */ "Create Folder"
  , /*  11 */ "Error creating the folder. {0}"
  , /*  12 */ "Enter folder name:"
  , /*  13 */ "Folder name can not contain spaces!"

  , /*  14 */ "Export Resource"
  , /*  15 */ "Exported \"{0}\" successfully to \"{1}\"."
  , /*  16 */ "Error exporting the resource. {0}"

  , /*  17 */ "Import Resource"
  , /*  18 */ "Imported \"{0}\" successfully from \"{1}\"."
  , /*  19 */ "Error importing the resource. {0}"

  , /*  20 */ "Update Resource"
  , /*  21 */ "Updated \"{0}\" successfully with \"{1}\"."
  , /*  22 */ "Error updating the resource. {0}"

  , /*  23 */ "Rename Document"
  , /*  24 */ "Enter document name:"
  , /*  25 */ "Renamed \"{0}\" successfully to \"{1}\"."
  , /*  26 */ "Error renaming the document. {0}"

  , /*  27 */ "Purge Old Versions"
  , /*  28 */ "Removes old versions from version history of unlabeled documents on the repository partition."
  , /*  29 */ "Are you sure you want to purge catalog \"{0}\"?"
  , /*  30 */ "Old versions successfully purged from catalog \"{0}\"."
  , /*  31 */ "Error purging old versions. {0}"
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ArrayResourceBundle RESOURCE = (ArrayResourceBundle)ResourceBundle.getBundle(
    Bundle.class.getName()
  , Locale.getDefault()
  , Bundle.class.getClassLoader()
  );

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ArrayResourceBundle)
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
  public Object[] getContents() {
    return CONTENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   character
  /**
   ** Returns the first character of the String associated with
   ** <code>key</code>.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the first character of the String associated
   **                            with <code>key</code>.
   */
  public static char character(final int key) {
    final String tmp = string(key);
    return (tmp == null || tmp.trim().length() == 0) ? '0' : tmp.charAt(0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ArrayResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final int key) {
    return RESOURCE.getStringImpl(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired string
   **
   ** @return                     the {@link Icon} resource the specified
   **                             <code>key</code> belongs to.
   */
  public static Icon icon(final int key) {
    return IconFactory.create(oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle.class, string(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaledIcon
  /**
   ** Fetch an {@link Icon} from this {@link ArrayResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will scaled to the dimension <code>width</code>
   ** <code>height</code> in pixel to fit the layout of the Oracle JDeveloper
   ** Gallery item list
   **
   ** @param  key                index into the resource array.
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    the {@link Icon} resource scaled up or doen to
   **                            a dimension of 16x16 pixel.
   */
  public static Icon scaledIcon(final int key, final int width, int height) {
    return IconFactory.scaled(null, IconFactory.create(oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle.class, string(key)), width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Fetchs a image for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired image.
   **
   ** @return                     the {@link ImageIcon} resource the specified
   **                             <code>key</code> belongs to.
   */
  public static ImageIcon image(final int key) {
    return IconFactory.create(oracle.jdeveloper.connection.iam.editor.ods.resource.Bundle.class, string(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  param1              the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final int index, final Object param1) {
    return RESOURCE.formatImpl(index, param1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  param1              the subsitution value for {0}
   ** @param  param2              the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object param1, Object param2) {
    return RESOURCE.formatImpl(index, param1, param2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  param1              the subsitution value for {0}
   ** @param  param2              the subsitution value for {1}
   ** @param  param3              the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object param1, Object param2, Object param3) {
    return RESOURCE.formatImpl(index, param1, param2, param3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  index               index into the resource array
   ** @param  params               the array of parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object[] params) {
    return RESOURCE.formatImpl(index, params);
  }
}