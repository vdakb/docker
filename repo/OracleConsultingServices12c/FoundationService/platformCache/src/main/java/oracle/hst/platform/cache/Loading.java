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

    File        :   Loading.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Loading.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

import java.util.Map;

import java.util.concurrent.CompletableFuture;

////////////////////////////////////////////////////////////////////////////////
// interface Loading
// ~~~~~~~~~ ~~~~~~~
/**
 ** A semi-persistent mapping from keys to values.
 ** <br>
 ** Cache entries  are automatically loaded by the cache, and are stored in the
 ** cache until either evicted or manually invalidated.
 ** <p>
 ** Implementations of this interface are expected to be thread-safe, and can be
 ** safely accessed by multiple concurrent threads.
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
public interface Loading<K extends Object, V extends Object> extends Cache<K, V> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refresh
  /**
   ** Loads a new value for the <code>key</code>, asynchronously.
   ** <br>
   ** While the new value is loading the previous value (if any) will continue
   ** to be returned by <code>get(key)</code> unless it is evicted.
   ** <br>
   ** If the new value is loaded successfully it will replace the previous value
   ** in the cache; if an exception is thrown while refreshing the previous
   ** value will remain, <i>and the exception will be logged (using
   ** {@link System.Logger}) and swallowed</i>.
   ** <p>
   ** Caches loaded by a {@link CacheLoader} will call
   ** {@link CacheLoader#reload} if the cache currently contains a value for the
   ** <code>key</code>, and {@link CacheLoader#load} otherwise.
   ** <br>
   ** Loading is asynchronous by delegating to the default executor.
   ** <p>
   ** Returns an existing future without doing anything if another thread is
   ** currently loading the value for <code>key</code>.
   **
   ** @param  key                the key with which the specified value is to be
   **                            associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the future that is loading the value.
   **                            <br>
   **                            Possible object is {@link CompletableFuture} for
   **                            type <code>V</code>.
   **
   ** @throws NullPointerException  if the specified key or
   **                               <code>function</code> is <code>null</code>.
   */
  CompletableFuture<V> refresh(K key);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshAll
  /**
   ** Loads a new value for the <code>key</code>, asynchronously.
   ** <br>
   ** While the new value is loading the previous value (if any) will continue
   ** to be returned by <code>get(key)</code> unless it is evicted.
   ** <br>
   ** If the new value is loaded successfully it will replace the previous value
   ** in the cache; if an exception is thrown while refreshing the previous
   ** value will remain, <i>and the exception will be logged (using
   ** {@link System.Logger}) and swallowed</i>.
   ** <br>
   ** If another thread is currently loading the value for <code>key</code>,
   ** then does not perform an additional load.
   ** <p>
   ** Caches loaded by a {@link CacheLoader} will call
   ** {@link CacheLoader#reload} if the cache currently contains a value for the
   ** <code>key</code>, and {@link CacheLoader#load} otherwise.
   ** <br>
   ** Loading is asynchronous by delegating to the default executor.
   **
   ** @param  key                the keys with which the specified values are to
   **                            be associated.
   **                            <br>
   **                            Allowed object is <code>K</code>.
   **
   ** @return                    the future containing an unmodifiable mapping
   **                            of keys to values for the specified keys that
   **                            are loading the values.
   **                            <br>
   **                            Possible object is {@link CompletableFuture}
   **                            for type <code>V</code>.
   **
   ** @throws NullPointerException  if the specified collection is
   **                               <code>null</code> or contains a
   **                               <code>null</code> element.
   */
  CompletableFuture<Map<K, V>> refreshAll(final Iterable<? extends K> keys);
}