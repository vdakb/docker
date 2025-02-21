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

    File        :   Search.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Base.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.scopedrole;

import java.util.Set;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.OperationOptions;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

import oracle.iam.junit.jes.TestModel;

import oracle.iam.junit.jes.integration.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation of scoped roles at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Create extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Create</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Create() {
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
    final String[] parameter = {Create.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknown
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
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

      final Uid uid = facade(endpoint()).create(ServiceClass.SCOPED, dataSet, null);
      assertNotNull(uid);
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
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    assertEquals(createAdminRole("igd/scopedrole-entadmin.json"), ScopedRole.ENTADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    assertEquals(createAdminRole("igd/scopedrole-entauthz.json"), ScopedRole.ENTAUTHZ.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    assertEquals(createAdminRole("igd/scopedrole-entviewer.json"), ScopedRole.ENTVIEWER.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    assertEquals(createAdminRole("igd/scopedrole-orgadmin.json"), ScopedRole.ORGADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMOrgViewer() {
    assertEquals(createAdminRole("igd/scopedrole-orgviewer.json"), ScopedRole.ORGVIEWER.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    assertEquals(createAdminRole("igd/scopedrole-roladmin.json"), ScopedRole.ROLADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    assertEquals(createAdminRole("igd/scopedrole-rolauthz.json"), ScopedRole.ROLAUTHZ.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMRoleViewer() {
    assertEquals(createAdminRole("igd/scopedrole-rolviewer.json"), ScopedRole.ROLVIEWER.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserAdmin() {
    assertEquals(createAdminRole("igd/scopedrole-usradmin.json"), ScopedRole.USRADMIN.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    assertEquals(createAdminRole("igd/scopedrole-usrhdesk.json"), ScopedRole.USRHDESK.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserViewer() {
    assertEquals(createAdminRole("igd/scopedrole-usrviewer.json"), ScopedRole.USRVIEWER.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminan.json"), ScopedRole.FIMADMINAN.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminbb.json"), ScopedRole.FIMADMINBB.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminbe.json"), ScopedRole.FIMADMINBE.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminbk.json"), ScopedRole.FIMADMINBK.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminbp.json"), ScopedRole.FIMADMINBP.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminbw.json"), ScopedRole.FIMADMINBW.value);
  }
 
  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBY
  /**
   ** Test create request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    assertEquals(createAdminRole("igd/scopedrole-fimadminby.json"), ScopedRole.FIMADMINBY.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAdminRole
  /**
   ** Performs all actions to create a scoped admin role in the target system by
   ** parsing the specified file to a map and transform that mapping to the
   ** connector server related attribute set.
   **
   ** @param  path               the file path providing the data to process.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the system identifier of the scoped admin role
   **                            created.
   **                            <br>
   **                            Possible object is {@link UID}.
   */
  private Uid createAdminRole(final String path) {
    final OperationOptions option   = null;
    try {
      final Descriptor provisioning = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), ScopedRole.PROVISIONING);
      assertNotNull(provisioning);

      // the model mimics how Identity Governance provides the data after the
      // adapter task createProcessData applied on the process data
      final TestModel model = TestModel.build(path);
      assertNotNull(model);

      final Set<Attribute> dataSet = DescriptorTransformer.build(provisioning, transfer(model.form("UD_OIG_SAR"), provisioning.attribute()));
      assertNotNull(dataSet);

      final Uid uid = facade(endpoint()).create(ServiceClass.SCOPED, dataSet, option);
      assertNotNull(uid);
      return uid;
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
    return new Uid("Failure");
  }
}