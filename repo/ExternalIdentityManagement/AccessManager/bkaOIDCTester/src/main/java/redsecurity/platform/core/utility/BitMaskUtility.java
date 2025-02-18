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

    File        :   BitMaskUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    BitMaskUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

////////////////////////////////////////////////////////////////////////////////
// abstract class BitMaskUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Miscellaneous bit mask utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class BitMaskUtility {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>BitMaskUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new BitMaskUtility()" and enforces use of the public method below.
   */
  public BitMaskUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clear an invariant byte bit mask.
   **
   ** @param  value              the value providing the bits to clear.
   ** @param  mask               the bit mask to clear.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code>.
   */
  public static byte clear(final byte value, final byte mask) {
    return set(value, mask, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clear an invariant integer bit mask.
   **
   ** @param  value              the value providing the bits to clear.
   ** @param  mask               the bit mask to clear.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code>.
   */
  public static int clear(final int value, final int mask) {
    return set(value, mask, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clear an invariant long bit mask.
   **
   ** @param  value              the value providing the bits to clear.
   ** @param  mask               the bit mask to clear.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code>.
   */
  public static long clear(final long value, final long mask) {
    return set(value, mask, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns the bit corresponding to the <code>mask</code> if it's set or
   ** cleared accordingly to <code>set</code>.
   **
   ** @param  value              the byte bit mask are set or cleared.
   ** @param  mask               the byte bit mask tested.
   ** @param  set                determines whether the bits are set or cleared.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code> set (if
   **                            <code>set</code> is <code>true</code>) or
   **                            cleared (if <code>set</code> is
   **                            <code>false</code>).
   */
  public static byte set(final byte value, final byte mask, final boolean set) {
    assertMask(mask);
    return (byte)(set ? value | mask : value & ~mask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns the bit corresponding to the <code>mask</code> if it's set or
   ** cleared accordingly to <code>set</code>.
   **
   ** @param  value              the int bit mask are set or cleared.
   ** @param  mask               the int bit mask tested.
   ** @param  set                determines whether the bits are set or cleared.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code> set (if
   **                            <code>set</code> is <code>true</code>) or
   **                            cleared (if <code>set</code> is
   **                            <code>false</code>).
   */
  public static int set(final int value, final int mask, final boolean set) {
    assertMask(mask);
    return set ? value | mask : value & ~mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns the bit corresponding to the <code>mask</code> if it's set or
   ** cleared accordingly to <code>set</code>.
   **
   ** @param  value              the long bit mask are set or cleared.
   ** @param  mask               the long bit mask tested.
   ** @param  set                determines whether the bits are set or cleared.
   **
   ** @return                    the <code>value</code> with the bit
   **                            corresponding to the <code>mask</code> set (if
   **                            <code>set</code> is <code>true</code>) or
   **                            cleared (if <code>set</code> is
   **                            <code>false</code>).
   */
  public static long set(final long value, final long mask, final boolean set) {
    assertMask(mask);
    return set ? value | mask : value & ~mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Wheter the byte bits corresponding to <code>mask</code> are set.
   **
   ** @param  value              the byte bit mask are set.
   ** @param  mask               the byte bit mask tested.
   **
   ** @return                    <code>true</code> if the bits corresponding to
   **                            <code>mask</code> are set;
   **                            <code>false</code>) otherwise.
   */
  public static boolean match(final byte value, final byte mask) {
    assertMask(mask);
    return (value & mask) == mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Wheter the int bits corresponding to <code>mask</code> are set.
   **
   ** @param  value              the int bit mask are set.
   ** @param  mask               the int bit mask tested.
   **
   ** @return                    <code>true</code> if the bits corresponding to
   **                            <code>mask</code> are set;
   **                            <code>false</code>) otherwise.
   */
  public static boolean match(final int value, final int mask) {
    assertMask(mask);
    return (value & mask) == mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   match
  /**
   ** Wheter long the bits corresponding to <code>mask</code> are set.
   **
   ** @param  value              the long bit mask are set.
   ** @param  mask               the long bit mask tested.
   **
   ** @return                    <code>true</code> if the bits corresponding to
   **                            <code>mask</code> are set;
   **                            <code>false</code>) otherwise.
   */
  public static boolean match(final long value, final long mask) {
    assertMask(mask);
    return (value & mask) == mask;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertMask
  /**
   ** Verify the correctness of an invariant byte bit mask.
   **
   ** @param  mask               the bit mask to verify.
   */
  private static void assertMask(final byte mask) {
    assertMask(mask & 0xffL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertMask
  /**
   ** Verify the correctness of an invariant integer bit mask.
   **
   ** @param  mask               the bit mask to verify.
   */
  private static void assertMask(final int mask) {
    assert(mask & mask - 1) == 0 : invalid(mask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertMask
  /**
   ** Verify the correctness of an invariant long bit mask.
   **
   ** @param  mask               the bit mask to verify.
   */
  private static void assertMask(final long mask) {
    assert(mask & mask - 1) == 0 : invalid(mask);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalid
  /**
   ** Returns the error message indicating the bit mask constraint violation.
   **
   ** @param  mask               the bit mask to display in the error message.
   **
   ** @return                    the composed error message indicating the bit
   **                            mask constraint violation.
   */
  private static String invalid(final long mask) {
    return "Mask must have only one bit set, but got: " + Long.toBinaryString(mask);
  }
}