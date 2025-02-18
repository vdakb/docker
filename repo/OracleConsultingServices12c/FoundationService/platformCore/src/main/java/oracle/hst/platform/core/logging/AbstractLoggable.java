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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   AbstractLoggable.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractLoggable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.logging;

import oracle.hst.platform.core.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractLoggable
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLoggable</code> implements the base functionality of a
 ** service loggable.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractLoggable<T extends AbstractLoggable> implements Loggable<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** these are for convenience the avoid a specific getter for this instance
   ** attributes.
   ** <p>
   ** Subclasses must not change these instance attributes.
   */
  protected final Logger logger;
  protected final String prefix;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractLogger</code> which is associated with the
   ** specified logging category.
   **
   ** @param  loggable           the {@link Loggable} which has
   **                            instantiated this
   **                            <code>AbstractLoggable</code>.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  protected AbstractLoggable(final Loggable loggable) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger = (loggable == null) ? null : loggable.logger();
    this.prefix = ClassUtility.packageName(this.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unwrap (Loggable)
  /**
   ** Returns the core {@link java.util.logging.Logger} wrapped by this
   ** <code>Logger</code>.
   **
   ** @return                    the core {@link java.util.logging.Logger}
   **                            wrapped by this <code>Logger</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link java.util.logging.Logger}.
   */
  @Override
  public final java.util.logging.Logger unwrap() {
    return this.logger == null ? null : this.logger.unwrap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger (Loggable)
  /**
   ** Returns the logger associated with this task.
   **
   ** @return                    the logger associated with this task.
   **                            <br>
   **                            Possible object is {@link Logger}.
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Writes a critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the exception as the reason to log.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public final AbstractLoggable<T> fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.prefix, method, throwable);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes a non-critical message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public AbstractLoggable<T> error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.prefix, method, message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public final AbstractLoggable<T> warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public final AbstractLoggable<T> warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.prefix, method, message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes a informational message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public final AbstractLoggable<T> info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  public final AbstractLoggable<T> debug(final String method, final String message) {
    if (this.logger != null)
      this.logger.debug(this.prefix, method, message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the <code>Logger</code> of the owning
   ** {@link Loggable}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final AbstractLoggable<T> trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.prefix, method, message);
    return this;
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
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  public AbstractLoggable<T> entering(final String method) {
    this.logger.entering(this.prefix, method);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with an array of parameters.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@link Record} with message "ENTRY", log level FINER, and the given
   ** source method and source class and parameter is logged.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  public AbstractLoggable<T> entering(final String method, final Object... parameter) {
    this.logger.entering(this.prefix, method, parameter);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@link Record} with message "RETURN", log level FINER, and the given
   ** source method and source class and result object is logged.
   **
   ** @param  method             the name of method that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  result             the Object that is being returned.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  public AbstractLoggable<T> exiting(final String method, final Object... result) {
    this.logger.exiting(this.prefix, method, result);
    return this;
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
   ** Note that the thrown argument is stored in the {@link Record} thrown
   ** property, rather than the {@link Record} parameters property. Thus is it
   ** processed specially by output Formatters and is not treated as a
   ** formatting parameter to the LogRecord message property.
   **
   ** @param  method             the name of method.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> that is being
   **                            thrown.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable} of
   **                            type <code>T</code>.
   */
  public AbstractLoggable<T> throwing(final String method, final Throwable throwable) {
    this.logger.throwing(this.prefix, method, throwable);
    return this;
  }
}