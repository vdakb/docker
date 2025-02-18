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

    File        :   StringUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation.utility;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Formatter;
import java.util.Properties;
import java.util.Dictionary;
import java.util.Collection;
import java.util.Enumeration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.identityconnectors.framework.common.objects.Attribute;

import oracle.iam.identity.icf.foundation.SystemMessage;
import oracle.iam.identity.icf.foundation.SystemConstant;

import oracle.iam.identity.icf.foundation.resource.SystemBundle;

import oracle.iam.identity.icf.foundation.logging.TableFormatter;

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

  /**
   ** Charset to be used for encoding the resulting digests.
   ** <p>
   ** The result of digesting some bytes can be any other bytes, and so the
   ** result of digesting, for example, some LATIN-1 valid String bytes, can be
   ** bytes that may not conform a "valid" LATIN-1 String.
   ** <p>
   ** Because of this, digests are always encoded in <i>BASE64</i> after being
   ** created, and this ensures that the digests will make perfectly
   ** representable, safe ASCII Strings. Because of this, the charset used to
   ** convert the digest bytes to the returned String is set to
   ** <b>US-ASCII</b>.
   */
  public static final Charset        ASCII            = StandardCharsets.US_ASCII;

  /**
   ** Encoding to be used to obtain "digestable" byte arrays from input Strings.
   ** <p>
   ** This encoding has to be fixed to some value so that we avoid problems with
   ** different platforms having different "default" charsets.
   ** <p>
   ** It is set to <b>UTF-8</b> because it covers the whole spectrum of
   ** characters representable in Java (which internally uses UTF-16), and
   ** avoids the size penalty of UTF-16 (which will always use two bytes for
   ** representing each character, even if it is an ASCII one).
   ** <p>
   ** Setting this value to UTF-8 does not mean that Strings that originally
   ** come for, for example, an ISO-8859-1 input, will not be correcly digested.
   ** It simply provides a way of "fixing" the way a String will be converted
   ** into bytes for digesting.
   */
  public static final Charset        UNICODE          = StandardCharsets.UTF_8;

  /** ISO character set used for encoding latin string. */
  public static final Charset        LATIN            = StandardCharsets.ISO_8859_1;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Align
  // ~~~~ ~~~~~
  /**
   ** Defines format patterns to align strings.
   */
  public enum Align {
      /** the encoded action values that can by applied on entitlements. */
      LEFT("%%-%ds",  "%%-%ds = %%s")
    , RIGHT("%%%ds",  "%%%ds = %%s")
    , CENTER("%%%ds", "%%%ds = %%s")
    ;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-3646406984065868422")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the format strings for this <code>Action</code>. */
    private final String      singleString;
    private final String      pairedString;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Align</code> with a single parent state.
     **
     ** @param  singleString     the format of a single string for this
     **                          <code>Align</code>.
     ** @param  pairedString     the format of a key/value pair string for this
     **                          <code>Align</code>.
     */
    Align(final String singleString, final String pairedString) {
      this.singleString = singleString;
      this.pairedString = pairedString;
    }
  }

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
   **   assert StringUtility.blank(cipher) : "Cipher transformation may not be null or empty!";
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
   ** Check to ensure that a {@code String} is not <code>null</code> or empty
   ** after trimming of leading and trailing whitespace. Usually used with
   ** assertions, as in
   ** <pre>
   **   assert StringUtility.empty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
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
   **   assert StringUtility.isEmpty(cipher, true) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  trim	             if <code>true</code>, the string is first
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
    if (lhs == null || rhs == null) {
      return (lhs == null && rhs == null);
    }
    if (lhs.length() < rhs.length()) {
      return false;
    }
    else
      return lhs.equals(rhs);
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
    if (lhs == null || rhs == null) {
      return (lhs == null && rhs == null);
    }
    if (lhs.length() < rhs.length()) {
      return false;
    }
    return lhs.equalsIgnoreCase(rhs);
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
   **                            <br>
   **                            Allowed object is <code>char</code>.
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
    if (base == null || prefix == null) {
      return (base == null && prefix == null);
    }
    if (base.length() < prefix.length()) {
      return false;
    }
    return base.startsWith(prefix);
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
   **                            <br>
   **                            Allowed object is <code>char</code>.
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
    if (base == null || suffix == null) {
      return (base == null && suffix == null);
    }
    if (base.length() < suffix.length()) {
      return false;
    }
    return base.endsWith(suffix);
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
   **                            <br>
   **                            Allowed object is <code>char</code>.
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
   **                            <br>
   **                            Allowed object is {@link String}.
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **                            <code>rhs</code>.
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
  // Method:   formatCollection
  /**
   ** Formats a {@link Map} as an output for debugging purpose.
   **
   ** @param  mapping            the {@link Map} to format for debugging output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatCollection(final Map<String, Object> mapping) {
    final StringBuilder buffer = new StringBuilder();
    for (String name : mapping.keySet()) {
      final Object value = mapping.get(name);
      if (value instanceof Map) {
        formatValuePair(buffer, name, formatCollection((Map<String, Object>)value));
      }
      else if (value instanceof List) {
        formatValuePair(buffer, name, listToString((List<Object>)value));
      }
      else if (value instanceof Dictionary) {
        formatValuePair(buffer, name, formatCollection((Dictionary<String, Object>)value));
      }
      else
        formatValuePair(buffer, name, (value == null) ? SystemConstant.NULL : value.toString());
      buffer.append(SystemConstant.LINEFEED);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Dictionary} as an output for debugging purpose.
   **
   ** @param  mapping            the {@link Dictionary} to format for debugging
   **                            output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatCollection(final Properties mapping) {
    final StringBuilder buffer = new StringBuilder();
    if (mapping.size() > 0) {
      final Enumeration<Object> i = mapping.keys();
      while (i.hasMoreElements()) {
        final String name  = (String)i.nextElement();
        final Object value = mapping.get(name);
        if (value instanceof Map) {
          buffer.append(formatCollection((Map<String, Object>)value));
        }
        else if (value instanceof List) {
          buffer.append(formatCollection((Map<String, Object>)value));
          formatValuePair(buffer, name, listToString((List<Object>)value));
        }
        else if (value instanceof Dictionary) {
          buffer.append(formatCollection((Dictionary<String, Object>)value));
        }
        else
          formatValuePair(buffer, name, (value == null) ? SystemConstant.NULL : value.toString());
        buffer.append(SystemConstant.LINEFEED);
      }
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Dictionary} as an output for debugging purpose.
   **
   ** @param  mapping            the {@link Dictionary} to format for debugging
   **                            output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatCollection(final Dictionary<String, Object> mapping) {
    final StringBuilder buffer = new StringBuilder();
    if (mapping.size() > 0) {
      final Enumeration<String> i = mapping.keys();
      while (i.hasMoreElements()) {
        final String name  = i.nextElement();
        final Object value = mapping.get(name);
        if (value instanceof Map) {
          buffer.append(formatCollection((Map<String, Object>)value));
        }
        else if (value instanceof List) {
          buffer.append(formatCollection((Map<String, Object>)value));
          formatValuePair(buffer, name, listToString((List<Object>)value));
        }
        else if (value instanceof Dictionary) {
          buffer.append(formatCollection((Dictionary<String, Object>)value));
        }
        else
          formatValuePair(buffer, name, (value == null) ? SystemConstant.NULL : value.toString());
        buffer.append(SystemConstant.LINEFEED);
      }
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Set} of {@link Attribute}s as an output for debugging
   ** purpose.
   **
   ** @param  mapping            the {@link Set} of {@link Attribute}s to format
   **                            for debugging output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatCollection(final Set<Attribute> mapping) {
    final TableFormatter table  = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.ATTRIBUTE_NAME))
    .header(SystemBundle.string(SystemMessage.ATTRIBUTE_VALUE))
    ;
    for (Attribute cursor : mapping) {
      StringUtility.formatValuePair(table, cursor.getName(), StringUtility.listToString(cursor.getValue()));         
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return SystemBundle.string(SystemMessage.ATTRIBUTE_MAPPING, buffer.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listToString
  /**
   ** Converts a {@link List} of strings to single string by inserting a space
   ** between each element of the list.
   **
   ** @param  list                the {@link List} of Strings to convert.
   **
   ** @return                     a String made up of the words in the list.
   */
  public static String listToString(final List<?> list) {
    return collectionToString(list, SystemConstant.BLANK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   listToString
  /**
   ** Converts a {@link List} of strings to single string by inserting  the
   ** specified <code>separator</code> between each element of the collection.
   **
   ** @param  collection          the {@link List} of Objects to convert.
   ** @param  separator           the separator to insert between elements.
   **
   ** @return                     a String made up of the words in the list.
   */
  public static String listToString(final List<?> collection, final char separator) {
    return collectionToString(collection, separator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectionToString
  /**
   ** Converts a {@link Collection} of {@link Character}s to single string.
   **
   ** @param  collection          the {@link Collection} of {@link Character}s
   **                             to convert.
   **
   ** @return                     the string representation of the specified
   **                             {@link Collection}.
   */
  public static String collectionToString(final Collection<Character> collection) {
    final StringBuilder buffer = new StringBuilder(SystemConstant.EMPTY);
    for (Character character : collection) {
      buffer.append(character);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collectionToString
  /**
   ** Converts a {@link Collection} of strings to single string by inserting the
   ** specified <code>separator</code> between each element of the list.
   **
   ** @param  collection          the {@link Collection} of strings to convert.
   ** @param  separator           the separator to insert between elements.
   **
   ** @return                     the string representation of the specified
   **                             {@link Collection}. Each element is separated
   **                             by the specified <code>separator</code>
   **                             character.
   */
  public static String collectionToString(final Collection<?> collection, final char separator) {
    final StringBuilder buffer = new StringBuilder(SystemConstant.EMPTY);
    if (collection.size() > 0) {
      for (Object i : collection)
        buffer.append(i.toString()).append(separator);
      buffer.deleteCharAt(buffer.length() - 1);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatValuePair
  /**
   ** Formats a tagged valued pair as an output for debugging purpose.
   **
   ** @param  buffer             the {@link StringBuilder} to receive.
   ** @param  key                the key of the tagged value pair.
   ** @param  value              the value of the tagged value pair.
   */
  public static void formatValuePair(final StringBuilder buffer, final String key, final String value) {
    final Locale    locale    = Locale.getDefault();
    final String    format    = String.format(locale, Align.LEFT.pairedString, 35);
    final Formatter formatter = new Formatter(buffer, locale);
    if (key.toUpperCase().contains("PASSWORD"))
      formatter.format(format, key, "********");
    else
      formatter.format(format, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatValuePair
  /**
   ** Formats a tagged valued pair as an output for debugging purpose.
   **
   ** @param  table              the {@link TableFormatter} to receive.
   ** @param  key                the key of the tagged value pair.
   ** @param  value              the value of the tagged value pair.
   */
  public static void formatValuePair(final TableFormatter table, final String key, final String value) {
    table.row().column(key).column(key.toUpperCase().contains("PASSWORD") ? "********" : value);
  }
}