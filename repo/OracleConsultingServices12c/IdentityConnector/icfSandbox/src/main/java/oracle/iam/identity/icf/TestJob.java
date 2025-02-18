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

    File        :   TestJob.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestJob.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobParameter;
import oracle.iam.scheduler.vo.ScheduledTask;

import oracle.iam.scheduler.api.SchedulerService;

import oracle.hst.foundation.SystemConsole;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

public class TestJob {

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
      final SchedulerService service = server.service(SchedulerService.class);
      try {
        // at first ask the task definition to get the parameters for
        // fulfillment this step is required due to the fact that the task
        // definition can be change out-of-band to the jobs base on that task
        final ScheduledTask             task       = service.lookupScheduledTask("PCF Lookup Reconciliation");
        final Map<String, JobParameter> definition = task.getParameters();
        // secondary ask the job definition to get the parameters for
        // fulfillment this step is required due to the fact that the job
        // instance can have parameters that needs to be changed accordingly to
        // new or drop parameters in the task definition
        final JobDetails                details = service.getJobDetail("PCF Production Group Reconciliation");
        // may be we have a new one hance there cannot be any parameter at the
        // time being than create a empty map to avoid NPE
        final Map<String, JobParameter> runtime = details == null ? new HashMap<String, JobParameter>() : details.getParams();
        // for testing purpost fake a parameter to delete
        runtime.put("To Delete", new JobParameter());
        // merge the keys of the obtained parameters to a unqiue set
        final List<String> sorted  = CollectionUtility.sortedList(CollectionUtility.union(definition.keySet(), runtime.keySet()));
        // now walk through the parameter definition an declare the state of
        // each parameter found in the runtime
        // 1. if a parameter is define in the definition set and not found in
        //    the set of runtime parameters it must be new.
        // 2. if a parameter is not existing in the definition set and but found
        //    in the set of runtime parameters it must be deleted.
        // 3. if a parameter with the same name s found in bothe sets the value
        //    to use is the value of the runtime set
        for (String cursor : sorted) {
          if (definition.keySet().contains(cursor) && !runtime.keySet().contains(cursor)) {
            System.out.println("Parameter added: " + cursor);
          }
          else if (!definition.keySet().contains(cursor) && runtime.keySet().contains(cursor)) {
            System.out.println("Parameter deleted: " + cursor);
          }
          else {
            System.out.println("Parameter value set: " + cursor);
          }
        }
      }
      catch (Exception e) {
        throw TaskException.unhandled(e);
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
