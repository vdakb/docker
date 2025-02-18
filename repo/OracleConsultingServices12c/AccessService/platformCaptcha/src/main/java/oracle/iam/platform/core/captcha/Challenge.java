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

    File        :   Challenge.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Challenge.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha;

import java.awt.Color;

import java.awt.image.BufferedImage;

import oracle.iam.config.captcha.type.Size;
import oracle.iam.config.captcha.type.Margin;

import oracle.iam.platform.core.captcha.word.WordFactory;
import oracle.iam.platform.core.captcha.word.RandomWordFactory;

import oracle.iam.platform.core.captcha.font.FontFactory;
import oracle.iam.platform.core.captcha.font.RandomFontFactory;

import oracle.iam.platform.core.captcha.color.ColorFactory;
import oracle.iam.platform.core.captcha.color.SingleColorFactory;

import oracle.iam.platform.core.captcha.filter.FilterFactory;
import oracle.iam.platform.core.captcha.filter.CurvesRippleFilterFactory;

import oracle.iam.platform.core.captcha.renderer.TextRenderer;
import oracle.iam.platform.core.captcha.renderer.CenterTextRenderer;

import oracle.iam.platform.core.captcha.canvas.CanvasFactory;
import oracle.iam.platform.core.captcha.canvas.SingleColorCanvasFactory;

////////////////////////////////////////////////////////////////////////////////
// abstract class Challenge
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** <code>Challenge</code> provides base functionalities to create a
 ** {@link Captcha} challenge.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Challenge<T extends Challenge> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Size          size   = Size.build(160, 60);
  protected Margin        margin = Margin.build();

  protected FontFactory   font;
  protected WordFactory   word;
  protected ColorFactory  color;
  protected FilterFactory filter;
  protected TextRenderer  renderer;

  private   CanvasFactory canvas;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Simple
  // ~~~~~ ~~~~~~
  /**
   ** <code>Simple</code> provides functionalities to create a simplified
   ** {@link Captcha} challenge.
   */
  public static class Simple extends Challenge<Simple>{

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Simple</code> that allows use as a JavaBean.
     **
     ** @param  color            the color factory to apply at the
     **                          {@link Captcha} challenge text.
     **                          <br>
     **                          Allowed object is {@link Color}.
     ** @param  background       the canvas background of the {@link Captcha}
     **                          challenge image.
     **                          <br>
     **                          Allowed object is {@link Color}.
     ** @param  filter           the filter to apply onto the {@link Captcha}
     **                          challenge text.
     **                          <br>
     **                          Allowed object is {@link FilterFactory}.
     */
    private Simple(final ColorFactory color, final Color background, final FilterFactory filter) {
      // ensure inheritance
      this(SingleColorCanvasFactory.build(SingleColorFactory.build(background)), color, filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Simple</code> that allows use as a JavaBean.
     **
     **
     ** @param  canvas           the {@link CanvasFactory} to create the image
     **                          with a proper background.
     **                          <br>
     **                          Allowed object is {@link CanvasFactory}.
     ** @param  color            the color of the {@link Captcha} challenge
     **                          text.
     **                          <br>
     **                          Allowed object is {@link ColorFactory}.
     ** @param  filter           the filter to apply onto the {@link Captcha}
     **                          challenge text.
     **                          <br>
     **                          Allowed object is {@link FilterFactory}.
     */
    protected Simple(final CanvasFactory canvas,final ColorFactory color, final FilterFactory filter) {
      // ensure inheritance
      super(canvas);

      // initialize instance attributes
      this.word     = RandomWordFactory.build();
      this.font     = RandomFontFactory.build();
      this.color    = color;
      this.filter   = filter;
      this.renderer = CenterTextRenderer.build();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Challenge</code> with the specified
   ** {@link CanvasFactory}.
   ** <br>
   ** {@link CanvasFactory} is responsible to create the image.
   **
   ** @param  canvas             the {@link CanvasFactory} to create the image
   **                            with a proper background.
   **                            <br>
   **                            Allowed object is {@link CanvasFactory}.
   */
  protected Challenge(final CanvasFactory canvas) {
    // ensure inheritance
    super();

    // intialize instance attributes
    this.canvas = canvas;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Set the width of the {@link Captcha} challenge.
   **
   ** @param  value              the width of the {@link Captcha} challenge.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T width(final int value) {
    this.size.width(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Returns the width of the {@link Captcha} challenge.
   **
   ** @return                    the width of the {@link Captcha} challenge.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int width() {
    return this.size.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Set the height of the {@link Captcha} challenge.
   **
   ** @param  value              the height of the {@link Captcha} challenge.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T height(final int value) {
    this.size.height(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Returns the height of the {@link Captcha} challenge.
   **
   ** @return                    the height of the {@link Captcha} challenge.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final int height() {
    return this.size.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Set the width of the {@link Captcha} challenge.
   **
   ** @param  value              the size of the visual captcha to generate.
   **                            <br>
   **                            Possible object is {@link Size}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T size(final Size value) {
    this.size = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the size of the visual captcha to generate.
   **
   ** @return                    the size of the visual captcha to generate.
   **                            <br>
   **                            Possible object is {@link Size}.
   */
  public Size size() {
    return this.size;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   margin
  /**
   ** Set the clear area around the challenge text.
   **
   ** @param  value              the clear area around the challenge text.
   **                            <br>
   **                            Allowed object is {@link Margin}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T margin(final Margin value) {
    this.margin = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   margin
  /**
   ** Returns the clear area around the challenge text.
   **
   ** @return                    the clear area around the challenge text.
   **                            <br>
   **                            Possible object is {@link Margin}.
   */
  public Margin margin() {
    return this.margin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Set the {@link ColorFactory} property.
   **
   ** @param  value              the {@link ColorFactory} to use.
   **                            <br>
   **                            Allowed object is {@link ColorFactory}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public T color(final ColorFactory value) {
    this.color = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   color
  /**
   ** Returns the {@link ColorFactory} property.
   **
   ** @return                    the {@link ColorFactory} to use.
   **                            <br>
   **                            Possible object is {@link ColorFactory}.
   */
  public ColorFactory color() {
    return this.color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   font
  /**
   ** Set the {@link FontFactory} property.
   **
   ** @param  value              the {@link FontFactory} to use.
   **                            <br>
   **                            Allowed object is {@link FontFactory}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T font(final FontFactory value) {
    this.font = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   font
  /**
   ** Returns the {@link FontFactory} property.
   **
   ** @return                    the {@link FontFactory} to use.
   **                            <br>
   **                            Possible object is {@link FontFactory}.
   */
  public FontFactory font() {
    return this.font;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canvas
  /**
   ** Returns the {@link CanvasFactory} property.
   **
   ** @return                    the {@link CanvasFactory} to use.
   **                            <br>
   **                            Possible object is {@link CanvasFactory}.
   */
  public final CanvasFactory canvas() {
    return this.canvas;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   word
  /**
   ** Set the {@link WordFactory} property.
   **
   ** @param  value              the {@link WordFactory} to use.
   **                            <br>
   **                            Allowed object is {@link WordFactory}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T word(final WordFactory value) {
    this.word = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   word
  /**
   ** Returns the {@link WordFactory} property.
   **
   ** @return                    the {@link WordFactory} to use.
   **                            <br>
   **                            Possible object is {@link WordFactory}.
   */
  public WordFactory word() {
    return this.word;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderer
  /**
   ** Set the {@link TextRenderer} property.
   **
   ** @param  value              the {@link TextRenderer} to use.
   **                            <br>
   **                            Allowed object is {@link TextRenderer}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T renderer(final TextRenderer value) {
    this.renderer = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderer
  /**
   ** Returns the {@link TextRenderer} property.
   **
   ** @return                    the {@link TextRenderer} to use.
   **                            <br>
   **                            Possible object is {@link TextRenderer}.
   */
  public TextRenderer renderer() {
    return this.renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Set the {@link FilterFactory} property.
   **
   ** @param  value              the {@link FilterFactory} to use.
   **                            <br>
   **                            Allowed object is {@link FilterFactory}.
   **
   ** @return                    the <code>Challenge</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Challenge</code> of
   **                            type <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T filter(final FilterFactory value) {
    this.filter = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the {@link FilterFactory} property.
   **
   ** @return                    the {@link FilterFactory} to use.
   **                            <br>
   **                            Possible object is {@link FilterFactory}.
   */
  public FilterFactory filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>Simple</code>.
   **
   ** @param  color              the color of the {@link Captcha} challenge
   **                            text.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  background         the canvas background of the{@link Captcha}
   **                            challenge image.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  filter             the filter to apply onto the {@link Captcha}
   **                            challenge text.
   **                            <br>
   **                            Allowed object is {@link FilterFactory}.
   **
   ** @return                    the created <code>Simple</code> {@link Captcha}
   **                            challenge.
   **                            <br>
   **                            Possible object is <code>Simple</code>.
   */
  public static Simple simple(final Color color, final Color background, final FilterFactory filter) {
    return new Simple(SingleColorFactory.build(color), background, filter);
  }
  public static Simple simple(final CanvasFactory canvas, final ColorFactory color, final FilterFactory filter) {
    return new Simple(canvas, color, filter);
  }

  public static Simple curves(final CanvasFactory canvas, final ColorFactory color) {
    return simple(canvas, color, CurvesRippleFilterFactory.build().color(color));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   captcha
  /**
   ** Returns a {@link Captcha} generated.
   **
   ** @return                    a {@link Captcha} containing a random character
   **                            sequence from an alphabet.
   **                            <br>
   **                            Possible object is {@link Captcha}.
   */
  public final Captcha captcha() {
    // capture the challenge text
    String challenge = this.word.next();
    int width        = this.font.avarage() * challenge.length();
    BufferedImage image  = new BufferedImage(
      width  + this.margin.left() + this.margin.right()
    , (int)this.size.height() + this.margin.top()  + this.margin.bottom()
    , BufferedImage.TRANSLUCENT
    );
    this.renderer.render(image, challenge, this.font, this.color);
    image = this.filter == null ? image : this.filter.apply(image);
    final BufferedImage canvas = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    this.canvas.render(canvas);
    canvas.getGraphics().drawImage(image, 0, 0, null);
    return Captcha.build(challenge, canvas);
  }
}