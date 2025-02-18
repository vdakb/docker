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

    File        :   TestAccount.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestAccount.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.integration;

import org.junit.runner.Result;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;

import org.junit.runner.notification.Failure;

import oracle.iam.junit.keycloak.integration.account.Search;
import oracle.iam.junit.keycloak.integration.account.Create;
import oracle.iam.junit.keycloak.integration.account.Delete;
import oracle.iam.junit.keycloak.integration.account.Resolve;
import oracle.iam.junit.keycloak.integration.account.RoleAssign;
import oracle.iam.junit.keycloak.integration.account.RoleRevoke;

////////////////////////////////////////////////////////////////////////////////
// class TestAccount
// ~~~~~ ~~~~~~~~~~~
/**
 ** The test suite to apply CRUD operations on users at the target system
 ** leveraging the connector bundle deployed on a
 ** <code>Java Connector Server</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({Create.class, Search.class, Resolve.class, RoleAssign.class, RoleRevoke.class, Delete.class})
public class TestAccount {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestAccount</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestAccount() {
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
    final String[] parameter = { TestAccount.class.getName() };
    final Result   result    = JUnitCore.runClasses(TestAccount.class);
    for (Failure failure : result.getFailures()) {
      TestBaseIntegration.CONSOLE.error("main", failure.toString());
    }
    TestBaseIntegration.CONSOLE.info("" + result.wasSuccessful());
  }
}