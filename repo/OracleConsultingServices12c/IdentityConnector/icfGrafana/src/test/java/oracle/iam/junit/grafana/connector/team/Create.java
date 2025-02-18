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

    File        :   Create.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Create.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.junit.grafana.connector.team;

import java.io.File;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.object.Pair;

import oracle.iam.identity.icf.connector.grafana.schema.Team;
import oracle.iam.identity.icf.connector.grafana.schema.Entity;

import oracle.iam.junit.grafana.connector.TestFixture;

////////////////////////////////////////////////////////////////////////////////
// class Create
// ~~~~~ ~~~~~~
/**
 ** The test case create operation on teams at the target system leveraging the
 // connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Create extends TestFixture {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Create</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Create() {
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
    final String[] parameter = {Create.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saxonyAnhalt
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void saxonyAnhalt() {
    create(Tenant.AN);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brandenburg
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Brandenburg</b>
   */
  @Test
  public void brandenburg() {
    create(Tenant.BB);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   federalOffice
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void federalOffice() {
    create(Tenant.BK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   federalPolice
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void federalPolice() {
    create(Tenant.BP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saxony
  /**
   ** Test create request leveraging server context.
   ** <p>
   ** <b>Saxony</b>
   */
  @Test
  public void saxony() {
    create(Tenant.SN);
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Test create request leveraging server context.
   */
  protected void create(final Pair<String, File> identity) {
    final Team origin = team(identity.value);
    assertNotNull(origin);
    assertEquals(Entity.NAME,  origin.name(), identity.tag);
    try {
      final String identifier = CONTEXT.createTeam(origin);
      assertNotNull(identifier);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}