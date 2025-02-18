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
    Subsystem   :   Common Shared Utility

    File        :   Pair.java

    Compiler    :   Java Developer Kit 8

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Pair.


    Revisions   Date        Editor                    Comment
    -----------+-----------+-------------------------+--------------------------
    1.0.0.0     2023-28-06  dieter.steding@icloud.com First release version
*/

package redsecurity.platform.core.entity;

import java.util.Map;
import java.util.List;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import java.io.Serializable;

import redsecurity.platform.core.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Pair
// ~~~~~ ~~~~
/**
 ** An immutable arbitrary pair of objects. Convenient implementation of
 ** Map.Entry.
 ** <p>
 ** Although the implementation is immutable, there is no restriction on the
 ** objects that may be stored. If mutable objects are stored in the pair,
 ** then the pair itself effectively becomes mutable.
 ** <p>
 ** This class is immutable and thread-safe if the stored objects are immutable.
 **
 ** @param  <K>                  the type of the tag implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 ** @param  <V>                  the type of the value implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the loggables
 **                              extending this class (loggables can return
 **                              their own specific type instead of type defined
 **                              by this class only).
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Pair<K extends Serializable, V extends Serializable> implements Map.Entry<K, V>
                                                                  ,          Comparable<Pair<K, V>>
                                                                  ,          Serializable
                                                                  ,          Tupel {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7648170097630942236")
  private static final long serialVersionUID = 8399806598996527389L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The tag corresponding to this value pair. */
  private K tag;

  /** The value corresponding to this value pair. */
  private V value;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Pair</code> with the specified tag name and value.
   **
   ** @param  tag                the unique name of the attribute.
   ** @param  value              the for the attribute.
   */
  protected Pair(final K tag, final V value) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.tag   = tag;
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getKey (Map.Entry)
  /**
   ** Returns the tag corresponding to this entry.
   **
   ** @return                    the tag corresponding to this entry.
   **
   ** @throws IllegalStateException implementations may, but are not required
   **                               to, throw this exception if the entry has
   **                               been removed from the backing map.
   */
  public final K getKey() {
    return this.tag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setValue (Map.Entry)
  /**
   ** Replaces the value corresponding to this value pair with the specified
   ** value (optional operation).  (Writes through to the map.)
   ** <p>
   ** The behavior of this call is undefined if the mapping has already been
   ** removed from the map (by the iterator's <code>remove</code> operation).
   **
   ** @param  value              the new value to be stored in this value pair.
   **
   ** @return                    the old value corresponding to the entry.
   */
  public V setValue(final V value) {
    final V old = this.value;
    this.value = value;
    return old;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue (Map.Entry)
  /**
   ** Returns the value corresponding to this entry.
   ** <p>
   ** If the mapping has been removed from the backing map (by the iterator's
   ** <code>remove</code> operation), the results of this call are undefined.
   **
   ** @return                    the value corresponding to this entry.
   **
   ** @throws IllegalStateException implementations may, but are not required
   **                               to, throw this exception if the entry has
   **                               been removed from the backing map.
   */
  public final V getValue() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares the pair based on the tag element followed by the value element.
   ** <p>
   ** The element types must be {@link Comparable}.
   **
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    a negative number if this is less, zero if
   **                            equal and a positive number if greater as
   **                            <code>other</code>.
   **
   ** @throws ClassCastException if either object is not comparable
   */
  @Override
  @SuppressWarnings("unchecked")
  public int compareTo(final Pair<K, V> other) {
    return ((Comparable<K>)tag).compareTo(other.tag) < 0 ? -1 : ((Comparable<K>)tag).compareTo(other.tag) > 0 ? 1 : ((Comparable<V>)value).compareTo(other.value) < 0 ? -1 :  ((Comparable<V>)value).compareTo(other.value) > 0 ? 1 : 0;
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
   */
  @Override
  public final int size() {
    return 2;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (Tupel)
  /**
   ** Returns the elements from this tuple as a collection.
   ** <p>
   ** The list contains each element in the tuple in order.
   **
   ** @return                    the elements as a collection.
   */
  public final List<?> element() {
    return CollectionUtility.list(this.tag, this.value);
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
   ** @param  <T>                the type of the tag implementation.
   ** @param  <V>                the type of the value implementation.
   ** @param  tag                the left element, may be <code>null</code>.
   ** @param  value              the right element, may be <code>null</code>.
   **
   ** @return                    a pair formed from the two parameters, never
   **                            <code>null</code>.
   */
  public static <T extends Serializable, V extends Serializable> Pair<T, V> of(final T tag, final V value) {
    return new Pair<T, V>(tag, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combining
  /**
   ** Returns a combiner of pair instances.
   ** <p>
   ** This is useful if you have a stream of <code>Pair&lt;T, V&gt;</code> and
   ** would like to reduce.
   ** <p>
   ** e.g
   ** <pre>
   **   pairList.stream().reduce(Pair.combining(T::combined, V::combined))
   ** </pre>
   **
   ** @param  <T>                the type of the tag implementation.
   ** @param  <V>                the type of the value implementation.
   ** @param  t                  the combiner of tag values.
   ** @param  v                  the combiner of value values.
   **
   ** @return                    the combiner of pair instance.
   */
  public static <T extends Serializable, V extends Serializable> BinaryOperator<Pair<T, V>> combining(final BiFunction<? super T, ? super T, ? extends T> t, final BiFunction<? super V, ? super V, ? extends V> v) {
    return (lhs, rhs) -> lhs.combined(rhs, t, v);
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
   */
  @Override
  public int hashCode() {
    int result = this.tag != null ? this.tag.hashCode() : 0;
    result = 31 * result + (this.value != null ? this.value.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Returns whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Pair</code>s are considered equal if and only if they represent
   ** the same encoded, decoded and template value. As a consequence, two given
   ** <code>Pair</code>s may be different even though they contain the same
   ** attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; otherwise
   **                            <code>false</code>.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Pair<?, ?> that = (Pair<?, ?>)other;
    return (Tupel.equals(this.tag, that.tag) && Tupel.equals(this.value, that.value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   */
  @Override
  public String toString() {
    return String.format("[%s, %s]", this.tag.toString(), this.value.toString());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combined
  /**
   ** Combines this instance with another.
   **
   ** @param  <C>                the type of the first value in the other
   **                            instance.
   ** @param  <D>                the type of the second value in the other
   **                            instance.
   ** @param  other              the other pair.
   ** @param  t                  the combiner of tag values
   ** @param  v                  the combiner of value values
   **                            type <code>V</code>.
   **
   ** @return                    the combined pair instance.
   */
  public <C extends Serializable, D extends Serializable> Pair<K, V> combined(final Pair<C, D> other, final BiFunction<? super K, ? super C, ? extends K> t, final BiFunction<? super V, ? super D, ? extends V> v) {
    return Pair.of(t.apply(tag, other.tag), v.apply(value, other.value));
  }
}