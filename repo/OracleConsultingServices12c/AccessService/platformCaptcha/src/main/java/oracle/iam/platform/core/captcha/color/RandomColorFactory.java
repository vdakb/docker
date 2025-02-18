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

    File        :   RandomColorFactory.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RandomColorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.color;

import java.awt.Color;

import oracle.iam.platform.core.captcha.Digester;

////////////////////////////////////////////////////////////////////////////////
// class RandomColorFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** <code>RandomColorFactory</code> returns the next pseudorandom, uniformly
 ** distributed {@link Color} value between <code>lower</code> (inclusive) and
 ** the specified <code>upper</code> (exclusive) limit value {@link Color},
 ** drawn from the random number generator's sequence.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RandomColorFactory implements ColorFactory {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Color color;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RandomColorFactory</code> with a randomly choosen color
   ** in the bounds of the upper and lower limited colors.
   **
   ** @param  lower              the lower limit for the {@link Color}s from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  upper              the upper limit for the {@link Color}s from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link Color}.
   */
  private RandomColorFactory(final Color lower, final Color upper) {
    // ensure inheritance
    super();
    
    // initialize intstance attributes
    this.color = new Color(
      lower.getRed()   + Digester.instance.nextInt(upper.getRed()   - lower.getRed())
    , lower.getGreen() + Digester.instance.nextInt(upper.getGreen() - lower.getGreen())
    , lower.getBlue()  + Digester.instance.nextInt(upper.getBlue()  - lower.getBlue())
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (ColorFactory)
  /**
   ** Returns the {@link Color} mutation at the specified <code>index</code> for
   ** a gradient element.
   **
   ** @param  index              the index of the background.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the desired {@link Color} for the background.
   **                            <br>
   **                            Possible object is {@link Color}.
   */
  @Override
  public Color get(final int index) {
    return this.color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomColorFactory</code>.
   **
   ** @return                    the created <code>RandomColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomColorFactory</code>.
   */
  public static RandomColorFactory build() {
    // Cool Blue as start color and Hmmm Blue as stop
    return build(Space.awt(20, 40, 80), Space.awt(21, 50, 140));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomColorFactory</code> from a
   ** monochromatic palette.
   **
   ** @param  palette            the monochromatic color palette to use.
   **                            <br>
   **                            Allowed Value is {@link Palette}.
   **
   ** @return                    the created <code>RandomColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomColorFactory</code>.
   */
  public static RandomColorFactory build(final Palette palette) {
    return build(Space.awt(palette.color(0)), Space.awt(palette.color(palette.size() - 1)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>RandomColorFactory</code>.
   **
   ** @param  lower              the lower limit for the {@link Color}s from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link Color}.
   ** @param  upper              the upper limit for the {@link Color}s from
   **                            which one will be randomly choosen.
   **                            <br>
   **                            Allowed object is {@link Color}.
   **
   ** @return                    the created <code>RandomColorFactory</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>RandomColorFactory</code>.
   */
  public static RandomColorFactory build(final Color lower, final Color upper) {
    return new RandomColorFactory(lower, upper);
  }
}