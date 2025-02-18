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

    Copyright 2022 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager UnitTest Library
    Subsystem   :   Identity Services Integration

    File        :   Network.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Network.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      12.07.2022  Dsteding    First release version
*/

package bka.iam.identity.event.pid;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import bka.iam.identity.rest.ServiceResource;

import bka.iam.identity.pid.Context;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("pid");
 /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "dieters-macbook-pro.fritz.box";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "dieters-macbook-pro.local";


  //////////////////////////////////////////////////////////////////////////////
  // Method:   loginContext
  /**
   ** Factory method to create a new {@link ScimContext} context if the test
   ** case is executed from extranet connectivity.
   **
   ** @return                    the {@link ScimContext} context configured
   **                            for extranet.
   **
   ** @throws TaskException      if the authentication/authorization process
   **                            fails.
   */
  public static Context context(final ServiceResource endpoint)
    throws TaskException {

    return Context.build(CONSOLE, endpoint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case is
   ** executed inside of the intranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            specified network access.
   */
  protected static ServiceResource intranet()
    throws TaskException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case is
   ** executed inside of the extranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            specified network access.
   */
  protected static ServiceResource extranet()
    throws TaskException {

    return endpoint(EXTRANET);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link ServiceResource} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            specified network access.
   */
  private static ServiceResource endpoint(String serverName)
    throws TaskException {

    return ServiceResource.build(CONSOLE, serverName, "8081", "/igs/pid/v1", false, "application/json", "application/json", "oauth-password", "http://laurel.cinnamonstar.net:8009/oauth2/rest/token", "651d559adef2426fa5534f33058bd2ac", "Welcome1", "igssysadm", "Welcome1", "igssysadm", "Welcome1", "de", "DE", "GMT+01:00", "unused");
  }
}