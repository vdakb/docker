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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Service SCIM

    File        :   TestBaseIntegration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestBaseIntegration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration;

import java.io.File;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.BundleLocator;
import oracle.iam.identity.connector.integration.ServerResource;

import oracle.iam.identity.gws.integration.igs.ServiceFeature;
import oracle.iam.identity.gws.integration.igs.ServiceResource;

import oracle.iam.junit.igs.TestBase;

////////////////////////////////////////////////////////////////////////////////
// class TestBaseIntegration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestBaseIntegration</code> implements the basic functionality of a
 ** Integration Test Cases.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestBaseIntegration extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  public static final SystemConsole CONSOLE  = new SystemConsole("gws");
  /**
   ** The location of the configuration file the standard
   ** <code>IT Resource</code> is populated from if the Test Cases are executed
   ** inside of the Intranet.
   */
  public static final String        INTRANET = "igd/intranet.conf";
  /**
   ** The location of the configuration file the standard
   ** <code>IT Resource</code> is populated from if the Test Cases are executed
   ** inside of the Extranet.
   */
  public static final String        EXTRANET = "igd/extranet.conf";
  /**
   ** The location of the connector server configuration in an extranet network.
   */
  public static final String        SERVER   = "igd/server.conf";
  /**
   ** The location of the credential file to use
   */
  public static final File          FEATURE  = new File("src/test/resources/mds/igs-feature.xml");

  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the size of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_SIZE             = "batchSize";
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the start index of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_START            = "batchStart";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search base(s) of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_BASE            = "searchBase";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search filter of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_FILTER          = "searchFilter";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_ORDER           = "searchOrder";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to obtain entries from the Service Provider incrementally means based
   ** on a synchronization token like <code>changeNumber</code> of timestamps.
   */
  public static final String INCREMENTAL            = "incremental";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization strategy of
   ** resources returned from a <code>Service Provider</code>.
   */
  public static final String SYNCHRONIZE            = "synchronize";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to specifiy the value of a synchronization token.
   */
  public static final String SYNCHRONIZE_TOKEN      = "synchronizeToken";
  /**
   ** The default start inedx of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  public static final int    BATCH_START_DEFAULT    = 1;
  /**
   ** The default size limit of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  public static final int    BATCH_SIZE_DEFAULT     = 500;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestBaseIntegration</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestBaseIntegration() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   facade
  /**
   ** Factory method to create a new {@link ConnectorFacade}.
   **
   ** @return                    the {@link ConnectorFacade} to interact with
   **                            the connector bundles.
   **
   ** @throws TaskException      if the connector bundle is not availbale.
   */
  public static ConnectorFacade facade(final ServiceResource resource)
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
   ** @throws TaskException      if  one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServerResource server()
    throws TaskException {

    return ServerResource.build(CONSOLE, config(SERVER));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Factory method to create a new {@link ServiceResource} if the test case
   ** is executed from intranet connectivity.
   **
   ** @return                    the {@link ServiceResource} configured for
   **                            intranet.
   **                            <br>
   **                            Possible objec is {@link ServiceResource}.
   **
   ** @throws TaskException      if one or more attributes are missing on the
   **                            <code>IT Resource</code> instance.
   */
  public static ServiceResource service()
    throws TaskException {

    return ServiceResource.build(CONSOLE, config(INTRANET));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector</code>.
   **                            <br>
   **                            Allowed object is {@link ConnectorException}.
   */
  public static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    failed(cause.getClass().getSimpleName().concat(cause.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a error code and a detailed message.
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Integration</code>.
   **                            <br>
   **                            Allowed object is {@link TaskException}.
   */
  public static void failed(final TaskException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    failed(cause.getClass().getSimpleName().concat("::").concat(cause.code()).concat("::").concat(cause.getLocalizedMessage()));
  }
}