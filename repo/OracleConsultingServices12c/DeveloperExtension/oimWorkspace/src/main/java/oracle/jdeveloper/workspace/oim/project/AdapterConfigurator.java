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
    11.1.1.3.37.60.18  2012-03-11  DSteding    All common stuff related to
                                               create the project previews
                                               are carved out to the class
                                               ProjectConfigurator. This was
                                               also done for building the class
                                               paths of build and deployment.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

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

import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.model.Adapter;
import oracle.jdeveloper.workspace.oim.model.CustomizationFrontend;
import oracle.jdeveloper.workspace.oim.model.Scheduler;
import oracle.jdeveloper.workspace.oim.model.Thirdparty;

import oracle.jdeveloper.workspace.oim.parser.AdapterHandler;

////////////////////////////////////////////////////////////////////////////////
// class AdapterConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Adapter projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class AdapterConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AdapterConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new AdapterConfigurator()" and enforces use of the public factory method
   ** below.
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
  // Method:   templateBuildPreview (ProjectConfigurator)
  /**
   ** Creates the ANT project build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  target             the ANT build file template to include in the
   **                            {@link TemplateStream} to create.
   */
  @Override
  protected void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream adapter = folder.add(templatePreview(folder));
    adapter.include(Manifest.OIM_TARGET, target);
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
   **                            configure Oracle Identity Manager Adapter
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new AdapterConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templatePreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
   ** Adapter projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream templatePreview(final TemplateFolder folder) {
    final Adapter        adapter    = Adapter.instance(this.context);
    final Scheduler      scheduler  = Scheduler.instance(this.context);
    final Thirdparty     thirdparty = Thirdparty.instance(this.context);
    final TemplateStream item       = new TemplateStream(folder, adapter.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(adapter.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(AdapterHandler.ANT_PROJECT,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), adapter.project());
//    item.add(AdapterHandler.ANT_BASEDIR,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(adapter.basedir()));
    item.add(AdapterHandler.ANT_DEFAULT,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), adapter.target());
    item.add(AdapterHandler.DESCRIPTION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.description());
    item.add(AdapterHandler.APPLICATION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.application());
    item.add(AdapterHandler.ADAPTER,                TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.application());
    item.add(AdapterHandler.SCHEDULER,              TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), scheduler.application());
    item.add(AdapterHandler.LIBRARY,                TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), thirdparty.application());
    item.add(AdapterHandler.DESTINATION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(adapter.destination()));
    item.add(AdapterHandler.PACKAGEPATH_ADAPTER,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.packagePath());
    item.add(AdapterHandler.PACKAGEPATH_SCHEDULER,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), scheduler.packagePath());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      AdapterHandler handler = new AdapterHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(AdapterHandler.ANT_PROJECT,           handler.name());
        item.property(AdapterHandler.ANT_BASEDIR,           handler.basedir());
        item.property(AdapterHandler.ANT_DEFAULT,           handler.target());
        item.property(AdapterHandler.DESCRIPTION,           handler.propertyValue(AdapterHandler.DESCRIPTION));
        item.property(AdapterHandler.APPLICATION,           handler.propertyValue(AdapterHandler.APPLICATION));
        item.property(AdapterHandler.ADAPTER,               handler.propertyValue(AdapterHandler.ADAPTER));
        item.property(AdapterHandler.SCHEDULER,             handler.propertyValue(AdapterHandler.SCHEDULER));
        item.property(AdapterHandler.LIBRARY,               handler.propertyValue(AdapterHandler.LIBRARY));
        item.property(AdapterHandler.DESTINATION,           item.relativeFile(handler.propertyValue(AdapterHandler.DESTINATION)));
        item.property(AdapterHandler.PACKAGEPATH_ADAPTER,   handler.propertyValue(AdapterHandler.PACKAGEPATH_ADAPTER));
        item.property(AdapterHandler.PACKAGEPATH_SCHEDULER, handler.propertyValue(AdapterHandler.PACKAGEPATH_SCHEDULER));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}