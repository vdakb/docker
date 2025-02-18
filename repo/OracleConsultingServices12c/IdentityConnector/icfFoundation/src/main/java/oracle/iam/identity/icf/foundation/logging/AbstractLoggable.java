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

    File        :   AbstractLoggable.java

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

////////////////////////////////////////////////////////////////////////////////
// class AbstractLoggable
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLoggable</code> implements the base functionality of an
 ** Identity Connector Framework Producer.
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
   ** These are for convenience the avoid a specific getter for this instance
   ** attributes.
   */
  protected Logger logger;
  protected String prefix;

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
    this.prefix = getClass().getSimpleName();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Sets the logger associated with this task.
   **
   ** @param  value              the logger to associate with this task.
   **                            <br>
   **                            Allowed object is {@link Logger}.
   */
  public final void logger(final Logger value) {
    this.logger = value;
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
  public T error(final String method, final String message) {
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
   ** @param  message            the message to log.
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
    if (this.logger != null)
      this.logger.entering(this.prefix, method);
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
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public final void entering(final String method, final Object... parameter) {
    if (this.logger != null)
      this.logger.entering(this.prefix, method, parameter);
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
    if (this.logger != null)
      this.logger.exiting(this.prefix, method);
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
   ** @param  result             the Object that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void exiting(final String method, final Object result) {
    if (this.logger != null)
      this.logger.exiting(this.prefix, method, result);
  }

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
  public Date systemTime() {
    return new Date();
  }
}