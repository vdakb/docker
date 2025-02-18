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
    Subsystem   :   Ligth-Weight HTTP Server

    File        :   Server.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Server.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.http;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.GregorianCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.EOFException;
import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URLDecoder;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Support
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Helper class containing convenience methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Support {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The SimpleDateFormat compatible formats of dates which must be supported.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** All generated date fields must be in the RFC 1123 format only, while the
   ** others are supported by recipients for backwards-compatibility.
   */
  private static final String[] FORMAT   = {
    // RFC 822, updated by RFC 1123
    "EEE, dd MMM yyyy HH:mm:ss z"
    // RFC 850, obsoleted by RFC 1036
  , "EEEE, dd-MMM-yy HH:mm:ss z"
    // ANSI C's asctime() format
  , "EEE MMM d HH:mm:ss yyyy"
  };

  /** A GMT (UTC) timezone instance. */
  private static final TimeZone GMT      = TimeZone.getTimeZone("GMT");

  /** Date format strings. */
  private static final char[]   DAY      = "Sun Mon Tue Wed Thu Fri Sat".toCharArray();
  private static final char[]   MONTH    = "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".toCharArray();

  /**
   ** The MIME types that can be compressed (prefix/suffix wildcards allowed).
   */
  private static String[]       COMPRESS = {"text/*", "*/javascript", "*icon", "*+xml", "*/json"};

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Support</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Support()" and enforces use of the public method below.
   */
  private Support() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parentPath
  /**
   ** Returns the parent of the given path.
   **
   ** @param  path               the path whose parent is returned (must start
   **                            with '/').
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parent of the given path (excluding
   **                            trailing slash), or null if given path is the
   **                            root path.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String parentPath(String path) {
    // remove trailing slash
    path = trimRight(path, '/');
    int slash = path.lastIndexOf('/');
    return slash < 0 ? null : path.substring(0, slash);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   localHostName
  /**
   ** Returns the local host's auto-detected name.
   **
   ** @return                    the local host name.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String localHostName() {
    try {
      return InetAddress.getLocalHost().getCanonicalHostName();
    }
    catch (UnknownHostException e) {
      return "localhost";
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transfer
  /**
   ** Transfers data from an input stream to an output stream.
   **
   ** @param  source             the input stream to transfer from.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  target             the output stream to transfer to (or
   **                            <code>null</code> to discard output).
   **                            <br>
   **                            Allowed object is {@link OutputStream}.
   ** @param  length             the number of bytes to transfer.
   **                            <br>
   **                            If negative, the entire contents of the input
   **                            stream are transferred.
   **
   ** @throws IOException        if an I/O error occurs, or the input stream
   **                            ends before the requested number of bytes have
   **                            been read.
   */
  public static void transfer(final InputStream source, final OutputStream target, long length)
    throws IOException {

    // small optimization - avoid buffer creation
    if (length == 0 || target == null && length < 0 && source.read() < 0)
      return;

    final byte[] buf = new byte[4096];
    while (length != 0) {
      int count = length < 0 || buf.length < length ? buf.length : (int)length;
      count = source.read(buf, 0, count);
      if (count < 0) {
        if (length > 0)
          throw new IOException("Unexpected end of stream");
        break;
      }
      if (target != null)
        target.write(buf, 0, count);

      length -= length > 0 ? count : 0;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Returns an HTML-escaped version of the given string for safe display
   ** within a web page.
   ** <br>
   ** The characters '&amp;', '&gt;' and '&lt;' must always be escaped, and
   ** single and double quotes must be escaped within attribute values; this
   ** method escapes them always.
   ** <br>
   ** This method can be used for generating both HTML and XHTML valid content.
   **
   ** @param  source             the string to escape.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the escaped string.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @see     <a href="http://www.w3.org/International/questions/qa-escapes">The W3C FAQ</a>
   */
  public static String escape(final String source) {
    int           start  = 0;
    int           lenght = source.length();
    StringBuilder target = new StringBuilder(lenght + 30);
    for (int i = 0; i < lenght; i++) {
      String ref = null;
      switch (source.charAt(i)) {
        case '&'  : ref = "&amp;"; break;
        case '>'  : ref = "&gt;"; break;
        case '<'  : ref = "&lt;"; break;
        case '"'  : ref = "&quot;"; break;
        case '\'' : ref = "&#39;"; break;
      }
      if (ref != null) {
        target.append(source.substring(start, i)).append(ref);
        start = i + 1;
      }
    }
    return start == 0 ? source : target.append(source.substring(start)).toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytes
  /**
   ** Converts strings to bytes by casting the chars to bytes.
   ** <br>
   ** This is a fast way to encode a string as ISO-8859-1/US-ASCII bytes.
   ** <br>
   ** If multiple strings are provided, their bytes are concatenated.
   **
   ** @param  strings            the strings to convert (containing only
   **                            ISO-8859-1 chars).
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    the byte array.
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  public static byte[] bytes(final String... strings) {
    int n = 0;
    for (String s : strings)
      n += s.length();

    byte[] b = new byte[n];
    n = 0;
    for (String s : strings)
      for (int i = 0, len = s.length(); i < len; i++)
        b[n++] = (byte)s.charAt(i);
    return b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   splitElements
  /**
   ** Splits the given element list string (comma-separated header value) into
   ** its constituent non-empty trimmed elements.
   ** <br>
   ** (RFC2616#2.1: element lists are delimited by a comma and optional LWS, and
   ** empty elements are ignored).
   **
   ** @param  expression         tthe element list string to split.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  lower              specifies whether the list elements should be
   **                            lower-cased.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the non-empty elements in the string, or an
   **                            empty array.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  public static String[] splitElements(final String expression, boolean lower) {
    return split(lower && expression != null ? expression.toLowerCase(Locale.US) : expression, ",", -1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits the given string into its constituent non-empty trimmed elements,
   ** which are delimited by any of the given delimiter characters.
   ** <br>
   ** This is a more direct and efficient implementation than using a regex
   ** (e.g. String.split()), trimming the elements and removing empty ones.
   **
   ** @param  expression         the string to split.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  delimiters         the characters used as the delimiters between
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  limit              if positive, limits the returned array size
   **                            (remaining of str in last element).
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the non-empty elements in the string, or an
   **                            empty array.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.empty-statement")
  public static String[] split(final String expression, final String delimiters, int limit) {
    if (expression == null)
      return new String[0];

    Collection<String> elements = new ArrayList<String>();
    int                length   = expression.length();
    int                start    = 0;
    int                end;
    while (start < length) {
      for (end = --limit == 0 ? length : start; end < length && delimiters.indexOf(expression.charAt(end)) < 0; end++)
        // intentionally left blank
        ;

      String element = expression.substring(start, end).trim();
      if (element.length() > 0)
        elements.add(element);
      start = end + 1;
    }
    return elements.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Returns a string constructed by joining the string representations of the
   ** iterated objects (in order), with the delimiter inserted between them.
   **
   ** @param  <T>                the item type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  delimiter          the delimiter that is inserted between the
   **                            joined strings.
   **                            <br>
   **                            Possible object is {@link String}.
   ** @param  items              the items whose string representations are
   **                            joined.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the joined string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static <T> String join(final String delimiter, final Iterable<T> items) {
    final StringBuilder builder = new StringBuilder();
    for (Iterator<T> it = items.iterator(); it.hasNext();)
      builder.append(it.next()).append(it.hasNext() ? delimiter : "");
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimLeft
  /**
   ** Returns the given string with all occurrences of the given character
   ** removed from its left side.
   **
   ** @param  expression         the string to trim.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  c                  the character to remove.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the trimmed string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.empty-statement")
  public static String trimLeft(String expression, final char c) {
    int len = expression.length();
    int start;
    for (start = 0; start < len && expression.charAt(start) == c; start++)
      // intentionally left blank
      ;
    return start == 0 ? expression : expression.substring(start);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimRight
  /**
   ** Returns the given string with all occurrences of the given character
   ** removed from its right side.
   **
   ** @param  expression         the string to trim.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  c                  the character to remove.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the trimmed string.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.empty-statement")
  public static String trimRight(String expression, final char c) {
    int len = expression.length() - 1;
    int end;
    for (end = len; end >= 0 && expression.charAt(end) == c; end--)
      // intentionally left blank
      ;
    return end == len ? expression : expression.substring(0, end + 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trimDuplicates
  /**
   ** Trims duplicate consecutive occurrences of the given character within the
   ** given string, replacing them with a single instance of the character.
   **
   ** @param  expression         the string to trim.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  c                  the character to trim.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the given string with duplicate consecutive
   **                            occurrences of <code>c</code> replaced by a
   **                            single instance of <code>c</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @SuppressWarnings({ "oracle.jdeveloper.java.nested-assignment", "oracle.jdeveloper.java.empty-statement" })
  public static String trimDuplicates(String expression, final char c) {
    int start = 0;
    while ((start = expression.indexOf(c, start) + 1) > 0) {
      int end;
      for (end = start; end < expression.length() && expression.charAt(end) == c; end++)
        // intentionally left blank
        ;
      if (end > start)
        expression = expression.substring(0, start) + expression.substring(end);
    }
    return expression;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readLine
  /**
   ** Reads the ISO-8859-1 encoded string starting at the current stream
   ** position and ending at the first occurrence of the <code>LF</code>
   ** character.
   **
   ** @param  stream             the stream from which the line is read.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the read string, excluding the terminating
   **                            <code>LF</code> character and (if exists) the
   **                            <code>CR</code> character immediately preceding
   **                            it.
   **                            <br>
   **                            Possible object is {@link String}.
   **
   ** @throws IOException        if an I/O error occurs, or the line is longer
   **                            than 8192 bytes.
   ** @throws EOFException       if the stream end is reached before an LF
   **                            character is found.
   **
   ** @see    #readToken(InputStream, int, String, int)
   */
  public static String readLine(final InputStream stream)
    throws IOException {

    return readToken(stream, '\n', "ISO8859_1", 8192);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readToken
  /**
   ** Reads the token starting at the current stream position and ending at the
   ** first occurrence of the given delimiter byte, in the given encoding.
   ** <br>
   ** If <code>LF</code> is specified as the delimiter, a <code>CRLF</code> pair
   ** is also treated as one.
   **
   ** @param  stream             the input stream from which the token is read.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  delimiter          the byte value which marks the end of the
   **                            token, or <code>-1</code> if the token ends at
   **                            the end of the stream.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  encoding           a character-encoding name.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  max                the maximum length (in bytes) to read.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the read token, excluding the delimiter.
   **
   ** @throws IOException                  if an I/O error occurs, or the
   **                                      maximum length is reached before the
   **                                      token end is reached.
   ** @throws EOFException                 if the stream end is reached before a
   **                                      delimiter is found.
   ** @throws UnsupportedEncodingException if the encoding is not supported.
   */
  @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
  public static String readToken(final InputStream stream, final int delimiter, final String encoding, final int max)
    throws IOException {

    // Note:
    // we avoid using a ByteArrayOutputStream here because it suffers the
    // overhead of synchronization for each byte written
    int    b;
    // buffer length
    int    length = 0;
    // number of read bytes
    int    count  = 0;
    // optimization - lazy allocation only if necessary
    byte[] buf = null;
    while ((b = stream.read()) != -1 && b != delimiter) {
      // expand buffer
      if (count == length) {
        if (count == max)
          throw new IOException("Token too large [" + count + "]");

        // start small, double each expansion
        length = length > 0 ? 2 * length : 256;
        length = max < length ? max : length;
        byte[] expanded = new byte[length];
        if (buf != null)
          System.arraycopy(buf, 0, expanded, 0, count);
        buf = expanded;
      }
      buf[count++] = (byte)b;
    }
    if (b < 0 && delimiter != -1)
      throw new EOFException("Unexpected end of stream");

    if (delimiter == '\n' && count > 0 && buf[count - 1] == '\r')
      count--;

    return count > 0 ? new String(buf, 0, count, encoding) : "";
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compressible
  /**
   ** Checks whether data of the given content type (MIME type) is compressible.
   **
   ** @param  type               the content type (MIME type).
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the data is compressible;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean compressible(final String type) {
    // exclude params
    int    pos = type.indexOf(';');
    String ct  = pos < 0 ? type : type.substring(0, pos);
    for (String cursor : COMPRESS)
      if (cursor.equals(ct) || cursor.charAt(0) == '*' && ct.endsWith(cursor.substring(1)) || cursor.charAt(cursor.length() - 1) == '*' && ct.startsWith(cursor.substring(0, cursor.length() - 1)))
        return true;
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDate
  /**
   ** Parses a date string in one of the supported {@link #FORMAT}.
   ** <p>
   ** Received date header values must be in one of the following formats:
   ** <ul>
   **   <li>Sun, 06 Nov 1994 08:49:37 GMT  ; RFC 822, updated by RFC 1123
   **   <li>Sunday, 06-Nov-94 08:49:37 GMT ; RFC 850, obsoleted by RFC 1036
   **   <li>Sun Nov  6 08:49:37 1994       ; ANSI C's asctime() format
   ** </ul>
   **
   ** @param  expression         a string representation of a time value.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed date value.
   **                            <br>
   **                            Possible object is {@link Date}.
   **
   ** @throws IllegalArgumentException if the given string does not contain a
   **                                  valid date format in any of the supported
   **                                  formats.
   */
  public static Date parseDate(final String expression) {
    for (String pattern : FORMAT) {
      final SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
      format.setLenient(false);
      format.setTimeZone(GMT);
      try {
        return format.parse(expression);
      }
      catch (ParseException e) {
        // intentionally left blank to stay in loop
        continue;
      }
    }
    throw new IllegalArgumentException("Invalid date format: " + expression);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatDate
  /**
   ** Formats the given time value as a string in RFC 1123 format.
   **
   ** @param  time               the time in milliseconds since
   **                            January 1, 1970, 00:00:00 GMT.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the given time value as a string in RFC 1123
   **                            format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String formatDate(final long time) {
    // this implementation performs far better than SimpleDateFormat instances,
    // and even quite better than ThreadLocal SDFs - the server's CPU-bound
    // benchmark gains over 20%!
    if (time < -62167392000000L || time > 253402300799999L)
      throw new IllegalArgumentException("Year out of range (0001-9999): " + time);

    // copy the format template
    final char[]   template = "DAY, 00 MON 0000 00:00:00 GMT".toCharArray();
    final Calendar calendar = new GregorianCalendar(GMT, Locale.US);
    calendar.setTimeInMillis(time);
    System.arraycopy(DAY,   4 * (calendar.get(Calendar.DAY_OF_WEEK) - 1), template, 0, 3);
    System.arraycopy(MONTH, 4 * calendar.get(Calendar.MONTH), template, 8, 3);
    int n = calendar.get(Calendar.DATE);
    template[5] += n / 10;
    template[6] += n % 10;
    n = calendar.get(Calendar.YEAR);
    template[12] += n / 1000;
    template[13] += n / 100 % 10;
    template[14] += n / 10 % 10;
    template[15] += n % 10;
    n = calendar.get(Calendar.HOUR_OF_DAY);
    template[17] += n / 10;
    template[18] += n % 10;
    n = calendar.get(Calendar.MINUTE);
    template[20] += n / 10;
    template[21] += n % 10;
    n = calendar.get(Calendar.SECOND);
    template[23] += n / 10;
    template[24] += n % 10;
    return new String(template);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseRange
  /**
   ** Returns the absolute (zero-based) content range value specified by the
   ** given range string. If multiple ranges are requested, a single range
   ** containing all of them is returned.
   **
   ** @param  range              the string containing the range description.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  length             the full length of the requested resource.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the requested range, or null if the range value
   **                            is invalid.
   **                            <br>
   **                            Possible object is array of <code>long</code>.
   */
  @SuppressWarnings("oracle.jdeveloper.java.null-array-return")
  public static long[] parseRange(final String range, final long length) {
    long min = Long.MAX_VALUE;
    long max = Long.MIN_VALUE;
    try {
      for (String token : splitElements(range, false)) {
        long start, end;
        int  dash = token.indexOf('-');
        // suffix range
        if (dash == 0) {
          start = length - parseUnsignedLong(token.substring(1), 10);
          end = length - 1;
        }
        // open range
        else if (dash == token.length() - 1) {
          start = parseUnsignedLong(token.substring(0, dash), 10);
          end   = length - 1;
        }
        // explicit range
        else {
          start = parseUnsignedLong(token.substring(0, dash),  10);
          end   = parseUnsignedLong(token.substring(dash + 1), 10);
        }
        if (end < start)
          throw new RuntimeException();
        if (start < min)
          min = start;
        if (end > max)
          max = end;
      }
      // no tokens
      if (max < 0)
        throw new RuntimeException();
      if (max >= length && min < length)
        max = length - 1;

      // start might be >= length!
      return new long[]{min, max};
    }
    // NFE, IOOBE or explicit RE
    catch (RuntimeException e) {
      // RFC2616#14.35.1 - ignore header if invalid
      return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseUnsignedLong
  /**
   ** Parses an unsigned long value.
   ** <br>
   ** This method behaves the same as calling
   ** {@link Long#parseLong(String, int)}, but considers the string invalid if
   ** it starts with an ASCII minus sign ('-') or plus sign ('+').
   **
   ** @param  expression         the string representation containing the long
   **                            representation to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  radix              the radix to be used while parsing
   **                            <code>expression</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the long represented by s in the specified
   **                            radix.
   **                            <br>
   **                            Possible object is <code>long</code>.
   **
   ** @throws NumberFormatException if the string does not contain a parsable
   **                               long, or if it starts with an ASCII minus
   **                               sign or plus sign
   */
  public static long parseUnsignedLong(final String expression, final int radix)
    throws NumberFormatException {

    // throws NumberFormatException
    long value = Long.parseLong(expression, radix);
    if (expression.charAt(0) == '-' || expression.charAt(0) == '+')
      throw new NumberFormatException("Invalid digit: " + expression.charAt(0));

    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseParameter
  /**
   ** Parses name-value pair parameters from the given "x-www-form-urlencoded"
   ** MIME-type string.
   ** <br>
   ** This is the encoding used both for parameters passed as the query of an
   ** HTTP GET method, and as the content of HTML forms submitted using the
   ** HTTP POST method (as long as they use the default
   ** <i>application/x-www-form-urlencoded</i> encoding in their ENCTYPE
   ** attribute).
   ** <br>
   ** UTF-8 encoding is assumed.
   ** <p>
   ** The parameters are returned as a list of string arrays, each containing
   ** the parameter name as the first element and its corresponding value as the
   ** second element (or an empty string if there is no value).
   ** <p>
   ** The list retains the original order of the parameters.
   **
   ** @param  source             an "application/x-www-form-urlencoded" string.
   **
   ** @return                    the parameter name-value pairs parsed from the
   **                            given string, or an empty list if there are
   **                            none.
   */
  public static List<String[]> parseParameter(final String source) {
    // prevent bogus input
    if (source == null || source.length() == 0)
      return Collections.emptyList();

    final List<String[]> collector = new ArrayList<String[]>(8);
    for (String pair : split(source, "&", -1)) {
      final int    pos   = pair.indexOf('=');
      final String name  = pos < 0 ? pair : pair.substring(0, pos);
      final String value = pos < 0 ? ""   : pair.substring(pos + 1);
      try {
        if (name.length() > 0)
          collector.add(
            new String[]{
              URLDecoder.decode(name.trim(),  "UTF-8")
            , URLDecoder.decode(value.trim(), "UTF-8")
            }
          );
      }
      catch (UnsupportedEncodingException ignore) {
        // intentionally left blank
        ;
      } // never thrown
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMap
  /**
   ** Converts a collection of pairs of objects (arrays of size two, each
   ** representing a key and corresponding value) into a {@link Map}.
   ** <br>
   ** Duplicate keys are ignored (only the first occurrence of each key is
   ** considered).
   ** <br>
   ** The map retains the original collection's iteration order.
   **
   ** @param  <K>                the key type.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the value type.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  pairs              a collection of arrays, each containing a key
   **                            and corresponding value.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is an array of {@link Object}.
   **
   ** @return                    a map containing the paired keys and values,
   **                            or an empty map.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <K, V> Map<K, V> toMap(final Collection<? extends Object[]> pairs) {
    if (pairs == null || pairs.isEmpty())
      return Collections.emptyMap();

    final Map<K, V> map = new LinkedHashMap<K, V>(pairs.size());
    for (Object[] pair : pairs)
      if (!map.containsKey(pair[0]))
        map.put((K)pair[0], (V)pair[1]);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIndex
  /**
   ** Serves the contents of a directory as an HTML file index.
   **
   ** @param  dir                the existing and readable directory whose
   **                            contents are served.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  path               the displayed base path corresponding to dir.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an HTML string containing the file index for
   **                            the directory.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String createIndex(final File dir, String path) {
    if (!path.endsWith("/"))
      path += "/";

    // calculate name column width
    int w = 21; // minimum width
    for (String name : dir.list())
      if (name.length() > w)
        w = name.length();
    w += 2; // with room for added slash and space
    // note: we use apache's format, for consistent user experience
    Formatter f = new Formatter(Locale.US);
    f.format("<!DOCTYPE html>%n" + "<html><head><title>Index of %s</title></head>%n" + "<body><h1>Index of %s</h1>%n" + "<pre> Name%" + (w - 5) + "s Last modified      Size<hr>", path, path, "");
    if (path.length() > 1) // add parent link if not root path
      f.format(" <a href=\"%s/\">Parent Directory</a>%" + (w + 5) + "s-%n", parentPath(path), "");
    for (File file : dir.listFiles()) {
      try {
        String name = file.getName() + (file.isDirectory() ? "/" : "");
        String size = file.isDirectory() ? "- " : approximateSize(file.length());
        // properly url-encode the link
        String link = new URI(null, path + name, null).toASCIIString();
        if (!file.isHidden() && !name.startsWith("."))
          f.format(" <a href=\"%s\">%s</a>%-" + (w - name.length()) + "s&#8206;%td-%<tb-%<tY %<tR%6s%n", link, name, "", file.lastModified(), size);
      }
      catch (URISyntaxException ignore) {
        // intentionally left blank
        ;
      }
    }
    f.format("</pre></body></html>");
    return f.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Matches the given ETag value against the given ETags.
   ** <br>
   ** A match is found if the given ETag is not <code>null</code>, and either
   ** the ETags contain a <code>*</code> value, or one of them is identical to
   ** the given ETag. If strong comparison is used, tags beginning with the weak
   ** ETag prefix "W/" never match.
   ** <br>
   ** See RFC2616#3.11, RFC2616#13.3.3.
   **
   ** @param  strong             if <code>true</code>, strong comparison is
   **                            used, otherwise weak comparison is used.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  etags              the ETags to match against.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  etag               the ETag to match.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the ETag is matched;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean match(final boolean strong, final String[] etags, final String etag) {
    // prevent bogus input
    if (etag == null || strong && etag.startsWith("W/"))
      return false;

    for (String e : etags)
      if (e.equals("*") || (e.equals(etag) && !(strong && (e.startsWith("W/")))))
        return true;

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   approximateSize
  /**
   ** Returns a human-friendly string approximating the given data size, e.g.
   ** "316", "1.8K", "324M", etc.
   **
   ** @param  size               the size to display.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    a human-friendly string approximating the given
   **                            data size
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @SuppressWarnings("oracle.jdeveloper.java.empty-statement")
  public static String approximateSize(final long size) {
    final char[] units = { ' ', 'K', 'M', 'G', 'T', 'P', 'E' };
    int          u;
    double       s;
    for (u = 0, s = size; s >= 1000; u++, s /= 1024)
      // intentionally left blank
      ;
    return String.format(s < 10 ? "%.1f%c" : "%.0f%c", s, units[u]);
  }
}