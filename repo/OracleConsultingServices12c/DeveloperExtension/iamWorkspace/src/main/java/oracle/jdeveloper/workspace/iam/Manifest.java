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

    File        :   Manifest.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Manifest.


    Revisions          Date        Editor      Comment
    -------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13   2011-05-14  DSteding    First release version
    11.1.1.3.37.60.15   2012-02-06  DSteding    New version for all the
                                                extensions included.
    11.1.1.3.37.60.27   2012-08-31  DSteding    Release 2 of IAM Suite adopted.
    11.1.1.3.37.60.28   2012-09-28  DSteding    Release 2 of IAM Suite sandbox
                                                handling incorporated.
    11.1.1.3.37.60.37   2013-06-24  DSteding    Support of Release 2
                                                customization projects
                                                incorporated.
    11.1.1.3.37.60.38   2013-07-06  DSteding    ANT-Classpath includes the common
                                                OPSS-Platform library
    11.1.1.3.37.60.39   2013-07-27  DSteding    Assembly of OIM customization
                                                artifacts added
    11.1.1.3.37.60.52   2013-10-24  DSteding    Fix for Defect DE-000102
                                                Warning at startup time about
                                                missing resource definition
                                                ocs.action.description
    11.1.1.3.37.60.66   2015-12-27  DSteding    PatchSet 3 of IAM Suite Release 2
                                                adopted
    11.1.1.3.37.60.69   2015-12-27  DSteding    Removed Oracle from the product
                                                names and libraries because its
                                                clear where we are.
    11.1.1.3.37.60.71   2017-10-27  DSteding    Access Manager Features extended.
    11.1.1.3.37.60.72   2017-11-29  DSteding    Deployment templates referencing
                                                formerly defined namespace
                                                http://oracle.hst.deployment/weblogic
                                                now as
                                                http://oracle.hst.deployment/topology.
    11.1.1.3.37.60.73   2017-12-15  DSteding    Add Access Manager libraries to
                                                control registrations of various
                                                components.
    12.2.1.3.42.60.74   2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.84   2019-05-24  DSteding    DocBook Site Author Provider
                                                added.
    12.2.1.3.42.60.84   2019-05-24  DSteding    ContentSet for build and test are
                                                included in the initial project
                                                setup.
    12.2.1.3.42.60.85   2019-11-22  DSteding    PatchSet 4 of IAM Suite 12c
                                                Release 2 adopted
    12.2.1.3.42.60.93   2021-02-14  DSteding    PatchSet 4 of IAM Suite 12c
                                                Release 2 extended
    12.2.1.3.42.60.100  2022-05-27  DSteding    Build runtime path includes0
                                                directory deployment
    12.2.1.3.42.60.101  2022-06-11  DSteding    Support for Foundation Services
                                                Library Generation
    12.2.1.3.42.60.102  2023-04-14  DSteding    Extend Directory Service to copy
                                                specific parts of an Directory
                                                Entry
*/

package oracle.jdeveloper.workspace.iam;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.Icon;

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
 ** @version 12.2.1.3.42.60.101
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
  public static final String FEATURE                       = "iam";
  public static final String FEATURE_AMF                   = "amf";
  public static final String FEATURE_IMF                   = "imf";
  public static final String FEATURE_DSF                   = "dsf";

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
  public static final String KEY                           = AbstractAddin.DATA_KEY;
  public static final String VERSION                       = "12.2.1.3.42.60.104";

  /**
   ** the translatable strings
   */
  public static final String NAME                          = FEATURE           + ".name";
  public static final String VENDOR                        = FEATURE           + ".vendor";
  public static final String LICENSE                       = FEATURE           + ".license";
  public static final String COPYRIGHT                     = FEATURE           + ".copyright";
  public static final String DESCRIPTION                   = FEATURE           + ".description";
  public static final String DESCRIPTION_AMF               = FEATURE_AMF       + ".description";
  public static final String DESCRIPTION_IMF               = FEATURE_IMF       + ".description";
  public static final String DESCRIPTION_DSF               = FEATURE_DSF       + ".description";

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
  public static final String TECHNOLOGY                    = FEATURE           + ".technology";
  public static final String TECHNOLOGY_AMF                = FEATURE_AMF       + ".technology";
  public static final String TECHNOLOGY_IMF                = FEATURE_IMF       + ".technology";
  public static final String TECHNOLOGY_DSF                = FEATURE_DSF       + ".technology";

  public static final String COMMAND                       = "iam.generate";
  public static final String COMMAND_NAME                  = COMMAND           + ".name";
  public static final String COMMAND_DESCRIPTION           = COMMAND           + ".description";

  /** identifier where all templates located */
  public static final String TEMPLATE_PACKAGE              = "template.iam";

  /** identifier to maintain the workspace preference properties */
  public static final String IAM_PREFERENCE                = "iam.preferences";
  public static final String WKS_PREFERENCE                = "wks.preferences";
  /** identifier to maintain the SCP Server properties */
  public static final String SCP_SERVER                    = "scp.server";
  public static final String SCP_SERVER_PROJECT            = SCP_SERVER        + ".project";
  public static final String SCP_SERVER_DESCRIPTION        = SCP_SERVER        + ".description";
  /** identifier to maintain the Application Server Server properties */
  public static final String JEE_SERVER                    = "jee.server";
  public static final String JEE_SERVER_PROJECT            = JEE_SERVER        + ".project";
  public static final String JEE_SERVER_DESCRIPTION        = JEE_SERVER        + ".description";
  /** identifier to maintain the Metadata Store Server properties */
  public static final String MDS_SERVER                    = "mds.server";
  public static final String MDS_SERVER_PROJECT            = MDS_SERVER        + ".project";
  public static final String MDS_SERVER_DESCRIPTION        = MDS_SERVER        + ".description";
  /** identifier to maintain the Access Manager Server properties */
  public static final String OAM_SERVER                    = "oam.server";
  public static final String OAM_SERVER_DESCRIPTION        = OAM_SERVER        + ".description";
  /** identifier to maintain the Access Manager library extension */
  public static final String OAM_LIBRARY                   = "oam.library";
  public static final String OAM_LIBRARY_DESCRIPTION       = OAM_LIBRARY       + ".description";
  /** identifier to maintain the Access Manager Agent properties */
  public static final String OAM_AGENT                     = "oam.agent";
  public static final String OAM_AGENT_DESCRIPTION         = OAM_AGENT         + ".description";
  /** identifier to maintain the Access Manager Authentication Plug-In properties */
  public static final String OAM_PLUGIN                    = "oam.plugin";
  public static final String OAM_PLUGIN_DESCRIPTION        = OAM_PLUGIN        + ".description";
  /** identifier to maintain the Access Manager Credential Collector properties */
  public static final String OAM_COLLECTOR                 = "oam.collector";
  public static final String OAM_COLLECTOR_DESCRIPTION     = OAM_COLLECTOR     + ".description";
  /** identifier to maintain the Access Manager deployment extension */
  public static final String OAM_DEPLOYMENT                = "oam.deployment";
  public static final String OAM_DEPLOYMENT_DESCRIPTION    = OAM_DEPLOYMENT    + ".description";
  /** identifier to maintain the DIP Server properties */
  public static final String DIP_SERVER                    = "dip.server";
  public static final String DIP_SERVER_DESCRIPTION        = DIP_SERVER        + ".description";
  /** identifier to maintain the Directory Service Server properties */
  public static final String ODS_SERVER                    = "ods.server";
  public static final String ODS_SERVER_PROJECT            = ODS_SERVER        + ".project";
  public static final String ODS_SERVER_DESCRIPTION        = ODS_SERVER        + ".description";
  /** identifier to maintain the Internet Directory Service Server properties */
  public static final String OID_SERVER                    = "ovd.server";
  public static final String OID_SERVER_PROJECT            = OID_SERVER        + ".project";
  public static final String OID_SERVER_DESCRIPTION        = OID_SERVER        + ".description";
  /** identifier to maintain the Internet Directory Service Server properties */
  public static final String OUD_SERVER                    = "ovd.server";
  public static final String OUD_SERVER_PROJECT            = OUD_SERVER        + ".project";
  public static final String OUD_SERVER_DESCRIPTION        = OUD_SERVER        + ".description";
  /** identifier to maintain the Virtual Directory Service Server properties */
  public static final String OVD_SERVER                    = "ovd.server";
  public static final String OVD_SERVER_PROJECT            = OVD_SERVER        + ".project";
  public static final String OVD_SERVER_DESCRIPTION        = OVD_SERVER        + ".description";
  /** identifier to maintain the Identity Manager Adapter Project properties */
  public static final String OIM_ADAPTER                   = "oim.adapter";
  public static final String OIM_ADAPTER_DESCRIPTION       = OIM_ADAPTER       + ".description";
  /** identifier to maintain the Identity Manager Assembly Project properties */
  public static final String OIM_ASSEMBLY                  = "oim.assembly";
  public static final String OIM_ASSEMBLY_DESCRIPTION      = OIM_ASSEMBLY      + ".description";
  /** identifier to maintain the Identity Manager Backend Project properties */
  public static final String OIM_BACKEND                   = "oim.backend";
  public static final String OIM_BACKEND_DESCRIPTION       = OIM_BACKEND       + ".description";
  /** identifier to maintain the Identity Manager Connector Project properties */
  public static final String OIM_CONNECTOR                 = "oim.connector";
  public static final String OIM_CONNECTOR_DESCRIPTION     = OIM_CONNECTOR     + ".description";
  /** identifier to maintain the Identity Manager Deployment Project properties */
  public static final String OIM_DEPLOYMENT                = "oim.deployment";
  public static final String OIM_DEPLOYMENT_DESCRIPTION    = OIM_DEPLOYMENT    + ".description";
  /** identifier to maintain the Identity Manager Frontend Project properties */
  public static final String OIM_FRONTEND                  = "oim.frontend";
  public static final String OIM_FRONTEND_DESCRIPTION      = OIM_FRONTEND      + ".description";
  /** identifier to maintain the Identity Manager Library Project properties */
  public static final String OIM_LIBRARY                   = "oim.library";
  public static final String OIM_LIBRARY_DESCRIPTION       = OIM_LIBRARY       + ".description";
  /** identifier to maintain the Identity Manager Plugin Project properties */
  public static final String OIM_PLUGIN                    = "oim.plugin";
  public static final String OIM_PLUGIN_DESCRIPTION        = OIM_PLUGIN        + ".description";
  /** identifier to maintain the Identity Manager Server properties */
  public static final String OIM_SERVER                    = "oim.server";
  public static final String OIM_SERVER_DESCRIPTION        = OIM_SERVER        + ".description";
  /** identifier to maintain the Identity Manager Workflow Project properties */
  public static final String OIM_WORKFLOW                  = "oim.workflow";
  public static final String OIM_WORKFLOW_DESCRIPTION      = OIM_WORKFLOW      + ".description";
  /** identifier to maintain the SOA Server properties */
  public static final String SOA_SERVER                    = "soa.server";
  public static final String SOA_SERVER_DESCRIPTION        = SOA_SERVER        + ".description";

  public static final String CONNECTION                    = FEATURE           + ".connection";

  /** identifier to maintain the IAM metadata server connection extension */
  public static final String METADATA_SERVER               = CONNECTION        + ".mds";
  public static final String METADATA_SERVER_ICON          = METADATA_SERVER   + ".icon";
  public static final String METADATA_SERVER_MNEMONIC      = METADATA_SERVER   + ".mnemonic";
  public static final String METADATA_SERVER_DESCRIPTION   = METADATA_SERVER   + ".description";
  public static final String METADATA_SERVER_FOLDER_CREATE = METADATA_SERVER   + ".folder.create";
  public static final String METADATA_ACTION               = METADATA_SERVER   + ".action";
  public static final String METADATA_SERVER_ACTION_DIS    = METADATA_ACTION   + ".close";
  public static final String METADATA_SERVER_ACTION_EXP    = METADATA_ACTION   + ".export";
  public static final String METADATA_SERVER_ACTION_IMP    = METADATA_ACTION   + ".import";
  public static final String METADATA_SERVER_ACTION_UPD    = METADATA_ACTION   + ".update";
  public static final String METADATA_SERVER_ACTION_REN    = METADATA_ACTION   + ".rename";
  public static final String METADATA_SERVER_ACTION_VER    = METADATA_ACTION   + ".version";
  public static final String METADATA_SERVER_ACTION_PRG    = METADATA_ACTION   + ".purge";

  /** identifier to maintain the IAM directory server connection extension */
  public static final String DIRECTORY_SERVER              = CONNECTION        + ".ods";
  public static final String DIRECTORY_SERVER_ICON         = DIRECTORY_SERVER  + ".icon";
  public static final String DIRECTORY_SERVER_MNEMONIC     = DIRECTORY_SERVER  + ".mnemonic";
  public static final String DIRECTORY_SERVER_PROPERTY     = DIRECTORY_SERVER  + ".property";
  public static final String DIRECTORY_SERVER_DESCRIPTION  = DIRECTORY_SERVER  + ".description";
  public static final String DIRECTORY_ACTION              = DIRECTORY_SERVER  + ".action";
  public static final String DIRECTORY_SERVER_ACTION_SCH   = DIRECTORY_ACTION  + ".search";
  public static final String DIRECTORY_SERVER_ACTION_ADD   = DIRECTORY_ACTION  + ".create";
  public static final String DIRECTORY_SERVER_ACTION_LKL   = DIRECTORY_ACTION  + ".likely";
  public static final String DIRECTORY_SERVER_ACTION_UPD   = DIRECTORY_ACTION  + ".modify";
  public static final String DIRECTORY_SERVER_ACTION_REN   = DIRECTORY_ACTION  + ".rename";
  public static final String DIRECTORY_SERVER_ACTION_MOV   = DIRECTORY_ACTION  + ".move";
  public static final String DIRECTORY_SERVER_ACTION_EXP   = DIRECTORY_ACTION  + ".export";
  public static final String DIRECTORY_SERVER_ACTION_IMP   = DIRECTORY_ACTION  + ".import";
  public static final String DIRECTORY_SERVER_ACTION_CPD   = DIRECTORY_ACTION  + ".copy.dn";
  public static final String DIRECTORY_SERVER_ACTION_CPN   = DIRECTORY_ACTION  + ".copy.name";
  public static final String DIRECTORY_SERVER_ACTION_CPV   = DIRECTORY_ACTION  + ".copy.value";
  public static final String DIRECTORY_EDITOR              = DIRECTORY_SERVER  + ".editor";
  public static final String DIRECTORY_EDITOR_GENERAL      = DIRECTORY_EDITOR  + ".general";
  public static final String DIRECTORY_EDITOR_SCHEMA       = DIRECTORY_EDITOR  + ".schema";

  private static final String CONTENT[][] = {
    { FEATURE,                       "Identity and Access Management" }
  , { FEATURE_AMF,                   "Access Management" }
  , { FEATURE_IMF,                   "Identity Management" }
  , { FEATURE_DSF,                   "Directory Services" }
  , { NAME,                          "Extension Foundation" }
  , { VENDOR,                        "Oracle Consulting Services" }
  , { LICENSE,                       "This software is the confidential and proprietary information of Oracle Corporation. (\"Confidential Information\"). You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into with Oracle." }
    // Do Not translate the copyright message.
    // It needs to be changed even when WPTG have locked down translations.
  , { COPYRIGHT,                     "Copyright &#x00A9; 2018 Oracle and/or its affiliates. All rights reserved." }
  , { DESCRIPTION,                   "Support for development of Oracle Identity and Access Management components" }
  , { DESCRIPTION_AMF,               "Support for development of Oracle Access Management components" }
  , { DESCRIPTION_IMF,               "Support for development of Oracle Identity Governance components" }
  , { DESCRIPTION_DSF,               "Support for development of Oracle Directory Service components" }

  , { TECHNOLOGY,                    "Identity and Access Management" }
  , { TECHNOLOGY_AMF,                "Access Management" }
  , { TECHNOLOGY_IMF,                "Identity Governance" }
  , { TECHNOLOGY_DSF,                "Directory Services" }

  , { COMMAND_NAME,                  "Consulting Library Manager" }
  , { COMMAND_DESCRIPTION,           "Creates the Consulting Framework libraries, with which you developing new or existing service components like Agents, Connectors etc.\\n\\nTo enable this option, you must select the Identity and Access Manager preference page." }

  , { IAM_PREFERENCE,                "Identity and Access Management Global Preferences" }
  , { WKS_PREFERENCE,                "Identity and Access Management Workspace Preferences" }

  , { DIP_SERVER,                    "DIP Server Properties" }
  , { DIP_SERVER_DESCRIPTION,        "The properties configured in an ANT property file describing the connection to a Directory Integration Platform server used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { JEE_SERVER,                    "JEE Server Properties" }
  , { JEE_SERVER_PROJECT,            "Application Server Properties" }
  , { JEE_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a JEE server used during deployments.\n The connection properties usually used to export or import artifacts through the JMX MBean Server" }
  , { MDS_SERVER,                    "MDS Server Properties" }
  , { MDS_SERVER_PROJECT,            "Metadata Service Server Properties" }
  , { MDS_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a Metadata Service used during deployments.\n The connection properties usually used to deploy XML artifacts to the approüriate partition" }
  , { OAM_AGENT,                     "Access Agent" }
  , { OAM_AGENT_DESCRIPTION,         "The properties configured in a build property file describing the build properties of an Access Server Agent" }
  , { OAM_COLLECTOR,                 "Access Credential Collector" }
  , { OAM_COLLECTOR_DESCRIPTION,     "The properties configured in a build property file describing the build properties of an Access Server Credential Collector" }
  , { OAM_DEPLOYMENT,                "Access Deployment" }
  , { OAM_DEPLOYMENT_DESCRIPTION,    "Opens the Create Access Manager Deployment project wizard, with which you create an Access Manager project using new or existing deployment service components." }
  , { OAM_LIBRARY,                   "Access Library" }
  , { OAM_LIBRARY_DESCRIPTION,       "Opens the Create Access Manager Library project wizard, with which you create an Access Manager project using new or existing deployment service components." }
  , { OAM_PLUGIN,                    "Access Authenticator" }
  , { OAM_PLUGIN_DESCRIPTION,        "The properties configured in a build property file describing the build properties of an Access Server Authentication Plug-In" }
  , { OAM_SERVER,                    "OAM Server Properties" }
  , { OAM_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to an Access Manager server used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { ODS_SERVER,                    "ODS Server Properties" }
  , { ODS_SERVER_PROJECT,            "ODS Server Properties" }
  , { ODS_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a Directory Services used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OID_SERVER,                    "OID Server Properties" }
  , { OID_SERVER_PROJECT,            "OID Server Properties" }
  , { OID_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a Internet Directory Services used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OUD_SERVER,                    "OID Server Properties" }
  , { OUD_SERVER_PROJECT,            "OUD Server Properties" }
  , { OUD_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a Unified Directory Services used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OVD_SERVER,                    "OVD Server Properties" }
  , { OVD_SERVER_PROJECT,            "OVD Server Properties" }
  , { OVD_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to a Virtual Directory Services used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OIM_ADAPTER,                   "Identity Adapter" }
  , { OIM_ADAPTER_DESCRIPTION,       "Opens the Create Identity Manager Adapter project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_ASSEMBLY,                  "Identity Assemply" }
  , { OIM_ASSEMBLY_DESCRIPTION,      "Opens the Create Identity Manager Assembly Customization project wizard, with which you create an Identity Manager project to package and deploy Identity Manager customization artifacts." }
  , { OIM_BACKEND,                   "Identity Backend" }
  , { OIM_BACKEND_DESCRIPTION,       "Opens the Create Identity Manager Backend Customization project wizard, with which you create an Identity Manager project using new or existing service components Identity Manager components." }
  , { OIM_CONNECTOR,                 "Identity Connector" }
  , { OIM_CONNECTOR_DESCRIPTION,     "Opens the Create Identity Manager Connector project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_DEPLOYMENT,                "Identity Deployment" }
  , { OIM_DEPLOYMENT_DESCRIPTION,    "Opens the Create Identity Manager Deployment project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_FRONTEND,                  "Identity Frontend" }
  , { OIM_FRONTEND_DESCRIPTION,      "Opens the Create Identity Manager Frontend Customization project wizard, with which you create an Identity Manager project using new or existing service components Identity Manager components like TaskFlows." }
  , { OIM_LIBRARY,                   "Identity Library" }
  , { OIM_LIBRARY_DESCRIPTION,       "Opens the Create Identity Manager Library project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_PLUGIN,                    "Identity Plug-In" }
  , { OIM_PLUGIN_DESCRIPTION,        "Opens the Create Identity Manager Plug-In project wizard, with which you create an Identity Manager project using new or existing deployment service components." }
  , { OIM_SERVER,                    "OIM Server Properties" }
  , { OIM_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to an Identity Manager server used during deployments.\n The connection properties usually used to export or import artifacts through the Deployment Manager" }
  , { OIM_WORKFLOW,                  "Identity Workflow" }
  , { OIM_WORKFLOW_DESCRIPTION,      "Opens the Create Identity Manager Workflow project wizard, with which you create an Identity Manager project using new or existing service components like SOA Composits." }
  , { SCP_SERVER,                    "SCP Server Properties" }
  , { SCP_SERVER_PROJECT,            "SCP Server Properties" }
  , { SCP_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to used to transfer files to a remote server that supports this protocol during deployments.\n The connection properties usually used to transfer the Java Archivs and plugins created during the development prozess" }
  , { SOA_SERVER,                    "SOA Server Properties" }
  , { SOA_SERVER_DESCRIPTION,        "The properties configured in a build property file describing the connection to an SOA server used during deployments.\n The connection properties usually used to deploy Approval Workfloes" }

  , { METADATA_SERVER,               "Metadata Service" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { METADATA_SERVER_ICON,          "/oracle/jdeveloper/connection/iam/gallery/metadata-wizard.png" }
  , { METADATA_SERVER_MNEMONIC,      "M" }
  , { METADATA_SERVER_DESCRIPTION,   "Launches the Create Metadata Service Connection wizard, in which you can create a database-based connection to a Meta Data Service (MDS) Server." }
  , { METADATA_SERVER_FOLDER_CREATE, "&Create Folder..." }
  , { METADATA_SERVER_ACTION_DIS,    "&Disconnect" }
  , { METADATA_SERVER_ACTION_EXP,    "&Export..." }
  , { METADATA_SERVER_ACTION_IMP,    "&Import..." }
  , { METADATA_SERVER_ACTION_UPD,    "&Update..." }
  , { METADATA_SERVER_ACTION_REN,    "&Rename..." }
  , { METADATA_SERVER_ACTION_VER,    "&Version..." }
  , { METADATA_SERVER_ACTION_PRG,    "&Purge..." }

  , { DIRECTORY_SERVER,              "Directory Service" }
    // icon is passed to the gallery manager by a project template
    // since 12c it needs to be a fullqualified path if its referenced in the
    // extension descriptor by a resource key
  , { DIRECTORY_SERVER_ICON,          "/oracle/jdeveloper/connection/iam/gallery/directory-wizard.png" }
  , { DIRECTORY_SERVER_MNEMONIC,      "D" }
  , { DIRECTORY_SERVER_DESCRIPTION,   "Launches the Create Directory Service Connection wizard, in which you create a connection to a directory server." }
  , { DIRECTORY_SERVER_ACTION_SCH,    "&Search..." }
  , { DIRECTORY_SERVER_ACTION_ADD,    "&Create..." }
  , { DIRECTORY_SERVER_ACTION_LKL,    "Create &Like..." }
  , { DIRECTORY_SERVER_ACTION_UPD,    "&Modify" }
  , { DIRECTORY_SERVER_ACTION_REN,    "&Rename..." }
  , { DIRECTORY_SERVER_ACTION_MOV,    "Mo&ve..." }
  , { DIRECTORY_SERVER_ACTION_EXP,    "&Export Data..." }
  , { DIRECTORY_SERVER_ACTION_IMP,    "&Import Data..." }
  , { DIRECTORY_SERVER_ACTION_CPD,    "Copy &Distiguished Name" }
  , { DIRECTORY_SERVER_ACTION_CPN,    "Copy &Name" }
  , { DIRECTORY_SERVER_ACTION_CPN,    "Copy &Value" }
  , { DIRECTORY_EDITOR_GENERAL,       "General" }
  , { DIRECTORY_EDITOR_SCHEMA,        "Schema" }
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
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument            the subsitution value for {0}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument) {
    return RESOURCE.formatted(key, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2) {
    return RESOURCE.formatted(key, argument1, argument2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   **
   ** @param  key                 key into the resource array.
   ** @param  argument1           the subsitution value for {0}
   ** @param  argument2           the subsitution value for {1}
   ** @param  argument3           the subsitution value for {2}
   **
   ** @return                     the formatted String resource
   */
  public static String format(final String key, final Object argument1, final Object argument2, final Object argument3) {
    return RESOURCE.formatted(key, argument1, argument2, argument3);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringFormat
  /**
   ** Fetch a formatted String resource from this {@link ListResourceBundle}.
   ** <p>
   ** This will substitute "{n}" occurrences in the string resource with the
   ** appropriate parameter "n" from the array.
   **
   ** @param  key                key into the resource array.
   ** @param  arguments          the array of substitution parameters.
   **
   ** @return                     the formatted String resource
   */
  public static String stringFormat(final String key, final Object... arguments) {
    return RESOURCE.stringFormatted(key, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   icon
  /**
   ** Fetchs a string for the given key from this resource bundle or one of its
   ** parents. Calling this method is equivalent to calling
   ** (String)internalObject(key).
   **
   ** @param  key                 the key for the desired string
   **
   ** @return                     the {@link Icon} resource the specified
   **                             <code>key</code> belongs to.
   */
  public static Icon icon(final String key) {
    return RESOURCE.fetchIcon(key);
  }
}