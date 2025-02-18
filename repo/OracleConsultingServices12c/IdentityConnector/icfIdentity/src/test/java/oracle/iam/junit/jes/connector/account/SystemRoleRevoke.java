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

    File        :   SystemRoleRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    SystemRoleRevoke.


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
// class SystemRoleRevoke
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case revoke operation of system roles from an identity at the target
 ** system leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemRoleRevoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemRoleRevoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public SystemRoleRevoke() {
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
    final String[] parameter = {SystemRoleRevoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   newAdministratorFromUnknown
  /**
   ** Test revoke system role from unknown user leveraging server context.
   */
  @Test
  public void newAdministratorFromUnknown() {
    try {
      CONTEXT.revokeSystemRole(SystemRole.NEWADMIN.value, Identity.UNKNOWN.value);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unkwonFromZitterbacke
  /**
   ** Test revoke system role from unknown user leveraging server context.
   */
  @Test
  public void unkwonFromZitterbacke() {
    try {
      CONTEXT.revokeSystemRole(SystemRole.UNKNOWN.value, Identity.ZITTERBACKE.value);
    }
    catch (SystemException e) {
      // swallow the expected exception but fail in any other case
      if (!"JES-00022".equals(e.code()))
        failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oldAdministratorFromZitterbacke
  /**
   ** Test revoke system role from unknown user leveraging server context.
   */
  @Test
  public void oldAdministratorFromZitterbacke() {
    try {
      CONTEXT.revokeSystemRole(SystemRole.OLDADMIN.value, Identity.ZITTERBACKE.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   newAdministratorFromMusterfrau
  /**
   ** Test revoke system role from unknown user leveraging server context.
   */
  @Test
  public void newAdministratorFromMusterfrau() {
    try {
      CONTEXT.revokeSystemRole(SystemRole.NEWADMIN.value, Identity.MUSTERFRAU.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bipAdministratorFromMusterfrau
  /**
   ** Test revoke system role from unknown user leveraging server context.
   */
  @Test
  public void bipAdministratorFromMusterfrau() {
    try {
      CONTEXT.revokeSystemRole(SystemRole.BIRADMIN.value, Identity.MUSTERFRAU.value);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}