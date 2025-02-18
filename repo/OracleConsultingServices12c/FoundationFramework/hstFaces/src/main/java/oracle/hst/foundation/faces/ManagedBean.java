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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Java Server Faces Foundation

    File        :   ManagedBean.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ManagedBean.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.faces;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class ManagedBean
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Declares methods a user interface service provides.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ManagedBean implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Logger logger;
  private final String clazz;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedBean</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ManagedBean() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.clazz  = ClassUtility.canonicalName(this);
    this.logger = Logger.create(this.clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatalLevel
  /**
   ** Check if a message of level {@link Logger#FATAL} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for fatel
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean fatalLevel() {
    return this.logger.fatalLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorLevel
  /**
   ** Check if a message of level {@link Logger#ERROR} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for error
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean errorLevel() {
    return this.logger.errorLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warningLevel
  /**
   ** Check if a message of level {@link Logger#WARNING} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for warning
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean warningLevel() {
    return this.logger.warningLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notificationLevel
  /**
   ** Check if a message of level {@link Logger#NOTIFICATION} the would actually
   ** be logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for
   **                            information is enabled; otherwise
   **                            <code>false</code>
   */
  public final boolean notificationLevel() {
    return this.logger.notificationLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debugLevel
  /**
   ** Check if a message of level {@link Logger#DEBUG} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean debugLevel() {
    return this.logger.debugLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traceLevel
  /**
   ** Check if a message of level {@link Logger#TRACE} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean traceLevel() {
    return this.logger.traceLevel();
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
   ** Logs an fatal error to an associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  @Override
  public final void fatal(final String method, final Throwable throwable) {
    this.logger.fatal(this.clazz, method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to an associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void error(final String method, final String message) {
    this.logger.error(this.clazz, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
  ** @param  message             the message to log.
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
    this.logger.warn(this.clazz, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to an associated <code>Logger</code>.
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
    this.logger.debug(this.clazz, method, message);
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
    this.logger.trace(this.clazz, method, message);
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
    this.logger.entering(this.clazz, method);
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
    this.logger.exiting(this.clazz, method);
  }
}