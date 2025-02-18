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

    File        :   AbstractLogger.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractLogger.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.logging;

import java.util.Date;

import oracle.iam.identity.icf.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractLogger
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLogger</code> implements the base functionality of an
 ** Identity Connector Framework Logger.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AbstractLogger<T extends AbstractLogger> implements Loggable<T> {

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
   ** @param  category           the category for the {@link Logger}.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected AbstractLogger(final String category) {
    // ensure inheritance
    this(Logger.create(category));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractLogger</code> leveraging the specified
   ** {@link Logger}.
   **
   ** @param  logger             the associated {@link Logger}.
   **                            <br>
   **                            Allowed object is {@link Logger}.
   */
  protected AbstractLogger(final Logger logger) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger = logger;
    this.prefix = ClassUtility.shortName(this);
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.prefix, method, throwable);
    return (T)this;
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.prefix, method, message);
    return (T)this;
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
    return (T)this;
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.prefix, method, message);
    return (T)this;
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
    return (T)this;
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
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T debug(final String method, final String message) {
    if (this.logger != null)
      this.logger.debug(this.prefix, method, message);
    return (T)this;
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link AbstractLoggable} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link AbstractLoggable}.
   */
  @Override
  @SuppressWarnings({"cast", "unchecked"})
  public final T trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.prefix, method, message);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescapePrefix
  /**
   ** Returns the unescaped value for the lookup fields after removing the '~'
   ** delimiter.
   **
   ** @param  value              contains the field value to be unescaped. For
   **                            example: 1~XYZ.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    string containing the unescaped value. For
   **                            example: XYZ
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String unescapePrefix(final String value) {
    final int    delimiter = '~';
    // this is fail safe as long if the entitlement itself does not contain
    // the delimiter
    return value.substring(value.lastIndexOf(delimiter) + 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void entering(final String method) {
    this.logger.entering(this.prefix, method);
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
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void exiting(final String method) {
    this.logger.exiting(this.prefix, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemTime
  /**
   ** The current time will be different from this system time.
   ** <p>
   ** To be able to plugin your own method this is the placeholder method to
   ** fetch the server time from the target system.
   ** <p>
   ** This current implementation does not ask the connected server for
   ** anything; if returns simple the current data of the machine where this
   ** library is used.
   **
   ** @return                    the timestamp of the local system.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public final Date systemTime() {
    return new Date();
  }
}