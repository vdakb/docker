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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.integration.scopedrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.icf.connector.oig.schema.ServiceClass;

import oracle.iam.junit.jes.integration.TestFixture;
import oracle.iam.junit.jes.integration.TestFixture.ScopedRole;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case for resolving scoped roles by its rolename at the target
 ** system leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resolve</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolve() {
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
    final String[] parameter = {Resolve.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknown
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void unknown() {
    try {
      FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.UNKNOWN.tag, null);
    }
    catch (ConnectorException e) {
      // swallow the expected exception but fail in any other case
      if (!e.getLocalizedMessage().startsWith("JES-00022"))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ENTADMIN.tag, null), ScopedRole.ENTADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ENTAUTHZ.tag, null), ScopedRole.ENTAUTHZ.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ENTVIEWER.tag, null), ScopedRole.ENTVIEWER.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ORGADMIN.tag, null), ScopedRole.ORGADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMOrgViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ORGVIEWER.tag, null), ScopedRole.ORGVIEWER.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ROLADMIN.tag, null), ScopedRole.ROLADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ROLAUTHZ.tag, null), ScopedRole.ROLAUTHZ.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test resolve request leveraging server context.
   */
  @Test
  public void orclOIMRoleViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.ROLVIEWER.tag, null), ScopedRole.ROLVIEWER.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserAdmin() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.USRADMIN.tag, null), ScopedRole.USRADMIN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.USRHDESK.tag, null), ScopedRole.USRHDESK.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void orclOIMUserViewer() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.USRVIEWER.tag, null), ScopedRole.USRVIEWER.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINAN.tag, null), ScopedRole.FIMADMINAN.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBB.tag, null), ScopedRole.FIMADMINBB.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBE.tag, null), ScopedRole.FIMADMINBE.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBK.tag, null), ScopedRole.FIMADMINBK.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBP.tag, null), ScopedRole.FIMADMINBP.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBW.tag, null), ScopedRole.FIMADMINBW.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBY
  /**
   ** Test resolve request leveraging connector bundle deployed on a
   ** <code>Java Connector Server</code>.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    try {
      assertEquals(FACADE.resolveUsername(ServiceClass.SCOPED, ScopedRole.FIMADMINBY.tag, null), ScopedRole.FIMADMINBY.value);
    }
    catch (ConnectorException e) {
      failed(e);
    }
  }
}