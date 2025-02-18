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

    File        :   RippleImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RippleImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter.library;

import oracle.iam.config.captcha.type.Size;

import oracle.iam.platform.core.captcha.Digester;

////////////////////////////////////////////////////////////////////////////////
// class RippleImageOp
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>RippleImageOp</code> describes and implements
 ** single-input/single-output ripple operations performed on
 ** {@code BufferedImage BufferedImage} objects.
 **
 ** @param  <T>                  the implementation type of this
 **                              {@link TransformImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RippleImageOp<T extends RippleImageOp> extends TransformImageOp<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Size wave      = Size.build(20, 10);
  protected Size amplitude = Size.build(5, 5);

  protected Size random    = Size.build(5 * Digester.instance.nextInt(), 5 * Digester.instance.nextInt());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RippleImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected RippleImageOp() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveLength
  /**
   ** Set the length wave property.
   **
   ** @param  value              the length wave property.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RippleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>RippleImageOp</code>
   **                            of type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T waveLength(final int value) {
    this.wave.width(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveLength
  /**
   ** Returns the length wave property.
   **
   ** @return                    the length wave property.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int waveLength() {
    return this.wave.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Set the wave height property.
   **
   ** @param  value              the wave height property.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RippleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>RippleImageOp</code>
   **                            of type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T waveHeight(final int value) {
    this.wave.height(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Returns the wave height property.
   **
   ** @return                    the wave height property.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int waveHeight() {
    return this.wave.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Set the amplitude length property.
   **
   ** @param  value              the amplitude length property.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RippleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>RippleImageOp</code>
   **                            of type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T amplitudeLength(final int value) {
    this.amplitude.width(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Returns the amplitude length property.
   **
   ** @return                    the amplitude length property.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int amplitudeLength() {
    return this.amplitude.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeHeight
  /**
   ** Set the amplitude height property.
   **
   ** @param  value              the amplitude height property.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>RippleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>RippleImageOp</code>
   **                            of type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T amplitudeHeight(final int value) {
    this.amplitude.height(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeHeight
  /**
   ** Returns the amplitude height property.
   **
   ** @return                    the amplitude height property.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int amplitudeHeight() {
    return this.amplitude.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract basee classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (AbstractTransformImageOp)
  /**
   ** Applies the transformations.
   **
   ** @param  x                  the x coordinate of this
   **                            {@code BufferedImage} to apply the
   **                            transformation.
   **                            <br>
   **                            Allowed object is array of <code>int</code>.
   ** @param  y                  the y coordinate of this
   **                            {@code BufferedImage} to apply the
   **                            transformation.
   **                            <br>
   **                            Allowed object is array of <code>int</code>.
   ** @param  t                  the array of <code>double</code> in which to
   **                            store the result.
   **                            <br>
   **                            Allowed object is array of <code>double</code>.
   */
  @Override
  protected void transform(final int x, final int y, final double[] t) {
    final double tx = Math.sin((double)y / this.wave.height() + this.random.height());
    final double ty = Math.cos((double)x / this.wave.width()  + this.random.width());
    t[0] = x + this.amplitude.width()  * tx;
    t[1] = y + this.amplitude.height() * ty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RippleImageOp</code>.
   **
   ** @return                    the created <code>RippleImageOp</code>.
   **                            <br>
   **                            Possible object is <code>RippleImageOp</code>.
   */
  public static RippleImageOp build() {
    return new RippleImageOp();
  }
}