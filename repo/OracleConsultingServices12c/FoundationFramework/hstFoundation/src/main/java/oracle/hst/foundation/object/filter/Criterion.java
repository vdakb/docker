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

    File        :   Criterion.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Criterion.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object.filter;

import java.io.Serializable;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class Criterion
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>Criterion</code> provides the description of a filter that can be
 ** applied on filterable values.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   0.0.0.2
 */
public class Criterion implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** a criterion filter declaration that is a EQUAL-filter */
  public final Operator     OPERATOR_DEFAULT = Operator.EQUAL;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-9101330200777684370")
  private static final long serialVersionUID = -6217695402356579052L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String      name;
  private final String      value;
  private final Operator    operator;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Operator
  // ~~~~~ ~~~~~~~
  /**
   ** This enum store the operational combinations of <code>Criterion</code>s.
   */
  public static enum Operator {
      EQUAL
    , NOT_EQUAL
    , LESS_EQUAL
    , LESS_THAN
    , GREATER_THAN
    , GREATER_EQUAL
    , CONTAINS
    , NOT_CONTAINS
    , STARTS_WITH
    , NOT_STARTS_WITH
    , ENDS_WITH
    , NOT_ENDS_WITH
    ;
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
   ** Constructs a <code>Criterion</code> that use the specified
   ** <code>Operator.EQUAL</code> to compare the value of a datasource which has
   ** the same name specified by <code>name</code> with the <code>value</code>
   ** passed in.
   **
   ** @param  name               the name of the attribute in the data source.
   ** @param  value              the value to match with the value fetched from
   **                            the data source.
   */
  public Criterion(final String name, final String value) {
    // ensure inheritance
    this(name, value, Operator.EQUAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Criteria</code> which populates its properties from the
   ** specified <code>criteria</code>.
   ** <br>
   ** Copy Constructor
   **
   ** @param  other              the template <code>Criteria</code> to populate
   **                            the instance attributes from.
   */
  public Criterion(final Criterion other) {
    // ensure inheritance
    this(other.name, other.value, other.operator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Criterion</code> that use the specified
   ** <code>operator</code> to compare the value of a datasource which has the
   ** same name specified by <code>name</code> with the <code>value</code>
   ** passed in.
   **
   ** @param  name               the name of the attribute in the data source.
   ** @param  value              the value to match with the value fetched from
   **                            the data source.
   ** @param  operator           the operator how to match.
   */
  public Criterion(final String name, final String value, final Operator operator) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (name == null)
      throw new NullPointerException("name");

    // initialize instance attributes
    this.name     = name;
    this.value    = value;
    this.operator = operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Criterion</code>.
   **
   ** @return                    the name of the <code>Criterion</code>.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of the <code>Criterion</code>.
   **
   ** @return                    the value of the <code>Criterion</code>.
   */
  public String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operator
  /**
   ** Returns the operator of the <code>Criterion</code>.
   **
   ** @return                    the operator of the <code>Criterion</code>.
   */
  public Operator operator() {
    return this.operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash + this.name.hashCode();
    hash = 31 * hash + this.value.hashCode();
    hash = 31 * hash + this.operator.hashCode();
    return hash;
  }

  /////////////////////////////////////////////////////////////////////////////
  // Method: equals (overriden)
  /**
   ** Compares this instance with the specified object.
   ** <p>
   ** The result is <code>true</code> if and only if the argument is not
   ** <code>null</code> and is a <code>Criterion</code> object that represents
   ** the same <code>name</code>.
   **
   ** @param other             the object to compare this <code>Criterion</code>
   **                          with.
   **
   ** @return                  <code>true</code> if the <code>Criterion</code>s
   **                          are equal; <code>false</code> otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other == this) {
      return true;
    }
    else if (!(other instanceof Criterion))
      return false;

    final Criterion that = (Criterion)other;
    return StringUtility.isEqual(this.name,  that.name)
        && StringUtility.isEqual(this.value, that.value)
        && this.operator == that.operator;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Return a string representation of the <code>Criterion</code> for debugging
   **
   ** @return                    a string representation of the
   **                            <code>Criterion</code>.
   */
  @Override
  public String toString() {
    return String.format("%s %s %s", this.name, this.operator, this.value);
  }
}