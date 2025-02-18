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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   IdentityManager.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityManager.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.Password;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.identity.foundation.rmi.IdentityServerFeature;
import oracle.iam.identity.foundation.rmi.IdentityServerResource;
import oracle.iam.identity.foundation.rmi.IdentityServerConstant;

////////////////////////////////////////////////////////////////////////////////
// class IdentityManager
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>IdentityManager</code> implements the environment functionality
 ** for a Test Case regarding Identity Manager API.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class IdentityManager {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  static final SystemConsole CONSOLE  = new SystemConsole("oim");
  /**
   ** The configuration file extending the standard <code>IT Resource</code>
   ** configuration.
   */
  static final String        INTRANET = "buster.vm.oracle.com";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String        EXTRANET = "www.cinnamonstar.net";
  /**
   ** The DNS name of the server if the Test Cases are executed inside of the
   ** Intranet
   */
  static final String        INTERNET = "www.cinnamonstar.dev";
  /**
   ** The location of the credential file to use
   */
  static final String        PASSWORD = System.getProperty(Password.class.getName().toLowerCase());

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link IdentityServerResource} if the test
   ** case is executed from intranet connectivity.
   **
   ** @return                    the {@link IdentityServerResource} configured
   **                            for intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static IdentityServerResource intranet()
    throws TaskException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link IdentityServerResource} if the test
   ** case is executed from extranet connectivity.
   **
   ** @return                    the {@link IdentityServerResource} configured
   **                            for extranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static IdentityServerResource extranet()
    throws TaskException {

    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   internet
  /**
   ** Factory method to create a new {@link IdentityServerResource} if the test
   ** case is executed from internet connectivity.
   **
   ** @return                    the {@link IdentityServerResource} configured
   **                            for internet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static IdentityServerResource internet()
    throws TaskException {

    return endpoint(INTERNET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  protected static IdentityServer server(final IdentityServerResource resource)
    throws TaskException {

    return IdentityServer.build(CONSOLE, resource, IdentityServerFeature.build(CONSOLE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link IdentityManagerResource} for the
   ** specified network connectivity.
   **
   ** @param  serviceDomain      the host name or IP address of the target
   **                            system on which the Service Provider is
   **                            deployed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link PivotalResource} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private static IdentityServerResource endpoint(final String serverName)
    throws TaskException {

    return IdentityServerResource.build(CONSOLE, serverName, "8005", IdentityServerConstant.SERVER_TYPE_WEBLOGIC, "D:/Oracle/product/oim/12.2.1.3/config/authwl.conf", "oim", "weblogic", Password.read(PASSWORD), "xelsysadm", Password.read(PASSWORD), false, "en", "US", "GMT+01:00", "eeeee");
  }
}