package oracle.iam.service.captcha.core.canvas;

import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.color.GradientColorFactory;

////////////////////////////////////////////////////////////////////////////////
// class GradientCanvasFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>GradientCanvasFactory</code> creates a {@code BufferedImage} filled
 ** with a gradiated background.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GradientCanvasFactory implements CanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final GradientColorFactory color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GradientCanvasFactory</code> that fills the
   ** background with a gradiated background.
   */
  private GradientCanvasFactory(final GradientColorFactory color) {
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
    final int            w = image.getWidth();
    final int            h = image.getHeight();
    final Graphics2D     g = image.createGraphics();
    g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
    // create the gradient color
    g.setPaint(new GradientPaint(0, 0, this.color.get(0), w, h, this.color.get(1)));
    // draw gradient color
    g.fill(new Rectangle2D.Double(0, 0, w, h));
    g.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>GradientCanvasFactory</code> that use
   ** the provided color factory to fill the background.
   **
   ** @return                    the created
   **                            <code>GradientCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>GradientCanvasFactory</code>.
   */
  public static GradientCanvasFactory build() {
    return new GradientCanvasFactory(GradientColorFactory.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>GradientCanvasFactory</code> that use
   ** the provided color factory to fill the background.
   **
   ** @param  color              the {@code Color} of the background to fill.
   **                            <br>
   **                            Allowed object is {@code Color}.
   **
   ** @return                    the created
   **                            <code>GradientCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>GradientCanvasFactory</code>.
   */
  public static GradientCanvasFactory build(final GradientColorFactory color) {
    return new GradientCanvasFactory(color);
  }
}