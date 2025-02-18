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
    11.1.1.3.37.60.17  2012-02-06  DSteding    Fixed Defect DE-000026
                                               SOA Server type not parsed from
                                               existing file
    11.1.1.3.37.60.28  2012-02-06  DSteding    Made the MDS storage of UI
                                               sandboxing configurable.
    11.1.1.3.37.60.32  2012-10-20  DSteding    Access to files changed from
                                               java.io.File to java.net.URL.
    11.1.1.3.37.60.33  2012-12-15  DSteding    Deployment Mode Feature
                                               introduced.
    11.1.1.3.37.60.34  2013-01-22  DSteding    MBean property Application
                                               removed due to it's always the
                                               same as the name of the WebLogic
                                               Managed Server.
    11.1.1.3.37.60.38  2013-07-10  DSteding    Workspace configuration to
                                               customize Identity Manager.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import oracle.ide.Ide;

import oracle.ide.net.URLFactory;

import oracle.ide.model.Workspace;

import oracle.ide.config.Preferences;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.model.BuildPropertyAdapter;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationConfigurator;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;

import oracle.jdeveloper.workspace.oim.model.OIMTarget;
import oracle.jdeveloper.workspace.oim.model.OIMContext;
import oracle.jdeveloper.workspace.oim.model.SOATarget;
import oracle.jdeveloper.workspace.oim.model.SOAContext;
import oracle.jdeveloper.workspace.oim.model.OIMPreference;
import oracle.jdeveloper.workspace.oim.model.SOAPreference;

import oracle.jdeveloper.workspace.oim.parser.OIMTargetHandler;
import oracle.jdeveloper.workspace.oim.parser.SOATargetHandler;
import oracle.jdeveloper.workspace.oim.parser.PreferenceHandler;

import oracle.jdeveloper.workspace.oim.preference.Store;

////////////////////////////////////////////////////////////////////////////////
// class WorkspaceConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class WorkspaceConfigurator extends ApplicationConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>WorkspaceConfigurator</code> for the specified
   ** {@link Workspace}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new WorkspaceConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  workspace          the {@link Workspace} this
   **                            {@link ApplicationConfigurator} belongs to.
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
    // create a folder where all the feature related files will be created
    // within
    final TemplateFolder featureRoot = workspaceFolder.add(featureFolder(this.workspaceFolder(), 0));
    featureRoot.add(WorkspaceConfigurator.templateSCPServerPreview(featureRoot, this.context));
    featureRoot.add(WorkspaceConfigurator.templateJEEServerPreview(featureRoot, this.context));
    featureRoot.add(WorkspaceConfigurator.templateOIMServerPreview(featureRoot, this.context));
    featureRoot.add(WorkspaceConfigurator.templateSOAServerPreview(featureRoot, this.context));
    featureRoot.add(WorkspaceConfigurator.templateMDSServerPreview(featureRoot, this.context));
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
    // create a folder where all the feature related files will be created
    // within
    workspaceFolder.add(featureFolder(this.workspaceFolder(), 0));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateOIMTargetPreview
  /**
   ** Creates the ANT target build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} of the preference
   **                            to include
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateOIMTargetPreview(final TemplateFolder folder, final TraversableContext context) {
    final OIMTarget      target = OIMTarget.instance(context);
    final TemplateStream item   = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(OIMTargetHandler.ANT_PROJECT,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  target.project());
    item.add(OIMTargetHandler.ANT_BASEDIR,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(target.basedir()));
    item.add(OIMTargetHandler.WKS_PACKAGE,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), TemplateWizardUtil.getApplicationPackage(context) + ".*");
    item.add(OIMTargetHandler.SPECIFICATION_VENDOR,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.specificationVendor());
    item.add(OIMTargetHandler.IMPLEMENTATION_VENDOR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.implementationVendor());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      OIMTargetHandler handler = new OIMTargetHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(OIMTargetHandler.ANT_PROJECT,           handler.name());
        item.property(OIMTargetHandler.ANT_BASEDIR,           handler.basedir());
        item.property(OIMTargetHandler.WKS_PACKAGE,           handler.packageName());
        item.property(OIMTargetHandler.SPECIFICATION_VENDOR,  handler.specificationVendor());
        item.property(OIMTargetHandler.IMPLEMENTATION_VENDOR, handler.implementationVendor());
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateOIMContextPreview
  /**
   ** Creates the ANT context build file for Oracle Identity Manager Server.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateOIMContextPreview(final TemplateFolder folder, final TraversableContext context) {
    final OIMContext     target = OIMContext.instance(context);
    final TemplateStream item   = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    // we don't need to substitute anythig in the file because its only an
    // accumulator which creates contextual wrappers only hence there should be
    // no dependency on the file system in this build file
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateSOAContextPreview
  /**
   ** Creates the ANT server context build file for Oracle Identity Manager
   ** projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateSOAContextPreview(final TemplateFolder folder, final TraversableContext context) {
    final SOAContext     target = SOAContext.instance(context);
    final TemplateStream item   = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    // we don't need to substitute anythig in the file because its only an
    // accumulator which creates contextual wrappers only hence there should be
    // no dependency on the file system in this build file
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateSOATargetPreview
  /**
   ** Creates the ANT target build file for Oracle Identity Manager
   ** workflows.
   **
   ** @param  folder             the {@link TemplateFolder} of the preference
   **                            to include
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream templateSOATargetPreview(final TemplateFolder folder, final TraversableContext context) {
    final SOATarget      target = SOATarget.instance(context);
    final TemplateStream item   = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(SOATargetHandler.ANT_PROJECT,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  target.project());
    item.add(SOATargetHandler.ANT_BASEDIR,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  item.relativeFile(target.basedir()));
    item.add(SOATargetHandler.WKS_PACKAGE,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), TemplateWizardUtil.getApplicationPackage(context) + ".*");
    item.add(SOATargetHandler.SPECIFICATION_VENDOR,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.specificationVendor());
    item.add(SOATargetHandler.IMPLEMENTATION_VENDOR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.implementationVendor());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      SOATargetHandler handler = new SOATargetHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(SOATargetHandler.ANT_PROJECT,           handler.name());
        item.property(SOATargetHandler.ANT_BASEDIR,           handler.basedir());
        item.property(SOATargetHandler.WKS_PACKAGE,           handler.propertyValue(OIMTargetHandler.WKS_PACKAGE));
        item.property(SOATargetHandler.SPECIFICATION_VENDOR,  handler.specificationVendor());
        item.property(SOATargetHandler.IMPLEMENTATION_VENDOR, handler.implementationVendor());
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateOIMPreferencePreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
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
  public static TemplateStream templateOIMPreferencePreview(final TemplateFolder folder, final TraversableContext context) {
    return templatePreferencePreview(folder, OIMPreference.instance(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateSOAPreferencePreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
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
  public static TemplateStream templateSOAPreferencePreview(final TemplateFolder folder, final TraversableContext context) {
    return templatePreferencePreview(folder, SOAPreference.instance(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mavenOIMPreferencePreview
  /**
   ** Creates the Maven Project Object Model preferences build file for Oracle
   ** Access Manager applications.
   **
   ** @param  folder             the {@link ModelFolder} where the file will
   **                            be created within.
   ** @param  context            the {@link TraversableContext) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link ModelStream} ready for
   **                            preview.
   */
  public static ModelStream mavenOIMPreferencePreview(final ModelFolder folder, final TraversableContext context) {
    return mavenPreferencePreview(folder, OIMPreference.instance(context));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the active workspace node wrapped in a
   ** {@link ApplicationConfigurator} representation.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the active project node as a
   **                            {@link ApplicationConfigurator} representation.
   */
  protected static TemplateApplicationConfigurator instance(final TraversableContext context) {
    return instance(Ide.getActiveWorkspace(), context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the specified workspace node configurator wrapped in a
   ** {@link ApplicationConfigurator}.
   **
   ** @param   workspace         the {@link Workspace} to configure.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    {@link ApplicationConfigurator} specific to
   **                            configure Oracle Identity Manager
   **                            {@link Workspace}s.
   */
  protected static TemplateApplicationConfigurator instance(final Workspace workspace, final TraversableContext context) {
    return new WorkspaceConfigurator(workspace, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templatePreferencePreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
   ** applications.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  preference         the {@link BuildPropertyAdapter) providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private static TemplateStream templatePreferencePreview(final TemplateFolder folder, final BuildPropertyAdapter preference) {
    final Store          store = Store.instance(Preferences.getPreferences());
    final TemplateStream item  = new TemplateStream(folder, preference.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(preference.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(PreferenceHandler.ANT_PROJECT, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), preference.project());
    item.add(PreferenceHandler.ANT_BASEDIR, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), item.relativeFile(preference.basedir()));
    item.add(PreferenceHandler.OIM_RELEASE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), store.release());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      PreferenceHandler handler = new PreferenceHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(PreferenceHandler.ANT_PROJECT, handler.name());
        item.property(PreferenceHandler.ANT_BASEDIR, handler.basedir());
        item.property(PreferenceHandler.OIM_RELEASE, handler.propertyValue(PreferenceHandler.OIM_RELEASE));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
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
  private static ModelStream mavenPreferencePreview(final ModelFolder folder, final BuildPropertyAdapter preference) {
    final Store       store = Store.instance(Preferences.getPreferences());
    final ModelStream item  = new ModelStream(folder, preference.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(preference.template(), "pom"));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
    }
    return item;
  }
}