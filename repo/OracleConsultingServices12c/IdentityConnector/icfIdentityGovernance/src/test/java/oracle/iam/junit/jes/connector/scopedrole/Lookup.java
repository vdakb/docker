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
  // Method:   lookupUnknown
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupUnknown() {
    try {
      CONTEXT.lookupScopedRole("Unkown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMEntitlementAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMEntitlementAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMEntitlementAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 7L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMEntitlementAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMEntitlementAuthorizer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMEntitlementAuthorizer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMEntitlementAuthorizer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 8L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMEntitlementAuthorizer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMEntitlementViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMEntitlementViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMEntitlementViewer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 9L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMEntitlementViewer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMOrgAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMOrgAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMOrgAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 13L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMOrgAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMOrgViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMOrgViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMOrgViewer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 14L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMOrgViewer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMRoleAdministrator
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMRoleAdministrator() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMRoleAdministrator");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 4L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMRoleAdministrator");
    }
    catch (SystemException e) {
      failed(e);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMRoleAuthorizer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMRoleAuthorizer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMRoleAuthorizer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 5L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMRoleAuthorizer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMRoleViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMRoleViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMRoleViewer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 6L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMRoleViewer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMUserAdmin
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMUserAdmin() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMUserAdmin");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 15L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMUserAdmin");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMUserHelpDesk
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMUserHelpDesk() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMUserHelpDesk");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 16L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMUserHelpDesk");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupOrclOIMUserViewer
  /**
   ** Test lookup request leveraging server context.
   */
  @Test
  public void lookupOrclOIMUserViewer() {
    try {
      final AdminRole identity = CONTEXT.lookupScopedRole("OrclOIMUserViewer");
      assertNotNull(identity);
      assertNotNull(identity.getRoleId());
      assertEquals(identity.getRoleId(), 17L);
      assertTrue(identity.isScoped());
      assertEquals(identity.getRoleName(), "OrclOIMUserViewer");
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}