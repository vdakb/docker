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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Command.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Command.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// abstract class Command
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** <code>Command</code> defines the attribute restriction on values that can
 ** be passed as the nested element command of a scheduled job instance.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Command extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int     retry = 0;
  private int     interval = 0;
  private boolean blocking = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Command</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Command() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the name of the category to be exported or imported by this
   ** category.
   **
   ** @return                    the name of the category to be exported or
   **                            imported by this category.
   */
  public final String value() {
    return super.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blocking
  /**
   ** Sets whether the execution of this command will be blocked until it is
   ** finished or not.
   **
   ** @param  blocking           whether the execution of this command will be
   **                            blocked until it is finished or not.
   */
  protected final void blocking(final boolean blocking) {
    this.blocking = blocking;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   blocking
  /**
   ** Returns whether the execution of this command will be blocked until it is
   ** finished or not.
   **
   ** @return                    whether the execution of this command will be
   **                            blocked until it is finished or not.
   */
  public final boolean blocking() {
    return this.blocking;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retry
  /**
   ** Sets the number of times the job this command belongs to will be
   ** retried.
   **
   ** @param  retry              the number of times the job this command
   **                            belongs to will be retried.
   */
  protected final void retry(final int retry) {
    this.retry = retry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   retry
  /**
   ** Returns the number of times the job this command belongs to will be
   ** retried.
   **
   ** @return                    the number of times the job this command
   **                            belongs to will be retried.
   */
  public final int retry() {
    return this.retry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   interval
  /**
   ** Sets the number of milliseconds the task will wait before the next time
   ** the status of the job will be checked.
   **
   ** @param  interval           the number of milliseconds the scheduler will
   **                            wait before the next time the status of the job
   **                            will be checked.
   */
  protected void interval(final int interval) {
    this.interval = interval;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   retryInterval
  /**
   ** Returns the number of milliseconds the task will wait before the next time
   ** the status of the job will be checked.
   **
   ** @return                    the number of milliseconds the task will wait
   **                            before the next time the status of the job will
   **                            be checked.
   */
  public final int interval() {
    return this.interval;
  }
}