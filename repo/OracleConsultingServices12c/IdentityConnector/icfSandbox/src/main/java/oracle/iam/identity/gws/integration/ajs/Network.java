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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-19-05  SBernet     First release version
*/

package oracle.iam.identity.gws.integration.ajs;

import java.io.File;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

import oracle.iam.identity.gws.integration.jira.ServiceFeature;
import oracle.iam.identity.gws.integration.jira.ServiceResource;

////////////////////////////////////////////////////////////////////////////////
// enum Network
// ~~~~ ~~~~~~~
/**
 ** The <code>Network</code> implements the environment functionality for a
 ** Test Case.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class Network {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  static final SystemConsole CONSOLE  = new SystemConsole("ajs");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** local network.
   */
  static final String        LOCALNET = "dsteding-de.de.oracle.com";
  /**
   ** The configuration file extending the standard <code>IT Resource</code>
   ** configuration.
   */
  static final String        INTRANET = "hardy.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String        EXTRANET = "hardy.vm.oracle.com";
  /**
   ** The location of the credential file to use
   */
  //static final String        PASSWORD = System.getProperty(Password.class.getName().toLowerCase());
  //static final File          FEATURE  = new File("C:/Oracle/Oracle Consulting/OracleConsultingServices12c/IdentityConnector/icfRESTFul/src/main/resources/mds/jira-feature.xml");

  static final File          FEATURE  = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/ajs-feature.xml");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  protected static ConnectorFacade facade(final ServiceResource resource)
    throws TaskException {

    return BundleLocator.create(server(), resource, ServiceFeature.build(CONSOLE, FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Factory method to create a new {@link ServerResource}.
   ** <br>
   ** The {@link ServerResource} is always running on the same host system.
   **
   ** @return                    the {@link ServerResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  protected static ServerResource server()
    throws TaskException {

    return ServerResource.build(CONSOLE, LOCALNET, "8757", false, "Sophie20061990$", Integer.MAX_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localnet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static ServiceResource localnet()
    throws TaskException {

    return endpoint(LOCALNET, "8080");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static ServiceResource intranet()
    throws TaskException {

    return endpoint(INTRANET, "8080");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            extranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static ServiceResource extranet()
    throws TaskException {

    return endpoint(EXTRANET, "8080");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link ServiceResource} for the specified
   ** network connectivity.
   **
   ** @param  server             the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  port               the port number of the target
   **                            system on which the Service Provider is
   **                            listening on.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private static ServiceResource endpoint(final String server, final String port)
    throws TaskException {

    return ServiceResource.build(CONSOLE, server, port, "/rest/api/2", false, "application/json", "application/json", "basic-preemptive", null, null, null, "dsteding", "Welcome1", "", "", "en", "US", "GMT+01:00", "eeeee");
  }
}