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

    File        :   Config.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Config.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha;

import oracle.iam.config.captcha.type.Canvas;
import oracle.iam.config.captcha.type.Filter;
import oracle.iam.config.captcha.type.Renderer;
import oracle.iam.config.captcha.type.ColorScheme;

////////////////////////////////////////////////////////////////////////////////
// final class Config
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** The configuration of the visual captcha generator.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Config {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The visual captcha canvas. */
  private final Canvas   canvas;

  /** The visual captcha renderer. */
  private final Renderer renderer;

  /** The visual captcha filter. */
  private Filter         filter = Filter.build();

  /** The color palette used by visual captcha. */
  private ColorScheme    schema;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Config</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argumment constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  private Config(final Canvas canvas, final Renderer renderer) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.canvas   = canvas;
    this.renderer = renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canvas
  /**
   ** Returns the visual captcha canvas properties.
   **
   ** @return                    the visual captcha canvas properties.
   **                            <br>
   **                            Allowed object is {@link Canvas}.
   */
  public final Canvas canvas() {
    return this.canvas;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renderer
  /**
   ** Returns the visual captcha renderer properties.
   **
   ** @return                    the visual captcha renderer properties.
   **                            <br>
   **                            Allowed object is {@link Renderer}.
   */
  public final Renderer renderer() {
    return this.renderer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Sets the filter configuration used by visual captcha.
   **
   ** @param  value              the filter configuration used by visual
   **                            captcha.
   **                            <br>
   **                            Allowed object is array of {@link Filter}.
   **
   ** @return                    the <code>Config</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public final Config filter(final Filter value) {
    this.filter = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  /**
   ** Returns the filter configuration used by visual captcha.
   **
   ** @return                    the filter configuration used by visual
   **                            captcha.
   **                            <br>
   **                            Possible object is {@link Filter}.
   */
  public final Filter filter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Sets the color palette used by visual captcha.
   **
   ** @param  value              the color palette used by visual captcha.
   **                            <br>
   **                            Allowed object is array of {@link ColorScheme}.
   **
   ** @return                    the <code>Config</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public final Config schema(final ColorScheme value) {
    this.schema = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   schema
  /**
   ** Returns the color palette used by visual captcha.
   **
   ** @return                    the color palette used by visual captcha.
   **                            <br>
   **                            Possible object is {@link ColorScheme}.
   */
  public final ColorScheme schema() {
    return this.schema;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Config</code>.
   **
   ** @return                    an newly created instance of
   **                            <code>Config</code>.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public static Config build() {
    return build(Canvas.build(), Renderer.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Config</code> with the specified
   ** {@link Canvas}.
   ** <br>
   ** The {@link Renderer} configuration remains as per default.
   **
   ** @param  canvas             the canvas configuration to use.
   **                            <br>
   **                            Allowed object is {@link Canvas}.
   **
   ** @return                    an newly created instance of
   **                            <code>Config</code>.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public static Config build(final Canvas canvas) {
    return build(canvas, Renderer.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Config</code> with the specified
   ** {@link Renderer}.
   ** <br>
   ** The {@link Canvas} configuration remains as per default.
   **
   ** @param  renderer           the renderer configuration to use.
   **                            <br>
   **                            Allowed object is {@link Canvas}.
   **
   ** @return                    an newly created instance of
   **                            <code>Config</code>.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public static Config build(final Renderer renderer) {
    return build(Canvas.build(), renderer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Config</code> with the specified
   ** {@link Canvas} and {@link Renderer} configuration.
   **
   ** @param  canvas             the canvas configuration to use.
   **                            <br>
   **                            Allowed object is {@link Canvas}.
   ** @param  renderer           the renderer configuration to use.
   **                            <br>
   **                            Allowed object is {@link Canvas}.
   **
   ** @return                    an newly created instance of
   **                            <code>Config</code>.
   **                            <br>
   **                            Possible object is <code>Config</code>.
   */
  public static Config build(final Canvas canvas, final Renderer renderer) {
    return new Config(canvas, renderer);
  }
}