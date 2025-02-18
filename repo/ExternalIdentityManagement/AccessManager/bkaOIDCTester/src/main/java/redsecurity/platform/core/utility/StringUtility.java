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

    File        :   StringUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.stream.Collectors;

import java.text.Normalizer;

import java.nio.charset.StandardCharsets;

import redsecurity.platform.core.SystemProperty;
import redsecurity.platform.core.SystemRuntimeException;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous string utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class StringUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final char       BLANK               = ' ';
  public static final String     EMPTY               = "";
  public static final String     NULL                = "[null]";

  /**
   ** The name of a system property that can be used to explicitly specify the
   ** Unicode normalization type that will be used when comparing two strings in
   ** a Unicode-aware manner.
   */
  private static final String    NORMALIZER_PROPERTY = "redsecurity.platform.normalizerForm";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////
  private static Normalizer.Form NORMALIZER_FORM     = Normalizer.Form.NFC;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    final String value = SystemProperty.stringValue(NORMALIZER_PROPERTY);
    if ((value == null) || value.equalsIgnoreCase("NFC")) {
      NORMALIZER_FORM = Normalizer.Form.NFC;
    }
    else if (value.equalsIgnoreCase("NFD")) {
      NORMALIZER_FORM = Normalizer.Form.NFD;
    }
    else if (value.equalsIgnoreCase("NFKC")) {
      NORMALIZER_FORM = Normalizer.Form.NFKC;
    }
    else if (value.equalsIgnoreCase("NFKD")) {
      NORMALIZER_FORM = Normalizer.Form.NFKD;
    }
    else {
      throw SystemRuntimeException.argumentVakue(NORMALIZER_PROPERTY, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>StringUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new StringUtility()" and enforces use of the public method below.
   */
  private StringUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytes
  /**
   ** Returns a UTF-8 byte representation of the provided string.
   **
   ** @param  value              the string for which to retrieve the UTF-8 byte
   **                            representation.
   **
   ** @return                    the UTF-8 byte representation for the provided
   **                            string.
   */
  public static byte[] bytes(final String value) {
    if (empty(value))
      return CollectionUtility.EMPTY_BYTES;

    final int    l = value.length();
    final byte[] b = new byte[l];
    for (int i = 0; i < l; i++) {
      final char c = value.charAt(i);
      if (c <= 0x7F) {
        b[i] = (byte)(c & 0x7F);
      }
      else {
        return value.getBytes(StandardCharsets.UTF_8);
      }
    }
    return b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicode
  /**
   ** Returns a string generated from the provided byte array using the UTF-8
   ** encoding.
   **
   ** @param  value              the byte array for which to return the
   **                            associated string.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the string generated from the provided byte
   **                            array using the UTF-8 encoding.
   */
  public static String unicode(final byte[] value) {
    return unicode(value, 0, value.length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicode
  /**
   ** Returns a string generated from the specified portion of the provided byte
   ** array using the UTF-8 encoding.
   **
   ** @param  value              the byte array for which to return the
   **                            associated string.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  offset             the offset in the array at which the value
   **                            begins.
   ** @param  length             the number of bytes in the value to convert to
   **                            a string.
   **
   ** @return                    the string generated from the specified portion
   **                            of the provided byte array using the UTF-8
   **                            encoding.
   **
   ** @throws IndexOutOfBoundsException if <code>offset</code> and
   **                                   <code>length</code> index characters
   **                                   outside the bounds of <code>value</code>.
   */
  public static String unicode(final byte[] value, final int offset, final int length) {
    try {
      return new String(value, offset, length, StandardCharsets.UTF_8);
    }
    catch (final Exception e) {
      // this should never happen.
      return new String(value, offset, length);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeHexadecimal
  /**
   ** Decode a 2-digit hex byte from within a string.
   **
   ** @param  s                  the string to decoded the 2-digit hex byte
   **                            from.
   ** @param  pos                the start position in <code>s</code> to decode.
   **
   ** @return                    the 2-digit hex byte from <code>s</code>
   **                            starting at <code>pos</code>.
   */
  public static byte decodeHexadecimal(final CharSequence s, final int pos) {
    int hi = CharacterUtility.decodeHexadecimal(s.charAt(pos));
    int lo = CharacterUtility.decodeHexadecimal(s.charAt(pos + 1));
    if (hi == -1 || lo == -1) 
      throw new IllegalArgumentException(String.format("Invalid hex byte '%s' at index %d of '%s'", s.subSequence(pos, pos + 2), pos, s));

    return (byte)((hi << 4) + lo);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upperCase
  /**
   ** Converts all of the characters in <code>word</code> to upper case using
   ** the rules of the default locale. This method is equivalent to
   ** <pre>
   **   StringUtility.upperCase(word, Locale.getDefault())}.
   ** </pre>
   ** <b>Note:</b>
   ** <br>
   ** This method is locale sensitive, and may produce unexpected results if
   ** used for strings that are intended to be interpreted locale independently.
   ** Examples are programming language identifiers, protocol keys, and HTML
   ** tags.
   ** <br>
   ** For instance, <code>upperCase("title")</code> in a Turkish locale returns
   ** <code>"T\u005Cu0130TLE"</code>, where '\u005Cu0130' is the LATIN CAPITAL
   ** LETTER I WITH DOT ABOVE character.
   ** <br>
   ** To obtain correct results for locale insensitive strings, use
   ** {@link #upperCase(String, Locale)}.
   ** <br>
   ** A <code>null</code> input {@link String} returns <code>null</code>.
   **
   ** @param  word               the string to convert to upper case.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    the {@link String}, converted to upper case or
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   **
   ** @see    #upperCase(String, Locale)
   ** @see    #lowerCase(String)
   ** @see    #lowerCase(String, Locale)
   */
  public static String upperCase(final String word) {
    return empty(word) ? word : word.toUpperCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upperCase
  /**
   ** Converts all of the characters in <code>word</code> to upper case using
   ** the rules of the given {@link Locale}. Case mapping is based on the
   ** Unicode Standard version specified by the
   ** {@link java.lang.Character Character} class. Since case mappings are not
   ** always 1:1 char mappings, the resulting {@link String} may be a different
   ** length than the original {@link String}.
   ** <p>
   ** Examples of locale-sensitive and 1:M case mappings are in the following
   ** table.
   ** <table border="1" summary="Examples of locale-sensitive and 1:M case mappings. Shows Language code of locale, lower case, upper case, and description.">
   ** <tr>
   **   <th>Language Code of Locale</th>
   **   <th>Lower Case</th>
   **   <th>Upper Case</th>
   **   <th>Description</th>
   ** </tr>
   ** <tr>
   **   <td>tr (Turkish)</td>
   **   <td>&#92;u0069</td>
   **   <td>&#92;u0130</td>
   **   <td>small letter i -&gt; capital letter I with dot above</td>
   ** </tr>
   ** <tr>
   **   <td>tr (Turkish)</td>
   **   <td>&#92;u0131</td>
   **   <td>&#92;u0049</td>
   **   <td>small letter dotless i -&gt; capital letter I</td>
   ** </tr>
   ** <tr>
   **   <td>(all)</td>
   **   <td>&#92;u00df</td>
   **   <td>&#92;u0053 &#92;u0053</td>
   **   <td>small letter sharp s -&gt; two letters: SS</td>
   ** </tr>
   ** <tr>
   **   <td>(all)</td>
   **   <td>Fahrvergn&uuml;gen</td>
   **   <td>FAHRVERGN&Uuml;GEN</td>
   **   <td></td>
   ** </tr>
   ** </table>
   **
   ** @param  word               the string to convert to upper case.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  locale             the {@link Locale} used by transformation
   **                            rules for this locale.
   **
   ** @return                    the {@link String}, converted to upper case or
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   **
   ** @see    #upperCase(String)
   ** @see    #lowerCase(String)
   ** @see    #lowerCase(String, Locale)
   */
  public static String upperCase(final String word, final Locale locale) {
    return empty(word) ? word : word.toUpperCase(locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lowerCase
  /**
   ** Converts all of the characters in <code>word</code> to lower case using
   ** the rules of the default locale. This method is equivalent to
   ** <pre>
   **   StringUtility.lowerCase(word, Locale.getDefault())}.
   ** </pre>
   ** <b>Note:</b>
   ** <br>
   ** This method is locale sensitive, and may produce unexpected results if
   ** used for strings that are intended to be interpreted locale independently.
   ** Examples are programming language identifiers, protocol keys, and HTML
   ** tags.
   ** <br>
   ** For instance, <code>upperCase("title")</code> in a Turkish locale returns
   ** <code>"t\u005Cu0131tle"</code>, where '\u005Cu0131' is the LATIN SMALL
   ** LETTER DOTLESS I character.
   ** <br>
   ** To obtain correct results for locale insensitive strings, use
   ** {@link #lowerCase(String, Locale)}.
   ** <br>
   ** A <code>null</code> input {@link String} returns <code>null</code>.
   **
   ** @param  word               the string to convert to lower case.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    the {@link String}, converted to lower case or
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   **                            <code>null</code>.
   **
   ** @see    #lowerCase(String, Locale)
   ** @see    #upperCase(String)
   ** @see    #upperCase(String, Locale)
   */
  public static String lowerCase(final String word) {
    return empty(word) ? word : word.toLowerCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lowerCase
  /**
   ** Converts all of the characters in <code>word</code> to lower case using
   ** the rules of the given {@link Locale}. Case mapping is based on the
   ** Unicode Standard version specified by the
   ** {@link java.lang.Character Character} class. Since case mappings are not
   ** always 1:1 char mappings, the resulting {@link String} may be a different
   ** length than the original {@link String}.
   ** <p>
   ** Examples of locale-sensitive and 1:M case mappings are in the following
   ** table.
   ** <table border="1" summary="Examples of locale-sensitive and 1:M case mappings. Shows Language code of locale, lower case, upper case, and description.">
   ** <tr>
   **   <th>Language Code of Locale</th>
   **   <th>Lower Case</th>
   **   <th>Upper Case</th>
   **   <th>Description</th>
   ** </tr>
   ** <tr>
   **   <td>tr (Turkish)</td>
   **   <td>&#92;u0130</td>
   **   <td>&#92;u0069</td>
   **   <td>capital letter I with dot above -&gt; small letter i</td>
   ** </tr>
   ** <tr>
   **   <td>tr (Turkish)</td>
   **   <td>&#92;u0049</td>
   **   <td>&#92;u0131</td>
   **   <td>capital letter I -&gt; small letter dotless i</td>
   ** </tr>
   ** <tr>
   **   <td>(all)</td>
   **   <td>French Fries</td>
   **   <td>french fries</td>
   **   <td>lowercased all chars in String</td>
   ** </tr>
   ** </table>
   **
   ** @param  word               the string to convert to upper case.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  locale             the {@link Locale} used by transformation
   **                            rules for this locale.
   **
   ** @return                    the {@link String}, converted to upper case or
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   ** @see    #lowerCase(String)
   ** @see    #upperCase(String)
   ** @see    #upperCase(String, Locale)
   */
  public static String lowerCase(final String word, final Locale locale) {
    return empty(word) ? word : word.toLowerCase(locale);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalize
  /**
   ** Capitalizes a word represented by a {@link String}.
   ** <br>
   ** The first letter is changed to uppercase all following characters are
   ** converted to lowercase.
   ** <br>
   ** A <code>null</code> input {@link String} returns <code>null</code>.
   **
   ** @param  word               the string to capitalize.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    the capitalized {@link String},
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   */
  public static String capitalize(final String word) {
    return empty(word) ? word : word.substring(0, 1).toUpperCase(Locale.ENGLISH) + word.substring(1).toLowerCase(Locale.ENGLISH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalize
  /**
   ** Capitalizes the words represented by a {@link String}.
   ** <br>
   ** The first letter is changed to uppercase and optionally the first
   ** character of each word in the string.
   ** <br>
   ** A <code>null</code> input {@link String} returns <code>null</code>.
   **
   ** @param  sentence           the string to capitalize.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  all                whether to capitalize all words in the string,
   **                            or only the first word.
   **
   ** @return                    the capitalized version of the specified
   **                            {@link String}, <code>null</code> if
   **                            <code>word</code> is <code>null</code>.
   */
  public static String capitalize(final String sentence, final boolean all) {
    // prevent bogus input
    if (empty(sentence))
      return null;
    switch (sentence.length()) {
      case 0  : return sentence;
      case 1  : return sentence.toUpperCase();
      default : boolean             capitalize = true;
                final char[]        chars      = sentence.toCharArray();
                final StringBuilder buffer     = new StringBuilder(chars.length);
                for (final char c : chars) {
                  // whitespace and punctuation will be considered word breaks
                  if (Character.isWhitespace(c) || (((c >= '!') && (c <= '.')) || ((c >= ':') && (c <= '@')) || ((c >= '[') && (c <= '`')) || ((c >= '{') && (c <= '~')))) {
                    buffer.append(c);
                    capitalize |= all;
                  }
                  else if (capitalize) {
                    buffer.append(Character.toUpperCase(c));
                    capitalize = false;
                  }
                  else {
                    buffer.append(c);
                  }
                }
                return buffer.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blank
  /**
   ** Check to ensure that a {@code String} is not <code>blank</code> after
   ** trimming of leading and trailing whitespace. Usually used with assertions,
   ** as in
   ** <pre>
   **   assert StringUtility.isBlank(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   ** <pre>
   **  StringUtility.blank(null)                = true
   **  StringUtility.blank(&quot;&quot;)        = true
   **  StringUtility.blank(&quot; &quot;)       = true
   **  StringUtility.blank(&quot;bob&quot;)     = false
   **  StringUtility.blank(&quot;  bob  &quot;) = false
   ** </pre>
   **
   ** @param  subject            the string to be tested for blank.
   **
   ** @return                    <code>true</code> if the subject is just
   **                            blank.
   */
  public static boolean blank(final String subject) {
    return (subject == null) ? true : empty(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Check to ensure that a {@code CharSequence} is not <code>null</code> or
   ** empty.
   ** <br>
   ** Usually used with assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            the character sequence to be tested for
   **                            emptiness.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   */
  public static boolean empty(final CharSequence subject) {
    return (subject == null || subject.length() == 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Check to ensure that a {@code String} is not <code>null</code> or empty
   ** after trimming of leading and trailing whitespace. Usually used with
   ** assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            the string to examine.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   */
  public static boolean empty(final String subject) {
    return empty(subject, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Check to ensure that a {@code String} is not <code>null</code> or empty
   ** (after optional trimming of leading and trailing whitespace). Usually used
   ** with assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher, true) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            the string to examine.
   ** @param  trim               if <code>true</code>, the string is first
   **                            trimmed before checking to see if it is empty,
   **                            otherwise it is not.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   */
  public static boolean empty(final String subject, final boolean trim) {
    return (trim) ? (subject == null || subject.trim().length() == 0) : (subject == null || subject.length() == 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Ensure the string contains only printable ascii characters.
   **
   ** @param  subject            the string to examine.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            printable ascii characters.
   */
  public static boolean printable(final String subject) {
    if (subject == null)
      return false;

    final int size = subject.length();
    for (int i = 0; i < size; i++) {
      if (!CharacterUtility.printable(subject.charAt(i)))
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printableRFC4517
  /**
   ** Returns whether the contents of the provided byte array represent a
   ** printable string as per RFC 4517 section 3.2.
   ** <br>
   ** The only characters allowed in a printable string are:
   ** <ul>
   **   <li>All uppercase and lowercase ASCII alphabetic letters
   **   <li>All ASCII numeric digits
   **   <li>The following additional ASCII characters:
   **       <ul>
   **         <li>single quote
   **         <li>left parenthesis
   **         <li>right parenthesis
   **         <li>plus
   **         <li>comma
   **         <li>hyphen
   **         <li>period
   **         <li>equals
   **         <li>forward slash
   **         <li>colon
   **         <li>question mark
   **         <li>space
   **       </ul>
   ** </ul>
   ** If the provided array contains anything other than the above characters
   ** (i.e., if the byte array contains any non-ASCII characters, or any ASCII
   ** control characters, or if it contains excluded ASCII characters like
   ** the exclamation point, double quote, octothorpe, dollar sign, etc.), then
   ** it will not be considered printable.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the contents of the
   **                            provided byte array represent a printable
   **                            string; <code>false</code> otherwise.
   */
  public static boolean printableRFC4517(final byte[] value) {
    for (final byte b : value) {
      if (!CharacterUtility.printableRFC4517(b))
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayable
  /**
   ** Returns whether the specified byte array <code>value</code> represents a
   ** valid UTF-8 string that is comprised entirely of characters that are
   ** believed to be displayable (as determined by the
   ** {@link CharacterUtility#displayable(int)} method).
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the specified byte array
   **                            represents a valid UTF-8 string that is
   **                            believed to be displayable;
   **                            <code>false</code> otherwise.
   */
  public static boolean displayable(final byte[] value) {
    return (!CharacterUtility.unicode(value)) ? false : displayable(StringUtility.unicode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayable
  /**
   ** Returns whether the specified string <code>value</code> is comprised
   ** entirely of characters that are believed to be displayable (as determined
   ** by the {@link CharacterUtility#displayable(int)} method).
   **
   ** @param  value              the string to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the specified string is
   **                            believed to be displayable;
   **                            <code>false</code> otherwise.
   */
  public static boolean displayable(final String value) {
    int pos = 0;
    while (pos < value.length()) {
      final int cp = value.codePointAt(pos);
      if (!CharacterUtility.displayable(cp)) {
        return false;
      }
      pos += Character.charCount(cp);
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unequal
  /**
   ** Compares two strings if they are <b>not</b> lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the strings are <b>not</b>
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean unequal(final String lhs, final String rhs) {
    return !equal(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equal(final String lhs, final String rhs) {
    return Objects.equals(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two character sequences if they are lexicographically
   ** identically.
   **
   ** @param  lhs                the character sequences to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the character sequences to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the character sequences
   **                            are lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equal(final CharSequence lhs, final CharSequence rhs) {
    if (lhs == rhs)
      return true;
    if (lhs == null || rhs == null)
      return false;

    if (lhs.length() != rhs.length())
      return false;

    for (int i = 0; i < lhs.length(); i++) {
      if (lhs.charAt(i) != rhs.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   similar
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean similar(final String lhs, final String rhs) {
    if (Objects.equals(lhs, rhs))
      return true;

    final String lhn = Normalizer.normalize(lhs, NORMALIZER_FORM);
    final String rhn = Normalizer.normalize(rhs, NORMALIZER_FORM);
    return lhn.equals(rhn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalIgnoreCase
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by the left hand side
   ** (<code>lhs</code>) argument string is considered equal ignoring case to
   ** the character sequence represented by the right hand side
   ** (<code>rhs</code>) argument string if both are of the same length and
   ** corresponding characters in the two strings are equal ignoring case.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code>, if <code>rhs</code>
   **                            represents an equivalent <code>String</code>
   **                            ignoring case as <code>lhs</code>;
   **                            <code>false</code> otherwise.
   */
  public static boolean equalIgnoreCase(final String lhs, final String rhs) {
    return lhs == null ? rhs == null : lhs.equalsIgnoreCase(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalIgnoreCase
  /**
   ** Compares two character sequences if they are lexicographically
   ** identically.
   **
   ** @param  lhs                the character sequences to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the character sequences to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the character sequences
   **                            are lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equalIgnoreCase(final CharSequence lhs, final CharSequence rhs) {
    if (lhs == rhs)
      return true;
    if (lhs == null || rhs == null)
      return false;

    if (lhs.length() != rhs.length())
      return false;

    for (int i = 0; i < lhs.length(); i++) {
      if (!CharacterUtility.equalIgnoreCase(lhs.charAt(i), rhs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Determines if the string parameter <code>base</code> starts with the
   ** character value.
   **
   ** @param  base               the string to check for the character at the
   **                            start.
   ** @param  value              the character to look for at the start of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found at the start of the string parameter
   **                            otherwise <code>false</code>.
   */
  public static boolean startsWith(final String base, final char value) {
    return blank(base) ? false : base.charAt(0) == value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Helper functions to query a strings start portion.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   ** @param  prefix             the starting text.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            the given prefix text.
   */
  public static boolean startsWith(final String base, final String prefix) {
    return (base == null || prefix == null) ? (base == null && prefix == null) : (base.length() < prefix.length()) ? false : base.startsWith(prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Returns <code>true</code> if the given string starts with one of the
   ** given prefixes.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   ** @param  prefix             the collection of starting text to be checked.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            one of the given prefixes; otherwise
   **                            <code>false</code>.
   */
  public static boolean startsWith(final String base, final String... prefix) {
    if (base == null || prefix == null) {
      return (base == null && prefix == null);
    }
    for (String cursor : prefix) {
      if (base.startsWith(cursor))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Determines if the string parameter <code>base</code> contains the
   ** character value.
   **
   ** @param  base               the string to check for the character at the
   **                            end.
   ** @param  value              the character to look for at the end of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found somewhere in the string parameter
   **                            otherwise <code>false</code>.
   */
  public static boolean contains(final String base, final char value) {
    return blank(base) ? false : base.indexOf(value) >= 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Helper functions to query a strings contains portion.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   ** @param  contain            the containing text.
   **
   ** @return                    <code>true</code>, if the string contains
   **                            the given text.
   */
  public static boolean contains(final String base, final String contain) {
    if (base == null || contain == null) {
      return (base == null && contain == null);
    }
    if (base.length() < contain.length()) {
      return false;
    }
    return base.contains(contain);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsIgnoreCase
  /**
   ** Helper functions to query a strings contains portion.
   ** <br>
   ** The comparison is case insensitive.
   **
   ** @param  base               the base string.
   ** @param  contain            the containing text.
   **
   ** @return                    <code>true</code>, if the string contains
   **                            the given text.
   */
  public static boolean containsIgnoreCase(final String base, final String contain) {
    if (base == null || contain == null) {
      return (base == null && contain == null);
    }
    if (base.length() < contain.length()) {
      return false;
    }
    return base.toLowerCase().contains(contain.toLowerCase());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by <code>a</code> is compared
   ** lexicographically to the character sequence represented by <code>b</code>.
   ** The result is a negative integer if <code>a</code> lexicographically
   ** precedes <code>b</code>. The result is a positive integer if
   ** <code>a</code> lexicographically follows <code>b</code>. The result is
   ** zero if the strings are equal; <code>compare</code> returns <code>0</code>
   ** exactly when the {@link #equal(String, String)} method would return
   ** <code>true</code>.
   ** <p>
   ** This is the definition of lexicographic ordering.
   ** <br>
   ** If two strings are different, then either they have different characters
   ** at some index that is a valid index for both strings, or their lengths are
   ** different, or both. If they have different characters at one or more index
   ** positions, let <i>k</i> be the smallest such index; then the string whose
   ** character at position <i>k</i> has the smaller value, as determined by
   ** using the &lt; operator, lexicographically precedes the other string. In
   ** this case, <code>compare</code> returns the difference of the two
   ** character values at position <code>k</code> in the two string -- that is,
   ** the value:
   ** <blockquote>
   **   <pre>
   **     this.charAt(k)-anotherString.charAt(k)
   **   </pre>
   ** </blockquote>
   ** If there is no index position at which they differ, then the shorter
   ** string lexicographically precedes the longer string. In this case,
   ** <code>compare</code> returns the difference of the lengths of the strings
   ** -- that is, the value:
   ** <blockquote>
   **   <pre>
   **     this.length()-anotherString.length()
   **   </pre>
   ** </blockquote>
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>a</code> is lexicographically greater
   **                            than the <code>rhs</code>.
   */
  public static int compare(final String lhs, final String rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null && rhs == null)
      return 0;
    else if (lhs == null)
      return -1;
    else if (rhs == null)
      return 1;
    else
      return lhs.compareTo(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two objects lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** objects. The character sequence represented by <code>a</code> is compared
   ** lexicographically to the character sequence represented by <code>b</code>.
   ** The result is a negative integer if <code>a</code> lexicographically
   ** precedes <code>b</code>. The result is a positive integer if
   ** <code>a</code> lexicographically follows <code>b</code>. The result is
   ** zero if the strings are equal; <code>compare</code> returns <code>0</code>
   ** exactly when the {@link #equal(String, String)} method would return
   ** <code>true</code>.
   ** <p>
   ** This is the definition of lexicographic ordering.
   ** <br>
   ** If two strings are different, then either they have different characters
   ** at some index that is a valid index for both strings, or their lengths are
   ** different, or both. If they have different characters at one or more index
   ** positions, let <i>k</i> be the smallest such index; then the string whose
   ** character at position <i>k</i> has the smaller value, as determined by
   ** using the &lt; operator, lexicographically precedes the other string. In
   ** this case, <code>compare</code> returns the difference of the two
   ** character values at position <code>k</code> in the two string -- that is,
   ** the value:
   ** <blockquote>
   **   <pre>
   **     this.charAt(k)-anotherString.charAt(k)
   **   </pre>
   ** </blockquote>
   ** If there is no index position at which they differ, then the shorter
   ** string lexicographically precedes the longer string. In this case,
   ** <code>compare</code> returns the difference of the lengths of the strings
   ** -- that is, the value:
   ** <blockquote>
   **   <pre>
   **     this.length()-anotherString.length()
   **   </pre>
   ** </blockquote>
   **
   ** @param  lhs                the <code>Object</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>Object</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than the <code>rhs</code>.
   */
  public static int compare(final Object lhs, final Object rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null && rhs == null)
      return 0;
    else if (lhs == null)
      return -1;
    else if (rhs == null)
      return 1;
    else
      return lhs.toString().compareTo(rhs.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareIgnoreCase
  /**
   ** Compares two strings lexicographically, ignoring case differences.
   ** <br>
   ** This method returns an integer whose sign is that of calling
   ** {@link #compare(String, String)} with normalized versions of the strings
   ** where case differences have been eliminated by calling
   ** <code>Character.toLowerCase(Character.toUpperCase(character))</code> on
   ** each character.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This method does <em>not</em> take locale into account, and will result in
   ** an unsatisfactory ordering for certain locales. The java.text package
   ** provides <em>collators</em> to allow locale-sensitive ordering.
   ** <p>
   ** This is the definition of lexicographic ordering.
   ** <br>
   ** If two strings are different, then either they have different characters
   ** at some index that is a valid index for both strings, or their lengths are
   ** different, or both. If they have different characters at one or more index
   ** positions, let <i>k</i> be the smallest such index; then the string whose
   ** character at position <i>k</i> has the smaller value, as determined by
   ** using the &lt; operator, lexicographically precedes the other string. In
   ** this case, <code>compare</code> returns the difference of the two
   ** character values at position <code>k</code> in the two string -- that is,
   ** the value:
   ** <blockquote>
   **   <pre>
   **     this.charAt(k)-anotherString.charAt(k)
   **   </pre>
   ** </blockquote>
   ** If there is no index position at which they differ, then the shorter
   ** string lexicographically precedes the longer string. In this case,
   ** <code>compare</code> returns the difference of the lengths of the strings
   ** -- that is, the value:
   ** <blockquote>
   **   <pre>
   **     this.length()-anotherString.length()
   **   </pre>
   ** </blockquote>
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>, ignoring case
   **                            considerations.
   */
  public static int compareIgnoreCase(final String lhs, final String rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null && rhs == null)
      return 0;
    else if (lhs == null)
      return -1;
    else if (rhs == null)
      return 1;
    else
      return lhs.compareToIgnoreCase(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareIgnoreCase
  /**
   ** Compares two objects lexicographically, ignoring case differences.
   ** <br>
   ** This method returns an integer whose sign is that of calling
   ** {@link #compare(String, String)} with normalized versions of the strings
   ** where case differences have been eliminated by calling
   ** <code>Character.toLowerCase(Character.toUpperCase(character))</code> on
   ** each character.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This method does <em>not</em> take locale into account, and will result in
   ** an unsatisfactory ordering for certain locales. The java.text package
   ** provides <em>collators</em> to allow locale-sensitive ordering.
   ** <p>
   ** This is the definition of lexicographic ordering.
   ** <br>
   ** If two strings are different, then either they have different characters
   ** at some index that is a valid index for both strings, or their lengths are
   ** different, or both. If they have different characters at one or more index
   ** positions, let <i>k</i> be the smallest such index; then the string whose
   ** character at position <i>k</i> has the smaller value, as determined by
   ** using the &lt; operator, lexicographically precedes the other string. In
   ** this case, <code>compare</code> returns the difference of the two
   ** character values at position <code>k</code> in the two string -- that is,
   ** the value:
   ** <blockquote>
   **   <pre>
   **     this.charAt(k)-anotherString.charAt(k)
   **   </pre>
   ** </blockquote>
   ** If there is no index position at which they differ, then the shorter
   ** string lexicographically precedes the longer string. In this case,
   ** <code>compare</code> returns the difference of the lengths of the strings
   ** -- that is, the value:
   ** <blockquote>
   **   <pre>
   **     this.length()-anotherString.length()
   **   </pre>
   ** </blockquote>
   **
   ** @param  lhs                the <code>Object</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>Object</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>, ignoring case
   **                            considerations.
   */
  public static int compareIgnoreCase(final Object lhs, final Object rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null && rhs == null)
      return 0;
    else if (lhs == null)
      return -1;
    else if (rhs == null)
      return 1;
    else
      return lhs.toString().compareToIgnoreCase(rhs.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWithIgnoreCase
  /**
   ** Helper functions to query a strings start portion.
   ** <br>
   ** The comparison is case insensitive.
   **
   ** @param  base               the base string.
   ** @param  prefix             the starting text.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            the given prefix text.
   */
  public static boolean startsWithIgnoreCase(final String base, final String prefix) {
    if (base == null || prefix == null) {
      return (base == null && prefix == null);
    }
    if (base.length() < prefix.length()) {
      return false;
    }
    return base.regionMatches(true, 0, prefix, 0, prefix.length());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   ** Determines if the string parameter <code>base</code> ends with the
   ** character value.
   **
   ** @param  base               the string to check for the character at the
   **                            end.
   ** @param  value              the character to look for at the end of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found at the end of the string parameter
   **                            otherwise <code>false</code>.
   */
  public static boolean endsWith(final String base, final char value) {
    return blank(base) ? false : base.charAt(base.length() - 1) == value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   ** Helper functions to query a strings end portion.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   ** @param  suffix             the ending text.
   **
   ** @return                    <code>true</code>, if the string ends with the
   **                            given suffix text.
   */
  public static boolean endsWith(final String base, final String suffix) {
    return (base == null || suffix == null) ? (base == null && suffix == null) : (base.length() < suffix.length())  ? false : base.endsWith(suffix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   ** Returns <code>true</code> if the given string ends with one of the given
   ** suffixes.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   ** @param  suffix             the collection of suffixes to be checked.
   **
   ** @return                    <code>true</code> if the given string ends
   **                            with one of the given suffixes; otherwise
   **                            <code>false</code>.
   */
  public static boolean endsWith(final String base, final String... suffix) {
    if (base == null || suffix == null) {
      return (base == null && suffix == null);
    }
    for (String cursor : suffix) {
      if (base.endsWith(cursor))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWithIgnoreCase
  /**
   ** Helper functions to query a strings end portion.
   ** <br>
   ** The comparison is case insensitive.
   **
   ** @param  base               the base string.
   ** @param  suffix             the ending text.
   **
   ** @return                    <code>true</code>, if the string ends with the
   **                            given suffix text.
   */
  public static boolean endsWithIgnoreCase(final String base, final String suffix) {
    if (base == null || suffix == null) {
      return (base == null && suffix == null);
    }
    if (base.length() < suffix.length()) {
      return false;
    }
    return base.regionMatches(true, base.length() - suffix.length(), suffix, 0, suffix.length());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removePrefix
  /**
   ** Removes the leading <code>prefix</code> from the given string
   ** <code>subject</code>.
   **
   ** @param  subject            the string to truncate.
   ** @param  prefix             the string to truncate.
   **
   ** @return                    the string <code>subject</code> without
   **                            <code>prefix</code>.
   */
  public static String removePrefix(final String subject, final String prefix) {
    return subject.startsWith(prefix) ? subject.substring(prefix.length()) : subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeSuffix
  /**
   ** Removes the trailing <code>suffix</code> from the given string
   ** <code>subject</code>.
   **
   ** @param  subject            the string to truncate.
   ** @param  suffix             the string to truncate.
   **
   ** @return                    the string <code>subject</code> without
   **                            <code>suffix</code>.
   */
  public static String removeSuffix(final String subject, final String suffix) {
    return subject.endsWith(suffix) ? subject.substring(0, subject.length() - suffix.length()) : subject;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string concatenating the <code>element</code>s separated by
   ** the <code>separator</code> specified.
   **
   ** @param  element            the elements to concatenate.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    a string containing concatenated
   **                            <code>element</code>.
   */
  public static String join(final String... element) {
    return join(0, element.length, null, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string concatenating the <code>element</code>s separated by
   ** the <code>separator</code> specified.
   **
   ** @param  separator          the separator to place between consecutive
   **                            elements.
   ** @param  element            the elements to concatenate.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    a string containing concatenated
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   */
  public static String join(final Object separator, final String... element) {
    return join(0, element.length, separator, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string concatenating the <code>element</code>s separated by
   ** the <code>separator</code> specified.
   **
   ** @param  <T>                the type of the elements element.
   ** @param  start              the subscript of the start element.
   ** @param  length             the number of elements.
   ** @param  separator          the separator to place between consecutive
   **                            elements.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  element            the elements to join.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    a string containing concatenated
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> String join(final int start, final int length, final Object separator, final T... element) {
    final StringBuilder builder = new StringBuilder();
    if (element == null || element.length == 0 || length < 0)
      return builder.toString();

    if (start < element.length) {
      builder.append(element[start]);
      for (int i = 1; i < length && i + start < element.length; i++) {
        if (separator != null)
          builder.append(separator);
        builder.append(element[i + start]);
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Converts a {@link List} of strings to single string by inserting a space
   ** between each element of the list.
   **
   ** @param  list               the {@link List} of Strings to convert.
   **
   ** @return                    a string made up of the words in the list.
   */
  public static String join(final Collection<String> list) {
    return join(list, " ");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Converts a {@link List} of strings to single string by inserting  the
   ** specified <code>separator</code> between each element of the collection.
   **
   ** @param  collection         the {@link List} of Objects to convert.
   **                            element is of type {@link String}.
   ** @param  separator          the separator to insert between elements.
   **
   ** @return                    a String made up of the words in the list.
   */
  public static String join(final Collection<String> collection, final String separator) {
    return collection.stream().map(Object::toString).collect(Collectors.joining(separator));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Converts a {@link List} of strings to single string by inserting  the
   ** specified <code>separator</code> between each element of the collection.
   **
   ** @param  collection         the {@link List} of Objects to convert.
   ** @param  separator          the separator to insert between elements.
   ** @param  escape             the enclosing character for each element.
   **
   ** @return                    a String made up of the words in the list.
   */
  public static String join(final Collection<String> collection, final String separator, final String escape) {
    return collection.stream().map(Object::toString).collect(Collectors.joining(separator, escape, escape));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string containing the <code>prefix</code> and
   ** <code>suffix</code> separated by a <code>:</code> (colon) sign.
   **
   ** @param  prefix             the prefix of the string ro build.
   ** @param  suffix             the suffix of the string ro build.
   **
   ** @return                    a formatted string containing
   **                            <code>prefix</code> and <code>suffix</code>.
   */
  public static String join(final String prefix, final String suffix) {
    if (blank(prefix))
      return suffix;
    if (blank(suffix))
      return prefix;
    if (prefix.endsWith(suffix))
      return prefix;
    if (prefix.trim().endsWith(":") || prefix.trim().endsWith(";"))
      return prefix.trim() + " " + suffix;
    return prefix + ": " + suffix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string containing the <code>element</code>s separated by a
   ** <code>separator</code> sign.
   **
   ** @param  separator          the separator to put between consecutive
   **                            elements.
   ** @param  element            the collection of elements to concatenate.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   */
  public static StringBuilder join(final Object separator, final Collection<String> element) {
    return (element == null || element.size() == 0) ? new StringBuilder() : join(separator, element.iterator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   join
  /**
   ** Builds a new string containing the <code>element</code>s separated by a
   ** <code>separator</code> sign.
   **
   ** @param  separator          the separator to put between consecutive
   **                            elements.
   ** @param  element            the elements to join.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   */
  public static StringBuilder join(final Object separator, final Iterator<String> element) {
    final StringBuilder builder = new StringBuilder();
    if (element == null || !element.hasNext())
      return builder;

    builder.append(element.next());
    while (element.hasNext())
      builder.append(separator).append(element.next());
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a string around <code>' '</code>.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by a <code>' '</code>. The substrings in the
   ** array are in the order in which they occur in the given string. If no
   ** <code>' '</code> match any part of the input then the resulting array has
   ** just one element, namely the given string.
   **
   ** @param  subject            the string to split.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of
   **                            <code>' '</code>.
   */
  public static List<String> split(final String subject) {
    return split(subject, BLANK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a string around <code>delimiter</code>.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by the character specified by
   ** <code>delimiter</code>. The substrings in the array are in the order in
   ** which they occur in the given string. If no character contained in
   ** <code>delimiter</code> match any part of the input then the resulting
   ** array has just one element, namely the given string.
   **
   ** @param  subject            the string to split.
   ** @param  delimiter          the character used as the breaking boundaries.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of character
   **                            <code>delimiter</code>.
   */
  public static List<String> split(final String subject, final char delimiter) {
    return split(subject, delimiter, false, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a string around <code>delimiter</code>.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by the character specified by
   ** <code>delimiter</code>. The substrings in the array are in the order in
   ** which they occur in the given string. If no character contained in
   ** <code>delimiter</code> match any part of the input then the resulting
   ** array has just one element, namely the given string.
   **
   ** @param  subject            the string to split.
   ** @param  delimiter          the character used as the breaking boundaries.
   ** @param  trim               if <code>true</code>, the string is first
   **                            trimmed before checking to see if it is empty,
   **                            otherwise it is not.
   ** @param  empty              <code>true</code> collect the
   **                            <code>subject</code> only if its not empty
   **                            or empty after trimming.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of character
   **                            <code>delimiter</code>.
   */
  public static List<String> split(String subject, final char delimiter, final boolean trim, final boolean empty) {
    int index = subject.indexOf(delimiter);
    if (index < 0) {
      if (trim)
        subject = trim(subject, 0);
      return !empty || !subject.isEmpty() ? Collections.singletonList(subject) : Collections.emptyList();
    }
    final ArrayList<String> capture = new ArrayList<String>();
    capture.ensureCapacity(subject.length() / 5);
    int i = 0;
    while (index >= 0) {
      collect(subject, i, index, trim, empty, capture);
      i = index + 1;
      index = subject.indexOf(delimiter, i);
    }
    collect(subject, i, subject.length(), trim, empty, capture);
    return capture;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Returns a string that is a substring of the given string.
   ** <br>
   ** The substring begins at the specified <code>startIndex</code> and extends
   ** to the end of the given string.
   ** <p>
   ** This is equal to <code>subject.substring(startIndex).trim()</code>, but
   ** avoids temporary untrimmed substring allocation.
   ** <br>
   ** If the trimmed string is empty, a shared empty string is returned.
   **
   ** @param  subject            String to be tested for emptiness.
   ** @param  startIndex         the beginning index, inclusive.
   **
   ** @return                    the substring beginning at the specified
   **                            <code>startIndex</code> and extends to the end
   **                            of the given string.
   */
  public static String trim(final String subject, final int startIndex) {
    return trim(subject, startIndex, subject.length());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Returns a new {@link String} object that includes a substring of the
   ** given string with their indexes lying between startIndex and endIndex. If
   ** the second argument is given, the substring begins with the element at
   ** the <code>startIndex</code> to <code>endIndex - 1</code>.
   ** <p>
   ** This is equal to
   ** <code>subject.substring(startIndex, endIndex).trim()</code>, but avoids
   ** temporary untrimmed substring allocation. If the trimmed string is empty,
   ** a shared empty string is returned.
   **
   ** @param  subject            the {@link String} to derive the new
   **                            {@link String} from.
   ** @param  startIndex         the beginning index, inclusive.
   ** @param  endIndex           the ending index, exclusive.
   **
   ** @return                    the substring beginning at the specified
   **                            <code>startIndex</code> and extends to
   **                            <code>endIndex - 1</code>.
   */
  public static String trim(final String subject, int startIndex, int endIndex) {
    startIndex = skipLeadingSpace(subject, startIndex, endIndex);
    endIndex   = skipTrailingSpace(subject, startIndex, endIndex);
    return (endIndex > startIndex) ? subject.substring(startIndex, endIndex) : EMPTY;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collect
  /**
   ** Collects the given string <code>subject</code> in the collection of
   ** strings <code>collector<c/ode> accordingly to <code>empty</code> if its
   ** empty or not.
   **
   ** @param  subject            the {@link String} to collect.
   ** @param  startIndex         the beginning index, inclusive, for trimming.
   ** @param  endIndex           the ending index, exclusive, for trimming.
   ** @param  trim               if <code>true</code>, the string is first
   **                            trimmed before checking to see if it is empty,
   **                            otherwise it is not.
   ** @param  empty              <code>true</code> collect the
   **                            <code>subject</code> only if its not empty
   **                            or empty after trimming.
   ** @param  collector          the collectore of <code>subject</code>,
   */
  private static void collect(final String subject, int startIndex, int endIndex, final boolean trim, final boolean empty, final List<String> collector) {
    if (trim) {
      startIndex = skipLeadingSpace(subject,  startIndex, endIndex);
      endIndex   = skipTrailingSpace(subject, startIndex, endIndex);
    }

    if (!empty || endIndex > startIndex)
      collector.add(subject.substring(startIndex, endIndex));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skipLeadingSpace
  /**
   ** Skip leading spaces.
   **
   ** @param  subject            the {@link String} to test.
   ** @param  startIndex         the beginning index, inclusive.
   ** @param  endIndex           the ending index, exclusive.
   **
   ** @return                    the index of the first occurence of a space
   **                            character.
   */
  private static int skipLeadingSpace(final String subject, int startIndex, int endIndex) {
    // skip leading whitespace
    while (startIndex < endIndex && subject.charAt(startIndex) <= BLANK)
      startIndex++;
    return startIndex;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skipTrailingSpace
  /**
   ** Skip trailing spaces.
   **
   ** @param  subject            the {@link String} to test.
   ** @param  startIndex         the beginning index, inclusive.
   ** @param  endIndex           the ending index, exclusive.
   **
   ** @return                    the index of the last occurence of a space
   **                            character.
   */
  private static int skipTrailingSpace(final String subject, int startIndex, int endIndex) {
    // skip trailing whitespace
    while (startIndex < endIndex && subject.charAt(endIndex - 1) <= BLANK)
      endIndex--;
    return endIndex;
  }
}