/*
                         Copyright © 2023 Red.Security

    Licensed under the MIT License (the "License"); you may not use this file
    except in compliance with the License. You may obtain a copy of the License
    at

                      https://opensource.org/licenses/MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

    ----------------------------------------------------------------------------

    System      :   Platform Service Extension
    Subsystem   :   Common Shared Utility Library

    File        :   Comparison.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Comparison.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.io.Serializable;

import java.util.Arrays;

import java.util.Collection;

////////////////////////////////////////////////////////////////////////////////
// abstract class Comparison
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Miscellaneous comparison utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Comparison {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Comparison</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Comparison()" and enforces use of the public method below.
   */
  private Comparison() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  public static <T extends Serializable> boolean equal(final T lhs, final T rhs) {
    if (lhs == rhs)
      return true;
    if (lhs == null || rhs == null) {
      return false;
    }
    if (lhs instanceof Object[] && rhs instanceof Object[]) {
      Object[] arr1 = (Object[])lhs;
      Object[] arr2 = (Object[])rhs;
      return Arrays.equals(arr1, arr2);
    }
    if (lhs instanceof CharSequence && rhs instanceof CharSequence) {
      return equal((CharSequence)lhs, (CharSequence)rhs, true);
    }
    return lhs.equals(rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equal(final CharSequence lhs, final CharSequence rhs) {
    return equal(lhs, rhs, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by the left hand side
   ** (<code>lhs</code>) argument string is considered equal ignoring case to
   ** the character sequence represented by the right hand side
   ** (<code>rhs</code>) argument string if both are of the same length and
   ** corresponding characters in the two strings are equal ignoring case.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   ** @param  caseSensitive      <code>true</code> if the comparison takes
   **                            case sensitivity in account; otherwise
   **                            <code>false</code>.
   **
   ** @return                    <code>true</code>, if <code>rhs</code>
   **                            represents an equivalent <code>String</code>
   **                            ignoring case as <code>lhs</code>;
   **                            <code>false</code> otherwise.
   */
  public static boolean equal(final CharSequence lhs, final CharSequence rhs, final boolean caseSensitive) {
    return caseSensitive ? StringUtility.equal(lhs, rhs) : StringUtility.equalIgnoreCase(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings. The character sequence represented by the left hand side
   ** (<code>lhs</code>) argument string is considered equal ignoring case to
   ** the character sequence represented by the right hand side
   ** (<code>rhs</code>) argument string if both are of the same length and
   ** corresponding characters in the two strings are equal ignoring case.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   ** @param  caseSensitive      <code>true</code> if the comparison takes
   **                            case sensitivity in account; otherwise
   **                            <code>false</code>.
   **
   ** @return                    <code>true</code>, if <code>rhs</code>
   **                            represents an equivalent <code>String</code>
   **                            ignoring case as <code>lhs</code>;
   **                            <code>false</code> otherwise.
   */
  public static boolean equal(final String lhs, final String rhs, boolean caseSensitive) {
    return caseSensitive ? equal(lhs, rhs) : StringUtility.equalIgnoreCase(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Compares two strings if they are lexicographically identically.
   **
   ** @param  lhs                the <code>String</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>String</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    <code>true</code> if the strings are
   **                            lexicographically identically; otherwise
   **                            <code>false</code>.
   */
  public static boolean equal(final String lhs, final String rhs) {
    return StringUtility.equal(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Returns if both {@link Collection Collections} contains the same elements,
   ** in the same quantities, regardless of order and collection type.
   ** <p>
   ** Empty collections and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the expected class type of the elements in both
   **                            collections.
   ** @param  lhs                the left-hand-side {@link Collection}.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  rhs                the right-hand-side {@link Collection}.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    <code>true</code> if the
   **                            {@link Collection Collections} containing the
   **                            same elements.
   */
 public static <T extends Serializable> boolean equal(final Collection<T> lhs, final Collection<T> rhs) {
    return CollectionUtility.equal(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Returns if both arrays contains the same elements, in the same quantities,
   ** regardless of order and collection type.
   ** <p>
   ** Empty arrays and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the expected class type of the array elements.
   ** @param  lhs                the left-hand-side array.
   **                            <br>
   **                            May be <code>null</code>.
   ** @param  rhs                the right-hand-side array.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    <code>true</code> if the arrays containing the
   **                            same elements.
   */
  public static <T extends Serializable> boolean equal(final T[] lhs, final T[] rhs) {
     return CollectionUtility.equal(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two <code>boolean</code> values.
   ** <br>
   ** The value returned is identical to what would be returned by:
   ** <pre>
   **   Boolean.compare(lhs, rhs)
   ** </pre>
   **
   ** @param  lhs                the <code>boolean</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>boolean</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    a negative number if lhs is less, zero if
   **                            equal and a positive number if lhs greater as
   **                            <code>rhs</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  public static int compare(final boolean lhs, final boolean rhs) {
    return Boolean.compare(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two <code>int</code> values.
   ** <br>
   ** The value returned is identical to what would be returned by:
   ** <pre>
   **   Integer.compare(lhs, rhs)
   ** </pre>
   **
   ** @param  lhs                the <code>int</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>int</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    a negative number if lhs is less, zero if
   **                            equal and a positive number if lhs greater as
   **                            <code>rhs</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  public static int compare(final int lhs, final int rhs) {
    return Integer.compare(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two <code>long</code> values.
   ** <br>
   ** The value returned is identical to what would be returned by:
   ** <pre>
   **   Long.compare(lhs, rhs)
   ** </pre>
   **
   ** @param  lhs                the <code>long</code> to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>long</code> to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    a negative number if lhs is less, zero if
   **                            equal and a positive number if lhs greater as
   **                            <code>rhs</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  public static int compare(final long lhs, final long rhs) {
    return Long.compare(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares two <code>byte</code> arrays.
   ** <br>
   ** The value returned is identical to what would be returned by:
   ** <pre>
   **   Long.compare(lhs, rhs)
   ** </pre>
   **
   ** @param  lhs                the <code>byte</code> array to compare with
   **                            <code>rhs</code>.
   ** @param  rhs                the <code>byte</code> array to compare with
   **                            <code>lhs</code>.
   **
   ** @return                    a negative number if lhs is less, zero if
   **                            equal and a positive number if lhs greater as
   **                            <code>rhs</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  public static int compare(final byte[] lhs, final byte[] rhs) {
    //noinspection ArrayEquality
    if (lhs == rhs)
      return 0;
    if (lhs == null)
      return 1;
    if (rhs == null)
      return -1;

    if (lhs.length > rhs.length)
      return 1;
    if (lhs.length < rhs.length)
      return -1;

    for (int i = 0; i < lhs.length; i++) {
      if (lhs[i] > rhs[i])
        return 1;
      else if (lhs[i] < rhs[i])
        return -1;
    }
    return 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compare
  /**
   ** Compares object <code>lhs</code> with the specified object
   ** <code>rhs</code> for order. Returns a negative integer, zero, or a
   ** positive integer as <code>lhs</code> is less than, equal to, or greater
   ** than <code>rhs</code>.
   ** <p>
   ** The implementor must ensure
   ** <code>sgn(lhs.compareTo(rhs)) == -sgn(rhs.compareTo(lhs))</code> for all
   ** <code>lhs</code> and <code>rhs</code>.  (This implies that
   ** <code>lhs.compareTo(rhs)</code> must throw an exception if
   ** <code>rhs.compareTo(lhs)</code> throws an exception.)
   ** <p>
   ** The implementor must also ensure that the relation is transitive:
   ** <br>
   ** <code>(lhs.compareTo(rhs)&gt;0 &amp;&amp; rhs.compareTo(z)&gt;0)</code>
   ** implies <code>lhs.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally, the implementor must ensure that
   ** <code>lhs.compareTo(rhs) == 0</code> implies that
   ** <code>sgn(lhs.compareTo(z)) == sgn(rhs.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** It is strongly recommended, but <i>not</i> strictly required that
   ** <code>(lhs.compareTo(rhs) == 0) == (lhs.equals(rhs))</code>.  Generally
   ** speaking, any class that implements the <code>Comparable</code> interface
   ** and violates this condition should clearly indicate this fact. The
   ** recommended language is "Note: this class has a natural ordering that is
   ** inconsistent with equals."
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  <T>                the expected class type of the objects.
   ** @param  lhs                the object to compare with <code>rhs</code>.
   ** @param  rhs                the object to compare with <code>lhs</code>.
   **
   ** @return                    a negative number if lhs is less, zero if
   **                            equal and a positive number if lhs greater as
   **                            <code>rhs</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  public static <T extends Comparable<? super T>> int compare(final T lhs, final T rhs) {
    if (lhs == rhs)
      return 0;
    if (lhs == null)
      return -1;
    if (rhs == null)
      return 1;
    return lhs.compareTo(rhs);
  }
}
