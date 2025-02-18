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

    File        :   Stop.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Stop.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.scheduler.type;

import oracle.hst.deployment.ServiceCommand;

////////////////////////////////////////////////////////////////////////////////
// final class Stop
// ~~~~~ ~~~~~ ~~~~
/**
 ** <code>Stop</code> defines the attribute restriction on values that can
 ** be passed as the nested element stop of a scheduled job instance.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Stop extends Command {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the names of the job commands in alphabetical order
  private static final String[] registry = { ServiceCommand.stop.id() };

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Start</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Stop() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setBlocking
  /**
   ** Sets whether the execution of this command will be blocked until it is
   ** finished or not.
   **
   ** @param  blocking           whether the execution of this command will be
   **                            blocked until it is finished or not.
   */
  public final void setBlocking(final boolean blocking) {
    super.blocking(blocking);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRetry
  /**
   ** Sets the number of times the job this command belongs to will be
   ** retried.
   **
   ** @param  retry              the number of times the job this command
   **                            belongs to will be retried.
   */
  public final void setRetry(final int retry) {
    super.retry(retry);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInterval
  /**
   ** Sets the number of milliseconds the task will wait before the next time
   ** the status of the job will be checked.
   **
   ** @param  interval           the number of milliseconds the scheduler will
   **                            wait before the next time the status of the job
   **                            will be checked.
   */
  public void setInterval(final int interval) {
    super.interval(interval);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return registry;
  }
}