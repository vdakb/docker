package oracle.iam.platform.captcha.core.math;

////////////////////////////////////////////////////////////////////////////////
// abstract class Utility
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** A class that contains utility methods related to numbers.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Utility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Utility</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Utility() {
    // do not instantiate
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolute
  /**
   ** Returns the absolute value of an <code>int</code> value.
   ** <p>
   ** If the argument is not negative, the argument is returned. If the argument
   ** is negative, the negation of the argument is returned.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** if the argument is equal to the value of {@link Integer#MIN_VALUE}, the
   ** most negative representable <code>int</code> value, the result is that
   ** same value, which is negative.
   **
   ** @param  value              the argument whose absolute value is to be
   **                            determined.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the absolute value of the argument.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int absolute(final int value) {
    // default implementation delegates to Math
    return Math.abs(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolute
  /**
   ** Returns the absolute value of an <code>long</code> value.
   ** <p>
   ** If the argument is not negative, the argument is returned. If the argument
   ** is negative, the negation of the argument is returned.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** if the argument is equal to the value of {@link Long#MIN_VALUE}, the
   ** most negative representable <code>long</code> value, the result is that
   ** same value, which is negative.
   **
   ** @param  value              the argument whose absolute value is to be
   **                            determined.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the absolute value of the argument.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public static long absolute(final long value) {
    // default implementation delegates to Math
    return Math.abs(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolute
  /**
   ** Returns the absolute value of an <code>float</code> value.
   ** <p>
   ** If the argument is not negative, the argument is returned. If the argument
   ** is negative, the negation of the argument is returned.
   **
   ** @param  value              the argument whose absolute value is to be
   **                            determined.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the absolute value of the argument.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float absolute(final float value) {
    // default implementation delegates to Math
    return Math.abs(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   absolute
  /**
   ** Returns the absolute value of an <code>double</code> value.
   ** <p>
   ** If the argument is not negative, the argument is returned. If the argument
   ** is negative, the negation of the argument is returned.
   **
   ** @param  value              the argument whose absolute value is to be
   **                            determined.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the absolute value of the argument.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double absolute(final double value) {
    // default implementation delegates to Math
    return Math.abs(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Returns the lesser of two <code>int</code> values. That is, the result is
   ** the argument closer to the value of {@link Integer#MIN_VALUE}.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int minimum(final int lhs, final int rhs) {
    // default implementation delegates to Math
    return Math.min(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Returns the lesser of two <code>long</code> values. That is, the result is
   ** the argument closer to the value of {@link Integer#MIN_VALUE}.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public static long minimum(final long lhs, final long rhs) {
    // default implementation delegates to Math
    return Math.min(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Returns the lesser of two <code>float</code> values. That is, the result
   ** is the argument closer to negative infinity.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   ** <br>
   ** If either value is <code>NaN</code>, then the result is <code>NaN</code>.
   ** <br>
   ** Unlike the numerical comparison operators, this method considers negative
   ** zero to be strictly smaller than positive zero. If one argument is
   ** positive zero and the other negative zero, the result is positive zero.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float minimum(final float lhs, final float rhs) {
    // default implementation delegates to Math
    return Math.min(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Returns the lesser of two <code>double</code> values. That is, the result
   ** is the argument closer to negative infinity.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   ** <br>
   ** If either value is <code>NaN</code>, then the result is <code>NaN</code>.
   ** <br>
   ** Unlike the numerical comparison operators, this method considers negative
   ** zero to be strictly smaller than positive zero. If one argument is
   ** positive zero and the other negative zero, the result is positive zero.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double minimum(final double lhs, final double rhs) {
    // default implementation delegates to Math
    return Math.min(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Returns the greater of two <code>int</code> values. That is, the result is
   ** the argument closer to the value of {@link Integer#MAX_VALUE}.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int maximum(final int lhs, final int rhs) {
    // default implementation delegates to Math
    return Math.max(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Returns the greater of two <code>long</code> values. That is, the result
   ** is the argument closer to the value of {@link Long#MAX_VALUE}.
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public static long maximum(final long lhs, final long rhs) {
    // default implementation delegates to Math
    return Math.max(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Returns the greater of two <code>float</code> values. That is, the result
   ** is the argument closer to to positive infinity
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   ** <br>
   ** If either value is <code>NaN</code>, then the result is <code>NaN</code>.
   ** <br>
   ** Unlike the numerical comparison operators, this method considers negative
   ** zero to be strictly smaller than positive zero. If one argument is
   ** positive zero and the other negative zero, the result is positive zero.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float maximum(final float lhs, final float rhs) {
    // default implementation delegates to Math
    return Math.max(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Returns the greater of two <code>double</code> values. That is, the result
   ** is the argument closer to to positive infinity
   ** <br>
   ** If the arguments have the same value, the result is that same value.
   ** <br>
   ** If either value is <code>NaN</code>, then the result is <code>NaN</code>.
   ** <br>
   ** Unlike the numerical comparison operators, this method considers negative
   ** zero to be strictly smaller than positive zero. If one argument is
   ** positive zero and the other negative zero, the result is positive zero.
   **
   ** @param  lhs                the left-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  rhs                the right-hand-side value of the comparision.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the larger of <code>lhs</code> and
   **                            <code>rhs</code>.
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double maximum(final double lhs, final double rhs) {
    // default implementation delegates to Math
    return Math.max(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constrain
  /**
   ** Returns the value nearest to <code>value</code> which is within the
   ** closed range [low..high].
   ** <p>
   ** If <code>value</code> is within the range [low..high], <code>value</code>
   ** is returned unchanged. If <code>value</code> is less than
   ** <code>low</code>, <code>low</code> is returned, and if <code>value</code>
   ** is greater than <code>high</code>, <code>high</code> is returned.
   **
   ** @param  value              the value to constrain.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  low                the lower bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  high               the upper bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the value nearest to <code>value</code> which
   **                            is within the closed range [low..high].
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int constrain(final int value, final int low, final int high) {
    // default implementation delegates to Math
    return Math.max(low, Math.min(value, high));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constrain
  /**
   ** Returns the value nearest to <code>value</code> which is within the
   ** closed range [low..high].
   ** <p>
   ** If <code>value</code> is within the range [low..high], <code>value</code>
   ** is returned unchanged. If <code>value</code> is less than
   ** <code>low</code>, <code>low</code> is returned, and if <code>value</code>
   ** is greater than <code>high</code>, <code>high</code> is returned.
   **
   ** @param  value              the value to constrain.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  low                the lower bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  high               the upper bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the value nearest to <code>value</code> which
   **                            is within the closed range [low..high].
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public static long constrain(final long value, final long low, final long high) {
    // default implementation delegates to Math
    return Math.max(low, Math.min(value, high));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constrain
  /**
   ** Returns the value nearest to <code>value</code> which is within the
   ** closed range [low..high].
   ** <p>
   ** If <code>value</code> is within the range [low..high], <code>value</code>
   ** is returned unchanged. If <code>value</code> is less than
   ** <code>low</code>, <code>low</code> is returned, and if <code>value</code>
   ** is greater than <code>high</code>, <code>high</code> is returned.
   **
   ** @param  value              the value to constrain.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  low                the lower bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  high               the upper bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the value nearest to <code>value</code> which
   **                            is within the closed range [low..high].
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float constrain(final float value, final float low, final float high) {
    // default implementation delegates to Math
    return Math.max(low, Math.min(value, high));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   constrain
  /**
   ** Returns the value nearest to <code>value</code> which is within the
   ** closed range [low..high].
   ** <p>
   ** If <code>value</code> is within the range [low..high], <code>value</code>
   ** is returned unchanged. If <code>value</code> is less than
   ** <code>low</code>, <code>low</code> is returned, and if <code>value</code>
   ** is greater than <code>high</code>, <code>high</code> is returned.
   **
   ** @param  value              the value to constrain.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  low                the lower bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   ** @param  high               the upper bound (inclusive) of the range to.
   **                            <br>
   **                            Allowed object is <code>double</code>.
   **
   ** @return                    the value nearest to <code>value</code> which
   **                            is within the closed range [low..high].
   **                            <br>
   **                            Possible object is <code>double</code>.
   */
  public static double constrain(final double value, final double low, final double high) {
    // default implementation delegates to Math
    return Math.max(low, Math.min(value, high));
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
   ** @param  t                 the alpha value.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  a                  the first point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  b                  the second point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the interpolation between <code>a</code> and
   **                            <code>b</code> for a parameter <code>t</code>.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float lerp(final float t, final float a, final float b) {
    return a + (b - a) * t;
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
   ** @param  t                  the alpha value.
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
  public static double lerp(final double t, final double a, final double b) {
    return a + (b - a) * t;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lerpInvert
  /**
   ** Returns the interpolation scalar that satisfies the equation:
   ** <br>
   ** <code>t = </code>{@link #lerp}{@code (t, a, b)}
   ** <p>
   ** If <code>a == b</code>, then this function will return <code>0</code>.
   **
   ** @param  t                  the alpha value.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  a                  the first point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   ** @param  b                  the second point used for interpolation.
   **                            <br>
   **                            Allowed object is <code>float</code>.
   **
   ** @return                    the interpolation between <code>a</code> and
   **                            <code>b</code> for a parameter <code>t</code>.
   **                            <br>
   **                            Possible object is <code>float</code>.
   */
  public static float lerpInvert(final float t, final float a, final float b) {
    return a != b ? ((t - a) / (b - a)) : 0.0f;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lerpInvert
  /**
   ** Returns the interpolation scalar that satisfies the equation:
   ** <br>
   ** <code>t = </code>{@link #lerp}{@code (t, a, b)}
   ** <p>
   ** If <code>a == b</code>, then this function will return <code>0</code>.
   **
   ** @param  t                  the alpha value.
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
  public static double lerpInvert(final double t, final double a, final double b) {
    return a != b ? ((t - a) / (b - a)) : 0.0f;
  }
}
