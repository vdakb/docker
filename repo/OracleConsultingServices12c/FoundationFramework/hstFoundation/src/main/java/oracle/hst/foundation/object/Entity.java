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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common shared collection facilities

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-28-12  DSteding    First release version
*/

package oracle.hst.foundation.object;

import java.util.Set;
import java.util.Map;
import java.util.Collection;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.resource.SystemBundle;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

////////////////////////////////////////////////////////////////////////////////
// class Entity
// ~~~~~ ~~~~~~
/**
 ** An <code>Entity</code> represents an object (e.g., an Account or a Group) on
 ** a target resource. Each <code>Entity</code> represents a resource object as
 ** a bag of attributes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Entity implements Map<String, Attribute> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The type of the <code>Entity</code>. */
  final String                 type;

  /** The attributes of the <code>Entity</code>. */
  final Map<String, Attribute> delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Entity</code> with the specified type.
   **
   ** @param  type               the type of the <code>Entity</code>.
   */
  protected Entity(final String type) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (StringUtility.isBlank(type))
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "type"));

    this.type = type;
    // create an easy lookup map
    this.delegate = CollectionUtility.<Attribute>caseInsensitiveMap();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> with the specified type and values.
   **
   ** @param  type               the type of the <code>Entity</code>.
   ** @param  value              the values of the {@link Attribute}.
   */
  protected Entity(final String type, final Set<? extends Attribute> value) {
    // ensure inheritance
    super();

    // prevent bogus input
    if (StringUtility.isBlank(type))
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "type"));

    if (CollectionUtility.empty(value))
      throw new IllegalArgumentException("The value set can not be null or empty.");

    this.type = type;
    // create an easy lookup map
    this.delegate = toMap(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the <code>Entity</code>.
   **
   ** @return                    the name of the <code>Entity</code>.
   */
  public String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the set of {@link Attribute}s that represent this
   ** <code>Entity</code>.
   **
   ** @return                    the set of {@link Attribute}s that represent
   **                            this <code>Entity</code>.
   */
  public Set<Attribute> attributes() {
    // create a copy/unmodifiable set..
    return CollectionUtility.unmodifiableSet(this.delegate.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size (Map)
  /**
   ** Returns the number of key-value mappings in the attribute map. If the
   ** attribute map contains more than <code>Integer.MAX_VALUE</code> elements,
   ** returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of key-value mappings in the
   **                            attribute map.
   */
  @Override
  public int size() {
    return this.delegate.size();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (Map)
  /**
   ** Returns <code>true</code> if the attribute map contains no key-value
   ** mappings.
   **
   ** @return                    <code>true</code> if the attribute map contains
   **                            no key-value mappings.
   */
  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsKey (Map)
  /**
   ** Returns <code>true</code> if the attribute map contains a mapping for the
   ** specified key. More formally, returns <code>true</code> if and only if the
   ** attribute map contains a mapping for a key <code>k</code> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>.
   ** (There can be at most one such mapping.)
   **
   ** @param  key                key whose presence in the attribute map is to
   **                            be tested.
   **
   ** @return                    <code>true</code> if the attribute map contains
   **                            a mapping for the specified key.
   **
   ** @throws ClassCastException   if the key is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the key is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> keys (optional).
   */
  @Override
  public boolean containsKey(final Object key) {
    return this.delegate.containsKey(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   containsValue (Map)
  /**
   ** Returns <code>true</code> if the attribute map maps one or more keys to
   ** the specified value. More formally, returns <code>true</code> if and only
   ** if the attribute map contains at least one mapping to a value
   ** <code>v</code> such that
   ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
   ** will probably require time linear in the map size for most implementations
   ** of the <code>Map</code> interface.
   **
   ** @param  value              value whose presence in the attribute map is to
   **                            be tested.
   **
   ** @return                    <code>true</code> if the attribute map maps one
   **                            or more keys to the specified value.
   **
   ** @throws ClassCastException   if the value is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the value is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> values (optional).
   */
  @Override
  public boolean containsValue(final Object value) {
    return this.delegate.containsValue(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get (Map)
  /**
   ** Returns the value to which the attribute map maps the specified key.
   ** Returns <code>null</code> if the map contains no mapping for this key. A
   ** return value of <code>null</code> does not <i>necessarily</i> indicate
   ** that the map contains no mapping for the key; it's also possible that the
   ** attribute map explicitly maps the key to <code>null</code>. The
   ** <code>containsKey</code> operation may be used to distinguish these two
   ** cases.
   ** <p>
   ** More formally, if the attribute map contains a mapping from a key
   ** <code>k</code> to a value <code>v</code> such that
   ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
   ** returns <code>v</code>; otherwise it returns <code>null</code>.
   ** (There can be at most one such mapping.)
   **
   ** @param  key                key whose associated value is to be returned.
   **
   ** @return                    the value to which the attribute map maps the
   **                            specified key, or <code>null</code> if the map
   **                            contains no mapping for this key.
   **
   ** @throws ClassCastException   if the key is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the key is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> keys (optional).
   **
   ** @see    #containsKey(Object)
   */
  @Override
  public Attribute get(final Object key) {
    return this.delegate.get(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (Map)
  /**
   ** Associates the specified value with the specified key in the attribute map
   ** (optional operation). If the attribute map previously contained a mapping
   ** for this key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if {@link #containsKey(Object) m.containsKey(k)}
   ** would return <code>true</code>.))
   **
   ** @param  key                key with which the specified value is to be
   **                            associated.
   ** @param  value              value to be associated with the specified key.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **
   ** @throws UnsupportedOperationException if the <code>put</code> operation is
   **                                        not supported by thattribute
   **                                       attribute map.
   ** @throws ClassCastException            if the class of the specified key or
   **                                       value prevents it from being stored
   **                                       in the attribute map.
   ** @throws IllegalArgumentException      if some aspect of this key or value
   **                                        prevents it from being stored in the
   **                                       attribute map.
   ** @throws NullPointerException          if the attribute map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified key or value is
   **                                       <code>null</code>.
   */
  @Override
  public Attribute put(final String key, final Attribute value) {
    return this.delegate.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove (Map)
  /**
   ** Removes the mapping for this key from the attribute map if it is present
   ** (optional operation).
   ** <p>
   ** More formally, if the attribute map contains a mapping from key
   ** <code>k</code> to value <code>v</code> such that
   ** <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is
   ** removed. (The map can contain at most one such mapping.)
   ** <p>
   ** Returns the value to which the map previously associated the key, or
   ** <code>null</code> if the map contained no mapping for this key. (A
   ** <code>null</code> return can also indicate that the map previously
   ** associated <code>null</code> with the specified key if the implementation
   ** supports <code>null</code> values.) The map will not contain a mapping for
   ** the specified  key once the call returns.
   **
   ** @param  key                key whose mapping is to be removed from the
   **                            map.
   **
   ** @return previous           value associated with specified key, or
   **                            <code>null</code> if there was no mapping for
   **                            key.
   **
   ** @throws ClassCastException            if the key is of an inappropriate
   **                                       type for the attribute map
   **                                       (optional).
   ** @throws NullPointerException          if the key is <code>null</code> and
   **                                       the attribute map does not permit
   **                                       <code>null</code> keys (optional).
   ** @throws UnsupportedOperationException if the <code>remove</code> method is
   **                                       not supported by the attribute map.
   */
  @Override
  public Attribute remove(final Object key) {
    return this.delegate.remove(key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putAll (Map)
  /**
   ** Copies all of the mappings from the specified map to the attribute map
   ** (optional operation). The effect of this call is equivalent to that of
   ** calling {@link #put(String,Attribute) put(k, v)} on the attribute map once
   ** for each mapping from key <code>k</code> to value <code>v</code> in the
   ** specified map. The behavior of this operation is unspecified if the
   ** specified map is modified while the operation is in progress.
   **
   ** @param  t                  mappings to be stored in the attribute map.
   **
   ** @throws UnsupportedOperationException if the <code>putAll</code> method is
   **                                       not supported by the attribute map.
   ** @throws ClassCastException            if the class of a key or value in
   **                                       the specified map prevents it from
   **                                       being stored in the attribute map.
   ** @throws IllegalArgumentException      some aspect of a key or value in the
   **                                        specified map prevents it from being
   **                                       stored in the attribute map.
   ** @throws NullPointerException          if the specified map is
   **                                       <code>null</code>, or if the
   **                                       attribute map does not permit
   **                                       <code>null</code> keys or values,
   **                                       and the specified map contains
   **                                       <code>null</code> keys or values.
   */
  @Override
  public void putAll(final Map<? extends String, ? extends Attribute> t) {
    this.delegate.putAll(t);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear (Map)
  /**
   ** Removes all mappings from the attribute map (optional operation).
   **
   ** @throws UnsupportedOperationException if clear is not supported by the
   **                                       attribute map.
   */
  @Override
  public void clear() {
   this.delegate.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   keySet (Map)
  /**
   ** Returns a set view of the keys contained in the attribute map. The set is
   ** backed by the attribute map, so changes to attribute map are reflected in
   ** the set, and vice-versa. If attribute map is modified while an iteration
   ** over the set is in progress (except through the iterator's own
   ** <code>remove</code> operation), the results of the iteration are
   ** undefined. The set supports element removal, which removes the
   ** corresponding mapping from attribute map, via the
   ** <code>Iterator.remove</code>, <code>Set.remove</code>,
   ** <code>removeAll</code> <code>retainAll</code>, and <code>clear</code>
   ** operations. It does not support the add or <code>addAll</code> operations.
   **
   ** @return                    a set view of the keys contained in the
   **                            attribute map.
   */
  @Override
  public Set<String> keySet() {
    return this.delegate.keySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   values (Map)
  /**
   ** Returns a collection view of the values contained in the attribute map.
   ** The collection is backed by the attribute map, so changes to the attribute
   ** map are reflected in the collection, and vice-versa. If the attribute map
   ** is modified while an iteration over the collection is in progress (except
   ** through the iterator's own <code>remove</code> operation), the results of
   ** the iteration are undefined. The collection supports element removal,
   ** which removes the corresponding mapping from the attribute map, via the
   ** <code>Iterator.remove</code>, <code>Collection.remove</code>,
   ** <code>removeAll</code>, <code>retainAll</code> and <code>clear</code>
   ** operations. It does not support the add or <code>addAll</code> operations.
   **
   ** @return                    a collection view of the values contained in
   **                            the attribute map.
   */
  @Override
  public Collection<Attribute> values() {
    return this.delegate.values();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entrySet (Map)
  /**
   ** Returns a set view of the mappings contained in the attribute map. Each
   ** element in the returned set is a <code>Map.Entry</code>. The set is backed
   ** by the attribute map, so changes to the map are reflected in the set, and
   ** vice-versa. If the attribute map is modified while an iteration over the
   ** set is in progress (except through the iterator's own <code>remove</code>
   ** operation, or through the <code>setValue</code> operation on a map entry
   ** returned by the iterator) the results of the iteration are undefined. The
   ** set supports element removal, which removes the corresponding mapping from
   ** the attribute map, via the <code>Iterator.remove</code>,
   ** <code>Set.remove</code>, <code>removeAll</code>, <code>retainAll</code>
   ** and <code>clear</code> operations. It does not support the
   ** <code>add</code> or <code>addAll</code> operations.
   **
   ** @return                    a set view of the mappings contained in the
   **                            attribute map.
   */
  @Override
  public Set<Map.Entry<String, Attribute>> entrySet() {
    return this.delegate.entrySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (Map)
  /**
   ** Compares the specified object with the attribute map for equality. Returns
   ** <code>true</code> if the given object is also a map and the two Maps
   ** represent the same mappings.
   ** <p>
   ** More formally, two maps <code>t1</code> and <code>t2</code> represent the
   ** same mappings if <code>t1.entrySet().equals(t2.entrySet())</code>. This
   ** ensures that the <code>equals</code> method works properly across
   ** different implementations of the <code>Map</code> interface.
   **
   ** @param  other              object to be compared for equality with the
   **                            attribute map.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other instanceof Entity) {
      Entity entity = (Entity)other;
      if (!this.type.equals(entity.type())) {
        return false;
      }
      return CollectionUtility.equals(attributes(), entity.attributes());
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Map)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public int hashCode() {
    return attributes().hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Entity</code> with the specified
   ** type.
   **
   ** @param  type               the type of the <code>Entity</code>.
   **
   ** @return                    an empty instance of <code>Entity</code> with a
   **                            type.
   */
  public static Entity build(final String type) {
    return new Entity(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Entity</code> with the specified type and
   ** values.
   **
   ** @param  type               the type of the <code>Entity</code>.
   ** @param  value              the values of the {@link Attribute}.
   **
   ** @return                    an empty instance of <code>Entity</code> with a
   **                            type and values.
   */
  public static Entity build(final String type, final Set<? extends Attribute> value) {
    return new Entity(type, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toMap
  /**
   ** Transform a <code>Collection</code> of {@link Attribute} instances into a
   ** {@link Map}.
   ** <p>
   ** The key to each element in the map is the <i>name</i> of an
   ** {@link Attribute}. The value of each element in the map is the
   ** {@link Attribute} instance with that name.
   **
   ** @param  attributes         a {@link Set} of attribute to transform to a
   **                            {@link Map}.
   **
   ** @return                    a {@link Map} of string and {@link Attribute}.
   **
   ** @throws NullPointerException if the parameter <b><code>attributes</code></b>
   **                              is <b><code>null</code></b>.
   */
  public static Map<String, Attribute> toMap(final Collection<? extends Attribute> attributes) {
    final Map<String, Attribute> map = CollectionUtility.<Attribute>caseInsensitiveMap();
    for (Attribute cursor : attributes)
      map.put(cursor.name(), cursor);

    return CollectionUtility.unmodifiable(map);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Takes all the attribute from an <code>Entity</code> and add/overwrite.
   **
   ** @param  entity             the <code>Entity</code> whos attribute added
   **                            or may be override the attributes of this
   **                            <code>Entity</code>.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   */
  public Entity add(final Entity entity) {
    // prevent bogus input
    if (entity == null)
      throw new IllegalArgumentException(SystemBundle.format(SystemError.ARGUMENT_IS_NULL, "entity"));

    // simply add all the attributes
    for (Attribute cursor : entity.attributes())
      add(cursor);

    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a <code>Attribute</code> with the specified name and values.
   **
   ** @param  name               the name of the {@link Attribute}.
   ** @param  value              the values for the {@link Attribute}s.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   */
  public Entity addAttribute(final String name, final Object... value) {
    add(Attribute.build(name, value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds a <code>Attribute</code> with the specified name and values.
   **
   ** @param  name               the name of the {@link Attribute}.
   ** @param  value              the values for the {@link Attribute}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   */
  public Entity addAttribute(final String name, final Collection<?> value) {
    add(Attribute.build(name, value));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds one or many {@link Attribute}s to the <code>Entity</code>.
   **
   ** @param  attrs              the array of {@link Attribute}s to add.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   */
  public Entity add(final Attribute... attrs) {
    for (Attribute a : attrs) {
      this.delegate.put(a.name(), a);
    }
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add all the {@link Attribute}s of a {@link Collection}.
   **
   ** @param  attrs              the {@link Collection} of {@link Attribute}s to
   **                            add.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   */
  public Entity add(final Collection<Attribute> attrs) {
    for (Attribute cursor : attrs) {
      this.delegate.put(cursor.name(), cursor);
    }
    return this;
  }
}