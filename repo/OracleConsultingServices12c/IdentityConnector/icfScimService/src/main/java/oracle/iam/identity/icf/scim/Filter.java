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
    Subsystem   :   Generic SCIM Library

    File        :   Filter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Filter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.scim;

import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.databind.node.ValueNode;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

import oracle.iam.identity.icf.rest.ServiceException;

import oracle.iam.identity.icf.scim.schema.Support;

import oracle.iam.identity.icf.scim.filter.Or;
import oracle.iam.identity.icf.scim.filter.And;
import oracle.iam.identity.icf.scim.filter.Not;
import oracle.iam.identity.icf.scim.filter.Equals;
import oracle.iam.identity.icf.scim.filter.Presence;
import oracle.iam.identity.icf.scim.filter.EndsWith;
import oracle.iam.identity.icf.scim.filter.Contains;
import oracle.iam.identity.icf.scim.filter.StartsWith;
import oracle.iam.identity.icf.scim.filter.ComplexFilter;
import oracle.iam.identity.icf.scim.filter.LessThan;
import oracle.iam.identity.icf.scim.filter.GreaterThan;
import oracle.iam.identity.icf.scim.filter.LessThanOrEqual;
import oracle.iam.identity.icf.scim.filter.GreaterThanOrEqual;

////////////////////////////////////////////////////////////////////////////////
// interface Filter
// ~~~~~~~~~ ~~~~~~
/**
 ** Basic interface to match a {@link Path}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Filter {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Type
  // ~~~~ ~~~~
  /**
   ** Defines the set of possible filter types that may be used for SCIM query
   ** filters.
   */
  public enum Type {
      /** the filter type for <code>and</code> filters */
      AND("and")
      /** the filter type for <code>or</code> filters */
    , OR("or")
      /** the filter type for <code>not</code> filters */
    , NOT("not")
      /** the filter type for <code>present</code> filters */
    , PR("pr")
      /** the filter type for <code>equal</code> filters */
    , EQ("eq")
      /** the filter type for <code>not equal</code> filters */
    , NE("ne")
      /** the filter type for <code>greater than</code> filters */
    , GT("gt")
      /** the filter type for <code>greater or equal</code> filters */
    , GE("ge")
      /** the filter type for <code>less than</code> filters */
    , LT("lt")
      /** the filter type for <code>less or equal</code> filters */
    , LE("le")
      /** the filter type for <code>starts with</code> filters */
    , SW("sw")
      /** the filter type for <code>starts ends</code> filters */
    , EW("ew")
      /** the filter type for <code>contains</code> filters */
    , CO("co")
      /** the filter type for complex attribute value filters */
    , COMPLEX("complex")
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
   ** @param  <P>                the type of the additional parameter to this
   **                            visitor's methods. Use {@link java.lang.Void}
   **                            for visitors that do not need an additional
   **                            parameter.
   **                            <br>
   **                            Allowed object is <code>P</code>.
   */
  public interface Visitor<R, P> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>and</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>true</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link And}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final And filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>or</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>false</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Or}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final Or filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>not</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Not}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final Not filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>present</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Presence}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final Presence filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>equality</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Equals}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final Equals filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>greater than</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThan}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final GreaterThan filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>greater than or equal to</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThanOrEqual}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final GreaterThanOrEqual filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>less than</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThan}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final LessThan filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits a <code>less than or equal to</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThanOrEqual}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final LessThanOrEqual filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: startsWith
    /**
     ** Visits a <code>starts with</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link StartsWith}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final StartsWith filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: ew
    /**
     ** Visits a {@code ends with} filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link EndsWith}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final EndsWith filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>contains</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Contains}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final Contains filter, final P parameter)
      throws ServiceException;

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Visits an <code>complex</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link ComplexFilter}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is <code>P</code>.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is <code>P</code>.
     **
     ** @throws ServiceException if an exception occurs during the operation.
     */
    R apply(final ComplexFilter filter, final P parameter)
      throws ServiceException;
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
   **                            Possible object is {@link ValueNode}.
   */
  ValueNode value();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Whether this filter is a complex multi-valued attribute value filter.
   **
   ** @return                    <code>true</code> if this filter is a complex
   **                            multi-valued attribute value filter or
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean complex();

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Applies a {@link Visitor} to this <code>Filter</code>.
   **
   ** @param  <R>                the return type of the visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <P>                the type of the additional parameters to the
   **                            visitor's methods.
   **                            <br>
   **                            Allowed object is <code>&lt;P&gt;</code>.
   ** @param  visitor            the filter visitor.
   **                            <br>
   **                            Allowed object is {@link Visitor}.
   ** @param  parameter          the optional additional visitor parameter.
   **                            <br>
   **                            Allowed object is <code>P</code>.
   **
   ** @return                    a result as specified by the visitor.
   **                            <br>
   **                            Posible object is <code>R</code>.
   **
   ** @throws ServiceException   if the filter is not valid for matching.
   */
  <R, P> R apply(final Visitor<R, P> visitor, final P parameter)
    throws ServiceException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to <code>logically and</code>'s together the two specified
   ** instances of <code>Filter</code>s
   ** <p>
   ** The resulting <i>conjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if at both of the specified filters are <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the result of <code>(lhs &amp;&amp; rhs)</code>
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter and(final Filter lhs, final Filter rhs) {
    return new And(lhs, rhs);
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
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link Filter}.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter and(final Filter... filters) {
    return and(Arrays.asList(filters));
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
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter and(final Collection<Filter> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new And(new ArrayList<Filter>(filters));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to <code>logically or</code>'s together the two specified
   ** instances of <code>Filter</code>s
   ** <p>
   ** The resulting <i>disjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the result of <code>(lhs || rhs)</code>
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter or(final Filter lhs, final Filter rhs) {
    return new Or(lhs, rhs);
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
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link Filter}.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter or(final Filter... filters) {
    return or(Arrays.asList(filters));
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
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter}.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter or(final Collection<Filter> filters) {
    switch (filters.size()) {
      case 0  : return null;
      case 1  : return filters.iterator().next();
      default : return new Or(new ArrayList<Filter>(filters));
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
   ** @param  filter             the filter expression to negate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter not(final String filter)
    throws ServiceException {

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
   ** @param  filter             the <code>Filter</code> to negate.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter not(final Filter filter) {
    return new Not(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path {@link Filter}
   ** that select only a path if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter pr(final String path)
    throws ServiceException {

    return pr(Path.from(path));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path {@link Filter}
   ** that select only a {@link Path} if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter pr(final Path path) {
    return new Presence(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Boolean value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Integer value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Long} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Long value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double containing}<em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Double value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Float value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link Date} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final Date value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().textNode(DateUtility.format(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final String value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final byte[] value)
    throws ServiceException {

    return eq(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em>
   ** of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter eq(final String path, final ValueNode value)
    throws ServiceException {

    return eq(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a {@link Path} with a value that <em>lexically equal
   ** to</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter eq(final Path path, final ValueNode value) {
    return new Equals(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Boolean value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Integer value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Select only an input path with a value for the specified {@link Long}
   ** that is <em>equal to</em> the value of the specified {@link Long}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Long value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Double value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Float value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link Date} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final Date value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().textNode(DateUtility.format(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final String value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final byte[] value)
    throws ServiceException {

    return gt(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter gt(final String path, final ValueNode value)
    throws ServiceException {

    return gt(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** {@link Filter} that select only a {@link Path} with a value that
   ** <em>lexically greater than</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter gt(final Path path, final ValueNode value) {
    return new GreaterThan(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Boolean}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Boolean value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Integer}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Integer value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Long}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Long value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Double}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Double value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Float}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Float value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link Date}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final Date value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().textNode(DateUtility.format(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link String}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final String value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final byte[] value)
    throws ServiceException {

    return ge(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** greater than or equal to</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches or sorts alphabetically after</em>
   **                            the value of the specified {@link ValueNode};
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ge(final String path, final ValueNode value)
    throws ServiceException {

    return ge(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** {@link Filter} that select only a {@link Path} with a value that
   ** <em>lexically greater than or equal to</em> of the specified
   ** {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches or sorts alphabetically after</em>
   **                            the value of the specified {@link ValueNode};
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter ge(final Path path, final ValueNode value) {
    return new GreaterThanOrEqual(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Boolean value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Integer value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Long} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Long value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Double value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Float value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link Date} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final Date value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().textNode(DateUtility.format(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final String value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final byte[] value)
    throws ServiceException {

    return lt(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically less than</em> of
   ** the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>sorts alphabetically before</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter lt(final String path, final ValueNode value)
    throws ServiceException {

    return lt(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path {@link Filter}
   ** that select only a {@link Path} with a value that <em>lexically less
   ** than</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>sorts alphabetically before</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter lt(final Path path, final ValueNode value) {
    return new LessThan(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Boolean}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Boolean value)
    throws ServiceException {

    return le(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Integer}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Integer value)
    throws ServiceException {

    return le(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Long}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Long value)
    throws ServiceException {

    return le(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Double}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Double value)
    throws ServiceException {

    return le(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Float}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Float value)
    throws ServiceException {

    return le(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link Date}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException  if the path expression could not be parsed.
   */
  public static Filter le(final String path, final Date value)
    throws ServiceException {

    return le(path, Support.nodeFactory().textNode(DateUtility.format(value)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link String}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final String value)
    throws ServiceException {

    return le(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final byte[] value)
    throws ServiceException {

    return le(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a path with a value that <em>lexically
   ** less than or equal to</em> of the specified {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches or sorts alphabetically before</em>
   **                            the value of the specified {@link ValueNode};
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter le(final String path, final ValueNode value)
    throws ServiceException {

    return le(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** {@link Filter} that select only a {@link Path} with a value that
   ** <em>lexically less than or equal to</em> of the specified
   **  {@link ValueNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches or sorts alphabetically before</em>
   **                            the value of the specified {@link ValueNode};
   **                            otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter le(final Path path, final ValueNode value) {
    return new LessThanOrEqual(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** {@link Filter} that select only a path with a value that <em>initial
   ** substring</em> of the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter sw(final String path, final String value)
    throws ServiceException {

    return sw(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** {@link Filter} that select only a path with a value that <em>initial
   ** substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains as its first part</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException  if the path expression could not be parsed.
   */
  public static Filter sw(final String path, final ValueNode value)
    throws ServiceException {

    return sw(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** {@link Filter} that select only a {@link Path} with a value that
   ** <em>initial substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains as its first part</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter sw(final Path path, final ValueNode value) {
    return new StartsWith(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path {@link Filter}
   ** that select only a path with a value that <em>contains as a final
   ** substring</em> of the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ew(final String path, final String value)
    throws ServiceException {

    return ew(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path {@link Filter}
   ** that select only a path with a value that <em>contains as a final
   ** substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Filter}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains as its last part</em> the value of
   **                            the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter ew(final String path, final ValueNode value)
    throws ServiceException {

    return ew(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path {@link Filter}
   ** that select only a {@link Path} with a value that <em>contains as a final
   ** substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Filter}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains as its last part</em> the value of
   **                            the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter ew(final Path path, final ValueNode value) {
    return new EndsWith(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path {@link Filter}
   ** that select only a path with a value that <em>contains as any
   ** substring</em> of the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter co(final String path, final String value)
    throws ServiceException {

    return co(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path {@link Filter}
   ** that select only a path with a value that <em>contains as any
   ** substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter co(final String path, final ValueNode value)
    throws ServiceException {

    return co(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path {@link Filter}
   ** that select only a {@link Path} with a value that <em>contains as any
   ** substring</em> of the specified {@link ValueNode}.
   ** <p>
   ** For example, if the specified {@link ValueNode} were
   ** <code>{"hairColor": "a"}</code>, this would match any path with a value
   ** such as
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
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link ValueNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link ValueNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter co(final Path path, final ValueNode value) {
    return new Contains(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Factory method to create <code>complex</code> input path {@link Filter}
   ** with multi-valued attribute.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expression         the filter expression to test against each
   **                            value of the corresponding path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified expression; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path or filter expression could not be
   **                            parsed.
   */
  public static Filter complex(final String path, final String expression)
    throws ServiceException {

    return complex(Path.from(path), from(expression));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** a new complex multi-valued attribute value filter.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Filter} value to apply.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the path expression could not be parsed.
   */
  public static Filter complex(final String path, final Filter value)
    throws ServiceException {

    return complex(Path.from(path), value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** a new complex multi-valued attribute value filter.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Filter} value to apply.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link ValueNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public static Filter complex(final Path path, final Filter value) {
    return new ComplexFilter(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parse a filter from its string representation.
   **
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed filter.
   **                            <br>
   **                            Possible object is {@link Filter}.
   **
   ** @throws ServiceException   if the filter expression could not be parsed.
   */
  public static Filter from(final String expression)
    throws ServiceException {

    return Parser.filter(expression);
  }
}