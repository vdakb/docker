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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Revoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Revoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.grafana.schema.User;
import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Organization;

import oracle.iam.junit.grafana.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Revoke
// ~~~~~ ~~~~~~
/**
 ** The test case grant operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Revoke extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Revoke</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Revoke() {
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
    final String[] parameter = {Revoke.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbackeSaxony
  /**
   ** Test revoke team request leveraging server context.
   ** <p>
   ** <b>Alfons Zitterbacke</b> from <code>Saxony</code>
   */
  @Test
  public void zitterbackeSaxony() {
    revokeTeam(Identity.ZITTERBACKE.tag, Tenant.SN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeOrganization
  /**
   ** Test revoke from organization request leveraging server context.
   */
  protected void revokeOrganization(final String beneficiary, final String organization) {
    try {
      final User user = CONTEXT.resolveUser(beneficiary);
      assertNotNull(user);

      final Organization subject = CONTEXT.resolveOrganization(organization);
      assertNotNull(subject);

      CONTEXT.revokeOrganization(user.id(), subject.id());      
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   revokeTeam
  /**
   ** Test revoke from team request leveraging server context.
   */
  protected void revokeTeam(final String beneficiary, final String team) {
    try {
      final User user = CONTEXT.resolveUser(beneficiary);
      assertNotNull(user);

      final Team subject = CONTEXT.resolveTeam(team);
      assertNotNull(subject);

      CONTEXT.revokeTeam(user.id(), subject.id());      
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}