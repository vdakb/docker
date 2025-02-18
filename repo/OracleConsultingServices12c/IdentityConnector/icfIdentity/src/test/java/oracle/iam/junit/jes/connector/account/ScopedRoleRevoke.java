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

    File        :   ScopedRoleRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ScopedRoleRevoke.


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

////////////////////////////////////////////////////////////////////////////////
// class ScopedRoleRevoke
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case revoke operation of scoped admin roles from an identity at the
 ** target system leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ScopedRoleRevoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ScopedRoleRevoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ScopedRoleRevoke() {
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
    final String[] parameter = {ScopedRoleRevoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminFromUnknown
  /**
   ** Test revoke scoped admin role from unknown user leveraging server context.
   */
  @Test
  public void entitlementAdminFromUnknown() {
    try {
      CONTEXT.revokeScopedRole(Identity.UNKNOWN.value, ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminFromZitterbacke
  /**
   ** Test revoke scoped admin role from unknown user leveraging server context.
   */
  @Test
  public void entitlementAdminFromZitterbacke() {
    try {
      CONTEXT.revokeScopedRole(Identity.ZITTERBACKE.value, ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminFromCambrault
  /**
   ** Test revoke scoped admin role from unknown user leveraging server context.
   */
  @Test
  public void entitlementAdminFromCambrault() {
    try {
      CONTEXT.revokeScopedRole(Identity.CAMBRAULT.value, ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlementAdminFromMusterfrau
  /**
   ** Test revoke scoped admin role from unknown user leveraging server context.
   */
  @Test
  public void entitlementAdminFromMusterfrau() {
    try {
      CONTEXT.revokeScopedRole(Identity.MUSTERFRAU.value, ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBPFromMusterfrau
  /**
   ** Test revoke scoped admin role from unknown user leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBPFromMusterfrau() {
    try {
      CONTEXT.revokeScopedRole(Identity.MUSTERFRAU.value, ScopedRole.FIMADMINBP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}