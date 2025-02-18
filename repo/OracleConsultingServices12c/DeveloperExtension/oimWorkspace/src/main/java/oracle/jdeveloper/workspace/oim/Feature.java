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

    File        :   Feature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Feature.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.44  2013-10-24  DSteding    Added support to discover base
                                               technology this feature
                                               implements.
    11.1.1.3.37.60.66  2015-09-12  DSteding    Extended support for Library
                                               projects
    11.1.1.3.37.60.72  2017-11-29  DSteding    Deployment templates referencing
                                               formerly defined namespace
                                               http://oracle.hst.deployment/weblogic
                                               now as
                                               http://oracle.hst.deployment/topology.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oim;

import java.util.List;
import java.util.ArrayList;

import oracle.ide.model.TechId;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyRegistry;

import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.workspace.iam.AbstractAddin;

import oracle.jdeveloper.workspace.oim.gallery.PluginWizard;
import oracle.jdeveloper.workspace.oim.gallery.LibraryWizard;
import oracle.jdeveloper.workspace.oim.gallery.AdapterWizard;
import oracle.jdeveloper.workspace.oim.gallery.BackendWizard;
import oracle.jdeveloper.workspace.oim.gallery.FrontendWizard;
import oracle.jdeveloper.workspace.oim.gallery.AssemblyWizard;
import oracle.jdeveloper.workspace.oim.gallery.WorkflowWizard;
import oracle.jdeveloper.workspace.oim.gallery.ConnectorWizard;
import oracle.jdeveloper.workspace.oim.gallery.DeploymentWizard;
import oracle.jdeveloper.workspace.oim.gallery.ApplicationWizard;
import oracle.jdeveloper.workspace.oim.gallery.CustomizationWizard;

////////////////////////////////////////////////////////////////////////////////
// class Feature
// ~~~~~ ~~~~~~~
/**
 ** Addin for application and project functionality within Oracle
 ** JDeveloper to handle Identity Manager artifacts.
 **
 ** The {@link AbstractAddin} provides the mechanism for this JDeveloper
 ** extensions to carry out programmatic initialization during the startup
 ** sequence of the JDeveloper IDE.
 ** <br>
 ** The initialize() method of this {@link AbstractAddin} is called prior to
 ** the display of the main window.
 ** <br>
 ** The registration is done in the <code>addins</code> section of the extension
 ** manifest. For more information on the extension manifest, see the
 ** documentation in jdev\doc\extension.
 ** <br>
 ** Care should be taken when implementing the initialize() method to minimize
 ** the work carried out and the classes loaded. If it is possible to defer some
 ** initialization until a later point, it will be deferred.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public final class Feature extends AbstractAddin {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Feature</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Feature() {
    // ensure inheritance
    super(Manifest.string(Manifest.FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (Addin)
  /**
   ** Invoked by the super class after the instance of the
   ** {@link AbstractAddin} is instantiated by the <code>AddinManager</code>.
   ** <br>
   ** The method is called prior to the display of the main window.
   **
   ** @see  AbstractAddin#initialize
   */
  @Override
  public void initialize() {
    createTemplate();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   featureTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager.
   */
  public static TechId featureTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.TECHNOLOGY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   libraryTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Libraries.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Libraries.
   */
  public static TechId libraryTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.LIBRARY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   adapterTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Adapters.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Adapters.
   */
  public static TechId adapterTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.ADAPTER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pluginTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Plug-In.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Plug-In.
   */
  public static TechId pluginTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.PLUGIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connectorTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Connector.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Connector.
   */
  public static TechId connectorTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.CONNECTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploymentTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Deployments.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Deployments.
   */
  public static TechId deploymentTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OIM_DEPLOYMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontendTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Customization Frontends.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Customization Frontends.
   */
  public static TechId frontendTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OIM_FRONTEND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   backendTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Customization Models.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Customization Models.
   */
  public static TechId backendTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OIM_BACKEND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assemblyTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Customization Libraries.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Customization Libraries.
   */
  public static TechId assemblyTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OIM_ASSEMBLY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   workflowTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Identity Manager Workflows.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Identity Manager Workflows.
   */
  public static TechId workflowTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.WORKFLOW);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oimServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a OIM Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a OIM
   **                            Server.
   */
  public static TechId oimServerTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OIM_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   soaServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a SOA Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a SOA
   **                            Server.
   */
  public static TechId soaServerTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.SOA_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTemplate
  /**
   ** Creates the project and application templates specific for Oracle Identity
   ** Manager and registers the gallery items for each of them.
   */
  private void createTemplate() {
    // register the standard development project template
    createLibraryTemplate();
    createAdapterTemplate();
    createPluginTemplate();
    createConnectorTemplate();
    createDeploymentTemplate();
    // register the customization development project template
    createBackendTemplate();
    createFrontendTemplate();
    createAssemblyTemplate();
    // register the workflow development project template
    createWorkflowTemplate();

    // register the standard development application template
    final List<ProjectTemplate> template = new ArrayList<ProjectTemplate>();
    createApplicationTemplate(template);

    // register the customization development application template
    createCustomizationTemplate(template);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLibraryTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager Library
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Library development.
   */
  private ProjectTemplate createLibraryTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.LIBRARY));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.LIBRARY);
    template.setName(Manifest.string(Manifest.LIBRARY));
    template.setIconFile(Manifest.string(Manifest.LIBRARY_ICON));
    template.setDescription(Manifest.string(Manifest.LIBRARY_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class AdapterWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, LibraryWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdapterTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager Adapter
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Adapter development.
   */
  private ProjectTemplate createAdapterTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.ADAPTER));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.ADAPTER);
    template.setName(Manifest.string(Manifest.ADAPTER));
    template.setIconFile(Manifest.string(Manifest.ADAPTER_ICON));
    template.setDescription(Manifest.string(Manifest.ADAPTER_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class AdapterWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, AdapterWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPluginTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager Adapter
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Adapter development.
   */
  private ProjectTemplate createPluginTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.PLUGIN));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.PLUGIN);
    template.setName(Manifest.string(Manifest.PLUGIN));
    template.setIconFile(Manifest.string(Manifest.PLUGIN_ICON));
    template.setDescription(Manifest.string(Manifest.PLUGIN_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class PluginWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, PluginWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createConnectorTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager
   ** Connector development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Connector development.
   */
  private ProjectTemplate createConnectorTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.CONNECTOR));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.CONNECTOR);
    template.setName(Manifest.string(Manifest.CONNECTOR));
    template.setIconFile(Manifest.string(Manifest.CONNECTOR_ICON));
    template.setDescription(Manifest.string(Manifest.CONNECTOR_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class ConnectorWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, ConnectorWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createWorkflowTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager Workflow
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Workflow development.
   */
  private ProjectTemplate createWorkflowTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    // we cannot add the technology keys that belongs to the SOA Composite here
    // due to the wizard sequence generator will than create the property page
    // for the project to create
    // the entire project configuration we don't want to expose to the end user
    // to prevent mistakes in the project setup hence the technologies will be
    // injected later if the wizard completes
    // (see WorkflowWizard.Flow.postProcessing)
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
//    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.WORKFLOW));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.WORKFLOW);
    template.setName(Manifest.string(Manifest.WORKFLOW));
    template.setIconFile(Manifest.string(Manifest.WORKFLOW_ICON));
    template.setDescription(Manifest.string(Manifest.WORKFLOW_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class WorkflowWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, WorkflowWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDeploymentTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager
   ** Deployments and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager Deployments.
   */
  private ProjectTemplate createDeploymentTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_DEPLOYMENT));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.OIM_DEPLOYMENT);
    template.setName(Manifest.string(Manifest.OIM_DEPLOYMENT));
    template.setDescription(Manifest.string(Manifest.OIM_DEPLOYMENT_DESCRIPTION));
    template.setIconFile(Manifest.string(Manifest.OIM_DEPLOYMENT_ICON));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class DeploymentWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.OIM_GALLERY_RULE, DeploymentWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createFrontendTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager frontend
   ** customization and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager frontend customization.
   */
  private ProjectTemplate createFrontendTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_FRONTEND));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.OIM_FRONTEND);
    template.setName(Manifest.string(Manifest.OIM_FRONTEND));
    template.setDescription(Manifest.string(Manifest.OIM_FRONTEND_DESCRIPTION));
    template.setIconFile(Manifest.string(Manifest.OIM_FRONTEND_ICON));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class FrontendWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.ADF_GALLERY_RULE, FrontendWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createBackendTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager backend
   ** customization and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager backend customization.
   */
  private ProjectTemplate createBackendTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_BACKEND));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.OIM_BACKEND);
    template.setName(Manifest.string(Manifest.OIM_BACKEND));
    template.setDescription(Manifest.string(Manifest.OIM_BACKEND_DESCRIPTION));
    template.setIconFile(Manifest.string(Manifest.OIM_BACKEND_ICON));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class BackendWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.ADF_GALLERY_RULE, BackendWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAssemblyTemplate
  /**
   ** Creates the project template specific for Oracle Identity Manager library
   ** customization and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager library customization.
   */
  private ProjectTemplate createAssemblyTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.OIM_ASSEMBLY));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.OIM_ASSEMBLY);
    template.setName(Manifest.string(Manifest.OIM_ASSEMBLY));
    template.setDescription(Manifest.string(Manifest.OIM_ASSEMBLY_DESCRIPTION));
    template.setIconFile(Manifest.string(Manifest.OIM_ASSEMBLY_ICON));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class AssemblyWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.ADF_GALLERY_RULE, AssemblyWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplicationTemplate
  /**
   ** Creates the application template specific for Oracle Identity Manager
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager development.
   */
  private ApplicationTemplate createApplicationTemplate(final List<ProjectTemplate> project) {
    // create a proper technology scope for the application template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));

    // create the application template
    final ApplicationTemplate template = new ApplicationTemplate();
    template.setTemplateId(Manifest.APPLICATION);
    template.setName(Manifest.string(Manifest.APPLICATION));
    template.setIconFile(Manifest.string(Manifest.APPLICATION_ICON));
    template.setDescription(Manifest.string(Manifest.APPLICATION_DESCRIPTION));
    template.setProjectTemplates(project);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class ApplicationWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, Manifest.FEATURE_RULE, ApplicationWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCustomizationTemplate
  /**
   ** Creates the application template specific for Oracle Identity Manager
   ** customiations and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Identity Manager customiations.
   */
  private ApplicationTemplate createCustomizationTemplate(final List<ProjectTemplate> project) {
    // create a proper technology scope for the application template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oim.Manifest.TECHNOLOGY));

    // create the application template
    final ApplicationTemplate template = new ApplicationTemplate();
    template.setTemplateId(Manifest.CUSTOMIZATION);
    template.setName(Manifest.string(Manifest.CUSTOMIZATION));
    template.setIconFile(Manifest.string(Manifest.APPLICATION_ICON));
    template.setDescription(Manifest.string(Manifest.CUSTOMIZATION_DESCRIPTION));
    template.setProjectTemplates(project);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class ApplicationWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, Manifest.FEATURE_RULE, CustomizationWizard.class);
    return template;
  }
}