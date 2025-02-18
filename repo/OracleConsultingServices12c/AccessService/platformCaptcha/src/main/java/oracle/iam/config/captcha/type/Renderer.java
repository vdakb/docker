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

    File        :   Renderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Renderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// class Renderer
// ~~~~~ ~~~~~~~~
/**
 ** Bean implementation for managing rendering properties of Visual Captcha
 ** Service configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Renderer implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1937409433317372829")
  private static final long serialVersionUID = 3772242768889672087L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The limits for legth of the challenge text to generate.
   ** <br>
   ** Default Value: 8...8
   */
  private Limit             length   = Limit.build(8, 8);

  /**
   ** The limits of the colors of challenge text to generate.
   ** <br>
   ** Default Value: 8...8
   */
  private Limit             color    = Limit.build(8, 8);

  /**
   ** The limits for size of a font for challenge text to generate.
   ** <br>
   ** Default Value: 28...32
   */
  private Limit             fontSize = Limit.build(28, 32);

  /**
   ** The limits for size of a font for challenge text to generate.
   ** <br>
   ** Default Value: 28...32
   */
  private Boolean           style  = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Renderer</code> configuration that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Renderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthLower
  /**
   ** Sets the lower limit for the length of challenge text to generate.
   **
   ** @param  value              the lower limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer lengthLower(final Integer value) {
    this.length.lower(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthLower
  /**
   ** Return the lower limit of the font size of challenge text to generate.
   **
   ** @return                    the lower limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer lengthLower() {
    return this.length.lower();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthUpper
  /**
   ** Sets the upper limit for the length of challenge text to generate.
   **
   ** @param  value              the upper limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer lengthUpper(final Integer value) {
    this.length.upper(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lengthUpper
  /**
   ** Return the upper limit of the font size of challenge text to generate.
   **
   ** @return                    the upper limit for the length of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer lengthUpper() {
    return this.length.upper();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Sets the limits for the length of challenge text to generate.
   **
   ** @param  value              the limits for the length of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer length(final Limit value) {
    this.length = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Return the limits for the length of challenge text to generate.
   **
   ** @return                    the limits for the length of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   */
  public Limit length() {
    return this.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Sets the limits of the colors of challenge text to generate.
   **
   ** @param  value              the limits of the colors of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer color(final Limit value) {
    this.color = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Return the limits of the colors of challenge text to generate.
   **
   ** @return                    the limits of the colors of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   */
  public Limit color() {
    return this.color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSizeLower
  /**
   ** Sets the lower limit of the font size of challenge text to generate.
   **
   ** @param  value              the lower limit of the font size of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer fontSizeLower(final Integer value) {
    this.fontSize.lower(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSizeLower
  /**
   ** Return the lower limit of the font size of challenge text to generate.
   **
   ** @return                    the lower limit of the font size of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer fontSizeLower() {
    return this.fontSize.lower();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSizeUpper
  /**
   ** Sets the upper limit of the font size of challenge text to generate.
   **
   ** @param  value              the upper limit of the font size of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer fontSizeUpper(final Integer value) {
    this.fontSize.upper(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSizeUpper
  /**
   ** Return the upper limit of the font size of challenge text to generate.
   **
   ** @return                    the upper limit of the font size of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public Integer fontSizeUpper() {
    return this.fontSize.upper();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSize
  /**
   ** Sets the limits font size of challenge text to generate.
   **
   ** @param  value              the limits font size of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer fontSize(final Limit value) {
    this.fontSize = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fontSize
  /**
   ** Return the limits font size of challenge text to generate.
   **
   ** @return                    the limits font size of challenge text to
   **                            generate.
   **                            <br>
   **                            Allowed object is {@link Limit}.
   */
  public Limit fontSize() {
    return this.fontSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   style
  /**
   ** Set <code>true</code> if the {@code Font} style is randomly choosen.
   **
   ** @param  value              <code>true</code> if the {@code Font} style is
   **                            randomly choosen; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the <code>Renderer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public Renderer style(final boolean value) {
    this.style = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   length
  /**
   ** Return <code>true</code> if the {@code Font} style is randomly choosen.
   **
   ** @return                    <code>true</code> if the {@code Font} style is
   **                            randomly choosen; otherwise <code>false</code>.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean style() {
    return this.style;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Renderer</code> configuration populated
   ** with the default values.
   **
   ** @return                    an newly created instance of
   **                            <code>Renderer</code>.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public static Renderer build() {
    return new Renderer();
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
    int result = this.color != null ? this.color.hashCode() : 0;
    result = 31 * result + (this.length   != null ? this.length.hashCode()   : 0);
    result = 31 * result + (this.fontSize != null ? this.fontSize.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Renderer</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Renderer</code>s may be different even though they contain the same
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

    final Renderer that = (Renderer)other;
    if (this.color != null ? !this.color.equals(that.color) : that.color != null)
      return false;

    if (this.length != null ? !this.length.equals(that.length) : that.length != null)
      return false;

    if (this.fontSize != null ? !this.fontSize.equals(that.fontSize) : that.fontSize != null)
      return false;

    return this.style == that.style;
  }
}