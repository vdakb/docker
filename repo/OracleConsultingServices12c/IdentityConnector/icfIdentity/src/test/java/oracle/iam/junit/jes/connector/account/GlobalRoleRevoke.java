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

    File        :   GlobalRoleRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    GlobalRoleRevoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.jes.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.jes.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class GlobalRoleRevoke
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case revoke operation of global admin roles from an identity at the
 ** target system leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GlobalRoleRevoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GlobalRoleRevoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GlobalRoleRevoke() {
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
    final String[] parameter = {GlobalRoleRevoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogAdministratorFromUnknown
  /**
   ** Test revoke global admin role from unknown user leveraging server context.
   */
  @Test
  public void catalogAdministratorFromUnknown() {
    try {
      CONTEXT.revokeGlobalRole(Identity.UNKNOWN.value, GlobalRole.CATADMIN.value);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemConfiguratorFromMusterfrau
  /**
   ** Test revoke global admin role from regular user leveraging server context.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The test will succeed regardless if the relationship betwee user and admin
   ** role exists. Only if either the user identity or the admin role does not
   ** exists the test will fail.
   */
  @Test
  public void systemConfiguratorFromMusterfrau() {
    try {
      CONTEXT.revokeGlobalRole(Identity.MUSTERFRAU.value, GlobalRole.SYSCONFIG.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catalogAdministratorFromMusterfrau
  /**
   ** Test revoke global admin role from regular user leveraging server context.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The test will succeed regardless if the relationship betwee user and admin
   ** role exists. Only if either the user identity or the admin role does not
   ** exists the test will fail.
   */
  @Test
  public void catalogAdministratorFromMusterfrau() {
    try {
      CONTEXT.revokeGlobalRole(Identity.MUSTERFRAU.value, GlobalRole.CATADMIN.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}