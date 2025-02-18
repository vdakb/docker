/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Service

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package oracle.iam.identity.service;

import oracle.iam.identity.service.api.Client;

import oracle.hst.platform.core.logging.SystemConsole;

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
  public static final SystemConsole CONSOLE  = new SystemConsole("igd");
  /**
   ** The provider URL to the server if the Test Cases are executed inside of
   ** the Intranet
   */
  static final String               INTRANET = "t3://buster.vm.oracle.com:8005";
  /**
   ** The provider URL to the server if the Test Cases are executed inside of
   ** the Extranet
   */
  static final String               EXTRANET = "t3://www.cinnamonstar.net:8005";
  /**
   ** The provider URL to the server if the Test Cases are executed inside of
   ** the Internet
   */
  static final String               INTERNET = "t3://www.cinnamonstar.dev:8005";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link Endpoint} if the test case is
   ** executed inside of the intranet connectivity.
   **
   ** @return                    the {@link Client} configured for
   **                            specified network access.
   **                            <br>
   **                            Possible object is {@link Client}.
   */
  public static Client intranet() {
    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link Endpoint} if the test case is
   ** executed inside of the extranet connectivity.
   **
   ** @return                    the {@link Client} configured for
   **                            specified network access.
   **                            <br>
   **                            Possible object is {@link Client}.
   */
  public static Client extranet() {
    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internet
  /**
   ** Factory method to create a new {@link Endpoint} if the test case is
   ** executed inside of the internet connectivity.
   **
   ** @return                    the {@link Client} configured for
   **                            specified network access.
   **                            <br>
   **                            Possible object is {@link Client}.
   */
  public static Client internet() {
    return endpoint(INTERNET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link Client} for the specified
   ** network connectivity.
   **
   ** @param  providerURL        the provider URL to the server.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Client} configured for
   **                            specified network access.
   **                            <br>
   **                            Possible object is {@link Client}.
   */
  private static Client endpoint(final String providerURL) {
    // fake system property configuration
    System.setProperty(Client.PROVIDER_URL, providerURL);
    return Client.build();
  }
}
