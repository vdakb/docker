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

    File        :   Unchecked.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Unchecked.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.lang.reflect.InvocationTargetException;

import java.util.function.Function;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.io.UncheckedIOException;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;
import java.util.function.BinaryOperator;

////////////////////////////////////////////////////////////////////////////////
// final class Unchecked
// ~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** Static utility methods that convert checked exceptions to unchecked.
 ** <p>
 ** Two {@code wrap()} methods are provided that can wrap an arbitrary piece of
 ** logic and convert checked exceptions to unchecked.
 ** <p>
 ** A number of other methods are provided that allow a lambda block to be
 ** decorated to avoid handling checked exceptions.
 ** <br>
 ** For example, the method {@code File#getCanonicalFile()} throws an
 ** {@link IOException} which can be handled as follows:
 ** <pre>
 **   stream.map(Unchecked.function(file -&gt; file.getCanonicalFile())
 ** </pre>
 ** Each method accepts a functional interface that is defined to throw
 ** {@link Throwable}.
 ** <br>
 ** Catching {@code Throwable} means that any method can be wrapped.
 ** <p>
 ** Any {@code InvocationTargetException} is extracted and processed
 ** recursively.
 ** Any {@link IOException} is converted to an {@link UncheckedIOException}.
 ** Any {@link ReflectiveOperationException} is converted to an
 ** {@link UncheckedException}.
 ** Any {@link Error} or {@link RuntimeException} is re-thrown without
 ** alteration.
 ** Any other exception is wrapped in a {@link RuntimeException}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class Unchecked {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Unchecked</code>.
   ** <br>
   ** This constructor is private to prevent other classes to use
   ** "new Unchecked()" and enforces use of the public method below.
   */
  private Unchecked() {
    // should never be instantiated
    throw new AssertionError();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   runnable
  /**
   ** Converts checked exceptions to unchecked based on the {@link Runnable}
   ** interface.
   ** <p>
   ** This wraps the specified runnable returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  runnable           the runnable to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedRunnable}.
   **
   ** @return                    the runnable instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link Runnable}.
   */
  public static Runnable runnable(final CheckedRunnable runnable) {
    return () -> {
      try {
        runnable.run();
      }
      catch (Throwable t) {
        throw propagate(t);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   supplier
  /**
   ** Converts checked exceptions to unchecked based on the {@link Supplier}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   * @param  <R>                 the result type of the supplier.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the predicate fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  supplier           the supplier to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedSupplier}.
   **
   ** @return                    the supplier instance that handles checked
   **                            exceptions
   **                            <br>
   **                            Possible object is {@link Supplier}.
   */
  public static <R, E extends Exception> Supplier<R> supplier(final CheckedSupplier<R, E> supplier) {
    return () -> {
      try {
        return supplier.get();
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   consumer
  /**
   ** Converts checked exceptions to unchecked based on the {@link Consumer} interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the input type of the consumer.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the predicate fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  consumer           the consumer to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedConsumer}.
   **
   ** @return                    the consumer instance that handles checked
   **                            exceptions
   **                            <br>
   **                            Possible object is {@link Consumer}.
   */
  public static <T, E extends Exception> Consumer<T> consumer(final CheckedConsumer<T, E> consumer) {
    return (t) -> {
      try {
        consumer.accept(t);
      } catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   biconsumer
  /**
   ** Converts checked exceptions to unchecked based on the {@link BiConsumer}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the type of the first input argument to the
   **                            consumer.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <U>                the type of the second input argument to the
   **                            consumer.
   **                            <br>
   **                            Allowed object is <code>&lt;U&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the operation fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  consumer           the predicate to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedBiConsumer}.
   **
   ** @return                    the consumer instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link BiConsumer}.
   */
  public static <T, U, E extends Exception> BiConsumer<T, U> biConsumer(CheckedBiConsumer<T, U, E> consumer) {
    return (t, u) -> {
      try {
        consumer.accept(t, u);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   predicate
  /**
   ** Converts checked exceptions to unchecked based on the {@link Predicate}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the input type of the predicate.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the predicate fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  predicate          the predicate to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedPredicate}.
   **
   ** @return                    the predicate instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link Predicate}.
   */
  public static <T, E extends Exception> Predicate<T> predicate(final CheckedPredicate<T, E> predicate) {
    return (t) -> {
      try {
        return predicate.test(t);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bipredicate
  /**
   ** Converts checked exceptions to unchecked based on the {@link BiPredicate}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the type of the first input argument to the
   **                            predicate.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <U>                the type of the second input argument to the
   **                            predicate.
   **                            <br>
   **                            Allowed object is <code>&lt;U&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the operation fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  predicate          the predicate to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedBiPredicate}.
   **
   ** @return                    the predicate instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link BiPredicate}.
   */
  public static <T, U, E extends Exception> BiPredicate<T, U> biPredicate(CheckedBiPredicate<T, U, E> predicate) {
    return (t, u) -> {
      try {
        return predicate.test(t, u);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   function
  /**
   ** Converts checked exceptions to unchecked based on the {@link Function}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the input type of the function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <R>                the return type of the function.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  function           the function to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedFunction}.
   **
   ** @return                    the function instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link Function}.
   */
  public static <T, R, E extends Exception> Function<T, R> function(final CheckedFunction<T, R, E> function) {
    return (t) -> {
      try {
        return function.apply(t);
      }
      catch (Exception e) {
        throw propagate(e);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bifunction
  /**
   ** Converts checked exceptions to unchecked based on the {@link BiFunction}
   ** interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   * 
   ** @param  <T>                the first input type of the function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <U>                the second input type of the function.
   **                            <br>
   **                            Allowed object is <code>&lt;U&gt;</code>.
   ** @param  <R>                the return type of the function.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  function           the function to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedBiFunction}.
   **
   ** @return                    the function instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link BiFunction}.
   */
  public static <T, U, R, E extends Exception> BiFunction<T, U, R> bifunction(CheckedBiFunction<T, U, R, E> function) {
    return (t, u) -> {
      try {
        return function.apply(t, u);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   binaryOperator
  /**
   ** Converts checked exceptions to unchecked based on the
   ** {@link BinaryOperator} interface.
   ** <p>
   ** This wraps the specified operator returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   ** @param  <T>                the input type of the operator.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  operator           the operator to be decorated.
   **                            <br>
   **                            Allowed object is
   **                            {@link CheckedBinaryOperator}.
   **
   ** @return                    the operator instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link BinaryOperator}.
   */
  public static <T, E extends Exception> BinaryOperator<T> binaryOperator(CheckedBinaryOperator<T, E> operator) {
    return (t, u) -> {
      try {
        return operator.apply(t, u);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unaryOperator
  /**
   ** Converts checked exceptions to unchecked based on the
   ** {@link UnaryOperator} interface.
   ** <p>
   ** This wraps the specified function returning an instance that handles
   ** checked exceptions.
   ** <br>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the input type of the operator.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  operator           the operator to be decorated.
   **                            <br>
   **                            Allowed object is {@link CheckedUnaryOperator}.
   **
   ** @return                    the operator instance that handles checked
   **                            exceptions.
   **                            <br>
   **                            Possible object is {@link UnaryOperator}.
   */
  public static <T, E extends Exception> UnaryOperator<T> unaryOperator(final CheckedUnaryOperator<T, E> operator) {
    return (t) -> {
      try {
        return operator.apply(t);
      }
      catch (Throwable ex) {
        throw propagate(ex);
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wrap
  /**
   ** Wraps a block of code, converting checked exceptions to unchecked.
   ** <pre>
   **   Unchecked.wrap(() -&gt; {
   **     // any code that throws a checked exception
   **   }
   ** </pre>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  block              the code block to wrap.
   **                            <br>
   **                            Allowed object is {@link CheckedRunnable}.
   **
   ** @throws RuntimeException     if an exception occurs.
   ** @throws UncheckedIOException if an IO exception occurs.
   */
  public static void wrap(final CheckedRunnable block) {
    try {
      block.run();
    }
    catch (Throwable t) {
      throw propagate(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   wrap
  /**
   ** Wraps a block of code, converting checked exceptions to unchecked.
   ** <pre>
   **   Unchecked.wrap(() -&gt; {
   **     // any code that throws a checked exception
   **   }
   ** </pre>
   ** If a checked exception is thrown it is converted to an
   ** {@link UncheckedIOException} or {@link RuntimeException} as appropriate.
   **
   ** @param  <T>                the expected type of the result.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  block              the code block to wrap.
   **                            <br>
   **                            Allowed object is {@link CheckedRunnable}.
   **
   ** @return                    the result of invoking the block.
   **                            <br>
   **                            Possible object is <code>T</code>.
   **
   ** @throws RuntimeException     if an exception occurs.
   ** @throws UncheckedIOException if an IO exception occurs.
   */
  public static <T, E extends Exception> T wrap(final CheckedSupplier<T, E> block) {
    try {
      return block.get();
    }
    catch (Throwable t) {
      throw propagate(t);
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   propagate
  /**
   ** Propagates <code>throwable</code> as-is if possible, or by wrapping in a
   ** {@link RuntimeException} if not.
   ** <ul>
   **   <li>If <code>throwable</code> is an {@link InvocationTargetException}
   **       the cause is extracted and processed recursively.
   **   <li>If <code>throwable</code> is an {@link CompletionException} the
   **       cause is extracted and processed recursively.
   **   <li>If <code>throwable</code> is an {@link ExecutionException} the cause
   **       is extracted and processed recursively.
   **   <li>If <code>throwable</code> is an {@link Error} or
   **       {@code RuntimeException} it is propagated as-is.
   **   <li>If <code>throwable</code> is an {@link IOException} it is wrapped in
   **       {@code UncheckedIOException} and thrown.
   **   <li>If <code>throwable</code> is an {@link ReflectiveOperationException}
   **       it is wrapped in {@link UncheckedException} and
   **       thrown.
   **   <li>Otherwise <code>throwable</code> is wrapped in a
   **       {@code RuntimeException} and thrown.
   ** </ul>
   ** This method always throws an exception. The return type is a convenience
   ** to satisfy the type system when the enclosing method returns a value. For
   ** example:
   ** <pre>
   **   T foo() {
   **     try {
   **       return methodWithCheckedException();
   **     }
   **     catch (Exception e) {
   **       throw Unchecked.propagate(e);
   **     }
   **   }
   ** </pre>
   **
   ** @param  throwable          the {@link Throwable} to propagate.
   **                            <br>
   **                            allowed object is {@link Throwable}.
   **
   ** @return                    never returns as an exception is always thrown.
   **                            <br>
   **                            Possible object is {@link RuntimeException}.
   */
  public static RuntimeException propagate(final Throwable throwable) {
    if (throwable instanceof InvocationTargetException) {
      throw propagate(throwable.getCause());
    }
    else if (throwable instanceof CompletionException) {
      throw propagate(throwable.getCause());
    }
    else if (throwable instanceof ExecutionException) {
      throw propagate(throwable.getCause());
    }
    else if (throwable instanceof IOException) {
      throw new UncheckedIOException((IOException)throwable);
    }
    else if (throwable instanceof ReflectiveOperationException) {
      throw new UncheckedException((ReflectiveOperationException)throwable);
    }
    else {
      throwIfUnchecked(throwable);
      throw new RuntimeException(throwable);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwIfUnchecked
  /**
   ** Throws <code>throwable</code> if it is a {@link RuntimeException} or
   ** {@link Error}. Example usage:
   ** <pre>
   **   for (Foo foo : foos) {
   **     try {
   **       foo.bar();
   **     }
   **     catch (RuntimeException | Error t) {
   **       failure = t;
   **     }
   **   }
   **   if (failure != null) {
   **     throwIfUnchecked(failure);
   **     throw new AssertionError(failure);
   **   }
   ** </pre>
   **
   ** @param  throwable          the {@link Throwable} to throw if its an
   **                            unchecked exception.
   **                            <br>
   **                            Allowed object is {@link Throwable}.
   */
  public static void throwIfUnchecked(final Throwable throwable) {
    if (throwable instanceof RuntimeException) {
      throw (RuntimeException)throwable;
    }
    if (throwable instanceof Error) {
      throw (Error)throwable;
    }
  }
}