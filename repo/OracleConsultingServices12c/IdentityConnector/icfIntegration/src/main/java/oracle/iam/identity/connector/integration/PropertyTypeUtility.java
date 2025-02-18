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
    Subsystem   :   Connector Bundle Integration

    File        :   PropertyTypeUtility.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PropertyTypeUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.lang.reflect.Array;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

import java.io.File;

import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import java.sql.Timestamp;

import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.common.security.GuardedByteArray;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class PropertyTypeUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous utility methods to convert types.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class PropertyTypeUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String SPLIT                = "\\s*,\\s*";

  private static final int    UTIL_DATE_LENGTH     = 28;
  private static final int    SQL_DATE_LENGTH      = 10;
  private static final int    SQL_TIMESTAMP_LENGTH = 21;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>PropertyTypeUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new TypeUtility()" and enforces use of the public method below.
   */
  private PropertyTypeUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseBoolean
  /**
   ** Parses the string argument as a <code>boolean</code>.
   ** <p>
   ** The <code>boolean</code> returned represents the value <code>true</code>
   ** if the string argument is not <code>null</code> and is equal, ignoring
   ** case, either to <code>true</code> or <code>yes</code> or <code>on</code>
   ** or <code>1</code>.
   **
   ** @param  value              the {@link String} containing the
   **                            <code>boolean</code> representation to be
   **                            parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the boolean represented by the string argument.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean parseBoolean(final String value) {
    return (SystemConstant.TRUE.equalsIgnoreCase(value) || SystemConstant.YES.equalsIgnoreCase(value) || SystemConstant.ON.equalsIgnoreCase(value) || "1".equals(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseInteger
  /**
   ** Parses the string argument as a signed decimal integer.
   ** <p>
   ** The characters in the string must all be decimal digits, except that the
   ** first character may be an ASCII minus sign '<code>-</code>'}
   ** (<code>'&#92;u002D'</code>) to indicate a negative value or an ASCII plus
   ** sign <code>'+'</code> (<code>'&#92;u002B'</code>) to indicate a positive
   ** value. The resulting integer value is returned, exactly as if the argument
   ** and the radix 10 were given as arguments to the
   ** {@link #parseInteger(java.lang.String, int)} method.
   **
   ** @param  value              a {@link String} containing the
   **                            <code>int</code> representation to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  defaultValue       the value to return if <code>value</code> does
   **                            not contain a parsable <code>int</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the integer value represented by the argument
   **                            in decimal.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int parseInteger(final String value, final int defaultValue) {
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseList
  public static String[] parseList(final String value) {
    return value.split("\\|");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseArray
  /**
   ** Converts string value to array of string in the following way:
   ** <br>
   ** \"one\",\"two\" -&lt; ["one","two"]
   ** <br>
   ** one -&gt; ["one"]
   ** <br>
   ** \"one\\\",one\",\"two\" -&lt; ["one\",one","two"]
   **
   ** @param  value              a {@link String} to be converted to array.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the array of strings.
   **                            <br>
   **                            Possible object is array of {@link String}.
   **
   ** @throws IllegalArgumentException in case it is not possible to parse the
   **                                  value.
   */
  public static String[] parseArray(final String value) {
    // prevent bogus input
    if (value == null)
      return new String[0];

    boolean inQuotes = false;
    boolean noQuotes = true;
    final LinkedList<Character> chars  = new LinkedList<Character>();
    final List<String>          values = new ArrayList<String>();
    for (char c : value.trim().toCharArray()) {
      switch (c) {
        case '\"' : if (!inQuotes) {
                      if (chars.size() > 0) {
                        // some value already present, that's wrong
                        throw new IllegalArgumentException("Invalid value [" + value + "]");
                      }
                      inQuotes = true;
                      noQuotes = false;
                    }
                    else if (chars.peekLast() == '\\') {
                      // escaped quote - remove the escape char
                      chars.removeLast();
                      chars.add(c);
                    }
                    else {
                      values.add(StringUtility.collectionToString(chars));
                      chars.clear();
                      inQuotes = false;
                    }
                    break;
        case ',' : if (inQuotes) {
                     chars.add(c);
                   }
                   break;
        default  : chars.add(c);
      }
    }
    // check there is nothing left
    if (inQuotes)
      throw new IllegalArgumentException("Invalid value [" + value + "]");

    if (noQuotes && !StringUtility.isEmpty(value)) {
      values.add(value);
    }
    else if (chars.size() > 0)
      throw new IllegalArgumentException("Invalid value [" + value + "]");

    return values.toArray(new String[values.size()]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString
  /**
   ** Returns a string representation of the given object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" the object. The result should be a concise but
   ** informative representation that is easy for a person to read.
   **
   ** @param  value              the value to transform.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    a string representation of the given object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String toString(final Object value) {
    // prevent bogus input
    if (value == null)
      return null;

    return (value instanceof Boolean) ? (Boolean)value ? "1" : "0" : value.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Convert the {@link List} of string to {@link List} of required properties.
   **
   ** @param  <T>                the {@link List} type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  origin             the {@link List} of values.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   ** @param  type               the type of the Class&lt;T[]&gt; extends
   **                            Object[].
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link List} with T type.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> convert(final List<String> origin, final Class<T> type) {
    final List<T> converted = new ArrayList<T>(origin.size());
    for (String value : origin) {
      converted.add(convert(value, type));
    }
    return converted;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Convert the string to required properties.
   **
   ** @param  <T>                the type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  origin             the value.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the type of the Class&lt;T[]&gt; extends
   **                            Object[].
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the T type.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"unchecked","cast"}) 
  public static <T> T convert(final String origin, final Class<T> type) {
    //TODO: conversion to Script type is missing!!!
    Object result = null;
    if (origin == null) {
      result = null;
    }
    else if (type == java.sql.Timestamp.class || type == java.util.Date.class || type == java.sql.Date.class) {
      result = timeStamp(origin);
    }
    else if (type == String.class) {
      result = origin;
    }
    else if (type == java.io.File.class) {
      result = new File(origin);
    }
    else if (type == java.math.BigInteger.class) {
      result = new java.math.BigInteger(origin);
    }
    else if (type == java.math.BigDecimal.class) {
      result = new java.math.BigDecimal(origin);
    }
    else if (type == byte[].class) {
      result = origin.getBytes();
    }
    else if (type == boolean.class || type == Boolean.class) {
      result = parseBoolean(origin);
    }
    else if (type == int.class || type == Integer.class) {
      result = StringUtility.isBlank(origin) ? null : Integer.valueOf(origin);
    }
    else if (type == long.class || type == Long.class) {
      result = StringUtility.isBlank(origin) ? null : Long.valueOf(origin);
    }
    else if (type == float.class || type == Float.class) {
      result = StringUtility.isBlank(origin) ? null : Float.valueOf(origin);
    }
    else if (type == double.class || type == Double.class) {
      result = StringUtility.isBlank(origin) ? null : Double.valueOf(origin);
    }
    else if (type == char.class || type == Character.class) {
      final char[] charArray = origin.toCharArray();
      result = charArray[0];
    }
    else if (type == char[].class) {
      result = origin.toCharArray();
    }
    else if (type == Character[].class) {
      final char[]      charArray      = origin.toCharArray();
      final Character[] characterArray = new Character[charArray.length];
      for (int i = 0; i < charArray.length; i++) {
        characterArray[i] = charArray[i];
      }
      result = characterArray;
    }
    else if (type == java.net.URI.class) {
      try {
        result = new java.net.URI(origin);
      }
      catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }
    else if (type == URL.class) {
      if (StringUtility.isEmpty(origin)) {
        return null;
      }
      try {
        result = new URL(origin);
      }
      catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
    else if (type == GuardedByteArray.class) {
      result = new GuardedByteArray(origin.getBytes());
    }
    else if (type == GuardedString.class) {
      result = new GuardedString(origin.toCharArray());
    }
    else if (Object[].class.isAssignableFrom(type)) {
      result = convert(origin.split(SPLIT), type.getComponentType());
    }
    else
      // currently the original type is always String
      throw new IllegalArgumentException("Invalid target type: " + type.getName());

    return (T)result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Convert the array of string to array of required properties.
   **
   ** @param  <T>                the array type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  origin             the array of values.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   ** @param  type               the type of the Class&lt;T[]&gt; extends
   **                            Object[].
   **                            <br>
   **                            Allowed object is {@link Class} of type
   **                            <code>T</code>.
   **
   ** @return                    the array with T type.
   **                            <br>
   **                            Possible object is array of <code>T</code>.
   */
  @SuppressWarnings("unchecked") 
  private static <T> T[] convert(final String[] origin, final Class<T> type) {
    final List<?> r = convert(Arrays.asList(origin), type);
    final T[]     t = (T[])Array.newInstance(type, 0);
    return r.toArray(t);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeStamp
  /**
   ** Converts a <code>String</code> object in JDBC timestamp escape format to a
   ** <code>Timestamp</code> value.
   **
   ** @param  origin             the timestamp in format
   **                            <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>.
   **                            The fractional seconds may be omitted. The
   **                            leading zero for <code>mm</code> and
   **                            <code>dd</code> may also be omitted.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the corresponding <code>Timestamp</code> value.
   **                            <br>
   **                            Possible object is {@link Timestamp}.
   **
   ** @throws IllegalArgumentException if the given argument does not have the
   **                                  format
   **                                  <code>yyyy-[m]m-[d]d hh:mm:ss[.f...]</code>
   */
  private static Timestamp timeStamp(final String origin) {
    Timestamp value = null;
    if (origin.length() == UTIL_DATE_LENGTH)
      value = new Timestamp(new java.util.Date(origin).getTime());
    else if (origin.length() == SQL_DATE_LENGTH)
      value = new Timestamp(java.sql.Date.valueOf(origin).getTime());
    else if (origin.length() == SQL_TIMESTAMP_LENGTH)
      value = Timestamp.valueOf(origin);
    return value;
  }
}