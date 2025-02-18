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

    Copyright ? 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Distorting.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Distorting.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.Digester;

////////////////////////////////////////////////////////////////////////////////
// interface Distorting
// ~~~~~~~~~ ~~~~~~~~~~
/**
 ** The rendere adds distortion to {@link BufferedImage} <code>CAPTCHA</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Distorting {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Shear
  // ~~~~~ ~~~~~~
  /**
   ** The image noise is random variation of brightness or color information in
   ** images, and is usually an aspect of electronic noise.
   ** <p>
   ** <p>
   ** This <code>Distorting</code> draws polygones onto the provided image to
   ** create noises to prevent OCR.
   */
  static class Shear implements Distorting {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Color color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Shear</code> image noise.
     **
     ** @param  color            the {@link Color} used for rendering.
     **                          <br>
     **                          Allowed object is {@link Color}.
     */
    private Shear(final Color color) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.color = color;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Distorting)
    /**
     ** Manipulate a {@link BufferedImage} with random straight lines.
     **
     ** @param  image              the image to manipulate.
     **                            <br>
     **                            Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final BufferedImage image) {
      final Graphics2D g = image.createGraphics();
      shearX(g, image.getWidth(), image.getHeight());
      shearY(g, image.getWidth(), image.getHeight());
      g.dispose();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: shearX
    private void shearX(Graphics2D g, int w1, int h1) {
      int period = Digester.instance.nextInt(10) + 5;

      boolean borderGap = true;
      int     frames = 15;
      int     phase = Digester.instance.nextInt(5) + 2;

      for (int i = 0; i < h1; i++) {
        double d = (period >> 1) * Math.sin((double)i / (double)period + (6.2831853071795862D * phase) / frames);
        g.copyArea(0, i, w1, 1, (int)d, 0);
        if (borderGap) {
          g.setColor(this.color);
          g.drawLine((int)d, i, 0, i);
          g.drawLine((int)d + w1, i, w1, i);
        }
      }
    }

    private void shearY(Graphics2D g, int w1, int h1) {
      int period = Digester.instance.nextInt(30) + 10; // 50;

      boolean borderGap = true;
      int     frames = 15;
      int     phase = 7;
      for (int i = 0; i < w1; i++) {
        double d = (period >> 1) * Math.sin((float)i / period + (6.2831853071795862D * phase) / frames);
        g.copyArea(i, 0, 1, h1, 0, (int)d);
        if (borderGap) {
          g.setColor(this.color);
          g.drawLine(i, (int)d, i, 0);
          g.drawLine(i, (int)d + h1, i, h1);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class FishEye
  // ~~~~~ ~~~~~~~
  /**
   ** The image noise is random variation of brightness or color information in
   ** images, and is usually an aspect of electronic noise.
   ** <p>
   ** <p>
   ** This <code>Distorting</code> draws polygones onto the provided image to
   ** create noises to prevent OCR.
   */
  static class FishEye implements Distorting {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Color color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>FishEye</code> image noise.
     **
     ** @param  color            the {@link Color} used for rendering.
     **                          <br>
     **                          Allowed object is {@link Color}.
     */
    private FishEye(final Color color) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.color = color;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Distorting)
    /**
     ** Manipulate a {@link BufferedImage} with random straight lines.
     **
     ** @param  image              the image to manipulate.
     **                            <br>
     **                            Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final BufferedImage image) {
      int height   = image.getHeight();
      int width    = image.getWidth();
      int hstripes = height / 10;
      int hspace   = height / (hstripes + 1);

      final Graphics2D g = image.createGraphics();
      int              i;
      for (i = hspace; i < height; i += hspace) {
        g.setColor(this.color);
        g.drawLine(0, i, width, i);
      }
      int[] pix = new int[height * width];
      int   j = 0;
      for (int j1 = 0; j1 < width; ++j1) {
        for (int k1 = 0; k1 < height; ++k1) {
          pix[j] = image.getRGB(j1, k1);
          ++j;
        }
      }

      double distance = randomize(width / 4, width / 3);
      int    wMid     = image.getWidth() / 2;
      int    hMid     = image.getHeight() / 2;
      for (int x = 0; x < image.getWidth(); ++x) {
        for (int y = 0; y < image.getHeight(); ++y) {
          int    relX = x - wMid;
          int    relY = y - hMid;
          double d1 = Math.sqrt(relX * relX + relY * relY);
          if (d1 < distance) {
            int j2 = wMid + (int)(calculate(d1 / distance) * distance / d1 * (double)(x - wMid));
            int k2 = hMid + (int)(calculate(d1 / distance) * distance / d1 * (double)(y - hMid));
            image.setRGB(x, y, pix[j2 * height + k2]);
          }
        }
      }
      g.dispose();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: randomize
    /**
     **
     ** @param  i                the width to calculate randomized.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param j                 the width to be randomized.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the randomized value.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    private int randomize(final int i, final int j) {
      return (int)((double)i + (double)(j - i + 1) * Digester.instance.nextDouble());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: calculate
    /**
     **
     ** @param  s                the starting value to randomize.
     **                          <br>
     **                          Allowed object is <code>double</code>.
     **
     ** @return                  the calculated random value.
     **                          <br>
     **                          Possible object is <code>double</code>.
     */
    private double calculate(final double s) {
      return (s < 0.0D) ? 0.0D : s > 1.0D ? s : -0.75D * s * s * s + 1.5D * s * s + 0.25D * s;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class StraightLine
  // ~~~~~ ~~~~~~~~~~~~
  /**
   ** The image noise is random variation of brightness or color information in
   ** images, and is usually an aspect of electronic noise.
   ** <p>
   ** This <code>Distorting</code> draws polygones onto the provided image to
   ** create noises to prevent OCR.
   */
  static class StraightLine implements Distorting {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int   size;
    private final Color color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>StraightLine</code> image noise.
     **
     ** @param  color            the {@link Color} used for rendering.
     **                          <br>
     **                          Allowed object is {@link Color}.
     ** @param  size             the line size of the drawing applied
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private StraightLine(final Color color, final int size) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.size  = size;
      this.color = color;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Distorting)
    /**
     ** Manipulate a {@link BufferedImage} with random straight lines.
     **
     ** @param  image            the image to manipulate.
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final BufferedImage image) {
      final int h  = image.getHeight();
      final int w  = image.getWidth();
      final int y1 = Digester.instance.nextInt(h) + 1;
      final int y2 = Digester.instance.nextInt(h) + 1;
      polygone(image.createGraphics(), y1, w, y2);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: polygone
    /**
     ** This method draws the polygon defined by <code>4</code> line segments,
     ** where the first <code>nPoint&nbsp;-&nbsp;1</code> line segments are line
     ** segments from
     ** <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code>
     ** to <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
     ** 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>.
     ** <br>
     ** The figure is automatically closed by drawing a line connecting the
     ** final point to the first point, if those points are different.
     ** <p>
     ** The area inside the polygon is defined using <code>color</code>.
     **
     ** @param  g                the {@link Graphics} canvas.
     **                          <br>
     **                          Allowed object is {@link Graphics}.
     ** @param y1                the ...
     **                          <br>
     **                          Allowed object is <code>int</code>..
     ** @param x2                the ...
     **                          <br>
     **                          Allowed object is <code>int</code>..
     ** @param y2                the ...
     **                          <br>
     **                          Allowed object is <code>int</code>..
     */
    private void polygone(final Graphics2D g, final int y1, final int x2, final int y2) {
      // the line is in fact a filled polygon
      int dX = x2;
      int dY = y2 - y1;
      // line length
      double length = Math.sqrt(dX * dX + dY * dY);
      double scale  = this.size / (2 * length);

      // The x and y increments from an endpoint needed to create a rectangle
      double ddx  = -scale * dY;
      double ddy  =  scale * dX;
      ddx        += (ddx > 0) ? 0.5 : -0.5;
      ddy        += (ddy > 0) ? 0.5 : -0.5;
      int dx      = (int)ddx;
      int dy      = (int)ddy;

      // now we can compute the corner points
      int[] x = new int[4];
      int[] y = new int[4];

      x[0] = dx;
      y[0] = y1 + dy;
      x[1] = dx;
      y[1] = y1 - dy;
      x[2] = x2 - dx;
      y[2] = y2 - dy;
      x[3] = x2 + dx;
      y[3] = y2 + dy;

      g.setColor(this.color);
      g.fillPolygon(x, y, 4);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Ripple
  // ~~~~~ ~~~~~~
  /**
   ** The image noise is random variation of brightness or color information in
   ** images, and is usually an aspect of electronic noise.
   ** <p>
   ** <code>Ripple</code> to
   ** <p>
   ** This <code>Distorting</code> generates ripple (wave) effect onto an image
   ** to prevent OCR.
   ** <br>
   ** Uses a transformed sinus wave for this.
   ** <p>
   ** This class is thread safe.
   */
  static class Ripple implements Distorting {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Axis vertical;
    private final Axis horizontal;

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Axis
    // ~~~~~ ~~~~
    /**
     ** Class to respresent wave tranforming information for an axis.
     */
    static class Axis {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private final double start;
      private final double length;
      private final double amplitude;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructs a <code>Axis</code> image noise.
       **
       ** @param  start          the starting x offset to generate wave values.
       **                        <br>
       **                        Should be between 0 and 2 * {@link Math#PI}.
       **                        <br>
       **                        Allowed object is <code>double</code>.
       ** @param  length         the length of x to be used to generate wave
       **                        values.
       **                        <br>
       **                        Should be between 0 and 4 * {@link Math#PI}.
       **                        <br>
       **                        Allowed object is <code>double</code>.
       ** @param  amplitude      the maximum y value, if it is too big, some
       **                        important parts of the image (like the text)
       **                        can "wave" out on the top or on the bottom of
       **                        the image.
       **                        <br>
       **                        Allowed object is <code>double</code>.
       */
      public Axis(final double start, final double length, final double amplitude) {
        // ensure inheritance
        super();

        // initialize instance attributes
        this.start     = start;
        this.length    = length;
        this.amplitude = amplitude;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: normalize
      /**
       ** Normalizes parameter to fall into [0, multi * {@link Math#PI}].
       **
       ** @param  value          the vlaue to normalize.
       **                        <br>
       **                        Allowed object is <code>double</code>.
       ** @param  multi          the multiplicator used for end value.
       **                        <br>
       **                        Allowed object is <code>int</code>.
       ** @return                the normalized value.
       **                        <br>
       **                        Possible object is <code>double</code>.
       */
      protected double normalize(final double value, int multi) {
        final double pi = multi * Math.PI;
        double a = Math.abs(value);
        final double d = Math.floor(a / pi);
        return a - d * pi;
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Ripple</code> image noise.
     **
     ** @param  vertical         calculate waving deltas from x axis (so to
     **                          modify y values)
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Axis}.
     ** @param  horizontal       calculate waving deltas from y axis (so to
     **                          modify x values).
     **                          <br>
     **                          Must not be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link Axis}.
     */
    public Ripple(final Axis vertical, final Axis horizontal) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.vertical   = vertical;
      this.horizontal = horizontal;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Distorting)
    /**
     ** Draw a {@link BufferedImage} a rippled (waved) variant of source into
     ** destination.
     **
     ** @param  image            the image to manipulate.
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final BufferedImage image) {
      final int   width      = image.getWidth();
      final int   height     = image.getHeight();
      final int[] vertical   = calcDeltaArray(this.vertical, width);
      final int[] horizontal = calcDeltaArray(this.horizontal, height);
      for (int x = 0; x < width; x++)
        for (int y = 0; y < height; y++) {
          final int ny = (y + vertical[x]    + height) % height;
          final int nx = (x + horizontal[ny] + width)  % width;
          image.setRGB(nx, ny, image.getRGB(x, y));
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: calcDeltaArray
    /**
     ** Calculates wave delta array.
     **
     ** @param  axis             the object to transform the wave, not null
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     ** @param  num              the number of points needed, positive.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     **
     ** @return                  the calculated num length delta array.
     **                          <br>
     **                          Possible Object is array of <code>int</code>.
     */
    protected int[] calcDeltaArray(final Axis axis, int num) {
      final int[]  delta  = new int[num];
      final double period = axis.length / num;
      for (int fi = 0; fi < num; fi++)
        delta[fi] = (int)Math.round(axis.amplitude * Math.sin(axis.start + fi * period));
      return delta;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   straightLine
  /**
   ** Factory method to creates a <code>StraightLine</code> gimper.
   **
   ** @param  color              the {@link Color} used for rendering.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  size               the line size of the drawing applied
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a <code>StraightLine</code> gimper.
   **                            <br>
   **                            Possible object is <code>StraightLine</code>.
   */
  public static StraightLine straightLine(final Color color, final int size) {
    return new StraightLine(color, size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shear
  /**
   ** Factory method to creates a <code>Shear</code> gimper.
   **
   ** @param  color              the {@link Color} used for rendering.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    a <code>Shear</code> gimper.
   **                            <br>
   **                            Possible object is <code>Shear</code>.
   */
  public static Shear shear(final Color color) {
    return new Shear(color);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fishEye
  /**
   ** Factory method to creates a <code>FishEye</code> gimper.
   **
   ** @return                    a <code>FishEye</code> gimper.
   **                            <br>
   **                            Possible object is <code>FishEye</code>.
   */
  public static FishEye fishEye() {
    return new FishEye(Color.gray);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fishEye
  /**
   ** Factory method to creates a <code>FishEye</code> gimper.
   **
   ** @param  color              the {@link Color} used for rendering.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    a <code>FishEye</code> gimper.
   **                            <br>
   **                            Possible object is <code>FishEye</code>.
   */
  public static FishEye fishEye(final Color color) {
    return new FishEye(color);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render
  /**
   ** Manipulate to a {@link BufferedImage}.
   **
   ** @param  image              the image to manipulate.
   **                            <br>
   **                            Allowed Object is {@link BufferedImage}.
   */
  void render(final BufferedImage image);
}
