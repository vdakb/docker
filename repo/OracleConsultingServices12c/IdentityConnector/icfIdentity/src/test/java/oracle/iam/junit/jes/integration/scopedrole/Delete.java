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

package oracle.iam.junit.jes.integration.scopedrole;

import java.util.Set;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.AttributeFactory;
import oracle.iam.identity.connector.service.DescriptorFactory;

import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

import oracle.iam.junit.jes.TestModel;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on on scoped roles at the target system
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
  // Method:   unknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void unknown() {
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), ScopedRole.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel model = TestModel.build("igd/scopedrole-unknown.json");
      assertNotNull(model);

      final Set<Attribute> dataSet = DescriptorTransformer.build(provisioning, transfer(model.form("UD_OIG_SAR"), provisioning.attribute()));
      assertNotNull(dataSet);

      FACADE.delete(ServiceClass.SCOPED, AttributeFactory.uid(model.stringValue("UD_OIG_SAR", provisioning.identifier())), null);
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
  // Method:   orclOIMEntitlementAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    deleteAdminRole("igd/scopedrole-entadmin.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    deleteAdminRole("igd/scopedrole-entauthz.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    deleteAdminRole("igd/scopedrole-entviewer.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    deleteAdminRole("igd/scopedrole-orgadmin.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMOrgViewer() {
    deleteAdminRole("igd/scopedrole-orgviewer.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    deleteAdminRole("igd/scopedrole-roladmin.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    deleteAdminRole("igd/scopedrole-rolauthz.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleViewer() {
    deleteAdminRole("igd/scopedrole-rolviewer.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserAdmin() {
    deleteAdminRole("igd/scopedrole-usradmin.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    deleteAdminRole("igd/scopedrole-usrhdesk.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserViewer() {
    deleteAdminRole("igd/scopedrole-usrviewer.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    deleteAdminRole("igd/scopedrole-fimadminan.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    deleteAdminRole("igd/scopedrole-fimadminbb.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    deleteAdminRole("igd/scopedrole-fimadminbe.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    deleteAdminRole("igd/scopedrole-fimadminbk.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    deleteAdminRole("igd/scopedrole-fimadminbp.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    deleteAdminRole("igd/scopedrole-fimadminbw.json");
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBY
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    deleteAdminRole("igd/scopedrole-fimadminby.json");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteAdminRole
  /**
   ** Performs all actions to delete an scoped admin role in the target system
   ** by parsing the specified file to a map and transform that mapping to the
   ** connector server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private void deleteAdminRole(final String path) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), ScopedRole.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel      model   = TestModel.build(path);
      assertNotNull(model);

      FACADE.delete(ServiceClass.SCOPED, AttributeFactory.uid(model.stringValue("UD_OIG_SAR", provisioning.identifier())), option);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}