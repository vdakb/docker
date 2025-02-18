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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AbstractObject.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractObject.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractObject
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>AbstractObject</code> implements the base functionality
 ** of an Oracle Identity Manager Object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public abstract class AbstractObject extends    AbstractLoggable
                                     implements Map<String, Object> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the attribute values of the Oracle Identity Manager instance where this
   ** wrapper belongs to
   */
  protected final Map<String, Object> attribute = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractObject</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this wrapper.
   */
  public AbstractObject(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AbstractObject</code> which is associated
   ** with the specified task and belongs to the Oracle Identity Manager Object
   ** specified by the given name.
   ** <br>
   ** The Oracle Identity Manager Object will be populated from the repository
   ** of the Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this wrapper.
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            instance where this wrapper belongs to.
   **
   ** @throws TaskException      if the object is not defined in the Oracle
   **                            Identity Manager meta entries or one or more
   **                            attributes are missing on the IT Resource Type
   **                            Definition.
   */
  public AbstractObject(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    this(loggable);

    // initialize instance attributes
    populateInstance(instanceName);
    populateAttributes(instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>String</code> object in the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the attribute string to set.
   ** @param  value              the value associated with the specified key
   */
  public void attribute(final String key, final String value) {
    attribute(this.attribute, key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the boolean for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public boolean booleanValue(final String key) {
    String result = attribute(key);
    // convert the yes/no semantic to the correct meaning for class Boolean
    if (SystemConstant.YES.equalsIgnoreCase(result))
      result = SystemConstant.TRUE;

    return Boolean.valueOf(result).booleanValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the boolean for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public boolean booleanValue(final String key, final boolean defaultValue) {
    String result = attribute(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else {
      // convert the yes/no semantic to the correct meaning for class Boolean
      if (SystemConstant.YES.equalsIgnoreCase(result))
        result = SystemConstant.TRUE;

      return Boolean.valueOf(result).booleanValue();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the int for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public int integerValue(final String key) {
    final String result = attribute(key);
    return Integer.parseInt(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public int integerValue(final String key, final int defaultValue) {
    final String result = attribute(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      return Integer.parseInt(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the long for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public long longValue(final String key) {
    final String result = attribute(key);
    return Long.parseLong(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public long longValue(final String key, final long defaultValue) {
    final String result = attribute(key);
    if (StringUtility.isEmpty(result))
      return defaultValue;
    else
      return Long.parseLong(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringValue(final String key) {
    return stringValue(key, SystemConstant.EMPTY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringValue
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public String stringValue(final String key, final String defaultValue) {
    String result = attribute(key);
    if (StringUtility.isEmpty(result))
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringList
  /**
   ** Returns a <code>String</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired {@link List} of
   **                            <code>String</code>.
   **
   ** @return                    the <code>String</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public List<String> stringList(final String key) {
    return stringList(key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringList
  /**
   ** Returns a {@link List} of <code>String</code> from the attribute mapping of
   ** this wrapper.
   **
   ** @param  key                the key for the desired {@link List} of
   **                            <code>String</code>.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the {@link List} of <code>String</code> for the
   **                            given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a {@link List}
   **                            of <code>String</code>.
   */
  public List<String> stringList(final String key, final List<String> defaultValue) {
    @SuppressWarnings("unchecked")
    List<String> result = (List<String>)attribute(this.attribute, key);
    if (result == null)
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enumValue
  /**
   ** Returns a <code>Enum</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired {@link Enum} of
   **                            <code>Enum</code>.
   **
   ** @return                    the <code>Enum</code> for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>Enum</code>.
   */
  public Enum<?> enumValue(final String key) {
    return enumValue(key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   enumValue
  /**
   ** Returns a <code>Enum</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the int for the given key or the default.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>Enum</code>.
   */
  public Enum<?> enumValue(final String key, final Enum<?> defaultValue) {
    Enum<?> result = (Enum<?>)attribute(this.attribute, key);
    if (result == null)
      result = defaultValue;

    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the array with names which should be populated from the definition
   ** of Oracle Identity Manager.
   **
   ** @return                    the array with names which should be populated.
   **
   ** @throws TaskException      if the attribute array was not build.
   */
  public abstract AbstractAttribute[] attributes()
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>String</code> value for the specified key object in the
   ** specified {@link Map}.
   **
   ** @param  map                the {@link Map} where the string should be
   **                            set for the specified key.
   ** @param  key                the key for the attribute string to set.
   ** @param  value              the value associated with the specified key
   */
  protected static void attribute(final Map<String, Object> map, final String key, final String value) {
    map.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>AbstractObject</code> object in the attribute mapping of this
   ** wrapper.
   **
   ** @param  map                the {@link Map} where the string should be
   **                            set for the specified key.
   ** @param  key                the key for the attribute string to set.
   ** @param  value              the value associated with the specified key
   */
  protected static void attribute(final Map<String, Object> map, final String key, final AbstractObject value) {
    map.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns a <code>String</code> object from the attribute mapping of wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the string for the given key.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  protected String attribute(final String key) {
    final Object value = attribute(this.attribute, key);
    return value != null ? value.toString() : null;
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
    return (this.attribute != null) ? this.attribute.size() : 0;
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
    return (this.attribute != null) ? this.attribute.isEmpty() : true;
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
    return (this.attribute != null) ? this.attribute.containsKey(key) : false;
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
    return (this.attribute != null) ? this.attribute.containsValue(value) : false;
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
  public Object get(final Object key) {
    return (this.attribute != null) ? this.attribute.get(key) : null;
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
  public Object put(final String key, final Object value) {
    return (this.attribute != null) ? this.attribute.put(key, value) : null;
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
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key.
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
  public Object remove(final Object key) {
    return (this.attribute != null) ? this.attribute.remove(key) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   putAll (Map)
  /**
   ** Copies all of the mappings from the specified map to the attribute map
   ** (optional operation). The effect of this call is equivalent to that of
   ** calling {@link #put(String,Object) put(k, v)} on the attribute map once
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
  public void putAll(final Map<? extends String, ? extends Object> t) {
    if (this.attribute != null)
     this.attribute.putAll(t);
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
    if (this.attribute != null)
     this.attribute.clear();
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
    return (this.attribute != null) ? this.attribute.keySet() : null;
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
  public Collection<Object> values() {
    return (this.attribute != null) ? this.attribute.values() : null;
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
  public Set<Map.Entry<String, Object>> entrySet() {
    return (this.attribute != null) ? this.attribute.entrySet() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Map)
  /**
   ** Returns the hash code value for the attribute map. The hash code of a map
   ** is defined to be the sum of the hashCodes of each entry in the map's
   ** entrySet view. This ensures that <code>t1.equals(t2)</code> implies that
   ** <code>t1.hashCode()==t2.hashCode()</code> for any two maps <code>t1</code>
   ** and <code>t2</code>, as required by the general contract of
   ** Object.hashCode.
   **
   ** @return                    the hash code value for the attribute map.
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public int hashCode() {
    return (this.attribute != null) ? this.attribute.hashCode() : 0;
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
   ** @return                    <code>true</code> if the specified object is
   **                            equal to the attribute map.
   */
  @Override
  public boolean equals(final Object other) {
    return (this.attribute != null) ? this.attribute.equals(other) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of this instance.
   ** <br>
   ** Adjacent elements are separated by the character "\n" (line feed).
   ** Elements are converted to strings as by String.valueOf(Object).
   **
   ** @return                    the string representation of this instance.
   */
  @Override
  public String toString() {
    return StringUtility.formatCollection(this);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes
  /**
   ** Prepares the container to recieve the attribute definition for this
   ** wrapper.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            instance where this wrapper belongs to.
   **
   ** @throws TaskException      a generell error has occurred.
   */
  public abstract void populateAttributes(final String instanceName)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateInstance
  /**
   ** Obtains the Oracle Identity Manager Object instance definition from Oracle
   ** Identity Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            instance where this wrapper belongs to.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the Oracle Identity Manager Object
   **                                  instance is not defined in the Oracle
   **                                  Identity Manager meta data entries with
   **                                  the name passed to the constructor of
   **                                  this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                            </ul>
   **                            or generell error has occurred.
   */
  protected abstract void populateInstance(final String instanceName)
    throws TaskException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchStringValue
  /**
   ** Retrieves the value of a particular field from the specified data record.
   **
   ** @param  resultSet          the meta data record where the value should be
   **                            obtained from.
   ** @param  fieldName          the name of the field as defined in the Oracle
   **                            Identity Manager meta data entries.
   ** @param  defaultValue       the default for the String value if no
   **                            resource found.
   **                            This value will also be returned if the field
   **                            is not defined in the meta data entries.
   **
   ** @return                    the string representation of the value of the
   **                            specified field.
   **
   ** @throws TaskException      a generell error has occurred.
   */
  protected static String fetchStringValue(final tcResultSet resultSet, final String fieldName, final String defaultValue)
    throws TaskException {

    String value = null;
    try {
      value = resultSet.getStringValue(fieldName);
      if (StringUtility.isEmpty(value))
        value = defaultValue;
    }
    catch (tcColumnNotFoundException e) {
      value = defaultValue;
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchStringValue
  /**
   ** Retrieves the value of a particular field from the specified data record.
   **
   ** @param  resultSet          the meta data record where the value should be
   **                            obtained from.
   ** @param  fieldName          the name of the field as defined in the Oracle
   **                            Identity Manager meta data entries.
   **
   ** @return                    the string representation of the value of the
   **                            specified field.
   **
   ** @throws TaskException      in case the field was not defined in the meta
   **                            data entries or a generell error has occurred.
   */
  protected static String fetchStringValue(final tcResultSet resultSet, final String fieldName)
    throws TaskException {

    String value = null;
    try {
      value = resultSet.getStringValue(fieldName);
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, fieldName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fetchLongValue
  /**
   ** Retrieves the value of a particular field from the specified data record.
   **
   ** @param  resultSet          the meta data record where the value should be
   **                            obtained from.
   ** @param  fieldName          the name of the field as defined in the Oracle
   **                            Identity Manager meta data entries.
   **
   ** @return                    the string representation of the value of the
   **                            specified field.
   **
   ** @throws TaskException      in case the field was not defined in the meta
   **                            data entries or a generell error has occurred.
   */
  protected static long fetchLongValue(final tcResultSet resultSet, final String fieldName)
    throws TaskException {

    long value = -1L;
    try {
      value = resultSet.getLongValue(fieldName);
    }
    catch (tcColumnNotFoundException e) {
      throw new TaskException(TaskError.COLUMN_NOT_FOUND, fieldName);
    }
    catch (tcAPIException e) {
      throw new TaskException(e);
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns a <code>String</code> object from the specified {@link Map}.
   **
   ** @param  map                the {@link Map} from where the string should be
   **                            taken from.
   ** @param  key                the key for the desired attribute string.
   **
   ** @return                    the string for the given key.
   **
   ** @throws ClassCastException if the object returnd by the get operation on
   **                            the map is not castable to a
   **                            <code>String</code>.
   */
  private static Object attribute(final Map<String, Object> map, final String key) {
    return map.get(key);
  }
}