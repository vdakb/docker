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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared runtime facilities

    File        :   SystemThreadPool.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemThreadPool.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation;

import java.util.List;

import java.io.StringWriter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.logging.TableFormatter;

////////////////////////////////////////////////////////////////////////////////
// class SystemThreadPool
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** An executor service that executes each submitted task using one of possibly
 ** several pooled threads, normally configured using {@link ThreadFactory}
 ** factory methods.
 ** <p>
 ** Thread pools address two different problems: they usually provide improved
 ** performance when executing large numbers of asynchronous tasks, due to
 ** reduced per-task invocation overhead, and they provide a means of bounding
 ** and managing the resources, including threads, consumed when executing a
 ** collection of tasks. Each <code>SystemThreadPoolExecutor</code> also
 ** maintains some basic statistics, such as the number of completed tasks.
 ** <p>
 ** Use the following guide when manually configuring and tuning this class:
 ** <dl>
 **   <dt>Minimum and maximum pool sizes</dt>
 **   <dd>A <code>SystemThreadPoolExecutor</code> will automatically adjust the
 **   pool size (see {@link Config#capacity()}) according to the bounds set by
 **   minimum (see {@link Config#minimum()}) and maximum
 **   (see {@link Config#maximum}). When a new task is submitted in method
 **   {@link #submit}, and fewer than minimum threads are running, a new thread
 **   is created to handle the request, even if other worker threads are idle.
 **   If there are more than minimum but less than maximum threads running, a
 **   new thread will be created only if the queue is full. By setting
 **   minimum and maximum the same, you create a fixed-size thread pool. By
 **   setting maximum to an essentially unbounded value such as
 **   <code>Integer.MAX_VALUE</code>, you allow the pool to accommodate an
 **   arbitrary number of concurrent tasks. Most typically, minimum and maximum
 **   pool sizes are set only upon construction.</dd>
 **   <dt>On-demand construction</dt>
 **   <dd>By default, even minimum threads are initially created and started
 **   only when new tasks arrive, but this can be overridden dynamically using
 **   method {@link ThreadPoolExecutor#prestartCoreThread} or
 **   {@link ThreadPoolExecutor#prestartAllCoreThreads}. You probably want to
 **   prestart threads if you construct the pool with a non-empty queue.</dd>
 **   <dt>Creating new threads</dt>
 **   <dd>New threads are created using a {@link ThreadFactory}. By supplying a
 **   {@link ThreadFactory}, you can alter the thread's name, thread group,
 **   priority, daemon status, etc. If a {@link ThreadFactory} fails to create a
 **   thread when asked by returning <code>null</code>
 **   from <code>newThread</code>, the executor will continue, but might not be
 **   able to execute any tasks. Threads should possess the "modifyThread"
 **   <code>RuntimePermission</code>. If worker threads or other threads using
 **   the pool do not possess this permission, service may be degraded:
 **   configuration changes may not take effect in a timely manner, and a
 **   shutdown pool may remain in a state in which termination is possible but
 **   not completed.</dd>
 **   <dt>Keep-alive Times</dt>
 **   <dd>If the pool currently has more than poolSizeMinimum threads, excess
 **   threads will be terminated if they have been idle for more than the
 **   keepAliveTime (see {@link Config#keepAlive}). This provides a means of
 **   reducing resource consumption when the pool is not being actively used. If
 **   the pool becomes more active later, new threads will be constructed. Using
 **   a value of <code>Long.MAX_VALUE</code> {@link TimeUnit#NANOSECONDS}
 **   effectively disables idle threads from ever terminating prior to shut
 **   down. By default, the keep-alive policy applies only when there are more
 **   than minimum Threads.</dd>
 **   <dt>Queuing</dt>
 **   <dd>A {@link LinkedBlockingQueue} is submitted tasks. The use of this
 **       queue interacts with pool sizing:
 **   <ul>
 **     <li>If fewer than corePoolSize threads are running, the Executor always
 **         prefers adding a new thread rather than queuing.
 **     <li>If poolSizeMinimum or more threads are running, the Executor always
 **         prefers queuing a request rather than adding a new thread.
 **     <li>If a request cannot be queued, a new thread is created unless this
 **         would exceed poolSizeMaximum, in which case, the task will be
 **         rejected.
 **   </ul>
 **   There are three general strategies for queuing:
 **   <ol>
 **     <li><em> Direct handoffs.</em>
 **         <br>
 **         A good default choice for a work queue is a
 **         <code>SynchronousQueue</code> that hands off tasks to threads
 **         without otherwise holding them. Here, an attempt to queue a task
 **         will fail if no threads are immediately available to run it, so a
 **         new thread will be constructed. This policy avoids lockups when
 **         handling sets of requests that might have internal dependencies.
 **         Direct handoffs generally require unbounded maximum to avoid
 **         rejection of new submitted tasks. This in turn admits the
 **         possibility of unbounded thread growth when commands continue to
 **         arrive on average faster than they can be processed.
 **     <li><em>Unbounded queues.</em>
 **         <br>
 **         Using an unbounded queue (for example a {@link LinkedBlockingQueue}
 **         without a predefined capacity) will cause new tasks to wait in the
 **         queue when all poolSizeMinimum threads are busy. Thus, no more than
 **         minimum threads will ever be created. (And the value of the
 **         maximum therefore doesn't have any effect.)  This may be
 **         appropriate when each task is completely independent of others, so
 **         tasks cannot affect each others execution; for example, in a web
 **         page server. While this style of queuing can be useful in smoothing
 **         out transient bursts of requests, it admits the possibility of
 **         unbounded work queue growth when commands continue to arrive on
 **         average faster than they can be processed.
 **     <li><em>Bounded queues.</em>
 **         <br>
 **         A bounded queue (for example, an <code>ArrayBlockingQueue</code>)
 **         helps prevent resource exhaustion when used with finite maximum,
 **         but can be more difficult to tune and control. Queue sizes and
 **         maximum pool sizes may be traded off for each other: Using large
 **         queues and small pools minimizes CPU usage, OS resources, and
 **         context-switching overhead, but can lead to artificially low
 **         throughput. If tasks frequently block (for example if they are I/O
 **         bound), a system may be able to schedule time for more threads than
 **         you otherwise allow. Use of small queues generally requires larger
 **         pool sizes, which keeps CPUs busier but may encounter unacceptable
 **         scheduling overhead, which also decreases throughput.
 **   </ol>
 **  </dd>
 **  <dt>Rejected tasks</dt>
 **  <dd>New tasks submitted in method {@link #submit} will be <em>rejected</em>
 **  when the Executor has been shut down, and also when the Executor uses
 **  finite bounds for both maximum threads and work queue capacity, and is
 **  saturated. In either case, the <code>execute</code> method invokes the
 **  <code>RejectedExecutionHandler.rejectedExecution</code> method of its
 **  <code>RejectedExecutionHandler</code>. Four predefined handler policies are
 **  provided:
 **  <ol>
 **    <li>In the default {@link ThreadPoolExecutor.AbortPolicy}, the handler
 **        throws a runtime {@link RejectedExecutionException} upon rejection.
 **    <li>In {@link ThreadPoolExecutor.CallerRunsPolicy}, the thread that
 **        invokes <code>execute</code> itself runs the task. This provides a
 **        simple feedback control mechanism that will slow down the rate that
 **        new tasks are submitted.
 **    <li>In {@link ThreadPoolExecutor.DiscardPolicy}, a task that cannot be
 **        executed is simply dropped.
 **    <li>In {@link ThreadPoolExecutor.DiscardOldestPolicy}, if the executor is
 **        not shut down, the task at the head of the work queue is dropped, and
 **        then execution is retried (which can fail again, causing this to be
 **        repeated.)
 **  </ol>
 **  It is possible to define and use other kinds of
 **  <code>RejectedExecutionHandler</code> classes. Doing so requires some care
 **  especially when policies are designed to work only under particular
 **  capacity or queuing policies. </dd>
 **  <dt>Finalization</dt>
 **  <dd>A pool that is no longer referenced in a program <em>AND</em> has no
 **  remaining threads will be <code>shutdown</code> automatically. If you would
 **  like to ensure that unreferenced pools are reclaimed even if users forget
 **  to call {@link #shutdown}, then you must arrange that unused threads
 **  eventually die, by setting appropriate keep-alive times, using a lower
 **  bound of zero core threads.</dd>
 ** </dl>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemThreadPool {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int   POOL_SIZE_DEFAULT  = 100;
  private static final int   POOL_SIZE_MINIMUM  = 1;
  private static final int   POOL_SIZE_MAXIMUM  = 1;
  private static final long  KEEP_ALIVE_DEFAULT = 0L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Semaphore          semaphore;
  private ThreadPoolExecutor delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Config
  // ~~~~~ ~~~~~~
  public static class Config {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int  capacity;
    private final int  minimum;
    private final int  maximum;
    private final long keepAlive;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Factory
    // ~~~~~ ~~~~~~~
    /**
     ** Factory to create {@link Config} instances.
     ** <br>
     ** If a factory instance is configured with some configuration, that would
     ** be used to configure the created config instances.
     ** <p>
     ** <b>For example:</b>
     ** <pre>
     **   SystemPoolConfig.Factory factory = SystemPoolConfig.build();
     **   SystemPoolConfig         config1 = factory.capacity(5).build();
     **   SystemPoolConfig         config2 = factory.reset().build();
     ** </pre>
     ** <p>
     ** All the methods in this class are safe for use by multiple concurrent
     ** threads.
     */
    public static class Factory {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private int  capacity  = POOL_SIZE_DEFAULT;
      private int  minimum   = POOL_SIZE_MINIMUM;
      private int  maximum   = POOL_SIZE_MAXIMUM;
      private long keepAlive = KEEP_ALIVE_DEFAULT;

      //////////////////////////////////////////////////////////////////////////
      // Accessor methods
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: capacity
      /**
       ** Sets value for the initial number of elements available in the pool.
       **
       ** @param value             the value for the initial number of elements
       **                          available in the pool.
       **                          <br>
       **                          Allowed object is <code>int</code>.
       **
       ** @return                  the <code>Factory</code> to allow method
       **                          chaining.
       **                          <br>
       **                          Possible object is {@link Factory}.
       */
      public Factory capacity(final int value) {
        if (value < 1)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.THREAD_POOL_SIZE_CAPACITY));

        this.capacity = value;
        return this;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: minimum
      /**
       ** Sets value for the minimum number of threads allow in the pool.
       **
       ** @param  value          the value for the minimum number of threads to
       **                        allow in the pool.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       **
       ** @return                the <code>Factory</code> to allow method
       **                        chaining.
       **                        <br>
       **                        Possible object is {@link Factory}.
       */
      public Factory minimum(final int value) {
        if (value < 1)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.THREAD_POOL_SIZE_MINIMUM));

        this.minimum = value;
        return this;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: maximum
      /**
       ** Sets value for the maximum number of threads allow in the pool.
       **
       ** @param  value          the value for the maximum number of threads to
       **                        allow in the pool.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       **
       ** @return                the <code>Factory</code> to allow method
       **                        chaining.
       **                        <br>
       **                        Possible object is {@link Factory}.
       */
      public Factory maximum(final int value) {
        if (value < 1)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.THREAD_POOL_SIZE_MAXIMUM));

        this.maximum = value;
        return this;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: keepAlive
      /**
       ** Sets value when the number of threads is greater than the minimum,
       ** this is the maximum time that excess idle threads will wait for new
       ** tasks before terminating.
       **
       ** @param  value          the value for the time number of threads to
       **                        allow in the pool.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       **
       ** @return                when the number of threads is greater than the
       **                        minimum, this is the maximum time that excess
       **                        idle threads will wait for new tasks before
       **                        terminating.
       **                        <br>
       **                        Possible object is {@link Factory}.
       */
      public Factory keepAlive(final int value) {
        if (value < 0)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.THREAD_POOL_KEEP_ALLIVE));

        this.keepAlive = value;
        return this;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: reset
      /**
       ** Resets the factory.
       **
       ** @return                the <code>Factory</code> to allow method
       **                        chaining.
       **                        <br>
       **                        Possible object is {@link Factory}.
       */
      public Factory reset() {
        this.capacity  = POOL_SIZE_DEFAULT;
        this.minimum   = POOL_SIZE_MINIMUM;
        this.maximum   = POOL_SIZE_MAXIMUM;
        this.keepAlive = KEEP_ALIVE_DEFAULT;
        return this;
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: build
      /**
       ** Returns the {@link Config} from the values of this builder.
       **
       ** @return                the {@link Config} from the values of this
       **                        builder.
       */
      public Config build() {
        if (this.minimum > this.maximum)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.THREAD_POOL_MIN_MAX_GT));

        return new Config(this.capacity, this.minimum, this.maximum, this.keepAlive);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for the <code>SystemPoolConfig</code> object.
     **
     ** @param  capacity         the value for the initial number of elements
     **                          available.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  minimum          the value for the number of threads to keep in
     **                          the pool, even if they are idle.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  maximum          the value for the maximum number of threads to
     **                          allow in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  keepAlive        when the number of threads is greater than the
     **                          minimum, this is the maximum time that excess
     **                          idle threads will wait for new tasks before
     **                          terminating.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    private Config(final int capacity, final int minimum, final int maximum, final long keepAlive) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.capacity  = capacity;
      this.minimum   = minimum;
      this.maximum   = maximum;
      this.keepAlive = keepAlive;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: capacity
    /**
     ** Returns the value for the initial number of elements available.
     **
     ** @return                    the value for the initial number of elements
     **                            available.
     **                            <br>
     **                            Possible object is <code>int</code>.
     */
    public final int capacity() {
      return this.capacity;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimum
    /**
     ** Returns the value for the number of threads to keep in the pool, even if
     ** they are idle.
     **
     ** @return                  the value for the number of threads to keep in
     **                          the pool, even if they are idle.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int minimum() {
      return this.minimum;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximum
    /**
     ** Returns the value for the maximum number of threads to allow in the
     ** pool.
     **
     ** @return                  the value for the maximum number of threads to
     **                          allow in the pool.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int maximum() {
      return this.maximum;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: keepAlive
    /**
     ** Returns the value when the number of threads is greater than the
     ** minimum, this is the maximum time that excess idle threads will wait for
     ** new tasks before terminating.
     **
     ** @return                  when the number of threads is greater than the
     **                          minimum, this is the maximum time that excess
     **                          idle threads will wait for new tasks before
     **                          terminating.
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    public long keepAlive() {
      return this.keepAlive;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Factory</code> object.
     **
     ** @return                    the {@link Factory}.
     */
    public static Factory build() {
      return new Factory();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final TableFormatter table = new TableFormatter()
      .header(SystemBundle.string(SystemMessage.PROPERTY_NAME))
      .header(SystemBundle.string(SystemMessage.PROPERTY_VALUE))
      ;
      table.row().column("capacity").column(this.capacity);
      table.row().column("minimum").column(this.minimum);
      table.row().column("maximum").column(this.maximum);
      table.row().column("keepAlive").column(this.keepAlive);
      final StringWriter writer = new StringWriter();
      table.print(writer);
      return writer.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for a new {@link ThreadPoolExecutor} with the given initial
   ** parameters and default thread factory and rejected execution handler.
   **
   ** @param  configuration      the configuration containing the values for
   **                            <ol>
   **                              <li>capacity
   **                              <li>minimum
   **                              <li>maximum
   **                              <li>keepAlive
   **                            </ol>
   **                            new thread.
   **                            <br>
   **                            Allowed object is {@link Config}.
   ** @param  factory            the factory to use when the executor creates a
   **                            new thread.
   **                            <br>
   **                            Allowed object is {@link ThreadFactory}.
   **
   ** @throws IllegalArgumentException if one of the following holds:
   **                                  <br>
   **                                  <code>minimum &lt; 1</code>
   **                                  <br>
   **                                  <code>maximum &lt; 1</code>
   **                                  <br>
   **                                  <code>keepAlive &lt; 0</code>
   **                                  <br>
   **                                  <code>maximum &lt; minimum</code>
   */
  public SystemThreadPool(final Config configuration, final ThreadFactory factory) {
    // ensure inheritance
    this(configuration.capacity, configuration.minimum, configuration.maximum, configuration.keepAlive, factory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for a new {@link ThreadPoolExecutor} with the given initial
   ** parameters and default thread factory and rejected execution handler.
   ** <p>
   ** It may be more convenient to use one of the {@link ThreadPoolExecutor}s
   ** factory methods instead of this general purpose constructor.
   **
   ** @param  capacity           the value for the initial number of elements
   **                            available in the pool.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minimum            the number of threads to keep in the pool, even
   **                            if they are idle.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  maximum            the maximum number of threads to allow in the
   **                            pool.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  keepAlive          when the number of threads is greater than the
   **                            minimum, this is the maximum time that excess
   **                            idle threads will wait for new tasks before
   **                            terminating.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  factory            the factory to use when the executor creates a
   **                            new thread.
   **                            <br>
   **                            Allowed object is {@link ThreadFactory}.
   **
   ** @throws IllegalArgumentException if one of the following holds:
   **                                  <br>
   **                                  <code>minimum &lt; 1</code>
   **                                  <br>
   **                                  <code>maximum &lt; 1</code>
   **                                  <br>
   **                                  <code>keepAlive &lt; 0</code>
   **                                  <br>
   **                                  <code>maximum &lt; minimum</code>
   */
  public SystemThreadPool(final int capacity, final int minimum, final int maximum, final long keepAlive, final ThreadFactory factory) {
    // ensure inheritance
    super();

    this.delegate  = new ThreadPoolExecutor(minimum, maximum, keepAlive, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), factory);
    this.semaphore = new Semaphore(capacity + maximum);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   terminating
  /**
   ** Returns <code>true</code> if this executor is in the process of
   ** terminating after {@link #shutdown} or {@link #shutdownNow} but has not
   ** completely terminated. This method may be useful for debugging. A return
   ** of <code>true</code> reported a sufficient period after shutdown may
   ** indicate that submitted tasks have ignored or suppressed interruption,
   ** causing this executor not to properly terminate.
   **
   ** @return                    <code>true</code> if terminating but not yet
   **                            terminated.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean terminating() {
    return this.delegate.isTerminating();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   terminated
  /**
   ** Returns <code>true</code> if this executor is terminated after
   ** {@link #shutdown} or {@link #shutdownNow}
   **
   ** @return                    <code>true</code> if  terminated.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean terminated() {
    return this.delegate.isTerminated();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   submit
  /**
   ** Executes the given task sometime in the future.
   ** <p>
   ** The task may execute in a new thread or in an existing pooled thread.
   ** <p>
   ** If the task cannot be submitted for execution, either because this
   ** executor has been shutdown or because its capacity has been reached, the
   ** task is handled by the current <code>RejectedExecutionHandler</code>.
   **
   ** @param  task               the {@link Runnable} task to execute.
   **
   ** @throws NullPointerException       if <code>task</code> is
   **                                    <code>null</code>.
   ** @throws RejectedExecutionException at discretion of
   **                                    <code>RejectedExecutionHandler</code>,
   **                                    if the task cannot be accepted for
   **                                    execution.
   */
  public void submit(final Runnable task) {
    this.semaphore.acquireUninterruptibly();
    try {
      this.delegate.execute(new Runnable() {
          public void run() {
            try {
              task.run();
            }
            finally {
              SystemThreadPool.this.semaphore.release();
            }
          }
        });
    }
    catch (RejectedExecutionException e) {
      this.semaphore.release();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdown
  /**
   ** Initiates an orderly shutdown in which previously submitted tasks are
   ** executed, but no new tasks will be accepted.
   ** <br>
   ** Invocation has no additional effect if already shut down.
   ** <p>
   ** This method does not wait for previously submitted tasks to complete
   ** execution.  Use {@link #awaitTermination(long, TimeUnit) awaitTermination}
   ** to do that.
   */
  public void shutdown() {
    this.delegate.shutdown();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shutdownNow
  /**
   ** Attempts to stop all actively executing tasks, halts the processing of
   ** waiting tasks, and returns a list of the tasks that were awaiting
   ** execution. These tasks are drained (removed) from the task queue upon
   ** return from this method.
   ** <p>
   ** This method does not wait for actively executing tasks to terminate. Use
   ** {@link #awaitTermination awaitTermination} to do that.
   ** <p>
   ** There are no guarantees beyond best-effort attempts to stop processing
   ** actively executing tasks. This implementation cancels tasks via
   ** {@link Thread#interrupt}, so any task that fails to respond to interrupts
   ** may never terminate.
   **
   ** @return                    the {@link List} of {@link Runnable} tasks that
   **                            are still executed.
   */
  public List<Runnable> shutdownNow() {
    return this.delegate.shutdownNow();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   awaitTermination
  public boolean awaitTermination(final long timeout, final TimeUnit unit)
    throws InterruptedException {

    return this.delegate.awaitTermination(timeout, unit);
  }
}