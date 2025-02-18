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
    Subsystem   :   Identity Manager Facility

    File        :   Bundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    Extended support for GUI elements
                                               rendering deployment destinations
    11.1.1.3.37.60.34  2012-02-06  DSteding    Extended support for R2
                                               deployments
    11.1.1.3.37.60.37  2012-02-06  DSteding    Extended support for R2
                                               customization projects
    11.1.1.3.37.60.39  2012-07-27  DSteding    Extended support for UI
                                               customization assembly projects
    11.1.1.3.37.60.43  2013-10-23  DSteding    Extended support for ANT
                                               runtime classpath configuration
    11.1.1.3.37.60.66  2015-09-12  DSteding    Extended support for Library
                                               projects
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim;

import java.util.Locale;
import java.util.ResourceBundle;

import oracle.ide.util.ArrayResourceBundle;

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
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public class Bundle extends ArrayResourceBundle {

  /**
   ** the translatable strings
   */
  public static final int     CONFIGURE_MAVEN_HEADER      =   0;
  public static final int     CONFIGURE_MAVEN_TEXT        =   1;
  public static final int     CONFIGURE_RUNTIME_HEADER    =   2;
  public static final int     CONFIGURE_RUNTIME_TEXT      =   3;
  public static final int     GENERAL_HEADER              =   4;
  public static final int     WORKSPACE_FOLDER            =   5;
  public static final int     WORKSPACE_FAILURE           =   6;
  public static final int     CONSOLE_HOME                =   7;
  public static final int     CONSOLE_HINT                =   8;
  public static final int     CONSOLE_FAILURE             =   9;
  public static final int     FRAMEWORK_HOME              =  10;
  public static final int     FRAMEWORK_HINT              =  11;
  public static final int     FRAMEWORK_FAILURE           =  12;
  public static final int     LIBRARIES_HEADER            =  13;
  public static final int     LIBRARIES_RELEASE           =  14;
  public static final int     LIBRARIES_GENERATE          =  15;
  public static final int     LIBRARIES_MANAGE            =  16;
  public static final int     LIBRARIES_MAVEN             =  17;

  public static final int     PROPERTY_HEADER             =  18;
  public static final int     FILE_LABEL                  =  19;
  public static final int     FILE_ERROR                  =  20;
  public static final int     PROJECT_LABEL               =  21;
  public static final int     PROJECT_ERROR               =  22;
  public static final int     DESCRIPTION_LABEL           =  23;
  public static final int     DESCRIPTION_ERROR           =  24;
  public static final int     APPLICATION_LABEL           =  25;
  public static final int     APPLICATION_ERROR           =  26;
  public static final int     DESTINATION_HEADER          =  27;
  public static final int     DESTINATION_BASE_LABEL      =  28;
  public static final int     DESTINATION_BASE_HINT       =  29;
  public static final int     DESTINATION_BASE_ERROR      =  30;
  public static final int     DESTINATION_TARGET_LABEL    =  31;
  public static final int     DESTINATION_TARGET_HINT     =  32;
  public static final int     DESTINATION_TARGET_ERROR    =  33;
  public static final int     DESTINATION_TRUSTED_LABEL   =  34;
  public static final int     DESTINATION_TRUSTED_HINT    =  35;
  public static final int     DESTINATION_TRUSTED_ERROR   =  36;

  public static final int     ADAPTER_HEADER              =  37;
  public static final int     ADAPTER_LIBRARY_LABEL       =  38;
  public static final int     ADAPTER_LIBRARY_ERROR       =  39;
  public static final int     ADAPTER_PACKAGE_LABEL       =  40;
  public static final int     ADAPTER_PACKAGE_ERROR       =  41;

  public static final int     SCHEDULER_HEADER            =  42;
  public static final int     SCHEDULER_LIBRARY_LABEL     =  43;
  public static final int     SCHEDULER_LIBRARY_ERROR     =  44;
  public static final int     SCHEDULER_PACKAGE_LABEL     =  45;
  public static final int     SCHEDULER_PACKAGE_ERROR     =  46;

  public static final int     DIAGNOSTIC_HEADER           =  47;
  public static final int     DIAGNOSTIC_LIBRARY_LABEL    =  48;
  public static final int     DIAGNOSTIC_LIBRARY_ERROR    =  49;
  public static final int     DIAGNOSTIC_PACKAGE_LABEL    =  50;
  public static final int     DIAGNOSTIC_PACKAGE_ERROR    =  51;

  public static final int     PLUGIN_HEADER               =  52;
  public static final int     PLUGIN_LIBRARY_LABEL        =  53;
  public static final int     PLUGIN_LIBRARY_ERROR        =  54;
  public static final int     PLUGIN_PACKAGE_LABEL        =  55;
  public static final int     PLUGIN_PACKAGE_ERROR        =  56;
  public static final int     PLUGIN_DESCRIPTOR_HEADER    =  57;
  public static final int     PLUGIN_REGISTRY_LABEL       =  58;
  public static final int     PLUGIN_REGISTRY_ERROR       =  59;
  public static final int     PLUGIN_HANDLER_LABEL        =  60;
  public static final int     PLUGIN_HANDLER_ERROR        =  61;

  public static final int     COMMON_HEADER               =  62;
  public static final int     COMMON_LIBRARY_HEADER       =  63;
  public static final int     COMMON_LIBRARY_LABEL        =  64;
  public static final int     COMMON_LIBRARY_ERROR        =  65;
  public static final int     COMMON_PACKAGE_LABEL        =  66;
  public static final int     COMMON_PACKAGE_ERROR        =  67;

  public static final int     CONFIGURATION_BASE_LABEL    =  68;
  public static final int     CONFIGURATION_TARGET_LABEL  =  69;
  public static final int     CONFIGURATION_TUSTED_LABEL  =  70;

  public static final int     DEPLOYMENT_HEADER           =  71;
  public static final int     DEPLOYMENT_FOLDER_LABEL     =  72;
  public static final int     DEPLOYMENT_FOLDER_HINT      =  73;
  public static final int     DEPLOYMENT_FOLDER_ERROR     =  74;
  public static final int     DEPLOYMENT_LIBRARY_LABEL    =  75;
  public static final int     DEPLOYMENT_LIBRARY_ERROR    =  76;

  public static final int     FRONTEND_HEADER             =  77;
  public static final int     FRONTEND_FOLDER_LABEL       =  78;
  public static final int     FRONTEND_FOLDER_HINT        =  79;
  public static final int     FRONTEND_FOLDER_ERROR       =  80;
  public static final int     FRONTEND_LIBRARY_LABEL      =  81;
  public static final int     FRONTEND_LIBRARY_ERROR      =  82;
  public static final int     FRONTEND_PACKAGE_LABEL      =  83;
  public static final int     FRONTEND_PACKAGE_ERROR      =  84;
  public static final int     APPLICATION_ARCHIVE_LABEL   =  85;
  public static final int     APPLICATION_ARCHIVE_ERROR   =  86;

  public static final int     BACKEND_HEADER              =  87;
  public static final int     BACKEND_LIBRARY_LABEL       =  88;
  public static final int     BACKEND_LIBRARY_ERROR       =  89;
  public static final int     BACKEND_PACKAGE_LABEL       =  90;
  public static final int     BACKEND_PACKAGE_ERROR       =  91;

  public static final int     ASSEMBLY_HEADER             =  92;
  public static final int     ASSEMBLY_FOLDER_LABEL       =  93;
  public static final int     ASSEMBLY_FOLDER_HINT        =  94;
  public static final int     ASSEMBLY_FOLDER_ERROR       =  95;
  public static final int     ASSEMBLY_ARCHIVE_LABEL      =  96;
  public static final int     ASSEMBLY_ARCHIVE_ERROR      =  97;
  public static final int     ASSEMBLY_INCLUDE_HEADER     =  98;
  public static final int     ASSEMBLY_BACKEND_LABEL      =  99;
  public static final int     ASSEMBLY_BACKEND_HINT       = 100;
  public static final int     ASSEMBLY_BACKEND_ERROR      = 101;
  public static final int     ASSEMBLY_FRONTEND_LABEL     = 102;
  public static final int     ASSEMBLY_FRONTEND_HINT      = 103;
  public static final int     ASSEMBLY_FRONTEND_ERROR     = 104;

  public static final int     CONTEXT_HEADER              = 105;
  public static final int     CONTEXT_FILE_LABEL          = 106;
  public static final int     CONTEXT_FILE_ERROR          = 107;
  public static final int     CONTEXT_PROJECT_LABEL       = 108;
  public static final int     CONTEXT_PROJECT_ERROR       = 109;
  public static final int     CONTEXT_TARGET_LABEL        = 110;
  public static final int     CONTEXT_TARGET_ERROR        = 111;

  public static final int     EXPORT_HEADER               = 112;
  public static final int     EXPORT_FILE_LABEL           = 113;
  public static final int     EXPORT_FILE_ERROR           = 114;
  public static final int     EXPORT_PROJECT_LABEL        = 115;
  public static final int     EXPORT_PROJECT_ERROR        = 116;
  public static final int     EXPORT_TARGET_LABEL         = 117;
  public static final int     EXPORT_TARGET_ERROR         = 118;
  public static final int     EXPORT_DESCRIPTION_LABEL    = 119;
  public static final int     EXPORT_DESCRIPTION_ERROR    = 120;

  public static final int     IMPORT_HEADER               = 121;
  public static final int     IMPORT_FILE_LABEL           = 122;
  public static final int     IMPORT_FILE_ERROR           = 123;
  public static final int     IMPORT_PROJECT_LABEL        = 124;
  public static final int     IMPORT_PROJECT_ERROR        = 125;
  public static final int     IMPORT_TARGET_LABEL         = 126;
  public static final int     IMPORT_TARGET_ERROR         = 127;
  public static final int     IMPORT_DESCRIPTION_LABEL    = 128;
  public static final int     IMPORT_DESCRIPTION_ERROR    = 129;

  public static final int     REQUEST_HEADER              = 130;
  public static final int     REQUEST_FILE_LABEL          = 131;
  public static final int     REQUEST_FILE_ERROR          = 132;
  public static final int     REQUEST_PROJECT_LABEL       = 133;
  public static final int     REQUEST_PROJECT_ERROR       = 134;
  public static final int     REQUEST_TARGET_LABEL        = 135;
  public static final int     REQUEST_TARGET_ERROR        = 136;
  public static final int     REQUEST_DESCRIPTION_LABEL   = 137;
  public static final int     REQUEST_DESCRIPTION_ERROR   = 138;

  public static final int     WORKFLOW_HEADER             = 139;
  public static final int     WORKFLOW_SERVICE_LABEL      = 140;
  public static final int     WORKFLOW_SERVICE_ERROR      = 141;
  public static final int     WORKFLOW_NAME_LABEL         = 142;
  public static final int     WORKFLOW_NAME_ERROR         = 143;
  public static final int     WORKFLOW_NAME_VALID         = 144;

  public static final int     COMPOSITE_HEADER            = 145;
  public static final int     COMPOSITE_BASE_LABEL        = 146;
  public static final int     COMPOSITE_BASE_HINT         = 147;
  public static final int     COMPOSITE_BASE_ERROR        = 148;
  public static final int     COMPOSITE_PACKAGE_LABEL     = 149;
  public static final int     COMPOSITE_PACKAGE_ERROR     = 150;

  public static final int     COMPOSITE_PARTITION_LABEL   = 151;
  public static final int     COMPOSITE_PARTITION_ERROR   = 152;
  public static final int     COMPOSITE_REVISION_LABEL    = 153;
  public static final int     COMPOSITE_REVISION_ERROR    = 154;
  public static final int     COMPOSITE_CATEGORY_LABEL    = 155;
  public static final int     COMPOSITE_CATEGORY_ERROR    = 156;
  public static final int     COMPOSITE_PROVIDER_LABEL    = 157;
  public static final int     COMPOSITE_PROVIDER_ERROR    = 158;
  public static final int     COMPOSITE_OPERATION_LABEL   = 159;
  public static final int     COMPOSITE_OPERATION_ERROR   = 160;
  public static final int     COMPOSITE_PAYLOAD_LABEL     = 161;
  public static final int     COMPOSITE_PAYLOAD_ERROR     = 162;

  public static final int     CONTENT_DEPLOYMENT_CATEGORY = 163;
  public static final int     CONTENT_DEPLOYMENT_HEADER   = 164;
  public static final int     CONTENT_DEPLOYMENT_LABEL    = 165;
  public static final int     CONTENT_DEPLOYMENT_HINT     = 166;

  private static final String CONTENT[] = {
    /*   0 */ "Do you realy want to regenerate the Maven Project Object Model build hierarchy of the selected JDeveloper Project?"
  , /*   1 */ "Regenerate Maven Project Object Model Hierarchy"
  , /*   2 */ "Do you realy want to refactor the runtime configuration of the selected JDeveloper Project?"
  , /*   3 */ "Regenerate Runtime Configuration"
  , /*   4 */ "General"
  , /*   5 */ "Default &Workspace Folder:"
  , /*   6 */ "Default Workspace Folder must not be empty"
  , /*   7 */ "Path to Identity Manager Design &Console:"
  , /*   8 */ "Select to open the Choose Directory dialog, through which you can choose the location of Identity Manager Design Console"
  , /*   9 */ "Path to Identity Manager Design Console must be a valid directory where Identity Manager Design Console is installed within"
  , /*  10 */ "Path to Identity Manager &Foundation Framework:"
  , /*  11 */ "Select to open the Choose Directory dialog, through which you can choose the location of Identity Manager Foundation Framework"
  , /*  12 */ "Path to Identity Manager Foundation Framework must be a valid directory"
  , /*  13 */ "Libraries"
  , /*  14 */ "&Release:"
  , /*  15 */ "&Generate"
  , /*  16 */ "&Manage..."
  , /*  17 */ "&Project Object Model"

  , /*  18 */ "Build Properties"
  , /*  19 */ "&File:"
  , /*  20 */ "Build Properties File must not be empty"
  , /*  21 */ "&Project:"
  , /*  22 */ "Build Properties Project must not be empty"
  , /*  23 */ "&Description:"
  , /*  24 */ "Build Properties Description must not be empty"
  , /*  25 */ "&Application:"
  , /*  26 */ "Build Properties Application must not be empty"
  , /*  27 */ "Deployment Destination:"
  , /*  28 */ "&Libraries"
  , /*  29 */ "Select to open the Choose Directory dialog, through which you can choose the location of where the archive to build will be copied during the build process"
  , /*  30 */ "Build Properties Deployment Destination Libraries must be a valid directory!"
  , /*  31 */ "&Target:"
  , /*  32 */ "Select to open the Choose Directory dialog, through which you can choose the location of where the XML descriptors will be copied during the build process"
  , /*  33 */ "Build Properties Deployment Destination Target must be a valid directory!"
  , /*  34 */ "T&rusted:"
  , /*  35 */ "Select to open the Choose Directory dialog, through which you can choose the location of where XML descriptors will be copied during the build process"
  , /*  36 */ "Build Properties Deployment Destination Trusted must be a valid directory!"

  , /*  37 */ "Adapter"
  , /*  38 */ "&Java Archive:"
  , /*  39 */ "Adapter Java Archive must not be empty"
  , /*  40 */ "&Package path:"
  , /*  41 */ "Adapter package path must not be empty"

  , /*  42 */ "Scheduler"
  , /*  43 */ "J&ava Archive:"
  , /*  44 */ "Scheduler Java Archive must not be empty"
  , /*  45 */ "P&ackage path:"
  , /*  46 */ "Scheduler package path must not be empty"

  , /*  47 */ "Diagnostic"
  , /*  48 */ "J&ava Archive:"
  , /*  49 */ "Diagnostic Java Archive must not be empty"
  , /*  50 */ "P&ackage path:"
  , /*  51 */ "Diagnostic package path must not be empty"

  , /*  52 */ "Plugin"
  , /*  53 */ "&Java Archive:"
  , /*  54 */ "Plugin Java Archive must not be empty"
  , /*  55 */ "&Package Path:"
  , /*  56 */ "Plugin package path must not be empty"
  , /*  57 */ "Descriptoren"
  , /*  58 */ "P&lugin:"
  , /*  59 */ "Plugin descriptor must not be empty"
  , /*  60 */ "&Event Handler:"
  , /*  61 */ "Event Handler descriptor must not be empty"

  , /*  62 */ "Common"
  , /*  63 */ "Library"
  , /*  64 */ "Java A&rchive:"
  , /*  65 */ "Common Java Archive must not be empty"
  , /*  66 */ "&Package Path:"
  , /*  67 */ "Library package path must not be empty"

  , /*  68 */ "&Transfer:"
  , /*  69 */ "&Tra&nsfer:"
  , /*  70 */ "Trans&fer:"

  , /*  71 */ "Deployment Target"
  , /*  72 */ "&Folder:"
  , /*  73 */ "Select to open the Choose Directory dialog, through which you can choose the location of where XML descriptors will be copied to during the build process"
  , /*  74 */ "Folder to Identity Manager Deployment Target must be a valid directory"
  , /*  75 */ "&Deployment:"
  , /*  76 */ "Deployment library must not be empty"

  , /*  77 */ "Frontend Target"
  , /*  78 */ "&Folder:"
  , /*  79 */ "Select to open the Choose Directory dialog, through which you can choose the location of where build artifacts will be copied to during the build process"
  , /*  80 */ "Folder to Identity Manager Frontend Target must be a valid directory"
  , /*  81 */ "&Frontend Archive:"
  , /*  82 */ "Frontend archive must not be empty"
  , /*  83 */ "&Package Path:"
  , /*  84 */ "Frontend package path must not be empty"
  , /*  85 */ "&Application Archive:"
  , /*  86 */ "Application archive must not be empty"

  , /*  87 */ "Backend Target"
  , /*  88 */ "&Backend Archive:"
  , /*  89 */ "Backend archive must not be empty"
  , /*  90 */ "&Package Path:"
  , /*  91 */ "Backend package path must not be empty"

  , /*  92 */ "Assembly Target"
  , /*  93 */ "&Folder:"
  , /*  94 */ "Select to open the Choose Directory dialog, through which you can choose the location of where build artifacts will be copied to during the build process"
  , /*  95 */ "Folder to Identity Manager Assembly Target must be a valid directory"
  , /*  96 */ "&Assembly Archive:"
  , /*  97 */ "Assembly archive must not be empty"
  , /*  98 */ "Assembly Include:"
  , /*  99 */ "&Backend Build:"
  , /* 100 */ "Select to open the Choose File dialog, through which you can choose the location of the Customization Backend build script used during the customization library assembly process"
  , /* 101 */ "File to Identity Manager Backend Build must be a valid ANT build script"
  , /* 102 */ "&Frontend Build:"
  , /* 103 */ "Select to open the Choose File dialog, through which you can choose the location of the Customization Frontend build script used during the customization library assembly process"
  , /* 104 */ "File to Identity Manager Frontend Build must be a valid ANT build script"

  , /* 105 */ "Context"
  , /* 106 */ "&File Name:"
  , /* 107 */ "Context file name cannot be empty"
  , /* 108 */ "&Project Name:"
  , /* 109 */ "Context project name cannot be empty"
  , /* 110 */ "&Default Target:"
  , /* 111 */ "Context Default Target cannot be empty"

  , /* 112 */ "Export"
  , /* 113 */ "&File Name:"
  , /* 114 */ "Export file name cannot be empty"
  , /* 115 */ "&Project Name:"
  , /* 116 */ "Export project name cannot be empty"
  , /* 117 */ "&Default Target:"
  , /* 118 */ "Export Default Target cannot be empty"
  , /* 119 */ "De&scription:"
  , /* 120 */ "Export Description cannot be empty"

  , /* 121 */ "Import"
  , /* 122 */ "&File Name:"
  , /* 123 */ "Import file name cannot be empty"
  , /* 124 */ "&Project Name:"
  , /* 125 */ "Import project name cannot be empty"
  , /* 126 */ "&Default Target:"
  , /* 127 */ "Import Default Target cannot be empty"
  , /* 128 */ "Descri&ption:"
  , /* 129 */ "Import Description cannot be empty"

  , /* 130 */ "Request"
  , /* 131 */ "&File Name:"
  , /* 132 */ "Request file name cannot be empty"
  , /* 133 */ "&Project Name:"
  , /* 134 */ "Request project name cannot be empty"
  , /* 135 */ "&Default Target:"
  , /* 136 */ "Request Default Target cannot be empty"
  , /* 137 */ "Descri&ption:"
  , /* 138 */ "Request Description cannot be empty"

  , /* 139 */ "Workflow"
  , /* 140 */ "&Service:"
  , /* 141 */ "Workflow Service cannot be empty"
  , /* 142 */ "&Name:"
  , /* 143 */ "Workflow Name cannot be empty"
  , /* 144 */ "Workflow Name must be a valid Java Name"

  , /* 145 */ "Composite"
  , /* 146 */ "&Destination:"
  , /* 147 */ "..."
  , /* 148 */ "Composite Destination must be a valid directory"
  , /* 149 */ "P&ackage path:"
  , /* 150 */ "Composite package path must not be empty"
  , /* 151 */ "&Partition:"
  , /* 152 */ "Partition cannot be empty"
  , /* 153 */ "&Revision:"
  , /* 154 */ "Revision cannot be empty"
  , /* 155 */ "&Category:"
  , /* 156 */ "Category cannot be empty"
  , /* 157 */ "&Provider:"
  , /* 158 */ "Provider cannot be empty"
  , /* 159 */ "&Operation:"
  , /* 160 */ "Operation cannot be empty"
  , /* 161 */ "&Payload:"
  , /* 162 */ "Payload cannot be empty"

  , /* 163 */ "Deployment Files"
  , /* 164 */ "Deployment Directory defines where Identity Manager related deployment files are stored in the project."
  , /* 165 */ "&Deployment Directory"
  , /* 166 */ "Select to open the Choose Directory dialog, through which you can choose the new location of the deployment content"
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