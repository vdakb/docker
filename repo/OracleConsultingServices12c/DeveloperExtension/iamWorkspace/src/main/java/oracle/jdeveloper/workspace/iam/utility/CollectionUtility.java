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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   StringUtility.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    StringUtility.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    11.1.1.3.37.56.13  2011-05-14  DSteding    First release version
    12.2.1.3.42.60.74  2018-05-15  DSteding    Migration to JDeveloper 12c
*/

package oracle.jdeveloper.workspace.iam.utility;

import java.lang.reflect.Array;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;

////////////////////////////////////////////////////////////////////////////////
// abstract class CollectionUtility
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Miscellaneous collection utility methods. Mainly for internal use.
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 12.2.1.3.42.60.74
 ** @since   11.1.1.3.37.56.13
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
  // Method:   empty
  /**
   ** Checks if the specified {@link Map} is empty.
   **
   ** @param  <K>                the type of the key value of the {@link Map} to
   **                            check.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value value of the {@link Map}
   **                            to check.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  collection         the {@link Map} to check for emptyness.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    <code>true</code> if the collection contains no
   **                            elements.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public static <K,V> boolean empty(final Map<K,V> collection) {
    return collection == null || collection.isEmpty();
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
  public static boolean equals(final Object o1, final Object o2, boolean ignoreCase) {
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
  // Method:   unmodifiable
  /**
   ** Returns an unmodifiable {@link List} based on the {@link List} passed in
   ** checks for <code>null</code> and returns an empty list if
   ** <code>null</code> is passed in. This one ensures that the order is
   ** maintained between lists.
   **
   ** @param  <T>                the type of the list.
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
   ** @param  <T>                the type of the enumeration.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Enumeration} where
   **                            each element is of type <code>T</code>.
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
   ** @param  <T>                the type of the collection.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the array of elements or <code>null</code>.
   **                            <br>
   **                            Allowed object is array of <code>T</code>.
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
   ** @param  <T>                the type of the {@link Iterator} to convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  iterator           the {@link Iterator} where the elements are
   **                            obtained from.
   **                            <br>
   **                            Allowed object is {@link Iterator} of type
   **                            <code>T</code>.
   **
   ** @return                    the {@link List} with the elements obtained
   **                            from the specified {@link Iterator}.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> list(final Iterator<T> iterator) {
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
   ** @param  <T>                the type of the enumeration.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  enumeration        the enumeration or <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Enumeration} of type
   **                            <code>T</code>.
   **
   ** @return                    a unmodifiable proxy on the enumeration
   **                            elements.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> List<T> list(final Enumeration<T> enumeration) {
    return Collections.list(enumeration);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns a modifiable {@link List} with the elements obtained from the
   ** specified typed array. The order of the elements remains as they are
   ** provided by the elements in the specified typed array.
   **
   ** @param  <T>                the type of the array to convert.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  array              the values where the elements are obtained
   **                            from.
   **                            <br>
   **                            Allowed object is array of <code>T</code>.
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
   **                            Allowed object is {@link Collection} of type
   **                            <code>T</code>.
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
   **                            Allowed object is array of <code>T</code>.
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
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
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
   **                            Allowed object is array of <code>T</code>.
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
    final Set<T> ret = new LinkedHashSet<T>();
    if (array != null && array.length != 0) {
      // not empty populate the set..
      for (T t : array)
        ret.add(t);
    }
    return ret;
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
      throw new IllegalArgumentException();

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
   ** @param  k                  the {@link List} providing the key values of
   **                            the created {@link Map}.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>K</code>.
   ** @param  v                  the {@link List} providing the values mapped to
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
  public static <K, V> Map<K, V> map(final List<K> k, final List<V> v) {
    // throw if there's invalid input
    if (k.size() != v.size())
      throw new IllegalArgumentException();

    final Map<K, V>   map = new HashMap<K, V>(k.size());
    final Iterator<K> kit   = k.iterator();
    final Iterator<V> vit   = v.iterator();
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
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of
   ** {@link HashSet} if the parameter <code>set</code> is <code>null</code>.
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
  public static <T> Set<T> notNull(final Set<T> set) {
    return (set == null) ? new HashSet<T>() : set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   notNull
  /**
   ** Protects from <code>null</code> and returns a new instance of
   ** {@link HashSet} if the parameter <code>collection</code> is
   ** <code>null</code>. Otherwise return the parameter that was passed in.
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
}