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

    File        :   WorkspaceConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    WorkspaceConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.38  2013-07-10  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oam.project;

import oracle.ide.net.URLFactory;

import oracle.ide.model.TechId;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationConfigurator;

import oracle.jdeveloper.workspace.oam.model.OAMServer;

import oracle.jdeveloper.workspace.oam.parser.OAMServerHandler;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Access Manager application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.60.38
 */
abstract class ApplicationConfigurator extends TemplateApplicationConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ApplicationConfigurator</code> for the specified
   ** {@link Workspace}.
   ** <br>
   ** This constructor is protected to prevent other classes to use
   ** "new ApplicationConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  workspace          the {@link Workspace} this
   **                            {@link TemplateApplicationConfigurator} belongs
   **                            to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  protected ApplicationConfigurator(final Workspace workspace, final TraversableContext context) {
    // ensure inhertitance
    super(workspace, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isRelated (TemplateObjectConfigurator)
  /**
   ** Returns <code>true</code> if the project node is related to Oracle Access
   ** Manager.
   **
   ** @param  technology         the {@link TechId} that must march the defined
   **                            technologies in the {@link Workspace}.
   **
   ** @return                    <code>true</code> if the workspace node
   **                            represented by this configurator is related to;
   **                            otherwise <code>false</code>.
   */
  @Override
  public boolean isRelated(final TechId technology) {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (TemplateObjectConfigurator)
  /**
   ** Configures a Oracle JDeveloper {@link Workspace} completely.
   */
  @Override
  public final void configure() {
    final TemplateFolder    root      = templateRootFolder();
    final TemplateGenerator generator = new TemplateGenerator(root);
    generator.startProgress();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateOAMServerPreview
  /**
   ** Creates the ANT preferences build file for Oracle Access Manager Server.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateOAMServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final OAMServer server = OAMServer.instance(context);
    TemplateStream  item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(OAMServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(OAMServerHandler.ANT_BASEDIR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(server.basedir()));
    item.add(OAMServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(OAMServerHandler.PROTOCOL,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.protocol());
    item.add(OAMServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(OAMServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(OAMServerHandler.NAME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.server());
    item.add(OAMServerHandler.HOME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.home());
    item.add(OAMServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(OAMServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      OAMServerHandler handler = new OAMServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(OAMServerHandler.ANT_PROJECT, handler.name());
        item.property(OAMServerHandler.ANT_BASEDIR, handler.basedir());
        item.property(OAMServerHandler.PLATFORM,    handler.propertyValue(OAMServerHandler.PLATFORM));
        item.property(OAMServerHandler.PROTOCOL,    handler.propertyValue(OAMServerHandler.PROTOCOL));
        item.property(OAMServerHandler.HOST,        handler.propertyValue(OAMServerHandler.HOST));
        item.property(OAMServerHandler.PORT,        handler.propertyValue(OAMServerHandler.PORT));
        item.property(OAMServerHandler.NAME,        handler.propertyValue(OAMServerHandler.NAME));
        item.property(OAMServerHandler.HOME,        handler.propertyValue(OAMServerHandler.HOME));
        item.property(OAMServerHandler.USERNAME,    handler.propertyValue(OAMServerHandler.USERNAME));
        item.property(OAMServerHandler.PASSWORD,    handler.propertyValue(OAMServerHandler.USERNAME));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}