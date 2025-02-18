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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Map;
import java.util.LinkedHashMap;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.xml.XMLSerialzable;

import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** The <code>Entity</code> act as generic wrapper for a object instance of
 ** Oracle Identity Manager.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entity"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="attribute" maxOccurs="unbounded" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;simpleContent&gt;
 **               &lt;extension base="&lt;http://www.oracle.com/schema/oim/offline&gt;token"&gt;
 **                 &lt;attribute name="id" use="required" type="{http://www.oracle.com/schema/oim/offline}token" /&gt;
 **               &lt;/extension&gt;
 **             &lt;/simpleContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="id" use="required" type="{http://www.oracle.com/schema/oim/offline}token" /&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public abstract class Entity implements XMLSerialzable
                             ,          Comparable<Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-424092317837580871")
  private static final long               serialVersionUID = 5026748363696628658L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The public name of this <code>Entity</code>. */
  protected String                        name;

  /** The internal system identifier of this <code>Entity</code>. */
  protected long                          key;

  /**
   ** The generic attribute mapping a <code>Entity</code> can have.
   */
  protected transient Map<String, Object> attribute        = new LinkedHashMap<String, Object>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Entity</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  protected Entity() {
    // ensure inheritance
    this(-1L, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> with the specified name but without a
   ** identifier.
   **
   ** @param  name               the identifying name of the <code>Entity</code>.
   */
  protected Entity(final String name) {
    // ensure inheritance
    this(-1L, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> with the specified key and name.
   **
   ** @param  key                the internal system identifier of the
   **                            <code>Entity</code> to load.
   ** @param  name               the name of the <code>Entity</code>.
   */
  protected Entity(final long key, final String name) {
    // ensure inheritance
    super();

    this.key  = key;
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   key
  /**
   ** Returns the internal system identifier of this <code>Entity</code>.
   **
   ** @return                    the public name of this <code>Entity</code>.
   */
  public final long key() {
    return this.key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the public name of this <code>Entity</code>.
   **
   ** @return                    the public name of this <code>Entity</code>.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the attribute {@link Map} of the <code>Entity</code> related to
   ** the source the <code>Entity</code> is loaded from.
   **
   ** @return                    the attribute {@link Map} of the
   **                            <code>Entity</code> related to the source the
   **                            <code>Entity</code> is loaded from.
   */
  public final Map<String, Object> attribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  public abstract String entity();

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
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **
   ** @throws ClassCastException if the specified object's type is not
   **                            an instance of <code>Entity</code>.
   */
  @Override
  public int compareTo(final Entity other) {
    return this.name.compareTo(other.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns the hash code value for this <code>Repository Object</code>.
   ** <p>
   ** This ensures that <code>s1.equals(s2)</code> implies that
   ** <code>s1.hashCode()==s2.hashCode()</code> for any two instances
   ** <code>s1</code> and <code>s2</code>, as required by the general contract
   ** of <code>Object.hashCode()</code>.
   **
   ** @return                    the hash code value for this
   **                            <code>Repository Object</code>.
   */
  public int hashCode() {
    return this.name.hashCode();
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
   ** <code>Entity</code>. If so, the hash code values of this nested set
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
  public boolean equals(Object other) {
    if (other instanceof Entity) {
      return this.name.equals(((Entity)other).name);
    }
    return super.equals(other);
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
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder(TaskBundle.format(TaskMessage.ENTITY_IDENTIFIER, entity(), this.name));
    if (!CollectionUtility.empty(this.attribute))
      builder.append(TaskBundle.format(TaskMessage.ENTITY_ATTRIBUTE, StringUtility.formatCollection(this.attribute)));
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Adds the specified <code>Attribute</code> to the {@link Map} of attributes
   ** that are provisioned to the <code>Entity</code>.
   ** <p>
   ** If the attribute mapping previously contained a mapping for the key, the
   ** old value is replaced by the specified value. (A map <code>m</code> is
   ** said to contain a mapping for a key <code>k</code> if and only if
   ** {@link Map#containsKey(Object) m.containsKey(k)} would return
   ** <code>true</code>.).
   **
   ** @param  code               the key with which the specified {@link Object}
   **                            is to be associated.
   ** @param  value              the {@link Object} to be associated with the
   **                            specified id.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object addAttribute(final String code, final String value) {
    return this.attribute.put(code, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Copies all of the mappings from the specified collection to the
   ** {@link Map} of attributes that are provisioned to the <code>Entity</code>.
   ** <p>
   ** The effect of this call is equivalent to that of calling
   ** {@link #addAttribute(String,String) addAttribute(k, v)} on this
   ** <code>Entity</code> once for each mapping from key <code>k</code> to value
   ** <code>v</code> in the specified map. The behavior of this operation is
   ** undefined if the specified map is modified while the operation is in
   ** progress.
   ** <p>
   ** If the attribute mapping previously contained a mapping for any key, the
   ** old value is replaced by the specified value. (A map <code>m</code> is
   ** said to contain a mapping for a key <code>k</code> if and only if
   ** {@link Map#containsKey(Object) m.containsKey(k)} would return
   ** <code>true</code>.).
   **
   ** @param  collection         appings to be stored in the {@link Map} of
   **                            attributes.
   */
  public final void addAttribute(final Map<String, Object> collection) {
    this.attribute.putAll(collection);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeAttribute
  /**
   ** Removes the specified <code>Attribute</code> from the {@link Map} of
   ** attributes that are provisioned to the <code>Entity</code>.
   **
   ** @param  code               the key with which the specified {@link Object}
   **                            is to be removed.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object removeAttribute(final String code) {
    return this.attribute.remove(code);
  }
}