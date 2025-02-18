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

package oracle.jdeveloper.connection.iam.editor.ods.resource;

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
 ** The resources provided by this bundle using numeric references rather than
 ** string references, it requires less overhead and provides better performance
 ** than ListResourceBundle and PropertyResourceBundle.
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

  public static final int ACTION_EXCEPTION                        =   0;

  public static final int ICON_DIRECTORY                          =   1;
  public static final int ICON_SYNTAX                             =   2;
  public static final int ICON_OBJECT_CLASS                       =   3;
  public static final int ICON_ATTRIBUTE_TYPE                     =   4;
  public static final int ICON_MATCHING_RULE                      =   5;
  public static final int ICON_PASSWORD_POLICY                    =   6;
  public static final int ICON_ACCESS_CONTROL_INFORMATION         =   7;
  public static final int ICON_SORT_ASCENDING                     =   8;
  public static final int ICON_SORT_DESCENDING                    =   9;

  public static final int SCHEMA_OBJECT_PANEL_TITLE               =  10;
  public static final int SCHEMA_ATTRIBUTE_PANEL_TITLE            =  11;
  public static final int SCHEMA_SYNTAX_PANEL_TITLE               =  12;
  public static final int SCHEMA_MATCHING_RULE_PANEL_TITLE        =  13;

  public static final int ENTRY_ATTRIBUTE_PANEL_TITLE             =  14;
  public static final int ENTRY_POLICY_PANEL_TITLE                =  15;
  public static final int ENTRY_SECURITY_PANEL_TITLE              =  16;
  public static final int ENTRY_ATTRIBUTE_PANEL_HEADER            =  17;
  public static final int ENTRY_ATTRIBUTE_NAME_HEADER             =  18;
  public static final int ENTRY_ATTRIBUTE_VALUE_HEADER            =  19;

  public static final int ROOT_GENERAL_PAGE_TITLE                 =  20;
  public static final int ROOT_AUTHN_PAGE_TITLE                   =  21;
  public static final int ROOT_CONTROL_PAGE_TITLE                 =  22;
  public static final int ROOT_SUPPORT_PAGE_TITLE                 =  23;
  public static final int ROOT_GENERAL_PROPERTY_PANEL_TITLE       =  24;
  public static final int ROOT_GENERAL_VENDOR_NAME_LABEL          =  25;
  public static final int ROOT_GENERAL_VENDOR_VERSION_LABEL       =  26;

  public static final int ROOT_NAMING_CONTEXT_PANEL_TITLE         =  27;
  public static final int ROOT_NAMING_CONTEXT_PANEL_HEADER        =  28;
  public static final int ROOT_AUTHN_SCHEME_PANEL_TITLE           =  29;
  public static final int ROOT_AUTHN_SCHEME_PANEL_HEADER          =  30;
  public static final int ROOT_AUTHN_MECHANISM_PANEL_TITLE        =  31;
  public static final int ROOT_AUTHN_MECHANISM_PANEL_HEADER       =  32;
  public static final int ROOT_SUPPORTED_VERSION_PANEL_TITLE      =  33;
  public static final int ROOT_SUPPORTED_VERSION_PANEL_HEADER     =  34;
  public static final int ROOT_SUPPORTED_CONTROL_PANEL_TITLE      =  35;
  public static final int ROOT_SUPPORTED_CONTROL_PANEL_HEADER     =  36;
  public static final int ROOT_SUPPORTED_FEATURE_PANEL_TITLE      =  37;
  public static final int ROOT_SUPPORTED_FEATURE_PANEL_HEADER     =  38;
  public static final int ROOT_SUPPORTED_EXTENSION_PANEL_TITLE    =  39;
  public static final int ROOT_SUPPORTED_EXTENSION_PANEL_HEADER   =  40;

  public static final int SCHEMA_SYNTAX_OID_HEADER                =  41;
  public static final int SCHEMA_SYNTAX_DESCRIPTION_HEADER        =  42;

  public static final int SCHEMA_MATCHING_RULE_OID_HEADER         =  43;
  public static final int SCHEMA_MATCHING_RULE_NAME_HEADER        =  44;
  public static final int SCHEMA_MATCHING_RULE_DESCRIPTION_HEADER =  45;

  public static final int SCHEMA_OBJECTCLASS_BROWSE_HEADER        =  46;
  public static final int SCHEMA_OBJECTCLASS_DETAIL_HEADER        =  47;
  public static final int SCHEMA_OBJECTCLASS_LABEL_HEADER         =  48;
  public static final int SCHEMA_OBJECTCLASS_VALUE_HEADER         =  49;
  public static final int SCHEMA_OBJECTCLASS_OID_LABEL            =  50;
  public static final int SCHEMA_OBJECTCLASS_NAME_LABEL           =  51;
  public static final int SCHEMA_OBJECTCLASS_KIND_LABEL           =  52;
  public static final int SCHEMA_OBJECTCLASS_DESCRIPTION_LABEL    =  53;
  public static final int SCHEMA_OBJECTCLASS_SUPERIROR_LABEL      =  54;
  public static final int SCHEMA_OBJECTCLASS_MUST_LABEL           =  55;
  public static final int SCHEMA_OBJECTCLASS_MAY_LABEL            =  56;

  public static final int SCHEMA_OBJECTCLASS_KIND_ABSTRACT        =  57;
  public static final int SCHEMA_OBJECTCLASS_KIND_STRUCTURAL      =  58;
  public static final int SCHEMA_OBJECTCLASS_KIND_AUXILIARY       =  59;
  public static final int SCHEMA_OBJECTCLASS_KIND_UNKNOWN         =  60;

  public static final int SCHEMA_ATTRIBUTE_BROWSE_HEADER          =  61;
  public static final int SCHEMA_ATTRIBUTE_DETAIL_HEADER          =  62;
  public static final int SCHEMA_ATTRIBUTE_BOOLEAN_TRUE           =  63;
  public static final int SCHEMA_ATTRIBUTE_BOOLEAN_FALSE          =  64;
  public static final int SCHEMA_ATTRIBUTE_LABEL_HEADER           =  65;
  public static final int SCHEMA_ATTRIBUTE_VALUE_HEADER           =  66;
  public static final int SCHEMA_ATTRIBUTE_OID_LABEL              =  67;
  public static final int SCHEMA_ATTRIBUTE_NAME_LABEL             =  68;
  public static final int SCHEMA_ATTRIBUTE_DESCRIPTION_LABEL      =  69;
  public static final int SCHEMA_ATTRIBUTE_SUPERIROR_LABEL        =  70;
  public static final int SCHEMA_ATTRIBUTE_SYNTAX_LABEL           =  71;
  public static final int SCHEMA_ATTRIBUTE_EQUALITY_LABEL         =  72;
  public static final int SCHEMA_ATTRIBUTE_ORDERING_LABEL         =  73;
  public static final int SCHEMA_ATTRIBUTE_SUBSTRING_LABEL        =  74;
  public static final int SCHEMA_ATTRIBUTE_MULTIVALUED_LABEL      =  75;
  public static final int SCHEMA_ATTRIBUTE_COLLECTIVE_LABEL       =  76;
  public static final int SCHEMA_ATTRIBUTE_OBSOLETE_LABEL         =  77;
  public static final int SCHEMA_ATTRIBUTE_READONLY_LABEL         =  78;
  public static final int SCHEMA_ATTRIBUTE_CREATEABLE_LABEL       =  79;
  public static final int SCHEMA_ATTRIBUTE_MODIFYABLE_LABEL       =  80;
  public static final int SCHEMA_ATTRIBUTE_OPERATIONAL_LABEL      =  81;

  public static final int EDITOR_ACTION_FREEZE_ICON               =  82;
  public static final int EDITOR_ACTION_FROZEN_ICON               =  83;

  public static final int ENTRY_ICON_ROOT                         =  84;
  public static final int ENTRY_ICON_BASE                         =  85;
  public static final int ENTRY_ICON_USER                         =  86;
  public static final int ENTRY_ICON_GROUP                        =  87;
  public static final int ENTRY_ICON_DOMAIN                       =  88;
  public static final int ENTRY_ICON_COUNTRY                      =  89;
  public static final int ENTRY_ICON_LOCATION                     =  90;
  public static final int ENTRY_ICON_CONTAINER                    =  91;
  public static final int ENTRY_ICON_ORGANIZATION                 =  92;
  public static final int ENTRY_ICON_ORGANIZATIONUNIT             =  93;

  public static final int ENTRY_ACTION_ENTRY_LABEL                =  94;
  public static final int ENTRY_ACTION_ENTRY_FETCH                =  95;

  public static final int RESOURCE_REMOVE_TITLE                   =  96;
  public static final int RESOURCE_REMOVE_MESSAGE                 =  97;
  public static final int RESOURCE_REMOVE_SUCCESS                 =  98;
  public static final int RESOURCE_REMOVE_FAILED                  =  99;

  public static final int ENTRY_DIALOG_IMAGE                      = 100;

  public static final int ENTRY_REFRESH_TITLE                     = 101;
  public static final int ENTRY_REFRESH_CONFIRM                   = 102;

  public static final int ENTRY_CREATE_TITLE                      = 103;
  public static final int ENTRY_CREATE_HEADER                     = 104;
  public static final int ENTRY_CREATE_PREFIX_REQUIRED            = 105;
  public static final int ENTRY_CREATE_SUFFIX_REQUIRED            = 106;
  public static final int ENTRY_CREATE_SUCCESS                    = 107;
  public static final int ENTRY_CREATE_FAILED                     = 108;

  public static final int ENTRY_MODIFY_TITLE                      = 109;
  public static final int ENTRY_MODIFY_HEADER                     = 110;
  public static final int ENTRY_MODIFY_SUCCESS                    = 111;
  public static final int ENTRY_MODIFY_FAILED                     = 112;

  public static final int ENTRY_DELETE_TITLE                      = 113;
  public static final int ENTRY_DELETE_SINGLE                     = 114;
  public static final int ENTRY_DELETE_MULTIPLE                   = 115;
  public static final int ENTRY_DELETED_SINGLE                    = 116;
  public static final int ENTRY_DELETED_MULTIPLE                  = 117;
  public static final int ENTRY_DELETE_FAILED                     = 118;

  public static final int ENTRY_RENAME_TITLE                      = 119;
  public static final int ENTRY_RENAME_HEADER                     = 120;
  public static final int ENTRY_RENAME_NAME_INVALID               = 121;
  public static final int ENTRY_RENAME_PREFIX_REQUIRED            = 122;
  public static final int ENTRY_RENAME_SUFFIX_REQUIRED            = 123;
  public static final int ENTRY_RENAME_SUCCESS                    = 124;
  public static final int ENTRY_RENAME_FAILED                     = 125;

  public static final int ENTRY_MOVE_TITLE                        = 126;
  public static final int ENTRY_MOVE_HEADER                       = 127;
  public static final int ENTRY_MOVE_NAME_INVALID                 = 128;
  public static final int ENTRY_MOVE_PREFIX_REQUIRED              = 129;
  public static final int ENTRY_MOVE_SUFFIX_REQUIRED              = 130;
  public static final int ENTRY_MOVE_SUCCESS                      = 131;
  public static final int ENTRY_MOVE_FAILED                       = 132;

  public static final int ENTRY_PREFIX_LABEL                      = 133;
  public static final int ENTRY_SUFFIX_LABEL                      = 134;
  public static final int ENTRY_ORIGIN_LABEL                      = 135;
  public static final int ENTRY_TARGET_LABEL                      = 136;
  public static final int ENTRY_OBJECT_LABEL                      = 137;
  public static final int ENTRY_EXTENSIBLE_LABEL                  = 138;

  public static final int FILE_FORMAT_LABEL                       = 139;
  public static final int FILE_FORMAT_LDIF                        = 140;
  public static final int FILE_FORMAT_XML1                        = 141;
  public static final int FILE_FORMAT_XML2                        = 142;
  public static final int FILE_FORMAT_JSON                        = 143;
  
  public static final int EXPORT_TITLE                            = 144;
  public static final int EXPORT_HEADER                           = 145;
  public static final int EXPORT_COMPLETE                         = 146;
  public static final int EXPORT_FILTER_FILE                      = 147;
  public static final int EXPORT_FILENAME_TITLE                   = 148;
  public static final int EXPORT_FILENAME_LABEL                   = 149;
  public static final int EXPORT_FILENAME_HINT                    = 150;
  public static final int EXPORT_FILENAME_REQUIRED                = 151;
  public static final int EXPORT_CONTEXT_LABEL                    = 152;
  public static final int EXPORT_SUBENTRY_LABEL                   = 153;
  public static final int EXPORT_OPERATIONAL_LABEL                = 154;
  public static final int EXPORT_ATTRIBUTE_LABEL                  = 155;

  public static final int IMPORT_TITLE                            = 156;
  public static final int IMPORT_HEADER                           = 157;
  public static final int IMPORT_COMPLETE                         = 158;
  public static final int IMPORT_FILTER_FILE                      = 159;
  public static final int IMPORT_FILTER_ERROR                     = 160;
  public static final int IMPORT_CONTEXT_LABEL                    = 161;
  public static final int IMPORT_STOP_LABEL                       = 162;
  public static final int IMPORT_FILENAME_TITLE                   = 163;
  public static final int IMPORT_FILENAME_LABEL                   = 164;
  public static final int IMPORT_FILENAME_HINT                    = 165;
  public static final int IMPORT_FILENAME_REQUIRED                = 166;
  public static final int IMPORT_FILEERROR_TITLE                  = 167;
  public static final int IMPORT_FILEERROR_LABEL                  = 168;
  public static final int IMPORT_FILEERROR_HINT                   = 169;
  public static final int IMPORT_FILEERROR_REQUIRED               = 170;
  public static final int IMPORT_FILEERROR_CONFLICT               = 171;

  private static final String CONTENT[] = {
    /*   0 */ "exception.png"

  , /*   1 */ "directory.png"
  , /*   2 */ "syntax.png"
  , /*   3 */ "object-class.png"
  , /*   4 */ "attribute-type.png"
  , /*   5 */ "matching-rule.png"
  , /*   6 */ "password-policy.png"
  , /*   7 */ "aci.png"
  , /*   8 */ "sort-ascending.gif"
  , /*   9 */ "sort-descending.gif"

  , /*  10 */ "Object Classes"
  , /*  11 */ "Attribute Types"
  , /*  12 */ "Syntaxes"
  , /*  13 */ "Matching Rules"

  , /*  14 */ "Attributes"
  , /*  15 */ "Password Policies"
  , /*  16 */ "Access Controls"
  , /*  17 */ "Attribute"
  , /*  18 */ "Name"
  , /*  19 */ "Value"

  , /*  20 */ "General"
  , /*  21 */ "Authentication"
  , /*  22 */ "Control"
  , /*  23 */ "Extension"
  , /*  24 */ "General"
  , /*  25 */ "Vendor Name:"
  , /*  26 */ "Vendor Version:"
  , /*  27 */ "Naming Contexts"
  , /*  28 */ "Naming Context"
  , /*  29 */ "Authentication Password Storage Schemes"
  , /*  30 */ "Password Storage Schema"
  , /*  31 */ "Authentication SASL Mechanisms"
  , /*  32 */ "SASL Mechanism"
  , /*  33 */ "LDAP Versions"
  , /*  34 */ "Version"
  , /*  35 */ "Supported Controls"
  , /*  36 */ "Control"
  , /*  37 */ "Supported Features"
  , /*  38 */ "Feature"
  , /*  39 */ "Supported Extension"
  , /*  40 */ "Extension"

  , /*  41 */ "OID"
  , /*  42 */ "Description"

  , /*  43 */ "OID"
  , /*  44 */ "Name"
  , /*  45 */ "Description"

  , /*  46 */ "Object Class"
  , /*  47 */ "Properties"
  , /*  48 */ "Property"
  , /*  49 */ "Value"
  , /*  50 */ "OID"
  , /*  51 */ "Name"
  , /*  52 */ "Kind"
  , /*  53 */ "Description"
  , /*  54 */ "Superiror"
  , /*  55 */ "Must"
  , /*  56 */ "May"

  , /*  57 */ "Abstrcat"
  , /*  58 */ "Structural"
  , /*  59 */ "Auxiliary"
  , /*  60 */ "Unknown"

  , /*  61 */ "Attribute Type"
  , /*  62 */ "Properties"
  , /*  63 */ "Yes"
  , /*  64 */ "No"
  , /*  65 */ "Property"
  , /*  66 */ "Value"
  , /*  67 */ "OID"
  , /*  68 */ "Name"
  , /*  69 */ "Description"
  , /*  70 */ "Superiror"
  , /*  71 */ "Syntax"
  , /*  72 */ "Equality"
  , /*  73 */ "Ordering"
  , /*  74 */ "Substring"
  , /*  75 */ "Multi-Valued"
  , /*  76 */ "Collective"
  , /*  77 */ "Obsolete"
  , /*  78 */ "Readonly"
  , /*  79 */ "Creatable"
  , /*  80 */ "Modifyable"
  , /*  81 */ "Operational"

  , /*  82 */ "action-freeze.png"
  , /*  83 */ "action-frozen.png"

  , /*  84 */ "root.gif"
  , /*  85 */ "base.png"
  , /*  86 */ "user.png"
  , /*  87 */ "group.png"
  , /*  88 */ "domain.png"
  , /*  89 */ "location.png"
  , /*  90 */ "location.png"
  , /*  91 */ "context.png"
  , /*  92 */ "organization.png"
  , /*  93 */ "organizationunit.png"

  , /*  94 */ "Directory Entry"
  , /*  95 */ "Retrieving Directory Entry failed. {0}"

  , /*  96 */ "Remove Connection"
  , /*  97 */ "Are you sure you want to remove \"{0}\" from this catalog?"
  , /*  98 */ "Removed \"{0}\" successfully."
  , /*  99 */ "Error removing the connection. {0}"

  , /* 100 */ "/oracle/jdeveloper/connection/iam/gallery/directory-header.png"

  , /* 101 */ "Refresh Entry"
  , /* 102 */ "Are you sure you want to refresh \"{0}\"?"

  , /* 103 */ "Create Entry"
  , /* 104 */ "Create a new entry in the connected Directory Service."
  , /* 105 */ "A relative distinguished name is required!"
  , /* 106 */ "A context distinguished name is required!"
  , /* 107 */ "Entry \"{1}\" created successfully in \"{0}\"."
  , /* 108 */ "Error creating the entry \"{0}\" in \"{1}\".\n\n{2}"

  , /* 109 */ "Modify Entry"
  , /* 110 */ "Modify the selected entry in the connected Directory Service."
  , /* 111 */ "Entry \"{0}\" modified successfully."
  , /* 112 */ "Error modifying the entry \"{0}\" in \"{1}\".\n\n{2}"

  , /* 113 */ "Delete Entry"
  , /* 114 */ "Are you sure you want to delete \"{0}\"?"
  , /* 115 */ "Are you sure you want to delete selected entries?"
  , /* 116 */ "Entry \"{0}\" deleted successfully."
  , /* 117 */ "Entries deleted successfully."
  , /* 118 */ "Error deleting the entry. {0}"

  , /* 119 */ "Rename Entry"
  , /* 120 */ "Rename the selected entry in the connected Directory Service."
  , /* 121 */ "{0} is not a valid name to rename directory entry!"
  , /* 122 */ "A RDN is required to rename an entry!"
  , /* 123 */ "A suffix is required to rename an entry!"
  , /* 124 */ "Entry \"{0}\" renamed successfully to \"{1}\"."
  , /* 125 */ "Error renaming the entry. {0}"

  , /* 126 */ "Move Entry"
  , /* 127 */ "Move around the selected entry in the connected Directory Service."
  , /* 128 */ "{0} is not a valid name to move a directory entry!"
  , /* 129 */ "A RDN is required to move an entry!"
  , /* 130 */ "A suffix is required to move an entry!"
  , /* 131 */ "Entry moved successfully from \"{0}\" to \"{1}\"."
  , /* 132 */ "Error moving the entry. {0}"

  , /* 133 */ "&Rdn:"
  , /* 134 */ "&In:"
  , /* 135 */ "&From:"
  , /* 136 */ "&To:"
  , /* 137 */ "&Objectclass"
  , /* 138 */ "<<new>>"

  , /* 139 */ "Format:"
  , /* 140 */ "&LDIF"
  , /* 141 */ "DSML V&1"
  , /* 142 */ "DSML V&2"
  , /* 143 */ "&JSON"

  , /* 144 */ "Export Data"
  , /* 145 */ "Export data from a Directory Service to a file."
  , /* 146 */ "{0} entries exported from Directory Service to {1}."
  , /* 147 */ "Export Data file"            
  , /* 148 */ "Export File"
  , /* 149 */ "&Filename:"
  , /* 150 */ "Choose the file that receive the exported data."
  , /* 151 */ "A filename is required to export data."
  , /* 152 */ "Export:"
  , /* 153 */ "&SubEntries"
  , /* 154 */ "&Operational Attributes"
  , /* 155 */ "&Attributes Names only"

  , /* 156 */ "Import Data"
  , /* 157 */ "Import data into a Directory Service from a file."
  , /* 158 */ "{0} entries imported into Directory Service from {1}."
  , /* 159 */ "Import Data file"
  , /* 160 */ "Error Report file"
  , /* 161 */ "Into:"
  , /* 162 */ "Stop on &errors"
  , /* 163 */ "Import File"
  , /* 164 */ "&Filename:"
  , /* 165 */ "Choose the file provides the data to import."
  , /* 166 */ "A filename is required to import data."
  , /* 167 */ "Error File"
  , /* 168 */ "&Record errors:"
  , /* 169 */ "Choose the file that receive the collected errors."
  , /* 170 */ "A filename is required to recored errors."
  , /* 171 */ "The import file cannot be the same as the one used to record errors."
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