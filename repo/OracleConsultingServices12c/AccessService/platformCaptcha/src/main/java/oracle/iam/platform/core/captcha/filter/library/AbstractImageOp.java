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

    File        :   AbstractImageOp.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractImageOp.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.filter.library;

import java.awt.RenderingHints;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.IndexColorModel;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractImageOp
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** <code>AbstractImageOp</code> describes and implements
 ** single-input/single-output operations performed on {@link BufferedImage}
 ** objects.
 **
 ** @param  <T>                  the implementation type of this
 **                              {@link BufferedImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractImageOp<T> implements BufferedImageOp {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int EDGE_ZERO   = 0;
  public static final int EDGE_MIRROR = 1;
  public static final int EDGE_CLAMP  = 2;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected int            edge = EDGE_CLAMP;
  protected RenderingHints hint;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractImageOp</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractImageOp() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   edge
  /**
   ** Set the edge mode.
   **
   ** @param  value              the edge mode.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractImageOp</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractImageOp</code> of type
   **                            <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T edge(final int value) {
    this.edge = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   edge
  /**
   ** Returns the edge mode.
   **
   ** @return                    the edge mode.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int edge() {
    return this.edge;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRenderingHints (BufferedImageOp)
  /**
   ** Returns the rendering hints for this operation.
   **
   ** @return                    the {@link RenderingHints} object for this
   *                             {@link BufferedImageOp}.
   *                             <br>
   *                             Returns <code>null</code> if no hints have been
   *                             set.
   */
  @Override
  public RenderingHints getRenderingHints() {
    return this.hint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (BufferedImageOp)
  /**
   ** Performs a single input/output operation onto a {@link BufferedImage}.
   ** <br>
   ** If the color models for the two images do not match, a color conversion
   ** into the destination color model is performed.
   ** <br>
   ** If the destination image is <code>null</code>, a {@link BufferedImage}
   ** with an appropriate <code>ColorModel</code> is created.
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
    if (target == null)
      target = createCompatibleDestImage(source, null);

    final int   w = source.getWidth();
    final int   h = source.getHeight();
    final int[] i = new int[w * h];
    final int[] o = new int[w * h];
    source.getRaster().getDataElements(0, 0, w, h, i);
    filter(i, o, w, h);
    target.getRaster().setDataElements(0, 0, w, h, o);
    return target;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createCompatibleDestImage (BufferedImageOp)
  /**
   ** Creates a zeroed destination image with the correct size and number of
   ** bands.
   ** <br>
   ** An {@link IllegalArgumentException} may be thrown if the source image is
   ** incompatible with the types of images allowed by the class implementing
   ** this filter.
   **
   ** @param  image              the {@link BufferedImage} to be filtered.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   ** @param  destination        the {@link ColorModel} of the destination.
   **                            If <code>null</code>, the {@link ColorModel} of
   **                            the source is used.
   **                            <br>
   **                            Allowed object is {@link ColorModel}.
   **
   ** @return                    the zeroed destination image.
   **                            <br>
   **                            Possible object is {@link BufferedImage}.
   */
  @Override
  public BufferedImage createCompatibleDestImage(final BufferedImage image, ColorModel destination) {
    if (destination == null) {
      destination = image.getColorModel();
      if (destination instanceof IndexColorModel) {
        destination = ColorModel.getRGBdefault();
      }
    }
    return new BufferedImage(destination, destination.createCompatibleWritableRaster(image.getWidth(), image.getHeight()), destination.isAlphaPremultiplied(), null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPoint2D (BufferedImageOp)
  /**
   ** Returns the location of the corresponding destination point given a point
   ** in the source image.  If <code>target</code> is specified, it is used to
   ** hold the return value.
   **
   ** @param  source             the {@link Point2D} that represents the point
   **                            in the source image
   **                            <br>
   **                            Allowed object is {@link Point2D}.
   ** @param  result             the {@link Point2D} in which to store the
   **                            result.
   **                            <br>
   **                            Allowed object is {@link Point2D}.
   **
   ** @return                    the {@link Point2D} in the destination image
   **                            that corresponds to the specified point in the
   **                            source image.
   **                            <br>
   **                            Possible object is {@link Point2D}.
   */
  @Override
  public Point2D getPoint2D(final Point2D source, Point2D result) {
    if (result == null) {
      result = new Point2D.Float();
    }
    result.setLocation(source.getX(), source.getY());
    return result;  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getBounds2D (BufferedImageOp)
  /**
   ** Returns the bounding box of the filtered destination image.
   ** <br>
   ** An {@link IllegalArgumentException} may be thrown if the source image is
   ** incompatible with the types of images allowed by the class implementing
   ** this filter.
   **
   ** @param  image              the {@link BufferedImage} to be filtered.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   **
   ** @return                    the {@link Rectangle2D} representing the
   **                            destination image's bounding box.
   **                            <br>
   **                            Possible object is {@link Rectangle2D}.
   */
  public Rectangle2D getBounds2D (final BufferedImage image) {
    return image.getRaster().getBounds();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  protected abstract void filter(final int[] inp, final int[] out, final int w, final int h);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   limit
  /**
   ** Apply the constraint that byte cannot exceed limits.
   **
   ** @param  value              the value to apply the constraint on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the given value ensuring that its not exeeding
   **                            the limits.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  protected int limit(final int value) {
    return limit(value, 0, 255);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   limit
  /**
   ** Apply the constraint that a value is in range of the given limits.
   **
   ** @param  value              the value to apply the constraint on.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  min                The minimum value that <code>value</code> must
   **                            meet.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  max                The maximum value that <code>value</code> must
   **                            meet.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the given value ensuring that its not violating
   **                            the limits.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  protected int limit(final int value, final int min, final int max) {
    return (value < min) ? min : (value > max) ? max : value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bilinearPixel
  protected int bilinearPixel(final int pixels[], final double x, final double y, final int width, final int height, final int edge) {
    final int    xi = (int) Math.floor(x);
    final int    yi = (int) Math.floor(y);
    final double xd = x - xi;
    final double yd = y - yi;
    final int    nw = pixel(pixels, xi,     yi,     width, height, edge);
    final int    ne = pixel(pixels, xi + 1, yi,     width, height, edge);
    final int    sw = pixel(pixels, xi,     yi + 1, width, height, edge);
    final int    se = pixel(pixels, xi + 1, yi + 1, width, height, edge);
    return bilinear(nw, ne, sw, se, xd, yd);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pixel
  protected int pixel(final int pixels[], int x, int y, final int width, int height, final int edge) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      return pixels[x + y * width];
    }
    else if (edge == EDGE_ZERO) {
      return 0;
    }
    else if (edge == EDGE_CLAMP) {
      if (x < 0) {
        x = 0;
      }
      else if (x >= width) {
        x = width - 1;
      }
      if (y < 0) {
        y = 0;
      }
      else if (y >= height) {
        y = height - 1;
      }
      return pixels[x + y * width];
    }
    else {
      if (x < 0) {
        x = -x;
      }
      else if (x >= width) {
        x = width + width - x - 1;
      }
      if (y < 0) {
        y = -y;
      }
      else if (y > height) {
        y = height = height - y - 1;
      }
      try {
        return pixels[x + y * height];
      }
      catch (Exception e) {
        return 0;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bilinear
  private int bilinear(final int nw, final int ne, final int sw, final int se, final double xd, final double yd) {
    return linear(linear(nw, ne, xd), linear(sw, se, xd), yd);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linear
  private int linear(final int from, final int to, final double d) {
    int c = 0;
    for (int i = 0; i < 4; i++) {
      c += linear(from, to, i * 8, d);
    }
    return c;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   linear
  private int linear(final int from, final int to, final int shift, final double d) {
    return ((int)Math.floor(((from >> shift) & 0xff) + d * (((to >> shift) & 0xff) - ((from >> shift) & 0xff)))) << shift;
  }
}