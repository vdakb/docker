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

    File        :   CustomizationLibraryConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationLibraryConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.37  2013-06-24  DSteding    First release version
    11.1.1.3.37.60.38  2013-06-24  DSteding    Renamed to get room for
                                               application workspace
                                               configuration.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import java.io.File;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

import oracle.jdeveloper.workspace.oim.Manifest;

////////////////////////////////////////////////////////////////////////////////
// abstract class CustomizationLibraryConfigurator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager customization
 ** projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.60.37
 */
abstract class CustomizationLibraryConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationLibraryConfigurator</code> for the
   ** specified {@link Project}.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new CustomizationConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link ProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  protected CustomizationLibraryConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Identity Manager ADF library
   ** projects.
   ** <p>
   ** This project type has no build file thus does nothing.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  protected void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream customization = folder.add(buildPreview(folder));
    customization.include(Manifest.ADF_TARGET, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateFeaturePreview (overridden)
  /**
   ** Creates the product related feature preferences ANT build file for the
   ** project.
   **
   ** @param  workspaceFolder    the {@link TemplateFolder} where the workspace
   **                            configuration will be created and act as the
   **                            parent in the logical build file hierarchy for
   **                            all other build files created by this method
   **                            implementation.
   */
  @Override
  public void templateFeaturePreview(final TemplateFolder workspaceFolder) {
    // create a folder where all the feature related files will be created
    // within and is relative to the passed workspaceRoot directory
    final TemplateFolder featureRoot = workspaceFolder.add(featureFolder(this.projectFolder(), 1));
    // all projects will use preference files to globalize common properties
    final TemplateStream preference  = featureRoot.add(WorkspaceConfigurator.templateOIMPreferencePreview(featureRoot, this.context));
    // we assume the preference template specifies the pattern
    // #{wks.preferences} to import the workspace preference file
    preference.include(oracle.jdeveloper.workspace.iam.Manifest.WKS_PREFERENCE, templateWorkspacePreview(workspaceFolder.add(featureFolder(this.projectFolder(), 2))));

    final TemplateStream context = featureRoot.add(WorkspaceConfigurator.templateOIMContextPreview(featureRoot, this.context));
    // include the technology related preview
    context.include(oracle.jdeveloper.workspace.iam.Manifest.SCP_SERVER, featureRoot.add(WorkspaceConfigurator.templateSCPServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.JEE_SERVER, featureRoot.add(WorkspaceConfigurator.templateJEEServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.OIM_SERVER, featureRoot.add(WorkspaceConfigurator.templateOIMServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.MDS_SERVER, featureRoot.add(WorkspaceConfigurator.templateMDSServerPreview(featureRoot, this.context)));
    // include the technology related preferences
    context.include(Manifest.OIM_PREFERENCE, preference);

    // a development project use always the predefined target file to globalize
    // build targets
    final TemplateStream target = featureRoot.add(CustomizationConfigurator.createADFTargetPreview(featureRoot, this.context));
    // we assume the template specifies the pattern #{oim.context} to
    // import the preferences file
    target.include(Manifest.OIM_CONTEXT, context);

    final TemplateFolder project = featureRoot.add(projectFolder()) ;
    templateBuildPreview(project.add(new File(projectFolder(), BUILD_DIRECTORY)), target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
  /**
   ** Creates the ANT project build file for Oracle Identity Manager ADF
   ** projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected abstract TemplateStream buildPreview(final TemplateFolder folder);
}