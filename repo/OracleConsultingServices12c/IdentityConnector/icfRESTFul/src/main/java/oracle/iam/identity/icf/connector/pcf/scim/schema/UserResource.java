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
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   UserResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    UserResource.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.scim.schema;

import java.util.List;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.identity.icf.scim.annotation.Schema;
import oracle.iam.identity.icf.scim.annotation.Attribute;
import oracle.iam.identity.icf.scim.annotation.Definition;

import oracle.iam.identity.icf.scim.schema.Entity;

////////////////////////////////////////////////////////////////////////////////
// class UserResource
// ~~~~~ ~~~~~~~~~~~~
/**
 ** SCIM provides a resource type for <code>UserResource</code> resources that
 ** belongs to the Pivotal Cloud Foundry SCIM interface.
 ** <br>
 ** The core schema for <code>User</code> is identified using the URI:
 ** <code>urn:scim:schemas:core:1.0</code>
 ** <b>Note</b>:
 ** <br>
 ** Pivotal doesn't follow the recommendation of the RFC to extend the schema
 ** levaraging the extension mechanism. Instead to agree in the standards
 ** Pivotal creates its own user resource uder the hood of
 ** <code>urn:scim:schemas:core:1.0</code>.
 ** <br>
 ** Due to the violation and disagreement to standads by pivotal we are not able
 ** to use the standard classes provided by the SCIM implementation of the
 ** connector and needs to duplicate most of the code lines of the classes
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema(id="urn:scim:schemas:core:1.0", name="User", description="Pivotal Cloud Foundry User Account")
public class UserResource extends Entity<UserResource> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("userName")
  @Attribute(description="Unique identifier for the User typically used by the user to directly authenticate to the service provider.", required=true, uniqueness=Definition.Uniqueness.SERVER)
  private String            userName;

  @JsonProperty("active")
  @Attribute(description="A Boolean value indicating the User's administrative status.")
  private Boolean           active;

  @JsonProperty("verified")
  @Attribute(description="New users are automatically verified by default. Unverified users can be created by specifying verified: false. Becomes true when the user verifies their email address.")
  private Boolean           verified;

  @JsonProperty("password")
  @Attribute(description="The User's clear text password. This attribute is intended to be used as a means to specify an initial password when creating a new User or to reset an existing User's password.", mutability=Definition.Mutability.WRITE_ONLY, returned=Definition.Returned.NEVER)
  private String            password;

  @JsonProperty("origin")
  @Attribute(description="A user in UAA always belongs to a user store with an alias referred to as an origin. For example, users that are authenticated by the UAA itself with a username/password combination have their origin set to the value uaa.", required=true)
  private String            origin;

  @JsonProperty("zoneId")
  @Attribute(description="The Identity Zone this user belongs to. The value uaa refers to the default zone.", required=true)
  private String            zoneId;

  @JsonProperty("name")
  @Attribute(description="The components of the user's real name.")
  private Name              name;

  @JsonProperty("emails")
  @Attribute(description="E-mail addresses for the user. The value SHOULD be canonicalized by the Service Provider, e.g., bjensen@example.com instead of bjensen@example.com.", multiValueClass=Email.class)
  private List<Email>       email;

  @JsonProperty("phoneNumbers")
  @Attribute(description="Phone numbers for the User. The value SHOULD be canonicalized by the Service Provider according to format in RFC3966 e.g., 'tel:+1-201-555-0123'.", multiValueClass=PhoneNumber.class)
  private List<PhoneNumber> phoneNumber;

  @JsonProperty("passwordLastModified")
  @Attribute(description="The timestamp when this user's password was last changed.", mutability=Definition.Mutability.READ_ONLY)
  private Calendar          passwordLastModified;

  @JsonProperty("lastLogonTime")
  @Attribute(description="The unix epoch timestamp in milliseconds of when the user last authenticated. This field will be omitted from the response if the user has never authenticated.", mutability=Definition.Mutability.READ_ONLY)
  private Long              lastLogonTime;

  @JsonProperty("previousLogonTime")
  @Attribute(description="The unix epoch timestamp in milliseconds of when the user last authenticated. This field will be omitted from the response if the user has never authenticated.", mutability=Definition.Mutability.READ_ONLY)
  private Long              previousLogonTime;

  @JsonProperty("groups")
  @Attribute(description="A list of groups that the user belongs to, either through direct membership, nested groups, or dynamically calculated.", mutability=Definition.Mutability.READ_ONLY, multiValueClass=Group.class)
  private List<Group>       groups;

  @JsonProperty("approvals")
  @Attribute(description="A list of approval decisions made by this user. Approvals record the user's explicit approval or rejection for an application's request for delegated permissions.", mutability=Definition.Mutability.READ_ONLY, multiValueClass=Approval.class)
  private List<Approval>    approvals;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty SCIM <code>UserResource</code> resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public UserResource() {
  	// ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Sets the user name of the User, or Last Name in most Western languages
   ** (for example, Jensen given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the unique identifier for the User typically
   **                            used by the user to directly authenticate to
   **                            the Service Provider.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource userName(final String value) {
    this.userName = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the unique identifier for the User typically used by the user
   ** to directly authenticate to the Service Provider.
   **
   ** @return                    the unique identifier for the User typically
   **                            used by the user to directly authenticate to
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Sets the value indicating the User's administrative status.
   **
   ** @param  value              the value indicating the User's administrative
   **                            status.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource active(final Boolean value) {
    this.active = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   active
  /**
   ** Returns the value indicating the User's administrative status.
   **
   ** @return                    the value indicating the User's administrative
   **                            status.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean active() {
    return this.active;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verified
  /**
   ** Sets the value indicating the <code>User</code>'s e-Mail verification
   ** status.
   **
   ** @param  value              the value indicating the <code>User</code>'s
   **                            e-Mail verification status.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource verified(final Boolean value) {
    this.verified = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verified
  /**
   ** Returns the value indicating the <code>User</code>'s e-Mail verification
   ** status.
   **
   ** @return                    the value indicating the <code>User</code>'s
   **                            e-Mail verification status.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean verified() {
    return this.verified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Sets the User's clear text password.
   **
   ** @param  value              the User's clear text password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource password(final String value) {
    this.password = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   password
  /**
   ** Returns the User's clear text password.
   **
   ** @return                    the User's clear text password.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String password() {
    return this.password;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Sets the store with an alias referred to as an origin.
   **
   ** @param  value              the store with an alias referred to as an
   **                            origin.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource origin(final String value) {
    this.origin = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   origin
  /**
   ** Returns store with an alias referred to as an origin.
   **
   ** @return                    the store with an alias referred to as an
   **                            origin.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String origin() {
    return this.origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zone
  /**
   ** Sets the Identity Zone the user belongs to.
   **
   ** @param  value              the Identity Zone the user belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource zone(final String value) {
    this.zoneId = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   zone
  /**
   ** Returns the Identity Zone the user belongs to.
   **
   ** @return                    the Identity Zone the user belongs to.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String zone() {
    return this.zoneId;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Sets the components of the user's full name.
   **
   ** @param  value              the components of the user's full name.
   **                            <br>
   **                            Allowed object is {@link Name}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource name(final Name value) {
    this.name = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the components of the user's full name.
   **
   ** @return                    the components of the user's full name.
   **                            <br>
   **                            Possible object is {@link Name}.
   */
  public final Name name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Sets the email addresses for the user.
   **
   ** @param  value              the email addresses for the user.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Email}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource email(final List<Email> value) {
    this.email = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   email
  /**
   ** Returns the email addresses for the user.
   **
   ** @return                    the email addresses for the user.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Email}.
   */
  public final List<Email> email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Sets the phone numbers for the User.
   **
   ** @param  value              the phone numbers for the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link PhoneNumber}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource phoneNumber(final List<PhoneNumber> value) {
    this.phoneNumber = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Returns the phone numbers for the User.
   **
   ** @return                    the phone numbers for the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link PhoneNumber}.
   */
  public final List<PhoneNumber> phoneNumber() {
    return this.phoneNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   updated
  /**
   ** Sets the Date this approval was last updated.
   **
   ** @param  value              the timestamp when this user's password was
   **                            last changed.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource updated(final Calendar value) {
    this.passwordLastModified = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   passwordLastModified
  /**
   ** Returns the timestamp when this user's password was last changed.
   **
   ** @return                    the timestamp when this user's password was
   **                            last changed.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar passwordLastModified() {
    return this.passwordLastModified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastLogonTime
  /**
   ** Sets the unix epoch timestamp in milliseconds of when the user last
   ** authenticated.
   **
   ** @param  value              the unix epoch timestamp in milliseconds of
   **                            when the user last authenticated.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource lastLogonTime(final Long value) {
    this.lastLogonTime = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lastLogonTime
  /**
   ** Returns the unix epoch timestamp in milliseconds of when the user last
   ** authenticated.
   **
   ** @return                    the unix epoch timestamp in milliseconds of
   **                            when the user last authenticated.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long lastLogonTime() {
    return this.lastLogonTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   previousLogonTime
  /**
   ** Sets the unix epoch timestamp in milliseconds of when the user last
   ** authenticated.
   **
   ** @param  value              the unix epoch timestamp in milliseconds of
   **                            when the user last authenticated.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource previousLogonTime(final Long value) {
    this.previousLogonTime = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   previousLogonTime
  /**
   ** Returns the unix epoch timestamp in milliseconds of when the user last
   ** authenticated.
   **
   ** @return                    the unix epoch timestamp in milliseconds of
   **                            when the user last authenticated.
   **                            <br>
   **                            Possible object is {@link Long}.
   */
  public final Long previousLogonTime() {
    return this.previousLogonTime;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groups
  /**
   ** Sets the list of groups that the user belongs to, either through
   ** direct membership, nested groups, or dynamically calculated.
   **
   ** @param  value              the list of groups that the user belongs to.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Group}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource groups(final List<Group> value) {
    this.groups = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groups
  /**
   ** Returns the list of groups that the user belongs to, either through
   ** direct membership, nested groups, or dynamically calculated.
   **
   ** @return                    the list of groups that the user belongs to.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Group}.
   */
  public final List<Group> groups() {
    return this.groups;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   approvals
  /**
   ** Sets the list of approvals that the user made.
   **
   ** @param  value              the list of approvals that the user made.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Approval}.
   **
   ** @return                    the <code>UserResource</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>UserResource</code>.
   */
  public final UserResource approvals(final List<Approval> value) {
    this.approvals = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   approval
  /**
   ** Returns the list of approvals that the user made.
   **
   ** @return                    the list of approvals that the user made.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Approval}.
   */
  public final List<Approval> approval() {
    return this.approvals;
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
   */
  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (this.userName             != null ? this.userName.hashCode()             : 0);
    result = 31 * result + (this.active               != null ? this.active.hashCode()               : 0);
    result = 31 * result + (this.verified             != null ? this.verified.hashCode()             : 0);
    result = 31 * result + (this.password             != null ? this.password.hashCode()             : 0);
    result = 31 * result + (this.origin               != null ? this.origin.hashCode()               : 0);
    result = 31 * result + (this.zoneId               != null ? this.zoneId.hashCode()               : 0);
    result = 31 * result + (this.name                 != null ? this.name.hashCode()                 : 0);
    result = 31 * result + (this.email                != null ? this.email.hashCode()                : 0);
    result = 31 * result + (this.phoneNumber          != null ? this.phoneNumber.hashCode()          : 0);
    result = 31 * result + (this.passwordLastModified != null ? this.passwordLastModified.hashCode() : 0);
    result = 31 * result + (this.lastLogonTime        != null ? this.lastLogonTime.hashCode()        : 0);
    result = 31 * result + (this.previousLogonTime    != null ? this.previousLogonTime.hashCode()    : 0);
    result = 31 * result + (this.groups               != null ? this.groups.hashCode()               : 0);
    result = 31 * result + (this.approvals            != null ? this.approvals.hashCode()            : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>UserResource</code>s filter are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>UserResource</code>s filter may be different even though they
   ** contain the same set of names with the same values, but in a different
   ** order.
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

    // ensure inheritance
    if (!super.equals(other))
      return false;

    final UserResource that = (UserResource)other;
    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.active != null ? !this.active.equals(that.active) : that.active != null)
      return false;

    if (this.verified != null ? !this.verified.equals(that.verified) : that.verified != null)
      return false;

    if (this.password != null ? !this.password.equals(that.password) : that.password != null)
      return false;

    if (this.origin != null ? !this.origin.equals(that.origin) : that.origin != null)
      return false;

    if (this.zoneId != null ? !this.zoneId.equals(that.zoneId) : that.zoneId != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.phoneNumber != null ? !this.phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
      return false;

    if (this.passwordLastModified != null ? !this.passwordLastModified.equals(that.passwordLastModified) : that.passwordLastModified != null)
      return false;

    if (this.lastLogonTime != null ? !this.lastLogonTime.equals(that.lastLogonTime) : that.lastLogonTime != null)
      return false;

    if (this.previousLogonTime != null ? !this.previousLogonTime.equals(that.previousLogonTime) : that.previousLogonTime != null)
      return false;

    if (this.groups != null ? !this.groups.equals(that.groups) : that.groups != null)
      return false;

    return !(this.approvals != null ? !this.approvals.equals(that.approvals) : that.approvals != null);
  }
}