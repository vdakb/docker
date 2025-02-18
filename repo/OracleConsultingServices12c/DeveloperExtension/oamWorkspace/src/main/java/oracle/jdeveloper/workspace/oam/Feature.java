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
    11.1.1.3.37.60.72  2017-11-29  DSteding    Deployment templates referencing
                                               formerly defined namespace
                                               http://oracle.hst.deployment/weblogic
                                               now as
                                               http://oracle.hst.deployment/topology.
    11.1.1.3.37.60.73  2017-12-15  DSteding    Add Access Manager libraries to
                                               control registrations of various
                                               components.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.oam;

import java.util.List;
import java.util.ArrayList;

import oracle.ide.model.TechId;
import oracle.ide.model.TechnologyScope;
import oracle.ide.model.TechnologyRegistry;

import oracle.jdeveloper.template.ProjectTemplate;
import oracle.jdeveloper.template.ApplicationTemplate;

import oracle.jdeveloper.workspace.iam.AbstractAddin;

import oracle.jdeveloper.workspace.oam.gallery.AgentWizard;
import oracle.jdeveloper.workspace.oam.gallery.PluginWizard;
import oracle.jdeveloper.workspace.oam.gallery.LibraryWizard;
import oracle.jdeveloper.workspace.oam.gallery.CollectorWizard;
import oracle.jdeveloper.workspace.oam.gallery.DeploymentWizard;
import oracle.jdeveloper.workspace.oam.gallery.ApplicationWizard;

////////////////////////////////////////////////////////////////////////////////
// class Feature
// ~~~~~ ~~~~~~~
/**
 ** Addin for application and project functionality within Oracle JDeveloper to
 **handle Access Manager artifacts.
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
public class Feature extends AbstractAddin {

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
   ** Invoked by the super class after the instance of the {@link AbstractAddin}
   ** is instantiated by the <code>AddinManager</code>.
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
   ** technology related to Oracle Access Manager.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager.
   */
  public static TechId featureTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.TECHNOLOGY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   libraryTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Access Manager Libraries.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager Libraries.
   */
  public static TechId libraryTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.LIBRARY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   agentTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Access Manager Agents.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager Agents.
   */
  public static TechId agentTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.AGENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pluginTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Access Manager Authentication Plug-In's.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager Authentication Plug-In's.
   */
  public static TechId pluginTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.PLUGIN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   credentialCollectorTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Access Manager Credential Collectors.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager Credential Collectors.
   */
  public static TechId credentialCollectorTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.COLLECTOR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deploymentTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to Oracle Access Manager Deploymants.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to Oracle
   **                            Access Manager Deploymants.
   */
  public static TechId deploymentTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OAM_DEPLOYMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oamServerTechnology
  /**
   ** Returns the {@link TechId} registered in Oracle JDeveloper as the
   ** technology related to a OAM Server.
   **
   ** @return                    the {@link TechId} registered in Oracle
   **                            JDeveloper as the technology related to a OAM
   **                            Server.
   */
  public static TechId oamServerTechnology() {
    return TechnologyRegistry.getInstance().getTechId(Manifest.OAM_SERVER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createTemplate
  /**
   ** Creates the project and application templates specific for Oracle Access
   ** Manager and registers the gallery items for each of them.
   */
  private void createTemplate() {
    // create the library project template and enlist it in the collection
    createLibraryTemplate();
    // create the agent project template and enlist it in the collection
    createAgentTemplate();
    // create the plugin project template and enlist it in the collection
    createPluginTemplate();
    // create the credential collector project template and enlist it in the collection
    createCollectorTemplate();
    // create the deployment project template and enlist it in the collection
    createDeploymentTemplate();

    // create the collection that will contain the project templates associated
    // with the application template
    final List<ProjectTemplate> template = new ArrayList<ProjectTemplate>();
    // register the application template
    createApplicationTemplate(template);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createLibraryTemplate
  /**
   ** Creates the project template specific for Oracle Access Manager Library
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager Library development.
   */
  private ProjectTemplate createLibraryTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.LIBRARY));

    // create the project template
    final ProjectTemplate template = new ProjectTemplate();
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
    // the specific class LibraryWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, LibraryWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAgentTemplate
  /**
   ** Creates the project template specific for Oracle Access Manager Agent
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager Agent development.
   */
  private ProjectTemplate createAgentTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.AGENT));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.AGENT);
    template.setName(Manifest.string(Manifest.AGENT));
    template.setIconFile(Manifest.string(Manifest.AGENT_ICON));
    template.setDescription(Manifest.string(Manifest.AGENT_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class AgentWizard will guide the user through the creation
    // of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, AgentWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createPluginTemplate
  /**
   ** Creates the project template specific for Oracle Access Manager
   ** Authentication Plug-In development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager Authentication Plug-In
   **                            development.
   */
  private ProjectTemplate createPluginTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.OAM_DEPLOYMENT));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.PLUGIN));

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
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, PluginWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCollectorTemplate
  /**
   ** Creates the project template specific for Oracle Access Manager
   ** Credential Collector development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager Credential Collector
   **                            development.
   */
  private ProjectTemplate createCollectorTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.COLLECTOR));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.COLLECTOR);
    template.setName(Manifest.string(Manifest.COLLECTOR));
    template.setIconFile(Manifest.string(Manifest.COLLECTOR_ICON));
    template.setDescription(Manifest.string(Manifest.COLLECTOR_DESCRIPTION));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class CollectorWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, CollectorWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createDeploymentTemplate
  /**
   ** Creates the project template specific for Oracle Access Manager
   ** Deployments and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager Deployments.
   */
  private ProjectTemplate createDeploymentTemplate() {
    // create a proper technology scope for the project template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.OAM_DEPLOYMENT));

    // create the project template
    final ProjectTemplate  template = new ProjectTemplate();
    template.setTemplateId(Manifest.OAM_DEPLOYMENT);
    template.setName(Manifest.string(Manifest.OAM_DEPLOYMENT));
    template.setDescription(Manifest.string(Manifest.OAM_DEPLOYMENT_DESCRIPTION));
    template.setIconFile(Manifest.string(Manifest.OAM_DEPLOYMENT_ICON));
    template.setTechnologyScope(scope);

    // create a generic gallery elements
    // the elment will be displayed in the folders represented by setPath
    // the folder itself is defined in the extension manifest as a gallery item
    // and the label that is resolved from the extension manifest resource
    // bundle
    // the specific class DeploymentWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, new String[] {oracle.jdeveloper.workspace.iam.Manifest.FEATURE, Manifest.FEATURE}, Manifest.GALLERY_RULE, DeploymentWizard.class);
    return template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createApplicationTemplate
  /**
   ** Creates the application template specific for Oracle Access Manager
   ** development and registers a gallery item for it.
   **
   ** @return                    the created project template specific for
   **                            Oracle Access Manager development.
   */
  private ApplicationTemplate createApplicationTemplate(final List<ProjectTemplate> project) {
    // create a proper technology scope for the application template to create
    final TechnologyRegistry registry = TechnologyRegistry.getInstance();
    final TechnologyScope    scope    = new TechnologyScope();
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.iam.Manifest.TECHNOLOGY));
    scope.add(registry.getTechId(oracle.jdeveloper.workspace.oam.Manifest.TECHNOLOGY));

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
    // the specific class ApplicationWizard will guide the user through the
    // creation of the artifacts.
    registerTemplate(template, Manifest.FEATURE_RULE, ApplicationWizard.class);
    return template;
  }
}