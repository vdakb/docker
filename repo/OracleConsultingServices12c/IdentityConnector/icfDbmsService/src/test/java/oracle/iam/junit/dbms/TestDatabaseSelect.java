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

    System      :   Oracle Identity Manager UnitTest Library
    Subsystem   :   Generic Database Connector

    File        :   TestDatabaseSelect.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestDatabaseSelect.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbms;

import java.util.Map;
import java.util.List;

import org.junit.Test;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.dbms.DatabaseFilter;
import oracle.iam.identity.icf.dbms.DatabaseSelect;
import oracle.iam.identity.icf.dbms.DatabaseEntity;
import oracle.iam.identity.icf.dbms.DatabaseAttribute;

////////////////////////////////////////////////////////////////////////////////
// class TestDatabaseSelect
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>Network</code> implements the environment functionality for a
 ** Test Case.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestDatabaseSelect extends TestTransaction {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @Test
  public void test() {
    final DatabaseAttribute name      = DatabaseAttribute.build("name"     , "name");
    final DatabaseAttribute value     = DatabaseAttribute.build("propvalue", "value");
    final DatabaseAttribute encrypted = DatabaseAttribute.build("encrypted", "encrypted");
    final DatabaseAttribute vector    = DatabaseAttribute.build("iv",        "vector");
    final DatabaseEntity    entity    = DatabaseEntity.build("ofsowner", "ofproperty", name);
    try {
      final DatabaseSelect             select = DatabaseSelect.build(Network.CONSOLE, entity, DatabaseFilter.NOP, CollectionUtility.list(name, value, encrypted, vector));
      final List<Map<String, Object>> result = select.execute(this.context.unwrap(), this.context.endpoint().timeOutResponse());
      notNull(result);
    }
    catch (SystemException e) {
      failed(e);
    }
  }
}