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
  // Method:   createUnknown
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createUnknown() {
    try {
      CONTEXT.createScopedRole("Unkown");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"RMI-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMEntitlementAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMEntitlementAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMEntitlementAdministrator"), 7L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMEntitlementAuthorizer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMEntitlementAuthorizer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMEntitlementAuthorizer"), 8L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMEntitlementViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMEntitlementViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMEntitlementViewer"), 9L);
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
  public void createOrclOIMOrgAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMOrgAdministrator"), 13L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMOrgViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMOrgViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMOrgViewer"), 14L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMRoleAdministrator
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMRoleAdministrator() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMRoleAdministrator"), 4L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMRoleAuthorizer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMRoleAuthorizer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMRoleAuthorizer"), 5L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMRoleViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMRoleViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMRoleViewer"), 6L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMUserAdmin
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMUserAdmin() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMUserAdmin"), 15L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMUserHelpDesk
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMUserHelpDesk() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMUserHelpDesk"), 16L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createOrclOIMUserViewer
  /**
   ** Test create request leveraging server context.
   */
  @Test
  public void createOrclOIMUserViewer() {
    try {
      assertEquals(CONTEXT.createScopedRole("OrclOIMUserViewer"), 17L);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}