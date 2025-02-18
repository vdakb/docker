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

    File        :   AbstractConnectionPool.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractConnectionPool.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import oracle.mds.core.MDSSession;
import oracle.mds.core.SessionOptions;
import oracle.mds.core.IsolationLevel;

import oracle.iam.platform.Platform;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractConnectionPool
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AbstractConnectionPool</code> implements the base functionality
 ** of an Oracle Identity Manager Pooled Connection for any Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AbstractConnectionPool implements AbstractMetadataTask {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // highly unsecure but there is no chance to obtain a logger from somewhere
  // due to subclasses are instantiated by the engine itself hence a zero
  // argument based constructor must exists.
  protected Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractConnectionPool</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractConnectionPool() {
    // ensure inheritance
    super();
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
   */
  @Override
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Writes a critical message to the associated {@link Logger}.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  throwable          the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable throwable) {
    if (this.logger != null)
      this.logger.fatal(this.logger.category(), method, throwable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes a non-critical message to the assined {@link Logger}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public void error(final String method, final String message) {
    if (this.logger != null)
      this.logger.error(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated {@link Logger}.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String message) {
    if (this.logger != null)
      this.logger.warn(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Logs an warning error to the associated {@link Logger}.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void warning(final String method, final String message) {
    if (this.logger != null)
      this.logger.warn(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes a informational message to the associated {@link Logger}.
   **
   ** @param  message            the message to log.
   */
  @Override
  public final void info(final String message) {
    if (this.logger != null)
      this.logger.info(message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated {@link Logger}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void debug(final String method, final String message) {
    if (this.logger != null)
    this.logger.debug(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a trace message to the associated {@link Logger}.
   ** <br>
   ** Convenience wrapper for sub classes.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to log.
   */
  @Override
  public final void trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.logger.category(), method, message);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession (AbstractMetadataTask)
  /**
   ** Creates a session to the Metadata Store.
   **
   ** @return                    the created {@link MDSSession}.
   */
  @Override
  public MDSSession createSession() {
    // create a session to the Metadata Store
    final SessionOptions option = new SessionOptions(IsolationLevel.READ_COMMITTED, null, null);
    return createSession(option);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createSession (AbstractMetadataTask)
  /**
   ** Creates a session to the Metadata Store with specific session options and
   ** no state handlers.
   **
   ** @param  option             the {@link SessionOptions} to create the
   **                            {@link MDSSession}.
   **
   ** @return                    the created {@link MDSSession}.
   */
  @Override
  public MDSSession createSession(final SessionOptions option) {
    final String method = "createSession";
    trace(method, SystemMessage.METHOD_ENTRY);
    MDSSession  session  = null;
    try {
      // create a session to the Metadata Store uising the session options and
      // without any specific state handlers
      session = Platform.getMDSInstance().createSession(option, null);
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return session;
  }
}