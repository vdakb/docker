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
    Subsystem   :   Atlassian Jira Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    Client.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-19-08  SBernet     First release version
*/

package oracle.iam.identity.icf.connector.plp;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.DirectoryEndpoint;
import oracle.iam.identity.icf.connector.DirectorySchema;
import oracle.iam.identity.icf.foundation.AbstractEndpoint;
import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.logging.SystemConsole;

import org.identityconnectors.common.security.GuardedString;
////////////////////////////////////////////////////////////////////////////////
// class Client
// ~~~~~ ~~~~~~
/**
 ** The <code>Client</code> implements the environment functionality for a
 ** Test Case.
 **
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Client {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE  = new SystemConsole("gws");
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String               INTRANET = "localhost";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Client} if the test case is
   ** executed from intranet connectivity.
   **
   ** @return                    the {@link DirectorySchema} configured for
   **                            intranet.
   */
  protected static DirectorySchema intranet()
    throws SystemException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DirectorySchema} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DirectorySchema} configured for
   **                            specified network access.
   */
  private static DirectorySchema endpoint(String serverName)
    throws SystemException {
    
    final AbstractEndpoint.Server    server    = new AbstractEndpoint.Server(serverName, 8759);

    final DirectoryEndpoint endpoint  = DirectoryEndpoint.build(CONSOLE).primary(server)
                                      .primaryHost("oig.silverid.fr")
                                      .primaryPort(3389)
                                      .principalUsername("cn=Directory Manager")
                                      .principalPassword(new GuardedString("WelcomeOra2018".toCharArray()))
                                      .localeCountry("EN")
                                      .rootContext("ou=P20,dc=example,dc=com")
                                      .accountClass(CollectionUtility.set("federatedIdentity"))
                                      .groupClass(CollectionUtility.set("groupOfUniqueNames"));
                                      
    System.out.println("Build");
    return DirectorySchema.build(endpoint);
  }
}