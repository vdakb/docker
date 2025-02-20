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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Unit Testing
    Subsystem   :   Notification

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package bka.iam.identity.notification;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.identity.foundation.rmi.IdentityServerFeature;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;

////////////////////////////////////////////////////////////////////////////////
// abstract class Network
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The <code>Network</code> implements the environment functionality for a
 ** Test Case.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Network {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE  = new SystemConsole("oig");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** intranet network.
   */
  static final String               INTRANET = "buster.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** extranet network
   */
  static final String               EXTRANET = "www.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** intranet network.
   */
  static final String               INTERNET = "www.cinnamonstar.dev";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Network</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Network() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from intranet connectivity.
   **
   ** @return                    the {@link IdentityServer} configured for
   **                            internet network.
   **
   ** @throws TaskException      if the server type isn't supported.
   */
  protected static IdentityServer intranet()
    throws TaskException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from extranet connectivity.
   **
   ** @return                    the {@link IdentityServer} configured for
   **                            extranet network.
   **
   ** @throws TaskException      if the server type isn't supported.
   */
  protected static IdentityServer extranet()
    throws TaskException {

    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internet
  /**
   ** Factory method to create a new {@link Context} if the test case is
   ** executed from internet connectivity.
   **
   ** @return                    the {@link IdentityServer} configured for
   **                            internet network.
   **
   ** @throws TaskException      if the server type isn't supported.
   */
  protected static IdentityServer internet()
    throws TaskException {

    return endpoint(INTERNET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link IdentityServer} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link IdentityServer} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if the server type isn't supported.
   */
  private static IdentityServer endpoint(final String serverName)
    throws TaskException {

    return IdentityServer.build(
       CONSOLE
     , IdentityServerResource.build(
         CONSOLE
       , serverName
       , "8005"
       , "weblogic"
       , "/Users/dsteding/Project/Bundeskriminalamt12c/ExternalIdentityManagement/IdentityTesting/bkaNotification/src/main/resources/config/authwl.conf"
       , "oim"
       , "weblogic"
       , "Sophie20061990$"
       , "xelsysadm"
       , "Sophie20061990$"
       , false
       , "en"
       , "US"
       , "GMT+01:00"
       , "unused"
       )  
     , IdentityServerFeature.build(CONSOLE)
    );
  }
}