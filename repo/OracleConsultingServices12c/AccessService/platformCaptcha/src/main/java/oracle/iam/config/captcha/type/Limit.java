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

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Service Extension
    Subsystem   :   Captcha Platform Feature

    File        :   Limit.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Limit.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Limit
// ~~~~~ ~~~~~
/**
 ** Bean implementation for managing bounded values of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Limit implements Serializable {
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8724204510875234939")
  private static final long serialVersionUID = -7469667127716576178L;

  ////////////////////////////////////////////////////////////////////////////
  // instance attributes
  ////////////////////////////////////////////////////////////////////////////

  /** The lower bound of the <code>Limit</code>. */
  Integer lower;

  /** The upper bound of the <code>Limit</code>. */
  Integer upper;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>Limit</code> with lower and upper bound limits.
   **
   ** @param  lower              the lower bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  upper              the upper bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  private Limit(final Integer lower, final Integer upper) {
    // ensure inheritance
    super();

    // initailize instance
    this.lower = lower;
    this.upper = upper;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lower
  /**
   ** Set the lower bound of this <code>Limit</code>.
   **
   ** @param  value              the lower bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Limit</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Limit</code>.
   */
  public final Limit lower(final Integer value) {
    this.lower = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lower
  /**
   ** Returns the lower bound of this <code>Limit</code>.
   **
   ** @return                    the lower bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public final Integer lower() {
    return this.lower;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upper
  /**
   ** Set the upper bound of this <code>Limit</code>.
   **
   ** @param  value              the upper bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Limit</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Limit</code>.
   */
  public final Limit upper(final Integer value) {
    this.upper = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   upper
  /**
   ** Returns the upper bound of this <code>Limit</code>.
   **
   ** @return                    the upper bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public final Integer upper() {
    return this.upper;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Limit</code> with the default lenght of
   ** <code>8</code> characters as the lower and uppper limit.
   **
   ** @return                    an newly created instance of
   **                            <code>Limit</code>.
   **                            <br>
   **                            Possible object is <code>Limit</code>.
   */
  public static Limit build() {
    return build(8, 8);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Limit</code> with the default dimension
   ** of <code>8</code> characters as the lower and uppper limit.
   **
   ** @param  lower              the lower bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  upper              the upper bound of this <code>Limit</code>.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an newly created instance of
   **                            <code>Limit</code>.
   **                            <br>
   **                            Possible object is <code>Limit</code>.
   */
  public static Limit build(final Integer lower, final Integer upper) {
    return new Limit(lower, upper);
  }

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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.lower != null ? this.lower.hashCode() : 0;
    result = 31 * result + (this.upper != null ? this.upper.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Limit</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Limit</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Limit that = (Limit)other;
    if (this.lower != null ? !this.lower.equals(that.lower) : that.lower != null)
      return false;

    return !(this.upper != null ? !this.upper.equals(that.upper) : that.upper != null);
  }
}