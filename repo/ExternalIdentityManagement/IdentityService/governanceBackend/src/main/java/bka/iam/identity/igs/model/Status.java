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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Identity Governance Backend

    File        :   Status.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Status.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.model;

import java.util.Objects;

import java.io.Serializable;

import javax.persistence.Entity;

////////////////////////////////////////////////////////////////////////////////
// class Status
// ~~~~~ ~~~~~~
/**
 ** The identifier <code>Status</code> virtual resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=Status.NAME)
@SuppressWarnings("oracle.jdeveloper.ejb.entity-class-auto-id-gen")
public class Status implements Serializable
                    ,          Comparable<Status> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME            = "Status";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:5364524895179064469")
  private static final long serialVersionUID = -7421831337579238673L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Integer  state;
  private String   name;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Status</code> virtual resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public Status() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a virtual <code>Status</code> datbase resource with the
   ** properties specified.
   **
   ** @param  state              the <code>state</code> property of the
   **                            <code>Status</code> to set.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   ** @param  name               the name for the <code>state</code>
   **                            property for the <code>Status</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public Status(final Integer state, final String name) {
    // ensure inheritance
    super();
    
    // initialize instance attributes
    this.state = state;
    this.name  = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setState
  /**
   ** Sets the <code>state</code> property of the <code>Status</code>.
   **
   ** @param  value              the <code>state</code> property of the
   **                            <code>Status</code> to set.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public final void setState(final Integer value) {
    this.state = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getState
  /**
   ** Returns the <code>state</code> property of the <code>Status</code>.
   **
   ** @return                    the <code>state</code> property of the
   **                            <code>Status</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer getState() {
    return this.state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the <code>name</code> property of the <code>Status</code>.
   **
   ** @param  value              the <code>name</code> property of the
   **                            <code>Status</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the <code>name</code> property of the <code>Status</code>.
   **
   ** @return                    the <code>name</code> property of the
   **                            <code>Status</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

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
  public final int compareTo(final Status other) {
    return this.state.compareTo(other.state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

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
  @Override
  public int hashCode() {
    return Objects.hash(this.state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Status</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Status</code> may be different even though they contain the same set
   ** of names with the same values, but in a different order.
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
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Status that = (Status)other;
    return Objects.equals(this.state, that.state);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (Persistable)
  /**
   ** Returns the string representation for the <code>Base</code> entity in its
   ** minimal form, without any additional whitespace.
   **
   ** @return                    a string representation that represents this
   **                            <code>Base</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    return this.name;
  }
}