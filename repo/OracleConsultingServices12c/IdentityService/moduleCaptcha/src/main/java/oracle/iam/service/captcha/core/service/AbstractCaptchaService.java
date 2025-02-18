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

    File        :   AbstractCaptchaService.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractCaptchaService.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.service;

import java.awt.image.BufferedImage;

import oracle.iam.platform.captcha.core.type.Size;
import oracle.iam.platform.captcha.core.type.Margin;

import oracle.iam.service.captcha.core.Captcha;
import oracle.iam.service.captcha.core.font.FontFactory;
import oracle.iam.service.captcha.core.word.WordFactory;
import oracle.iam.service.captcha.core.color.ColorFactory;
import oracle.iam.service.captcha.core.canvas.CanvasFactory;
import oracle.iam.service.captcha.core.filter.FilterFactory;
import oracle.iam.service.captcha.core.renderer.TextRenderer;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractCaptchaService
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** <code>AbstractCaptchaService</code> provides base functionalities to
 ** create a <code>CAPTCHA</code> challenge.
 **
 ** @param  <T>                  the implementation type of this
 **                              {@link CaptchaService}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// https://github.com/ppiastucki/patchca/blob/master/patchca/src/org/patchca/service/AbstractCaptchaService.java
public abstract class AbstractCaptchaService<T> implements CaptchaService {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected Size          size   = new Size();
  protected Margin        margin = new Margin();

  protected FontFactory   font;
  protected WordFactory   word;
  protected ColorFactory  color;
  protected FilterFactory filter;
  protected TextRenderer  renderer;

  private   CanvasFactory canvas;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractCaptchaService</code> with the specified
   ** {@link CanvasFactory}.
   ** <br>
   ** {@link CanvasFactory} is responsible to create the image.
   **
   ** @param  canvas             the {@link CanvasFactory} to create the image
   **                            with a proper background.
   **                            <br>
   **                            Allowed object is {@link CanvasFactory}.
   */
  protected AbstractCaptchaService(final CanvasFactory canvas) {
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
   ** Set the width of the <code>Captcha</code>.
   **
   ** @param  value              the width of the <code>Captcha</code>.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T width(final double value) {
    this.size.width(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   width
  /**
   ** Returns the width of the <code>Captcha</code>.
   **
   ** @return                    the width of the <code>Captcha</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final double width() {
    return this.size.width();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Set the height of the <code>Captcha</code>.
   **
   ** @param  value              the height of the <code>Captcha</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T height(final double value) {
    this.size.height(value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   height
  /**
   ** Returns the height of the <code>Captcha</code>.
   **
   ** @return                    the height of the <code>Captcha</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public final double height() {
    return this.size.height();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Set the width of the <code>Captcha</code>.
   **
   ** @param  value              the size of the visual captcha to generate.
   **                            <br>
   **                            Possible object is {@link Size}.
   **
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
   ** @return                    the <code>AbstractCaptchaService</code> to
   **                            allow method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>AbstractCaptchaService</code> of type
   **                            <code>T</code>.
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
  // Methods of implemented intefaceses
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   captcha (CaptchaService)
  /**
   ** Returns a {@link Captcha} generated.
   **
   ** @return                    a {@link Captcha} containing a random character
   **                            sequence from an alphabet.
   **                            <br>
   **                            Possible object is {@link Captcha}.
   */
  @Override
  public final Captcha captcha() {
    BufferedImage image  = new BufferedImage((int)this.size.width(), (int)this.size.height(), BufferedImage.TRANSLUCENT);
    // capture the challenge text
    String challenge = this.word.next();
    this.renderer.render(image, challenge, this.font, this.color);
    image = this.filter.apply(image);
    BufferedImage canvas = new BufferedImage(
      (int)(this.size.width()  + this.margin.left() + this.margin.right())
    , (int)(this.size.height() + this.margin.top()  + this.margin.bottom())
    , BufferedImage.TYPE_INT_ARGB
    );
    this.canvas.render(canvas);
    canvas.getGraphics().drawImage(image, this.margin.left(), this.margin.top(), null);
    return Captcha.build(challenge, canvas);
  }
}