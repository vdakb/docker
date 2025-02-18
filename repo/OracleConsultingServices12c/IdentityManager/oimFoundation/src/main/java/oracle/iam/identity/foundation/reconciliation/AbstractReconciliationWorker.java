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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Scheduler Facilities

    File        :   AbstractReconciliationWorker.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractReconciliationWorker.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2008-03-17  DSteding    First release version
*/

package oracle.iam.identity.foundation.reconciliation;

import java.util.List;
import java.util.Map;

import java.util.concurrent.Callable;

import javax.security.auth.login.LoginException;

import oracle.hst.foundation.SystemWatch;
import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractSchedulerWorker;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractReconciliationWorker
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractReconciliationWorker</code> implements the base
 ** functionality of a service end point for the Oracle Identity Manager
 ** Scheduler dedicated to reconciliation tasks.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractReconciliationWorker extends    AbstractSchedulerWorker
                                                   implements Callable<AbstractReconciliationWorker.Summary> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the target object of the operation */
  private final String                    reconcileObject;

  /** the mapping of data an subject of reconciliation may have */
  private final List<Map<String, Object>> bulk;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractReconciliationWorker</code> which is associated
   ** with the specified task.
   **
   ** @param  logger             the {@link Logger} this
   **                            {@link AbstractSchedulerWorker} use for logging purpose.
   ** @param  reconcileObject    what is the target of the
   **                            {@link AbstractSchedulerWorker}.
   ** @param  bulk               the {@link List} of entries to process by this
   **                            {@link AbstractSchedulerWorker}.
   **                            Any element <code>bulk</code> is a {@link Map}
   **                            of key/value pairs.
   ** @param  watch              the {@link SystemWatch} to collect the
   **                            performance metrics.
   **
   ** @throws LoginException     if there is an error during login.
   ** @throws TaskException      if implementing subclass is not able to
   **                            initialize correctly.
   */
  public AbstractReconciliationWorker(final Logger logger, final String reconcileObject, final List<Map<String, Object>> bulk, final SystemWatch watch)
    throws LoginException
    ,      TaskException {

    // ensure inheritance
    super(logger, watch);

    // initialize instance
    this.bulk            = bulk;
    this.reconcileObject = reconcileObject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   call (Callable)
  /**
   ** When an object implementing interface <code>Callable</code> is used to
   ** create a thread, starting the thread causes the object's <code>call</code>
   ** method to be called in that separately executing thread.
   ** <p>
   ** The general contract of the method <code>call</code> is that it may take
   ** any action whatsoever.
   **
   ** @return                    the computed result {@link Summary}.
   **
   ** @throws Exception          if unable to compute a result.
   */
  @Override
  public Summary call()
    throws Exception {

    try {
      run();
    }
    catch (Throwable t) {
      fatal("call", t);
    }
    return this.summary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract ase classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution
  /**
   ** The entry point of the scheduled task to perform.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  public void onExecution()
    throws TaskException {

    final String method = "onExecution";
    trace(method, SystemMessage.METHOD_ENTRY);
    info(TaskBundle.format(TaskMessage.WILLING_TO_CHANGE, String.valueOf(this.bulk.size())));
    try {
      info(TaskBundle.format(TaskMessage.RECONCILE_BEGIN, this.reconcileObject));
      for (Map<String, Object> subject : this.bulk) {
        try {
          processSubject(subject);
        }
        catch (TaskException e) {
          error(method, TaskBundle.format(TaskError.GENERAL, e));
        }
        catch (Throwable e) {
          fatal(method, e);
        }
      }
    }
    finally {
      info(TaskBundle.format(TaskMessage.ABLE_TO_CHANGE, this.summary.asStringArray()));
      info(TaskBundle.format(TaskMessage.RECONCILE_COMPLETE, this.reconcileObject));
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processSubject
  /**
   ** Do all action which should take place for reconciliation for a particular
   ** subject.
   **
   ** @param  subject            the {@link Map} to reconcile.
   **
   ** @throws TaskException      if an error occurs processing data.
   */
  protected abstract void processSubject(final Map<String, Object> subject)
    throws TaskException;
}