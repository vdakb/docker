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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   GregorianDateTime.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GregorianDateTime.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;

////////////////////////////////////////////////////////////////////////////////
// class GregorianDateTime
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A Gregorian calendar with date and time, but without milliseconds.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GregorianDateTime extends GregorianCalendar {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7464461394534109421")
  private static final long serialVersionUID = -5580219923929037089L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor.
   */
  public GregorianDateTime() {
    // ensure inheritance
    this(TimeZone.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Createss a <code>GregorianDateTime</code> based on the current time in the
   ** given time zone with the default locale.
   **
   ** @param  zone               the given time zone.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   */
  public GregorianDateTime(final TimeZone zone) {
    // ensure inheritance
    super(zone);

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given <code>Date</code>.
   **
   ** @param  date               the date for the new calendar
   **                            <br>
   **                            Allowed object is {@link Date}.
   */
  public GregorianDateTime(final Date date) {
    // ensure inheritance
    this(date, TimeZone.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given <code>Date</code>.
   **
   ** @param  date               the date for the new calendar
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  zone               the given time zone.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   */
  public GregorianDateTime(final Date date, final TimeZone zone) {
    // ensure inheritance
    super(zone);

    if (date != null) {
      setTime(date);
    }
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given <code>Calendar</code>.
   **
   ** @param  calendar           the original calendar for the new calendar
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   */
  public GregorianDateTime(final Calendar calendar) {
    // ensure inheritance
    super();

    if (calendar != null) {
      setTimeZone(calendar.getTimeZone());
      setTime(calendar.getTime());
      set(HOUR_OF_DAY, calendar.get(HOUR_OF_DAY));
      set(MINUTE, calendar.get(MINUTE));
    }
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from given year, month and date.
   ** <p>
   ** The first month of the year is <code>JANUARY</code> which is 0; the
   ** last month is <code>DEDCEMBER</code> which is 11.
   **
   ** @param  year               the year of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  month              the month of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  day                the day of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public GregorianDateTime(final int year, final int month, final int day) {
    // ensure inheritance
    super(year, month, day);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from given year, month and date.
   ** <p>
   ** The first month of the year is <code>JANUARY</code> which is 0; the
   ** last month is <code>DEDCEMBER</code> which is 11.
   **
   ** @param  year               the year of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  month              the month of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  day                the day of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  hour               the hour of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minute             the minute of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  second             the second of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public GregorianDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second) {
    // ensure inheritance
    this(year, month, day, hour, minute, second, TimeZone.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from given year, month and date.
   ** <p>
   ** The first month of the year is <code>JANUARY</code> which is 0; the
   ** last month is <code>DEDCEMBER</code> which is 11.
   **
   ** @param  year               the year of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  month              the month of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  day                the day of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  hour               the hour of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  minute             the minute of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  second             the second of the new calendar
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  zone               the given time zone.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   */
  public GregorianDateTime(final int year, final int month, final int day, final int hour, final int minute, final int second, final TimeZone zone) {
    // ensure inheritance
    super(zone);

    set(Calendar.YEAR, year);
    set(Calendar.MONTH, month);
    set(Calendar.DATE, day);
    set(Calendar.HOUR_OF_DAY, hour);
    set(Calendar.MINUTE, minute);
    set(Calendar.SECOND, second);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getYear
  /**
   ** Returns the year of the calendar.
   **
   ** @return                    the year.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getYear() {
    return get(Calendar.YEAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMonth
  /**
   ** Returns the month of the calendar.
   **
   ** @return                    the month.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getMonth() {
    return get(Calendar.MONTH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDay
  /**
   ** Returns the day of the calendar.
   **
   ** @return                    the day.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getDay() {
    return get(Calendar.DATE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHour
  /**
   ** Returns the hour of the calendar.
   **
   ** @return                    the hour.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getHour() {
    return get(Calendar.HOUR_OF_DAY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMinute
  /**
   ** Returns the minute of the calendar.
   **
   ** @return                    the minute.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getMinute() {
    return get(Calendar.MINUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecond
  /**
   ** Returns the second of the calendar.
   **
   ** @return                    the second.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int getSecond() {
    return get(Calendar.SECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** {@inheritDoc}
   **
   ** @see Calendar#toString()
   */
  public String toString() {
    return DateUtility.format(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set (overridden)
  /**
   ** Overwrite prevents setting time fields.
   **
   ** @param field               the given calendar field.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param value               the value to be set for the given calendar
   **                            field.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @see Calendar#set(int, int)
   */
  public void set(final int field, final int value) {

    if (field == Calendar.MILLISECOND) {
      return;
    }
    super.set(field, value);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone (overridden)
  /**
   ** Overwrite prevents setting a time zone to keep the time unchanged and
   ** unavailable.
   **
   ** @param  zone               the new time zone (ignored)
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @see    Calendar#setTimeZone(TimeZone)
   */
  public void setTimeZone(final TimeZone zone) {
    super.setTimeZone(zone);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeTime (overridden)
  /**
   ** Overwrites GregorianCalendar.computeTime(), to disable all time fields.
   **
   ** @see    GregorianCalendar#computeTime()
   */
  protected void computeTime() {
    clearFields();
    super.computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clearFields
  /**
   ** Clears all time fields.
   */
  private void clearFields() {
    clear(Calendar.MILLISECOND);
  }
}