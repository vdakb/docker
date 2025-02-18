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

    File        :   Metric.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Metric.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.cache;

import java.util.Objects;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.function.Supplier;

import java.util.concurrent.atomic.LongAdder;

////////////////////////////////////////////////////////////////////////////////
// final class Metric
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** Metrics about the performance metrics of a {@link Cache}.
 ** <p>
 ** Cache metric are incremented according to the following rules:
 ** <ul>
 **   <li>When a cache lookup encounters an existing cache entry
 **       <code>hitCount</code> is incremented.
 **   <li>When a cache lookup first encounters a missing cache entry, a new
 **       entry is loaded.
 **       <ul>
 **         <li>After successfully loading an entry <code>missCount</code> and
 **            <code>loadSuccessCount</code> are incremented, and the total
 **            loading time, in nanoseconds, is added to
 **            <code>totalLoadTime</code>.
 **         <li>When an exception is thrown while loading an entry or if the
 **             loaded value is <code>null</code>, <code>missCount</code> and
 **             <code>loadFailureCount</code> are incremented, and the total
 **             loading time, in nanoseconds, is added to
 **             <code>totalLoadTime</code>.
 **         <li>Cache lookups that encounter a missing cache entry that is still
 **             loading will wait for loading to complete (whether successful or
 **             not) and then increment <code>missCount</code>.
 **       </ul>
 **   <li>When an entry is computed through the {@link Cache#map map} the
 **       <code>loadSuccessCount</code> or <code>loadFailureCount</code> is
 **       incremented.
 **   <li>When an entry is evicted from the cache, <code>evictionCount</code> is
 **       incremented and the weight added to <code>evictionWeight</code>.
 **   <li>No stats are modified when a cache entry is invalidated or manually
 **       removed.
 **   <li>No stats are modified by non-computing operations invoked on the
 **       {@link Cache#map map} view of the cache.
 ** </ul>
 ** A lookup is specifically defined as an invocation of one of the methods
 ** {@code LoadingCache#get(Object)},
 ** {@link Cache#get(Object, java.util.function.Function)}, or
 ** {@code LoadingCache#getAll(Iterable)}.
 ** <p>
 ** This is a <em>value-based</em> class; use of identity-sensitive operations
 ** (including reference equality (<code>==</code>), identity hash code, or
 ** synchronization) on instances of <code>Metric</code> may have unpredictable
 ** results and should be avoided.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
*/
public final class Metric {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final Metric             ZERO    = Metric.of(0L, 0L, 0L, 0L, 0L, 0L, 0L);
  static final Supplier<Metric.Collector> DEFAULT = Metric.Concurrent::new;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final long hitsCount;
  private final long missCount;
  private final long loadCount;
  private final long failCount;
  private final long totalTime;
  private final long evictionCount;
  private final long evictionWeight;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // interface Collector
  // ~~~~~~~~~ ~~~~~~~~~
  /**
   ** Accumulates statistics during the operation of a {@link Cache} for
   ** presentation by {@link Cache#metric}.
   ** <br>
   ** This is solely intended for consumption by {@link Cache} implementors.
   */
  public interface Collector {

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordHits
    /**
     ** Records cache hits.
     ** <br>
     ** This should be called when a cache request returns a cached value.
     **
     ** @param  count            the number of hits to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    void recordHits(final int count);

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMiss
    /**
     ** Records cache misses.
     ** <br>
     ** This should be called when a cache request returns a value that was not
     ** found in the cache. This method should be called by the loading thread,
     ** as well as by threads blocking on the load. Multiple concurrent calls to
     ** {@link Cache} lookup methods with the same key on an absent value should
     ** result in a single call to either {@link #recordSuccess(long)} or
     ** {@link #recordFailure(long)} and multiple calls to this method,
     ** despite all being served by the results of a single load operation.
     **
     ** @param  count            the number of of misses to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    void recordMiss(final int count);

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordLoad
    /**
     ** Records the successful load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@code Cache#get} or {@code Map#computeIfAbsent}) and
     ** the loading completes successfully. In contrast to
     ** {@link #recordMisses(int)}, this method should only be called by the
     ** loading thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    void recordLoad(final long time);

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordFail
    /**
     ** Records the failed load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@code Cache#get} or {@code Map#computeIfAbsent}),
     ** but an exception is thrown while loading the entry or the loading
     ** function returns <code>null</code>. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value prior to
     **                          discovering the value doesn't exist or an
     **                          exception being thrown.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    void recordFail(final long time);

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordEviction
    /**
     ** Records the eviction of an entry from the cache.
     ** <br>
     ** This should only been called when an entry is evicted due to the cache's
     ** eviction strategy, and not as a result of manual
     ** {@link Cache#invalidate invalidations}.
     **
     ** @param  weight           the weight of the evicted entry.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  cause            the reason for which the entry was removed.
     **                          <br>
     **                          Allowed object is
     **                          {@link Cache.Event}.
     */
    void recordEviction(final int weight, final Cache.Event cause);

    ////////////////////////////////////////////////////////////////////////////
    // Method: snapshot
    /**
     ** Returns a snapshot of this collector's values.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** This may be an inconsistent view, as it may be interleaved with update
     ** operations.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The values of the metrics are undefined in case of overflow (though it
     ** is guaranteed not to throw an exception). If you require specific
     ** handling, we recommend implementing your own stats collector.
     **
     ** @return                  a snapshot of this collector's values.
     **                          <br>
     **                          Allowed object is {@link Metric}.
     */
    Metric snapshot();
  }

  ////////////////////////////////////////////////////////////////////////////////
  // final class Concurrent
  // ~~~~~ ~~~~~ ~~~~~~~~~~
  /**
   ** A thread-safe metric {@link Collector} implementation for use by
   ** {@link Cache} implementors.
   */
  static final class Concurrent implements Collector {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final LongAdder hitsCount;
    private final LongAdder missCount;
    private final LongAdder loadCount;
    private final LongAdder failCount;
    private final LongAdder totalTime;
    private final LongAdder evictionCount;
    private final LongAdder evictionWeight;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Concurrent</code> <code>Collector</code> instance
     ** with all counts initialized to zero.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Concurrent()" and enforces use of the public method below.
     */
    Concurrent() {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.hitsCount      = new LongAdder();
      this.missCount      = new LongAdder();
      this.loadCount      = new LongAdder();
      this.failCount      = new LongAdder();
      this.totalTime      = new LongAdder();
      this.evictionCount  = new LongAdder();
      this.evictionWeight = new LongAdder();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordHits (Collector)
    /**
     ** Records cache hits.
     ** <br>
     ** This should be called when a cache request returns a cached value.
     **
     ** @param  count            the number of hits to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordHits(final int count) {
      this.hitsCount.add(count);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMiss (Collector)
    /**
     ** Records cache misses.
     ** <br>
     ** This should be called when a cache request returns a value that was not
     ** found in the cache. This method should be called by the loading thread,
     ** as well as by threads blocking on the load. Multiple concurrent calls to
     ** {@link Cache} lookup methods with the same key on an absent value should
     ** result in a single call to either {@link recordSuccess(int)} or
     ** {@link recordFailure(int)} and multiple calls to this method,
     ** despite all being served by the results of a single load operation.
     **
     ** @param  count            the number of of misses to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordMiss(final int count) {
      this. missCount.add(count);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordLoad (Collector)
    /**
     ** Records the successful load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}) and
     ** the loading completes successfully. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordLoad(final long time) {
      this.loadCount.increment();
      this.totalTime.add(time);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordFail (Collector)
    /**
     ** Records the failed load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}),
     ** but an exception is thrown while loading the entry or the loading
     ** function returns <code>null</code>. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value prior to
     **                          discovering the value doesn't exist or an
     **                          exception being thrown.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordFail(final long time) {
      this.failCount.increment();
      this.totalTime.add(time);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordEviction (Collector)
    /**
     ** Records the eviction of an entry from the cache.
     ** <br>
     ** This should only been called when an entry is evicted due to the cache's
     ** eviction strategy, and not as a result of manual
     ** {@link Cache#invalidate invalidations}.
     **
     ** @param  weight           the weight of the evicted entry.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  cause            the reason for which the entry was removed.
     **                          <br>
     **                          Allowed object is
     **                          {@link RemovalListener.Cause}.
     */
    @Override
    public final void recordEviction(final int weight, final Cache.Event cause) {
      Objects.requireNonNull(cause);
      this.evictionCount.increment();
      this.evictionWeight.add(weight);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: snapshot (Collector)
    /**
     ** Returns a snapshot of this collector's values.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** This may be an inconsistent view, as it may be interleaved with update
     ** operations.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The values of the metrics are undefined in case of overflow (though it
     ** is guaranteed not to throw an exception). If you require specific
     ** handling, we recommend implementing your own stats collector.
     **
     ** @return                  a snapshot of this collector's values.
     **                          <br>
     **                          Allowed object is {@link Metric}.
     */
    @Override
    public final Metric snapshot() {
       return Metric.of(
        negativeToMaxValue(this.hitsCount.sum()),
        negativeToMaxValue(this.missCount.sum()),
        negativeToMaxValue(this.loadCount.sum()),
        negativeToMaxValue(this.failCount.sum()),
        negativeToMaxValue(this.totalTime.sum()),
        negativeToMaxValue(this.evictionCount.sum()),
        negativeToMaxValue(this.evictionWeight.sum())
      );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return snapshot().toString();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // final class Guarded
  // ~~~~~ ~~~~~ ~~~~~~~~
  /**
   ** A metric {@link Collector} implementation that suppresses and logs any
   ** exception thrown by the delegate <code>collector</code>.
   */
  private static final class Guarded implements Collector {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    private static final Logger logger   = Logger.getLogger(Guarded.class.getName());

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final Collector             delegate;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Guarded</code> <code>Collector</code>.
     ** <br>
     ** This constructor is private to prevent other classes to use
     ** "new Guarded()" and enforces use of the public method below.
     **
     ** @param  delegate         the collector.
     **                          <br>
     **                          Allowed object is <code>Collector</code>.
     */
    private Guarded(final Collector delegate) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.delegate = delegate;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordHits (Collector)
    /**
     ** Records cache hits.
     ** <br>
     ** This should be called when a cache request returns a cached value.
     **
     ** @param  count            the number of hits to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordHits(final int count) {
      try {
        this.delegate.recordHits(count);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMiss (Collector)
    /**
     ** Records cache misses.
     ** <br>
     ** This should be called when a cache request returns a value that was not
     ** found in the cache. This method should be called by the loading thread,
     ** as well as by threads blocking on the load. Multiple concurrent calls to
     ** {@link Cache} lookup methods with the same key on an absent value should
     ** result in a single call to either {@link recordSuccess(int)} or
     ** {@link recordFailure(int)} and multiple calls to this method,
     ** despite all being served by the results of a single load operation.
     **
     ** @param  count            the number of of misses to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordMiss(final int count) {
      try {
        this.delegate.recordMiss(count);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordLoad (Collector)
    /**
     ** Records the successful load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}) and
     ** the loading completes successfully. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordLoad(final long time) {
      try {
        this.delegate.recordLoad(time);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordFail (Collector)
    /**
     ** Records the failed load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}),
     ** but an exception is thrown while loading the entry or the loading
     ** function returns <code>null</code>. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value prior to
     **                          discovering the value doesn't exist or an
     **                          exception being thrown.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordFail(final long time) {
      try {
        this.delegate.recordFail(time);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordEviction (Collector)
    /**
     ** Records the eviction of an entry from the cache.
     ** <br>
     ** This should only been called when an entry is evicted due to the cache's
     ** eviction strategy, and not as a result of manual
     ** {@link Cache#invalidate invalidations}.
     **
     ** @param  weight           the weight of the evicted entry.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  cause            the reason for which the entry was removed.
     **                          <br>
     **                          Allowed object is {@link Cache.Event}.
     */
    @Override
    public final void recordEviction(final int weight, final Cache.Event cause) {
      Objects.requireNonNull(cause);
      try {
        this.delegate.recordEviction(weight, cause);
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: snapshot (Collector)
    /**
     ** Returns a snapshot of this collector's values.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** This may be an inconsistent view, as it may be interleaved with update
     ** operations.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The values of the metrics are undefined in case of overflow (though it
     ** is guaranteed not to throw an exception). If you require specific
     ** handling, we recommend implementing your own stats collector.
     **
     ** @return                  a snapshot of this collector's values.
     **                          <br>
     **                          Allowed object is {@link Metric}.
     */
    @Override
    public final Metric snapshot() {
      try {
        return this.delegate.snapshot();
      }
      catch (Throwable t) {
        logger.log(Level.WARNING, "Exception thrown by metric collector", t);
        return ZERO;
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // enum Disabled
  // ~~~~ ~~~~~~~~
  /**
   ** A metric {@link Collector} implementation that does not record any cache
   ** events.
   */
  private enum Disabled implements Collector {

    INSTANCE;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordHits (Collector)
    /**
     ** Records cache hits.
     ** <br>
     ** This should be called when a cache request returns a cached value.
     **
     ** @param  count            the number of hits to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordHits(final int count) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordMiss (Collector)
    /**
     ** Records cache misses.
     ** <br>
     ** This should be called when a cache request returns a value that was not
     ** found in the cache. This method should be called by the loading thread,
     ** as well as by threads blocking on the load. Multiple concurrent calls to
     ** {@link Cache} lookup methods with the same key on an absent value should
     ** result in a single call to either {@link recordSuccess(int)} or
     ** {@link recordFailure(int)} and multiple calls to this method,
     ** despite all being served by the results of a single load operation.
     **
     ** @param  count            the number of of misses to record.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    @Override
    public final void recordMiss(final int count) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordLoad (Collector)
    /**
     ** Records the successful load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}) and
     ** the loading completes successfully. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordLoad(final long time) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordFail (Collector)
    /**
     ** Records the failed load of a new entry.
     ** <br>
     ** This method should be called when a cache request causes an entry to be
     ** loaded (such as by {@link Cache#get} or {@link Map#computeIfAbsent}),
     ** but an exception is thrown while loading the entry or the loading
     ** function returns <code>null</code>. In contrast to
     ** {@link #recordMisses}, this method should only be called by the loading
     ** thread.
     **
     ** @param  time             the number of nanoseconds the cache spent
     **                          computing or retrieving the new value prior to
     **                          discovering the value doesn't exist or an
     **                          exception being thrown.
     **                          <br>
     **                          Allowed object is <code>long</code>.
     */
    @Override
    public final void recordFail(final long time) {
      // intentionally left blank
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: recordEviction (Collector)
    /**
     ** Records the eviction of an entry from the cache.
     ** <br>
     ** This should only been called when an entry is evicted due to the cache's
     ** eviction strategy, and not as a result of manual
     ** {@link Cache#invalidate invalidations}.
     **
     ** @param  weight           the weight of the evicted entry.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  cause            the reason for which the entry was removed.
     **                          <br>
     **                          Allowed object is
     **                          {@link RemovalListener.Cause}.
     */
    @Override
    public final void recordEviction(final int weight, final Cache.Event cause) {
      // intentionally left blank
      Objects.requireNonNull(cause);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: snapshot (Collector)
    /**
     ** Returns a snapshot of this collector's values.
     ** <br>
     ** <b>Note</b>:
     ** <br>
     ** This may be an inconsistent view, as it may be interleaved with update
     ** operations.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** The values of the metrics are undefined in case of overflow (though it
     ** is guaranteed not to throw an exception). If you require specific
     ** handling, we recommend implementing your own stats collector.
     **
     ** @return                  a snapshot of this collector's values.
     **                          <br>
     **                          Allowed object is {@link Metric}.
     */
    @Override
    public final Metric snapshot() {
      return ZERO;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return snapshot().toString();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Metric</code> with the inital values.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Metric()" and enforces use of the public method below.
   **
   ** @param  hitsCount          the number of cache hits.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  missCount          the number of cache misses.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  loadCount          the number of successful cache loads.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  failCount          the number of failed cache loads.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  totalTime          the total load time (success and failure).
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  evictionCount      the number of entries evicted from the cache.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  evictionWeight     the sum of weights of entries evicted from the
   **                            cache.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   */
  private Metric(final long hitsCount, final long missCount, final long loadCount, final long failCount, final long totalTime, final long evictionCount, final long evictionWeight) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.hitsCount      = hitsCount;
    this.missCount      = missCount;
    this.loadCount      = loadCount;
    this.failCount      = failCount;
    this.totalTime      = totalTime;
    this.evictionCount  = evictionCount;
    this.evictionWeight = evictionWeight;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory methods to create a <code>Metric</code> with the inital values.
   **
   ** @param  hitsCount          the number of cache hits.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  missCount          the number of cache misses.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  loadCount          the number of successful cache loads.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  failCount          the number of failed cache loads.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  totalTime          the total time spend (success and failure).
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  evictionCount      the number of entries evicted from the cache.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   ** @param  evictionWeight     the sum of weights of entries evicted from the
   **                            cache.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    a <code>Metric</code> representing the
   **                            specified statistics.
   **                            <br>
   **                            Possible object is <code>Metric</code>.
   */
  public static Metric of(final long hitsCount, final long missCount, final long loadCount, final long failCount, final long totalTime, final long evictionCount, final long evictionWeight) {
    // many parameters of the same type in a row is a bad thing, but this class
    // is not constructed by end users and is too fine-grained for a builder.
    return new Metric(hitsCount, missCount, loadCount, failCount, totalTime, evictionCount, evictionWeight);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   concurrent
  /**
   ** Returns a thread-safe statistic accumulator.
   **
   ** @return                    a thread-safe statistic accumulator.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static Collector concurrent() {
    return new Concurrent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disabled
  /**
   ** Returns a statistic accumulator that does not record any cache metrics.
   **
   ** @return                    a statistic accumulator that does not record
   **                            any cache metrics.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static Collector disabled() {
    return Disabled.INSTANCE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guarded
  /**
   ** Returns a statistic accumulator that suppresses and logs any exception
   ** thrown by the delegate <code>collector</code>.
   **
   ** @param  collector          the accumulator to delegate to.
   **                            <br>
   **                            Allowed object is {@link Collector}.
   **
   ** @return                    a statistic accumulator that suppresses and
   **                            logs any exception thrown by the delegate.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static Collector guarded(final Collector collector) {
    return (collector instanceof Guarded) ? collector : new Guarded(collector);
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.hitsCount, this.missCount, this.loadCount,this.failCount, this.totalTime, this.evictionCount, this.evictionWeight);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Metric</code>s are considered equal if and only if they
   ** represent the same encoded, decoded and template value. As a consequence,
   ** two given <code>Metric</code>s may be different even though they contain
   ** the same attribute value.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    Metric that = (Metric)other;
    return this.hitsCount      == that.hitsCount
        && this.missCount      == that.missCount
        && this.loadCount      == that.loadCount
        && this.failCount      == that.failCount
        && this.totalTime      == that.totalTime
        && this.evictionCount  == that.evictionCount
        && this.evictionWeight == that.evictionWeight
    ;
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
    return getClass().getSimpleName() + '{' + "hitCount=" + this.hitsCount + ", " + "missCount=" + this.missCount + ", " + "loadCount=" + this.loadCount + ", " + "failCount=" + this.failCount + ", " + "totalTime=" + this.totalTime + ", " + "evictionCount=" + this.evictionCount + ", " + "evictionWeight=" + this.evictionWeight + '}';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minus
  /**
   ** Returns a new <code>Metric</code> representing the difference between this
   ** <code>Metric</code> and <code>other</code>.
   ** <br>
   ** Negative values, which aren't supported by <code>Metric</code> will be
   ** rounded up to zero.
   **
   ** @param  other              the statistics to subtract with.
   **                            <br>
   **                            Allowed object is <code>Metric</code>.
   **
   ** @return                    the difference between this instance and
   **                            <code>other</code>.
   **                            <br>
   **                            Possible object is <code>Metric</code>.
   */
  public Metric minus(final Metric other) {
    return Metric.of(
      Math.max(0L, subtract(this.hitsCount,     other.hitsCount))
    , Math.max(0L, subtract(this.missCount,     other.missCount))
    , Math.max(0L, subtract(this.loadCount,     other.loadCount))
    , Math.max(0L, subtract(this.failCount,     other.failCount))
    , Math.max(0L, subtract(this.totalTime,     other.totalTime))
    , Math.max(0L, subtract(this.evictionCount, other.evictionCount))
    , Math.max(0L, subtract(this.evictionWeight,other.evictionWeight))
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   plus
  /**
   ** Returns a new <code>Metric</code> representing the sum of this
   ** <code>Metric</code> and <code>other</code>.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The values of the metrics are undefined in case of overflow (though it is
   ** guaranteed not to throw an exception). If you require specific handling,
   ** we recommend implementing your own statistic collector.
   *
   * @param other the statistics to add with
   * @return the sum of the statistics
   */
  public Metric plus(final Metric other) {
    return Metric.of(
      add(this.hitsCount,      other.hitsCount)
    , add(this.missCount,      other.missCount)
    , add(this.loadCount,      other.loadCount)
    , add(this.failCount,      other.failCount)
    , add(this.totalTime,      other.totalTime)
    , add(this.evictionCount,  other.evictionCount)
    , add(this.evictionWeight, other.evictionWeight)
    );
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saturatedSubtract
  /**
   ** Returns the difference of <code>a</code> and <code>b</code> unless it
   ** would overflow or underflow in which case {@link Long.MAX_VALUE} or
   ** {@link Long.MIN_VALUE} is returned, respectively.
   */
  private static long subtract(final long a, final long b) {
    long naive = a - b;
    if ((a ^ b) >= 0 | (a ^ naive) >= 0) {
      // if a and b have the same signs or a has the same sign as the result
      // then there was no overflow, return
      return naive;
    }
    // we did over/under flow
    return Long.MAX_VALUE + ((naive >>> (Long.SIZE - 1)) ^ 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   saturatedAdd
  /**
   ** Returns the sum of <code>a</code> and <code>b</code> unless it would
   ** overflow or underflow in which case {@link Long.MAX_VALUE} or
   ** {@link Long.MIN_VALUE} is returned, respectively.
   */
  private static long add(final long a, final long b) {
    long naive = a + b;
    if ((a ^ b) < 0 | (a ^ naive) >= 0) {
      // if a and b have different signs or a has the same sign as the result
      // then there was no overflow, return
      return naive;
    }
    // we did over/under flow, if the sign is negative we should return MAX
    // otherwise MIN
    return Long.MAX_VALUE + ((naive >>> (Long.SIZE - 1)) ^ 1);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negativeToMaxValue
  /**
   ** Returns <code>value</code>, if non-negative; otherwise, returns
   ** {@link Long#MAX_VALUE}.
   **
   ** @return                    <code>value</code>, if non-negative; otherwise
   **                            {@link Long#MAX_VALUE}.
   */
  private static long negativeToMaxValue(final long value) {
    return (value >= 0) ? value : Long.MAX_VALUE;
  }
}