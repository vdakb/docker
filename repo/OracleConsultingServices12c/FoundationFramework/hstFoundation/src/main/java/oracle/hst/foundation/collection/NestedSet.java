/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2008. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Collection Utility

    File        :   NestedSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    NestedSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.Iterator;
import java.util.Set;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface NestedSet
// ~~~~~~~~~ ~~~~~~~~~
/**
 ** An extension of {@link Set} for representing nested sets of sets.
 ** The primary set elements must implement the <code>Set</code> interface.
 ** The <code>null</code> element is not allowed.
 **
 ** @see Set
 ** @see AbstractNestedSet
 */
public interface NestedSet<E> extends Set<E>
                              ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7314058622097686650")
  static final long serialVersionUID = 1L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator
  /**
   ** Returns an iterator over the elements in this nested set. The elements
   ** are returned in no particular order.
   **
   ** @return                    an iterator over the elements in this set of
   **                            sets.
   */
  Iterator<E> iterator();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified element to this set if it is not already present.
   **
   ** @param  element            element to be added to this set.
   **
   ** @return                    <code>true</code> if the set did not already
   **                            contain the specified element,
   **                            <code>false</code> otherwise.
   **
   ** @throws ClassCastException if the type of the specified element is
   **                            incompatible (<code>!(o instanceof Set)</code>).
   */
  boolean add(final E element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the specified element from this set if it is present.
   **
   ** @param  element            object to be removed from this set, if present.
   **
   ** @return                    <code>true</code> if the set did contain the
   **                            specified element, <code>false</code>
   **                            otherwise.
   **
   ** @throws ClassCastException if the type of the specified element is
   **                            incompatible (<code>!(o instanceof Set)</code>).
   */
  boolean remove(final Object element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Returns <code>true</code> if this set contains the specified element.
   **
   ** @param  element            element whose presence in this set is to be
   **                            tested.
   **
   ** @return                    <code>true</code> if this set contains the
   **                            specified element, <code>false</code>
   **                            otherwise.
   **
   ** @throws ClassCastException if the type of the specified element is
   **                            incompatible (<code>!(o instanceof Set)</code>).
   */
  boolean contains(final Object element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAtom
  /**
   ** Returns <code>true</code> if this nested set contains a set in which the
   ** specified element is present.
   **
   ** @param  element            the element whose presence in any elementary
   **                            set within this nested set is tested for.
   **
   ** @return                    <code>true</code> if any set within this nested
   **                            set contains the specified element,
   **                            <code>false</code> otherwise.
   */
  boolean containsAtom(final E element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containingSets
  /**
   ** Returns a set containing the elementary sets from within this nested set
   ** that contain the specified element. If there are no sets containing the
   ** specified element, an empty set is returned.
   **
   ** @param  element            the element that the returned sets have to
   **                            contain.
   **
   ** @return                    the elementary sets from this nested set that
   **                            contain the specified element.
   */
  NestedSet<? extends E> containingSets(final E element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toSet
  /**
   ** Returns the 'flattened' version of this nested set, in which each basic
   ** element of this nested set is present exactly once.
   **
   ** @return                    the 'flattened' version (simple set) of this
   **                            nested set.
   */
  Set<? extends E> toSet();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMultiSet
  /**
   ** Returns the 'flattened' multiset version of this nested set, containing
   ** the same elements as in all sets of this nested set.
   **
   ** @return                    the 'flattened' {@link MultiSet} version of
   **                            this nested set.
   */
  MultiSet<? extends E> toMultiSet();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superSets
  /**
   ** Returns a new set containing the supersets of the specified set.
   ** <p>
   ** If the specified set is empty, a copy of this nested set is returned. If
   ** no supersets exist in this set, an empty nested set is returned.
   **
   ** @param  set                the set that the returned sets have to be
   **                            supersets of.
   **
   ** @return                    the elementary sets from this nested set that
   **                            contain the specified set.
   */
  NestedSet<? extends E> superSets(final Set<? extends E> set);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subSets
  /**
   ** Returns a new set containing the subsets of the specified set.
   ** <p>
   ** If the specified set is empty or no supersets exist in this set, an empty
   ** nested set is returned.
   **
   ** @param  set                the set that the returned sets have to be
   **                            subsets of.
   **
   ** @return                    the elementary sets from this nested set that
   **                            occur in the specified set.
   */
  NestedSet<E> subSets(final Set<E> set);
}