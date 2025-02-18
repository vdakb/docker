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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Common Shared Utility

    File        :   RangeTest.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RangeTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.entity;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.entity.Range;

////////////////////////////////////////////////////////////////////////////////
// class RangeTest
// ~~~~~ ~~~~~~~~~
/**
 ** Example use of {@link Range}s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RangeTest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  protected <T extends Comparable<T>> Range<T> newRange(final T min, final T max) {
    return Range.immutable(min, max);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>RangeTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RangeTest() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = { RangeTest.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testWithMinimim
  /**
   ** Test for copy minimum limit of a {@link Range}.
   */
  @Test
  public void testWithMinimim() {
    Range<Integer> range    = newRange(1, 10);
    Range<Integer> newRange = range.minimum(0);

    Assert.assertEquals((Integer)1, range.minimum());
    Assert.assertNotEquals(range, newRange);
    Assert.assertEquals(range.maximum(),          newRange.maximum());
    Assert.assertEquals(range.minimumInclusive(), newRange.minimumInclusive());
    Assert.assertEquals(range.maximumInclusive(), newRange.maximumInclusive());
    Assert.assertEquals((Integer)0,               newRange.minimum());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testWithMaximum
  /**
   ** Test for copy maximum limit of a {@link Range}.
   */
  @Test
  public void testWithMaximum() {
    Range<Integer> range = newRange(1, 10);

    Range<Integer> newRange = range.maximum(100);

    Assert.assertEquals((Integer)10, range.maximum());
    Assert.assertNotEquals(range, newRange);

    Assert.assertEquals(range.minimum(),          newRange.minimum());
    Assert.assertEquals(range.minimumInclusive(), newRange.minimumInclusive());
    Assert.assertEquals(range.maximumInclusive(), newRange.maximumInclusive());
    Assert.assertEquals((Integer)100,             newRange.maximum());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testWithMinInclusive
  /**
   ** Test for copy inclusive minimum limit of a {@link Range}.
   */
  @Test
  public void testWithMinInclusive() {
    Range<Integer> range = newRange(1, 10).minimumInclusive(true);
    Assert.assertTrue(range.minimumInclusive());

    Range<Integer> newRange = range.minimumInclusive(false);
    Assert.assertTrue(range.minimumInclusive());
    Assert.assertFalse(newRange.minimumInclusive());

    Assert.assertNotEquals(range, newRange);
    Assert.assertEquals(range.minimum(), newRange.minimum());
    Assert.assertEquals(range.maximum(), newRange.maximum());
    Assert.assertEquals(range.maximumInclusive(), newRange.maximumInclusive());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testWithMaxInclusive
  /**
   ** Test for copy inclusive maxnimum limit of a {@link Range}.
   */
  @Test
  public void testWithMaxInclusive() {
    final Range<Integer> range = newRange(1, 10).maximumInclusive(true);
    Assert.assertTrue(range.maximumInclusive());

    final Range<Integer> newRange = range.maximumInclusive(false);
    Assert.assertTrue(range.maximumInclusive());

    Assert.assertFalse(newRange.maximumInclusive());
    Assert.assertNotEquals(range,                 newRange);
    Assert.assertEquals(range.minimum(),          newRange.minimum());
    Assert.assertEquals(range.maximum(),          newRange.maximum());
    Assert.assertEquals(range.minimumInclusive(), newRange.minimumInclusive());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testContains
  /**
   ** Test for {@link Range} containing values.
   */
  @Test
  public void testContains() {
    Range<Integer> range = newRange(1, 10).minimumInclusive(true).maximumInclusive(true);

    Assert.assertFalse(range.contains(0));
    Assert.assertFalse(range.contains(11));
    Assert.assertTrue(range.contains(1));
    Assert.assertTrue(range.contains(10));
    Assert.assertTrue(range.contains(5));

    range = range.minimumInclusive(false);
    Assert.assertFalse(range.contains(1));

    range = range.maximumInclusive(false);
    Assert.assertFalse(range.contains(10));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testIntersects
  /**
   ** Test for {@link Range} intersections.
   */
  @Test
  public void testIntersects() {
    Range<Integer> range1 = newRange(1, 10).minimumInclusive(true).maximumInclusive(false);
    Range<Integer> range2 = newRange(10, 20).minimumInclusive(true).maximumInclusive(false);

    Assert.assertTrue(range1.intersects(range1));
    Assert.assertTrue(range2.intersects(range2));

    Assert.assertFalse(range1.intersects(range2));
    Assert.assertFalse(range2.intersects(range1));

    range1 = range1.maximumInclusive(true);

    Assert.assertTrue(range1.intersects(range2));
    Assert.assertTrue(range2.intersects(range1));

    range2 = range2.minimumInclusive(false);

    Assert.assertFalse(range1.intersects(range2));
    Assert.assertFalse(range2.intersects(range1));

    range1 = range1.maximumInclusive(false);

    Assert.assertFalse(range1.intersects(range2));
    Assert.assertFalse(range2.intersects(range1));
  }
}