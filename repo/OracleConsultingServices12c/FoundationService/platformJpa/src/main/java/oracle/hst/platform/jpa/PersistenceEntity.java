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

    System      :   Presistence Foundation Shared Library
    Subsystem   :   Generic Persistence Interface

    File        :   PersistenceEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    PersistenceEntity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa;

import java.util.Objects;

import java.io.Serializable;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

////////////////////////////////////////////////////////////////////////////////
// abstract class PersistenceEntity
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A generic persistent entity which is invoked to process data exchanges.
 **
 ** @param  <I>                  the type of the identifiying value
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@MappedSuperclass
@EntityListeners(PersistenceLifeCycle.class)
@SuppressWarnings("oracle.jdeveloper.java.annotation-callback")
public abstract class PersistenceEntity<I extends Comparable<I> & Serializable> implements Identifiable<I> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6271641406410304876")
  private static final long serialVersionUID = 4039537170667936481L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>PersistenceEntity</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public PersistenceEntity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Persistable)
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
  @Override
  public int hashCode() {
    return (getId() != null) ? Objects.hash(getId()) : super.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>PersistenceEntities</code> are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>PersistenceEntities</code> may be different even though they contain
   ** the same set of names with the same values, but in a different order.
   **
   ** @param  other            the reference object with which to compare.
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
    final I       tid = getId();
    final I       oid = ((PersistenceEntity<I>)other).getId();
    final boolean tin = getClass().isInstance(other);
    final boolean oin = other.getClass().isInstance(this);
    return (tid != null && tin && oin ? tid.equals(oid) : (other == this));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Returns the string representation for the <code>PersistenceEntity</code>
   ** in its minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            <code>PersistenceEntity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public final String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), (getId() != null) ? getId() : ("@" + hashCode()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:    compareTo (Comparable)
  /**
   ** Compares this object with the specified object <code>other</code> for
   ** order.
   ** <p>
   ** Returns a negative integer, zero, or a positive integer as this object is
   ** less than, equal to, or greater than the specified object.
   ** <p>
   ** The implementation ensures that
   ** <code>sgn(x.compareTo(y)) == -sgn(y.compareTo(x))</code> for all
   ** <code>x</code> and <code>y</code>.
   ** <p>
   ** The implementation also ensures that the relation is transitive:
   ** <code>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</code>
   ** implies <code>x.compareTo(z)&gt;0</code>.
   ** <p>
   ** Finally it is ensured that <code>x.compareTo(y)==0</code> implies that
   ** <code>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</code>, for all
   ** <code>z</code>.
   ** <p>
   ** In the foregoing description, the notation
   ** <code>sgn(</code><i>expression</i><code>)</code> designates the
   ** mathematical <i>signum</i> function, which is defined to return one of
   ** <code>-1</code>, <code>0</code>, or <code>1</code> according to whether
   ** the value of <i>expression</i> is negative, zero or positive.
   **
   ** @param  other              the object to be compared.
   **                            <br>
   **                            Allowed object is <code>I</code>.
   **
   ** @return                    a negative integer, zero, or a positive integer
   **                            as this object is less than, equal to, or
   **                            greater than the specified object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   **
   ** @throws ClassCastException if the specified object's type prevents it
   **                             from being compared to this object.
   */
  @Override
  public final int compareTo(final I other) {
    return (other == null) ? -1 : getId() == null ? 1 : getId().compareTo(other);
  }
}