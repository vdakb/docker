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

    File        :   CustomizationFrontend.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationFrontend.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.29  2012-11-08  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.model;

import java.net.URL;

import oracle.ide.panels.TraversableContext;

import oracle.javatools.data.HashStructure;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationFrontend
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The model to support the configuration wizard in creating an Oracle Identity
 ** Manager Frontend Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.29
 */
public class CustomizationFrontend extends Customization {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String TEMPLATE               = Manifest.BUILDFILE_PACKAGE + ".adf-frontend-ant";

  private static final String BACKEND                = "backend";
  private static final String BACKEND_PROJECT        = BACKEND + ".project";
  private static final String BACKEND_ARCHIVE        = BACKEND + ".archive";

  private static final String DEFAULT_LIBRARY        = "ocs.iam.identity.ui.frontend";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationFrontend</code>.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new CustomizationFrontend()" and enforces use of the public factory
   ** method below.
   **
   ** @param  structure          the data provider to initialize the instance.
   */
  protected CustomizationFrontend(final HashStructure structure) {
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
    return string(PROJECT, Manifest.string(Manifest.OIM_FRONTEND_PROJECT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backend
  /**
   ** Sets the backend tag used by the component configuration.
   **
   ** @param  backend            the backend tag used by the component
   **                            configuration.
   */
  public void backend(final URL backend) {
    this._hash.putURL(BACKEND, backend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backend
  /**
   ** Returns the backend tag for the project.
   **
   ** @return                    the backend tag for the project.
   */
  public URL backend() {
    return this.url(BACKEND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backendProject
  /**
   ** Sets the backend project tag used by the component configuration.
   **
   ** @param  backend            the backend project tag used by the component
   **                            configuration.
   */
  public void backendProject(final URL backend) {
    this._hash.putURL(BACKEND_PROJECT, backend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backendProject
  /**
   ** Returns the backend project tag for the project.
   **
   ** @return                    the backend archive tag for the project.
   */
  public URL backendProject() {
    return this.url(BACKEND_PROJECT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backendArchive
  /**
   ** Sets the backend archive tag used by the component configuration.
   **
   ** @param  backend            the backend archive tag used by the component
   **                            configuration.
   */
  public void backendArchive(final URL backend) {
    this._hash.putURL(BACKEND_ARCHIVE, backend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backendArchive
  /**
   ** Returns the archive archive tag for the project.
   **
   ** @return                    the backend archive tag for the project.
   */
  public URL backendArchive() {
    return this.url(BACKEND_ARCHIVE);
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
  // Method:   library (Customization)
  /**
   ** Returns the library tag for the library.
   **
   ** @return                    the library tag for the library.
   */
  @Override
  public String library() {
    // The second parameter to getString() is a default value. Defaults are
    // stored in the persistent preferences file using a "placeholder".
    return string(LIBRARY, DEFAULT_LIBRARY);
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
   ** using {@link Manifest#PLUGIN} in the design time objects of the specified
   ** {@link TraversableContext}.
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
   ** @return                    the <code>Plugin</code> instance obtained from
   **                            the specifed {@link TraversableContext}.
   */
  public static CustomizationFrontend instance(final TraversableContext context) {
    CustomizationFrontend instance = (CustomizationFrontend)context.getDesignTimeObject(Manifest.OIM_FRONTEND);
    if (instance == null) {
      instance = new CustomizationFrontend(HashStructure.newInstance());
      context.putDesignTimeObject(Manifest.OIM_FRONTEND, instance);
    }
    return instance;
  }
}