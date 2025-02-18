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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   DateAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DateAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.utility.DateUtility;

import oracle.iam.identity.foundation.GenericAdapter;

////////////////////////////////////////////////////////////////////////////////
// class DateAdapter
// ~~~~~ ~~~~~~~~~~~
/**
 ** Offers utility methods to handle date objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class DateAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DateAdapter</code> task adpater that allows
   ** use as  a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public DateAdapter(final tcDataProvider provider) {
    // ensure inheritance
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DateAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public DateAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evalutaDate
  /**
   ** Returns the future date.
   **
   ** @param  daysAhead          the number of days, which are to be added to
   **                            the current date when the event arose.
   **
   ** @return                    the evaluated date
   */
  public static Date evalutaDate(int daysAhead) {
    // create a calendar object with the current local time
    Calendar calendar = Calendar.getInstance();
    // reset the time ties
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    // add the amount of days
    calendar.add(Calendar.DATE, daysAhead);

    return calendar.getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evalutaDate
  /**
   ** Returns the future date.
   **
   ** @param  currentDate        the date the calculation is based on.
   ** @param  daysAhead          the number of days, which are to be added to
   **                            the current date when the event arose.
   **
   ** @return                    the evaluated date
   */
  public static Date evalutaDate(final String currentDate, int daysAhead) {
    return evalutaDate(toDate(currentDate, "yyyy-MM-dd hh:mm:ss.SSS"), daysAhead);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   evalutaDate
  /**
   ** Returns the future date.
   **
   ** @param  currentDate        the date the calculation is based on.
   ** @param  daysAhead          the number of days, which are to be added to
   **                            the current date when the event arose.
   **
   ** @return                    the evaluated date
   */
  public static Date evalutaDate(final Date currentDate, int daysAhead) {
    // create a calendar object with the current local time
    Calendar calendar = Calendar.getInstance();
    //
    // set the time of the calendar object to the passed date
    calendar.setTime(currentDate);
    // add the amount of days
    calendar.add(Calendar.DATE, daysAhead);

    return calendar.getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareDate
  /**
   ** Returns the future date.
   **
   ** @param  date1              the to compare
   ** @param  date2              the to compare
   **
   ** @return                    the value <code>0</code> if the argument
  **                             <code>date1</code> is equal to argument
  **                             <code>date2</code>; a value less than
  **                             <code>0</code> if <code>date1</code> is before
  **                             argument <code>date2</code>; and a value
  **                             greater than <code>0</code> if
  **                             <code>date1</code> is after argument
  **                             <code>date2</code>.
   **
   ** @see java.util.Date
   */
  public static int compareDate(Date date1, Date date2) {
    return DateUtility.compare(date1, date2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toDate
  /**
   ** Parses text from a string to produce a {@link Date}.
   **
   ** @param  dateValue          a <code>String</code>, part of which should be
   **                            parsed.
   ** @param  pattern            the pattern describing the date and time format
   **
   ** @return                    a {@link Date} parsed from the string.
   **                            In case of error, returns the current date.
   **
   ** @see java.util.Date
   */
  public static Date toDate(final String dateValue, final String pattern) {
    try {
      return DateUtility.parseDate(dateValue, pattern).getTime();
    }
    catch (ParseException e) {
      return new Date();
    }
  }
}