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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Generic Database Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbms;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.junit.Password;

import oracle.iam.identity.icf.dbms.DatabaseEndpoint;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("dbs");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "hardy.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String               EXTRANET = "hardy.vm.oracle.com";
  /**
   ** The location of the credential file to use
   */
  static final String               PASSWORD = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link DatabaseEndpoint} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link DatabaseEndpoint} configured for
   **                            intranet.
   */
  protected static DatabaseEndpoint intranet() {
    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link DatabaseEndpoint} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link DatabaseEndpoint} configured for
   **                            extranet.
   */
  public static DatabaseEndpoint extranet() {
    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DatabaseEndpoint} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DatabaseContext} configured for
   **                            specified network access.
   */
  private static DatabaseEndpoint endpoint(String serverName) {
    DatabaseEndpoint.Driver    driver    = DatabaseEndpoint.Driver.from("oracle.jdbc.OracleDriver");
    AbstractEndpoint.Server    primary   = new AbstractEndpoint.Server(serverName, 1521);
    AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal("ofsowner", new GuardedString(Password.read(PASSWORD).toCharArray()));
    return DatabaseEndpoint.build(CONSOLE, driver, primary, "mdr.vm.oracle.com", principal, false, "en", "US", "GMT+01:00");
  }
}