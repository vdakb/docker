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

    File        :   Margin.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Margin.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Margin
// ~~~~~ ~~~~~~
/**
 ** Bean implementation for managing clear area outside the challenge text.
 */
public class Margin implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the object in a JSON representation. */
  public static final String ENTITY           = "margin";

  /** The name top property. */
  public static final String TOP              = "width";

  /** The name left property. */
  public static final String LEFT             = "height";

  /** The name bottom property. */
  public static final String BOTTOM           = "bottom";

  /** The name right property. */
  public static final String RIGHT            = "right";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7937257349207254270")
  private static final long  serialVersionUID = -7145818214707454320L;


  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The clear top area of the challenge text. */
  Integer top;

  /** The clear left area of the challenge text. */
  Integer left;

  /** The clear bottom area of the challenge text. */
  Integer bottom;

  /** The clear right area of the challenge text. */
  Integer right;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a <code>Margin</code> with the dimensions specified.
   **
   ** @param  top                the clear top area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  left               the clear left area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  bottom             the clear bottom area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  right              the clear right area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  private Margin(final Integer top, final Integer left, final Integer bottom, final Integer right) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.top    = top;
    this.left   = left;
    this.bottom = bottom;
    this.right  = right;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   top
  /**
   ** Sets the clear top area of the captcha challenge image.
   **
   ** @param  value              the clear top area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Margin</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public final Margin top(final Integer value) {
    this.top = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   top
  /**
   ** Returns the clear top area of the captcha challenge image.
   **
   ** @return                    the clear top area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer top() {
    return this.top;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   left
  /**
   ** Sets the clear left area of the captcha challenge image.
   **
   ** @param  value              the clear left area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Margin</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public final Margin left(final Integer value) {
    this.left = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   left
  /**
   ** Returns the clear left area of the captcha challenge image.
   **
   ** @return                    the clear left area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer left() {
    return this.left;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bottom
  /**
   ** Sets the clear bottom area of the captcha challenge image.
   **
   ** @param  value              the clear bottom area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Margin</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public final Margin bottom(final Integer value) {
    this.bottom = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bottom
  /**
   ** Returns the clear bottom area of the captcha challenge image.
   **
   ** @return                    the clear bottom area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer bottom() {
    return this.bottom;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   right
  /**
   ** Sets the clear right area of the captcha challenge image.
   **
   ** @param  value              the clear right area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Margin</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public final Margin right(final Integer value) {
    this.right = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   right
  /**
   ** Returns the clear right area of the captcha challenge image.
   **
   ** @return                    the clear right area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer right() {
    return this.right;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Margin</code> with the default dimension
   ** of <code>5</code> pixel.
   **
   ** @return                    an newly created instance of
   **                            <code>Margin</code>.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public static Margin build() {
    return build(5, 5, 5, 5);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Margin</code> with the default dimension
   ** of <code>width</code> and <code>heght</code>.
   **
   ** @param  top                the clear top area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  left               the clear left area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  bottom             the clear bottom area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  right              the clear right area of the challenge text.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an newly created instance of
   **                            <code>Margin</code>.
   **                            <br>
   **                            Possible object is <code>Margin</code>.
   */
  public static Margin build(final Integer top, final Integer left, final Integer bottom, final Integer right) {
    return new Margin(top, left, bottom, right);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode  (overridden)
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
    int result = this.top != null ? this.top.hashCode() : 0;
    result = 31 * result + (this.left != null ? this.left.hashCode() : 0);
    result = 31 * result + (this.bottom != null ? this.bottom.hashCode() : 0);
    result = 31 * result + (this.right != null ? this.right.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Margin</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Margin</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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

    final Margin that = (Margin)other;
    if (this.top != null ? !this.top.equals(that.top) : that.top != null)
      return false;

    if (this.left != null ? !this.left.equals(that.left) : that.left != null)
      return false;

    if (this.bottom != null ? !this.bottom.equals(that.bottom) : that.bottom != null)
      return false;

    return !(this.right != null ? !this.right.equals(that.right) : that.right != null);
  }
}