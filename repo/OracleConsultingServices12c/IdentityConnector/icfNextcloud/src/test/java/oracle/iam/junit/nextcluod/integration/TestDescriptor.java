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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Nextcloud Connector

    File        :   TestDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    TestDescriptor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.nextcloud.integration;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.api.ConnectorKey;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.identity.gws.integration.nextcloud.Service;
import oracle.iam.identity.gws.integration.nextcloud.ServiceFeature;

import oracle.iam.identity.icf.connector.nextcloud.Main;

////////////////////////////////////////////////////////////////////////////////
// class TestDescriptor
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to parse the provisioning and reconciliation descriptors.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestDescriptor extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestDescriptor</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestDescriptor() {
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
    final String[] parameter = {TestDescriptor.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   serverFeature
  /**
   ** Test the fetching the extended <code>IT Resource</code> configuration.
   */
  @Test
  public void serverFeature() {
    try {
      final ServiceFeature feature = ServiceFeature.build(CONSOLE, FEATURE);
      assertNotNull(feature);
      CONSOLE.info(feature.toString());

      // properties below are excluded per default
      assertFalse(ServiceFeature.BUNDLE_ENTRY,   feature.containsKey(ServiceFeature.BUNDLE_ENTRY));
      assertFalse(ServiceFeature.BUNDLE_NAME,    feature.containsKey(ServiceFeature.BUNDLE_NAME));
      assertFalse(ServiceFeature.BUNDLE_VERSION, feature.containsKey(ServiceFeature.BUNDLE_VERSION));

      // properties below are included per default
      assertTrue(Service.OIM.Feature.RFC_9110, feature.containsKey(Service.OIM.Feature.RFC_9110));
      assertFalse(Service.OIM.Feature.RFC_9110, feature.booleanValue(Service.OIM.Feature.RFC_9110));

      // validate the token to access the connector bundle
      final ConnectorKey token = feature.token();
      assertNotNull(token);
      assertEquals(token.getBundleName(),    "gfn.identity.connector.bundle");
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
  // Method:   userProvisioning
  /**
   ** Test user provisioning descriptor.
   */
  @Test
  public void userProvisioning() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), User.PROVISIONING);
      assertNotNull(descriptor);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userReconciliation
  /**
   ** Test user reconciliation descriptor.
   */
  @Test
  public void userReconciliation() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildReconciliation(CONSOLE), User.RECONCILIATION);
      assertNotNull(descriptor);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}