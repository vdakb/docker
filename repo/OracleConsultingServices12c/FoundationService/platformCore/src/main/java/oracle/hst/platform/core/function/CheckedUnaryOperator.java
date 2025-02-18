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

    File        :   CheckedUnaryOperator.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    CheckedUnaryOperator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.function;

////////////////////////////////////////////////////////////////////////////////
// interface CheckedUnaryOperator
// ~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Represents checked version of {@code Predicat} as an operation on a single
 ** operand that produces a result of the same type as its operand.
 ** <br>
 ** This is a specialization of {@link CheckedFunction} for the case where the
 ** operand and result are of the same type.
 ** <p>
 ** This is a <a href="package-summary.html">functional interface</a> whose
 ** functional method is {@link #apply(Object)}.
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
 ** @see    CheckedFunction
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@FunctionalInterface
public interface CheckedUnaryOperator<T, E extends Exception> extends CheckedFunction<T, T, E> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Returns a unary operator that always returns its input argument.
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
   ** @return                    a unary operator that always returns its input
   **                            argument.
   **                            <br>
   **                            Possible object is
   **                            <code>CheckedUnaryOperator</code>.
   */
  static <T, E extends Exception> CheckedUnaryOperator<T, E> identity() {
    return t -> t;
  }
}