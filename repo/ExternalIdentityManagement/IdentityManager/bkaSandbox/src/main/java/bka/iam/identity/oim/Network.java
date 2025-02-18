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
    Subsystem   :   Generic SCIM Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package bka.iam.identity.oim;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.identity.foundation.rmi.IdentityServerFeature;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;

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
public class Network {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE  = new SystemConsole("oim");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "buster.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "www.cinnamonstar.net";

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
  public static IdentityServer intranet()
    throws TaskException {

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
  public static IdentityServer extranet()
    throws TaskException {

    return endpoint(EXTRANET);
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
   ** @param  serverName         teh name of the DNS name to access.
   **
   ** @return                    the {@link Context} configured for specified
   **                            network access.
   */
  private static IdentityServer endpoint(String serverName)
    throws TaskException {

    IdentityServerFeature  feature  = new IdentityServerFeature(CONSOLE);
    IdentityServerResource resource = new IdentityServerResource(CONSOLE, serverName, "8005", "weblogic", "D:/Oracle/product/oim/12.2.1.3/config/authwl.conf", "oim", "weblogic", "Sophie20061990$", "xelsysadm", "Sophie20061990$", false, "en", "US", "+01:00", "notused");
    resource.name("internal");
    return new IdentityServer(CONSOLE, resource, feature);
  }
}