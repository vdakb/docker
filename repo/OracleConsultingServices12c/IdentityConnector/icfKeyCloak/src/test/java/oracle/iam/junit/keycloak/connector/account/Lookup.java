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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.connector.account;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.keycloak.schema.User;
import oracle.iam.identity.icf.connector.keycloak.schema.Service;

import oracle.iam.junit.keycloak.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case retrieve operation on identities at the target system
 ** leveraging the connector implementation directly.
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
  // Method:   zitterbacke
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Max Musterman</b>
   */
  @Test
  public void zitterbacke() {
    lookup(Identity.ZITTERBACKE.tag);
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
    lookup(Identity.CAMBRAULT.tag);
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
    lookup(Identity.MUSTERFRAU.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void mustermann() {
    lookup(Identity.MUSTERMANN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Test create request leveraging server context.
   */
  protected void lookup(final String identity) {
    try {
      final User origin = CONTEXT.resolveUser(identity);
      assertNotNull(origin);
      assertEquals(Service.USERNAME,   origin.username(), identity);
      assertTrue(Service.VERIFIED,     origin.verified());
      assertTrue(Service.STATUS,       origin.enabled());
      assertNotNull(Service.FIRSTNAME, origin.firstName());
      assertNotNull(Service.LASTNAME,  origin.lastName());
      assertNotNull(Service.EMAIL,     origin.email());

      final User beneficiary = CONTEXT.lookupUser(origin.id(), true);
      assertNotNull(beneficiary);
      assertNotNull(beneficiary.id());
      assertEquals(Service.ID,        beneficiary.id(),        origin.id());
      assertEquals(Service.USERNAME,  beneficiary.username(),  origin.username());
      assertEquals(Service.VERIFIED,  beneficiary.verified(),  origin.verified());
      assertEquals(Service.STATUS,    beneficiary.enabled(),   origin.enabled());
      assertEquals(Service.FIRSTNAME, beneficiary.firstName(), origin.firstName());
      assertEquals(Service.LASTNAME,  beneficiary.lastName(),  origin.lastName());
      assertEquals(Service.EMAIL,     beneficiary.email(),     origin.email());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}