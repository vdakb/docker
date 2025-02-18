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

    System      :   Foundation Shared Library
    Subsystem   :   Common shared logging facilities

    File        :   AbstractFormatter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractFormatter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.logging;

import java.util.Calendar;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

import java.text.SimpleDateFormat;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFormatter
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractFormatter</code> provides the basic formatting utilities to
 ** formats a {@link LogRecord}.
 */
public abstract class AbstractFormatter extends Formatter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public enum Level { FATAL, ERROR, WARNING, INFO, DEBUG, TRACE };

  private static int              FIELD_WIDTH_LEVEL  = 8;
  private static int              FIELD_WIDTH_LOGGER = 30;
  private static Calendar         CALENDAR           = Calendar.getInstance();
  private static SimpleDateFormat FORMAT             = new SimpleDateFormat("dd-MM-yyyy H:mm:ss:S");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printDate
  /**
   ** Print the event time from the given {@link LogRecord} by formatting it to
   ** {@link #FORMAT} and appends the result to the given {@link StringBuilder}.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the event time to be formatted.
   */
  protected final void printDate(final StringBuilder buffer, final LogRecord record) {
    CALENDAR.setTimeInMillis(record.getMillis());
    buffer.append(FORMAT.format(CALENDAR.getTime()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printLevel
  /**
   ** Print the logging level from the given {@link LogRecord} left aligned and
   ** appends the result to the given {@link StringBuilder}.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the logging level to be
   **                            formatted.
   */
  protected final void printLevel(final StringBuilder buffer, final LogRecord record) {
    printLevel(buffer, record, StringUtility.Align.LEFT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printLevel
  /**
   ** Print the logging level from the given {@link LogRecord} left or right
   ** aligned and appends the result to the given {@link StringBuilder}.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the logging level to be
   **                            formatted.
   ** @param  align              the advice how the logging level is aligned
   **                            in the output.
   */
  protected final void printLevel(final StringBuilder buffer, final LogRecord record, final StringUtility.Align align) {
    // Get the level name and add it to the buffer
    Level level;
    switch (record.getLevel().intValue()) {
      case  300 :
      case  400 : level = Level.TRACE;
                  break;
      case  500 : level = Level.DEBUG;
                  break;
      case  700 :
      case  800 : level = Level.INFO;
                  break;
      case  900 : level = Level.WARNING;
                  break;
      case 1000 : level = Level.ERROR;
                  break;
      default   : level = Level.FATAL;
                  break;
    }
    StringUtility.alignText(buffer, level.toString(), FIELD_WIDTH_LEVEL, align);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printLogger
  /**
   ** Print the logger name from the given {@link LogRecord} left aligned and
   ** appends the result to the given {@link StringBuilder}.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the logging level to be
   **                            formatted.
   */
  protected final void printLogger(final StringBuilder buffer, final LogRecord record) {
    printLogger(buffer, record, StringUtility.Align.LEFT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printLogger
  /**
   ** Print the logger name from the given {@link LogRecord} left or right
   ** aligned and appends the result to the given {@link StringBuilder}.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the logging level to be
   **                            formatted.
   ** @param  align              the advice how the logger name is aligned in
   **                            the output.
   */
  protected final void printLogger(final StringBuilder buffer, final LogRecord record, final StringUtility.Align align) {
    // Get the level name and add it to the buffer
    StringUtility.alignText(buffer, record.getLoggerName(), FIELD_WIDTH_LOGGER, align);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printMessage
  /**
   ** Print the message text from the given {@link LogRecord} by invoking
   ** {@link Formatter#formatMessage(LogRecord)} method to localize and format
   ** the message field.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  record             the log record the logging level to be
   **                            formatted.
   */
  protected final void printMessage(final StringBuilder buffer, final LogRecord record) {
     buffer.append(formatMessage(record));
  }
}