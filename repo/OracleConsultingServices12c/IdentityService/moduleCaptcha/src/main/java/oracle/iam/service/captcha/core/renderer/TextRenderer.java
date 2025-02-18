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

    File        :   TextRenderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    TextRenderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.renderer;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.font.FontFactory;

import oracle.iam.service.captcha.core.color.ColorFactory;

////////////////////////////////////////////////////////////////////////////////
// interface TextRenderer
// ~~~~~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>TextRenderer</code> renders text onto a {@link BufferedImage}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface TextRenderer<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render
  /**
   ** Render a text onto a {@link BufferedImage}.
   **
   ** @param  canvas             the image onto which the character sequence
   **                            will be rendered.
   **                            <br>
   **                            Allowed Object is {@link BufferedImage}.
   ** @param  sequence           the character sequence to be rendered.
   **                            <br>
   **                            Allowed Object is {@link String}.
   ** @param  fontFactory        the {@link FontFactory} to use.
   **                            <br>
   **                            Allowed object is {@link FontFactory}.
   ** @param  colorFactory       the {@link ColorFactory} to use.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   */
  void render(final BufferedImage canvas, final String sequence, final FontFactory fontFactory, final ColorFactory colorFactory);
}