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

    File        :   SystemProperty.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SystemProperty.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.utility;

import java.util.Set;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.Collection;

import java.util.function.BiFunction;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import oracle.hst.platform.core.SystemError;
import oracle.hst.platform.core.SystemBundle;

//////////////////////////////////////////////////////////////////////////////
// class SystemProperty
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Representation of a system property.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request extending
 **                              this class (requests can return their own
 **                              specific type instead of type defined by this
 **                              class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SystemProperty<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Cache<String, String>                                  CACHE      = new Cache<String, String>();
  private static final Set<Class>                                             NILABLE    = new HashSet<>(Arrays.asList(String.class, Class.class));
  private static final Map<Class, BiFunction<String, SystemProperty, Object>> INBOUND    = new HashMap<>();
  private static final Map<String, SystemProperty>                            PROPERTIES = new ConcurrentHashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    INBOUND.put(Long.class,    (value, systemProperty) -> parseLong(value));
    INBOUND.put(String.class,  (value, systemProperty) -> value);
    INBOUND.put(Double.class,  (value, systemProperty) -> parseDouble(value));
    INBOUND.put(Integer.class, (value, systemProperty) -> parseInteger(value));
    INBOUND.put(Boolean.class, (value, systemProperty) -> parseBoolean(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Class<T> clazz;
  private final String   name;
  private final T        initialValue;
  private final T        maximumValue;
  private final T        minimumValue;
  private final T        defaultValue;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Cache
  // ~~~~~ ~~~~~
  /**
   ** An arbitrary pair of objects. Convenient implementation of Map.
   **
   ** @param <K>                 the type of keys maintained by this map.
   ** @param <V>                 the type of mapped values.
   */
  private static class Cache<K, V> implements ConcurrentMap<K, V> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The arbitrary values of the instance where this wrapper belongs to.
     */
    protected final ConcurrentMap<K, V> delegate = new ConcurrentHashMap<K, V>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Cache</code> discoverer that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Cache()" and enforces use of the public method below.
     */
    private Cache() {
      // ensure inheritance
      super();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: size (Map)
    /**
     ** Returns the number of key-value mappings in the backing map. If the
     ** backing map contains more than <code>Integer.MAX_VALUE</code> elements,
     ** returns <code>Integer.MAX_VALUE</code>.
     **
     ** @return                  the number of key-value mappings in the
     **                          backing map.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public final int size() {
      return (this.delegate != null) ? this.delegate.size() : 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isEmpty (Map)
    /**
     ** Returns <code>true</code> if the backing map contains no key-value
     ** mappings.
     **
     ** @return                  <code>true</code> if the backing map contains
     **                          no key-value mappings.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean isEmpty() {
      return (this.delegate != null) ? this.delegate.isEmpty() : true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear (Map)
    /**
     ** Removes all mappings from the backing map (optional operation).
     **
     ** @throws UnsupportedOperationException if the <code>clear</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final void clear() {
      if (this.delegate != null)
        this.delegate.clear();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsKey (Map)
    /**
     ** Returns <code>true</code> if the backing map contains a mapping for
     ** the specified key. More formally, returns <code>true</code> if and only
     ** if the backing map contains a mapping for a key <code>k</code> such
     ** that <code>(key==null ? k==null : key.equals(k))</code>.
     ** (There can be at most one such mapping.)
     **
     ** @param  key              key whose presence in the backing map is to
     **                          be tested.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if the backing map contains a
     **                          mapping for the specified key.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws NullPointerException if the specified map is <code>null</code>,
     **                              or if the backing {@link Map} does not
     **                              permit <code>null</code> keys or values.
     ** @throws ClassCastException   if the key is of an inappropriate type for
     **                              the backing map (optional).
     */
    @Override
    public final boolean containsKey(final Object key) {
      return (this.delegate != null) ? this.delegate.containsKey(key) : false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsValue (Map)
    /**
     ** Returns <code>true</code> if the backing map maps one or more keys to
     ** the specified value. More formally, returns <code>true</code> if and
     ** only if the backing map contains at least one mapping to a value
     ** <code>v</code> such that
     ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
     ** will probably require time linear in the map size for most
     ** implementations of the <code>Map</code> interface.
     **
     ** @param  value            value whose presence in the backing map is to
     **                          be tested.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if the backing map maps one
     **                          or more keys to the specified value.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws NullPointerException if the value is <code>null</code> and the
     **                              backing map does not permit
     **                              <code>null</code> values (optional).
     ** @throws ClassCastException   if the value is of an inappropriate type
     **                              for the backing map (optional).
     */
    @Override
    public final boolean containsValue(final Object value) {
      return (this.delegate != null) ? this.delegate.containsValue(value) : false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (Map)
    /**
     ** Returns the value to which the backing map maps the specified key.
     ** Returns <code>null</code> if the map contains no mapping for this key. A
     ** return value of <code>null</code> does not <i>necessarily</i> indicate
     ** that the map contains no mapping for the key; it's also possible that
     ** the backing map explicitly maps the key to <code>null</code>. The
     ** <code>containsKey</code> operation may be used to distinguish these two
     ** cases.
     ** <p>
     ** More formally, if the backing map contains a mapping from a key
     ** <code>k</code> to a value <code>v</code> such that
     ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
     ** returns <code>v</code>; otherwise it returns <code>null</code>.
     ** (There can be at most one such mapping.)
     **
     ** @param  key              key whose associated value is to be returned.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  the value to which the backing map maps the
     **                          specified key, or <code>null</code> if the map
     **                          contains no mapping for this key.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws ClassCastException   if the key is of an inappropriate type for
     **                              the backing map (optional).
     ** @throws NullPointerException if the key is <code>null</code> and the
     **                              backing map does not permit
     **                              <code>null</code> keys (optional).
     **
     ** @see    #containsKey(Object)
     */
    @Override
    public final V get(final Object key) {
      return (this.delegate != null) ? this.delegate.get(key) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: put (Map)
    /**
     ** Associates the specified value with the specified key in the backing
     ** map (optional operation). If the backing map previously contained a
     ** mapping for this key, the old value is replaced by the specified value.
     ** (A map <code>m</code> is said to contain a mapping for a key
     ** <code>k</code> if and only if {@link #containsKey(Object) m.containsKey(k)}
     ** would return <code>true</code>.))
     **
     ** @param  key              key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            value to be associated with the specified key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  previous value associated with specified key,
     **                          or <code>null</code> if there was no mapping
     **                          for key. A <code>null</code> return can also
     **                          indicate that the map previously associated
     **                          <code>null</code> with the specified key, if
     **                          the implementation supports <code>null</code>
     **                          values.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws ClassCastException            if the class of the specified key
     **                                       or value prevents it from being
     **                                       stored in the backing
     **                                       {@link Map}.
     ** @throws IllegalArgumentException      if some aspect of this key or
     **                                       value prevents it from being
     **                                       stored in the backing
     **                                       {@link Map}.
     ** @throws NullPointerException          if the backing {@link Map} does
     **                                       not permit <code>null</code> keys
     **                                       or values, and the specified key
     **                                       or value is <code>null</code>.
     ** @throws UnsupportedOperationException if the <code>put</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final V put(final K key, final V value) {
      return (this.delegate != null) ? this.delegate.put(key, value) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   putAll (Map)
    /**
     ** Copies all of the mappings from the specified map to the backing map
     ** (optional operation). The effect of this call is equivalent to that of
     ** calling <code>put(K, V)</code> on the backing map once for each
     ** mapping from key <code>k</code> to value <code>v</code> in the specified
     ** map. The behavior of this operation is unspecified if the specified map
     ** is modified while the operation is in progress.
     **
     ** @param  t                mappings to be stored in the backing map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type <code>K</code> for the key
     **                          and <code>V</code> for the value.
     **
     ** @throws ClassCastException            if the class of a key or value in
     **                                       the specified map prevents it from
     **                                       being stored in the backing
     **                                       {@link Map}.
     ** @throws IllegalArgumentException      some aspect of a key or value in
     **                                       the specified map prevents it from
     **                                       being stored in the backing
     **                                       {@link Map}.
     ** @throws NullPointerException          if the specified map is
     **                                       <code>null</code>, or if the
     **                                       backing map does not permit
     **                                       <code>null</code> keys or values,
     **                                       and the specified map contains
     **                                       <code>null</code> keys or values.
     ** @throws UnsupportedOperationException if the <code>putAll</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final void putAll(final Map<? extends K, ? extends V> t) {
      if (this.delegate != null)
        this.delegate.putAll(t);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (Map)
    /**
     ** Removes the mapping for this key from the backing map if it is present
     ** (optional operation).
     ** <p>
     ** More formally, if the backing map contains a mapping from key
     ** <code>k</code> to value <code>v</code> such that
     ** <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is
     ** removed. (The map can contain at most one such mapping.)
     ** <p>
     ** Returns the value to which the map previously associated the key, or
     ** <code>null</code> if the map contained no mapping for this key. (A
     ** <code>null</code> return can also indicate that the map previously
     ** associated <code>null</code> with the specified key if the
     ** implementation supports <code>null</code> values.) The map will not
     ** contain a mapping for the specified key once the call returns.
     **
     ** @param  key              key whose mapping is to be removed from the
     **                          map.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return previous         value associated with specified key, or
     **                          <code>null</code> if there was no mapping for
     **                          key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @throws ClassCastException            if the key is of an inappropriate
     **                                       type for the backing map
     **                                       (optional).
     ** @throws NullPointerException          if the key is <code>null</code>
     **                                       and the backing map does not
     **                                       permit <code>null</code> keys
     **                                       (optional).
     ** @throws UnsupportedOperationException if the <code>remove</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final V remove(final Object key) {
      return (this.delegate != null) ? this.delegate.remove(key) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: keySet (Map)
    /**
     ** Returns a set view of the keys contained in the backing map. The set
     ** is backed by the backing map, so changes to backing map are
     ** reflected in the set, and vice-versa. If backing map is modified while
     ** an iteration over the set is in progress (except through the iterator's
     ** own <code>remove</code> operation), the results of the iteration are
     ** undefined. The set supports element removal, which removes the
     ** corresponding mapping from backing map, via the
     ** <code>Iterator.remove</code>, <code>Set.remove</code>,
     ** <code>removeAll</code> <code>retainAll</code>, and <code>clear</code>
     ** operations. It does not support the add or <code>addAll</code>
     ** operations.
     **
     ** @return                  a set view of the keys contained in the
     **                          backing map.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type <code>K</code>.
     */
    @Override
    public final Set<K> keySet() {
      return (this.delegate != null) ? this.delegate.keySet() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (Map)
    /**
     ** Returns a collection view of the values contained in the backing map.
     ** The collection is backed by the backing map, so changes to the
     ** backing map are reflected in the collection, and vice-versa. If the
     ** backing map is modified while an iteration over the collection is in
     ** progress (except
     ** through the iterator's own <code>remove</code> operation), the results
     ** of the iteration are undefined. The collection supports element removal,
     ** which removes the corresponding mapping from the backing map, via the
     ** <code>Iterator.remove</code>, <code>Collection.remove</code>,
     ** <code>removeAll</code>, <code>retainAll</code> and <code>clear</code>
     ** operations. It does not support the add or <code>addAll</code>
     ** operations.
     **
     ** @return                  a collection view of the values contained in
     **                          the backing map.
     **                          <br>
     **                          Possible object is {@link Collection} where
     **                          each element is of type <code>V</code>.
     */
    @Override
    public final Collection<V> values() {
      return (this.delegate != null) ? this.delegate.values() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entrySet (Map)
    /**
     ** Returns a set view of the mappings contained in the backing map. Each
     ** element in the returned set is a <code>Map.Entry</code>. The set is
     ** backed by the backing map, so changes to the map are reflected in the
     ** set, and vice-versa. If the backing map is modified while an iteration
     ** over the set is in progress (except through the iterator's own
     ** <code>remove</code> operation, or through the <code>setValue</code>
     ** operation on a map entry returned by the iterator) the results of the
     ** iteration are undefined. The set supports element removal, which removes
     ** the corresponding mapping from the backing map, via the
     ** <code>Iterator.remove</code>, <code>Set.remove</code>,
     ** <code>removeAll</code>, <code>retainAll</code> and <code>clear</code>
     ** operations. It does not support the <code>add</code> or
     ** <code>addAll</code> operations.
     **
     ** @return                  a set view of the mappings contained in the
     **                          backing map.
     **                          <br>
     **                          Possible object is {@link Set} where
     **                          each element is of type {@link Map.Entry}.
     */
    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
      return (this.delegate != null) ? this.delegate.entrySet() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: putIfAbsent (ConcurrentMap)
    /**
     ** If the specified key is not already associated with a value, associate
     ** it with the given value.
     ** <br>
     ** This is equivalent to
     ** <pre>
     **    if (!map.containsKey(key))
     **      return map.put(key, value);
     **    else
     **      return map.get(key);
     ** </pre>
     ** except that the action is performed atomically.
     ** <br>
     ** <b>Note</b>
     ** <br>
     ** This implementation intentionally re-abstracts the inappropriate default
     ** provided in {@link Map}.
     **
     ** @param  key              the key with which the specified value is
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            to be associated with the specified key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  previous value associated with specified key,
     **                          or <code>null</code> if there was no mapping
     **                          for key. A <code>null</code> return can also
     **                          indicate that the map previously associated
     **                          <code>null</code> with the specified key, if
     **                          the implementation supports <code>null</code>
     **                          values.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws NullPointerException          if the specified map is
     **                                       <code>null</code>, or if the
     **                                       backing {@link Map} does not
     **                                       permit <code>null</code> keys or
     **                                       values.
     ** @throws ClassCastException            if the key or value is of an
     **                                       inappropriate type for the
     **                                       backing {@link Map}.
     ** @throws IllegalArgumentException      if some property of the specified
     **                                       key or value prevents it from
     **                                       being stored in the backing
     **                                       {@link Map}.
     ** @throws UnsupportedOperationException if the <code>putIfAbsent</code>
     **                                       method is not supported by the
     **                                       backing {@link Map}.
     */
    @Override
    public final V putIfAbsent(final K key, final V value) {
      return (this.delegate != null) ? this.delegate.putIfAbsent(key, value) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (ConcurrentMap)
    /**
     ** Removes the entry for a key only if currently mapped to a given value.
     ** <br>
     ** This is equivalent to
     ** <pre>
     **    if (map.containsKey(key) &amp;&amp; Objects.equals(map.get(key), value)) {
     **      map.remove(key);
     **      return true;
     **    }
     **    else
     **      return false;
     **    }
     ** </pre>
     ** except that the action is performed atomically.
     ** <br>
     ** <b>Note</b>
     ** <br>
     ** This implementation intentionally re-abstracts the inappropriate default
     ** provided in {@link Map}.
     **
     ** @param  key              the key with which the specified value is
     **                          associated.
     **                          <br>
     **                          Allowed object is {@link Object}.
     ** @param  value            the value expected to be associated with the
     **                          specified key.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if the value was removed;
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws NullPointerException          if the specified map is
     **                                       <code>null</code>, or if the
     **                                       backing {@link Map} does not
     **                                       permit <code>null</code> keys or
     **                                       values.
     ** @throws ClassCastException            if the key or value is of an
     **                                       inappropriate type for the
     **                                       backing {@link Map}.
     ** @throws UnsupportedOperationException if the <code>remove</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final boolean remove(final Object key, final Object value) {
      return (this.delegate != null) ? this.delegate.remove(key, value) : false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: replace (ConcurrentMap)
    /**
     ** Replaces the entry for a key if currently mapped to a given value.
     ** <br>
     ** This is equivalent to
     ** <pre>
     **    if (map.containsKey(key) {
     **      return map.put(key, value);
     **    }
     **    else
     **      return null;
     **    }
     ** </pre>
     ** except that the action is performed atomically.
     ** <br>
     ** <b>Note</b>
     ** <br>
     ** This implementation intentionally re-abstracts the inappropriate default
     ** provided in {@link Map}.
     **
     ** @param  key              the key with which the specified value is
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            to be associated with the specified key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @throws NullPointerException          if the specified map is
     **                                       <code>null</code>, or if the
     **                                       backing {@link Map} does not
     **                                       permit <code>null</code> keys or
     **                                       values.
     ** @throws ClassCastException            if the key or value is of an
     **                                       inappropriate type for the
     **                                       backing {@link Map}.
     ** @throws IllegalArgumentException      if some property of the specified
     **                                       key or value prevents it from
     **                                       being stored in the backing
     **                                       {@link Map}.
     ** @throws UnsupportedOperationException if the <code>replace</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final V replace(final K key, final V value) {
      return (this.delegate != null) ? this.delegate.replace(key, value) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: replace (ConcurrentMap)
    /**
     ** Replaces the entry for a key if currently mapped to a given value.
     ** <br>
     ** This is equivalent to
     ** <pre>
     **    if (map.containsKey(key) &amp;&amp; Objects.equals(map.get(key), origin)) {
     **      return map.put(key, value);
     **    }
     **    else
     **      return null;
     **    }
     ** </pre>
     ** except that the action is performed atomically.
     ** <br>
     ** <b>Note</b>
     ** <br>
     ** This implementation intentionally re-abstracts the inappropriate default
     ** provided in {@link Map}.
     **
     ** @param  key              the key with which the specified value is
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  origin           the value expected to be associated with the
     **                          specified key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  value            the value to be associated with the specified
     **                          key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  <code>true</code> if the value was replaced;
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws NullPointerException          if the specified map is
     **                                       <code>null</code>, or if the
     **                                       backing {@link Map} does not
     **                                       permit <code>null</code> keys or
     **                                       values.
     ** @throws ClassCastException            if the key or value is of an
     **                                       inappropriate type for the
     **                                       backing {@link Map}.
     ** @throws IllegalArgumentException      if some property of the specified
     **                                       key or value prevents it from
     **                                       being stored in the backing
     **                                       {@link Map}.
     ** @throws UnsupportedOperationException if the <code>replace</code> method
     **                                       is not supported by the backing
     **                                       {@link Map}.
     */
    @Override
    public final boolean replace(final K key, final V origin, final V value) {
      return (this.delegate != null) ? this.delegate.replace(key, origin, value) : false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** The <code>Builder</code> for a {@link SystemProperty}.
   **
   ** @param  <T>                the type of the implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the request extending
   **                            this class (requests can return their own
   **                            specific type instead of type defined by this
   **                            class only).
   */
  public static class Builder<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Class<T> clazz;

    private String         name;
    private T              maximumValue;
    private T              minimumValue;
    private T              defaultValue;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default <code>Builder</code> for the specified the type of
     ** the implementation.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Builder()" and enforces use of the public method below.
     **
     ** @param  clazz            the type of the implementation.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>
     */
    private Builder(final Class<T> clazz) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.clazz    = clazz;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name for the {@link SystemProperty} being build.
     ** <br>
     ** <b>Must</b> be unique.
     **
     ** @param  name             the name of the {@link SystemProperty} being
     **                          build.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder<T> name(final String name) {
      this.name = name;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: defaultValue
    /**
     ** Sets the default value for the {@link SystemProperty} to build.
     **
     ** @param  value            the <code>T</code> if the property is not set
     **                          or in not in the range of min/max values.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @see    #minimumValue(Object)
     ** @see    #maximumValue(Object)
     */
    public Builder<T> defaultValue(final T value) {
      this.defaultValue = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: minimumValue
    /**
     ** Sets the minimum value for the {@link SystemProperty} to build.
     ** <br>
     ** If the configured value is less than minimum value, the default value
     ** will be used instead.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** If a minimum value is configured, the type of property being built
     ** <b>must</b> implement {@link Comparable}.
     **
     ** @param  value            the minimum value.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @see    #defaultValue(Object)
     ** @see    #maximumValue(Object)
     */
    public Builder<T> minimumValue(final T value) {
      this.minimumValue = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumValue
    /**
     ** Sets the maximum value for the {@link SystemProperty} to build.
     ** <br>
     ** If the configured value is less than minimum value, the default value
     ** will be used instead.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** If a maximum value is configured, the type of property being built
     ** <b>must</b> implement {@link Comparable}.
     **
     ** @param  value            the <code>T</code> if the property is not set
     **                          or in not in the range of min/max values.
     **                          <br>
     **                          Allowed object is <code>T</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @see    #defaultValue(Object)
     ** @see    #minimumValue(Object)
     */
    public Builder<T> maximumValue(final T value) {
      this.maximumValue = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Obtains a mutable builder for the specified the type of the
     ** implementation.
     ** <p>
     ** This factory support following types.
     ** <ul>
     **   <li>{@link String}
     **   <li>{@link Integer} - for which a default value must be supplied using
     **                         {@link #defaultValue(Object)}
     **   <li>{@link Long}    - for which a default value must be supplied
     **   <li>{@link Float}   - for which a default value must be supplied
     **   <li>{@link Double}  - for which a default value must be supplied
     **   <li>{@link Boolean} - for which a default value must be supplied
     ** </ul>
     **
     ** @param  <T>              the type of the implementation.
     **                          <br>
     **                          This parameter is used for convenience to allow
     **                          better implementations extending this class).
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>.
     ** @param  clazz            the type of the implementation.
     **                          <br>
     **                          Allowed object is {@link Class} of type
     **                          <code>T</code>.
     **
     ** @return                  a <code>Builder</code> for a
     **                          {@link SystemProperty} of type <code>T</code>.
     **                          <br>
     **                          Possible object is <code>Builder</code> for
     **                          type <code>T</code>
     */
    public static <T> Builder<T> of(final Class<T> clazz) {
      // prevent bogus input
      if (!Enum.class.isAssignableFrom(clazz) && (!INBOUND.containsKey(clazz)))
        throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_TYPE_INVALID, clazz.getName()));

      return new Builder<T>(clazz);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Validates the details of the {@link SystemProperty} being built, and
     ** generates one if it's valid.
     **
     ** @return                  the validated {@link SystemProperty}.
     **                          <br>
     **                          Possible object is {@link SystemProperty}.
     **
     ** @throws IllegalArgumentException if incorrect arguments have been
     **                                  supplied to the builder.
     */
    public SystemProperty<T> build() {
      checkNotNull(this.name, SystemError.PROPERTY_NAME_REQUIRED);
      final SystemProperty<T> property = new SystemProperty<>(this);

      if (PROPERTIES.containsKey(this.name))
        throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_NAME_EXISTS, this.name));

      if (!NILABLE.contains(this.clazz))
        checkNotNull(this.defaultValue, SystemError.PROPERTY_DEFAULT_REQUIRED);

      if (this.minimumValue != null) {
        if (!Comparable.class.isAssignableFrom(clazz))
          throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_MINIMUM_COMPARABLE));
        //noinspection unchecked
        if (this.defaultValue != null && ((Comparable<T>)this.minimumValue).compareTo(this.defaultValue) > 0)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_MINIMUM_DEFAULT));
      }
      if (this.maximumValue != null) {
        if (!Comparable.class.isAssignableFrom(clazz))
          throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_MAXIMUM_COMPARABLE));
        //noinspection unchecked
        if (this.defaultValue != null && ((Comparable<T>)this.maximumValue).compareTo(this.defaultValue) > 0)
          throw new IllegalArgumentException(SystemBundle.string(SystemError.PROPERTY_MAXIMUM_DEFAULT));
      }

      PROPERTIES.put(this.name, property);
      return property;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: checkNotNull
    /**
     ** Verifies that the given <code>value</code> is not null.
     **
     ** @param  value            the value to validate.
     **                          <br>
     **                          Allowed object is {@link Object}.
     * @param s
     */
    private void checkNotNull(final Object value, final String error) {
      if (value == null) {
        throw new IllegalArgumentException(SystemBundle.string(error));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>SystemProperty</code> using the specified
   ** {@link Builder} to populate the instance;
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new SystemProperty()" and enforces use of the public method below.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            populate the instance.
   **                            <br>
   **                            Allowed object is {@link Builder} of type
   **                            <code>T</code>.
   */
  private SystemProperty(final Builder<T> builder) {
    // ensure inheritance
    super();

    // initailize instance attributes
    this.clazz        = builder.clazz;
    this.name         = builder.name;
    this.minimumValue = builder.minimumValue;
    this.maximumValue = builder.maximumValue;
    this.defaultValue = builder.defaultValue;
    this.initialValue = value();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Return the name of the <code>SystemProperty</code>.
   ** <br>
   ** <b>Must</b> be unique.
   **
   ** @return                    the name of the <code>SystemProperty</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Return the current value of the <code>SystemProperty</code>.
   ** <br>
   ** If the <code>SystemProperty</code> isn't set the <code>defaultValue</code>
   ** is returned.
   ** <br>
   ** If the min/max constraints are violated the <code>defaultValue</code> is
   ** returned.
   **
   ** @return                    the current value of the
   **                            <code>SystemProperty</code>.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public T value() {
    final T value = (T) INBOUND.get(converterClass()).apply(CACHE.<String>get(this.name), this);
    if (value == null || (Collection.class.isAssignableFrom(value.getClass()) && ((Collection) value).isEmpty())) {
      return this.defaultValue;
    }
    if (this.minimumValue != null && ((Comparable)this.minimumValue).compareTo(value) > 0) {
      return this.defaultValue;
    }
    if (this.maximumValue != null && ((Comparable)this.maximumValue).compareTo(value) < 0) {
      return defaultValue;
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseBoolean
  /**
   ** Parses the string argument as a boolean.
   ** <br>
   ** The <code>boolean</code> returned represents the value <code>true</code>
   ** if the string argument is not <code>null</code> and is equal, ignoring
   ** case, to the string <code>"true"</code>.
   ** <br>
   ** Subtly different from Boolean.parseBoolean in that it returns
   ** <code>Optional.empty()</code> instead of <code>false</code> if the
   ** supplied value is not <code>"true"</code> or <code>"false"</code>
   ** (ignoring case).
   **
   ** @param  value              any string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    <code>true</code>, <code>false</code> or
   **                            <code>Optional.empty()</code>.
   **                            <br>
   **                            Possible object id {@link Optional} for type
   **                            {@link Boolean}.
   */
  public static Optional<Boolean> parseBoolean(final String value) {
    if ("true".equalsIgnoreCase(value)) {
      return Optional.of(Boolean.TRUE);
    }
    else if ("false".equalsIgnoreCase(value)) {
      return Optional.of(Boolean.FALSE);
    }
    else {
      return Optional.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseInteger
  /**
   ** Parses the string argument as an integer.
   ** <br>
   ** Returns an {@link Integer} object holding the value of the specified
   ** {@link String}. The argument is interpreted as representing a signed
   ** decimal integer.
   **
   ** @param  value              any string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    an {@link Optional} {@link Integer} object
   **                            holding the value represented by the string
   **                            argument.
   **                            <br>
   **                            Possible object id {@link Optional} for type
   **                            {@link Integer}.
   */
  public static Optional<Integer> parseInteger(final String value) {
    if (value == null || value.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Integer.valueOf(value));
    }
    catch (final NumberFormatException e) {
      return Optional.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseLong
  /**
   ** Parses the string argument as a long.
   ** <br>
   ** Returns a {@link Long} object holding the value of the specified
   ** {@link String}. The argument is interpreted as representing a signed
   ** decimal <code>long</code>.
   **
   ** @param  value              any string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    an {@link Optional} {@link Long} object
   **                            holding the value represented by the string
   **                            argument.
   **                            <br>
   **                            Possible object id {@link Optional} for type
   **                            {@link Long}.
   */
  public static Optional<Long> parseLong(final String value) {
    if (value == null || value.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Long.valueOf(value));
    }
    catch (final NumberFormatException e) {
      return Optional.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDouble
  /**
   ** Parses the string argument as a double.
   ** <br>
   ** Returns an {@link Double} object holding the value of the specified
   ** {@link String}.
   **
   ** @param  value              any string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    an {@link Optional} {@link Double} object
   **                            holding the value represented by the string
   **                            argument.
   **                            <br>
   **                            Possible object id {@link Optional} for type
   **                            {@link Double}.
   */
  public static Optional<Double> parseDouble(final String value) {
    if (value == null || value.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Double.valueOf(value));
    }
    catch (final NumberFormatException e) {
      return Optional.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseFloat
  /**
   ** Parses the string argument as a float.
   ** <br>
   ** Returns an {@link Float} object holding the value of the specified
   ** {@link String}.
   **
   ** @param  value              any string value to convert.
   **                            <br>
   **                            Allowed object is {@link String}
   **
   ** @return                    an {@link Optional} {@link Float} object
   **                            holding the value represented by the string
   **                            argument.
   **                            <br>
   **                            Possible object id {@link Optional} for type
   **                            {@link Float}.
   */
  public static Optional<Float> parseFloat(final String value) {
    if (value == null || value.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Float.valueOf(value));
    }
    catch (final NumberFormatException e) {
      return Optional.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   converterClass
  /**
   ** Returns the converter implementation:
   ** <br>
   ** Enums are a special case.
   **
   ** @return                    the converter class to apply.
   **                            <br>
   **                            Possible object is {@link Class}.
   */
  private Class converterClass() {
    return (Enum.class.isAssignableFrom(this.clazz)) ? Enum.class : this.clazz;
  }
}