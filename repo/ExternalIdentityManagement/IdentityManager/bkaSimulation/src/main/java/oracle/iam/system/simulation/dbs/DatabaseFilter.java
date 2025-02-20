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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Generic SCIM Interface

    File        :   DatabaseFilter.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseFilter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.dbs;

import java.util.Date;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseFilter
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>DatabaseFilter</code> provides the description of a filter that
 ** can be applied on Database Statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DatabaseFilter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A database filter declaration that is a NOP-filter
   */
  public static final DatabaseFilter NOP = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Object   first;
  private final Object   second;
  private final Operator operator;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Operator
  // ~~~~~ ~~~~~~~
  /**
   ** This enum store the operational combinations of
   ** <code>DatabaseFilter</code>s.
   */
  public static enum Operator {
    AND, OR, EQ, NOT_EQ, GT, GE, LT, LE, SW, NOT_SW, EW, NOT_EW, CO, NOT_CO, IN, NOT_IN, HIERARCHY;

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseFilter</code> .
   **
   ** @param  first              the first part of the filter.
   ** @param  second             the second part of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   */
  private DatabaseFilter(final Object first, final Object second, final Operator operator) {
    // ensure inheritance
    super();

    this.first    = first;
    this.second   = second;
    this.operator = operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   first
  /**
   ** Returns the first argument of this filter.
   **
   ** @return                    the first argument of this filter.
   */
  public Object first() {
    return this.first;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   second
  /**
   ** Returns the second argument of this filter.
   **
   ** @return                    the second argument of this filter.
   */
  public Object second() {
    return this.second;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operator
  /**
   ** Returns the operation of this filter.
   **
   ** @return                    the operation of this filter.
   */
  public Operator operator() {
    return this.operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Object value, final Operator operator) {
    if ((value instanceof Boolean))
      return build(first,(Boolean)value, operator);
    else if ((value instanceof Character))
      return build(first,(Character)value, operator);
    else if ((value instanceof Integer))
      return build(first,(Integer)value, operator);
    else if ((value instanceof Long))
      return build(first,(Long)value, operator);
    else if ((value instanceof Double))
      return build(first,(Double)value, operator);
    else if ((value instanceof Float))
      return build(first,(Float)value, operator);
    else if ((value instanceof String))
      return build(first,(String)value, operator);
    else if ((value instanceof DatabaseFilter))
      return build(first,(DatabaseFilter)value, operator);
    else if ((value instanceof DatabaseEntity))
      return build(first,(DatabaseEntity)value, operator);
    else
      return build(first,(String)value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the parameter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Boolean value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Character value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Integer value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Long value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Double value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Float value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final Date value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final String value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final DatabaseFilter value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a filter with the specified value.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code>
   **                            with the value provided.
   */
  public static DatabaseFilter build(final Object first, final DatabaseEntity value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildInternal
  /**
   ** Factory method to create a filter with the specified value with the
   ** dedicated type.
   **
   ** @param  first              the first part of the filter.
   ** @param  value              the value of the filter.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   **
   ** @return                    an instance of <code>DatabaseFilter</code> with
   **                            the specified arguments provided.
   */
  private static DatabaseFilter buildInternal(final Object first, final Object value, final Operator operator) {
    return new DatabaseFilter(first, value, operator);
  }
}