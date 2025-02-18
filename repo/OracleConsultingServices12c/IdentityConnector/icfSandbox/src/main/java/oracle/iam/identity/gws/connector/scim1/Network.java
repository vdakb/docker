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
    Subsystem   :   Generic SCIM 1 Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.scim1;

import java.net.URI;
import java.net.URISyntaxException;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.SecurityContext;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.rest.ServiceClient;
import oracle.iam.identity.icf.rest.ServiceContext;
import oracle.iam.identity.icf.rest.ServiceEndpoint;

import oracle.iam.identity.icf.connector.scim.v1.Context;

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
   ** Intranet
   */
  static final String               INTRANET = "uaa.vm.pivotal.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "uaa.vm.pivotal.com";

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

    return endpoint(INTRANET);
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
   ** @return                    the {@link ServiceContext} configured for
   **                            specified network access.
   */
  private static Context endpoint(String serverName)
    throws SystemException {

    URI authorization = null;
    try {
      // for pcf-dev
      authorization = new URI("https://login.vm.pivotal.com/oauth/token");
    }
    catch (URISyntaxException e) {
      throw SystemException.unhandled(e);
    }
    // for uaa standalone
//    final AbstractEndpoint.Server    primary   = new AbstractEndpoint.Server(serverName, 8080);
//    final AbstractEndpoint.Principal client    = new AbstractEndpoint.Principal("client", null);
//    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("admin", new GuardedString("adminsecret".toCharArray()));
//    final ServiceEndpoint            endpoint  = ServiceEndpoint.build(CONSOLE, primary, "uaa", false, "application/json", "application/json", "credential", client, principal, null, authorization, "en", "US", "GMT+01:00");
    // for pcf-dev
    final AbstractEndpoint.Server    primary   = new AbstractEndpoint.Server(serverName, -1);
    final AbstractEndpoint.Principal client    = new AbstractEndpoint.Principal("cf", null);
    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("admin", new GuardedString("admin".toCharArray()));
    final ServiceEndpoint            endpoint  = ServiceEndpoint.build(CONSOLE, primary, null, true, "application/json", "application/json", "oauth-password", client, principal, null, authorization, "en", "US", "GMT+01:00");
    endpoint.trustedStoreType("JKS").trustedStoreFile("D:/Oracle/product/ics/12.2.1.3.0/java/security/trusted.jks").trustedStorePassword("Welcome1");
    endpoint.timeOutConnect(0).timeOutResponse(0);
    if (endpoint.secureSocket()) {
      System.setProperty(SecurityContext.TRUSTED_STORE_TYPE, endpoint.trustedStoreType());
      System.setProperty(SecurityContext.TRUSTED_STORE_FILE, endpoint.trustedStoreFile());
      System.setProperty(SecurityContext.TRUSTED_STORE_PASSWORD, endpoint.trustedStorePassword());
    }
    return Context.build(ServiceClient.build(endpoint));
  }
}