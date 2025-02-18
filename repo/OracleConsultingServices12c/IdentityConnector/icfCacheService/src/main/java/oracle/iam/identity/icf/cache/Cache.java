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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   Cache.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Cache.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.cache;

import java.time.Duration;

import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import java.util.function.Supplier;
import java.util.function.Function;
import java.util.function.BiFunction;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executor;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CompletionException;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// interface Cache
// ~~~~~~~~~ ~~~~~
/**
 ** A semi-persistent mapping from keys to values.
 ** <br>
 ** Cache entries are manually added using {@link #put(Object, Object)} or
 ** retrieved using {@link #get(Object, Function)}, and are stored in the cache
 ** until either evicted or manually invalidated.
 ** <p>
 ** Implementations of this interface are expected to be thread-safe, and can be
 ** safely accessed by multiple concurrent threads.
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
public interface Cache<K extends Object, V extends Object> extends ConcurrentMap<K, V> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final int UNSET                    = -1;

  static final int REFRESH_TIME_DEFAULT     = 0;
  static final int EXPIRATION_TIME_DEFAULT  = 0;
  static final int INITIAL_CAPACITY_DEFAULT = 16;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Strength
  enum Strength {WEAK, SOFT;}

  //////////////////////////////////////////////////////////////////////////////
  // enum Event
  /**
   ** The reason why a cached entry was removed.
   */
  static enum Event {
      /**
       ** The entry was manually removed by the user.
       ** <br>
       ** This can result from the user invoking any of the following methods on
       ** the cache or map view.
       ** <ul>
       **   <li>{@link Cache#invalidate}
       **   <li>{@link Cache#invalidateAll(Iterable)}
       **   <li>{@link Cache#invalidateAll()}
       **   <li>{@code LoadingCache#refresh}
       **   <li>{@link java.util.Map#remove}
       **   <li>{@link java.util.Map#computeIfPresent}
       **   <li>{@link java.util.Map#compute}
       **   <li>{@link java.util.Map#merge}
       **   <li>{@link java.util.concurrent.ConcurrentMap#remove}
       ** </ul>
       ** A manual removal may also be performed through the key, value, or
       ** entry collections views by the user invoking any of the following
       ** methods.
       ** <ul>
       **   <li>{@link java.util.Collection#remove}
       **   <li>{@link java.util.Collection#removeAll}
       **   <li>{@link java.util.Collection#removeIf}
       **   <li>{@link java.util.Collection#retainAll}
       **   <li>{@link java.util.Iterator#remove}
       ** </ul>
       */
      EXPLICIT {
        @Override
        public boolean evicted() {
          return false;
        }
      },

    /**
     ** The entry itself was not actually removed, but its value was replaced
     ** by the user.
     ** <br>
     ** This can result from the user invoking any of the following methods on
     ** the cache or map view.
     ** <ul>
     **   <li>{@link Cache#put}
     **   <li>{@link Cache#putAll}
     **   <li>{@code LoadingCache#getAll}
     **   <li>{@code LoadingCache#refresh}
     **   <li>{@link java.util.Map#put}
     **   <li>{@link java.util.Map#putAll}
     **   <li>{@link java.util.Map#replace}
     **   <li>{@link java.util.Map#computeIfPresent}
     **   <li>{@link java.util.Map#compute}
     **   <li>{@link java.util.Map#merge}</li>
     ** </ul>
     */
    REPLACED {
        @Override public boolean evicted() {
          return false;
        }
      },

    /**
     ** The entry was removed automatically because its key or value was
     ** garbage-collected.
     ** <br>
     ** This can occur when using {@link Builder#weakKeys},
     ** {@link Builder#weakValues}, or {@link Builder#softValues}.
     */
    COLLECTED {
        @Override public boolean evicted() {
          return true;
        }
      },

    /**
     ** The entry's expiration timestamp has passed.
     ** <br>
     ** This can occur when using {@link Builder#expireAfterWrite},
     ** {@link Builder#expireAfterAccess}, or
     ** {@link Builder#expireAfter(Expiry)}.
     */
    EXPIRED {
        @Override public boolean evicted() {
          return true;
        }
      },

    /**
     ** The entry was evicted due to size constraints.
     ** <br>
     ** This can occur when using {@link Builder#maximumSize} or
     ** {@link Builder#maximumWeight}.
     */
    SIZE {
        @Override public boolean evicted() {
          return true;
        }
      }
    ;

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: evicted
    /**
     ** Returns <code>true</code> if there was an automatic removal due to
     ** eviction (the cause is neither {@link #EXPLICIT} nor {@link #REPLACED}).
     **
     ** @return                  if the entry was automatically removed due to
     **                          eviction.
     */
    public abstract boolean evicted();
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Listener
  // ~~~~~~~~~ ~~~~~~~~
  /**
   ** An object that can receive a notification when an entry is removed from a
   ** cache. The removal resulting in notification could have occurred to an
   ** entry being manually removed or replaced, or due to eviction resulting
   ** from timed expiration, exceeding a maximum size, or garbage collection.
   ** <p>
   ** An instance may be called concurrently by multiple threads to process
   ** different entries.
   ** <br>
   ** Implementations of this interface should avoid performing blocking calls or
   ** synchronizing on shared resources.
   **
   ** @param  <K>                the most general type of keys this listener can
   **                            listen for; for example {@link Object} if any
   **                            key is acceptable.
   ** @param  <V>                the most general type of values this listener
   **                            can listen for; for example {@link Object} if
   **                            any value is acceptable.
   */
  interface Listener<K extends Object, V extends Object> {

    //////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   onRemoval
    /**
     ** Notifies the listener that a removal occurred at some point in the past.
     ** <p>
     ** This does not always signify that the key is now absent from the cache,
     ** as it may have already been re-added.
     **
     ** @param  key              the key represented by this entry, or
     **                          <code>null</code> if collected.
     **                          <br>
     **                          Alllowed object is <code>K</code>.
     ** @param  value            the value represented by this entry, or
     **                          <code>null</code> if collected.
     **                          <br>
     **                          Alllowed object is <code>K</code>.
     ** @param  event            the reason for which the entry was removed.
     **                          <br>
     **                          Alllowed object is <code>Reason</code>.
     */
    void onRemoval(final K key, final V value, final Event event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // interface Store
  // ~~~~~~~~~ ~~~~~
  /**
   ** An in-memory cache providing thread safety and atomicity guarantees.
   ** <br>
   ** This interface provides an extension to {@link ConcurrentMap} for use with
   ** skeletal implementations.
   */
  interface Store<K, V> extends Cache<K, V> {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Manual
    /**
     ** This class provides a skeletal implementation of the {@link Cache}
     ** interface to minimize the effort required to implement a
     ** {@link Store}.
     */
    interface Manual<K, V> extends Store<K, V> {

      //////////////////////////////////////////////////////////////////////////
      // Methods of implemented interfaces
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: estimatedSize (Cache)
      /**
       ** Returns the approximate number of entries in this cache.
       ** <br>
       ** The value returned is an estimate; the actual count may differ if
       ** there are concurrent insertions or removals, or if some entries are
       ** pending removal due to expiration or weak/soft reference collection.
       ** In the case of stale entries this inaccuracy can be mitigated by
       ** performing a {@link #cleanup()} first.
       **
       ** @return                the estimated number of mappings.
       **                        <br>
       **                        Possible object is <code>long</code>.
       */
      @Override
      default long estimatedSize() {
        return cache().estimatedSize();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: cleanup (Cache)
      /**
       ** Performs any pending maintenance operations needed by the cache.
       ** Exactly which activities are performed -- if any -- is
       ** implementation-dependent.
       */
      @Override
      default void cleanup() {
        cache().cleanup();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: get
      /**
       ** Returns the value associated with the <code>key</code> in this cache,
       ** obtaining that value from the <code>function</code> if necessary. This
       ** method provides a simple substitute for the conventional
       ** "if cached, return; otherwise create, cache and return" pattern.
       ** <p>
       ** If the specified key is not already associated with a value, attempts
       ** to compute its value using the given mapping function and enters it
       ** into this cache unless <code>null</code>. The entire method invocation
       ** is performed atomically, so the function is applied at most once per
       ** key.
       ** <br>
       ** Some attempted update operations on this cache by other threads may be
       ** blocked while the computation is in progress, so the computation
       ** should be short and simple, and must not attempt to update any other
       ** mappings of this cache.
       ** <p>
       ** <b>Warning</b>:
       ** <br>
       ** As with {@code CacheLoader#load}, <code>function</code>
       ** <b>must not</b> attempt to update any other mappings of this cache.
       **
       ** @param  key            the key with which the specified value is to be
       **                        associated.
       ** @param  function       the function to compute a value.
       **
       ** @return                the current (existing or computed) value
       **                        associated with the specified key, or
       **                        <code>null</code> if the computed value is
       **                        <code>null</code>.
       **
       ** @throws RuntimeException      if the <code>function</code> does so, in
       **                               which case the mapping is left
       **                               unestablished.
       ** @throws NullPointerException  if the specified <code>key</code> or
       **                               <code>function</code> is
       **                               <code>null</code>.
       ** @throws IllegalStateException if the computation detectably attempts a
       **                               recursive update to this cache that
       **                               would otherwise never complete.
       */
      @Override
      default V get(final K key, final Function<? super K, ? extends V> function) {
        return cache().computeIfAbsent(key, function);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: getIfPresent (Cache)
      /**
       ** Returns the value associated with the <code>key</code> in this cache, or
       ** <code>null</code> if there is no cached value for the <code>key</code>.
       **
       ** @param  key            the key whose associated value is to be
       **                        returned.
       **                        <br>
       **                        Allowed object is <code>K</code>.
       **
       ** @return                the value to which the specified key is mapped,
       **                        or <code>null</code> if this cache contains no
       **                        mapping for the key.
       **                        <br>
       **                        Possible object is <code>V</code>.
       **
       ** @throws NullPointerException if the specified key is <code>null</code>.
       */
      @Override
      default V getIfPresent(final Object key) {
        return cache().getIfPresent(key, /* recordMetric */true);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: getAll (Cache)
      /**
       ** Returns a map of the values associated with the <code>filter</code>,
       ** creating or retrieving those values if necessary.
       ** <br>
       ** The returned map contains entries that were already cached, combined
       ** with the newly loaded entries; it will never contain <code>null</code>
       ** keys or values.
       ** <p>
       ** A single request to the <code>function</code> is performed for all
       ** keys which are not already present in the cache. All entries returned
       ** by <code>function</code> will be stored in the cache, over-writing any
       ** previously cached values. If another call to {@link #get} tries to
       ** load the value for a key in <code>filter</code>, implementations may
       ** either have that thread load the entry or simply wait for this thread
       ** to finish and return the loaded value. In the case of overlapping
       ** non-blocking loads, the last load to complete will replace the
       ** existing entry.
       ** <br>
       ** <b>Note</b>:
       ** <br>
       ** Multiple threads can concurrently load values for distinct keys.
       ** <p>
       ** <b>Note</b>:
       ** Duplicate elements in <code>filter</code>, as determined by
       ** {@link Object#equals}, will be ignored.
       **
       ** @param  filter         the keys whose associated values are to be
       **                        returned.
       **                        <br>
       **                        Allowed object is {@link Iterable} of type
       **                        <code>K</code>.
       ** @param  function       the function to compute the values.
       **                        <br>
       **                        Allowed object is {@link Function} of type
       **                        <code>K</code>.
       **
       ** @return                an unmodifiable mapping of keys to values for
       **                        the specified keys in this cache.
       **                        <br>
       **                        Possible object is {@link Map} where each
       **                        elements is of type <code>K</code> for the key
       **                        and <code>V</code> for the value.
       **
       ** @throws NullPointerException if the specified collection is
       **                              <code>null</code> or contains a
       **                              <code>null</code> element, or if the map
       **                              returned by the <code>function</code> is
       **                              <code>null</code>.
       ** @throws RuntimeException     if the <code>function</code> does so, in
       **                              which case the mapping is left unestablished.
       */
      @Override
      default Map<K, V> getAll(final Iterable<? extends K> filter, final Function<Iterable<? extends K>, Map<K, V>> function) {
        Objects.requireNonNull(function);
        final Set<K>    unique = new LinkedHashSet<>();
        final Map<K, V> found  = getAllPresent(filter);
        final Map<K, V> result = new LinkedHashMap<>(found.size());
        for (K key : filter) {
          final V value = found.get(key);
          if (value == null)
            unique.add(key);

          result.put(key, value);
        }
        if (unique.isEmpty())
          return found;

        bulk(unique, result, function);
        return CollectionUtility.unmodifiable(result);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: invalidate (Cache)
      /**
       ** Discards any cached value for the <code>key</code>.
       ** <br>
       ** The behavior of this operation is undefined for an entry that is being
       ** loaded (or reloaded) and is otherwise not present.
       **
       ** @param  key            the key whose mapping is to be removed from the
       **                        cache.
       **                        <br>
       **                        Allowed object is <code>K</code>.
       **
       ** @throws NullPointerException if the specified key is <code>null</code>.
       */
      @Override
      default void invalidate(final K key) {
         cache().remove(key);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: invalidateAll
      /**
       ** Discards any cached values for the <code>key</code>.
       ** <br>
       ** The behavior of this operation is undefined for an entry that is being
       ** loaded (or reloaded) and is otherwise not present.
       */
      @Override
      default void invalidateAll() {
        cache().clear();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: invalidateAll
      /**
       ** Discards any cached values for the <code>key</code>.
       ** <br>
       ** The behavior of this operation is undefined for an entry that is being
       ** loaded (or reloaded) and is otherwise not present.
       **
       ** @param  keys           the keys whose associated values are to be
       **                        removed.
       **                        <br>
       **                        Allowed object is {@link Iterable} of type
       **                        <code>K</code>.
       **
       ** @throws NullPointerException if the specified collection is
       **                              <code>null</code> or contains a
       **                              <code>null</code> element.
       */
      @Override
      default void invalidateAll(final Iterable<? extends K> keys) {
        cache().invalidateAll(keys);
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: metric
      /**
       ** Returns a current snapshot of this cache's cumulative metrics.
       ** <br>
       ** All metrics are initialized to zero, and are monotonically
       ** increasing over the lifetime of the cache.
       ** <p>
       ** Due to the performance penalty of maintaining metrics, some
       ** implementations may not record the usage history immediately or at
       ** all.
       **
       ** @return                the current snapshot of the metrics of this
       **                        cache.
       **                        <br>
       **                        Possible object is {@link Metric}.
       */
      @Override
      default Metric metric() {
        return cache().metricCollector().snapshot();
      }

      //////////////////////////////////////////////////////////////////////////
      // Method: map
      /**
       ** Returns a view of the entries stored in this cache as a thread-safe
       ** map.
       ** <br>
       ** Modifications made to the map directly affect the cache.
       ** <p>
       ** A computation operation, such as {@link ConcurrentMap#compute},
       ** performs the entire method invocation atomically, so the function is
       ** applied at most once per key. Some attempted update operations by
       ** other threads may be blocked while computation is in progress. The
       ** computation must not attempt to update any other mappings of this
       ** cache.
       ** <p>
       ** Iterators from the returned map are at least <i>weakly consistent</i>:
       ** they are safe for concurrent use, but if the cache is modified
       ** (including by eviction) after the iterator is created, it is undefined
       ** which of the changes (if any) will be reflected in that iterator.
       **
       ** @return                a thread-safe view of this cache supporting all
       **                        of the optional {@link Map} operations.
       **                        <br>
       **                        Allowed object is {@link ConcurrentMap} where
       **                        each elements is of type <code>K</code> for the
       **                        key and <code>V</code> for the value.
       */
      @Override
      default ConcurrentMap<K, V> map() {
        return cache();
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: cache
      /**
       ** Returns the backing {@link Store} data store.
       **
       ** @return                  the backing {@link Store} data store.
       **                          <br>
       **                          Possible object is {@link Store}.
       */
      Store<K, V> cache();

      //////////////////////////////////////////////////////////////////////////
      // Method: bulk
      /**
       ** Performs a non-blocking bulk load of the missing keys.
       ** <br>
       ** Any missing entry that materializes during the load are replaced when
       ** the loaded entries are inserted into the cache.
       **
       ** @param  filter         the keys whose associated values are to be
       **                        returned.
       **                        <br>
       **                        Allowed object is {@link Iterable} of type
       **                        <code>K</code>.
       ** @param  result         the map to load the missing keys into
       **                        <br>
       **                        Allowed object is {@link Map} where each
       **                        element is of type <code>K</code> for the key
       **                        and <code>V</code> for the value.
       ** @param  function       the function to compute the values.
       **                        <br>
       **                        Allowed object is {@link Function} of type
       **                        <code>K</code>.
       */
      @SuppressWarnings("CatchingUnchecked")
      default void bulk(final Set<K> filter, final Map<K, V> result, final Function<? super Iterable<? extends K>, Map<K, V>> function) {
        boolean    success = false;
        final long start   = cache().metricTicker().read();
        try {
          Map<K, V> loaded = loaded = function.apply(filter);
          loaded.forEach(cache()::put);
          for (K key : filter) {
            V value = loaded.get(key);
            if (value == null) {
              result.remove(key);
            }
            else {
              result.put(key, value);
            }
          }
          success = !loaded.isEmpty();
        }
        catch (RuntimeException e) {
          throw e;
        }
        catch (Exception e) {
          throw new CompletionException(e);
        }
        finally {
          final long elapsed = cache().metricTicker().read() - start;
          if (success) {
            cache().metricCollector().recordSuccess(elapsed);
          }
          else {
            cache().metricCollector().recordFailure(elapsed);
          }
        }
      }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: invalidateAll
    /**
     ** Discards any cached values for the <code>key</code>.
     ** <br>
     ** The behavior of this operation is undefined for an entry that is being
     ** loaded (or reloaded) and is otherwise not present.
     **
     ** @param  keys             the keys whose associated values are to be
     **                          removed.
     **                          <br>
     **                          Allowed object is {@link Iterable} of type
     **                          <code>K</code>.
     **
     ** @throws NullPointerException if the specified collection is
     **                              <code>null</code> or contains a
     **                              <code>null</code> element.
     */
    @Override
    default void invalidateAll(final Iterable<? extends K> keys) {
      for (Object cursor : keys) {
        remove(cursor);
      }
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    //////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordingMetric
    /**
     ** Returns whether this cache has metrics enabled.
     **
     ** @return                  <code>true</code> this cache has metrics
     **                          enabled.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    boolean recordingMetric();

    ////////////////////////////////////////////////////////////////////////////
    // Method: metricCollector
    /**
     ** Returns the {@link Metric.Collector} used by this cache.
     **
     ** @return                  the {@link Metric.Collector} used by this
     **                          cache.
     **                          <br>
     **                          Possible object is {@link Metric.Collector}.
     */
    Metric.Collector metricCollector();

    ////////////////////////////////////////////////////////////////////////////
    // Method: metricTicker
    /**
     ** Returns the {@link Ticker} used by this cache for metrics.
     **
     ** @return                  the {@link Ticker} used by this cache for
     **                          metrics.
     **                          <br>
     **                          Possible object is {@link Ticker}.
     */
    Ticker metricTicker();

    ////////////////////////////////////////////////////////////////////////////
    // Method: expiry
    /**
     ** Returns the {@link Expiry} used by this cache.
     **
     ** @return                  the {@link Expiry} used by this cache.
     **                          <br>
     **                          Possible object is {@link Expiry}.
     */
    Expiry<K, V> expiry();

    ////////////////////////////////////////////////////////////////////////////
    // Method: expirationTicker
    /**
     ** Returns the {@link Ticker} used by this cache for expiration.
     **
     ** @return                  the {@link Ticker} used by this cache for
     **                          expiration.
     **                          <br>
     **                          Possible object is {@link Ticker}.
     */
    Ticker expirationTicker();
    ////////////////////////////////////////////////////////////////////////////
    // Method: getIfPresent
    /**
     ** Returns the value associated with the <code>key</code> in this cache, or
     ** <code>null</code> if there is no cached value for the <code>key</code>.
     **
     ** @param  key              the key whose associated value is to be
     **                          returned.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     **
     ** @return                  the value to which the specified key is mapped,
     **                          or <code>null</code> if this cache contains no
     **                          mapping for the key.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws NullPointerException if the specified key is <code>null</code>.
     **
     ** @see    #getIfPresent(Object, boolean)
     */
    V getIfPresent(final Object key);

    ////////////////////////////////////////////////////////////////////////////
    // Method: getIfPresent
    /**
     ** Returns the value associated with the <code>key</code> in this cache, or
     ** <code>null</code> if there is no cached value for the <code>key</code>.
     ** <br>
     ** This method differs by accepting a parameter of whether to record the
     ** hit and miss metrics based on the success of this operation.
     **
     ** @param  key              the key whose associated value is to be
     **                          returned.
     **                          <br>
     **                          Allowed object is {@link Object}.
     ** @param  recordMetric     whether to record the hit and miss metrics
     **                          based on the success of this operation.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the value to which the specified key is mapped,
     **                          or <code>null</code> if this cache contains no
     **                          mapping for the key.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws NullPointerException if the specified key is <code>null</code>.
     **
     ** @see    #getIfPresent(Object)
     */
     V getIfPresent(final Object key, final boolean recordMetric);

    ////////////////////////////////////////////////////////////////////////////
    // Method: compute
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
     **
     ** @param  key              the key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param function          the function to compute a value.
     **                          <br>
     **                          Allowed object is {@link BiFunction}.
     **
     ** @return                  the new value associated with the specified
     **                          key, or <code>null</code> if none.
     **                          <br>
     **                          Possible object is <code>V</code>.
     */
    @Override
    default V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> function) {
      return compute(key, function, expiry(), /* recordMiss */false, /* recordSuccess */true, /* recordFailure */true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: compute
    /**
     ** Attempts to compute a mapping for the specified key and its current
     ** mapped value (or <code>null</code> if there is no current mapping).
     ** <br>
     ** For example, to either create or append a string message to a value
     ** mapping:
     ** <pre>
     **    memory.compute(key, (k, v) -&gt; (v == null) ? msg : v.concat(msg))
     ** </pre>
     ** <br>
     ** (Method {@link #merge merge()} is often simpler to use for such
     ** purposes.)
     ** <p>
     ** If the function returns <code>null</code>, the mapping is removed (or
     ** remains absent if initially absent). If the function itself throws an
     ** (unchecked) exception, the exception is rethrown, and the current
     ** mapping is left unchanged.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** This method differs by accepting parameters indicating whether to record
     ** miss and load metrics based on the success of this operation.
     **
     ** @param  key              the key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param function          the function to compute a value.
     **                          <br>
     **                          Allowed object is {@link BiFunction}.
     ** @param expiry            the {@link Expiry} used by this cache.
     **                          <br>
     **                          Allowed object is {@link Expiry}.
     ** @param recordMiss        whether cache miss hit metrics recorded.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param recordSuccess     whether load success metrics recorded.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param recordFailure     whether load failure metrics recorded.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the new value associated with the specified
     **                          key, or <code>null</code> if none.
     **                          <br>
     **                          Possible object is <code>V</code>.
     */
    V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> function, final Expiry<K, V> expiry, final boolean recordMiss, final boolean recordSuccess, final boolean recordFailure);

    ////////////////////////////////////////////////////////////////////////////
    // Method: computeIfAbsent
    /**
     ** If the specified key is not already associated with a value (or is
     ** mapped to <code>null</code>), attempts to compute its value using the
     ** given mapping function and enters it into this map unless
     ** <code>null</code>.
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
     ** @param  key              the key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  function         the function to compute a value.
     **                          <br>
     **                          Allowed object is {@link BiFunction}.
     **
     ** @return                  the current (existing or computed) value
     **                          associated with the specified key, or
     **                          <code>null</code> the computed value is
     **                          <code>null</code>.
     **                          <br>
     **                          Possible object is <code>V</code>.
     */
    @Override
    default V computeIfAbsent(final K key, final Function<? super K, ? extends V> function) {
      return computeIfAbsent(key, function, /* cacheMetrics */true, /* loadMetrics */true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: computeIfAbsent
    /**
     ** If the specified key is not already associated with a value (or is
     ** mapped to <code>null</code>), attempts to compute its value using the
     ** given mapping function and enters it into this map unless
     ** <code>null</code>.
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
     ** @param  key              the key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param function          the function to compute a value.
     **                          <br>
     **                          Allowed object is {@link BiFunction}.
     ** @param cacheMetrics      whether cache metrics recorded.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param loadMetrics       whether load metrics recorded.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  the current (existing or computed) value
     **                          associated with the specified key, or
     **                          <code>null</code> the computed value is
     **                          <code>null</code>.
     **                          <br>
     **                          Possible object is <code>V</code>.
     */
    V computeIfAbsent(final K key, final Function<? super K, ? extends V> function, final boolean cacheMetrics, final boolean loadMetrics);

    ////////////////////////////////////////////////////////////////////////////
    // Method: metricAware
    /**
     ** Decorates the remapping function to record metrics if enabled.
     **
     ** @param  <T>                the type of the first argument to the
     **                            function.
     ** @param  <U>                the type of the second argument to the
     **                            function.
     ** @param  <R>                the type of the result of the function.
     ** @param  function           the function to compute a value.
     **                            <br>
     **                            Allowed object is {@link BiFunction}.
     **
     ** @return                    the decorected remapping function.
     **                            <br>
     **                            Possible object is {@link BiFunction}.
     */
    default <T, U, R> BiFunction<? super T, ? super U, ? extends R> metricAware(final BiFunction<? super T, ? super U, ? extends R> function) {
      return metricAware(function, /* recordMiss */true, /* recordSuccess */true, /* recordFailure */true);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: metricAware
    /**
     ** Decorates the remapping function to record metrics if enabled.
     **
     ** @param  <T>                the type of the first argument to the
     **                            function.
     ** @param  <R>                the type of the result of the function.
     ** @param function            the function to compute a value.
     **                            <br>
     **                            Allowed object is {@link BiFunction}.
     ** @param metrics             whether cache metrics are recorded.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     **
     ** @return                    the decorected remapping function.
     **                            <br>
     **                            Possible object is {@link BiFunction}.
     */
    default <T, R> Function<? super T, ? extends R> metricAware(final Function<? super T, ? extends R> function, final boolean metrics) {
      if (!recordingMetric())
        return function;

      return key -> {
        R value;
        metricCollector().recordMisses(1);
        long startTime = metricTicker().read();
        try {
          value = function.apply(key);
        }
        catch (RuntimeException | Error e) {
          metricCollector().recordFailure(metricTicker().read() - startTime);
          throw e;
        }
        long elapsed = metricTicker().read() - startTime;
        if (metrics) {
          if (value == null) {
            metricCollector().recordFailure(elapsed);
          }
          else {
            metricCollector().recordSuccess(elapsed);
          }
        }
        return value;
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: metricAware
    /**
     ** Decorates the remapping function to record metrics if enabled.
     **
     ** @param  <T>                the type of the first argument to the
     **                            function.
     ** @param  <U>                the type of the second argument to the
     **                            function.
     ** @param  <R>                the type of the result of the function.
     ** @param function            the function to compute a value.
     **                            <br>
     **                            Allowed object is {@link BiFunction}.
     ** @param recordMiss          whether cache miss hit metrics recorded.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     ** @param metrics             whether load metrics are recorded.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     ** @param recordFailure       whether load failure metrics are recorded.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     **
     ** @return                    the decorected remapping function.
     **                            <br>
     **                            Possible object is {@link BiFunction}.
     */
  default <T, U, R> BiFunction<? super T, ? super U, ? extends R> metricAware(final BiFunction<? super T, ? super U, ? extends R> function, final boolean recordMiss, final boolean metrics, final boolean recordFailure) {
      if (!recordingMetric())
        return function;

    return (t, u) -> {
      R result;
      if ((u == null) && recordMiss) {
        metricCollector().recordMisses(1);
      }
      long startTime = metricTicker().read();
      try {
        result = function.apply(t, u);
      } catch (RuntimeException | Error e) {
        if (recordFailure) {
          metricCollector().recordFailure(metricTicker().read() - startTime);
        }
        throw e;
      }
      long elapsed = metricTicker().read() - startTime;
      if (metrics) {
        if (result == null) {
          metricCollector().recordFailure(elapsed);
        }
        else {
          metricCollector().recordSuccess(elapsed);
        }
      }
      return result;
    };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notifyReplace
    /**
     ** Notify the removal listener of a replacement if the value reference was
     ** changed.
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
     */
    @SuppressWarnings("FutureReturnValueIgnored")
    default void notifyReplace(final K key, final V origin, final V value) {
      if ((origin == null) || (origin == value))
        return;

      notifyRemove(key, origin, Event.REPLACED);

      /*
    if ((oldValue == null) || (oldValue == newValue)) {
      return;
    } else if (isAsync()) {
      var oldFuture = (CompletableFuture<?>)oldValue;
      var newFuture = (CompletableFuture<?>) newValue;
      newFuture.whenCompleteAsync((nv, e) -> {
        if (e == null) {
          oldFuture.thenAcceptAsync(ov -> {
            if (nv != ov) {
              notifyRemove(key, oldValue, RemovalCause.REPLACED);
            }
          }, executor());
        } else {
          notifyRemove(key, oldValue, RemovalCause.REPLACED);
        }
      }, executor());
    } else {
      notifyRemove(key, oldValue, RemovalCause.REPLACED);
   */
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: notifyRemove
    /**
     ** Asynchronously sends a removal notification to the listener.
     **
     ** @param  key              the key with which the specified value is
     **                          associated.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  value            the value to be associated with the specified
     **                          key.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  event            the reason for the removal.
     **                          <br>
     **                          Allowed object is {@link Event}}.
     */
    void notifyRemove(final K key, final V value, final Event event);
  }

  //////////////////////////////////////////////////////////////////////////////
  // final class Builder
  // ~~~~~ ~~~~~ ~~~~~~~
  static final class Builder<K extends Object, V extends Object> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    long                           maximumSize       = UNSET;
    long                           maximumWeight     = UNSET;
    private int                    initialCapacity   = UNSET;

    Strength                       strengthKey;
    Strength                       strengthValue;

    long                           expireAfterWrite  = UNSET;
    long                           expireAfterAccess = UNSET;
    long                           refreshAfterWrite = UNSET;

    Ticker                         metricTicker;
    Supplier<Metric.Collector>     metricSupplier;

    Expiry<? super K, ? super V>   expiry;
    Weighter<? super K, ? super V> weighter;
    Listener<? super K, ? super V> evictionListener;
    Listener<? super K, ? super V> removalListener;

    Scheduler                      scheduler;
    Executor                       executor;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a default cache <code>Builder</code> that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Builder()" and enforces use of the public method below.
     */
    private Builder() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   initialCapacity
    /**
     ** Sets the minimum total size for the internal data structures.
     ** <br>
     ** Providing a large enough estimate at construction time avoids the need
     ** for expensive resizing operations later, but setting this value
     ** unnecessarily high wastes memory.
     **
     ** @param  value            the minimum total size for the internal data
     **                          structures.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException    if an initial capacity was already set.
     ** @throws IllegalArgumentException if <code>value</code> is negative.
     */
    public Builder<K, V> initialCapacity(final int value) {
      requireState((this.initialCapacity == UNSET), "Initial capacity was already set to %s", this.initialCapacity);
      requireArgument(value >= 0);
      this.initialCapacity = value;
      return this;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method:   initialCapacity
    /**
     ** Retruns the minimum total size for the internal data structures.
     ** <br>
     ** Providing a large enough estimate at construction time avoids the need
     ** for expensive resizing operations later, but setting this value
     ** unnecessarily high wastes memory.
     **
     ** @return                  the minimum total size for the internal data
     **                          structures.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws IllegalStateException    if an initial capacity was already set.
     ** @throws IllegalArgumentException if <code>value</code> is negative.
     */
   int initialCapacity() {
    return this.initialCapacity == UNSET ? INITIAL_CAPACITY_DEFAULT :  this.initialCapacity ;
   }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumSize
    /**
     ** Specifies the maximum number of entries the cache may contain.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** The cache <b>may evict an entry before this limit is exceeded or
     ** temporarily exceed the threshold while evicting</b>.
     ** <p>
     ** As the cache size grows close to the maximum, the cache evicts entries
     ** that are less likely to be used again. For example, the cache may evict
     ** an entry because it hasn't been used recently or very often.
     ** <p>
     ** When <code>size</code> is zero, elements will be evicted immediately
     ** after being loaded into the cache. This can be useful in testing, or to
     ** disable caching temporarily without a code change.
     ** <br>
     ** As eviction is scheduled on the configured {@link #executor}, tests may
     ** instead prefer to configure the cache to execute tasks directly on the
     ** same thread.
     ** <p>
     ** This feature cannot be used in conjunction with {@link #maximumWeight}.
     **
     ** @param  value            the maximum size of the cache.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>size</code> is negative.
     ** @throws IllegalStateException    if a maximum size or weight was already
     **                                  set.
     */
    public Builder<K, V> maximumSize(final long value) {
      requireState(this.maximumSize   == UNSET, "Maximum size was already set to %s",   this.maximumSize);
      requireState(this.maximumWeight == UNSET, "Maximum weight was already set to %s", this.maximumWeight);
      requireState(this.weighter      == null,  "Maximum size can not be combined with weighter");
      requireArgument(value           >= 0,     "Maximum size must not be negative");
      this.maximumSize = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: maximumWeight
    /**
     ** Specifies the maximum weight of entries the cache may contain.
     ** <br>
     ** Weight is determined using the {@link Weighter} specified with
     ** {@code #weigther(Weighter)}, and use of this method requires a
     ** corresponding call to {@code #weigther(Weighter)} prior to calling
     ** {@link #build}.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The cache <b>may evict an entry before this limit is exceeded or
     ** temporarily exceed the threshold while evicting</b>. As the cache size
     ** grows close to the maximum, the cache evicts entries that are less likely
     ** to be used again. For example, the cache may evict an entry because it
     ** hasn't been used recently or very often.
     ** <p>
     ** When <code>maximumWeight</code> is zero, elements will be evicted
     ** immediately after being loaded into the cache. This can be useful in
     ** testing, or to disable caching temporarily without a code change. As
     ** eviction is scheduled on the configured {@link #executor}, tests may
     ** instead prefer to configure the cache to execute tasks directly on the
     ** same thread.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Weight is only used to determine whether the cache is over capacity; it
     ** has no effect on selecting which entry should be evicted next.
     ** <p>
     ** This feature cannot be used in conjunction with {@link #maximumSize}.
     **
     ** @param  value            the maximum total weight of entries the cache
     **                          may contain.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>maximumWeight</code> is
     **                                  negative.
     ** @throws IllegalStateException    if a maximum weight or size was already
     **                                  set.
     */
    public Builder<K, V> maximumWeight(final long value) {
      requireState(this.maximumWeight == UNSET, "Maximum weight was already set to %s", this.maximumWeight);
      requireState(this.maximumSize   == UNSET, "Maximum size was already set to %s", this.maximumSize);
      requireArgument(value           >= 0,     "Maximum weight must not be negative");
      this.maximumWeight = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: weighter
    /**
     ** Specifies the weighter to use in determining the weight of entries.
     ** <br>
     ** Entry weight is taken into consideration by {@link #maximumWeight(long)}
     ** when determining which entries to evict, and use of this method requires
     ** a corresponding call to {@link #maximumWeight(long)} prior to calling
     ** {@link #build}.
     ** <br>
     ** Weights are measured and recorded when entries are inserted into or
     ** updated in the cache, and are thus effectively static during the
     ** lifetime of a cache entry.
     ** <p>
     ** When the weight of an entry is zero it will not be considered for
     ** size-based eviction (though it still may be evicted by other means).
     ** <p>
     ** <b>Important</b>:
     ** <br>
     ** Instead of returning <em>this</em> as a <code>Builder</code> instance,
     ** this method returns <code>Builder&lt;K1, V1&gt;</code>. From this point
     ** on, either the original reference or the returned reference may be used
     ** to complete configuration and build the cache, but only the "generic"
     ** one is type-safe. That is, it will properly prevent you from building
     ** caches whose key or value types are incompatible with the types accepted
     ** by the weighter already provided; the <code>Builder</code> type cannot
     ** do this. For best results, simply use the standard method-chaining
     ** idiom, as illustrated in the documentation at top, configuring a
     ** <code>Builder</code> and building your {@link Cache} all in a single
     ** statement.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** If you ignore the above advice, and use this <code>Builder</code> to
     ** build a cache whose key or value type is incompatible with the weighter,
     ** you will likely experience a {@link ClassCastException} at some
     ** <i>undefined</i> point in the future.
     **
     ** @param  <K1>             the key type of the weighter.
     ** @param  <V1>             the value type of the weighter.
     ** @param  value            the weighter to use in calculating the weight
     **                          of cache entries.
     **                          <br>
     **                          Allowed object is {@link Weighter}.
     **
     ** @return                  the cache builder reference that should be used
     **                          instead of <code>this</code> for any remaining
     **                          configuration and cache building.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException if a weighter was already set.
     */
    public <K1 extends K, V1 extends V> Builder<K1, V1> weighter(final Weighter<? super K1, ? super V1> value) {
      Objects.requireNonNull(value);
      requireState(this.weighter    == null,  "Weighter was already set to %s", this.weighter);
      requireState(this.maximumSize == UNSET, "Weighter can not be combined with maximum size");

      @SuppressWarnings("unchecked")
      Builder<K1, V1> self = (Builder<K1, V1>)this;
      self.weighter = value;
      return self;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: weakKeys
    /**
     ** Specifies that each key (not value) stored in the cache should be
     ** wrapped in a {@code WeakReference} (by default, strong references are
     ** used).
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** When this method is used, the resulting cache will use identity
     ** (<code>==</code>) comparison to determine equality of keys. Its
     ** {@link Cache#map} view will therefore technically violate the
     ** {@link Map} specification (in the same way that
     ** {@code IdentityHashMap} does).
     ** <p>
     ** Entries with keys that have been garbage collected may be counted in
     ** {@link Cache#estimatedSize()}, but will never be visible to read or
     ** write operations; such entries are cleaned up as part of the routine
     ** maintenance described in the class javadoc.
     ** <p>
     ** This feature cannot be used in conjunction when {@link #weakKeys()} is
     ** combined with {@code #buildAsync}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException if the key strength was already set.
     */
    public Builder<K, V> weakKeys() {
      requireState(this.strengthKey == null, "Key strength was already set to %s", this.strengthKey);
      this.strengthKey = Strength.WEAK;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: weakValues
    /**
     ** Specifies that each value (not key) stored in the cache should be
     ** wrapped in a {@code WeakReference} (by default, strong references are
     ** used).
     ** <p>
     ** Weak values will be garbage collected once they are weakly reachable.
     ** This makes them a poor candidate for caching; consider
     ** {@link #softValues} instead.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** When this method is used, the resulting cache will use identity
     ** (<code>==</code>) comparison to determine equality of values.
     ** <p>
     ** Entries with values that have been garbage collected may be counted in
     ** {@link Cache#estimatedSize()}, but will never be visible to read or
     ** write operations; such entries are cleaned up as part of the routine
     ** maintenance described in the class javadoc.
     ** <p>
     ** This feature cannot be used in conjunction with {@code #buildAsync}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException if the value strength was already set.
     */
    public Builder<K, V> weakValues() {
      requireState(this.strengthValue == null, "Value strength was already set to %s", this.strengthValue);
      this.strengthValue = Strength.WEAK;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: softValues
    /**
     ** Specifies that each value (not key) stored in the cache should be
     ** wrapped in a {@code SoftReference} (by default, strong references are
     ** used). Softly-referenced objects will be garbage-collected in a
     ** <i>globally</i> least-recently-used manner, in response to memory
     ** demand.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** In most circumstances it is better to set a per-cache
     ** {@link #maximumSize(long) maximum size} instead of using soft
     ** references. You should only use this method if you are very familiar
     ** with the practical consequences of soft references.
     ** <p>
     ** <b>Warning</b>:
     ** When this method is used, the resulting cache will use identity
     ** (<code>==</code>) comparison to determine equality of values.
     ** <p>
     ** Entries with values that have been garbage collected may be counted in
     ** {@link Cache#estimatedSize()}, but will never be visible to read or
     ** write operations; such entries are cleaned up as part of the routine
     ** maintenance described in the class javadoc.
     ** <p>
     ** This feature cannot be used in conjunction with {@code #buildAsync}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException if the value strength was already set.
     */
    public Builder<K, V> softValues() {
      requireState(this.strengthValue == null, "Value strength was already set to %s", this.strengthValue);
      this.strengthValue = Strength.SOFT;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expireAfterWrite
    /**
     ** Specifies that each entry should be automatically removed from the cache
     ** once a fixed duration has elapsed after the entry's creation, or the
     ** most recent replacement of its value.
     ** <p>
     ** Expired entries may be counted in {@link Cache#estimatedSize()}, but
     ** will never be visible to read or write operations. Expired entries are
     ** cleaned up as part of the routine maintenance described in the class
     ** javadoc. A {@link #scheduler(Scheduler)} may be configured for a prompt
     ** removal of expired entries.
     **
     ** @param  duration         the length of time after an entry is created
     **                          that it should be automatically removed.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is negative.
     ** @throws IllegalStateException    if the time to live or variable
     **                                  expiration was already set.
     ** @throws ArithmeticException      for durations greater than +/-
     **                                  approximately 292 years.
     */
    public Builder<K, V> expireAfterWrite(final Duration duration) {
      return expireAfterWrite(nanoSecond(duration), TimeUnit.NANOSECONDS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expireAfterWrite
    /**
     ** Specifies that each entry should be automatically removed from the cache
     ** once a fixed duration has elapsed after the entry's creation, or the
     ** most recent replacement of its value.
     ** <p>
     ** Expired entries may be counted in {@link Cache#estimatedSize()}, but
     ** will never be visible to read or write operations. Expired entries are
     ** cleaned up as part of the routine maintenance described in the class
     ** javadoc. A {@link #scheduler(Scheduler)} may be configured for a prompt
     ** removal of expired entries.
     ** <p>
     ** If you can represent the duration as a {@link java.time.Duration} (which
     ** should be preferred when feasible), use
     ** {@link #expireAfterWrite(Duration)} instead.
     **
     ** @param  duration         the length of time after an entry is created
     **                          that it should be automatically removed.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     ** @param  unit             the unit that <code>duration</code> is
     **                          expressed in.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is negative.
     ** @throws IllegalStateException    if the time to live or variable
     **                                  expiration was already set.
     */
    public Builder<K, V> expireAfterWrite(final long duration, final TimeUnit unit) {
      requireState(this.expireAfterWrite == UNSET, "ExpireAfterWrite was already set to %s ns", this.expireAfterWrite);
      requireState(this.expiry           == null,  "ExpireAfterWrite may not be used with variable expiration");
      requireArgument(duration           >= 0,     "Duration cannot be negative: %s %s", duration, unit);
      this.expireAfterWrite = unit.toNanos(duration);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expireAfterAccess
    /**
     ** Specifies that each entry should be automatically removed from the cache
     ** once a fixed duration has elapsed after the entry's creation, the most
     ** recent replacement of its value, or its last access. Access time is
     ** reset by all cache read and write operations (including
     ** {@code Cache.map().get(Object)} and {@code Cache.map().put(K, V)}), but
     ** not by operations on the collection-views of {@link Cache#map()}.
     ** <p>
     ** Expired entries may be counted in {@link Cache#estimatedSize()}, but
     ** will never be visible to read or write operations. Expired entries are
     ** cleaned up as part of the routine maintenance described in the class
     ** javadoc. A {@link #scheduler(Scheduler)} may be configured for a prompt
     ** removal of expired entries.
     **
     ** @param  duration         the length of time after an entry is last
     **                          accessed that it should be automatically
     **                          removed.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is negative.
     ** @throws IllegalStateException    if the time to idle or variable
     **                                  expiration was already set.
     ** @throws ArithmeticException      for durations greater than +/-
     **                                  approximately 292 years.
     */
    public Builder<K, V> expireAfterAccess(final Duration duration) {
      return expireAfterAccess(nanoSecond(duration), TimeUnit.NANOSECONDS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expireAfterAccess
    /**
     ** Specifies that each entry should be automatically removed from the cache
     ** once a fixed duration has elapsed after the entry's creation, the most
     ** recent replacement of its value, or its last read. Access time is reset
     ** by all cache read and write operations (including
     ** {@code Cache.map().get(Object)} and {@code Cache.map().put(K, V)}), but
     ** not by operations on the collection-views of {@link Cache#map()}.
     ** <p>
     ** Expired entries may be counted in {@link Cache#estimatedSize()}, but
     ** will never be visible to read or write operations. Expired entries are
     ** cleaned up as part of the routine maintenance described in the class
     ** javadoc. A {@link #scheduler(Scheduler)} may be configured for a prompt
     ** removal of expired entries.
     ** <p>
     ** If you can represent the duration as a {@link Duration} (which should be
     ** preferred when feasible), use {@link #expireAfterAccess(Duration)}
     ** instead.
     **
     ** @param  duration         the length of time after an entry is last
     **                          accessed that it should be automatically
     **                          removed.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     ** @param  unit             the unit that <code>duration</code> is
     **                          expressed in.
     **                          <br>
     **                          Allowed object is {@link TimeUnit}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is negative.
     ** @throws IllegalStateException    if the time to idle or variable
     **                                  expiration was already set.
     */
    public Builder<K, V> expireAfterAccess(final long duration, final TimeUnit unit) {
      requireState(this.expireAfterAccess == UNSET, "ExpireAfterAccess was already set to %s ns", this.expireAfterAccess);
      requireState(this.expiry            == null,  "ExpireAfterAccess may not be used with variable expiration");
      requireArgument(duration            >= 0,     "Duration cannot be negative: %s %s", duration, unit);
      this.expireAfterAccess = unit.toNanos(duration);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: expireAfter
    /**
     ** Specifies that each entry should be automatically removed from the cache
     ** once a duration has elapsed after the entry's creation, the most recent
     ** replacement of its value, or its last read. The expiration time is reset
     ** by all cache read and write operations (including
     ** {@code Cache.map().get(Object)} and {@code Cache.map().put(K, V)}), but
     ** not by operations on the collection-views of {@link Cache#map}.
     ** <p>
     ** Expired entries may be counted in {@link Cache#estimatedSize()}, but
     ** will never be visible to read or write operations. Expired entries are
     ** cleaned up as part of the routine maintenance described in the class
     ** javadoc. A {@link #scheduler(Scheduler)} may be configured for a prompt
     ** removal of expired entries.
     ** <p>
     ** <b>Important</b>:
     ** <br>
     ** After invoking this method, do not continue to use <i>this</i> cache
     ** builder reference; instead use the reference this method <i>returns</i>.
     ** At runtime, these point to the same instance, but only the returned
     ** reference has the correct generic type information so as to ensure typ
     ** safety. For best results, use the standard method-chaining idiom
     ** illustrated in the class documentation above, configuring a builder and
     ** building your cache in a single statement. Failure to heed this advice
     ** can result in a {@link ClassCastException} being thrown by a cache
     ** operation at some <i>undefined</i> point in the future.
     **
     ** @param  <K1>             the most general key type this
     **                          <code>Expiry</code> will be able to compute.
     **                          This is normally {@link Object}.
     ** @param  <V1>             the most general value type this
     **                          <code>Expiry</code> will be able to compute.
     **                          This is normally {@link Object}.
     ** @param  value            the expiry to use in calculating the expiration
     **                          time of cache entries.
     **                          <br>
     **                          Allowed object is {@link Expiry}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalStateException if expiration was already set.
     */
    public <K1 extends K, V1 extends V> Builder<K1, V1> expireAfter(final Expiry<? super K1, ? super V1> value) {
      Objects.requireNonNull(value);
      requireState(this.expiry            == null,  "Expiry was already set to %s", this.expiry);
      requireState(this.expireAfterAccess == UNSET, "Expiry may not be used with expiresAfterAccess");
      requireState(this.expireAfterWrite  == UNSET, "Expiry may not be used with expiresAfterWrite");
      @SuppressWarnings("unchecked") Builder<K1, V1> self = (Builder<K1, V1>)this;
      self.expiry = value;
      return self;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshAfterWrite
    /**
     ** Specifies that active entries are eligible for automatic refresh once a
     ** fixed duration has elapsed after the entry's creation, or the most
     ** recent replacement of its value. The semantics of refreshes are
     ** specified in {@code LoadingCache#refresh}, and are performed by calling
     ** {@code CacheLoader#reload}.
     ** <p>
     ** Automatic refreshes are performed when the first stale request for an
     ** entry occurs. The request triggering refresh will make an asynchronous
     ** call to {@code CacheLoader#reload} and immediately return the old value.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** <i>All exceptions thrown during refresh will be logged and then
     ** swallowed</i>.
     **
     ** @param  duration         the length of time after an entry is created
     **                          that it should be considered stale, and thus
     **                          eligible for refresh.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is zero or
     **                                  negative.
     ** @throws IllegalStateException    if the refresh interval was already
     **                                  set.
     ** @throws ArithmeticException      for durations greater than +/-
     **                                  approximately 292 years.
     */
    public Builder<K, V> refreshAfterWrite(final Duration duration) {
      return refreshAfterWrite(nanoSecond(duration), TimeUnit.NANOSECONDS);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: refreshAfterWrite
    /**
     ** Specifies that active entries are eligible for automatic refresh once a
     ** fixed duration has elapsed after the entry's creation, or the most
     ** recent replacement of its value. The semantics of refreshes are
     ** specified in {@code LoadingCache#refresh}, and are performed by calling
     ** {@code CacheLoader#reload}.
     ** <p>
     ** Automatic refreshes are performed when the first stale request for an
     ** entry occurs. The request triggering refresh will make an asynchronous
     ** call to {@code CacheLoader#reload} and immediately return the old value.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** <i>All exceptions thrown during refresh will be logged and then
     ** swallowed</i>.
     ** <p>
     ** If you can represent the duration as a {@link Duration} (which should be
     ** preferred when feasible), use {@link #refreshAfterWrite(Duration)}
     ** instead.
     **
     ** @param  duration         the length of time after an entry is created
     **                          that it should be considered stale, and thus
     **                          eligible for refresh.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     ** @param  unit             the unit that <code>duration</code> is
     **                          expressed in.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws IllegalArgumentException if <code>duration</code> is zero or
     **                                  negative.
     ** @throws IllegalStateException    if the refresh interval was already
     **                                  set.
     */
    public Builder<K, V> refreshAfterWrite(final long duration, final TimeUnit unit) {
      Objects.requireNonNull(unit);
      requireState(this.refreshAfterWrite == UNSET, "RefreshAfterWrite was already set to %s ns", this.refreshAfterWrite);
      requireArgument(duration            >  0,     "Duration must be positive: %s %s", duration, unit);
      this.refreshAfterWrite = unit.toNanos(duration);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: evictionListener
    /**
     ** Specifies a listener instance that caches should notify each time an
     ** entry is evicted. The cache will invoke this listener during the atomic
     ** operation to remove the entry. In the case of expiration or reference
     ** collection, the entry may be pending removal and will be discarded as
     ** part of the routine maintenance described in the class documentation
     ** above. For a more prompt notification on expiration a
     ** {@link #scheduler(Scheduler)} may be configured. A
     ** {@code #removalListener(Listener)} may be preferred when the listener
     ** should be invoked for any {@link Event event}, be performed outside of
     ** the atomic operation to remove the entry, and delegated to
     ** the configured {@link #executor(Executor)}.
     ** <p>
     ** <b>Important</b>:
     ** <br>
     ** After invoking this method, do not continue to use <i>this</i> cache
     ** builder reference; instead use the reference this method <i>returns</i>.
     ** At runtime, these point to the same instance, but only the returned
     ** reference has the correct generic type information so as to ensure type
     ** safety. For best results, use the standard method-chaining idiom
     ** illustrated in the class documentation above, configuring a builder and
     ** building your cache in a single statement. Failure to heed this advice
     ** can result in a {@link ClassCastException} being thrown by a cache
     ** operation at some <i>undefined</i> point in the future.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** Any exception thrown by <code>listener</code> will <i>not</i> be
     ** propagated to the {@link Cache} user, only logged via a {@code Logger}.
     ** <p>
     ** This feature cannot be used in conjunction when {@link #weakKeys()} is
     ** combined with {@code #buildAsync}.
     **
     ** @param  <K1>             the most general type of keys the listener can
     **                          listen for; for example {@link Object} if any
     **                          key is acceptable.
     ** @param  <V1>             the most general type of values the listener
     **                          can listen for; for example {@link Object} if
     **                          any value is acceptable.
     ** @param  listener         a listener instance that caches should notify
     **                          each time an entry is being automatically
     **                          removed due to eviction.
     **                          <br>
     **                          Allowed object is {@link Listener}.
     **
     ** @return                  the cache builder reference that should be used
     **                          instead of <code>this</code> for any remaining
     **                          configuration and cache building.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException     if the specified listener is
     **                                  <code>null</code>.
     ** @throws IllegalArgumentException if a eviction listener was already set.
     */
    public <K1 extends K, V1 extends V> Builder<K1, V1> evictionListener(final Listener<? super K1, ? super V1> listener) {
      requireArgument((this.evictionListener == null), "Eviction listener was already set to %s", this.evictionListener);
      @SuppressWarnings("unchecked")
      Builder<K1, V1> self = (Builder<K1, V1>)this;
      self.evictionListener = Objects.requireNonNull(listener);
      return self;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: removalListener
    /**
     ** Specifies a listener instance that caches should notify each time an
     ** entry is removed for any {@link Event event}. The cache will invoke this
     ** listener on the configured {@link #executor(Executor)} after the entry's
     ** removal operation has completed. In the case of expiration or reference
     ** collection, the entry may be pending removal and will be discarded as
     ** part of the routine maintenance described in the class documentation
     ** above. For a more prompt notification on expiration a
     ** {@link #scheduler(Scheduler)} may be configured. An
     ** {@code #evictionListener(Listener)} may be preferred when the listener
     ** should be invoked as part of the atomic operation to remove the entry.
     ** <p>
     ** <b>Important</b>:
     ** <br>
     ** After invoking this method, do not continue to use <i>this</i> cache
     ** builder reference; instead use the reference this method <i>returns</i>.
     ** At runtime, these point to the same instance, but only the returned
     ** reference has the correct generic type information so as to ensure type
     ** safety. For best results, use the standard method-chaining idiom
     ** illustrated in the class documentation above, configuring a builder and
     ** building your cache in a single statement. Failure to heed this advice
     ** can result in a {@link ClassCastException} being thrown by a cache
     ** operation at some <i>undefined</i> point in the future.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** Any exception thrown by <code>listener</code> will <i>not</i> be
     ** propagated to the {@link Cache} user, only logged via a {@code Logger}.
     **
     ** @param  <K1>             the most general type of keys the listener can
     **                          listen for; for example {@link Object} if any
     **                          key is acceptable.
     ** @param  <V1>             the most general type of values the listener
     **                          can listen for; for example {@link Object} if
     **                          any value is acceptable.
     ** @param  listener         a listener instance that caches should notify
     **                          each time an entry is removed.
     **                          <br>
     **                          Allowed object is {@link Listener}.
     **
     ** @return                  the cache builder reference that should be used
     **                          instead of <code>this</code> for any remaining
     **                          configuration and cache building.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException     if the specified listener is
     **                                  <code>null</code>.
     ** @throws IllegalArgumentException if a removal listener was already set.
     */
    public <K1 extends K, V1 extends V> Builder<K1, V1> removalListener(final Listener<? super K1, ? super V1> listener) {
      requireArgument((this.removalListener == null), "Removal listener was already set to %s", this.removalListener);
      @SuppressWarnings("unchecked")
      Builder<K1, V1> self = (Builder<K1, V1>)this;
      self.removalListener = Objects.requireNonNull(listener);
      return self;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMetric
    /**
     ** Enables the accumulation of {@link Metric}s during the operation of
     ** the cache. Without this {@link Cache#metric()} will return zero for
     ** all metrics.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** Recording metrics requires bookkeeping to be performed with each
     ** operation, and thus imposes a performance penalty on cache operation.
     **
     ** @return                    the <code>Builder</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>Builder</code>.
     */
    public Builder<K, V> recordMetric() {
      requireState(this.metricSupplier == null, "Metrics recording was already set");
      this.metricSupplier = Metric.DEFAULT;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMetric
    /**
     ** Enables the accumulation of {@link Metric}s during the operation of
     ** the cache. Without this {@link Cache#metric()} will return zero for
     ** all metrics.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** Recording metrics requires bookkeeping to be performed with each
     ** operation, and thus imposes a performance penalty on cache operation.
     ** <p>
     ** Any exception thrown by the supplied {@link Metric.Collector} will be
     ** suppressed and logged.
     **
     ** @param  supplier         a supplier instance that returns a new
     **                          {@link Metric.Collector}.
     **                          <br>
     **                          Allowed object is {@link Supplier} with type
     **                          {@link Metric.Collector}.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public Builder<K, V> recordMetric(final Supplier<? extends Metric.Collector> supplier) {
      requireState(this.metricSupplier == null, "Metrics recording was already set");
      Objects.requireNonNull(supplier);
      this.metricSupplier = () -> Metric.guarded(supplier.get());
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: scheduler
    /**
     ** Specifies the scheduler to use when scheduling routine maintenance based
     ** on an expiration event. This augments the periodic maintenance that
     ** occurs during normal cache operations to allow for the prompt removal of
     ** expired entries regardless of whether any cache activity is occurring at
     ** that time. By default, {@link Scheduler#disabled()} is used.
     ** <p>
     ** The scheduling between expiration events is paced to exploit batching
     ** and to minimize executions in short succession. This minimum difference
     ** between the scheduled executions is implementation-specific, currently
     ** at ~1 second (2^30 ns). In addition, the provided scheduler may not
     ** offer real-time guarantees (including
     ** {@code ScheduledThreadPoolExecutor}). The scheduling is best-effort and
     ** does not make any hard guarantees of when an expired entry will be
     ** removed.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For Java 9 and later consider using {@link Scheduler#system()} to
     ** leverage the dedicated, system-wide scheduling thread.
     **
     ** @param  scheduler        the scheduler that submits a task to the
     **                          {@link #executor(Executor)} after a given
     **                          delay.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if the specified <code>scheduler</code> is
     **                              <code>null</code>.
     */
    public Builder<K, V> scheduler(final Scheduler scheduler) {
      requireState(this.scheduler == null, "Scheduler was already set to %s", this.scheduler);
      this.scheduler = Objects.requireNonNull(scheduler);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: executor
    /**
     ** Specifies the executor to use when running asynchronous tasks.
     ** <br>
     ** The executor is delegated to when sending removal notifications, when
     ** asynchronous computations are performed by {@code AsyncCache} or
     ** {@code LoadingCache#refresh} or {@link #refreshAfterWrite}, or when
     ** performing periodic maintenance. By default,
     ** {@code ForkJoinPool#commonPool()} is used.
     ** <p>
     ** The primary intent of this method is to facilitate testing of caches
     ** which have been configured with {@link #removalListener} or utilize
     ** asynchronous computations. A test may instead prefer to configure the
     ** cache to execute tasks directly on the same thread.
     ** <p>
     ** Beware that configuring a cache with an executor that throws
     ** {@code RejectedExecutionException} may experience non-deterministic
     ** behavior.
     **
     ** @param  executor         the executor to use for asynchronous execution.
     **
     ** @return                  the <code>Builder</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     **
     ** @throws NullPointerException if the specified <code>executor</code> is
     **                              <code>null</code>.
     */
    public Builder<K, V> executor(final Executor executor) {
      requireState(this.executor == null, "Executor was already set to %s", this.executor);
      this.executor = Objects.requireNonNull(executor);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: of
    /**
     ** Factory method to create a new <code>Builder</code> instance with
     ** default settings, including strong keys, strong values, and no automatic
     ** eviction of any kind.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** While this return type is <code>Builder&lt;Object, Object&gt;</code>,
     ** type parameters on the {@link #build} methods allow you to create a
     ** cache of any key and value type desired.
     **
     ** @return                  a new instance with default settings.
     **                          <br>
     **                          Possible object is <code>Builder</code>.
     */
    public static Builder<Object, Object> of() {
      return new Builder<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   build
    /**
     ** Factory method to create a {@link Cache} which does not automatically
     ** load values when keys are requested unless a mapping function is
     ** provided.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** Multiple threads can concurrently load values for distinct keys.
     ** <p>
     ** Consider {@code #build(CacheLoader)} instead, if it is feasible to
     ** implement a {@code CacheLoader}.
     ** <p>
     ** This method does not alter the state of this <code>Builder</code>
     ** instance, so it can be invoked again to create multiple independent
     ** caches.
     **
     ** @param  <K1>               the most general type of keys of the cache;
     **                            for example {@link Object}.
     ** @param  <V1>               the most general type of values of the cache;
     **                            for example {@link Object}.
     **
     ** @return                    a {@link Cache} having the requested
     **                            features.
     **                            <br>
     **                            Possible object is {@link Cache}.
     */
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
      /*
      requireWeightWithWeigher();
      requireNonLoadingCache();

      return bounded() ? new BoundedMemory.BoundedLocalManualCache<>(self) : new UnboundedMemory.UnboundedLocalManualCache<>(self);
      */
      @SuppressWarnings("unchecked")
      Builder<K1, V1> self = (Builder<K1, V1>)this;
      return Unbounded.<K1, V1>of(self, false);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: requireState
    /**
     ** Asserts that a condition is <code>true</code> If it isn't it throws an
     ** {@link IllegalStateException} without a message.
     **
     ** @param  condition          the condition to be checked.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     */
    static void requireState(final boolean expression) {
      if (!expression)
        throw new IllegalStateException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: requireState
    /**
     ** Asserts that a condition is <code>true</code> If it isn't it throws an
     ** {@link IllegalStateException} without a message.
     **
     ** @param  condition        the condition to be checked.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  message          the identifying message for the error.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  args             the arguments referenced by the format
     **                          specifiers in the format string. If there are
     **                          more arguments than format specifiers, the
     **                          extra arguments are ignored.
     **                          <br>
     **                          The number of arguments is variable and may be
     **                          zero. The maximum number of arguments is
     **                          limited by the maximum dimension of a Java
     **                          array as defined by
     **                          <cite>The Java&trade; Virtual Machine Specification</cite>.
     **                          <br>
     **                          The behaviour on a <code>null</code> argument
     **                          depends on the
     **                          <a href="../util/Formatter.html#syntax">conversion</a>.
     */
    static void requireState(final boolean expression, final String message, final Object... args) {
      if (!expression)
        throw new IllegalStateException(String.format(message, args));
    }


    ////////////////////////////////////////////////////////////////////////////
    // Method: requireArgument
    /**
     ** Asserts that a condition is <code>true</code> If it isn't it throws an
     ** {@link IllegalArgumentException} without a message.
     **
     ** @param  condition          the condition to be checked.
     **                            <br>
     **                            Allowed object is <code>boolean</code>.
     */
    static void requireArgument(final boolean expression) {
      if (!expression)
        throw new IllegalArgumentException();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: requireArgument
    /**
     ** Asserts that a condition is <code>true</code>. If it isn't it throws an
     ** {@link IllegalArgumentException} with the given message.
     **
     ** @param  condition        the condition to be checked.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  message          the identifying message for the error.
     **                          <br>
     **                          May be <code>null</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  args             the arguments referenced by the format
     **                          specifiers in the format string. If there are
     **                          more arguments than format specifiers, the
     **                          extra arguments are ignored.
     **                          <br>
     **                          The number of arguments is variable and may be
     **                          zero. The maximum number of arguments is
     **                          limited by the maximum dimension of a Java
     **                          array as defined by
     **                          <cite>The Java&trade; Virtual Machine Specification</cite>.
     **                          <br>
     **                          The behaviour on a <code>null</code> argument
     **                          depends on the
     **                          <a href="../util/Formatter.html#syntax">conversion</a>.
     */
    static void requireArgument(final boolean condition, final String message, final Object... args) {
      if (!condition)
        throw new IllegalArgumentException(String.format(message, args));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   nanoSecond
    /**
     ** Returns the number of nanoseconds of the given duration without throwing
     ** or overflowing.
     ** <p>
     ** Instead of throwing {@link ArithmeticException}, this method silently
     ** saturates to either {@link Long#MAX_VALUE} or {@link Long#MIN_VALUE}.
     ** This behavior can be useful when decomposing a duration in order to call
     ** a legacy API which requires a <code>long, TimeUnit</code> pair.
     **
     ** @param  duration         the length of time after an entry is created
     **                          that it should be automatically removed.
     **                          <br>
     **                          Allowed object is {@link Duration}.
     **
     ** @return                  the total length of the duration in
     **                          nanoseconds.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    private static long nanoSecond(final Duration duration) {
      // using a try/catch seems lazy, but the catch block will rarely get
      // invoked (except for durations longer than approximately +/- 292 years).
      try {
        return duration.toNanos();
      }
      catch (ArithmeticException tooBig) {
        return duration.isNegative() ? Long.MIN_VALUE : Long.MAX_VALUE;
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   estimatedSize
  /**
   ** Returns the approximate number of entries in this cache.
   ** <br>
   ** The value returned is an estimate; the actual count may differ if there
   ** are concurrent insertions or removals, or if some entries are pending
   ** removal due to expiration or weak/soft reference collection. In the case
   ** of stale entries this inaccuracy can be mitigated by performing a
   ** {@link #cleanup()} first.
   **
   ** @return                    the estimated number of mappings.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  long estimatedSize();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   cleanup
  /**
   ** Performs any pending maintenance operations needed by the cache. Exactly
   ** which activities are performed -- if any -- is implementation-dependent.
   */
  void cleanup();

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
  ConcurrentMap<K, V> store();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Returns the value associated with the <code>key</code> in this cache,
   ** obtaining that value from the <code>function</code> if necessary. This
   ** method provides a simple substitute for the conventional
   ** "if cached, return; otherwise create, cache and return" pattern.
   ** <p>
   ** If the specified key is not already associated with a value, attempts to
   ** compute its value using the given mapping function and enters it into this
   ** cache unless <code>null</code>. The entire method invocation is performed
   ** atomically, so the function is applied at most once per key.
   ** <br>
   ** Some attempted update operations on this cache by other threads may be
   ** blocked while the computation is in progress, so the computation should be
   ** short and simple, and must not attempt to update any other mappings of
   ** this cache.
   ** <p>
   ** <b>Warning</b>:
   ** <br>
   ** As with {@code CacheLoader#load}, <code>function</code> <b>must not</b>
   ** attempt to update any other mappings of this cache.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   ** @param  function           the function to compute a value.
   **
   ** @return                    the current (existing or computed) value
   **                            associated with the specified key, or
   **                            <code>null</code> if the computed value is
   **                            <code>null</code>.
   **
   ** @throws RuntimeException      if the <code>function</code> does so, in
   **                               which case the mapping is left
   **                               unestablished.
   ** @throws NullPointerException  if the specified key or
   **                               <code>function</code> is <code>null</code>.
   ** @throws IllegalStateException if the computation detectably attempts a
   **                               recursive update to this cache that would
   **                               otherwise never complete.
   */
  V get(final K key, final Function<? super K, ? extends V> function);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getIfPresent
  /**
   ** Returns the value associated with the <code>key</code> in this cache, or
   ** <code>null</code> if there is no cached value for the <code>key</code>.
   **
   ** @param  key                the key whose associated value is to be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the value to which the specified key is mapped,
   **                            or <code>null</code> if this cache contains no
   **                            mapping for the key.
   **                            <br>
   **                            Possible object is <code>V</code>.
   **
   ** @throws NullPointerException if the specified key is <code>null</code>.
   */
  V getIfPresent(final Object key);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAll
  /**
   ** Returns a map of the values associated with the <code>filter</code>,
   ** creating or retrieving those values if necessary.
   ** <br>
   ** The returned map contains entries that were already cached, combined with
   ** the newly loaded entries; it will never contain <code>null</code> keys or
   ** values.
   ** <p>
   ** A single request to the <code>function</code> is performed for all keys
   ** which are not already present in the cache. All entries returned by
   ** <code>function</code> will be stored in the cache, over-writing any
   ** previously cached values. If another call to {@link #get} tries to load
   ** the value for a key in <code>filter</code>, implementations may either
   ** have that thread load the entry or simply wait for this thread to finish
   ** and return the loaded value. In the case of overlapping non-blocking
   ** loads, the last load to complete will replace the existing entry.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Multiple threads can concurrently load values for distinct keys.
   ** <p>
   ** <b>Note</b>:
   ** Duplicate elements in <code>filter</code>, as determined by
   ** {@link Object#equals}, will be ignored.
   **
   ** @param  filter             the keys whose associated values are to be
   **                            returned.
   **                            <br>
   **                            Allowed object is {@link Iterable} of type
   **                            <code>K</code>.
   ** @param  function           the function to compute the values.
   **                            <br>
   **                            Allowed object is {@link Function} of type
   **                            <code>K</code>.
   **
   ** @return                    an unmodifiable mapping of keys to values for
   **                            the specified keys in this cache.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            elements is of type <code>K</code> for the key
   **                            and <code>V</code> for the value.
   **
   ** @throws NullPointerException if the specified collection is
   **                              <code>null</code> or contains a
   **                              <code>null</code> element, or if the map
   **                              returned by the <code>function</code> is
   **                              <code>null</code>.
   ** @throws RuntimeException     if the <code>function</code> does so, in
   **                              which case the mapping is left unestablished.
   */
  Map<K, V> getAll(final Iterable<? extends K> filter, final Function<Iterable<? extends K>, Map<K, V>> function);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAllPresent
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
  Map<K, V> getAllPresent(final Iterable<? extends K> filter);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidate
  /**
   ** Discards any cached value for the <code>key</code>.
   ** <br>
   ** The behavior of this operation is undefined for an entry that is being
   ** loaded (or reloaded) and is otherwise not present.
   **
   ** @param  key                the key whose mapping is to be removed from the
   **                            cache.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @throws NullPointerException if the specified key is <code>null</code>.
   */
  void invalidate(final K key);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidateAll
  /**
   ** Discards any cached values for the <code>key</code>.
   ** <br>
   ** The behavior of this operation is undefined for an entry that is being
   ** loaded (or reloaded) and is otherwise not present.
   */
  void invalidateAll();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   invalidateAll
  /**
   ** Discards any cached values for the <code>key</code>.
   ** <br>
   ** The behavior of this operation is undefined for an entry that is being
   ** loaded (or reloaded) and is otherwise not present.
   **
   ** @param  keys               the keys whose associated values are to be
   **                            removed.
   **                            <br>
   **                            Allowed object is {@link Iterable} of type
   **                            <code>K</code>.
   **
   ** @throws NullPointerException if the specified collection is
   **                              <code>null</code> or contains a
   **                              <code>null</code> element.
   */
  void invalidateAll(final Iterable<? extends K> keys);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metric
  /**
   ** Returns a current snapshot of this cache's cumulative metrics.
   ** <br>
   ** All metrics are initialized to zero, and are monotonically increasing
   ** over the lifetime of the cache.
   ** <p>
   ** Due to the performance penalty of maintaining metrics, some
   ** implementations may not record the usage history immediately or at all.
   **
   ** @return                    the current snapshot of the metrics of this
   **                            cache.
   **                            <br>
   **                            Possible object is {@link Metric}.
   */
  Metric metric();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   map
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
  ConcurrentMap<K, V> map();
}