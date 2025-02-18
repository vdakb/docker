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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   AbstractTask.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractTask.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceMessage;
import oracle.hst.deployment.ServiceException;
import oracle.hst.deployment.AbstractFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractTask
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** This is the abstract base class for Ant tasks in general.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractTask extends AbstractFrontend {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs aa <code>AbstractTask</code> Ant task that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractTask() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   execute (overridden)
  /**
   ** Called by the project to let the task do its work.
   ** <p>
   ** This method may be called more than once, if the task is invoked more than
   ** once. For example, if target1 and target2 both depend on target3, then
   ** running "ant target1 target2" will run all tasks in target3 twice.
   **
   ** @throws BuildException      if something goes wrong with the build
   */
  @Override
  public final void execute() {
    debug(ServiceResourceBundle.string(ServiceMessage.TASK_VALIDATING));
    validate();
    debug(ServiceResourceBundle.string(ServiceMessage.TASK_VALIDATED));

    ServiceException reason = null;
    try {
      connect();
      if (connected()) {
        debug(ServiceResourceBundle.string(ServiceMessage.TASK_EXECUTING));
        beforeExecution();
        onExecution();
        debug(ServiceResourceBundle.string(ServiceMessage.TASK_EXECUTED));
      }
    }
    catch (ServiceException e) {
      reason = e;
    }
    finally {
      try {
        afterExecution();
        if (connected())
          disconnect();
      }
      catch (ServiceException e) {
        // override the result only if there is not an exception already
        // assigned to avoid overridding of the root cause
        if (reason == null)
          reason = e;
      }
    }

    if (reason != null) {
      if (this.failonerror()) {
        getProject().fireBuildFinished(reason);
      }
      else
        fatal(reason);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  protected abstract void validate()
    throws BuildException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connected
  /**
   ** Returns the state of connection.
   **
   ** @return                    the state of connection.
   */
  protected abstract boolean connected();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   connect
  /**
   ** Establish a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected abstract void connect()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disconnect
  /**
   ** Close a connection to the target system.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected abstract void disconnect()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beforeExecution
  /**
   ** The call back method just invoked before execution takes place.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void beforeExecution()
    throws ServiceException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onExecution
  /**
   ** The entry point of the task to perform.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected abstract void onExecution()
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   afterExecution
  /**
   ** The call back method just invoked after execution finished.
   ** <br>
   ** Default implementation does nothing.
   **
   ** @throws ServiceException   in case an error does occur.
   */
  protected void afterExecution()
    throws ServiceException {

    // intentionally left blank
  }
}