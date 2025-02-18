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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVOperation.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.EOFException;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.logging.Logger;

import oracle.iam.identity.utility.resource.CSVBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class CSVOperation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** The base class of CSV file operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class CSVOperation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the recommended prefix of temporary file produced by any subclass of
   ** <code>CSVOperation</code>
   */
  public static final String  TMPPREFIX    = "~csv";

  /**
   ** the recommended file extension of temporary file produced by any subclass
   ** of <code>CSVOperation</code>
   */
  public static final String  TMPEXTENSION = ".tmp";

  /**
   ** the recommended file extension of CSV files produced or consumned by any
   ** subclass of <code>CSVOperation</code>
   */
  public static final String  CSVEXTENSION = ".csv";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean             validated    = false;

  /** describing the lines of the file */
  private final CSVDescriptor descriptor;

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
   ** Constructs a <code>CSVOperation</code> with the specified
   ** {@link CSVDescriptor}.
   **
   ** @param  descriptor         the {@link CSVDescriptor} used to extract the
   **                            content form the CSV files.
   */
  public CSVOperation(final CSVDescriptor descriptor) {
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
   ** Set the log writer for this CSV operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** CSV file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>CSVOperation</code> object is created the logger is
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
   ** Returns the log writer for this CSV operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** CSV file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>CSVOperation</code> object is created the logger is
   ** initially <code>null</code>, in other words, logging is disabled.
   **
   ** @return                    the log writer for this CSV operation.
   */
  public Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   descriptor
  /**
   ** Returns the {@link CSVDescriptor} of this <code>CSVOperation</code>.
   **
   ** @return                    the {@link CSVDescriptor} of this
   **                            <code>CSVOperation</code>.
   */
  public final CSVDescriptor descriptor() {
    return this.descriptor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validated
  /**
   ** Returns <code>true</code> if this <code>CSVOperation</code> has validated
   ** the {@link CSVDescriptor}.
   **
   ** @return                    <code>true</code> if this
   **                            <code>CSVOperation</code> has validated the
   **                            {@link CSVDescriptor}.
   */
  public boolean validated() {
    return this.validated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateDescriptor
  /**
   ** Reads a record from a CSV file and interprete it as the header of the
   ** file.
   ** <br>
   ** For this purpose it is important that the provided reader is not used
   ** before this method is invoked. We assume the first line of a CSV file
   ** is the header.
   **
   ** @param  reader             the {@link CSVReader} where the header will be
   **                            obtained from.
   **
   ** @throws CSVException       if the header could not be read or an declared
   **                            {@link CSVAttribute} is missing in the record.
   */
  protected void validateDescriptor(final CSVReader reader)
    throws CSVException {

    final String method = "validateDescriptor";

    if (!this.validated) {
      try {
        debug(method, CSVMessage.VALIDATING);
        this.descriptor().read(reader);
        this.validated = true;
      }
      catch (EOFException e) {
        throw new CSVException(CSVError.MISSING_HEADER);
      }
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
   */
  public void error(final String method, final String resourceKey) {
    if (this.logger != null) {
      this.logger.error(category, method, CSVBundle.string(resourceKey));
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
      this.logger.error(category, method, CSVBundle.format(resourceKey, arguments));
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
      this.logger.warn(category, method, CSVBundle.string(resourceKey));
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
      this.logger.warn(category, method, CSVBundle.format(resourceKey, arguments));
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
      this.logger.info(CSVBundle.string(resourceKey));
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
      this.logger.debug(this.category, method, CSVBundle.string(resourceKey));
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
      this.logger.debug(this.category, method, CSVBundle.format(resourceKey, arguments));
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