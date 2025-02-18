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

    File        :   Space.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Space.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.color;

import java.awt.Color;

////////////////////////////////////////////////////////////////////////////////
// abstract class Space
// ~~~~~~~~ ~~~~~ ~~~~~
/**
 ** <code>Space</code> represents the root class for different color spaces.
 ** <br>
 ** Color spaces represent different ways of describing Color. <code>HSL</code>
 ** and <code>RGB</code> color spaces are isomorphic to three dimensional real
 ** space. As such, this class should only serve as a root for color spaces that
 ** are also isomorphic to three dimensional space.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Space {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class RGB
  // ~~~~~ ~~~
  /**
   ** Useing integer values for red, blue and green and compensates for any
   ** remainder.
   ** <br>
   ** Elements in <code>RGB</code> space are ordered triplets, where each
   ** component is on the range [0, 255], inclusive.
   */
  static class RGB extends Space {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int r;
    private int g;
    private int b;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>RGB</code> color with the amount of red, green and
     ** blue values to their corresponding parameters.
     **
     ** @param  r                the amount of red.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  g                the amount of green.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  b                the amount of blue.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private RGB(final int r, final int g, final int b) {
      // ensure inheritance
      super();

      // initilaize instance
      // keep in range if less than 0 and use remainder if more than 255
      this.r = (r < 0) ? 0 : r % 256;
      this.g = (g < 0) ? 0 : g % 256;
      this.b = (b < 0) ? 0 : b % 256;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: red
    /**
     ** Set the amount of red in the current <code>RGB</code> color.
     ** <br>
	   ** The amount of red is guaranteed to be on the range [0, 255], inclusive.
     **
     ** @param  value            the amount of red.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public void red(final int value) {
      // keep in range if less than 0 and use remainder if more than 255
      this.r = (value < 0) ? 0 : value % 256;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: red
    /**
     ** Returns the amount of red in the current <code>RGB</code> color.
     ** <br>
     ** The amount of red is guaranteed to be on the range [0, 255], inclusive.
     **
     ** @return                  the amount of red.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int red() {
      return this.r;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: green
    /**
     ** Set the amount of green in the current <code>RGB</code> color.
     ** <br>
     ** The amount of green is guaranteed to be on the range [0, 255],
     ** inclusive.
     **
     ** @param  value            the amount of green.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public void green(final int value) {
      // keep in range if less than 0 and use remainder if more than 255
      this.g = (value < 0) ? 0 : value % 256;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: green
    /**
     ** Returns the amount of green in the current <code>RGB</code> color.
     ** <br>
     ** The amount of green is guaranteed to be on the range [0, 255],
     ** inclusive.
     **
     ** @return                  the amount of green.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int green() {
      return this.g;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: blue
    /**
     ** Set the amount of blue in the current <code>RGB</code> color.
     ** <br>
     ** The amount of blue is guaranteed to be on the range [0, 255],
     ** inclusive.
     **
     ** @param  value            the amount of blue.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public void blue(final int value) {
      // keep in range if less than 0 and use remainder if more than 255
      this.b = (value < 0) ? 0 : value % 256;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: green
    /**
     ** Returns the amount of blue in the current <code>RGB</code> color.
      ** <br>
	   ** The amount of blue is guaranteed to be on the range [0, 255],
     ** inclusive.
     **
     ** @return                  the amount of blue.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int blue() {
      return this.b;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hsl (Space)
    /**
     ** Returns the corresponding color in an <code>HSL</code> color space.
     ** <br>
     ** This is a linear transformation between <code>RGB</code> and
     ** <code>HSL</code> color spaces, and represents the equivalent form in
     ** <code>HSL</code> space.
     **
     ** @return                  the <code>HSL</code> color object that
     **                          corresponds to the current instance's
     **                          <code>RGB</code> values.
     **                          <br>
     **                          Possible object is <code>HSL</code>.
     */
    @Override
    public final HSL hsl() {
      final double minimum    = min();
      final double maximum    = max();
      final double lightness  = (maximum + minimum) / 2;
      double       saturation = 0;
      // if they are equal, saturation should be 0
      if (minimum != maximum) {
        if (lightness < 0.5) {
          saturation = (maximum - minimum) / (maximum + minimum);
        }
        else {
          saturation = (maximum - minimum) / (2 - (maximum + minimum));
        }
      }
      return new HSL(hue(), saturation, lightness);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rgb (Space)
    /**
     ** Returns the corresponding color in an <code>RGB</code> color space.
     ** <br>
     ** This is a linear transformation between <code>RGB</code> and
     ** <code>HSL</code> color spaces, and represents the equivalent form in
     ** <code>RGB</code> space.
     **
     ** @return                  the <code>RGB</code> object that corresponds to
     **                          the current instance's <code>HSL</code> values.
     */
    @Override
    public final Space rgb() {
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return 31 * (int)(this.r + this.g + this.b);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>RGB</code> values are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>RGB</code> values may be different even though they contain the
     ** same set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof RGB)) {
        return false;
      }
      final RGB that = (RGB)other;
      return (this.r == that.r && this.g == that.g && this.b == that.b);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hue
    /**
     ** Returns the hue corresponding to the current <code>RGB</code> values.
     ** <p>
     ** Algorithm for hue is based on the Chromaticity plane, which takes the
     ** additive primary colors (red, green, and blue), and puts them all on a
     ** polar graph, where red is at 0 degrees, green is at 120 degrees, and
     ** blue is at 240 degrees.
     ** <br>
     ** More information can be found here:
     ** <a href="https://en.wikipedia.org/wiki/HSL_and_HSV"> https://en.wikipedia.org/wiki/HSL_and_HSV</a>
     **
     ** @return                  the number of degrees away on a chromaticity
     **                          plane.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    private int hue() {
      // cartesian pair for chromaticity plane
      double x = (((this.g - this.b) * .5) * Math.sqrt(3));
      double y = (((2 * this.r) - (this.g + this.b)) * .5);
      // take arctangent for radial direction in polar plane
      double v = Math.atan2(x, y);
      // radians to degree conversion add 360 in order to remove negative radian
      // values from atan2, while preserving value
      v = Math.toDegrees(v) + 360;
      //mod 360 in order to keep in range of [0, 360]
      v %= 360;
      return (int)v;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: max
    /**
     ** Returns the maximum interpolated <code>RGB</code> value.
     **
     ** @return                  the maximum interpolated <code>RGB</code> value
     **                          on range [0, 1], inclusive.
     **                          <br>
     **                          Possible object is <code>double</code>.
     */
    private double max() {
      double v = ((double)this.r / 255);
      double h = ((double)this.g / 255);
      if (h > v) {
        v = h;
      }
      h = ((double)this.b / 255);
      if (h > v) {
        v = h;
      }
      return v;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: min
    /**
     ** Returns the minimum interpolated <code>RGB</code> value.
     **
     ** @return                  the minimum interpolated <code>RGB</code> value
     **                          on range [0, 1], inclusive.
     **                          <br>
     **                          Possible object is <code>double</code>.
     */
    private double min() {
      double v = ((double)this.r / 255);
      double h = ((double)this.g / 255);
      if (h < v) {
        v = h;
      }
      h = ((double)this.b / 255);
      if (h < v) {
        v = h;
      }
      return v;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class HSL
  // ~~~~~ ~~~
  /**
   ** Used for describing a color by <b>h</b>ue, <b>s</b>aturation, and
   ** <b>l</b>ightness.
   */
  public static class HSL extends Space {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private int    h;
    private double s;
    private double l;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>HSL</code> color by setting the hue, saturation, and
     ** lightness values to their corresponding parameters.
     **
     ** @param  h                the degree of the hue.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  s                the percentage of saturation.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     ** @param  l                the percentage of lightness.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     */
    private HSL(final int h, final double s, final double l) {
      // ensure inheritance
      super();

      // initilaize instance attributes
      this.h = (h < 0) ? 360 + (h % 360) : h % 360;
      this.s = (s < 0) ? 0 : (s > 1) ? 1 : s;
      this.l = (l < 0) ? 0 : (l > 1) ? 1 : l;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hue
    /**
     ** Set the hue of the current instance.
     ** <br>
     ** Takes values in range [0, 360], inclusive. However, in the instance
     ** where the hue is not in the given range, modulus will be used in order
     ** to ensure the resulting hue will be in the range [0, 360].
     **
     ** @param  value            the degree of the hue.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public void hue(final int value) {
      this.h = (value < 0) ? 360 + (value % 360) : value % 360;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hue
    /**
     ** Returns the hue of the current instance.
     ** <br>
     ** The resulting hue is guaranteed to be in the range [0, 360].
     **
     ** @return                  the degree of the hue.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int hue() {
      return this.h;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: saturation
    /**
     ** Set the saturation of the current instance on the range [0, 1],
     ** inclusive.
     ** <br>
     ** If the given saturation is outside of these bounds, it is set to the
     ** closest boundary on this range.
     **
     ** @param  value            the percentage of saturation.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     */
    public void saturation(final double value) {
      this.s = (value < 0) ? 0 : (value > 1) ? 1 : value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: saturation
    /**
     ** Returns the saturation of the current instance.
     ** <br>
     ** The saturation is guaranteed to always be on the range [0, 1],
     ** inclusive.
     **
     ** @return                  the percentage of saturation.
     **                          <br>
     **                          Possible object is <code>double</code>.
     */
    public double saturation() {
      return this.s;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: lightness
    /**
     ** Set the lightness of the current instance on the range [0 to 1],
     ** inclusive.
     ** <br>
     ** If the given lightness is outside of these bounds, it is set to the
     ** closest boundary on this range.
     **
     ** @param  value            the percentage of lightness.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     */
    public void lightness(final double value) {
      this.l = (value < 0) ? 0 : (value > 1) ? 1 : value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: lightness
    /**
     ** Returns the lightness of the current instance.
     ** <br>
     ** The lightness is guaranteed to always be on the range [0, 1], inclusive.
     **
     ** @return                  the percentage of lightness.
     **                          <br>
     **                          Possible object is <code>double</code>.
     */
    public double lightness() {
      return this.l;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hsl (Space)
    /**
     ** Returns the corresponding color in an <code>HSL</code> color space.
     ** <br>
     ** This is a linear transformation between <code>RGB</code> and
     ** <code>HSL</code> color spaces, and represents the equivalent form in
     ** <code>RGB</code> space.
     **
     ** @return                  the <code>HSL</code> object that corresponds to
     **                          the current instance's <code>RGB</code> values.
     */
    @Override
    public final Space hsl() {
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: rgb (Space)
    /**
     ** Returns the corresponding color in an <code>RGB</code> color space.
     ** <br>
     ** This is a linear transformation between <code>RGB</code> and
     ** <code>HSL</code> color spaces, and represents the equivalent form in
     ** <code>RGB</code> space.
     **
     ** @return                  the <code>RGB</code> object that corresponds to
     **                          the current instance's <code>HSL</code> values.
     */
    @Override
    public final RGB rgb() {
      return new RGB(red(), green(), blue());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return 31 * (int)(this.h + this.s + this.l);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>HSL</code> values are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>HSL</code> values may be different even though they contain the
     ** same set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof HSL)) {
        return false;
      }
      final HSL that = (HSL)other;
      return (this.hue() == that.hue() && this.saturation() == that.saturation() && this.lightness() == that.lightness());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: incrementHue
    /**
     ** Increment the hue of the current color by the specified amount.
     ** <br>
     ** If a negative value is specified, the hue is decremented instead.
     **
     ** @param  degrees          the number of degrees to increment or decrement
     **                          the hue by.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public void incrementHue(final int degrees) {
      this.h += degrees;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: red
    /**
     ** Returns the amount of red in the <code>HSL</code> color.
     ** <br>
     ** The amount of red is guaranteed to be on the range [0, 255] inclusive.
     **
     ** @return                  the amount of red in the current
     **                          <code>HSL</code> color.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    private int red() {
      double amount = 0;
      // if no saturation there is a shade of gray
      if (saturation() == 0) {
        amount = 255 * lightness();
      }
      else {
        // transformation is based around green as a starting point.
        // since red is 120 degrees away, we need to add an interpolated 120
        // degrees
        double v = (((double)hue() / 360) + ((double)1 / 3));
        if (v < 0) {
          v += 1;
        }
        else if (v > 1) {
          v -= 1;
        }
        amount = convert(v);
      }
      return (int)amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: green
    /**
     ** Returns the amount of green in the <code>HSL</code> color.
     ** <br>
     ** The amount of green is guaranteed to be on the range [0, 255] inclusive.
     **
     ** @return                  the amount of green in the current
     **                          <code>HSL</code> color.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    private int green() {
      double amount = 0;
      // if no saturation there is a shade of gray
      if (saturation() == 0) {
        amount = 255 * lightness();
      }
      else {
        // no need to add an interpolated 120 degrees, since our transformation
        // is based with green as a starting point
        double v = ((double)hue() / 360);
        if (v < 0) {
          v += 1;
        }
        else if (v > 1) {
          v -= 1;
        }
        amount = convert(v);
      }
      return (int)amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: blue
    /**
     ** Returns the amount of blue in the <code>HSL</code> color.
     ** <br>
     ** The amount of blue is guaranteed to be on the range [0, 255] inclusive.
     **
     ** @return                  the amount of blue in the current
     **                          <code>HSL</code> color.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int blue() {
      double amount = 0;
      // if no saturation there is a shade of gray*/
      if (saturation() == 0) {
        amount = 255 * lightness();
      }
      else {
        // transformation is based around green as a starting point.
        // since blue is 120 degrees away like red, we need to subtract an
        // interpolated 120 degrees instead of adding, since blue is in the
        // opposite direction of red in terms of rotation
        double v = (((double)this.h) / 360) - ((double)1 / 3);
        if (v < 0) {
          v += 1;
        }
        else if (v > 1) {
          v -= 1;
        }
        amount = convert(v);
      }
      return (int)amount;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: convert
    /**
     ** Returns the conversion for an <code>HSL</code> to an <code>RGB</code>
     ** color space.
     ** <br>
     ** The math conversion can be done with a 3x3 matrix transformation.
     ** Information about the implemented algorithm can be found here:
     ** <a href="http://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/">
     ** http://www.niwa.nu/2013/05/math-behind-colorspace-conversions-rgb-hsl/
     ** </a>
     **
     ** @param  value            the interpolated value to transform into
     **                          <code>RGB</code> color space.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     **
     ** @return                  the color's corresponding interpolated value in
     **                          <code>RGB</code> color space, in range of 0 to
     **                          1, inclusive.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    private int convert(final double value) {
      double t1;
      if (lightness() < .5) {
        t1 = lightness() * (1.0 + saturation());
      }
      else {
        t1 = lightness() + saturation() - (lightness() * saturation());
      }

      double v  = 0;
      double t2 = (2 * lightness()) - t1;
      // if color value is in range of 0 to 60 degrees
      if (value * 6 < 1) {
        v = t2 + ((t1 - t2) * 6 * value);
      }
      // if color value is in range of 60 to 180 degrees
      else if (value * 2 < 1) {
        v = t1;
      }
      // if color value is in range of 180 to 240 degrees
      else if (value * 3 < 2) {
        v = t2 + ((t1 - t2) * (4 - (6 * value)));
      }
      // otherwise in range of 180 to 360 degrees
      else {
        v = t2;
      }
      // put in range of 0, 255, inclusive
      v *= 255;
      // prevent any truncating to keep as accurate as possible
      v = Math.round(v);
      return (int)v;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Space</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Space() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   awt
  public static Color awt(final Space space) {
    return (space instanceof HSL) ? awt(((HSL)space).rgb()) : awt((RGB)space);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-primary</code>
   **   <li><code>.text-primary</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space primary() {
    // #007bff
    return build(new RGB(47, 112, 225));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secondary
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-secondary</code>
   **   <li><code>.text-secondary</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space secondary() {
    // #6c757d
    return build(new RGB(108, 117, 125));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   success
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-success</code>
   **   <li><code>.text-success</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space success() {
    // #28a745
    return build(new RGB(40, 138, 69));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   danger
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-danger</code>
   **   <li><code>.text-danger</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space danger() {
    // #dc3545
    return build(new RGB(220, 53, 69));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-info</code>
   **   <li><code>.text-info</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space info() {
    // #17a2b8
    return build(new RGB(23, 162, 184));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-warning</code>
   **   <li><code>.text-warning</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space warning() {
    // #ffc107
    return build(new RGB(255, 193, 7));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ligth
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-light</code>
   **   <li><code>.text-light</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space ligth() {
    // #f8f9fa
    return build(new RGB(248, 249, 250));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dark
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-dark</code>
   **   <li><code>.text-dark</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space dark() {
    // #343a40
    return build(new RGB(52, 58, 64));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   muted
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-muted</code>
   **   <li><code>.text-muted</code>
   ** </ul>
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space muted() {
    // #6c757d
    return build(new RGB(108, 117, 125));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   antiqueWhite
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space antiqueWhite() {
    // #faebd7
    return build(new RGB(250, 235, 215));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oldLace
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space oldLace() {
    // #fdf5e6
    return build(new RGB(253, 245, 230));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ivory
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space ivory() {
    // #fffff0
    return build(new RGB(255, 255, 240));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   seaShell
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space seaShell() {
    // #fff5ee
    return build(new RGB(255, 245, 238));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ghostWhite
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space ghostWhite() {
    // #f8f8ff
    return build(new RGB(248, 248, 255));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   snow
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space snow() {
    // #fffafa
    return build(new RGB(255, 250, 250));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linen
  /**
   ** White similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space linen() {
    // #faf0e6
    return build(new RGB(250, 240, 230));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   darkGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   **
   ** @see     Color#darkGray
   */
  public static Space darkGray() {
    // #a9a9a9
    return build(new RGB(166, 166, 166));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gray
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   **
   ** @see     Color#gray
   */
  public static Space gray() {
    // #808080
    return build(new RGB(128, 128, 128));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   silver
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   **
   ** @see     Color#silver
   */
  public static Space silver() {
    // #c0c0c0
    return build(new RGB(192, 192, 192));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pinball
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   **
   ** @see     Color#silver
   */
  public static Space pinball() {
    // #d3d3d3
    return build(new RGB(211, 211, 211));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warmGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space warmGray() {
    // #857570
    return build(new RGB(133, 117, 112));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coolGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space coolGray() {
    // #767a85
    return build(new RGB(118, 122, 133));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blackOut
  /**
   ** Gray similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space blackOut() {
    // #222222
    return build(new RGB(34, 34, 34));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   laRioja
  /**
   ** Green similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space laRioja() {
    // #bac00e
    return build(new RGB(186, 192, 14));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lakeGreen
  /**
   ** Green similarity color.
   ** <br>
   ** Also know as seaGreen.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space lakeGreen() {
    // #2e8b57
    return build(new RGB(46, 139, 87));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   limeGreen
  /**
   ** Green similarity color.
   ** <br>
   ** Also know as seaGreen.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space limeGreen() {
    // #32cdaa
    return build(new RGB(50, 205, 170));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   orientalPink
  /**
   ** Red similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space orientalPink() {
    // #c28e88
    return build(new RGB(227, 38, 54));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   alizarinCrimson
  /**
   ** Red similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space alizarinCrimson() {
    // #e32636
    return build(new RGB(227, 38, 54));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   manganese
  /**
   ** Red similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space manganese() {
    // #e52b50
    return build(new RGB(229, 43, 80));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   aliceBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space aliceBlue() {
    // #f0f8ff
    return build(new RGB(240, 248, 255));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coolBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space coolBlue() {
    // #142850
    return build(new RGB(20, 40, 80));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hmmmmBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space hmmmmBlue() {
    // #15328c
    return build(new RGB(21, 50, 140));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lightBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space lightBlue() {
    // #add8e6
    return build(new RGB(173, 216, 230));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mediumBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space mediumBlue() {
    // #0000cd
    return build(new RGB(0, 0, 205));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   midnightBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space midnightBlue() {
    // #191970
    return build(new RGB(25, 25, 112));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   navy
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space navy() {
    // #000080
    return build(new RGB(0, 0, 128));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   powderBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space powderBlue() {
    // #b0e0e6
    return build(new RGB(176, 224, 230));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   royalBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space royalBlue() {
    // #4169e1
    return build(new RGB(65, 105, 225));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ameixa
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space ameixa() {
    // #6a5acd
    return build(new RGB(106, 90, 205));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absoluteZero
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space absoluteZero() {
    // #0048ba
    return build(new RGB(0, 72, 186));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   steelBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the <code>Space</code> evaluated from the
   **                            <code>RGB</code> components.
   **                            <br>
   **                            Possible object is <code>Space</code>.
   */
  public static Space steelBlue() {
    // #4682b4
    return build(new RGB(103, 153, 170));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hsl
  /**
   ** Factory method used to create the given color in <code>HSL</code> space.
   **
   ** @param  space              the <code>Space</code> reference containing the
   **                            color to get in <code>HSL</code> space.
   **                            <br>
   **                            Allowed object is <code>Space</code>.
   **
   ** @return                    the <code>HSL</code> color object containing
   **                            the given color in <code>HSL</code> space.
   **                            <br>
   **                            Possible object is <code>HSL</code>.
   */
  static HSL hsl(final Space space) {
    return (space instanceof HSL) ? (HSL)space : ((RGB)space).hsl();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hsl
  /**
   ** Factory method used to create the given color in <code>HSL</code> space.
   **
   ** @param  h                  the degree of the hue.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  s                  the percentage of saturation.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  l                  the percentage of lightness.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>HSL</code> color object containing
   **                            the given color in <code>HSL</code> space.
   **                            <br>
   **                            Possible object is <code>HSL</code>.
   */
  public static HSL hsl(final int h, final double s, final double l) {
    return new HSL(h, s,l);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Factory method used to create the given color in <code>RGB</code> space.
   **
   ** @param  space              the <code>Space</code> reference containing the
   **                            color to get in <code>RGB</code> space.
   **                            <br>
   **                            Allowed object is <code>Space</code>.
   **
   ** @return                    the <code>RGB</code> color object containing
   **                            the given color in <code>RGB</code> space.
   **                            <br>
   **                            Possible object is <code>RGB</code>.
   */
  static RGB rgb(final Space space) {
    return (space instanceof RGB) ? (RGB)space: ((HSL)space).rgb();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Factory method used to create the given color in <code>RGB</code> space.
   **
   ** @param  r                  the amount of red.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  g                  the amount of green.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  b                  the amount of blue.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RGB</code> color object containing
   **                            the given color in <code>RGB</code> space.
   **                            <br>
   **                            Possible object is <code>RGB</code>.
   */
  public static RGB rgb(final int r, final int g, final int b) {
    return new RGB(r, g, b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hsl
  /**
   ** Returns the corresponding color in an <code>HSL</code> color space.
   ** <br>
   ** This is a linear transformation between <code>RGB</code> and
   ** <code>HSL</code> color spaces, and represents the equivalent form in
   ** <code>RGB</code> space.
   **
   ** @return                  the <code>HSL</code> object that corresponds to
   **                          the current instance's <code>RGB</code> values.
   */
  public abstract Space hsl();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Returns the corresponding color in an <code>RGB</code> color space.
   ** <br>
   ** This is a linear transformation between <code>RGB</code> and
   ** <code>HSL</code> color spaces, and represents the equivalent form in
   ** <code>RGB</code> space.
   **
   ** @return                  the <code>RGB</code> object that corresponds to
   **                          the current instance's <code>HSL</code> values.
   */
  public abstract Space rgb();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   awt
  private static Color awt(final RGB value) {
    return new Color(value.r, value.g, value.b);
  }
}