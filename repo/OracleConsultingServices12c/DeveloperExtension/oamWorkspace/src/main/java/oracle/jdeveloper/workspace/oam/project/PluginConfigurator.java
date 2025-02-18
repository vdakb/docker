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

    File        :   PluginConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    PluginConfigurator.


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
                                               ProjectConfigurator.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oam.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

import oracle.jdeveloper.workspace.oam.Library;
import oracle.jdeveloper.workspace.oam.Manifest;

import oracle.jdeveloper.workspace.oam.model.Agent;
import oracle.jdeveloper.workspace.oam.model.Plugin;

import oracle.jdeveloper.workspace.oam.parser.PluginHandler;
import oracle.jdeveloper.workspace.oam.parser.PluginRegistryHandler;

////////////////////////////////////////////////////////////////////////////////
// class PluginConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Access Manager Authentication Plug-In
 ** projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class PluginConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>PluginConfigurator</code> for the specified
   ** {@link Project}.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private PluginConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Access Manager Authenticator
   ** project.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  protected final void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream plugin = folder.add(buildPreview(folder));
    // we assume the template specifies the pattern #{oam.target} to
    // import the build target file
    plugin.include(Manifest.OAM_TARGET, target);
    folder.add(createRegistryPreview(folder));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateProjectDeployment (ProjectConfigurator)
  /**
   ** Creates the ANT project deployment file for Oracle Access Manager projects.
   **
   ** @param  project            the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  public void templateProjectDeployment(final TemplateFolder project, final TemplateStream context) {
    templateDeploymentPreview(project, context);
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
    final Plugin provider = Plugin.instance(this.context);
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
    final Plugin provider = Plugin.instance(this.context);
    return provider.file().replace("ant", "pom");
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
   **                            configure Oracle Access Manager Agent
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new PluginConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureLibraries (overridden)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected final void configureLibraries() {
    JProjectLibraries settings = JProjectLibraries.getInstance(this.project());
    settings.addLibrary(Library.OAM_PLUGIN_ID,     false);
    settings.addLibrary(Library.HST_FOUNDATION_ID, false);
    settings.addLibrary(Library.OAM_FOUNDATION_ID, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
  /**
   ** Creates the ANT project build file for Oracle Access Manager Authenticator
   ** Plug-In projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream buildPreview(final TemplateFolder folder) {
    final Plugin         context = Plugin.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    item.add(PluginHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.project());
    item.add(PluginHandler.ANT_DEFAULT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.target());
    item.add(PluginHandler.ANT_BASEDIR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(context.basedir()));
    item.add(PluginHandler.DESCRIPTION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.description());
    item.add(PluginHandler.DESTINATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(context.destination()));
    item.add(PluginHandler.APPLICATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.application());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      PluginHandler handler = new PluginHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(PluginHandler.ANT_PROJECT, handler.name());
        item.property(PluginHandler.ANT_DEFAULT, handler.target());
        item.property(PluginHandler.ANT_BASEDIR, handler.basedir());
        item.property(PluginHandler.DESCRIPTION, handler.propertyValue(PluginHandler.DESCRIPTION));
        item.property(PluginHandler.DESTINATION, item.relativeFile(handler.propertyValue(PluginHandler.DESTINATION)));
        item.property(PluginHandler.APPLICATION, handler.propertyValue(PluginHandler.APPLICATION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createRegistryPreview
  /**
   ** Creates the EventHandlerDescriptor file for Oracle Access Manager
   ** Plug-In projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream createRegistryPreview(final TemplateFolder folder) {
    final Plugin         context = Plugin.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.pluginFile());
    item.hotspotSelected(!item.exists());
    // configure the template to use
    if (!item.exists())
      item.template(ClassUtility.classNameToFile(context.pluginTemplate(), ClassUtility.XML));
    else {
      item.template(item.file().getAbsolutePath());
      PluginRegistryHandler handler = new PluginRegistryHandler(item.file());
      try {
        handler.load();
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}