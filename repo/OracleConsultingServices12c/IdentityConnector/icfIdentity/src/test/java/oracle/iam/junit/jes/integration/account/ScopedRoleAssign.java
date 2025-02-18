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

    File        :   ScopedRoleAssign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ScopedRoleAssign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.account;

import java.util.Set;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.hst.foundation.object.Pair;

import oracle.iam.identity.connector.service.AttributeFactory;
import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.junit.jes.TestModel;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class ScopedRoleAssign
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case assign operation of scoped admin roles to an identity at the
 ** target system leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class ScopedRoleAssign extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ScopedRoleAssign</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ScopedRoleAssign() {
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
    final String[] parameter = {ScopedRoleAssign.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToUnknown
  /**
   ** Test grant scoped admin role to unknown user leveraging connector bundle
   ** deployed on a <code>Java Connector Server</code>.
   ** <p>
   ** Special condition to capture the unknown user exception.
   */
  @Test
  public void entitlementAdminToUnknown() {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), Account.PROVISIONING);
      assertNotNull(provisioning);

      final Pair<String, String> element   = Pair.of("Scoped", "UD_OIG_UPS");
      final Descriptor           reference = provisioning.reference(element);
      assertNotNull(reference);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build("igd/account-unknown.json");
      assertNotNull(model);

      final Set<Attribute> dataSet = DescriptorTransformer.build(DescriptorTransformer.objectClass(element.tag), reference, transfer(model.form(element.value), reference.attribute()));
      assertNotNull(dataSet);

      FACADE.addAttributeValues(ObjectClass.ACCOUNT, Account.UNKNOWN.value, dataSet, option);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToMustermann
  /**
   ** Test grant scoped admin role to unknown user leveraging connector bundle
   ** deployed on a <code>Java Connector Server</code>.
   */
  @Test
  public void entitlementAdminToMustermann() {
    assign("igd/account-mustermann.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToMusterfrau
  /**
   ** Test grant scoped admin role to unknown user leveraging connector bundle
   ** deployed on a <code>Java Connector Server</code>.
   */
  @Test
  public void entitlementAdminToMusterfrau() {
    assign("igd/account-musterfrau.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assign
  /**
   ** Test grant scoped admin role to any user leveraging connector bundle
   ** deployed on a <code>Java Connector Server</code>.
   */
  protected void assign(final String config) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), Account.PROVISIONING);
      assertNotNull(provisioning);

      final Pair<String, String> element   = Pair.of("Scoped", "UD_OIG_UPS");
      final Descriptor           reference = provisioning.reference(element);
      assertNotNull(reference);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build(config);
      assertNotNull(model);

      final Set<Attribute> dataSet = DescriptorTransformer.build(DescriptorTransformer.objectClass(element.tag), reference, transfer(model.form(element.value), reference.attribute()));
      assertNotNull(dataSet);

      FACADE.addAttributeValues(ObjectClass.ACCOUNT, AttributeFactory.uid(model.stringValue("UD_OIG_USR", provisioning.identifier())), dataSet, option);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}