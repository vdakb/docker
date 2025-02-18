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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Apache Archiva Connector

    File        :   Role.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Role.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.archiva.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Resource;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class Role
// ~~~~~ ~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.GROUP)
public class Role implements Resource<Role> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty
  private String           name;

  @Attribute
  @JsonProperty
  private String           description;

  @Attribute
  @JsonProperty
  private Boolean          assignable;

  @Attribute
  @JsonProperty
  private Boolean          permanent;

  @Attribute
  @JsonProperty("users")
  private List<String>     user;

  @Attribute
  @JsonProperty("otherUsers")
  private List<String>     other;

  @Attribute
  @JsonProperty("removedUsers")
  private List<String>     removed;

  @Attribute
  @JsonProperty("childRoleNames")
  private List<String>     subordinated;

  @Attribute
  @JsonProperty("parentRoleNames")
  private List<String>     superordinated;

  @Attribute
  @JsonProperty("parentsRolesUsers")
  private List<String>     superordinatedUser;

  @Attribute
  @JsonProperty("permissions")
  private List<Permission> permission;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Role</code> REST Resource that allows use
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
   ** Sets the identifier of the <code>Role</code>.
   **
   ** @param  value              the identifier of the <code>Role</code>.
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
   ** Returns the identifier of the <code>Role</code>.
   **
   ** @return                    the identifier of the <code>Role</code>.
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
  // Method:   assignable
  /**
   ** Sets the assignable behavior of the <code>Role</code>.
   **
   ** @param  value              the assignable behavior of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role assignable(final Boolean value) {
    this.assignable = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assignable
  /**
   ** Returns the assignable behavior of the <code>Role</code>.
   **
   ** @return                    the assignable behavior of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean assignable() {
    return this.assignable;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permanent
  /**
   ** Sets the permanent behavior of the <code>Role</code>.
   **
   ** @param  value              the permanent behavior of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public final Role permanent(final Boolean value) {
    this.permanent = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permanent
  /**
   ** Returns the permanent behavior of the <code>Role</code>.
   **
   ** @return                    the permanent behavior of the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean permanent() {
    return this.permanent;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Sets the user associated with the <code>Role</code>.
   **
   ** @param  value              the user associated with the <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role user(final List<String> value) {
    this.user = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   user
  /**
   ** Returns the user associated with the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the user associated with the <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> user() {
    return this.user;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   other
  /**
   ** Sets the other users associated with the <code>Role</code>.
   **
   ** @param  value              the other users associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role other(final List<String> value) {
    this.other = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   other
  /**
   ** Returns the other users associated with the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the other users associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> other() {
    return this.other;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   remove
  /**
   ** Sets the removed users associated with the <code>Role</code>.
   **
   ** @param  value              the removed users associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role removed(final List<String> value) {
    this.removed = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removed
  /**
   ** Returns the removed users associated with the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the removed users associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> removed() {
    return this.removed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subordinated
  /**
   ** Sets the subordinated roles associated with the <code>Role</code>.
   **
   ** @param  value              the subordinated roles associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role subordinated(final List<String> value) {
    this.subordinated = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subordinated
  /**
   ** Returns the subordinated roles associated with the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the subordinated roles associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> subordinated() {
    return this.subordinated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superordinated
  /**
   ** Sets the superordinated roles associated with the <code>Role</code>.
   **
   ** @param  value              the superordinated roles associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role superordinated(final List<String> value) {
    this.superordinated = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superordinated
  /**
   ** Returns the superordinated roles associated with the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the superordinated roles associated with the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> superordinated() {
    return this.superordinated;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superordinatedUser
  /**
   ** Sets the users associated with the superordinated <code>Role</code> of
   ** this <code>Role</code>.
   **
   ** @param  value              the users associated with the superordinated
   **                            <code>Role</code> of this <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role superordinatedUser(final List<String> value) {
    this.superordinatedUser = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   superordinatedUser
  /**
   ** Returns the users associated with the superordinated <code>Role</code> of
   ** this <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the users associated with the superordinated
   **                            <code>Role</code> of this <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public List<String> superordinatedUser() {
    return this.superordinatedUser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Sets the permission granted to the <code>Role</code>.
   **
   ** @param  value              the permission granted to the
   **                            <code>Role</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Permission}.
   **
   ** @return                    the <code>Role</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Role</code>.
   */
  public Role permission(final List<Permission> value) {
    this.permission = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   permission
  /**
   ** Returns the permission granted to the <code>Role</code>.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the permission granted to the
   **                            <code>Role</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Permission}.
   */
  public List<Permission> permission() {
    return this.permission;
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
    int result = this.name != null ? this.name.hashCode() : 0;
    result = 31 * result + (this.description        != null ? this.description.hashCode()        : 0);
    result = 31 * result + (this.assignable         != null ? this.assignable.hashCode()         : 0);
    result = 31 * result + (this.permanent          != null ? this.permanent.hashCode()          : 0);
    result = 31 * result + (this.user               != null ? this.user.hashCode()               : 0);
    result = 31 * result + (this.other              != null ? this.other.hashCode()              : 0);
    result = 31 * result + (this.removed            != null ? this.removed.hashCode()            : 0);
    result = 31 * result + (this.subordinated       != null ? this.subordinated.hashCode()       : 0);
    result = 31 * result + (this.superordinated     != null ? this.superordinated.hashCode()     : 0);
    result = 31 * result + (this.superordinatedUser != null ? this.superordinatedUser.hashCode() : 0);
    result = 31 * result + (this.permission         != null ? this.permission.hashCode()         : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>UserResult</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>UserResult</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different
   ** order.
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
    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.assignable != null ? !this.assignable.equals(that.assignable) : that.assignable != null)
      return false;

    if (this.permanent != null ? !this.permanent.equals(that.permanent) : that.permanent != null)
      return false;

    if (this.user != null ? !this.user.equals(that.user) : that.user != null)
      return false;

    if (this.other != null ? !this.other.equals(that.other) : that.other != null)
      return false;

    if (this.removed != null ? !this.removed.equals(that.removed) : that.removed != null)
      return false;

    if (this.subordinated != null ? !this.subordinated.equals(that.subordinated) : that.subordinated != null)
      return false;

    if (this.superordinated != null ? !this.superordinated.equals(that.superordinated) : that.superordinated != null)
      return false;

    if (this.superordinatedUser != null ? !this.superordinatedUser.equals(that.superordinatedUser) : that.superordinatedUser != null)
      return false;

    return !(this.permission != null ? !this.permission.equals(that.permission) : that.permission != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("{\"name\":\"").append(this.name).append("\"");
    builder.append(",\"description\":\"").append(this.description).append("\"");
    builder.append(",\"permanent\":\"").append(this.permanent).append("\"");
    builder.append("}");
    return builder.toString();
  }
}