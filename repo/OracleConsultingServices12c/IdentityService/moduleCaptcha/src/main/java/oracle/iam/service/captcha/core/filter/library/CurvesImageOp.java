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

    File        :   CurvesImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CurvesImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.filter.library;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.platform.captcha.core.math.Spline;

import oracle.iam.service.captcha.core.color.ColorFactory;
import oracle.iam.service.captcha.core.color.SingleColorFactory;

////////////////////////////////////////////////////////////////////////////////
// class CurvesImageOp
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>CurvesImageOp</code> describes and implements
 ** single-input/single-output {@code hhhhhhhhh} operations performed on
 ** {@link BufferedImage}
 ** objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CurvesImageOp extends AbstractImageOp<CurvesImageOp> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private ColorFactory color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CurvesImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private CurvesImageOp(final ColorFactory color) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.color = color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Set the {@link ColorFactory} property.
   **
   ** @param  value              the lower limit for the colors from which one
   **                            will be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   **
   ** @return                    the <code>CurvesImageOp</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is <code>CurvesImageOp</code>.
   */
  public CurvesImageOp color(final ColorFactory value) {
    this.color = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (AbstractImageOp)
  @Override
  protected final void filter(final int[] inp, final int[] out, final int w, final int h) {
    // intentionall left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>CurvesImageOp</code>.
   **
   ** @return                    the created <code>CurvesImageOp</code>.
   **                            <br>
   **                            Possible object is <code>CurvesImageOp</code>.
   */
  public static CurvesImageOp build() {
    return new CurvesImageOp(SingleColorFactory.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (overidden)
  /**
   ** Performs a single-input/single-output operation on a
   ** {@link BufferedImage}.
   ** <br>
   ** If the color models for the two images do not match, a color conversion
   ** into the destination color model is performed.
   ** <br>
   ** If the destination image is <code>null</code>, a {@link BufferedImage}
   ** with an appropriate <code>ColorModel</code>
   ** is created.
   ** <p>
   ** An {@link IllegalArgumentException} may be thrown if the source and/or
   ** destination image is incompatible with the types of images allowed by the
   ** class implementing this filter.
   **
   ** @param  source             the {@link BufferedImage} to be filtered.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   ** @param  target             the {@link BufferedImage} in which to store the
   **                            results.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   **
   ** @return                    the filtered {@link BufferedImage}.
   **                            <br>
   **                            Possible object is {@link BufferedImage}.
   **
   ** @throws IllegalArgumentException if the source and/or destination image is
   **                                  not compatible with the types of images
   **                                  allowed by the class implementing this
   **                                  filter.
   */
  @Override
  public BufferedImage filter(final BufferedImage source, BufferedImage target) {
    if (target == null) {
      target = createCompatibleDestImage(source, null);
    }
    double     width  = target.getWidth();
    double     height = target.getHeight();
    Graphics2D g = (Graphics2D) source.getGraphics();
    g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    final int    cp = 4 + Digester.instance.nextInt(3);
    final int[]  xp = new int[cp];
    final int[]  yp = new int[cp];
    width -= 10;
    for (int i = 0; i < cp; i++) {
      xp[i] = (int)((int)5 + (i * width) / (cp - 1));
      yp[i] = (int)(height * (Digester.instance.nextDouble() * 0.5 + 0.2));
    }
    final int section = 6;
    int[] xs = new int[(cp - 1) * section];
    int[] ys = new int[(cp - 1) * section];
    for (int i = 0; i < cp - 1; i++) {
      double x0 = i > 0 ? xp[i - 1] : 2 * xp[i] - xp[i + 1];
      double x1 = xp[i];
      double x2 = xp[i + 1];
      double x3 = (i + 2 < cp) ? xp[i + 2] : 2 * xp[i + 1] - xp[i];
      double y0 = i > 0 ? yp[i - 1] : 2 * yp[i] - yp[i + 1];
      double y1 = yp[i];
      double y2 = yp[i + 1];
      double y3 = (i + 2 < cp) ? yp[i + 2] : 2 * yp[i + 1] - yp[i];
      for (int j = 0; j < section; j++) {
        xs[i * section + j] = (int)Spline.catmullRom(x0, x1, x2, x3, 1.0 / section * j);
        ys[i * section + j] = (int)Spline.catmullRom(y0, y1, y2, y3, 1.0 / section * j);
      }
    }
    for (int i = 0; i < xs.length - 1; i++) {
      g.setColor(this.color.get(i));
      g.setStroke(new BasicStroke(2 + 2 * Digester.instance.nextFloat()));
      g.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
    }
    return source;
  }
}