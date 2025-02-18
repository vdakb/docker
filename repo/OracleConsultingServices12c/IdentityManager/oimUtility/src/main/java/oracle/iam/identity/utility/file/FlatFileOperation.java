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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileOperation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.iam.identity.utility.resource.FlatFileBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class FlatFileOperation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The base class of flat file operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public abstract class FlatFileOperation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the recommended prefix of temporary file produced by any subclass of
   ** <code>FlatFileOperation</code>
   */
  public static final String  TMPPREFIX    = "~txt";

  /**
   ** the recommended file extension of temporary file produced by any subclass
   ** of <code>FlatFileOperation</code>
   */
  public static final String  TMPEXTENSION = ".tmp";

  /**
   ** the recommended file extension of text files produced or consumned by any
   ** subclass of <code>FlatFileOperation</code>
   */
  public static final String  TXTEXTENSION = ".txt";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean             validated    = false;

  /** describing a particular line of the file */
  private final FlatFileDescriptor descriptor;

  /** the logger instance for tracing */
  private Logger              logger;

  /** the logger instance for tracing */
  private String              category     = ClassUtility.shortName(this.getClass().getName());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FlatFileOperation</code> with the specified
   ** {@link FlatFileDescriptor}.
   **
   ** @param  descriptor         the {@link FlatFileDescriptor} used to extract the
   **                            content form the FlatFile files.
   */
  public FlatFileOperation(final FlatFileDescriptor descriptor) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.descriptor = descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Set the log writer for this FlatFile operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** FlatFile file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>FlatFileOperation</code> object is created the logger is
   ** initially <code>null</code>, in other words, logging is disabled.
   **
   ** @param  logger             the new logger; to disable, set to null.
   */
  public void logger(final Logger logger) {
    this.logger = logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Returns the log writer for this FlatFile operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** FlatFile file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>FlatFileOperation</code> object is created the logger is
   ** initially <code>null</code>, in other words, logging is disabled.
   **
   ** @return                    the log writer for this FlatFile operation.
   */
  public Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns the {@link FlatFileDescriptor} of this <code>FlatFileOperation</code>.
   **
   ** @return                    the {@link FlatFileDescriptor} of this
   **                            <code>FlatFileOperation</code>.
   */
  public final FlatFileDescriptor descriptor() {
    return this.descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validated
  /**
   ** Returns <code>true</code> if this <code>FlatFileOperation</code> has validated
   ** the {@link FlatFileDescriptor}.
   **
   ** @return                    <code>true</code> if this
   **                            <code>FlatFileOperation</code> has validated the
   **                            {@link FlatFileDescriptor}.
   */
  public boolean validated() {
    return this.validated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  public void error(final String method, final String resourceKey) {
    if (this.logger != null) {
      this.logger.error(category, method, FlatFileBundle.string(resourceKey));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  argument           argument to substitute the paceholders in
   **                            the pattern referd by <code>resourceKey</code>.
   */
  public void error(final String method, final String resourceKey, final String argument) {
    if (this.logger != null) {
      final String[] value = { argument };
      error(method, resourceKey, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error
  /**
   ** Writes a error message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  public void error(final String method, final String resourceKey, final String[] arguments) {
    if (this.logger != null) {
      this.logger.error(category, method, FlatFileBundle.format(resourceKey, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Writes a warning message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  protected void warn(final String method, final String resourceKey) {
    if (this.logger != null) {
      this.logger.warn(category, method, FlatFileBundle.string(resourceKey));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Writes a warning message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  argument           argument to substitute the paceholders in
   **                            the pattern referd by <code>resourceKey</code>.
   */
  protected void warn(final String method, final String resourceKey, final String argument) {
    if (this.logger != null) {
      final String[] value = { argument };
      warn(method, resourceKey, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warn
  /**
   ** Writes a warning message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected void warn(final String method, final String resourceKey, final String[] arguments) {
    if (this.logger != null) {
      this.logger.warn(category, method, FlatFileBundle.format(resourceKey, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Writes a informational message to the associated <code>Logger</code> if
   ** once is available.
   **
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  protected void info(final String resourceKey) {
    if (this.logger != null) {
      this.logger.info(FlatFileBundle.string(resourceKey));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   */
  public void debug(final String method, final String resourceKey) {
    if (this.logger != null) {
      this.logger.debug(this.category, method, FlatFileBundle.string(resourceKey));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  argument           argument to substitute the paceholders in
   **                            the pattern referd by <code>resourceKey</code>.
   */
  public void debug(final String method, final String resourceKey, final String argument) {
    if (this.logger != null) {
      final String[] value = { argument };
      debug(method, resourceKey, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug
  /**
   ** Writes a debug message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  resourceKey        the resource key of the message to write to the
   **                            log.
   ** @param  arguments          arguments to substitute the paceholders in
   **                            the given pattern.
   */
  protected void debug(final String method, final String resourceKey, final String[] arguments) {
    if (this.logger != null) {
      this.logger.debug(this.category, method, FlatFileBundle.format(resourceKey, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace
  /**
   ** Writes a trace message to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to write to the log.
   */
  protected void trace(final String method, final String message) {
    if (this.logger != null) {
      this.logger.trace(this.category, method, message);
    }
  }
}