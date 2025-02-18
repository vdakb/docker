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
    Subsystem   :   Identity Governance Domain Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.igd;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.Password;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceEndpoint;

import oracle.iam.identity.icf.connector.scim.v2.Context;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("igd");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet.
   */
  static final String               INTRANET = "buster.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet.
   */
  static final String               EXTRANET = "www.cinnamonstar.net";
  /**
   ** The location of the credential file to use
   */
  static final String               PASSWORD = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from intranet connectivity.
   **
   ** @return                    the {@link Context} configured for intranet.
   */
  protected static Context intranet()
    throws SystemException {

    return Context.build(ServiceClient.build(endpoint(INTRANET)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from extranet connectivity.
   **
   ** @return                    the {@link Context} configured for extranet.
   */
  public static Context extranet()
    throws SystemException {

    return Context.build(ServiceClient.build(endpoint(EXTRANET)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link Context} for the specified network
   ** connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link Context} configured for specified
   **                            network access.
   */
  private static ServiceEndpoint endpoint(String serverName) {

    final AbstractEndpoint.Server    server    = new AbstractEndpoint.Server(serverName, 8005);
    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("xelsysadm", new GuardedString(Password.read(PASSWORD).toCharArray()));
    final ServiceEndpoint            endpoint  = ServiceEndpoint.build(CONSOLE);
    endpoint.authenticationScheme("non-preemptive")
      .typeContent("application/json")
      .typeAccept("application/scim+json")
      .primary(server)
      .rootContext("/iam/governance/scim/v1")
      .secureSocket(false)
      .principal(principal)
      .localeLanguage("en")
      .localeCountry("US")
      .localeTimeZone("+01:00")
      .timeOutConnect(0)
      .timeOutResponse(0);
    return endpoint;
  }
}