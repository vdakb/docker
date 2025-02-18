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

    File        :   GradientCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GradientCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.canvas;

import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import oracle.iam.platform.core.captcha.color.GradientColorFactory;

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