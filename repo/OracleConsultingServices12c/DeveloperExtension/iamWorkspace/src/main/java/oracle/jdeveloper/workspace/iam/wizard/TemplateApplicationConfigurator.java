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
    Subsystem   :   Identity and Access Management Facilities

    File        :   TemplateApplicationConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    TemplateApplicationConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.16  2012-02-06  DSteding    TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.17  2012-07-15  DSteding    Fixed the unresolved protocol
                                               property in MDS preview
                                               generation. Also changed in the
                                               SCP server template the incorrect
                                               reference scp.server.hostname to
                                               scp.server.host.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.iam.wizard;

import java.io.File;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.Manifest;

import oracle.jdeveloper.workspace.iam.model.JEEServer;
import oracle.jdeveloper.workspace.iam.model.MDSServer;
import oracle.jdeveloper.workspace.iam.model.ODSServer;
import oracle.jdeveloper.workspace.iam.model.SCPServer;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.JEEServerHandler;
import oracle.jdeveloper.workspace.iam.parser.MDSServerHandler;
import oracle.jdeveloper.workspace.iam.parser.ODSServerHandler;
import oracle.jdeveloper.workspace.iam.parser.SCPServerHandler;
import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.project.maven.ModelData;
import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;

import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateData;
import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

////////////////////////////////////////////////////////////////////////////////
// abstract class TemplateApplicationConfigurator
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The {@link Workspace} configurator interface.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
public abstract class TemplateApplicationConfigurator extends TemplateObjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final File      folder;
  private final Workspace workspace;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TemplateApplicationConfigurator</code> for the
   ** specified {@link Workspace}.
   **
   ** @param  workspace          the {@link Workspace} this
   **                            {@link TemplateObjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  public TemplateApplicationConfigurator(final Workspace workspace, final TraversableContext context) {
    // ensure inhertitance
    super(context);

    // initialize instance attributes
    this.workspace = workspace;
    this.folder    = new File(this.workspace.getBaseDirectory());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspaceFolder
  /**
   ** Returns the {@link File} the {@link Workspace} the instance is configuring
   ** is located.
   **
   ** @return                    the {@link File} the {@link Workspace} the
   **                            instance is configuring is located.
   */
  public final File workspaceFolder() {
    return this.folder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workspace
  /**
   ** Returns the {@link Workspace} the configurator handles.
   **
   ** @return                    the {@link Workspace} the configurator
   **                            handles.
   */
  public final Workspace workspace() {
    return this.workspace;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base clases
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureFeature (TemplateObjectConfigurator)
  /**
   ** Creates the product related feature preferences ANT build file for the
   ** workspace.
   ** <p>
   ** Per default there is no feature related configuration related to a
   ** workspace only project will be related to.
   **
   ** @param  context            the {@link TraversableContext} where the data
   **                            are stored as design time objects.
   */
  @Override
  public final void configureFeature(final TraversableContext context) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateRootFolder (TemplateObjectConfigurator)
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link TemplateFolder} ready for
   **                            preview.
   */
  @Override
  public TemplateFolder templateRootFolder() {
    // build the workspace root folder the file to generate will be located
    // within (remember this does not need to be the folder of the Oracle
    // JDeveloper Workspace the related project is associated with)
    final TemplateFolder   iamRoot   = new TemplateFolder(featureFolder(this.workspaceFolder(), 2));
    final TemplateStream   iam       = iamRoot.add(templateRootPreview(iamRoot));
    final TemplateFolder   wksRoot   = iamRoot.add(featureFolder(this.workspaceFolder(), 1));
    final TemplateStream   workspace = wksRoot.add(templateWorkspacePreview(wksRoot));
    // we assume the workspace template specifies the pattern #{iam.preferences}
    // to import the root preference file
    workspace.include(Manifest.IAM_PREFERENCE, iam);
    templateFeaturePreview(workspace.folder());
    return iamRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenRootFolder (TemplateObjectConfigurator)
  /**
   ** Creates the starting root node of the buildfile hierarchy with all
   ** the options of each particular node. This gives the end user the ability
   ** to adopt those option especially the substitution parameter.
   **
   ** @return                    the created {@link ModelFolder} ready for
   **                            preview.
   */
  public ModelFolder mavenRootFolder() {
    // build the workspace root folder the file to generate will be located
    // within (remember this does not need to be the folder of the Oracle
    // JDeveloper Workspace the related project is associated with)
    final ModelFolder      iamRoot   = new ModelFolder(featureFolder(this.workspaceFolder(), 2));
    final ModelStream      iam       = iamRoot.add(mavenRootPreview(iamRoot));
    final ModelFolder      wksRoot   = iamRoot.add(featureFolder(this.workspaceFolder(), 1));
    final ModelStream      workspace = wksRoot.add(mavenWorkspacePreview(wksRoot));
    // we assume the workspace template specifies the pattern #{iam.preferences}
    // to import the root preference file
    workspace.model().setParent(ModelData.parentReference(iam.model(), workspace.relativeFile(iamRoot.folder().getPath()).toString()));
    mavenFeaturePreview(workspace.folder());
    return iamRoot;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateSCPServerPreview
  /**
   ** Creates the ANT preferences build file for Secure Shell Server.
   ** <p>
   ** The created {@link TemplateData} node is added as a dependant to the
   ** specified superior {@link TemplateData} node.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateSCPServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final SCPServer      server = SCPServer.instance(context);
    final TemplateStream item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(SCPServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(SCPServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(SCPServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(SCPServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(SCPServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(SCPServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    item.add(SCPServerHandler.CERTIFICAT,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.absoluteFile(server.certificate()));
    item.add(SCPServerHandler.PASSPHRASE,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.passphrase());
    item.add(SCPServerHandler.TRUSTED,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), Boolean.toString(server.trusted()));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      SCPServerHandler handler = new SCPServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(SCPServerHandler.ANT_PROJECT, handler.name());
        item.property(SCPServerHandler.PLATFORM,    handler.propertyValue(SCPServerHandler.PLATFORM));
        item.property(SCPServerHandler.HOST,        handler.propertyValue(SCPServerHandler.HOST));
        item.property(SCPServerHandler.PORT,        handler.propertyValue(SCPServerHandler.PORT));
        item.property(SCPServerHandler.USERNAME,    handler.propertyValue(SCPServerHandler.USERNAME));
        item.property(SCPServerHandler.PASSWORD,    handler.propertyValue(SCPServerHandler.PASSWORD));
        item.property(SCPServerHandler.CERTIFICAT,  item.absoluteFile(handler.propertyValue(SCPServerHandler.CERTIFICAT)));
        item.property(SCPServerHandler.PASSPHRASE,  handler.propertyValue(SCPServerHandler.PASSPHRASE));
        item.property(SCPServerHandler.TRUSTED,     handler.propertyValue(SCPServerHandler.TRUSTED));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateJEEServerPreview
  /**
   ** Creates the ANT preferences build file for Oracle Weblogic Server.
   ** <p>
   ** The created {@link TemplateData} node is added as a dependant to the
   ** specified superior {@link TemplateData} node.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateJEEServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final JEEServer      server = JEEServer.instance(context);
    final TemplateStream item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(JEEServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(JEEServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(JEEServerHandler.PROTOCOL,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.protocol());
    item.add(JEEServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(JEEServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(JEEServerHandler.HOME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.home());
    item.add(JEEServerHandler.NAME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.server());
    item.add(JEEServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(JEEServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      JEEServerHandler handler = new JEEServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(JEEServerHandler.ANT_PROJECT, handler.name());
        item.property(JEEServerHandler.PLATFORM,    handler.propertyValue(JEEServerHandler.PLATFORM));
        item.property(JEEServerHandler.PROTOCOL,    handler.propertyValue(JEEServerHandler.PROTOCOL));
        item.property(JEEServerHandler.HOST,        handler.propertyValue(JEEServerHandler.HOST));
        item.property(JEEServerHandler.PORT,        handler.propertyValue(JEEServerHandler.PORT));
        item.property(JEEServerHandler.HOME,        handler.propertyValue(JEEServerHandler.HOME));
        item.property(JEEServerHandler.NAME,        handler.propertyValue(JEEServerHandler.NAME));
        item.property(JEEServerHandler.USERNAME,    handler.propertyValue(JEEServerHandler.USERNAME));
        item.property(JEEServerHandler.PASSWORD,    handler.propertyValue(JEEServerHandler.PASSWORD));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateMDSServerPreview
  /**
   ** Creates the ANT preferences build file for a Oracle Metadata Service.
   ** <p>
   ** The created {@link TemplateData} node is added as a dependant to the
   ** specified superior {@link TemplateData} node.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateMDSServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final MDSServer      server = MDSServer.instance(context);
    final TemplateStream item = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(MDSServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(MDSServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(MDSServerHandler.PROTOCOL,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.protocol());
    item.add(MDSServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(MDSServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(MDSServerHandler.SERVICE,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.service());
    item.add(MDSServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(MDSServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      MDSServerHandler handler = new MDSServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(MDSServerHandler.ANT_PROJECT, handler.name());
        item.property(MDSServerHandler.PLATFORM,    handler.propertyValue(MDSServerHandler.PLATFORM));
        item.property(MDSServerHandler.PROTOCOL,    handler.propertyValue(MDSServerHandler.PROTOCOL));
        item.property(MDSServerHandler.HOST,        handler.propertyValue(MDSServerHandler.HOST));
        item.property(MDSServerHandler.PORT,        handler.propertyValue(MDSServerHandler.PORT));
        item.property(MDSServerHandler.SERVICE,     handler.propertyValue(MDSServerHandler.SERVICE));
        item.property(MDSServerHandler.USERNAME,    handler.propertyValue(MDSServerHandler.USERNAME));
        item.property(MDSServerHandler.PASSWORD,    handler.propertyValue(MDSServerHandler.PASSWORD));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateODSServerPreview
  /**
   ** Creates the ANT preferences build file for Oracle Internet Directory
   ** Server.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateODSServerPreview(final TemplateFolder folder, final TraversableContext context) {
    final ODSServer server = ODSServer.instance(context);
    TemplateStream  item   = new TemplateStream(folder, server.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(server.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(ODSServerHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), server.project());
    item.add(ODSServerHandler.PLATFORM,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.platform());
    item.add(ODSServerHandler.HOST,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.host());
    item.add(ODSServerHandler.PORT,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.port());
    item.add(ODSServerHandler.HOME,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.home());
    item.add(ODSServerHandler.INSTANCE,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.instance());
    item.add(ODSServerHandler.CONTEXT,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.context());
    item.add(ODSServerHandler.USERNAME,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.username());
    item.add(ODSServerHandler.PASSWORD,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), server.password());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists() || server.override());
    if (item.exists() && !server.override()) {
      ODSServerHandler handler = new ODSServerHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(ODSServerHandler.ANT_PROJECT, handler.name());
        item.property(ODSServerHandler.PLATFORM,    handler.propertyValue(ODSServerHandler.PLATFORM));
        item.property(ODSServerHandler.HOST,        handler.propertyValue(ODSServerHandler.HOST));
        item.property(ODSServerHandler.PORT,        handler.propertyValue(ODSServerHandler.PORT));
        item.property(ODSServerHandler.HOME,        handler.propertyValue(ODSServerHandler.HOME));
        item.property(ODSServerHandler.INSTANCE,    handler.propertyValue(ODSServerHandler.INSTANCE));
        item.property(ODSServerHandler.CONTEXT,     handler.propertyValue(ODSServerHandler.CONTEXT));
        item.property(ODSServerHandler.USERNAME,    handler.propertyValue(ODSServerHandler.USERNAME));
        item.property(ODSServerHandler.PASSWORD,    handler.propertyValue(ODSServerHandler.PASSWORD));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }
}