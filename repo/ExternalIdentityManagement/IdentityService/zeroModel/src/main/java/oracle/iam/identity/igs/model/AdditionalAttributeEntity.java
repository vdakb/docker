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

    File        :   EntitlementEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** An <code>EntitlementEntity</code> wrappes the details of authorizations
 ** granted or revoked to or from an account in a target system.
 ** <p>
 ** The public identifier of an <code>EntitlementEntity</code> refers to the
 ** namespace of the authorization model in Identity Manager. In other words
 ** this id represents the register tab displayed in the UI of an account detail
 ** page.
 ** <p>
 ** The record of such an authorization can in turn consist of a number of
 ** attributes, which in turn are specified by the authorization system of the
 ** target system. For this reason, an entitlement maintains a collection of
 ** generic attributes describing the <code>EntitlementEntity</code>s.
 ** <p>
 ** An authorization can be granted or revoked. A modification of existing
 ** authorizations is not planned. For this reason,
 ** <code>EntitlementEntity</code> as a concrete implementation of
 ** {@link Entity} has been extended with an attribute <code>action</code>,
 ** which declares <code>assign</code> and <code>revoke</code> as its canonical
 ** values.
 ** <br>
 ** <code>Action.assign</code> is the default value for this attribute.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entitlement"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="attributes" type="{http://www.oracle.com/schema/igs}attribute" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="action" use="required"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.oracle.com/schema/igs}token"&gt;
 **             &lt;enumeration value="assign"/&gt;
 **             &lt;enumeration value="revoke"/&gt;
 **             &lt;enumeration value="modify"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 ** <b>Note</b>:
 ** <br>
 ** The name of the class is chossen in this way to avoid conflicts if the
 ** implementation hits IdentityManager where a class with similar semantic is
 ** offered by the EJB's.
 **
 ** @see     Entity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class AdditionalAttributeEntity implements Serializable
                               ,          Map<String, Object> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4219509221081119259")
  private static final long serialVersionUID = 2043719141690200989L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The generic attribute mapping an <code>Entity</code> can have. */
  private final transient Map<String, Object> attribute = new LinkedHashMap<String, Object>();

  /** The generic attribute mapping an <code>Entity</code> can have. */
  private final transient List<String> members = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntitlementEntity</code>.
   */
  AdditionalAttributeEntity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   members
  /**
   ** Replaces the existing list of members with the value supplied.
   **
   ** @param  value            the {@link List} of member user names.
   **                          <br>
   **                          Allowed object is {@link List} of
   **                          {@link String}s.
   **
   ** @return                  the <code>AttributeValueEntity</code> to allow
   **                          method chaining.
   **                          <br>
   **                          Possible object is
   **                          <code>AttributeValueEntity</code>.
   */
  public final AdditionalAttributeEntity members(List<String> value) {
    this.members.clear();
    return addMembers(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMembers
  /**
   ** Appends the existing list of members with the value supplied.
   **
   ** @param  value            the {@link List} of member user names.
   **                          <br>
   **                          Allowed object is {@link List} of
   **                          {@link String}s.
   **
   ** @return                  the <code>AttributeValueEntity</code> to allow
   **                          method chaining.
   **                          <br>
   **                          Possible object is
   **                          <code>AttributeValueEntity</code>.
   */
  public final AdditionalAttributeEntity addMembers(List<String> value) {
    if (value != null) {
      this.members.addAll(value);
    }
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   addMember
  /**
   ** Adds a single member to the members list.
   **
   ** @param  value            the {@link String} user name of the member to
   **                          be added to the list.
   **                          <br>
   **                          Allowed object is {@link List} of
   **                          {@link String}s.
   **
   ** @return                  the <code>AttributeValueEntity</code> to allow
   **                          method chaining.
   **                          <br>
   **                          Possible object is
   **                          <code>AttributeValueEntity</code>.
   */
  public final AdditionalAttributeEntity addMember(String value) {
    this.members.add(value);
    return this;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   members
  /**
   ** Retrieves the list of members.
   **
   ** @return                  the <code>AttributeValueEntity</code> to allow
   **                          method chaining.
   **                          <br>
   **                          Possible object is
   **                          <code>AttributeValueEntity</code>.
   */
  public final List<String> members() {
    return this.members;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the value of a specific property.
   **
   ** @param  id                 the identifier of the attribute to set
   **                            <code>Attribute Data</code> for.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the attribute to set as
   **                            <code>Attribute Data</code>.
   **                            <br>
   **                            Possible object is {@link Object}.
   */
  public final Object value(final String id) {
    return get(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute {@link Map} of the <code>EntitlementEntity</code>
   ** related to the source the <code>EntitlementEntity</code> is loaded from.
   **
   ** @return                    the attribute {@link Map} of the
   **                            <code>EntitlementEntity</code> related to the
   **                            source the <code>EntitlementEntity</code> is
   **                            loaded from.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  public final Map<String, Object> attribute() {
    return this.attribute;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  size (Map)
  /**
   ** Returns the number of key-value mappings in the attribute map. If the
   ** attribute map contains more than <code>Integer.MAX_VALUE</code>
   ** elements, returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return                    the number of key-value mappings in the
   **                            attribute map.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override public int size() {
    return (this.attribute != null) ? this.attribute.size() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  isEmpty (Map)
  /**
   ** Returns <code>true</code> if the attribute map contains no key-value
   ** mappings.
   **
   ** @return                    <code>true</code> if the attribute map contains
   **                            no key-value mappings.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override public boolean isEmpty() {
    return (this.attribute != null) ? this.attribute.isEmpty() : true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  containsKey (Map)
  /**
   ** Returns <code>true</code> if the attribute map contains a mapping for
   ** the specified key. More formally, returns <code>true</code> if and only
   ** if the attribute map contains a mapping for a key <code>k</code> such
   ** that <code>(key==null ? k==null : key.equals(k))</code>.
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
  public boolean containsKey(final Object key) {
    return (this.attribute != null) ? this.attribute.containsKey(key) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  containsValue (Map)
  /**
   ** Returns <code>true</code> if the attribute map maps one or more keys to
   ** the specified value. More formally, returns <code>true</code> if and
   ** only if the attribute map contains at least one mapping to a value
   ** <code>v</code> such that
   ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
   ** will probably require time linear in the map size for most
   ** implementations of the <code>Map</code> interface.
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:  get (Map)
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
  public Object get(final Object key) {
    return (this.attribute != null) ? this.attribute.get(key) : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  put (Map)
  /**
   ** Associates the specified value with the specified key in the attribute
   ** map (optional operation). If the attribute map previously contained a
   ** mapping for this key, the old value is replaced by the specified value.
   ** (A map <code>m</code> is said to contain a mapping for a key
   ** <code>k</code> if and only if
   ** {@link #containsKey(Object) m.containsKey(k)} would return
   ** <code>true</code>.))
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:  remove (Map)
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
   ** @param  key                  key whose mapping is to be removed from the
   **                              map.
   **                              <br>
   **                              Allowed object is {@link Object}.
   **
   ** @return                      previous value associated with specified key,
   **                              or <code>null</code> if there was no mapping
   **                              for key.
   **                              <br>
   **                              Possible object is {@link Object}.
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
  // Method:  putAll (Map)
  /**
   ** Copies all of the mappings from the specified map to the attribute map
   ** (optional operation). The effect of this call is equivalent to that of
   ** calling {@link #put(String,Object) put(k, v)} on the attribute map once
   ** for each mapping from key <code>k</code> to value <code>v</code> in the
   ** specified map. The behavior of this operation is unspecified if the
   ** specified map is modified while the operation is in progress.
   **
   ** @param  t                  the mappings to be stored in the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
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

  //////////////////////////////////////////////////////////////////////////////
  // Method:  clear (Map)
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
  // Method:  keySet (Map)
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
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  @Override
  public Set<String> keySet() {
    return (this.attribute != null) ? this.attribute.keySet() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  values (Map)
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
   ** @return                    a collection view of the values contained in
   **                            the attribute map.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Object} as the value.
   */
  @Override
  public Collection<Object> values() {
    return (this.attribute != null) ? this.attribute.values() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  entrySet (Map)
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
   ** @return                    a set view of the mappings contained in the
   **                            attribute map.
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is of type {@link Map.Entry}.
   */
  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return (this.attribute != null) ? this.attribute.entrySet() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  hashCode (Map)
  /**
   ** Returns the hash code value for the attribute map. The hash code of a
   ** map is defined to be the sum of the hashCodes of each entry in the map's
   ** entrySet view. This ensures that <code>t1.equals(t2)</code> implies that
   ** <code>t1.hashCode()==t2.hashCode()</code> for any two maps
   ** <code>t1</code> and <code>t2</code>, as required by the general contract
   ** of Object.hashCode.
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
  public int hashCode() {
    return (this.attribute != null) ? this.attribute.hashCode() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  equals (Map)
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
   ** @param  other              object to be compared for equality with the
   **                            attribute map.
   **                            <br>
   **                            Possible object is {@link Object}.
   **
   ** @return                    <code>true</code> if the specified object is
   **                            equal to the attribute map.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    return (this.attribute != null) ? this.attribute.equals(other) : false;
  }
}