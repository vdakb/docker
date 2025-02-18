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

    Copyright © 2019. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   EndpointRequestProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointRequestProcessor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import java.util.Map;
import java.util.Timer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimerTask;
import java.util.Collection;

import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.ExecutorService;

import oracle.jdeveloper.connection.iam.service.EndpointService;

////////////////////////////////////////////////////////////////////////////////
// class EndpointRequestProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
public class EndpointRequestProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** This is a shared lock used for any scheduling or execution by all request
   ** processors.
   ** <p>
   ** Nothing long running should be done under this lock, it is simply used to
   ** protect access to <code>schedule</code> and other members in this class.
   */
  private final Lock              lock;

  /**
   ** The current scheduled tasks. They could be waiting to run, or running.
   ** This map is not thread safe, access to it must be done under the
   ** <code>schedule</code>
   */
  private final Map<String, Task> schedule = new HashMap<String, Task>();

  /**
   ** The current sumbitted (executing) tasks. They will either be executing or
   ** waiting to execute in the ExecutorService.
   ** <br>
   ** This collection is not thread safe, access to it must be done under the
   ** <code>schedule</code>
   */
  private final Collection<Run>   executing = new HashSet<Run>();

  private final boolean           privateExecutor;
  private final ExecutorService   executor;
  private final EndpointElement   node;

  private Timer                   timer;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Task
  // ~~~~~ ~~~~
  /**
   ** Subclass of TimerThread that executes the wrapped EndpointRunnable using
   ** {@link ExecutorService#submit(Runnable)} when run. If cancelled while the
   ** DBRunnable is executing it calls interrupt on the processor Thread running
   ** the DBRunnable using the Future from the ExecutorService.
   */
  private class Task extends TimerTask {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String key;
    private final Run    run;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a new {@link TimerTask}.
     ** <br>
     ** This constructor is protected to prevent other classes to use
     ** "new Timer()".
     **
     ** @param  key              the identifier of the timer task.
     ** @param  run              the task to execute.
     */
     Task(final String key, final Run run) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.key = key;
      this.run = run;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: propertyStorage (TimerTask)
    /**
     ** The action to be performed by this timer task.
     */
    @Override
    public void run() {
      // Note this runs on the Timer thread (owned by m_timer), not
      // a Processor thread (owned by m_executor or m_dbExecutor).
      EndpointRequestProcessor.this.lock.lock();
      try {
        // Check if we are still scheduled, and if we are execute
        final TimerTask scheduled = EndpointRequestProcessor.this.schedule.get(this.key);
        if (scheduled == this) {
          this.run.identifier = this.key;
          this.run.future     = EndpointRequestProcessor.this.executeInternal(this.run);
        }
      }
      catch (Exception e) {
        // This catch is important because if this method throws an
        // unchecked exception m_timer will be cancelled and stop accepting
        // tasks.
        //this.run.getLogger().log(Level.SEVERE, "task execution failed", e); //NOTRANS
        e.printStackTrace();
      }
      finally {
        EndpointRequestProcessor.this.lock.unlock();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: cancel (overridden)
    /**
     ** Cancels this timer task.
     ** <p>
     ** If the task has been scheduled for one-time execution and has not yet
     ** run, or has not yet been scheduled, it will never run. If the task has
     ** been scheduled for repeated execution, it will never run again. (If the
     ** task is running when this call occurs, the task will run to completion,
     ** but will never run again.)
     ** <p>
     ** <b>Note</b> that calling this method from within the <code>run</code>
     ** method of a repeating timer task absolutely guarantees that the timer
     ** task will not run again.
     ** <p>
     ** This method may be called repeatedly; the second and subsequent calls
     ** have no effect.
     **
     ** @return                  <code>true</code> if this task is scheduled for
     **                          one-time execution and has not yet run, or this
     **                          task is scheduled for repeated execution.
     **                          Returns <code>false</code> if the task was
     **                          scheduled for one-time execution and has
     **                          already run, or if the task was never
     **                          scheduled, or if the task was already
     **                          cancelled. (Loosely speaking, this method
     **                          returns <code>true</code> if it prevents one or
     **                          more scheduled executions from taking place.)
     */
    @Override
    public boolean cancel() {
      // Stop the runnable from starting if it is pending
      final boolean retval = super.cancel();
      this.run.cancel();
      return retval;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Run
  // ~~~~~ ~~~
  /**
   ** Specific Runnable implementation for a process that works against a given
   ** EndpointService. This class is used for scheduling work with
   ** EndpointRequestProcessor.
   */
  public abstract class Run implements Runnable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final EndpointService provider;
    private final String          name;

    /**
     ** identifier that we have been schedule with.
     ** <br>
     ** This is only set once the Task is executed (i.e. its delay has expired).
     */
    private String                identifier;

    /**
     ** when scheduled (rather than executed directly) this is {@link Future}
     ** for the execution of this runnable.
     */
    private Future<?>             future;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Create a new runnable that works against the given provider.
     **
     ** @param  provider          the connection provider.
     ** @param  name              a translated string describing the task for
     **                           displaying to the user in the status UI.
     */
    public Run(final EndpointService provider, final String name ) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.provider = provider;
      this.name     = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: provider
    /**
     ** Returns the {@link EndpointService} that this task is working against.
     **
     ** @return                  the provider, which can be <code>null</code>.
     */
    public final EndpointService provider() {
      return this.provider;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns a name for this task that can be displayed to the user in the
     ** status UI as the task is executed.
     **
     ** @return                 a translated name
     */
    public String getName() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: run
    /**
     ** Implementing interface <code>Runnable</code> to create a thread,
     ** starting the thread causes the object's <code>run</code> method to be
     ** called in that separately executing thread.
     ** <p>
     ** The general contract of the method <code>run</code> is that it may take
     ** any action whatsoever.
     */
    @Override
    public final void run() {
      if (this.provider.established()) {
        try {
          doWork();
        }
        catch (InterruptedException ie) {
          // If InterruptedException is caught it was because of an
          // interrupt, ensure that the thread's state is still interrupted.
          Thread.currentThread().interrupt();
        }
        finally {
          this.future = null;
          EndpointRequestProcessor.this.finished(this);
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: doWork
    /**
     ** Does the work for this Runnable.
     ** <br>
     ** Do not perform UI operations in this method, do the work against the
     ** provider, then if necessary schedule any relevant UI events using
     ** {@code SwingUtilities.invokeLater}.
     ** <p>
     ** Tasks are expected to be cancellable. If they are cancelled the thread
     ** will be interrupted. Any long running process should therefore check the
     ** thread's interrupted during execution and stop processing if it is
     ** <code>true</code>.
     ** <p>
     ** When using {@link #schedule} (rather than {@link #execute}) interrupt
     ** checking by a Task is important. If it takes too long for the
     ** interrupted process to stop, then the replacement Task scheduled
     ** afterwards might have already started (i.e. the scheduler does not wait
     ** for the existing runnable to stop). This means that if a shared object
     ** (e.g.a DBObject) is modified by this method then the interruption should
     ** always be checked before any modification to that object.
     **
     ** @throws InterruptedException if the work was interrupted and did not
     **                              complete. This is simply to allow
     **                              interruptable API calls to be made without
     **                              a catch in the doWork implementation, it is
     **                              not necessary to throw InterruptedException
     **                              when the thread has been interrupted.
     */
    protected abstract void doWork()
      throws InterruptedException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: cancelWork
    /**
     ** Called if the runnable is cancelled or interrupted.
     ** <br>
     ** If additional work is required to stop a task (beyond Thread interrupt)
     ** then override and implement in this method.
     ** <p>
     ** It is possible that {@link #doWork()} will not have been called when
     ** this method is called (if the task was scheduled but waiting for
     ** execution when it was cancelled). It is also possible that it could be
     ** called after {@link #doWork()} has finished.
     */
    protected void cancelWork() {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: cancel
    private void cancel() {
      // if it's in progress, this will interrupt the Thread that this Runnable
      // is executing on
      final Future<?> future = this.future;
      if (future != null && future.cancel(true)) {
        cancelWork();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new processor.
   **
   ** @param  executor           the executor service to use for any execution.
   **                            Can be <code>null</code> in which case a
   **                            default service is created.
   ** @param  node               the node this processor is for.
   ** @param  lock               the shared Lock to use for all scheduling.
   */
  public EndpointRequestProcessor(final ExecutorService executor, final EndpointElement node, final Lock lock) {
    // ensure inheritance
    super();

    // initialize instance state
    this.node            = node;
    this.privateExecutor = (executor == null);
    this.lock            = lock;
    if (this.privateExecutor) {
      // Default implementation is an interruptable, single threaded
      // processor.
      this.executor = null;//new RequestProcessor(getThreadName("_Processor"), 1, true);
    }
    else {
      this.executor = executor;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  qualify
  /**
   ** Returns the real scheduler key to use for the given key passed to schedule
   ** or cancel. The real key include the node name to ensure uniquess when the
   ** ExecutorService is shared between different EndpointRequestProcessor's
   ** (which happens with a db connection).
   */
  private String qualify(final String key) {
    // if the scheduler is shared (e.g. its the central db scheduler) then we
    // need to ensure that the key for scheduling is unique to this processor to
    // avoid different nodes' processors clashing.
    return key + '_' + this.node.getShortLabel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  execute
  /**
   ** Post the given runnable to the process queue for immediate execution.
   ** <br>
   ** The execution will happen off of the event thread, and only one process
   ** will execute at a time for a given Database.
   **
   ** @param  run                the runnable to execute.
   **
   ** @return                    a {@link Future} that can be used to track the
   **                            progress of the runnable, or <code>null</code> 
   **                            if this processor has been shutdown.
   */
  public Future<?> execute(final Run run) {
    this.lock.lock();
    try {
      return executeInternal(run);
    }
    finally {
      this.lock.unlock();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  schedule
  /**
   ** Schedules a given runnable to run after a delay.
   ** <br>
   ** If another runnable is scheduled with the same key before the delay has
   ** expired, the delay will simply be reset (i.e. the execution for a given
   ** key is always delayed until the last scheduled delay has expired).
   **
   ** @param  key                the key to schedule by.
   ** @param  run                the runnable to schedule after the delay.
   ** @param  delay              the delay to wait for before executing the
   **                            runnable.
   */
  public void schedule(final String key, final Run run, final int delay) {
//    logRequest("Scheduling " + key + " with delay " + delay); //NOTRANS
    final String qualified = qualify(key);
    final Task   task      = new Task(qualified, run);
    this.lock.lock();
    try {
      final TimerTask existing = this.schedule.put(qualified, task);
      if (existing != null) {
        existing.cancel();
      }
      timer().schedule(task, delay);
    }
    finally {
      this.lock.unlock();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  cancel
  /**
   ** Cancels a task that was previously scheduled with the given key.
   ** <br>
   ** If the task has been scheduled its Thread will be interrupted.
   **
   ** @param  key                the key for the task to cancel.
   **
   ** @see    #schedule(String, Run, int)
   */
  public void cancel(final String key) {
//    logRequest("Cancelling " + key);
    this.lock.lock();
    try {
      // remove any task from the schedule and cancel it
      // the task might be pending or executing.
      final String    qualify  = qualify(key);
      final TimerTask existing = this.schedule.remove(qualify);
      if (existing != null) {
        existing.cancel();
      }
    }
    finally {
      this.lock.unlock();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  shutdown
  /**
   ** Call to shutdown this processor, cancelling all running tasks and
   ** preventing any new ones being scheduled.
   */
  void shutdown() {
    if (this.privateExecutor) {
      // Only shutdown the executor if it is private to this processor.
      this.lock.lock();
      try {
        this.executor.shutdownNow();
      }
      finally {
        this.lock.unlock();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  finished
  /**
   ** Called by a Task when its doWork() method completes.
   ** <br>
   ** This ensures that the schedule is cleaned up.
   **
   ** @param  run                the Task that has just finished.
   */
  private void finished(final Run run) {
    // Note as this is called from Task.doWork the current Thread will be a
    // Processor thread. Even still we want to lock (not lockInterruptably)
    // because we need to ensure the Task is no longer cached in
    // schedule/executing (so it can be GC'd etc).
    this.lock.lock();
    try {
      this.executing.remove(run);
      final String key = run.identifier;
      if (key != null) {

        final Task existing = this.schedule.get(key);
        if (existing.run == run) {
          this.schedule.remove(key);
        }
      }

      if (this.schedule.isEmpty() && this.executing.isEmpty()) {
        // If this processor has nothing to do we notify the factory so
        // that it can be cleaned up if necessary.
//        DBRequestProcessorFactory.getInstance().notifyProcessorEmpty(m_node, this);
      }
    }
    finally {
      this.lock.unlock();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  executeInternal
  /**
   ** Submits the given runnable for execution with the underlying service. It
   ** is only safe to call this method while under a schedule lock.
   **
   ** @param  run                the runnable to execute.
   **
   ** @return                    a {@link Future} that can be used to track the
   **                            progress of the runnable, or <code>null</code> 
   **                            if this processor has been shutdown.
   */
  private Future<?> executeInternal(final Run run) {
    Future<?> retval = null;
    if (!this.executor.isShutdown()) {
      this.executing.add(run);
      retval = this.executor.submit(run);
    }
    return retval;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  timer
  /**
   ** Returns the {@link Timer} that all RequestProcessors use to delay
   ** execution when schedule is called. The tasks scheduled with this
   ** {@link Timer} by EndpointRequestProcessor are never long running, they
   ** simply call execute with the scheduled task so we can use a single Timer
   ** for all processors.
   ** <p>
   ** Must be called under the schedule lock.
   */
  Timer timer() {
    if (this.timer == null) {
      this.timer = new Timer(getClass().getSimpleName() + "_Timer", true);
    }
    return this.timer;
  }
}
