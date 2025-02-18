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

    File        :   DateUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DateUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.GregorianCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.DatatypeConverter;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;

////////////////////////////////////////////////////////////////////////////////
// class DateUtility
// ~~~~~ ~~~~~~~~~~~
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
public class DateUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final char   MODIFIER_YEARS    = 'y';
  public static final char   MODIFIER_MONTHS   = 'M';
  public static final char   MODIFIER_DAYS     = 'd';
  public static final char   MODIFIER_HOURS    = 'H';
  public static final char   MODIFIER_MINUTES  = 'm';
  public static final char   MODIFIER_SECONDS  = 'S';

  /**
   ** Date format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String ISO8601_DATE      = "yyyy-MM-dd";

  /**
   ** Time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String ISO8601_TIME      = "HH:mm:ss";

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
  public static final String ISO8601           = ISO8601_DATE + "'T'" + ISO8601_TIME;

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class {@link SimpleDateFormat}.
   */
  public static final String ISO8601_ZULU      = ISO8601 + "'Z'";

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String ISO8601_ZULU_NANO = ISO8601 + ".SSS'Z'";

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
  public static final String       XML8601           = ISO8601_DATE + "'T'" + ISO8601_TIME;

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
  public static final String       XML8601_ZULU      = XML8601 + "'Z'";

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String       XML8601_ZULU_NANO = XML8601 + ".SSS'Z'";

  /**
   ** A value of the Generalized Time syntax is a character string representing
   ** a date and time.
   ** <br>
   ** The LDAP-specific encoding of a value of this syntax is a restriction
   ** of the format defined in [ISO8601].
   */
  public static final String RFC4517           = "yyyyMMddHHmmSS";

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String RFC4517_ZULU      = RFC4517 + "'Z'";

  /**
   ** Date/time format used for parsing.
   ** <br>
   ** The format it compatible with the format of class
   ** {@link SimpleDateFormat}.
   */
  public static final String RFC4517_ZULU_NANO = RFC4517 + ".SSS'Z'";

  /**
   ** the Coordinated Universal Time (UTC) timezone.
   ** the Coordinated Universal Time (UTC) is only an alias for Greenwich Mean
   ** Time (GMT).
   */
  public static final TimeZone   UTC           = TimeZone.getTimeZone("UTC");

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static DatatypeFactory factory       = null;
  private static DateUtility     instance      = new DateUtility();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      factory = DatatypeFactory.newInstance();
    }
    catch (DatatypeConfigurationException e) {
      throw new IllegalStateException("Exception while obtaining Datatype Factory instance", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private long                 offset                       = 0L;

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
    final String offset = System.getProperty("oracle.iam.identity.icf.foundation.dateOffset");
    offset(offset);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   currentTimeMillis
  /**
   ** Returns the current time in milliseconds.
   ** <br>
   ** Note that while the unit of time of the return value is a millisecond,
   ** the granularity of the value depends on the underlying operating system
   ** and may be larger. For example, many operating systems measure time in
   ** units of tens of milliseconds.
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
  // Method:   startEpoch
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which the epoch starts.
   **
   ** @return                    the allocated {@link Date} object initialized
   */
  public static Date startEpoch() {
    return new Date(0);
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
   ** @see     #currentTimeMillis()
   */
  public static Date now() {
    return date(currentTimeMillis());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   date
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which it was allocated, measured to the nearest millisecond.
   ** Allocates a {@link Date} object and initializes it to represent the
   ** specified number of milliseconds since the standard base time known as
   ** "the epoch", namely January 1, 1970, 00:00:00 GMT.
   **
   ** @param  date               the number of milliseconds since the standard
   **                            base time known as "the epoch", namely
   **                            January 1, 1970, 00:00:00 GMT.
   **
   ** @return                    the allocated {@link Date} object initialized
   **                            that it represent the specified number of
   **                            milliseconds since the standard base time known
   **                            as "the epoch", namely
   **                            January 1, 1970, 00:00:00 GMT.
   **
   ** @see     #date(long)
   */
  public static Date date(final Long date) {
    return date(date.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   date
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which it was allocated, measured to the nearest millisecond.
   ** Allocates a {@link Date} object and initializes it to represent the
   ** specified number of milliseconds since the standard base time known as
   ** "the epoch", namely January 1, 1970, 00:00:00 GMT.
   **
   ** @param  date               the number of milliseconds since the standard
   **                            base time known as "the epoch", namely
   **                            January 1, 1970, 00:00:00 GMT.
   **
   ** @return                    the allocated {@link Date} object initialized
   **                            that it represent the specified number of
   **                            milliseconds since the standard base time known
   **                            as "the epoch", namely
   **                            January 1, 1970, 00:00:00 GMT.
   **
   ** @see     #date(Long)
   */
  public static Date date(final long date) {
    return new Date(date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   date
  /**
   ** Converts an {@link XMLGregorianCalendar} to an instance of {@link Date}.
   **
   ** @param  calendar           the instance of {@link XMLGregorianCalendar} or
   **                            a <code>null</code> reference.
   **                            Allowed object is {@link XMLGregorianCalendar}.
   **
   ** @return                    a {@link Date} instance whose value is based
   **                            upon the value in the <code>calendar</code>
   **                            parameter. If the <code>calendar</code>
   **                            parameter is <code>null</code> then this method
   **                            will simply return <code>null</code>.
   **                            Possible object {@link Date}.
   */
  public static Date date(final XMLGregorianCalendar calendar) {
    return (calendar == null) ? null : calendar.toGregorianCalendar().getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offset
  /**
   ** Returns the offset
   **
   ** @return                    the offset in relative format.
   */
  public static long offset() {
    return instance().offset;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offset
  /**
   ** Set the offset.
   **
   ** @param  offset             the offset, either absolut in the format
   **                            <code>yyyyMMddHHmmSS</code> or relative.
   **                            If a relative value is provided the value must
   **                            start with a <code>+</code> or <code>-</code>
   **                            sign.
   **                            Without any further information the value is
   **                            interpreted as the ammount of milliseconds.
   **                            By definining a modifier at the end of the
   **                            value provided the behavior can be changed.
   **                            Following cassensitive modifiers are allowed:
   **                            <ul>
   **                              <li><code>y</code> - Years
   **                              <li><code>M</code> - Month
   **                              <li><code>d</code> - Days
   **                              <li><code>H</code> - Hours
   **                              <li><code>m</code> - Minutes
   **                              <li><code>s</code> - Seconds
   **                            </ul>
   **                            Example: <code>+3y</code>
   */
  public void offset(final String offset) {
    long now = currentTimeMillis();
    if (offset != null) {
      if (offset.startsWith("+")) {
        offset(parse(now, 1, offset.substring(1)));
      }
      else if (offset.startsWith("-")) {
        offset(parse(now, -1, offset.substring(1)));
      }
      else {
        // convert to relative offset
        final SimpleDateFormat format = new SimpleDateFormat(RFC4517);
        try {
          offset(format.parse(offset).getTime() - now);
        }
        catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offset
  /**
   ** Set the offset.
   **
   ** @param  offset             the offset relative to the current time.
   **                            Can be negative.
   */
  public void offset(final long offset) {
    this.offset = offset;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   calendar
  /**
   ** Converts a {@link Date} into an calendar of type
   ** {@link XMLGregorianCalendar}.
   **
   ** @param  date               the {@link Date} or a <code>null</code>
   **                            reference.
   **                            Allowed object is {@link Date}.
   **
   ** @return                    a calendar whose value is based upon the value
   **                            in the <code>date</code> parameter. If the
   **                            <code>date</code> parameter is
   **                            <code>null</code> then this method will simply
   **                            return <code>null</code>.
   **                            Possible object {@link XMLGregorianCalendar}.
   */
  public static XMLGregorianCalendar calendar(final Date date) {
    if (date == null) {
      return null;
    }
    else {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(date.getTime());
      return factory.newXMLGregorianCalendar(calendar);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the singleton instance of the {@link DateUtility}.
   **
   ** @return                    the singleton instance of the
   **                            {@link DateUtility}.
   */
  public static DateUtility instance() {
    return instance;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two {@link Date}s if they are identically.
   **
   ** @param  lhs                the first {@link Date} to be tested for
   **                            equality.
   ** @param  rhs                the second {@link Date} to be tested for
   **                            equality.
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
  // Method:   parse
  /**
   ** Converts the string argument into a {@link Calendar} value.
   **
   ** @param  dateTime           a string containing lexical representation of
   **                            xsd:datetime.
   **
   ** @return                    a {@link Calendar} object represented by the
   **                            string argument.
   **
   ** @throws IllegalArgumentException if string parameter does not conform to
   **                                  lexical value space defined in XML Schema
   **                                  Part 2: Datatypes for xsd:dateTime.
   */
  public static Calendar parse(final String dateTime)
    throws IllegalArgumentException {

    return DatatypeConverter.parseDateTime(dateTime);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Converts the string argument into a {@link Date} value.
   ** <p>
   ** <b>Note</b>:
   ** If parameter <code>dateTime</code> does not conform to lexical value space
   ** defined in parameter <code>pattern</code> the returned {@link Date} value
   ** is equivalent to <i>Thu Jan 01 01:00:00 GMT 1970</i> (Start od Epoche).
   **
   ** @param  dateTime           a string containing lexical representation of
   **                            a date.
   ** @param  pattern            the pattern describing the date and time format
   **                            of the lexical representation of a date.
   **
   ** @return                    a {@link Date} object represented by the string
   **                            argument.
   */
  public static Date parse(final String dateTime, final String pattern) {
    final SimpleDateFormat format = new SimpleDateFormat(pattern);
    try {
      return format.parse(dateTime);
    }
    catch (ParseException e) {
      e.printStackTrace(System.err);
      return startEpoch();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Long} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Long}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Long date) {
    return format(date(date.longValue()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Long} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Long}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Long date, final String format) {
    return format(date(date.longValue()), format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Long} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Long}.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Long date, final Locale locale) {
    return format(date(date.longValue()), locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Date}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date) {
    return format(date, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final Locale locale) {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale).format(date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final String format) {
    return format(date, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final String format, final Locale locale) {
    return format(date, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  formatter          the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final SimpleDateFormat formatter) {
    return formatter.format(date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar) {
    return format(calendar, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar, final String format) {
    return format(calendar, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar, final TimeZone timeZone) {
    return format(calendar, timeZone, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar, final TimeZone timeZone, final String format) {
    return format(calendar, timeZone, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar, final TimeZone timeZone, final Locale locale) {
    return format(calendar, timeZone, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar calendar, final TimeZone timeZone, final String format, final Locale locale) {
    return format(calendar, timeZone, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Calendar}.
   ** @param  time               the time as a {@link Calendar}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar date, final Calendar time) {
    return format(date, time, UTC, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Calendar}.
   ** @param  time               the time as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Calendar date, final Calendar time, final TimeZone timeZone, final Locale locale) {
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    dateFormatter.setTimeZone(timeZone);
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.LONG, locale);
    timeFormatter.setTimeZone(timeZone);
    return dateFormatter.format(date.getTime()) + " " + timeFormatter.format(time.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final TimeZone timeZone) {
    final Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTime(date);
    return format(calendar, timeZone);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final TimeZone timeZone, final String format) {
    return format(date, timeZone, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final TimeZone timeZone, final Locale locale) {
    return format(date, timeZone, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final TimeZone timeZone, final String format, final Locale locale) {
    return format(date, timeZone, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  time               the time as a {@link Date}.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final Date time) {
    return format(date, time, UTC, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  time               the time as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  locale             the {@link Locale} whose date format symbols
   **                            should be used.
   **
   ** @return                    date as a formatted string.
   */
  public static String format(final Date date, final Date time, final TimeZone timeZone, final Locale locale) {
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    dateFormatter.setTimeZone(timeZone);
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.LONG, locale);
    timeFormatter.setTimeZone(timeZone);
    return dateFormatter.format(date.getTime()) + " " + timeFormatter.format(time.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Date} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  formatter          the requested {@link DateFormat} to produce the
   **                            output.
   **
   ** @return                    date as a formatted string.
   */
  private static String format(final Date date, final TimeZone timeZone, final DateFormat formatter) {
    final Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTime(date);
    return format(calendar, timeZone, formatter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from UTC.
   ** @param  formatter          the requested {@link DateFormat} to produce the
   **                            output.
   **
   ** @return                    date as a formatted string.
   */
  private static String format(final Calendar calendar, final TimeZone timeZone, final DateFormat formatter) {
    formatter.setTimeZone(calendar.getTimeZone());
    long distance = calendar.getTimeZone().getRawOffset() + timeZone.getRawOffset();
    Date date = new Date(calendar.getTimeInMillis() + distance);
    return formatter.format(date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Returns the evaluated offset.
   **
   ** @param  now                the current system timestamp.
   ** @param  sign               determines if the amount has to be
   **                            treated as a positive value (future dated) or
   **                            negative value (past dated).
   ** @param  value              the string repesentation of the amount of date
   **                            or time.
   **
   ** @return                    the evaluated offset.
   */
  private long parse(final long now, final int sign, final String value) {
    if (StringUtility.empty(value, true)) {
      return 0;
    }
    final char    modifier = value.charAt(value.length() - 1);
    // last character may be a format modifier
    final boolean modify   = Character.isDigit(modifier) ? false : true;
    return modify ? offset(now, modifier, Integer.parseInt(value.substring(0, value.length() - 1)) * sign) : Long.parseLong(value) * sign;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   offset
  /**
   ** Adds the specified (signed) amount of time to a certain calendar field,
   ** based on the calendar's rules and calculates the offset to the given
   ** current system timestamp <code>now</code>.
   **
   ** @param  now                the current system timestamp.
   ** @param  modifier
   ** @param  amount             the amount of date or time to be added to a
   **                            certain calendar field.
   **
   ** @return                    the timestamp value with the evaluated offset.
   */
  private long offset(final long now, final char modifier, final int amount) {
    final GregorianCalendar calendar = new GregorianDateTime();
    calendar.setTimeInMillis(now);
    // calculate the amount of date or time to be added to a certain field
    if (modifier == MODIFIER_YEARS) {
      calendar.add(Calendar.YEAR, amount);
    }
    else if (modifier == MODIFIER_MONTHS) {
      calendar.add(Calendar.MONTH, amount);
    }
    else if (modifier == MODIFIER_DAYS) {
      calendar.add(Calendar.DATE, amount);
    }
    else if (modifier == MODIFIER_HOURS) {
      calendar.add(Calendar.HOUR_OF_DAY, amount);
    }
    else if (modifier == MODIFIER_MINUTES) {
      calendar.add(Calendar.MINUTE, amount);
    }
    else if (modifier == MODIFIER_MINUTES) {
      calendar.add(Calendar.MINUTE, amount);
    }
    else if (modifier == MODIFIER_SECONDS) {
      calendar.add(Calendar.SECOND, amount);
    }
    return calendar.getTimeInMillis() - now;
  }
}