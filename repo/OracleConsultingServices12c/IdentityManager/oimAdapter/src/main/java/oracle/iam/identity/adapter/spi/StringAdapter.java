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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   StringAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    StringAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.StringTokenizer;

import com.thortech.xl.dataaccess.tcDataProvider;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.GenericAdapter;

////////////////////////////////////////////////////////////////////////////////
// class StringAdapter
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Offers utility methods to handle string objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class StringAdapter extends GenericAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Rule
  // ~~~~ ~~~~
  /**
   ** This enum store the grammar's constants for the convertion rules of this
   ** adapter.
   */
  enum Rule {
      /**
       ** Rule tag that might be defined in the requested rule to convert a
       ** string to upper case.
       */
     Upper
     /**
      ** Rule tag that might be defined in the requested rule to convert a string
      ** to lower case.
      */
    , Lower
      /**
       ** Rule tag that might be defined in the requested rule to convert a string
       ** to reverse oder of the characters.
       */
    , Reverse
      /**
       ** Rule tag that might be defined in the requested rule to convert a string
       ** to capitilize the word boundaries.
       */
    , Capitalize
    ;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6181461637027890231")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: typeString
    /**
     ** Returns a string with all the possible types a catalog has.
     **
     ** @return                  a string with all the possible types a catalog
     **                          has.
     */
    public static final String typeString() {
      return Rule.Upper.toString() + " | " + Rule.Lower.toString() + " | " + Rule.Reverse.toString() + " | " + Rule.Capitalize.toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StringAdapter</code> task adpater that allows
   ** use as  a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public StringAdapter(final tcDataProvider provider) {
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>StringAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public StringAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAccents
  /**
   ** Replace accented characters in a String by unaccented equivalents.
   **
   ** @param  input              the String that may contain characters that
   **                            should be replaced
   **
   ** @return                    the passed String with the replaced characters
   */
  public final static String removeAccents(String input) {
    return StringUtility.replaceAccents(input);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Returns a new string that is converted by applying the specified rule to
   ** the given string.
   **
   ** @param  string             the string to convert.
   ** @param  convertionRule     the convertion rule to apply.
   **                            The rule can be specified by piping.
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String convert(final String string, final String convertionRule) {
    String result = string;
    // don't try to do anything if no rule can be applied
    if (StringUtility.isEmpty(convertionRule))
      return result;

    StringTokenizer tokenizer = new StringTokenizer(convertionRule, "|");
    while (tokenizer.hasMoreTokens()) {
      final String rule = tokenizer.nextToken().trim();
      // don't evaluate any rule if it doesn't make sense
      if (StringUtility.isEmpty(rule))
        continue;

      final Rule step = Rule.valueOf(rule);
      // don't evaluate any rule if it doesn't make sense
      if (step == null)
        continue;

      // evaluate the rule
      switch (step) {
        case Upper      : result = upperCase(result);
                          break;
        case Lower      : result = lowerCase(result);
                          break;
        case Reverse    : result = reverse(result);
                          break;
        case Capitalize : result = capitalize(result);
                          break;
      }
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Concats two String.
   **
   ** @param  first              the first part of the string to concate.
   ** @param  second             the second part of the string to concate.
   ** <p>
   ** Per default the coverion rule <code>Lower</code> is applied.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code>.
   */
  public static String concat(final String first, final String second) {
    return concat(first, second, "Lower");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Concats two String.
   **
   ** @param  first              the first part of the string to concate.
   ** @param  second             the second part of the string to concate.
   ** @param  convertionRule     the convertion rule to apply.
   **                            The rule can be specified by piping.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code>.
   */
  public static String concat(final String first, final String second, final String convertionRule) {
    boolean ignoreFirst  = StringUtility.isEmpty(first);
    boolean ignoreSecond = StringUtility.isEmpty(second);
    StringBuilder buffer = new StringBuilder();
    if (!(ignoreFirst && ignoreSecond))
      buffer.append(first).append(second);
    else if (StringUtility.isEmpty(first))
      buffer.append(second);
    else if (StringUtility.isEmpty(second))
      buffer.append(first);

    return convert(buffer.toString(), convertionRule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Concats two String separated by th given character as a delimiter.
   ** <p>
   ** Per default the coverion rule <code>Lower</code> is applied.
   **
   ** @param  first              the first part of the string to concate.
   ** @param  second             the second part of the string to concate.
   ** @param  separator          the character used to separate the first and
   **                            second part of the new string to build.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code> separated by the specified
   **                            character <code>separator</code>.
   */
  public static String concat(final String first, final String second, final char separator) {
    return concat(first, second, separator, "Lower");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concat
  /**
   ** Concats two String separated by th given character as a delimiter.
   **
   ** @param  first              the first part of the string to concate.
   ** @param  second             the second part of the string to concate.
   ** @param  separator          the character used to separate the first and
   **                            second part of the new string to build.
   ** @param  convertionRule     the convertion rule to apply.
   **                            The rule can be specified by piping.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code> separated by the specified
   **                            character <code>separator</code>.
   */
  public static String concat(final String first, final String second, final char separator, final String convertionRule) {
    boolean ignoreFirst  = StringUtility.isEmpty(first);
    boolean ignoreSecond = StringUtility.isEmpty(second);

    StringBuilder buffer = new StringBuilder();
    if (!ignoreFirst && !ignoreSecond)
      buffer.append(first).append(separator == '+' ? ' ' :  separator).append(second);
    else if (ignoreFirst && !ignoreSecond)
      buffer.append(second);
    else if (!ignoreFirst && ignoreSecond)
      buffer.append(first);

    return convert(buffer.toString(), convertionRule);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns a new string that is a substring of the passed string.
   ** <p>
   ** The substring begins with the character at the specified index and extends
   ** to the end of the passed string.
   **
   ** @param  string             the string to build the substring from.
   ** @param  start              the start index, inclusive.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code> separated a blank.
   */
  public static String locale(final String string, int start) {
    return StringUtility.isEmpty(string) ? "en" : string.substring(start < 0 ? 0 : start);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns a new string that is a substring of the passed string.
   ** <p>
   ** The substring begins with the character at the specified index and extends
   ** to the end of the passed string.
   **
   ** @param  string             the string to build the substring from.
   ** @param  start              the start index, inclusive.
   ** @param  end                the end index, exclusive.
   **
   ** @return                    the substring starting at <code>start</code> up
   **                            to <code>end</code>.
   */
  public static String locale(final String string, final int start, final int end) {
    return StringUtility.isEmpty(string) ? "en" : string.substring(start < 0 ? 0 : start, end > string.length() ? string.length(): end);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substring
  /**
   ** Returns a new string that is a substring of the passed string.
   ** <p>
   ** The substring begins with the character at the specified index and extends
   ** to the end of the passed string.
   **
   ** @param  string             the string to build the substring from.
   ** @param  start              the start index, inclusive.
   **
   ** @return                    the concatenated strings <code>first</code> and
   **                            <code>second</code> separated a blank.
   */
  public static String substring(final String string, final int start) {
    return string.substring(start < 0 ? 0 : start);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   substring
  /**
   ** Returns a new string that is a substring of the passed string.
   ** <p>
   ** The substring begins with the character at the specified index and extends
   ** to the end of the passed string.
   **
   ** @param  string             the string to build the substring from.
   ** @param  start              the start index, inclusive.
   ** @param  end                the end index, exclusive.
   **
   ** @return                    the substring starting at <code>start</code> up
   **                            to <code>end</code>.
   */
  public static String substring(final String string, final int start, final int end) {
    return string.substring(start < 0 ? 0 : start, end > string.length() ? string.length(): end);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upperCase
  /**
   ** Returns a new string where all of the characters in the passed string are
   ** converted to upper case using the rules of the default locale.
   **
   ** @param  string             the string to convert.
   **
   ** @return                    the passed string converted to upper case.
   */
  public static String upperCase(final String string) {
    return string.toUpperCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lowerCase
  /**
   ** Returns a new string where all of the characters in the passed string are
   ** converted to lower case using the rules of the default locale.
   **
   ** @param  string             the string to convert.
   **
   ** @return                    the passed string converted to lower case.
   */
  public static String lowerCase(final String string) {
    return string.toLowerCase();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   capitalize
  /**
   ** Returns a new string where all the whitespace separated words in a String.
   ** <p>
   ** Only the first letter of each word is changed.
   **
   ** @param  string             the string to convert.
   **
   ** @return                    capitalized String, <code>null</code> if null
   **                            String input
   */
  public static String capitalize(final String string) {
    return StringUtility.capitalize(string);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reverse
  /**
   ** Returns a new string in reverse order of the characzters.
   **
   ** @param  string             the string to convert.
   **
   ** @return                    reverted String, <code>null</code> if null
   **                            String input
   */
  public static String reverse(final String string) {
    return StringUtility.reverse(string);
  }
}