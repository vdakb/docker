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

    File        :   Tupel.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Tupel.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.core.entity;

import java.io.Serializable;

import java.util.List;

////////////////////////////////////////////////////////////////////////////////
// interface Tupel
// ~~~~~~~~~ ~~~~~
/**
 ** Base interface for all tuple types.
 ** <p>
 ** An ordered list of elements of a fixed size, where each element can have a
 ** different type.
 ** <p>
 ** All implementations must be final, immutable and thread-safe.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Tupel {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of elements held by this tuple.
   ** <p>
   ** Each tuple type has a fixed size, returned by this method.
   ** <br>
   ** For example, {@link Pair} returns <code>2</code>.
   **
   ** @return                    the size of the tuple.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  int size();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Returns the elements from this tuple as a collection.
   ** <p>
   ** The list contains each element in the tuple in order.
   **
   ** @return                    the elements as a collection.
   **                            <br>
   **                            Possible object is {@link List}.
   */
  List<?> element();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Indicates whether an object <code>lhs</code> is "equal to" an object
   ** <code>rhs</code>.
   **
   ** @param  <L>                the type of the first value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;L&gt;</code>.
   ** @param  <R>                the type of the second value to compare.
   **                            <br>
   **                            Allowed object is <code>&lt;R&gt;</code>.
   ** @param  lhs                the left-hand-side object to compare.
   **                            <br>
   **                            Allowed object is <code>L</code>.
   ** @param  rhs                the right-hand-side object to compare.
   **                            <br>
   **                            Allowed object is <code>R</code>.
   **
   ** @return                    <code>true</code> if the objects are the same;
   **                            <code>false</code> otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  static <L extends Serializable, R extends Serializable> boolean equals(final L lhs, final R rhs) {
    if (lhs == null) {
      return rhs == null;
    }
    else if (rhs == null) {
      return false;
    }
    else {
      return lhs.equals(rhs);
    }
  }
}