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

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   StringUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.text.Collator;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous string utility methods. Mainly for internal use.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public abstract class StringUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String EMPTY = "";
  public static final char   BLANK = ' ';

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
    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blank
  /**
   ** Check to ensure that a <code>String</code> is not <code>blank</code> after
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
   ** @param  subject            String to be tested for blank.
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
   ** Check to ensure that a <code>String</code> is not <code>null</code> or
   ** empty after trimming of leading and trailing whitespace. Usually used with
   ** assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
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
   ** Check to ensure that a <code>String</code> is not <code>null</code> or
   ** empty (after optional trimming of leading and trailing whitespace).
   ** Usually used with assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher, true) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
   ** @param  trim	             if <code>true</code>, the string is first
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
  // Method:   whitespace
  /**
   ** Is specified {@link Character} a whitespace character according to
   ** production 3 of the XML 1.0 specification.
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
   ** Is specified character a whitespace character according to production 3 of
   ** the XML 1.0 specification.
   **
   ** @param ch                  <code>char</code> to check for XML whitespace
   **                            compliance.
   **
   ** @return                    <code>true</code> if it's a whitespace;
   **                            otherwise <code>false</code>.
   */
  public static boolean whitespace(final char ch) {
    // most of the characters are non-control characters.
    // so check that first to quickly return false for most of the cases.
    if (ch > 0x20)
      return false;

    // other than we have to do four comparisons.
    // the following if is faster than switch statements.
    // seems the implicit conversion to int is slower than the fall-through or's
    return ch == 0x09 || ch == 0x0a || ch == 0x0d || ch == 0x20;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Ensure the string contains only printable ascii characters.
   **
   ** @param  subject            string to test.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            printable ascii characters.
   */
  public static boolean printable(final String subject) {
    if (subject == null)
      return false;

    final int size = subject.length();
    for (int i = 0; i < size; i++) {
      if (!printable(subject.charAt(i)))
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   printable
  /**
   ** Is the specified {@link Character} an ASCII printable character?
   **
   ** @param  ch                 {@link Character} to be tested.
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
   ** Is the specified character an ASCII printable character?
   **
   ** @param  ch                 char to be tested.
   **
   ** @return                    <code>true</code> if the subject is ASCII
   **                            printable character; otherwise
   **                            <code>false</code>.
   */
  public static boolean printable(final char ch) {
    return ch >= 32 && ch < 128;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  lhs                the first String to be tested for equality.
   ** @param  rhs                the second String to be tested for equality.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equal(final String lhs, final String rhs) {
    if (lhs == rhs)
      return true;
    else if (lhs == null)
      return (rhs == null);
    else if (rhs == null)
      return (lhs == null);
    else
      return lhs.equals(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalCaseInsensitive
  /**
   ** Compares two strings if they are lexicographically identically by ignoring
   ** case sensitivity
   **
   ** @param  lhs                the first String to be tested for equality.
   ** @param  rhs                the second String to be tested for equality.
   **
   ** @return                    <code>true</code> if the strings are
   **                            caseinsensitve lexicographically identically;
   **                            otherwise <code>false</code>.
   */
  public static boolean equalCaseInsensitive(final String lhs, final String rhs) {
    if (lhs == rhs)
      return true;
    else if (lhs == null)
      return (rhs == null);
    else if (rhs == null)
      return (lhs == null);
    else
      return equal(lhs.toUpperCase(LocaleRegistry.instance()), rhs.toUpperCase(LocaleRegistry.instance()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by <code>lhs</code> is
   ** compared lexicographically to the character sequence represented by
   ** <code>rhs</code>.
   ** <br>
   ** The result is a negative integer if <code>lhs</code> lexicographically
   ** precedes <code>rhs</code>. The result is a positive integer if
   ** <code>lhs</code> lexicographically follows <code>rhs</code>. The result is
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>.
   */
  public static int compare(final String lhs, final String rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
    else
      return lhs.compareTo(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two objects lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** objects. The character sequence represented by <code>lhs</code> is
   ** compared lexicographically to the character sequence represented by
   ** <code>rhs</code>.
   ** <br>
   ** The result is a negative integer if <code>lhs</code> lexicographically
   ** precedes <code>rhs</code>. The result is a positive integer if
   ** <code>lhs</code> lexicographically follows <code>lhs</code>. The result is
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>.
   */
  public static int compare(final Object lhs, final Object rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
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
   ** provides {@link Collator}s to allow locale-sensitive ordering.
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
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
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
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
   ** provides {@link Collator}s to allow locale-sensitive ordering.
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
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
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
    else
      return lhs.toString().compareToIgnoreCase(rhs.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   colate
  /**
   ** Compares two strings according to the collation rules for
   ** {@link Collator}.
   ** The comparison is based on the Unicode value of each character in the
   ** objects. The character sequence represented by <code>lhs</code> is
   ** compared lexicographically to the character sequence represented by
   ** <code>rhs</code>.
   ** <br>
   ** The result is a negative integer if <code>lhs</code> lexicographically
   ** precedes <code>rhs</code>. The result is a positive integer if
   ** <code>lhs</code> lexicographically follows <code>lhs</code>. The result is
   ** zero if the strings are equal; <code>compare</code> returns <code>0</code>
   ** exactly when the {@link #equal(String, String)} method would return
   ** <code>true</code>.
   ** <p>
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>.
   */
  public static int colate(final String lhs, final String rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
    else
      return Collator.getInstance().compare(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   colate
  /**
   ** Compares two objects according to the collation rules for
   ** {@link Collator}.
   ** The comparison is based on the Unicode value of each character in the
   ** objects. The character sequence represented by <code>lhs</code> is
   ** compared lexicographically to the character sequence represented by
   ** <code>rhs</code>.
   ** <br>
   ** The result is a negative integer if <code>lhs</code> lexicographically
   ** precedes <code>rhs</code>. The result is a positive integer if
   ** <code>lhs</code> lexicographically follows <code>lhs</code>. The result is
   ** zero if the strings are equal; <code>compare</code> returns <code>0</code>
   ** exactly when the {@link #equal(String, String)} method would return
   ** <code>true</code>.
   ** <p>
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
   ** @param  lhs                the first String to compare.
   ** @param  rhs                the second String to compare.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>.
   */
  public static int colate(final Object lhs, final Object rhs) {
    if (lhs == rhs)
      return 0;
    else if (lhs == null)
      return (rhs == null) ? 0 : -1;
    else if (rhs == null)
      return (lhs == null) ? 1 : 0;
    else
      return Collator.getInstance().compare(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToHex
  /**
   ** Turns array of bytes into string representing each byte as a two digit
   ** unsigned hex number.
   **
   ** @param  raw                array of bytes to convert to hex-string
   **
   ** @return                    generated hex string
   */
  public static String bytesToHex(final byte raw[]) {
    final StringBuilder buffer = new StringBuilder(raw.length * 2);
    for (int i = 0; i < raw.length; i++) {
      final int value = raw[i] & 0xff;
      if (value < 0x10)
        // append a zero before a one digit hex number to make it two digits.
        buffer.append("0");

      buffer.append(Integer.toHexString(value));
    }
    return buffer.toString().toUpperCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToString
  /**
   ** Turns array of bytes into string representing
   **
   ** @param  raw                array of bytes to convert to hex-string
   **
   ** @return                    converted string
   */
  public static String bytesToString(final byte raw[]) {
    return new String(raw);
  }
}