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

    File        :   SimpleFormatter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SimpleFormatter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-11-12  DSteding    First release version
*/

package oracle.hst.foundation.logging;

import java.util.logging.LogRecord;
import java.util.logging.Formatter;

import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class SimpleFormatter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>SimpleFormatter</code> formats the {@link LogRecord} as follows:
 ** <pre>
 ** [date|logger|level] localized message with parameters
 ** </pre>
 */
public class SimpleFormatter extends AbstractFormatter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static String FIELD_PREFIX    = "[";
  private static String FIELD_SUFFIX    = "] ";
  private static String FIELD_SEPARATOR = "|";

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format (AbstractFormatter)
  /**
   ** Format the given log record and return the formatted string.
   ** <p>
   ** The resulting formatted String will normally include a localized and
   ** formated version of the {@link LogRecord}'s message field.
   ** <p>
   ** The {@link Formatter#formatMessage(LogRecord)} method is used to localize
   ** and format the message field.
   **
   ** @param  record             the log record to be formatted.
   **
   ** @return                    the formatted log record
   */
  public synchronized String format(final LogRecord record) {
    // Create a StringBuilder to contain the formatted record start with the
    // date.
    StringBuilder buffer = new StringBuilder();

    buffer.append(FIELD_PREFIX);
    // Get the date from the LogRecord and add it formatted to the buffer
    printDate(buffer, record);
    buffer.append(FIELD_SEPARATOR);
    // Get the logger name and add it to the buffer
    printLogger(buffer, record);
    buffer.append(FIELD_SUFFIX);
    // Get the level name and add it to the buffer
    printLevel(buffer, record);
    buffer.append(FIELD_SEPARATOR);
    // Get the formatted message (includes localization  and substitution of
    // paramters) and add it to the buffer
    printMessage(buffer, record);

    buffer.append(SystemConstant.LINEFEED);
    return buffer.toString();
  }
}