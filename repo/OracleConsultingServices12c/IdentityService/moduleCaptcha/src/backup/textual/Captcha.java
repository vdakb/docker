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

    File        :   Captcha.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Captcha.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.util.Queue;
import java.util.LinkedList;

import java.util.concurrent.TimeUnit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.canvas.CanvasFactory;
import oracle.iam.service.captcha.core.canvas.TransparentCanvasFactory;
import oracle.iam.service.captcha.core.renderer.Challenge;

////////////////////////////////////////////////////////////////////////////////
// class Captcha
// ~~~~~ ~~~~~~~
/**
 ** The <code>Captcha</code> challenge/response state.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Captcha {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final int     DEGREE_MIN     = 15;
  public static final int     DEGREE_MAX     = 180;
  public static final Integer DEGREE_DEFAULT = 30;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String        challenge;
  private final BufferedImage image;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** Builds <code>ExpiringMap</code> instances.
   ** <br>
   ** Defaults to expiration of 60 {@link TimeUnit#SECONDS}.
   **
   ** @param  <T>                the type of the key implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the loggables
   **                            extending this class (loggables can return
   **                            their own specific type instead of type defined
   **                            by this class only).
   ** @param  <R>                the type of the value implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the loggables
   **                            extending this class (loggables can return
   **                            their own specific type instead of type defined
  **                             by this class only).
   */
  public static final class Builder {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private boolean           border      = false;
    private boolean           mixedCase   = false;

    private CanvasFactory     canvas      = TransparentCanvasFactory.build();
    private Producer          producer    = Producer.buildDefault();
    private Renderer          renderer    = Renderer.buildDefault();
    private Dimension         dimension;
    private Queue<Distorting> distorting;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Builder</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** The lifetime of any entry defaulted to 10 minutes.
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Sets the dimension of the <code>CAPTCHA</code> image.
     **
     ** @param  width            the with of the image.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  height           the height of the image.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @see    #width(int)
     ** @see    #height(int)
     */
    public final Builder with(final int width, final int height) {
      return with(new Dimension(width, height));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Sets the dimension of the <code>CAPTCHA</code> image.
     **
     ** @param  value            the dimension of the <code>CAPTCHA</code>
     **                          image.
     **                          <br>
     **                          Allowed object is {@link Dimension}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @see    #width(int)
     ** @see    #height(int)
     */
    public final Builder with(final Dimension value) {
      this.dimension = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Sets the text {@link Producer} of the <code>CAPTCHA</code> image.
     **
     ** @param  value            the text {@link Producer} of the
     **                          <code>CAPTCHA</code> image.
     **                          <br>
     **                          Allowed object is {@link Producer}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder with(final Producer value) {
      this.producer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Sets the text {@link Renderer} of the <code>CAPTCHA</code> image.
     **
     ** @param  value            the text {@link Renderer} of the
     **                          <code>CAPTCHA</code> image.
     **                          <br>
     **                          Allowed object is {@link Renderer}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder with(final Renderer value) {
      this.renderer = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Sets the {@link CanvasFactory} renderer of the <code>CAPTCHA</code>
     ** image.
     **
     ** @param  value            the {@link CanvasFactory} renderer of the
     **                          <code>CAPTCHA</code> image.
     **                          <br>
     **                          Allowed object is {@link CanvasFactory}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder with(final CanvasFactory value) {
      this.canvas = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: with
    /**
     ** Adds a {@link Distorting} renderer of the <code>CAPTCHA</code> image.
     **
     ** @param  value            the {@link Distorting} renderer of the
     **                          <code>CAPTCHA</code> image.
     **                          <br>
     **                          Allowed object is {@link Gimp}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public final Builder with(final Distorting value) {
      if (this.distorting == null)
        this.distorting = new LinkedList<>();
      this.distorting.add(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: border
    /**
     ** Draw a single-pixel wide black border around the image.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder border() {
      this.border = true;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mixedCase

    /**
     ** The generated text contains uppercase letters, lower case letters and
     ** digits.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder mixedCase() {
      this.mixedCase = true;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to creates a new instance of {@link Captcha} with
     ** properties of this <code>Builder</code>.
     **
     ** @return                    the created {@link Captcha}.
     **                            <br>
     **                            Possible object is {@link Captcha}.
     */
    public Captcha build() {
      final BufferedImage image = this.canvas.create(this.dimension.width, this.dimension.height);
      // paint the main image over the background
      final Graphics2D g = image.createGraphics();
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
      g.drawImage(image, null, null);

      if (this.border) {
        int width  = image.getWidth();
        int height = image.getHeight();

        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, width);
        g.drawLine(0, 0, width, 0);
        g.drawLine(0, height - 1, width, height - 1);
        g.drawLine(width - 1, height - 1, width - 1, 0);
      }
      final Challenge challenge = Challenge.build();//this.mixedCase ? this.producer.shuffle() : this.producer.shuffle().toLowerCase();
      this.renderer.render(challenge, image);

      if (this.distorting != null) {
        while(this.distorting.peek() != null)
          this.distorting.poll().render(image);
      }

      return new Captcha(challenge, image);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Captcha</code> that populates its values fom
   ** the specified {@link Builder}.
   **
   ** @param  challenge          the <code>CAPTCHA</code> challenge text.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  image              the <code>CAPTCHA</code> challenge image.
   **                            <br>
   **                            Allowed object is {@link BufferedImage}.
   */
  private Captcha(final String challenge, final BufferedImage image) {
    // ensure inheritance
    super();

    // initialize instance
    this.challenge  = challenge;
    this.image      = image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Returns the <code>CAPTCHA</code> challenge image.
   **
   ** @return                    the <code>CAPTCHA</code> challenge image.
   **                            <br>
   **                            Possible object is {@link BufferedImage}.
   */
  public final BufferedImage image() {
    return this.image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Returns the <code>CAPTCHA</code> challenge text.
   **
   ** @return                    the <code>CAPTCHA</code> challenge text.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String challenge() {
    return this.challenge;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>Captcha</code> {@link Builder}.
   **
   ** @return                    a new <code>Captcha</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Captcha.Builder</code>.
   */
  public static Builder builder() {
    return new Builder();
  }
}