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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Scheduler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Scheduler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.Objects;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ScheduledExecutorService;

////////////////////////////////////////////////////////////////////////////////
// interface Scheduler
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** A scheduler that submits a task to an executor after a given delay.
 */
public interface Scheduler {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // enum DisabledFuture
  // ~~~~ ~~~~~~~~~~~~~~
  /**
   ** A {@link Future} implementation that does nothing.
   */
  enum DisabledFuture implements Future<Void> {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: cancel (Future)
    /**
     ** Attempts to cancel execution of this task.
     ** <br>
     ** This attempt will fail if the task has already completed, has already
     ** been cancelled, or could not be cancelled for some other reason. If
     ** successful, and this task has not started when {@code cancel} is called,
     ** this task should never run.
     ** <br>
     ** If the task has already started, then the <code>interrupt</code>
     ** parameter determines whether the thread executing this task should be
     ** interrupted in an attempt to stop the task.
     ** <p>
     ** After this method returns, subsequent calls to {@link #isDone} will
     ** always return <code>true</code>. Subsequent calls to
     ** {@link #isCancelled} will always return <code>true</code> if this method
     ** returned <code>true</code>.
     **
     ** @param  interrupt        <code>true</code> if the thread executing this
     **                          task should be interrupted; otherwise,
     **                          in-progress tasks are allowed to complete.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  <code>false</code>if the task could not be
     **                          cancelled, typically because it has already
     **                          completed normally; otherwise
     **                          <code>true</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean cancel(final boolean interrupt) {
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isCancelled (Future)
    /**
     ** Returns <code>true</code> if this task cancelled before it completed
     ** normally.
     **
     ** @return                  <code>true</code> if this task was cancelled
     **                          before it completed.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isCancelled() {
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isDone (Future)
    /**
     ** Returns <code>true</code> if this task completed.
     ** <br>
     ** Completion may be due to normal termination, an exception, or
     ** cancellation -- in all of these cases, this method will return
     ** <code>true</code>.
     **
     ** @return                  <code>true</code> if this task completed.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean isDone() {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (Future)
    /**
     ** Waits if necessary for the computation to complete, and then retrieves
     ** its result.
     **
     ** @return                  the computed result.
     **                          <br>
     **                          Possible object is {@link Void}}.
     **
     ** @throws ExecutionException    if the computation threw an exception.
     ** @throws InterruptedException  if the current thread was interrupted
     **                               while waiting.
     ** @throws CancellationException if the computation was cancelled.
     */
    @Override
    public final Void get()
      throws ExecutionException
      ,      InterruptedException
      ,      CancellationException {

      return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (Future)
    /**
     ** Waits if necessary for at most the given time for the computation to
     ** complete, and then retrieves its result, if available.
     **
     ** @param  timeout          the maximum time to wait.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  unit             the time unit of the timeout argument.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  the computed result.
     **                          <br>
     **                          Possible object is {@link Void}}.
     **
     ** @throws TimeoutException      if the wait timed out.
     ** @throws ExecutionException    if the computation threw an exception.
     ** @throws InterruptedException  if the current thread was interrupted
     **                               while waiting.
     ** @throws CancellationException if the computation was cancelled.
     */
    @Override
    public final Void get(final long timeout, final TimeUnit unit)
      throws TimeoutException
      ,      ExecutionException
      ,      InterruptedException
      ,      CancellationException {

      Objects.requireNonNull(unit);
      return null;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Disabled
  // ~~~~ ~~~~~~~~
  /**
   ** A {@link Scheduler} implementation that always returns a successfully
   ** completed future.
   */
  enum Disabled implements Scheduler {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schedule (Scheduler)
    /**
     ** Returns a {@link Future} that will submit the task to the given executor
     ** after the given delay.
     **
     ** @param  executor         the {@link Executor} to run the task.
     **                          <br>
     **                          Allowed object is {@link Executor}.
     ** @param  task             the runnable task to schedule.
     **                          <br>
     **                          Allowed object is {@link Runnable}.
     ** @param  delay            how long to delay, in units of
     **                          <code>unit</code>.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  unit             a {@link TimeUnit} determining how to
     **                          interpret the <code>delay</code> parameter.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  a scheduled future representing pending
     **                          submission of the task.
     **                          <br>
     **                          Possible object is {@link Future}.
     */
    @Override
    public final Future<?> schedule(final Executor executor, final Runnable task, final long delay, final TimeUnit unit) {
      Objects.requireNonNull(executor);
      Objects.requireNonNull(task);
      Objects.requireNonNull(unit);
    return DisabledFuture.INSTANCE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Guarded
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** A {@link Scheduler} implementation that suppresses and logs any exception
   ** thrown by the delegate <code>scheduler</code>.
   */
  static final class Guarded implements Scheduler {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Logger logger   = Logger.getLogger(Guarded.class.getName());

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Scheduler             delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Guarded</code> <code>Scheduler</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Guarded()" and enforces use of the public method below.
     **
     ** @param  delegate         the counter.
     **                          <br>
     **                          Allowed object is <code>Scheduler</code>.
     */
    private Guarded(final Scheduler delegate) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schedule (Scheduler)
    /**
     ** Returns a {@link Future} that will submit the task to the given executor
     ** after the given delay.
     **
     ** @param  executor         the {@link Executor} to run the task.
     **                          <br>
     **                          Allowed object is {@link Executor}.
     ** @param  task             the runnable task to schedule.
     **                          <br>
     **                          Allowed object is {@link Runnable}.
     ** @param  delay            how long to delay, in units of
     **                          <code>unit</code>.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  unit             a {@link TimeUnit} determining how to
     **                          interpret the <code>delay</code> parameter.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  a scheduled future representing pending
     **                          submission of the task.
     **                          <br>
     **                          Possible object is {@link Future}.
     */
    @Override
    public final Future<?> schedule(final Executor executor, final Runnable task, final long delay, final TimeUnit unit) {
      try {
        final Future<?> future = this.delegate.schedule(executor, task, delay, unit);
        return (future == null) ? DisabledFuture.INSTANCE : future;
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by scheduler; discarded task", t);
        return DisabledFuture.INSTANCE;
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum System
  // ~~~~ ~~~~~~~~
  /**
   ** A {@link Scheduler} implementation that uses the system-wide scheduling
   ** thread by using {@code CompletableFuture#delayedExecutor}.
   */
  enum System implements Scheduler {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    static final Method delayedExecutor = delayedExecutor();

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schedule (Scheduler)
    /**
     ** Returns a {@link Future} that will submit the task to the given executor
     ** after the given delay.
     **
     ** @param  executor         the {@link Executor} to run the task.
     **                          <br>
     **                          Allowed object is {@link Executor}.
     ** @param  task             the runnable task to schedule.
     **                          <br>
     **                          Allowed object is {@link Runnable}.
     ** @param  delay            how long to delay, in units of
     **                          <code>unit</code>.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  unit             a {@link TimeUnit} determining how to
     **                          interpret the <code>delay</code> parameter.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  a scheduled future representing pending
     **                          submission of the task.
     **                          <br>
     **                          Possible object is {@link Future}.
     */
    @Override
    public final Future<?> schedule(final Executor executor, final Runnable task, final long delay, final TimeUnit unit) {
      Objects.requireNonNull(executor);
      Objects.requireNonNull(task);
      Objects.requireNonNull(unit);
      try {
        Executor delayed = (Executor)delayedExecutor.invoke(CompletableFuture.class, delay, unit, executor);
        return CompletableFuture.runAsync(task, delayed);
      }
      catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: present
    /**
     ** Returns whether a system-wide scheduling thread by using
     ** {@link CompletableFuture#delayedExecutor} is available.
     **
     ** @return                  <code>true</code> if a system-wide scheduling
     **                          thread by using
     **                          {@link CompletableFuture#delayedExecutor} is
     **                          available.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    static boolean present() {
      return (delayedExecutor != null);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: delayedExecutorMethod
    /**
     ** Determines if the <code>delayedExecutor</code> {@link Method} of the
     ** system-wide scheduling.
     **
     ** @return                the <code>delayedExecutor</code> {@link Method}
     **                        of the system-wide scheduling service;
     **                        <code>null</code> otherwise.
     */
    static Method delayedExecutor() {
      try {
        return CompletableFuture.class.getMethod("delayedExecutor", long.class, TimeUnit.class, Executor.class);
      }
      catch (NoSuchMethodException | SecurityException e) {
        return null;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Default
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** A thread-safe {@link Scheduler} implementation that delegates to the a
   ** {@link ScheduledExecutorService}.
   */
  static final class Default implements Scheduler {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Logger    logger   = Logger.getLogger(Default.class.getName());

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final ScheduledExecutorService service;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Default</code> <code>Scheduler</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Default()" and enforces use of the public method below.
     **
     ** @param  service          the service.
     **                          <br>
     **                          Allowed object is
     **                          {@link ScheduledExecutorService}.
     */
    private Default(final ScheduledExecutorService service) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.service = service;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: schedule (Scheduler)
    /**
     ** Returns a {@link Future} that will submit the task to the given executor
     ** after the given delay.
     **
     ** @param  executor         the {@link Executor} to run the task.
     **                          <br>
     **                          Allowed object is {@link Executor}.
     ** @param  task             the runnable task to schedule.
     **                          <br>
     **                          Allowed object is {@link Runnable}.
     ** @param  delay            how long to delay, in units of
     **                          <code>unit</code>.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     ** @param  unit             a {@link TimeUnit} determining how to
     **                          interpret the <code>delay</code> parameter.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  a scheduled future representing pending
     **                          submission of the task.
     **                          <br>
     **                          Possible object is {@link Future}.
     */
    @Override
    public final Future<?> schedule(final Executor executor, final Runnable task, final long delay, final TimeUnit unit) {
      Objects.requireNonNull(executor);
      Objects.requireNonNull(task);
      Objects.requireNonNull(unit);
      if (this.service.isShutdown())
        return DisabledFuture.INSTANCE;

      return this.service.schedule(
        () -> {
          try {
            executor.execute(task);
          }
          catch (Throwable t) {
            logger.log(Level.WARNING, "Exception thrown when submitting scheduled task", t);
            throw t;
          }
       }
       , delay
       , unit
      );
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schedule
  /**
   ** Returns a {@link Future} that will submit the task to the given executor
   ** after the given delay.
   **
   ** @param  executor           the {@link Executor} to run the task.
   **                            <br>
   **                            Allowed object is {@link Executor}.
   ** @param  task               the runnable task to schedule.
   **                            <br>
   **                            Allowed object is {@link Runnable}.
   ** @param  delay              how long to delay, in units of
   **                            <code>unit</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  unit               a {@link TimeUnit} determining how to interpret
   **                            the <code>delay</code> parameter.
   **                            <br>
   **                            Allowed object is {@link TimeUnit}.
   **
   ** @return                    a scheduled future representing pending
   **                            submission of the task.
   **                            <br>
   **                            Possible object is {@link Future}.
   */
  Future<?> schedule(final Executor executor, final Runnable task, final long delay, final TimeUnit unit);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns a scheduler that always returns a successfully completed future.
   **
   ** @return                    a scheduler that always returns a successfully
   **                            completed future.
   **                            <br>
   **                            Possible object is <code>Scheduler</code>.
   */
  static Scheduler disabled() {
    return Disabled.INSTANCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guarded
  /**
   ** Returns a scheduler that suppresses and logs any exception thrown by the
   ** delegate <code>scheduler</code>.
   **
   ** @param  scheduler          the scheduler to delegate to.
   **                            <br>
   **                            Allowed object is <code>Scheduler</code>.
   **
   ** @return                    a scheduler that suppresses and logs any
   **                            exception thrown by the delegate.
   **                            <br>
   **                            Possible object is <code>Scheduler</code>.
   */
  static Scheduler guarded(final Scheduler scheduler) {
    return (scheduler instanceof Guarded) ? scheduler : new Guarded(scheduler);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   system
  /**
   ** Returns a scheduler that uses the system-wide scheduling thread by using
   ** {@code CompletableFuture#delayedExecutor}.
   **
   ** @return                    a scheduler that uses the system-wide
   **                            scheduling thread.
   **                            <br>
   **                            Possible object is <code>Scheduler</code>.
   */
  static Scheduler system() {
    return System.present() ? System.INSTANCE : disabled();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   system
  /**
   ** Returns a scheduler that delegates to the a
   ** {@link ScheduledExecutorService}.
   **
   ** @param  service            the executor to schedule on.
   **                            <br>
   **                            Possible object is
   **                            {@link ScheduledExecutorService}.
   **
   ** @return                    a scheduler that delegates to the a
   **                            {@link ScheduledExecutorService}
   **                            <br>
   **                            Possible object is <code>Scheduler</code>.
   */
  static Scheduler forScheduledExecutorService(final ScheduledExecutorService service) {
    return new Default(service);
  }
}