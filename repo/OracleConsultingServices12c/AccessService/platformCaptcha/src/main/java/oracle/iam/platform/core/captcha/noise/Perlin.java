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

    File        :   Perlin.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Perlin.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.noise;

////////////////////////////////////////////////////////////////////////////////
// abstract class Perlin
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** <code>Perlin</code> implements the type of gradient noise developed by
 ** Ken Perlin.
 ** <p>
 ** <code>Perlin</code> is a procedural texture primitive, a type of
 ** gradient noise used by visual effects artists to increase the appearance of
 ** realism in computer graphics. The function has a pseudo-random appearance,
 ** yet all of its visual details are the same size. This property allows it to
 ** be readily controllable; multiple scaled copies of <code>PerlinNoise</code>
 ** can be inserted into mathematical expressions to create a great variety of
 ** procedural textures. Synthetic textures using <code>PerlinNoise</code> are
 ** often used in CGI to make computer-generated visual elements – such as
 ** object surfaces, fire, smoke, or clouds – appear more natural, by imitating
 ** the controlled random appearance of textures in nature.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Perlin {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final int p[] = new int[512];
  static final int o[] = {
    151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225
  , 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247
  , 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177
  , 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165
  , 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211
  , 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63
  , 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135
  , 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226
  , 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59
  , 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152
  , 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253
  , 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228
  , 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235
  , 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176
  , 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24
  , 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180, 151, 160, 137, 91, 90
  , 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142
  , 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62
  , 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174
  , 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77
  , 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55
  , 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76
  , 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100
  , 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147
  , 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28
  , 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101
  , 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232
  , 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12
  , 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31
  , 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254
  , 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66
  , 215, 61, 156, 180
  };

  static {
		for (int i = 0; i < 256; i++) {
      p[      i] = o[i];
			p[256 + i] = o[i];
		}
	}

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Perlin</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Perlin() {
    // do not instantiate
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noise2D
  /**
   ** see also https://rosettacode.org/wiki/Perlin_noise#Java.
   **
   ** @param x                   ...
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param y                   ...
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    ...
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double noise2D(double x, double y) {
    final double fx = Math.floor(x);
    final double fy = Math.floor(y);
    x -= fx;
    y -= fy;
    final double u  = fade(x);
    final double v  = fade(y);
    final int    X  = ((int)fx) & 255;
    final int    Y  = ((int)fy) & 255;
    int A = p[X];
    int B = p[X + 1];
    double g1 = gradient(p[A + Y], x, y);
    double g2 = gradient(p[B + Y], x - 1, y);
    double g3 = gradient(p[A + Y + 1], x, y - 1);
    double g4 = gradient(p[B + Y + 1], x - 1, y - 1);
    return lerp(v, lerp(u, g1, g2), lerp(u, g3, g4));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noise3D
  /**
   ** see also https://rosettacode.org/wiki/Perlin_noise#Java.
   **
   ** @param x                   ...
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param y                   ...
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param z                   ...
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    ...
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double noise3D(double x, double y, double z) {
    final double fx = Math.floor(x);
    final double fy = Math.floor(y);
    final double fz = Math.floor(y);
    x -= fx;
    y -= fy;
    z -= fz;
    final double u  = fade(x);
    final double v  = fade(y);
    final double w  = fade(z);

    final int    X  = ((int)fx) & 255;
    final int    Y  = ((int)fy) & 255;
    final int    Z  = ((int)fz) & 255;

    // hash coordinates of the 8 cube corners ...
    int A  = p[X    ] + Y;
    int AA = p[A]     + Z;
    int AB = p[A + 1] + Z;
    int B  = p[X + 1] + Y;
    int BA = p[B    ] + Z;
    int BB = p[B + 1] + Z;
    // ... and add blended results from 8 corners of cube
    return lerp(w, lerp(v, lerp( u
                               , gradient(p[AA    ], x    , y    , z)
                               , gradient(p[BA    ], x - 1, y    , z)
                               )
                         , lerp( u
                               , gradient(p[AB    ], x    , y - 1, z)
                               , gradient(p[BB    ], x - 1, y - 1, z)
                               )
                       )
                 , lerp(v, lerp( u
                               , gradient(p[AA + 1], x    , y    , z - 1)
                               , gradient(p[BA + 1], x - 1, y    , z - 1)
                               )
                         , lerp( u
                               , gradient(p[AB + 1], x    , y - 1, z - 1 )
                               , gradient(p[BB + 1], x - 1, y - 1, z - 1 )
                               )
                       )
    );

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lerp
  /**
   ** Returns an interpolation between two inputs (a, b) for a parameter (t) in
   ** the closed unit interval [0, 1].
   ** <p>
   ** This lerp function is commonly used for alpha blending (the parameter
   ** <code>t</code> is the "alpha value"), and the formula may be extended to
   ** blend multiple components of a vector (such as spatial x, y, z axes or r,
   ** g, b colour components) in parallel.
   ** <p>
   ** In mathematics, linear interpolation is a method of curve fitting using
   ** linear polynomials to construct new data points within the range of a
   ** discrete set of known data points.
   ** <p>
   ** Imprecise method, which does not guarantee a = b when t = 1, due to
   ** floating-point arithmetic error. This method is monotonic.
   ** <br>
   ** This form may be used when the hardware has a native fused multiply-add
   ** instruction.
   **
   ** @param  amount             the alpha value.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  a                  the first point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  b                  the second point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the interpolation between <code>a</code> and
   **                            <code>b</code> for a parameter <code>t</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double lerp(final double amount, final double a, final double b) {
    return a + (b - a) * amount;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fade
  private static double fade(final double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gradient
  private static double gradient(final int hash, final double x, final double y) {
    // convert low 4 bits of hash code
    final int    h = hash & 15;
    final double u = (h < 8) ? x : y;
    final double v = (h < 4) ? y : x;
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   gradient
  private static double gradient(final int hash, final double x, final double y, final double z) {
    // convert low 4 bits of hash code into 12 gradient directions
    int    h = hash & 15;
    double u = h < 8 ? x : y;
    double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
    return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
  }
}