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
    Subsystem   :   Virtual Directory Facility

    File        :   AdapterConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    AdapterConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.ovd.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

import oracle.jdeveloper.workspace.ovd.Library;
import oracle.jdeveloper.workspace.ovd.Manifest;

import oracle.jdeveloper.workspace.ovd.model.Adapter;

import oracle.jdeveloper.workspace.ovd.parser.AdapterHandler;

////////////////////////////////////////////////////////////////////////////////
// class AdapterConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Virtual Directory Adapter projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class AdapterConfigurator extends TemplateProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AdapterConfigurator</code> for the specified
   ** {@link Project}.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private AdapterConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (TemplateObjectConfigurator)
  /**
   ** Configures a Oracle JDeveloper {@link Project} completely.
   */
  @Override
  public final void configure() {
    configureSourceDirectory();
    configureOutputDirectory();
    configureCompiler();
    configureClassPath();
  }

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
  public final void templateFeaturePreview(final TemplateFolder workspaceFolder) {
    // create a folder where all the feature related files will be created
    // within and is relative to the passed workspaceRoot directory
    final TemplateFolder featureRoot = workspaceFolder.add(featureFolder(this.projectFolder(), 1));
    // all projects will use preference files to globalize common properties
    final TemplateStream preference  = featureRoot.add(WorkspaceConfigurator.templatePreferencePreview(featureRoot, this.context));
    // we assume the preference template specifies the pattern
    // #{wks.preferences} to import the workspace preference file
    preference.include(oracle.jdeveloper.workspace.iam.Manifest.WKS_PREFERENCE, templateWorkspacePreview(workspaceFolder));
    // a development project use always the predefined target file to globalize
    // build targets
    final TemplateStream target = featureRoot.add(WorkspaceConfigurator.targetPreview(featureRoot, this.context));
    // we assume the template specifies the pattern #{ovd.preferences} to
    // import the preferences file
    target.include(Manifest.PREFERENCE, preference);

    final TemplateFolder folder = featureRoot.add(projectFolder()) ;
    final TemplateStream item   = folder.add(adapterPreview(folder));
    item.include(Manifest.TARGET, target);
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
    final ModelStream preference  = featureRoot.add(WorkspaceConfigurator.mavenPreferencePreview(featureRoot, this.context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClassPath (TemplateProjectConfigurator)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected final void configureClassPath() {
    JProjectLibraries settings = JProjectLibraries.getInstance(this.project());
    settings.addLibrary(Library.OVD_SERVER_ID,     false);
    settings.addLibrary(Library.HST_FOUNDATION_ID, false);
    settings.addLibrary(Library.OVD_FOUNDATION_ID, false);
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
    final Adapter provider = Adapter.instance(this.context);
    return provider.file();
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
    final Adapter provider = Adapter.instance(this.context);
    return provider.file().replace("ant", "pom");
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
  public final void configureFeature(final TraversableContext context) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the active project node configurator wrapped in a
   ** {@link TemplateProjectConfigurator} representation.
   ** representation.
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
   **                            configure Oracle Virtual Directory Agent
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new AdapterConfigurator(project,context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adapterPreview
  /**
   ** Creates the ANT preferences build file for Oracle Virtual Directory
   ** Adapter projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream adapterPreview(final TemplateFolder folder) {
    final Adapter        context = Adapter.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    item.add(AdapterHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.project());
    item.add(AdapterHandler.ANT_DEFAULT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.target());
    item.add(AdapterHandler.DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.description());
    item.add(AdapterHandler.DESTINATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(context.destination()));
    item.add(AdapterHandler.APPLICATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.application());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      AdapterHandler handler = new AdapterHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(AdapterHandler.ANT_PROJECT, handler.name());
        item.property(AdapterHandler.ANT_DEFAULT, handler.target());
        item.property(AdapterHandler.DESCRIPTION, handler.propertyValue(AdapterHandler.DESCRIPTION));
        item.property(AdapterHandler.DESTINATION, handler.propertyValue(AdapterHandler.DESTINATION));
        item.property(AdapterHandler.APPLICATION, handler.propertyValue(AdapterHandler.APPLICATION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}