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
    Subsystem   :   Identity Governance Provisioning

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.scopedrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on scoped roles at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
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
   ** Test create request leveraging server context.
   */
  @Test
  public void unknown() {
    try {
      CONTEXT.createScopedRole(ScopedRole.UNKNOWN.tag);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ENTADMIN.tag), ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ENTAUTHZ.tag), ScopedRole.ENTAUTHZ.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ENTVIEWER.tag), ScopedRole.ENTVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ORGADMIN.tag), ScopedRole.ORGADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMOrgViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ORGVIEWER.tag), ScopedRole.ORGVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ROLADMIN.tag), ScopedRole.ROLADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ROLAUTHZ.tag), ScopedRole.ROLAUTHZ.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMRoleViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.ROLVIEWER.tag), ScopedRole.ROLVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMUserAdmin() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMUserAdmin"), ScopedRole.USRADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.USRHDESK.tag), ScopedRole.USRHDESK.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void orclOIMUserViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.USRVIEWER.tag), ScopedRole.USRVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINAN.tag), ScopedRole.FIMADMINAN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBB.tag), ScopedRole.FIMADMINBB.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBE.tag), ScopedRole.FIMADMINBE.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBK.tag), ScopedRole.FIMADMINBK.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBP.tag), ScopedRole.FIMADMINBP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBW.tag), ScopedRole.FIMADMINBW.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBY
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    try {
      assertEquals(CONTEXT.createScopedRole(ScopedRole.FIMADMINBY.tag), ScopedRole.FIMADMINBY.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}