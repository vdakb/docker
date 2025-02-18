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

    File        :   OAMTarget.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    OAMTarget.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.model;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.iam.model.BuildPropertyAdapter;

import oracle.jdeveloper.workspace.oam.Manifest;

import oracle.jdeveloper.workspace.oam.parser.TargetHandler;

////////////////////////////////////////////////////////////////////////////////
// class OAMTarget
// ~~~~~ ~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Access
 ** Manager application build targets.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class OAMTarget extends BuildPropertyAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String FILE           = "oam-targets";
  private static final String TEMPLATE       = Manifest.BUILDFILE_PACKAGE + "." + FILE;
  private static final String DEFAULT_FILE   = FILE + "." + Manifest.CONFIG_FILE_TYPE;
  private static final String DEFAULT_VENDOR = "Oracle Consulting Services";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>OAMTarget</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Target()" and enforces use of the public factory method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private OAMTarget(final HashStructure structure) {
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
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(FILE, DEFAULT_FILE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   project (overridden)
  /**
   ** Returns the project name tag used by the component configuration.
   **
   ** @return                    the project name tag used by the component
   **                            configuration.
   */
  @Override
  public String project() {
    // the second parameter to string() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, Manifest.string(Manifest.OAM_TARGET_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVendor
  /**
   ** Sets the vendor of the specification of the package.
   **
   ** @param  vendor             the vendor of the specification of the package.
   */
  public void specificationVendor(final String vendor) {
    this._hash.putString(TargetHandler.SPECIFICATION_VENDOR, vendor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   specificationVendor
  /**
   ** Returns the vendor of the specification of the package.
   **
   ** @return                    the vendor of the specification of the package.
   */
  public String specificationVendor() {
    return string(TargetHandler.SPECIFICATION_VENDOR, defaultImplementationVendor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVendor
  /**
   ** Sets the vendor of the implementation of the package.
   **
   ** @param  vendor             the vendor of the implementation of the package.
   */
  public void implementationVendor(final String vendor) {
    this._hash.putString(TargetHandler.IMPLEMENTATION_VENDOR, vendor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   implementationVendor
  /**
   ** Returns the vendor of the implementation of the package.
   **
   ** @return                    the vendor of the implementation of the package.
   */
  public String implementationVendor() {
    return string(TargetHandler.IMPLEMENTATION_VENDOR, defaultImplementationVendor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultSpecificationVendor
  /**
   ** Returns the vendor of the specification of the package.
   **
   ** @return                    the vendor of the specification of the package.
   */
  public final String defaultSpecificationVendor(){
    return DEFAULT_VENDOR;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultImplementationVendor
  /**
   ** Returns the vendor of the implementation of the package.
   **
   ** @return                    the vendor of the implementation of the
   **                            package.
   */
  public final String defaultImplementationVendor(){
    return DEFAULT_VENDOR;
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
  public String template() {
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
   ** {@link BuildPropertyAdapter} using {@link Manifest#OAM_TARGET} in the
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
  public static OAMTarget instance(final TraversableContext context) {
    OAMTarget instance = (OAMTarget)context.getDesignTimeObject(Manifest.OAM_TARGET);
    if (instance == null) {
      instance = new OAMTarget(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.OAM_TARGET, instance);
    }
    return instance;
  }
}