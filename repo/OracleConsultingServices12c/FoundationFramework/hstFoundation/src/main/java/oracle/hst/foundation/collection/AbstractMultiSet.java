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
    Subsystem   :   Common shared collection facilities

    File        :   AbstractMultiSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractMultiSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.AbstractSet;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractMultiSet
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** This class provides a skeletal implementation of the <code>MultiSet</code>
 ** interface to minimize the effort required to implement this interface.
 */
public abstract class AbstractMultiSet<E> extends    AbstractSet<E>
                                          implements MultiSet<E>
                                          ,          Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:606245047327819360")
  private static final long serialVersionUID = 1005357212879348057L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Default constructor.
   */
  public AbstractMultiSet() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superSet (MultiSet)
  /**
   ** Returns <code>true</code> if this multiset is a superset of the specified
   ** collection. That is, if all elements of the specified collection are also
   ** present in this multiset at least the same number of times.
   ** <p>
   ** This implementation first compares the sizes of this multiset and the
   ** specified collection by invoking the <code>size</code> method on each. If
   ** this multiset is bigger than the specified collection then each element of
   ** the specified collection is checked for presence in this multiset (for
   ** multiple equal elements, the quantity in this multiset has to be greater
   ** or equal). Otherwise, <code>false</code> is returned.
   **
   ** @param  collection         {@link Collection} for which is checked whether
   **                            this multiset is a superset of or not.
   **
   ** @return                    <code>true</code> if this multiset is a
   **                            superset, <code>false</code> otherwise.
   **
   ** @see    #subSet(Collection)
   */
  @Override
  public boolean superSet(final Collection<E> collection) {
    MultiSet<E> multiset;

    if (collection == this)
      return true;

    if (this.size() >= collection.size()) {
      if (collection instanceof MultiSet) {
        multiset = (MultiSet<E>)collection;
        if (this.setSize() < multiset.setSize())
          return false;
        else {
          for (Iterator<E> i = multiset.toSet().iterator(); i.hasNext(); ) {
            E element = i.next();
            if (this.quantity(element) < multiset.quantity(element))
              return false;
          }
        }
        return true;
      }
      else if (collection instanceof Set) {
        return (this.setSize() >= collection.size()) ? this.containsAll(collection) : false;
      }
      else {
        multiset = this.intersection(collection);
        return (collection.size() == multiset.size());
      }
    }
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subSet (MultiSet)
  /**
   ** Returns <code>true</code> if this multiset is a subset of the specified
   ** collection. That is, if all elements of this multiset are also present in
   ** the specified collection at least the same number of times.
   ** <p>
   ** This implementation first compares the sizes of this multiset and the
   ** specified collection by invoking the <code>size</code> method on each. If
   ** the specified collection is bigger than this multiset then each element of
   ** this multiset is checked for presence in the specified collection (for
   ** multiple equal elements, the quantity in the specified collection has to
   ** be greater or equal). Otherwise, <code>false</code> is returned.
   **
   ** @param  collection         {@link Collection} for which is checked whether
   **                            this multiset is a subset of or not.
   **
   ** @return                    <code>true</code> if this multiset is a subset,
   **                            <code>false</code> otherwise.
   **
   ** @see    #superSet(Collection)
   */
  @Override
  public boolean subSet(final Collection<E> collection) {
    Iterator<E> i;
    E           cursor;
    MultiSet<E> multiSet;

    if (collection == this)
      return true;

    if (this.size() <= collection.size()) {
      if (collection instanceof MultiSet) {
        multiSet = (MultiSet<E>)collection;
        if (this.setSize() > multiSet.setSize())
          return false;
        else {
          for (i = this.toSet().iterator(); i.hasNext(); ) {
            cursor = i.next();
            if (this.quantity(cursor) > multiSet.quantity(cursor)) {
              return false;
            }
          }
        }
        return true;
      }
      else if (collection instanceof Set) {
        if (this.setSize() == this.size()) {
          // "this" doesn't contain any identical elements,
          // i.e. "this" can be seen as a set
          multiSet = this.intersection(collection);
          return (this.size() == multiSet.size());
        }
        else
          return false;
      }
      else {
        multiSet = this.intersection(collection);
        return (this.size() == multiSet.size());
      }
    }
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   disjoint (MultiSet)
  /**
   ** Returns <code>true</code> if this multiset has no common element with the
   ** specified set.
   ** <p>
   ** This implementation checks whether the specified collection is an instance
   ** of <code>MultiSet</code> or not. If so, it iterates over the multiset that
   ** has fewer different elements. During iteration, only different elements
   ** are taken into account. If the specified collection is not an instance of
   ** <code>MultiSet</code>, it iterates over all elements of the specified
   ** collection. If a common element is found, that is, if an element is
   ** contained both in this multiset and in the specified collection,
   ** <code>false</code> is returned.
   **
   ** @param  collection         {@link Collection} to be checked for common
   **                            elements.
   **
   ** @return                    <code>true</code> if there are no common
   **                            elements, <code>false</code> otherwise.
   */
  @Override
  public boolean disjoint(final Collection<E> collection) {
    if (collection == this)
      return true;

    if (collection instanceof MultiSet) {
      if (this.setSize() <= ((MultiSet<E>)collection).setSize()) {
        for (Iterator<? extends E> i = this.toSet().iterator(); i.hasNext(); ) {
          if (collection.contains(i.next()))
            return false;
        }
        return true;
      }
      else {
        for (Iterator<E> i = ((MultiSet<E>)collection).toSet().iterator(); i.hasNext(); ) {
          if (this.contains(i.next()))
            return false;
        }
        return true;
      }
    }
    else {
      for (Iterator<E> i = collection.iterator(); i.hasNext(); ) {
        if (this.contains(i.next()))
          return false;
      }
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this multiset. To get the hash code of
   ** this multiset, new hash code values for every element of this multiset are
   ** calculated from a polynomial of 3rd order and finally summed up.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two multisets
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this multiset.
   */
  @Override
  public int hashCode() {
    int hash = -1;
    for (Iterator<E> i = this.toSet().iterator(); i.hasNext(); ) {
      final E   element  = i.next();
      final int hashCode = element.hashCode();
      hash += -1 + (3 + hashCode) * (7 + hashCode) * (11 + hashCode) * this.quantity(element);
    }
    return hash;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares the specified object with this multiset for equality. Returns
   ** <code>true</code> if the specified object is also a collection, the two
   ** sets have the same size, and every element of the specified set is
   ** contained in this set the same number of times.
   ** <p>
   ** If the specified object is not this multiset itself but another
   ** collection, this implementation first compares the sizes of this multiset
   ** and the specified collection by invoking the <code>size</code> method on
   ** each. If the sizes match, the sets are compared on a per-element basis.
   **
   ** @param  other              object to be compared for equality with this
   **                            multiset.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to this multiset, <code>false</code>
   **                            otherwise.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(final Object other) {
    if (other == this)
      return true;

    if (!(other instanceof Collection))
      return false;

    Collection<E> collection = (Collection<E>)other;
    if (this.size() != collection.size())
      return false;

    MultiSet<E>   multiSet;
    if (collection instanceof MultiSet) {
      multiSet = (MultiSet<E>)collection;
      if (multiSet.setSize() != this.setSize())
        return false;


      for (Iterator<E> i = this.toSet().iterator(); i.hasNext(); ) {
        E cursor = i.next();
        if (this.quantity(cursor) != multiSet.quantity(cursor)) {
          return false;
        }
      }
      return true;
    }
    else if (collection instanceof Set) {
      if (this.setSize() == this.size()) {
        // "this" doesn't contain any identical elements,
        // i.e. "this" can be seen as a set
        return this.containsAll(collection);
      }
      else {
        return false;
      }
    }
    else {
      multiSet = this.intersection(collection);
      return (this.size() == multiSet.size());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this multiset.
   ** <p>
   ** The string representation consists of a list of the set's elements in the
   ** order they are returned by its iterator, enclosed in curly brackets
   ** (<code>"{}"</code>). Adjacent elements are separated by the characters
   ** <code>", "</code> (comma and space). Elements are converted to strings as
   ** by <code>Object.toString()</code>.
   **
   ** @return                    a string representation of this multiset.
   */
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    output.append("{");
    for (Iterator<E> i = this.iterator(); i.hasNext(); ) {
      output.append(i.next().toString());
      if (i.hasNext())
        output.append(", ");
    }
    output.append("}");
    return output.toString();
  }
}