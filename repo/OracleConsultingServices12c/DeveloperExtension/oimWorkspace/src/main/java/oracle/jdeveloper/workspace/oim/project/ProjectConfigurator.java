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

    File        :   ProjectConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ProjectConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.18  2012-03-11  DSteding    All common stuff related to
                                               create the project previews
                                               are carved out to this class.
    11.1.1.3.37.60.27  2012-08-31  DSteding    Removed configurable elements of
                                               project template oim-context.xml
                                               and oim-deployment.xml.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.34  2012-02-06  DSteding    Extended support for R2
                                               deployments
    11.1.1.3.37.60.37  2013-06-24  DSteding    ANT-Classpath includes the common
                                               foundation deployment library
    11.1.1.3.37.60.38  2013-07-06  DSteding    ANT-Classpath includes the common
                                               OPSS platform library and Java
                                               Secure Channel (jsch.jar)
    11.1.1.3.37.60.64  2015-06-02  DSteding    include of SOA server configuration
                                               accidentally dropped
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import java.io.File;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;
import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;

import oracle.jdeveloper.workspace.oim.Library;
import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.model.Export;
import oracle.jdeveloper.workspace.oim.model.Import;
import oracle.jdeveloper.workspace.oim.model.Request;

import oracle.jdeveloper.workspace.oim.parser.ExportHandler;
import oracle.jdeveloper.workspace.oim.parser.ImportHandler;
import oracle.jdeveloper.workspace.oim.parser.RequestHandler;

////////////////////////////////////////////////////////////////////////////////
// abstract class ProjectConfigurator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager development projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
abstract class ProjectConfigurator extends TemplateProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** default path relative to the project base URL where system configuration
   ** files are managed
   */
  public static final String BASE_DIRECTORY    = "base";

  /**
   ** default path relative to the project base URL where target system
   ** configuration files are managed
   */
  public static final String TARGET_DIRECTORY  = "target";

  /**
   ** default path relative to the project base URL where target system
   ** configuration files are managed
   */
  public static final String TRUSTED_DIRECTORY = "trusted";

  /**
   ** default path relative to a project base URL where arbitrary xml files are
   ** managed
   */
  public static final String XML_DIRECTORY     = "xml";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ProjectConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new ProjectConfigurator()" and enforces use of the public factory method
   ** implemented by the subclasses.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  protected ProjectConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateFeaturePreview (TemplateObjectConfigurator)
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
    // within and is relative to the passed workspaceFolder directory
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
    context.include(oracle.jdeveloper.workspace.iam.Manifest.SOA_SERVER, featureRoot.add(WorkspaceConfigurator.templateSOAServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.MDS_SERVER, featureRoot.add(WorkspaceConfigurator.templateMDSServerPreview(featureRoot, this.context)));
    // include the technology related preferences
    context.include(Manifest.OIM_PREFERENCE, preference);

    // a development project use always the predefined target file to globalize
    // build targets
    final TemplateStream target = featureRoot.add(WorkspaceConfigurator.templateOIMTargetPreview(featureRoot, this.context));
    // we assume the template specifies the pattern #{oim.context} to
    // import the preferences file
    target.include(Manifest.OIM_CONTEXT, context);

    final TemplateFolder project = featureRoot.add(projectFolder()) ;
    final TemplateFolder build   = project.add(new File(projectFolder(), BUILD_DIRECTORY));
    templateBuildPreview(build, target);
    templateDeploymentPreview(build, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenFeaturePreview (TemplateObjectConfigurator)
  /**
   ** Creates the product related feature target Maven Project Object Model
   ** build files for current project.
   **
   ** @param  workspaceFolder    the {@link ModelFolder} where the workspace
   **                            configuration will be created and act as the
   **                            parent in the logical build file hierarchy for
   **                            all other build files created by this method
   **                            implementation.
   */
  @Override
  public final void mavenFeaturePreview(final ModelFolder workspaceFolder) {
    // create a folder where all the feature related files will be created
    // within and is relative to the passed workspaceRoot directory
    final ModelFolder featureRoot = workspaceFolder.add(featureFolder(this.projectFolder(), 1));
    // all projects will use preference files to globalize common properties
    final ModelStream preference  = featureRoot.add(WorkspaceConfigurator.mavenOIMPreferencePreview(featureRoot, this.context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (TemplateObjectConfigurator)
  /**
   ** Configures a Oracle JDeveloper {@link Project} completely.
   */
  @Override
  public void configure() {
    configureBuildDirectory();
    configureSiteDirectory();
    configureSourceDirectory();
    configureResourceDirectory();
    configureOutputDirectory();
    configureTestDirectory();
    configureModelDirectory();
    configureCompiler();
    configureClassPath();
    configureBuildfile();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureFeature (TemplateObjectConfigurator)
  /**
   ** Creates the product related feature preferences ANT build file for the
   ** project.
   **
   ** @param  context            the {@link TraversableContext} where the data
   **                            are stored as design time objects.
   */
  @Override
  public void configureFeature(final TraversableContext context) {
    final TemplateFolder root = new TemplateFolder(featureFolder(projectFolder(), 1));
    templateFeaturePreview(root);
    // generate the ANT property files for deployment project configuration
    final TemplateGenerator builder = new TemplateGenerator(root);
    builder.startProgress();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClassPath (overridden)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected void configureClassPath() {
    configureLibraries();
    configureRuntime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview
  /**
   ** Creates the ANT project build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  protected abstract void templateBuildPreview(final TemplateFolder folder, final TemplateStream target);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateDeploymentPreview
  /**
   ** Creates the ANT deployment build files for Oracle Identity Manager
   ** deployment projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  protected void templateDeploymentPreview(final TemplateFolder folder, final TemplateStream context) {
    TemplateStream deploy = folder.add(templateExportPreview(folder));
    deploy.include(Manifest.OIM_CONTEXT, context);
    deploy = folder.add(templateImportPreview(folder));
    deploy.include(Manifest.OIM_CONTEXT, context);
    deploy = folder.add(templateRequestPreview(folder));
    deploy.include(Manifest.OIM_CONTEXT, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateExportPreview
  /**
   ** Creates the ANT export build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateExportPreview(final TemplateFolder folder) {
    final Export         context = Export.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(ExportHandler.ANT_PROJECT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    context.project());
    item.add(ExportHandler.ANT_BASEDIR,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    item.relativeFile(context.basedir()));
    item.add(ExportHandler.ANT_DEFAULT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.target());
    item.add(ExportHandler.ANT_DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.description());

    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      ExportHandler handler = new ExportHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(ExportHandler.ANT_PROJECT,     handler.name());
        item.property(ExportHandler.ANT_BASEDIR,     item.relativeFile(handler.basedir()));
        item.property(ExportHandler.ANT_DEFAULT,     handler.target());
        item.property(ExportHandler.ANT_DESCRIPTION, handler.targetDescription(ExportHandler.ANT_DESCRIPTION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateImportPreview
  /**
   ** Creates the ANT import build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateImportPreview(final TemplateFolder folder) {
    final Import         context = Import.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(ImportHandler.ANT_PROJECT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    context.project());
    item.add(ImportHandler.ANT_BASEDIR,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    item.relativeFile(context.basedir()));
    item.add(ImportHandler.ANT_DEFAULT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.target());
    item.add(ImportHandler.ANT_DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.description());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      ImportHandler handler = new ImportHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(ImportHandler.ANT_PROJECT,     handler.name());
        item.property(ImportHandler.ANT_BASEDIR,     item.relativeFile(handler.basedir()));
        item.property(ImportHandler.ANT_DEFAULT,     handler.target());
        item.property(ImportHandler.ANT_DESCRIPTION, handler.targetDescription(ImportHandler.ANT_DESCRIPTION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateRequestPreview
  /**
   ** Creates the ANT import build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateRequestPreview(final TemplateFolder folder) {
    final Request        context = Request.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(RequestHandler.ANT_PROJECT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    context.project());
    item.add(RequestHandler.ANT_BASEDIR,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),    item.relativeFile(context.basedir()));
    item.add(RequestHandler.ANT_DEFAULT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.target());
    item.add(RequestHandler.ANT_DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_TARGET_ICON), context.description());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      ImportHandler handler = new ImportHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(RequestHandler.ANT_PROJECT,     handler.name());
        item.property(RequestHandler.ANT_BASEDIR,     item.relativeFile(handler.basedir()));
        item.property(RequestHandler.ANT_DEFAULT,     handler.target());
        item.property(RequestHandler.ANT_DESCRIPTION, handler.targetDescription(RequestHandler.ANT_DESCRIPTION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureLibraries
  /**
   ** Configures the libraries the project needs.
   */
  protected void configureLibraries() {
    // configure the development class path
    final JProjectLibraries settings = JProjectLibraries.getInstance(this.project());
    settings.addLibrary(Library.MDS_RUNTIME_ID,    false);
    settings.addLibrary(Library.OIM_PLATFORM_ID,   false);
    settings.addLibrary(Library.OIM_ADAPTER_ID,    false);
    settings.addLibrary(Library.OIM_SCHEDULER_ID,  false);
    settings.addLibrary(Library.HST_FOUNDATION_ID, false);
    settings.addLibrary(Library.OIM_FOUNDATION_ID, false);
    settings.addLibrary(Library.OIM_UTILITY_ID,    false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureRuntime
  /**
   ** Configures the ANT runtime classpath a project needs.
   */
  protected void configureRuntime() {
    Runtime.configure(this.project());
  }
}