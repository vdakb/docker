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

    File        :   AbstractTextRenderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    AbstractTextRenderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.renderer;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;

import java.awt.geom.Rectangle2D;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.font.FontFactory;

import oracle.iam.service.captcha.core.color.ColorFactory;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractFilterFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractFilterFactory</code> is the base class to render a character
 ** sequence onto a {@link BufferedImage}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
abstract class AbstractTextRenderer<T extends TextRenderer> implements TextRenderer<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractTextRenderer</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected AbstractTextRenderer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render (TextRenderer)
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
  @Override
  public void render(final BufferedImage canvas, final String sequence, final FontFactory fontFactory, final ColorFactory colorFactory) {
    final Graphics2D g = (Graphics2D)canvas.getGraphics();
    final Challenge  s = convert(sequence, g, fontFactory, colorFactory);
    arrange(s, canvas.getWidth(), canvas.getHeight());
    g.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    for (Challenge.Letter l : s.sequence()) {
      g.setColor(l.color);
      g.drawString(l.iterator(), (float)l.x, (float)l.y);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   arrange
  /**
   ** Calulates the position of each character in the sequence <code>text</code>
   ** so that its fits in the rectange defined in in the underlying
   ** {@link BufferedImage}.
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
  protected abstract void arrange(final Challenge challenge, final int width, final int height);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convert
  /**
   ** Converts a java string to a {@link Challenge} with the fonts and colors
   ** provided by the factories given.
   **
   ** @param  text               the java string to convert.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  g                  the graphic context to get the font mertrics
   **                            from.
   **                            <br>
   **                            Allowed object is {@link Graphics2D}.
   ** @param  fontFactory        the {@link FontFactory} to randomly choose a
   **                            font.
   **                            <br>
   **                            Allowed object is {@link FontFactory}.
   ** @param  colorFactory       the {@link ColorFactory} to randomly choose a
   **                            font.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   **
   ** @return                    the {@link Challenge} converted from the given
   **                            java string.
   **                            <br>
   **                            Possible object is {@link Challenge}.
   */
  protected Challenge convert(final String text, final Graphics2D g, final FontFactory fontFactory, final ColorFactory colorFactory) {
    final Challenge         challenge = Challenge.build();
    final FontRenderContext context   = g.getFontRenderContext();
    double                  x         = 0;
    for (int i = 0; i < text.length(); i++) {
      final char         c      = text.charAt(i);
      final Font         font   = fontFactory.get(i);
      final FontMetrics  metric = g.getFontMetrics(font);
      final Rectangle2D  bounds = font.getStringBounds(String.valueOf(c), context);
      challenge.add(c, x, 0, metric.charWidth(c), metric.getAscent() + metric.getDescent(), metric.getAscent(), metric.getAscent(), font, colorFactory.get(i));
      x += bounds.getWidth();
    }
    return challenge;
  }
}