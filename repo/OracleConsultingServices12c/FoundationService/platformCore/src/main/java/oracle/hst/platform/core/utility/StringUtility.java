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

    File        :   StringUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

////////////////////////////////////////////////////////////////////////////////
// abstract class StringUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Miscellaneous string utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class StringUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String  EMPTY     = "";
  static final String  NULL      = "[null]";
  static final String  LINEBREAK = "\n";
  static final String  PATHBREAK = "/";

  static final char    BLANK     = ' ';

  static final Pattern CSV       = Pattern.compile("\\s*,\\s*");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StringUtility</code>.
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
  // Method:   hashIgnoreCase
  /**
   ** Returns a hash code for the given string.
   ** <p>
   ** The hash code for a <code>String</code> object is computed as
   ** <blockquote>
   **   <pre>
   **     s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
   **   </pre>
   ** </blockquote> using <code>int</code> arithmetic, where <code>s[i]</code>
   ** is the <i>i</i>th character of the string, <code>n</code> is the length of
   ** the string, and <code>^</code> indicates exponentiation.
   ** <p>
   ** To normalize the computed hash values the string is converted to uppercase
   ** using the rules of the default locale.
   **
   ** @param  subject            a string to build a hash code value for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hashIgnoreCase(final String subject) {
    return hash(subject.toUpperCase(LocaleRegistry.instance()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hash
  /**
   ** Returns a hash code for the given string.
   ** <p>
   ** The hash code for a <code>String</code> object is computed as
   ** <blockquote>
   **   <pre>
   **     s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
   **   </pre>
   ** </blockquote> using <code>int</code> arithmetic, where <code>s[i]</code>
   ** is the <i>i</i>th character of the string, <code>n</code> is the length of
   ** the string, and <code>^</code> indicates exponentiation.
   ** <p>
   ** To normalize the computed hash values the string is converted to uppercase
   ** using the rules of the default locale.
   **
   ** @param  subject            a string to build a hash code value for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hash(final String subject) {
    return subject.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   null2empty
  /**
   ** Returns the given string if it is non-null; the empty string otherwise.
   **
   ** @param  string             the string to test and possibly return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>string</code> itself if it is
   **                            non-<code>null</code>; <code>""</code> if it is
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String null2empty(final String string) {
    return (string == null) ? "" : string;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty2Null
  /**
   ** Returns the given string if it is nonempty; <code>null</code> otherwise.
   **
   ** @param  string             the string to test and possibly return.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>string</code> itself if it isnonempty;
   **                            <code>null</code> if it is empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String empty2Null(final String string) {
    return empty(string) ? null : string;
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the subject is just
   **                            blank.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   ** @param  subject            the string to be tested for emptiness.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   ** @param  subject            String to be tested for emptiness.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  trim               if <code>true</code>, the string is first
   **                            trimmed before checking to see if it is empty,
   **                            otherwise it is not.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean empty(final String subject, final boolean trim) {
    return (trim) ? (subject == null || subject.trim().length() == 0) : (subject == null || subject.length() == 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unequal
  /**
   ** Compares two strings if they are <b>not</b> lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the strings are <b>not</b>
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean equal(final String lhs, final String rhs) {
    return lhs == null ? rhs == null : lhs.equals(rhs);
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if <code>rhs</code>
   **                            represents an equivalent <code>String</code>
   **                            ignoring case as <code>lhs</code>;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean equalIgnoreCase(final String lhs, final String rhs) {
    return lhs == null ? rhs == null : lhs.equalsIgnoreCase(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWith
  /**
   ** Determines if the string parameter <code>base</code> starts with the
   ** character value.
   **
   ** @param  base               the string to check for the character at the
   **                            start.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the character to look for at the start of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found at the start of the string parameter
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  prefix             the starting text.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            the given prefix text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean startsWith(final String base, final String prefix) {
    return (base == null || prefix == null) ? (base == null && prefix == null) : (base.length() < prefix.length()) ? false : base.startsWith(prefix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   startsWithOneOf
  /**
   ** Returns <code>true</code> if the given string starts with one of the
   ** given prefixes.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  prefix             the collection of starting text to be checked.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            one of the given prefixes; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean startsWithOneOf(final String base, final String... prefix) {
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
  // Method:   startsWithIgnoreCase
  /**
   ** Helper functions to query a strings start portion.
   ** <br>
   ** The comparison is case insensitive.
   **
   ** @param  base               the base string.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  prefix             the starting text.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the string starts with
   **                            the given prefix text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the character to look for at the end of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found at the end of the string parameter
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffix             the ending text.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the string ends with the
   **                            given suffix text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean endsWith(final String base, final String suffix) {
    return (base == null || suffix == null) ? (base == null && suffix == null) : (base.length() < suffix.length())  ? false : base.endsWith(suffix);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWithOneOf
  /**
   ** Returns <code>true</code> if the given string ends with one of the given
   ** suffixes.
   ** <br>
   ** The comparison is case sensitive.
   **
   ** @param  base               the base string.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffix             the collection of suffixes to be checked.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                     <code>true</code> if the given string ends
   **                             with one of the given suffixes; otherwise
   **                             false.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean endsWithOneOf(final String base, final String... suffix) {
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffix             the ending text.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the string ends with the
   **                            given suffix text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
  // Method:   contains
  /**
   ** Determines if the string parameter <code>base</code> contains the
   ** character value.
   **
   ** @param  base               the string to check for the character at the
   **                            end.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the character to look for at the end of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found somewhere in the string parameter
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contain            the containing text.
   **
   ** @return                    <code>true</code>, if the string contains
   **                            the given text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  contain            the containing text.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code>, if the string contains
   **                            the given text.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>a</code> is lexicographically greater
   **                            than the <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  rhs                the <code>Object</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than the <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>, ignoring case
   **                            considerations.
   **                            <br>
   **                            Possible object is <code>int</code>.
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  rhs                the <code>Object</code> to compare with
   **                            <code>lhs</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the value <code>0</code> if <code>lhs</code> is
   **                            equal to <code>rhs</code>; a value less than
   **                            <code>0</code> if <code>lhs</code> is
   **                            lexicographically less than <code>rhs</code>;
   **                            and a value greater than <code>0</code> if
   **                            <code>lhs</code> is lexicographically greater
   **                            than <code>rhs</code>, ignoring case
   **                            considerations.
   **                            <br>
   **                            Possible object is <code>int</code>.
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
  // Method:   join
  /**
   ** Converts a {@link List} of strings to single string by inserting a space
   ** between each element of the list.
   **
   ** @param  list               the {@link List} of Strings to convert.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   **
   ** @return                    a String made up of the words in the list.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   ** @param  separator          the separator to insert between elements.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a String made up of the words in the list.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   ** @param  separator          the separator to insert between elements.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   ** @param  escape             the enclosing character for each element.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a String made up of the words in the list.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  suffix             the suffix of the string ro build.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>prefix</code> and <code>suffix</code>.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  element            the collection of elements to conatenate.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   **                            <br>
   **                            Possible object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  element            the elements to join.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   **                            <br>
   **                            Possible object is {@link String}.
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
   ** Splits a string around Whitespaces.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by a Whitespace. The substrings in the array are
   ** in the order in which they occur in the given string. If no Whitespace
   ** match any part of the input then the resulting array has just one element,
   ** namely the given string.
   **
   ** @param string              the string to split.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of Whitespaces.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  public static String[] split(final String string) {
    // Whitespace will be used as separator
    return split(string, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   split
  /**
   ** Splits a string around <code>separators</code>.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by any of the characters specified by
   ** <code>separators</code>. The substrings in the array are in the order in
   ** which they occur in the given string. If no character contained in
   ** <code>separators</code> match any part of the input then the resulting
   ** array has just one element, namely the given string.
   **
   ** @param string              the string to split.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param separators          the characters used as the breaking boundaries.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of characters
   **                            contained in <code>separators</code>.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  public static String[] split(final String string, final String separators) {
    if (string == null)
      return new String[0];

    final int length = string.length();
    if (length == 0)
      return new String[0];

    final List<String> results = new ArrayList<String>();
    int     i        = 0;
    int     start    = 0;
    boolean progress = false;

    if (separators == null) {
      results.addAll(Arrays.asList(string.split(" ")));
      progress = true;
      i++;
    }
    else if (separators.length() == 1) {
      results.addAll(Arrays.asList(string.split(separators)));
      progress = true;
      i++;
    }
    else {
      while (i < length) {
        if (separators.indexOf(string.charAt(i)) >= 0) {
          if (progress) {
            results.add(string.substring(start, i));
            progress = false;
          }
          start = ++i;
          continue;
        }
        progress = true;
        i++;
      }
    }

    if (progress)
      results.add(string.substring(start, i));

    return results.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   csv
  /**
   ** Splits a string around comma.
   ** <p>
   ** The array returned by this method contains each substring of the given
   ** string that is terminated by a comma. The substrings in the array are
   ** in the order in which they occur in the given string. If no comma
   ** match any part of the input then the resulting array has just one element,
   ** namely the given string.
   **
   ** @param string              the string to split.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of commas.
   **                            <br>
   **                            Possible object is array of {@link String}.
   */
  public static String[] csv(final String string) {
    // prevent bogus input else comma will be used as separator
    return (string == null) ? new String[0] : CSV.split(string.trim());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dup
  /**
   ** Create a string filled n-times with the specified character sequence.
   **
   ** @param  sequence           the character sequence.
   **                            <br>
   **                            Allowed object is {@link CharSequence}.
   ** @param  count              how often the specified character sequence
   **                            occurs in the create string.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    string filled n-times with the specified
   **                            character sequence.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String dup(final CharSequence sequence, final int count) {
    // prevent bogus input
    if (empty(sequence) || count <= 0)
      return "";

    final StringBuilder builder = new StringBuilder(sequence.length() * count);
    for (int i = 0; i < count; i++)
      builder.append(sequence);
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Builds a new string concatenating the <code>element</code>s separated by
   ** the <code>separator</code> specified.
   **
   ** @param  separator          the separator to place between consecutive
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  element            the elements to conatenate.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  public static StringBuilder concat(final Object separator, final String... element) {
    return concat(0, element.length, separator, element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Builds a new string concatenating the <code>element</code>s separated by
   ** the <code>separator</code> specified.
   **
   ** @param  <T>                the type of the elements element.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  start              the subscript of the start element.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  length             the number of elements.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  separator          the separator to place between consecutive
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  element            the elements to join.
   **                            <br>
   **                            Allowed object is array of <code>T</code>.
   **
   ** @return                    a formatted string containing
   **                            <code>delimiter</code> and
   **                            <code>element</code>.
   **                            <br>
   **                            Possible object is {@link StringBuilder}.
   */
  @SuppressWarnings("unchecked")
  public static <T> StringBuilder concat(final int start, final int length, final Object separator, final T... element) {
    final StringBuilder builder = new StringBuilder();
    if (element == null || element.length == 0 || length < 0)
      return builder;

    if (start < element.length) {
      builder.append(element[start]);
      for (int i = 1; i < length && i + start < element.length; i++) {
        builder.append(separator).append(element[i + start]);
      }
    }
    return builder;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Converts a {@link List} of strings to single string by inserting a space
   ** between each element of the list.
   **
   ** @param  list               the {@link List} of Strings to convert.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of any type.
   **
   ** @return                    a String made up of the words in the list.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String toString(final List<?> list) {
    return toString(list, BLANK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Converts a {@link List} of strings to single string by inserting  the
   ** specified <code>separator</code> between each element of the collection.
   **
   ** @param  collection         the {@link List} of Objects to convert.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of any type.
   ** @param  separator          the separator to insert between elements.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    a String made up of the words in the list.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String toString(final List<?> collection, final char separator) {
    return toString(collection, separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Converts a {@link Collection} of {@link Character}s to single string.
   **
   ** @param  collection         the {@link Collection} of {@link Character}s
   **                            to convert.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Character}.
   **
   ** @return                    the string representation of the specified
   **                            {@link Collection}.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String toString(final Collection<Character> collection) {
    final StringBuilder buffer = new StringBuilder(EMPTY);
    for (Character character : collection) {
      buffer.append(character);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Converts a {@link Collection} of strings to single string by inserting the
   ** specified <code>separator</code> between each element of the list.
   **
   ** @param  collection         the {@link Collection} of strings to convert.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link String}.
   ** @param  separator          the separator to insert between elements.
   **                            <br>
   **                            Allowed object is <code>char</code>.
   **
   ** @return                    the string representation of the specified
   **                            {@link Collection}. Each element is separated
   **                            by the specified <code>separator</code>
   **                            character.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String toString(final Collection<?> collection, final char separator) {
    final StringBuilder buffer = new StringBuilder(EMPTY);
    if (collection.size() > 0) {
      for (Object i : collection)
        buffer.append(i.toString()).append(separator);
      buffer.deleteCharAt(buffer.length() - 1);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalizeWord
  /**
   ** Capitalizes a word represented by a {@link String}. Only the first letter
   ** is changed. To convert the rest of each word to lowercase at the same
   ** time, use {@code #capitalizeAll(String)}.
   ** <br>
   ** A <code>null</code> input {@link String} returns <code>null</code>.
   **
   ** @param  word               the String to capitalize, may be
   **                            <code>null</code>
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the capitalized {@link String},
   **                            <code>null</code> if <code>word</code> is
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String capitalizeWord(final String word) {
    return empty(word) ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
  }
}