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
    Subsystem   :   Atlassian Jira Connector

    File        :   TestProjectRoleSearch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the interface
                    TestProjectRoleSearch.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-19-05  SBernet     First release version
*/

package oracle.iam.identity.gws.integration.jira;

import java.util.Set;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.icf.connector.jira.Marshaller;

import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
import org.identityconnectors.framework.common.objects.ResultsHandler;
////////////////////////////////////////////////////////////////////////////////
// class TestProjectRoleSearch
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to fetch a paginated result set from the target system
 ** leveraging the <code>Connector Server</code> facade.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestProjectRoleSearch extends TestCase {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  private static class Handler implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    private Handler() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object             each object return from the search.
     **
     ** @return                    <code>true</code> if we should keep processing;
     **                            otherwise <code>false</code> to cancel.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      System.out.println(object);
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
   ** @throws TaskException      if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    final Set<String>             target = CollectionUtility.set("__UID__", "__NAME__");
    final OperationOptionsBuilder option = new OperationOptionsBuilder().setAttributesToGet(target);
    option.getOptions().put(BATCH_SIZE_OPTION, new Integer(500));
    try {
      final ServiceResource resource = Network.extranet();
      final ConnectorFacade facade   = Network.facade(resource);
      facade.search(Marshaller.PROJECT, null, new Handler(), option.build());
    }
    catch (ConnectorException e) {
      // the exception thrown provides a special format in the message
      // containing a error code and a detailed text
      // this message needs to be speilt by an "::" chanracter sequence
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
