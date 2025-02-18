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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractLoggable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractLoggable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Date;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class AbstractLoggable
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractLoggable</code> implements the base functionality
 ** of an Oracle Identity Manager Connector.
 ** <br>
 ** A connector can be any kind of outbound object as a provisioning target or a
 ** reconciliation source. It can also be an inbound object which is a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AbstractLoggable implements Loggable {

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
   ** Constructs a <code>AbstractLoggable</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  public AbstractLoggable(final Loggable loggable) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.logger = loggable == null ? null : loggable.logger();
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
   */
  @Override
  public final void fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.prefix, method, throwable);
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
   */
  @Override
  public void error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.prefix, method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public final void warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
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
   */
  @Override
  public final void warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.prefix, method, message);
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
   */
  @Override
  public final void info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
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
   */
  @Override
  public final void debug(final String method, final String message) {
    if (this.logger != null)
      this.logger.debug(this.prefix, method, message);
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
   */
  @Override
  public final void trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.prefix, method, message);
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
   ** A {@code Record} with message "ENTRY", log level FINER, and the given
   ** sourceMethod and sourceClass is logged.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void entering(final String method) {
    this.logger.entering(this.prefix, method);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entering
  /**
   ** Log a method entry, with an array of parameters.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@code Record} with message "ENTRY", log level FINER, and the given
   ** source method and source class and parameter is logged.
   **
   ** @param  method             the name of method that is being entered.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          parameter to the method being entered.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void entering(final String method, final Object... parameter) {
    this.logger.entering(this.prefix, method, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exiting
  /**
   ** Log a method return.
   ** <p>
   ** This is a convenience method that can be used to log entry to a method.
   ** <br>
   ** A {@code Record} with message "RETURN", log level FINER, and the given
   ** source method and source class and result object is logged.
   **
   ** @param  method             the name of method that is being returned.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  result             the Object that is being returned.
   **                            <br>
   **                            Allowed object is array of {@link Object}.
   */
  public void exiting(final String method, final Object... result) {
    this.logger.exiting(this.prefix, method, result);
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
   ** given arguments are stored in a {@code Record} which is forwarded to all
   ** registered output handlers. The {@code Record} 's message is set to
   ** "THROW".
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** The thrown argument is stored in the {@code Record} thrown property,
   ** rather than the {@code Record} parameters property. Thus is it
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
   */
  public void throwing(final String method, final Throwable throwable) {
    this.logger.throwing(this.prefix, method, throwable);
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
    final char delimiter = '~';
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
   ** anything; it returns simple the current data of the machine where this
   ** library is used.
   **
   ** @return                    the timestamp of the local system.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws TaskException      if the operation fails.
   */
  public Date systemTime()
    throws TaskException {

    return DateUtility.now();
  }
}