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
    Subsystem   :   Atlassian Jira Connector

    File        :   TestGroupAssign.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernetg@oracle.com

    Purpose     :   This file implements the interface
                    TestGroupAssign.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-19-05  SBernet     First release version
*/

package oracle.iam.junit.gws.integration.ajs;

import org.junit.Test;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.junit.gws.integration.ajs.TestUser.bkbk4711123;

////////////////////////////////////////////////////////////////////////////////
// class TestGroupAssign
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set from the target system
 ** leveraging the <code>Connector Server</code> facade.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestGroupAssign extends TestUser {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bkbk4711123jiraCoreUserAssign
  /**
   ** Test that accounts could be assigned to Jira Core User.
   */
	@Test
	public void bkbk4711123jiraCoreUserAssign() {
    try {
      final Uid uid = Network.facade(Network.intranet()).addAttributeValues(ObjectClass.ACCOUNT, bkbk4711123.UID, TestGroup.jiraCoreUser.grant(), null);
      System.out.println(uid);
    }
    catch (ConnectorException e) {
      failed(e);
    }
    catch (TaskException e) {
      failed(e);
    }
  }
}