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

    File        :   AbstractImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter.library;

import oracle.iam.platform.captcha.core.Digester;
import oracle.iam.platform.captcha.visual.type.Diffusor;

////////////////////////////////////////////////////////////////////////////////
// class DiffuseImageOp
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** <code>DiffuseImageOp</code> describes and implements
 ** single-input/single-output diffuse operations performed on
 ** {@code BufferedImage} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DiffuseImageOp extends AbstractTransformImageOp<DiffuseImageOp> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int SIZE   = 256;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Diffusor         config = new Diffusor();

  private final double[]   tx     = new double[SIZE];
  private final double[]   ty     = new double[SIZE];

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DiffuseImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private DiffuseImageOp() {
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
   ** @return                    the <code>DiffuseImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is <code>DiffuseImageOp</code>.
   */
  public DiffuseImageOp amount(final double value) {
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
    final int angle = Digester.instance.nextInt(255);
    t[0] = x + this.tx[angle];
    t[1] = y + this.ty[angle];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DiffuseImageOp</code>.
   **
   ** @return                    the created <code>DiffuseImageOp</code>.
   **                            <br>
   **                            Possible object is <code>DiffuseImageOp</code>.
   */
  public static DiffuseImageOp build() {
    return new DiffuseImageOp();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** Callback method to initialize the instance.
   */
  @Override
  protected void initialize() {
    final double amount = this.config.amount();
    for (int i = 0; i < SIZE; i++) {
      final double angle = 2 * Math.PI * i / SIZE;
      this.tx[i] = amount * Math.sin(angle);
      this.ty[i] = amount * Math.cos(angle);
    }
  }
}