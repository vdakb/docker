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

    File        :   CheckedBinaryOperator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedBinaryOperator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.util.Objects;
import java.util.Comparator;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedBinaryOperator
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** Represents an operation upon two operands of the same type, producing a
 ** result of the same type as the operands.
 ** <br>
 ** This is a specialization of {@link CheckedBiFunction} for the case where the
 ** operands and the result are all of the same type.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #apply(Object, Object)}.
 ** <p>
 ** Since Java 8, lambda expression to create a functional interface that throws
 ** an exception. To do this, you need to use a functional interface that
 ** declares the exception in its throws clause, or create your own functional
 ** interface that declares the exception.
 **
 ** @param  <T>                  the type of the operand and result of the
 **                              operator.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <E>                  the type of the exception thrown if the
 **                              evaluation of the function fails.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 **
 ** @see    CheckedBiFunction
 ** @see    CheckedUnaryOperator
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedBinaryOperator<T, E extends Exception> extends CheckedBiFunction<T,T,T, E> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   minBy
  /**
   ** Returns a <code>CheckedBinaryOperator</code> which returns the lesser of
   ** two elements according to the specified {@link Comparator}.
   **
   ** @param  <T>                the type of the input arguments of the
   **                            comparator.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            comparison of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  comparator         a {@link Comparator} for comparing the two
   **                            values.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    a <code>CheckedBinaryOperator</code> which
   **                            returns the lesser of its operands, according
   **                            to the supplied {@link Comparator}.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedBinaryOperator</code>.
   **
   ** @throws NullPointerException if <code>comparator</code> is <code>null</code>
   */
  public static <T, E extends Exception> CheckedBinaryOperator<T, E> minBy(final Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);
    return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   maxBy
  /**
   ** Returns a <code>CheckedBinaryOperator</code> which returns the greater of
   ** two elements according to the specified {@link Comparator}.
   **
   ** @param  <T>                the type of the input arguments of the
   **                            comparator.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   ** @param  <E>                the type of the exception thrown if the
   **                            comparison of the function fails.
   **                            <br>
   **                            Allowed object is <code>&lt;E&gt;</code>.
   ** @param  comparator         a {@link Comparator} for comparing the two
   **                            values.
   **                            <br>
   **                            Allowed object is {@link Comparator}.
   **
   ** @return                    a <code>CheckedBinaryOperator</code> which
   **                            returns the greater of its operands, according
   **                            to the supplied {@link Comparator}.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedBinaryOperator</code>.
   **
   ** @throws NullPointerException if <code>comparator</code> is <code>null</code>
   */
  public static <T, E extends Exception> CheckedBinaryOperator<T, E> maxBy(final Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);
    return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
  }
}