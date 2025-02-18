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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared collection facilities

    File        :   FilterBuilder.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilterBuilder.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import oracle.hst.foundation.object.filter.Or;
import oracle.hst.foundation.object.filter.And;
import oracle.hst.foundation.object.filter.Not;
import oracle.hst.foundation.object.filter.Equals;
import oracle.hst.foundation.object.filter.Presence;
import oracle.hst.foundation.object.filter.Contains;
import oracle.hst.foundation.object.filter.EndsWith;
import oracle.hst.foundation.object.filter.LessThan;
import oracle.hst.foundation.object.filter.StartsWith;
import oracle.hst.foundation.object.filter.ContainsAll;
import oracle.hst.foundation.object.filter.GreaterThan;
import oracle.hst.foundation.object.filter.LessThanOrEqual;
import oracle.hst.foundation.object.filter.GreaterThanOrEqual;

import oracle.hst.foundation.object.parser.FilterFactory;

////////////////////////////////////////////////////////////////////////////////
// final class FilterBuilder
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** FilterBuilder creates a {@link Filter filter} that will
 ** {@link Filter#accept match} any {@link Entity} that satisfies all of the
 ** selection criteria that were specified using this builder.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public final class FilterBuilder {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor (private)
  /**
   ** Default constructor
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new FilterBuilder()" and enforces use of the public factory method
   ** below.
   */
   private FilterBuilder() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Logically "ANDs" together the two specified instances of {@link Filter}.
   ** The resulting <i>conjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if both of the specified filters are <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   ** @param  rhs                the right-hand-side filter.
   **
   ** @return                    the result of <code>(lhs &amp;&amp; rhs)</code>
   */
  public static Filter and(final Filter lhs, final Filter rhs) {
    return new And(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Creates a new "AND" filter using the provided list of filters.
   ** <p>
   ** Creating a new "AND" filter with a <code>null</code> or empty list of
   ** sub-filters is equivalent to calling "alwaysTrue".
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **
   ** @return                    the newly created "AND" filter.
   */
  public static Filter and(final Filter... filters) {
    return and(Arrays.asList(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Creates a new "AND" filter using the provided list of filters.
   ** <p>
   ** Creating a new "AND" filter with a <code>null</code> or empty list of
   ** sub-filters is equivalent to calling "alwaysTrue".
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **
   ** @return                    the newly created "AND" filter.
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
   ** Logically "OR" together the two specified instances of {@link Filter}.
   ** The resulting <i>disjunct</i> <code>Filter</code> is <code>true</code> if
   ** and only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   ** @param  rhs                the right-hand-side filter.
   **
   ** @return                    the result of <code>(lhs || rhs)</code>
   */
  public static Filter or(final Filter lhs, final Filter rhs) {
    return new Or(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Creates a new "OR" filter using the provided list of filters.
   ** <p>
   ** Creating a new "OR" filter with a <code>null</code> or empty list of
   ** filters is equivalent to "alwaysTrue".
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **
   ** @return                    the newly created "OR" filter.
   */
  public static Filter or(final Filter... filters) {
    return or(Arrays.asList(filters));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Creates a new "OR" filter using the provided list of filters.
   ** <p>
   ** Creating a new "OR" filter with a <code>null</code> or empty list of
   ** filters is equivalent to "alwaysTrue".
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **
   ** @return                    the newly created "OR" filter.
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
   ** Logically negate the specified {@link Filter}. The resulting
   ** <code>Filter</code> is <code>true</code> if and only if the specified
   ** filter is <code>false</code>.
   **
   ** @param  filter             the <code>Filter</code> to negate.
   **
   ** @return                    the result of <code>(!filter)</code>.
   */
  public static Filter not(final Filter filter) {
    return new Not(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   present
  /**
   ** Creates a new {@code presence} filter using the provided attribute name.
   **
   ** @param  attributeName      the name of field within the {@link Entity}
   **                            which must be present.
   **
   ** @return                    the newly created {@code presence} filter.
   */
  public static Filter present(final String attributeName) {
    return new Presence(attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that is <em>lexically equal to</em> the value of the
   ** specified {@link Attribute}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Entity}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "BROWN"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
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
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter equal(final Attribute attribute) {
    return new Equals(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThan
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that is <em>lexically greater than</em> the value of the
   ** specified {@link Attribute}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Entity}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
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
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>sorts alphabetically after</em> the value
   **                            of the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter greaterThan(final Attribute attribute) {
    return new GreaterThan(attribute);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   greaterThanOrEqual
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that is <em>lexically greater than or equal to</em> the
   ** value of the specified {@link Attribute}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Entity}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
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
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>matches or sorts alphabetically after</em>
   **                            the value of the specified {@link Attribute};
   **                            otherwise <code>false</code>.
   */
  public static Filter greaterThanOrEqual(final Attribute attribute) {
    return new GreaterThanOrEqual(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThan
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that is <em>lexically less than</em> the value of the
   ** specified {@link Attribute}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Entity}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
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
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>sorts alphabetically before</em> the value
   **                            of the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter lessThan(final Attribute attribute) {
    return new LessThan(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lessThanOrEqual
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that is <em>lexically less than or equal to</em> the
   ** value of the specified {@link Attribute}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Entity}
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
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
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
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>matches or sorts alphabetically before</em>
   **                            the value of the specified {@link Attribute};
   **                            otherwise <code>false</code>.
   */
  public static Filter lessThanOrEqual(final Attribute attribute) {
    return new LessThanOrEqual(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that contains as an <em>initial substring</em> the value
   ** of the specified {@link Attribute}.
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "b"}</code>, this would match any {@link Entity} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>contains as its first part</em> the value
   **                            of the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter startsWith(final Attribute attribute) {
    return new StartsWith(attribute, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   endsWith
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that <em>contains as a final substring</em> the
   ** value of the specified {@link Attribute}.
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "d"}</code>, this would match any {@link Entity} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blonde"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
   ** <code>{"hairColor": null}</code>
   ** <br>
   ** or that lacks the attribute <code>"hairColor"</code>.
   **
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>contains as its last part</em> the value of
   **                            the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter endsWith(final Attribute attribute) {
    return new EndsWith(attribute, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that <em>contains as any substring</em> the value of the
   ** specified {@link Attribute}.
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "a"}</code>, this would match any {@link Entity} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "gray"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "grey"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Entity} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter contains(final Attribute attribute) {
    return new Contains(attribute, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAll
  /**
   ** Select only an input {@link Entity} with a value for the specified
   ** {@link Attribute} that contains all the values from the specified
   ** {@link Attribute}.
   ** <p>
   ** For example, if the specified {@link Attribute} were
   ** <code>{"hairColor": "brown", "red"}</code>, this would match any
   ** {@link Entity} with values such as
   ** <ul>
   **   <li><code>{"hairColor": "black", "brown", "red"}</code>
   **   <li><code>{"hairColor": "blond", "brown", "red"}</code>
   **   <li><code>{"hairColor": "auburn", "brown", "red"}</code>
   ** </ul>
   ** but would <em>not</em> match any {@link Entity} that contains only
   ** <ul>
   **   <li><code>{"hairColor": "brown"}</code>
   **   <li><code>{"hairColor": "brownish-gray"}</code>
   **   <li><code>{"hairColor": "red"}</code>
   ** </ul>
   ** This also would <em>not</em> match any {@link Entity} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric.
   **
   ** @param  attribute          the {@link Attribute} <em>containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Entity} attribute.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Entity}
   **                            <em>sorts alphabetically before</em> the value
   **                            of the specified {@link Attribute}; otherwise
   **                            <code>false</code>.
   */
  public static Filter containsAll(final Attribute attribute) {
    return new ContainsAll(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link Filter} that handles the provided
   ** expression.
   **
   ** @param  expression         the string expression to handle by the created
   **                            {@link Filter}.
   **
   ** @return                    the {@link Filter} to handle the given
   **                            expresssion.
   */
  public static Filter build(final String expression) {
    return FilterFactory.build(expression);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a {@link Filter} that handles the provided
   ** expression.
   **
   ** @param  expression         the string expression to handle by the created
   **                            {@link Filter}.
   ** @param  dateAttributes     the comma separated list of attribute names
   **                            which are represent data values.
   ** @param  dateFormat         the format definition for every data attributs.
   **
   ** @return                    the {@link Filter} to handle the given
   **                            expresssion.
   */
  public static Filter build(final String expression, final String dateAttributes, final String dateFormat) {
    return FilterFactory.build(expression, dateAttributes, dateFormat);
  }
}