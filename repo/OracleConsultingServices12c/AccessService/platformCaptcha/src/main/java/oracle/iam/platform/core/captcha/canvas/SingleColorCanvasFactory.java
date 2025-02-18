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

    File        :   SingleColorCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SingleColorCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.canvas;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.image.BufferedImage;

import oracle.iam.platform.core.captcha.color.ColorFactory;
import oracle.iam.platform.core.captcha.color.SingleColorFactory;

////////////////////////////////////////////////////////////////////////////////
// class SingleColorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>SingleColorCanvasFactory</code> fills the background of an image with
 ** a single color.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SingleColorCanvasFactory implements CanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final ColorFactory color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SingleColorCanvasFactory</code> that use the provided
   ** color factory to fill the background.
   **
   ** @param  color              the {@link ColorFactory} providing the
   **                            background color to fill.
   **                            <br>
   **                            Allowed object is {@link Color}.
   */
  protected SingleColorCanvasFactory(final ColorFactory color) {
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
  public void render(final BufferedImage image) {
    final Graphics g = image.getGraphics();
    g.setColor(this.color.get(0));
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    g.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SingleColorCanvasFactory</code> that
   ** fills the background of an image with {@link Color#WHITE}.
   **
   ** @return                    the created
   **                            <code>SingleColorCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SingleColorCanvasFactory</code>.
   */
  public static SingleColorCanvasFactory build() {
    return build(SingleColorFactory.build(Color.WHITE));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SingleColorCanvasFactory</code> that use
   ** the provided color factory to fill the background.
   **
   ** @param  color              the {@link Color} of the background to fill.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the created
   **                            <code>SingleColorCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>SingleColorCanvasFactory</code>.
   */
  public static SingleColorCanvasFactory build(final ColorFactory color) {
    return new SingleColorCanvasFactory(color);
  }
}