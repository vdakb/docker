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

    File        :   Spline.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Spline.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.platform.core.captcha.noise;

////////////////////////////////////////////////////////////////////////////////
// abstract class Spline
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** Performs spline interpolation given a set of control points.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Spline {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Spline</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Spline() {
    // do not instantiate
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   catmullRom
  /**
   ** In computer graphics, Catmull–Rom splines are frequently used to get
   ** smooth interpolated motion between key frames. For example, most camera
   ** path animations generated from discrete key-frames are handled using
   ** Catmull–Rom splines. They are popular mainly for being relatively easy to
   ** compute, guaranteeing that each key frame position will be hit exactly,
   ** and also guaranteeing that the tangents of the generated curve are
   ** continuous over multiple segments.
   ** <p>
   ** The curve is named after
   ** <a href="https://en.wikipedia.org/wiki/Edwin_Catmull">Edwin Catmull</a>
   ** and <a href="https://en.wikipedia.org/wiki/Raphael_Rom">Raphael Rom</a>.
   ** <p>
   ** The principal advantage of this technique is that the points along the
   ** original set of points also make up the control points for the spline
   ** curve.
   ** <br>
   ** Two additional points are required on either end of the curve. The uniform
   ** Catmull–Rom implementation can produce loops and self-intersections. The
   ** chordal and centripetal Catmull–Rom implementations solve this problem,
   ** but use a slightly different calculation.
   **
   ** @param  x1                 the 1st source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  x2                 the 2nd source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  x3                 the 3rd source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  x4                 the 4th source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  t                  the interpolated position
   **                            (<code>0 &lt;= t &lt;= 1</code>) on the spline.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the result of the CatmullRom spline
   **                            interpolation.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double catmullRom(final double x1, final double x2, final double x3, final double x4, final double t) {
    final double a1 = (x3 - x1) / 2;
    final double a2 = (x4 - x2) / 2;
    return hermite(x2, a1, x3, a2, t);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hermite
  /**
   ** In the mathematical subfield of numerical analysis, a Hermite spline is a
   ** spline curve where each polynomial of the spline is in Hermite form.
   ** <p>
   ** Hermite polynomials were defined by
   ** <a href="https://en.wikipedia.org/wiki/Pierre-Simon_Laplace">Pierre-Simon Laplace</a>
   ** in 1810, though in scarcely recognizable form, and studied in detail by
   ** <a href="https://en.wikipedia.org/wiki/Pafnuty_Chebyshev">Pafnuty Chebyshev</a>
   ** in 1859. Chebyshev's work was overlooked, and they were named later after
   ** <a href="https://en.wikipedia.org/wiki/Charles_Hermite">Charles Hermite</a>,
   ** who wrote on the polynomials in 1864, describing them as new. They were
   ** consequently not new, although Hermite was the first to define the
   ** multidimensional polynomials in his later 1865 publications.
   **
   ** @param  x1                 the first source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  a1                 the first source tangent point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  x2                 the second source control point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  a2                 the first source tangent point.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  t                  the interpolated position
   **                            (<code>0 &lt;= t &lt;= 1</code>) on the spline.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the result of the Hermite spline interpolation.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double hermite(final double x1, final double a1, final double x2, final double a2, final double t) {
    final double t2 = t * t;
    final double t3 = t2 * t;
    final double b  = -a2 - 2.0 * a1 - 3.0 * x1 + 3.0 * x2;
    final double a  = a2 + a1 + 2.0 * x1 - 2.0 * x2;
    return a * t3 + b * t2 + a1 * t + x1;
  }
}