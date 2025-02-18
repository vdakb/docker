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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Openfire Database Connector

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.openfire.schema;

import java.io.IOException;
import java.io.Reader;

import java.io.StringWriter;

import java.sql.NClob;

import java.sql.SQLException;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.Collection;
import java.util.LinkedHashMap;

import oracle.iam.identity.icf.dbms.DatabaseError;
import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The base class of resources.
 **
 ** @param  <T>                  the type of the <code>Entity</code>
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>T</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Entity<T extends Entity> implements Resource<T>
                                               ,          Map<String, Object> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The alias name of a public name attribute. */
  public static final String NAME    = "name";

  /** The alias name of the user name attribute */
  public static final String UID     = "userName";

  /** The alias name of the group name attribute */
  public static final String GID     = "groupName";

  /** The alias name of a created attribute. */
  public static final String CREATED = "createdOn";

  /** The alias name of a updated attribute. */
  public static final String UPDATED = "updatedOn";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The attribute values of the entity instance where this wrapper belongs to
   */
  protected final Map<String, Object> data;

  //////////////////////////////////////////////////////////////////////////////
  // class Property
  // ~~~~~ ~~~~~~~~
  /**
   ** The <code>Property</code> belonging to an <code>Entity</code>.
   */
  public static class Property extends Entity<Property> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The alias name of the value attribute. */
    public static final String VALUE = "value";

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Property</code> resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Property() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Property</code> for the specified key-value pair.
     **
     ** @param  name             the name of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    protected Property(final String name, final String value) {
      // ensure inheritance
      super();

      // initialize instance attributes
      attribute(NAME, name);
      attribute(VALUE, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Property</code> target resource with the values supplied
     ** by the given mapping
     **
     ** @param  data             the mapping to be stored in the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     */
    protected Property(final Map<String, Object> data) {
      // ensure inheritance
      super(data);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Property</code>.
     **
     ** @param  value            the name of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Property</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Property</code>.
     */
    public final Property name(final String value) {
      attribute(NAME, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:  name
    /**
     ** Returns the name of the <code>Property</code>.
     **
     ** @return                  the userName of the <code>Property</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return attribute(NAME);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the value of the <code>Property</code>.
     **
     ** @param  value            the value of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Property</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Property</code>.
     */
    public final Property value(final String value) {
      attribute(VALUE, value);
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Property</code>.
     **
     ** @return                  the value of the <code>Property</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String value() {
      return attribute(VALUE);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: primary (Entity)
    /**
     ** Returns the value array of the uniquely identifying attribute.
     ** <br>
     ** Such a value is mainly used in join operations with other entities.
     **
     ** @return                  the value array of the uniquely identifying
     **                          attribute.
     **                          <br>
     **                          Possible object is array of {@link Object}.
     */
    @Override
    public final Object[] primary() {
      return new Object[]{attribute(NAME)};
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (Resource)
    /**
     ** Returns the string representation for this instance in its minimal form.
     **
     ** @return                  the string representation for this instance in
     **                          its minimal form.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return name();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Property</code> instance with the
     ** <code>name</code> specified and an initial <code>value</code>.
     **
     ** @param  name             the name of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the initial value of the <code>Property</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Property</code> instance created.
     **                          <br>
     **                          Possible object is <code>Property</code>.
     */
    public static Property build(final String name, final String value) {
      return new Property(name, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: build
    /**
     ** Factory method to create a <code>Property</code> that populates its
     ** values from the given attribute mapping.
     **
     ** @param  data             the mapping to be stored in the attribute
     **                          mapping.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     **
     ** @return                  the <code>Property</code> instance populated.
     **                          <br>
     **                          Possible object is <code>Property</code>.
     */
    public static Property build(final Map<String, Object> data) {
      return new Property(data);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Entity</code> target resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  Entity() {
    // ensure inheritance
    super();
    
     this.data = new LinkedHashMap<String, Object>();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> target resource with the values
   ** supplied by the given mapping.
   **
   ** @param  data               the mapping to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  Entity(final Map<String, Object> data) {
    // ensure inheritance
    super();

    // initialize instance
    this.data = data;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Returns the value array of the uniquely identifying attributes.
   ** <br>
   ** Such a value is mainly used in join operations with other entities.
   **
   ** @return                    the value array of the uniquely identifying
   **                            attribute.
   **                            <br>
   **                            Possible object is array of {@link Object}.
   */
  public abstract Object[] primary();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>String</code> object in the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the attribute string to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value associated with the specified key
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   **
   ** @return                   the <code>Entity</code> to allow method
   **                           chaining.
   **                           <br>
   **                           Possible object is <code>Entity</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public T attribute(final String key, final Object value) {
    attribute(this.data, key, value);
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   booleanValue
  /**
   ** Returns a <code>boolean</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the boolean for the given key.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public Boolean booleanValue(final String key) {
    final String result = attribute(key);
    return result == null ? null : Boolean.valueOf(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the int for the given key.
   **                            <br>
   **                            Possible object is {@link Integer}.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public Integer integerValue(final String key) {
    final String result = attribute(key);
    return result == null ? null : Integer.valueOf(result);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> from the attribute mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the long for the given key.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws ClassCastException if the object returned by the get operation on
   **                            the mappping is not castable to a
   **                            <code>String</code>.
   */
  public Long longValue(final String key) {
    final String result = attribute(key);
    return result == null ? null : Long.valueOf(result);
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
    return attribute(key);
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
    Object value = attribute(this.data, key);
    // IAM-1426: For some reason OFPROPERTY column PROPVALUE was changed to NCLOB, employ CharasterStream in this case
    // to convert the value to String
    if (value instanceof NClob) {
      // Implement a simple NCLOB -> String converter
      try (Reader reader = ((NClob) value).getCharacterStream();
           StringWriter w = new StringWriter()) {
          char[] buffer = new char[4096];
          int charsRead;
          while ((charsRead = reader.read(buffer)) != -1) {
              w.write(buffer, 0, charsRead);
          }
          value = w.toString();
      } catch (IOException | SQLException e) {
        System.out.println("OFS: Entity#attribute: Exception caught: " + e.getMessage());
      }
    }
    return value == null ? null : String.valueOf(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets a <code>String</code> value for the specified key object in the
   ** specified {@link Map}.
   **
   ** @param  map                the {@link Map} where the string should be
   **                            set for the specified key.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the
   **                            key and {@link Object} for the value.
   ** @param  key                the key for the attribute string to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value associated with the specified key
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected static void attribute(final Map<String, Object> map, final String key, final String value) {
    map.put(key, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets an {@link Object} value in the attribute mapping of this
   ** wrapper.
   **
   ** @param  map                the {@link Map} where the string should be
   **                            set for the specified key.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the
   **                            key and {@link Object} for the value.
   ** @param  key                the key for the attribute string to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value associated with the specified key
   **                            <br>
   **                            Allowed object is {@link Object}.
   */
  protected static void attribute(final Map<String, Object> map, final String key, final Object value) {
    map.put(key, value);
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public final int size() {
    return (this.data != null) ? this.data.size() : 0;
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
  public final void clear() {
    if (this.data != null)
     this.data.clear();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isEmpty (Map)
  /**
   ** Returns <code>true</code> if the attribute map contains no key-value
   ** mappings.
   **
   ** @return                    <code>true</code> if the attribute map contains
   **                            no key-value mappings.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean isEmpty() {
    return (this.data != null) ? this.data.isEmpty() : true;
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the attribute map contains
   **                            a mapping for the specified key.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ClassCastException   if the key is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the key is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> keys (optional).
   */
  @Override
  public final boolean containsKey(final Object key) {
    return (this.data != null) ? this.data.containsKey(key) : false;
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the attribute map maps one
   **                            or more keys to the specified value.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ClassCastException   if the value is of an inappropriate type for
   **                              the attribute map (optional).
   ** @throws NullPointerException if the value is <code>null</code> and the
   **                              attribute map does not permit
   **                              <code>null</code> values (optional).
   */
  @Override
  public final boolean containsValue(final Object value) {
    return (this.data != null) ? this.data.containsValue(value) : false;
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    the value to which the attribute map maps the
   **                            specified key, or <code>null</code> if the map
   **                            contains no mapping for this key.
   **                            <br>
   **                            Possible object is {@link Object}.
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
  public final Object get(final Object key) {
    return (this.data != null) ? this.data.get(key) : null;
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
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              value to be associated with the specified key.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key. A <code>null</code> return can also
   **                            indicate that the map previously associated
   **                            <code>null</code> with the specified key, if
   **                            the implementation supports <code>null</code>
   **                            values.
   **                            <br>
   **                            Possible object is {@link Object}.
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
  public final Object put(final String key, final Object value) {
    return (this.data != null) ? this.data.put(key, value) : null;
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    previous value associated with specified key,
   **                            or <code>null</code> if there was no mapping
   **                            for key.
   **                            <br>
   **                            Possible object is {@link Object}.
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
  public final Object remove(final Object key) {
    return (this.data != null) ? this.data.remove(key) : null;
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
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
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
  public final void putAll(final Map<? extends String, ? extends Object> t) {
    if (this.data != null)
     this.data.putAll(t);
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
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  public final Set<String> keySet() {
    return (this.data != null) ? this.data.keySet() : null;
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
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Object}.
   */
  @Override
  public final Collection<Object> values() {
    return (this.data != null) ? this.data.values() : null;
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
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link Map.Entry} with
   **                            {@link String} for the key and {@link Object}
   **                            as the value.
   */
  @Override
  public final Set<Map.Entry<String, Object>> entrySet() {
    return (this.data != null) ? this.data.entrySet() : null;
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public final int hashCode() {
    return (this.data != null) ? this.data.hashCode() : 0;
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to the attribute map.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public final boolean equals(final Object other) {
    return (this.data != null) ? this.data.equals(other) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertTimestamp
  /**
   ** Converte a {@link Date} value to the appropriate format of Openfire.
   **
   ** @param  timestamp          the {@link Date} to convert.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    a the internal string representation of a
   **                            timestamp value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String convertTimestamp(final Date timestamp) {
    return timestamp == null ? null : convertTimestamp(timestamp.getTime());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertTimestamp
  /**
   ** Converte a {@link Date} value to the appropriate format of Openfire.
   **
   ** @param  timestamp          the {@link Date} to convert.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    a the internal string representation of a
   **                            timestamp value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String convertTimestamp(final Long timestamp) {
    return timestamp == null ? null : convertTimestamp(timestamp.longValue());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertTimestamp
  /**
   ** Converta a {@link Date} value (represented by its long value )to the
   ** appropriate format of Openfire.
   **
   ** @param  timestamp          the timestamp value to convert.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    a the internal string representation of a
   **                            timestamp value.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public static String convertTimestamp(final long timestamp) {
    return String.format("%015d", timestamp);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns a <code>String</code> object from the specified {@link Map}.
   **
   ** @param  map                the {@link Map} from where the string should be
   **                            taken from.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   ** @param  key                the key for the desired attribute string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the string for the given key.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @throws ClassCastException if the object returnd by the get operation on
   **                            the map is not castable to a
   **                            <code>String</code>.
   */
  private static Object attribute(final Map<String, Object> map, final String key) {
    return map.get(key);
  }
}