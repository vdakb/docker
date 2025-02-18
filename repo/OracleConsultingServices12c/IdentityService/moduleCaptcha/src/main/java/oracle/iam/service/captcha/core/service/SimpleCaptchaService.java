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

    File        :   SimpleCaptchaService.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SimpleCaptchaService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.service;

import java.awt.Color;

import oracle.iam.service.captcha.core.canvas.SingleColorCanvasFactory;
import oracle.iam.service.captcha.core.color.SingleColorFactory;
import oracle.iam.service.captcha.core.filter.FilterFactory;
import oracle.iam.service.captcha.core.font.RandomFontFactory;
import oracle.iam.service.captcha.core.renderer.CenterTextRenderer;
import oracle.iam.service.captcha.core.word.RandomWordFactory;

////////////////////////////////////////////////////////////////////////////////
// class SimpleCaptchaService
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>SimpleCaptchaService</code> provides functionalities to
 ** create a simplified <code>CAPTCHA</code> challenge.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// https://github.com/ppiastucki/patchca/blob/master/patchca/src/org/patchca/service/SimpleCaptchaService.java
public class SimpleCaptchaService extends AbstractCaptchaService<SimpleCaptchaService> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SimpleCaptchaService</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @param  width              the width of the <code>CAPTCHA</code> image.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  height             the height of the <code>CAPTCHA</code> image.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  color              the color of the <code>CAPTCHA</code>
   **                            challenge text.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  background         the canvas background of the
   **                            <code>CAPTCHA</code> image.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  filter             the filter to apply onto the
   **                            <code>CAPTCHA</code> challenge text.
   **                            <br>
   **                            Allowed object is {@link FilterFactory}.
   */
  private SimpleCaptchaService(final Color color, final Color background, final FilterFactory filter) {
    super(SingleColorCanvasFactory.build(SingleColorFactory.build(background)));

    this.word     = RandomWordFactory.build();
    this.font     = RandomFontFactory.build();
    this.renderer = CenterTextRenderer.build();
    this.color    = SingleColorFactory.build(color);
    this.filter   = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>SimpleCaptchaService</code>.
   **
   ** @param  color              the color of the <code>CAPTCHA</code>
   **                            challenge text.
   **                            <br>
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  background         the canvas background of the
   **                            <code>CAPTCHA</code> image.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  filter             the filter to apply onto the
   **                            <code>CAPTCHA</code> challenge text.
   **                            <br>
   **                            Allowed object is {@link FilterFactory}.
   **
   ** @return                    the created
   **                            <code>DefaultCaptchaService</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>DefaultCaptchaService</code>.
   */
  public static SimpleCaptchaService build(final Color color, final Color background, final FilterFactory filter) {
    return new SimpleCaptchaService(color, background, filter);
  }
}