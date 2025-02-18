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

    File        :   UserRole.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserRole.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.igs.model;

import java.util.Objects;
import java.util.Comparator;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import bka.iam.identity.jpa.provider.Base;

////////////////////////////////////////////////////////////////////////////////
// class UserRole
// ~~~~~ ~~~~~~~~
/**
 ** The <code>UserRole</code> database resources.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The relation declared to {@link User} and {link Role} must not be annotated
 ** with any <i>CascadeType</i> or <i>orphanRemoval</i>.
 ** <p>
 ** <b>Important</b>:
 ** <br>
 ** The counterpart of the relation declared in {@link User} and {link Role}
 ** must be annotated with <i>CascadeType.REMOVE</i> and
 ** <i>orphanRemoval=true</i>. That's the only way to delete orphaned entities
 ** from the persistence layer. An entity that is no longer attached to its
 ** parent is the definition of being an orphan.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=UserRole.NAME)
@Table(name=UserRole.ENTITY)
@IdClass(UserRole.Identifier.class)
public class UserRole extends Base<UserRole.Identifier> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity */
  public static final String NAME             = "UserRole";

  /** The name of the entity */
  public static final String ENTITY           = "igt_userroles";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7946802391991391387")
  private static final long  serialVersionUID = -5827996324177190889L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @ManyToOne
  @JoinColumn(name="usr_id", nullable=false, updatable=false)
  private User  user;

  @Id
  @ManyToOne
  @JoinColumn(name="rol_id", nullable=false, updatable=false)
  private Role  role;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Identifier
  // ~~~~~ ~~~~~~~~~~
  /**
   ** The composite primary identifier of a user role relationship
   */
  public static class Identifier implements Serializable
                                 ,          Comparable<Identifier> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:2906416149507481024")
    private static final long serialVersionUID = -4745533296410105361L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private Long    user;
    private String  role;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Identifier</code> database resource that
     ** allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Identifier() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a minimal <code>Identifier</code> database resource.
     **
     ** @param  user             the <code>user</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link Long}.
     ** @param  role             the <code>role</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Identifier(final Long user, final String role) {
      // ensure inheritance
      super();

      // initialize instance sttributes
      this.user = user;
      this.role = role;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: user
    /**
     ** Sets the <code>user</code> property of the <code>Identifier</code>.
     **
     ** @param  value            the <code>user</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link Long}.
     **
     ** @return                  the <code>Identifier</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is<code>Identifier</code>.
     */
    public final Identifier user(final Long value) {
      this.user = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: user
    /**
     ** Returns the <code>user</code> property of the <code>Identifier</code>.
     **
     ** @return                  the <code>user</code> property of the
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link Long}.
     */
    public final Long user() {
      return this.user;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: role
    /**
     ** Sets the <code>role</code> property of the <code>Identifier</code>.
     **
     ** @param  value            the <code>role</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Identifier</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is<code>Identifier</code>.
     */
    public final Identifier role(final String value) {
      this.role = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: role
    /**
     ** Returns the <code>role</code> property of the <code>Identifier</code>.
     **
     ** @return                  the <code>role</code> property of the
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String role() {
      return this.role;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: compareTo (Comparable)
    /**
     ** Compares this object with the specified object <code>other</code> for
     ** order.
     ** <p>
     ** Returns a negative integer, zero, or a positive integer as this object
     ** is less than, equal to, or greater than the specified object.
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
     ** @param  other            the object to be compared.
     **                          <br>
     **                          Allowed object is <code>Identifier</code>.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as this object is less than, equal to, or
     **                          greater than the specified object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     **
     ** @throws ClassCastException if the specified object's type prevents it
     **                             from being compared to this object.
     */
    @Override
    public final int compareTo(final Identifier other) {
      return Comparator.comparing((Identifier i) -> i.user).thenComparing(i -> i.role).compare(this, other);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode
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
     */
    @Override
    public int hashCode() {
      return Objects.hash(this.user, this.role);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Identifier</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Identifier</code>s may be different even though they contain the
     ** same set of names with the same values, but in a different order.
     **
     ** @param  other          the reference object with which to compare.
     **                        <br>
     **                        Allowed object is {@link Object}.
     **
     ** @return                <code>true</code> if this object is the same as
     **                        the object argument; <code>false</code>
     **                        otherwise.
     **                        <br>
     **                        Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Identifier that = (Identifier)other;
      return Objects.equals(this.user, that.user) && Objects.equals(this.role, that.role);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this <code>Identifier</code>.
     ** <p>
     ** The string representation consists of a list of the set's elements in
     ** the order. Adjacent elements are separated by the characters
     ** <code>"-"</code> (dash). Elements are converted to strings as by
     ** <code>Object.toString()</code>.
     **
     ** @return                  a string representation of this
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      return String.format("%s-%s", this.user, this.role);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>UserRole</code> database resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** Do not remove this constructor; its required for serialization!
   */
  public UserRole() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>UserRole</code> database resource.
   **
   ** @param  user               the <code>user</code> property of the
   **                            <code>Identifier</code> to set.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  role               the <code>role</code> property of the
   **                            <code>Identifier</code> to set.
   **                            <br>
   **                            Allowed object is {@link Role}.
   */
  private UserRole(final User user, final Role role) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.user = user;
    this.role = role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUser
  /**
   ** Sets the <code>user</code> property of the <code>UserRole</code>.
   **
   ** @param  value              the <code>user</code> property of the
   **                            <code>UserRole</code> to set.
   **                            <br>
   **                            Allowed object is {@link User}.
   */
  public final void setUser(final User value) {
    this.user = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUser
  /**
   ** Returns the <code>user</code> property of the <code>UserRole</code>.
   **
   ** @return                    the <code>user</code> property of the
   **                            <code>UserRole</code>.
   **                            <br>
   **                            Possible object is {@link User}.
   */
  public final User getUser() {
    return this.user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRole
  /**
   ** Sets the <code>role</code> property of the <code>UserRole</code>.
   **
   ** @param  value              the <code>role</code> property of the
   **                            <code>UserRole</code> to set.
   **                            <br>
   **                            Allowed object is {@link Role}.
   */
  public final void setRole(final Role value) {
    this.role = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRole
  /**
   ** Returns the <code>role</code> property of the <code>UserRole</code>.
   **
   ** @return                    the <code>role</code> property of the
   **                            <code>UserRole</code>.
   **                            <br>
   **                            Possible object is {@link Role}.
   */
  public final Role getRole() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId (Identifiable)
  /**
   ** Sets the <code>user</code> and <code>role</code> properties of the
   ** <code>UserRole</code>.
   **
   ** @param  value              the identifiyng properties of the
   **                            <code>UserRole</code> to set.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   */
  @Override
  public final void setId(final Identifier value) {
    this.user = User.build(value.user);
    this.role = Role.build(value.role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId (Identifiable)
  /**
   ** Returns the identifiyng properties of the <code>UserRole</code>.
   **
   ** @return                    the identifiyng properties of the
   **                            <code>UserRole</code>.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   */
  @Override
  public final Identifier getId() {
    return new Identifier(this.user.getId(), this.role.getId());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>UserRole</code> database
   ** resource.
   **
   ** @param  user               the <code>user</code> property of the
   **                            <code>Identifier</code> to set.
   **                            <br>
   **                            Allowed object is {@link User}.
   ** @param  role               the <code>role</code> property of the
   **                            <code>Identifier</code> to set.
   **                            <br>
   **                            Allowed object is {@link Role}.
   **
   ** @return                    the <code>UserRole</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>UserRole</code>.
   */
  public static UserRole build(final User user, final Role role) {
    return new UserRole(user, role);
  }
}