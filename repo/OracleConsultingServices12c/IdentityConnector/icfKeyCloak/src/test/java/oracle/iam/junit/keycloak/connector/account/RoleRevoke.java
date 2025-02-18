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

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   RoleRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    RoleRevoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.junit.keycloak.connector.TestFixture;

import oracle.iam.identity.icf.connector.keycloak.schema.User;
import oracle.iam.identity.icf.connector.keycloak.schema.Role;

////////////////////////////////////////////////////////////////////////////////
// class RoleRevoke
// ~~~~~ ~~~~~~~~~~
/**
 ** The test case modify operation on role mappings at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleRevoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RoleRevoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleRevoke() {
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
    final String[] parameter = {RoleRevoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrauOfflineAccess
  /**
   ** Test modify request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrauOfflineAccess() {
    revoke(Identity.MUSTERFRAU.tag, RealmRole.OFFLINE.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermannAuthorization
  /**
   ** Test modify request leveraging server context.
   ** <p>
   ** <b>Max Mustermann</b>
   */
  @Test
  public void mustermannAuthorization() {
    revoke(Identity.MUSTERMANN.tag, RealmRole.AUTHZ.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revoke
  /**
   ** Test modify request leveraging server context.
   */
  protected void revoke(final String identity, final String role) {
    try {
      final User beneficiary = CONTEXT.resolveUser(identity);
      assertNotNull(beneficiary);
      assertNotNull(beneficiary.id());
      final Role subject = CONTEXT.resolveRole(role);
      assertNotNull(subject);
      assertNotNull(subject.id());
      assertNotNull(subject.id());
      CONTEXT.revokeRole(beneficiary.id(), subject);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}