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

    File        :   Range.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Range.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.Comparator;
import java.util.Objects;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

import java.util.stream.Stream;

import java.util.function.UnaryOperator;
import java.util.stream.StreamSupport;

////////////////////////////////////////////////////////////////////////////////
// interface Range
// ~~~~~~~~~ ~~~~~
/**
 ** An interface that covers value ranges.
 **
 ** @param  <T>                  the type of the range entity representations.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations.
 **                              <br>
 **                              Allowed object is <code>&lt;I&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Range<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // abstract class Base
  // ~~~~~~~~ ~~~~~ ~~~~
  /**
   ** The basic implememtation of mutable and immutable {@link Range}s.
   **
   ** @param  <T>                the type of the range entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   */
  static abstract class Base<T> implements Range<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    protected final Comparator<? super T> comparator;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Base</code> {@link Range} with the specified
     ** {@link Comparator}.
     **
     ** @param  comparator       the {@link Comparator} used to validate the
     **                          lower and upper limit of the {@link Range}.
     **                          <br>
     **                          Allowed object is {@link Comparator}.
     */
    protected Base(final Comparator<? super T> comparator) {
      // ensure inheritance
      super();

      // initalize instance attributes
      this.comparator = Objects.requireNonNull(comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: contains (Range)
    /**
     ** Whether a given value is contained within this {@link Range}.
     **
     ** @param  value            the value to check.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  <code>true</code> if the value is contained by
     **                          this {@link Range}; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean contains(final T value) {
      int min = minimum() == null ? -1 : compare(minimum(), value);
      int max = maximum() == null ?  1 : compare(maximum(), value);
      return (minimumInclusive() ? min <= 0 : min < 0) && (maximumInclusive() ? max >= 0 : max > 0);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: intersects (Range)
    /**
     ** Whether a given range intersects this {@link Range}.
     **
     ** @param  other            the {@link Range} to check against this
     **                          {@link Range}.
     **                          <br>
     **                          Allowed object is {@link Range}.
     **
     ** @return                  <code>true</code> if both {@link Range}s
     **                          intersect; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean intersects(final Range<T> other) {
      checkNonNull(this);
      checkNonNull(other);

      boolean intersects;
      if (!minimumInclusive() || !other.maximumInclusive()) {
        intersects = compare(minimum(), other.maximum()) < 0;
      }
      else {
        intersects = compare(minimum(), other.maximum()) <= 0;
      }
      if (!maximumInclusive() || !other.minimumInclusive()) {
        intersects &= compare(maximum(), other.minimum()) > 0;
      }
      else {
        intersects &= compare(maximum(), other.minimum()) >= 0;
      }
      return intersects;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
    public final int hashCode() {
      return Objects.hash(getClass(), minimum(), maximum(), minimumInclusive(), maximumInclusive());
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>AbstractRange</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>AbstractRange</code>s may be different even though they contain
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
    public final boolean equals(final Object other) {
      if (other == this)
        return true;

      if (!Range.class.isInstance(other))
        return false;

      final Range<?> that = (Range<?>)other;
      return Objects.equals(minimum(), that.minimum()) && Objects.equals(maximum(), that.maximum()) && Objects.equals(minimumInclusive(), that.minimumInclusive()) && Objects.equals(maximumInclusive(), that.maximumInclusive());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character ".." (two dots).
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public final String toString() {
      return (minimumInclusive() ? "(" : "[") + minimum() + ".." + maximum() + (maximumInclusive() ? ")" :"]");
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: compare
    /**
     ** Compares two objects.
     **
     ** @param  lhs              the value to compare with <code>rhs</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     ** @param  rhs              the value to compare with <code>lhs</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the value <code>0</code> if <code>lhs</code> is
     **                          equal to <code>rhs</code>; a value less than
     **                          <code>0</code> if <code>lhs</code> is less than
     **                          <code>rhs</code>; and a value greater than
     **                          <code>0</code> if <code>lhs</code> is greater
     **                          than <code>rhs</code>.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    protected final int compare(final T lhs, final T rhs) {
      return Objects.compare(lhs, rhs, this.comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkNonNull
    /**
     ** Verifies that the given {@link Range} is valid.
     **
     ** @param  range            the {@link Range} to validate.
     **                            <br>
     **                            Allowed object is {@link Range}.
     */
    private void checkNonNull(final Range<?> range) {
      Objects.requireNonNull(range, "range");
      Objects.requireNonNull(range.minimum(), (range != this ? "range " : "") + "minimum");
      Objects.requireNonNull(range.maximum(), (range != this ? "range " : "") + "maximum");
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Immutable
  // ~~~~~ ~~~~~~~~~
  /**
   ** The immutable implememtation of {@link Range}s.
   **
   ** @param  <T>                the type of the range entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   */
  static class Immutable<T> extends Base<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final T       min;
    private final T       max;
    private final boolean minInclusive;
    private final boolean maxInclusive;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Immutable</code> {@link Range}with the specified
     ** limits <code>min</code> and <code>max</code>
     ** <code>(cause == null ? null : cause.toString())</code> (which typically
     ** contains the class and detail message of <code>cause</code>).
     **
     ** @param  min              the lower limit of the <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     ** @param  max              the upper limit of the <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     ** @param  minInclusive     <code>true</code> if the lower limit of
     **                          this <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  maxInclusive     <code>true</code> if the upper limit of this
     **                          <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  comparator       the {@link Comparator} used to validate the
     **                          lower and upper limits of the {@link Range}.
     **                          <br>
     **                          Allowed object is {@link Comparator}.
     */
    private Immutable(final T min, final T max, final boolean minInclusive, final boolean maxInclusive, final Comparator<? super T> comparator) {
      // ensure inheritance
      super(comparator);

      // initalize instance attributes
      this.min          = Objects.requireNonNull(min);
      this.max          = Objects.requireNonNull(max);
      this.minInclusive = minInclusive;
      this.maxInclusive = maxInclusive;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimum (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the lower
     ** limit is set to the given value.
     **
     ** @param  value            the lower limit of the new <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  a copy of this <code>Range</code> with the new
     **                          lower limit.
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> minimum(final T value) {
      return new Immutable<>(Objects.requireNonNull(value), this.max, this.minInclusive, this.maxInclusive, this.comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimum (Range)
    /**
     ** Returns the lower limit of this <code>Range</code>.
     **
     ** @return                  the lower limit of this <code>Range</code>.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public final T minimum() {
      return this.min;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimumInclusive (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** <code>minimum</code> value will be either inclusive or exclusive.
     **
     ** @param  value            <code>true</code> indicating if the
     **                          <code>minimum</code> value should be inclusive
     **                          or exclusive.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  a copy of this <code>Range</code>
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> minimumInclusive(final boolean value) {
      return new Immutable<>(this.min, this.max, value, this.maxInclusive, this.comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimumInclusive (Range)
    /**
     ** Whether the lower limit of this <code>Range</code> is inclusive.
     **
     ** @return                  <code>true</code> if the lower limit of this
     **                          <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean minimumInclusive() {
      return this.minInclusive;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximum (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** upper limit is set to the given value.
     **
     ** @param  value            the upper limit of the new <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  a copy of this <code>Range</code> with the new
     **                          upper limit.
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> maximum(final T value) {
      return new Immutable<>(this.min, Objects.requireNonNull(value), this.minInclusive, this.maxInclusive, this.comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximum (Range)
    /**
     ** Returns the upper limit of this <code>Range</code>.
     **
     ** @return                  the upper limit of this <code>Range</code>.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public final T maximum() {
      return this.max;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumInclusive (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** <code>maximim</code> value will be either inclusive or exclusive.
     **
     ** @param  value            <code>true</code> indicating if the
     **                          <code>maximim</code> value should be inclusive
     **                          or exclusive.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  a copy of this <code>Range</code>
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> maximumInclusive(final boolean value) {
      return new Immutable<>(this.min, this.max, this.minInclusive, value, this.comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumInclusive (Range)
    /**
     ** Whether the upper limit of this <code>Range</code> is inclusive.
     **
     ** @return                  <code>true</code> if the upper limit of this
     **                          <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean maximumInclusive() {
      return this.maxInclusive;
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Mutable
  // ~~~~~ ~~~~~~~
  /**
   ** The mutable implememtation of {@link Range}s.
   **
   ** @param  <T>                the type of the range entity representations.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   */
  static class Mutable<T> extends Base<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private T       min;
    private T       max;
    private boolean minInclusive;
    private boolean maxInclusive;

    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Immutable</code> {@link Range}with the specified
     ** limits <code>min</code> and <code>max</code>
     ** <code>(cause == null ? null : cause.toString())</code> (which typically
     ** contains the class and detail message of <code>cause</code>).
     **
     ** @param  min              the lower limit of the  <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     ** @param  max              the upper limit of the  <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     ** @param  minInclusive     <code>true</code> if the lower limit of
     **                          this <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  maxInclusive     <code>true</code> if the upper limit of
     **                          this <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  comparator       the {@link Comparator} used to validate the
     **                          lower and upper limit of the {@link Range}.
     **                          <br>
     **                          Allowed object is {@link Comparator}.
     */
    Mutable(final T min, final T max, final boolean minInclusive, final boolean maxInclusive, final Comparator<? super T> comparator) {
      // ensure inheritance
      super(comparator);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimum (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the lower
     ** limit is set to the given value.
     **
     ** @param  value            the lower limit of the new <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  a copy of this <code>Range</code> with the new
     **                          lower limit.
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> minimum(final T value) {
      this.min = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimum (Range)
    /**
     ** Returns the lower limit of this <code>Range</code>.
     **
     ** @return                  the lower limit of this <code>Range</code>.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public final T minimum() {
      return this.min;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimumInclusive (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** <code>minimum</code> value will be either inclusive or exclusive.
     **
     ** @param  value            <code>true</code> indicating if the
     **                          <code>minimum</code> value should be inclusive
     **                          or exclusive.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  a copy of this <code>Range</code>
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> minimumInclusive(final boolean value) {
      this.minInclusive = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimumInclusive (Range)
    /**
     ** Whether the lower limit of this <code>Range</code> is inclusive.
     **
     ** @return                  <code>true</code> if the lower limit of this
     **                          <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean minimumInclusive() {
      return this.minInclusive;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximum (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** upper limit is set to the given value.
     **
     ** @param  value            the upper limit of the new <code>Range</code>.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  a copy of this <code>Range</code> with the new
     **                          upper limit.
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> maximum(final T value) {
      this.max = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximum (Range)
    /**
     ** Returns the upper limit of this <code>Range</code>.
     **
     ** @return                  the upper limit of this <code>Range</code>.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @Override
    public final T maximum() {
      return this.max;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumInclusive (Range)
    /**
     ** Factory method to create a copy of this <code>Range</code> where the
     ** <code>maximim</code> value will be either inclusive or exclusive.
     **
     ** @param  value            <code>true</code> indicating if the
     **                          <code>maximim</code> value should be inclusive
     **                          or exclusive.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  a copy of this <code>Range</code>
     **                          <br>
     **                          Possible object is <code>Range</code>.
     */
    @Override
    public final Range<T> maximumInclusive(final boolean value) {
      this.maxInclusive = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumInclusive (Range)
    /**
     ** Whether the upper limit of this <code>Range</code> is inclusive.
     **
     ** @return                  <code>true</code> if the upper limit of this
     **                          <code>Range</code> is inclusive; otherwise
     **                          <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean maximumInclusive() {
      return this.maxInclusive;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Factory method to create a copy of this <code>Range</code> where the lower
   ** limit is set to the given value.
   **
   ** @param  value              the lower limit of the new <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    a copy of this <code>Range</code> with the new
   **                            lower limit.
   **                            <br>
   **                            Possible object is <code>Range</code>.
   */
  Range<T> minimum(final T value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimum
  /**
   ** Returns the lower limit of this <code>Range</code>.
   **
   ** @return                    the lower limit of this <code>Range</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T minimum();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimumInclusive
  /**
   ** Factory method to create a copy of this <code>Range</code> where the
   ** <code>minimum</code> value will be either inclusive or exclusive.
   **
   ** @param  value              <code>true</code> indicating if the
   **                            <code>minimum</code> value should be inclusive
   **                            or exclusive.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a copy of this <code>Range</code>
   **                            <br>
   **                            Possible object is <code>Range</code>.
   */
  Range<T> minimumInclusive(final boolean value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minimumInclusive
  /**
   ** Whether the lower limit of this <code>Range</code> is inclusive.
   **
   ** @return                    <code>true</code> if the lower limit of this
   **                            <code>Range</code> is inclusive; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean minimumInclusive();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Factory method to create a copy of this <code>Range</code> where the upper
   ** limit is set to the given value.
   **
   ** @param  value              the upper limit of the new <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    a copy of this <code>Range</code> with the new
   **                            upper limit.
   **                            <br>
   **                            Possible object is <code>Range</code>.
   */
  Range<T> maximum(final T value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximum
  /**
   ** Returns the upper limit of this <code>Range</code>.
   **
   ** @return                    the upper limit of this <code>Range</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  T maximum();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximumInclusive
  /**
   ** Factory method to create a copy of this <code>Range</code> where the
   ** <code>maximim</code> value will be either inclusive or exclusive.
   **
   ** @param  value              <code>true</code> indicating if the
   **                            <code>maximim</code> value should be inclusive
   **                            or exclusive.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a copy of this <code>Range</code>
   **                            <br>
   **                            Possible object is <code>Range</code>.
   */
  Range<T> maximumInclusive(final boolean value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maximumInclusive
  /**
   ** Whether the upper limit of this <code>Range</code> is inclusive.
   **
   ** @return                    <code>true</code> if the upper limit of this
   **                            <code>Range</code> is inclusive; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean maximumInclusive();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Whether a given value is contained within this <code>Range</code>.
   **
   ** @param  value              the value to check.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    <code>true</code> if the value is contained by
   **                            this <code>Range</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean contains(final T value);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intersects
  /**
   ** Whether a given range intersects this <code>Range</code>.
   **
   ** @param  other              the <code>Range</code> to check against this
   **                            <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>Range</code>.
   **
   ** @return                    <code>true</code> if both <code>Range</code>s
   **                            intersect; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  boolean intersects(final Range<T> other);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Returns a {@link Stream} containing all values contained by the range,
   ** with a given incrementer.
   ** <p>
   ** This method should not return the minimum or maximum value if they are
   ** exclusive.
   ** <p>
   ** The default implementation will apply the incrementer repeatedly on the
   ** current value, starting with the minimum value and will continue as long
   ** as {@link Range#contains(Object)} returns <code>true</code>.
   **
   ** @param  incrementer        the incrementer to use to determine the next
   **                            value.
   **
   ** @return                    a stream containing all values within this
   **                           <code>Range</code>.
   */
  default Stream<T> stream(final UnaryOperator<T> incrementer) {
    final T           m = Objects.requireNonNull(minimum());
    final T           s = contains(m) ? m : incrementer.apply(m);
    final Iterator<T> i = new Iterator<T>() {
      private T next = s;
      @Override
      public boolean hasNext() {
        return contains(next);
      }
      @Override
      public T next() {
        T n = next;
        next = incrementer.apply(next);
        return n;
      }
    };

    Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(i, Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SORTED);
    return StreamSupport.stream(spliterator, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableInteger
  /**
   ** Factory method to create an immutable integer <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Integer> immutableInteger(final int min, final int max) {
    return immutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableLong
  /**
   ** Factory method to create an immutable long <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Long> immutableLong(final long min, final long max) {
    return immutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableDouble
  /**
   ** Factory method to create an immutable double <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed limit is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Double> immutableDouble(final double min, final double max) {
    return immutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutable
  /**
   ** Factory method to create an immutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T extends Comparable<T>> Range<T> immutable(final T min, final T max) {
    return immutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutable
  /**
   ** Factory method to create an immutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  comparator         the {@link Comparator} used to validate the
   **                            lower and upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T> Range<T> immutable(final T min, final T max, final Comparator<? super T> comparator) {
    return new Immutable<>(min, max, true, false, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableClosedInteger
  /**
   ** Factory method to create a immutable closed integer <code>Range</code>
   ** with the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Integer> immutableClosedInteger(final int min, final int max) {
    return immutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableClosedLong
  /**
   ** Factory method to create a immutable closed long <code>Range</code> with
   ** the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Long> immutableClosedLong(final long min, final long max) {
    return immutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableClosedDouble
  /**
   ** Factory method to create a immutable closed double <code>Range</code> with
   ** the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Double> immutableClosedDouble(final double min, final double max) {
    return immutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableClosed
  /**
   ** Factory method to create a closed immutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   **
   ** @param  <T>                the type of the lower and upper limit
   **                            values.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
    **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T extends Comparable<T>> Range<T> immutableClosed(final T min, final T max) {
    return immutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   immutableClosed
  /**
   ** Factory method to create an immutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  comparator         the {@link Comparator} used to validate the
   **                            lower and upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    the immutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T> Range<T> immutableClosed(final T min, final T max, final Comparator<? super T> comparator) {
    return new Immutable<>(min, max, true, true, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableInteger
  /**
   ** Factory method to create an mutable integer <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Integer> mutableInteger(final int min, final int max) {
    return mutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableLong
  /**
   ** Factory method to create an mutable long <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Long> mutableLong(final long min, final long max) {
    return mutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableDouble
  /**
   ** Factory method to create an mutable double <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Double> mutableDouble(final double min, final double max) {
    return mutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutable
  /**
   ** Factory method to create an mutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T extends Comparable<T>> Range<T> mutable(final T min, final T max) {
    return mutable(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutable
  /**
   ** Factory method to create an mutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower limit is inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  comparator         the {@link Comparator} used to validate the
   **                            lower and upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T> Range<T> mutable(final T min, final T max, final Comparator<? super T> comparator) {
    return new Immutable<>(min, max, true, false, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableClosedInteger
  /**
   ** Factory method to create a mutable closed integer <code>Range</code>
   ** with the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Integer> mutableClosedInteger(final int min, final int max) {
    return mutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableClosedLong
  /**
   ** Factory method to create a mutable closed long <code>Range</code> with
   ** the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Long> mutableClosedLong(final long min, final long max) {
    return mutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableClosedDouble
  /**
   ** Factory method to create a mutable closed double <code>Range</code> with
   ** the lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static Range<Double> mutableClosedDouble(final double min, final double max) {
    return mutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableClosed
  /**
   ** Factory method to create a closed mutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
    **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T extends Comparable<T>> Range<T> mutableClosed(final T min, final T max) {
    return mutableClosed(min, max, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mutableClosed
  /**
   ** Factory method to create an mutable <code>Range</code> with the
   ** lower and upper limits specified.
   ** <br>
   ** The lower <b>and</b> upper limit are inclusive per default.
   **
   ** @param  <T>                the type of the lower and upper limits.
   **                            <br>
   **                            Allowed object is <code>&lt;I&gt;</code>.
   ** @param  min                the lower limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  max                the upper limit of the <code>Range</code>.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  comparator         the {@link Comparator} used to validate the
   **                            lower and upper limit of the {@link Range}.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    the mutable <code>Range</code>.
   **                            <br>
   **                            Possilble object is <code>Range</code>.
   */
  static <T> Range<T> mutableClosed(final T min, final T max, final Comparator<? super T> comparator) {
    return new Immutable<>(min, max, true, true, comparator);
  }
}