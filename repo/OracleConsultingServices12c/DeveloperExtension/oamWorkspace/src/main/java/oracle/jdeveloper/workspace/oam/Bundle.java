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
    Subsystem   :   Access Manager Facility

    File        :   Bundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oam;

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

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the translatable strings
   */
  public static final int     CONFIGURE_RUNTIME_HEADER =  0;
  public static final int     CONFIGURE_RUNTIME_TEXT   =  1;
  public static final int     GENERAL_HEADER           =  2;
  public static final int     WORKSPACE_FOLDER         =  3;
  public static final int     WORKSPACE_FAILURE        =  4;
  public static final int     FRAMEWORK_HOME           =  5;
  public static final int     FRAMEWORK_HINT           =  6;
  public static final int     FRAMEWORK_FAILURE        =  7;
  public static final int     LIBRARIES_HEADER         =  8;
  public static final int     LIBRARIES_RELEASE        =  9;
  public static final int     LIBRARIES_GENERATE       = 10;
  public static final int     LIBRARIES_MANAGE         = 11;
  public static final int     LIBRARIES_MAVEN          = 12;

  public static final int     PROPERTY_HEADER          = 13;
  public static final int     FILE_LABEL               = 14;
  public static final int     FILE_ERROR               = 15;
  public static final int     PROJECT_LABEL            = 16;
  public static final int     PROJECT_ERROR            = 17;
  public static final int     DESCRIPTION_LABEL        = 18;
  public static final int     DESCRIPTION_ERROR        = 19;
  public static final int     DESTINATION_HEADER       = 20;
  public static final int     DESTINATION_LABEL        = 21;
  public static final int     DESTINATION_HINT         = 22;
  public static final int     DESTINATION_BROWSE       = 23;
  public static final int     DESTINATION_ERROR        = 24;

  public static final int     APPLICATION_HEADER       = 25;
  public static final int     APPLICATION_LABEL        = 26;
  public static final int     APPLICATION_ERROR        = 27;
  public static final int     PACKAGE_LABEL            = 28;
  public static final int     PACKAGE_ERROR            = 29;

  public static final int     COLLECTOR_HEADER         = 30;
  public static final int     COLLECTOR_LABEL          = 31;
  public static final int     COLLECTOR_ERROR          = 32;

  public static final int     DEPLOYMENT_HEADER        = 33;
  public static final int     DEPLOYMENT_FOLDER_LABEL  = 34;
  public static final int     DEPLOYMENT_FOLDER_HINT   = 35;
  public static final int     DEPLOYMENT_FOLDER_ERROR  = 36;
  public static final int     DEPLOYMENT_LIBRARY_LABEL = 37;
  public static final int     DEPLOYMENT_LIBRARY_ERROR = 38;

  public static final int     CONTEXT_HEADER           = 39;
  public static final int     CONTEXT_FILE_LABEL       = 40;
  public static final int     CONTEXT_FILE_ERROR       = 41;
  public static final int     CONTEXT_PROJECT_LABEL    = 42;
  public static final int     CONTEXT_PROJECT_ERROR    = 43;
  public static final int     CONTEXT_TARGET_LABEL     = 44;
  public static final int     CONTEXT_TARGET_ERROR     = 45;

  public static final int     EXPORT_HEADER            = 46;
  public static final int     EXPORT_FILE_LABEL        = 47;
  public static final int     EXPORT_FILE_ERROR        = 48;
  public static final int     EXPORT_PROJECT_LABEL     = 49;
  public static final int     EXPORT_PROJECT_ERROR     = 50;
  public static final int     EXPORT_TARGET_LABEL      = 51;
  public static final int     EXPORT_TARGET_ERROR      = 52;
  public static final int     EXPORT_DESCRIPTION_LABEL = 53;
  public static final int     EXPORT_DESCRIPTION_ERROR = 54;

  public static final int     IMPORT_HEADER            = 55;
  public static final int     IMPORT_FILE_LABEL        = 56;
  public static final int     IMPORT_FILE_ERROR        = 57;
  public static final int     IMPORT_PROJECT_LABEL     = 58;
  public static final int     IMPORT_PROJECT_ERROR     = 59;
  public static final int     IMPORT_TARGET_LABEL      = 60;
  public static final int     IMPORT_TARGET_ERROR      = 61;
  public static final int     IMPORT_DESCRIPTION_LABEL = 62;
  public static final int     IMPORT_DESCRIPTION_ERROR = 63;

  private static final String CONTENT[] = {
    /*  0 */ "Do you realy want to refactor the runtime configuration of the selected JDeveloper Project?"
  , /*  1 */ "Regenerate Runtime Configuration"
  , /*  2 */ "General"
  , /*  3 */ "Default &Workspace Folder:"
  , /*  4 */ "Default Workspace Folder must not be empty"
  , /*  5 */ "Path to Access Manager &Foundation Framework:"
  , /*  6 */ "Select to open the Choose Directory dialog, through which you can choose the location of Access Manager Foundation Framework"
  , /*  7 */ "Path to Access Manager Foundation Framework must be a valid directory"
  , /*  8 */ "Libraries"
  , /*  9 */ "&Release:"
  , /* 10 */ "&Generate"
  , /* 11 */ "&Manage..."
  , /* 12 */ "&Project Object Model"

  , /* 13 */ "Build Properties"
  , /* 14 */ "&File"
  , /* 15 */ "File must not be empty"
  , /* 16 */ "&Project"
  , /* 17 */ "Project must not be empty"
  , /* 18 */ "&Description"
  , /* 19 */ "Description must not be empty"
  , /* 20 */ "Deployment Destination"
  , /* 21 */ "&Libraries"
  , /* 22 */ "Click to open the Choose Directory dialog, through which you can choose the location of where the archive to build will be copied during the build process"
  , /* 23 */ "..."
  , /* 24 */ "Deployment Destination Libraries must not be empty"

  , /* 25 */ "Libraries"
  , /* 26 */ "&Java Archive"
  , /* 27 */ "Libraries Java Archive must not be empty"
  , /* 28 */ "&Package path"
  , /* 29 */ "Libraries package path must not be empty"

  , /* 30 */ "Credential Collector"
  , /* 31 */ "&Collector Module"
  , /* 32 */ "Credential Collector Java Archive must not be empty"

  , /* 33 */ "Deployment Target"
  , /* 34 */ "&Folder:"
  , /* 35 */ "Select to open the Choose Directory dialog, through which you can choose the location of where XML descriptors will be copied to during the build process"
  , /* 36 */ "Folder to Access Manager Deployment Target must be a valid directory"
  , /* 37 */ "&Deployment:"
  , /* 38 */ "Deployment library must not be empty"

  , /* 39 */ "Context"
  , /* 40 */ "&File Name:"
  , /* 41 */ "Context file name cannot be empty"
  , /* 42 */ "&Project Name:"
  , /* 43 */ "Context project name cannot be empty"
  , /* 44 */ "&Default Target:"
  , /* 45 */ "Context Default Target cannot be empty"

  , /* 46 */ "Export"
  , /* 47 */ "&File Name:"
  , /* 48 */ "Export file name cannot be empty"
  , /* 49 */ "&Project Name:"
  , /* 50 */ "Export project name cannot be empty"
  , /* 51 */ "&Default Target:"
  , /* 52 */ "Export Default Target cannot be empty"
  , /* 53 */ "De&scription:"
  , /* 54 */ "Export Description cannot be empty"

  , /* 55 */ "Import"
  , /* 56 */ "&File Name:"
  , /* 57 */ "Import file name cannot be empty"
  , /* 58 */ "&Project Name:"
  , /* 59 */ "Import project name cannot be empty"
  , /* 60 */ "&Default Target:"
  , /* 61 */ "Import Default Target cannot be empty"
  , /* 62 */ "Descri&ption:"
  , /* 63 */ "Import Description cannot be empty"
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