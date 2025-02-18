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

package oracle.iam.junit.igs.integration.tenant;


import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

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
   ** Test for unmarshalling the provisioning descriptor for tenants.
   */
  @Test
  public void provisioning() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      assertNotNull(descriptor);
      assertEquals(descriptor.attribute().size(), 2);
      assertTrue(descriptor.source().contains("UD_IGS_TNT_SID"));
      assertTrue(descriptor.source().contains("UD_IGS_TNT_UID"));
      assertTrue(descriptor.target().contains("id"));
      assertTrue(descriptor.target().contains("__NAME__"));

      final Descriptor account = descriptor.reference(Pair.of(ObjectClass.ACCOUNT_NAME, "UD_IGS_TUS"));
      assertNotNull(account);
      assertEquals(account.attribute().size(), 2);
      assertTrue(account.source().contains("UD_IGS_TUS_TID"));
      assertTrue(account.source().contains("UD_IGS_TUS_SID"));
      assertTrue(account.target().contains("__UID__"));
      assertTrue(account.target().contains("scope"));
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reconciliation
  /**
   ** Test for unmarshalling the reconcilaition descriptor for tenants.
   */
  @Test
  public void reconciliation() {
    try {
      final Descriptor descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), RECONCILIATION);
      assertNotNull(descriptor);
      assertEquals(descriptor.identifier(), "Id");
      assertEquals(descriptor.uniqueName(), "Name");

      assertEquals(descriptor.attribute().size(), 3);
      assertTrue(descriptor.source().contains("__UID__"));
      assertTrue(descriptor.source().contains("__NAME__"));
      assertTrue(descriptor.source().contains("__ENABLE__"));
      assertTrue(descriptor.target().contains("Id"));
      assertTrue(descriptor.target().contains("Name"));
      assertTrue(descriptor.target().contains("Status"));

      final Descriptor account = descriptor.reference(Pair.of("User", ObjectClass.ACCOUNT_NAME));
      assertNotNull(account);

      assertEquals(account.attribute().size(), 3);
      assertTrue(account.source().contains("__UID__"));
      assertTrue(account.source().contains("__NAME__"));
      assertTrue(account.source().contains("scope"));
      assertTrue(account.target().contains("Id"));
      assertTrue(account.target().contains("Name"));
      assertTrue(account.target().contains("Scope"));
    }
    catch (TaskException e) {
     failed(e);
    }
  }
}