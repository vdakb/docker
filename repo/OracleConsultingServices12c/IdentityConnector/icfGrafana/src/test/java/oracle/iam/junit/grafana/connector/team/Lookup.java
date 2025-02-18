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

    File        :   Lookup.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Lookup.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector.team;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Entity;

import oracle.iam.junit.grafana.connector.TestFixture;
import oracle.iam.junit.grafana.connector.TestFixture.Tenant;

////////////////////////////////////////////////////////////////////////////////
// class Lookup
// ~~~~~ ~~~~~~
/**
 ** The test case retrieve operation on identities at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
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
  // Method:   saxonyAnhalt
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void saxonyAnhalt() {
    lookup(Tenant.AN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brandenburg
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Brandenburg</b>
   */
  @Test
  public void brandenburg() {
    lookup(Tenant.BB.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   federalOffice
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void federalOffice() {
    lookup(Tenant.BK.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   federalPolice
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void federalPolice() {
    lookup(Tenant.BP.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saxony
  /**
   ** Test lookup request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void saxony() {
    lookup(Tenant.SN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** Test create request leveraging server context.
   */
  protected void lookup(final String identity) {
    try {
      final Team origin = CONTEXT.resolveTeam(identity);
      assertNotNull(origin);
      assertEquals(Entity.NAME,   origin.name(), identity);

      final Team target = CONTEXT.lookupTeam(origin.id());
      assertNotNull(target);
      assertNotNull(target.id());
      assertEquals(Entity.ID,   target.id(),   origin.id());
      assertEquals(Entity.NAME, target.name(), origin.name());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}