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
    Subsystem   :   Java Server Faces Feature

    File        :   ManagedBean.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ManagedBean.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.state;

import java.io.Serializable;

import oracle.hst.platform.core.logging.Logger;
import oracle.hst.platform.core.logging.Loggable;

import oracle.hst.platform.core.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class ManagedBean
// ~~~~~ ~~~~~~~~~~~
/**
 ** The <code>ManagedBean</code> backing bean provideing access to the
 ** Foundation Application API.
 **
 ** @param  <T>                  the type of the loggable implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              implementing this interface (loggables can
 **                              return their own specific type instead of type
 **                              defined by this interface only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
public class ManagedBean<T extends ManagedBean> implements Loggable<T>
                                                ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4022133537436382540")
  private static final long serialVersionUID = -4231180352799557916L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String clazz;
  private final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ManagedBean</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected ManagedBean() {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.clazz  = ClassUtility.packageName(this.getClass());
    this.logger = Logger.create(this.clazz);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatalLevel
  /**
   ** Check if a message of level {@link Logger#FATAL} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for fatel
   **                            is enabled; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean fatalLevel() {
    return this.logger.fatalLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   errorLevel
  /**
   ** Check if a message of level {@link Logger#ERROR} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for error
   **                            is enabled; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean errorLevel() {
    return this.logger.errorLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warningLevel
  /**
   ** Check if a message of level {@link Logger#WARNING} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for warning
   **                            is enabled; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean warningLevel() {
    return this.logger.warningLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notificationLevel
  /**
   ** Check if a message of level {@link Logger#NOTIFICATION} the would actually
   ** be logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for
   **                            information is enabled; otherwise
   **                            <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean notificationLevel() {
    return this.logger.notificationLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debugLevel
  /**
   ** Check if a message of level {@link Logger#DEBUG} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean debugLevel() {
    return this.logger.debugLevel();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traceLevel
  /**
   ** Check if a message of level {@link Logger#TRACE} the would actually be
   ** logged by the associated logger.
   ** <p>
   ** This check is based on the Loggers effective level, which may be inherited
   ** from its parent.
   **
   ** @return                    <code>true</code> if the log level for tracing
   **                            is enabled; otherwise <code>false</code>
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public final boolean traceLevel() {
    return this.logger.traceLevel();
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
   ** Logs an fatal error to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  throwable          the <code>Throwable</code> associated with log
   **                            message.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public ManagedBean<T> fatal(final String location, final Throwable throwable) {
    this.logger.fatal(this.clazz, location, throwable);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Logs an normal error to an associated <code>Logger</code>.
   **
   ** @param  location           the location where the logging event occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> error(final String location, final String message) {
    this.logger.error(this.clazz, location, message);
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
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> warning(final String message) {
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
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> warning(final String method, final String message) {
    this.logger.warn(this.clazz, method, message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes an informational message to an associated <code>Logger</code>.
   **
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> info(final String message) {
    this.logger.info(message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> debug(final String method, final String message) {
    this.logger.debug(this.clazz, method, message);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging event
   **                            was occurred.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  message            the message to log.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link ManagedBean} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is {@link ManagedBean} of type
   **                            <code>T</code>.
   */
  @Override
  public final ManagedBean<T> trace(final String method, final String message) {
    this.logger.trace(this.clazz, method, message);
    return this;
  }
}