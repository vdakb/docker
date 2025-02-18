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

    File        :   User.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.system.simulation.scim.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 **
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonIgnoreProperties(value={"version", "createdBy", "lastModifiedBy"})
public class User extends Entity<User> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  @JsonProperty
  private String   emailId;

  @JsonProperty
  private String   firstName;

  @JsonProperty
  private String   lastName;

  @JsonProperty
  private String   password;

  @JsonProperty("roles")
  private UserRole role;

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
   ** Sets the email identifier of the User.
   **
   ** @param  value              the the email identifier of the User.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User emailId(final String value) {
    this.emailId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   emailId
  /**
   ** Returns the email identifier of the User.
   **
   ** @return                    the email identifier of the User.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String emailId() {
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
  public User lastName(final String value) {
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
  public String lastName() {
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
  public User firstName(final String value) {
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
  public String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the password for the User.
   **
   ** @param  value              the password for the User.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public User password(final String value) {
    this.password = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns tthe password for the User.
   **
   ** @return                    the password for the User.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the userroles value collection.
   **
   ** @param  value              the userroles value collection.
   **                            <br>
   **                            Allowed object is {@link UserRole}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>User</code>.
   */
  public final User role(final UserRole value) {
    this.role = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the userroles value collection.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the userroles value collection.
   **                            <br>
   **                            Possible object is {@link UserRole}.
   */
  public final UserRole role() {
    return this.role;
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
    int result = super.hashCode();
    result = PRIME * result + (this.lastName  != null ? this.lastName.hashCode() : 0);
    result = PRIME * result + (this.firstName != null ? this.firstName.hashCode() : 0);
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
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final User that = (User)other;
    if (this.lastName != null ? !this.lastName.equals(that.lastName) : that.lastName != null)
      return false;

    if (this.firstName != null ? !this.firstName.equals(that.firstName) : that.firstName != null)
      return false;

    return super.equals(other);
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
    try {
      return Support.objectWriter().writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}