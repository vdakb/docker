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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import java.util.List;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class User
// ~~~~~ ~~~~
/**
 ** SCIM provides a resource type for <code>User</code> resources.
 ** <br>
 ** The core schema for <code>User</code> is identified using the URI's:
 ** <code>urn:scim:schemas:core:1.0</code>
 ** <code>urn:ietf:params:scim:schemas:core:2.0:User}</code>
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class User<T extends User> extends Entity<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("userName")
  @Attribute(description="Unique identifier for the User typically used by the user to directly authenticate to the service provider.", required=true, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.SERVER)
  private String                 userName;

  @JsonProperty("userType")
  @Attribute(description="Used to identify the organization to user relationship. Typical values used might be 'Contractor', 'Employee', 'Intern', 'Temp', 'External', and 'Unknown' but any value may be used.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 userType;

  @JsonProperty("active")
  @Attribute(description="A Boolean value indicating the User's administrative status.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private Boolean                active;

  @JsonProperty("password")
  @Attribute(description="The User's clear text password. This attribute is intended to be used as a means to specify an initial password when creating a new User or to reset an existing User's password.", required=false, caseExact=false, mutability=Definition.Mutability.WRITE_ONLY, returned=Definition.Returned.NEVER, uniqueness=Definition.Uniqueness.NONE)
  private String                 password;

  @JsonProperty("nickName")
  @Attribute(description="The casual way to address the user in real " + "life, e.g.'Bob' or 'Bobby' instead of 'Robert'. This attribute SHOULD " + "NOT be used to represent a User's username " + "(e.g., bjensen or mpepperidge)", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 nickName;

  @JsonProperty("displayName")
  @Attribute(description="The name of the User, suitable for display to end-users. The name SHOULD be the full name of the User being described if known.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 displayName;

  @JsonProperty("title")
  @Attribute(description="The user's title, such as \"Vice President\".", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 title;

  @JsonProperty("profileUrl")
  @Attribute(description="A fully qualified URL to a page representing the User's online profile", required=false, reference={ "external" }, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private URI                    profileURI;

  // Note that "name" is a complex attribute, so it is represented by the Name
  // class.
  @JsonProperty("name")
  @Attribute(description="The components of the user's real name.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private Name                   name;

  @JsonProperty("emails")
  @Attribute(description="E-mail addresses for the user. The value SHOULD be canonicalized by the Service Provider, e.g., bjensen@example.com instead of bjensen@EXAMPLE.COM. Canonical Type values of work, home, and other.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, multiValueClass=Email.class)
  private List<Email>            email;

  @JsonProperty("phoneNumbers")
  @Attribute(description="Phone numbers for the User. The value SHOULD be canonicalized by the Service Provider according to format in RFC3966 e.g., 'tel:+1-201-555-0123'. Canonical Type values of work, home, mobile, fax, pager and other.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, multiValueClass=PhoneNumber.class)
  private List<PhoneNumber>      phoneNumber;

  @JsonProperty("ims")
  @Attribute(description="Instant messaging addresses for the User.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, multiValueClass=InstantMessaging.class)
  private List<InstantMessaging> ims;

  @JsonProperty("addresses")
  @Attribute(description="Physical mailing addresses for this User.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, multiValueClass=Address.class)
  private List<Address>          address;

  @JsonProperty("photos")
  @Attribute(description="URIs of photos of the User.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE, multiValueClass=Photo.class)
  private List<Photo>            photo;

  @JsonProperty("preferredLanguage")
  @Attribute(description="Indicates the User's preferred written or spoken language. Generally used for selecting a localized User interface. e.g., 'en_US' specifies the language English and country US.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 preferredLanguage;

  @JsonProperty("locale")
  @Attribute(description="Used to indicate the User's default location for purposes of localizing items such as currency, date time format, numerical representations, etc.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 locale;

  @JsonProperty("timezone")
  @Attribute(description="The User's time zone in the 'Olson' timezone database format; e.g.,'America/Los_Angeles'", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String                 timeZone;

  @JsonProperty("groups")
  @Attribute(description="A list of groups that the user belongs to, either thorough direct membership, nested groups, or dynamically calculated.", required=false, mutability=Definition.Mutability.READ_ONLY, returned=Definition.Returned.DEFAULT, multiValueClass=Group.class)
  private List<Group>            group;

  @JsonProperty("roles")
  @Attribute(description="A list of roles for the User that collectively represent who the User is; e.g., 'Student', 'Faculty'.", required=false, returned=Definition.Returned.DEFAULT, multiValueClass=Role.class)
  private List<Role>             role;

  @JsonProperty("entitlements")
  @Attribute(description="A list of entitlements for the User that represent a thing the User has.", required=false, returned=Definition.Returned.DEFAULT, multiValueClass=Entitlement.class)
  private List<Entitlement>      entitlement;

  @JsonProperty("x509Certificates")
  @Attribute(description="A list of certificates issued to the User.", required=false, returned=Definition.Returned.DEFAULT, multiValueClass=Certificate.class)
  private List<Certificate>      certificate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty SCIM <code>User</code> that allows use as a JavaBean.
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
  // Method:   userName
  /**
   ** Sets the user name of the User, or Last Name in most Western languages
   ** (for example, Jensen given the full name Ms. Barbara J Jensen, III.).
   **
   ** @param  value              the unique identifier for the User typically
   **                            used by the user to directly authenticate to
   **                            the <code>Service Provider</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T userName(final String value) {
    this.userName = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userName
  /**
   ** Returns the unique identifier for the User typically used by the user
   ** to directly authenticate to the <code>Service Provider</code>.
   **
   ** @return                    the unique identifier for the User typically
   **                            used by the user to directly authenticate to
   **                            the <code>Service Provider</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userName() {
    return this.userName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userType
  /**
   ** Sets the string used to identify the organization to user relationship.
   ** Typical values used might be 'Contractor', 'Employee', 'Intern', 'Temp',
   ** 'External', and 'Unknown' but any value may be used.
   **
   ** @param  value              the string used to identify the organization to
   **                            user relationship.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T userType(final String value) {
    this.userType = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   userType
  /**
   ** Returns the string used to identify the organization to user relationship.
   ** Typical values used might be 'Contractor', 'Employee', 'Intern', 'Temp',
   ** 'External', and 'Unknown' but any value may be used.
   **
   ** @return                    the string used to identify the organization to
   **                            user relationship.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String userType() {
    return this.userType;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T active(final Boolean value) {
    this.active = value;
    return (T)this;
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
  // Method:   password
  /**
   ** Sets the User's clear text password.
   **
   ** @param  value              the User's clear text password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T password(final String value) {
    this.password = value;
    return (T)this;
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
  // Method:   nickName
  /**
   ** Sets the casual way to address the user in real life, for example, 'Bob'
   ** or 'Bobby' instead of 'Robert'.
   **
   ** @param  value              the casual way to address the user in real
   **                            life, for example, 'Bob' or 'Bobby' instead of
   **                            'Robert'.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T nickName(final String value) {
    this.nickName = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nickName
  /**
   ** Returns the casual way to address the user in real life, for example,
   ** 'Bob' or 'Bobby' instead of 'Robert'.
   **
   ** @return                    the casual way to address the user in real
   **                            life, for example, 'Bob' or 'Bobby' instead of
   **                            'Robert'.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String nickName() {
    return this.nickName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Sets the name of the User, suitable for display to end-users.
   **
   ** @param  value              the name of the User, suitable for display to
   **                            end-users.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T displayName(final String value) {
    this.displayName = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   displayName
  /**
   ** Returns the name of the User, suitable for display to end-users.
   **
   ** @return                    the name of the User, suitable for display to
   **                            end-users.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String displayName() {
    return this.displayName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   title
  /**
   ** Sets the user's title, such as "<code>Vice President</code>".
   **
   ** @param  value              the user's title, such as
   **                            "<code>Vice President</code>".
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T title(final String value) {
    this.title = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   title
  /**
   ** Returns the user's title, such as "<code>Vice President</code>".
   **
   ** @return                    the user's title, such as
   **                            "<code>Vice President</code>".
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String title() {
    return this.title;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileURI
  /**
   ** Sets the fully qualified URL to a page representing the User's online
   ** profile.
   **
   ** @param  value              the fully qualified URL to a page representing
   **                            the User's online profile.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T profileURI(final URI value) {
    this.profileURI = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileURI
  /**
   ** Returns the fully qualified URL to a page representing the User's online
   ** profile.
   **
   ** @return                    the fully qualified URL to a page representing
   **                            the User's online profile.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI profileURI() {
    return this.profileURI;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T name(final Name value) {
    this.name = value;
    return (T)this;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T email(final List<Email> value) {
    this.email = value;
    return (T)this;
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
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T phoneNumber(final List<PhoneNumber> value) {
    this.phoneNumber = value;
    return (T)this;
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
  // Method:   ims
  /**
   ** Sets the instant messaging addresses for the User.
   **
   ** @param  value              the instant messaging addresses for the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link InstantMessaging}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T ims(final List<InstantMessaging> value) {
    this.ims = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ims
  /**
   ** Returns the instant messaging addresses for the User.
   **
   ** @return                    the instant messaging addresses for the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link InstantMessaging}.
   */
  public final List<InstantMessaging> ims() {
    return this.ims;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Sets the physical mailing addresses for this User.
   **
   ** @param  value              the physical mailing addresses for this User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Address}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T address(final List<Address> value) {
    this.address = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Returns the physical mailing addresses for this User.
   **
   ** @return                    the physical mailing addresses for this User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Address}.
   */
  public final List<Address> address() {
    return this.address;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   photo
  /**
   ** Sets the URIs of photos of the User.
   **
   ** @param  value              the URIs of photos of the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Photo}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T photo(final List<Photo> value) {
    this.photo = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   photo
  /**
   ** Returns the URIs of photos of the User.
   **
   ** @return                    the URIs of photos of the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Photo}.
   */
  public final List<Photo> photo() {
    return this.photo;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredLanguage
  /**
   ** Sets the User's preferred written or spoken language.
   ** <br>
   ** Generally used for selecting a localized User interface. for example,
   ** 'en_US' specifies the language English and country US.
   **
   ** @param  value              the User's preferred written or spoken
   **                            language.
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T preferredLanguage(final String value) {
    this.preferredLanguage = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   preferredLanguage
  /**
   ** Returns the User's preferred written or spoken language.
   ** <br>
   ** Generally used for selecting a localized User interface. for example,
   ** 'en_US' specifies the language English and country US.
   **
   ** @return                    the User's preferred written or spoken
   **                            language.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String preferredLanguage() {
    return this.preferredLanguage;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Sets the User's default location for purposes of localizing items such
   ** as currency, date time format, numerical representations, etc.
   **
   ** @param  value              the User's default location.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T locale(final String value) {
    this.locale = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns the User's default location for purposes of localizing items such
   ** as currency, date time format, numerical representations, etc.
   **
   ** @return                    the User's default location.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String locale() {
    return this.locale;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Sets the User's time zone in the 'Olson' timezone database format;
   ** for example, 'America/Los_Angeles'.
   **
   ** @param  value              the User's time zone in the 'Olson' timezone
   **                            database format.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T timeZone(final String value) {
    this.timeZone = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   timeZone
  /**
   ** Returns the User's time zone in the 'Olson' timezone database format;
   ** for example, 'America/Los_Angeles'.
   **
   ** @return                    the User's time zone in the 'Olson' timezone
   **                            database format.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String timeZone() {
    return this.timeZone;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Sets the list of groups that the user belongs to, either thorough
   ** direct membership, nested groups, or dynamically calculated.
   **
   ** @param  value              the list of groups that the user belongs to.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Group}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T group(final List<Group> value) {
    this.group = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   group
  /**
   ** Returns the list of groups that the user belongs to, either thorough
   ** direct membership, nested groups, or dynamically calculated.
   **
   ** @return                    the list of groups that the user belongs to.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Group}.
   */
  public final List<Group> group() {
    return this.group;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Sets the list of roles for the User that collectively represent who
   ** the User is; for example, 'Student', 'Faculty'.
   **
   ** @param  value              the list of roles for the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Role}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T role(final List<Role> value) {
    this.role = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   role
  /**
   ** Returns the list of roles for the User that collectively represent who
   ** the User is; for example, 'Student', 'Faculty'.
   **
   ** @return                    the list of roles for the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Role}.
   */
  public final List<Role> role() {
    return this.role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Sets the list of entitlements for the User that represent a thing the
   ** User has.
   **
   ** @param  value              the list of entitlements for the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Entitlement}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entitlement(final List<Entitlement> value) {
    this.entitlement = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns the list of entitlements for the User that represent a thing the
   ** User has.
   **
   ** @return                    the list of entitlements for the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Entitlement}.
   */
  public final List<Entitlement> entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Sets the list of certificates issued to the User.
   **
   ** @param  value              the list of certificates issued to the User.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link Certificate}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T certificate(final List<Certificate> value) {
    this.certificate = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Returns the list of certificates issued to the User.
   **
   ** @return                    the list of certificates issued to the User.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link Certificate}.
   */
  public final List<Certificate> certificate() {
    return this.certificate;
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
    result = 31 * result + (this.userName          != null ? this.userName.hashCode()          : 0);
    result = 31 * result + (this.userType          != null ? this.userType.hashCode()          : 0);
    result = 31 * result + (this.active            != null ? this.active.hashCode()            : 0);
    result = 31 * result + (this.password          != null ? this.password.hashCode()          : 0);
    result = 31 * result + (this.nickName          != null ? this.nickName.hashCode()          : 0);
    result = 31 * result + (this.displayName       != null ? this.displayName.hashCode()       : 0);
    result = 31 * result + (this.title             != null ? this.title.hashCode()             : 0);
    result = 31 * result + (this.profileURI        != null ? this.profileURI.hashCode()        : 0);
    result = 31 * result + (this.name              != null ? this.name.hashCode()              : 0);
    result = 31 * result + (this.email             != null ? this.email.hashCode()             : 0);
    result = 31 * result + (this.phoneNumber       != null ? this.phoneNumber.hashCode()       : 0);
    result = 31 * result + (this.ims               != null ? this.ims.hashCode()               : 0);
    result = 31 * result + (this.address           != null ? this.address.hashCode()           : 0);
    result = 31 * result + (this.photo             != null ? this.photo.hashCode()             : 0);
    result = 31 * result + (this.preferredLanguage != null ? this.preferredLanguage.hashCode() : 0);
    result = 31 * result + (this.locale            != null ? this.locale.hashCode()            : 0);
    result = 31 * result + (this.timeZone          != null ? this.timeZone.hashCode()          : 0);
    result = 31 * result + (this.group             != null ? this.group.hashCode()             : 0);
    result = 31 * result + (this.role              != null ? this.role.hashCode()              : 0);
    result = 31 * result + (this.entitlement       != null ? this.entitlement.hashCode()       : 0);
    result = 31 * result + (this.certificate       != null ? this.certificate.hashCode()       : 0);
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

    final User that = (User)other;
    if (this.userName != null ? !this.userName.equals(that.userName) : that.userName != null)
      return false;

    if (this.userType != null ? !this.userType.equals(that.userType) : that.userType != null)
      return false;

    if (this.active != null ? !this.active.equals(that.active) : that.active != null)
      return false;

    if (this.password != null ? !this.password.equals(that.password) : that.password != null)
      return false;

    if (this.nickName != null ? !this.nickName.equals(that.nickName) : that.nickName != null)
      return false;

    if (this.displayName != null ? !this.displayName.equals(that.displayName) : that.displayName != null)
      return false;

    if (this.title != null ? !this.title.equals(that.title) : that.title != null)
      return false;

    if (this.profileURI != null ? !this.profileURI.equals(that.profileURI) : that.profileURI != null)
      return false;

    if (this.name != null ? !this.name.equals(that.name) : that.name != null)
      return false;

    if (this.email != null ? !this.email.equals(that.email) : that.email != null)
      return false;

    if (this.phoneNumber != null ? !this.phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
      return false;

    if (this.ims != null ? !this.ims.equals(that.ims) : that.ims != null)
      return false;

    if (this.address != null ? !this.address.equals(that.address) : that.address != null)
      return false;

    if (this.photo != null ? !this.photo.equals(that.photo) : that.photo != null)
      return false;

    if (this.preferredLanguage != null ? !this.preferredLanguage.equals(that.preferredLanguage) : that.preferredLanguage != null)
      return false;

    if (this.locale != null ? !this.locale.equals(that.locale) : that.locale != null)
      return false;

    if (this.timeZone != null ? !this.timeZone.equals(that.timeZone) : that.timeZone != null)
      return false;

    if (this.group != null ? !this.group.equals(that.group) : that.group != null)
      return false;

    if (this.role != null ? !this.role.equals(that.role) : that.role != null)
      return false;

    if (this.entitlement != null ? !this.entitlement.equals(that.entitlement) : that.entitlement != null)
      return false;

    return !(this.certificate != null ? !this.certificate.equals(that.certificate) : that.certificate != null);
  }
}