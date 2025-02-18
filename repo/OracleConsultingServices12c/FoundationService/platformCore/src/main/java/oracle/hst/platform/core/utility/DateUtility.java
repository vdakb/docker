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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   DateUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DateUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;

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

  public static final char         MODIFIER_YEARS    = 'y';
  public static final char         MODIFIER_MONTHS   = 'M';
  public static final char         MODIFIER_DAYS     = 'd';
  public static final char         MODIFIER_HOURS    = 'H';
  public static final char         MODIFIER_MINUTES  = 'm';
  public static final char         MODIFIER_SECONDS  = 'S';

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
  public static final String       RFC4517_TIME      = "HHmmSS";

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
   * Date/time format used for parsing.
   * The format it compatible with the format of class {@link SimpleDateFormat}.
   */
  public static final String       DEFAULT           = "EEEE yyyy/MM/dd hh:mm:ss aa zz : zzzzzz";

  /**
   ** the Coordinated Universal Time (UTC) timezone.
   ** the Coordinated Universal Time (UTC) is only an alias for Greenwich Mean
   ** Time (GMT).
   */
  public static final TimeZone     UTC               = TimeZone.getTimeZone("UTC");

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
   ** @param  a                  the first {@link Date} to be tested for
   **                            equality.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  b                  the second {@link Date} to be tested for
   **                            equality.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    <code>true</code> if the {@link Date}s are
   **                            identically; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean equal(final Date a, final Date b) {
    if (a == b)
      return true;
    else if (a == null && b == null)
      return true;
    else if ((a == null && b != null) || (a != null && b == null))
      return false;
    else
      return a.equals(b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Transforms the current system date in a string representation with the
   ** {@link #DEFAULT} format.
   **
   ** @return                    the current system date as a {@link String} in
   **                            the {@link #DEFAULT} format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate() {
    return formatDate(System.currentTimeMillis(), DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Transforms the current system date in a string representation with the
   ** specified format.
   **
   ** @param  format             the format in which the timestamp should be
   **                            converted.
   **
   ** @return                    the current system date as a {@link String}
   **                            in the specified format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final String format) {
    return formatDate(System.currentTimeMillis(), format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Convert timestamp to displayable form.
   ** <br>
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   **                            <br>
   **                            Allowed object is <code>lomg</code>.
   **
   ** @return                    the string representation of the timestamp in
   **                            the requested format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final long timestamp) {
    return formatDate(timestamp, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Convert timestamp to displayable form.
   ** <br>
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the string representation of the timestamp in
   **                            the requested format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Date timestamp) {
    return formatDate(timestamp, DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Convert timestamp to displayable form.
   ** <br>
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   ** @param  format             the format in which the timestamp should be
   **                            converted.
   **
   ** @return                    the string representation of the timestamp in
   **                            the requested format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final long timestamp, final String format) {
    return formatDate(new Date(timestamp), format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Convert timestamp to displayable form.
   ** <br>
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   ** @param  format             the format in which the timestamp should be
   **                            converted.
   **
   ** @return                    the string representation of the timestamp in
   **                            the requested format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Date timestamp, final String format) {
    return formatDate(timestamp, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Convert timestamp to displayable form.
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the format in which the timestamp should be
   **                            converted.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string representation of the timestamp in
   **                            the requested format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Date timestamp, final TimeZone  timeZone, final String format) {
    return format(timestamp, timeZone, new SimpleDateFormat(format, Locale.getDefault()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Date timestamp, final TimeZone timeZone, final Locale locale) {
    return format(timestamp, timeZone, DateFormat.getDateInstance(DateFormat.SHORT, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseTime
  /**
   ** Parse the specified <code>dataString</code> to create a new string
   ** representation in the specified format.
   ** <br>
   ** If the specified <code>dataString</code> could not be successfully parsed
   ** the current date will be returned.
   **
   ** @param  timestamp          the timestamp to convert in a human readable
   **                            format.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  format             the format in which the timestamp should be
   **                            converted.
   **                            Allowed object is {@link String}.
   **
   ** @return                    a string representation of the date
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String parseTime(final String timestamp, final String format) {
    Date date = null;
    final SimpleDateFormat parser = new SimpleDateFormat(format);
    try {
      date = parser.parse(timestamp);
    }
    catch (Exception exception) {
      date = new Date(System.currentTimeMillis());
    }
    return parser.format(date);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two date values.
   **
   ** @param  date1              the to compare
   ** @param  date2              the to compare
   **
   ** @return                    the value <code>0</code> if the argument
   **                            <code>date1</code> is equal to argument
   **                            <code>date2</code>; a value less than
   **                            <code>0</code> if <code>date1</code> is before
   **                            argument <code>date2</code>; and a value
   **                            greater than <code>0</code> if
   **                            <code>date1</code> is after argument
   **                            <code>date2</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see java.util.Date
   */
  public static int compare(final Date date1, final Date date2) {
    int  result    = 1;
    long date1Time = 0L;
    if (date1 != null) {
      date1Time = date1.getTime();
      if (date1Time != 0L)
        result = date1.compareTo(date2);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDate
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
   **                            <br>
   **                            Possible object is {@link Calendar}.
   **
   ** @throws ParseException     if a date/time component is out of range or
   **                            cannot be parsed
   */
  public static Calendar parseDate(final String date, final String format)
    throws ParseException {

    return parseDate(date, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDate
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
   **                            <br>
   **                            Possible object is {@link Calendar}.
   **
   ** @throws ParseException     if a date/time component is out of range or
   **                            cannot be parsed
   */
  public static Calendar parseDate(final String date, final TimeZone timeZone, final String format)
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
   ** @param  calendar           the date as a {@link Calendar}
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the date as a {@link Date} object
   **                            <br>
   **                            Possible object is {@link Date}.
   **                            <br>
   **                            Possible object is {@link Calendar}.
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
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @return                    the date as a {@link Date} object.
   **                            <br>
   **                            Possible object is {@link Date}.
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
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the date as a {@link Calendar} object.
   **                            <br>
   **                            Possible object is {@link Calendar}.
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
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @return                    the date as a {@link Calendar} object.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public static Calendar calendar(final Date date, final TimeZone timeZone) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.setTimeZone(timeZone);
    return calendar;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   toXMLGregorianCalendar
  /**
   ** Converts {@link String} to {@link XMLGregorianCalendar}.
   ** <p>
   ** If XJC si used to create Java classes from XML Schema or XSD file, by
   ** default JAXB map XSD data types xsd:date, xsd:time and xsd:dateTime to
   ** XMLGregorianCalendar in Java.
   **
   ** @param  date               the {@link String} to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link XMLGregorianCalendar} derived from
   **                            the specified {@link String}.
   **                            May be <code>null</code> if the specified
   **                            {@link XMLGregorianCalendar} was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link XMLGregorianCalendar}.
   */
  public static XMLGregorianCalendar toXMLGregorianCalendar(final String date) {
    // prevent bogus input
    XMLGregorianCalendar xml = null;
    if (date == null)
      return xml;

    try {
      final SimpleDateFormat format = new SimpleDateFormat(ISO8601);
      xml = toXMLGregorianCalendar(format.parse(date));
    }
    catch (ParseException e) {
      e.printStackTrace();
    }
    return xml;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   toXMLGregorianCalendar
  /**
   ** Converts {@link Date} to {@link XMLGregorianCalendar}.
   ** <p>
   ** If XJC si used to create Java classes from XML Schema or XSD file, by
   ** default JAXB map XSD data types xsd:date, xsd:time and xsd:dateTime to
   ** XMLGregorianCalendar in Java.
   **
   ** @param  date               the {@link Date} to convert.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    the {@link XMLGregorianCalendar} derived from
   **                            the specified {@link Date}.
   **                            May be <code>null</code> if the specified
   **                            {@link XMLGregorianCalendar} was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link XMLGregorianCalendar}.
   */
  public static XMLGregorianCalendar toXMLGregorianCalendar(final Date date) {
    // prevent bogus input
    XMLGregorianCalendar xml = null;
    if (date == null)
      return xml;

    final GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    try {
      xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
    }
    catch (DatatypeConfigurationException e) {
      e.printStackTrace();
    }
    return xml;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   toDate
  /**
   ** Converts {@link XMLGregorianCalendar} to {@link Date} in Java.
   ** <p>
   ** If XJC si used to create Java classes from XML Schema or XSD file, by
   ** default JAXB map XSD data types xsd:date, xsd:time and xsd:dateTime to
   ** XMLGregorianCalendar in Java.
   **
   ** @param  calendar           the {@link XMLGregorianCalendar} to convert.
   **                            <br>
   **                            Allowed object is
   **                            {@link XMLGregorianCalendar}.
   **
   ** @return                    the {@link Date} derived from the specified
   **                            {@link XMLGregorianCalendar}.
   **                            May be <code>null</code> if the specified
   **                            {@link XMLGregorianCalendar} was
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public static Date toDate(final XMLGregorianCalendar calendar) {
    return (calendar == null) ? null : calendar.toGregorianCalendar().getTime();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar) {
    return formatDate(calendar, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar, final String format) {
    return formatDate(calendar, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar, final TimeZone timeZone) {
    return formatDate(calendar, timeZone, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar, final TimeZone timeZone, final String format) {
    return formatDate(calendar, timeZone, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar, final TimeZone timeZone, final Locale locale) {
    return format(calendar, timeZone, DateFormat.getDateInstance(DateFormat.SHORT, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final Calendar calendar, final TimeZone timeZone, final String format, final Locale locale) {
    return format(calendar, timeZone, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short time format of the default
   ** locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar) {
    return formatTime(calendar, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar, final String format) {
    return formatTime(calendar, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short time format of the default
   ** locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar, final TimeZone timeZone) {
    return formatTime(calendar, timeZone, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar, final TimeZone timeZone, final String format) {
    return formatTime(calendar, timeZone, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short time format of the given
   ** locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar, final TimeZone timeZone, final Locale locale) {
    return format(calendar, timeZone, DateFormat.getTimeInstance(DateFormat.LONG, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatTime
  /**
   ** Formats the time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short time format of the given
   ** locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    time as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatTime(final Calendar calendar, final TimeZone timeZone, final String format, final Locale locale) {
    return format(calendar, timeZone, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar) {
    return formatDateTime(calendar, UTC);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar, final String format) {
    return formatDateTime(calendar, UTC, format);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar, final TimeZone timeZone) {
    return formatDateTime(calendar, timeZone, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar, final TimeZone timeZone, final String format) {
    return formatDateTime(calendar, timeZone, format, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar, final TimeZone timeZone, final Locale locale) {
    return format(calendar, timeZone, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  calendar           the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  format             the requested format specification compatible
   **                            with {@link SimpleDateFormat}.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar calendar, final TimeZone timeZone, final String format, final Locale locale) {
    return format(calendar, timeZone, new SimpleDateFormat(format, locale));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** default locale.
   **
   ** @param  date               the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  time               the time as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    date as a formatted string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar date, final Calendar time) {
    return formatDateTime(date, time, UTC, Locale.getDefault());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDateTime
  /**
   ** Formats the date and time time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  time               the time as a {@link Calendar}.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  locale             the given locale.
   **                            <br>
   **                            Allowed object is {@link Locale}.
   **
   ** @return                    date as a formatted string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDateTime(final Calendar date, final Calendar time, final TimeZone timeZone, final Locale locale) {
    DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT, locale);
    dateFormatter.setTimeZone(timeZone);
    DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.LONG, locale);
    timeFormatter.setTimeZone(timeZone);
    return dateFormatter.format(date.getTime()) + " " + timeFormatter.format(time.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startEpoch
  /**
   ** Allocates a {@link Date} object and initializes it so that it represents
   ** the time at which the epoch starts.
   **
   ** @return                    the allocated {@link Date} object initialized.
   **                            <br>
   **                            Possible object is {@link Date}.
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
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @see     ZonedDateTime#now()
   */
  public static Date today() {
    // capture the current moment in the wall-clock time used by the people of a
    // certain region (a time zone).
    final ZonedDateTime today = ZonedDateTime.now(
      // get the JVM's current default time zone. Can change at any moment
      // during runtime. If important, confirm with the user.
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
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @see     LocalDateTime#now()
   */
  public static Date now() {
    // capture the current moment in the wall-clock time used by the people of a
    // certain region (a time zone).
    final ZonedDateTime today = ZonedDateTime.now(
      // get the JVM's current default time zone. Can change at any moment
      // during runtime. If important, confirm with the user.
      ZoneId.systemDefault()
    );
    return Date.from(LocalDateTime.now().toInstant(today.getOffset()));
  }

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
   **                            <br>
   **                            Possible object is <code>long</code>.
   **
   ** @see     java.util.Date
   */
  public static long currentTimeMillis() {
    return System.currentTimeMillis();
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
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  reference          the reference date (e.g. the current
   **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, plus the maximum
   **                            accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean after(final Date date, final Date reference, final long clockSkew) {
    return new Date(date.getTime() + clockSkew * 1000L).after(reference);
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
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  reference          the reference date (e.g. the current
	 **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
	 **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
	 **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, plus the maximum
   **                            accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean after(final ZonedDateTime date, final ZonedDateTime reference, final long clockSkew) {
		return date.minusSeconds(clockSkew).isAfter(reference);
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
   **   </pre>
   **
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
   ** @param  reference          the reference date (e.g. the current
   **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, minus the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean before(final Date date, final Date reference, final long clockSkew) {
    return new Date(date.getTime() - clockSkew * 1000L).before(reference);
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
   **   </pre>
	 **
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  reference          the reference date (e.g. the current
	 **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
	 **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
	 **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is before the reference, minus the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean before(final ZonedDateTime date, final ZonedDateTime reference, final long clockSkew) {
		return date.minusSeconds(clockSkew).isBefore(reference);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   within
  /**
   ** Checks if the specified date is within the specified reference, give or
   ** take the maximum accepted clock skew.
   **
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
   ** @param  reference          the reference date (e.g. the current
   **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
   ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
   **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is within the reference, give or take the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean isWithin(final Date date, final Date reference, final long clockSkew) {
    final long lower = reference.getTime() - clockSkew * 1000L;
    final long upper = reference.getTime() + clockSkew * 1000L;
    return date.getTime() > lower && date.getTime() < upper;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   within
  /**
   ** Checks if the specified date is within the specified reference, give or
   ** take the maximum accepted clock skew.
	 **
   ** @param  date               the date to check.
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  reference          the reference date (e.g. the current
	 **                            time).
   **                            <br>
   **                            Must not be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link ZonedDateTime}.
	 ** @param  clockSkew          the maximum acceptable negative clock skew of
   **                            the <code>date</code> value to check, in
	 **                            seconds.
   **                            <br>
   **                            Allowed object is <code>long</code>.
	 **
   ** @return                    <code>true</code> if the <code>date</code>
   **                            value is within the reference, give or take the
   **                            maximum accepted clock skew; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
	 */
	public static boolean within(final ZonedDateTime date, final ZonedDateTime reference, final long clockSkew) {
		return (date.isAfter(reference.minusSeconds(clockSkew))) && (date.isBefore(reference.plusSeconds(clockSkew))) ;
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
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  formatter          the requested {@link DateFormat} to produce the
   **                            output.
   **                            <br>
   **                            Allowed object is {@link DateFormat}.
   **
   ** @return                    date as a formatted string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String format(final Calendar calendar, final TimeZone timeZone, final DateFormat formatter) {
    return format(calendar.getTime(), timeZone, formatter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   format
  /**
   ** Formats the date and time of a {@link Calendar} instance to a String.
   ** <br>
   ** The format of the resulting string is the short date/time format of the
   ** given locale.
   **
   ** @param  date               the date value.
   **                            <br>
   **                            Allowed object is {@link Date}.
   ** @param  timeZone           a {@link TimeZone} with the given base time
   **                            zone offset from GMT.
   **                            <br>
   **                            Allowed object is {@link TimeZone}.
   ** @param  formatter          the requested {@link DateFormat} to produce the
   **                            output.
   **                            <br>
   **                            Allowed object is {@link DateFormat}.
   **
   ** @return                    date as a formatted string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  private static String format(final Date date, final TimeZone timeZone, final DateFormat formatter) {
    formatter.setTimeZone(timeZone);
    return formatter.format(date);
  }
}