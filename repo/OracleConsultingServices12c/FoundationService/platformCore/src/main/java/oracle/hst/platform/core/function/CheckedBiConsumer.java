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

    File        :   CheckedBiConsumer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedBiConsumer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedBiConsumer
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Represents a checked version of {@code BiConsumer} accepts two input
 ** arguments and returns no result. This is the two-arity specialization of
 ** {@link CheckedConsumer}. Unlike most other functional interfaces,
 ** <code>CheckedBiConsumer</code> is expected to operate via side-effects.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #accept(Object, Object)}.
 ** <p>
 ** Since Java 8, lambda expression to create a functional interface that throws
 ** an exception. To do this, you need to use a functional interface that
 ** declares the exception in its throws clause, or create your own functional
 ** interface that declares the exception.
 **
 ** @param  <T>                  the type of the first input argument to the
 **                              consumer.
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 ** @param  <U>                  the type of the second input argument to the
 **                              consumer.
 **                              <br>
 **                              Allowed object is <code>&lt;U&gt;</code>.
 ** @param  <E>                  the type of the exception thrown if the
 **                              evaluation of the operation fails.
 **                              <br>
 **                              Allowed object is <code>&lt;E&gt;</code>.
 **
 ** @see   CheckedConsumer
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedBiConsumer<T, U, E extends Exception> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accept
  /**
   ** Performs this operation on the given argument.
   **
   ** @param  lhs                the first input argument to the
   **                            operation.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   ** @param  rhs                the second input argument to the
   **                            operation.
   **                            <br>
   **                            Allowed object is <code>U</code>.
   **
   ** @throws E                  if the operation failed.
   */
  void accept(final T lhs, final U rhs)
    throws E;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   andThen
  /**
   ** Returns a composed <code>CheckedBiConsumer</code> that performs, in
   ** sequence, this operation followed by the <code>after</code> operation.
   ** <br>
   ** If performing either operation throws an exception, it is relayed to the
   ** caller of the composed operation.
   ** <br>
   ** If performing this operation throws an exception, the <code>after</code>
   ** operation will not be performed.
   **
   ** @param  after              the operation to perform after this operation.
   **                            <br>
   **                            Allowed object is
   **                            <code>CheckedBiConsumer</code>.
   **
   ** @return                    a composed <code>CheckedBiConsumer</code> that
   **                            performs in sequence this operation followed by
   **                            the <code>after</code> operation.
   **
   ** @throws E                    if the operation failed.
   ** @throws NullPointerException if <code>after</code> is <code>null</code>.
   */
  default CheckedBiConsumer<T, U, E> andThen(final CheckedBiConsumer<? super T, ? super U, E> after)
    throws E {

    Objects.requireNonNull(after);
    return (T t, U u) -> {
      accept(t, u);
      after.accept(t, u);
    };
  }
}