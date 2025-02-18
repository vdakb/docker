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

    File        :   CollectorsTest.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CollectorsTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.stream;

import java.util.Optional;
import java.util.Iterator;

import java.util.stream.Stream;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.stream.Collectors;

////////////////////////////////////////////////////////////////////////////////
// class CollectorsTest
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Example use of {@link Collectors}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class CollectorsTest {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CollectorsTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public CollectorsTest() {
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
    final String[] parameter = { CollectorsTest.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testReversedStream
  /**
   ** Test for reversing stream.
   ** <p>
   ** Don'T worry this test takes a little bit longer.
   */
  @Test
  public void testReversedStream() {
    final Iterator<Integer> iterator = IntStream.rangeClosed(0, 10000000).boxed().parallel().collect(Collectors.reversedStream()).iterator();
    for (int i = 10000000; i >= 0; i--) {
      Assert.assertTrue(iterator.hasNext());
      Assert.assertEquals(i, iterator.next().intValue());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testReversedStream
  /**
   ** Test for find the last element in e stream.
   */
  @Test
  public void testFindLast() {
    Assert.assertEquals(Optional.of("a"), Stream.of("a").collect(Collectors.findLast()));
    Assert.assertEquals(Optional.of("b"), Stream.of("a", "b").collect(Collectors.findLast()));
    Assert.assertEquals(Optional.empty(), Stream.empty().collect(Collectors.findLast()));
  }
}