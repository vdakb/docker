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

    File        :   ConnectorConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ConnectorConfigurator.


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
import oracle.jdeveloper.workspace.oim.model.Connector;
import oracle.jdeveloper.workspace.oim.model.Scheduler;
import oracle.jdeveloper.workspace.oim.model.Diagnostic;
import oracle.jdeveloper.workspace.oim.model.Thirdparty;

import oracle.jdeveloper.workspace.oim.parser.ConnectorHandler;

////////////////////////////////////////////////////////////////////////////////
// class ConnectorConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Connector projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class ConnectorConfigurator extends ProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ConnectorConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new ConnectorConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private ConnectorConfigurator(final Project project, final TraversableContext context) {
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
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  @Override
  protected void templateBuildPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream item = folder.add(buildPreview(folder));
    item.include(Manifest.OIM_TARGET, target);
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
    final Connector provider = Connector.instance(this.context);
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
    final Connector provider = Connector.instance(this.context);
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
    return new ConnectorConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildPreview
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
  private TemplateStream buildPreview(final TemplateFolder folder) {
    final Connector      connector  = Connector.instance(this.context);
    final Adapter        adapter    = Adapter.instance(this.context);
    final Scheduler      scheduler  = Scheduler.instance(this.context);
    final Thirdparty     thirdparty = Thirdparty.instance(this.context);
    final Diagnostic     diagnostic = Diagnostic.instance(this.context);
    final TemplateStream item       = new TemplateStream(folder, connector.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(connector.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(ConnectorHandler.ANT_PROJECT,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), connector.project());
//    item.add(ConnectorHandler.ANT_BASEDIR,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(connector.basedir()));
    item.add(ConnectorHandler.ANT_DEFAULT,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), connector.target());
    item.add(ConnectorHandler.DESCRIPTION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), connector.description());
    item.add(ConnectorHandler.APPLICATION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), connector.application());
    item.add(ConnectorHandler.ADAPTER,                TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.application());
    item.add(ConnectorHandler.SCHEDULER,              TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), scheduler.application());
    item.add(ConnectorHandler.LIBRARY,                TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), thirdparty.application());
    item.add(ConnectorHandler.DIAGNOSTIC,             TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), diagnostic.application());
    item.add(ConnectorHandler.DESTINATION,            TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(connector.destination()));
    item.add(ConnectorHandler.DESTINATION_TARGET,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(connector.destinationTarget()));
    item.add(ConnectorHandler.DESTINATION_TRUSTED,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(connector.destinationTrusted()));
    item.add(ConnectorHandler.PACKAGEPATH_ADAPTER,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), adapter.packagePath());
    item.add(ConnectorHandler.PACKAGEPATH_SCHEDULER,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), scheduler.packagePath());
    item.add(ConnectorHandler.PACKAGEPATH_DIAGNOSTIC, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), diagnostic.packagePath());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      ConnectorHandler handler = new ConnectorHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(ConnectorHandler.ANT_PROJECT,            handler.name());
        item.property(ConnectorHandler.ANT_BASEDIR,            handler.basedir());
        item.property(ConnectorHandler.ANT_DEFAULT,            handler.target());
        item.property(ConnectorHandler.DESCRIPTION,            handler.propertyValue(ConnectorHandler.DESCRIPTION));
        item.property(ConnectorHandler.APPLICATION,            handler.propertyValue(ConnectorHandler.APPLICATION));
        item.property(ConnectorHandler.ADAPTER,                handler.propertyValue(ConnectorHandler.ADAPTER));
        item.property(ConnectorHandler.SCHEDULER,              handler.propertyValue(ConnectorHandler.SCHEDULER));
        item.property(ConnectorHandler.LIBRARY,                handler.propertyValue(ConnectorHandler.LIBRARY));
        item.property(ConnectorHandler.DIAGNOSTIC,             handler.propertyValue(ConnectorHandler.DIAGNOSTIC));
        item.property(ConnectorHandler.DESTINATION,            item.relativeFile(handler.propertyValue(ConnectorHandler.DESTINATION)));
        item.property(ConnectorHandler.DESTINATION_TARGET,     item.relativeFile(handler.propertyValue(ConnectorHandler.DESTINATION_TARGET)));
        item.property(ConnectorHandler.DESTINATION_TRUSTED,    item.relativeFile(handler.propertyValue(ConnectorHandler.DESTINATION_TRUSTED)));
        item.property(ConnectorHandler.PACKAGEPATH_ADAPTER,    handler.propertyValue(ConnectorHandler.PACKAGEPATH_ADAPTER));
        item.property(ConnectorHandler.PACKAGEPATH_SCHEDULER,  handler.propertyValue(ConnectorHandler.PACKAGEPATH_SCHEDULER));
        item.property(ConnectorHandler.PACKAGEPATH_DIAGNOSTIC, handler.propertyValue(ConnectorHandler.PACKAGEPATH_DIAGNOSTIC));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}