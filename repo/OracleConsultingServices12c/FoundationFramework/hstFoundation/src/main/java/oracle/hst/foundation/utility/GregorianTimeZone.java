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

    File        :   GregorianTimeZone.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GregorianTimeZone.


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
// class GregorianTimeZone
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A time-only Gregorian calendar.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class GregorianTimeZone extends GregorianCalendar {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int MILLIS_PER_HOUR = 3600000;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default Constructor.
   */
   public GregorianTimeZone() {
    super();
    computeFields();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given {@link Date}.
   **
   ** @param  date               the date for the new calendar
   */
  public GregorianTimeZone(final Date date) {
    super();
    if (date != null)
      setTime(date);

    computeFields();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian date from a given {@link Calendar}.
   **
   ** @param  calendar           the original calendar for the new calendar
   */
  public GregorianTimeZone(final Calendar calendar) {
    super();
    if (calendar != null) {
      set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
      set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
      set(Calendar.SECOND, calendar.get(Calendar.SECOND));
      set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
      computeFields();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new Gregorian time zone from a given time zone.
   **
   ** @param  timezone           the time zone
   */
  public GregorianTimeZone(final TimeZone timezone) {
    super();
    setTimeZone(timezone);
    computeFields();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Formats a date as a string in a locale-independent format.
   **
   ** @return                    string representation of date in reverse format
   **                             with leading zeros:
   **                             <code>YYYY-MM-DD</code>.
   **
   ** @see Object#toString()
   */
  public String toString() {
    // FIXME getDSTSavings() should be compatible with JDK 1.3
    return getTimeZone().getID() + "/" + (getTimeZone().getDSTSavings() / MILLIS_PER_HOUR) + "h";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one.
   **
   ** @param  object             the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **
   ** @see    Object#equals(Object)
   */
  public boolean equals(final Object object) {
    if (this == object)
      return true;

    if (!(object instanceof java.util.Calendar))
      return false;
    else {
      TimeZone tz1 = ((java.util.Calendar)object).getTimeZone();
      TimeZone tz2 = getTimeZone();
      // todo write unit tests for the equals method
      return tz1.getID().equals(tz2.getID());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   **
   ** @return a hash code value for this object.
   **
   ** @see Object#hashCode()
   */
  public int hashCode() {
    return get(Calendar.HOUR) << (2 * 2 * 3) | get(Calendar.MINUTE) << (2 * 3) | get(Calendar.SECOND);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set (overridden)
  /**
   ** Overwrite prevents setting date and time fields.
   **
   ** @param field               the given calendar field.
   ** @param value               the value to be set for the given calendar
   **                            field.
   **
   ** @see Calendar#set(int, int)
   */
  public void set(final int field, final int value) {
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeFields
  /**
   ** Overwrites GregorianCalendar.computeFields(), to disable all date and
   ** time fields.
   **
   ** @see GregorianCalendar#computeFields()
   */
  protected void computeFields() {
    super.computeFields();
    clear(Calendar.ERA);
    clear(Calendar.YEAR);
    clear(Calendar.MONTH);
    clear(Calendar.WEEK_OF_YEAR);
    clear(Calendar.WEEK_OF_MONTH);
    clear(Calendar.DAY_OF_MONTH);
    clear(Calendar.DAY_OF_YEAR);
    clear(Calendar.DAY_OF_WEEK);
    clear(Calendar.DAY_OF_WEEK_IN_MONTH);
    clear(Calendar.HOUR_OF_DAY);
    clear(Calendar.HOUR);
    clear(Calendar.MINUTE);
    clear(Calendar.SECOND);
    clear(Calendar.AM_PM);
    clear(Calendar.MILLISECOND);
  }
}
