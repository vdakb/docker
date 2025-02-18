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
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateBundle.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplateBuildBundle.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.project.template;

import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.Image;

import javax.swing.Icon;

import oracle.ide.util.ArrayResourceBundle;

import oracle.jdeveloper.workspace.iam.utility.IconFactory;

////////////////////////////////////////////////////////////////////////////////
// class TemplateBundle
// ~~~~~ ~~~~~~~~~~~~~~
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
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class TemplateBundle extends ArrayResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int DIALOG_TITLE                 =  0;
  public static final int DIALOG_HEADER                =  1;
  public static final int DIALOG_IMAGE                 =  2;

  public static final int NODE_FOLDER_ICON             =  3;
  public static final int NODE_BUILDFILE_ICON          =  4;
  public static final int NODE_DESCRIPTOR_ICON         =  5;
  public static final int NODE_PROPERTY                =  6;
  public static final int NODE_PROPERTY_ANT_ICON       =  7;
  public static final int NODE_PROPERTY_TARGET_ICON    =  8;
  public static final int NODE_PROPERTY_XML_ICON       =  9;
  public static final int NODE_PROPERTY_JAVA_ICON      = 10;
  public static final int NODE_PROPERTY_FOLDER_ICON    = 11;
  public static final int NODE_PROPERTY_PROCESS_ICON   = 12;

  public static final int CREATE_BEGIN                 = 13;
  public static final int CREATE_FINISHED              = 14;
  public static final int CREATE_FAILED                = 15;
  public static final int FILE_SIZE_DIFFERNET          = 16;
  public static final int REWRITE_BEFORE_PARSE         = 17;

  public static final int BUILDFILE_NAVIGATOR_TITLE    = 18;
  public static final int BUILDFILE_PARAMETER_TITLE    = 19;
  public static final int BUILDFILE_PREVIEW_TITLE      = 20;
  public static final int BUILDFILE_TITLE_FORMAT       = 21;
  public static final int BUILDFILE_FOLDER_TITLE       = 22;
  public static final int BUILDFILE_FOLDER_LABEL       = 23;
  public static final int BUILDFILE_FOLDER_BROWSE      = 24;
  public static final int BUILDFILE_FOLDER_FAILURE     = 25;
  public static final int BUILDFILE_NAME_TITLE         = 26;
  public static final int BUILDFILE_NAME_LABEL         = 27;
  public static final int BUILDFILE_NAME_CREATE        = 28;
  public static final int BUILDFILE_NAME_FAILURE       = 29;
  public static final int BUILDFILE_PROPERTIES         = 30;
  public static final int BUILDFILE_PROPERTY_NAME      = 31;
  public static final int BUILDFILE_PROPERTY_VALUE     = 32;
  public static final int BUILDFILE_PREVIEW            = 33;
  public static final int BUILDFILE_PARSE_TEMPLATE     = 34;
  public static final int BUILDFILE_SUBSTITUTE_PARAM   = 35;
  public static final int BUILDFILE_WRITE_TEMPLATE     = 36;

  private static final String CONTENT[] = {
    /*  0 */ "Generate Build File"
  , /*  1 */ "Review the hierarchy of the files to generate.\n\nThe view below gives you the opportunity to adjust the generation of the files. The navigator display the hierarchy of the files in the appropriate folders.\n\nFiles with the deselected option create are already exists and will be kept as they are.\nBe carefull if you deside to override those files by selecting the option create it will affect other build hierarchies in the same workspace too."
  , /*  2 */ "images/ant.png"

  , /*  3 */ "images/ant-node-folder.png"
  , /*  4 */ "images/ant-node-file.png"
  , /*  5 */ "images/xml-node-file.png"

  , /*  6 */ "Properties"
  , /*  7 */ "images/ant-property.png"
  , /*  8 */ "images/ant-target.png"
  , /*  9 */ "images/ant-parameter.png"
  , /* 10 */ "images/ant-java-duke.png"
  , /* 11 */ "images/ant-folder.png"
  , /* 12 */ "images/soa-process.png"

  , /* 13 */ "Createting build file {0} ..."
  , /* 14 */ "Build file {0} created."
  , /* 15 */ "Createting build file {0} failed"
  , /* 16 */ "File size different for template {0}"
  , /* 17 */ "Template rewrite invoked before parse"

  , /* 18 */ "Hierarchy"
  , /* 19 */ "Parameter"
  , /* 20 */ "Preview"
  , /* 21 */ "{0}::{1}"
  , /* 22 */ "Directory"
  , /* 23 */ "&Directory"
  , /* 24 */ "&Browse..."
  , /* 25 */ "Path to Directory must be a valid directory"
  , /* 26 */ "Name"
  , /* 27 */ "&Name"
  , /* 28 */ "&Create"
  , /* 29 */ "Name must be a valid file name"
  , /* 30 */ "Properties"
  , /* 31 */ "Name"
  , /* 32 */ "Value"
  , /* 33 */ "&Preview"
  , /* 34 */ "Parsing template {0}"
  , /* 35 */ "Substituting parameters in {0}"
  , /* 36 */ "Writing buildfile {0}"
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ArrayResourceBundle RESOURCE = (ArrayResourceBundle)ResourceBundle.getBundle(
    TemplateBundle.class.getName()
  , Locale.getDefault()
  , TemplateBundle.class.getClassLoader()
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
  @Override
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Fetch an {@link Icon} from this {@link ArrayResourceBundle}.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource
   */
  public static Icon icon(final int key) {
    return IconFactory.create(TemplateBundle.class, string(key));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Fetch an {@link Image} from this {@link ArrayResourceBundle}.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Image} resource
   */
  public static Image image(final int key) {
    return IconFactory.create(TemplateBundle.class, string(key)).getImage();
  }
}