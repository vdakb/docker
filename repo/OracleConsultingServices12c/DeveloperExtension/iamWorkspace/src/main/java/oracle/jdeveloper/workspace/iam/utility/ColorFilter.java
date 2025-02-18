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

    File        :   ColorFilter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    ColorFilter.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.awt.Image;
import java.awt.Toolkit;

import java.awt.image.RGBImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.FilteredImageSource;

////////////////////////////////////////////////////////////////////////////////
// class ColorFilter
// ~~~~~ ~~~~~~~~~~~
/**
 ** An image filter that brighten or darken an existing image.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
 */
public class ColorFilter extends RGBImageFilter {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static ColorFilter filter;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private boolean            brighter;
  private int                percent    = 30;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ColorFilter</code> object that filters a color image to
   ** a brighter or a darker image.
   ** <p>
   ** Please note, you can also use {@link #instance(boolean,int)} to reuse
   ** the same instance of ColorFilter.
   **
   ** @param  brighter           a boolean <code>true</code> if the pixels
   **                            should be brightened
   ** @param  percent            an int in the range 0..100 that determines the
   **                            percentage of gray, where 100 is the darkest
   **                            gray, and 0 is the lightest.
   */
  public ColorFilter(final boolean brighter, final int percent) {
    this.brighter                 = brighter;
    this.percent                  = percent;
    this.canFilterIndexColorModel = true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Sets the mode of the filter.
   ** <p>
   ** If <code>brighter</code> is <code>true</code> if the pixels of the
   ** generated images will be brightened. On the opposite if
   ** <code>brighter</code> is set to <code>false</code> the pixels of the
   ** generated images will be darkened.
   **
   ** @param  brighter           the coloring mode of the filter to set
   */
  public void brighter(final boolean brighter) {
    this.brighter = brighter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   percent
  /**
   ** Sets the percentage of the filtering for an image.
   **
   ** @param  percent            an int in the range 0..100 that determines the
   **                            percentage of gray, where 100 is the darkest
   **                            gray, and 0 is the lightest.
   */
  public void percent(final int percent) {
    this.percent = percent;
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
  public int filterRGB(int x, int y, int rgb) {
    int r = (rgb >> 16) & 0xff;
    int g = (rgb >> 8) & 0xff;
    int b = rgb & 0xff;

    return rgb & 0xff000000 | convert(r) << 16 | convert(g) << 8 | convert(b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Creates and returns a <code>ColorFilter</code> object that filters a color
   ** image to a brighter or a darker image.
   ** <p>
   ** Subsequent calls will reuse the same instance of <code>ColorFilter</code>.
   **
   ** @param  brighter           a boolean <code>true</code> if the pixels
   **                            should be brightened
   ** @param  percent            an int in the range 0..100 that determines the
   **                            percentage of gray, where 100 is the darkest
   **                            gray, and 0 is the lightest.
   **
   ** @return                    a <code>ColorFilter</code> object that filters
   **                            a color image to a brighter or a darker image.
   */
  public static ColorFilter instance(final boolean brighter, final int percent) {
    if (filter == null)
      filter = new ColorFilter(brighter, percent);
    else {
      filter.brighter = brighter;
      filter.percent  = percent;
    }
    return filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Creates a brighter image
   **
   ** @param  origin             the original image
   **
   ** @return                    a brighter image
   */
  public static Image brighter(final Image origin) {
    return brighter(origin, 30);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brighter
  /**
   ** Creates a brighter image with a given percentage of brightness
   **
   ** @param  origin             the original image
   ** @param  percent            percentage of brightness
   **
   ** @return                    a brighter image
   */
  public static Image brighter(final Image origin, final int percent) {
    ColorFilter   filter   = ColorFilter.instance(true, percent);
    ImageProducer producer = new FilteredImageSource(origin.getSource(), filter);
    return Toolkit.getDefaultToolkit().createImage(producer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   darker
  /**
   ** Creates a darker image
   **
   ** @param  origin             the original image
   **
   ** @return                    a darker image.
   */
  public static Image darker(final Image origin) {
    return darker(origin, 30);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   darker
  /**
   ** Creates a darker image
   **
   ** @param  origin             the original image
   ** @param  percent            an int in the range 0..100 that determines the
   **                            percentage of gray, where 100 is the darkest
   **                            gray, and 0 is the lightest.
   **
   ** @return                    a darker image.
   */
  public static Image darker(final Image origin, final int percent) {
    ColorFilter filter = ColorFilter.instance(false, percent);
    ImageProducer prod = new FilteredImageSource(origin.getSource(), filter);
    return Toolkit.getDefaultToolkit().createImage(prod);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  private int convert(int color) {
    if (this.brighter)
      color += (255 - color) * percent / 100;
    else
      color -= (255 - color) * percent / 100;

    if (color < 0)
      color = 0;
    if (color > 255)
      color = 255;
    return color;
  }
}