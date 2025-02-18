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

    File        :   PixelColorCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PixelColorCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.canvas;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;

import oracle.iam.platform.core.captcha.Digester;

import oracle.iam.platform.core.captcha.color.ColorFactory;

////////////////////////////////////////////////////////////////////////////////
// class PixelColorCanvasFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>SingleColorFactory</code> fills the background with some noise and
 ** interference lines.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PixelColorCanvasFactory extends SingleColorCanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PixelColorCanvasFactory</code> that use the provided
   ** color factory to fill the background.
   **
   ** @param  color              the {@link ColorFactory} providing the
   **                            background color to fill.
   **                            <br>
   **                            Allowed object is {@link Color}.
   */
  private PixelColorCanvasFactory(final ColorFactory color) {
    // ensure inheritance
    super(color);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render (CanvasFactory)
  /**
   ** Fills a given {@link BufferedImage} with an randomly choosen background.
   **
   ** @param  image              the image to manipulate.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   */
  @Override
  public final void render(final BufferedImage image) {
    // ensure inheritance
    super.render(image);

    // the width and height of the captcha image
    final int w = image.getWidth();
    final int h = image.getHeight();

    // draw 100 noises (random colors and positions)
    final Graphics gc = image.getGraphics();
    for (int i = 0; i < 100; i++) {
      // random color
      int r = Digester.instance.nextInt(255);
      int g = Digester.instance.nextInt(255);
      int b = Digester.instance.nextInt(255);
      gc.setColor(new Color(r, g, b));

      // random position
      int x1 = Digester.instance.nextInt(w - 3);
      int y1 = Digester.instance.nextInt(h - 2);
      // random rotation angle
      int s = Digester.instance.nextInt(360);
      int e = Digester.instance.nextInt(360);
      // random size
      int wInt = Digester.instance.nextInt(6);
      int hInt = Digester.instance.nextInt(6);
      gc.fillArc(x1, y1, wInt, hInt, s, e);
      // Draw 5 interference lines
      if (i % 20 == 0) {
        int x2 = Digester.instance.nextInt(w);
        int y2 = Digester.instance.nextInt(h);
        gc.drawLine(x1, y1, x2, y2);
      }
    }
    gc.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>PixelColorCanvasFactory</code> that
   ** fills the background with some noise and interference lines.
   **
   ** @param  color              the {@link ColorFactory} providing the
   **                            background color to fill.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the created
   **                            <code>PixelColorCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PixelColorCanvasFactory</code>.
   */
  public static PixelColorCanvasFactory build(final ColorFactory color) {
    return new PixelColorCanvasFactory(color);
  }
}