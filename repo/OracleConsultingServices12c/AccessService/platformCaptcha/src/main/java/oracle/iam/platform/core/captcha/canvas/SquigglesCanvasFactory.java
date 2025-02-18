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

    File        :   SquigglesCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SquigglesCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.canvas;

import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.AlphaComposite;

import java.awt.geom.Arc2D;

import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////////////////////
// class SquigglesCanvasFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>SquigglesCanvasFactory</code> fills an image with black and white
 ** squiggly background.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SquigglesCanvasFactory implements CanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SquigglesCanvasFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private SquigglesCanvasFactory() {
    // ensure inheritance
    super();
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
    final BasicStroke    s = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, new float[] { 2.0f, 2.0f }, 0.0f);
    final AlphaComposite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);

    int    width  = image.getWidth();
    int    height = image.getWidth();
    double ts     = 0.0;
    double dt     = 5.0;
    final Graphics2D g = image.createGraphics();
    g.setComposite(c);
    g.setStroke(s);
    g.translate(width * -1.0, 0.0);
    for (double xt = 0.0; xt < (2.0 * width); xt += dt, ts += dt) {
      final Arc2D arc = new Arc2D.Double(0, 0, width, height, 0.0, 360.0, Arc2D.OPEN);
      g.draw(arc);
      g.translate(dt, 0.0);
    }
    g.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SquigglesCanvasFactory</code> that
   ** fills the background of an image with {@code Color#WHITE}.
   **
   ** @return                    the created
   **                            <code>SquigglesCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SquigglesCanvasFactory</code>.
   */
  public static SquigglesCanvasFactory build() {
    return new SquigglesCanvasFactory();
  }
}