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
    Subsystem   :   Common shared logging facilities

    File        :   Logger.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Logger.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.logging;

import java.util.logging.Level;
import java.util.logging.Filter;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.resource.LoggingBundle;

////////////////////////////////////////////////////////////////////////////////
// class Logger
// ~~~~~ ~~~~~~
/**
 ** The <code>Logger</code> provides the mechanism to log informations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class Logger {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Level                ALL          = Level.ALL;
  public static final Level                TRACE        = Level.FINEST;
  public static final Level                DEBUG        = Level.FINE;
  public static final Level                NOTIFICATION = Level.INFO;
  public static final Level                WARNING      = Level.WARNING;
  public static final Level                ERROR        = Level.SEVERE;
  public static final Level                FATAL        = new Fatal();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final java.util.logging.Logger delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Fatal
  // ~~~~~ ~~~~~
  /**
   ** A custom logging level to wrap a fatal log level
   */
  private static class Fatal extends Level {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-1817122222111301175")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Fatal</code> logging level.
     */
    private Fatal() {
      super(ERROR.toString(), ERROR.intValue() + 100);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class NOP
  // ~~~~~ ~~~
  /**
   ** A direct NOP (no operation) implementation of {@link Logger}.
   */
  private static class NOP extends Logger {

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
    private NOP(final String category) {
      // ensure inheritance
      super(category);

      // configure the logger
      this.delegate.setLevel(Level.OFF);
      this.delegate.setUseParentHandlers(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: loggable
    /**
     ** Check if a message of the given level would actually be logged by this
     ** logger.
     ** <p>
     ** This check is based on the Loggers effective level, which may be
     ** inherited from its parent.
     **
     ** @param  level            a message logging level.
     **
     ** @return                  <code>true</code> if the log level is currently
     **                          being logged.
     */
    @Override
    public final boolean loggable(final Level level) {
      return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatal
    /**
     ** Logs an fatal error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void fatal(final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatal
    /**
     ** Logs an fatal error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void fatal(final String message, final Throwable throwable) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatal
    /**
     ** Logs an fatal error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  clazz            the class where the logging event has occurred.
     ** @param  method           the name of the method where the logging
     **                          event has occurred.
     ** @param  throwable        the <code>Throwable</code>.
     */
    @Override
    public final void fatal(final Class<Loggable> clazz, final String method, final Throwable throwable) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatal
    /**
     ** Logs an fatal error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  className        the name of the class where the logging event
     **                          has occurred.
     ** @param  method           the name of the method where the logging
     **                          event has occurred.
     ** @param  message          the message to log.
     */
    @Override
    public final void fatal(final String className, final String method, final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: fatal
    /**
     ** Logs an fatal error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  className        the name of the class where the logging event
     **                          has occurred.
     ** @param  method           the name of the method where the logging
     **                          event has occurred.
     ** @param  throwable        the <code>Throwable</code>.
     */
    @Override
    public final void fatal(final String className, final String method, final Throwable throwable) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: error
    /**
     ** Logs an application error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void error(final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: error
    /**
     ** Logs an application error to the associated <code>Logger</code>.
     ** <p>
     ** The no operation implementation.
     **
     ** @param  message          the message to log.
     ** @param  throwable        the exception to log.
     */
    @Override
    public final void error(final String message, final Throwable throwable) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: warn
    /**
     ** Writes a warning message to the associated <code>Logger</code>.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void warn(final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: warn
    /**
     ** Logs an application warning to the associated <code>Logger</code>.
     **
     ** @param  className        the name of the class where the logging event
     **                          was occurred.
     ** @param  method           the name of the method where the logging
     **                          event was occurred.
     ** @param  message          the message to log.
     */
    @Override
    public final void warn(final String className, final String method, final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: info
    /**
     ** Writes an informational message to the associated <code>Logger</code>.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void info(final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: debug
    /**
     ** Writes a debug message to the associated <code>Logger</code>.
     **
     ** @param  message          the message to log.
     */
    @Override
    public final void debug(final String message) {
      // NOP
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: debug
    /**
     ** Writes a debug message to the associated <code>Logger</code>.
     **
     ** @param  className        the name of the class where the logging event
     **                          was occurred.
     ** @param  method           the name of the method where the logging
     **                          event was occurred.
     ** @param  message          the message to log.
     */
    @Override
    public final void debug(final String className, final String method, final String message) {
      // NOP
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Logger</code> for the specified logging category.
   **
   ** @param  category           the category for the Logger.
   */
  public Logger(final String category) {
    this.delegate = java.util.logging.Logger.getLogger(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   level
  /**
   ** Returns the level that would actually be logged by this logger.
   **
   ** @return                    the level that would actually be logged by this
   **                            logger.
   */
  public final int level() {
    return this.delegate.getLevel().intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Set a filter to control output on this Logger.
   ** <p>
   ** After passing the initial "level" check, the Logger will call this
   ** {@link Filter} to check if a log record should really be published.
   **
   ** @param  filter             a {@link Filter} object (may be
   **                            <code>null</code>).
   **
   ** @throws SecurityException  if a security manager exists and if the caller
   **                            does not have LoggingPermission("control").
     */
  public final void filter(final Filter filter)
    throws SecurityException {

    this.delegate.setFilter(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the current {@link Filter} for this Logger.
   **
   ** @return                    a {@link Filter} object (may be
   **                            <code>null</code>).
   */
  public final Filter getFilter() {
    return this.delegate.getFilter();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatalLevel
  /**
   ** Check if a message of level {@link #FATAL} the would actually be logged by
   ** this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for fatel
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean fatalLevel() {
    return loggable(FATAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorLevel
  /**
   ** Check if a message of level {@link #ERROR} the would actually be logged by
   ** this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for error
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean errorLevel() {
    return loggable(ERROR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warningLevel
  /**
   ** Check if a message of level {@link #WARNING} the would actually be logged
   ** by this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for warning
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean warningLevel() {
    return loggable(WARNING);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notificationLevel
  /**
   ** Check if a message of level {@link #NOTIFICATION} the would actually be
   ** logged by this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for
   **                            information is enabled; otherwise
   **                            <code>false</code>
   */
  public final boolean notificationLevel() {
    return loggable(NOTIFICATION);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debugLevel
  /**
   ** Check if a message of level {@link #DEBUG} the would actually be logged by
   ** this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean debugLevel() {
    return loggable(DEBUG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traceLevel
  /**
   ** Check if a message of level {@link #TRACE} the would actually be logged by
   ** this logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   */
  public final boolean traceLevel() {
    return loggable(TRACE);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:  loggable
  /**
   ** Check if a message of the given level would actually be logged by this
   ** logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @param  level              a message logging level.
   **
   ** @return                    <code>true</code> if the log level is currently
   **                            being logged.
   */
  public boolean loggable(final Level level) {
    return this.delegate.isLoggable(level);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a new {@link Logger} for the specified category.
   **
   ** @param  category           the category for the Logger.
   **
   ** @return                    a new {@link Logger}.
   */
  public static Logger create(final String category) {
    return new Logger(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nop
  /**
   ** Creates a <code>Logger</code> that writes nothing.
   **
   ** @param  category         the category for the Logger.
   **
   ** @return                  the NOP-Logger.
   */
  public static final Logger nop(final String category) {
    return new NOP(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the name for this logger.
   **
   ** @return                    the logger name.
   **                            Will be <code>null</code> for anonymous
   **                            Loggers.
   */
  public final String category() {
    return this.delegate.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void fatal(final String message) {
    log(FATAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void fatal(final Throwable throwable) {
    fatal(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void fatal(final String message, final Throwable throwable) {
    log(FATAL, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event has occurred.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void fatal(final Class<Loggable> clazz, final String method, final Throwable throwable) {
    log(FATAL, LoggingBundle.message(ClassUtility.shortName(clazz), method, throwable.getLocalizedMessage()), throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            has occurred.
   ** @param  method             the name of the method where the logging
   **                            event has occurred.
   ** @param  message            the message to log.
   */
  public void fatal(final String className, final String method, final String message) {
    log(FATAL, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            has occurred.
   ** @param  method             the name of the method where the logging
   **                            event has occurred.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void fatal(final String className, final String method, final Throwable throwable) {
    log(FATAL, LoggingBundle.message(className, method, throwable.getLocalizedMessage()), throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void error(final String message) {
    log(ERROR, message);
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
    error(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void error(final String message, final Throwable throwable) {
    log(ERROR, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to the associated <code>Logger</code>.
   **
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  public void error(final Class<Loggable> clazz, final String method, final String message) {
    error(ClassUtility.shortName(clazz), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to the associated <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  public void error(final String className, final String method, final String message) {
    log(ERROR, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void warn(final String message) {
    log(WARNING, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to the associated <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void warn(final Throwable throwable) {
    warn(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void warn(final String message, final Throwable throwable) {
    log(WARNING, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to the associated <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  public void warn(final String className, final String method, final String message) {
    log(WARNING, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void info(final String message) {
    log(NOTIFICATION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void debug(final String message) {
    log(DEBUG, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  public void debug(final String className, final String method, final String message) {
    debug(LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public void trace(final String message) {
    log(TRACE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a trace message to the associated <code>Logger</code>.
   **
   ** @param  categorie          the name of the categorie where the logging
   **                            event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  public void trace(final String categorie, final String method, final String message) {
    trace(LoggingBundle.message(categorie, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message "ENTRY", log level FINER, and the given
   ** sourceMethod and sourceClass is logged.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method that is being entered.
   */
  public void entering(final String clazz, final String method) {
    this.delegate.entering(clazz, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with one parameter.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message "ENTRY", log level FINER, and the given
   ** sourceMethod and sourceClass and parameter is logged.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method that is being entered.
   ** @param  parameter          parameter to the method being entered.
   */
  public void entering(final String clazz, final String method, final Object parameter) {
    this.delegate.entering(clazz, method, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with an array of parameters.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message "ENTRY", log level FINER, and the given
   ** sourceMethod and sourceClass and parameters is logged.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method that is being entered.
   ** @param  parameter          parameters to the method being entered.
   */
  public void entering(final String clazz, final String method, final Object[] parameter) {
    this.delegate.entering(clazz, method, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log returning from a
   ** method. A {@link Record} with message "RETURN", log level FINER, and the
   ** given sourceMethod and sourceClass is logged.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method that is being returned from.
   */
  public void exiting(final String clazz, final String method) {
    this.delegate.exiting(clazz, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log returning from a
   ** method. A {@link Record} with message "RETURN", log level FINER, and the
   ** given sourceMethod and sourceClass, and result object is logged.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method that is being returned from.
   ** @param  result             the Object that is being returned.
   */
  public void exiting(final String clazz, final String method, final Object result) {
    this.delegate.exiting(clazz, method, result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwing
  /**
   ** Log throwing an exception.
   ** <p>
   ** This is a convenience method to log that a method is terminating by
   ** throwing an exception. The logging is done using the FINER level.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers. The {@link Record}'s message is set to
   ** "THROW".
   ** <p>
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  clazz              the name of class that issued the logging
   **                            request.
   ** @param  method             the name of method.
   ** @param  throwable          the <code>Throwable</code> that is being
   **                            thrown.
   */
  public void throwing(final String clazz, final String method, final Throwable throwable) {
    this.delegate.throwing(clazz, method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Writes a trace message to the associated <code>Logger</code>.
   **
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   */
  public void log(final String message) {
    log(DEBUG, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with no arguments.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given message is forwarded to all the registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   */
  public void log(final Level level, final String message) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with one object parameter.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given message is forwarded to all the registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          parameter to the message.
   */
  public void log(final Level level, final String message, final Object parameter) {
    final Object[] parameters = { parameter };
    log(level, message, parameters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with an array of object arguments.
   ** <p>
   ** If the logger is currently enabled for the given message level then a
   ** corresponding {@link Record} is created and forwarded to all the
   ** registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          array of parameters to the message.
   */
  public void log(final Level level, final String message, final Object[] parameter) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setParameters(parameter);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with associated <code>Throwable</code> information.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers.
   ** <p>
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the {@link Record} message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void log(final Level level, final String message, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with associated <code>Throwable</code> information.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers.
   ** <p>
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the {@link Record} message property.
   ** <p>
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          array of parameters to the message.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void log(final Level level, final String message, final Object[] parameter, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setParameters(parameter);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with no arguments.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given message is forwarded to all the registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   */
  public void log(final Level level, final String clazz, final String method, final String message) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(clazz);
      record.setSourceMethodName(method);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with one object parameter.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given message is forwarded to all the registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          parameter to the message.
   */
  public void log(final Level level, final String clazz, final String method, final String message, final Object parameter) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(clazz);
      record.setSourceMethodName(method);
      final Object[] params = { parameter };
      record.setParameters(params);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with an array of object arguments.
   ** <p>
   ** If the logger is currently enabled for the given message level then a
   ** corresponding {@link Record} is created and forwarded to all the
   ** registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          array of parameters to the message.
   */
  public void log(final Level level, final String clazz, final String method, final String message, final Object[] parameter) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(clazz);
      record.setSourceMethodName(method);
      record.setParameters(parameter);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with associated <code>Throwable</code> information.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers.
   ** <p>
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the {@link Record} message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void log(final Level level, final String clazz, final String method, final String message, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(clazz);
      record.setSourceMethodName(method);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Log a message, with associated <code>Throwable</code> information.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers.
   ** <p>
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the {@link Record} message property.
   ** <p>
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   ** @param  clazz              the class where the logging event has occurred.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   ** @param  parameter          array of parameters to the message.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   */
  public void log(final Level level, final String clazz, final String method, final String message, final Object[] parameter, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(clazz);
      record.setSourceMethodName(method);
      record.setParameters(parameter);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doLog
  /**
   ** Log a {@link Record}.
   ** <p>
   ** All the other logging methods in this class call through this method to
   ** actually perform any logging. Subclasses can override this single method
   ** to capture all log activity.
   **
   ** @param  record             the {@link Record} to be published.
   */
  private void doLog(final Record record) {
    record.setLoggerName(this.delegate.getName());
    final String bundle = this.delegate.getResourceBundleName();
    if (bundle != null) {
      record.setResourceBundleName(bundle);
      record.setResourceBundle(this.delegate.getResourceBundle());
    }
    this.delegate.log(record);
  }
}