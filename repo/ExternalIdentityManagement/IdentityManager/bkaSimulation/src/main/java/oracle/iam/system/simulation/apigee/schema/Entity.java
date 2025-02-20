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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   Google API Gateway

    File        :   Entity.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Entity.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.rest.schema.Resource;

public class Entity<T extends Entity> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final
  //////////////////////////////////////////////////////////////////////////////
  
  static final int PRIME = 31;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @JsonIgnore
  protected String version;

  /**
   ** UNIX time when the <code>Entity</code> was created.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty
  protected Long   createdAt;

  /**
   ** The username of the user who created the <code>Entity</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty
  protected String createdBy;

  /**
   ** UNIX time when the <code>Entity</code> was most recently updated.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("lastModifiedAt")
  protected Long   modifiedAt;

  /**
   ** The username of the user who most recently updated the
   ** <code>Entity</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("lastModifiedBy")
  protected String modifiedBy;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Attribute
  // ~~~~~ ~~~~~~~~~
  /**
   ** Stores extended attributes of the <code>Entity</code>.
   */
  public static class Attribute implements Resource<Attribute> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an new <code>Attribute</code> <code>Entity</code> attribute
     ** with the specified properties.
     **
     ** @param  name             the name of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  value            the value of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    @JsonCreator
    public Attribute(final @JsonProperty(value="name") String name, final @JsonProperty(value="value") String value) {
      // ensure inheritance
      super();

      // initialize instance
      this.name  = name;
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Attribute</code> attribute.
     **
     ** @param  value            the name of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Attribute</code> attribute to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Attribute</code>.
     */
    public final Attribute name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Attribute</code> attribute.
     **
     ** @return                  the name of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the value of the <code>Attribute</code> attribute.
     **
     ** @param  value            the value of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Attribute</code> attribute to allow
     **                          method chaining.
     **                          <br>
     **                          Possible object is <code>Attribute</code>.
     */
    public final Attribute value(final String value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Attribute</code> attribute.
     **
     ** @return                  the value of the <code>Attribute</code>
     **                          attribute.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String value() {
      return this.name;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Entity</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  Entity() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the version identifier of the <code>Entity</code>.
   **
   ** @param  value              the version identifier of the
   **                            <code>Entity</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Entity</code>.
   */
  @SuppressWarnings({"oracle.jdeveloper.java.unnecessary-cast", "unchecked"})
  public final T version(final String value) {
    this.version = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Returns the version identifier of the <code>Entity</code>.
   **
   ** @return                    the version identifier of the
   **                            <code>Entity</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdAt
  /**
   ** Sets the timestamp of when the REST object was created.
   **
   ** @param  value              the date and time the REST object was created.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"oracle.jdeveloper.java.unnecessary-cast", "unchecked"})
  public final T createdAt(final Long value) {
    this.createdAt = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdAt
  /**
   ** Returns the timestamp of when the REST object was created.
   **
   ** @return                    the date and time the REST object was created.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long createdAt() {
    return this.createdAt;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdBy
  /**
   ** Sets the account identifier whom created the REST object.
   **
   ** @param  value              the account identifier whom created the REST
   **                            object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"oracle.jdeveloper.java.unnecessary-cast", "unchecked"})
  public final T createdBy(final String value) {
    this.createdBy = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createdBy
  /**
   ** Returns the account identifier whom created the REST object.
   **
   ** @return                    the account identifier whom created the REST
   **                            object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String createdBy() {
    return this.createdBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedAt
  /**
   ** Sets the timestamp of when the REST object was last modified.
   **
   ** @param  value              the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"oracle.jdeveloper.java.unnecessary-cast", "unchecked"})
  public final T modifiedAt(final Long value) {
    this.modifiedAt = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedAt
  /**
   ** Returns the timestamp of when the REST object was last modified.
   **
   ** @return                    the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long modifiedAt() {
    return this.modifiedAt;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedBy
  /**
   ** Sets the account identifier whom modified the REST object last time.
   **
   ** @param  value              the account identifier whom modified the REST
   **                            object last time.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Entity</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"oracle.jdeveloper.java.unnecessary-cast", "unchecked"})
  public final T modifiedBy(final String value) {
    this.modifiedBy = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifiedBy
  /**
   ** Returns the account identifier whom modified the REST object last time.
   **
   ** @return                    the account identifier whom modified the REST
   **                            object last time.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String modifiedBy() {
    return this.modifiedBy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
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
    int result = this.version != null ? this.version.hashCode() : 0;
    result = PRIME * result + (this.createdAt  != null ? this.createdAt.hashCode()  : 0);
    result = PRIME * result + (this.createdBy  != null ? this.createdBy.hashCode()  : 0);
    result = PRIME * result + (this.modifiedAt != null ? this.modifiedAt.hashCode() : 0);
    result = PRIME * result + (this.modifiedBy != null ? this.modifiedBy.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entity</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entity</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Entity that = (Entity)other;
    if (this.createdAt != null ? !this.createdAt.equals(that.createdAt) : that.createdAt != null)
      return false;

    if (this.createdBy != null ? !this.createdBy.equals(that.createdBy) : that.createdBy != null)
      return false;

    if (this.modifiedAt != null ? !this.modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null)
      return false;

    return !(this.modifiedBy != null ? !this.modifiedBy.equals(that.modifiedBy) : that.modifiedBy != null);
  }
}