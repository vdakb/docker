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

    File        :   AbstractFrontend.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractFrontend.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-06-23  DSteding    First release version
*/

package oracle.hst.deployment;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.Reference;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFrontend
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Provides basic implementations to handle Apache Ant Tasks.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractFrontend extends    Task
                                       implements ServiceFrontend {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** flag to control verbosity. */
  private boolean verbose     = false;

  /** flag to control behavior of failure handling. */
  private boolean failonerror = true;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractFrontend</code>.
   */
  public AbstractFrontend() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setVerbose
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>verbose</code>.
   ** <p>
   ** Set the flag indicating that verbosity is requested.
   **
   ** @param verbose             <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  public void setVerbose(final boolean verbose) {
    this.verbose = verbose;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setFailOnError
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>failonerror</code>.
   ** <p>
   ** Sets how the task behavior will be in case an error is detected.
   **
   ** @param  failonerror        how the task behavior will be in case an error
   **                            is detected.
   */
  public final void setFailOnError(final boolean failonerror) {
    this.failonerror = failonerror;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verbose (ServiceFrontend)
  /**
   ** Returns how the task will provide verbosity of operations.
   **
   ** @return                    <code>true</code> if verbosity is requested;
   **                            otherwise <code>false</code>.
   */
  @Override
  public final boolean verbose() {
    return this.verbose;
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
  public final boolean failonerror() {
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
  public final void fatal(final Throwable what) {
    System.err.println(what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (ServiceFrontend)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String location, final Throwable what) {
    System.err.println(what);
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
    String[] values = { argument };
    error(pattern, values);
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
    log(message, Project.MSG_ERR);
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
    String[] arguments = { argument };
    warning(pattern, arguments);
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
    log(message, Project.MSG_WARN);
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
    String[] arguments = { argument };
    info(pattern, arguments);
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
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    log(message, Project.MSG_INFO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void trace(final String pattern, final String what) {
    String[] arguments = { what };
    trace(pattern, arguments);
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
    log(message, Project.MSG_VERBOSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (ServiceFrontend)
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  @Override
  public final void debug(final String pattern, final String what) {
    String[] arguments = { what };
    debug(pattern, arguments);
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
    log(message, Project.MSG_DEBUG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeError
  /**
   ** Creates and throws a {@link BuildException} about an attribute mixup
   **
   ** @param  attributeName      the name of the attribute mixed.
   **
   ** @throws BuildException     always.
   */
  protected void handleAttributeError(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, attributeName), getLocation());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleAttributeMissing
  /**
   ** Creates and throws a {@link BuildException} about an attribute is missing.
   **
   ** @param  attributeName      the name of the missing attribute.
   **
   ** @throws BuildException     always.
   */
  protected void handleAttributeMissing(final String attributeName)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TASK_ATTRIBUTE_MISSING, attributeName), getLocation());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   handleReferenceError
  /**
   ** Creates and throws a {@link BuildException} about an attribute mixup
   **
   ** @param  reference          the {@link Reference} of the violated context type.
   ** @param  contextType        the name of the violated context type.
   ** @param  failedClass        the class name that violates the context
   **
   ** @throws BuildException     always.
   */
  protected void handleReferenceError(final Reference reference, final String contextType, final Class<?> failedClass)
    throws BuildException {

    final Object[] parameter = {reference.getRefId(), contextType, reference.getRefId(), failedClass.getName() };
    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
  }
}