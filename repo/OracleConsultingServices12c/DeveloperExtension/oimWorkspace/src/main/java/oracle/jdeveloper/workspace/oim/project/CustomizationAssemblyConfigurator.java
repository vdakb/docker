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

    File        :   CustomizationAssemblyConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationAssemblyConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.39  2013-07-29  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.model.J2eeSettings;
import oracle.jdeveloper.model.JProjectLibraries;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

import oracle.jdeveloper.workspace.oim.Library;

import oracle.jdeveloper.workspace.oim.model.CustomizationAssembly;

import oracle.jdeveloper.workspace.oim.parser.CustomizationAssemblyHandler;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationAssemblyConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Assembly customization
 ** projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.39
 */
public class CustomizationAssemblyConfigurator extends CustomizationLibraryConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationAssemblyConfigurator</code> for the
   ** specified {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CustomizationAssemblyConfigurator()" and enforces use of the public
   ** factory method below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link ProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private CustomizationAssemblyConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

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
    final CustomizationAssembly provider = CustomizationAssembly.instance(this.context);
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
    final oracle.jdeveloper.workspace.oim.model.Library provider = oracle.jdeveloper.workspace.oim.model.Library.instance(this.context);
    return provider.file().replace("ant", "pom");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager ADF
   ** Assembly projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  @Override
  protected TemplateStream buildPreview(final TemplateFolder folder) {
    final J2eeSettings          settings = J2eeSettings.getInstance(this.project());
    final CustomizationAssembly context  = CustomizationAssembly.instance(this.context);
    final TemplateStream        item     = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(CustomizationAssemblyHandler.ANT_PROJECT,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  context.project());
    item.add(CustomizationAssemblyHandler.ANT_BASEDIR,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.basedir()));
    item.add(CustomizationAssemblyHandler.ANT_DEFAULT,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  context.target());
    item.add(CustomizationAssemblyHandler.DESCRIPTION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), context.description());
    item.add(CustomizationAssemblyHandler.DESTINATION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON),  item.relativeFile(context.destination()));
    item.add(CustomizationAssemblyHandler.APPLICATION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  context.application());
    item.add(CustomizationAssemblyHandler.BACKEND_SCRIPT,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.backend()));
    item.add(CustomizationAssemblyHandler.BACKEND_LIBRARY,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.backendArchive()));
    item.add(CustomizationAssemblyHandler.FRONTEND_SCRIPT,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.frontend()));
    item.add(CustomizationAssemblyHandler.FRONTEND_LIBRARY, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.frontendArchive()));
    item.add(CustomizationAssemblyHandler.CONTENT_FOLDER,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(settings.getHtmlRootDirectory()));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      CustomizationAssemblyHandler handler = new CustomizationAssemblyHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(CustomizationAssemblyHandler.ANT_PROJECT,      handler.name());
        item.property(CustomizationAssemblyHandler.ANT_BASEDIR,      item.relativeFile(handler.basedir()));
        item.property(CustomizationAssemblyHandler.ANT_DEFAULT,      handler.target());
        item.property(CustomizationAssemblyHandler.DESCRIPTION,      handler.propertyValue(CustomizationAssemblyHandler.DESCRIPTION));
        item.property(CustomizationAssemblyHandler.DESTINATION,      item.relativeFile(handler.propertyValue(CustomizationAssemblyHandler.DESTINATION)));
        item.property(CustomizationAssemblyHandler.APPLICATION,      handler.propertyValue(CustomizationAssemblyHandler.APPLICATION));
        item.property(CustomizationAssemblyHandler.BACKEND_SCRIPT,   item.relativeFile(handler.propertyValue(CustomizationAssemblyHandler.BACKEND_SCRIPT)));
        item.property(CustomizationAssemblyHandler.BACKEND_LIBRARY,  item.relativeFile(handler.locationValue(CustomizationAssemblyHandler.BACKEND_LIBRARY)));
        item.property(CustomizationAssemblyHandler.FRONTEND_SCRIPT,  item.relativeFile(handler.propertyValue(CustomizationAssemblyHandler.FRONTEND_SCRIPT)));
        item.property(CustomizationAssemblyHandler.FRONTEND_LIBRARY, item.relativeFile(handler.locationValue(CustomizationAssemblyHandler.FRONTEND_LIBRARY)));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
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
    return new CustomizationAssemblyConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureLibraries (overridden)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected void configureLibraries() {
    // configure the development class path
    JProjectLibraries settings = JProjectLibraries.getInstance(this.project());
    settings.addLibrary(Library.OIM_PLATFORM_ID,   false);
    settings.addLibrary(Library.OIM_BACKEND_ID,    false);
    settings.addLibrary(Library.OIM_FRONTEND_ID,   false);
    settings.addLibrary(Library.HST_FOUNDATION_ID, false);
    settings.addLibrary(Library.HST_FACES_ID,      false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureContentDirectory (overridden)
  /**
   ** Configures the <code>Web Content Directory</code> accordingly to the
   ** Oracle Consulting
   ** <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">Style Guide</a>.
   ** <p>
   ** Consult <a href="https://stbeehive.oracle.com/teamcollab/wiki/iam-germany">iam-germany</a>
   ** for a better understanding of this Style Guide.
   */
  @Override
  protected void configureContentDirectory() {
    // change the J2EE settings
    final CustomizationAssembly context = CustomizationAssembly.instance(this.context);
    final J2eeSettings          setting = J2eeSettings.getInstance(this.project());
    setting.setJ2eeWebAppName(context.application());
    setting.setJ2eeWebContextRoot(context.application());
    // ensure inheritance
    super.configureContentDirectory();
  }
}