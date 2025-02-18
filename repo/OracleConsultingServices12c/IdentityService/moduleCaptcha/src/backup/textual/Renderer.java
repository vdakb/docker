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

    File        :   Renderer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Renderer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.util.List;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.font.GlyphVector;
import java.awt.font.FontRenderContext;

import java.awt.image.BufferedImage;

import java.awt.geom.AffineTransform;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.service.captcha.core.Digester;
import oracle.iam.service.captcha.core.renderer.Challenge;

////////////////////////////////////////////////////////////////////////////////
// interface Renderer
// ~~~~~~~~ ~~~~~~~~~
/**
 ** Renderer to transform character sequences into an image.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Renderer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final List<Font>  FONT   = CollectionUtility.unmodifiableList(
    new Font("Arial",   Font.BOLD, 40)
  , new Font("Courier", Font.BOLD, 40)
  );
  static final List<Color> COLOR  = CollectionUtility.unmodifiableList(Color.BLACK);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~~
  /**
   ** The default implementation of a {@link Renderer} to transform character
   ** sequences into an image.
   */
  public static class Default implements Renderer{

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the text will be rendered 25%/5% of the image height/width from the X and
    // Y axis
    private static final double YOFFSET = 0.25;
    private static final double XOFFSET = 0.05;

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    protected final List<Font>  font;
    protected final List<Color> color;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Default</code> renderer that will used the specified
     ** {@link Color}s and {@link Font}s to render the character sequence.
     **
     ** @param  color            the collection of {@link Color}s used for
     **                          rendering.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Color}.
     ** @param  font             the collection of {@link Font}s to used for
     **                          rendering.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Font}.
     */
    private Default(final List<Color> color, final List<Font> font) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.font = font;
      this.color = color;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (Renderer)
    /**
     ** Render a word to a {@link BufferedImage}.
     **
     ** @param  sequence         the character sequence to be rendered.
     **                          <br>
     **                          Allowed Object is {@link String}.
     ** @param  image            the image onto which the character sequence
     **                          will be rendered.
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final Challenge challenge, final BufferedImage image) {
      int                     x  = (int)Math.round(image.getWidth() * XOFFSET);
      final int               y  = image.getHeight() - (int)Math.round(image.getHeight() * YOFFSET);
      final Graphics2D        g  = canvas(image);
      final FontRenderContext c  = g.getFontRenderContext();
      final char[]            ca = new char[1];
      for (Challenge.Letter cursor : challenge.sequence()) {
        ca[0] = cursor.c;
        final Font        font  = this.font.get(Digester.instance.nextInt(this.color.size()));
        final GlyphVector glyph = font.createGlyphVector(c, ca);
        g.setColor(this.color.get(Digester.instance.nextInt(this.color.size())));
        g.setFont(font);
        g.drawChars(ca, 0, ca.length, x, y);
        x += (int)glyph.getVisualBounds().getWidth();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: canvas
    /**
     ** Constructs and returns a {@link Graphics2D} object for rendering into
     ** the specified {@link BufferedImage}.
     **
     ** @param  image            the {@link BufferedImage}.
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     **
     ** @return                  a {@link Graphics2D} to be used for rendering
     **                          into the specified {@link BufferedImage}.
     **                          <br>
     **                          Possible object is {@link Graphics2D}.
     */
    protected static Graphics2D canvas(final BufferedImage image) {
      final RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

      final Graphics2D canvas = image.createGraphics();
      canvas.setRenderingHints(hints);
      return canvas;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Captcha
  // ~~~~~ ~~~~~ ~~~~~~~
  /**
   ** The CAPTCHA implementation of a {@link Renderer} to transform character
   ** sequences into an image.
   */
  public static final class Captcha extends Default {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    private final List<Font> mono;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Captcha</code> text renderer that will used the
     ** specified {@link Color}s and {@link Font}s to render the character
     ** sequence.
     **
     ** @param  color            the collection of {@link Color}s to used for
     **                          rendering.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Color}.
     ** @param  lowerCase        the collection of {@link Font}s to used for
     **                          rendering lowercase letters.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Font}.
     ** @param  upperCase        the collection of {@link Font}s to used for
     **                          rendering uppercase letters.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link Font}.
     */
    private Captcha(final List<Color> color, final List<Font> lowerCase, final List<Font> upperCase) {
      // ensure inheritance
      super(color, lowerCase);

      // initialize instance attributes
      this.mono = upperCase;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: render (overridden)
    /**
     ** Render a word to a {@link BufferedImage}.
     **
     ** @param  sequence         the character sequence to be rendered.
     **                          <br>
     **                          Allowed Object is {@link String}.
     ** @param  image            the image onto which the character sequence
     **                          will be rendered.
     **                          <br>
     **                          Allowed Object is {@link BufferedImage}.
     */
    @Override
    public void render(final Challenge challenge, final BufferedImage image) {
      int                     x  = (int) Math.round(image.getWidth() * (Digester.instance.nextFloat() * (0.2 - 0.3)) + 0.2);
      final int               y  = image.getHeight() - (int) Math.round(image.getHeight() * (Digester.instance.nextFloat() * (0.6 - 0.45)) + 0.6);
      final Graphics2D        g  = canvas(image);
      final FontRenderContext c  = g.getFontRenderContext();
      final char[]            ca = new char[1];
      for (Challenge.Letter cursor : challenge.sequence()) {
        ca[0] = cursor.c;
        Font font = null;
        if (Character.isUpperCase(cursor.c)) {
          int choiceFont = Digester.instance.nextInt(this.mono.size());
          font = this.mono.get(choiceFont);
        }
        else {
          int choiceFont = Digester.instance.nextInt(this.font.size());
          font = this.font.get(choiceFont);
        }
        final GlyphVector     glyph = font.createGlyphVector(c, ca);
        final AffineTransform aftrf = new AffineTransform();
        aftrf.rotate(Math.toRadians(15), 0, 0);

        g.setFont(font);
        g.setTransform(aftrf);
        g.drawChars(ca, 0, ca.length, x, y);
        x += (int)glyph.getVisualBounds().getWidth();
      }
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildDefault
  /**
   ** Factory method to creates a default <code>Renderer</code>.
   **
   ** @return                    a new default <code>Captcha</code>
   **                            <code>Renderer</code>.
   **                            <br>
   **                            Possible object is <code>Renderer</code>.
   */
  public static Renderer buildDefault() {
    return buildDefault(COLOR, FONT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildDefault
  /**
   ** Factory method to creates a default <code>Renderer</code>.
   **
   ** @param  color              the collection of {@link Color}s used for
   **                            rendering.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Color}.
   ** @param  font               the collection of {@link Font}s to used for
   **                            rendering.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Font}.
   **
   ** @return                    a new default <code>Captcha</code>
   **                            <code>Renderer</code>.
   **                            Possible object is <code>Renderer</code>.
   */
  public static Renderer buildDefault(final List<Color> color, final List<Font> font) {
    return new Default(color, font);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildExtended
  /**
   ** Factory method to creates a extended <code>Renderer</code>.
   **
   ** @param  color              the collection of {@link Color}s used for
   **                            rendering.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Color}.
   ** @param  lowerCase          the collection of {@link Font}s to used for
   **                            rendering lowercase letters.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Font}.
   ** @param  upperCase          the collection of {@link Font}s to used for
   **                            rendering uppercase letters.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Font}.
   **
   ** @return                    a new default <code>Captcha</code>
   **                            <code>Renderer</code>.
   **                            Possible object is <code>Renderer</code>.
   */
  public static Renderer buildExtended(final List<Color> color, final List<Font> lowerCase, final List<Font> upperCase) {
    return new Captcha(color, lowerCase, upperCase);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   render
  /**
   ** Render a word to a {@link BufferedImage}.
   **
   ** @param  challenge          the character sequence to be rendered.
   **                            <br>
   **                            Allowed Object is {@link String}.
   ** @param  image              the image onto which the character sequence
   **                            will be rendered.
   **                            <br>
   **                            Allowed Object is {@link BufferedImage}.
   */
  void render(final Challenge challenge, final BufferedImage image);
}