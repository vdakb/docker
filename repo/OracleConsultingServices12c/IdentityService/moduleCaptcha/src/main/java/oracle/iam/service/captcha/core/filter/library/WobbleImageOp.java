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

    File        :   WobbleImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    WobbleImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter.library;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.platform.captcha.core.type.Size;

////////////////////////////////////////////////////////////////////////////////
// class WobbleImageOp
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>WobbleImageOp</code> describes and implements
 ** single-input/single-output wobble operations performed on
 ** {@code BufferedImage} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class WobbleImageOp extends AbstractTransformImageOp<WobbleImageOp> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Size wave      = new Size(15, 15);
  protected Size scale     = new Size(1.0, 1.0);
  protected Size amplitude = new Size(4.0, 3.0);

  protected Size random    = new Size(3 * Digester.instance.nextDouble(), 10 * Digester.instance.nextDouble());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>WobbleImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private WobbleImageOp() {
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
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp waveLength(final double value) {
    this.wave.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveLength
  /**
   ** Returns the length wave property.
   **
   ** @return                    the length wave property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double waveLength() {
    return this.wave.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Set the wave height property.
   **
   ** @param  value              the wave height property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp waveHeight(final double value) {
    this.wave.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   waveHeight
  /**
   ** Returns the wave height property.
   **
   ** @return                    the wave height property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double waveHeight() {
    return this.wave.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Set the amplitude length property.
   **
   ** @param  value              the amplitude length property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp amplitudeLength(final double value) {
    this.amplitude.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amplitudeLength
  /**
   ** Returns the amplitude length property.
   **
   ** @return                    the amplitude length property.
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
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp amplitudeHeight(final double value) {
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
  // Method:   scaleLength
  /**
   ** Set the scale length property.
   **
   ** @param  value              the scale length property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp scaleLength(final double value) {
    this.scale.width(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleLength
  /**
   ** Returns the scale length property.
   **
   ** @return                    the scale length property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scaleLength() {
    return this.scale.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleHeight
  /**
   ** Set the scale height property.
   **
   ** @param  value              the scale height property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>WobbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public WobbleImageOp scaleHeight(final double value) {
    this.scale.height(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scaleHeight
  /**
   ** Returns the scale height property.
   **
   ** @return                    the scale height property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scaleHeight() {
    return this.scale.height();
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
  protected final void transform(final int x, final int y, final double[] t) {
    final double tx = Math.cos((double) (this.scale.width()  * x + y) / this.wave.width()  + this.random.width());
    final double ty = Math.sin((double) (this.scale.height() * y + x) / this.wave.height() + this.random.height());
    t[0] = x + this.amplitude.width() * tx;
    t[1] = y + this.amplitude.height() * ty;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>WobbleImageOp</code>.
   **
   ** @return                    the created <code>WobbleImageOp</code>.
   **                            <br>
   **                            Possible object is <code>WobbleImageOp</code>.
   */
  public static WobbleImageOp build() {
    return new WobbleImageOp();
  }
}