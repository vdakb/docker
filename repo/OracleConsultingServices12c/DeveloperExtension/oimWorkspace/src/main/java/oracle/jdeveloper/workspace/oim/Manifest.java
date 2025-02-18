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

    File        :   Manifest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Manifest.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.34  2012-02-06  DSteding    Extended support for R2
                                               deployments
    11.1.1.3.37.60.39  2013-07-27  DSteding    Extended support for UI
                                               customization deployments
    11.1.1.3.37.60.43  2013-10-23  DSteding    Extended support for ANT
                                               runtime classpath configuration
    11.1.1.3.37.60.66  2015-09-12  DSteding    Extended support for Library
                                               projects
    11.1.1.3.37.60.69  2015-12-27  DSteding    Removed Oracle from the product
                                               names and libraries because its
                                               clear where we are.
    11.1.1.3.37.60.72  2017-11-29  DSteding    Deployment templates referencing
                                               formerly defined namespace
                                               http://oracle.hst.deployment/weblogic
                                               now as
                                               http://oracle.hst.deployment/topology.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.84  2019-06-20  DSteding    Connection properties added
*/

package oracle.jdeveloper.workspace.oim;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

import oracle.jdeveloper.workspace.iam.AbstractAddin;

import oracle.jdeveloper.workspace.iam.utility.ListResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class Manifest
// ~~~~~ ~~~~~~~~
/**
 ** This declares the translated objects by resource keys for message output.
 ** <br>
 ** Used for translation resources to
 ** <ul>
 **   <li>country code  common
 **   <li>language code common
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.84
 ** @since   11.1.1.3.37.56.13
 */
public class Manifest extends ListResourceBundle {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The FEATURE key should be a hard-coded String to guarantee that its
   ** value stays constant across releases.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for
   ** FEATURE.
   ** <b>Note to myself</b>:
   ** <br>
   ** Ensure always the key defined here is the same that we use in the
   ** extension manifest as the key of the folder.
   */
  public static final String FEATURE                        = "oim";

  public static final String FEATURE_RULE                   = "oim-extension-state";
  public static final String OIM_GALLERY_RULE               = "oim-workspace";
  public static final String ADF_GALLERY_RULE               = "adf-workspace";

  /**
   ** The key KEY should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   ** <br>
   ** Specifically, do NOT use CoolFeaturePrefs.class.getName(). The reason is
   ** that if CoolFeaturePrefs is ever renamed or moved,
   ** CoolFeaturePrefs.class.getName() will cause the KEY String to change,
   ** which introduces a preferences migration issue (since this key is used in
   ** the persisted XML) that will require more code and testing to accomodate
   ** and open up your code to annoying little bugs.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for key
   ** KEY.
   ** <p>
   ** By convention, KEY should be the fully qualified class name of the
   ** <code>HashStructureAdapter</code>. This helps ensure against name
   ** collisions. This also makes it easier to identify what piece of code is
   ** responsible for a preference when you're looking at the XML in the
   ** product-preferences.xml file. Of course, that only works as long as the
   ** adapter class itself is never renamed or moved, so avoid renaming or
   ** moving this class once it's been released in production.
   */
  public static final String KEY                            = AbstractAddin.DATA_KEY + "/" + FEATURE;

  /* the translatable strings */
  public static final String NAME                           = FEATURE + ".name";
  public static final String VENDOR                         = FEATURE + ".vendor";
  public static final String LICENSE                        = FEATURE + ".license";
  public static final String COPYRIGHT                      = FEATURE + ".copyright";
  public static final String DESCRIPTION                    = FEATURE + ".description";

  /**
   ** The TECHNOLOGY should be a hard-coded String to guarantee that its value
   ** stays constant across releases.
   ** <br>
   ** Specifically, do NOT use CoolFeaturePrefs.class.getName(). The reason is
   ** that if CoolFeaturePrefs is ever renamed or moved,
   ** CoolFeaturePrefs.class.getName() will cause the KEY String to change,
   ** which introduces a preferences migration issue (since this key is used in
   ** the persisted XML) that will require more code and testing to accomodate
   ** and open up your code to annoying little bugs.
   ** <br>
   ** Always eliminate this cause of bugs by using a hard-coded String for
   ** TECHNOLOGY.
   */
  public static final String TECHNOLOGY                     = FEATURE                + ".technology";
  public static final String PROJECT                        = FEATURE                + ".project";
  public static final String PROJECT_CONFIG                 = PROJECT                + ".configurator";
  public static final String PROJECT_CONFIG_ICON            = PROJECT_CONFIG         + ".icon";
  public static final String PROJECT_CONFIG_DESCRIPTION     = PROJECT_CONFIG         + ".description";
  public static final String PROJECT_RUNTIME                = PROJECT                + ".runtime";
  public static final String PROJECT_RUNTIME_ICON           = PROJECT_RUNTIME        + ".icon";
  public static final String PROJECT_RUNTIME_DESCRIPTION    = PROJECT_RUNTIME        + ".description";
  public static final String MAVEN                          = FEATURE                + ".maven";
  public static final String MAVEN_BUILD                    = MAVEN                  + ".build";
  public static final String MAVEN_BUILD_ICON               = MAVEN_BUILD            + ".icon";
  public static final String MAVEN_BUILD_DESCRIPTION        = MAVEN_BUILD            + ".description";

  public static final String APPLICATION                    = FEATURE                + ".application";
  public static final String APPLICATION_ICON               = APPLICATION            + ".icon";
  public static final String APPLICATION_DESCRIPTION        = APPLICATION            + ".description";
  public static final String CUSTOMIZATION                  = FEATURE                + ".customization";
  public static final String CUSTOMIZATION_ICON             = CUSTOMIZATION          + ".icon";
  public static final String CUSTOMIZATION_DESCRIPTION      = CUSTOMIZATION          + ".description";

  /** identifier where all templates located */
  public static final String BUILDFILE_PACKAGE              = "template.oim";
  public static final String BUILDFILE_CONFIG               = BUILDFILE_PACKAGE      + ".config";
  public static final String BUILDFILE_SOURCE               = BUILDFILE_PACKAGE      + ".source";
  public static final String WORKFLOW_PACKAGE               = BUILDFILE_PACKAGE      + ".workflow";
  public static final String WORKFLOW_SCHEMA                = WORKFLOW_PACKAGE       + ".schema";
  public static final String WORKFLOW_RULES                 = WORKFLOW_PACKAGE       + ".rules";
  public static final String WORKFLOW_CONFIG                = WORKFLOW_PACKAGE       + ".config";

  /** identifier to maintain the OIM Server properties */
  public static final String OIM_SERVER                     = FEATURE                + ".server";
  public static final String OIM_SERVER_PROJECT             = OIM_SERVER             + ".project";
  public static final String OIM_SERVER_DESCRIPTION         = OIM_SERVER             + ".description";
  /** identifier to maintain the SOA Server properties */
  public static final String SOA_SERVER                     = "soa.server";
  public static final String SOA_SERVER_PROJECT             = SOA_SERVER             + ".project";
  public static final String SOA_SERVER_DESCRIPTION         = SOA_SERVER             + ".description";
  /** identifier to maintain the OIM build preferences */
  public static final String OIM_PREFERENCE                 = FEATURE                + ".preferences";
  public static final String OIM_PREFERENCE_PROJECT         = OIM_PREFERENCE         + ".project";
  public static final String OIM_PREFERENCE_DESCRIPTION     = OIM_PREFERENCE         + ".description";
  /** identifier to maintain the OIM ANT context extension */
  public static final String OIM_CONTEXT                    = FEATURE                + ".context";
  public static final String OIM_CONTEXT_PROJECT            = OIM_CONTEXT            + ".project";
  public static final String OIM_CONTEXT_DESCRIPTION        = OIM_CONTEXT            + ".description";
  /** identifier to maintain the OIM ANT deployment extension */
  public static final String OIM_DEPLOYMENT                 = FEATURE                + ".deployment";
  public static final String OIM_DEPLOYMENT_ICON            = OIM_DEPLOYMENT         + ".icon";
  public static final String OIM_DEPLOYMENT_PROJECT         = OIM_DEPLOYMENT         + ".project";
  public static final String OIM_DEPLOYMENT_DESCRIPTION     = OIM_DEPLOYMENT         + ".description";
  /** identifier to maintain the OIM ANT frontend extension */
  public static final String OIM_FRONTEND                   = FEATURE                + ".frontend";
  public static final String OIM_FRONTEND_ICON              = OIM_FRONTEND           + ".icon";
  public static final String OIM_FRONTEND_PROJECT           = OIM_FRONTEND           + ".project";
  public static final String OIM_FRONTEND_DESCRIPTION       = OIM_FRONTEND           + ".description";
  public static final String OIM_FRONTEND_PATH              = OIM_FRONTEND           + ".path";
  /** identifier to maintain the OIM ANT model extension */
  public static final String OIM_BACKEND                    = FEATURE                + ".backend";
  public static final String OIM_BACKEND_ICON               = OIM_BACKEND            + ".icon";
  public static final String OIM_BACKEND_PROJECT            = OIM_BACKEND            + ".project";
  public static final String OIM_BACKEND_DESCRIPTION        = OIM_BACKEND            + ".description";
  public static final String OIM_BACKEND_PATH               = OIM_BACKEND            + ".path";
  /** identifier to maintain the OIM ANT model extension */
  public static final String OIM_ASSEMBLY                   = FEATURE                + ".assembly";
  public static final String OIM_ASSEMBLY_ICON              = OIM_ASSEMBLY           + ".icon";
  public static final String OIM_ASSEMBLY_PROJECT           = OIM_ASSEMBLY           + ".project";
  public static final String OIM_ASSEMBLY_DESCRIPTION       = OIM_ASSEMBLY           + ".description";
  /** identifier to maintain the OIM ANT build targets properties */
  public static final String OIM_TARGET                     = FEATURE                + ".target";
  public static final String OIM_TARGET_PROJECT             = OIM_TARGET             + ".project";
  public static final String OIM_TARGET_DESCRIPTION         = OIM_TARGET             + ".description";
  /** identifier to maintain the OIM ANT build targets properties */
  public static final String ADF_TARGET                     = "adf.target";
  public static final String ADF_MANIFEST                   = "adf.manifest";
  public static final String ADF_CONTENT                    = "adf.content";
  public static final String ADF_TARGET_PROJECT             = ADF_TARGET             + ".project";
  public static final String ADF_TARGET_DESCRIPTION         = ADF_TARGET             + ".description";
  /** identifier to maintain the SOA build preferences */
  public static final String SOA_PREFERENCE                 = "soa.preferences";
  public static final String SOA_PREFERENCE_PROJECT         = SOA_PREFERENCE         + ".project";
  public static final String SOA_PREFERENCE_DESCRIPTION     = SOA_PREFERENCE         + ".description";
  /** identifier to maintain the SCA ANT deployment extension */
  public static final String SOA_CONTEXT                    = "soa.context";
  public static final String SOA_CONTEXT_PROJECT            = SOA_CONTEXT            + ".project";
  public static final String SOA_CONTEXT_DESCRIPTION        = SOA_CONTEXT            + ".description";
  /** identifier to maintain the SOA ANT deployment targets properties */
  public static final String SOA_DEPLOYMENT                 = "soa.deployment";
  public static final String SOA_DEPLOYMENT_PROJECT         = SOA_DEPLOYMENT         + ".project";
  /** identifier to maintain the SOA ANT build targets properties */
  public static final String SOA_TARGET                     = "soa.target";
  public static final String SOA_TARGET_PROJECT             = SOA_TARGET             + ".project";
  public static final String SOA_TARGET_DESCRIPTION         = SOA_TARGET             + ".description";
  /** identifier to maintain the OIM ANT adapter extension */
  public static final String ADAPTER                        = FEATURE                + ".adapter";
  public static final String ADAPTER_ICON                   = ADAPTER                + ".icon";
  public static final String ADAPTER_PROJECT                = ADAPTER                + ".project";
  public static final String ADAPTER_DESCRIPTION            = ADAPTER                + ".description";
  /** identifier to maintain the OIM ANT scheduler extension */
  public static final String SCHEDULER                      = FEATURE                + ".scheduler";
  public static final String SCHEDULER_ICON                 = SCHEDULER              + ".icon";
  public static final String SCHEDULER_PROJECT              = SCHEDULER              + ".project";
  public static final String SCHEDULER_DESCRIPTION          = SCHEDULER              + ".description";
  /** identifier to maintain the OIM ANT library extension */
  public static final String LIBRARY                        = FEATURE                + ".library";
  public static final String LIBRARY_ICON                   = LIBRARY                + ".icon";
  public static final String LIBRARY_PROJECT                = LIBRARY                + ".project";
  public static final String LIBRARY_DESCRIPTION            = LIBRARY                + ".description";
  /** identifier to maintain the OIM ANT adapter extension */
  public static final String PLUGIN                         = FEATURE                + ".plugin";
  public static final String PLUGIN_ICON                    = PLUGIN                 + ".icon";
  public static final String PLUGIN_PROJECT                 = PLUGIN                 + ".project";
  public static final String PLUGIN_DESCRIPTION             = PLUGIN                 + ".description";
  /** identifier to maintain the OIM ANT workflow extension */
  public static final String WORKFLOW                       = FEATURE                + ".workflow";
  public static final String WORKFLOW_ICON                  = WORKFLOW               + ".icon";
  public static final String WORKFLOW_PROJECT               = WORKFLOW               + ".project";
  public static final String WORKFLOW_DESCRIPTION           = WORKFLOW               + ".description";
  public static final String WORKFLOW_COMPOSITE             = "composite";
  public static final String WORKFLOW_JAVA                  = "Java";
  public static final String WORKFLOW_SOA                   = "SOA";
  public static final String WORKFLOW_WEB_SERVICE           = "WebServices";
  public static final String WORKFLOW_XML                   = "XML";
  /** identifier to maintain the OIM ANT deployment export file */
  public static final String DEPLOYMENT_EXPORT              = FEATURE                + ".export";
  public static final String DEPLOYMENT_EXPORT_PROJECT      = DEPLOYMENT_EXPORT      + ".project";
  public static final String DEPLOYMENT_EXPORT_DESCRIPTION  = DEPLOYMENT_EXPORT      + ".description";
  /** identifier to maintain the OIM ANT deployment import file */
  public static final String DEPLOYMENT_IMPORT              = FEATURE                + ".import";
  public static final String DEPLOYMENT_IMPORT_PROJECT      = DEPLOYMENT_IMPORT      + ".project";
  public static final String DEPLOYMENT_IMPORT_DESCRIPTION  = DEPLOYMENT_IMPORT      + ".description";
  /** identifier to maintain the OIM ANT deployment import file */
  public static final String DEPLOYMENT_REQUEST             = FEATURE                + ".request";
  public static final String DEPLOYMENT_REQUEST_PROJECT     = DEPLOYMENT_REQUEST     + ".project";
  public static final String DEPLOYMENT_REQUEST_DESCRIPTION = DEPLOYMENT_REQUEST     + ".description";
  /** identifier to maintain the OIM ANT connector extension */
  public static final String CONNECTOR                      = FEATURE                + ".connector";
  public static final String CONNECTOR_ICON                 = CONNECTOR              + ".icon";
  public static final String CONNECTOR_PROJECT              = CONNECTOR              + ".project";
  public static final String CONNECTOR_DESCRIPTION          = CONNECTOR              + ".description";

  public static final String CONNECTION                     = "iam.connection";

  /** identifier to maintain the OIM identity server connection extension */
  public static final String IDENTITY_SERVER                = CONNECTION             + ".oim";
  public static final String IDENTITY_SERVER_ICON           = IDENTITY_SERVER        + ".icon";
  public static final String IDENTITY_SERVER_MNEMONIC       = IDENTITY_SERVER        + ".mnemonic";
  public static final String IDENTITY_SERVER_DESCRIPTION    = IDENTITY_SERVER        + ".description";
  public static final String IDENTITY_SERVER_ACTION         = IDENTITY_SERVER        + ".action";
  public static final String IDENTITY_SERVER_ACTION_DIS     = IDENTITY_SERVER_ACTION + ".close";
  public static final String IDENTITY_SERVER_ACTION_ADD     = IDENTITY_SERVER_ACTION + ".create";
  public static final String IDENTITY_SERVER_ACTION_UPD     = IDENTITY_SERVER_ACTION + ".modify";

  /** identifier to maintain the OIM identity server metadata file extension */
  public static final String IDENTITY_DEPLOY                = CONNECTION             + ".file";
  public static final String IDENTITY_DEPLOY_ICON           = IDENTITY_DEPLOY        + ".icon";
  public static final String IDENTITY_DEPLOY_MNEMONIC       = IDENTITY_DEPLOY        + ".mnemonic";
  public static final String IDENTITY_DEPLOY_DESCRIPTION    = IDENTITY_DEPLOY        + ".description";
  public static final String IDENTITY_DEPLOY_ACTION         = IDENTITY_DEPLOY        + ".action";
  public static final String IDENTITY_DEPLOY_ACTION_DIS     = IDENTITY_DEPLOY_ACTION + ".close";
  public static final String IDENTITY_DEPLOY_ACTION_ADD     = IDENTITY_DEPLOY_ACTION + ".create";
  public static final String IDENTITY_DEPLOY_ACTION_UPD     = IDENTITY_DEPLOY_ACTION + ".modify";

  private static final String CONTENT[][] = {
    { FEATURE,                        "Oracle Identity Manager" }
  , { NAME,                           "Identity Manager" }
  , { VENDOR,                         "Oracle Consulting Services" }
  , { LICENSE,                        "This software is the confidential and proprietary information of Oracle Corporation. (\"Confidential Information\"). You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into with Oracle." }
    // Do Not translate the copyright message.
    // It needs to be changed even when WPTG have locked down translations.
  , { COPYRIGHT,                      "Copyright &#x00A9; 2018 Oracle and/or its affiliates. All rights reserved." }
  , { DESCRIPTION,                    "Support for development of Oracle Identity Manager components" }

  , { TECHNOLOGY,                     "Identity Manager" }

  , { PROJECT_CONFIG,                 "Refactor &Build Files" }
  , { PROJECT_CONFIG_ICON,            "res:/oracle/jdeveloper/workspace/oim/gallery/pathlib.png" }
  , { PROJECT_CONFIG_DESCRIPTION,     "Regenerates the build files in the hierarchy based on the select project and the prefrences for the related feature" }
  , { PROJECT_RUNTIME,                "Refactor Runtime &Path" }
  , { PROJECT_RUNTIME_ICON,           "res:/oracle/jdeveloper/workspace/oim/gallery/pathant.png" }
  , { PROJECT_RUNTIME_DESCRIPTION,    "Regenerates the ANT classpath" }
  , { MAVEN_BUILD,                    "Refactor &Maven &Build" }
  , { MAVEN_BUILD_ICON,               "res:/oracle/jdeveloper/workspace/oim/gallery/maven.png" }
  , { MAVEN_BUILD_DESCRIPTION,        "Regenerates the Maven Build Hierarchy" }

  , { APPLICATION,                    "Identity Manager Application" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { APPLICATION_ICON,               "/oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { APPLICATION_DESCRIPTION,        "Creates an Identity Manager application.\nThe application consists of one or more Identity Manager projects for generic libraries, connectors, adapters, event handlers and/or deployments." }
  , { CUSTOMIZATION,                  "Identity Manager Customization" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { CUSTOMIZATION_ICON,             "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { CUSTOMIZATION_DESCRIPTION,      "Creates an Identity Manager customization application.\nThe application consists of two Identity Manager projects; one for the model and one for the view." }

  , { OIM_SERVER,                     "OIM Server Properties" }
  , { OIM_SERVER_PROJECT,             "Identity Manager Server Properties" }
  , { OIM_SERVER_DESCRIPTION,         "The properties configured in an ANT property file describing the connection to an Identity Manager server used during deployments.\nThe connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { SOA_SERVER,                     "SOA Server Properties" }
  , { SOA_SERVER_PROJECT,             "Identity Manager Workflow Properties" }
  , { SOA_SERVER_DESCRIPTION,         "The properties configured in an ANT property file describing the connection to a SOA server used during deployments.\nThe connection properties usually used to deploy Approval Workfloes" }
  , { OIM_PREFERENCE,                 "Identity Manager Workspace Preferences" }
  , { OIM_PREFERENCE_PROJECT,         "Identity Manager Workspace Preferences" }
  , { OIM_CONTEXT,                    "Identity Manager Deployment Context" }
  , { OIM_CONTEXT_PROJECT,            "Identity Manager Deployment Context Definitions" }
  , { OIM_CONTEXT_DESCRIPTION,        "The properties configured in an ANT property file describing the connection to an Identity Manager server used during deployments.\nThe connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OIM_DEPLOYMENT,                 "Identity Manager Deployment" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { OIM_DEPLOYMENT_ICON,            "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { OIM_DEPLOYMENT_PROJECT,         "Identity Manager Deployment Task Definitions" }
  , { OIM_DEPLOYMENT_DESCRIPTION,     "Launches the Create Identity Manager Deployment project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_FRONTEND,                   "Identity Manager Frontend" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { OIM_FRONTEND_ICON,              "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { OIM_FRONTEND_PROJECT,           "Identity Manager Frontend Preferences" }
  , { OIM_FRONTEND_DESCRIPTION,       "Launches the Create Identity Manager Frontend project wizard, with which you create an Identity Manager project using new or existing ADF Services." }
  , { OIM_BACKEND,                    "Identity Manager Backend" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { OIM_BACKEND_ICON,                "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { OIM_BACKEND_PROJECT,             "Identity Manager Backend Preferences" }
  , { OIM_BACKEND_DESCRIPTION,         "Launches the Create Identity Manager Backend project wizard, with which you create an Identity Manager project using new or existing ADF Services." }
  , { OIM_ASSEMBLY,                    "Identity Manager Assembly" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { OIM_ASSEMBLY_ICON,              "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { OIM_ASSEMBLY_PROJECT,           "Identity Manager Assembly Preferences" }
  , { OIM_ASSEMBLY_DESCRIPTION,       "Launches the Create Identity Manager Assembly project wizard, with which you create an Identity Manager project using new or existing ADF Services." }
  , { OIM_TARGET,                     "Identity Manager Workspace Targets" }
  , { OIM_TARGET_PROJECT,             "Identity Manager Workspace Targets" }
  , { ADF_TARGET,                     "Identity Customization Workspace Targets" }
  , { ADF_TARGET_PROJECT,             "Identity Customization Workspace Targets" }
  , { SOA_PREFERENCE,                 "Identity Workflow Workspace Preferences" }
  , { SOA_PREFERENCE_PROJECT,         "Identity Workflow Workspace Preferences" }
  , { SOA_CONTEXT,                    "Identity Workflow Deployment Context" }
  , { SOA_CONTEXT_PROJECT,            "Identity Workflow Deployment Context Definitions" }
  , { SOA_CONTEXT_DESCRIPTION,        "The properties configured in an ANT property file describing the connection to an Identity Manager server used during deployments.\nThe connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { SOA_DEPLOYMENT,                 "Identity Workflow Deployment" }
  , { SOA_DEPLOYMENT_PROJECT,         "Identity Workflow Deployment Task Definitions" }
  , { SOA_TARGET,                     "Identity Workflow Workspace Targets" }
  , { SOA_TARGET_PROJECT,             "Identity Workflow Workspace Targets" }
  , { ADAPTER,                        "Identity Manager Adapter" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { ADAPTER_ICON,                   "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { ADAPTER_PROJECT,                "Identity Manager Adapter Preferences" }
  , { ADAPTER_DESCRIPTION,            "Launches the Create Identity Manager Adapter project wizard, with which you create an Identity Manager project using new or existing service components like Adapters, Scheduler and Web Services." }
  , { SCHEDULER,                      "Identity Manager Scheduler" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { SCHEDULER_ICON,                 "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { SCHEDULER_PROJECT,              "Identity Manager Scheduler Preferences" }
  , { SCHEDULER_DESCRIPTION,          "Launches the Create Identity Manager Scheduler project wizard, with which you create an Identity Manager project using new or existing service components like Adapters, Scheduler and Web Services." }
  , { LIBRARY,                        "Identity Manager Library" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { LIBRARY_ICON,                   "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { LIBRARY_PROJECT,                "Identity Manager Library Preferences" }
  , { LIBRARY_DESCRIPTION,            "Launches the Create Identity Manager Library project wizard, with which you create an Identity Manager project using new or existing service components like Adapters, Scheduler and Web Services." }
  , { PLUGIN,                         "Identity Manager Plug-In" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { PLUGIN_ICON,                    "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { PLUGIN_PROJECT,                 "Identity Manager Plug-In Preferences" }
  , { PLUGIN_DESCRIPTION,             "Launches the Create Identity Manager Plug-In project wizard, with which you create an Identity Manager project using new or existing service components like Adapters, Scheduler and Web Services." }
  , { WORKFLOW,                       "Identity Manager Workflow" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { WORKFLOW_ICON,                  "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { WORKFLOW_PROJECT,               "Identity Manager Workflow Preferences" }
  , { WORKFLOW_DESCRIPTION,           "Launches the Create Identity Manager Workflow project wizard, with which you create an Identity Manager project using new or existing service components like SOA Composits." }
  , { DEPLOYMENT_EXPORT,              "ANT build file used to export artifacts from Identity Manager." }
  , { DEPLOYMENT_EXPORT_PROJECT,      "Identity Manager Deployment Exporter" }
  , { DEPLOYMENT_EXPORT_DESCRIPTION,  "Exports the project artifacts from the target host and Identity Manager Instance" }
  , { DEPLOYMENT_IMPORT,              "ANT build file used to import artifacts into Identity Manager." }
  , { DEPLOYMENT_IMPORT_PROJECT,      "Identity Manager Deployment Importer" }
  , { DEPLOYMENT_IMPORT_DESCRIPTION,  "Deploys and configures the project artifacts on the target host and the Identity Manager Instance" }
  , { DEPLOYMENT_REQUEST,             "ANT build file used to import artifacts into Identity Manager." }
  , { DEPLOYMENT_REQUEST_PROJECT,     "Identity Manager Deployment Importer" }
  , { DEPLOYMENT_REQUEST_DESCRIPTION, "Deploys and configures the project artifacts on the target host and the Identity Manager Instance" }
  , { CONNECTOR,                      "Identity Manager Connector" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { CONNECTOR_ICON,                 "oracle/jdeveloper/workspace/oim/gallery/application.png" }
  , { CONNECTOR_PROJECT,              "Identity Manager Connector Preferences" }
  , { CONNECTOR_DESCRIPTION,          "Launches the Create Identity Manager Connector project wizard, with which you create an Identity Manager project using new or existing service components like Adapters, Scheduler and Web Services that are communicating with a target system to load data from and provisioning data to such a system." }

  , { IDENTITY_SERVER,                "Identity Service" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { IDENTITY_SERVER_ICON,           "/oracle/jdeveloper/connection/oim/gallery/identity-wizard.png" }
  , { IDENTITY_SERVER_MNEMONIC,       "I" }
  , { IDENTITY_SERVER_DESCRIPTION,    "Launches the Create Identity Service Connection dialog, in which you create and edit connections.\nIf the new connection is created within the application, it will be listed in the Application Resources panel. If it is created as an IDE Connection, it will appear in the Resources window and be available for reuse." }
  , { IDENTITY_SERVER_ACTION_DIS,     "&Disconnect" }
  , { IDENTITY_SERVER_ACTION_ADD,     "&Create..." }
  , { IDENTITY_SERVER_ACTION_UPD,     "&Modify..." }

  , { IDENTITY_DEPLOY,                "Identity Deployment" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { IDENTITY_DEPLOY_ICON,           "/oracle/jdeveloper/connection/oim/gallery/identity-deploy.png" }
  , { IDENTITY_DEPLOY_MNEMONIC,       "D" }
  , { IDENTITY_DEPLOY_DESCRIPTION,    "Launches the Create Identity Service Deployment dialog, in which you create and edit connections.\nIf the new connection is created within the application, it will be listed in the Application Resources panel. If it is created as an IDE Connection, it will appear in the Resources window and be available for reuse." }
  , { IDENTITY_DEPLOY_ACTION_DIS,     "&Disconnect" }
  , { IDENTITY_DEPLOY_ACTION_ADD,     "&Create..." }
  , { IDENTITY_DEPLOY_ACTION_UPD,     "&Modify..." }

  , { Library.OIM_PLATFORM_ID,        "The Identity Manager API provides access to services available in Identity Manager. Because the APIs introduced in 11g Release 1 (11.1.1) and the legacy APIs use different conventions" }
  , { Library.OIM_CLIENT_ID,          "The Identity Manager API provides access to services available in Identity Manager. Because the APIs introduced in 11g Release 1 (11.1.1) and the legacy APIs use different conventions" }
  , { Library.OIM_ADAPTER_ID,         "The Identity Manager API provides access to services available in Identity Manager. Because the APIs introduced in 11g Release 1 (11.1.1) and the legacy APIs use different conventions" }
  , { Library.OIM_SCHEDULER_ID,       "The Identity Manager API provides access to services available in Identity Manager. Because the APIs introduced in 11g Release 1 (11.1.1) and the legacy APIs use different conventions" }
  , { Library.OIM_FRONTEND_ID,        "The Identity Self Service user interface (UI) in Identity Manager is based on Application Development Framework (ADF), which ensures consistent customization. Because the APIs introduced in 11g Release 2 (11.1.2) earlier releases use different conventions" }
  , { Library.OIM_BACKEND_ID,         "The Identity Self Service user interface (UI) in Identity Manager is based on Application Development Framework (ADF), which ensures consistent customization. Because the APIs introduced in 11g Release 2 (11.1.2) earlier releases use different conventions" }
  , { Library.HST_FOUNDATION_ID,      Library.HST_FOUNDATION_NAME + " provides the base functionality that are shared across product features" }
  , { Library.HST_FACES_ID,           Library.HST_FACES_NAME + " provides the Web Frontend functionality that are shared across product features" }
  , { Library.OIM_FOUNDATION_ID,      Library.OIM_FOUNDATION_NAME + " is the abstraction layer to compose the functionality provided by " + Library.OIM_PLATFORM_NAME + ", " + Library. OIM_ADAPTER_NAME + " and on a higher level" }
  , { Library.OIM_UTILITY_ID,         Library.OIM_UTILITY_NAME + " compose the functionality provided by " + Library.OIM_PLATFORM_NAME + ", " + Library. OIM_ADAPTER_NAME + " and on a higher level" }

    // the descripiion of the SOA Composite Editor extension plug-in shameless stolen from there
  , { Library.SOA_DESIGNTIME_ID,      "SOA Designtime is a SOA designtime library for SOA technology" }
  , { Library.SOA_RUNTIME_ID,         "SOA Runtime is a SOA runtime library for SOA technology" }
  , { Library.BPEL_RUNTIME_ID,        "BPEL Runtime is a BPEL runtime library for SOA technology" }
  , { Library.MEDIATOR_RUNTIME_ID,    "Mediator Runtime is a Mediator runtime library for SOA technology" }
  };

  // load the resource bundle itself in the resource bundle cache of the virtual
  // machine
  public static final ListResourceBundle RESOURCE = (ListResourceBundle)ResourceBundle.getBundle(
    Manifest.class.getName()
  , Locale.getDefault()
  , Manifest.class.getClassLoader()
  );

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContents (ListResourceBundle)

  /**
   ** Returns an array, where each item in the array is a pair of objects.
   ** <br>
   ** The first element of each pair is the key, which must be a
   ** <code>String</code>, and the second element is the value associated with
   ** that key.
   **
   ** @return                    an array, where each item in the array is a
   **                            pair of objects.
   */
  public Object[][] getContents() {
    return CONTENT;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Fetch a String from this {@link ListResourceBundle}.
   ** <p>
   ** This is for convenience to save casting.
   **
   ** @param  key                key into the resource array.
   **
   ** @return                    the String resource
   */
  public static String string(final String key) {
    return RESOURCE.getString(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   galleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will always have the dimension 16x16 pixel to
   ** for the layout of the Oracle JDeveloper navigator item list
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource scaled uü or doen to
   **                            a dimension of 16x16 pixel.
   */
  public static Icon galleryIcon(final String key) {
    return galleryIcon(key, 16, 16);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   galleryIcon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   ** <p>
   ** The returned {@link Icon} will scaled to the dimension <code>width</code>
   ** <code>height</code> in pixel to fit the layout of the Oracle JDeveloper
   ** Gallery item list
   **
   ** @param  key                index into the resource array.
   ** @param  width              the indented width of the generated image.
   ** @param  height             the indented height of the generated image.
   **
   ** @return                    the {@link Icon} resource scaled uü or doen to
   **                            a dimension of 16x16 pixel.
   */
  public static Icon galleryIcon(final String key, final int width, int height) {
    return RESOURCE.fetchScaledIcon(key, width, height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Fetch an {@link Icon} from this {@link ListResourceBundle}.
   **
   ** @param  key                index into the resource array.
   **
   ** @return                    the {@link Icon} resource
   */
  public static Icon icon(final String key) {
    return RESOURCE.fetchIcon(key);
  }
}