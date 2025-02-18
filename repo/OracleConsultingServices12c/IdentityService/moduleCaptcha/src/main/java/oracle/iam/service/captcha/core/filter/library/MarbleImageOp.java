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

    File        :   MarbleImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MarbleImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter.library;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.platform.captcha.core.type.Size;

import oracle.iam.platform.captcha.core.noise.Perlin;

import oracle.iam.platform.captcha.visual.type.Marble;

////////////////////////////////////////////////////////////////////////////////
// class MarbleImageOp
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>MarbleImageOp</code> describes and implements
 ** single-input/single-output wobble operations performed on
 ** {@code BufferedImage} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class MarbleImageOp extends AbstractTransformImageOp<MarbleImageOp> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int SIZE       = 256;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private double[]         tx         = new double[SIZE];
  private double[]         ty         = new double[SIZE];
  private Marble           config     = new Marble();
  private Size             random     = new Size(SIZE * Digester.instance.nextDouble(), SIZE * Digester.instance.nextDouble());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MarbleImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private MarbleImageOp() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amount
  /**
   ** Set the amount property.
   **
   ** @param  value              the amount property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>MarbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>MarbleImageOp</code>.
   */
  public MarbleImageOp amount(final double value) {
    this.config.amount(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   amount
  /**
   ** Returns the amount property.
   **
   ** @return                    the amount property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double amount() {
    return this.config.amount();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   turbulence
  /**
   ** Set the turbulence property.
   **
   ** @param  value              the turbulence property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>MarbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>MarbleImageOp</code>.
   */
  public MarbleImageOp turbulence(final double value) {
    this.config.turbulence(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   turbulence
  /**
   ** Returns the turbulence property.
   **
   ** @return                    the turbulence property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double turbulence() {
    return this.config.turbulence();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Set the scale property.
   **
   ** @param  value              the scale property.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>MarbleImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>MarbleImageOp</code>.
   */
  public MarbleImageOp scale(final double value) {
    this.config.scale(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   scale
  /**
   ** Returns the scale property.
   **
   ** @return                    the scale property.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public double scale() {
    return this.config.scale();
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
    int d = limit((int)(127 * (1 + Perlin.noise2D(((double)x) / this.config.scale() + this.random.width(), ((double)y) / this.config.scale() + this.random.height()))));
    t[0] = x + this.tx[d];
    t[1] = y + this.ty[d];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>MarbleImageOp</code>.
   **
   ** @return                    the created <code>MarbleImageOp</code>.
   **                            <br>
   **                            Possible object is <code>MarbleImageOp</code>.
   */
  public static MarbleImageOp build() {
    return new MarbleImageOp();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   *+ Callback method to initialize subclass instances
   */
  @Override
  protected final void initialize() {
    for (int i = 0; i < SIZE; i++) {
      final double angle = 2 * Math.PI * i * this.config.turbulence() / SIZE;
      this.tx[i] = this.config.amount() * Math.sin(angle);
      this.ty[i] = this.config.amount() * Math.cos(angle);
    }
  }
}