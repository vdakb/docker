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

    File        :   Wobble.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Wobble.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.visual.type;

import java.io.Serializable;

import oracle.iam.platform.captcha.core.type.Size;

////////////////////////////////////////////////////////////////////////////////
// class Wobble
// ~~~~~ ~~~~~~
/**
 ** Bean implementation for managing wobble effects of Visual Captcha Service
 ** configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Wobble implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8565390820549986925")
  private static final long serialVersionUID = -6472399602537213864L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  Size scale     = new Size();
  Size wave      = new Size(15.0, 15.0);
  Size amplitude = new Size(4.0, 3.0);

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Wobble</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Wobble() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveLength
  /**
   ** Set the length of the wave property.
   **
   ** @param  value              the length of the wave property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble waveLength(final double value) {
    this.wave.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveLength
  /**
   ** Returns the length of the wave property.
   **
   ** @return                    the length of the wave property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double waveLength() {
    return this.wave.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Set the height of the wave property.
   **
   ** @param  value              the height of the wave property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble waveHeight(final double value) {
    this.wave.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Returns the height of the wave property.
   **
   ** @return                    the height of the wave property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double waveHeight() {
    return this.wave.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wave
  /**
   ** Sets the size of the wobble wave of challenge text to generate.
   **
   ** @param  value              the size of the wobble wave of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble color(final Size value) {
    this.wave = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wave
  /**
   ** Return the size of the wobble wave of challenge text to generate.
   **
   ** @return                    the size of the wobble wave of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   */
  public Size wave() {
    return this.wave;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Set the length of the amplitude property.
   **
   ** @param  value              the length of the amplitude property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble amplitudeLength(final double value) {
    this.amplitude.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Returns the length of the amplitude property.
   **
   ** @return                    the length of the amplitude property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double amplitudeLength() {
    return this.amplitude.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeHeight
  /**
   ** Set the amplitude height property.
   **
   ** @param  value              the amplitude height property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble amplitudeHeight(final double value) {
    this.amplitude.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeHeight
  /**
   ** Returns the amplitude height property.
   **
   ** @return                    the amplitude height property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double amplitudeHeight() {
    return this.amplitude.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitude
  /**
   ** Sets the size of the wobble amplitude of challenge text to generate.
   **
   ** @param  value              the size of the wobble amplitude of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble amplitude(final Size value) {
    this.amplitude = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitude
  /**
   ** Return the size of the wobble amplitude of challenge text to generate.
   **
   ** @return                    the size of the wobble amplitude of challenge
   **                            text to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   */
  public Size amplitude() {
    return this.amplitude;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleLength
  /**
   ** Set the length of the scale property.
   **
   ** @param  value              the length of the scale property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble scaleLength(final double value) {
    this.scale.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleLength
  /**
   ** Returns the length of the scale property.
   **
   ** @return                    the length of the scale property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scaleLength() {
    return this.scale.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleHeight
  /**
   ** Set the height of the scale property.
   **
   ** @param  value              the height of the scale property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble scaleHeight(final double value) {
    this.scale.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleHeight
  /**
   ** Returns the height of the scale property.
   **
   ** @return                    the height of the scale property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scaleHeight() {
    return this.scale.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Sets the size of the wobble scale of challenge text to generate.
   **
   ** @param  value              the size of the wobble scale of challenge text
   **                            to generate.
   **                            <br>
   **                            Allowed object is {@link Size}.
   **
   ** @return                    the <code>Wobble</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Wobble</code>.
   */
  public Wobble scale(final Size value) {
    this.scale = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Return the size of the wobble scale of challenge text to generate.
   **
   ** @return                    the size of the wobble scale of challenge text
   **                            to generate.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Size}.
   */
  public Size scale() {
    return this.scale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
    int result = this.wave != null ? this.wave.hashCode() : 0;
    result = 31 * result + (this.scale     != null ? this.scale.hashCode()     : 0);
    result = 31 * result + (this.amplitude != null ? this.amplitude.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Wobble</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Wobble</code>s may be different even though they contain the same
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

    final Wobble that = (Wobble)other;
    if (this.wave != null ? !this.wave.equals(that.wave) : that.wave != null)
      return false;

    if (this.amplitude != null ? !this.amplitude.equals(that.amplitude) : that.amplitude != null)
      return false;

    return !(this.scale != null ? !this.scale.equals(that.scale) : that.scale != null);
  }
}