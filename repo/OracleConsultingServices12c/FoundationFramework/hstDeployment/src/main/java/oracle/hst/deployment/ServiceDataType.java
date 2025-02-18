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

    File        :   ServiceDataType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceDataType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

////////////////////////////////////////////////////////////////////////////////
// abstract class ServiceDataType
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>ServiceDataType</code> is an extension to {@link DataType} that
 ** provides the basic implementation of an ANT DataType used in this context.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ServiceDataType extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServiceDataType</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ServiceDataType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  what               the exception as the reason to log.
   */
  public final void fatal(final Throwable what) {
    log(what.getLocalizedMessage(), Project.MSG_ERR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  public final void error(final String pattern, final String argument) {
    String[] values = { argument };
    error(pattern, values);
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
  public final void error(final String pattern, final Object[] arguments) {
    error(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Logs an normal error message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public final void error(final String message) {
    log(message, Project.MSG_ERR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
  ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  public final void warning(final String pattern, final String argument) {
    String[] arguments = { argument };
    warning(pattern, arguments);
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
  public final void warning(final String pattern, final Object[] arguments) {
    warning(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  public final void warning(final String message) {
    log(message, Project.MSG_WARN);
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
  public final void info(final String pattern, final String argument) {
    String[] arguments = { argument };
    info(pattern, arguments);
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
  public final void info(final String pattern, final Object[] arguments) {
    info(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  public final void info(final String message) {
    log(message, Project.MSG_INFO);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  public final void trace(final String pattern, final String what) {
    String[] arguments = { what };
    trace(pattern, arguments);
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
  public final void trace(final String pattern, final Object[] arguments) {
    trace(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   trace
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  public final void trace(final String message) {
    log(message, Project.MSG_VERBOSE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
  ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  public final void debug(final String pattern, final String what) {
    String[] arguments = { what };
    debug(pattern, arguments);
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
  public final void debug(final String pattern, final Object[] arguments) {
    trace(String.format(pattern, arguments));
  }

  //////////////////////////////////////////////////////////////////////////////
 // Method:   debug
 /**
  ** Writes a debug message to the associated <code>Logger</code>.
  **
  ** @param  message             what is the reason to log.
  */
  public final void debug(final String message) {
    log(message, Project.MSG_DEBUG);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the data type to use.
   ** <p>
   ** The defualt implementation does nothing.
   ** Subclasses are enforced to override this method to achive validation.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate() {
    // intentionally left blank
  }

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

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MIXEDUP, attributeName), getLocation());
  }

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

    throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_ATTRIBUTE_MISSING, attributeName));
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