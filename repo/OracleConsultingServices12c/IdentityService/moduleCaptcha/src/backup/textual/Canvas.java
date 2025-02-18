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

    File        :   Canvas.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Canvas.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.util.List;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////////////////////
// abstract class Canvas
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** Renderer to create a background image.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Canvas {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final Dimension DEFAULT = new Dimension(30 ,10);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Transparent
  // ~~~~~ ~~~~~~~~~~~
  /**
   ** Renderer to create transparent background.
   */
  public static class Transparent extends Canvas {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a <code>Transparent</code> background that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    private Transparent() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abtract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Canvas)
    /**
     ** Factory method to create a new background canvas with the specified
     ** dimensions.
     **
     ** @param  width            the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     ** @param  height           the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     **
     ** @return                  the background image created.
     **                          <br>
     **                          Possible Object is {@link BufferedImage}.
     */
    @Override
    public BufferedImage render(final int width, final int height) {
      final BufferedImage b = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
      final Graphics2D    g = b.createGraphics();
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
      g.fillRect(0, 0, width, height);
      return b;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Gradient
  // ~~~~~ ~~~~~~~~
  /**
   ** Renderer to create gradient background.
   */
  public static class Gradient extends Canvas {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    private final Color to;
    private final Color from;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a <code>Gradient</code> background that allows use as a
     ** JavaBean.
     **
     ** @param  from             the {@link Color} anchored by the image origin.
     **                          <br>
     **                          Allowed object is {@link Color}.
     ** @param  to               the {@link Color} anchored by the image
     **                          dimension.
     **                          <br>
     **                          Allowed object is {@link Color}.
     */
    private Gradient(final Color from, final Color to) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.to   = to;
      this.from = from;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abtract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Canvas)
    /**
     ** Factory method to create a new background canvas with the specified
     ** dimensions.
     **
     ** @param  width            the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     ** @param  height           the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     **
     ** @return                  the background image created.
     **                          <br>
     **                          Possible Object is {@link BufferedImage}.
     */
    @Override
    public final BufferedImage render(final int width, final int height) {
      final RenderingHints h = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      final BufferedImage  b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      final Graphics2D     g = b.createGraphics();
      // create the gradient color
      final GradientPaint  y = new GradientPaint(0, 0, this.from, width, height, this.to);

      g.setRenderingHints(h);
      g.setPaint(y);
      // draw gradient color
      g.fill(new Rectangle2D.Double(0, 0, width, height));
      // draw the transparent image over the background
      g.drawImage(b, 0, 0, null);
      g.dispose();

      return b;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Boxes
  // ~~~~~ ~~~~~
  /**
   ** Renderer to create background with boxes.
   */
  public static class Boxes extends Canvas {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    private final int         count;
    private final List<Color> color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  Ctor
    /**
     ** Constructs a <code>Boxes</code> background that allows use as a
     ** JavaBean.
     **
     ** @param  count            the number of boxes to draw.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  color            the collection of {@link Color}s used to draw
     **                          boxes.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Color}.
     */
    private Boxes(final int count, final List<Color> color) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.count = count;
      this.color = color;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abtract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Canvas)
    /**
     ** Factory method to create a new background canvas with the specified
     ** dimensions.
     **
     ** @param  width            the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     ** @param  height           the width of the background canvas to create.
     **                          <br>
     **                          Allowed Object is <code>int</code>.
     **
     ** @return                  the background image created.
     **                          <br>
     **                          Possible Object is {@link BufferedImage}.
     */
    @Override
    public final BufferedImage render(final int width, final int height) {
      final BufferedImage b = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
      final Graphics2D    g = b.createGraphics();
      final int           c = this.color.size();
      for (int i = 0; i < this.count; i++) {
        g.setColor(this.color.get(random(c)));
        g.fillRect(random(width), random(height), random(width), random(height));
      }
      return b;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: random
    /**
     ** Returns the next pseudorandom, uniformly distributed <code>int</code>
     ** value between 0 (inclusive) and the specified <code>bound</code> value
     ** (exclusive), drawn from the random number generator's sequence.
     ** <p>
     ** The method <code>nextInt</code> is backed by class
     ** <code>SecureRandom</code>.
     **
     ** @param  bound              the upper bound (exclusive).
     **                            <br>
     **                            Must be positive.
     **                            <br>
     **                            Allowed object is <code>int</code>.
     */
    private int random(final int bound) {
      return (int)(Math.random() * bound);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transparent
  /**
   ** Factory method to creates a transparent <code>Canvas</code>.
   **
   ** @return                    a transparent <code>Canvas</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>Canvas.Transparent</code>.
   */
  public static Transparent transparent() {
    return new Transparent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gradient
  /**
   ** Factory method to creates a gradient <code>Canvas</code>.
   **
   ** @param  from               the {@link Color} anchored by the image origin.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  to                 the {@link Color} anchored by the image
   **                            dimension.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    a gradient <code>Canvas</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>Canvas.Transparent</code>.
   */
  public static Gradient gradient(final Color from, final Color to) {
    return new Gradient(from, to);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   boxes
  /**
   ** Factory method to creates a background with <code>Boxes</code>.
   **
   ** @param  count              the number of boxes to draw.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  color              the collection of {@link Color}s used to draw
   **                            boxes.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Color}.
   **
   ** @return                    a gradient <code>Canvas</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>Canvas.Transparent</code>.
   */
  public static Boxes boxes(final int count, final List<Color> color) {
    return new Boxes(count, color);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render
  /**
   ** Factory method to create a new background canvas with the specified
   ** dimensions.
   **
   ** @param  width              the width of the background canvas to create.
   **                            <br>
   **                            Allowed Object is <code>int</code>.
   ** @param  height             the width of the background canvas to create.
   **                            <br>
   **                            Allowed Object is <code>int</code>.
   **
   ** @return                    the background canvas created.
   **                            <br>
   **                            Possible Object is {@link BufferedImage}.
   */
  public abstract BufferedImage render(final int width, final int height);
}