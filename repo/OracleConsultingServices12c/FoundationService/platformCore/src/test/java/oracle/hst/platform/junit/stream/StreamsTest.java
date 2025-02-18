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

    File        :   StreamsTest.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    StreamsTest.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.junit.stream;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Comparator;

import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.JUnitCore;

import oracle.hst.platform.core.stream.Streams;

////////////////////////////////////////////////////////////////////////////////
// class StreamsTest
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Example use of streaming API.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class StreamsTest {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final BigDecimal TWO   = BigDecimal.valueOf(2L);
  private static final BigDecimal THREE = BigDecimal.valueOf(3L);

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class NonComparable
  // ~~~~~ ~~~~~~~~~~~~~
  /**
   ** The test subject
   */
  private static class NonComparable {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NonComparable</code> element with the specified
     ** interger <code>value</code>.
     **
     ** @param  value            the initial value.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private NonComparable(int value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the current value of this <code>NonComparable</code>.
     **
     ** @return                  the current value of this
     **                          <code>NonComparable</code>.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    public int value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>NonComparable</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>NonComparable</code>s may be different even though they contain
     ** the same set of names with the same values, but in a different order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      NonComparable that = (NonComparable)o;
      return value == that.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character " " (space).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return "NonComparable{" + "value=" + this.value + '}';
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: decrement
    /**
     ** Factory method to create a copy of this <code>NonComparable</code> where
     ** the value decremented by <code>1</code>.
     **
     ** @return                    a copy of this <code>NonComparable</code>
     **                            with zhe decrementedvalue.
     **                            <br>
     **                            Possible object is
     **                            <code>NonComparable</code>.
     */
    public NonComparable decrement() {
      return new NonComparable(this.value - 1);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>StreamsTest</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public StreamsTest() {
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
    final String[] parameter = { StreamsTest.class.getName() };
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRange
  /**
   ** Test for default {@link Range}.
   */
  @Test
  public void testRange() {
    Assert.assertEquals(Arrays.asList(BigDecimal.ONE, TWO), Streams.range(BigDecimal.ONE, THREE, i -> i.add(BigDecimal.ONE)).collect(Collectors.toList()));
    Assert.assertEquals(Arrays.asList(new NonComparable(2), new NonComparable(1), new NonComparable(0)), Streams.range(new NonComparable(2), new NonComparable(-1), NonComparable::decrement, Comparator.comparingInt(NonComparable::value).reversed()).collect(Collectors.toList()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testRangeClosed
  /**
   ** Test for closed {@link Range}.
   ** <p>
   ** A closed range considers the upper limit inclusive.
   */
  @Test
  public void testRangeClosed() {
    Assert.assertEquals(Arrays.asList(BigDecimal.ONE, TWO, THREE), Streams.rangeClosed(BigDecimal.ONE, THREE, i -> i.add(BigDecimal.ONE)).collect(Collectors.toList()));
    Assert.assertEquals(Arrays.asList(new NonComparable(2), new NonComparable(1), new NonComparable(0)), Streams.range(new NonComparable(2), new NonComparable(-1), NonComparable::decrement, Comparator.comparingInt(NonComparable::value).reversed()).collect(Collectors.toList()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testMapToType
  /**
   ** Test for flatten {@link Map}s.
   */
  @Test
  public void testMapToType() {
    final Optional<String> result = Stream.of((CharSequence)"").flatMap(Streams.mapToType(String.class)).findAny();
    Assert.assertEquals("", result.get());
    final Optional<StringBuilder> builder = Stream.of((CharSequence)"").flatMap(Streams.mapToType(StringBuilder.class)).findAny();
    Assert.assertFalse(builder.isPresent());
  }
}
