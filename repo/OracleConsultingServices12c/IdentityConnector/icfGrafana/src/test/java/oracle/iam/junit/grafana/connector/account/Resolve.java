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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.grafana.schema.User;

import oracle.iam.junit.grafana.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~~
/**
 ** The test case retrieve operation on identities at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Resolve extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Resolve</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Resolve() {
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
    final String[] parameter = {Resolve.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   admin
  /**
   ** Test resolve request leveraging server context.
   ** <p>
   ** <b>Admin</b>
   */
  @Test
  public void admin() {
    resolve(Identity.ADMIN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cambrault
  /**
   ** Test resolve request leveraging server context.
   ** <p>
   ** <b>Gerald Cambrault</b>
   */
  @Test
  public void cambrault() {
    resolve(Identity.CAMBRAULT.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrau
  /**
   ** Test resolve request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrau() {
    resolve(Identity.MUSTERFRAU.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test resolve request leveraging server context.
   ** <p>
   ** <b>Max Mustermann</b>
   */
  @Test
  public void mustermann() {
    resolve(Identity.MUSTERMANN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbacke
  /**
   ** Test resolve request leveraging server context.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void zitterbacke() {
    resolve(Identity.ZITTERBACKE.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
  /**
   ** Test resolve request leveraging server context.
   */
  protected void resolve(final String identity) {
    try {
      final User origin = CONTEXT.resolveUser(identity);
      assertNotNull(origin);
      assertEquals(User.LOGIN,  origin.login(), identity);
      assertNotNull(User.EMAIL, origin.email());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}