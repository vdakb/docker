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

    File        :   Palette.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Palette.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.captcha.core.math;

import java.util.HashMap;

import java.awt.Color;

////////////////////////////////////////////////////////////////////////////////
// abstract class Palette
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Color palette utility.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
// https://raw.githubusercontent.com/MatthewYork/Colours/master/ColoursLibrary/src/com/mattyork/colours/Colour.java
public abstract class Palette {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final HashMap<String, Integer> colorMap = new HashMap<String, Integer>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    colorMap.put("white",     0xffffff);
    colorMap.put("black",     0x000000);
    colorMap.put("red",       0xff0000);
    colorMap.put("green",     0x00ff00);
    colorMap.put("blue",      0x0000ff);
    colorMap.put("cyan",      0x00ffff);
    colorMap.put("yellow",    0xffff00);
    colorMap.put("fuchsia",   0xff00ff);
    colorMap.put("maroon",    0x800000);
    colorMap.put("navy",      0x000080);
    colorMap.put("olive",     0x808000);
    colorMap.put("purple",    0x800080);
    colorMap.put("silver",    0xc0c0c0);
    colorMap.put("teal",      0x008080);
    colorMap.put("gray",      0x808080);
    colorMap.put("darkgray",  0x404040);
    colorMap.put("lightgray", 0xc0c0c0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // enum Schema
  // ~~~~ ~~~~~~
  /**
   ** Color Scheme Enumeration (for color scheme generation)
   */
  public enum Scheme {
    Square
  , Triadic
  , Tetradic
  , Analagous
  , Monochromatic
  , Complementary
  ;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Formula
  // ~~~~ ~~~~~~~
  /**
   ** Color Distance Formula
   */
  public enum Formula {
    /**
     ** The 1976 formula is the first formula that related a measured color
     ** difference to a known set of CIELAB coordinates. This formula has been
     ** succeeded by the 1994 and 2000 formulas because the CIELAB space turned
     ** out to be not as perceptually uniform as intended, especially in the
     ** saturated regions. This means that this formula rates these colors too
     ** highly as opposed to other colors. 
     */
    CIE76,

    /**
     ** The 1976 definition was extended to address perceptual non-uniformities,
     ** while retaining the CIELAB color space, by the introduction of
     ** application-specific weights derived from an automotive paint test's
     ** tolerance data.
     */
    CIE94,

    /**
     ** Since the 1994 definition did not adequately resolve the perceptual
     ** uniformity issue, the CIE refined their definition, adding five
     ** corrections.
     ** <ol>
     **   <li>A hue rotation term (R<sub>T</sub>), to deal with the problematic
     **       blue region (hue angles in the neighborhood of 275°)
     **   <li>Compensation for neutral colors (the primed values in the L*C*h
     **       differences)
     **   <li>Compensation for lightness (S<sub>L</sub>
     **   <li>Compensation for chroma (S<sub>C</sub>
     **   <li>Compensation for hue (S<sub>H</sub>
     ** </ol>
     */
    CIE2000;
  }

  public Palette() {
    super();
  }

  // System Colors

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-primary</code>
   **   <li><code>.text-primary</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int primary() {
    // #007bff
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secondary
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-secondary</code>
   **   <li><code>.text-secondary</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int secondary() {
    // #6c757d
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   success
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-success</code>
   **   <li><code>.text-success</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int success() {
    // #28a745
    return rgb(83, 215, 106);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   danger
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-danger</code>
   **   <li><code>.text-danger</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int danger() {
    // #dc3545
    return rgb(229, 0, 15);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-info</code>
   **   <li><code>.text-info</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int info() {
    // #17a2b8
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-warning</code>
   **   <li><code>.text-warning</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int warning() {
    // #ffc107
    return rgb(221, 170, 59);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   light
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-light</code>
   **   <li><code>.text-light</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int ligth() {
    // #f8f9fa
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dark
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-dark</code>
   **   <li><code>.text-dark</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int dark() {
    // #343a40
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   muted
  /**
   ** Boostrap Color similarity for the style classes:
   ** <ul>
   **   <li><code>.bg-muted</code>
   **   <li><code>.text-muted</code>
   ** </ul>
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int muted() {
    // #6c757d
    return rgb(47, 112, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   antiquewhite
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int antiquewhite() {
    return rgb(250, 235, 215);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oldlace
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int oldlace() {
    return rgb(253, 245, 230);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ivory
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int ivory() {
    return rgb(255, 255, 240);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   seashell
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int seashell() {
    return rgb(255, 245, 238);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ghostwhite
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int ghostwhite() {
    return rgb(248, 248, 255);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   snow
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int snow() {
    return rgb(255, 250, 250);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   snow
  /**
   ** White similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int linen() {
    return rgb(250, 240, 230);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   darkGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see     Color#darkGray
   */
  public static int darkGray() {
    return rgb(64, 64, 64);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gray
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see     Color#gray
   */
  public static int gray() {
    return rgb(128, 128, 128);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lightGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see     Color#lightGray
   */
  public static int lightGray() {
    return rgb(192, 192, 192);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warmGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int warmGray() {
    return rgb(133, 117, 112);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coolGray
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int coolGray() {
    return rgb(118, 122, 133);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   charcoal
  /**
   ** Gray similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int charcoal() {
    return rgb(34, 34, 34);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   salmon
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int salmon() {
    return rgb(233, 87, 95);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brickRed
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int brickRed() {
    return rgb(151, 27, 16);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   easterPink
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int easterPink() {
    return rgb(241, 167, 162);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grapefruit
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int grapefruit() {
    return rgb(228, 31, 54);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pink
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pink() {
    return rgb(255, 95, 154);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indianRed
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int indianRed() {
    return rgb(205, 92, 92);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   strawberry
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int strawberry() {
    return rgb(190, 38, 37);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coral
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int coral() {
    return rgb(240, 128, 128);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maroon
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int maroon() {
    return rgb(80, 4, 28);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   watermelon
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int watermelon() {
    return rgb(242, 71, 63);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tomato
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int tomato() {
    return rgb(255, 99, 71);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pinkLipstick
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pinkLipstick() {
    return rgb(255, 105, 180);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paleRose
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int paleRose() {
    return rgb(255, 228, 225);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   crimson
  /**
   ** Red similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int crimson() {
    return rgb(187, 18, 36);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emerald
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int emerald() {
    return rgb(1, 152, 117);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grass
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int grass() {
    return rgb(99, 214, 74);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pastelGreen
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pastelGreen() {
    return rgb(126, 242, 124);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   seafoam
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int seafoam() {
    return rgb(77, 226, 140);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   paleGreen
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int paleGreen() {
    return rgb(176, 226, 172);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cactusGreen
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cactusGreen() {
    return rgb(99, 111, 87);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chartreuse
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int chartreuse() {
    return rgb(69, 139, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hollyGreen
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hollyGreen() {
    return rgb(32, 87, 14);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   olive
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int olive() {
    return rgb(91, 114, 34);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oliveDrab
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int oliveDrab() {
    return rgb(107, 142, 35);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   moneyGreen
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int moneyGreen() {
    return rgb(134, 198, 124);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   honeydew
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int honeydew() {
    return rgb(216, 255, 231);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lime
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int lime() {
    return rgb(56, 237, 56);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cardTable
  /**
   ** Green similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cardTable() {
    return rgb(87, 121, 107);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   teal
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int teal() {
    return rgb(28, 160, 170);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   steelBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int steelBlue() {
    return rgb(103, 153, 170);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   robinEgg
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int robinEgg() {
    return rgb(141, 218, 247);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pastelBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pastelBlue() {
    return rgb(99, 161, 247);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   turquoise
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int turquoise() {
    return rgb(112, 219, 219);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   skyBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int skyBlue() {
    return rgb(0, 178, 238);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   indigo
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int indigo() {
    return rgb(13, 79, 139);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   denim
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int denim() {
    return rgb(67, 114, 170);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blueberry
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int blueberry() {
    return rgb(89, 113, 173);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cornflower
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cornflower() {
    return rgb(100, 149, 237);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   babyBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int babyBlue() {
    return rgb(190, 220, 230);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   midnightBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int midnightBlue() {
    return rgb(13, 26, 35);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fadedBlue
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int fadedBlue() {
    return rgb(23, 137, 155);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iceberg
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int iceberg() {
    return rgb(200, 213, 219);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wave
  /**
   ** Blue similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int wave() {
    return rgb(102, 169, 251);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eggplant
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int eggplant() {
    return rgb(105, 5, 98);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pastelPurple
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pastelPurple() {
    return rgb(207, 100, 235);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   palePurple
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int palePurple() {
    return rgb(229, 180, 235);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coolPurple
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int coolPurple() {
    return rgb(140, 93, 228);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   violet
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int violet() {
    return rgb(191, 95, 255);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plum
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int plum() {
    return rgb(139, 102, 139);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lavender
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int lavender() {
    return rgb(204, 153, 204);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   raspberry
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int raspberry() {
    return rgb(135, 38, 87);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fuschia
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int fuschia() {
    return rgb(255, 20, 147);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   grape
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int grape() {
    return rgb(54, 11, 88);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   periwinkle
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int periwinkle() {
    return rgb(135, 159, 237);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orchid
  /**
   ** Purple similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int orchid() {
    return rgb(218, 112, 214);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   goldenrod
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int goldenrod() {
    return rgb(215, 170, 51);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   yellowGreen
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int yellowGreen() {
    return rgb(192, 242, 39);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   banana
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int banana() {
    return rgb(229, 227, 58);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mustard
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int mustard() {
    return rgb(205, 171, 45);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buttermilk
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int buttermilk() {
    return rgb(254, 241, 181);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gold
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int gold() {
    return rgb(139, 117, 18);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cream
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cream() {
    return rgb(240, 226, 187);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lightCream
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int lightCream() {
    return rgb(240, 238, 215);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wheat
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int wheat() {
    return rgb(240, 238, 215);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   beige
  /**
   ** Yellow similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int beige() {
    return rgb(245, 245, 220);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   peach
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int peach() {
    return rgb(242, 187, 97);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   burntOrange
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int burntOrange() {
    return rgb(184, 102, 37);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pastelOrange
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int pastelOrange() {
    return rgb(248, 197, 143);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cantaloupe
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cantaloupe() {
    return rgb(250, 154, 79);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   carrot
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int carrot() {
    return rgb(237, 145, 33);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mandarin
  /**
   ** Orange similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int mandarin() {
    return rgb(247, 145, 55);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chiliPowder
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int chiliPowder() {
    return rgb(199, 63, 23);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   burntSienna
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int burntSienna() {
    return rgb(138, 54, 15);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chocolate
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int chocolate() {
    return rgb(94, 38, 5);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coffee
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int coffee() {
    return rgb(141, 60, 15);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cinnamon
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int cinnamon() {
    return rgb(123, 63, 9);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   almond
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int almond() {
    return rgb(196, 142, 72);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eggshell
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int eggshell() {
    return rgb(252, 230, 201);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sand
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int sand() {
    return rgb(222, 182, 151);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mud
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int mud() {
    return rgb(70, 45, 29);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sienna
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int sienna() {
    return rgb(160, 82, 45);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dust
  /**
   ** Brown similarity color.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int dust() {
    return rgb(236, 214, 197);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   alpha
  /**
   ** Returns the alpha component of a color int value.
   ** <br>
   ** This is the aquivalent to <code>value &gt;&gt;&gt; 24</code>.
   **
   ** @param  value              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the alpha component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static int alpha(int value) {
    return value >>> 24;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   red
  /**
   ** Returns the red component of a color int value.
   ** <br>
   ** This is the aquivalent to <code>(value &gt;&gt; 16) &amp; 0xff</code>.
   **
   ** @param  value              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the red component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static int red(int value) {
    return (value >> 16) & 0xff;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   green
  /**
   ** Returns the green component of a color int value.
   ** <br>
   ** This is the aquivalent to <code>c(value &gt;&gt; 8) &amp; 0xff</code>.
   **
   ** @param  value              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the green component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static int green(int value) {
    return (value >> 8) & 0xff;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   blue
  /**
   ** Returns the blue component of a color int value.
   ** <br>
   ** This is the aquivalent to <code>value &amp; 0xff</code>.
   **
   ** @param  value              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the blue component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   */
  public static int blue(final int value) {
    return value & 0xff;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Returns a color-int from red, green, blue components.
   ** <br>
   ** The alpha component is implicity 255 (fully opaque).
   ** <br>
   ** These component values should be [0..255], but there is no range check
   ** performed, so if they are out of range, the returned color is undefined.
   **
   ** @param  r                  the red component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  g                  the green component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  b                  the Blue component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int rgb(final int r, final int g, final int b) {
    return rgb(0xff, r, g, b);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Returns a color-int from alpha, red, green, blue components.
   ** <br>
   ** These component values should be [0..255], but there is no range check
   ** performed, so if they are out of range, the returned color is undefined.
   **
   ** @param  a                  the alpha component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  r                  the red component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  g                  the green component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  b                  the Blue component [0..255] of the color.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the color-int evaluated from the components.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int rgb(final int a, final int r, final int g, final int b) {
    return (a << 24) | (r << 16) | (g << 8) | b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb
  /**
   ** Parse the color string, and return the corresponding color-int.
   ** <br>
   ** If the string cannot be parsed, throws an IllegalArgumentException
   ** exception.
   ** Supported formats are:
   ** <ul>
   **   <li><code>#rrggbb</code>
   **   <li><code>#aarrggbb</code>
   **   <li><code>red</code>
   **   <li><code>blue</code>
   **   <li><code>green</code>
   **   <li><code>black</code>
   **   <li><code>white</code>
   **   <li><code>gray</code>
   **   <li><code>cyan</code>
   **   <li><code>magenta</code>
   **   <li><code>yellow</code>
   **   <li><code>lightgray</code>
   **   <li><code>darkgray</code>
   **   <li><code>grey</code>
   **   <li><code>lightgrey</code>
   **   <li><code>darkgrey</code>
   **   <li><code>fuschia</code>
   **   <li><code>maroon</code>
   **   <li><code>navy</code>
   **   <li><code>olive</code>
   **   <li><code>purple</code>
   **   <li><code>silver</code>
   **   <li><code>teal</code>
   ** </ul>
   **
   ** @param  value              the color string to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the color-int evaluated from the string.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int rgb(final String value) {
    if (value.charAt(0) == '#') {
      // use a long to avoid rollovers on #ffxxxxxx
      long color = Long.parseLong(value.substring(1), 16);
      if (value.length() == 7) {
        // set the alpha value
        color |= 0x00000000ff000000;
      }
      else if (value.length() != 9)
        throw new IllegalArgumentException("Unknown color");

      return (int)color;
    }
    else {
      Integer color = colorMap.get(value.toLowerCase());
      if (color != null) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hue
  /**
   ** Returns the hue component of a color int.
   **
   ** @param  color              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a value between 0.0f and 1.0f.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float hue(final int color) {
    int   r = (color >> 16) & 0xff;
    int   g = (color >> 8)  & 0xff;
    int   b = color         & 0xff;
    int   v = Math.max(b, Math.max(r, g));
    int   temp = Math.min(b, Math.min(r, g));
    float h;
    if (v == temp) {
      h = 0;
    }
    else {
      final float vtemp = v - temp;
      final float cr = (v - r) / vtemp;
      final float cg = (v - g) / vtemp;
      final float cb = (v - b) / vtemp;
      if (r == v) {
        h = cb - cg;
      }
      else if (g == v) {
        h = 2 + cr - cb;
      }
      else {
        h = 4 + cg - cr;
      }
      h /= 6.f;
      if (h < 0) {
        h++;
      }
    }
    return h;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saturation
  /**
   ** Returns the saturation component of a color int.
   **
   ** @param  color              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a value between 0.0f and 1.0f.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float saturation(final int color) {
    int   r = (color >> 16) & 0xff;
    int   g = (color >> 8)  & 0xff;
    int   b = color         & 0xff;
    int   v = Math.max(b, Math.max(r, g));
    int   temp = Math.min(b, Math.min(r, g));
    float s;
    if (v == temp) {
      s = 0;
    }
    else {
      s = (v - temp) / (float)v;
    }
    return s;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   brightness
  /**
   ** Returns the brightness component of a color int.
   **
   ** @param  color              the color int value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    a value between 0.0f and 1.0f.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float brightness(final int color) {
    int r = (color >> 16) & 0xff;
    int g = (color >> 8)  & 0xff;
    int b = color         & 0xff;
    int v = Math.max(b, Math.max(r, g));
    return (v / 255.f);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Creates an int[] of 4 Colors that complement the Color.
   **
   ** @param  color              the base color int value.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  type               the color scheme.
   **                            <br>
   **                            Allowed object is <code>Scheme</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] of(final int color, final Scheme type) {
    final float[] hsb = new float[3];
    rgb2hsb(color, hsb);

    switch (type) {
      case Square        : return squareScheme(hsb);
      case Triadic       : return traidicScheme(hsb);
      case Tetradic      : return tetradicScheme(hsb);
      case Analagous     : return analagousScheme(hsb);
      case Monochromatic : return monochromaticScheme(hsb);
      case Complementary : return complementaryScheme(hsb);
      default            : return null;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   analagousScheme
  /**
   ** Analogous color schemes use colors that are next to each other on the
   ** color wheel. They usually match well and create serene and comfortable
   ** designs.
   ** <p>
   ** Analogous color schemes are often found in nature and are harmonious
   ** and pleasing to the eye.
   ** <p>
   ** Make sure you have enough contrast when choosing an analogous color
   ** scheme.
   ** <p>
   ** Choose one color to dominate, a second to support. The third color is used
   ** (along with black, white or gray) as an accent.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on analagous color scheme.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] analagousScheme(final float[] base) {
    /*
    float[] ca1 = { wheel(base[0],  15), (float)(base[1] - 0.05), (float)(base[2] - 0.05) };
    float[] ca2 = { wheel(base[0],  30), (float)(base[1] - 0.05), (float)(base[2] - 0.1) };
    float[] cb1 = { wheel(base[0], -15), (float)(base[1] - 0.05), (float)(base[2] - 0.05) };
    float[] cb2 = { wheel(base[0], -30), (float)(base[1] - 0.05), (float)(base[2] - 0.1) };
    */
    float[] ca1 = { wheel(base[0],  30), (float)(base[1] - 0.05), (float)(base[2] - 0.05) };
    float[] ca2 = { wheel(base[0],  60), (float)(base[1] - 0.1), (float)(base[2] - 0.1) };
    return new int[] { hsb2rgb(base), hsb2rgb(ca1), hsb2rgb(ca2), hsb2rgb(base) };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   monochromaticScheme
  /**
   ** Monochromatic color schemes are derived from a single base hue and
   ** extended using its shades, tones and tints. Tints are achieved by adding
   ** white and shades and tones are achieved by adding a darker color, grey or
   ** black.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on monochromatic color scheme.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] monochromaticScheme(final float[] base) {
    float[] ca1 = { base[0], base[1],     base[2] / 2 };
    float[] ca2 = { base[0], base[1] / 2, base[2] / 3 };
    float[] cb1 = { base[0], base[1] / 3, base[2] * 2 / 3 };
    float[] cb2 = { base[0], base[1],     base[2] * 4 / 5 };
    return new int[] { hsb2rgb(ca1), hsb2rgb(ca2), hsb2rgb(cb1), hsb2rgb(cb2) };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traidicScheme
  /**
   ** A triadic color scheme uses colors that are evenly spaced around the color
   ** wheel.
   ** <p>
   ** Triadic color schemes tend to be quite vibrant, even if use pale or
   ** unsaturated versions of hues.
   ** <p>
   ** To use a triadic harmony successfully, the colors should be carefully
   ** balanced - let one color dominate and use the two others for accent.
   ** <p>
   ** Burger King uses this color scheme quite successfully.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on complementary color scheme.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] traidicScheme(final float[] base) {
    float[] ca1 = { wheel(base[0], 120), base[1],         base[2] };
    float[] ca2 = { wheel(base[0], 120), base[1] * 7 / 6, (float)(base[2] - 0.05) };
    float[] cb1 = { wheel(base[0], 240), base[1],         base[2] };
    float[] cb2 = { wheel(base[0], 240), base[1] * 7 / 6, (float)(base[2] - 0.05) };
    return new int[] { hsb2rgb(ca1), hsb2rgb(ca2), hsb2rgb(cb1), hsb2rgb(cb2) };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complementaryScheme
  /**
   ** Colors that are opposite each other on the color wheel are considered to
   ** be complementary colors (example: red and green).
   ** <p>
   ** The high contrast of complementary colors creates a vibrant look
   ** especially when used at full saturation. This color scheme must be managed
   ** well so it is not jarring.
   ** <p>
   ** Complementary color schemes are tricky to use in large doses, but work
   ** well when you want something to stand out.
   ** <p>
   ** Complementary colors are really bad for text.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on complementary colors.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] complementaryScheme(final float[] base) {
    float[] ca1 = { base[0],             base[1] * 5 / 7, base[2] };
    float[] ca2 = { base[0],             base[1],         base[2] * 4 / 5 };
    float[] cb1 = { wheel(base[0], 180), base[1],         base[2] };
    float[] cb2 = { wheel(base[0], 180), base[1] * 5 / 7, base[2] };
    return new int[]{hsb2rgb(ca1), hsb2rgb(ca2), hsb2rgb(cb1), hsb2rgb(cb2)};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tetradicScheme
  /**
   ** The tetradic or rectangle color scheme uses four colors arranged into two
   ** complementary pairs.
   ** <p>
   ** This rich color scheme offers plenty of possibilities for variation.
   ** <p>
   ** Tetradic color schemes works best if you let one color be dominant.
   ** <p>
   ** Attention should be payed also to the balance between warm and cool colors
   ** in the design.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on tetradic colors.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] tetradicScheme(final float[] base) {
    return new int[]{0, 0, 0, 0};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   squareScheme
  /**
   ** The square color scheme is similar to the tetraedic, but with all four
   ** colors spaced evenly around the color circle.
   ** <p>
   ** Square color schemes works best if you let one color be dominant.
   ** <p>
   ** Attention should be payed also to the balance between warm and cool colors
   ** in the design.
   **
   ** @param  base               the base color components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the collection on tetradic colors.
   **                            <br>
   **                            Possible object is array of <code>int</code>.
   */
  public static int[] squareScheme(final float[] base) {
    float[] cb1 = { wheel(base[0], 15), base[1],         base[2] };
    return new int[]{0, 0, 0, 0};
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wheel
  /**
   ** The color wheel or color circle is the basic tool for combining colors.
   ** The first circular color diagram was designed by Sir Isaac Newton in 1666.
   ** <p>
   ** The color wheel is designed so that virtually any colors you pick from it
   ** will look good together. Over the years, many variations of the basic
   ** design have been made, but the most common version is a wheel of 12
   ** colors based on the RYB (or artistic) color model.
   ** <p>
   ** Traditionally, there are a number of color combinations that are
   ** considered especially pleasing. These are called color harmonies or color
   ** chords and they consist of two or more colors with a fixed relation in the
   ** color wheel. 
   **
   ** @param  radian             the radian value.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  degree             the value in degrees to convert.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the radian value.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float wheel(float radian, float degree) {
    radian += radian(degree);
    if (radian > 1) {
      return radian - 1;
    }
    else if (radian < 0) {
      return -1 * radian;
    }
    else {
      return radian;
    }
/*    
    degree += add;
    if (degree > 360) {
      float offset = degree - 360;
      return offset;
    }
    else if (degree < 0) {
      return -1 * degree;
    }
    else {
      return degree;
    }
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complementaryColor
  /**
   ** This method will create a color instance that is the exact opposite color
   ** from another color on the color wheel.
   ** <br>
   ** The same saturation and brightness are preserved, just the hue is changed.
   **
   ** @param  color              the color to complement.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the complementary color.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int complementaryColor(final int color) {
    final float[] hsb = new float[3];
    rgb2hsb(color, hsb);
    float newH = wheel(180, hsb[0]);
    hsb[0] = newH;
    return hsb2rgb(hsb);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   highcontrastColor
  /**
   ** This method will create a color instance that is in high contrast
   ** from another color on the color wheel.
   **
   ** @param  color              the color to contrast.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the high contrast color.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int highcontrastColor(final int color) {
    return ~color;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb2hsb
  /**
   ** Convert the given color int value to its HSB components.
   ** <br>
   ** hsb[0] is Hue [0 .. 360)
   ** hsb[1] is Saturation [0...1]
   ** hsb[2] is Brideness [0...1]
   **
   ** @param  color              the the color int value to convert.
   **                            <br>
   **                            The alpha component is ignored.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @param  hsb                the 3 element array which holds the resulting
   **                            HSB components.
   **                            <br>
   **                            Possible object is array of <code>float</code>.
   */
  public static void rgb2hsb(final int color, final float hsb[]) {
    rgb2hsb((color >> 16) & 0xff, (color >> 8) & 0xff, color & 0xff, hsb);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rgb2hsb
  /**
   ** Convert the given color componentto its HSB components.
   ** <br>
   ** hsb[0] is Hue [0 .. 360)
   ** hsb[1] is Saturation [0...1]
   ** hsb[2] is Brideness [0...1]
   **
   ** @param  r                  the red color component value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  g                  the green color component value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  b                  the blue color component value to convert.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @param  hsb                the 3 element array which holds the resulting
   **                            HSB components.
   **                            <br>
   **                            Possible object is array of <code>float</code>.
   */
  public static void rgb2hsb(int r, int g, int b, final float[] hsb) {
    if (hsb.length < 3)
      throw new RuntimeException("3 components required for hsb");

    float min = Utility.minimum(Utility.minimum(r, g), b);
    float max = Utility.maximum(Utility.maximum(r, g), b);
    float d   = max - min;
    
    // Brightness
    hsb[2] = ((float)max) / 255.0f;
    // Saturation
    hsb[1] = (max != 0) ?  d / max : 0.0f;
    // Hue
    hsb[0] = 0.0f;
    if (hsb[1] != 0) {
      float rc = ((float)(max - r)) / d;
      float gc = ((float)(max - g)) / d;
      float bc = ((float)(max - b)) / d;
      // between yellow & magenta
      if (r == max)
        hsb[0] = bc - gc;
      // between cyan & yellow
      else if (g == max)
        hsb[0] = 2.0f + rc - bc;
      // between magenta & cyan
      else
        hsb[0] = 4.0f + gc - rc;
      hsb[0] = hsb[0] / 6.0f;
      if (hsb[0] < 0)
        hsb[0] = hsb[0] + 1.0f;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hsb2rgb
  /**
   ** Converts a HSB components to an ARGB color.
   ** <br>
   ** Alpha set to 0xff.
   ** <br>
   ** hsb[0] is Hue [0 .. 1)
   ** hsb[1] is Saturation [0...1]
   ** hsb[2] is Brideness [0...1]
   ** <br>
   ** If <code>hsb</code> values are out of range, they are pinned.
   **
   ** @param  hsb                the array which holds the 3 HSB input
   **                            components.
   **                            <br>
   **                            Allowed object is array of <code>float</code>.
   **
   ** @return                    the resulting argb color.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hsb2rgb(final float[] hsb) {
    return hsb2rgb(hsb[0], hsb[1], hsb[2]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hsb2rgb
  /**
   ** Converts a HSB components to an ARGB color.
   ** <br>
   ** Alpha set to 0xff.
   ** If hsb values are out of range, they are pinned.
   **
   ** @param  h                  the <code>Hue</code> component.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  s                  the <code>Saturation</code> component.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  b                  the <code>Brightness</code> component.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the resulting argb color.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hsb2rgb(final float h, final float s, final float b) {
    int R = 0;
    int G = 0;
    int B = 0;
    if (s == 0) {
      R = (int)(b * 255.0f + 0.5f);
      G = (int)(b * 255.0f + 0.5f);
      B = (int)(b * 255.0f + 0.5f);
    }
    else {
      final float hf = (h - (int)h) * 6.0f;
      final int   i  = (int)hf;
      final float f  = hf - i;
      final float p  = b * (1.0f - s);
      final float q  = b * (1.0f - s * f);
      final float t  = b * (1.0f - (s * (1.0f - f)));
      switch (i) {
        // red is the dominant color
        case 0 : R = (int)(b * 255.0f + 0.5f); G = (int)(t * 255.0f + 0.5f); B = (int)(p * 255.0f + 0.5f); break;
        case 5 : R = (int)(b * 255.0f + 0.5f); G = (int)(p * 255.0f + 0.5f); B = (int)(q * 255.0f + 0.5f); break;
        // green is the dominant color
        case 1 : R = (int)(q * 255.0f + 0.5f); G = (int)(b * 255.0f + 0.5f); B = (int)(p * 255.0f + 0.5f); break;
        case 2 : R = (int)(p * 255.0f + 0.5f); G = (int)(b * 255.0f + 0.5f); B = (int)(t * 255.0f + 0.5f); break;
        // blue is the dominant color
        case 3 : R = (int)(p * 255.0f + 0.5f); G = (int)(q * 255.0f + 0.5f); B = (int)(b * 255.0f + 0.5f); break;
        case 4 : R = (int)(t * 255.0f + 0.5f); G = (int)(p * 255.0f + 0.5f); B = (int)(b * 255.0f + 0.5f); break;
      }
    }
    return 0xff000000 | (R << 16) | (G << 8) | B;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   radian
  /**
   ** Convert degree to radian.
   **
   ** @param  value              the value in degrees to convert.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the radian value.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  private static double radian(final double degree) {
    return degree * Math.PI / 180;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   degree
  /**
   ** Convert radian to degree.
   **
   ** @param  value              the value in radians to convert.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the degree value.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  private static double degree(final double radian) {
    return radian * 180 / Math.PI;
  }
}