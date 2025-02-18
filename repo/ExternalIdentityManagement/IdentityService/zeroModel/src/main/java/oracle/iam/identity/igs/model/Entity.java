/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.io.Serializable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The <code>Entity</code> is a generic wrapper for any object instance in
 ** Identity Manager. Such an object instance referes to a public name of such
 ** an instance and the internal system identifier served by Identiy Manager.
 ** Beside those identifiers <code>Entity</code> specifies the generic attribute
 ** collection to describe aditional properties of an <code>Entity</code>.
 ** <p>
 ** For example an account needs to be described by additional attributes. This
 ** account has an public identifier; ususally the name of the identity and the
 ** collection of attributes that are set for the account to provision for that
 ** identity. The account will look like:
 ** <pre>
 ** { "id"         : "azitterbacke"
 ** , "attributes" : [
 **     { "id"     : "firstName"
 **     , "value"  : "Alfons"
 **     }
 **   , { "id"     : "lastName"
 **     , "value"  : "Zitterbacke"
 **     }
 **   ]
 ** }
 ** </pre>
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entity"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="attribute" type="{http://www.oracle.com/schema/igs}attribute" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="id"        type="{http://www.oracle.com/schema/igs}token" use="required"/&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @param  <T>                  the java type the <code>Entity</code> relies
 **                              on.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the request payload
 **                              extending this class (requests can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **                              <br>
 **                              Allowed object is <code>&lt;T&gt;</code>.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Entity<T extends Entity> implements Serializable
                                               ,          Comparable<Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2476553743809266214")
  private static final long serialVersionUID = 9042908005769767417L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The public identifier of this <code>Entity</code>. */
  protected final String    id;

  /**
   ** The internal system identifier to which the <code>Entity</code> belongs in
   ** Identity Manager.
   ** <br>
   ** Intended use is to reuse after it fetched from Identity Manager to
   ** popultate referenced or depended entities that requires the key.
   */
  protected long            key = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** Attribute is a simple collection of tagged-value pairs.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;element name="attribute" minOccurs="0" maxOccurs="unbounded"&gt;
   **   &lt;complexType name="attribute"&gt;
   **     &lt;complexContent&gt;
   **       &lt;extension base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   **         &lt;attribute name="id"  type="{http://www.oracle.com/schema/igs}token"  use="required"/&gt;
   **         &lt;attribute name="enc" type="{http://www.oracle.com/schema/igs}encode" default="none"/&gt;
   **       &lt;/extension&gt;
   **     &lt;/simpleContent&gt;
   **   &lt;/complexType&gt;
   ** &lt;/element&gt;
   ** </pre>
   ** <b>Important</b>:
   ** <code>Attribute</code> extends {@link Entity}, which actually isn't the
   ** case if you look at the XSD. However, since other subclasses like account
   ** or application represent an {@link Entity}, this type of implementation
   ** was chosen in order not to control the attribution in each individual
   ** subclass, but to inherit it.
   **
   ** @param  <T>                the java type the attribute collection relies
   **                            on.
   **                            <br>
   **                            Allowed object is <code>&lt;T&gt;</code>.
   */
  public static class Attribute<T extends Attribute> extends    Entity<T>
                                                     implements Map<String, Object> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attribute
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:8923369595673202621")
    private static final long serialVersionUID = 3149456074686145289L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The generic attribute mapping an <code>Entity</code> can have. */
    protected final transient Map<String, Object> attribute = new LinkedHashMap<String, Object>();

    ////////////////////////////////////////////////////////////////////////////
    // enum Encode
    // ~~~~ ~~~~~~
    /**
     ** This enum store the grammar's constants of {@link Attribute.Encode}s.
     ** <p>
     ** The following schema fragment specifies the expected content contained
     ** within this class.
     ** <pre>
     ** &lt;simpleType name="encode"&gt;
     **   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
     **     &lt;enumeration value="none"/&gt;
     **     &lt;enumeration value="base16"/&gt;
     **     &lt;enumeration value="base64"/&gt;
     **   &lt;/restriction&gt;
     ** &lt;/simpleType&gt;
     ** </pre>
     */
    enum Encode {
        /** the encoded action values that can by applied on entitlements. */
        none("none")
      , base16("base16")
      , base64("base64")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      /** the human readable state value for this <code>Encode</code>. */
      public final String  id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Encode</code> with a single state.
       **
       ** @param  id             the human readable state value for this
       **                        <code>Encode</code>.
       **                        <br>
       **                        Possible object is {@link String}.
       */
      Encode(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper <code>Encode</code> from the given
       ** <code>id</code> value.
       **
       ** @param  id             the string value the <code>Encode</code> should
       **                        e returned for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the <code>Encode</code> mapped at
       **                        <code>id</code>.
       **                        <br>
       **                        Possible object is <code>Encode</code>.
       **
       ** @throws IllegalArgumentException if the given <code>id</code> is not
       **                                  mapped to an <code>Encode</code>.
       */
      public static Encode from(final String id) {
        for (Encode cursor : Encode.values()) {
          if (cursor.id.equals(id))
            return cursor;
        }
        throw new IllegalArgumentException(id);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Attribute</code> with the specified identifier and
     ** name.
     **
     ** @param  id               public identifier of <code>Attribute</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @throws NullPointerException if the public identifier <code>id</code> is
     **                              <code>null</code>.
     */
    protected Attribute(final String id) {
      // ensure inheritance
      super(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the value of a specific attribute.
     **
     ** @param  id               the identifier of the attribute to set
     **                          <code>Attribute Data</code> for.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the attribute to set as
     **                          <code>Attribute Data</code>.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  the <code>Attribute</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>T</code>.
     */
    @SuppressWarnings({"unchecked", "cast"})
    public final T value(final String id, final Object value) {
      put(id, value);
      return (T)this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of a specific property.
     **
     ** @param  id               the identifier of the attribute to set
     **                          <code>Attribute Data</code> for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the value of the attribute to set as
     **                          <code>Attribute Data</code>.
     **                          <br>
     **                          Possible object is {@link Object}.
     */
    public final Object value(final String id) {
      return get(id);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: attribute
    /**
     ** Returns the attribute {@link Map} of the <code>Entity</code> related to
     ** the source the <code>Entity</code> is loaded from.
     **
     ** @return                  the attribute {@link Map} of the
     **                          <code>Entity</code> related to the source the
     **                          <code>Entity</code> is loaded from.
     **                          <br>
     **                          Possible object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     */
    public final Map<String, Object> attribute() {
      return this.attribute;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: size (Map)
    /**
     ** Returns the number of key-value mappings in the attribute map. If the
     ** attribute map contains more than <code>Integer.MAX_VALUE</code>
     ** elements, returns <code>Integer.MAX_VALUE</code>.
     **
     ** @return                  the number of key-value mappings in the
     **                          attribute map.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int size() {
      return (this.attribute != null) ? this.attribute.size() : 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: isEmpty (Map)
    /**
     ** Returns <code>true</code> if the attribute map contains no key-value
     ** mappings.
     **
     ** @return                  <code>true</code> if the attribute map contains
     **                          no key-value mappings.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean isEmpty() {
      return (this.attribute != null) ? this.attribute.isEmpty() : true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsKey (Map)
    /**
     ** Returns <code>true</code> if the attribute map contains a mapping for
     ** the specified key. More formally, returns <code>true</code> if and only
     ** if the attribute map contains a mapping for a key <code>k</code> such
     ** that <code>(key==null ? k==null : key.equals(k))</code>.
     ** (There can be at most one such mapping.)
     **
     ** @param  key              key whose presence in the attribute map is to
     **                          be tested.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if the attribute map contains
     **                          a mapping for the specified key.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
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

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsValue (Map)
    /**
     ** Returns <code>true</code> if the attribute map maps one or more keys to
     ** the specified value. More formally, returns <code>true</code> if and
     ** only if the attribute map contains at least one mapping to a value
     ** <code>v</code> such that
     ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
     ** will probably require time linear in the map size for most
     ** implementations of the <code>Map</code> interface.
     **
     ** @param  value            value whose presence in the attribute map is to
     **                          be tested.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if the attribute map maps one
     **                          or more keys to the specified value.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     **
     ** @throws ClassCastException   if the value is of an inappropriate type
     **                              for the attribute map (optional).
     ** @throws NullPointerException if the value is <code>null</code> and the
     **                              attribute map does not permit
     **                              <code>null</code> values (optional).
     */
    @Override
    public boolean containsValue(final Object value) {
      return (this.attribute != null) ? this.attribute.containsValue(value) : false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: get (Map)
    /**
     ** Returns the value to which the attribute map maps the specified key.
     ** Returns <code>null</code> if the map contains no mapping for this key. A
     ** return value of <code>null</code> does not <i>necessarily</i> indicate
     ** that the map contains no mapping for the key; it's also possible that
     ** the attribute map explicitly maps the key to <code>null</code>. The
     ** <code>containsKey</code> operation may be used to distinguish these two
     ** cases.
     ** <p>
     ** More formally, if the attribute map contains a mapping from a key
     ** <code>k</code> to a value <code>v</code> such that
     ** <code>(key==null ? k==null : key.equals(k))</code>, then this method
     ** returns <code>v</code>; otherwise it returns <code>null</code>.
     ** (There can be at most one such mapping.)
     **
     ** @param  key              key whose associated value is to be returned.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  the value to which the attribute map maps the
     **                          specified key, or <code>null</code> if the map
     **                          contains no mapping for this key.
     **                          <br>
     **                          Possible object is {@link Object}.
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

    ////////////////////////////////////////////////////////////////////////////
    // Method: put (Map)
    /**
     ** Associates the specified value with the specified key in the attribute
     ** map (optional operation). If the attribute map previously contained a
     ** mapping for this key, the old value is replaced by the specified value.
     ** (A map <code>m</code> is said to contain a mapping for a key
     ** <code>k</code> if and only if
     ** {@link #containsKey(Object) m.containsKey(k)} would return
     ** <code>true</code>.))
     **
     ** @param  key              key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            value to be associated with the specified key.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  previous value associated with specified key,
     **                          or <code>null</code> if there was no mapping
     **                          for key. A <code>null</code> return can also
     **                          indicate that the map previously associated
     **                          <code>null</code> with the specified key, if
     **                          the implementation supports <code>null</code>
     **                          values.
     **                          <br>
     **                          Possible object is {@link Object}.
     **
     ** @throws UnsupportedOperationException if the <code>put</code> operation
     **                                       is not supported by thattribute
     **                                       attribute map.
     ** @throws ClassCastException            if the class of the specified key
     **                                       or value prevents it from being
     **                                       stored in the attribute map.
     ** @throws IllegalArgumentException      if some aspect of this key or
     **                                       value prevents it from being
     **                                       stored in the attribute map.
     ** @throws NullPointerException          if the attribute map does not
     **                                       permit <code>null</code> keys or
     **                                       values, and the specified key or
     **                                       value is <code>null</code>.
     */
    @Override
    public Object put(final String key, final Object value) {
      return (this.attribute != null) ? this.attribute.put(key, value) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: remove (Map)
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
    public Object remove(final Object key) {
      return (this.attribute != null) ? this.attribute.remove(key) : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: putAll (Map)
    /**
     ** Copies all of the mappings from the specified map to the attribute map
     ** (optional operation). The effect of this call is equivalent to that of
     ** calling {@link #put(String,Object) put(k, v)} on the attribute map once
     ** for each mapping from key <code>k</code> to value <code>v</code> in the
     ** specified map. The behavior of this operation is unspecified if the
     ** specified map is modified while the operation is in progress.
     **
     ** @param  t                the mappings to be stored in the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     **
     ** @throws UnsupportedOperationException if the <code>putAll</code> method
     **                                       is not supported by the attribute
     **                                       map.
     ** @throws ClassCastException            if the class of a key or value in
     **                                       the specified map prevents it from
     **                                       being stored in the attribute map.
     ** @throws IllegalArgumentException      some aspect of a key or value in
     **                                       the specified map prevents it from
     **                                       being stored in the attribute map.
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

    ////////////////////////////////////////////////////////////////////////////
    // Method: clear (Map)
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

    ////////////////////////////////////////////////////////////////////////////
    // Method: keySet (Map)
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
     ** @return                  a set view of the keys contained in the
     **                          attribute map.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    @Override
    public Set<String> keySet() {
      return (this.attribute != null) ? this.attribute.keySet() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: values (Map)
    /**
     ** Returns a collection view of the values contained in the attribute map.
     ** The collection is backed by the attribute map, so changes to the
     ** attribute map are reflected in the collection, and vice-versa. If the
     ** attribute map is modified while an iteration over the collection is in
     ** progress (except through the iterator's own <code>remove</code>
     ** operation), the results of the iteration are undefined. The collection
     ** supports element removal, which removes the corresponding mapping from
     ** the attribute map, via the <code>Iterator.remove</code>,
     ** <code>Collection.remove</code>, <code>removeAll</code>,
     ** <code>retainAll</code> and <code>clear</code> operations. It does not
     ** support the add or <code>addAll</code> operations.
     **
     ** @return                  a collection view of the values contained in
     **                          the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Object} as the value.
     */
    @Override
    public Collection<Object> values() {
      return (this.attribute != null) ? this.attribute.values() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: entrySet (Map)
    /**
     ** Returns a set view of the mappings contained in the attribute map. Each
     ** element in the returned set is a <code>Map.Entry</code>. The set is
     ** backed by the attribute map, so changes to the map are reflected in the
     ** set, and vice-versa. If the attribute map is modified while an iteration
     ** over the set is in progress (except through the iterator's own
     ** <code>remove</code> operation, or through the <code>setValue</code>
     ** operation on a map entry returned by the iterator) the results of the
     ** iteration are undefined. The set supports element removal, which removes
     ** the corresponding mapping from the attribute map, via the
     ** <code>Iterator.remove</code>, <code>Set.remove</code>,
     ** <code>removeAll</code>, <code>retainAll</code> and <code>clear</code>
     ** operations. It does not support the <code>add</code> or
     ** <code>addAll</code> operations.
     **
     ** @return                  a set view of the mappings contained in the
     **                          attribute map.
     **                          <br>
     **                          Possible object is {@link Set} where each
     **                          element is of type {@link Map.Entry}.
     */
    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
      return (this.attribute != null) ? this.attribute.entrySet() : null;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (Map)
    /**
     ** Returns the hash code value for the attribute map. The hash code of a
     ** map is defined to be the sum of the hashCodes of each entry in the map's
     ** entrySet view. This ensures that <code>t1.equals(t2)</code> implies that
     ** <code>t1.hashCode()==t2.hashCode()</code> for any two maps
     ** <code>t1</code> and <code>t2</code>, as required by the general contract
     ** of Object.hashCode.
     **
     ** @return                  the hash code value for the attribute map.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @see    Object#equals(Object)
     ** @see    Object#hashCode()
     ** @see    #equals(Object)
     */
    @Override
    public int hashCode() {
      return (this.attribute != null) ? this.attribute.hashCode() : 0;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (Map)
    /**
     ** Compares the specified object with the attribute map for equality.
     ** Returns <code>true</code> if the given object is also a map and the two
     ** Maps represent the same mappings.
     ** <p>
     ** More formally, two maps <code>t1</code> and <code>t2</code> represent
     ** the same mappings if <code>t1.entrySet().equals(t2.entrySet())</code>.
     ** This ensures that the <code>equals</code> method works properly across
     ** different implementations of the <code>Map</code> interface.
     **
     ** @param  other            object to be compared for equality with the
     **                          attribute map.
     **                          <br>
     **                          Possible object is {@link Object}.
     **
     ** @return                  <code>true</code> if the specified object is
     **                          equal to the attribute map.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      return (this.attribute != null) ? this.attribute.equals(other) : false;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Entity</code> with the specified public identifier but
   ** without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>Entity</code> belongs
   ** must be populated in manually.
   **
   ** @param  id                 public identifier of <code>Entity</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  protected Entity(final String id) {
    // ensure inheritance
    super();

    // prevent bogus input
    this.id = Objects.requireNonNull(id, "Public Identifier must not be null");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Sets the internal system identifier of this <code>Entity</code>.
   **
   ** @param  value              the internal system identifier for this
   **                            <code>Entity</code>.
   **                            <br>
   **                            Allowed object is <code>long</code>.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"unchecked", "cast"})
  public final T with(final long value) {
    this.key = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the internal system identifier of this <code>Entity</code>.
   **
   ** @return                    the internal system identifier of this
   **                            <code>Entity</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   */
  public final long key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the public name of this <code>Entity</code>.
   **
   ** @return                    the public name of this <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareTo (Comparable)
  /**
   ** Compares this object with the specified object for order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   **
   ** @param  other              the Object to be compared.
   **                            <br>
   **                            Allowed object is <code>Entity</code>.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the specified object's type is not
   **                            an instance of <code>Entity</code>.
   */
  @Override
  public int compareTo(final Entity other) {
    return this.id.compareTo(other.id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identity
  /**
   ** Factory method to create an <code>IdentityEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier to which the <code>IdentityEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>IdentityEntity</code> (usually the
   **                            <code>Login Name</code> of the identity in
   **                            Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>IdentityEntity</code> populated with
   **                            the given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>IdentityEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static IdentityEntity identity(final String id) {
    return new IdentityEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Factory method to create an <code>OrganizationEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier the <code>OrganizationEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>OrganizationEntity</code> (usually the
   **                            name of the <code>Organization</code> in
   **                            Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>OrganizationEntity</code> populated
   **                            with the given <code>id</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>OrganizationEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static OrganizationEntity organization(final String id) {
    return new OrganizationEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Factory method to create an <code>RoleEntity</code> with the specified
   ** public identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>RoleEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>RoleEntity</code> (usually the
   **                            <code>Role Name</code> of the identity in
   **                            Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>RoleEntity</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>RoleEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static RoleEntity role(final String id) {
    return new RoleEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Factory method to create an <code>ApplicationEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier the <code>ApplicationEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>ApplicationEntity</code> (usually the
   **                            name of the <code>Application Instance</code>
   **                            in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>ApplicationEntity</code> populated
   **                            with the given <code>id</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>ApplicationEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static ApplicationEntity application(final String id) {
    return new ApplicationEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Factory method to create an <code>NamespaceEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier the <code>NamespaceEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>NamespaceEntity</code> resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>NamespaceEntity</code> populated
   **                            with the given <code>id</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>NamespaceEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static NamespaceEntity namespace(final String id) {
    return new NamespaceEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Factory method to create an <code>EntitlementEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier the <code>NamespaceEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @param  action             the {@link EntitlementEntity.Action} to be
   **                            applied on this entitlement.
   **                            <br>
   **                            Allowed object is
   **                            {@link EntitlementEntity.Action}.
   ** @param  risk               the {@link Risk} to be applied on this
   **                            entitlement.
   **                            <br>
   **                            Allowed object is {@link Risk}.
   **
   ** @return                    the <code>NamespaceEntity</code> populated
   **                            with the given <code>id</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>NamespaceEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static EntitlementEntity entitlement(final EntitlementEntity.Action action, final Risk risk) {
    return new EntitlementEntity().with(action).with(risk);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   account
  /**
   ** Factory method to create a minimal <code>AccountEntity</code> resource.
   **
   ** @param  id                 the public identifier of the
   **                            <code>AccountEntity</code> (usually the
   **                            value of the <code>UID</code> for the account
   **                            in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>AccountEntity</code> populated with
   **                            the given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>AccountEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static AccountEntity account(final String id) {
    return new AccountEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   policy
  /**
   ** Factory method to create an <code>PolicyEntity</code> with the specified
   ** public identifier but without an valid internal system identifier.
   ** <p>
   ** The internal system identifier to which the <code>RoleEntity</code>
   ** belongs must be populated in manually.
   **
   ** @param  id                 the public identifier of the
   **                            <code>PolicyEntity</code> (usually the
   **                            <code>Access Policy Name</code> of the identity in
   **                            Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>PolicyEntity</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>PolicyEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static PolicyEntity policy(final String id) {
    return new PolicyEntity(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeValue
  /**
   ** Factory method to create an <code>EntitlementEntity</code> with the
   ** specified public identifier but without an valid internal system
   ** identifier.
   ** <p>
   ** The internal system identifier the <code>NamespaceEntity</code> belongs
   ** to has to be populated manually.
   **
   ** @return                    A new <code>AttributeValueEntity</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>NamespaceEntity</code>.
   **
   ** @throws NullPointerException if the public identifier <code>id</code> is
   **                              <code>null</code>.
   */
  public static AdditionalAttributeEntity additionalAttribute() {
    return new AdditionalAttributeEntity();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of
   **       the two objects must produce distinct integer results. However,
   **       the programmer should be aware that producing distinct integer
   **       results for unequal objects may improve the performance of hash
   **       tables.
   ** </ul>
   ** The implementation use the identifier of the persisted entity if available
   ** only. This is sufficient because the identifier is the primary key of the
   ** entity.
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  public int hashCode() {
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entities</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entities</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(Object other) {
    return (other instanceof Entity) ? this.id.equals(((Entity)other).id) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>Entity</code> returns a
   ** string consisting of the name of the class of which the object is an
   ** instance.
   **
   ** @return                    a string representation of the object.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  @Override
  public String toString() {
    return this.id;
  }
}