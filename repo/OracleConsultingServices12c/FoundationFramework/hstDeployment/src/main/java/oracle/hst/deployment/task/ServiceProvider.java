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

    File        :   ServiceProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceProvider.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.hst.deployment.task;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class ServiceProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** This is the abstract base class for Oracle WebLogic Domain services in
 ** general.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ServiceProvider implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ServiceFrontend frontend;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ServiceProvider</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  protected ServiceProvider(final ServiceFrontend frontend) {
    // ensure inheritance
    super();

    // initialize instance
    this.frontend = frontend;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Returns the logger associated with this instance.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   **
   ** @return                    the logger associated with this instance.
   */
  @Override
  public final Logger logger() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String location, final Throwable what) {
    this.frontend.fatal(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
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
    this.frontend.error(pattern, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  @Override
  public final void warning(final String message) {
    this.frontend.warning(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
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
    this.frontend.warning(pattern, argument);
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
    this.frontend.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void debug(final String pattern, final String what) {
    this.frontend.debug(pattern, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void trace(final String pattern, final String what) {
    this.frontend.trace(pattern, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   frontend
  /**
   ** Returns the {link ServiceFrontend} that instantiated this service.
   **
   ** @return                    the {link ServiceFrontend} that instantiated
   **                            this service
   */
  public final ServiceFrontend frontend() {
    return this.frontend;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verbose
  /**
   ** Returns the flag indicating if verbosity is requested or not.
   **
   ** @return                    <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  public final boolean verbose() {
    return this.frontend.verbose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   failonerror
  /**
   ** Returns how the task behavior will be in case an error is detected.
   **
   ** @return                    how the task behavior will be in case an error
   **                            is detected.
   */
  public final boolean failonerror() {
    return this.frontend.failonerror();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the service provider to perform.
   **
   ** @throws BuildException     in case an error does occur.
   */
  public abstract void validate();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeMissing
  /**
   ** Creates and throws a {@link BuildException} about an attribute mixup
   **
   ** @param  attributeName      the name of the attribute mixed.
   **
   ** @throws BuildException     always.
   */
  protected void handleAttributeMissing(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ATTRIBUTE_MISSING, attributeName));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  protected final void fatal(final Throwable what) {
    this.frontend.fatal(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes an normal error message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void error(final String pattern, final Object[] arguments) {
    this.frontend.error(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  protected final void error(final String message) {
    this.frontend.error(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Writes an warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void warning(final String pattern, final Object[] arguments) {
    this.frontend.warning(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
  ** Writes a informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void info(final String pattern, final String argument) {
    this.frontend.info(pattern, argument);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void info(final String pattern, final Object[] arguments) {
    this.frontend.info(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void debug(final String pattern, final Object[] arguments) {
    this.frontend.debug(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  protected final void debug(final String message) {
    this.frontend.debug(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given arguments
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected final void trace(final String pattern, final Object[] arguments) {
    this.frontend.trace(pattern, arguments);
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   trace
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  protected final void trace(final String message) {
    this.frontend.trace(message);
  }
}