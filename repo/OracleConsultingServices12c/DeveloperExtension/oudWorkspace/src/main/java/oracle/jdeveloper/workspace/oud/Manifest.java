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
    Subsystem   :   Unified Directory Facilities

    File        :   Manifest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Manifest.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.43  2013-10-23  DSteding    Extended support for ANT
                                               runtime classpath configuration
    11.1.1.3.37.60.69  2015-12-27  DSteding    Removed Oracle from the product
                                               names and libraries because its
                                               clear where we are.
                                               Removed deprecated R1 Release.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oud;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.AbstractAddin;

import oracle.jdeveloper.workspace.iam.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Manifest
// ~~~~~ ~~~~~~~~
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
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class Manifest extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The FEATURE key should be a hard-coded String to guarantee that its
   ** value stays constant across releases.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for
   ** FEATURE.
   ** <b>Note to myself</b>:
   ** <br>
   ** Ensure always the key defined here is the same that we use in the
   ** extension manifest as the key of the folder.
   */
  public static final String FEATURE                     = "oud";

  public static final String FEATURE_RULE                = "oud-extension-state";
  public static final String GALLERY_RULE                = "oud-workspace";

  /**
   ** The key KEY should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   ** <br>
   ** Specifically, do NOT use CoolFeaturePrefs.class.getName(). The reason is
   ** that if CoolFeaturePrefs is ever renamed or moved,
   ** CoolFeaturePrefs.class.getName() will cause the KEY String to change,
   ** which introduces a preferences migration issue (since this key is used in
   ** the persisted XML) that will require more code and testing to accomodate
   ** and open up your code to annoying little bugs.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for key
   ** KEY.
   ** <p>
   ** By convention, KEY should be the fully qualified class name of the
   ** <code>HashStructureAdapter</code>. This helps ensure against name
   ** collisions. This also makes it easier to identify what piece of code is
   ** responsible for a preference when you're looking at the XML in the
   ** product-preferences.xml file. Of course, that only works as long as the
   ** adapter class itself is never renamed or moved, so avoid renaming or
   ** moving this class once it's been released in production.
   */
  public static final String KEY                         = AbstractAddin.DATA_KEY + "/" + FEATURE;

  /**
   ** the translatable strings
   */
  public static final String NAME                       = FEATURE + ".name";
  public static final String VENDOR                     = FEATURE + ".vendor";
  public static final String LICENSE                    = FEATURE + ".license";
  public static final String COPYRIGHT                  = FEATURE + ".copyright";
  public static final String DESCRIPTION                = FEATURE + ".description";

  /**
   ** The TECHNOLOGY should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   ** <br>
   ** Specifically, do NOT use CoolFeaturePrefs.class.getName(). The reason is
   ** that if CoolFeaturePrefs is ever renamed or moved,
   ** CoolFeaturePrefs.class.getName() will cause the KEY String to change,
   ** which introduces a preferences migration issue (since this key is used in
   ** the persisted XML) that will require more code and testing to accomodate
   ** and open up your code to annoying little bugs.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for
   ** TECHNOLOGY.
   */
  public static final String TECHNOLOGY                  = FEATURE         + ".technology";
  public static final String PROJECT                     = FEATURE         + ".project";
  public static final String PROJECT_CONFIG              = PROJECT         + ".configurator";
  public static final String PROJECT_CONFIG_ICON         = PROJECT_CONFIG  + ".icon";
  public static final String PROJECT_CONFIG_DESCRIPTION  = PROJECT_CONFIG  + ".description";
  public static final String PROJECT_RUNTIME             = PROJECT         + ".runtime";
  public static final String PROJECT_RUNTIME_ICON        = PROJECT_RUNTIME + ".icon";
  public static final String PROJECT_RUNTIME_DESCRIPTION = PROJECT_RUNTIME + ".description";
  public static final String APPLICATION                 = FEATURE         + ".application";
  public static final String APPLICATION_ICON            = APPLICATION     + ".icon";
  public static final String APPLICATION_DESCRIPTION     = APPLICATION     + ".description";

  /** identifier where all templates located */
  public static final String BUILDFILE_PACKAGE           = "template.oud";

  /** identifier to maintain the OUD build preferences */
  public static final String PREFERENCE                  = FEATURE         + ".preferences";
  public static final String PREFERENCE_PROJECT          = PREFERENCE      + ".project";
  /** identifier to maintain the OUD ANT build targets properties */
  public static final String TARGET                      = FEATURE         + ".targets";
  public static final String TARGET_PROJECT              = TARGET          + ".project";
  /** identifier to maintain the OUD ANT plugin extension */
  public static final String PLUGIN                      = FEATURE         + ".plugin";
  public static final String PLUGIN_ICON                 = PLUGIN          + ".icon";
  public static final String PLUGIN_PROJECT              = PLUGIN          + ".project";
  public static final String PLUGIN_DESCRIPTION          = PLUGIN          + ".description";
  /** identifier to maintain the OIM connection extension */
  public static final String CONNECTION                  = FEATURE         + ".connection";
  public static final String CONNECTION_ICON             = CONNECTION      + ".icon";
  public static final String CONNECTION_DESCRIPTION      = CONNECTION      + ".description";

  private static final String CONTENT[][] = {
    { FEATURE,                     "Oracle Unified Directory" }
  , { NAME,                        "Unified Directory" }
  , { VENDOR,                      "Oracle Consulting Services" }
  , { LICENSE,                     "This software is the confidential and proprietary information of Oracle Corporation. (\"Confidential Information\"). You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into with Oracle." }
    // Do Not translate the copyright message.
    // It needs to be changed even when WPTG have locked down translations.
  , { COPYRIGHT,                   "Copyright &#x00A9; 2011 Oracle and/or its affiliates. All rights reserved." }
  , { DESCRIPTION,                 "Support for development of Oracle Unified Directory components" }

  , { TECHNOLOGY,                  "Unified Directory" }
  , { PROJECT_CONFIG,              "Regenerate &Build File" }
  , { PROJECT_CONFIG_ICON,         "res:/oracle/jdeveloper/workspace/oud/gallery/pathlib.png" }
  , { PROJECT_CONFIG_DESCRIPTION,  "Regenerates the build files in the hierarchy based on the select project and the prefrences for the related feature" }
  , { PROJECT_RUNTIME,             "Refactor Runtime &Path" }
  , { PROJECT_RUNTIME_ICON,        "res:/oracle/jdeveloper/workspace/oud/gallery/pathant.png" }
  , { PROJECT_RUNTIME_DESCRIPTION, "Regenerates the ANT classpath" }
  , { APPLICATION,                 "Unified Directory Application" }
  , { APPLICATION_ICON,            "/oracle/jdeveloper/workspace/oud/gallery/application.png" }
  , { APPLICATION_DESCRIPTION,     "Creates a Unified Directory application.\nThe application consists of one or more Unified Directory projects for the plugin components." }
  , { PREFERENCE,                  "Unified Directory Application Properties" }
  , { PREFERENCE_PROJECT,          "Unified Directory Application Properties" }
  , { TARGET,                      "Unified Directory Application Targets" }
  , { TARGET_PROJECT,              "Unified Directory Application Targets" }
  , { PLUGIN,                      "Unified Directory Plug-In" }
  , { PLUGIN_ICON,                 "/oracle/jdeveloper/workspace/oud/gallery/application.png" }
  , { PLUGIN_PROJECT,              "Unified Directory Application Targets" }
  , { PLUGIN_DESCRIPTION,          "Launches the Create Unified Directory Plug-In project wizard, with which you create an Unified Directory project using new or existing service components like Plug-Ins." }
  , { CONNECTION,                  "Unified Directory Connection" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { CONNECTION_ICON,             "/oracle/jdeveloper/connection/oud/gallery/connection-wizard.png" }
  , { CONNECTION_DESCRIPTION,      "Launches the Create Unified Directory Connection dialog, in which you create and edit connections.\nIf the new connection is created within the application, it will be listed in the Application Resources panel. If it is created as an IDE Connection, it will appear in the Resources window and be available for reuse." }

  , { Library.OUD_SERVER_ID,       Library.OUD_SERVER_NAME + " provides the base functionality to communicate with an Unified Directory Server 12c" }
  , { Library.HST_FOUNDATION_ID,   Library.HST_FOUNDATION_NAME + " provides the base functionality that are shared across product features" }
  , { Library.OUD_FOUNDATION_ID,   Library.OUD_FOUNDATION_NAME + " is the abstraction layer to compose the functionality provided by " + Library.OUD_SERVER_NAME + " on a higher level" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    Manifest.class.getName()
  , Locale.getDefault()
  , Manifest.class.getClassLoader()
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
  // Method:   galleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will always have the dimension 16x16 pixel to
   ** for the layout of the Oracle JDeveloper navigator item list
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource scaled uü or doen to
   **                            a dimension of 16x16 pixel.
   */
  public static Icon galleryIcon(final String key) {
    return galleryIcon(key, 16, 16);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   galleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will scaled to the dimension <code>width</code>
   ** <code>height</code> in pixel to fit the layout of the Oracle JDeveloper
   ** Gallery item list
   **
   ** @param  key                index into the resource array.
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    the {@link Icon} resource scaled uü or doen to
   **                            a dimension of 16x16 pixel.
   */
  public static Icon galleryIcon(final String key, final int width, int height) {
//    return new ScaledIcon(icon(key), width, height);
    return RESOURCE.fetchScaledIcon(key, width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource
   */
  public static Icon icon(final String key) {
    return RESOURCE.fetchIcon(key);
  }
}