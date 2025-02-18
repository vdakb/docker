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
    Subsystem   :   Foundation Shared Library

    File        :   AbstractConnectorLogger.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConnectorLogger.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import oracle.iam.identity.icf.foundation.logging.Watch;
import oracle.iam.identity.icf.foundation.logging.AbstractLogger;

import oracle.iam.identity.icf.foundation.resource.LoggingBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractConnectorLogger
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractConnectorLogger</code> implements the base functionality of an
 ** Identity Manager {@link AbstractLogger} regarding connector capabilities.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractConnectorLogger extends AbstractLogger {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The system watch to gather performance metrics. */
  protected final Watch  watch;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConnectorLogger</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   */
  protected AbstractConnectorLogger(final String loggerCategory) {
    // ensure inheritance
    this(loggerCategory, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>AbstractConnectorLogger</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  loggerCategory     the category for the Logger.
   ** @param  processName        the name of the process used for debugging
   **                            purpose in the scope of gathering performance
   **                            metrics.
   */
  protected AbstractConnectorLogger(final String loggerCategory, final String processName) {
    // ensure inheritance
    super(loggerCategory);

    // initialize instance attributes
    this.watch = new Watch(processName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStart
  /**
   ** Starts a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start
   **
   ** @see    #timerStop(String)
   */
  protected void timerStart(final String name) {
    // start the task to gather performance metrics
    this.watch.start(LoggingBundle.location(getClass(), name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timerStop
  /**
   ** Stops a timer for a named task.
   ** <p>
   ** The results are undefined if {@link #timerStop(String)} or timing methods
   ** are called without invoking this method.
   **
   ** @param  name               the name of the task to start
   **
   ** @see    #timerStart(String)
   */
  protected void timerStop(final String name) {
    // stop the task timer from gathering performance metrics
    this.watch.stop(LoggingBundle.location(getClass(), name));
  }
}