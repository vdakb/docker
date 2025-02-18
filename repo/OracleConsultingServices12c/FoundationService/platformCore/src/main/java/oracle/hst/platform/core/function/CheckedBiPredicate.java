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

    File        :   CheckedBiPredicate.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedBiPredicate.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedBiPredicate
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** Represents a checked version of {@code BiPredicate} (boolean-valued 
 ** unction) of two arguments.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #test(Object, Object)}.
 ** <p>
 ** Since Java 8, lambda expression to create a functional interface that throws
 ** an exception. To do this, you need to use a functional interface that
 ** declares the exception in its throws clause, or create your own functional
 ** interface that declares the exception.
 **
 ** @param  <T>                  the type of the first input argument to the
 **                              predicate.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <U>                  the type of the second input argument to the
 **                              predicate.
 **                              <br>
 **                              Allowed object is <code>&lt;U&gt;</code>.
 ** @param  <E>                  the type of the exception thrown if the
 **                              evaluation of the predicate fails.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 **
 ** @see    CheckedPredicate
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedBiPredicate<T, U, E extends Exception> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   test
  /**
   ** Evaluates this predicate on the given argument.
   **
   ** @param  lhs                the type of the first input argument to the
   **                            predicate.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the type of the second input argument to the
   **                            predicate.
   **                            <br>
   **                            Allowed object is <code>U</code>.
   **
   ** @return                    <code>true</code> if the input arguments
   **                            matches the predicate; otherwise <code>false</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws E                  if the operation failed.
   */
  boolean test(final T lhs, final U rhs)
    throws E;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Returns a composed predicate that represents a short-circuiting logical
   ** AND of this predicate and another.
   ** <br>
   ** When evaluating the composed predicate, if this predicate is
   ** <code>false</code>, then the <code>other</code> predicate is not
   ** evaluated.
   ** <p>
   ** Any exceptions thrown during evaluation of either predicate are relayed
   ** to the caller; if evaluation of this predicate throws an exception, the
   ** <code>other</code> predicate will not be evaluated.
   **
   ** @param  other              a predicate that will be logically-ANDed with
   **                            this predicate.
   **                            <br>
   **                            Allowed object is
   **                            <code>CheckedBiPredicate</code>.
   **
   ** @return                    a composed predicate that represents the
   **                            short-circuiting logical AND of this predicate
   **                            and the <code>other</code> predicate.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedBiPredicate</code>.
   **
   ** @throws E                    if the operation failed.
   ** @throws NullPointerException if <code>other</code> is <code>null</code>.
   */
  default CheckedBiPredicate<T, U, E> and(final CheckedBiPredicate<? super T, ? super U, E> other)
    throws E {

    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) && other.test(t, u);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Returns a composed predicate that represents a short-circuiting logical OR
   ** of this predicate and another.
   ** <br>
   ** When evaluating the composed predicate, if this predicate is
   ** <code>true</code>, then the <code>other</code> predicate is not evaluated.
   ** <p>
   ** Any exceptions thrown during evaluation of either predicate are relayed to
   ** the caller; if evaluation of this predicate throws an exception, the
   ** <code>other</code> predicate will not be evaluated.
   **
   ** @param  other              a predicate that will be logically-ORed with with
   **                            this predicate.
   **                            <br>
   **                            Allowed object is
   **                            <code>CheckedBiPredicate</code>.
   **
   ** @return                    a composed predicate that represents the
   **                            short-circuiting logical OR of this predicate
   **                            and the <code>other</code> predicate.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedBiPredicate</code>.
   **
   ** @throws E                    if the operation failed.
   ** @throws NullPointerException if <code>other</code> is <code>null</code>.
   */
  default CheckedBiPredicate<T, U, E> or(final CheckedBiPredicate<? super T, ? super U, E> other)
    throws E {

    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) || other.test(t, u);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   negate
  /**
   ** Returns a predicate that represents the logical negation of this
   ** predicate.
   **
   ** @return                    a predicate that represents the logical
   **                            negation of this predicate.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedBiPredicate</code>.
   **
   ** @throws E                  if the operation failed.
   */
  default CheckedBiPredicate<T, U, E> negate()
    throws E {

    return (T t, U u) -> !test(t, u);
  }
}