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

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.keycloak.schema;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// final class Role
// ~~~~~ ~~~~~ ~~~~
/**
 ** The REST realm role entity representation.
 ** <br>
 ** Roles identify a type or category of user. Admin, user, manager, and
 ** employee are all typical roles that may exist in an organization.
 ** Applications often assign access and permissions to specific roles rather
 ** than individual users as dealing with users can be too fine grained and
 ** hard to manage.
 ** <br>
 ** For example, the Admin Console has specific roles which give permission to
 ** users to access parts of the Admin Console UI and perform certain actions.
 ** <br>
 ** There is a global namespace for roles and each client also has its own
 ** dedicated namespace where roles can be defined.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ROLE)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Role extends Structural<Role> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unqiue identifier of the resource defined by a Service Provider
   ** administrator.
   */
  @Attribute
  @JsonProperty
  private String   containerId;
  /**
   ** The unqiue identifier of the resource defined by a Service Provider
   ** administrator.
   */
  @JsonProperty
  @Attribute(required=true)
  private String   name;
  /**
   ** The description of the resource defined by a Service Provider
   ** administrator.
   */
  @Attribute
  @JsonProperty
  private String   description;
  /**
   ** Any realm or client level role can be turned into a composite role. A
   ** composite role is a role that has one or more additional roles associated
   ** with it. When a composite role is mapped to the user, the user also gains
   ** the roles associated with that composite. This inheritance is recursive so
   ** any composite of composites also gets inherited.
   */
  @Attribute
  @JsonProperty
  private Boolean   composite;
  /**
   ** Client roles are basically a namespace dedicated to a client. Each client
   ** gets its own namespace. Client roles are managed under the Roles tab under
   ** each individual client. You interact with this UI the same way you do for
   ** realm-level roles.
   */
  @Attribute
  @JsonProperty
  private Boolean   clientRole;

  @Attribute
  @JsonProperty
  private Composite composites;

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
  // Method:   containerId
  /**
   ** Sets the container id of the <code>Role</code>.
   **
   ** @param  value              the container id of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role containerId(final String value) {
    this.containerId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   container
  /**
   ** Returns the container id of of the <code>Role</code>.
   **
   ** @return                    the container id of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String containerId() {
    return this.containerId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the name of the <code>Role</code>.
   **
   ** @param  value              the name of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role name(final String value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name of the <code>Role</code>.
   **
   ** @return                    the name of the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
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
  // Method:   clientRole
  /**
   ** Sets the client property of the <code>Role</code>.
   **
   ** @param  value              the client property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role clientRole(final Boolean value) {
    this.clientRole = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clientRole
  /**
   ** Returns the client property of the <code>Role</code>.
   **
   ** @return                    the client property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean clientRole() {
    return this.clientRole;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composite
  /**
   ** Sets the composite property of the <code>Role</code>.
   **
   ** @param  value              the composite property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role composite(final Boolean value) {
    this.composite = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composite
  /**
   ** Returns the composite property of the <code>Role</code>.
   **
   ** @return                    the composite property of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean composite() {
    return this.composite;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Role</code> as a payload of a request.
   **
   ** @param  id                 the identifier of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  name               the name of the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Role} as a payload of a request.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public static Role build(final String id, final String name) {
    return new Role().id(id).name(name);
  }

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
    return Objects.hash(this.id, this.containerId, this.name, this.clientRole, this.composite, this.description, this.composites);
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
   ** Two <code>Role</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Role</code>s may be
   ** different even though they contain the same set of names with the same
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

    final Role that = (Role)other;
    return Objects.equals(this.id,          that.id)
        && Objects.equals(this.containerId, that.containerId)
        && Objects.equals(this.name,        that.name)
        && Objects.equals(this.clientRole,  that.clientRole)
        && Objects.equals(this.composite,   that.composite)
        && Objects.equals(this.composites,  that.composites)
        && Objects.equals(this.description, that.description)
    ;
  }
}