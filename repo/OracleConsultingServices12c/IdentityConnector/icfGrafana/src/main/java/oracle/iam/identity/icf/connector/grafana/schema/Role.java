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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.grafana.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Mutability;
import oracle.iam.identity.icf.schema.Returned;

////////////////////////////////////////////////////////////////////////////////
// final class User
// ~~~~~ ~~~~~ ~~~~
/**
 ** The base REST role entity representation.
 ** <p>
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.GROUP)
@JsonIgnoreProperties({"created", "updated"})
public final class Role  extends Entity<Role> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The JSON element of the global system identifier attribute of a role.
   */
  public static final String UID          = "uid";
  /**
   ** The JSON element of the global indicator attribute of a role.
   */
  public static final String GLOBAL       = "global";
  /**
   ** The JSON element of the version attribute of a role.
   */
  public static final String VERSION      = "version";
  /**
   ** The JSON element of the display attribute of a role.
   */
  public static final String DISPLAY_NAME = "displayName";
  /**
   ** The JSON element of the description attribute of a role.
   */
  public static final String DESCRIPTION  = "description";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unique identifier for the <code>Organization</code> typically used as
   ** a label for the <code>Organization</code> by Service Provider.
   */
  @JsonProperty(NAME)
  @Attribute(value=Attribute.UNIQUE, required=true, mutability=Mutability.MUTABLE, returned=Returned.ALWAYS)
  protected String  name;

  /**
   ** The flag indicating if the role is global or not. If set to
   ** <code>false</code>, the default org ID of the authenticated user will be
   ** used from the request.
   */
  @Attribute
  @JsonProperty(GLOBAL)
  protected Boolean global;
  /**
   ** The version identifier of the <code>Role</code>. If not present, version
   ** <code>0</code> will be assigned to the role and returned in the response.
   */
  @Attribute
  @JsonProperty(VERSION)
  protected Long    version;
  /**
   ** The display name of the <code>Role</code> resource.
   */
  @Attribute
  @JsonProperty(DISPLAY_NAME)
  protected String  displayName;
  /**
   ** The description of the <code>Role</code> resource.
   */
  @Attribute
  @JsonProperty(DESCRIPTION)
  protected String  description;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Role</code> REST representation that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Role() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the unique identifier of the <code>Role</code>.
   **
   ** @param  value              the unique identifier of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   **
   ** @throws NullPointerException if the unique identifier <code>value</code>
   **                              is <code>null</code>.
   */
  public final Role name(final String value) {
    this.name = Objects.requireNonNull(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the unique identifier of the <code>Role</code>.
   **
   ** @return                    the unique identifier of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   global
  /**
   ** Whether the <code>Role</code> is global.
   **
   ** @return                    <code>true</code> if the <code>Role</code> is
   **                            global.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean global() {
    return this.global;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Sets the last recently active version of the <code>Role</code>.
   **
   ** @param  value              the last recently active version of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role version(final Long value) {
    this.version = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   version
  /**
   ** Returns the last recently active version of the <code>Role</code>.
   **
   ** @return                    the last recently active version of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long version() {
    return this.version;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the display name of the <code>Role</code>.
   **
   ** @param  value              the display name of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role displayName(final String value) {
    this.displayName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the display name of the <code>Role</code>.
   **
   ** @return                    the display name of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Sets the description of the <code>Role</code>.
   **
   ** @param  value              the description of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role description(final String value) {
    this.description = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   description
  /**
   ** Returns the description of the <code>Role</code>.
   **
   ** @return                    the description of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String description() {
    return this.description;
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
  public final int hashCode() {
    return Objects.hash(this.id, this.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a minimal <code>Role</code> resource.
   **
   ** @param  name               the unique identifier of the <code>Role</code>
   **                            (usually the value of the <code>NAME</code> for
   **                            the <code>Role</code> in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> populated with the given
   **                            <code>name</code>.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   **
   ** @throws NullPointerException if the public identifier <code>name</code> is
   **                              <code>null</code>.
   */
  public static Role build(final String name) {
    return new Role().name(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entities</code> are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Entities</code> may be different even though they contain the same
   ** set of properties with the same values, but in a different order.
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
  public final boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final Role that = (Role)other;
    return Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name);
  }
}