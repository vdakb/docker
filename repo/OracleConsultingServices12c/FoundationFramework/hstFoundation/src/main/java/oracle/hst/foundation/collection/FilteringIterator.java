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
    Subsystem   :   Common Shared Collection Utility

    File        :   FilteringIterator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FilteringIterator.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import oracle.hst.foundation.utility.ClassUtility;

////////////////////////////////////////////////////////////////////////////////
// class FilteringIterator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A ValueIterator that filters entries from its source iterator.
 ** <p>
 ** Subclasses must implement an accept method: public boolean accept(E); for
 ** which the accept method returns <code>true</code> are said to match the
 ** filter.
 ** <br>
 ** By default, this class iterates over entries that match its filter.
 **
 ** @see Filter
 */
public class FilteringIterator<E> implements Iterator<E> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Iterator<E> delegate;
  private Filter<E>   filter;

  private E           next;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilteringIterator</code> which use the specified
   ** {@link Iterator}.
   ** <br>
   ** The iterator will return each element from the underlying collection in
   ** the origin.
   **
   ** @param  delegate           the {@link Iterator} used by this filter.
   */
  public FilteringIterator(final Iterator<E> delegate) {
    this(delegate, Filter.NULL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilteringIterator</code> which use the specified
   ** {@link Iterator}.
   **
   ** <br>
   ** The iterator will only return such elements from the underlying
   ** collection which fulfill the filter condition implemented in the specified
   ** {@link Filter}.
   **
   ** @param  delegate           the {@link Iterator} used by this filter.
   ** @param  filter             the {@link Filter} applied on each element
   **                            before it will be returned by this
   **                            {@link Iterator}.
   */
  public FilteringIterator(final Iterator<E> delegate, final Filter<E> filter) {
    this.delegate = delegate;
    this.filter   = filter;
    this.next     = null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext (Iterator)
  /**
   ** Returns <code>true</code> if the iteration has more elements.
   ** <br>
   ** (In other words, returns <code>true</code> if <code>next</code> would
   ** return an element rather than throwing an exception.)
   **
   ** @return                    <code>true</code> if the iterator has more
   **                            elements.
   */
  @Override
  public boolean hasNext() {
    if (this.next == null)
      loadNext();

    return this.next != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   next (Iterator)
  /**
   ** Returns the next transformed element in the iteration.
   ** <br>
   ** Calling this method repeatedly until the {@link #hasNext()} method returns
   ** <code>false</code> will return each transformed element in the underlying
   ** collection exactly once.
   **
   ** @return                    the next element in the iteration.
   **
   ** @throws NoSuchElementException iteration has no more elements.
   */
  @Override
  public E next() {
    if (this.next == null)
      loadNext();
    if (this.next == null)
      throw new NoSuchElementException();

    E result = this.next;
    loadNext();
    return result;
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
   **                                       isnot supported by this Iterator.
   ** @throws IllegalStateException         if the <code>next</code> method has
   **                                       not yet been called, or the
   **                                       <code>remove</code> method has
   **                                       already been called after the last
   **                                       call to the <code>next</code>
   **                                       method.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
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
  // Method:   accept
  /**
   ** Returns <code>true</code> if the specified {@link Object} matches the
   ** requirements of the filter; returns <code>false</code> otherwise.
   **
   ** @param  test              the object to test if the filter will
   **                           accepting the instance.
   **
   ** @return                   whether the passed object instance was
   **                           accepted by the filter.
   */
  protected boolean accept(final E test) {
    return this.filter.accept(test);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   loadNext
  /**
   ** Moves to the next element in the nested iterator and applies the filter
   ** on the retrieved element.
   **
   ** @return                   the next element in the iteration which is
   **                           accepted by the nested {@link Filter}.
   */
  private void loadNext() {
    for (this.next = null; this.delegate.hasNext() && this.next == null;) {
      this.next = this.delegate.next();
      if (!accept(this.next))
        this.next = null;
    }
  }
}