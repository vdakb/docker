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

    File        :   DoubleRippleImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DoubleRippleImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter.library;

////////////////////////////////////////////////////////////////////////////////
// class DoubleRippleImageOp
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>DoubleRippleImageOp</code> describes and implements
 ** single-input/single-output ripple operations performed on
 ** {@code BufferedImage} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DoubleRippleImageOp extends RippleImageOp<DoubleRippleImageOp> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DoubleRippleImageOp</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private DoubleRippleImageOp() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DoubleRippleImageOp</code>.
   **
   ** @return                    the created <code>DoubleRippleImageOp</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DoubleRippleImageOp</code>.
   */
  public static DoubleRippleImageOp build() {
    return new DoubleRippleImageOp();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform (overidden)
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
    final double tx = Math.sin((double) y / this.wave.height() + this.random.height()) + 1.3 * Math.sin((double) 0.6 * y / this.wave.height() + this.random.height());
    final double ty = Math.cos((double) x / this.wave.width()  + this.random.width())  + 1.3 * Math.cos((double) 0.6 * x / this.wave.width() + this.random.width());
    t[0] = x + this.amplitude.width() * tx;
    t[1] = y + this.amplitude.height() * ty;
  }
}