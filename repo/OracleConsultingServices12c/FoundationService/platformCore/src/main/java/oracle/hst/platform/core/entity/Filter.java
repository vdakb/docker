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

    File        :   Filter.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Filter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.List;

import java.text.ParseException;

import oracle.hst.platform.core.SystemException;

import oracle.hst.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface Filter
// ~~~~~~~~~ ~~~~~~
/**
 ** Basic interface to match a {@link Path}.
 **
 ** @param  <T>                  the type of the contained filter value.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the filters
 **                              implementing this interface (compare operations
 **                              can use their own specific types instead of
 **                              types defined by this interface only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Filter<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** Defines the set of possible filter types that may be used for query
   ** filters.
   */
  public enum Type {
      /** the type for <code>and</code> filters */
      AND("and")
      /** the type for <code>or</code> filters */
    , OR("or")
      /** the type for <code>not</code> filters */
    , NOT("not")
      /** the filter type for complex attribute value filters */
    , COMPLEX("complex")
      /** the type for <code>equal</code> filters */
    , EQ("eq")
      /** the type for <code>not equal</code> filters */
    , NE("ne")
      /** the type for <code>contains</code> filters */
    , CO("co")
      /** the type for <code>starts with</code> filters */
    , SW("sw")
      /** the type for <code>starts ends</code> filters */
    , EW("ew")
      /** the type for <code>present</code> filters */
    , PR("pr")
      /** the type for <code>greater than</code> filters */
    , GT("gt")
      /** the type for <code>greater or equal</code> filters */
    , GE("ge")
      /** the type for <code>less than</code> filters */
    , LT("lt")
      /** the type for <code>less or equal</code> filters */
    , LE("le")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the lower case string value for this filter type. */
    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Type</code> with a constraint value.
     **
     ** @param  value            the filter type of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Type(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the type constraint.
     **
     ** @return                  the value of the type constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Type</code> constraint from the
     ** given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Type</code> constraint.
     **                          <br>
     **                          Possible object is <code>Type</code>.
     */
    public static Type from(final String value) {
      for (Type cursor : Type.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Visitor
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** A visitor of {@link Filter}s, in the style of the visitor design pattern.
   ** <p>
   ** Classes implementing this interface can query filters in a type-safe
   ** manner. When a visitor is passed to a filter's accept method, the
   ** corresponding visit method most applicable to that filter is invoked.
   **
   ** @param  <R>                the return type of this visitor's methods. Use
   **                            {@link java.lang.Void} for visitors that do not
   **                            need to return results.
   **                            <br>
   **                            Allowed object is <code>R</code>.
   ** @param  <V>                the type of the parameter value to this
   **                            visitor's methods. Use {@link java.lang.Void}
   **                            for visitors that do not need an  parameter
   **                            value.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   */
  public interface Visitor<R, V> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: and
    /**
     ** Visits an <code>and</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>true</code>.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link And} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R and(final V value, final And<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: or
    /**
     ** Visits an <code>or</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>false</code>.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Or} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R or(final V value, final Or<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: not
    /**
     ** Visits an <code>not</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Not} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R not(final V value, final Not<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: present
    /**
     ** Visits an <code>present</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Presence} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R present(final V value, final Presence<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals
    /**
     ** Visits an <code>equality</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Equals} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R equals(final V value, final Equals<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: greaterThan
    /**
     ** Visits an <code>greater than</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThan} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R greaterThan(final V value, final GreaterThan<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: greaterThanOrEqual
    /**
     ** Visits an <code>greater than or equal to</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThanOrEqual}
     **                          for type <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R greaterThanOrEqual(final V value, final GreaterThanOrEqual<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: lessThan
    /**
     ** Visits an <code>less than</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThan} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException if an exception occurs during the operation.
     */
    R lessThan(final V value, final LessThan<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: lessThanOrEqual
    /**
     ** Visits a <code>less than or equal to</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThanOrEqual} for
     **                          type <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R lessThanOrEqual(final V value, final LessThanOrEqual<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: startsWith
    /**
     ** Visits a <code>starts with</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link StartsWith} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R startsWith(final V value, final StartsWith<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: endsWith
    /**
     ** Visits a <code>ends with</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link EndsWith} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R endsWith(final V value, final EndsWith<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: contains
    /**
     ** Visits an <code>contains</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Contains} for type
     **                          <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R contains(final V value, final Contains<V> filter)
      throws SystemException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: complex
    /**
     ** Visits an <code>complex</code> filter.
     **
     ** @param  value            the optional value.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link ComplexFilter} for
     **                          type <code>V</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws SystemException  if an exception occurs during the operation.
     */
    R complex(final V value, final ComplexFilter<V> filter)
      throws SystemException;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor method
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of this filter.
   **
   ** @return                    the type of this filter.
   **                            <br>
   **                            Possible object is {@link Type}.
   */
  Type type();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path to the attribute to filter by, or <code>null</code> if
   ** this filter is not a comparison filter or a value filter for complex
   ** multi-valued attributes.
   **
   ** @return                    the path to the attribute to filter by, or
   **                            <code>null</code> if this filter is not a
   **                            comparison filter or a value filter for complex
   **                            multi-valued attributes.
   **                            <br>
   **                            Possible object is {@link Path}.
   */
  Path path();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the comparison value, or <code>null</code> if this filter is not a
   ** comparison filter or a value filter for complex multi-valued attributes.
   **
   ** @return                    the comparison value, or <code>null</code> if
   **                            this filter is not a comparison filter or a
   **                            value filter for complex multi-valued
   **                            attributes.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T value();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isComplex
  /**
   ** Whether this filter is a complex multi-valued attribute value filter.
   **
   ** @return                    <code>true</code> if this filter is a complex
   **                            multi-valued attribute value filter or
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean isComplex();

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Applies a {@link Visitor} to this <code>Filter</code>.
   **
   ** @param  <R>                the return type of the visitor's methods
   **                            return type.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  visitor            the filter visitor.
   **                            <br>
   **                            Allowed object is {@link Visitor}.
   ** @param  value              the optional visitor value.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    a result as specified by the visitor.
   **                            <br>
   **                            Possible object is <code>R</code>.
   **
   ** @throws SystemException    if the filter is not valid for matching.
   */
  <R> R accept(final Visitor<R, T> visitor, final T value)
    throws SystemException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to <code>logically and</code>'s together the two specified
   ** instances of <code>Filter</code>s
   ** <p>
   ** The resulting <i>conjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if at both of the specified filters are <code>true</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>Filter</code> for type
   **                            <code>T</code>.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @return                    the result of <code>(lhs &amp;&amp; rhs)</code>
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> and(final Filter<T> lhs, final Filter<T> rhs) {
    return new And<T>(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code> <code>Filter</code>
   ** using the provided list of <code>Filter</code>s.
   ** <p>
   ** Creating a new <code>logically and</code> <code>Filter</code> with a
   ** <code>null</code> or empty list of <code>Filter</code>s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>Filter</code>
   **                            for type <code>T</code>.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Filter<T> and(final Filter<T>... filters) {
    return and(CollectionUtility.list(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code> <code>Filter</code>
   ** using the provided list of <code>Filter</code>s.
   ** <p>
   ** Creating a new <code>logically and</code> <code>Filter</code> with a
   ** <code>null</code> or empty list of <code>Filter</code>s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>Filter</code>.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> and(final List<Filter<T>> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new And<T>(CollectionUtility.list(filters));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to <code>logically or</code>'s together the two specified
   ** instances of <code>Filter</code>s.
   ** <p>
   ** The resulting <i>disjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    the result of <code>(lhs || rhs)</code>
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> or(final Filter<T> lhs, final Filter<T> rhs) {
    return new Or<T>(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code> <code>Filter</code>
   ** using the provided array of <code>Filter</code>s.
   ** <p>
   ** Creating a new <code>logically or</code> <code>Filter</code> with a
   ** <code>null</code> or empty list of <code>Filter</code>s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>Filter</code>.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Filter<T> or(final Filter<T>... filters) {
    return or(CollectionUtility.list(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code> <code>Filter</code>
   ** using the provided list of <code>Filter</code>s.
   ** <p>
   ** Creating a new <code>logically or</code> <code>Filter</code> with a
   ** <code>null</code> or empty list of <code>Filter</code>s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>Filter</code>.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> or(final List<Filter<T>> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new Or<T>(CollectionUtility.list(filters));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Factory method to create <code>logically negate</code> <code>Filter</code>
   ** from the specified filter expression.
   ** <br>
   ** The resulting <code>Filter</code> is <code>true</code> if and only if the
   ** specified filter expression is <code>false</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filter             the filter expression to negate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> not(final String filter)
    throws ParseException {

    return not(from(filter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Factory method to create <code>logically negate</code> <code>Filter</code>.
   ** <br>
   ** The resulting <code>Filter</code> is <code>true</code> if and only if the
   ** specified filter is <code>false</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  filter             the <code>Filter</code> to negate.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> not(final Filter<T> filter) {
    return new Not<T>(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path
   ** <code>Filter</code> that select only a path if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static Filter pr(final String path)
    throws ParseException {

    return pr(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path
   ** <code>Filter</code> that select only a {@link Path} if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static Filter pr(final Path path) {
    return new Presence(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "BROWN"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric. The values <code>"01"</code> and <code>"1"</code> are unequal
   ** lexically, although they would be equivalent arithmetically.
   ** <p>
   ** Two attributes with binary syntax are equal if and only if their
   ** constituent bytes match.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> eq(final String path, final T value)
    throws ParseException {

    return eq(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path
   ** <code>Filter</code> that select only a {@link Path} with a value that
   ** <em>lexically equal to</em> of the specified <code>value</code>.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "BROWN"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric. The values <code>"01"</code> and <code>"1"</code> are unequal
   ** lexically, although they would be equivalent arithmetically.
   ** <p>
   ** Two attributes with binary syntax are equal if and only if their
   ** constituent bytes match.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> eq(final Path path, final T value) {
    return new Equals<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> lt(final String path, final T value)
    throws ParseException {

    return lt(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> lt(final Path path, final T value) {
    return new LessThan<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> le(final String path, final T value)
    throws ParseException {

    return le(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> le(final Path path, final T value) {
    return new LessThanOrEqual<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> gt(final String path, final T value)
    throws ParseException {

    return gt(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a {@link Path} with a value that is
   ** <em>lexically greater than</em> the specified <code>value</code>.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code>  or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric.
   ** <br>
   ** When compared lexically, <code>"99"</code> is greater than
   ** <code>"123"</code>.
   ** <br>
   ** When compared arithmetically, <code>99</code> is less than
   ** <code>123</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> gt(final Path path, final T value) {
    return new GreaterThan<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> ge(final String path, final T value)
    throws ParseException {

    return ge(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> ge(final Path path, final T value) {
    return new GreaterThanOrEqual<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that <em>initial
   ** substring</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> sw(final String path, final T value)
    throws ParseException {

    return sw(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that <em>initial
   ** substring</em> the specified <code>value</code>.
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "b"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> sw(final Path path, final T value) {
    return new StartsWith<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as a final substring</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> ew(final String path, final T value)
    throws ParseException {

    return ew(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as a final substring</em> the specified <code>value</code>.
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "d"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blonde"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code>
   ** <br>
   ** or that lacks the attribute <code>"hairColor"</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> ew(final Path path, final T value) {
    return new EndsWith<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as any substring</em> the specified <code>value</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> co(final String path, final T value)
    throws ParseException {

    return co(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as any substring</em> the specified <code>value</code>.
   ** <p>
   ** For example, if the specified <code>value</code> were
   ** <code>{"hairColor": "a"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "gray"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "grey"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> co(final Path path, final T value) {
    return new Contains<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Factory method to create a <code>complex</code> input path
   ** <code>Filter</code> with multi-valued attribute.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expression         the filter expression to test against each
   **                            value of the corresponding path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified expression; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path or filter expression could not be
   **                            parsed.
   */
  public static <T> Filter<T> complex(final String path, final String expression)
    throws ParseException {

    return complex(Path.from(path), from(expression));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Factory method to create a new complex multi-valued attribute value
   ** filter.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the <code>Filter</code> value to apply.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the path expression could not be parsed.
   */
  public static <T> Filter<T> complex(final String path, final Filter<T> value)
    throws ParseException {

    return complex(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Factory method to create a new complex multi-valued attribute value
   ** filter.
   **
   ** @param  <T>                the implementation type of the filter value to
   **                            compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the <code>Filter</code> value to apply.
   **                            <br>
   **                            Allowed object is <code>Filter</code> for typr
   **                            <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   */
  public static <T> Filter<T> complex(final Path path, final Filter<T> value) {
    return new ComplexFilter<T>(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parse a filter from its string representation.
   **
   ** @param  <T>                the implementation type of the filter
   **                            value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed filter.
   **                            <br>
   **                            Possible object is <code>Filter</code> for type
   **                            <code>T</code>.
   **
   ** @throws ParseException     if the filter expression could not be parsed.
   */
  public static <T> Filter<T> from(final String expression)
    throws ParseException {

    return EntityParser.<T>filter(expression);
  }
}