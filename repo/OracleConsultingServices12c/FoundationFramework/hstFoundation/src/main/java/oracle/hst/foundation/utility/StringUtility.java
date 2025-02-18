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

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   StringUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Formatter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Dictionary;
import java.util.Collection;
import java.util.Enumeration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.Normalizer;

import java.io.UnsupportedEncodingException;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.logging.TableFormatter;

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

  /** system property used to specify character set. */
  public static final String         CHARSET_PROPERTY = "oracle.hst.foundation.charset";

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

  /** ASCII encoder used for all encoding methods. */
  public static final CharsetEncoder ASCII_ENCODER    = ASCII.newEncoder();

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

  /** UNICODE encoder used for all encoding methods. */
  public static final CharsetEncoder UNICODE_ENCODER  = UNICODE.newEncoder();

  /** ISO character set used for encoding latin string. */
  public static final Charset        LATIN            = StandardCharsets.ISO_8859_1;

  /** ISO encoder used for all encoding methods. */
  public static final CharsetEncoder LATIN_ENCODER    = LATIN.newEncoder();

  /** the pattern describing a valid e-Mail Address. */
  public static final String         EMAIL_PATTERN    = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  private static final String        REGEX_PATTERN    = "\\$\\{%s\\}";

  private static final Pattern       CSV_SEPARATOR    = Pattern.compile("\\s*,\\s*");

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** A mutable sequence of characters.
   ** <p>
   ** Implements a modifiable string. At any point in time it contains some
   ** particular sequence of characters, but the length and content of the
   ** sequence can be changed through certain method calls.
   ** <p>
   ** Unless otherwise noted, passing a <code>null</code> argument to a
   ** constructor or method in this class will cause a
   ** <code>NullPointerException</code> to be thrown.
   */
  public static class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final StringBuilder delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a string builder with no characters in it and an initial
     ** capacity of 16 characters.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Builder() {
      // ensure inheritance
      super();

      // initialize intsnace attributes
      this.delegate = new StringBuilder();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a string builder initialized to the contents of the specified
     ** string.
     ** <br>
     ** The initial capacity of the string builder is <code>16</code> plus the
     ** length of the string argument.
     **
     ** @param  string           the initial contents of the buffer.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Builder(final String string) {
      // ensure inheritance
      super();

      // initialize intsnace attributes
      this.delegate = new StringBuilder(string);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: appendLine
    /**
     ** Appends the specified generic instance to this character sequence.
     ** <p>
     ** The characters of the <code>T</code> argument are appended, in order,
     ** increasing the length of this sequence by the length of the argument. If
     ** <code>T</code> is <code>null</code>, then the four characters
     ** "<code>null</code>" are appended.
     ** <p>
     ** Let <i>n</i> be the length of this character sequence just prior to
     ** execution of the <code>append</code> method. Then the character at index
     ** <i>k</i> in the new character sequence is equal to the character at index
     ** <i>k</i> in the old character sequence, if <i>k</i> is less than <i>n</i>;
     ** otherwise, it is equal to the character at index <i>k-n</i> in the
     ** argument <code>T</code>.
     **
     ** @param  <T>              the expected type of the <code>t</code> to
     **                          append.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  t                a string.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public <T> Builder appendLine(final T t) {
      this.delegate.append(t).append(SystemConstant.LINEFEED);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: append
    /**
     ** Appends the specified generic instance to this character sequence.
     ** <p>
     ** The characters of the <code>T</code> argument are appended, in order,
     ** increasing the length of this sequence by the length of the argument. If
     ** <code>T</code> is <code>null</code>, then the four characters
     ** "<code>null</code>" are appended.
     ** <p>
     ** Let <i>n</i> be the length of this character sequence just prior to
     ** execution of the <code>append</code> method. Then the character at index
     ** <i>k</i> in the new character sequence is equal to the character at index
     ** <i>k</i> in the old character sequence, if <i>k</i> is less than <i>n</i>;
     ** otherwise, it is equal to the character at index <i>k-n</i> in the
     ** argument <code>T</code>.
     **
     ** @param  <T>              the expected type of the <code>t</code> to
     **                          append.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  t                a string.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the <code>Builder</code> for method chaining
     **                          purpose.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public <T> Builder append(T t) {
      this.delegate.append(t);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representing the data in this sequence.
     ** <p>
     ** A new {@link String} object is allocated and initialized to contain the
     ** character sequence currently represented by this object. This
     ** {@link String} is then returned. Subsequent changes to this sequence do
     ** not affect the contents of the {@link String}.
     **
     ** @return                  a string representation of this sequence of
     **                          characters.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return this.delegate.toString();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Align
  // ~~~~ ~~~~~
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

  private static final String         NORMALIZER_CLASS = "java.text.Normalizer";

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  /** cached system property for the line separator */
  private static String  lineSeparator = null;

  /** cached compiled pattern to validate e-Mail Addresses */
  private static Pattern emailPattern  = Pattern.compile(EMAIL_PATTERN);

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
  // Method:   lineSeparator
  /**
   ** Queries the system properties for the line separator. If access
   ** to the System properties is forbidden, the UNIX default is returned.
   **
   ** @return                    the line separator.
   */
  public static String lineSeparator() {
    try {
      if (lineSeparator == null)
        lineSeparator = System.getProperty("line.separator", SystemConstant.LINEBREAK);
    }
    catch (Exception e) {
      lineSeparator = SystemConstant.LINEBREAK;
    }
    return lineSeparator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   defaultCharset
  /**
   ** Returns the default charset used for character/byte conversions.
   ** <p>
   ** If the oracle.hst.foundation.charset system property is specified,
   ** attempts to get the named character set, otherwise returns
   ** {@link #UNICODE}.
   **
   ** @return                    the default character set to use.
   */
  public static Charset defaultCharset() {
    final String charSetName = System.getProperty(CHARSET_PROPERTY);
    if (charSetName != null) {
      return Charset.forName(System.getProperty(charSetName));
    }
    else {
      return UNICODE;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseInsensitiveHash
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
   **
   ** @return                    a hash code value for this object.
   */
  public static int caseInsensitiveHash(final String subject) {
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
   **
   ** @return                    a hash code value for this object.
   */
  public static int hash(final String subject) {
    return subject.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isBlank
  /**
   ** Check to ensure that a <code>String</code> is not <code>blank</code> after
   ** trimming of leading and trailing whitespace. Usually used with assertions,
   ** as in
   ** <pre>
   **   assert StringUtility.isBlank(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   ** <pre>
   **  StringUtility.isBlank(null)                = true
   **  StringUtility.isBlank(&quot;&quot;)        = true
   **  StringUtility.isBlank(&quot; &quot;)       = true
   **  StringUtility.isBlank(&quot;bob&quot;)     = false
   **  StringUtility.isBlank(&quot;  bob  &quot;) = false
   ** </pre>
   **
   ** @param  subject            String to be tested for blank.
   **
   ** @return                    <code>true</code> if the subject is just
   **                            blank.
   */
  public static boolean isBlank(final String subject) {
    return (subject == null) ? true : isEmpty(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty
  /**
   ** Check to ensure that a <code>String</code> is not <code>null</code> or
   ** empty after trimming of leading and trailing whitespace. Usually used with
   ** assertions, as in
   ** <pre>
   **   assert StringUtility.isEmpty(cipher) : "Cipher transformation may not be null or empty!";
   ** </pre>
   **
   ** @param  subject            String to be tested for emptiness.
   **
   ** @return                    <code>true</code> if the subject is
   **                            <code>null</code>, equal to the "" null string
   **                            or just blanks.
   */
  public static boolean isEmpty(final String subject) {
    return isEmpty(subject, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty
  /**
   ** Check to ensure that a <code>String</code> is not <code>null</code> or
   ** empty (after optional trimming of leading and trailing whitespace).
   ** Usually used with assertions, as in
   ** <pre>
   **   assert StringUtility.isEmpty(cipher, true) : "Cipher transformation may not be null or empty!";
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
  public static boolean isEmpty(final String subject, final boolean trim) {
    return (trim) ? (subject == null || subject.trim().length() == 0) : (subject == null || subject.length() == 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmail
  /**
   ** The combination means, email address must start with "_A-Za-z0-9-\\+",
   ** optional follow by ".[_A-Za-z0-9-]", and end with a "@" symbol.
   ** <br>
   ** The email's domain name must start with "A-Za-z0-9-", follow by first
   ** level Tld (.com, .net) ".[A-Za-z0-9]" and optional follow by a second
   ** level Tld (.com.au, .com.my) "\\.[A-Za-z]{2,}", where second level Tld
   ** must start with a dot "." and length must equal or more than 2 characters.
   ** <pre>
   **   assert !StringUtility.isEmail(cipher) : "Not a valid e-Mail Address!";
   ** </pre>
   **
   ** @param  subject            String to be tested.
   **
   ** @return                    <code>true</code> if the subject is a valid
   **                            e-Mail Address.
   */
  public static boolean isEmail(final String subject) {
		final Matcher matcher = emailPattern.matcher(subject);
		return matcher.matches();
   }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPureAscii
  /**
   ** Ensure the string contains only ascii characters.
   **
   ** @param  subject            string to test.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            ascii characters.
   */
  public static boolean isPureAscii(final String subject) {
    return isPure(subject, ASCII_ENCODER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPureLatin
  /**
   ** Ensure the string contains only ISO-8859-1 characters.
   **
   ** @param  subject            string to test.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            ISO-8859-1 characters.
   */
  public static boolean isPureLatin(final String subject) {
    return isPure(subject, LATIN_ENCODER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPure
  /**
   ** Ensure the string contains only characters of the specified character set
   ** encoder.
   **
   ** @param  subject            string to test.
   ** @param  encoder            the {@link CharsetEncoder} used to check if all
   **                            characters of the provided string fits in the
   **                            character set.

   ** @return                    <code>true</code> if subject is formed only by
   **                            characters supported by the specified
   **                            {@link CharsetEncoder}.
   */
  public static boolean isPure(final String subject, final CharsetEncoder encoder) {
    return encoder.canEncode(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isPrintableAscii
  /**
   ** Ensure the string contains only printable ascii characters.
   **
   ** @param  subject            string to test.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            printable ascii characters.
   */
  public static boolean isPrintableAscii(final String subject) {
    if (subject == null)
      return false;

    final int size = subject.length();
    for (int i = 0; i < size; i++) {
      if (!CharacterUtility.isAsciiPrintable(subject.charAt(i)))
        return false;
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isLegal
  /**
   ** Ensure the string contains only legal characters.
   **
   ** @param  subject            string to test.
   ** @param  charSet            characters than are legal for subject.
   **
   ** @return                    <code>true</code> if subject is formed only of
   **                            chars from the legal set.
   */
  public static boolean isLegal(final String subject, final String charSet) {
    for (int i = 0; i < subject.length(); i++)
      if (charSet.indexOf(subject.charAt(i)) < 0)
        return false;

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEqual
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  a                  the first String to be tested for equality.
   ** @param  b                  the second String to be tested for equality.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean isEqual(final String a, final String b) {
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
  // Method:   isCaseInsensitiveEqual
  /**
   ** Compares two strings if they are lexicographically identically by ignoring
   ** case sensitivity
   **
   ** @param  a                  the first String to be tested for equality.
   ** @param  b                  the second String to be tested for equality.
   **
   ** @return                    <code>true</code> if the strings are
   **                            caseinsensitve lexicographically identically;
   **                            otherwise <code>false</code>.
   */
  public static boolean isCaseInsensitiveEqual(final String a, final String b) {
    if (a == b)
      return true;
    else if (a == null && b == null)
      return true;
    else if ((a == null && b != null) || (a != null && b == null))
      return false;
    else
      return isEqual(a.toUpperCase(LocaleRegistry.instance()), b.toUpperCase(LocaleRegistry.instance()));
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
   ** exactly when the {@link #isEqual(String, String)} method would return
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
   **
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
   ** exactly when the {@link #isEqual(String, String)} method would return
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
   **
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
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of Whitespaces.
   */
  public static String[] split(final String string) {
    // Whitespace will be used as separator
    return split(string, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   splitCSV
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
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of commas.
   */
  public static String[] splitCSV(final String string) {
    // comma will be used as separator
    return CSV_SEPARATOR.split(string.trim());
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
   ** @param separators          the characters used as the breaking boundaries.
   **
   ** @return                    the array of strings computed by splitting the
   **                            given string around matches of characters
   **                            contained in <code>separators</code>.
   */
  public static String[] split(final String string, final String separators) {
    if (string == null)
      return new String[0];

    final int length = string.length();
    if (length == 0)
      return new String[0];

    final List<String> results = new ArrayList<String>();
    int i = 0;
    int start = 0;
    boolean tokenInProgress = false;

    if (separators == null) {
      while (i < length) {
        if (Character.isWhitespace(string.charAt(i))) {
          if (tokenInProgress) {
            results.add(string.substring(start, i));
            tokenInProgress = false;
          }
          start = ++i;
          continue;
        }
        tokenInProgress = true;
        i++;
      }
    }
    else if (separators.length() == 1) {
      final char separator = separators.charAt(0);
      while (i < length) {
        if (string.charAt(i) == separator) {
          if (tokenInProgress) {
            results.add(string.substring(start, i));
            tokenInProgress = false;
          }
          start = ++i;
          continue;
        }
        tokenInProgress = true;
        i++;
      }
    }
    else {
      while (i < length) {
        if (separators.indexOf(string.charAt(i)) >= 0) {
          if (tokenInProgress) {
            results.add(string.substring(start, i));
            tokenInProgress = false;
          }
          start = ++i;
          continue;
        }
        tokenInProgress = true;
        i++;
      }
    }

    if (tokenInProgress)
      results.add(string.substring(start, i));

    return results.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   breakString
  /**
   ** Breaks a String along word boundarys to the specified maximum length.
   ** <br>
   ** This method returns an array of two strings:
   ** <br>
   ** The first String is a line, of length less than <code>width</code>. If
   ** this String is not the entire passed String, it will be terminated with a
   ** newline.
   ** <br>
   ** The second String is the remainder of the original String. If the original
   ** String had been broken on a space (as opposed to a newline that had been
   ** in the original String) all leading spaces will have been removed. If
   ** there is no remainder, null is returned as the second String and no
   ** newline will have been appended to the first String.
   **
   ** @param  string             the String to be broken.
   **                            If null, will be converted to an empty string.
   ** @param  width              the maximum line length of the first returned
   **                            string.
   **
   ** @return                    see the method description
   */
  @SuppressWarnings("oracle.jdeveloper.java.nested-assignment")
  public static String[] breakString(String string, final int width) {
    string = (string == null) ? SystemConstant.EMPTY : string;

    StringBuilder line      = null;
    StringBuilder remainder = null;
    int           linebreak = string.indexOf('\n');
    if (linebreak != -1 && linebreak <= width) {
      linebreak++;  // point to next char
      line.append(string.substring(0, linebreak));
      if (linebreak < string.length())
        remainder.append(string.substring(linebreak));
    }
    else if (string.length() <= width)
      line.append(string);
    else if ((linebreak = string.lastIndexOf(SystemConstant.BLANK, width)) != -1) {
      line.append(string.substring(0, linebreak));
      while (linebreak < string.length() && string.charAt(linebreak) == SystemConstant.BLANK)
        linebreak++;

      if (linebreak < string.length()) {
        line.append(SystemConstant.LINEFEED);
        remainder.append(string.substring(linebreak));
      }
    }
    else {
      line.append(string.substring(0, width)).append(SystemConstant.LINEFEED);
      remainder.append(string.substring(width));
    }
    return new String[] { line.toString(), remainder.toString() };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   similar
  /**
   ** Calculate the Edit Distance between 2 Strings as a measure of similarity.
   ** <p>
   ** For example, if the strings GUMBO and GAMBOL are passed in, the edit
   ** distance is 2, since GUMBO transforms into GAMBOL by replacing the 'U'
   ** with an 'A' and adding an 'L'.
   ** <p>
   ** Original Implementation of this algorithm by Michael Gilleland, adapted by
   ** Chas Emerick for the Apache-Commons project
   ** http://www.merriampark.com/ldjava.htm
   **
   ** @param  lhs                the source string.
   ** @param  rhs                the target String.
   **
   ** @return                    the edit distance between the 2 strings.
   */
  public static int similar(final String lhs, final String rhs) {
    // prevent bogus input
    if (lhs == null || rhs == null)
      throw new IllegalArgumentException("Strings must not be null");

    int n = lhs.length(); // length of lhs
    int m = rhs.length(); // length of rhs

    if (n == 0)
      return m;
    else if (m == 0)
      return n;

    // 'previous' cost array, horizontally
    int p[] = new int[n + 1];
    // 'previous' cost array, horizontally
    int d[] = new int[n + 1];
    // placeholder to assist in swapping p and d
    int _d[];

    // indexes into strings s and t
    int i; // iterates through s
    int j; // iterates through t

    char t_j; // jth character of t

    int cost; // cost

    for (i = 0; i <= n; i++)
      p[i] = i;

    for (j = 1; j <= m; j++) {
      t_j = rhs.charAt(j - 1);
      d[0] = j;

      for (i = 1; i <= n; i++) {
        cost = lhs.charAt(i - 1) == t_j ? 0 : 1;
        // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
        d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
      }

      // copy current distance counts to 'previous row' distance counts
      _d = p;
      p = d;
      d = _d;
    }

    // our last action in the above loop was to switch d and p, so p now
    // actually has the most recent cost counts
    return p[n];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatMapping
  /**
   ** Formats a {@link Map} as an output for debugging purpose.
   **
   ** @param  mapping            the {@link Map} to format for debugging output.
   **
   ** @return                    the formatted string representation
   */
  public static String formatMapping(final Map<String, String> mapping) {
    final TableFormatter table  = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.MAPPING_NAME))
    .header(SystemBundle.string(SystemMessage.MAPPING_VALUE))
    ;
    for (Map.Entry<String, String> cursor : mapping.entrySet()) {
      formatValuePair(table, cursor.getKey(), cursor.getValue());
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return buffer.toString();
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
  @SuppressWarnings({"unchecked", "cast"})
  public static String formatCollection(final Map<String, Object> mapping) {
    final TableFormatter table  = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.MAPPING_NAME))
    .header(SystemBundle.string(SystemMessage.MAPPING_VALUE))
    ;
    for (String name : mapping.keySet()) {
      final Object value = mapping.get(name);
      if (value instanceof Map) {
        formatValuePair(table, name, formatCollection((Map<String, Object>)value));
      }
      else if (value instanceof List) {
        formatValuePair(table, name, listToString((List<Object>)value));
      }
      else if (value instanceof Dictionary) {
        formatValuePair(table, name, formatCollection((Dictionary<String, Object>)value));
      }
      else
        formatValuePair(table, name, (value == null) ? SystemConstant.NULL : value.toString());
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   decodeUnicode
  /**
   ** Decodes all unicode characters
   **
   ** @param  encoded            the {@link String} that may be contain unicodes.
   **
   ** @return                    the decoded String
   */
  public static String decodeUnicode(final String encoded) {
    if (!containsUnicode(encoded))
      return encoded;

    StringBuilder builder  = new StringBuilder();
    StringBuilder escaping = new StringBuilder();
    StringBuilder number   = new StringBuilder();

    int state = 0;
    for (int i = 0; i < encoded.length(); i++) {
      char a = encoded.charAt(i);
      switch (state) {
        case 0  : switch (a) {
                    case CharacterUtility.UNICODE_ESCAPE_1 : state = 1;
                                                             escaping.append(a);
                                                             break;
                    default                                : builder.append(a);
                  }
                  break;
        case 1  : switch (a) {
                    case CharacterUtility.UNICODE_ESCAPE_1 : state = 0;
                                                             builder.append(CharacterUtility.UNICODE_ESCAPE_1);
                                                             break;
                    case CharacterUtility.UNICODE_ESCAPE_2 : state = 2;
                                                             escaping.append(a);
                                                             break;
                    default                                : state = 0;
                                                             builder.append(escaping.toString());
                                                             builder.append(a);
                                                             escaping.setLength(0);
                  }
                  break;
        case 2  : if ((CharacterUtility.isDigit(a)) || ((a > '@') && (a < 'G')) || ((a > '`') && (a < 'g'))) {
                    escaping.append(a);
                    number.append(a);
                    if (number.length() != 4)
                      continue;

                    char unicode = (char)Integer.parseInt(number.toString(), 16);
                    builder.append(unicode);
                    escaping.setLength(0);
                    number.setLength(0);
                    state = 0;
                  }
                  else {
                    builder.append(escaping.toString());
                    builder.append(a);
                    escaping.setLength(0);
                    number.setLength(0);
                    state = 0;
                  }
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsUnicode
  /**
   ** Checjs if the given string cotains unicode sequences
   **
   ** @param  encoded            the {@link String} that may be contain unicode
   **                            seqeunces.
   **
   ** @return                    <code>true</code> if the given string contains
   **                            unicode seqeunces; otherwise
   **                            <code>false</code>.
   */
  public static boolean containsUnicode(final String encoded) {
    if ((encoded != null) && (encoded.length() > 5)) {
      int state = 0;
      for (int i = 0; i < encoded.length(); i++) {
        char a = encoded.charAt(i);
        switch (state) {
          case 0  : switch (a) {
                      case CharacterUtility.UNICODE_ESCAPE_1 : state = 1;
                    }
                    break;
          case 1  : switch (a) {
                      case CharacterUtility.UNICODE_ESCAPE_1 : return true;
                      case CharacterUtility.UNICODE_ESCAPE_2 : state = 2;
                                                               break;
                      default                                : state = 0;
                    }
                    break;
          default : if ((CharacterUtility.isDigit(a)) || ((a > '@') && (a < 'G')) || ((a > '`') && (a < 'g'))) {
                      state++;
                      if (state != 6)
                        continue;
                      return true;
                    }
                    else {
                      state = 0;
                    }
        }
      }
    }
    return false;
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
  @SuppressWarnings({"unchecked", "cast"})
  public static String formatCollection(final Properties mapping) {
    final TableFormatter table  = new TableFormatter()
    .header(SystemBundle.string(SystemMessage.PROPERTY_NAME))
    .header(SystemBundle.string(SystemMessage.PROPERTY_VALUE))
    ;
    if (mapping.size() > 0) {
      final Enumeration<Object> i = mapping.keys();
      while (i.hasMoreElements()) {
        final String name  = (String)i.nextElement();
        final Object value = mapping.get(name);
        if (value instanceof Map) {
          formatValuePair(table, name, formatCollection((Map<String, Object>)value));
        }
        else if (value instanceof List) {
          formatValuePair(table, name, listToString((List<Object>)value));
        }
        else if (value instanceof Dictionary) {
          formatValuePair(table, name, formatCollection((Dictionary<String, Object>)value));
        }
        else
          formatValuePair(table, name, (value == null) ? SystemConstant.NULL : value.toString());
      }
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
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
  @SuppressWarnings({"unchecked", "cast"})
  public static String formatCollection(final Dictionary<String, Object> mapping) {
    final TableFormatter table = new TableFormatter();
    if (mapping.size() > 0) {
      final Enumeration<String> i = mapping.keys();
      while (i.hasMoreElements()) {
        final String name  = i.nextElement();
        final Object value = mapping.get(name);
        if (value instanceof Map) {
          formatValuePair(table, name, formatCollection((Map<String, Object>)value));
        }
        else if (value instanceof List) {
          formatValuePair(table, name, listToString((List<Object>)value));
        }
        else if (value instanceof Dictionary) {
          formatValuePair(table, name, formatCollection((Dictionary<String, Object>)value));
        }
        else
          formatValuePair(table, name, (value == null) ? SystemConstant.NULL : value.toString());
      }
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return buffer.toString();
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
  public static void formatValuePair(final TableFormatter table, final String key, final boolean value) {
    formatValuePair(table, key, Boolean.toString(value));
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
  public static void formatValuePair(final TableFormatter table, final String key, final int value) {
    formatValuePair(table, key, String.valueOf(value));
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
  public static void formatValuePair(final TableFormatter table, final String key, final long value) {
    formatValuePair(table, key, Long.toString(value));
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:   alignText
  /**
   ** Print the given string left or right adjusted and appends the result to
   ** the given StringBuilder.
   **
   ** @param  buffer             the {@link StringBuilder} receiving the result.
   ** @param  text               the text to print alligned.
   ** @param  width              the width of the field to align the
   **                            <code>text</code>.
   ** @param  align              the advice how the specified <code>text</code>
   **                            is aligned in the output.
   */
  public static void alignText(final StringBuilder buffer, final String text, int width, final Align align) {
   final String    format    = String.format(Locale.getDefault(), align.singleString, width);
   final Formatter formatter = new Formatter(buffer, Locale.getDefault());
   formatter.format(format, text);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftIndent
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to indent.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String leftIndent(final String input, final int length) {
    return leftIndent(input, SystemConstant.BLANK, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftIndent
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to indent.
   ** @param  indentChar         the character to indent with.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String leftIndent(final String input, final char indentChar, final int length) {
    final StringBuilder buffer = new StringBuilder(input.length() + length);
    fillBufferWith(buffer, indentChar, length);
    buffer.append(input);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   left
  /**
   ** Returns the substring to the left of the specified substring in the
   ** specified String, starting from the left.
   **
   ** @param  source             the source String to search.
   ** @param  search             the substring to search for in
   **                            <code>source</code>.
   **
   ** @return                    the substring that is to the left of
   **                            <code>search</code> in <code>source</code>.
   */
  public static String left(final String source, final String search) {
    int index = source.indexOf(search);
    return (index < 0) ? SystemConstant.EMPTY : source.substring(0, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftBack
  /**
   ** Returns the substring to the left of the specified substring in the
   ** specified String, starting from the right.
   **
   ** @param  source             the source String to search.
   ** @param  search             the substring to search for in
   **                            <code>source</code>.
   **
   ** @return                    the substring that is to the left of
   **                            <code>search</code> in <code>source</code>,
   **                            starting from the right.
   */
  public static String leftBack(final String source, final String search) {
    int index = source.lastIndexOf(search);
    return (index < 0) ? SystemConstant.EMPTY : source.substring(0, index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftPad
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to pad.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String leftPad(final String input, final int length) {
    return leftPad(input, SystemConstant.BLANK, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   leftPad
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to pad.
   ** @param  paddingChar        the character to pad with.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String leftPad(final String input, final char paddingChar, final int length) {
    final StringBuilder buffer = new StringBuilder(input.length() + length);
    buffer.append(input);
    fillBufferWith(buffer, paddingChar, length - input.length());
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   right
  /**
   ** Returns the substring to the right of the specified substring in the
   ** specified String, starting from the left.
   **
   ** @param  source             the source String to search.
   ** @param  search             the substring to search for in
   **                            <code>source</code>.
   **
   ** @return                    the substring that is to the right of
   **                            <code>search</code> in <code>source</code>.
   */
  public static String right(final String source, final String search) {
    final int index = source.indexOf(search) + search.length();
    return (index < 0) ? SystemConstant.EMPTY : source.substring(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rightBack
  /**
   ** Returns the substring to the right of the specified substring in the
   ** specified String, starting from the right.
   **
   ** @param  source             the source String to search.
   ** @param  search             the substring to search for in
   **                            <code>source</code>.
   **
   ** @return                    the substring that is to the right of
   **                            <code>search</code> in <code>source</code>,
   **                            starting from the right.
   */
  public static String rightBack(final String source, final String search) {
    int index = source.lastIndexOf(search) + search.length();
    return (index < 0) ? SystemConstant.EMPTY : source.substring(index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rightPad
  /**
   ** Padding a string to a passed length.
   **
   ** @param  input              the string to pad.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String rightPad(final String input, final int length) {
    return rightPad(input, SystemConstant.BLANK, length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rightPad
  /**
   ** Right justify a string to a given length filling with the passed character.
   **
   ** @param  input              the string to justify.
   ** @param  paddingChar        the character to pad with.
   ** @param  length             the length to pad to.
   **
   ** @return                    the padded input string.
   */
  public static String rightPad(final String input, final char paddingChar, final int length) {
    final StringBuilder buffer = new StringBuilder(input.length() + length);
    fillBufferWith(buffer, paddingChar, length - input.length());
    buffer.append(input);
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   middle
  /**
   ** Returns the substring between two substrings.
   ** <br>
   ** I.e. StringUtility.middle("This i a big challenge", "a", "challenge")
   ** returns " big ".
   **
   ** @param  source             the String to search.
   ** @param  start              the String to the left to search for, from the
   **                            left.
   ** @param  end                the String to the right to search for, from the
   **                            right.
   **
   ** @return                    the substring including the characters at
   **                            <code>start</code> and <code>end</code> of the
   **                            string <code>source</code>.
   */
  public static String middle(final String source, final String start, final String end) {
    String one = StringUtility.right(source, start);
    return StringUtility.leftBack(one, end);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   middle
  /**
   ** Returns a substring of a String, starting from specified index and with
   ** specified length.
   ** <br>
   ** I.e. StringUtility.middle("This is a big challenge", 5, 6)
   ** returns " is a ".
   **
   ** @param  source             the String to get a substring from.
   ** @param  start              the index in <code>source</code> String to get
   **                            the substring from.
   ** @param  length             the length of the substring to return.
   **
   ** @return                    the substring including the characters at
   **                            <code>start</code> and <code>length</code> of
   **                            the string <code>source</code>.
   */
  public static String middle(final String source, final int start, final int length) {
    return source.substring(start, source.length() - length);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalizeAll
  /**
   ** Converts all the delimiter separated words in a String into capitalized
   ** words, that is each word is made up of a titlecase character and then a
   ** series of lowercase characters. Capitalization uses the unicode title
   ** case, normally equivalent to upper case.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.
   ** <p>
   ** Whitespace is defined by {@link Character#isWhitespace(char)}.
   **
   ** @param  sentence           the String to capitalize, may be null
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String capitalizeAll(String sentence) {
    return capitalizeAll(sentence, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalizeAll
  /**
   ** Converts all the delimiter separated words in a String into capitalized
   ** words, that is each word is made up of a titlecase character and then a
   ** series of lowercase characters. Capitalization uses the unicode title
   ** case, normally equivalent to upper case.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.
   ** <p>
   ** The delimiters represent a set of characters understood to separate words.
   ** The first string character and the first non-delimiter character after a
   ** delimiter will be capitalized.
   **
   ** @param  sentence           the String to capitalize, may be null
   ** @param  delimiters         set of characters to determine capitalization,
   **                            null means whitespace.
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String capitalizeAll(String sentence, char[] delimiters) {
    int delimLen = (delimiters == null ? -1 : delimiters.length);
    if (isEmpty(sentence) || delimLen == 0)
      return sentence;

    sentence = sentence.toLowerCase();
    return capitalize(sentence, delimiters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalize
  /**
   ** Capitalizes all the whitespace separated words in a String. Only the first
   ** letter of each word is changed. To convert the rest of each word to
   ** lowercase at the same time, use {@link #capitalizeAll(String)}.
   ** <p>
   ** Whitespace is defined by {@link Character#isWhitespace(char)}.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.</p>
   **
   ** @param  sentence           the String to capitalize, may be null
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String capitalize(String sentence) {
    return capitalize(sentence, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalize
  /**
   ** Capitalizes all the whitespace separated words in a String. Only the first
   ** letter of each word is changed. To convert the rest of each word to
   ** lowercase at the same time, use {@link #capitalizeAll(String)}.
   ** <p>
   ** Whitespace is defined by {@link Character#isWhitespace(char)}.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.</p>
   **
   ** @param  sentence           the String to capitalize, may be null
   ** @param  delimiters         set of characters to determine capitalization,
   **                            null means whitespace.
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String capitalize(String sentence, char[] delimiters) {
    int delimLen = (delimiters == null ? -1 : delimiters.length);
    if (isEmpty(sentence) || delimLen == 0)
      return sentence;

    char[]  chars = sentence.toLowerCase().toCharArray();
    boolean next  = false;
    for (int i = 0; i < chars.length; i++) {
      if (i == 0)
        chars[i] = (Character.toTitleCase(chars[i]));
      else if (CharacterUtility.isDelimiter(chars[i], delimiters))
        next = true;
      else if (next) {
        chars[i] = (Character.toTitleCase(chars[i]));
        next = false;
      }
    }
    return String.valueOf(chars);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uncapitalize
  /**
   ** Uncapitalizes all the whitespace separated words in a String. Only the
   ** first letter of each word is changed.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.
   ** <p>
   ** For a word based algorithm, see {@link #uncapitalize(String)}.
   **
   ** @param  sentence           the String to uncapitalize, may be null.
   **
   ** @return                    the uncapitalized String, <code>null</code> if
   **                            null String input.
   **
   ** @see    #uncapitalize(String)
   ** @see    #capitalize(String)
   */
  public static String uncapitalize(final String sentence) {
    return uncapitalize(sentence, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   uncapitalize
  /**
   ** Uncapitalizes all the whitespace separated words in a String. Only the
   ** first letter of each word is changed.
   ** <br>
   ** A <code>null</code> input String returns <code>null</code>.
   ** <p>
   ** The delimiters represent a set of characters understood to separate words.
   ** The first string character and the first non-delimiter character after a
   ** delimiter will be uncapitalized.
   **
   ** @param  sentence           the String to uncapitalize, may be null.
   ** @param  delimiters         the characters used a s word boundaries.
   **
   ** @return                    the uncapitalized String, <code>null</code> if
   **                            null String input.
   */
  public static String uncapitalize(String sentence, char[] delimiters) {
    int delimLen = (delimiters == null ? -1 : delimiters.length);
    if (isEmpty(sentence) || delimLen == 0)
      return sentence;

    int     length =  sentence.length();
    char[]  chars  = sentence.toCharArray();
    boolean next   = false;
    for (int i = 0; i < length; i++) {
      if (i == 0)
        chars[i] = (Character.toLowerCase(chars[i]));
      else if (CharacterUtility.isDelimiter(chars[i], delimiters))
        next = true;
      else if (next) {
        chars[i] = (Character.toLowerCase(chars[i]));
        next = false;
      }
    }
    return String.valueOf(chars);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Replaces several substrings in a string.
   **
   ** @param  source             the subject of invocation.
   ** @param  patterns           the string pattern that has to be replaced.
   **                            with <code>replace</code>.
   ** @param  replace            by what has each occurence of
   **                            <code>pattern</code> to be replaced.
   **
   ** @return                    the string with the replaced pattern.
   */
  public static String replace(final String source, final String[] patterns, final String replace) {
    for (int i = 0; i < patterns.length; i++)
      StringUtility.replace(source, patterns[i], replace);

    return source;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Replace all occurences of <code>pattern</code> in <code>source</code>
   ** with the given string <code>replace</code>.
   **
   ** @param  source             the subject of invocation.
   ** @param  pattern            the string pattern that has to be replaced.
   **                            with <code>replace</code>.
   ** @param  replace            by what has each occurence of
   **                            <code>pattern</code> to be replaced.
   **
   ** @return                    the string with the replaced pattern.
   */
  public static String replace(final String source, final String pattern, final String replace) {
    // nothing to do if the source is empty
    // we cannot check by isEmpty doe to the fact that an empty string might
    // also be subject of a replacement
    if (source == null)
      return SystemConstant.EMPTY;

    // iterate over the source and replace all occurences of pattern with
    // the given string
    int          start  = 0;
    int          length = pattern.length();
    int          found  = source.indexOf(pattern, start);
    final StringBuilder buffer = new StringBuilder();
    while (found != -1) {
      buffer.append(source.substring(start, found));
      buffer.append(replace);
      start = found + length;
      found = source.indexOf(pattern, start);
    }
    buffer.append(source.substring(start));
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace
  /**
   ** Simple variable replacement internally using regular expressions.
   ** <pre>
   **   String o = &quot;Some string with a ${variable} in it.&quot;
   **   String n = replace(o, &quot;variable&quot;, &quot;something&quot;);
   **   String r = &quot;Some string with a something in it&quot;
   **   assert r.equals(n);
   ** </pre>
   ** @param  origin             the original string to do the replacement on.
   ** @param  var                string representation of the variable to
   **                            replace.
   ** @param  value              value to replace the variable with.
   **
   ** @return                    string will all the variables replaced with the
   **                            value.
   **
   ** @throws IllegalArgumentException if o is <code>null</code>,
   **                                  <code>var</code> is blank, or
   **                                  <code>val</code> is <code>null</code>.
   */
  public static String replaceVariable(final String origin, final String var, final String value) {
    try {
      if (isBlank(origin) || isBlank(var) || value == null)
        throw new IllegalArgumentException();

      final String regex = String.format(REGEX_PATTERN, var);
      final String match = Matcher.quoteReplacement(value);
      return origin.replaceAll(regex, match);
    }
    catch (RuntimeException e) {
      // catch from reqex too..
      final StringBuilder builder = new StringBuilder();
      builder.append(" var: ").append(var);
      builder.append(" value: ").append(value);
      builder.append(" origin: ").append(origin);
      throw new IllegalArgumentException(builder.toString());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reverse
  /**
   ** Returns the given inpurt in reverse order of the containing characters.
   **
   ** @param  input              the String that revert.
   **
   ** @return                    the reverted String
   */
  public static String reverse(final String input) {
    // the easiest way to reverse a given string is to use reverse()
    // method of java StringBuilder class.
    // reverse() method returns the StringBuilder object so we need to
    // cast it back to String using toString() method of StringBuilder
    return new StringBuilder(input).reverse().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substringBefore
  /**
   ** Returns the substring before the first occurrence of a separator.
   ** <p>
   ** The separator is not returned.
   ** <p>
   ** A <code>null</code> string input will return <code>null</code>. An empty
   ** ("") string input will return the empty string. A <code>null</code>
   ** separator will return the empty string if the input string is not
   ** <code>null</code>.
   **
   ** <pre>
   **   StringUtility.substringAfter(null, *)      = null
   **   StringUtility.substringAfter("", *)        = ""
   **   StringUtility.substringAfter(*, null)      = ""
   **   StringUtility.substringAfter("abc", "a")   = ""
   **   StringUtility.substringAfter("abcba", "b") = "a"
   **   StringUtility.substringAfter("abc", "c")   = "ab"
   **   StringUtility.substringAfter("abc", "d")   = "abc"
   **   StringUtility.substringAfter("abc", "")    = ""
   ** </pre>
   **
   ** @param  string             the String to get a substring from, may be
   **                            <code>null</code>.
   ** @param  separator          the String to search for, may be
   **                            <code>null</code>
   **
   ** @return                    the substring before the first occurrence of
   **                            the separator, <code>null</code> if
   **                            <code>null</code> String input.
   */
  public static String substringBefore(final String string, final String separator) {
    if (isEmpty(string))
      return string;

    if (isEmpty(separator))
      return SystemConstant.EMPTY;


    final int pos = string.indexOf(separator);
    if (pos == -1)
      return string;

    return string.substring(0, pos);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substringAfter
  /**
   ** Returns the substring after the first occurrence of a separator.
   ** <p>
   ** The separator is not returned.
   ** <p>
   ** A <code>null</code> string input will return <code>null</code>. An empty
   ** ("") string input will return the empty string. A <code>null</code>
   ** separator will return the empty string if the input string is not
   ** <code>null</code>.
   **
   ** <pre>
   **   StringUtility.substringAfter(null, *)      = null
   **   StringUtility.substringAfter("", *)        = ""
   **   StringUtility.substringAfter(*, null)      = ""
   **   StringUtility.substringAfter("abc", "a")   = "bc"
   **   StringUtility.substringAfter("abcba", "b") = "cba"
   **   StringUtility.substringAfter("abc", "c")   = ""
   **   StringUtility.substringAfter("abc", "d")   = ""
   **   StringUtility.substringAfter("abc", "")    = "abc"
   ** </pre>
   **
   ** @param  string             the String to get a substring from, may be
   **                            <code>null</code>.
   ** @param  separator          the String to search for, may be
   **                            <code>null</code>
   **
   ** @return                    the substring after the first occurrence of the
   **                            separator, <code>null</code> if
   **                            <code>null</code> String input.
   */
  public static String substringAfter(final String string, final String separator) {
    if (isEmpty(string))
      return string;

    if (separator == null)
      return SystemConstant.EMPTY;

    int pos = string.indexOf(separator);
    if (pos == -1)
      return SystemConstant.EMPTY;

    return string.substring(pos + separator.length());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseWhitespace
  /**
   ** Returns a copy of the string, with leading and trailing whitespace
   ** omitted (trim). Any further occurence of a whitespace character sequence
   ** will be replaced with one space character (collapse).
   ** <p>
   ** If the String object represents an empty character sequence, or the first
   ** and last characters of character sequence represented by ths String object
   ** both have codes greater than '\u0020' (the space character), then a
   ** reference to a String object is returned where all other whitespaces are
   ** collapsed to excatly one space character.
   **
   ** @param  input              the string inpute to trim and collapse.
   **
   ** @return                    a copy of the string with leading and trailing
   **                            white space removed and all multiple occurences
   **                            of whitespaces substituted by obe space
   **                            character.
   */
  public final static String collapseWhitespace(final String input) {
    // prevent bogus input
    if (isEmpty(input))
      return input;

    int length = input.length();
    // most of the texts are already in the collapsed form.
    // so look for the first whitespace in the hope that we will
    // never see it.
    int pos    = 0;
    while(pos < length) {
      if (CharacterUtility.isWhitespace(input.charAt(pos)))
        break;
      pos++;
    }
    if (pos == length)
      // the input happens to be already collapsed.
      return input;

    // we now know that the input contains spaces.
    // let's sit down and do the collapsing normally.
    return collapseWhitespace(new StringBuilder(input));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   collapseWhitespace
  /**
   ** Returns a copy of the string, with leading and trailing whitespace
   ** omitted (trim). Any further occurence of a whitespace character sequence
   ** will be replaced with one space character (collapse).
   ** <p>
   ** If the String object represents an empty character sequence, or the first
   ** and last characters of character sequence represented by ths String object
   ** both have codes greater than '\u0020' (the space character), then a
   ** reference to a String object is returned where all other whitespaces are
   ** collapsed to excatly one space character.
   **
   ** @param  input              the string inpute to trim and collapse.
   **
   ** @return                    a copy of the string with leading and trailing
   **                            white space removed and all multiple occurences
   **                            of whitespaces substituted by obe space
   **                            character.
   */
  public final static String collapseWhitespace(final StringBuilder input) {
    // prevent bogus input
    if (input.length() == 0)
      return SystemConstant.EMPTY;

    // the character pointing to the last scanned non-whitespace character in
    // the input
    char last = input.charAt(0);
    // loop to trim leading whitespaces
    int  start;
    for (start = 0; start < input.length(); start++) {
      last = input.charAt(start);
      if (!Character.isWhitespace(last))
        break;
    }
    // loop to trim trailing whitespaces
    int  end;
    for (end = input.length(); end > start; end--)
      if (!Character.isWhitespace(input.charAt(end - 1)))
        break;

    final StringBuilder output = new StringBuilder();
    for (int i = start ; i < end; i++) {
      char cursor = input.charAt(i);
      // substitute all possible white spaces with a space
      if (Character.isWhitespace(cursor))
        cursor = ' ';
      // append the character only if the current character itself isn't a
      // whitespace or the last scanned character was not a whitespace
      if (!Character.isWhitespace(cursor) || !Character.isWhitespace(last)) {
        output.append(cursor);
        last = cursor;
      }
    }
    return output.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes all instances of a character in a String.
   **
   ** @param  source             the String to remove character in.
   ** @param  search             the character to remove.
   **
   ** @return                    the replaced String.
   */
  public static String remove(final String source, final char search) {
    return StringUtility.remove(source, String.valueOf(search));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes all instances of substrings in a String.
   **
   ** @param  source             the String to remove substrings in.
   ** @param  search             an array of substrings to remove from the
   **                            <code>source</code> String.
   **
   ** @return                    the replaced String.
   */
  public static String remove(final String source, final String[] search) {
    return StringUtility.replace(source, search, "");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes all instances of a substring in a String.
   **
   ** @param  source             the String to remove substring in.
   ** @param  search             the substring to remove.
   **
   ** @return                    the replaced String.
   */
  public static String remove(final String source, final String search) {
    return StringUtility.replace(source, search, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAccents
  /**
   ** Removes accented characters in a String by unaccented equivalents.
   **
   ** @param  input              the String that may contain characters that
   **                            should be replaced.
   **
   ** @return                    the passed String with the removed characters
   **
   ** @throws NullPointerException if <code>input</code> is <code>null</code>.
   */
  public static String removeAccents(final String input) {
    // you probably want to use Normalizer.Form.NFKD rather than NFD
    // NFKD will convert things like ligatures into ascii characters, NFD will
    // not do this.
    return removeAccents(input, Normalizer.Form.NFD);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceAccents
  /**
   ** Removes accented characters in a String by unaccented equivalents.
   **
   ** @param  input              the String that may contain characters that
   **                            should be replaced.
   ** @param  form               The normalization form; one of
   **                            <ul>
   **                              <li>{@link java.text.Normalizer.Form#NFC}
   **                              <li>{@link java.text.Normalizer.Form#NFD}
   **                              <li>{@link java.text.Normalizer.Form#NFKC}
   **                              <li>{@link java.text.Normalizer.Form#NFKD}
   **                            </ul>
   **
   ** @return                    the passed String with the removed characters
   **
   ** @throws NullPointerException if <code>input</code> or <code>form</code> is
   **                              <code>null</code>.
   */
  public static String removeAccents(final String input, final Normalizer.Form form) {
    final char[] out  = new char[input.length()];
    final String norm = Normalizer.normalize(input, form);

    int j = 0;
    for (int i = 0, n = norm.length(); i < n; ++i) {
      char c = norm.charAt(i);
      int type = Character.getType(c);
      if (type != Character.NON_SPACING_MARK) {
        out[j] = c;
        j++;
      }
    }
    return new String(out);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replaceAccents
  /**
   ** Replace accented characters in a String by unaccented equivalents.
   **
   ** @param  input              the String that may contain characters that
   **                            should be replaced.
   **
   ** @return                    the passed String with the replaced characters
   */
  public final static String replaceAccents(String input) {
    final StringBuilder output = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      switch (input.charAt(i)) {
        case '\u00C0': // À
        case '\u00C1': // Á
        case '\u00C2': // Â
        case '\u00C3': // Ã
        case '\u00C5': // Å
          output.append("A");
          break;
        case '\u00C4': // Ä
        case '\u00C6': // Æ
          output.append("AE");
          break;
        case '\u00C7': // Ç
          output.append("C");
          break;
        case '\u00C8': // È
        case '\u00C9': // É
        case '\u00CA': // Ê
        case '\u00CB': // Ë
          output.append("E");
          break;
        case '\u00CC': // Ì
        case '\u00CD': // Í
        case '\u00CE': // Î
        case '\u00CF': // Ï
          output.append("I");
          break;
        case '\u00D0': // Ð
          output.append("D");
          break;
        case '\u00D1': // Ñ
          output.append("N");
          break;
        case '\u00D2': // Ò
        case '\u00D3': // Ó
        case '\u00D4': // Ô
        case '\u00D5': // Õ
        case '\u00D8': // Ø
          output.append("O");
          break;
        case '\u00D6': // Ö
        case '\u0152': // Œ
          output.append("OE");
          break;
        case '\u00DE': // Þ
          output.append("TH");
          break;
        case '\u00D9': // Ù
        case '\u00DA': // Ú
        case '\u00DB': // Û
          output.append("U");
          break;
        case '\u00DC': // Ü
          output.append("UE");
          break;
        case '\u00DD': // Ý
        case '\u0178': // Ÿ
          output.append("Y");
          break;
        case '\u00E0': // à
        case '\u00E1': // á
        case '\u00E2': // â
        case '\u00E3': // ã
        case '\u00E5': // å
          output.append("a");
          break;
        case '\u00E4': // ä
        case '\u00E6': // æ
          output.append("ae");
          break;
        case '\u00E7': // ç
          output.append("c");
          break;
        case '\u00E8': // è
        case '\u00E9': // é
        case '\u00EA': // ê
        case '\u00EB': // ë
          output.append("e");
          break;
        case '\u00EC': // ì
        case '\u00ED': // í
        case '\u00EE': // î
        case '\u00EF': // ï
          output.append("i");
          break;
        case '\u00F0': // ð
          output.append("d");
          break;
        case '\u00F1': // ñ
          output.append("n");
          break;
        case '\u00F2': // ò
        case '\u00F3': // ó
        case '\u00F4': // ô
        case '\u00F5': // õ
        case '\u00F8': // ø
          output.append("o");
          break;
        case '\u00F6': // ö
        case '\u0153': // œ
          output.append("oe");
          break;
        case '\u00DF': // ß
          output.append("ss");
          break;
        case '\u00FE': // þ
          output.append("th");
          break;
        case '\u00F9': // ù
        case '\u00FA': // ú
        case '\u00FB': // û
          output.append("u");
          break;
        case '\u00FC': // ü
          output.append("ue");
          break;
        case '\u00FD': // ý
        case '\u00FF': // ÿ
          output.append("y");
          break;
        default: output.append(input.charAt(i));
          break;
      }
    }
    return output.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expand
  /**
   ** Replaces all tabulator characters with 2 spaces.
   **
   ** @param  string             the string to handle.
   **
   ** @return                    the string without tabulator characters.
   */
  public static String expand(final String string) {
    return string.replace("\t", "  ");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeNfc
  /**
   ** Normalize Unicode-input message to NFC.
   ** <p>
   ** This algorithm will normalize the input's UNICODE using
   ** <code>java.text.Normalizer</code>. If this is not present either (this
   ** class appeared in JavaSE 6), it will raise an exception.
   **
   ** @param  message            the message to be normalized.
   **
   ** @return                    the result of the normalization operation
   */
  public static String normalizeNfc(final String message) {
    return new String(normalizeNfc(message.toCharArray()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   normalizeNfc
  /**
   ** Normalize Unicode-input message to NFC.
   ** <p>
   ** This algorithm will normalize the input's UNICODE using
   ** <code>java.text.Normalizer</code>. If this is not present either (this
   ** class appeared in JavaSE 6), it will raise an exception.
   **
   ** @param  message            the message to be normalized.
   **
   ** @return                    the result of the normalization operation
   */
  public static char[] normalizeNfc(final char[] message) {
    try {
      Thread.currentThread().getContextClassLoader().loadClass(NORMALIZER_CLASS);
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException("Cannot find a valid UNICODE normalizer: " + NORMALIZER_CLASS + " have not been found at the classpath. If you are using a version of the JDK older than JavaSE 6, you should include the icu4j library in your classpath.");
    }
    // using java JDK's Normalizer, we cannot avoid creating Strings
    // (it is the only possible interface to the Normalizer class).
    final String result = java.text.Normalizer.normalize(new String(message), java.text.Normalizer.Form.NFC);
    return result.toCharArray();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toBytes
  /**
   ** Converts a character array to a byte array in the default character set.
   **
   ** @param  input              the character array to convert.
   **
   ** @return                    the characters as bytes in the default charset.
   */
  public static byte[] toBytes(final char[] input)
  {
    try {
      return new String(input).getBytes(defaultCharset().name());
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Error decoding input characters.");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toBytes
  /**
   ** Converts a string to bytes in the default character set.
   **
   ** @param  input              the String to convert.
   **
   ** @return                    the String characters as byte array.
   */
  public static byte[] toBytes(final String input) {
    try {
      return input.getBytes(defaultCharset().name());
    }
    catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Error decoding " + input);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toAscii
  /**
   ** Converts a string to bytes in the ASCII character set.
   **
   ** @param  input              the String to convert.
   **
   ** @return                    the String characters as byte array converted
   **                            to ASCII characters.
   */
  public static byte[] toAscii(final String input)
  {
    try {
      return input.getBytes(ASCII.name());
    }
    catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Error decoding " + input);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digitSubString
  /**
   ** Return the string of digits from string starting the search from the
   ** beginning.
   **
   ** @param  source             the string source to search for a digit.
   **
   ** @return                    the string of digits from string starting the
   **                            search from the index specified.
   */
  public static String digitSubString(String source) {
    return digitSubString(source, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   digitSubString
  /**
   ** Return the string of digits from string starting the search from the
   ** index specified.
   **
   ** @param  source             the string source to search for a digit.
   ** @param  fromIndex          the index to start the search from.
   **
   ** @return                    the string of digits from string starting the
   **                            search from the index specified.
   */
  public static String digitSubString(String source, int fromIndex) {
    String ret = null;
    int d = indexOfDigit(source, fromIndex);
    if (d != -1) {
      int a = indexOfAlpha(source, d);
      ret = (a == -1) ? source.substring(d) : source.substring(d, a);
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOfDigit
  /**
   ** Finds the index of the first digit starting from the beginning of
   ** <code>source</code>.
   **
   ** @param  source             the string source to search for a digit.
   **
   ** @return                    the index of the first occurrence of any
   **                            digit in the character sequence represented by
   **                            <code>source</code>, or <code>-1</code> if no
   **                            character occurs.
   */
  public static int indexOfDigit(final String source) {
    return indexOfDigit(source, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOfDigit
  /**
   ** Finds the index of the first digit and starts from the index specified.
   **
   ** @param  source             the string source to search for a digit.
   ** @param  fromIndex          the index to start the search from.
   **
   ** @return                    the index of the first occurrence of any
   **                            digit in the character sequence represented by
   **                            <code>source</code> that is greater than or
   **                            equal to <code>fromIndex</code>, or
   **                            <code>-1</code> if no character occurs.
   */
  public static int indexOfDigit(final String source, final int fromIndex) {
    int pos = -1;
    if (source != null) {
      for (int i = fromIndex; i < source.length(); i++) {
        // get the first digit..
        if (Character.isDigit(source.charAt(i))) {
          pos = i;
          break;
        }
      }
    }
    return pos;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOfAlpha
  /**
   ** Finds the index of the first non-digit starting from the beginning of
   ** <code>source</code>.
   **
   ** @param  source             the string source to search for a non-digit.
   **
   ** @return                    the index of the first occurrence of any
   **                            non-digit in the character sequence represented
   **                            by <code>source</code>, or <code>-1</code> if
   **                            no character occurs.
   */
  public static int indexOfAlpha(final String source) {
    return indexOfAlpha(source, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOfAlpha
  /**
   ** Finds the index of the first non-digit and starts from the index specified.
   **
   ** @param  source             the string source to search for a non-digit.
   ** @param  fromIndex          the index to start the search from.
   **
   ** @return                    the index of the first occurrence of any
   **                            non-digit in the character sequence represented
   **                            by <code>source</code> that is greater than or
   **                            equal to <code>fromIndex</code>, or
   **                            <code>-1</code> if no character occurs.
   */
  public static int indexOfAlpha(final String source, final int fromIndex) {
    int pos = -1;
    if (source != null) {
      for (int i = fromIndex; i < source.length(); i++) {
        // get the first digit..
        if (!Character.isDigit(source.charAt(i))) {
          pos = i;
          break;
        }
      }
    }
    return pos;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOf
  /**
   ** Returns the index within a string of the first occurrence of the any of
   ** the specified characters, starting the search at the beginning of
   ** <code>source</code>.
   **
   ** @param  source             the string source to inspect.
   ** @param  ch                 a character array (Unicode code point).
   **
   ** @return                    the index of the first occurrence of any
   **                            character in the character sequence represented
   **                            by <code>source</code>, or <code>-1</code> if
   **                            no character occurs.
   */
  public static int indexOf(final String source, final char[] ch) {
    return indexOf(source, ch, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indexOf
  /**
   ** Returns the index within a string of the first occurrence of the any of
   ** the specified characters, starting the search at the specified index.
   ** <p>
   ** If a character with from <code>ch</code> occurs in the character sequence
   ** represented by <code>source</code> object at an index no smaller than
   ** <code>fromIndex</code>, then the index of the first such occurrence is
   ** returned. For values of <code>ch</code> in the range from 0 to 0xFFFF
   ** (inclusive), this is the smallest value <i>k</i> such that:
   ** <pre>
   **  (source.charAt(<i>k</i>) == ch) &amp;&amp; (<i>k</i> &gt;= fromIndex)
   ** </pre>
   ** is <code>true</code>. For other values of <code>ch</code>, it is the
   ** smallest value <i>k</i> such that:
   ** <pre>
   **  (source.codePointAt(<i>k</i>) == ch) &amp;SAXException (<i>k</i> &gt;= fromIndex)
   ** </pre>
   ** is <code>true</code>. In either case, if no such character occurs in the
   ** string at or after position <code>fromIndex</code>, then
   ** <code>-1</code> is returned.
   ** <p>
   ** There is no restriction on the value of <code>fromIndex</code>. If it is
   ** negative, it has the same effect as if it were zero: this entire string
   ** may be searched. If it is greater than the length of this string, it has
   ** the same effect as if it were equal to the length of this string:
   ** <code>-1</code> is returned.
   ** <p>
   ** All indices are specified in <code>char</code> values (Unicode code units).
   **
   ** @param  source             the string source to inspect.
   ** @param  ch                 a character array (Unicode code point).
   ** @param  fromIndex          the index to start the search from.
   **
   ** @return                    the index of the first occurrence of any
   **                            character in the character sequence represented
   **                            by <code>source</code> that is greater than or
   **                            equal to <code>fromIndex</code>, or
   **                            <code>-1</code> if no character occurs.
   */
  public static int indexOf(final String source, final char[] ch, final int fromIndex) {
    int pos = Integer.MAX_VALUE;
    for (int i = 0; i < ch.length; i++) {
      int tmp = source.indexOf(ch[i], fromIndex);
      if (tmp != -1 && tmp < pos)
        pos = tmp;
    }
    return (pos == Integer.MAX_VALUE) ? -1 : pos;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToBool
  /**
   ** Converts a string to its boolean value.
   ** <br>
   ** This method checks for "yes" or "true" or "1" to determine if the value is
   ** <code>true</code> anything else returns <code>false</code>.
   **
   ** @param  value              the string to evaluate.
   **
   ** @return                    the boolean value for a string.
   */
  public static boolean stringToBool(final String value) {
    return (SystemConstant.TRUE.equalsIgnoreCase(value) || SystemConstant.YES.equalsIgnoreCase(value) || "1".equals(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToInt
  /**
   ** Returns a <code>int</code> for the specified string value.
   **
   ** @param  value              the string value to convert.
   **                            Allowed object {@link String}.
   **
   ** @return                    the <code>int</code> value of the string or
   **                            <code>Integer.MIN_VALUE</code> if the string
   **                            is not numeric.
   **                            Possible object <code>int</code>.
   */
  public static int stringToInt(final String value) {
    final Integer temp = new Integer(value);
    return temp.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToInt
  /**
   ** Returns a <code>int</code> for the specified string value.
   **
   ** @param  value              the string value to convert.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the value that should be returned if the
   **                            provided value cannot converted
   **                            Allowed object <code>int</code>.
   **
   ** @return                    the converted value for the specified string
   **                            value.
   **                            Possible object <code>int</code>.
   */
  public static int stringToInt(final String value, final int defaultValue) {
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      // intentionally left blank
      return defaultValue;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToLong
  /**
   ** Converts a string its long value.
   **
   ** @param  value              the string to evaluate.
   **
   ** @return                    the <code>long</code> value of the string or
   **                            <code>Long.MIN_VALUE</code> if the string is
   **                            not numeric.
   */
  public static long stringToLong(final String value) {
    final Long temp = new Long(value);
    return temp.longValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringToLong
  /**
   ** Returns a <code>long</code> for the specified string value.
   **
   ** @param  value              the string value to convert.
   **                            Allowed object {@link String}.
   ** @param  defaultValue       the value that should be returned if the
   **                            provided value cannot converted
   **                            Allowed object <code>long</code>.
   **
   ** @return                    the converted value for the specified string
   **                            value.
   **                            Possible object <code>long</code>.
   */
  public static long stringToLong(final String value, final long defaultValue) {
    try {
      return Long.parseLong(value);
    }
    catch (NumberFormatException e) {
      // intentionally left blank
      return defaultValue;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToString
  /**
   ** Returns the string for an array of bytes converted as UTF8.
   **
   ** @param  raw                the byte array to convert.
   **
   ** @return                    the String
   */
  public static String bytesToString(final byte[] raw) {
    return bytesToString(raw, UNICODE.name());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToString
  /**
   ** Returns the string for an array of bytes converted as UTF8.
   **
   ** @param  raw                the byte array to convert.
   ** @param  charset            the name of the {@link Charset charset} to be
   **                            used to decode the <code>bytes</code>.
   **
   ** @return                    the String
   */
  public static String bytesToString(final byte[] raw, final String charset) {
    String result = null;
    try {
      result = new String(raw, charset);
    }
    catch (UnsupportedEncodingException e) {
      // should never happens
      e.printStackTrace();
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bytesToString
  /**
   ** Returns the string for an array of bytes converted as UTF8.
   **
   ** @param  raw                the byte array to convert.
   ** @param  charset            the {@link Charset} to be used to decode the
   **                            <code>bytes</code>.
   **
   ** @return                    the String
   */
  public static String bytesToString(final byte[] raw, final Charset charset) {
    return new String(raw, charset);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   byteToHex
  /**
   ** Converts a byte to a Hex string.
   **
   ** @param raw                 the byte to convert.
   **
   ** @return                    a two character Hex representation of the byte.
   */
  public static String byteToHex(final byte raw) {
    final StringBuilder buffer = new StringBuilder(2);
    final int           high   = (raw & 0xf0) >> 4;
    final int           low    =  raw & 0xf;

    buffer.append(SystemConstant.HEX_DIGITS[high]).append(SystemConstant.HEX_DIGITS[low]);

    return buffer.toString();
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
  public static String bytesToHex(final byte raw[]){
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
  // Method:   bytesToHex
  /**
   ** Converts an array of bytes to a String composed of hex representations of
   ** the bytes separated by a given separator character.
   **
   ** @param  raw                the byte array to convert.
   ** @param  sep                the separator character.
   **
   ** @return                    the string representation.
   */
  public static String bytesToHex(final byte[] raw, final char sep) {
    final int           len    = raw.length;
    final StringBuilder buffer = new StringBuilder((len * 3));

    int high;
    int low;
    for (int i = 0; i < len; i++) {
      high = (raw[i] & 0xf0) >> 4;
      low  =  raw[i] & 0xf;

      buffer.append(SystemConstant.HEX_DIGITS[high]).append(SystemConstant.HEX_DIGITS[low]);

      if (i < (len - 1))
        buffer.append(sep);
    }

    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hexToByte
  /**
   ** Converts a String thats represents a sequence hexadecimal digits to an
   ** array of bytes.
   **
   ** @param  encoded            the String to convert.
   **
   ** @return                    the byte array with hexadecimals.
   */
  public static byte[] hexToByte(final String encoded) {
    int    length = encoded.length();
    byte[] data   = new byte[length / 2];
    for (int i = 0; i < length; i += 2)
      data[i / 2] = (byte) ((Character.digit(encoded.charAt(i), 16) << 4) + Character.digit(encoded.charAt(i+1), 16));
    return data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayToString
  /**
   ** Converts an array of strings to single string by inserting a comma between
   ** each element of the string array.
   **
   ** @param  array               the array of Strings to convert.
   **
   ** @return                     a String made up of the element of the array.
   */
  public static String arrayToString(final String[] array) {
    return arrayToString(array, SystemConstant.COMMA);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayToString
  /**
   ** Converts an array of strings to single string by inserting a blank between
   ** each element of the string array.
   **
   ** @param  array               the array of Strings to convert.
   ** @param  start               the starting index in the specified string
   **                             array <code>array</code>.
   ** @param  end                 the end index in the specified string
   **                             array <code>array</code>.
   **
   ** @return                     a String made up of the element of the array.
   */
  public static String arrayToString(final String[] array, final int start, final int end) {
    final StringBuilder buffer = new StringBuilder();
    if (array.length > start) {
      for (int i = start; i < end; i++) {
        buffer.append(array[i]).append(SystemConstant.BLANK);
      }
      return buffer.substring(0, buffer.length() - 1);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrayToString
  /**
   ** Converts an array of strings to single string by inserting the specified
   ** delimiter between each element of the string array.
   **
   ** @param  array               the array of Strings to convert.
   ** @param  delimiter           the character to insert between each element
   **                             of the string array.
   **
   ** @return                     a String made up of the element of the array.
   */
  public static String arrayToString(final String[] array, final char delimiter) {
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      buffer.append(array[i]).append(delimiter);
    }
    return buffer.substring(0, buffer.length() - 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToString
  /**
   ** Converts a {@link Set} of strings to single string by inserting a space
   ** between each element of the collection.
   **
   ** @param  collection          the {@link Set} of Strings to convert.
   **
   ** @return                     a String made up of the words in the list.
   */
  public static String setToString(final Set<Object> collection) {
    return setToString(collection, SystemConstant.BLANK);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setToString
  /**
   ** Converts a {@link Set} of strings to single string by inserting  the
   ** specified <code>separator</code> between each element of the collection.
   **
   ** @param  collection          the {@link Set} of Strings to convert.
   ** @param  separator           the separator to insert between elements.
   **
   ** @return                     a String made up of the words in the list.
   */
  public static String setToString(final Set<Object> collection, final char separator) {
    return collectionToString(collection, separator);
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
    if (collection != null && collection.size() > 0) {
      for (Object i : collection)
        buffer.append(i == null ? "<null>" : i.toString()).append(separator);
      buffer.deleteCharAt(buffer.length() - 1);
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fillWithChars
  /**
   ** Return a <code>StringBuilder</code> which is derived from a supplied string.
   **
   ** @param  text               the string to put either at the beginning or
   **                            the end of the resulting string.
   ** @param  length             the initial capacity.
   ** @param  fillChar           the character to fillup the avialable space.
   ** @param  addAtEnd           <code>true</code> if the passed
   **                            <code>text</code> wiil apended at the end of
   **                            the resulting string; otherwise
   **                            <code>false</code>.
   **
   ** @return                    the {@link StringBuilder} filled up with the
   **                            specified properties.
   */
  public static final StringBuilder fillWithChars(String text, char fillChar, int length, boolean addAtEnd) {
    if (text == null)
      text = SystemConstant.EMPTY;

    if (text.length() >= length)
      return new StringBuilder(text);

    StringBuilder buffer = new StringBuilder(length);
    if (addAtEnd) {
      buffer.append(text);
      fillBufferWith(buffer, fillChar, length - text.length());
    }
    else {
      fillBufferWith(buffer, fillChar, length - text.length());
      buffer.append(text);
    }
    return buffer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalsWithIgnoreCase
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by the left hand side
   ** (<code>lhs</code>) argument string is compared lexicographically to the
   ** character sequence represented by the right hand side (<code>rhs</code>)
   ** argument string.
   ** <br>
   ** The result is a negative integer if this String object lexicographically
   ** precedes the argument string. The result is a positive integer if this
   ** String object lexicographically follows the argument string. The result is
   ** zero if the strings are equal; compareTo returns 0 exactly when the
   ** equals(Object) method would return true.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code>, if <code>rhs</code>
   **                            represents an equivalent <code>String</code>
   **                            ignoring case as <code>lhs</code>;
   **                            <code>true</code> otherwise.
   */
  public static boolean equalsWithIgnoreCase(final String lhs, final String rhs) {
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
   ** @param  value              the character to look for at the start of the
   **                            string.
   **
   ** @return                    <code>true</code>, if character parameter is
   **                            found at the start of the string parameter
   **                            otherwise <code>false</code>.
   */
  public static boolean startsWith(final String base, final char value) {
    return isBlank(base) ? false : base.charAt(0) == value;
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
    return isBlank(base) ? false : base.charAt(base.length() - 1) == value;
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
    return isBlank(base) ? false : base.indexOf(value) >= 0;
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
  // Method:   containsWithIgnoreCase
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
  public static boolean containsWithIgnoreCase(final String base, final String contain) {
    if (base == null || contain == null) {
      return (base == null && contain == null);
    }
    if (base.length() < contain.length()) {
      return false;
    }
    return base.toLowerCase().contains(contain.toLowerCase());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fillBufferWith
  //
  private static final void fillBufferWith(final StringBuilder buffer, final char fillChar, int length) {
    for (int i = 0; i < length; i++)
      buffer.append(fillChar);
  }
}