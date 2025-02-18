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

    File        :   MultiSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    MultiSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.Collection;
import java.util.Set;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface MultiSet
// ~~~~~~~~~ ~~~~~~~~
/**
 ** An extension of java.util.Collection for representing mathematical multisets.
 **
 ** @see Collection
 ** @see AbstractMultiSet
 ** @see MultiHashSet
 */
public interface MultiSet<E> extends Collection<E>
                             ,       Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3050444094357410970")
  static final long serialVersionUID = 1L;

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified element <code>quantity</code> of times to this
   ** multiset. If <code>quantity</code> is negative or 0, <code>false</code>
   ** is returned.
   **
   ** @param  element            element to be added to this set.
   ** @param  quantity           quantity of elements to add.
   **
   ** @return                    <code>true</code> if <code>quantity</code> is
   **                            greater than 0, <code>false</code> otherwise
   */
  boolean add(final E element, final int quantity);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the specified element <code>quantity</code> of times from this
   ** multiset if possible. If <code>quantity</code> is negative or 0,
   ** <code>false</code> is returned.
   **
   ** @param  element            object to be removed from this multiset.
   ** @param  quantity           quantity of elements to remove.
   **
   ** @return                    <code>true</code> if the multiset got altered,
   **                            <code>false</code> otherwise.
   */
  boolean remove(final E element, final int quantity);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toSet
  /**
   ** Returns a 'flattened' version of this multiset in which every element of
   ** this multiset is present exactly once.
   **
   ** @return                    the 'flattened' version of this multiset.
   */
  Set<E> toSet();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSize
  /**
   ** Returns the size of a 'flattened' version of this multiset in which every
   ** element of this multiset is present exactly once.
   **
   ** @return                    the size of the 'flattened' version of this
   **                            multiset.
   */
  int setSize();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   quantity
  /**
   ** Adjusts the number of times the specified element is present in this
   ** multiset to be the specified value.
   **
   ** @param  element            element whose quantity gets set.
   ** @param  quantity           quantity of the specified element to be set.
   **
   ** @return                    <code>true</code> if this multiset has been
   **                            modified, <code>false</code> otherwise.
   **
   ** @see    #quantity
   */
  boolean quantity(final E element, final int quantity);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   quantity
  /**
   ** Returns the number of times the specified element is present in this
   ** multiset.
   **
   ** @param  element            element whose quantity is returned.
   **
   ** @return                    quantity of the specified element, 0 if it is
   **                            not present.
   **
   ** @see    #quantity
   */
  int quantity(final E element);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superSet
  /**
   ** Returns <code>true</code> if this multiset is a superset of the specified
   ** collection. That is, if all elements of the specified collection are also
   ** present in this multiset at least the same number of times.
   **
   ** @param  collection         collection for which is checked whether this
   **                            multiset is a superset of or not.
   **
   ** @return                    <code>true</code> if this multiset is a
   **                            superset, <code>false</code> otherwise.
   */
  boolean superSet(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subSet
  /**
   ** Returns <code>true</code> if this multiset is a subset of the
   ** specified collection. That is, if all elements of this multiset are also
   ** present in the specified collection at least the same number of times.
   **
   ** @param  collection         collection for which is checked whether this
   **                            multiset is a subset of or not.
   **
   ** @return                    <code>true</code> if this multiset is a subset,
   **                            <code>false</code> otherwise.
   */
  boolean subSet(Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjoint
  /**
   ** Returns <code>true</code> if the specified collection has no common
   ** elements with this multiset.
   **
   ** @param  collection         collection to be checked for common elements.
   **
   ** @return                    <code>true</code> if there are no common
   **                            elements, <code>false</code> otherwise.
   */
  boolean disjoint(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union
  /**
   ** Returns the sum with the specified collection. This is a new
   ** <code>MultiSet</code> containing all elements that are present in this
   ** multiset or in the specified collection. The quantities of equal elements
   ** get added up.
   **
   ** @param  collection         collection to be united with.
   **
   ** @return                    the union with the specified collection.
   */
  MultiSet<E> sum(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union
  /**
   ** Returns the union with the specified collection. This is a new
   ** <code>MultiSet</code> containing all elements that are present in this
   ** multiset or in the specified collection. For equal elements, the resulting
   ** quantity is the maximum of the two given quantities.
   **
   ** @param  collection         collection to be united with.
   **
   ** @return                    the union with the specified collection.
   */
  MultiSet<E> union(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intersection
  /**
   ** Returns the intersection with the specified collection. This is a new
   ** <code>MultiSet</code> containing all elements that are present in this
   ** multiset as well as in the specified collection. For equal elements,
   ** the resulting quantity is the minimum of the two given quantities.
   **
   ** @param  collection         collection to be intersected with.
   **
   ** @return                    the intersection with the specified collection.
   */
  MultiSet<E> intersection(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   difference
  /**
   ** Returns the asymmetric difference between this multiset and the
   ** specified collection. This is a new <code>MultiSet</code> containing all
   ** elements that are present in this multiset but not in the specified
   ** collection. The quantities of equal elements get subtracted.
   **
   ** @param  collection         collection from which the difference is
   **                            calculated.
   **
   ** @return                    the difference with the specified collection.
   */
  MultiSet<E> difference(final Collection<E> collection);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   symmetricDifference
  /**
   ** Returns the symmetric difference between this multiset and the specified
   ** collection. This is a new <code>MultiSet</code> containing all elements
   ** that are present either in this multiset or in the specified collection
   ** but not in both. The quantities of equal elements get subtracted from each
   ** other (maximum minus minimum).
   **
   ** @param  collection         collection from which the symmetric difference
   **                            is calculated
   **
   ** @return                    the symmetric difference with the specified
   **                            collection.
   */
  MultiSet<E> symmetricDifference(final Collection<E> collection);
}