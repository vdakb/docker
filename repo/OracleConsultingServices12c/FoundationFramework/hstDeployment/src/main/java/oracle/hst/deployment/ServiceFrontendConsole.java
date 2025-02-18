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

    File        :   ServiceFrontendConsole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceFrontendConsole.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.SimpleFormatter;

////////////////////////////////////////////////////////////////////////////////
// class ServiceFrontendConsole
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** A convenient implementation for testing and debugging purpose.
 ** <p>
 ** This implementation publishes log records to <code>System.err</code>. By
 ** default a <code>SimpleFormatter</code> is used to generate brief summaries.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServiceFrontendConsole implements ServiceFrontend {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  final static String  CATEGORY = "deployment";
  final static String  METHOD   = "method";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String shortName;
  private final Logger logger;

  private boolean      failonerror = true;

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
      handler.setLevel(Level.FINEST);

      // configure the logger
      this.delegate.addHandler(handler);
      this.delegate.setLevel(Level.FINEST);
      this.delegate.setUseParentHandlers(false);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ServiceFrontendConsole</code> which use the default
   ** category logging purpose.
   */
  public ServiceFrontendConsole() {
    this(CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>ServiceFrontendConsole</code> which use the specified
   ** category for logging purpose.
   **
   ** @param  category           the category for the Logger.
   */
  protected ServiceFrontendConsole(final String category) {
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
  // Method:   verbose (ServiceFrontend)
  /**
   ** Returns how the task will provide verbosity of operations.
   **
   ** @return                    how the task will verbosity of operations.
   */
  @Override
  public boolean verbose() {
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failonerror (ServiceFrontend)
  /**
   ** Sets how the task behavior will be in case an error is detected.
   **
   ** @param  failonerror        how the task behavior will be in case an error
   **                            is detected.
   */
  public void failonerror(final boolean failonerror) {
    this.failonerror = failonerror;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failonerror (ServiceFrontend)
  /**
   ** Return how the task behavior will be in case an error is detected.
   **
   ** @return                    how the task behavior will be in case an error
   **                            is detected.
   */
  @Override
  public boolean failonerror() {
    return this.failonerror;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (ServiceFrontend)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public void fatal(final Throwable what) {
    fatal(METHOD, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (ServiceFrontend)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public void fatal(final String location, final Throwable what) {
    this.logger.fatal(this.shortName, location, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void error(final String pattern, final String argument) {
    error(String.format(pattern, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Writes an normal error message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void error(final String pattern, final Object[] arguments) {
    error(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (ServiceFrontend)
  /**
   ** Logs an normal error message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String message) {
    this.logger.error(this.shortName, METHOD, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
  ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void warning(final String pattern, final String argument) {
    warning(String.format(pattern, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
   ** Writes an warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void warning(final String pattern, final Object[] arguments) {
    warning(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (ServiceFrontend)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  @Override
  public final void warning(final String message) {
    this.logger.warn(this.shortName, METHOD, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
  ** Writes a informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void info(final String pattern, final String argument) {
    info(String.format(pattern, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void info(final String pattern, final Object[] arguments) {
    info(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (ServiceFrontend)
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  @Override
  public final void info(final String message) {
    this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void trace(final String pattern, final String argument) {
    trace(String.format(pattern, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (ServiceFrontend)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void trace(final String pattern, final Object[] arguments) {
    trace(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   trace (ServiceFrontend)
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  @Override
  public final void trace(final String message) {
    this.logger.trace(this.shortName, METHOD, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  @Override
  public final void debug(final String pattern, final String argument) {
    debug(String.format(pattern, argument));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (ServiceFrontend)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  @Override
  public final void debug(final String pattern, final Object[] arguments) {
    debug(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug (ServiceFrontend)
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  @Override
  public final void debug(final String message) {
    this.logger.debug(this.shortName, METHOD, message);
  }
}