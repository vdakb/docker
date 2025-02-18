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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   TestTenantDelete.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestTenantDelete.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.junit.gws.integration.gae;

import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import oracle.iam.identity.connector.service.AttributeFactory;

import org.identityconnectors.framework.api.ConnectorFacade;

import org.identityconnectors.framework.common.exceptions.ConnectorException;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// class TestTenantDelete
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The test case to delete a tenant in the target system leveraging the
 ** <code>Connector Server</code> facade.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class TestTenantDelete extends TestTenant {

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
   ** @throws TaskException      if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      final ConnectorFacade facade = Network.facade(Network.localnet());
      facade.delete(TENANT, AttributeFactory.uid(T3.getUidValue()), null);
      facade.delete(TENANT, AttributeFactory.uid(T2.getUidValue()), null);
      facade.delete(TENANT, AttributeFactory.uid(T1.getUidValue()), null);
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be split by a "::" character sequence
      final String   message = e.getLocalizedMessage();
      final String[] parts   = message.split("::");
      if (parts.length > 1)
        System.out.println(parts[0].concat("::").concat(parts[1]));
      else
        System.out.println(message);
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}