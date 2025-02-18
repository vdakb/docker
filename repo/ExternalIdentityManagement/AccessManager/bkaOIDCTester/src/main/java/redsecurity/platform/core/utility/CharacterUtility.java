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

    File        :   CharacterUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    CharacterUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.util.Arrays;

import redsecurity.platform.core.SystemValidator;

////////////////////////////////////////////////////////////////////////////////
// abstract class CharacterUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous character utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class CharacterUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final byte[]   HEX2B;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    // generate the lookup table that converts an hex char into its decimal
    // value
    // the size of the table is such that the JVM is capable of save any
    // bounds-check if a char type is used as an index.
    HEX2B = new byte[Character.MAX_VALUE + 1];
    Arrays.fill(HEX2B, (byte) - 1);
    HEX2B['0'] =  0;
    HEX2B['1'] =  1;
    HEX2B['2'] =  2;
    HEX2B['3'] =  3;
    HEX2B['4'] =  4;
    HEX2B['5'] =  5;
    HEX2B['6'] =  6;
    HEX2B['7'] =  7;
    HEX2B['8'] =  8;
    HEX2B['9'] =  9;
    HEX2B['A'] = 10;
    HEX2B['B'] = 11;
    HEX2B['C'] = 12;
    HEX2B['D'] = 13;
    HEX2B['E'] = 14;
    HEX2B['F'] = 15;
    HEX2B['a'] = 10;
    HEX2B['b'] = 11;
    HEX2B['c'] = 12;
    HEX2B['d'] = 13;
    HEX2B['e'] = 14;
    HEX2B['f'] = 15;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CharacterUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CharacterUtility()" and enforces use of the public method below.
   */
  private CharacterUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalIgnoreCase
  /**
   ** Compares two characters if they are lexicographically identically.
   **
   ** @param  lhs                the character to compare with <code>rhs</code>.
   ** @param  rhs                the characterto compare with <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the characters are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equalIgnoreCase(final char lhs, final char rhs) {
    return lhs == rhs || upperCase(lhs) == upperCase(rhs) || lowerCase(lhs) == lowerCase(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upperCase
  /**
   ** Converts the character argument to uppercase using case mapping
   ** information from the UnicodeData file.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <code>Character.isUpperCase(Character.toUpperCase(c))</code> does not
   ** always return <code>true</code> for some ranges of characters,
   ** particularly those that are symbols or ideographs.
   ** <p>
   ** In general, {@link StringUtility#upperCase(String)} should be used to map
   ** characters to uppercase. {@link String} case mapping methods
   ** have several benefits over {@link Character} case mapping methods.
   ** {@link String} case mapping methods can perform locale-sensitive mappings,
   ** context-sensitive mappings, and 1:M character mappings, whereas the
   ** {@link Character} case mapping methods cannot.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method cannot handle <a href="#supplementary"> supplementary characters</a>.
   ** To support all Unicode characters, including supplementary characters, use
   ** the {@link #upperCase(int)} method.
   **
   ** @param  c                  the character to be converted.
   **
   ** @return                    the uppercase equivalent of the character, if
   **                            any; otherwise, the character itself.
   */
  public static char upperCase(final char c) {
    return (c < 'a') ? c : (c <= 'z') ? (char)(c + ('A' - 'a')) : Character.toUpperCase(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upperCase
  /**
   ** Converts the character argument to uppercase using case mapping
   ** information from the UnicodeData file.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <code>Character.isUpperCase(Character.toUpperCase(c))</code> does not
   ** always return <code>true</code> for some ranges of characters,
   ** particularly those that are symbols or ideographs.
   **
   ** @param  c                  the character to be converted.
   **
   ** @return                    the uppercase equivalent of the character, if
   **                            any; otherwise, the character itself.
   */
  public static int upperCase(final int c) {
    return (c < 97) ? c : (c <= 122) ? (c + (64 - 97)) : Character.toUpperCase(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lowerCase
  /**
   ** Converts the character argument to lowercase using case mapping
   ** information from the UnicodeData file.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <code>Character.isLowerCase(Character.toLowerCase(c))</code> does not
   ** always return <code>true</code> for some ranges of characters,
   ** particularly those that are symbols or ideographs.
   ** <p>
   ** In general, {@link StringUtility#lowerCase(String)} should be used to map
   ** characters to lowercase. {@link String} case mapping methods have several
   ** benefits over {@link Character} case mapping methods. {@link String} case
   ** mapping methods can perform locale-sensitive mappings, context-sensitive
   ** mappings, and 1:M character mappings, whereas the {@link Character} case
   ** mapping methods cannot.
   ** <p>
   ** <b>Note:</b>
   ** <br>
   ** This method cannot handle <a href="#supplementary"> supplementary characters</a>.
   ** To support all Unicode characters, including supplementary characters, use
   ** the {@link #lowerCase(int)} method.
   **
   ** @param  c                  the character to be converted.
   **
   ** @return                    the lowercase equivalent of the character, if
   **                            any; otherwise, the character itself.
   */
  public static char lowerCase(final char c) {
    return (c <= 'z') ? c : c >= 'A' && c <= 'Z' ? (char)(c + ('a' - 'A')) : Character.toLowerCase(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lowerCase
  /**
   ** Converts the character (Unicode code point) argument to lowercase using
   ** case mapping information from the UnicodeData file.
   ** <p>
   ** <b>Note</b>
   ** <br>
   ** <code>Character.isLowerCase(Character.toLowerCase(c))</code> does not
   ** always return <code>true</code> for some ranges of characters,
   ** particularly those that are symbols or ideographs.
   **
   ** @param  c                  the character to be converted.
   **
   ** @return                    the lowercase equivalent of the character, if
   **                            any; otherwise, the character itself.
   */
  public static int lowerCase(final int c) {
    return (c <= 122) ? c : c >= 64 && c <= 90 ? (c + (97 - 64)) : Character.toLowerCase(c);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ascii
  /**
   ** Returns whether the contents of the specified byte array
   ** <code>value</code> represent an ASCII string, which is also known in LDAP
   ** terminology as an IA5 string.
   ** <br>
   ** An ASCII string is one that contains only bytes in which the most
   ** significant bit is zero.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the contents of the
   **                            specified array represent an ASCII string;
   **                            <code>false</code> otherwise.
   */
  public static boolean ascii(final byte[] value) {
    for (final byte b : value) {
      if ((b & 0x80) == 0x80)
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ascii
  /**
   ** Returns whether the specified {@link Character} <code>ch</code> is an
   ** ASCII character.
   **
   ** @param  ch                 the {@link Character} to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            character; otherwise <code>false</code>.
   */
  public static boolean ascii(final Character ch) {
    return ascii(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ascii
  /**
   ** Returns whether the specified byte <code>b</code> is an ASCII character.
   **
   ** @param  b                  the byte to examine.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            character; otherwise <code>false</code>.
   */
  public static boolean ascii(final byte b) {
    return b < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ascii
  /**
   ** Returns whether the specified character <code>c</code> is an ASCII
   ** character.
   **
   ** @param  c                  the character to examine.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            character; otherwise <code>false</code>.
   */
  public static boolean ascii(final char c) {
    return c < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Returns whether the specified {@link Character} <code>ch</code> is a
   ** printable ASCII character.
   **
   ** @param  ch                 the {@link Character} to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            printable character; otherwise
   **                            <code>false</code>.
   */
  public static boolean printable(final Character ch) {
    return printable(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Returns whether the contents of the specified byte array
   ** <code>value</code> represent a printable string, as per RFC 4517 section
   ** 3.2.
   ** <br>
   ** The only characters allowed in a printable string are:
   ** <ul>
   **   <li>All uppercase and lowercase ASCII alphabetic letters
   **   <li>All ASCII numeric digits
   **   <li>The following additional ASCII characters:  single quote, left
   **       parenthesis, right parenthesis, plus, comma, hyphen, period, equals,
   **       forward slash, colon, question mark, space.</LI>
   ** </ul>
   ** If the specified array contains anything other than the above characters
   ** (i.e., if the byte array contains any non-ASCII characters, or any ASCII
   ** control characters, or if it contains excluded ASCII characters like the
   ** exclamation point, double quote, octothorpe, dollar sign, etc.), then it
   ** will not be considered printable.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the contents of the
   **                            specified byte array represent a printable
   **                            string; otherwise
   **                            <code>false</code>.
   */
  public static boolean printable(final byte[] value) {
    for (final byte b : value) {
      if ((b & 0x80) == 0x80) {
        return false;
      }

      if (((b >= 'a') && (b <= 'z')) || ((b >= 'A') && (b <= 'Z')) || ((b >= '0') && (b <= '9')))
        continue;

      switch (b) {
        case '\'' :
        case '('  :
        case ')'  :
        case '+'  :
        case ','  :
        case '-'  :
        case '.'  :
        case '='  :
        case '/'  :
        case ':'  :
        case '?'  :
        case ' '  : continue;
        default   : return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Returns whether the specified character <code>c</code> is a printable
   ** ASCII character.
   **
   ** @param  c                  the character to examine.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            printable character; otherwise
   **                            <code>false</code>.
   */
  public static boolean printable(final char c) {
    return c >= 32 && c < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printableRFC4517
  /**
   ** Returns whether the contents of the specified byte represent a printable
   ** character as per RFC 4517 section 3.2.
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
   ** If the specified byte is anything other than the above characters
   ** (i.e., if the byte may any non-ASCII characters, or any ASCII
   ** control character, or if it is a excluded ASCII characters like
   ** the exclamation point, double quote, octothorpe, dollar sign, etc.), then
   ** it will not be considered printable.
   **
   ** @param  value              the byte to examine.
   **
   ** @return                    <code>true</code> if the specified byte
   **                            represent a printable character;
   **                            <code>false</code> otherwise.
   */
  public static boolean printableRFC4517(final byte value) {
    if ((value & 0x80) == 0x80)
        return false;
    if (((value >= 'a') && (value <= 'z')) || ((value >= 'A') && (value <= 'Z')) || ((value >= '0') && (value <= '9')))
      return true;
    switch (value) {
      case '\'' :
      case '('  :
      case ')'  :
      case '+'  :
      case ','  :
      case '-'  :
      case '.'  :
      case '='  :
      case '/'  :
      case ':'  :
      case '?'  :
      case ' '  : return true;
      default   : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayable
  /**
   ** Returns whether the specified Unicode code point represents a character
   ** that is believed to be displayable.
   ** <br>
   ** Displayable characters include letters, numbers, spaces, dashes,
   ** punctuation, symbols, and marks.
   ** <br>
   ** Non-displayable characters include control characters, directionality
   ** indicators, like and paragraph separators, format characters, and
   ** surrogate characters.
   **
   ** @param  codePoint          the code point to examine.
   **
   ** @return                    <code>true</code> if the specified Unicode
   **                            character is believed to be displayable;
   **                            <code>false</code> otherwise.
   */
  public static boolean displayable(final int codePoint) {
    final int type = Character.getType(codePoint);
    switch (type) {
      case Character.UPPERCASE_LETTER          :
      case Character.LOWERCASE_LETTER          :
      case Character.TITLECASE_LETTER          :
      case Character.MODIFIER_LETTER           :
      case Character.OTHER_LETTER              :
      case Character.DECIMAL_DIGIT_NUMBER      :
      case Character.LETTER_NUMBER             :
      case Character.OTHER_NUMBER              :
      case Character.SPACE_SEPARATOR           :
      case Character.DASH_PUNCTUATION          :
      case Character.START_PUNCTUATION         :
      case Character.END_PUNCTUATION           :
      case Character.CONNECTOR_PUNCTUATION     :
      case Character.OTHER_PUNCTUATION         :
      case Character.INITIAL_QUOTE_PUNCTUATION :
      case Character.FINAL_QUOTE_PUNCTUATION   :
      case Character.MATH_SYMBOL               :
      case Character.CURRENCY_SYMBOL           :
      case Character.MODIFIER_SYMBOL           :
      case Character.OTHER_SYMBOL              :
      case Character.NON_SPACING_MARK          :
      case Character.ENCLOSING_MARK            :
      case Character.COMBINING_SPACING_MARK    : return true;
      case Character.UNASSIGNED                :
      case Character.LINE_SEPARATOR            :
      case Character.PARAGRAPH_SEPARATOR       :
      case Character.CONTROL                   :
      case Character.FORMAT                    :
      case Character.PRIVATE_USE               :
      case Character.SURROGATE                 :
      default                                  : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeHexadeciaml
  /**
   ** Appends a hex-encoded representation of the specified code point to the
   ** given buffer.
   ** <br>
   ** Each byte of the hex-encoded representation will be prefixed with a
   ** backslash.
   **
   ** @param  codePoint          the code point to be encoded.
   ** @param  buffer             the buffer to which the hex-encoded
   **                            representation should be appended.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   */
  public static void encodeHexadeciaml(final int codePoint, final StringBuilder buffer) {
    final byte[] value = StringUtility.bytes(new String(new int[] { codePoint }, 0, 1));
    for (final byte b : value) {
      buffer.append('\\');
      hexadecimal(b, buffer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   encodeHexadecimal
  /**
   ** Appends a hex-encoded representation of the specified character to the
   ** given buffer.
   ** <br>
   ** Each byte of the hex-encoded representation will be prefixed with a
   ** backslash.
   **
   ** @param  c                  the character to be encoded.
   ** @param  buffer             the buffer to which the hex-encoded
   **                            representation should be appended.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   */
  public static void encodeHexadecimal(final char c, final StringBuilder buffer) {
    final byte[] value = (c <= 0x7F) ?  new byte[]{(byte)(c & 0x7F)} : StringUtility.bytes(String.valueOf(c));
    for (final byte b : value) {
      buffer.append('\\');
      hexadecimal(b, buffer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeHexadecimal
  /**
   ** Decode half of a hexadecimal number from a <code>byte</code>.
   **
   ** @param  b                  the ASCII character of the hexadecimal number
   **                            to decode.
   **                            <br>
   **                            Must be in the range {@code [0-9a-fA-F]}.
   **
   ** @return                    the hexadecimal value represented in the ASCII
   **                            character specified, or <code>-1</code> if the
   **                            character is invalid.
   */
  public static int decodeHexadecimal(final byte b) {
    // Character.digit() is not used here, as it addresses a larger
    // set of characters (both ASCII and full-width latin letters).
    return HEX2B[b];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeHexadecimal
  /**
   ** Decode half of a hexadecimal number from a <code>char</code>.
   **
   ** @param  c                  the ASCII character of the hexadecimal number
   **                            to decode.
   **                            <br>
   **                            Must be in the range {@code [0-9a-fA-F]}.
   **
   ** @return                    the hexadecimal value represented in the ASCII
   **                            character specified, or <code>-1</code> if the
   **                            character is invalid.
   */
  public static int decodeHexadecimal(final char c) {
    // Character.digit() is not used here, as it addresses a larger
    // set of characters (both ASCII and full-width latin letters).
    return HEX2B[c];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns whether the specified character is a valid hexadecimal digit.
   **
   ** @param  c                  the character to examine.
   **
   ** @return                    <code>true</code> if the specified character
   **                            does represent a valid hexadecimal digit;
   **                            <code>false</code> otherwise.
   */
  public static boolean hexadecimal(final Character c) {
    return hexadecimal(c.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns whether the specified character is a valid hexadecimal digit.
   **
   ** @param  c                  the character to examine.
   **
   ** @return                    <code>true</code> if the specified character
   **                            does represent a valid hexadecimal digit;
   **                            <code>false</code> otherwise.
   */
  public static boolean hexadecimal(final char c) {
    switch (c) {
      case '0' :
      case '1' :
      case '2' :
      case '3' :
      case '4' :
      case '5' :
      case '6' :
      case '7' :
      case '8' :
      case '9' :
      case 'a' :
      case 'A' :
      case 'b' :
      case 'B' :
      case 'c' :
      case 'C' :
      case 'd' :
      case 'D' :
      case 'e' :
      case 'E' :
      case 'f' :
      case 'F' : return true;
      default  : return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns a hexadecimal representation of the specified byte.
   **
   ** @param  value              the byte to encode as hexadecimal.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    a string containing a hexadecimal
   **                            representation of the contents of the specified
   **                            byte.
   */
  public static String hexadecimal(final byte value) {
    final StringBuilder buffer = new StringBuilder(2);
    hexadecimal(value, buffer);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns a hexadecimal representation of the contents of the specified byte
   ** array.
   ** <br>
   ** No delimiter character will be inserted between the hexadecimal digits for
   ** each byte.
   **
   ** @param  value              the byte array for which to returna hexadecimal
   **                            string.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    a string containing a hexadecimal
   **                            representation of the contents of the specified
   **                            byte array.
   */
  public static String hexadecimal(final byte[] value) {
    SystemValidator.assertNotNull(value);
    final StringBuilder buffer = new StringBuilder(2 * value.length);
    hexadecimal(value, buffer);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns a hexadecimal representation of the contents of the specified byte
   ** array.
   ** <br>
   ** No delimiter character will be inserted between the hexadecimal digits for
   ** each byte.
   **
   ** @param  value              the byte array for which to returna hexadecimal
   **                            string.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  buffer             a buffer to which the hexadecimal
   **                            representation of the contents of the specified
   **                            byte array should be appended.
   */
  public static void hexadecimal(final byte[] value, final StringBuilder buffer) {
    hexadecimal(value, null, buffer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Returns a hexadecimal representation of the contents of the specified byte
   ** array.
   ** <br>
   ** No delimiter character will be inserted between the hexadecimal digits for
   ** each byte.
   **
   ** @param  value              the byte array for which to returna hexadecimal
   **                            string.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  delimiter          a delimiter to be inserted between bytes.
   **                            <br>
   **                            May be <code>null</code> if no delimiter should
   **                            be used.
   ** @param  buffer             a buffer to which the hexadecimal
   **                            representation of the contents of the specified
   **                            byte array should be appended.
   */
  public static void hexadecimal(final byte[] value, final String delimiter, final StringBuilder buffer) {
    boolean hit = true;
    for (final byte b : value) {
      if (hit) {
        hit = false;
      }
      else if (delimiter != null) {
        buffer.append(delimiter);
      }
      hexadecimal(b, buffer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexadecimal
  /**
   ** Appends a hexadecimal representation of the specified byte to the given
   ** buffer.
   **
   ** @param  value              the byte to encode as hexadecimal.
   ** @param  buffer             a buffer to which the hexadecimal
   **                            representation is to be appended.
   */
  public static void hexadecimal(final byte value, final StringBuilder buffer) {
    switch (value & 0xF0) {
      case 0x00 : buffer.append('0');
                  break;
      case 0x10 : buffer.append('1');
                  break;
      case 0x20 : buffer.append('2');
                  break;
      case 0x30 : buffer.append('3');
                  break;
      case 0x40 : buffer.append('4');
                  break;
      case 0x50 : buffer.append('5');
                  break;
      case 0x60 : buffer.append('6');
                  break;
      case 0x70 : buffer.append('7');
                  break;
      case 0x80 : buffer.append('8');
                  break;
      case 0x90 : buffer.append('9');
                  break;
      case 0xA0 : buffer.append('a');
                  break;
      case 0xB0 : buffer.append('b');
                  break;
      case 0xC0 : buffer.append('c');
                  break;
      case 0xD0 : buffer.append('d');
                  break;
      case 0xE0 : buffer.append('e');
                  break;
      case 0xF0 : buffer.append('f');
                  break;
    }

    switch (value & 0x0F) {
      case 0x00 : buffer.append('0');
                  break;
      case 0x01 : buffer.append('1');
                  break;
      case 0x02 : buffer.append('2');
                  break;
      case 0x03 : buffer.append('3');
                  break;
      case 0x04 : buffer.append('4');
                  break;
      case 0x05 : buffer.append('5');
                  break;
      case 0x06 : buffer.append('6');
                  break;
      case 0x07 : buffer.append('7');
                  break;
      case 0x08 : buffer.append('8');
                  break;
      case 0x09 : buffer.append('9');
                  break;
      case 0x0A : buffer.append('a');
                  break;
      case 0x0B : buffer.append('b');
                  break;
      case 0x0C : buffer.append('c');
                  break;
      case 0x0D : buffer.append('d');
                  break;
      case 0x0E : buffer.append('e');
                  break;
      case 0x0F : buffer.append('f');
                  break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns whether the specified {@link Character} a control character.
   **
   ** @param  ch                 the {@link Character} to examine.
   **
   ** @return                    <code>true</code> if the subject is a control
   **                            character; otherwise <code>false</code>.
   */
  public static boolean control(final Character ch) {
    return control(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   control
  /**
   ** Returns whether the specified character a control character.
   **
   ** @param  c                  the character to examine.
   **
   ** @return                    <code>true</code> if the subject is a control
   **                            character; otherwise <code>false</code>.
   */
  public static boolean control(final char c) {
    return (c < ' ') || (c == '');
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   whitespace
  /**
   ** Returns whether the specified {@link Character} a whitespace character
   ** according to production 3 of the XML 1.0 specification.
   **
   ** @param ch                  {@link Character} to check for XML whitespace
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a whitespace;
   **                            otherwise <code>false</code>.
   */
  public static boolean whitespace(final Character ch) {
    return whitespace(ch.charValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   whitespace
  /**
   ** Returns whether the specified character a whitespace character according
   ** to production 3 of the XML 1.0 specification.
   **
   ** @param c                   <code>char</code> to check for XML whitespace
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a whitespace;
   **                            otherwise <code>false</code>.
   */
  public static boolean whitespace(final char c) {
    // most of the characters are non-control characters.
    // so check that first to quickly return false for most of the cases else we
    // we have to do four comparisons the following if is faster than switch
    // statements
    // seems the implicit conversion to int is slower than the fall-through or's
    return (c > 0x20) ? false : c == 0x09 || c == 0x0a || c == 0x0d || c == 0x20;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicode
  /**
   ** Returns whether the contents of the specified array represent a valid
   ** UTF-8 string, which may or may not contain non-ASCII characters.
   ** <br>
   ** <b>Note</b>;
   ** <br>
   ** this method does not make any attempt to determine whether the characters
   ** in the UTF-8 string actually map to assigned Unicode code points.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the byte array can be
   **                            parsed as a valid UTF-8 string;
   **                            <code>false</code> otherwise.
   */
  public static boolean unicode(final byte[] value) {
    return unicode(value, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validUnicode
  /**
   ** Returns whether the contents of the specified array represent a valid
   ** UTF-8 string that contains at least one non-ASCII character (and may
   ** contain zero or more ASCII characters).
   ** <br>
   ** <b>Note</b>>;
   ** <br>
   ** this method does not make any attempt to determine whether the characters
   ** in the UTF-8 string actually map to assigned Unicode code points.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  nonASCII           whether to require at least one non-ASCII
   **                            character in the specified string.
   **
   ** @return                    <code>true</code> if the byte array can be
   **                            parsed as a valid UTF-8 string and meets the
   **                            non-ASCII requirement if appropriate;
   **                            <code>false</code> otherwise.
   */
  private static boolean unicode(final byte[] b, final boolean nonASCII) {
    int     i = 0;
    boolean containsNonASCII = false;
    while (i < b.length) {
      final byte currentByte = b[i++];
      // if the most significant bit is not set, then this represents a valid
      // single-byte character
      if ((currentByte & 0b1000_0000) == 0b0000_0000)
        continue;

      // if the first byte starts with 0b110, then it must be followed by
      // another byte that starts with 0b10
      if ((currentByte & 0b1110_0000) == 0b1100_0000) {
        if (!unicode(b, i, 1))
          return false;

        i++;
        containsNonASCII = true;
        continue;
      }
      // if the first byte starts with 0b1110, then it must be followed by two
      // more bytes that start with 0b10
      if ((currentByte & 0b1111_0000) == 0b1110_0000) {
        if (!unicode(b, i, 2))
          return false;

        i += 2;
        containsNonASCII = true;
        continue;
      }
      // if the first byte starts with 0b11110, then it must be followed by
      // three more bytes that start with 0b10
      if ((currentByte & 0b1111_1000) == 0b1111_0000) {
        if (!unicode(b, i, 3))
          return false;

        i += 3;
        containsNonASCII = true;
        continue;
      }
      // if the first byte starts with 0b111110, then it must be followed by
      // four more bytes that start with 0b10
      if ((currentByte & 0b1111_1100) == 0b1111_1000) {
        if (!unicode(b, i, 4))
          return false;

        i += 4;
        containsNonASCII = true;
        continue;
      }
      // if the first byte starts with 0b1111110, then it must be followed by
      // five more bytes that start with 0b10
      if ((currentByte & 0b1111_1110) == 0b1111_1100) {
        if (!unicode(b, i, 5))
          return false;

        i += 5;
        containsNonASCII = true;
        continue;
      }
      // this is not a valid first byte for a UTF-8 character
      return false;
    }

    // if we've gotten here, then the specified array represents a valid UTF-8
    // string.  If appropriate, make sure it also satisfies the requirement to
    // have at leaste one non-ASCII character
    return containsNonASCII || (!nonASCII);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unicode
  /**
   ** Returns whether the specified vyte array <code>value</code> has the
   ** expected number of bytes that start with 0b10 starting at the specified
   ** position in the array.
   **
   ** @param  value              the byte array to examine.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  pos                the position in the byte array at which to
   **                            start looking.
   ** @param  length             the number of bytes to examine.
   **
   ** @return                    <code>true</code> if the specified byte array
   **                            has the expected number of bytes that start
   **                            with 0b10; otherwise <code>false</code>.
   */
  private static boolean unicode(final byte[] value, final int pos, final int length) {
    // prevent bogus input
    if (value.length < (pos + length))
      return false;

    for (int i = 0; i < length; i++) {
      if ((value[pos + i] & 0b1100_0000) != 0b1000_0000)
        return false;
    }
    return true;
  }
}