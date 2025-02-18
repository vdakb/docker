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

    File        :   PerlinCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PerlinCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.canvas;

//import java.awt.Color;

import java.awt.Color;

import java.awt.image.BufferedImage;

import oracle.iam.platform.core.captcha.Digester;

import oracle.iam.platform.core.captcha.noise.Perlin;

import oracle.iam.platform.core.captcha.color.ColorFactory;
import oracle.iam.platform.core.captcha.color.RandomColorFactory;

////////////////////////////////////////////////////////////////////////////////
// class PerlinCanvasFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** <code>PerlinCanvasFactory</code> fills the background of an image with
 ** random noise (Rosetta Code).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class PerlinCanvasFactory implements CanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final ColorFactory color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PerlinCanvasFactory</code> that fills the background
   ** with some noise.
   **
   ** @param  color              the {@link ColorFactory} of the background to
   **                            fill.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   */
  public PerlinCanvasFactory(final ColorFactory color) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.color = color;
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
    final int      w = image.getWidth();
    final int      h = image.getHeight();
    final double[] c = new double[4];
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        double dx = (double)x / h;
      	double dy = (double)y / h;
    	  int frequency = 6;
    	  double noise = Perlin.noise2D((dx * frequency) + 0.01, (dy * frequency) + 0.01);//, 2.718);
        // Perlin noise returns a double in the range [-1..1], whereas we want a
        // double in the range [0..1], so:
        noise = (noise + 1) / 2;
        // colors a and b stored in component form.
        final float[] c0 = this.color.get(Digester.instance.nextInt(255)).getRGBComponents(null);
        final float[] c1 = this.color.get(Digester.instance.nextInt(255)).getRGBComponents(null);
        for (int i = 0; i < 4; i++ ) {
          c[i]  = Perlin.lerp(noise, c0[i], c1[i]);
          // we assume the default RGB model, 8 bits per band.
          c[i] *= 255;
        }
        final Color f = new Color((int)c[0], (int)c[1], (int)c[2], (int)c[3]);
        image.setRGB(x, y, f.getRGB());
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>PerlinCanvasFactory</code> that fills
   ** the background noise of {@link Color#BLACK} and {@link Color#WHITE}.
   **
   ** @return                    the created <code>PerlinCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PerlinCanvasFactory</code>.
   */
  public static PerlinCanvasFactory build() {
    return build(RandomColorFactory.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>PerlinCanvasFactory</code> that use the
   ** provided color factory to fill the background.
   **
   ** @param  color              the {@link ColorFactory} of the background to
   **                            fill.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   **
   ** @return                    the created <code>PerlinCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>PerlinCanvasFactory</code>.
   */
  public static PerlinCanvasFactory build(final ColorFactory color) {
    return new PerlinCanvasFactory(color);
  }
}