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
import oracle.iam.junit.jes.connector.TestFixture.ScopedRole;

////////////////////////////////////////////////////////////////////////////////
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on scoped roles at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
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
   ** Test delete request leveraging server context.
   */
  @Test
  public void unknown() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.UNKNOWN.value);
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
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAdministrator() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ENTADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementAuthorizer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ENTAUTHZ.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMEntitlementViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMEntitlementViewer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ENTVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMOrgAdministrator() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ORGADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMOrgViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMOrgViewer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ORGVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleAdministrator() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ROLADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleAuthorizer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ROLAUTHZ.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMRoleViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMRoleViewer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.ROLVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserAdmin
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserAdmin() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.USRADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserHelpDesk
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserHelpDesk() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.USRHDESK.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orclOIMUserViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void orclOIMUserViewer() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.USRVIEWER.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAc
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorAN() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINAN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBB
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBB() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBB.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBE
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBE() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBE.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBK
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBK() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBK.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBP
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBP() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBP.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorBW
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBW() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBW.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fimIdentityAdministratorAN
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void fimIdentityAdministratorBY() {
    try {
      CONTEXT.deleteScopedRole(ScopedRole.FIMADMINBY.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}