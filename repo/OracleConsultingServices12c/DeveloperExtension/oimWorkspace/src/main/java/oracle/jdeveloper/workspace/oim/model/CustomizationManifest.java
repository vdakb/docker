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
    Subsystem   :   Identity Management Facility

    File        :   CustomizationManifest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationManifest.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.38  2013-07-10  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import oracle.javatools.data.HashStructure;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.workspace.iam.model.BuildPropertyAdapter;

import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.parser.CustomizationManifestHandler;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationManifest
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Identity
 ** Manager application manifest file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.38
 */
public class CustomizationManifest extends Target {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE                           = "manifest";
  private static final String TEMPLATE                       = Manifest.BUILDFILE_SOURCE + "." + FILE;
  private static final String DEFAULT_FILE                   = FILE + ".mf";

  private static final String DEFAULT_SPECIFICATION_VERSION  = "11.1.1";
  private static final String DEFAULT_IMPLEMENTATION_VERSION = "11.1.2";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationManifest</code>.
   ** <p>
   ** This constructor is private to prevent other classes to use
   ** "new CustomizationManifest()" and enforces use of the public factory of
   ** the implementing sub classes.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private CustomizationManifest(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   file (overridden)
  /**
   ** Returns the file name tag used by the component configuration to store the
   ** bulld file in the local filesystem.
   **
   ** @return                    the file name tag used by the component
   **                            configuration to store the bulld file in the
   **                            local filesystem.
   */
  @Override
  public String file() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationTitle
  /**
   ** Sets the title of the specification of the package.
   **
   ** @param  title              the title of the specification of the package.
   */
  public void specificationTitle(final String title) {
    this._hash.putString(CustomizationManifestHandler.SPECIFICATION_VENDOR, title);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationTitle
  /**
   ** Returns the title of the specification of the package.
   **
   ** @return                    the title of the specification of the package.
   */
  public String specificationTitle() {
    String title = this.string(CustomizationManifestHandler.SPECIFICATION_TITLE);
    if (StringUtility.empty(title))
      title = String.format("%s %s", specificationVendor(), "Identity Manager Interface Customization Library");
    return title;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVersion
  /**
   ** Sets the version of the specification of the package.
   **
   ** @param  version            the version of the specification of the package.
   */
  public void specificationVersion(final String version) {
    this._hash.putString(CustomizationManifestHandler.SPECIFICATION_VERSION, version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVersion
  /**
   ** Returns the version of the specification of the package.
   **
   ** @return                    the version of the specification of the package.
   */
  public String specificationVersion() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(CustomizationManifestHandler.SPECIFICATION_VERSION, DEFAULT_SPECIFICATION_VERSION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationTitle
  /**
   ** Sets the title of the implementation of the package.
   **
   ** @param  title              the title of the implementation of the package.
   */
  public void implementationTitle(final String title) {
    this._hash.putString(CustomizationManifestHandler.IMPLEMENTATION_VENDOR, title);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationTitle
  /**
   ** Returns the title of the implementation of the package.
   **
   ** @return                    the title of the implementation of the package.
   */
  public String implementationTitle() {
    String title = this.string(CustomizationManifestHandler.IMPLEMENTATION_TITLE);
    if (StringUtility.empty(title))
      title = String.format("%s %s", implementationVendor(), "Identity Manager Interface Customization Library");
    return title;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVersion
  /**
   ** Sets the version of the implementation of the package.
   **
   ** @param  version            the version of the implementation of the package.
   */
  public void implementationVersion(final String version) {
    this._hash.putString(CustomizationManifestHandler.IMPLEMENTATION_VERSION, version);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVersion
  /**
   ** Returns the version of the implementation of the package.
   **
   ** @return                    the version of the implementation of the package.
   */
  public String implementationVersion() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(CustomizationManifestHandler.IMPLEMENTATION_VERSION, DEFAULT_IMPLEMENTATION_VERSION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template (BuildPropertyAdapter)
  /**
   ** Returns the template of this descriptor the generation is based on.
   **
   ** @return                    the template of this descriptor the generation
   **                            is based on.
   */
  @Override
  public final String template() {
    return TEMPLATE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link BuildPropertyAdapter}.
   ** <p>
   ** This method tries to find an existing instance of a
   ** {@link BuildPropertyAdapter} using {@link Manifest#OIM_TARGET} in the
   ** design time objects of the specified {@link TraversableContext}.
   ** <p>
   ** If none can be found, this method will create a new {@link HashStructure},
   ** first attempting to wire that into the specified name in the
   ** {@link TraversableContext} or, failing that, leave the new
   ** {@link HashStructure} disconnected.
   ** <p>
   ** This method is guaranteed to return a <code>non-null</code>.
   ** Factory method takes a <code>PropertyStorage</code> (instead of
   ** {@link HashStructure} directly). This decouples the origin of the
   ** {@link HashStructure} and allows the future possibility of resolving
   ** preferences through multiple layers of {@link HashStructure}.
   ** <p>
   ** Classes/methods that currently implement/return PropertyStorage:
   ** <ul>
   **   <li>oracle.ide.config.Preferences
   **   <li>oracle.ide.model.Project
   **   <li>oracle.ide.model.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  context            the data provider to initialize the instance.
   **
   ** @return                    the <code>Target</code> instance obtained from
   **                            the specifed {@link TraversableContext}.
   */
  public static CustomizationManifest instance(final TraversableContext context) {
    CustomizationManifest instance = (CustomizationManifest)context.getDesignTimeObject(Manifest.ADF_MANIFEST);
    if (instance == null) {
      instance = new CustomizationManifest(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.ADF_MANIFEST, instance);
    }
    return instance;
  }
}