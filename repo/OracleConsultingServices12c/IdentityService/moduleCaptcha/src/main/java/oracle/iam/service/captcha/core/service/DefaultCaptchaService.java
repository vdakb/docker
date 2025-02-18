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

    File        :   DefaultCaptchaService.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DefaultCaptchaService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.service;

import oracle.iam.service.captcha.core.canvas.CanvasFactory;
import oracle.iam.service.captcha.core.color.SingleColorFactory;
import oracle.iam.service.captcha.core.filter.CurvesRippleFilterFactory;
import oracle.iam.service.captcha.core.font.RandomFontFactory;
import oracle.iam.service.captcha.core.renderer.CenterTextRenderer;
import oracle.iam.service.captcha.core.word.RandomWordFactory;

////////////////////////////////////////////////////////////////////////////////
// class DefaultCaptchaService
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** ...
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DefaultCaptchaService extends AbstractCaptchaService<DefaultCaptchaService> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DefaultCaptchaService</code> with the specified
   ** {@link CanvasFactory}.
   ** <br>
   ** {@link CanvasFactory} is responsible to create the image.
   **
   ** @param  canvas             the {@link CanvasFactory} to create the image
   **                            with a proper background.
   **                            <br>
   **                            Allowed object is {@link CanvasFactory}.
   */
  private DefaultCaptchaService(final CanvasFactory canvas) {
    // ensure inheritance
    super(canvas);

    // initailize instance
    this.word     = RandomWordFactory.build();
    this.font     = RandomFontFactory.build();
    this.color    = SingleColorFactory.build();
    this.filter   = CurvesRippleFilterFactory.build().color(this.color);
    this.renderer = CenterTextRenderer.build();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>DefaultCaptchaService</code>.
   **
   ** @param  canvas             the {@link CanvasFactory} to create the image
   **                            with a proper background.
   **                            <br>
   **                            Allowed object is {@link CanvasFactory}.
   **
   ** @return                    the created
   **                            <code>DefaultCaptchaService</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DefaultCaptchaService</code>.
   */
  public static DefaultCaptchaService build(final CanvasFactory canvas) {
    return new DefaultCaptchaService(canvas);
  }
}