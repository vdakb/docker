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
  // Method:   deleteUnknown
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteUnknown() {
    try {
      CONTEXT.deleteScopedRole("9999999999");
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"RMI-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMEntitlementAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMEntitlementAdministrator() {
    try {
      CONTEXT.deleteScopedRole("7");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMEntitlementAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMEntitlementAuthorizer() {
    try {
      CONTEXT.deleteScopedRole("8");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMEntitlementViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMEntitlementViewer() {
    try {
      CONTEXT.deleteScopedRole("9");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMOrgAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMOrgAdministrator() {
    try {
      CONTEXT.deleteScopedRole("13");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMOrgViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMOrgViewer() {
    try {
      CONTEXT.deleteScopedRole("14");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMRoleAdministrator
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMRoleAdministrator() {
    try {
      CONTEXT.deleteScopedRole("4");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMRoleAuthorizer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMRoleAuthorizer() {
    try {
      CONTEXT.deleteScopedRole("5");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMRoleViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMRoleViewer() {
    try {
      CONTEXT.deleteScopedRole("6");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMUserAdmin
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMUserAdmin() {
    try {
      CONTEXT.deleteScopedRole("15");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMUserHelpDesk
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMUserHelpDesk() {
    try {
      CONTEXT.deleteScopedRole("16");
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteOrclOIMUserViewer
  /**
   ** Test delete request leveraging server context.
   */
  @Test
  public void deleteOrclOIMUserViewer() {
    try {
      CONTEXT.deleteScopedRole("17");
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}