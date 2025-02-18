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

    File        :   Streams.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Streams.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-06  DSteding    First release version
*/

package oracle.hst.platform.core.stream;

import java.util.Map;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.Predicate;

//////////////////////////////////////////////////////////////////////////////
// abstract class Streams
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** Collection of utility methods for working with streams.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Streams {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // class Range
  // ~~~~~ ~~~~~
  /**
   ** A base class useful for implementing {@link Iterator}s for
   ** <code>Range</code>s.
   ** 
   ** @param  <T>                the type that the <code>Range</code> contains.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>
   */
  private static class Range<T> implements Iterator<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private T                                      next;
    private final Predicate<T>                     hasNext;
    private final Function<? super T, ? extends T> incrementer;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new <code>Range</code> {@link Iterator} where the elements
     ** are returned by {@link #next()} are formed by applying the function
     ** <code>fnc</code> to the elements of the {@link Iterator}s
     ** <code>start</code> and <code>end</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Both {@link Iterator}s should have the same amount of elements to be
     ** symmetric. If its isn't ensured that both iterators have the same size
     ** of elements the smaller one determines the amount of elements returned.
     **
     ** @param  start            the {@link Iterator} over the left-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Iterator} where each
     **                          element is of type <code>T</code>.
     ** @param  end              the {@link Iterator} over the right-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Iterator} where each
     **                          element is of type <code>U</code>.
     ** @param  fnc              the {@link Function} to apply to form the
     **                          resulting {@link Stream} elements.
     **                          <br>
     **                          Allowed object is {@link BiFunction} where each
     **                          elemnt is of type <code>T</code> for the first
     **                          function argument, <code>R</code> for the
     **                          second argument and <code>R</code> as the
     **                          function result.
     */
    private Range(final T start, final T end, final boolean closed, final Comparator<? super T> comparator, final Function<? super T, ? extends T> incrementer) {
      // ensure inheritance
      super();

      // initailize instance attributes
      this.next        = start;
      this.hasNext     = closed ? lessThanOrEqual(end, comparator) : lessThan(end, comparator);
      this.incrementer = incrementer;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasNext (Iterator)
    /**
     ** Returns <code>true</code> if the iteration has more elements.
     ** <br>
     ** (In other words, returns <code>true</code> if {@link #next} would return
     ** an element rather than throwing an exception.)
     **
     ** @return                  <code>true</code> if the iteration has more
     **                          elements; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean hasNext() {
      return this.hasNext.test(this.next);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: next (Iterator)
    /**
     ** Returns the next element in the iteration.
     **
     ** @return                  the next element in the iteration.
     **                          <br>
     **                          Possible object is <code>T</code>.
     **
     ** @throws NoSuchElementException if the iteration has no more elements.
     */
    @Override
    public final T next() {
      // prevent bogus state
      if (!hasNext())
        throw new NoSuchElementException();

      final T cursor = this.next;
      this.next = this.incrementer.apply(this.next);
      return cursor;
    }


    public static <T> Predicate<T> lessThan(final T value, final Comparator<? super T> comparator) {
      Objects.requireNonNull(value);
      Objects.requireNonNull(comparator);
      return t -> comparator.compare(t, value) < 0;
    }

    public static <T> Predicate<T> lessThanOrEqual(final T value, final Comparator<? super T> comparator) {
      Objects.requireNonNull(value);
      Objects.requireNonNull(comparator);
      return t -> comparator.compare(t, value) <= 0;
    }
/*
    public static <T> Predicate<T> greaterThan(final T value, final Comparator<? super T> comparator) {
      Objects.requireNonNull(value);
      Objects.requireNonNull(comparator);
      return t -> comparator.compare(t, value) > 0;
    }

    public static <T> Predicate<T> greaterThanOrEqual(final T value, final Comparator<? super T> comparator) {
      Objects.requireNonNull(value);
      Objects.requireNonNull(comparator);
      return t -> comparator.compare(t, value) >= 0;
    }

    public static <T extends Comparable<T>> Predicate<T> comparativelyEqual(final T value, final Comparator<? super T> comparator) {
      Objects.requireNonNull(value);
      Objects.requireNonNull(comparator);
      return t -> comparator.compare(t, value) == 0;
    }
*/
  }

  ////////////////////////////////////////////////////////////////////////////////
  // class Concat
  // ~~~~~ ~~~~~~
  /**
   ** <code>Concat</code> is an {@link Iterator} where the elements of this
   ** {@link Iterator} are formed by applying a {@link Function} to the elements
   ** of the source {@link Iterator}s to concatenate the elements of the first
   ** and second source.
   **
   ** @param  <T>                the expected type of the left-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <U>                the expected type of the right-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <R>                the expected type of the stream elements.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   */
  public static final class Concat<T, U, R> implements Iterator<R> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Iterator<? extends T>               lhs;
    private final Iterator<? extends U>               rhs;
    private final BiFunction<? super T, ? super U, R> fnc;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a new {@link Concat} {@link Iterator} where the elements are
     ** returned by {@link #next()} are formed by applying the function
     ** <code>fnc</code> to the elements of the {@link Iterator}s
     ** <code>lhs</code> and <code>rhs</code>.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Both {@link Iterator}s should have the same amount of elements to be
     ** symmetric. If its isn't ensured that both iterators have the same size
     ** of elements the smaller one determines the amount of elements returned.
     **
     ** @param  lhs              the {@link Iterator} over the left-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Iterator} where each
     **                          element is of type <code>T</code>.
     ** @param  rhs              the {@link Iterator} over the right-hand-side
     **                          elements.
     **                          <br>
     **                          Allowed object is {@link Iterator} where each
     **                          element is of type <code>U</code>.
     ** @param  fnc              the {@link Function} to apply to form the
     **                          resulting {@link Stream} elements.
     **                          <br>
     **                          Allowed object is {@link BiFunction} where each
     **                          elemnt is of type <code>T</code> for the first
     **                          function argument, <code>R</code> for the
     **                          second argument and <code>R</code> as the
     **                          function result.
     */
    public Concat(final Iterator<? extends T> lhs, final Iterator<? extends U> rhs, BiFunction<? super T, ? super U, R> fnc) {
      // ensure inheritance
      super();

      // initailize instance attributes
      this.lhs = lhs;
      this.rhs = rhs;
      this.fnc = fnc;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hasNext (Iterator)
    /**
     ** Returns <code>true</code> if the iteration has more elements.
     ** <br>
     ** (In other words, returns <code>true</code> if {@link #next} would return
     ** an element rather than throwing an exception.)
     **
     ** @return                  <code>true</code> if the iteration has more
     **                          elements; otherwise <code>false</code>.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public final boolean hasNext() {
      return lhs.hasNext() || rhs.hasNext();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: next (Iterator)
    /**
     ** Returns the next element in the iteration.
     **
     ** @return                  the next element in the iteration.
     **                          <br>
     **                          Possible object is <code>R</code>.
     **
     ** @throws NoSuchElementException if the iteration has no more elements.
     */
    @Override
    public final R next() {
      return fnc.apply(lhs.next(), rhs.next());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Streams</code> utility.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Streams()" and enforces use of the public method below.
   */
  private Streams() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   combine
  /**
   ** Returns a {@link Stream} where the elements of the {@link Stream} are
   ** formed by applying the function fnc to the elements of the
   ** {@link Iterator}s <code>lhs</code> and <code>rhs</code>.
   ** <p>
   ** <b>Note</b>:
   ** Both {@link Iterator}s should have the same number of elements to get a
   ** symmetrical result. If the number of elements differ, the {@link Iterator}
   ** with the smaller amount of elements determines the amount of results
   ** produced.
   **
   ** @param  <T>                the expected type of the left-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <U>                the expected type of the right-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <R>                the expected stream type.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  lhs                the {@link Iterator} over the left-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link Iterator} where each
   **                            element is of type <code>T</code>.
   ** @param  rhs                the {@link Iterator} over the right-hand-side
   **                            elements.
   **                            <br>
   **                            Allowed object is {@link Iterator} where each
   **                            element is of type <code>U</code>.
   ** @param  fnc                the {@link Function} to apply to form the
   **                            resulting {@link Stream} elements.
   **                            <br>
   **                            Allowed object is {@link BiFunction} where each
   **                            elemnt is of type <code>T</code> for the first
   **                            function argument, <code>R</code> for the
   **                            second argument and <code>R</code> as the
   **                            function result.
   **
   ** @return                    the result {@link Stream}.
   **                            <br>
   **                            Possible object is {@link Stream} where each
   **                            element is of type <code>R</code>.
   */
  public static <T, U, R> Stream<R> combine(final Stream<? extends T> lhs, final Stream<? extends U> rhs, BiFunction<? super T, ? super U, R> fnc) {
    final Spliterator<? extends T> f = lhs.spliterator();
    final Spliterator<? extends U> s = rhs.spliterator();
    final Concat<T, U, R>          c = new Concat<>(Spliterators.iterator(f), Spliterators.iterator(s), fnc);
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(c, 0), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Factory method to create a stream of given object.
   ** <br>
   ** Supported types are:
   ** <ul>
   **   <li>{@link Iterable}
   **   <li>{@link Map} (returns a stream of entryset)
   **   <li><code>int[]</code>
   **   <li><code>long[]</code>
   **   <code>double[]</code>
   **   <li><code>Object[]</code>
   **   <li>{@link Stream}
   ** </ul>
   ** Anything else is returned as a single-element stream.
   ** <code>null</code> is returned as an empty stream.
   **
   ** @param  <T>                the expected stream type.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  value              any object to get a stream for.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    a stream of given object.
   **                            <br>
   **                            Possible object is {@link Stream} where each
   **                            element is of type <code>T</code>.
   **
   ** @throws ClassCastException if <code>T</code> is of wrong type.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public static <T> Stream<T> stream(final Object value) {
    if (value instanceof Iterable<?>) {
      return (Stream<T>)StreamSupport.stream(((Iterable<?>)value).spliterator(), false);
    }
    else if (value instanceof Map<?, ?>) {
      return (Stream<T>)((Map<?, ?>)value).entrySet().stream();
    }
    else if (value instanceof int[]) {
      return (Stream<T>)Arrays.stream((int[])value).boxed();
    }
    else if (value instanceof long[]) {
      return (Stream<T>)Arrays.stream((long[])value).boxed();
    }
    else if (value instanceof double[]) {
      return (Stream<T>)Arrays.stream((double[])value).boxed();
    }
    else if (value instanceof Object[]) {
      return (Stream<T>)Arrays.stream((Object[])value);
    }
    else if (value instanceof Stream) {
      return (Stream<T>)value;
    }
    else if (value != null) {
      return (Stream<T>)Stream.of(value);
    }
    else {
      return Stream.empty();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Factory method to create a sequential {@link Stream} with the specified
   ** array as its source.
   **
   ** @param  <T>                the type of the array elements.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  value              the array, assumed to be unmodified during use
   **                            <br>
   **                            Allowed object is array of
   **                            <code>&lt;T&gt;</code>.
   **
   ** @return                    a {@link Stream} with the specified array as
   **                            its source.
   **                            <br>
   **                            Possible object is {@link Stream} where each
   **                            element is of type <code>T</code>.
   */
  @SuppressWarnings("unchecked")
  public static <T> Stream<T> stream (final T... value) {
    return value == null ? Stream.empty() : Arrays.stream(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Factory method to create a sequential {@link Stream} with the specified
   ** {@link Iterable} as its source.
   **
   ** @param  <T>                the type of the element of the specified
   **                            {@link Iterable} <code>value</code>.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  value              the {@link Iterable}, assumed to be unmodified
   **                            during use
   **                            <br>
   **                            Allowed object is{@link Iterable} where each
   **                            element is of type <code>T</code>.
   **
   ** @return                    a {@link Stream} with the specified
   **                            {@link Iterable} as its source.
   **                            <br>
   **                            Possible object is {@link Stream} where each
   **                            element is of type <code>T</code>.
   */
  public static <T> Stream<T> stream(final Iterable<T> value) {
    return value == null ? Stream.empty() : StreamSupport.stream(value.spliterator(), false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Factory method to create a sequential {@link Stream} with the specified
   ** {@link Map} as its source.
   **
   ** @param  <K>                the type of the key element of the specified
   **                            {@link Map} <code>value</code>.
   **                            <br>
   **                            Allowed object is <code>&lt;K&gt;</code>.
   ** @param  <V>                the type of the value element of the specified
   **                            {@link Map} <code>value</code>.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  value              the {@link Map}, assumed to be unmodified
   **                            during use
   **                            <br>
   **                            Allowed object is{@link Map} where each
   **                            element is of type <code>T</code> for the key
   **                            and <code>V</code> as the value.
   **
   ** @return                    a {@link Stream} with the specified {@link Map}
   **                            as its source.
   **                            <br>
   **                            Possible object is {@link Stream} where each
   **                            element is of type {@link Map.Entry}.
   */
  public static <K, V> Stream<Map.Entry<K, V>> stream(final Map<K, V> value) {
    return value == null ? Stream.empty() : value.entrySet().stream();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   range
  /**
   ** Returns a {@link Stream#flatMap(Function) flatMap} {@link Function} that
   ** only retains a instances of a given type and casts them to this type.
   ** <p>
   ** Unlike other flatMap functions, this function will only return
   ** <code>0</code> or <code>1</code> result. If an instance passed to it is of
   ** the specified type, then the function will return a {@link Stream} with
   ** only this item, cast to this type.
   ** <br>
   ** If the instance is not of this type, the function will return
   ** {@link Stream#empty()}.
   ** <br>
   ** <b>Example use</b>
   ** <br>
   ** Say we have a Stream&lt;X&gt; from which we want retain only all instances
   ** of <code>Y</code>, then could do the following to obtain a
   ** Stream&lt;X&gt;:
   ** <pre>
   **   Stream&lt;X&gt; streamX = ...;
   **   Stream&lt;Y&gt; streamY = streamX.flatMap(mapToType(Y.class));
   ** </pre>
   ** Which is the equivalent of this:
   ** <pre>
   **   streamX.filter(x -&gt; x instanceof Y).map(x -&gt; (Y)x)
   ** </pre>
   **
   **
   ** @param  <T>                the type of the elements in the stream.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <R>                the type of the instances to retain.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  clazz              the type of the instances to retain.
   **                            <br>
   **                            Allowed object is {@link Class} for type
   **                            <code>R</code>.
   **
   ** @return                    a flatMap function that only retains instances
   **                            of a given type.
   **                            Possible object is {@link Function} for type
   **                            <code>T</code> for the source and
   **                            {@link Stream} of type <code>R</code> as the
   **                            result.
   */
  public static <T, R extends T> Function<T, Stream<R>> mapToType(final Class<R> clazz) {
    return T -> {
      if (clazz.isInstance(T)) {
        return Stream.of(clazz.cast(T));
      }
      return Stream.empty();
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   range
  public static <T extends Comparable<T>> Stream<T> range(final T start, T endExclusive, final Function<? super T, ? extends T> incrementer) {
    return range(start, endExclusive, incrementer, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   range
  public static <T> Stream<T> range(final T start, final T endExclusive, Function<? super T, ? extends T> incrementer, final Comparator<? super T> comparator) {
    return rangeStream(start, endExclusive, false, incrementer, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rangeClosed
  public static <T extends Comparable<T>> Stream<T> rangeClosed(final T start, final T endInclusive, final Function<? super T, ? extends T> incrementer) {
    return rangeClosed(start, endInclusive, incrementer, Comparator.naturalOrder());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rangeClosed
  public static <T> Stream<T> rangeClosed(final T start, final T endInclusive, final Function<? super T, ? extends T> incrementer, final Comparator<? super T> comparator) {
    return rangeStream(start, endInclusive, true, incrementer, comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rangeStream
  private static <T> Stream<T> rangeStream(final T start, final T end, final boolean closed, final Function<? super T, ? extends T> incrementer, final Comparator<? super T> comparator) {
    final Iterator<T>    iterator = new Range<>(start, end, closed, comparator, incrementer);
    final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.SORTED | Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE);
    return StreamSupport.stream(spliterator, false);
  }
}
