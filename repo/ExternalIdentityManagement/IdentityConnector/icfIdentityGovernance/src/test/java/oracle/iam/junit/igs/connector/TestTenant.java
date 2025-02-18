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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Identity Governance Service SCIM

    File        :   TestTenant.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestTenant.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2022-06-11  DSteding    First release version
*/

package oracle.iam.junit.igs.connector;

import org.junit.runner.Result;
import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import org.junit.runner.JUnitCore;

import org.junit.runner.notification.Failure;

import oracle.iam.junit.igs.connector.tenant.Search;
import oracle.iam.junit.igs.connector.tenant.Lookup;
import oracle.iam.junit.igs.connector.tenant.Create;
import oracle.iam.junit.igs.connector.tenant.Modify;
import oracle.iam.junit.igs.connector.tenant.Assign;
import oracle.iam.junit.igs.connector.tenant.Revoke;
import oracle.iam.junit.igs.connector.tenant.Delete;

////////////////////////////////////////////////////////////////////////////////
// class TestTenant
// ~~~~~ ~~~~~~~~~~
/**
 ** The test suite to apply CRUD operations on tenants at the target system
 ** leveraging the connector implementation directly.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({Search.class,Lookup.class,Create.class,Modify.class,Assign.class,Revoke.class,Delete.class})
public class TestTenant {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestTenant</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestTenant() {
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
    final String[] parameter = { TestTenant.class.getName() };
    final Result   result    = JUnitCore.runClasses(TestSuite.class);
    for (Failure failure : result.getFailures()) {
      Network.CONSOLE.error("main", failure.toString());
    }
    Network.CONSOLE.info("" + result.wasSuccessful());
  }
}