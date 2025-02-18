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

    File        :   Config.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Config.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.integration.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.identity.icf.connector.scim.igs.ExtensionClass;

////////////////////////////////////////////////////////////////////////////////
// class Config
// ~~~~~ ~~~~~~
/**
 ** The test case to validate descriptor attribute mapping from Identity
 ** Governance semantics to a ICF Attribute set.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Config extends Base {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Config</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Config() {
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
    final String[] parameter = {Config.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provisioning
  /**
   ** Test for unmarshalling the provisioning descriptor for accounts.
   */
  @Test
  public void provisioning() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      assertEquals(descriptor.attribute().size(), 8);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  reconciliation
  /**
   ** Test for unmarshalling the reconcilaition descriptor for accounts.
   */
  @Test
  public void reconciliation() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), RECONCILIATION);
      assertNotNull(descriptor);
      assertEquals(descriptor.identifier(), "Identifier");
      assertEquals(descriptor.uniqueName(), "User Name");

      assertEquals(descriptor.attribute().size(), 8);

      assertTrue(descriptor.source().contains("__UID__"));
      assertTrue(descriptor.source().contains("__NAME__"));
      assertTrue(descriptor.source().contains("__ENABLE__"));
      assertTrue(descriptor.source().contains("name.familyName"));
      assertTrue(descriptor.source().contains("name.givenName"));
      assertTrue(descriptor.source().contains("emails.work.value"));
      assertTrue(descriptor.source().contains("phoneNumbers.work.value"));

      assertTrue(descriptor.target().contains("Identifier"));
      assertTrue(descriptor.target().contains("User Name"));
      assertTrue(descriptor.target().contains("Status"));
      assertTrue(descriptor.target().contains("Last Name"));
      assertTrue(descriptor.target().contains("First Name"));
      assertTrue(descriptor.target().contains("e-Mail"));
      assertTrue(descriptor.target().contains("Phone"));
      assertTrue(descriptor.target().contains("Mobile"));

      assertEquals(descriptor.reference().size(), 2);

      final Descriptor group = descriptor.reference(Pair.of("Role", ObjectClass.GROUP_NAME));
      assertNotNull(group);
      assertTrue("__UID__",  group.source().contains("__UID__"));

      assertTrue("Name",         group.target().contains("Name"));

      final Descriptor tenant = descriptor.reference(Pair.of("Tenant", ExtensionClass.TENANT.getObjectClassValue()));
      assertNotNull(tenant);
      assertTrue("__UID__",  tenant.source().contains("__UID__"));
      assertTrue("scope",    tenant.source().contains("scope"));

      assertTrue("Name",         tenant.target().contains("Name"));
      assertTrue("Scope",        tenant.target().contains("Scope"));
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}