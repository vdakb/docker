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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   TestCount.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestCount.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.dbs;

import oracle.iam.system.simulation.dbs.DatabaseCount;
import oracle.iam.system.simulation.dbs.DatabaseException;

import oracle.iam.system.simulation.efbs.v2.schema.Account;

////////////////////////////////////////////////////////////////////////////////
// class TestCount
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>TestCount</code> is unit testing the count capabilities of the
 ** implementation by directly operationg at the persistence layer.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestCount extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Entry point to the unit test.
   **
   ** @param  args               the array ouf command line arguments passed in.
   */
  public static void main(final String[] args) {
    final TestCase       test   = new TestDelete();
    final DatabaseCount  count  = DatabaseCount.build(Account.ENTITY);
    try {
      test.connect(Network.intranet());
      System.out.println(count.execute(test.connection));
    }
    catch (DatabaseException e) {
      e.printStackTrace();
    }
    finally {
      test.disconnect();
    }
  }
}