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

    File        :   CustomizationConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    CustomizationConfigurator.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.38  2013-07-10  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import oracle.ide.Ide;

import oracle.ide.model.Workspace;

import oracle.ide.net.URLFactory;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.template.wizard.TemplateWizardUtil;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;

import oracle.jdeveloper.workspace.iam.wizard.TemplateApplicationConfigurator;

import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;

import oracle.jdeveloper.workspace.oim.model.ADFTarget;

import oracle.jdeveloper.workspace.oim.parser.ADFTargetHandler;

////////////////////////////////////////////////////////////////////////////////
// class CustomizationConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager customization application.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.60.38
 */
public class CustomizationConfigurator extends ApplicationConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>CustomizationConfigurator</code> for the specified
   ** {@link Workspace}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CustomizationConfigurator()" and enforces use of the public factory
   ** method below.
   **
   ** @param  workspace          the {@link Workspace} this
   **                            {@link ApplicationConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private CustomizationConfigurator(final Workspace workspace, final TraversableContext context) {
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
    featureRoot.add(CustomizationConfigurator.templateSCPServerPreview(featureRoot, this.context));
    featureRoot.add(CustomizationConfigurator.templateJEEServerPreview(featureRoot, this.context));
    featureRoot.add(CustomizationConfigurator.templateSOAServerPreview(featureRoot, this.context));
    featureRoot.add(CustomizationConfigurator.templateOIMServerPreview(featureRoot, this.context));
    featureRoot.add(CustomizationConfigurator.templateMDSServerPreview(featureRoot, this.context));
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
  // Method:   createADFTargetPreview
  /**
   ** Creates the ANT target build file for Oracle Identity Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} of the preference
   **                            to include
   ** @param  context            the {@link TraversableContext} providing the
   **                            substitution values.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  public static TemplateStream createADFTargetPreview(final TemplateFolder folder, final TraversableContext context) {
    final ADFTarget      target    = ADFTarget.instance(context);
    final TemplateStream item      = new TemplateStream(folder, target.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(target.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(ADFTargetHandler.ANT_PROJECT,                    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON),  target.project());
    item.add(ADFTargetHandler.WKS_PACKAGE,                    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), TemplateWizardUtil.getApplicationPackage(context) + ".*");
    item.add(ADFTargetHandler.SPECIFICATION_VENDOR,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.specificationVendor());
    item.add(ADFTargetHandler.IMPLEMENTATION_VENDOR,          TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_JAVA_ICON), target.implementationVendor());
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      ADFTargetHandler handler = new ADFTargetHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(ADFTargetHandler.ANT_PROJECT,           handler.name());
        item.property(ADFTargetHandler.WKS_PACKAGE,           handler.packageName());
        item.property(ADFTargetHandler.SPECIFICATION_VENDOR,  handler.specificationVendor());
        item.property(ADFTargetHandler.IMPLEMENTATION_VENDOR, handler.implementationVendor());
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
   ** Returns the active workspace node wrapped in an
   ** {@link ApplicationConfigurator} representation.
   **
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   **
   ** @return                    the active project node as an
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
    return new CustomizationConfigurator(workspace, context);
  }
}