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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Fixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Fixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.pcf;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

public class Fixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final File      PROVISIONING   = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/pcf-account-provisioning.xml");
  static final File      RECONCILIATION = new File("D:/Project/OracleConsultingServices12c/IdentityConnector/icfSandbox/src/test/resources/mds/pcf-account-reconciliation.xml");

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static Descriptor      provisioning;
  static Descriptor      reconciliation;
  static ConnectorFacade facade;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Fixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Fixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeClass
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>@BeforeClass</code> causes it to be run
   ** once before any of the test methods in the class.
   */
  @BeforeClass
  public static void beforeClass() {
    try {
      provisioning   = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), PROVISIONING);
      reconciliation = DescriptorFactory.configure(Descriptor.buildProvisioning(Network.CONSOLE), RECONCILIATION);
      facade         = Network.facade(Network.intranet());
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Tests need to share computationally expensive setup.
   ** <br>
   ** While this can compromise the independence of tests, it is a necessary
   ** optimization.
   ** <p>
   ** Annotating this method with <code>@Before</code> causes it to be run once
   ** before any of the test methods in the class.
   */
  @Before
  public void before() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link ConnectorException} thrown from the
   **                            <code>Connector Bundle</code>.
   */
  static void failed(final ConnectorException cause) {
    // the exception thrown provides a special format in the message
    // containing a error code and a detailed text
    // this message needs to be split by a "::" character sequence
    final String   message = cause.getLocalizedMessage();
    final String[] parts   = message.split("::");
    if (parts.length > 1)
      failed(parts[0].concat("::").concat(parts[1]));
    else
      failed(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** The exception captured provides a special format in the message containing
   ** a error code and a detailed text this message needs to be plitted by a
   ** "::" character sequence
   **
   ** @param  cause              the {@link TaskException} thrown from the
   **                            <code>Integration Layer</code>.
   */
  static void failed(final TaskException e) {
    failed(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failed
  /**
   ** Fails a test with the given message.
   **
   ** @param  message            the identifying message for the
   **                            {@link AssertionError}.
   **                            <br>
   **                            May be <code>nulld</code>.
   */
  static void failed(final String message) {
    Assert.fail(message);
  }
}