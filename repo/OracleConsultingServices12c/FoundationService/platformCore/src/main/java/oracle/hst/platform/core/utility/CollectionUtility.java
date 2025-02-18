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

    File        :   CollectionUtility.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CollectionUtility.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.lang.reflect.Array;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.SystemBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class CollectionUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection utility methods. Mainly for internal use.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class CollectionUtility {

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
   ** Determines if the given {@link Iterable} contains no elements.
   ** <p>
   ** There is no precise {@link Iterator} equivalent to this method, since one
   ** can only ask an iterator whether it has any elements <i>remaining</i>
   ** (which one does using {@link Iterator#hasNext}).
   **
   ** @param  iterable           the {@link Iterable} to be tested for
   **                            emptiness.
   **                            <br>
   **                            Allowed object is {@link Iterable}.
   **
   ** @return                    <code>true</code> if the {@link Iterable}
   **                            contains no elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean empty(final @NotNull(message="Iterable cannot be null") Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>)iterable).isEmpty() : !iterable.iterator().hasNext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements in {@link Iterable}.
   **
   ** @param  iterable          the {@link Iterable} to return the number of
   **                           remaining elements from.
   **                           <br>
   **                           Allowed object is {@link Iterator}.
   **
   ** @return                   the number of elements.
   **                           <br>
   **                           Possible object is <code>int</code>.
   */
  public static int size(final  @NotNull(message="Iterable cannot be null") Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : size(iterable.iterator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements remaining in {@link Iterator}.
   ** <br>
   ** The iterator will be left exhausted: its {@link Iterator#hasNext()} method
   ** will return <code>false</code> aftrewards.
   **
   ** @param  iterator           the {@link Iterator} to return the number of
   **                            remaining elements from.
   **                            <br>
   **                            Allowed object is {@link Iterator}.
   **
   ** @return                    the number of elements remaining.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int size(final @NotNull(message="Iterable cannot be null") Iterator<?> iterator) {
    int count = 0;
    while (iterator.hasNext()) {
      iterator.next();
      count++;
    }
    return count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   at
  /**
   ** Returns the element at the specified position in an {@link Iterable}.
   **
   ** @param  <T>                the type of the {@link Iterable} elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  iterable           the {@link Iterable} as the provider of the
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link Iterable}.
   ** @param  index              the index position of the element to return.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the element at the specified position in
   **                            {@link Iterable}.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or
   **                                   greater than or equal to the size of
   **                                   {@link Iterable}.
   */
  public static <T> T at(final @NotNull(message="Iterable cannot be null") Iterable<T> iterable, final int index) {
    if (iterable instanceof List) {
      return ((List<T>)iterable).get(index);
    }
    else if (iterable instanceof Collection) {
      // can check both ends
      final Collection<T> collection = (Collection<T>)iterable;
      ValidatorUtility.elementIndex(index, collection.size());
    }
    return at(iterable.iterator(), index);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   at
  /**
   ** Advances {@link Iterator} <code>index + 1</code> times, returning the
   ** element at the <code>index</code><sup>th</sup> position.
   **
   ** @param  <T>                the type of the {@link Iterator} elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  iterator           the {@link Iterator} as the provider of the
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link Iterable}.
   ** @param  index              the index position of the element to return.
   **                            <br>
   **                            Allowed object is <code>int</code>.
   **
   ** @return                    the element at the specified position in
   **                            {@link Iterator}.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws IndexOutOfBoundsException if <code>index</code> is negative or
   **                                   greater than or equal to the number of
   **                                   elements remaining in {@link Iterator}.
   */
  public static <T> T at(final @NotNull(message="Iterator cannot be null") Iterator<T> iterator, final int index) {
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
   ** @param  <T>                the type of the array element to be tested.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the argument list of objects to be tested for
   **                            non-<code>null</code>.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    the first non-<code>null</code> object of the
   **                            argument list, or <code>null</code> if there is
   **                            no such element.
   **                            <br>
   **                            Possible object is <code>T</code>.
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
   ** @param  <T>                the type of the array element to be tested.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
	 ** @param  value              the object value to be checked if it equals one
   **                            of the given objects.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  array              the argument list of objects to be tested for
   **                            equality.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    <code>true</code> if the given object equals
   **                            one of the given objects.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
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
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable {@link List} based on the {@link List} passed in
   ** checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between lists.
   **
   ** @param  <T>                the type of the {@link List} element to
   **                            convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  list               the list or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the original list.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> unmodifiable(final List<? extends T> list) {
    return Collections.unmodifiableList(new ArrayList<T>(notNull(list)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified {@link Collection}.
   ** <br>
   ** The {@link List} is backed by the original collection so no copy is made.
   **
   ** @param  <T>                the type of the {@link Collection} element to
   **                            convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the collection or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            collection.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> unmodifiableList(final Collection<? extends T> collection) {
    return Collections.unmodifiableList(list(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified {@link Enumeration}.
   **
   ** @param  <T>                the type of the {@link Enumeration} element to
   **                            convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Enumeration} where
   **                            eachelement is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> unmodifiableList(final Enumeration<T> enumeration) {
    return Collections.unmodifiableList(list(enumeration));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableList
  /**
   ** Returns a unmodifiable {@link List} with the elements obtained from the
   ** specified typed array.
   **
   ** @param  <T>                the type of the array element to collect.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the array of elements or <code>null</code>.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the array elements.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> unmodifiableList(final T... array) {
    return Collections.unmodifiableList(list(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a {@link List} with the elements obtained from the specified
   ** {@link Iterator}.
   **
   ** @param  <E>                the type of the {@link Iterator} to convert.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Iterator} where each
   **                            element is of type <code>E</code>.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>E</code>.
   */
  public static <E> List<E> list(final Iterator<E> iterator) {
    final List<E> result = new ArrayList<E>();
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
   ** @param  <E>                the type of the enumeration.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Enumeration} where
   **                            each element is of type <code>E</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>E</code>.
   */
  public static <E> List<E> list(final Enumeration<E> enumeration) {
    return Collections.list(enumeration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a modifiable {@link List} with the elements obtained from the
   ** specified typed array. The order of the elements remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the array element to collect.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> list(final T... array) {
    final List<T> ret = new ArrayList<T>();
    if (array != null && array.length != 0) {
      for (T t : array)
        ret.add(t);
    }
    return ret;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortedList
  /**
   ** Returns a modifiable sorted {@link List} with the elements obtained from
   ** the specified {@link Collection}.
   **
   ** @param  <T>                the type of the {@link Collection} element to
   **                            convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the modifiable sorted {@link List} with the
   **                            elements obtained from the specified
   **                            {@link Collection}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T extends Object & Comparable<? super T>> List<T> sortedList(final Collection<? extends T> collection) {
    final List<T> list = list(collection);
    Collections.sort(list);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a modifiable {@link List} with the elements obtained from the
   ** specified {@link Collection}.
   **
   ** @param  <T>                the type of the {@link Collection} to check.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified {@link Collection}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> list(final Collection<? extends T> collection) {
    return new ArrayList<T>(notNull(collection));
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  list               the {@link List} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a new instance of {@link ArrayList} if
   **                            <code>list</code> is <code>null</code>;
   **                            otherwise <code>list</code>,
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> List<T> notNull(final List<T> list) {
    return (list == null) ? Collections.EMPTY_LIST : list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNullList
  /**
   ** Protects from <code>null</code> and returns a new instance of
   ** {@link ArrayList} if the parameter <code>collection</code> is
   ** <code>null</code>.
   **
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection}  where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>set</code> is <code>null</code>;
   **                            otherwise <code>set</code>.
   **                            <br>
   **                            Possible object is {@link ArrayList} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> ArrayList<T> notNullList(final Collection<T> collection) {
    return new ArrayList<T>(notNull(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronize
  /**
   ** Returns an synchronized {@link Set} based on the {@link Set} passed in
   ** checks for <code>null</code> and returns an empty set if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the type of the set.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  origin             the set or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the original set.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> synchronize(final Set<? extends T> origin) {
    return Collections.synchronizedSet(new HashSet<T>(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedSet
  /**
   ** Returns an synchronized {@link Set} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the set.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    a synchronized proxy on the original set.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Set<T> synchronizedSet(final T... array) {
    return Collections.synchronizedSet(set(array));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable {@link Set} based on the {@link Set} passed in
   ** checks for <code>null</code> and returns an empty set if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between sets.
   **
   ** @param  <T>                the type of the set.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  set                the set or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the original set.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> unmodifiable(final Set<? extends T> set) {
    return Collections.unmodifiableSet(notNull(set));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableSet
  /**
   ** Returns a unmodifiable {@link Set} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the expected class type of elements in the
   **                            returned {@link Set}.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Set<T> unmodifiableSet(final T... array) {
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
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the collection or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the original
   **                            collection.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> unmodifiableSet(final Collection<T> collection) {
    return Collections.unmodifiableSet(set(collection));
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
   **                            <br>
   **                            Allowed object is {@link Iterator} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> set(final Iterator<T> iterator) {
    final Set<T> result = new TreeSet<T>();
    if (iterator != null)
       while (iterator.hasNext())
         result.add(iterator.next());

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   set
  /**
   ** Returns a modifiable {@link Set} with the elements obtained from the
   ** specified {@link Collection}.
   **
   ** @param  <T>                the expected class type of elements in the
   **                            returned {@link Set}.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified {@link Collection}.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> set(final Collection<T> collection) {
    return new HashSet<T>(notNull(collection));
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of type <code>T</code>.
   **
   ** @return                    the {@link Set} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Set<T> set(final T... array) {
    // default to empty
    final Set<T> set = new LinkedHashSet<T>();
    if (array != null && array.length != 0) {
      // not empty populate the set
      for (T t : array)
        set.add(t);
    }
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseInsensitiveSet
  /**
   ** Creates a case-insensitive modifiable {@link Set}.
   **
   ** @return                    an empty case-insensitive set.
   **                            <br>
   **                            Possible object is {@link SortedSet} where each
   **                            element is of type {@link String}.
   */
  public static SortedSet<String> caseInsensitiveSet() {
    return new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCaseInsensitive
  /**
   ** Returns <code>true</code> if the given {@link Set} is a case-insensitive
   ** {@link Set}.
   **
   ** @param  set                the {@link Set} to verify.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type any.
   **
   ** @return                    <code>true</code> if the given {@link Set} is a
   **                            case-insensitive {@link Set}.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean isCaseInsensitive(final Set<?> set) {
    if (set instanceof SortedSet) {
      final SortedSet<?>  sortedSet = (SortedSet<?>)set;
      final Comparator<?> comp = sortedSet.comparator();
      if (comp.equals(String.CASE_INSENSITIVE_ORDER))
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronize
  /**
   ** Returns an synchronized {@link Map} with the elements obtained from the
   ** specified {@link Map}. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <K>                the type of the key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  origin             the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    a synchronized proxy on the original set.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> synchronize(final Map<K, V> origin) {
    return Collections.unmodifiableMap(new HashMap<K, V>(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   synchronizedMap
  /**
   ** Returns an synchronized {@link Map} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the array to convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is two dimensional array of type
   **                            <code>T</code>.
   **
   ** @return                    a synchronized proxy on the original set.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <T> Map<T, T> synchronizedMap(final T[][] array) {
    final Map<T, T> map = new HashMap<T, T>();
    for (int i = 0; array != null && i < array.length; i++) {
      map.put(array[i][0], array[i][1]);
    }
    return Collections.synchronizedMap(map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiable
  /**
   ** Returns a unmodifiable {@link Map} with the elements obtained from the
   ** specified {@link Map}. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <K>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  origin             the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>K</code> as the value.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link Map}.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>K</code> as the value.
   */
  public static <T, K> Map<T, K> unmodifiable(final Map<T, K> origin) {
    return Collections.unmodifiableMap(new HashMap<T, K>(notNull(origin)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmodifiableMap
  /**
   ** Returns a unmodifiable {@link Map} with the elements obtained from the
   ** specified typed array. The order of the elemants remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the key and value value of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is two dimensional array of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified typed array.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>T</code> as the value.
   */
  public static <T> Map<T, T> unmodifiableMap(final T[][] array) {
    final Map<T, T> map = new LinkedHashMap<T, T>();
    for (int i = 0; array != null && i < array.length; i++) {
      map.put(array[i][0], array[i][1]);
    }
    return Collections.<T, T>unmodifiableMap(map);
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  set                the {@link Set} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>set</code> is <code>null</code>;
   **                            otherwise <code>set</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Set<T> notNull(final Set<T> set) {
    return (set == null) ? Collections.EMPTY_SET : set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNullSet
  /**
   ** Protects from <code>null</code> and returns a new instance of
   ** {@link HashSet} if the parameter <code>collection</code> is
   ** <code>null</code>.
   **
   ** @param  <T>                the type of the {@link Collection} to check.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection}  where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>set</code> is <code>null</code>;
   **                            otherwise <code>set</code>.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Set<T> notNullSet(final Collection<T> collection) {
    return new HashSet<T>(notNull(collection));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
  /**
   ** Returns a modifiable {@link Map} with the elements obtained from the
   ** specified <code>k</code> and <code>v</code> as the value pair.
   **
   ** @param  <K>                the expected class type of the key element.
   **                            <br>
   **                            Allowed object is <code>&lt;k&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k                  the key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v                  the value mapped to the key of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k, final V v) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k0                 the first key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k0, final V v0, final K k1, final V v1) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k0                 the first key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k0                 the first key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k0                 the first key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k4                 the fiveth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v4                 the fiveth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k0                 the first key value of the created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v0                 the first value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k1                 the second key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v1                 the second value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k2                 the third key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v2                 the third value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k3                 the forth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v3                 the forth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k4                 the fiveth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v4                 the fiveth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  k5                 the sixth key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  v5                 the sixth value mapped to the first key of the
   **                            created {@link Map}.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified parameters.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the expected class type of the value element.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Iterator} where each
   **                            element is of type <code>K</code>.
   ** @param  map                the {@link Map} providing the source
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link Map} that had an
   **                            mapping to an element of {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K,V> Map<K,V> map(final Iterator<K> iterator, final Map<K,V> map) {
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
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  k                  the array providing the key values of
   **                            the created {@link Map}.
   **                            <br>
   **                            Allowed object is array of <code>K</code>.
   ** @param  v                  the array providing the values mapped to
   **                            the keys of the created {@link Map}.
   **                            <br>
   **                            Allowed object is array of <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of the arrays.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final K[] k, final V[] v) {
    // throw if there's invalid input
    if (k.length != v.length)
      throw new IllegalArgumentException(SystemBundle.string(SystemError.ARGUMENT_SIZE_MISMATCH));

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
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  keys               the {@link List} providing the key values of
   **                            the created {@link Map}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>K</code>.
   ** @param  values             the {@link List} providing the values mapped to
   **                            the keys of the created {@link Map}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>V</code>.
   **
   ** @return                    the {@link Map} with the elements obtained
   **                            from the specified {@link List}s that had an
   **                            mapping to an element of the {@link List}s.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   */
  public static <K, V> Map<K, V> map(final List<K> keys, final List<V> values) {
    // throw if there's invalid input
    if (keys.size() != values.size())
      throw new IllegalArgumentException(SystemBundle.string(SystemError.ARGUMENT_SIZE_MISMATCH));

    final Map<K, V>   map = new HashMap<K, V>(keys.size());
    final Iterator<K> kit   = keys.iterator();
    final Iterator<V> vit   = values.iterator();
    while (kit.hasNext() && vit.hasNext())
      map.put(kit.next(), vit.next());

    return map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   caseInsensitiveMap
  /**
   ** Creates a case-insenstive modifiable {@link Map}.
   **
   ** @param <V>                 the object type of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   **
   ** @return                    an empty case-insenstive modifiable
   **                            {@link Map}.
   **                            <br>
   **                            Possible object is {@link SortedMap} where each
   **                            element is of type {@link String} for the key
   **                            and <code>V</code> as the value.
   */
  public static <V> SortedMap<String, V> caseInsensitiveMap() {
    return new TreeMap<String, V>(String.CASE_INSENSITIVE_ORDER);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isCaseInsensitive
  /**
   ** Returns <code>true</code> if the given {@link Map} is a case-insensitive
   ** {@link Map}.
   **
   ** @param  <K>                the type of the key value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  map                the {@link Map}.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    <code>true</code> if the given {@link Map} is a
   **                            case-insensitive {@link Map}.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static <K, V> boolean isCaseInsensitive(final Map<K, V> map) {
    if (map instanceof SortedMap) {
      final SortedMap<K, V> sorted  = (SortedMap<K, V>)map;
      final Comparator<?>   compare = sorted.comparator();
      if (compare.equals(String.CASE_INSENSITIVE_ORDER))
        return true;
    }
    return false;
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <K>                the type of the value value of the created
   **                            {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  map                the {@link Map} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    a new instance of {@link HashMap} if
   **                            <code>map</code> is <code>null</code>;
   **                            otherwise <code>map</code>,
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>K</code> as the value.
   */
  @SuppressWarnings("unchecked")
  public static <T, K> Map<T, K> notNull(final Map<T, K> map) {
    return (map == null) ? Collections.EMPTY_MAP : map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Map} is empty.
   **
   ** @param  <K>                the type of the key property of the {@link Map}
   **                            to check.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value property of the
   **                            {@link Map} to
   **                            check.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  collection         the {@link Map} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> as the key
   **                            and for <code>V</code> the value.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static <K, V> boolean empty(final Map<K, V> collection) {
    return collection == null || collection.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   empty
  /**
   ** Checks if the specified {@link Collection} is empty.
   **
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static <T> boolean empty(final Collection<T> collection) {
    return collection == null || collection.isEmpty();
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  collection         the {@link Collection} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a new instance of {@link HashSet} if
   **                            <code>collection</code> is <code>null</code>;
   **                            otherwise <code>collection</code>,
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> Collection<T> notNull(final Collection<T> collection) {
    return collection == null ? new HashSet<T>() : collection;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjoined
  /**
   ** Returns <code>true</code> if the two specified collections have no
   ** elements in common.
   ** <p>
   ** Care must be exercised if this method is used on collections that do not
   ** comply with the general contract for {@link Collection}.
   ** <br>
   ** Implementations may elect to iterate over either collection and test for
   ** containment in the other collection (or to perform any equivalent
   ** computation). If either collection uses a nonstandard equality test (as
   ** does a {@link SortedSet} whose ordering is not <em>compatible with
   ** equals</em>, or the key set of a {@link HashMap}), both collections must
   ** use the same nonstandard equality test, or the result of this method is
   ** undefined.
   ** <p>
   ** Care must also be exercised when using collections that have restrictions
   ** on the elements that they may contain. Collection implementations are
   ** allowed to throw exceptions for any operation involving elements they deem
   ** ineligible. For absolute safety the specified collections should contain
   ** only elements which are eligible elements for both collections.
   ** <p>
   ** Note that it is permissible to pass the same collection in both
   ** parameters, in which case the method will return {@code true} if and only
   ** if the collection is empty.
   **
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}, must not be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link Collection}, must not be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    <code>true</code> if the two specified
   **                            collections have no elements in common.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException if either collection is <code>null</code> or
   **                              if one collection contains a
   **                              <code>null</code> element and
   **                              <code>null</code> is not an eligible element
   **                              for the other collection.
   ** @throws ClassCastException   if one collection contains an element that is
   **                              of a type which is ineligible for the other
   **                              collection.
   */
  public static <T> boolean disjoined(final Collection<T> c1, final Collection<T> c2) {
    return Collections.disjoint(c2, c1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjunction
  /**
   ** Returns the exclusive symmetric difference of two {@link Collection}s as
   ** an unmodifiable {@link Set}.
   ** <p>
   ** The cardinality of each element in the returned {@link Set} will be equal
   ** to <code>max(cardinality(e,c1),cardinality(e,c2)) - min(cardinality(e,c1),cardinality(e,c2))</code>.
   ** <br>
   ** This is equivalent to
   ** <code>minus(union(c1,c2),intersection(c1,c2)) or union(minus(c1,c2),minus(c2,c1))</code>.
   **
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the the symmetric difference of the two
   **                            {@link Collection}s.
   **                            <br>
   **                            Possible object is {@link Set} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> Set<T> disjunction(final Collection<T> c1, final Collection<T> c2) {
    return Collections.<T>unmodifiableSet(union(minus(c1, c2), minus(c2, c1)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minus
  /**
   ** Returns the symmetric difference of two {@link Collection}s as an
   ** unmodifiable {@link Set}.
   ** <p>
   ** The cardinality of each element in the returned {@link Set} will be the
   ** cardinality of <code>c1 - c2</code>.
   **
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the the symmetric difference of the two
   **                            {@link Collection}s.
   **                            <br>
   **                            Possible object is {@link Set} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> Set<T> minus(final Collection<T> c1, final Collection<T> c2) {
    final Set<T> minus = set(c1);
    minus.removeAll(c2);

    return Collections.<T>unmodifiableSet(minus);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union
  /**
   ** Returns the union of two {@link List}s as an unmodifiable {@link List}.
   **
   ** @param  <T>                the type of the collections.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link List}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link List}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the union  of the two {@link List}s.
   **                            <br>
   **                            Possible object is {@link List} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> List<T> union(final List<T> c1, final List<T> c2) {
    final List<T> union = list(c1);
    union.addAll(c2);

    return Collections.<T>unmodifiableList(union);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union
  /**
   ** Returns the union of two {@link Collection}s as an unmodifiable
   ** {@link Set}.
   ** <p>
   ** The cardinality of each element in the returned {@link Set} will be equal
   ** to the maximum of the cardinality of that element in the two given
   ** {@link Collection}s.
   **
   ** @param  <T>                the type of the collections.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the union  of the two {@link Collection}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> Set<T> union(final Collection<T> c1, final Collection<T> c2) {
    final Set<T> union = set(c1);
    union.addAll(c2);

    return Collections.<T>unmodifiableSet(union);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intersection
  /**
   ** Returns intersection of two {@link Collection}s as an unmodifiable
   ** {@link Set}.
   ** <p>
   ** The cardinality of each element in the returned {@link Set} will be equal
   ** to the minimum of the cardinality of that element in the two given
   ** {@link Collection}s.
   **
   ** @param  <T>                the type of the collections.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   ** @param  c2                 the second {@link Collection}, may be
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    the intersection of the two
   **                            {@link Collection}s.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type <code>T</code>.
   */
  public static <T> Set<T> intersection(final Collection<T> c1, final Collection<T> c2) {
    final Set<T> intersection = set(c1);
    intersection.retainAll(c2);
    return Collections.<T>unmodifiableSet(intersection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
  /**
   ** hashCode function that properly handles arrays, collections, maps,
   ** collections of arrays, and maps of arrays.
   **
   ** @param  o                  the object.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the hashCode.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public static int hashCode(final Object o) {
    if (o == null)
      return 0;
    else if (o.getClass().isArray()) {
      int length = Array.getLength(o);
      int rv = 0;
      for (int i = 0; i < length; i++) {
        Object el = Array.get(o, i);
        rv += hashCode(el);
      }
      return rv;
    }
    else if (o instanceof Collection) {
      Collection<?> l = (Collection<?>)o;
      int rv = 0;
      for (Object el : l) {
        rv += hashCode(el);
      }
      return rv;
    }
    else if (o instanceof Map) {
      Map<?, ?> map = (Map<?, ?>)o;
      return hashCode(map.values());
    }
    else
      return o.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Equality function that properly handles arrays, lists, maps, lists of
   ** arrays, and maps of arrays.
   ** <p>
   ** <b>NOTE</b>:
   ** <br>
   ** For {@link Set}s, this relies on the equals method of the {@link Set} to
   ** do the right thing. This is a reasonable assumption since, in order for
   ** {@link Set}s to behave properly as {@link Set}s, their values must already
   ** have a proper implementation of <code>equals</code>. (Or they must be
   ** specialized {@link Set}s that define a custom comparator that knows how to
   ** do the right thing). The same holds true for {@link Map} keys. {@link Map}
   ** values, on the other hand, are compared (so {@link Map} values can be
   ** arrays).
   **
   ** @param  o1                 the first object.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  o2                 the second object.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the two objects are equal.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean equals(final Object o1, final Object o2) {
    return equals(o1, o2, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Equality function that properly handles arrays, lists, maps, lists of
   ** arrays, and maps of arrays.
   ** <p>
   ** <b>NOTE</b>:
   ** <br>
   ** For {@link Set}s, this relies on the equals method of the {@link Set} to
   ** do the right thing. This is a reasonable assumption since, in order for
   ** {@link Set}s to behave properly as {@link Set}s, their values must already
   ** have a proper implementation of <code>equals</code>. (Or they must be
   ** specialized {@link Set}s that define a custom comparator that knows how to
   ** do the right thing). The same holds true for {@link Map} keys. {@link Map}
   ** values, on the other hand, are compared (so {@link Map} values can be
   ** arrays).
   **
   ** @param  o1                 the first object.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  o2                 the second object.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param ignoreCase          if <code>true</code> the String and Character
   **                            comparison is case-ignore.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    <code>true</code> if the two objects are equal.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static boolean equals(final Object o1, final Object o2, final boolean ignoreCase) {
    //same object or both null
    if (o1 == o2)
      return true;
    else if (o1 == null)
      return false;
    else if (o2 == null)
      return false;
    else if (o1.getClass().isArray()) {
      final Class<?> clazz1 = o1.getClass();
      final Class<?> clazz2 = o2.getClass();
      if (!clazz1.equals(clazz2))
        return false;

      int length1 = Array.getLength(o1);
      int length2 = Array.getLength(o2);
      if (length1 != length2)
        return false;

      for (int i = 0; i < length1; i++) {
        Object el1 = Array.get(o1, i);
        Object el2 = Array.get(o2, i);
        if (!equals(el1, el2, ignoreCase))
          return false;
      }
      return true;
    }
    else if (o1 instanceof List) {
      if (o2 instanceof List) {
        final List<?> l1 = (List<?>)o1;
        final List<?> l2 = (List<?>)o2;
        if (l1.size() != l2.size())
          return false;

        for (int i = 0; i < l1.size(); i++) {
          Object el1 = l1.get(i);
          Object el2 = l2.get(i);
          if (!equals(el1, el2, ignoreCase))
            return false;
        }
        return true;
      }
      else
        return false;
    }
    else if (o1 instanceof Set) {
      if (o2 instanceof Set) {
        // rely on Set equality.
        // this does not handle the case of arrays within sets, but arrays
        // should not be placed within sets unless the set is a specialized set
        // that knows how to compare arrays
        return o1.equals(o2);
      }
      else
        return false;
    }
    else if (o1 instanceof Map) {
      if (o2 instanceof Map) {
        final Map<?, ?> m1 = (Map<?, ?>)o1;
        final Map<?, ?> m2 = (Map<?, ?>)o2;
        if (m1.size() != m2.size())
          return false;

        for (Map.Entry<?, ?> entry1 : m1.entrySet()) {
          final Object key1 = entry1.getKey();
          final Object val1 = entry1.getValue();
          if (!m2.containsKey(key1))
            return false;

          Object val2 = m2.get(key1);
          if (!equals(val1, val2, ignoreCase))
            return false;
        }
        return true;
      }
      else
        return false;
    }
    else if (ignoreCase && o1 instanceof String && o2 instanceof String) {
      return ((String)o1).equalsIgnoreCase((String) o2);
    }
    else if (ignoreCase && o1 instanceof Character && o2 instanceof Character) {
      return Character.toLowerCase((Character) o1) == Character.toLowerCase((Character) o2);
    }
    else
      return o1.equals(o2);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   haveSameElements
  /**
   ** Returns if both {@link Collection Collections} contains the same elements,
   ** in the same quantities, regardless of order and collection type.
   ** <p>
   ** Empty collections and <code>null</code> are regarded as equal.
   **
   ** @param  <T>                the expected class type of the element.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  c1                 the first {@link Collection}.
   **                            May be <code>null</code>.
   ** @param  c2                 the second {@link Collection}.
   **                            May be <code>null</code>.
   **
   ** @return                    <code>true</code> if the
   **                            {@link Collection Collections} containing the
   **                            same elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static <T> boolean haveSameElements(final Collection<T> c1, final Collection<T> c2) {
    if (c1 == c2)
      return true;

    // if either list is null, return whether the other is empty
    if (c1 == null)
      return c2.isEmpty();
    if (c2 == null)
      return c1.isEmpty();

    // if lengths are not equal, they can't possibly match
    if (c1.size() != c2.size())
      return false;

    // helper class, so we don't have to do a whole lot of autoboxing
    class Count {
      // initialize as 1, as we would increment it anyway
      private int count = 1;
    }

    final Map<T, Count> counts = new HashMap<>();

    // count the items in collection 1
    for (final T item : c1) {
      final Count count = counts.get(item);
      if (count != null)
        count.count++;
      else
        // if the map doesn't contain the item, put a new count
        counts.put(item, new Count());
    }

    // subtract the count of items in collection 2
    for (final T item : c2) {
      final Count count = counts.get(item);
      // if the map doesn't contain the item, or the count is already reduced to
      // 0, the lists are unequal
      if (count == null || count.count == 0)
        return false;
      count.count--;
    }

    // if any count is nonzero at this point, then the two lists don't match
    for (final Count count : counts.values())
      if (count.count != 0)
        return false;

    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value to which the specified key is mapped, or
   ** {@code defaultValue} if the given map contains no mapping for the key.
   ** <p>
   ** More formally, if the given map contains a mapping from a key {@code k} to
   ** a value {@code v} such that {@code (key==null ? k==null : key.equals(k))},
   ** then this method returns {@code v}; otherwise it returns
   ** {@code defaultValue}.
   ** <br>
   ** (There can be at most one such mapping.)
   **
   ** @param  <K>                the type of the key value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  map                the map the value for a key has to be returned.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   ** @param  key                the key whose associated value is to be
   **                            returned from the given map.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  defaultValue       the value returned if no value is mapped for
   **                            <code>key</code> in the specified
   **                            <code>map</code>.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or {@code defaultValue} if the given map
   **                            contains no mapping for the key.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  public static <K, V> V value(final Map<K, V> map, final K key, final V defaultValue) {
    V value = map.get(key);
    if (value == null || StringUtility.blank(value.toString()))
      value = defaultValue;

    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value to which the specified key is mapped, or
   ** <code>null</code> if the given map contains no mapping for the key.
   ** <p>
   ** More formally, if the given map contains a mapping from a key {@code k} to
   ** a value {@code v} such that {@code (key==null ? k==null : key.equals(k))},
   ** then this method returns {@code v}; otherwise it returns {@code null}.
   ** <br>
   ** (There can be at most one such mapping.)
   **
   ** @param  <K>                the type of the key value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  map                the map the value for a key has to be returned.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   ** @param  key                the key whose associated value is to be
   **                            returned from the given map.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or {@code null} if the given map contains no
   **                            mapping for the key.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  public static <K, V> V value(final Map<K, V> map, final K key) {
    return map.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   valueRequired
  /**
   ** Returns the value to which the specified key must be mapped and the value
   ** cannot be empty.
   ** <p>
   ** More formally, the given map must contains a mapping from a key {@code k}
   ** to a value {@code v} such that
   ** {@code (key==null ? k==null : key.equals(k))}, then this method returns
   ** {@code v}.
   ** <br>
   ** (There can be at most one such mapping.)
   **
   ** @param  <K>                the type of the key value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the {@link Map}.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  map                the map the value for a key has to be returned.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   ** @param  key                the key whose associated value is to be
   **                            returned from the given map.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the value to which the specified key is mapped.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws RuntimeException   when the key is not defined in the map or the
   **                            value is empty.
   */
  public static <K, V> V valueRequired(final Map<K, V> map, final K key) {
    final V value = map.get(key);
    if (value == null || (value instanceof String && StringUtility.blank((String)value)))
      throw new RuntimeException(String.format("The value for a key [%s] is not defined in the provided map.", key));

    return value;
  }
}