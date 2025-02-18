/*
                         Copyright © 2023 Red.Security

    Licensed under the MIT License (the "License"); you may not use this file
    except in compliance with the License. You may obtain a copy of the License
    at

                      https://opensource.org/licenses/MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

    ----------------------------------------------------------------------------

    System      :   Platform Service Extension
    Subsystem   :   Common Shared Utility Library

    File        :   DateUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    DateUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;

import redsecurity.platform.core.SystemBundle;
import redsecurity.platform.core.SystemValidator;

////////////////////////////////////////////////////////////////////////////////
// abstract class DateUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** Miscellaneous date and time utility methods. Mainly for internal use.
 ** <p>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DateUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Date format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       ISO8601_DATE      = "yyyy-MM-dd";
  /**
   ** Time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       ISO8601_TIME      = "HH:mm:ss";
  /**
   ** A value of the profile syntax is a character string representing a date
   ** and time.
   ** <br>
   ** The truncated representations of years with only two digits are not
   ** allowed -- RFC 3339 requires 4-digit years, and the RFC only allows a
   ** period character to be used as the decimal point for fractional seconds.
   */
  public static final String       ISO8601           = ISO8601_DATE + " " + ISO8601_TIME;
  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class {@link SimpleDateFormat}.
   */
  public static final String       ISO8601_ZULU      = ISO8601 + "'Z'";
  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       ISO8601_ZULU_NANO = ISO8601 + ".SSS'Z'";
  /**
   ** A value of the profile syntax is a character string representing a date
   ** and time.
   ** <br>
   ** The Internet-specific encoding of a value of this syntax is a restriction
   ** of the format defined in [ISO8601].
   ** <br>
   ** The truncated representations of years with only two digits are not
   ** allowed -- RFC 3339 requires 4-digit years, and the RFC only allows a
   ** period character to be used as the decimal point for fractional seconds.
   */
  public static final String       RFC3339           = ISO8601_DATE + "'T'" + ISO8601_TIME;
  /**
   ** A value of the profile syntax is a character string representing a date
   ** and time.
   ** <br>
   ** The Internet-specific encoding of a value of this syntax is a restriction
   ** of the format defined in [ISO8601].
   ** <br>
   ** The truncated representations of years with only two digits are not
   ** allowed -- RFC 3339 requires 4-digit years, and the RFC only allows a
   ** period character to be used as the decimal point for fractional seconds.
   */
  public static final String       RFC3339_ZULU      = RFC3339 + "'Z'";
  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       RFC3339_ZULU_NANO = RFC3339 + ".SSS'Z'";
  /**
   ** Date format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       RFC4517_DATE      = "yyyyMMdd";
  /**
   ** Time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       RFC4517_TIME      = "HHmmss";
  /**
   ** A value of the Generalized Time syntax is a character string representing
   ** a date and time.
   ** <br>
   ** The LDAP-specific encoding of a value of this syntax is a restriction
   ** of the format defined in [ISO8601].
   */
  public static final String       RFC4517           = RFC4517_DATE + RFC4517_TIME;
  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       RFC4517_ZULU      = RFC4517 + "'Z'";

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       RFC4517_ZULU_NANO = RFC4517 + ".SSS'Z'";
  /**
   ** The {@link TimeZone} object that represents the UTC (universal coordinated
   ** time) time zone.
   */
  public static final TimeZone     UTC               = TimeZone.getTimeZone("UTC");
  /**
   ** The thread-local date formatter used to encode ISO time values.
   */
  private static final ThreadLocal<SimpleDateFormat> ISO8601_FORMATTER = new ThreadLocal<>();
  /**
   ** The thread-local date formatter used to encode generalized time values.
   */
  private static final ThreadLocal<SimpleDateFormat> RFC4517_FORMATTER = new ThreadLocal<>();
  /**
   ** The thread-local date formatter used to encode RFC 3339 time values.
   */
  private static final ThreadLocal<SimpleDateFormat> RFC3339_FORMATTER = new ThreadLocal<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DateUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new DateUtility()" and enforces use of the public method below.
   */
  private DateUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two {@link Date}s if they are identically.
   **
   ** @param  lhs                the left-hand-side {@link Date} to be tested
   **                            for equality.
   ** @param  rhs                the right-hand-side {@link Date} to be tested
   **                            for equality.
   **
   ** @return                    <code>true</code> if the {@link Date}s are
   **                            identically; otherwise <code>false</code>.
   */
  public static boolean equal(final Date lhs, final Date rhs) {
    if (lhs == rhs)
      return true;
    else if (lhs == null && rhs == null)
      return true;
    else if ((lhs == null && rhs != null) || (lhs != null && rhs == null))
      return false;
    else
      return lhs.equals(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two date values.
   **
   ** @param  lhs                the left-hand-side {@link Date}  to compare.
   ** @param  rhs                the right-hand-side {@link Date} to compare
   **
   ** @return                    the value <code>0</code> if the argument
   **                            <code>lhs</code> is equal to argument
   **                            <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is before
   **                            argument <code>rhs</code>; and a value
   **                            greater than <code>0</code> if
   **                            <code>lhs</code> is after argument
   **                            <code>rhs</code>.
   **
   ** @see   java.util.Date
   */
  public static int compare(final Date lhs, final Date rhs) {
    int  result    = 1;
    long lhsTime = 0L;
    if (lhs != null) {
      lhsTime = lhs.getTime();
      if (lhsTime != 0L)
        result = lhs.compareTo(rhs);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Parses a date given as a string.
   ** <p>
   ** The format of the resulting string is <code>format</code>.
   **
   ** @param  date               a date formatted as a string.
   **                            the date must refere to the UTC time zone.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    the date as a {@link Calendar} or
   **                            <code>null</code> if argument string is null or
   **                            empty.
   **
   ** @throws ParseException     if a date/time component is out of range or
   **                            cannot be parsed
   */
  public static Calendar decode(final String date, final String format)
    throws ParseException {

    return decode(date, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decode
  /**
   ** Parses a date given as a string.
   ** <p>
   ** The format of the resulting string is <code>format</code>.
   **
   ** @param  date               a date formatted as a string.
   **                            the date must refere to the UTC time zone.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    the date as a {@link Calendar} or
   **                            <code>null</code> if argument string is null or
   **                            empty.
   **
   ** @throws ParseException     if a date/time component is out of range or
   **                            cannot be parsed
   */
  public static Calendar decode(final String date, final TimeZone timeZone, final String format)
    throws ParseException {

    if (StringUtility.empty(date))
      return null;

    // create a date formatter with the specified format
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    // set the TimeZone of the internal calendar used by the formatter to the
    // requested TimeZone
    formatter.setTimeZone(timeZone);
    formatter.parse(date);

    return formatter.getCalendar();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   date
  /**
   ** Returns the date of a {@link Calendar} instance.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **
   ** @return                    the date as a {@link Date} object.
   */
  public static Date date(final Calendar calendar) {
    return date(calendar, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   date
  /**
   ** Returns the date of a {@link Calendar} instance.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **
   ** @return                    the date as a {@link Date} object.
   */
  public static Date date(final Calendar calendar, final TimeZone timeZone) {
    Date date = new Date(calendar.getTimeInMillis());
    if (!calendar.getTimeZone().equals(timeZone))
      date.setTime(calendar.getTimeInMillis() + timeZone.getRawOffset());

    return date;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calendar
  /**
   ** Returns the calendar of a {@link Date}.
   **
   ** @param  date               the date as a {@link Date}
   **
   ** @return                    the date as a {@link Calendar} object.
   */
  public static Calendar calendar(final Date date) {
    return calendar(date, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calendar
  /**
   ** Returns the calendar of a {@link Date}.
   **
   ** @param  date               the date as a {@link Date}
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **
   ** @return                    the date as a {@link Calendar} object.
   */
  public static Calendar calendar(final Date date, final TimeZone timeZone) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.setTimeZone(timeZone);
    return calendar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startEpoch
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which the epoch starts.
   **
   ** @return                    the allocated {@link Date} object initialized.
   */
  public static Date startEpoch() {
    return new Date(0);
  }

//////////////////////////////////////////////////////////////////////////////
  // Method:   today
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the date only at which it was allocated.
   ** <br>
   ** The time fraction of the {@link Date} value created id zeroed out.
   **
   ** @return                    the allocated {@link Date} object initialized
   **                            that it represents the date only at which it
   **                            was allocated.
   **
   ** @see     ZonedDateTime#now()
   */
  public static Date today() {
    // capture the current moment in the wall-clock time used by the people of a
    // certain region (a time zone)
    final ZonedDateTime today = ZonedDateTime.now(
      // get the JVM's current default time zone. Can change at any moment
      // during runtime
      ZoneId.systemDefault()
    );
    return Date.from(today.toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   now
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which it was allocated, measured to the nearest millisecond.
   **
   ** @return                    the allocated {@link Date} object initialized
   **                            that it represents the time at which it was
   **                            allocated.
   **
   ** @see     LocalDateTime#now()
   */
  public static Date now() {
    // capture the current moment in the wall-clock time used by the people of a
    // certain region (a time zone).
    final ZonedDateTime today = ZonedDateTime.now(
      // get the JVM's current default time zone. Can change at any moment
      // during runtime
      ZoneId.systemDefault()
    );
    return Date.from(LocalDateTime.now().toInstant(today.getOffset()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentTimeMillis
  /**
   ** Returns the current time in milliseconds.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** While the unit of time of the return value is a millisecond, the
   ** granularity of the value depends on the underlying operating system and
   ** may be larger. For example, many operating systems measure time in units
   ** of tens of milliseconds.
   ** <p>
   ** See the description of the class {@link Date} for a discussion of slight
   ** discrepancies that may arise between "computer time" and coordinated
   ** universal time (UTC).
   **
   ** @return                    the difference, measured in milliseconds,
   **                            between the current time and midnight,
   **                            January 1, 1970 UTC.
   **
   ** @see     java.util.Date
   */
  public static long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calendarTimeMillis
  /**
   ** Returns calendar's current time from the given long <code>value</code>.
   **
   ** @param  value              the new time in UTC milliseconds from the
   **                            epoch.
   **
   ** @return                    the current time as UTC milliseconds from the
   **                            epoch.
   **
   ** @see     java.util.Date
   */
  public static long calendarTimeMillis(final long value) {
    final GregorianCalendar calendar = new GregorianCalendar(UTC);
    calendar.setTimeInMillis(value);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   after
  /**
   ** Check if the specified date is after the specified reference, given the
   ** maximum accepted negative clock skew.
   ** <p>
   ** Formula:
   ** <pre>
   **   return date + clock_skew &gt; reference
   ** </pre>
   ** Example:
   ** <br>
   ** Ensure an expiration (exp) timestamp is after the current time, with a
   ** minute of acceptable clock skew.
   ** <pre>
   **   boolean valid = DateUtility.after(exp, new Date(), 60);
   ** </pre>
   **
   ** @param  lhs                the left-hand-side {@link Date} to check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference {@link Date}
   **                            (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, plus the maximum
   **                            accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean after(final Date lhs, final Date rhs, final long clockSkew) {
    return new Date(lhs.getTime() + clockSkew * 1000L).after(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   after
  /**
   ** Check if the specified date is after the specified reference, given the
   ** maximum accepted negative clock skew.
   ** <p>
   ** Formula:
   ** <pre>
   **   return date + clock_skew &gt; reference
   ** </pre>
   ** Example:
   ** <br>
   ** Ensure an expiration (exp) timestamp is after the current time, with a
   ** minute of acceptable clock skew.
   ** <pre>
   **   boolean valid = DateUtility.after(exp, new Date(), 60);
   ** </pre>
   **
   ** @param  lhs                the left-hand-side {@link ZonedDateTime} to
   **                            check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference
   **                            {@link ZonedDateTime} (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, plus the maximum
   **                            accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean after(final ZonedDateTime lhs, final ZonedDateTime rhs, final long clockSkew) {
    return lhs.minusSeconds(clockSkew).isAfter(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Checks if the specified date is before the specified reference, given the
   ** maximum accepted positive clock skew.
   ** <p>
   ** Formula:
   ** <pre>
   **   return date - clock_skew &lt; reference
   ** </pre>
   ** Example:
   ** <br>
   ** Ensure a issued-at (iat) timestamp is before the current time, with a
   ** minute of acceptable clock skew.
   ** <pre>
   **   boolean valid = DateUtility.before(iat, new Date(), 60);
   ** </pre>
   **
   ** @param  lhs                the left-hand-side {@link Date} to check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference {@link Date}
   **                            (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, minus the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean before(final Date lhs, final Date rhs, final long clockSkew) {
    return new Date(lhs.getTime() - clockSkew * 1000L).before(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   before
  /**
   ** Checks if the specified date is before the specified reference, given the
   ** maximum accepted positive clock skew.
   ** <p>
   ** Formula:
   ** <pre>
   **   return date - clock_skew &lt; reference
   ** </pre>
   ** Example:
   ** <br>
   ** Ensure a issued-at (iat) timestamp is before the current time, with a
   ** minute of acceptable clock skew.
   ** <pre>
   **   boolean valid = DateUtility.before(iat, new Date(), 60);
   ** </pre>
   **
   ** @param  lhs                the left-hand-side {@link ZonedDateTime} to
   **                            check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference
   **                            {@link ZonedDateTime} (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, minus the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean before(final ZonedDateTime lhs, final ZonedDateTime rhs, final long clockSkew) {
    return lhs.minusSeconds(clockSkew).isBefore(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   within
  /**
   ** Checks if the specified date is within the specified reference, give or
   ** take the maximum accepted clock skew.
   **
   ** @param  lhs                the left-hand-side {@link Date} to check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference {@link Date}
   **                            (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if <code>lhs</code> is within
   **                            the <code>rhs</code> reference, give or take
   **                            the maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean within(final Date lhs, final Date rhs, final long clockSkew) {
    final long lower = rhs.getTime() - clockSkew * 1000L;
    final long upper = rhs.getTime() + clockSkew * 1000L;
    return lhs.getTime() > lower && lhs.getTime() < upper;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   within
  /**
   ** Checks if the specified date is within the specified reference, give or
   ** take the maximum accepted clock skew.
   **
   ** @param  lhs                the left-hand-side {@link ZonedDateTime} to
   **                            check.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  rhs                the right-hand-side reference
   **                            {@link ZonedDateTime} (e.g. the current time).
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **
   ** @return                    <code>true</code> if <code>lhs</code> is within
   **                            the <code>rhs</code> reference, give or take
   **                            the maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   */
  public static boolean within(final ZonedDateTime lhs, final ZonedDateTime rhs, final long clockSkew) {
    return (lhs.isAfter(rhs.minusSeconds(clockSkew))) && (lhs.isBefore(rhs.plusSeconds(clockSkew))) ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeISO8601
  /**
   ** Encodes the provided timestamp in ISO time format.
   **
   ** @param  value              the timestamp to be encoded in ISO time format.
   **                            <br>
   **                            It should use the same format as the
   **                            {@link System#currentTimeMillis()} method
   **                            (i.e., the number of milliseconds since
   **                            12:00am UTC on January 1, 1970).
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeISO8601(final long value) {
    return encodeISO8601(new Date(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeISO8601
  /**
   ** Encodes the provided date in ISO time format.
   **
   ** @param  value              the date to be encoded in ISO time format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeISO8601(final Calendar value) {
    return encodeISO8601(value.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeISO8601
  /**
   ** Encodes the provided date in ISO time format.
   **
   ** @param  value              the date to be encoded in ISO time format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeISO8601(final Date value) {
    SimpleDateFormat formatter = ISO8601_FORMATTER.get();
    if (formatter == null) {
      formatter = new SimpleDateFormat(ISO8601_ZULU_NANO);
      formatter.setTimeZone(UTC);
      ISO8601_FORMATTER.set(formatter);
    }
    return formatter.format(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC3339
  /**
   ** Encodes the provided timestamp in internet time format.
   **
   ** @param  value              the timestamp to be encoded in internet time
   **                            format.
   **                            <br>
   **                            It should use the same format as the
   **                            {@link System#currentTimeMillis()} method
   **                            (i.e., the number of milliseconds since
   **                            12:00am UTC on January 1, 1970).
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeRFC3339(final long value) {
    return encodeRFC3339(new Date(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC3339
  /**
   ** Encodes the provided date in internet time format.
   **
   ** @param  value              the date to be encoded in internet time
   **                            format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeRFC3339(final Calendar value) {
    return encodeRFC3339(value.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC3339
  /**
   ** Encodes the provided date in internet time format.
   **
   ** @param  value              the date to be encoded in internet time
   **                            format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the internet time representation of the
   **                            provided date.
   */
  public static String encodeRFC3339(final Date value) {
    SimpleDateFormat formatter = RFC3339_FORMATTER.get();
    if (formatter == null) {
      formatter = new SimpleDateFormat(RFC3339_ZULU_NANO);
      formatter.setTimeZone(UTC);
      RFC3339_FORMATTER.set(formatter);
    }
    return formatter.format(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC4517
  /**
   ** Encodes the provided timestamp in generalized time format.
   **
   ** @param  value              the timestamp to be encoded in generalized time
   **                            format.
   **                            <br>
   **                            It should use the same format as the
   **                            {@link System#currentTimeMillis()} method
   **                            (i.e., the number of milliseconds since
   **                            12:00am UTC on January 1, 1970).
   **
   ** @return                    the generalized time representation of the
   **                            provided date.
   */
  public static String encodeRFC4517(final long value) {
    return encodeRFC4517(new Date(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC4517
  /**
   ** Encodes the provided date in generalized time format.
   **
   ** @param  value              the date to be encoded in generalized time
   **                            format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the generalized time representation of the
   **                            provided date.
   */
  public static String encodeRFC4517(final Calendar value) {
    return encodeRFC4517(value.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeRFC4517
  /**
   ** Encodes the provided date in generalized time format.
   **
   ** @param  value              the date to be encoded in generalized time
   **                            format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the generalized time representation of the
   **                            provided date.
   */
  public static String encodeRFC4517(final Date value) {
    SimpleDateFormat formatter = RFC4517_FORMATTER.get();
    if (formatter == null) {
      formatter = new SimpleDateFormat(RFC4517_ZULU_NANO);
      formatter.setTimeZone(UTC);
      RFC4517_FORMATTER.set(formatter);
    }

    return formatter.format(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeISO8601
  /**
   ** Decodes the provided string as a timestamp encoded in the ISO 8601 format
   ** described in .
   **
   ** @param  value              the timestamp to be decoded in ISO 8601 format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the {@link Calendar} object decoded from the
   **                            provided timestamp.
   **
   ** @throws ParseException     if the provided string could not be decoded as
   **                            a timestamp in the RFC 3339 time format.
   */
  public static Calendar decodeISO8601(final String value)
    throws ParseException {

    return decodeCalendar(value, ' ');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeRFC3339
  /**
   ** Decodes the provided string as a timestamp encoded in the ISO 8601 format
   ** described in RFC 3339.
   **
   ** @param  value              the timestamp to be decoded in the RFC 3339
   **                            format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the {@link Calendar} object decoded from the
   **                            provided timestamp.
   **
   ** @throws ParseException     if the provided string could not be decoded as
   **                            a timestamp in the RFC 3339 time format.
   */
  public static Calendar decodeRFC3339(final String value)
    throws ParseException {

    return decodeCalendar(value, 'T');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeRFC4517
  /**
   ** Decodes the provided string as a timestamp in generalized time format.
   **
   ** @param  value              the timestamp to be decoded.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the {@link Calendar} object decoded from the
   **                            provided timestamp.
   **
   ** @throws ParseException     if the provided string could not be decoded as
   **                            a timestamp in generalized time format.
   */
  public static Calendar decodeRFC4517(final String value)
    throws ParseException {

    SystemValidator.assertNotNull(value);

    // extract the time zone information from the end of the value.
    int            pos;
    final TimeZone tz;
    if (value.endsWith("Z")) {
      tz  = TimeZone.getTimeZone("UTC");
      pos = value.length() - 1;
    }
    else {
      pos = value.lastIndexOf('-');
      if (pos < 0) {
        pos = value.lastIndexOf('+');
        if (pos < 0)
          throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_TIMEZONE_INVALID, value), 0);
      }

      tz = TimeZone.getTimeZone("GMT" + value.substring(pos));
      if (tz.getRawOffset() == 0) {
        // this is the default time zone that will be returned if the value
        // cannot be parsed
        // if it's valid, then it will end in "+0000" or "-0000"
        // otherwise, it's invalid and GMT was just a fallback
        if (!(value.endsWith("+0000") || value.endsWith("-0000"))) {
          throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_TIMEZONE_INVALID, value), pos);
        }
      }
    }

    // see if the timestamp has a sub-second portion
    // if there is a sub-second portion, then we may need to massage the value
    // so that there are exactly three sub-second characters so that it can be
    // interpreted as milliseconds
    final String subsec;
    final String trimmed;
    int          periodPos = value.lastIndexOf('.', pos);
    if (periodPos > 0) {
      final int subSecondLength = pos - periodPos - 1;
      switch (subSecondLength) {
        case 0: subsec = "";
          trimmed = value.substring(0, periodPos);
          break;
        case 1: subsec = ".SSS";
          trimmed = value.substring(0, (periodPos + 2)) + "00";
          break;
        case 2: subsec = ".SSS";
          trimmed = value.substring(0, (periodPos + 3)) + '0';
          break;
        default: subsec = ".SSS";
          trimmed = value.substring(0, periodPos + 4);
          break;
      }
    }
    else {
      subsec    = "";
      periodPos = pos;
      trimmed   = value.substring(0, pos);
    }
    // look at where the period is (or would be if it existed) to see how many
    // characters are in the integer portion
    // this will give us what we need for the rest of the format string
    final String formatStr;
    switch (periodPos) {
      case 10 : formatStr = "yyyyMMddHH" + subsec;
                break;
      case 12 : formatStr = "yyyyMMddHHmm" + subsec;
                break;
      case 14 : formatStr = "yyyyMMddHHmmss" + subsec;
                break;
      default : throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_TIMESTAMP_LENGTH, value), periodPos);
    }
    // finally be able to create an appropriate date format object to parse the
    // trimmed version of the timestamp
    final SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
    dateFormat.setTimeZone(tz);
    dateFormat.setLenient(false);

    // put everything together to construct the appropriate date
    final Calendar calendar = GregorianCalendar.getInstance();
    calendar.setTime(dateFormat.parse(trimmed));
    return calendar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validateSeparator
  /**
   ** Ensures that the provided timestamp string has the expected character at
   ** the specified position.
   **
   ** @param  value              the timestamp expression to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  pos                the position of the character to examine.
   ** @param  expected           the character expected at the specified
   **                            position.
   **
   ** @throws ParseException     if the provided timestamp does not have the
   **                            expected.
   */
  private static void validateSeparator(final String value, final int pos, final char expected)
    throws ParseException {

    if (value.charAt(pos) != expected)
      throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_SEPARATOR_INVALID, value, value.charAt(pos), pos, expected), pos);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeCalendar
  /**
   ** Decodes the provided string as a timestamp encoded in the ISO 8601 or
   ** RFC 3339 format.
   **
   ** @param  value              the timestamp to be decoded in ISO 8601 or
   **                            RFC 3339 format.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the {@link Date} object decoded from the
   **                            provided timestamp.
   **
   ** @throws ParseException     if the provided string could not be decoded as
   **                            a timestamp in the RFC 3339 time format.
   */
  private static Calendar decodeCalendar(final String value, final char separator)
    throws ParseException {

    // make sure that the string representation has the minimum acceptable
    // length
    if (value.length() < 20)
      throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_TIMEZONE_INVALID, value), 0);

    // parse the year, month, day, hour, minute, and second components from the
    // timestamp, and make sure the appropriate separator characters are between
    // those components
    final int year = decodeNumber(value, 0, 4);
    validateSeparator(value, 4, '-');
    final int month = decodeNumber(value, 5, 2);
    validateSeparator(value, 7, '-');
    final int day = decodeNumber(value, 8, 2);
    validateSeparator(value, 10, separator);
    final int hour = decodeNumber(value, 11, 2);
    validateSeparator(value, 13, ':');
    final int minute = decodeNumber(value, 14, 2);
    validateSeparator(value, 16, ':');
    final int second = decodeNumber(value, 17, 2);

    // make sure that the month and day values are acceptable.
    switch (month) {
      case 1  :
      case 3  :
      case 5  :
      case 7  :
      case 8  :
      case 10 :
      case 12 : // January, March, May, July, August, October, and December all
                // have 31 days
                if ((day < 1) || (day > 31))
                  throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_DAY, value, day, month), 8);
                break;

      case 4  :
      case 6  :
      case 9  :
      case 11 : // April, June, September, and November all have 30 days
                if ((day < 1) || (day > 30))
                  throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_DAY, value, day, month), 8);
                break;
      case 2  : // February can have 28 or 29 days, depending on whether it's a
                // leap year
                // Although we could determine whether the provided year is a
                // leap year, we'll just always accept up to 29 days for
                // February
                if ((day < 1) || (day > 29))
                  throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_DAY, value, day, month), 8);
                break;

      default : throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_MONTH, value, month), 5);
    }
    // Make sure that the hour, minute, and second values are acceptable
    // while ISO 8601 permits a value of 24 for the hour, RFC 3339 only permits
    // hour values between 0 and 23
    // some minutes can have up to 61 seconds for leap seconds, so we'll always
    // account for that
    if ((hour < 0) || (hour > 23))
      throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_HOUR, value, hour), 11);

    if ((minute < 0) || (minute > 59))
      throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_MINUTE, value, minute), 14);

    if ((second < 0) || (second > 60))
      throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_INVALID_SECOND, value, second), 17);

    // see if there is a sub-second portion
    // if so, then there will be a period at position 19 followed by at least
    // one digit
    // this implementation will only support timestamps with no more than three
    // sub-second digits
    int ms = 0;
    int tz = -1;
    if (value.charAt(19) == '.') {
      int length = 0;
      final StringBuilder milli = new StringBuilder(3);
      for (int pos=20; pos < value.length(); pos++) {
        final char c = value.charAt(pos);
        switch (c) {
          case '0' : length++;
                     if (milli.length() > 0) {
                       // only add a zero if it's not the first digit
                       milli.append(c);
                     }
                     break;
          case '1' :
          case '2' :
          case '3' :
          case '4' :
          case '5' :
          case '6' :
          case '7' :
          case '8' :
          case '9' : length++;
                     milli.append(c);
                     break;
          case 'Z' :
          case '+' :
          case '-' : tz = pos;
                     break;
          default  : throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_SUBSEC_INVALID, value, c, pos), pos);
        }

        if (tz > 0)
          break;

        if (length > 3)
          throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_SUBSEC_DIGITS, value), 20);
      }

      if (tz < 0)
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_MISSING, value), (value.length() - 1));

      if (length == 0)
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_SUBSEC_MISSING, value), 19);

      if (milli.length() == 0) {
        // this is possible if the sub-second portion is all zeroes
        milli.append('0');
      }

      ms = Integer.parseInt(milli.toString());
      if (length == 1) {
        ms *= 100;
      }
      else if (length == 2) {
        ms *= 10;
      }
    }
    else {
      tz = 19;
    }

    // the remainder of the timestamp should be the time zone
    final TimeZone timeZone;
    if (value.substring(tz).equals("Z")) {
      // this is shorthand for the UTC time zone.
      timeZone = UTC;
    }
    else {
      // this is an offset from UTC, which should be in the form "+HH:MM" or
      // "-HH:MM".  Make sure it has the expected length
      if ((value.length() - tz) != 6) {
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_INVALID, value), tz);
      }
      // make sure it starts with "+" or "-".
      final int firstChar = value.charAt(tz);
      if ((firstChar != '+') && (firstChar != '-'))
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_INVALID, value), tz);

      // make sure the hour offset is valid.
      final int timeZoneHourOffset = decodeNumber(value, (tz + 1), 2);
      if ((timeZoneHourOffset < 0) || (timeZoneHourOffset > 23))
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_INVALID, value), tz);

      // make sure there is a colon between the hour and the minute portions of
      // the offset.
      if (value.charAt(tz + 3) != ':')
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_INVALID, value), tz);

      final int timeZoneMinuteOffset = decodeNumber(value, (tz + 4), 2);
      if ((timeZoneMinuteOffset < 0) || (timeZoneMinuteOffset > 59))
        throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_TIMEZONE_INVALID, value), tz);

      timeZone = TimeZone.getTimeZone( "GMT" + value.substring(tz));
    }

    // put everything together to construct the appropriate date
    final GregorianCalendar calendar =
      new GregorianCalendar(
        year
      , (month - 1) // Calendar stupidly uses zero-indexed months
      , day
      , hour
      , minute
      , second
    );
    calendar.set(GregorianCalendar.MILLISECOND, ms);
    calendar.setTimeZone(timeZone);
    return calendar;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeNumber
  /**
   ** Decodes the number at the specified location in the timestamp.
   **
   ** @param  value              the timestamp expression to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  start              the position at which to begin parsing the
   **                            number.
   ** @param  length             the number of digits in the number.
   **
   ** @return                    the number parsed from the provided timestamp.
   **
   ** @throws ParseException     if a problem is encountered while trying to
   **                            parse the number from the timestamp.
   */
  private static int decodeNumber(final String value, final int start, final int length)
    throws ParseException {

    int digit = 0;
    for (int i = 0; i < length; i++) {
      digit *= 10;
      switch (value.charAt(start + i)) {
        case '0': break;
        case '1' : digit += 1;
                   break;
        case '2' : digit += 2;
                   break;
        case '3' : digit += 3;
                   break;
        case '4' : digit += 4;
                   break;
        case '5' : digit += 5;
                   break;
        case '6' : digit += 6;
                   break;
        case '7' : digit += 7;
                   break;
        case '8' : digit += 8;
                   break;
        case '9' : digit += 9;
                   break;
        default  : throw new ParseException(SystemBundle.RESOURCE.string(SystemBundle.DECODE_RFC3339_DIGIT_INVALID, value, value.charAt(start + i), (start + i)), (start + i));
      }
    }
    return digit;
  }
}