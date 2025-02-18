/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Password Reset Administration

    File        :   SimpleCaptchaServlet.java

    Compiler    :   Oracle JDeveloper 12c

    Purpose     :   This file implements the class
                    SimpleCaptchaServlet.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
*/

package bka.iam.identity.pwr.servlet;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.renderer.DefaultWordRenderer;

/**
 ** This class extends the {@link HttpServlet} to generate a custom captcha image
 ** on every http request to prevent bot interaction.
 */
public class SimpleCaptchaServlet extends HttpServlet {

  /** the official serial version ID which says cryptically which version we're
   ** compatible with
   */
  private static final long        serialVersionUID = 4879107577254912870L;
  /** A {@link List} of {@link Color}s that defines the colours of image to generate  */
  private static final List<Color> COLORS           = new ArrayList<>();
  /** A {@link List} of {@link Font}s that defines the fonts of image to generate  */
  private static final List<Font>  FONTS            = new ArrayList<>();
  /** The width of the image to generate */
  private static final int         WIDTH            = 180;
  /** The height of the image to generate */
  private static final int         HEIGHT           = 60;


  static {
    COLORS.add(new Color(0, 75, 118));

    FONTS.add(new Font("Geneva", 2, 48));
    FONTS.add(new Font("Courier", 1, 48));
    FONTS.add(new Font("Arial", 1, 48));
  }

  /**
   * Generates a new captcha on every request.
   * Writes the image to the response object to render,
   * and sets the captcha instance to the session for later usage and verification
   *
   * @param req  the actual {@link HttpServletRequest} instance
   * @param resp the actual {@link HttpServletResponse} instance
   * @see <a href="https://simplecaptcha.sourceforge.net/custom_images.html">SimpleCaptcha custom images</a>
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    Captcha captcha = new Captcha.Builder(WIDTH, HEIGHT)
        .addText(new DefaultWordRenderer(COLORS, FONTS))
        .gimp()
        .addNoise()
        .build();

    CaptchaServletUtil.writeImage(resp, captcha.getImage());
    req.getSession().setAttribute(Captcha.NAME, captcha);
  }
}
