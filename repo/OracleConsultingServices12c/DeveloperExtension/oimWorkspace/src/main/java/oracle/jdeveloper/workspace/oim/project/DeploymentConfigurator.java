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

    File        :   DeploymentConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    DeploymentConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.18  2012-03-11  DSteding    All common stuff related to
                                               create the project previews
                                               are carved out to the class
                                               ProjectConfigurator. This was
                                               also done for building the class
                                               paths for build and deployment.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import oracle.ide.Ide;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

////////////////////////////////////////////////////////////////////////////////
// class DeploymentConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Deployment projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class DeploymentConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DeploymentConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DeploymentConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private DeploymentConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Identity Manager projects.
   ** <p>
   ** This project type has no build file thus does nothing.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  @Override
  protected void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildfile (TemplateProjectConfigurator)
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  @Override
  protected final String buildfile() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenfile (TemplateProjectConfigurator)
  /**
   ** Returns the name of the build file for a development project.
   **
   ** @return                    the name of the build file for a development
   **                            project.
   */
  @Override
  protected final String mavenfile() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the active project node configurator wrapped in a
   ** {@link TemplateProjectConfigurator} representation.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the active project node as a
   **                            {@link TemplateProjectConfigurator}
   **                            representation.
   */
  protected static TemplateProjectConfigurator instance(final TraversableContext context) {
    return instance(Ide.getActiveProject(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the specified project node configurator wrapped in a
   ** {@link TemplateProjectConfigurator}.
   **
   ** @param  project            the {@link Project} to configure.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    {@link TemplateProjectConfigurator} specific to
   **                            configure Oracle Identity Manager Deployment
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new DeploymentConfigurator(project, context);
  }
}