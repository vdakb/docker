package oracle.iam.service.captcha.core.filter.library;

import java.awt.image.BufferedImageOp;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractTransformImageOp
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractConvolveImageOp</code> describes and implements
 ** single-input/single-output transformation operation performed on
 ** {@code BufferedImage} objects.
 **
 ** @param  <T>                  the implementation type of this
 **                              {@link AbstractImageOp}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractTransformImageOp<T extends AbstractTransformImageOp> extends AbstractImageOp<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean initialized;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractTransformImageOp</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractTransformImageOp() {
    // ensure inheritance
    super();

    // initialize instance
    edge(EDGE_CLAMP);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter (AbstractImageOp)
  @Override
  protected void filter(final int[] inp, final int[] out, final int w, final int h) {
    if (!this.initialized) {
      initialize();
      initialized = true;
    }
    final double[] t = new double[2];
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        transform(x, y, t);
        int pixel = bilinearPixel(inp, t[0], t[1], w, h, edge());
        out[x + y * w] = pixel;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Callback method to initialize subclass instances.
   */
  protected void initialize() {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
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
  protected abstract void transform(final int x, final int y, final double[] t);
}