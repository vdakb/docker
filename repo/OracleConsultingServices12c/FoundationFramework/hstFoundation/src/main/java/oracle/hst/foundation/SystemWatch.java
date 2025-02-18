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

    File        :   SystemWatch.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemWatch.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation;

import java.util.Map;
import java.util.LinkedHashMap;

import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.resource.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// class SystemWatch
// ~~~~~ ~~~~~~~~~~~
/**
 ** A convenient implementation to bench a number of tasks, exposing total
 ** elapsed time and elapsed time for named tasks.
 ** <p>
 ** Conceals use of <code>System.currentTimeMillis()</code>, improving the
 ** readability of application code and reducing the likelihood of calculation
 ** errors.
 ** <p>
 ** Note that this object is not designed to be thread-safe and does not use
 ** synchronization.
 ** <p>
 ** This class is normally used to verify performance during proof-of-concepts
 ** and in development, rather than as part of production applications.
 ** <p>
 ** Example:
 ** <pre>
 **   SystemWatch watch = new SystemWatch();<br>
 **   watch.start("key1");
 **   &emsp;&emsp; :
 **   watch.start("key2");
 **   &emsp;&emsp; :
 **   watch.stop();
 **   &emsp;&emsp; :
 **   watch.start("key2");
 **   &emsp;&emsp; :
 **   watch.stop();
 **   watch.stop();
 **
 **   System.out.println(watch);
 ** </pre>
 ** Result:
 ** <pre>
 **   key1(140msec, 2time, average:70.0msec)
 **   key2(100msec, 1time, average:100.0msec)
 **   Summary:150msec
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SystemWatch {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** flag to advice that tha list of task has to be kept along one pair
   ** {@link #start(String)} / {@link #stop(String)}
   */
  private boolean                   keep  = true;

  /** the task to track the total elapsed time of the watch */
  protected final Task              root;
  protected final Map<String, Task> timer = new LinkedHashMap<String, Task>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Task
  // ~~~~~ ~~~~
  /**
   ** Value holder of data about one task executed within the unit.
   */
  public static class Task {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String name;
    /** the elapsed time this task took */
    private long elapsed;
    private long start;
    private int  time  = 0;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    //
    public Task(final String name) {
      // ensure inheritance
      super();

      // initialize instance
      this.name = name;
      this.restart();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  restart
    /**
     ** Returns the name of this task timer.
     **
     ** @return                  the name of this task timer.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  restart
    /**
     ** Initialize the start time with the current system time.
     */
    public synchronized void restart() {
      this.start = System.currentTimeMillis();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   elapsed
    /**
     ** Return the elapsed time in milliseconds for this task.
     **
     ** @return                  the elapsed time in milliseconds for this task.
     */
    public long elapsed() {
      return this.elapsed;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: second
    /**
     ** Return the elapsed time in seconds for this task.
     **
     ** @return                  the elapsed time in seconds for this task.
     */
    public double second() {
      return this.elapsed / 1000.0D;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: stop
    /**
     ** Stop the mesurement of the task by.
     **
     ** @return                  the <code>Task</code> to allow method chaining.
     **
     ** @see    #restart()
     */
    public synchronized Task stop() {
      this.elapsed += System.currentTimeMillis() - this.start;
      this.time++;
      return this;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct a new system watch.
   */
  public SystemWatch() {
    // ensure inheritance
    this("SystemWatch");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Construct a new system watch with the given identifier.
   ** <p>
   ** The identifier will be handy if output from multiple watches has to be
   ** captured and to distinguish between them.
   **
   ** @param  identifier         the identifier for this watch.
   */
  public SystemWatch(final String identifier) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.root = new Task(identifier);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Return the number of tasks timed.
   **
   ** @return                    the number of tasks timed.
   */
  public synchronized int count() {
    return this.timer.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elapsed
  /**
   ** Return the elapsed time in milliseconds for all tasks.
   **
   ** @return                    the elapsed time in milliseconds for all tasks.
   */
  public long elapsed() {
    return this.root.elapsed();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   second
  /**
   ** Return the elapsed time in seconds for all tasks.
   **
   ** @return                    the elapsed time in seconds for all tasks.
   */
  public double second() {
    return this.root.second();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   running
  /**
   ** Return whether the watch is currently running.
   **
   ** @return                    <code>true</code> if the watch is currently
   **                            running; otheriwse <code>false</code>.
   */
  public synchronized boolean running() {
    return count() > 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keepTask
  /**
   ** Determine whether the {@link Task} array is built over time.
   ** <p>
   ** Set this to <code>false</code> when using a <code>SystemWatch</code> for
   ** millions of intervals, or the task structure will consume excessive
   ** memory.
   ** <p>
   ** Default is <code>true</code>.
   **
   ** @param  keep               <code>true</code> if the {@link Task} array is
   **                            built over time; otherwise <code>false</code>.
   */
  public void keepTask(final boolean keep) {
    this.keep = keep;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(summary());
    if (this.keep) {
      for (String name : this.timer.keySet()) {
        Task task = this.timer.get(name);
        sb.append("; [" + name + "] took " + task.elapsed);
        long percent = Math.round((100.0 * task.second()) / this.second());
        sb.append(" = " + percent + "%");
      }
    }
    else {
      sb.append("; no task info kept");
    }
    return sb.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatted
  /**
   ** Return a string with a table describing all tasks performed.
   **
   ** @return                    a formatted string with the results of the task
   **                            timer.
   */
  public String formatted() {
    StringBuilder buffer = new StringBuilder(summary());
    if (this.keep) {
      final TableFormatter table  = new TableFormatter()
        .header(SystemBundle.string(SystemMessage.TIMER_ELAPSED))
        .header(SystemBundle.string(SystemMessage.TIMER_PERCENT))
        .header(SystemBundle.string(SystemMessage.TIMER_COUNTER))
        .header(SystemBundle.string(SystemMessage.TIMER_METHOD))
      ;
      for (String name : this.timer.keySet()) {
        final Task task = this.timer.get(name);
        table.row()
        .column(String.format("%14d",  task.elapsed))
        .column(String.format("%6.2f", (task.second() / this.root.second()) * 100.0D))
        .column(String.format("%6d",   task.time))
        .column(String.format("%s",    name));
      }
      table.print(buffer);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   summary
  /**
   ** Return a short description of the total running time.
   **
   ** @return                    a short description of the total running time.
   */
  public String summary() {
    stop();
    return SystemBundle.format(SystemMessage.TIMER_SUMMARY, this.root.name, this.root.elapsed);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   start
  /**
   ** Start a named task timer.
   ** <p>
   ** The results are undefined if {@link #stop()} or timing methods are called
   ** without invoking this method.
   **
   ** @param  name               the name of the task timer to start.
   **
   ** @see    #stop(String)
   */
  public synchronized void start(final String name)
    throws IllegalStateException {

    if (!keep)
      return;

    Task task = this.timer.get(name);
    if (task == null) {
      task = new Task(name);
      this.timer.put(name, task);
    }
    else
      task.restart();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stop the root task.
   ** <p>
   ** The results are undefined if timing methods are called without invoking at
   ** least one pair {@link #start(String)} / {@link #stop(String)} methods.
   **
   ** @see    #start(String)
   */
  public void stop()
    throws IllegalStateException {

    this.root.stop();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stop
  /**
   ** Stop the named task timer.
   ** <p>
   ** The results are undefined if timing methods are called without invoking at
   ** least one pair {@link #start(String)} / {@link #stop(String)} methods.
   **
   ** @param  name               the name of the task timer to stop.
   **
   ** @see    #start(String)
   */
  public synchronized void stop(final String name)
    throws IllegalStateException {

    if (!keep)
      return;

    if (this.keep) {
      final Task task = this.timer.get(name);
      if (task == null)
        throw new IllegalStateException(SystemBundle.string(SystemError.TIMER_NOT_RUNNING));

      task.stop();
    }
  }
}