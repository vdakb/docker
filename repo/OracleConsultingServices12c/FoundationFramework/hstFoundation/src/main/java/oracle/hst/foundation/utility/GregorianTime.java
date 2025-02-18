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

    File        :   GregorianTime.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GregorianTime.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

////////////////////////////////////////////////////////////////////////////////
// class GregorianTime
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** A time-only Gregorian calendar.
 ** Milliseconds are always zero,
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GregorianTime extends GregorianCalendar {

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
  public GregorianTime() {
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
  public GregorianTime(final Date date) {
    super(GMT);
    if (date != null)
      setTime(date);

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given {@link Calendar}.
   *
   * @param calendar the original calendar for the new calendar
   */
  public GregorianTime(final Calendar calendar) {
    super(GMT);
    if (calendar != null)
      setTime(calendar.getTime());

    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from given year, month and date.
   **
   ** @param  hour               the hour of the new calendar
   ** @param  minute             the minute of the new calendar
   ** @param  second             the second of the new calendar
   */
  public GregorianTime(final int hour, final int minute, final int second) {
    super(GMT);
    set(Calendar.HOUR_OF_DAY, hour);
    set(Calendar.MINUTE, minute);
    set(Calendar.SECOND, second);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getHour
  /**
   ** Returns the hour of the calendar.
   **
   ** @return                    the hour
   */
  public int getHour() {
    return get(Calendar.HOUR_OF_DAY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMinute
  /**
   ** Returns the minute of the calendar.
   **
   ** @return                    the minute
   */
  public int getMinute() {
    return get(Calendar.MINUTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getSecond
  /**
   ** Returns the second of the calendar.
   **
   ** @return                    the second
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
    return DateUtility.formatTime(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set (overridden)
  /**
   ** Overwrite prevents setting date fields.
   **
   ** @param field               the given calendar field.
   ** @param value               the value to be set for the given calendar
   **                            field.
   **
   ** @see Calendar#set(int, int)
   */
  public void set(final int field, final int value) {
    if (isDateField(field))
      return;

    super.set(field, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Overwrite to prevent setting date fields.
   **
   ** @param  field              the time field.
   ** @param  amount             the amount of date or time to be added to the
   **                            field.
   **
   ** @see    GregorianCalendar#add(int, int)
   */
  public void add(final int field, final int amount) {
    if (isDateField(field))
      return;

    super.add(field, amount);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeZone (overridden)
  /**
   ** Overwrite prevents setting a time zone to keep the date unchanged and
   ** unavailable.
   **
   ** @param  zone               the new time zone (ignored)
   **
   ** @see    Calendar#setTimeZone(TimeZone)
   */
  public void setTimeZone(final TimeZone zone) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTimeInMillis (overridden)
  /**
   ** Overwrites {@link Calendar#setTimeInMillis(long)}, to disable all date
   ** fields.
   **
   ** @param  millis             the new time in UTC milliseconds from the
   **                            epoch.
   **
   ** @see    Calendar#setTimeInMillis(long)
   */
  public void setTimeInMillis(final long millis) {
    super.setTimeInMillis(millis);
    computeTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeFields
  /**
   ** Overwrites GregorianCalendar.computeFields(), to disable all date fields.
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
   ** Overwrites GregorianCalendar.computeTime(), to disable all date fields.
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
    clear(Calendar.ERA);
    clear(Calendar.YEAR);
    clear(Calendar.MONTH);
    clear(Calendar.WEEK_OF_YEAR);
    clear(Calendar.WEEK_OF_MONTH);
    clear(Calendar.DAY_OF_MONTH);
    clear(Calendar.DAY_OF_YEAR);
    clear(Calendar.DAY_OF_WEEK);
    clear(Calendar.DAY_OF_WEEK_IN_MONTH);
    clear(Calendar.MILLISECOND);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   isDateField
  /**
   ** Checks if a given field number indicating a time field.
   **
   ** @param  field              field index
   **
   ** @return                    <code>true</code> if the field number
   **                            indicating a time field, else
   **                            <code>false</code>
   */
  private boolean isDateField(final int field) {
    if (field == Calendar.ERA || field == Calendar.YEAR) {
      return true;
    }
    if (field == Calendar.MONTH || field == Calendar.DATE) {
      return true;
    }
    if (field == Calendar.WEEK_OF_MONTH || field == Calendar.WEEK_OF_YEAR) {
      return true;
    }
    if (field == Calendar.MILLISECOND) {
      return true;
    }
    return false;
  }
}
