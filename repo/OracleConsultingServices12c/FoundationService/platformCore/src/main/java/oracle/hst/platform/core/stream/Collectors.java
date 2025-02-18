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

    File        :   Collectors.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Collectors.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.stream;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;

import java.util.stream.Stream;
import java.util.stream.Collector;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

//////////////////////////////////////////////////////////////////////////////
// abstract class Collectors
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Collection of utility methods for working with collectors.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Collectors {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Batch
  private static class Batch<T> implements Collector<T, List<T>, Void> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int               size;
    private final Consumer<List<T>> payload;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Batch</code> {@link Collector} where the
     ** elements are formed by the provided {@link List} embeded in a
     ** {@link Consumer}.
     **
     ** @param  payload          the collection of elements elements.
     **                          <br>
     **                          Allowed object is {@link Consumer} where the
     **                          element is of type {@link List}.
     ** @param  size             the amount of elements to assign to the
     **                          collector.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    private Batch(final Consumer<List<T>> payload, final int size) {
      // ensure inheritance
      super();

      // initailize instance attributes
      this.size    = size;
      this.payload = payload;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supplier (Collector)
    /**
     ** A function that creates and returns a new mutable result container.
     **
     ** @return                  a function which returns a new, mutable result
     **                          container.
     **                          <br>
     **                          Possible object is {@link Supplier} where the
     **                          element is of type {@link List}.
     */
    @Override
    public Supplier<List<T>> supplier() {
      return ArrayList::new;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: accumulator (Collector)
    /**
     ** A function that folds a value into a mutable result container.
     **
     ** @return                  a function which folds a value into a mutable
     **                          result container.
     **                          <br>
     **                          Possible object is {@link BiConsumer} where
     **                          the element is of type {@link List}.
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
      return (list, element) -> {
        list.add(element);
        if (list.size() == this.size) {
          this.payload.accept(list);
          list.clear();
        }
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: combiner (Collector)
    /**
     ** A function that accepts two partial results and merges them.
     ** <br>
     ** The combiner function may fold state from one argument into the other
     ** and return that, or may return a new result container.
     **
     ** @return                  a function which combines two partial results
     **                          into a combined result.
     **                          <br>
     **                          Possible object is {@link BinaryOperator} where
     **                          the element is of type {@link Result}.
     */
    @Override
    public BinaryOperator<List<T>> combiner() {
      return (lhs, rhs) -> {
        if (lhs.size() + rhs.size() < this.size) {
          lhs.addAll(rhs);
          return lhs;
        }
        while (lhs.size() < this.size) {
          lhs.add(rhs.remove(0));
        }
        this.payload.accept(lhs);
        return rhs;
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: finisher (Collector)
    /**
     ** Perform the final transformation from the intermediate accumulation
     ** types <code>IL</code> and <code>IR</code>} to the final result types
     ** <code>RL</code> and <code>RR</code>.
     **
     ** @return                  a function which transforms the intermediate
     **                          result to the final result
     **                          <br>
     **                          Possible object is {@link Function} where the
     **                          element is of type {@link Result}.
     */
    @Override
    public Function<List<T>, Void> finisher() {
      return (list) -> {
        if (!list.isEmpty()) {
          this.payload.accept(list);
        }
        return null;
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characteristics (Collector)
    /**
     ** Returns a {@link Set} of {@link Collector.Characteristics} indicating
     ** the characteristics of this {@link Collector}.
     ** <br>
     ** This set should be immutable.
     **
     ** @return                  an immutable set of collector characteristics.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Characteristics}.
     */
    @Override
    public Set<Characteristics> characteristics() {
      // TODO: correctly determine elements
      return Collections.emptySet();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class FindLast
  /**
   ** A {@link Collector} which will return the last element of a stream, if
   ** present.
   **
   ** @param  <T>                the type of the elements in the {@link Stream}
   **                            to traverse.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  private static class FindLast<T> implements Collector<T, FindLast.Result<T>, Optional<T>> {

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // class Result
    // ~~~~~ ~~~~~~
    /**
     ** The mutable accumulation type of the reduction operation.
     **
     ** @param  <T>              the accumulation type.
     **                          <br>
     **                          Allowed object is <code>&lt;T&gt;</code>
     */
    private static class Result<T> {

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      private boolean set;
      private T       element;

      //////////////////////////////////////////////////////////////////////////
      // Methods grouped by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: next
      void next(final T element) {
        this.set     = true;
        this.element = element;
      }

      Result<T> combine(final Result<T> other) {
        if (other.set)
          return other;

        return this;
      }

      Optional<T> finish() {
        return (this.set) ? Optional.of(this.element) : Optional.empty();
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supplier (Collector)
    /**
     ** A function that creates and returns a new mutable result container.
     **
     ** @return                  a function which returns a new, mutable result
     **                          container.
     **                          <br>
     **                          Possible object is {@link Supplier} where the
     **                          element is of type {@link Result}.
     */
    @Override
    public Supplier<Result<T>> supplier() {
      return Result::new;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: accumulator (Collector)
    /**
     ** A function that folds a value into a mutable result container.
     **
     ** @return                  a function which folds a value into a mutable
     **                          result container.
     **                          <br>
     **                          Possible object is {@link BiConsumer} where
     **                          the element is of type {@link Result}.
     */
    @Override
    public BiConsumer<Result<T>, T> accumulator() {
      return Result::next;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: combiner (Collector)
    /**
     ** A function that accepts two partial results and merges them.
     ** <br>
     ** The combiner function may fold state from one argument into the other
     ** and return that, or may return a new result container.
     **
     ** @return                  a function which combines two partial results
     **                          into a combined result.
     **                          <br>
     **                          Possible object is {@link BinaryOperator} where
     **                          the element is of type {@link Result}.
     */
    @Override
    public BinaryOperator<Result<T>> combiner() {
      return Result::combine;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: finisher (Collector)
    /**
     ** Perform the final transformation from the intermediate accumulation
     ** types <code>IL</code> and <code>IR</code>} to the final result types
     ** <code>RL</code> and <code>RR</code>.
     **
     ** @return                  a function which transforms the intermediate
     **                          result to the final result
     **                          <br>
     **                          Possible object is {@link Function} where the
     **                          element is of type {@link Result}.
     */
    @Override
    public Function<Result<T>, Optional<T>> finisher() {
      return Result::finish;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characteristics (Collector)
    /**
     ** Returns a {@link Set} of {@link Collector.Characteristics} indicating
     ** the characteristics of this {@link Collector}.
     ** <br>
     ** This set should be immutable.
     **
     ** @return                  an immutable set of collector characteristics.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Characteristics}.
     */
    @Override
    public Set<Characteristics> characteristics() {
      // TODO: correctly determine elements
      return Collections.emptySet();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ReversedStream
  // ~~~~~ ~~~~~~~~~~~~~~
  /**
   ** A {@link Collector} which takes the elements of the current stream and
   ** returns a new stream with the same elements in reverse order.
   ** <p>
   ** This collector will collect all elements from a stream into memory in
   ** order to return the reversed stream. As a result this collector may not
   ** be suitable for extremely large or infinite streams.
   **
   ** @param  <T>                the type of the elements in the {@link Stream}
   **                            to reveres.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  private static class ReversedStream<T> implements Collector<T, LinkedList<T>, Stream<T>> {

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supplier (Collector)
    /**
     ** A function that creates and returns a new mutable result container.
     **
     ** @return                  a function which returns a new, mutable result
     **                          container.
     **                          <br>
     **                          Possible object is {@link Supplier} where the
     **                          element is of type {@link LinkedList}.
     */
    @Override
    public Supplier<LinkedList<T>> supplier() {
      return LinkedList::new;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: accumulator (Collector)
    /**
     ** A function that folds a value into a mutable result container.
     **
     ** @return                  a function which folds a value into a mutable
     **                          result container.
     **                          <br>
     **                          Possible object is {@link BiConsumer} where
     **                          the element is of type {@link LinkedList}.
     */
    @Override
    public BiConsumer<LinkedList<T>, T> accumulator() {
      return LinkedList::addFirst;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: combiner (Collector)
    /**
     ** A function that accepts two partial results and merges them.
     ** <br>
     ** The combiner function may fold state from one argument into the other
     ** and return that, or may return a new result container.
     **
     ** @return                  a function which combines two partial results
     **                          into a combined result.
     **                          <br>
     **                          Possible object is {@link BinaryOperator} where
     **                          the element is of type {@link LinkedList}.
     */
    @Override
    public BinaryOperator<LinkedList<T>> combiner() {
      return (t1, t2) -> {
        t2.addAll(t1);
        return t2;
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: finisher (Collector)
    /**
     ** Perform the final transformation from the intermediate accumulation
     ** types <code>IL</code> and <code>IR</code>} to the final result types
     ** <code>RL</code> and <code>RR</code>.
     **
     ** @return                  a function which transforms the intermediate
     **                          result to the final result
     **                          <br>
     **                          Possible object is {@link Function} where the
     **                          element is of type {@link LinkedList}.
     */
    @Override
    public Function<LinkedList<T>, Stream<T>> finisher() {
      return LinkedList::stream;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characteristics (Collector)
    /**
     ** Returns a {@link Set} of {@link Collector.Characteristics} indicating
     ** the characteristics of this {@link Collector}.
     ** <br>
     ** This set should be immutable.
     **
     ** @return                  an immutable set of collector characteristics.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Characteristics}.
     */
    @Override
    public Set<Characteristics> characteristics() {
      // TODO: correctly determine elements
      return Collections.emptySet();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Combined
  // ~~~~~ ~~~~~~~~
  /**
   ** A base class useful for implementing {@link Collector}s for combined
   ** <code>Map</code>s.
   ** 
   ** @param  <T>                the type of input elements to the reduction
   **                            operation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **@param  <IL>                the type of keys maintained by left-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;IL&gt;</code>.
   **@param  <IR>                the type of mapped values by right-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;IR&gt;</code>.
   **@param  <RL>                the type of keys maintained by left-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;RL&gt;</code>.
   **@param  <RR>                the type of mapped values by right-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;RR&gt;</code>.
   */
  private static class Combined<T, IL, IR, RL, RR> implements Collector<T, Map.Entry<IL, IR>, Map.Entry<RL, RR>> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Collector<T, IL, RL> lhs;
    private final Collector<T, IR, RR> rhs;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Combined</code> {@link Collector} where the
     ** elements are formed by combining the two {@link Collector}s.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Both {@link Collector}s should have the same amount of elements to be
     ** symmetric. If its isn't ensured that both collector have the same size
     ** of elements the smaller one determines the amount of elements returned.
     **
     ** @param  lhs              the {@link Collector} over the left-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Collector} where each
     **                          element is of type <code>T</code>.
     ** @param  rhs              the {@link Collector} over the right-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Collector} where each
     **                          element is of type <code>U</code>.
     */
    private Combined(final Collector<T, IL, RL> lhs, final Collector<T, IR, RR> rhs) {
      // ensure inheritance
      super();

      // initailize instance attributes
      this.lhs = lhs;
      this.rhs = rhs;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: supplier (Collector)
    /**
     ** A function that creates and returns a new mutable result container.
     **
     ** @return                  a function which returns a new, mutable result
     **                          container.
     **                          <br>
     **                          Possible object is {@link Supplier} where the
     **                          element is of type {@link Map.Entry}.
     */
    @Override
    public Supplier<Map.Entry<IL, IR>> supplier() {
      return () -> new AbstractMap.SimpleEntry<>(this.lhs.supplier().get(), this.rhs.supplier().get());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: accumulator (Collector)
    /**
     ** A function that folds a value into a mutable result container.
     **
     ** @return                  a function which folds a value into a mutable
     **                          result container.
     **                          <br>
     **                          Possible object is {@link BiConsumer} where
     **                          the element is of type {@link Map.Entry}.
     */
    @Override
    public BiConsumer<Map.Entry<IL, IR>, T> accumulator() {
      final BiConsumer<IL, T> lhs = this.lhs.accumulator();
      final BiConsumer<IR, T> rhs = this.rhs.accumulator();
      return (pair, element) -> {
        lhs.accept(pair.getKey(), element);
        rhs.accept(pair.getValue(), element);
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: combiner (Collector)
    /**
     ** A function that accepts two partial results and merges them.
     ** <br>
     ** The combiner function may fold state from one argument into the other
     ** and return that, or may return a new result container.
     **
     ** @return                  a function which combines two partial results
     **                          into a combined result.
     **                          <br>
     **                          Possible object is {@link BinaryOperator} where
     **                          the element is of type {@link Map.Entry}.
     */
    @Override
    public BinaryOperator<Map.Entry<IL, IR>> combiner() {
      final BinaryOperator<IL> lhs = this.lhs.combiner();
      final BinaryOperator<IR> rhs = this.rhs.combiner();
      return (pairL, pairR) -> {
        final IL il = lhs.apply(pairL.getKey(),   pairR.getKey());
        final IR ir = rhs.apply(pairL.getValue(), pairR.getValue());
        return new AbstractMap.SimpleEntry<>(il, ir);
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: finisher (Collector)
    /**
     ** Perform the final transformation from the intermediate accumulation
     ** types <code>IL</code> and <code>IR</code>} to the final result types
     ** <code>RL</code> and <code>RR</code>.
     **
     ** @return                  a function which transforms the intermediate
     **                          result to the final result
     **                          <br>
     **                          Possible object is {@link Function} where the
     **                          element is of type {@link Map.Entry}.
     */
    @Override
    public Function<Map.Entry<IL, IR>, Map.Entry<RL, RR>> finisher() {
      final Function<IL, RL> lhs = this.lhs.finisher();
      final Function<IR, RR> rhs = this.rhs.finisher();
      return (pair) -> {
        final RL rl = lhs.apply(pair.getKey());
        final RR rr = rhs.apply(pair.getValue());
        return new AbstractMap.SimpleEntry<>(rl, rr);
      };
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: characteristics (Collector)
    /**
     ** Returns a {@link Set} of {@link Collector.Characteristics} indicating
     ** the characteristics of this {@link Collector}.
     ** <br>
     ** This set should be immutable.
     **
     ** @return                  an immutable set of collector characteristics.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Characteristics}.
     */
    @Override
    public Set<Characteristics> characteristics() {
      // TODO: correctly determine elements
      return Collections.emptySet();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Collectors</code> utility.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Collectors()" and enforces use of the public method below.
   */
  private Collectors() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findLast
  /**
   ** Returns a {@link Collector} which will return the last element of a
   ** stream, if present.
   **
   ** @param  <T>                the type of the elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    an {@link Optional} containing the last
   **                            element of the stream or
   **                            {@link Optional#empty()} if the stream is
   **                            empty.
   */
  public static <T> Collector<T, ?, Optional<T>> findLast() {
    return new FindLast<>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   batch
  public static <T> Collector<T, ?, Void> batch(final Consumer<List<T>> payload, final int size) {
    return new Batch<>(payload, size);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toList
  /**
   ** Returns a {@link Collector} that accumulates the input elements into a new
   ** {@link List}. There are no guarantees on the type, mutability,
   ** serializability, or thread-safety of the {@link List} returned.
   **
   ** @param  <T>                the type of the input elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    a {@link Collector} which collects all the
   **                            input elements into a {@link List}.
   **                            <br>
   **                            Possible object is {@link Collector} for type
   **                            <code>T</code>.
   */
	public static <T> Collector<T, ?, List<T>> toList() {
		return java.util.stream.Collectors.toList();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toSet
  /**
   ** Returns a {@link Collector} that accumulates the input elements into a new
   ** {@link Set}. There are no guarantees on the type, mutability,
   ** serializability, or thread-safety of the {@link Set} returned.
   ** <p>
   ** This is an
   ** {@link Collector.Characteristics#UNORDERED unordered} Collector.
   **
   ** @param  <T>                the type of the input elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    a {@link Collector} which collects all the
   **                            input elements into a {@link Set}.
   **                            <br>
   **                            Possible object is {@link Collector} for type
   **                            <code>T</code>.
   */
	public static <T> Collector<T, ?, Set<T>> toSet() {
		return java.util.stream.Collectors.toSet();
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toLinkedSet
  /**
   ** Returns a {@link Collector} that accumulates elements into a new
   ** {@link LinkedHashSet}, in encounter order.
   **
   ** @param  <T>                the type of the input elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    a {@link Collector} which collects all the
   **                            input elements into a {@link LinkedHashSet}.
   **                            <br>
   **                            Possible object is {@link Collector} for type
   **                            <code>T</code>.
   */
	public static <T> Collector<T, ?, Set<T>> toLinkedSet() {
		return java.util.stream.Collectors.toCollection(LinkedHashSet::new);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMap
  /**
   ** Returns a {@link Collector} that accumulates elements into a {@link Map}
   ** whose keys and values are the result of applying the provided mapping
   ** functions to the input elements.
   **
   ** @param  <T>                the type of the input element function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <K>                the type of the key mapping function.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  mapper             a mapping function to produce keys.
   **                            <br>
   **                            Allowed object is {@link Function} where the
   **                            source is of type <code>T</code> and the
   **                            result of type <code>K</code>.
   **
   ** @return                    a {@link Collector} which collects elements
   **                            into a {@link Map} whose keys are the result of
   **                            applying mapping functions to it.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static <T, K> Collector<T, ?, Map<K, T>> toMap(final Function<? super T, ? extends K> mapper) {
    return java.util.stream.Collectors.toMap(mapper, Function.identity());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toLinkedMap
  /**
   ** Returns a {@link Collector} that accumulates elements into a
   ** {@link LinkedHashMap} whose keys and values are the result of applying the
   ** provided mapping functions to the input elements.
   **
   ** @param  <T>                the type of the input element function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <K>                the type of the key mapping function.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  mapper             a mapping function to produce keys.
   **                            <br>
   **                            Allowed object is {@link Function} where the
   **                            source is of type <code>T</code> and the
   **                            result of type <code>K</code>.
   **
   ** @return                    a {@link Collector} which collects elements
   **                            into a {@link LinkedHashMap} whose keys are the
   **                            result of applying mapping functions to it.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static <T, K> Collector<T, ?, Map<K, T>> toLinkedMap(final Function<? super T, ? extends K> mapper) {
    return java.util.stream.Collectors.toMap(mapper, Function.identity(), (l, r) -> l, LinkedHashMap::new);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combine
  /**
   ** Returns a {@link Collector} where the elements are formed by combining the
   ** two {@link Collector}s provided.
   ** 
   ** @param  <T>                the type of input elements to the reduction
   **                            operation.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **@param  <K>                 the type of keys maintained by left-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;RL&gt;</code>.
   **@param  <V>                 the type of mapped values by right-hand-side
   **                            mapping.
   **                            <br>
   **                            Allowed object is <code>&lt;RR&gt;</code>.
   ** @param  lhs                the left-hand-side {@link Collector}.
   **                            <br>
   **                            Allowed object is {@link Collector}.
   ** @param  rhs                the right-hand-side {@link Collector}.
   **                            <br>
   **                            Allowed object is {@link Collector}.
   **
   ** @return                    the {@link Collector} with final transformation
   **                            from the intermediate accumulation to result
   **                            types.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static <T, K, V> Collector<T, ?, Map.Entry<K, V>> combine(final Collector<T, ?, K> lhs, final Collector<T, ?, V> rhs) {
    return new Combined<>(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reversedStream
  /**
   ** Returns a {@link Collector} which takes the elements of the current stream
   ** and returns a new stream with the same elements in reverse order.
   ** <p>
   ** This collector will collect all elements from a stream into memory in
   ** order to return the reversed stream. As a result this collector may not
   ** be suitable for extremely large or infinite streams.
   **
   ** @param  <T>                the type of the elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    a {@link Collector} that returns the elements
   **                            of a stream in reverse order.
   **                            <br>
   **                            Possible object is {@link Collector}.
   */
  public static <T> Collector<T, ?, Stream<T>> reversedStream() {
    return new ReversedStream<>();
  }
}