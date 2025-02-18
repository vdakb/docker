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

    File        :   CollectionUtility.java

    Compiler    :   Java Developer Kit 8 (JDK8)

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    CollectionUtility.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-17  DSteding    First release version
*/

package redsecurity.platform.core.utility;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.util.stream.Collectors;

import java.util.concurrent.atomic.AtomicInteger;

import redsecurity.platform.core.SystemBundle;
import redsecurity.platform.core.SystemValidator;

import redsecurity.platform.core.entity.Pair;

////////////////////////////////////////////////////////////////////////////////
// abstract class CollectionUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class CollectionUtility {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A pre-allocated byte array containing zero bytes.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final byte[]     EMPTY_BYTES   = {};
  /**
   ** A pre-allocated empty character array.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final char[]     EMPTY_CHARS   = {};
  /**
   ** A pre-allocated empty integer array.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final int[]      EMPTY_INTS    = {};
  /**
   ** A pre-allocated empty string array.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final String[]   EMPTY_STRINGS = {};
  /**
   ** A pre-allocated empty objects array.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final Object[]   EMPTY_OBJECTS = {};
  /**
   ** A pre-allocated empty objects array.
   */
  @SuppressWarnings("oracle.jdeveloper.java.array-field-access")
  public static final Class<?>[] EMPTY_CLASSES = {};

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CollectionUtility</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new CollectionUtility()" and enforces use of the public method below.
   */
  private CollectionUtility() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Map} is empty.
   **
   ** @param  <K>                the type of the key property of the {@link Map}
   **                            to check.
   ** @param  <V>                the type of the value property of the
   **                            {@link Map} to
   **                            check.
   ** @param  collection         the {@link Map} to check for emptyness.
   **                            element is of type <code>K</code> as the key
   **                            and for <code>V</code> the value.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   */
  public static <K, V> boolean empty(final Map<K, V> collection) {
    return collection == null || collection.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Collection} is empty.
   **
   ** @param  <T>                the required class type of elements in the
   **                            {@link Collection}.
   ** @param  collection         the {@link Collection} to check for emptyness.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   */
  public static <T> boolean empty(final Collection<T> collection) {
    return collection == null || collection.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Determines if the given {@link Iterable} contains no elements.
   ** <p>
   ** There is no precise {@link Iterator} equivalent to this method, since one
   ** can only ask an iterator whether it has any elements <i>remaining</i>
   ** (which one does using {@link Iterator#hasNext}).
   **
   ** @param  iterable           the {@link Iterable} to be tested for
   **                            emptiness.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    <code>true</code> if the {@link Iterable}
   **                            contains no elements.
   */
  public static boolean empty(final Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>)iterable).isEmpty() : !iterable.iterator().hasNext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements in {@link Iterable}.
   **
   ** @param  iterable           the {@link Iterable} to return the number of
   **                            remaining elements from.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                   the number of elements.
   */
  public static int size(final Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>)iterable).size() : remain(iterable.iterator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remain
  /**
   ** Returns the number of elements remaining in {@link Iterator}.
   ** <br>
   ** The iterator will be left exhausted: its {@link Iterator#hasNext()} method
   ** will return <code>false</code> aftrewards.
   **
   ** @param  iterator           the {@link Iterator} to return the number of
   **                            remaining elements from.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   **
   ** @return                    the number of elements remaining.
   */
  public static int remain(final Iterator<?> iterator) {
    final AtomicInteger count = new AtomicInteger(0);
    iterator.forEachRemaining(
      element -> {
        count.incrementAndGet();
      }
    );
    return count.get();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   at
  /**
   ** Returns the element at the specified position in an {@link Iterable}.
   **
   ** @param  <T>                the required class type of the returned item.
   ** @param  iterable           the {@link Iterable} as the provider of the
   **                            elements.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  index              the index position of the element to return.
   **
   ** @return                    the element at the specified position in
   **                            {@link Iterable}.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or
   **                                   greater than or equal to the size of
   **                                   {@link Iterable}.
   */
  public static <T> T at(final Iterable<T> iterable, final int index) {
    if (iterable instanceof List) {
      return ((List<T>)iterable).get(index);
    }
    else if (iterable instanceof Collection) {
      // can check both ends
      final Collection<T> collection = (Collection<T>)iterable;
      SystemValidator.assertBoundary(index, collection.size());
    }
    return at(iterable.iterator(), index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   at
  /**
   ** Advances {@link Iterator} <code>index + 1</code> times, returning the
   ** element at the <code>index</code><sup>th</sup> position.
   **
   ** @param  <T>                the required class type of the returned item.
   ** @param  iterator           the {@link Iterator} as the provider of the
   **                            elements.
   **                            <br>
   **                            Must <b>not</b> be <code>null</code>.
   ** @param  index              the index position of the element to return.
   **
   ** @return                    the element at the specified position in
   **                            {@link Iterator}.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or
   **                                   greater than or equal to the number of
   **                                   elements remaining in {@link Iterator}.
   */
  public static <T> T at(final Iterator<T> iterator, final int index) {
    // prevent bogus input can only check the lower end
    if (index < 0)
      throw new IndexOutOfBoundsException("Index cannot be negative: " + index);

    int skipped = 0;
    while (iterator.hasNext()) {
      T t = iterator.next();
      if (skipped++ == index) {
        return t;
      }
    }
    throw new IndexOutOfBoundsException("position (" + index + ") must be less than the number of elements that remained (" + skipped + ")");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   coalesce
  /**
   ** Returns the first non-<code>null</code> object of the argument list, or
   ** <code>null</code> if there is no such element.
   **
   ** @param  <T>                the required class type of elements in the
   **                            array to examine.
   ** @param  array              the argument list of objects to be tested for
   **                            non-<code>null</code>.
   **
   ** @return                    the first non-<code>null</code> object of the
   **                            argument list, or <code>null</code> if there is
   **                            no such element.
   */
  @SafeVarargs
  public static <T> T coalesce(final T... array) {
    for (T cursor : array) {
      if (cursor != null) {
        return cursor;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   oneOf
  /**
   ** Returns <code>true</code> if the given object equals one of the given
   ** objects.
   **
   ** @param  <T>                the required class type of elements in the
   **                            array to examine.
   ** @param  value              the object value to be checked if it equals one
   **                            of the given objects.
   ** @param  array              the argument list of objects to be tested for
   **                            equality.
   **
   ** @return                    <code>true</code> if the given object equals
   **                            one of the given objects.
   */
  @SafeVarargs
  public static <T> boolean oneOf(final T value, final T... array) {
    for (Object cursor : array) {
      if (Objects.equals(value, cursor)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Returns <code>true</code> if both {@link Collection Collections} contains
   ** the same elements, in the same quantities, regardless of order and
   ** collection type.
   ** <p>
   ** Empty collections and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the required class type of elements in the
   **                            {@link Collection} to examine.
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
 public static <T> boolean equal(final Collection<T> lhs, final Collection<T> rhs) {
    if (lhs == rhs)
      return true;

    // if either lhs is null, return whether the rhs is empty
    if (lhs == null)
      return rhs == null || rhs.isEmpty();
    if (rhs == null)
      return lhs == null || lhs.isEmpty();

    // if sizes are not equal, they can't possibly match
    if (lhs.size() != rhs.size())
      return false;

    final Set<T> l = new HashSet<>(lhs);
    final Set<T> r = new HashSet<>(rhs);
    return l.equals(r);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalIgnoreCase
  /**
   ** Returns <code>true</code> if both {@link Collection Collections} contains
   ** the same elements, in the same quantities, regardless of order and
   ** collection type.
   ** <p>
   ** Empty collections and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the required class type of elements in the
   **                            {@link Collection} to examine.
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
 public static <T> boolean equalIgnoreCase(final Collection<T> lhs, final Collection<T> rhs) {
    if (lhs == rhs)
      return true;

    // if either lhs is null, return whether the rhs is empty
    if (lhs == null)
      return rhs == null || rhs.isEmpty();
    if (rhs == null)
      return lhs == null || lhs.isEmpty();

    // if sizes are not equal, they can't possibly match
    if (lhs.size() != rhs.size())
      return false;

    final Set<String> l = lhs.stream().map(e -> lhs.toString().toLowerCase()).collect(Collectors.toSet());
    final Set<String> r = lhs.stream().map(e -> rhs.toString().toLowerCase()).collect(Collectors.toSet());
    return l.equals(r);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equal
  /**
   ** Returns if both arrays contains the same elements, in the same quantities,
   ** regardless of order and collection type.
   ** <p>
   ** Empty arrays and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the required class type of elements in the
   **                            arrays to examine.
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
  public static <T> boolean equal(final T[] lhs, final T[] rhs) {
    if (lhs == rhs)
      return true;

    // if left-hand-side array is null, return whether the right-hand-side array
    // is null or empty
    if (lhs == null)
      return rhs == null || rhs.length == 0;
    // if right-hand-side array is null, return whether the left-hand-side array
    // is null or empty
    if (rhs == null)
      return lhs == null || lhs.length == 0;

    // if sizes are not equal, they can't possibly match
    if (lhs.length != rhs.length)
      return false;

    final Set<T> l = new HashSet<>(Arrays.asList(lhs));
    final Set<T> r = new HashSet<>(Arrays.asList(rhs));
    return l.equals(r);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equalIgnoreCase
  /**
   ** Returns if both arrays contains the same elements, in the same quantities,
   ** regardless of order and collection type.
   ** <p>
   ** Empty arrays and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the required class type of elements in the
   **                            arrays to examine.
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
  public static <T> boolean equalIgnoreCase(final T[] lhs, final T[] rhs) {
    if (lhs == rhs)
      return true;

    // if left-hand-side array is null, return whether the right-hand-side array
    // is null or empty
    if (lhs == null)
      return rhs == null || rhs.length == 0;
    // if right-hand-side array is null, return whether the left-hand-side array
    // is null or empty
    if (rhs == null)
      return lhs == null || lhs.length == 0;

    // if sizes are not equal, they can't possibly match
    if (lhs.length != rhs.length)
      return false;

    final Set<String> l = Arrays.stream(lhs).map(e -> e.toString().toLowerCase()).collect(Collectors.toSet());
    final Set<String> r = Arrays.stream(rhs).map(e -> e.toString().toLowerCase()).collect(Collectors.toSet());
    return l.equals(r);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable {@link List} based on the {@link List} passed in
   ** checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between lists.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  list               the list or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original list.
   */
  public static <T extends Serializable> List<T> unmodifiable(final List<? extends T> list) {
    return Collections.unmodifiableList(new ArrayList<T>(notNull(list)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable {@link Set} based on the {@link Set} passed in
   ** checks for <code>null</code> and returns an empty set if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Set}.
   ** @param  set                the set or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original set.
   */
  public static <T extends Serializable> Set<T> unmodifiable(final Set<? extends T> set) {
    return Collections.unmodifiableSet(notNull(set));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns a unmodifiable {@link Map} with the elements obtained from the
   ** specified {@link Map}. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the key value of the returned
   **                            {@link Map}.
   ** @param  <K>                the type of the value value of the returned
   **                            {@link Map}.
   ** @param  origin             the values where the elements are obtained
   **                            from.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link Map}.
   */
  public static <T, K> Map<T, K> unmodifiable(final Map<T, K> origin) {
    return Collections.unmodifiableMap(new HashMap<T, K>(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedList
  /**
   ** Returns an synchronized {@link List} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  origin             the values where the elements are obtained
   **                            from.
   **
   ** @return                    a unmodifiable proxy on the original {@link List}.
   */
  @SafeVarargs
  @SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
  public static <T extends Serializable> List<T> synchronizedList(final T... origin) {
    return synchronizedList(list(origin));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedList
  /**
   ** Returns an synchronized {@link List} based on the {@link List} passed in
   ** checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  origin             the List or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original {@link List}.
   */
  public static <T extends Serializable> List<T> synchronizedList(final List<? extends T> origin) {
    return Collections.synchronizedList(unmodifiableList(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedList
  /**
   ** Returns an synchronized {@link List} based on the {@link Set} passed in
   ** checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  origin             the {@link List} or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            {@link Set}.
   */
  public static <T extends Serializable> List<T> synchronizedList(final Set<? extends T> origin) {
    return Collections.synchronizedList(unmodifiableList(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedList
  /**
   ** Returns an synchronized {@link List} based on the {@link Collection}
   ** passed in checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  origin             the {@link List} or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            {@link Collection}.
   */
  public static <T extends Serializable> List<T> synchronizedList(final Collection<? extends T> origin) {
    return Collections.synchronizedList(unmodifiableList(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified {@link Collection}.
   ** <br>
   ** The {@link List} is backed by the original collection so no copy is made.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  collection         the collection or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            collection.
   */
  public static <T extends Serializable> List<T> unmodifiableList(final Collection<? extends T> collection) {
    return Collections.unmodifiableList(list(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified {@link Enumeration}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   */
  public static <T extends Serializable> List<T> unmodifiableList(final Enumeration<T> enumeration) {
    return Collections.unmodifiableList(list(enumeration));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  array              the array of elements or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the array elements.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> List<T> unmodifiableList(final T... array) {
    return Collections.unmodifiableList(list(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a modifiable {@link List} with the elements obtained from the
   ** specified typed array. The order of the elements remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  item               the values where the elements are obtained
   **                            from.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified typed array.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> List<T> list(final T... item) {
    // default to empty
    final List<T> collector = new ArrayList<T>();
    // not empty populate the set
    if (item != null && item.length != 0) {
      for (T t : item)
        collector.add(t);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a {@link List} with the elements obtained from the specified
   ** {@link Iterator}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified {@link Iterator}.
   */
  public static <T extends Serializable> List<T> list(final Iterator<T> iterator) {
    if (iterator == null || !iterator.hasNext())
      return Collections.emptyList();

    final List<T> result = new ArrayList<T>();
    if (iterator != null)
       while (iterator.hasNext())
         result.add(iterator.next());

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified {@link Enumeration}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   */
  public static <T extends Serializable> List<T> list(final Enumeration<T> enumeration) {
    return (enumeration == null || !enumeration.hasMoreElements()) ? Collections.emptyList() : Collections.list(enumeration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a modifiable {@link List} with the elements obtained from the
   ** specified {@link Collection}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified {@link Collection}.
   */
  public static <T extends Serializable> List<T> list(final Collection<? extends T> collection) {
    return new ArrayList<T>(notNull(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortedList
  /**
   ** Returns a modifiable sorted {@link List} with the elements obtained from
   ** the specified {@link Collection}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link List}.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **
   ** @return                    the modifiable sorted {@link List} with the
   **                            elements obtained from the specified
   **                            {@link Collection}.
   */
  public static <T  extends Serializable & Comparable<? super T>> List<T> sortedList(final Collection<? extends T> collection) {
    final List<T> list = list(collection);
    Collections.sort(list);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseInsensitiveSet
  /**
   ** Creates a case-insensitive modifiable {@link Set}.
   **
   ** @return                    an empty case-insensitive set.
   */
  public static SortedSet<String> caseInsensitiveSet() {
    return new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedSet
  /**
   ** Returns an synchronized {@link Set} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Set}.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **
   ** @return                    a synchronized proxy on the original set.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Set<T> synchronizedSet(final T... array) {
    return Collections.synchronizedSet(set(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableSet
  /**
   ** Returns a unmodifiable {@link Set} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Set}.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Set<T> unmodifiableSet(final T... array) {
    return Collections.unmodifiableSet(set(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableSet
  /**
   ** Returns a unmodifiable {@link Set} with the elements obtained from the
   ** specified {@link Collection}.
   ** <br>
   ** The {@link Set} is backed by the original collection so no copy is made.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Set}.
   ** @param  collection         the collection or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            collection.
   */
  public static <T extends Serializable> Set<T> unmodifiableSet(final Collection<T> collection) {
    return Collections.unmodifiableSet(set(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns a modifiable {@link Set} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the expected class type of elements in the
   **                            returned {@link Set}.
   ** @param  item               the values where the elements are obtained
   **                            from.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Set<T> set(final T... item) {
    // default to empty
    final Set<T> collector = new LinkedHashSet<T>();
    // not empty populate the set
    if (item != null && item.length != 0) {
      for (T t : item)
        collector.add(t);
    }
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns a modifiable {@link Set} with the elements obtained from the
   ** specified {@link Iterator}.
   **
   ** @param  <T>                the expected class type of elements in the
   **                            returned {@link Set}.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified {@link Iterator}.
   */
  public static <T extends Serializable> Set<T> set(final Iterator<T> iterator) {
    final Set<T> result = new TreeSet<T>();
    if (iterator != null)
       while (iterator.hasNext())
         result.add(iterator.next());

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns a unmodifiable {@link Set} with the elements obtained from the
   ** specified {@link Enumeration}.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Set}.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   */
  public static <T extends Serializable> Set<T> set(final Enumeration<T> enumeration) {
    return (enumeration == null || !enumeration.hasMoreElements()) ? Collections.emptySet() : unmodifiableSet(Collections.list(enumeration));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns a modifiable {@link Set} with the elements obtained from the
   ** specified {@link Collection}.
   **
   ** @param  <T>                the expected class type of elements in the
   **                            returned {@link Set}.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified {@link Collection}.
   */
  public static <T extends Serializable> Set<T> set(final Collection<T> collection) {
    return new HashSet<T>(notNull(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedMap
  /**
   ** Returns an synchronized {@link Map} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the key and value of the returned
   **                            {@link Map}.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **
   ** @return                    a synchronized proxy on the original array.
   */
  public static <T extends Serializable> Map<T, T> synchronizedMap(final T[][] array) {
    final Map<T, T> map = new HashMap<T, T>();
    for (int i = 0; array != null && i < array.length; i++) {
      map.put(array[i][0], array[i][1]);
    }
    return Collections.synchronizedMap(map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedMap
  /**
   ** Returns an synchronized {@link Map} with the elements obtained from the
   ** specified {@link Map}. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <K>                the type of the key value of the created
   **                            {@link Map}.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   ** @param  origin             the values where the elements are obtained
   **                            from.
   **
   ** @return                    a synchronized proxy on the original set.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> synchronizedMap(final Map<K, V> origin) {
    return Collections.unmodifiableMap(new HashMap<K, V>(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the required class type of elements in the
   **                            returned {@link Map}.
   ** @param  item               the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            If it is <code>null</code> or empty, the map
   **                            will be empty.
   **                            <br>
   **                            If it is non-empty, then the number of elements
   **                            in the array must be a multiple of two.
   **                            <br>
   **                            Elements in even-numbered indexes will be the
   **                            keys for the map entries, while elements in
   **                            odd-numbered indexes will be the map values.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified typed array.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Map<T, T> map(final T... item) {
    if ((item == null) || (item.length == 0))
      return Collections.emptyMap();

    SystemValidator.assertTrue(((item.length % 2) == 0));

    final int                 size = item.length / 2;
    final LinkedHashMap<T, T> map  = new LinkedHashMap<>(computeCapacity(size));
    for (int i = 0; i < item.length;) {
      map.put(item[i++], item[i++]);
    }
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <K>                the required class type of keys in the returned
   **                            {@link Map}.
   ** @param  <V>                the required class type of values in the
   **                            returned {@link Map}.
   ** @param  item               the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            If it is <code>null</code> or empty, the map
   **                            will be empty.
   **                            <br>
   **                            If it is non-empty, then the number of elements
   **                            in the array must be a multiple of two.
   **                            <br>
   **                            Elements in even-numbered indexes will be the
   **                            keys for the map entries, while elements in
   **                            odd-numbered indexes will be the map values.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified typed array.
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final Pair<K, V>... item) {
    if ((item == null) || (item.length == 0))
      return Collections.emptyMap();

    final LinkedHashMap<K, V> map  = new LinkedHashMap<>(computeCapacity(item.length));
    for (Pair<K, V> pair : item) {
      map.put(pair.getKey(), pair.getValue());
    }
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code> and <code>v</code> as the value pair.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k                  the key value of the created {@link Map}.
   ** @param  v                  the value mapped to the key of the created
   **                            {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k, final V v) {
    final Map<K, V> map = new HashMap<K, V>();
    map.put(k, v);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code>s and <code>v</code>s as the value pairs.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k0                 the first key value of the created {@link Map}.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k0, final V v0, final K k1, final V v1) {
    final Map<K, V> map = map(k0, v0);
    map.put(k1, v1);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code>s and <code>v</code>s as the value pairs.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k0                 the first key value of the created {@link Map}.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {
    final Map<K, V> map = map(k0, v0, k1, v1);
    map.put(k2, v2);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code>s and <code>v</code>s as the value pairs.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k0                 the first key value of the created {@link Map}.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
    final Map<K, V> map = map(k0, v0, k1, v1, k2, v2);
    map.put(k3, v3);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code>s and <code>v</code>s as the value pairs.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k0                 the first key value of the created {@link Map}.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k4                 the fiveth key value of the created
   **                            {@link Map}.
   ** @param  v4                 the fiveth value mapped to the first key of the
   **                            created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
    final Map<K, V> map = map(k0, v0, k1, v1, k2, v2, k3, v3);
    map.put(k4, v4);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code>s and <code>v</code>s as the value pairs.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  k0                 the first key value of the created {@link Map}.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k4                 the fiveth key value of the created
   **                            {@link Map}.
   ** @param  v4                 the fiveth value mapped to the first key of the
   **                            created {@link Map}.
   ** @param  k5                 the sixth key value of the created
   **                            {@link Map}.
   ** @param  v5                 the sixth value mapped to the first key of the
   **                            created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
    final Map<K, V> map = map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4);
    map.put(k5, v5);
    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified {@link Iterator} that may be contained in the specified source
   ** {@link Map}.
   **
   ** @param  <K>                the expected class type of the key element.
   ** @param  <V>                the expected class type of the value element.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   ** @param  map                the {@link Map} providing the source
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link Map} that had an
   **                            mapping to an element of {@link Iterator}.
   */
  public static <K extends Serializable,V extends Serializable> Map<K,V> map(final Iterator<K> iterator, final Map<K,V> map) {
    final Map<K,V> result = new HashMap<K,V>();
    if (iterator != null && !empty(map))
      while (iterator.hasNext()) {
        K o = iterator.next();
        result.put(o, map.get(o));
      }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified {@link Properties}.
   **
   ** @param  properties         the {@link Properties} where the elements are
   **                            obtained from.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of {@link Iterator}.
   */
  public static Map<String, String> map(final Properties properties) {
    final Map<String, String> rv = new HashMap<String, String>();
    for (Map.Entry<Object, Object> entry : properties.entrySet())
      rv.put((String)entry.getKey(), (String)entry.getValue());

    return rv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified arrays.
   ** <p>
   ** The order is important here because each key will map to one value.
   **
   ** @param  <K>                the type of the key value of the created
   **                            {@link Map}.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   ** @param  k                  the array providing the key values of
   **                            the created {@link Map}.
   ** @param  v                  the array providing the values mapped to
   **                            the keys of the created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of the arrays.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final K[] k, final V[] v) {
    // throw if there's invalid input
    if (k.length != v.length)
      throw new IllegalArgumentException(SystemBundle.RESOURCE.string(SystemBundle.ARGUMENT_SIZE_MISMATCH));

    final Map<K, V>   map = new HashMap<K, V>(k.length);
    for (int i = 0; i < k.length; i++)
      map.put(k[i], v[i]);

    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified {@link List}s.
   ** <p>
   ** The order is important here because each key will map to one value.
   **
   ** @param  <K>                the type of the key value of the created
   **                            {@link Map}.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   ** @param  keys               the {@link List} providing the key values of
   **                            the created {@link Map}.
   ** @param  values             the {@link List} providing the values mapped to
   **                            the keys of the created {@link Map}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of the {@link List}s.
   */
  public static <K extends Serializable, V extends Serializable> Map<K, V> map(final List<K> keys, final List<V> values) {
    // throw if there's invalid input
    if (keys.size() != values.size())
      throw new IllegalArgumentException(SystemBundle.RESOURCE.string(SystemBundle.ARGUMENT_SIZE_MISMATCH));

    final Map<K, V>   map = new HashMap<K, V>(keys.size());
    final Iterator<K> kit   = keys.iterator();
    final Iterator<V> vit   = values.iterator();
    while (kit.hasNext() && vit.hasNext())
      map.put(kit.next(), vit.next());

    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   array
  /**
   ** Returns a byte array from the provided integer values.
   ** <br>
   ** All of the integer values must be between 0x00 and 0xFF (0 and 255),
   ** inclusive.
   ** <br>
   ** Any bits et outside of that range will be ignored.
   **
   ** @param  bytes              the values to include in the byte array.
   **
   ** @return                    a byte array with the provided set of values.
   */
  public static byte[] array(final int... bytes) {
    // prevent bogus input
    if ((bytes == null) || (bytes.length == 0))
      return EMPTY_BYTES;

    final byte[] array = new byte[bytes.length];
    for (int i = 0; i < bytes.length; i++)
      array[i] = (byte)(bytes[i] & 0xFF);

    return array;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   array
  /**
   ** Returns the specified collection of strings to an array.
   **
   ** @param  collection         the collection to convert to an array.
   **                            <br>
   **                            May be <code>null</code>.
   **
   ** @return                    a string array if the specified collection is
   **                            non-<code>null</code>, or <code>null</code> if
   **                            the specified collection is <code>null</code>.
   */
  public static String[] array(final Collection<String> collection) {
    // prevent bogus input
    return (collection == null) ? null : collection.toArray(EMPTY_STRINGS);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   array
  /**
   ** Returns an array containing the elements of the provided collection.
   **
   ** @param  <T>                the type of element included in the provided
   **                            collection.
   ** @param  collection         the collection to convert to an array.
   **                            <br>
   **                            May be <code>null</code>
   ** @param  type               the type of element contained in the
   **                            collection.
   **
   ** @return                    an array containing the elements of the
   **                            provided collection, or <code>null</code> if
   **                            the provided collection is<code>null</code>.
   */
  @SuppressWarnings({"unchecked", "oracle.jdeveloper.java.null-array-return"})
  public static <T extends Serializable> T[] array(final Collection<T> collection, final Class<T> type) {
    // prevent bogus input
    if (collection == null)
      return null;

    final T[] array = (T[]) Array.newInstance(type, collection.size());
    return collection.toArray(array);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of {@link List}
   ** if the parameter <code>list</code> is <code>null</code>.
   ** <br>
   ** Otherwise return the parameter that was passed in.
   **
   ** @param  <T>                the type of the {@link List} to check.
   ** @param  list               the {@link List} to check for emptyness.
   **
   ** @return                    a new instance of {@link ArrayList} if
   **                            <code>list</code> is <code>null</code>;
   **                            otherwise <code>list</code>,
   */
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> List<T> notNull(final List<T> list) {
    return (list == null) ? Collections.emptyList() : list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of {@link Set}
   ** if the parameter <code>set</code> is <code>null</code>.
   ** <br>
   ** Otherwise return the parameter that was passed in.
   **
   ** @param  <T>                the type of the {@link Set} to check.
   ** @param  set                the {@link Set} to check for emptyness.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>set</code> is <code>null</code>;
   **                            otherwise <code>set</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Set<T> notNull(final Set<T> set) {
    return (set == null) ? Collections.emptySet() : set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of {@link Map}
   ** if the parameter <code>map</code> is <code>null</code>.
   ** <br>
   ** Otherwise return the parameter that was passed in.
   **
   ** @param  <T>                the type of the key value of the created
   **                            {@link Map}.
   ** @param  <K>                the type of the value value of the created
   **                            {@link Map}.
   ** @param  map                the {@link Map} to check for emptyness.
   **
   ** @return                    a new instance of {@link HashMap} if
   **                            <code>map</code> is <code>null</code>;
   **                            otherwise <code>map</code>,
   */
  @SuppressWarnings("unchecked")
  public static <T, K> Map<T, K> notNull(final Map<T, K> map) {
    return (map == null) ? Collections.emptyMap() : map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of
   ** {@link Collection} if the parameter <code>collection</code> is
   ** <code>null</code>.
   ** <br>
   ** Otherwise return the parameter that was passed in.
   **
   ** @param  <T>                the type of the collection.
   ** @param  collection         the {@link Collection} to check for emptyness.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>collection</code> is <code>null</code>;
   **                            otherwise <code>collection</code>,
   */
  public static <T extends Serializable> Collection<T> notNull(final Collection<T> collection) {
    return collection == null ? Collections.emptySet() : collection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeCapacity
  /**
   ** Computes the capacity that should be used for a map or a set with the
   ** <code>required</code> number of elements, which can help avoid the need to
   ** re-hash or re-balance the map if too many items are added.
   ** <br>
   ** This method bases its computation on the default map load factor of
   ** <code>0.75</code>.
   **
   ** @param  required           the required maximum number of items that will
   **                            be placed in a collection.
   **                            <br>
   **                            It must be greater than or equal to zero.
   **
   ** @return                    the capacity that should be used for a
   **                            collection with the estimated number of
   **                            elements.
   */
  public static int computeCapacity(final int required) {
    switch (required) {
      case 0   : return 0;
      case 1   : return 2;
      case 2   : return 3;
      case 3   : return 5;
      case 4   : return 6;
      case 5   : return 7;
      case 6   : return 9;
      case 7   : return 10;
      case 8   : return 11;
      case 9   : return 13;
      case 10  : return 14;
      case 11  : return 15;
      case 12  : return 17;
      case 13  : return 18;
      case 14  : return 19;
      case 15  : return 21;
      case 16  : return 22;
      case 17  : return 23;
      case 18  : return 25;
      case 19  : return 26;
      case 20  : return 27;
      case 30  : return 41;
      case 40  : return 54;
      case 50  : return 67;
      case 60  : return 81;
      case 70  : return 94;
      case 80  : return 107;
      case 90  : return 121;
      case 100 : return 134;
      case 110 : return 147;
      case 120 : return 161;
      case 130 : return 174;
      case 140 : return 187;
      case 150 : return 201;
      case 160 : return 214;
      case 170 : return 227;
      case 180 : return 241;
      case 190 : return 254;
      case 200 : return 267;
      default  : SystemValidator.assertTrue((required >= 0));
                 // 536,870,911 is Integer.MAX_VALUE/4
                 // if the value is larger than that, then we'll fall back to
                 // using floating-point arithmetic
                 if (required > 536_870_911) {
                   final int capacity = ((int)(required / 0.75)) + 1;
                   if (capacity <= required) {
                     // suggest that the expected number of items is so big that
                     // the computed capacity can't be adequately represented by
                     // an integer
                     // in that case, we'll just return the expected item count
                     // and let the map or set get re-hashed/re-balanced if it
                     // actually gets anywhere near that size
                     return required;
                   }
                   else {
                     return capacity;
                   }
                 }
                 else {
                   return ((required * 4) / 3) + 1;
                 }
    }
  }
}