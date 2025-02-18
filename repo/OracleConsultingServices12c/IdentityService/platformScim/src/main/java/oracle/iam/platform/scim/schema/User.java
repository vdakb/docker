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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   User.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    User.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.schema;

import java.util.Objects;
import java.util.Collection;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.platform.scim.annotation.Attribute;

import static oracle.iam.platform.scim.annotation.Attribute.Returned;
import static oracle.iam.platform.scim.annotation.Attribute.Mutability;
import static oracle.iam.platform.scim.annotation.Attribute.Uniqueness;

////////////////////////////////////////////////////////////////////////////////
// final class User
// ~~~~~ ~~~~~ ~~~~
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
  @Attribute(description="Unique identifier for the User typically used by the user to directly authenticate to the service provider.", required=true, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.SERVER)
  private String                       userName;

  @JsonProperty("userType")
  @Attribute(description="Used to identify the organization to user relationship. Typical values used might be 'Contractor', 'Employee', 'Intern', 'Temp', 'External', and 'Unknown' but any value may be used.", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                       userType;

  @JsonProperty("active")
  @Attribute(description="A Boolean value indicating the User's administrative status.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private Boolean                      active;

  @JsonProperty("password")
  @Attribute(description="The User's clear text password. This attribute is intended to be used as a means to specify an initial password when creating a new User or to reset an existing User's password.", required=false, caseExact=false, mutability=Mutability.WRITE_ONLY, returned=Returned.NEVER, uniqueness=Uniqueness.NONE)
  private String                       password;

  @JsonProperty("nickName")
  @Attribute(description="The casual way to address the user in real " + "life, e.g.'Bob' or 'Bobby' instead of 'Robert'. This attribute SHOULD " + "NOT be used to represent a User's username " + "(e.g., bjensen or mpepperidge)", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                       nickName;

  @JsonProperty("displayName")
  @Attribute(description="The name of the User, suitable for display to end-users. The name SHOULD be the full name of the User being described if known.", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                       displayName;

  @JsonProperty("title")
  @Attribute(description="The user's title, such as \"Vice President\".", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                       title;

  @JsonProperty("profileUrl")
  @Attribute(description="A fully qualified URL to a page representing the User's online profile", required=false, reference={Resource.EXTERNAL}, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private URI                          profileURI;

  // Note that "name" is a complex attribute, so it is represented by the Name
  // class.
  @JsonProperty("name")
  @Attribute(description="The components of the user's real name.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private Name                         name;

  @JsonProperty("emails")
  @Attribute(description="E-mail addresses for the user. The value SHOULD be canonicalized by the Service Provider, e.g., bjensen@example.com instead of bjensen@EXAMPLE.COM. Canonical Type values of work, home, and other.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=Email.class)
  private Collection<Email>            email;

  @JsonProperty("phoneNumbers")
  @Attribute(description="Phone numbers for the User. The value SHOULD be canonicalized by the Service Provider according to format in RFC3966 e.g., 'tel:+1-201-555-0123'. Canonical Type values of work, home, mobile, fax, pager and other.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=PhoneNumber.class)
  private Collection<PhoneNumber>      phoneNumber;

  @JsonProperty("ims")
  @Attribute(description="Instant messaging addresses for the User.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=InstantMessaging.class)
  private Collection<InstantMessaging> ims;

  @JsonProperty("addresses")
  @Attribute(description="Physical mailing addresses for this User.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=Address.class)
  private Collection<Address>           address;

  @JsonProperty("photos")
  @Attribute(description="URIs of photos of the User.", required=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE, multiValueClass=Photo.class)
  private Collection<Photo>             photo;

  @JsonProperty("preferredLanguage")
  @Attribute(description="Indicates the User's preferred written or spoken language. Generally used for selecting a localized User interface. Valid values are concatenation of the ISO 639-1 two letter language code, an underscore, and the ISO 3166-1 2 letter country code; e.g., \"en_US\" specifies the language English and country US.", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                        preferredLanguage;

  @JsonProperty("locale")
  @Attribute(description="Used to indicate the User's default location for purposes of localizing items such as currency, date time format, numerical representations, etc.", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                        locale;

  @JsonProperty("timezone")
  @Attribute(description="The User's time zone in the 'Olson' timezone database format; e.g.,'America/Los_Angeles'", required=false, caseExact=false, mutability=Mutability.READ_WRITE, returned=Returned.DEFAULT, uniqueness=Uniqueness.NONE)
  private String                        timeZone;

  @JsonProperty("groups")
  @Attribute(description="A list of groups that the user belongs to, either thorough direct membership, nested groups, or dynamically calculated. The values are meant to enable expression of common group or role based access control models, although no explicit authorization model is defined. It is intended that the semantics of group membership and any behavior or authorization granted as a result of membership are defined by the Service Provider. The Canonical types \"direct\" and \"indirect\" are defined to describe how the group membership was derived. Direct group membership indicates the User is directly associated with the group and SHOULD indicate that Consumers may modify membership through the Group Resource. Indirect membership indicates User membership is transitive or dynamic and implies that Consumers cannot modify indirect group membership through the Group resource but MAY modify direct group membership through the Group resource which MAY influence indirect memberships. If the SCIM Service Provider exposes a Group resource, the value MUST be the \\\"id\\\" attribute of the corresponding Group resources to which the user belongs. Since this attribute is read-only, group membership changes MUST be applied via the Group Resource.", required=false, mutability=Mutability.READ_ONLY, returned=Returned.DEFAULT, multiValueClass=Group.class)
  private Collection<Group>             group;

  @JsonProperty("roles")
  @Attribute(description="A list of roles for the User that collectively represent who the User is; e.g., 'Student', 'Faculty'.", required=false, returned=Returned.DEFAULT, multiValueClass=Role.class)
  private Collection<Role>              role;

  @JsonProperty("entitlements")
  @Attribute(description="A list of entitlements for the User that represent a thing the User has.", required=false, returned=Returned.DEFAULT, multiValueClass=Entitlement.class)
  private Collection<Entitlement>       entitlement;

  @JsonProperty("x509Certificates")
  @Attribute(description="A list of certificates issued to the User.", required=false, returned=Returned.DEFAULT, multiValueClass=Certificate.class)
  private Collection<Certificate>      certificate;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>User</code> SCIM Resource that allows use as a
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
  // Method:   Ctor
  /**
   ** Constructs a new <code>User</code> SCIM Resource, and sets the namespace
   ** if the class extending this one is annotated.
   **
   ** @param  id                 the ID fo the <code>User</code> SCIM
   **                            Resource.
   */
  public User(final String id) {
    // ensure inheritance
    super(id);
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Email}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T email(final Collection<Email> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Email}.
   */
  public final Collection<Email> email() {
    return this.email;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   phoneNumber
  /**
   ** Sets the phone numbers for the User.
   **
   ** @param  value              the phone numbers for the User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link PhoneNumber}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T phoneNumber(final Collection<PhoneNumber> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link PhoneNumber}.
   */
  public final Collection<PhoneNumber> phoneNumber() {
    return this.phoneNumber;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ims
  /**
   ** Sets the instant messaging addresses for the User.
   **
   ** @param  value              the instant messaging addresses for the User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link InstantMessaging}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T ims(final Collection<InstantMessaging> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type
   **                            {@link InstantMessaging}.
   */
  public final Collection<InstantMessaging> ims() {
    return this.ims;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   address
  /**
   ** Sets the physical mailing addresses for this User.
   **
   ** @param  value              the physical mailing addresses for this User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Address}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T address(final Collection<Address> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Address}.
   */
  public final Collection<Address> address() {
    return this.address;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   photo
  /**
   ** Sets the URIs of photos of the User.
   **
   ** @param  value              the URIs of photos of the User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Photo}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T photo(final Collection<Photo> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Photo}.
   */
  public final Collection<Photo> photo() {
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Group}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T group(final Collection<Group> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Group}.
   */
  public final Collection<Group> group() {
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Role}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T role(final Collection<Role> value) {
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
   **                            Possible object is {@link Collection} of where
   **                            each element is of type {@link Role}.
   */
  public final Collection<Role> role() {
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
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Entitlement}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T entitlement(final Collection<Entitlement> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Entitlement}.
   */
  public final Collection<Entitlement> entitlement() {
    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   certificate
  /**
   ** Sets the list of certificates issued to the User.
   **
   ** @param  value              the list of certificates issued to the User.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Certificate}.
   **
   ** @return                    the <code>User</code> to allow method chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public final T certificate(final Collection<Certificate> value) {
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
   **                            Possible object is {@link Collection} where
   **                            each element is of type {@link Certificate}.
   */
  public final Collection<Certificate> certificate() {
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
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.userName, this.userType, this.active, this.password, this.nickName, this.displayName, this.title, this.profileURI, this.name, this.email, this.phoneNumber, this.ims, this.address, this.photo, this.preferredLanguage, this.locale, this.timeZone, this.group , this.role, this.entitlement, this.certificate);
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
    // ensure inheritance
    return super.equals(that)
        && Objects.equals(this.userName,          that.userName)
        && Objects.equals(this.userType,          that.userType)
        && Objects.equals(this.active,            that.active)
        && Objects.equals(this.password,          that.password)
        && Objects.equals(this.nickName,          that.nickName)
        && Objects.equals(this.displayName,       that.displayName)
        && Objects.equals(this.title,             that.title)
        && Objects.equals(this.profileURI,        that.profileURI)
        && Objects.equals(this.name,              that.name)
        && Objects.equals(this.email,             that.email)
        && Objects.equals(this.phoneNumber,       that.phoneNumber)
        && Objects.equals(this.ims,               that.ims)
        && Objects.equals(this.address,           that.address)
        && Objects.equals(this.photo,             that.photo)
        && Objects.equals(this.preferredLanguage, that.preferredLanguage)
        && Objects.equals(this.locale,            that.locale)
        && Objects.equals(this.timeZone,          that.timeZone)
        && Objects.equals(this.group,             that.group)
        && Objects.equals(this.role,              that.role)
        && Objects.equals(this.entitlement,       that.entitlement)
        && Objects.equals(this.certificate,       that.certificate);
  }
}