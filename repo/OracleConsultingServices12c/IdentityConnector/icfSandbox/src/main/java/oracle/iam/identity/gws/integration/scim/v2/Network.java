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
    Subsystem   :   Generic SCIM 2 Connector

    File        :   Network.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Network.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.scim.v2;

import java.util.Map;
import java.util.HashMap;

import java.io.File;

import org.identityconnectors.framework.common.objects.Uid;

import org.identityconnectors.framework.api.ConnectorFacade;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.Password;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.gws.integration.ServiceFeature;
import oracle.iam.identity.gws.integration.ServiceResource;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

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
  static final SystemConsole CONSOLE  = new SystemConsole("scim2");
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
   ** The location of the credential file to use
   */
  static final String        PASSWORD = System.getProperty(Password.class.getName().toLowerCase());

  static final File          FEATURE  = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfRESTFul/src/main/resources/mds/scim2-feature.xml");

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Entry
  // ~~~~~ ~~~~~
  public static abstract class Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Map<String, Object> create = new HashMap<String, Object>();
    final Map<String, Object> update = new HashMap<String, Object>();

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create
    public abstract Map<String, Object> create();
  }


  //////////////////////////////////////////////////////////////////////////////
  // class Zitterbacke
  // ~~~~~ ~~~~~~~~~~~
  public static class ZitterBacke extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Uid UID = new Uid("5a6137f4-aaac-4d52-aa2b-9b78cf0e3094");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("User ID",      "azitterbacke");
        this.create.put("Last Name",    "Zitterbacke");
        this.create.put("First Name",   "Alfons");
        this.create.put("Type",         "EMP");
        this.create.put("Country",      "DE");
        this.create.put("Language",     "de");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterMann
  // ~~~~~ ~~~~~~~~~~
  public static class MusterMann extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Uid UID    = new Uid("4627fac2-ed43-4928-a1f5-4f6bdd25f9d0");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("User ID",      "amustermann");
        this.create.put("Last Name",    "Mustermann");
        this.create.put("First Name",   "Alfred");
        this.create.put("Type",         "EMP");
        this.create.put("Country",      "DE");
        this.create.put("Language",     "de");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class MusterFrau
  // ~~~~~ ~~~~~~~~~~
  public static class MusterFrau extends Entry {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Uid UID = new Uid("05027f17-d5b7-435d-9684-a4c771274a84");

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: create (Entry)
    @Override
    public Map<String, Object> create() {
      if (this.create.size() == 0) {
        this.create.put("User ID",      "amusterfrau");
        this.create.put("Last Name",    "Musterfrau");
        this.create.put("First Name",   "Agathe");
        this.create.put("Type",         "EMP");
        this.create.put("Country",      "DE");
        this.create.put("Language",     "de");
      }
      return this.create;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  protected static ConnectorFacade facade(final ServiceResource resource)
    throws TaskException {

    return BundleLocator.create(server(), resource, ServiceFeature.build(CONSOLE, FEATURE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   server
  /**
   ** Factory method to create a new {@link ServerResource}.
   ** <br>
   ** The {@link ServerResource} is always running on the same host system.
   **
   ** @return                    the {@link ServerResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if the <code>IT Resource</code> is not defined
   **                            in the Identity Manager meta entries for the
   **                            given name or one or more attributes are
   **                            missing on the <code>IT Resource</code>
   **                            instance.
   */
  protected static ServerResource server()
    throws TaskException {

    return ServerResource.build(CONSOLE, "dsteding-de.de.oracle.com", "8757", false, Password.read(PASSWORD), 20);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            intranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static ServiceResource intranet()
    throws TaskException {

    return endpoint(INTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   extranet
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from extranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            extranet.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  protected static ServiceResource extranet()
    throws TaskException {

    return endpoint(EXTRANET);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endpoint
  /**
   ** Factory method to create a new {@link DatabaseResource} for the specified
   ** network connectivity.
   **
   ** @param  serverName         the name of the DNS name to access.
   **
   ** @return                    the {@link DatabaseResource} configured for
   **                            specified network access.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  private static ServiceResource endpoint(final String serverName)
    throws TaskException {

    return ServiceResource.build(CONSOLE, serverName, "8011", "/scim/v2", false, "application/scim+json", "application/scim+json", "none", "", "", "", "", "", "", "", "en", "US", "GMT+01:00", "eeeee");
  }
}