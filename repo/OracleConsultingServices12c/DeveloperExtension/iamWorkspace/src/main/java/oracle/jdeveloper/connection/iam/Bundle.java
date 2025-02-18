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

package oracle.jdeveloper.connection.iam;

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

  public static final int RESOURCE_FACTORY_SOURCE           =   0;

  public static final int METADATA_CREATE_TITLE             =   1;
  public static final int METADATA_CREATE_HEADER            =   2;
  public static final int METADATA_MODIFY_TITLE             =   3;
  public static final int METADATA_MODIFY_HEADER            =   4;
  public static final int METADATA_HEADER_IMAGE             =   5;

  public static final int METADATA_GENERAL_HEADER           =   6;
  public static final int METADATA_TEST_LABEL               =   7;
  public static final int METADATA_TEST_HINT                =   8;
  public static final int METADATA_TEST_ERROR               =   9;
  public static final int METADATA_TEST_FAILED              =  10;
  public static final int METADATA_TEST_SUCCESS             =  11;
  public static final int METADATA_TEST_WORKING             =  12;
  public static final int METADATA_TEST_MESSAGE             =  13;
  public static final int METADATA_TEST_CONNECTING          =  14;
  public static final int METADATA_TEST_CANCELLED           =  15;
  public static final int METADATA_TEST_AUTHENTICATION      =  16;

  public static final int METADATA_CONTEXT_LABEL            =  17;
  public static final int METADATA_CONTEXT_ERROR            =  18;
  public static final int METADATA_PARTITION_HEADER         =  19;
  public static final int METADATA_PARTITION_LABEL          =  20;
  public static final int METADATA_PARTITION_ERROR          =  21;
  public static final int METADATA_PACKAGE_ICON             =  22;
  public static final int METADATA_DOCUMENT_ICON            =  23;
  public static final int METADATA_DOCUMENT_ERROR           =  24;
  public static final int METADATA_DOCUMENT_CONFIRM_TITLE   =  25;
  public static final int METADATA_DOCUMENT_CONFIRM_MESSAGE =  26;

  public static final int FILESYSTEM_FILE_ERROR             =  27;
  public static final int FILESYSTEM_ICON_DIR               =  28;
  public static final int FILESYSTEM_ICON_JAR               =  29;
  public static final int FILESYSTEM_ICON_XML               =  30;
  public static final int FILESYSTEM_ICON_ZIP               =  31;

  public static final int DIRECTORY_NODE_LABEL              =  32;
  public static final int DIRECTORY_NODE_HINT               =  33;
  public static final int DIRECTORY_TOOLBAR_LABEL           =  34;
  public static final int DIRECTORY_CREATE_TITLE            =  35;
  public static final int DIRECTORY_CREATE_HEADER           =  36;
  public static final int DIRECTORY_MODIFY_TITLE            =  37;
  public static final int DIRECTORY_MODIFY_HEADER           =  38;
  public static final int DIRECTORY_HEADER_IMAGE            =  39;

  public static final int DIRECTORY_GENERAL_HEADER          =  40;
  public static final int DIRECTORY_TEST_LABEL              =  41;
  public static final int DIRECTORY_TEST_HINT               =  42;
  public static final int DIRECTORY_TEST_ERROR              =  43;
  public static final int DIRECTORY_TEST_FAILED             =  44;
  public static final int DIRECTORY_TEST_SUCCESS            =  45;
  public static final int DIRECTORY_TEST_WORKING            =  46;
  public static final int DIRECTORY_TEST_MESSAGE            =  47;
  public static final int DIRECTORY_TEST_CONNECTING         =  48;
  public static final int DIRECTORY_TEST_CANCELLED          =  49;
  public static final int DIRECTORY_TEST_AUTHENTICATION     =  50;

  public static final int DIRECTORY_CONTEXT_LABEL           =  51;
  public static final int DIRECTORY_CONTEXT_ERROR           =  52;
  public static final int DIRECTORY_SIMPLE_LABEL            =  53;
  public static final int DIRECTORY_GSSAPI_LABEL            =  54;
  public static final int DIRECTORY_SSL_LABEL               =  55;
  public static final int DIRECTORY_TLS_LABEL               =  56;
  public static final int DIRECTORY_SASL_LABEL              =  57;
  public static final int DIRECTORY_ANONYMOUS_LABEL         =  58;

  public static final int CONNECTION_LEAF_ICON              =  59;
  public static final int CONNECTION_BUSY_ICON              =  60;
  public static final int CONNECTION_BUSY_TEXT              =  61;
  public static final int CONNECTION_NAME_LABEL             =  62;
  public static final int CONNECTION_NAME_ERROR             =  63;
  public static final int CONNECTION_NAME_EXISTS            =  64;

  public static final int CONTEXT_ERROR_UNHANDLED           =  65;
  public static final int CONTEXT_ERROR_GENERAL             =  66;
  public static final int CONTEXT_ERROR_ABORT               =  67;
  public static final int CONTEXT_ERROR_SERVERTYPE          =  68;
  public static final int CONTEXT_ERROR_NETWORK_SECURITY    =  69;
  public static final int CONTEXT_ERROR_ENCODING            =  70;
  public static final int CONTEXT_ERROR_CONNECTION          =  71;
  public static final int CONTEXT_ERROR_UNKNOWN_HOST        =  72;
  public static final int CONTEXT_ERROR_UNAVAILABLE         =  73;
  public static final int CONTEXT_ERROR_AUTHENTICATION      =  74;
  public static final int CONTEXT_ERROR_ACCESS_DENIED       =  75;
  public static final int CONTEXT_NAME_NOT_FOUND            =  76;
  public static final int CONTEXT_OBJECT_NOT_EXISTS         =  77;
  public static final int CONTEXT_RESOURCE_NOT_EXISTS       =  78;
  public static final int CONTEXT_NAME_EXISTS               =  79;
  public static final int CONTEXT_OBJECT_EXISTS             =  80;
  public static final int CONTEXT_RESOURCE_EXISTS           =  81;
  public static final int CONTEXT_OPERATION_NOT_SUPPORTED   =  82;

  public static final int CERTIFICATE_EMPTY                 =  83;
  public static final int CERTIFICATE_REJECTED              =  84;
  public static final int CERTIFICATE_DIALOG_TITLE          =  85;
  public static final int CERTIFICATE_DIALOG_HEADER         =  86;
  public static final int CERTIFICATE_DIALOG_IMAGE          =  87;
  public static final int CERTIFICATE_ACCEPT_LABEL          =  88;
  public static final int CERTIFICATE_NOTACCEPT_LABEL       =  89;
  public static final int CERTIFICATE_TEXT_LABEL            =  90;
  public static final int CERTIFICATE_LIST_LABEL            =  91;
  public static final int CERTIFICATE_DESCRIPTION_LABEL     =  92;
  
  public static final int FILE_MISSING                      =  93;
  public static final int FILE_OVERRIDE                     =  94;
  public static final int FILE_ENCODING_TYPE                =  95;
  public static final int FILE_IS_DIRECTORY                 =  96;
  public static final int FILE_NOTWRITABLE                  =  97;
  public static final int FILE_NOTREADABLE                  =  98;

  public static final int LDIF_LINE                         =  99;
  public static final int LDIF_LINE_NOWHERE                 = 100;
  public static final int LDIF_UNEXPECTED                   = 101;
  public static final int LDIF_EXPECTING_OID                = 102;
  public static final int LDIF_EXPECTING_PREFIX             = 103;
  public static final int LDIF_EXPECTING_SEPARATOR          = 104;
  public static final int LDIF_EXPECTING_ATTRIBUTE          = 105;
  public static final int LDIF_EXPECTING_CRITICALITY        = 106;
  public static final int LDIF_CONSTRUCT_URL                = 107;
  public static final int LDIF_CONSTRUCT_STRING             = 108;
  public static final int LDIF_CHANGE_TYPE_UNKNOW           = 109;
  public static final int LDIF_CHANGE_TYPE_NOTSUPPORTED     = 110;
  public static final int LDIF_DELETE_OLDRDN                = 111;

  public static final int DSML_SEARCH_DESCRIPTOR            = 112; /* A search descriptor is required to perform this operation */
  public static final int DSML_LISTENER_ONLYONE             = 113; /* Only one listener supported */
  public static final int DSML_TAG_OPENING_NOT_RECOGNIZED   = 114; /* The opening tag {0} is not recognized in this context */
  public static final int DSML_TAG_CLOSING_NOT_RECOGNIZED   = 115; /* The closing tag {0} is not recognized in this context */
  public static final int DSML_UNEXPECTED_EVENT             = 116; /* Document error: An unexpected event occured */
  public static final int DSML_EXPECTIING_NAMESPACE         = 117; /* Document error: Expecting namespace declaration {0} */
  public static final int DSML_EXPECTIING_OPENING_TAG       = 118; /* Document error: Expecting opening tag {0}, found {1} instead */
  public static final int DSML_EXPECTIING_CLOSING_TAG       = 119; /* Document error: Expecting closing tag {0}, found {1} instead */
  public static final int DSML_EXPECTIING_ATTRIBUTE         = 120; /* Document error: The element {0} is missing required attribute {1} */
  public static final int DSML_EXPECTIING_ATTRIBUTE_ONCE    = 121; /* Document error: Entries can only have one dishtinguished name */
  public static final int DSML_VALUE_INVALID                = 122; /* Document error: Element {0} has invalid value {1} */
  public static final int DSML_ROOT_CLOSING_OUTSIDE         = 123; /* Document error: Closing tag {0} was not expected, the document root has been closed */
  public static final int DSML_ROOT_DOCUMENT_STILL_OPEN     = 124; /* Document error: The root element is still open at end of document */

  private static final String CONTENT[] = {
    /*   0 */ "Invalid Resource Factory Source"

  , /*   1 */ "Create Metadata Service Connection"
  , /*   2 */ "Configure a new connection to an installed Metadata Service."
  , /*   3 */ "Modify Metadata Service Connection"
  , /*   4 */ "Modify the details of the existing Metadata Service connection."
  , /*   5 */ "gallery/metadata-header.png"

  , /*   6 */ "Connection"
  , /*   7 */ "&Test"
  , /*   8 */ "Test Connection"
  , /*   9 */ "Test Failed. The Adapter could not establish the connection:\n"
  , /*  10 */ "Test Failed. The Adapter could not establish the connection.\nCheck network parameters or make sure that the required metadata server is reachable."
  , /*  11 */ "Success!"
  , /*  12 */ "Working..."
  , /*  13 */ "Connecting to {0}..."
  , /*  14 */ "Connecting..."
  , /*  15 */ "{0}\nTest Cancelled."
  , /*  16 */ "Test Failed. The Adapter could not establish the connection.\nCheck authentication parameters: Authentication method, Username, Password.\n"

  , /*  17 */ "&Service:"
  , /*  18 */ "Database Server Service must be entered"
  , /*  19 */ "Partition"
  , /*  20 */ "N&ame"
  , /*  21 */ "Partition Name must be selected"
  , /*  22 */ "gallery/metadata-package.png"
  , /*  23 */ "gallery/metadata-document.png"
  , /*  24 */ "Failure in xml validation w.r.t. xsd"
  , /*  25 */ "Resource Exists"
  , /*  26 */ "Are you sure you want to override \"{0}/{1}\"?"

  , /*  27 */ "Error transfering file {0}"
  , /*  28 */ "gallery/xmlfile.png"
  , /*  29 */ "gallery/xmlfile.png"
  , /*  30 */ "gallery/xmlfile.png"
  , /*  31 */ "gallery/xmlfile.png"

  , /*  32 */ "Directory Services"
  , /*  33 */ "IDE directory connections appear in both the Directory Navigator and the Resource Palette."
  , /*  34 */ "Di&rectory Services"
  , /*  35 */ "Create Directory Service Connection"
  , /*  36 */ "Configure a new connection to an installed Directory Service."
  , /*  37 */ "Modify Directory Service Connection"
  , /*  38 */ "Modify the details of the existing Directory Service connection."
  , /*  39 */ "gallery/directory-header.png"

  , /*  40 */ "Connection"
  , /*  41 */ "&Test"
  , /*  42 */ "Test Connection"
  , /*  43 */ "Test Failed. The Adapter could not establish the connection:\n"
  , /*  44 */ "Test Failed. The Adapter could not establish the connection.\nCheck network parameters or make sure that the required metadata server is reachable."
  , /*  45 */ "Success!"
  , /*  46 */ "Working..."
  , /*  47 */ "Connecting to {0}..."
  , /*  48 */ "Connecting..."
  , /*  49 */ "{0}\nTest Cancelled."
  , /*  50 */ "Test Failed. The Adapter could not establish the connection.\nCheck authentication parameters: Authentication method, Username, Password.\n"

  , /*  51 */ "&Base:"
  , /*  52 */ "Server Base must be entered"
  , /*  53 */ "Simple authentication"
  , /*  54 */ "GSS-API"
  , /*  55 */ "SSL"
  , /*  56 */ "TLS"
  , /*  57 */ "SASL"
  , /*  58 */ "Anonymous connection"

  , /*  59 */ "gallery/leaf.gif"
  , /*  60 */ "gallery/busy.gif"
  , /*  61 */ "... loading ..."
  , /*  62 */ "&Name:"
  , /*  63 */ "Connection Name cannot be empty!"
  , /*  64 */ "Connection with name {0} already exist!"

  , /*  65 */ "Unhandled exception occured:\n{0}."
  , /*  66 */ "Encounter some problems:\n{0}."
  , /*  67 */ "Execution aborted:\n{0}."
  , /*  68 */ "Server Type \"{0}\" not supported."
  , /*  69 */ "Only one of the two options SSL or TLS can be used to secure the network layer."
  , /*  70 */ "URL Encoding \"{0}\" not supported."
  , /*  71 */ "Unable to establish connection. Reason:\n{0}."
  , /*  72 */ "The IP address of host {0} could not be determined."
  , /*  73 */ "The problem may be with physical connectivity or Target System is not alive."
  , /*  74 */ "Principal Name \"{0}\" or Password is incorrect, system failed to get access with supplied credentials."
  , /*  75 */ "Principal \"{0}\" has insufficient privileges to perform operation \"{1}\"."
  , /*  76 */ "Name does not exists for \"{0}\""
  , /*  77 */ "Object does not exists for \"{0}\""
  , /*  78 */ "Resource does not exists for \"{0}\""
  , /*  79 */ "Name \"{0}\" already exists"
  , /*  80 */ "Object \"{0}\" already exists"
  , /*  81 */ "Resource \"{0}\" already exists"
  , /*  82 */ "Operation not supported by Directory Service."

  , /*  83 */ "Could not obtain server certificate chain."
  , /*  84 */ "Certificate was explicitly rejected by the user."
  , /*  85 */ "Network Security"
  , /*  86 */ "Confirm Certificate"
  , /*  87 */ "gallery/certificate-header.png"
  , /*  88 */ "&Accept"
  , /*  89 */ "&Decline"
  , /*  90 */ "Your computer cannot confirm the identity of {0}.\n\nThis could be an attempt by an unknow party to connect to your computer and access confidential information.\n\nIf you ar not sure if you could continue, contact your system administrator. Tell the Administrator the Oracle JDeveloper is prompting you to accept the {1} certificate."
  , /*  91 */ "Certificates"
  , /*  92 */ "Description"
              
  , /*  93 */ "Encountered problems to find file \"{0}\""
  , /*  94 */ "File \"{0}\" already exists in\n\"{1}\".\n\nOverride the existing file?"
  , /*  95 */ "Encoding Type \"{0}\" is not supported"
  , /*  96 */ "Specified file \"{0}\" is a directory!"
  , /*  97 */ "Specified file \"{0}\" is not writable!"
  , /*  98 */ "Specified file \"{0}\" is not readable!"

  , /*  99 */ "Line {1}: {0}"
  , /* 100 */ "Continuation out of nowhere"
  , /* 101 */ "Unexpected \"{0}\""
  , /* 102 */ "OID expected for control"
  , /* 103 */ "Expecting \"{0}\" as a separator in line {1}"
  , /* 104 */ "Expecting separator \"{0}\" is missing in line {1}"
  , /* 105 */ "Add operation needs a value for attribute \"{0}\""
  , /* 106 */ "Criticality for control must be true or false, not \"{0}\""
  , /* 107 */ "{0}: cannot construct URL \"{1}\""
  , /* 108 */ "{0}: cannot construct string \"{1}\""
  , /* 109 */ "Unknown Change Type \"{0}\""
  , /* 110 */ "Change Type \"{0}\" not supported"
  , /* 111 */ "Incorrect input for deleteOldRdn"

  , /* 112 */ "A search descriptor is required to perform this operation"
  , /* 113 */ "Only one listener supported"
  , /* 114 */ "Document error at line {0}, column {1}: The opening tag \"{2}\" is not recognized in this context."
  , /* 115 */ "Document error at line {0}, column {1}: The closing tag \"{2}\" is not recognized in this context."
  , /* 116 */ "Document error at line {0}, column {1}: The event \"{2}\" isn\'t expected."
  , /* 117 */ "Document error at line {0}, column {1}: Document error: Expecting namespace declaration \"{2}\""
  , /* 118 */ "Document error at line {0}, column {1}: Expecting opening tag \"{2}\", found \"%4$s\" instead."
  , /* 119 */ "Document error at line {0}, column {1}: Expecting closing tag \"{2}\", found \"%4$s\" instead."
  , /* 120 */ "Document error at line {0}, column {1}: The element \"{2}\" is missing required attribute \"%4$s\""
  , /* 121 */ "Document error at line {0}, column {1}: The element \"{2}\" can only have one attribute \"%4$s\""
  , /* 122 */ "Document error at line {0}, column {1}: Element \"{2}\" has invalid value \"%4$s\""
  , /* 123 */ "Document error at line {0}, column {1}: Closing tag \"{2}\" was not expected, the document root has been closed"
  , /* 124 */ "Document error at line {0}, column {1}: The root element is still open at end of document"
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