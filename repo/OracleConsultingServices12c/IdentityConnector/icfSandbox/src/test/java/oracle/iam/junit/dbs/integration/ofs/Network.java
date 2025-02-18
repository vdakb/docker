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
    Subsystem   :   Openfire Database Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the enum
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import java.io.File;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

import oracle.iam.identity.dbs.integration.openfire.DatabaseFeature;
import oracle.iam.identity.dbs.integration.openfire.DatabaseResource;

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
  static final SystemConsole CONSOLE        = new SystemConsole("ofs");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** local network.
   */
  static final String        LOCALNET       = "Dieters-MacBook-Pro.local";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String        INTRANET       = "hardy.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String        EXTRANET       = "hardy.vm.oracle.com";
  /**
   ** The location of the credential file to use
   */
  static final String        PASSWORD       = System.getProperty(Password.class.getName().toLowerCase());
  /**
   ** The location of the Connector Server credential file to use
   */
  static final String        SECRET         = System.getProperty("oracle.iam.identity.secret");

  static final File          FEATURE        = new File("./src/test/resources/mds/ofs-dbms-feature.xml");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Returns the {@link ConnectorFacade} based on configuration map passed.
   **
   ** @param  resource           the {@link DatabaseResource} containing the
   **                            properties to establish the connection to a
   **                            target system.
   **                            <br>
   **                            Allowed object is {@link TargetResource}.
   */
  protected static ConnectorFacade facade(DatabaseResource resource)
    throws TaskException {

    return BundleLocator.create(server(), resource, DatabaseFeature.build(CONSOLE, FEATURE));
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

    return ServerResource.build(CONSOLE, LOCALNET, "8757", false, Password.read(SECRET), Integer.MAX_VALUE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link DatabaseResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static DatabaseResource intranet()
    throws TaskException {

    return endpoint(INTRANET, "1521");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link DatabaseResource} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            extranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static DatabaseResource extranet()
    throws TaskException {

    return endpoint(EXTRANET, "1521");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DatabaseResource} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private static DatabaseResource endpoint(final String serverName, final String serverPort)
    throws TaskException {

    return DatabaseResource.build(CONSOLE, serverName, serverPort, "oracle.jdbc.OracleDriver", "mdr.vm.oracle.com", "ofsowner", "iamuser", Password.read(PASSWORD), false, "en", "US", "GMT+01:00", "eeeee");
  }
}