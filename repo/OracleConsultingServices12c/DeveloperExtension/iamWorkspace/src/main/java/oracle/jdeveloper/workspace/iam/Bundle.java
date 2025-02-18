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

    File        :   Bundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bundle.


    Revisions          Date        Editor      Comment
    -------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13   2011-05-14  DSteding    First release version
    11.1.1.3.37.60.64   2015-02-14  DSteding    Extended to support the custom
                                                content provider set for build
                                                and unit test
    11.1.1.3.37.60.69   2015-12-27  DSteding    Removed Oracle from the product
                                               names and libraries because its
                                                clear where we are.
    11.1.1.3.37.60.71   2017-10-27  DSteding    Release Dependency of Platform
                                                services in library generation
                                                honored.
    12.2.1.3.42.60.74   2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94   2021-03-09  DSteding    Support for Maven Project Object
                                                Model
    12.2.1.3.42.60.101  2022-06-11  DSteding    Support for Foundation Services
                                                Library Generation
*/

package oracle.jdeveloper.workspace.iam;

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
 ** @version 12.2.1.3.42.60.101
 ** @since   11.1.1.3.37.56.13
 */
public class Bundle extends ArrayResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the translatable strings
   */
  public static final int     CONFIGURE_RUNTIME_HEADER     =  0;
  public static final int     CONFIGURE_RUNTIME_TEXT       =  1;
  public static final int     GENERAL_HEADER               =  2;
  public static final int     FOLDER_ERROR_TITLE           =  3;
  public static final int     WKS_WORKSPACE_HOME           =  4;
  public static final int     WKS_WORKSPACE_HINT           =  5;
  public static final int     WKS_WORKSPACE_FAILURE        =  6;
  public static final int     OCS_WORKSPACE_HOME           =  7;
  public static final int     OCS_WORKSPACE_HINT           =  8;
  public static final int     OCS_WORKSPACE_FAILURE        =  9;
  public static final int     HST_FRAMEWORK_HOME           = 10;
  public static final int     HST_FRAMEWORK_HINT           = 11;
  public static final int     HST_FRAMEWORK_FAILURE        = 12;
  public static final int     SVC_FRAMEWORK_HOME           = 13;
  public static final int     SVC_FRAMEWORK_HINT           = 14;
  public static final int     SVC_FRAMEWORK_FAILURE        = 15;
  public static final int     OPS_FRAMEWORK_HOME           = 16;
  public static final int     OPS_FRAMEWORK_HINT           = 17;
  public static final int     OPS_FRAMEWORK_FAILURE        = 18;
  public static final int     LIBRARIES_HEADER             = 19;
  public static final int     LIBRARIES_RELEASE            = 20;
  public static final int     LIBRARIES_GENERATE           = 21;
  public static final int     LIBRARIES_MANAGE             = 22;
  public static final int     LIBRARIES_MAVEN              = 23;

  public static final int     PARSER_NODE_REQUIRED         = 24;
  public static final int     PARSER_ELEMENT_REQUIRED      = 25;
  public static final int     PARSER_ELEMENT_NOTEXISTS     = 26;
  public static final int     PARSER_TAG_REQUIRED          = 27;
  public static final int     PARSER_ATTRIBUTE_REQUIRED    = 28;
  public static final int     PARSER_ATTRIBUTE_NOTMAAPPED  = 29;
  public static final int     PARSER_ATTRIBUTE_MISSING     = 30;
  public static final int     PARSER_EXPRESSION_REQUIRED   = 31;
  public static final int     PARSER_FILE_REQUIRED         = 32;
  public static final int     PARSER_FILE_NOTEXISTS        = 33;
  public static final int     PARSER_FILE_NOTAFILE         = 34;
  public static final int     PARSER_FILE_NOTREADABLE      = 35;
  public static final int     PARSER_DOCUMENT_REQUIRED     = 36;
  public static final int     PARSER_INVALID_XPATH         = 37;
  public static final int     PARSER_READ_ERROR            = 38;
  public static final int     PARSER_WRITE_ERROR           = 39;
  public static final int     PARSER_DOCUMENT_ERROR        = 40;

  public static final int     CONTENT_ANTBUILD_CATEGORY    = 41;
  public static final int     CONTENT_ANTBUILD_PATH_HINT   = 42;
  public static final int     CONTENT_ANTBUILD_PATH_LABEL  = 43;
  public static final int     CONTENT_ANTBUILD_PATH_ERROR  = 44;
  public static final int     CONTENT_ANTBUILD_PATH_NULL   = 45;
  public static final int     CONTENT_ANTBUILD_PATH_EMPTY  = 46;
  public static final int     CONTENT_UNITTEST_CATEGORY    = 47;
  public static final int     CONTENT_UNITTEST_PATH_HINT   = 48;
  public static final int     CONTENT_UNITTEST_PATH_LABEL  = 49;
  public static final int     CONTENT_UNITTEST_PATH_ERROR  = 50;
  public static final int     CONTENT_UNITTEST_PATH_NULL   = 51;
  public static final int     CONTENT_UNITTEST_PATH_EMPTY  = 52;
  public static final int     CONTENT_SITEDOC_CATEGORY     = 53;
  public static final int     CONTENT_SITEDOC_PATH_HINT    = 54;
  public static final int     CONTENT_SITEDOC_PATH_LABEL   = 55;
  public static final int     CONTENT_SITEDOC_PATH_ERROR   = 56;
  public static final int     CONTENT_SITEDOC_PATH_NULL    = 57;
  public static final int     CONTENT_SITEDOC_PATH_EMPTY   = 58;

  private static final String CONTENT[] = {
    /*  0 */ "Do you realy want to refactor the runtime configuration of the selected JDeveloper Project?"
  , /*  1 */ "Refactor Runtime Configuration"
  , /*  2 */ "General"
  , /*  3 */ "Error in validate folder"
  , /*  4 */ "Path to &Development Workspace:"
  , /*  5 */ "Select to open the Choose Directory dialog, through which you can choose the directory in your local file system where you will start the development"
  , /*  6 */ "Path to Development Workspace must be a valid directory"
  , /*  7 */ "Path to &Consulting Workspace:"
  , /*  8 */ "Select to open the Choose Directory dialog, through which you can choose the directory where you checked-out the entire Oracle Consulting Workspace"
  , /*  9 */ "Path to Consulting Workspace must be a valid directory"
  , /* 10 */ "Path to Consulting &Foundation Framework:"
  , /* 11 */ "Select to open the Choose Directory dialog, through which you can choose the directory where you checked-out the Headstart Foundation Framework"
  , /* 12 */ "Path to Consulting Foundation Framework must be a valid directory"
  , /* 13 */ "Path to Consulting &Services Framework:"
  , /* 14 */ "Select to open the Choose Directory dialog, through which you can choose the directory where you checked-out the Headstart Services Framework"
  , /* 15 */ "Path to Consulting Services Framework must be a valid directory"
  , /* 16 */ "Path to Platform &Service Framework:"
  , /* 17 */ "Select to open the Choose Directory dialog, through which you can choose the directory where you checked-out the Platform Service Framework"
  , /* 18 */ "Path to Platform Service Framework must be a valid directory"
  , /* 19 */ "Libraries"
  , /* 20 */ "&Release:"
  , /* 21 */ "&Generate"
  , /* 22 */ "&Manage..."
  , /* 23 */ "&Project Object Model"

  , /* 24 */ "Node must not be null or empty"
  , /* 25 */ "Element must not be null or empty"
  , /* 26 */ "Element path {0} does not exists"
  , /* 27 */ "tagName must not be null or empty"
  , /* 28 */ "attributeName must not be null or empty"
  , /* 29 */ "Attribute {0} is not mapped."
  , /* 30 */ "Missing attribute {0} on element: {1}"
  , /* 31 */ "Expression must not be null or empty"
  , /* 32 */ "File must not be null"
  , /* 33 */ "Error file handle {0} does not exists"
  , /* 34 */ "Error file handle {0} is not a regular file"
  , /* 35 */ "Error file handle {0} is not readable"
  , /* 36 */ "Document must not be null"
  , /* 37 */ "Invalid xpath expression"
  , /* 38 */ "Error parsing file {0} : {1}"
  , /* 39 */ "Error writing document content to {0}"
  , /* 40 */ "Error creating document: {0}"

  , /* 41 */ "Ant Build Sources"
  , /* 42 */ "Ant Build Source path defines where build related files are stored in the project."
  , /* 43 */ "&Ant Build Source Paths:"
  , /* 44 */ "Ant Build Source Error"
  , /* 45 */ "The Ant Build Source path property path \"{0}\" is invalid. Please specify a relative path."
  , /* 46 */ "At least one content folder is required."

  , /* 47 */ "Unit Test Sources"
  , /* 48 */ "Unit Test Source path property defines where unit test related files are stored in the project."
  , /* 49 */ "&Unit Test Source Paths:"
  , /* 50 */ "Unit Test Source Error"
  , /* 51 */ "The Unit Test Source path property path \"{0}\" is invalid. Please specify a relative path."
  , /* 52 */ "At least one content folder is required."

  , /* 53 */ "Site Document Sources"
  , /* 54 */ "Site Document Sources path property defines where DocBook related files are stored in the project."
  , /* 55 */ "&Site Document Sources Paths:"
  , /* 56 */ "Site Document Sources Error"
  , /* 57 */ "The Site Document Sources path property path \"{0}\" is invalid. Please specify a relative path."
  , /* 58 */ "At least one content folder is required."
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

  public static final ArrayResourceBundle instance() {
    return RESOURCE;
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
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  argument1           the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final int index, final Object argument1) {
    return RESOURCE.formatImpl(index, argument1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object argument1, Object argument2) {
    return RESOURCE.formatImpl(index, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   **
   ** @param  index               index into the resource array
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   ** @param  argument3           the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object argument1, Object argument2, Object argument3) {
    return RESOURCE.formatImpl(index, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ArrayResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  index                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String format(int index, Object[] arguments) {
    return RESOURCE.formatImpl(index, arguments);
  }
}