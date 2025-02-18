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

    File        :   ColorScheme.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    ColorScheme.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.config.captcha.type;

import java.io.Serializable;

import oracle.iam.platform.core.captcha.color.Space;

////////////////////////////////////////////////////////////////////////////////
// class ColorScheme
// ~~~~~ ~~~~~~~~~~~
/**
 ** Bean implementation for managing color properties of Visual Captcha
 ** Service configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ColorScheme implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The default color used by visual captcha (lightBlue). */
  private Space             danger           = Space.rgb(220, 53, 69);

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8439492777828539164")
  private static final long serialVersionUID = 4491215864110455922L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The default foreground colors used by visual captcha (lightBlue). */
  private Space[]        foreground = {Space.rgb(173, 216, 230)};

  /** The default background colors used by visual captcha (mediumBlue). */
  private Space[]        background = {Space.rgb(0, 0, 205)};

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ColorScheme</code> configuration that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private ColorScheme() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foreground
  /**
   ** Sets the foreground colors used by visual captcha.
   **
   ** @param  value              the foreground colors used by visual captcha.
   **                            <br>
   **                            Allowed object is array of {@link Space}.
   **
   ** @return                    the <code>ColorScheme</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ColorScheme</code>.
   */
  public final ColorScheme foreground(final Space[] value) {
    this.foreground = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foreground
  /**
   ** Returns the foreground colors used by visual captcha.
   **
   ** @return                    the foreground colors used by visual captcha.
   **                            <br>
   **                            Possible object is array of {@link Space}.
   */
  public final Space[] foreground() {
    return this.foreground;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   foreground
  /**
   ** Returns the foreground colors at the specified index used by visual
   ** captcha.
   **
   ** @param  index              the index for the desired {@link Space}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the foreground colors at the specified index
   **                            used by visual captcha.
   **                            <br>
   **                            Possible object is {@link Space}.
   */
  public final Space foreground(int index) {
    if (index < 0)
      index = 0;

    return this.foreground == null ? danger : index > this.foreground.length ? this.foreground[this.foreground.length - 1] : this.foreground[index];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   background
  /**
   ** Sets the background colors used by visual captcha.
   **
   ** @param  value              the background colors used by visual captcha.
   **                            <br>
   **                            Allowed object is array of {@link Space}.
   **
   ** @return                    the <code>ColorScheme</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>ColorScheme</code>.
   */
  public final ColorScheme background(final Space[] value) {
    this.background = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   background
  /**
   ** Returns the background colors used by visual captcha.
   **
   ** @return                    the background colors used by visual captcha.
   **                            <br>
   **                            Possible object is array of {@link Space}.
   */
  public final Space[] background() {
    return this.background;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   background
  /**
   ** Returns the background colors at the specified index used by visual
   ** captcha.
   **
   ** @param  index              the index for the desired {@link Space}.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the background colors at the specified index
   **                            used by visual captcha.
   **                            <br>
   **                            Possible object is {@link Space}.
   */
  public final Space background(int index) {
    if (index < 0)
      index = 0;

    return this.background == null ? danger : index > this.background.length ? this.background[this.background.length - 1] : this.background[index];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>ColorScheme</code> configuration
   ** populated with the default values.
   **
   ** @return                    an newly created instance of
   **                            <code>ColorScheme</code>.
   **                            <br>
   **                            Possible object is <code>ColorScheme</code>.
   */
  public static ColorScheme build() {
    return new ColorScheme();
  }
}