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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   Entity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain.role;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

////////////////////////////////////////////////////////////////////////////////
// class Entity
// ~~~~~ ~~~~~~
/**
 ** Java class for entity complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entity"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="id" use="required" type="{http://service.api.oia.iam.ots/base}token"/&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlType(name="entity")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Policy.Resource.Entitlements.Entitlement.class, Policy.Resource.Entitlements.class, Role.class})
public class Entity implements Comparable<Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  protected String id;

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
  public Entity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Entity</code> with the specified id.
   **
   ** @param  id                 the id of this <code>Entity</code>.
   */
  public Entity(final String id) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId
  /**
   ** Sets the value of the id property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public final void setId(String value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Returns the value of the id property.
   **
   ** @return                    possible object is {@link String}.
   */
  public final String getId() {
    return id;
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
    return this.id.compareTo(other.id);
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
    return this.id.hashCode();
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
      return (this.hashCode() == ((Entity)other).hashCode());
    }
    return super.equals(other);
  }
}