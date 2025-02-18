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

    File        :   RandomTextRenderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    RandomTextRenderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.renderer;

import oracle.iam.platform.captcha.core.Digester;

////////////////////////////////////////////////////////////////////////////////
// class RandomTextRenderer
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>RandomTextRenderer</code> renders randomly a character sequence onto a
 ** {@code BufferedImage}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RandomTextRenderer extends AbstractTextRenderer<RandomTextRenderer> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RandomTextRenderer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RandomTextRenderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrange (TextRenderer)
  /**
   ** Calulates the position of each character in the sequence <code>text</code>
   ** so that its fits in the rectange defined in in the underlying
   ** {@code BufferedImage}.
   **
   ** @param  challenge          the {@link Challenge} text to render.
   **                            <br>
   **                            Allowed object is {@link Challenge}.
   ** @param  width              the width of the {@link Challenge}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  height             the height of the {@link Challenge}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  @Override
  protected void arrange(final Challenge challenge, final int width, final int height) {
    final double r = (width - challenge.width()) / challenge.sequence().size();
    final double v = height / 2;
    double       x = r / 2;
    for (Challenge.Letter c : challenge.sequence()) {
      c.x = x;
      c.y = v + 0.35 * c.ascent + (1 - 2 * Digester.instance.nextDouble()) * (height - c.h);
      x += c.w + r;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomTextRenderer</code>.
   **
   ** @return                    the created <code>RandomTextRenderer</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomTextRenderer</code>.
   */
  public static RandomTextRenderer build() {
    return new RandomTextRenderer();
  }
}