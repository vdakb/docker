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
    Subsystem   :   Identity Manager Facility

    File        :   Bundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.oim;

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

  public static final int IDENTITY_CREATE_TITLE            =   0;
  public static final int IDENTITY_CREATE_HEADER           =   1;
  public static final int IDENTITY_MODIFY_TITLE            =   2;
  public static final int IDENTITY_MODIFY_HEADER           =   3;
  public static final int IDENTITY_DELETE_TITLE            =   4;
  public static final int IDENTITY_DELETE_SINGLE           =   5;
  public static final int IDENTITY_DELETE_MULTIPLE         =   6;
  public static final int IDENTITY_HEADER_IMAGE            =   7;

  public static final int DIRECTORY_CREATE_TITLE           =   8;
  public static final int DIRECTORY_CREATE_HEADER          =   9;
  public static final int DIRECTORY_MODIFY_TITLE           =  10;
  public static final int DIRECTORY_MODIFY_HEADER          =  11;
  public static final int DIRECTORY_HEADER_IMAGE           =  12;

  public static final int IDENTITY_SERVER_HEADER           =  13;
  public static final int DIRECTORY_NAME_HEADER            =  14;
  public static final int DIRECTORY_PATH_HEADER            =  15;

  public static final int IDENTITY_TEST_LABEL              =  16;
  public static final int IDENTITY_TEST_HINT               =  17;
  public static final int IDENTITY_TEST_ERROR              =  18;
  public static final int IDENTITY_TEST_FAILED             =  19;
  public static final int IDENTITY_TEST_SUCCESS            =  20;
  public static final int IDENTITY_TEST_WORKING            =  21;
  public static final int IDENTITY_TEST_MESSAGE            =  22;
  public static final int IDENTITY_TEST_CONNECTING         =  23;
  public static final int IDENTITY_TEST_CANCELLED          =  24;
  public static final int IDENTITY_TEST_AUTHENTICATION     =  25;

  public static final int CONNECTION_LEAF_ICON             =  26;
  public static final int CONNECTION_BUSY_ICON             =  27;
  public static final int CONNECTION_BUSY_TEXT             =  28;
  public static final int CONNECTION_NAME_LABEL            =  29;
  public static final int CONNECTION_NAME_ERROR            =  30;
  public static final int CONNECTION_NAME_EXISTS           =  31;
  public static final int CONNECTION_NO_WORKSPACE          =  32;

  public static final int DIRECTORY_PATH_LABEL             =  33;
  public static final int DIRECTORY_PATH_ERROR             =  34;
  public static final int DIRECTORY_PATH_MOTEXISTS         =  35;

  public static final int RUNTIME_LIBRARY_NOTFOUND         =  36;
  public static final int RUNTIME_METHOD_NOTFOUND          =  37;
  public static final int RUNTIME_METHOD_INACCESSIBLE      =  38;
  public static final int RUNTIME_METHOD_EXCEPTION         =  39;
  public static final int RUNTIME_CLIENT_NOTFOUND          =  40;

  public static final int CONTEXT_SERVERTYPE_NOTSUPPORTED  =  41;
  public static final int CONTEXT_ENCODING_NOTSUPPORTED    =  42;
  public static final int CONTEXT_CONNECTION_ERROR         =  43;
  public static final int CONTEXT_AUTHENTICATION           =  44;
  public static final int CONTEXT_ACCESS_DENIED            =  45;
  public static final int CONTEXT_ERROR_UNHANDLED          =  46;
  public static final int CONTEXT_ERROR_GENERAL            =  47;
  public static final int CONTEXT_ERROR_ABORT              =  48;

  public static final int IDENTITY_NODE_LABEL              =  49;
  public static final int IDENTITY_NODE_HINT               =  50;
  public static final int IDENTITY_ITRESOURCEDEF_LABEL     =  51;
  public static final int IDENTITY_ITRESOURCEDEF_HINT      =  52;
  public static final int IDENTITY_ITRESOURCEDEF_ICON      =  53;
  public static final int IDENTITY_ITRESOURCE_LABEL        =  54;
  public static final int IDENTITY_ITRESOURCE_HINT         =  55;
  public static final int IDENTITY_ITRESOURCE_ICON         =  56;
  public static final int IDENTITY_LOOKUP_LABEL            =  57;
  public static final int IDENTITY_LOOKUP_HINT             =  58;
  public static final int IDENTITY_LOOKUP_ICON             =  59;
  public static final int IDENTITY_ADAPTER_LABEL           =  60;
  public static final int IDENTITY_ADAPTER_HINT            =  61;
  public static final int IDENTITY_ADAPTER_ICON            =  62;
  public static final int IDENTITY_FORM_LABEL              =  63;
  public static final int IDENTITY_FORM_HINT               =  64;
  public static final int IDENTITY_FORM_ICON               =  65;
  public static final int IDENTITY_RESOURCE_LABEL          =  66;
  public static final int IDENTITY_RESOURCE_HINT           =  67;
  public static final int IDENTITY_RESOURCE_ICON           =  68;
  public static final int IDENTITY_PROCESS_LABEL           =  69;
  public static final int IDENTITY_PROCESS_HINT            =  70;
  public static final int IDENTITY_PROCESS_ICON            =  71;

  private static final String CONTENT[] = {
    /*   0 */ "Create Identity Server Connection"
  , /*   1 */ "Configure a new connection to an installed identity server."
  , /*   2 */ "Modify Identity Server Connection"
  , /*   3 */ "Modify the connection details of the existing identity server connection."
  , /*   4 */ "Confirm Delete"
  , /*   5 */ "Delete {0}?"
  , /*   6 */ "Delete selected nodes?"
  , /*   7 */ "gallery/identity-header.png"

  , /*   8 */ "Create Identity Deployment Connection"
  , /*   9 */ "Choose Application Resources to create a file system connection owned by and deployed with the current application ({0}). Choose IDE Connections to create a connection that can be added to any application."
  , /*  10 */ "Modify Identity Deployment Connection"
  , /*  11 */ "Modify a file system connection owned by and deployed with the current application ({0}) or can be added to any application."
  , /*  12 */ "gallery/deploy-header.png"

  , /*  13 */ "Connection"
  , /*  14 */ "Deployment"
  , /*  15 */ "Directory"

  , /*  16 */ "&Test"
  , /*  17 */ "Test Connection"
  , /*  18 */ "Test Failed. The Adapter could not establish the connection:\n"
  , /*  19 */ "Test Failed. The Adapter could not establish the connection.\nCheck network parameters or make sure that the required identity server is reachable."
  , /*  20 */ "Success!"
  , /*  21 */ "Working..."
  , /*  22 */ "Connecting to {0}..."
  , /*  23 */ "Connecting..."
  , /*  24 */ "{0}\nTest Cancelled."
  , /*  25 */ "Test Failed. The Adapter could not establish the connection.\nCheck authentication parameters: Authentication method, Username, Password.\n"

  , /*  26 */ "gallery/leaf.gif"
  , /*  27 */ "gallery/busy.gif"
  , /*  28 */ "... loading ..."
  , /*  29 */ "&Name"
  , /*  30 */ "Connection Name cannot be empty!"
  , /*  31 */ "Connection with name {0} already exist!"
  , /*  32 */ "not available"

  , /*  33 */ "&Path"
  , /*  34 */ "Directory Path cannot be empty!"
  , /*  35 */ "Directory Path {0} does not exists!"

  , /*  36 */ "Required User Library with id \"{0}\" isn''t available."
  , /*  37 */ "Method not found. Reason:\n{0}"
  , /*  38 */ "Method inaccessible. Reason:\n{0}"
  , /*  39 */ "Method throwed exception:\n{0}"
  , /*  40 */ "Client implementation not found. Reason:\n{0}"

  , /*  41 */ "Server Type \"{0}\" not supported"
  , /*  42 */ "URL Encoding \"{0}\" not supported"
  , /*  43 */ "Unable to establish connection. Reason:\n{0}"
  , /*  44 */ "Principal Name \"{0}\" or Password is incorrect, system failed to get access with supplied credentials"
  , /*  45 */ "Principal \"{0}\" has insufficient privileges to perform operation \"{1}\""
  , /*  46 */ "Unhandled exception occured:\n{0}."
  , /*  47 */ "Encounter some problems:\n{0}."
  , /*  48 */ "Execution aborted:\n{0}."

  , /*  49 */ "Identity Services"
  , /*  50 */ "IDE identity connections appear in both the Identity Navigator and the Resource Palette."
  , /*  51 */ "IT Resource Definitions"
  , /*  52 */ "???"
  , /*  53 */ "gallery/itresourcedefinition.png"
  , /*  54 */ "IT Resources"
  , /*  55 */ "???"
  , /*  56 */ "gallery/itresource.png"
  , /*  57 */ "Lookup Definition"
  , /*  58 */ "You use this category to create and manage lookup definitions. A lookup definition represents a lookup field and the values you can access from that lookup field."
  , /*  59 */ "gallery/lookup.png"
  , /*  60 */ "Adapter Factory"
  , /*  61 */ "???"
  , /*  62 */ "gallery/adapter.png"
  , /*  63 */ "Form Definitions"
  , /*  64 */ "???"
  , /*  65 */ "gallery/form.png"
  , /*  66 */ "Resource Objects"
  , /*  67 */ "???"
  , /*  68 */ "gallery/resource.png"
  , /*  69 */ "Process Definitions"
  , /*  70 */ "???"
  , /*  71 */ "gallery/process.png"
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
    return IconFactory.create(Bundle.class, string(key));
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
    return IconFactory.scaled(null, IconFactory.create(Bundle.class, string(key)), width, height);
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
    return IconFactory.create(Bundle.class, string(key));
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