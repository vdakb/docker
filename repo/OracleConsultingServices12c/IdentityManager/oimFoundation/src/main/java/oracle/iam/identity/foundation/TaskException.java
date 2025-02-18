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

    File        :   TaskException.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskException.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.resource.ListResourceBundle;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class TaskException
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Exception class for throwing from <code>AbstractTask</code> if any goes
 ** wrong during execution aof a task.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class TaskException extends SystemException {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8256025558155138165")
  private static final long serialVersionUID = -4046743256670495565L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default Constructor
   ** <p>
   ** Create a new <code>TaskException</code> with <code>null</code> as its
   ** detail message.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   */
  protected TaskException() {
    // ensure ineritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a messageCode.
   **
   ** @param  code               the resource key for the exception message.
   */
  public TaskException(final String code) {
    // ensure inheritance
    super(TaskBundle.RESOURCE, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   */
  public TaskException(final ListResourceBundle bundle, final String code) {
    // ensure inheritance
    super(bundle, code);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a messageCode.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public TaskException(final String code, final String parameter) {
    // ensure inheritance
    this(TaskBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final String parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a messageCode.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public TaskException(final String code, final String[] parameter) {
    // ensure inheritance
    this(TaskBundle.RESOURCE, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final String[] parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code.
   ** <p>
   ** The cause is not initialized, and may subsequently be initialized by a
   ** call to {@link #initCause}.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the substitutions for placholders contained in
   **                            the message regarding to <code>code</code>.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final Object... parameter) {
    // ensure inheritance
    super(bundle, code, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> and passes it the causing
   ** exception.
   **
   ** @param  causing            the causing exception.
   */
  public TaskException(final Throwable causing) {
    this(TaskError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public TaskException(final String code, final Throwable causing) {
    // ensure inheritance
    this(TaskBundle.RESOURCE, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public TaskException(final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    this(TaskBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> from a code and passes it the
   ** causing exception.
   **
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public TaskException(final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    this(TaskBundle.RESOURCE, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> and passes it the causing
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  causing            the causing exception.
   */
  public TaskException(final ListResourceBundle bundle, final Throwable causing) {
    // ensure inheritance
    this(bundle, TaskError.UNHANDLED, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> and passes it the causing
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the detail message.
   ** @param  causing            the causing exception.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final Throwable causing) {
    // ensure inheritance
    super(bundle, code, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> and passes it the causing
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the format pattern of the
   **                            detail message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final String parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>TaskException</code> and passes it the causing
   ** exception.
   **
   ** @param  bundle             the resource bundle where the message will be
   **                            obtained.
   ** @param  code               the resource key for the exception message.
   ** @param  parameter          the parameter context where this exception
   **                            belongs to.
   ** @param  causing            the causing exception.
   */
  public TaskException(final ListResourceBundle bundle, final String code, final String[] parameter, final Throwable causing) {
    // ensure inheritance
    super(bundle, code, parameter, causing);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unhandled
  /**
   ** Factory method to create a new unhandled <code>TaskException</code>.
   **
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException unhandled(final Throwable cause) {
    return new TaskException(cause);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new not recoverable <code>TaskException</code>.
   **
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException general(final Throwable cause) {
    return general(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   general
  /**
   ** Factory method to create a new not recoverable <code>TaskException</code>.
   **
   ** @param  message            the message explaining the reason.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException general(final String message) {
    return new TaskException(TaskError.GENERAL, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new recoverable <code>TaskException</code>.
   **
   ** @param  cause              the root cause of the exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException abort(final Throwable cause) {
    return abort(cause.getLocalizedMessage());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   abort
  /**
   ** Factory method to create a new recoverable <code>TaskException</code>.
   **
   ** @param  message            the message explaining the reason.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException abort(final String message) {
    return new TaskException(TaskError.ABORT, message);
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNotFound
  /**
   ** Factory method to create a recoverable <code>TaskException</code> about
   ** class not found.
   **
   ** @param  className          the name of the class to blame.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException classNotFound(final String className) {
    return new TaskException(TaskError.CLASSNOTFOUND, className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNotCreated
  /**
   ** Factory method to create a recoverable <code>TaskException</code> about
   ** class could not created.
   **
   ** @param  className          the name of the class to blame.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException classNotCreated(final String className) {
    return new TaskException(TaskError.CLASSNOTCREATE, className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   classNotCreated
  /**
   ** Factory method to create a recoverable <code>TaskException</code> about
   ** class could not be accessed.
   **
   ** @param  className          the name of the class to blame.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException classNoAccess(final String className) {
    return new TaskException(TaskError.CLASSNOACCESS, className);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   argumentIsNull
  /**
   ** Factory method to create a recoverable <code>TaskException</code> about
   ** passed arguments.
   **
   ** @param  argumentName       the name of the argument to blame.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link TaskException} wrapping the
   **                            given exception.
   **                            <br>
   **                            Possible object is {@link TaskException}.
   */
  public static TaskException argumentIsNull(final String argumentName) {
    return new TaskException(TaskError.ARGUMENT_IS_NULL, argumentName);
  }
}