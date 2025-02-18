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

    Copyright © 2011. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extensions
    Subsystem   :   Identity and Access Management Facilities

    File        :   MaskFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    MaskFilter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.FilteredImageSource;

////////////////////////////////////////////////////////////////////////////////
// class MaskFilter
// ~~~~~ ~~~~~~~~~~
/**
 ** An image filter that will replace one color in an image with another color.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class MaskFilter extends RGBImageFilter {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static MaskFilter filter = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Color             newColor;
  private Color             oldColor;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>MaskFilter</code> instance that filters color of image
   ** to another color.
   ** <p>
   ** Please note, you can also use {@link #instance(Color, Color)} to reuse the
   ** same instance of <code>MaskFilter</code>.
   **
   ** @param  oldColor           old color in exist image that needs to be
   **                            replaced by new color.
   ** @param  newColor           new color to replace the old color.
   */
  public MaskFilter(final Color oldColor, final Color newColor) {
    this.newColor                 = newColor;
    this.oldColor                 = oldColor;
    this.canFilterIndexColorModel = true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: filterRGB (RGBImageFilter)
  /**
   ** Convert a single input pixel in the default RGB ColorModel to a single
   ** output pixel.
   **
   ** @param  x                  the x coordinate of the pixel.
   ** @param  y                  the y coordinate of the pixel.
   ** @param  rgb                the integer pixel representation in the default
   **                            RGB color model.
   **
   ** @return                    a filtered pixel in the default RGB color
   **                            model.
   */
  @Override
  public int filterRGB(final int x, final int y, final int rgb) {
    if (this.newColor != null && this.oldColor != null) {
      if (rgb == this.oldColor.getRGB())
        return this.newColor.getRGB();
    }
    return rgb;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  public static MaskFilter instance(final Color oldColor, final Color newColor) {
    if (filter == null)
      filter = new MaskFilter(oldColor, newColor);
    else {
      filter.oldColor = oldColor;
      filter.newColor = newColor;
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invert
  /**
   ** Returns an image as negative of an existing one. It will basically replace
   ** the black color with white color.
   **
   ** @param  origin             the source {@link Image} to transform.
   **
   ** @return                    an image after replacing the color.
   */
  public static Image invert(final Image origin) {
    return create(origin, Color.black, Color.white);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Returns an image from an existing one by replacing the old color with the
   ** new color.
   **
   ** @param  origin             the source {@link Image} to transform.
   ** @param  oldColor           old color in supplied <code>image</code> that
   **                            needs to be replaced by <code>newColor</code>.
   ** @param  newColor           new color to replace the <code>oldColor</code>.
   **
   ** @return                    an image after replacing the color.
   */
  public static Image create(final Image origin, final Color oldColor, final Color newColor) {
    MaskFilter    filter = MaskFilter.instance(oldColor, newColor);
    ImageProducer prod = new FilteredImageSource(origin.getSource(), filter);
    Image image = Toolkit.getDefaultToolkit().createImage(prod);
    return image;
  }
}