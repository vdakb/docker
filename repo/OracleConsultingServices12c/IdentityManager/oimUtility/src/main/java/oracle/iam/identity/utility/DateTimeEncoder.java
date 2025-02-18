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

    Copyright © 2006. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   DateTimeEncoder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DateTimeEncoder.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2006-04-08  DSteding    First release version
*/

package oracle.iam.identity.utility;

import java.util.Map;
import java.util.Date;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class DateTimeEncoder
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** The <code>DateTimeEncoder</code> transform an internal Oracle Identity
 ** Manager Date Format '<code>yyyy/MM/dd hh:mm:ss z</code>' representation to
 ** an ISO Date and Time '<code>'yyyy-MM-dd hh:mm:ss'</code>' Date String
 ** representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DateTimeEncoder extends StringTimestampEncoder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   */
  public DateTimeEncoder(final Logger logger) {
    // ensure inheritance
    this(logger, DateUtility.ISO8601);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for debugging purpose.
   **
   ** @param  logger             the {@link Logger} for debugging purpose
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   */
  public DateTimeEncoder(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger, targetFormat);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  defaultDate        the default date that will be set if to the
   **                            transformation method.
   */
  protected DateTimeEncoder(final Logger logger, final Date defaultDate) {
    // ensure inheritance
    this(logger, DateUtility.ISO8601, defaultDate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   ** @param  defaultDate        the default date that will be set if to the
   **                            transformation method.
   */
  protected DateTimeEncoder(final Logger logger, final String targetFormat, final Date defaultDate) {
    // ensure inheritance
    super(logger, targetFormat, defaultDate);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  year               the value used to set the YEAR calendar field.
   ** @param  month              the value used to set the MONTH calendar field.
   **                            Month value is 0-based. e.g., 0 for January.
   ** @param  date               the value used to set the DAY_OF_MONTH calendar
   **                            field.
   ** @param  hourOfDay          the value used to set the HOUR_OF_DAY calendar
   **                            field
   ** @param  minute             the value used to set the MINUTE calendar field.
   ** @param  second             the value used to set the SECOND calendar field.
   */
  protected DateTimeEncoder(final Logger logger, final int year, int month, int date, int hourOfDay, int minute, int second) {
    // ensure inheritance
    this(logger, DateUtility.ISO8601, year, month, date, hourOfDay, minute, second);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DateTimeEncoder</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used to parse or
   **                            write the date value.
   ** @param  year               the value used to set the YEAR calendar field.
   ** @param  month              the value used to set the MONTH calendar field.
   **                            Month value is 0-based. e.g., 0 for January.
   ** @param  date               the value used to set the DAY_OF_MONTH calendar
   **                            field.
   ** @param  hourOfDay          the value used to set the HOUR_OF_DAY calendar
   **                            field
   ** @param  minute             the value used to set the MINUTE calendar field.
   ** @param  second             the value used to set the SECOND calendar field.
   */
  protected DateTimeEncoder(final Logger logger, final String targetFormat, final int year, int month, int date, int hourOfDay, int minute, int second) {
    // ensure inheritance
    super(logger, targetFormat, year, month, date, hourOfDay, minute, second);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AttributeTransformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   ** <p>
   ** ISO Timestamp is in the form of "yyyy-MM-dd hh:mm:ss zz". The date and
   ** time specified in ISO Timestamp is based on the localized time. The date
   ** and time "Mon Jul 30 17:42:00 2001" is represented in ISO Timestamp as
   ** "2001-07-30 17:42:00 CET".
   ** <p>
   ** The {@link Map} <code>origin</code> contains all untouched values. The
   ** {@link Map} <code>subject</code> contains all transformed values
   **
   ** @param  attributeName      the specific attribute in the {@link Map}
   **                            <code>origin</code> that has to be transformed.
   ** @param  origin             the {@link Map} to transform.
   ** @param  subject            the transformation of the specified
   **                            {@link Map} <code>origin</code>.
   */
  public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
    final String method = "transform";

    Object value = origin.get(attributeName);
    // if we not got a null value put it without transformation in the returning
    // container
    Date timestamp = null;
    if (value == null)
      timestamp = this.defaultDate();
    else if (value.toString().length() == 0) {
      timestamp = this.defaultDate();
    }
    else {
      try {
        timestamp = parseInternal(value.toString());
      }
      catch (Exception e) {
        error(method, e.getLocalizedMessage());
      }
    }
    subject.put(attributeName, formatExternal(timestamp == null ? this.defaultDate() : timestamp));
  }
}