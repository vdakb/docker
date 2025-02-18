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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.scopedrole;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.platform.authopss.vo.AdminRole;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;
import oracle.iam.junit.jes.connector.TestFixture.ScopedRole;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case lookup operation on roles at the target system leveraging the
 ** connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Lookup extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Lookup</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Lookup() {
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
    final String[] parameter = {Lookup.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unknown
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void unknown() {
    try {
      CONTEXT.lookupScopedRole(ScopedRole.UNKNOWN.tag);
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
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ENTADMIN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ENTADMIN.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ENTADMIN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ENTAUTHZ.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ENTAUTHZ.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ENTAUTHZ.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ENTVIEWER.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ENTVIEWER.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ENTVIEWER.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ORGADMIN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ORGADMIN.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ORGADMIN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMOrgViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ORGVIEWER.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ORGVIEWER.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ORGVIEWER.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ROLADMIN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ROLADMIN.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ROLADMIN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ROLAUTHZ.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ROLAUTHZ.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ROLAUTHZ.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMRoleViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.ROLVIEWER.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.ROLVIEWER.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.ROLVIEWER.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMUserAdmin() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.USRADMIN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.USRADMIN.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.USRADMIN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.USRHDESK.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.USRHDESK.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.USRHDESK.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void orclOIMUserViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.USRVIEWER.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.USRVIEWER.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.USRVIEWER.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINAN.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINAN.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINAN.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBB.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBB.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBB.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBE.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBE.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBE.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBK.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBK.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBK.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBP.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBP.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBP.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBW.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBW.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBW.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBY
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole(ScopedRole.FIMADMINBY.tag);
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), ScopedRole.FIMADMINBY.value);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), ScopedRole.FIMADMINBY.tag);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}