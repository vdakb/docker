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
    Subsystem   :   Openfire Database Connector

    File        :   TestGroupRevoke.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernetg@oracle.com

    Purpose     :   This file implements the interface
                    TestGroupRevoke.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-13-06  DSteding    First release version
*/

package oracle.iam.junit.dbs.integration.ofs;

import org.junit.Test;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.junit.dbs.integration.ofs.TestUser.bkbk4711123;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupRevoke
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set from the target system
 ** leveraging the <code>Connector Server</code> facade.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGroupRevoke extends TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123orclGroupWritePrivilegeGroup
  /**
   ** Test that accounts could be removed from group
   ** orclGroupWritePrivilegeGroup.
   */
  @Test
  public void bkbk4711123orclGroupWritePrivilegeGroup() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, TestGroup.orclGroupWritePrivilegeGroup.GRANT, null);
      System.out.println(uid);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123orclGroupReadPrivilegeGroup
  /**
   ** Test that accounts could be removed from group
   ** orclGroupReadPrivilegeGroup.
   */
  @Test
  public void bkbk4711123orclGroupReadPrivilegeGroup() {
    try {
      final Uid uid = Network.facade(Network.intranet()).removeAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, TestGroup.orclGroupReadPrivilegeGroup.GRANT, null);
      System.out.println(uid);
    }
    catch (TaskException e) {
      failed(e);
    }
  }

}