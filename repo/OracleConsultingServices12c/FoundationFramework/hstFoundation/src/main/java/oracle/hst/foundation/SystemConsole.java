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

    File        :   SystemConsole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemConsole.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation;

import java.util.logging.ConsoleHandler;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.logging.SimpleFormatter;

import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class SystemConsole
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A convenient implementation for testing and debugging purpose.
 ** <p>
 ** This implementation publishes log records to <code>System.err</code>. By
 ** default a <code>SimpleFormatter</code> is used to generate brief summaries.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class SystemConsole implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String shortName;
  private final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Console
  // ~~~~~ ~~~~~~~
  /**
   ** A logger that writes all to the console.
   */
  private static class Console extends Logger {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Logger</code> for the specified logging category.
     **
     ** @param  category         the category for the Logger.
     */
    private Console(final String category) {
      // ensure inheritance
      super(category);

      // create an appropriate handler with a nice formatter
      ConsoleHandler handler = new ConsoleHandler();
      handler.setFormatter(new SimpleFormatter());
      handler.setLevel(Logger.ALL);

      // configure the logger
      this.delegate.addHandler(handler);
      this.delegate.setLevel(Logger.ALL);
      this.delegate.setUseParentHandlers(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SystemConsole</code> which use the specified category
   ** for logging purpose.
   **
   ** @param  category           the category for the Logger.
   */
  public SystemConsole(final String category) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.shortName = ClassUtility.shortName(this);
    this.logger    = new Console(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    this.logger.fatal(this.shortName, method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String method, final String message) {
    this.logger.error(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String message) {
    this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String method, final String message) {
    this.logger.warn(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void debug(final String method, final String message) {
    this.logger.debug(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    this.logger.trace(this.shortName, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void fatal(final Throwable throwable) {
    this.logger.fatal(throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to the associated <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void error(final Throwable throwable) {
    this.logger.error(throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an application warning to the associated <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void warning(final Throwable throwable) {
    this.logger.warn(throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public final void debug(final String message) {
    this.logger.log(Logger.DEBUG, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   **
   ** @param  method             the name of method that is being entered.
   */
  public final void entering(final String method) {
    this.logger.entering(this.shortName, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log returning from a
   ** method.
   **
   ** @param  method             the name of method that is being returned from.
   */
  public final void exiting(final String method) {
    this.logger.exiting(this.shortName, method);
  }
}