package oracle.iam.identity.icf.cache;

import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.function.Function;
import java.util.function.BiFunction;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// final class Unbounded
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** An in-memory cache that has no capabilities for bounding the map.
 ** <p>
 ** This implementation provides a lightweight wrapper on top of
 ** {@link ConcurrentHashMap}.
 **
 ** @param  <K>                  the most general type of keys this cache
 **                              stores; for example {@link Object} if any
 **                              key is acceptable.
 ** @param  <V>                  the most general type of values this cache
 **                              stores; for example {@link Object} if any
 **                              value is acceptable.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 **
 */
final class Unbounded<K, V> implements Cache.Store.Manual<K, V> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final Logger                        logger = Logger.getLogger(Unbounded.class.getName());

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The chache storage. */
  final ConcurrentHashMap<K, V>              store;

  /** Indicates that notification of cache events send asynchron. */
  final boolean                              asynchron;
  final Cache.Listener<? super K, ? super V> removal;
  final Cache.Listener<? super K, ? super V> eviction;

  final Ticker                               ticker;
  final Metric.Collector                     metric;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Unbounded</code> where each entry expires in the
   ** specified timeframe.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Unbounded()" and enforces use of the public method below.
   **
   ** @param  builder            the {@link Builder} providing the values to
   **                            populate the instance.
   **                            <br>
   **                            Allowed object is {@link Builder} of type
   **                            <code>K</code> for the key and <code>V</code>
   ** @param  asynchron          <code>true</code> if cache event notifications
   **                            send asynchron.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   */
  private Unbounded(final Cache.Builder<? super K, ? super V> builder, final boolean asynchron) {
    // ensure inheritance
    super();

    // initailize instance attributes
    this.store     = new ConcurrentHashMap<>(builder.initialCapacity());
    this.removal   = builder.removalListener;
    this.eviction  = builder.evictionListener;
    this.ticker    = builder.metricTicker;
    this.metric    = builder.metricSupplier.get();
    this.asynchron = asynchron;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cache (Cache.Store.Manual)
  /**
   ** Returns the backing {@link Store} data store.
   **
   ** @return                  the backing {@link Store} data store.
   **                          <br>
   **                          Possible object is {@link Store}.
   */
  @Override
  public final Cache.Store<K, V> cache() {
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   store
  /**
   ** Returns a view of the entries stored in this cache as a thread-safe map.
   ** <br>
   ** Modifications made to the map directly affect the cache.
   ** <p>
   ** A computation operation, such as {@link ConcurrentMap#compute}, performs
   ** the entire method invocation atomically, so the function is applied at
   ** most once per key. Some attempted update operations by other threads may
   ** be blocked while computation is in progress. The computation must not
   ** attempt to update any other mappings of this cache.
   ** <p>
   ** Iterators from the returned map are at least <i>weakly consistent</i>:
   ** they are safe for concurrent use, but if the cache is modified (including
   ** by eviction) after the iterator is created, it is undefined which of the
   ** changes (if any) will be reflected in that iterator.
   **
   ** @return                    a thread-safe view of this cache supporting all
   **                            of the optional {@link Map} operations.
   **                            <br>
   **                            Allowed object is {@link ConcurrentMap} where
   **                            each elements is of type <code>K</code> for the
   **                            key and <code>V</code> for the value.
   */
  @Override
  public final ConcurrentMap<K, V> store() {
    return this.store();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   recordingMetric (Cache.Store)
  /**
   ** Returns whether this cache has metrics enabled.
   **
   ** @return                    <code>true</code> this cache has metrics
   **                            enabled.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean recordingMetric() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metricCollector (Cache.Store)
  /**
   ** Returns the {@link Metric.Collector} used by this cache.
   **
   ** @return                    the {@link Metric.Collector} used by this
   **                            cache.
   **                            <br>
   **                            Possible object is {@link Metric.Collector}.
   */
  @Override
  public final Metric.Collector metricCollector() {
    return this.metric;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   metricTicker (Cache.Store)
  /**
   ** Returns the {@link Ticker} used by this cache for metrics.
   **
   ** @return                    the {@link Ticker} used by this cache for
   **                            metrics.
   **                            <br>
   **                            Possible object is {@link Ticker}.
   */
  @Override
  public final Ticker metricTicker() {
    return Ticker.zeroTicker();
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   expiry (Cache.Store)
  /**
   ** Returns the {@link Expiry} used by this cache.
   **
   ** @return                  the {@link Expiry} used by this cache.
   **                          <br>
   **                          Possible object is {@link Expiry}.
   */
  @Override
  public final Expiry<K, V> expiry() {
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   expirationTicker (Cache.Store)
  /**
   ** Returns the {@link Ticker} used by this cache for expiration.
   **
   ** @return                  the {@link Ticker} used by this cache for
   **                          expiration.
   **                          <br>
   **                          Possible object is {@link Ticker}.
   */
  @Override
  public final Ticker expirationTicker() {
    return Ticker.zeroTicker();
  }

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
  public final int size() {
    return this.store.size();
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
  public final boolean isEmpty() {
    return this.store.isEmpty();
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
  public final void clear() {
    if (this.removal == null) {
      this.store.clear();
      return;
    }
    for (K key : this.store.keySet())
      remove(key);
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
  public final boolean containsKey(final Object key) {
    return this.store.containsKey(key);
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
  public final boolean containsValue(final Object value) {
    return this.store.containsValue(value);
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
   **                            Possible object is {@link Set} where each
   **                            element is of type <code>K</code>.
   */
  @Override
  public final Set<K> keySet() {
    /*
    final Set<K> ks = this.store.keySet();
    return (ks == null) ? (keySet = new KeySetView<>(this)) : ks;
    */
    return this.store.keySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values (Map)
  /**
   ** Returns a collection view of the values contained in the backing map.
   ** The collection is backed by the backing map, so changes to the backing
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
  public final Collection<V> values() {
    /*
    final Collection<V> vs = values;
    return (vs == null) ? (values = new ValuesView<>(this)) : vs;
    */
    return this.store.values();
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
   **                            Possible object is {@link Set} where
   **                            each element is of type {@link Map.Entry}.
   */
  @Override
  public final Set<Map.Entry<K, V>> entrySet() {
    /**
    final Set<Entry<K, V>> es = entrySet;
    return (es == null) ? (entrySet = new EntrySetView<>(this)) : es;
    */
    return this.store.entrySet();
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
   **                            Possible object is <code>V</code>.
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
    return getIfPresent(key, /* recordMetric */ false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIfPresent (Cache.Store)
  /**
   ** Returns the value associated with the <code>key</code> in this cache, or
   ** <code>null</code> if there is no cached value for the <code>key</code>.
   ** <br>
   ** This method differs by accepting a parameter of whether to record the
   ** hit and miss metrics based on the success of this operation.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  recordMetric       whether to record the hit and miss metrics
   **                            based on the success of this operation.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if this cache contains no
   **                            mapping for the key.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws NullPointerException if the specified key is <code>null</code>.
   */
  @Override
  public final V getIfPresent(final Object key, boolean recordMetric) {
    final V value = this.store.get(key);
    if (recordMetric) {
      if (value == null)
        this.metric.recordMisses(1);
      else
        this.metric.recordHits(1);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllPresent (Cache)
  /**
   ** Returns a map of the values associated with the <code>filter</code> in
   ** this cache.
   ** <br>
   ** The returned map will only contain entries which are already present in
   ** the cache.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** duplicate elements in <code>filter</code>, as determined by
   ** {@link Object#equals}, will be ignored.
   **
   ** @param  filter             the keys whose associated values are to be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link Iterable} of type
   **                            <code>K</code>.
   **
   ** @return                    the unmodifiable mapping of keys to values for
   **                            the specified keys found in this cache.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            elements is of type <code>K</code> for the key
   **                            and <code>V</code> for the value.
   **
   ** @throws NullPointerException if the specified collection is
   **                              <code>null</code> or contains a
   **                              <code>null</code> element.
   */
  @Override
  public final Map<K, V> getAllPresent(final Iterable<? extends K> filter) {
    final Set<K> unique = new LinkedHashSet<>();
    for (K key : filter) {
      unique.add(key);
    }

    int misses = 0;
    final Map<K, V> result = new LinkedHashMap<>(unique.size());
    for (K key : unique) {
      final V value = this.store.get(key);
      if (value == null) {
        misses++;
      }
      else {
        result.put(key, value);
      }
    }
    this.metric.recordMisses(misses);
    this.metric.recordHits(result.size());
    return CollectionUtility.unmodifiable(result);
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
   **                                       not supported by thattribute
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
  @Override
  public final V put(final K key, final V value) {
    Objects.requireNonNull(value);
    // ensures that the removal notification is processed after the removal has
    // completed
    final V old = this.store.put(key, value);
    notifyReplace(key, old, value);
    return old;
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
   **                            Allowed object is {@link Map} where each
   **                            element is of type <code>K</code> for the key
   **                            and <code>V</code> for the value.
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
  public final void putAll(final Map<? extends K, ? extends V> t) {
    if (this.removal == null)
      t.forEach(this::put);
    else
      this.store.putAll(t);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putIfAbsent (ConcurrentMap)
  /**
   ** If the specified key is not already associated with a value, associate it
   ** with the given value.
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
   ** @param  key                the key with which the specified value is
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              to be associated with the specified key.
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
   ** @throws NullPointerException          if the specified key or value is
   **                                       <code>null</code>, and the backing
   **                                       {@link Map} does not permit
   **                                       <code>null</code> keys or values.
   ** @throws ClassCastException            if the key or value is of an
   **                                       inappropriate type for the backing
   **                                       {@link Map}.
   ** @throws IllegalArgumentException      if some property of the specified
   **                                       key or value prevents it from being
   **                                       stored in the backing {@link Map}.
   ** @throws UnsupportedOperationException if the <code>remove</code> operation
   **                                       is not supported by the backing.
   */
  @Override
  public final V putIfAbsent(final K key, final V value) {
    return this.store.putIfAbsent(key, value);
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
   ** the specified key once the call returns.
   **
   ** @param  key                key whose mapping is to be removed from the
   **                            map.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return previous           value associated with specified key, or
   **                            <code>null</code> if there was no mapping for
   **                            key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
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
  public final V remove(final Object key) {
    @SuppressWarnings("unchecked")
    final K   casted = (K)key;
    @SuppressWarnings({"unchecked", "rawtypes"})
    final V[] old   = (V[])new Object[1];
    this.store.computeIfPresent(casted, (k, v) -> {discardRefresh(k); old[0] = v; return null;});
    if (old[0] != null)
      notifyRemove(casted, old[0], Cache.Event.EXPLICIT);

    return old[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (ConcurrentMap)
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
   ** @param  key                the key with which the specified value is
   **                            associated.
   **                            <br>
   **                            Allowed object is {@link Object}.
   ** @param  value              the value expected to be associated with the
   **                            specified key.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the value was removed;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException          if the specified key or value is
   **                                       <code>null</code>, and the backing
   **                                       {@link Map} does not permit
   **                                       <code>null</code> keys or values.
   ** @throws ClassCastException            if the key or value is of an
   **                                       inappropriate type for the backing
   **                                       {@link Map}.
   ** @throws UnsupportedOperationException if the <code>remove</code> operation
   **                                       is not supported by the backing
   **                                       {@link Map}.
   */
  @Override
  public final boolean remove(final Object key, final Object value) {
    if (value == null)
      return false;

    Objects.requireNonNull(key);
    @SuppressWarnings("unchecked")
    K   casted = (K)key;
    @SuppressWarnings({"unchecked", "rawtypes"})
    V[] old    = (V[])new Object[1];

    this.store.computeIfPresent(casted, (k, v) -> {
      if (v.equals(value)) {
        discardRefresh(k);
        old[0] = v;
        return null;
      }
      return v;
    });

    if (old[0] != null) {
      notifyRemove(casted, old[0], Cache.Event.EXPLICIT);
      return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace (ConcurrentMap)
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
   ** @param  key                the key with which the specified value is
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              to be associated with the specified key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @throws NullPointerException          if the specified key or value is
   **                                       <code>null</code>, and the backing
   **                                       {@link Map} does not permit
   **                                       <code>null</code> keys or values.
   ** @throws ClassCastException            if the key or value is of an
   **                                       inappropriate type for the backing
   **                                       {@link Map}.
   ** @throws IllegalArgumentException      if some property of the specified
   **                                       key or value prevents it from being
   **                                       stored in the backing {@link Map}.
   ** @throws UnsupportedOperationException if the <code>remove</code> operation
   **                                       is not supported by the backing
   **                                       {@link Map}.
   */
  @Override
  public final V replace(final K key, final V value) {
    Objects.requireNonNull(value);

    @SuppressWarnings({"unchecked", "rawtypes"})
    V[] oldValue = (V[])new Object[1];
    this.store.computeIfPresent(key, (k, v) -> {
      discardRefresh(k);
      oldValue[0] = v;
      return value;
    });

    if ((oldValue[0] != null) && (oldValue[0] != value)) {
      notifyRemove(key, value, Cache.Event.REPLACED);
    }
    return oldValue[0];
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   replace (ConcurrentMap)
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
   ** @param  key                the key with which the specified value is
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  origin             the value expected to be associated with the
   **                            specified key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   **
   ** @return                    <code>true</code> if the value was replaced;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws NullPointerException          if the specified key or value is
   **                                       <code>null</code>, and the backing
   **                                       {@link Map} does not permit
   **                                       <code>null</code> keys or values.
   ** @throws ClassCastException            if the key or value is of an
   **                                       inappropriate type for the backing
   **                                       {@link Map}.
   ** @throws IllegalArgumentException      if some property of the specified
   **                                       key or value prevents it from being
   **                                       stored in the backing {@link Map}.
   ** @throws UnsupportedOperationException if the <code>remove</code> operation
   **                                       is not supported by the backing
   **                                       {@link Map}.
   */
  @Override
  public final boolean replace(final K key, final V origin, final V value) {
    Objects.requireNonNull(origin);
    Objects.requireNonNull(value);

    @SuppressWarnings({"unchecked", "rawtypes"})
    V[] prev = (V[]) new Object[1];
    this.store.computeIfPresent(key, (k, v) -> {
      if (v.equals(origin)) {
        prev[0] = v;
        discardRefresh(k);
        return value;
      }
      return v;
    });

    boolean replaced = (prev[0] != null);
    if (replaced && (prev[0] != value)) {
      notifyRemove(key, prev[0], Cache.Event.REPLACED);
    }
    return replaced;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compute (Cache.Store)
  /**
   ** Attempts to compute a mapping for the specified key and its current mapped
   ** value (or <code>null</code> if there is no current mapping).
   ** <br>
   ** For example, to either create or append a string message to a value
   ** mapping:
   ** <pre>
   **    memory.compute(key, (k, v) -&gt; (v == null) ? msg : v.concat(msg))
   ** </pre>
   ** <br>
   ** (Method {@link #merge merge()} is often simpler to use for such purposes.)
   ** <p>
   ** If the function returns <code>null</code>, the mapping is removed (or
   ** remains absent if initially absent). If the function itself throws an
   ** (unchecked) exception, the exception is rethrown, and the current mapping
   ** is left unchanged.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** This method differs by accepting parameters indicating whether to record
   ** miss and load metrics based on the success of this operation.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param function            the function to compute a value.
   **                            <br>
   **                            Allowed object is {@link BiFunction}.
   ** @param recordMiss          whether cache miss hit metrics recorded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param recordLoad          whether load metrics are recorded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param recordFailure       whether load failure metrics are recorded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the new value associated with the specified
   **                            key, or <code>null</code> if none.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  @Override
  public final V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> function, final Expiry<K, V> expiry, final boolean recordMiss, final boolean recordLoad, final boolean recordFailure) {
    Objects.requireNonNull(function);
    return remap(key, metricAware(function, recordMiss, recordLoad, recordFailure));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeIfAbsent (Cache.Store)
  /**
   ** If the specified key is not already associated with a value (or is mapped
   ** to <code>null</code>), attempts to compute its value using the given
   ** mapping function and enters it into this map unless <code>null</code>.
   ** <p>
   ** If the function returns <code>null</code> no mapping is recorded. If the
   ** function itself throws an (unchecked) exception, the exception is
   ** rethrown, and no mapping is recorded. The most common usage is to
   ** construct a new object serving as an initial mapped value or memoized
   ** result, as in:
   ** <pre>
   **   memory.computeIfAbsent(key, k -&gt; new Value(f(k)));
   ** </pre>
   ** Or to implement a multi-value map,
   ** <code>Map&lt;K, Collection&lt;V&gt;&gt;</code>, supporting multiple
   ** values per key:
   ** <pre>
   **   memory.computeIfAbsent(key, k -&gt; new HashSet&lt;V&gt;()).add(v);
   ** </pre>
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param function            the function to compute a value.
   **                            <br>
   **                            Allowed object is {@link BiFunction}.
   ** @param recordMetric        whether cache metrics recorded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param recordLoad          whether load metrics recorded.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the current (existing or computed) value
   **                            associated with the specified key, or
   **                            <code>null</code> the computed value is
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  @Override
  public final V computeIfAbsent(K key, Function<? super K, ? extends V> function, boolean recordMetric, boolean recordLoad) {
    Objects.requireNonNull(function);
    // optimistic fast path due to computeIfAbsent always locking
    V value = this.store.get(key);
    if (value != null) {
      if (recordMetric)
        this.metric.recordHits(1);
      return value;
    }

    final boolean[] missed = new boolean[1];
    value = this.store.computeIfAbsent(
      key
    , k -> {
        // sdo not communicate to Cache.Writer on a load
        missed[0] = true;
        return recordMetric ? this.metricAware(function, recordLoad).apply(key) : function.apply(key);
      }
    );
    if (!missed[0] && recordMetric) {
      this.metric.recordHits(1);
    }
    return value;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   notifyRemove (Cache.Store)
  /**
   ** Asynchronously sends a removal notification to the listener.
   **
   ** @param  key                the key with which the specified value is
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  value              the value to be associated with the specified
   **                            key.
   **                            <br>
   **                            Allowed object is <code>V</code>.
   */
  @Override
  public final void notifyRemove(final K key, final V value, Cache.Event cause) {
    if (this.removal == null)
      return;

    Runnable task = () -> {
      try {
        this.removal.onRemoval(key, value, cause);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by removal listener", t);
      }
    };
/*
    try {
      executor.execute(task);
    }
    catch (Throwable t) {
      logger.log(Level.ERROR, "Exception thrown when submitting removal listener", t);
      task.run();
    }
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a <code>Unbounded</code>.
   **
   ** @param  <K>                the most general type of keys the cache
   **                            stores; for example {@link Object} if any
   **                            key is acceptable.
   ** @param  <V>                the most general type of values the cache
   **                            stores; for example {@link Object} if any
   **                            value is acceptable.
   ** @param  builder            the {@link Builder} providing the values to
   **                            populate the instance.
   **                            <br>
   **                            Allowed object is {@link Builder} of type
   **                            <code>K</code> for the key and <code>V</code>
   ** @param  asynchron          <code>true</code> if cache event notifications
   **                            send asynchron.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a pair formed from the two parameters, never
   **                            <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Cache} where each
   **                            element id of type <code>K</code> for the key
   **                            and <code>V</code> for the value.
   */
  public static <K, V> Cache<K, V> of(final Cache.Builder<? super K, ? super V> builder, final boolean asynchron) {
    return new Unbounded<K, V>(builder, asynchron);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   merge
  /**
   ** If the specified key is not already associated with a (non-null) value,
   ** associates it with the given value. Otherwise, replaces the value with
   ** the results of the given remapping function, or removes if
   ** <code>null</code>. The entire method invocation is performed atomically.
   ** Some attempted update operations on this map by other threads may be
   ** blocked while computation is in progress, so the computation should be
   ** short and simple, and must not attempt to update any other mappings of
   ** this {@link Cache}.
   **
   **
   ** @param  key                 the key with which the specified value is to
   **                             be associated.
   **                             <br>
   **                             Allowed object is <code>K</code>.
   ** @param  value               the value to use if absent.
   **                             <br>
   **                             Allowed object is <code>V</code>.
   ** @param  function            the function to compute a value.
   **                             <br>
   **                             Allowed object is {@link BiFunction} with type
   **                             <code>K</code> for the key and <code>V</code>
   **                             for the value.
   **
   ** @return                     the new value associated with the specified
   **                             key, or <code>null</code> if none.
   **                             <br>
   **                             Possible object is <code>V</code>.
   */
  @Override
  public final V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> function) {
    Objects.requireNonNull(value);
    Objects.requireNonNull(function);
    return remap(key, (k, oldValue) -> (oldValue == null) ? value : metricAware(function).apply(oldValue, value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remap
  /**
   ** A {@link Map#compute(Object, BiFunction)} that does not directly record
   ** any cache metrics.
   **
   ** @param  key                 the key with which the specified value is to
   **                             be associated.
   **                             <br>
   **                             Allowed object is <code>K</code>.
   ** @param  function            the function to compute a value.
   **                             <br>
   **                             Allowed object is {@link BiFunction} with type
   **                             <code>K</code> for the key and <code>V</code>
   **                             for the value.
   **
   ** @return                     the new value associated with the specified
   **                             key, or <code>null</code> if none.
   **                             <br>
   **                             Possible object is <code>V</code>.
   */
  final V remap(final K key, final BiFunction<? super K, ? super V, ? extends V> function) {
    // ensures that the removal notification is processed after the removal has
    // completed
    @SuppressWarnings({"unchecked", "rawtypes"})
    final V[]       oldValue = (V[])new Object[1];
    final boolean[] replaced = new boolean[1];
    final V         nv       = this.store.compute(
      key
    , (K k, V value) -> {
        V newValue = function.apply(k, value);
        if ((value == null) && (newValue == null))
          return null;

        replaced[0] = (newValue != null);
        if ((value != null) && (newValue != value))
          oldValue[0] = value;

        discardRefresh(k);
        return newValue;
      }
    );
    if (replaced[0])
      notifyReplace(key, oldValue[0], nv);
    else if (oldValue[0] != null)
      notifyRemove(key, oldValue[0], Cache.Event.EXPLICIT);

    return nv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   computeIfPresent (overridden)
  /**
   ** If the value for the specified key is present, attempts to compute a new
   ** mapping given the key and its current mapped value. The entire method
   ** invocation is performed atomically.
   ** <br>
   ** Some attempted update operations on this map by other threads may be
   ** blocked while computation is in progress, so the computation should be
   ** short and simple, and must not attempt to update any other mappings of
   ** this map.
   **
   ** @param  key                the key with which a value may be associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  function           the function to compute a value.
   **                            <br>
   **                            Allowed object is {@link BiFunction} with type
   **                            <code>K</code> for the key and <code>V</code>
   **                            for the value.
   **
   ** @return                    the new value associated with the specified
   **                            key, or <code>null</code> if none.
   **                            <br>
   **                            Possible object is <code>V</code>.
   */
  @Override
  public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
    Objects.requireNonNull(function);
    // optimistic fast path due to computeIfAbsent always locking
    if (!this.store.containsKey(key)) {
      return null;
    }
    // ensures that the removal notification is processed after the removal has completed
    @SuppressWarnings({"unchecked", "rawtypes"})
    V[] oldValue = (V[]) new Object[1];
    boolean[] replaced = new boolean[1];
    V nv = this.store.computeIfPresent(
      key
    , (K k, V value) -> {
        BiFunction<? super K, ? super V, ? extends V> eee = this.metricAware(function, /* recordMiss */ false, /* recordLoad */ true, /* recordFailure */ true);
        V newValue = eee.apply(k, value);
        replaced[0] = (newValue != null);
        if (newValue != value) {
          oldValue[0] = value;
        }
        discardRefresh(k);
        return newValue;
      }
    );
    if (replaced[0]) {
      notifyReplace(key, oldValue[0], nv);
    }
    else if (oldValue[0] != null) {
      notifyRemove(key, oldValue[0], Cache.Event.EXPLICIT);
    }
    return nv;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   discardRefresh
  /**
   ** Invalidate the in-flight refresh.
   **
   ** @param  reference          the reference to the key in the cache store to
   **                            discard.
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  final void discardRefresh(final Object reference) {
    /*
    var pending = refreshes;
    if (pending != null) {
      pending.remove(reference);
    }
    */
  }
}