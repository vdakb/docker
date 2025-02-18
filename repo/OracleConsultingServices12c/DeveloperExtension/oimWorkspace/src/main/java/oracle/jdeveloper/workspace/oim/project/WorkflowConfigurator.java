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

    File        :   WorkflowConfigurator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    WorkflowConfigurator.


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
    11.1.1.3.37.60.27  2012-08-32  DSteding    ANT property configuration
                                               dropped for templates
                                               soa-context.xml and
                                               soa-deployment.xml.
    11.1.1.3.37.60.31  2012-04-18  DSteding    Adopted the changes required by
                                               Release 2 of Oracle Identity
                                               Manager.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.94  2021-03-09  DSteding    Support for Maven Project Object
                                               Model
*/

package oracle.jdeveloper.workspace.oim.project;

import java.io.File;

import oracle.javatools.data.HashStructure;

import oracle.ide.Ide;

import oracle.ide.net.URLPath;
import oracle.ide.net.URLFactory;

import oracle.ide.model.Project;

import oracle.ide.config.Preferences;

import oracle.ide.panels.TraversableContext;

import oracle.jdeveloper.ant.AntRunConfiguration;

import oracle.jdeveloper.workspace.iam.parser.XMLFileHandlerException;

import oracle.jdeveloper.workspace.iam.utility.ClassUtility;

import oracle.jdeveloper.workspace.iam.preference.Provider;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.iam.project.maven.ModelFolder;
import oracle.jdeveloper.workspace.iam.project.maven.ModelStream;

import oracle.jdeveloper.workspace.iam.project.template.TemplateBundle;
import oracle.jdeveloper.workspace.iam.project.template.TemplateStream;
import oracle.jdeveloper.workspace.iam.project.template.TemplateFolder;
import oracle.jdeveloper.workspace.iam.project.template.TemplateGenerator;

import oracle.jdeveloper.workspace.oim.Manifest;

import oracle.jdeveloper.workspace.oim.model.Workflow;
import oracle.jdeveloper.workspace.oim.model.SOADeployment;

import oracle.jdeveloper.workspace.oim.preference.Store;

import oracle.jdeveloper.workspace.oim.parser.WorkflowHandler;
import oracle.jdeveloper.workspace.oim.parser.CompositeHandler;

////////////////////////////////////////////////////////////////////////////////
// class WorkflowConfigurator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The configurator to setup Oracle Identity Manager Workflow projects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.94
 ** @since   11.1.1.3.37.56.13
 */
class WorkflowConfigurator extends TemplateProjectConfigurator {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WorkflowConfigurator</code> for the specified
   ** {@link Project}.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new WorkflowConfigurator()" and enforces use of the public factory method
   ** below.
   **
   ** @param  project            the {@link Project} this
   **                            {@link TemplateProjectConfigurator} belongs to.
   ** @param  context            the {@link TraversableContext} providing the
   **                            design time settings.
   */
  private WorkflowConfigurator(final Project project, final TraversableContext context) {
    // ensure inhertitance
    super(project, context);
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
    // all projects will use preference files to globalize common properties
    final TemplateFolder featureRoot = workspaceFolder.add(featureFolder(this.projectFolder(), 1));
    final TemplateStream preference  = featureRoot.add(WorkspaceConfigurator.templateSOAPreferencePreview(featureRoot, this.context));
    // we assume the preference template specifies the pattern
    // #{wks.preferences} to import the workspace preference file
    preference.include(oracle.jdeveloper.workspace.iam.Manifest.WKS_PREFERENCE, templateWorkspacePreview(workspaceFolder.add(featureFolder(this.projectFolder(), 2))));
    // a development project use always the predefined context build file to
    // globalize build targets
    final TemplateStream context    = featureRoot.add(WorkspaceConfigurator.templateSOAContextPreview(featureRoot, this.context));
    // include the technology related preview
    context.include(oracle.jdeveloper.workspace.iam.Manifest.SCP_SERVER, featureRoot.add(WorkspaceConfigurator.templateSCPServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.JEE_SERVER, featureRoot.add(WorkspaceConfigurator.templateJEEServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.OIM_SERVER, featureRoot.add(WorkspaceConfigurator.templateOIMServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.SOA_SERVER, featureRoot.add(WorkspaceConfigurator.templateSOAServerPreview(featureRoot, this.context)));
    context.include(oracle.jdeveloper.workspace.iam.Manifest.MDS_SERVER, featureRoot.add(WorkspaceConfigurator.templateMDSServerPreview(featureRoot, this.context)));
    // include the technology related preferences
    context.include(Manifest.SOA_PREFERENCE, preference);

    // a development project use always the predefined target build file to
    // globalize build targets
    final TemplateStream target = featureRoot.add(WorkspaceConfigurator.templateSOATargetPreview(featureRoot, this.context));
    // we assume the template specifies the pattern #{soa.preferences} to
    // import the preferences file
    target.include(Manifest.SOA_PREFERENCE, preference);

    final TemplateFolder project = featureRoot.add(this.projectFolder()) ;
    templateProjectPreview(project, target);
    // add the SOA Composite artifacts to the folder
    templateCompositePreview(project);
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
    workspaceFolder.add(featureFolder(this.projectFolder(), 1));
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
    final Workflow provider = Workflow.instance(this.context);
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
    final Workflow provider = Workflow.instance(this.context);
    return provider.file().replace("ant", "pom");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure (TemplateObjectConfigurator)
  /**
   ** Configures a Oracle JDeveloper {@link Project} completely.
   */
  @Override
  public final void configure() {
    configureSourceDirectory();
    configureOutputDirectory();
    configureCompiler();
    configureClassPath();
    configureBuildfile();
    configureComposite();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureClassPath (TemplateProjectConfigurator)
  /**
   ** Configures the libraries the project needs.
   */
  @Override
  protected void configureClassPath() {
    configureRuntime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureFeature (TemplateObjectConfigurator)
  /**
   ** Creates the product related feature preferences ANT build file for the
   ** project.
   **
   ** @param  context            the {@link TraversableContext} where the data
   **                            are stored as design time objects.
   */
  @Override
  public final void configureFeature(final TraversableContext context) {
    final TemplateFolder root = new TemplateFolder(featureFolder(projectFolder(), 1));
    templateFeaturePreview(root);
    // generate the ANT property files for workflow project configuration
    final TemplateGenerator builder = new TemplateGenerator(root);
    builder.startProgress();
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
   **                            configure Oracle Identity Manager Wrokflow
   **                            {@link Project}s.
   */
  protected static TemplateProjectConfigurator instance(final Project project, final TraversableContext context) {
    return new WorkflowConfigurator(project, context);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateDeploymentPreview
  /**
   ** Creates the ANT deployment declaration build file for Oracle Identity
   ** Manager projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  protected TemplateStream templateDeploymentPreview(final TemplateFolder folder) {
    final SOADeployment  context = SOADeployment.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    item.hotspotSelected(!item.exists());
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateProjectPreview
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
  protected void templateProjectPreview(final TemplateFolder folder, final TemplateStream target) {
    final TemplateStream workflow = folder.add( templateWorkflowPreview(folder));
    workflow.include(Manifest.SOA_TARGET, target);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureComposite
  /**
   ** Configures the <code>Composite</code> properties of the project
   */
  private void configureComposite() {
    final Workflow instance  = Workflow.instance(this.context);
    final String   namespace = "http://xmlns.oracle.com/"+ instance.applicationNamespace() + "/" + instance.name() + "/ApprovalProcess";
    // configure the values we found in the template but didn't know if they are
    // required or not
    final HashStructure properties = this.project().getProperties();
    if (!properties.containsKey("Namespace"))
      properties.putString("Namespace", namespace);

    if (!properties.containsKey("ProcessName"))
      properties.putString("ProcessName", CompositeHandler.PROCESS);

    if (!properties.containsKey("Template"))
      properties.putString("Template", "BPEL_ASYNC_PROCESS");

    if (!properties.containsKey("ApprovalProcess_bpelFileURL"))
      properties.putURL("ApprovalProcess_bpelFileURL", URLFactory.newFileURL(this.project().getBaseDirectory() + "/" + CompositeHandler.PROCESS + "." + CompositeHandler.BPEL));

    if (!properties.containsKey("ApprovalProcess_Namespace"))
      properties.putString("ApprovalProcess_Namespace", namespace);

    if (!properties.containsKey("ApprovalProcess_ProcessName"))
      properties.putString("ApprovalProcess_ProcessName", CompositeHandler.PROCESS);

    if (!properties.containsKey("ApprovalProcess_Template"))
      properties.putString("ApprovalProcess_Template", "BPEL_ASYNC_PROCESS");

    if (!properties.containsKey("ApprovalProcess_wsdl"))
      properties.putString("ApprovalProcess_wsdl", CompositeHandler.PROCESS + "." + CompositeHandler.WSDL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateWorkflowPreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
   ** Workflow projects.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream templateWorkflowPreview(final TemplateFolder folder) {
    final Workflow       context = Workflow.instance(this.context);
    final TemplateStream item    = new TemplateStream(folder, context.file());
    // configure the template to use
    item.template(ClassUtility.classNameToFile(context.template(), ClassUtility.XML));
    // configure the substitution parameter
    item.add(WorkflowHandler.ANT_PROJECT,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.project());
    item.add(WorkflowHandler.ANT_DEFAULT,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_ANT_ICON), context.target());
    item.add(WorkflowHandler.DESCRIPTION,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.description());
    item.add(WorkflowHandler.DESTINATION,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), item.relativeFile(context.destination()));
    item.add(WorkflowHandler.PACKAGEPATH,    TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.packagePath());
    item.add(WorkflowHandler.PARTITION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.partition());
    item.add(WorkflowHandler.WORKFLOW,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.name());
    item.add(WorkflowHandler.REVISION,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.revision());
    item.add(WorkflowHandler.SERVICE,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.service());
    item.add(WorkflowHandler.CATEGORY,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.category());
    item.add(WorkflowHandler.PROVIDER,       TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.provider());
    item.add(WorkflowHandler.OPERATION,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.operation());
    item.add(WorkflowHandler.PAYLOAD,        TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.payload());
    item.add(WorkflowHandler.PLAN,           TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), context.plan());
    item.add(WorkflowHandler.PLAN_OVERWRITE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_XML_ICON), Boolean.toString(context.planOverwrite()));
    // check if the item already exists to override the default values created
    // above with the values found in the file
    item.hotspotSelected(!item.exists());
    if (item.exists()) {
      WorkflowHandler handler = new WorkflowHandler(URLFactory.newFileURL(item.file()));
      try {
        handler.load();
        item.property(WorkflowHandler.ANT_PROJECT,    handler.name());
        item.property(WorkflowHandler.ANT_DEFAULT,    handler.target());
        item.property(WorkflowHandler.DESCRIPTION,    handler.propertyValue(WorkflowHandler.DESCRIPTION));
        item.property(WorkflowHandler.DESTINATION,    item.relativeFile(handler.propertyValue(WorkflowHandler.DESTINATION)));
        item.property(WorkflowHandler.PACKAGEPATH,    handler.propertyValue(WorkflowHandler.PACKAGEPATH));
        item.property(WorkflowHandler.PARTITION,      handler.propertyValue(WorkflowHandler.PARTITION));
        item.property(WorkflowHandler.WORKFLOW,       handler.propertyValue(WorkflowHandler.WORKFLOW));
        item.property(WorkflowHandler.REVISION,       handler.propertyValue(WorkflowHandler.REVISION));
        item.property(WorkflowHandler.SERVICE,        handler.propertyValue(WorkflowHandler.SERVICE));
        item.property(WorkflowHandler.CATEGORY,       handler.propertyValue(WorkflowHandler.CATEGORY));
        item.property(WorkflowHandler.PROVIDER,       handler.propertyValue(WorkflowHandler.PROVIDER));
        item.property(WorkflowHandler.OPERATION,      handler.propertyValue(WorkflowHandler.OPERATION));
        item.property(WorkflowHandler.PAYLOAD,        handler.propertyValue(WorkflowHandler.PAYLOAD));
        item.property(WorkflowHandler.PLAN,           handler.propertyValue(WorkflowHandler.PLAN));
        item.property(WorkflowHandler.PLAN_OVERWRITE, handler.propertyValue(WorkflowHandler.PLAN_OVERWRITE));
      }
      catch (XMLFileHandlerException e) {
        e.printStackTrace();
      }
    }
    return item;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   templateCompositePreview
  /**
   ** Creates the SOA Composite related files for the project.
   **
   ** @param  composite          the {@link TemplateFolder} where the files will
   **                            be created within.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private void templateCompositePreview (final TemplateFolder composite) {
    // create the folder for the schema definitions
    TemplateFolder folder = composite.add(new File(composite.folder(), ClassUtility.XSD));
    // add the XSD artifacts to the folder created above
    schemaPreview(folder, CompositeHandler.PAYLOAD);
    schemaPreview(folder, CompositeHandler.PROCESS);
    schemaPreview(folder, CompositeHandler.RULE);
    schemaPreview(folder, CompositeHandler.TASK);
    schemaPreview(folder, CompositeHandler.DECISION);
    schemaPreview(folder, CompositeHandler.DOCUMENT);
    schemaPreview(folder, CompositeHandler.ELEMENT);
    schemaPreview(folder, CompositeHandler.REQUEST);
    schemaPreview(folder, CompositeHandler.ROUTING);
    schemaPreview(folder, CompositeHandler.MACHINE);
    schemaPreview(folder, CompositeHandler.WSADDR);

    // create the folder hierarchy for the rule definitions
    folder = composite.add(new File(composite.folder(), "oracle"));
    folder = composite.add(new File(folder.folder(),    "rules"));
    // add the rule artifacts to the folder hierarchy created above
    configPreview(folder,    CompositeHandler.RULE,      "rules", Manifest.WORKFLOW_RULES, "rules");
    configPreview(folder,    CompositeHandler.RULE_BASE, "rules", Manifest.WORKFLOW_RULES, "rules");

    // add the SOA artifacts to the SOA Content folder passed in
    configPreview(composite, CompositeHandler.CALLBACK,  CompositeHandler.WSDL, Manifest.WORKFLOW_PACKAGE, CompositeHandler.WSDL);
    configPreview(composite, CompositeHandler.RULE,      CompositeHandler.DECS, Manifest.WORKFLOW_PACKAGE, CompositeHandler.DECS);
    configPreview(composite, CompositeHandler.RULE,      CompositeHandler.TYPE, Manifest.WORKFLOW_PACKAGE, CompositeHandler.TYPE);
    configPreview(composite, CompositeHandler.RULE,      CompositeHandler.WSDL, Manifest.WORKFLOW_PACKAGE, CompositeHandler.WSDL);
    configPreview(composite, CompositeHandler.TASK,      CompositeHandler.TYPE, Manifest.WORKFLOW_PACKAGE, CompositeHandler.TYPE);
    configPreview(composite, CompositeHandler.TASK,      "task",                Manifest.WORKFLOW_PACKAGE, "task");
    configPreview(composite, CompositeHandler.PROCESS,   CompositeHandler.WSDL, Manifest.WORKFLOW_PACKAGE, CompositeHandler.WSDL);
    configPreview(composite, CompositeHandler.PROCESS,   CompositeHandler.TYPE, Manifest.WORKFLOW_PACKAGE, CompositeHandler.TYPE);
    configPreview(composite, CompositeHandler.PROCESS,   CompositeHandler.BPEL, Manifest.WORKFLOW_PACKAGE, CompositeHandler.BPEL);
    configPreview(composite, CompositeHandler.COMPOSITE, ClassUtility.XML,      Manifest.WORKFLOW_PACKAGE, "lmx");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configureRuntime
  /**
   ** Configures the libraries the project needs.
   */
  protected void configureRuntime() {
    // configure the development class path
    // configure the ANT class path needed for deployments
    final Provider preference = Provider.instance(Preferences.getPreferences());
    final Store    provider = Store.instance(Preferences.getPreferences());
    final File     fmwHome    = featureFolder(new File(Ide.getOracleHomeDirectory()), 1);
    final File     jrfModule  = new File(fmwHome, "oracle_common/modules");
    final File     soaModule  = new File(Ide.getOracleHomeDirectory(), "soa/modules");
    final File     oimHome    = toFolder(provider.console());
    final File     hstHome    = toFolder(preference.headstart());
    final File     ocsHome    = toFolder(provider.foundation());
    // setup the ANT classpath used by the deployment utilities
    final URLPath  classpath  = new URLPath();
    classpath.add(pathEntry(oimHome,   "ext/commons-logging.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.odl_11.1.1/ojdl.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.adf.share_11.1.1/adflogginghandler.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.dms_11.1.1/dms.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.ucp_11.1.0.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.javacache_11.1.1/cache.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.adf.share.ca_11.1.1/adf-share-base.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.xdk_11.1.0/xml.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.xdk_11.1.0/xmlparserv2.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.xmlef_11.1.1/xmlef.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.bali.share_11.1.1/share.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.mds_11.1.1/mdsrt.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.mds_11.1.1/oramds.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.jdbc_11.1.1/ojdbc6dms.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.fabriccommon_11.1.1/fabric-common.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.http_client_11.1.1.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.webservices_11.1.1/wsclient-rt.jar"));
    classpath.add(pathEntry(jrfModule, "oracle.fabriccommon_11.1.1/fabric-common.jar"));
    classpath.add(pathEntry(soaModule, "commons-cli-1.1.jar"));
    classpath.add(pathEntry(soaModule, "oracle.soa.fabric_11.1.1/fabric-runtime.jar"));
    classpath.add(pathEntry(soaModule, "oracle.soa.fabric_11.1.1/soa-infra-tools.jar"));
    classpath.add(pathEntry(soaModule, "oracle.soa.mgmt_11.1.1/soa-infra-mgmt.jar"));
    classpath.add(pathEntry(soaModule, "oracle.soa.wrokflow_11.1.1/bpm_services.jar"));
    classpath.add(pathEntry(ocsHome,   "CodeBasePlatform/" + provider.release() + "/lib/iam-platform-pluginframework.jar"));
    classpath.add(pathEntry(ocsHome,   "CodeBasePlatform/" + provider.release() + "/lib/iam-platform-workflowservice.jar"));
    classpath.add(pathEntry(oimHome,   "ext/wlfullclient.jar"));
    classpath.add(pathEntry(oimHome,   "lib/jrf-api.jar"));
    classpath.add(pathEntry(oimHome,   "lib/oimclient.jar"));
    classpath.add(pathEntry(oimHome,   "ext/spring.jar"));
    classpath.add(pathEntry(oimHome,   "lib/xlAPI.jar"));
    classpath.add(pathEntry(oimHome,   "lib/xlDDM.jar"));
    classpath.add(pathEntry(hstHome,   "hstFoundation/lib/hst-foundation.jar"));
    classpath.add(pathEntry(hstHome,   "hstFoundation/lib/hst-deployment.jar"));
    classpath.add(pathEntry(ocsHome,   "oimDeployment/lib/oim-deploy.jar"));

    final AntRunConfiguration configuration = AntRunConfiguration.getInstance(this.project());
    configuration.setClassPath(classpath);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schemaPreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
   ** Workflow schema definitions.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  schemaFile         the name of the file to create.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream schemaPreview(final TemplateFolder folder, final String schemaFile) {
    return configPreview(folder, schemaFile, ClassUtility.XSD, Manifest.WORKFLOW_SCHEMA, ClassUtility.XSD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configPreview
  /**
   ** Creates the ANT preferences build file for Oracle Identity Manager
   ** Workflow schema definitions.
   **
   ** @param  folder             the {@link TemplateFolder} where the file will
   **                            be created within.
   ** @param  configFile         the name of the file to create.
   ** @param  configType         the extension of the file to create.
   ** @param  templatePackage    the name of the package the template to
   **                            generate the content of the file specified by
   **                            <code>configFile</code> exists.
   ** @param  templateType       the extension of the template.
   **
   ** @return                    the created {@link TemplateStream} ready for
   **                            preview.
   */
  private TemplateStream configPreview(final TemplateFolder folder, final String configFile, final String configType, final String templatePackage, final String templateType) {
    final Workflow       config    = Workflow.instance(this.context);
    final TemplateStream item      = folder.add(new TemplateStream(folder, configFile, configType));
    // configure the template to use
    item.template(ClassUtility.classNameToFile(templatePackage + "." + configFile, templateType));
     // configure the substitution parameter
    item.add(CompositeHandler.WORKSPACE, TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.applicationNamespace());
    item.add(CompositeHandler.SERVICE,   TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.service());
    item.add(CompositeHandler.WORKFLOW,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.name());
    item.add(CompositeHandler.REVISION,  TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.revision());
    item.add(CompositeHandler.LABEL,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.label());
    item.add(CompositeHandler.MODE,      TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.mode());
    item.add(CompositeHandler.STATE,     TemplateBundle.icon(TemplateBundle.NODE_PROPERTY_PROCESS_ICON), config.state());
    // check if the item already exists to avoid overriding an existing file
    item.hotspotSelected(!item.exists());
    return item;
  }
}