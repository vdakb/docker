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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager OAuth Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Bearer.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    Bearer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.platform.oauth;

import java.util.Set;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;

///////////////////////////////////////////////////////////////////////////////
// class Bearer
// ~~~~~ ~~~~~~
/**
 ** Class that contains a result of the Authorization Flow including a access
 ** token.
 ** <p>
 ** Some of the properties are standardized by the OAuth 2 specification and
 ** therefore the class contains getters that extract these properties.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Bearer implements Map<String, Object> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the access properties */
  private final Map<String, Object> delegate;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** The OAuth attribute constraints of an token.
   */
  public enum Attribute {
      /**
       ** The property name of the access token type issued by the authorization
       ** server as described in
       ** <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-7.1">Access Token Types</a>.
       */
      TYPE("token_type")
      /**
       ** The property name of the access token issued by the authorization
       ** server.
       */
    , ACCESS("access_token")
      /**
       ** The property name of the refresh token issued by the authorization
       ** server.
       */
    , REFRESH("refresh_token")
      /**
       ** The property name of the lifetime in seconds of the access token (for
       ** example 3600 for an hour).
       */
    , EXPIRY("expires_in")
      /**
       ** The property name of the token scope issued by the authorization server
       ** as described in
       ** <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-3.3">Access Token Scope</a>.
       */
    , SCOPE("scope")
      /**
       ** The property name of the Just-In-Time provisionig capabilities of the
       ** token.
       */
    , JTI("jti")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Attribute</code> with a constraint value.
     **
     ** @param  value            the constraint name (used in OAuth schemas) of
     **                          the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Attribute(final String value) {
      this.id = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Bearer</code> initiated from the property map.
   **
   ** @param  origin             the access token properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   */
  private Bearer(final Map<String, Object> origin) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.delegate = origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the returned access token.
   ** <br>
   ** Type is in most cases <code>bearer</code> (no cryptography is used) but
   ** provider might support also other kinds of token like <code>mac</code>.
   **
   ** @return                    the token type.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String type() {
    return String.valueOf(get(Attribute.TYPE.id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accessToken
  /**
   ** Returns the access token.
   **
   ** @return                    the access token.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String accessToken() {
    return String.valueOf(get(Attribute.ACCESS.id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   refreshToken
  /**
   ** Returns the refresh token.
   ** <br>
   ** <b>Note</b>:
   ** The refresh token must not be issued during the authorization flow. Some
   ** Service Providers issue refresh token only on first user authorization and
   ** some providers does not support refresh token at all and authorization
   ** flow must be always performed when token expires.
   **
   ** @return                    the refresh token or <code>null</code> if the
   **                            value is not provided.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String refreshToken() {
    return String.valueOf(get(Attribute.REFRESH.id));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   expirationTime
  /**
   ** Returns expiration time of the {@link #accessToken() access token} in
   ** seconds.
   **
   ** @return                    the expiration time in seconds or
   **                            <code>null</code> if the value is not provided.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer expirationTime() {
    final String value = String.valueOf(get(Attribute.EXPIRY.id));
    return (value == null)  ? null : Integer.valueOf(value);
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
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean isEmpty() {
    return this.delegate.isEmpty();
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
   **                            <br>
   **                            Allowed object is {@link String}.
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
  public Object put(final String key, final Object value) {
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
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return previous           value associated with specified key, or
   **                            <code>null</code> if there was no mapping for
   **                            key.
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
    return this.delegate.remove(key);
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
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
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
    this.delegate.putAll(t);
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
   **                            Possible object is {@link Set} where each
   **                            element is a {@link String}.
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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link Object}.
   */
  @Override
  public Collection<Object> values() {
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
   **                            <br>
   **                            Possible object is {@link Set} where each
   **                            element is a {@link Map.Entry}.
   */
  @Override
  public Set<Map.Entry<String, Object>> entrySet() {
    return this.delegate.entrySet();
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @see    Object#equals(Object)
   ** @see    Object#hashCode()
   ** @see    #equals(Object)
   */
  @Override
  public int hashCode() {
    return this.delegate.hashCode();
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
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (other instanceof Bearer) {
      final Bearer that = (Bearer)other;
      return this.delegate.equals(that.delegate);
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a OAuth <code>Bearer</code> token.
   **
   ** @param  origin             the access token properties.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is a {@link String} mapped to a
   **                            {@link Object}.
   **
   ** @return                    the created create a OAuth <code>Bearer</code>
   **                            token.
   **                            <br>
   **                            Possible object is <code>Bearer</code>.
   */
  public static Bearer build(final Map<String, ?> origin) {
    return new Bearer(Collections.unmodifiableMap(origin == null ? Collections.<String, Object>emptyMap() : origin));
  }
}