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
    Subsystem   :   Generic Directory Connector

    File        :   TestAccountChangeLog.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestAccountChangeLog.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import org.identityconnectors.framework.common.objects.SyncToken;
import org.identityconnectors.framework.common.objects.SyncDelta;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.SyncResultsHandler;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.connector.DirectoryEndpoint;
import oracle.iam.identity.icf.connector.DirectorySynchronization;

////////////////////////////////////////////////////////////////////////////////
// class TestAccountChangeLog
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set of accounts from the target
 ** system by accessing the change log.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestAccountChangeLog extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements SyncResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Handler() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (SyncResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SyncApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final SyncDelta object) {
      CONSOLE.info(object.getUid() + "::" + object.getObject());
      return true;
    }
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
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    // tweak the configuration to use chnageLog synchronization
    PEOPLEOPTS.getOptions().put(OperationContext.SYNCHRONIZE, OperationContext.Synchronization.CHANGELOG.value());
    try {
      final DirectoryEndpoint        endpoint = Network.extranet();
      final DirectorySynchronization sync     = DirectorySynchronization.build(endpoint, ObjectClass.ACCOUNT, PEOPLEOPTS.build());
      final SyncToken                latest   = sync.lastToken();
      sync.execute(latest, new Handler());
    }
    catch (SystemException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
    }
  }
}