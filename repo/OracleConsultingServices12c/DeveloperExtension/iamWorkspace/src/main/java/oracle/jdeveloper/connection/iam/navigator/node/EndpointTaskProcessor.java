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

    File        :   EndpointTaskProcessor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    EndpointTaskProcessor.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.84  2019-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.navigator.node;

import oracle.ide.Ide;

import oracle.ide.controls.ProgressTrackedTask;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class EndpointTaskProcessor
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
public class EndpointTaskProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The {@link EndpointTaskProcessor} implements the singleton pattern.
   ** <br>
   ** The static attribute {@link #instance} holds this single instance.
   */
  private static EndpointTaskProcessor instance;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Task
  // ~~~~~ ~~~~
  /**
   ** Subclass of ProgressTrackedTask that executes the wrapped EndpointRunnable
   ** using {@link ExecutorService#submit(Runnable)} when run. If cancelled
   ** while the EndpointRunnable is executing it calls interrupt on the
   ** processor Thread running the EndpointRunnable using the Future from the
   ** ExecutorService.
   */
  private class Task implements ProgressTrackedTask {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String   activity;
    private final Runnable runnable;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a new {@link ProgressTrackedTask}.
     ** <br>
     ** This constructor is protected to prevent other classes to use
     ** "new Timer()".
     **
     ** @param  activity         the identifier of the task.
     ** @param  runnable         the runnable to execute.
     */
     Task(final String activity, final Runnable runnable) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.activity = activity;
      this.runnable = runnable;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: isIndeterminate (ProgressTrackedTask)
    /**
     ** Used to determine if a task is determiniate or indeterminate.
     **
     ** @return                  <code>true</code> of the task is indeterminate;
     **                          <code>false</code> if it is determinate.
     */
    @Override
    public boolean isIndeterminate() {
      return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getMinimum (ProgressTrackedTask)
    /**
     ** Used for determinate tasks to get the minimum range for the value
     ** returned by <code>getCurrentValue()</code>.
     **
     ** @return                  the minumum range value.
     */
    @Override
    public int getMinimum() {
      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getMaximum (ProgressTrackedTask)
    /**
     ** Used for determinate tasks to get the maximum range for the value
     ** returned by <code>getCurrentValue()</code>.
     **
     ** @return                  the maximum range value.
     */
    @Override
    public int getMaximum() {
      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCurrentValue (ProgressTrackedTask)
    /**
     ** Used for determinate tasks to get the current task completion status.
     ** <code>getCurrentValue()</code> should always return a value in the range
     ** of <code>getMinimum()</code> and <code>getMaximum()</code>.
     **
     ** @return                  the current completion status of the task.
     */
    @Override
    public int getCurrentValue() {
      return 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getCurrentText (ProgressTrackedTask)
    /**
     ** Used to set a textual string in the status bar.
     ** <br>
     ** This can be used by either determinate or indeterminate tasks. When the
     ** task does not provide textual status then return <code>null</code>.
     ** <br>
     ** When multiple tasks are running no textual representation will be shown
     ** in the progress bar.
     **
     ** @return                  the string to show in the progress bar.
     */
    @Override
    public String getCurrentText() {
      return this.activity;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTaskDescription (ProgressTrackedTask)
    /**
     ** Used to provide a name for the task.
     **
     ** @return                 the name of the task
     */
    @Override
    public String getTaskDescription() {
      return this.activity;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: run (Runnable)
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
      this.runnable.run();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EndpointTaskProcessor</code> that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private EndpointTaskProcessor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** The <code>EndpointTaskProcessor</code> is a singleton class.
   ** This method gets this task tracker single instance.
   **
   ** @return                    the <code>EndpointTaskProcessor</code>
   **                            single instance.
   */
  public static synchronized EndpointTaskProcessor instance() {
    if (instance == null) {
      instance = new EndpointTaskProcessor();
    }
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  execute
  /**
   ** Post the given runnable to the process queue for immediate execution.
   ** <br>
   ** The execution will happen off of the event thread, and only one process
   ** will execute at a time for a given Database.
   **
   ** @param  runnable           the {@link Runnable} to execute.
   */
  public void execute(final Runnable runnable) {
    execute(StringUtility.EMPTY, runnable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  execute
  /**
   ** Post the given runnable to the process queue for immediate execution.
   ** <br>
   ** The execution will happen off of the event thread, and only one process
   ** will execute at a time for a given Database.
   **
   **
   ** @param  activity           the identifier of the task.
   ** @param  runnable           the {@link Runnable} to execute.
   */
  public void execute(final String activity, final Runnable runnable) {
    execute(activity, null, runnable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  execute
  /**
   ** Post the given runnable to the process queue for immediate execution.
   ** <br>
   ** The execution will happen off of the event thread, and only one process
   ** will execute at a time for a given Database.
   **
   **
   ** @param  activity           the identifier of the task.
   ** @param  thread             the identifier of the thread.
   ** @param  runnable           the {@link Runnable} to execute.
   */
  public void execute(final String activity, final String thread, final Runnable runnable) {
    if (thread != null) {
      Ide.getStatusBar().getProgressTracker().addTask(new Task(activity, runnable), thread);
    }
    else
      Ide.getStatusBar().getProgressTracker().addTask(new Task(activity, runnable));
  }
}