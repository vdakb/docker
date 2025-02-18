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

    File        :   NestedHashSet.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    NestedHashSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.lang.reflect.Array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

////////////////////////////////////////////////////////////////////////////////
// class NestedHashSet
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This class implements the {@link NestedSet} interface, backed by a hash
 ** table (specifically, a {@link HashSet} instance). It makes no guarantees as
 ** to the iteration order of the set; in particular, it does not guarantee that
 ** the order will remain constant over time. An instance of
 ** {@link MultiHashSet} is used for permanently maintaining a 'flat' version of
 ** the nested set (a simple set containing all the elements from the
 ** elementary sets once).
 ** <p>
 ** Note that this implementation is not synchronized. If multiple threads
 ** access a set concurrently, and at least one of the threads modifies the set,
 ** it must be synchronized externally. This is typically accomplished by
 ** synchronizing on some object that naturally encapsulates the set. If no such
 ** object exists, the set should be "wrapped" using the
 ** <code>AbstractSetFactory.synchronizedNestedSet</code> method. This is best
 ** done at creation time, to prevent accidental unsynchronized access to the
 ** <code>NestedHashSet</code> instance:
 ** <pre>
 **  NestedSet set = AbstractSetFactory.synchronizedNestedSet(new NestedHashSet(...));
 ** </pre>
 **
 ** @see Set
 ** @see AbstractNestedSet
 ** @see HashSet
 */
public class NestedHashSet<E> extends AbstractNestedSet<E> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4174784215621423593")
  static final long serialVersionUID = 1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The backing instance of <code>HashSet</code> where the elements of this
   ** set are stored.
   */
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private HashSet<E>      delegate;

  /**
   ** Acts as a cache for the 'flattened' multiset version of this nested set
   ** out of performance considerations. The contents of flatVersion are always
   ** up-to-date since they get updated by all destructive methods in this
   ** class.
   */
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private MultiHashSet<E> flatVersion;

  /**
   ** Acts as a cache for the hash code value of this set out of performance
   ** considerations.
   ** Whenever this set is changed, <code>storedHashCode</code> is set to 0 and
   ** gets updated as soon as the <code>hashCode()</code> method is called.
   */
  private int             storedHashCode = 0;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Element
  // ~~~~~ ~~~~~~~
  /**
   ** Class for the array elements representing the flatVersion elements
   ** Used in fastSupersets method
   */
  private static class Element<E> implements Comparable<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private E   contents;
    private int quantity;

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compareTo (Comparable)
    /**
     ** Compares this object with the specified object for order.
     ** <p>
     ** Returns a negative integer, zero, or a positive integer as this object
     ** is less than, equal to, or greater than the specified object.
     **
     ** @param  other            the Object to be compared.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as this object is less than, equal to, or
     **                          greater than the specified object.
     **
     ** @throws ClassCastException if the specified object's type is not
     **                            an instance of <code>Accessor</code>.
     */
    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(final Object other) {
      final Element<E> that = (Element<E>)other;
      return this.quantity - that.quantity;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class NestedSetIterator
  // ~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** A 'wrapper' iterator class that uses <code>HashSet.iterator()</code> while
   ** accounting for the additional storedHashCode attribute.
   */
  private class NestedSetIterator implements Iterator<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Iterator<E> delegate;
    private E                 cursor;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Default constructor.
     */
    public NestedSetIterator() {
      this.delegate = NestedHashSet.this.toSet().iterator();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   hasNext (Iterator)
    @Override
    public boolean hasNext() {
      return this.delegate.hasNext();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   next (Iterator)
    @Override
    @SuppressWarnings({"unchecked", "cast"})
    public E next() {
      this.cursor = (E)this.delegate.next();
      return this.cursor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   remove (Iterator)
    @Override
    @SuppressWarnings("unchecked")
    public void remove() {
      storedHashCode = 0;
      this.delegate.remove();
      flatVersion.removeAll((Collection<E>)this.cursor);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty nested set; the backing <code>HashSet</code>
   ** instance as well as the <code>HashMap flatVersion</code> have default
   ** initial capacity (16) and load factor (0.75).
   */
  public NestedHashSet() {
    this.delegate    = new HashSet<E>();
    this.flatVersion = new MultiHashSet<E>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty nested set; the backing {@link HashSet} instance
   ** has the specified initial capacity and default load factor, which is 0.75.
   **
   ** @param  initialCapacity    the initial capacity of the hash set.
   **
   ** @throws IllegalArgumentException if the initial capacity is
   **                                  less than zero.
   */
  public NestedHashSet(final int initialCapacity) {
    this.delegate    = new HashSet<E>(initialCapacity);
    this.flatVersion = new MultiHashSet<E>(initialCapacity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty nested set; the backing {@link HashSet} instance
   ** has the specified initial capacity and the specified load factor.
   **
   ** @param  initialCapacity   the initial capacity of the hash set.
   ** @param  loadFactor        the load factor of the hash set.
   **
   ** @throws IllegalArgumentException if the initial capacity is less than
   **                                  zero, or if the load factor is
   **                                  nonpositive.
   */
  public NestedHashSet(final int initialCapacity, final float loadFactor) {
    this.delegate    = new HashSet<E>(initialCapacity, loadFactor);
    this.flatVersion = new MultiHashSet<E>(initialCapacity, loadFactor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new nested set set containing the elements in the specified
   ** collection. The {@link HashSet} is created with default load factor (0.75)
   ** and an initial capacity sufficient to contain the elements in the
   ** specified collection.
   **
   ** @param  collection         the {@link Collection} whose elements are to be
   **                            placed into this set.
   **
   ** @throws NullPointerException if the specified collection is
   **                              null.
   */
  public NestedHashSet(final Collection<E> collection) {
    this.delegate    = new HashSet<E>(collection);
    this.flatVersion = new MultiHashSet<E>(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (Cloneable)
  /**
   ** Returns a shallow copy of this <code>NestedSet</code> instance: the
   ** elements themselves are not cloned.
   **
   ** @return                    a shallow copy of this nested set.
   */
  @Override
  public Object clone() {
    NestedHashSet<E> copy = new NestedHashSet<E>(this);
    copy.storedHashCode = this.storedHashCode;
    return copy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Set)
  /**
   ** Returns an iterator over the elements in this nested set. The elements
   ** are returned in no particular order.
   **
   ** @return                    an iterator over the elements in this nested
   **                            set.
   */
  @Override
  public Iterator<E> iterator() {
    return new NestedSetIterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Set)
  /**
   ** Returns the number of elements (contained sets) in this nested set
   ** (its cardinality).
   **
   ** @return                    the number of elements in this nested set (its
   **                            cardinality).
   */
  @Override
  public int size() {
    return this.delegate.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (NestedSet)
  /**
   ** Adds the specified element (Set) to this nested set if it is not already
   ** present.
   ** <p>
   ** If the set gets altered, this implementation sets
   ** <code>storedHashCode</code> to 0 (representing an unavailable hash code
   ** value), which forces <code>hashCode()</code> to recalculate the actual
   ** hash code value.
   **
   ** @param  element            element to be added to this nested set.
   **
   ** @return                    <code>true</code> if the nested set did not
   **                            already contain the specified element,
   **                            <code>false</code> otherwise.
   **
   ** @throws ClassCastException if the type of the specified element is
   **                            incompatible (<code>!(o instanceof Set)</code>).
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean add(final E element) {
    if (element instanceof Set) {
      if (this.delegate.add(element)) {
        // Add the set elements to flatVersion
        this.flatVersion.addAll((Set<E>)element);
        this.storedHashCode = 0;
        return true;
      }
      else
        return false;
    }
    else
      throw (new ClassCastException());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (NestedSet)
  /**
   ** Removes the specified element (Set) from this nested set if it is
   ** present.
   ** <p>
   ** If the set gets altered, this implementation sets
   ** <code>storedHashCode</code> to 0 (representing an unavailable hash code
   ** value), which forces <code>hashCode()</code> to recalculate the actual
   ** hash code value.
   **
   ** @param  element            object to be removed from this nested set, if
   **                            present.
   **
   ** @return                    <code>true</code> if the nested set contained
   **                            the specified element, <code>false</code>
   **                            otherwise.
   **
   ** @throws ClassCastException if the type of the specified element
   **                            is incompatible
   **                            (<code>!(o instanceof Set)</code>).
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean remove(final Object element) {
    if (element instanceof Set) {
      if (this.delegate.remove(element)) {
        flatVersion.removeAll((Collection<E>)element);
        this.storedHashCode = 0;
        return true;
      }
      else
        return false;
    }
    else
      throw (new ClassCastException());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains (NestedSet)
  /**
   ** Returns <code>true</code> if this nested set contains the specified
   ** element (Set).
   **
   ** @param  element            element whose presence in this nested set is
   **                            to be tested.
   **
   ** @return                    <code>true</code> if this nested set contains
   **                            the specified element, <code>false</code>
   **                            otherwise.
   **
   ** @throws ClassCastException if the type of the specified element is
   **                            incompatible
   **                            (<code>!(element instanceof Set)</code>).
   */
  @Override
  public boolean contains(final Object element) {
    if (element instanceof Set)
      return this.delegate.contains(element);
    else
      throw (new ClassCastException());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsAtom (NestedSet)
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
  @Override
  public boolean containsAtom(final E element) {
    return flatVersion.contains(element);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containingSets (NestedSet)
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
  @Override
  @SuppressWarnings("unchecked")
  public NestedSet<E> containingSets(final E element) {
    NestedSet<E> containSets = new NestedHashSet<E>();
    for (Iterator<E> i = this.delegate.iterator(); i.hasNext(); ) {
      final Set<E> cursor = (Set<E>)i.next();
      if (cursor.contains(element)) {
        containSets.addAll(cursor);
      }
    }
    return containSets;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toSet (NestedSet)
  /**
   ** Returns the 'flattened' version of this nested set, in which each basic
   ** element of this nested set is present exactly once.
   **
   ** @return                    the 'flattened' version (simple set) of this
   **                            nested set.
   */
  @Override
  public Set<E> toSet() {
    return this.flatVersion.toSet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMultiSet (NestedSet)
  /**
   ** Returns the 'flattened' multiset version of this nested set, containing
   ** the same elements as in all sets of this nested set.
   **
   ** @return                    the 'flattened' {@link MultiSet} version of
   **                            this nested set.
   */
  @Override
  @SuppressWarnings("unchecked")
  public MultiSet<E> toMultiSet() {
    return (MultiSet<E>)flatVersion.clone();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superSets (NestedSet)
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
  @Override
  @SuppressWarnings("unchecked")
  public NestedSet<? extends E> superSets(final Set<? extends E> set) {
    NestedSet<E> resultSet = new NestedHashSet<E>();
     if (set.isEmpty())
      return resultSet;

    MultiHashSet<E> multiSet = (set instanceof MultiHashSet) ? (MultiHashSet<E>)set : new MultiHashSet<E>(set);
    for (Iterator<E> i = this.delegate.iterator(); i.hasNext(); ) {
      final Set<E> cursor = (Set<E>)i.next();
      if (multiSet.superSet(cursor))
        resultSet.addAll(cursor);
    }
    return resultSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subSets (NestedSet)
  /**
   ** Returns a new set containing the subsets of the specified set.
   ** <p>
   ** If the specified set is empty or no supersets exist in this set, an empty
   ** nested set is returned.
   **
   ** @param  set                the set that the returned sets have to be
   **                            subsets of.
   ** @return                    the elementary sets from this nested set that
   **                            occur in the specified set.
   */
  @Override
  @SuppressWarnings("unchecked")
  public NestedSet<E> subSets(final Set<E> set) {
    NestedSet<E> resultingSet = new NestedHashSet<E>();
    if (set.isEmpty())
      return (NestedSet<E>)this.clone();

    MultiHashSet<E> multiSet = (set instanceof MultiHashSet) ? (MultiHashSet<E>)set : new MultiHashSet<E>(set);
    if (!multiSet.subSet(this.flatVersion.toSet()))
      return resultingSet;

    for (Iterator<E> i = this.delegate.iterator(); i.hasNext(); ) {
      final Set<E> cursor = (Set<E>)i.next();
      if (multiSet.subSet(cursor))
        resultingSet.addAll(cursor);
    }
    return resultingSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this nested set. To get the hash code of
   ** this nested set, new hash code values for every element of this nested set
   ** are calculated from a polynomial of 3rd order and finally summed up.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two sets
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of Object.hashCode.
   ** <p>
   ** This implementation first checks whether a cached hash code value is
   ** available. If not (i.e. <code>storedHashCode</code> is zero), the hash
   ** code gets calculated using <code>hashCode()</code> of the super class.
   **
   ** @return                    the hash code value for this nested set.
   */
  @Override
  public int hashCode() {
    if (this.storedHashCode == 0) {
      this.storedHashCode = super.hashCode();
    }
    return this.storedHashCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares the specified object with this nested set for equality.
   ** Returns <code>true</code> if the specified object is also a set, the two
   ** sets have the same size, and every element of the specified set is
   ** contained in this set.
   ** <p>
   ** This implementation first checks if the given object is a
   ** <code>NestedSet</code>. If so, the hash code values of this nested set
   ** and the specified <code>HashSetOfSets</code> are compared. Since the hash
   ** code values are being cached, this represents a quick solution if the sets
   ** aren't equal. However, if the hash code values are equal, it cannot be
   ** assumed that the sets themselves are equal, since different sets can have
   ** the same hash code value. In this case, the result of the super method
   ** <code>equals()</code> is returned.
   **
   ** @param  other              object to be compared for equality with this
   **                            nested set.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to this nested set, <code>false</code>
   **                            otherwise.
   */
  @Override
  @SuppressWarnings("cast")
  public boolean equals(final Object other) {
    if (other instanceof NestedSet) {
      if (this.hashCode() != ((NestedSet)other).hashCode()) {
        return false;
      }
    }
    return super.equals(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (overridden)
  /**
   ** Returns <code>true</code> if this nested set contains no elements.
   **
   ** @return                    <code>true</code> if this nested set contains
   **                            no elements, <code>false</code> otherwise.
   */
  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (overridden)
  /**
   ** Removes all elements from this nested set.
   */
  @Override
  public void clear() {
    this.storedHashCode = 0;
    this.delegate.clear();
    this.flatVersion.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sortedArraySupersets
  /**
   ** Returns a new set containing the supersets of the specified set. If the
   ** specified set is empty, a copy of this nested set is returned. If no
   ** supersets exist in this set, an empty nested set is returned.
   ** <p>
   ** This implementation is faster for large numbers of large sets. An array
   ** containing the elements of the specified set, sorted by their frequency
   ** of occurrence in this nested set, is used to make the superset relation
   ** checks faster.
   ** <p>
   ** Note that this implementation requires the elements of this nested set
   ** to have an efficient <code>contains()</code> method (constant complexity)
   ** to achieve the improved performance.
   **
   ** @param  set                the set that the returned sets have to be
   **                            supersets of.
   **
   ** @return                    the elementary sets from this nested set that
   **                            contain the specified set.
   */
  @SuppressWarnings("unchecked")
  public NestedSet<E> sortedArraySupersets(final Set<E> set) {
    if (set.isEmpty())
      return (NestedSet<E>)this.clone();

    NestedSet<E> resultSet = new NestedHashSet<E>();
    if (!((this.flatVersion.toSet().size() >= set.size()) && (this.flatVersion.toSet().containsAll(set))))
      return resultSet;

    int          k = 0;
    final Element<E>[] e = (Element<E>[])Array.newInstance(Element.class,set.size());
    final Iterator<E>  i = set.iterator();
    while (i.hasNext()) {
      final Element<E> elem = new Element<E>();
      elem.contents = i.next();
      elem.quantity = this.flatVersion.quantity(elem.contents);
      if (elem.quantity > 0) {
        e[k++] = elem;
        k++;
      }
    }
    Arrays.sort(e);

    final Iterator<E> j = this.delegate.iterator();
    while (j.hasNext()) {
      final Set<E> cursor = (Set<E>)j.next();
      if (cursor.size() >= set.size()) {
        k = 0;
        while ((k < e.length) && (cursor.contains(e[k].contents))) {
          k++;
        }
        if (k == e.length)
          resultSet.addAll(cursor);
      }
    }
    return resultSet;
  }
}