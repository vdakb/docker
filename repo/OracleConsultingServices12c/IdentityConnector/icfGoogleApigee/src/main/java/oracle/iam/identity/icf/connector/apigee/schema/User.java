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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Google Apigee Edge Connector

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-21-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.apigee.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.schema.Schema;
import oracle.iam.identity.icf.schema.Attribute;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** The <code>User</code> REST resource in Google Apigee Edge.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(Schema.ACCOUNT)
public class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The JSON tag name of the <code>emailId</code> attribute */
  public static final String EMAIL     = "emailId";

  /** The JSON tag name of the <code>lastName</code> attribute */
  public static final String LASTNAME  = "lastName";

  /** The JSON tag name of the <code>firstName</code> attribute */
  public static final String FIRSTNAME = "firstName";

  /** The JSON tag name of the <code>password</code> attribute */
  public static final String PASSWORD  = "password";

  /** The JSON tag name of the <code>roles</code> attribute */
  public static final String ROLES     = "roles";

  /** The JSON tag name of the <code>role</code> attribute */
  public static final String ROLE      = "role";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Attribute
  @JsonProperty(EMAIL)
  private String         emailId;

  @Attribute
  @JsonProperty(FIRSTNAME)
  private String         firstName;

  @Attribute
  @JsonProperty(LASTNAME)
  private String         lastName;

  @Attribute
  @JsonProperty(PASSWORD)
  private String         password;

  @Attribute
  @JsonProperty(ROLES)
  private Roles          roles;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Role
  // ~~~~~ ~~~~
  /**
   ** Stores role membership a user belongs to, either through direct
   ** membership.
   ** <br>
   ** The values are meant to enable expression of common group or role based
   ** access control models, although no explicit authorization model is
   ** defined. It is intended that the semantics of group membership and any
   ** behavior or authorization granted as a result of membership are defined by
   ** the Service Provider.
   */
  public static class Role {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty("organization")
    protected String tenant;

    @JsonProperty("name")
    protected String name;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Role</code> REST Resource that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Role() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: tenant
    /**
     ** Sets the organization name of the <code>Userrole</code>.
     **
     ** @param  value              the organization name of the
     **                            <code>Userrole</code>.
     **                            <br>
     **                            Allowed object is {@link String}.
     **
     ** @return                    the <code>MemberOf</code> to allow method
     **                            chaining.
     **                            <br>
     **                            Possible object is <code>MemberOf</code>.
     */
    public final Role tenant(final String value) {
      this.tenant = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: tenant
    /**
     ** Returns the tenant name of the <code>Userrole</code>.
     **
     ** @return                    the organization name of the
     **                            <code>Userrole</code>.
     **                            <br>
     **                            Possible object is {@link String}.
     */
    public final String tenant() {
      return this.tenant;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Sets the name of the <code>Role</code>.
     **
     ** @param  value            the name of the <code>Role</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>MemberOf</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>MemberOf</code>.
     */
    public final Role name(final String value) {
      this.name = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the <code>Role</code>.
     **
     ** @return                  the name of the <code>Role</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
      int result = super.hashCode();
      result = PRIME * result + (this.tenant != null ? this.tenant.hashCode() : 0);
      result = PRIME * result + (this.name   != null ? this.name.hashCode()   : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>MemberOf</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>MemberOf</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different
     ** order.
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

      final Role that = (Role)other;
      if (this.tenant != null ? !this.tenant.equals(that.tenant) : that.tenant != null)
        return false;

      if (this.name != null ? !this.name.equals(that.name) : that.name != null)
        return false;

      return super.equals(other);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Roles
  // ~~~~~ ~~~~~
  /**
   ** Stores role membership a user belongs to, either through direct
   ** membership.
   ** <br>
   ** The values are meant to enable expression of common group or role based
   ** access control models, although no explicit authorization model is
   ** defined. It is intended that the semantics of group membership and any
   ** behavior or authorization granted as a result of membership are defined by
   ** the Service Provider.
   */
  public static class Roles {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @Attribute
    @JsonProperty(ROLE)
    private List<Role>     role;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Roles</code> REST Resource that allows use as
     ** a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Roles() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: role
    /**
     ** Sets the list of role that the user belongs to, either through direct
     ** membership.
     **
     ** @param  value              the list of role that the user belongs to.
     **                            <br>
     **                            Allowed object is {@link List} of
     **                            {@link Role}.
     **
     ** @return                    the <code>User</code> to allow method chaining.
     **                            <br>
     **                            Possible object is <code>User</code> for type
     **                            <code>T</code>.
     */
    public final Roles role(final List<Role> value) {
      this.role = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   role
    /**
     ** Returns the list of roles that the user belongs to through direct
     ** membership.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the JSON object.
     **
     ** @return                  the list of roles for the User.
     **                          <br>
     **                          Possible object is {@link List} of
     **                          {@link Role}.
     */
    public final List<Role> role() {
      return this.role;
    }

  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> REST Resource that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public User() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emailId
  /**
   ** Sets the emailId of the <code>User</code>.
   **
   ** @param  value              the emailId of the <code>User</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User emailId(final String value) {
    this.emailId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emailId
  /**
   ** Returns the emailId of the <code>User</code>.
   **
   ** @return                    the emailId of the <code>User</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String emailId() {
    return this.emailId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the family name of the User, or Last Name in most Western languages
   ** (for example, Jensen given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the family name of the User, or Last Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User lastName(final String value) {
    this.lastName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Returns the family name of the User, or Last Name in most Western
   ** languages (for example, Jensen given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the family name of the User, or Last Name in
   **                            most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String lastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the given name of the User, or First Name in most Western languages
   ** (for example, Barbara given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User firstName(final String value) {
    this.firstName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Returns the given name of the User, or First Name in most Western
   ** languages (for example, Barbara given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the password of the User.
   **
   ** @param  value              the password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User password(final String value) {
    this.password = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the given name of the User, or First Name in most Western
   ** languages (for example, Barbara given the full name Ms. Barbara J Jensen,
   ** III.).
   **
   ** @return                    the given name of the User, or First Name in
   **                            most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   ** Sets the collection of role that the user belongs to through direct
   ** membership.
   **
   ** @param  value              the list of role that the user belongs to.
   **                            <br>
   **                            Allowed object is {@link Roles}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code> for type
   **                            <code>T</code>.
   */
  public final User roles(final Roles value) {
    this.roles = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roles
  /**
   ** Returns the collection of role that the user belongs to through direct
   ** membership.
   **
   ** @return                    the collection of roles for the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Role}.
   */
  public final Roles role() {
    return this.roles;
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
  public final int hashCode() {
    int result = this.emailId != null ? this.emailId.hashCode() : 0;
    result = PRIME * result + (this.lastName  != null ? this.lastName.hashCode()  : 0);
    result = PRIME * result + (this.firstName != null ? this.firstName.hashCode() : 0);
    result = PRIME * result + (this.password  != null ? this.password.hashCode()  : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>User</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>User</code>s may be
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
  public final boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final User that = (User)other;
    if (this.emailId != null ? !this.emailId.equals(that.emailId) : that.emailId != null)
      return false;

    if (this.lastName != null ? !this.lastName.equals(that.lastName) : that.lastName != null)
      return false;

    if (this.firstName != null ? !this.firstName.equals(that.firstName) : that.firstName != null)
      return false;

    return !(this.password != null ? !this.password.equals(that.password) : that.password != null);
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
  public final String toString() {
    return this.emailId;
  }
}