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

    File        :   Resolve.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Resolve.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.connector.account;

import java.io.File;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.keycloak.schema.User;
import oracle.iam.identity.icf.connector.keycloak.schema.Service;

import oracle.iam.junit.keycloak.connector.TestFixture;

import oracle.iam.identity.icf.foundation.object.Pair;
import oracle.iam.junit.keycloak.connector.TestFixture.Identity;

////////////////////////////////////////////////////////////////////////////////
// class Resolve
// ~~~~~ ~~~~~~
/**
 ** The test case lookup operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
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
  // Method:   cambrault
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Gerald Cambrault</b>
   */
  @Test
  public void cambrault() {
    resolve(Identity.CAMBRAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrau
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrau() {
    resolve(Identity.MUSTERFRAU);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Max Musterman</b>
   */
  @Test
  public void mustermann() {
    resolve(Identity.MUSTERMANN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbacke
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Max Musterman</b>
   */
  @Test
  public void zitterbacke() {
    resolve(Identity.ZITTERBACKE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resolve
  /**
   ** Test lookup request leveraging server context.
   */
  protected void resolve(final Pair<String, File> identity) {
    final User origin = user(identity.value);
    assertNotNull(origin);
    assertEquals(Service.USERNAME,   origin.username(), identity.tag);
    assertTrue(Service.VERIFIED,     origin.verified());
    assertTrue(Service.STATUS,       origin.enabled());
    assertNotNull(Service.FIRSTNAME, origin.firstName());
    assertNotNull(Service.LASTNAME,  origin.lastName());
    assertNotNull(Service.EMAIL,     origin.email());
    try {
      final User target = CONTEXT.resolveUser(identity.tag);
      assertNotNull(target);
      assertEquals(Service.USERNAME,  target.username(),  origin.username());
      assertEquals(Service.VERIFIED,  target.verified(),  origin.verified());
      assertEquals(Service.STATUS,    target.enabled(),   origin.enabled());
      assertEquals(Service.FIRSTNAME, target.firstName(), origin.firstName());
      assertEquals(Service.LASTNAME,  target.lastName(),  origin.lastName());
      assertEquals(Service.EMAIL,     target.email(),     origin.email());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}