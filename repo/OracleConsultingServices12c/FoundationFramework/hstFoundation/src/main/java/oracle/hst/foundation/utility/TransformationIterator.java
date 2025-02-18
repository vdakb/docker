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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   TransformationIterator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TransformationIterator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Iterator;

public class TransformationIterator<E> implements Iterator<E> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Iterator<E>    delegate;
  private Transformer<E> transformer;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TransformationIterator</code> which use the specified
   ** {@link Iterator}.
   ** <br>
   ** The iterator will return each element in the underlying collection in the
   ** origin.
   **
   ** @param  delegate          an implementation of the {@link Iterator} API.
   */
  public TransformationIterator(final Iterator<E> delegate) {
    this(delegate, Transformer.NULL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TransformationIterator</code> which use the specified
   ** {@link Iterator} and the specified {@link Transformer}.
   ** <br>
   ** The iterator will return each element in the underlying collection as an
   ** transformation of the origin.
   **
   ** @param  delegate          an implementation of the {@link Iterator} API.
   ** @param  transformer       an implementation of the {@link Transformer}
   **                           API.
   */
  public TransformationIterator(final Iterator<E> delegate, final Transformer<E> transformer) {
    this.delegate    = delegate;
    this.transformer = transformer;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext (Iterator)
  /**
   ** Returns <code>true</code> if the iteration has more elements. (In other
   ** words, returns <code>true</code> if <code>next</code> would return an
   ** element rather than throwing an exception.)
   **
   ** @return                    <code>true</code> if the iterator has more
   **                            elements.
   */
  public boolean hasNext() {
    return this.delegate.hasNext();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next (Iterator)
  /**
   ** Returns the next transformed element in the iteration.
   ** <br>
   ** Calling this method repeatedly until the {@link #hasNext()} method returns
   ** false will return each transformed element in the underlying collection
   ** exactly once.
   **
   * @return                     the next element in the iteration.
   */
  public E next() {
    return transform(this.delegate.next());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Iterator)
  /**
   ** Removes from the underlying collection the last element returned by the
   ** iterator (optional operation).
   ** <br>
   ** This method can be called only once per call to <code>next</code>. The
   ** behavior of an iterator is unspecified if the underlying collection is
   ** modified while the iteration is in progress in any way other than by
   ** calling this method.
   **
   ** @throws UnsupportedOperationException if the <code>remove</code> operation
   **                                       is not supported by this Iterator.
   ** @throws IllegalStateException         if the <code>next</code> method has
   **                                       not yet been called, or the
   **                                       <code>remove</code> method has
   **                                       already been called after the last
   **                                       call to the <code>next</code>
   **                                       method.
   */
  public void remove() {
    this.delegate.remove();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   **
   ** @return                   the string representation of this instance.
   */
  public String toString() {
    return ClassUtility.shortName(this) + '(' + this.delegate + ')';
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  <E>               the required class type of the transformation.
   ** @param  origin            the value to transform.
   **
   ** @return                   the transformation of the specified
   **                           <code>origin</code>.
   */
   protected <E> E transform(final E origin) {
    return this.transformer.transform(origin);
  }
}