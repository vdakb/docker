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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Delete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.account;

import oracle.iam.identity.connector.service.AttributeFactory;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.junit.jes.TestModel;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on on accounts at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Delete extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Delete</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Delete() {
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
    final String[] parameter = {Delete.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteUnknown() {
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), Account.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build("igd/account-unknown.json");
      assertNotNull(model);

      FACADE.delete(ObjectClass.ACCOUNT, AttributeFactory.uid(model.stringValue("UD_OIG_USR", provisioning.identifier())), null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteSystemAdministrator() {
    deleteUser("igd/account-xelsysadm.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteSystemOperator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteSystemOperator() {
    deleteUser("igd/account-xeloperator.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteServerAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void deleteServerAdministrator() {
    deleteUser("igd/account-weblogic.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteInternalAdministrator
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void deleteInternalAdministrator() {
    deleteUser("igd/account-oiminternal.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteZitterbacke
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void deleteZitterbacke() {
    deleteUser("igd/account-zitterbacke.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteCambrault
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void deleteCambrault() {
    deleteUser("igd/account-cambrault.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMustermann
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void deleteMustermann() {
    deleteUser("igd/account-mustermann.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteMusterfrau
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void deleteMusterfrau() {
    deleteUser("igd/account-musterfrau.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteUser
  /**
   ** Performs all actions to delete an account in the target system by parsing
   ** the specified file to a map and transform that mapping to the connector
   ** server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void deleteUser(final String path) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), Account.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build(path);
      assertNotNull(model);

      FACADE.delete(ObjectClass.ACCOUNT, AttributeFactory.uid(model.stringValue("UD_OIG_USR", provisioning.identifier())), option);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}