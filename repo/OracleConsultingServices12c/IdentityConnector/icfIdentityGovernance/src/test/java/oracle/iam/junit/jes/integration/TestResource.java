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

    File        :   TestResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration;

import java.util.Set;

import oracle.hst.foundation.utility.ClassUtility;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.api.ConnectorKey;
import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.api.operations.TestApiOp;
import org.identityconnectors.framework.api.operations.SchemaApiOp;
import org.identityconnectors.framework.api.operations.SearchApiOp;
import org.identityconnectors.framework.api.operations.CreateApiOp;
import org.identityconnectors.framework.api.operations.UpdateApiOp;
import org.identityconnectors.framework.api.operations.DeleteApiOp;
import org.identityconnectors.framework.api.operations.APIOperation;
import org.identityconnectors.framework.api.operations.ResolveUsernameApiOp;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.jes.integration.oig.ServiceFeature;
import oracle.iam.identity.jes.integration.oig.ServiceResource;

import oracle.iam.identity.icf.connector.oig.Main;

////////////////////////////////////////////////////////////////////////////////
// class TestResource
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The test case to obtain the <code>IT Resource</code> configuration for the
 ** target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestResource extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestResource</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestResource() {
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
    final String[] parameter = {TestResource.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnmarshalResource
  /**
   ** Test the fetching the standard <code>IT Resource</code> configuration.
   */
  @Test
  public void testUnmarshalResource() {
    try {
      final ServiceResource resource = ServiceResource.build(CONSOLE, config(ENDPOINT));
      assertNotNull(resource);
      CONSOLE.info(resource.toString());

      assertTrue(resource.containsKey(ServiceResource.SERVER_HOST));
      assertTrue(resource.containsKey(ServiceResource.SERVER_PORT));
      assertTrue(resource.containsKey(ServiceResource.SERVER_NAME));
      assertTrue(resource.containsKey(ServiceResource.ROOT_CONTEXT));

//      assertEquals(resource.stringValue(ServiceResource.SERVER_HOST),  "buster.vm.oracle.com");
      assertEquals(resource.stringValue(ServiceResource.SERVER_PORT),  "8005");
      assertEquals(resource.stringValue(ServiceResource.SERVER_NAME),  "oim");
      assertEquals(resource.stringValue(ServiceResource.ROOT_CONTEXT), "/oim");
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testUnmarshalFeature
  /**
   ** Test the fetching the extended <code>IT Resource</code> configuration.
   */
  @Test
  public void testUnmarshalFeature() {
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testConnectorBundle
  /**
   ** Confirms that a Java Connector Server is reachable.
   **
   ** @throws Exception          if an error occurs.
   */
  @Test
  public void testConnectorBundle()
    throws Exception {

    try {
      final ConnectorFacade facade = facade(endpoint());
      assertNotNull(facade);

      final Set<Class<? extends APIOperation>> supported = facade.getSupportedOperations();
      assertNotNull(supported);
      assertTrue(supported.contains(TestApiOp.class));
      assertTrue(supported.contains(SchemaApiOp.class));
      assertTrue(supported.contains(SearchApiOp.class));
      assertTrue(supported.contains(CreateApiOp.class));
      assertTrue(supported.contains(UpdateApiOp.class));
      assertTrue(supported.contains(DeleteApiOp.class));
      assertTrue(supported.contains(ResolveUsernameApiOp.class));
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}