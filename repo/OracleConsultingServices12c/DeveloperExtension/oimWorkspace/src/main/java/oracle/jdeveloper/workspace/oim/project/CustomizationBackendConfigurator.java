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

    File        :   CustomizationBackendConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationBackendConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.37  2013-06-24  DSteding    First release version
    11.1.1.3.37.60.38  2013-06-24  DSteding    Renamed to get room for
                                               application workspace
                                               configuration.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim.project;

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

import oracle.jdeveloper.workspace.oim.Library;

import oracle.jdeveloper.workspace.oim.model.CustomizationBackend;

import oracle.jdeveloper.workspace.oim.parser.CustomizationHandler;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationBackendConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Backend customization
 ** projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.37
 */
public class CustomizationBackendConfigurator extends CustomizationLibraryConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CustomizationBackendConfigurator</code> for the
   ** specified {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CustomizationBackendConfigurator()" and enforces use of the public
   ** factory method below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private CustomizationBackendConfigurator(final Project project, final TraversableContext context) {
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
    final CustomizationBackend provider = CustomizationBackend.instance(this.context);
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
    final CustomizationBackend provider = CustomizationBackend.instance(this.context);
    return provider.file().replace("ant", "pom");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview (CustomizationLibraryConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Identity Manager ADF Model
   ** projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  @Override
  protected TemplateStream buildPreview(final TemplateFolder folder) {
    final CustomizationBackend context = CustomizationBackend.instance(this.context);
    final TemplateStream       item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(CustomizationHandler.ANT_PROJECT,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  context.project());
    item.add(CustomizationHandler.ANT_BASEDIR,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(context.basedir()));
    item.add(CustomizationHandler.ANT_DEFAULT,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  context.target());
    item.add(CustomizationHandler.DESCRIPTION,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), context.description());
    item.add(CustomizationHandler.LIBRARY,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), context.library());
    item.add(CustomizationHandler.PACKAGE_PATH, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), context.packagePath());
   // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      CustomizationHandler handler = new CustomizationHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(CustomizationHandler.ANT_PROJECT,  handler.name());
        item.property(CustomizationHandler.ANT_BASEDIR,  item.relativeFile(handler.basedir()));
        item.property(CustomizationHandler.ANT_DEFAULT,  handler.target());
        item.property(CustomizationHandler.DESCRIPTION,  handler.propertyValue(CustomizationHandler.DESCRIPTION));
        item.property(CustomizationHandler.LIBRARY,      handler.propertyValue(CustomizationHandler.LIBRARY));
        item.property(CustomizationHandler.PACKAGE_PATH, handler.propertyValue(CustomizationHandler.PACKAGE_PATH));
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
    return new CustomizationBackendConfigurator(project, context);
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
    settings.addLibrary(Library.OIM_CLIENT_ID,     false);
    settings.addLibrary(Library.OIM_BACKEND_ID,    false);
    settings.addLibrary(Library.HST_FOUNDATION_ID, false);
  }
}