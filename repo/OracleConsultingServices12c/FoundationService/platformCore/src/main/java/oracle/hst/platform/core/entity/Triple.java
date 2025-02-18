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

    File        :   Triple.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Triple.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.io.Serializable;

import java.util.List;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import oracle.hst.platform.core.utility.CollectionUtility;

/**
 ** An immutable arbitrary triple of elements.
 ** <p>
 ** This implementation refers to the elements as 'first', 'second' and 'third'.
 ** <p>
 ** The elements cannot be <code>null</code>.
 ** <p>
 ** Although the implementation is immutable, there is no restriction on the
 ** objects that may be stored. If mutable objects are stored in the triple,
 ** then the triple itself effectively becomes mutable.
 ** <p>
 ** This class is immutable and thread-safe if the stored objects are immutable.
 **
 ** @param  <X>                  the type of the first element implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 ** @param  <Y>                  the type of the second element implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 ** @param  <Z>                  the type of the third element implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 */
public final class Triple<X extends Serializable, Y extends Serializable, Z extends Serializable> implements Comparable<Triple<X, Y, Z>>
                                                                                                  ,          Serializable
                                                                                                  ,          Tupel {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The first element implementation to this value tripple. */
  private X x;

  /** The second element implementation to this value tripple. */
  private Y y;

  /** The thrid element implementation to this value tripple. */
  private Z z;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Triple</code> with the specified values.
   **
   ** @param  x                  the first element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>X</code>.
   ** @param  y                  the second element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>Y</code>.
   ** @param  z                  the thrid element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>Z</code>.
   */
  private Triple(final X x, final Y y, final Z z) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.x = x;
    this.y = y;
    this.z = z;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares the triple based on the first element followed by the second
   ** element followed by the third element.
   ** <p>
   ** The element types must be {@link Comparable}.
   **
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is <code>Triple</code>.
   **
   ** @return                    a negative number if this is less, zero if
   **                            equal and a positive number if greater as
   **                            <code>other</code>.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  @Override
  @SuppressWarnings("unchecked")
  public int compareTo(final Triple<X, Y, Z> other) {
    //return ((Comparable<X>)x).compareTo(other.x) < 0 ? -1 : ((Comparable<Y>)y).compareTo(other.y) > 0 ? 1 : ((Comparable<Z>)z).compareTo(other.z) < 0 ? -1 :  ((Comparable<V>)value).compareTo(other.value) > 0 ? 1 : 0;
    return -1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Tupel)
  /**
   ** Returns the number of elements held by this tuple.
   ** <p>
   ** Each tuple type has a fixed size, returned by this method.
   ** <br>
   ** For example, {@link Pair} returns <code>2</code>.
   **
   ** @return                    the size of the tuple.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int size() {
    return 3;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (Tupel)
  /**
   ** Returns the elements from this tuple as a collection.
   ** <p>
   ** The list contains each element in the tuple in order.
   **
   ** @return                    the elements as a collection.
   **                            <br>
   **                            Possible object is {@link List}.
   */
  public final List<?> element() {
    return CollectionUtility.list(this.x, this.y, this.z);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Obtains a mutable pair of two objects inferring the generic types.
   ** <p>
   ** This factory allows the pair to be created using inference to obtain the
   ** generic types.
   **
   ** @param  <X>                the type of the first element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;X&gt;</code>.
   ** @param  <Y>                the type of the second element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;Y&gt;</code>.
   ** @param  <Z>                the type of the third element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;Z&gt;</code>.
   ** @param  x                  the first element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>X</code>.
   ** @param  y                  the second element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>Y</code>.
   ** @param  z                  the thrid element implementation to this value
   **                            tripple.
   **                            <br>
   **                            Allowed object is <code>Z</code>.
   **
   ** @return                    a tripple formed from the two parameters, never
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>Triple</code>.
   */
  public static <X extends Serializable, Y extends Serializable, Z extends Serializable> Triple<X, Y, Z> of(final X x, final Y y, final Z z) {
    return new Triple<X, Y, Z>(x, y, z);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combining
  /**
   ** Returns a combiner of pair instances.
   ** <p>
   ** This is useful if you have a stream of <code>Triple&lt;X, Y, Z&gt;</code>
   ** and would like to reduce.
   ** <p>
   ** e.g
   ** <pre>
   **   tripleList.stream().reduce(Triple.combining(X::combined, Y::combined, Z::combined))
   ** </pre>
   **
   ** @param  <X>                the type of the first element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;X&gt;</code>.
   ** @param  <Y>                the type of the second element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;Y&gt;</code>.
   ** @param  <Z>                the type of the third element implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;Z&gt;</code>.
   ** @param  x                  the combiner of the first element
   **                            implementation to this value tripple.
   **                            <br>
   **                            Allowed object is <code>X</code>.
   ** @param  y                  the combiner of the second element
   **                            implementation to this value tripple.
   **                            <br>
   **                            Allowed object is <code>Y</code>.
   ** @param  z                  the combiner of the thrid element
   **                            implementation to this value tripple.
   **                            <br>
   **                            Allowed object is <code>Z</code>.
   **
   ** @return                    the combiner of pair instance.
   **                            <br>
   **                            Allowed object is {@link BinaryOperator}.
   */
  public static <X extends Serializable, Y extends Serializable, Z extends Serializable> BinaryOperator<Triple<X, Y, Z>> combining(final BiFunction<? super X, ? super X, ? extends X> x, final BiFunction<? super Y, ? super Y, ? extends Y> y, final BiFunction<? super Z, ? super Z, ? extends Z> z) {
    return (t1, t2) -> t1.combined(t2, x, y, z);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.x != null ? this.x.hashCode() : 0;
    result = 31 * result + (this.y != null ? this.y.hashCode() : 0);
    result = 31 * result + (this.z != null ? this.z.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Triple</code>s are considered equal if and only if they
   ** represent the same encoded, decoded and template value. As a consequence,
   ** two given <code>Triple</code>s may be different even though they contain
   ** the same attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Triple<?, ?, ?> that = (Triple<?, ?, ?>)other;
    return (Tupel.equals(this.x, that.x) && Tupel.equals(this.y, that.y) && Tupel.equals(this.y, that.y));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return String.format("[%s, %s, %s]", this.x.toString(), this.y.toString(), this.z.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combined
  /**
   ** Combines this instance with another.
   **
   ** @param  <Q>                the type of the first value in the other
   **                            instance.
   **                            <br>
   **                            Allowed object is <code>&lt;Q&gt;</code>.
   ** @param  <R>                the type of the second value in the other
   **                            instance.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <S>                the type of the second value in the other
   **                            instance.
   **                            <br>
   **                            Allowed object is <code>&lt;S&gt;</code>.
   ** @param  other              the other triple.
   **                            <br>
   **                            Allowed object is <code>Triple</code>.
   ** @param  x                  the combiner of first value values.
   **                            <br>
   **                            Allowed object is {@link BiFunction} where the
   **                            first argument of the function applied is of
   **                            type <code>X</code> and the second argument of
   **                            type <code>Q</code>.
   ** @param  y                  the combiner of second value values.
   **                            <br>
   **                            Allowed object is {@link BiFunction} where the
   **                            first argument of the function applied is of
   **                            type <code>Y</code> and the second argument of
   **                            type <code>R</code>.
   ** @param  z                  the combiner of third value values.
   **                            <br>
   **                            Allowed object is {@link BiFunction} where the
   **                            first argument of the function applied is of
   **                            type <code>Z</code> and the second argument of
   **                            type <code>S</code>.
   **
   ** @return                    the combined tripple instance.
   **                            <br>
   **                            Possible object is <code>Triple</code>.
   */
  public <Q extends Serializable, R extends Serializable, S extends Serializable> Triple<X, Y, Z> combined(final Triple<Q, R, S> other, final BiFunction<? super X, ? super Q, ? extends X> x, final BiFunction<? super Y, ? super R, ? extends Y> y, final BiFunction<? super Z, ? super S, ? extends Z> z) {
    return Triple.of(x.apply(this.x, other.x), y.apply(this.y, other.y), z.apply(this.z, other.z));
  }
}