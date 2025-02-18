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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Directory Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gds.integration;

import java.io.File;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

import oracle.iam.identity.gds.integration.DirectoryFeature;
import oracle.iam.identity.gds.integration.DirectoryResource;

import oracle.iam.junit.Password;

////////////////////////////////////////////////////////////////////////////////
// class Network
// ~~~~~ ~~~~~~~
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
  public static final SystemConsole CONSOLE    = new SystemConsole("gds");
  /**
   ** The configuration file extending the standard <code>IT Resource</code>
   ** configuration.
   */
  public static final File          CONFIG     = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/gds-feature.xml");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET   = "buster.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET   = "www.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTERNET   = "dbs.cinnamonstar.dev";
  /**
   ** The location of the credential file to use
   */
  static final String               PASSWORD   = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Returns the {@link ConnectorFacade} based on configuration map passed.
   **
   ** @param  resource           the {@link DirectoryResource} containing the
   **                            properties to establish the connection to a
   **                            target system.
   **                            <br>
   **                            Allowed object is {@link DirectoryResource}.
   */
  protected static ConnectorFacade facade(DirectoryResource resource)
    throws TaskException {

    return BundleLocator.create(server(), resource, DirectoryFeature.build(CONSOLE, CONFIG));
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

    return ServerResource.build(CONSOLE, "dsteding-de.de.oracle.com", "8757", false, Password.read(PASSWORD), 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link DirectoryResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link DirectoryResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static DirectoryResource intranet()
    throws TaskException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link DirectoryResource} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link DirectoryResource} configured for
   **                            extranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static DirectoryResource extranet()
    throws TaskException {

    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internet
  /**
   ** Factory method to create a new {@link DirectoryResource} if the test case
   ** is executed from internet connectivity.
   **
   ** @return                    the {@link DirectoryResource} configured for
   **                            internet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static DirectoryResource internet()
    throws TaskException {

    return endpoint(INTERNET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DirectoryResource} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DirectoryResource} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private static DirectoryResource endpoint(final String serverName)
    throws TaskException {

    return new DirectoryResource(CONSOLE, serverName, "7389", "dc=bka,dc=bund,dc=de", "uid=oimadmin,cn=System,dc=bka,dc=bund,dc=de", "Sophie20061990$", false, false, "en", "US", "GMT+01:00", "eeeee");
  }
}