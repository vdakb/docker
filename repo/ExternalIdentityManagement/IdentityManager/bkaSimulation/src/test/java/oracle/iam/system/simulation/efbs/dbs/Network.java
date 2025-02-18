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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.dbs;

import oracle.iam.system.simulation.dbs.DatabaseResource;

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

  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String        INTRANET = "buster.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Extranet
   */
  static final String        EXTRANET = "www.cinnamonstar.net";

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link DatabaseResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            intranet.
   */
  protected static DatabaseResource intranet() {
    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link DatabaseResource} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            extranet.
   */
  protected static DatabaseResource extranet() {
    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DatabaseResource} for the specified
   ** network connectivity.
   **
   ** @param  serverName         teh name of the DNS name to access.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            specified network access.
   */
  private static DatabaseResource endpoint(final String serverName) {
    return DatabaseResource.build(serverName, 7021, "mdr.cinnamonstar.net", "igd_fbs", "Sophie20061990$");
  }
}