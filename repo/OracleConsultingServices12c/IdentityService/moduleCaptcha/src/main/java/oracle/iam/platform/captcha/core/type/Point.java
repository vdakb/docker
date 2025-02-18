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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Point.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Point.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.core.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Point
// ~~~~~ ~~~~~
/**
 ** Bean implementation for managing coordinates of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Point implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:689246476366867540")
  private static final long serialVersionUID = -6207808624005145194L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The X coordinate of the <code>Point</code>. */
  double x;

  /** The Y coordinate of the <code>Point</code>. */
  double y;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Point</code> bean that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Point() {
    // ensure inheritance
    this(0.0, 0.0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>Point</code> with the X coordinate and Y coordinate
   ** specified.
   **
   ** @param  x                  the X coordinate of this <code>Point</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  y                  the Y coordinate of this <code>Point</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   */
  public Point(final double x, final double y) {
    // ensure inheritance
    super();

    // initailize instance
    this.x = x;
    this.y = y;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   x
  /**
   ** Set the X coordinate of this <code>Point</code>.
   **
   ** @param  value              the X coordinate of this <code>Point</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Point</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Point</code>.
   */
  public final Point x(final double value) {
    this.x = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   x
  /**
   ** Returns the X coordinate of this <code>Point</code>.
   **
   ** @return                    the X coordinate of this <code>Point</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public final double x() {
    return this.x;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   y
  /**
   ** Set the Y coordinate of this <code>Point</code>.
   **
   ** @param  value              the Y coordinate of this <code>Point</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Point</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Point</code>.
   */
  public final Point y(final double value) {
    this.y = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   y
  /**
   ** Returns the Y coordinate of this <code>Point</code>.
   **
   ** @return                    the Y coordinate of this <code>Point</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public final double y() {
    return this.y;
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
    return 31 * (int)(this.x + this.y);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Point</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Point</code>s may
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

    final Point that = (Point)other;
    if (!(this.x == that.x))
      return false;

    return (this.y == that.y);
  }
}