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

    File        :   Delete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Delete.


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
// class Delete
// ~~~~~ ~~~~~~
/**
 ** The test case delete operation on identities at the target system leveraging
 ** the connector implementation directly.
 **
 ** @author  dieter.steding@icloud.com
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
  // Method:   cambrault
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Gerald Cambrault</b>
   */
  @Test
  public void cambrault() {
    delete(Identity.CAMBRAULT.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   musterfrau
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Agathe Musterfrau</b>
   */
  @Test
  public void musterfrau() {
    delete(Identity.MUSTERFRAU.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustermann
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Max Mustermann</b>
   */
  @Test
  public void mustermann() {
    delete(Identity.MUSTERMANN.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zitterbacke
  /**
   ** Test delete request leveraging server context.
   ** <p>
   ** <b>Alfons Zitterbacke</b>
   */
  @Test
  public void zitterbacke() {
    delete(Identity.ZITTERBACKE.tag);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Test delete request leveraging server context.
   */
  protected void delete(final String identity) {
    try {
      final User target = CONTEXT.resolveUser(identity);
      assertNotNull(target);
      assertNotNull(target.id());
      CONTEXT.deleteUser(target.id());
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}