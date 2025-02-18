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

    File        :   TransparentCanvasFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TransparentCanvasFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.canvas;

import java.awt.Graphics2D;
import java.awt.AlphaComposite;

import java.awt.image.BufferedImage;

////////////////////////////////////////////////////////////////////////////////
// class TransparentCanvasFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>TransparentCanvasFactory</code> creates a {@link BufferedImage} filled
 ** with a transparent background.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TransparentCanvasFactory implements CanvasFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TransparentCanvasFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TransparentCanvasFactory() {
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
    final Graphics2D g = image.createGraphics();
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    g.dispose();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>TransparentCanvasFactory</code> that
   ** fills the background of an image transparent.
   **
   ** @return                    the created
   **                            <code>TransparentCanvasFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>TransparentCanvasFactory</code>.
   */
  public static TransparentCanvasFactory build() {
    return new TransparentCanvasFactory();
  }
}