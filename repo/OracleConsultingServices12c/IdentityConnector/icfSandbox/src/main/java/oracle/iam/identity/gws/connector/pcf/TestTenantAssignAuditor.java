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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   TestTenantAssignAuditor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestTenantAssignAuditor.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gws.connector.pcf;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.pcf.RestContext;

import oracle.iam.identity.icf.connector.pcf.rest.domain.Result;

////////////////////////////////////////////////////////////////////////////////
// class TestTenantAssignAuditor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to assign a tenant resource in the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestTenantAssignAuditor {

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
    try {
      final RestContext context  = Network.cloudContext(Network.intranet());
      final Result.User response = context.assign("4bd93aee-1236-4af8-bee2-9fcea298fc75", "e64a84b4-5c11-4ff8-8bfa-7bb17213f545", "audited_organizations");
      System.out.println(response.metadata().guid());
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}