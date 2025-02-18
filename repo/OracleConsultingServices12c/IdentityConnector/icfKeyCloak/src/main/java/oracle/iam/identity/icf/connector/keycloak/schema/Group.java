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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   Group.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Group.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.rest.utility.MappedStringListDeserializer;
import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class Group
// ~~~~~ ~~~~~ ~~~~~
/**
 ** The base REST group entity representation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.GROUP)
@JsonIgnoreProperties("subGroupCount")
public final class Group extends Structural<Group> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unqiue identifier of the resource defined by a Service Provider
   ** administrator.
   */
  @JsonProperty
  @Attribute(required=true)
  private String                     name;
  /**
   ** The path of the resource defined by a Service Provider administrator.
   */
  @Attribute
  @JsonProperty
  private String                    path;
  /**
   ** The path of the resource defined by a Service Provider administrator.
   */
  @JsonProperty("realmRoles")
  @Attribute(multiValueClass=String.class)
  private List<String>              realm;
  /**
   ** The path of the resource defined by a Service Provider administrator.
   */
  @Attribute
  @JsonProperty("clientRoles")
  @JsonDeserialize(using=MappedStringListDeserializer.class)
  private Map<String, List<String>> client;
  /**
   ** The groups that are part of this group.
   */
  @JsonProperty("subGroups")
  @Attribute(multiValueClass=Credential.class)
  private List<Group>               sub;
  /**
   ** The collection of access permissions.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** <code>access</code> is a complex attribute, so it is represented by the
   ** {@link Access} class.
   */
  @Attribute
  @JsonProperty
  private Access      access;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////////
  // final class Access
  // ~~~~~ ~~~~~ ~~~~~~
  /**
   ** The REST group access entity representation.
   */
  @Schema(Keycloak.ENTITY_ACCESS)
  public static final class Access implements Resource<Access>
                                   ,          Map<Access.Permission, Boolean> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** The tag-value pairs of the <code>Access</code> permissions.
     ** <p>
     ** Implemented as {@link LinkedHashMap} to keep the order of occurence.
     */
    private Map<Permission, Boolean> delegate = new LinkedHashMap<Permission, Boolean>();

    ////////////////////////////////////////////////////////////////////////////
    // Member classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // enum Permission
    // ~~~~ ~~~~~~~~~~
    /**
     ** The canonical values of a required user access permission.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Be carefull in changing the Enum names. By default, Jackson will use the
     ** Enum name to deserialize from JSON.
     ** <p>
     ** The <code>id</code> wrapped in this Enum is the suffix of the layered
     ** attribute passed through ICF Framwork.
     */
    public enum Permission {
        view("view")
      , manage("manage")
      , viewMembers("viewMembers")
      , manageMembers("manageMembers")
      , manageMembership("manageMembership")
      ;

      //////////////////////////////////////////////////////////////////////////
      // instance attributes
      //////////////////////////////////////////////////////////////////////////

      public final String id;

      //////////////////////////////////////////////////////////////////////////
      // Constructors
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: Ctor
      /**
       ** Constructor for <code>Permission</code> with a constraint value.
       **
       ** @param  id             the constraint name (used in REST schemas) of
       **                        the object.
       **                        <br>
       **                        Allowed object is {@link String}.
       */
      Permission(final String id) {
        this.id = id;
      }

      //////////////////////////////////////////////////////////////////////////
      // Methods group by functionality
      //////////////////////////////////////////////////////////////////////////

      //////////////////////////////////////////////////////////////////////////
      // Method: from
      /**
       ** Factory method to create a proper <code>Permission</code> constraint
       ** from the given string value.
       **
       ** @param  id             the string value the order constraint should be
       **                        returned for.
       **                        <br>
       **                        Allowed object is {@link String}.
       **
       ** @return                the <code>Permission</code> constraint.
       **                        <br>
       **                        Possible object is <code>Permission</code>.
       */
      public static Permission from(final String id) {
        for (Permission cursor : Permission.values()) {
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
     ** Constructs an empty <code>Access</code> REST representation that allows
     ** use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Access() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (Resource, Map)
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
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     ** @see    Object#equals(Object)
     ** @see    Object#hashCode()
     ** @see    #equals(Object)
     */
    @Override
    public int hashCode() {
      return this.delegate.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (Resource, Map)
    /**
     ** Compares the specified object with the attribute map for equality.
     ** Returns <code>true</code> if the given object is also a map and the two
     ** {@link Map}s represent the same mappings.
     ** <p>
     ** More formally, two maps <code>t1</code> and <code>t2</code> represent
     ** the same mappings if <code>t1.entrySet().equals(t2.entrySet())</code>.
     ** This ensures that the <code>equals</code> method works properly across
     ** different implementations of the <code>Map</code> interface.
     **
     ** @param  other            the object to be compared for equality with the
     **                          attribute map.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (other instanceof Access) {
        final Access that = (Access)other;
        return CollectionUtility.equals(this.delegate, that.delegate);
      }
      return false;
    }

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
    public final String toString() {
      return this.delegate == null ? "null" : this.delegate.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: size (Map)
    /**
     ** Returns the number of key-value mappings in the attribute map. If the
     ** attribute map contains more than <code>Integer.MAX_VALUE</code> elements,
     ** returns <code>Integer.MAX_VALUE</code>.
     **
     ** @return                  the number of key-value mappings in the
     **                          attribute map.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int size() {
      return this.delegate.size();
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
      return this.delegate.isEmpty();
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
      this.delegate.clear();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: keySet (Map)
    /**
     ** Returns a set view of the keys contained in the attribute map. The set
     ** is backed by the attribute map, so changes to attribute map are
     ** reflected in the set, and vice-versa. If attribute map is modified while
     ** an iteration over the set is in progress (except through the iterator's
     ** own <code>remove</code> operation), the results of the iteration are
     ** undefined. The set supports element removal, which removes the
     ** corresponding mapping from attribute map, via the
     ** <code>Iterator.remove</code>, <code>Set.remove</code>,
     ** <code>removeAll</code> <code>retainAll</code>, and <code>clear</code>
     ** operations. It does not support the add or <code>addAll</code>
     ** operations.
     **
     ** @return                  a set view of the keys contained in the
     **                          attribute map.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link Permission}.
     */
    @Override
    public Set<Permission> keySet() {
      return this.delegate.keySet();
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
     **                          Allowed object is {@link Collection} where each
     **                          element is of type {@link Boolean}.
     */
    @Override
    public Collection<Boolean> values() {
      return this.delegate.values();
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
     **                          Allowed object is {@link Map.Entry} where each
     **                          element is of type {@link Permission} for the
     **                          key and {@link Boolean} as the value.
     */
    @Override
    public Set<Map.Entry<Permission, Boolean>> entrySet() {
      return this.delegate.entrySet();
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
     **                          Allowed object is {@link String}.
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
      return this.delegate.containsKey(key);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: containsValue (Map)
    /**
     ** Returns <code>true</code> if the attribute map maps one or more keys to
     ** the specified value. More formally, returns <code>true</code> if and only
     ** if the attribute map contains at least one mapping to a value
     ** <code>v</code> such that
     ** <code>(value==null ? v==null : value.equals(v))</code>. This operation
     ** will probably require time linear in the map size for most implementations
     ** of the <code>Map</code> interface.
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
      return this.delegate.containsValue(value);
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
     **                          Possible object is {@link Boolean}.
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
    public Boolean get(final Object key) {
      return this.delegate.get(key);
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
     ** @param  key              the key with which the specified value is to be
     **                          associated.
     **                          <br>
     **                          Allowed object is {@link Permission}.
     ** @param  value            the value to be associated with the specified
     **                          key.
     **                          <br>
     **                          Allowed object is {@link Boolean}.
     **
     ** @return                  previous value associated with specified key,
     **                          or <code>null</code> if there was no mapping
     **                          for key. A <code>null</code> return can also
     **                          indicate that the map previously associated
     **                          <code>null</code> with the specified key, if
     **                          the implementation supports <code>null</code>
     **                          values.
     **                          <br>
     **                          Possible object is {@link Boolean}.
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
    public Boolean put(final Permission key, final Boolean value) {
      return this.delegate.put(key, value);
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
     ** associated <code>null</code> with the specified key if the
     ** implementation supports <code>null</code> values.) The map will not
     ** contain a mapping for the specified  key once the call returns.
     **
     ** @param  key              key whose mapping is to be removed from the
     **                          map.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return previous         value associated with specified key, or
     **                          <code>null</code> if there was no mapping for
     **                          key.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ClassCastException            if the key is of an inappropriate
     **                                       type for the attribute map
     **                                       (optional).
     ** @throws NullPointerException          if the key is <code>null</code>
     **                                       and the attribute map does not
     **                                       permit <code>null</code> keys
     **                                       (optional).
     ** @throws UnsupportedOperationException if the <code>remove</code> method
     **                                       is not supported by the attribute
     **                                       map.
     */
    @Override
    public Boolean remove(final Object key) {
      return this.delegate.remove(key);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: putAll (Map)
    /**
     ** Copies all of the mappings from the specified map to the attribute map
     ** (optional operation). The effect of this call is equivalent to that of
     ** calling {@link #put(Permission,Boolean) put(k, v)} on the attribute map
     ** once for each mapping from key <code>k</code> to value <code>v</code> in
     ** the specified map. The behavior of this operation is unspecified if the
     ** specified map is modified while the operation is in progress.
     **
     ** @param  t                the mappings to be stored in the attribute map.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link Permission} for the
     **                          key and {@link Boolean} as the value.
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
    public void putAll(final Map<? extends Permission, ? extends Boolean> t) {
      this.delegate.putAll(t);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Group</code> REST representation that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Group() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>Group</code>.
   **
   ** @param  value              the name of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Group</code>.
   **
   ** @return                    the name of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Sets the path of the <code>Group</code>.
   **
   ** @param  value              the path of the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group path(final String value) {
    this.path = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path of the <code>Group</code>.
   **
   ** @return                    the path of the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String path() {
    return this.path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
  /**
   ** Sets the collection of realm {@link Role}s belonging to the
   ** <code>Group</code>.
   **
   ** @param  value              the collection of realm {@link Role}s
   **                            belonging to the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group realm(final List<String> value) {
    this.realm = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   realm
  /**
   ** Returns the collection of realm {@link Role}s belonging to the
   ** <code>Group</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of realm {@link Role}s
   **                            belonging to the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> realm() {
    return this.realm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Sets the collection of client {@link Role}s belonging to the
   ** <code>Group</code>.
   **
   ** @param  value              the collection of client {@link Role}s
   **                            belonging to the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the
   **                            key and {@link List} as the value.
   **
   ** @return                    the <code>Group</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group client(final Map<String, List<String>> value) {
    this.client = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   client
  /**
   ** Returns the collection of client {@link Role}s belonging to the
   ** <code>Group</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of client {@link Role}s
   **                            belonging to the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the
   **                            key and {@link List} as the value.
   */
  public final  Map<String, List<String>> client() {
    return this.client;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sub
  /**
   ** Sets the collection of <code>Group</code>s belonging to the
   ** <code>Group</code>.
   **
   ** @param  value              the collection of <code>Group</code> belonging
   **                            to the <code>Group</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type <code>Group</code>.
   **
   ** @return                    the <code>Group</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group sub(final List<Group> value) {
    this.sub = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sub
  /**
   ** Returns the collection of <code>Group</code>s belonging to the
   ** <code>Group</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the collection of <code>Group</code> belonging
   **                            to the <code>Group</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type <code>Group</code>.
   */
  public final List<Group> sub() {
    return this.sub;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Sets the access permission of the <code>User</code>.
   **
   ** @param  value              the access permission of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link Access} which is a
   **                            {@link Map} where each element is of type
   **                            {@link String} for the key and {@link Boolean}
   **                            as the value.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Group</code>.
   */
  public final Group access(final Access value) {
    this.access = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   access
  /**
   ** Returns the access permission of the <code>User</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the access permission of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link Access} which is a
   **                            {@link Map} where each element is of type
   **                            {@link String} for the key and {@link Boolean}
   **                            as the value.
   */
  public final Access access() {
    return this.access;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.realm, this.client, this.sub);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Group</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Group</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
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
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Group that = (Group)other;
    return Objects.equals(this.id,     that.id)
        && Objects.equals(this.name,   that.name)
        && Objects.equals(this.realm,  that.realm)
        && Objects.equals(this.client, that.client)
        && Objects.equals(this.sub,    that.sub)
        && Objects.equals(this.access, that.access)
    ;
  }
}