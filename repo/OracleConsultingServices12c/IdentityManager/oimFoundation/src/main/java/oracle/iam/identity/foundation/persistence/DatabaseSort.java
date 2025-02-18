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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   DatabaseSort.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseSort.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation.persistence;

import oracle.hst.foundation.SystemConstant;

////////////////////////////////////////////////////////////////////////////////
// class DatabaseSort
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The <code>DatabaseSort</code> provides the description of a sort order that
 ** can be applied on Database Statements.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class DatabaseSort {

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
   ** <code>DatabaseSort</code>s.
   */
   public static enum Operator {
      ASCENDING("asc")
    , DESCENDING("desc")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String      value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Operator</code> that allows use as a JavaBean.
     **
     ** @param  value              the value.
     */
    Operator(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                    possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DatabaseSort</code> .
   **
   ** @param  first              the first part of the sort.
   ** @param  second             the second part of the sort.
   ** @param  operator           the operator to combine the first and second
   **                            argument.
   */
  public DatabaseSort(final Object first, final Object second, final Operator operator) {
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
   ** Returns the first argument of this sort order.
   **
   ** @return                    the first argument of this sort order.
   */
  public Object first() {
    return this.first;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   second
  /**
   ** Returns the second argument of this sort order.
   **
   ** @return                    the second argument of this sort order.
   */
  public Object second() {
    return this.second;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operator
  /**
   ** Returns the operation of this sort order.
   **
   ** @return                    the operation of this sort order.
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
   ** Factory method to create a sort order with the specified first part of the
   ** sort order.
   ** <p>
   ** The sort order is per default ascending.
   **
   ** @param  first              the first part of the sort order.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the value provided.
   */
  public static DatabaseSort build(final String first) {
    return buildInternal(first, null, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a sort order with the specified first part of the
   ** sort order.
   ** <p>
   ** The sort order is per default ascending.
   **
   ** @param  first              the first part of the sort order.
   ** @param  operator           the operator to apply on the first argument.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the value provided.
   */
  public static DatabaseSort build(final String first, final Operator operator) {
    return buildInternal(first, null, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a sort order with the specified value.
   **
   ** @param  first              the first part of the sort order.
   ** @param  value              the value of the sort order.
   ** @param  operator           the operator to apply on the first argument.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the value provided.
   */
  public static DatabaseSort build(final Object first, final Object value, final Operator operator) {
    if ((value instanceof String))
      return build(first,(String)value, operator);
    else if ((value instanceof DatabaseSort))
      return build(first,(DatabaseSort)value, operator);
    else
      return build(first,(String)value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a sort order with the specified value.
   **
   ** @param  first              the first part of the sort order.
   ** @param  value              the value of the sort order.
   ** @param  operator           the operator to apply on the first argument.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the value provided.
   */
  public static DatabaseSort build(final Object first, final String value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a sort order with the specified value.
   **
   ** @param  first              the first part of the sort order.
   ** @param  value              the value of the sort order.
   ** @param  operator           the operator to apply on the first argument.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the value provided.
   */
  public static DatabaseSort build(final Object first, final DatabaseSort value, final Operator operator) {
    return buildInternal(first, value, operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Return a string representation of the sort order for debugging
   **
   ** @return                    a string representation of the sort order.
   */
  @Override
  public String toString() {
     final StringBuilder buffer = new StringBuilder();
     buffer.append(this.first).append(SystemConstant.BLANK).append(this.operator).append(SystemConstant.BLANK).append(this.second);
     return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildInternal
  /**
   ** Factory method to create a sort order with the specified value with the
   ** dedicated type.
   **
   ** @param  first              the first part of the sort order.
   ** @param  value              the value of the sort order.
   ** @param  operator           the operator to apply on the first argument.
   **
   ** @return                    an instance of <code>DatabaseSort</code> with
   **                            the specified arguments provided.
   */
  private static DatabaseSort buildInternal(final Object first, final Object value, final Operator operator) {
    return new DatabaseSort(first, value, operator);
  }
}