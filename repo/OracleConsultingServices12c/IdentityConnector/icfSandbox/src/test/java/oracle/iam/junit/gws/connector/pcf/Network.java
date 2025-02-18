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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.connector.pcf;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.connector.pcf.Service;
import oracle.iam.identity.icf.connector.pcf.Endpoint;
import oracle.iam.identity.icf.connector.pcf.ScimContext;
import oracle.iam.identity.icf.connector.pcf.RestContext;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("pcf");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "vm.pivotal.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "sys.w0.psp.bka.bund.de";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginContext
  /**
   ** Factory method to create a new {@link ScimContext} context if the test
   ** case is executed from extranet connectivity.
   **
   ** @return                    the {@link ScimContext} context configured
   **                            for extranet.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  public static ScimContext loginContext(final Endpoint endpoint)
    throws SystemException {

    return ScimContext.build(Service.build(endpoint));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cloudContext
  /**
   ** Factory method to create a new {@link RestContext} context if the test
   ** case is executed from intranet connectivity.
   **
   ** @return                    the {@link RestContext} context configured
   **                            for intranet.
   **
   ** @throws SystemException    if the authentication/authorization process
   **                            fails.
   */
  protected static RestContext cloudContext(final Endpoint endpoint)
    throws SystemException {

    return RestContext.build(Service.build(endpoint));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Endpoint} if the test case is
   ** executed inside of the intranet connectivity.
   **
   ** @return                    the {@link Endpoint} configured for
   **                            specified network access.
   */
  protected static Endpoint intranet() {
      return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link Endpoint} if the test case is
   ** executed inside of the extranet connectivity.
   **
   ** @return                    the {@link Endpoint} configured for
   **                            specified network access.
   */
  protected static Endpoint extranet() {
      return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link ServiceContext} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link Endpoint} configured for
   **                            specified network access.
   */
  private static Endpoint endpoint(String serverName) {
    final AbstractEndpoint.Server    primary   = new AbstractEndpoint.Server(serverName, -1);
    final AbstractEndpoint.Principal client    = new AbstractEndpoint.Principal("cf", null);
    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("svc-oim-prod-w0", new GuardedString("NU2MjdGDN3WSo08RcRVhdPFtrHm45X".toCharArray()));
    final Endpoint                   endpoint  = Endpoint.build(CONSOLE, primary, true, "application/json", "application/json", "oauth-password", client, principal, null, "en", "US", "GMT+01:00");
    endpoint.timeOutConnect(15000).timeOutResponse(100000);
    return endpoint;
  }
}