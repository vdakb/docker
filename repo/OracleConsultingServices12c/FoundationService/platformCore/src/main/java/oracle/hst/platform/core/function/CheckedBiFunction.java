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

    File        :   CheckedBiFunction.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedBiFunction.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.util.Objects;

import java.util.function.Function;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedBiFunction
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Represents a function that accepts two arguments and produces a result.
 ** <br>
 ** This is the two-arity specialization of {@link Function}.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #apply(Object, Object)}.
 ** <p>
 ** Since Java 8, lambda expression to create a functional interface that throws
 ** an exception. To do this, you need to use a functional interface that
 ** declares the exception in its throws clause, or create your own functional
 ** interface that declares the exception.
 **
 ** @param  <T>                  the type of the first input to the function.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <U>                  the type of the second input to the function.
 **                              <br>
 **                              Allowed object is <code>&lt;U&gt;</code>.
 ** @param  <R>                  the type of the result of the function.
 **                              <br>
 **                              Allowed object is <code>&lt;R&gt;</code>.
 ** @param  <E>                  the type of the exception thrown if the
 **                              evaluation of the function fails.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 **
 ** @see   CheckedFunction
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedBiFunction<T, U, R, E extends Exception> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Applies this function to the given arguments.
   **
   ** @param  lhs                the first function argument.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the second function argument.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    the function result.
   **                            <br>
   **                            Possisble object is <code>R</code>.
   **
   ** @throws E                  if the evaluation of the function fails.
   */
  R apply(final T lhs, final U rhs)
    throws E;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply
  /**
   ** Returns a composed function that first applies this function to its input,
   ** and then applies the <code>after</code> function to the result.
   ** <br>
   ** If evaluation of either function throws an exception, it is relayed to
   ** the caller of the composed function.
   **
   **  @param <V>                the type of output of the <code>after</code>
   **                            function, and of the composed function.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  after              the function to apply after this function is
   **                            applied.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   **
   ** @return                    a composed function that first applies this
   **                            function and then applies the
   **                            <code>after</code> function.
   **
   ** @throws E                    if the evaluation of the function fails.
   ** @throws NullPointerException if <code>after</code> is <code>null</code>.
   */
  default <V> CheckedBiFunction<T, U, V, E> andThen(final CheckedFunction<? super R, ? extends V, E> after)
    throws E {

    Objects.requireNonNull(after);
    return (T t, U u) -> after.apply(apply(t, u));
  }
}