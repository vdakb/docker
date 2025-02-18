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

    File        :   IntergerMap.java

    Compiler    :   Oracle JDeveloper 10g

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IntergerMap.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2008-21-03  DSteding    First release version
*/

package oracle.hst.foundation.collection;

import java.lang.reflect.Array;

////////////////////////////////////////////////////////////////////////////////
// class IntergerMap
// ~~~~~ ~~~~~~~~~~~
/**
 ** A hash map that uses primitive ints for the key rather than objects.
 ** <p>
 ** Note that this class is for internal optimization purposes only, and may
 ** not be supported in future releases.
 **
 ** @see java.util.HashMap
 */
public class IntergerMap<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The load factor for the hash map. */
  private final float loadFactor;

  /** The hash table data. */
  private Entry<T>    table[];

  /** The total number of entries in the hash table. */
  private int         count;

  /**
   ** The table is rehashed when its size exceeds this threshold.
   ** (The value of this field is (int)(capacity * loadFactor).)
   */
  private int         threshold;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** This class that acts as a datastructure to create a new entry in the
   ** table.
   */
  private static class Entry<T> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    final int hash;
    final int key;
    T         value;
    Entry<T>  next;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:Ctor
    /**
     ** Create a new entry with the given values.
     **
     ** @param  hash             the code used to hash the object with.
     ** @param  key              the key used to enter this in the table.
     ** @param  value            the value for this key.
     ** @param  next             a reference to the next entry in the table.
     */
    protected Entry(final int hash, final int key, final T value, final Entry<T> next) {
      this.hash  = hash;
      this.key   = key;
      this.value = value;
      this.next  = next;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new, empty hash map with a default capacity and load factor,
   ** which is <code>20</code> and <code>0.75</code> respectively.
   */
  public IntergerMap() {
    // ensure inheritance
    this(20, 0.75f);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new, empty hash map with the specified initial capacity and
   ** default load factor, which is <code>0.75</code>.
   **
   ** @param  initialCapacity    the initial capacity of the hash map.
   **
   ** @throws IllegalArgumentException if the initial capacity is less than
   **                                  zero.
   */
  public IntergerMap(final int initialCapacity) {
    // ensure inheritance
    this(initialCapacity, 0.75f);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructs a new, empty hash map with the specified initial capacity and
   ** the specified load factor.
   **
   ** @param  initialCapacity    the initial capacity of the hash map.
   ** @param  loadFactor         the load factor of the hash map.
   **
   ** @throws IllegalArgumentException if the initial capacity is less than
   **                                  zero, or if the load factor is
   **                                  nonpositive.
   */
  public IntergerMap(int initialCapacity, final float loadFactor) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);

    // prevent bogus input
    if (loadFactor <= 0)
      throw new IllegalArgumentException("Illegal Load: " + loadFactor);

    // prevent bogus input
    if (initialCapacity == 0)
      initialCapacity = 1;

    // initialize instance
    this.loadFactor = loadFactor;
    this.table      = (Entry<T>[])Array.newInstance(Entry.class, initialCapacity); //new Entry<T>[initialCapacity];
    this.threshold  = (int)(initialCapacity * loadFactor);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methodes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  size
  /**
   ** Returns the number of keys in this hash map.
   **
   ** @return                    the number of keys in this hash map.
   */
  public int size() {
    return this.count;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isEmpty
  /**
   ** Tests if this hash map contains no keys for values.
   **
   ** @return                    <code>true</code> if this hash map contains no
   **                            keys to values; <code>false</code> otherwise.
   */
  public boolean isEmpty() {
    return this.count == 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Removes all elements from this hash map.
   */
  public void clear() {
    final Entry<T> table[] = this.table;
    for (int index = table.length; --index >= 0; )
      table[index] = null;
    this.count = 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsValue
  /**
   ** Returns <code>true</code> if this HashMap maps one or more keys to this
   ** value.
   ** <p>
   ** Note that this method is identical in functionality to contains
   ** (which predates the Map interface).
   **
   ** @param   value             the value whose presence in this HashMap is to
   **                            be tested.
   **
   ** @return                    <code>true</code> if the value is contained.
   **
   ** @see    java.util.Map
   */
  public boolean containsValue(final Object value) {
    return contains(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   contains
  /**
   ** Tests if some key maps into the specified value in this hash map.
   ** <p>
   ** This operation is more expensive than the <code>containsKey</code> method.
   ** <p>
   ** Note that this method is identical in functionality to containsValue,
   ** (which is part of the Map interface in the collections framework).
   **
   ** @param  value              a value to search for.
   **
   ** @return                    <code>true</code> if and only if some key maps
   **                            to the <code>value</code> argument in this hash
   **                            map as determined by the <code>equals</code>
   **                            method; <code>false</code> otherwise.
   ** @throws NullPointerException  if the value is <code>null</code>.
   **
   ** @see    #containsKey(int)
   ** @see    #containsValue(Object)
   ** @see    java.util.Map
   */
  public boolean contains(final Object value) {
    if (value == null)
      throw new NullPointerException();

    final Entry<T> table[] = this.table;
    for (int i = table.length; i-- > 0; ) {
      for (Entry<T> entry = table[i]; entry != null; entry = entry.next) {
        if (entry.value.equals(value))
          return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsKey
  /**
   ** Tests if the specified object is a key in this hash map.
   **
   ** @param  key                possible key.
   **
   ** @return                    <code>true</code> if and only if the specified
   **                            object is a key in this hash map, as
   **                            determined by the <code>equals</code> method;
   **                            <code>false</code> otherwise.
   **
   ** @see    #contains(Object)
   */
  public boolean containsKey(final int key) {
    final Entry<T> table[] = this.table;
    final int      hash    = key;
    final int      index   = (hash & 0x7FFFFFFF) % table.length;
    for (Entry<T> entry = table[index]; entry != null; entry = entry.next) {
      if (entry.hash == hash)
        return true;
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put
  /**
   ** Maps the specified <code>key</code> to the specified <code>value</code> in
   ** this hash map. The key cannot be <code>null</code>.
   ** <p>
   ** The value can be retrieved by calling the <code>get</code> method with a
   ** key that is equal to the original key.
   **
   ** @param  key                the hash map key.
   ** @param  value              the hash map value.
   **
   ** @return                     the previous value of the specified key in
   **                             this hash map, or <code>null</code> if it did
   **                             not have one.
   **
   ** @throws NullPointerException  if the key is <code>null</code>.
   **
   * @see     #get(int)
   */
  public T put(final int key, final T value) {
    // makes sure the key is not already in the hash map.
    Entry<T> table[] = this.table;
    int      hash    = key;
    int      index   = (hash & 0x7FFFFFFF) % table.length;
    for (Entry<T> entry = table[index]; entry != null; entry = entry.next) {
      if (entry.hash == hash) {
        T old = entry.value;
        entry.value = value;
        return old;
      }
    }

    if (this.count >= this.threshold) {
      // rehash the table if the threshold is exceeded
      ensureCapacity();
      table = this.table;
      index = (hash & 0x7FFFFFFF) % table.length;
    }

    // Creates the new entry.
    table[index] = new Entry<T>(hash, key, value, table[index]);
    this.count++;
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Returns the value to which the specified key is mapped in this map.
   **
   ** @param  key                a key in the hash map.
   **
   ** @return                    the value to which the key is mapped in this
   **                            hash map; <code>null</code> if the key is not
   **                            mapped to any value in this hash map.
   **
   ** @see    #put(int, Object)
   */
  public T get(final int key) {
    final Entry<T> table[] = this.table;
    final int      hash    = key;
    final int      index   = (hash & 0x7FFFFFFF) % table.length;
    for (Entry<T> entry = table[index]; entry != null; entry = entry.next) {
      if (entry.hash == hash)
        return entry.value;
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Removes the key (and its corresponding value) from this hash map.
   ** <p>
   ** This method does nothing if the key is not present in the hash map.
   **
   ** @param  key                the key that needs to be removed.
   **
   ** @return                    the value to which the key had been mapped in
   **                            this hash map, or <code>null</code> if the key
   **                            did not have a mapping.
   */
  public T remove(int key) {
    final Entry<T> table[] = this.table;
    final int      hash    = key;
    final int      index   = (hash & 0x7FFFFFFF) % table.length;
    for (Entry<T> e = table[index], prev = null; e != null; prev = e, e = e.next) {
      if (e.hash == hash) {
        if (prev != null)
          prev.next = e.next;
        else
          table[index] = e.next;
        this.count--;
        T oldValue = e.value;
        e.value = null;
        return oldValue;
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureCapacity
  /**
   ** Increases the capacity of and internally reorganizes this hash map, in
   ** order to accommodate and access its entries more efficiently.
   ** <p>
   ** This method is called automatically when the number of keys in the
   ** hash map exceeds this hash map's capacity and load factor.
   */
  protected void ensureCapacity() {
    final int oldCapacity   = this.table.length;
    final Entry<T> oldMap[] = this.table;

    final int newCapacity   = oldCapacity * 2 + 1;
    final Entry<T> newMap[] = (Entry<T>[])Array.newInstance(Entry.class, newCapacity); //new Entry<T>[newCapacity];

    this.threshold          = (int)(newCapacity * loadFactor);
    this.table              = newMap;

    for (int i = oldCapacity; i-- > 0; ) {
      for (Entry<T> old = oldMap[i]; old != null; ) {
        Entry<T> e = old;
        old = old.next;

        int index = (e.hash & 0x7FFFFFFF) % newCapacity;
        e.next = newMap[index];
        newMap[index] = e;
      }
    }
  }
}