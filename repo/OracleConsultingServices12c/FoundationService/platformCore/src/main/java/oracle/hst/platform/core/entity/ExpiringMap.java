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

    File        :   ExpiringMap.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ExpiringMap.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.util.Set;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ConcurrentHashMap;

////////////////////////////////////////////////////////////////////////////////
// class ExpiringMap
// ~~~~~ ~~~~~~~~~~~
/**
 ** A hash map that expires and removes items if they are older than a given
 ** time-to-live.
 ** <p>
 ** The expiry is a passive process, items aren't removed until they are
 ** retrieved and deemed to be expired by {@link #get(Object)}.
 ** <br>
 ** Certain operations do cause the cache to clear all expired values in order
 ** to provide an accurate result (keySet, entrySet, values, size) and these
 ** operations are therefore O(n).
 ** <p>
 ** For the purpose the class use the Delegate Pattern.
 **
 ** @param  <K>                  the type of the key implementation.
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
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ExpiringMap<K, V> implements Map<K, V> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final long                lifetime;
  private final Map<K, V>           delegate;
  private final Map<K, Expired<K>>  elapsed;
  /**
   ** Holds the map keys using the given life time for expiration.
   */
  private final DelayQueue<Expired> expired   = new DelayQueue<Expired>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Builder
  // ~~~~~ ~~~~~~~
  /**
   ** Builds <code>ExpiringMap</code> instances.
   ** <br>
   ** Defaults to expiration of 60 {@link TimeUnit#SECONDS}.
   **
   ** @param  <T>                the type of the key implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the loggables
   **                            extending this class (loggables can return
   **                            their own specific type instead of type defined
   **                            by this class only).
   ** @param  <R>                the type of the value implementation.
   **                            <br>
   **                            This parameter is used for convenience to allow
   **                            better implementations of the loggables
   **                            extending this class (loggables can return
   **                            their own specific type instead of type defined
  **                             by this class only).
   */
  public static final class Builder<T, R> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The default policy of lifetime duration is 10 {@link TimeUnit#MINUTES}.
     */
    private long lifetime = TimeUnit.MINUTES.toNanos(10L);

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Builder</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** The lifetime of any entry defaulted to 10 minutes.
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: lifetime
    /**
     ** Sets the default map entry lifetime duration.
     **
     ** @param  unit             the {@link TimeUnit} to evaluate the lifetime
     **                          duration.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     ** @param  value            the value to evaluate the lifetime duration.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder<T, R> lifetime(final TimeUnit unit, final long value) {
      this.lifetime = unit.toNanos(value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to creates a new instance of {@link ExpiringMap} with
     ** properties of this <code>Builder</code>.
     **
     ** @return                    the created {@link ExpiringMap}.
     **                            <br>
     **                            Possible object is {@link ExpiringMap}.
     */
    public ExpiringMap<T, R> build() {
      return new ExpiringMap<T, R>(this);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Expired
  // ~~~~~ ~~~~~~~
  /**
   ** A mix-in implementation for marking objects that should be acted upon
   ** after a given delay.
   */
  private static class Expired<T> implements Delayed {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private long       start = System.currentTimeMillis();
    private final T    key;
    private final long ttl;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Expired</code> instance.
     **
     ** @param  key              the key with which the lifetime is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  ttl              the time-to-life of the specified key in
     **                          milliseconds.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    private Expired(final T key, final long ttl) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.ttl = ttl;
      this.key = key;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: delay
    /**
     ** Evaluates the time after this instance elapse.
     **
     ** @return                  the time after this instance elapse.
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    private long delay() {
      return (this.start + this.ttl) - System.currentTimeMillis();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: renew
    /**
     ** Renews the lifetime of this instance.
     */
    private void renew() {
      this.start = System.currentTimeMillis();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: elapsed
    /**
     ** Renews
     */
    private void elapsed() {
      this.start = Long.MIN_VALUE;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compareTo (Delayed)
    /**
     ** Compares this object with the specified object <code>other</code> for
     ** order.
     ** <p>
     ** Returns a negative integer, zero, or a positive integer as this object
     ** is less than, equal to, or greater than the specified object.
     ** <p>
     ** The implementation ensures that
     ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
     ** <code>x</code> and <code>y</code>.
     ** <p>
     ** The implementation also ensures that the relation is transitive:
     ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code>
     ** implies <code>x.compareTo(z)&gt;0</code>.
     ** <p>
     ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
     ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
     ** <code>z</code>.
     ** <p>
     ** In the foregoing description, the notation
     ** <code>sgn(</code><i>expression</i><code>)</code> designates the
     ** mathematical <i>signum</i> function, which is defined to return one of
     ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
     ** the value of <i>expression</i> is negative, zero or positive.
     **
     ** @param  other            the object to be compared.
     **                          <br>
     **                          Allowed object is {@link Delayed}.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as this object is less than, equal to, or
     **                          greater than the specified object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws ClassCastException if the specified object's type prevents it
     **                             from being compared to this object.
     */
    @Override
    public int compareTo(final Delayed other) {
      return Long.compare(this.delay(), ((Expired)other).delay());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getDelay (Delayed)
    /**
     ** Returns the remaining delay associated with this object, in the given
     ** time unit.
     **
     ** @param  unit             the time unit.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  the remaining delay; zero or negative values
     **                          indicate that the delay has already elapsed.
     **                          <br>
     **                          Possible object is <code>long</code>.
     */
    @Override
    public long getDelay(final TimeUnit unit) {
      return unit.convert(delay(), TimeUnit.MILLISECONDS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
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
    public int hashCode() {
      int result = 7;
      result = 31 * result + (this.key != null ? this.key .hashCode() : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>ExpiringMap</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>ExpiringMap</code> may be different even though they contain the
     ** same set of names with the same values, but in a different order.
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
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Expired that = (Expired)other;
      return (this.key  == null ? that.key  == null : this.key .equals(that.key ));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an expiring <code>Map</code> that populates its values from
   ** the specified {@link Builder}.
   **
   ** @param  builder            the {@link Builder} to populate this instance
   **                            from.
   **                            <br>
   **                            Allowed object is {@link Builder}.
   */
  private ExpiringMap(final Builder<K, V> builder) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = new ConcurrentHashMap<K, V>();
    this.elapsed  = new WeakHashMap<K, Expired<K>>();
    this.lifetime = builder.lifetime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Map)
  /**
   ** Returns the number of key-value mappings in the backing map. If the
   ** backing map contains more than <code>Integer.MAX_VALUE</code> elements,
   ** returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of key-value mappings in the
   **                            backing map.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int size() {
    housekeeping();
    return this.delegate.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (Map)
  /**
   ** Returns <code>true</code> if the backing map contains no key-value
   ** mappings.
   **
   ** @return                    <code>true</code> if the backing map contains
   **                            no key-value mappings.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isEmpty() {
    housekeeping();
    return this.delegate.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (Map)
  /**
   ** Removes all mappings from the backing map (optional operation).
   **
   ** @throws UnsupportedOperationException if clear is not supported by the
   **                                       backing map.
   */
  @Override
  public void clear() {
    this.expired.clear();
    this.elapsed.clear();
    this.delegate.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsKey (Map)
  /**
   ** Returns <code>true</code> if the backing map contains a mapping for the
   ** specified key. More formally, returns <code>true</code> if and only if the
   ** backing map contains a mapping for a key <code>k</code> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>.
   ** (There can be at most one such mapping.)
   **
   ** @param  key                key whose presence in the backing map is to
   **                            be tested.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the backing map contains
   **                            a mapping for the specified key.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ClassCastException   if the key is of an inappropriate type for
   **                              the backing map (optional).
   ** @throws NullPointerException if the key is <code>null</code> and the
   **                              backing map does not permit
   **                              <code>null</code> keys (optional).
   */
  @Override
  public boolean containsKey(final Object key) {
    housekeeping();
    return this.delegate.containsKey(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsValue (Map)
  /**
   ** Returns <code>true</code> if the backing map maps one or more keys to
   ** the specified value. More formally, returns <code>true</code> if and only
   ** if the backing map contains at least one mapping to a value
   ** <code>v</code> such that
   ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
   ** will probably require time linear in the map size for most implementations
   ** of the <code>Map</code> interface.
   **
   ** @param  value              value whose presence in the backing map is to
   **                            be tested.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the backing map maps one
   **                            or more keys to the specified value.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ClassCastException   if the value is of an inappropriate type for
   **                              the backing map (optional).
   ** @throws NullPointerException if the value is <code>null</code> and the
   **                              backing map does not permit
   **                              <code>null</code> values (optional).
   */
  @Override
  public boolean containsValue(final Object value) {
    housekeeping();
    return this.delegate.containsValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (Map)
  /**
   ** Returns the value to which the backing map maps the specified key.
   ** Returns <code>null</code> if the map contains no mapping for this key. A
   ** return value of <code>null</code> does not <i>necessarily</i> indicate
   ** that the map contains no mapping for the key; it's also possible that the
   ** backing map explicitly maps the key to <code>null</code>. The
   ** <code>containsKey</code> operation may be used to distinguish these two
   ** cases.
   ** <p>
   ** More formally, if the backing map contains a mapping from a key
   ** <code>k</code> to a value <code>v</code> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <code>v</code>; otherwise it returns <code>null</code>.
   ** (There can be at most one such mapping.)
   **
   ** @param  key                key whose associated value is to be returned.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the value to which the backing map maps the
   **                            specified key, or <code>null</code> if the map
   **                            contains no mapping for this key.
   **                            <br>
   **                            Possible object is <code>K</code>.
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
  public V get(final Object key) {
    housekeeping();
    renew(key);
    return this.delegate.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (Map)
  /**
   ** Associates the specified value with the specified key in the backing map
   ** (optional operation). If the backing map previously contained a mapping
   ** for this key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if {@link #containsKey(Object) m.containsKey(k)}
   ** would return <code>true</code>.))
   **
   ** @param  key                key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              value to be associated with the specified key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws UnsupportedOperationException if the <code>put</code> operation is
   **                                       not supported by backing map.
   ** @throws ClassCastException            if the class of the specified key or
   **                                       value prevents it from being stored
   **                                       in the backing map.
   ** @throws IllegalArgumentException      if some aspect of this key or value
   **                                       prevents it from being stored in the
   **                                       backing map.
   ** @throws NullPointerException          if the backing map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified key or value is
   **                                       <code>null</code>.
   */
  @Override
  public V put(final K key, final V value) {
    return put(key, value, this.lifetime);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Map)
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
   ** associated <code>null</code> with the specified key if the implementation
   ** supports <code>null</code> values.) The map will not contain a mapping for
   ** the specified  key once the call returns.
   **
   ** @param  key                key whose mapping is to be removed from the
   **                            map.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    value associated with specified key, or
   **                            <code>null</code> if there was no mapping for
   **                            key.
   **                            <br>
   **                            Possible object is <code>K</code>.
   **
   ** @throws ClassCastException            if the key is of an inappropriate
   **                                       type for the backing map
   **                                       (optional).
   ** @throws NullPointerException          if the key is <code>null</code> and
   **                                       the backing map does not permit
   **                                       <code>null</code> keys (optional).
   ** @throws UnsupportedOperationException if the <code>remove</code> method is
   **                                       not supported by the backing map.
   */
  @Override
  public V remove(final Object key) {
    V removedValue = this.delegate.remove(key);
    elapse(this.elapsed.remove(key));
    return removedValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putAll (Map)
  /**
   ** Copies all of the mappings from the specified map to the backing map
   ** (optional operation). The effect of this call is equivalent to that of
   ** calling <code>put(K, V)</code> on the backing map once for each mapping
   ** from key <code>k</code> to value <code>v</code> in the specified map. The
   ** behavior of this operation is unspecified if the specified map is modified
   ** while the operation is in progress.
   **
   ** @param  t                  mappings to be stored in the backing map.
   **                            <br>
   **                            Allowed object is {@link Map.Entry} where
   **                            each element is of type <code>K</code> for the
   **                            key and <code>V</code> for the value.
   **
   ** @throws UnsupportedOperationException if the <code>putAll</code> method is
   **                                       not supported by the backing map.
   ** @throws ClassCastException            if the class of a key or value in
   **                                       the specified map prevents it from
   **                                       being stored in the backing map.
   ** @throws IllegalArgumentException      some aspect of a key or value in the
   **                                       specified map prevents it from being
   **                                       stored in the backing map.
   ** @throws NullPointerException          if the specified map is
   **                                       <code>null</code>, or if the
   **                                       backing map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified map contains
   **                                       <code>null</code> keys or values.
   */
  @Override
  public void putAll(final Map<? extends K, ? extends V> t) {
    for (Map.Entry<? extends K, ? extends V> e : t.entrySet()) {
      this.put(e.getKey(), e.getValue());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keySet (Map)
  /**
   ** Returns a set view of the keys contained in the backing map. The set is
   ** backed by the backing map, so changes to backing map are reflected in
   ** the set, and vice-versa. If backing map is modified while an iteration
   ** over the set is in progress (except through the iterator's own
   ** <code>remove</code> operation), the results of the iteration are
   ** undefined. The set supports element removal, which removes the
   ** corresponding mapping from backing map, via the
   ** <code>Iterator.remove</code>, <code>Set.remove</code>,
   ** <code>removeAll</code> <code>retainAll</code>, and <code>clear</code>
   ** operations. It does not support the add or <code>addAll</code> operations.
   **
   ** @return                    a set view of the keys contained in the
   **                            backing map.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type <code>K</code>.
   */
  @Override
  public Set<K> keySet() {
    housekeeping();
    return Collections.unmodifiableSet(this.delegate.keySet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values (Map)
  /**
   ** Returns a collection view of the values contained in the backing map.
   ** The collection is backed by the backing map, so changes to the attribute
   ** map are reflected in the collection, and vice-versa. If the backing map
   ** is modified while an iteration over the collection is in progress (except
   ** through the iterator's own <code>remove</code> operation), the results of
   ** the iteration are undefined. The collection supports element removal,
   ** which removes the corresponding mapping from the backing map, via the
   ** <code>Iterator.remove</code>, <code>Collection.remove</code>,
   ** <code>removeAll</code>, <code>retainAll</code> and <code>clear</code>
   ** operations. It does not support the add or <code>addAll</code> operations.
   **
   ** @return                    a collection view of the values contained in
   **                            the backing map.
   **                            <br>
   **                            Possible object is {@link Collection} where
   **                            each element is of type <code>V</code>.
   */
  @Override
  public Collection<V> values() {
    housekeeping();
    return Collections.unmodifiableCollection(this.delegate.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySet (Map)
  /**
   ** Returns a set view of the mappings contained in the backing map. Each
   ** element in the returned set is a <code>Map.Entry</code>. The set is backed
   ** by the backing map, so changes to the map are reflected in the set, and
   ** vice-versa. If the backing map is modified while an iteration over the
   ** set is in progress (except through the iterator's own <code>remove</code>
   ** operation, or through the <code>setValue</code> operation on a map entry
   ** returned by the iterator) the results of the iteration are undefined. The
   ** set supports element removal, which removes the corresponding mapping from
   ** the backing map, via the <code>Iterator.remove</code>,
   ** <code>Set.remove</code>, <code>removeAll</code>, <code>retainAll</code>
   ** and <code>clear</code> operations. It does not support the
   ** <code>add</code> or <code>addAll</code> operations.
   **
   ** @return                    a set view of the mappings contained in the
   **                            backing map.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Map.Entry} with
   **                            <code>K</code> as the mapping key and
   **                            <code>V</code> as the mapped value.
   */
  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    housekeeping();
    return Collections.unmodifiableSet(this.delegate.entrySet());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to creates a new instance of <code>ExpiringMap</code> with
   ** an expiration of 60 seconds.
   **
   ** @param  <K>                the type of the key implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   **
   ** @return                    the created <code>ExpiringMap</code>.
   **                            <br>
   **                            Possible object is <code>ExpiringMap</code>.
   **                            where each element is of type <code>K</code> as
   **                            the mapping key and <code>V</code> as the
   **                            mapped value.
   */
  @SuppressWarnings("unchecked")
  public static <K, V> ExpiringMap<K, V> build() {
    return new ExpiringMap<K, V>(ExpiringMap.builder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   builder
  /**
   ** Factory method to creates an <code>ExpiringMap</code> {@link Builder}.
   **
   ** @param  <T>                the type of the key implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the value implementation.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   **
   ** @return                    a new <code>ExpiringMap</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>ExpiringMap.Builder</code>.
   */
  public static <T, E> Builder<T, E> builder() {
    return new Builder<T, E>();
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
    return this.delegate.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
  /**
   ** Associates the specified value with the specified key in the backing map
   ** (optional operation). If the backing map previously contained a mapping
   ** for this key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if {@link #containsKey(Object) m.containsKey(k)}
   ** would return <code>true</code>.))
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  unit               the {@link TimeUnit} to evaluate the lifetime
   **                            duration of the association.
   **                            <br>
   **                            Allowed object is {@link TimeUnit}.
   ** @param  ttl                the value to evaluate the lifetime duration of
   **                            the association.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws ClassCastException            if the class of the specified key or
   **                                       value prevents it from being stored
   **                                       in the backing map.
   ** @throws IllegalArgumentException      if some aspect of this key or value
   **                                       prevents it from being stored in the
   **                                       backing map.
   ** @throws NullPointerException          if the backing map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified key or value is
   **                                       <code>null</code>.
   ** @throws UnsupportedOperationException if the <code>put</code> operation is
   **                                       not supported by backing map.
   */
  public V put(final K key, final V value, final TimeUnit unit, final long ttl) {
    return put(key, value, unit.toNanos(ttl));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
  /**
   ** Associates the specified value with the specified key in the backing map
   ** (optional operation). If the backing map previously contained a mapping
   ** for this key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if {@link #containsKey(Object) m.containsKey(k)}
   ** would return <code>true</code>.))
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  ttl                the lifetime of the association.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws UnsupportedOperationException if the <code>put</code> operation is
   **                                        not supported by thattribute
   **                                       backing map.
   ** @throws ClassCastException            if the class of the specified key or
   **                                       value prevents it from being stored
   **                                       in the backing map.
   ** @throws IllegalArgumentException      if some aspect of this key or value
   **                                        prevents it from being stored in the
   **                                       backing map.
   ** @throws NullPointerException          if the backing map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified key or value is
   **                                       <code>null</code>.
   */
  public V put(final K key, final V value, final long ttl) {
    housekeeping();
    final Expired<K> delayed = new Expired<K>(key, ttl);
    final Expired<K> oldKey  = this.elapsed.put(key, delayed);
    if (oldKey != null) {
      elapse(oldKey);
      this.elapsed.put(key, delayed);
    }
    this.expired.offer(delayed);
    return this.delegate.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renew
  /**
   ** Renews a key.
   **
   ** @param  key                the key to renew.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>treu</code> is the specified key is
   **                            about to expire; other wise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>;
   */
  public boolean renew(final Object key) {
    final Expired<K> elapsed = this.elapsed.get(key);
    if (elapsed != null) {
      elapsed.renew();
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elapse
  private void elapse(final Expired<K> delayed) {
    if (delayed != null) {
      delayed.elapsed();
      housekeeping();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   housekeeping
  private void housekeeping() {
    Expired<K> cursor = this.expired.poll();
    while (cursor != null) {
      this.delegate.remove(cursor.key);
      this.elapsed.remove(cursor.key);
      cursor = this.expired.poll();
    }
  }
}