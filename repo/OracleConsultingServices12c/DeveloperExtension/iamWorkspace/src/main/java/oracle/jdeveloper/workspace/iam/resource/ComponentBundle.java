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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   ComponentBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ComponentBundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Extended support for GUI elements
                                               rendering labels of Database and
                                               WebLogic environment properties
    11.1.1.3.37.60.28  2012-02-06  DSteding    Made the MDS storage of UI
                                               sandboxing configurable.
    11.1.1.3.37.60.63  2015-02-14  DSteding    Extended to support the custom
                                               content provider set for build
                                               and unit test
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import oracle.ide.util.ArrayResourceBundle;

import oracle.jdeveloper.workspace.iam.utility.IconFactory;

////////////////////////////////////////////////////////////////////////////////
// class ComponentBundle
// ~~~~~ ~~~~~~~~~~~~~~~
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
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ComponentBundle extends ArrayResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the translatable strings
   */
  public static final int VALIDATION_ERROR_TITLE          =   0;
  public static final int VALIDATION_WARNING_TITLE        =   1;
  public static final int IGNORE                          =   2;
  public static final int CANCEL                          =   3;
  public static final int CONTINUE                        =   4;

  public static final int FILE_TITLE                      =   5;
  public static final int FILE_ICON                       =   6;
  public static final int FILE_ACCELERATOR                =   7;

  public static final int DIRECTORY_TITLE                 =   8;
  public static final int DIRECTORY_ICON                  =   9;
  public static final int DIRECTORY_ACCELERATOR           =  10;

  public static final int COMMAND_NOTFOUND                =  11;

  public static final int TEMPALTE_TITLE                  =  12;
  public static final int TEMPALTE_MANDATORY              =  13;
  public static final int TEMPALTE_MISMATCHED_TYPE        =  14;
  public static final int TEMPALTE_MISMATCHED_PROJECT     =  15;
  public static final int TEMPALTE_INTERNAL_FAILED        =  16;
  public static final int TEMPALTE_EXTERNAL_FAILED        =  17;
  public static final int TEMPALTE_READING_ERROR          =  18;
  public static final int TEMPALTE_WRITING_ERROR          =  19;
  public static final int TEMPALTE_ENCODING_ERROR         =  20;

  public static final int CONFIG_CLONEFROM_LABEL          =  21;
  public static final int CONFIG_CLONEFROM_ICON           =  22;
  public static final int CONFIG_CLONEFROM_HINT           =  23;
  public static final int CONFIG_OVERRIDE_LABEL           =  24;

  public static final int SERVER_GENERAL_HEADER           =  25;
  public static final int SERVER_JAAS_CONFIG_HEADER       =  26;
  public static final int SERVER_ACCOUNT_HEADER           =  27;
  public static final int SERVER_CERTIFICAT_HEADER        =  28;
  public static final int SERVER_METADATA_HEADER          =  29;
  public static final int SERVER_MBEAN_HEADER             =  30;
  public static final int SERVER_DATABASE_HEADER          =  31;
  public static final int SERVER_APPLICATION_HEADER       =  32;

  public static final int SERVER_PLATFORM_LABEL           =  33;
  public static final int SERVER_PROTOCOL_LABEL           =  34;
  public static final int SERVER_HOST_LABEL               =  35;
  public static final int SERVER_HOST_ERROR               =  36;
  public static final int SERVER_PORT_LABEL               =  37;
  public static final int SERVER_PORT_ERROR               =  38;
  public static final int SERVER_PORT_FORMAT              =  39;
  public static final int SERVER_PORT_RANGE               =  40;
  public static final int SERVER_HOME_LABEL               =  41;
  public static final int SERVER_HOME_ERROR               =  42;
  public static final int SERVER_NAME_LABEL               =  43;
  public static final int SERVER_NAME_ERROR               =  44;
  public static final int SERVER_INSTANCE_LABEL           =  45;
  public static final int SERVER_INSTANCE_ERROR           =  46;
  public static final int SERVER_CONTEXT_LABEL            =  47;
  public static final int SERVER_CONTEXT_ERROR            =  48;
  public static final int SERVER_MODE_LABEL               =  49;
  public static final int SERVER_SERVICE_LABEL            =  50;
  public static final int SERVER_SERVICE_ERROR            =  51;
  public static final int SERVER_USERNAME_LABEL           =  52;
  public static final int SERVER_USERNAME_ERROR           =  53;
  public static final int SERVER_PASSWORD_LABEL           =  54;
  public static final int SERVER_PASSWORD_ERROR           =  55;
  public static final int SERVER_PASSWORD_CONFIRM_LABEL   =  56;
  public static final int SERVER_PASSWORD_CONFIRM_ERROR   =  57;
  public static final int SERVER_PASSWORD_MISMATCH        =  58;
  public static final int SERVER_CERIFICATE_LABEL         =  59;
  public static final int SERVER_CERIFICATE_HINT          =  60;
  public static final int SERVER_CERIFICATE_ERROR         =  61;
  public static final int SERVER_PASSPHRASE_LABEL         =  62;
  public static final int SERVER_PASSPHRASE_ERROR         =  63;
  public static final int SERVER_PASSPHRASE_CONFIRM_LABEL =  64;
  public static final int SERVER_PASSPHRASE_CONFIRM_ERROR =  65;
  public static final int SERVER_PASSPHRASE_MISMATCH      =  66;
  public static final int SERVER_CERIFICATE_TRUST         =  67;
  public static final int SERVER_JAAS_CONFIG_LABEL        =  68;
  public static final int SERVER_JAAS_CONFIG_HINT         =  69;
  public static final int SERVER_JAAS_CONFIG_ERROR        =  70;
  public static final int SERVER_CONFIG_PARTITION_LABEL   =  71;
  public static final int SERVER_CONFIG_PARTITION_ERROR   =  72;
  public static final int SERVER_SANDBOX_PARTITION_LABEL  =  73;
  public static final int SERVER_SANDBOX_PARTITION_ERROR  =  74;
  public static final int SERVER_MBEAN_METADATA_LABEL     =  75;
  public static final int SERVER_MBEAN_METADATA_ERROR     =  76;
  public static final int SERVER_MBEAN_APPLICATION_LABEL  =  77;
  public static final int SERVER_MBEAN_APPLICATION_ERROR  =  78;
  public static final int SERVER_MBEAN_VERSION_LABEL      =  79;
  public static final int SERVER_MBEAN_VERSION_ERROR      =  80;

  public static final int SERVER_TEST_CONNECTION          =  81;

  public static final int SERVICE_PLATFORM_LABEL          =  82;
  public static final int SERVICE_PROTOCOL_LABEL          =  83;
  public static final int SERVICE_HOST_LABEL              =  84;
  public static final int SERVICE_HOST_ERROR              =  85;
  public static final int SERVICE_PORT_LABEL              =  86;
  public static final int SERVICE_PORT_ERROR              =  87;
  public static final int SERVICE_PORT_FORMAT             =  88;
  public static final int SERVICE_PORT_RANGE              =  89;
  public static final int SERVICE_HOME_LABEL              =  90;
  public static final int SERVICE_HOME_ERROR              =  91;
  public static final int SERVICE_APPLICATION_LABEL       =  92;
  public static final int SERVICE_APPLICATION_ERROR       =  93;
  public static final int SERVICE_CONTEXT_LABEL           =  94;
  public static final int SERVICE_CONTEXT_ERROR           =  95;
  public static final int SERVICE_USERNAME_LABEL          =  96;
  public static final int SERVICE_USERNAME_ERROR          =  97;
  public static final int SERVICE_PASSWORD_LABEL          =  98;
  public static final int SERVICE_PASSWORD_ERROR          =  99;
  public static final int SERVICE_PASSWORD_CONFIRM_LABEL  = 100;
  public static final int SERVICE_PASSWORD_CONFIRM_ERROR  = 101;
  public static final int SERVICE_PASSWORD_MISMATCH       = 102;

  public static final int PLATFORM_LINUX                  = 103;
  public static final int PLATFORM_SOLARIS                = 104;
  public static final int PLATFORM_HPUX                   = 105;
  public static final int PLATFORM_AIX                    = 106;
  public static final int PLATFORM_WINDOWS                = 107;

  public static final int VENDOR_ORACLE                   = 108;
  public static final int VENDOR_IBM                      = 109;

  public static final int DIRECTORY_OUD                   = 110;
  public static final int DIRECTORY_OVD                   = 111;
  public static final int DIRECTORY_OID                   = 112;

  public static final int XML_TITLE                       = 113;
  public static final int XML_MANDATORY                   = 114;
  public static final int XML_WRITING_ERROR               = 115;
  public static final int XML_OUTPUT_ROOT                 = 116;
  public static final int XML_OUTPUT_NODE_NAME            = 117;
  public static final int XML_OUTPUT_NODE_INVALID         = 118;
  public static final int XML_OUTPUT_NODE_REMOVE          = 119;
  public static final int XML_OUTPUT_START_TAG            = 120;

  private static final String CONTENT[] = {
    /*   0 */ "Error: Validation of {0}"
  , /*   1 */ "Warning: Validation of {0}"
  , /*   2 */ "&Ignore"
  , /*   3 */ "&Cancel"
  , /*   4 */ "&Continue"

  , /*   5 */ "Select a file"
  , /*   6 */ "images/pick-file.png"
  , /*   7 */ "F"

  , /*   8 */ "Select a directory"
  , /*   9 */ "images/pick-folder.png"
  , /*  10 */ "D"

  , /*  11 */ "Action {0} not found."

  , /*  12 */ "Template Generator"
  , /*  13 */ "Template required"
  , /*  14 */ "Template must be of type \"{0}\""
  , /*  15 */ "Mismatched number of project templates from project wizard context objects."
  , /*  16 */ "Template \"{0}\" could not be loaded from resource stream."
  , /*  17 */ "Template \"{0}\" could not be loaded from file system."
  , /*  18 */ "Reading template failed. Reason:\n{0}"
  , /*  19 */ "Writing template failed. Reason:\n{0}"
  , /*  20 */ "Template Character Encoding failed. Reason:\n{0}"

  , /*  21 */ "Build configuration file"
  , /*  22 */ "images/pick-configuration.png"
  , /*  23 */ "Select to open the Choose File dialog, through which you can choose the location of a proper configured file as a source for cloning."
  , /*  24 */ "&Override"

  , /*  25 */ "Server"
  , /*  26 */ "Authentication Configuration"
  , /*  27 */ "Account"
  , /*  28 */ "Certificate"
  , /*  29 */ "Metadata Partition"
  , /*  30 */ "MBean Properties"
  , /*  31 */ "Database"
  , /*  32 */ "Application"

  , /*  33 */ "&Platform:"
  , /*  34 */ "P&rotocol:"
  , /*  35 */ "Ho&st:"
  , /*  36 */ "Server Host cannot be empty!"
  , /*  37 */ "&Port:"
  , /*  38 */ "Server Port cannot be empty!"
  , /*  39 */ "Server Port must be a valid number!"
  , /*  40 */ "Server Port must be between {0} and {1}!"
  , /*  41 */ "H&ome:"
  , /*  42 */ "Server Home must be a valid directory"
  , /*  43 */ "&Name:"
  , /*  44 */ "Server Name cannot be empty!"
  , /*  45 */ "&Instance:"
  , /*  46 */ "Server Instance cannot be empty!"
  , /*  47 */ "&Context:"
  , /*  48 */ "Server Context cannot be empty!"
  , /*  49 */ "&Production Deployment"
  , /*  50 */ "&Service:"
  , /*  51 */ "Server Service cannot be empty!"
  , /*  52 */ "&Username:"
  , /*  53 */ "Account Username cannot be empty!"
  , /*  54 */ "Pass&word:"
  , /*  55 */ "Account Password cannot be empty!"
  , /*  56 */ "&Confirmation:"
  , /*  57 */ "Account Password Confirmation cannot be empty!"
  , /*  58 */ "Account Password and Account Password Confirmation don't match!"
  , /*  59 */ "P&ath:"
  , /*  60 */ "Select to open the Choose File dialog, through which you can choose the location of a private key file for authentication purpose."
  , /*  61 */ "Certificate Path must be a valid file!"
  , /*  62 */ "Passph&rase:"
  , /*  63 */ "Certificate Passphrase cannot be empty!"
  , /*  64 */ "Confir&mation:"
  , /*  65 */ "Certificate Passphrase Confirmation cannot be empty!"
  , /*  66 */ "Certificate Passphrase and Certificate Passphrase Confirmation don't match!"
  , /*  67 */ "&Trusted"
  , /*  68 */ "&Location:"
  , /*  69 */ "Select to open the Choose File dialog, through which you can choose the location of the server authentication configuration."
  , /*  70 */ "Authentication Config Location must be a valid file!"
  , /*  71 */ "&Runtime:"
  , /*  72 */ "Metadata Runtime Partition cannot be empty!"
  , /*  73 */ "&Sandbox:"
  , /*  74 */ "Metadata Sandbox Partition cannot be empty!"
  , /*  75 */ "&Metadata:"
  , /*  76 */ "MBean Properties Metadata cannot be empty!"
  , /*  77 */ "&Application:"
  , /*  78 */ "MBean Properties Application cannot be empty!"
  , /*  79 */ "&Version:"
  , /*  80 */ "MBean Properties Version cannot be empty!"

  , /*  81 */ "Test &Connection"

  , /*  82 */ "&Platform:"
  , /*  83 */ "Pro&tocol:"
  , /*  84 */ "H&ost:"
  , /*  85 */ "Service Host cannot be empty!"
  , /*  86 */ "Port:"
  , /*  87 */ "Service Port cannot be empty!"
  , /*  88 */ "Service Port must be a valid number!"
  , /*  89 */ "Service Port must be between {0} and {1}!"
  , /*  90 */ "H&ome:"
  , /*  91 */ "Service Home must be a valid directory"
  , /*  92 */ "&Application:"
  , /*  93 */ "Service Application must be a valid directory"
  , /*  94 */ "&Context:"
  , /*  95 */ "Service Context must be a valid directory"
  , /*  96 */ "&Username:"
  , /*  97 */ "Service Username cannot be empty!"
  , /*  98 */ "Pass&word:"
  , /*  99 */ "Service Password cannot be empty!"
  , /* 100 */ "&Confirmation:"
  , /* 101 */ "Service Password Confirmation cannot be empty!"
  , /* 102 */ "Service Password and Service Password Confirmation don't match!"

  , /* 103 */ "images/platform-linux.png"
  , /* 104 */ "images/platform-solaris.png"
  , /* 105 */ "images/platform-hpux.png"
  , /* 106 */ "images/platform-aix.png"
  , /* 107 */ "images/platform-windows.png"

  , /* 108 */ "images/vendor-oracle.png"
  , /* 109 */ "images/vendor-ibm.png"

  , /* 110 */ "images/directory-oud.png"
  , /* 111 */ "images/directory-ovd.png"
  , /* 112 */ "images/directory-oid.png"

  , /* 113 */ "XML File Generator"
  , /* 114 */ "XML File required"
  , /* 115 */ "Writing XML file failed. Reason:\n{0}"
  , /* 116 */ "Root element required"
  , /* 117 */ "Name of a node connecot be null or empty"
  , /* 118 */ "Either value or child nodes are allowed only"
  , /* 119 */ "Cannot remove node \"%1$s\""
  , /* 120 */ "Start element required"
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ArrayResourceBundle RESOURCE = (ArrayResourceBundle)ResourceBundle.getBundle(
    ComponentBundle.class.getName()
  , Locale.getDefault()
  , ComponentBundle.class.getClassLoader()
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
    return IconFactory.create(ComponentBundle.class, string(key));
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
    return IconFactory.scaled(null, IconFactory.create(ComponentBundle.class, string(key)), width, height);
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