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
    Subsystem   :   Apache Archiva Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.Password;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceEndpoint;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("gws");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** local network
   */
  static final String               LOCALNET = "www.cinnamonstar.dev";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "www.cinnamonstar.dev";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "www.cinnamonstar.dev";
  /**
   ** The location of the credential file to use
   */
  static final String               PASSWORD = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localnet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from local network connectivity.
   **
   ** @return                    the {@link DatabaseContext} configured for
   **                            local network.
   */
  protected static Context localnet()
    throws SystemException {

    return endpoint(LOCALNET, 8443);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from intranet connectivity.
   **
   ** @return                    the {@link DatabaseContext} configured for
   **                            intranet.
   */
  protected static Context intranet()
    throws SystemException {

    return endpoint(INTRANET, 8443);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from intranet connectivity.
   **
   ** @return                    the {@link DatabaseContext} configured for
   **                            intranet.
   */
  protected static Context extranet()
    throws SystemException {

    return endpoint(EXTRANET, 8443);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link ServiceContext} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link ServiceContext} configured for
   **                            specified network access.
   */
  private static Context endpoint(String serverName, final int port)
    throws SystemException {

    final AbstractEndpoint.Server    server    = new AbstractEndpoint.Server(serverName, port);
    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("admin", new GuardedString(Password.read(PASSWORD)));
    final ServiceEndpoint            endpoint  = ServiceEndpoint.build(CONSOLE);
    endpoint.authenticationScheme("basic-preemptive")
      .typeContent("application/json")
      .typeAccept("application/json")
      .primary(server)
      .rootContext("/restServices/redbackServices")
      .secureSocket(true)
      .principal(principal)
      .localeLanguage("en")
      .localeCountry("US")
      .localeTimeZone("+01:00")
      .timeOutConnect(0)
      .timeOutResponse(0);
    return Context.build(ServiceClient.build(endpoint));
  }
}