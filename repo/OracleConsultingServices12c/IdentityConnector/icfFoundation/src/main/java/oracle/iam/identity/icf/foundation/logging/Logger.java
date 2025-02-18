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

    File        :   Logger.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Logger.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.logging;

import java.util.logging.Level;
import java.util.logging.Filter;

import oracle.iam.identity.icf.foundation.SystemConstant;

import oracle.iam.identity.icf.foundation.utility.ClassUtility;

import oracle.iam.identity.icf.foundation.resource.LoggingBundle;

////////////////////////////////////////////////////////////////////////////////
// class Logger
// ~~~~~ ~~~~~~
/**
 ** The <code>Logger</code> provides the mechanism to log informations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Logger {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

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
    @SuppressWarnings("compatibility:2360335041289386509")
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
   * A direct NOP (no operation) implementation of {@link Logger} .
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
     ** Logs an fatal error to this <code>Logger</code>.
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
     ** Logs an fatal error to this <code>Logger</code>.
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
     ** Logs an fatal error to this <code>Logger</code>.
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
     ** Logs an fatal error to this <code>Logger</code>.
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
     ** Logs an fatal error to this <code>Logger</code>.
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
     ** Logs an application error to this <code>Logger</code>.
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
     ** Logs an application error to this <code>Logger</code>.
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
     ** Writes a warning message to this <code>Logger</code>.
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
     ** Logs an application warning to this <code>Logger</code>.
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
     ** Writes an informational message to this <code>Logger</code>.
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
     ** Writes a debug message to this <code>Logger</code>.
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
     ** Writes a debug message to this <code>Logger</code>.
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
   ** Constructs a <code>Logger</code> for the specified named subsystem.
   ** <p>
   ** If a logger has already been created with the given name it is obtained;
   ** otherwise a new logger is created.
   **
   ** @param  category           the category for the Logger.
   **                            <br>
   **                            This should be a dot-separated name and should
   **                            usually be based on the package or class name
   **                            of the subsystem, such as java.net or
   **                            javax.swing.
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Possible object is <code>int</code>.
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
   **                            <br>
   **                            Allowed object is {@link Filter}.
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
   **                            <br>
   **                            Possible object is {@link Filter}.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    <code>true</code> if the log level is currently
   **                            being logged.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean loggable(final Level level) {
    return this.delegate.isLoggable(level);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   category
  /**
   ** Returns the name for this <code>Logger</code>.
   **
   ** @return                    the category for the <code>Logger</code>.
   **                            <br>
   **                            Will be <code>null</code> for anonymous
   **                            <code>Logger</code>s.
   **                            <br>
   **                            Possible object is @link String}.
   */
  public final String category() {
    return this.delegate.getName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory method to create a new <code>Logger</code> for the specified
   ** named subsystem.
   ** <p>
   ** If a logger has already been created with the given name it is obtained;
   ** otherwise a new logger is created.
   **
   ** @param  category           the category for the <code>Logger</code>.
   **                            <br>
   **                            This should be a dot-separated name and should
   **                            usually be based on the package or class name
   **                            of the subsystem, such as java.net or
   **                            javax.swing.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new <code>Logger</code>.
   **                            <br>
   **                            Possible object is <code>Logger</code>.
   */
  public static Logger create(final String category) {
    return new Logger(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nop
  /**
   ** Creates a <code>Logger</code> that writes nothing for the specified
   ** named subsystem.
   ** <p>
   ** If a logger has already been created with the given name it is obtained;
   ** otherwise a new <code>Logger</code> is created.
   **
   ** @param  category           the category for the <code>Logger</code>.
   **                            <br>
   **                            This should be a dot-separated name and should
   **                            usually be based on the package or class name
   **                            of the subsystem, such as java.net or
   **                            javax.swing.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a <code>/dev/null</code> <code>Logger</code>.
   **                            <br>
   **                            Possible object is <code>Logger</code>.
   */
  public static final Logger nop(final String category) {
    return new NOP(category);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void fatal(final String message) {
    log(FATAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void fatal(final Throwable throwable) {
    fatal(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void fatal(final String message, final Throwable throwable) {
    log(FATAL, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  clazz              the class where the logging event has occurred.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Loggable}.
   ** @param  method             the name of the method where the logging
   **                            event has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void fatal(final Class<Loggable> clazz, final String method, final Throwable throwable) {
    log(FATAL, LoggingBundle.message(ClassUtility.shortName(clazz), method, throwable.getLocalizedMessage()), throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            event has occurred.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void fatal(final String className, final String method, final String message) {
    log(FATAL, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to this <code>Logger</code>.
   **
   ** @param  prefix             the prefix of the class where the logging event
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            has occurred.
   ** @param  method             the name of the method where the logging
   **                            event has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void fatal(final String prefix, final String method, final Throwable throwable) {
    log(FATAL, LoggingBundle.message(prefix, method, throwable.getLocalizedMessage()), throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void error(final String message) {
    log(ERROR, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to this <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void error(final Throwable throwable) {
    error(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void error(final String message, final Throwable throwable) {
    log(ERROR, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to this <code>Logger</code>.
   **
   ** @param  clazz              the class where the logging event has occurred.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            {@link Loggable}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void error(final Class<Loggable> clazz, final String method, final String message) {
    error(ClassUtility.shortName(clazz), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an application error to this <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void error(final String className, final String method, final String message) {
    log(ERROR, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Writes a warning message to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void warn(final String message) {
    log(WARNING, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to this <code>Logger</code>.
   **
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void warn(final Throwable throwable) {
    warn(SystemConstant.EMPTY, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void warn(final String message, final Throwable throwable) {
    log(WARNING, message, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Logs an application warning to this <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void warn(final String className, final String method, final String message) {
    log(WARNING, LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void info(final String message) {
    log(NOTIFICATION, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void debug(final String message) {
    log(DEBUG, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to this <code>Logger</code>.
   **
   ** @param  className          the name of the class where the logging event
   **                            was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void debug(final String className, final String method, final String message) {
    debug(LoggingBundle.message(className, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a debug message to this <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void trace(final String message) {
    log(TRACE, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a trace message to this <code>Logger</code>.
   **
   ** @param  category           the name of the category where the logging
   **                            event has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void trace(final String category, final String method, final String message) {
    trace(LoggingBundle.message(category, method, message));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message <code>entry</code>, log level FINEST, and
   ** the given <code>prefix</code> and <code>method</code> is logged.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void entering(final String prefix, final String method) {
    log(Level.FINEST, LoggingBundle.message(prefix, method, Loggable.METHOD_ENTRY));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with one parameter.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message <code>entry</code>, log level FINEST, and
   ** the given <code>prefix</code> and <code>method</code> is logged.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public void entering(final String prefix, final String method, final Object parameter) {
    log(Level.FINEST, LoggingBundle.message(prefix, method, String.format("%s [%s]", Loggable.METHOD_ENTRY, parameter.toString())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with an array of parameters.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message <code>entry</code>, log level FINEST, and
   ** the given <code>prefix</code> and <code>method</code> is logged.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void entering(final String prefix, final String method, final Object[] parameter) {
    this.delegate.entering(prefix, method, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message <code>exit</code>, log level FINEST, and
   ** the given <code>prefix</code> and <code>method</code> is logged.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            <br>
   **                            Allowed object is {@link String}.
   **                            request.
   ** @param  method             the name of method that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void exiting(final String prefix, final String method) {
    log(Level.FINEST, LoggingBundle.message(prefix, method, Loggable.METHOD_EXIT));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** A {@link Record} with message <code>exit</code>, log level FINEST, and
   ** the given <code>prefix</code> and <code>method</code> is logged.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of method that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the Object that is being returned.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  public void exiting(final String prefix, final String method, final Object parameter) {
    log(Level.FINEST, LoggingBundle.message(prefix, method, String.format("%s [%s]", Loggable.METHOD_EXIT, parameter.toString())));
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
   ** registered output handlers. The {@link Record} 's message is set to
   ** "THROW".
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The thrown argument is stored in the {@link Record} thrown property,
   ** rather than the {@link Record} parameters property. Thus is it processed
   ** specially by output Formatters and is not treated as a formatting
   ** parameter to the LogRecord message property.
   **
   ** @param  prefix             the prefix of class that issued the logging
   **                            request.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> that is being
   **                            thrown.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void throwing(final String prefix, final String method, final Throwable throwable) {
    this.delegate.throwing(prefix, method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   ** Writes a trace message to this <code>Logger</code>.
   **
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
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
   ** Log a message, with an array of object arguments.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** corresponding {@link Record} is created and forwarded to all the
   ** registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          array of parameters to the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void log(final Level level, final String message, final Object... parameter) {
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
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
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
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          array of parameters to the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
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
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  prefix             the prefix of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void log(final Level level, final String prefix, final String method, final String message) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(prefix);
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
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  prefix             the prefix of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the message.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void log(final Level level, final String prefix, final String method, final String message, final Object parameter) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(prefix);
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
   ** If the logger is currently enabled for the given message level then the
   ** corresponding {@link Record} is created and forwarded to all the
   ** registered output Handler objects.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  prefix             the prefix of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          array of parameters to the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void log(final Level level, final String prefix, final String method, final String message, final Object... parameter) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(prefix);
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
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  prefix             the prefix of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void log(final Level level, final String prefix, final String method, final String message, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(prefix);
      record.setSourceMethodName(method);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   log
  /**
   * Log a message, with associated <code>Throwable</code> information.
   ** <p>
   ** If the logger is currently enabled for the given message level then the
   ** given arguments are stored in a {@link Record} which is forwarded to all
   ** registered output handlers.
   * <p>
   * Note that the thrown argument is stored in the {@link Record} thrown
   * property, rather than the {@link Record} parameters property. Thus is it
   * processed specially by output Formatters and is not treated as a
   * formatting parameter to the {@link Record} message property.
   **
   ** @param  level              one of the message level identifiers, e.g.
   **                            <code>SEVERE</code>.
   **                            <br>
   **                            Allowed object is {@link Level}.
   ** @param  prefix             the prefix of the class where the logging event
   **                            has occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the string message (or a key in the message
   **                            catalog).
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          array of parameters to the message.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public void log(final Level level, final String prefix, final String method, final String message, final Object[] parameter, final Throwable throwable) {
    if (loggable(level)) {
      final Record record = new Record(getClass(), level, message);
      record.setSourceClassName(prefix);
      record.setSourceMethodName(method);
      record.setParameters(parameter);
      record.setThrown(throwable);
      doLog(record);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   doLog
  /**
   ** Log a {@link Record} .
   ** <p>
   ** All the other logging methods in this class call through this method to
   ** actually perform any logging. Subclasses can override this single method
   ** to capture all log activity.
   **
   ** @param  record             the {@link Record} to be published.
   **                            <br>
   **                            Allowed object is {@link Record}.
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