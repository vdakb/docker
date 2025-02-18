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

    File        :   CheckedFunction.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedFunction.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedFunction
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Represents a checked version of {@code Function}. that accepts one argument
 ** and produces a result.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #apply(Object)}.
 ** <p>
 ** Since Java 8, lambda expression to create a functional interface that throws
 ** an exception. To do this, you need to use a functional interface that
 ** declares the exception in its throws clause, or create your own functional
 ** interface that declares the exception.
 **
 ** @param  <T>                  the type of the input to the function.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <R>                  the type of the result of the function.
 **                              <br>
 **                              Allowed object is <code>&lt;R&gt;</code>.
 ** @param  <E>                  the type of the exception thrown if the
 **                              evaluation of the function fails.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Applies this function to the given argument.
   **
   ** @param  value              the function argument.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @return                    the function result.
   **                            <br>
   **                            Possisble object is <code>R</code>.
   **
   ** @throws E                  if the evaluation of the function fails.
   */
  R apply(final T value)
    throws E;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Returns a composed function that first applies the <code>before</code>
   ** function to its input, and then applies this function to the result.
   ** <br>
   ** If evaluation of either function throws an exception, it is relayed to
   ** the caller of the composed function.
   **
   ** @param  <V>                the type of input to the <code>before</code>
   **                            function, and to the composed function.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  before             the function to apply before this function is
   **                            applied.
   **                            <br>
   **                            Allowed object is <code>CheckedFunction</code>.
   **
   ** @return                    a composed function that first applies the
   **                            <code>before</code> function and then applies
   **                            this function.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedFunction</code>.
   **
   ** @throws E                    if applying this function to the given
   **                              argument failed.
   ** @throws NullPointerException if <code>before</code> is <code>null</code>.
   **
   ** @see    #andThen(CheckedFunction)
   */
  default <V> CheckedFunction<V, R, E> compose(final CheckedFunction<? super V, ? extends T, E> before)
    throws E {

    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   andThen
  /**
   ** Returns a composed function that first applies this function to its input,
   ** and then applies the <code>after</code> function to the result.
   ** <br>
   ** If evaluation of either function throws an exception, it is relayed to
   ** the caller of the composed function.
   **
   ** @param  <V>                the type of input to the <code>after</code>
   **                            function, and to the composed function.
   **                            <br>
   **                            Allowed object is <code>&lt;V&gt;</code>.
   ** @param  after              the function to apply after this function is
   **                            applied.
   **
   ** @return                    a composed function that first applies this
   **                            function and then applies the
   **                            <code>after</code> function.
   **
   ** @throws E                    if applying this function to the given
   **                              argument failed.
   ** @throws NullPointerException if <code>after</code> is <code>null</code>.
   **
   ** @see    #compose(CheckedFunction)
   */
  default <V> CheckedFunction<T, V, E> andThen(final CheckedFunction<? super R, ? extends V, E> after)
    throws E {

    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Returns a function that always returns its input argument.
   **
   ** @param  <T>                the type of the input and output objects to the
   **                            function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            evaluation of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   **
   ** @return                    a function that always returns its input
   **                            argument.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedFunction</code>.
   */
  static <T, E extends Exception> CheckedFunction<T, T, E> identity() {
    return (T t) -> t;
  }
}
