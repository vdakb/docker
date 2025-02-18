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

    File        :   CustomizationAssembly.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationAssembly.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.39  2013-07-29  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import java.net.URL;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationAssembly
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The Assembly model to support the configuration wizard in creating an Oracle
 ** Identity Manager Backend Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.39
 */
public class CustomizationAssembly extends CustomizationFrontend {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TEMPLATE         = Manifest.BUILDFILE_PACKAGE + ".adf-assembly-ant";

  private static final String FRONTEND         = "frontend";
  private static final String FRONTEND_PROJECT = FRONTEND + ".project";
  private static final String FRONTEND_ARCHIVE = FRONTEND + ".archive";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationAssembly</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CustomizationAssembly()" and enforces use of the public factory
   ** method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  private CustomizationAssembly(final HashStructure structure) {
    // ensure inheritance
    super(structure);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

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
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(PROJECT, Manifest.string(Manifest.OIM_ASSEMBLY_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontend
  /**
   ** Sets the frontend tag used by the component configuration.
   **
   ** @param  frontend           the frontend tag used by the component
   **                            configuration.
   */
  public void frontend(final URL frontend) {
    this._hash.putURL(FRONTEND, frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontend
  /**
   ** Returns the frontend tag for the project.
   **
   ** @return                    the frontend tag for the project.
   */
  public URL frontend() {
    return this.url(FRONTEND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontendProject
  /**
   ** Sets the frontend project tag used by the component configuration.
   **
   ** @param  frontend           the frontend project tag used by the component
   **                            configuration.
   */
  public void frontendProject(final URL frontend) {
    this._hash.putURL(FRONTEND_PROJECT, frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontendProject
  /**
   ** Returns the frontend project tag for the project.
   **
   ** @return                    the frontend project tag for the project.
   */
  public URL frontendProject() {
    return this.url(FRONTEND_PROJECT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontendArchive
  /**
   ** Sets the frontend archive tag used by the component configuration.
   **
   ** @param  frontend           the frontend archive tag used by the component
   **                            configuration.
   */
  public void frontendArchive(final URL frontend) {
    this._hash.putURL(FRONTEND_ARCHIVE, frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontendArchive
  /**
   ** Returns the frontend archive tag for the project.
   **
   ** @return                    the frontend archive tag for the project.
   */
  public URL frontendArchive() {
    return this.url(FRONTEND_ARCHIVE);
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
  // Method:   library (Customization)
  /**
   ** Returns the library tag for the library.
   **
   ** @return                    the library tag for the library.
   */
  @Override
  public String library() {
    return this.application();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns an instance of this {@link BuildArtifact}.
   ** <p>
   ** This method tries to find an existing instance of a {@link BuildArtifact}
   ** using {@link Manifest#OIM_ASSEMBLY} in the design time objects of the
   ** specified {@link TraversableContext}.
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
   **   <li>oracle.ide.Backend.Project
   **   <li>oracle.ide.Backend.Workspace
   **   <li>oracle.ide.panels.TraversableContext.getPropertyStorage()
   ** </ul>
   **
   ** @param  context            the data provider to initialize the instance.
   **
   ** @return                    the <code>Plugin</code> instance obtained from
   **                            the specifed {@link TraversableContext}.
   */
  public static CustomizationAssembly instance(final TraversableContext context) {
    CustomizationAssembly instance = (CustomizationAssembly)context.getDesignTimeObject(Manifest.OIM_ASSEMBLY);
    if (instance == null) {
      instance = new CustomizationAssembly(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.OIM_ASSEMBLY, instance);
    }
    return instance;
  }
}