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

    File        :   Canvas.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Canvas.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Canvas
// ~~~~~ ~~~~~~
/**
 ** Bean implementation for managing rendering properties of Visual Captcha
 ** Service configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Canvas implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /** The name of the object in a JSON representation. */
  public static final String ENTITY           = "canvas";

  /** The name width property. */
  public static final String WIDTH            = "width";

  /** The name width property. */
  public static final String HEIGHT           = "height";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2435236040140578079")
  private static final long  serialVersionUID = -6834865297771381767L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The size of the visual captcha to generate.
   ** <br>
   ** Default Value: 160x70
   */
  private Size     size     = Size.build(160, 70);

  /**
   ** The clear area around the challenge text.
   ** <br>
   ** The margin is filled with the configure canvas factory.
   ** <br>
   ** Default Value: 5
   */
  private Margin   margin   = Margin.build();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Canvas</code> configuration that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Canvas() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Sets the width of the visual captcha to generate.
   **
   ** @param  value              the width of the visual captcha to generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas width(final int value) {
    this.size.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Returns the width of the visual captcha to generate.
   **
   ** @return                    the width of the visual captcha to generate.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int width() {
    return this.size.width;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Sets the height of the visual captcha to generate.
   **
   ** @param  value              the height of the visual captcha to generate.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas height(final int value) {
    this.size.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Returns the height of the visual captcha to generate.
   **
   ** @return                    the height of the visual captcha to generate.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int height() {
    return this.size.height;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Sets the size of the visual captcha to generate.
   **
   ** @param  value              the size of the visual captcha to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas size(final Size value) {
    this.size = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the size of the visual captcha to generate.
   **
   ** @return                    the size of the visual captcha to generate.
   **                            <br>
   **                            Possible object is {@link Size}.
   */
  public Size size() {
    return this.size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginTop
  /**
   ** Sets the clear top area of the captcha challenge image.
   **
   ** @param  value              the clear top area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas marginTop(final Integer value) {
     this.margin.top(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginTop
  /**
   ** Return the clear top area of the captcha challenge image.
   **
   ** @return                    the clear top area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer marginTop() {
    return this.margin.top();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginLeft
  /**
   ** Sets the clear left area of the captcha challenge image.
   **
   ** @param  value              the clear left area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas marginLeft(final Integer value) {
     this.margin.left(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginLeft
  /**
   ** Return the clear left area of the captcha challenge image.
   **
   ** @return                    the clear left area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer marginLeft() {
    return this.margin.left();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginBottom
  /**
   ** Sets the clear bottom area of the captcha challenge image.
   **
   ** @param  value              the clear bottom area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas marginBottom(final Integer value) {
     this.margin.bottom(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginBottom
  /**
   ** Return the clear bottom area of the captcha challenge image.
   **
   ** @return                    the clear bottom area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer marginBottom() {
    return this.margin.bottom();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginRight
  /**
   ** Sets the clear right area of the captcha challenge image.
   **
   ** @param  value              the clear right area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas marginRight(final Integer value) {
     this.margin.right(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marginRight
  /**
   ** Return the clear right area of the captcha challenge image.
   **
   ** @return                    the clear right area of the captcha challenge
   **                            image.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer marginRight() {
    return this.margin.right();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   margin
  /**
   ** Sets the clear area around the challenge text.
   **
   ** @param  value              the clear area around the challenge text.
   **                            <br>
   **                            Allowed object is {@link Margin}.
   **
   ** @return                    the <code>Canvas</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public Canvas margin(final Margin value) {
    this.margin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   margin
  /**
   ** Returns the clear area around the challenge text.
   **
   ** @return                    the clear area around the challenge text.
   **                            <br>
   **                            Possible object is {@link Margin}.
   */
  public Margin margin() {
    return this.margin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Canvas</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Canvas</code>.
   **                            <br>
   **                            Possible object is <code>Canvas</code>.
   */
  public static Canvas build() {
    return new Canvas();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
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
    int result = this.size != null ? this.size.hashCode() : 0;
    result = 31 * result + (this.margin != null ? this.margin.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Canvas</code>es are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Canvas</code>es may be different even though they contain the same
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

    final Canvas that = (Canvas)other;
    if (this.size != null ? !this.size.equals(that.size) : that.size != null)
      return false;

    return (this.margin != null ? !this.margin.equals(that.margin) : that.margin != null);
  }
}