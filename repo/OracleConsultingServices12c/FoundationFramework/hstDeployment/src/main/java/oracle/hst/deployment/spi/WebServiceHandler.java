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

    File        :   WebServiceHandler.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WebServiceHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-12-09  DSteding    First release version
*/

package oracle.hst.deployment.spi;

import java.util.List;
import java.util.ArrayList;

import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceException;

////////////////////////////////////////////////////////////////////////////////
// class WebServiceHandler
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Invokes a WebService in Oracle WebLogic Server domains.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WebServiceHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ServiceFrontend frontend;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Context
  // ~~~~~ ~~~~~~~
  public static class Context extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final List<WebServiceInstance> operation = new ArrayList<WebServiceInstance>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Context</code> type that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Context() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Context</code> with the specified name.
     **
     ** @param  packageName      the value set for the packageName property.
     ** @param  classLoader      the value set for the classLoader property.
     */
    public Context(final String packageName, final String classLoader) {
      // ensure inheritance
      super();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>WebServiceHandler</code> to initialize the
   ** instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public WebServiceHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super();

    // initialize instance
    this.frontend = frontend;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
   ** Logs an normal error to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void error(final String pattern, final String argument) {
    this.frontend.error(pattern, argument);
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
  ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  pattern            the given pattern and uses it to format the
   **                            given argument.
   ** @param  argument           argument to substitute the paceholder in
   **                            the given pattern.
   */
  protected final void warning(final String pattern, final String argument) {
    this.frontend.warning(pattern, argument);
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
  // Method:   warning
  /**
   ** Logs an warning error to the associated <code>Logger</code>.
   **
   ** @param  message            what is the reason to log.
   */
  protected final void warning(final String message) {
    this.frontend.warning(message);
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
  // Method:   info
  /**
   ** Writes an informational message to the associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   */
  protected final void info(final String message) {
    this.frontend.info(message);
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
  protected final void debug(final String pattern, final String what) {
    this.frontend.debug(pattern, what);
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
   **                            given argument
   ** @param  what               what is the reason to log.
   */
  protected final void trace(final String pattern, final String what) {
    this.frontend.trace(pattern, what);
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dispatch
  /**
   ** Save all changes done so far.
   **
   ** @return                    <code>true</code> if the operation succeeded;
   **                            otherwise <code>false</code>.
   **
   ** @throws ServiceException   in case a validation error does occur.
   */
  public boolean dispatch()
    throws ServiceException {

    boolean result = false;
    return result;
  }
}