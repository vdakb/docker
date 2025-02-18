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

    File        :   Runtime.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Runtime.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.60.42  2011-05-14  DSteding    First release version
    11.1.1.3.37.60.48  2013-10-24  DSteding    Fix for Defect DE-000111
                                               Wrong path in classpath element
                                               referring to Java Archive
                                               bpm-services.jar
    11.1.1.3.37.60.49  2013-10-24  DSteding    Fix for Defect DE-000112
                                               Wrong name of Java Archive
                                               bpm-services.jar
    11.1.1.3.37.60.50  2013-10-24  DSteding    Fix for Defect DE-000113
                                               Wrong path in classpath element
                                               referring to Java Archive
                                               hst-deployment.jar
    11.1.1.3.37.60.51  2013-07-24  DSteding    Fix for Defect DE-000115
                                               Error shown if RMI connection
                                               opend to SOA Suite from ANT
                                               script
    11.1.1.3.37.60.52  2013-11-19  DSteding    Added jps-mbean.jar to the ANT
                                               classpath
    11.1.1.3.37.60.59  2014-07-27  DSteding    Added ws_confmbeans.jar,
                                               wsm-policy-core.jar, orawsdl.jar
                                               wsm-pmlib.jar, osdt_xmlsec.jar
                                               wsm-agent-core.jar, osdt_wss.jar
                                               oracle.logging-utils_11.1.1.jar,
                                               wsm-secpol.jar to the ANT
                                               classpath
    11.1.1.3.37.60.62  2014-11-23  DSteding    Extended ANT classpath by
                                               adding adf-share-support.jar,
                                               adf-share-ca.jar,
                                               javatools-nodeps.jar,
                                               resourcebundle.jar, which are
                                               needed by the sandbox operations.
    11.1.1.3.37.60.69  2017-31-01  DSteding    Removed spaces in directory names
                                               to avoid code generation issues.
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
    12.2.1.3.42.60.81  2018-10-15  DSteding    Extended ANT classpath by
                                               adding orai18n-mapping.jar which
                                               is needed by the sandbox
                                               operations.
    12.2.1.3.42.60.82  2018-12-12  DSteding    Removed ANT wsm-pmlib.jar and
                                               wsm-agent-core.jar from classpath
                                               and extended by
                                               adding javax.management.j2ee.jarclasspath
                                               due to the changes in the deployment
                                               framework.
    12.2.1.3.42.60.99 2021-10-22   DSteding    Accomplished ANT runtime path
                                               with wlfullclient.jar.
*/

package oracle.jdeveloper.workspace.oim.project;

import java.io.File;

import oracle.ide.Ide;

import oracle.ide.net.URLPath;

import oracle.ide.config.Preferences;

import oracle.ide.controller.Command;

import oracle.ide.model.Project;

import oracle.javatools.dialogs.MessageDialog;

import oracle.jdeveloper.ant.AntRunConfiguration;

import oracle.jdeveloper.workspace.iam.preference.Provider;

import oracle.jdeveloper.workspace.iam.resource.ComponentBundle;

import oracle.jdeveloper.workspace.iam.wizard.TemplateProjectConfigurator;

import oracle.jdeveloper.workspace.oim.Bundle;

import oracle.jdeveloper.workspace.oim.preference.Store;

////////////////////////////////////////////////////////////////////////////////
// class Runtime
// ~~~~~ ~~~~~~~
/**
 ** The factory implementation that creates the runtime configurator based on
 ** the technology scope of a Oracle JDeveloper Project.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.99
 ** @since   11.1.1.3.37.60.42
 */
public class Runtime extends Command {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Command ID to maintain the Oracle JDeveloper ANT runtime classpath */
  public static final String COMMAND = "oim.jpr.runtime";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Runtime</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Runtime() {
    // ensure inheritance
    super(action());
  }

   //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doit (Command)
  /**
   ** Executes the actions associated with a specific command.
   ** <p>
   ** When a command executes successfully, implementations should return
   ** {@link Command#OK OK}, otherwise, return
   ** {@link Command#CANCEL CANCEL} or any other non-zero value.
   ** <p>
   ** The default implementation always returns {@link Command#OK OK}.
   ** Subclasses should override this method to implement their own execution
   ** and return the appropriate value.
   **
   ** @return                    {@link Command#OK OK} id the command
   **                            execution completes successfully; otherwise
   **                            {@link Command#CANCEL CANCEL} or any
   **                            other non-zero value.
   **
   ** @throws Exception          if the execution of the command fails in
   **                            general
   */
  @Override
  public int doit()
    throws Exception {

    if (MessageDialog.confirm(Ide.getMainWindow(), Bundle.string(Bundle.CONFIGURE_RUNTIME_HEADER), Bundle.string(Bundle.CONFIGURE_RUNTIME_TEXT), "???", true)) {
      configure(getContext().getProject());
      return OK;
    }
    else
      return CANCEL;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action (action)
  /**
   ** Returns the id of the action this command is associated with.
   **
   ** @return                    the id of the action this command is associated
   **                            with.
   **
   ** @throws IllegalStateException if the action this command is associated
   **                               with is not registered.
   */
  public static int action() {
    final Integer id = Ide.findCmdID(COMMAND);
    if (id == null)
      throw new IllegalStateException(ComponentBundle.format(ComponentBundle.COMMAND_NOTFOUND, COMMAND));

    return id;
  }

 //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Configures the ANT runtime classpath a project needs.
   **
   ** @param  project            the JDeveloper {@link Project} to configure a
   **                            proper ANT runtime classpath for.
   */
  protected static void configure(final Project project) {
    final Provider preference = Provider.instance(Preferences.getPreferences());
    final Store    provider   = Store.instance(Preferences.getPreferences());
    final File     ideHome    = TemplateProjectConfigurator.featureFolder(new File(Ide.getOracleHomeDirectory()), 1);
    final File     wlsHome    = new File(ideHome, "wlserver");
    final File     fmwHome    = new File(ideHome, "oracle_common/modules");
    final File     soaHome    = new File(ideHome, "soa/soa/modules");
    final File     hstHome    = TemplateProjectConfigurator.toFolder(preference.headstart());
    final File     ocsHome    = TemplateProjectConfigurator.toFolder(provider.foundation());
    // setup the ANT classpath used by the deployment utilities
    final URLPath  classpath  = new URLPath();
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "javax.management.j2ee.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "thirdparty/features/jsch.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "thirdparty/ant-contrib-1.0b3.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "org.apache.commons.logging_1.2.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.logging-utils.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.odl/ojdl.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.dms/dms.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.ucp.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.javatools/resourcebundle.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.javatools/javatools-nodeps.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.javatools/javatools-annotations.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.share.ca/adf-share-base.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.share.ca/adf-share-ca.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.share/adflogginghandler.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.share/adf-share-support.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.model/adfm.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.adf.businesseditor/adf-businesseditor-model.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.xdk/xml.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.xdk/xmlparserv2.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.xmlef/xmlef.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.bali.share/share.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.mds/mdsrt.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.mds/oramds.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.jdbc/ojdbc8.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.nlsrtl/orai18n-mapping.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "com.oracle.http_client.http_client.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.osdt/osdt_wss.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.osdt/osdt_xmlsec.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "clients/com.oracle.webservices.fmw.client.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.webservices_11.1.1/orawsdl.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.webservices_11.1.1/ws_confmbeans.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.wsm.common/wsm-secpol.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.wsm.common/wsm-policy-core.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.jps/jps-mbeans.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(fmwHome, "oracle.toplink/eclipselink.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "commons-cli-1.1.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "oracle.soa.fabric_11.1.1/fabric-runtime.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "oracle.soa.fabric_11.1.1/soa-infra-tools.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "oracle.soa.fabric_11.1.1/oracle-soa-client-api.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "oracle.soa.mgmt_11.1.1/soa-infra-mgmt.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(soaHome, "oracle.soa.workflow_11.1.1/bpm-services.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(wlsHome, "modules/com.oracle.weblogic.ant.taskdefs.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(wlsHome, "server/lib/wlfullclient.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBasePlatform/" + provider.release() + "/lib/iam-platform-pluginframework.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBasePlatform/" + provider.release() + "/lib/iam-platform-workflowservice.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBaseClient/"   + provider.release() + "/lib/jrf-api.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBaseClient/"   + provider.release() + "/lib/oimclient.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBaseServer/"   + provider.release() + "/lib/xlAPI.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "CodeBaseServer/"   + provider.release() + "/lib/xlDDM.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(hstHome, "hstFoundation/lib/hst-foundation.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(hstHome, "hstDeployment/lib/hst-deployment.jar"));
    classpath.add(TemplateProjectConfigurator.pathEntry(ocsHome, "oimDeployment/lib/oim-deployment.jar"));

    // configure the ANT class path needed for deployments
    final AntRunConfiguration configuration = AntRunConfiguration.getInstance(project);
    configuration.setClassPath(classpath);
  }
}