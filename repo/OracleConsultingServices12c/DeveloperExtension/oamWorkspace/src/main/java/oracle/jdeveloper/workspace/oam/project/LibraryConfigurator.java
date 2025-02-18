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

    Copyright © 2015. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Access Management Facility

    File        :   LibraryConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LibraryConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.66  2015-09-12  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

import oracle.jdeveloper.workspace.oam.Manifest;

import oracle.jdeveloper.workspace.oam.model.Library;

import oracle.jdeveloper.workspace.oam.model.Plugin;
import oracle.jdeveloper.workspace.oam.parser.LibraryHandler;

////////////////////////////////////////////////////////////////////////////////
// class LibraryConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Access Manager Adapter projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.60.66
 */
class LibraryConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LibraryConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new LibraryConfigurator()" and enforces use of the public factory method
   ** below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private LibraryConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Access Manager projects.
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
    final TemplateStream library = folder.add(buildPreview(folder));
    library.include(Manifest.OAM_TARGET, target);
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
  protected void templateProjectDeployment(final TemplateFolder project, final TemplateStream context) {
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
    final Library provider = Library.instance(this.context);
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
    final Library provider = Library.instance(this.context);
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
   **                            configure Oracle Access Manager Adapter
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new LibraryConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
  /**
   ** Creates the ANT project build file for Oracle Access Manager Library
   ** projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream buildPreview(final TemplateFolder folder) {
    final Library        library = Library.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, library.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(library.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(LibraryHandler.ANT_PROJECT,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), library.project());
    item.add(LibraryHandler.ANT_DEFAULT,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), library.target());
    item.add(LibraryHandler.ANT_BASEDIR,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(library.basedir()));
    item.add(LibraryHandler.DESCRIPTION,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), library.description());
    item.add(LibraryHandler.APPLICATION,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), library.application());
    item.add(LibraryHandler.LIBRARY,             TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), library.application());
    item.add(LibraryHandler.DESTINATION,         TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(library.destination()));
    item.add(LibraryHandler.PACKAGEPATH_LIBRARY, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), library.packagePath());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      LibraryHandler handler = new LibraryHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(LibraryHandler.ANT_PROJECT,         handler.name());
        item.property(LibraryHandler.ANT_DEFAULT,         handler.target());
        item.property(LibraryHandler.ANT_BASEDIR,         handler.basedir());
        item.property(LibraryHandler.DESCRIPTION,         handler.propertyValue(LibraryHandler.DESCRIPTION));
        item.property(LibraryHandler.APPLICATION,         handler.propertyValue(LibraryHandler.APPLICATION));
        item.property(LibraryHandler.LIBRARY,             handler.propertyValue(LibraryHandler.LIBRARY));
        item.property(LibraryHandler.DESTINATION,         item.relativeFile(handler.propertyValue(LibraryHandler.DESTINATION)));
        item.property(LibraryHandler.PACKAGEPATH_LIBRARY, handler.propertyValue(LibraryHandler.PACKAGEPATH_LIBRARY));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}