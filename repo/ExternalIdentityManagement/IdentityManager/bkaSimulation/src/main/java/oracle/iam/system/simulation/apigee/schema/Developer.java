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

    File        :   Developer.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Developer.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-28-01  DSteding    First release version
*/

package oracle.iam.system.simulation.apigee.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.core.JsonProcessingException;

import oracle.iam.system.simulation.scim.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class Developer
// ~~~~~ ~~~~~~~~~
/**
 ** Developers must register with an organization on Apigee Edge.
 ** <br>
 ** After they are registered, developers register their apps, choose the APIs
 ** they want to use, and receive the unique API credentials (consumer keys and
 ** secrets) needed to access your APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Developer extends Entity<Developer>{

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////
  
  /**
   ** Read-only alternate unique ID. Useful when querying developer analytics.
   */
  @JsonProperty("developerId")
  private String id;

  /** The developer status: 'active' or 'inactive'. */
  @JsonProperty
  private String status;

  /** The developer's email, used to unique identify the developer in Edge. */
  @JsonProperty
  private String email;

  /** The developer's username. Not used in Apigee */
  @JsonProperty
  private String userName;

  /** The first name of the developer. */
  @JsonProperty
  private String firstName;

  /** The last name of the developer. */
  @JsonProperty
  private String lastName;

  /** Read-only Apigee organization where the developer is registered. */
  @JsonProperty("organizationName")
  private String tenant;

  /**
   ** The collection of companies the <code>Developer</code> belongs too.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("companies")
  protected List<String>         company     = new ArrayList<String>();


  /**
   ** The collection of applications the <code>Developer</code> belongs too.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("apps")
  protected List<String>         application = new ArrayList<String>();

  /**
   ** The extended attributes of the <code>Developer</code>.
   ** <br>
   ** This property is read-only.
   */
  @JsonProperty("attributes")
  protected Collection<Attribute> attribute  = new ArrayList<Attribute>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Developer</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Developer() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the system identifier of the <code>Developer</code>.
   **
   ** @param  value              the system identifier of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer id(final String value) {
    this.id = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Returns the system identifier of the <code>Developer</code>.
   **
   ** @return                    the system identifier of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Sets the activation status of the <code>Developer</code>.
   **
   ** @param  value              the activation status of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer status(final String value) {
    this.status = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the activation status of the <code>Developer</code>.
   **
   ** @return                    the activation status of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email identifier of the User.
   **
   ** @param  value              the the email identifier of the User.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer email(final String value) {
    this.email = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email identifier of the User.
   **
   ** @return                    the email identifier of the User.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the login name of the <code>Developer</code>.
   **
   ** @param  value              the login name of the <code>Developer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer userName(final String value) {
    this.userName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the login name of the <code>Developer</code>.
   **
   ** @return                    the login name of the <code>Developer</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Sets the family name of the <code>Developer</code>, or Last Name in most
   ** Western languages (for example, Jensen given the full name Ms. Barbara J
   ** Jensen, III.).
   **
   ** @param  value              the family name of the <code>Developer</code>,
   **                            or Last Name in most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer lastName(final String value) {
    this.lastName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastName
  /**
   ** Returns the family name of the <code>Developer</code>, or Last Name in
   ** most Western languages (for example, Jensen given the full name Ms.
   ** Barbara J Jensen, III.).
   **
   ** @return                    the family name of the <code>Developer</code>,
   **                            or Last Name in most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String lastName() {
    return this.lastName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Sets the given name of the <code>Developer</code>, or First Name in most
   ** Western languages (for example, Barbara given the full name Ms. Barbara J
   ** Jensen, III.).
   **
   ** @param  value              the given name of the <code>Developer</code>,
   **                            or First Name in most Western languages.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer firstName(final String value) {
    this.firstName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   firstName
  /**
   ** Returns the given name of the <code>Developer</code>, or First Name in
   ** most Western languages (for example, Barbara given the full name Ms.
   ** Barbara J Jensen, III.).
   **
   ** @return                    the given name of the <code>Developer</code>,
   **                            or First Name in most Western languages.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String firstName() {
    return this.firstName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tenant
  /**
   ** Sets the organization name of the <code>Developer</code>.
   **
   ** @param  value              the organization name of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public Developer tenant(final String value) {
    this.tenant = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   organization
  /**
   ** Returns the organization name of the <code>Developer</code>.
   **
   ** @return                    the organization name of the
   **                            <code>Developer</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String tenant() {
    return this.tenant;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   company
  /**
   ** Sets the {@link List} of companies the <code>Developer</code> belongs
   ** too.
   **
   ** @param  value              the {@link List} of companies.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>v</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer company(final List<String> value) {
    this.company = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   company
  /**
   ** Returns the {@link List} of companies the <code>Developer</code> belongs
   ** too.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of companies.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> company() {
    return this.company;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Sets the {@link List} of applications the <code>Developer</code> belongs
   ** too.
   **
   ** @param  value              the {@link List} of applications.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the <code>v</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer application(final List<String> value) {
    this.application = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   application
  /**
   ** Returns the {@link List} of applications the <code>Developer</code> belongs
   ** too.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link List} of applications.
   **                            <br>
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   */
  public final List<String> application() {
    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Sets the {@link Collection} of <code>Attribute</code>s.
   **
   ** @param  value              the {@link Collection} of
   **                            <code>Attribute</code>s.
   **                            <br>
   **                            Allowed object is {@link Collection} of
   **                            <code>Attribute</code>.
   **
   ** @return                    the <code>Developer</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Developer</code>.
   */
  public final Developer attribute(final Collection<Attribute> value) {
    this.attribute = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns the {@link Collection} of <code>Attribute</code>s.
   ** <p>
   ** This accessor method returns a reference to the live collection, not a
   ** snapshot. Therefore any modification you make to the returned list will
   ** be present inside the JSON object.
   **
   ** @return                    the {@link Collection} of entries that make up
   **                            the result set.
   **                            <br>
   **                            Possible object is {@link Collection} of
   **                            <code>Attribute</code>.
   */
  public final Collection<Attribute> attribute() {
    return this.attribute;
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
    int result = super.hashCode();
    result = PRIME * result + (this.id         != null ? this.id.hashCode()         : 0);
    result = PRIME * result + (this.email      != null ? this.email.hashCode()      : 0);
    result = PRIME * result + (this.tenant     != null ? this.tenant.hashCode()     : 0);
    result = PRIME * result + (this.status     != null ? this.status.hashCode()     : 0);
    result = PRIME * result + (this.userName   != null ? this.userName.hashCode()   : 0);
    result = PRIME * result + (this.lastName   != null ? this.lastName.hashCode()   : 0);
    result = PRIME * result + (this.firstName  != null ? this.firstName.hashCode()  : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Developer</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>Developer</code>s may be different even though they contain the same
   ** set of names with the same values, but in a different order.
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

    final Developer that = (Developer)other;
    if (this.id != null ? !this.id.equals(that.id) : that.id != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.tenant != null ? !this.tenant.equals(that.tenant) : that.tenant != null)
      return false;

    if (this.status != null ? !this.status.equals(that.status) : that.status != null)
      return false;

    if (this.lastName != null ? !this.lastName.equals(that.lastName) : that.lastName != null)
      return false;

    if (this.firstName != null ? !this.firstName.equals(that.firstName) : that.firstName != null)
      return false;

    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.createdAt != null ? !this.createdAt.equals(that.createdAt) : that.createdAt != null)
      return false;

    if (this.createdBy != null ? !this.createdBy.equals(that.createdBy) : that.createdBy != null)
      return false;

    if (this.modifiedAt != null ? !this.modifiedAt.equals(that.modifiedAt) : that.modifiedAt != null)
      return false;

    return !(this.modifiedBy != null ? !this.modifiedBy.equals(that.modifiedBy) : that.modifiedBy != null);
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