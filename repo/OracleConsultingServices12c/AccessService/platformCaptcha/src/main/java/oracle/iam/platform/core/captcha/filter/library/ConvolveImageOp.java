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

    File        :   ConvolveImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ConvolveImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter.library;

////////////////////////////////////////////////////////////////////////////////
// abstract class ConvolveImageOp
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>ConvolveImageOp</code> describes and implements single input/output
 ** convolve operation onto a {@code BufferedImage} objects.
 **
 ** @param  <T>                  the implementation type of this
 **                              {@link AbstractImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class ConvolveImageOp<T extends ConvolveImageOp> extends AbstractImageOp<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private float[][] matrix;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Blur
  // ~~~~~ ~~~~
  /**
   ** <code>ConvolveImageOp</code> describes and implements single input/output
   ** blur operations performed onto a {@code BufferedImage} objects.
   */
  static class Blur extends ConvolveImageOp<Blur> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final float[][] matrix = {
      { 1 / 16f, 2 / 16f, 1 / 16f }
    , { 2 / 16f, 4 / 16f, 2 / 16f }
    , { 1 / 16f, 2 / 16f, 1 / 16f }
    };

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Blur</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Blur() {
      // ensure inheritance
      super(matrix);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Soften
  // ~~~~~ ~~~~~~
  /**
   ** <code>ConvolveImageOp</code> describes and implements single input/output
   ** soften operations onto a {@code BufferedImage} objects.
   */
  static class Soften extends ConvolveImageOp<Soften> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final float[][] matrix = {
      { 0 / 16f,  1 / 16f, 0 / 16f }
    , { 1 / 16f, 12 / 16f, 1 / 16f }
    , { 0 / 16f,  1 / 16f, 0 / 16f }
    };

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Soften</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Soften() {
      // ensure inheritance
      super(matrix);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ConvolveImageOp</code> that allows use as a JavaBean.
   **
   ** @param  marix              the filter effect to apply.         
   **                            <br>
   **                            Allowed object is array of <code>byte</code>.
   */
  protected ConvolveImageOp(final float[][] matrix) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.matrix = matrix;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (AbstractImageOp)
  @Override
  protected final void filter(final int[] inp, final int[] out, final int w, final int h) {
    final int matrixWidth  = this.matrix[0].length;
    final int matrixHeight = this.matrix.length;
    final int mattrixLeft  = - matrixWidth / 2;
    final int matrixTop    = - matrixHeight / 2;
    for (int y = 0; y < h; y++) {
      final int ytop    = y + matrixTop;
      final int ybottom = y + matrixTop + matrixHeight;
      for (int x = 0; x < w; x++) {
        final float[] sum = {0.5f, 0.5f, 0.5f, 0.5f};
        final int xleft  = x + mattrixLeft;
        final int xright = x + mattrixLeft + matrixWidth;
        int matrixY = 0;
        for (int my = ytop; my < ybottom; my ++, matrixY++) {
          int matrixX = 0;
          for (int mx = xleft; mx < xright; mx ++, matrixX++) {
            final int   p = pixel(inp, mx, my, w, h, EDGE_ZERO);
            final float m = this.matrix[matrixY][matrixX];
            sum[0] += m * ((p >> 24) & 0xff);
            sum[1] += m * ((p >> 16) & 0xff);
            sum[2] += m * ((p >> 8)  & 0xff);
            sum[3] += m * (p         & 0xff);
          }
        }
        out[x + y * w] = (limit((int)sum[0]) << 24) | (limit((int)sum[1]) << 16) | (limit((int)sum[2]) << 8) | (limit((int)sum[3]));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blur
  /**
   ** Factory method to create an {@link ConvolveImageOp}.
   **
   ** @return                    the created {@link Blur} convolve operation.
   **                            <br>
   **                            Possible object is
   **                            <code>ConvolveImageOp</code>.
   */
  public static ConvolveImageOp blur() {
    return new Blur();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   soften
  /**
   ** Factory method to create an {@link ConvolveImageOp}.
   **
   ** @return                    the created {@link Soften} convolve operation.
   **                            <br>
   **                            Possible object is
   **                            <code>ConvolveImageOp</code>.
   */
  public static ConvolveImageOp soften() {
    return new Soften();
  }
}