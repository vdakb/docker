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

    File        :   GradientColorFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    GradientColorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.core.color;

import java.awt.Color;

////////////////////////////////////////////////////////////////////////////////
// class GradientColorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Gradients are elements of the image data type that show a transition between
 ** two or more colors. These transitions are shown as either linear or radial.
 ** Because they are of the image data type, gradients can be used anywhere an
 ** image might be. The most popular use for gradients would be in a background
 ** element.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class GradientColorFactory implements ColorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Color start;
  private final Color stop;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GradientColorFactory</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   **
   ** @param  start              the start {@link Color} of the gradient.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  stop               the stop {@link Color} of the gradient.
   **                            <br>
   **                            Allowed object is {@link Color}.
   */
  private GradientColorFactory(final Color start, final Color stop) {
    // ensure inheritance
    super();

    this.start = start;
    this.stop  = stop;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented intefaceses
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (ColorFactory)
  /**
   ** Returns the {@link Color} mutation at the specified <code>index</code> for
   ** a gradient element.
   **
   ** @param  index              the color index of the gradient.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the step {@link Color} in the gradient.
   **                            <br>
   **                            Possible object is {@link Color}.
   */
  @Override
  public Color get(final int index) {
    return new Color(
      (this.start.getRed()   + this.stop.getRed()   * index) % 256
    , (this.start.getGreen() + this.stop.getGreen() * index) % 256
    , (this.start.getBlue()  + this.stop.getBlue()  * index) % 256
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>GradientColorFactory</code>.
   **
   ** @return                    the created <code>GradientColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>GradientColorFactory</code>.
   */
  public static GradientColorFactory build() {
    // La Rioja as start color and Oriental Pink as stop
    return build(new Color(192, 192, 0), new Color(192, 128, 128));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>GradientColorFactory</code>.
    **
   ** @param  start              the start {@link Color} of the gradient.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  stop               the stop {@link Color} of the gradient.
   **                            <br>
   **                            Allowed object is {@link Color}.
  **
   ** @return                    the created <code>GradientColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>GradientColorFactory</code>.
   */
  public static GradientColorFactory build(final Color start, final Color stop) {
    return new GradientColorFactory(start, stop);
  }
}