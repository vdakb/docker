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
    Subsystem   :   Common Shared Utility Facility

    File        :   GregorianDate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GregorianDate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;

////////////////////////////////////////////////////////////////////////////////
// class GregorianDate
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A date-only Gregorian calendar.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GregorianDate extends GregorianCalendar {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Default time zone: Greenwich meantime. */
  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default Constructor.
   */
  public GregorianDate() {
    super(GMT);

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given {@link Date}.
   **
   ** @param  date               the date for the new calendar
   */
  public GregorianDate(final Date date) {
    super(GMT);
    if (date != null)
      setTime(date);

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given {@link Calendar}.
   **
   ** @param  calendar           the original calendar for the new calendar
   */
  public GregorianDate(final Calendar calendar) {
    super(GMT);
    if (calendar != null)
      setTime(calendar.getTime());

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from given year, month and date.
   ** <p>
   ** The first month of the year is <code>JANUARY</code> which is 0; the last
   ** month is <code>DEDCEMBER</code> which is 11.
   **
   ** @param  year               the year of the new calendar
   ** @param  month              the month of the new calendar
   ** @param  day                the day of the new calendar
   */
  public GregorianDate(final int year, final int month, final int day) {
    super(GMT);
    set(Calendar.YEAR, year);
    set(Calendar.MONTH, month);
    set(Calendar.DATE, day);
    computeTime();
    computeFields();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getYear
  /**
   ** Returns the year of the calendar.
   **
   ** @return                    the year
   */
  public int getYear() {
    return get(Calendar.YEAR);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMonth
  /**
   ** Returns the month of the calendar.
   **
   ** @return                    the month
   */
  public int getMonth() {
    return get(Calendar.MONTH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDay
  /**
   ** Returns the day of the calendar.
   **
   ** @return                    the day
   */
  public int getDay() {
    return get(Calendar.DATE);
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
  @Override
  public String toString() {
    return DateUtility.formatDate(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set (overridden)
  /**
   ** Overwrite prevents setting time fields.
   **
   ** @param field               the given calendar field.
   ** @param value               the value to be set for the given calendar
   **                            field.
   **
   ** @see Calendar#set(int, int)
   */
  @Override
  public void set(final int field, final int value) {
    if (isTimeField(field))
      return;

    super.set(field, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone (overridden)
  /**
   ** Overwrite prevents setting a time zone to keep the time unchanged and
   ** unavailable.
   **
   ** @param  zone               the new time zone (ignored)
   **
   ** @see    Calendar#setTimeZone(TimeZone)
   */
  @Override
  public void setTimeZone(final TimeZone zone) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeInMillis (overridden)
  /**
   ** Overwrites {@link Calendar#setTimeInMillis(long)}, to disable all time
   ** fields.
   **
   ** @param  millis             the new time in UTC milliseconds from the
   **                            epoch.
   **
   ** @see    Calendar#setTimeInMillis(long)
   */
  @Override
  public void setTimeInMillis(final long millis) {
    super.setTimeInMillis(millis);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Overwrite to prevent setting time fields.
   **
   ** @param  field              the time field.
   ** @param  amount             the amount of date or time to be added to the
   **                            field.
   **
   ** @see    GregorianCalendar#add(int, int)
   */
  @Override
  public void add(final int field, final int amount) {
    if (isTimeField(field)) {
      return;
    }
    super.add(field, amount);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeFields
  /**
   ** Overwrites GregorianCalendar.computeFields(), to disable all time fields.
   **
   ** @see GregorianCalendar#computeFields()
   */
  protected void computeFields() {
    super.computeFields();
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
    clear(Calendar.HOUR_OF_DAY);
    clear(Calendar.HOUR);
    clear(Calendar.MINUTE);
    clear(Calendar.SECOND);
    clear(Calendar.AM_PM);
    clear(Calendar.MILLISECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isTimeField
  /**
   ** Checks if a given field number indicating a time field.
   **
   ** @param  field              field index
   **
   ** @return                    <code>true</code> if the field number
   **                            indicating a time field, else
   **                            <code>false</code>
   */
  private boolean isTimeField(final int field) {
    if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
      return true;
    }
    if (field == Calendar.MINUTE || field == Calendar.SECOND) {
      return true;
    }
    if (field == Calendar.MILLISECOND) {
      return true;
    }
    return false;
  }
}
