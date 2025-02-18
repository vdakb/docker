package oracle.hst.platform.cache;

import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;

import java.util.function.Function;
import java.util.function.BiFunction;

import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 ** Computes or retrieves values asynchronously, based on a key, for use in
 ** populating a {@link AsyncLoadingCache}.
 ** <p>
 ** Most implementations will only need to implement {@link #asyncLoad}.
 ** <br>
 ** Other methods may be overridden as desired.
 ** <p>
 ** Usage example:
 ** <pre>
 **   Loader&lt;Key, Graph&gt; loader = (key, executor) -> createExpensiveGraphAsync(key, executor);
 **   Async&lt;Key, Graph&gt;  cache  = Cache.Builder.of().build(loader);
 ** </pre>
 **
 ** @param  <K>                  the most general type of keys this cache
 **                              stores; for example {@link Object} if any
 **                              key is acceptable.
 **                              <br>
 **                              Allowed object is <code>&lt;K&gt;</code>.
 ** @param  <V>                  the most general type of values this cache
 **                              stores; for example {@link Object} if any
 **                              value is acceptable.
 **                              <br>
 **                              Allowed object is <code>&lt;V&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Loader<K extends Object, V extends Object> {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Default
  // ~~~~~~~~~ ~~~~~~~
  /**
   ** Computes or retrieves values asynchronously, based on a key, for use in
   ** populating a {@link AsyncLoadingCache}.
   ** <p>
   ** Most implementations will only need to implement {@link #asyncLoad}.
   ** <br>
   ** Other methods may be overridden as desired.
   ** <p>
   ** Usage example:
   ** <pre>
   **   Loader.Default&lt;Key, Graph&gt; loader = (key, executor) -> createExpensiveGraphAsync(key, executor);
   **   Async&lt;Key, Graph&gt;          cache  = Cache.Builder.of().build(loader);
   ** </pre>
   **
   ** @param  <K>                the most general type of keys this cache
   **                            stores; for example {@link Object} if any
   **                            key is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the most general type of values this cache
   **                            stores; for example {@link Object} if any
   **                            value is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   */
  interface Default<K extends Object, V extends Object> extends Loader<K, V> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: bulk
    /**
     ** Retruns a cache loader that delegates to the supplied mapping function
     ** for retrieving the values.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** {@link #load} will silently discard any additional mappings loaded when
     ** retrieving the {@code key} prior to returning to the value to the cache.
     * <p>
     * Usage example:
     * <pre>{@code
     *   CacheLoader<Key, Graph> loader = CacheLoader.bulk(keys -> createExpensiveGraphs(keys));
     *   LoadingCache<Key, Graph> cache = Caffeine.newBuilder().build(loader);
     * }</pre>
     **
     ** @param  <K>              the most general type of keys; for example
     **                          {@link Object} if any key is acceptable.
     **                          <br>
     **                          Allowed object is <code>&lt;K&gt;</code>.
     ** @param  <V>              the most general type of values; for example
     **                          {@link Object} if any value is acceptable.
     **                          <br>
     **                          Allowed object is <code>&lt;V&gt;</code>.
     ** @param  mapping          the function to compute the values.
     **                          <br>
     **                          Allowed object is {@link Function}.
     **
     ** @return                   an cache loader that delegates to the supplied
     **                          <code>mapping</code> {@link Function}.
     **                          <br>
     **                          Possible object is <code>Loader</code> with
     **                          type <code>K</code> for the key and
     **                          <code>V</code> as the value.
     **
     ** @throws NullPointerException if the mappingFunction is
     **                              <code>null</code>.
     */
    static <K extends Object, V extends Object> Default<K, V> bulk(final Function<? super Set<? extends K>, ? extends Map<? extends K, ? extends V>> mapping) {
      return new Default<K, V>() {
        @Override
        public V load(K key) {
          return loadAll(Collections.singleton(key)).get(key);
        }

        @Override
        public Map<? extends K, ? extends V> loadAll(Set<? extends K> keys) {
          return Objects.requireNonNull(mapping).apply(keys);
        }
      };
    }
    ////////////////////////////////////////////////////////////////////////////
    // Method: load
    /**
     ** Computes or retrieves the value corresponding to <code>key</code>.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly.
     **
     ** @param  key              the non-<code>null</code> key whose value
     **                          should be loaded.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     **
     ** @return                  the value associated with <code>key</code>, or
     **                          <code>null</code> if not found.
     **                          <br>
     **                          Possible object is {@link CompletableFuture}
     **                          for type <code>V</code>.
     **
     ** @throws Exception            in which case the mapping is unchanged.
     ** @throws InterruptedException if this method is interrupted.
     **                              {@link InterruptedException} is treated
     **                              like any other {@link Exception} in all
     **                              respects except that, when it is caught,
     **                              the thread's interrupt status is set.
     */
    V load(K key)
      throws Exception;

    ////////////////////////////////////////////////////////////////////////////
    // Method: loadAll
    /**
     ** Computes or retrieves the values corresponding to <code>key</code>.
     ** <br>
     ** This method is called by {@link LoadingCache#getAll}.
     ** <p>
     ** If the returned map doesn't contain all requested <code>key</code> then
     ** the entries it does contain will be cached and <code>getAll</code> will
     ** return the partial results.
     ** <br>
     ** If the returned map contains extra keys not present in <code>key</code>
     ** then all returned entries will be cached, but only the entries for
     ** <code>key</code> will be returned from <code>getAll</code>.
     ** <p>
     ** This method should be overridden when bulk retrieval is significantly
     ** more efficient than many individual lookups.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** {@link Loading#getAll} will defer to individual calls to
     ** {@link Loading#get} if this method is not overridden.
     ** <p>
     ** <b>Warning</b>:
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly.
     **
     ** @param  key              the non-<code>null</code> key whose value
     **                          should be loaded.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type <code>K</code>.
     **
     ** @return                  a map from each key in <code>key</code> to the
     **                          value associated with that key;
     **                          <b>may not contain null values</b>.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is of type <code>K</code> for the key
     **                          and <code>V</code> as the value.
     **
     ** @throws Exception            in which case the mapping is unchanged.
     ** @throws InterruptedException if this method is interrupted.
     **                              {@link InterruptedException} is treated
     **                              like any other {@link Exception} in all
     **                              respects except that, when it is caught,
     **                              the thread's interrupt status is set.
     */
    default Map<? extends K, ? extends V> loadAll(final Set<? extends K> keys)
      throws Exception {

      throw new UnsupportedOperationException();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: reload
    /**
     ** Computes or retrieves a replacement value corresponding to an
     ** already-cached <code>key</code>.
     ** <br>
     ** If the replacement value is not found then the mapping will be removed
     ** if <code>null</code> is returned.
     ** <br>
     ** This method is called when an existing cache entry is refreshed by
     ** {@link Cache#refreshAfterWrite}, or through a call to
     ** {@link Loading#refresh}.
     ** <p>
     ** <b>Warning:</b>
     ** <br>
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly or block waiting for other cache operations to complete.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** <i>all exceptions thrown by this method will be logged and then swallowed</i>.
     **
     ** @param  key              the non-<code>null</code> key whose value
     **                          should be loaded.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  oldValue         the non-<code>null</code> old value
     **                          corresponding to <code>null</code>.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     **
     ** @return                  the new value associated with <code>key</code>,
     **                          or <code>null</code> if the mapping is to be
     **                          removed.
     **                          <br>
     **                          Possible object is <code>V</code>.
     **
     ** @throws Exception            in which case the mapping is unchanged.
     ** @throws InterruptedException if this method is interrupted.
     **                              {@link InterruptedException} is treated
     **                              like any other {@link Exception} in all
     **                              respects except that, when it is caught,
     **                              the thread's interrupt status is set.
     */
    default V reload(K key, V oldValue)
      throws Exception {

      return load(key);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: reloadAsync
    /**
     ** Computes or retrieves a replacement value corresponding to an
     ** already-cached <code>key</code>.
     ** <br>
     ** If the replacement value is not found then the mapping will be removed
     ** if <code>null</code> is returned.
     ** <br>
     ** This method is called when an existing cache entry is refreshed by
     ** {@link Cache#refreshAfterWrite}, or through a call to
     ** {@link Loading#refresh}.
     ** <p>
     ** <b>Warning:</b>
     ** <br>
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly or block waiting for other cache operations to complete.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** <i>all exceptions thrown by this method will be logged and then swallowed</i>.
     **
     ** @param  key              the non-<code>null</code> key whose value
     **                          should be loaded.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  oldValue         the non-<code>null</code> old value
     **                          corresponding to <code>null</code>.
     **                          <br>
     **                          Allowed object is <code>V</code>.
     ** @param  executor         the executor with which the entry may be
     **                          asynchronously loaded with.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     * @return a future containing the new value associated with {@code key}, or containing
     *         {@code null} if the mapping is to be removed
     */
    @Override
    default CompletableFuture<? extends V> reloadAsync(final K key, final V oldValue, final Executor executor)
      throws Exception {

      return CompletableFuture.supplyAsync(() -> {
        try {
          return reload(Objects.requireNonNull(key), oldValue);
        }
        catch (RuntimeException e) {
          throw e;
        }
        catch (Exception e) {
          throw new CompletionException(e);
        }
      }
      , Objects.requireNonNull(executor));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: loadAsync
    /**
     ** Asynchronously computes or retrieves the value corresponding to
     ** <code>key</code>.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly.
     **
     ** @param  key              the non-<code>null</code> key whose value
     **                          should be loaded.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     ** @param  executor         the executor with which the entry may be
     **                          asynchronously loaded with.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     **
     ** @return                  the future value associated with
     **                          <code>key</code>.
     **                          <br>
     **                          Possible object is {@link CompletableFuture}
     **                          for type <code>V</code>.
     **
     ** @throws Exception            in which case the mapping is unchanged.
     ** @throws InterruptedException if this method is interrupted.
     **                              {@link InterruptedException} is treated
     **                              like any other {@link Exception} in all
     **                              respects except that, when it is caught,
     **                              the thread's interrupt status is set.
     */
    @Override
    default CompletableFuture<? extends V> loadAsync(final K key, final Executor executor)
      throws Exception {

      return CompletableFuture.supplyAsync(() -> {
        try {
          return load(Objects.requireNonNull(key));
        }
        catch (RuntimeException e) {
          throw e;
        }
        catch (Exception e) {
          throw new CompletionException(e);
        }
      }
      , Objects.requireNonNull(executor));
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method: loadAllAsync
    /**
     ** Asynchronously computes or retrieves the value corresponding to
     ** <code>key</code>.
     ** This method is called by {@link AsyncLoadingCache#getAll}.
     ** <p>
     ** If the returned map doesn't contain all requested <code>key</code> then
     ** the entries it does contain will be cached and <code>getAll</code> will
     ** return the partial results.
     ** <br>
     ** If the returned map contains extra keys not present in <code>key</code>
     ** then all returned entries will be cached, but only the entries for
     ** <code>key</code> will be returned from <code>getAll</code>.
     ** <p>
     ** This method should be overridden when bulk retrieval is significantly more
     ** efficient than many individual lookups.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** {@link AsyncLoadingCache#getAll} will defer to individual calls to
     ** {@link AsyncLoadingCache#get} if this method is not overridden.
     ** <p>
     ** <b>Warning</b>:
     ** <br>
     ** Loading <b>must not</b> attempt to update any mappings of this cache
     ** directly.
     **
     ** @param  key              the unique, non-<code>null</code> keys whose
     **                          values should be loaded.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type <code>K</code>.
     ** @param  executor         the executor with which the entry may be
     **                          asynchronously loaded with.
     **                          <br>
     **                          Allowed object is <code>K</code>.
     **
     ** @return                  the future containing the map from each key in
     **                          <code>key</code> to the value associated with
     **                          that key; <b>may not contain null values</b>.
     **                          <br>
     **                          Possible object is {@link CompletableFuture}
     **                          for type <code>Map</code>.
     **
     ** @throws Exception            in which case the mapping is unchanged.
     ** @throws InterruptedException if this method is interrupted.
     **                              {@link InterruptedException} is treated
     **                              like any other {@link Exception} in all
     **                              respects except that, when it is caught,
     **                              the thread's interrupt status is set.
     */
    @Override
    default CompletableFuture<? extends Map<? extends K, ? extends V>> loadAllAsync(Set<? extends K> keys, Executor executor)
      throws Exception {

      return CompletableFuture.supplyAsync(() -> {
        try {
          return loadAll(Objects.requireNonNull(keys));
        }
        catch (RuntimeException e) {
          throw e;
        }
        catch (Exception e) {
          throw new CompletionException(e);
        }
      }
      , Objects.requireNonNull(executor));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulk
  /**
   ** Returns an asynchronous cache loader that delegates to the supplied
   ** mapping function for retrieving the values.
   ** <br>
   ** <b>Note</b>:
   ** {@link #asyncLoad} will discard any additional mappings loaded when
   ** retrieving the <code>key</code> prior to returning to the value to the
   ** cache.
   ** <p>
   ** Usage example:
   ** <pre>
   **   Loader&lt;Key, Graph&gt;       loader = Loader.bulk(keys -> createExpensiveGraphs(keys));
   **   AsyncLoading&lt;Key, Graph&gt; cache  = Cache.Builder.of().build(loader);
   ** </pre>
   **
   ** @param  <K>                the most general type of keys; for example
   **                            {@link Object} if any key is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the most general type of values; for example
   **                            {@link Object} if any value is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  mapping            the function to asynchronously compute the
   **                            values.
   **                            <br>
   **                            Allowed object is {@link Function}.
   **
   ** @return                    an asynchronous cache loader that delegates to
   **                            the supplied <code>mapping</code>.
   **                            <br>
   **                            Possible object is <code>Loader</code> with
   **                            type <code>K</code> for the key and
   **                            <code>V</code> as the value.
   **
   ** @throws NullPointerException if the mappingFunction is <code>null</code>.
   */
  static <K extends Object, V extends Object> Loader<K, V> bulk(final Function<? super Set<? extends K>, ? extends Map<? extends K, ? extends V>> mapping) {
    return Default.bulk(mapping);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bulk
  /**
   ** Returns an asynchronous cache loader that delegates to the supplied
   ** mapping function for retrieving the values.
   ** <br>
   ** <b>Note</b>:
   ** {@link #asyncLoad} will discard any additional mappings loaded when
   ** retrieving the <code>key</code> prior to returning to the value to the
   ** cache.
   ** <p>
   ** Usage example:
   ** <pre>
   **   Loader&lt;Key, Graph&gt;       loader = Loader.bulk(keys -> createExpensiveGraphs(keys));
   **   AsyncLoading&lt;Key, Graph&gt; cache  = Cache.Builder.of().build(loader);
   ** </pre>
   **
   ** @param  <K>                the most general type of keys; for example
   **                            {@link Object} if any key is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the most general type of values; for example
   **                            {@link Object} if any value is acceptable.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  mapping            the function to asynchronously compute the
   **                            values.
   **                            <br>
   **                            Allowed object is {@link Function}.
   **
   ** @return                    an asynchronous cache loader that delegates to
   **                            the supplied <code>mapping</code>.
   **                            <br>
   **                            Possible object is <code>Loader</code> with
   **                            type <code>K</code> for the key and
   **                            <code>V</code> as the value.
   **
   ** @throws NullPointerException if the mappingFunction is <code>null</code>.
   */
  static <K extends Object, V extends Object> Loader<K, V> bulk(final BiFunction<? super Set<? extends K>, ? super Executor, ? extends CompletableFuture<? extends Map<? extends K, ? extends V>>> mapping) {
    return new Loader<K, V>() {
      @Override
      public CompletableFuture<V> loadAsync(final K key, final Executor executor) {
        return loadAllAsync(Collections.singleton(key), executor).thenApply(results -> results.get(key));
      }
      @Override
      @SuppressWarnings("unchecked")
      public CompletableFuture<Map<K, V>> loadAllAsync(final Set<? extends K> key, final Executor executor) {
        return (CompletableFuture<Map<K, V>>)Objects.requireNonNull(mapping).apply(Objects.requireNonNull(key), Objects.requireNonNull(executor));
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadAsync
  /**
   ** Asynchronously computes or retrieves the value corresponding to
   ** <code>key</code>.
   ** <p>
   ** <b>Warning</b>:
   ** <br>
   ** Loading <b>must not</b> attempt to update any mappings of this cache
   ** directly.
   **
   ** @param  key                the non-<code>null</code> key whose value
   **                            should be loaded.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  executor           the executor with which the entry may be
   **                            asynchronously loaded with.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the future value associated with
   **                            <code>key</code>.
   **                            <br>
   **                            Possible object is {@link CompletableFuture}
   **                            for type <code>V</code>.
   **
   ** @throws Exception            in which case the mapping is unchanged.
   ** @throws InterruptedException if this method is interrupted.
   **                              {@link InterruptedException} is treated like
   **                              any other {@link Exception} in all respects
   **                              except that, when it is caught, the thread's
   **                              interrupt status is set.
   */
  CompletableFuture<? extends V> loadAsync(final K key, final Executor executor)
    throws Exception;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadAllAsync
  /**
   ** Asynchronously computes or retrieves the value corresponding to
   ** <code>key</code>.
   ** This method is called by {@link AsyncLoadingCache#getAll}.
   ** <p>
   ** If the returned map doesn't contain all requested <code>key</code> then
   ** the entries it does contain will be cached and <code>getAll</code> will
   ** return the partial results.
   ** <br>
   ** If the returned map contains extra keys not present in <code>key</code>
   ** then all returned entries will be cached, but only the entries for
   ** <code>key</code> will be returned from <code>getAll</code>.
   ** <p>
   ** This method should be overridden when bulk retrieval is significantly more
   ** efficient than many individual lookups.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** {@link AsyncLoadingCache#getAll} will defer to individual calls to
   ** {@link AsyncLoadingCache#get} if this method is not overridden.
   ** <p>
   ** <b>Warning</b>:
   ** <br>
   ** Loading <b>must not</b> attempt to update any mappings of this cache
   ** directly.
   **
   ** @param  key                the unique, non-<code>null</code> keys whose
   **                            values should be loaded.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type <code>K</code>.
   ** @param  executor           the executor with which the entry may be
   **                            asynchronously loaded with.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the future containing the map from each key in
   **                            <code>key</code> to the value associated with
   **                            that key; <b>may not contain null values</b>.
   **                            <br>
   **                            Possible object is {@link CompletableFuture}
   **                            for type <code>Map</code>.
   **
   ** @throws Exception            in which case the mapping is unchanged.
   ** @throws InterruptedException if this method is interrupted.
   **                              {@link InterruptedException} is treated like
   **                              any other {@link Exception} in all respects
   **                              except that, when it is caught, the thread's
   **                              interrupt status is set.
   */
  default CompletableFuture<? extends Map<? extends K, ? extends V>> loadAllAsync(Set<? extends K> key, Executor executor)
    throws Exception {

    throw new UnsupportedOperationException();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reloadAsync
  /**
   ** Asynchronously computes or retrieves a replacement value corresponding to
   ** an already-cached <code>key</code>.
   ** <br>
   ** If the replacement value is not found then the mapping will be removed if
   ** <code>null</code> is computed.
   ** <br>
   ** This method is called when an existing cache entry is refreshed by
   ** {@link Cache#refreshAfterWrite}, or through a call to
   ** {@link Loading#refresh}.
   ** <p>
   ** <b>Warning</b>:
   ** <br>
   ** Loading <b>must not</b> attempt to update any mappings of this cache
   ** directly or block waiting for other cache operations to complete.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** <i>all exceptions thrown by this method will be logged and then swallowed</i>.
   **
   ** @param  key                the non-<code>null</code> key whose value
   **                            should be loaded.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  oldValue           the non-<code>null</code> old value
   **                            corresponding to <code>null</code>.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   ** @param  executor           the executor with which the entry may be
   **                            asynchronously loaded with.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the future containing the new value associated
   **                            with <code>key</code>, or containing
   **                            <code>null</code> if the mapping is to be
   **                            removed.
   **                            <br>
   **                            Possible object is {@link CompletableFuture}
   **                            for type <code>V</code>.
   **
   ** @throws Exception            in which case the mapping is unchanged.
   ** @throws InterruptedException if this method is interrupted.
   **                              {@link InterruptedException} is treated like
   **                              any other {@link Exception} in all respects
   **                              except that, when it is caught, the thread's
   **                              interrupt status is set.
   */
  default CompletableFuture<? extends V> reloadAsync(final K key, final V oldValue, final Executor executor)
    throws Exception {

    return loadAsync(key, executor);
  }
}