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

package oracle.iam.junit.gds.connector;

import org.identityconnectors.common.security.GuardedString;

import oracle.iam.identity.icf.foundation.AbstractEndpoint;

import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import oracle.iam.identity.icf.connector.DirectoryEndpoint;
import oracle.iam.identity.icf.connector.DirectoryException;

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
  static final String               INTERNET   = "www.cinnamonstar.dev";
  /**
   ** The location of the credential file to use
   */
  static final String               PASSWORD   = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link DirectoryEndpoint} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link DirectoryEndpoint} configured for
   **                            intranet.
   */
  protected static DirectoryEndpoint intranet() {
    final AbstractEndpoint.Server primary = new AbstractEndpoint.Server(INTRANET, 7389);
    return endpoint(primary, "dc=bka,dc=bund,dc=de", "cn=Directory Manager");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link DirectoryEndpoint} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link DirectoryEndpoint} configured for
   **                            extranet.
   */
  protected static DirectoryEndpoint extranet() {
    final AbstractEndpoint.Server primary = new AbstractEndpoint.Server(EXTRANET, 7389);
    return endpoint(primary, "dc=bka,dc=bund,dc=de", "cn=Directory Manager");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internet
  /**
   ** Factory method to create a new {@link DirectoryEndpoint} if the test case
   ** is executed from internet connectivity.
   **
   ** @return                    the {@link DirectoryEndpoint} configured for
   **                            internet.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  protected static DirectoryEndpoint internet() {
    final AbstractEndpoint.Server primary = new AbstractEndpoint.Server(INTERNET, 7636);
    return endpoint(primary, "dc=vm,dc=oracle,dc=com", "uid=oimadmin,cn=System,dc=vm,dc=oracle,dc=com");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DirectoryEndpoint} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DirectoryEndpoint} configured for
   **                            specified network access.
   **
   ** @throws DirectoryException if a syntax violation is detected.
   */
  private static DirectoryEndpoint endpoint(final AbstractEndpoint.Server primary, final String suffix, final String principalName) {
    final AbstractEndpoint.Principal principal = new AbstractEndpoint.Principal(principalName, new GuardedString(Password.read(PASSWORD).toCharArray()));
    return DirectoryEndpoint.build(CONSOLE, primary, suffix, principal, (primary.port() == 7636), "en", "US", "GMT+01:00");
  }
}