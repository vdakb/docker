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

    File        :   Size.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Size.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.core.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Size
// ~~~~~ ~~~~
/**
 ** Bean implementation for managing sized values of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Size implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3715781203318348511")
  private static final long serialVersionUID = 6249563106674818218L;

  ////////////////////////////////////////////////////////////////////////////
  // instance attributes
  ////////////////////////////////////////////////////////////////////////////

  /** The width of the <code>Size</code>. */
  double width;

  /** The height of the <code>Size</code>. */
  double height;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Size</code> bean that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Size() {
    this(0, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>Size</code> with width and height limits.
   **
   ** @param  width              the width of this <code>Size</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  height             the height of this <code>Size</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   */
  public Size(final double width, final double height) {
    // ensure inheritance
    super();

    // initailize instance
    this.width  = width;
    this.height = height;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Set the width of this <code>Size</code>.
   **
   ** @param  value              the width of this <code>Size</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Size</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Size</code>.
   */
  public final Size width(final double value) {
    this.width = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lower
  /**
   ** Returns the width of this <code>Size</code>.
   **
   ** @return                    the width of this <code>Size</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public final double width() {
    return this.width;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Set the height of this <code>Size</code>.
   **
   ** @param  value              the height of this <code>Size</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Size</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Size</code>.
   */
  public final Size height(final double value) {
    this.height = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Returns the height of this <code>Size</code>.
   **
   ** @return                    the height of this <code>Size</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public final double height() {
    return this.height;
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
    return 31 * (int)(this.width + this.height);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Size</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Size</code>s may
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

    final Size that = (Size)other;
    if (!(this.width == that.width))
      return false;

    return (this.height == that.height);
  }
}