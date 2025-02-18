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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic Database Connector

    File        :   TestException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.connector.dbs;

import java.util.Map;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.dbms.DatabaseException;

import oracle.iam.identity.icf.foundation.SystemMessage;

import oracle.iam.identity.icf.foundation.logging.TableFormatter;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

import oracle.iam.identity.icf.foundation.utility.StringUtility;

import oracle.iam.identity.icf.connector.DatabaseContext;

import oracle.jdbc.driver.OracleDriver;

////////////////////////////////////////////////////////////////////////////////
// class TestException
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** The test case to test excpation handling with the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestException {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final DatabaseContext context = Network.intranet();
    try {
      DatabaseException.installError(OracleDriver.class.getPackage());
      final TableFormatter table  = new TableFormatter()
      .header(SystemBundle.string(SystemMessage.ATTRIBUTE_NAME))
      .header(SystemBundle.string(SystemMessage.ATTRIBUTE_VALUE))
      ;
      Map<Integer, String> error = DatabaseException.of(OracleDriver.class.getPackage());
      for (Map.Entry<Integer, String> cursor : error.entrySet())
        StringUtility.formatValuePair(table, String.valueOf(cursor.getKey()), cursor.getValue());
      final StringBuilder buffer = new StringBuilder();
      table.print(buffer);
      System.out.println(buffer.toString());
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}