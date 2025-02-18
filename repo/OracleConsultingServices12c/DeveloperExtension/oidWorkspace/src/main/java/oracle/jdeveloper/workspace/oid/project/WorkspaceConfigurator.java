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
    Subsystem   :   Internet Directory Facility

    File        :   WorkspaceConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    WorkspaceConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15  2012-02-06  DSteding    The name of the managed server is
                                               included now in the properties.
                                               --
                                               TemplateFile and TemplateStream
                                               handled specific to allow the
                                               load of XML descroptor files
                                               itself in the previewer.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oid.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.TechId;
import oracle.ide.model.Workspace;

import oracle.ide.panels.TraversableContext;

import oracle.ide.config.Preferences;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationConfigurator;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;

import oracle.jdeveloper.workspace.oid.preference.Store;

import oracle.jdeveloper.workspace.oid.model.Target;
import oracle.jdeveloper.workspace.oid.model.Preference;

import oracle.jdeveloper.workspace.oid.parser.TargetHandler;
import oracle.jdeveloper.workspace.oid.parser.PreferenceHandler;

////////////////////////////////////////////////////////////////////////////////
// class WorkspaceConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Internet Directory application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class WorkspaceConfigurator extends TemplateApplicationConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>FeatureConfigurator</code> for the specified
   ** {@link Workspace}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new WorkspaceConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  workspace          the {@link Workspace} this
   **                            {@link TemplateApplicationConfigurator} belongs
   **                            to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private WorkspaceConfigurator(final Workspace workspace, final TraversableContext context) {
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
    // all projects will use preference files to globalize common properties
    final TemplateFolder featureRoot = workspaceFolder.add(featureFolder(this.workspaceFolder(), 0));
    featureRoot.add(templatePreferencePreview(featureRoot, this.context));
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
    // all projects will use preference files to globalize common properties
    final ModelFolder featureRoot = workspaceFolder.add(featureFolder(this.workspaceFolder(), 0));
    featureRoot.add(mavenPreferencePreview(featureRoot, this.context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templatePreferencePreview
  /**
   ** Creates the ANT preferences build file for Oracle Internet Directory
   ** applications.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templatePreferencePreview(final TemplateFolder folder, final TraversableContext context) {
    final Preference     config = Preference.instance(context);
    final Store          store  = Store.instance(Preferences.getPreferences());
    final TemplateStream item   = new TemplateStream(folder, config.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(config.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(PreferenceHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), config.project());
    item.add(PreferenceHandler.OID_RELEASE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), store.release());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      PreferenceHandler handler = new PreferenceHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(PreferenceHandler.ANT_PROJECT, handler.name());
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    item.include(oracle.jdeveloper.workspace.iam.Manifest.SCP_SERVER, folder.add(templateSCPServerPreview(folder, context)));
    item.include(oracle.jdeveloper.workspace.iam.Manifest.OID_SERVER, folder.add(templateODSServerPreview(folder, context)));
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenPreferencePreview
  /**
   ** Creates the Maven Project Object Model preferences build file for Oracle
   ** Virtual Directory applications.
   **
   ** @param  folder             the {@link ModelFolder} where the file will be
   **                            created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link ModelStream} ready for
   **                            preview.
   */
  public static ModelStream mavenPreferencePreview(final ModelFolder folder, final TraversableContext context) {
    final Preference  config = Preference.instance(context);
    final Store       store  = Store.instance(Preferences.getPreferences());
    final ModelStream item   = new ModelStream(folder, config.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(config.template(), "pom"));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetPreview
  /**
   ** Creates the ANT target build file for Oracle Internet Directory
   ** applications.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream targetPreview(final TemplateFolder folder, final TraversableContext context) {
    final Target         target = Target.instance(context);
    final TemplateStream item   = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(TargetHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), target.project());
    item.add(TargetHandler.WKS_PACKAGE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), "oracle");
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      TargetHandler handler = new TargetHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(TargetHandler.ANT_PROJECT, handler.name());
        item.property(TargetHandler.WKS_PACKAGE, handler.propertyValue(TargetHandler.WKS_PACKAGE));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the active workspace node wrapped in a
   ** {@link TemplateApplicationConfigurator} representation.
   ** representation.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the active project node as a
   **                            {@link TemplateApplicationConfigurator}
   **                            representation.
   */
  protected static TemplateApplicationConfigurator instance(final TraversableContext context) {
    return instance(Ide.getActiveWorkspace(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the specified workspace node configurator wrapped in a
   ** {@link TemplateApplicationConfigurator}.
   **
   ** @param   workspace         the {@link Workspace} to configure.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    {@link TemplateApplicationConfigurator}
   **                            specific to configure Oracle Internet Directory
   **                            {@link Workspace}s.
   */
  protected static TemplateApplicationConfigurator instance(final Workspace workspace, final TraversableContext context) {
    return new WorkspaceConfigurator(workspace, context);
  }
}