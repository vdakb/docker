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

    File        :   MultiHashSet.java

    Compiler    :   Oracle JDeveloper 10g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    MultiHashSet.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

////////////////////////////////////////////////////////////////////////////////
// class MultiHashSet
// ~~~~~ ~~~~~~~~~~~~
/**
 ** This class implements the <code>MultiSet</code> interface, backed by a hash
 ** map (specifically, a <code>Map</code> instance).
 ** <p>
 ** Equal elements are returned in successive order, whereas different elements
 ** have no specific iteration order; in particular, it is not guaranteed that
 ** the element order will remain constant over time.
 ** <br>
 ** Multiple copies of the same element only require as much memory as a single
 ** instance of it. This class permits the <code>null</code> element.
 ** <p>
 ** Note that this implementation is not synchronized. If multiple threads
 ** access a set concurrently, and at least one of the threads modifies the set,
 ** it must be synchronized externally. This is typically accomplished by
 ** synchronizing on some object that naturally encapsulates the set. If no such
 ** object exists, the set should be "wrapped" using the
 ** <code>AbstractSetFactory.synchronizedMultiSet</code> method. This is best
 ** done at creation time, to prevent accidental unsynchronized access to the
 ** <code>MultiHashSet</code> instance:
 ** <pre>
 **   MultiSet set = AbstractSetFactory.synchronizedMultiSet(new MultiHashSet(...));
 ** </pre>
 **
 ** @see Collection
 ** @see Map
 ** @see MultiSet
 ** @see AbstractMultiSet
 */
public class MultiHashSet<E> extends    AbstractMultiSet<E>
                             implements Cloneable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:885064631282079229")
  private static final long serialVersionUID = -2401745333937274414L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The backing instance of <code>Map</code> where the elements of this
   ** set are stored.
   */
  @SuppressWarnings("oracle.jdeveloper.java.field-not-serializable")
  private Map<E, Integer> delegate;

  /**
   ** Acts as a cache for the hash code value of this set out of performance
   ** considerations.
   ** Whenever this set is changed, storedHashCode is set to 0 and gets updated
   ** as soon as the <code>hashCode()</code> method is called.
   */
  private int             storedHashCode          = 0;

  /**
   ** Acts as a cache for the size value of this set out of performance
   ** considerations. The value of storedSize is always up-to-date since it
   ** gets updated by all destructive methods in this class.
   */
  private int             storedSize             = 0;

  /**
   ** Used to check, whether this multiset was modified by an destructive
   ** method while iterating over it.
   */
  private boolean         isConcurrentlyModified = false;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class MultiHashSetIterator
  // ~~~~~ ~~~~~~~~~~~~~~~~~~~~
  /**
   ** An iterator that, in spite of the specific element storage technique in
   ** a <code>MultiSet</code> (equal elements get 'counted' instead of
   ** each being stored separately), iterates over individual
   ** <code>MultiSet</code> elements.
   */
  private class MultiHashSetIterator implements Iterator<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Iterator<E> delegate;

    private E                 cursor;
    private int               multiElementIndex;
    private int               multiElementNumber;
    private boolean           removeEnabled;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Default constructor.
     */
    public MultiHashSetIterator() {
      this.delegate           = MultiHashSet.this.toSet().iterator();
      this.removeEnabled      = false;
      this.cursor             = null;
      this.multiElementNumber = 0;
      this.multiElementIndex  = 0;

      MultiHashSet.this.isConcurrentlyModified = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   hasNext (Iterator)
    public boolean hasNext() {
      if (MultiHashSet.this.isConcurrentlyModified)
        throw new ConcurrentModificationException();

      if (this.multiElementIndex > 0) {
        return true;
      }
      else {
        return (this.delegate.hasNext());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   next (Iterator)
    public E next() {
      if (hasNext()) {
        this.removeEnabled = true;
        if (this.multiElementIndex == 0) {
          this.cursor             = this.delegate.next();
          this.multiElementNumber = quantity(this.cursor);
          this.multiElementIndex  = this.multiElementNumber - 1;
        }
        else {
          this.multiElementIndex--;
        }
      }
      else {
        throw new NoSuchElementException();
      }
      return this.cursor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   remove (Iterator)
    public void remove() {
      if (MultiHashSet.this.isConcurrentlyModified) {
        throw new ConcurrentModificationException();
      }
      else if (this.removeEnabled) {
        if (this.multiElementNumber > 1) {
          final Integer oldCount = MultiHashSet.this.delegate.get(this.cursor);
          final Integer newCount = new Integer(oldCount.intValue() - 1);
          MultiHashSet.this.delegate.put(this.cursor, newCount);
          this.multiElementNumber--;
        }
        else {
          this.delegate.remove();
          this.multiElementNumber = 0;
        }
        removeEnabled = false;
        storedHashCode = 0;
        storedSize--;
      }
      else {
        throw new IllegalStateException();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty multiset; the backing <code>Map</code>
   ** instance has default initial capacity (16) and load factor (0.75).
   */
  public MultiHashSet() {
    this.delegate = new HashMap<E, Integer>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new multiset containing the elements in the specified
   ** {@link Collection}. The backing <code>Map</code> instance is created
   ** with default load factor (0.75) and an initial capacity sufficient to
   ** contain the elements in the specified {@link Collection}.
   **
   ** @param  collection         the {@link Collection} whose elements are to be
   **                            placed into this multiset.
   **
   ** @throws NullPointerException if the specified {@link Collection} is null.
   */
  public MultiHashSet(final Collection<? extends E> collection) {
    this.delegate = new HashMap<E, Integer>();
    this.addAll(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty multiset; the backing <code>Map</code>
   ** instance has specified initial capacity and default load factor (0.75).
   ** Note that the backing <code>Map</code> only stores single copies of
   ** equal elements.
   **
   ** @param  initialCapacity    the initial capacity for distinct
   **                            elements.
   **
   ** @throws IllegalArgumentException  if the initial capacity is negative.
   */
  public MultiHashSet(final int initialCapacity) {
   this.delegate = new HashMap<E, Integer>(initialCapacity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new, empty multiset; the backing <code>Map</code>
   ** instance has specified initial capacity and load factor. Note that the
   ** backing <code>Map</code> only stores single copies of equal elements.
   **
   ** @param  initialCapacity          the initial capacity for distinct
   **                                  elements.
   ** @param  loadFactor               the load factor.
   **
   ** @throws IllegalArgumentException if the initial capacity is negative
   **                                  or the load factor is nonpositive.
   */
  public MultiHashSet(final int initialCapacity, final float loadFactor) {
    this.delegate = new HashMap<E, Integer>(initialCapacity, loadFactor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clone (Cloneable)
  /**
   ** Returns a shallow copy of this <code>MultiHashSet</code> instance: the
   ** elements themselves are not cloned.
   **
   ** @return                    a shallow copy of this multiset.
   */
  @Override
  public Object clone() {
    MultiHashSet<E> copy = new MultiHashSet<E>(this.delegate.size());
    copy.delegate.putAll(this.delegate);
    copy.storedSize     = this.storedSize;
    copy.storedHashCode = this.storedHashCode;
    return copy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   iterator (Collection)
  /**
   ** Returns an iterator over the elements in this multiset. Different
   ** elements are returned in no particular order, however, equal elements
   ** are always returned subsequently.
   **
   ** @return                    an Iterator over the elements in this multiset.
   **
   ** @see    ConcurrentModificationException
   */
  @Override
  public Iterator<E> iterator() {
    return new MultiHashSetIterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Collection)
  /**
   ** Returns the number of elements in this multiset (its cardinality).
   **
   ** @return                    the number of elements in this multiset (its
   **                            cardinality).
   */
  @Override
  public int size() {
    return this.storedSize;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (MultiSet)
  /**
   ** Adds the specified element <code>quantity</code> of times to this
   ** multiset. If <code>quantity</code> is negative or 0, the multiset remains
   ** unchanged and <code>false</code> is returned.
   ** <p>
   ** If the set gets altered, this implementation sets
   ** <code>storedHashCode</code> to 0 (representing an unavailable hash code
   ** value), which forces <code>hashCode()</code> to recalculate the actual
   ** hash code value.
   **
   ** @param  element            element to be added to this set.
   ** @param  quantity           quantity of elements to add.
   **
   ** @return                    <code>true</code> if <code>quantity</code> is
   **                            greater than 0, <code>false</code> otherwise
   */
  @Override
  public boolean add(final E element, final int quantity) {
    if (quantity <= 0)
      return false;

    return this.quantity(element, this.quantity(element) + quantity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (MultiSet)
  /**
   ** Removes the specified element <code>quantity</code> of times from this
   ** multiset if possible. If <code>quantity</code> is negative or 0, the
   ** multiset remains unchanged and <code>false</code> is returned.
   ** <p>
   ** If the set gets altered, this implementation sets
   ** <code>storedHashCode</code> to 0 (representing an unavailable hash code
   ** value), which forces <code>hashCode()</code> to recalculate the actual
   ** hash code value.
   **
   ** @param  element            object to be removed from this multiset, if
   **                            present.
   ** @param  quantity           quantity of elements to remove.
   **
   ** @return                    <code>true</code> if the multiset got altered,
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean remove(final E element, final int quantity) {
    if (quantity <= 0)
      return false;

    return this.quantity(element, this.quantity(element) - quantity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toSet (MultiSet)
  /**
   ** Returns a new {@link Set} containing the 'flattened' version of this
   ** multiset in which every element of this multiset is present exactly once.
   **
   ** @return                     the 'flattened' version of this multiset.
   */
  @Override
  public Set<E> toSet() {
    return this.delegate.keySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setSize (MultiSet)
  /**
   ** Returns the size of a 'flattened' version of this multiset in which every
   ** element of this multiset is present exactly once.
   **
   ** @return                    the size of the 'flattened' version of this
   **                            multiset.
   */
  @Override
  public int setSize() {
    return this.delegate.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   quantity (MultiSet)
  /**
   ** Adjusts the number of times the specified element is present in this
   ** multiset to be the specified value (zero if the value is negative).
   ** <p>
   ** This implementation sets <code>storedHashCode</code> to 0 (representing an
   ** unavailable hash code value), which forces <code>hashCode()</code> to
   ** recalculate the actual hash code value.
   **
   ** @param  element            element whose quantity gets set.
   ** @param  quantity           quantity of the specified element to be set.
   **
   ** @return                    <code>true</code> if this multiset has been
   **                            modified, <code>false</code> otherwise.
   **
   ** @see    #quantity(Object)
   */
  @Override
  public boolean quantity(final E element, final int quantity) {
    int newStoredSize;

    if (quantity <= 0)
      newStoredSize = this.storedSize - quantity(element);
    else
      newStoredSize = this.storedSize + quantity - quantity(element);

    if (newStoredSize == this.storedSize)
      return false;

    this.storedSize = newStoredSize;
    this.storedHashCode = 0;
    this.isConcurrentlyModified = true;
    if (quantity <= 0) {
       this.delegate.remove(element);
    }
    else {
       this.delegate.put(element, new Integer(quantity));
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   quantity (MultiSet)
  /**
   ** Returns the number of times the specified element is present in this
   ** multiset.
   **
   ** @param  element            element whose quantity is returned.
   **
   ** @return                    quantity of the specified element, 0 if it is
   **                            not present.
   **
   ** @see    #quantity(Object, int)
   */
  @Override
  public int quantity(final Object element) {
    final Integer keyCount = this.delegate.get(element);
    if (keyCount == null)
      return 0;
    else
      return keyCount.intValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superSet (MultiSet)
  /**
   ** Returns <code>true</code> if this multiset is a superset of the specified
   ** {@link Collection}. That is, if all elements of the specified collection
   ** are also present in this multiset at least the same number of times.
   ** <p>
   ** This implementation checks if the specified {@link Collection} is an
   ** instance of {@link MultiSet} or {@link Set}. If so, the result of the
   ** super method <code>isSuperSet</code> is returned. Otherwise, it
   ** tries to create the intersection of this MultiHashSet and the specified
   ** {@link Collection} <code>collection</code> by iterating over
   ** <code>collection</code> and adding common elements to a new multiset. If
   ** an element is found whose quantity in the current intersection multiset is
   ** greater or equal than in this MultiHashSet, <code>false</code> is
   ** returned. If the intersection can be built up completely, this
   ** MultiHashSet is a superset of <code>collection</code> and
   ** <code>true</code> is returned.
   **
   ** @param  collection         {@link Collection} to be checked for being a
   **                            superset.
   **
   ** @return                    <code>true</code> if this multiset is a
   **                            superset of the specifed {@link Collection},
   **                            <code>false</code> otherwise.
   */
  @Override
  public boolean superSet(final Collection<E> collection) {
    if ((collection instanceof MultiSet) || (collection instanceof Set))
      return super.superSet(collection);

    if (this.size() >= collection.size()) {
      final MultiSet<E> ms = new MultiHashSet<E>();
      for (Iterator<E> i = collection.iterator(); i.hasNext(); ) {
        final E cursor = i.next();
        if (ms.quantity(cursor) < this.quantity(cursor)) {
          ms.add(cursor);
        }
        else {
          return false;
        }
      }
      return true;
    }
    else {
      return false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sum (MultiSet)
  /**
   ** Returns the sum with the specified {@link Collection}. This is a new
   ** <code>MultiSet</code> containing all elements that are present in this
   ** multiset or in the specified {@link Collection}. The quantities of equal
   ** elements get added up.
   **
   ** @param  collection         {@link Collection} to be united with.
   **
   ** @return                    the union with the specified {@link Collection}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public MultiSet<E> sum(final Collection<E> collection) {
    final MultiHashSet<E> result = (MultiHashSet<E>)this.clone();
    if (collection instanceof MultiSet) {
      final MultiSet<E> cMultiset = (MultiSet<E>)collection;
      for (Iterator<E> i = cMultiset.toSet().iterator(); i.hasNext(); ) {
        final E cursor = i.next();
        result.add(cursor, cMultiset.quantity(cursor));
      }
    }
    else {
      result.addAll(collection);
    }
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   union (MultiSet)
  /**
   ** Returns the union with the specified {@link Collection}. This is a new
   ** <code>MultiSet</code> containing all elements that are present in this
   ** multiset or in the specified {@link Collection}. For equal elements,
   ** the resulting quantity is the maximum of the two given quantities.
   **
   ** @param  collection         {@link Collection} to be united with.
   **
   ** @return                    the union with the specified {@link Collection}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public MultiSet<E> union(final Collection<E> collection) {
    MultiHashSet<E> resultingSet = null;
    if (collection instanceof MultiHashSet) {
      resultingSet = (MultiHashSet<E>)((MultiHashSet<E>)collection).clone();
    }
    else {
      resultingSet = new MultiHashSet<E>(collection);
    }

    for (Iterator<E> i = this.delegate.keySet().iterator(); i.hasNext(); ) {
      final E cursor             = i.next();
      final int numberThis       = this.quantity(cursor);
      final int numberCollection = resultingSet.quantity(cursor);
      if (numberThis > numberCollection) {
        resultingSet.quantity(cursor, numberThis);
      }
    }
    return resultingSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   intersection (MultiSet)
  /**
   ** Returns the intersection with the specified {@link Collection}. This is a
   ** new <code>MultiSet</code> containing all elements that are present in this
   ** multiset as well as in the specified {@link Collection}. For equal
   ** elements, the resulting quantity is the minimum of the two given quantities.
   **
   ** @param  collection         {@link Collection} to be intersected with.
   **
   ** @return                    the intersection with the specified collection.
   */
  @Override
  public MultiSet<E> intersection(final Collection<E> collection) {
    MultiHashSet<E> resultingSet = null;
    // d(Collection c) := number of unique elements in collection
    if (collection instanceof MultiSet) {
      // time  complexity = O(min{|d(this)|, |d(c)|})
      // space complexity = O(|resultingSet|)
      final MultiSet<E> collectionMultiSet = (MultiSet<E>)collection;
      Iterator<E>       i;
      if (collectionMultiSet.setSize() < this.setSize()) {
        i = collectionMultiSet.toSet().iterator();
      }
      else {
        i = this.toSet().iterator();
      }
      resultingSet = new MultiHashSet<E>();
      while (i.hasNext()) {
        final E   cursor           = i.next();
        final int numberThis       = this.quantity(cursor);
        final int numberCollection = collectionMultiSet.quantity(cursor);
        resultingSet.quantity(cursor, Math.min(numberThis, numberCollection));
      }
    }
    else {
      // time  complexity = O(|c|)
      // space complexity = O(|resultingSet|)
      // This section creates the intersection of this MultiHashSet
      // and the specified collection by iterating over collection
      // and adding common elements into a new MultiSet resultingSet.
      // If the quantity of a currentElement in the intersection MultiSet
      // is greater or equal than in this MultiHashSet, this
      // currentElement doesn't get added anymore.
      resultingSet = new MultiHashSet<E>();
      for (Iterator<E> i = collection.iterator(); i.hasNext(); ) {
        final E cursor = i.next();
        if (resultingSet.quantity(cursor) < this.quantity(cursor)) {
          resultingSet.add(cursor);
        }
      }
    }
    return resultingSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   difference (MultiSet)
  /**
   ** Returns the asymmetric difference between this multiset and the
   ** specified {@link Collection}. This is a new <code>MultiHashSet</code>
   ** containing all elements that are present in this multiset but not in the
   ** specified {@link Collection}. The quantities of equal elements get
   ** subtracted.
   **
   ** @param  collection         {@link Collection} from which the difference is
   **                            calculated.
   **
   ** @return                    the difference with the specified
   **                            {@link Collection}.
   */
  @Override
  public MultiSet<E> difference(final Collection<E> collection) {
    // d(Collection c) := number of unique elements in c
    MultiSet<E>     collectionSet;
    MultiHashSet<E> resultingSet = new MultiHashSet<E>();
    if (collection instanceof MultiSet) {
      // no time or space ressources needed
      collectionSet = (MultiSet<E>)collection;
    }
    else {
      // time  complexity = O(|c|)
      // space complexity = O(|d(c)|)
      collectionSet = new MultiHashSet<E>(collection);
    }

    // time  complexity = O(|d(this)|)
    // space complexity = O(|d(resultingSet)|)
    Iterator<E> i = this.toSet().iterator();
    while (i.hasNext()) {
      E currentElement = i.next();
      int numberThis = this.quantity(currentElement);
      int numberCollection = collectionSet.quantity(currentElement);
      resultingSet.quantity(currentElement, numberThis - numberCollection);
    }
    return resultingSet;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   symmetricDifference (MultiSet)
  /**
   ** Returns the symmetric difference between this multiset and the specified
   ** {@link Collection}. This is a new <code>MultiHashSet</code> containing
   ** all elements that are present either in this multiset or in the specified
   ** {@link Collection} but not in both. The quantities of equal elements get
   ** subtracted from each other (maximum minus minimum).
   **
   ** @param  collection         {@link Collection} from which the symmetric
   **                            difference is calculated
   **
   ** @return                    the symmetric difference with the specified
   **                            {@link Collection}.
   */
  @Override
  @SuppressWarnings("unchecked")
  public MultiSet<E> symmetricDifference(final Collection<E> collection) {
    MultiHashSet<E> resultingSet;
    if (collection instanceof MultiHashSet) {
      resultingSet = (MultiHashSet<E>)((MultiHashSet<E>)collection).clone();
    }
    else {
      resultingSet = new MultiHashSet<E>(collection);
    }

    for (Iterator<E> i = this.toSet().iterator(); i.hasNext(); ) {
      final E   cursor           = i.next();
      final int numberThis       = this.quantity(cursor);
      final int numberCollection = resultingSet.quantity(cursor);
      if (numberThis >= numberCollection) {
        resultingSet.quantity(cursor, numberThis - numberCollection);
      }
      else {
        resultingSet.quantity(cursor, numberCollection - numberThis);
      }
    }
    return resultingSet;
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
   ** <br>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two multisets
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   ** <p>
   ** This implementation first checks whether a cached hash code value is
   ** available. If not (i.e. <code>storedHashCode</code> is zero), the hash
   ** code gets calculated using <code>hashCode()</code> of the super class.
   **
   ** @return                    the hash code value for this multiset.
   */
  @Override
  public int hashCode() {
    if (this.storedHashCode == 0) {
      this.storedHashCode = super.hashCode();
    }
    return this.storedHashCode;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (overridden)
  /**
   ** Returns <code>true</code> if this multiset contains no elements.
   **
   ** @return                    <code>true</code> if this multiset contains no
   **                            elements, <code>false</code> otherwise.
   */
  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add (overridden)
  /**
   ** Adds the specified element to this multiset.
   ** <p>
   ** This implementation sets <code>storedHashCode</code> to 0 (representing
   ** an unavailable hash code value), which forces <code>hashCode()</code> to
   ** recalculate the actual hash code value.
   **
   ** @param  element            element to be added to this set.
   **
   ** @return                    <code>true</code>, adding an element to a
   **                            multiset will always be a success.
   */
  @Override
  public boolean add(final E element) {
    final Integer oldKeyCount = this.delegate.get(element);
    if (oldKeyCount != null) {
      final Integer newKeyCount = new Integer(oldKeyCount.intValue() + 1);
      this.delegate.put(element, newKeyCount);
    }
    else {
      this.delegate.put(element, new Integer(1));
    }
    this.storedSize++;
    this.storedHashCode = 0;
    this.isConcurrentlyModified = true;
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (overridden)
  /**
   ** Removes the specified element from this multiset if it is present. If the
   ** element is present more than once, its quantity gets decreased by one.
   ** <p>
   ** If the set gets altered, this implementation sets
   ** <code>storedHashCode</code> to 0 (representing an unavailable hash code
   ** value), which forces <code>hashCode()</code> to recalculate the actual
   ** hash code value.
   **
   ** @param  element            object to be removed from this multiset, if
   **                            present.
   **
   ** @return                    <code>true</code> if the multiset contained the
   **                            specified element, <code>false</code> otherwise.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean remove(final Object element) {
    final Integer oldKeyCount = this.delegate.get(element);
    if (oldKeyCount != null) {
      if (oldKeyCount.intValue() == 1) {
        this.delegate.remove(element);
      }
      else {
        final Integer newKeyCount = new Integer(oldKeyCount.intValue() - 1);
        this.delegate.put((E)element, newKeyCount);
      }
      this.storedSize--;
      this.storedHashCode = 0;
      this.isConcurrentlyModified = true;
      return true;
    }
    else {
      return false;
    }
  }
}