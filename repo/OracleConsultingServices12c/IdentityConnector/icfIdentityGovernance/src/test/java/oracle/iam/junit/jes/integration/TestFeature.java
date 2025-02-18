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
    Subsystem   :   Identity Governance Service Provisioning

    File        :   TestFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration;

import oracle.hst.foundation.utility.ClassUtility;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.api.ConnectorKey;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.connector.oig.Main;

import oracle.iam.identity.jes.integration.oig.ServiceFeature;

////////////////////////////////////////////////////////////////////////////////
// class TestFeature
// ~~~~~ ~~~~~~~~~~~
/**
 ** The test case to obtain the extended <code>IT Resource</code> configuration
 ** for the target system leveraging the <code>Metadata Service</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestFeature extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestFeature</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestFeature() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {TestFeature.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnmarshal
  /**
   ** Test the fetching the extended <code>IT Resource</code> configuration.
   */
  @Test
  public void testUnmarshal() {
    try {
      final ServiceFeature feature = ServiceFeature.build(CONSOLE, FEATURE);
      assertNotNull(feature);
      CONSOLE.info(feature.toString());

      // properties below are excluded per default
      assertFalse(feature.containsKey(ServiceFeature.BUNDLE_ENTRY));
      assertFalse(feature.containsKey(ServiceFeature.BUNDLE_NAME));
      assertFalse(feature.containsKey(ServiceFeature.BUNDLE_VERSION));

      // validate the token to access the connector bundle
      final ConnectorKey token = feature.token();
      assertNotNull(token);
      assertEquals(token.getBundleName(),    "oig.identity.connector.bundle");
      assertEquals(token.getBundleVersion(), "12.2.1.3");
      assertEquals(token.getConnectorName(), ClassUtility.canonicalName(Main.class));
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}