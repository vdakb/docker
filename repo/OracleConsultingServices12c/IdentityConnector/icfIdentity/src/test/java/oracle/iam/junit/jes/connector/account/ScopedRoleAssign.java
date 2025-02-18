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

    File        :   ScopedRoleAssign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ScopedRoleAssign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;
import oracle.iam.junit.jes.connector.TestFixture.Identity;
import oracle.iam.junit.jes.connector.TestFixture.ScopedRole;
import oracle.iam.junit.jes.connector.TestFixture.Tenant;

////////////////////////////////////////////////////////////////////////////////
// class ScopedRoleAssign
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case assign operation of scoped admin roles to an identity at the
 ** target system leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
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
   ** Test grant scoped admin role to unknown user leveraging server context.
   */
  @Test
  public void entitlementAdminToUnknown() {
    try {
      CONTEXT.assignScopedRole(Identity.UNKNOWN.value, ScopedRole.ENTADMIN.value, Tenant.AN.value, false);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToZitternbacke
  /**
   ** Test grant scoped admin role to regular user leveraging server context.
   */
  @Test
  public void entitlementAdminToZitternbacke() {
    try {
      CONTEXT.assignScopedRole(Identity.ZITTERBACKE.value, ScopedRole.ENTADMIN.value, Tenant.BK.value, true);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToCambrault
  /**
   ** Test grant scoped admin role to regular user leveraging server context.
   */
  @Test
  public void entitlementAdminToCambrault() {
    try {
      CONTEXT.assignScopedRole(Identity.CAMBRAULT.value, ScopedRole.ENTADMIN.value, Tenant.AN.value, false);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminToMusterfrau
  /**
   ** Test grant scoped admin role to regular user leveraging server context.
   */
  @Test
  public void entitlementAdminToMusterfrau() {
    try {
      CONTEXT.assignScopedRole(Identity.MUSTERFRAU.value, ScopedRole.ENTADMIN.value, Tenant.BP.value, true);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBPToMusterfrau
  /**
   ** Test grant scoped admin role to regular user leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBPToMusterfrau() {
    try {
      CONTEXT.assignScopedRole(Identity.MUSTERFRAU.value, ScopedRole.FIMADMINBP.value, Tenant.BP.value, true);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}