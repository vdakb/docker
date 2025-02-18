/*
    Oracle Consulting Services

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2011. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Utility Facilities

    File        :   SimpleDateTransformer.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Rashi.Rastogi@oracle.com

    Purpose     :   This file implements the class
                    SimpleDateTransformer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-04-26  RRastogi    First release version
*/

package oracle.iam.identity.utility;

import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.hst.foundation.logging.Logger;

import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.AbstractAttributeTransformer;

import oracle.iam.identity.utility.resource.TransformationBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class SimpleDateTransformer
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>SimpleDateTransformer</code> is the base class to transform a
 ** Data String to the internal Oracle Identity Manager Date Format
 ** '<code>yyyy/MM/dd hh:mm:ss z</code>' representation and vice versa.
 **
 ** @author  Rashi.Rastogi@oracle.com
 ** @version 1.0.0.0
 ** @since   3.0.0.0
 */
public abstract class SimpleDateTransformer extends AbstractAttributeTransformer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static final int       DEFAULT_YEAR_START       = 1900;
  protected static final int       DEFAULT_YEAR_END         = 9998;
  protected static final int       DEFAULT_MONTH_START      = 1;
  protected static final int       DEFAULT_MONTH_END        = 12;
  protected static final int       DEFAULT_DAY_START        = 1;
  protected static final int       DEFAULT_DAY_END          = 31;
  protected static final int       DEFAULT_HOUROFDAY_START  = 0;
  protected static final int       DEFAULT_HOUROFDAY_END    = 12;
  protected static final int       DEFAULT_MINUTE           = 0;
  protected static final int       DEFAULT_SECOND           = 0;

  protected static final String    DEFAULTDATE_PROPERTY     = "XL.DefaultDateFormat";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Calendar         calendar;
  protected final SimpleDateFormat targetFormatter;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>SimpleDateTransformer</code> which use the specified
   ** {@link Logger} for logging purpose.
   **
   ** @param  logger             the {@link Logger} for logging purpose.
   ** @param  targetFormat       the date format that will be used parse or
   **                            write the date value.
   */
  protected SimpleDateTransformer(final Logger logger, final String targetFormat) {
    // ensure inheritance
    super(logger);

    // create a formatter to convert server values to date or vice versa
    this.targetFormatter = new SimpleDateFormat(targetFormat);
    this.calendar        = Calendar.getInstance();
    this.calendar.setTime(new Date());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   targetFormatter
  /**
   ** Returns the date formatter for the target system.
   **
   ** @return                    the date formatter for the target system.
   */
  public final SimpleDateFormat targetFormatter() {
    return this.targetFormatter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultDate
  /**
   ** Returns the default date that will be set if to the transformation method
   ** of this {@link AbstractAttributeTransformer} an invalid value is passed.
   **
   ** @return                    the default date that will be set if to the
   **                            transformation method.
   */
  public final Date defaultDate() {
    return this.calendar.getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   yearAsString
  /**
   ** Returns the value of the embedded calendar {@link Calendar#YEAR} field as
   ** a string representation..
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#YEAR} field as a string
   **                            representation.
   */
  public final String yearAsString() {
    return String.valueOf(year());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   year
  /**
   ** Returns the value of the embedded calendar {@link Calendar#YEAR} field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#YEAR} field.
   */
  public final int year() {
    return this.calendar.get(Calendar.YEAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   month
  /**
   ** Returns the value of the embedded calendar {@link Calendar#MONTH} field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#MONTH} field.
   */
  public int month() {
    return this.calendar.get(Calendar.MONTH) + 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dayOfMonth
  /**
   ** Returns the value of the embedded calendar {@link Calendar#DAY_OF_MONTH}
   ** field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#DAY_OF_MONTH} field.
   */
  public int dayOfMonth() {
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dayOfWeek
  /**
   ** Returns the value of the embedded calendar {@link Calendar#DAY_OF_WEEK}
   ** field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#DAY_OF_WEEK} field.
   */
  public int dayOfWeek() {
    return calendar.get(Calendar.DAY_OF_WEEK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hour
  /**
   ** Returns the value of the embedded calendar {@link Calendar#HOUR} field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#HOUR} field.
   */
  public int hour() {
    return calendar.get(Calendar.HOUR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hourOfDay
  /**
   ** Returns the value of the embedded calendar {@link Calendar#HOUR_OF_DAY}
   ** field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#HOUR_OF_DAY} field.
   */
  public int hourOfDay() {
    return calendar.get(Calendar.HOUR_OF_DAY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minute
  /**
   ** Returns the value of the embedded calendar {@link Calendar#MINUTE} field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#MINUTE} field.
   */
  public int minute() {
    return calendar.get(Calendar.MINUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   second
  /**
   ** Returns the value of the embedded calendar {@link Calendar#SECOND} field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#SECOND} field.
   */
  public int second() {
    return calendar.get(Calendar.SECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   milliSecond
  /**
   ** Returns the value of the embedded calendar {@link Calendar#MILLISECOND}
   ** field.
   ** <p>
   ** In lenient mode, all calendar fields are normalized. In non-lenient mode,
   ** all calendar fields are validated and this method throws an exception if
   ** any calendar fields have out-of-range values. The normalization and
   ** validation are handled by the {@link Calendar#complete()} method, which
   ** process is calendar system dependent.
   **
   ** @return                    the value for the embedded calendar
   **                            {@link Calendar#MILLISECOND} field.
   */
  public int milliSecond() {
    return calendar.get(Calendar.MILLISECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseInternal
  /**
   ** Convert timestamp to a {@link Date} by parsing text from the beginning of
   ** the given string to produce a date.
   ** <p>
   ** Any {@link ParseException} is handled internally to avoid interruption of
   ** processes. If such an exception occurs an error message is written to the
   ** log and <code>null</code> is returned
   **
   ** @param  timestamp          the <code>String</code> whose beginning should
   **                            be parsed.
   **
   ** @return                    the {@link Date} parsed from the string.
   **                            May be <code>null</code>.
   */
  protected Date parseInternal(final String timestamp) {
    final String method = "parseInternal";
    if (StringUtility.isEmpty(timestamp))
      // may be we are making trouble on this
      return null;

    String internalPattern = null;
    try {
      // parse the string value to the date representation used internally
      // by Oracle Identity Manager by asking the system itself for the
      // configured format
      return DateUtility.parseDate(timestamp, propertyFacade().getPropertyValue(DEFAULTDATE_PROPERTY)).getTime();
    }
    catch (ParseException e) {
      warning(method, TransformationBundle.format(TransformationError.NOT_PARSEBLE, timestamp, (internalPattern == null ? "<unknown>" : internalPattern)));
      // may be we are making trouble on this
      return null;
    }
    catch (Exception e) {
      error(method, e.getLocalizedMessage());
      // may be we are making trouble on this
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseExternal
  /**
   ** Convert timestamp to a {@link Date} by parsing text from the beginning of
   ** the given string to produce a date.
   ** <p>
   ** Any {@link ParseException} is handled internally to avoid interruption of
   ** processes. If such an exception occurs an error message is written to the
   ** log and <code>null</code> is returned
   **
   ** @param  timestamp          the <code>String</code> whose beginning should
   **                            be parsed.
   **
   ** @return                    the {@link Date} parsed from the string.
   **                            May be <code>null</code>.
   */
  protected Date parseExternal(final String timestamp) {
    final String method = "parseExternal";
    if (StringUtility.isEmpty(timestamp))
      // may be we are making trouble on this
      return null;

    try {
      // parse the date from the string that must match the format passed to
      // the constructor of this instance
      return this.targetFormatter.parse(timestamp);
    }
    catch (ParseException e) {
      warning(method, TransformationBundle.format(TransformationError.NOT_PARSEBLE, timestamp, this.targetFormatter.toPattern()));
      // may be we are making trouble on this
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatExternal
  /**
   ** Convert the specified <code>timestamp</code> to the string representation
   ** used by the external system.
   **
   ** @param  timestamp          the timestamp to be converted to the string
   **                            representation required for the external
   **                            system.
   **
   ** @return                    the string representation of the timestamp in
   **                            the external format.
   */
  protected String formatExternal(final Date timestamp) {
    // convert the date value to the string representation used externally
      return (timestamp == null) ? null : this.targetFormatter.format(timestamp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatInternal
  /**
   ** Convert the specified <code>timestamp</code> to the string representation
   ** used internally by Oracle Identity Manager.
   **
   ** @param  timestamp          the timestamp to be converted to the string
   **                            representation used internally by Oracle
   **                            Identity Manager.
   **
   ** @return                    the string representation of the timestamp in
   **                            the internal format.
   */
  protected String formatInternal(final Date timestamp) {
    final String method = "formatInternal";
    if (timestamp == null)
      // may be we are making trouble on this
      return null;

    try {
      // convert the date value to the string representation used internally
      // by Oracle Identity Manager by asking the system itself for the configured
      // format
      return DateUtility.display(timestamp, propertyFacade().getPropertyValue(DEFAULTDATE_PROPERTY));
    }
    catch (Exception e) {
      error(method, e.getLocalizedMessage());
      // may be we are making trouble on this
      return null;
    }
  }
}