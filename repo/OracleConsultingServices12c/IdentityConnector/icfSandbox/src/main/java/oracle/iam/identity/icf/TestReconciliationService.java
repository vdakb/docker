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
    Subsystem   :   Implementation Security Test Case

    File        :   TestReconciliationService.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestReconciliationService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf;

import oracle.iam.reconciliation.config.vo.Profile;

import oracle.iam.reconciliation.api.ReconConfigService;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.reconciliation.config.vo.TargetAttribute;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliationService
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestReconciliationService</code> checks the behavior of the
 ** reconcilaition descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliationService {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The console used for logging purpose */
  static final SystemConsole CONSOLE    = new SystemConsole("icf");

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
    IdentityServer server = null;
    try {
      server = IdentityManager.server(IdentityManager.intranet());
      server.connect();
      
      Profile                  profile = null;
      final ReconConfigService facade  =  server.service(ReconConfigService.class);
      try {
        profile = facade.getProfile("PCF Account Production");
      }
      catch (Exception e) {
        throw TaskException.unhandled(e);
      }
      if (profile != null && profile.getForm() != null) {
        final String            objectName = profile.getName();
        final TargetAttribute[] attribute  = profile.getForm().getTargetAttributes();
        System.out.println(attribute);
      }
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
    finally {
      if (server != null)
        server.disconnect();
    }
  }
}
