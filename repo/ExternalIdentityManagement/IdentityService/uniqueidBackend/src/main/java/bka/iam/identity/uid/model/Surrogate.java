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
    Subsystem   :   Unique Identifier Assembler

    File        :   Surrogate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Surrogate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.model;

import java.util.Objects;
import java.util.Comparator;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;

import javax.validation.constraints.Size;
import javax.validation.constraints.Digits;

import bka.iam.identity.jpa.provider.Base;

////////////////////////////////////////////////////////////////////////////////
// class Surrogate
// ~~~~~ ~~~~~~~~~
/**
 ** The <code>Surrogate</code> database resources.
 ** <p>
 ** The object intentionally used to provided uniqueness of an identifier.
 ** <pre>
 **   Einführung einer personenbezogenen ID im Progamm Polizei 2020
 **   Unique Identifier (UID)
 **   Version 1.2 | Stand 15.03.2022
 **
 **   Reference to Page 21 Table 2
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Entity(name=Surrogate.NAME)
@Table(name=Surrogate.ENTITY)
@IdClass(Surrogate.Identifier.class)
public class Surrogate extends Base<Surrogate.Identifier> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The name of the logical entity. */
  public static final String NAME             = "Surrogate";

  /** The name of the persistent entity. */
  public static final String ENTITY           = "uit_identifiers";

  /** The string pattern to for a full qualified unique identifier. */
  public static final String FORMAT           = "%s-%s-%s";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:1547957836142146302")
  private static final long  serialVersionUID = 6247762067866707393L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Id
  @Column(name="tnt_id", nullable=false, unique=true, updatable=false, length=20)
  private String  tenant;

  @Id
  @Size(min=3, max=3)
  @Digits(integer=3, fraction=0)
  @Column(name="typ_id", nullable=false, unique=true, updatable=false, length=3)
  private String  type;

  @Id
  @Size(min=5, max=11)
  @Column(name="ext_id", nullable=false, unique=true, updatable=false, length=11)
  private String  external;

  @Column(name="state", nullable=false)
  private Integer state;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum State
  // ~~~~ ~~~~~
  /**
   ** Value descriptor of the surrogate state.
   */
  public enum State {
      /** The value of a deactivated/deleted surrogate */
      DELETED(0)
      /** The value of a generated surrogate */
    , GENERATED(1)
      /** The value of a registred surrogate */
    , REGISTERED(2)
      /** The value of a <code>I don't know anymore</code> surrogate */
    , FORWHATWASTHIS(4)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final int value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>State</code> that allows use as a JavaBean.
     **
     ** @param  value            the value representation of the
     **                          <code>State</code>.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    State(final int value) {
      // initailize instance attributes
      this.value = value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Attribute
  // ~~~~ ~~~~~~~~~
  /**
   ** Attribute descriptor of the database entity.
   ** <br>
   ** <b>Note</b>:
   ** <br>
   ** Only database server managed attributes enlisted.
   */
  public enum Attribute {
      /** The name of the identifying attribute. */
      ID(Base.ID)
      /** The state attribute. */
    , STATE("state")
      /** The account type attribute. */
    , TYPE("type")
      /** The tenant to which the surrogate belongs. */
    , TENANT("tenant")
      /** The account identifier attribute. */
    , EXTERNAL("external")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    public final String  id;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
     **
     ** @param  id               the value representation of the
     **                          <code>Attribute</code> identifier.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Attribute(final String id) {
      // initailize instance attributes
      this.id = id;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Identifier
  // ~~~~~ ~~~~~~~~~~
  /**
   ** The composite primary identifier
   */
  public static class Identifier implements Serializable
                                 ,          Comparable<Identifier> {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-2738568986722037598")
    private static final long serialVersionUID = -22034213464498942L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String  tenant;
    private String  type;
    private String  external;

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
     ** @param  tenant           the <code>tenant</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  type             the <code>type</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  external         the <code>external</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public Identifier(final String tenant, final String type, final String external) {
      // ensure inheritance
      super();

      // initialize instance sttributes
      this.tenant   = tenant;
      this.type     = type;
      this.external = external;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setTenant
    /**
     ** Sets the <code>tenant</code> property of the <code>Identifier</code>.
     **
     ** @param  value            the <code>tenant</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public final void setTenant(final String value) {
      this.tenant = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTenant
    /**
     ** Returns the <code>tenant</code> property of the <code>Identifier</code>.
     **
     ** @return                  the <code>tenant</code> property of the
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String getTenant() {
      return this.tenant;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setType
    /**
     ** Sets the <code>type</code> property of the <code>Identifier</code>.
     **
     ** @param  value            the <code>type</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public final void setType(final String value) {
      this.type = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the <code>type</code> property of the <code>Identifier</code>.
     **
     ** @return                  the <code>type</code> property of the
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setExternal
    /**
     ** Sets the <code>external</code> property of the <code>Identifier</code>.
     **
     ** @param  value            the <code>external</code> property of the
     **                          <code>Identifier</code> to set.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    public final void setExternal(final String value) {
      this.external = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getExternal
    /**
     ** Returns the <code>external</code> property of the 
     ** <code>Identifier</code>.
     **
     ** @return                  the <code>external</code> property of the
     **                          <code>Identifier</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String getExternal() {
      return this.external;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:  compareTo (Comparable)
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
      return Comparator.comparing((Identifier i) -> i.tenant).thenComparing(i -> i.type).thenComparing(i -> i.external).compare(this, other);
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
      return Objects.hash(this.tenant, this.type, this.external);
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
      return Objects.equals(this.tenant,   that.tenant)
          && Objects.equals(this.type,     that.type)
          && Objects.equals(this.external, that.external)
      ;
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
      return String.format(FORMAT, this.tenant, this.type, this.external);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Surrogate</code> database resource that allows
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
  public Surrogate() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a minimal <code>Surrogate</code> database resource.
   **
   ** @param  tenant             the <code>tenant</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the <code>type</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  external           the <code>external</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Surrogate(final String tenant, final String type, final String external) {
    // ensure inheritance
    super();

    // initialize instance sttributes
    this.tenant   = tenant;
    this.type     = type;
    this.external = external;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setTenant
  /**
   ** Sets the <code>tenant</code> property of the <code>Surrogate</code>.
   **
   ** @param  value              the <code>tenant</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setTenant(final String value) {
    this.tenant = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTenant
  /**
   ** Returns the <code>tenant</code> property of the <code>Surrogate</code>.
   **
   ** @return                    the <code>tenant</code> property of the
   **                            <code>Surrogate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getTenant() {
    return this.tenant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setType
  /**
   ** Sets the <code>type</code> property of the <code>Surrogate</code>.
   **
   ** @param  value              the <code>type</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setType(final String value) {
    this.type = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getType
  /**
   ** Returns the <code>type</code> property of the <code>Surrogate</code>.
   **
   ** @return                    the <code>type</code> property of the
   **                            <code>Surrogate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getType() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExternal
  /**
   ** Sets the <code>external</code> property of the <code>Surrogate</code>.
   **
   ** @param  value              the <code>external</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setExternal(final String value) {
    this.external = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExternal
  /**
   ** Returns the <code>external</code> property of the <code>Surrogate</code>.
   **
   ** @return                    the <code>external</code> property of the
   **                            <code>Surrogate</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getExternal() {
    return this.external;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setState
  /**
   ** Sets the <code>state</code> property of the <code>Surrogate</code>.
   **
   ** @param  value              the <code>state</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   */
  public final void setState(final Integer value) {
    this.state = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getState
  /**
   ** Returns the <code>state</code> property of the <code>Surrogate</code>.
   **
   ** @return                    the <code>state</code> property of the
   **                            <code>Surrogate</code>.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer getState() {
    return this.state;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setId (Identifiable)
  /**
   ** Sets the <code>tenant</code>, <code>type</code> and <code>external</code>
   ** properties of the <code>Surrogate</code>.
   **
   ** @param  value              the identifiyng properties of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   */
  @Override
  public final void setId(final Identifier value) {
    this.tenant   = value.tenant;
    this.type     = value.type;
    this.external = value.external;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId (Identifiable)
  /**
   ** Returns the identifiyng properties of the <code>Surrogate</code>.
   **
   ** @return                    the identifiyng properties of the
   **                            <code>State</code>.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   */
  @Override
  public final Identifier getId() {
    return new Identifier(this.tenant, this.type, this.external);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Surrogate</code> database
   ** resource.
   **
   ** @param  tenant             the <code>tenant</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  type               the <code>type</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  external           the <code>external</code> property of the
   **                            <code>Surrogate</code> to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Surrogate</code> populated with the
   **                            given <code>id</code>.
   **                            <br>
   **                            Possible object is <code>Surrogate</code>.
   */
  public static Surrogate build(final String tenant, final String type, final String external) {
    return new Surrogate(tenant, type, external);
  }
}