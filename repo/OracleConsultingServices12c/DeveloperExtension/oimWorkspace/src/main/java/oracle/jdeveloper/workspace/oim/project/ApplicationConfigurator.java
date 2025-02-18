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
    Subsystem   :   Identity Manager Facility

    File        :   ApplicationConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ApplicationConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.38  2013-07-10  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

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

import oracle.jdeveloper.workspace.oim.model.OIMServer;
import oracle.jdeveloper.workspace.oim.model.SOAServer;

import oracle.jdeveloper.workspace.oim.parser.OIMServerHandler;
import oracle.jdeveloper.workspace.oim.parser.SOAServerHandler;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager application.
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
  // Method:   templateOIMPServerPreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager Server.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateOIMServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final OIMServer server = OIMServer.instance(context);
    TemplateStream  item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(OIMServerHandler.ANT_PROJECT,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(OIMServerHandler.ANT_BASEDIR,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(server.basedir()));
    item.add(OIMServerHandler.PLATFORM,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(OIMServerHandler.PROTOCOL,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.protocol());
    item.add(OIMServerHandler.HOST,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(OIMServerHandler.PORT,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(OIMServerHandler.HOME,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.home());
    item.add(OIMServerHandler.NAME,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.server());
    item.add(OIMServerHandler.MODE,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), String.valueOf(server.mode()));
    item.add(OIMServerHandler.USERNAME,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(OIMServerHandler.PASSWORD,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    item.add(OIMServerHandler.AUTHENTICATION, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.absoluteFile(server.authenticationConfig()));
    item.add(OIMServerHandler.PARTITION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.partition());
    item.add(OIMServerHandler.METADATA,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.metadata());
    item.add(OIMServerHandler.SANDBOX,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.sandbox());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      OIMServerHandler handler = new OIMServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(OIMServerHandler.ANT_PROJECT,    handler.name());
        item.property(OIMServerHandler.ANT_PROJECT,    handler.basedir());
        item.property(OIMServerHandler.PLATFORM,       handler.propertyValue(OIMServerHandler.PLATFORM));
        item.property(OIMServerHandler.PROTOCOL,       handler.propertyValue(OIMServerHandler.PROTOCOL));
        item.property(OIMServerHandler.HOST,           handler.propertyValue(OIMServerHandler.HOST));
        item.property(OIMServerHandler.PORT,           handler.propertyValue(OIMServerHandler.PORT));
        item.property(OIMServerHandler.HOME,           handler.propertyValue(OIMServerHandler.HOME));
        item.property(OIMServerHandler.NAME,           handler.propertyValue(OIMServerHandler.NAME));
        item.property(OIMServerHandler.MODE,           handler.propertyValue(OIMServerHandler.MODE));
        item.property(OIMServerHandler.USERNAME,       handler.propertyValue(OIMServerHandler.USERNAME));
        item.property(OIMServerHandler.PASSWORD,       handler.propertyValue(OIMServerHandler.PASSWORD));
        item.property(OIMServerHandler.AUTHENTICATION, item.absoluteFile(handler.propertyValue(OIMServerHandler.AUTHENTICATION)));
        item.property(OIMServerHandler.PARTITION,      handler.propertyValue(OIMServerHandler.PARTITION));
        item.property(OIMServerHandler.METADATA,       handler.propertyValue(OIMServerHandler.METADATA));
        item.property(OIMServerHandler.SANDBOX,        handler.propertyValue(OIMServerHandler.SANDBOX));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateSOAServerPreview
  /**
   ** Creates the ANT preferences build file for a Oracle SOA Suite.
   ** <p>
   ** The created {@link TemplateStream} node is added as a dependant to the
   ** specified superior {@link TemplateStream} node.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateSOAServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final SOAServer server = SOAServer.instance(context);
    TemplateStream  item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(SOAServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(SOAServerHandler.ANT_BASEDIR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(server.basedir()));
    item.add(SOAServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(SOAServerHandler.PROTOCOL,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.protocol());
    item.add(SOAServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(SOAServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(SOAServerHandler.HOME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.home());
    item.add(SOAServerHandler.NAME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.server());
    item.add(SOAServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(SOAServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    item.add(SOAServerHandler.PARTITION,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.partition());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      SOAServerHandler handler = new SOAServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(SOAServerHandler.ANT_PROJECT, handler.name());
        item.property(SOAServerHandler.ANT_PROJECT, handler.basedir());
        item.property(SOAServerHandler.PLATFORM,    handler.propertyValue(SOAServerHandler.PLATFORM));
        item.property(SOAServerHandler.PROTOCOL,    handler.propertyValue(SOAServerHandler.PROTOCOL));
        item.property(SOAServerHandler.HOST,        handler.propertyValue(SOAServerHandler.HOST));
        item.property(SOAServerHandler.PORT,        handler.propertyValue(SOAServerHandler.PORT));
        item.property(SOAServerHandler.HOME,        handler.propertyValue(SOAServerHandler.HOME));
        item.property(SOAServerHandler.NAME,        handler.propertyValue(SOAServerHandler.NAME));
        item.property(SOAServerHandler.USERNAME,    handler.propertyValue(SOAServerHandler.USERNAME));
        item.property(SOAServerHandler.PASSWORD,    handler.propertyValue(SOAServerHandler.PASSWORD));
        item.property(SOAServerHandler.PARTITION,   handler.propertyValue(SOAServerHandler.PARTITION));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}