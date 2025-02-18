/*
                         Copyright © 2023 Red.Security

    Licensed under the MIT License (the "License"); you may not use this file
    except in compliance with the License. You may obtain a copy of the License
    at

                      https://opensource.org/licenses/MIT

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to
    deal in the Software without restriction, including without limitation the
    rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
    sell copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
    IN THE SOFTWARE.

    ----------------------------------------------------------------------------

    System      :   Platform Service Extension
    Subsystem   :   Common Shared Utility

    File        :   Tupel.java

    Compiler    :   Java Developer Kit 8

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the interface
                    Tupel.


    Revisions   Date        Editor                    Comment
    -----------+-----------+-------------------------+--------------------------
    1.0.0.0     2023-28-06  dieter.steding@icloud.com First release version
*/

package redsecurity.platform.core.entity;

import java.util.List;

import java.io.Serializable;

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
 ** @author  dieter.steding@icloud.com
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
   */
  List<?> element();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Returns whether an object <code>lhs</code> is "equal to" an object
   ** <code>rhs</code>.
   **
   ** @param  <L>                the type of the first value to compare.
   ** @param  <R>                the type of the second value to compare.
   ** @param  lhs                the left-hand-side object to compare.
   ** @param  rhs                the right-hand-side object to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; otherwise
   **                            <code>false</code>.
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